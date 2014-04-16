//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.bean.AuxDeuMdf;
import ar.gov.rosario.siat.bal.buss.bean.BalAsentamientoManager;
import ar.gov.rosario.siat.bal.buss.bean.Ejercicio;
import ar.gov.rosario.siat.bal.buss.bean.EstEjercicio;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.AsentamientoAdapter;
import ar.gov.rosario.siat.bal.iface.model.AsentamientoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.AsentamientoVO;
import ar.gov.rosario.siat.bal.iface.model.CorridaProcesoAsentamientoAdapter;
import ar.gov.rosario.siat.bal.iface.model.EjercicioVO;
import ar.gov.rosario.siat.bal.iface.model.EstEjercicioVO;
import ar.gov.rosario.siat.bal.iface.model.ProcesoAsentamientoAdapter;
import ar.gov.rosario.siat.bal.iface.service.IBalAsentamientoService;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.gde.buss.bean.DeuAdmRecCon;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaManager;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import ar.gov.rosario.siat.pro.buss.bean.PasoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.pro.buss.dao.ProDAOFactory;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.PasoCorridaVO;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

/**
 * Implementacion de servicios del submodulo Asentamiento del modulo Balance
 * @author tecso
 */
public class BalAsentamientoServiceHbmImpl implements IBalAsentamientoService {

	private Logger log = Logger.getLogger(BalAsentamientoServiceHbmImpl.class);
	
