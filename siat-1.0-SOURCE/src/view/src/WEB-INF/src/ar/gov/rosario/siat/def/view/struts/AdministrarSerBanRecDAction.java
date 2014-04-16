//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

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
import ar.gov.rosario.siat.def.iface.model.SerBanRecAdapter;
import ar.gov.rosario.siat.def.iface.model.SerBanRecVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarSerBanRecDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarSerBanRecDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SERVICIO_BANCO_RECURSO, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			SerBanRecAdapter serBanRecAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getSerBanRecAdapterForView(userSession, commonKey)";
					serBanRecAdapterVO = DefServiceLocator.getServicioBancoService().getSerBanRecAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_SERBANREC_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getSerBanRecAdapterForUpdate(userSession, commonKey)";
					serBanRecAdapterVO = DefServiceLocator.getServicioBancoService().getSerBanRecAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_SERBANREC_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getSerBanRecAdapterForDelete(userSession, commonKey)";
					serBanRecAdapterVO = DefServiceLocator.getServicioBancoService().getSerBanRecAdapterForView
						(userSession, commonKey);
					serBanRecAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.SERBANREC_LABEL);				
					actionForward = mapping.findForward(DefConstants.FWD_SERBANREC_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getSerBanRecAdapterForCreate(userSession)";
					serBanRecAdapterVO = DefServiceLocator.getServicioBancoService().getSerBanRecAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_SERBANREC_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (serBanRecAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + serBanRecAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, SerBanRecAdapter.NAME, serBanRecAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				serBanRecAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + SerBanRecAdapter.NAME + ": "+ serBanRecAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(SerBanRecAdapter.NAME, serBanRecAdapterVO);
				// Subo el apdater al userSession
				userSession.put(SerBanRecAdapter.NAME, serBanRecAdapterVO);
				
				saveDemodaMessages(request, serBanRecAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, SerBanRecAdapter.NAME);
			}
		}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				DefSecurityConstants.ABM_SERVICIO_BANCO_RECURSO, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				SerBanRecAdapter serBanRecAdapterVO = (SerBanRecAdapter) userSession.get(SerBanRecAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (serBanRecAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + SerBanRecAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, SerBanRecAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(serBanRecAdapterVO, request);
				
	            // Tiene errores recuperables
				if (serBanRecAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + serBanRecAdapterVO.infoString()); 
					saveDemodaErrors(request, serBanRecAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, SerBanRecAdapter.NAME, serBanRecAdapterVO);
				}
				
				// llamada al servicio
				SerBanRecVO serBanRecVO = DefServiceLocator.getServicioBancoService().createSerBanRec(userSession, serBanRecAdapterVO.getSerBanRec());
				
	            // Tiene errores recuperables
				if (serBanRecVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + serBanRecVO.infoString()); 
					saveDemodaErrors(request, serBanRecVO);
					return forwardErrorRecoverable(mapping, request, userSession, SerBanRecAdapter.NAME, serBanRecAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (serBanRecVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + serBanRecVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, SerBanRecAdapter.NAME, serBanRecAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, SerBanRecAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, SerBanRecAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_SERVICIO_BANCO_RECURSO, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				SerBanRecAdapter serBanRecAdapterVO = (SerBanRecAdapter) userSession.get(SerBanRecAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (serBanRecAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + SerBanRecAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, SerBanRecAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(serBanRecAdapterVO, request);
				
	            // Tiene errores recuperables
				if (serBanRecAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + serBanRecAdapterVO.infoString()); 
					saveDemodaErrors(request, serBanRecAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						SerBanRecAdapter.NAME, serBanRecAdapterVO);
				}
				
				// llamada al servicio
				SerBanRecVO serBanRecVO = DefServiceLocator.getServicioBancoService().updateSerBanRec
					(userSession, serBanRecAdapterVO.getSerBanRec());
				
	            // Tiene errores recuperables
				if (serBanRecVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + serBanRecAdapterVO.infoString()); 
					saveDemodaErrors(request, serBanRecVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						SerBanRecAdapter.NAME, serBanRecAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (serBanRecVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + serBanRecAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						SerBanRecAdapter.NAME, serBanRecAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, SerBanRecAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, SerBanRecAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_SERVICIO_BANCO_RECURSO, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				SerBanRecAdapter serBanRecAdapterVO = (SerBanRecAdapter) userSession.get(SerBanRecAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (serBanRecAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + SerBanRecAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, SerBanRecAdapter.NAME); 
				}

				// llamada al servicio
				SerBanRecVO serBanRecVO = DefServiceLocator.getServicioBancoService().deleteSerBanRec
					(userSession, serBanRecAdapterVO.getSerBanRec());
				
	            // Tiene errores recuperables
				if (serBanRecVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + serBanRecAdapterVO.infoString());
					saveDemodaErrors(request, serBanRecVO);				
					request.setAttribute(SerBanRecAdapter.NAME, serBanRecAdapterVO);
					return mapping.findForward(DefConstants.FWD_SERBANREC_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (serBanRecVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + serBanRecAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						SerBanRecAdapter.NAME, serBanRecAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, SerBanRecAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, SerBanRecAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, SerBanRecAdapter.NAME);
		}
	

}
