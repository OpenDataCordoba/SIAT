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
import ar.gov.rosario.siat.ef.iface.model.PeriodoOrdenAdapter;
import ar.gov.rosario.siat.ef.iface.model.PeriodoOrdenVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPeriodoOrdenDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPeriodoOrdenDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName+"       act:"+act);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PERIODOORDEN, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PeriodoOrdenAdapter periodoOrdenAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPeriodoOrdenAdapterForAgregar(userSession, commonKey)";
				periodoOrdenAdapterVO = EfServiceLocator.getFiscalizacionService().getPeriodoOrdenAdapterForAgregar(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_PERIODOORDEN_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPeriodoOrdenAdapterForView(userSession, commonKey)";
				periodoOrdenAdapterVO = EfServiceLocator.getFiscalizacionService().getPeriodoOrdenAdapterForView(userSession, commonKey);				
				periodoOrdenAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.PERIODOORDEN_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_PERIODOORDEN_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (periodoOrdenAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + periodoOrdenAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PeriodoOrdenAdapter.NAME, periodoOrdenAdapterVO);
			}
			
			// setea a que parte de la pantalla vuelve (en el adapter de la ordenControl)
			userSession.put("irA", "bloquePeriodos");

			// Seteo los valores de navegacion en el adapter
			periodoOrdenAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PeriodoOrdenAdapter.NAME + ": "+ periodoOrdenAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PeriodoOrdenAdapter.NAME, periodoOrdenAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PeriodoOrdenAdapter.NAME, periodoOrdenAdapterVO);
			 
			saveDemodaMessages(request, periodoOrdenAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PeriodoOrdenAdapter.NAME);
		}
	}

	public ActionForward buscar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PERIODOORDEN, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PeriodoOrdenAdapter periodoOrdenAdapterVO = (PeriodoOrdenAdapter) userSession.get(PeriodoOrdenAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (periodoOrdenAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PeriodoOrdenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PeriodoOrdenAdapter.NAME); 
			}

			log.debug("ids:"+request.getParameter("idsSelectedForCrear"));
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(periodoOrdenAdapterVO, request);
			
            // Tiene errores recuperables
			if (periodoOrdenAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + periodoOrdenAdapterVO.infoString()); 
				saveDemodaErrors(request, periodoOrdenAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PeriodoOrdenAdapter.NAME, periodoOrdenAdapterVO);
			}
			
			// llamada al servicio
			periodoOrdenAdapterVO = EfServiceLocator.getFiscalizacionService().getPeriodoOrdenAdapterResult(userSession, periodoOrdenAdapterVO);
			
            // Tiene errores recuperables
			if (periodoOrdenAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + periodoOrdenAdapterVO.infoString()); 
				saveDemodaErrors(request, periodoOrdenAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PeriodoOrdenAdapter.NAME, periodoOrdenAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (periodoOrdenAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + periodoOrdenAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PeriodoOrdenAdapter.NAME, periodoOrdenAdapterVO);
			}
						
			// Envio el VO al request
			request.setAttribute(PeriodoOrdenAdapter.NAME, periodoOrdenAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PeriodoOrdenAdapter.NAME, periodoOrdenAdapterVO);
			 
			saveDemodaMessages(request, periodoOrdenAdapterVO);
			
			return mapping.findForward(EfConstants.FWD_PERIODOORDEN_EDIT_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PeriodoOrdenAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PERIODOORDEN, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PeriodoOrdenAdapter periodoOrdenAdapterVO = (PeriodoOrdenAdapter) userSession.get(PeriodoOrdenAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (periodoOrdenAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PeriodoOrdenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PeriodoOrdenAdapter.NAME); 
			}

			log.debug("ids:"+request.getParameter("idsSelected"));
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(periodoOrdenAdapterVO, request);
			
            // Tiene errores recuperables
			if (periodoOrdenAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + periodoOrdenAdapterVO.infoString()); 
				saveDemodaErrors(request, periodoOrdenAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PeriodoOrdenAdapter.NAME, periodoOrdenAdapterVO);
			}
			
			if(request.getParameter("idsSelected")==null){
				periodoOrdenAdapterVO.addRecoverableError(EfError.PERIODOORDEN_AGREGAR_MSG_IDS_REQUERIDO);
				// Envio el VO al request
				request.setAttribute(PeriodoOrdenAdapter.NAME, periodoOrdenAdapterVO);
				// Subo en el adapter al userSession
				userSession.put(PeriodoOrdenAdapter.NAME, periodoOrdenAdapterVO);
				
				saveDemodaErrors(request, periodoOrdenAdapterVO);
				return mapping.findForward(EfConstants.FWD_PERIODOORDEN_EDIT_ADAPTER);
			}

			// llamada al servicio
			periodoOrdenAdapterVO = EfServiceLocator.getFiscalizacionService().agregarListPeriodoOrden(userSession, periodoOrdenAdapterVO);
			
            // Tiene errores recuperables
			if (periodoOrdenAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + periodoOrdenAdapterVO.infoString()); 
				saveDemodaErrors(request, periodoOrdenAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PeriodoOrdenAdapter.NAME, periodoOrdenAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (periodoOrdenAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + periodoOrdenAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PeriodoOrdenAdapter.NAME, periodoOrdenAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PeriodoOrdenAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PeriodoOrdenAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PERIODOORDEN, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PeriodoOrdenAdapter periodoOrdenAdapterVO = (PeriodoOrdenAdapter) userSession.get(PeriodoOrdenAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (periodoOrdenAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PeriodoOrdenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PeriodoOrdenAdapter.NAME); 
			}

			// llamada al servicio
			PeriodoOrdenVO periodoOrdenVO = EfServiceLocator.getFiscalizacionService().deletePeriodoOrden(userSession, periodoOrdenAdapterVO.getPeriodoOrden());
			
            // Tiene errores recuperables
			if (periodoOrdenVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + periodoOrdenAdapterVO.infoString());
				saveDemodaErrors(request, periodoOrdenVO);				
				request.setAttribute(PeriodoOrdenAdapter.NAME, periodoOrdenAdapterVO);
				return mapping.findForward(EfConstants.FWD_PERIODOORDEN_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (periodoOrdenVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + periodoOrdenAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PeriodoOrdenAdapter.NAME, periodoOrdenAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PeriodoOrdenAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PeriodoOrdenAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PeriodoOrdenAdapter.NAME);
		
	}
		
}
