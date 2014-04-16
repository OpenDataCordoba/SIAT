//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore;

import java.lang.reflect.Constructor;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import coop.tecso.demoda.iface.helper.DemodaUtil;

public abstract class MultiThreadedAdpWorker implements AdpWorker {
  
	private static Logger log = Logger.getLogger(MultiThreadedAdpWorker.class);

	// Tamaño de la cola de mensajes
	private static final int MSG_QUEUE_SIZE = 2500;
	// Quantum de tiempo para Busy Wait (en milisegundos)
	private static final int TIME_QUANTUM = 10000;
	// Cantidad de Threads Procesadores
	private int numProcessorThreads = 0;
	// Cola de mensajes para los procesadores 
	private BlockingQueue<ProcessMessage<Object>> msgQueue;
	// Id del mensaje actual
	private Long currentId = 0L;

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
	 * Inicializa a cola de mensajes del worker y los threads procesadores 
	 * con el context pasado como parametro
	 *
	 * @throws Exception
	 */
	public void initializeProcessors(Class<? extends AdpProcessor> ProcessorClass, 
			Integer cantProcessors, AdpProcessorContext context) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		// Seteamos la cantidad de procesadores
		this.numProcessorThreads = cantProcessors;
		// Inicializamos la cola de mensajes
		msgQueue  = new ArrayBlockingQueue<ProcessMessage<Object>>(MSG_QUEUE_SIZE, true);
		
		for (int i = 0; i < this.numProcessorThreads; i++) {
				// Obtenemos el constructor
				Constructor<? extends AdpProcessor> constructor = ProcessorClass
					.getConstructor(context.getClass(), BlockingQueue.class);
				// Instanciamos el procesador
				AdpProcessor processor = constructor.newInstance(context, msgQueue);
				// Comenzamos a ejecutar el thread
				new Thread(processor).start();
		}
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
	}
	
	/** 
	 * Envia un "poison message" para cada uno de los threads 
	 * procesadores y se bloquea hasta que la cola de mensajes 
	 * se vacie.
	 *
	 * @throws Exception
	 */
	public void finishProcessors() throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		for (int i = 0; i < this.numProcessorThreads; i++) {
			// Creamos el POISON messaje
			ProcessMessage<Object> msg = new ProcessMessage<Object>();
			msg.setData(null);
			msg.setMessageType(ProcessMessage.POISON_MSG);
			
			// Enviamos el mensaje a los threads
			sendMessage(msg);
		}
	
		// Busy Wait
		while (!this.msgQueue.isEmpty()) {
			synchronized (this) {wait(TIME_QUANTUM);}
		}

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
	}

	/** 
	 * Vacia la cola de mensajes y envia un "poison message" a 
	 * cada uno de los threads procesadores.
	 *
	 * @throws Exception
	 */
	public void abortProcessors() throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// Vaciamos la cola de mensajes
		this.msgQueue.clear();
		for (int i = 0; i < this.numProcessorThreads; i++) {
			// Creamos el POISON messaje
			ProcessMessage<Object> msg = new ProcessMessage<Object>();
			msg.setData(null);
			msg.setMessageType(ProcessMessage.POISON_MSG);

			// Enviamos el mensaje a los threads
			sendMessage(msg);
		}
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
	}

	/** 
	 *  Envia un mensaje a la cola del Procesador
	 */
	public void sendMessage(ProcessMessage<Object> msg) throws InterruptedException {
		 msg.setId(++currentId);
		 msgQueue.put(msg);
	}
	
}
