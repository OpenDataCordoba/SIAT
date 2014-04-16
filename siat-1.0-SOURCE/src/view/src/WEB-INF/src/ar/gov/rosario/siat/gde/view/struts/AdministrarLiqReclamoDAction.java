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
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqReclamoAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqReclamoVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public final class AdministrarLiqReclamoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarLiqReclamoDAction.class);
	
	/**
	 * Fordwardea a la pantalla para registrar el Reclamo de Asentamiento de Pago.
	 * 
	 * @author Cristian
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward reclamarAcent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_RECLAMAR_ACENT);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "getLiqReclamoAdapterInit()";
		LiqReclamoAdapter liqReclamoAdapter = new LiqReclamoAdapter();

		try {
			
			// Nos llega idDeuda + idEstadoDeuda
			// En el servicio lo separamos.
			
			String[] listIdDeudaSelected = null;
			
			if (request.getParameterValues("listIdDeudaSelected") != null)
				listIdDeudaSelected = request.getParameterValues("listIdDeudaSelected");
			
			if (listIdDeudaSelected == null){
				liqReclamoAdapter.addRecoverableValueError("Debe seleccionar el periodo a reclamar.");
			} else if (listIdDeudaSelected != null && listIdDeudaSelected.length != 1){
				liqReclamoAdapter.addRecoverableValueError("Debe seleccionar un solo periodo a reclamar.");
			} else {
				liqReclamoAdapter.setSelectedId(listIdDeudaSelected[0]);
			}
			
			if (liqReclamoAdapter.hasError()){
				userSession.put(LiqReclamoAdapter.NAME, liqReclamoAdapter);
				return this.volverACuenta(mapping, form, request, response);
			}
			
			liqReclamoAdapter = GdeServiceLocator.getGestionDeudaService().getLiqReclamoAdapterInit(userSession, liqReclamoAdapter);
			
			// Tiene errores no recuperables
			if (liqReclamoAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqReclamoAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqReclamoAdapter.NAME, liqReclamoAdapter);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqReclamoAdapter.NAME + ": "+ liqReclamoAdapter.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqReclamoAdapter.NAME, liqReclamoAdapter);
			
			saveDemodaMessages(request, liqReclamoAdapter);
			
			return mapping.findForward(GdeConstants.FWD_RECLAMO_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqReclamoAdapter.NAME);
		}
	}
	
	/**
	 *  
	 * Aqui se regitra el Reclamo de Asentamiento de Pago.
	 * 
	 * 
	 * @author Cristian
	 * @return Liquidacion Deuda
	 * @throws Exception
	 */
	public ActionForward registrarReclamo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_RECLAMAR_ACENT);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "createReclamo()";
		LiqReclamoAdapter liqReclamoAdapter = new LiqReclamoAdapter();

		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idDeuda: " + request.getParameter("selectedId"));
			
			if (request.getParameter("selectedId") != null ){
				liqReclamoAdapter.setSelectedId(request.getParameter("selectedId"));
			}
			
			liqReclamoAdapter = GdeServiceLocator.getGestionDeudaService().getLiqReclamoAdapterInit(userSession, liqReclamoAdapter);
						
			LiqReclamoVO reclamoVO = liqReclamoAdapter.getLiqReclamo();
			
			// hacemos un populateVO manualmente, cargamos datos, y validamos sintaxis
			reclamoVO.setFechaPagoView(request.getParameter("liqReclamo.fechaPagoView"));
			reclamoVO.setImportePagadoView(request.getParameter("liqReclamo.importePagadoView"));
			reclamoVO.setBanco(request.getParameter("liqReclamo.banco"));
			reclamoVO.setNombre(request.getParameter("liqReclamo.nombre"));
			reclamoVO.setApellido(request.getParameter("liqReclamo.apellido"));
			reclamoVO.setTipoDoc(request.getParameter("liqReclamo.tipoDoc"));
			reclamoVO.setNroDoc(request.getParameter("liqReclamo.nroDoc"));
			reclamoVO.setTelefono(request.getParameter("liqReclamo.telefono"));
			reclamoVO.setEmail(request.getParameter("liqReclamo.email"));
			reclamoVO.setObservacion(request.getParameter("liqReclamo.observacion"));
			String selectedId = request.getParameter("selectedId"); // es idDeuda-estadoDeuda
			reclamoVO.setIdDeuda(new Long(selectedId.split("-")[0]));
			
			// validamos syntaxis y convertimos los view en tipados.
			// Fecha de Pago			 
			if (!DateUtil.isValidDate(reclamoVO.getFechaPagoView(), DateUtil.ddSMMSYYYY_MASK)) {
				liqReclamoAdapter.addRecoverableError(GdeError.LIQRECLAMO_FECHAPAGO_FORMAT_ERROR);
			} else {
				reclamoVO.setFechaPago(DateUtil.getDate(reclamoVO.getFechaPagoView(), "dd/MM/yyyy"));
			}
			
			// Importe Pagado 
			if (!StringUtil.isDouble(reclamoVO.getImportePagadoView())){
				liqReclamoAdapter.addRecoverableError(GdeError.LIQRECLAMO_IMPORTEPAGADO_FORMAT_ERROR);
			} else {
				liqReclamoAdapter.getLiqReclamo().setImportePagado(new Double (liqReclamoAdapter.getLiqReclamo().getImportePagadoView()));
			}
						
			// Numero Documento
			if (!StringUtil.isNumeric(reclamoVO.getNroDoc())){
				liqReclamoAdapter.addRecoverableError(GdeError.LIQRECLAMO_NRODOC_FORMAT_ERROR);
			}
			
			// Telefono o Mail
			if (!StringUtil.isNullOrEmpty(reclamoVO.getEmail()) && !StringUtil.isValidMail(reclamoVO.getEmail())){
				liqReclamoAdapter.addRecoverableError(GdeError.LIQRECLAMO_EMAIL_FORMAT_ERROR);
			}
			
			// Si no tiene Errores llamos al service
			// Grabo el reclamo
			// Va a la pantalla con el mensaje
			if (!liqReclamoAdapter.hasError()) {
				
				liqReclamoAdapter = GdeServiceLocator.getGestionDeudaService().createReclamoDeuda(userSession, liqReclamoAdapter);
				
				// Tiene errores recuperables
				if (liqReclamoAdapter.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + liqReclamoAdapter.infoString()); 
					saveDemodaErrors(request, liqReclamoAdapter);
					request.setAttribute(LiqReclamoAdapter.NAME, liqReclamoAdapter);
					return mapping.findForward(GdeConstants.FWD_RECLAMO_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (liqReclamoAdapter.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqReclamoAdapter.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, LiqReclamoAdapter.NAME, liqReclamoAdapter);
				}
				
				request.setAttribute(LiqReclamoAdapter.NAME, liqReclamoAdapter);
				saveDemodaMessages(request, liqReclamoAdapter);
				
				return mapping.findForward(GdeConstants.FWD_RECLAMO_MSG);

			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqReclamoAdapter.NAME + ": "+ liqReclamoAdapter.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqReclamoAdapter.NAME, liqReclamoAdapter);
			
			saveDemodaErrors(request, liqReclamoAdapter);
			
			return mapping.findForward(GdeConstants.FWD_RECLAMO_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqReclamoAdapter.NAME);
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
}