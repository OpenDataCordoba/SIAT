//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.emi.iface.model.ProcesoEmisionCdMAdapter;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarProcesoEmisionCdMDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProcesoEmisionCdMDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
			UserSession userSession = getCurrentUserSession(request, mapping);
			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			ProcesoEmisionCdMAdapter procesoEmisionCdMAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			
			try {
			
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());

				if (navModel.getAct().equals(EmiConstants.ACT_ADM_PROCESO_EMISION_CDM)) {
					stringServicio = "getProcesoEmisionCdMAdapterInit(userSession, commonKey)";
					procesoEmisionCdMAdapterVO = EmiServiceLocator.getEmisionCdMService()
						.getProcesoEmisionCdMAdapterInit(userSession, commonKey);
					actionForward = mapping.findForward(EmiConstants.FWD_PROCESO_EMISION_CDM_ADAPTER);
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				
				// Tiene errores no recuperables
				if (procesoEmisionCdMAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + 
								procesoEmisionCdMAdapterVO.errorString()); 
					
					return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoEmisionCdMAdapter.NAME, 
								procesoEmisionCdMAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				procesoEmisionCdMAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcesoEmisionCdMAdapter.NAME + ": "+ 
								procesoEmisionCdMAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(ProcesoEmisionCdMAdapter.NAME, procesoEmisionCdMAdapterVO);

				// Subo el apdater al userSession
				userSession.put(ProcesoEmisionCdMAdapter.NAME, procesoEmisionCdMAdapterVO);
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ProcesoEmisionCdMAdapter.NAME);
			}
		}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

			// Bajo el adapter del userSession
			ProcesoEmisionCdMAdapter procesoEmisionCdMAdapterVO = (ProcesoEmisionCdMAdapter) 
				userSession.get(ProcesoEmisionCdMAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoEmisionCdMAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoEmisionCdMAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoEmisionCdMAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Emision de CdM
			EmisionVO emisionVO = EmiServiceLocator.getEmisionCdMService()
				.activar(userSession, procesoEmisionCdMAdapterVO.getEmision());

			// Tiene errores recuperables
			if (emisionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionVO.infoString()); 
				saveDemodaErrors(request, emisionVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoEmisionCdMAdapter.NAME,
								procesoEmisionCdMAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoEmisionCdMAdapter.NAME, 
								procesoEmisionCdMAdapterVO);
			}
						
			// Cargo el Id de la Corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, 
						procesoEmisionCdMAdapterVO.getEmision().getCorrida().getId());

			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_EMISION, 
									ProConstants.ACT_ACTIVAR);
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
		ProcesoEmisionCdMAdapter procesoEmisionCdMAdapterVO = (ProcesoEmisionCdMAdapter) 
			userSession.get(ProcesoEmisionCdMAdapter.NAME);
			
		// Si es nulo no se puede continuar
		if (procesoEmisionCdMAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + ProcesoEmisionCdMAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoEmisionCdMAdapter.NAME); 
		}
		
		// Llamar a un servicio especifico para el Proceso de Emision de CdM
		EmisionVO emisionVO = EmiServiceLocator.getEmisionCdMService()
			.cancelar(userSession, procesoEmisionCdMAdapterVO.getEmision());

		// Tiene errores recuperables
		if (emisionVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + emisionVO.infoString()); 
			saveDemodaErrors(request, emisionVO);

			// Recargamos la pagina
			return baseRefill(mapping, form, request, response, funcName, ProcesoEmisionCdMAdapter.NAME);
		}
		
		// Tiene errores no recuperables
		if (emisionVO.hasErrorNonRecoverable()) {
			log.error("error en: "  + funcName + ": " + emisionVO.errorString()); 
			return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoEmisionCdMAdapter.NAME, 
							procesoEmisionCdMAdapterVO);
		}
		
		saveDemodaMessages(request, emisionVO);

		// Recargamos la pagina
		return baseRefill(mapping, form, request, response, funcName, ProcesoEmisionCdMAdapter.NAME);
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
		ProcesoEmisionCdMAdapter procesoEmisionCdMAdapterVO = (ProcesoEmisionCdMAdapter) 
			userSession.get(ProcesoEmisionCdMAdapter.NAME);
			
		// Si es nulo no se puede continuar
		if (procesoEmisionCdMAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + ProcesoEmisionCdMAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoEmisionCdMAdapter.NAME); 
		}
		
		// Llamar a un servicio especifico para el Proceso de Emision de CdM
		EmisionVO emisionVO = EmiServiceLocator.getEmisionCdMService()
			.reiniciar(userSession, procesoEmisionCdMAdapterVO.getEmision());

		// Tiene errores recuperables
		if (emisionVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + emisionVO.infoString()); 
			saveDemodaErrors(request, emisionVO);
		
			// Recargamos la pagina
			return baseRefill(mapping, form, request, response, funcName, ProcesoEmisionCdMAdapter.NAME);
		}
		
		// Tiene errores no recuperables
		if (emisionVO.hasErrorNonRecoverable()) {
			log.error("error en: "  + funcName + ": " + emisionVO.errorString()); 
			return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoEmisionCdMAdapter.NAME, 
							procesoEmisionCdMAdapterVO);
		}

		saveDemodaMessages(request, emisionVO);
		
		// Recargamos la pagina
		return baseRefill(mapping, form, request, response, funcName, ProcesoEmisionCdMAdapter.NAME);


	}

	public ActionForward consultarAuxDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
		ProcesoEmisionCdMAdapter procesoEmisionCdMAdapterVO = (ProcesoEmisionCdMAdapter) userSession.get(ProcesoEmisionCdMAdapter.NAME);
		
		Long idEmision = procesoEmisionCdMAdapterVO.getEmision().getId();
		
		navModel.putParameter("idEmision", idEmision);
		
		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, EmiConstants.ACTION_BUSCAR_AUXDEUDA, BaseConstants.ACT_BUSCAR);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, ProcesoEmisionCdMAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ProcesoEmisionCdMAdapter.NAME);
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
				return baseException(mapping, request, funcName, exception, ProcesoEmisionCdMAdapter.NAME);
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
			ProcesoEmisionCdMAdapter procesoEmisionCdMAdapterVO = (ProcesoEmisionCdMAdapter) userSession.get(ProcesoEmisionCdMAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoEmisionCdMAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoEmisionCdMAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoEmisionCdMAdapter.NAME); 
			}
			
			CommonKey idCorrida = new CommonKey(procesoEmisionCdMAdapterVO.getEmision().getCorrida().getId());

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
