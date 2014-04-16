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

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.DeudaProMasPlanillasDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

// Administra las planillas de las deudas a incluir del proceso de envio a judicial

public final class AdministrarDeudaIncProMasPlanillasDeudaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDeudaIncProMasPlanillasDeudaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			String act = getCurrentAct(request);
			
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
					GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO); 
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			DeudaProMasPlanillasDeudaAdapter deudaIncProMasPlanillasDeudaAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {

				// obtengo el id del envio judicial
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (act.equals(GdeConstants.ACT_PLANILLAS_DEUDA_INC_PRO_MAS)) {
					stringServicio = "getDeudaIncProMasPlanillasDeudaAdapter(userSession, commonKey)";
					deudaIncProMasPlanillasDeudaAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getDeudaIncProMasPlanillasDeudaAdapter(userSession, commonKey);
					actionForward = mapping.findForward(GdeConstants.FWD_DEUDA_INC_PRO_MAS_PLANILLAS_DEUDA_ADAPTER);
				}else if (act.equals(GdeConstants.ACT_PLANILLAS_CONVENIO_CUOTA_INC_PRO_MAS)) {
					stringServicio = "getConvenioCuotaIncProMasPlanillasDeudaAdapter(userSession, commonKey)";
					deudaIncProMasPlanillasDeudaAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getConvenioCuotaIncProMasPlanillasDeudaAdapter(userSession, commonKey);
					actionForward = mapping.findForward(GdeConstants.FWD_DEUDA_INC_PRO_MAS_PLANILLAS_DEUDA_ADAPTER);
				}else if (act.equals(GdeConstants.ACT_PLANILLAS_CUENTA_INC_PRO_MAS)) {
					stringServicio = "getCuentaIncProMasPlanillasDeudaAdapter(userSession, commonKey)";
					deudaIncProMasPlanillasDeudaAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getCuentaIncProMasPlanillasDeudaAdapter(userSession, commonKey);
					actionForward = mapping.findForward(GdeConstants.FWD_DEUDA_INC_PRO_MAS_PLANILLAS_DEUDA_ADAPTER);
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio);
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (deudaIncProMasPlanillasDeudaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + deudaIncProMasPlanillasDeudaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DeudaProMasPlanillasDeudaAdapter.NAME, deudaIncProMasPlanillasDeudaAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				deudaIncProMasPlanillasDeudaAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + DeudaProMasPlanillasDeudaAdapter.NAME + ": "+ deudaIncProMasPlanillasDeudaAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(DeudaProMasPlanillasDeudaAdapter.NAME, deudaIncProMasPlanillasDeudaAdapterVO);
				// Subo el apdater al userSession
				userSession.put(DeudaProMasPlanillasDeudaAdapter.NAME, deudaIncProMasPlanillasDeudaAdapterVO);
				 
				saveDemodaMessages(request, deudaIncProMasPlanillasDeudaAdapterVO);
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DeudaProMasPlanillasDeudaAdapter.NAME);
			}
		}
	
	public ActionForward verPlanilla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			DeudaProMasPlanillasDeudaAdapter deudaIncProMasPlanillasDeudaAdapterVO = (DeudaProMasPlanillasDeudaAdapter) userSession.get(DeudaProMasPlanillasDeudaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (deudaIncProMasPlanillasDeudaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaProMasPlanillasDeudaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaProMasPlanillasDeudaAdapter.NAME); 
			}

			// no hacemos populate

			// obtenemos el nombre del archivo seleccionado
			String fileName = request.getParameter("fileParam");
			
			baseResponseFile(response, fileName);
            log.debug("finalizando: " + funcName);
			// se prefiere antes que el null: ver
			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaProMasPlanillasDeudaAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, DeudaProMasPlanillasDeudaAdapter.NAME);
	}
		
}
