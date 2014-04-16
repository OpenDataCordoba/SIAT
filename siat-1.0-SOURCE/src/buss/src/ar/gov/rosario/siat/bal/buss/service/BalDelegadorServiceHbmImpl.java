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

import ar.gov.rosario.siat.bal.buss.bean.AseDel;
import ar.gov.rosario.siat.bal.buss.bean.BalDelegadorManager;
import ar.gov.rosario.siat.bal.buss.bean.Ejercicio;
import ar.gov.rosario.siat.bal.buss.bean.EstEjercicio;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.AseDelAdapter;
import ar.gov.rosario.siat.bal.iface.model.AseDelSearchPage;
import ar.gov.rosario.siat.bal.iface.model.AseDelVO;
import ar.gov.rosario.siat.bal.iface.model.CorridaProcesoAseDelAdapter;
import ar.gov.rosario.siat.bal.iface.model.EjercicioVO;
import ar.gov.rosario.siat.bal.iface.model.EstEjercicioVO;
import ar.gov.rosario.siat.bal.iface.model.ProcesoAseDelAdapter;
import ar.gov.rosario.siat.bal.iface.service.IBalDelegadorService;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
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
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.UserContext;

public class BalDelegadorServiceHbmImpl implements IBalDelegadorService {

	private Logger log = Logger.getLogger(BalDelegadorServiceHbmImpl.class);
	
