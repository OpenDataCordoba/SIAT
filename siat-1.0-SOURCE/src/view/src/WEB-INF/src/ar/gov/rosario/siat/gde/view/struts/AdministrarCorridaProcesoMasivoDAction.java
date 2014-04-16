//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;
/**
 * Action que se utiliza para administrar las corridas online del envio judicial 
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.CorridaProcesoMasivoAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarCorridaProcesoMasivoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCorridaProcesoMasivoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping);		
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();
		CorridaProcesoMasivoAdapter corridaProcesoMasivoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {

			CommonKey procesoMasivoKey = new CommonKey(userSession.getNavModel().getSelectedId());

			if (navModel.getAct().equals(GdeConstants.ACT_RETROCEDER)) {
				stringServicio = "getCorridaProcesoMasivoAdapterForView(userSession, commonKey)";
				corridaProcesoMasivoAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getCorridaProcesoMasivoAdapterForView
				(userSession, procesoMasivoKey); 

				actionForward = mapping.findForward(GdeConstants.FWD_CORRIDA_PRO_MAS_VIEW_ADAPTER);					
			}

			if (navModel.getAct().equals(GdeConstants.ACT_REINICIAR)) {
				stringServicio = "getCorridaProcesoMasivoAdapterForView(userSession, commonKey)";
				corridaProcesoMasivoAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getCorridaProcesoMasivoAdapterForView
				(userSession, procesoMasivoKey); 

				actionForward = mapping.findForward(GdeConstants.FWD_CORRIDA_PRO_MAS_VIEW_ADAPTER);					
			}


			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables

			// Tiene errores no recuperables
			if (corridaProcesoMasivoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + corridaProcesoMasivoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CorridaProcesoMasivoAdapter.NAME, corridaProcesoMasivoAdapterVO);
			}

			// Seteo los valores de navegacion en el adapter
			corridaProcesoMasivoAdapterVO.setValuesFromNavModel(navModel);

			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					CorridaProcesoMasivoAdapter.NAME + ": " + corridaProcesoMasivoAdapterVO.infoString());

			// Envio el VO al request
			request.setAttribute(CorridaProcesoMasivoAdapter.NAME, corridaProcesoMasivoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CorridaProcesoMasivoAdapter.NAME, corridaProcesoMasivoAdapterVO);

			saveDemodaMessages(request, corridaProcesoMasivoAdapterVO);			

			return actionForward;

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CorridaProcesoMasivoAdapter.NAME);
		}
	}

	public ActionForward retroceder(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			CorridaProcesoMasivoAdapter corridaProcesoMasivoAdapterVO = (CorridaProcesoMasivoAdapter) userSession.get(CorridaProcesoMasivoAdapter.NAME);

			// Si es nulo no se puede continuar
			if (corridaProcesoMasivoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CorridaProcesoMasivoAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CorridaProcesoMasivoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(corridaProcesoMasivoAdapterVO, request);

			// Tiene errores recuperables
			if (corridaProcesoMasivoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + corridaProcesoMasivoAdapterVO.infoString()); 
				saveDemodaErrors(request, corridaProcesoMasivoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CorridaProcesoMasivoAdapter.NAME, corridaProcesoMasivoAdapterVO);
			}

			// llamada al servicio
			ProcesoMasivoVO procesoMasivoVO = GdeServiceLocator.getGestionDeudaJudicialService().retrocederPaso(userSession, corridaProcesoMasivoAdapterVO.getProcesoMasivo());

			// Tiene errores recuperables
			if (procesoMasivoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + corridaProcesoMasivoAdapterVO.infoString());
				saveDemodaErrors(request, procesoMasivoVO);				
				request.setAttribute(CorridaProcesoMasivoAdapter.NAME, corridaProcesoMasivoAdapterVO);
				return mapping.findForward(GdeConstants.FWD_CORRIDA_PRO_MAS_VIEW_ADAPTER);
			}

			// Tiene errores no recuperables
			if (procesoMasivoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + corridaProcesoMasivoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CorridaProcesoMasivoAdapter.NAME, corridaProcesoMasivoAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CorridaProcesoMasivoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CorridaProcesoMasivoAdapter.NAME);
		}	
	}

	public ActionForward reiniciar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			CorridaProcesoMasivoAdapter corridaProcesoMasivoAdapterVO = (CorridaProcesoMasivoAdapter) userSession.get(CorridaProcesoMasivoAdapter.NAME);

			// Si es nulo no se puede continuar
			if (corridaProcesoMasivoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CorridaProcesoMasivoAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CorridaProcesoMasivoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(corridaProcesoMasivoAdapterVO, request);

			// Tiene errores recuperables
			if (corridaProcesoMasivoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + corridaProcesoMasivoAdapterVO.infoString()); 
				saveDemodaErrors(request, corridaProcesoMasivoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CorridaProcesoMasivoAdapter.NAME, corridaProcesoMasivoAdapterVO);
			}

			// llamada al servicio
			ProcesoMasivoVO procesoMasivoVO = GdeServiceLocator.getGestionDeudaJudicialService().reiniciarPaso(userSession, corridaProcesoMasivoAdapterVO.getProcesoMasivo());

			// Tiene errores recuperables
			if (procesoMasivoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + corridaProcesoMasivoAdapterVO.infoString());
				saveDemodaErrors(request, procesoMasivoVO);				
				request.setAttribute(CorridaProcesoMasivoAdapter.NAME, corridaProcesoMasivoAdapterVO);
				return mapping.findForward(GdeConstants.FWD_CORRIDA_PRO_MAS_VIEW_ADAPTER);
			}

			// Tiene errores no recuperables
			if (procesoMasivoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + corridaProcesoMasivoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CorridaProcesoMasivoAdapter.NAME, corridaProcesoMasivoAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CorridaProcesoMasivoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CorridaProcesoMasivoAdapter.NAME);
		}	
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		return baseVolver(mapping, form, request, response, CorridaProcesoMasivoAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CorridaProcesoMasivoAdapter.NAME);		
	}

}
