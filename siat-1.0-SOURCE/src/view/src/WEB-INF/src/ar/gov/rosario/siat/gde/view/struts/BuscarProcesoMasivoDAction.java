//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoSearchPage;
import ar.gov.rosario.siat.gde.iface.model.TipProMasVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;

public final class BuscarProcesoMasivoDAction extends BaseDispatchAction {
	
	private Log log = LogFactory.getLog(BuscarProcesoMasivoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_MASIVO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		//Chequeo de acceso por instancia
		if (!userSession.getEsAdmin() && SiatParam.isWebSiat() && !SiatParam.isIntranetSiat()) {
			return forwardFuncionNoDisponible(request);
		}
		
		// Tipo de Proceso masivo pasado como parametro desde el menu
		CommonKey tipProMasKey = null;
		//Determinamos el Tipo de Proceso masivo pasado como parametro desde el Menu
		String idTipProMasFromReq = request.getParameter(TipProMasVO.ID_TIPOPROMAS);
		if(idTipProMasFromReq != null){
			tipProMasKey = new CommonKey(idTipProMasFromReq);
		}
		
		//Guardamos el id del Tipo de Proceso masivo en el User Session
		userSession.put(TipProMasVO.ID_TIPOPROMAS, idTipProMasFromReq);
		
		try {
			ProcesoMasivoSearchPage procesoMasivoSearchPage = GdeServiceLocator.getGestionDeudaJudicialService().getProcesoMasivoSearchPageInit(userSession, tipProMasKey);
			
			// Tiene errores recuperables
			if (procesoMasivoSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoSearchPage.infoString()); 
				saveDemodaErrors(request, procesoMasivoSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoMasivoSearchPage.NAME, procesoMasivoSearchPage);
			} 

			// Tiene errores no recuperables
			if (procesoMasivoSearchPage.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoMasivoSearchPage.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoMasivoSearchPage.NAME, procesoMasivoSearchPage);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ProcesoMasivoSearchPage.NAME, procesoMasivoSearchPage);
			return mapping.findForward(GdeConstants.FWD_PROCESO_MASIVO_SEARCHPAGE);

			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoMasivoSearchPage.NAME);
		}
	}
	
	public ActionForward buscar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = getCurrentUserSession(request, mapping);		
		if (userSession == null) return forwardErrorSession(request);
		
		try {
		
			// Bajo el searchPage del userSession
			ProcesoMasivoSearchPage procesoMasivoSearchPageVO = (ProcesoMasivoSearchPage) userSession.get(ProcesoMasivoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoMasivoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoMasivoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(procesoMasivoSearchPageVO, request);
				// Setea el PageNumber del PageModel
				
				String reqAttPageNumber = (String)userSession.get("reqAttPageNumber");
				procesoMasivoSearchPageVO.setPageNumber(new Long(reqAttPageNumber));
				
				/*if (!StringUtil.isNullOrEmpty(reqAttPageNumber))
					procesoMasivoSearchPageVO.setPageNumber(new Long(reqAttPageNumber));
				else
					procesoMasivoSearchPageVO.setPageNumber(new Long(procesoMasivoSearchPageVO.getRecsByPage()));*/
			}
			
            // Tiene errores recuperables
			if (procesoMasivoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoSearchPageVO.infoString()); 
				saveDemodaErrors(request, procesoMasivoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoMasivoSearchPage.NAME, procesoMasivoSearchPageVO);
			}

			// Llamada al servicio	
			procesoMasivoSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().getProcesoMasivoSearchPageResult(userSession, procesoMasivoSearchPageVO);

			// Tiene errores recuperables
			if (procesoMasivoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoSearchPageVO.infoString()); 
				saveDemodaErrors(request, procesoMasivoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoMasivoSearchPage.NAME, procesoMasivoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (procesoMasivoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoMasivoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoMasivoSearchPage.NAME, procesoMasivoSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ProcesoMasivoSearchPage.NAME, procesoMasivoSearchPageVO);
			// Nuleo el list result
			//procesoMasivoSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ProcesoMasivoSearchPage.NAME, procesoMasivoSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_PROCESO_MASIVO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoMasivoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_PROCESO_MASIVO_JUDICIAL);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_PROCESO_MASIVO_JUDICIAL);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_PROCESO_MASIVO_JUDICIAL);	
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, GdeConstants.ACTION_PROCESO_MASIVO_JUDICIAL);
	}
	
	public ActionForward admProceso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			return baseForwardSearchPage(mapping, request, funcName, 
					GdeConstants.ACTION_ADMINISTRAR_PROCESO_PROCESO_MASIVO,
					GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO );		
		}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ProcesoMasivoSearchPage.NAME);
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, ProcesoMasivoSearchPage.NAME);
	}
	
}
