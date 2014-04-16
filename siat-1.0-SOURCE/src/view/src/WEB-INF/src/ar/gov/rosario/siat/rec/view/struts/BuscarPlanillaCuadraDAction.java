//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.rec.iface.model.ObraAdapter;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraSearchPage;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarPlanillaCuadraDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarPlanillaCuadraDAction.class);
	
	@SuppressWarnings("unchecked")
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_PLANILLACUADRA, act); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			
			PlanillaCuadraSearchPage planillaCuadraSearchPageParam = new PlanillaCuadraSearchPage();
			Long idRecurso = (Long) userSession.getNavModel()
										.getParameter(PlanillaCuadraSearchPage.ID_RECURSO);
			planillaCuadraSearchPageParam.getPlanillaCuadra().getRecurso().setId(idRecurso);			

			PlanillaCuadraSearchPage planillaCuadraSearchPageVO = 
				RecServiceLocator.getCdmService().getPlanillaCuadraSearchPageInit
				(userSession, planillaCuadraSearchPageParam);

			// Tiene errores recuperables
			if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (planillaCuadraSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planillaCuadraSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
			}

			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , PlanillaCuadraSearchPage.NAME, 
				planillaCuadraSearchPageVO);

			// si esta presente el id de obra los seteo
			List<Long> listIdPlanillaCuadra = (ArrayList<Long>)(userSession.getNavModel()
					.getParameter(PlanillaCuadraSearchPage.LISTID_PLANILLACUADRA));
			
			Boolean isMultiselect = (Boolean) userSession.getNavModel()
					.getParameter(PlanillaCuadraSearchPage.ISMULTISELECT);

			planillaCuadraSearchPageVO.setIsMultiselect(isMultiselect);
			planillaCuadraSearchPageVO.setListIdPlanillaCuadra(listIdPlanillaCuadra);
			
			return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, PlanillaCuadraSearchPage.NAME);
		
	}
	
	public ActionForward buscar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			PlanillaCuadraSearchPage planillaCuadraSearchPageVO = (PlanillaCuadraSearchPage) userSession
					.get(PlanillaCuadraSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (planillaCuadraSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + PlanillaCuadraSearchPage.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(planillaCuadraSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				planillaCuadraSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
			}
			
			// le indico que no pagine en caso de ser seleccion multiple
			if (planillaCuadraSearchPageVO.getIsMultiselect() != null
				&& planillaCuadraSearchPageVO.getIsMultiselect()) {
				planillaCuadraSearchPageVO.setPaged(false);
			}
				
			// Llamada al servicio	
			planillaCuadraSearchPageVO = RecServiceLocator.getCdmService()
								.getPlanillaCuadraSearchPageResult(userSession, planillaCuadraSearchPageVO);			

			// Tiene errores recuperables
			if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (planillaCuadraSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planillaCuadraSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);

			// Subo en el el searchPage al userSession
			userSession.put(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
			
			return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_PLANILLACUADRA);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, 
			RecConstants.ACTION_ADMINISTRAR_ENC_PLANILLACUADRA);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, 
			funcName, RecConstants.ACTION_ADMINISTRAR_ENC_PLANILLACUADRA);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_PLANILLACUADRA);

	}
	
	public ActionForward cambiarEstado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage(mapping, request, funcName, 
			RecConstants.ACTION_ADMINISTRAR_PLANILLACUADRA,RecConstants.ACT_CAMBIAR_ESTADO);
	}
	
	public ActionForward informarCatastrales(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage(mapping, request, funcName, 
			RecConstants.ACTION_ADMINISTRAR_PLANILLACUADRA,RecConstants.ACT_INFORMAR_CATASTRALES);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, PlanillaCuadraSearchPage.NAME);

	}

	/** Agrega las planillas seleccionadas a una obra.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward agregarAObra(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_OBRA_PLANILLACUADRA, 
			BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanillaCuadraSearchPage planillaCuadraSearchPageVO= (PlanillaCuadraSearchPage) 
				userSession.get(PlanillaCuadraSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (planillaCuadraSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + PlanillaCuadraSearchPage.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraSearchPage.NAME); 
			}
	
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planillaCuadraSearchPageVO,request);
			
	        // Tiene errores recuperables
			if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
				
				saveDemodaErrors(request, planillaCuadraSearchPageVO);	
				request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);

				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_SEARCHPAGE);
			}
			
			//recupero la obra cargada en session para sacar su id
			ObraAdapter obraAdapterVO = (ObraAdapter) userSession.get(ObraAdapter.NAME);

			// seteo el is de obra para poder asociar la planilla a la obra
			planillaCuadraSearchPageVO.getPlanillaCuadra().getObra().setId(obraAdapterVO.getObra().getId());
	
			// llamada al servicio
			planillaCuadraSearchPageVO = RecServiceLocator.getCdmService()
				.createObraPlanillaCuadra(userSession, planillaCuadraSearchPageVO);
			
	        // Tiene errores recuperables
			if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraSearchPageVO);
				request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_SEARCHPAGE);
			}
	
			// Tiene errores no recuperables
			if (planillaCuadraSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planillaCuadraSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
			}
	
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanillaCuadraSearchPage.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraSearchPage.NAME);
		}
	}
	
	// Metodos usados para la asignacion de repartidor a las planillas en las Obras
	/** Inicializa la asinacion de un Rerpartidor a las
	 *  planillas de una obra.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward inicializarForAsignarRepartidor(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, 
			RecSecurityConstants.ABM_OBRA, RecSecurityConstants.MTD_OBRA_ASIGNAR_REPARTIDOR); 
		if (userSession == null) return forwardErrorSession(request);

		try {

			// bajo el adapter de obra que esta en session para recuperar el id de obra
			// si esta presente el id de obra los seteo
			ObraAdapter obraAdapter = (ObraAdapter) userSession.get(ObraAdapter.NAME);
			CommonKey obraKey = new CommonKey(obraAdapter.getObra().getId());

			PlanillaCuadraSearchPage planillaCuadraSearchPageVO = 
				RecServiceLocator.getCdmService().getPlanillaCuadraForAsignarRepartidoresInit(userSession, obraKey);
				
			// Tiene errores recuperables
			if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraSearchPageVO);
				request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_FORASIGNARREPARTIDOR);
			} 

			// Tiene errores no recuperables
			if (planillaCuadraSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planillaCuadraSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
			}

			// indico que el searchPage no paginara
			planillaCuadraSearchPageVO.setPaged(false);
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , PlanillaCuadraSearchPage.NAME, 
				planillaCuadraSearchPageVO);

			return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_FORASIGNARREPARTIDOR);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraSearchPage.NAME);
		}
	}
	
	public ActionForward limpiarForAsignarRepartidores(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		 
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) forwardErrorSession(request);
		
		try {
			
			NavModel navModel = userSession.getNavModel();

			PlanillaCuadraSearchPage planillaCuadraSearchPageVO = (PlanillaCuadraSearchPage) 
				userSession.get(PlanillaCuadraSearchPage.NAME);

			// reestablesco el navmodel como estaba cuando se entro al action
			navModel.setValuesFromCommonNavegableView(planillaCuadraSearchPageVO);

			return this.inicializarForAsignarRepartidor(mapping, form, request, response);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraSearchPage.NAME);
		}
	}
	
	public ActionForward buscarForAsignarRepartidor(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			PlanillaCuadraSearchPage planillaCuadraSearchPageVO = 
				(PlanillaCuadraSearchPage) userSession.get(PlanillaCuadraSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (planillaCuadraSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + PlanillaCuadraSearchPage.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(planillaCuadraSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				planillaCuadraSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraSearchPageVO);
				request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_FORASIGNARREPARTIDOR);
			}
				
			// Llamada al servicio	
			planillaCuadraSearchPageVO = 
				RecServiceLocator.getCdmService().getPlanillaCuadraForAsignarRepartidoresResult
				(userSession, planillaCuadraSearchPageVO);			

			// Tiene errores recuperables
			if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraSearchPageVO);
				// Envio el VO al request
				request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_FORASIGNARREPARTIDOR);
			}
			
			// Tiene errores no recuperables
			if (planillaCuadraSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planillaCuadraSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);

			// Subo en el el searchPage al userSession
			userSession.put(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
			
			return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_FORASIGNARREPARTIDOR);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraSearchPage.NAME);
		}
	}
	
	public ActionForward asignarRepartidor(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			RecSecurityConstants.ABM_OBRA, RecSecurityConstants.MTD_OBRA_ASIGNAR_REPARTIDOR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanillaCuadraSearchPage planillaCuadraSearchPageVO= (PlanillaCuadraSearchPage) 
				userSession.get(PlanillaCuadraSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (planillaCuadraSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + PlanillaCuadraSearchPage.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraSearchPage.NAME); 
			}
	
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planillaCuadraSearchPageVO,request);
			
	        // Tiene errores recuperables
			if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + 
					planillaCuadraSearchPageVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraSearchPageVO);	
				request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_FORASIGNARREPARTIDOR);
			}

			// recupero el repartidor seleccionado, el valor puede
			// ser -2,-1 o un valor de repartidor
			CommonKey repartidorKey = new CommonKey(planillaCuadraSearchPageVO.getRepartidor().getId());

			// llamada al servicio
			planillaCuadraSearchPageVO = RecServiceLocator.getCdmService().asignarDesasignarRepartidor
				(userSession, planillaCuadraSearchPageVO, repartidorKey);

	        // Tiene errores recuperables
			if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraSearchPageVO);
				request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_FORASIGNARREPARTIDOR);
			}
	
			// Tiene errores no recuperables
			if (planillaCuadraSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planillaCuadraSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
			}
	
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanillaCuadraSearchPage.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraSearchPage.NAME);
		}
	}
	
	public ActionForward buscarCalleForAsignarRepartidor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			
			// Bajo el adapter del userSession
			PlanillaCuadraSearchPage planillaCuadraSearchPageVO = 
				(PlanillaCuadraSearchPage) userSession.get(PlanillaCuadraSearchPage.NAME);

			// Si es nulo no se puede continuar
			if (planillaCuadraSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + PlanillaCuadraSearchPage.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planillaCuadraSearchPageVO, request);

	        // Tiene errores recuperables
			if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraSearchPageVO);
				request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);			
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_FORASIGNARREPARTIDOR);
			}

			return forwardSeleccionar(mapping, request, RecConstants.MTD_PARAM_CALLE_FORASIGNARREPARTIDOR, 
				RecConstants.ACTION_BUSCAR_CALLE, false); 
		}
	
	public ActionForward paramCalleForAsignarRepartidor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				// Bajo el adapter del userSession
				PlanillaCuadraSearchPage planillaCuadraSearchPageVO = 
					(PlanillaCuadraSearchPage) userSession.get(PlanillaCuadraSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (planillaCuadraSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + PlanillaCuadraSearchPage.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraSearchPage.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					//Seteo el PageNumber en 0
					planillaCuadraSearchPageVO.setPageNumber(0L);
					// Envio el VO al request
					request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
					return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_FORASIGNARREPARTIDOR); 
				}

				// seteo el id de calle seleccionado en el searchPage
				planillaCuadraSearchPageVO.getPlanillaCuadra().getCallePpal().setId(new Long(selectedId));
				
				planillaCuadraSearchPageVO = RecServiceLocator.getCdmService().getPlanillaCuadraSearchPageParamCalle
					(userSession, planillaCuadraSearchPageVO); 

	            // Tiene errores recuperables
				if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
					saveDemodaErrors(request, planillaCuadraSearchPageVO);
					//Seteo el PageNumber en 0
					planillaCuadraSearchPageVO.setPageNumber(0L);
					return forwardErrorRecoverable(mapping, request, userSession, 
							PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				}

				// Tiene errores no recuperables
				if (planillaCuadraSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + planillaCuadraSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, planillaCuadraSearchPageVO);

				//Seteo el PageNumber en 0
				planillaCuadraSearchPageVO.setPageNumber(0L);
				
				// Envio el VO al request
				request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);

				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_FORASIGNARREPARTIDOR);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PlanillaCuadraSearchPage.NAME);
			}
		}
	// Fin de Metodos usados para la asignacion de repartidor a las planillas las Obras
	
	public ActionForward buscarCalle(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
		// Bajo el adapter del userSession
		PlanillaCuadraSearchPage planillaCuadraSearchPageVO = 
			(PlanillaCuadraSearchPage) userSession.get(PlanillaCuadraSearchPage.NAME);

		// Si es nulo no se puede continuar
		if (planillaCuadraSearchPageVO == null) {
			log.error("error en: "  + funcName + ": " + PlanillaCuadraSearchPage.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraSearchPage.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(planillaCuadraSearchPageVO, request);

        // Tiene errores recuperables
		if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
			saveDemodaErrors(request, planillaCuadraSearchPageVO);
			request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);			
			return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_SEARCHPAGE);
		}

		return forwardSeleccionar(mapping, request, RecConstants.MTD_PARAM_CALLE, 
			RecConstants.ACTION_BUSCAR_CALLE, false); 
	}

	public ActionForward paramCalle(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			// Bajo el adapter del userSession
			PlanillaCuadraSearchPage planillaCuadraSearchPageVO = 
				(PlanillaCuadraSearchPage) userSession.get(PlanillaCuadraSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (planillaCuadraSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + PlanillaCuadraSearchPage.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraSearchPage.NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al search page
			if (StringUtil.isNullOrEmpty(selectedId)) {
				
				if (ListUtil.isNullOrEmpty(planillaCuadraSearchPageVO.getListResult()))
					//Seteo el PageNumber en 0
					planillaCuadraSearchPageVO.setPageNumber(0L);
				
				// Envio el VO al request
				request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_SEARCHPAGE); 
			}

			// seteo el id de calle seleccionado en el searchPage
			planillaCuadraSearchPageVO.getPlanillaCuadra().getCallePpal().setId(new Long(selectedId));
			
			planillaCuadraSearchPageVO = RecServiceLocator.getCdmService().getPlanillaCuadraSearchPageParamCalle
				(userSession, planillaCuadraSearchPageVO); 

            // Tiene errores recuperables
			if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
			}

			// Tiene errores no recuperables
			if (planillaCuadraSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planillaCuadraSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, planillaCuadraSearchPageVO);

			if (ListUtil.isNullOrEmpty(planillaCuadraSearchPageVO.getListResult()))
				//Seteo el PageNumber en 0
				planillaCuadraSearchPageVO.setPageNumber(0L);

			// Envio el VO al request
			request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);

			return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraSearchPage.NAME);
		}
	}

	public ActionForward paramRecurso (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PlanillaCuadraSearchPage planillaCuadraSearchPageVO = (PlanillaCuadraSearchPage) userSession.get(PlanillaCuadraSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (planillaCuadraSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + PlanillaCuadraSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraSearchPage.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(planillaCuadraSearchPageVO, request);
				
	            // Tiene errores recuperables
				if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
					saveDemodaErrors(request, planillaCuadraSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				}
				
				// llamada al servicio
				planillaCuadraSearchPageVO = RecServiceLocator.getCdmService().getPlanillaCuadraSearchPageParamRecurso(userSession, planillaCuadraSearchPageVO);
				
	            // Tiene errores recuperables
				if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
					saveDemodaErrors(request, planillaCuadraSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (planillaCuadraSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + planillaCuadraSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				}
				
				// Envio el VO al request
				request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
		
				if (ListUtil.isNullOrEmpty(planillaCuadraSearchPageVO.getListResult()))
					//Seteo el PageNumber en 0
					planillaCuadraSearchPageVO.setPageNumber(0L);
				
				// Subo el apdater al userSession
				userSession.put(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_SEARCHPAGE);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PlanillaCuadraSearchPage.NAME);
			}
		}
	
	public ActionForward paramContrato (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PlanillaCuadraSearchPage planillaCuadraSearchPageVO = (PlanillaCuadraSearchPage) userSession.get(PlanillaCuadraSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (planillaCuadraSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + PlanillaCuadraSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraSearchPage.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(planillaCuadraSearchPageVO, request);
				
	            // Tiene errores recuperables
				if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
					saveDemodaErrors(request, planillaCuadraSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				}
				
				// llamada al servicio
				planillaCuadraSearchPageVO = RecServiceLocator.getCdmService().getPlanillaCuadraSearchPageParamContrato(userSession, planillaCuadraSearchPageVO);
				
	            // Tiene errores recuperables
				if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
					saveDemodaErrors(request, planillaCuadraSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (planillaCuadraSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + planillaCuadraSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				}
				
				// Envio el VO al request
				request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				
				if (ListUtil.isNullOrEmpty(planillaCuadraSearchPageVO.getListResult()))
					//Seteo el PageNumber en 0
					planillaCuadraSearchPageVO.setPageNumber(0L);
				
				// Subo el apdater al userSession
				userSession.put(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_SEARCHPAGE);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PlanillaCuadraSearchPage.NAME);
			}
		}
	
	public ActionForward paramTipoObra (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PlanillaCuadraSearchPage planillaCuadraSearchPageVO = (PlanillaCuadraSearchPage) userSession.get(PlanillaCuadraSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (planillaCuadraSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + PlanillaCuadraSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraSearchPage.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(planillaCuadraSearchPageVO, request);
				
	            // Tiene errores recuperables
				if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
					saveDemodaErrors(request, planillaCuadraSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				}
				
				// llamada al servicio
				planillaCuadraSearchPageVO = RecServiceLocator.getCdmService().getPlanillaCuadraSearchPageParamTipoObra(userSession, planillaCuadraSearchPageVO);
				
	            // Tiene errores recuperables
				if (planillaCuadraSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + planillaCuadraSearchPageVO.infoString()); 
					saveDemodaErrors(request, planillaCuadraSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (planillaCuadraSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + planillaCuadraSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				}
				
				// Envio el VO al request
				request.setAttribute(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				
				if (ListUtil.isNullOrEmpty(planillaCuadraSearchPageVO.getListResult()))
					//Seteo el PageNumber en 0
					planillaCuadraSearchPageVO.setPageNumber(0L);
				
				// Subo el apdater al userSession
				userSession.put(PlanillaCuadraSearchPage.NAME, planillaCuadraSearchPageVO);
				
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_SEARCHPAGE);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PlanillaCuadraSearchPage.NAME);
			}
		}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanillaCuadraSearchPage.NAME);
		
	}

}
