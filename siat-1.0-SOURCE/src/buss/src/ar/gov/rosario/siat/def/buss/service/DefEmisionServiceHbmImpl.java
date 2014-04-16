//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.service;

/**
 * Implementacion de servicios del submodulo Emision del modulo 
 * Definicion
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.QryTableDataType;
import ar.gov.rosario.siat.def.buss.bean.CodEmi;
import ar.gov.rosario.siat.def.buss.bean.ColEmiMat;
import ar.gov.rosario.siat.def.buss.bean.DefEmisionManager;
import ar.gov.rosario.siat.def.buss.bean.EmiMat;
import ar.gov.rosario.siat.def.buss.bean.PeriodoDeuda;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipCodEmi;
import ar.gov.rosario.siat.def.buss.bean.TipoEmision;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.CodEmiAdapter;
import ar.gov.rosario.siat.def.iface.model.CodEmiSearchPage;
import ar.gov.rosario.siat.def.iface.model.CodEmiVO;
import ar.gov.rosario.siat.def.iface.model.ColEmiMatAdapter;
import ar.gov.rosario.siat.def.iface.model.ColEmiMatVO;
import ar.gov.rosario.siat.def.iface.model.EmiMatAdapter;
import ar.gov.rosario.siat.def.iface.model.EmiMatSearchPage;
import ar.gov.rosario.siat.def.iface.model.EmiMatVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.TipCodEmiVO;
import ar.gov.rosario.siat.def.iface.service.IDefEmisionService;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.emi.buss.bean.AuxDeuda;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

public class DefEmisionServiceHbmImpl implements IDefEmisionService {
	private Logger log = Logger.getLogger(DefEmisionServiceHbmImpl.class);
	
	// ---> ABM EmiMat 	
	public EmiMatSearchPage getEmiMatSearchPageInit(UserContext userContext) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			EmiMatSearchPage emiMatSearchPage = new EmiMatSearchPage();
			
			// Obtenemos los Recursos disponibles
			// y los cargamos en la lista de SearchPage
			List<Recurso> listRecurso = Recurso.getListEmitibles();
			
			// Seteo la lista de Recursos
			emiMatSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				emiMatSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}

			emiMatSearchPage.getEmiMat().getRecurso().setId(-1L);

			return emiMatSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmiMatSearchPage getEmiMatSearchPageResult(UserContext userContext, EmiMatSearchPage emiMatSearchPage) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			emiMatSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<EmiMat> listEmiMat = DefDAOFactory.getEmiMatDAO().getBySearchPage(emiMatSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		emiMatSearchPage.setListResult(ListUtilBean.toVO(listEmiMat,1, false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return emiMatSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmiMatAdapter getEmiMatAdapterForView(UserContext userContext, CommonKey commonKey) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			EmiMat emiMat = EmiMat.getById(commonKey.getId());

	        EmiMatAdapter emiMatAdapter = new EmiMatAdapter();
	        emiMatAdapter.setEmiMat((EmiMatVO) emiMat.toVO(1));
	        
	        log.debug(funcName + ": exit");
			return emiMatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EmiMatAdapter getEmiMatAdapterForCreate(UserContext userContext) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			EmiMatAdapter emiMatAdapter = new EmiMatAdapter();
			
			// Seteo la listas para combos, etc
			List<Recurso> listRecurso = Recurso.getListEmitibles();

			emiMatAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				emiMatAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			emiMatAdapter.getEmiMat().getRecurso().setId(-1L);

			log.debug(funcName + ": exit");
			return emiMatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public EmiMatAdapter getEmiMatAdapterForUpdate(UserContext userContext, CommonKey commonKeyEmiMat) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			EmiMat emiMat = EmiMat.getById(commonKeyEmiMat.getId());
			
	        EmiMatAdapter emiMatAdapter = new EmiMatAdapter();
	        emiMatAdapter.setEmiMat((EmiMatVO) emiMat.toVO(1));

			// Seteo la lista para combo, etc
			List<Recurso> listRecurso = Recurso.getListEmitibles();

			emiMatAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				emiMatAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}

			log.debug(funcName + ": exit");
			return emiMatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmiMatAdapter imprimirEmiMat(UserContext userContext, EmiMatAdapter emiMatAdapterVO ) 
			throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			EmiMat emiMat = EmiMat.getById(emiMatAdapterVO.getEmiMat().getId());

			DefDAOFactory.getEmiMatDAO().imprimirGenerico(emiMat, emiMatAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return emiMatAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}

	public EmiMatVO createEmiMat(UserContext userContext, EmiMatVO emiMatVO) 
			throws DemodaServiceException {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null; 
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			emiMatVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            EmiMat emiMat = new EmiMat();

            this.copyFromVO(emiMat, emiMatVO);
            
            emiMat.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            emiMat = DefEmisionManager.getInstance().createEmiMat(emiMat);
            
            if (emiMat.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				emiMatVO = (EmiMatVO) emiMat.toVO(0,false);
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			emiMat.passErrorMessages(emiMatVO);
            
            log.debug(funcName + ": exit");
            return emiMatVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EmiMatVO updateEmiMat(UserContext userContext, EmiMatVO emiMatVO) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			emiMatVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            EmiMat emiMat = EmiMat.getById(emiMatVO.getId());
			
			if(!emiMatVO.validateVersion(emiMat.getFechaUltMdf())) return emiMatVO;
			
            this.copyFromVO(emiMat, emiMatVO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            emiMat = DefEmisionManager.getInstance().updateEmiMat(emiMat);
            
            if (emiMat.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			emiMat.passErrorMessages(emiMatVO);
            
            log.debug(funcName + ": exit");
            return emiMatVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	private void copyFromVO(EmiMat emiMat, EmiMatVO emiMatVO) {
		// Seteamos el Codigo
		emiMat.setCodEmiMat(emiMatVO.getCodEmiMat());
		// Seteamos el Recurso
		Recurso recurso = Recurso
			.getByIdNull(emiMatVO.getRecurso().getId());
		emiMat.setRecurso(recurso);
	}

	public EmiMatVO deleteEmiMat(UserContext userContext, EmiMatVO emiMatVO) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null;
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			emiMatVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			EmiMat emiMat = EmiMat.getById(emiMatVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			emiMat = DefEmisionManager.getInstance().deleteEmiMat(emiMat);
			
			if (emiMat.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			emiMat.passErrorMessages(emiMatVO);
            
            log.debug(funcName + ": exit");
            return emiMatVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmiMatVO activarEmiMat(UserContext userContext, EmiMatVO emiMatVO ) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx  = null; 
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            EmiMat emiMat = EmiMat.getById(emiMatVO.getId());

            emiMat.activar();

            if (emiMat.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            emiMat.passErrorMessages(emiMatVO);
            
            log.debug(funcName + ": exit");
            return emiMatVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmiMatVO desactivarEmiMat(UserContext userContext, EmiMatVO emiMatVO ) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");      
		Session session = null;
		Transaction tx  = null; 
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            EmiMat emiMat = EmiMat.getById(emiMatVO.getId());
                           
            emiMat.desactivar();

            if (emiMat.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
            emiMat.passErrorMessages(emiMatVO);
            
            log.debug(funcName + ": exit");
            return emiMatVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	       
	// <--- ABM EmiMat
	
	// ---> ABM_ColEmiMat
	public ColEmiMatAdapter getColEmiMatAdapterForView(UserContext userContext, CommonKey commonKey) 
			throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ColEmiMat colEmiMat = ColEmiMat.getById(commonKey.getId());
		
		    ColEmiMatAdapter colEmiMatAdapter = new ColEmiMatAdapter();
		    colEmiMatAdapter.setColEmiMat((ColEmiMatVO) colEmiMat.toVO(2, false));
		    
		    log.debug(funcName + ": exit");
			return colEmiMatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ColEmiMatAdapter getColEmiMatAdapterForCreate(UserContext userContext, CommonKey commonKey) 
			throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			EmiMat emiMat = EmiMat.getById(commonKey.getId());
			
			ColEmiMatAdapter colEmiMatAdapter = new ColEmiMatAdapter();
			colEmiMatAdapter.getColEmiMat().setEmiMat((EmiMatVO) emiMat.toVO(1,false));
			
			colEmiMatAdapter.setListEmiMatTipoDato(QryTableDataType.getList(QryTableDataType.OpcionSelecionar));
			
			colEmiMatAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
		    
		    log.debug(funcName + ": exit");
			return colEmiMatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ColEmiMatAdapter getColEmiMatAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
			throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ColEmiMat colEmiMat = ColEmiMat.getById(commonKey.getId());
			
			ColEmiMatAdapter colEmiMatAdapter = new ColEmiMatAdapter();
			colEmiMatAdapter.setColEmiMat((ColEmiMatVO) colEmiMat.toVO(2,false));
			
			colEmiMatAdapter.setListEmiMatTipoDato(QryTableDataType.getList(QryTableDataType.OpcionSelecionar));
			
			colEmiMatAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
		    
		    log.debug(funcName + ": exit");
			return colEmiMatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ColEmiMatVO createColEmiMat(UserContext userContext, ColEmiMatVO colEmiMatVO) 
			throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null; 
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			colEmiMatVO.clearErrorMessages();
			
			// Obtenemos la matriz de Emision
			EmiMat emiMat = EmiMat.getById(colEmiMatVO.getEmiMat().getId());
			
			// Copiado de propiadades de VO al BO
		    ColEmiMat colEmiMat = new ColEmiMat();
		    colEmiMat.setEmiMat(emiMat);
		    colEmiMat.setCodColumna(colEmiMatVO.getCodColumna());
		    colEmiMat.setTipoDato(colEmiMatVO.getTipoDato().getBussId());
		    colEmiMat.setTipoColumna(colEmiMatVO.getTipoColumna().getBussId());
		    colEmiMat.setOrden(colEmiMatVO.getOrden());
		    colEmiMat.setEstado(Estado.ACTIVO.getId());
		    
		    // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
		    colEmiMat = emiMat.createColEmiMat(colEmiMat);
		    
		    if (colEmiMat.hasError()) {
		    	tx.rollback();
		    	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			colEmiMat.passErrorMessages(colEmiMatVO);
		    
		    log.debug(funcName + ": exit");
		    return colEmiMatVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ColEmiMatVO updateColEmiMat(UserContext userContext, ColEmiMatVO colEmiMatVO) 
			throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null; 
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			colEmiMatVO.clearErrorMessages();
			
			// Obtenemos la matriz de Emision
			ColEmiMat colEmiMat = ColEmiMat.getById(colEmiMatVO.getId());
			EmiMat emiMat = colEmiMat.getEmiMat(); 
			
			// Copiado de propiadades de VO al BO
		    colEmiMat.setCodColumna(colEmiMatVO.getCodColumna());
		    colEmiMat.setTipoDato(colEmiMatVO.getTipoDato().getBussId());
		    colEmiMat.setTipoColumna(colEmiMatVO.getTipoColumna().getBussId());
		    colEmiMat.setOrden(colEmiMatVO.getOrden());
		    
		    // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
		    colEmiMat = emiMat.updateColEmiMat(colEmiMat);
		    
		    if (colEmiMat.hasError()) {
		    	tx.rollback();
		    	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			colEmiMat.passErrorMessages(colEmiMatVO);
		    
		    log.debug(funcName + ": exit");
		    return colEmiMatVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ColEmiMatVO deleteColEmiMat(UserContext userContext, ColEmiMatVO colEmiMatVO) 
			throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = null;
		Transaction tx = null;
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			colEmiMatVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			ColEmiMat colEmiMat = ColEmiMat.getById(colEmiMatVO.getId());
			EmiMat emiMat = colEmiMat.getEmiMat();
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			colEmiMat = emiMat.deleteColEmiMat(colEmiMat);
			
			if (colEmiMat.hasError()) {
		    	tx.rollback();
		    	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			colEmiMat.passErrorMessages(colEmiMatVO);
		    
		    log.debug(funcName + ": exit");
		    return colEmiMatVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM_ColEmiMat

	// ---> ABM Codigo Emison
	@SuppressWarnings("unchecked")
	public CodEmiSearchPage getCodEmiSearchPageInit(UserContext userContext) throws DemodaServiceException {		

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			CodEmiSearchPage codEmiSearchPage = new CodEmiSearchPage();
			
			// Obtenemos los Recursos disponibles
			// y los cargamos en la lista de SearchPage
			List<Recurso> listRecurso = Recurso.getListEmitibles();
			
			// Seteo la lista de Recursos
			codEmiSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				codEmiSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			codEmiSearchPage.getCodEmi().getRecurso().setId(-1L);
			
			// Seteamos los Tipos de Codigos
			List<TipCodEmi> listTipCodEmi = TipCodEmi.getListActivos();
			codEmiSearchPage.setListTipCodEmi((ArrayList<TipCodEmiVO>) ListUtilBean.toVO(listTipCodEmi, 0 , 
					new TipCodEmiVO(-1, StringUtil.SELECT_OPCION_TODOS))); 
			
			return codEmiSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CodEmiSearchPage getCodEmiSearchPageResult(UserContext userContext, CodEmiSearchPage codEmiSearchPage) 
			throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			codEmiSearchPage.clearError();

			// Validamos que Fecha Desde no sea mayor a Fecha Hasta (si se ingresaron)
			Date fechaDesde = codEmiSearchPage.getCodEmi().getFechaDesde();
			Date fechaHasta = codEmiSearchPage.getCodEmi().getFechaHasta();
			
			if (fechaDesde != null && fechaHasta != null && DateUtil.isDateAfter(fechaDesde, fechaHasta)) {
				codEmiSearchPage.addRecoverableError(
						BaseError.MSG_VALORMAYORQUE, 
						DefError.CODEMI_FECHADESDE, 
						DefError.CODEMI_FECHAHASTA);
				return codEmiSearchPage;
			}

			// Aqui obtiene lista de BOs
	   		List<CodEmi> listCodEmi = DefDAOFactory.getCodEmiDAO().getBySearchPage(codEmiSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		codEmiSearchPage.setListResult(ListUtilBean.toVO(listCodEmi,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return codEmiSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CodEmiAdapter getCodEmiAdapterForView(UserContext userContext, CommonKey commonKey) 
			throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CodEmi codEmi = CodEmi.getById(commonKey.getId());

	        CodEmiAdapter codEmiAdapter = new CodEmiAdapter();
	        codEmiAdapter.setCodEmi((CodEmiVO) codEmi.toVO(1));
	        
			Recurso recurso = Recurso.getByIdNull(codEmi.getRecurso().getId());
			if (recurso != null) {
				codEmiAdapter.getCodEmi().setRecAtrCueEmiDefinition(recurso.getRecAtrCueEmiDefinition(new Date()));
			}

			System.out.println("TEST ..." + 
					!ListUtil.isNullOrEmpty(codEmiAdapter.getCodEmi().getRecAtrCueEmiDefinition().getListGenericAtrDefinition()))
					;
			
			log.debug(funcName + ": exit");
			return codEmiAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public CodEmiAdapter getCodEmiAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			CodEmiAdapter codEmiAdapter = new CodEmiAdapter();

			// Obtenemos los Recursos disponibles
			// y los cargamos en la lista de SearchPage
			List<Recurso> listRecurso = Recurso.getListEmitibles();
			
			// Seteo la lista de Recursos
			codEmiAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				codEmiAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			codEmiAdapter.getCodEmi().getRecurso().setId(-1L);
			
			// Seteamos los Tipos de Codigos
			List<TipCodEmi> listTipCodEmi = TipCodEmi.getListActivos();
			codEmiAdapter.setListTipCodEmi((ArrayList<TipCodEmiVO>) ListUtilBean.toVO(listTipCodEmi, 0 , 
					new TipCodEmiVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR))); 

			log.debug(funcName + ": exit");
			return codEmiAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	@SuppressWarnings("unchecked")
	public CodEmiAdapter getCodEmiAdapterForUpdate(UserContext userContext, 
			CommonKey commonKeyCodEmi) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CodEmi codEmi = CodEmi.getById(commonKeyCodEmi.getId());
			
	        CodEmiAdapter codEmiAdapter = new CodEmiAdapter();
	        codEmiAdapter.setCodEmi((CodEmiVO) codEmi.toVO(1));

			// Obtenemos los Recursos disponibles
			// y los cargamos en la lista de SearchPage
			List<Recurso> listRecurso = Recurso.getListEmitibles();
			
			// Seteo la lista de Recursos
			codEmiAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				codEmiAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			// Seteamos los Tipos de Codigos
			List<TipCodEmi> listTipCodEmi = TipCodEmi.getListActivos();
			codEmiAdapter.setListTipCodEmi((ArrayList<TipCodEmiVO>) ListUtilBean.toVO(listTipCodEmi, 0 , 
					new TipCodEmiVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR))); 
			
			if (codEmi.getRecurso() != null) {
				Recurso recurso = Recurso.getByIdNull(codEmi.getRecurso().getId());
				if (recurso != null) {
					codEmiAdapter.getCodEmi().setRecAtrCueEmiDefinition(recurso.getRecAtrCueEmiDefinition(new Date()));
				}
			}
			
			log.debug(funcName + ": exit");
			return codEmiAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CodEmiAdapter getCodEmiAdapterParamTipCodEmi(UserContext userContext, 
			CodEmiAdapter codEmiAdapter) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			codEmiAdapter.clearError();
			
			TipCodEmiVO tipCodEmiVO  = codEmiAdapter.getCodEmi().getTipCodEmi();
			TipCodEmi tipCodEmi = TipCodEmi.getByIdNull(tipCodEmiVO.getId());
			
			// Si se selecciono un recurso
			if (tipCodEmi != null) {
				if (tipCodEmi.getId().equals(TipCodEmi.ID_LIBRERIA)) {
					codEmiAdapter.setSeleccionarRecursoEnabled(false);
					return codEmiAdapter;
				}
			} 
				
			codEmiAdapter.setSeleccionarRecursoEnabled(true);
					
			log.debug(funcName + ": exit");
			return codEmiAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	
	public CodEmiVO createCodEmi(UserContext userContext, CodEmiVO codEmiVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			codEmiVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            CodEmi codEmi = new CodEmi();

            this.copyFromVO(codEmi, codEmiVO);
            
            codEmi.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            codEmi = DefEmisionManager.getInstance().createCodEmi(codEmi);
            
            if (codEmi.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				codEmiVO =  (CodEmiVO) codEmi.toVO(0,false);
			}
			codEmi.passErrorMessages(codEmiVO);
            
            log.debug(funcName + ": exit");
            return codEmiVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CodEmiVO updateCodEmi(UserContext userContext, CodEmiVO codEmiVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			codEmiVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            CodEmi codEmi = CodEmi.getById(codEmiVO.getId());
			
			if(!codEmiVO.validateVersion(codEmi.getFechaUltMdf())) return codEmiVO;
			
			this.copyFromVO(codEmi, codEmiVO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            codEmi = DefEmisionManager.getInstance().updateCodEmi(codEmi);
            
            if (codEmi.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				codEmiVO =  (CodEmiVO) codEmi.toVO(0,false);
			}
			codEmi.passErrorMessages(codEmiVO);
            
            log.debug(funcName + ": exit");
            return codEmiVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyFromVO(CodEmi codEmi, CodEmiVO codEmiVO) {
		Recurso recurso = Recurso.getByIdNull(codEmiVO.getRecurso().getId());
		codEmi.setRecurso(recurso);
		TipCodEmi tipCodEmi = TipCodEmi.getByIdNull(codEmiVO.getTipCodEmi().getId());
		codEmi.setTipCodEmi(tipCodEmi);
		codEmi.setNombre(codEmiVO.getNombre());
		codEmi.setDescripcion(codEmiVO.getDescripcion());
		codEmi.setCodigo(codEmiVO.getCodigo());
		codEmi.setFechaDesde(codEmiVO.getFechaDesde());
		codEmi.setFechaHasta(codEmiVO.getFechaHasta());
	}
	
	public CodEmiVO deleteCodEmi(UserContext userContext, CodEmiVO codEmiVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			codEmiVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			CodEmi codEmi = CodEmi.getById(codEmiVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			codEmi = DefEmisionManager.getInstance().deleteCodEmi(codEmi);
			
			if (codEmi.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				codEmiVO =  (CodEmiVO) codEmi.toVO(0,false);
			}
			codEmi.passErrorMessages(codEmiVO);
            
            log.debug(funcName + ": exit");
            return codEmiVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CodEmiVO activarCodEmi(UserContext userContext, CodEmiVO codEmiVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            CodEmi codEmi = CodEmi.getById(codEmiVO.getId());

            codEmi.activar();

            if (codEmi.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				codEmiVO =  (CodEmiVO) codEmi.toVO(0,false);
			}
            codEmi.passErrorMessages(codEmiVO);
            
            log.debug(funcName + ": exit");
            return codEmiVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CodEmiVO desactivarCodEmi(UserContext userContext, CodEmiVO codEmiVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		      
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            CodEmi codEmi = CodEmi.getById(codEmiVO.getId());
                           
            codEmi.desactivar();

            if (codEmi.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				codEmiVO =  (CodEmiVO) codEmi.toVO(0,false);
			}

            codEmi.passErrorMessages(codEmiVO);
            
            log.debug(funcName + ": exit");
            return codEmiVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	       

	public CodEmiAdapter testCodEmi(UserContext userContext, CodEmiAdapter codEmiAdapterVO) 
		throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			// Seteamos el tab actual en Probar
			codEmiAdapterVO.setCurrentTab("probar");
			
			CodEmiVO codEmiVO = codEmiAdapterVO.getCodEmi();

		    Long idRecurso = codEmiVO.getRecurso().getId();
		    Recurso recurso = Recurso.getById(idRecurso);

		    // Validamos que se Numero de Cuenta
		    String numCuenta = codEmiAdapterVO.getCuenta().getNumeroCuenta();
			if (StringUtil.isNullOrEmpty(numCuenta)) 
				codEmiAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.CODEMIADAPTER_CUENTA);
		    
		    if (codEmiAdapterVO.hasError()) {
		    	return codEmiAdapterVO;
		    }

		    // Obtenemos los parametros de entrada
		    Integer anio    = codEmiAdapterVO.getAnio();
		    Integer periodo = codEmiAdapterVO.getPeriodo();

		    Emision emision = new Emision();
			TipoEmision tipoEmision = TipoEmision.getById(TipoEmision.ID_INDIVIDUAL);
			emision.setTipoEmision(tipoEmision);
			emision.setRecurso(recurso);
		    // Seteamos la cuenta
		    Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(idRecurso,numCuenta);
		    emision.setCuenta(cuenta);

		    if (emision.getRecurso() != null && recurso.getPeriodoDeuda().getId().equals(PeriodoDeuda.ESPORADICO)) {
		    	anio = 0;
		    	periodo = 0;
		    }
		    
		    emision.setAnio(anio);
			emision.setFechaEmision(new Date());
			emision.setPeriodoDesde(periodo);
			emision.setPeriodoHasta(periodo);

			if (cuenta == null) {
				emision.addMessageValue("La cuenta no existe para el recurso");
				emision.passErrorMessages(codEmiAdapterVO);
				return codEmiAdapterVO;
			}
			
			if (!emision.validateCreateForTest()) {
				emision.passErrorMessages(codEmiAdapterVO);
				return codEmiAdapterVO;
			}
		
		    // Obtenemos el codigo
		    String codigo = codEmiVO.getCodigo();

		    // Obtenemos las exenciones
		    Date fechaAnalisis = DateUtil.getFirstDatOfMonth(periodo, anio);
		    if (emision.getRecurso() != null && recurso.getPeriodoDeuda().getId().equals(PeriodoDeuda.ESPORADICO)) {
		    	fechaAnalisis = emision.getFechaEmision();
		    }
		    List<CueExe> listCueExe = cuenta.getListCueExeVigente(fechaAnalisis);
		    
			// Salida del evaluador
			String evalOutput = "";
			try {
				emision.ininitializaEngine(codigo);
				
				AuxDeuda auxDeuda = emision.eval(cuenta,listCueExe,anio,periodo, 
						codEmiAdapterVO.getCodEmi().getRecAtrCueEmiDefinition().getListGenericAtrDefinition());

				if (auxDeuda != null) {
				
					evalOutput += "Log: \n";
					evalOutput += auxDeuda.getLogMessage() + "\n";
					
					evalOutput += "Deuda a Generar: \n";
					evalOutput += auxDeuda.getInfoMessage();
					evalOutput += "\n";
					evalOutput += "Valores de Atributos: \n";
					Pattern pattern = Pattern.compile("<(.+?)>(.*?)</(\\1)>");
					Matcher matcher = pattern.matcher(auxDeuda.getStrAtrVal());
					
					int pos = 0;
					while (matcher.find(pos)) {
						evalOutput += matcher.group(1) + ": ";
						evalOutput += matcher.group(2) + "\n";
						pos = matcher.end();
					}
					
					evalOutput += "Codigo de Exencion: " + auxDeuda.getStrExencion();
					
				} else {
					log.info("Emision de deuda cancelada.");
					evalOutput = "La emision de la deuda fue cancelada.\n";
				}

			} catch (Exception e) {
				log.error("Ocurrio una excepcion durante la evaluacion: ", e);
				evalOutput = "ERROR: " + e.getMessage();
			}
			
			codEmiAdapterVO.setEvalOutput(evalOutput);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return codEmiAdapterVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Codigo Emision

}
