//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.service;

/**
 * Implementacion de servicios del Submodulo Gravamen del modulo Definicion
 * @author tecso
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.ConAtr;
import ar.gov.rosario.siat.def.buss.bean.DefContribuyenteManager;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.ConAtrAdapter;
import ar.gov.rosario.siat.def.iface.model.ConAtrSearchPage;
import ar.gov.rosario.siat.def.iface.model.ConAtrVO;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.service.IDefContribuyenteService;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;


public class DefContribuyenteServiceHbmImpl implements IDefContribuyenteService {
	private Logger log = Logger.getLogger(DefContribuyenteServiceHbmImpl.class);

    // --> ABM ConAtr
	public ConAtrSearchPage getConAtrSearchPageInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			ConAtrSearchPage conAtrSearchPage = new ConAtrSearchPage();
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return conAtrSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	                        
	public ConAtrSearchPage getConAtrSearchPageResult(UserContext userContext, ConAtrSearchPage conAtrSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			//Aqui realizar validaciones, cargar errores y mensajes

			// Aqui obtiene lista de BOs
	   		List<ConAtr> listConAtr = DefDAOFactory.getConAtrDAO().getListBySearchPage(conAtrSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

			//Aqui pasamos BO a VO
	   		conAtrSearchPage.setListResult(ListUtilBean.toVO(listConAtr,2));

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return conAtrSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ConAtrAdapter getConAtrAdapterForView(UserContext userContext, CommonKey conAtrCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ConAtr conAtr = ConAtr.getById(conAtrCommonKey.getId());

			ConAtrAdapter conAtrAdapter = new ConAtrAdapter();
			conAtrAdapter.setConAtr((ConAtrVO) conAtr.toVO(2));
			
			//Recupero el Definition para este ConAtr
			GenericAtrDefinition genericAtrDefinition = conAtr.getDefinitionValue();
			conAtrAdapter.setGenericAtrDefinition(genericAtrDefinition);
			
			log.debug(funcName + ": exit");
			return conAtrAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ConAtrAdapter getConAtrAdapterForCreate(UserContext userContext, CommonKey atributoCommonKey) 
		throws DemodaServiceException{

		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

	        ConAtrAdapter conAtrAdapter = new ConAtrAdapter();
	        
	        Atributo atributo = Atributo.getById(atributoCommonKey.getId());
	        AtributoVO atributoVO = (AtributoVO) atributo.toVO(0);
	        
	        conAtrAdapter.getConAtr().setAtributo(atributoVO);
	        conAtrAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
	    	//Recupero el Definition para este Atributo
			GenericAtrDefinition genericAtrDefinition = atributo.getDefinition();
			genericAtrDefinition.setEsRequerido(true);
			conAtrAdapter.setGenericAtrDefinition(genericAtrDefinition);
			

			log.debug(funcName + ": exit");
			return conAtrAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ConAtrAdapter getConAtrAdapterForUpdate(UserContext userContext, CommonKey conAtrCommonKey) 
		throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ConAtr conAtr = ConAtr.getById(conAtrCommonKey.getId());

	        ConAtrAdapter conAtrAdapter = new ConAtrAdapter();
	        
	        conAtrAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));	        
	        conAtrAdapter.setConAtr((ConAtrVO) conAtr.toVO(2));
	    
	        //Recupero el Definition para este ConAtr
			GenericAtrDefinition genericAtrDefinition = conAtr.getDefinitionValue();
			conAtrAdapter.setGenericAtrDefinition(genericAtrDefinition);
	
			// Seteo de banderas ver
	    			
			log.debug(funcName + ": exit");
			return conAtrAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ConAtrVO createConAtr(UserContext userContext, ConAtrVO conAtrVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			conAtrVO.clearErrorMessages();

            ConAtr conAtr = new ConAtr();

            conAtr.setAtributo(Atributo.getById(conAtrVO.getAtributo().getId()));
            conAtr.setEsAtrSegmentacion(conAtrVO.getEsAtrSegmentacion().getBussId());
            conAtr.setValorDefecto(conAtrVO.getValorDefecto());
            conAtr.setEsVisConDeu(conAtrVO.getEsVisConDeu().getBussId());
            conAtr.setEsAtributoBus(conAtrVO.getEsAtributoBus().getBussId());
            conAtr.setAdmBusPorRan(conAtrVO.getAdmBusPorRan().getBussId());
            conAtr.setEstado(Estado.ACTIVO.getBussId());
            
            DefContribuyenteManager.getInstance().createConAtr(conAtr); 

            if (conAtr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				conAtrVO =  (ConAtrVO) conAtr.toVO(2);
			}
            conAtr.passErrorMessages(conAtrVO);
            
            log.debug(funcName + ": exit");
            return conAtrVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ConAtrVO updateConAtr(UserContext userContext, ConAtrVO conAtrVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			conAtrVO.clearErrorMessages();
			
            ConAtr conAtr = ConAtr.getById(conAtrVO.getId());

            if(!conAtrVO.validateVersion(conAtr.getFechaUltMdf())) return conAtrVO;
            
            conAtr.setEsAtrSegmentacion(conAtrVO.getEsAtrSegmentacion().getBussId());
            conAtr.setValorDefecto(conAtrVO.getValorDefecto());
            conAtr.setEsVisConDeu(conAtrVO.getEsVisConDeu().getBussId());
            conAtr.setEsAtributoBus(conAtrVO.getEsAtributoBus().getBussId());
            conAtr.setAdmBusPorRan(conAtrVO.getAdmBusPorRan().getBussId());

            DefContribuyenteManager.getInstance().updateConAtr(conAtr); 

            if (conAtr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				conAtrVO =  (ConAtrVO) conAtr.toVO(2);
			}
            conAtr.passErrorMessages(conAtrVO);
            
            log.debug(funcName + ": exit");
            return conAtrVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ConAtrVO deleteConAtr(UserContext userContext, ConAtrVO conAtrVO ) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			conAtrVO.clearErrorMessages();

            ConAtr conAtr = ConAtr.getById(conAtrVO.getId());

            DefContribuyenteManager.getInstance().deleteConAtr(conAtr); 

            if (conAtr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				conAtrVO =  (ConAtrVO) conAtr.toVO(2);
			}
            conAtr.passErrorMessages(conAtrVO);
            
            log.debug(funcName + ": exit");
            return conAtrVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ConAtrVO activarConAtr(UserContext userContext, ConAtrVO conAtrVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			ConAtr conAtr = ConAtr.getById(conAtrVO.getId());

			conAtr.activar();

            if (conAtr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				conAtrVO =  (ConAtrVO) conAtr.toVO();
			}
            conAtr.passErrorMessages(conAtrVO);
            
            log.debug(funcName + ": exit");
            return conAtrVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ConAtrVO desactivarConAtr(UserContext userContext, ConAtrVO conAtrVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			ConAtr conAtr = ConAtr.getById(conAtrVO.getId());

			conAtr.desactivar();

            if (conAtr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				conAtrVO =  (ConAtrVO) conAtr.toVO();
			}
            conAtr.passErrorMessages(conAtrVO);
            
            log.debug(funcName + ": exit");
            return conAtrVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public List<AtributoVO> getListAtributoConAtr(UserContext userContext)
		throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		List<AtributoVO> listAtributoVO = new ArrayList<AtributoVO>(); 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {

			List<ConAtr> listConAtr = ConAtr.getList();
			
			for(ConAtr conAtr: listConAtr) {
				AtributoVO atributoVO =(AtributoVO) conAtr.getAtributo().toVO(); 
				listAtributoVO.add(atributoVO);
			}
			
			log.debug(funcName + ": exit");
			return listAtributoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
		
	}

	public ConAtrAdapter paramAtributo(UserContext userContext,
		ConAtrAdapter conAtrAdapter) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			ConAtrVO conAtrVO = conAtrAdapter.getConAtr();

			Atributo atributo = Atributo.getById(conAtrVO.getAtributo().getId());
			conAtrVO.setAtributo((AtributoVO) atributo.toVO());
		
			//Recupero el Definition para este Atributo  TODO VER SI ES NECESARIO
			GenericAtrDefinition genericAtrDefinition = atributo.getDefinition();
			conAtrAdapter.setGenericAtrDefinition(genericAtrDefinition);
				
			log.debug(funcName + ": exit");
			return conAtrAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

    // <-- ABM ConAtr

}
 