//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.seg.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.swe.SweServiceLocator;
import ar.gov.rosario.swe.base.view.struts.SweBaseDispatchAction;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.model.UsrAplSearchPage;
import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;


public final class BuscarUsrAplDAction extends SweBaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarUsrAplDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");

		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping,
				SweSecurityConstants.ABM_USUARIOS, act);
		if (userSession == null)
			return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();

		try {

			// obtiene el id de la aplicacion.
			CommonKey aplicacionCommonKey = new CommonKey(navModel
					.getSelectedId());

			UsrAplSearchPage usrAplSearchPageVO = SweServiceLocator
					.getSweService().getUsrAplSearchPageInit(userSession,
							aplicacionCommonKey);

			// Tiene errores recuperables
			if (usrAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: " + funcName + ": "
						+ usrAplSearchPageVO.infoString());
				saveDemodaErrors(request, usrAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession,
						UsrAplSearchPage.NAME, usrAplSearchPageVO);
			}

			// Tiene errores no recuperables
			if (usrAplSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": "
						+ usrAplSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName,
						UsrAplSearchPage.NAME, usrAplSearchPageVO);
			}

			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession,
					UsrAplSearchPage.NAME, usrAplSearchPageVO);

			usrAplSearchPageVO.setPageNumber(new Long(1));
			return buscar(mapping, form, request, response);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");

		SweUserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null)
			return forwardErrorSession(request);

		try {
			// seteo en el navModel el que estaba cargado en el inicializar
			userSession.setNavModel((NavModel) userSession.get(mapping
					.getPath()));
			return this.inicializar(mapping, form, request, response);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward buscar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");

		SweUserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null)
			return forwardErrorSession(request);

		try {
			// seteo la accion buscar del act para que muetre el boton agregar
			userSession.getNavModel().setAct(funcName);

			// Bajo el searchPage del userSession
			UsrAplSearchPage usrAplSearchPageVO = (UsrAplSearchPage) userSession
					.get(UsrAplSearchPage.NAME);

			// Si es nulo no se puede continuar
			if (usrAplSearchPageVO == null) {
				log.error("error en: " + funcName + ": "
						+ UsrAplSearchPage.NAME
						+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request,
						funcName, UsrAplSearchPage.NAME);
			}

			// si el buscar diparado desde la pagina de busqueda
			if (((String) userSession.get("reqAttIsSubmittedForm"))
					.equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(usrAplSearchPageVO, request);
				// Setea el PageNumber del PageModel
				usrAplSearchPageVO.setPageNumber(new Long((String) userSession
						.get("reqAttPageNumber")));
			}

			// Tiene errores recuperables
			if (usrAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: " + funcName + ": "
						+ usrAplSearchPageVO.infoString());
				saveDemodaErrors(request, usrAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession,
						UsrAplSearchPage.NAME, usrAplSearchPageVO);
			}

			// Llamada al servicio
			usrAplSearchPageVO = SweServiceLocator.getSweService()
					.getUsrAplSearchPageResult(userSession, usrAplSearchPageVO);

			// Tiene errores recuperables
			if (usrAplSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: " + funcName + ": "
						+ usrAplSearchPageVO.infoString());
				saveDemodaErrors(request, usrAplSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession,
						UsrAplSearchPage.NAME, usrAplSearchPageVO);
			}

			// Tiene errores no recuperables
			if (usrAplSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": "
						+ usrAplSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName,
						UsrAplSearchPage.NAME, usrAplSearchPageVO);
			}

			// Envio el VO al request
			request.setAttribute(UsrAplSearchPage.NAME, usrAplSearchPageVO);
			// Nuleo el list result
			// usrAplSearchPageVO.setListResult(new ArrayList()); TODO ver como
			// solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(UsrAplSearchPage.NAME, usrAplSearchPageVO);

			return mapping.findForward(SweSegConstants.FWD_USRAPL_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null)
			return forwardErrorSession(request);

		try {

			baseVer(mapping, userSession, funcName);

			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_USRAPL);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null)
			return forwardErrorSession(request);

		try {

			NavModel navModelUS = (NavModel) userSession.get(mapping.getPath()); // contiene
																					// la
																					// aplicacion
																					// sobre
																					// la
																					// que
																					// trabajo
			String selecteId = navModelUS.getSelectedId(); // id de la
															// aplicacion
			baseAgregar(mapping, userSession, funcName, selecteId);

			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_USRAPL);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward clonar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null)
			return forwardErrorSession(request);

		try {

			NavModel navModelUS = (NavModel) userSession.get(mapping.getPath()); // contiene
																					// la
																					// aplicacion
																					// sobre
																					// la
																					// que
																					// trabajo
			String selecteId = navModelUS.getSelectedId(); // id de la
															// aplicacion
			baseClonar(mapping, userSession, funcName, selecteId);

			return mapping.findForward(SweSegConstants.ACTION_CLONAR_USRAPL);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null)
			return forwardErrorSession(request);

		try {

			baseModificar(mapping, userSession, funcName);

			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_USRAPL);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null)
			return forwardErrorSession(request);

		try {

			baseEliminar(mapping, userSession, funcName);

			return mapping.findForward(SweSegConstants.ACTION_ADMINISTRAR_USRAPL);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();

		return baseSeleccionar(mapping, form, request, response, funcName,
				UsrAplSearchPage.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return baseVolver(mapping, form, request, response,
				UsrAplSearchPage.NAME);
	}

	// va a la busqueda de roles del usuario de la aplicacion seleccionado
	public ActionForward buscarUsrRolApl(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null)
			return forwardErrorSession(request);

		try {

			baseBuscar(mapping, userSession, funcName);

			return mapping.findForward(SweSegConstants.ACTION_BUSCAR_USRROLAPL);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	// modificaciones para clonar de diego
	protected void baseClonar(ActionMapping mapping, SweUserSession userSession,
			String funcName, String selecteId) {
		try {
			NavModel navModel = userSession.getNavModel();
			NavModel navModelUS = (NavModel) userSession.get(mapping.getPath());

			navModel.setPrevAction(mapping.getPath());
			navModel.setPrevActionParameter(SweConstants.ACT_BUSCAR);
			navModel.setPrevNavModel(navModelUS.getPrevNavModel());
			navModel.setSelectedId(selecteId);
			navModel.setAct(SweConstants.ACT_CLONAR);

			if (log.isDebugEnabled())
				log.debug(funcName + ": navModel: " + navModel.infoString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
