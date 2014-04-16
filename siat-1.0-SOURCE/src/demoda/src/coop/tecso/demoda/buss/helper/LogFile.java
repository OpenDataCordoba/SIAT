//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.buss.helper;

import java.io.BufferedWriter;
import java.io.FileWriter;


/**
 *  Clase para Logear cadenas de string en un archivo.
 * 
 * @author Tecso
 *
 */
public class LogFile {

	private String path;
	private String nameFile;
	private boolean append;
	
	private BufferedWriter file;
	
	public LogFile(){
		this.path = "";
		this.nameFile = "";
		this.append=false;
		this.file=null;
	}
	
	/**
	 *  Constructor que devuelve el LogFile listo para logear.
	 *  
	 * @param path - string con el directorio en el cual se crea el archivo
	 * @param nameFile - string con el nombre del archivo a crear o abrir.
	 * @param append - boolean que indica si se desea agregar al archivo a reemplazarlo.
	 * @throws Exception
	 */
	public LogFile(String path, String nameFile, boolean append) throws Exception{
		this.path = path;
		this.nameFile = nameFile;
		this.append=append;
		open();
	}

	/**
	 * @return el append
	 */
	public boolean isAppend() {
		return append;
	}

	/**
	 * @param append el append a setear
	 */
	public void setAppend(boolean append) {
		this.append = append;
	}

	/**
	 * @return el nameFile
	 */
	public String getNameFile() {
		return nameFile;
	}

	/**
	 * @param nameFile el nameFile a setear
	 */
	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	/**
	 * @return el path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path el path a setear
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 *  Abre un archivo para logear. Si existe lo reemplaza. 
	 *
	 */
	public void open() throws Exception{
		try{
			this.file = new BufferedWriter(new FileWriter(path.concat(nameFile), this.append));
		}catch(Exception e){
			throw new Exception("LogFile Exception - No se puede abrir el archivo "+path.concat(nameFile)+" : "+e);
		}
	}
	
	/**
	 *  Cierra el archivo abierto por el logger.
	 *
	 */
	public void close() throws Exception{
		try{
			this.file.close();
		}catch(Exception e){
			throw new Exception("LogFile Exception - No se puede cerrar el archivo "+path.concat(nameFile)+" : "+e);
		}
	}
	
	/**
	 *  Agrega una linea al archivo.
	 * 
	 * @param line
	 */
	public void addline(String line) throws Exception{
		try{
			this.file.write(line);
			this.file.newLine();
			this.file.flush();
		}catch(Exception e){
			throw new Exception("LogFile Exception - No se puede escribir en el archivo "+path.concat(nameFile)+" : "+e);
		}
	}
	
}
