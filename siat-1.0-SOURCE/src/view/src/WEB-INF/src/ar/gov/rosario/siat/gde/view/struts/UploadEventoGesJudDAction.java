//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

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
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.EmisionExternaAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class UploadEventoGesJudDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(UploadEventoGesJudDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.UPLOAD_EVENTO_GESJUD, GdeSecurityConstants.MTD_UPLOAD); 
			if (userSession == null) return forwardErrorSession(request);
			
			EmisionExternaAdapter uploadEventoAdapterVO = new EmisionExternaAdapter();
			
			// Seteo los valores de navegacion en el adapter
			uploadEventoAdapterVO.setValuesFromNavModel(userSession.getNavModel());

			// Envio el VO al request
			request.setAttribute(EmisionExternaAdapter.NAME, uploadEventoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EmisionExternaAdapter.NAME, uploadEventoAdapterVO);			 			
			
			return mapping.findForward("uploadEventoAdapter");

	}
	
	public ActionForward upload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.UPLOAD_EVENTO_GESJUD, GdeSecurityConstants.MTD_UPLOAD); 
		if (userSession == null) return forwardErrorSession(request);

		EmisionExternaAdapter uploadEventoAdapterVO = (EmisionExternaAdapter) userSession.get(EmisionExternaAdapter.NAME);
		
		DemodaUtil.populateVO(uploadEventoAdapterVO, request);

		Collection<FormFile> col = form.getMultipartRequestHandler().getFileElements().values();
		FormFile file = col.iterator().next();
		
		// ---> Valida que sea un archivo correcto
		boolean archivoCorrecto = true;
		
		if(file==null || file.getFileData()==null || StringUtil.isNullOrEmpty(file.getFileName()) ||
				file.getFileSize()==0){
			uploadEventoAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.UPLOAD_EVENTO_FILE_LABEL);
			archivoCorrecto=false;
		}else{
			try{
				String ext = file.getFileName().substring(file.getFileName().lastIndexOf(".")+1);
				boolean contenTypeCorrecto = file.getContentType().equals("text/plain");
				boolean extCorrecta = ext.equals("txt");
				if(!contenTypeCorrecto || !extCorrecta){
					uploadEventoAdapterVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, GdeError.UPLOAD_EVENTO_FILE_LABEL);
					archivoCorrecto=false;
				}
			}catch(IndexOutOfBoundsException e){
				archivoCorrecto=false;
			}
		}
		
		if(!archivoCorrecto){
			saveDemodaErrors(request, uploadEventoAdapterVO);
			// Envio el VO al request
			request.setAttribute(EmisionExternaAdapter.NAME, uploadEventoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EmisionExternaAdapter.NAME, uploadEventoAdapterVO);

			return forwardErrorRecoverable(mapping, request, userSession, EmisionExternaAdapter.NAME, uploadEventoAdapterVO);
		}
		// <--- Valida que sea un archivo correcto
		
		// Se setean los datos del archivo en el adapter
		uploadEventoAdapterVO.setFileData(file.getFileData());
		uploadEventoAdapterVO.setFileName(file.getFileName());
		
		// Llamada al servicio 
		uploadEventoAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().uploadFileEventosGesJud(userSession, uploadEventoAdapterVO);

        // Tiene errores recuperables
		if (uploadEventoAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + uploadEventoAdapterVO.infoString()); 
			saveDemodaErrors(request, uploadEventoAdapterVO);
			return forwardErrorRecoverable(mapping, request, userSession, EmisionExternaAdapter.NAME, uploadEventoAdapterVO);
		}
		
		// Tiene errores no recuperables
		if (uploadEventoAdapterVO.hasErrorNonRecoverable()) {
			log.error("error en: "  + funcName + ": " + uploadEventoAdapterVO.errorString()); 
			return forwardErrorNonRecoverable(mapping, request, funcName, EmisionExternaAdapter.NAME, uploadEventoAdapterVO);
		}		
	
				
		// Envio el VO al request
		request.setAttribute(EmisionExternaAdapter.NAME, uploadEventoAdapterVO);
		// Subo el apdater al userSession
		userSession.put(EmisionExternaAdapter.NAME, uploadEventoAdapterVO);

		log.debug("exit");
		return mapping.findForward("uploadEventoAdapter");
	}

	public ActionForward imprimirLogAnalisis(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.UPLOAD_EVENTO_GESJUD, GdeSecurityConstants.MTD_UPLOAD); 
		if (userSession == null) return forwardErrorSession(request);

		EmisionExternaAdapter uploadEventoAdapterVO = (EmisionExternaAdapter) userSession.get(EmisionExternaAdapter.NAME);
		
		DemodaUtil.populateVO(uploadEventoAdapterVO, request);
				
        // Tiene errores recuperables
		if (uploadEventoAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + uploadEventoAdapterVO.infoString()); 
			saveDemodaErrors(request, uploadEventoAdapterVO);
			return forwardErrorRecoverable(mapping, request, userSession, EmisionExternaAdapter.NAME, uploadEventoAdapterVO);
		}
		
		// Tiene errores no recuperables
		if (uploadEventoAdapterVO.hasErrorNonRecoverable()) {
			log.error("error en: "  + funcName + ": " + uploadEventoAdapterVO.errorString()); 
			return forwardErrorNonRecoverable(mapping, request, funcName, EmisionExternaAdapter.NAME, uploadEventoAdapterVO);
		}			
				
		// Envio el VO al request
		request.setAttribute(EmisionExternaAdapter.NAME, uploadEventoAdapterVO);
		// Subo el apdater al userSession
		userSession.put(EmisionExternaAdapter.NAME, uploadEventoAdapterVO);

		log.debug("exit");
		baseResponseFile(response, uploadEventoAdapterVO.getFileNameLogAnalisis());
		return null;
	}

	public ActionForward cargarEventos(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.UPLOAD_EVENTO_GESJUD, GdeSecurityConstants.MTD_UPLOAD); 
		if (userSession == null) return forwardErrorSession(request);

		EmisionExternaAdapter uploadEventoAdapterVO = (EmisionExternaAdapter) userSession.get(EmisionExternaAdapter.NAME);
		
		DemodaUtil.populateVO(uploadEventoAdapterVO, request);
		
		// Llamada al servicio 
		uploadEventoAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().cargarEventosGesJud(userSession, uploadEventoAdapterVO);

        // Tiene errores recuperables
		if (uploadEventoAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + uploadEventoAdapterVO.infoString()); 
			saveDemodaErrors(request, uploadEventoAdapterVO);
			return forwardErrorRecoverable(mapping, request, userSession, EmisionExternaAdapter.NAME, uploadEventoAdapterVO);
		}
		
		// Tiene errores no recuperables
		if (uploadEventoAdapterVO.hasErrorNonRecoverable()) {
			log.error("error en: "  + funcName + ": " + uploadEventoAdapterVO.errorString()); 
			return forwardErrorNonRecoverable(mapping, request, funcName, EmisionExternaAdapter.NAME, uploadEventoAdapterVO);
		}			
				
		// Envio el VO al request
		request.setAttribute(EmisionExternaAdapter.NAME, uploadEventoAdapterVO);
		// Subo el apdater al userSession
		userSession.put(EmisionExternaAdapter.NAME, uploadEventoAdapterVO);

		log.debug("exit");
		return mapping.findForward("uploadEventoAdapter");
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, EmisionExternaAdapter.NAME);
		}
}
