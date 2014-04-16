//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.Seccion;
import ar.gov.rosario.siat.def.buss.bean.Zona;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.SeccionVO;
import ar.gov.rosario.siat.def.iface.model.ZonaVO;
import ar.gov.rosario.siat.pad.buss.bean.BroCue;
import ar.gov.rosario.siat.pad.buss.bean.Broche;
import ar.gov.rosario.siat.pad.buss.bean.Calle;
import ar.gov.rosario.siat.pad.buss.bean.CriRepCalle;
import ar.gov.rosario.siat.pad.buss.bean.CriRepCat;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.PadCuentaManager;
import ar.gov.rosario.siat.pad.buss.bean.PadDistribucionManager;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.buss.bean.Repartidor;
import ar.gov.rosario.siat.pad.buss.bean.TipoBroche;
import ar.gov.rosario.siat.pad.buss.bean.TipoRepartidor;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.BroCueAdapter;
import ar.gov.rosario.siat.pad.iface.model.BroCueSearchPage;
import ar.gov.rosario.siat.pad.iface.model.BroCueVO;
import ar.gov.rosario.siat.pad.iface.model.BrocheAdapter;
import ar.gov.rosario.siat.pad.iface.model.BrocheSearchPage;
import ar.gov.rosario.siat.pad.iface.model.BrocheVO;
import ar.gov.rosario.siat.pad.iface.model.CalleVO;
import ar.gov.rosario.siat.pad.iface.model.CriRepCalleAdapter;
import ar.gov.rosario.siat.pad.iface.model.CriRepCalleVO;
import ar.gov.rosario.siat.pad.iface.model.CriRepCatAdapter;
import ar.gov.rosario.siat.pad.iface.model.CriRepCatVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.RepartidorAdapter;
import ar.gov.rosario.siat.pad.iface.model.RepartidorSearchPage;
import ar.gov.rosario.siat.pad.iface.model.RepartidorVO;
import ar.gov.rosario.siat.pad.iface.model.TipoBrocheVO;
import ar.gov.rosario.siat.pad.iface.model.TipoRepartidorVO;
import ar.gov.rosario.siat.pad.iface.service.IPadDistribucionService;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

/**
 * Implementacion de servicios del submodulo Distribucion del modulo Padron
 * @author tecso
 */
public class PadDistribucionServiceHbmImpl implements IPadDistribucionService {

	private Logger log = Logger.getLogger(PadDistribucionServiceHbmImpl.class);
	
