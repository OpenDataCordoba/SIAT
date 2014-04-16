//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package coop.tecso.adpcore.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import coop.tecso.adpcore.AdpEngine;

/**
 * Utilidad para monitorear archivos en un directorio.
 *
 * @author Filip Hanik
 * @author Peter Rossbach
 * @version 1.1
 * 
 * @author Tecso Coop. Ltda. (Adapatada para soportar arribos de archivos largos)
 * @version 1.2adp
 *
 */
public class DirWatcher {
	static private Logger log = Logger.getLogger(AdpEngine.class);	

	private FileChangeListener listener = null;
	private File watchDir = null;
	private Map<String, FileInfo> status = new HashMap<String, FileInfo>();
	public Long idProceso = 0L;

	/**
	 * Construye un observador para un directorio
	 * @param listener A esta clase se llamara cada vez que ocurra un cambio en el dir
	 * @param watchDir Directorio a observar.
	 */
	public DirWatcher(FileChangeListener listener, File watchDir, Long idProceso) {
		this.listener = listener;
		this.watchDir = watchDir;
		this.idProceso = idProceso;
	}

	/**
	 * LLamar a este metodo para verificar si hubo cambios.
	 * Si ocurrienron cambies, se llama a los metodos fileModified() o
	 * fileRemoved() del listener pasado en el constructor.
	 */
	public void watch() {
		File[] list = watchDir.listFiles();
		if (list == null)
			list = new File[0];
		//first make sure all the files are listed in our current status
		for (int i = 0; i < list.length; i++) {
			addFileInfo(list[i]);
		}

		// sort status por info.lastModifiedDate de menor a mayor
		// esto lo hacemos para que si corresponde se llamane primero
		// a los fileModified() mas antiguos
		List<FileInfo> sorted = new ArrayList<FileInfo>();
		//pasamos el mapa a una lista
		for (Iterator i = status.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			FileInfo info = (FileInfo) entry.getValue();
			sorted.add(info);
		}

		Collections.sort(sorted);

		//check all the status codes
		for (FileInfo info: sorted) {
			int check = info.check();
			if (check == 1 || check == 0) {
				log.info("watch() filemodified:" + info.file.getName() + " - " + info.file.lastModified());
				listener.fileModified(info.getFile());
			} else if (check == -1) {
				listener.fileRemoved(info.getFile());
				//no need to keep in memory
				status.remove(info.file.getAbsolutePath());
			}
		}
	}

	/**
	 * agrega un file info al status 
	 * @param file
	 */
	protected void addFileInfo(File file) {
		FileInfo info = (FileInfo) status.get(file.getAbsolutePath());
		if (info == null) {
			info = new FileInfo(file);
			info.setLastState(-1); //asumir que el archivo no existe.
			status.put(file.getAbsolutePath(), info);
		}
	}

	public void clean() {
		status.clear(); 
	}
	/*-- --*/

	/**
	 * Filtrar por extension
	 */
	/* Tal vez algun filter
	protected class WarFilter implements java.io.FilenameFilter {
        public boolean accept(File path, String name) {
            if (name == null)
                return false;
            return name.endsWith(".war");
        }
    }
	 */

	/**
	 * Informacion extra de cada archivo.
	 */
	protected class FileInfo implements Comparable {
		protected File file = null;
		protected long lastChecked = 0;
		protected long lastState = 0;
		protected boolean changed = false; //bandera que indica si ocurrio cambio previamente.
		protected long parsedTimestamp = 0L;

		public FileInfo(File file) {
			this.file = file;
			this.lastChecked = file.lastModified();
			if (!file.exists())
				lastState = -1;
			parsedTimestamp = parseTimestamp(file.getName());
		}

		private long parseTimestamp(String name) {
			String[] arr = name.split("-");

			try { return Long.parseLong(arr[1]); } catch (Exception e) {};
			return 0;
		}

		public int compareTo(Object obj) {
			try {
				FileInfo info = (FileInfo) obj;

				/*
        		//por nombre de archivo
				if (this.parsedTimestamp < info.parsedTimestamp ) {
        			return -1;
        		} else if (this.parsedTimestamp == info.parsedTimestamp) {
        			return 0;
        		}
        		return 1;
				 */

				//por orden de llegada
				if (this.file.lastModified() < info.file.lastModified()) {
					return -1;
				} else if (this.file.lastModified() == info.file.lastModified()) {
					return 0;
				}
				return 1;
			} catch (Exception e) {
				return -1;
			}
		}

