//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

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
import ar.gov.rosario.siat.gde.iface.model.ProRecAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProRecVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarProRecDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProRecDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROREC, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ProRecAdapter proRecAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getProRecAdapterForView(userSession, commonKey)";
				proRecAdapterVO = GdeServiceLocator.getDefinicionService().getProRecAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PROREC_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getProRecAdapterForUpdate(userSession, commonKey)";
				proRecAdapterVO = GdeServiceLocator.getDefinicionService().getProRecAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PROREC_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getProRecAdapterForView(userSession, commonKey)";
				proRecAdapterVO = GdeServiceLocator.getDefinicionService().getProRecAdapterForView(userSession, commonKey);				
				proRecAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PROREC_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PROREC_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getProRecAdapterForCreate(userSession)";
				proRecAdapterVO = GdeServiceLocator.getDefinicionService().getProRecAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PROREC_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (proRecAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + proRecAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProRecAdapter.NAME, proRecAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			proRecAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProRecAdapter.NAME + ": "+ proRecAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ProRecAdapter.NAME, proRecAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProRecAdapter.NAME, proRecAdapterVO);
			 
			saveDemodaMessages(request, proRecAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProRecAdapter.NAME);
		}
	}
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				GdeConstants.ACTION_ADMINISTRAR_ENC_PROREC, BaseConstants.ACT_MODIFICAR);

		}	
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROREC, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProRecAdapter proRecAdapterVO = (ProRecAdapter) userSession.get(ProRecAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proRecAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProRecAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProRecAdapter.NAME); 
			}

			// llamada al servicio
			ProRecVO proRecVO = GdeServiceLocator.getDefinicionService().deleteProRec
				(userSession, proRecAdapterVO.getProRec());
			
            // Tiene errores recuperables
			if (proRecVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proRecAdapterVO.infoString());
				saveDemodaErrors(request, proRecVO);				
				request.setAttribute(ProRecAdapter.NAME, proRecAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PROREC_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (proRecVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proRecAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProRecAdapter.NAME, proRecAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProRecAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProRecAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProRecAdapter.NAME);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ProRecAdapter.NAME);
			
		}

	// Metodos relacionados ProRecDesHas
	public ActionForward verProRecDesHas(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PRORECDESHAS);

	}

	public ActionForward modificarProRecDesHas(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PRORECDESHAS);

	}

	public ActionForward eliminarProRecDesHas(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PRORECDESHAS);

	}
	
	public ActionForward agregarProRecDesHas(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PRORECDESHAS);
		
	}
	
	// Metodos relacionados ProRecCom
	public ActionForward verProRecCom(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PRORECCOM);

	}

	public ActionForward modificarProRecCom(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PRORECCOM);

	}

	public ActionForward eliminarProRecCom(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PRORECCOM);

	}
	
	public ActionForward agregarProRecCom(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PRORECCOM);
		
	}
	
}
