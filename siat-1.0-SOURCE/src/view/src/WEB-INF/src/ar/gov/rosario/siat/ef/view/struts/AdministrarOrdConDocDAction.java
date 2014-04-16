//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

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
import ar.gov.rosario.siat.ef.iface.model.OrdConDocAdapter;
import ar.gov.rosario.siat.ef.iface.model.OrdConDocVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarOrdConDocDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarOrdConDocDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ORDCONDOC, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		OrdConDocAdapter ordConDocAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			log.debug("llego el id:"+navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getOrdConDocAdapterForView(userSession, commonKey)";
				ordConDocAdapterVO = EfServiceLocator.getFiscalizacionService().getOrdConDocAdapterForView(userSession, commonKey);				
				ordConDocAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.ORDCONDOC_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_ORDCONDOC_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getOrdConDocAdapterForCreate(userSession)";
				ordConDocAdapterVO = EfServiceLocator.getFiscalizacionService().getOrdConDocAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_ORDCONDOC_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (ordConDocAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + ordConDocAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdConDocAdapter.NAME, ordConDocAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			ordConDocAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + OrdConDocAdapter.NAME + ": "+ ordConDocAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(OrdConDocAdapter.NAME, ordConDocAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OrdConDocAdapter.NAME, ordConDocAdapterVO);
			 
			saveDemodaMessages(request, ordConDocAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdConDocAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ORDCONDOC, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OrdConDocAdapter ordConDocAdapterVO = (OrdConDocAdapter) userSession.get(OrdConDocAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ordConDocAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdConDocAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdConDocAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordConDocAdapterVO, request);
			
            // Tiene errores recuperables
			if (ordConDocAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordConDocAdapterVO.infoString()); 
				saveDemodaErrors(request, ordConDocAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdConDocAdapter.NAME, ordConDocAdapterVO);
			}
			
			if(request.getParameter("idsSelected")==null){
				ordConDocAdapterVO.addRecoverableError(EfError.AGREGAR_ORDCONDOC_MSG_IDS_REQUERIDO);
				// Envio el VO al request
				request.setAttribute(OrdConDocAdapter.NAME, ordConDocAdapterVO);
				// Subo en el adapter al userSession
				userSession.put(OrdConDocAdapter.NAME, ordConDocAdapterVO);
				
				saveDemodaErrors(request, ordConDocAdapterVO);
				return mapping.findForward(EfConstants.FWD_ORDCONDOC_EDIT_ADAPTER);
			}
			
			// llamada al servicio
			ordConDocAdapterVO = EfServiceLocator.getFiscalizacionService().createOrdConDoc(userSession, ordConDocAdapterVO);
			
            // Tiene errores recuperables
			if (ordConDocAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordConDocAdapterVO.infoString()); 
				saveDemodaErrors(request, ordConDocAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdConDocAdapter.NAME, ordConDocAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (ordConDocAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordConDocAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdConDocAdapter.NAME, ordConDocAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OrdConDocAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdConDocAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ORDCONDOC, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OrdConDocAdapter ordConDocAdapterVO = (OrdConDocAdapter) userSession.get(OrdConDocAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ordConDocAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdConDocAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdConDocAdapter.NAME); 
			}

			// llamada al servicio
			OrdConDocVO ordConDocVO = EfServiceLocator.getFiscalizacionService().deleteOrdConDoc
				(userSession, ordConDocAdapterVO.getOrdConDoc());
			
            // Tiene errores recuperables
			if (ordConDocVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordConDocAdapterVO.infoString());
				saveDemodaErrors(request, ordConDocVO);				
				request.setAttribute(OrdConDocAdapter.NAME, ordConDocAdapterVO);
				return mapping.findForward(EfConstants.FWD_ORDCONDOC_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (ordConDocVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordConDocAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdConDocAdapter.NAME, ordConDocAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OrdConDocAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdConDocAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, OrdConDocAdapter.NAME);
		
	}
		
}