		/**
		 * Verfica si existen cambios en el archivo usando fecha de ultima modificacion.
		 * <p>
		 * Control para la deteccion de archivos demasiado extensos.
		 * En linux cuando un archivo demasiado largo es copiado o creado
		 * la bandera lastModified va cambiando a medida que el achivo crece
		 * de tamaño hasta concluir la operacion.
		 * <p>Este metodo, 'espera' que la fecha de modificacion se detenga
		 * para informar el cambio. De esta manera evitamos iformar multiples cambios
		 * cuando en realidad ocurre uno solo, pero esto trae otros problemas, ver NOTAS.
		 * <p>NOTA: ver el comportamiento en win32 y otros.
		 * <p>BUG:<p> 1- Al setear lastState = -1, no podemos mas distinguir
		 * 	          entre un arribo genuino y modificacion.
		 * 	      <p> 2- En el caso que arriben archivos realmente distintos 
		 * 	          con el mismo nombre y a mas velocidad que la del chequeo.
		 *            Se pierden los eventos del nuevo archivo. :(
		 *            Considero que es mas frecuente que lleguen archivo largos en tiempos separados
		 *            a que lleguen pequeños archivos en intervalos cortos de tiempo. Si es esto
		 *            ultimo, piense en utilizar otra cosa que no sea file pooling.
		 * @return
		 */
		public boolean modified() {
			boolean modif = file.exists() && file.lastModified() > lastChecked;
			//si se detecta cambio, almaceno que hubo pero informo que no hubo cambio.
			if (modif) { 
				changed = true;
				lastState = -1; //asumimos que el archivo aun no existe, de esta menera
				//evitamos que added() informe un cambio prematuro.
				return false;
			}

			//recien cuando no se detectan cambios, verifico si hubo algun cambio previo que no se informo
			//si es asi, informo del cambio y des seteo la bandera que hubo cambio previo
			if (!modif && changed) {
				changed = false;
				return true;
			}

			//si estoy aca es modif fue falso y no hubo cambios previos -> informo que no hubo cambios
			return false;
		}

		/**
		 * Verfica si se trata de un nuevo archivo que llego.
		 * <p>Esta funcion tambien posee la verificacion retardada para
		 * informar el arribo cuando no es necesario.
		 * <p>VER modified() de esta misma clase.
		 * @return
		 */
		public boolean added() {
			boolean added = (lastState == -1) && exists();
			// si detecta que es un archivo nuevo, almaceno el cambio pero informo que no paso nada.
			if (added) {
				changed = true;
				return false;
			}

			//recien cuando detecta que no hay agregados, verifico si hay que informar el arribo previo.
			if (!added && changed) {
				changed = false;
				return true;
			}

			//si estoy aca, es que no hubo arribos y no hay arribo previo -> informo no arribos.
			return false;        	
		}

		public boolean exists() {
			return file.exists();
		}

		/**
		 * Retorna 1 si el archivo ha sido agrgado/modificado, 0 si no
		 * tiene cambios y -1 lo eliminaron
		 * <p>NOTA: la funcion no puede distinguir entre arribos y cambios.
		 * 
		 * @return int 1=file added/modified; 0=unchanged; -1=file removed
		 */
		public int check() {
			//file unchanged by default
			int result = 0;

			if (modified()) {
				//file has changed - timestamp
				result = 1;
				lastState = result;
			} else if ((!exists()) && (!(lastState == -1))) {
				//file was removed
				result = -1;
				lastState = result;
			} else if (added()) {
				//file was added
				result = 1;
				lastState = result;
			}
			//this.lastChecked = System.currentTimeMillis();
			this.lastChecked = file.lastModified();
			return result;
		}

		protected void setLastState(int lastState) {
			this.lastState = lastState;
		}

		public File getFile() {
			return file;
		}

		public long getLastChecked() {
			return lastChecked;
		}

		public long getLastState() {
			return lastState;
		}
	}
}
