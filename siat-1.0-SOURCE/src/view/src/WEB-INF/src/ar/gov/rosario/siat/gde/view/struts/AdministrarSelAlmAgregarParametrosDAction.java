//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

import java.util.Map;
import java.util.TreeMap;

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
import ar.gov.rosario.siat.def.view.util.DefinitionUtil;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.gde.iface.model.SelAlmAgregarParametrosSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.SiNo;

public final class AdministrarSelAlmAgregarParametrosDAction extends BaseDispatchAction {
	
	private Log log = LogFactory.getLog(AdministrarSelAlmAgregarParametrosDAction.class);
	
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_SEL_ALM, 
				GdeSecurityConstants.MTD_AGREGAR_PARAMETROS); 
 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			CommonKey recursoKey = new CommonKey((Long) userSession.getNavModel().getParameter(
					SelAlmAgregarParametrosSearchPage.ID_RECURSO));
			CommonKey corridaKey = new CommonKey((Long) userSession.getNavModel().getParameter(
					SelAlmAgregarParametrosSearchPage.ID_CORRIDA));
			CommonKey selAlmKey  = new CommonKey((Long) userSession.getNavModel().getParameter(
					SelAlmAgregarParametrosSearchPage.ID_SEL_ALM_INC));
			CommonKey tipoProcMasKey = new CommonKey((Long)userSession.getNavModel().getParameter(
					SelAlmAgregarParametrosSearchPage.ID_TIPO_PROC_MAS));
			CommonKey viaDeudaKey = new CommonKey((Long)userSession.getNavModel().getParameter(
					SelAlmAgregarParametrosSearchPage.ID_VIA_DEUDA));

			SelAlmAgregarParametrosSearchPage selAlmAgregarParametrosSearchPage = GdeServiceLocator.getSelAlmServiceHbmImpl().getSelAlmDeudaAgregarSearchPageInit(userSession, recursoKey, corridaKey, selAlmKey, tipoProcMasKey, viaDeudaKey);
			
			// Tiene errores recuperables
			if (selAlmAgregarParametrosSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + selAlmAgregarParametrosSearchPage.infoString()); 
				saveDemodaErrors(request, selAlmAgregarParametrosSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, SelAlmAgregarParametrosSearchPage.NAME, selAlmAgregarParametrosSearchPage);
			} 

			// Tiene errores no recuperables
			if (selAlmAgregarParametrosSearchPage.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + selAlmAgregarParametrosSearchPage.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SelAlmAgregarParametrosSearchPage.NAME, selAlmAgregarParametrosSearchPage);
			}
			
			baseInicializarSearchPage(mapping, request, userSession , SelAlmAgregarParametrosSearchPage.NAME, selAlmAgregarParametrosSearchPage);
			
			return mapping.findForward(GdeConstants.FWD_SEL_ALM_AGREGAR_PARAMETROS_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SelAlmAgregarParametrosSearchPage.NAME);
		}
	}
	
	
	public ActionForward agregarParametrosSelAlm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_SEL_ALM, 
					GdeSecurityConstants.MTD_AGREGAR_PARAMETROS); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el SearchPage del userSession
				SelAlmAgregarParametrosSearchPage selAlmAgregarParametrosSearchPageVO = (SelAlmAgregarParametrosSearchPage) userSession.get(SelAlmAgregarParametrosSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (selAlmAgregarParametrosSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + SelAlmAgregarParametrosSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, SelAlmAgregarParametrosSearchPage.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(selAlmAgregarParametrosSearchPageVO, request);
				
				selAlmAgregarParametrosSearchPageVO.setListIdRecClaDeu(request.getParameterValues("listIdRecClaDeu"));
				
				// limpio las exenciones seleccionadas previamente, para cargar la nueva seleccion. 
				selAlmAgregarParametrosSearchPageVO.clearExencionesSeleccionadas();
				// carga los valores de las selecciones de excenciones en el mapa exencionesSeleccionadas
				// con clave igual al id de la excencion y con valor seleccionado (0 o 1), descartando el Todos
				for (ExencionVO exencionVO : selAlmAgregarParametrosSearchPageVO.getListExencion()) {
					String seleccion = request.getParameter("dynaExcencion("+exencionVO.getIdView()+")");
					if (!StringUtil.isNullOrEmpty(seleccion) && 
							!seleccion.equals(StringUtil.formatInteger(SiNo.OpcionTodo.getId()))  ){
						selAlmAgregarParametrosSearchPageVO.getExencionesSeleccionadas().put(exencionVO.getIdView(), seleccion);
					}
				}

				// limpio los atributos seleccionadas previamente, para cargar la nueva seleccion. 
				selAlmAgregarParametrosSearchPageVO.clearAtributosSeleccionados();
				// carga los valores de las selecciones de atributos en el mapa atributosSeleccionados
				// con clave igual al id del atributo y con valor seleccionado (0 o 1), descartando el Todos
				for (AtributoVO atributoVO : selAlmAgregarParametrosSearchPageVO.getListAtributo()) {
					String seleccion = request.getParameter("dynaAtributo("+atributoVO.getIdView()+")");
					if (!StringUtil.isNullOrEmpty(seleccion) && 
							!seleccion.equals(StringUtil.formatInteger(SiNo.OpcionTodo.getId()))  ){
						selAlmAgregarParametrosSearchPageVO.getAtributosSeleccionados().put(atributoVO.getIdView(), seleccion);
					}
				}
				
				// Carga filtros de los parametros de objetos imponibles 
				// Metodo auxiliar para loguear todo el request					
				DefinitionUtil.requestValues(request);
			
				Map<String,String> mapFiltros = DefinitionUtil.requestToHashMap(request);
				
				for(TipObjImpAtrDefinition itemDefinition: selAlmAgregarParametrosSearchPageVO.getTipObjImpDefinition().getListTipObjImpAtrDefinition()){
					itemDefinition.populateAtrVal4Busqueda(mapFiltros);
				}
				
				selAlmAgregarParametrosSearchPageVO.getTipObjImpDefinition().clearError();
				selAlmAgregarParametrosSearchPageVO.getTipObjImpDefinition().validate();
				
				// pasaje de errores desde el TipObjImpDefinition al DeudaIncProMasAgregarSearchPageVO 
				selAlmAgregarParametrosSearchPageVO.getTipObjImpDefinition().addErrorMessages(selAlmAgregarParametrosSearchPageVO);
				
				// Setea el PageNumber del PageModel				
				selAlmAgregarParametrosSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));

				// Tiene errores recuperables
				if (selAlmAgregarParametrosSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + selAlmAgregarParametrosSearchPageVO.infoString()); 
					saveDemodaErrors(request, selAlmAgregarParametrosSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, SelAlmAgregarParametrosSearchPage.NAME, selAlmAgregarParametrosSearchPageVO);
				}
				
				mapFiltros = this.cleanHashMap(mapFiltros); 
				// cargo los parametros del objeto imponible al Map del SearchPage
				selAlmAgregarParametrosSearchPageVO.setParamObjImp(mapFiltros);

				// llamada al servicio
				selAlmAgregarParametrosSearchPageVO = GdeServiceLocator.getSelAlmServiceHbmImpl().cargarParametrosSelAlmDeuda(userSession, selAlmAgregarParametrosSearchPageVO);
				
	            // Tiene errores recuperables
				if (selAlmAgregarParametrosSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + selAlmAgregarParametrosSearchPageVO.infoString()); 
					saveDemodaErrors(request, selAlmAgregarParametrosSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, SelAlmAgregarParametrosSearchPage.NAME, selAlmAgregarParametrosSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (selAlmAgregarParametrosSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + selAlmAgregarParametrosSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, SelAlmAgregarParametrosSearchPage.NAME, selAlmAgregarParametrosSearchPageVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, SelAlmAgregarParametrosSearchPage.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, SelAlmAgregarParametrosSearchPage.NAME);
			}
		}
	
	/**
	 * Excluye elementos del mapa que no son necesarios para la busqueda
	 * @param mapaSucio
	 * @return Map<String,String>
	 */
	private Map<String,String> cleanHashMap(Map<String,String> mapaSucio ){
		String funcName = "requestValues"; 
		log.debug(funcName + ": enter ---------------------------------------------------------");

		String[] excluidos = {"dynaExcencion","dynaAtributo", "anonimo", "urlReComenzar", "isSubmittedForm", 
				"pageNumber", "pageMethod", "method", "cantidadMinimaDeudaView", "fechaVencimientoDesdeView",
				"fechaVencimientoHastaView", "cuenta.numeroCuenta", "aplicaAlTotalDeuda.id"};

		// para que quede ordenado por clave
		TreeMap<String, String> hm = new TreeMap<String, String>();

		for (String clave : mapaSucio.keySet()) {
			String valor = mapaSucio.get(clave);

			boolean esExcluido = false;

			for (int i = 0; i < excluidos.length && !esExcluido; i++) {
				if (clave.startsWith(excluidos[i])){
					esExcluido = true;
				}
			}
			if(!esExcluido){
				if(!StringUtil.isNullOrEmpty(valor) && !valor.trim().equals("-1")){
					hm.put(clave, valor);
				}
			}
		}
		log.debug(funcName + ": exit ---------------------------------------------------------");
		return hm;
	}
	
	public ActionForward paramTipoSelAlmDet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_SEL_ALM, 
					GdeSecurityConstants.MTD_AGREGAR_PARAMETROS); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el SearchPage del userSession
				SelAlmAgregarParametrosSearchPage selAlmAgregarParametrosSearchPageVO = (SelAlmAgregarParametrosSearchPage) userSession.get(SelAlmAgregarParametrosSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (selAlmAgregarParametrosSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + SelAlmAgregarParametrosSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, SelAlmAgregarParametrosSearchPage.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(selAlmAgregarParametrosSearchPageVO, request);
				
				selAlmAgregarParametrosSearchPageVO.setListIdRecClaDeu(request.getParameterValues("listIdRecClaDeu"));
				
				// limpio las exenciones seleccionadas previamente, para cargar la nueva seleccion. 
				selAlmAgregarParametrosSearchPageVO.clearExencionesSeleccionadas();
				// carga los valores de las selecciones de excenciones en el mapa exencionesSeleccionadas
				// con clave igual al id de la excencion y con valor seleccionado (0 o 1), descartando el Todos
				for (ExencionVO exencionVO : selAlmAgregarParametrosSearchPageVO.getListExencion()) {
					String seleccion = request.getParameter("dynaExcencion("+exencionVO.getIdView()+")");
					if (!StringUtil.isNullOrEmpty(seleccion) && 
							!seleccion.equals(StringUtil.formatInteger(SiNo.OpcionTodo.getId()))  ){
						selAlmAgregarParametrosSearchPageVO.getExencionesSeleccionadas().put(exencionVO.getIdView(), seleccion);
					}
				}

				// limpio los atributos seleccionadas previamente, para cargar la nueva seleccion. 
				selAlmAgregarParametrosSearchPageVO.clearAtributosSeleccionados();
				// carga los valores de las selecciones de atributos en el mapa atributosSeleccionados
				// con clave igual al id del atributo y con valor seleccionado (0 o 1), descartando el Todos
				for (AtributoVO atributoVO : selAlmAgregarParametrosSearchPageVO.getListAtributo()) {
					String seleccion = request.getParameter("dynaAtributo("+atributoVO.getIdView()+")");
					if (!StringUtil.isNullOrEmpty(seleccion) && 
							!seleccion.equals(StringUtil.formatInteger(SiNo.OpcionTodo.getId()))  ){
						selAlmAgregarParametrosSearchPageVO.getAtributosSeleccionados().put(atributoVO.getIdView(), seleccion);
					}
				}
				
				// Carga filtros de los parametros de objetos imponibles 
				// Metodo auxiliar para loguear todo el request					
				DefinitionUtil.requestValues(request);
			
				Map<String,String> mapFiltros = DefinitionUtil.requestToHashMap(request);
				
				for(TipObjImpAtrDefinition itemDefinition: selAlmAgregarParametrosSearchPageVO.getTipObjImpDefinition().getListTipObjImpAtrDefinition()){
					itemDefinition.populateAtrVal4Busqueda(mapFiltros);
				}
				
				selAlmAgregarParametrosSearchPageVO.getTipObjImpDefinition().clearError();
				selAlmAgregarParametrosSearchPageVO.getTipObjImpDefinition().validate();
				
				// pasaje de errores desde el TipObjImpDefinition al DeudaIncProMasAgregarSearchPageVO 
				selAlmAgregarParametrosSearchPageVO.getTipObjImpDefinition().addErrorMessages(selAlmAgregarParametrosSearchPageVO);
				
				// Setea el PageNumber del PageModel				
				selAlmAgregarParametrosSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));

				// Tiene errores recuperables
				if (selAlmAgregarParametrosSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + selAlmAgregarParametrosSearchPageVO.infoString()); 
					saveDemodaErrors(request, selAlmAgregarParametrosSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, SelAlmAgregarParametrosSearchPage.NAME, selAlmAgregarParametrosSearchPageVO);
				}
				
				mapFiltros = this.cleanHashMap(mapFiltros); 
				// cargo los parametros del objeto imponible al Map del SearchPage
				selAlmAgregarParametrosSearchPageVO.setParamObjImp(mapFiltros);

				// llamada al servicio
				selAlmAgregarParametrosSearchPageVO = GdeServiceLocator.getSelAlmServiceHbmImpl().getSelAlmDeudaAgregarSearchPageParam(userSession, selAlmAgregarParametrosSearchPageVO);
				
	            // Tiene errores recuperables
				if (selAlmAgregarParametrosSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + selAlmAgregarParametrosSearchPageVO.infoString()); 
					saveDemodaErrors(request, selAlmAgregarParametrosSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, SelAlmAgregarParametrosSearchPage.NAME, selAlmAgregarParametrosSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (selAlmAgregarParametrosSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + selAlmAgregarParametrosSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, SelAlmAgregarParametrosSearchPage.NAME, selAlmAgregarParametrosSearchPageVO);
				}
				
				userSession.put(SelAlmAgregarParametrosSearchPage.NAME, selAlmAgregarParametrosSearchPageVO);
				
				request.setAttribute(SelAlmAgregarParametrosSearchPage.NAME, selAlmAgregarParametrosSearchPageVO);
				
				return mapping.findForward(GdeConstants.FWD_SEL_ALM_AGREGAR_PARAMETROS_SEARCHPAGE);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, SelAlmAgregarParametrosSearchPage.NAME);
			}
		}


	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, SelAlmAgregarParametrosSearchPage.NAME);
		}


	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			return baseVolver(mapping, form, request, response, SelAlmAgregarParametrosSearchPage.NAME);
		}
	
	
	public ActionForward buscarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el SearchPage del userSession
		SelAlmAgregarParametrosSearchPage selAlmAgregarParametrosSearchPageVO = (SelAlmAgregarParametrosSearchPage) userSession.get(SelAlmAgregarParametrosSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (selAlmAgregarParametrosSearchPageVO == null) {
			log.error("error en: "  + funcName + ": " + SelAlmAgregarParametrosSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, SelAlmAgregarParametrosSearchPage.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(selAlmAgregarParametrosSearchPageVO, request);
		
		return forwardSeleccionar(mapping, request,
				GdeConstants.METHOD_SEL_ALM_PARAMETROS_PARAM_PERSONA,
				PadConstants.ACTION_BUSCAR_PERSONA, false);
	}
		
	public ActionForward paramPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			SelAlmAgregarParametrosSearchPage selAlmAgregarParametrosSearchPageVO = null;
			
			try {
				
				// Bajo el SearchPage del userSession
				selAlmAgregarParametrosSearchPageVO = (SelAlmAgregarParametrosSearchPage) userSession.get(SelAlmAgregarParametrosSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (selAlmAgregarParametrosSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + SelAlmAgregarParametrosSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, SelAlmAgregarParametrosSearchPage.NAME); 
				}
				
				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(SelAlmAgregarParametrosSearchPage.NAME, selAlmAgregarParametrosSearchPageVO);
					return mapping.findForward(GdeConstants.FWD_SEL_ALM_AGREGAR_PARAMETROS_SEARCHPAGE); 
				}
				
				// Seteo el id contribuyente seleccionado: no se si es contribuyente o persona
				selAlmAgregarParametrosSearchPageVO.getPersona().setId(new Long(selectedId));
				
				// llamo al param del servicio
				selAlmAgregarParametrosSearchPageVO = GdeServiceLocator.getSelAlmServiceHbmImpl().paramPersona(userSession, selAlmAgregarParametrosSearchPageVO);

	            // Tiene errores recuperables
				if (selAlmAgregarParametrosSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + selAlmAgregarParametrosSearchPageVO.infoString()); 
					saveDemodaErrors(request, selAlmAgregarParametrosSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							SelAlmAgregarParametrosSearchPage.NAME, selAlmAgregarParametrosSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (selAlmAgregarParametrosSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + selAlmAgregarParametrosSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							SelAlmAgregarParametrosSearchPage.NAME, selAlmAgregarParametrosSearchPageVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, selAlmAgregarParametrosSearchPageVO);
				
				// Envio el VO al request
				request.setAttribute(SelAlmAgregarParametrosSearchPage.NAME, selAlmAgregarParametrosSearchPageVO);
				userSession.put(SelAlmAgregarParametrosSearchPage.NAME, selAlmAgregarParametrosSearchPageVO);
								
				userSession.getNavModel().setAct(BaseConstants.ACT_INICIALIZAR);
				userSession.getNavModel().setPrevAction(selAlmAgregarParametrosSearchPageVO.getPrevAction());
				userSession.getNavModel().setPrevActionParameter(selAlmAgregarParametrosSearchPageVO.getPrevActionParameter());
				
				return mapping.findForward(GdeConstants.FWD_SEL_ALM_AGREGAR_PARAMETROS_SEARCHPAGE);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, SelAlmAgregarParametrosSearchPage.NAME);
			}
		}

	
	
	
}
