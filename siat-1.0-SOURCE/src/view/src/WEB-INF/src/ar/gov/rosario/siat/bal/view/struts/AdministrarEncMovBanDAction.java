//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import ar.gov.rosario.siat.bal.iface.model.MovBanAdapter;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncMovBanDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncMovBanDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_MOVBAN_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		MovBanAdapter movBanAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getMovBanAdapterForCreate(userSession)";
				movBanAdapterVO = BalServiceLocator.getConciliacionOsirisService().getMovBanAdapterForCreate(userSession);
				actionForward = mapping.findForward(BalConstants.FWD_MOVBAN_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (movBanAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + movBanAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MovBanAdapter.ENC_NAME, movBanAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			movBanAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + MovBanAdapter.ENC_NAME + ": "+ movBanAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(MovBanAdapter.ENC_NAME, movBanAdapterVO);
			// Subo el apdater al userSession
			userSession.put(MovBanAdapter.ENC_NAME, movBanAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MovBanAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			BalSecurityConstants.ABM_MOVBAN_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MovBanAdapter movBanAdapterVO = (MovBanAdapter) userSession.get(MovBanAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (movBanAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MovBanAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MovBanAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(movBanAdapterVO, request);
			
			Collection<FormFile> col = form.getMultipartRequestHandler().getFileElements().values();
			FormFile file = col.iterator().next();
			
			// ---> Valida que sea un archivo correcto
			boolean archivoCorrecto = true;
			
			if(file==null || file.getFileData()==null || StringUtil.isNullOrEmpty(file.getFileName()) ||
					file.getFileSize()==0){
				movBanAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.MOVBAN_UPLOAD_FILE_LABEL);
				archivoCorrecto=false;
			}else{
				try{
					String ext = file.getFileName().substring(file.getFileName().lastIndexOf(".")+1);
					boolean contenTypeCorrecto = file.getContentType().equals("text/plain");
					boolean extCorrecta = ext.equals("txt");
					if(!contenTypeCorrecto || !extCorrecta){
						movBanAdapterVO.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, BalError.MOVBAN_UPLOAD_FILE_LABEL);
						archivoCorrecto=false;
					}
				}catch(IndexOutOfBoundsException e){
					archivoCorrecto=false;
				}
			}
			
			if(!archivoCorrecto){
				saveDemodaErrors(request, movBanAdapterVO);
				// Envio el VO al request
				request.setAttribute(MovBanAdapter.NAME, movBanAdapterVO);
				// Subo el apdater al userSession
				userSession.put(MovBanAdapter.NAME, movBanAdapterVO);
	
				return forwardErrorRecoverable(mapping, request, userSession, MovBanAdapter.ENC_NAME, movBanAdapterVO);
			}
			// <--- Valida que sea un archivo correcto
			
			// Se setean los datos del archivo en el adapter
			movBanAdapterVO.setFileData(file.getFileData());
			movBanAdapterVO.setFileName(file.getFileName());
			
            // Tiene errores recuperables
			if (movBanAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + movBanAdapterVO.infoString()); 
				saveDemodaErrors(request, movBanAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, MovBanAdapter.ENC_NAME, movBanAdapterVO);
			}
			
			// llamada al servicio
			movBanAdapterVO = BalServiceLocator.getConciliacionOsirisService().createMovBan(userSession, movBanAdapterVO);
			
            // Tiene errores recuperables
			if (movBanAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + movBanAdapterVO.infoString()); 
				saveDemodaErrors(request, movBanAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, MovBanAdapter.ENC_NAME, movBanAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (movBanAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + movBanAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MovBanAdapter.ENC_NAME, movBanAdapterVO);
			}
	
			// lo dirijo al searchPage				
			return forwardConfirmarOk(mapping, request, funcName, MovBanAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MovBanAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, MovBanAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, MovBanAdapter.ENC_NAME);
		
	}
	
}
	
