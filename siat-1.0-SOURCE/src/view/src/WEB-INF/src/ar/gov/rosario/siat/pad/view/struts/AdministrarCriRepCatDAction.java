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
import ar.gov.rosario.siat.pad.iface.model.CriRepCatAdapter;
import ar.gov.rosario.siat.pad.iface.model.CriRepCatVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarCriRepCatDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCriRepCatDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CRIREPCAT, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			CriRepCatAdapter criRepCatAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getCriRepCatAdapterForView(userSession, commonKey)";
					criRepCatAdapterVO = PadServiceLocator.getDistribucionService().getCriRepCatAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(PadConstants.FWD_CRIREPCAT_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getCriRepCatAdapterForUpdate(userSession, commonKey)";
					criRepCatAdapterVO = PadServiceLocator.getDistribucionService().getCriRepCatAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(PadConstants.FWD_CRIREPCAT_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getCriRepCatAdapterForDelete(userSession, commonKey)";
					criRepCatAdapterVO = PadServiceLocator.getDistribucionService().getCriRepCatAdapterForView
						(userSession, commonKey);
					criRepCatAdapterVO.addMessage(BaseError.MSG_ELIMINAR, PadError.CRIREPCAT_LABEL);				
					actionForward = mapping.findForward(PadConstants.FWD_CRIREPCAT_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getCriRepCatAdapterForCreate(userSession)";
					criRepCatAdapterVO = PadServiceLocator.getDistribucionService().getCriRepCatAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(PadConstants.FWD_CRIREPCAT_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (criRepCatAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + criRepCatAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CriRepCatAdapter.NAME, criRepCatAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				criRepCatAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + CriRepCatAdapter.NAME + ": "+ criRepCatAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(CriRepCatAdapter.NAME, criRepCatAdapterVO);
				// Subo el apdater al userSession
				userSession.put(CriRepCatAdapter.NAME, criRepCatAdapterVO);
				
				saveDemodaMessages(request, criRepCatAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CriRepCatAdapter.NAME);
			}
		}
	
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				PadSecurityConstants.ABM_CRIREPCAT, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				CriRepCatAdapter criRepCatAdapterVO = (CriRepCatAdapter) userSession.get(CriRepCatAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (criRepCatAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CriRepCatAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CriRepCatAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(criRepCatAdapterVO, request);
				
	            // Tiene errores recuperables
				if (criRepCatAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + criRepCatAdapterVO.infoString()); 
					saveDemodaErrors(request, criRepCatAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, CriRepCatAdapter.NAME, criRepCatAdapterVO);
				}
				
				// llamada al servicio
				CriRepCatVO criRepCatVO = PadServiceLocator.getDistribucionService().createCriRepCat(userSession, criRepCatAdapterVO.getCriRepCat());
				
	            // Tiene errores recuperables
				if (criRepCatVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + criRepCatVO.infoString()); 
					saveDemodaErrors(request, criRepCatVO);
					return forwardErrorRecoverable(mapping, request, userSession, CriRepCatAdapter.NAME, criRepCatAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (criRepCatVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + criRepCatVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CriRepCatAdapter.NAME, criRepCatAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, CriRepCatAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CriRepCatAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				PadSecurityConstants.ABM_CRIREPCAT, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				CriRepCatAdapter criRepCatAdapterVO = (CriRepCatAdapter) userSession.get(CriRepCatAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (criRepCatAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CriRepCatAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CriRepCatAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(criRepCatAdapterVO, request);
				
	            // Tiene errores recuperables
				if (criRepCatAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + criRepCatAdapterVO.infoString()); 
					saveDemodaErrors(request, criRepCatAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						CriRepCatAdapter.NAME, criRepCatAdapterVO);
				}
				
				// llamada al servicio
				CriRepCatVO criRepCatVO = PadServiceLocator.getDistribucionService().updateCriRepCat(userSession, criRepCatAdapterVO.getCriRepCat());
				
	            // Tiene errores recuperables
				if (criRepCatVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + criRepCatAdapterVO.infoString()); 
					saveDemodaErrors(request, criRepCatVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						CriRepCatAdapter.NAME, criRepCatAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (criRepCatVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + criRepCatAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						CriRepCatAdapter.NAME, criRepCatAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, CriRepCatAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CriRepCatAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				PadSecurityConstants.ABM_CRIREPCAT, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				CriRepCatAdapter criRepCatAdapterVO = (CriRepCatAdapter) userSession.get(CriRepCatAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (criRepCatAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CriRepCatAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CriRepCatAdapter.NAME); 
				}

				// llamada al servicio
				CriRepCatVO criRepCatVO = PadServiceLocator.getDistribucionService().deleteCriRepCat(userSession, criRepCatAdapterVO.getCriRepCat());
				
	            // Tiene errores recuperables
				if (criRepCatVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + criRepCatAdapterVO.infoString());
					saveDemodaErrors(request, criRepCatVO);				
					request.setAttribute(CriRepCatAdapter.NAME, criRepCatAdapterVO);
					return mapping.findForward(PadConstants.FWD_CRIREPCAT_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (criRepCatVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + criRepCatAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						CriRepCatAdapter.NAME, criRepCatAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, CriRepCatAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CriRepCatAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, CriRepCatAdapter.NAME);
		}
}
