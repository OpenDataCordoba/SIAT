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
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqFormConvenioAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class AdministrarLiqFormConvenioEspDAction extends
		BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarLiqFormConvenioEspDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_FORMALIZAR_CONVENIO);			
		if (userSession == null) return forwardErrorSession(request);
		
		LiqFormConvenioAdapter liqFormConvenioAdapterVO; 
		String stringServicio = "getLiqFormConvenioInit";
		
		try {
			// Recuperamos datos del form en el vo
			log.debug(funcName + " idCuenta: " + request.getParameter("selectedId"));
			
			// La instanciacion se realiza aqui por motivos particulares de navegacion
			liqFormConvenioAdapterVO = new LiqFormConvenioAdapter();					
		
			liqFormConvenioAdapterVO.getCuenta().setIdCuenta(Long.parseLong(request.getParameter("selectedId")));
			
			liqFormConvenioAdapterVO.setListIdDeudaSelected(request.getParameterValues("listIdDeudaSelected"));
			
			// Pasamos el filtro de la liquidacion de deuda.
			LiqCuentaVO liqCuentaFiltro=(LiqCuentaVO)userSession.get("liqCuentaFilter");
			
			if (liqCuentaFiltro==null){
				liqCuentaFiltro = new LiqCuentaVO();
			}
			
			liqFormConvenioAdapterVO.setCuentaFilter(liqCuentaFiltro);
			
			liqFormConvenioAdapterVO = GdeServiceLocator.getFormConvenioDeudaService().getLiqFormConvenioInit(userSession, 
					liqFormConvenioAdapterVO);
			
			liqFormConvenioAdapterVO.setEsEspecial(true);
			
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			// Lo subo a la session con o sin error
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
	        // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				
				request.setAttribute("selectedId", liqFormConvenioAdapterVO.getCuenta().getIdCuenta());
									
				return this.volverACuenta(mapping, form, request, response);
				
			} else {
				
				log.error(funcName + " : liqFormConvenioAdapterVO " + liqFormConvenioAdapterVO.infoString());
				
				saveDemodaMessages(request, liqFormConvenioAdapterVO);		
				// Envio el VO al request
				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			return mapping.findForward(GdeConstants.FWD_FORMCONVENIOESP_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqFormConvenioAdapter.NAME);
		}
	}
	
	
	public ActionForward refillInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

	
		return mapping.findForward(GdeConstants.FWD_FORMCONVENIOESP_ADAPTER);
	}
	
	/**
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward seleccionarPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_FORMALIZAR_CONVENIO);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "getPlanesEsp";
		try {
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(liqFormConvenioAdapterVO, request);

			// Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);

				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				return mapping.findForward(GdeConstants.FWD_FORMCONVENIOESP_ADAPTER);
			}
			
			// Llamada al servicio
			liqFormConvenioAdapterVO = GdeServiceLocator.getFormConvenioDeudaService().getPlanesEsp(userSession, liqFormConvenioAdapterVO);

	        // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);

				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				return mapping.findForward(GdeConstants.FWD_FORMCONVENIOESP_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			log.debug(funcName + " Es Especial: " + liqFormConvenioAdapterVO.getEsEspecial() );
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
						
			saveDemodaMessages(request, liqFormConvenioAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_PLANES_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
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
	
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (liqFormConvenioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LiqFormConvenioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqFormConvenioAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(liqFormConvenioAdapterVO, request);
			
			log.debug( funcName + " " + liqFormConvenioAdapterVO.getConvenio().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, liqFormConvenioAdapterVO.getConvenio()); 
			
			liqFormConvenioAdapterVO.getConvenio().passErrorMessages(liqFormConvenioAdapterVO);
		    
		    saveDemodaMessages(request, liqFormConvenioAdapterVO);
		    saveDemodaErrors(request, liqFormConvenioAdapterVO);
		    
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			return mapping.findForward( GdeConstants.FWD_FORMCONVENIOESP_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqFormConvenioAdapter.NAME);
		}	
	}
	



}