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

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlAdapter;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarSolicitudEmiPerRetroDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarSolicitudEmiPerRetroDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);

		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_SOLICITUDEMIPERRETRO, act); 
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();
		OrdenControlAdapter ordenControlAdapterVO = new OrdenControlAdapter();
		String stringServicio = "";
		ActionForward actionForward = null;
		try {

			//CommonKey commonKey = new CommonKey(request.getParameter("selectedId"));

			ordenControlAdapterVO = EfServiceLocator.getFiscalizacionService().getSolicitudEmiPerRetroInit(userSession);
			actionForward = mapping.findForward(EfConstants.FWD_SOLICITUDEMIPERRETRO_EDIT_ADAPTER);


			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables

			// Tiene errores no recuperables
			if (ordenControlAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + ordenControlAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlAdapter.NAME, ordenControlAdapterVO);
			}

			// Seteo los valores de navegacion en el adapter
			ordenControlAdapterVO.setValuesFromNavModel(navModel);

			if (log.isDebugEnabled()) log.debug(funcName + ": " + OrdenControlAdapter.NAME + ": "+ ordenControlAdapterVO.infoString());

			// Envio el VO al request
			request.setAttribute(OrdenControlAdapter.NAME, ordenControlAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OrdenControlAdapter.NAME, ordenControlAdapterVO);
            
			saveDemodaMessages(request, ordenControlAdapterVO);

			return actionForward;

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlAdapter.NAME);
		}
	}

	public ActionForward paramOrdenControl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				OrdenControlAdapter ordenControlAdapterVO =  (OrdenControlAdapter) userSession.get(OrdenControlAdapter.NAME);
			
				
				// Si es nulo no se puede continuar
				if (ordenControlAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OrdenControlAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName,OrdenControlAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(ordenControlAdapterVO, request);
			
	            // Tiene errores recuperables
				if (ordenControlAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ordenControlAdapterVO.infoString()); 
					saveDemodaErrors(request, ordenControlAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, OrdenControlAdapter.NAME, ordenControlAdapterVO);
				}
				
				// llamada al servicio
				ordenControlAdapterVO = EfServiceLocator.getFiscalizacionService().getSolicitudEmiPerRetroAdapterParamOrdenControl(userSession, ordenControlAdapterVO);
				
				
	            // Tiene errores recuperables
				if (ordenControlAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ordenControlAdapterVO.infoString()); 
					saveDemodaErrors(request, ordenControlAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, OrdenControlAdapter.NAME, ordenControlAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (ordenControlAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + ordenControlAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlAdapter.NAME, ordenControlAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(OrdenControlAdapter.NAME, ordenControlAdapterVO);
				// Subo el apdater al userSession
				userSession.put(OrdenControlAdapter.NAME, ordenControlAdapterVO);
		
				return mapping.findForward(EfConstants.FWD_SOLICITUDEMIPERRETRO_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OrdenControlAdapter.NAME);
			}
		}
	
	public ActionForward paramOrdConCue(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				OrdenControlAdapter ordenControlAdapterVO =  (OrdenControlAdapter) userSession.get(OrdenControlAdapter.NAME);
				
				
				// Si es nulo no se puede continuar
				if (ordenControlAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OrdenControlAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName,OrdenControlAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(ordenControlAdapterVO, request);
				
			     // Tiene errores recuperables
				if (ordenControlAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ordenControlAdapterVO.infoString()); 
					saveDemodaErrors(request, ordenControlAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, OrdenControlAdapter.NAME, ordenControlAdapterVO);
				}
				
				// llamada al servicio
				ordenControlAdapterVO = EfServiceLocator.getFiscalizacionService().getSolicitudEmiPerRetroAdapterParamOrdConCue(userSession, ordenControlAdapterVO);
				
				
	            // Tiene errores recuperables
				if (ordenControlAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ordenControlAdapterVO.infoString()); 
					saveDemodaErrors(request, ordenControlAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, OrdenControlAdapter.NAME, ordenControlAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (ordenControlAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + ordenControlAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlAdapter.NAME, ordenControlAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(OrdenControlAdapter.NAME, ordenControlAdapterVO);
				// Subo el apdater al userSession
				userSession.put(OrdenControlAdapter.NAME, ordenControlAdapterVO);
				
				return mapping.findForward(EfConstants.FWD_SOLICITUDEMIPERRETRO_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OrdenControlAdapter.NAME);
			}
		}
	
	public ActionForward enviarSolicitud(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);

		try {

			//bajo el adapter del usserSession
			OrdenControlAdapter ordenControlAdapterVO =  (OrdenControlAdapter) userSession.get(OrdenControlAdapter.NAME);

			// Si es nulo no se puede continuar
			if (ordenControlAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordenControlAdapterVO, request);			
		
			// llamo al servicio
			ordenControlAdapterVO = EfServiceLocator.getFiscalizacionService().enviarSolicitud(userSession, ordenControlAdapterVO);

			// Tiene errores recuperables
			if (ordenControlAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						OrdenControlAdapter.NAME, ordenControlAdapterVO);
			}

			// Tiene errores no recuperables
			if (ordenControlAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordenControlAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						OrdenControlAdapter.NAME, ordenControlAdapterVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, ordenControlAdapterVO);

			// Envio el VO al request
			request.setAttribute(OrdenControlAdapter.NAME, ordenControlAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OrdenControlAdapter.NAME, ordenControlAdapterVO);

			return forwardConfirmarOk(mapping, request, funcName, OrdenControlAdapter.NAME);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		return baseVolver(mapping, form, request, response, OrdenControlAdapter.NAME);

	}


}
