//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.struts;

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
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.model.BuzonCambiosAdapter;
import ar.gov.rosario.siat.pad.iface.model.BuzonCambiosVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarBuzonCambiosDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarBuzonCambiosDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ADM_BUZONCAMBIOS, BaseSecurityConstants.AGREGAR);
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		BuzonCambiosAdapter buzonCambiosAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			String vieneDe = (String) request.getAttribute("vieneDe");
			
			if ("liqDeuda".equals(vieneDe)){
				Long idCuenta =  new Long(request.getParameter("cuentaId"));
				userSession.put("buzonCambiosVolverA", GdeConstants.PATH_VER_CUENTA + idCuenta);
			
			} else if ("administrarPersona".equals(vieneDe) ){
				Long idPersona =  new Long(request.getParameter("selectedId"));
				userSession.put("buzonCambiosVolverA", PadConstants.PATH_ADMINISTRAR_PERSONA + idPersona);
				
			} else if ("formalizarConvenio".equals(vieneDe) ){
				Long idPersona =  new Long(request.getParameter("selectedId"));
				userSession.put("buzonCambiosVolverA", "/gde/AdministrarLiqFormConvenio.do?method=paramPersona&selectedId=" + idPersona);	
				
			} else {
				log.error("No se especifico el atributo en el request 'vieneDe'.para usar esta funcion hay que modificar el codigo del action");
				return forwardErrorNonRecoverable (mapping, request, funcName, BuzonCambiosAdapter.NAME, buzonCambiosAdapterVO);
			}
			
			CommonKey commonKeyTitular = new CommonKey(navModel.getSelectedId());
			
			stringServicio = "getBuzonCambiosAdapterForCreate(userSession)";
			buzonCambiosAdapterVO = PadServiceLocator.getReclamoService().getBuzonCambiosAdapterForCreate(userSession, commonKeyTitular);
			actionForward = mapping.findForward(PadConstants.FWD_BUZONCAMBIOS_EDIT_ADAPTER);				

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (buzonCambiosAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + buzonCambiosAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, BuzonCambiosAdapter.NAME, buzonCambiosAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			buzonCambiosAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + BuzonCambiosAdapter.NAME + ": "+ buzonCambiosAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(BuzonCambiosAdapter.NAME, buzonCambiosAdapterVO);
			// Subo el apdater al userSession
			userSession.put(BuzonCambiosAdapter.NAME, buzonCambiosAdapterVO);
			 
			saveDemodaMessages(request, buzonCambiosAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, BuzonCambiosAdapter.NAME);
		}
	}

	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ADM_BUZONCAMBIOS, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			BuzonCambiosAdapter buzonCambiosAdapterVO = (BuzonCambiosAdapter) userSession.get(BuzonCambiosAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (buzonCambiosAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + BuzonCambiosAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, BuzonCambiosAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(buzonCambiosAdapterVO, request);
			
            // Tiene errores recuperables
			if (buzonCambiosAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + buzonCambiosAdapterVO.infoString()); 
				saveDemodaErrors(request, buzonCambiosAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, BuzonCambiosAdapter.NAME, buzonCambiosAdapterVO);
			}
			
			// llamada al servicio
			BuzonCambiosVO buzonCambiosVO = PadServiceLocator.getReclamoService().createBuzonCambios(userSession, buzonCambiosAdapterVO.getBuzonCambios());
			
            // Tiene errores recuperables
			if (buzonCambiosVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + buzonCambiosVO.infoString()); 
				saveDemodaErrors(request, buzonCambiosVO);
				return forwardErrorRecoverable(mapping, request, userSession, BuzonCambiosAdapter.NAME, buzonCambiosAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (buzonCambiosVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + buzonCambiosVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, BuzonCambiosAdapter.NAME, buzonCambiosAdapterVO);
			}
			
			buzonCambiosAdapterVO.setPrevAction(PadConstants.PATH_ADMINISTRAR_BUZON_CAMBIOS);
			buzonCambiosAdapterVO.setPrevActionParameter(BaseConstants.ACT_VOLVER );
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, BuzonCambiosAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, BuzonCambiosAdapter.NAME);
		}
	}

	public ActionForward paramTipoModificacion (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			BuzonCambiosAdapter buzonCambiosAdapterVO = (BuzonCambiosAdapter) userSession.get(BuzonCambiosAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (buzonCambiosAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + BuzonCambiosAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, BuzonCambiosAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(buzonCambiosAdapterVO, request);
			
            // Tiene errores recuperables
			if (buzonCambiosAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + buzonCambiosAdapterVO.infoString()); 
				saveDemodaErrors(request, buzonCambiosAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, BuzonCambiosAdapter.NAME, buzonCambiosAdapterVO);
			}
			
			// llamada al servicio
			//buzonCambiosAdapterVO = ${Modulo}ServiceLocator.get${Submodulo}Service().getBuzonCambiosAdapterParam(userSession, buzonCambiosAdapterVO);
			
            // Tiene errores recuperables
			if (buzonCambiosAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + buzonCambiosAdapterVO.infoString()); 
				saveDemodaErrors(request, buzonCambiosAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, BuzonCambiosAdapter.NAME, buzonCambiosAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (buzonCambiosAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + buzonCambiosAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, BuzonCambiosAdapter.NAME, buzonCambiosAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(BuzonCambiosAdapter.NAME, buzonCambiosAdapterVO);
			// Subo el apdater al userSession
			userSession.put(BuzonCambiosAdapter.NAME, buzonCambiosAdapterVO);
			
			return mapping.findForward(PadConstants.FWD_BUZONCAMBIOS_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, BuzonCambiosAdapter.NAME);
		}
	}
	
	
	public ActionForward paramTipoPersona (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			BuzonCambiosAdapter buzonCambiosAdapterVO = (BuzonCambiosAdapter) userSession.get(BuzonCambiosAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (buzonCambiosAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + BuzonCambiosAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, BuzonCambiosAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(buzonCambiosAdapterVO, request);
			
            // Tiene errores recuperables
			if (buzonCambiosAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + buzonCambiosAdapterVO.infoString()); 
				saveDemodaErrors(request, buzonCambiosAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, BuzonCambiosAdapter.NAME, buzonCambiosAdapterVO);
			}
			
			// llamada al servicio
			//buzonCambiosAdapterVO = ${Modulo}ServiceLocator.get${Submodulo}Service().getBuzonCambiosAdapterParam(userSession, buzonCambiosAdapterVO);
			
            // Tiene errores recuperables
			if (buzonCambiosAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + buzonCambiosAdapterVO.infoString()); 
				saveDemodaErrors(request, buzonCambiosAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, BuzonCambiosAdapter.NAME, buzonCambiosAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (buzonCambiosAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + buzonCambiosAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, BuzonCambiosAdapter.NAME, buzonCambiosAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(BuzonCambiosAdapter.NAME, buzonCambiosAdapterVO);
			// Subo el apdater al userSession
			userSession.put(BuzonCambiosAdapter.NAME, buzonCambiosAdapterVO);
			
			return mapping.findForward(PadConstants.FWD_BUZONCAMBIOS_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, BuzonCambiosAdapter.NAME);
		}
	}
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		String volverA=userSession.get("buzonCambiosVolverA").toString();
		
		userSession.remove(BuzonCambiosAdapter.NAME);
		userSession.remove("buzonCambiosVolverA");
		log.debug("volviendo a "+volverA);
		
		return new ActionForward (volverA);			
	}
}
