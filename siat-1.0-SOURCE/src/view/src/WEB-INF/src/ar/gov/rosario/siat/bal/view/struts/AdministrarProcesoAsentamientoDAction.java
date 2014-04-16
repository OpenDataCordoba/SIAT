//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.AsentamientoVO;
import ar.gov.rosario.siat.bal.iface.model.ProcesoAsentamientoAdapter;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarProcesoAsentamientoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProcesoAsentamientoDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
			UserSession userSession = getCurrentUserSession(request, mapping);
			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			ProcesoAsentamientoAdapter procesoAsentamientoAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
			
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());

				if (navModel.getAct().equals(BalConstants.ACT_ADM_PROCESO_ASENTAMIENTO)) {
					stringServicio = "getProcesoAsentamientoAdapterForInit(userSession, commonKey)";
					procesoAsentamientoAdapterVO = BalServiceLocator.getAsentamientoService().getProcesoAsentamientoAdapterInit(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_PROCESO_ASENTAMIENTO_ADAPTER);
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (procesoAsentamientoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + procesoAsentamientoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoAsentamientoAdapter.NAME, procesoAsentamientoAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				procesoAsentamientoAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcesoAsentamientoAdapter.NAME + ": "+ procesoAsentamientoAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(ProcesoAsentamientoAdapter.NAME, procesoAsentamientoAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ProcesoAsentamientoAdapter.NAME, procesoAsentamientoAdapterVO);
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ProcesoAsentamientoAdapter.NAME);
			}
		}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoAsentamientoAdapter procesoAsentamientoAdapterVO = (ProcesoAsentamientoAdapter) userSession.get(ProcesoAsentamientoAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoAsentamientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoAsentamientoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoAsentamientoAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			//DemodaUtil.populateVO(procesoAsentamientoAdapterVO, request);
			String logDetalladoEnabled = request.getParameter("logDetalladoEnabled");
			if(logDetalladoEnabled != null)
				procesoAsentamientoAdapterVO.getAsentamiento().setLogDetalladoEnabled(true);
			else
				procesoAsentamientoAdapterVO.getAsentamiento().setLogDetalladoEnabled(false);
			
			// Llamar a un servicio especifico para el Proceso de Asentamiento
			AsentamientoVO asentamientoVO = BalServiceLocator.getAsentamientoService().activar(userSession, procesoAsentamientoAdapterVO.getAsentamiento());

			// Tiene errores recuperables
			if (asentamientoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + asentamientoVO.infoString()); 
				saveDemodaErrors(request, asentamientoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoAsentamientoAdapter.NAME, procesoAsentamientoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (asentamientoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + asentamientoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoAsentamientoAdapter.NAME, procesoAsentamientoAdapterVO);
			}
					
			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoAsentamientoAdapterVO.getAsentamiento().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_CORRIDA, ProConstants.ACT_ACTIVAR);
	}

	public ActionForward reprogramar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)	throws Exception {

			String funcName = DemodaUtil.currentMethodName();

			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoAsentamientoAdapter procesoAsentamientoAdapterVO = (ProcesoAsentamientoAdapter) userSession.get(ProcesoAsentamientoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoAsentamientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoAsentamientoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoAsentamientoAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Asentamiento
			AsentamientoVO asentamientoVO = BalServiceLocator.getAsentamientoService().reprogramar(userSession, procesoAsentamientoAdapterVO.getAsentamiento());

			// Tiene errores recuperables
			if (asentamientoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + asentamientoVO.infoString()); 
				saveDemodaErrors(request, asentamientoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoAsentamientoAdapter.NAME, procesoAsentamientoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (asentamientoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + asentamientoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoAsentamientoAdapter.NAME, procesoAsentamientoAdapterVO);
			}

			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoAsentamientoAdapterVO.getAsentamiento().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_CORRIDA, ProConstants.ACT_ACTIVAR);
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
			ProcesoAsentamientoAdapter procesoAsentamientoAdapterVO = (ProcesoAsentamientoAdapter) userSession.get(ProcesoAsentamientoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoAsentamientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoAsentamientoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoAsentamientoAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Asentamiento
			AsentamientoVO asentamientoVO = BalServiceLocator.getAsentamientoService().cancelar(userSession, procesoAsentamientoAdapterVO.getAsentamiento());

			// Tiene errores recuperables
			if (asentamientoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + asentamientoVO.infoString()); 
				saveDemodaErrors(request, asentamientoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoAsentamientoAdapter.NAME, procesoAsentamientoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (asentamientoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + asentamientoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoAsentamientoAdapter.NAME, procesoAsentamientoAdapterVO);
			}

			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoAsentamientoAdapterVO.getAsentamiento().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_CORRIDA, ProConstants.ACT_CANCELAR);
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
			ProcesoAsentamientoAdapter procesoAsentamientoAdapterVO = (ProcesoAsentamientoAdapter) userSession.get(ProcesoAsentamientoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoAsentamientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoAsentamientoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoAsentamientoAdapter.NAME); 
			}
						
			return baseForwardAdapter(mapping, request, funcName, BalConstants.FWD_ADMINISTRAR_CORRIDA_PRO_ASE, BalConstants.ACT_REINICIAR);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ASENTAMIENTO);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, ProcesoAsentamientoAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ProcesoAsentamientoAdapter.NAME);
	}
	
	public ActionForward downloadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);

			try {
				//NavModel navModel = userSession.getNavModel();
				
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
				return baseException(mapping, request, funcName, exception, ProcesoAsentamientoAdapter.NAME);
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
			ProcesoAsentamientoAdapter procesoAsentamientoAdapterVO = (ProcesoAsentamientoAdapter) userSession.get(ProcesoAsentamientoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoAsentamientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoAsentamientoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoAsentamientoAdapter.NAME); 
			}
			
			CommonKey idCorrida = new CommonKey(procesoAsentamientoAdapterVO.getAsentamiento().getCorrida().getId());

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

	public ActionForward forzar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoAsentamientoAdapter procesoAsentamientoAdapterVO = (ProcesoAsentamientoAdapter) userSession.get(ProcesoAsentamientoAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoAsentamientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoAsentamientoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoAsentamientoAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			String logDetalladoEnabled = request.getParameter("logDetalladoEnabled");
			if(logDetalladoEnabled != null)
				procesoAsentamientoAdapterVO.getAsentamiento().setLogDetalladoEnabled(true);
			else
				procesoAsentamientoAdapterVO.getAsentamiento().setLogDetalladoEnabled(false);
			
			return baseForwardAdapter(mapping, request, funcName, BalConstants.FWD_ADMINISTRAR_CORRIDA_PRO_ASE, BalConstants.ACT_FORZAR);
			
	}

}
