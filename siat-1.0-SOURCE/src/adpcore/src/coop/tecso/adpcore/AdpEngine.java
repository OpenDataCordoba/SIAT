//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import coop.tecso.adpcore.engine.AdpDao;
import coop.tecso.adpcore.engine.AdpLog;
import coop.tecso.adpcore.engine.AdpParameter;
import coop.tecso.adpcore.engine.CronProcessor;
import coop.tecso.adpcore.engine.DirectoryProcessor;
import coop.tecso.adpcore.engine.ExecuteMode;
import coop.tecso.adpcore.engine.QueueProcessor;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.UserContext;

public class AdpEngine {
	static private Logger log = Logger.getLogger(AdpEngine.class);	

	static public void deleteRun(AdpProcess process) throws Exception {
	}

    static public void schedule(AdpProcess process, ExecuteMode execMode)  throws Exception {
	}

	static public void execute(AdpProcess process, ExecuteMode execMode)  throws Exception {
	}

    static public Long getProcessId(String processName) throws Exception {
    	return AdpDao.getProcessId(processName);
	}

	public static void loadProcessDefData(Long processId, AdpProcess process)  throws Exception {
		AdpDao.loadProcess(processId, process);
	}

	public static void loadRunData(Long runId, AdpRun adpRun)  throws Exception {
		AdpDao.loadRun(runId, adpRun);
	}

	/**
	 * Crea un corrida con sus parametros de proceso en estado EnPreparacion
	 * @param process el proceso al que peternece la corrida
	 */
	public static void createRun(AdpRun run, Map<String, AdpParameter> params) throws Exception {
		AdpDao.createRun(run, params);
	}
	
	/**
	 * Elimina un corrida
	 * @param runId id de la corrida a eliminar
	 * @return >1 si elimino corrida, 0 si runId no existia a ninguna corrida.
	 * @throws Exception Si algo falla por cuestiones de JDBC
	 */
	public static Long deleteRun(Long runId) throws Exception {
		return AdpDao.deleteRun(runId);
	}

	
	/** Procesador **/
	private Thread thread = null;
	private boolean threadDone = false;
	private boolean started = false;
	private int engineDelay = -1; //en millisegundos
	private DirectoryProcessor directory = null;
	private QueueProcessor queue = null;
	private CronProcessor cron = null;
	
    /**
     * Start the background thread.
     */
    protected void threadStart() {
        if (thread != null)
            return;

        threadDone = false;
        String threadName = "EngineProcessor[" + toString() + "]";
        thread = new Thread(new EngineProcessor(), threadName);
        thread.setDaemon(true);
        thread.start();
    }


