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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelAdapter;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarCueExcSelDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCueExcSelDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUEEXCSEL, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CueExcSelAdapter cueExcSelAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getCueExcSelAdapterForView(userSession, commonKey)";
				cueExcSelAdapterVO = PadServiceLocator.getCuentaService().getCueExcSelAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_CUEEXCSEL_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getCueExcSelAdapterForView(userSession, commonKey)";
				cueExcSelAdapterVO = PadServiceLocator.getCuentaService().getCueExcSelAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_CUEEXCSEL_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getCueExcSelAdapterForView(userSession)";
				cueExcSelAdapterVO = PadServiceLocator.getCuentaService().getCueExcSelAdapterForView(userSession, commonKey);
				cueExcSelAdapterVO.addMessage(BaseError.MSG_ACTIVAR, PadError.CUEEXCSEL_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_CUEEXCSEL_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getCueExcSelAdapterForView(userSession)";
				cueExcSelAdapterVO = PadServiceLocator.getCuentaService().getCueExcSelAdapterForView(userSession, commonKey);
				cueExcSelAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, PadError.CUEEXCSEL_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_CUEEXCSEL_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (cueExcSelAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cueExcSelAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExcSelAdapter.NAME, cueExcSelAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			cueExcSelAdapterVO.setValuesFromNavModel(navModel);
			
			//Seteo lso Valores Para poder Volver al Search Page (WARNING PICHANGA!!!!)
			cueExcSelAdapterVO.setPrevAction("/pad/BuscarCueExcSel");
			cueExcSelAdapterVO.setPrevActionParameter("buscar");
			
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CueExcSelAdapter.NAME + ": "+ cueExcSelAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CueExcSelAdapter.NAME, cueExcSelAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CueExcSelAdapter.NAME, cueExcSelAdapterVO);
			 
			saveDemodaMessages(request, cueExcSelAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExcSelAdapter.NAME);
		}
	}
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				PadConstants.ACTION_ADMINISTRAR_ENC_CUEEXCSEL, BaseConstants.ACT_MODIFICAR);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUEEXCSEL, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExcSelAdapter cueExcSelAdapterVO = (CueExcSelAdapter) userSession.get(CueExcSelAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExcSelAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExcSelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExcSelAdapter.NAME); 
			}

			// llamada al servicio
			CueExcSelVO cueExcSelVO = PadServiceLocator.getCuentaService().activarCueExcSel
				(userSession, cueExcSelAdapterVO.getCueExcSel());
			
            // Tiene errores recuperables
			if (cueExcSelVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExcSelAdapterVO.infoString());
				saveDemodaErrors(request, cueExcSelVO);				
				request.setAttribute(CueExcSelAdapter.NAME, cueExcSelAdapterVO);
				return mapping.findForward(PadConstants.FWD_CUEEXCSEL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (cueExcSelVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExcSelAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExcSelAdapter.NAME, cueExcSelAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CueExcSelAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExcSelAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUEEXCSEL, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExcSelAdapter cueExcSelAdapterVO = (CueExcSelAdapter) userSession.get(CueExcSelAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExcSelAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExcSelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExcSelAdapter.NAME); 
			}

			// llamada al servicio
			CueExcSelVO cueExcSelVO = PadServiceLocator.getCuentaService().desactivarCueExcSel
				(userSession, cueExcSelAdapterVO.getCueExcSel());
			
            // Tiene errores recuperables
			if (cueExcSelVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExcSelAdapterVO.infoString());
				saveDemodaErrors(request, cueExcSelVO);				
				request.setAttribute(CueExcSelAdapter.NAME, cueExcSelAdapterVO);
				return mapping.findForward(PadConstants.FWD_CUEEXCSEL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (cueExcSelVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExcSelAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExcSelAdapter.NAME, cueExcSelAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CueExcSelAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExcSelAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CueExcSelAdapter.NAME);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, CueExcSelAdapter.NAME);
			
		}
	
	// ---> Metodos relacionados CueExcSelDeu (Detalle)
	public ActionForward agregarCueExcSelDeu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUEEXCSELDEU);
	}
	
	
	public ActionForward verCueExcSelDeu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUEEXCSELDEU);

		}
	
	
	public ActionForward activarCueExcSelDeu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardActivarAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUEEXCSELDEU);
	}
	
	public ActionForward desactivarCueExcSelDeu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardDesactivarAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUEEXCSELDEU);
	}
	// <--- Fin Metodos relacionados CueExcSelDeu (Detalle)
}
