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
import ar.gov.rosario.siat.cyq.iface.service.CyqServiceLocator;
import ar.gov.rosario.siat.cyq.view.util.CyqConstants;
import ar.gov.rosario.siat.gde.iface.model.LiqFormConvenioAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.PrintModel;


/**
 * Action encargado de:
 *  - Impresion del Formulario de Formalizacion
 *  - Impresion de Recibos de Cuotas 
 *  - Impresion de Recibos de Reconfeccion de Cuotas
 * 
 * Es utilizado desde:
 *  - Formalizacion de convenio
 *  - Formalizacion de convenio especial
 *  - Liquidacion de Deuda -> Ver Convenio
 *  - Consultar Convenio
 *  
 *  - Formalizacion de convenio (Cyq)
 *  - Formalizacion de convenio especial (Cyq)
 *  
 * Hay que indicarle a donde volver. 
 * 
 * @author Tecso
 */
public final class ImprimirConvenioDAction extends
		BaseDispatchAction {

	private Log log = LogFactory.getLog(ImprimirConvenioDAction.class);
	
	public ActionForward getFormPDF(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_CONVENIOCUENTA, GdeSecurityConstants.MTD_IMPRIMIR_FORM_CONVENIO);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "getPrintForm";
		try {
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
						
			// Llamada al servicio
			PrintModel print  = GdeServiceLocator.getFormConvenioDeudaService().getPrintForm(userSession, liqFormConvenioAdapterVO);
			
	        // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);
				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			baseResponsePrintModel(response, print);
			return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
	}
	
	
	public ActionForward getRecibosPDF(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_CONVENIOCUENTA, GdeSecurityConstants.MTD_IMPRIMIR_RECIBOS_CONVENIO);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "getRecibosPDF";
		try {
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
						
			// Llamada al servicio
			PrintModel print  = GdeServiceLocator.getFormConvenioDeudaService().getPrintRecibos(userSession, liqFormConvenioAdapterVO);
			
	        // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);
				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			baseResponsePrintModel(response, print);
			return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
	}
	
	
	/** 
	 * Vuelve al lugar de donde fue llamado. 
	 * 
	 */
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null)return forwardErrorSession(request);
		
		try {
			
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter) userSession.get(LiqFormConvenioAdapter.NAME);
			
			log.debug( funcName + " PrevActionParameter: " +   liqFormConvenioAdapterVO.getPrevActionParameter());
			
			String prevActPar = liqFormConvenioAdapterVO.getPrevActionParameter(); 
			
			// Volver al LiqConvenioCuentaAdapter llamado desde Consultar Convenio			
			if (prevActPar != null && 
					prevActPar.equals(GdeConstants.ACTION_VER_CONVENIO_INIT) ){
				request.setAttribute("selectedId",  liqFormConvenioAdapterVO.getConvenio().getIdConvenio());
				return mapping.findForward(GdeConstants.ACTION_VER_CONVENIO_INIT);
			}

			// Volver al LiqConvenioCuentaAdapter llamado desde la liquidacion Deuda
			if (prevActPar != null && 
					prevActPar.equals(GdeConstants.ACTION_VER_CONVENIO_VER) ){
				String forwardVerConvenio = "/gde/AdministrarLiqConvenioCuenta.do?method=verConvenio&selectedId=" + liqFormConvenioAdapterVO.getConvenio().getIdConvenio();  
				
				return new ActionForward (forwardVerConvenio);
			}
			
			// Volver al Print Adapter de la formalizacion
			if (prevActPar != null && 
					prevActPar.equals(GdeConstants.ACTION_FORMCONVENIO) ){
				
				// Llamada al servicio
				liqFormConvenioAdapterVO = GdeServiceLocator.getFormConvenioDeudaService().getConvenioFormalizado(userSession, liqFormConvenioAdapterVO);
		        			
				// Envio el VO al request
				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				
				userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				
				saveDemodaMessages(request, liqFormConvenioAdapterVO);
				
				return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_PRINT_ADAPTER);
			}
			
			// Volver al Print Adapter de la formalizacion de Cyq
			if (prevActPar != null && 
					prevActPar.equals(CyqConstants.ACTION_FORMCONVENIOCYQ) ){
				
				// Llamada al servicio
				liqFormConvenioAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getConvenioFormalizado(userSession, liqFormConvenioAdapterVO);
		        			
				// Envio el VO al request
				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				
				userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				
				saveDemodaMessages(request, liqFormConvenioAdapterVO);
				
				return mapping.findForward(GdeConstants.FWD_FORMCONVENIOCYQ_PRINT_ADAPTER);
			}			
			
			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
		
	}
		
}