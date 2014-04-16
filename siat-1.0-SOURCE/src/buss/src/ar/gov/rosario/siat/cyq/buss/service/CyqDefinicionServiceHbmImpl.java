//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.service;

/**
 * Implementacion de servicios del submodulo Definicion del modulo Cyq
 * @author tecso
 */

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cyq.buss.bean.Abogado;
import ar.gov.rosario.siat.cyq.buss.bean.CyqDefinicionManager;
import ar.gov.rosario.siat.cyq.buss.bean.Juzgado;
import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import ar.gov.rosario.siat.cyq.iface.model.AbogadoAdapter;
import ar.gov.rosario.siat.cyq.iface.model.AbogadoSearchPage;
import ar.gov.rosario.siat.cyq.iface.model.AbogadoVO;
import ar.gov.rosario.siat.cyq.iface.model.JuzgadoAdapter;
import ar.gov.rosario.siat.cyq.iface.model.JuzgadoSearchPage;
import ar.gov.rosario.siat.cyq.iface.model.JuzgadoVO;
import ar.gov.rosario.siat.cyq.iface.service.ICyqDefinicionService;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.UserContext;

public class CyqDefinicionServiceHbmImpl implements ICyqDefinicionService {
	private Logger log = Logger.getLogger(CyqDefinicionServiceHbmImpl.class);
	
