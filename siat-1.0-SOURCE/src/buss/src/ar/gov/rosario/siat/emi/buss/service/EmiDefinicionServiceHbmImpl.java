//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.service;

/**
 * Implementacion de servicios del submodulo Definicion 
 * del modulo Emision
 * @author tecso
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.EmiMat;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.EmiMatVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.buss.bean.EmiDefinicionManager;
import ar.gov.rosario.siat.emi.buss.bean.ValEmiMat;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.emi.iface.model.ValEmiMatAdapter;
import ar.gov.rosario.siat.emi.iface.model.ValEmiMatSearchPage;
import ar.gov.rosario.siat.emi.iface.model.ValEmiMatVO;
import ar.gov.rosario.siat.emi.iface.service.IEmiDefinicionService;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.UserContext;

public class EmiDefinicionServiceHbmImpl implements IEmiDefinicionService {
	private Logger log = Logger.getLogger(EmiDefinicionServiceHbmImpl.class);
	
	// ---> ABM ValEmiMat
	public ValEmiMatSearchPage getValEmiMatSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			ValEmiMatSearchPage valEmiMatSearchPage = new ValEmiMatSearchPage();
			
			// Obtenemos los Recursos disponibles
			// y los cargamos en la lista de SearchPage
			List<Recurso> listRecurso = Recurso.getListEmitibles();

			valEmiMatSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				valEmiMatSearchPage.getListRecurso().add(item.toVOWithCategoria());							
			}
			valEmiMatSearchPage.getValEmiMat().getEmiMat().getRecurso().setId(-1L);
			
			return valEmiMatSearchPage;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ValEmiMatSearchPage getValEmiMatSearchPageResult(UserContext userContext, ValEmiMatSearchPage valEmiMatSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			valEmiMatSearchPage.clearError();

			// Aqui obtiene lista de BOs
	   		List<ValEmiMat> listValEmiMat = EmiDAOFactory.getValEmiMatDAO().getBySearchPage(valEmiMatSearchPage);  

			//Aqui pasamos BO a VO
	   		valEmiMatSearchPage.setListResult(ListUtilBean.toVO(listValEmiMat,2, false));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return valEmiMatSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ValEmiMatAdapter getValEmiMatAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ValEmiMat valEmiMat = ValEmiMat.getById(commonKey.getId());

	        ValEmiMatAdapter valEmiMatAdapter = new ValEmiMatAdapter();
	        valEmiMatAdapter.setValEmiMat((ValEmiMatVO) valEmiMat.toVO(2, false));
			
			log.debug(funcName + ": exit");
			return valEmiMatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public ValEmiMatAdapter getValEmiMatAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ValEmiMatAdapter valEmiMatAdapter = new ValEmiMatAdapter();
			
			// Obtenemos los Recurso disponibles
			List<Recurso> listRecurso = Recurso.getListEmitibles();
			valEmiMatAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				valEmiMatAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}
			valEmiMatAdapter.getValEmiMat().getEmiMat().getRecurso().setId(-1L);

			// Seteamos la lista de matrices de Emision
			List<EmiMat> listEmiMat = new ArrayList<EmiMat>(); 
			valEmiMatAdapter.setListEmiMat((ArrayList<EmiMatVO>)
			ListUtilBean.toVO(listEmiMat, 0, new EmiMatVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR))); 
			
			log.debug(funcName + ": exit");
			return valEmiMatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ValEmiMatAdapter getValEmiMatAdapterForUpdate(UserContext userContext, CommonKey commonKeyValEmiMat) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ValEmiMat valEmiMat = ValEmiMat.getById(commonKeyValEmiMat.getId());
			
	        ValEmiMatAdapter valEmiMatAdapter = new ValEmiMatAdapter();
	        valEmiMatAdapter.setValEmiMat((ValEmiMatVO) valEmiMat.toVO(2, false));

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return valEmiMatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public ValEmiMatAdapter getValEmiMatAdapterParamRecurso(UserContext userContext, 
				ValEmiMatAdapter valEmiMatAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
	
			valEmiMatAdapter.clearError();
			
			EmiMatVO emiMatVO = valEmiMatAdapter.getValEmiMat().getEmiMat();
			
			List<EmiMat> listEmiMat = new ArrayList<EmiMat>();

			Recurso recurso =  Recurso.getByIdNull(emiMatVO.getRecurso().getId());
			
			if (recurso != null) {
				// Seteamos la lista de matrices de Emision
				listEmiMat = EmiMat.getListActivosBy(recurso); 
			}
			
			valEmiMatAdapter.setListEmiMat((ArrayList<EmiMatVO>)
			ListUtilBean.toVO(listEmiMat, 0, new EmiMatVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return valEmiMatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ValEmiMatVO createValEmiMat(UserContext userContext, ValEmiMatVO valEmiMatVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			valEmiMatVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ValEmiMat valEmiMat = new ValEmiMat();
            
            this.copyFromVO(valEmiMat, valEmiMatVO);
            
            valEmiMat.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            valEmiMat = EmiDefinicionManager.getInstance().createValEmiMat(valEmiMat);
            
            if (valEmiMat.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				valEmiMatVO = (ValEmiMatVO) valEmiMat.toVO(2, false);
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			valEmiMat.passErrorMessages(valEmiMatVO);
            
            log.debug(funcName + ": exit");
            return valEmiMatVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ValEmiMatVO updateValEmiMat(UserContext userContext, ValEmiMatVO valEmiMatVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			valEmiMatVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ValEmiMat valEmiMat = ValEmiMat.getById(valEmiMatVO.getId());
			
			if(!valEmiMatVO.validateVersion(valEmiMat.getFechaUltMdf())) return valEmiMatVO;
			
            this.copyFromVO(valEmiMat, valEmiMatVO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            valEmiMat = EmiDefinicionManager.getInstance().updateValEmiMat(valEmiMat);
            
            if (valEmiMat.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}

            valEmiMat.passErrorMessages(valEmiMatVO);
            
            log.debug(funcName + ": exit");
            return valEmiMatVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	private void copyFromVO(ValEmiMat valEmiMat, ValEmiMatVO valEmiMatVO) {
		EmiMat emiMat = EmiMat.getByIdNull(valEmiMatVO.getEmiMat().getId());
		valEmiMat.setEmiMat(emiMat);
		valEmiMat.setValores(valEmiMatVO.getValores());
	}
	
	public ValEmiMatVO deleteValEmiMat(UserContext userContext, ValEmiMatVO valEmiMatVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			valEmiMatVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			ValEmiMat valEmiMat = ValEmiMat.getById(valEmiMatVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			valEmiMat = EmiDefinicionManager.getInstance().deleteValEmiMat(valEmiMat);
			
			if (valEmiMat.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			}
			valEmiMat.passErrorMessages(valEmiMatVO);
            
            log.debug(funcName + ": exit");
            return valEmiMatVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM ValEmiMat

}
