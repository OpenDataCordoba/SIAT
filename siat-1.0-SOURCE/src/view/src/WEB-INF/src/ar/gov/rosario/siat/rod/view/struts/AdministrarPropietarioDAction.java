//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.view.struts;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.Sexo;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.rod.iface.model.PropietarioAdapter;
import ar.gov.rosario.siat.rod.iface.model.PropietarioVO;
import ar.gov.rosario.siat.rod.iface.model.TramiteRAAdapter;
import ar.gov.rosario.siat.rod.iface.service.RodServiceLocator;
import ar.gov.rosario.siat.rod.iface.util.RodError;
import ar.gov.rosario.siat.rod.iface.util.RodSecurityConstants;
import ar.gov.rosario.siat.rod.view.util.RodConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;


public final class AdministrarPropietarioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPropietarioDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_PROPIETARIO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PropietarioAdapter propietarioAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = null;
			if (!StringUtil.isNullOrEmpty(navModel.getSelectedId()))
				commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPropietarioAdapterForView(userSession, commonKey)";
				propietarioAdapterVO = RodServiceLocator.getTramiteService().getPropietarioAdapterForView(userSession, commonKey);
				
				String selectedId = userSession.getNavModel().getSelectedId();				
				TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				List<PropietarioVO> listProp = tramiteRAAdapterVO.getTramiteRA().getListPropietario(); 
				for(PropietarioVO propietario:listProp){
					if(tramiteRAAdapterVO.isEsPropActual()){
						if(propietario.getNroDoc().equals(new Long(selectedId)) && propietario.getTipoPropietario().equals(1)){
							propietario.setSexo(Sexo.getById(propietario.getCodSexo()));							
							propietarioAdapterVO.setPropietario(propietario);
							break;
						}
					}else{
						if(propietario.getNroDoc().equals(new Long(selectedId)) && propietario.getTipoPropietario().equals(2)){
							propietario.setSexo(Sexo.getById(propietario.getCodSexo()));
							propietarioAdapterVO.setPropietario(propietario);
							break;
						}
					}
					
				}
				actionForward = mapping.findForward(RodConstants.FWD_PROPIETARIO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPropietarioAdapterForUpdate(userSession, commonKey)";
				propietarioAdapterVO = RodServiceLocator.getTramiteService().getPropietarioAdapterForUpdate(userSession, commonKey);
				String selectedId = userSession.getNavModel().getSelectedId();				
				TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
								
				List<PropietarioVO> listProp = tramiteRAAdapterVO.getTramiteRA().getListPropietario(); 
				for(PropietarioVO propietario:listProp){
					if(tramiteRAAdapterVO.isEsPropActual()){
						propietario.setSexo(Sexo.getById(propietario.getCodSexo()));
						if(propietario.getNroDoc().equals(new Long(selectedId)) && propietario.getTipoPropietario().equals(1)){														
							propietarioAdapterVO.setPropietario(propietario);
							break;
						}
					}else{
						if(propietario.getNroDoc().equals(new Long(selectedId)) && propietario.getTipoPropietario().equals(2)){
							propietario.setSexo(Sexo.getById(propietario.getCodSexo()));							
							propietarioAdapterVO.setPropietario(propietario);						
							break;
						}
					}
				}
				actionForward = mapping.findForward(RodConstants.FWD_PROPIETARIO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPropietarioAdapterForView(userSession, commonKey)";
				propietarioAdapterVO = RodServiceLocator.getTramiteService().getPropietarioAdapterForView(userSession, commonKey);				
				
				String selectedId = userSession.getNavModel().getSelectedId();				
				TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
				List<PropietarioVO> listProp = tramiteRAAdapterVO.getTramiteRA().getListPropietario(); 
				for(PropietarioVO propietario:listProp){
					if(tramiteRAAdapterVO.isEsPropActual()){
						propietario.setSexo(Sexo.getById(propietario.getCodSexo()));
						if(propietario.getNroDoc().equals(new Long(selectedId)) && propietario.getTipoPropietario().equals(1)){														
							propietarioAdapterVO.setPropietario(propietario);							
							break;
						}
					}else{
						if(propietario.getNroDoc().equals(new Long(selectedId)) && propietario.getTipoPropietario().equals(2)){													
							propietarioAdapterVO.setPropietario(propietario);
							break;
						}
					}
				}
				propietarioAdapterVO.addMessage(BaseError.MSG_ELIMINAR, RodError.PROPIETARIO_LABEL);
				
				actionForward = mapping.findForward(RodConstants.FWD_PROPIETARIO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPropietarioAdapterForCreate(userSession)";
				propietarioAdapterVO = RodServiceLocator.getTramiteService().getPropietarioAdapterForCreate(userSession);
				actionForward = mapping.findForward(RodConstants.FWD_PROPIETARIO_EDIT_ADAPTER);				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (propietarioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + propietarioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PropietarioAdapter.NAME, propietarioAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			propietarioAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PropietarioAdapter.NAME + ": "+ propietarioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PropietarioAdapter.NAME, propietarioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PropietarioAdapter.NAME, propietarioAdapterVO);
			 
			saveDemodaMessages(request, propietarioAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PropietarioAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_PROPIETARIO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PropietarioAdapter propietarioAdapterVO = (PropietarioAdapter) userSession.get(PropietarioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (propietarioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PropietarioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PropietarioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(propietarioAdapterVO, request);
			
            // Tiene errores recuperables
			if (propietarioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + propietarioAdapterVO.infoString()); 
				saveDemodaErrors(request, propietarioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PropietarioAdapter.NAME, propietarioAdapterVO);
			}
			
			// llamada al servicio
			PropietarioVO propietarioVO = RodServiceLocator.getTramiteService().validateCreatePropietario(userSession, propietarioAdapterVO.getPropietario());
							
			TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);			
		
			propietarioVO.setSexo(Sexo.getById(propietarioVO.getCodSexo()));
			
			
			for(PropietarioVO p: tramiteRAAdapterVO.getTramiteRA().getListPropietario()){
				if(p.getNroDoc().equals(propietarioVO.getNroDoc()) && propietarioVO.getTipoPropietario().equals(p.getTipoPropietario())){							
					propietarioVO.addRecoverableError(RodError.EXISTE_PROPIETARIO);
				}
			}
			
				
            // Tiene errores recuperables
			if (propietarioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + propietarioVO.infoString()); 
				saveDemodaErrors(request, propietarioVO);
				return forwardErrorRecoverable(mapping, request, userSession, PropietarioAdapter.NAME, propietarioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (propietarioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + propietarioVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PropietarioAdapter.NAME, propietarioAdapterVO);
			}
			
			// Fue Exitoso

			if(tramiteRAAdapterVO.isEsPropActual()){		
		
				propietarioVO.setTipoPropietario(1);
				tramiteRAAdapterVO.getTramiteRA().setCCantDuenios(tramiteRAAdapterVO.getTramiteRA().getCCantDuenios()+1);				
				tramiteRAAdapterVO.setEsPropActual(false);
		
			}else{
				propietarioVO.setTipoPropietario(2);
			}
			tramiteRAAdapterVO.getTramiteRA().getListPropietario().add(propietarioVO);
			
			return baseVolver(mapping, form, request, response, PropietarioAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PropietarioAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_PROPIETARIO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PropietarioAdapter propietarioAdapterVO = (PropietarioAdapter) userSession.get(PropietarioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (propietarioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PropietarioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PropietarioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(propietarioAdapterVO, request);
			
            // Tiene errores recuperables
			if (propietarioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + propietarioAdapterVO.infoString()); 
				saveDemodaErrors(request, propietarioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PropietarioAdapter.NAME, propietarioAdapterVO);
			}
			
			// llamada al servicio			
			
			PropietarioVO propietarioVO = RodServiceLocator.getTramiteService().validateCreatePropietario(userSession, propietarioAdapterVO.getPropietario());
			
			String selectedId = userSession.getNavModel().getSelectedId();
			TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			for(PropietarioVO propietario: tramiteRAAdapterVO.getTramiteRA().getListPropietario() ){			
					
					if(tramiteRAAdapterVO.isEsPropActual()){
						
						if(propietario.getNroDoc().equals(new Long(selectedId)) && propietario.getTipoPropietario().equals(1)){							
							tramiteRAAdapterVO.setEsPropActual(false);
							break;
						}
					}				
				
			}
		    // Tiene errores recuperables
			if (propietarioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + propietarioAdapterVO.infoString()); 
				saveDemodaErrors(request, propietarioVO);
				return forwardErrorRecoverable(mapping, request, userSession, PropietarioAdapter.NAME, propietarioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (propietarioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + propietarioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PropietarioAdapter.NAME, propietarioAdapterVO);
			}
			
			// Fue Exitoso
			
			return baseVolver(mapping, form, request, response, PropietarioAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PropietarioAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_PROPIETARIO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PropietarioAdapter propietarioAdapterVO = (PropietarioAdapter) userSession.get(PropietarioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (propietarioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PropietarioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PropietarioAdapter.NAME); 
			}

			// Fue Exitoso
			//return forwardConfirmarOk(mapping, request, funcName, PropietarioAdapter.NAME);
			TramiteRAAdapter tramiteRAAdapterVO = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME); 
			
			String selectedId = userSession.getNavModel().getSelectedId();
			List<PropietarioVO> listPropietario = tramiteRAAdapterVO.getTramiteRA().getListPropietario();
			for(PropietarioVO p: listPropietario){
				if(tramiteRAAdapterVO.isEsPropActual()){
					if(p.getNroDoc().equals(new Long(selectedId)) && p.getTipoPropietario().equals(1)){
						tramiteRAAdapterVO.getTramiteRA().getListPropietario().remove(p);
						tramiteRAAdapterVO.getTramiteRA().setCCantDuenios(tramiteRAAdapterVO.getTramiteRA().getCCantDuenios()-1);
						tramiteRAAdapterVO.setEsPropActual(false);
						break;
					}
				}else{
					if(p.getNroDoc().equals(new Long(selectedId)) && p.getTipoPropietario().equals(2)){
						tramiteRAAdapterVO.getTramiteRA().getListPropietario().remove(p);						
						break;
					}
				}
			}
			
			return baseVolver(mapping, form, request, response, PropietarioAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PropietarioAdapter.NAME);
		}
	}
			
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PropietarioAdapter.NAME);
		
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
			String name = PropietarioAdapter.NAME;
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
			PropietarioAdapter propietarioAdapterVO = (PropietarioAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (propietarioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, PropietarioAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			propietarioAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			propietarioAdapterVO = RodServiceLocator.getTramiteService().imprimirPropietario(userSession, propietarioAdapterVO);

			// limpia la lista de reports y la lista de tablas
			propietarioAdapterVO.getReport().getListReport().clear();
			propietarioAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (propietarioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + propietarioAdapterVO.infoString());
				saveDemodaErrors(request, propietarioAdapterVO);				
				request.setAttribute(name, propietarioAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (propietarioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + propietarioAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, propietarioAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = propietarioAdapterVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}
	
	
	public ActionForward paramTipoDoc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PropietarioAdapter propietarioAdapterVO = (PropietarioAdapter) userSession.get(PropietarioAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (propietarioAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PropietarioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PropietarioAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(propietarioAdapterVO, request);
				
	            // Tiene errores recuperables
				if (propietarioAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + propietarioAdapterVO.infoString()); 
					saveDemodaErrors(request, propietarioAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PropietarioAdapter.NAME, propietarioAdapterVO);
				}
				
				// llamada al servicio
				propietarioAdapterVO = RodServiceLocator.getTramiteService().getPropietarioAdapterParamTipoDoc(userSession, propietarioAdapterVO);
				
	            // Tiene errores recuperables
				if (propietarioAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + propietarioAdapterVO.infoString()); 
					saveDemodaErrors(request, propietarioAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PropietarioAdapter.NAME, propietarioAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (propietarioAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + propietarioAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, PropietarioAdapter.NAME, propietarioAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(PropietarioAdapter.NAME, propietarioAdapterVO);
				// Subo el apdater al userSession
				userSession.put(PropietarioAdapter.NAME, propietarioAdapterVO);
				
				return mapping.findForward(RodConstants.FWD_PROPIETARIO_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PropietarioAdapter.NAME);
			}
		}

	public ActionForward paramEstadoCivil(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PropietarioAdapter propietarioAdapterVO = (PropietarioAdapter) userSession.get(PropietarioAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (propietarioAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PropietarioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PropietarioAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(propietarioAdapterVO, request);
				
	            // Tiene errores recuperables
				if (propietarioAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + propietarioAdapterVO.infoString()); 
					saveDemodaErrors(request, propietarioAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PropietarioAdapter.NAME, propietarioAdapterVO);
				}
				
				// llamada al servicio
				propietarioAdapterVO = RodServiceLocator.getTramiteService().getPropietarioAdapterParamEstadoCivil(userSession, propietarioAdapterVO);
				
	            // Tiene errores recuperables
				if (propietarioAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + propietarioAdapterVO.infoString()); 
					saveDemodaErrors(request, propietarioAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PropietarioAdapter.NAME, propietarioAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (propietarioAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + propietarioAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, PropietarioAdapter.NAME, propietarioAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(PropietarioAdapter.NAME, propietarioAdapterVO);
				// Subo el apdater al userSession
				userSession.put(PropietarioAdapter.NAME, propietarioAdapterVO);
				
				return mapping.findForward(RodConstants.FWD_PROPIETARIO_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PropietarioAdapter.NAME);
			}
		}

	public ActionForward paramTipoPropietario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PropietarioAdapter propietarioAdapterVO = (PropietarioAdapter) userSession.get(PropietarioAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (propietarioAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PropietarioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PropietarioAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(propietarioAdapterVO, request);
				
	            // Tiene errores recuperables
				if (propietarioAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + propietarioAdapterVO.infoString()); 
					saveDemodaErrors(request, propietarioAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PropietarioAdapter.NAME, propietarioAdapterVO);
				}
				
				// llamada al servicio
				propietarioAdapterVO = RodServiceLocator.getTramiteService().getPropietarioAdapterParamTipoPropietario(userSession, propietarioAdapterVO);
				
	            // Tiene errores recuperables
				if (propietarioAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + propietarioAdapterVO.infoString()); 
					saveDemodaErrors(request, propietarioAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PropietarioAdapter.NAME, propietarioAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (propietarioAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + propietarioAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, PropietarioAdapter.NAME, propietarioAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(PropietarioAdapter.NAME, propietarioAdapterVO);
				// Subo el apdater al userSession
				userSession.put(PropietarioAdapter.NAME, propietarioAdapterVO);
				
				return mapping.findForward(RodConstants.FWD_PROPIETARIO_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PropietarioAdapter.NAME);
			}
		}

	public ActionForward validarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
				
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PropietarioAdapter propietarioAdapterVO = (PropietarioAdapter) userSession.get(PropietarioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (propietarioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PropietarioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PropietarioAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(propietarioAdapterVO, request);
			
			// Tiene errores recuperables
			if (propietarioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + propietarioAdapterVO.infoString()); 
				saveDemodaErrors(request, propietarioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PropietarioAdapter.NAME, propietarioAdapterVO	);
				}
			
			DemodaUtil.populateVO(propietarioAdapterVO, request);
			
			// Tiene errores recuperables
			if (propietarioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + propietarioAdapterVO.infoString()); 
				saveDemodaErrors(request, propietarioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PropietarioAdapter.NAME, propietarioAdapterVO);
			}
			
			String findForward = RodConstants.FWD_PROPIETARIO_EDIT_ADAPTER;
			
			//propietarioAdapterVO = RodServiceLocator.getTramiteService().validarPersona(propietarioAdapterVO);
			if(propietarioAdapterVO.getPropietario().getSexo().getId()!=-1 && propietarioAdapterVO.getPropietario().getNroDoc()!=null){
				PersonaVO personaVO = new PersonaVO();
				personaVO.setSexo(propietarioAdapterVO.getPropietario().getSexo());
				personaVO.getDocumento().setNumero(propietarioAdapterVO.getPropietario().getNroDoc());
			
				boolean b = PadServiceLocator.getPadPersonaService().existePersonaBySexoyNroDoc(userSession, personaVO);
				
				if(b){
					propietarioAdapterVO.addMessage(RodError.PERSONA_VALIDA);
				}else{
					propietarioAdapterVO.addRecoverableError(RodError.PERSONA_INVALIDA);
				}
			}else{
				propietarioAdapterVO.addRecoverableError(RodError.VALIDAR_PERSONA);
			}
			
			// paso de errores y mensajes		
            // Tiene errores recuperables
			if (propietarioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + propietarioAdapterVO.infoString()); 
				saveDemodaErrors(request, propietarioAdapterVO);
				
				saveDemodaMessages(request, propietarioAdapterVO);
				request.setAttribute(PropietarioAdapter.NAME, propietarioAdapterVO);
				return mapping.findForward(findForward);
				
			}
			
			// Tiene errores no recuperables
			if (propietarioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + propietarioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PropietarioAdapter.NAME, propietarioAdapterVO);
			}
			
			//propietarioAdapterVO.addMessage(RodError.DOMICILIO_VALIDO);
			saveDemodaMessages(request, propietarioAdapterVO);
			request.setAttribute(PropietarioAdapter.NAME, propietarioAdapterVO);
			
			// Fue Exitoso
			// graba el mensaje: Domicilio Valido
			
			return mapping.findForward(findForward);
			
		} catch (Exception exception) {
			exception.printStackTrace();
			return baseException(mapping, request, funcName, exception, PropietarioAdapter.NAME);
		}
		
	}

}
