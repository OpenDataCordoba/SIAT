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
import ar.gov.rosario.siat.gde.iface.model.ProMasProExcAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProMasProExcVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarProMasProExcDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProMasProExcDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROMASPROEXC, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ProMasProExcAdapter proMasProExcAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getProMasProExcAdapterForView(userSession, commonKey)";
				proMasProExcAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getProMasProExcAdapterForView(userSession, commonKey);				
				proMasProExcAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PROMASPROEXC_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PROMASPROEXC_VIEW_ADAPTER);	 			
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getProMasProExcAdapterForCreate(userSession)";
				proMasProExcAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getProMasProExcAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PROMASPROEXC_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (proMasProExcAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + proMasProExcAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProMasProExcAdapter.NAME, proMasProExcAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			proMasProExcAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProMasProExcAdapter.NAME + ": "+ proMasProExcAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ProMasProExcAdapter.NAME, proMasProExcAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProMasProExcAdapter.NAME, proMasProExcAdapterVO);
			 
			saveDemodaMessages(request, proMasProExcAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProMasProExcAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROMASPROEXC, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProMasProExcAdapter proMasProExcAdapterVO = (ProMasProExcAdapter) userSession.get(ProMasProExcAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proMasProExcAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProMasProExcAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProMasProExcAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(proMasProExcAdapterVO, request);
			
            // Tiene errores recuperables
			if (proMasProExcAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proMasProExcAdapterVO.infoString()); 
				saveDemodaErrors(request, proMasProExcAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProMasProExcAdapter.NAME, proMasProExcAdapterVO);
			}
			
			// llamada al servicio
			ProMasProExcVO proMasProExcVO = GdeServiceLocator.getGestionDeudaJudicialService().createProMasProExc(userSession, proMasProExcAdapterVO.getProMasProExc());
			
            // Tiene errores recuperables
			if (proMasProExcVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proMasProExcVO.infoString()); 
				saveDemodaErrors(request, proMasProExcVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProMasProExcAdapter.NAME, proMasProExcAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (proMasProExcVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proMasProExcVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProMasProExcAdapter.NAME, proMasProExcAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProMasProExcAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProMasProExcAdapter.NAME);
		}
	}


	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROMASPROEXC, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProMasProExcAdapter proMasProExcAdapterVO = (ProMasProExcAdapter) userSession.get(ProMasProExcAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proMasProExcAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProMasProExcAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProMasProExcAdapter.NAME); 
			}

			// llamada al servicio
			ProMasProExcVO proMasProExcVO = GdeServiceLocator.getGestionDeudaJudicialService().deleteProMasProExc
				(userSession, proMasProExcAdapterVO.getProMasProExc());
			
            // Tiene errores recuperables
			if (proMasProExcVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proMasProExcAdapterVO.infoString());
				saveDemodaErrors(request, proMasProExcVO);				
				request.setAttribute(ProMasProExcAdapter.NAME, proMasProExcAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PROMASPROEXC_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (proMasProExcVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proMasProExcAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProMasProExcAdapter.NAME, proMasProExcAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProMasProExcAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProMasProExcAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			return baseVolver(mapping, form, request, response, ProMasProExcAdapter.NAME);
		}
}
