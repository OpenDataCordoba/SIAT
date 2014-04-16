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

import ar.gov.rosario.siat.bal.iface.model.OtrIngTesParAdapter;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesParVO;
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

public class AdministrarOtrIngTesParDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarOtrIngTesParDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_OTRINGTESPAR, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			OtrIngTesParAdapter otrIngTesParAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getOtrIngTesParAdapterForView(userSession, commonKey)";
					otrIngTesParAdapterVO = BalServiceLocator.getOtroIngresoTesoreriaService().getOtrIngTesParAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_OTRINGTESPAR_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getOtrIngTesParAdapterForUpdate(userSession, commonKey)";
					otrIngTesParAdapterVO = BalServiceLocator.getOtroIngresoTesoreriaService().getOtrIngTesParAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_OTRINGTESPAR_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getOtrIngTesParAdapterForDelete(userSession, commonKey)";
					otrIngTesParAdapterVO = BalServiceLocator.getOtroIngresoTesoreriaService().getOtrIngTesParAdapterForView
						(userSession, commonKey);
					otrIngTesParAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.OTRINGTESPAR_LABEL);				
					actionForward = mapping.findForward(BalConstants.FWD_OTRINGTESPAR_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getOtrIngTesParAdapterForCreate(userSession)";
					otrIngTesParAdapterVO = BalServiceLocator.getOtroIngresoTesoreriaService().getOtrIngTesParAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_OTRINGTESPAR_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (otrIngTesParAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + otrIngTesParAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, OtrIngTesParAdapter.NAME, otrIngTesParAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				otrIngTesParAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + OtrIngTesParAdapter.NAME + ": "+ otrIngTesParAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(OtrIngTesParAdapter.NAME, otrIngTesParAdapterVO);
				// Subo el apdater al userSession
				userSession.put(OtrIngTesParAdapter.NAME, otrIngTesParAdapterVO);
				
				saveDemodaMessages(request, otrIngTesParAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OtrIngTesParAdapter.NAME);
			}
		}
	

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				BalSecurityConstants.ABM_OTRINGTESPAR, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				OtrIngTesParAdapter otrIngTesParAdapterVO = (OtrIngTesParAdapter) userSession.get(OtrIngTesParAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (otrIngTesParAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OtrIngTesParAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OtrIngTesParAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(otrIngTesParAdapterVO, request);
				
	            // Tiene errores recuperables
				if (otrIngTesParAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesParAdapterVO.infoString()); 
					saveDemodaErrors(request, otrIngTesParAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, OtrIngTesParAdapter.NAME, otrIngTesParAdapterVO);
				}
				
				// llamada al servicio
				OtrIngTesParVO otrIngTesParVO = BalServiceLocator.getOtroIngresoTesoreriaService().createOtrIngTesPar(userSession, otrIngTesParAdapterVO.getOtrIngTesPar());
				
	            // Tiene errores recuperables
				if (otrIngTesParVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesParVO.infoString()); 
					saveDemodaErrors(request, otrIngTesParVO);
					return forwardErrorRecoverable(mapping, request, userSession, OtrIngTesParAdapter.NAME, otrIngTesParAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (otrIngTesParVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + otrIngTesParVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, OtrIngTesParAdapter.NAME, otrIngTesParAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, OtrIngTesParAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OtrIngTesParAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_OTRINGTESPAR, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				OtrIngTesParAdapter otrIngTesParAdapterVO = (OtrIngTesParAdapter) userSession.get(OtrIngTesParAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (otrIngTesParAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OtrIngTesParAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OtrIngTesParAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(otrIngTesParAdapterVO, request);
				
	            // Tiene errores recuperables
				if (otrIngTesParAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesParAdapterVO.infoString()); 
					saveDemodaErrors(request, otrIngTesParAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						OtrIngTesParAdapter.NAME, otrIngTesParAdapterVO);
				}
				
				// llamada al servicio
				OtrIngTesParVO otrIngTesParVO = BalServiceLocator.getOtroIngresoTesoreriaService().updateOtrIngTesPar(userSession, otrIngTesParAdapterVO.getOtrIngTesPar());
				
	            // Tiene errores recuperables
				if (otrIngTesParVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesParAdapterVO.infoString()); 
					saveDemodaErrors(request, otrIngTesParVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						OtrIngTesParAdapter.NAME, otrIngTesParAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (otrIngTesParVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + otrIngTesParAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						OtrIngTesParAdapter.NAME, otrIngTesParAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, OtrIngTesParAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OtrIngTesParAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_OTRINGTESPAR, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				OtrIngTesParAdapter otrIngTesParAdapterVO = (OtrIngTesParAdapter) userSession.get(OtrIngTesParAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (otrIngTesParAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OtrIngTesParAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OtrIngTesParAdapter.NAME); 
				}

				// llamada al servicio
				OtrIngTesParVO otrIngTesParVO = BalServiceLocator.getOtroIngresoTesoreriaService().deleteOtrIngTesPar(userSession, otrIngTesParAdapterVO.getOtrIngTesPar());
				
	            // Tiene errores recuperables
				if (otrIngTesParVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesParAdapterVO.infoString());
					saveDemodaErrors(request, otrIngTesParVO);				
					request.setAttribute(OtrIngTesParAdapter.NAME, otrIngTesParAdapterVO);
					return mapping.findForward(BalConstants.FWD_OTRINGTESPAR_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (otrIngTesParVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + otrIngTesParAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						OtrIngTesParAdapter.NAME, otrIngTesParAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, OtrIngTesParAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OtrIngTesParAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, OtrIngTesParAdapter.NAME);
		}


}
