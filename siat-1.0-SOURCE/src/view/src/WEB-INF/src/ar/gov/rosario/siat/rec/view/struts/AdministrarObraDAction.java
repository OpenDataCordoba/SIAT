//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

import java.util.List;

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
import ar.gov.rosario.siat.rec.iface.model.ObraAdapter;
import ar.gov.rosario.siat.rec.iface.model.ObraVO;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraSearchPage;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarObraDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarObraDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_OBRA, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ObraAdapter obraAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getObraAdapterForView(userSession, commonKey)";
				obraAdapterVO = RecServiceLocator.getCdmService().getObraAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_OBRA_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getObraAdapterForView(userSession, commonKey)";
				obraAdapterVO = RecServiceLocator.getCdmService().getObraAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_OBRA_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getObraAdapterForDelete(userSession, commonKey)";
				obraAdapterVO = RecServiceLocator.getCdmService().getObraAdapterForView(userSession, commonKey);
				obraAdapterVO.addMessage(BaseError.MSG_ELIMINAR, RecError.OBRA_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_OBRA_VIEW_ADAPTER);					
			}

			if (act.equals(RecSecurityConstants.MTD_OBRA_CAMBIAR_ESTADO)) {
				stringServicio = "getObraPlanillaObraAdapterForView(userSession, commonKey)";
				obraAdapterVO = RecServiceLocator.getCdmService().getObraAdapterForCambiarEstado(userSession, commonKey);				
				actionForward = mapping.findForward(RecConstants.FWD_OBRA_VIEW_ADAPTER);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (obraAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + obraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			obraAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				ObraAdapter.NAME + ": " + obraAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ObraAdapter.NAME, obraAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ObraAdapter.NAME, obraAdapterVO);
			
			saveDemodaMessages(request, obraAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObraAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			RecConstants.ACTION_ADMINISTRAR_ENC_OBRA, BaseConstants.ACT_MODIFICAR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_OBRA, 
			BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObraAdapter obraAdapterVO = (ObraAdapter) userSession.get(ObraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (obraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObraAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// llamada al servicio
			ObraVO obraVO = RecServiceLocator.getCdmService().deleteObra
				(userSession, obraAdapterVO.getObra());
			
            // Tiene errores recuperables
			if (obraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraAdapterVO.infoString());
				saveDemodaErrors(request, obraVO);				
				request.setAttribute(ObraAdapter.NAME, obraAdapterVO);
				return mapping.findForward(RecConstants.FWD_OBRA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (obraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + obraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ObraAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObraAdapter.NAME);
		}
	}
	
	public ActionForward cambiarEstado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			RecSecurityConstants.ABM_OBRA, RecSecurityConstants.MTD_OBRA_CAMBIAR_ESTADO);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObraAdapter obraVO = (ObraAdapter) userSession.get(ObraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (obraVO == null) {
				log.error("error en: "  + funcName + ": " + ObraAdapter.NAME 
					+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(obraVO, request);
			
            // Tiene errores recuperables
			if (obraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraVO.infoString()); 
				saveDemodaErrors(request, obraVO);
				return mapping.findForward(RecConstants.FWD_OBRA_VIEW_ADAPTER);
			}
			
			// llamada al servicio
			ObraVO planillaCuadraVO = RecServiceLocator.getCdmService().cambiarEstadoObra(userSession, obraVO);
			
            // Tiene errores recuperables
			if (planillaCuadraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraVO);
				request.setAttribute(ObraAdapter.NAME, obraVO);					
				return mapping.findForward(RecConstants.FWD_OBRA_VIEW_ADAPTER);					
			}
			
			// Tiene errores no recuperables
			if (planillaCuadraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + obraVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					ObraAdapter.NAME, obraVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ObraAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObraAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, ObraAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ObraAdapter.NAME);

	}
	
	// Metodos relacionados ObraFormaPago
	public ActionForward verObraFormaPago(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_OBRAFORMAPAGO);

	}

	public ActionForward modificarObraFormaPago(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_OBRAFORMAPAGO);

	}

	public ActionForward eliminarObraFormaPago(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_OBRAFORMAPAGO);

	}
	
	public ActionForward agregarObraFormaPago(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_OBRAFORMAPAGO);
		
	}

	public ActionForward activarObraFormaPago(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardActivarAdapter(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_OBRAFORMAPAGO);
			
	}
	
	public ActionForward desactivarObraFormaPago(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardDesactivarAdapter(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_OBRAFORMAPAGO);
			
	}
	
	// Metodos relacionados PlanillaCuadra	
	public ActionForward verPlanillaCuadra(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_OBRAPLANILLACUADRA);

	}
	
	public ActionForward modificarPlanillaCuadra(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
				RecConstants.ACTION_ADMINISTRAR_ENC_PLANILLACUADRA, RecConstants.ACT_MODIFICAR_NUMERO_CUADRA);

	}
	
	public ActionForward eliminarPlanillaCuadra(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, 
			RecConstants.ACTION_ADMINISTRAR_OBRAPLANILLACUADRA);

	}
	
	public ActionForward cambiarEstadoPlanillaCuadra(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			RecConstants.ACTION_ADMINISTRAR_OBRAPLANILLACUADRA, RecConstants.ACT_CAMBIAR_ESTADO);

	}
	
	public ActionForward agregarPlanillaCuadra(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// seteo la accion y el parametro para volver
		navModel.setPrevAction(mapping.getPath());
		navModel.setPrevActionParameter(BaseConstants.ACT_REFILL);

		// seteo la opcion agregar en false
		navModel.setAgregarEnSeleccion(false);

		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);

		// seteo los parametros para usar desde el action de planilla cuadra
		ObraAdapter obraAdapteterVO = (ObraAdapter) userSession.get(ObraAdapter.NAME);
		List<Long> listIdPlanillasEnObra = ListUtil.getListLongIdFromListModel
			(obraAdapteterVO.getObra().getListPlanillaCuadra());
		Long idRecursoObra = obraAdapteterVO.getObra().getRecurso().getId(); 

		navModel.putParameter(PlanillaCuadraSearchPage.ISMULTISELECT, true);		
		navModel.putParameter(PlanillaCuadraSearchPage.LISTID_PLANILLACUADRA, listIdPlanillasEnObra);
		navModel.putParameter(PlanillaCuadraSearchPage.ID_RECURSO, idRecursoObra);		

		return mapping.findForward(RecConstants.ACTION_BUSCAR_PLANILLACUADRA);

	}

	// Metodos relacionados con asignar repartidores a planillas de la obra
	public ActionForward asignarRepartidor(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			RecConstants.ACTION_BUSCARPLANILLAS_FORASIGNARREPARTIDOR, 
			RecConstants.ACT_OBRA_ASIGNAR_REPARTIDOR);

	}

	public ActionForward imprimirReportFromAdapter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			// obtiene el nombre del page del request
			//String name = request.getParameter("name");
			String name = ObraAdapter.NAME;
			String reportFormat = request.getParameter("report.reportFormat");
			
			// **Bajo el searchPage del userSession
			String responseFile = request.getParameter("responseFile");
			if ("1".equals(responseFile)) {
				String fileName = (String) userSession.get("baseImprimir.reportFilename");
				// realiza la visualizacion del reporte
				baseResponseEmbedContent(response, fileName, "application/pdf");
				return null;
			}
			
			// Bajo el adapter del userSession
			ObraAdapter obraAdapterVO = (ObraAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (obraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			obraAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			obraAdapterVO = RecServiceLocator.getCdmService().imprimirReporte(userSession, obraAdapterVO);

			// limpia la lista de reports y la lista de tablas
			obraAdapterVO.getReport().getListReport().clear();
			obraAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (obraAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraAdapterVO.infoString());
				saveDemodaErrors(request, obraAdapterVO);				
				request.setAttribute(name, obraAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (obraAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + obraAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, obraAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = obraAdapterVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);
		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}
	
	// Metodos relacionados con Reprogramacion de Vencimientos
	public ActionForward agregarObrRepVen(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_OBRREPVEN);
			
		}
}
	
