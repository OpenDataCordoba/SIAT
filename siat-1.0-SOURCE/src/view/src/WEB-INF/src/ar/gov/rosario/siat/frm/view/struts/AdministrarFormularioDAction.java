//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.view.struts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import ar.gov.rosario.siat.frm.iface.model.FormularioAdapter;
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;
import ar.gov.rosario.siat.frm.iface.service.FrmServiceLocator;
import ar.gov.rosario.siat.frm.iface.util.FrmError;
import ar.gov.rosario.siat.frm.iface.util.FrmSecurityConstants;
import ar.gov.rosario.siat.frm.view.util.FrmConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarFormularioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarFormularioDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, FrmSecurityConstants.ABM_FORMULARIO, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		FormularioAdapter formularioAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getFormularioAdapterForView(userSession, commonKey)";
				formularioAdapterVO = FrmServiceLocator.getFormularioService().getFormularioAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(FrmConstants.FWD_FORMULARIO_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getFormularioAdapterForUpdate(userSession, commonKey)";
				formularioAdapterVO = FrmServiceLocator.getFormularioService().getFormularioAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(FrmConstants.FWD_FORMULARIO_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getFormularioAdapterForDelete(userSession, commonKey)";
				formularioAdapterVO = FrmServiceLocator.getFormularioService().getFormularioAdapterForView
					(userSession, commonKey);
				formularioAdapterVO.addMessage(BaseError.MSG_ELIMINAR, FrmError.FORMULARIO_LABEL);
				actionForward = mapping.findForward(FrmConstants.FWD_FORMULARIO_VIEW_ADAPTER);					
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getFormularioAdapterForView(userSession)";
				formularioAdapterVO = FrmServiceLocator.getFormularioService().getFormularioAdapterForView
					(userSession, commonKey);
				formularioAdapterVO.addMessage(BaseError.MSG_ACTIVAR, FrmError.FORMULARIO_LABEL);
				actionForward = mapping.findForward(FrmConstants.FWD_FORMULARIO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getFormularioAdapterForView(userSession)";
				formularioAdapterVO = FrmServiceLocator.getFormularioService().getFormularioAdapterForView
					(userSession, commonKey);
				formularioAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, FrmError.FORMULARIO_LABEL);			
				actionForward = mapping.findForward(FrmConstants.FWD_FORMULARIO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (formularioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + formularioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FormularioAdapter.NAME, formularioAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			formularioAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				FormularioAdapter.NAME + ": " + formularioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(FormularioAdapter.NAME, formularioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(FormularioAdapter.NAME, formularioAdapterVO);
			
			saveDemodaMessages(request, formularioAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FormularioAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			FrmConstants.ACTION_ADMINISTRAR_ENC_FORMULARIO, BaseConstants.ACT_MODIFICAR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, FrmSecurityConstants.ABM_FORMULARIO, 
			BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FormularioAdapter formularioAdapterVO = (FormularioAdapter) userSession.get(FormularioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (formularioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FormularioAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FormularioAdapter.NAME); 
			}

			// llamada al servicio
			FormularioVO formularioVO = FrmServiceLocator.getFormularioService().deleteFormulario
				(userSession, formularioAdapterVO.getFormulario());
			
            // Tiene errores recuperables
			if (formularioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formularioAdapterVO.infoString());
				saveDemodaErrors(request, formularioVO);				
				request.setAttribute(FormularioAdapter.NAME, formularioAdapterVO);
				return mapping.findForward(FrmConstants.FWD_FORMULARIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (formularioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + formularioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FormularioAdapter.NAME, formularioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FormularioAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FormularioAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, FrmSecurityConstants.ABM_FORMULARIO, 
			BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FormularioAdapter formularioAdapterVO = (FormularioAdapter) userSession.get(FormularioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (formularioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FormularioAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FormularioAdapter.NAME); 
			}

			// llamada al servicio
			FormularioVO formularioVO = FrmServiceLocator.getFormularioService().activarFormulario
				(userSession, formularioAdapterVO.getFormulario());
			
            // Tiene errores recuperables
			if (formularioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formularioAdapterVO.infoString());
				saveDemodaErrors(request, formularioVO);				
				request.setAttribute(FormularioAdapter.NAME, formularioAdapterVO);
				return mapping.findForward(FrmConstants.FWD_FORMULARIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (formularioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + formularioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FormularioAdapter.NAME, formularioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FormularioAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FormularioAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, FrmSecurityConstants.ABM_FORMULARIO, 
			BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FormularioAdapter formularioAdapterVO = (FormularioAdapter) userSession.get(FormularioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (formularioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FormularioAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FormularioAdapter.NAME); 
			}

			// llamada al servicio
			FormularioVO formularioVO = FrmServiceLocator.getFormularioService().desactivarFormulario
				(userSession, formularioAdapterVO.getFormulario());
			
            // Tiene errores recuperables
			if (formularioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formularioAdapterVO.infoString());
				saveDemodaErrors(request, formularioVO);				
				request.setAttribute(FormularioAdapter.NAME, formularioAdapterVO);
				return mapping.findForward(FrmConstants.FWD_FORMULARIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (formularioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + formularioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FormularioAdapter.NAME, formularioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FormularioAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FormularioAdapter.NAME);
		}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, FrmSecurityConstants.ABM_FORMULARIO, 
				BaseSecurityConstants.MODIFICAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				FormularioAdapter formularioAdapterVO = (FormularioAdapter) userSession.get(FormularioAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (formularioAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + FormularioAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, FormularioAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(formularioAdapterVO, request);
				
				// llamada al servicio
				FormularioVO formularioVO = FrmServiceLocator.getFormularioService().updateFormulario
					(userSession, formularioAdapterVO.getFormulario());
				
	            // Tiene errores recuperables
				if (formularioVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + formularioAdapterVO.infoString());
					saveDemodaErrors(request, formularioVO);				
					request.setAttribute(FormularioAdapter.NAME, formularioAdapterVO);
					return mapping.findForward(FrmConstants.FWD_FORMULARIO_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (formularioVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + formularioAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, FormularioAdapter.NAME, formularioAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, FormularioAdapter.NAME);
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FormularioAdapter.NAME);
			}	
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, FormularioAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, FormularioAdapter.NAME);
		
	}
	
	public ActionForward irTest(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, FrmSecurityConstants.ABM_FORMULARIO, 
				BaseSecurityConstants.MODIFICAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				FormularioAdapter formularioAdapterVO = (FormularioAdapter) userSession.get(FormularioAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (formularioAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + FormularioAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, FormularioAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(formularioAdapterVO, request);
				
				NavModel navModel = userSession.getNavModel();
				
				navModel.setSelectedId(formularioAdapterVO.getFormulario().getId().toString());

				formularioAdapterVO.setValuesFromNavModel(navModel);
								
				// Envio el VO al request
				request.setAttribute(FormularioAdapter.NAME, formularioAdapterVO);
				// Subo el apdater al userSession
				userSession.put(FormularioAdapter.NAME, formularioAdapterVO);
				
				//Setea la variable en el request para que abra el pop-up con el PDF, TXT, etc
				request.setAttribute("abrirPopUpTest", "SI");
				
				//return forwardConfirmarOk(mapping, request, funcName, FormularioAdapter.NAME);
				return mapping.findForward(FrmConstants.FWD_FORMULARIO_ADAPTER);
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FormularioAdapter.NAME);
			}	
	}
	
	public ActionForward test(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, FrmSecurityConstants.ABM_FORMULARIO, 
				BaseSecurityConstants.MODIFICAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				FormularioAdapter formularioAdapterVO = (FormularioAdapter) userSession.get(FormularioAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (formularioAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + FormularioAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, FormularioAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(formularioAdapterVO, request);
				
				FormularioVO formularioVO = formularioAdapterVO.getFormulario();
				PrintModel print = new PrintModel();
				Integer idFormatoSalidaSelec = Integer.valueOf(formularioAdapterVO.getIdFormatoSalidaSelec());
				
				FormatoSalida formatoSalida = FormatoSalida.getById(idFormatoSalidaSelec);
				
				print.setRenderer(idFormatoSalidaSelec);
				print.putCabecera("FileSharePath", SiatParam.getString("FileSharePath"));
				print.setExcludeFileName("/publico/general/reportes/default.exclude");
				print.setXmlData(formularioVO.getXmlTest());
				
				// Si es una unica linea, se toma como path, relativo a /tmp
				if (formularioVO.getXsl() != null){
					if (formularioVO.getXsl().length() < 100){
						String unidad = File.separator.equals("\\") ? "c:\\" : "";
						
						File file = new File(unidad + "/tmp/" + formularioVO.getXsl());
						
		    			StringBuffer contents = new StringBuffer();
		    			
		    			BufferedReader input =  new BufferedReader(new FileReader(file));
					      try {
					        String line = null;
					      
					        while (( line = input.readLine()) != null){
					          contents.append(line);
					          contents.append(System.getProperty("line.separator"));
					        }
					      }
					      finally {
					        input.close();
					      }
					      
					     if (formatoSalida.getEsPDF()){
					    	 print.setXslPdfString(contents.toString());
					     }else if(formatoSalida.getEsTXT()){
					    	 // si es txt 
					    	 print.setXslTxtString(contents.toString());
					     }
						
					} else {
						 if (formatoSalida.getEsPDF()){
					    	 print.setXslPdfString(formularioVO.getXsl());
					     }else if(formatoSalida.getEsTXT()){
					    	 // si es txt 
					    	 print.setXslTxtString(formularioVO.getXslTxt());
					     }
					}
				}
								
				// Envio el VO al request
				request.setAttribute(FormularioAdapter.NAME, formularioAdapterVO);
				// Subo el apdater al userSession
				userSession.put(FormularioAdapter.NAME, formularioAdapterVO);
				
				baseResponsePrintModel(response, print);				
			} catch (Exception exception) {
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT); //baseException(mapping, request, funcName, exception, null);
			}
			return null;			
	}
	
	// Metodos relacionados ForCam
	public ActionForward verForCam(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, FrmConstants.ACTION_ADMINISTRAR_FORCAM);

	}

	public ActionForward modificarForCam(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, FrmConstants.ACTION_ADMINISTRAR_FORCAM);

	}

	public ActionForward eliminarForCam(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, FrmConstants.ACTION_ADMINISTRAR_FORCAM);

	}
	
	public ActionForward agregarForCam(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, FrmConstants.ACTION_ADMINISTRAR_FORCAM);
		
	}
	
}
	
