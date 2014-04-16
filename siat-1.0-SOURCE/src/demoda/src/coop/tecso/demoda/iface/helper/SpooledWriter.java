//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.helper;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilidad que permite a varios threads escribir 
 * de forma ordenada un conjunto de streams de 
 * caracteres.
 * 
 * Cada thread ejecuta una peticion de escritura
 * invocando al metodo write, y pasando como 
 * argumentos el nombre del stream, el orden de la 
 * peticion y el dato a escribir.
 * 
 * Nota: se debe asegurar que ninguna peticion
 * de escritura se pierda.
 * 
 * @author tecso
 */
public class SpooledWriter {

	private static String ERROR = "error";
	
	private Map<String, Writer> writerMap;

	private Long currentRequest;
	
	private Map<Long, RequestInfo> requestBuffer;
	
	/**
	 * Constructor
	 */
	public SpooledWriter() {
		writerMap = new HashMap<String, Writer>();
		currentRequest = 1L;
		requestBuffer = new HashMap<Long, RequestInfo>();
	}

	/**
	 * Agrega un stream de caracteres para escritura, asociandolo
	 * a una clave
  	 */
	public void addWriter(String key, Writer writer) throws IOException {
		if (writerMap.get(key) == null) {
			writerMap.put(key, writer);
		}
	}

	/**
	 * Obtiene un stream de caracteres por su clave 
	 */
	public synchronized Writer getWriter(String key) {
		return writerMap.get(key);
	}

	/**
	 * Obtiene la lista de streams de caracteres
	 * asociadas. 
	 */
	public synchronized List<Writer> getListWriter() {
		List<Writer> listWriter = new ArrayList<Writer>();
		
		for (String key: writerMap.keySet()) {
			listWriter.add(writerMap.get(key));
		}
		
		return listWriter;
	}


	public synchronized Map<String, Writer> getMap() {
		return this.writerMap;
	}
	
	/**
	 * Cierra todos los streams de caracteres asociados 
	 */
	public void close() throws IOException {
		for (String fileName: writerMap.keySet()) {
			System.out.println("Cerrando " + fileName);
			writerMap.get(fileName).close();
		}
	}

	/**
	 * Escribe en el stream de caracteres con clave key,
	 * la peticion de escritura con orden reqOrden.   
	 */
	public synchronized void write(String key, Long reqOrder, String data) 
		throws IOException {
		
		// Si es la peticion de escritura que la estoy esperando,
		if (reqOrder.equals(this.currentRequest)) {
			if (key != ERROR) { 
				// Escribo
				Writer buffer = writerMap.get(key);
				if (buffer == null) 
					throw new IllegalArgumentException("Invalid Key: " + key);
				buffer.write(data);
			}
			// Busco si la peticion que sigue, ya fue realizada y la ejecuto
			this.currentRequest++;
			RequestInfo nextReq = this.requestBuffer.get(this.currentRequest);
			if (nextReq != null) {
				this.requestBuffer.remove(this.currentRequest);
				write(nextReq.getWriterName(), this.currentRequest ,nextReq.getData());
			} 
		}
		// Si no, la pongo en espera
		else {
			this.requestBuffer.put(reqOrder, new RequestInfo(key,data));
		}
	}

	/**
	 * Escribe en el stream de caracteres con clave key,
	 * sin importar el orden.
	 */
	public synchronized void write(String fileName, String data) 
		throws IOException {
		Writer buffer = writerMap.get(fileName);
		buffer.write(data);
	}
	
	/**
	 * Avisa al spooler que llego un error
  	 */
	public synchronized void error(Long reqOrder) throws IOException {
		this.write(ERROR, reqOrder, null);
	}


	/**
	 * Representa una solicitud de escritura a un stream,
	 * de caracteres. 
	 */
	private class RequestInfo {

    	public String writerName;

    	public String data;
    	
    	public RequestInfo(String name, String s) {
    		writerName = name;
    		data = s;
    	} 
    	
    	public String getWriterName() {
    		return writerName;
    	} 

    	public String getData() {
    		return data;
    	} 
    }

}
