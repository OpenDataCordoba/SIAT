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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.ef.iface.model.InicioInvAdapter;
import ar.gov.rosario.siat.ef.iface.model.InicioInvVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarInicioInvDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarInicioInvDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_INICIOINV, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		InicioInvAdapter inicioInvAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getInicioInvAdapterForUpdate(userSession, commonKey)";
				inicioInvAdapterVO = EfServiceLocator.getFiscalizacionService().getInicioInvAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_INICIOINV_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getInicioInvAdapterForCreate(userSession)";
				inicioInvAdapterVO = EfServiceLocator.getFiscalizacionService().getInicioInvAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_INICIOINV_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (inicioInvAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + inicioInvAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InicioInvAdapter.NAME, inicioInvAdapterVO);
			}
			
			// setea a que parte de la pantalla vuelve
			userSession.put("irA", "bloqueInicioInv");

			// Seteo los valores de navegacion en el adapter
			inicioInvAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + InicioInvAdapter.NAME + ": "+ inicioInvAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(InicioInvAdapter.NAME, inicioInvAdapterVO);
			// Subo el apdater al userSession
			userSession.put(InicioInvAdapter.NAME, inicioInvAdapterVO);
			 
			saveDemodaMessages(request, inicioInvAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InicioInvAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_INICIOINV, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			InicioInvAdapter inicioInvAdapterVO = (InicioInvAdapter) userSession.get(InicioInvAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (inicioInvAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + InicioInvAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, InicioInvAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(inicioInvAdapterVO, request);
			
            // Tiene errores recuperables
			if (inicioInvAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + inicioInvAdapterVO.infoString()); 
				saveDemodaErrors(request, inicioInvAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, InicioInvAdapter.NAME, inicioInvAdapterVO);
			}
			
			// llamada al servicio
			InicioInvVO inicioInvVO = EfServiceLocator.getFiscalizacionService().createInicioInv(userSession, inicioInvAdapterVO.getInicioInv());
			
            // Tiene errores recuperables
			if (inicioInvVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + inicioInvVO.infoString()); 
				saveDemodaErrors(request, inicioInvVO);
				return forwardErrorRecoverable(mapping, request, userSession, InicioInvAdapter.NAME, inicioInvAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (inicioInvVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + inicioInvVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InicioInvAdapter.NAME, inicioInvAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, InicioInvAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InicioInvAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_INICIOINV, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			InicioInvAdapter inicioInvAdapterVO = (InicioInvAdapter) userSession.get(InicioInvAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (inicioInvAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + InicioInvAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, InicioInvAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(inicioInvAdapterVO, request);
			
            // Tiene errores recuperables
			if (inicioInvAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + inicioInvAdapterVO.infoString()); 
				saveDemodaErrors(request, inicioInvAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, InicioInvAdapter.NAME, inicioInvAdapterVO);
			}
			
			// llamada al servicio
			InicioInvVO inicioInvVO = EfServiceLocator.getFiscalizacionService().updateInicioInv(userSession, inicioInvAdapterVO.getInicioInv());
			
            // Tiene errores recuperables
			if (inicioInvVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + inicioInvAdapterVO.infoString()); 
				saveDemodaErrors(request, inicioInvVO);
				return forwardErrorRecoverable(mapping, request, userSession, InicioInvAdapter.NAME, inicioInvAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (inicioInvVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + inicioInvAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InicioInvAdapter.NAME, inicioInvAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, InicioInvAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InicioInvAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, InicioInvAdapter.NAME);
		
	}
	
}