	public AseDelVO createAseDel(UserContext userContext,
			AseDelVO aseDelVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			aseDelVO.clearErrorMessages();

			AseDel aseDel = new AseDel();
        
			aseDel.setFechaBalance(aseDelVO.getFechaBalance());			
			Ejercicio ejercicio= Ejercicio.getByIdNull(aseDelVO.getEjercicio().getId());
			aseDel.setEjercicio(ejercicio);
			ServicioBanco servicioBanco= ServicioBanco.getByIdNull(aseDelVO.getServicioBanco().getId());
			if(servicioBanco==null){
				aseDelVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.ASEDEL_SERVICIO_BANCO);
				tx.rollback();
				return aseDelVO;
			}
			aseDel.setServicioBanco(servicioBanco);
	
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_ASEDEL_MANUAL); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(aseDelVO, aseDel, 
        			accionExp, null, aseDel.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (aseDelVO.hasError()){
        		tx.rollback();
        		return aseDelVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	aseDel.setIdCaso(aseDelVO.getIdCaso());
        				
			aseDel.setObservacion(aseDelVO.getObservacion());
			aseDel.setUsuarioAlta(userContext.getUserName());
            aseDel.setEstado(Estado.ACTIVO.getId());
      
            //--> Crear Corrida para aseDel   
            Proceso proceso = Proceso.getByCodigo(Proceso.PROCESO_ASEDEL);
            
            Corrida corrida = null;
            AdpRun run = null;
            if(proceso!=null){
            	String desCorrida = proceso.getDesProceso()+" - "+servicioBanco.getDesServicioBanco()+" - "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK);
            	run = AdpRun.newRun(proceso.getCodProceso(), desCorrida);
            	run.create();
            	
            	corrida = Corrida.getByIdNull(run.getId());
            	//<-- Fin Crear Corrida para aseDel
            }
			aseDel.setCorrida(corrida);

            BalDelegadorManager.getInstance().createAseDel(aseDel); 
            
            if(run!=null && !aseDel.hasError())
            	run.putParameter("idAseDel", aseDel.getId().toString());            	
            
            if (aseDel.hasError()) {
            	tx.rollback();
            	if(run != null)
            		AdpRun.deleteRun(run.getId());
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				aseDelVO =  (AseDelVO) aseDel.toVO(1, false);
			}
            aseDel.passErrorMessages(aseDelVO);
            
            log.debug(funcName + ": exit");
            return aseDelVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AseDelVO deleteAseDel(UserContext userContext,
			AseDelVO aseDelVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			AseDel aseDel = AseDel.getById(aseDelVO.getId());
			
			BalDelegadorManager.getInstance().deleteAseDel(aseDel);				
			
			if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			
            if (aseDel.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				aseDelVO =  (AseDelVO) aseDel.toVO();
			}
            session = SiatHibernateUtil.currentSession();
            tx = session.beginTransaction();
            
            AdpRun.deleteRun(aseDel.getCorrida().getId());

            tx.commit();
            
            aseDel.passErrorMessages(aseDelVO);
            
            log.debug(funcName + ": exit");
            return aseDelVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AseDelAdapter getAseDelAdapterForCreate(
			UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

	        AseDelAdapter aseDelAdapter = new AseDelAdapter();

	    	aseDelAdapter.setListServicioBanco( (ArrayList<ServicioBancoVO>)
					ListUtilBean.toVO(ServicioBanco.getListActivos(),
					new ServicioBancoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));	
	        
	        log.debug(funcName + ": exit");
			return aseDelAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AseDelAdapter getAseDelAdapterForView(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			AseDel aseDel = AseDel.getById(commonKey.getId());			
			
			AseDelAdapter aseDelAdapter = new AseDelAdapter();

			aseDelAdapter.setAseDel((AseDelVO) aseDel.toVO(1, false));
			aseDelAdapter.getAseDel().getEjercicio().setEstEjercicio((EstEjercicioVO) aseDel.getEjercicio().getEstEjercicio().toVO(false));
			aseDelAdapter.getAseDel().getCorrida().setEstadoCorrida((EstadoCorridaVO) aseDel.getCorrida().getEstadoCorrida().toVO(false));
			
			if(aseDel.getEjercicio().getEstEjercicio().getId().longValue() == EstEjercicio.ID_ABIERTO){
				aseDelAdapter.setParamEstadoEjercicio("ABIERTO");
			}else if(aseDel.getEjercicio().getEstEjercicio().getId().longValue() == EstEjercicio.ID_CERRADO){
				aseDelAdapter.setParamEstadoEjercicio("CERRADO");
			}
			
			log.debug(funcName + ": exit");
			return aseDelAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AseDelSearchPage getAseDelSearchPageInit(
			UserContext userContext) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			AseDelSearchPage aseDelSearchPage = new AseDelSearchPage();
		
			aseDelSearchPage.setListEjercicio( (ArrayList<EjercicioVO>)
					ListUtilBean.toVO(Ejercicio.getListActivos(),
					new EjercicioVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			aseDelSearchPage.setListEstadoCorrida( (ArrayList<EstadoCorridaVO>)
					ListUtilBean.toVO(EstadoCorrida.getListActivos(),
					new EstadoCorridaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			aseDelSearchPage.setListServicioBanco( (ArrayList<ServicioBancoVO>)
					ListUtilBean.toVO(ServicioBanco.getListActivos(),
					new ServicioBancoVO(-1, StringUtil.SELECT_OPCION_TODOS)));	
				
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return aseDelSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AseDelSearchPage getAseDelSearchPageResult(
			UserContext userContext,
			AseDelSearchPage aseDelSearchPage)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			aseDelSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<AseDel> listAseDel = BalDAOFactory.getAseDelDAO().getListBySearchPage(aseDelSearchPage);  
		
	   		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

	   		//Aqui pasamos BO a VO
	   		aseDelSearchPage.setListResult(ListUtilBean.toVO(listAseDel,2,false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return aseDelSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public ProcesoAseDelAdapter getProcesoAseDelAdapterInit(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			AseDel aseDel = AseDel.getById(commonKey.getId());
			
			ProcesoAseDelAdapter procesoAseDelAdapter = new ProcesoAseDelAdapter();

			//Datos para el encabezado
			procesoAseDelAdapter.setAseDel((AseDelVO) aseDel.toVO(1, false));
			procesoAseDelAdapter.getAseDel().getEjercicio().setEstEjercicio((EstEjercicioVO) aseDel.getEjercicio().getEstEjercicio().toVO(false));
			procesoAseDelAdapter.getAseDel().getCorrida().setEstadoCorrida((EstadoCorridaVO) aseDel.getCorrida().getEstadoCorrida().toVO(false));
			
			if(aseDel.getEjercicio().getEstEjercicio().getId().longValue() == EstEjercicio.ID_ABIERTO){
				procesoAseDelAdapter.setParamEstadoEjercicio("ABIERTO");
			}else if(aseDel.getEjercicio().getEstEjercicio().getId().longValue() == EstEjercicio.ID_CERRADO){
				procesoAseDelAdapter.setParamEstadoEjercicio("CERRADO");
			}
			// Parametro para conocer el pasoActual (para ubicar botones)
			procesoAseDelAdapter.setParamPaso(aseDel.getCorrida().getPasoActual().toString());
			
			// Obtengo el Paso 1 (si existe)
			PasoCorrida pasoCorrida = aseDel.getCorrida().getPasoCorrida(1);
			if(pasoCorrida!=null){
				procesoAseDelAdapter.setPasoCorrida1((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			// Obtengo el Paso 2 (si existe)
			pasoCorrida = aseDel.getCorrida().getPasoCorrida(2);
			if(pasoCorrida!=null){
				procesoAseDelAdapter.setPasoCorrida2((PasoCorridaVO) pasoCorrida.toVO(1,false));
			}
			
			//Obtengo Reportes para cada Paso
			List<FileCorrida> listFileCorrida1 = FileCorrida.getListByCorridaYPaso(aseDel.getCorrida(), 1);
			if(!ListUtil.isNullOrEmpty(listFileCorrida1)){
				procesoAseDelAdapter.setListFileCorrida1((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida1,0, false));				
			}
			List<FileCorrida> listFileCorrida2 = FileCorrida.getListByCorridaYPaso(aseDel.getCorrida(), 2);
			if(!ListUtil.isNullOrEmpty(listFileCorrida2)){
				procesoAseDelAdapter.setListFileCorrida2((ArrayList<FileCorridaVO>) ListUtilBean.toVO(listFileCorrida2,0, false));				
			}
			
			// Seteo las flags para las acciones de Activar, Cancelar, Reiniciar, Reprogramar y Modificar
			// segun el estado de la corrida.
			if(aseDel.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_PREPARACION
					|| aseDel.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_ESPERA_CONTINUAR){
				procesoAseDelAdapter.setParamActivar(true);
				procesoAseDelAdapter.setParamCancelar(false);
			}else{
				procesoAseDelAdapter.setParamActivar(false);
				if(aseDel.getCorrida().getEstadoCorrida().getId().longValue() != EstadoCorrida.ID_PROCESANDO){
					procesoAseDelAdapter.setParamCancelar(true);					
				}
			}
			if(aseDel.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_ESPERA_COMENZAR){
				procesoAseDelAdapter.setParamReprogramar(true);
			}else{
				procesoAseDelAdapter.setParamReprogramar(false);
			}
			if(aseDel.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_ESPERA_COMENZAR
					|| aseDel.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_ESPERA_CONTINUAR
					|| aseDel.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_PROCESADO_CON_ERROR
					|| aseDel.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_ABORTADO_POR_EXCEPCION){
				procesoAseDelAdapter.setParamReiniciar(true);
			}else{
				procesoAseDelAdapter.setParamReiniciar(false);
			}
			if(aseDel.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_PREPARACION
					|| aseDel.getCorrida().getEstadoCorrida().getId().longValue() == EstadoCorrida.ID_EN_ESPERA_COMENZAR){
				procesoAseDelAdapter.setParamModificar(true);
			}else{
				procesoAseDelAdapter.setParamModificar(false);
			}
		
			log.debug(funcName + ": exit");
			return procesoAseDelAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AseDelVO updateAseDel(UserContext userContext,
			AseDelVO aseDelVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			aseDelVO.clearErrorMessages();
			
			AseDel aseDel = AseDel.getById(aseDelVO.getId());
	        
			if(!aseDelVO.validateVersion(aseDel.getFechaUltMdf())) return aseDelVO;
			
			aseDel.setFechaBalance(aseDelVO.getFechaBalance());			
			Ejercicio ejercicio= Ejercicio.getByIdNull(aseDelVO.getEjercicio().getId());
			aseDel.setEjercicio(ejercicio);
			aseDel.setObservacion(aseDelVO.getObservacion());
			
        	// 2) Esta linea debe ir siempre despues de 1).
        	aseDel.setIdCaso(aseDelVO.getIdCaso());
        	
			BalDelegadorManager.getInstance().updateAseDel(aseDel); 
            
            if (aseDel.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				aseDelVO =  (AseDelVO) aseDel.toVO(1 ,false);
			}
            aseDel.passErrorMessages(aseDelVO);
            
            log.debug(funcName + ": exit");
            return aseDelVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AseDelAdapter getAseDelAdapterParamFechaBalance(UserContext userContext, AseDelAdapter aseDelAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			AseDelVO aseDelVO = aseDelAdapter.getAseDel();

			Ejercicio ejercicio = Ejercicio.getByFechaBalance(aseDelVO.getFechaBalance());
						
			if(ejercicio!=null){
				aseDelVO.setEjercicio((EjercicioVO) ejercicio.toVO(1,false));
				
				if(ejercicio.getEstEjercicio().getId().longValue() == EstEjercicio.ID_ABIERTO){
					aseDelAdapter.setParamEstadoEjercicio("ABIERTO");
				}else if(ejercicio.getEstEjercicio().getId().longValue() == EstEjercicio.ID_CERRADO){
					aseDelAdapter.setParamEstadoEjercicio("CERRADO");
				}
				
			}else {
				aseDelVO.setEjercicio(new EjercicioVO());
				aseDelAdapter.setParamEstadoEjercicio("");
			}
			log.debug(funcName + ": exit");
			return aseDelAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public AseDelVO activar(UserContext userContext, AseDelVO aseDelVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			aseDelVO.clearErrorMessages();
			
			AseDel aseDel = AseDel.getById(aseDelVO.getId());
	        
			if(!aseDelVO.validateVersion(aseDel.getFechaUltMdf())) return aseDelVO;
				
			// Tareas adicionales si se requieren 
			
            if (aseDel.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				aseDelVO =  (AseDelVO) aseDel.toVO(1 ,false);
			}
            aseDel.passErrorMessages(aseDelVO);
            
            log.debug(funcName + ": exit");
            return aseDelVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AseDelVO cancelar(UserContext userContext, AseDelVO aseDelVO) throws DemodaServiceException {
		return aseDelVO;
	}

	public AseDelVO reiniciar(UserContext userContext, AseDelVO aseDelVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			aseDelVO.clearErrorMessages();
			
			AseDel aseDel = AseDel.getById(aseDelVO.getId());
	        
			if(!aseDelVO.validateVersion(aseDel.getFechaUltMdf())) return aseDelVO;
			
			
			AdpRun run = null;
			run = AdpRun.getRun(aseDel.getCorrida().getId());
			if(run!=null){
				run.reset();
			}

			ProDAOFactory.getFileCorridaDAO().deleteAllByAseDel(aseDel);
			ProDAOFactory.getPasoCorridaDAO().deleteAllByAseDel(aseDel);
			ProDAOFactory.getLogCorridaDAO().deleteAllByAseDel(aseDel);
			
            if (aseDel.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				aseDelVO =  (AseDelVO) aseDel.toVO(1 ,false);
			}
            aseDel.passErrorMessages(aseDelVO);
            
            log.debug(funcName + ": exit");
            return aseDelVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AseDelVO reprogramar(UserContext userContext, AseDelVO aseDelVO) throws DemodaServiceException {
		return aseDelVO;
	}

	public CorridaProcesoAseDelAdapter getCorridaProcesoAseDelAdapterForView(UserContext userContext, CommonKey procesoAseDelKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			AseDel aseDel = AseDel.getById(procesoAseDelKey.getId());
			Corrida corrida = aseDel.getCorrida();

			CorridaProcesoAseDelAdapter corridaProcesoAseDelAdapter = new CorridaProcesoAseDelAdapter();
			AseDelVO aseDelVO = (AseDelVO) aseDel.toVO(0);
			aseDelVO.setCorrida((CorridaVO) corrida.toVO(1,false));
			aseDelVO.getCorrida().setHoraInicio(DateUtil.getTimeFromDate(corrida.getFechaInicio()));
			
			corridaProcesoAseDelAdapter.setAseDel(aseDelVO);
			
			log.debug(funcName + ": exit");
			return corridaProcesoAseDelAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
}
