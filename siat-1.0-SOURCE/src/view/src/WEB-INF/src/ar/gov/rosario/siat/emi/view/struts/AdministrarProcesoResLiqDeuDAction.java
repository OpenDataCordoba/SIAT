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
import ar.gov.rosario.siat.emi.iface.model.ProcesoResLiqDeuAdapter;
import ar.gov.rosario.siat.emi.iface.model.ProcesoResumenLiqDeuAdapter;
import ar.gov.rosario.siat.emi.iface.model.ResLiqDeuVO;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarProcesoResLiqDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProcesoResLiqDeuDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
	
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
	
		NavModel navModel = userSession.getNavModel();
		ProcesoResLiqDeuAdapter procesoResLiqDeuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
	
		try {
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
	
			if (navModel.getAct().equals(EmiSecurityConstants.ACT_ADMINISTRAR_PROCESO)) {
				stringServicio = "getProcesoResLiqDeuAdapterInit(userSession, commonKey)";
				procesoResLiqDeuAdapterVO = EmiServiceLocator.getGeneralService()
						.getProcesoResLiqDeuAdapterInit(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_PROCESO_RESLIQDEU_ADAPTER);
			}
	
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio);
	
			// Tiene errores no recuperables
			if (procesoResLiqDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": servicio: " + stringServicio + ": "
						+ procesoResLiqDeuAdapterVO.errorString());
	
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoResLiqDeuAdapter.NAME,
						procesoResLiqDeuAdapterVO);
			}
	
			// Guardamos los warnings en el request
			saveDemodaMessages(request, procesoResLiqDeuAdapterVO);
			
			// Seteo los valores de navegacion en el adapter
			procesoResLiqDeuAdapterVO.setValuesFromNavModel(navModel);
	
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcesoResLiqDeuAdapter.NAME + ": "
								+ procesoResLiqDeuAdapterVO.infoString());
	
			// Envio el VO al request
			request.setAttribute(ProcesoResLiqDeuAdapter.NAME, procesoResLiqDeuAdapterVO);
	
			// Subo el apdater al userSession
			userSession.put(ProcesoResLiqDeuAdapter.NAME, procesoResLiqDeuAdapterVO);
			
			return actionForward;
	
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoResLiqDeuAdapter.NAME);
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
		ProcesoResLiqDeuAdapter procesoResLiqDeuAdapterVO = 
			(ProcesoResLiqDeuAdapter) userSession.get(ProcesoResLiqDeuAdapter.NAME);
			
		// Cargo el id de la corrida como parametro
		navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, 
					procesoResLiqDeuAdapterVO.getResLiqDeu().getCorrida().getId());
		
		return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_RESLIQDEU, 
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
		ProcesoResLiqDeuAdapter procesoResLiqDeuAdapterVO = 
			(ProcesoResLiqDeuAdapter) userSession.get(ProcesoResLiqDeuAdapter.NAME);
			
		// Cargo el id de la corrida como parametro
		navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, 
					procesoResLiqDeuAdapterVO.getResLiqDeu().getCorrida().getId());
		
		return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_RESLIQDEU, 
					ProConstants.ACT_ACTIVAR);
	}
	
	public ActionForward reiniciar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
	
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
	
		// Bajo el adapter del userSession
		ProcesoResLiqDeuAdapter procesoResLiqDeuAdapterVO = 
			(ProcesoResLiqDeuAdapter) userSession.get(ProcesoResLiqDeuAdapter.NAME);
			
		// Cargo el id de la corrida como parametro
		navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, 
					procesoResLiqDeuAdapterVO.getResLiqDeu().getCorrida().getId());
		
		return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_RESLIQDEU, 
					ProConstants.ACT_REINICIAR);
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProcesoResLiqDeuAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ProcesoResLiqDeuAdapter.NAME);
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
			ProcesoResLiqDeuAdapter procesoResLiqDeuAdapterVO = (ProcesoResLiqDeuAdapter) 
			userSession.get(ProcesoResLiqDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoResLiqDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoResLiqDeuAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoResLiqDeuAdapter.NAME); 
			}
			
			CommonKey idCorrida = new CommonKey(procesoResLiqDeuAdapterVO
					.getResLiqDeu().getCorrida().getId());
			
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
			return baseException(mapping, request, funcName, exception, ProcesoResLiqDeuAdapter.NAME);
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
			
			return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoResumenLiqDeuAdapter.NAME);
		}
	}

	public ActionForward consultarEmiInfCue(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
		ProcesoResLiqDeuAdapter procesoResLiqDeuAdapterVO = (ProcesoResLiqDeuAdapter) 
					userSession.get(ProcesoResLiqDeuAdapter.NAME);
		
		Long idResLiqDeu = procesoResLiqDeuAdapterVO.getResLiqDeu().getId();
		navModel.putParameter(ResLiqDeuVO.ADP_PARAM_ID, idResLiqDeu);
		
		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, EmiConstants.ACTION_BUSCAR_EMIINFCUE, BaseConstants.ACT_BUSCAR);
	}
}
