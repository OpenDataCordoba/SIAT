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

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.buss.bean.TipoEmision;
import ar.gov.rosario.siat.def.iface.model.TipoEmisionVO;
import ar.gov.rosario.siat.emi.iface.model.EmisionSearchPage;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarEmisionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarEmisionDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//Determinamos el Tipo de Emision pasado como parametro desde el Menu
		Long idTipoEmisionFromReq = new Long(request.getParameter(TipoEmision.ID_TIPOEMISION));
  		
  		//Chequeamos permisos para una Emision especifica
		UserSession userSession = canAccess(request, mapping, 
				EmiSecurityConstants.getById(idTipoEmisionFromReq) ,act);
		if (userSession==null) return forwardErrorSession(request);

		//Chequeo de acceso por instancia
		if (!userSession.getEsAdmin() && SiatParam.isWebSiat() && !SiatParam.isIntranetSiat()) {
			return forwardFuncionNoDisponible(request);
		}

		//Guardamos el id del tipo de Emision en el User Session
		userSession.put(TipoEmision.ID_TIPOEMISION, idTipoEmisionFromReq);
		
		try {

			Long idTipoEmision = (Long) userSession.get(TipoEmision.ID_TIPOEMISION);
			EmisionSearchPage emisionSearchPageVO = EmiServiceLocator
						.getEmisionServiceBy(idTipoEmision).getEmisionSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (emisionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionSearchPageVO.infoString()); 
				saveDemodaErrors(request, emisionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionSearchPage.NAME, emisionSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (emisionSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionSearchPage.NAME, emisionSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , EmisionSearchPage.NAME, emisionSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_EMISION_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, EmisionSearchPage.NAME);
	}
	
	public ActionForward buscar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			//Determinamos el Tipo de Emision
			Long idTipoEmision = (Long) userSession.get(TipoEmision.ID_TIPOEMISION);
			// Bajo el searchPage del userSession
			EmisionSearchPage emisionSearchPageVO = (EmisionSearchPage) userSession.get(EmisionSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(emisionSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				emisionSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (emisionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionSearchPageVO.infoString()); 
				saveDemodaErrors(request, emisionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionSearchPage.NAME, emisionSearchPageVO);
			}
				
			// Llamada al servicio	
			emisionSearchPageVO = EmiServiceLocator.getEmisionServiceBy(idTipoEmision).getEmisionSearchPageResult(userSession, emisionSearchPageVO);			

			// Tiene errores recuperables
			if (emisionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionSearchPageVO.infoString()); 
				saveDemodaErrors(request, emisionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionSearchPage.NAME, emisionSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (emisionSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionSearchPage.NAME, emisionSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(EmisionSearchPage.NAME, emisionSearchPageVO);

			// Subo en el el searchPage al userSession
			userSession.put(EmisionSearchPage.NAME, emisionSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_EMISION_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionSearchPage.NAME);
		}
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_EMISION);
		}
	
	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_EMISION);

	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_EMISION);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_EMISION);
	}

	public ActionForward administrarProceso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			//Determinamos el Tipo de Emision
			Long idTipoEmision = (Long) userSession.get(TipoEmision.ID_TIPOEMISION);
			// Bajo el searchPage del userSession
			EmisionSearchPage emisionSearchPageVO = (EmisionSearchPage) userSession.get(EmisionSearchPage.NAME);

			//Determinamos el Tipo de Administrador de Procesos segun el Tipo de Emision
			if (idTipoEmision.equals(TipoEmisionVO.ID_EMISIONCDM))
				return baseForwardSearchPage(mapping, request, funcName, 
						EmiConstants.ACTION_ADMINISTRAR_PROCESO_EMISION_CDM, 
						EmiConstants.ACT_ADM_PROCESO_EMISION_CDM);

			if (idTipoEmision.equals(TipoEmisionVO.ID_IMPRESIONCDM))
				return baseForwardSearchPage(mapping, request, funcName, 
						EmiConstants.ACTION_ADMINISTRAR_PROCESO_IMPRESION_CDM, 
						EmiConstants.ACT_ADM_PROCESO_IMPRESION_CDM);
	
			if (idTipoEmision.equals(TipoEmisionVO.ID_EMISIONCORCDM))
				return baseForwardSearchPage(mapping, request, funcName, 
						EmiConstants.ACTION_ADMINISTRAR_PROCESO_EMISION_COR_CDM,
						EmiConstants.ACT_ADM_PROCESO_EMISION_COR_CDM);

			
			return forwardErrorRecoverable(mapping, request, userSession, EmisionSearchPage.NAME, emisionSearchPageVO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, EmisionSearchPage.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EmisionSearchPage.NAME);
	}
		
}
