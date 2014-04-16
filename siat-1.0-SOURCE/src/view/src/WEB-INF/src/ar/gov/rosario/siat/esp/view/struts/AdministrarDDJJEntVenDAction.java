//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.esp.iface.model.HabilitacionAdapter;
import ar.gov.rosario.siat.esp.iface.service.EspServiceLocator;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import ar.gov.rosario.siat.esp.view.util.EspConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarDDJJEntVenDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDDJJEntVenDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_HABILITACION, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			HabilitacionAdapter habilitacionAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = null;
				
				if(!StringUtil.isNullOrEmpty(navModel.getSelectedId())){
					commonKey = new CommonKey(navModel.getSelectedId());
				}
				
				if (act.equals(EspConstants.MTD_ADM_DDJJENTVEN)) {
					stringServicio = "getHabilitacionAdapterForDDJJ(userSession, commonKey)";
					 habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().getHabilitacionAdapterForDDJJ(userSession, commonKey);
					 actionForward = mapping.findForward(EspConstants.FWD_DDJJENTVEN_ADAPTER);				
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (habilitacionAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + habilitacionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, HabilitacionAdapter.NAME, habilitacionAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				habilitacionAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					HabilitacionAdapter.NAME + ": " + habilitacionAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(HabilitacionAdapter.NAME, habilitacionAdapterVO);
				// Subo el apdater al userSession
				userSession.put(HabilitacionAdapter.NAME, habilitacionAdapterVO);
				
				saveDemodaMessages(request, habilitacionAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, HabilitacionAdapter.NAME);
			}
		}
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, HabilitacionAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, HabilitacionAdapter.NAME);
	}
	


	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_HABILITACION, 
			EspSecurityConstants.MTD_SEL_HAB);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			HabilitacionAdapter habilitacionAdapterVO = (HabilitacionAdapter) userSession.get(HabilitacionAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (habilitacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + HabilitacionAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, HabilitacionAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(habilitacionAdapterVO, request);
			
            // Tiene errores recuperables
			if (habilitacionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapterVO.infoString()); 
				saveDemodaErrors(request, habilitacionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, HabilitacionAdapter.NAME, habilitacionAdapterVO);
			}
			
			// llamada al servicio
			habilitacionAdapterVO = EspServiceLocator.getHabilitacionService().seleccionarHabilitacion(userSession, habilitacionAdapterVO);
			
            // Tiene errores recuperables
			if (habilitacionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + habilitacionAdapterVO.infoString());
				saveDemodaErrors(request, habilitacionAdapterVO);				
				request.setAttribute(HabilitacionAdapter.NAME, habilitacionAdapterVO);
				return mapping.findForward(EspConstants.FWD_DDJJENTVEN_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (habilitacionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + habilitacionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, HabilitacionAdapter.NAME, habilitacionAdapterVO);
			}
					
			// Se carga el id de habilitacion en la variable selectedId
			userSession.getNavModel().setSelectedId(habilitacionAdapterVO.getHabilitacion().getId().toString());
			// Se carga un parametro que indica el origen de la llamada, para que al volver se cambie el selectedId con el id de la cuenta
			userSession.getNavModel().putParameter("idCuenta", habilitacionAdapterVO.getHabilitacion().getCuenta().getId().toString());
			// Setea parametro para identificar el origen
			userSession.getNavModel().putParameter("origen", "ddjjEntVen");
			
			return forwardAgregarAdapter(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_ENTVEN);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, HabilitacionAdapter.NAME);
		}
	}
	
}
