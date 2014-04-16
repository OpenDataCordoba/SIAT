//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.struts;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.view.util.DefinitionUtil;
import ar.gov.rosario.siat.pad.iface.model.CuentaAdapter;
import ar.gov.rosario.siat.pad.iface.model.RecAtrCueVAdapter;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarRecAtrCueVDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRecAtrCueVDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_RECATRCUEV, act);		
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();

		RecAtrCueVAdapter recAtrCueVAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CuentaAdapter cuentaAdapterVO = (CuentaAdapter) userSession.get(CuentaAdapter.NAME);

			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getRecAtrCueAdapterForUpdate(userSession, recursoCommonKey, atributoCommonKey)";
				
				CommonKey cuentaKey = new CommonKey(cuentaAdapterVO.getCuenta().getId());
				
				recAtrCueVAdapterVO = PadServiceLocator.getCuentaService().getRecAtrCueVAdapterForView(userSession, commonKey, cuentaKey);
				actionForward = mapping.findForward(PadConstants.FWD_RECATRCUEV_EDIT_ADAPTER);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (recAtrCueVAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + recAtrCueVAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecAtrCueVAdapter.NAME, recAtrCueVAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			recAtrCueVAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + RecAtrCueVAdapter.NAME + ": "+ recAtrCueVAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(RecAtrCueVAdapter.NAME, recAtrCueVAdapterVO);
			// Subo el apdater al userSession
			userSession.put(RecAtrCueVAdapter.NAME, recAtrCueVAdapterVO);
			
			saveDemodaMessages(request, recAtrCueVAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecAtrCueVAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_RECATRCUEV, BaseSecurityConstants.AGREGAR);
		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RecAtrCueVAdapter recAtrCueVAdapterVO = (RecAtrCueVAdapter) userSession.get(RecAtrCueVAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (recAtrCueVAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RecAtrCueVAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecAtrCueVAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(recAtrCueVAdapterVO, request);
			
			// Se realiza el populate de los atributos submitidos			
			DefinitionUtil.populateAtrVal4Edit(recAtrCueVAdapterVO.getRecAtrCueDefinition(), request);
							
			// Si fue submitido el valor se setea bandera para mostrarlo si no pasa la validacion
			if (!recAtrCueVAdapterVO.getRecAtrCueDefinition().getValorView().equals(""))
				recAtrCueVAdapterVO.getRecAtrCueDefinition().setIsSubmited(true);
			
			// Se validan formatos
			recAtrCueVAdapterVO.getRecAtrCueDefinition().clearError();
			
			recAtrCueVAdapterVO.getRecAtrCueDefinition().setFechaDesde(new Date());
			recAtrCueVAdapterVO.getRecAtrCueDefinition().validate("manual");				
			 
			
            // Tiene errores recuperables
			if (recAtrCueVAdapterVO.getRecAtrCueDefinition().hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recAtrCueVAdapterVO.infoString()); 
				saveDemodaErrors(request, recAtrCueVAdapterVO.getRecAtrCueDefinition());
				return forwardErrorRecoverable(mapping, request, userSession, 
					RecAtrCueVAdapter.NAME, recAtrCueVAdapterVO);
			}
			
			// llamada al servicio
			recAtrCueVAdapterVO = PadServiceLocator.getCuentaService().updateRecAtrCueV(userSession, recAtrCueVAdapterVO);
			
            // Tiene errores recuperables
			if (recAtrCueVAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recAtrCueVAdapterVO.infoString()); 
				saveDemodaErrors(request, recAtrCueVAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					RecAtrCueVAdapter.NAME, recAtrCueVAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (recAtrCueVAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recAtrCueVAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					RecAtrCueVAdapter.NAME, recAtrCueVAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RecAtrCueVAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecAtrCueVAdapter.NAME);
		}
	}


	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, RecAtrCueVAdapter.NAME);
	}

}
