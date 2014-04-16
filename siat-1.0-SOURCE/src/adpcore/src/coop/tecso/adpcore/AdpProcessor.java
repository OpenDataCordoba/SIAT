//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore;

import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public abstract class AdpProcessor implements Runnable, AdpWorker {

	private static Logger log = Logger.getLogger(AdpProcessor.class);
	// Cola de Mensajes a procesar
	private final BlockingQueue<ProcessMessage<Long>> msgQueue;
	// Context del Thread
	private AdpProcessorContext context;

	public AdpProcessor(AdpProcessorContext context, 
				BlockingQueue<ProcessMessage<Long>> msgQueue) throws Exception {
		this.context  = context;
		this.msgQueue = msgQueue;
	}
	
	/** 
	 * En caso de necesitarse, redefinirse en la subcalse.
	 */
	public void reset(AdpRun run) throws Exception {
	}

	/** 
	 * En caso de necesitarse, redefinirse en la subcalse.
	 */
	public void cancel(AdpRun run) throws Exception {
	}

	/** 
	 * En caso de necesitarse, redefinirse en la subcalse.
	 */
	public boolean validate(AdpRun run) throws Exception {
		return true;
	}
	
	/** 
	 * Entry Point del Procesador. 
	 * Invoca al metodo execute implementado en cada
	 * subclase 
	 */
	public void run() {
		try {
			// Obtenemos la corrida
			AdpRun run = context.getAdpRun();
			
			// Invocamos a execute (implementado en cada subclase)
			execute(run);
	
		} catch (Exception e) {
			String msg = "Error durante la ejecucion del thread.";
			log.error(msg, e);
		} 		
	}

	/** 
	 *  Consume un mensaje de la cola del Procesador
	 */
	public ProcessMessage<Long> getMessage() throws InterruptedException { 
		return msgQueue.take();
	}
}