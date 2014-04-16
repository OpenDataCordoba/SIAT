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
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.DeudaProMasPlanillasDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlaEnvDeuProSearchPage;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarPlaEnvDeuProDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarPlaEnvDeuProDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			PlaEnvDeuProSearchPage plaEnvDeuProSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getPlaEnvDeuProSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (plaEnvDeuProSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaEnvDeuProSearchPageVO.infoString()); 
				saveDemodaErrors(request, plaEnvDeuProSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaEnvDeuProSearchPage.NAME, plaEnvDeuProSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (plaEnvDeuProSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + plaEnvDeuProSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaEnvDeuProSearchPage.NAME, plaEnvDeuProSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , PlaEnvDeuProSearchPage.NAME, plaEnvDeuProSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_PLAENVDEUPRO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaEnvDeuProSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, PlaEnvDeuProSearchPage.NAME);
		
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
			PlaEnvDeuProSearchPage plaEnvDeuProSearchPageVO = (PlaEnvDeuProSearchPage) userSession.get(PlaEnvDeuProSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (plaEnvDeuProSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + PlaEnvDeuProSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaEnvDeuProSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(plaEnvDeuProSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				plaEnvDeuProSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (plaEnvDeuProSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaEnvDeuProSearchPageVO.infoString()); 
				saveDemodaErrors(request, plaEnvDeuProSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaEnvDeuProSearchPage.NAME, plaEnvDeuProSearchPageVO);
			}
				
			// Llamada al servicio	
			plaEnvDeuProSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getPlaEnvDeuProSearchPageResult(userSession, plaEnvDeuProSearchPageVO);			

			// Tiene errores recuperables
			if (plaEnvDeuProSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaEnvDeuProSearchPageVO.infoString()); 
				saveDemodaErrors(request, plaEnvDeuProSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaEnvDeuProSearchPage.NAME, plaEnvDeuProSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (plaEnvDeuProSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + plaEnvDeuProSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaEnvDeuProSearchPage.NAME, plaEnvDeuProSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(PlaEnvDeuProSearchPage.NAME, plaEnvDeuProSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(PlaEnvDeuProSearchPage.NAME, plaEnvDeuProSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_PLAENVDEUPRO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaEnvDeuProSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLAENVDEUPRO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		//Se utiliza uno de los dos return, segun sea un encabezado detalle o no.
		return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLAENVDEUPRO);
		//return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_ENC_PLAENVDEUPRO);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLAENVDEUPRO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLAENVDEUPRO);

	}
	
	public ActionForward habilitarPlanilla(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return baseForward(mapping, request, funcName, BaseConstants.ACT_BUSCAR, GdeConstants.ACTION_ADMINISTRAR_PLAENVDEUPRO, GdeConstants.ACTION_ADM_PLAENVDEUPRO_HABILITAR_PLANILLA);			
	}
	
	public ActionForward recomponerPlanilla(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return  baseForward(mapping, request, funcName, BaseConstants.ACT_BUSCAR, GdeConstants.ACTION_ADMINISTRAR_PLAENVDEUPRO, GdeConstants.ACTION_ADM_PLAENVDEUPRO_RECOMPONER_PLANILLA);
	}
	
	public ActionForward imprimirPadron(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return  baseForward(mapping, request, funcName, BaseConstants.ACT_BUSCAR, GdeConstants.ACTION_ADMINISTRAR_PLAENVDEUPRO, GdeConstants.ACTION_ADM_PLAENVDEUPRO_IMP_PAD);
		}

	public ActionForward imprimirConstanciasPlanilla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return  baseForward(mapping, request, funcName, BaseConstants.ACT_BUSCAR, GdeConstants.ACTION_ADMINISTRAR_PLAENVDEUPRO, GdeConstants.ACTION_ADM_PLAENVDEUPRO_IMP_CON);
	}
	
	public ActionForward imprimirArchivoCD(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return  baseForward(mapping, request, funcName, BaseConstants.ACT_BUSCAR, GdeConstants.ACTION_ADMINISTRAR_PLAENVDEUPRO, GdeConstants.ACTION_ADM_PLAENVDEUPRO_GENERAR_ARCHIVO);
	}
	
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, PlaEnvDeuProSearchPage.NAME);
		
	}

	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				// Bajo el searchPage del userSession
				PlaEnvDeuProSearchPage plaEnvDeuProSearchPageVO = (PlaEnvDeuProSearchPage) userSession.get(PlaEnvDeuProSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (plaEnvDeuProSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + PlaEnvDeuProSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PlaEnvDeuProSearchPage.NAME); 
				}
								
	            // Tiene errores recuperables
				if (plaEnvDeuProSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + plaEnvDeuProSearchPageVO.infoString()); 
					saveDemodaErrors(request, plaEnvDeuProSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlaEnvDeuProSearchPage.NAME, plaEnvDeuProSearchPageVO);
				}
					
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(plaEnvDeuProSearchPageVO, request);
				
				// Llamada al servicio	
				plaEnvDeuProSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getPlaEnvDeuProSearchPageParamProcuradorByRecurso(userSession, plaEnvDeuProSearchPageVO);			
				
				//Esto es para que no muestre la tabla de resultados
				plaEnvDeuProSearchPageVO.setPageNumber(0L);
				
				// Tiene errores recuperables
				if (plaEnvDeuProSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + plaEnvDeuProSearchPageVO.infoString()); 
					saveDemodaErrors(request, plaEnvDeuProSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlaEnvDeuProSearchPage.NAME, plaEnvDeuProSearchPageVO);
				}
				
				// Tiene errores no recuperabConstanciaDeuSearchPageles
				if (plaEnvDeuProSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + plaEnvDeuProSearchPageVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, PlaEnvDeuProSearchPage.NAME, plaEnvDeuProSearchPageVO);
				}
			
				// Envio el VO al request
				request.setAttribute(PlaEnvDeuProSearchPage.NAME, plaEnvDeuProSearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(PlaEnvDeuProSearchPage.NAME, plaEnvDeuProSearchPageVO);
				
				return mapping.findForward(GdeConstants.FWD_PLAENVDEUPRO_SEARCHPAGE);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PlaEnvDeuProSearchPage.NAME);
			}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlaEnvDeuProSearchPage.NAME);
		
	}
		
	public ActionForward verArchivosProcurador(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, act); 
		if (userSession==null) return forwardErrorSession(request);

		try {

			ProcesoMasivoAdapter procesoMasivoAdapter = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getArchivosProcuradorForView(userSession);			

			// Tiene errores recuperables
			if (procesoMasivoAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoAdapter.infoString()); 
				saveDemodaErrors(request, procesoMasivoAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoMasivoAdapter.NAME, procesoMasivoAdapter);
			}

			// Tiene errores no recuperables
			if (procesoMasivoAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoMasivoAdapter.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoMasivoAdapter.NAME, procesoMasivoAdapter);
			}

			// Envio el VO al request
			request.setAttribute(ProcesoMasivoAdapter.NAME, procesoMasivoAdapter);
			// Subo en el el searchPage al userSession
			userSession.put(ProcesoMasivoAdapter.NAME, procesoMasivoAdapter);

			return mapping.findForward(GdeConstants.FWD_VER_ARCHIVOS_PROCURADOR);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaEnvDeuProSearchPage.NAME);
		}
	}
	
	public ActionForward verArchivosEnvioRealizado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

 		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {			
			// obtenemos el nombre del archivo seleccionado
			String fileName = request.getParameter("fileParam");
			baseResponseFile(response,fileName);
			
			return null;			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaProMasPlanillasDeudaAdapter.NAME);
		}
	}

	
}
