//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilidad para escribir archivos de una determinada
 * cantidad de lineas.
 */
public class SplitedFileWriter extends Writer {

	private static final String FILE_SEPARATOR = "_";
	
	private String fileName;
	private Long rows;
	private File currentFile;
	//private FileWriter currentFileWriter;
	private BufferedWriter currentBuffer;
	private Long rowCounter;
	private int fileCounter;
	private List<File> listSplitedFiles;


	/**
	 * Constructor
	 * 
	 * @param fileName: Nombre del archivo
	 * @param row:		Cantidad maxima de lineas por archivo
	 */
 	public SplitedFileWriter(String fileName, Long rows) throws IOException  {
		this.fileName = fileName;
		this.rows = rows;
		this.currentFile = new File(fileName);
		//this.currentFileWriter = new FileWriter(this.currentFile, false);
		//this.currentBuffer = new BufferedWriter(this.currentFileWriter);
		this.currentBuffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.currentFile, false), "ISO-8859-1"));
		this.rowCounter = 0L;
		this.fileCounter = 1;
		this.listSplitedFiles = new ArrayList<File>();
	}

 	/**
 	 * Escribe una string
 	 */
	public synchronized void write(String data) throws IOException {
		String[] tmp = data.split("\n");
		boolean flag = data.endsWith("\n");

		if ((tmp.length > 1) || flag) {
			int i=0;
			while(i < tmp.length - 1) {
				bufferWrite(tmp[i++]);
				newLine();
			}
			bufferWrite(tmp[i]);
			if (flag) newLine();
		} 
		else {
			bufferWrite(data);
		}
	}
	
 	/**
 	 * Escribe una string, seguida de un separador de linea
 	 */
	public synchronized void writeln(String data) throws IOException {
		this.write(data);
		this.newLine();
	}
	
	/**
 	 * Escribe un separador de linea
 	 */
	public synchronized void newLine() throws IOException {
		this.currentBuffer.newLine();
		this.rowCounter++;
	}

 	/**
 	 * Cierra el stream
 	 */
	public synchronized void close() throws IOException {
		this.currentBuffer.close();
		this.listSplitedFiles.add(this.currentFile);
	}	

	/**
	 * Retorna la lista de archivos generados
	 */
	public synchronized  List<File> getListSplitedFiles() {
		return this.listSplitedFiles;
	}
	

	// Metodos Privados
	
	private String createFileName(String fileName) {
		String  name = fileName;
		String[] tmp = name.split("\\.");
		if (tmp.length > 1) {
			name = tmp[0] + FILE_SEPARATOR + this.fileCounter + "." +tmp[1]; 
		} else {
			name += FILE_SEPARATOR + this.fileCounter;
		}
		return name;
	}	
	
	private void bufferWrite(String data) throws IOException {
		if (this.rowCounter.equals(this.rows)) {
			this.currentBuffer.close();
			File auxFile = new File(createFileName(this.fileName));
			this.listSplitedFiles.add(auxFile);
			this.currentFile.renameTo(auxFile);
			this.fileCounter++;
			this.currentFile = new File(createFileName(this.fileName));
			//this.currentFileWriter = new FileWriter(this.currentFile, false);
			//this.currentBuffer = new BufferedWriter(this.currentFileWriter);
			this.currentBuffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.currentFile, false), "ISO-8859-1"));
			this.rowCounter = 0L;
		}
		this.currentBuffer.write(data);
	}

	@Override
	public void flush() throws IOException {
		this.currentBuffer.flush();		
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		bufferWrite(new String(cbuf));
	}

}