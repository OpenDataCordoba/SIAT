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
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.emi.iface.model.ProcesoEmisionExternaAdapter;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarProcesoEmisionExternaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProcesoEmisionExternaDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
	
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
	
		NavModel navModel = userSession.getNavModel();
		ProcesoEmisionExternaAdapter procesoEmisionExternaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
	
		try {
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
	
			if (navModel.getAct().equals(EmiSecurityConstants.ACT_ADMINISTRAR_PROCESO)) {
				stringServicio = "getProcesoEmisionExternaAdapterInit(userSession, commonKey)";
				procesoEmisionExternaAdapterVO = EmiServiceLocator.getEmisionService()
						.getProcesoEmisionExternaAdapterInit(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_PROCESO_EMISIONEXTERNA_ADAPTER);
			}
	
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio);
	
			// Tiene errores no recuperables
			if (procesoEmisionExternaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": servicio: " + stringServicio + ": "
						+ procesoEmisionExternaAdapterVO.errorString());
	
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoEmisionExternaAdapter.NAME,
						procesoEmisionExternaAdapterVO);
			}
	
			// Guardamos los warnings en el request
			saveDemodaMessages(request, procesoEmisionExternaAdapterVO);
			
			// Seteo los valores de navegacion en el adapter
			procesoEmisionExternaAdapterVO.setValuesFromNavModel(navModel);
	
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcesoEmisionExternaAdapter.NAME + ": "
								+ procesoEmisionExternaAdapterVO.infoString());
	
			// Envio el VO al request
			request.setAttribute(ProcesoEmisionExternaAdapter.NAME, procesoEmisionExternaAdapterVO);
	
			// Subo el apdater al userSession
			userSession.put(ProcesoEmisionExternaAdapter.NAME, procesoEmisionExternaAdapterVO);
			
			return actionForward;
	
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoEmisionExternaAdapter.NAME);
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
		ProcesoEmisionExternaAdapter procesoEmisionExternaAdapterVO = 
			(ProcesoEmisionExternaAdapter) userSession.get(ProcesoEmisionExternaAdapter.NAME);
			
		// Cargo el id de la corrida como parametro
		navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, 
					procesoEmisionExternaAdapterVO.getEmision().getCorrida().getId());
		
		return baseForwardAdapter(mapping, request, funcName, 
					ProConstants.ACTION_ADMINISTRAR_CORRIDA, 
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
		ProcesoEmisionExternaAdapter procesoEmisionExternaAdapterVO = 
			(ProcesoEmisionExternaAdapter) userSession.get(ProcesoEmisionExternaAdapter.NAME);
			
		// Cargo el id de la corrida como parametro
		navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, 
					procesoEmisionExternaAdapterVO.getEmision().getCorrida().getId());
		
		return baseForwardAdapter(mapping, request, funcName, 
					ProConstants.ACTION_ADMINISTRAR_CORRIDA, 
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
		ProcesoEmisionExternaAdapter procesoEmisionExternaAdapterVO = 
			(ProcesoEmisionExternaAdapter) userSession.get(ProcesoEmisionExternaAdapter.NAME);
			
		// Si es nulo no se puede continuar
		if (procesoEmisionExternaAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + ProcesoEmisionExternaAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoEmisionExternaAdapter.NAME); 
		}
		
		// Llamar a un servicio especifico para el Proceso de Prescripcion de Deuda
		EmisionVO emisionVO = EmiServiceLocator.getEmisionService()
				.reiniciar(userSession, procesoEmisionExternaAdapterVO.getEmision());

		// Tiene errores recuperables
		if (emisionVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + emisionVO.infoString()); 
			saveDemodaErrors(request, emisionVO);
			return forwardErrorRecoverable(mapping, request, userSession, ProcesoEmisionExternaAdapter.NAME, 
					procesoEmisionExternaAdapterVO);
		}
		
		// Tiene errores no recuperables
		if (emisionVO.hasErrorNonRecoverable()) {
			log.error("error en: "  + funcName + ": " + emisionVO.errorString()); 
			return forwardErrorNonRecoverable(mapping, request, funcName, 
					ProcesoEmisionExternaAdapter.NAME, 
					procesoEmisionExternaAdapterVO);
		}
					
		// Recargamos la pagina
		return baseRefill(mapping, form, request, response, funcName, ProcesoEmisionExternaAdapter.NAME);
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProcesoEmisionExternaAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ProcesoEmisionExternaAdapter.NAME);
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
			ProcesoEmisionExternaAdapter procesoEmisionExternaAdapterVO = (ProcesoEmisionExternaAdapter) 
			userSession.get(ProcesoEmisionExternaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoEmisionExternaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoEmisionExternaAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoEmisionExternaAdapter.NAME); 
			}
			
			CommonKey idCorrida = new CommonKey(procesoEmisionExternaAdapterVO.getEmision().getCorrida().getId());
			
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
			return baseException(mapping, request, funcName, exception, ProcesoEmisionExternaAdapter.NAME);
		}
	}

	public ActionForward consultarAuxDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
		ProcesoEmisionExternaAdapter procesoEmisionExternaAdapterVO = (ProcesoEmisionExternaAdapter) 
					userSession.get(ProcesoEmisionExternaAdapter.NAME);
		
		Long idEmision = procesoEmisionExternaAdapterVO.getEmision().getId();
		navModel.putParameter(Emision.ADP_PARAM_ID, idEmision);
		
		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, EmiConstants.ACTION_BUSCAR_AUXDEUDA, BaseConstants.ACT_BUSCAR);
	}

}
