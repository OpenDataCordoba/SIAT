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
import ar.gov.rosario.siat.emi.iface.model.ImpMasDeuVO;
import ar.gov.rosario.siat.emi.iface.model.ProcesoImpMasDeuAdapter;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarProcesoImpMasDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProcesoImpMasDeuDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();
		ProcesoImpMasDeuAdapter procesoImpMasDeuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(EmiConstants.ACT_ADM_PROCESO_IMPMASDEU)) {
				stringServicio = "getProcesoImpMasDeuAdapterInit(userSession, commonKey)";
				procesoImpMasDeuAdapterVO = EmiServiceLocator.getImpresionService()
					.getProcesoImpMasDeuAdapterInit(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_PROCESO_IMPMASDEU_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio);

			// Tiene errores no recuperables
			if (procesoImpMasDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": servicio: " + stringServicio + ": "
						+ procesoImpMasDeuAdapterVO.errorString());

				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoImpMasDeuAdapter.NAME,
						procesoImpMasDeuAdapterVO);
			}

			// Guardamos los warnings en el request
			saveDemodaMessages(request, procesoImpMasDeuAdapterVO);
			
			// Seteo los valores de navegacion en el adapter
			procesoImpMasDeuAdapterVO.setValuesFromNavModel(navModel);

			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcesoImpMasDeuAdapter.NAME + ": " 
				+ procesoImpMasDeuAdapterVO.infoString());

			// Envio el VO al request
			request.setAttribute(ProcesoImpMasDeuAdapter.NAME, procesoImpMasDeuAdapterVO);

			// Subo el apdater al userSession
			userSession.put(ProcesoImpMasDeuAdapter.NAME, procesoImpMasDeuAdapterVO);
			
			return actionForward;

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoImpMasDeuAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
	
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el adapter del userSession
		ProcesoImpMasDeuAdapter procesoImpMasDeuAdapterVO = 
			(ProcesoImpMasDeuAdapter) userSession.get(ProcesoImpMasDeuAdapter.NAME);
			
		// Si es nulo no se puede continuar
		if (procesoImpMasDeuAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + ProcesoImpMasDeuAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoImpMasDeuAdapter.NAME); 
		}
		
		// Llamar a un servicio especifico para el Proceso de Prescripcion de Deuda
		ImpMasDeuVO impMasDeuVO = EmiServiceLocator.getImpresionService()
				.activar(userSession, procesoImpMasDeuAdapterVO.getImpMasDeu());

		// Tiene errores recuperables
		if (impMasDeuVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + impMasDeuVO.infoString()); 
			saveDemodaErrors(request, impMasDeuVO);
			return forwardErrorRecoverable(mapping, request, userSession, ProcesoImpMasDeuAdapter.NAME, 
					procesoImpMasDeuAdapterVO);
		}
		
		// Tiene errores no recuperables
		if (impMasDeuVO.hasErrorNonRecoverable()) {
			log.error("error en: "  + funcName + ": " + impMasDeuVO.errorString()); 
			return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoImpMasDeuAdapter.NAME, 
					procesoImpMasDeuAdapterVO);
		}
					
		// Cargo el id de la corrida como parametro
		Long idCorrida = procesoImpMasDeuAdapterVO.getImpMasDeu().getCorrida().getId();
		navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, idCorrida);
		
		return baseForwardAdapter(mapping, request, funcName, 
				ProConstants.ACTION_ADMINISTRAR_ADP_IMPMASDEU,
				ProConstants.ACT_ACTIVAR);
	}
	
	public ActionForward cancelar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
	
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el adapter del userSession
		ProcesoImpMasDeuAdapter procesoImpMasDeuAdapterVO = 
			(ProcesoImpMasDeuAdapter) userSession.get(ProcesoImpMasDeuAdapter.NAME);
			
		// Si es nulo no se puede continuar
		if (procesoImpMasDeuAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + ProcesoImpMasDeuAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoImpMasDeuAdapter.NAME); 
		}
		
		// Llamar a un servicio especifico para el Proceso de Prescripcion de Deuda
		ImpMasDeuVO impMasDeuVO = EmiServiceLocator.getImpresionService()
				.cancelar(userSession, procesoImpMasDeuAdapterVO.getImpMasDeu());

		// Tiene errores recuperables
		if (impMasDeuVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + impMasDeuVO.infoString()); 
			saveDemodaErrors(request, impMasDeuVO);
			return forwardErrorRecoverable(mapping, request, userSession, ProcesoImpMasDeuAdapter.NAME, 
					procesoImpMasDeuAdapterVO);
		}
		
		// Tiene errores no recuperables
		if (impMasDeuVO.hasErrorNonRecoverable()) {
			log.error("error en: "  + funcName + ": " + impMasDeuVO.errorString()); 
			return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoImpMasDeuAdapter.NAME, 
					procesoImpMasDeuAdapterVO);
		}
					
		// Cargo el id de la corrida como parametro
		navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, 
					procesoImpMasDeuAdapterVO.getImpMasDeu().getCorrida().getId());
		
		return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_IMPMASDEU, 
					ProConstants.ACT_ACTIVAR);
	}

	public ActionForward reiniciarPreview(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
	
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
		try {
			// Bajo el adapter del userSession
			ProcesoImpMasDeuAdapter procesoImpMasDeuAdapterVO = 
				(ProcesoImpMasDeuAdapter) userSession.get(ProcesoImpMasDeuAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoImpMasDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoImpMasDeuAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoImpMasDeuAdapter.NAME); 
			}
			
			if (log.isDebugEnabled()) log.debug("exit: " + funcName);
			
			return mapping.findForward(EmiConstants.FWD_PROCESO_IMPMASDEU_REINICIAR_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoImpMasDeuAdapter.NAME);
		}
	}
	
	public ActionForward reiniciar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
	
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el adapter del userSession
		ProcesoImpMasDeuAdapter procesoImpMasDeuAdapterVO = 
			(ProcesoImpMasDeuAdapter) userSession.get(ProcesoImpMasDeuAdapter.NAME);
			
		// Si es nulo no se puede continuar
		if (procesoImpMasDeuAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + ProcesoImpMasDeuAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoImpMasDeuAdapter.NAME); 
		}
		
		// Llamar a un servicio especifico para el Proceso de Prescripcion de Deuda
		ImpMasDeuVO impMasDeuVO = EmiServiceLocator.getImpresionService()
			.reiniciar(userSession, procesoImpMasDeuAdapterVO.getImpMasDeu());

		// Tiene errores recuperables
		if (impMasDeuVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + impMasDeuVO.infoString()); 
			saveDemodaErrors(request, impMasDeuVO);
			return forwardErrorRecoverable(mapping, request, userSession, ProcesoImpMasDeuAdapter.NAME, 
					procesoImpMasDeuAdapterVO);
		}
		
		// Tiene errores no recuperables
		if (impMasDeuVO.hasErrorNonRecoverable()) {
			log.error("error en: "  + funcName + ": " + impMasDeuVO.errorString()); 
			return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoImpMasDeuAdapter.NAME, 
					procesoImpMasDeuAdapterVO);
		}
					
		// Recargamos la pagina
		return baseRefill(mapping, form, request, response, funcName, ProcesoImpMasDeuAdapter.NAME);
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProcesoImpMasDeuAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ProcesoImpMasDeuAdapter.NAME);
	}


	public ActionForward downloadLogFile(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		try {
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoImpMasDeuAdapter procesoImpMasDeuAdapterVO = (ProcesoImpMasDeuAdapter) 
			userSession.get(ProcesoImpMasDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoImpMasDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoImpMasDeuAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoImpMasDeuAdapter.NAME); 
			}
			
			CommonKey idCorrida = new CommonKey(procesoImpMasDeuAdapterVO
					.getImpMasDeu().getCorrida().getId());
			
			// llamada al servicio
			String fileName = ProServiceLocator.getAdpProcesoService().getLogFile(userSession, idCorrida);
			
			if (fileName == null) {			
				log.error("error en: "  + funcName + ": No se pudo formar logFileName para la corrida con id = " 
						+ idCorrida.getId()+".");
				return forwardErrorSession(request); 
			}
			
			baseResponseFile(response,fileName);

			log.debug("exit: " + funcName);

			return new ActionForward();
		} 
		catch (Exception exception) {
			log.error("Error al abrir archivo: ", exception);
			return baseException(mapping, request, funcName, exception, ProcesoImpMasDeuAdapter.NAME);
		}
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
			
			return new ActionForward();
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoImpMasDeuAdapter.NAME);
		}
	}

}