	// ---> ABM Abogado 	
	public AbogadoSearchPage getAbogadoSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {

			AbogadoSearchPage abogadoSearchPage= new AbogadoSearchPage();
			//abogadoSearchPage.setListJuzgado((ArrayList<JuzgadoVO>) ListUtilBean.toVO(Juzgado.getList(),new JuzgadoVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			return abogadoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AbogadoSearchPage getAbogadoSearchPageResult(UserContext userContext, AbogadoSearchPage abogadoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			abogadoSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Abogado> listAbogado = CyqDAOFactory.getAbogadoDAO().getBySearchPage(abogadoSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		abogadoSearchPage.setListResult(ListUtilBean.toVO(listAbogado,1));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return abogadoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AbogadoAdapter getAbogadoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Abogado abogado = Abogado.getById(commonKey.getId());

	        AbogadoAdapter abogadoAdapter = new AbogadoAdapter();
	        
	        abogadoAdapter.setAbogado((AbogadoVO) abogado.toVO(1));
			
			log.debug(funcName + ": exit");
			return abogadoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AbogadoAdapter getAbogadoAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			AbogadoAdapter abogadoAdapter = new AbogadoAdapter();
			
			//abogadoAdapter.setListJuzgado((ArrayList<JuzgadoVO>) ListUtilBean.toVO(Juzgado.getList(),new JuzgadoVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return abogadoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}



	public AbogadoAdapter getAbogadoAdapterForUpdate(UserContext userContext, CommonKey commonKeyAbogado) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Abogado abogado = Abogado.getById(commonKeyAbogado.getId());
			
	        AbogadoAdapter abogadoAdapter = new AbogadoAdapter();
	        
	        //abogadoAdapter.setListJuzgado((ArrayList<JuzgadoVO>) ListUtilBean.toVO(Juzgado.getList(),new JuzgadoVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR)));
			
	        abogadoAdapter.setAbogado((AbogadoVO) abogado.toVO(1));

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return abogadoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AbogadoVO createAbogado(UserContext userContext, AbogadoVO abogadoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			abogadoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Abogado abogado = new Abogado();

            this.copyFromVO(abogado, abogadoVO);
            
            abogado.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            abogado = CyqDefinicionManager.getInstance().createAbogado(abogado);
            
            if (abogado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				abogadoVO =  (AbogadoVO) abogado.toVO(0,false);
			}
			abogado.passErrorMessages(abogadoVO);
            
            log.debug(funcName + ": exit");
            return abogadoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AbogadoVO updateAbogado(UserContext userContext, AbogadoVO abogadoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			abogadoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Abogado abogado = Abogado.getById(abogadoVO.getId());
			
			if(!abogadoVO.validateVersion(abogado.getFechaUltMdf())) return abogadoVO;
									
            this.copyFromVO(abogado, abogadoVO);
            
           /*/ Si se intenta realizar un cambio de abogado 
           if ( abogado.getJuzgado() != null && !ModelUtil.isNullOrEmpty(abogadoVO.getJuzgado())){
            
	            if (!(abogado.getJuzgado().getDesJuzgado().equals(abogadoVO.getJuzgado().getDesJuzgado()))){
	  			  
	    			if (GenericDAO.hasReference(abogado, Procedimiento.class, "abogado")) {
	    			 abogadoVO.addRecoverableError(BaseError.MSG_MODIFICAR_REGISTROS_ASOCIADOS,
	    						CyqError.ABOGADO_LABEL, CyqError.PROCEDIMIENTO_LABEL);
	    			 				 
	    			 return abogadoVO;
	    			}
	    		}
            }*/
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            abogado = CyqDefinicionManager.getInstance().updateAbogado(abogado);
            
            if (abogado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				abogadoVO =  (AbogadoVO) abogado.toVO(0,false);
			}
			abogado.passErrorMessages(abogadoVO);
            
            log.debug(funcName + ": exit");
            return abogadoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	

	public AbogadoVO deleteAbogado(UserContext userContext, AbogadoVO abogadoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			abogadoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Abogado abogado = Abogado.getById(abogadoVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			abogado = CyqDefinicionManager.getInstance().deleteAbogado(abogado);
			
			if (abogado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				abogadoVO =  (AbogadoVO) abogado.toVO(0,false);
			}
			abogado.passErrorMessages(abogadoVO);
            
            log.debug(funcName + ": exit");
            return abogadoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AbogadoVO activarAbogado(UserContext userContext, AbogadoVO abogadoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Abogado abogado = Abogado.getById(abogadoVO.getId());

            abogado.activar();

            if (abogado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				abogadoVO =  (AbogadoVO) abogado.toVO(0,false);
			}
            abogado.passErrorMessages(abogadoVO);
            
            log.debug(funcName + ": exit");
            return abogadoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AbogadoVO desactivarAbogado(UserContext userContext, AbogadoVO abogadoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Abogado abogado = Abogado.getById(abogadoVO.getId());

            abogado.desactivar();

            if (abogado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				abogadoVO =  (AbogadoVO) abogado.toVO(0,false);
			}
            abogado.passErrorMessages(abogadoVO);
            
            log.debug(funcName + ": exit");
            return abogadoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	
	public AbogadoAdapter imprimirAbogado(UserContext userContext, AbogadoAdapter abogadoAdapter ) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Abogado abogado = Abogado.getById(abogadoAdapter.getAbogado().getId());

	   		CyqDAOFactory.getAbogadoDAO().imprimirGenerico(abogado, abogadoAdapter.getReport());
	   		
			log.debug(funcName + ": exit");
			return abogadoAdapter;
		
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Abogado

	// ---> ABM Juzgado 	
	public JuzgadoSearchPage getJuzgadoSearchPageInit(UserContext userContext) throws DemodaServiceException {	
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			JuzgadoSearchPage juzgadoSearchPage = new JuzgadoSearchPage();
			

			return juzgadoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	public JuzgadoSearchPage getJuzgadoSearchPageResult(UserContext userContext, JuzgadoSearchPage juzgadoSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			juzgadoSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Juzgado> listJuzgado = CyqDAOFactory.getJuzgadoDAO().getBySearchPage(juzgadoSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		juzgadoSearchPage.setListResult(ListUtilBean.toVO(listJuzgado,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return juzgadoSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public JuzgadoAdapter getJuzgadoAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Juzgado juzgado = Juzgado.getById(commonKey.getId());

			JuzgadoAdapter juzgadoAdapter = new JuzgadoAdapter();
			juzgadoAdapter.setJuzgado((JuzgadoVO) juzgado.toVO(2,true));
			
			log.debug(funcName + ": exit");
			return juzgadoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public JuzgadoAdapter getJuzgadoAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			JuzgadoAdapter juzgadoAdapter = new JuzgadoAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return juzgadoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}


	public JuzgadoAdapter getJuzgadoAdapterForUpdate(UserContext userContext, CommonKey commonKeyJuzgado) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Juzgado juzgado = Juzgado.getById(commonKeyJuzgado.getId());
			
			JuzgadoAdapter juzgadoAdapter = new JuzgadoAdapter();
			juzgadoAdapter.setJuzgado((JuzgadoVO) juzgado.toVO(1));

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return juzgadoAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public JuzgadoVO createJuzgado(UserContext userContext, JuzgadoVO juzgadoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			juzgadoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Juzgado juzgado = new Juzgado();

            this.copyFromVO(juzgado, juzgadoVO);
            
            juzgado.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            juzgado = CyqDefinicionManager.getInstance().createJuzgado(juzgado);
            
            if (juzgado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				juzgadoVO =  (JuzgadoVO) juzgado.toVO(0,false);
			}
            juzgado.passErrorMessages(juzgadoVO);
            
            log.debug(funcName + ": exit");
            return juzgadoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public JuzgadoVO updateJuzgado(UserContext userContext, JuzgadoVO juzgadoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			juzgadoVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			Juzgado juzgado = Juzgado.getById(juzgadoVO.getId());
			
			if(!juzgadoVO.validateVersion(juzgado.getFechaUltMdf())) return juzgadoVO;
			
            this.copyFromVO(juzgado, juzgadoVO);
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            juzgado = CyqDefinicionManager.getInstance().updateJuzgado(juzgado);
            
            if (juzgado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				juzgadoVO =  (JuzgadoVO) juzgado.toVO(0,false);
			}
            juzgado.passErrorMessages(juzgadoVO);
            
            log.debug(funcName + ": exit");
            return juzgadoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public JuzgadoVO deleteJuzgado(UserContext userContext, JuzgadoVO jugadoVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			jugadoVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Juzgado jugado = Juzgado.getById(jugadoVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			jugado = CyqDefinicionManager.getInstance().deleteJuzgado(jugado);
			
			if (jugado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				jugadoVO =  (JuzgadoVO) jugado.toVO(0,false);
			}
			jugado.passErrorMessages(jugadoVO);
            
            log.debug(funcName + ": exit");
            return jugadoVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public JuzgadoVO activarJuzgado(UserContext userContext, JuzgadoVO juzgadoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Juzgado juzgado = Juzgado.getById(juzgadoVO.getId());

			juzgado.activar();

            if (juzgado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				juzgadoVO =  (JuzgadoVO) juzgado.toVO(0,false);
			}
            juzgado.passErrorMessages(juzgadoVO);
            
            log.debug(funcName + ": exit");
            return juzgadoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public JuzgadoVO desactivarJuzgado(UserContext userContext, JuzgadoVO juzgadoVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Juzgado juzgado = Juzgado.getById(juzgadoVO.getId());

			juzgado.desactivar();

            if (juzgado.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				juzgadoVO =  (JuzgadoVO) juzgado.toVO(0,false);
			}
            juzgado.passErrorMessages(juzgadoVO);
            
            log.debug(funcName + ": exit");
            return juzgadoVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	
	public JuzgadoAdapter imprimirJuzgado(UserContext userContext, JuzgadoAdapter juzgadoAdapter ) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Juzgado juzgado = Juzgado.getById(juzgadoAdapter.getJuzgado().getId());

	   		CyqDAOFactory.getJuzgadoDAO().imprimirGenerico(juzgado, juzgadoAdapter.getReport());
	   		
			log.debug(funcName + ": exit");
			return juzgadoAdapter;
		
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- ABM Juzgado
	
	private void copyFromVO(Juzgado juzgado, JuzgadoVO juzgadoVO) {
		try {
			
			juzgado.setDesJuzgado(juzgadoVO.getDesJuzgado());
				} catch (Exception e) {
			log.error("ERROR en copyFromVO():"+e);
		}	
	}
	
	private void copyFromVO(Abogado abogado, AbogadoVO abogadoVO) {
		try {
			abogado.setDescripcion(abogadoVO.getDescripcion());
			abogado.setTelefono(abogadoVO.getTelefono());
			abogado.setDomicilio(abogadoVO.getDomicilio());
			//abogado.setJuzgado(Juzgado.getByIdNull(abogadoVO.getJuzgado().getId()));
			
				} catch (Exception e) {
			log.error("ERROR en copyFromVO():"+e);
		}	
	}
}
