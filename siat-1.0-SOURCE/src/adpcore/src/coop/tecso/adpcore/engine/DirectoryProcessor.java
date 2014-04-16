//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore.engine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import coop.tecso.adpcore.AdpEngine;
import coop.tecso.adpcore.AdpProcess;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpUtil;

public class DirectoryProcessor implements FileChangeListener {

	static private Logger log = Logger.getLogger(DirectoryProcessor.class);	
	
	private List<DirWatcher> watchers = new ArrayList<DirWatcher>(); //Lista de observadores de directorios
	private List<AdpProcess> watchProcess = new ArrayList<AdpProcess>(); // lista de procesos en los que se observa el directorio

	private boolean started = false;
	private int processorFrecuency = -1; //cada cuanto se ejecuta el processor
	private int count = 0; //frecuency contador
	
	/**
	 * Comienza el monitor de directorios de input de procesos.
	 * @throws Exception
	 */
	public void start() throws Exception {
		if (started) {
			log.info("DirectoryProcessor ya esta iniciado.");
			return;
		}
		
		started = true;
		log.info("DirectoryProcessor iniciando...");

		processorFrecuency = Integer.parseInt(AdpUtil.getConfig("directoryProcessor.frecuency", "3"));
		
		//obtenemos lista de directorios de entrada, y cremos los watcher
		List<AdpProcess> arriveProcessList = AdpDao.getFileArriveProcess();
		for(AdpProcess process: arriveProcessList) {
			File dir = new File(process.getInputFilePath(AdpRunDirEnum.RECIBIDO));
			
			//intetamos crearlo
			if (!dir.isDirectory()) {
				dir.mkdir();
			}
			
			if (!dir.isDirectory()) {
				log.warn("Agregando Directorios de Arribo: '" + dir.getAbsolutePath()
						+ "': no existe o no es un directorio.\n" 
						+ "Codigo Proceso: '" + process.getCodProceso()
						+ "', verifique valor de campo direcotorioInput en Proceso");
			} else if (!dir.canRead() || !dir.canWrite()) {
				log.warn("Agregando Directorios de Arribo: '" + dir.getAbsolutePath() 
						+ "': No existe permisos suficiente requiere lectura y escritura.\n" 
						+ "Codigo Proceso: '" + process.getCodProceso() + "', agregar permisos.");
			}

			log.debug("Escuchando directorio en: " + dir.getAbsolutePath());
			watchers.add(new DirWatcher(this, dir, process.getId()));
			watchProcess.add(process);
		}
		log.info("DirectoryProcessor iniciado.");
	}

	/**
	 * Detiene el monitor.
	 * @throws Exception
	 */
	public void stop() throws Exception {
		if (!started) {
			log.info("DirectoryProcessor ya esta detenido.");
			return;
		}

		//vaciamos los watcher
		watchers.clear();		
	}
  
	/**
	 * Ejecutado periodicamente teniendo en cuenta el 
	 * processoFrecuncy: 
	 * 1 ejecutar siempre, 
	 * n ejecutar cada n*delay_de_engine
	 */
	public void background() throws Exception {
		if (started) {
			count = (count + 1) % processorFrecuency;
			if (count==0) {
				//invocamos el watch de cada directorio
				for(DirWatcher watcher: watchers) {
					watcher.watch();
				}
			}
		}
	}

	public void fileModified(File file) {
		for (AdpProcess process: watchProcess) {
			try {
				
				//verficamos que el proceso no este lokeado.
				AdpProcess p = new AdpProcess();
				AdpDao.loadProcess(process.getId(), p);

				//asumimos que todos los archibos que arriban llegan al directorio de corrida 'recibido'
				String dirRecibido = process.getInputFilePath(AdpRunDirEnum.RECIBIDO);
				File procdir = new File(dirRecibido).getCanonicalFile();
				File chgfile = file.getCanonicalFile();
				
				if (chgfile.getParent().equals(procdir.getAbsolutePath())) {
					if (p.isLocked()) {
						log.info("Arribo de archivo:" +  file.getName() + ": Ignorando ejecucion por proceso con marca de lock. Proceso: '" + process.getCodProceso());
						return;
					}
					
					//chequeamos si este nodo puede ejecutar el proceso
					if (!AdpEngine.canExecProcess(p)) {
						log.info("Arribo de archivo:" +  file.getName() + ": Ignorando ejecucion, por no ser nodo valido. Este nodo: '" + AdpEngine.nodeName() + "' " + " nodos habilitados: " + p.getEjecNodo());
						return;
					}

					if (!file.exists()) {
						log.info("Arribo de archivo:" +  file.getName() + " Archivo ya procesado.");
						return;
					}					
					
					//encontramos el proceso del archivo que cambio. Scheduleamos la corrida ya!
					log.info("Arribo de archivo:" +  file.getName() + " Preparando ejecucion.");
					AdpRun run = AdpRun.newRun(process.getId(), "Iniciado por arribo de archivo: " + chgfile.getName());
					run.create();
					run.putParameter(AdpParameter.INPUTFILEPATH, chgfile.getAbsolutePath());
					
					try {
						run.execute(null);
					} catch (Exception e) {
						log.error("DirectoryProcessor: fileModified(): Error durante la ejecucion del proceso '" + process.getCodProceso() + "'.", e);				
					}
				}
			} catch (IOException e) {
				log.error("DirectoryProcessor: fileModified(): No se pudo comenzar corrida de proceso '" + process.getCodProceso() + "'." +
						" Fallo al obtener nombres de directorio del proceso. " +
						" Esto puede ocurrir si elimino un archivo del directorio durante el proceso.", e);
			} catch (Exception e) {
				log.error("DirectoryProcessor: fileModified(): No se pudo comenzar corrida de proceso '" + process.getCodProceso() + "'.", e);
			}		
		}
		
	}

	public void fileRemoved(File changeFile) {
		//ignoramos los removidos
	}

	/**
	 * @return the processorFrecuency
	 */
	public int getProcessorFrecuency() {
		return processorFrecuency;
	}

	/**
	 * @param processorFrecuency the processorFrecuency to set
	 */
	public void setProcessorFrecuency(int processorFrecuency) {
		this.processorFrecuency = processorFrecuency;
	}
}
