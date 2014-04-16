//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.ArchivoAdapter;
import ar.gov.rosario.siat.bal.iface.model.ArchivoVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarArchivoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarArchivoDAction.class);


	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ARCHIVO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ArchivoAdapter archivoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getArchivoAdapterForView(userSession, commonKey)";
				archivoAdapterVO = BalServiceLocator.getArchivosBancoService().getArchivoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_ARCHIVO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ANULAR)) {
				stringServicio = "getArchivoAdapterForView(userSession, commonKey)";
				archivoAdapterVO = BalServiceLocator.getArchivosBancoService().getArchivoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_ARCHIVO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ACEPTAR)) {
				stringServicio = "getArchivoAdapterForView(userSession, commonKey)";
				archivoAdapterVO = BalServiceLocator.getArchivosBancoService().getArchivoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_ARCHIVO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getArchivoAdapterForView(userSession, commonKey)";
				archivoAdapterVO = BalServiceLocator.getArchivosBancoService().getArchivoAdapterForView(userSession, commonKey);				
				archivoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.ARCHIVO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_ARCHIVO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (archivoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + archivoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ArchivoAdapter.NAME, archivoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			archivoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ArchivoAdapter.NAME + ": "+ archivoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ArchivoAdapter.NAME, archivoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ArchivoAdapter.NAME, archivoAdapterVO);
			 
			saveDemodaMessages(request, archivoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ArchivoAdapter.NAME);
		}
	}
	
	public ActionForward anular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ARCHIVO, BaseSecurityConstants.ANULAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ArchivoAdapter archivoAdapterVO = (ArchivoAdapter) userSession.get(ArchivoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (archivoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ArchivoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ArchivoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(archivoAdapterVO, request);
			
            // Tiene errores recuperables
			if (archivoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + archivoAdapterVO.infoString()); 
				saveDemodaErrors(request, archivoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ArchivoAdapter.NAME, archivoAdapterVO);
			}
			
			// llamada al servicio
			ArchivoVO archivoVO = BalServiceLocator.getArchivosBancoService().anularArchivo(userSession, archivoAdapterVO.getArchivo());
			
            // Tiene errores recuperables
			if (archivoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + archivoAdapterVO.infoString());
				saveDemodaErrors(request, archivoVO);				
				request.setAttribute(ArchivoAdapter.NAME, archivoAdapterVO);
				return mapping.findForward(BalConstants.FWD_ARCHIVO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (archivoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + archivoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ArchivoAdapter.NAME, archivoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ArchivoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ArchivoAdapter.NAME);
		}
	}

	public ActionForward aceptar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ARCHIVO, BaseSecurityConstants.ACEPTAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ArchivoAdapter archivoAdapterVO = (ArchivoAdapter) userSession.get(ArchivoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (archivoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ArchivoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ArchivoAdapter.NAME); 
			}

			// llamada al servicio
			ArchivoVO archivoVO = BalServiceLocator.getArchivosBancoService().aceptarArchivo(userSession, archivoAdapterVO.getArchivo());
			
            // Tiene errores recuperables
			if (archivoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + archivoAdapterVO.infoString());
				saveDemodaErrors(request, archivoVO);				
				request.setAttribute(ArchivoAdapter.NAME, archivoAdapterVO);
				return mapping.findForward(BalConstants.FWD_ARCHIVO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (archivoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + archivoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ArchivoAdapter.NAME, archivoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ArchivoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ArchivoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ARCHIVO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ArchivoAdapter archivoAdapterVO = (ArchivoAdapter) userSession.get(ArchivoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (archivoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ArchivoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ArchivoAdapter.NAME); 
			}

			// llamada al servicio
			ArchivoVO archivoVO = BalServiceLocator.getArchivosBancoService().deleteArchivo(userSession, archivoAdapterVO.getArchivo());
			
            // Tiene errores recuperables
			if (archivoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + archivoAdapterVO.infoString());
				saveDemodaErrors(request, archivoVO);				
				request.setAttribute(ArchivoAdapter.NAME, archivoAdapterVO);
				return mapping.findForward(BalConstants.FWD_ARCHIVO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (archivoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + archivoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ArchivoAdapter.NAME, archivoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ArchivoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ArchivoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, ArchivoAdapter.NAME);
			
	}

}
