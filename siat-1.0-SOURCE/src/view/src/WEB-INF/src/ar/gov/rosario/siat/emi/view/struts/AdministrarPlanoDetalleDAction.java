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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.emi.iface.model.EmisionTRPAdapter;
import ar.gov.rosario.siat.emi.iface.model.PlanoDetalleAdapter;
import ar.gov.rosario.siat.emi.iface.model.PlanoDetalleVO;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlanoDetalleDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlanoDetalleDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISIONPUNTUAL, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanoDetalleAdapter planoDetalleAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlanoDetalleAdapterForCreate(userSession)";
				planoDetalleAdapterVO = EmiServiceLocator.getEmisionService().getPlanoDetalleAdapterForCreate(userSession);
				actionForward = mapping.findForward(EmiConstants.FWD_PLANODETALLE_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (planoDetalleAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + planoDetalleAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanoDetalleAdapter.NAME, planoDetalleAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planoDetalleAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlanoDetalleAdapter.NAME + ": "+ planoDetalleAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanoDetalleAdapter.NAME, planoDetalleAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanoDetalleAdapter.NAME, planoDetalleAdapterVO);
			 
			saveDemodaMessages(request, planoDetalleAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanoDetalleAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISIONPUNTUAL, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanoDetalleAdapter planoDetalleAdapterVO = (PlanoDetalleAdapter) userSession.get(PlanoDetalleAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planoDetalleAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanoDetalleAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanoDetalleAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planoDetalleAdapterVO, request);
			
            // Tiene errores recuperables
			if (planoDetalleAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planoDetalleAdapterVO.infoString()); 
				saveDemodaErrors(request, planoDetalleAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanoDetalleAdapter.NAME, planoDetalleAdapterVO);
			}
			
			// llamada al servicio
			PlanoDetalleVO planoDetalleVO = EmiServiceLocator.getEmisionService().
				createPlanoDetalle(userSession, planoDetalleAdapterVO.getPlanoDetalle());
			
			// Tiene errores recuperables
			if (planoDetalleVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planoDetalleVO.infoString()); 
				saveDemodaErrors(request, planoDetalleVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlanoDetalleAdapter.NAME, planoDetalleAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (planoDetalleVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planoDetalleVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanoDetalleAdapter.NAME, planoDetalleAdapterVO);
			}
			
			EmisionTRPAdapter emisionTRPAdapterVO = (EmisionTRPAdapter) userSession.get(EmisionTRPAdapter.NAME);

			int i = emisionTRPAdapterVO.getListPlanoDetalle().size();
			planoDetalleVO.setId(new Long(i));
			emisionTRPAdapterVO.getListPlanoDetalle().add(planoDetalleVO);
			
			// Subo el apdater al userSession
			userSession.put(EmisionTRPAdapter.NAME, emisionTRPAdapterVO);

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanoDetalleAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanoDetalleAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanoDetalleAdapter.NAME);
		
	}
	
}
