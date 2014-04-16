//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.exe.iface.model.CueExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.CueExeSearchPage;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarSeleccionDeudaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarSeleccionDeudaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping);		
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();

		try {
			
			CueExeAdapter cueExeAdapterVO  = (CueExeAdapter) userSession.get(CueExeAdapter.CAMBIOESTADO_NAME);
			
			// Seteo los valores de navegacion en el adapter
			cueExeAdapterVO.setValuesFromNavModel(navModel);

			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				CueExeAdapter.CAMBIOESTADO_NAME + ": " + cueExeAdapterVO.infoString());
			
			// Envio el VO al request y al usser Session
			request.setAttribute(CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
			userSession.put(CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
			
			cueExeAdapterVO.addMessage(ExeError.MSG_POSEE_DEUDA_EMITIDA);
			
			saveDemodaMessages(request, cueExeAdapterVO);			
			
			return mapping.findForward(ExeConstants.FWD_SELECCIONDEUDA_EDIT_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.CAMBIOESTADO_NAME);
		}
	}
	
	public ActionForward cambiarEstado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeAdapter cueExeAdapterVO = (CueExeAdapter) userSession.get(CueExeAdapter.CAMBIOESTADO_NAME);

			// Si es nulo no se puede continuar
			if (cueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeAdapter.CAMBIOESTADO_NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.CAMBIOESTADO_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cueExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (cueExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
			}

			// creo la accion, y envio la solicitud si se selecciona alguna deuda
			cueExeAdapterVO = ExeServiceLocator.getExencionService().cambiarEstadoCueExe(userSession, cueExeAdapterVO);
			
			
            // Tiene errores recuperables
			if (cueExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
			}

			// Tiene errores no recuperables
			if (cueExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName,
					CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
			}

			return forwardConfirmarOk(mapping, request, funcName, CueExeAdapter.CAMBIOESTADO_NAME);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.CAMBIOESTADO_NAME);
		}
	}

	/** Vuelve al adapter de creacion de la
	 *  accion, borra las listas que se hallan cargado
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		try {

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
				
			CueExeAdapter cueExeAdapterVO  = (CueExeAdapter) userSession.get(CueExeAdapter.CAMBIOESTADO_NAME);

			// limpio las lista de deuda a seleccionar y
			// la lista de deuda seleccionada
			cueExeAdapterVO.getListDeudaASeleccionar().clear();
			cueExeAdapterVO.setListIdDeudaSeleccionada(null);

			request.setAttribute(CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapterVO);
			
			log.debug(" #### deberia volver ha: " + userSession.getNavModel().getPrevActionParameter());
			
			return mapping.findForward(ExeConstants.FWD_CAMBIOESTADO_CUEEXE_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.CAMBIOESTADO_NAME);
		}
		
	}

	public ActionForward volverASearchPageCueExe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			try {
				
				UserSession userSession = getCurrentUserSession(request, mapping); 
				if (userSession == null) return forwardErrorSession(request);
				
				// Bajo el searchPage del userSession
				CueExeSearchPage cueExeSearchPageVO = (CueExeSearchPage) userSession.get(CueExeSearchPage.NAME);
								
				String path = ExeConstants.PATH_BUSCARCUEEXE+".do?method="+BaseSecurityConstants.BUSCAR+"&pageNumber="+StringUtil.formatLong(cueExeSearchPageVO.getPageNumber());		
				return new ActionForward(path);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CueExeAdapter.CAMBIOESTADO_NAME);
			}
			
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CueExeAdapter.CAMBIOESTADO_NAME);
		
	}

}
	
