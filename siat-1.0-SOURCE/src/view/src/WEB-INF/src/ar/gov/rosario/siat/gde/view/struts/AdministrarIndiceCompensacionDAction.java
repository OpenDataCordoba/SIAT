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
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.IndiceCompensacionAdapter;
import ar.gov.rosario.siat.gde.iface.model.IndiceCompensacionVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarIndiceCompensacionDAction extends
		BaseDispatchAction {

	private Log log = LogFactory
			.getLog(AdministrarIndiceCompensacionDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.ABM_INDICECOMPENSACION, act);
		if (userSession == null)
			return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();
		IndiceCompensacionAdapter indiceCompensacionAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {

			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getIndiceCompensacionAdapterForView(userSession, commonKey)";
				indiceCompensacionAdapterVO = GdeServiceLocator.getDefinicionService()
						.getIndiceCompensacionAdapterForView(userSession,
								commonKey);
				actionForward = mapping
						.findForward(GdeConstants.FWD_INDICECOMPENSACION_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getIndiceCompensacionAdapterForUpdate(userSession, commonKey)";
				indiceCompensacionAdapterVO = GdeServiceLocator.getDefinicionService()
						.getIndiceCompensacionAdapterForUpdate(userSession,
								commonKey);
				actionForward = mapping
						.findForward(GdeConstants.FWD_INDICECOMPENSACION_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getIndiceCompensacionAdapterForView(userSession, commonKey)";
				indiceCompensacionAdapterVO = GdeServiceLocator.getDefinicionService()
						.getIndiceCompensacionAdapterForView(userSession,
								commonKey);
				indiceCompensacionAdapterVO.addMessage(BaseError.MSG_ELIMINAR,
						GdeError.INDICECOMPENSACION_LABEL);
				actionForward = mapping
						.findForward(GdeConstants.FWD_INDICECOMPENSACION_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getIndiceCompensacionAdapterForCreate(userSession)";
				indiceCompensacionAdapterVO = GdeServiceLocator.getDefinicionService()
						.getIndiceCompensacionAdapterForCreate(userSession);
				actionForward = mapping
						.findForward(GdeConstants.FWD_INDICECOMPENSACION_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getIndiceCompensacionAdapterForView(userSession,commonKey)";
				indiceCompensacionAdapterVO = GdeServiceLocator.getDefinicionService()
						.getIndiceCompensacionAdapterForView(userSession,
								commonKey);
				indiceCompensacionAdapterVO.addMessage(BaseError.MSG_ACTIVAR,
						GdeError.INDICECOMPENSACION_LABEL);
				actionForward = mapping
						.findForward(GdeConstants.FWD_INDICECOMPENSACION_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getIndiceCompensacionAdapterForView(userSession)";
				indiceCompensacionAdapterVO = GdeServiceLocator.getDefinicionService()
						.getIndiceCompensacionAdapterForView(userSession,
								commonKey);
				indiceCompensacionAdapterVO.addMessage(BaseError.MSG_DESACTIVAR,
						GdeError.INDICECOMPENSACION_LABEL);
				actionForward = mapping
						.findForward(GdeConstants.FWD_INDICECOMPENSACION_VIEW_ADAPTER);
			}

			if (log.isDebugEnabled())
				log.debug(funcName + " salimos de servicio: " + stringServicio
						+ " para " + act);
			// Nunca Tiene errores recuperables

			// Tiene errores no recuperables
			if (indiceCompensacionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": servicio: "
						+ stringServicio + ": "
						+ indiceCompensacionAdapterVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName,
						IndiceCompensacionAdapter.NAME, indiceCompensacionAdapterVO);
			}

			// Seteo los valores de navegacion en el adapter
			indiceCompensacionAdapterVO.setValuesFromNavModel(navModel);

			if (log.isDebugEnabled())
				log.debug(funcName + ": " + IndiceCompensacionAdapter.NAME
						+ ": " + indiceCompensacionAdapterVO.infoString());

			// Envio el VO al request
			request.setAttribute(IndiceCompensacionAdapter.NAME,
					indiceCompensacionAdapterVO);
			// Subo el apdater al userSession
			userSession.put(IndiceCompensacionAdapter.NAME, indiceCompensacionAdapterVO);

			saveDemodaMessages(request, indiceCompensacionAdapterVO);

			return actionForward;

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					IndiceCompensacionAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.ABM_INDICECOMPENSACION,
				BaseSecurityConstants.ELIMINAR);
		if (userSession == null)
			return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			IndiceCompensacionAdapter indiceCompensacionAdapterVO = (IndiceCompensacionAdapter) userSession
					.get(IndiceCompensacionAdapter.NAME);

			// Si es nulo no se puede continuar
			if (indiceCompensacionAdapterVO == null) {
				log.error("error en: " + funcName + ": "
						+ IndiceCompensacionAdapter.NAME
						+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request,
						funcName, IndiceCompensacionAdapter.NAME);
			}

			// llamada al servicio
			IndiceCompensacionVO indiceCompensacionVO = GdeServiceLocator
					.getDefinicionService().deleteIndiceCompensacion(
							userSession,
							indiceCompensacionAdapterVO.getIndiceCompensacion());

			// Tiene errores recuperables
			if (indiceCompensacionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: " + funcName + ": "
						+ indiceCompensacionAdapterVO.infoString());
				saveDemodaErrors(request, indiceCompensacionVO);
				request.setAttribute(IndiceCompensacionAdapter.NAME,
						indiceCompensacionAdapterVO);
				return mapping
						.findForward(GdeConstants.FWD_INDICECOMPENSACION_VIEW_ADAPTER);
			}

			// Tiene errores no recuperables
			if (indiceCompensacionVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": "
						+ indiceCompensacionAdapterVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName,
						IndiceCompensacionAdapter.NAME, indiceCompensacionAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName,
					IndiceCompensacionAdapter.NAME);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					IndiceCompensacionAdapter.NAME);
		}
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.ABM_INDICECOMPENSACION,
				BaseSecurityConstants.AGREGAR);
		if (userSession == null)
			return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			IndiceCompensacionAdapter indiceCompensacionAdapterVO = (IndiceCompensacionAdapter) userSession
					.get(IndiceCompensacionAdapter.NAME);

			// Si es nulo no se puede continuar
			if (indiceCompensacionAdapterVO == null) {
				log.error("error en: " + funcName + ": "
						+ IndiceCompensacionAdapter.NAME
						+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request,
						funcName, IndiceCompensacionAdapter.NAME);
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(indiceCompensacionAdapterVO, request);

			// Tiene errores recuperables
			if (indiceCompensacionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: " + funcName + ": "
						+ indiceCompensacionAdapterVO.infoString());
				saveDemodaErrors(request, indiceCompensacionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession,
						IndiceCompensacionAdapter.NAME, indiceCompensacionAdapterVO);
			}

			// llamada al servicio
			IndiceCompensacionVO indiceCompensacionVO = GdeServiceLocator
					.getDefinicionService().createIndiceCompensacion(
							userSession,
							indiceCompensacionAdapterVO.getIndiceCompensacion());

			// Tiene errores recuperables
			if (indiceCompensacionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: " + funcName + ": "
						+ indiceCompensacionVO.infoString());
				saveDemodaErrors(request, indiceCompensacionVO);
				return forwardErrorRecoverable(mapping, request, userSession,
						IndiceCompensacionAdapter.NAME, indiceCompensacionAdapterVO);
			}

			// Tiene errores no recuperables
			if (indiceCompensacionVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": "
						+ indiceCompensacionVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName,
						IndiceCompensacionAdapter.NAME, indiceCompensacionAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName,
					IndiceCompensacionAdapter.NAME);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					IndiceCompensacionAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.ABM_INDICECOMPENSACION,
				BaseSecurityConstants.MODIFICAR);
		if (userSession == null)
			return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			IndiceCompensacionAdapter indiceCompensacionAdapterVO = (IndiceCompensacionAdapter) userSession
					.get(IndiceCompensacionAdapter.NAME);

			// Si es nulo no se puede continuar
			if (indiceCompensacionAdapterVO == null) {
				log.error("error en: " + funcName + ": "
						+ IndiceCompensacionAdapter.NAME
						+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request,
						funcName, IndiceCompensacionAdapter.NAME);
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(indiceCompensacionAdapterVO, request);

			// Tiene errores recuperables
			if (indiceCompensacionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: " + funcName + ": "
						+ indiceCompensacionAdapterVO.infoString());
				saveDemodaErrors(request, indiceCompensacionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession,
						IndiceCompensacionAdapter.NAME, indiceCompensacionAdapterVO);
			}

			// llamada al servicio
			IndiceCompensacionVO indiceCompensacionVO = GdeServiceLocator
					.getDefinicionService().updateIndiceCompensacion(
							userSession,
							indiceCompensacionAdapterVO.getIndiceCompensacion());

			// Tiene errores recuperables
			if (indiceCompensacionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: " + funcName + ": "
						+ indiceCompensacionAdapterVO.infoString());
				saveDemodaErrors(request, indiceCompensacionVO);
				return forwardErrorRecoverable(mapping, request, userSession,
						IndiceCompensacionAdapter.NAME, indiceCompensacionAdapterVO);
			}

			// Tiene errores no recuperables
			if (indiceCompensacionVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": "
						+ indiceCompensacionAdapterVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName,
						IndiceCompensacionAdapter.NAME, indiceCompensacionAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName,
					IndiceCompensacionAdapter.NAME);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					IndiceCompensacionAdapter.NAME);
		}
	}

	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.ABM_INDICECOMPENSACION,
				BaseSecurityConstants.ACTIVAR);
		if (userSession == null)
			return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			IndiceCompensacionAdapter indiceCompensacionAdapterVO = (IndiceCompensacionAdapter) userSession
					.get(IndiceCompensacionAdapter.NAME);

			// Si es nulo no se puede continuar
			if (indiceCompensacionAdapterVO == null) {
				log.error("error en: " + funcName + ": "
						+ IndiceCompensacionAdapter.NAME
						+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request,
						funcName, IndiceCompensacionAdapter.NAME);
			}

			// llamada al servicio
			IndiceCompensacionVO indiceCompensacionVO = GdeServiceLocator
					.getDefinicionService().activarIndiceCompensacion(
							userSession,
							indiceCompensacionAdapterVO.getIndiceCompensacion());

			// Tiene errores recuperables
			if (indiceCompensacionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: " + funcName + ": "
						+ indiceCompensacionAdapterVO.infoString());
				saveDemodaErrors(request, indiceCompensacionVO);
				request.setAttribute(IndiceCompensacionAdapter.NAME,
						indiceCompensacionAdapterVO);
				return mapping
						.findForward(GdeConstants.FWD_INDICECOMPENSACION_VIEW_ADAPTER);
			}

			// Tiene errores no recuperables
			if (indiceCompensacionVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": "
						+ indiceCompensacionAdapterVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName,
						IndiceCompensacionAdapter.NAME, indiceCompensacionAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName,
					IndiceCompensacionAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					IndiceCompensacionAdapter.NAME);
		}
	}

	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.ABM_INDICECOMPENSACION,
				BaseSecurityConstants.DESACTIVAR);
		if (userSession == null)
			return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			IndiceCompensacionAdapter indiceCompensacionAdapterVO = (IndiceCompensacionAdapter) userSession
					.get(IndiceCompensacionAdapter.NAME);

			// Si es nulo no se puede continuar
			if (indiceCompensacionAdapterVO == null) {
				log.error("error en: " + funcName + ": "
						+ IndiceCompensacionAdapter.NAME
						+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request,
						funcName, IndiceCompensacionAdapter.NAME);
			}

			// llamada al servicio
			IndiceCompensacionVO indiceCompensacionVO = GdeServiceLocator
					.getDefinicionService().desactivarIndiceCompensacion(
							userSession,
							indiceCompensacionAdapterVO.getIndiceCompensacion());

			// Tiene errores recuperables
			if (indiceCompensacionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: " + funcName + ": "
						+ indiceCompensacionAdapterVO.infoString());
				saveDemodaErrors(request, indiceCompensacionVO);
				request.setAttribute(IndiceCompensacionAdapter.NAME,
						indiceCompensacionAdapterVO);
				return mapping
						.findForward(GdeConstants.FWD_INDICECOMPENSACION_VIEW_ADAPTER);
			}

			// Tiene errores no recuperables
			if (indiceCompensacionVO.hasErrorNonRecoverable()) {
				log.error("error en: " + funcName + ": "
						+ indiceCompensacionAdapterVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName,
						IndiceCompensacionAdapter.NAME, indiceCompensacionAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName,
					IndiceCompensacionAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					IndiceCompensacionAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return baseVolver(mapping, form, request, response,
				IndiceCompensacionAdapter.NAME);

	}

}
