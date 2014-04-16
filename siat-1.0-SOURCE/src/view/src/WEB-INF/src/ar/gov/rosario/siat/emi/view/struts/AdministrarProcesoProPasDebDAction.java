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
import ar.gov.rosario.siat.emi.iface.model.ProPasDebVO;
import ar.gov.rosario.siat.emi.iface.model.ProcesoProPasDebAdapter;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarProcesoProPasDebDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProcesoProPasDebDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
	
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
	
		NavModel navModel = userSession.getNavModel();
		ProcesoProPasDebAdapter procesoProPasDebAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
	
		try {
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
	
			if (navModel.getAct().equals(EmiSecurityConstants.ACT_ADMINISTRAR_PROCESO)) {
				stringServicio = "getProcesoProPasDebAdapterInit(userSession, commonKey)";
				procesoProPasDebAdapterVO = EmiServiceLocator.getGeneralService()
						.getProcesoProPasDebAdapterInit(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_PROCESO_PROPASDEB_ADAPTER);
			}
	
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio);
	
			// Tiene errores no recuperables
			if (procesoProPasDebAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": servicio: " + stringServicio + ": "
						+ procesoProPasDebAdapterVO.errorString());
	
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoProPasDebAdapter.NAME,
						procesoProPasDebAdapterVO);
			}
	
			// Guardamos los warnings en el request
			saveDemodaMessages(request, procesoProPasDebAdapterVO);
			
			// Seteo los valores de navegacion en el adapter
			procesoProPasDebAdapterVO.setValuesFromNavModel(navModel);
	
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcesoProPasDebAdapter.NAME + ": "
								+ procesoProPasDebAdapterVO.infoString());
	
			// Envio el VO al request
			request.setAttribute(ProcesoProPasDebAdapter.NAME, procesoProPasDebAdapterVO);
	
			// Subo el apdater al userSession
			userSession.put(ProcesoProPasDebAdapter.NAME, procesoProPasDebAdapterVO);
			
			return actionForward;
	
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoProPasDebAdapter.NAME);
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
		ProcesoProPasDebAdapter procesoProPasDebAdapterVO = 
			(ProcesoProPasDebAdapter) userSession.get(ProcesoProPasDebAdapter.NAME);
			
		// Cargo el id de la corrida como parametro
		navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, 
					procesoProPasDebAdapterVO.getProPasDeb().getCorrida().getId());
		
		return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_PROPASDEB, 
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
		ProcesoProPasDebAdapter procesoProPasDebAdapterVO = 
			(ProcesoProPasDebAdapter) userSession.get(ProcesoProPasDebAdapter.NAME);
			
		// Cargo el id de la corrida como parametro
		navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, 
					procesoProPasDebAdapterVO.getProPasDeb().getCorrida().getId());
		
		return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_PROPASDEB, 
					ProConstants.ACT_ACTIVAR);
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
		ProcesoProPasDebAdapter procesoProPasDebAdapterVO = 
			(ProcesoProPasDebAdapter) userSession.get(ProcesoProPasDebAdapter.NAME);
			
		// Si es nulo no se puede continuar
		if (procesoProPasDebAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + ProcesoProPasDebAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoProPasDebAdapter.NAME); 
		}
		
		// Llamar a un servicio especifico para el Proceso de Prescripcion de Deuda
		ProPasDebVO proPasDebVO = EmiServiceLocator.getGeneralService()
			.reiniciar(userSession, procesoProPasDebAdapterVO.getProPasDeb());

		// Tiene errores recuperables
		if (proPasDebVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + proPasDebVO.infoString()); 
			saveDemodaErrors(request, proPasDebVO);
			return forwardErrorRecoverable(mapping, request, userSession, ProcesoProPasDebAdapter.NAME, 
					procesoProPasDebAdapterVO);
		}
		
		// Tiene errores no recuperables
		if (proPasDebVO.hasErrorNonRecoverable()) {
			log.error("error en: "  + funcName + ": " + proPasDebVO.errorString()); 
			return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoProPasDebAdapter.NAME, 
					procesoProPasDebAdapterVO);
		}
					
		// Recargamos la pagina
		return baseRefill(mapping, form, request, response, funcName, ProcesoProPasDebAdapter.NAME);
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProcesoProPasDebAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ProcesoProPasDebAdapter.NAME);
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
			ProcesoProPasDebAdapter procesoProPasDebAdapterVO = (ProcesoProPasDebAdapter) 
			userSession.get(ProcesoProPasDebAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoProPasDebAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoProPasDebAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoProPasDebAdapter.NAME); 
			}
			
			CommonKey idCorrida = new CommonKey(procesoProPasDebAdapterVO
					.getProPasDeb().getCorrida().getId());
			
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
			return baseException(mapping, request, funcName, exception, ProcesoProPasDebAdapter.NAME);
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
			return baseException(mapping, request, funcName, exception, ProcesoProPasDebAdapter.NAME);
		}
	}
}
