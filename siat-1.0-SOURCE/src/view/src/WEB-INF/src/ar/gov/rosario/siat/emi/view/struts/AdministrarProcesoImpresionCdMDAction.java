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
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.emi.iface.model.ProcesoImpresionCdMAdapter;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarProcesoImpresionCdMDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProcesoImpresionCdMDAction.class);
	
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			ProcesoImpresionCdMAdapter procesoImpresionCdMAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
			
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(EmiConstants.ACT_ADM_PROCESO_IMPRESION_CDM)) {
					stringServicio = "getProcesoImpresionCdMAdapterInit(userSession, commonKey)";
					procesoImpresionCdMAdapterVO = EmiServiceLocator.getImpresionCdMService().getProcesoImpresionCdMAdapterInit(userSession, commonKey);
					actionForward = mapping.findForward(EmiConstants.FWD_PROCESO_IMPRESION_CDM_ADAPTER);
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (procesoImpresionCdMAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + procesoImpresionCdMAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoImpresionCdMAdapter.NAME, procesoImpresionCdMAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				procesoImpresionCdMAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcesoImpresionCdMAdapter.NAME + ": "+ procesoImpresionCdMAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(ProcesoImpresionCdMAdapter.NAME, procesoImpresionCdMAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ProcesoImpresionCdMAdapter.NAME, procesoImpresionCdMAdapterVO);
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ProcesoImpresionCdMAdapter.NAME);
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
			ProcesoImpresionCdMAdapter procesoImpresionCdMAdapterVO = (ProcesoImpresionCdMAdapter) 
				userSession.get(ProcesoImpresionCdMAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoImpresionCdMAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoImpresionCdMAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoImpresionCdMAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Impresion de CdM
			EmisionVO impresionVO = EmiServiceLocator.getImpresionCdMService()
				.activar(userSession, procesoImpresionCdMAdapterVO.getEmision());

			// Tiene errores recuperables
			if (impresionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + impresionVO.infoString()); 
				saveDemodaErrors(request, impresionVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoImpresionCdMAdapter.NAME,
								procesoImpresionCdMAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (impresionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + impresionVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoImpresionCdMAdapter.NAME, 
								procesoImpresionCdMAdapterVO);
			}
						
			// Cargo el Id de la Corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, 
						procesoImpresionCdMAdapterVO.getEmision().getCorrida().getId());

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
		ProcesoImpresionCdMAdapter procesoImpresionCdMAdapterVO = (ProcesoImpresionCdMAdapter) 
			userSession.get(ProcesoImpresionCdMAdapter.NAME);
			
		// Si es nulo no se puede continuar
		if (procesoImpresionCdMAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + ProcesoImpresionCdMAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoImpresionCdMAdapter.NAME); 
		}
		
		// Llamar a un servicio especifico para el Proceso de Impresion de CdM
		EmisionVO impresionVO = EmiServiceLocator.getImpresionCdMService()
			.cancelar(userSession, procesoImpresionCdMAdapterVO.getEmision());

		// Tiene errores recuperables
		if (impresionVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + impresionVO.infoString()); 
			saveDemodaErrors(request, impresionVO);

			// Recargamos la pagina
			return baseRefill(mapping, form, request, response, funcName, ProcesoImpresionCdMAdapter.NAME);
		}
		
		// Tiene errores no recuperables
		if (impresionVO.hasErrorNonRecoverable()) {
			log.error("error en: "  + funcName + ": " + impresionVO.errorString()); 
			return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoImpresionCdMAdapter.NAME, 
							procesoImpresionCdMAdapterVO);
		}
		
		saveDemodaMessages(request, impresionVO);

		// Recargamos la pagina
		return baseRefill(mapping, form, request, response, funcName, ProcesoImpresionCdMAdapter.NAME);
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
		ProcesoImpresionCdMAdapter procesoImpresionCdMAdapterVO = (ProcesoImpresionCdMAdapter) 
			userSession.get(ProcesoImpresionCdMAdapter.NAME);
			
		// Si es nulo no se puede continuar
		if (procesoImpresionCdMAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + ProcesoImpresionCdMAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoImpresionCdMAdapter.NAME); 
		}
		
		// Llamar a un servicio especifico para el Proceso de Impresion de CdM
		EmisionVO impresionVO = EmiServiceLocator.getImpresionCdMService()
			.reiniciar(userSession, procesoImpresionCdMAdapterVO.getEmision());

		// Tiene errores recuperables
		if (impresionVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + impresionVO.infoString()); 
			saveDemodaErrors(request, impresionVO);
		
			// Recargamos la pagina
			return baseRefill(mapping, form, request, response, funcName, ProcesoImpresionCdMAdapter.NAME);
		}
		
		// Tiene errores no recuperables
		if (impresionVO.hasErrorNonRecoverable()) {
			log.error("error en: "  + funcName + ": " + impresionVO.errorString()); 
			return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoImpresionCdMAdapter.NAME, 
							procesoImpresionCdMAdapterVO);
		}

		saveDemodaMessages(request, impresionVO);
		
		// Recargamos la pagina
		return baseRefill(mapping, form, request, response, funcName, ProcesoImpresionCdMAdapter.NAME);


	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, ProcesoImpresionCdMAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ProcesoImpresionCdMAdapter.NAME);
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
				return baseException(mapping, request, funcName, exception, ProcesoImpresionCdMAdapter.NAME);
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
			ProcesoImpresionCdMAdapter procesoImpresionCdMAdapterVO = (ProcesoImpresionCdMAdapter) userSession.get(ProcesoImpresionCdMAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoImpresionCdMAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoImpresionCdMAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoImpresionCdMAdapter.NAME); 
			}
			
			CommonKey idCorrida = new CommonKey(procesoImpresionCdMAdapterVO.getEmision().getCorrida().getId());

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
