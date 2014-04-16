//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.struts;

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
import ar.gov.rosario.siat.pad.iface.model.CriRepCalleAdapter;
import ar.gov.rosario.siat.pad.iface.model.CriRepCalleVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarCriRepCalleDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCriRepCalleDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CRIREPCALLE, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			CriRepCalleAdapter criRepCalleAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getCriRepCalleAdapterForView(userSession, commonKey)";
					criRepCalleAdapterVO = PadServiceLocator.getDistribucionService().getCriRepCalleAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(PadConstants.FWD_CRIREPCALLE_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getCriRepCalleAdapterForUpdate(userSession, commonKey)";
					criRepCalleAdapterVO = PadServiceLocator.getDistribucionService().getCriRepCalleAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(PadConstants.FWD_CRIREPCALLE_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getCriRepCalleAdapterForDelete(userSession, commonKey)";
					criRepCalleAdapterVO = PadServiceLocator.getDistribucionService().getCriRepCalleAdapterForView
						(userSession, commonKey);
					criRepCalleAdapterVO.addMessage(BaseError.MSG_ELIMINAR, PadError.CRIREPCALLE_LABEL);				
					actionForward = mapping.findForward(PadConstants.FWD_CRIREPCALLE_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getCriRepCalleAdapterForCreate(userSession)";
					criRepCalleAdapterVO = PadServiceLocator.getDistribucionService().getCriRepCalleAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(PadConstants.FWD_CRIREPCALLE_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (criRepCalleAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + criRepCalleAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CriRepCalleAdapter.NAME, criRepCalleAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				criRepCalleAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + CriRepCalleAdapter.NAME + ": "+ criRepCalleAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(CriRepCalleAdapter.NAME, criRepCalleAdapterVO);
				// Subo el apdater al userSession
				userSession.put(CriRepCalleAdapter.NAME, criRepCalleAdapterVO);
				
				saveDemodaMessages(request, criRepCalleAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CriRepCalleAdapter.NAME);
			}
		}
	
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				PadSecurityConstants.ABM_CRIREPCALLE, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				CriRepCalleAdapter criRepCalleAdapterVO = (CriRepCalleAdapter) userSession.get(CriRepCalleAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (criRepCalleAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CriRepCalleAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CriRepCalleAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(criRepCalleAdapterVO, request);
				
	            // Tiene errores recuperables
				if (criRepCalleAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + criRepCalleAdapterVO.infoString()); 
					saveDemodaErrors(request, criRepCalleAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, CriRepCalleAdapter.NAME, criRepCalleAdapterVO);
				}
				
				// llamada al servicio
				CriRepCalleVO criRepCalleVO = PadServiceLocator.getDistribucionService().createCriRepCalle(userSession, criRepCalleAdapterVO.getCriRepCalle());
				
	            // Tiene errores recuperables
				if (criRepCalleVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + criRepCalleVO.infoString()); 
					saveDemodaErrors(request, criRepCalleVO);
					return forwardErrorRecoverable(mapping, request, userSession, CriRepCalleAdapter.NAME, criRepCalleAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (criRepCalleVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + criRepCalleVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CriRepCalleAdapter.NAME, criRepCalleAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, CriRepCalleAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CriRepCalleAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				PadSecurityConstants.ABM_CRIREPCALLE, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				CriRepCalleAdapter criRepCalleAdapterVO = (CriRepCalleAdapter) userSession.get(CriRepCalleAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (criRepCalleAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CriRepCalleAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CriRepCalleAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(criRepCalleAdapterVO, request);
				
	            // Tiene errores recuperables
				if (criRepCalleAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + criRepCalleAdapterVO.infoString()); 
					saveDemodaErrors(request, criRepCalleAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						CriRepCalleAdapter.NAME, criRepCalleAdapterVO);
				}
				
				// llamada al servicio
				CriRepCalleVO criRepCalleVO = PadServiceLocator.getDistribucionService().updateCriRepCalle(userSession, criRepCalleAdapterVO.getCriRepCalle());
				
	            // Tiene errores recuperables
				if (criRepCalleVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + criRepCalleAdapterVO.infoString()); 
					saveDemodaErrors(request, criRepCalleVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						CriRepCalleAdapter.NAME, criRepCalleAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (criRepCalleVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + criRepCalleAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						CriRepCalleAdapter.NAME, criRepCalleAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, CriRepCalleAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CriRepCalleAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				PadSecurityConstants.ABM_CRIREPCALLE, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				CriRepCalleAdapter criRepCalleAdapterVO = (CriRepCalleAdapter) userSession.get(CriRepCalleAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (criRepCalleAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CriRepCalleAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CriRepCalleAdapter.NAME); 
				}

				// llamada al servicio
				CriRepCalleVO criRepCalleVO = PadServiceLocator.getDistribucionService().deleteCriRepCalle(userSession, criRepCalleAdapterVO.getCriRepCalle());
				
	            // Tiene errores recuperables
				if (criRepCalleVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + criRepCalleAdapterVO.infoString());
					saveDemodaErrors(request, criRepCalleVO);				
					request.setAttribute(CriRepCalleAdapter.NAME, criRepCalleAdapterVO);
					return mapping.findForward(PadConstants.FWD_CRIREPCALLE_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (criRepCalleVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + criRepCalleAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						CriRepCalleAdapter.NAME, criRepCalleAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, CriRepCalleAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CriRepCalleAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, CriRepCalleAdapter.NAME);
		}

		
		public ActionForward buscarCalle(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				CriRepCalleAdapter criRepCalleAdapterVO = (CriRepCalleAdapter) userSession.get(CriRepCalleAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (criRepCalleAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CriRepCalleAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CriRepCalleAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(criRepCalleAdapterVO, request);
				
	            // Tiene errores recuperables
				if (criRepCalleAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + criRepCalleAdapterVO.infoString()); 
					saveDemodaErrors(request, criRepCalleAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						CriRepCalleAdapter.NAME, criRepCalleAdapterVO);
				}
			
			return forwardSeleccionar(mapping, request, 
					PadConstants.METOD_CRIREPCALLE_PARAM_CALLE, PadConstants.ACTION_BUSCAR_CALLE, false);
		
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CriRepCalleAdapter.NAME);
			}
		}

		public ActionForward paramCalle(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				// Bajo el adapter del userSession
				CriRepCalleAdapter criRepCalleAdapterVO = (CriRepCalleAdapter) userSession.get(CriRepCalleAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (criRepCalleAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CriRepCalleAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CriRepCalleAdapter.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(CriRepCalleAdapter.NAME, criRepCalleAdapterVO);
					return mapping.findForward(PadConstants.FWD_CRIREPCALLE_EDIT_ADAPTER); 
				}

				// llamo al param del servicio
				criRepCalleAdapterVO = PadServiceLocator.getDistribucionService().paramCalleCriRepCalle
					(userSession, criRepCalleAdapterVO, new Long(selectedId));

	            // Tiene errores recuperables
				if (criRepCalleAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + criRepCalleAdapterVO.infoString()); 
					saveDemodaErrors(request, criRepCalleAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						CriRepCalleAdapter.NAME, criRepCalleAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (criRepCalleAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + criRepCalleAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						CriRepCalleAdapter.NAME, criRepCalleAdapterVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, criRepCalleAdapterVO);
				
				// Envio el VO al request
				request.setAttribute(CriRepCalleAdapter.NAME, criRepCalleAdapterVO);

				return mapping.findForward(PadConstants.FWD_CRIREPCALLE_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CriRepCalleAdapter.NAME);
			}
		}

}
