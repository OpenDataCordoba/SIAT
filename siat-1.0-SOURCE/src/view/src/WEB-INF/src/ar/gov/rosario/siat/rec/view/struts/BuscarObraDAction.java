//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.rec.iface.model.ObraAdapter;
import ar.gov.rosario.siat.rec.iface.model.ObraSearchPage;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarObraDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarObraDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_OBRA, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ObraSearchPage obraSearchPageVO = RecServiceLocator.getCdmService().getObraSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (obraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraSearchPageVO.infoString()); 
				saveDemodaErrors(request, obraSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObraSearchPage.NAME, obraSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (obraSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + obraSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObraSearchPage.NAME, obraSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ObraSearchPage.NAME, obraSearchPageVO);
			
			return mapping.findForward(RecConstants.FWD_OBRA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObraSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ObraSearchPage.NAME);
		
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
			ObraSearchPage obraSearchPageVO = (ObraSearchPage) userSession.get(ObraSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (obraSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ObraSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObraSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(obraSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				obraSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (obraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraSearchPageVO.infoString()); 
				saveDemodaErrors(request, obraSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObraSearchPage.NAME, obraSearchPageVO);
			}
				
			// Llamada al servicio	
			obraSearchPageVO = RecServiceLocator.getCdmService().getObraSearchPageResult(userSession, obraSearchPageVO);			

			// Tiene errores recuperables
			if (obraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraSearchPageVO.infoString()); 
				saveDemodaErrors(request, obraSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObraSearchPage.NAME, obraSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (obraSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + obraSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ObraSearchPage.NAME, obraSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ObraSearchPage.NAME, obraSearchPageVO);
			// Nuleo el list result
			//obraSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ObraSearchPage.NAME, obraSearchPageVO);
			
			return mapping.findForward(RecConstants.FWD_OBRA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObraSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_OBRA);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_ENC_OBRA);
		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_OBRA);
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_OBRA);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_OBRA);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_OBRA);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ObraSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ObraSearchPage.NAME);
		
	}
	
	public ActionForward cambiarEstado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();

		return baseForwardSearchPage(mapping, request, funcName, 
			RecConstants.ACTION_ADMINISTRAR_OBRA, RecSecurityConstants.MTD_OBRA_CAMBIAR_ESTADO);		
	}

	public ActionForward imprimirInfObrRep(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			// Permitimos la impresion si y solo si tiene permisos para Ver
			UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_OBRA, 
														BaseSecurityConstants.VER);

			if (userSession==null) return forwardErrorSession(request);
			
			// Obtenemos el NavModel
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				// Obtenemos la Obra seleccionada
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				// Obtenemos el Adapter
				ObraAdapter obraAdapterVO = RecServiceLocator.getCdmService().getObraAdapterForView(userSession, commonKey);
				
				// Si es nulo no se puede continuar
				if (obraAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + 
						" IS NULL. No se pudo obtener de la sesion");
					return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
 				}
				
				obraAdapterVO.prepareInfObrReport(1L);
				
				// Llamada al servicio que genera el reporte
				obraAdapterVO = RecServiceLocator.getCdmService().imprimirInfObrRep(userSession, obraAdapterVO);

				// Limpiamos la lista de reports y la lista de tablas
				obraAdapterVO.getReport().getListReport().clear();
				obraAdapterVO.getReport().getReportListTable().clear();
				
				// Tiene errores no recuperables
				if (obraAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + obraAdapterVO.errorString());
					return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				}
				
				// Obtenemos el nombre del archivo seleccionado
				String fileName = obraAdapterVO.getInfObrReport().getReportFileName();

				// Enviamos el archivo
				baseResponseFile(response,fileName);

				log.debug("exit: " + funcName);
				return null;

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ObraSearchPage.NAME);
			}
		}
	
}
