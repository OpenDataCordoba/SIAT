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
import ar.gov.rosario.siat.gde.iface.model.DeudaProMasArmadoSeleccionAdapter;
import ar.gov.rosario.siat.gde.iface.model.SelAlmDeudaVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

// Administra los logs de armado de la deuda a exluir del proceso de envio a judicial
// Limpia la seleccion de deuda excluida: borrando los registros excluidos y borrando los logs de las exclusiones realizadas

public final class AdministrarDeudaIncProMasArmadoSeleccionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDeudaIncProMasArmadoSeleccionDAction.class);
	
	public static String SELEC_ALM_KEY = "seleccionAlmacenadaKey";

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			String act = getCurrentAct(request);
			
			// podriamos usar el MTD en vez del act
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
					GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO); 
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			DeudaProMasArmadoSeleccionAdapter deudaIncProMasLogsArmadoAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {

				// obtengo el id del envio judicial 
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (act.equals(GdeConstants.ACT_LOGS_ARMADO_DEUDA_PRO_MAS)) {
					stringServicio = "getDeudaIncProMasLogsArmadoAdapter(userSession, commonKey)";
					deudaIncProMasLogsArmadoAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getDeudaIncProMasArmadoSeleccionAdapter(userSession, commonKey);
					deudaIncProMasLogsArmadoAdapterVO.setEsLimpiarSeleccion(false);
					actionForward = mapping.findForward(GdeConstants.FWD_DEUDA_INC_PRO_MAS_ARMADO_SELECCION_ADAPTER);
				}
				
				if (act.equals(GdeConstants.ACT_LIMPIAR_SELECCION_DEUDA_PRO_MAS)) {
					stringServicio = "getDeudaIncProMasLogsArmadoAdapter(userSession, commonKey)";
					deudaIncProMasLogsArmadoAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getDeudaIncProMasArmadoSeleccionAdapter(userSession, commonKey);
					deudaIncProMasLogsArmadoAdapterVO.setEsLimpiarSeleccion(true);
					actionForward = mapping.findForward(GdeConstants.FWD_DEUDA_INC_PRO_MAS_ARMADO_SELECCION_ADAPTER);
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio);
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (deudaIncProMasLogsArmadoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + deudaIncProMasLogsArmadoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DeudaProMasArmadoSeleccionAdapter.NAME, deudaIncProMasLogsArmadoAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				deudaIncProMasLogsArmadoAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + DeudaProMasArmadoSeleccionAdapter.NAME + ": "+ deudaIncProMasLogsArmadoAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(DeudaProMasArmadoSeleccionAdapter.NAME, deudaIncProMasLogsArmadoAdapterVO);
				// Subo el apdater al userSession
				userSession.put(DeudaProMasArmadoSeleccionAdapter.NAME, deudaIncProMasLogsArmadoAdapterVO);
				 
				saveDemodaMessages(request, deudaIncProMasLogsArmadoAdapterVO);
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DeudaProMasArmadoSeleccionAdapter.NAME);
			}
		}

	public ActionForward limpiarSeleccion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			// TODO ver constantes del canAccess
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
					GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el Adapter del userSession
				DeudaProMasArmadoSeleccionAdapter deudaIncProMasArmadoSeleccionAdapterVO = (DeudaProMasArmadoSeleccionAdapter) userSession.get(DeudaProMasArmadoSeleccionAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (deudaIncProMasArmadoSeleccionAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DeudaProMasArmadoSeleccionAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DeudaProMasArmadoSeleccionAdapter.NAME); 
				}
				
				// llamada al servicio
				SelAlmDeudaVO selAlmDeudaVO = GdeServiceLocator.getGestionDeudaJudicialService().limpiarSelAlmDetDeudaIncluidaProcesoMasivo(userSession, 
						new CommonKey(deudaIncProMasArmadoSeleccionAdapterVO.getProcesoMasivo().getId()));

				// pasaje de errores
				selAlmDeudaVO.passErrorMessages(deudaIncProMasArmadoSeleccionAdapterVO);
	            // Tiene errores recuperables
				if (deudaIncProMasArmadoSeleccionAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + deudaIncProMasArmadoSeleccionAdapterVO.infoString()); 
					saveDemodaErrors(request, deudaIncProMasArmadoSeleccionAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DeudaProMasArmadoSeleccionAdapter.NAME, deudaIncProMasArmadoSeleccionAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (deudaIncProMasArmadoSeleccionAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + deudaIncProMasArmadoSeleccionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DeudaProMasArmadoSeleccionAdapter.NAME, deudaIncProMasArmadoSeleccionAdapterVO);
				}
				
				// Fue Exitoso
				// esta comentado porque no hace falta: sacar una vez probado
				//le seteo la accion y metodo a donde ir al navModel para que la use despues del Confirmar
				//String actionConfirmacion = "/gde/BuscarDeudaIncProMasEliminar";
				//String methodConfirmacion = "buscar";
				//String act = ""; // no hace falta el act para el metodo buscar del action BuscarDeudaIncProMasEliminar

				return forwardConfirmarOk(mapping, request, funcName, DeudaProMasArmadoSeleccionAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DeudaProMasArmadoSeleccionAdapter.NAME);
			}
		}
	
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, DeudaProMasArmadoSeleccionAdapter.NAME);
	}
		
}
