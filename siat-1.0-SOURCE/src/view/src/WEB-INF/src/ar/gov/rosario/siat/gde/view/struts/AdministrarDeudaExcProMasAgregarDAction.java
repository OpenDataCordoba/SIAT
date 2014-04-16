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
import ar.gov.rosario.siat.gde.iface.model.DeudaExcProMasAgregarSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;

//Administra la agregacion de la seleccion almacenada de la deuda a excluir del proceso de envio a judicial

public final class AdministrarDeudaExcProMasAgregarDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDeudaExcProMasAgregarDAction.class);

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
			DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPageVO = (DeudaExcProMasAgregarSearchPage) userSession.get(DeudaExcProMasAgregarSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (deudaExcProMasAgregarSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaExcProMasAgregarSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaExcProMasAgregarSearchPage.NAME); 
			}
			// invocar al servicio que vuelve a ejecutar la consulta contenida en el searchPage 
			stringServicio = "getDeudaExcProMasAgregSelIndSeachPageInit(userSession, deudaExcProMasAgregarSearchPageVO)";
			deudaExcProMasAgregarSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().getDeudaExcProMasAgregarSelIndSeachPage(userSession, deudaExcProMasAgregarSearchPageVO);

			// Tiene errores recuperables
			if (deudaExcProMasAgregarSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasAgregarSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaExcProMasAgregarSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSearchPageVO);
			}

			// Tiene errores no recuperables
			if (deudaExcProMasAgregarSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaExcProMasAgregarSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSearchPageVO);
			}

			// Seteo los valores de navegacion en el adapter
			deudaExcProMasAgregarSearchPageVO.setValuesFromNavModel(navModel);

			// para que se dibuje la lista de resultados porque getView trabaja sobre el pageNumber
			deudaExcProMasAgregarSearchPageVO.setPageNumber(1L);

			// Subo el apdater al userSession
			userSession.put(DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSearchPageVO);

			saveDemodaMessages(request, deudaExcProMasAgregarSearchPageVO);

			// Envio el VO al request
			request.setAttribute(DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSearchPageVO);

			return mapping.findForward(GdeConstants.FWD_DEUDA_EXC_PRO_MAS_AGREGAR_SELEC_IND_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaExcProMasAgregarSearchPage.NAME);
		}
	}


	// agregar seleccion individual
	public ActionForward agregarSeleccionIndividual(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		// TODO ver constantes del canAccess
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
				GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el SearchPage del userSession
			DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSelecIndSearchPageVO = (DeudaExcProMasAgregarSearchPage) userSession.get(DeudaExcProMasAgregarSearchPage.NAME);

			// Si es nulo no se puede continuar
			if (deudaExcProMasAgregarSelecIndSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaExcProMasAgregarSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaExcProMasAgregarSearchPage.NAME); 
			}

			// no realizamos populate ya que solo leemos del request la lista de ids seleccionados
			deudaExcProMasAgregarSelecIndSearchPageVO.setListIdDeudaAdmin(request.getParameterValues("listIdDeudaAdmin"));

			// Tiene errores recuperables
			if (deudaExcProMasAgregarSelecIndSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasAgregarSelecIndSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaExcProMasAgregarSelecIndSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSelecIndSearchPageVO);
			}

			// llamada al servicio
			deudaExcProMasAgregarSelecIndSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().agregarSelIndDeudaExcProMas(userSession, deudaExcProMasAgregarSelecIndSearchPageVO);
 
			// Tiene errores recuperables
			if (deudaExcProMasAgregarSelecIndSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasAgregarSelecIndSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaExcProMasAgregarSelecIndSearchPageVO);
				request.setAttribute(DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSelecIndSearchPageVO);
				return mapping.getInputForward();
				//return forwardErrorRecoverable(mapping, request, userSession, DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSelecIndSearchPageVO);
			}

			// Tiene errores no recuperables
			if (deudaExcProMasAgregarSelecIndSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaExcProMasAgregarSelecIndSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSelecIndSearchPageVO);
			}

			// Fue Exitoso
			//le seteo la accion y metodo a donde ir al navModel para que la use despues del Confirmar
			String actionConfirmacion = "/gde/BuscarDeudaExcProMasAgregar";
			String methodConfirmacion = "buscar";
			String act = ""; // no hace falta el act para el metodo buscar del action BuscarDeudaExcProMasAgregar

			// voName es "" para que no saque el VO de la sesion ya que se utiliza cuando vuelve
			
			return forwardConfirmarOk(mapping, request, 
					funcName, "", actionConfirmacion , methodConfirmacion, act);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaExcProMasAgregarSearchPage.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		// no se realiza un baseVolver para no alterar la misma instancia del SearchPage utilizado por los 2 actions
		return mapping.findForward(GdeConstants.FWD_BUSCAR_DEUDA_EXC_PRO_MAS_AGREGAR);
	}

}