	public AsentamientoVO createAsentamiento(UserContext userContext,
			AsentamientoVO asentamientoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			asentamientoVO.clearErrorMessages();

			Asentamiento asentamiento = new Asentamiento();
        
			asentamiento.setFechaBalance(asentamientoVO.getFechaBalance());			
			Ejercicio ejercicio= Ejercicio.getByIdNull(asentamientoVO.getEjercicio().getId());
			asentamiento.setEjercicio(ejercicio);
			ServicioBanco servicioBanco= ServicioBanco.getByIdNull(asentamientoVO.getServicioBanco().getId());
			if(servicioBanco==null){
				asentamientoVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.ASENTAMIENTO_SERVICIO_BANCO);
				tx.rollback();
				return asentamientoVO;
			}
			asentamiento.setServicioBanco(servicioBanco);

			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_ASENTAMIENTO_MANUAL); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(asentamientoVO, asentamiento, 
        			accionExp, null, asentamiento.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (asentamientoVO.hasError()){
        		tx.rollback();
        		return asentamientoVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	asentamiento.setIdCaso(asentamientoVO.getIdCaso());
        				
			asentamiento.setObservacion(asentamientoVO.getObservacion());
			asentamiento.setUsuarioAlta(userContext.getUserName());
            asentamiento.setEstado(Estado.ACTIVO.getId());
      
            //--> Crear Corrida para asentamiento   
            Proceso proceso = Proceso.getByCodigo(Proceso.PROCESO_ASENTAMIENTO);
            
            Corrida corrida = null;
            AdpRun run = null;
            if(proceso!=null){
            	String desCorrida = proceso.getDesProceso()+" - "+servicioBanco.getDesServicioBanco()+" - "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK);
            	run = AdpRun.newRun(proceso.getCodProceso(), desCorrida);
            	run.create();
            	
            	corrida = Corrida.getByIdNull(run.getId());
            	//<-- Fin Crear Corrida para asentamiento
            }
			asentamiento.setCorrida(corrida);

            BalAsentamientoManager.getInstance().createAsentamiento(asentamiento); 
            
            if(run!=null && !asentamiento.hasError())
            	run.putParameter("idAsentamiento", asentamiento.getId().toString());            	
            
            if (asentamiento.hasError()) {
            	tx.rollback();
            	if(run != null)
            		AdpRun.deleteRun(run.getId());
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				asentamientoVO =  (AsentamientoVO) asentamiento.toVO(1, false);
			}
            asentamiento.passErrorMessages(asentamientoVO);
            
            log.debug(funcName + ": exit");
            return asentamientoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AsentamientoVO deleteAsentamiento(UserContext userContext,
			AsentamientoVO asentamientoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Asentamiento asentamiento = Asentamiento.getById(asentamientoVO.getId());
			
			BalAsentamientoManager.getInstance().deleteAsentamiento(asentamiento);				
			/*if(AdpRun.deleteRun(asentamiento.getCorrida().getId())){
			}else {
				asentamiento.addRecoverableError(BalError.ASENTAMIENTO_CORRIDA_NO_ELIMINADA);
			}*/
			
			if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			
            if (asentamiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				asentamientoVO =  (AsentamientoVO) asentamiento.toVO();
			}
            session = SiatHibernateUtil.currentSession();
            tx = session.beginTransaction();
            
            AdpRun.deleteRun(asentamiento.getCorrida().getId());

            tx.commit();
            
            asentamiento.passErrorMessages(asentamientoVO);
            
            log.debug(funcName + ": exit");
            return asentamientoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AsentamientoAdapter getAsentamientoAdapterForCreate(
			UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

	        AsentamientoAdapter asentamientoAdapter = new AsentamientoAdapter();

	    	asentamientoAdapter.setListServicioBanco( (ArrayList<ServicioBancoVO>)
					ListUtilBean.toVO(ServicioBanco.getListActivos(),
					new ServicioBancoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));	
	        
	        log.debug(funcName + ": exit");
			return asentamientoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AsentamientoAdapter getAsentamientoAdapterForView(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Asentamiento asentamiento = Asentamiento.getById(commonKey.getId());			
			
			AsentamientoAdapter asentamientoAdapter = new AsentamientoAdapter();

			asentamientoAdapter.setAsentamiento((AsentamientoVO) asentamiento.toVO(1, false));
			asentamientoAdapter.getAsentamiento().getEjercicio().setEstEjercicio((EstEjercicioVO) asentamiento.getEjercicio().getEstEjercicio().toVO(false));
			asentamientoAdapter.getAsentamiento().getCorrida().setEstadoCorrida((EstadoCorridaVO) asentamiento.getCorrida().getEstadoCorrida().toVO(false));
			
			if(asentamiento.getEjercicio().getEstEjercicio().getId().longValue() == EstEjercicio.ID_ABIERTO){
				asentamientoAdapter.setParamEstadoEjercicio("ABIERTO");
			}else if(asentamiento.getEjercicio().getEstEjercicio().getId().longValue() == EstEjercicio.ID_CERRADO){
				asentamientoAdapter.setParamEstadoEjercicio("CERRADO");
			}
			
			log.debug(funcName + ": exit");
			return asentamientoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AsentamientoSearchPage getAsentamientoSearchPageInit(
			UserContext userContext) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			AsentamientoSearchPage asentamientoSearchPage = new AsentamientoSearchPage();
		
			asentamientoSearchPage.setListEjercicio( (ArrayList<EjercicioVO>)
					ListUtilBean.toVO(Ejercicio.getListActivos(),
					new EjercicioVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			asentamientoSearchPage.setListEstadoCorrida( (ArrayList<EstadoCorridaVO>)
					ListUtilBean.toVO(EstadoCorrida.getListActivos(),
					new EstadoCorridaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			asentamientoSearchPage.setListServicioBanco( (ArrayList<ServicioBancoVO>)
					ListUtilBean.toVO(ServicioBanco.getListActivos(),
					new ServicioBancoVO(-1, StringUtil.SELECT_OPCION_TODOS)));	
				
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return asentamientoSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AsentamientoSearchPage getAsentamientoSearchPageResult(
			UserContext userContext,
			AsentamientoSearchPage asentamientoSearchPage)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			asentamientoSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Asentamiento> listAsentamiento = BalDAOFactory.getAsentamientoDAO().getListBySearchPage(asentamientoSearchPage);  
		
	   		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

	   		//Aqui pasamos BO a VO
	   		asentamientoSearchPage.setListResult(ListUtilBean.toVO(listAsentamiento,2,false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return asentamientoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public ProcesoAsentamientoAdapter getProcesoAsentamientoAdapterInit(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			Asentamiento asentamiento = Asentamiento.getById(commonKey.getId());
			
			ProcesoAsentamientoAdapter procesoAsentamientoAdapter = new ProcesoAsentamientoAdapter();

			//Datos para el encabezado
			procesoAsentamientoAdapter.setAsentamiento((AsentamientoVO) asentamiento.toVO(1, false));
			procesoAsentamientoAdapter.getAsentamiento().getEjercicio().setEstEjercicio((EstEjercicioVO) asentamiento.getEjercicio().getEstEjercicio().toVO(false));
			procesoAsentamientoAdapter.getAsentamiento().getCorrida().setEstadoCorrida((EstadoCorridaVO) asentamiento.getCorrida().getEstadoCorrida().toVO(false));
			
			if(asentamiento.getEjercicio().getEstEjercicio().getId().longValue() == EstEjercicio.ID_ABIERTO){
				procesoAsentamientoAdapter.setParamEstadoEjercicio("ABIERTO");
			}else if(asentamiento.getEjercicio().getEstEjercicio().getId().longValue() == EstEjercicio.ID_CERRADO){
				procesoAsentamientoAdapter.setParamEstadoEjercicio("CERRADO");
			}
			// Parametro para conocer el pasoActual (para ubicar botones)
			procesoAsentamientoAdapter.setParamPaso(asentamiento.getCorrida().getPasoActual().toString());
			
			// Obtengo el Paso 1 (si existe)
			PasoCorrida pasoCorrida = asentamiento.getCorrida().getPasoCorrida(1);
			if(pasoCorrida!=null){
				procesoAsentamientoAdapter.setPasoCorrida1((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			// Obtengo el Paso 2 (si existe)
			pasoCorrida = asentamiento.getCorrida().getPasoCorrida(2);
			if(pasoCorrida!=null){
				procesoAsentamientoAdapter.setPasoCorrida2((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			// Obtengo el Paso 3 (si existe)
			pasoCorrida = asentamiento.getCorrida().getPasoCorrida(3);
			if(pasoCorrida!=null){
				procesoAsentamientoAdapter.setPasoCorrida3((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			
			//Obtengo Reportes para cada Paso
			List<FileCorrida> listFileCorrida1 = FileCorrida.getListByCorridaYPaso(asentamiento.getCorrida(), 1);
			if(!ListUtil.isNullOrEmpty(listFileCorrida1)){
				procesoAsentamientoAdapter.setListFileCorrida1((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida1,0, false));				
			}
			List<FileCorrida> listFileCorrida2 = FileCorrida.getListByCorridaYPaso(asentamiento.getCorrida(), 2);
			if(!ListUtil.isNullOrEmpty(listFileCorrida2)){
				procesoAsentamientoAdapter.setListFileCorrida2((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida2,0, false));				
			}
			List<FileCorrida> listFileCorrida3 = FileCorrida.getListByCorridaYPaso(asentamiento.getCorrida(), 3);
			if(!ListUtil.isNullOrEmpty(listFileCorrida3)){
				procesoAsentamientoAdapter.setListFileCorrida3((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida3,0, false));				
			}
			
			// Seteo las flags para las acciones de Activar, Cancelar, Reiniciar, Reprogramar y Modificar
			// segun el estado de la corrida.
			if(asentamiento.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_PREPARACION
					|| asentamiento.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_ESPERA_CONTINUAR){
				procesoAsentamientoAdapter.setParamActivar(true);
				procesoAsentamientoAdapter.setParamCancelar(false);
			}else{
				procesoAsentamientoAdapter.setParamActivar(false);
				if(asentamiento.getCorrida().getEstadoCorrida().getId().longValue() != EstadoCorrida.ID_PROCESANDO
						&& asentamiento.getCorrida().getEstadoCorrida().getId().longValue() != EstadoCorrida.ID_PROCESADO_CON_EXITO){
					procesoAsentamientoAdapter.setParamCancelar(true);					
				}
			}
			if(asentamiento.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_ESPERA_COMENZAR){
				procesoAsentamientoAdapter.setParamReprogramar(true);
			}else{
				procesoAsentamientoAdapter.setParamReprogramar(false);
			}
			if(asentamiento.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_ESPERA_COMENZAR
					|| asentamiento.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_ESPERA_CONTINUAR
					|| asentamiento.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_PROCESADO_CON_ERROR
					|| asentamiento.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_ABORTADO_POR_EXCEPCION){
				procesoAsentamientoAdapter.setParamReiniciar(true);
			}else{
				procesoAsentamientoAdapter.setParamReiniciar(false);
			}
			if(asentamiento.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_PREPARACION
					|| asentamiento.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_ESPERA_COMENZAR){
				procesoAsentamientoAdapter.setParamModificar(true);
			}else{
				procesoAsentamientoAdapter.setParamModificar(false);
			}
			
			// Se setea el parametro Continuar, usado en el paso 3 para terminar un asentamiento que se detuvo por falla
			// imprevista.
			if(asentamiento.getCorrida().getPasoActual() == 3){ 
				pasoCorrida = asentamiento.getCorrida().getPasoCorrida(3);
				if(pasoCorrida != null && pasoCorrida.getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_PROCESADO_CON_ERROR){
					procesoAsentamientoAdapter.setParamContinuar(true);		
					procesoAsentamientoAdapter.setParamReiniciar(false);
					procesoAsentamientoAdapter.setParamCancelar(false);
				}else{
					procesoAsentamientoAdapter.setParamContinuar(false);
				}
				if(pasoCorrida != null && pasoCorrida.getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_PROCESANDO){
					procesoAsentamientoAdapter.setParamContinuar(false);		
					procesoAsentamientoAdapter.setParamReiniciar(false);
					procesoAsentamientoAdapter.setParamCancelar(false);
					procesoAsentamientoAdapter.setParamReprogramar(false);
				}
			}else{
				procesoAsentamientoAdapter.setParamContinuar(false);
			}
			
			// imprevista.
			procesoAsentamientoAdapter.setParamForzar(false);
			if(asentamiento.getCorrida().getPasoActual() == 2){ 
				pasoCorrida = asentamiento.getCorrida().getPasoCorrida(2);
				if(pasoCorrida != null && pasoCorrida.getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_PROCESADO_CON_ERROR){
					procesoAsentamientoAdapter.setParamForzar(true);
				}
			}
			
			// Setea una bandera si el Asentamiento se encuentra asociado a un Proceso de Balance
			if(asentamiento.getBalance() != null){
				procesoAsentamientoAdapter.setTieneBalanceAsociado(true);
			}
			
			log.debug(funcName + ": exit");
			return procesoAsentamientoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AsentamientoVO updateAsentamiento(UserContext userContext,
			AsentamientoVO asentamientoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			asentamientoVO.clearErrorMessages();
			
			Asentamiento asentamiento = Asentamiento.getById(asentamientoVO.getId());
	        
			if(!asentamientoVO.validateVersion(asentamiento.getFechaUltMdf())) return asentamientoVO;
			
			asentamiento.setFechaBalance(asentamientoVO.getFechaBalance());			
			Ejercicio ejercicio= Ejercicio.getByIdNull(asentamientoVO.getEjercicio().getId());
			asentamiento.setEjercicio(ejercicio);
			asentamiento.setObservacion(asentamientoVO.getObservacion());
			
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_ASENTAMIENTO_MANUAL); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(asentamientoVO, asentamiento, 
        			accionExp, null, asentamiento.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (asentamientoVO.hasError()){
        		tx.rollback();
        		return asentamientoVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	asentamiento.setIdCaso(asentamientoVO.getIdCaso());
        	
			BalAsentamientoManager.getInstance().updateAsentamiento(asentamiento); 
            
            if (asentamiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				asentamientoVO =  (AsentamientoVO) asentamiento.toVO(1 ,false);
			}
            asentamiento.passErrorMessages(asentamientoVO);
            
            log.debug(funcName + ": exit");
            return asentamientoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AsentamientoAdapter getAsentamientoAdapterParamFechaBalance(UserContext userContext, AsentamientoAdapter asentamientoAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			AsentamientoVO asentamientoVO = asentamientoAdapter.getAsentamiento();

			Ejercicio ejercicio = Ejercicio.getByFechaBalance(asentamientoVO.getFechaBalance());
						
			if(ejercicio!=null){
				asentamientoVO.setEjercicio((EjercicioVO) ejercicio.toVO(1,false));
				
				if(ejercicio.getEstEjercicio().getId().longValue() == EstEjercicio.ID_ABIERTO){
					asentamientoAdapter.setParamEstadoEjercicio("ABIERTO");
				}else if(ejercicio.getEstEjercicio().getId().longValue() == EstEjercicio.ID_CERRADO){
					asentamientoAdapter.setParamEstadoEjercicio("CERRADO");
				}
				
			}else {
				asentamientoVO.setEjercicio(new EjercicioVO());
				asentamientoAdapter.setParamEstadoEjercicio("");
			}
			log.debug(funcName + ": exit");
			return asentamientoAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public AsentamientoVO activar(UserContext userContext, AsentamientoVO asentamientoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			asentamientoVO.clearErrorMessages();
			
			Asentamiento asentamiento = Asentamiento.getById(asentamientoVO.getId());
	        
			if(!asentamientoVO.validateVersion(asentamiento.getFechaUltMdf())) return asentamientoVO;
				
			AdpRun run = null;
			run = AdpRun.getRun(asentamiento.getCorrida().getId());
			if(run!=null){
				if(asentamientoVO.isLogDetalladoEnabled()){
					run.putParameter("LOG_DETALLADO", "true");				
				}else{
					run.putParameter("LOG_DETALLADO", "false");
				}
			}
			
            if (asentamiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				asentamientoVO =  (AsentamientoVO) asentamiento.toVO(1 ,false);
			}
            asentamiento.passErrorMessages(asentamientoVO);
            
            log.debug(funcName + ": exit");
            return asentamientoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AsentamientoVO cancelar(UserContext userContext, AsentamientoVO asentamientoVO) throws DemodaServiceException {
		return asentamientoVO;
	}

	public AsentamientoVO reiniciar(UserContext userContext, AsentamientoVO asentamientoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			asentamientoVO.clearErrorMessages();
			
			Asentamiento asentamiento = Asentamiento.getById(asentamientoVO.getId());
	        
			if(!asentamientoVO.validateVersion(asentamiento.getFechaUltMdf())) return asentamientoVO;
			
			// Restaurar deuda autoliquidable modificada y eliminar registros temporales de AuxDeuMdf
			for(AuxDeuMdf auxDeuMdf: asentamiento.getListAuxDeuMdf()){
				// Por cada auxDeuMdf buscar la deuda, y elimina o corrige los valores de la deuda y la lista de conceptos 
				if(auxDeuMdf.getEsNueva().intValue() == SiNo.NO.getId().intValue()){
					DeudaAdmin deuda = DeudaAdmin.getById(auxDeuMdf.getIdDeuda());
					if(deuda != null){
						deuda.setImporte(auxDeuMdf.getImporteOrig());
						deuda.setImporteBruto(auxDeuMdf.getImporteOrig());
						deuda.setSaldo(auxDeuMdf.getSaldoOrig());
						
						List<DeuAdmRecCon> listDeuAdmRecCon = deuda.getListDeuRecCon();
						for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
							RecCon recCon = deuAdmRecCon.getRecCon();
							deuAdmRecCon.setImporte(NumberUtil.truncate(deuda.getImporte()*recCon.getPorcentaje(), SiatParam.DEC_IMPORTE_DB));
							deuAdmRecCon.setImporteBruto(deuAdmRecCon.getImporte());
						}
						deuda.setStrConceptosByListRecCon(listDeuAdmRecCon);
						deuda.setReclamada(0);
						
						GdeDAOFactory.getDeudaDAO().update(deuda);
						
						for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
							GdeDAOFactory.getDeuAdmRecConDAO().update(deuAdmRecCon);
						}									
					}else{
						log.error("NO SE ENCONTRO DEUDA DE ID: "+auxDeuMdf.getIdDeuda()+" EN gde_deudaAdmin.");	
					}
				}else{
					DeudaAdmin deuda = DeudaAdmin.getById(auxDeuMdf.getIdDeuda());
					if(deuda != null){
						List<DeuAdmRecCon> listDeuAdmRecCon = deuda.getListDeuRecCon();
						for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
							deuda.deleteDeuAdmRecCon(deuAdmRecCon);
						}
						session.flush();
						
						GdeGDeudaManager.getInstance().deleteDeudaAdmin(deuda);						
					}else{
						log.error("NO SE ENCONTRO DEUDA DE ID: "+auxDeuMdf.getIdDeuda()+" EN gde_deudaAdmin.");	
					}
				}
			}
			// Eliminar los registros de auxDeuMdf
			BalDAOFactory.getAuxDeuMdfDAO().deleteAllByAsentamiento(asentamiento);
			
			// Eliminar las tablas temporales asociadas al Asentamiento:
			// auxConDeu, auxConDeuCuo, auxPagCuo, auxPagDeu, auxRecaudado, auxSellado, auxConvenio, sinPartida,
			// sinIndet, sinSalAFav, transaccion, auxImpRec.
			// Eliminar los registros de archivos generados en pro_fileCorrida, pro_pasoCorrida, pro_logCorrida
			BalDAOFactory.getAuxRecaudadoDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getAuxSelladoDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getAuxPagDeuDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getAuxPagCuoDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getAuxConvenioDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getAuxConDeuDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getAuxConDeuCuoDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getSinPartidaDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getSinIndetDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getSinSalAFavDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getAuxImpRecDAO().deleteAllByAsentamiento(asentamiento);
			// Unicamente eliminamos las transacciones cuando el Asentamiento no esta asociado a un Balance
			if(asentamiento.getBalance() == null)
				BalDAOFactory.getTransaccionDAO().deleteAllByAsentamiento(asentamiento);
			// Si esta asociad a un Balance debemos setear esIndet de las transacciones en 0 para volver a estado inicial
			if(asentamiento.getBalance() != null)
				BalDAOFactory.getTransaccionDAO().reiniciarAllByAsentamiento(asentamiento);
			
			
			AdpRun run = null;
			run = AdpRun.getRun(asentamiento.getCorrida().getId());
			if(run!=null){
				run.reset();
			}

			ProDAOFactory.getFileCorridaDAO().deleteAllByAsentamiento(asentamiento);
			ProDAOFactory.getPasoCorridaDAO().deleteAllByAsentamiento(asentamiento);
			ProDAOFactory.getLogCorridaDAO().deleteAllByAsentamiento(asentamiento);
			
            if (asentamiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				asentamientoVO =  (AsentamientoVO) asentamiento.toVO(1 ,false);
			}
            asentamiento.passErrorMessages(asentamientoVO);
            
            log.debug(funcName + ": exit");
            return asentamientoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AsentamientoVO reprogramar(UserContext userContext, AsentamientoVO asentamientoVO) throws DemodaServiceException {
		return asentamientoVO;
	}

	public CorridaProcesoAsentamientoAdapter getCorridaProcesoAsentamientoAdapterForView(UserContext userContext, CommonKey procesoAsentamientoKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Asentamiento asentamiento = Asentamiento.getById(procesoAsentamientoKey.getId());
			Corrida corrida = asentamiento.getCorrida();

			CorridaProcesoAsentamientoAdapter corridaProcesoAsentamientoAdapter = new CorridaProcesoAsentamientoAdapter();
			AsentamientoVO asentamientoVO = (AsentamientoVO) asentamiento.toVO(0);
			asentamientoVO.setCorrida((CorridaVO) corrida.toVO(1,false));
			asentamientoVO.getCorrida().setHoraInicio(DateUtil.getTimeFromDate(corrida.getFechaInicio()));
			
			corridaProcesoAsentamientoAdapter.setAsentamiento(asentamientoVO);
			
			log.debug(funcName + ": exit");
			return corridaProcesoAsentamientoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public AsentamientoVO forzar(UserContext userContext, AsentamientoVO asentamientoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			asentamientoVO.clearErrorMessages();
			
			Asentamiento asentamiento = Asentamiento.getById(asentamientoVO.getId());
	        
			if(!asentamientoVO.validateVersion(asentamiento.getFechaUltMdf())) return asentamientoVO;

			// Restaurar deuda autoliquidable modificada y eliminar registros temporales de AuxDeuMdf
			for(AuxDeuMdf auxDeuMdf: asentamiento.getListAuxDeuMdf()){
				// Por cada auxDeuMdf buscar la deuda, y elimina o corrige los valores de la deuda y la lista de conceptos 
				if(auxDeuMdf.getEsNueva().intValue() == SiNo.NO.getId().intValue()){
					DeudaAdmin deuda = DeudaAdmin.getById(auxDeuMdf.getIdDeuda());
					if(deuda != null){
						deuda.setImporte(auxDeuMdf.getImporteOrig());
						deuda.setImporteBruto(auxDeuMdf.getImporteOrig());
						deuda.setSaldo(auxDeuMdf.getSaldoOrig());
						
						List<DeuAdmRecCon> listDeuAdmRecCon = deuda.getListDeuRecCon();
						for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
							RecCon recCon = deuAdmRecCon.getRecCon();
							deuAdmRecCon.setImporte(NumberUtil.truncate(deuda.getImporte()*recCon.getPorcentaje(), SiatParam.DEC_IMPORTE_DB));
							deuAdmRecCon.setImporteBruto(deuAdmRecCon.getImporte());
						}
						deuda.setStrConceptosByListRecCon(listDeuAdmRecCon);
						deuda.setReclamada(0);
						
						GdeDAOFactory.getDeudaDAO().update(deuda);
						
						for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
							GdeDAOFactory.getDeuAdmRecConDAO().update(deuAdmRecCon);
						}									
					}else{
						log.error("NO SE ENCONTRO DEUDA DE ID: "+auxDeuMdf.getIdDeuda()+" EN gde_deudaAdmin.");	
					}
				}else{
					DeudaAdmin deuda = DeudaAdmin.getById(auxDeuMdf.getIdDeuda());
					if(deuda != null){
						List<DeuAdmRecCon> listDeuAdmRecCon = deuda.getListDeuRecCon();
						for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
							deuda.deleteDeuAdmRecCon(deuAdmRecCon);
						}
						session.flush();
						
						GdeGDeudaManager.getInstance().deleteDeudaAdmin(deuda);						
					}else{
						log.error("NO SE ENCONTRO DEUDA DE ID: "+auxDeuMdf.getIdDeuda()+" EN gde_deudaAdmin.");	
					}
				}
			}
			// Eliminar los registros de auxDeuMdf
			BalDAOFactory.getAuxDeuMdfDAO().deleteAllByAsentamiento(asentamiento);
			
			// Eliminar las tablas temporales asociadas al Asentamiento:
			// auxConDeu, auxConDeuCuo, auxPagCuo, auxPagDeu, auxRecaudado, auxSellado, auxConvenio, sinPartida,
			// sinIndet, sinSalAFav y auxImpRec.

			BalDAOFactory.getAuxRecaudadoDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getAuxSelladoDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getAuxPagDeuDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getAuxPagCuoDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getAuxConvenioDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getAuxConDeuDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getAuxConDeuCuoDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getSinPartidaDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getSinIndetDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getSinSalAFavDAO().deleteAllByAsentamiento(asentamiento);
			BalDAOFactory.getAuxImpRecDAO().deleteAllByAsentamiento(asentamiento);
	
			AdpRun run = null;
			run = AdpRun.getRun(asentamiento.getCorrida().getId());
			
			if(run!=null){
				if(asentamientoVO.isLogDetalladoEnabled()){
					run.putParameter("LOG_DETALLADO", "true");				
				}else{
					run.putParameter("LOG_DETALLADO", "false");
				}
				run.putParameter("FORZADO", "true");
			}
			
            if (asentamiento.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				asentamientoVO =  (AsentamientoVO) asentamiento.toVO(1 ,false);
			}
            asentamiento.passErrorMessages(asentamientoVO);
            
            log.debug(funcName + ": exit");
            return asentamientoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

}
