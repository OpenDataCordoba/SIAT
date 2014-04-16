//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.service;

/**
 * Implementacion de servicios del submodulo ObjetoImponible del modulo Pad
 * @author tecso
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.iface.model.CasoCache;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.RecGenCueAtrVa;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAtrVO;
import ar.gov.rosario.siat.def.iface.model.TipObjImpVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.ef.buss.bean.Inspector;
import ar.gov.rosario.siat.ef.iface.model.InspectorVO;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.gde.buss.bean.CierreComercio;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAnulada;
import ar.gov.rosario.siat.gde.buss.bean.DeudaCancelada;
import ar.gov.rosario.siat.gde.buss.bean.DeudaJudicial;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaAutoManager;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.CierreComercioVO;
import ar.gov.rosario.siat.pad.buss.bean.AltaOficio;
import ar.gov.rosario.siat.pad.buss.bean.Calle;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaTitular;
import ar.gov.rosario.siat.pad.buss.bean.Domicilio;
import ar.gov.rosario.siat.pad.buss.bean.EstCue;
import ar.gov.rosario.siat.pad.buss.bean.EstObjImp;
import ar.gov.rosario.siat.pad.buss.bean.GeneralFacade;
import ar.gov.rosario.siat.pad.buss.bean.Localidad;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import ar.gov.rosario.siat.pad.buss.bean.ObjImpAtrVal;
import ar.gov.rosario.siat.pad.buss.bean.PadCuentaManager;
import ar.gov.rosario.siat.pad.buss.bean.PadDomicilioManager;
import ar.gov.rosario.siat.pad.buss.bean.PadObjetoImponibleManager;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.buss.bean.TipoDomicilio;
import ar.gov.rosario.siat.pad.buss.bean.TipoTitular;
import ar.gov.rosario.siat.pad.buss.bean.UbicacionFacade;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.AltaOficioAdapter;
import ar.gov.rosario.siat.pad.iface.model.AltaOficioSearchPage;
import ar.gov.rosario.siat.pad.iface.model.AltaOficioVO;
import ar.gov.rosario.siat.pad.iface.model.CierreComercioSearchPage;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.model.LocalidadVO;
import ar.gov.rosario.siat.pad.iface.model.ObjImpAdapter;
import ar.gov.rosario.siat.pad.iface.model.ObjImpAtrValAdapter;
import ar.gov.rosario.siat.pad.iface.model.ObjImpSearchPage;
import ar.gov.rosario.siat.pad.iface.model.ObjImpVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import ar.gov.rosario.siat.pad.iface.service.IPadObjetoImponibleService;
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
import coop.tecso.demoda.iface.model.UserContext;

public class PadObjetoImponibleServiceHbmImpl implements IPadObjetoImponibleService {
	private Logger log = Logger.getLogger(PadObjetoImponibleServiceHbmImpl.class);
	
	// ---> ABM ObjImp 	
	public ObjImpSearchPage getObjImpSearchPageInit(UserContext userContext, TipObjImpVO tipObjImpReadOnly) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ObjImpSearchPage objImpSearchPage = new ObjImpSearchPage();
			

			//	Seteo la lista de tipObjImp
			if (ModelUtil.isNullOrEmpty(tipObjImpReadOnly)) {
				List<TipObjImp> listTipObjImp = TipObjImp.getListActivos();			
				objImpSearchPage.setListTipObjImp((ArrayList<TipObjImpVO>)ListUtilBean.toVO(listTipObjImp, 0, 
						new TipObjImpVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			// Si es utilizado para buscar un tipo de objimp en particular
			} else {
				List<TipObjImpVO> listTipObjImp = new ArrayList<TipObjImpVO>();
				listTipObjImp.add(tipObjImpReadOnly);
				objImpSearchPage.setListTipObjImp(listTipObjImp);
				
				TipObjImp tipObjImp = TipObjImp.getById(tipObjImpReadOnly.getId()); 
				
				objImpSearchPage.setTipObjImpDefinition( tipObjImp.getDefinitionForBusqueda(TipObjImp.FOR_BUSQUEDA));				
				
				// Seteo de permiso para Agregar si el Tipo Objeto Imponible "Es Siat"
				if (tipObjImp.getEsSiat().intValue() == 0){
					objImpSearchPage.setAgregarBussEnabled(false);		
				}else{
					objImpSearchPage.setAgregarBussEnabled(true);
				}
			}		
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return objImpSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
		
	}

	public ObjImpSearchPage getObjImpSearchPageResult(UserContext userContext, ObjImpSearchPage objImpSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			objImpSearchPage.clearError();

			// Validaciones
			if (ModelUtil.isNullOrEmpty(objImpSearchPage.getObjImp().getTipObjImp())){
				objImpSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMP_LABEL);
			}
			
			if (objImpSearchPage.hasError()){
				return objImpSearchPage;
			}
			
			
			// Aqui obtiene lista de BOs
	   		List<ObjImp> listObjImp = PadDAOFactory.getObjImpDAO().getBySearchPage(objImpSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		objImpSearchPage.setListResult(ListUtilBean.toVO(listObjImp,0));

	   		// bug 343: si el tipo de objeto imponible es ta marcado como esSiat=NO, deshabilitamos la opcion eliminar
	   		TipObjImp tipObjImp = TipObjImp.getById(objImpSearchPage.getObjImp().getTipObjImp().getId());

	   		for (ObjImpVO objImpVO: (List<ObjImpVO>) objImpSearchPage.getListResult()){
	   			if (tipObjImp.getEsSiat().intValue() == 0){
	   				objImpVO.setEliminarBussEnabled(false);
	   			}
	   		}
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return objImpSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ObjImpSearchPage getObjImpSearchPageResultAva(UserContext userContext, ObjImpSearchPage objImpSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			objImpSearchPage.clearError();

			// Validaciones
			if (ModelUtil.isNullOrEmpty(objImpSearchPage.getObjImp().getTipObjImp())){
				objImpSearchPage.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMP_LABEL);
			}
			
			if (objImpSearchPage.hasError()){
				return objImpSearchPage;
			}
			
			// Aqui obtiene lista de BOs
	   		List<ObjImp> listObjImp = PadDAOFactory.getObjImpDAO().getBySearchPageAva(objImpSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		objImpSearchPage.setListResult(ListUtilBean.toVO(listObjImp,0));
	   		
	   		// bug 343: si el tipo de objeto imponible es ta marcado como esSiat=NO, deshabilitamos la opcion eliminar
	   		TipObjImp tipObjImp = TipObjImp.getById(objImpSearchPage.getObjImp().getTipObjImp().getId());

	   		for (ObjImpVO objImpVO: (List<ObjImpVO>) objImpSearchPage.getListResult()){
	   			if (tipObjImp.getEsSiat().intValue() == 0){
	   				objImpVO.setEliminarBussEnabled(false);
	   			}
	   		}
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return objImpSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	public ObjImpSearchPage getObjImpSearchPageParamTipObjImp(UserContext userContext, ObjImpSearchPage objImpSearchPage) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			objImpSearchPage.clearError();
			
			if (!ModelUtil.isNullOrEmpty(objImpSearchPage.getObjImp().getTipObjImp())){
				TipObjImp tipObjImp = TipObjImp.getById(objImpSearchPage.getObjImp().getTipObjImp().getId()); 
				
				objImpSearchPage.setTipObjImpDefinition( tipObjImp.getDefinitionForBusqueda(TipObjImp.FOR_BUSQUEDA));				
				
				// Seteo de permiso para Agregar si el Tipo Objeto Imponible "Es Siat"
				if (tipObjImp.getEsSiat().intValue() == 0){
					objImpSearchPage.setAgregarBussEnabled(false);		
				}else{
					objImpSearchPage.setAgregarBussEnabled(true);
				}
				
			} else {
				objImpSearchPage.getTipObjImpDefinition().setListTipObjImpAtrDefinition(new ArrayList<TipObjImpAtrDefinition>()); 
			}
			
			log.debug(funcName + ": exit");
			return objImpSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
		
	}
	
	public ObjImpAdapter getObjImpAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ObjImp objImp = ObjImp.getById(commonKey.getId());

	        ObjImpAdapter objImpAdapter = new ObjImpAdapter();
	        // toVO personalizado para que no traiga las listas
	        objImpAdapter.setObjImp((ObjImpVO) objImp.toVO(0));
	        objImpAdapter.getObjImp().setTipObjImp((TipObjImpVO) objImp.getTipObjImp().toVO(0));
	        
	        // Recupero la definicion de los AtrVal valorizados
	        objImpAdapter.setTipObjImpDefinition(objImp.getDefinitionValue());
	        
			log.debug(funcName + ": exit");
			return objImpAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ObjImpAdapter getObjImpAdapterForCreate(UserContext userContext, CommonKey idTipObjImp) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			ObjImpAdapter objImpAdapter = new ObjImpAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para combos, etc
			objImpAdapter.clearError();
			
			List<TipObjImp> listTipObjImp = TipObjImp.getListEsSiat();
						
			objImpAdapter.setListTipObjImp(
					(ArrayList<TipObjImpVO>) ListUtilBean.toVO(listTipObjImp, 0, 
					new TipObjImpVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR))); 
						
			log.debug(funcName + ": exit");
			return objImpAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ObjImpAdapter getObjImpAdapterParamTipObjImp(UserContext userContext, ObjImpAdapter objImpAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			objImpAdapter.clearError();
			
			// Logica del param
			objImpAdapter.getObjImp().setFechaAlta(new Date());
			
			if (!ModelUtil.isNullOrEmpty(objImpAdapter.getObjImp().getTipObjImp())){
				TipObjImp tipObjImp = TipObjImp.getById(objImpAdapter.getObjImp().getTipObjImp().getId()); 
				
				objImpAdapter.setTipObjImpDefinition( tipObjImp.getDefinitionForManual());				
				
			} else {
				objImpAdapter.getTipObjImpDefinition().setListTipObjImpAtrDefinition(new ArrayList<TipObjImpAtrDefinition>()); 
			}
			
			
			log.debug(funcName + ": exit");
			return objImpAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public ObjImpAdapter getObjImpAdapterForUpdate(UserContext userContext, CommonKey commonKeyObjImp) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ObjImp objImp = ObjImp.getById(commonKeyObjImp.getId());

	        ObjImpAdapter objImpAdapter = new ObjImpAdapter();
	        objImpAdapter.setObjImp((ObjImpVO) objImp.toVO(1));

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return objImpAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ObjImpAdapter createObjImp(UserContext userContext, ObjImpAdapter objImpAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			objImpAdapterVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ObjImp objImp = new ObjImp();
            
            objImp.setTipObjImp(TipObjImp.getByIdNull(objImpAdapterVO.getObjImp().getTipObjImp().getId()));
            objImp.setFechaAlta(objImpAdapterVO.getObjImp().getFechaAlta());
            objImp.setClave(objImpAdapterVO.getTipObjImpDefinition().getValClave());
            objImp.setClaveFuncional(objImpAdapterVO.getTipObjImpDefinition().getValClaveFunc());
            
            objImp.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager
            // En el se recorren los AtrValDefinitions cargados.
            objImp = PadObjetoImponibleManager.getInstance().createObjImp(objImp, objImpAdapterVO.getTipObjImpDefinition());
            
            if (objImp.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				ObjImpVO objImpVO =  (ObjImpVO) objImp.toVO(3);
				objImpAdapterVO.setObjImp(objImpVO);
			}
            
            objImp.passErrorMessages(objImpAdapterVO);
			
            log.debug(funcName + ": exit");
            return objImpAdapterVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ObjImpVO updateObjImp(UserContext userContext, ObjImpVO objImpVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			objImpVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ObjImp objImp = ObjImp.getById(objImpVO.getId());
            
            if(!objImpVO.validateVersion(objImp.getFechaUltMdf())) return objImpVO;
            
            objImp.setFechaAlta(objImpVO.getFechaAlta());                        
            objImp.setEstado(Estado.ACTIVO.getId());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            objImp = PadObjetoImponibleManager.getInstance().updateObjImp(objImp);
            
            if (objImp.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				objImpVO =  (ObjImpVO) objImp.toVO(3);
			}
			objImp.passErrorMessages(objImpVO);
            
            log.debug(funcName + ": exit");
            return objImpVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ObjImpVO deleteObjImp(UserContext userContext, ObjImpVO objImpVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			objImpVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			ObjImp objImp = ObjImp.getById(objImpVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			objImp = PadObjetoImponibleManager.getInstance().deleteObjImp(objImp);
			
			if (objImp.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				objImpVO =  (ObjImpVO) objImp.toVO(3);
			}
			objImp.passErrorMessages(objImpVO);
            
            log.debug(funcName + ": exit");
            return objImpVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ObjImpVO activarObjImp(UserContext userContext, ObjImpVO objImpVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            ObjImp objImp = ObjImp.getById(objImpVO.getId());

            objImp.activar();

            if (objImp.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				objImpVO =  (ObjImpVO) objImp.toVO();
			}
            objImp.passErrorMessages(objImpVO);
            
            log.debug(funcName + ": exit");
            return objImpVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ObjImpVO desactivarObjImp(UserContext userContext, ObjImpVO objImpVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx  = null;

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

            ObjImp objImp = ObjImp.getById(objImpVO.getId());

            objImp.desactivar(objImpVO.getFechaBaja());

            if (objImp.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				objImpVO =  (ObjImpVO) objImp.toVO();
			}
            objImp.passErrorMessages(objImpVO);
            
            log.debug(funcName + ": exit");
            return objImpVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	// <--- ABM ObjImp

	
	public ObjImpAtrValAdapter getObjImpAtrValAdapterForView(UserContext userContext, CommonKey idObjImp, CommonKey idTipObjImpAtr) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			ObjImp objImp = ObjImp.getById(idObjImp.getId()); 
			
			ObjImpAtrValAdapter objImpAtrValAdapter = new ObjImpAtrValAdapter();
			ObjImpVO objImpVO = (ObjImpVO) objImp.toVO(0);
			objImpAtrValAdapter.setObjImp(objImpVO);
			objImpAtrValAdapter.getObjImp().setTipObjImp((TipObjImpVO) objImp.getTipObjImp().toVO(0));
			
			// Recupero la definicion del AtrVal valorizados
			TipObjImpDefinition tipObjImpDefinition = objImp.getDefinitionValue(idTipObjImpAtr.getId());
			
			TipObjImpAtrDefinition tipObjImpAtrDefinition = tipObjImpDefinition.getListTipObjImpAtrDefinition().get(0);
			
			objImpAtrValAdapter.setTipObjImpAtrDefinition(tipObjImpAtrDefinition);
			
			
			log.debug(funcName + ": exit");
			return objImpAtrValAdapter;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ObjImpAtrValAdapter updateObjImpAtrVal(UserContext userContext, ObjImpAtrValAdapter objImpAtrValAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			objImpAtrValAdapterVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            ObjImp objImp = ObjImp.getById(objImpAtrValAdapterVO.getObjImp().getId());
            
            TipObjImpDefinition tipObjImpDefinition = new TipObjImpDefinition();
            
            tipObjImpDefinition.addTipObjImpAtrDefinition(objImpAtrValAdapterVO.getTipObjImpAtrDefinition());
            
            tipObjImpDefinition = objImp.updateObjImpAtrDefinition(tipObjImpDefinition);
            
            
            if (tipObjImpDefinition.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				
				/*objImpAtrValAdapterVO.setObjImp((ObjImpVO) objImp.toVO(0));
				objImpAtrValAdapterVO.getObjImp().sett*/
			}
            
            tipObjImpDefinition.passErrorMessages(objImpAtrValAdapterVO);
			
            log.debug(funcName + ": exit");
            return objImpAtrValAdapterVO;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// ---> ABM AltaOficio
	public AltaOficioAdapter createAltaOficio(UserContext userContext,
			AltaOficioAdapter altaOficioAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			altaOficioAdapter.clearErrorMessages();

			// valida nro Acta
			if(StringUtil.isNullOrEmpty(altaOficioAdapter.getAltaOficio().getNroActa())){
				altaOficioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.ALTAOFICIO_NROACTA_LABEL);
			}
			
			// valida fecha Acta
			if(altaOficioAdapter.getAltaOficio().getFecha()==null){
				altaOficioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.ALTAOFICIO_FECHA);
			}

			//valida inspector
			if(ModelUtil.isNullOrEmpty(altaOficioAdapter.getAltaOficio().getInspector())){
				altaOficioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.INSPECTOR_LABEL);
			}
			
			
			// valida el contribuyente
			if(ModelUtil.isNullOrEmpty(altaOficioAdapter.getContribuyente().getPersona())){
				altaOficioAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.ALTAOFICIO_CONTR_LABEL);				
			}
			
			// valida el domicilio
			if(altaOficioAdapter.getContribuyente().getPersona().getDomicilio().getEsValidado().equals(
																									SiNo.NO)){
				altaOficioAdapter.addRecoverableError(PadError.ALTAOFICIO_DOMICILIO_NOVALIDADO);
			}
			
			if(altaOficioAdapter.hasError()){
				return altaOficioAdapter;
			}
			
			
			
			// Creacion del Comercio
            ObjImp objImpComercio = new ObjImp();                        
            objImpComercio.setTipObjImp(TipObjImp.getById(TipObjImp.COMERCIO));
            Date fechaInicioActividad = altaOficioAdapter.getFechaInicioActividad();
			objImpComercio.setFechaAlta(fechaInicioActividad);
			log.debug("Fecha Inicio: "+objImpComercio.getFechaAlta());
			// actualiza el domicilio ingresado - se pone el valor view() del domicilio porque despues al crear el objImp, parsea el string y obtiene el domicilio
			DomicilioVO domicilioVO = altaOficioAdapter.getContribuyente().getPersona().getDomicilio();
			Domicilio domicilioEnvio = new Domicilio();
			Calle calle = new Calle();
			Localidad localidad = new Localidad();
			localidad.setId(domicilioVO.getLocalidad().getId());
			localidad.setCodPostal(domicilioVO.getLocalidad().getCodPostal());
			localidad.setCodSubPostal(domicilioVO.getLocalidad().getCodSubPostal());
			localidad.setDescripcionPostal(domicilioVO.getLocalidad().getDescripcionPostal());
			log.debug("numero:"+domicilioVO.getNumeroView());
			domicilioEnvio.setLocalidad(localidad);
			domicilioEnvio.setNumero(StringUtil.isNullOrEmpty(domicilioVO.getNumeroView())?null:new Long(domicilioVO.getNumeroView()));
			domicilioEnvio.setBis(domicilioVO.getBis().getBussId());
			domicilioEnvio.setLetraCalle(domicilioVO.getLetraCalle());
			domicilioEnvio.setPiso(domicilioVO.getPiso());
			domicilioEnvio.setDepto(domicilioVO.getDepto());
			domicilioEnvio.setMonoblock(domicilioVO.getMonoblock());
			domicilioEnvio.setRefGeografica(domicilioVO.getRefGeografica());
			domicilioEnvio.setTipoDomicilio(TipoDomicilio.getById(TipoDomicilio.ID_DOMICILIO_ENVIO));
			domicilioEnvio.setEsValidado(SiNo.SI.getId());
			// Seteamos en null para que la validacion siempre utilize el nombre de calle para validarla
			calle.setId(null);
			calle.setNombreCalle(domicilioVO.getCalle().getNombreCalle());
			domicilioEnvio.setCalle(calle);
			
			// validacion de datos requeridos
			if (!domicilioEnvio.validateForMCR()){
				domicilioEnvio.passErrorMessages(altaOficioAdapter);
				domicilioVO.setValidoPorRequeridos(false);
				return altaOficioAdapter;
			}
			
			altaOficioAdapter.getTipObjImpDefinition().getTipObjImpAtrDefinitionByCodigo("DomicilioFinca").
								setValor(domicilioEnvio.toStringAtr());
			
			log.debug("valor clave del objImp:"+altaOficioAdapter.getTipObjImpDefinition().getValClave());
			//Obtengo el nro de comercio
			//Modificado el 171109 segun mantis 4678 para sumar 1 al numero
			Integer nroComercio = GeneralFacade.getInstance().getNroDeComercio("COMERCIOS", "NRO_COM")+1;
			
			altaOficioAdapter.getTipObjImpDefinition().getTipObjImpAtrDefinitionByCodigo("NroComercio").setValor(nroComercio.toString());
			String nroCuenta = StringUtil.agregaDigitoVerificador(nroComercio.toString());
			altaOficioAdapter.getTipObjImpDefinition().getTipObjImpAtrDefinitionByCodigo("NroCuenta").setValor(nroCuenta);
            objImpComercio.setClave(altaOficioAdapter.getTipObjImpDefinition().getValClave());
            objImpComercio.setClaveFuncional(altaOficioAdapter.getTipObjImpDefinition().getValClaveFunc());            
            objImpComercio.setEstado(Estado.ACTIVO.getId());            
            	// Aqui la creacion esta delegada en el manager. En el se recorren los AtrValDefinitions cargados.
            objImpComercio = PadObjetoImponibleManager.getInstance().createObjImp(objImpComercio, altaOficioAdapter.getTipObjImpDefinition());
            if(objImpComercio.hasError()){
            	tx.rollback();
            	if(log.isDebugEnabled())log.debug(funcName + ": tx.rollback");
            	objImpComercio.passErrorMessages(altaOficioAdapter);
            	log.debug(funcName + ": exit Con Errores en alta de Comercio");
                return altaOficioAdapter;
            }
                       
            tx.commit();            
            log.debug("Creo el comercio sin errores - id:"+objImpComercio.getId());
            
            session = SiatHibernateUtil.currentSession();
            tx = session.beginTransaction();
                        
            // Se actualiza la fechaNovedad de los atributos (objImpAtrVal) que no tienen vigencia. Esto es por una falta en el metodo createObjImp(...) llamado anteriormente
            session.refresh(objImpComercio);
            for(ObjImpAtrVal objImpAtrVal: objImpComercio.getListObjImpAtrVal()){
            	if(objImpAtrVal.getFechaDesde()==null && objImpAtrVal.getFechaNovedad()==null){
            		objImpAtrVal.setFechaNovedad(new Date());
            		objImpComercio.updateObjImpAtrVal(objImpAtrVal);
            	}
            }
            
            // Se replican los valores de aquellos atributos que son SIAT y que tienen como valor por defecto un campo definido como  NO SIAT
            log.debug("Replicando valores...");
            List<TipObjImpAtr>  listTipObjImpAtr = TipObjImpAtr.getList(TipObjImp.COMERCIO, true, true,
            		Estado.ACTIVO);
            
            for(TipObjImpAtr tipObjImpAtr: listTipObjImpAtr){
            	TipObjImpAtr tipObjImpAtr1 = TipObjImpAtr.getById(new Long(tipObjImpAtr.getValorDefecto()));
            	ObjImpAtrVal objImpAtrValComercio =  ObjImpAtrVal.getVigenteByIdObjImpAtrVal(
            								tipObjImpAtr1.getId(), objImpComercio.getId());
				
            	if(objImpAtrValComercio!=null){
            		log.debug("atributo:"+objImpAtrValComercio.getTipObjImpAtr().getAtributo().getDesAtributo()+":"+objImpAtrValComercio.getStrValor());
	            	ObjImpAtrVal objImpAtrValNuevo = new ObjImpAtrVal();
					objImpAtrValNuevo.setObjImp(objImpComercio);            	
					objImpAtrValNuevo.setTipObjImpAtr(tipObjImpAtr);
					log.debug("fechaDesde:"+objImpAtrValComercio.getFechaDesde());
					objImpAtrValNuevo.setFechaDesde(objImpAtrValComercio.getFechaDesde());
					objImpAtrValNuevo.setFechaNovedad(objImpAtrValComercio.getFechaDesde()!=null?objImpAtrValComercio.getFechaDesde():new Date());
					objImpAtrValNuevo.setStrValor(objImpAtrValComercio.getStrValor());
					objImpAtrValNuevo = objImpComercio.createObjImpAtrVal(objImpAtrValNuevo);
            	}
            }
                      
            log.debug("FIN Replicando valores...");
            
            // Creacion del Alta de Oficio
			AltaOficio altaOficio = new AltaOficio();
				// Copiado de propiadades de VO al BO
            this.copyFromVO(altaOficio, altaOficioAdapter.getAltaOficio());
            altaOficio.setEstado(Estado.ACTIVO.getId());
            altaOficio.setObjImp(objImpComercio); 
            altaOficio.setInspector(Inspector.getByIdNull(altaOficioAdapter.getAltaOficio().getInspector().getId()));
            
            	// Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            altaOficio = PadObjetoImponibleManager.getInstance().createAltaOficio(altaOficio);
            
            if(altaOficio.hasError()){
            	tx.rollback();
            	if(log.isDebugEnabled())log.debug(funcName + ": tx.rollback");
            	altaOficio.passErrorMessages(altaOficioAdapter);
            	log.debug(funcName + ": exit Con Errores en crear Alta de Oficio");
                return altaOficioAdapter;
            }
            
	          //crea el titular de la cuenta DReI y ETur y las activa
		           // obtiene la cuenta que se creo al momento de crear al objImp
            
	        Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(Recurso.getDReI().getId(), altaOficioAdapter.getTipObjImpDefinition().getValClave());
	        session.flush();
	        
	        crearTitular(cuenta, altaOficioAdapter.getContribuyente(), altaOficioAdapter.getAltaOficio().getFecha());

	        // setea el domicilio de envio en la cuenta
	        domicilioEnvio = PadDomicilioManager.getInstance().createDomicilio(domicilioEnvio);
	        log.debug("Seteand el domicilio en la cuenta:"+domicilioEnvio.getViewDomicilio());
	        cuenta.setDomicilioEnvio(domicilioEnvio);

	        cuenta = Cuenta.getByIdRecursoYNumeroCuenta(Recurso.getETur().getId(), altaOficioAdapter.getTipObjImpDefinition().getValClave());
	        	// Si tiene cuenta ETuR crea un titular
	        if(cuenta!=null){
	        	crearTitular(cuenta, altaOficioAdapter.getContribuyente(), altaOficioAdapter.getAltaOficio().getFecha());	        	        
		        // setea el domicilio de envio en la cuenta
		        domicilioEnvio = PadDomicilioManager.getInstance().createDomicilio(domicilioEnvio);
		        log.debug("Seteand el domicilio en la cuenta:"+domicilioEnvio.getViewDomicilio());
		        cuenta.setDomicilioEnvio(domicilioEnvio);	        	
	        }


           if (altaOficio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				log.debug("Creo el Alta de Oficio sin errores");
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
           altaOficio.passErrorMessages(altaOficioAdapter);
            
           
            log.debug(funcName + ": exit");
            return altaOficioAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	/**
	 * Crea un titular para la cuenta pasada como parametro con el contribuyente pasada como parametro, 
	 * seteando la fechaDesde con la fecha pasada como parametro
	 * @param cuenta
	 * @param contribuyenteVO
	 * @param fecha
	 * @throws Exception
	 */
	private void crearTitular(Cuenta cuenta, ContribuyenteVO contribuyenteVO, Date fecha) throws Exception{
		   log.debug("Va a buscar el contribuyente para la persona:"+contribuyenteVO.getPersona().getId()+
				   "    "+contribuyenteVO.getPersona().getRepresent());
	        Contribuyente contribuyente = Contribuyente.getByIdPersona(contribuyenteVO.getPersona().getId());
	        if(contribuyente==null){
	        	log.debug("Va a crear el contribuyente");
	     	   // Se crea el contribuyente
	     	   contribuyente = new Contribuyente();
	     	   contribuyente.setPersona(Persona.getById(contribuyenteVO.getPersona().getId()));
	     	   contribuyente.setFechaDesde(fecha);        	   
	        }
	        contribuyente.setNroIsib(contribuyenteVO.getNroIsib());
	        
	        
	        
	        CuentaTitular cuentaTitular = new CuentaTitular();
	        cuentaTitular.setContribuyente(contribuyente);	
	        cuentaTitular.setTipoTitular(TipoTitular.getById(1L));// tipoTitular= TITULAR
	        cuentaTitular.setFechaDesde(cuenta.getFechaAlta());
	        cuentaTitular.setFechaNovedad(new Date());
	        cuentaTitular.setEsAltaManual(0);
	        cuentaTitular.setEsTitularPrincipal(1);
	        
	        cuenta.createCuentaTitular(cuentaTitular);
	        cuenta.setEstado(Estado.ACTIVO.getId());	
	        cuenta.setEstCue(EstCue.getById(EstCue.ID_ACTIVO));
	        
	        if (cuenta.getObjImp()!=null && cuenta.getObjImp().getTipObjImp().equals(TipObjImp.getById(TipObjImp.COMERCIO))){
	        	log.debug("Reviso las deudas, fecha de alta cuenta: "+DateUtil.formatDate(cuenta.getFechaAlta(),DateUtil.ddSMMSYYYY_MASK));
	        	contribuyente = GdeGDeudaAutoManager.getInstance().updateFecVenListDeuForTipoContribuyente(contribuyente, cuenta.getFechaAlta());
	        }
	}
	
	/**
	 * Copia el nro de acta, la fecha y el inspector del VO al Bean
	 * @param altaOficio
	 * @param altaOficioVO
	 */
	private void copyFromVO(AltaOficio altaOficio, AltaOficioVO altaOficioVO) {
		altaOficio.setNroActa(altaOficioVO.getNroActa());
		altaOficio.setFecha(altaOficioVO.getFecha());
		altaOficio.setInspector(Inspector.getByIdNull(altaOficioVO.getInspector().getId()));
	}

	
	public AltaOficioVO deleteAltaOficio(UserContext userContext,
			AltaOficioVO altaOficioVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			altaOficioVO.clearErrorMessages();
						
			//recupera del alta de oficio
			AltaOficio altaOficio = AltaOficio.getById(altaOficioVO.getId());

			// Borra el altaOficio
			altaOficio = PadObjetoImponibleManager.getInstance().deleteAltaOficio(altaOficio);
			
			if(altaOficio.hasError()){
				tx.rollback();				
				log.error("Se produjeron errores eliminando el altaOficio");
				altaOficio.passErrorMessages(altaOficioVO);
			}else{				
				tx.commit();                        
			}
			
			log.debug(funcName + ": exit");                      
            return altaOficioVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	public AltaOficioAdapter getAltaOficioAdapterForCreate(
			UserContext userContext) throws DemodaServiceException {
		AltaOficioAdapter altaOficioAdapter = new AltaOficioAdapter();
		TipObjImp tipObjImp = TipObjImp.getById(TipObjImp.COMERCIO); 
		
		try {
			TipObjImpDefinition tipObjImpDefinition = tipObjImp.getDefinitionForManual();

			altaOficioAdapter.setTipObjImpDefinition(tipObjImpDefinition);
			
			altaOficioAdapter.getAltaOficio().setFecha(new Date());
			//Setea por defecto la localidad ROSARIO
			altaOficioAdapter.getContribuyente().getPersona().getDomicilio().setLocalidad(
											(LocalidadVO) UbicacionFacade.getInstance().getRosario().toVO());
			
		} catch (Exception e) {
			log.error(e);
		}				

		return altaOficioAdapter;		
	}
	
	public AltaOficioAdapter getAltaOficioAdapterParamPersona(UserContext userContext, AltaOficioAdapter altaOficioAdapterVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			altaOficioAdapterVO.clearError();

			Persona persona = Persona.getById(altaOficioAdapterVO.getContribuyente().getPersona().getId());
			
			PersonaVO personaVO = (PersonaVO) persona.toVO(0, false);
			personaVO.setCuit(persona.getCuitContr());
			personaVO.setDomicilio(altaOficioAdapterVO.getContribuyente().getPersona().getDomicilio());
			altaOficioAdapterVO.getContribuyente().setPersona(personaVO);
			
			Contribuyente contribuyente = Contribuyente.getByIdPersona(altaOficioAdapterVO.getContribuyente().getPersona().getId());
			if(contribuyente!=null){
				// Si existe el contribuyente le setea el nro de ISIB
				altaOficioAdapterVO.getContribuyente().setId(contribuyente.getId());
				altaOficioAdapterVO.getContribuyente().setNroIsib(contribuyente.getNroIsib());
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return altaOficioAdapterVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public AltaOficioAdapter getAltaOficioAdapterParamInspector(UserContext userContext, AltaOficioAdapter altaOficioAdapterVO) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			altaOficioAdapterVO.clearError();

			//Aqui realizar validaciones

			Inspector inspector = Inspector.getById(altaOficioAdapterVO.getAltaOficio().getInspector().getId());
			
			altaOficioAdapterVO.getAltaOficio().setInspector((InspectorVO) inspector.toVO(0, false));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return altaOficioAdapterVO;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public AltaOficioAdapter getAltaOficioAdapterForView(
			UserContext userContext, CommonKey commonKey)
			throws Exception {
		
		// Obtiene el altaOficio
		AltaOficio altaOficio = AltaOficio.getByObjImp(commonKey.getId());
		
		AltaOficioAdapter altaOficioAdapter = new AltaOficioAdapter();
		
		altaOficioAdapter.setAltaOficio((AltaOficioVO) altaOficio.toVO(0, false));
		
		// Setea el inspector
		altaOficioAdapter.getAltaOficio().setInspector((InspectorVO) altaOficio.getInspector().toVO(0, false));
		
		// Setea el objImp
		altaOficioAdapter.getAltaOficio().setObjImp((ObjImpVO) altaOficio.getObjImp().toVO(0, false)); 
		
		// Setea los atributos del comercio		
		altaOficioAdapter.setTipObjImpDefinition(altaOficio.getObjImp().getDefinitionValue());

		// Setea los datos del contribuyente
		Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(Recurso.getDReI().getId(), altaOficio.getObjImp().getClave());				
		Contribuyente contribuyente = cuenta.getListContribuyente().get(0);
		contribuyente.loadPersonaFromMCR();		
		altaOficioAdapter.getContribuyente().getPersona().setRepresent(contribuyente.getPersona().getRepresent());
		altaOficioAdapter.getContribuyente().getPersona().setCuit(contribuyente.getPersona().getCuitContr());
		altaOficioAdapter.getContribuyente().setNroIsib(contribuyente.getNroIsib());
		
		return altaOficioAdapter;	
	}

	public AltaOficioAdapter getAltaOficioAdapterForUpdate(UserContext userContext, CommonKey commonKey) throws Exception {
		
		// Obtiene el altaOficio
		AltaOficio altaOficio = AltaOficio.getByObjImp(commonKey.getId());
		
		AltaOficioAdapter altaOficioAdapter = new AltaOficioAdapter();
		
		altaOficioAdapter.setAltaOficio((AltaOficioVO) altaOficio.toVO(3, true));
		
		// Setea el inspector
		altaOficioAdapter.getAltaOficio().setInspector((InspectorVO) altaOficio.getInspector().toVO(0, false));
		
		// Setea el objImp
		altaOficioAdapter.getAltaOficio().setObjImp((ObjImpVO) altaOficio.getObjImp().toVO(3, true)); 
		
		// Setea los atributos del comercio		
		
		TipObjImpDefinition definitionValue = altaOficio.getObjImp().getDefinitionValue();
		log.debug("Iterando el definition");
		for(TipObjImpAtrDefinition def:definitionValue.getListTipObjImpAtrDefinition()){
			log.debug(def.getAtributo().getDesAtributo()+":"+def.getValorString());
			log.debug("valorView:"+def.getValorView());
		}
		log.debug("FIN Iterando el definition");
		altaOficioAdapter.setTipObjImpDefinition(definitionValue);
		
		// Setea los datos del contribuyente
		Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(Recurso.getDReI().getId(), altaOficio.getObjImp().getClave());				
		Contribuyente contribuyente = cuenta.getListContribuyente().get(0);
		contribuyente.loadPersonaFromMCR();		
		altaOficioAdapter.getContribuyente().getPersona().setId(contribuyente.getPersona().getId());
		altaOficioAdapter.getContribuyente().getPersona().setRepresent(contribuyente.getPersona().getRepresent());
		altaOficioAdapter.getContribuyente().getPersona().setCuit(contribuyente.getPersona().getCuitContr());
		altaOficioAdapter.getContribuyente().setNroIsib(contribuyente.getNroIsib());
		// obtiene el domicilio, que es el de la cuenta
		if(cuenta.getDomicilioEnvio()!=null){			
			altaOficioAdapter.getContribuyente().getPersona().setDomicilio((DomicilioVO) 
																	cuenta.getDomicilioEnvio().toVO(3, false));
			Localidad localidad = Localidad.getByCodPostSubPost(cuenta.getDomicilioEnvio().getLocalidad().getCodPostal(),
					cuenta.getDomicilioEnvio().getLocalidad().getCodSubPostal());
			altaOficioAdapter.getContribuyente().getPersona().getDomicilio().setLocalidad((LocalidadVO) localidad.toVO(0));
		}
		
		// Valida que la cuenta no este referenciada a ninuna deuda para que se pueda modificar
		if (GenericDAO.hasReference(cuenta, DeudaAdmin.class, "cuenta")
				||GenericDAO.hasReference(cuenta, DeudaJudicial.class, "cuenta")
				||GenericDAO.hasReference(cuenta, DeudaAnulada.class, "cuenta")
				||GenericDAO.hasReference(cuenta, DeudaCancelada.class, "cuenta")) {
			altaOficioAdapter.setModificarBussEnabled(false);
			
		}else{
			// Se le setean a algunos atributos la bandera para que no se puedan modificar
			List<TipObjImpAtrDefinition> listAtrDef = altaOficioAdapter.getTipObjImpDefinition().getListTipObjImpAtrDefinition();
			for(TipObjImpAtrDefinition t: listAtrDef){
								
				// Se guarda la fecha de inicio de actividad porque si cambia se actualiza la fecha de alta de las cuentas DReI y ETUR (si corresponde)
				if(t.getAtributo().getCodAtributo().trim().equals(Atributo.COD_FECHA_INICIO)){
					altaOficioAdapter.setFechaInicioActividadOriginal(t.getValorString());
				}
				
				// no permite modificar estos atributos
				if(t.getAtributo().getCodAtributo().trim().equals(Atributo.COD_NROCUENTA) ||
					t.getAtributo().getCodAtributo().trim().equals(Atributo.COD_NROCOMERCIO)){
					
					t.setModificarBussEnabled(false);					
				}				
			}
		}
		

		return altaOficioAdapter;	
	}
	
	public AltaOficioSearchPage getAltaOficioSearchPageInit(
			UserContext usercontext) throws Exception {
		AltaOficioSearchPage altaOficioSearchPage = new AltaOficioSearchPage();
		
		altaOficioSearchPage.getListEstado().add(Estado.ACTIVO);
		altaOficioSearchPage.getListEstado().add(Estado.INACTIVO);		
		altaOficioSearchPage.setIdEstadoSel(-1);
		
		// Setea el recurso DReI
		altaOficioSearchPage.getCuenta().getRecurso().setId(Recurso.getDReI().getId());
		
		return altaOficioSearchPage;
	}

	public AltaOficioSearchPage getAltaOficioSearchPageResult(
			UserContext userContext, AltaOficioSearchPage altaOficioSearchPage)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			altaOficioSearchPage.clearError();

			//Aqui realizar validaciones
			if(altaOficioSearchPage.getFechaHasta()!=null && altaOficioSearchPage.getFechaDesde()!=null &&
					DateUtil.isDateBefore(altaOficioSearchPage.getFechaHasta(), 
							altaOficioSearchPage.getFechaDesde())){
				altaOficioSearchPage.addRecoverableError(BaseError.MSG_VALORMENORQUE,
						altaOficioSearchPage.getFechaHastaView(), altaOficioSearchPage.getFechaDesdeView());
				return altaOficioSearchPage;
			}
			
			// Aqui obtiene lista de BOs
	   		List<Cuenta> listCuenta = PadDAOFactory.getAltaOficioDAO().getBySearchPage(altaOficioSearchPage);   
	   		
			//Aqui pasamos BO a VO y se setean los permisos
	   		List<CuentaVO> listCuentaVO = new ArrayList<CuentaVO>();
	   		for(Cuenta cuenta: listCuenta){
	   			CuentaVO cuentaVO = cuenta.toVOLightForPDF();
	   			
	   			// Valida que la cuenta no este referenciada a ninguna deuda para que se pueda eliminar y/o activar/desactivar
	   			if (GenericDAO.hasReference(cuenta, DeudaAdmin.class, "cuenta")
	   					||GenericDAO.hasReference(cuenta, DeudaJudicial.class, "cuenta")
	   					||GenericDAO.hasReference(cuenta, DeudaAnulada.class, "cuenta")
	   					||GenericDAO.hasReference(cuenta, DeudaCancelada.class, "cuenta")) {
	   				cuentaVO.setEliminarBussEnabled(false);
	   				cuentaVO.setActivarBussEnabled(false);
	   				cuentaVO.setDesactivarBussEnabled(false);
	   			}

				listCuentaVO.add(cuentaVO);
	   		}
	   		altaOficioSearchPage.setListResult(listCuentaVO);
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return altaOficioSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	
	public AltaOficioAdapter updateAltaOficio(UserContext userContext,AltaOficioAdapter altaOficioAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			altaOficioAdapter.clearErrorMessages();
									
			//update del alta de oficio
			AltaOficio altaOficio = AltaOficio.getById(altaOficioAdapter.getAltaOficio().getId());
			copyFromVO(altaOficio, altaOficioAdapter.getAltaOficio());
			// Setea el nroIsib
			Contribuyente contribuyente = Contribuyente.getByIdPersona(
						altaOficioAdapter.getContribuyente().getPersona().getId());
			contribuyente.setNroIsib(altaOficioAdapter.getContribuyente().getNroIsib());
			
			// setea la fecha alta del objImp
			altaOficio.getObjImp().setFechaAlta(altaOficio.getFecha());
			
			altaOficio = PadObjetoImponibleManager.getInstance().updateAltaOficio(altaOficio);						
			
			boolean aplicaETuR = false;
			Date fechaInicioAct=DateUtil.getDate(altaOficioAdapter.getFechaInicioActividadOriginal(), DateUtil.YYYYMMDD_MASK);
			
			// Variable para identificar si se modifico un rubro de Comercio. Necesaria para actualizar el valor del atributo de asentamiento 
			// que determina la distribucion de partidas que le corresponde.
			boolean rubroDreiActualizado = false;
			
			//actualizar los atributos
			log.debug("iterando atributos----------------");
			for(TipObjImpAtrDefinition def: altaOficioAdapter.getTipObjImpDefinition().getListTipObjImpAtrDefinition()){
				log.debug("Atributo:"+def.getAtributo().getCodAtributo()+"     es multivalor:"+def.getEsMultivalor()+"    "+def.getMultiValor());
				
				// actualiza los datos del domicilio y el domicilio de envio de la cuenta asociada
				if(def.getAtributo().getCodAtributo().equals(Atributo.COD_DOMICILIOENVIO)){
					log.debug("actualizando domicilio:"+altaOficioAdapter.getContribuyente().getDomicilioFiscal().getView());
					String numeroCuenta = altaOficio.getObjImp().getClave();
					Cuenta cuenta  =Cuenta.getByIdRecursoYNumeroCuenta(Recurso.getDReI().getId(), numeroCuenta);					
					Domicilio domicilio = PadDomicilioManager.getInstance().obtenerDomicilio(altaOficioAdapter.getContribuyente().getPersona().getDomicilio());
					PadDomicilioManager.getInstance().updateDomicilio(domicilio);
					session.flush();
					cuenta.setDomicilioEnvio(domicilio);
					def.setValor(domicilio.getId().toString());
				}
				
				if(def.getEsMultivalor()){
					TipObjImpAtr tipObjImpAtr = TipObjImpAtr.getById(def.getTipObjImpAtr().getId());
					
					// borra los valores actuales de la BD para el tipObjImpAtr
					PadDAOFactory.getObjImpAtrValDAO().delete(altaOficio.getObjImp().getId(),
							tipObjImpAtr.getId());
					
					if(!rubroDreiActualizado)
						rubroDreiActualizado = true;

					log.debug("Iterando el multivalor");
					// inserta los nuevos valores 
					for(String s: def.getMultiValor()){
						
						ObjImpAtrVal objImpAtrVal = new ObjImpAtrVal();
						objImpAtrVal.setObjImp(altaOficio.getObjImp());
						objImpAtrVal.setTipObjImpAtr(tipObjImpAtr);
						objImpAtrVal.setFechaDesde(new Date());
						objImpAtrVal.setFechaNovedad(new Date());
						objImpAtrVal.setStrValor(s);
						objImpAtrVal = altaOficio.getObjImp().createObjImpAtrVal(objImpAtrVal);
												
						log.debug("Creo un objImpAtrVal:"+objImpAtrVal.getStrValor());
						
						// Verifica si el atributo actual es "rubro" y alguno de los rubros esta afectado a ETuR
						if(def.getAtributo().getCodAtributo().equals(Atributo.COD_RUBRO) && 
									aplicaETuR(s)){							
							aplicaETuR = true;
						}
					}
					log.debug("FIN Iterando el multivalor");
					
					
				}else{
					TipObjImpAtrVO tipObjImpAtr = def.getTipObjImpAtr();
					ObjImpAtrVal objImpAtrVal = ObjImpAtrVal.getVigenteByIdObjImpAtrVal(tipObjImpAtr.getId(), altaOficio.getObjImp().getId());
					log.debug("No es multivalor - valorString:"+def.getValorString());
					if(objImpAtrVal!=null){
						log.debug("objImpAtrVal.getId():"+(objImpAtrVal.getId()+"    "+objImpAtrVal.getTipObjImpAtr().getAtributo().getDesAtributo()));
						//borrar el registro si el valor es vacio
						if(StringUtil.isNullOrEmpty(def.getValorString())){
							PadDAOFactory.getObjImpAtrValDAO().delete(objImpAtrVal);							
						}else{
							// Si fecha de inicio de actividad cambio --> se actualiza la fecha de alta de las cuentas DReI y ETUR (si corresponde)
							if(def.getAtributo().getCodAtributo().trim().equals(Atributo.COD_FECHA_INICIO) &&
									!def.getValorString().equals(altaOficioAdapter.getFechaInicioActividadOriginal())){
								// guarda la fecha para la cuenta ETuR
								fechaInicioAct = DateUtil.getDate(def.getValorString(), DateUtil.YYYYMMDD_MASK);
								
								// Cuenta DReI
								Cuenta cuenta =Cuenta.getByIdRecursoYNumeroCuenta(Recurso.getDReI().getId(), 
										altaOficio.getObjImp().getClave());
								cuenta.setFechaAlta(fechaInicioAct);								
								PadCuentaManager.getInstance().updateCuenta(cuenta);								
							}
							
							objImpAtrVal.setStrValor(def.getValorString());
							objImpAtrVal = altaOficio.getObjImp().updateObjImpAtrVal(objImpAtrVal);
						}
					}else{
						log.debug("No encontro objImpAtrVal - lo va a crear");
						objImpAtrVal = new ObjImpAtrVal();
						objImpAtrVal.setObjImp(altaOficio.getObjImp());
						objImpAtrVal.setTipObjImpAtr(TipObjImpAtr.getById(tipObjImpAtr.getId()));
						objImpAtrVal.setFechaDesde(new Date());
						objImpAtrVal.setFechaNovedad(new Date());
						objImpAtrVal.setStrValor(def.getValorString());
						objImpAtrVal = altaOficio.getObjImp().createObjImpAtrVal(objImpAtrVal);					
					}
				}
			}
			log.debug("FIN iterando atributos----------------");
			
			log.debug("aplicaETuR:"+aplicaETuR);
			if(aplicaETuR){
				Cuenta cuenta =Cuenta.getByIdRecursoYNumeroCuenta(Recurso.getETur().getId(), 
														altaOficio.getObjImp().getClave());
				if(cuenta!=null){
					// Actualiza la fechaAlta de la cuenta
					cuenta.setFechaAlta(fechaInicioAct);								
					PadCuentaManager.getInstance().updateCuenta(cuenta);
					log.debug("Actualizo la cuenta ETuR");
				}else{
					// Crea la cuenta para ETuR
					cuenta = new Cuenta();
					cuenta.setRecurso(Recurso.getETur());
					cuenta.setObjImp(altaOficio.getObjImp());
					cuenta.setNumeroCuenta(altaOficio.getObjImp().getClave());
						// Genera el codigo de gestion personal. (generalmente sera un numero aleatorio) ### VER BIEN ###
					Long rndNumber = 1234567890L;
					String codGesCue = StringUtil.formatLong(rndNumber);
					cuenta.setCodGesCue(codGesCue);
					cuenta.setFechaAlta(fechaInicioAct);
					cuenta.setEstObjImp(EstObjImp.getById(EstObjImp.ID_EST_ALTA_OFICIO));
					cuenta.setEstado(Estado.CREADO.getId());		
							
					cuenta = PadCuentaManager.getInstance().createCuenta(cuenta);
					if(cuenta.hasError()){
						log.debug("ERRORES insertando la cuenta ETuR");
						tx.rollback();
						cuenta.passErrorMessages(altaOficioAdapter);
						return altaOficioAdapter;
					}
					log.debug("Creo la cuenta ETuR - erorres:"+cuenta.hasError());
				}
			}else{
				// Si tiene cuenta ETuR la borra porque no aplica ETuR (Se desseleccionaron todos los rubros que estaban a fectado por ETuR)
				Cuenta cuenta =Cuenta.getByIdRecursoYNumeroCuenta(Recurso.getETur().getId(), 
														altaOficio.getObjImp().getClave());
				if(cuenta!=null){
					PadCuentaManager.getInstance().deleteCuenta(cuenta);
					log.debug("Borro la cuenta ETuR");
				}				
			}
            tx.commit();                        
            
            session = SiatHibernateUtil.currentSession();
            tx = session.beginTransaction();
			
            ObjImp objImpComercio = altaOficio.getObjImp();
            
            // Replica de los atibutos con valores por defecto...
            List<TipObjImpAtr>  listTipObjImpAtr = TipObjImpAtr.getList(TipObjImp.COMERCIO, true, true,
            		Estado.ACTIVO);
            
            for(TipObjImpAtr tipObjImpAtr:listTipObjImpAtr){
            	TipObjImpAtr tipObjImpAtr1 = TipObjImpAtr.getById(new Long(tipObjImpAtr.getValorDefecto()));
            	// Obtiene el objImpAtrVal a modificar
            	ObjImpAtrVal objImpAtrValAMod =  ObjImpAtrVal.getVigenteByIdObjImpAtrVal(tipObjImpAtr.getId(),
            									objImpComercio.getId());
				
            	// Obtiene el valor que se le cargara
            	ObjImpAtrVal objImpAtrValComercio =  ObjImpAtrVal.getVigenteByIdObjImpAtrVal(tipObjImpAtr1.getId(), 
            									objImpComercio.getId());            
            	
            	if(objImpAtrValAMod!=null && objImpComercio!=null){
	            	objImpAtrValAMod.setObjImp(objImpComercio);            	
					objImpAtrValAMod.setTipObjImpAtr(tipObjImpAtr);
					objImpAtrValAMod.setFechaDesde(objImpAtrValComercio.getFechaDesde());
					objImpAtrValAMod.setStrValor(objImpAtrValComercio.getStrValor());
					objImpAtrValAMod = objImpComercio.updateObjImpAtrVal(objImpAtrValAMod);					
            	}
            }
            tx.commit();        
                           
            //session = SiatHibernateUtil.currentSession();
            tx = session.beginTransaction();
            //objImpComercio = altaOficio.getObjImp();
            
    		// Si la bandera 'rubroDreiActualizado' es true, significa que se modifico el atributo rubro de comercio.
    		if(rubroDreiActualizado){
    			// Si se el recurso principal es Drei, se verifica
    			// Calcular valor de atributo de asentamiento para Recurso DRei (Determina la distribucion de partidas para DRei y depende de los rubros habilitados)
    			Recurso recPri = objImpComercio.getTipObjImp().getRecursoPrincipal();
    			if(recPri != null && recPri.getAtributoAse() != null && Recurso.COD_RECURSO_DReI.equals(recPri.getCodRecurso())){
    				TipObjImpAtr tipObjImpAtrAse = TipObjImpAtr.getByIdAtributo(recPri.getAtributoAse().getId());
    				TipObjImpDefinition toidRubros = objImpComercio.getDefinitionValue(TipObjImpAtr.ID_RUBROS_TIPO_COMERCIO);
    				TipObjImpAtrDefinition toidAtrRubros = null;
    				if(!ListUtil.isNullOrEmpty(toidRubros.getListTipObjImpAtrDefinition()) && toidRubros.getListTipObjImpAtrDefinition().size() == 1)
    					toidAtrRubros = toidRubros.getListTipObjImpAtrDefinition().get(0);
    				// El valor por defecto es cero. (Distribucion general)
    				String atrAseVal = "0";
    				if(toidAtrRubros != null){
    					List<String> listRubros = toidAtrRubros.getMultiValor(new Date());
    					// Si el comercio se encuentra habilitado como "Bingo" (rubro: rrrr), le corresponde valor 1
    					String rubroBingo = SiatParam.getString(SiatParam.RUBRO_BINGO_DREI);
    					if(!StringUtil.isNullOrEmpty(rubroBingo)){
    						for(String rubro: listRubros){
    							if(rubroBingo.equals(rubro)){
    								atrAseVal = "1";
    								break;
    							}
    						}					
    					}
    					// Si el comercio no se encuentra habilitado como "Bingo" pero si en alguno de los rubros: rrrr1,rrrr2,rrrr3,rrrr4. Le corresponde valor 2
    					String listRubrosAseStr = SiatParam.getString(SiatParam.LISTA_RUBROS_DREI);
    					if(!"1".equals(atrAseVal) && !StringUtil.isNullOrEmpty(listRubrosAseStr)){
    						for(String rubro: listRubros){
    							rubro = "|" + rubro + "|";
    							if (listRubrosAseStr.indexOf(rubro) >= 0) {
    								atrAseVal = "2";
    								break;								
    							}
    						}	
    					}
    					
    					// Guardar el valor de atributo de asentamiento para Drei
    					ObjImpAtrVal objImpAtrVal = ObjImpAtrVal.getVigenteByIdObjImpAtrVal(tipObjImpAtrAse.getId(), objImpComercio.getId());
    					if(objImpAtrVal == null)
    						objImpAtrVal = new ObjImpAtrVal();
    					objImpAtrVal.setObjImp(objImpComercio);
    					objImpAtrVal.setTipObjImpAtr(tipObjImpAtrAse);
    					objImpAtrVal.setStrValor(atrAseVal);
    					objImpAtrVal.setFechaDesde(objImpComercio.getFechaAlta());
    					objImpAtrVal.setFechaHasta(null);
    					objImpAtrVal.setFechaNovedad(objImpComercio.getFechaAlta());
    					
    					objImpComercio.createObjImpAtrVal(objImpAtrVal);	
    				}
    			}
    		}
    	    tx.commit();      
    	    
		    return altaOficioAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Verifica si el codigo del rubro pasado como parametro esta afectado a ETuR
	 * @param codRubro
	 * @return
	 */
	private boolean aplicaETuR(String codRubro){
		List<RecGenCueAtrVa> listByStrValor = RecGenCueAtrVa.getListByStrValor(codRubro);
		if(listByStrValor!=null && !listByStrValor.isEmpty())
			return true;
		return false;
	}
	
	public AltaOficioSearchPage getAltaOficioSearchPageParamCuenta(UserContext userContext,
			AltaOficioSearchPage altaOficioSearchPage) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			altaOficioSearchPage.clearError();

			//Aqui realizar validaciones

			Cuenta cuenta = Cuenta.getById(altaOficioSearchPage.getCuenta().getId());
			
			altaOficioSearchPage.setCuenta((CuentaVO) cuenta.toVO(0, false));
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return altaOficioSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AltaOficioVO activarCuentaAltaOficio(UserContext userContext, AltaOficioVO altaOficioVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			AltaOficio altaOficio = AltaOficio.getById(altaOficioVO.getId());
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(Recurso.getDReI().getId(), altaOficio.getObjImp().getClave());
			cuenta.activar();

            if (cuenta.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				altaOficioVO =  (AltaOficioVO) altaOficio.toVO();
			}
            altaOficio.passErrorMessages(altaOficioVO);
            
            log.debug(funcName + ": exit");
            return altaOficioVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AltaOficioVO desactivarCuentaAltaOficio(UserContext userContext, AltaOficioVO altaOficioVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			AltaOficio altaOficio = AltaOficio.getById(altaOficioVO.getId());

			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(Recurso.getDReI().getId(), altaOficio.getObjImp().getClave());
			cuenta.setEstado(Estado.INACTIVO.getId());
			cuenta = PadCuentaManager.getInstance().updateCuenta(cuenta);
			
			cuenta.passErrorMessages(altaOficio);
			
            if (altaOficio.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				altaOficioVO =  (AltaOficioVO) altaOficio.toVO();
			}
            altaOficio.passErrorMessages(altaOficioVO);
            
            log.debug(funcName + ": exit");
            return altaOficioVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	
	// <--- ABM AltaOficio

	
	// ---> ABM CierreComercio
	public CierreComercioSearchPage getCierreComercioSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
		
			CierreComercioSearchPage cierreComercioSearchPage = new CierreComercioSearchPage();

			// Cargamos la lista de Sistema Origen
			cierreComercioSearchPage.setListSistemaOrigen(CasoCache.getInstance().getListSistemaOrigen());
					
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cierreComercioSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public CierreComercioSearchPage getCierreComercioSearchPageResult(UserContext userContext, CierreComercioSearchPage cierreComercioSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			cierreComercioSearchPage.clearError();

			//Aqui realizar validaciones
			if(!StringUtil.isNullOrEmpty(cierreComercioSearchPage.getCierreComercio().getCuentaVO().getNumeroCuenta())){
				Recurso recurso = Recurso.getByCodigo(Recurso.COD_RECURSO_DReI);
				
				Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(recurso.getId(),
						cierreComercioSearchPage.getCierreComercio().getCuentaVO().getNumeroCuenta());
				
				if(cuenta != null) {
					cierreComercioSearchPage.getCierreComercio().getCuentaVO().getObjImp().setId(cuenta.getObjImp().getId());
	
				} else {
					cierreComercioSearchPage.addRecoverableValueError("No existe la cuenta buscada");
					
				}
			}
			
			
			if(cierreComercioSearchPage.hasError()) return(cierreComercioSearchPage);
			
			// Aqui obtiene lista de BOs			
			List<CierreComercio> listCierreComercio = GdeDAOFactory.getCierreComercioDAO().getBySearchPage(cierreComercioSearchPage);
			
			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
		
			//Aqui pasamos BO a VO
	   		
	   		List<CierreComercioVO> listCierreComercioVO = new ArrayList<CierreComercioVO>();
			
			for(CierreComercio c: listCierreComercio){
				CierreComercioVO cierreComercioVO = (CierreComercioVO)c.toVO(0);
				Cuenta cuenta = c.getObjImp().getCuentaByRecurso(Recurso.getDReI());
				if (cuenta!=null)
					cierreComercioVO.setCuentaVO((CuentaVO) cuenta.toVO(0));
								
				cierreComercioVO.setIdCaso(c.getIdCaso());
				
				listCierreComercioVO.add(cierreComercioVO); 
			}
				
			cierreComercioSearchPage.setListResult(listCierreComercioVO);
						
	   		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return cierreComercioSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	// <--- ABM CierreComercio

	
}
