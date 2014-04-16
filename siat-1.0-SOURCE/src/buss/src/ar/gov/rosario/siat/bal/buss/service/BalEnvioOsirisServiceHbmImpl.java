//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.service;

/**
 * Implementacion de servicios del submodulo Envios Osiris del modulo Bal
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.afi.buss.bean.EstForDecJur;
import ar.gov.rosario.siat.afi.buss.bean.ForDecJur;
import ar.gov.rosario.siat.afi.buss.dao.AfiDAOFactory;
import ar.gov.rosario.siat.bal.buss.bean.BalEnvioOsirisManager;
import ar.gov.rosario.siat.bal.buss.bean.CierreBanco;
import ar.gov.rosario.siat.bal.buss.bean.Conciliacion;
import ar.gov.rosario.siat.bal.buss.bean.DetalleDJ;
import ar.gov.rosario.siat.bal.buss.bean.DetallePago;
import ar.gov.rosario.siat.bal.buss.bean.EnvioOsiris;
import ar.gov.rosario.siat.bal.buss.bean.EstTranAfip;
import ar.gov.rosario.siat.bal.buss.bean.EstadoEnvio;
import ar.gov.rosario.siat.bal.buss.bean.TranAfip;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.CierreBancoAdapter;
import ar.gov.rosario.siat.bal.iface.model.CierreBancoVO;
import ar.gov.rosario.siat.bal.iface.model.CierreSucursalVO;
import ar.gov.rosario.siat.bal.iface.model.ConciliacionAdapter;
import ar.gov.rosario.siat.bal.iface.model.ConciliacionVO;
import ar.gov.rosario.siat.bal.iface.model.DetalleDJAdapter;
import ar.gov.rosario.siat.bal.iface.model.DetalleDJVO;
import ar.gov.rosario.siat.bal.iface.model.DetallePagoAdapter;
import ar.gov.rosario.siat.bal.iface.model.DetallePagoVO;
import ar.gov.rosario.siat.bal.iface.model.EnvioOsirisAdapter;
import ar.gov.rosario.siat.bal.iface.model.EnvioOsirisSearchPage;
import ar.gov.rosario.siat.bal.iface.model.EnvioOsirisVO;
import ar.gov.rosario.siat.bal.iface.model.EstadoEnvioVO;
import ar.gov.rosario.siat.bal.iface.model.NotaImptoVO;
import ar.gov.rosario.siat.bal.iface.model.NovedadEnvioVO;
import ar.gov.rosario.siat.bal.iface.model.TipoOperacionVO;
import ar.gov.rosario.siat.bal.iface.model.TranAfipAdapter;
import ar.gov.rosario.siat.bal.iface.model.TranAfipVO;
import ar.gov.rosario.siat.bal.iface.service.IBalEnvioOsirisService;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.gde.buss.bean.DecJur;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.pro.iface.model.ProcesoVO;
import coop.tecso.adpcore.AdpEngine;
import coop.tecso.adpcore.AdpProcess;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.engine.AdpDao;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public class BalEnvioOsirisServiceHbmImpl implements IBalEnvioOsirisService {

	private Logger log = Logger.getLogger(BalEnvioOsirisServiceHbmImpl.class);
	
	// ---> ABM Envio Osiris
	public EnvioOsirisSearchPage getEnvioSearchPageInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			EnvioOsirisSearchPage envioSearchPage = new EnvioOsirisSearchPage();
			
		
			envioSearchPage.setListEstadoEnvio((ArrayList<EstadoEnvioVO>) ListUtilBean.toVO(EstadoEnvio.getListActivos(),new EstadoEnvioVO(-1,StringUtil.SELECT_OPCION_TODOS)));

			log.debug(funcName + ": exit");
			return envioSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EnvioOsirisSearchPage getEnvioSearchPageResult (EnvioOsirisSearchPage envioSearchPage, UserContext userContext)throws DemodaServiceException{
		String funcName=DemodaUtil.currentMethodName();
		
		if(log.isDebugEnabled())log.debug(funcName+": enter");
		
		try{
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			List<EnvioOsiris>listEnvioOsiris = BalDAOFactory.getEnvioOsirisDAO().getListBySearchPage(envioSearchPage);
			
			List<EnvioOsirisVO> listEnvioOsirisVO = new ArrayList<EnvioOsirisVO>();
			for(EnvioOsiris envioOsiris: listEnvioOsiris){
				EnvioOsirisVO envioOsirisVO = (EnvioOsirisVO) envioOsiris.toVO(1,false);
				//Transacciones tipo "DJ" y "DJ Y PAGO" asociadas al EnvioOsiris
				envioOsirisVO.setCanTranDecJur(envioOsiris.getCantTranDecJur().intValue());
				//Transacciones tipo "PAGO" asociadas al EnvioOsiris
				envioOsirisVO.setCanTranPago(envioOsiris.getCantTranPago().intValue());

				long idEstadoEnvio = envioOsiris.getEstadoEnvio().getId().longValue();
				if(EstadoEnvio.ID_ESTADO_PROCESADO_ERROR == idEstadoEnvio){
					List<TranAfip> listTranAfipHasError = envioOsiris.getListTranAfipDJHasError();
					if(envioOsiris.getCanDecJur() != 0 && !ListUtil.isNullOrEmpty(listTranAfipHasError)){
						envioOsirisVO.setGenerarForDecJurBussEnabled(true);
					}
					if(envioOsiris.getCantidadPagos() != 0 && ListUtil.isNullOrEmpty(listTranAfipHasError)){
						envioOsirisVO.setGenerarTransaccionBussEnabled(true);
					}			
				}else if (EstadoEnvio.ID_ESTADO_INCONSISTENTE == idEstadoEnvio){
					envioOsirisVO.setCambiarEstadoBussEnabled(true);
				}
				listEnvioOsirisVO.add(envioOsirisVO);
			}
			
			envioSearchPage.setListResult(listEnvioOsirisVO);
			
			return envioSearchPage;
			
		}catch (Exception e){
			log.error("Service error: ", e);
			throw new DemodaServiceException(e);
		}finally{
			SiatHibernateUtil.closeSession();
		}
	}

	public EnvioOsirisAdapter getEnvioOsirisAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			EnvioOsiris envioOsiris = EnvioOsiris.getById(commonKey.getId());			
			
			EnvioOsirisAdapter envioOsirisAdapter = new EnvioOsirisAdapter();
			
			EnvioOsirisVO envioOsirisVO = (EnvioOsirisVO) envioOsiris.toVO(1,false);
			//Transacciones tipo "DJ" y "DJ Y PAGO" asociadas al EnvioOsiris
			envioOsirisVO.setCanTranDecJur(envioOsiris.getCantTranDecJur().intValue());
			//Transacciones tipo "PAGO" y "DJ Y PAGO" asociadas al EnvioOsiris
			envioOsirisVO.setCanTranPago(envioOsiris.getCantTranPago().intValue());
			//Sumatoria de importe de Transacciones asociadas al EnvioOsiris
			envioOsirisVO.setImportePagos(envioOsiris.getSumTotalImportePagos());
			//Cantidad de Transacciones de pago SIAT			
			envioOsirisVO.setCantidadPagos(BalDAOFactory.getDetallePagoDAO().getCantDetallePago(envioOsiris).intValue());

			envioOsirisVO.setListCierreBanco((ArrayList<CierreBancoVO>) ListUtilBean.toVO(envioOsiris.getListCierreBanco()));
			envioOsirisVO.setListConciliacion((ArrayList<ConciliacionVO>) ListUtilBean.toVO(envioOsiris.getListConciliacion()));
			
			envioOsirisAdapter.setEnvioOsiris(envioOsirisVO);
			
			log.debug(funcName + ": exit");
			return envioOsirisAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EnvioOsirisAdapter obtenerEnviosOsiris(UserContext userContext, EnvioOsirisAdapter envioOsirisAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			envioOsirisAdapter.clearError();
			
			Proceso proceso = Proceso.getByCodigo(Proceso.PROCESO_NOVOSIRIS);
	
			if (proceso.getLocked() != null && proceso.getLocked().intValue() == 1 ) {
				envioOsirisAdapter.addRecoverableError(BalError.ENVIOOSIRIS_NOVEDAD_LOCK_ERROR);
				tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				log.debug(funcName + ": exit");
				return envioOsirisAdapter;
			}
			
			// validamos si no existe una corrida en ejecucion o por comenzar para el proceso
			if (AdpDao.getRunningRunIdByCodProcess(proceso.getCodProceso()) != 0) {
				envioOsirisAdapter.addRecoverableError(BalError.ENVIOOSIRIS_NOVEDAD_EN_EJECUCION_ERROR);
				log.debug(funcName + ": exit");
				tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				return envioOsirisAdapter;
			}					
			
			//	 encontramos el proceso del archivo que cambio. Scheduleamos la corrida ya!
			AdpRun run = AdpRun.newRun(proceso.getId(), "Iniciado por ejecucion manual.");
		
			// llamar a validate
			if(!AdpEngine.validateProcess(run)){
				String msg = run.getMensajeEstado();
				envioOsirisAdapter.addRecoverableValueError(msg);
				log.debug(funcName + ": exit");
				tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				return envioOsirisAdapter;
			}
			
			run.create();
			
			try {
				run.execute(null);
			} catch (Exception e) {
				envioOsirisAdapter.addRecoverableValueError("Error durante la ejecucion del proceso.");
				tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}	
			
			if(!envioOsirisAdapter.hasError()){
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			log.debug(funcName + ": exit");
			return envioOsirisAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Forzar ejecucion de Proceso de Obtencion de Envios
	 * 
	 */
	public EnvioOsirisAdapter getEnvioOsirisAdapterForObtener(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			EnvioOsirisAdapter envioOsirisAdapter = new EnvioOsirisAdapter();
			
			Proceso proceso = Proceso.getByCodigo(Proceso.PROCESO_NOVOSIRIS);
			
			envioOsirisAdapter.setProceso((ProcesoVO) proceso.toVO(1, false));
			
			if(proceso.getTipoEjecucion().getId().longValue() == AdpProcess.TIPO_PERIODIC){
				envioOsirisAdapter.setParamPeriodic(true);
			}else {
				envioOsirisAdapter.setParamPeriodic(false);
			}
			envioOsirisAdapter.setParamForzar(true);
			
			String envios = SiatParam.getString(SiatParam.OBTENERENVIO_ENVIOID, "");
			if (!envios.equals("")) {
				envioOsirisAdapter.addMessageValue("Atencion: Por configuracion del sistema se obtendran los envio_id: " + envios + " de mulator.");
			}
			
			log.debug(funcName + ": exit");
			return envioOsirisAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Forzar ejecucion de Procesamiento de Envios
	 * 
	 */
	public EnvioOsirisAdapter getEnvioOsirisAdapterForProcesar(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			EnvioOsirisAdapter envioOsirisAdapter = new EnvioOsirisAdapter();
			
			Proceso proceso = Proceso.getByCodigo(Proceso.PROCESO_ENVIO_OSIRIS);
			
			envioOsirisAdapter.setProceso((ProcesoVO) proceso.toVO(1, false));
			
			if(proceso.getTipoEjecucion().getId().longValue() == AdpProcess.TIPO_PERIODIC){
				envioOsirisAdapter.setParamPeriodic(true);
			}else {
				envioOsirisAdapter.setParamPeriodic(false);
			}
			envioOsirisAdapter.setParamForzar(true);
			
			String envios = SiatParam.getString(SiatParam.PROCESARENVIO_ENVIOID, "");
			if (!envios.equals("")) {
				envioOsirisAdapter.addMessageValue("Atenci\u00F3n: Por configuraci\u00F3n del sistema se procesar\u00E1n los envio_id: " + envios + " de mulator.");
			}
			
			log.debug(funcName + ": exit");
			return envioOsirisAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EnvioOsirisAdapter procesarEnviosOsiris(UserContext userContext, EnvioOsirisAdapter envioOsirisAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			envioOsirisAdapter.clearError();
			
			Proceso proceso = Proceso.getByCodigo(Proceso.PROCESO_ENVIO_OSIRIS);
	
			if (proceso.getLocked() != null && proceso.getLocked().intValue() == 1 ) {
				envioOsirisAdapter.addRecoverableError(BalError.ENVIOOSIRIS_PROCESO_LOCK_ERROR);
				tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				log.debug(funcName + ": exit");
				return envioOsirisAdapter;
			}
			
			// validamos si no existe una corrida en ejecucion o por comenzar para el proceso
			if (AdpDao.getRunningRunIdByCodProcess(proceso.getCodProceso()) != 0) {
				envioOsirisAdapter.addRecoverableError(BalError.ENVIOOSIRIS_PROCESO_EN_EJECUCION_ERROR);
				log.debug(funcName + ": exit");
				tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				return envioOsirisAdapter;
			}					
			
			//	 encontramos el proceso del archivo que cambio. Scheduleamos la corrida ya!
			AdpRun run = AdpRun.newRun(proceso.getId(), "Iniciado por ejecucion manual.");
			
			// llamar a validate
			if(!AdpEngine.validateProcess(run)){
				envioOsirisAdapter.addRecoverableError(BalError.ENVIOOSIRIS_PROCESO_VALIDACION_ERROR);
				log.debug(funcName + ": exit");
				tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
				return envioOsirisAdapter;
			}
			
			run.create();
			
			try {
				run.execute(null);
			} catch (Exception e) {
				envioOsirisAdapter.addRecoverableValueError("Error durante la ejecucion del proceso.");
				tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}	
			
			if(!envioOsirisAdapter.hasError()){
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			
			log.debug(funcName + ": exit");
			return envioOsirisAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EnvioOsirisAdapter imprimirEnvioOsiris(UserContext userContext, EnvioOsirisAdapter envioOsirisAdapterVO ) throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			EnvioOsiris envioOsiris = EnvioOsiris.getById(envioOsirisAdapterVO.getEnvioOsiris().getId());

			BalDAOFactory.getEnvioOsirisDAO().imprimirGenerico(envioOsiris, envioOsirisAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return envioOsirisAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}

	
	/**
	 * Genera un Archivo de Transaccion para el Envio. Esta opcion solo se utiliza para Envios en estado "Procesado con Error".
	 * 
	 */
	public EnvioOsirisVO generarTransaccion(UserContext userContext, EnvioOsirisVO envioOsirisVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			envioOsirisVO.clearErrorMessages();

			// validamos si no existe una corrida en ejecucion o por comenzar para el proceso masivo de generacion de transacciones para envios osiris
			if (AdpDao.getRunningRunIdByCodProcess(Proceso.PROCESO_ENVIO_OSIRIS) != 0) {
				envioOsirisVO.addRecoverableError(BalError.ENVIOOSIRIS_PROCESO_EN_EJECUCION_ERROR);
				log.debug(funcName + ": exit");
				return envioOsirisVO;
			}		
			
			EnvioOsiris envioOsiris = EnvioOsiris.getById(envioOsirisVO.getId());		
			
			//Tran Pago Error al Generar				
			int tranErr = 0;
			
			// Valida si se generaron los Formularios de Declaracion Jurada para el Envio (cuando corresponde) antes de generar el archivo de transacciones y pasar a Procesado Ok
			if(envioOsiris.getCanDecJur() > 0 && !envioOsiris.existenForDecJurForEnvio()){
				envioOsirisVO.addRecoverableError(BalError.ENVIOOSIRIS_EJECUTAR_GENERAR_DECJUR_ERROR);
				log.debug(funcName + ": exit");
				return envioOsirisVO;
			}
			
			try{
				envioOsiris.generarTransacciones();
			}catch (Exception e) {
				tranErr++;
				envioOsiris.addRecoverableValueError("Envio Osiris de id: "+envioOsiris.getId()+" IdEnvioAfip: "+envioOsiris.getIdEnvioAfip()+". Error inesperado al intentar generar de transacciones de pago.");
			}
			
			if (envioOsiris.hasError()) {
				tx.rollback();
				envioOsiris.setEstadoEnvio(EstadoEnvio.getById(EstadoEnvio.ID_ESTADO_PROCESADO_ERROR));
			} else {
				tx.commit();
				envioOsiris.setEstadoEnvio(EstadoEnvio.getById(EstadoEnvio.ID_ESTADO_PROCESADO_EXITO));
				
				if (DateUtil.isDateBefore(Recurso.getDReI().getFecUltEnvPro(), envioOsiris.getFechaRegistroMulat())) {
					//Actualizo la fecha de ultimo envio procesado para los recursos 
					DefDAOFactory.getRecursoDAO().updateFecUltEnvProForDReIorETuR(envioOsiris.getFechaRegistroMulat());
				}
			}
	
			String observacion = envioOsiris.getObservacion();
			observacion +="\n"+DateUtil.formatDate(new Date(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK);
			observacion+="\n- Se generaron " + envioOsiris.getListTranPago().size() + " Transacciones de Pago. ";
			if (tranErr > 0) {
				observacion+= tranErr + " quedaron con error.\n";
			}
			envioOsiris.setObservacion(observacion);
			
			tx = session.beginTransaction();
			BalDAOFactory.getEnvioOsirisDAO().update(envioOsiris);
			tx.commit();
			
			envioOsirisVO = (EnvioOsirisVO) envioOsiris.toVO(0,false);
			envioOsiris.passErrorMessages(envioOsirisVO);

			
			log.debug(funcName + ": exit");
			return envioOsirisVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	/**
	 * Genera los Formularios de Declaracion Jurada para el Envio. Esta opcion solo se utiliza para Envios en estado "Procesado con Error".
	 * 
	 */
	public EnvioOsirisVO generarDecJur(UserContext userContext, EnvioOsirisVO envioOsirisVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			envioOsirisVO.clearErrorMessages();

			// validamos si no existe una corrida en ejecucion o por comenzar para el proceso masivo de generacion de declaraciones juradas para envios osiris
			if (AdpDao.getRunningRunIdByCodProcess(Proceso.PROCESO_ENVIO_OSIRIS) != 0) {
				envioOsirisVO.addRecoverableError(BalError.ENVIOOSIRIS_PROCESO_EN_EJECUCION_ERROR);
				log.debug(funcName + ": exit");
				return envioOsirisVO;
			}	
						
			EnvioOsiris envioOsiris = EnvioOsiris.getById(envioOsirisVO.getId());			

			// llamo al procesar envio con true (true = reprocesar)
			envioOsiris.procesarEnvio(true);
						
			envioOsirisVO = (EnvioOsirisVO) envioOsiris.toVO(0,false);
			envioOsiris.passErrorMessages(envioOsirisVO);
			
			log.debug(funcName + ": exit");
			return envioOsirisVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	/**
	 * 	Metodo que surge como soporte al circuito definido para procesar los envios inconsistentes.
	 * 	Permite cambiar el estado del envio a "Pendiente de procesamiento".
	 *  Al ejecutarse verifica que la suma de total_monto_ingresado = la suma de los detalles pago.
	 */
	public EnvioOsirisVO cambiarEstado(UserContext userContext, EnvioOsirisVO envioOsirisVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			
			tx = session.beginTransaction();
			envioOsirisVO.clearErrorMessages();
	
			EnvioOsiris envioOsiris = EnvioOsiris.getById(envioOsirisVO.getId());		
			if (!envioOsiris.getEsConcistente()) {
				String err = "No se puede modificar el estado del envío "+envioOsiris.getIdEnvioAfip()+" ya que es inconcistente.";
				envioOsirisVO.addNonRecoverableValueError(err);
				return envioOsirisVO;
			}
			
			EstadoEnvio estadoPendiente = EstadoEnvio.getById(EstadoEnvio.ID_ESTADO_PENDIENTE);
			
			envioOsiris.setEstadoEnvio(estadoPendiente);
			
			String strObs = envioOsiris.getObservacion();
				   strObs +="\n"+DateUtil.formatDate(new Date(), DateUtil.yyyy_MM_dd_HH_MM_SS_MASK);
				   strObs +="\n- Se ha modificado el estado del Envío a 'Pendiente de Procesamiento'.";
			
			envioOsiris.setObservacion(strObs);
			
			 // Aqui la modificacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			envioOsiris = BalEnvioOsirisManager.getInstance().updateEnvioOsiris(envioOsiris);
            if (envioOsiris.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				envioOsirisVO = (EnvioOsirisVO) envioOsiris.toVO(0,false);
			}
			
			envioOsiris.passErrorMessages(envioOsirisVO);
			log.debug(funcName + ": exit");
			return envioOsirisVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}
	
	public CierreBancoAdapter getCierreBancoAdapterForView(	UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			CierreBanco cierreBanco = CierreBanco.getById(commonKey.getId());			
			
			CierreBancoAdapter cierreBancoAdapter = new CierreBancoAdapter();			
			cierreBancoAdapter.setCierreBanco((CierreBancoVO)cierreBanco.toVO(1,false));	
			EnvioOsiris envioOsiris = cierreBanco.getEnvioOsiris();
			
			List<TranAfipVO> listTranAfipVO = new ArrayList<TranAfipVO>();
			for(TranAfip tranAfip: cierreBanco.getListTranAfip()){
				TranAfipVO tranAfipVO = (TranAfipVO) tranAfip.toVO(0,false);
				tranAfipVO.setTipoOperacion((TipoOperacionVO) tranAfip.getTipoOperacion().toVO(0,false));
				if(tranAfip.getEstTranAfip().getId().longValue() == EstTranAfip.ID_PENDIENTE)
					tranAfipVO.setGenerarDecJurBussEnabled(true);
				else
					tranAfipVO.setGenerarDecJurBussEnabled(false);
				
				
				if(envioOsiris.getEstadoEnvio().getId().longValue() == EstadoEnvio.ID_ESTADO_INCONSISTENTE)
					tranAfipVO.setEliminarTranAfipBussEnabled(true);
				else
					tranAfipVO.setEliminarTranAfipBussEnabled(false);

				/*
				if(tranAfip.getEstTranAfip().getId().longValue() != EstTranAfip.ID_PROCESADO_OK)
					tranAfipVO.setEliminarTranAfipBussEnabled(true);
				else
					tranAfipVO.setEliminarTranAfipBussEnabled(false);
				*/
				listTranAfipVO.add(tranAfipVO);
			}
		
			cierreBancoAdapter.getCierreBanco().setListTranAfip(listTranAfipVO);
			cierreBancoAdapter.getCierreBanco().setListNotaImpto((ArrayList<NotaImptoVO>) ListUtilBean.toVO(cierreBanco.getListNotaImpto(),1));
			cierreBancoAdapter.getCierreBanco().setListNovedadEnvio((ArrayList<NovedadEnvioVO>) ListUtilBean.toVO(cierreBanco.getListNovedadEnvio(),1));
			cierreBancoAdapter.getCierreBanco().setListCierreSucursal((ArrayList<CierreSucursalVO>) ListUtilBean.toVO(cierreBanco.getListCierreSucursal(),1));
			
			log.debug(funcName + ": exit");
			return cierreBancoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TranAfipAdapter getTranAfipAdapterForView(UserContext userContext,CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			TranAfip tranAfip = TranAfip.getById(commonKey.getId());	
			EnvioOsiris envioOsiris = tranAfip.getEnvioOsiris();
			
			TranAfipAdapter tranAfipAdapter = new TranAfipAdapter();		
			TranAfipVO tranAfipVO = (TranAfipVO)tranAfip.toVO(2,true);	
			
			/*
			if(tranAfip.getEstTranAfip().getId().longValue() != EstTranAfip.ID_PROCESADO_OK)
				tranAfipVO.setEliminarTranAfipBussEnabled(true);
			else 
				tranAfipVO.setEliminarTranAfipBussEnabled(false);
			*/
			
			if(envioOsiris.getEstadoEnvio().getId().longValue() == EstadoEnvio.ID_ESTADO_INCONSISTENTE)
				tranAfipVO.setEliminarTranAfipBussEnabled(true);
			else
				tranAfipVO.setEliminarTranAfipBussEnabled(false);
			
			tranAfipAdapter.setTranAfip(tranAfipVO);
			
			log.debug(funcName + ": exit");
			return tranAfipAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}		
	}

	public DetalleDJAdapter getDetalleDJAdapterForView(UserContext userContext,	CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DetalleDJ detalleDJ = DetalleDJ.getById(commonKey.getId());			
			
			DetalleDJAdapter detalleDJAdapter = new DetalleDJAdapter();		
			detalleDJAdapter.setDetalleDJ((DetalleDJVO)detalleDJ.toVO(1,true));		
			detalleDJAdapter.getDetalleDJ().setDataStrView(detalleDJ.getDataStr());
			detalleDJAdapter.getDetalleDJ().setContenidoParseadoView(detalleDJ.getContenidoParseado());
			if(detalleDJAdapter.getDetalleDJ().getContenidoParseadoView() != null)
				detalleDJAdapter.setParamContenidoParseado(true);
			
			log.debug(funcName + ": exit");
			return detalleDJAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public DetallePagoAdapter getDetallePagoAdapterForView(UserContext userContext,	CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DetallePago detallePago = DetallePago.getById(commonKey.getId());			
			
			DetallePagoAdapter detallePagoAdapter = new DetallePagoAdapter();		
			detallePagoAdapter.setDetallePago((DetallePagoVO)detallePago.toVO(1,true));		
			
			log.debug(funcName + ": exit");
			return detallePagoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	@Deprecated
	public TranAfipVO generarDecJurForTranAfip(UserContext userContext, TranAfipVO tranAfipVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			tranAfipVO.clearErrorMessages();

			TranAfip tranAfip = TranAfip.getById(tranAfipVO.getId());			

			log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> validacion para tranAfip....");
			tranAfip.validar();
			log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> validacion para tranAfip...."+(!tranAfip.hasError()?"OK":"ERROR"));
			
			if(tranAfip.hasError()){
				if(tx != null){
					tx.rollback();
					tx = session.beginTransaction();
				}
				tranAfip.setEstTranAfip(EstTranAfip.getById(EstTranAfip.ID_PROCESADO_ERROR));
				tranAfip.getEnvioOsiris().updateTranAfip(tranAfip);
				if(tx != null) tx.commit();
				tranAfipVO = (TranAfipVO) tranAfip.toVO(0,false);
				tranAfip.passErrorMessages(tranAfipVO);

				log.debug(funcName + ": exit");
				return tranAfipVO;
			}	
			
			log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> generarForDecJur para tranAfip....");
			ForDecJur forDecJur = tranAfip.generarForDecJur();
			log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> generarForDecJur para tranAfip...."+(!tranAfip.hasError()?"OK":"ERROR"));

			// Si no hay error hace un commit para guardar el ForDecJur. Cambia el Estado de la TranAfip a Procesado OK. (Aunque despues no pueda generar DJ Siat)
			if(!tranAfip.hasError()){
				tranAfip.setEstTranAfip(EstTranAfip.getById(EstTranAfip.ID_PROCESADO_OK)); 
			}else{
				if(tx != null){
					tx.rollback();
					tx = session.beginTransaction();
				}
				tranAfip.setEstTranAfip(EstTranAfip.getById(EstTranAfip.ID_PROCESADO_ERROR));
				tranAfip.getEnvioOsiris().updateTranAfip(tranAfip);
				if(tx != null) tx.commit();
				tranAfipVO = (TranAfipVO) tranAfip.toVO(0,false);
				tranAfip.passErrorMessages(tranAfipVO);

				log.debug(funcName + ": exit");
				return tranAfipVO;
			}	
			tranAfip.getEnvioOsiris().updateTranAfip(tranAfip);
			if(tx != null) tx.commit();
			
			tx = session.beginTransaction();
			
			log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> generarDecJur para tranAfip....");
			List<DecJur> listDecJur = forDecJur.generarDecJur();
			log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> generarDecJur para tranAfip...."+(!tranAfip.hasError()?"OK":"ERROR"));
			
			List<LiqDeudaVO> listDeuda = new ArrayList<LiqDeudaVO>();
			if(listDecJur != null && !tranAfip.hasError()){
				log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> listDecJur.size() = "+listDecJur.size());
				// Si termino sin errores pasamos a procesar las declaraciones juradas impactando sobre la deuda
				for (DecJur decJur: listDecJur){
					// procesar ....
					log.debug(" procesarDDJJ " + decJur.getPeriodo() + "/" + decJur.getAnio()+" Cuenta:"+decJur.getCuenta());
					
					decJur.procesarDDJJ(listDeuda,null, true); 
					
					if (decJur.hasError()){
						log.debug(" procesarDDJJ ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						decJur.passErrorMessages(tranAfip);
					}
				}				
			}else{
				log.debug(" generarDecJur ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}
			
			// Cambia el estado del forDecJur a procesado
			if(!tranAfip.hasError()){
				forDecJur.setEstForDecJur(EstForDecJur.getById(EstForDecJur.ID_PROCESADA)); 
				AfiDAOFactory.getForDecJurDAO().update(forDecJur);
				if(tx != null) tx.commit();
			}else{
				if(tx != null){
					tx.rollback();
				}
			}	
			
			tranAfipVO = (TranAfipVO) tranAfip.toVO(0,false);
			tranAfip.passErrorMessages(tranAfipVO);

			log.debug(funcName + ": exit");
			return tranAfipVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	// Inicio Eliminar Transacciones Afip, DetallesDJ y DetallesPago
	public TranAfipVO eliminarTranAfip(UserContext userContext,TranAfipVO tranAfipVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tranAfipVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			TranAfip tranAfip = TranAfip.getById(tranAfipVO.getId());
			
			
			//Borrado
			EnvioOsiris envioOsiris = tranAfip.getEnvioOsiris();
			
			//Borrado de DJ y DP asociados a tranAfip actual
			
			List<DetalleDJ> detallesDJ = tranAfip.getListDetalleDJ();
			List<DetallePago> detallesPago = tranAfip.getListDetallePago();
			
			if(detallesPago!=null) {
				for(DetallePago detallePago:detallesPago){
					tranAfip.deleteDetallePago(detallePago);
				}
			}
			
			if(detallesDJ!=null) {
				for(DetalleDJ detalleDJ:detallesDJ){
					tranAfip.deleteDetalleDJ(detalleDJ);
				}
			}
			
			envioOsiris.deleteTranAfip(tranAfip);
			
			if (tranAfip.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tranAfipVO =  (TranAfipVO) tranAfip.toVO(0,false);
			}
			tranAfip.passErrorMessages(tranAfipVO);
            
            log.debug(funcName + ": exit");
            return tranAfipVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DetalleDJVO eliminarDetalleDJ(UserContext userContext,DetalleDJVO detalleDJVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			detalleDJVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			DetalleDJ detalleDJ = DetalleDJ.getById(detalleDJVO.getId());
			
			
			//Borrado
			TranAfip tranAfip = detalleDJ.getTranAfip();
			tranAfip.deleteDetalleDJ(detalleDJ);
			
			if (detalleDJ.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				detalleDJVO =  (DetalleDJVO) detalleDJ.toVO(0,false);
			}
			detalleDJ.passErrorMessages(detalleDJVO);
            
            log.debug(funcName + ": exit");
            return detalleDJVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DetallePagoVO eliminarDetallePago(UserContext userContext,DetallePagoVO detallePagoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			detallePagoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			DetallePago detallePago = DetallePago.getById(detallePagoVO.getId());
			
			
			//Borrado
			TranAfip tranAfip = detallePago.getTranAfip();
			tranAfip.deleteDetallePago(detallePago);

			if (detallePago.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				detallePagoVO =  (DetallePagoVO) detallePago.toVO(0,false);
			}
			detallePago.passErrorMessages(detallePagoVO);
            
            log.debug(funcName + ": exit");
            return detallePagoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// Fin - Eliminar Transacciones Afip, DetallesDJ y DetallesPago
	
	public ConciliacionAdapter getConciliacionAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Conciliacion conciliacion = Conciliacion.getById(commonKey.getId());			
			
			ConciliacionAdapter conciliacionAdapter = new ConciliacionAdapter();			
			conciliacionAdapter.setConciliacion((ConciliacionVO)conciliacion.toVO(1,true));	
					
			log.debug(funcName + ": exit");
			return conciliacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ConciliacionAdapter imprimirConciliacion(UserContext userContext, ConciliacionAdapter conciliacionAdapterVO ) throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Conciliacion conciliacion = Conciliacion.getById(conciliacionAdapterVO.getConciliacion().getId());

			BalDAOFactory.getConciliacionDAO().imprimirGenerico(conciliacion, conciliacionAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return conciliacionAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}

	
}
