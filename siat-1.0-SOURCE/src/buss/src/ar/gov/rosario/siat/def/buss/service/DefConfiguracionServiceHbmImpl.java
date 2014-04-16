//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.service;

/**
 * Implementacion de servicios del submodulo Configuracion del modulo Definicion
 * @author tecso
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.IndeterminadoFacade;
import ar.gov.rosario.siat.base.buss.dao.SiatBussCache;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.SiatCache;
import ar.gov.rosario.siat.cas.iface.model.CasoCache;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.DefConfiguracionManager;
import ar.gov.rosario.siat.def.buss.bean.Parametro;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.RecursoArea;
import ar.gov.rosario.siat.def.buss.bean.SiatScript;
import ar.gov.rosario.siat.def.buss.bean.SiatScriptUsr;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.AreaAdapter;
import ar.gov.rosario.siat.def.iface.model.AreaSearchPage;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.ParametroAdapter;
import ar.gov.rosario.siat.def.iface.model.ParametroSearchPage;
import ar.gov.rosario.siat.def.iface.model.ParametroVO;
import ar.gov.rosario.siat.def.iface.model.RecursoAreaAdapter;
import ar.gov.rosario.siat.def.iface.model.RecursoAreaVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.SiatScriptAdapter;
import ar.gov.rosario.siat.def.iface.model.SiatScriptSearchPage;
import ar.gov.rosario.siat.def.iface.model.SiatScriptUsrAdapter;
import ar.gov.rosario.siat.def.iface.model.SiatScriptUsrVO;
import ar.gov.rosario.siat.def.iface.model.SiatScriptVO;
import ar.gov.rosario.siat.def.iface.service.IDefConfiguracionService;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.fra.iface.util.Frase;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.pro.buss.dao.ProDAOFactory;
import ar.gov.rosario.siat.pro.iface.model.ProcesoVO;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import ar.gov.rosario.siat.seg.iface.model.UsuarioSiatVO;
import ar.gov.rosario.swe.SweServiceLocator;
import ar.gov.rosario.swe.buss.dao.SweHibernateUtil;
import ar.gov.rosario.swe.iface.model.SweContext;
import ar.gov.rosario.swe.iface.service.ISweService;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaTimer;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;
import coop.tecso.grs.Grs;

public class DefConfiguracionServiceHbmImpl implements IDefConfiguracionService {
	private Logger log = Logger.getLogger(DefConfiguracionServiceHbmImpl.class);
	
	// ---> ABM Area 	
	public AreaSearchPage getAreaSearchPageInit(UserContext usercontext) throws DemodaServiceException {		
		return new AreaSearchPage();
	}

	public AreaSearchPage getAreaSearchPageResult(UserContext userContext, AreaSearchPage areaSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			areaSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Area> listArea = DefDAOFactory.getAreaDAO().getBySearchPage(areaSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		areaSearchPage.setListResult(ListUtilBean.toVO(listArea,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return areaSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AreaAdapter getAreaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Area area = Area.getById(commonKey.getId());

	        AreaAdapter areaAdapter = new AreaAdapter();
	        areaAdapter.setArea((AreaVO) area.toVO(1));
			
			log.debug(funcName + ": exit");
			return areaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AreaAdapter getAreaAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			AreaAdapter areaAdapter = new AreaAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return areaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public AreaAdapter getAreaAdapterForUpdate(UserContext userContext, CommonKey commonKeyArea) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Area area = Area.getById(commonKeyArea.getId());

	        AreaAdapter areaAdapter = new AreaAdapter();
	        areaAdapter.setArea((AreaVO) area.toVO(1));

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return areaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AreaVO createArea(UserContext userContext, AreaVO areaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			areaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Area area = new Area();
            area.setCodArea(areaVO.getCodArea());
            area.setDesArea(areaVO.getDesArea());
            
            area.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            area = DefConfiguracionManager.getInstance().createArea(area);
            
            if (area.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				areaVO =  (AreaVO) area.toVO(3);
			}
			area.passErrorMessages(areaVO);
            
            log.debug(funcName + ": exit");
            return areaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AreaVO updateArea(UserContext userContext, AreaVO areaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			areaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Area area = Area.getById(areaVO.getId());

            if(!areaVO.validateVersion(area.getFechaUltMdf())) return areaVO;
            
            area.setCodArea(areaVO.getCodArea());
            area.setDesArea(areaVO.getDesArea());
                                    
            area.setEstado(Estado.ACTIVO.getId());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            area = DefConfiguracionManager.getInstance().updateArea(area);
            
            if (area.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				areaVO =  (AreaVO) area.toVO(3);
			}
			area.passErrorMessages(areaVO);
            
            log.debug(funcName + ": exit");
            return areaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AreaVO deleteArea(UserContext userContext, AreaVO areaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			areaVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Area area = Area.getById(areaVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			area = DefConfiguracionManager.getInstance().deleteArea(area);
			
			if (area.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				areaVO =  (AreaVO) area.toVO(3);
			}
			area.passErrorMessages(areaVO);
            
            log.debug(funcName + ": exit");
            return areaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AreaVO activarArea(UserContext userContext, AreaVO areaVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Area area = Area.getById(areaVO.getId());

            area.activar();

            if (area.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				areaVO =  (AreaVO) area.toVO();
			}
            area.passErrorMessages(areaVO);
            
            log.debug(funcName + ": exit");
            return areaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AreaVO desactivarArea(UserContext userContext, AreaVO areaVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Area area = Area.getById(areaVO.getId());

            area.desactivar();

            if (area.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				areaVO =  (AreaVO) area.toVO();
			}
            area.passErrorMessages(areaVO);
            
            log.debug(funcName + ": exit");
            return areaVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	
	public AreaAdapter imprimirArea(UserContext userContext, AreaAdapter areaAdapterVO ) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Area area = Area.getById(areaAdapterVO.getArea().getId());

			PadDAOFactory.getContribuyenteDAO().imprimirGenerico(area, areaAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return areaAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
	   }
		
		
	}
	// <--- ABM Area

	// ---> ABM Parametro 	
	public ParametroSearchPage getParametroSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		return new ParametroSearchPage();
	}

	public ParametroSearchPage getParametroSearchPageResult(UserContext userContext, ParametroSearchPage parametroSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			parametroSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Parametro> listParametro = DefDAOFactory.getParametroDAO().getBySearchPage(parametroSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		parametroSearchPage.setListResult(ListUtilBean.toVO(listParametro,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return parametroSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	public ParametroAdapter getParametroAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Parametro parametro = Parametro.getById(commonKey.getId());

	        ParametroAdapter parametroAdapter = new ParametroAdapter();
	        parametroAdapter.setParametro((ParametroVO) parametro.toVO(1));
			
			log.debug(funcName + ": exit");
			return parametroAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ParametroAdapter getParametroAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ParametroAdapter parametroAdapter = new ParametroAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return parametroAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ParametroAdapter getParametroAdapterParam(UserContext userContext, ParametroAdapter parametroAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			parametroAdapter.clearError();
			
			// Logica del param
			
			
			
			log.debug(funcName + ": exit");
			return parametroAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ParametroAdapter getParametroAdapterForUpdate(UserContext userContext, CommonKey commonKeyParametro) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Parametro parametro = Parametro.getById(commonKeyParametro.getId());
			
	        ParametroAdapter parametroAdapter = new ParametroAdapter();
	        parametroAdapter.setParametro((ParametroVO) parametro.toVO(1));

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return parametroAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ParametroVO createParametro(UserContext userContext, ParametroVO parametroVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			parametroVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Parametro parametro = new Parametro();
            parametro.setCodParam(parametroVO.getCodParam());
            parametro.setDesParam(parametroVO.getDesParam());
            parametro.setValor(parametroVO.getValor());
            
            parametro.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            parametro = DefConfiguracionManager.getInstance().createParametro(parametro);
            
            if (parametro.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				parametroVO =  (ParametroVO) parametro.toVO(3);
			}
			parametro.passErrorMessages(parametroVO);
            
            log.debug(funcName + ": exit");
            return parametroVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ParametroVO updateParametro(UserContext userContext, ParametroVO parametroVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			parametroVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            Parametro parametro = Parametro.getById(parametroVO.getId());
            
            if(!parametroVO.validateVersion(parametro.getFechaUltMdf())) return parametroVO;
            
            parametro.setCodParam(parametroVO.getCodParam());
            parametro.setDesParam(parametroVO.getDesParam());
            parametro.setValor(parametroVO.getValor());
                                    
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            parametro = DefConfiguracionManager.getInstance().updateParametro(parametro);
            
            if (parametro.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				parametroVO =  (ParametroVO) parametro.toVO(3);
			}
			parametro.passErrorMessages(parametroVO);
            
            log.debug(funcName + ": exit");
            return parametroVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ParametroVO deleteParametro(UserContext userContext, ParametroVO parametroVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			parametroVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Parametro parametro = Parametro.getById(parametroVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			parametro = DefConfiguracionManager.getInstance().deleteParametro(parametro);
			
			if (parametro.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				parametroVO =  (ParametroVO) parametro.toVO(3);
			}
			parametro.passErrorMessages(parametroVO);
            
            log.debug(funcName + ": exit");
            return parametroVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ParametroVO activarParametro(UserContext userContext, ParametroVO parametroVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Parametro parametro = Parametro.getById(parametroVO.getId());

            parametro.activar();

            if (parametro.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				parametroVO =  (ParametroVO) parametro.toVO();
			}
            parametro.passErrorMessages(parametroVO);
            
            log.debug(funcName + ": exit");
            return parametroVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ParametroVO desactivarParametro(UserContext userContext, ParametroVO parametroVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Parametro parametro = Parametro.getById(parametroVO.getId());

            parametro.desactivar();

            if (parametro.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				parametroVO =  (ParametroVO) parametro.toVO();
			}
            parametro.passErrorMessages(parametroVO);
            
            log.debug(funcName + ": exit");
            return parametroVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	// <--- ABM Parametro
	
	
	// ---> ABM RecursoArea
	public AreaAdapter getAdapterForRecursoAreaView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Area area = Area.getById(commonKey.getId());
			AreaAdapter areaAdapter = new AreaAdapter();
			areaAdapter.setArea((AreaVO) area.toVO(1, false));
			areaAdapter.getArea().setListRecursoArea(ListUtilBean.toVO(area.getListRecursoArea(), 2, false));
			
			log.debug(funcName + ": exit");
			return areaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RecursoAreaAdapter getRecursoAreaAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException{
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
		
			RecursoArea recursoArea = RecursoArea.getById(commonKey.getId());
			
	        RecursoAreaAdapter recursoAreaAdapter = new RecursoAreaAdapter();
	        recursoAreaAdapter.setRecursoArea((RecursoAreaVO) recursoArea.toVO(2));
	        recursoAreaAdapter.getRecursoArea().setRecurso((RecursoVO) recursoArea.getRecurso().toVO(2, false));
	       
			recursoAreaAdapter.getRecursoArea().getArea().setListRecursoArea(ListUtilBean.toVO(recursoArea.getArea().getListRecursoArea(), 2, false));
			
			log.debug(funcName + ": exit");
			return recursoAreaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	

	public RecursoAreaAdapter getRecursoAreaAdapterForCreate(UserContext userContext, CommonKey areaCommonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			RecursoAreaAdapter recursoAreaAdapter = new RecursoAreaAdapter();
			
			Area area = Area.getById(areaCommonKey.getId());
			
			recursoAreaAdapter.getRecursoArea().setArea((AreaVO) area.toVO(1, false));
			recursoAreaAdapter.getRecursoArea().getArea().setListRecursoArea(ListUtilBean.toVO(area.getListRecursoArea(), 2, false));
			
			recursoAreaAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));
			
			List<Recurso> listRecursoExcluir = new ArrayList<Recurso>();
			List<Recurso> listRecursoFiltrada = new ArrayList<Recurso>();
			
			for (RecursoArea ra:area.getListRecursoArea()){
				listRecursoExcluir.add(ra.getRecurso());
			}
			
			List<Recurso> listRecurso = Recurso.getListTributariosVigentes(new Date());
			
			listRecursoFiltrada = (List<Recurso>) ListUtilBean.getListExclude(listRecurso, listRecursoExcluir);
			
			recursoAreaAdapter.setListRecurso(ListUtilBean.toVO(listRecursoFiltrada, 2, false));
			recursoAreaAdapter.getListRecurso().add(0,  new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			
			log.debug(funcName + ": exit");
			return recursoAreaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public RecursoAreaAdapter getRecursoAreaAdapterForUpdate(UserContext userContext, CommonKey commonKeyRecursoArea) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			RecursoArea recursoArea = RecursoArea.getById(commonKeyRecursoArea.getId());
			
	        RecursoAreaAdapter recursoAreaAdapter = new RecursoAreaAdapter();
	        recursoAreaAdapter.setRecursoArea((RecursoAreaVO) recursoArea.toVO(2));
	        recursoAreaAdapter.getRecursoArea().setRecurso((RecursoVO) recursoArea.getRecurso().toVO(2, false));
	       
	        recursoAreaAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));

			log.debug(funcName + ": exit");
			return recursoAreaAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RecursoAreaVO createRecursoArea(UserContext userContext, RecursoAreaVO recursoAreaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recursoAreaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            RecursoArea recursoArea = new RecursoArea();
            
            Area area = Area.getById(recursoAreaVO.getArea().getId());
            Recurso recurso = Recurso.getByIdNull(recursoAreaVO.getRecurso().getId());
           
            recursoArea.setArea(area);
            recursoArea.setRecurso(recurso);
            recursoArea.setPerCreaEmi(recursoAreaVO.getPerCreaEmi().getBussId());
            
            recursoArea.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            recursoArea = recurso.createRecursoArea(recursoArea);
            
            if (recursoArea.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			recursoArea.passErrorMessages(recursoAreaVO);
            
            log.debug(funcName + ": exit");
            return recursoAreaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RecursoAreaVO updateRecursoArea(UserContext userContext, RecursoAreaVO recursoAreaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recursoAreaVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            RecursoArea recursoArea = RecursoArea.getById(recursoAreaVO.getId());
			
			if(!recursoAreaVO.validateVersion(recursoArea.getFechaUltMdf())) return recursoAreaVO;
			
			Recurso recurso = Recurso.getByIdNull(recursoAreaVO.getRecurso().getId());
			 
            recursoArea.setRecurso(recurso);
            recursoArea.setPerCreaEmi(recursoAreaVO.getPerCreaEmi().getBussId());
	            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            recursoArea = recursoArea.getRecurso().updateRecursoArea(recursoArea);
            
            if (recursoArea.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				recursoAreaVO =  (RecursoAreaVO) recursoArea.toVO(0,false);
			}
			recursoArea.passErrorMessages(recursoAreaVO);
            
            log.debug(funcName + ": exit");
            return recursoAreaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RecursoAreaVO deleteRecursoArea(UserContext userContext, RecursoAreaVO recursoAreaVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			recursoAreaVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			RecursoArea recursoArea = RecursoArea.getById(recursoAreaVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			recursoArea = recursoArea.getRecurso().deleteRecursoArea(recursoArea);
			
			if (recursoArea.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			recursoArea.passErrorMessages(recursoAreaVO);
            
            log.debug(funcName + ": exit");
            return recursoAreaVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// <--- ABM RecursoArea
	
	
	// ----> Incializacion / Destruccion de SIAT
	public void updateSiatParam()throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			SiatHibernateUtil.currentSession(); 
			
			/*Throwable e = new Exception().fillInStackTrace();
			log.debug( funcName + ": ", e);*/
			
			DefConfiguracionManager.getInstance().updateSiatParam();
			
			log.debug(funcName + ": exit");
			return;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public void initializeSiat() throws DemodaServiceException {
		try {
			System.out.println("Iniciando SWE Context");
			DemodaTimer dt = new DemodaTimer(); 
			if (SiatCache.getInstance().getSweContext() == null) {
				ISweService sweService = new ar.gov.rosario.swe.buss.service.SweServiceHbmImpl();
				SweContext sweContext = sweService.getSweContext("SIAT");
				SiatCache.getInstance().setSweContext(sweContext);
			}		
			long swetime = dt.stop();
			// Actualiza el contenido del mapa de los parametros del singleton SiatParam en Iface
			System.out.println("Iniciando Tabla de Parametros");
			updateSiatParam();
			long paramtime = dt.stop();
			
			System.out.println("Iniciando Cache de Sistema Origen");
			CasoCache.getInstance().setListSistemaOrigen(CasServiceLocator.getCasCasoService().getListSistemaOrigenInit());
			long casocachetime = dt.stop();

			System.out.println("Iniciando Estructura de directorios de Procesps de Adp.");
			initFileShareDirs();
			long dirstime = dt.stop();

			System.out.println("Iniciando Estructura de directorios de Procesps de Adp.");
			Frase.getInstance(); //causa load de frases (si aun no se cargaron)
			long frasetime = dt.stop();

			System.out.println("Iniciando Grs.");
			Grs.GrsPath = new File(SiatParam.getFileSharePath(), "privado").getPath();
			Grs.GrsPageTemplate = "/Grs.do";
			long grstime = dt.stop();
			
			
			
			String summary = "\nSIAT startup summary:\n";
			summary += "swe:" + swetime + "ms\n";
			summary += "parametros:" + paramtime + "ms\n";
			summary += "caso_sistema_orig cahe:" + casocachetime + "ms\n";
			summary += "directorios:" + dirstime + "ms\n";
			summary += "frases:" + frasetime + "ms\n";
			summary += "grs:" + grstime + "ms\n";
			summary += "total:" + (swetime + paramtime + dirstime + frasetime) + "(ms)\n";
			log.info(summary);
		} catch  (Exception e) {
			throw new DemodaServiceException("Error al inicializar siat.", e); 
		}
	}
	
	public void destroySiat() throws DemodaServiceException {
		SiatHibernateUtil.closeSessionFactory();
		SweHibernateUtil.closeSessionFactory();
	}
	
	public void initFileShareDirs() throws Exception {
		//crea la estructura definida por los procesos
		File dir = null;
		@SuppressWarnings({"unchecked"})
		List<Proceso> listProceso = ProDAOFactory.getProcesoDAO().getListActiva();
		for(Proceso proceso : listProceso) {
			String di = proceso.getDirectorioInput();
			if (di == null || di.equals("null") || di.trim().equals("")) {
				continue;
			}
			
			for(AdpRunDirEnum e : AdpRunDirEnum.values()) {
				dir = new File(proceso.getDirectorioInput() + "/" + e.dirName());
				if (!dir.exists() && !dir.mkdirs()) {
						log.error("No se pudo crear el directorio: " + dir.getPath());
				}
			} 
		}
		
		//crea directorio temporal
		dir = new File(SiatParam.getFileSharePath() + "/publico/tmp");
		if (!dir.exists() && !dir.mkdirs()) {
				log.error("No se pudo crear el directorio: " + dir.getPath());
		}

		//Estructura de reportes
		dir = new File(SiatParam.getFileSharePath() + "/publico/general/reportes/db");
		if (!dir.exists() && !dir.mkdirs()) {
				log.error("No se pudo crear el directorio: " + dir.getPath());
		}
		dir = new File(SiatParam.getFileSharePath() + "/publico/general/reportes/images");
		if (!dir.exists() && !dir.mkdirs()) {
				log.error("No se pudo crear el directorio: " + dir.getPath());
		}
	} 

	
	/***
	 * Refresca todos los caches 
	 */
	public void refreshCache(String cacheFlag) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			SiatHibernateUtil.currentSession(); 

			if (cacheFlag == null || cacheFlag.equals(DefError.CACHE_INDETERMINADO)) {
				log.info("Refrescando cache de Indeterminados");
				IndeterminadoFacade.getInstance().invalidateCache();
			}
	
			if (cacheFlag == null || cacheFlag.equals(DefError.CACHE_DEFATRIBUTO)) {
				log.info("Refrescando cache de Definiciones de Atributos");
				SiatBussCache.getInstance().reloadAllTipObjImpAtr();
				SiatBussCache.getInstance().reloadAllAtributoRecursoCuentaDefinition();
			}
	
			if (cacheFlag == null || cacheFlag.equals(DefError.CACHE_SWE)) {
				log.info("Refrescando cachede  Seguridad SWE");
				SweContext sweContext = SweServiceLocator.getSweService().getSweContext("SIAT");
				SiatCache.getInstance().setSweContext(sweContext);
			}
	
			if (cacheFlag == null || cacheFlag.equals(DefError.CACHE_PARAMETRO)) {
				log.info("Refrescando cache de Parametros del Siat.");
				updateSiatParam();
			}
	
			if (cacheFlag == null || cacheFlag.equals(DefError.CACHE_CASO)) {
				log.info("Refrescando cache Casos.");
				CasoCache.getInstance().setListSistemaOrigen(CasServiceLocator.getCasCasoService().getListSistemaOrigenInit());
			}

			if (cacheFlag == null || cacheFlag.equals(DefError.CACHE_FRASE)) {
				log.info("Refrescando cache de Frases.");
				Frase.getInstance().reload();
			}
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <----- Incializacion / Destruccion de SIAT
	
	
	// ---> ABM SiatScript	
	public SiatScriptSearchPage getSiatScriptSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		return new SiatScriptSearchPage();
	}

	public SiatScriptSearchPage getSiatScriptSearchPageResult(UserContext userContext, SiatScriptSearchPage siatScriptSearchPage)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			siatScriptSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<SiatScript> listSiatScript = DefDAOFactory.getSiatScriptDAO().getBySearchPage(siatScriptSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		siatScriptSearchPage.setListResult(ListUtilBean.toVO(listSiatScript,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return siatScriptSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SiatScriptAdapter getSiatScriptAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {	
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			SiatScript siatScript = SiatScript.getById(commonKey.getId());

	        SiatScriptAdapter siatScriptAdapter = new SiatScriptAdapter();
	        siatScriptAdapter.setSiatScript((SiatScriptVO) siatScript.toVO(2));
	        
			// Seteo la listas para el combo de Siat ScriptUsr			
			List<SiatScriptUsr> listSiatScriptUsr = SiatScriptUsr.getListActivos();
			siatScriptAdapter.setListSiatScriptUsr((ArrayList<SiatScriptUsrVO>)ListUtilBean.toVO(listSiatScriptUsr, 
					new SiatScriptUsrVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return siatScriptAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SiatScriptAdapter getSiatScriptAdapterForCreate(	UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			SiatScriptAdapter siatScriptAdapter = new SiatScriptAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			
			log.debug(funcName + ": exit");
			return siatScriptAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public SiatScriptAdapter getSiatScriptAdapterParam(UserContext userContext,	SiatScriptAdapter siatScriptAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			siatScriptAdapter.clearError();
			
			// Logica del param
			
			
			
			log.debug(funcName + ": exit");
			return siatScriptAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	public SiatScriptAdapter getSiatScriptAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			SiatScriptAdapter siatScriptAdapter = new SiatScriptAdapter();
			SiatScript siatScript = SiatScript.getById(commonKey.getId());			 
			File scriptFile;
			try {
				 //Seteo el archivo de script para poder editarlo  
				scriptFile = new File(siatScript.getPath());
				BufferedReader currentBuffer = new BufferedReader(new FileReader(scriptFile));	        
			    String scriptStr = new String();    	        
			        
			    while (currentBuffer.ready()) {
			        	scriptStr+='\n'+currentBuffer.readLine();				
					}
			    
			    siatScript.setScriptFile(scriptStr);
				
			} catch (FileNotFoundException e) {
				log.error("Service Error lalala1: ",  e);
				siatScript.setScriptFile("");
				siatScriptAdapter.addRecoverableError(DefError.SIATSCRIPT_READSIATSCRIPT);		
			}	       
	        
	      
	        siatScriptAdapter.setSiatScript((SiatScriptVO) siatScript.toVO(2));
	        
			// Seteo la listas para el combo de Siat ScriptUsr			
			List<SiatScriptUsr> listSiatScriptUsr = SiatScriptUsr.getListActivos();
			siatScriptAdapter.setListSiatScriptUsr((ArrayList<SiatScriptUsrVO>)ListUtilBean.toVO(listSiatScriptUsr, 
					new SiatScriptUsrVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			

			
			log.debug(funcName + ": exit");
			return siatScriptAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SiatScriptVO createSiatScript(UserContext userContext,SiatScriptVO siatScriptVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			siatScriptVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            SiatScript siatScript = new SiatScript();

            this.copyFromVO(siatScript, siatScriptVO);
            
            siatScript.setEstado(Estado.ACTIVO.getId());            
            
	        File scriptFile;
			try {
				 scriptFile = new File(siatScriptVO.getPath());
				 BufferedWriter currentBuffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(scriptFile, false), "ISO-8859-1"));	            
			     currentBuffer.write(siatScriptVO.getScriptFile());
			     currentBuffer.close(); 
			} catch (FileNotFoundException e) {	
				log.error("Service Error: ",  e);
				siatScriptVO.addRecoverableError(DefError.SIATSCRIPT_READSIATSCRIPT);
				return siatScriptVO;
			}	
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            siatScript = DefConfiguracionManager.getInstance().createSiatScript(siatScript);
            
            if (siatScript.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				siatScriptVO =  (SiatScriptVO) siatScript.toVO(0,false);
			}
			siatScript.passErrorMessages(siatScriptVO);
            
            log.debug(funcName + ": exit");
            return siatScriptVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SiatScriptVO updateSiatScript(UserContext userContext,SiatScriptVO siatScriptVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			siatScriptVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            SiatScript siatScript = SiatScript.getById(siatScriptVO.getId());
			
			if(!siatScriptVO.validateVersion(siatScript.getFechaUltMdf())) return siatScriptVO;
			
            this.copyFromVO(siatScript, siatScriptVO);
                  
	        File scriptFile;
			try {
				 scriptFile = new File(siatScriptVO.getPath());
				 BufferedWriter currentBuffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(scriptFile, false), "ISO-8859-1"));	            
			     currentBuffer.write(siatScriptVO.getScriptFile());
			     currentBuffer.close(); 
			} catch (FileNotFoundException e) {	
				log.error("Service Error: ",  e);
				siatScriptVO.addRecoverableError(DefError.SIATSCRIPT_READSIATSCRIPT);
				return siatScriptVO;
			}	 	      
	                         
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            siatScript = DefConfiguracionManager.getInstance().updateSiatScript(siatScript);
            
            if (siatScript.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				siatScriptVO =  (SiatScriptVO) siatScript.toVO(0,false);
			}
			siatScript.passErrorMessages(siatScriptVO);
            
            log.debug(funcName + ": exit");
            return siatScriptVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SiatScriptVO deleteSiatScript(UserContext userContext, SiatScriptVO siatScriptVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			siatScriptVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			SiatScript siatScript = SiatScript.getById(siatScriptVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			siatScript = DefConfiguracionManager.getInstance().deleteSiatScript(siatScript);
			
			if (siatScript.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				siatScriptVO =  (SiatScriptVO) siatScript.toVO(0,false);
			}
			siatScript.passErrorMessages(siatScriptVO);
            
            log.debug(funcName + ": exit");
            return siatScriptVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SiatScriptVO activarSiatScript(UserContext userContext,	SiatScriptVO siatScriptVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		      
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            SiatScript siatScript = SiatScript.getById(siatScriptVO.getId());
                           
            siatScript.desactivar();

            if (siatScript.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				siatScriptVO =  (SiatScriptVO) siatScript.toVO(0,false);
			}
            siatScript.passErrorMessages(siatScriptVO);
            
            log.debug(funcName + ": exit");
            return siatScriptVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	       
	
	public SiatScriptAdapter imprimirSiatScript(UserContext userContext, SiatScriptAdapter siatScriptAdapterVO ) throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			SiatScript siatScript = SiatScript.getById(siatScriptAdapterVO.getSiatScript().getId());

			DefDAOFactory.getSiatScriptDAO().imprimirGenerico(siatScript, siatScriptAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return siatScriptAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}

	private void copyFromVO(SiatScript siatScript, SiatScriptVO siatScriptVO) {
	
		siatScript.setCodigo(siatScriptVO.getCodigo());
		siatScript.setDescripcion(siatScriptVO.getDescripcion());
		siatScript.setPath(siatScriptVO.getPath());
		siatScript.setUsuarioUltMdf(siatScriptVO.getUsuario());
		siatScript.setFechaUltMdf(siatScriptVO.getFechaUltMdf());		
		siatScript.setEstado(Estado.ACTIVO.getId());
		
	}

	public SiatScriptVO desactivarSiatScript(UserContext userContext,SiatScriptVO siatScriptVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
	      
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            SiatScript siatScript = SiatScript.getById(siatScriptVO.getId());
                           
            siatScript.desactivar();

            if (siatScript.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				siatScriptVO =  (SiatScriptVO) siatScript.toVO(0,false);
			}
            siatScript.passErrorMessages(siatScriptVO);
            
            log.debug(funcName + ": exit");
            return siatScriptVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	// <--- ABM SiatScript
	
		
	
	// ---> ABM SiatScriptUsr
	
	public SiatScriptUsrAdapter getSiatScriptUsrAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			SiatScriptUsr siatScriptUsr = SiatScriptUsr.getById(commonKey.getId());

	        SiatScriptUsrAdapter siatScriptUsrAdapter = new SiatScriptUsrAdapter();
	        siatScriptUsrAdapter.setSiatScriptUsr((SiatScriptUsrVO) siatScriptUsr.toVO(1));
			
			log.debug(funcName + ": exit");
			return siatScriptUsrAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SiatScriptUsrAdapter getSiatScriptUsrAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
	
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			SiatScriptUsrAdapter siatScriptUsrAdapter = new SiatScriptUsrAdapter();
			
			SiatScript siatScript = SiatScript.getById(commonKey.getId());			
			SiatScriptUsrVO siatScriptUsrVO = new SiatScriptUsrVO();
			
			siatScriptUsrVO.setSiatScript((SiatScriptVO)siatScript.toVO(1));
			siatScriptUsrAdapter.setSiatScriptUsr(siatScriptUsrVO);		
									
			// Seteo la listas para el combo de Proceso			
			List<Proceso> listProceso = Proceso.getListActivos();
			siatScriptUsrAdapter.setListProceso(
					(ArrayList<ProcesoVO>)ListUtilBean.toVO(listProceso, 
					new ProcesoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Seteo la listas para el combo de Usuarios Siat		
			List<UsuarioSiat> listUsuarioSiat = UsuarioSiat.getListActivos();
			siatScriptUsrAdapter.setListUsuarioSiat(
					(ArrayList<UsuarioSiatVO>)ListUtilBean.toVO(listUsuarioSiat, 
					new UsuarioSiatVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return siatScriptUsrAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public SiatScriptUsrAdapter getSiatScriptUsrAdapterParam(UserContext userContext, SiatScriptUsrAdapter siatScriptUsrAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			siatScriptUsrAdapter.clearError();
			
			// Logica del param						
			
			log.debug(funcName + ": exit");
			return siatScriptUsrAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public SiatScriptUsrAdapter getSiatScriptUsrAdapterForUpdate(UserContext userContext, CommonKey commonKeySiatScriptUsr) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			SiatScriptUsr siatScriptUsr = SiatScriptUsr.getById(commonKeySiatScriptUsr.getId());
			
	        SiatScriptUsrAdapter siatScriptUsrAdapter = new SiatScriptUsrAdapter();
	        siatScriptUsrAdapter.setSiatScriptUsr((SiatScriptUsrVO) siatScriptUsr.toVO(1));
		
			// Seteo la listas para el combo de Proceso			
			List<Proceso> listProceso = Proceso.getListActivos();
			siatScriptUsrAdapter.setListProceso(
					(ArrayList<ProcesoVO>)ListUtilBean.toVO(listProceso, 
					new ProcesoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Seteo la listas para el combo de Usuarios Siat		
			List<UsuarioSiat> listUsuarioSiat = UsuarioSiat.getListActivos();
			siatScriptUsrAdapter.setListUsuarioSiat(
					(ArrayList<UsuarioSiatVO>)ListUtilBean.toVO(listUsuarioSiat, 
					new UsuarioSiatVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return siatScriptUsrAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SiatScriptUsrVO createSiatScriptUsr(UserContext userContext, SiatScriptUsrVO siatScriptUsrVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			siatScriptUsrVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            SiatScriptUsr siatScriptUsr = new SiatScriptUsr();           
          
            //Obtengo el Proceso
			Proceso proceso = null;
			if (siatScriptUsrVO.getProceso().getId() != -1) {
				proceso = Proceso.getById(siatScriptUsrVO.getProceso().getId());
			}
			//Obtengo el Script
			SiatScript siatScript = SiatScript.getById(siatScriptUsrVO.getSiatScript().getId());
	
			//Obtengo el Siat User
			UsuarioSiat usuarioSiat = null;
			if (siatScriptUsrVO.getUsuarioSiat().getId()!=-1) {
				usuarioSiat = UsuarioSiat.getById(siatScriptUsrVO.getUsuarioSiat().getId());
			}
			
            siatScriptUsr.setProceso(proceso);
            siatScriptUsr.setSiatScript(siatScript);
            siatScriptUsr.setUsuarioSiat(usuarioSiat);
            siatScriptUsr.setUsuarioUltMdf(siatScriptUsrVO.getUsuario());
            siatScriptUsr.setFechaUltMdf(siatScriptUsrVO.getFechaUltMdf());
                                
            siatScriptUsr.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            siatScriptUsr = DefConfiguracionManager.getInstance().createSiatScriptUsr(siatScriptUsr);
            
            if (siatScriptUsr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				siatScriptUsrVO =  (SiatScriptUsrVO) siatScriptUsr.toVO(0,false);
			}
			siatScriptUsr.passErrorMessages(siatScriptUsrVO);
            
            log.debug(funcName + ": exit");
            return siatScriptUsrVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SiatScriptUsrVO updateSiatScriptUsr(UserContext userContext, SiatScriptUsrVO siatScriptUsrVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			siatScriptUsrVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            SiatScriptUsr siatScriptUsr = SiatScriptUsr.getById(siatScriptUsrVO.getId());
			
			if(!siatScriptUsrVO.validateVersion(siatScriptUsr.getFechaUltMdf())) return siatScriptUsrVO;
			
            //Obtengo el Proceso
			Proceso proceso = Proceso.getById(siatScriptUsrVO.getProceso().getId());
			//Obtengo el Script
			SiatScript siatScript = SiatScript.getById(siatScriptUsrVO.getSiatScript().getId());
			//Obtengo el Siat User
			UsuarioSiat usuarioSiat = UsuarioSiat.getById(siatScriptUsrVO.getUsuarioSiat().getId());
			
            siatScriptUsr.setProceso(proceso);
            siatScriptUsr.setSiatScript(siatScript);
            siatScriptUsr.setUsuarioSiat(usuarioSiat);
            siatScriptUsr.setUsuarioUltMdf(siatScriptUsrVO.getUsuario());
            siatScriptUsr.setFechaUltMdf(siatScriptUsrVO.getFechaUltMdf());
                                
            siatScriptUsr.setEstado(Estado.ACTIVO.getId());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            siatScriptUsr = DefConfiguracionManager.getInstance().updateSiatScriptUsr(siatScriptUsr);
            
            if (siatScriptUsr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				siatScriptUsrVO =  (SiatScriptUsrVO) siatScriptUsr.toVO(0,false);
			}
			siatScriptUsr.passErrorMessages(siatScriptUsrVO);
            
            log.debug(funcName + ": exit");
            return siatScriptUsrVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SiatScriptUsrVO deleteSiatScriptUsr(UserContext userContext, SiatScriptUsrVO siatScriptUsrVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			siatScriptUsrVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			SiatScriptUsr siatScriptUsr = SiatScriptUsr.getById(siatScriptUsrVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			siatScriptUsr = DefConfiguracionManager.getInstance().deleteSiatScriptUsr(siatScriptUsr);
			
			if (siatScriptUsr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				siatScriptUsrVO =  (SiatScriptUsrVO) siatScriptUsr.toVO(0,false);
			}
			siatScriptUsr.passErrorMessages(siatScriptUsrVO);
            
            log.debug(funcName + ": exit");
            return siatScriptUsrVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SiatScriptUsrVO activarSiatScriptUsr(UserContext userContext, SiatScriptUsrVO siatScriptUsrVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            SiatScriptUsr siatScriptUsr = SiatScriptUsr.getById(siatScriptUsrVO.getId());

            siatScriptUsr.activar();

            if (siatScriptUsr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				siatScriptUsrVO =  (SiatScriptUsrVO) siatScriptUsr.toVO(0,false);
			}
            siatScriptUsr.passErrorMessages(siatScriptUsrVO);
            
            log.debug(funcName + ": exit");
            return siatScriptUsrVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SiatScriptUsrVO desactivarSiatScriptUsr(UserContext userContext, SiatScriptUsrVO siatScriptUsrVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		      
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            SiatScriptUsr siatScriptUsr = SiatScriptUsr.getById(siatScriptUsrVO.getId());
                           
            siatScriptUsr.desactivar();

            if (siatScriptUsr.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				siatScriptUsrVO =  (SiatScriptUsrVO) siatScriptUsr.toVO(0,false);
			}
            siatScriptUsr.passErrorMessages(siatScriptUsrVO);
            
            log.debug(funcName + ": exit");
            return siatScriptUsrVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	       
	
	public SiatScriptUsrAdapter imprimirSiatScriptUsr(UserContext userContext, SiatScriptUsrAdapter siatScriptUsrAdapterVO ) throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			SiatScriptUsr siatScriptUsr = SiatScriptUsr.getById(siatScriptUsrAdapterVO.getSiatScriptUsr().getId());

			DefDAOFactory.getSiatScriptUsrDAO().imprimirGenerico(siatScriptUsr, siatScriptUsrAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return siatScriptUsrAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}


	
	
	// <--- ABM SiatScriptUsr
}

