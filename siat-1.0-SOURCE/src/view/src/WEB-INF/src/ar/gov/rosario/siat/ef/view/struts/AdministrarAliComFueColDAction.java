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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.ef.iface.model.AliComFueColAdapter;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarAliComFueColDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAliComFueColDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ALICOMFUECOL, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		AliComFueColAdapter aliComFueColAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			

			if (act.equals(EfConstants.ACT_INIT)) {
				stringServicio = "getAliComFueColAdapterForView(userSession)";
				aliComFueColAdapterVO = EfServiceLocator.getFiscalizacionService().getAliComFueColAdapterInit(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_ALICOMFUECOL_INIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (aliComFueColAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + aliComFueColAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			aliComFueColAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + AliComFueColAdapter.NAME + ": "+ aliComFueColAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			 
			saveDemodaMessages(request, aliComFueColAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AliComFueColAdapter.NAME);
		}
	}

	public ActionForward irModificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AliComFueColAdapter aliComFueColAdapterVO = (AliComFueColAdapter) userSession.get(AliComFueColAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (aliComFueColAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AliComFueColAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AliComFueColAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(aliComFueColAdapterVO, request);
			
            // Tiene errores recuperables
			if (aliComFueColAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aliComFueColAdapterVO.infoString()); 
				saveDemodaErrors(request, aliComFueColAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			}
			
			aliComFueColAdapterVO.getAliComFueCol().setId(Long.parseLong(request.getParameter("selectedId")));
			
			// llamada al servicio
			aliComFueColAdapterVO = EfServiceLocator.getFiscalizacionService().getAliComFueColAdapterForUpdate(userSession, aliComFueColAdapterVO);
			
            // Tiene errores recuperables
			if (aliComFueColAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aliComFueColAdapterVO.infoString()); 
				saveDemodaErrors(request, aliComFueColAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (aliComFueColAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + aliComFueColAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			aliComFueColAdapterVO.setValuesFromNavModel(userSession.getNavModel());
		
			aliComFueColAdapterVO.setAct("irModificar");
			
			request.setAttribute("irA", "listHist");
			
			// Envio el VO al request
			request.setAttribute(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			 
			return mapping.findForward(EfConstants.FWD_ALICOMFUECOL_HIST_ALICUOTA);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AliComFueColAdapter.NAME);
		}
	}
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ALICOMFUECOL, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AliComFueColAdapter aliComFueColAdapterVO = (AliComFueColAdapter) userSession.get(AliComFueColAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (aliComFueColAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AliComFueColAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AliComFueColAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(aliComFueColAdapterVO, request);
			
            // Tiene errores recuperables
			if (aliComFueColAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aliComFueColAdapterVO.infoString()); 
				saveDemodaErrors(request, aliComFueColAdapterVO);

				// Envio el VO al request
				request.setAttribute(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
				// Subo el apdater al userSession
				userSession.put(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
				 
				return mapping.findForward(EfConstants.FWD_ALICOMFUECOL_HIST_ALICUOTA);
			}
			
			// llamada al servicio
			aliComFueColAdapterVO = EfServiceLocator.getFiscalizacionService().updateAliComFueCol(userSession, aliComFueColAdapterVO);
			
            // Tiene errores recuperables
			if (aliComFueColAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aliComFueColAdapterVO.infoString()); 
				saveDemodaErrors(request, aliComFueColAdapterVO);
				// Envio el VO al request
				request.setAttribute(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
				// Subo el apdater al userSession
				userSession.put(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
				 
				return mapping.findForward(EfConstants.FWD_ALICOMFUECOL_HIST_ALICUOTA);

			}
			
			// Tiene errores no recuperables
			if (aliComFueColAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + aliComFueColAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			}
			
			aliComFueColAdapterVO.setAct(BaseConstants.ACT_MODIFICAR);
			request.setAttribute("irA", "listHist");
			
			// Envio el VO al request
			request.setAttribute(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			 
			return mapping.findForward(EfConstants.FWD_ALICOMFUECOL_HIST_ALICUOTA);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AliComFueColAdapter.NAME);
		}
	}

	public ActionForward irAgregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AliComFueColAdapter aliComFueColAdapterVO = (AliComFueColAdapter) userSession.get(AliComFueColAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (aliComFueColAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AliComFueColAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AliComFueColAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(aliComFueColAdapterVO, request);
			
            // Tiene errores recuperables
			if (aliComFueColAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aliComFueColAdapterVO.infoString()); 
				saveDemodaErrors(request, aliComFueColAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			}
			if(!StringUtil.isNullOrEmpty(request.getParameter(BaseConstants.SELECTED_ID)))
				aliComFueColAdapterVO.getCompFuenteCol().setId(Long.parseLong(request.getParameter(BaseConstants.SELECTED_ID)));
			
			// llamada al servicio
			aliComFueColAdapterVO = EfServiceLocator.getFiscalizacionService().getAliComFueColAdapterForCreate(userSession, aliComFueColAdapterVO);
			
            // Tiene errores recuperables
			if (aliComFueColAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aliComFueColAdapterVO.infoString()); 
				saveDemodaErrors(request, aliComFueColAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (aliComFueColAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + aliComFueColAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			aliComFueColAdapterVO.setValuesFromNavModel(userSession.getNavModel());
		
			aliComFueColAdapterVO.setAct("irAgregar");
			request.setAttribute("irA", "listHist");
			
			// Envio el VO al request
			request.setAttribute(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			 
			return mapping.findForward(EfConstants.FWD_ALICOMFUECOL_HIST_ALICUOTA);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AliComFueColAdapter.NAME);
		}
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ALICOMFUECOL, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AliComFueColAdapter aliComFueColAdapterVO = (AliComFueColAdapter) userSession.get(AliComFueColAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (aliComFueColAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AliComFueColAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AliComFueColAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(aliComFueColAdapterVO, request);
			
            // Tiene errores recuperables
			if (aliComFueColAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aliComFueColAdapterVO.infoString()); 
				saveDemodaErrors(request, aliComFueColAdapterVO);

				// Envio el VO al request
				request.setAttribute(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
				// Subo el apdater al userSession
				userSession.put(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
				 
				return mapping.findForward(EfConstants.FWD_ALICOMFUECOL_HIST_ALICUOTA);
			}
			
			// llamada al servicio
			aliComFueColAdapterVO = EfServiceLocator.getFiscalizacionService().createAliComFueCol(userSession, aliComFueColAdapterVO);
			
            // Tiene errores recuperables
			if (aliComFueColAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aliComFueColAdapterVO.infoString()); 
				saveDemodaErrors(request, aliComFueColAdapterVO);
				// Envio el VO al request
				request.setAttribute(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
				// Subo el apdater al userSession
				userSession.put(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
				 
				return mapping.findForward(EfConstants.FWD_ALICOMFUECOL_HIST_ALICUOTA);

			}
			
			// Tiene errores no recuperables
			if (aliComFueColAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + aliComFueColAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			}
			
			aliComFueColAdapterVO.setAct(BaseConstants.ACT_AGREGAR);
			request.setAttribute("irA", "listHist");
			
			// Envio el VO al request
			request.setAttribute(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			 
			return mapping.findForward(EfConstants.FWD_ALICOMFUECOL_HIST_ALICUOTA);

			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AliComFueColAdapter.NAME);
		}
	}

	public ActionForward irHistoricos(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AliComFueColAdapter aliComFueColAdapterVO = (AliComFueColAdapter) userSession.get(AliComFueColAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (aliComFueColAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AliComFueColAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AliComFueColAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(aliComFueColAdapterVO, request);
			
            // Tiene errores recuperables
			if (aliComFueColAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aliComFueColAdapterVO.infoString()); 
				saveDemodaErrors(request, aliComFueColAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			}
			
			CommonKey commonKey = new CommonKey(request.getParameter("selectedId"));
			// llamada al servicio
			aliComFueColAdapterVO = EfServiceLocator.getFiscalizacionService().getAliComFueColAdapterFor4Hist(userSession,commonKey,aliComFueColAdapterVO);
			
            // Tiene errores recuperables
			if (aliComFueColAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aliComFueColAdapterVO.infoString()); 
				saveDemodaErrors(request, aliComFueColAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (aliComFueColAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + aliComFueColAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			aliComFueColAdapterVO.setValuesFromNavModel(userSession.getNavModel());
		
			// Envio el VO al request
			request.setAttribute(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			 
			return mapping.findForward(EfConstants.FWD_ALICOMFUECOL_HIST_ALICUOTA);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AliComFueColAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ALICOMFUECOL, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AliComFueColAdapter aliComFueColAdapterVO = (AliComFueColAdapter) userSession.get(AliComFueColAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (aliComFueColAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AliComFueColAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AliComFueColAdapter.NAME); 
			}

			// setea en el adapter el id seleccionado
			aliComFueColAdapterVO.getAliComFueCol().setId(Long.parseLong(request.getParameter("selectedId")));
			
			// llamada al servicio
			aliComFueColAdapterVO = EfServiceLocator.getFiscalizacionService().deleteAliComFueCol(userSession, aliComFueColAdapterVO);
			
            // Tiene errores recuperables
			if (aliComFueColAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aliComFueColAdapterVO.infoString());
				saveDemodaErrors(request, aliComFueColAdapterVO);				
				request.setAttribute(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
				return mapping.findForward(EfConstants.FWD_ALICOMFUECOL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (aliComFueColAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + aliComFueColAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AliComFueColAdapter.NAME, aliComFueColAdapterVO);
			 
			return mapping.findForward(EfConstants.FWD_ALICOMFUECOL_HIST_ALICUOTA);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AliComFueColAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AliComFueColAdapter.NAME);
		
	}
		
}
