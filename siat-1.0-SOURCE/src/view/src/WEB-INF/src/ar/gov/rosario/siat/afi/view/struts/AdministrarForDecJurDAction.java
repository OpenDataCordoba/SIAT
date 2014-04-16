//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.afi.iface.model.ForDecJurAdapter;
import ar.gov.rosario.siat.afi.iface.model.ForDecJurVO;
import ar.gov.rosario.siat.afi.iface.service.AfiServiceLocator;
import ar.gov.rosario.siat.afi.iface.util.AfiError;
import ar.gov.rosario.siat.afi.iface.util.AfiSecurityConstants;
import ar.gov.rosario.siat.afi.view.util.AfiConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.DecJurAdapter;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarForDecJurDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarForDecJurDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, AfiSecurityConstants.ABM_FORDECJUR, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ForDecJurAdapter forDecJurAdapterVO = null;
		DecJurAdapter decJurAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = null;
			if(!StringUtil.isNullOrEmpty(navModel.getSelectedId()))
					commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				
				Long idDecJur = null;
				decJurAdapterVO = (DecJurAdapter) userSession.get(DecJurAdapter.NAME);
				if(decJurAdapterVO != null){
					if(!ModelUtil.isNullOrEmpty(decJurAdapterVO.getDecJur()))
						idDecJur = decJurAdapterVO.getDecJur().getId();
				}
				stringServicio = "getForDecJurAdapterForView(userSession, commonKey)";
				forDecJurAdapterVO = AfiServiceLocator.getFormulariosDJService().getForDecJurAdapterForView(userSession, commonKey, idDecJur);
				actionForward = mapping.findForward(AfiConstants.FWD_FORDECJUR_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(AfiConstants.ACT_GENERAR_DECJUR)) {
				stringServicio = "getForDecJurAdapterForView(userSession, commonKey)";
				forDecJurAdapterVO = AfiServiceLocator.getFormulariosDJService().getForDecJurAdapterForView(userSession, commonKey , null);
				forDecJurAdapterVO.addMessage(AfiError.FORDECJUR_MSG_GENERAR_DECJUR);
				actionForward = mapping.findForward(AfiConstants.FWD_FORDECJUR_VIEW_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (forDecJurAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + forDecJurAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ForDecJurAdapter.NAME, forDecJurAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			forDecJurAdapterVO.setValuesFromNavModel(navModel);
						
			if(decJurAdapterVO != null){
				forDecJurAdapterVO.setPrevAction(decJurAdapterVO.getPrevAction());
				forDecJurAdapterVO.setPrevActionParameter(decJurAdapterVO.getPrevActionParameter());
				forDecJurAdapterVO.setSelectedId(decJurAdapterVO.getDecJur().getId().toString());
				
				//CommonNavegableView cnv = (CommonNavegableView) userSession.get(DecJurAdapter.NAME);
				//navModel.setValuesFromCommonNavegableView(cnv);
			}
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ForDecJurAdapter.NAME + ": "+ forDecJurAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ForDecJurAdapter.NAME, forDecJurAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ForDecJurAdapter.NAME, forDecJurAdapterVO);
			 
			saveDemodaMessages(request, forDecJurAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ForDecJurAdapter.NAME);
		}
	}	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ForDecJurAdapter.NAME);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ForDecJurAdapter.NAME);
			
	}

	public ActionForward verTotDerYAccDJ(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{		

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, AfiConstants.ACTION_ADMINISTRAR_TOTDERYACCDJ);
	}

	public ActionForward verRetYPer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{		

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, AfiConstants.ACTION_ADMINISTRAR_RETYPER);
	}
	
	public ActionForward verSocio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{		

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, AfiConstants.ACTION_ADMINISTRAR_SOCIO);
	}
	
	public ActionForward verDatosDomicilio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{		

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, AfiConstants.ACTION_ADMINISTRAR_DATOSDOMICILIO);
	}
	
	public ActionForward verLocal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception{		

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, AfiConstants.ACTION_ADMINISTRAR_LOCAL);
	}
	
	public ActionForward imprimirReportFromAdapter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			// obtiene el nombre del page del request
			//String name = request.getParameter("name");
			String name = ForDecJurAdapter.NAME;
			String reportFormat = request.getParameter("report.reportFormat");
			
			// **Bajo el searchPage del userSession
			String responseFile = request.getParameter("responseFile");
			if ("1".equals(responseFile)) {
				String fileName = (String) userSession.get("baseImprimir.reportFilename");
				// realiza la visualizacion del reporte
				baseResponseEmbedContent(response, fileName, "application/pdf");
				return null;
			}
			
			// Bajo el adapter del userSession
			ForDecJurAdapter forDecJurAdapterVO = (ForDecJurAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (forDecJurAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ForDecJurAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			forDecJurAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			forDecJurAdapterVO = AfiServiceLocator.getFormulariosDJService().imprimirForDecJur(userSession, forDecJurAdapterVO);

			// limpia la lista de reports y la lista de tablas
			forDecJurAdapterVO.getReport().getListReport().clear();
			forDecJurAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (forDecJurAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + forDecJurAdapterVO.infoString());
				saveDemodaErrors(request, forDecJurAdapterVO);				
				request.setAttribute(name, forDecJurAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (forDecJurAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + forDecJurAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, forDecJurAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = forDecJurAdapterVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}

	public ActionForward generarDecJur(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				AfiSecurityConstants.ABM_FORDECJUR, AfiSecurityConstants.MTD_GENERARDECJUR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ForDecJurAdapter forDecJurAdapterVO = (ForDecJurAdapter) userSession.get(ForDecJurAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (forDecJurAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ForDecJurAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ForDecJurAdapter.NAME); 
				}
				
				// llamada al servicio
				ForDecJurVO forDecJurVO = AfiServiceLocator.getFormulariosDJService().generarDecJur(userSession, forDecJurAdapterVO.getForDecJur());
				
	            // Tiene errores recuperables
				if (forDecJurVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + forDecJurAdapterVO.infoString()); 
					saveDemodaErrors(request, forDecJurVO);
					return forwardErrorRecoverable(mapping, request, userSession, ForDecJurAdapter.NAME, forDecJurAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (forDecJurVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + forDecJurAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ForDecJurAdapter.NAME, forDecJurAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, ForDecJurAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ForDecJurAdapter.NAME);
			}
	}
}
