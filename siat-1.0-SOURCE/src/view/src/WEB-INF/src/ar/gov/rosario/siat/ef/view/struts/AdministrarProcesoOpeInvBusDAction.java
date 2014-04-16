//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.ef.iface.model.OpeInvBusVO;
import ar.gov.rosario.siat.ef.iface.model.ProcesoOpeInvBusAdapter;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarProcesoOpeInvBusDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProcesoOpeInvBusDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
			UserSession userSession = getCurrentUserSession(request, mapping);
			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			ProcesoOpeInvBusAdapter procesoOpeInvBusAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
			
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());

				if (navModel.getAct().equals(EfConstants.ACT_ADM_PROCESO_OPEINVBUS)) {
					stringServicio = "getProcesoOpeInvBusAdapterInit(userSession, commonKey)";
					procesoOpeInvBusAdapterVO = EfServiceLocator.getInvestigacionService().getProcesoOpeInvBusAdapterInit(userSession, commonKey);
					actionForward = mapping.findForward(EfConstants.FWD_PROCESO_OPEINVBUS_ADAPTER);
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (procesoOpeInvBusAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + procesoOpeInvBusAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoOpeInvBusAdapter.NAME, procesoOpeInvBusAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				procesoOpeInvBusAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcesoOpeInvBusAdapter.NAME + ": "+ procesoOpeInvBusAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(ProcesoOpeInvBusAdapter.NAME, procesoOpeInvBusAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ProcesoOpeInvBusAdapter.NAME, procesoOpeInvBusAdapterVO);
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ProcesoOpeInvBusAdapter.NAME);
			}
		}

	public ActionForward activar(ActionMapping mapping, ActionForm ACTION_ADMINISTRAR_OPEINVBUSform,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoOpeInvBusAdapter procesoLiqComAdapterVO = (ProcesoOpeInvBusAdapter) userSession.get(ProcesoOpeInvBusAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoLiqComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoOpeInvBusAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoOpeInvBusAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Asentamiento
			OpeInvBusVO opeInvBusVO = EfServiceLocator.getInvestigacionService().activar(userSession, procesoLiqComAdapterVO.getOpeInvBus());

			// Tiene errores recuperables
			if (opeInvBusVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvBusVO.infoString()); 
				saveDemodaErrors(request, opeInvBusVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoOpeInvBusAdapter.NAME, procesoLiqComAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (opeInvBusVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvBusVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoOpeInvBusAdapter.NAME, procesoLiqComAdapterVO);
			}
						
			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoLiqComAdapterVO.getOpeInvBus().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_OPEINVBUS, ProConstants.ACT_ACTIVAR);
	}

	public ActionForward reprogramar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)	throws Exception {

			String funcName = DemodaUtil.currentMethodName();

			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoOpeInvBusAdapter procesoLiqComAdapterVO = (ProcesoOpeInvBusAdapter) userSession.get(ProcesoOpeInvBusAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoLiqComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoOpeInvBusAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoOpeInvBusAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Asentamiento
			OpeInvBusVO opeInvBusVO = EfServiceLocator.getInvestigacionService().reprogramar(userSession, procesoLiqComAdapterVO.getOpeInvBus());

			// Tiene errores recuperables
			if (opeInvBusVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvBusVO.infoString()); 
				saveDemodaErrors(request, opeInvBusVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoOpeInvBusAdapter.NAME, procesoLiqComAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (opeInvBusVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvBusVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoOpeInvBusAdapter.NAME, procesoLiqComAdapterVO);
			}

			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoLiqComAdapterVO.getOpeInvBus().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_OPEINVBUS, ProConstants.ACT_REPROGRAMAR);
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
			ProcesoOpeInvBusAdapter procesoLiqComAdapterVO = (ProcesoOpeInvBusAdapter) userSession.get(ProcesoOpeInvBusAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoLiqComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoOpeInvBusAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoOpeInvBusAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Asentamiento
			OpeInvBusVO opeInvBusVO = EfServiceLocator.getInvestigacionService().cancelar(userSession, procesoLiqComAdapterVO.getOpeInvBus());

			// Tiene errores recuperables
			if (opeInvBusVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvBusVO.infoString()); 
				saveDemodaErrors(request, opeInvBusVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoOpeInvBusAdapter.NAME, procesoLiqComAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (opeInvBusVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvBusVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoOpeInvBusAdapter.NAME, procesoLiqComAdapterVO);
			}

			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoLiqComAdapterVO.getOpeInvBus().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_OPEINVBUS, ProConstants.ACT_CANCELAR);
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
			ProcesoOpeInvBusAdapter procesoLiqComAdapterVO = (ProcesoOpeInvBusAdapter) userSession.get(ProcesoOpeInvBusAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoLiqComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoOpeInvBusAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoOpeInvBusAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Asentamiento
			OpeInvBusVO opeInvBusVO = EfServiceLocator.getInvestigacionService().reiniciar(userSession, procesoLiqComAdapterVO.getOpeInvBus());

			// Tiene errores recuperables
			if (opeInvBusVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvBusVO.infoString()); 
				saveDemodaErrors(request, opeInvBusVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoOpeInvBusAdapter.NAME, procesoLiqComAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (opeInvBusVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvBusVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoOpeInvBusAdapter.NAME, procesoLiqComAdapterVO);
			}

			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoLiqComAdapterVO.getOpeInvBus().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_OPEINVBUS, ProConstants.ACT_REINICIAR);			
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINVBUS);
	}
	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, ProcesoOpeInvBusAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ProcesoOpeInvBusAdapter.NAME);
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
				return baseException(mapping, request, funcName, exception, ProcesoOpeInvBusAdapter.NAME);
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
			ProcesoOpeInvBusAdapter procesoLiqComAdapterVO = (ProcesoOpeInvBusAdapter) userSession.get(ProcesoOpeInvBusAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoLiqComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoOpeInvBusAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoOpeInvBusAdapter.NAME); 
			}
			
			CommonKey idCorrida = new CommonKey(procesoLiqComAdapterVO.getOpeInvBus().getCorrida().getId());

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
