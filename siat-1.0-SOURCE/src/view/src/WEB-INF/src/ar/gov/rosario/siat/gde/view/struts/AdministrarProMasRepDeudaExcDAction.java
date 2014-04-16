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
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoReportesDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

// Administra los reportes de las deudas excluidas del proceso de envio a judicial

public final class AdministrarProMasRepDeudaExcDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProMasRepDeudaExcDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			// uso los mismos parametros que se utilizan en la habilitacion del boton desde donde invoco
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
					GdeSecurityConstants.MTD_VER_REPORTES_DEUDA); 
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			String act = getCurrentAct(request);
			
			ProcesoMasivoReportesDeudaAdapter deudaProMasReportesAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {

				// obtengo el id del envio judicial
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				stringServicio = "getProcesoMasivoReportesDeudaExcluidaAdapter(userSession, commonKey)";
				deudaProMasReportesAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getProcesoMasivoReportesDeudaExcluidaAdapter(userSession, commonKey);
				
				if (act.equals(GdeConstants.ACT_REPORTES_DEUDA_PRO_MAS)) {
					stringServicio = "getProcesoMasivoReportesDeudaExcluidaAdapter(userSession, commonKey)";
					deudaProMasReportesAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getProcesoMasivoReportesDeudaExcluidaAdapter(userSession, commonKey);
				}else if (act.equals(GdeConstants.ACT_REPORTES_CONVENIO_CUOTA_PRO_MAS)) {
					stringServicio = "getProcesoMasivoReportesConvenioCuotaExcluidaAdapter(userSession, commonKey)";
					deudaProMasReportesAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getProcesoMasivoReportesConvenioCuotaExcluidaAdapter(userSession, commonKey);
				}
				actionForward = mapping.findForward(GdeConstants.FWD_PRO_MAS_REPORTES_DEUDA_EXC_ADAPTER);
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio);
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (deudaProMasReportesAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + deudaProMasReportesAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoMasivoReportesDeudaAdapter.NAME, deudaProMasReportesAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				deudaProMasReportesAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcesoMasivoReportesDeudaAdapter.NAME + ": "+ deudaProMasReportesAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(ProcesoMasivoReportesDeudaAdapter.NAME, deudaProMasReportesAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ProcesoMasivoReportesDeudaAdapter.NAME, deudaProMasReportesAdapterVO);
				 
				saveDemodaMessages(request, deudaProMasReportesAdapterVO);
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ProcesoMasivoReportesDeudaAdapter.NAME);
			}
		}
	
	public ActionForward verReporte(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el adapter del userSession
			ProcesoMasivoReportesDeudaAdapter deudaProMasReportesAdapterVO = (ProcesoMasivoReportesDeudaAdapter) userSession.get(ProcesoMasivoReportesDeudaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (deudaProMasReportesAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoMasivoReportesDeudaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoReportesDeudaAdapter.NAME); 
			}

			// no hacemos populate

			// obtenemos el nombre del archivo seleccionado
			String fileName = request.getParameter("fileParam");
			baseResponseFile(response, fileName);
            log.debug("finalizando: " + funcName);
			// se prefiere antes que el null: ver
			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoMasivoReportesDeudaAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, ProcesoMasivoReportesDeudaAdapter.NAME);
	}
		
}
