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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteAdapter;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncContribuyenteDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncContribuyenteDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CONTRIBUYENTE_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ContribuyenteAdapter contribuyenteAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getContribuyenteAdapterForUpdate(userSession, commonKey)";
				contribuyenteAdapterVO = PadServiceLocator.getContribuyenteService().getContribuyenteAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_CONTRIBUYENTE_ENC_EDIT_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (contribuyenteAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio 
					+ ": " + contribuyenteAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					ContribuyenteAdapter.ENC_NAME, contribuyenteAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			contribuyenteAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " 
				+ ContribuyenteAdapter.ENC_NAME + ": "+ contribuyenteAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ContribuyenteAdapter.ENC_NAME, contribuyenteAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ContribuyenteAdapter.ENC_NAME, contribuyenteAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribuyenteAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			PadSecurityConstants.ABM_CONTRIBUYENTE_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContribuyenteAdapter contribuyenteAdapterVO = (ContribuyenteAdapter) 
				userSession.get(ContribuyenteAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (contribuyenteAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + 
					ContribuyenteAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribuyenteAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(contribuyenteAdapterVO, request);
			
            // Tiene errores recuperables
			if (contribuyenteAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribuyenteAdapterVO.infoString()); 
				saveDemodaErrors(request, contribuyenteAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					ContribuyenteAdapter.ENC_NAME, contribuyenteAdapterVO);
			}
			
			// llamada al servicio
			ContribuyenteVO contribuyenteVO = PadServiceLocator.getContribuyenteService().updateContribuyente
				(userSession, contribuyenteAdapterVO.getContribuyente());
			
            // Tiene errores recuperables
			if (contribuyenteVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribuyenteAdapterVO.infoString()); 
				saveDemodaErrors(request, contribuyenteVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					ContribuyenteAdapter.ENC_NAME, contribuyenteAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (contribuyenteVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contribuyenteAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					ContribuyenteAdapter.ENC_NAME, contribuyenteAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ContribuyenteAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribuyenteAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ContribuyenteAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ContribuyenteAdapter.ENC_NAME);
		
	}
	
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContribuyenteAdapter adapterVO = (ContribuyenteAdapter)userSession.get(ContribuyenteAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContribuyenteAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribuyenteAdapter.ENC_NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getContribuyente().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getContribuyente()); 
			
			adapterVO.getContribuyente().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(ContribuyenteAdapter.ENC_NAME, adapterVO);
			
			return mapping.findForward( PadConstants.FWD_CONTRIBUYENTE_ENC_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribuyenteAdapter.ENC_NAME);
		}	
	}
}
	
