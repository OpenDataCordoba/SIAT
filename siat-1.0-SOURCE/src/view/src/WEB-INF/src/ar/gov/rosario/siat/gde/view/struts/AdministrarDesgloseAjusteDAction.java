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
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.gde.iface.model.DesgloseAjusteAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDesgloseAjusteDAction extends
		BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDesgloseAjusteDAction.class);
	
	
	/**
	 * Metodo que se llama desde la liquidacion de la deuda
	 * @author Horacio
	 * @return
	 * @throws Exception
	 * 
	 */
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DESGLOSE_AJUSTE);			
		if (userSession == null) return forwardErrorSession(request);
		
		DesgloseAjusteAdapter desgloseAjusteAdapterVO; 
		String stringServicio = "getDesgloseAjusteInit";
		
		try {
			// Recuperamos datos del form en el vo
			log.debug(funcName + " idCuenta: " + request.getParameter("selectedId"));
			
			desgloseAjusteAdapterVO = new DesgloseAjusteAdapter();
			
			desgloseAjusteAdapterVO.getCuenta().setIdCuenta(Long.parseLong(request.getParameter("selectedId")));
			
			desgloseAjusteAdapterVO.setListIdDeudaSelected(request.getParameterValues("listIdDeudaSelected"));
			
			desgloseAjusteAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getDesgloseAjusteInit(userSession, desgloseAjusteAdapterVO);
			// Tiene errores no recuperables
			if (desgloseAjusteAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + desgloseAjusteAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesgloseAjusteAdapter.NAME, desgloseAjusteAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DesgloseAjusteAdapter.NAME + ": "+ desgloseAjusteAdapterVO.infoString());
			
			// Lo subo a la session con o sin error

			userSession.put(DesgloseAjusteAdapter.NAME, desgloseAjusteAdapterVO);
			
	        // Tiene errores recuperables
			if (desgloseAjusteAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desgloseAjusteAdapterVO.infoString()); 
				
				request.setAttribute("selectedId", desgloseAjusteAdapterVO.getCuenta().getIdCuenta());
									
				return this.volverACuenta(mapping, form, request, response);
				
			} else {
				
				log.error(funcName + " : desgloseAjusteAdapterVO " + desgloseAjusteAdapterVO.infoString());
				
				saveDemodaMessages(request, desgloseAjusteAdapterVO);		
				// Envio el VO al request
				request.setAttribute(DesgloseAjusteAdapter.NAME, desgloseAjusteAdapterVO);
			}
			
			return mapping.findForward(GdeConstants.FWD_DESGLOSEAJUSTE_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesgloseAjusteAdapter.NAME);
		}
	}
	
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesgloseAjusteAdapter desgloseAdapterVO = (DesgloseAjusteAdapter)userSession.get(DesgloseAjusteAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desgloseAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesgloseAjusteAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesgloseAjusteAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(desgloseAdapterVO, request);
			
			log.debug( funcName + " " + desgloseAdapterVO.getDesglose().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, desgloseAdapterVO.getDesglose()); 
			
			desgloseAdapterVO.getDesglose().passErrorMessages(desgloseAdapterVO);
		    
		    saveDemodaMessages(request, desgloseAdapterVO);
		    saveDemodaErrors(request, desgloseAdapterVO);
		    
			request.setAttribute(DesgloseAjusteAdapter.NAME, desgloseAdapterVO);
			
			return mapping.findForward( GdeConstants.FWD_DESGLOSEAJUSTE_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesgloseAjusteAdapter.NAME);
		}	
	}
	
	/**
	 * Utilizado para volver desde el el detalle de una Deuda 
	 * 
	 * @author Horacio
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
	 * 
	 * @author Horacio
	 * @return
	 * @throws Exception
	 */
	public ActionForward desglosar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_DESGLOSAR_AJUSTE);
		if (userSession == null)return forwardErrorSession(request);
		
		try {
			
			DesgloseAjusteAdapter desgloseAjusteAdapterVO = (DesgloseAjusteAdapter) userSession.get(DesgloseAjusteAdapter.NAME);
			
			DemodaUtil.populateVO(desgloseAjusteAdapterVO, request);
			
			// Llamada al servicio
			desgloseAjusteAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getDesglose(desgloseAjusteAdapterVO);
	        			
			
			if(desgloseAjusteAdapterVO.hasError()){
			// Envio el VO al request
			request.setAttribute(DesgloseAjusteAdapter.NAME, desgloseAjusteAdapterVO);
			userSession.put(DesgloseAjusteAdapter.NAME, desgloseAjusteAdapterVO);
			saveDemodaMessages(request, desgloseAjusteAdapterVO);
			saveDemodaErrors(request, desgloseAjusteAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_DESGLOSEAJUSTE_ADAPTER);
			} else {
			
		    // recupero el navMocel del usserSession
            NavModel navModel = userSession.getNavModel();
            
            //le seteo la accion a donde ir al navModel
            navModel.setConfAction("/gde/AdministrarLiqDeuda");
            navModel.setConfActionParameter("verCuenta&selectedId="+desgloseAjusteAdapterVO.getCuenta().getIdCuenta()+"&validAuto=false");
                                    
            // me dirije al mensaje de confirmacion OK
            return this.forwardMessage(mapping, navModel, 
                    NavModel.NAVMODEL_MESSAGE_TYPE_CONFIRMATION, BaseConstants.SUCCESS_MESSAGE_DESCRIPTION);
			
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					DesgloseAjusteAdapter.NAME);
		}
	}
	
}