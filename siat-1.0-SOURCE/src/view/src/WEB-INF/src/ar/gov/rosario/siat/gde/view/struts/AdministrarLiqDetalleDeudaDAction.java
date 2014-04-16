//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.LiqConceptoDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDetalleDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarLiqDetalleDeudaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarLiqDetalleDeudaDAction.class);
	
	/**
	 * Permite ver el detalle de un registro de Deuda con 
	 * sus conceptos y pagos asociados.
	 * 
	 * @author Cristian
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward verDetalleDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		LiqDetalleDeudaAdapter liqDetalleDeudaAdapterVO = new LiqDetalleDeudaAdapter();
		String stringServicio = "getLiqDetalleDeudaAdapter()";
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idDeuda: " + request.getParameter("selectedId"));
			
			if (request.getParameter("selectedId") != null){
				liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setIdDeuda(new Long(request.getParameter("selectedId")));
			}
			
			LiqDetalleDeudaAdapter liqDetalleDeudaAdapter = GdeServiceLocator.getGestionDeudaService().
					getLiqDetalleDeudaAdapter(userSession, liqDetalleDeudaAdapterVO);
			
			// Seteamos el filtro de liquidacion de deuda
			liqDetalleDeudaAdapter.setCuenta((LiqCuentaVO) userSession.get("liqCuentaFilter"));
			
			// Tiene errores no recuperables
			if (liqDetalleDeudaAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDetalleDeudaAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDetalleDeudaAdapter.NAME, liqDetalleDeudaAdapter);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDetalleDeudaAdapter.NAME + ": "+ liqDetalleDeudaAdapter.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqDetalleDeudaAdapter.NAME, liqDetalleDeudaAdapter);
			
			userSession.put(LiqDetalleDeudaAdapter.NAME, liqDetalleDeudaAdapterVO);
			
			saveDemodaMessages(request, liqDetalleDeudaAdapter);
			
			return mapping.findForward(GdeConstants.FWD_LIQDETALLEDEUDA_VIEW_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDetalleDeudaAdapter.NAME);
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
	
	
	/**
	 * Forward a buscar declaraciones juradas
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward verCuentaDecJur(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idDeuda: " + request.getParameter("idDeuda"));
			log.debug(funcName + " selectedId: " + request.getParameter("selectedId"));
			
			LiqDetalleDeudaAdapter liqDetalleDeudaAdapterVO = new LiqDetalleDeudaAdapter();
			
			if (request.getParameter("idDeuda") != null)
				liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setIdDeuda(new Long(request.getParameter("idDeuda")));
			
			// Traspaso de permisos al adapter que el elevado a la session.
			liqDetalleDeudaAdapterVO.setPrevAction(userSession.getAccionSWE());
			liqDetalleDeudaAdapterVO.setPrevActionParameter(userSession.getMetodoSWE());
			
			userSession.put(LiqDetalleDeudaAdapter.NAME, liqDetalleDeudaAdapterVO);
			
			return baseForward(mapping,request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_VER_DEC_JURADA, BaseConstants.ACT_INICIALIZAR); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	/**
	 * Obtiene y muestra un detalle de dedua recibiendo un selectedId formado por:
	 * 		idDeuda mas "-" mas idEstadoDeuda
	 * 
	 * Vuelve a donde le hallan indicado volver.
	 *  
	 */
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, 
				GdeSecurityConstants.MTD_VER_DETALLE_DEUDA);
		if (userSession == null) return forwardErrorSession(request);
		
		LiqDetalleDeudaAdapter liqDetalleDeudaAdapterVO = new LiqDetalleDeudaAdapter();
		String stringServicio = "getLiqDetalleDeudaAdapter()";
		try {
			
			String selectedId = userSession.getNavModel().getSelectedId();
			
			userSession.getNavModel().setAct(GdeConstants.ACT_INCLUDE_VERDETALLE_DEUDA);
			
			if (!StringUtil.isNullOrEmpty(selectedId)){
				String[] arrId = selectedId.split("-");
				
				liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setIdDeuda(new Long(arrId[0]));
				liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().setIdEstadoDeuda(new Long(arrId[1]));
			} else {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDetalleDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDetalleDeudaAdapter.NAME, liqDetalleDeudaAdapterVO);				
			}
			
			LiqDetalleDeudaAdapter liqDetalleDeudaAdapter = GdeServiceLocator.getGestionDeudaService().
					getLiqDetalleDeudaAdapter(userSession, liqDetalleDeudaAdapterVO);
			
			// Tiene errores no recuperables
			if (liqDetalleDeudaAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDetalleDeudaAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDetalleDeudaAdapter.NAME, liqDetalleDeudaAdapter);
			}
			
			// Seteo los valores de navegacion en el adapter
			liqDetalleDeudaAdapter.setValuesFromNavModel(userSession.getNavModel());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDetalleDeudaAdapter.NAME + ": "+ liqDetalleDeudaAdapter.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqDetalleDeudaAdapter.NAME, liqDetalleDeudaAdapter);
			
			userSession.put(LiqDetalleDeudaAdapter.NAME, liqDetalleDeudaAdapter);
			
			saveDemodaMessages(request, liqDetalleDeudaAdapter);
			
			return mapping.findForward(GdeConstants.FWD_LIQDETALLEDEUDA_VIEW_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDetalleDeudaAdapter.NAME);
		}
	}
	
	/**
	 * Volver que se ejecuta cuando este action es incluido por otro distinto al AdministrarLiqDeuda. 
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		return baseVolver(mapping, form, request, response, LiqDetalleDeudaAdapter.NAME);		
	}
	
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		try {

			LiqDetalleDeudaAdapter liqDetalleDeudaAdapterVO = (LiqDetalleDeudaAdapter) userSession.get(LiqDetalleDeudaAdapter.NAME);
			
			userSession = canAccess(request, mapping, 
					liqDetalleDeudaAdapterVO.getPrevAction(),
					liqDetalleDeudaAdapterVO.getPrevActionParameter());
			
			if (userSession == null) return forwardErrorSession(request);
			
			// Si es nulo no se puede continuar
			if (liqDetalleDeudaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LiqDetalleDeudaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqDetalleDeudaAdapter.NAME); 
			}
			
			Long selectedId = liqDetalleDeudaAdapterVO.getLiqDetalleDeuda().getIdDeuda();
			log.debug("selectedId "+ selectedId);
			
			userSession.remove(LiqDetalleDeudaAdapter.NAME);
			
			String pathVerDetalleDeuda = GdeConstants.PATH_VER_DETALLE_DEUDA + "&selectedId=" + selectedId;
			
			log.debug(funcName + " pathVerDetalleDeuda =" + pathVerDetalleDeuda);
			
			return  new ActionForward (pathVerDetalleDeuda);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	/**
	 * Forward a modificar Detalle Deuda
	 * 
	 * @return
	 * @throws Exception
	 */
	public ActionForward modificarDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		LiqDetalleDeudaAdapter liqDetalleDeudaAdapter = (LiqDetalleDeudaAdapter) userSession.get(LiqDetalleDeudaAdapter.NAME);
		
		try {
			
			liqDetalleDeudaAdapter.setAct(GdeConstants.ACT_MODIFICAR_DEUDA);
			// Envio el VO al request
			request.setAttribute(LiqDetalleDeudaAdapter.NAME, liqDetalleDeudaAdapter);
			
			//saveDemodaMessages(request, liqDetalleDeudaAdapter);
			
			return mapping.findForward(GdeConstants.FWD_LIQDETALLEDEUDA_VIEW_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDetalleDeudaAdapter.NAME);
		}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
					GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_MODIFICAR_DEUDA);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				LiqDetalleDeudaAdapter liqDetalleDeudaAdapter = (LiqDetalleDeudaAdapter) userSession.get(LiqDetalleDeudaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (liqDetalleDeudaAdapter == null) {
					log.error("error en: "  + funcName + ": " + LiqDetalleDeudaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, LiqDetalleDeudaAdapter.NAME); 
				}
				liqDetalleDeudaAdapter.clearError();
				
				// Recuperamos datos del form en el VO
				Double importe = null;
				String importeView = request.getParameter("liqDetalleDeuda.importeView");
				if(!StringUtil.isNullOrEmpty(importeView)){
					importe = NumberUtil.getDouble(importeView);
					if(importe == null){
						liqDetalleDeudaAdapter.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, GdeError.DEUDA_IMPORTE);
					}else{
						liqDetalleDeudaAdapter.getLiqDetalleDeuda().setImporte(importe);						
					}
				}else{
					liqDetalleDeudaAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDA_IMPORTE);					
				}
				Double saldo = null;
				String saldoView = request.getParameter("liqDetalleDeuda.saldoView");
				if(!StringUtil.isNullOrEmpty(saldoView)){
					saldo = NumberUtil.getDouble(saldoView);
					if(saldo == null){
						liqDetalleDeudaAdapter.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, GdeError.DEUDA_SALDO);
					}else{						
						liqDetalleDeudaAdapter.getLiqDetalleDeuda().setSaldo(saldo);
					}
				}else{
					liqDetalleDeudaAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDA_SALDO);					
				}
				Double actualizacion = null;
				String actualizacionView = request.getParameter("liqDetalleDeuda.actualizacionView");
				if(!StringUtil.isNullOrEmpty(actualizacionView)){
					actualizacion = NumberUtil.getDouble(actualizacionView);
					if(actualizacion == null){
						liqDetalleDeudaAdapter.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, GdeError.DEUDA_ACTUALIZACION);
					}else{						
						liqDetalleDeudaAdapter.getLiqDetalleDeuda().setActualizacion(actualizacion);
					}
				}else{
					liqDetalleDeudaAdapter.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDA_ACTUALIZACION);					
				}
				for(LiqConceptoDeudaVO concepto: liqDetalleDeudaAdapter.getLiqDetalleDeuda().getListConceptos()){
					Double importeConcepto = null;
					String importeConceptoView = request.getParameter("importe"+concepto.getIdRecConView());
					if(!StringUtil.isNullOrEmpty(importeConceptoView)){
						importeConcepto = NumberUtil.getDouble(importeConceptoView);
						if(importeConcepto == null){
							liqDetalleDeudaAdapter.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, GdeError.DEURECCON_LABEL);
							break;
						}else{
							concepto.setImporte(importeConcepto);		
						}
					}else{
						liqDetalleDeudaAdapter.addRecoverableError(GdeError.DEUDA_CONCEPTO_IMPORTE_ERROR);
						break;
					}
				}
				String fechaPagoView = request.getParameter("liqDetalleDeuda.fechaPagoView");
				if(!StringUtil.isNullOrEmpty(fechaPagoView)){
					try{
						Date fechaPago = DateUtil.getDate(fechaPagoView,  DateUtil.ddSMMSYYYY_MASK);
						liqDetalleDeudaAdapter.getLiqDetalleDeuda().setFechaPago(fechaPago);							
					}catch(Exception e){
						liqDetalleDeudaAdapter.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO,   GdeError.DEUDA_FECHAPAGO);
					}					
				}else{
					liqDetalleDeudaAdapter.getLiqDetalleDeuda().setFechaPago(null);
				}
				
			    // Tiene errores recuperables
				if (liqDetalleDeudaAdapter.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + liqDetalleDeudaAdapter.infoString()); 
					saveDemodaErrors(request, liqDetalleDeudaAdapter);
					return forwardErrorRecoverable(mapping, request, userSession, LiqDetalleDeudaAdapter.NAME, liqDetalleDeudaAdapter);
				}
				
				// llamada al servicio
				liqDetalleDeudaAdapter = GdeServiceLocator.getGestionDeudaService().modificarDeuda(userSession, liqDetalleDeudaAdapter);
				
	            // Tiene errores recuperables
				if (liqDetalleDeudaAdapter.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + liqDetalleDeudaAdapter.infoString()); 
					saveDemodaErrors(request, liqDetalleDeudaAdapter);
					return forwardErrorRecoverable(mapping, request, userSession, LiqDetalleDeudaAdapter.NAME, liqDetalleDeudaAdapter);
				}
				
				// Tiene errores no recuperables
				if (liqDetalleDeudaAdapter.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + liqDetalleDeudaAdapter.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, LiqDetalleDeudaAdapter.NAME, liqDetalleDeudaAdapter);
				}
				
				// Fue Exitoso, Confirmar OK con volverACuenta:
				
				// Setea selectedId con idCuenta (como el volverACuenta)
				Long selectedId = liqDetalleDeudaAdapter.getLiqDetalleDeuda().getIdCuenta();
				log.debug("selectedId"+selectedId);
				
				// recupero el navModel del usserSession
				NavModel navModel = userSession.getNavModel();
				
				// le seteo la accion a donde ir en la pantalla de confirmacion al navModel (Esto se hace de forma especial para que la confirmar vuelva de la misma forma que el volver
				// especial 'volverACuenta'
				navModel.setConfAction(GdeConstants.PATH_ACTION_FOR_CONFIRMAR_MODIFICAR);
				navModel.setConfActionParameter(GdeConstants.PATH_ACTION_PARAMETER_FOR_CONFIRMAR_MODIFICAR + selectedId + "&validAuto=false");
				
				// saco el VO del usser session
				userSession.remove(LiqDetalleDeudaAdapter.NAME);
					
				return this.forwardMessage(mapping, navModel, NavModel.NAVMODEL_MESSAGE_TYPE_CONFIRMATION, BaseConstants.SUCCESS_MESSAGE_DESCRIPTION);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, LiqDetalleDeudaAdapter.NAME);
			}
	}

}