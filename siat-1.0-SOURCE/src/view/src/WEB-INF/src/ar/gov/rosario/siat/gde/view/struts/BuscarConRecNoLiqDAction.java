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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.ConRecNoLiqSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarConRecNoLiqDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarConRecNoLiqDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CONSULTAR_CONRECNOLIQ, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ConRecNoLiqSearchPage conRecNoLiqSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConRecNoLiqSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (conRecNoLiqSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conRecNoLiqSearchPageVO.infoString()); 
				saveDemodaErrors(request, conRecNoLiqSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConRecNoLiqSearchPage.NAME, conRecNoLiqSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (conRecNoLiqSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + conRecNoLiqSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConRecNoLiqSearchPage.NAME, conRecNoLiqSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ConRecNoLiqSearchPage.NAME, conRecNoLiqSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_CONRECNOLIQ_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConRecNoLiqSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ConRecNoLiqSearchPage.NAME);
		
	}
	
	public ActionForward buscar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			ConRecNoLiqSearchPage conRecNoLiqSearchPageVO = (ConRecNoLiqSearchPage) userSession.get(ConRecNoLiqSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (conRecNoLiqSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ConRecNoLiqSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConRecNoLiqSearchPage.NAME); 
			}
				
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(conRecNoLiqSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				conRecNoLiqSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
				if(conRecNoLiqSearchPageVO.getPageNumber().longValue()<=0)
					conRecNoLiqSearchPageVO.setPageNumber(new Long(1));
			}
			
            // Tiene errores recuperables
			if (conRecNoLiqSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conRecNoLiqSearchPageVO.infoString()); 
				saveDemodaErrors(request, conRecNoLiqSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConRecNoLiqSearchPage.NAME, conRecNoLiqSearchPageVO);
			}
				
			// Llamada al servicio	
			conRecNoLiqSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConRecNoLiqSearchPageResult(userSession, conRecNoLiqSearchPageVO);			

			// Tiene errores recuperables
			if (conRecNoLiqSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conRecNoLiqSearchPageVO.infoString()); 
				saveDemodaErrors(request, conRecNoLiqSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConRecNoLiqSearchPage.NAME, conRecNoLiqSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (conRecNoLiqSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + conRecNoLiqSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ConRecNoLiqSearchPage.NAME, conRecNoLiqSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ConRecNoLiqSearchPage.NAME, conRecNoLiqSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(ConRecNoLiqSearchPage.NAME, conRecNoLiqSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_CONRECNOLIQ_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConRecNoLiqSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADM_CONRECNOLIQ);

	}

	public ActionForward paramTipoPago(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);			
			
			try {
				
				//bajo el searchPage del usserSession
				ConRecNoLiqSearchPage conRecNoLiqSearchPageVO =  (ConRecNoLiqSearchPage) userSession.get(ConRecNoLiqSearchPage.NAME);
								
				// Si es nulo no se puede continuar
				if (conRecNoLiqSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + ConRecNoLiqSearchPage.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ConRecNoLiqSearchPage.NAME); 
				}
							
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(conRecNoLiqSearchPageVO, request);

				// no llama a ningun servicio

				//conRecNoLiqSearchPageVO.setPageNumber(0L);
				
				// Envio el VO al request
				request.setAttribute(ConRecNoLiqSearchPage.NAME, conRecNoLiqSearchPageVO);
				// Subo el apdater al userSession
				userSession.put(ConRecNoLiqSearchPage.NAME, conRecNoLiqSearchPageVO);

				return mapping.findForward(GdeConstants.FWD_CONRECNOLIQ_SEARCHPAGE);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConRecNoLiqSearchPage.NAME);
			}
	}
	
	public ActionForward procesarConRecNoLiq(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			// Bajo el searchPage del userSession
			ConRecNoLiqSearchPage conRecNoLiqSearchPageVO = (ConRecNoLiqSearchPage) userSession.get(ConRecNoLiqSearchPage.NAME);
		

			// Si es nulo no se puede continuar
			if (conRecNoLiqSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ConRecNoLiqSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConRecNoLiqSearchPage.NAME); 
			}
			
			// limpia la lista de errores
			conRecNoLiqSearchPageVO.clearErrorMessages();
			
			// recupero datos en el form
			long pageNumber = conRecNoLiqSearchPageVO.getPageNumber();

			DemodaUtil.populateVO(conRecNoLiqSearchPageVO, request);
			
			//actualiza el pageNumbre porque al hacer el populate, se puso en 0
			conRecNoLiqSearchPageVO.setPageNumber(pageNumber);

			String strTipoPago =conRecNoLiqSearchPageVO.getTipoPago().trim();			
			
			//Valida que se haya seleccionado alguno
			if(request.getParameter("idsSelected")==null){
				if(strTipoPago.equals("1"))
					conRecNoLiqSearchPageVO.addRecoverableError(GdeError.CONRECNOLIQ_PROCESAR_NINGUNO_SELECTED, GdeError.CONVENIO_LABEL);
				
				if(strTipoPago.equals("2"))
					conRecNoLiqSearchPageVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONRECNOLIQ_RECIBO_LABEL);				
			}
			
            // Tiene errores recuperables
			if (conRecNoLiqSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conRecNoLiqSearchPageVO.infoString()); 
				// Envio el VO al request
				request.setAttribute(ConRecNoLiqSearchPage.NAME, conRecNoLiqSearchPageVO);
				// Subo el apdater al userSession
				userSession.put(ConRecNoLiqSearchPage.NAME, conRecNoLiqSearchPageVO);
				saveDemodaErrors(request, conRecNoLiqSearchPageVO);				
				return mapping.findForward(GdeConstants.FWD_CONRECNOLIQ_SEARCHPAGE);
			}

			
			// Seteo los valores en el navModel para que los agarre el otro Action			
			userSession.getNavModel().putParameter("idsSelected", conRecNoLiqSearchPageVO.getIdsSelected());
			userSession.getNavModel().putParameter("resultTipoRecibo", conRecNoLiqSearchPageVO.getTipoPago().trim().equals("2")?true:false);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConRecNoLiqSearchPage.NAME);
		}
		return baseForward(mapping, request, funcName, BaseConstants.ACT_BUSCAR, 
				GdeConstants.ACTION_ADM_CONRECNOLIQ, GdeConstants.ACTION_ADM_CONRECNOLIQ_PROCESAR);		
	}

	public ActionForward volverLiquidableConRecNoLiq(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				// Bajo el searchPage del userSession
				ConRecNoLiqSearchPage conRecNoLiqSearchPageVO = (ConRecNoLiqSearchPage) userSession.get(ConRecNoLiqSearchPage.NAME);
			
				// Si es nulo no se puede continuar
				if (conRecNoLiqSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + ConRecNoLiqSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ConRecNoLiqSearchPage.NAME); 
				}
				
				// limpia la lista de errores
				conRecNoLiqSearchPageVO.clearErrorMessages();
				
				long pageNumber = conRecNoLiqSearchPageVO.getPageNumber();

				// recupero datos en el form
				DemodaUtil.populateVO(conRecNoLiqSearchPageVO, request);
				
				//actualiza el pageNumber porque al hacer el populate, se puso en 0
				conRecNoLiqSearchPageVO.setPageNumber(pageNumber);
				
				// Seteo los valores en el navModel para que los agarre el otro Action			
				userSession.getNavModel().putParameter("selectedId", conRecNoLiqSearchPageVO.getSelectedId());
				userSession.getNavModel().putParameter("resultTipoRecibo", conRecNoLiqSearchPageVO.getTipoPago().trim().equals("2")?true:false);
				
				//limpio los idsSeleccionados
				conRecNoLiqSearchPageVO.setIdsSelected(new String[0]);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConRecNoLiqSearchPage.NAME);
			}
			return baseForward(mapping, request, funcName, BaseConstants.ACT_BUSCAR, 
					GdeConstants.ACTION_ADM_CONRECNOLIQ, GdeConstants.ACTION_ADM_CONRECNOLIQ_VOLVER_LIQUIDABLE);		
	}
	
	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				// Bajo el searchPage del userSession
				ConRecNoLiqSearchPage conRecNoLiqSearchPageVO = (ConRecNoLiqSearchPage) userSession.get(ConRecNoLiqSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (conRecNoLiqSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + ConRecNoLiqSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ConRecNoLiqSearchPage.NAME); 
				}
				
				// si el buscar diparado desde la pagina de busqueda
				if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
					// Recuperamos datos del form en el vo
					DemodaUtil.populateVO(conRecNoLiqSearchPageVO, request);
					// Setea el PageNumber del PageModel				
					conRecNoLiqSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
				}
				
	            // Tiene errores recuperables
				if (conRecNoLiqSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + conRecNoLiqSearchPageVO.infoString()); 
					saveDemodaErrors(request, conRecNoLiqSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, ConRecNoLiqSearchPage.NAME, conRecNoLiqSearchPageVO);
				}
					
				// Llamada al servicio	
				conRecNoLiqSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConRecNoLiqSearchPageParamRecurso(userSession, conRecNoLiqSearchPageVO);			

				//conRecNoLiqSearchPageVO.setPageNumber(0L);
				
				// Tiene errores recuperables
				if (conRecNoLiqSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + conRecNoLiqSearchPageVO.infoString()); 
					saveDemodaErrors(request, conRecNoLiqSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, ConRecNoLiqSearchPage.NAME, conRecNoLiqSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (conRecNoLiqSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + conRecNoLiqSearchPageVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, ConRecNoLiqSearchPage.NAME, conRecNoLiqSearchPageVO);
				}
			
				// Envio el VO al request
				request.setAttribute(ConRecNoLiqSearchPage.NAME, conRecNoLiqSearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(ConRecNoLiqSearchPage.NAME, conRecNoLiqSearchPageVO);
				
				return mapping.findForward(GdeConstants.FWD_CONRECNOLIQ_SEARCHPAGE);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConRecNoLiqSearchPage.NAME);
			}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ConRecNoLiqSearchPage.NAME);
		
	}
		
	
}
