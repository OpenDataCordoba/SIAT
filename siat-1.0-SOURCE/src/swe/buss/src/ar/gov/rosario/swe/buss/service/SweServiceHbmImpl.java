//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.service;

/**
 * Implementacion de servicios de seguridad extendido
 * Base para servicios
 * @author tecso
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.exception.ConstraintViolationException;

import ar.gov.rosario.swe.SweCommonError;
import ar.gov.rosario.swe.SweDAOFactory;
import ar.gov.rosario.swe.buss.AuditLog;
import ar.gov.rosario.swe.buss.bean.SweAdmManager;
import ar.gov.rosario.swe.buss.bean.SweManager;
import ar.gov.rosario.swe.buss.bean.UsrAplAdmSwe;
import ar.gov.rosario.swe.buss.dao.SweHibernateUtil;
import ar.gov.rosario.swe.iface.model.AccModAplAdapter;
import ar.gov.rosario.swe.iface.model.AccModAplSearchPage;
import ar.gov.rosario.swe.iface.model.AccModAplVO;
import ar.gov.rosario.swe.iface.model.AplicacionAdapter;
import ar.gov.rosario.swe.iface.model.AplicacionSearchPage;
import ar.gov.rosario.swe.iface.model.AplicacionVO;
import ar.gov.rosario.swe.iface.model.CloneUsrAplAdapter;
import ar.gov.rosario.swe.iface.model.ItemMenuAdapter;
import ar.gov.rosario.swe.iface.model.ItemMenuSearchPage;
import ar.gov.rosario.swe.iface.model.ItemMenuVO;
import ar.gov.rosario.swe.iface.model.ModAplAdapter;
import ar.gov.rosario.swe.iface.model.ModAplSearchPage;
import ar.gov.rosario.swe.iface.model.ModAplVO;
import ar.gov.rosario.swe.iface.model.RolAccModAplAdapter;
import ar.gov.rosario.swe.iface.model.RolAccModAplSearchPage;
import ar.gov.rosario.swe.iface.model.RolAccModAplVO;
import ar.gov.rosario.swe.iface.model.RolAplAdapter;
import ar.gov.rosario.swe.iface.model.RolAplSearchPage;
import ar.gov.rosario.swe.iface.model.RolAplVO;
import ar.gov.rosario.swe.iface.model.SweContext;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.model.UsrAplAdapter;
import ar.gov.rosario.swe.iface.model.UsrAplSearchPage;
import ar.gov.rosario.swe.iface.model.UsrAplVO;
import ar.gov.rosario.swe.iface.model.UsrRolAplAdapter;
import ar.gov.rosario.swe.iface.model.UsrRolAplSearchPage;
import ar.gov.rosario.swe.iface.model.UsrRolAplVO;
import ar.gov.rosario.swe.iface.model.UsuarioVO;
import ar.gov.rosario.swe.iface.service.ISweService;
import ar.gov.rosario.swe.iface.util.SegBussConstants;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.UserContext;

public class SweServiceHbmImpl implements ISweService { 
	private Logger log = Logger.getLogger(SweServiceHbmImpl.class); 

	/**
	 * Implementacion de login
	 */
	public SweUserSession login(String codigoAplicacion, UsuarioVO usuarioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		try {
			SweHibernateUtil.currentSession();
			SweUserSession sweUserSession = SweManager.getInstance().login(codigoAplicacion, usuarioVO, false);
			return sweUserSession;
		} catch (Exception e) {
			log.error(funcName + "Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	
	public SweUserSession login(String codigoAplicacion, UsuarioVO usuarioVO, boolean anonimo) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		try {
			SweHibernateUtil.currentSession();
			SweUserSession sweUserSession = SweManager.getInstance().login(codigoAplicacion, usuarioVO, anonimo);
			return sweUserSession;
		} catch (Exception e) {
			log.error(funcName + "Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	
	public SweContext getSweContext(String codApp) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		try {
			SweHibernateUtil.currentSession();
			
			SweContext sweContext = SweManager.getInstance().getSweContext(codApp);

			return sweContext;
		} catch (Exception e) {
			log.error(funcName + "Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	
    // ABM Aplicacion
	/*-----------------------------------------------------*/
	/* servicios de Aplicacion                             */
	/*-----------------------------------------------------*/
	public AplicacionAdapter getAplicacionAdapterForView(UserContext userContext, CommonKey aplicacionKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession(); 
			
			AplicacionAdapter aplicacionAdapter = SweAdmManager.getInstance().getAplicacionAdapterForView(aplicacionKey);

			log.debug(funcName + ": exit");
			return aplicacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
    }

	public AplicacionAdapter getAplicacionAdapterForCreate(UserContext userContext) throws DemodaServiceException {	
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			AplicacionAdapter aplicacionAdapter = SweAdmManager.getInstance().getAplicacionAdapterForCreate();

			log.debug(funcName + ": exit");
			return aplicacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	public AplicacionAdapter getAplicacionAdapterForUpdate(UserContext userContext, CommonKey aplicacionKey) throws DemodaServiceException {	
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();

			log.debug("---------PASAMOS SweServiceHbmImpl---------");
			
			AplicacionAdapter aplicacionAdapter = SweAdmManager.getInstance().getAplicacionAdapterForUpdate(aplicacionKey);

			log.debug(funcName + ": exit");
			return aplicacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
    }

	public AplicacionAdapter getAplicacionAdapterForDelete(UserContext userContext, CommonKey aplicacionKey) throws DemodaServiceException {	
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();

			AplicacionAdapter aplicacionAdapter = SweAdmManager.getInstance().getAplicacionAdapterForDelete(aplicacionKey);

			log.debug(funcName + ": exit");
			return aplicacionAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
    }

    public AplicacionVO createAplicacion(UserContext userContext, AplicacionVO aplicacionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			aplicacionVO = SweAdmManager.getInstance().createAplicacion(aplicacionVO);

			if (!aplicacionVO.hasError()) {
				tx.commit();
				new AuditLog("Alta de Aplicacion", aplicacionVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return aplicacionVO;
		} catch (ConstraintViolationException cve) {
			aplicacionVO.addRecoverableError(SweCommonError.APLICACION_UNIQUE);
			if(tx != null) tx.rollback();
			return aplicacionVO;  
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	public AplicacionVO updateAplicacion(UserContext userContext, AplicacionVO aplicacionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			log.debug("---------PASAMOS SweServiceHbmImpl---------");
			aplicacionVO = SweAdmManager.getInstance().updateAplicacion(aplicacionVO);

			if (!aplicacionVO.hasError()) {
				tx.commit();
				new AuditLog("Update de Aplicacion", aplicacionVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return aplicacionVO;
		} catch (ConstraintViolationException cve) {
			aplicacionVO.addRecoverableError(SweCommonError.APLICACION_UNIQUE);
			if(tx != null) tx.rollback();
			return aplicacionVO;  
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
   }

	public AplicacionVO deleteAplicacion(UserContext userContext, AplicacionVO aplicacionVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			aplicacionVO = SweAdmManager.getInstance().deleteAplicacion(aplicacionVO);

			if (!aplicacionVO.hasError()) {
				tx.commit();
				new AuditLog("Borrado de Aplicacion", aplicacionVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return aplicacionVO;
		} catch (ConstraintViolationException cve) {
			aplicacionVO.addRecoverableError(SweCommonError.APLICACION_HASREF);
			if(tx != null) tx.rollback();
			return aplicacionVO;  
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}


	/* Busqueda */
	public AplicacionSearchPage getAplicacionSearchPageInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			AplicacionSearchPage aplicacionSearchPage = SweAdmManager.getInstance().getAplicacionSearchPageInit();

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return aplicacionSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e); //CAMBIO: ,e para que imprima el stack trace
			throw new DemodaServiceException(e); //CAMBIO metemos la EXECEPCION original dentro de DemodaExce...
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	public AplicacionSearchPage getAplicacionSearchPageParam(UserContext userContext, AplicacionSearchPage aplicacionSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return SweAdmManager.getInstance().getAplicacionSearchPageParam(aplicacionSearchPage);
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	public AplicacionSearchPage getAplicacionSearchPageResult(UserContext userContext, AplicacionSearchPage aplicacionSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();

			aplicacionSearchPage = SweAdmManager.getInstance().getAplicacionSearchPageResult(aplicacionSearchPage);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return aplicacionSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	// ABM de Modulos Aplicacion
	/*-----------------------------------------------------*/
	/* servicios de Modulos Aplicacion                     */
	/*-----------------------------------------------------*/
	// searchPage
	public ModAplSearchPage getModAplSearchPageInit(UserContext userContext, CommonKey aplicacionKey) 
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			ModAplSearchPage modAplSearchPage = 
				SweAdmManager.getInstance().getModAplSearchPageInit(aplicacionKey);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return modAplSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e); //CAMBIO: ,e para que imprima el stack trace
			throw new DemodaServiceException(e); //CAMBIO metemos la EXECEPCION original dentro de DemodaExce...
		} finally {
			SweHibernateUtil.closeSession();
		}

	}

	public ModAplSearchPage getModAplSearchPageResult(UserContext userContext, 
		ModAplSearchPage aplicacionSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();

			aplicacionSearchPage = SweAdmManager.getInstance().getModAplSearchPageResult(aplicacionSearchPage);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return aplicacionSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	// Adapter
	public ModAplAdapter getModAplAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			ModAplAdapter modAplAdapter = SweAdmManager.getInstance().getModAplAdapter(commonKey);

			log.debug(funcName + ": exit");
			return modAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	public ModAplAdapter getModAplAdapterForCreate(UserContext userContext, CommonKey aplicacionKey) 
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			ModAplAdapter modAplAdapter = SweAdmManager.getInstance().getModAplAdapterForCreate(aplicacionKey);

			log.debug(funcName + ": exit");
			return modAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public ModAplAdapter getModAplAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			ModAplAdapter modAplAdapter = SweAdmManager.getInstance().getModAplAdapter(commonKey);

			log.debug(funcName + ": exit");
			return modAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	public ModAplAdapter getModAplAdapterForDelete(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			ModAplAdapter modAplAdapter = SweAdmManager.getInstance().getModAplAdapter(commonKey);

			log.debug(funcName + ": exit");
			return modAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	// crud
	public ModAplVO createModApl(UserContext userContext, ModAplVO modAplVO ) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			modAplVO = SweAdmManager.getInstance().createModApl(modAplVO);

			if (!modAplVO.hasError()) {
				tx.commit();
				new AuditLog("Alta de M�dulo de Aplicaci�n", modAplVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return modAplVO;
		} catch (ConstraintViolationException cve) {
			log.error("Service Error: ",  cve);
			modAplVO.addRecoverableError(SweCommonError.MODAPL_UNIQUE);
			if(tx != null) tx.rollback();
			return modAplVO;  
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	public ModAplVO updateModApl(UserContext userContext, ModAplVO modAplVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			modAplVO = SweAdmManager.getInstance().updateModApl(modAplVO);

			if (!modAplVO.hasError()) {
				tx.commit();
				new AuditLog("Update de M�dulo de Aplicaci�n", modAplVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return modAplVO;
		} catch (ConstraintViolationException cve) {
			modAplVO.addRecoverableError(SweCommonError.MODAPL_UNIQUE);
			if(tx != null) tx.rollback();
			return modAplVO;  
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	public ModAplVO deleteModApl(UserContext userContext, ModAplVO modAplVO ) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			modAplVO = SweAdmManager.getInstance().deleteModApl(modAplVO);

			if (!modAplVO.hasError()) {
				tx.commit();
				new AuditLog("Borrado de M�dulo de Aplicaci�n", modAplVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return modAplVO;
		} catch (ConstraintViolationException cve) {
			modAplVO.addRecoverableError(SweCommonError.MODAPL_HASREF);
			if(tx != null) tx.rollback();
			return modAplVO;  
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	// Fin ABM de Modulos Aplicacion
	
	/*-----------------------------------------------------*/
	/* servicios de Acciones Modulos Aplicacion            */
	/*-----------------------------------------------------*/
	// searchPage
	public AccModAplSearchPage getAccModAplSearchPageInit(UserContext userContext, AccModAplSearchPage accModAplSearchPage) 
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			accModAplSearchPage = SweAdmManager.getInstance().getAccModAplSearchPageInit(accModAplSearchPage);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return accModAplSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e); //CAMBIO: ,e para que imprima el stack trace
			throw new DemodaServiceException(e); //CAMBIO metemos la EXECEPCION original dentro de DemodaExce...
		} finally {
			SweHibernateUtil.closeSession();
		}

	}

	public AccModAplSearchPage getAccModAplSearchPageParamMod(UserContext userContext, AccModAplSearchPage accModAplSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
	
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			accModAplSearchPage = SweAdmManager.getInstance().getAccModAplSearchPageInit(accModAplSearchPage);
	
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return accModAplSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e); //CAMBIO: ,e para que imprima el stack trace
			throw new DemodaServiceException(e); //CAMBIO metemos la EXECEPCION original dentro de DemodaExce...
		} finally {
			SweHibernateUtil.closeSession();
		}

	}
	public AccModAplSearchPage getAccModAplSearchPageResult(UserContext userContext, 
		AccModAplSearchPage aplicacionSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();

			aplicacionSearchPage = SweAdmManager.getInstance().getAccModAplSearchPageResult(aplicacionSearchPage);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return aplicacionSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	// Adapter
	public AccModAplAdapter getAccModAplAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			AccModAplAdapter accModAplAdapter = SweAdmManager.getInstance().getAccModAplAdapter(commonKey);

			log.debug(funcName + ": exit");
			return accModAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public AccModAplAdapter getAccModAplAdapterForCreate(UserContext userContext, CommonKey modAplKey) 
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			AccModAplAdapter accModAplAdapter = 
				SweAdmManager.getInstance().getAccModAplAdapterForCreate(modAplKey);

			log.debug(funcName + ": exit");
			return accModAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public AccModAplAdapter getAccModAplAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			AccModAplAdapter accModAplAdapter = SweAdmManager.getInstance().getAccModAplAdapter(commonKey);

			log.debug(funcName + ": exit");
			return accModAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	public AccModAplAdapter getAccModAplAdapterForDelete(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			AccModAplAdapter accModAplAdapter = SweAdmManager.getInstance().getAccModAplAdapter(commonKey);

			log.debug(funcName + ": exit");
			return accModAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	// crud
	public AccModAplVO createAccModApl(UserContext userContext, AccModAplVO accModAplVO ) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			accModAplVO = SweAdmManager.getInstance().createAccModApl(accModAplVO);

			if (!accModAplVO.hasError()) {
				tx.commit();
				new AuditLog("Alta de Acci�n de M�dulo de Aplicaci�n", accModAplVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return accModAplVO;
		} catch (ConstraintViolationException cve) {
			accModAplVO.addRecoverableError(SweCommonError.ACCMODAPL_UNIQUE);
			if(tx != null) tx.rollback();
			return accModAplVO;  
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	public AccModAplVO updateAccModApl(UserContext userContext, AccModAplVO accModAplVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			accModAplVO = SweAdmManager.getInstance().updateAccModApl(accModAplVO);

			if (!accModAplVO.hasError()) {
				tx.commit();
				new AuditLog("Update de Acci�n de M�dulo de Aplicaci�n", accModAplVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return accModAplVO;
		} catch (ConstraintViolationException cve) {
			accModAplVO.addRecoverableError(SweCommonError.ACCMODAPL_UNIQUE);
			if(tx != null) tx.rollback();
			return accModAplVO;  
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	public AccModAplVO deleteAccModApl(UserContext userContext, AccModAplVO accModAplVO ) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			accModAplVO = SweAdmManager.getInstance().deleteAccModApl(accModAplVO);

			if (!accModAplVO.hasError()) {
				tx.commit();
				new AuditLog("Borrado de Acci�n de M�dulo de Aplicaci�n", accModAplVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return accModAplVO;
		} catch (ConstraintViolationException cve) {
			accModAplVO.addRecoverableError(SweCommonError.ACCMODAPL_HASREF);
			if(tx != null) tx.rollback();
			return accModAplVO;  
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	/*-----------------------------------------------------*/
	/* servicios de Acciones de Modulos de Aplicacion      */
	/*-----------------------------------------------------*/
	
	/*-----------------------------------------------------*/
	/* servicios de Usuarios de Aplicacion                 */
	/*-----------------------------------------------------*/

	public UsrAplSearchPage getUsrAplSearchPageInit(UserContext userContext, CommonKey aplicacionKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			UsrAplSearchPage usrAplSearchPage = SweAdmManager.getInstance().getUsrAplSearchPageInit(aplicacionKey);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return usrAplSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e); //CAMBIO: ,e para que imprima el stack trace
			throw new DemodaServiceException(e); //CAMBIO metemos la EXECEPCION original dentro de DemodaExce...
		} finally {
			SweHibernateUtil.closeSession();
		}

	}

	public UsrAplSearchPage getUsrAplSearchPageResult(UserContext userContext, UsrAplSearchPage usrAplSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();

			usrAplSearchPage = SweAdmManager.getInstance().getUsrAplSearchPageResult(usrAplSearchPage);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return usrAplSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public UsrAplAdapter getUsrAplAdapterForView(UserContext userContext, CommonKey usrAplCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession(); 
			
			UsrAplAdapter usrAplAdapter = SweAdmManager.getInstance().getUsrAplAdapter(usrAplCommonKey);
			if (usrAplAdapter.getUsrApl().getAplicacion().getCodigo().equals(SegBussConstants.CODIGO_SWE)){
				if( SegBussConstants.USUARIO_ADMIN.equals(usrAplAdapter.getUsrApl().getUsername().trim())){
					usrAplAdapter.setListAplicaciones(SweDAOFactory.getAplicacionDAO().getList());
				}else{
					usrAplAdapter.setListAplicaciones(new ArrayList());
					List<UsrAplAdmSwe> listUsrAplAdmSwe = SweDAOFactory.getUsrAplAdmiSweDAO().getByIdUsr(usrAplCommonKey.getId());
					for(UsrAplAdmSwe usrAplAdmSwe: listUsrAplAdmSwe){
						usrAplAdapter.getListAplicaciones().add(usrAplAdmSwe.getAplicacion().toVO());
					}
				}
			}
			
			log.debug(funcName + ": exit");
			return usrAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public UsrAplAdapter getUsrAplAdapterForCreate(UserContext userContext, CommonKey commonKeyAplicacion ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			UsrAplAdapter usrAplAdapter = SweAdmManager.getInstance().getUsrAplAdapterForCreate(commonKeyAplicacion);

			log.debug(funcName + ": exit");
			return usrAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}

	}
	
	public CloneUsrAplAdapter getCloneUsrAplAdapterForCreate(UserContext userContext, CommonKey commonKeyAplicacion ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			CloneUsrAplAdapter cloneUsrAplAdapter = SweAdmManager.getInstance().getCloneUsrAplAdapter(commonKeyAplicacion);

			log.debug(funcName + ": exit");
			return cloneUsrAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}

	}
	
	public UsrAplAdapter getUsrAplAdapterForUpdate(UserContext userContext, CommonKey usrAplCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();

			UsrAplAdapter usrAplAdapter =  SweAdmManager.getInstance().getUsrAplAdapter(usrAplCommonKey);

			log.debug(funcName + ": exit");
			return usrAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}

	}
	public UsrAplAdapter getUsrAplAdapterForDelete(UserContext userContext, CommonKey usrAplCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();

			UsrAplAdapter usrAplAdapter = SweAdmManager.getInstance().getUsrAplAdapter(usrAplCommonKey);
			
			//Si es usuario SWE, llena la lista de app permitidas
			if (usrAplAdapter.getUsrApl().getAplicacion().getCodigo().equals(SegBussConstants.CODIGO_SWE)){
				usrAplAdapter.setListAplicaciones(new ArrayList());
				List<UsrAplAdmSwe> listUsrAplAdmSwe = SweDAOFactory.getUsrAplAdmiSweDAO().getByIdUsr(usrAplCommonKey.getId());
				for(UsrAplAdmSwe usrAplAdmSwe: listUsrAplAdmSwe){
					usrAplAdapter.getListAplicaciones().add(usrAplAdmSwe.getAplicacion().toVO());
				}
			}
			
			log.debug(funcName + ": exit");
			return usrAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public UsrAplVO createUsrApl(UserContext userContext, UsrAplVO usrAplVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			usrAplVO = SweAdmManager.getInstance().createUsrApl(usrAplVO);

			if (!usrAplVO.hasError()) {
				tx.commit();
				new AuditLog("Alta de Usuario de Aplicaci�n", usrAplVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return usrAplVO;
		} catch (ConstraintViolationException cve) {
			usrAplVO.addRecoverableError(SweCommonError.USR_APL_UNIQUE);
			if(tx != null) tx.rollback();
			return usrAplVO;  
		} catch (Exception e) {
				log.error("Service Error: ",  e);
				if(tx != null) tx.rollback();
				throw new DemodaServiceException(e);
			} finally {
				SweHibernateUtil.closeSession();
			}
		}
	
	public UsrAplVO cloneUsrApl(UserContext userContext, UsrAplVO usrAplVO, UsrAplVO usrAplToCloneVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			// Llama al mismo método que en una creación
			usrAplVO = SweAdmManager.getInstance().cloneUsrApl(usrAplVO, usrAplToCloneVO);

			if (!usrAplVO.hasError()) {
				tx.commit();
				new AuditLog("Alta de Usuario de Aplicaci�n", usrAplVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return usrAplVO;
		} catch (ConstraintViolationException cve) {
			usrAplVO.addRecoverableError(SweCommonError.USR_APL_UNIQUE);
			if(tx != null) tx.rollback();
			return usrAplVO;  
		} catch (Exception e) {
				log.error("Service Error: ",  e);
				if(tx != null) tx.rollback();
				throw new DemodaServiceException(e);
			} finally {
				SweHibernateUtil.closeSession();
			}
		}
			
	public UsrAplVO updateUsrApl(UserContext userContext, UsrAplVO usrAplVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			usrAplVO = SweAdmManager.getInstance().updateUsrApl(usrAplVO);

			if (!usrAplVO.hasError()) {
				tx.commit();
				new AuditLog("Update de Usuario de Aplicaci�n", usrAplVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return usrAplVO; 
		} catch (ConstraintViolationException cve) {
			usrAplVO.addRecoverableError(SweCommonError.USR_APL_UNIQUE);
			if(tx != null) tx.rollback();
			return usrAplVO; 
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	
	public UsrAplVO deleteUsrApl(UserContext userContext, UsrAplVO usrAplVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			usrAplVO = SweAdmManager.getInstance().deleteUsrApl(usrAplVO);

			if (!usrAplVO.hasError()) {
				tx.commit();
				new AuditLog("Borrado de Usuario de Aplicaci�n", usrAplVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return usrAplVO;
		} catch (ConstraintViolationException cve) {
			usrAplVO.addRecoverableError(SweCommonError.USR_APL_HASREF);
			if(tx != null) tx.rollback();
			return usrAplVO; 
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	
	// ABM de Rol Aplicacion
	/*-----------------------------------------------------*/
	/* servicios Rol Aplicacion                            */
	/*-----------------------------------------------------*/
	// searchPage
	public RolAplSearchPage getRolAplSearchPageInit(UserContext userContext, CommonKey aplicacionKey) 
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			RolAplSearchPage rolAplSearchPage = 
				SweAdmManager.getInstance().getRolAplSearchPageInit(aplicacionKey);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return rolAplSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e); //CAMBIO: ,e para que imprima el stack trace
			throw new DemodaServiceException(e); //CAMBIO metemos la EXECEPCION original dentro de DemodaExce...
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public RolAplSearchPage getRolAplSearchPageInitForCreateUsrRolApl(UserContext userContext, CommonKey usrAplCommonKey) 
		throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
	
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			RolAplSearchPage rolAplSearchPage = 
				SweAdmManager.getInstance().getRolAplSearchPageInitForCreateUsrRolApl(usrAplCommonKey);;
	
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return rolAplSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e); //CAMBIO: ,e para que imprima el stack trace
			throw new DemodaServiceException(e); //CAMBIO metemos la EXECEPCION original dentro de DemodaExce...
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	public RolAplSearchPage getRolAplSearchPageResult(UserContext userContext, 
		RolAplSearchPage rolAplSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();

			rolAplSearchPage = SweAdmManager.getInstance().getRolAplSearchPageResult(rolAplSearchPage);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return rolAplSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	
	public RolAplAdapter getRolAplAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			RolAplAdapter rolAplAdapter = SweAdmManager.getInstance().getRolAplAdapter(commonKey);

			log.debug(funcName + ": exit");
			return rolAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}	
	}

	public RolAplAdapter getRolAplAdapterForCreate(UserContext userContext, CommonKey aplicacionKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			RolAplAdapter rolAplAdapter = SweAdmManager.getInstance().getRolAplAdapterForCreate(aplicacionKey);

			log.debug(funcName + ": exit");
			return rolAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	public RolAplAdapter getRolAplAdapterForUpdate(UserContext userContext, CommonKey aplicacionKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			RolAplAdapter rolAplAdapter = SweAdmManager.getInstance().getRolAplAdapter(aplicacionKey);

			log.debug(funcName + ": exit");
			return rolAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	public RolAplAdapter getRolAplAdapterForDelete(UserContext userContext, CommonKey aplicacionKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			RolAplAdapter rolAplAdapter = SweAdmManager.getInstance().getRolAplAdapter(aplicacionKey);

			log.debug(funcName + ": exit");
			return rolAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	public RolAplVO createRolApl(UserContext userContext, RolAplVO rolAplVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			rolAplVO = SweAdmManager.getInstance().createRolApl(rolAplVO);

			if (!rolAplVO.hasError()) {
				tx.commit();
				new AuditLog("Alta de Rol de Aplicaci�n", rolAplVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return rolAplVO;
		} catch (ConstraintViolationException cve) {
			rolAplVO.addRecoverableError(SweCommonError.ROLAPL_UNIQUE);
			if(tx != null) tx.rollback();
			return rolAplVO; 
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	public RolAplVO updateRolApl(UserContext userContext, RolAplVO rolAplVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			rolAplVO = SweAdmManager.getInstance().updateRolApl(rolAplVO);

			if (!rolAplVO.hasError()) {
				tx.commit();
				new AuditLog("Update de Rol de Aplicaci�n", rolAplVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return rolAplVO;
		} catch (ConstraintViolationException cve) {
			rolAplVO.addRecoverableError(SweCommonError.ROLAPL_UNIQUE);
			if(tx != null) tx.rollback();
			return rolAplVO; 
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	public RolAplVO deleteRolApl(UserContext userContext, RolAplVO rolAplVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			rolAplVO = SweAdmManager.getInstance().deleteRolApl(rolAplVO);

			if (!rolAplVO.hasError()) {
				tx.commit();
				new AuditLog("Borrado de Rol de Aplicaci�n", rolAplVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return rolAplVO;
		} catch (ConstraintViolationException cve) {
			rolAplVO.addRecoverableError(SweCommonError.ROLAPL_HASREF);
			if(tx != null) tx.rollback();
			return rolAplVO; 
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	/*-----------------------------------------------------*/
	/* servicios Rol Accion Modulo Aplicacion              */
	/*-----------------------------------------------------*/

	public RolAccModAplSearchPage getRolAccModAplSearchPageInit(UserContext userContext, CommonKey rolAplKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			RolAccModAplSearchPage rolAccModAplSearchPage = SweAdmManager.getInstance().getRolAccModAplSearchPageInit(rolAplKey);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return rolAccModAplSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e); //CAMBIO: ,e para que imprima el stack trace
			throw new DemodaServiceException(e); //CAMBIO metemos la EXECEPCION original dentro de DemodaExce...
		} finally {
			SweHibernateUtil.closeSession();
		}
		
	}
	
	public RolAccModAplSearchPage getRolAccModAplSearchPageResult(UserContext userContext, RolAccModAplSearchPage rolAccModAplSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();

			rolAccModAplSearchPage = SweAdmManager.getInstance().getRolAccModAplSearchPageResult(rolAccModAplSearchPage);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return rolAccModAplSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public RolAccModAplAdapter getRolAccModAplAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			RolAccModAplAdapter rolAccModAplAdapter = SweAdmManager.getInstance().getRolAccModAplAdapter(commonKey);

			log.debug(funcName + ": exit");
			return rolAccModAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public RolAccModAplAdapter getRolAccModAplAdapterForDelete(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			RolAccModAplAdapter rolAccModAplAdapter = SweAdmManager.getInstance().getRolAccModAplAdapter(commonKey);

			log.debug(funcName + ": exit");
			return rolAccModAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public RolAplVO createRolAccModAplMultiple(UserContext userContext, RolAplVO rolAplVO, Long[] listId) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			RolAccModAplVO rolAccModAplVO = new RolAccModAplVO();
			
			for (Long idLong: listId){
				rolAccModAplVO.setRolApl(rolAplVO);
				
				rolAccModAplVO.getAccModApl().setId(idLong);
				
				rolAccModAplVO = SweAdmManager.getInstance().createRolAccModApl(rolAccModAplVO);

				rolAccModAplVO = new RolAccModAplVO();

			}

			if (!rolAccModAplVO.hasError()) {
				tx.commit();
				new AuditLog("Alta de Acci�n de Rol de Aplicaci�n", rolAccModAplVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				rolAccModAplVO.passErrorMessages(rolAplVO);
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return rolAplVO;
		} catch (ConstraintViolationException cve) {
			rolAplVO.addRecoverableError(SweCommonError.ROLACCMODAPL_UNIQUE);
			if(tx != null) tx.rollback();
			return rolAplVO; 
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public RolAccModAplVO deleteRolAccModApl(UserContext userContext, RolAccModAplVO rolAccModAplVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			rolAccModAplVO = SweAdmManager.getInstance().deleteRolAccModApl(rolAccModAplVO);

			if (!rolAccModAplVO.hasError()) {
				tx.commit();
				new AuditLog("Borrado de Acci�n de Rol de Aplicaci�n", rolAccModAplVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return rolAccModAplVO;
		} catch (ConstraintViolationException cve) {
			rolAccModAplVO.addRecoverableError(SweCommonError.ROLACCMODAPL_HASREF);
			if(tx != null) tx.rollback();
			return rolAccModAplVO; 
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	/*-----------------------------------------------------*/
	/* servicios de Roles de Usuarios de Aplicacion        */
	/*-----------------------------------------------------*/

	public UsrRolAplSearchPage getUsrRolAplSearchPageInit(UserContext userContext, CommonKey usrAplCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			UsrRolAplSearchPage usrAplSearchPage = SweAdmManager.getInstance().getUsrRolAplSearchPageInit(usrAplCommonKey);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return usrAplSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e); //CAMBIO: ,e para que imprima el stack trace
			throw new DemodaServiceException(e); //CAMBIO metemos la EXECEPCION original dentro de DemodaExce...
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	public UsrRolAplSearchPage getUsrRolAplSearchPageResult(UserContext userContext, UsrRolAplSearchPage usrAplSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();

			usrAplSearchPage = SweAdmManager.getInstance().getUsrRolAplSearchPageResult(usrAplSearchPage);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return usrAplSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public UsrRolAplAdapter getUsrRolAplAdapterForView(UserContext userContext, CommonKey usrRolAplCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession(); 
			
			UsrRolAplAdapter usrRolAplAdapter = SweAdmManager.getInstance().getUsrRolAplAdapter(usrRolAplCommonKey);

			log.debug(funcName + ": exit");
			return usrRolAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public UsrRolAplAdapter getUsrRolAplAdapterForDelete(UserContext userContext, CommonKey usrRolAplCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();

			UsrRolAplAdapter usrRolAplAdapter = SweAdmManager.getInstance().getUsrRolAplAdapter(usrRolAplCommonKey);

			log.debug(funcName + ": exit");
			return usrRolAplAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public UsrRolAplVO createUsrRolApl(UserContext userContext, UsrRolAplVO usrRolAplVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			usrRolAplVO = SweAdmManager.getInstance().createUsrRolApl(usrRolAplVO);

			if (!usrRolAplVO.hasError()) {
				tx.commit();
				new AuditLog("Alta de Rol Usuario de Aplicaci�n", usrRolAplVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return usrRolAplVO;
		} catch (ConstraintViolationException cve) {
			usrRolAplVO.addRecoverableError(SweCommonError.USRROLAPL_UNIQUE);
			if(tx != null) tx.rollback();
			return usrRolAplVO; 
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public UsrRolAplVO deleteUsrRolApl(UserContext userContext, UsrRolAplVO usrRolAplVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			usrRolAplVO = SweAdmManager.getInstance().deleteUsrRolApl(usrRolAplVO);

			if (!usrRolAplVO.hasError()) {
				tx.commit();
				new AuditLog("Borrado de Rol Usuario de Aplicaci�n", usrRolAplVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return usrRolAplVO;
		} catch (ConstraintViolationException cve) {
			usrRolAplVO.addRecoverableError(SweCommonError.USRROLAPL_HASREF);
			if(tx != null) tx.rollback();
			return usrRolAplVO; 
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	// Item de menu

	public ItemMenuSearchPage getItemMenuSearchPageInit(UserContext userContext, CommonKey aplicacionCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			ItemMenuSearchPage itemMenuSearchPage = SweAdmManager.getInstance().getItemMenuSearchPageInit(aplicacionCommonKey); 

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return itemMenuSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e); //CAMBIO: ,e para que imprima el stack trace
			throw new DemodaServiceException(e); //CAMBIO metemos la EXECEPCION original dentro de DemodaExce...
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public ItemMenuSearchPage getItemMenuHijosSearchPageInit(UserContext userContext, CommonKey itemMenuPadreCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			ItemMenuSearchPage itemMenuSearchPage = SweAdmManager.getInstance().getItemMenuHijosSearchPageInit(itemMenuPadreCommonKey);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return itemMenuSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e); //CAMBIO: ,e para que imprima el stack trace
			throw new DemodaServiceException(e); //CAMBIO metemos la EXECEPCION original dentro de DemodaExce...
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	public ItemMenuSearchPage getItemMenuSearchPageResult(UserContext userContext, ItemMenuSearchPage itemMenuSearchPage) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();

			itemMenuSearchPage = SweAdmManager.getInstance().getItemMenuSearchPageResult(itemMenuSearchPage);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return itemMenuSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public ItemMenuAdapter getItemMenuAdapterForView(UserContext userContext, CommonKey itemMenuCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession(); 
			
			ItemMenuAdapter itemMenuAdapter = SweAdmManager.getInstance().getItemMenuAdapter(itemMenuCommonKey);
			log.debug(funcName + ": exit");
			return itemMenuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
		
	}
	
	public ItemMenuAdapter getItemMenuAdapterForCreateRoot(UserContext userContext, CommonKey aplicacionCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			ItemMenuAdapter itemMenuAdapter = SweAdmManager.getInstance().getItemMenuAdapterForCreateRoot(aplicacionCommonKey);

			log.debug(funcName + ": exit");
			return itemMenuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public ItemMenuAdapter getItemMenuAdapterForCreate(UserContext userContext, CommonKey itemMenuPadreCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();
			
			ItemMenuAdapter itemMenuAdapter = SweAdmManager.getInstance().getItemMenuAdapterForCreate(itemMenuPadreCommonKey);

			log.debug(funcName + ": exit");
			return itemMenuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public ItemMenuAdapter getItemMenuAdapterForUpdate(UserContext userContext, CommonKey itemMenuCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();

			ItemMenuAdapter itemMenuAdapter =  SweAdmManager.getInstance().getItemMenuAdapter(itemMenuCommonKey);

			log.debug(funcName + ": exit");
			return itemMenuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public ItemMenuAdapter getItemMenuAdapterParam(UserContext userContext, ItemMenuAdapter itemMenuAdapter) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();

			itemMenuAdapter =  SweAdmManager.getInstance().getItemMenuAdapterParam(itemMenuAdapter);
			
			log.debug(funcName + ": exit");
			return itemMenuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}

	public ItemMenuAdapter getItemMenuAdapterForDelete(UserContext userContext, CommonKey itemMenuCommonKey) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SweHibernateUtil.currentSession();

			ItemMenuAdapter itemMenuAdapter = SweAdmManager.getInstance().getItemMenuAdapter(itemMenuCommonKey);

			log.debug(funcName + ": exit");
			return itemMenuAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public ItemMenuVO createItemMenu(UserContext userContext, ItemMenuVO itemMenuVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			itemMenuVO = SweAdmManager.getInstance().createItemMenu(itemMenuVO); 

			if (!itemMenuVO.hasError()) {
				tx.commit();
				new AuditLog("Alta de Item de Men� de Aplicaci�n", itemMenuVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return itemMenuVO;
		} catch (ConstraintViolationException cve) {
			itemMenuVO.addRecoverableError(SweCommonError.ITEM_MENU_UNIQUE);
			if(tx != null) tx.rollback();
			return itemMenuVO;  
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public ItemMenuVO updateItemMenu(UserContext userContext, ItemMenuVO itemMenuVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			itemMenuVO = SweAdmManager.getInstance().updateItemMenu(itemMenuVO);

			if (!itemMenuVO.hasError()) {
				tx.commit();
				new AuditLog("Update de Item de Men� de Aplicaci�n", itemMenuVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}
			
			log.debug(funcName + ": exit");
			return itemMenuVO; 
		} catch (ConstraintViolationException cve) {
			itemMenuVO.addRecoverableError(SweCommonError.ITEM_MENU_UNIQUE);
			if(tx != null) tx.rollback();
			return itemMenuVO;  
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}
	
	public ItemMenuVO deleteItemMenu(UserContext userContext, ItemMenuVO itemMenuVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SweHibernateUtil.currentSession();
			tx = session.beginTransaction();

			itemMenuVO = SweAdmManager.getInstance().deleteItemMenu(itemMenuVO);

			if (!itemMenuVO.hasError()) {
				tx.commit();
				new AuditLog("Borrado de Item de Men� de Aplicaci�n", itemMenuVO).info();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
			} else {
				tx.rollback();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			}

			log.debug(funcName + ": exit");
			return itemMenuVO;
		} catch (ConstraintViolationException cve) {
			itemMenuVO.addRecoverableError(SweCommonError.ITEM_MENU_HASREF);
			if(tx != null) tx.rollback();
			return itemMenuVO;  
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SweHibernateUtil.closeSession();
		}
	}	
}
