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

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.MultaSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarMultaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarMultaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.ABM_MULTA, act);
		if (userSession == null)
			return forwardErrorSession(request);

		try {

			MultaSearchPage multaSearchPageVO = GdeServiceLocator
					.getGdeGDeudaAutoService().getMultaSearchPageInit(
							userSession);

			// Tiene errores recuperables
			if (multaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: " + funcName + ": "
						+ multaSearchPageVO.infoString());
				saveDemodaErrors(request, multaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession,
						MultaSearchPage.NAME, multaSearchPageVO);
			}

			// Tiene errores no recuperables
			if (multaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": "
						+ multaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName,
						MultaSearchPage.NAME, multaSearchPageVO);
			}

			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession,
					MultaSearchPage.NAME, multaSearchPageVO);

			return mapping.findForward(GdeConstants.FWD_MULTA_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					MultaSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName,
				MultaSearchPage.NAME);

	}

	public ActionForward buscar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null)
			return forwardErrorSession(request);

		try {

			// Bajo el searchPage del userSession
			MultaSearchPage multaSearchPageVO = (MultaSearchPage) userSession
					.get(MultaSearchPage.NAME);

			// Si es nulo no se puede continuar
			if (multaSearchPageVO == null) {
				log.error("error en: " + funcName + ": " + MultaSearchPage.NAME
						+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request,
						funcName, MultaSearchPage.NAME);
			}

			// si el buscar diparado desde la pagina de busqueda
			if (((String) userSession.get("reqAttIsSubmittedForm"))
					.equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(multaSearchPageVO, request);
				// Setea el PageNumber del PageModel
				multaSearchPageVO.setPageNumber(new Long((String) userSession
						.get("reqAttPageNumber")));
			}

			// Tiene errores recuperables
			if (multaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: " + funcName + ": "
						+ multaSearchPageVO.infoString());
				saveDemodaErrors(request, multaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession,
						MultaSearchPage.NAME, multaSearchPageVO);
			}

			// Llamada al servicio
			multaSearchPageVO = GdeServiceLocator.getGdeGDeudaAutoService()
					.getMultaSearchPageResult(userSession, multaSearchPageVO);

			// Tiene errores recuperables
			if (multaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: " + funcName + ": "
						+ multaSearchPageVO.infoString());
				saveDemodaErrors(request, multaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession,
						MultaSearchPage.NAME, multaSearchPageVO);
			}

			// Tiene errores no recuperables
			if (multaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": "
						+ multaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName,
						MultaSearchPage.NAME, multaSearchPageVO);
			}

			// Envio el VO al request
			request.setAttribute(MultaSearchPage.NAME, multaSearchPageVO);
			// Nuleo el list result
			// multaSearchPageVO.setListResult(new ArrayList()); TODO ver como
			// solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(MultaSearchPage.NAME, multaSearchPageVO);

			return mapping.findForward(GdeConstants.FWD_MULTA_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					MultaSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName,
				GdeConstants.ACTION_ADMINISTRAR_MULTA);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();

		/*
		 * Se utiliza uno de los dos return, segun sea un encabezado detalle o
		 * no.
		 */
		return forwardAgregarSearchPage(mapping, request, funcName,
				GdeConstants.ACTION_ADMINISTRAR_MULTA);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
	
		return forwardModificarSearchPage(mapping, request, funcName,
				GdeConstants.ACTION_ADMINISTRAR_MULTA);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName,
				GdeConstants.ACTION_ADMINISTRAR_MULTA);

	}

	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName,
				GdeConstants.ACTION_ADMINISTRAR_MULTA);
	}

	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName,
				GdeConstants.ACTION_ADMINISTRAR_MULTA);
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName,
				MultaSearchPage.NAME);

	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return baseVolver(mapping, form, request, response,
				MultaSearchPage.NAME);

	}

}
