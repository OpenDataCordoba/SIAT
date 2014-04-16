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
import ar.gov.rosario.siat.gde.iface.model.LiqComVO;
import ar.gov.rosario.siat.gde.iface.model.ProcesoLiqComAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarProcesoLiqComDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProcesoLiqComDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
			UserSession userSession = getCurrentUserSession(request, mapping);
			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			ProcesoLiqComAdapter procesoLiqComAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
			
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());

				if (navModel.getAct().equals(GdeConstants.ACT_ADM_PROCESO_LIQCOM)) {
					stringServicio = "getProcesoLiqComAdapterInit(userSession, commonKey)";
					procesoLiqComAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getProcesoLiqComAdapterInit(userSession, commonKey);
					actionForward = mapping.findForward(GdeConstants.FWD_PROCESO_LIQCOM_ADAPTER);
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (procesoLiqComAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + procesoLiqComAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoLiqComAdapter.NAME, procesoLiqComAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				procesoLiqComAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcesoLiqComAdapter.NAME + ": "+ procesoLiqComAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(ProcesoLiqComAdapter.NAME, procesoLiqComAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ProcesoLiqComAdapter.NAME, procesoLiqComAdapterVO);
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ProcesoLiqComAdapter.NAME);
			}
		}

	public ActionForward activar(ActionMapping mapping, ActionForm ACTION_ADMINISTRAR_LIQCOMform,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoLiqComAdapter procesoLiqComAdapterVO = (ProcesoLiqComAdapter) userSession.get(ProcesoLiqComAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoLiqComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoLiqComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoLiqComAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Asentamiento
			LiqComVO liqComVO = GdeServiceLocator.getGestionDeudaJudicialService().activar(userSession, procesoLiqComAdapterVO.getLiqCom());

			// Tiene errores recuperables
			if (liqComVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComVO.infoString()); 
				saveDemodaErrors(request, liqComVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoLiqComAdapter.NAME, procesoLiqComAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (liqComVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + liqComVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoLiqComAdapter.NAME, procesoLiqComAdapterVO);
			}
						
			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoLiqComAdapterVO.getLiqCom().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_LIQCOM, ProConstants.ACT_ACTIVAR);
	}

	public ActionForward reprogramar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)	throws Exception {

			String funcName = DemodaUtil.currentMethodName();

			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoLiqComAdapter procesoLiqComAdapterVO = (ProcesoLiqComAdapter) userSession.get(ProcesoLiqComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoLiqComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoLiqComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoLiqComAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Asentamiento
			LiqComVO liqComVO = GdeServiceLocator.getGestionDeudaJudicialService().reprogramar(userSession, procesoLiqComAdapterVO.getLiqCom());

			// Tiene errores recuperables
			if (liqComVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComVO.infoString()); 
				saveDemodaErrors(request, liqComVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoLiqComAdapter.NAME, procesoLiqComAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (liqComVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + liqComVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoLiqComAdapter.NAME, procesoLiqComAdapterVO);
			}

			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoLiqComAdapterVO.getLiqCom().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_LIQCOM, ProConstants.ACT_REPROGRAMAR);
	}

	public ActionForward cancelar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoLiqComAdapter procesoLiqComAdapterVO = (ProcesoLiqComAdapter) userSession.get(ProcesoLiqComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoLiqComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoLiqComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoLiqComAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Asentamiento
			LiqComVO liqComVO = GdeServiceLocator.getGestionDeudaJudicialService().cancelar(userSession, procesoLiqComAdapterVO.getLiqCom());

			// Tiene errores recuperables
			if (liqComVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComVO.infoString()); 
				saveDemodaErrors(request, liqComVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoLiqComAdapter.NAME, procesoLiqComAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (liqComVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + liqComVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoLiqComAdapter.NAME, procesoLiqComAdapterVO);
			}

			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoLiqComAdapterVO.getLiqCom().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_LIQCOM, ProConstants.ACT_CANCELAR);
	}
	
	public ActionForward reiniciar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoLiqComAdapter procesoLiqComAdapterVO = (ProcesoLiqComAdapter) userSession.get(ProcesoLiqComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoLiqComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoLiqComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoLiqComAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Asentamiento
			LiqComVO liqComVO = GdeServiceLocator.getGestionDeudaJudicialService().reiniciar(userSession, procesoLiqComAdapterVO.getLiqCom());

			// Tiene errores recuperables
			if (liqComVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqComVO.infoString()); 
				saveDemodaErrors(request, liqComVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoLiqComAdapter.NAME, procesoLiqComAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (liqComVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + liqComVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoLiqComAdapter.NAME, procesoLiqComAdapterVO);
			}

			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoLiqComAdapterVO.getLiqCom().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_LIQCOM, ProConstants.ACT_REINICIAR);			
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_LIQCOM);
	}
	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, ProcesoLiqComAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ProcesoLiqComAdapter.NAME);
	}
	
	public ActionForward downloadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);

			try {
				NavModel navModel = userSession.getNavModel();
				
				String fileId = request.getParameter("fileParam");	
				CommonKey commonKey = new CommonKey(fileId);

				// Obtenemos el id del archivo seleccionado
				Long idFileCorrida = commonKey.getId();			

				// Obtenemos el nombre del archivo seleccionado mediante una llamada a un servicio
				String fileName = ProServiceLocator.getAdpProcesoService().obtenerFileCorridaName(idFileCorrida);
				
				baseResponseFile(response,fileName);

				log.debug("exit: " + funcName);
				
				return null;
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ProcesoLiqComAdapter.NAME);
			}
	}

	public ActionForward downloadLogFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)	throws Exception {

			String funcName = DemodaUtil.currentMethodName();

			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoLiqComAdapter procesoLiqComAdapterVO = (ProcesoLiqComAdapter) userSession.get(ProcesoLiqComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoLiqComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoLiqComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoLiqComAdapter.NAME); 
			}
			
			CommonKey idCorrida = new CommonKey(procesoLiqComAdapterVO.getLiqCom().getCorrida().getId());

			// llamada al servicio
			String fileName = ProServiceLocator.getAdpProcesoService().getLogFile(userSession, idCorrida);
			
			if(fileName == null){			
				log.error("error en: "  + funcName + ": No se pudo formar logFileName para la corrida con id="+idCorrida.getId()+".");
				return forwardErrorSession(request); 
			}
			try{
				baseResponseFile(response,fileName);								
			}catch(Exception e){
				log.error("Error al abrir archivo: ", e);
				return null;
			}

			log.debug("exit: " + funcName);
			
			return null;
		}
	

}
