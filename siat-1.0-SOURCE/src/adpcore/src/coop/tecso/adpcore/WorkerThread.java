//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore;

import org.apache.log4j.Logger;

import coop.tecso.adpcore.engine.AdpLog;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.UserContext;

public class WorkerThread implements Runnable {
	static private Logger log = Logger.getLogger(WorkerThread.class);	

	private AdpRun run;
	private AdpWorker worker;
	private UserContext userContext;
	
	public WorkerThread(AdpRun run, AdpWorker worker, UserContext uc) {
		this.run = run;
		this.worker = worker;
		this.userContext = uc;
	}
	
	public void run() {	
		
		//nos convertimos en el usuario que escheduleo la corrida
		//createUserContext(run);
		
		try {
			// ejecutamos el worker
			// ponemos los datos del usuario en el hilo de ejecucion
			DemodaUtil.setCurrentUserContext(userContext);
			AdpRun.setCurrentRun(this.run);
			worker.execute(run);
		} catch (Exception e) {
			log.debug(run.toString() + ": Se produjo un excepcion mientras se ejecutaba la corrida.", e);
			run.adplog.log(run.getPasoActual(), AdpLog.BOTH, "Se produjo un excepcion mientras se ejecutaba esta corrida", e);
			run.changeState(AdpRunState.ABORT_EXCEP, "Se produjo la excepcion: " + e.toString(), false);
			return;
		}
			
		// cerramos el log antes de moverlo
		run.adplog.close();
		if (run.idEstadoCorrida.longValue() == AdpRunState.PROCESANDO.id()) {
			run.changeState(AdpRunState.FIN_ERROR, "Pasaje a FIN_ERROR realizado por ADP. La corrida finalizo y no realizo ningun cambio de estado. La corrida esta mal programada, tiene que encargarse de cambiar el estado", false);
		}
	}
}