    /**
     * Stop the background thread.
     */
    protected void threadStop() {
        if (thread == null)
            return;
        threadDone = true;
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            ;
        }
        thread = null;
    }    
    
	/**
	 * Comienza el engine de adp.
	 * @throws Exception
	 */
	public void start() throws Exception {
		if (started) {
	    	log.info("AdpEngine ya esta inicializado.");
			return;
		}
		
		String nodeName = nodeName();
		log.info("Iniciando nodo ADP '" + nodeName + "'");
				
    	started = true;
    	engineDelay = Integer.parseInt(AdpUtil.getConfig("engine.delay", "1000"));
    	
		directory = new DirectoryProcessor();
		queue = new QueueProcessor();
		cron = new CronProcessor();
		
		cleanZombies(nodeName);

		directory.start();
		cron.start();
		queue.start();
		
		threadStart();
	}

	/**
	 * Detiene el monitor.
	 * @throws Exception
	 */
	public void stop() throws Exception {
		if (!started) {
	    	log.info("AdpEngine ya esta detenido.");
			return;
		}
		
		threadStop();
		started = false;

		directory.stop();
		cron.stop();
		queue.stop();
	}
  
	/**
	 * Ejecutado periodicamente
	 */
	protected void background() throws Exception {
		directory.background();
		queue.background();
		cron.background();
	}
	
	protected class EngineProcessor implements Runnable {
		public void run() {
            while (!threadDone) {
                try {
                    Thread.sleep(engineDelay);
                } catch (InterruptedException e) {
                    ;
                }
                if (!threadDone) {
                    try {
                        background();
                    } catch (Throwable t) {
                        log.error("Excepcion invocando operation periodica del engine: ", t);
                    }
                }
            }
		}		
	}

	/**
	 * @return the started
	 */
	public boolean isStarted() {
		return started;
	}

	/*
	 * Crea un user context del siat apartir del usuario que escheduleo la corrida.
	 */
	private static UserContext createUserContext(AdpRun run) throws Exception {
		//Los hilos creados por ADP, no posee informacion de usuarioSiat, 
		//Esto se decidio hacerlo asi, para los procesos no genern solicitudes en nombre de otro,
		//ya que podria causar conflictos entre humanos.
		//OJO: si aqui añadimos info usando setUsuarioSiat()..., automaticamente las solicitudes
		//tendran usuarioOrigen y areaOrigen del UsuarioSiat seteado. No añadir esta info,
		//a menos que se quiera cambiar el comportamento de arriva, tambien ver: Solicitud.setInitValues()
		UserContext userContext = new UserContext();
		userContext.setUserName(run.getUsuario() == null ? "" : run.getUsuario());
		DemodaUtil.setCurrentUserContext(userContext);
		log.debug("createUserContext(): " + run.getUsuario() + " uc:" + userContext.getUserName());
		return userContext;
	} 
	
	public static void executeScheduled(AdpRun run) throws Exception {
		Class clazz = null;
		AdpWorker worker = null;
		
		//chequeamos si el proceso esta lockead
		if (run.process.isLocked()) {
			log.info(run + ": Ignorando ejecucion, por proceso con marca de lock. Proceso: '" + run.process.getCodProceso());
			return;
		}

		//chequeamos si este nodo puede ejecutar el proceso
		if (!canExecProcess(run.process)) {
			log.info(run + ": Ignorando ejecucion, por no ser nodo valido. Este nodo: '" + nodeName() + "' " + " nodos habilitados: " + run.process.getEjecNodo());
			return;
		}
		
		// ponemos los datos del usuario en el hilo de ejecucion
		UserContext userContext = createUserContext(run);
		
		//iniciamos adp logs
		// si podemos determinar logFilename ok. sino usamos el estandar
		String logFilename = run.getLogFilename(AdpRunDirEnum.LOGS);
		if (!"".equals(logFilename)) {
			run.adplog.close();
			run.adplog = new AdpLog(run.id, logFilename);
		}
		
		run.fechaInicio = new Date();
		run.changeState(AdpRunState.PROCESANDO, null, false);

		try {
			clazz = Class.forName(run.process.getClassForName());
			worker = (AdpWorker) clazz.newInstance();
		} catch (Exception e) {
			String msg = "Adp no pudo instanciar el worker. Verifique que exista nombre de clase: '" +  run.process.getClassForName() + "'";
			log.error(msg, e);
			run.logError(msg, e);
			run.changeState(AdpRunState.ABORT_EXCEP, msg, false, e);
		}			
		
		//Iniciamos hilo
		log.debug(run.toString() + ": Comenzando hilo: " + clazz.getName());
		workerThreadStart(run, worker, userContext);
	}


    /**
     * Comenzar el hilo para el worker.
     */
    static protected void workerThreadStart(AdpRun run, AdpWorker worker, UserContext uc) {
        String threadName = "AdpWorker[" + run.toString() + "]";
		WorkerThread wt = new WorkerThread(run, worker, uc);
		Thread t = new Thread(wt, threadName);
        t.setDaemon(true);
        t.start();
    }

	/* Obtiene el nombre del nodo de procesamiento:
	   Si definido lee el  "engine.node" del context.xml y toma este nombre.
	   Si no, intenta leer el hostname, si falla o no esta, utiliza el nombre "default"
	 */
	public static String nodeName() {
		String nodename = AdpUtil.getConfig("engine.node", "default");		
		if (nodename.equals("") || nodename.equals("default")) {
			try {
				java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
				nodename = localMachine.getHostName();
			} catch(java.net.UnknownHostException ex) {
				log.warn("Error al intentar determinar nombre de host. Usando 'default'" + ex);
				nodename = "default";
			}
		}
		return nodename;
	}


	/**
	 * Limpia en la db, los estados de corridas que estan en procesando,
	 * y en realidad no hay ningun hilo ejecutandolo.
	 */
	protected static void cleanZombies(String nodeName) throws Exception {
		List<Long> myZombies = AdpDao.getZombiesRuns(nodeName);
		for(Long id : myZombies) {
			AdpRun run = AdpRun.getRun(id);
			log.debug(run.toString() + ": Se detecto una corrida sin procesador.");
			run.adplog.log(run.getPasoActual(), AdpLog.BOTH, "Se detecto una corrida sin procesador.", null);
			run.changeState(AdpRunState.ABORT_EXCEP, "El servicio ADP fue apagado inesperadamente.", false);
		}
	}


	/**
	 * Retorna true si este nodo puede ejecutar este proceso.
	 * false si no lo puede ejecutar.
	 * @return
	 */
	public static boolean canExecProcess(AdpProcess process) {
		String ejecNodo = process.getEjecNodo();
		
		if (ejecNodo == null || ejecNodo.trim().equals("") || ejecNodo.trim().equals("*")) {
			return true;
		}
		String nodes = "," + ejecNodo + ",";
		return nodes.indexOf("," + nodeName() + ",") >= 0;
	}
	
	/**
	 * Ejecuta un worker que corresponde a un proceso por arribo de archivo
	 * @throws Exception
	 */
	static protected void executeFileArribe(AdpRun run) throws Exception {
		Class clazz = null;
		AdpWorker worker = null;

		//chequeamos si el proceso esta lockead
		if (run.process.isLocked()) {
			log.info(run + ": Ignorando ejecucion, por proceso con marca de lock. Proceso: '" + run.process.getCodProceso());
			return;
		}

		//chequeamos si este nodo puede ejecutar el proceso
		if (!canExecProcess(run.process)) {
			log.info(run + ": Ignorando ejecucion, por no ser nodo valido. Este nodo: '" + nodeName() + "' " + " nodos habilitados: " + run.process.getEjecNodo());
			return;
		}

		// ponemos los datos del usuario en el hilo de ejecucion
		createUserContext(run);

		try {
			clazz = Class.forName(run.process.getClassForName());
			worker = (AdpWorker) clazz.newInstance();
		} catch (Exception e) {
			String msg = "Adp no pudo instanciar el worker. Verifique que exista nombre de clase: '" +  run.process.getClassForName() + "'";
			log.error(msg, e);
			run.logError(msg, e);
			run.changeState(AdpRunState.ABORT_EXCEP, msg, false, e);
		}
		
		// iniciamos el log
		String filename = run.getInputFilename();
		String logFilename = run.getLogFilename(AdpRunDirEnum.LOGS);//AdpRunDirEnum.PROCESANDO);
		run.adplog.close();
		run.adplog = new AdpLog(run.id, logFilename);

		String msg = "Comenzado por arribo de archivo " + filename + " " + AdpUtil.formatDate(run.fechaInicio);
		run.changeState(AdpRunState.PROCESANDO, msg, false);
		
		try {
			// ejecutamos el worker
			log.debug(run.toString() + ": Invocando execute() en " + clazz.getName());
			AdpRun.setCurrentRun(run);
			worker.execute(run);
		} catch (Exception e) {
			log.debug(run.toString() + ": Se produjo un excepcion mientras se ejecutaba la corrida.", e);
			run.adplog.log(run.pasoActual, AdpLog.BOTH, "Se produjo un excepcion mientras se ejecutaba esta corrida", e);
			run.changeState(AdpRunState.ABORT_EXCEP, "Se produjo la excepcion: " + e.toString(), false, e);
			return;
		}
		
		// cerramos el log antes de moverlo
		run.adplog.close();
		if (run.idEstadoCorrida.longValue() == AdpRunState.PROCESANDO.id()) {
			run.changeState(AdpRunState.FIN_OK, "Pasaje a FIN_OK realizado por ADP. La corrida finalizo y no realizo ningun cambio de estado.", false);
		}
	}

	/**
	 * Inicializa el TLS del hilo con el current run 
	 * y con la informacion de contexto del usuario
	 * */
	public static void initializeThread(AdpRun run) throws Exception {
		AdpRun.setCurrentRun(run);
		createUserContext(run);
	}

	/**
	 * 	Llama al metodo validar del worker para el proceso pasado. Se utiliza para verificar antes de crear la corrida.
	 * 
	 * @param run
	 * @return
	 * @throws Exception
	 */
	public static boolean validateProcess(AdpRun run) throws Exception {
		Class clazz = null;
		AdpWorker worker = null;
		
		// ponemos los datos del usuario en el hilo de ejecucion
		createUserContext(run);

		try {
			clazz = Class.forName(run.process.getClassForName());
			worker = (AdpWorker) clazz.newInstance();
		} catch (Exception e) {
			String msg = "Adp no pudo instanciar el worker. Verifique que exista nombre de clase: '" +  run.process.getClassForName() + "'";
			log.error(msg, e);
			return false;
		}
		
		return worker.validate(run);
	} 
}
