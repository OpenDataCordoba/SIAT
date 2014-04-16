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
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import ar.gov.rosario.siat.gde.iface.model.SerBanDesGenAdapter;
import ar.gov.rosario.siat.gde.iface.model.SerBanDesGenVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarSerBanDesGenDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarSerBanDesGenDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SERVICIO_BANCO_DESCUENTOS_GENERALES, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			SerBanDesGenAdapter serBanDesGenAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getSerBanDesGenAdapterForView(userSession, commonKey)";
					serBanDesGenAdapterVO = GdeServiceLocator.getDefinicionService().getSerBanDesGenAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_SERBANDESGEN_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getSerBanDesGenAdapterForUpdate(userSession, commonKey)";
					serBanDesGenAdapterVO = GdeServiceLocator.getDefinicionService().getSerBanDesGenAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_SERBANDESGEN_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getSerBanDesGenAdapterForDelete(userSession, commonKey)";
					serBanDesGenAdapterVO = GdeServiceLocator.getDefinicionService().getSerBanDesGenAdapterForView
						(userSession, commonKey);
					serBanDesGenAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.SERBANDESGEN_LABEL);				
					actionForward = mapping.findForward(DefConstants.FWD_SERBANDESGEN_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getSerBanDesGenAdapterForCreate(userSession)";
					serBanDesGenAdapterVO = GdeServiceLocator.getDefinicionService().getSerBanDesGenAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_SERBANDESGEN_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (serBanDesGenAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + serBanDesGenAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, SerBanDesGenAdapter.NAME, serBanDesGenAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				serBanDesGenAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + SerBanDesGenAdapter.NAME + ": "+ serBanDesGenAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(SerBanDesGenAdapter.NAME, serBanDesGenAdapterVO);
				// Subo el apdater al userSession
				userSession.put(SerBanDesGenAdapter.NAME, serBanDesGenAdapterVO);
				
				saveDemodaMessages(request, serBanDesGenAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, SerBanDesGenAdapter.NAME);
			}
		}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				DefSecurityConstants.ABM_SERVICIO_BANCO_DESCUENTOS_GENERALES, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				SerBanDesGenAdapter serBanDesGenAdapterVO = (SerBanDesGenAdapter) userSession.get(SerBanDesGenAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (serBanDesGenAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + SerBanDesGenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, SerBanDesGenAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(serBanDesGenAdapterVO, request);
				
	            // Tiene errores recuperables
				if (serBanDesGenAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + serBanDesGenAdapterVO.infoString()); 
					saveDemodaErrors(request, serBanDesGenAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, SerBanDesGenAdapter.NAME, serBanDesGenAdapterVO);
				}
				
				// llamada al servicio
				SerBanDesGenVO serBanDesGenVO = GdeServiceLocator.getDefinicionService().createSerBanDesGen(userSession, serBanDesGenAdapterVO.getSerBanDesGen());
				
	            // Tiene errores recuperables
				if (serBanDesGenVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + serBanDesGenVO.infoString()); 
					saveDemodaErrors(request, serBanDesGenVO);
					return forwardErrorRecoverable(mapping, request, userSession, SerBanDesGenAdapter.NAME, serBanDesGenAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (serBanDesGenVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + serBanDesGenVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, SerBanDesGenAdapter.NAME, serBanDesGenAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, SerBanDesGenAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, SerBanDesGenAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_SERVICIO_BANCO_DESCUENTOS_GENERALES, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				SerBanDesGenAdapter serBanDesGenAdapterVO = (SerBanDesGenAdapter) userSession.get(SerBanDesGenAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (serBanDesGenAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + SerBanDesGenAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, SerBanDesGenAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(serBanDesGenAdapterVO, request);
				
	            // Tiene errores recuperables
				if (serBanDesGenAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + serBanDesGenAdapterVO.infoString()); 
					saveDemodaErrors(request, serBanDesGenAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						SerBanDesGenAdapter.NAME, serBanDesGenAdapterVO);
				}
				
				// llamada al servicio
				SerBanDesGenVO serBanDesGenVO = GdeServiceLocator.getDefinicionService().updateSerBanDesGen
					(userSession, serBanDesGenAdapterVO.getSerBanDesGen());
				
	            // Tiene errores recuperables
				if (serBanDesGenVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + serBanDesGenAdapterVO.infoString()); 
					saveDemodaErrors(request, serBanDesGenVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						SerBanDesGenAdapter.NAME, serBanDesGenAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (serBanDesGenVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + serBanDesGenAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						SerBanDesGenAdapter.NAME, serBanDesGenAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, SerBanDesGenAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, SerBanDesGenAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_SERVICIO_BANCO_DESCUENTOS_GENERALES, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				SerBanDesGenAdapter serBanDesGenAdapterVO = (SerBanDesGenAdapter) userSession.get(SerBanDesGenAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (serBanDesGenAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + SerBanDesGenAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, SerBanDesGenAdapter.NAME); 
				}

				// llamada al servicio
				SerBanDesGenVO serBanDesGenVO = GdeServiceLocator.getDefinicionService().deleteSerBanDesGen
					(userSession, serBanDesGenAdapterVO.getSerBanDesGen());
				
	            // Tiene errores recuperables
				if (serBanDesGenVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + serBanDesGenAdapterVO.infoString());
					saveDemodaErrors(request, serBanDesGenVO);				
					request.setAttribute(SerBanDesGenAdapter.NAME, serBanDesGenAdapterVO);
					return mapping.findForward(DefConstants.FWD_SERBANREC_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (serBanDesGenVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + serBanDesGenAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						SerBanDesGenAdapter.NAME, serBanDesGenAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, SerBanDesGenAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, SerBanDesGenAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, SerBanDesGenAdapter.NAME);
		}
	

	
}
