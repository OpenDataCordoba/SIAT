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
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuVO;
import ar.gov.rosario.siat.gde.iface.model.ProcesoPrescripcionDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarProcesoPrescripcionDeudaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProcesoPrescripcionDeudaDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();
		ProcesoPrescripcionDeudaAdapter procesoPrescripcionDeudaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;

		try {

			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(GdeConstants.ACT_ADM_PROCESO_PRESCRIPCION_DEUDA)) {
				stringServicio = "getProcesoPrescripcionDeudaAdapterInit(userSession, commonKey)";
				procesoPrescripcionDeudaAdapterVO = GdeServiceLocator.getAdmDeuConService()
						.getProcesoPrescripcionDeudaAdapterInit(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PROCESO_PRESCRIPCION_DEUDA_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio);

			// Tiene errores no recuperables
			if (procesoPrescripcionDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": servicio: " + stringServicio + ": "
						+ procesoPrescripcionDeudaAdapterVO.errorString());

				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoPrescripcionDeudaAdapter.NAME,
						procesoPrescripcionDeudaAdapterVO);
			}

			// Guardamos los warnings en el request
			saveDemodaMessages(request, procesoPrescripcionDeudaAdapterVO);
			
			// Seteo los valores de navegacion en el adapter
			procesoPrescripcionDeudaAdapterVO.setValuesFromNavModel(navModel);

			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcesoPrescripcionDeudaAdapter.NAME + ": "
								+ procesoPrescripcionDeudaAdapterVO.infoString());

			// Envio el VO al request
			request.setAttribute(ProcesoPrescripcionDeudaAdapter.NAME, procesoPrescripcionDeudaAdapterVO);

			// Subo el apdater al userSession
			userSession.put(ProcesoPrescripcionDeudaAdapter.NAME, procesoPrescripcionDeudaAdapterVO);
			
			return actionForward;

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					ProcesoPrescripcionDeudaAdapter.NAME);
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
			ProcesoPrescripcionDeudaAdapter procesoPrescripcionDeudaAdapterVO = 
				(ProcesoPrescripcionDeudaAdapter) userSession.get(ProcesoPrescripcionDeudaAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoPrescripcionDeudaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoPrescripcionDeudaAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoPrescripcionDeudaAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Prescripcion de Deuda
			ProPreDeuVO proPreDeuVO = GdeServiceLocator.getAdmDeuConService()
					.activar(userSession, procesoPrescripcionDeudaAdapterVO.getProPreDeu());

			// Tiene errores recuperables
			if (proPreDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPreDeuVO.infoString()); 
				saveDemodaErrors(request, proPreDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoPrescripcionDeudaAdapter.NAME, 
						procesoPrescripcionDeudaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (proPreDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proPreDeuVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoPrescripcionDeudaAdapter.NAME, 
						procesoPrescripcionDeudaAdapterVO);
			}
						
			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, 
						procesoPrescripcionDeudaAdapterVO.getProPreDeu().getCorrida().getId());
			
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_PRESCRIPCION, 
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
			ProcesoPrescripcionDeudaAdapter procesoPrescripcionDeudaAdapterVO = 
				(ProcesoPrescripcionDeudaAdapter) userSession.get(ProcesoPrescripcionDeudaAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoPrescripcionDeudaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoPrescripcionDeudaAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoPrescripcionDeudaAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Prescripcion de Deuda
			ProPreDeuVO proPreDeuVO = GdeServiceLocator.getAdmDeuConService()
					.cancelar(userSession, procesoPrescripcionDeudaAdapterVO.getProPreDeu());

			// Tiene errores recuperables
			if (proPreDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPreDeuVO.infoString()); 
				saveDemodaErrors(request, proPreDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoPrescripcionDeudaAdapter.NAME, 
						procesoPrescripcionDeudaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (proPreDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proPreDeuVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoPrescripcionDeudaAdapter.NAME, 
						procesoPrescripcionDeudaAdapterVO);
			}
						
			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, 
						procesoPrescripcionDeudaAdapterVO.getProPreDeu().getCorrida().getId());
			
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_PRESCRIPCION, 
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
			ProcesoPrescripcionDeudaAdapter procesoPrescripcionDeudaAdapterVO = 
				(ProcesoPrescripcionDeudaAdapter) userSession.get(ProcesoPrescripcionDeudaAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoPrescripcionDeudaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoPrescripcionDeudaAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoPrescripcionDeudaAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Prescripcion de Deuda
			ProPreDeuVO proPreDeuVO = GdeServiceLocator.getAdmDeuConService()
					.reiniciar(userSession, procesoPrescripcionDeudaAdapterVO.getProPreDeu());

			// Tiene errores recuperables
			if (proPreDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPreDeuVO.infoString()); 
				saveDemodaErrors(request, proPreDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoPrescripcionDeudaAdapter.NAME, 
						procesoPrescripcionDeudaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (proPreDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proPreDeuVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoPrescripcionDeudaAdapter.NAME, 
						procesoPrescripcionDeudaAdapterVO);
			}
						
			// Recargamos la pagina
			return baseRefill(mapping, form, request, response, funcName, ProcesoPrescripcionDeudaAdapter.NAME);
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, ProcesoPrescripcionDeudaAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ProcesoPrescripcionDeudaAdapter.NAME);
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
				ProcesoPrescripcionDeudaAdapter procesoPrescripcionDeudaAdapterVO = (ProcesoPrescripcionDeudaAdapter) 
				userSession.get(ProcesoPrescripcionDeudaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (procesoPrescripcionDeudaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ProcesoPrescripcionDeudaAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoPrescripcionDeudaAdapter.NAME); 
				}
				
				CommonKey idCorrida = new CommonKey(procesoPrescripcionDeudaAdapterVO
						.getProPreDeu().getCorrida().getId());
				
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
				return baseException(mapping, request, funcName, exception, ProcesoPrescripcionDeudaAdapter.NAME);
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
				return baseException(mapping, request, funcName, exception, ProcesoPrescripcionDeudaAdapter.NAME);
			}
	}

	public ActionForward consultarDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
		// Seteamos el id de Proceso de Prescripcion en el navModel
		ProcesoPrescripcionDeudaAdapter procesoPrescripcionDeudaAdapterVO = 
			(ProcesoPrescripcionDeudaAdapter) userSession.get(ProcesoPrescripcionDeudaAdapter.NAME);
		
		Long idProPreDeu = procesoPrescripcionDeudaAdapterVO.getProPreDeu().getId();
		
		navModel.putParameter(ProPreDeuVO.ADP_PARAM_ID, idProPreDeu);
		
		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, GdeConstants.ACTION_BUSCAR_PROPREDEUDET, BaseConstants.ACT_BUSCAR);
	}
	
}
