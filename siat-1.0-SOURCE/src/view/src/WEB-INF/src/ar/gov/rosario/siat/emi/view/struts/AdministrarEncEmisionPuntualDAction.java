//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.view.util.DefinitionUtil;
import ar.gov.rosario.siat.emi.iface.model.EmisionPuntualAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionPuntualPreviewAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;


public final class AdministrarEncEmisionPuntualDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncEmisionPuntualDAction.class);
	
	public final static String CUENTA_EMITIR = "cuentaEmitir";

	private final static String MTD_VOLVER_PREVIEW = "volverPreview";
	
	private final static String MTD_PARAM_CUENTA = "paramCuenta";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
	
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISIONPUNTUAL, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		EmisionPuntualAdapter emisionPuntualAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CuentaVO cuentaVO = (CuentaVO) userSession.getNavModel().getParameter(AdministrarEncEmisionPuntualDAction.CUENTA_EMITIR);
			
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getEmisionPuntualAdapterForCreate(userSession)";
				emisionPuntualAdapterVO = EmiServiceLocator.getEmisionService()
					.getEmisionPuntualAdapterForCreate(userSession, cuentaVO);
				actionForward = mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_ENC_EDIT_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (emisionPuntualAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + emisionPuntualAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			emisionPuntualAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EmisionPuntualAdapter.ENC_NAME + ": "+ emisionPuntualAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);
	
			// Si es Tasa por Revision de Planos, ejecutamos el param
			if (emisionPuntualAdapterVO.getEsTRP()) {
				actionForward = this.paramRecurso(mapping, form, request, response);
			}
			
			return actionForward;
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionPuntualAdapter.ENC_NAME);
		}
	}

	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EmisionPuntualAdapter emisionPuntualAdapterVO = (EmisionPuntualAdapter) userSession.get(EmisionPuntualAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (emisionPuntualAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionPuntualAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionPuntualAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emisionPuntualAdapterVO, request);
			
            // Tiene errores recuperables
			if (emisionPuntualAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionPuntualAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionPuntualAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);
			}
			
			// llamada al servicio
			emisionPuntualAdapterVO = EmiServiceLocator.getEmisionService()
				.getEmisionPuntualAdapterParamRecurso(userSession, emisionPuntualAdapterVO);
			
            // Tiene errores recuperables
			if (emisionPuntualAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionPuntualAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionPuntualAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionPuntualAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionPuntualAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);

  			// Si el recurso seleccionado es Tasa por Revision de Planos
			if (emisionPuntualAdapterVO.getEsTRP()) {
				return mapping.findForward(EmiConstants.ACTION_ADMINISTRAR_EMISIONTRP); 
			}
			
			return mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_ENC_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionPuntualAdapter.ENC_NAME);
		}
	}
	
	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el searchPage del userSession
		EmisionPuntualAdapter emisionPuntualAdapterVO = (EmisionPuntualAdapter) userSession.get(EmisionPuntualAdapter.ENC_NAME);
		
		// Si es nulo no se puede continuar
		if (emisionPuntualAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + EmisionPuntualAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, EmisionPuntualAdapter.ENC_NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(emisionPuntualAdapterVO, request);
		
        // Tiene errores recuperables
		if (emisionPuntualAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + emisionPuntualAdapterVO.infoString()); 
			saveDemodaErrors(request, emisionPuntualAdapterVO);

			request.setAttribute(EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);
			return mapping.getInputForward();
		}
		
		// Subo el searchPage al userSession
		userSession.put(EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);

		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso 
		cuentaFiltro.getCuentaTitular().getCuenta()
			.setRecurso(emisionPuntualAdapterVO.getEmision().getRecurso()); 

		// y el numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta()
			.setNumeroCuenta(emisionPuntualAdapterVO.getEmision().getCuenta().getNumeroCuenta());

		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);
		
		// Forwardeo a la Search Page de Cuenta
		return forwardSeleccionar(mapping, request, MTD_PARAM_CUENTA, PadConstants.ACTION_BUSCAR_CUENTA, false);
	}
	
	public ActionForward paramCuenta (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName + " idSelected:"+request.getParameter("selectedId"));
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		try {
			//bajo el adapter del usserSession
			EmisionPuntualAdapter emisionPuntualAdapterVO =  (EmisionPuntualAdapter) userSession.get(EmisionPuntualAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (emisionPuntualAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionPuntualAdapter.ENC_NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionPuntualAdapter.ENC_NAME); 
			}
			
			// recupero el id seleccionado por el usuario
			String selectedId = request.getParameter("selectedId");
						
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);
				return mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_ENC_EDIT_ADAPTER); 
			}
			
			// Seteo el id de la cuenta
			emisionPuntualAdapterVO.getEmision().getCuenta().setId(new Long(selectedId));
			
			emisionPuntualAdapterVO = EmiServiceLocator.getEmisionService()
				.getEmisionPuntualAdapterParamCuenta(userSession, emisionPuntualAdapterVO);
			
			// Tiene errores recuperables
			if (emisionPuntualAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionPuntualAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionPuntualAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionPuntualAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionPuntualAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);
			}			
			
			// Envio el VO al request
			request.setAttribute(EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);

			// Subo el apdater al userSession
			userSession.put(EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);

			return mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_ENC_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionPuntualAdapter.ENC_NAME);
		}
	}
	
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EmisionPuntualAdapter emisionPuntualAdapterVO = (EmisionPuntualAdapter) 
				userSession.get(EmisionPuntualAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (emisionPuntualAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionPuntualAdapter.ENC_NAME 
						+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionPuntualAdapter.ENC_NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(emisionPuntualAdapterVO, request);
			
			log.debug( funcName + " " + emisionPuntualAdapterVO.getEmision().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, emisionPuntualAdapterVO.getEmision()); 
			
			emisionPuntualAdapterVO.getEmision().passErrorMessages(emisionPuntualAdapterVO);
		    
		    saveDemodaMessages(request, emisionPuntualAdapterVO);
		    
		    saveDemodaErrors(request, emisionPuntualAdapterVO);
		    
			request.setAttribute(EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);
			
			return mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_ENC_EDIT_ADAPTER);	
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionPuntualAdapter.ENC_NAME);
		}	
	}
	
	public ActionForward emitir(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISIONPUNTUAL, 
				BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		try {
			// Bajo el adapter del userSession
			EmisionPuntualAdapter emisionPuntualAdapterVO = (EmisionPuntualAdapter) 
				userSession.get(EmisionPuntualAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (emisionPuntualAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionPuntualAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionPuntualAdapter.ENC_NAME); 
			}
	
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emisionPuntualAdapterVO, request);
			
			// Populado de atributos de emision
			for (GenericAtrDefinition genericAtrDefinition:
					emisionPuntualAdapterVO.getEmision().getRecAtrCueEmiDefinition().getListGenericAtrDefinition()) {

				DefinitionUtil.populateAtrVal4Edit(genericAtrDefinition, request);

				// Validamos si es requerido
				if (!genericAtrDefinition.validateRequerido()){
					emisionPuntualAdapterVO.addRecoverableValueError("El campo " + 
							genericAtrDefinition.getAtributo().getDesAtributo() + " es requerido." );
				
				} else if (!genericAtrDefinition.validate("manual")) {
					emisionPuntualAdapterVO.addRecoverableValueError("El formato del campo " + 
							genericAtrDefinition.getAtributo().getDesAtributo() + " es incorrecto." );
				}
			}
			
	        // Tiene errores recuperables
			if (emisionPuntualAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionPuntualAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionPuntualAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);
			}
			
			// llamada al servicio
			EmisionVO emisionVO = EmiServiceLocator.getEmisionService().createEmisionPuntual(userSession, emisionPuntualAdapterVO);
			
	        // Tiene errores recuperables
			if (emisionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionVO.infoString()); 
				saveDemodaErrors(request, emisionVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);
			}
	
			// Seteamos la emision creada en el adapter para Preview
			EmisionPuntualPreviewAdapter emisionPuntualPreviewAdapter = new EmisionPuntualPreviewAdapter();
			emisionPuntualPreviewAdapter.setEmision(emisionVO);
			
			// Envio el VO al request
			request.setAttribute(EmisionPuntualPreviewAdapter.NAME, emisionPuntualPreviewAdapter);

			// Subo el apdater al userSession
			userSession.put(EmisionPuntualPreviewAdapter.NAME, emisionPuntualPreviewAdapter);

			// lo dirijo al adapter de modificacion
			return baseForward(mapping, request, funcName, MTD_VOLVER_PREVIEW, 
					EmiConstants.ACTION_ADMINISTRAR_EMISIONPUNTUAL_PREVIEW, EmiConstants.ACT_PREVIEW);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionPuntualAdapter.ENC_NAME);
		}
	}

	public ActionForward volverPreview(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);		
		if (userSession==null) return forwardErrorSession(request);
		
		EmisionPuntualAdapter emisionPuntualAdapterVO = null;
		
		try {
			
			// Bajo el adapter del userSession
			emisionPuntualAdapterVO = (EmisionPuntualAdapter) userSession.get(EmisionPuntualAdapter.ENC_NAME);

			// Si es nulo no se puede continuar
			if (emisionPuntualAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionPuntualAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionPuntualAdapter.ENC_NAME); 
			}
	
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EmisionPuntualAdapter.ENC_NAME + ": "+ emisionPuntualAdapterVO.infoString());
			
			// Limpiamos los errores si habia
			emisionPuntualAdapterVO.clearErrorMessages();
			
			// Envio el VO al request
			request.setAttribute(EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);

			// Subo el apdater al userSession
			userSession.put(EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapterVO);

			// lo dirijo al adapter de modificacion
			return mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_ENC_EDIT_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionPuntualAdapter.ENC_NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		return baseVolver(mapping, form, request, response, EmisionPuntualAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, EmisionPuntualAdapter.ENC_NAME);
		
	}

}
