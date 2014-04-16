//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.service;

/**
 * Implementacion de servicios del submodulo Definicion del modulo Exe
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.exe.buss.bean.ContribExe;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.exe.buss.bean.EstadoCueExe;
import ar.gov.rosario.siat.exe.buss.bean.ExeDefinicionManager;
import ar.gov.rosario.siat.exe.buss.bean.ExeRecCon;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.buss.bean.TipSujExe;
import ar.gov.rosario.siat.exe.buss.bean.TipoSujeto;
import ar.gov.rosario.siat.exe.buss.dao.ExeDAOFactory;
import ar.gov.rosario.siat.exe.iface.model.ContribExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.ContribExeSearchPage;
import ar.gov.rosario.siat.exe.iface.model.ContribExeVO;
import ar.gov.rosario.siat.exe.iface.model.EstadoCueExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.EstadoCueExeSearchPage;
import ar.gov.rosario.siat.exe.iface.model.EstadoCueExeVO;
import ar.gov.rosario.siat.exe.iface.model.ExeRecConAdapter;
import ar.gov.rosario.siat.exe.iface.model.ExeRecConVO;
import ar.gov.rosario.siat.exe.iface.model.ExencionAdapter;
import ar.gov.rosario.siat.exe.iface.model.ExencionSearchPage;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.exe.iface.model.TipSujExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.TipSujExeVO;
import ar.gov.rosario.siat.exe.iface.model.TipoSujetoAdapter;
import ar.gov.rosario.siat.exe.iface.model.TipoSujetoSearchPage;
import ar.gov.rosario.siat.exe.iface.model.TipoSujetoVO;
import ar.gov.rosario.siat.exe.iface.service.IExeDefinicionService;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.pad.buss.bean.Broche;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.PadContribuyenteManager;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.buss.bean.PersonaFacade;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.BrocheVO;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TipoEstadoCueExe;
import coop.tecso.demoda.iface.model.UserContext;

public class ExeDefinicionServiceHbmImpl implements IExeDefinicionService {
	private Logger log = Logger.getLogger(ExeDefinicionServiceHbmImpl.class);
	
	// ---> ABM Exencion
	public ExencionSearchPage getExencionSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ExencionSearchPage exencionSearchPage = new ExencionSearchPage();
			
			// Aqui obtiene lista de BOs
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());

			exencionSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				exencionSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
			// Seteo del id para que sea nulo
			exencionSearchPage.getExencion().getRecurso().setId(-1L);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return exencionSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ExencionSearchPage getExencionSearchPageResult(UserContext userContext, ExencionSearchPage exencionSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			exencionSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Exencion> listExencion = ExeDAOFactory.getExencionDAO().getBySearchPage(exencionSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		exencionSearchPage.setListResult(ListUtilBean.toVO(listExencion,1));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return exencionSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ExencionAdapter getExencionAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Exencion exencion = Exencion.getById(commonKey.getId());

	        ExencionAdapter exencionAdapter = new ExencionAdapter();
	        exencionAdapter.setExencion((ExencionVO) exencion.toVO(1));
	        
	        exencionAdapter.getExencion().setListExeRecCon(ListUtilBean.toVO(exencion.getListExeRecCon(), 2));
			
			log.debug(funcName + ": exit");
			return exencionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ExencionAdapter getExencionAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ExencionAdapter exencionAdapter = new ExencionAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());

			exencionAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				exencionAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			// Seteo del id para que sea nulo
			exencionAdapter.getExencion().getRecurso().setId(-1L);
			
			exencionAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			
			log.debug(funcName + ": exit");
			return exencionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ExencionAdapter getExencionAdapterForUpdate(UserContext userContext, CommonKey commonKeyExencion) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Exencion exencion = Exencion.getById(commonKeyExencion.getId());

	        ExencionAdapter exencionAdapter = new ExencionAdapter();
	        exencionAdapter.setExencion((ExencionVO) exencion.toVO(2));

			// Seteo la lista para combo, valores, etc
	        List<Recurso> listRecurso = Recurso.getListVigentes(new Date());

	        exencionAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				exencionAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			exencionAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			log.debug(funcName + ": exit");
			return exencionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ExencionVO createExencion(UserContext userContext, ExencionVO exencionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			exencionVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Exencion exencion = new Exencion();
            
            // 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_TIPO_EXENCION); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(exencionVO, exencion, 
        			accionExp, null, exencion.infoString());
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (exencionVO.hasError()){
        		tx.rollback();
        		return exencionVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	exencion.setIdCaso(exencionVO.getIdCaso());
            
            Recurso recurso = Recurso.getByIdNull(exencionVO.getRecurso().getId());
            exencion.setRecurso(recurso);
            
            exencion.setCodExencion(exencionVO.getCodExencion());
            exencion.setDesExencion(exencionVO.getDesExencion());
            exencion.setMontoMinimo(exencionVO.getMontoMinimo());
            exencion.setAplicaMinimo(exencionVO.getAplicaMinimo().getBussId());
            exencion.setAfectaEmision(exencionVO.getAfectaEmision().getBussId());
            exencion.setActualizaDeuda(exencionVO.getActualizaDeuda().getBussId());
            exencion.setEnviaJudicial(exencionVO.getEnviaJudicial().getBussId());
            exencion.setEnviaCyQ(exencionVO.getEnviaCyQ().getBussId());
            exencion.setEsParcial(exencionVO.getEsParcial().getBussId());
            exencion.setPermiteManPad(exencionVO.getPermiteManPad().getBussId());
            
            exencion.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            exencion = ExeDefinicionManager.getInstance().createExencion(exencion);
            
            if (exencion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				exencionVO =  (ExencionVO) exencion.toVO(3);
			}
			exencion.passErrorMessages(exencionVO);
            
            log.debug(funcName + ": exit");
            return exencionVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ExencionVO updateExencion(UserContext userContext, ExencionVO exencionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			exencionVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Exencion exencion = Exencion.getById(exencionVO.getId());
            
            if(!exencionVO.validateVersion(exencion.getFechaUltMdf())) return exencionVO;

            // 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_TIPO_EXENCION); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(exencionVO, exencion, 
        			accionExp, null, exencion.infoString());
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (exencionVO.hasError()){
        		tx.rollback();
        		return exencionVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	exencion.setIdCaso(exencionVO.getIdCaso());
        	
            Recurso recurso = Recurso.getByIdNull(exencionVO.getRecurso().getId());
            exencion.setRecurso(recurso);
            
            exencion.setCodExencion(exencionVO.getCodExencion());
            exencion.setDesExencion(exencionVO.getDesExencion());
            exencion.setMontoMinimo(exencionVO.getMontoMinimo());
            exencion.setAplicaMinimo(exencionVO.getAplicaMinimo().getBussId());
            exencion.setAfectaEmision(exencionVO.getAfectaEmision().getBussId());
            exencion.setActualizaDeuda(exencionVO.getActualizaDeuda().getBussId());
            exencion.setEnviaJudicial(exencionVO.getEnviaJudicial().getBussId());
            exencion.setEnviaCyQ(exencionVO.getEnviaCyQ().getBussId());
            exencion.setEsParcial(exencionVO.getEsParcial().getBussId());
            exencion.setPermiteManPad(exencionVO.getPermiteManPad().getBussId());
            
            exencion.setEstado(Estado.ACTIVO.getId());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            exencion = ExeDefinicionManager.getInstance().updateExencion(exencion);
            
            if (exencion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				exencionVO =  (ExencionVO) exencion.toVO(3);
			}
			exencion.passErrorMessages(exencionVO);
            
            log.debug(funcName + ": exit");
            return exencionVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ExencionVO deleteExencion(UserContext userContext, ExencionVO exencionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			exencionVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Exencion exencion = Exencion.getById(exencionVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			exencion = ExeDefinicionManager.getInstance().deleteExencion(exencion);
			
			if (exencion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				exencionVO =  (ExencionVO) exencion.toVO(3);
			}
			exencion.passErrorMessages(exencionVO);
            
            log.debug(funcName + ": exit");
            return exencionVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ExencionVO activarExencion(UserContext userContext, ExencionVO exencionVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Exencion exencion = Exencion.getById(exencionVO.getId());

            exencion.activar();

            if (exencion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				exencionVO =  (ExencionVO) exencion.toVO();
			}
            exencion.passErrorMessages(exencionVO);
            
            log.debug(funcName + ": exit");
            return exencionVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ExencionVO desactivarExencion(UserContext userContext, ExencionVO exencionVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Exencion exencion = Exencion.getById(exencionVO.getId());

            exencion.desactivar();

            if (exencion.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				exencionVO =  (ExencionVO) exencion.toVO();
			}
            exencion.passErrorMessages(exencionVO);
            
            log.debug(funcName + ": exit");
            return exencionVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	
	public ExencionAdapter imprimirExencion(UserContext userContext, ExencionAdapter exencionAdapter) throws  DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Exencion exencion = Exencion.getById(exencionAdapter.getExencion().getId());

			PadDAOFactory.getContribuyenteDAO().imprimirGenerico(exencion, exencionAdapter.getReport());
	   		
			log.debug(funcName + ": exit");
			return exencionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
	}
	}
	// <--- ABM Exencion

	
	//	 ---> ABM ExeRecCon 	
	public ExeRecConAdapter getExeRecConAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ExeRecCon exeRecCon = ExeRecCon.getById(commonKey.getId());

	        ExeRecConAdapter exeRecConAdapter = new ExeRecConAdapter();
	        exeRecConAdapter.setExeRecCon((ExeRecConVO) exeRecCon.toVO(1));
			
	        exeRecConAdapter.getExeRecCon().getExencion().setRecurso((RecursoVO) exeRecCon.getExencion().getRecurso().toVO());
	        
			log.debug(funcName + ": exit");
			return exeRecConAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ExeRecConAdapter getExeRecConAdapterForCreate(UserContext userContext, ExeRecConAdapter exeRecConAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ExeRecConAdapter exeRecConAdapter = new ExeRecConAdapter();
			
			// Seteo la listas para combos, etc
			Exencion exencion = Exencion.getById(exeRecConAdapterVO.getExeRecCon().getExencion().getId());
			exeRecConAdapter.getExeRecCon().setExencion((ExencionVO) exencion.toVO(1));
			
			List<RecCon> listRecCon = RecCon.getListVigentesByIdRecurso(exencion.getRecurso().getId(), exeRecConAdapterVO.getListVOExcluidos(), new Date());
			
			exeRecConAdapter.setListRecCon(ListUtilBean.toVO(listRecCon, new RecConVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return exeRecConAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ExeRecConAdapter getExeRecConAdapterForUpdate(UserContext userContext, CommonKey commonKeyExeRecCon) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ExeRecCon exeRecCon = ExeRecCon.getById(commonKeyExeRecCon.getId());

	        ExeRecConAdapter exeRecConAdapter = new ExeRecConAdapter();
	        exeRecConAdapter.setExeRecCon((ExeRecConVO) exeRecCon.toVO(1));
	        
	        exeRecConAdapter.getExeRecCon().getExencion().setRecurso((RecursoVO) exeRecCon.getExencion().getRecurso().toVO());
	        
			log.debug(funcName + ": exit");
			return exeRecConAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ExeRecConVO createExeRecCon(UserContext userContext, ExeRecConVO exeRecConVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			exeRecConVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			Exencion exencion = Exencion.getById(exeRecConVO.getExencion().getId());
			
            ExeRecCon exeRecCon = new ExeRecCon();
            
            // Load From BO
            RecCon recCon = RecCon.getByIdNull(exeRecConVO.getRecCon().getId());
            exeRecCon.setRecCon(recCon);            
            exeRecCon.setExencion(exencion);
            exeRecCon.setPorcentaje(exeRecConVO.getPorcentaje());
            exeRecCon.setMontoFijo(exeRecConVO.getMontoFijo());
            exeRecCon.setFechaDesde(exeRecConVO.getFechaDesde());
            exeRecCon.setFechaHasta(exeRecConVO.getFechaHasta()); 
            
            exeRecCon.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            exeRecCon = exencion.createExeRecCon(exeRecCon);
            
            if (exeRecCon.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				exeRecConVO =  (ExeRecConVO) exeRecCon.toVO(3);
			}
			exeRecCon.passErrorMessages(exeRecConVO);
            
            log.debug(funcName + ": exit");
            return exeRecConVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ExeRecConVO updateExeRecCon(UserContext userContext, ExeRecConVO exeRecConVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			exeRecConVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ExeRecCon exeRecCon = ExeRecCon.getById(exeRecConVO.getId());
            
            if(!exeRecConVO.validateVersion(exeRecCon.getFechaUltMdf())) return exeRecConVO;
            
            Exencion exencion = exeRecCon.getExencion();
            
            //Load From VO
            RecCon recCon = RecCon.getByIdNull(exeRecConVO.getRecCon().getId());
            exeRecCon.setRecCon(recCon);            
            exeRecCon.setExencion(exencion);
            exeRecCon.setPorcentaje(exeRecConVO.getPorcentaje());
            exeRecCon.setMontoFijo(exeRecConVO.getMontoFijo());
            exeRecCon.setFechaDesde(exeRecConVO.getFechaDesde());
            exeRecCon.setFechaHasta(exeRecConVO.getFechaHasta());
            
            exeRecCon.setEstado(Estado.ACTIVO.getId());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            exeRecCon = exencion.updateExeRecCon(exeRecCon);
            
            
            if (exeRecCon.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				exeRecConVO =  (ExeRecConVO) exeRecCon.toVO(3);
			}
			exeRecCon.passErrorMessages(exeRecConVO);
            
            log.debug(funcName + ": exit");
            return exeRecConVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ExeRecConVO deleteExeRecCon(UserContext userContext, ExeRecConVO exeRecConVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			exeRecConVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			ExeRecCon exeRecCon = ExeRecCon.getById(exeRecConVO.getId());
			Exencion exencion = exeRecCon.getExencion();
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			exeRecCon = exencion.deleteExeRecCon(exeRecCon);
			
			if (exeRecCon.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				exeRecConVO =  (ExeRecConVO) exeRecCon.toVO(3);
			}
			exeRecCon.passErrorMessages(exeRecConVO);
            
            log.debug(funcName + ": exit");
            return exeRecConVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM ExeRecCon

	
	// ---> ABM ContribExe
	public ContribExeSearchPage getContribExeSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ContribExeSearchPage contribExeSearchPage = new ContribExeSearchPage();

			List<ExencionVO> listExencionVO = ListUtilBean.toVO
				(Exencion.getListActivos(),new ExencionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			contribExeSearchPage.setListExencion(listExencionVO);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return contribExeSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ContribExeSearchPage getContribExeSearchPageParamContribuyente(UserContext userContext, 
		ContribExeSearchPage contribExeSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			contribExeSearchPage.clearError();

			Long idContribuyente = contribExeSearchPage.getContribExe().getContribuyente().getId();

			// recupero en contribuyente
			Contribuyente contribuyente = Contribuyente.getById(idContribuyente);

			contribExeSearchPage.getContribExe().setContribuyente
				((ContribuyenteVO) contribuyente.toVO(4));
			
			log.debug(funcName + ": exit");
			return contribExeSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ContribExeSearchPage getContribExeSearchPageResult(UserContext userContext, ContribExeSearchPage contribExeSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			contribExeSearchPage.clearError();
			
			Date fechaDesde = contribExeSearchPage.getFechaDesde();
			Date fechaHasta = contribExeSearchPage.getFechaHasta();			

			// se cargaron las fechas desde y hasta valido el rango
			if (fechaDesde != null && fechaHasta != null) {
				if ( DateUtil.isDateBefore(fechaHasta, fechaDesde) ) {

					contribExeSearchPage.addRecoverableError(BaseError.MSG_VALORMENORQUE, 
						ExeError.CONTRIBEXE_FECHAHASTA, ExeError.CONTRIBEXE_FECHADESDE);

				}
			}

			// si no hubo error realizo la busqueda
			if (!contribExeSearchPage.hasError()) {
		   		List<ContribExe> listContribExe = 
		   			ExeDAOFactory.getContribExeDAO().getBySearchPage(contribExeSearchPage);  

		   		List listResult = new ArrayList();
		   		for (ContribExe contribExe : listContribExe) {
		   			Persona persona = Persona.getById(contribExe.getContribuyente().getPersona().getId());
		   			
		   			ContribExeVO contribExeVO = (ContribExeVO) contribExe.toVO(1);
		   			contribExeVO.getContribuyente().setPersona((PersonaVO) persona.toVO());
		   			
		   			listResult.add(contribExeVO);
		   		}
		   		contribExeSearchPage.setListResult(listResult);
			}

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return contribExeSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ContribExeAdapter getContribExeAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
	        ContribExeAdapter contribExeAdapter = new ContribExeAdapter();

	        ContribExe contribExe = ContribExe.getById(commonKey.getId());
	        Persona persona = Persona.getById(contribExe.getContribuyente().getPersona().getId());
	
   			ContribExeVO contribExeVO = (ContribExeVO) contribExe.toVO(2, false);
   			contribExeVO.getContribuyente().setPersona((PersonaVO) persona.toVO(2));
   			contribExeAdapter.setContribExe(contribExeVO);
			
   			log.debug(funcName + ": exit");
			return contribExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ContribExeAdapter getContribExeAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ContribExeAdapter contribExeAdapter = new ContribExeAdapter();
			
			
			List<Exencion> listExencion = Exencion.getListActivos();
			
			List<ExencionVO> listExencionVO = new ArrayList<ExencionVO>(); 
				
			listExencionVO.add(new ExencionVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR));			
			
			for (Exencion exencion:listExencion){
				String desExencion = exencion.getRecurso().getDesRecurso() + " - " + exencion.getDesExencion(); 
				
				ExencionVO exencionVO = new ExencionVO(exencion.getId().intValue(), desExencion);
				listExencionVO.add(exencionVO);
			}
			
			contribExeAdapter.setListExencion(listExencionVO);
			
			log.debug(funcName + ": exit");
			return contribExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ContribExeAdapter getContribExeAdapterParamContribuyente(UserContext userContext, 
		ContribExeAdapter contribExeAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			contribExeAdapter.clearError();

			Long idContribuyente = contribExeAdapter.getContribExe().getContribuyente().getId();
			ContribuyenteVO contribuyenteVO = null;
			Persona persona = PersonaFacade.getInstance().getPersonaById(idContribuyente);
			contribuyenteVO = new ContribuyenteVO();
			contribuyenteVO.setPersona((PersonaVO) persona.toVO(2));
			contribuyenteVO.setId(idContribuyente);
			
			contribExeAdapter.getContribExe().setContribuyente(contribuyenteVO);
			
			log.debug(funcName + ": exit");
			return contribExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ContribExeAdapter getContribExeAdapterForUpdate(UserContext userContext, CommonKey commonKeyContribExe) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ContribExe contribExe = ContribExe.getById(commonKeyContribExe.getId());

	        ContribExeAdapter contribExeAdapter = new ContribExeAdapter();

	        Persona persona = Persona.getById(contribExe.getContribuyente().getPersona().getId());

			List<ExencionVO> listExencionVO = ListUtilBean.toVO
				(Exencion.getListActivos(),new ExencionVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR));			
			contribExeAdapter.setListExencion(listExencionVO);

   			ContribExeVO contribExeVO = (ContribExeVO) contribExe.toVO(2, false);
   			contribExeVO.getContribuyente().setPersona((PersonaVO) persona.toVO(2));
   			   			
   			contribExeAdapter.setContribExe(contribExeVO);
   			
   			if (!ModelUtil.isNullOrEmpty(contribExeAdapter.getContribExe().getExencion())){
				contribExeAdapter.setPoseeExencion(true);
			}
   			
			log.debug(funcName + ": exit");
			return contribExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ContribExeVO createContribExe(UserContext userContext, ContribExeVO contribExeVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			contribExeVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ContribExe contribExe = new ContribExe();

            contribExe.setDesContribExe(contribExeVO.getDesContribExe());
            contribExe.setExencion(Exencion.getByIdNull(contribExeVO.getExencion().getId()));
            contribExe.setFechaDesde(contribExeVO.getFechaDesde());
            contribExe.setFechaHasta(contribExeVO.getFechaHasta());

            Long idPersona = contribExeVO.getContribuyente().getPersona().getId();
            
            if (idPersona == null) {
            	contribExeVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CONTRIBUYENTE_LABEL);
            	return contribExeVO;
    		}
            
            // recupero el contribuyente
            Contribuyente contribuyente = Contribuyente.getByIdNull(idPersona);

            // si no existe lo creo a partir de la persona
            if (contribuyente == null) {
            	
            	Persona persona = Persona.getByIdNull(idPersona);
            	
            	// si la persona existe creo el contribuyente
            	if (persona != null) {
            		// creo al contribuyente a partir de la persona
            		contribuyente = PadContribuyenteManager.getInstance().createContribuyente(persona);
            	}
            }
            
            // seteo el contribuyente
            contribExe.setContribuyente(contribuyente);

            // si tiene borche lo asignamos
			Broche broche = Broche.getByIdNull(contribExeVO.getBroche().getId());
			contribExe.setBroche(broche);
            
            // Aqui la creacion esta delegada en el manager
            //contribExe = ExeDefinicionManager.getInstance().createContribExe(contribExe);

            
            // *****************************************************************
            
			// Obtenemos las cuentas que pueda poseer el contribuyente
			// Por cada Cuenta:
			//		Si ContribExe tiene broche, y solo si la cuenta tiene broche nulo, se lo seteamos a la cuenta.
			// 		Si la cuenta no posee la exencion, 
			//		             o si la posee en un estado distinto de Ha Lugar
			//		             o si los periodos no se solapan.
			
			// Validaciones de negocio
			if (!contribExe.validateCreate()) {
				contribExe.passErrorMessages(contribExeVO);
	            return contribExeVO;				
			}

			ExeDAOFactory.getContribExeDAO().update(contribExe);
			log.debug(funcName + " Creo contribExe");
			
			tx.commit();
			
			Long idRecurso = contribExe.getExencion().getRecurso().getId();
			log.debug(funcName + " Recurso de la exencion: " + contribExe.getExencion().getRecurso().getDesRecurso());
			
			EstadoCueExe estadoHaLugar = EstadoCueExe.getById(EstadoCueExe.ID_HA_LUGAR); 
			
			// Si se selecciona una exencion.
			List<Exencion> listExencion = new ArrayList<Exencion>();
			if (contribExe.getExencion() != null){
				listExencion.add(contribExe.getExencion());
			}
			
			long skip = 0;
			long first = 1000;
			
			boolean existenCuentas = true;
			
			log.debug(funcName + " idContribuyente: " + contribExe.getContribuyente().getId());
			
			while (existenCuentas) {
				
				SiatHibernateUtil.closeSession();
				session = SiatHibernateUtil.currentSession();
				tx = session.beginTransaction();
				
				List<Cuenta> listCuentas = contribExe.getContribuyente().getListCuentaVigentesForTitular(skip, first);
				
				log.debug(funcName + " listCuentasVigentes.size: " + listCuentas.size());

				existenCuentas = (listCuentas.size() > 0);

				if(existenCuentas){
				
					for(Cuenta cuenta :listCuentas){
						log.debug(funcName + " nroCuenta: "+cuenta.getNumeroCuenta()+" id: "+cuenta.getId() + " recurso: " + cuenta.getRecurso().getDesRecurso());
						
						// Seteo de Broche solo para cuentas TGI.
						if (Recurso.COD_RECURSO_TGI.equals(cuenta.getRecurso().getCodRecurso()) &&  
								contribExe.getBroche() != null && contribExe.getBroche().getId() != null){
							cuenta.setBroche(contribExe.getBroche());
							PadDAOFactory.getCuentaDAO().update(cuenta);
							log.debug(funcName + " Broche asignado: " + contribExe.getBroche().getId() + " - " + contribExe.getBroche().getDesBroche());
						}
						
						// Creacion de CueExe, Si se selecciona, corresponde al mismo recurso y no existe una vigente para la cuenta.
						if (contribExe.getExencion() != null){
							if (cuenta.getRecurso().getId().longValue() == idRecurso.longValue()){
								if (!cuenta.tieneAlgunaExencion(listExencion, contribExe.getFechaDesde())) {
									//crea la exencion
									CueExe cueExe = new CueExe();
									cueExe.setCuenta(cuenta);
									cueExe.setExencion(contribExe.getExencion());
									cueExe.setFechaDesde(contribExe.getFechaDesde());
									cueExe.setEstadoCueExe(estadoHaLugar);
									
									ExeDAOFactory.getCueExeDAO().update(cueExe);
									log.debug(funcName + " CueExe creada: " + cueExe.getId());
								} else {
									log.debug(funcName + " La cuenta ya posee la exencion");
								}
								
							} else {
								log.debug(funcName + " No se crea exencion por no ser recurso");
							}
						}
					}
					
					/*session.flush();
					session.clear();*/
					
					tx.commit();
					SiatHibernateUtil.closeSession();
					session = SiatHibernateUtil.currentSession();
					tx = session.beginTransaction();
					
					skip += first; // incremento el indice del 1er registro
					log.info( skip + " Cuentas procesadas ");
					
				}
			
			} // while existenCuentas
			
			
			tx.commit();
			log.debug(funcName + " FIN createContribExe()");

			contribExe.passErrorMessages(contribExeVO);
            
            log.debug(funcName + ": exit");
            return contribExeVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ContribExeVO updateContribExe(UserContext userContext, ContribExeVO contribExeVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			contribExeVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ContribExe contribExe = ContribExe.getById(contribExeVO.getId());
            
            if(!contribExeVO.validateVersion(contribExe.getFechaUltMdf())) return contribExeVO;
            
            contribExe.setDesContribExe(contribExeVO.getDesContribExe());
            //contribExe.setContribuyente(Contribuyente.getByIdNull(contribExeVO.getContribuyente().getId()));            
            contribExe.setExencion(Exencion.getByIdNull(contribExeVO.getExencion().getId()));
            contribExe.setFechaDesde(contribExeVO.getFechaDesde());
            contribExe.setFechaHasta(contribExeVO.getFechaHasta());
            
            Long idPersona = contribExeVO.getContribuyente().getPersona().getId();

            // recupero el contribuyente
            Contribuyente contribuyente = Contribuyente.getByIdNull(idPersona);

            // si no existe lo creo a partir de la persona
            if (contribuyente == null) {
            	
            	Persona persona = Persona.getByIdNull(idPersona);
            	
            	// si la persona existe creo el contribuyente
            	if (persona != null) {
            		// creo al contribuyente a partir de la persona
            		contribuyente = PadContribuyenteManager.getInstance().createContribuyente(persona);
            	}
            }
            
            // seteo el contribuyente
            contribExe.setContribuyente(contribuyente);
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            contribExe = ExeDefinicionManager.getInstance().updateContribExe(contribExe);
            
            if (contribExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				contribExeVO =  (ContribExeVO) contribExe.toVO(1);
			}
			contribExe.passErrorMessages(contribExeVO);
            
            log.debug(funcName + ": exit");
            return contribExeVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ContribExeVO deleteContribExe(UserContext userContext, ContribExeVO contribExeVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			contribExeVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			ContribExe contribExe = ContribExe.getById(contribExeVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			contribExe = ExeDefinicionManager.getInstance().deleteContribExe(contribExe);
			
			if (contribExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				contribExeVO =  (ContribExeVO) contribExe.toVO(1);
			}
			contribExe.passErrorMessages(contribExeVO);
            
            log.debug(funcName + ": exit");
            return contribExeVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ContribExeVO activarContribExe(UserContext userContext, ContribExeVO contribExeVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            ContribExe contribExe = ContribExe.getById(contribExeVO.getId());

            contribExe.activar();

            if (contribExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				contribExeVO =  (ContribExeVO) contribExe.toVO();
			}
            contribExe.passErrorMessages(contribExeVO);
            
            log.debug(funcName + ": exit");
            return contribExeVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ContribExeVO desactivarContribExe(UserContext userContext, ContribExeVO contribExeVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            ContribExe contribExe = ContribExe.getById(contribExeVO.getId());

            contribExe.desactivar();

            if (contribExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				contribExeVO =  (ContribExeVO) contribExe.toVO();
			}
            contribExe.passErrorMessages(contribExeVO);
            
            log.debug(funcName + ": exit");
            return contribExeVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	

	//<-- Asignar / Quitar / Modificar Broche de Cuenta

	/**
	 * Asigna un nuevo broche una cuenta
	 * @return broche asignado
	 */
	public ContribExeVO paramAsignarBroche(UserContext userContext, ContribExeVO contribExeVO, CommonKey idBroche) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		Session session = null;
		Transaction tx = null; 
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			ContribExe contribExe = ContribExe.getById(contribExeVO.getId());
			Broche broche = Broche.getById(idBroche.getId());

			contribExe.setBroche(broche);
			
            if (contribExe.hasError()) {
            	contribExe.passErrorMessages(contribExeVO);
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

			return contribExeVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
        	tx.rollback();			
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		}
	}

	/**
	 * Quita el broche de una cuenta
	 */
	public ContribExeVO paramQuitarBroche(UserContext userContext, ContribExeVO contribExeVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Session session = null;
		Transaction tx = null; 

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			contribExeVO.clearError();
			
			ContribExe contribExe = ContribExe.getById(contribExeVO.getId());
			contribExe.setBroche(null);				
            if (contribExe.hasError()) {
            	contribExe.passErrorMessages(contribExeVO);
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

			return contribExeVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
        	tx.rollback();			
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		}
	}

	public BrocheVO paramGetBroche(UserContext userContext, CommonKey keyBroche)  throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			Broche broche = Broche.getById(keyBroche.getId());

			return (BrocheVO) broche.toVO(1,false);
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		}		
	}
	// <--- ABM ContribExe

	// <--- ABM Tipo Sujeto	
    public TipoSujetoSearchPage getTipoSujetoSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			TipoSujetoSearchPage tipoSujetoSearchPage = new TipoSujetoSearchPage();

			//Aqui realizar validaciones
			Recurso recurso = Recurso.getByCodigo(Recurso.COD_RECURSO_TGI);			
		//	tipoSujetoSearchPage.setRecursoTGI((RecursoVO) recurso.toVO());
				   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tipoSujetoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoSujetoSearchPage getTipoSujetoSearchPageResult(UserContext userContext, TipoSujetoSearchPage tipoSujetoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			tipoSujetoSearchPage.clearError();

			//Aqui realizar validaciones
		
			
			// Aqui obtiene lista de BOs
		
	   		
			List<TipoSujeto> listTipoSujeto = ExeDAOFactory.getTipoSujetoDAO().getBySearchPage(tipoSujetoSearchPage);  
			//Aqui pasamos BO a VO
			tipoSujetoSearchPage.setListResult(ListUtilBean.toVO(listTipoSujeto, 1));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tipoSujetoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
	public TipoSujetoAdapter getTipoSujetoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoSujeto tipoSujeto = TipoSujeto.getById(commonKey.getId());

			TipoSujetoAdapter tipoSujetoAdapter = new TipoSujetoAdapter();
			tipoSujetoAdapter.setTipoSujeto(tipoSujeto.toVOforView());
			
			log.debug(funcName + ": exit");
			return tipoSujetoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TipoSujetoAdapter getTipoSujetoAdapterForUpdate(UserContext userContext, CommonKey commonKey)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoSujeto tipoSujeto = TipoSujeto.getById(commonKey.getId());

			TipoSujetoAdapter tipoSujetoAdapter = new TipoSujetoAdapter();
			tipoSujetoAdapter.setTipoSujeto((TipoSujetoVO) tipoSujeto.toVOforView());
			
			log.debug(funcName + ": exit");
			return tipoSujetoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
		
	} 
	
	public TipoSujetoAdapter getTipoSujetoAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		TipoSujetoAdapter tipoSujetoAdapter = new TipoSujetoAdapter();
									
		log.debug(funcName + ": exit");
		return tipoSujetoAdapter;
	
	}
	
	public TipoSujetoVO createTipoSujeto(UserContext userContext, TipoSujetoVO tipoSujetoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoSujetoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			TipoSujeto tipoSujeto = new TipoSujeto();
            
			tipoSujeto.setCodTipoSujeto(tipoSujetoVO.getCodTipoSujeto());
			tipoSujeto.setDesTipoSujeto(tipoSujetoVO.getDesTipoSujeto());
			tipoSujeto.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            tipoSujeto = ExeDefinicionManager.getInstance().createTipoSujeto(tipoSujeto);
            
            if (tipoSujeto.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoSujetoVO = (TipoSujetoVO) tipoSujeto.toVO(1, true);
			}

            tipoSujeto.passErrorMessages(tipoSujetoVO);
            
            log.debug(funcName + ": exit");
            return tipoSujetoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TipoSujetoVO updateTipoSujeto(UserContext userContext, TipoSujetoVO tipoSujetoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
	
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoSujetoVO.clearErrorMessages(); 
			
			TipoSujeto tipoSujeto = TipoSujeto.getById(tipoSujetoVO.getId());

			tipoSujeto.setCodTipoSujeto(tipoSujetoVO.getCodTipoSujeto());
			tipoSujeto.setDesTipoSujeto(tipoSujetoVO.getDesTipoSujeto());

            tipoSujeto = ExeDefinicionManager.getInstance().updateTipoSujeto(tipoSujeto);
            
            if (tipoSujeto.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

            tipoSujeto.passErrorMessages(tipoSujetoVO);

			
			log.debug(funcName + ": exit");
			return tipoSujetoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TipoSujetoVO deleteTipoSujeto (UserContext userContext, TipoSujetoVO tipoSujetoVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoSujetoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			TipoSujeto tipoSujeto = TipoSujeto.getById(tipoSujetoVO.getId());
		
		
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			tipoSujeto = ExeDefinicionManager.getInstance().deleteTipoSujeto(tipoSujeto);
				
			
			if (tipoSujeto.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoSujetoVO =  (TipoSujetoVO) tipoSujeto.toVO(1);
			}
			tipoSujeto.passErrorMessages(tipoSujetoVO);
            
            log.debug(funcName + ": exit");
            return tipoSujetoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
	public TipoSujetoVO activarTipoSujeto(UserContext userContext, TipoSujetoVO tipoSujetoVO)throws DemodaServiceException {
     String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			TipoSujeto tipoSujeto = TipoSujeto.getById(tipoSujetoVO.getId());

			tipoSujeto.activar();

            if (tipoSujeto.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoSujetoVO =  (TipoSujetoVO) tipoSujeto.toVO();
			}
            tipoSujeto.passErrorMessages(tipoSujetoVO);
            
            log.debug(funcName + ": exit");
            return tipoSujetoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	
	}
	public TipoSujetoVO desactivarTipoSujeto (UserContext userContext, TipoSujetoVO tipoSujetoVO)throws DemodaServiceException {
        String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			TipoSujeto tipoSujeto = TipoSujeto.getById(tipoSujetoVO.getId());

			tipoSujeto.desactivar();

            if (tipoSujeto.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoSujetoVO =  (TipoSujetoVO) tipoSujeto.toVO();
			}
            tipoSujeto.passErrorMessages(tipoSujetoVO);
            
            log.debug(funcName + ": exit");
            return tipoSujetoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		
		}		
	}

	public TipSujExeVO createTipSujExe(UserContext userContext, TipSujExeVO tipSujExeVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			TipoSujeto tipoSujeto = TipoSujeto.getById(tipSujExeVO.getTipoSujeto().getId());
			Exencion exencion = Exencion.getByIdNull(tipSujExeVO.getExencion().getId());
			
			TipSujExe tipSujExe = new TipSujExe();
			tipSujExe.setTipoSujeto(tipoSujeto);
			tipSujExe.setExencion(exencion);
			
			tipSujExe = tipoSujeto.createTipSujExe(tipSujExe);

            if (tipSujExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipSujExeVO =  (TipSujExeVO) tipSujExe.toVO(0, false);
			}
            tipSujExe.passErrorMessages(tipSujExeVO);
            
            log.debug(funcName + ": exit");
            return tipSujExeVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		
		}
	}

	public TipSujExeVO deleteTipSujExe(UserContext userContext, TipSujExeVO tipSujExeVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipSujExeVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			TipoSujeto tipoSujeto = TipoSujeto.getById(tipSujExeVO.getTipoSujeto().getId());
		
			TipSujExe tipSujExe = TipSujExe.getById(tipSujExeVO.getId());
		
			tipSujExe = tipoSujeto.deleteTipSujExe(tipSujExe);			
			
			if (tipSujExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipSujExeVO =  (TipSujExeVO) tipSujExe.toVO(0, false);
			}
			tipSujExe.passErrorMessages(tipSujExeVO);
            
            log.debug(funcName + ": exit");
            return tipSujExeVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipSujExeAdapter getTipSujExeAdapterForCreate(UserContext userContext, CommonKey ckTipoSujeto) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipoSujeto tipoSujeto = TipoSujeto.getById(ckTipoSujeto.getId());

			TipSujExeAdapter tipSujExeAdapter = new TipSujExeAdapter();
			tipSujExeAdapter.getTipSujExe().setTipoSujeto((TipoSujetoVO) tipoSujeto.toVO(2, false));
			
			List<Recurso> listRecurso = Recurso.getListVigentes(new Date());

			tipSujExeAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				tipSujExeAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			// Seteo del id para que sea nulo
			tipSujExeAdapter.getTipSujExe().getExencion().getRecurso().setId(-1L);

			List<ExencionVO> listExencion = new ArrayList<ExencionVO>();
			listExencion.add(new ExencionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			tipSujExeAdapter.setListExencion(listExencion);
			
			log.debug(funcName + ": exit");
			return tipSujExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
		
	}

	public TipSujExeAdapter getTipSujExeAdapterParamRecurso(UserContext userContext,TipSujExeAdapter tipSujExeAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Long idRecurso = tipSujExeAdapter.getTipSujExe().getExencion().getRecurso().getId();
			List<Exencion> listExencion = Exencion.getListActivosByIdRecurso(idRecurso);
			
			tipSujExeAdapter.setListExencion(ListUtilBean.toVO(listExencion, 
					new ExencionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));			
			
			log.debug(funcName + ": exit");
			return tipSujExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipSujExeAdapter getTipSujExeAdapterForView(UserContext userContext,CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			TipSujExe tipSujExe = TipSujExe.getById(commonKey.getId());

			TipSujExeAdapter tipSujExeAdapter = new TipSujExeAdapter();
			tipSujExeAdapter.setTipSujExe((TipSujExeVO)tipSujExe.toVO(2, false));
						
			log.debug(funcName + ": exit");
			return tipSujExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Tipo Sujeto
	
	// <--- ABM Estado Cuenta/Exencion
	public EstadoCueExeSearchPage getEstadoCueExeSearchPageInit(UserContext userContext)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			// Aqui obtiene lista de BOs
			
			EstadoCueExeSearchPage estadoCueExeSearchPage = new EstadoCueExeSearchPage();
             
			estadoCueExeSearchPage.setListTipoEstadoCueExe(TipoEstadoCueExe.getList());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return estadoCueExeSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public EstadoCueExeSearchPage getEstadoCueExeSearchPageResult(UserContext userContext, EstadoCueExeSearchPage estadoCueExeSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			estadoCueExeSearchPage.clearError();
			
			log.debug("estado seleccionado:" + estadoCueExeSearchPage.getTipoEstadoCueExe());
	        // Aqui obtiene lista de BOs
			List<EstadoCueExe> listEstadoCueExe = ExeDAOFactory.getEstadoCueExeDAO().getBySearchPage(estadoCueExeSearchPage);  

			
			//Aqui pasamos BO a VO
			estadoCueExeSearchPage.setListResult(ListUtilBean.toVO(listEstadoCueExe,1));
			
			Long[] arrIdNoModif = new Long[]{ EstadoCueExe.ID_CREADA,
											EstadoCueExe.ID_EN_ANALISIS,
											EstadoCueExe.ID_MODIFICACION_DATOS,	
											EstadoCueExe.ID_HA_LUGAR,
											EstadoCueExe.ID_NO_HA_LUGAR,
											EstadoCueExe.ID_REVOCADA,
											EstadoCueExe.ID_PRORROGADA,
											EstadoCueExe.ID_DESESTIMACION,
											EstadoCueExe.ID_ENVIADO_SYNTIS,
											EstadoCueExe.ID_ENVIADO_DIR_GRAL,
											EstadoCueExe.ID_ENVIADO_CATASTRO,
											EstadoCueExe.ID_REVOCACION_PARCIAL };
			
	   		for(EstadoCueExeVO estadoCueExe:(ArrayList<EstadoCueExeVO>)estadoCueExeSearchPage.getListResult()){	   			
	   			// Cambio solicita en bug 657, que se puedan administrar estados. 
	   			// No vamos a permitir administrar los estado reservados
	   			/*if(TipoEstadoCueExe.getByCod(estadoCueExe.getTipo()) == TipoEstadoCueExe.TIPOESTADO) {
	   				estadoCueExe.setModificarBussEnabled(false);
	   				estadoCueExe.setEliminarBussEnabled(false);
	   			}else{
	   				estadoCueExe.setModificarBussEnabled(true);
	   				estadoCueExe.setEliminarBussEnabled(true);
	   			}*/
	   			
	   			if ( ListUtil.isInArrLong(estadoCueExe.getId(), arrIdNoModif) ){
	   				estadoCueExe.setModificarBussEnabled(false);
	   				estadoCueExe.setEliminarBussEnabled(false);
	   			} else {
	   				estadoCueExe.setModificarBussEnabled(true);
	   				estadoCueExe.setEliminarBussEnabled(true);
	   			}
	   		}
	   		
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return estadoCueExeSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public EstadoCueExeAdapter getEstadoCueExeAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			EstadoCueExe estadoCueExe = EstadoCueExe.getById(commonKey.getId());

			EstadoCueExeAdapter estadoCueExeAdapter = new EstadoCueExeAdapter();
			estadoCueExeAdapter.setEstadoCueExe((EstadoCueExeVO) estadoCueExe.toVO(1));

			log.debug(funcName + ": exit");
			return estadoCueExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	} 

	public EstadoCueExeAdapter getEstadoCueExeAdapterForUpdate(UserContext userContext, CommonKey commonKey)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			EstadoCueExe estadoCueExe = EstadoCueExe.getById(commonKey.getId());
			
			;
			EstadoCueExeAdapter estadoCueExeAdapter = new EstadoCueExeAdapter();
			EstadoCueExeVO estadoCueExeVO = (EstadoCueExeVO) estadoCueExe.toVO(2);
						
			estadoCueExeVO.setTipoEstadoCueExe(TipoEstadoCueExe.getByCod(estadoCueExe.getTipo()));
			estadoCueExeAdapter.setEstadoCueExe(estadoCueExeVO);
			estadoCueExeAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			estadoCueExeAdapter.setListTipoEstadoCueExe(TipoEstadoCueExe.getList());
			
			log.debug(funcName + ": exit");
			return estadoCueExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EstadoCueExeAdapter getEstadoCueExeAdapterForCreate(UserContext userContext)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			EstadoCueExeAdapter estadoCueExeAdapter = new EstadoCueExeAdapter();
			
			estadoCueExeAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			estadoCueExeAdapter.setListTipoEstadoCueExe(TipoEstadoCueExe.getList());
			
			return estadoCueExeAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public EstadoCueExeVO createEstadoCueExe(UserContext userContext, EstadoCueExeVO estadoCueExeVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			estadoCueExeVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			EstadoCueExe estadoCueExe = new EstadoCueExe();
            
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (estadoCueExeVO.hasError()){
        		tx.rollback();
        		return estadoCueExeVO;
        	}
         
        	estadoCueExe.setDesEstadoCueExe(estadoCueExeVO.getDesEstadoCueExe());
            estadoCueExe.setEsResolucion(estadoCueExeVO.getEsResolucion().getBussId());
            estadoCueExe.setPermiteModificar(SiNo.NO.getBussId());
            estadoCueExe.setEsInicial(SiNo.NO.getBussId());
            estadoCueExe.setTipo(estadoCueExeVO.getTipoEstadoCueExe().getCod());
            estadoCueExe.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager
            estadoCueExe = ExeDefinicionManager.getInstance().createEstadoCueExe(estadoCueExe);

            // Si es un estado, seteamos estadoencueexe = id
            if (!StringUtil.isNullOrEmpty(estadoCueExeVO.getTipoEstadoCueExe().getCod()) && 
            		estadoCueExeVO.getTipoEstadoCueExe().getId().equals(TipoEstadoCueExe.TIPOESTADO.getCod())){
            
            	estadoCueExe.setIdEstadoEnCueExe(estadoCueExe.getId());

            	ExeDAOFactory.getEstadoCueExeDAO().update(estadoCueExe);
            }
            
            if (estadoCueExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				estadoCueExeVO =  (EstadoCueExeVO) estadoCueExe.toVO(3);
			}
            estadoCueExe.passErrorMessages(estadoCueExeVO);
            
            log.debug(funcName + ": exit");
            return estadoCueExeVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	public EstadoCueExeVO updateEstadoCueExe(UserContext userContext, EstadoCueExeVO estadoCueExeVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			estadoCueExeVO.clearErrorMessages();

			// Copiado de propiadades de VO al BO
			EstadoCueExe estadoCueExe = EstadoCueExe.getById(estadoCueExeVO.getId());

			if(!estadoCueExeVO.validateVersion(estadoCueExe.getFechaUltMdf())) return estadoCueExeVO;

     		// Si no pasa la validacion, vuelve a la vista. 
			if (estadoCueExeVO.hasError()){
				tx.rollback();
				return estadoCueExeVO;
			}

			estadoCueExe.setDesEstadoCueExe(estadoCueExeVO.getDesEstadoCueExe());
			estadoCueExe.setEsResolucion(estadoCueExeVO.getEsResolucion().getBussId());
			estadoCueExe.setTipo(estadoCueExeVO.getTipoEstadoCueExe().getCod());

			estadoCueExe.setEstado(Estado.ACTIVO.getId());
			
			// Si es un estado, seteamos estadoencueexe = id
            if (!StringUtil.isNullOrEmpty(estadoCueExeVO.getTipoEstadoCueExe().getCod()) && 
            		estadoCueExeVO.getTipoEstadoCueExe().getId().equals(TipoEstadoCueExe.TIPOESTADO.getCod())){
            
            	estadoCueExe.setIdEstadoEnCueExe(estadoCueExe.getId());
            }
			
			// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			estadoCueExe = ExeDefinicionManager.getInstance().updateEstadoCueExe(estadoCueExe);
			
			if (estadoCueExe.hasError()) {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				estadoCueExeVO =  (EstadoCueExeVO) estadoCueExe.toVO(3);
			}
			estadoCueExe.passErrorMessages(estadoCueExeVO);

			log.debug(funcName + ": exit");
			return estadoCueExeVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public EstadoCueExeVO deleteEstadoCueExe(UserContext userContext, EstadoCueExeVO estadoCueExeVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			estadoCueExeVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			EstadoCueExe estadoCueExe = EstadoCueExe.getById(estadoCueExeVO.getId());
			
			 // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
	        estadoCueExe = ExeDefinicionManager.getInstance().deleteEstadoCueExe(estadoCueExe);
	        
			if (estadoCueExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				estadoCueExeVO =  (EstadoCueExeVO) estadoCueExe.toVO(3);
			}
			estadoCueExe.passErrorMessages(estadoCueExeVO);
            
            log.debug(funcName + ": exit");
            return estadoCueExeVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EstadoCueExeVO activarEstadoCueExe(UserContext userContext, EstadoCueExeVO estadoCueExeVO)throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			EstadoCueExe estadoCueExe = EstadoCueExe.getById(estadoCueExeVO.getId());

			estadoCueExe.activar();

            if (estadoCueExe.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				estadoCueExeVO =  (EstadoCueExeVO) estadoCueExe.toVO();
			}
            estadoCueExe.passErrorMessages(estadoCueExeVO);
            
            log.debug(funcName + ": exit");
            return estadoCueExeVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	
	}
	public EstadoCueExeVO desactivarEstadoCueExe(UserContext userContext, EstadoCueExeVO estadoCueExeVO)throws DemodaServiceException {
	String funcName = DemodaUtil.currentMethodName();
	
	Session session = null;
	Transaction tx  = null; 

	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
	try {
		DemodaUtil.setCurrentUserContext(userContext);
		session = SiatHibernateUtil.currentSession();
		tx = session.beginTransaction();
		
		EstadoCueExe estadoCueExe = EstadoCueExe.getById(estadoCueExeVO.getId());

		estadoCueExe.desactivar();

        if (estadoCueExe.hasError()) {
        	tx.rollback();
        	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
		} else {
			tx.commit();
			if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			estadoCueExeVO =  (EstadoCueExeVO) estadoCueExe.toVO();
		}
        estadoCueExe.passErrorMessages(estadoCueExeVO);
        
        log.debug(funcName + ": exit");
        return estadoCueExeVO;
	} catch (Exception e) {
		log.error("Service Error: ",  e);
		if(tx != null) tx.rollback();
		throw new DemodaServiceException(e);
	} finally {
		SiatHibernateUtil.closeSession();
	}
}	

	// <--- ABM Estado Cuenta/Exencion
}
