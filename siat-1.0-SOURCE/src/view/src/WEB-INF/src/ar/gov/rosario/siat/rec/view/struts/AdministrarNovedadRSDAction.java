//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.NovedadRSAdapter;
import ar.gov.rosario.siat.rec.iface.model.NovedadRSVO;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarNovedadRSDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarNovedadRSDAction.class);


	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_NOVEDADRS, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			NovedadRSAdapter novedadRSAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getNovedadRSAdapterForView(userSession, commonKey)";
					novedadRSAdapterVO = RecServiceLocator.getDreiService().getNovedadRSAdapterForView(userSession, commonKey);
					actionForward = mapping.findForward(RecConstants.FWD_NOVEDADRS_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_APLICAR)) {
					stringServicio = "getNovedadRSAdapterForUpdate(userSession, commonKey)";
					novedadRSAdapterVO = RecServiceLocator.getDreiService().getNovedadRSAdapterForSimular(userSession, commonKey);
					novedadRSAdapterVO.addMessage(RecError.NOVEDADRS_MSG_APLICAR);
					actionForward = mapping.findForward(RecConstants.FWD_NOVEDADRS_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(RecConstants.ACT_APLICAR_MASIVO_NOVEDADRS)) {
					stringServicio = "getNovedadRSAdapterForMasivo(userSession)";
					novedadRSAdapterVO = RecServiceLocator.getDreiService().getNovedadRSAdapterForMasivo(userSession);
					novedadRSAdapterVO.addMessage(RecError.NOVEDADRS_MSG_APLICAR_MASIVO);
					actionForward = mapping.findForward(RecConstants.FWD_NOVEDADRS_VIEW_ADAPTER);
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (novedadRSAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + novedadRSAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, NovedadRSAdapter.NAME, novedadRSAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				novedadRSAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					NovedadRSAdapter.NAME + ": " + novedadRSAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(NovedadRSAdapter.NAME, novedadRSAdapterVO);
				// Subo el apdater al userSession
				userSession.put(NovedadRSAdapter.NAME, novedadRSAdapterVO);
				
				saveDemodaMessages(request, novedadRSAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, NovedadRSAdapter.NAME);
			}
		}
	
	
	public ActionForward aplicar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				RecSecurityConstants.ABM_NOVEDADRS, RecSecurityConstants.MTD_APLICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				NovedadRSAdapter novedadRSAdapterVO = (NovedadRSAdapter) userSession.get(NovedadRSAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (novedadRSAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + NovedadRSAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, NovedadRSAdapter.NAME); 
				}
				
				// llamada al servicio
				NovedadRSVO novedadRSVO = RecServiceLocator.getDreiService().aplicarNovedadRS(userSession, novedadRSAdapterVO.getNovedadRS());
				
	            // Tiene errores recuperables
				if (novedadRSVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + novedadRSAdapterVO.infoString()); 
					saveDemodaErrors(request, novedadRSVO);
					return forwardErrorRecoverable(mapping, request, userSession, NovedadRSAdapter.NAME, novedadRSAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (novedadRSVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + novedadRSAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, NovedadRSAdapter.NAME, novedadRSAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, NovedadRSAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, NovedadRSAdapter.NAME);
			}
	}
	
	public ActionForward aplicarMasivo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				RecSecurityConstants.ABM_NOVEDADRS, RecSecurityConstants.MTD_APLICARMASIVO);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				NovedadRSAdapter novedadRSAdapterVO = (NovedadRSAdapter) userSession.get(NovedadRSAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (novedadRSAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + NovedadRSAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, NovedadRSAdapter.NAME); 
				}
				
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(novedadRSAdapterVO, request);
				
	            // Tiene errores recuperables
				if (novedadRSAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + novedadRSAdapterVO.infoString()); 
					saveDemodaErrors(request, novedadRSAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, NovedadRSAdapter.NAME, novedadRSAdapterVO);
				}
				
				// llamada al servicio
				novedadRSAdapterVO = RecServiceLocator.getDreiService().aplicarMasivoNovedadRS(userSession,novedadRSAdapterVO);
				
	            // Tiene errores recuperables
				if (novedadRSAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + novedadRSAdapterVO.infoString()); 
					saveDemodaErrors(request, novedadRSAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, NovedadRSAdapter.NAME, novedadRSAdapterVO, RecConstants.FWD_NOVEDADRS_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (novedadRSAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + novedadRSAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, NovedadRSAdapter.NAME, novedadRSAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, NovedadRSAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, NovedadRSAdapter.NAME);
			}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, NovedadRSAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, NovedadRSAdapter.NAME);
			
	}
	
}
