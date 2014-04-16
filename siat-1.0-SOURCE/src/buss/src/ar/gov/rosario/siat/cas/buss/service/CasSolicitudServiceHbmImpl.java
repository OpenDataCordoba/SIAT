//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.service;

/**
 * Implementacion de servicios del submodulo Solicitud del modulo Cas
 * @author tecso
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.AccionExp;
import ar.gov.rosario.siat.cas.buss.bean.AreaSolicitud;
import ar.gov.rosario.siat.cas.buss.bean.CasSolicitudManager;
import ar.gov.rosario.siat.cas.buss.bean.EstSolicitud;
import ar.gov.rosario.siat.cas.buss.bean.Solicitud;
import ar.gov.rosario.siat.cas.buss.bean.SolicitudCUIT;
import ar.gov.rosario.siat.cas.buss.bean.TipoSolicitud;
import ar.gov.rosario.siat.cas.buss.dao.CasDAOFactory;
import ar.gov.rosario.siat.cas.iface.model.AreaSolicitudAdapter;
import ar.gov.rosario.siat.cas.iface.model.AreaSolicitudSearchPage;
import ar.gov.rosario.siat.cas.iface.model.AreaSolicitudVO;
import ar.gov.rosario.siat.cas.iface.model.EstSolicitudVO;
import ar.gov.rosario.siat.cas.iface.model.SolicitudAdapter;
import ar.gov.rosario.siat.cas.iface.model.SolicitudCUITVO;
import ar.gov.rosario.siat.cas.iface.model.SolicitudSearchPage;
import ar.gov.rosario.siat.cas.iface.model.SolicitudVO;
import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudAdapter;
import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudSearchPage;
import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudVO;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cas.iface.service.ICasSolicitudService;
import ar.gov.rosario.siat.cas.iface.util.CasError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.UserContext;

public class CasSolicitudServiceHbmImpl implements ICasSolicitudService {
	private Logger log = Logger.getLogger(CasSolicitudServiceHbmImpl.class);
	
	// ---> ABM Solicitud 	
	public SolicitudSearchPage getSolicitudSearchPageInit(UserContext usercontext) throws DemodaServiceException {		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {

			SolicitudSearchPage solicitudSearchPage = new SolicitudSearchPage();

			// recupero la lista de tipos de solicitud
			List<TipoSolicitud> listTipoSolicitud = TipoSolicitud.getListActivos(); 
			List<TipoSolicitudVO> listTipoSolicitudVO = (ArrayList<TipoSolicitudVO>)ListUtilBean.toVO
				(listTipoSolicitud, new TipoSolicitudVO(-1,StringUtil.SELECT_OPCION_TODOS));

			// recupero la lista de estados de solicitud
			List<EstSolicitud> listEstSolicitud = EstSolicitud.getListActivos(); 
			List<EstSolicitudVO> listEstSolicitudVO = (ArrayList<EstSolicitudVO>)ListUtilBean.toVO
				(listEstSolicitud, new EstSolicitudVO(-1,StringUtil.SELECT_OPCION_TODOS));

			// recupero la lista de estados de solicitud
			List<Area> listArea = Area.getListActivas(); 
			List<AreaVO> listAreaVO = (ArrayList<AreaVO>)ListUtilBean.toVO
				(listArea, new AreaVO(-1,StringUtil.SELECT_OPCION_TODOS));

			// recupero una lista de areas
			solicitudSearchPage.setListTipoSolicitud(listTipoSolicitudVO);
			solicitudSearchPage.setListEstSolicitud(listEstSolicitudVO);
			solicitudSearchPage.setListArea(listAreaVO);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return solicitudSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	public SolicitudSearchPage getSolicitudSearchPageResult(UserContext userContext, SolicitudSearchPage solicitudSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			solicitudSearchPage.clearError();

			String nroCuenta = solicitudSearchPage.getSolicitud().getCuenta().getNumeroCuenta();
			if(!StringUtil.isNullOrEmpty(nroCuenta) && NumberUtil.getLong(nroCuenta)==null){
				solicitudSearchPage.addRecoverableError(CasError.SOLICITUD_PEND_NROCUENTA_FORMATOINCORRECTO);
				return solicitudSearchPage;
			}
			
			if(solicitudSearchPage.getEstaEnSolPendiente()) {
				solicitudSearchPage.getSolicitud().getAreaDestino().setId(userContext.getIdArea());
				solicitudSearchPage.getSolicitud().getAreaOrigen().setId(null);
				solicitudSearchPage.getSolicitud().getEstSolicitud().setId(EstSolicitud.ID_PENDIENTE);
			}
			
			
			if(solicitudSearchPage.getEstaEnSolEmitidas()) {
				solicitudSearchPage.getSolicitud().getAreaDestino().setId(null);
				solicitudSearchPage.getSolicitud().getAreaOrigen().setId(userContext.getIdArea());
				solicitudSearchPage.getSolicitud().getEstSolicitud().setId(EstSolicitud.ID_PENDIENTE);
			}
			
			// Aqui obtiene lista de BOs
	   		List<Solicitud> listSolicitud = CasDAOFactory.getSolicitudDAO().getBySearchPage(solicitudSearchPage);  
	   			
	   		
	   		
			//Aqui pasamos BO a VO
	   		solicitudSearchPage.setListResult(ListUtilBean.toVO(listSolicitud,1));
	   		
	   		// Recuperar usuarioSiat de userContext.idUsuariosSiat.
	   		
	   		
	   		// Setear permisos segun usuario alta y area
	   		String usrActual = userContext.getUserName().trim();
	   		for(SolicitudVO sol:(ArrayList<SolicitudVO>)solicitudSearchPage.getListResult()){	   			
	   			//Si el usuario logueado es el creador de la solicitud y está en estado PENDIENTE, se permite modificar y eliminar la misma
	   			if(sol.getUsuarioAlta()!=null && sol.getUsuarioAlta().trim().equals(usrActual) 
	   					&& sol.getEstSolicitud().getId().longValue() == EstSolicitud.ID_PENDIENTE.longValue()){
	   				sol.setModificarBussEnabled(true);
	   				sol.setEliminarBussEnabled(true);
	   			}else{
	   				sol.setModificarBussEnabled(false);
	   				sol.setEliminarBussEnabled(false);
	   			}
	   			//Si el area destino es la misma que la del usuario logueado y está en estado PENDIENTE, se permite modificar el estado de la solicitud
	   			if(sol.getAreaDestino().getId().equals(userContext.getIdArea()) &&
	   					(sol.getEstSolicitud().getId().equals(EstSolicitud.ID_PENDIENTE) || sol.getEstSolicitud().getId().equals(EstSolicitud.ID_APROBADO)) ) {
	   				sol.setCambiarEstadoBussEnabled(true);
	   			}else{
	   				sol.setCambiarEstadoBussEnabled(false);
	   			}	   			
	   		}
	   		
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return solicitudSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SolicitudSearchPage getSolicitudSearchPagePendArea(UserContext userContext, SolicitudSearchPage solicitudSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();			
			
			solicitudSearchPage.clearError();
			
			// llena la lista de tipoSolicitud
			List<TipoSolicitud> listTipoSolicitud = TipoSolicitud.getListActivos(); 
			List<TipoSolicitudVO> listTipoSolicitudVO = (ArrayList<TipoSolicitudVO>)ListUtilBean.toVO
				(listTipoSolicitud, new TipoSolicitudVO(-1,StringUtil.SELECT_OPCION_TODOS));
			solicitudSearchPage.setListTipoSolicitud(listTipoSolicitudVO);
			
			// Obtiene lista de solicitudes pendientes y "fallo actualizacion"
				// Setea las propiedades para obtener las del area del usuario logueado			
				solicitudSearchPage.getSolicitud().getAreaDestino().setId(userContext.getIdArea());
				solicitudSearchPage.getSolicitud().getAreaOrigen().setId(null);
//solicitudSearchPage.getSolicitud().getEstSolicitud().setId(EstSolicitud.ID_PENDIENTE);
				
				List<Solicitud> listSolicitudPendientes = CasDAOFactory.getSolicitudDAO().getPendientesArea(solicitudSearchPage);  
				
				//Aqui pasamos BO a VO
				solicitudSearchPage.setListResult(ListUtilBean.toVO(listSolicitudPendientes,1));					

			//Si no trajo resultados no muestra el formulario para filtrar solicitudes
			if(solicitudSearchPage.getListResult().isEmpty())
				solicitudSearchPage.setMostrarFiltro(false);
			else
				solicitudSearchPage.setMostrarFiltro(true);
				
	   		// Setear permisos segun usuario alta y area
	   		setearPermisosSolicitudes(userContext, solicitudSearchPage.getListResult());
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return solicitudSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SolicitudSearchPage getSolicitudSearchPageEmitidasArea(UserContext userContext, SolicitudSearchPage solicitudSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();			
			
			// llena la lista de tipoSolicitud
			List<TipoSolicitud> listTipoSolicitud = TipoSolicitud.getListActivos(); 
			List<TipoSolicitudVO> listTipoSolicitudVO = (ArrayList<TipoSolicitudVO>)ListUtilBean.toVO
				(listTipoSolicitud, new TipoSolicitudVO(-1,StringUtil.SELECT_OPCION_TODOS));
			solicitudSearchPage.setListTipoSolicitud(listTipoSolicitudVO);

			// Obtiene lista de solicitudes pendientes
				// Setea las propiedades para obtener las emitidas por el area del usuario logueado			
				solicitudSearchPage.getSolicitud().getAreaDestino().setId(null);
				solicitudSearchPage.getSolicitud().getAreaOrigen().setId(userContext.getIdArea());
				solicitudSearchPage.getSolicitud().getEstSolicitud().setId(EstSolicitud.ID_PENDIENTE);
				
			List<Solicitud> listSolicitudPendientes = CasDAOFactory.getSolicitudDAO().getBySearchPage(solicitudSearchPage);  
				
			//Aqui pasamos BO a VO
			solicitudSearchPage.setListResult(ListUtilBean.toVO(listSolicitudPendientes,1));					
			
			//Si no trajo resultados no muestra el formulario para filtrar solicitudes
			if(solicitudSearchPage.getListResult().isEmpty())
				solicitudSearchPage.setMostrarFiltro(false);
			else
				solicitudSearchPage.setMostrarFiltro(true);

	   		// Setear permisos segun usuario alta y area
	   		setearPermisosSolicitudes(userContext, solicitudSearchPage.getListResult());
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return solicitudSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public SolicitudSearchPage getSolicitudSearchPageParamCuenta(UserContext userContext, SolicitudSearchPage solicitudSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			solicitudSearchPage.clearError();
			
			Cuenta cuenta = Cuenta.getById(solicitudSearchPage.getSolicitud().getCuenta().getId());
			solicitudSearchPage.getSolicitud().setCuenta((CuentaVO) cuenta.toVO(0, false));
			
			log.debug(funcName + ": exit");
			return solicitudSearchPage;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}


	/**
	 * Setea permisos segun usuario alta y area
	 * @param userContext
	 * @param listSolicitud
	 */
	private void setearPermisosSolicitudes(UserContext userContext, List<SolicitudVO> listSolicitud){
   		String usrActual = userContext.getUserName().trim();
   		for(SolicitudVO sol:(ArrayList<SolicitudVO>)listSolicitud){
   			//Si el usuario logueado es el creador de la solicitud y está en estado PENDIENTE, se permite modificar y eliminar la misma
   			if(sol.getUsuarioAlta()!=null && sol.getUsuarioAlta().trim().equals(usrActual) 
   					&& sol.getEstSolicitud().getId().longValue() == EstSolicitud.ID_PENDIENTE.longValue()){
   				sol.setModificarBussEnabled(true);
   				sol.setEliminarBussEnabled(true);
   			}else{
   				sol.setModificarBussEnabled(false);
   				sol.setEliminarBussEnabled(false);
   			}
   			//Si el area destino es la misma que la del usuario logueado y está en estado PENDIENTE O FALLO ACT, se permite modificar el estado de la solicitud
   			if(sol.getAreaDestino().getId().equals(userContext.getIdArea())
   					&& (sol.getEstSolicitud().getId().equals(EstSolicitud.ID_PENDIENTE) ||
   						sol.getEstSolicitud().getId().equals(EstSolicitud.ID_FALLO_ACTUALIZ))) {
   				sol.setCambiarEstadoBussEnabled(true);
   			}else{
   				sol.setCambiarEstadoBussEnabled(false);
   			}	   			
   		}
	}
	
	public SolicitudAdapter getSolicitudAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Solicitud solicitud = Solicitud.getById(commonKey.getId());

	        SolicitudAdapter solicitudAdapter = new SolicitudAdapter();
	        solicitudAdapter.setSolicitud((SolicitudVO) solicitud.toVO(1));
			
	        // Si tipo de Solicitud es Modificacion de Datos, llena la SolicitudCUIT (llenada cuando se creo el buzon de cambios)
	        if(solicitud.getTipoSolicitud().getCodigo().equals(TipoSolicitud.COD_MODIFICACION_IDENTIFICACION_PERSONA)){
	        	SolicitudCUIT solicitudCUIT = SolicitudCUIT.getBySolicitud(solicitud);
	        	solicitudAdapter.setSolicitudCUIT((SolicitudCUITVO) solicitudCUIT.toVO(1, false));

	        }

			log.debug(funcName + ": exit");
			return solicitudAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SolicitudAdapter getSolicitudAdapterForCreate(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			SolicitudAdapter solicitudAdapter = new SolicitudAdapter();
			
			// seteo la lista de tipos de solicitud
			List<TipoSolicitud> listTipoSolicitud = TipoSolicitud.getListActivos(); 
			List<TipoSolicitudVO> listTipoSolicitudVO = (ArrayList<TipoSolicitudVO>) ListUtilBean.toVO
				(listTipoSolicitud,1,new TipoSolicitudVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR));

			solicitudAdapter.setListTipoSolicitud(listTipoSolicitudVO);
			
			Solicitud solicitud = new Solicitud();
			solicitud.setInitValues();
			SolicitudVO solicitudVO = (SolicitudVO) solicitud.toVO(1); 
			
			//seteo la solicitud al adapter
			solicitudAdapter.setSolicitud(solicitudVO);

			log.debug(funcName + ": exit");
			return solicitudAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public SolicitudAdapter getSolicitudAdapterParamAreaDestino(UserContext userContext, SolicitudAdapter solicitudAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			solicitudAdapter.clearError();
			
			// Logica del param
			Long idTipoSol = solicitudAdapter.getSolicitud().getTipoSolicitud().getId();			
			
			if (idTipoSol == null || idTipoSol.equals(new Long(-1))){
				solicitudAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.TIPOSOLICITUD_LABEL);
				return solicitudAdapter;
			}
			
			TipoSolicitud tipoSolicitud = TipoSolicitud.getById(idTipoSol);
			
			// valida que no sea del tipo "modificacion de identif de persona"
			if(tipoSolicitud.getCodigo().trim().equals(TipoSolicitud.COD_MODIFICACION_IDENTIFICACION_PERSONA)){
				solicitudAdapter.addRecoverableError(CasError.TIPOSOLICITUD_MSG_NO_PERMITIDA);
				return solicitudAdapter;				
			}
			
			solicitudAdapter.getSolicitud().setAreaDestino((AreaVO)tipoSolicitud.getAreaDestino().toVO(1));
			
			
			log.debug(funcName + ": exit");
			return solicitudAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public SolicitudAdapter getSolicitudAdapterForUpdate(UserContext userContext, 
		CommonKey commonKeySolicitud) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Solicitud solicitud = Solicitud.getById(commonKeySolicitud.getId());

	        SolicitudAdapter solicitudAdapter = new SolicitudAdapter();
	        solicitudAdapter.setSolicitud((SolicitudVO) solicitud.toVO(1));

			// seteo la lista de tipos de solicitud
			List<TipoSolicitud> listTipoSolicitud = TipoSolicitud.getListActivos(); 
			List<TipoSolicitudVO> listTipoSolicitudVO = (ArrayList<TipoSolicitudVO>)ListUtilBean.toVO
				(listTipoSolicitud,1,new TipoSolicitudVO(-1,StringUtil.SELECT_OPCION_SELECCIONAR));

			solicitudAdapter.setListTipoSolicitud(listTipoSolicitudVO);

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return solicitudAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SolicitudVO createSolicitud(UserContext userContext, SolicitudVO solicitudVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			solicitudVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			Solicitud solicitud = new Solicitud();
			TipoSolicitud tipoSolicitud = TipoSolicitud.getByIdNull(solicitudVO.getTipoSolicitud().getId()); 
			
			// valida que no sea del tipo "modificacion de identif de persona"
			if(tipoSolicitud.getCodigo().trim().equals(TipoSolicitud.COD_MODIFICACION_IDENTIFICACION_PERSONA)){
				solicitudVO.addRecoverableError(CasError.TIPOSOLICITUD_MSG_NO_PERMITIDA);
				return solicitudVO;				
			}

			Area areaDestino = Area.getByIdNull(solicitudVO.getAreaDestino().getId());
			Area areaOrigen = Area.getByIdNull(solicitudVO.getAreaOrigen().getId());
			
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_SOLICITUD); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(solicitudVO, solicitud, 
        			accionExp, solicitud.getCuenta(), solicitud.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (solicitudVO.hasError()){
        		tx.rollback();
        		return solicitudVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	solicitud.setIdCaso(solicitudVO.getIdCaso());
			
			if(solicitudVO.getCuenta()!=null && solicitudVO.getCuenta().getId()!=null){
				Cuenta cuenta = Cuenta.getByIdNull(solicitudVO.getCuenta().getId());
				solicitud.setCuenta(cuenta);
			}			
			EstSolicitud estSolicitud = EstSolicitud.getByIdNull(EstSolicitud.ID_PENDIENTE);
			
			solicitud.setTipoSolicitud(tipoSolicitud);
			solicitud.setAsuntoSolicitud(solicitudVO.getAsuntoSolicitud());
			solicitud.setDescripcion(solicitudVO.getDescripcion());
			solicitud.setEstSolicitud(estSolicitud);						
			solicitud.setAreaDestino(areaDestino);
			solicitud.setUsuarioAlta(userContext.getUserName());
			solicitud.setAreaOrigen(areaOrigen);
			solicitud.addLogCreateSolicitud(userContext.getUserName());
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
			solicitud = CasSolicitudManager.getInstance().createSolicitud(solicitud);			            
			
			
            if (solicitud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				solicitudVO =  (SolicitudVO) solicitud.toVOForView();
			}
			solicitud.passErrorMessages(solicitudVO);
            
            log.debug(funcName + ": exit");
            return solicitudVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SolicitudVO updateSolicitud(UserContext userContext, SolicitudVO solicitudVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			solicitudVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
			TipoSolicitud tipoSolicitud = TipoSolicitud.getByIdNull(solicitudVO.getTipoSolicitud().getId());
			Area areaDestino = Area.getByIdNull(solicitudVO.getAreaDestino().getId());
			Cuenta cuenta = null;
			if(solicitudVO.getCuenta()!=null && solicitudVO.getCuenta().getId()!=null){
				cuenta = Cuenta.getByIdNull(solicitudVO.getCuenta().getId());
			}
            Solicitud solicitud = Solicitud.getById(solicitudVO.getId());
            
            if(!solicitudVO.validateVersion(solicitud.getFechaUltMdf())) return solicitudVO;
            
			// 1) Registro uso de expediente 
        	AccionExp accionExp = AccionExp.getById(AccionExp.ID_CREACION_SOLICITUD); 
        	CasServiceLocator.getCasCasoService().registrarUsoExpediente(solicitudVO, solicitud, 
        			accionExp, solicitud.getCuenta(), solicitud.infoString() );
        	// Si no pasa la validacion, vuelve a la vista. 
        	if (solicitudVO.hasError()){
        		tx.rollback();
        		return solicitudVO;
        	}
        	// 2) Esta linea debe ir siempre despues de 1).
        	solicitud.setIdCaso(solicitudVO.getIdCaso());
        	
            solicitud.setTipoSolicitud(tipoSolicitud);
            solicitud.setAreaDestino(areaDestino);
            solicitud.setAsuntoSolicitud(solicitudVO.getAsuntoSolicitud());
            solicitud.setDescripcion(solicitudVO.getDescripcion());
            solicitud.setCuenta(cuenta);            
   
            // Aqui el update esta delegado en el manager, pero puede corresponder a un Bean contenedor
            solicitud = CasSolicitudManager.getInstance().updateSolicitud(solicitud);
            
            if (solicitud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				solicitudVO =  (SolicitudVO) solicitud.toVOForView();
			}
			solicitud.passErrorMessages(solicitudVO);
            
            log.debug(funcName + ": exit");
            return solicitudVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SolicitudVO deleteSolicitud(UserContext userContext, SolicitudVO solicitudVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			solicitudVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			Solicitud solicitud = Solicitud.getById(solicitudVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			solicitud = CasSolicitudManager.getInstance().deleteSolicitud(solicitud);
			
			if (solicitud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				solicitudVO =  (SolicitudVO) solicitud.toVOForView();
			}
			solicitud.passErrorMessages(solicitudVO);
            
            log.debug(funcName + ": exit");
            return solicitudVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SolicitudVO activarSolicitud(UserContext userContext, SolicitudVO solicitudVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Solicitud solicitud = Solicitud.getById(solicitudVO.getId());

            solicitud.activar();

            if (solicitud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				solicitudVO =  (SolicitudVO) solicitud.toVO();
			}
            solicitud.passErrorMessages(solicitudVO);
            
            log.debug(funcName + ": exit");
            return solicitudVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public SolicitudVO desactivarSolicitud(UserContext userContext, SolicitudVO solicitudVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            Solicitud solicitud = Solicitud.getById(solicitudVO.getId());

            solicitud.desactivar();

            if (solicitud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				solicitudVO =  (SolicitudVO) solicitud.toVO();
			}
            solicitud.passErrorMessages(solicitudVO);
            
            log.debug(funcName + ": exit");
            return solicitudVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	

	public SolicitudAdapter getSolicitudAdapterParamCuenta(UserContext userContext, SolicitudAdapter solicitudAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			solicitudAdapterVO.clearError();

			Long idCuenta = solicitudAdapterVO.getSolicitud().getCuenta().getId();

			// recupero la persona
			Cuenta cuenta = Cuenta.getByIdNull(idCuenta);
			CuentaVO cuentaVO = new CuentaVO();
			
			// si la persona
			if (cuenta != null) {
				cuentaVO = (CuentaVO) cuenta.toVO(0);
			}

			solicitudAdapterVO.getSolicitud().setCuenta(cuentaVO);
			
			log.debug(funcName + ": exit");
			return solicitudAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	// <--- ABM Solicitud

	public SolicitudAdapter getSolicitudAdapterForCambiarEstado(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			Solicitud solicitud = Solicitud.getById(commonKey.getId());

	        SolicitudAdapter solicitudAdapter = new SolicitudAdapter();
	        solicitudAdapter.setSolicitud((SolicitudVO) solicitud.toVO(1));
	        
	        //Esto se utiliza para controlar despues si se cambio el estado
			solicitudAdapter.getSolicitud().setIdEstSolicitudAnterior(solicitud.getEstSolicitud().getId());
			
			llenarEstados(solicitudAdapter);
	        solicitudAdapter.getSolicitud().getEstSolicitud().setId(-1L);
	        
	        // Si tipo de Solicitud es Modificacion de Datos, llena la SolicitudCUIT (llenada cuando se creo el buzon de cambios)
	        if(solicitud.getTipoSolicitud().getCodigo().equals(TipoSolicitud.COD_MODIFICACION_IDENTIFICACION_PERSONA)){
	        	SolicitudCUIT solicitudCUIT = SolicitudCUIT.getBySolicitud(solicitud);
	        	solicitudAdapter.setSolicitudCUIT((SolicitudCUITVO) solicitudCUIT.toVO(1, false));

	        	// Setea valores por defecto
	        	solicitudAdapter.getSolicitudCUIT().setearValoresDefecto();
	        }
	        
			log.debug(funcName + ": exit");
			return solicitudAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	/**
	 * Llena la lista de estados en el adapter dependiendo del tipo de solicitud. Si es del tipo 6 agrega "PENDIENTE", "RECHAZADO", "APROBADO".<br>
	 * Si no es 6 agrega "PENDIENTE", "RECHAZADO", "REALIZADO"
	 * @param solicitudAdapter
	 * @throws Exception 
	 */
	private void llenarEstados(SolicitudAdapter solicitudAdapter) throws Exception{
		List<EstSolicitudVO> listEstSolicitud = new ArrayList<EstSolicitudVO>();
		listEstSolicitud.add(new EstSolicitudVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
		
		if(solicitudAdapter.getSolicitud().getTipoSolicitud().getId().equals(6L)){
			listEstSolicitud.add((EstSolicitudVO)EstSolicitud.getById(EstSolicitud.ID_PENDIENTE).toVO(0, false));
			listEstSolicitud.add((EstSolicitudVO)EstSolicitud.getById(EstSolicitud.ID_RECHAZADO).toVO(0, false));
			listEstSolicitud.add((EstSolicitudVO)EstSolicitud.getById(EstSolicitud.ID_APROBADO).toVO(0, false));
			listEstSolicitud.add((EstSolicitudVO)EstSolicitud.getById(EstSolicitud.ID_REALIZADO).toVO(0, false));
		}else{
			listEstSolicitud.add((EstSolicitudVO)EstSolicitud.getById(EstSolicitud.ID_PENDIENTE).toVO(0, false));
			listEstSolicitud.add((EstSolicitudVO)EstSolicitud.getById(EstSolicitud.ID_REALIZADO).toVO(0, false));
			listEstSolicitud.add((EstSolicitudVO)EstSolicitud.getById(EstSolicitud.ID_RECHAZADO).toVO(0, false));
		}
		solicitudAdapter.setListEstSolicitud(listEstSolicitud);
	}
	
	public SolicitudAdapter cambiarEstado(UserContext userContext, SolicitudAdapter solicitudAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			solicitudAdapter.clearErrorMessages();
			// Controla que se haya cambiado el estado
			/*long idEstSolAnterior = solicitudAdapter.getSolicitud().getIdEstSolicitudAnterior();
			long idEstNuevo = solicitudAdapter.getSolicitud().getEstSolicitud().getId();
			if ((idEstSolAnterior == idEstNuevo) || idEstNuevo<=0){
				String[] msg = {CasError.ESTSOLICITUD_LABEL,solicitudAdapter.getSolicitud().getEstSolicitud().getDescripcion()};
				solicitudAdapter.addRecoverableError(BaseError.MSG_IGUALQUE, msg);
				return solicitudAdapter;
			}*/

			if(solicitudAdapter.getSolicitud().getEstSolicitud().getId().longValue()<=0){
				solicitudAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.ESTSOLICITUD_LABEL);
			}
			
			if(StringUtil.isNullOrEmpty(solicitudAdapter.getSolicitud().getObsestsolicitud())){
				solicitudAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.OBS_EST_SOLICITUD_LABEL);
			}
			
			if(solicitudAdapter.hasError())
				return solicitudAdapter;
			
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			
			EstSolicitud estSolicitud = EstSolicitud.getByIdNull(solicitudAdapter.getSolicitud().getEstSolicitud().getId());
            Solicitud solicitud = Solicitud.getById(solicitudAdapter.getSolicitud().getId());
            solicitud.setObsestsolicitud(solicitudAdapter.getSolicitud().getObsestsolicitud());
            solicitud.setEstSolicitud(estSolicitud);            
            solicitud.addLogCambiarEstadoSolicitud(userContext.getUserName());
            
            // Aqui la actualizacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            solicitud = CasSolicitudManager.getInstance().cambiarEstadoSolicitud(solicitud);
            
            if (solicitud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				
				if(solicitud.getTipoSolicitud().getCodigo().equals(TipoSolicitud.COD_MODIFICACION_IDENTIFICACION_PERSONA)&&
						estSolicitud.getId().equals(EstSolicitud.ID_APROBADO)){
					// actualiza la SolicitudCUIT
					SolicitudCUIT solicitudCUIT = SolicitudCUIT.getById(solicitudAdapter.getSolicitudCUIT().getId());
					solicitudCUIT.setApellidoAprobado(solicitudAdapter.getSolicitudCUIT().getApellidoAprobado());
					solicitudCUIT.setNombreAprobado(solicitudAdapter.getSolicitudCUIT().getNombreAprobado());
					solicitudCUIT.setRazonSocAprobado(solicitudAdapter.getSolicitudCUIT().getRazonSocAprobado());
					solicitudCUIT.setCuit01Aprobado(solicitudAdapter.getSolicitudCUIT().getCuit01Aprobado());
					solicitudCUIT.setCuit02Aprobado(solicitudAdapter.getSolicitudCUIT().getCuit02Aprobado());
					solicitudCUIT.setCuit03Aprobado(solicitudAdapter.getSolicitudCUIT().getCuit03Aprobado());
					
					CasSolicitudManager.getInstance().updateSolicitudCUIT(solicitudCUIT);
				}
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}				
			}
			
            log.debug(funcName + ": exit");
            return solicitudAdapter;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	//-----------------------------
	// ---> ABM TipoSolicitud 	

	public TipoSolicitudSearchPage getTipoSolicitudSearchPageInit(UserContext userSession) throws DemodaServiceException {
		return new TipoSolicitudSearchPage();
	}

	public TipoSolicitudSearchPage getTipoSolicitudSearchPageResult(UserContext userSession, TipoSolicitudSearchPage tipoSolicitudSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userSession);
			SiatHibernateUtil.currentSession();

			tipoSolicitudSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<TipoSolicitud> listTipoSolicitud = CasDAOFactory.getTipoSolicitudDAO().getBySearchPage(tipoSolicitudSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		tipoSolicitudSearchPage.setListResult(ListUtilBean.toVO(listTipoSolicitud,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return tipoSolicitudSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public TipoSolicitudAdapter getTipoSolicitudAdapterForUpdate(UserContext userSession, CommonKey commonKey)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userSession);
			SiatHibernateUtil.currentSession(); 
			
			TipoSolicitud tipoSolicitud = TipoSolicitud.getById(commonKey.getId());

	        TipoSolicitudAdapter tipoSolicitudAdapter = new TipoSolicitudAdapter();
	        tipoSolicitudAdapter.setTipoSolicitud((TipoSolicitudVO) tipoSolicitud.toVO(2));
	        
	        // Seteo la listas para el combo de Area Destino			
			List<Area> listAreaDestino = Area.getListActivas();
			tipoSolicitudAdapter.setListArea(
					(ArrayList<AreaVO>)ListUtilBean.toVO(listAreaDestino, 
					new AreaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return tipoSolicitudAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoSolicitudAdapter getTipoSolicitudAdapterForCreate(UserContext userSession) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userSession);
			SiatHibernateUtil.currentSession(); 
		
			TipoSolicitudAdapter tipoSolicitudAdapter = new TipoSolicitudAdapter();
			
			// Seteo de banderas
			
			// Seteo la listas para el combo de Area Destino			
			List<Area> listAreaDestino = Area.getListActivas();
			tipoSolicitudAdapter.setListArea(
					(ArrayList<AreaVO>)ListUtilBean.toVO(listAreaDestino, 
					new AreaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return tipoSolicitudAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public TipoSolicitudVO createTipoSolicitud(UserContext userContext,	TipoSolicitudVO tipoSolicitudVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoSolicitudVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            TipoSolicitud tipoSolicitud = new TipoSolicitud();

            Area areaDestino = null;
            if (tipoSolicitudVO.getAreaDestino().getId() != -1 ) {
            	areaDestino = Area.getById(tipoSolicitudVO.getAreaDestino().getId());
			}			
			
			tipoSolicitud.setCodigo(tipoSolicitudVO.getCodigo());
			tipoSolicitud.setDescripcion(tipoSolicitudVO.getDescripcion());
			tipoSolicitud.setAreaDestino(areaDestino);
			tipoSolicitud.setUsuarioUltMdf(tipoSolicitudVO.getUsuario());
			tipoSolicitud.setFechaUltMdf(tipoSolicitudVO.getFechaUltMdf());
            
            tipoSolicitud.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            tipoSolicitud = CasSolicitudManager.getInstance().createTipoSolicitud(tipoSolicitud);
            
            if (tipoSolicitud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoSolicitudVO =  (TipoSolicitudVO) tipoSolicitud.toVO(0,false);
			}
			tipoSolicitud.passErrorMessages(tipoSolicitudVO);
            
            log.debug(funcName + ": exit");
            return tipoSolicitudVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoSolicitudVO updateTipoSolicitud(UserContext userSession, TipoSolicitudVO tipoSolicitudVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userSession);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoSolicitudVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            TipoSolicitud tipoSolicitud = TipoSolicitud.getById(tipoSolicitudVO.getId());
			
			if(!tipoSolicitudVO.validateVersion(tipoSolicitud.getFechaUltMdf())) return tipoSolicitudVO;
						
            Area areaDestino = null;
            if (tipoSolicitudVO.getAreaDestino().getId() != -1 ) {
            	areaDestino = Area.getById(tipoSolicitudVO.getAreaDestino().getId());
			}
            
			tipoSolicitud.setCodigo(tipoSolicitudVO.getCodigo());
			tipoSolicitud.setDescripcion(tipoSolicitudVO.getDescripcion());
			tipoSolicitud.setAreaDestino(areaDestino);
			tipoSolicitud.setUsuarioUltMdf(tipoSolicitudVO.getUsuario());
			tipoSolicitud.setEstado(tipoSolicitudVO.getEstado().getId());
			tipoSolicitud.setFechaUltMdf(tipoSolicitudVO.getFechaUltMdf());						
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            tipoSolicitud = CasSolicitudManager.getInstance().updateTipoSolicitud(tipoSolicitud);
            
            if (tipoSolicitud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoSolicitudVO =  (TipoSolicitudVO) tipoSolicitud.toVO(0,false);
			}
			tipoSolicitud.passErrorMessages(tipoSolicitudVO);
            
            log.debug(funcName + ": exit");
            return tipoSolicitudVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoSolicitudVO deleteTipoSolicitud(UserContext userSession,	TipoSolicitudVO tipoSolicitudVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userSession);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			tipoSolicitudVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			TipoSolicitud tipoSolicitud = TipoSolicitud.getById(tipoSolicitudVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			tipoSolicitud = CasSolicitudManager.getInstance().deleteTipoSolicitud(tipoSolicitud);
			
			if (tipoSolicitud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoSolicitudVO =  (TipoSolicitudVO) tipoSolicitud.toVO(0,false);
			}
			tipoSolicitud.passErrorMessages(tipoSolicitudVO);
            
            log.debug(funcName + ": exit");
            return tipoSolicitudVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoSolicitudVO activarTipoSolicitud(UserContext userContext,TipoSolicitudVO tipoSolicitudVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            TipoSolicitud tipoSolicitud = TipoSolicitud.getById(tipoSolicitudVO.getId());

            tipoSolicitud.activar();

            if (tipoSolicitud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoSolicitudVO =  (TipoSolicitudVO) tipoSolicitud.toVO(0,false);
			}
            tipoSolicitud.passErrorMessages(tipoSolicitudVO);
            
            log.debug(funcName + ": exit");
            return tipoSolicitudVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoSolicitudVO desactivarTipoSolicitud(UserContext userSession,	TipoSolicitudVO tipoSolicitudVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		      
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userSession);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            TipoSolicitud tipoSolicitud = TipoSolicitud.getById(tipoSolicitudVO.getId());
                           
            tipoSolicitud.desactivar();

            if (tipoSolicitud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				tipoSolicitudVO =  (TipoSolicitudVO) tipoSolicitud.toVO(0,false);
			}
            tipoSolicitud.passErrorMessages(tipoSolicitudVO);
            
            log.debug(funcName + ": exit");
            return tipoSolicitudVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	       
	
	public TipoSolicitudAdapter imprimirTipoSolicitud(UserContext userSession, TipoSolicitudAdapter tipoSolicitudAdapterVO)	throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userSession);
			SiatHibernateUtil.currentSession(); 
			
			TipoSolicitud tipoSolicitud = TipoSolicitud.getById(tipoSolicitudAdapterVO.getTipoSolicitud().getId());

			CasDAOFactory.getTipoSolicitudDAO().imprimirGenerico(tipoSolicitud, tipoSolicitudAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return tipoSolicitudAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}

	public TipoSolicitudAdapter getTipoSolicitudAdapterForView(UserContext userSession, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userSession);
			SiatHibernateUtil.currentSession(); 
			
			TipoSolicitud tipoSolicitud = TipoSolicitud.getById(commonKey.getId());

	        TipoSolicitudAdapter tipoSolicitudAdapter = new TipoSolicitudAdapter();
	        tipoSolicitudAdapter.setTipoSolicitud((TipoSolicitudVO) tipoSolicitud.toVO(2));
			
			log.debug(funcName + ": exit");
			return tipoSolicitudAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public TipoSolicitudAdapter getTipoSolicitudAdapterParam(UserContext userSession, TipoSolicitudAdapter tipoSolicitudAdapterVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userSession);
			SiatHibernateUtil.currentSession();
			
			tipoSolicitudAdapterVO.clearError();
			
			// Logica del param
					
			log.debug(funcName + ": exit");
			return tipoSolicitudAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}
	
	// <--- ABM TipoSolicitud

	
	// ---> ABM AreaSolicitud 	
	public AreaSolicitudSearchPage getAreaSolicitudSearchPageInit(UserContext userContext) throws DemodaServiceException {		
		return new AreaSolicitudSearchPage();
	}

	public AreaSolicitudSearchPage getAreaSolicitudSearchPageResult(UserContext userContext, AreaSolicitudSearchPage areaSolicitudSearchPage) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			areaSolicitudSearchPage.clearError();

			//Aqui realizar validaciones

			// Aqui obtiene lista de BOs
	   		List<AreaSolicitud> listAreaSolicitud = CasDAOFactory.getAreaSolicitudDAO().getBySearchPage(areaSolicitudSearchPage);  

			//Aqui se podria iterar la lista de BO para setear banderas en VOs y otras cosas del negocio.
	   		
			//Aqui pasamos BO a VO
	   		areaSolicitudSearchPage.setListResult(ListUtilBean.toVO(listAreaSolicitud,0));
	   		
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return areaSolicitudSearchPage;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AreaSolicitudAdapter getAreaSolicitudAdapterForView(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			AreaSolicitud areaSolicitud = AreaSolicitud.getById(commonKey.getId());

	        AreaSolicitudAdapter areaSolicitudAdapter = new AreaSolicitudAdapter();
	        areaSolicitudAdapter.setAreaSolicitud((AreaSolicitudVO) areaSolicitud.toVO(1));
			
			log.debug(funcName + ": exit");
			return areaSolicitudAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AreaSolicitudAdapter getAreaSolicitudAdapterForCreate(UserContext userContext, CommonKey commonKey) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
		
			AreaSolicitudAdapter areaSolicitudAdapter = new AreaSolicitudAdapter();
			TipoSolicitud tipoSolicitud = TipoSolicitud.getById(commonKey.getId());			
			AreaSolicitudVO areaSolicitudVO = new AreaSolicitudVO();
			
			areaSolicitudVO.setTipoSolicitud((TipoSolicitudVO)tipoSolicitud.toVO(1));
			areaSolicitudAdapter.setAreaSolicitud(areaSolicitudVO);
						
			// Seteo la listas para el combo de Area Destino			
			List<Area> listAreaDestino = Area.getListActivas();
			areaSolicitudAdapter.setListArea(
					(ArrayList<AreaVO>)ListUtilBean.toVO(listAreaDestino, 
					new AreaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			
			log.debug(funcName + ": exit");
			return areaSolicitudAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public AreaSolicitudAdapter getAreaSolicitudAdapterParam(UserContext userContext, AreaSolicitudAdapter areaSolicitudAdapter) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			areaSolicitudAdapter.clearError();
			
			// Logica del param
			
			
			
			log.debug(funcName + ": exit");
			return areaSolicitudAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}	
	}

	public AreaSolicitudAdapter getAreaSolicitudAdapterForUpdate(UserContext userContext, CommonKey commonKeyAreaSolicitud) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			AreaSolicitud areaSolicitud = AreaSolicitud.getById(commonKeyAreaSolicitud.getId());
			
	        AreaSolicitudAdapter areaSolicitudAdapter = new AreaSolicitudAdapter();
	        areaSolicitudAdapter.setAreaSolicitud((AreaSolicitudVO) areaSolicitud.toVO(1));       
	        

			// Seteo la lista para combo, valores, etc
			
			log.debug(funcName + ": exit");
			return areaSolicitudAdapter;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AreaSolicitudVO createAreaSolicitud(UserContext userContext, AreaSolicitudVO areaSolicitudVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			areaSolicitudVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            AreaSolicitud areaSolicitud = new AreaSolicitud();
              
			//Obtengo el Tipo de Solicitud
			TipoSolicitud tipoSolicitud = TipoSolicitud.getById(areaSolicitudVO.getTipoSolicitud().getId());
			//Obtengo el Area de Destino
			Area areaDestino = Area.getById(areaSolicitudVO.getAreaDestino().getId());
									
			areaSolicitud.setTipoSolicitud(tipoSolicitud);
			areaSolicitud.setAreaDestino(areaDestino);
			areaSolicitud.setUsuarioUltMdf(areaSolicitudVO.getUsuario());
			areaSolicitud.setFechaUltMdf(areaSolicitudVO.getFechaUltMdf());            
            areaSolicitud.setEstado(Estado.ACTIVO.getId());
            
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            areaSolicitud = CasSolicitudManager.getInstance().createAreaSolicitud(areaSolicitud);
            
            if (areaSolicitud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				areaSolicitudVO =  (AreaSolicitudVO) areaSolicitud.toVO(0,false);
			}
			areaSolicitud.passErrorMessages(areaSolicitudVO);
            
            log.debug(funcName + ": exit");
            return areaSolicitudVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AreaSolicitudVO updateAreaSolicitud(UserContext userContext, AreaSolicitudVO areaSolicitudVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			areaSolicitudVO.clearErrorMessages();
			
			// Copiado de propiadades de VO al BO
            AreaSolicitud areaSolicitud = AreaSolicitud.getById(areaSolicitudVO.getId());
			
			if(!areaSolicitudVO.validateVersion(areaSolicitud.getFechaUltMdf())) return areaSolicitudVO;
			
			//Obtengo el Tipo de Solicitud
			TipoSolicitud tipoSolicitud = TipoSolicitud.getById(areaSolicitudVO.getTipoSolicitud().getId());
			//Obtengo el Area de Destino
			Area areaDestino = Area.getById(areaSolicitudVO.getAreaDestino().getId());
			
			areaSolicitud.setUsuarioUltMdf(areaSolicitudVO.getUsuario());
			areaSolicitud.setTipoSolicitud(tipoSolicitud);
			areaSolicitud.setAreaDestino(areaDestino);
			areaSolicitud.setFechaUltMdf(areaSolicitudVO.getFechaUltMdf());
			areaSolicitud.setEstado(areaSolicitudVO.getEstado().getId());
   
            // Aqui la creacion esta delegada en el manager, pero puede corresponder a un Bean contenedor
            areaSolicitud = CasSolicitudManager.getInstance().updateAreaSolicitud(areaSolicitud);
            
            if (areaSolicitud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				areaSolicitudVO =  (AreaSolicitudVO) areaSolicitud.toVO(0,false);
			}
			areaSolicitud.passErrorMessages(areaSolicitudVO);
            
            log.debug(funcName + ": exit");
            return areaSolicitudVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AreaSolicitudVO deleteAreaSolicitud(UserContext userContext, AreaSolicitudVO areaSolicitudVO) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();

		Session session = null;
		Transaction tx = null;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			areaSolicitudVO.clearErrorMessages();
			
			// Se recupera el Bean dado su id
			AreaSolicitud areaSolicitud = AreaSolicitud.getById(areaSolicitudVO.getId());
			
			// Se le delega al Manager el borrado, pero puse ser responsabilidad de otro bean
			areaSolicitud = CasSolicitudManager.getInstance().deleteAreaSolicitud(areaSolicitud);
			
			if (areaSolicitud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				areaSolicitudVO =  (AreaSolicitudVO) areaSolicitud.toVO(0,false);
			}
			areaSolicitud.passErrorMessages(areaSolicitudVO);
            
            log.debug(funcName + ": exit");
            return areaSolicitudVO;
		} catch (Exception e) {
			log.error(funcName + ": Service Error: ",  e);
			try { tx.rollback(); } catch (Exception ee) {}
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AreaSolicitudVO activarAreaSolicitud(UserContext userContext, AreaSolicitudVO areaSolicitudVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            AreaSolicitud areaSolicitud = AreaSolicitud.getById(areaSolicitudVO.getId());

            areaSolicitud.activar();

            if (areaSolicitud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				areaSolicitudVO =  (AreaSolicitudVO) areaSolicitud.toVO(0,false);
			}
            areaSolicitud.passErrorMessages(areaSolicitudVO);
            
            log.debug(funcName + ": exit");
            return areaSolicitudVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public AreaSolicitudVO desactivarAreaSolicitud(UserContext userContext, AreaSolicitudVO areaSolicitudVO ) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		      
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
            AreaSolicitud areaSolicitud = AreaSolicitud.getById(areaSolicitudVO.getId());
                           
            areaSolicitud.desactivar();

            if (areaSolicitud.hasError()) {
            	tx.rollback();
            	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
			} else {
				tx.commit();
				if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
				areaSolicitudVO =  (AreaSolicitudVO) areaSolicitud.toVO(0,false);
			}
            areaSolicitud.passErrorMessages(areaSolicitudVO);
            
            log.debug(funcName + ": exit");
            return areaSolicitudVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}	       
	
	public AreaSolicitudAdapter imprimirAreaSolicitud(UserContext userContext, AreaSolicitudAdapter areaSolicitudAdapterVO ) throws DemodaServiceException {
	    String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession(); 
			
			AreaSolicitud areaSolicitud = AreaSolicitud.getById(areaSolicitudAdapterVO.getAreaSolicitud().getId());

			CasDAOFactory.getAreaSolicitudDAO().imprimirGenerico(areaSolicitud, areaSolicitudAdapterVO.getReport());
	   		
			log.debug(funcName + ": exit");
			return areaSolicitudAdapterVO;
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();        
	    } 
	}
	
	// <--- ABM AreaSolicitud
	
}
