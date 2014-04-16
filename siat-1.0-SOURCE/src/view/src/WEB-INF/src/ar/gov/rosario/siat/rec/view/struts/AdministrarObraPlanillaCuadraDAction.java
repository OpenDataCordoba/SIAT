//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

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
import ar.gov.rosario.siat.rec.iface.model.ObraAdapter;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraAdapter;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraSearchPage;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraVO;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarObraPlanillaCuadraDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlanillaCuadraDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_OBRA_PLANILLACUADRA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanillaCuadraAdapter planillaCuadraAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPlanillaCuadraAdapterForView(userSession, commonKey)";
				planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_OBRAPLANILLACUADRA_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlanillaCuadraAdapterForView(userSession, commonKey)";
				planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterForView(userSession, commonKey);				
				planillaCuadraAdapterVO.addMessage(BaseError.MSG_ELIMINAR, RecError.OBRAPLANILLACUADRA_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_OBRAPLANILLACUADRA_VIEW_ADAPTER);				
			}
			if (act.equals(RecConstants.ACT_CAMBIAR_ESTADO)) {
				stringServicio = "getPlanillaCuadraAdapterForView(userSession, commonKey)";
				planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterForCambiarEstado
					(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_OBRAPLANILLACUADRA_VIEW_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (planillaCuadraAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + planillaCuadraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanillaCuadraAdapter.NAME, planillaCuadraAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planillaCuadraAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlanillaCuadraAdapter.NAME + ": "+ planillaCuadraAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanillaCuadraAdapter.NAME, planillaCuadraAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanillaCuadraAdapter.NAME, planillaCuadraAdapterVO);
			 
			saveDemodaMessages(request, planillaCuadraAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_OBRA_PLANILLACUADRA, 
			BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanillaCuadraSearchPage planillaCuadraSearchPageVO= (PlanillaCuadraSearchPage) 
				userSession.get(PlanillaCuadraSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (planillaCuadraSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + PlanillaCuadraSearchPage.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planillaCuadraSearchPageVO,request);
			
            // Tiene errores recuperables
			if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + 
					planillaCuadraSearchPageVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraSearchPageVO);	
				return forwardErrorRecoverable(mapping, request, userSession, 
					PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
			}
			
			//recupero la obra cargada en session para sacar su id
			ObraAdapter obraAdapterVO = (ObraAdapter) userSession.get(ObraAdapter.NAME);
			// seteo el is de obra para poder asociar la planilla a la obra
			planillaCuadraSearchPageVO.getPlanillaCuadra().getObra().setId(obraAdapterVO.getObra().getId());

			// llamada al servicio
			planillaCuadraSearchPageVO = RecServiceLocator.getCdmService().createObraPlanillaCuadra
				(userSession, planillaCuadraSearchPageVO);
			
            // Tiene errores recuperables
			if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraSearchPageVO);
				request.setAttribute(PlanillaCuadraAdapter.NAME, planillaCuadraSearchPageVO);
				return mapping.findForward(RecConstants.FWD_OBRAPLANILLACUADRA_SELECT_SEARCHPAGE);
			}

			// Tiene errores no recuperables
			if (planillaCuadraSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planillaCuadraSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					PlanillaCuadraAdapter.NAME, planillaCuadraSearchPageVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanillaCuadraSearchPage.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_OBRA_PLANILLACUADRA, 
			BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanillaCuadraAdapter planillaCuadraAdapterVO = (PlanillaCuadraAdapter) 
				userSession.get(PlanillaCuadraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planillaCuadraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanillaCuadraAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraAdapter.NAME); 
			}

			// llamada al servicio
			PlanillaCuadraVO planillaCuadraVO = RecServiceLocator.getCdmService().deleteObraPlanillaCuadra
				(userSession, planillaCuadraAdapterVO.getPlanillaCuadra());
			
            // Tiene errores recuperables
			if (planillaCuadraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString());
				saveDemodaErrors(request, planillaCuadraVO);				
				request.setAttribute(PlanillaCuadraAdapter.NAME, planillaCuadraAdapterVO);
				return mapping.findForward(RecConstants.FWD_OBRAPLANILLACUADRA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planillaCuadraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planillaCuadraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					PlanillaCuadraAdapter.NAME, planillaCuadraAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanillaCuadraAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraAdapter.NAME);
		}
	}
	
	public ActionForward cambiarEstado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			RecSecurityConstants.ABM_OBRA_PLANILLACUADRA, RecSecurityConstants.MTD_OBRA_PLANILLACUADRA_CAMBIAR_ESTADO);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanillaCuadraAdapter planillaCuadraAdapterVO = 
				(PlanillaCuadraAdapter) userSession.get(PlanillaCuadraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planillaCuadraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanillaCuadraAdapter.NAME 
					+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planillaCuadraAdapterVO, request);
			
            // Tiene errores recuperables
			if (planillaCuadraAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraAdapterVO);
				return mapping.findForward(RecConstants.FWD_OBRAPLANILLACUADRA_VIEW_ADAPTER);
			}
			
			// llamada al servicio
			PlanillaCuadraVO planillaCuadraVO = 
				RecServiceLocator.getCdmService().cambiarEstadoPlanillaCuadra
				(userSession, planillaCuadraAdapterVO);
			
            // Tiene errores recuperables
			if (planillaCuadraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraVO);
				request.setAttribute(PlanillaCuadraAdapter.NAME, planillaCuadraAdapterVO);					
				return mapping.findForward(RecConstants.FWD_OBRAPLANILLACUADRA_VIEW_ADAPTER);					
			}
			
			// Tiene errores no recuperables
			if (planillaCuadraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planillaCuadraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					PlanillaCuadraAdapter.NAME, planillaCuadraAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanillaCuadraAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanillaCuadraAdapter.NAME);
		
	}
		
}
