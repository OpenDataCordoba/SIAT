//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.ConsultaNodoSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.RangoFechaVO;

public final class ConsultaNodoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(ConsultaNodoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.CONSULTAR_TOTAL_NODO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ConsultaNodoSearchPage consultaNodoSearchPageVO = BalServiceLocator.getClasificacionService().getConsultaNodoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (consultaNodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + consultaNodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, consultaNodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (consultaNodoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + consultaNodoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_CONSULTANODO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConsultaNodoSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ConsultaNodoSearchPage.NAME);
		
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
			ConsultaNodoSearchPage consultaNodoSearchPageVO = (ConsultaNodoSearchPage) userSession.get(ConsultaNodoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (consultaNodoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ConsultaNodoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConsultaNodoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(consultaNodoSearchPageVO, request);
				
				// Popula la lista de codigos que forman la clave del nodo
				for(CeldaVO codNivel: consultaNodoSearchPageVO.getListCodNivel()){
					String codigo = request.getParameter("codNivel"+codNivel.getNombreColumna());
					codNivel.setValor(codigo);								
				}
				
				// Si estan habilitados los rangos de fechas adicionales
				if("on".equals(request.getParameter("rangosFechaExtras"))){
					consultaNodoSearchPageVO.setRangosFechaExtras(true);
					
					// Popula los rangos adicionales de fechas
					for(RangoFechaVO rangoFecha: consultaNodoSearchPageVO.getListRangosFecha()){
						String fechaDesdeView = request.getParameter("fechaDesde"+rangoFecha.getIndice());
						String fechaHastaView = request.getParameter("fechaHasta"+rangoFecha.getIndice());
						rangoFecha.setFechaDesdeView(fechaDesdeView);
						rangoFecha.setFechaHastaView(fechaHastaView);
					}
				}
				
				// Setea el PageNumber del PageModel				
				consultaNodoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (consultaNodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + consultaNodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, consultaNodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}
				
			// Llamada al servicio	
			consultaNodoSearchPageVO = BalServiceLocator.getClasificacionService().getConsultaNodoSearchPageResult(userSession, consultaNodoSearchPageVO);			

			// Tiene errores recuperables
			if (consultaNodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + consultaNodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, consultaNodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (consultaNodoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + consultaNodoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			
			// Subo en el el searchPage al userSession
			userSession.put(ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_CONSULTANODO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConsultaNodoSearchPage.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ConsultaNodoSearchPage.NAME);
		
	}
	
	
	public ActionForward paramClasificador(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
			
		try {
			//bajo el report del usserSession
			ConsultaNodoSearchPage consultaNodoSearchPageVO =  (ConsultaNodoSearchPage) userSession.get(ConsultaNodoSearchPage.NAME);
				
			// Si es nulo no se puede continuar
			if (consultaNodoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ConsultaNodoSearchPage.NAME + " " +
						  "IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConsultaNodoSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(consultaNodoSearchPageVO, request);
				
			// Si estan habilitados los rangos de fechas adicionales
			if("on".equals(request.getParameter("rangosFechaExtras"))){
				consultaNodoSearchPageVO.setRangosFechaExtras(true);
				
				// Popula los rangos adicionales de fechas
				for(RangoFechaVO rangoFecha: consultaNodoSearchPageVO.getListRangosFecha()){
					String fechaDesdeView = request.getParameter("fechaDesde"+rangoFecha.getIndice());
					String fechaHastaView = request.getParameter("fechaHasta"+rangoFecha.getIndice());
					rangoFecha.setFechaDesdeView(fechaDesdeView);
					rangoFecha.setFechaHastaView(fechaHastaView);
				}
			}

			// Tiene errores recuperables
			if (consultaNodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + consultaNodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, consultaNodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}

			consultaNodoSearchPageVO = BalServiceLocator.getClasificacionService().getConsultaNodoSearchPageParamClasificador(userSession, consultaNodoSearchPageVO);
				
			// Tiene errores recuperables
			if (consultaNodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + consultaNodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, consultaNodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}
				
			// Tiene errores no recuperables
			if (consultaNodoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + consultaNodoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}
				
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, consultaNodoSearchPageVO);
				
			// Envio el VO al request
			request.setAttribute(ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);

			return mapping.findForward(BalConstants.FWD_CONSULTANODO_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConsultaNodoSearchPage.NAME);
		}
	}

	public ActionForward paramNivel(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
			
		try {
			//bajo el report del usserSession
			ConsultaNodoSearchPage consultaNodoSearchPageVO =  (ConsultaNodoSearchPage) userSession.get(ConsultaNodoSearchPage.NAME);
				
			// Si es nulo no se puede continuar
			if (consultaNodoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ConsultaNodoSearchPage.NAME + " " +
						  "IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConsultaNodoSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(consultaNodoSearchPageVO, request);
		
			// Si estan habilitados los rangos de fechas adicionales
			if("on".equals(request.getParameter("rangosFechaExtras"))){
				consultaNodoSearchPageVO.setRangosFechaExtras(true);
				
				// Popula los rangos adicionales de fechas
				for(RangoFechaVO rangoFecha: consultaNodoSearchPageVO.getListRangosFecha()){
					String fechaDesdeView = request.getParameter("fechaDesde"+rangoFecha.getIndice());
					String fechaHastaView = request.getParameter("fechaHasta"+rangoFecha.getIndice());
					rangoFecha.setFechaDesdeView(fechaDesdeView);
					rangoFecha.setFechaHastaView(fechaHastaView);
				}
			}

			// Tiene errores recuperables
			if (consultaNodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + consultaNodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, consultaNodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}

			consultaNodoSearchPageVO = BalServiceLocator.getClasificacionService().getConsultaNodoSearchPageParamNivel(userSession, consultaNodoSearchPageVO);
				
			// Tiene errores recuperables
			if (consultaNodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + consultaNodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, consultaNodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
											   ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}
				
			// Tiene errores no recuperables
			if (consultaNodoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + consultaNodoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
												  ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}
				
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, consultaNodoSearchPageVO);
				
			// Envio el VO al request
			request.setAttribute(ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);

			return mapping.findForward(BalConstants.FWD_CONSULTANODO_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConsultaNodoSearchPage.NAME);
		}
	}
	
	public ActionForward validarNodo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)	throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConsultaNodoSearchPage consultaNodoSearchPageVO = (ConsultaNodoSearchPage) userSession.get(ConsultaNodoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (consultaNodoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ConsultaNodoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConsultaNodoSearchPage.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(consultaNodoSearchPageVO, request);
			
			// Popula la lista de codigos que forman la clave del nodo
			for(CeldaVO codNivel: consultaNodoSearchPageVO.getListCodNivel()){
				String codigo = request.getParameter("codNivel"+codNivel.getNombreColumna());
				codNivel.setValor(codigo);								
			}
			
			// Si estan habilitados los rangos de fechas adicionales
			if("on".equals(request.getParameter("rangosFechaExtras"))){
				consultaNodoSearchPageVO.setRangosFechaExtras(true);
				
				// Popula los rangos adicionales de fechas
				for(RangoFechaVO rangoFecha: consultaNodoSearchPageVO.getListRangosFecha()){
					String fechaDesdeView = request.getParameter("fechaDesde"+rangoFecha.getIndice());
					String fechaHastaView = request.getParameter("fechaHasta"+rangoFecha.getIndice());
					rangoFecha.setFechaDesdeView(fechaDesdeView);
					rangoFecha.setFechaHastaView(fechaHastaView);
				}
			}

			// Tiene errores recuperables
			if (consultaNodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + consultaNodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, consultaNodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}
			
			// llamada al servicio
			consultaNodoSearchPageVO = BalServiceLocator.getClasificacionService().getConsultaNodoSearchPageValidarNodo(userSession, consultaNodoSearchPageVO); 
		    
			// Tiene errores recuperables
			if (consultaNodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + consultaNodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, consultaNodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}
				
			// Tiene errores no recuperables
			if (consultaNodoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + consultaNodoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, consultaNodoSearchPageVO);
				
			// Envio el VO al request
			request.setAttribute(ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
		
			return mapping.findForward( BalConstants.FWD_CONSULTANODO_SEARCHPAGE); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConsultaNodoSearchPage.NAME);
		}	
	}
	
	public ActionForward downloadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);

			try {
				
				// obtenemos el nombre del archivo seleccionado
				String fileName = request.getParameter("fileParam");
				
				baseResponseFile(response,fileName);

				log.debug("exit: " + funcName);
				
				return null;
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConsultaNodoSearchPage.NAME);
			}
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ConsultaNodoSearchPage.NAME);
	}
	
	public ActionForward paramRango(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
			
		try {
			//bajo el report del usserSession
			ConsultaNodoSearchPage consultaNodoSearchPageVO =  (ConsultaNodoSearchPage) userSession.get(ConsultaNodoSearchPage.NAME);
				
			// Si es nulo no se puede continuar
			if (consultaNodoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ConsultaNodoSearchPage.NAME + " " +
						  "IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConsultaNodoSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(consultaNodoSearchPageVO, request);
		
			// Si estan habilitados los rangos de fechas adicionales
			if("on".equals(request.getParameter("rangosFechaExtras"))){
				consultaNodoSearchPageVO.setRangosFechaExtras(true);
				
				// Popula los rangos adicionales de fechas
				for(RangoFechaVO rangoFecha: consultaNodoSearchPageVO.getListRangosFecha()){
					String fechaDesdeView = request.getParameter("fechaDesde"+rangoFecha.getIndice());
					String fechaHastaView = request.getParameter("fechaHasta"+rangoFecha.getIndice());
					rangoFecha.setFechaDesdeView(fechaDesdeView);
					rangoFecha.setFechaHastaView(fechaHastaView);
				}
			}

			// Tiene errores recuperables
			if (consultaNodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + consultaNodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, consultaNodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}

			consultaNodoSearchPageVO = BalServiceLocator.getClasificacionService().getConsultaNodoSearchPageParamRango(userSession, consultaNodoSearchPageVO);
				
			// Tiene errores recuperables
			if (consultaNodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + consultaNodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, consultaNodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
											   ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}
				
			// Tiene errores no recuperables
			if (consultaNodoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + consultaNodoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
												  ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}
				
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, consultaNodoSearchPageVO);
				
			// Envio el VO al request
			request.setAttribute(ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);

			return mapping.findForward(BalConstants.FWD_CONSULTANODO_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConsultaNodoSearchPage.NAME);
		}
	}
	
	public ActionForward paramEspecial(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
			
		try {
			//bajo el report del usserSession
			ConsultaNodoSearchPage consultaNodoSearchPageVO =  (ConsultaNodoSearchPage) userSession.get(ConsultaNodoSearchPage.NAME);
				
			// Si es nulo no se puede continuar
			if (consultaNodoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ConsultaNodoSearchPage.NAME + " " +
						  "IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConsultaNodoSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(consultaNodoSearchPageVO, request);
				
			// Si estan habilitados los rangos de fechas adicionales
			if("on".equals(request.getParameter("rangosFechaExtras"))){
				consultaNodoSearchPageVO.setRangosFechaExtras(true);
				
				// Popula los rangos adicionales de fechas
				for(RangoFechaVO rangoFecha: consultaNodoSearchPageVO.getListRangosFecha()){
					String fechaDesdeView = request.getParameter("fechaDesde"+rangoFecha.getIndice());
					String fechaHastaView = request.getParameter("fechaHasta"+rangoFecha.getIndice());
					rangoFecha.setFechaDesdeView(fechaDesdeView);
					rangoFecha.setFechaHastaView(fechaHastaView);
				}
			}
			
			// Si estan habilitados los rangos de fechas adicionales
			if("on".equals(request.getParameter("reporteEspecial"))){
				consultaNodoSearchPageVO.setReporteEspecial(true);
			}else{
				consultaNodoSearchPageVO.setReporteEspecial(false);
			}

			// Tiene errores recuperables
			if (consultaNodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + consultaNodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, consultaNodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}
			
			consultaNodoSearchPageVO = BalServiceLocator.getClasificacionService().getConsultaNodoSearchPageParamEspecial(userSession, consultaNodoSearchPageVO);
			
			// Tiene errores recuperables
			if (consultaNodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + consultaNodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, consultaNodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (consultaNodoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + consultaNodoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);
			}
	
				
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, consultaNodoSearchPageVO);
				
			// Envio el VO al request
			request.setAttribute(ConsultaNodoSearchPage.NAME, consultaNodoSearchPageVO);

			return mapping.findForward(BalConstants.FWD_CONSULTANODO_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConsultaNodoSearchPage.NAME);
		}
	}
}
