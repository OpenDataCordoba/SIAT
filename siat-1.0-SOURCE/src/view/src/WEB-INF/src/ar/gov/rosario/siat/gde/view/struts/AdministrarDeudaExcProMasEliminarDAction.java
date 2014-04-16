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
import ar.gov.rosario.siat.gde.iface.model.DeudaExcProMasEliminarSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;

//Administra la eliminacion de la seleccion almacenada de la deuda a excluir del proceso de envio a judicial

//eliminacion de la seleccion individual
//ver si podemos incluir la eliminacion de toda

public final class AdministrarDeudaExcProMasEliminarDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDeudaExcProMasEliminarDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		// TODO revisar el canAccess: asunto permisos y constantes
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
				GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO); 
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();

		String stringServicio = "";
		// ActionForward actionForward = null;
		try {

			//CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			// Bajo el searchPage del userSession
			DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPageVO = (DeudaExcProMasEliminarSearchPage) userSession.get(DeudaExcProMasEliminarSearchPage.NAME);

			// Si es nulo no se puede continuar
			if (deudaExcProMasEliminarSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaExcProMasEliminarSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaExcProMasEliminarSearchPage.NAME); 
			}
			// invocar al servicio que realiza la consulta 
			stringServicio = "getDeudaExcProMasElimSelIndSeachPageInit(userSession, deudaExcProMasElimSelIndSeachPageVO)";
			deudaExcProMasEliminarSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().getDeudaExcProMasElimSelIndSeachPageInit(userSession, deudaExcProMasEliminarSearchPageVO);

			// Tiene errores recuperables
			if (deudaExcProMasEliminarSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasEliminarSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaExcProMasEliminarSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaExcProMasEliminarSearchPage.NAME, deudaExcProMasEliminarSearchPageVO);
			}

			// Tiene errores no recuperables
			if (deudaExcProMasEliminarSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaExcProMasEliminarSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaExcProMasEliminarSearchPage.NAME, deudaExcProMasEliminarSearchPageVO);
			}

			// Seteo los valores de navegacion en el adapter
			deudaExcProMasEliminarSearchPageVO.setValuesFromNavModel(navModel);

			// Subo el apdater al userSession
			userSession.put(DeudaExcProMasEliminarSearchPage.NAME, deudaExcProMasEliminarSearchPageVO);

			saveDemodaMessages(request, deudaExcProMasEliminarSearchPageVO);

			// Envio el VO al request
			request.setAttribute(DeudaExcProMasEliminarSearchPage.NAME, deudaExcProMasEliminarSearchPageVO);

			return mapping.findForward(GdeConstants.FWD_DEUDA_EXC_PRO_MAS_ELIMINAR_SELEC_IND_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaExcProMasEliminarSearchPage.NAME);
		}
	}

	// eliminar seleccion individual
	public ActionForward eliminarSeleccionIndividual(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		// TODO ver constantes del canAccess
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
				GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el SearchPage del userSession
			DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSelecIndSearchPageVO = (DeudaExcProMasEliminarSearchPage) userSession.get(DeudaExcProMasEliminarSearchPage.NAME);

			// Si es nulo no se puede continuar
			if (deudaExcProMasEliminarSelecIndSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaExcProMasEliminarSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaExcProMasEliminarSearchPage.NAME); 
			}

			// no realizamos populate ya que solo leemos del request la lista de ids seleccionados
			deudaExcProMasEliminarSelecIndSearchPageVO.setListIdSelAlmDet(request.getParameterValues("listIdSelAlmDet"));

			// Tiene errores recuperables
			if (deudaExcProMasEliminarSelecIndSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasEliminarSelecIndSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaExcProMasEliminarSelecIndSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaExcProMasEliminarSearchPage.NAME, deudaExcProMasEliminarSelecIndSearchPageVO);
			}

			// llamada al servicio
			deudaExcProMasEliminarSelecIndSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().eliminarSelIndDeudaExcProMas(userSession, deudaExcProMasEliminarSelecIndSearchPageVO);

			// Tiene errores recuperables
			if (deudaExcProMasEliminarSelecIndSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasEliminarSelecIndSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaExcProMasEliminarSelecIndSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaExcProMasEliminarSearchPage.NAME, deudaExcProMasEliminarSelecIndSearchPageVO);
			}

			// Tiene errores no recuperables
			if (deudaExcProMasEliminarSelecIndSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaExcProMasEliminarSelecIndSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaExcProMasEliminarSearchPage.NAME, deudaExcProMasEliminarSelecIndSearchPageVO);
			}

			// Fue Exitoso
			//le seteo la accion y metodo a donde ir al navModel para que la use despues del Confirmar
			String actionConfirmacion = "/gde/BuscarDeudaExcProMasEliminar";
			String methodConfirmacion = "buscar";
			String act = ""; // no hace falta el act para el metodo buscar del action BuscarDeudaExcProMasEliminar
			// voName es "" para que no saque el VO de la sesion ya que se utiliza cuando vuelve
			
			return forwardConfirmarOk(mapping, request, 
					funcName, "", actionConfirmacion , methodConfirmacion, act);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaExcProMasEliminarSearchPage.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		// no se realiza un baseVolver para no alterar la misma instancia del SearchPage utilizado por los 2 actions
		return mapping.findForward(GdeConstants.FWD_BUSCAR_DEUDA_EXC_PRO_MAS_ELIMINAR);
	}

}
