//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.model.RecConADecVO;
import ar.gov.rosario.siat.gde.iface.model.DecJurPagVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDecJurAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqRecMinVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarLiqDecJurDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarLiqDecJurDAction.class);
	
	/**
	 * Metodo que se llama desde la liquidacion de la deuda
	 * 
	 */
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);			
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "getLiqDecJurAdapterInit";
		
		try {
			// Recuperamos datos del form en el vo
			log.debug(funcName + " idCuenta: " + request.getParameter("selectedId"));
			
			LiqDecJurAdapter liqDecJurAdapterVO = new LiqDecJurAdapter();
			
			liqDecJurAdapterVO.getCuenta().setIdCuenta(Long.parseLong(request.getParameter("selectedId")));
			
			liqDecJurAdapterVO.setListIdDeudaSelected(request.getParameterValues("listIdDeudaSelected"));
			
			liqDecJurAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getLiqDecJurAdapterInit(userSession, liqDecJurAdapterVO);
			
			// Tiene errores no recuperables
			if (liqDecJurAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDecJurAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());
			
			// Lo subo a la session con o sin error

			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
	        // Tiene errores recuperables
			if (liqDecJurAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqDecJurAdapterVO.infoString()); 
				
				request.setAttribute("selectedId", liqDecJurAdapterVO.getCuenta().getIdCuenta());
									
				return this.volverACuenta(mapping, form, request, response);
				
			} else {
				
				log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
				
				saveDemodaMessages(request, liqDecJurAdapterVO);
				// Envio el VO al request
				request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			}
			
			return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_INIT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	
	public ActionForward agregarActividad(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			DemodaUtil.populateVO(liqDecJurAdapterVO, request);
			
			if(!ModelUtil.isNullOrEmpty(liqDecJurAdapterVO.getActividad())){
				RecConADecVO actividadSelect = null;
				// Buscamos la actividad seleccionada
				for(RecConADecVO actividad:liqDecJurAdapterVO.getListActividad()){
					if (actividad.getId().longValue() == liqDecJurAdapterVO.getActividad().getId().longValue()){
						actividadSelect = actividad;
						break;
					}
				}
				
				liqDecJurAdapterVO.getListActividad().remove(actividadSelect);
				
				liqDecJurAdapterVO.getListActividadDec().add(actividadSelect);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());
			

			saveDemodaMessages(request, liqDecJurAdapterVO);		
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_INIT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	public ActionForward quitarActividad(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			// Recuperamos datos del form en el vo
			log.debug(funcName + " idQuitar: " + request.getParameter("selectedId"));
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			Long idActividadQuitar = new Long(request.getParameter("selectedId"));			
			
			RecConADecVO actividadSelect = null;
			// Buscamos la actividad seleccionada para quitar y la volvemos a agregar a la lista
			for(RecConADecVO actividad:liqDecJurAdapterVO.getListActividadDec()){
				if (actividad.getId().longValue() == idActividadQuitar.longValue()){
					actividadSelect = actividad;
					break;
				}
			}
			
			liqDecJurAdapterVO.getListActividadDec().remove(actividadSelect);
			
			// agregar en el orden correctamente
			liqDecJurAdapterVO.getListActividad().add(actividadSelect);
		
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());
			

			saveDemodaMessages(request, liqDecJurAdapterVO);		
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_INIT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}

	/**
	 * Agrega cantidad de Personal.
	 * 
	 * @return
	 * @throws Exception
	 */
	public ActionForward agregarRecMin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			log.debug(" PeriodoDesde A: " + liqDecJurAdapterVO.getPeriodoDesde().getId());
			log.debug(" PeriodoHasta A: " + liqDecJurAdapterVO.getPeriodoHasta().getId());
						
			log.debug(" reqD: " + request.getParameter("periodoDesde.id"));
			log.debug(" reqH: " + request.getParameter("periodoHasta.id"));
			log.debug(" cant: " + request.getParameter("cantPersonal.valor"));
			
			liqDecJurAdapterVO.setPeriodoDesde(new LiqRecMinVO());
			liqDecJurAdapterVO.setPeriodoHasta(new LiqRecMinVO());
						
			DemodaUtil.populateVO(liqDecJurAdapterVO, request);
			
			if (StringUtil.isNullOrEmpty(liqDecJurAdapterVO.getCantPersonal().getValor())){
				liqDecJurAdapterVO.addRecoverableValueError("La Cantidad de Personal es requerida.");
			} else if (!StringUtil.isInteger(liqDecJurAdapterVO.getCantPersonal().getValor())){
				liqDecJurAdapterVO.addRecoverableValueError("El formato del campo Cantidad es incorrecto."); 
			}

			if (!liqDecJurAdapterVO.hasError()){
				
				LiqRecMinVO cantPerAgregar = new LiqRecMinVO();
				
				cantPerAgregar.setId(new Long(liqDecJurAdapterVO.getListRecMin().size()));

				// Periodo Desde
				for(LiqRecMinVO periodo: liqDecJurAdapterVO.getListPeriodosDesde()){
					if (periodo.getId().longValue() == liqDecJurAdapterVO.getPeriodoDesde().getId().longValue()){
						cantPerAgregar.setPeriodoDesde(periodo.getPeriodo());
						cantPerAgregar.setAnioDesde(periodo.getAnio());
						break;
					}
				}
				
				// Periodo Hasta
				for(LiqRecMinVO periodo: liqDecJurAdapterVO.getListPeriodosHasta()){
					if (periodo.getId().longValue() == liqDecJurAdapterVO.getPeriodoHasta().getId().longValue()){
						cantPerAgregar.setPeriodoHasta(periodo.getPeriodo());
						cantPerAgregar.setAnioHasta(periodo.getAnio());
						break;
					}
				}
				
				// Cantidad
				cantPerAgregar.setValor(liqDecJurAdapterVO.getCantPersonal().getValor());
				cantPerAgregar.setQuitarEnabled(true);
				
				// Se podra quitar secuencialmente
				for(LiqRecMinVO liq:liqDecJurAdapterVO.getListRecMin()){
					liq.setQuitarEnabled(false);
				}
				
				liqDecJurAdapterVO.getListRecMin().add(cantPerAgregar);
	
				liqDecJurAdapterVO.setCantPersonal(new LiqRecMinVO());
				
				// Reload de combos
				liqDecJurAdapterVO.setListPeriodosDesde(new ArrayList<LiqRecMinVO>());

				LiqRecMinVO periodoD;
				for(LiqRecMinVO periodo: liqDecJurAdapterVO.getListPeriodos()){
					if (periodo.getId().longValue() > liqDecJurAdapterVO.getPeriodoHasta().getId().longValue()){
						periodoD = new LiqRecMinVO(periodo.getPeriodo(), periodo.getAnio(), periodo.getId());
						liqDecJurAdapterVO.getListPeriodosDesde().add(periodoD);
						break;
					}
				}
				
				liqDecJurAdapterVO.setListPeriodosHasta(new ArrayList<LiqRecMinVO>());
				LiqRecMinVO periodoH;
				for(LiqRecMinVO periodo: liqDecJurAdapterVO.getListPeriodos()){
					if (periodo.getId().longValue() > liqDecJurAdapterVO.getPeriodoHasta().getId().longValue()){
						periodoH = new LiqRecMinVO(periodo.getPeriodo(), periodo.getAnio(), periodo.getId());
						liqDecJurAdapterVO.getListPeriodosHasta().add(periodoH);
					}
				}

				// Bandera para posicionar dentro de la pantalla
				liqDecJurAdapterVO.setIrPersonal(true);
			} else {
				// Bandera para posicionar dentro de la pantalla
				liqDecJurAdapterVO.setIrPersonal(false);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());

			saveDemodaMessages(request, liqDecJurAdapterVO);
			saveDemodaErrors(request, liqDecJurAdapterVO);
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_INIT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	@Deprecated
	public ActionForward paramRecMin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			DemodaUtil.populateVO(liqDecJurAdapterVO, request);
			
			
			// Cargamos los periodos hasta mayore o iguales al periodo desde seleccionado
			if (!liqDecJurAdapterVO.hasError()){
			
				liqDecJurAdapterVO.setListPeriodosHasta(new ArrayList<LiqRecMinVO>());
				
				LiqRecMinVO periodoH;
				for(LiqRecMinVO periodo: liqDecJurAdapterVO.getListPeriodos()){
					
					if (periodo.getId().longValue() >= liqDecJurAdapterVO.getPeriodoDesde().getId().longValue()){
						
						periodoH = new LiqRecMinVO(periodo.getPeriodo(), periodo.getAnio(), periodo.getId());
						liqDecJurAdapterVO.getListPeriodosHasta().add(periodoH);
					}
				}
			}
			
			// Bandera para posicionar dentro de la pantalla
			liqDecJurAdapterVO.setIrPersonal(true);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());

			saveDemodaMessages(request, liqDecJurAdapterVO);
			saveDemodaErrors(request, liqDecJurAdapterVO);
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_INIT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	public ActionForward quitarRecMin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			// Recuperamos datos del form en el vo
			log.debug(funcName + " idQuitar: " + request.getParameter("selectedId"));
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			Long idRecMinQuitar = new Long(request.getParameter("selectedId"));			
			
			LiqRecMinVO liqRecMinSelect = null;
			// Buscamos la actividad seleccionada para quitar y la volvemos a agregar a la lista
			for(LiqRecMinVO liqRecMinVO:liqDecJurAdapterVO.getListRecMin()){
				if (liqRecMinVO.getId().longValue() == idRecMinQuitar.longValue()){
					liqRecMinSelect = liqRecMinVO;
					break;
				}
			}
			
			Integer perDes, anioDes;
			
			perDes = liqRecMinSelect.getPeriodoDesde();
			anioDes = liqRecMinSelect.getAnioDesde();
				
			liqDecJurAdapterVO.getListRecMin().remove(liqRecMinSelect);
			
			// Se podra quitar secuencialmente
			if(liqDecJurAdapterVO.getListRecMin().size() > 0){
				liqDecJurAdapterVO.getListRecMin().get(liqDecJurAdapterVO.getListRecMin().size() - 1).setQuitarEnabled(true);
			}
			
			// Reload de combos
			liqDecJurAdapterVO.setListPeriodosDesde(new ArrayList<LiqRecMinVO>());

			LiqRecMinVO periodoD = null;
			for(LiqRecMinVO periodo: liqDecJurAdapterVO.getListPeriodos()){
				if (periodo.getPeriodo().intValue() == perDes.intValue() && 
						periodo.getAnio().intValue() == anioDes.intValue()){
					periodoD = new LiqRecMinVO(periodo.getPeriodo(), periodo.getAnio(), periodo.getId());
					liqDecJurAdapterVO.getListPeriodosDesde().add(periodoD);
					break;
				}
			}
			
			liqDecJurAdapterVO.setListPeriodosHasta(new ArrayList<LiqRecMinVO>());
			LiqRecMinVO periodoH;
			for(LiqRecMinVO periodo: liqDecJurAdapterVO.getListPeriodos()){
				if (periodo.getId().longValue() >= periodoD.getId().longValue()){
					periodoH = new LiqRecMinVO(periodo.getPeriodo(), periodo.getAnio(), periodo.getId());
					liqDecJurAdapterVO.getListPeriodosHasta().add(periodoH);
				}
			}
			
			liqDecJurAdapterVO.setPeriodoDesde(periodoD);
			liqDecJurAdapterVO.setPeriodoHasta(periodoD);
			
			// Bandera para posicionar dentro de la pantalla
			liqDecJurAdapterVO.setIrPersonal(true);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());

			saveDemodaMessages(request, liqDecJurAdapterVO);		
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_INIT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	
	public ActionForward irInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_INIT);
						
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	public ActionForward irDetalle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			DemodaUtil.populateVO(liqDecJurAdapterVO, request);
			
			
			// Llamamos al servicio
			liqDecJurAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getLiqDecJurAdapterDetalle(userSession, liqDecJurAdapterVO);
			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());
			

			saveDemodaMessages(request, liqDecJurAdapterVO);
			saveDemodaErrors(request, liqDecJurAdapterVO);
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			if(liqDecJurAdapterVO.hasError()){
				
				liqDecJurAdapterVO.setIrPersonal(false);
				
				return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_INIT);
			} else{
				
				return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_DETALLE);
			}
			
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	
	public ActionForward volverDetalle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_DETALLE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	public ActionForward paramUnidad(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			DemodaUtil.populateVO(liqDecJurAdapterVO, request);
			
			
			this.populateDetalle(liqDecJurAdapterVO, request);
			
			// Realizamos el param sin ir al servicio, desde el mapa
			if (!liqDecJurAdapterVO.hasError()){
				
				liqDecJurAdapterVO.setListTipoUnidad(new ArrayList<RecConADecVO>());
				
				Long idUni = liqDecJurAdapterVO.getUnidad().getId();
				
				// Si existe tipo unidad para la unidad seleccionada
				if (liqDecJurAdapterVO.getMapTipoUnidad().containsKey(idUni)){
					liqDecJurAdapterVO.getListTipoUnidad().addAll(liqDecJurAdapterVO.getMapTipoUnidad().get(idUni));
				} else {
					// Seteamos la opcion seleccionar
					liqDecJurAdapterVO.getListTipoUnidad().addAll(liqDecJurAdapterVO.getMapTipoUnidad().get(-1L));
				}
				
			}
			
			// Bandera para posicionar dentro de la pantalla
			liqDecJurAdapterVO.setIrUnidad(true);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());

			saveDemodaMessages(request, liqDecJurAdapterVO);
			saveDemodaErrors(request, liqDecJurAdapterVO);
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_DETALLE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	
	public ActionForward agregarCantidad(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			DemodaUtil.populateVO(liqDecJurAdapterVO, request);
			
			this.populateDetalle(liqDecJurAdapterVO, request);
			
			liqDecJurAdapterVO.setListIdDetalleSelected(request.getParameterValues("listIdDetalleSelected"));
			
			// Validamos requeridos
			if (liqDecJurAdapterVO.getCantUni() == null){
				liqDecJurAdapterVO.addRecoverableValueError("La cantidad es requerida");
			}
			
			if (ModelUtil.isNullOrEmpty(liqDecJurAdapterVO.getUnidad())){
				liqDecJurAdapterVO.addRecoverableValueError("La Unidad es requerida");
			}
			
			if (ModelUtil.isNullOrEmpty(liqDecJurAdapterVO.getTipoUnidad())){
				liqDecJurAdapterVO.addRecoverableValueError("El Tipo Unidad es requerido");
			}
			
			// Validamos que existan seleccionados registros
			if (liqDecJurAdapterVO.getListIdDetalleSelected() == null || 
					liqDecJurAdapterVO.getListIdDetalleSelected().length == 0){
				
				liqDecJurAdapterVO.addRecoverableValueError("Debe seleccionar periodos para aplicar cantidades");
			} 

			// Realizamos el param sin ir al servicio, desde el mapa
			if (!liqDecJurAdapterVO.hasError()){
					
				log.debug(" Cantidad " + liqDecJurAdapterVO.getCantUni());
				log.debug(" Unidad " + liqDecJurAdapterVO.getUnidad().getIdView());
				log.debug(" Tipo Unidad " + liqDecJurAdapterVO.getTipoUnidad().getIdView());
				
				Integer idSel, periodo, anio;
				Date fecVigCon;
				Double valorUni;
				Integer cantUni = liqDecJurAdapterVO.getCantUni();
				
				for(String idSelected:liqDecJurAdapterVO.getListIdDetalleSelected()){
					
					log.debug(" idSelectd " + idSelected);
					
					idSel = new Integer(idSelected);
					
					liqDecJurAdapterVO.getListDetalle().get(idSel).setCantUni(cantUni);
					liqDecJurAdapterVO.getListDetalle().get(idSel).setUnidad(liqDecJurAdapterVO.obtenerUnidad());
					liqDecJurAdapterVO.getListDetalle().get(idSel).setTipoUnidad(liqDecJurAdapterVO.obtenerTipoUnidad());
				
					periodo = liqDecJurAdapterVO.getListDetalle().get(idSel).getPeriodo();
					anio = liqDecJurAdapterVO.getListDetalle().get(idSel).getAnio();
					
					fecVigCon = DateUtil.getDate("01/"+ StringUtil.completarCerosIzq(periodo.toString(),2)+"/"+ anio,DateUtil.ddSMMSYYYY_MASK);
					
					valorUni = liqDecJurAdapterVO.obtenerTipoUnidad().getValorUnitario(fecVigCon);
					
					liqDecJurAdapterVO.getListDetalle().get(idSel).setValorUni(valorUni);
											
					liqDecJurAdapterVO.getListDetalle().get(idSel).setSubTotal2(cantUni * valorUni);
					
				}
				
				// Reset
				liqDecJurAdapterVO.setListIdDetalleSelected(null);
				
				liqDecJurAdapterVO.setIrUnidad(true);
					
			} else {
				liqDecJurAdapterVO.setIrUnidad(false);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());

			saveDemodaMessages(request, liqDecJurAdapterVO);
			saveDemodaErrors(request, liqDecJurAdapterVO);
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_DETALLE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}

	
	public ActionForward agregarMinimo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			DemodaUtil.populateVO(liqDecJurAdapterVO, request);
			
			this.populateDetalle(liqDecJurAdapterVO, request);
			
			liqDecJurAdapterVO.setListIdDetalleSelected(request.getParameterValues("listIdDetalleSelected"));
			
			// Validamos requeridos
			if (ModelUtil.isNullOrEmpty(liqDecJurAdapterVO.getMinimo())){
				liqDecJurAdapterVO.addRecoverableValueError("El Minimo a aplicar es requerido");
			}
			
			// Validamos que existan seleccionados registros
			if (liqDecJurAdapterVO.getListIdDetalleSelected() == null || 
					liqDecJurAdapterVO.getListIdDetalleSelected().length == 0){
				
				liqDecJurAdapterVO.addRecoverableValueError("Debe seleccionar periodos para aplicar minimos");
			} 

			// Realizamos el param sin ir al servicio, desde el mapa
			if (!liqDecJurAdapterVO.hasError()){
					
				log.debug(" Minimo " + liqDecJurAdapterVO.obtenerMinimo().getValorUnitarioView());
				
				Integer idSel;
				Double valorUni = liqDecJurAdapterVO.obtenerMinimo().getValorUnitario();
				
				for(String idSelected:liqDecJurAdapterVO.getListIdDetalleSelected()){
					
					log.debug(" idSelectd " + idSelected);
					
					idSel = new Integer(idSelected);
					
					liqDecJurAdapterVO.getListDetalle().get(idSel).setValorUni(valorUni);
					liqDecJurAdapterVO.getListDetalle().get(idSel).setSubTotal2(valorUni);
					
				}
				
				// Reset
				liqDecJurAdapterVO.setListIdDetalleSelected(null);
				
				liqDecJurAdapterVO.setIrUnidad(true);
					
			} else {
				liqDecJurAdapterVO.setIrUnidad(false);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());

			saveDemodaMessages(request, liqDecJurAdapterVO);
			saveDemodaErrors(request, liqDecJurAdapterVO);
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_DETALLE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	
	public ActionForward agregarAlicuota(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			DemodaUtil.populateVO(liqDecJurAdapterVO, request);
			
			this.populateDetalle(liqDecJurAdapterVO, request);
			
			liqDecJurAdapterVO.setListIdDetalleSelected(request.getParameterValues("listIdDetalleSelected"));
			
			// Validamos requeridos
			if (ModelUtil.isNullOrEmpty(liqDecJurAdapterVO.getAlicuota())){
				liqDecJurAdapterVO.addRecoverableValueError("La alicuota a aplicar es requerida");
			}

			// Validamos que existan seleccionados registros
			if (liqDecJurAdapterVO.getListIdDetalleSelected() == null || 
					liqDecJurAdapterVO.getListIdDetalleSelected().length == 0){
				
				liqDecJurAdapterVO.addRecoverableValueError("Debe seleccionar periodos para aplicar la alicuota");
			} 

			// Realizamos el param sin ir al servicio, desde el mapa
			if (!liqDecJurAdapterVO.hasError()){
					
				Integer idSel;
				Double alicuota = liqDecJurAdapterVO.obtenerAlicuota().getAlicuota();

				log.debug(" Alicuota " + liqDecJurAdapterVO.obtenerAlicuota().getId() + " " + alicuota.toString());
				
				for(String idSelected:liqDecJurAdapterVO.getListIdDetalleSelected()){
					
					log.debug(" idSelectd " + idSelected);
					
					idSel = new Integer(idSelected);
					
					liqDecJurAdapterVO.getListDetalle().get(idSel).setAlicuota(alicuota);
					
					// Seteamos el producto MontoImponible * Alicuota
					if (liqDecJurAdapterVO.getListDetalle().get(idSel).getMontoImponible() != null &&
							liqDecJurAdapterVO.getListDetalle().get(idSel).getAlicuota() != null) {
						
						liqDecJurAdapterVO.getListDetalle().get(idSel).setSubTotal1(liqDecJurAdapterVO.getListDetalle().get(idSel).getMontoImponible() *
								liqDecJurAdapterVO.getListDetalle().get(idSel).getAlicuota());
					}
				}
				
				// Reset
				liqDecJurAdapterVO.setListIdDetalleSelected(null);
				
				liqDecJurAdapterVO.setIrUnidad(true);
					
			} else {
				liqDecJurAdapterVO.setIrUnidad(false);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());

			saveDemodaMessages(request, liqDecJurAdapterVO);
			saveDemodaErrors(request, liqDecJurAdapterVO);
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_DETALLE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	public ActionForward agregarPub(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			DemodaUtil.populateVO(liqDecJurAdapterVO, request);
			
			this.populateGeneral(liqDecJurAdapterVO, request);
			
			liqDecJurAdapterVO.setListIdGeneralSelected(request.getParameterValues("listIdGeneralSelected"));
			
			// Validamos requeridos
			if (ModelUtil.isNullOrEmpty(liqDecJurAdapterVO.getAliPub())){
				liqDecJurAdapterVO.addRecoverableValueError("Al alicuota es requerida");
			}
			
			// Validamos que existan seleccionados registros
			if (liqDecJurAdapterVO.getListIdGeneralSelected() == null || 
					liqDecJurAdapterVO.getListIdGeneralSelected().length == 0){
				
				liqDecJurAdapterVO.addRecoverableValueError("Debe seleccionar periodos para aplicar porcentajes");
			}
			
			// Realizamos el param sin ir al servicio, desde el mapa
			if (!liqDecJurAdapterVO.hasError()){
					
				log.debug(" alicuota " + liqDecJurAdapterVO.obtenerAliPub().getAlicuota());
									
				Integer idSel;
				Double porcentaje = liqDecJurAdapterVO.obtenerAliPub().getAlicuota();
				Double adicPub;
				
				for(String idSelected:liqDecJurAdapterVO.getListIdGeneralSelected()){
					
					log.debug(" idSelectd " + idSelected);
					
					idSel = new Integer(idSelected);
					
					liqDecJurAdapterVO.getListGeneral().get(idSel).setPorcAdicPub(porcentaje);
					
					adicPub = (liqDecJurAdapterVO.getListGeneral().get(idSel).getDeterminado() * porcentaje); 
					
					liqDecJurAdapterVO.getListGeneral().get(idSel).setAdicPub(adicPub);
					
					liqDecJurAdapterVO.getListGeneral().get(idSel).calcularTotal();
				}
				
				// Reset
				liqDecJurAdapterVO.setListIdGeneralSelected(null);
				liqDecJurAdapterVO.setPorcentaje(0D);
				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());

			saveDemodaMessages(request, liqDecJurAdapterVO);
			saveDemodaErrors(request, liqDecJurAdapterVO);
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_GENERAL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	public ActionForward agregarOtro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			DemodaUtil.populateVO(liqDecJurAdapterVO, request);
			
			this.populateGeneral(liqDecJurAdapterVO, request);
			
			liqDecJurAdapterVO.setListIdGeneralSelected(request.getParameterValues("listIdGeneralSelected"));
			
			// Validamos requeridos
			if (ModelUtil.isNullOrEmpty(liqDecJurAdapterVO.getAliMyS())){
				liqDecJurAdapterVO.addRecoverableValueError("Al alicuota es requerida");
			}
			
			// Validamos que existan seleccionados registros
			if (liqDecJurAdapterVO.getListIdGeneralSelected() == null || 
					liqDecJurAdapterVO.getListIdGeneralSelected().length == 0){
				
				liqDecJurAdapterVO.addRecoverableValueError("Debe seleccionar periodos para aplicar porcentajes");
			}
			
			// Realizamos el param sin ir al servicio, desde el mapa
			if (!liqDecJurAdapterVO.hasError()){
				
				log.debug(" Alicuota " + liqDecJurAdapterVO.obtenerAliMyS().getAlicuota());
									
				Integer idSel, periodo;
				Double porcentaje = liqDecJurAdapterVO.obtenerAliMyS().getAlicuota();
				Double adicOtro;
				boolean fueraDeRango = false;
				
				for(String idSelected:liqDecJurAdapterVO.getListIdGeneralSelected()){
					
					log.debug(" idSelectd " + idSelected);
					
					idSel = new Integer(idSelected);
					
					periodo = liqDecJurAdapterVO.getListGeneral().get(idSel).getPeriodo();
				
					if(periodo.intValue() >= 10 || periodo.intValue() <=3 ){

						liqDecJurAdapterVO.getListGeneral().get(idSel).setPorcAdicOtro(porcentaje);
						
						adicOtro = (liqDecJurAdapterVO.getListGeneral().get(idSel).getDeterminado() * porcentaje); 
						
						liqDecJurAdapterVO.getListGeneral().get(idSel).setAdicOtro(adicOtro);
						
						liqDecJurAdapterVO.getListGeneral().get(idSel).calcularTotal();
						
					} else {
						fueraDeRango = true;
					}
					
				}
				
				if (fueraDeRango){
					liqDecJurAdapterVO.addMessageValue("La alicuota solo se aplica a periodos entre octubre y marzo.");
				}
				
				// Reset
				liqDecJurAdapterVO.setListIdGeneralSelected(null);
				liqDecJurAdapterVO.setPorcentaje(0D);
				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());

			saveDemodaMessages(request, liqDecJurAdapterVO);
			saveDemodaErrors(request, liqDecJurAdapterVO);
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_GENERAL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	
	public ActionForward imputarRetencion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			DemodaUtil.populateVO(liqDecJurAdapterVO, request);
			
			if (liqDecJurAdapterVO.getEsDReI()){
				this.populateGeneral(liqDecJurAdapterVO, request);
			}
			
			liqDecJurAdapterVO.setListIdGeneralSelected(request.getParameterValues("listIdGeneralSelected"));
			
			// Validamos que existan seleccionados registros
			if (liqDecJurAdapterVO.getListIdGeneralSelected() == null || 
					liqDecJurAdapterVO.getListIdGeneralSelected().length == 0){
				liqDecJurAdapterVO.addRecoverableValueError("Debe seleccionar periodos para aplicar retenciones");
			}
			
			// Validamos requeridos
			if (liqDecJurAdapterVO.getRetencion().getImporte() == null){
				liqDecJurAdapterVO.addRecoverableValueError("El Importe es requerido");
			}
			
			if (StringUtil.isNullOrEmpty(liqDecJurAdapterVO.getRetencion().getCertificado())){
				liqDecJurAdapterVO.addRecoverableValueError("El Numero de Certificado es requerido");
			}

			// Realizamos el param sin ir al servicio, desde el mapa
			if (!liqDecJurAdapterVO.hasError()){
					
				log.debug(" Retencion " + liqDecJurAdapterVO.getRetencion().getImporte());
				log.debug(" Certificado " + liqDecJurAdapterVO.getRetencion().getCertificado());
				log.debug(" AgeRet " + liqDecJurAdapterVO.getRetencion().getAgeRet().getDesCuitView());
									
				Integer idSel;
				Double importeTotal = liqDecJurAdapterVO.getRetencion().getImporte();
				Double saldo = 0D;
				
				
				// calculo de total:
				// Determinado + Ad. Publicidad + Ad. Mesa y Silla - Retencion - (mayor valor entre Pago y Declarado + EnConvenio)
				
				for(String idSelected:liqDecJurAdapterVO.getListIdGeneralSelected()){
					
					Double retencion = 0D;
					Double subTotal = 0D;
					
					DecJurPagVO decJurPag = new DecJurPagVO();
					decJurPag.setCertificado(liqDecJurAdapterVO.getRetencion().getCertificado());
					decJurPag.setAgeRet(liqDecJurAdapterVO.getRetencion().getAgeRet());
					
					log.debug(" idSelectd " + idSelected);
					
					idSel = new Integer(idSelected);
					
					// Prorrateamos el importe sobre los periodos seleccionados
					log.debug(" @@@@@@@@@@@@@@ importeTotal: " + importeTotal);
					
					// Si queda saldo 
					if (importeTotal.doubleValue() >= 0){
						liqDecJurAdapterVO.getListGeneral().get(idSel).calcularTotal();
						subTotal = liqDecJurAdapterVO.getListGeneral().get(idSel).getTotal();
						//subTotal = liqDecJurAdapterVO.getListGeneral().get(idSel).getDeterminado();
						saldo = importeTotal - subTotal;
					}
					
					// De aca en adelante importeTotal = saldo restante
					// Positivo e iguala o supera al deteminado
					if (saldo.doubleValue() >= 0){
						log.debug(" @@@@@ saldo positivo " + saldo);
						
						retencion = subTotal;							

						importeTotal -= subTotal;
						decJurPag.setImporte(retencion);
					}
					
					// Si da negativo
					if (saldo.doubleValue() < 0){
						log.debug(" @@@@@ saldo negativo " + saldo);
						retencion = importeTotal;
						// Fin del prorrateo
						importeTotal = 0D;
						saldo = 0D;
						decJurPag.setImporte(retencion);
					}
					
					log.debug(" @@@@@ determinado: " + subTotal);
					log.debug(" @@@@@ retencion: " + retencion);
					log.debug(" @@@@@ decJurPag.importe: " + decJurPag.getImporte());
					
					// Si queda saldo a imputar
					if (retencion.doubleValue() > 0){
						// Acumulamos la retencion, para permitir mas de un certificado.
						retencion += liqDecJurAdapterVO.getListGeneral().get(idSel).getRetencion();
						liqDecJurAdapterVO.getListGeneral().get(idSel).setRetencion(retencion);
					
						// Agregamos a la lista de Certificados (decJurPag's)
						liqDecJurAdapterVO.getListGeneral().get(idSel).getListDecJurPag().add(decJurPag);
					}
					
					// Recalculamos el total
					liqDecJurAdapterVO.getListGeneral().get(idSel).calcularTotal();
				}
				
				// Reset
				liqDecJurAdapterVO.setListIdGeneralSelected(null);
				liqDecJurAdapterVO.getRetencion().setImporte(0D);
				liqDecJurAdapterVO.getRetencion().setCertificado("");
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());

			saveDemodaMessages(request, liqDecJurAdapterVO);
			saveDemodaErrors(request, liqDecJurAdapterVO);
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_GENERAL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	
	private void populateDetalle(LiqDecJurAdapter liqDecJurAdapterVO, HttpServletRequest request) throws Exception {
		
		for (int i=0; i < liqDecJurAdapterVO.getListDetalle().size(); i++ ){
			
			log.debug( " *********************   "  + i );
			log.debug( request.getParameter("montoImponible" + i));
			//log.debug( request.getParameter("alicuota" + i));
			
			// montoImponible
			String montoImponibleString = request.getParameter("montoImponible" + i);
			try{
				Double montoImponible = null;
				
				liqDecJurAdapterVO.getListDetalle().get(i).setMontoImponibleView(montoImponibleString);
				
				// Si es "" o espacios, nuleo el valor.
				if (montoImponibleString.trim().equals("") ){
					liqDecJurAdapterVO.getListDetalle().get(i).setMontoImponible(null);
					liqDecJurAdapterVO.getListDetalle().get(i).setMontoImponibleView(montoImponibleString);
				// Validacion de Formato
				} else {
					montoImponible = new Double(montoImponibleString);
					liqDecJurAdapterVO.getListDetalle().get(i).setMontoImponible(montoImponible);
				} 
				
			} catch (Exception e){
				
				liqDecJurAdapterVO.getListDetalle().get(i).setMontoImponible(null);
				liqDecJurAdapterVO.getListDetalle().get(i).setMontoImponibleView(montoImponibleString);
				liqDecJurAdapterVO.addRecoverableValueError("El formato del  Monto Imponible para la linea " + (i + 1) + " es invalido.");
			}

			// Seteamos el producto MontoImponible * Alicuota
			if (liqDecJurAdapterVO.getListDetalle().get(i).getMontoImponible() != null &&
					liqDecJurAdapterVO.getListDetalle().get(i).getAlicuota() != null) {
				
				liqDecJurAdapterVO.getListDetalle().get(i).setSubTotal1(liqDecJurAdapterVO.getListDetalle().get(i).getMontoImponible() *
						liqDecJurAdapterVO.getListDetalle().get(i).getAlicuota());
			}
		}
	}
	
	
	private void populateGeneral(LiqDecJurAdapter liqDecJurAdapterVO, HttpServletRequest request) throws Exception {
		
		for (int i=0; i < liqDecJurAdapterVO.getListGeneral().size(); i++ ){
			
			log.debug( " *********************   "  + i );
			log.debug( request.getParameter("pago" + i));
			
			// pago
			String pagoString = request.getParameter("pago" + i);
			try{
				Double pago = null;
				
				liqDecJurAdapterVO.getListGeneral().get(i).setPagoView(pagoString);
				
				// Si es "" o espacios, nuleo el valor.
				if (pagoString.trim().equals("") ){
					liqDecJurAdapterVO.getListGeneral().get(i).setPago(0D);
					liqDecJurAdapterVO.getListGeneral().get(i).setPagoView(pagoString);
				// Validacion de Formato
				} else {
					pago = new Double(pagoString);
					liqDecJurAdapterVO.getListGeneral().get(i).setPago(pago);
				} 
				
				
			} catch (Exception e){
				
				liqDecJurAdapterVO.getListGeneral().get(i).setPago(0D);
				liqDecJurAdapterVO.getListGeneral().get(i).setPagoView(pagoString);
				liqDecJurAdapterVO.addRecoverableValueError("El formato del Pago para la linea " + (i + 1) + " es invalido.");
			}
			
			liqDecJurAdapterVO.getListGeneral().get(i).calcularTotal();
			
		}
	}

	
	public ActionForward irGeneral(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			DemodaUtil.populateVO(liqDecJurAdapterVO, request);
			
			this.populateDetalle(liqDecJurAdapterVO, request);
			
			// Llamamos al servicio
			liqDecJurAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getLiqDecJurAdapterGeneral(userSession, liqDecJurAdapterVO);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());

			saveDemodaMessages(request, liqDecJurAdapterVO);
			saveDemodaErrors(request, liqDecJurAdapterVO);
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			if(liqDecJurAdapterVO.hasError()){
				
				return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_DETALLE);
			} else{
				
				return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_GENERAL);
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	
	
	public ActionForward volverGeneral(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			// Reset valores
			liqDecJurAdapterVO.setListDeudaSimulada(new ArrayList<LiqDeudaVO>());
			liqDecJurAdapterVO.setTotalImporte(0D);
			liqDecJurAdapterVO.setTotalSaldo(0D);
			
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_GENERAL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	public ActionForward simular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			DemodaUtil.populateVO(liqDecJurAdapterVO, request);
			
			if (liqDecJurAdapterVO.getEsDReI()){
				
				this.populateGeneral(liqDecJurAdapterVO, request);
			} else {
				for (int i=0; i < liqDecJurAdapterVO.getListGeneral().size(); i++ ){
					liqDecJurAdapterVO.getListGeneral().get(i).calcularTotal();
				}
			}
			
			// Llamamos al servicio
			liqDecJurAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getLiqDecJurAdapterSimular(userSession, liqDecJurAdapterVO);
			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());
			

			saveDemodaMessages(request, liqDecJurAdapterVO);
			saveDemodaErrors(request, liqDecJurAdapterVO);
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			if(liqDecJurAdapterVO.hasError()){
				
				return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_GENERAL);
			} else{
				
				return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_SIMULA);
			}
			
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	
	
	public ActionForward simularAFecha(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			DemodaUtil.populateVO(liqDecJurAdapterVO, request);
			
			// Llamamos al servicio
			liqDecJurAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getLiqDecJurAdapterSimularAFecha(userSession, liqDecJurAdapterVO);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());

			saveDemodaMessages(request, liqDecJurAdapterVO);
			saveDemodaErrors(request, liqDecJurAdapterVO);
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_SIMULA);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	
	public ActionForward generar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			DemodaUtil.populateVO(liqDecJurAdapterVO, request);
			
			// Llamamos al servicio
			liqDecJurAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getLiqDecJurAdapterGenerar(userSession, liqDecJurAdapterVO);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDecJurAdapter.NAME + ": "+ liqDecJurAdapterVO.infoString());
			
			saveDemodaMessages(request, liqDecJurAdapterVO);
			saveDemodaErrors(request, liqDecJurAdapterVO);
			// Lo subo a la session con o sin error
			userSession.put(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			// Envio el VO al request
			request.setAttribute(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
			
			log.error(funcName + " : liqDecJurAdapterVO " + liqDecJurAdapterVO.infoString());
			
			if(liqDecJurAdapterVO.hasError()){
				
				return mapping.findForward(GdeConstants.FWD_DECJUR_ADAPTER_SIMULA);
			} else{

				userSession.getNavModel().putParameter(LiqDecJurAdapter.NAME, liqDecJurAdapterVO);
				
				return forwardConfirmarOk(mapping, request, funcName, LiqDecJurAdapter.NAME, 
						GdeConstants.PATH_ADMINISTRAR_CONVENIO, GdeConstants.MTD_INICIALIZAR_FROM_DECJURMAS,
						GdeConstants.MTD_INICIALIZAR_FROM_DECJURMAS);
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDecJurAdapter.NAME);
		}
	}
	
	/**
	 * Utilizado para volver desde el el detalle de una Deuda 
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward volverACuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			log.debug(funcName + " selectedId: " + request.getParameter("selectedId"));
			
			Long selectedId = new Long(request.getParameter("selectedId"));
			log.debug("selectedId"+selectedId);
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId + "&validAuto=false";
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			return  new ActionForward (pathVerCuenta);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}


	public ActionForward imprimirSimulacion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DECLARACION_JURADA_MAS);
		
		try {
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			PrintModel print = GdeServiceLocator.getGdeGDeudaAutoService().imprimirSimulacion(userSession, liqDecJurAdapterVO);
			
			baseResponsePrintModel(response, print);
			
			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
}