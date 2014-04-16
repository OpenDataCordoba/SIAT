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
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteAdapter;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarContribuyenteDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarContribuyenteDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CONTRIBUYENTE, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ContribuyenteAdapter atributoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getContribuyenteAdapterForView(userSession, commonKey)";
				atributoAdapterVO = PadServiceLocator.getContribuyenteService().getContribuyenteAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_CONTRIBUYENTE_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getContribuyenteAdapterForUpdate(userSession, commonKey)";
				atributoAdapterVO = PadServiceLocator.getContribuyenteService().getContribuyenteAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_CONTRIBUYENTE_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getContribuyenteAdapterForView(userSession)";
				atributoAdapterVO = PadServiceLocator.getContribuyenteService().getContribuyenteAdapterForView(userSession, commonKey);				
				//atributoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, PadError.CONTRIBUYENTE_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_CONTRIBUYENTE_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getContribuyenteAdapterForView(userSession)";
				atributoAdapterVO = PadServiceLocator.getContribuyenteService().getContribuyenteAdapterForView(userSession, commonKey);					
				//atributoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, PadError.CONTRIBUYENTE_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_CONTRIBUYENTE_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + 
				stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (atributoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + atributoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					ContribuyenteAdapter.NAME, atributoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			atributoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				ContribuyenteAdapter.NAME + ": "+ atributoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ContribuyenteAdapter.NAME, atributoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ContribuyenteAdapter.NAME, atributoAdapterVO);
			 
			saveDemodaMessages(request, atributoAdapterVO);
			
			//Integer pepe = null;
			//pepe.byteValue();
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribuyenteAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CONTRIBUYENTE, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContribuyenteAdapter atributoAdapterVO = (ContribuyenteAdapter) userSession.get(ContribuyenteAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (atributoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContribuyenteAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribuyenteAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(atributoAdapterVO, request);

            // Tiene errores recuperables
			if (atributoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoAdapterVO.infoString()); 
				saveDemodaErrors(request, atributoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContribuyenteAdapter.NAME, atributoAdapterVO);
			}
			
			// llamada al servicio
			ContribuyenteVO atributoVO = PadServiceLocator.getContribuyenteService().createContribuyente(userSession, atributoAdapterVO.getContribuyente());
			
            // Tiene errores recuperables
			if (atributoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoVO.infoString()); 
				saveDemodaErrors(request, atributoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContribuyenteAdapter.NAME, atributoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (atributoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + atributoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContribuyenteAdapter.NAME, atributoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ContribuyenteAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribuyenteAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CONTRIBUYENTE, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContribuyenteAdapter atributoAdapterVO = (ContribuyenteAdapter) userSession.get(ContribuyenteAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (atributoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContribuyenteAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribuyenteAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(atributoAdapterVO, request);
			
            // Tiene errores recuperables
			if (atributoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoAdapterVO.infoString()); 
				saveDemodaErrors(request, atributoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContribuyenteAdapter.NAME, atributoAdapterVO);
			}
			
			// llamada al servicio
			ContribuyenteVO atributoVO = PadServiceLocator.getContribuyenteService().updateContribuyente(userSession, atributoAdapterVO.getContribuyente());
			
            // Tiene errores recuperables
			if (atributoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoAdapterVO.infoString()); 
				saveDemodaErrors(request, atributoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContribuyenteAdapter.NAME, atributoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (atributoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + atributoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContribuyenteAdapter.NAME, atributoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ContribuyenteAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribuyenteAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CONTRIBUYENTE, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContribuyenteAdapter atributoAdapterVO = (ContribuyenteAdapter) userSession.get(ContribuyenteAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (atributoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContribuyenteAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribuyenteAdapter.NAME); 
			}

			// llamada al servicio
			ContribuyenteVO atributoVO = PadServiceLocator.getContribuyenteService().deleteContribuyente
				(userSession, atributoAdapterVO.getContribuyente());
			
            // Tiene errores recuperables
			if (atributoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoAdapterVO.infoString());
				saveDemodaErrors(request, atributoVO);				
				request.setAttribute(ContribuyenteAdapter.NAME, atributoAdapterVO);
				return mapping.findForward(PadConstants.FWD_CONTRIBUYENTE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (atributoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + atributoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContribuyenteAdapter.NAME, atributoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ContribuyenteAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribuyenteAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CONTRIBUYENTE, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContribuyenteAdapter atributoAdapterVO = (ContribuyenteAdapter) userSession.get(ContribuyenteAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (atributoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContribuyenteAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribuyenteAdapter.NAME); 
			}

			// llamada al servicio
			ContribuyenteVO atributoVO = PadServiceLocator.getContribuyenteService().activarContribuyente
				(userSession, atributoAdapterVO.getContribuyente());
			
            // Tiene errores recuperables
			if (atributoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoAdapterVO.infoString());
				saveDemodaErrors(request, atributoVO);				
				request.setAttribute(ContribuyenteAdapter.NAME, atributoAdapterVO);
				return mapping.findForward(PadConstants.FWD_CONTRIBUYENTE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (atributoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + atributoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContribuyenteAdapter.NAME, atributoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ContribuyenteAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribuyenteAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CONTRIBUYENTE, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContribuyenteAdapter atributoAdapterVO = (ContribuyenteAdapter) userSession.get(ContribuyenteAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (atributoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContribuyenteAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribuyenteAdapter.NAME); 
			}

			// llamada al servicio
			ContribuyenteVO atributoVO = PadServiceLocator.getContribuyenteService().desactivarContribuyente
				(userSession, atributoAdapterVO.getContribuyente());
			
            // Tiene errores recuperables
			if (atributoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoAdapterVO.infoString());
				saveDemodaErrors(request, atributoVO);				
				request.setAttribute(ContribuyenteAdapter.NAME, atributoAdapterVO);
				return mapping.findForward(PadConstants.FWD_CONTRIBUYENTE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (atributoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + atributoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContribuyenteAdapter.NAME, atributoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ContribuyenteAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribuyenteAdapter.NAME);
		}
	}
	
	public ActionForward modificarPersona(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			PadConstants.ACTION_ADMINISTRAR_PERSONA, BaseConstants.ACT_MODIFICAR);

	}
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			PadConstants.ACTION_ADMINISTRAR_ENCCONTRIBUYENTE, BaseConstants.ACT_MODIFICAR);

	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ContribuyenteAdapter.NAME);
		
	}
	
	public ActionForward verCuentaTitular(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			PadConstants.ACTION_ADMINISTRAR_CUENTATITULAR, BaseConstants.ACT_VER);

	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ContribuyenteAdapter.NAME);
		
	}
	
	// Metodos relacionados ConAtrVal
	public ActionForward verConAtrVal(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CONATRVAL);

	}

	public ActionForward modificarConAtrVal(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CONATRVAL);

	}
	
}
