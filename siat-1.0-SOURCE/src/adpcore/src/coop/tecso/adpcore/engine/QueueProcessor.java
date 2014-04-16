//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore.engine;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import coop.tecso.adpcore.AdpEngine;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpUtil;

public class QueueProcessor {
	static private Logger log = Logger.getLogger(DirectoryProcessor.class);	
	
	private boolean started = false;
	private int processorFrecuency = -1; //cada cuanto se ejecuta el processor
	private int count = 0; //contador frecuencia

	/**
	 * Comienza el monitor de la cola de corridas.
	 * @throws Exception
	 */
	public void start() throws Exception {
		if (started) {
			log.info("QueueProcessor ya esta iniciado.");
			return;
		}
		
		started = true;
		log.info("QueueProcessor iniciando...");

		processorFrecuency = Integer.parseInt(AdpUtil.getConfig("queueProcessor.frecuency", "30"));
	}

	/**
	 * Detiene el monitor.
	 * @throws Exception
	 */
	public void stop() throws Exception {
		if (!started) {
			log.info("QueueProcessor ya esta detenido.");
			return;
		}		
	}
  
	/**
	 * Ejecutado periodicamente
	 */
	public void background() throws Exception {
		if (started) {
			count = (count + 1) % processorFrecuency;
			if (count==0) {
				log.info("Adp Used Memory: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) /1024/1024) + "M");

				Date now = new Date();
				List<Long> ids = AdpDao.getReadyRuns(now);
				for(Long id: ids) {
					AdpRun run = AdpRun.getRun(id);
					AdpEngine.executeScheduled(run);
				}
			}
		}
	}
}
