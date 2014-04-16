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

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import ar.gov.rosario.siat.rec.iface.model.AnulacionObraVO;
import ar.gov.rosario.siat.rec.iface.model.ProcesoAnulacionObraAdapter;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarProcesoAnulacionObraDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProcesoAnulacionObraDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
			UserSession userSession = getCurrentUserSession(request, mapping);
			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			ProcesoAnulacionObraAdapter procesoAnulacionObraAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
			
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());

				if (navModel.getAct().equals(RecConstants.ACT_ADM_PROCESO_ANULACION_OBRA)) {
					stringServicio = "getProcesoAnulacionObraAdapterInit(userSession, commonKey)";
					procesoAnulacionObraAdapterVO = RecServiceLocator.getCdmService().getProcesoAnulacionObraAdapterInit(userSession, commonKey);
					actionForward = mapping.findForward(RecConstants.FWD_PROCESO_ANULACION_OBRA_ADAPTER);
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (procesoAnulacionObraAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + procesoAnulacionObraAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoAnulacionObraAdapter.NAME, procesoAnulacionObraAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				procesoAnulacionObraAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcesoAnulacionObraAdapter.NAME + ": "+ procesoAnulacionObraAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(ProcesoAnulacionObraAdapter.NAME, procesoAnulacionObraAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ProcesoAnulacionObraAdapter.NAME, procesoAnulacionObraAdapterVO);
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ProcesoAnulacionObraAdapter.NAME);
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
			ProcesoAnulacionObraAdapter procesoAnulacionObraAdapterVO = (ProcesoAnulacionObraAdapter) userSession.get(ProcesoAnulacionObraAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoAnulacionObraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoAnulacionObraAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoAnulacionObraAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Anulacion de Obra de CdM
			AnulacionObraVO anulacionObraVO = RecServiceLocator.getCdmService().activar(userSession, procesoAnulacionObraAdapterVO.getAnulacionObra());

			// Tiene errores recuperables
			if (anulacionObraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + anulacionObraVO.infoString()); 
				saveDemodaErrors(request, anulacionObraVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoAnulacionObraAdapter.NAME, procesoAnulacionObraAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (anulacionObraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + anulacionObraVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoAnulacionObraAdapter.NAME, procesoAnulacionObraAdapterVO);
			}
						
			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoAnulacionObraAdapterVO.getAnulacionObra().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_ANU_OBR, ProConstants.ACT_ACTIVAR);
	}

	public ActionForward reprogramar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)	throws Exception {

			String funcName = DemodaUtil.currentMethodName();

			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoAnulacionObraAdapter procesoAnulacionObraAdapterVO = (ProcesoAnulacionObraAdapter) userSession.get(ProcesoAnulacionObraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoAnulacionObraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoAnulacionObraAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoAnulacionObraAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Anulacion de Obra de CdM
			AnulacionObraVO anulacionObraVO = RecServiceLocator.getCdmService().reprogramar(userSession, procesoAnulacionObraAdapterVO.getAnulacionObra());

			// Tiene errores recuperables
			if (anulacionObraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + anulacionObraVO.infoString()); 
				saveDemodaErrors(request, anulacionObraVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoAnulacionObraAdapter.NAME, procesoAnulacionObraAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (anulacionObraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + anulacionObraVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoAnulacionObraAdapter.NAME, procesoAnulacionObraAdapterVO);
			}

			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoAnulacionObraAdapterVO.getAnulacionObra().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_ANU_OBR, ProConstants.ACT_ACTIVAR);
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
			ProcesoAnulacionObraAdapter procesoAnulacionObraAdapterVO = (ProcesoAnulacionObraAdapter) userSession.get(ProcesoAnulacionObraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoAnulacionObraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoAnulacionObraAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoAnulacionObraAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Anulacion de Obra de CdM
			AnulacionObraVO anulacionObraVO = RecServiceLocator.getCdmService().cancelar(userSession, procesoAnulacionObraAdapterVO.getAnulacionObra());

			// Tiene errores recuperables
			if (anulacionObraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + anulacionObraVO.infoString()); 
				saveDemodaErrors(request, anulacionObraVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoAnulacionObraAdapter.NAME, procesoAnulacionObraAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (anulacionObraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + anulacionObraVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoAnulacionObraAdapter.NAME, procesoAnulacionObraAdapterVO);
			}

			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoAnulacionObraAdapterVO.getAnulacionObra().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_ANU_OBR, ProConstants.ACT_CANCELAR);
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
			ProcesoAnulacionObraAdapter procesoAnulacionObraAdapterVO = (ProcesoAnulacionObraAdapter) userSession.get(ProcesoAnulacionObraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoAnulacionObraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoAnulacionObraAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoAnulacionObraAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Anulacion de Obra de CdM
			AnulacionObraVO  anulacionObraVO = RecServiceLocator.getCdmService().reiniciar(userSession, procesoAnulacionObraAdapterVO.getAnulacionObra());

			// Tiene errores recuperables
			if (anulacionObraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + anulacionObraVO.infoString()); 
				saveDemodaErrors(request, anulacionObraVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoAnulacionObraAdapter.NAME, procesoAnulacionObraAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (anulacionObraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + anulacionObraVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoAnulacionObraAdapter.NAME, procesoAnulacionObraAdapterVO);
			}

			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoAnulacionObraAdapterVO.getAnulacionObra().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_CORRIDA, ProConstants.ACT_REINICIAR);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_EMISIONMAS);
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, ProcesoAnulacionObraAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ProcesoAnulacionObraAdapter.NAME);
	}
	
	public ActionForward downloadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);

			try {

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
				return baseException(mapping, request, funcName, exception, ProcesoAnulacionObraAdapter.NAME);
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
			ProcesoAnulacionObraAdapter procesoAnulacionObraAdapterVO = (ProcesoAnulacionObraAdapter) userSession.get(ProcesoAnulacionObraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoAnulacionObraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoAnulacionObraAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoAnulacionObraAdapter.NAME); 
			}
			
			CommonKey idCorrida = new CommonKey(procesoAnulacionObraAdapterVO.getAnulacionObra().getCorrida().getId());

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
