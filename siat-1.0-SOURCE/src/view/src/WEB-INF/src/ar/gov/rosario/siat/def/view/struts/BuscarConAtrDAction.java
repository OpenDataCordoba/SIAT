//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.ConAtrSearchPage;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarConAtrDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarConAtrDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CONTRIBUYENTE_ATRIBUTO, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ConAtrSearchPage conAtrSearchPageVO = DefServiceLocator.getContribuyenteService().getConAtrSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (conAtrSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conAtrSearchPageVO.infoString()); 
				saveDemodaErrors(request, conAtrSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConAtrSearchPage.NAME, conAtrSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (conAtrSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + conAtrSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConAtrSearchPage.NAME, conAtrSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ConAtrSearchPage.NAME, conAtrSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_CONATR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConAtrSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ConAtrSearchPage.NAME);

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
			ConAtrSearchPage conAtrSearchPageVO = (ConAtrSearchPage) userSession.get(ConAtrSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (conAtrSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ConAtrSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConAtrSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(conAtrSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				conAtrSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (conAtrSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conAtrSearchPageVO.infoString()); 
				saveDemodaErrors(request, conAtrSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConAtrSearchPage.NAME, conAtrSearchPageVO);
			}

			// Llamada al servicio	
			conAtrSearchPageVO = DefServiceLocator.getContribuyenteService().getConAtrSearchPageResult(userSession, conAtrSearchPageVO);			

			// Tiene errores recuperables
			if (conAtrSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conAtrSearchPageVO.infoString()); 
				saveDemodaErrors(request, conAtrSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConAtrSearchPage.NAME, conAtrSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (conAtrSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + conAtrSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ConAtrSearchPage.NAME, conAtrSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ConAtrSearchPage.NAME, conAtrSearchPageVO);
			// Nuleo el list result
			//conAtrSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ConAtrSearchPage.NAME, conAtrSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_CONATR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConAtrSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CONATR);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// seteo la accion y el parametro para volver
		navModel.setPrevAction(mapping.getPath());
		navModel.setPrevActionParameter(BaseConstants.ACT_BUSCAR);

		// seteo los parametros para cuando oprima seleccionar
		navModel.setSelectAction("/def/AdministrarConAtr");
		navModel.setSelectActionParameter(BaseConstants.ACT_INICIALIZAR);
		navModel.setSelectAct(BaseConstants.ACT_AGREGAR);
		navModel.setAgregarEnSeleccion(false);

		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);

		List<AtributoVO> listAtributosExcluidos = 
			DefServiceLocator.getContribuyenteService().getListAtributoConAtr(userSession);

		// seteo la lista de atributos a exluir en la busqueda
		userSession.getNavModel().setListVOExcluidos(listAtributosExcluidos);

		return mapping.findForward(DefConstants.ACTION_BUSCAR_ATRIBUTO);
		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CONATR);	
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CONATR);
		
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CONATR);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_CONATR);
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ConAtrSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		return baseVolver(mapping, form, request, response, ConAtrSearchPage.NAME);
		
	}

}