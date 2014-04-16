//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.FolioAdapter;
import ar.gov.rosario.siat.bal.iface.model.FolioVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarEncFolioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncFolioDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_FOLIO_ENC, act);		

			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			FolioAdapter folioAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());

				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getFolioAdapterForUpdate(userSession, commonKey)";
					folioAdapterVO = BalServiceLocator.getFolioTesoreriaService().getFolioAdapterForUpdate(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_FOLIO_ENC_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getFolioAdapterForCreate(userSession)";
					folioAdapterVO = BalServiceLocator.getFolioTesoreriaService().getFolioAdapterForCreate(userSession);
					actionForward = mapping.findForward(BalConstants.FWD_FOLIO_ENC_EDIT_ADAPTER);
				}
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (folioAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + folioAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, FolioAdapter.ENC_NAME, folioAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				folioAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + FolioAdapter.ENC_NAME + ": "+ folioAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(FolioAdapter.ENC_NAME, folioAdapterVO);
				// Subo el apdater al userSession
				userSession.put(FolioAdapter.ENC_NAME, folioAdapterVO);
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FolioAdapter.ENC_NAME);
			}
		}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_FOLIO_ENC, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				FolioAdapter folioAdapterVO = (FolioAdapter) userSession.get(FolioAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (folioAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + FolioAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, FolioAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(folioAdapterVO, request);
				
	            // Tiene errores recuperables
				if (folioAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folioAdapterVO.infoString()); 
					saveDemodaErrors(request, folioAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, FolioAdapter.ENC_NAME, folioAdapterVO);
				}
				
				// llamada al servicio
				FolioVO folioVO = BalServiceLocator.getFolioTesoreriaService().createFolio(userSession, folioAdapterVO.getFolio());
				
	            // Tiene errores recuperables
				if (folioVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folioVO.infoString()); 
					saveDemodaErrors(request, folioVO);
					return forwardErrorRecoverable(mapping, request, userSession, FolioAdapter.ENC_NAME, folioAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (folioVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + folioVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, FolioAdapter.ENC_NAME, folioAdapterVO);
				}

				return forwardConfirmarOk(mapping, request, funcName, FolioAdapter.ENC_NAME);
									
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FolioAdapter.ENC_NAME);
			}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_FOLIO_ENC, BaseSecurityConstants.MODIFICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				FolioAdapter folioAdapterVO = (FolioAdapter) userSession.get(FolioAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (folioAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + FolioAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, FolioAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(folioAdapterVO, request);
				
	            // Tiene errores recuperables
				if (folioAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folioAdapterVO.infoString()); 
					saveDemodaErrors(request, folioAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, FolioAdapter.ENC_NAME, folioAdapterVO);
				}
				
				// llamada al servicio
				FolioVO folioVO = BalServiceLocator.getFolioTesoreriaService().updateFolio(userSession, folioAdapterVO.getFolio());
				
	            // Tiene errores recuperables
				if (folioVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folioAdapterVO.infoString()); 
					saveDemodaErrors(request, folioVO);
					return forwardErrorRecoverable(mapping, request, userSession, FolioAdapter.ENC_NAME, folioAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (folioVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + folioAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, FolioAdapter.ENC_NAME, folioAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, FolioAdapter.ENC_NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FolioAdapter.ENC_NAME);
			}
	}

	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, FolioAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, FolioAdapter.ENC_NAME);
	}

}
