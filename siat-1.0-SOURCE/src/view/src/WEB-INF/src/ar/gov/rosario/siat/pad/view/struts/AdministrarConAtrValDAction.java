//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.struts;

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
import ar.gov.rosario.siat.pad.iface.model.ConAtrValAdapter;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteAdapter;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarConAtrValDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarConAtrValDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CONTRIBUYENTE_ATRIBUTO_VALOR, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ConAtrValAdapter conAtrValValAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			ContribuyenteAdapter objImpAdapterVO  = (ContribuyenteAdapter) userSession.get(ContribuyenteAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContribuyenteAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribuyenteAdapter.NAME); 
			}
			
			CommonKey idConAtrVal = new CommonKey(navModel.getSelectedId());			
			CommonKey idContribuyente = new CommonKey(objImpAdapterVO.getContribuyente().getId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getConAtrValAdapterForView(userSession, commonKey)";
				conAtrValValAdapterVO = PadServiceLocator.getContribuyenteService().getConAtrValAdapterForView(userSession, idContribuyente, idConAtrVal);
				actionForward = mapping.findForward(PadConstants.FWD_CONATRVAL_VIEW_ADAPTER);
			}	
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getConAtrValAdapterForUpdate(userSession, commonKey)";
				conAtrValValAdapterVO = PadServiceLocator.getContribuyenteService().getConAtrValAdapterForView(userSession, idContribuyente, idConAtrVal);
				actionForward = mapping.findForward(PadConstants.FWD_CONATRVAL_EDIT_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (conAtrValValAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + conAtrValValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConAtrValAdapter.NAME, conAtrValValAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			conAtrValValAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ConAtrValAdapter.NAME + ": "+ conAtrValValAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ConAtrValAdapter.NAME, conAtrValValAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ConAtrValAdapter.NAME, conAtrValValAdapterVO);
			 
			saveDemodaMessages(request, conAtrValValAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConAtrValAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CONTRIBUYENTE_ATRIBUTO_VALOR, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConAtrValAdapter conAtrValValAdapterVO = (ConAtrValAdapter) userSession.get(ConAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (conAtrValValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConAtrValAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConAtrValAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(conAtrValValAdapterVO, request);
			
            // Tiene errores recuperables
			if (conAtrValValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conAtrValValAdapterVO.infoString()); 
				saveDemodaErrors(request, conAtrValValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConAtrValAdapter.NAME, conAtrValValAdapterVO);
			}
			
			// llamada al servicio
			conAtrValValAdapterVO = PadServiceLocator.getContribuyenteService().updateConAtrVal(userSession, conAtrValValAdapterVO);
			
            // Tiene errores recuperables
			if (conAtrValValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conAtrValValAdapterVO.infoString()); 
				saveDemodaErrors(request, conAtrValValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConAtrValAdapter.NAME, conAtrValValAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (conAtrValValAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + conAtrValValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConAtrValAdapter.NAME, conAtrValValAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ConAtrValAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConAtrValAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CONTRIBUYENTE_ATRIBUTO_VALOR, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConAtrValAdapter conAtrValValAdapterVO = (ConAtrValAdapter) userSession.get(ConAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (conAtrValValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConAtrValAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConAtrValAdapter.NAME); 
			}

			// Se realiza el populate de los atributos submitidos			
			DefinitionUtil.populateAtrVal4Edit(conAtrValValAdapterVO.getConAtrDefinition(), request);

			DefinitionUtil.populateVigenciaAtrVal(conAtrValValAdapterVO.getConAtrDefinition(), request);

			// Si fue submitido el valor se setea bandera para mostrarlo si no pasa la validacion
			if (!conAtrValValAdapterVO.getConAtrDefinition().getValorView().equals("")) {
				conAtrValValAdapterVO.getConAtrDefinition().setIsSubmited(true);
			}

			// Se validan formatos
			conAtrValValAdapterVO.getConAtrDefinition().clearError();
			conAtrValValAdapterVO.getConAtrDefinition().validate4Contribuyente();
			
			
            // Tiene errores recuperables
			if (conAtrValValAdapterVO.getConAtrDefinition().hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conAtrValValAdapterVO.infoString()); 
				saveDemodaErrors(request, conAtrValValAdapterVO.getConAtrDefinition());
				return forwardErrorRecoverable(mapping, request, userSession, 
						ConAtrValAdapter.NAME, conAtrValValAdapterVO);
			}
			
			// llamada al servicio
			conAtrValValAdapterVO = PadServiceLocator.getContribuyenteService().updateConAtrVal(userSession, conAtrValValAdapterVO);
			
            // Tiene errores recuperables
			if (conAtrValValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conAtrValValAdapterVO.infoString()); 
				saveDemodaErrors(request, conAtrValValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConAtrValAdapter.NAME, conAtrValValAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (conAtrValValAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + conAtrValValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConAtrValAdapter.NAME, conAtrValValAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ConAtrValAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConAtrValAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ConAtrValAdapter.NAME);
		
	}
		
}