	public RepartidorVO activarRepartidor(UserContext userContext,
			RepartidorVO repartidorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Repartidor repartidor = Repartidor.getById(repartidorVO.getId());

			repartidor.activar();

            if (repartidor.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				repartidorVO =  (RepartidorVO) repartidor.toVO();
			}
            repartidor.passErrorMessages(repartidorVO);
            
            log.debug(funcName + ": exit");
            return repartidorVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CriRepCalleVO createCriRepCalle(UserContext userContext,
			CriRepCalleVO criRepCalleVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			criRepCalleVO.clearErrorMessages();
			
            CriRepCalle criRepCalle = new CriRepCalle();
            
            Zona zona = Zona.getByIdNull(criRepCalleVO.getZona().getId());
            criRepCalle.setZona(zona);
            Calle calle = null;
            if(criRepCalleVO.getCalle()!=null && criRepCalleVO.getCalle().getId()!=null && criRepCalleVO.getCalle().getId().intValue() >0){
            	calle = Calle.getByIdNull(criRepCalleVO.getCalle().getId());
            	calle.setCodCalle(calle.getId());
            }
            criRepCalle.setCalle(calle);
            criRepCalle.setNroDesde(criRepCalleVO.getNroDesde());
            criRepCalle.setNroHasta(criRepCalleVO.getNroHasta());
            criRepCalle.setFechaDesde(criRepCalleVO.getFechaDesde());
            criRepCalle.setFechaHasta(criRepCalleVO.getFechaHasta());           

            criRepCalle.setEstado(Estado.ACTIVO.getId());
            
            // es requerido y no opcional
            Repartidor repartidor = Repartidor.getById(criRepCalleVO.getRepartidor().getId());
            criRepCalle.setRepartidor(repartidor);

            repartidor.createCriRepCalle(criRepCalle);
            
            // TODO Buscar si existe un registro para la misma calle, zona origen e intervalo de altura con fechaHasta
            // null, y modificarla con el valor fechadesde del nuevo registro.
            
            if (criRepCalle.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				criRepCalleVO =  (CriRepCalleVO) criRepCalle.toVO(0);
			}
            criRepCalle.passErrorMessages(criRepCalleVO);
            
            log.debug(funcName + ": exit");
            return criRepCalleVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CriRepCatVO createCriRepCat(UserContext userContext,
			CriRepCatVO criRepCatVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			criRepCatVO.clearErrorMessages();
			
            CriRepCat criRepCat = new CriRepCat();
            
            Seccion seccion = Seccion.getByIdNull(criRepCatVO.getSeccion().getId());
            criRepCat.setSeccion(seccion);
            criRepCat.setCatastralDesde(criRepCatVO.getCatastralDesde());
            criRepCat.setCatastralHasta(criRepCatVO.getCatastralHasta());
            criRepCat.setFechaDesde(criRepCatVO.getFechaDesde());
            criRepCat.setFechaHasta(criRepCatVO.getFechaHasta());           

            criRepCat.setEstado(Estado.ACTIVO.getId());
            
            // es requerido y no opcional
            Repartidor repartidor = Repartidor.getById(criRepCatVO.getRepartidor().getId());
            criRepCat.setRepartidor(repartidor);

            repartidor.createCriRepCat(criRepCat);
            
            // TODO Buscar si existe un registro para la misma seccion y e intervalo de manzana con fecha Hasta null
            // y modificarla con el valor fechadesde del nuevo registro.
            
            if (criRepCat.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				criRepCatVO =  (CriRepCatVO) criRepCat.toVO(0);
			}
            criRepCat.passErrorMessages(criRepCatVO);
            
            log.debug(funcName + ": exit");
            return criRepCatVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RepartidorVO createRepartidor(UserContext userContext,
			RepartidorVO repartidorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			repartidorVO.clearErrorMessages();

			Repartidor repartidor = new Repartidor();
        
			repartidor.setNroRepartidor(repartidorVO.getNroRepartidor());
			repartidor.setDesRepartidor(repartidorVO.getDesRepartidor());
			Recurso recurso = Recurso.getByIdNull(repartidorVO.getRecurso().getId());
			repartidor.setRecurso(recurso);
			Persona persona = null;
			if(!ModelUtil.isNullOrEmpty(repartidorVO.getPersona()))
				persona = Persona.getByIdNull(repartidorVO.getPersona().getId());
			repartidor.setPersona(persona);
			repartidor.setLegajo(repartidorVO.getLegajo());
			Zona zona = null;
			if(!ModelUtil.isNullOrEmpty(repartidorVO.getZona()))
				zona = Zona.getByIdNull(repartidorVO.getZona().getId());
			repartidor.setZona(zona);
			TipoRepartidor tipoRepartidor = TipoRepartidor.getByIdNull(repartidorVO.getTipoRepartidor().getId());
			repartidor.setTipoRepartidor(tipoRepartidor);
			Broche broche = null;
			if(!ModelUtil.isNullOrEmpty(repartidorVO.getBroche()))
				broche = Broche.getByIdNull(repartidorVO.getBroche().getId());
			repartidor.setBroche(broche);
			
			
            repartidor.setEstado(Estado.CREADO.getId());
      
            PadDistribucionManager.getInstance().createRepartidor(repartidor); 
      
            if (repartidor.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				repartidorVO =  (RepartidorVO) repartidor.toVOSpecific(true);
			}
            repartidor.passErrorMessages(repartidorVO);
            
            log.debug(funcName + ": exit");
            return repartidorVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public CriRepCalleVO deleteCriRepCalle(UserContext userContext,
			CriRepCalleVO criRepCalleVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			criRepCalleVO.clearErrorMessages();
			
            CriRepCalle criRepCalle = CriRepCalle.getById(criRepCalleVO.getId());
            
            criRepCalle.getRepartidor().deleteCriRepCalle(criRepCalle);
            
            if (criRepCalle.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				criRepCalleVO = (CriRepCalleVO) criRepCalle.toVO();
			}
            criRepCalle.passErrorMessages(criRepCalleVO);
            
            log.debug(funcName + ": exit");
            return criRepCalleVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CriRepCatVO deleteCriRepCat(UserContext userContext,
			CriRepCatVO criRepCatVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			criRepCatVO.clearErrorMessages();
			
            CriRepCat criRepCat = CriRepCat.getById(criRepCatVO.getId());
            
            criRepCat.getRepartidor().deleteCriRepCat(criRepCat);
            
            if (criRepCat.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				criRepCatVO = (CriRepCatVO) criRepCat.toVO();
			}
            criRepCat.passErrorMessages(criRepCatVO);
            
            log.debug(funcName + ": exit");
            return criRepCatVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RepartidorVO deleteRepartidor(UserContext userContext,
			RepartidorVO repartidorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Repartidor repartidor = Repartidor.getById(repartidorVO.getId());

			PadDistribucionManager.getInstance().deleteRepartidor(repartidor);

            if (repartidor.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				repartidorVO =  (RepartidorVO) repartidor.toVO();
			}
            repartidor.passErrorMessages(repartidorVO);
            
            log.debug(funcName + ": exit");
            return repartidorVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RepartidorVO desactivarRepartidor(UserContext userContext,
			RepartidorVO repartidorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Repartidor repartidor = Repartidor.getById(repartidorVO.getId());

			repartidor.desactivar();

            if (repartidor.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				repartidorVO =  (RepartidorVO) repartidor.toVO();
			}
            repartidor.passErrorMessages(repartidorVO);
            
            log.debug(funcName + ": exit");
            return repartidorVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CriRepCalleAdapter getCriRepCalleAdapterForCreate(UserContext userContext, CommonKey repartidorCommonKey) 
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			CriRepCalleAdapter criRepCalleAdapter = new CriRepCalleAdapter();
			Repartidor repartidor = Repartidor.getById(repartidorCommonKey.getId());
			//repartidor.setPersona(Persona.getByIdLight(repartidor.getPersona().getId()));
			CriRepCalleVO criRepCalleVO = new CriRepCalleVO();
			criRepCalleVO.setRepartidor((RepartidorVO) repartidor.toVO(1));
			criRepCalleAdapter.setCriRepCalle(criRepCalleVO);
			
			criRepCalleAdapter.setListZona( (ArrayList<ZonaVO>)
					ListUtilBean.toVO(Zona.getListActivos(),
					new ZonaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return criRepCalleAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CriRepCalleAdapter getCriRepCalleAdapterForUpdate(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CriRepCalle criRepCalle = CriRepCalle.getById(commonKey.getId());
			Calle calle = Calle.getByIdNull(criRepCalle.getCalle().getCodCalle());
			calle.setCodCalle(calle.getId());
			criRepCalle.setCalle(calle);
			//criRepCalle.getRepartidor().setPersona(Persona.getByIdLight(criRepCalle.getRepartidor().getPersona().getId()));
			
	        CriRepCalleAdapter criRepCalleAdapter = new CriRepCalleAdapter();
	        criRepCalleAdapter.setCriRepCalle((CriRepCalleVO) criRepCalle.toVO(2));
			
			criRepCalleAdapter.setListZona( (ArrayList<ZonaVO>)
					ListUtilBean.toVO(Zona.getListActivos(),
					new ZonaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	        
			log.debug(funcName + ": exit");
			return criRepCalleAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CriRepCalleAdapter getCriRepCalleAdapterForView(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CriRepCalle criRepCalle = CriRepCalle.getById(commonKey.getId());
			criRepCalle.setCalle(Calle.getByIdNull(criRepCalle.getCalle().getCodCalle()));
			//criRepCalle.getRepartidor().setPersona(Persona.getByIdLight(criRepCalle.getRepartidor().getPersona().getId()));
			
	        CriRepCalleAdapter criRepCalleAdapter = new CriRepCalleAdapter();
	        criRepCalleAdapter.setCriRepCalle((CriRepCalleVO) criRepCalle.toVO(2));
			
			log.debug(funcName + ": exit");
			return criRepCalleAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CriRepCatAdapter getCriRepCatAdapterForCreate(UserContext userContext, CommonKey repartidorCommonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

			CriRepCatAdapter criRepCatAdapter = new CriRepCatAdapter();
			Repartidor repartidor = Repartidor.getById(repartidorCommonKey.getId());
		//	repartidor.setPersona(Persona.getByIdLight(repartidor.getPersona().getId()));
			CriRepCatVO criRepCatVO = new CriRepCatVO();
			criRepCatVO.setRepartidor((RepartidorVO) repartidor.toVO(1));
			criRepCatAdapter.setCriRepCat(criRepCatVO);
			
			criRepCatAdapter.setListSeccion( (ArrayList<SeccionVO>)
					ListUtilBean.toVO(Seccion.getListActivosOrder(),
					new SeccionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return criRepCatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CriRepCatAdapter getCriRepCatAdapterForUpdate(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CriRepCat criRepCat = CriRepCat.getById(commonKey.getId());
			//criRepCat.getRepartidor().setPersona(Persona.getByIdLight(criRepCat.getRepartidor().getPersona().getId()));
	        
			CriRepCatAdapter criRepCatAdapter = new CriRepCatAdapter();
	        criRepCatAdapter.setCriRepCat((CriRepCatVO) criRepCat.toVO(2));
			
			criRepCatAdapter.setListSeccion( (ArrayList<SeccionVO>)
					ListUtilBean.toVO(Seccion.getListActivosOrder(),
					new SeccionVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	 
			log.debug(funcName + ": exit");
			return criRepCatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public CriRepCatAdapter getCriRepCatAdapterForView(UserContext userContext,
			CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			CriRepCat criRepCat = CriRepCat.getById(commonKey.getId());
			//criRepCat.getRepartidor().setPersona(Persona.getByIdLight(criRepCat.getRepartidor().getPersona().getId()));
			
	        CriRepCatAdapter criRepCatAdapter = new CriRepCatAdapter();
	        criRepCatAdapter.setCriRepCat((CriRepCatVO) criRepCat.toVO(2));
			
			log.debug(funcName + ": exit");
			return criRepCatAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public RepartidorAdapter getRepartidorAdapterForCreate(
			UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

	        RepartidorAdapter repartidorAdapter = new RepartidorAdapter();

	        repartidorAdapter.setListRecurso( (ArrayList<RecursoVO>)
					ListUtilBean.toVO(Recurso.getListActivos(),
					new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
	        
			List<TipoRepartidor> listTipoRepartidor = new ArrayList<TipoRepartidor>();			
			repartidorAdapter.setListTipoRepartidor((ArrayList<TipoRepartidorVO>)ListUtilBean.toVO(listTipoRepartidor, 
											new TipoRepartidorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
       
	    	repartidorAdapter.setListZona( (ArrayList<ZonaVO>)
					ListUtilBean.toVO(Zona.getListActivos(),
					new ZonaVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
	
	    	List<Broche> listBroche = new ArrayList<Broche>();
	    	repartidorAdapter.setListBroche( (ArrayList<BrocheVO>)
					ListUtilBean.toVO(listBroche, 1, false,
					new BrocheVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
	        	        
	        log.debug(funcName + ": exit");
			return repartidorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RepartidorAdapter getRepartidorAdapterForUpdate(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Repartidor repartidor = Repartidor.getById(commonKey.getId());
//			repartidor.setPersona(Persona.getByIdLight(repartidor.getPersona().getId())); ahora se usa el campo desRepartidor
			
			RepartidorAdapter repartidorAdapter = new RepartidorAdapter();
			repartidorAdapter.setRepartidor((RepartidorVO) repartidor.toVOSpecific(true));    
			
			repartidorAdapter.setListZona( (ArrayList<ZonaVO>)
						ListUtilBean.toVO(Zona.getListActivos(),
						new ZonaVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
		
			List<Broche> listBroche = new ArrayList<Broche>(); 
			
			// Si el tipo de Repartidor es 'Fuera de Zona'
			//if(repartidor.getTipoRepartidor().getId().longValue()==2){
			//	listBroche = Broche.getListActivosByIdTipoBrocheYIdRecurso(TipoBroche.ID_REPARTIDORES_FUERA_DE_ZONA, repartidorAdapter.getRepartidor().getRecurso().getId());
			//}
			
			// Si el tipo de Repartidor es 'de Zona'
			//if(repartidor.getTipoRepartidor().getId().longValue()==1){
			//	listBroche = Broche.getListActivosByIdTipoBrocheYIdRecurso(TipoBroche.ID_REPARTIDORES_DE_ZONA, repartidorAdapter.getRepartidor().getRecurso().getId());				
			//}
			
			listBroche = Broche.getListActivosByIdTipoBrocheYIdRecurso(null, repartidorAdapter.getRepartidor().getRecurso().getId());
			repartidorAdapter.setListBroche( (ArrayList<BrocheVO>)
					ListUtilBean.toVO(listBroche, 1, false,
					new BrocheVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));

			repartidorAdapter.setParamRecurso(1);
			
			for(CriRepCalleVO item: repartidorAdapter.getRepartidor().getListCriRepCalle()){
				Calle calle = Calle.getByIdNull(item.getCalle().getCodCalle());
				if(calle!=null)
					item.setCalle((CalleVO) calle.toVO());
			}
			
			log.debug(funcName + ": exit");
			return repartidorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public RepartidorAdapter getRepartidorAdapterForView(
			UserContext userContext, CommonKey commonKey)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Repartidor repartidor = Repartidor.getById(commonKey.getId());
			//repartidor.setPersona(Persona.getByIdLight(repartidor.getPersona().getId()));
			
			RepartidorAdapter repartidorAdapter = new RepartidorAdapter();
			repartidorAdapter.setRepartidor((RepartidorVO) repartidor.toVOSpecific(true));    
				
			log.debug(funcName + ": exit");
			return repartidorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RepartidorAdapter getRepartidorAdapterParamRecurso(
			UserContext userContext, RepartidorAdapter repartidorAdapter)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			repartidorAdapter.clearError();
			
			if (!ModelUtil.isNullOrEmpty(repartidorAdapter.getRepartidor().getRecurso())){
				
				List<TipoRepartidor> listTipoRepartidor = TipoRepartidor.getListActivosByIdRecurso(
												repartidorAdapter.getRepartidor().getRecurso().getId());
				repartidorAdapter.setListTipoRepartidor((ArrayList<TipoRepartidorVO>)ListUtilBean.toVO(listTipoRepartidor, 
												new TipoRepartidorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				// Se arma la lista de Broches Correspondientes al Tipo de Broche: 'Repartidores de Zona'
				// y 'Repartidores Fuera de Zona'. Cuyos id son 6 y 3 respectivamente.
				List<Broche> listBroche = new ArrayList<Broche>(); 
				if(repartidorAdapter.getRepartidor().getTipoRepartidor() != null && repartidorAdapter.getRepartidor().getTipoRepartidor().getId() != null){
					// Si el tipo de Repartidor es 'Fuera de Zona'
					//if(repartidorAdapter.getRepartidor().getTipoRepartidor().getId().longValue()==2){
					//	listBroche = Broche.getListActivosByIdTipoBrocheYIdRecurso(TipoBroche.ID_REPARTIDORES_FUERA_DE_ZONA, repartidorAdapter.getRepartidor().getRecurso().getId());
					//}
					// Si el tipo de Repartidor es 'de Zona'
					//if(repartidorAdapter.getRepartidor().getTipoRepartidor().getId().longValue()==1){
					//	listBroche = Broche.getListActivosByIdTipoBrocheYIdRecurso(TipoBroche.ID_REPARTIDORES_DE_ZONA, repartidorAdapter.getRepartidor().getRecurso().getId());				
					//}
					listBroche = Broche.getListActivosByIdTipoBrocheYIdRecurso(null, repartidorAdapter.getRepartidor().getRecurso().getId());				
				}
				repartidorAdapter.setListBroche( (ArrayList<BrocheVO>)					
						ListUtilBean.toVO(listBroche, 1, false,
						new BrocheVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
				repartidorAdapter.setParamRecurso(1);
			}else{
				// Seteo la lista de TipoRepartidor y la de Broche vacias y el param correspondiente en cero.
				List<TipoRepartidor> listTipoRepartidor = new ArrayList<TipoRepartidor>();			
				repartidorAdapter.setListTipoRepartidor((ArrayList<TipoRepartidorVO>)ListUtilBean.toVO(listTipoRepartidor, 
												new TipoRepartidorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				
				List<Broche> listBroche = new ArrayList<Broche>();
		    	repartidorAdapter.setListBroche( (ArrayList<BrocheVO>)
						ListUtilBean.toVO(listBroche, 1, false,
						new BrocheVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));			
				
				
				repartidorAdapter.setParamRecurso(0);
			}
			
			log.debug(funcName + ": exit");
			return repartidorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public RepartidorAdapter getRepartidorAdapterParamTipoRepartidor
			(UserContext userContext, RepartidorAdapter repartidorAdapter)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			repartidorAdapter.clearError();
			
			if (!ModelUtil.isNullOrEmpty(repartidorAdapter.getRepartidor().getRecurso())){
				// Se arma la lista de Broches Correspondientes al Tipo de Broche: 'Repartidores de Zona'
				// y 'Repartidores Fuera de Zona'. Cuyos id son 6 y 3 respectivamente.
				List<Broche> listBroche = new ArrayList<Broche>(); 
				if(repartidorAdapter.getRepartidor().getTipoRepartidor() != null && repartidorAdapter.getRepartidor().getTipoRepartidor().getId() != null){
					// Si el tipo de Repartidor es 'Fuera de Zona'
					//if(repartidorAdapter.getRepartidor().getTipoRepartidor().getId().longValue()==2){
					//	listBroche = Broche.getListActivosByIdTipoBrocheYIdRecurso(TipoBroche.ID_REPARTIDORES_FUERA_DE_ZONA, repartidorAdapter.getRepartidor().getRecurso().getId());
					//}
					// Si el tipo de Repartidor es 'de Zona'
					//if(repartidorAdapter.getRepartidor().getTipoRepartidor().getId().longValue()==1){
					//	listBroche = Broche.getListActivosByIdTipoBrocheYIdRecurso(TipoBroche.ID_REPARTIDORES_DE_ZONA, repartidorAdapter.getRepartidor().getRecurso().getId());				
					//}
					listBroche = Broche.getListActivosByIdTipoBrocheYIdRecurso(null, repartidorAdapter.getRepartidor().getRecurso().getId());				
				}
				repartidorAdapter.setListBroche( (ArrayList<BrocheVO>)					
						ListUtilBean.toVO(listBroche, 1, false,
						new BrocheVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
			}else{
				// Seteo la lista de Broche vacia.
				List<Broche> listBroche = new ArrayList<Broche>();
		    	repartidorAdapter.setListBroche( (ArrayList<BrocheVO>)
						ListUtilBean.toVO(listBroche, 1, false,
						new BrocheVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));			
			}
			
			log.debug(funcName + ": exit");
			return repartidorAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public RepartidorSearchPage getRepartidorSearchPageInit(
			UserContext userContext) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			RepartidorSearchPage repartidorSearchPage = new RepartidorSearchPage();
		
			//	Seteo la lista de recurso
			List<Recurso> listRecurso = Recurso.getListActivos();			
			repartidorSearchPage.setListRecurso((ArrayList<RecursoVO>)ListUtilBean.toVO(listRecurso, 
											new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS)));
		
			//	Seteo la lista de Tipo de Repartidor
			List<TipoRepartidor> listTipoRepartidor = new ArrayList<TipoRepartidor>();			
			repartidorSearchPage.setListTipoRepartidor((ArrayList<TipoRepartidorVO>)ListUtilBean.toVO(listTipoRepartidor, 
											new TipoRepartidorVO(-1, StringUtil.SELECT_OPCION_TODOS)));

			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return repartidorSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RepartidorSearchPage getRepartidorSearchPageParamRecurso(
			UserContext userContext, RepartidorSearchPage repartidorSearchPage)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			repartidorSearchPage.clearError();
			
			if (!ModelUtil.isNullOrEmpty(repartidorSearchPage.getRepartidor().getRecurso())){
				
				List<TipoRepartidor> listTipoRepartidor= TipoRepartidor.getListActivosByIdRecurso(
												repartidorSearchPage.getRepartidor().getRecurso().getId());
				repartidorSearchPage.setListTipoRepartidor((ArrayList<TipoRepartidorVO>)ListUtilBean.toVO(listTipoRepartidor, 
												new TipoRepartidorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				repartidorSearchPage.setParamRecurso(1);
			}else{
				// Seteo la lista de TipoRepartidor vacia y el param correspondiente en cero.
				List<TipoRepartidor> listTipoRepartidor = new ArrayList<TipoRepartidor>();			
				repartidorSearchPage.setListTipoRepartidor((ArrayList<TipoRepartidorVO>)ListUtilBean.toVO(listTipoRepartidor, 
												new TipoRepartidorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				repartidorSearchPage.setParamRecurso(0);
			}
			
			log.debug(funcName + ": exit");
			return repartidorSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public RepartidorSearchPage getRepartidorSearchPageResult(
			UserContext userContext, RepartidorSearchPage repartidorSearchPage)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			repartidorSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Repartidor> listRepartidor = PadDAOFactory.getRepartidorDAO().getListBySearchPage(repartidorSearchPage);  
		
	   		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.

	   		// Cargamos el Nombre y Apellido para los repartidores
	   		/*for(Repartidor repartidor: listRepartidor){
	   			repartidor.setPersona(Persona.getByIdLight(repartidor.getPersona().getId()));
	   		}
	   		*/ // ahora se usa el campo desRepartidor

	   		//Aqui pasamos BO a VO
	   		//repartidorSearchPage.setListResult(ListUtilBean.toVO(listRepartidor,3));
	   		repartidorSearchPage.setListResult(Repartidor.listToVOSpecific(listRepartidor));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return repartidorSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public CriRepCalleVO updateCriRepCalle(UserContext userContext,
			CriRepCalleVO criRepCalleVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			criRepCalleVO.clearErrorMessages();
			
            CriRepCalle criRepCalle = CriRepCalle.getById(criRepCalleVO.getId());
            
            if(!criRepCalleVO.validateVersion(criRepCalle.getFechaUltMdf())) return criRepCalleVO;
            
            Zona zona = Zona.getByIdNull(criRepCalleVO.getZona().getId());
            criRepCalle.setZona(zona);
            Calle calle = null;
            if(criRepCalleVO.getCalle()!=null){
            	calle = Calle.getByIdNull(criRepCalleVO.getCalle().getId());
            	calle.setCodCalle(calle.getId());
            }
            criRepCalle.setNroDesde(criRepCalleVO.getNroDesde());
            criRepCalle.setNroHasta(criRepCalleVO.getNroHasta());
            criRepCalle.setFechaDesde(criRepCalleVO.getFechaDesde());
            criRepCalle.setFechaHasta(criRepCalleVO.getFechaHasta());           
           
            criRepCalle.getRepartidor().createCriRepCalle(criRepCalle);
            
            if (criRepCalle.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				criRepCalleVO =  (CriRepCalleVO) criRepCalle.toVO(0);
			}
            criRepCalle.passErrorMessages(criRepCalleVO);
            
            log.debug(funcName + ": exit");
            return criRepCalleVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public CriRepCatVO updateCriRepCat(UserContext userContext,
			CriRepCatVO criRepCatVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			criRepCatVO.clearErrorMessages();
			
            CriRepCat criRepCat = CriRepCat.getById(criRepCatVO.getId());
            
            if(!criRepCatVO.validateVersion(criRepCat.getFechaUltMdf())) return criRepCatVO;
            
            Seccion seccion = Seccion.getByIdNull(criRepCatVO.getSeccion().getId());
            criRepCat.setSeccion(seccion);
            criRepCat.setCatastralDesde(criRepCatVO.getCatastralDesde());
            criRepCat.setCatastralHasta(criRepCatVO.getCatastralHasta());
            criRepCat.setFechaDesde(criRepCatVO.getFechaDesde());
            criRepCat.setFechaHasta(criRepCatVO.getFechaHasta());           

            criRepCat.getRepartidor().createCriRepCat(criRepCat);
            
            if (criRepCat.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				criRepCatVO =  (CriRepCatVO) criRepCat.toVO(0);
			}
            criRepCat.passErrorMessages(criRepCatVO);
            
            log.debug(funcName + ": exit");
            return criRepCatVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RepartidorVO updateRepartidor(UserContext userContext,
			RepartidorVO repartidorVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			repartidorVO.clearErrorMessages();
			
			Repartidor repartidor = Repartidor.getById(repartidorVO.getId());
	        
			if(!repartidorVO.validateVersion(repartidor.getFechaUltMdf())) return repartidorVO;
			
			repartidor.setNroRepartidor(repartidorVO.getNroRepartidor());
			repartidor.setDesRepartidor(repartidorVO.getDesRepartidor());
			Persona persona = null;
			if(!ModelUtil.isNullOrEmpty(repartidorVO.getPersona()))
				persona = Persona.getByIdNull(repartidorVO.getPersona().getId());
			repartidor.setPersona(persona);
			repartidor.setLegajo(repartidorVO.getLegajo());
			Zona zona = null;
			if(!ModelUtil.isNullOrEmpty(repartidorVO.getZona()))
				zona = Zona.getById(repartidorVO.getZona().getId());
			repartidor.setZona(zona);
			Broche broche = null;
			if(!ModelUtil.isNullOrEmpty(repartidorVO.getBroche()))
				broche = Broche.getById(repartidorVO.getBroche().getId());
			repartidor.setBroche(broche);
			              
			
            PadDistribucionManager.getInstance().updateRepartidor(repartidor); 

            if (repartidor.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				repartidorVO =  (RepartidorVO) repartidor.toVOSpecific(true);
			}
            repartidor.passErrorMessages(repartidorVO);
            
            log.debug(funcName + ": exit");
            return repartidorVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Param de Persona para Repartidor
	 * @author tecso
	 * @return RepartidorAdapter 
	 */
	public RepartidorAdapter paramPersonaRepartidor(UserContext userContext, RepartidorAdapter repartidorAdapter, Long selectedId) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			// Con el idPersona obtenger cargar la persona del facade
			
			Persona persona = Persona.getByIdLight(selectedId);
			repartidorAdapter.getRepartidor().setPersona((PersonaVO) persona.toVO());
			
			log.debug(funcName + ": exit");
			return repartidorAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Param de Calle para CriRepCalle
	 * @author tecso
	 * @return CriRepCalleAdapter 
	 */
	public CriRepCalleAdapter paramCalleCriRepCalle(UserContext userContext, CriRepCalleAdapter criRepCalleAdapter, Long selectedId) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			// Con el idCalle obtenger cargar la calle del facade
			Calle calle = Calle.getByIdNull(selectedId);
			criRepCalleAdapter.getCriRepCalle().setCalle((CalleVO) calle.toVO());
			
			log.debug(funcName + ": exit");
			return criRepCalleAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public BroCueVO createBroCue(UserContext userContext, BroCueVO broCueVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			broCueVO.clearErrorMessages();

			BroCue broCue = new BroCue();
			
			Broche broche = Broche.getById(broCueVO.getBroche().getId());

			Cuenta cuenta = null;
			if(!ModelUtil.isNullOrEmpty(broCueVO.getCuenta())){
				cuenta = Cuenta.getByIdNull(broCueVO.getCuenta().getId());				
			} else{
				if(broCueVO != null && !StringUtil.isNullOrEmpty(broCueVO.getCuenta().getNumeroCuenta()))
					cuenta = Cuenta.getByIdRecursoYNumeroCuenta(broche.getRecurso().getId(), broCueVO.getCuenta().getNumeroCuenta());
			}
			
			if(cuenta!=null){
				
				// 1) Se setena los valores para que el infoString devuelva datos correctos
				broCue = cuenta.asignarBroche(broche, broCueVO.getFechaAlta(), broCueVO.getIdCaso());
				
				// 2) Registro uso de expediente 
	        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_ASOCIAR_BROCHE_A_CUENTA); 
	        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(broCueVO, broCue, 
	        			accionExp, broCue.getCuenta(), broCue.infoString() );
	        	// Si no pasa la validacion, vuelve a la vista. 
	        	if (broCueVO.hasError()){
	        		tx.rollback();
	        		return broCueVO;
	        	}
								
			}else {
				broCue.addRecoverableError(PadError.CUENTA_NO_NUMERO_CUENTA_PARA_RECURSO);				
			}
			      
            if (broCue.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				broCueVO =  (BroCueVO) broCue.toVO(2);
			}
            broCue.passErrorMessages(broCueVO);
            
            log.debug(funcName + ": exit");
            return broCueVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public BrocheVO createBroche(UserContext userContext, BrocheVO brocheVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			brocheVO.clearErrorMessages();

			Broche broche = new Broche();
        
			broche.setDesBroche(brocheVO.getDesBroche());
			Recurso recurso = Recurso.getByIdNull(brocheVO.getRecurso().getId());
			broche.setRecurso(recurso);
			TipoBroche tipoBroche = TipoBroche.getByIdNull(brocheVO.getTipoBroche().getId());
			broche.setTipoBroche(tipoBroche);
			broche.setStrDomicilioEnvio(brocheVO.getStrDomicilioEnvio());
			broche.setTelefono(brocheVO.getTelefono());
			broche.setExentoEnvioJud(brocheVO.getExentoEnvioJud().getBussId());
			broche.setPermiteImpresion(brocheVO.getPermiteImpresion().getBussId());
			
			Repartidor repartidor = null;
			if(!ModelUtil.isNullOrEmpty(brocheVO.getRepartidor()))
				repartidor = Repartidor.getByIdNull(brocheVO.getRepartidor().getId());
			broche.setRepartidor(repartidor);
			
            broche.setEstado(Estado.ACTIVO.getId());
      
            PadDistribucionManager.getInstance().createBroche(broche); 
      
            if (broche.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				brocheVO =  (BrocheVO) broche.toVO(0,false);
			}
            
            broche.passErrorMessages(brocheVO);
            
            log.debug(funcName + ": exit");
            return brocheVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public BroCueVO deleteBroCue(UserContext userContext, BroCueVO broCueVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			BroCue broCue = BroCue.getById(broCueVO.getId());

			broCue.getBroche().deleteBroCue(broCue);

            if (broCue.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				broCueVO =  (BroCueVO) broCue.toVO();
			}
            broCue.passErrorMessages(broCueVO);
            
            log.debug(funcName + ": exit");
            return broCueVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public BrocheVO deleteBroche(UserContext userContext, BrocheVO brocheVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Broche broche = Broche.getById(brocheVO.getId());

			PadDistribucionManager.getInstance().deleteBroche(broche);

            if (broche.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				brocheVO =  (BrocheVO) broche.toVO(0,false);
			}
            broche.passErrorMessages(brocheVO);
            
            log.debug(funcName + ": exit");
            return brocheVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public BroCueAdapter getBroCueAdapterForCreate(UserContext userContext, CommonKey brocheCommonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

	        BroCueAdapter broCueAdapter = new BroCueAdapter();

	        Broche broche = Broche.getById(brocheCommonKey.getId());
	        BroCueVO broCue = new BroCueVO();
	        broCue.setBroche(broche.toVOSpecific(false));
	        broCueAdapter.setBroCue(broCue);
	        
	        log.debug(funcName + ": exit");
			return broCueAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public BroCueAdapter getBroCueAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			BroCue broCue = BroCue.getById(commonKey.getId());
			
			/*if(broCue.getBroche().getRepartidor()!=null)
				broCue.getBroche().getRepartidor().setPersona(Persona.getByIdLight(broCue.getBroche().getRepartidor().getPersona().getId()));
			*/
			
			BroCueAdapter broCueAdapter = new BroCueAdapter();
			broCueAdapter.setBroCue((BroCueVO) broCue.toVO(0));    
			broCueAdapter.getBroCue().setBroche(broCue.getBroche().toVOSpecific(false));
			broCueAdapter.getBroCue().setCuenta((CuentaVO) broCue.getCuenta().toVO(1));

			// Inicializo el repartidor si el broche no tenia ninguno asignado. 
			if(broCue.getBroche().getRepartidor()==null)
				broCueAdapter.getBroCue().getBroche().setRepartidor(new RepartidorVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
			
			if (broCue.getBroche().getTipoBroche()!=null){
				if(broCue.getBroche().getTipoBroche().getId().longValue() == 1){
					broCueAdapter.setParamTipoBroche(1);
				} else if(broCue.getBroche().getTipoBroche().getId().longValue() == 3 || broCue.getBroche().getTipoBroche().getId().longValue() == 6){  
					broCueAdapter.setParamTipoBroche(2);
				}else{
					broCueAdapter.setParamTipoBroche(0);		
				}

			}else{
				broCueAdapter.setParamTipoBroche(0);
			}
			
			log.debug(funcName + ": exit");
			return broCueAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	@SuppressWarnings("unchecked")
	public BrocheAdapter getBrocheAdapterForCreate(UserContext userContext) 
		throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 

	        BrocheAdapter brocheAdapter = new BrocheAdapter();

	        List<Recurso> listRecurso = Recurso.getListActivos();
	        
			// Por defecto seteamos la opcion TODOS
	        brocheAdapter.getBroche().getRecurso().setId(-1L);
			
	        brocheAdapter.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso) {				
				brocheAdapter.getListRecurso().add(item.toVOWithCategoria());							
			}

			brocheAdapter.setListTipoBroche((ArrayList<TipoBrocheVO>) 
					ListUtilBean.toVO(TipoBroche.getListActivos(), 0, false,
					new TipoBrocheVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
       
			// Inicializo el repartidor. (no se inicializo desde el VO, porque genera un ciclo con Repartidor)
			brocheAdapter.getBroche().setRepartidor(new RepartidorVO());
			
			// Repartidor de Tipo de Repartidor 'Fuera de Zona' de Id:2
	       	List<Repartidor> listRepartidor = Repartidor.getListActivosByIdTipoRepartidor(TipoRepartidor.ID_TGI_FUERA_DE_ZONA_);
	       	
			brocheAdapter.setListRepartidor((ArrayList<RepartidorVO>)
					ListUtilBean.toVO(listRepartidor, 1, false, 
					new RepartidorVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
   	  
			// lista de SiNo
			brocheAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));

			log.debug(funcName + ": exit");
			return brocheAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public BrocheAdapter getBrocheAdapterForUpdate(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Broche broche = Broche.getById(commonKey.getId());
			
			BrocheAdapter brocheAdapter = new BrocheAdapter();
    		brocheAdapter.setBroche((BrocheVO) broche.toVOSpecific(false));
			
			// Inicializo el repartidor si no tenia ninguno asignado. 
			if (broche.getRepartidor() == null) {
				brocheAdapter.getBroche().setRepartidor(new RepartidorVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
			}

			// Repartidor de Tipo de Repartidor 'Fuera de Zona' de Id:2
	       	List<Repartidor> listRepartidor = Repartidor.getListActivosByIdTipoRepartidor(TipoRepartidor.ID_TGI_FUERA_DE_ZONA_);
	       	
			brocheAdapter.setListRepartidor((ArrayList<RepartidorVO>)
					ListUtilBean.toVO(listRepartidor, 1, false,
					new RepartidorVO(-1, StringUtil.SELECT_OPCION_NINGUNO)));
   	  			   	  
			// lista de SiNo
			brocheAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));

			if (!ModelUtil.isNullOrEmpty(brocheAdapter.getBroche().getTipoBroche())){
				if(brocheAdapter.getBroche().getTipoBroche().getId().longValue() == 1){
					brocheAdapter.setParamTipoBroche(1);
				} else if(brocheAdapter.getBroche().getTipoBroche().getId().longValue() == 3 || brocheAdapter.getBroche().getTipoBroche().getId().longValue() == 6){  
					brocheAdapter.setParamTipoBroche(2);
				}else{
					brocheAdapter.setParamTipoBroche(0);		
				}
			}else{
				brocheAdapter.setParamTipoBroche(0);
			}
			
			log.debug(funcName + ": exit");
			return brocheAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public BrocheAdapter getBrocheAdapterForView(UserContext userContext, CommonKey commonKey) 
		throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Broche broche = Broche.getById(commonKey.getId());
			BrocheAdapter brocheAdapter = new BrocheAdapter();
			brocheAdapter.setBroche((BrocheVO) broche.toVOSpecific(false));
			
			// lista de SiNo
			brocheAdapter.setListSiNo(SiNo.getList(SiNo.OpcionSelecionar));

			// Inicializo el repartidor si no tenia ninguno asignado. 
			if (broche.getRepartidor() == null) {
				brocheAdapter.getBroche().setRepartidor(new RepartidorVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
			}
			
			if (!ModelUtil.isNullOrEmpty(brocheAdapter.getBroche().getTipoBroche())){
				if(brocheAdapter.getBroche().getTipoBroche().getId().longValue() == 1){
					brocheAdapter.setParamTipoBroche(1);
				} else if(brocheAdapter.getBroche().getTipoBroche().getId().longValue() == 3 || brocheAdapter.getBroche().getTipoBroche().getId().longValue() == 6){  
					brocheAdapter.setParamTipoBroche(2);
				}else{
					brocheAdapter.setParamTipoBroche(0);		
				}

			}else{
				brocheAdapter.setParamTipoBroche(0);
			}
			
			log.debug(funcName + ": exit");
			return brocheAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public BrocheSearchPage getBrocheSearchPageInit(UserContext userContext, RecursoVO recursoReadOnly) 
		throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			BrocheSearchPage brocheSearchPage = new BrocheSearchPage();
			//	Seteo la lista de recurso
			if (ModelUtil.isNullOrEmpty(recursoReadOnly)) {
				List<Recurso> listRecurso = Recurso.getListActivos();

				// Por defecto seteamos la opcion TODOS
				brocheSearchPage.getBroche().getRecurso().setId(-1L);
				
				brocheSearchPage.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
				for (Recurso item: listRecurso) {				
					brocheSearchPage.getListRecurso().add(item.toVOWithCategoria());							
				}

			} else {
				List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
				listRecurso.add(recursoReadOnly);
				brocheSearchPage.setListRecurso(listRecurso);				
			}

			//	Seteo la lista de Tipo de Broche
			List<TipoBroche> listTipoBroche = TipoBroche.getListActivos();			
			brocheSearchPage.setListTipoBroche((ArrayList<TipoBrocheVO>)ListUtilBean.toVO(listTipoBroche, 0,false, 
												new TipoBrocheVO(-1, StringUtil.SELECT_OPCION_TODOS)));

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return brocheSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public BrocheSearchPage getBrocheSearchPageResult(UserContext userContext, BrocheSearchPage brocheSearchPage) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			brocheSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<Broche> listBroche = PadDAOFactory.getBrocheDAO().getListBySearchPage(brocheSearchPage);  
		
	   		// Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
	   		//Aqui pasamos BO a VO
	   		List<BrocheVO> listBrocheVO = (ArrayList<BrocheVO>) Broche.listToVOSpecific(listBroche);

	   		brocheSearchPage.setListResult(listBrocheVO);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return brocheSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public BroCueVO updateBroCue(UserContext userContext, BroCueVO broCueVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			broCueVO.clearErrorMessages();
			
			BroCue broCue = BroCue.getById(broCueVO.getId());
			
			if(!broCueVO.validateVersion(broCue.getFechaUltMdf())) return broCueVO;
			
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_ASOCIAR_BROCHE_A_CUENTA); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(broCueVO, broCue, 
        			accionExp, broCue.getCuenta(), broCue.infoString());
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (broCueVO.hasError()){
        		tx.rollback();
        		return broCueVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	broCue.setIdCaso(broCueVO.getIdCaso());
        	        	
			broCue.setFechaBaja(broCueVO.getFechaBaja());
			              
            broCue.getBroche().updateBroCue(broCue);
            
            // Luego de modificar el registro. Si la fecha de baja es distinta de null. Nulleo la referencia a Broche en la Cuenta.
            if(!broCue.hasError()){
            	broCue.getCuenta().setBroche(null);
            	PadCuentaManager.getInstance().updateCuenta(broCue.getCuenta());
            }

            if (broCue.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				broCueVO =  (BroCueVO) broCue.toVO(2);
			}
            broCue.passErrorMessages(broCueVO);
            
            log.debug(funcName + ": exit");
            return broCueVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public BrocheVO updateBroche(UserContext userContext, BrocheVO brocheVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			brocheVO.clearErrorMessages();
			
			Broche broche = Broche.getById(brocheVO.getId());
			
			if(!brocheVO.validateVersion(broche.getFechaUltMdf())) return brocheVO;
			
			broche.setDesBroche(brocheVO.getDesBroche());
			broche.setStrDomicilioEnvio(brocheVO.getStrDomicilioEnvio());
			broche.setTelefono(brocheVO.getTelefono());
			
			broche.setExentoEnvioJud(brocheVO.getExentoEnvioJud().getBussId());			
			broche.setPermiteImpresion(brocheVO.getPermiteImpresion().getBussId());
			
			Repartidor repartidor = null;
			if(!ModelUtil.isNullOrEmpty(brocheVO.getRepartidor()))
				repartidor = Repartidor.getByIdNull(brocheVO.getRepartidor().getId());
			broche.setRepartidor(repartidor);
			
            PadDistribucionManager.getInstance().updateBroche(broche); 

            if (broche.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				brocheVO =  (BrocheVO) broche.toVO(0, false);
			}
            broche.passErrorMessages(brocheVO);
            
            log.debug(funcName + ": exit");
            return brocheVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public BrocheVO activarBroche(UserContext userContext, BrocheVO brocheVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Broche broche = Broche.getById(brocheVO.getId());

			broche.activar();

            if (broche.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				brocheVO =  (BrocheVO) broche.toVO();
			}
            broche.passErrorMessages(brocheVO);
            
            log.debug(funcName + ": exit");
            return brocheVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public BrocheVO desactivarBroche(UserContext userContext, BrocheVO brocheVO) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			Broche broche = Broche.getById(brocheVO.getId());

			broche.desactivar();

            if (broche.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				brocheVO =  (BrocheVO) broche.toVO();
			}
            broche.passErrorMessages(brocheVO);
            
            log.debug(funcName + ": exit");
            return brocheVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public BrocheAdapter getBrocheAdapterParamTipoBroche(UserContext userContext, BrocheAdapter brocheAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			brocheAdapter.clearError();
			
			if (!ModelUtil.isNullOrEmpty(brocheAdapter.getBroche().getTipoBroche())){
				if(brocheAdapter.getBroche().getTipoBroche().getId().longValue() == 1){
					brocheAdapter.setParamTipoBroche(1);
				} else if(brocheAdapter.getBroche().getTipoBroche().getId().longValue() == 3 || brocheAdapter.getBroche().getTipoBroche().getId().longValue() == 6){  
					brocheAdapter.setParamTipoBroche(2);
				}else{
					brocheAdapter.setParamTipoBroche(0);		
				}

			}else{
				brocheAdapter.setParamTipoBroche(0);
			}
			
			log.debug(funcName + ": exit");
			return brocheAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	

	}

	/**
	 * Param de Cuenta para BroCue
	 * @author tecso
	 * @return BroCueAdapter 
	 */
	public BroCueAdapter paramCuentaBroCue(UserContext userContext, BroCueAdapter broCueAdapter, Long selectedId) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			
			Cuenta cuenta = Cuenta.getByIdNull(selectedId);
			broCueAdapter.getBroCue().setCuenta((CuentaVO) cuenta.toVO());
			
			log.debug(funcName + ": exit");
			return broCueAdapter;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public BroCueSearchPage getBroCueSearchPageInit(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			BroCueSearchPage broCueSearchPage = new BroCueSearchPage();
	
			Broche broche = Broche.getById(commonKey.getId());
			/*if(broche.getRepartidor()!=null)
				broche.getRepartidor().setPersona(Persona.getByIdLight(broche.getRepartidor().getPersona().getId()));
				 */ 
			broCueSearchPage.getBroCue().setBroche((BrocheVO) broche.toVOSpecific(false));
			
			// Inicializo el repartidor si no tenia ninguno asignado. 
			if(broche.getRepartidor()==null)
				broCueSearchPage.getBroCue().getBroche().setRepartidor(new RepartidorVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
			
			if (!ModelUtil.isNullOrEmpty(broCueSearchPage.getBroCue().getBroche().getTipoBroche())){
				if(broCueSearchPage.getBroCue().getBroche().getTipoBroche().getId().longValue() == 1){
					broCueSearchPage.setParamTipoBroche(1);
				} else if(broCueSearchPage.getBroCue().getBroche().getTipoBroche().getId().longValue() == 3 || broCueSearchPage.getBroCue().getBroche().getTipoBroche().getId().longValue() == 6){  
					broCueSearchPage.setParamTipoBroche(2);
				}else{
					broCueSearchPage.setParamTipoBroche(0);		
				}

			}else{
				broCueSearchPage.setParamTipoBroche(0);
			}
			
			broCueSearchPage.setPageNumber(new Long(1));
			// Aqui obtiene lista de BOs
	   		List<BroCue> listBroCue = PadDAOFactory.getBroCueDAO().getListBySearchPage(broCueSearchPage);  
			
			//List<BroCueVO> listBroCueVO = (ArrayList<BroCueVO>) ListUtilBean.toVO(listBroCue,1);
	   		List<BroCueVO> listBroCueVO = (ArrayList<BroCueVO>) BroCue.listToVOSpecific(listBroCue);
	   		
	   		broCueSearchPage.setListBroCue(listBroCueVO);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return broCueSearchPage;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public BroCueSearchPage getBroCueSearchPageResult(UserContext userContext, BroCueSearchPage broCueSearchPage) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			broCueSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<BroCue> listBroCue = PadDAOFactory.getBroCueDAO().getListBySearchPage(broCueSearchPage);  
		
	   		//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
	   		//Aqui pasamos BO a VO
	   		//List<BroCueVO> listBroCueVO = (ArrayList<BroCueVO>) ListUtilBean.toVO(listBroCue,1);
	   		List<BroCueVO> listBroCueVO = (ArrayList<BroCueVO>) BroCue.listToVOSpecific(listBroCue);
	   		
	   		broCueSearchPage.setListBroCue(listBroCueVO);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return broCueSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

}
