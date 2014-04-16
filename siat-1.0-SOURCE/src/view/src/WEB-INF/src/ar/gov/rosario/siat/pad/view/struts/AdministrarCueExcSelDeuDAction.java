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
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelDeuAdapter;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelDeuVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarCueExcSelDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCueExcSelDeuDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUEEXCSELDEU, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CueExcSelDeuAdapter cueExcSelDeuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getCueExcSelDeuAdapterForView(userSession, commonKey)";
				cueExcSelDeuAdapterVO = PadServiceLocator.getCuentaService().getCueExcSelDeuAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_CUEEXCSELDEU_VIEW_ADAPTER);				
			}
			
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getCueExcSelDeuAdapterForCreate(userSession, commonKey)";
				cueExcSelDeuAdapterVO = PadServiceLocator.getCuentaService().getCueExcSelDeuAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_CUEEXCSELDEU_ADAPTER);				
			}
			
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getCueExcSelDeuAdapterForView(userSession, commonKey)";
				cueExcSelDeuAdapterVO = PadServiceLocator.getCuentaService().getCueExcSelDeuAdapterForView(userSession, commonKey);
				cueExcSelDeuAdapterVO.addMessage(BaseError.MSG_ACTIVAR, PadError.CUEEXCSEL_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_CUEEXCSELDEU_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getCueExcSelAdapterForView(userSession, commonKey)";
				cueExcSelDeuAdapterVO = PadServiceLocator.getCuentaService().getCueExcSelDeuAdapterForView(userSession, commonKey);
				cueExcSelDeuAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, PadError.CUEEXCSEL_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_CUEEXCSELDEU_VIEW_ADAPTER);				
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (cueExcSelDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cueExcSelDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExcSelDeuAdapter.NAME, cueExcSelDeuAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			cueExcSelDeuAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CueExcSelDeuAdapter.NAME + ": "+ cueExcSelDeuAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CueExcSelDeuAdapter.NAME, cueExcSelDeuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CueExcSelDeuAdapter.NAME, cueExcSelDeuAdapterVO);
			 
			saveDemodaMessages(request, cueExcSelDeuAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExcSelDeuAdapter.NAME);
		}
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUEEXCSELDEU, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExcSelDeuAdapter cueExcSelDeuAdapterVO = (CueExcSelDeuAdapter) userSession.get(CueExcSelDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExcSelDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExcSelDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExcSelDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cueExcSelDeuAdapterVO, request);

			// Validamos si se seleccionó Deuda a Excluir.
			if (cueExcSelDeuAdapterVO.getListIdDeudaSelected() == null || cueExcSelDeuAdapterVO.getListIdDeudaSelected().length == 0 ) {
				cueExcSelDeuAdapterVO.addRecoverableError(PadError.CUEEXCSELDEU_MSJ_NINGUNO_SELECTED, PadError.CUEEXCSELDEU_DEUDA_LABEL);
			}
			
            // Tiene errores recuperables
			if (cueExcSelDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExcSelDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExcSelDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExcSelDeuAdapter.NAME, cueExcSelDeuAdapterVO);
			}
			
			//	llamada al servicio
			cueExcSelDeuAdapterVO = PadServiceLocator.getCuentaService().createCueExcSelDeuList(userSession, cueExcSelDeuAdapterVO);
					
			// Tiene errores recuperables
			if (cueExcSelDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExcSelDeuAdapterVO.infoString()); 
				cueExcSelDeuAdapterVO.getLiqDeudaAdapter().setListIdDeudaSelected(cueExcSelDeuAdapterVO.getListIdDeudaSelected());
				
				saveDemodaErrors(request, cueExcSelDeuAdapterVO);
				saveDemodaMessages(request, cueExcSelDeuAdapterVO);				
				return forwardErrorRecoverable(mapping, request, userSession, CueExcSelDeuAdapter.NAME, cueExcSelDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cueExcSelDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExcSelDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExcSelDeuAdapter.NAME, cueExcSelDeuAdapterVO);
			}
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CueExcSelDeuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExcSelDeuAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CueExcSelDeuAdapter.NAME);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, CueExcSelDeuAdapter.NAME);
			
		}

	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUEEXCSELDEU, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExcSelDeuAdapter cueExcSelDeuAdapterVO = (CueExcSelDeuAdapter) userSession.get(CueExcSelDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExcSelDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExcSelDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExcSelDeuAdapter.NAME); 
			}

			// llamada al servicio
			CueExcSelDeuVO cueExcSelDeuVO = PadServiceLocator.getCuentaService().activarCueExcSelDeu
				(userSession, cueExcSelDeuAdapterVO.getCueExcSelDeu());
			
            // Tiene errores recuperables
			if (cueExcSelDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExcSelDeuAdapterVO.infoString());
				saveDemodaErrors(request, cueExcSelDeuVO);				
				request.setAttribute(CueExcSelDeuAdapter.NAME, cueExcSelDeuAdapterVO);
				return mapping.findForward(PadConstants.FWD_CUEEXCSELDEU_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (cueExcSelDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExcSelDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExcSelDeuAdapter.NAME, cueExcSelDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CueExcSelDeuAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExcSelDeuAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUEEXCSELDEU, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExcSelDeuAdapter cueExcSelDeuAdapterVO = (CueExcSelDeuAdapter) userSession.get(CueExcSelDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExcSelDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExcSelDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExcSelDeuAdapter.NAME); 
			}

			// llamada al servicio
			CueExcSelDeuVO cueExcSelDeuVO = PadServiceLocator.getCuentaService().desactivarCueExcSelDeu
				(userSession, cueExcSelDeuAdapterVO.getCueExcSelDeu());
			
            // Tiene errores recuperables
			if (cueExcSelDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExcSelDeuAdapterVO.infoString());
				saveDemodaErrors(request, cueExcSelDeuVO);				
				request.setAttribute(CueExcSelDeuAdapter.NAME, cueExcSelDeuAdapterVO);
				return mapping.findForward(PadConstants.FWD_CUEEXCSELDEU_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (cueExcSelDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExcSelDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExcSelDeuAdapter.NAME, cueExcSelDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CueExcSelDeuAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExcSelDeuAdapter.NAME);
		}	
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
			CueExcSelDeuAdapter adapterVO = (CueExcSelDeuAdapter)userSession.get(CueExcSelDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExcSelDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExcSelDeuAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getCueExcSelDeu().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getCueExcSelDeu()); 
			
			adapterVO.getCueExcSelDeu().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
		    adapterVO.getLiqDeudaAdapter().setListIdDeudaSelected(adapterVO.getListIdDeudaSelected());
		    
			request.setAttribute(CueExcSelDeuAdapter.NAME, adapterVO);
			
			return mapping.findForward( PadConstants.FWD_CUEEXCSELDEU_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExcSelDeuAdapter.NAME);
		}	
	}
	
}
