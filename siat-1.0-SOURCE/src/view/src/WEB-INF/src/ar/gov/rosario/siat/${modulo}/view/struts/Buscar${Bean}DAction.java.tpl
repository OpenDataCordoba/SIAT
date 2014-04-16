package ar.gov.rosario.siat.${modulo}.view.struts;

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
import ar.gov.rosario.siat.${modulo}.iface.model.${Bean}Adapter;
import ar.gov.rosario.siat.${modulo}.iface.model.${Bean}VO;
import ar.gov.rosario.siat.${modulo}.iface.service.${Modulo}ServiceLocator;
import ar.gov.rosario.siat.${modulo}.iface.util.${Modulo}Error;
import ar.gov.rosario.siat.${modulo}.iface.util.${Modulo}SecurityConstants;
import ar.gov.rosario.siat.${modulo}.view.util.${Modulo}Constants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import ar.gov.rosario.siat.base.view.util.UserSession;

public final class Buscar${Bean}DAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(Buscar${Bean}DAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, ${Modulo}SecurityConstants.ABM_${BEAN}, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			${Bean}SearchPage ${bean}SearchPageVO = ${Modulo}ServiceLocator.get${Submodulo}Service().get${Bean}SearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (${bean}SearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ${bean}SearchPageVO.infoString()); 
				saveDemodaErrors(request, ${bean}SearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ${Bean}SearchPage.NAME, ${bean}SearchPageVO);
			} 

			// Tiene errores no recuperables
			if (${bean}SearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ${bean}SearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ${Bean}SearchPage.NAME, ${bean}SearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ${Bean}SearchPage.NAME, ${bean}SearchPageVO);
			
			return mapping.findForward(${Modulo}Constants.FWD_${BEAN}_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ${Bean}SearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ${Bean}SearchPage.NAME);
		
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
			${Bean}SearchPage ${bean}SearchPageVO = (${Bean}SearchPage) userSession.get(${Bean}SearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (${bean}SearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ${Bean}SearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ${Bean}SearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(${bean}SearchPageVO, request);
				// Setea el PageNumber del PageModel				
				${bean}SearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (${bean}SearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ${bean}SearchPageVO.infoString()); 
				saveDemodaErrors(request, ${bean}SearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ${Bean}SearchPage.NAME, ${bean}SearchPageVO);
			}
				
			// Llamada al servicio	
			${bean}SearchPageVO = ${Modulo}ServiceLocator.get${Submodulo}Service().get${Bean}SearchPageResult(userSession, ${bean}SearchPageVO);			

			// Tiene errores recuperables
			if (${bean}SearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ${bean}SearchPageVO.infoString()); 
				saveDemodaErrors(request, ${bean}SearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ${Bean}SearchPage.NAME, ${bean}SearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (${bean}SearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ${bean}SearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ${Bean}SearchPage.NAME, ${bean}SearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(${Bean}SearchPage.NAME, ${bean}SearchPageVO);
			// Nuleo el list result
			//${bean}SearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(${Bean}SearchPage.NAME, ${bean}SearchPageVO);
			
			return mapping.findForward(${Modulo}Constants.FWD_${BEAN}_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ${Bean}SearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, ${Modulo}Constants.ACTION_ADMINISTRAR_${BEAN});

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		Se utiliza uno de los dos return, segun sea un encabezado detalle o no.
		return forwardAgregarSearchPage(mapping, request, funcName, ${Modulo}Constants.ACTION_ADMINISTRAR_${BEAN});
		return forwardAgregarSearchPage(mapping, request, funcName, ${Modulo}Constants.ACTION_ADMINISTRAR_ENC_${BEAN});
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, ${Modulo}Constants.ACTION_ADMINISTRAR_${BEAN});

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, ${Modulo}Constants.ACTION_ADMINISTRAR_${BEAN});

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, ${Modulo}Constants.ACTION_ADMINISTRAR_${BEAN});			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, ${Modulo}Constants.ACTION_ADMINISTRAR_${BEAN});
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ${Bean}SearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ${Bean}SearchPage.NAME);
		
	}
		
	
}
