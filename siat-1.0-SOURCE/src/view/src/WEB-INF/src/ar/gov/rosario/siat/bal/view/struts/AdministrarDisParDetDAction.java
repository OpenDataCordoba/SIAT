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

import ar.gov.rosario.siat.bal.iface.model.DisParDetAdapter;
import ar.gov.rosario.siat.bal.iface.model.DisParDetVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarDisParDetDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDisParDetDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DISPARDET, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			DisParDetAdapter disParDetAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getDisParDetAdapterForView(userSession, commonKey)";
					disParDetAdapterVO = BalServiceLocator.getDistribucionService().getDisParDetAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_DISPARDET_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getDisParDetAdapterForUpdate(userSession, commonKey)";
					disParDetAdapterVO = BalServiceLocator.getDistribucionService().getDisParDetAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_DISPARDET_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getDisParDetAdapterForDelete(userSession, commonKey)";
					disParDetAdapterVO = BalServiceLocator.getDistribucionService().getDisParDetAdapterForView
						(userSession, commonKey);
					disParDetAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.DISPARDET_LABEL);				
					actionForward = mapping.findForward(BalConstants.FWD_DISPARDET_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getDisParDetAdapterForCreate(userSession)";
					disParDetAdapterVO = BalServiceLocator.getDistribucionService().getDisParDetAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_DISPARDET_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (disParDetAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + disParDetAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DisParDetAdapter.NAME, disParDetAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				disParDetAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + DisParDetAdapter.NAME + ": "+ disParDetAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(DisParDetAdapter.NAME, disParDetAdapterVO);
				// Subo el apdater al userSession
				userSession.put(DisParDetAdapter.NAME, disParDetAdapterVO);
				
				saveDemodaMessages(request, disParDetAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParDetAdapter.NAME);
			}
		}
	

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				BalSecurityConstants.ABM_DISPARDET, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DisParDetAdapter disParDetAdapterVO = (DisParDetAdapter) userSession.get(DisParDetAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (disParDetAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DisParDetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DisParDetAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(disParDetAdapterVO, request);
				
	            // Tiene errores recuperables
				if (disParDetAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParDetAdapterVO.infoString()); 
					saveDemodaErrors(request, disParDetAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DisParDetAdapter.NAME, disParDetAdapterVO);
				}
				
				// llamada al servicio
				DisParDetVO disParDetVO = BalServiceLocator.getDistribucionService().createDisParDet(userSession, disParDetAdapterVO.getDisParDet());
				
	            // Tiene errores recuperables
				if (disParDetVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParDetVO.infoString()); 
					saveDemodaErrors(request, disParDetVO);
					return forwardErrorRecoverable(mapping, request, userSession, DisParDetAdapter.NAME, disParDetAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (disParDetVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + disParDetVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DisParDetAdapter.NAME, disParDetAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DisParDetAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParDetAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_DISPARDET, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DisParDetAdapter disParDetAdapterVO = (DisParDetAdapter) userSession.get(DisParDetAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (disParDetAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DisParDetAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DisParDetAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(disParDetAdapterVO, request);
				
	            // Tiene errores recuperables
				if (disParDetAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParDetAdapterVO.infoString()); 
					saveDemodaErrors(request, disParDetAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						DisParDetAdapter.NAME, disParDetAdapterVO);
				}
				
				// llamada al servicio
				DisParDetVO disParDetVO = BalServiceLocator.getDistribucionService().updateDisParDet(userSession, disParDetAdapterVO.getDisParDet());
				
	            // Tiene errores recuperables
				if (disParDetVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParDetAdapterVO.infoString()); 
					saveDemodaErrors(request, disParDetVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						DisParDetAdapter.NAME, disParDetAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (disParDetVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + disParDetAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						DisParDetAdapter.NAME, disParDetAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DisParDetAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParDetAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_DISPARDET, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DisParDetAdapter disParDetAdapterVO = (DisParDetAdapter) userSession.get(DisParDetAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (disParDetAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DisParDetAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DisParDetAdapter.NAME); 
				}

				// llamada al servicio
				DisParDetVO disParDetVO = BalServiceLocator.getDistribucionService().deleteDisParDet(userSession, disParDetAdapterVO.getDisParDet());
				
	            // Tiene errores recuperables
				if (disParDetVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParDetAdapterVO.infoString());
					saveDemodaErrors(request, disParDetVO);				
					request.setAttribute(DisParDetAdapter.NAME, disParDetAdapterVO);
					return mapping.findForward(BalConstants.FWD_DISPARDET_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (disParDetVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + disParDetAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						DisParDetAdapter.NAME, disParDetAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DisParDetAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParDetAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, DisParDetAdapter.NAME);
		}

		public ActionForward paramTipoImporte(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
				
				String funcName = DemodaUtil.currentMethodName();
				if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
				UserSession userSession = getCurrentUserSession(request, mapping); 
				if (userSession==null) return forwardErrorSession(request);
				
				try {
					// Bajo el adapter del userSession
					DisParDetAdapter disParDetAdapterVO = (DisParDetAdapter) userSession.get(DisParDetAdapter.NAME);
			
					// Si es nulo no se puede continuar
					if (disParDetAdapterVO == null) {
						log.error("error en: "  + funcName + ": " + DisParDetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
						return forwardErrorSessionNullObject(mapping, request, funcName, DisParDetAdapter.NAME); 
					}

					// Recuperamos datos del form en el vo
					DemodaUtil.populateVO(disParDetAdapterVO, request);
					
		            // Tiene errores recuperables
					if (disParDetAdapterVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + disParDetAdapterVO.infoString()); 
						saveDemodaErrors(request, disParDetAdapterVO);
						return forwardErrorRecoverable(mapping, request, userSession, DisParDetAdapter.NAME, disParDetAdapterVO);
					}
					
					// Llamada al servicio
					disParDetAdapterVO = BalServiceLocator.getDistribucionService().getDisParDetAdapterParamTipoImporte(userSession, disParDetAdapterVO);
					
		            // Tiene errores recuperables
					if (disParDetAdapterVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + disParDetAdapterVO.infoString()); 
						saveDemodaErrors(request, disParDetAdapterVO);
						return forwardErrorRecoverable(mapping, request, userSession, DisParDetAdapter.NAME, disParDetAdapterVO);
					}
					
					// Tiene errores no recuperables
					if (disParDetAdapterVO.hasErrorNonRecoverable()) {
						log.error("error en: "  + funcName + ": " + disParDetAdapterVO.errorString()); 
						return forwardErrorNonRecoverable(mapping, request, funcName, DisParDetAdapter.NAME, disParDetAdapterVO);
					}
					
					// Envio el VO al request
					request.setAttribute(DisParDetAdapter.NAME, disParDetAdapterVO);
					// Subo el adapter al userSession
					userSession.put(DisParDetAdapter.NAME, disParDetAdapterVO);
					
					return mapping.findForward(BalConstants.FWD_DISPARDET_EDIT_ADAPTER);
				
				} catch (Exception exception) {
					return baseException(mapping, request, funcName, exception, DisParDetAdapter.NAME);
				}
		}

}
