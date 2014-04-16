//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.seg.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.swe.SweServiceLocator;
import ar.gov.rosario.swe.base.view.struts.SweBaseDispatchAction;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.model.UsrAplAdapter;
import ar.gov.rosario.swe.iface.model.UsrAplVO;
import ar.gov.rosario.swe.iface.util.SegBussConstants;
import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarUsrAplDAction extends SweBaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarUsrAplDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_USUARIOS, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		UsrAplAdapter usrAplAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			// puede ser la clave de UsrApl o de la aplicacion
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(SweConstants.ACT_VER)) {
				stringServicio = "getUsrAplAdapterForView(userSession, commonKey)";
				usrAplAdapterVO = SweServiceLocator.getSweService().getUsrAplAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_USRAPL_ADAPTER_VIEW);
			}
			if (act.equals(SweConstants.ACT_MODIFICAR)) {
				stringServicio = "getUsrAplAdapterForUpdate(userSession, commonKey)";
				usrAplAdapterVO = SweServiceLocator.getSweService().getUsrAplAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_USRAPL_ADAPTER);
			}
			if (act.equals(SweConstants.ACT_ELIMINAR)) {
				stringServicio = "getUsrAplAdapterForDelete(userSession, commonKey)";
				usrAplAdapterVO = SweServiceLocator.getSweService().getUsrAplAdapterForDelete(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_USRAPL_ADAPTER_VIEW);					
			}
			if (act.equals(SweConstants.ACT_AGREGAR)) {
				stringServicio = "getUsrAplAdapterForCreate(userSession)";
				usrAplAdapterVO = SweServiceLocator.getSweService().getUsrAplAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_USRAPL_ADAPTER);
			}

			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (usrAplAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + usrAplAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsrAplAdapter.NAME, usrAplAdapterVO);
			}
			
			// Incremento el historial de navegacion del userSession
			pasarNavModelActualAHistorial(mapping, funcName, userSession);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + UsrAplAdapter.NAME + ": "+ usrAplAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(UsrAplAdapter.NAME, usrAplAdapterVO);
			// Subo el apdater al userSession
			userSession.put(UsrAplAdapter.NAME, usrAplAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_USUARIOS, SweSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			UsrAplAdapter usrAplAdapterVO = (UsrAplAdapter) userSession.get(UsrAplAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (usrAplAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + UsrAplAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsrAplAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(usrAplAdapterVO, request);
			
            // Tiene errores recuperables
			if (usrAplAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usrAplAdapterVO.infoString()); 
				saveDemodaErrors(request, usrAplAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsrAplAdapter.NAME, usrAplAdapterVO);
			}
			//Si es usuario de SWE se obtienen los ids de las aplicaciones seleccionadas 
			if(usrAplAdapterVO.getUsrApl().getAplicacion().getCodigo().equals(SegBussConstants.CODIGO_SWE)){
				Long[] listIdsAppSelected = (Long[]) PropertyUtils.getSimpleProperty(form, "listIdsAppSelected");
				usrAplAdapterVO.getUsrApl().setListIdsAppSelected(listIdsAppSelected);
			}
			
			// llamada al servicio
			UsrAplVO usrAplVO = SweServiceLocator.getSweService().createUsrApl(userSession, usrAplAdapterVO.getUsrApl());
			
			
            // Tiene errores recuperables
			if (usrAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usrAplVO.infoString()); 
				saveDemodaErrors(request, usrAplVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsrAplAdapter.NAME, usrAplAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (usrAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usrAplVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsrAplAdapter.NAME, usrAplAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, UsrAplAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
	
	public ActionForward clonar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_USUARIOS, SweSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			UsrAplAdapter usrAplAdapterVO = (UsrAplAdapter) userSession.get(UsrAplAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (usrAplAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + UsrAplAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsrAplAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(usrAplAdapterVO, request);
			
            // Tiene errores recuperables
			if (usrAplAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usrAplAdapterVO.infoString()); 
				saveDemodaErrors(request, usrAplAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsrAplAdapter.NAME, usrAplAdapterVO);
			}
			//Si es usuario de SWE se obtienen los ids de las aplicaciones seleccionadas 
			if(usrAplAdapterVO.getUsrApl().getAplicacion().getCodigo().equals(SegBussConstants.CODIGO_SWE)){
				Long[] listIdsAppSelected = (Long[]) PropertyUtils.getSimpleProperty(form, "listIdsAppSelected");
				usrAplAdapterVO.getUsrApl().setListIdsAppSelected(listIdsAppSelected);
			}
			
			// llamada al servicio 
			UsrAplVO usrAplVO = SweServiceLocator.getSweService().createUsrApl(userSession, usrAplAdapterVO.getUsrApl());
			
			
            // Tiene errores recuperables
			if (usrAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usrAplVO.infoString()); 
				saveDemodaErrors(request, usrAplVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsrAplAdapter.NAME, usrAplAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (usrAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usrAplVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsrAplAdapter.NAME, usrAplAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, UsrAplAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_USUARIOS, SweSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			UsrAplAdapter usrAplAdapterVO = (UsrAplAdapter) userSession.get(UsrAplAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (usrAplAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + UsrAplAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsrAplAdapter.NAME); 
			}
 
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(usrAplAdapterVO, request);
			
            // Tiene errores recuperables
			if (usrAplAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usrAplAdapterVO.infoString()); 
				saveDemodaErrors(request, usrAplAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsrAplAdapter.NAME, usrAplAdapterVO);
			}
			//Si es usuario de SWE se obtienen los ids de las aplicaciones seleccionadas 
			if(usrAplAdapterVO.getUsrApl().getAplicacion().getCodigo().equals(SegBussConstants.CODIGO_SWE)){
				Long[] listIdsAppSelected = (Long[]) PropertyUtils.getSimpleProperty(form, "listIdsAppSelected");
				usrAplAdapterVO.getUsrApl().setListIdsAppSelected(listIdsAppSelected);
			}

			// llamada al servicio
			UsrAplVO usrAplVO = SweServiceLocator.getSweService().updateUsrApl(userSession, usrAplAdapterVO.getUsrApl());
			
            // Tiene errores recuperables
			if (usrAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usrAplAdapterVO.infoString()); 
				saveDemodaErrors(request, usrAplVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsrAplAdapter.NAME, usrAplAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (usrAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usrAplAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsrAplAdapter.NAME, usrAplAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, UsrAplAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_USUARIOS, SweSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			UsrAplAdapter usrAplAdapterVO = (UsrAplAdapter) userSession.get(UsrAplAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (usrAplAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + UsrAplAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, UsrAplAdapter.NAME); 
			}
			
			// llamada al servicio
			UsrAplVO usrAplVO = SweServiceLocator.getSweService().deleteUsrApl(userSession, usrAplAdapterVO.getUsrApl());
			
            // Tiene errores recuperables
			if (usrAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usrAplAdapterVO.infoString()); 
				saveDemodaErrors(request, usrAplVO);
				request.setAttribute(UsrAplAdapter.NAME,usrAplAdapterVO);
				return mapping.findForward(SweSegConstants.FWD_USRAPL_ADAPTER_VIEW);
			}
			
			// Tiene errores no recuperables
			if (usrAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usrAplAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsrAplAdapter.NAME, usrAplAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, UsrAplAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return baseVolver(mapping, form, request, response, UsrAplAdapter.NAME);
	}
	
}
