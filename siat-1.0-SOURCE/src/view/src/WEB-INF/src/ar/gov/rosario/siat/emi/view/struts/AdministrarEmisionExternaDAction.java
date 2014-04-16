//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.view.struts;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.emi.iface.model.EmisionExternaAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionExternaSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarEmisionExternaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEmisionExternaDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		String act = getCurrentAct(request);
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ADM_EMISION_EXTERNA, funcName); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		EmisionExternaAdapter emisionExternaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			
			if (act.equals(BaseConstants.ACT_VER)) {
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				stringServicio = "getEmisionMasAdapterForView(userSession, commonKey)";
				emisionExternaAdapterVO = EmiServiceLocator.getEmisionService().getEmisionExternaAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_EMISIONEXTERNA_VIEW_ADAPTER);
			} else if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				stringServicio = "getEmisionMasAdapterForView(userSession, commonKey)";
				emisionExternaAdapterVO = EmiServiceLocator.getEmisionService().getEmisionExternaAdapterForView(userSession, commonKey);				
				emisionExternaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EmiError.EMISION_LABEL);
				actionForward = mapping.findForward(EmiConstants.FWD_EMISIONEXTERNA_VIEW_ADAPTER);				
			} else {
				emisionExternaAdapterVO = new EmisionExternaAdapter();
				actionForward = mapping.findForward(EmiConstants.FWD_EMISIONEXTERNA_UPLOADADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (emisionExternaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + emisionExternaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionExternaAdapter.NAME, emisionExternaAdapterVO);
			}	
			
			// Seteo los valores de navegacion en el adapter
			emisionExternaAdapterVO.setValuesFromNavModel(userSession.getNavModel());

			// Envio el VO al request
			request.setAttribute(EmisionExternaAdapter.NAME, emisionExternaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EmisionExternaAdapter.NAME, emisionExternaAdapterVO);			 			
			
			saveDemodaMessages(request, emisionExternaAdapterVO);	
			
			return actionForward;
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionExternaSearchPage.NAME);
		}	
	}
	
	public ActionForward upload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ADM_EMISION_EXTERNA, funcName); 
		if (userSession == null) return forwardErrorSession(request);

		try {
			EmisionExternaAdapter emisionExternaAdapterVO = (EmisionExternaAdapter) userSession.get(EmisionExternaAdapter.NAME);
			
			DemodaUtil.populateVO(emisionExternaAdapterVO, request);
	
			Collection<FormFile> col = form.getMultipartRequestHandler().getFileElements().values();
			FormFile file = col.iterator().next();
			
			// ---> Valida que sea un archivo correcto
			boolean archivoCorrecto = true;
			
			if(file==null || file.getFileData()==null || StringUtil.isNullOrEmpty(file.getFileName()) ||
					file.getFileSize()==0){
				emisionExternaAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EmiError.EMISIONEXTERNA_UPLOAD_FILE_LABEL);
				archivoCorrecto=false;
			}else{
				try{
					String ext = file.getFileName().substring(file.getFileName().lastIndexOf(".")+1);
					boolean contenTypeCorrecto = file.getContentType().equals("text/plain");
					boolean extCorrecta = ext.equals("txt");
					if(!contenTypeCorrecto || !extCorrecta){
						emisionExternaAdapterVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, EmiError.EMISIONEXTERNA_UPLOAD_FILE_LABEL);
						archivoCorrecto=false;
					}
				}catch(IndexOutOfBoundsException e){
					archivoCorrecto=false;
				}
			}
			
			if(!archivoCorrecto){
				saveDemodaErrors(request, emisionExternaAdapterVO);
				// Envio el VO al request
				request.setAttribute(EmisionExternaAdapter.NAME, emisionExternaAdapterVO);
				// Subo el apdater al userSession
				userSession.put(EmisionExternaAdapter.NAME, emisionExternaAdapterVO);
	
				return forwardErrorRecoverable(mapping, request, userSession, EmisionExternaAdapter.NAME, emisionExternaAdapterVO);
			}
			// <--- Valida que sea un archivo correcto
			
			// Se setean los datos del archivo en el adapter
			emisionExternaAdapterVO.setFileData(file.getFileData());
			emisionExternaAdapterVO.setFileName(file.getFileName());
			
			// Llamada al servicio 
			emisionExternaAdapterVO = EmiServiceLocator.getEmisionService().emisionExternaUploadFile(userSession, emisionExternaAdapterVO);
	
	        // Tiene errores recuperables
			if (emisionExternaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionExternaAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionExternaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionExternaAdapter.NAME, emisionExternaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionExternaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionExternaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionExternaAdapter.NAME, emisionExternaAdapterVO);
			}		
		
					
			// Envio el VO al request
			request.setAttribute(EmisionExternaAdapter.NAME, emisionExternaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EmisionExternaAdapter.NAME, emisionExternaAdapterVO);
	
			log.debug(funcName + "exit");
			return mapping.findForward(EmiConstants.FWD_EMISIONEXTERNA_UPLOADADAPTER);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionExternaSearchPage.NAME);
		}	
	}

	public ActionForward validar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ADM_EMISION_EXTERNA, funcName); 
		if (userSession == null) return forwardErrorSession(request);

		try {
			
		
			EmisionExternaAdapter emisionExternaAdapterVO = (EmisionExternaAdapter) userSession.get(EmisionExternaAdapter.NAME);
			
			// Llamada al servicio 
			emisionExternaAdapterVO = EmiServiceLocator.getEmisionService().emisionExternaValidarFile(userSession, emisionExternaAdapterVO);
	
	        // Tiene errores recuperables
			if (emisionExternaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionExternaAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionExternaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionExternaAdapter.NAME, emisionExternaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionExternaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionExternaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionExternaAdapter.NAME, emisionExternaAdapterVO);
			}			
			
			saveDemodaMessages(request, emisionExternaAdapterVO);
			// Envio el VO al request
			request.setAttribute(EmisionExternaAdapter.NAME, emisionExternaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EmisionExternaAdapter.NAME, emisionExternaAdapterVO);

			log.debug("exit");
			return mapping.findForward(EmiConstants.FWD_EMISIONEXTERNA_UPLOADADAPTER);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionExternaSearchPage.NAME);
		}	
	}

	public ActionForward continuar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ADM_EMISION_EXTERNA, funcName); 
		if (userSession == null) return forwardErrorSession(request);

		try {
		
			EmisionExternaAdapter emisionExternaAdapterVO = (EmisionExternaAdapter) userSession.get(EmisionExternaAdapter.NAME);
		
			// Llamada al servicio 
			emisionExternaAdapterVO = EmiServiceLocator.getEmisionService().createEmisionExterna(userSession, emisionExternaAdapterVO);
	
	        // Tiene errores recuperables
			if (emisionExternaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionExternaAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionExternaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionExternaAdapter.NAME, emisionExternaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionExternaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionExternaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionExternaAdapter.NAME, emisionExternaAdapterVO);
			}			
			
			
			log.debug("exit");
			return forwardConfirmarOk(mapping, request, funcName, EmisionExternaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionExternaSearchPage.NAME);
		}	
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISION_EXTERNA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EmisionExternaAdapter emisionExternaAdapterVO = (EmisionExternaAdapter) userSession.get(EmisionExternaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionExternaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionExternaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionExternaAdapter.NAME); 
			}

			// llamada al servicio
			EmisionVO emisionVO = EmiServiceLocator.getEmisionService().deleteEmisionExterna(userSession, emisionExternaAdapterVO.getEmision());
			
            // Tiene errores recuperables
			if (emisionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionExternaAdapterVO.infoString());
				saveDemodaErrors(request, emisionVO);				
				request.setAttribute(EmisionExternaAdapter.NAME, emisionExternaAdapterVO);
				return mapping.findForward(EmiConstants.FWD_EMISIONEXTERNA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (emisionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionExternaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionExternaAdapter.NAME, emisionExternaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EmisionExternaAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionExternaAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		return baseVolver(mapping, form, request, response, EmisionExternaAdapter.NAME);
	}
}
