//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.service;

/**
 * Implementacion de servicios del submodulo Atributo del modulo Definicion
 * @author tecso
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.DefAtributoManager;
import ar.gov.rosario.siat.def.buss.bean.DomAtr;
import ar.gov.rosario.siat.def.buss.bean.DomAtrVal;
import ar.gov.rosario.siat.def.buss.bean.TipoAtributo;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.AtributoAdapter;
import ar.gov.rosario.siat.def.iface.model.AtributoSearchPage;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.DomAtrAdapter;
import ar.gov.rosario.siat.def.iface.model.DomAtrSearchPage;
import ar.gov.rosario.siat.def.iface.model.DomAtrVO;
import ar.gov.rosario.siat.def.iface.model.DomAtrValAdapter;
import ar.gov.rosario.siat.def.iface.model.DomAtrValVO;
import ar.gov.rosario.siat.def.iface.model.TipoAtributoVO;
import ar.gov.rosario.siat.def.iface.service.IDefAtributoService;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.UserContext;

public class DefAtributoServiceHbmImpl implements IDefAtributoService {
	private Logger log = Logger.getLogger(DefAtributoServiceHbmImpl.class);
	
	// ---> ABM Atributo 	
	public AtributoSearchPage getAtributoSearchPageInit(UserContext usercontext) throws DemodaServiceException {		
		return new AtributoSearchPage();
	}

	public AtributoSearchPage getAtributoSearchPageResult(UserContext userContext, AtributoSearchPage atributoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			atributoSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Atributo> listAtributo = DefDAOFactory.getAtributoDAO().getListBySearchPage(atributoSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		atributoSearchPage.setListResult(ListUtilBean.toVO(listAtributo,3));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return atributoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	

	public AtributoAdapter getAtributoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Atributo atributo = Atributo.getById(commonKey.getId());

	        AtributoAdapter atributoAdapter = new AtributoAdapter();
	        atributoAdapter.setAtributo((AtributoVO) atributo.toVO(1));
			
			log.debug(funcName + ": exit");
			return atributoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AtributoAdapter getAtributoAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			AtributoAdapter atributoAdapter = new AtributoAdapter();
			
			// Seteo de banderas
			
			// Seteo la lista de tipoAtributo
			List<TipoAtributo> listTipoAtributo = TipoAtributo.getListActivos();			
			atributoAdapter.setListTipoAtributo(
					(ArrayList<TipoAtributoVO>)ListUtilBean.toVO(listTipoAtributo, 
					new TipoAtributoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Seteo la lista vacia de domAtr
			atributoAdapter.getListDomAtr().add(0, new DomAtrVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			log.debug(funcName + ": exit");
			return atributoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public AtributoAdapter getAtributoAdapterParamTipoAtributo(UserContext userContext, AtributoAdapter atributoAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			atributoAdapter.clearError();
			
			if (!ModelUtil.isNullOrEmpty(atributoAdapter.getAtributo().getTipoAtributo())){
				
				List listDomAtr= DomAtr.getListActivosByIdTipoAtributo(
						atributoAdapter.getAtributo().getTipoAtributo().getId()	);
				atributoAdapter.setListDomAtr(
						(ArrayList<DomAtrVO>)ListUtilBean.toVO(listDomAtr, 
						new DomAtrVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			}else{
				// limpio la lista de domAtr
				atributoAdapter.setListDomAtr(new ArrayList<DomAtrVO>());
				atributoAdapter.getListDomAtr().add(0, new DomAtrVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			}
			
			log.debug(funcName + ": exit");
			return atributoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public AtributoAdapter getAtributoAdapterForUpdate(UserContext userContext, CommonKey commonKeyAtributo) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Atributo atributo = Atributo.getById(commonKeyAtributo.getId());

	        AtributoAdapter atributoAdapter = new AtributoAdapter();
	        atributoAdapter.setAtributo((AtributoVO) atributo.toVO(4));

			// Seteo la lista de tipoAtributo
			List<TipoAtributo> listTipoAtributo = TipoAtributo.getListActivos();			
			atributoAdapter.setListTipoAtributo(
					(ArrayList<TipoAtributoVO>)ListUtilBean.toVO(listTipoAtributo, 
					new TipoAtributoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Seteo la lista de domAtr
			List<DomAtr> listDomAtr = DomAtr.getListActivosByIdTipoAtributo(atributo.getTipoAtributo().getId());
			atributoAdapter.setListDomAtr(
					(ArrayList<DomAtrVO>)ListUtilBean.toVO(listDomAtr, 
					new DomAtrVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return atributoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AtributoVO createAtributo(UserContext userContext, AtributoVO atributoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			atributoVO.clearErrorMessages();
			
            Atributo atributo = new Atributo();
            atributo.setCodAtributo(atributoVO.getCodAtributo());
            atributo.setDesAtributo(atributoVO.getDesAtributo());
            atributo.setMascaraVisual(atributoVO.getMascaraVisual());
            
            TipoAtributo tipoAtributo = TipoAtributo.getByIdNull(atributoVO.getTipoAtributo().getId());
            atributo.setTipoAtributo(tipoAtributo);
                        
            DomAtr domAtr = DomAtr.getByIdNull(atributoVO.getDomAtr().getId());
            atributo.setDomAtr(domAtr);
            
            atributo.setEstado(Estado.ACTIVO.getId());
            
            atributo = DefAtributoManager.getInstance().createAtributo(atributo);
            
            if (atributo.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				atributoVO =  (AtributoVO) atributo.toVO(3);
			}
			atributo.passErrorMessages(atributoVO);
            
            log.debug(funcName + ": exit");
            return atributoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AtributoVO updateAtributo(UserContext userContext, AtributoVO atributoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			atributoVO.clearErrorMessages();
			
            Atributo atributo = Atributo.getById(atributoVO.getId());
            
            if(!atributoVO.validateVersion(atributo.getFechaUltMdf())) return atributoVO;
            
            atributo.setCodAtributo(atributoVO.getCodAtributo());
            atributo.setDesAtributo(atributoVO.getDesAtributo());
            atributo.setMascaraVisual(atributoVO.getMascaraVisual());
            
            TipoAtributo tipoAtributo = TipoAtributo.getByIdNull(atributoVO.getTipoAtributo().getId());
            atributo.setTipoAtributo(tipoAtributo);
                        
            DomAtr domAtr = DomAtr.getByIdNull(atributoVO.getDomAtr().getId());
            atributo.setDomAtr(domAtr);
            
            atributo.setEstado(Estado.ACTIVO.getId());
            
            atributo = DefAtributoManager.getInstance().updateAtributo(atributo);
            
            if (atributo.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				atributoVO =  (AtributoVO) atributo.toVO(3);
			}
			atributo.passErrorMessages(atributoVO);
            
            log.debug(funcName + ": exit");
            return atributoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AtributoVO deleteAtributo(UserContext userContext, AtributoVO atributoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			atributoVO.clearErrorMessages();
			
			Atributo atributo = Atributo.getById(atributoVO.getId());
			
			atributo = DefAtributoManager.getInstance().deleteAtributo(atributo);
			
			if (atributo.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				atributoVO =  (AtributoVO) atributo.toVO(3);
			}
			atributo.passErrorMessages(atributoVO);
            
            log.debug(funcName + ": exit");
            return atributoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AtributoVO activarAtributo(UserContext userContext, AtributoVO atributoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Atributo atributo = Atributo.getById(atributoVO.getId());

            atributo.activar();

            if (atributo.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				atributoVO =  (AtributoVO) atributo.toVO();
			}
            atributo.passErrorMessages(atributoVO);
            
            log.debug(funcName + ": exit");
            return atributoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AtributoVO desactivarAtributo(UserContext userContext, AtributoVO atributoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Atributo atributo = Atributo.getById(atributoVO.getId());

            atributo.desactivar();

            if (atributo.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				atributoVO =  (AtributoVO) atributo.toVO();
			}
            atributo.passErrorMessages(atributoVO);
            
            log.debug(funcName + ": exit");
            return atributoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	// <--- ABM Atributo
	
	
	// ---> ABM DomAtr
	public DomAtrSearchPage getDomAtrSearchPageInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			DomAtrSearchPage domAtrSearchPage = new DomAtrSearchPage();
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return domAtrSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	                        
	public DomAtrSearchPage getDomAtrSearchPageResult(UserContext userContext, DomAtrSearchPage domAtrSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			//Aqui realizar validaciones, cargar errores y mensajes

			// Aqui obtiene lista de BOs
	   		List<DomAtr> listDomAtr = DefDAOFactory.getDomAtrDAO().getListBySearchPage(domAtrSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

			//Aqui pasamos BO a VO
	   		domAtrSearchPage.setListResult(ListUtilBean.toVO(listDomAtr,0));

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return domAtrSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DomAtrAdapter getDomAtrAdapterForView(UserContext userContext, CommonKey domAtrCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DomAtr domAtr = DomAtr.getById(domAtrCommonKey.getId());

			DomAtrAdapter domAtrAdapter = new DomAtrAdapter();
			domAtrAdapter.setDomAtr((DomAtrVO) domAtr.toVO(2));
			
			log.debug(funcName + ": exit");
			return domAtrAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DomAtrAdapter getDomAtrAdapterForCreate(UserContext userContext, CommonKey atributoCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

	        DomAtrAdapter domAtrAdapter = new DomAtrAdapter();

	        domAtrAdapter.setListTipoAtributo( (ArrayList<TipoAtributoVO>)
				ListUtilBean.toVO(TipoAtributo.getListActivosByDomAtr(),
				new TipoAtributoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));	        

			log.debug(funcName + ": exit");
			return domAtrAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DomAtrAdapter getDomAtrAdapterForUpdate(UserContext userContext, CommonKey domAtrCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DomAtr domAtr = DomAtr.getById(domAtrCommonKey.getId());

	        DomAtrAdapter domAtrAdapter = new DomAtrAdapter();
	        domAtrAdapter.setDomAtr((DomAtrVO) domAtr.toVO(2));
	        
	        domAtrAdapter.setListTipoAtributo( (ArrayList<TipoAtributoVO>)
					ListUtilBean.toVO(TipoAtributo.getListActivosByDomAtr(),
					new TipoAtributoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));	        
	        
			// Seteo de banderas ver
			
			log.debug(funcName + ": exit");
			return domAtrAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DomAtrVO createDomAtr(UserContext userContext, DomAtrVO domAtrVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			domAtrVO.clearErrorMessages();

            DomAtr domAtr = new DomAtr();

            domAtr.setCodDomAtr(domAtrVO.getCodDomAtr());
            domAtr.setDesDomAtr(domAtrVO.getDesDomAtr());
            domAtr.setClassForName(domAtrVO.getClassForName());

            TipoAtributo tipoAtributo = TipoAtributo.getByIdNull(domAtrVO.getTipoAtributo().getId());
            domAtr.setTipoAtributo(tipoAtributo);

            // hasta que se cargue un valor del domAtr
            domAtr.setEstado(Estado.CREADO.getId());
            
            DefAtributoManager.getInstance().createDomAtr(domAtr); 

            if (domAtr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				domAtrVO =  (DomAtrVO) domAtr.toVO(2);
			}
            domAtr.passErrorMessages(domAtrVO);
            
            log.debug(funcName + ": exit");
            return domAtrVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DomAtrVO updateDomAtr(UserContext userContext, DomAtrVO domAtrVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			domAtrVO.clearErrorMessages();
			
            DomAtr domAtr = DomAtr.getById(domAtrVO.getId());

            if(!domAtrVO.validateVersion(domAtr.getFechaUltMdf())) return domAtrVO;
            
            domAtr.setCodDomAtr(domAtrVO.getCodDomAtr());
            domAtr.setDesDomAtr(domAtrVO.getDesDomAtr());
            domAtr.setClassForName(domAtrVO.getClassForName());
            TipoAtributo tipoAtributo = TipoAtributo.getByIdNull(domAtrVO.getTipoAtributo().getId());
            domAtr.setTipoAtributo(tipoAtributo);

            DefAtributoManager.getInstance().updateDomAtr(domAtr); 

            if (domAtr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				domAtrVO =  (DomAtrVO) domAtr.toVO(2);
			}
            domAtr.passErrorMessages(domAtrVO);
            
            log.debug(funcName + ": exit");
            return domAtrVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DomAtrVO deleteDomAtr(UserContext userContext, DomAtrVO domAtrVO ) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			domAtrVO.clearErrorMessages();

            DomAtr domAtr = DomAtr.getById(domAtrVO.getId());

            DefAtributoManager.getInstance().deleteDomAtr(domAtr); 

            if (domAtr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				domAtrVO =  (DomAtrVO) domAtr.toVO(2);
			}
            domAtr.passErrorMessages(domAtrVO);
            
            log.debug(funcName + ": exit");
            return domAtrVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DomAtrVO activarDomAtr(UserContext userContext, DomAtrVO domAtrVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			DomAtr domAtr = DomAtr.getById(domAtrVO.getId());

			domAtr.activar();

            if (domAtr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				domAtrVO =  (DomAtrVO) domAtr.toVO();
			}
            domAtr.passErrorMessages(domAtrVO);
            
            log.debug(funcName + ": exit");
            return domAtrVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DomAtrVO desactivarDomAtr(UserContext userContext, DomAtrVO domAtrVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			DomAtr domAtr = DomAtr.getById(domAtrVO.getId());

			domAtr.desactivar();

            if (domAtr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				domAtrVO =  (DomAtrVO) domAtr.toVO();
			}
            domAtr.passErrorMessages(domAtrVO);
            
            log.debug(funcName + ": exit");
            return domAtrVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM DomAtr
	
	// ---> ABM DomAtrVal
	public DomAtrValAdapter getDomAtrValAdapterForView(UserContext userContext, CommonKey domAtrValCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DomAtrVal domAtrVal = DomAtrVal.getById(domAtrValCommonKey.getId());

	        DomAtrValAdapter domAtrValAdapter = new DomAtrValAdapter();
	        domAtrValAdapter.setDomAtrVal((DomAtrValVO) domAtrVal.toVO(2));
			
			log.debug(funcName + ": exit");
			return domAtrValAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DomAtrValAdapter getDomAtrValAdapterForCreate(UserContext userContext, CommonKey domAtrCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			DomAtrValAdapter domAtrValAdapter = new DomAtrValAdapter();
			DomAtr domAtr = DomAtr.getById(domAtrCommonKey.getId());
			DomAtrValVO domAtrValVO = new DomAtrValVO();
			domAtrValVO.setDomAtr((DomAtrVO) domAtr.toVO(1));
			domAtrValAdapter.setDomAtrVal(domAtrValVO);

			log.debug(funcName + ": exit");
			return domAtrValAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public DomAtrValAdapter getDomAtrValAdapterForUpdate(UserContext userContext, CommonKey domAtrValCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			DomAtrVal domAtrVal = DomAtrVal.getById(domAtrValCommonKey.getId());

	        DomAtrValAdapter domAtrValAdapter = new DomAtrValAdapter();
	        domAtrValAdapter.setDomAtrVal((DomAtrValVO) domAtrVal.toVO(2));

			// Seteo de banderas ver
			
			log.debug(funcName + ": exit");
			return domAtrValAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DomAtrValVO createDomAtrVal(UserContext userContext, DomAtrValVO domAtrValVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			domAtrValVO.clearErrorMessages();
			
            DomAtrVal domAtrVal = new DomAtrVal();
            
            domAtrVal.setDesValor(domAtrValVO.getDesValor());
            domAtrVal.setValor(domAtrValVO.getValor());
            domAtrVal.setFechaDesde(domAtrValVO.getFechaDesde());
            domAtrVal.setFechaHasta(domAtrValVO.getFechaHasta());            
            domAtrVal.setEstado(Estado.ACTIVO.getId());
            
            // es requerido y no opcional
            DomAtr domAtr = DomAtr.getById(domAtrValVO.getDomAtr().getId());
            domAtrVal.setDomAtr(domAtr);

            domAtr.createDomAtrVal(domAtrVal);
            
            if (domAtrVal.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				domAtrValVO =  (DomAtrValVO) domAtrVal.toVO(2);
			}
            domAtrVal.passErrorMessages(domAtrValVO);
            
            log.debug(funcName + ": exit");
            return domAtrValVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DomAtrValVO updateDomAtrVal(UserContext userContext, DomAtrValVO domAtrValVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			domAtrValVO.clearErrorMessages();
			
            DomAtrVal domAtrVal = DomAtrVal.getById(domAtrValVO.getId());
            
            if(!domAtrValVO.validateVersion(domAtrVal.getFechaUltMdf())) return domAtrValVO;
            
            domAtrVal.setDesValor(domAtrValVO.getDesValor());
            domAtrVal.setValor(domAtrValVO.getValor());
            domAtrVal.setFechaDesde(domAtrValVO.getFechaDesde());
            domAtrVal.setFechaHasta(domAtrValVO.getFechaHasta());            
            domAtrVal.setEstado(domAtrValVO.getEstado().getId());
            
            domAtrVal.getDomAtr().updateDomAtrVal(domAtrVal);
            
            if (domAtrVal.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				domAtrValVO = (DomAtrValVO) domAtrVal.toVO(2);
			}
            domAtrVal.passErrorMessages(domAtrValVO);
            
            log.debug(funcName + ": exit");
            return domAtrValVO;
		}catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DomAtrValVO deleteDomAtrVal(UserContext userContext, DomAtrValVO domAtrValVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			domAtrValVO.clearErrorMessages();
			
            DomAtrVal domAtrVal = DomAtrVal.getById(domAtrValVO.getId());
            
            domAtrVal.getDomAtr().deleteDomAtrVal(domAtrVal);
            
            if (domAtrVal.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				domAtrValVO = (DomAtrValVO) domAtrVal.toVO();
			}
            domAtrVal.passErrorMessages(domAtrValVO);
            
            log.debug(funcName + ": exit");
            return domAtrValVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM DomAtrVal

}
