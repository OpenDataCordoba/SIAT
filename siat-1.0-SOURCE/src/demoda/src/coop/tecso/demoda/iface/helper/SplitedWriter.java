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

public class SplitedWriter extends Writer{

	private static final String FILENAME_SEPARATOR = "_";

	private String fileName;
	private long chunckSize;
	private char[] header;
	private char[] footer;
	private File currentFile;
	private BufferedWriter currentBuffer;
	private int fileCounter;
	private List<File> listSplitedFiles;
	private long currentSize;
	
	
	// Constructores
	public SplitedWriter(String fileName, long chunkSize, boolean append) throws IOException  {
		new SplitedWriter(fileName, chunkSize, null, null, append);
	}
	
	public SplitedWriter(String fileName, long chunkSize, char[] header, boolean append) throws IOException  {
		new SplitedWriter(fileName, chunkSize, header, null, append);
	}
	
	public SplitedWriter(String fileName, long chunkSize, char[] header, char[] footer) throws IOException  {
		new SplitedWriter(fileName, chunkSize, header, footer, false);
	}
	
	public SplitedWriter(String fileName, long chunkSize, char[] header, char[] footer, boolean append) throws IOException  {
 		this.fileName = fileName;
 		this.chunckSize = chunkSize;
 		this.header = header;
 		this.footer = footer;
 		this.currentFile = new File(fileName);
 		this.currentBuffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.currentFile, append), "ISO-8859-1"));
		//this.currentBuffer = new BufferedWriter(new FileWriter(this.currentFile, append));
		this.fileCounter = 1;
		this.listSplitedFiles = new ArrayList<File>();
		this.listSplitedFiles.add(this.currentFile);
		
		this.currentBuffer.write(header);
		this.currentSize = header.length;
 	}

	// Metodos
	@Override
	public void close() throws IOException {
		this.currentBuffer.write(this.footer);
		this.currentBuffer.close();
		if (this.fileCounter > 1) {
			this.listSplitedFiles.add(this.currentFile);
		}
	}

	@Override
	public void flush() throws IOException {
		this.currentBuffer.flush();
		
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		if (this.currentSize + cbuf.length  < this.chunckSize) {
			this.currentBuffer.write(cbuf, off, len);
			this.currentSize += cbuf.length;
		} else {
			this.currentBuffer.write(this.footer);
			this.currentBuffer.close();
			this.listSplitedFiles.remove(this.currentFile);
			File auxFile = new File(createFileName(this.fileName));
			this.currentFile.renameTo(auxFile);
			this.listSplitedFiles.add(auxFile);
			this.fileCounter++;
			this.currentFile = new File(createFileName(this.fileName));
			this.currentBuffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.currentFile, false), "ISO-8859-1"));
			//this.currentBuffer = new BufferedWriter(new FileWriter(this.currentFile));
			this.currentBuffer.write(this.header);
			this.currentSize = this.header.length;
			this.write(cbuf, off, len);
		}
	}

	public List<File> getListSplitedFiles() {
		return this.listSplitedFiles;
	}
	
	private String createFileName(String fileName) {
		String  name = fileName;
		String[] tmp = name.split("\\.");
		if (tmp.length > 1) {
			name = tmp[0] + FILENAME_SEPARATOR + this.fileCounter + "." +tmp[1]; 
		} else {
			name += FILENAME_SEPARATOR + this.fileCounter;
		}
		return name;
	}

}