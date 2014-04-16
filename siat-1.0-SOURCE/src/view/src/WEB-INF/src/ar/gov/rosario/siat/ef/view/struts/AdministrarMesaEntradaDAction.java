//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.ef.iface.model.MesaEntradaAdapter;
import ar.gov.rosario.siat.ef.iface.model.MesaEntradaVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarMesaEntradaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarMesaEntradaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_MESAENTRADA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		MesaEntradaAdapter mesaEntradaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getMesaEntradaAdapterForView(userSession, commonKey)";
				mesaEntradaAdapterVO = EfServiceLocator.getFiscalizacionService().getMesaEntradaAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_MESAENTRADA_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getMesaEntradaAdapterForUpdate(userSession, commonKey)";
				mesaEntradaAdapterVO = EfServiceLocator.getFiscalizacionService().getMesaEntradaAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_MESAENTRADA_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getMesaEntradaAdapterForView(userSession, commonKey)";
				mesaEntradaAdapterVO = EfServiceLocator.getFiscalizacionService().getMesaEntradaAdapterForView(userSession, commonKey);				
				mesaEntradaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.MESAENTRADA_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_MESAENTRADA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getMesaEntradaAdapterForCreate(userSession)";
				mesaEntradaAdapterVO = EfServiceLocator.getFiscalizacionService().getMesaEntradaAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_MESAENTRADA_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (mesaEntradaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + mesaEntradaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MesaEntradaAdapter.NAME, mesaEntradaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			mesaEntradaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + MesaEntradaAdapter.NAME + ": "+ mesaEntradaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(MesaEntradaAdapter.NAME, mesaEntradaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(MesaEntradaAdapter.NAME, mesaEntradaAdapterVO);
			 
			saveDemodaMessages(request, mesaEntradaAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MesaEntradaAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_MESAENTRADA, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MesaEntradaAdapter mesaEntradaAdapterVO = (MesaEntradaAdapter) userSession.get(MesaEntradaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (mesaEntradaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MesaEntradaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MesaEntradaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(mesaEntradaAdapterVO, request);
			
            // Tiene errores recuperables
			if (mesaEntradaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + mesaEntradaAdapterVO.infoString()); 
				saveDemodaErrors(request, mesaEntradaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, MesaEntradaAdapter.NAME, mesaEntradaAdapterVO);
			}
			
			// llamada al servicio
			MesaEntradaVO mesaEntradaVO = EfServiceLocator.getFiscalizacionService().createMesaEntrada(userSession, mesaEntradaAdapterVO.getMesaEntrada());
			
            // Tiene errores recuperables
			if (mesaEntradaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + mesaEntradaVO.infoString()); 
				saveDemodaErrors(request, mesaEntradaVO);
				return forwardErrorRecoverable(mapping, request, userSession, MesaEntradaAdapter.NAME, mesaEntradaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (mesaEntradaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + mesaEntradaVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MesaEntradaAdapter.NAME, mesaEntradaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, MesaEntradaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MesaEntradaAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_MESAENTRADA, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MesaEntradaAdapter mesaEntradaAdapterVO = (MesaEntradaAdapter) userSession.get(MesaEntradaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (mesaEntradaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MesaEntradaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MesaEntradaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(mesaEntradaAdapterVO, request);
			
            // Tiene errores recuperables
			if (mesaEntradaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + mesaEntradaAdapterVO.infoString()); 
				saveDemodaErrors(request, mesaEntradaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, MesaEntradaAdapter.NAME, mesaEntradaAdapterVO);
			}
			
			// llamada al servicio
			MesaEntradaVO mesaEntradaVO = EfServiceLocator.getFiscalizacionService().updateMesaEntrada(userSession, mesaEntradaAdapterVO.getMesaEntrada());
			
            // Tiene errores recuperables
			if (mesaEntradaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + mesaEntradaAdapterVO.infoString()); 
				saveDemodaErrors(request, mesaEntradaVO);
				return forwardErrorRecoverable(mapping, request, userSession, MesaEntradaAdapter.NAME, mesaEntradaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (mesaEntradaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + mesaEntradaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MesaEntradaAdapter.NAME, mesaEntradaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, MesaEntradaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MesaEntradaAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_MESAENTRADA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MesaEntradaAdapter mesaEntradaAdapterVO = (MesaEntradaAdapter) userSession.get(MesaEntradaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (mesaEntradaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MesaEntradaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MesaEntradaAdapter.NAME); 
			}

			// llamada al servicio
			MesaEntradaVO mesaEntradaVO = EfServiceLocator.getFiscalizacionService().deleteMesaEntrada
				(userSession, mesaEntradaAdapterVO.getMesaEntrada());
			
            // Tiene errores recuperables
			if (mesaEntradaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + mesaEntradaAdapterVO.infoString());
				saveDemodaErrors(request, mesaEntradaVO);				
				request.setAttribute(MesaEntradaAdapter.NAME, mesaEntradaAdapterVO);
				return mapping.findForward(EfConstants.FWD_MESAENTRADA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (mesaEntradaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + mesaEntradaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MesaEntradaAdapter.NAME, mesaEntradaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, MesaEntradaAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MesaEntradaAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, MesaEntradaAdapter.NAME);
		
	}
	
}
