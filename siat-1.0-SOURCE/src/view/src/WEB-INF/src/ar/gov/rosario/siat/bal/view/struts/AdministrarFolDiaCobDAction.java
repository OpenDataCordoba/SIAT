//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.FolDiaCobAdapter;
import ar.gov.rosario.siat.bal.iface.model.FolDiaCobColVO;
import ar.gov.rosario.siat.bal.iface.model.FolDiaCobVO;
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
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarFolDiaCobDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarFolDiaCobDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_FOLDIACOB, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			FolDiaCobAdapter folDiaCobAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getFolDiaCobAdapterForView(userSession, commonKey)";
					folDiaCobAdapterVO = BalServiceLocator.getFolioTesoreriaService().getFolDiaCobAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_FOLDIACOB_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getFolDiaCobAdapterForUpdate(userSession, commonKey)";
					folDiaCobAdapterVO = BalServiceLocator.getFolioTesoreriaService().getFolDiaCobAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_FOLDIACOB_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getFolDiaCobAdapterForDelete(userSession, commonKey)";
					folDiaCobAdapterVO = BalServiceLocator.getFolioTesoreriaService().getFolDiaCobAdapterForView
						(userSession, commonKey);
					folDiaCobAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.FOLDIACOB_LABEL);				
					actionForward = mapping.findForward(BalConstants.FWD_FOLDIACOB_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getFolDiaCobAdapterForCreate(userSession)";
					folDiaCobAdapterVO = BalServiceLocator.getFolioTesoreriaService().getFolDiaCobAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_FOLDIACOB_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (folDiaCobAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + folDiaCobAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, FolDiaCobAdapter.NAME, folDiaCobAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				folDiaCobAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + FolDiaCobAdapter.NAME + ": "+ folDiaCobAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(FolDiaCobAdapter.NAME, folDiaCobAdapterVO);
				// Subo el apdater al userSession
				userSession.put(FolDiaCobAdapter.NAME, folDiaCobAdapterVO);
				
				saveDemodaMessages(request, folDiaCobAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FolDiaCobAdapter.NAME);
			}
		}
	

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				BalSecurityConstants.ABM_FOLDIACOB, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				FolDiaCobAdapter folDiaCobAdapterVO = (FolDiaCobAdapter) userSession.get(FolDiaCobAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (folDiaCobAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + FolDiaCobAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, FolDiaCobAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(folDiaCobAdapterVO, request);
				
				for(FolDiaCobColVO folDiaCobCol: folDiaCobAdapterVO.getFolDiaCob().getListFolDiaCobCol()){
					try{
						Double importe = Double.valueOf(request.getParameter("importe"+folDiaCobCol.getTipoCob().getIdView()));
						folDiaCobCol.setImporte(importe);							
					}catch(Exception e){
						folDiaCobAdapterVO.addRecoverableError(BalError.FOLDIACOBCOL_IMPORTE_ERROR);
						break;
					}
				}
				
	            // Tiene errores recuperables
				if (folDiaCobAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folDiaCobAdapterVO.infoString()); 
					saveDemodaErrors(request, folDiaCobAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, FolDiaCobAdapter.NAME, folDiaCobAdapterVO);
				}
				
				// llamada al servicio
				FolDiaCobVO folDiaCobVO = BalServiceLocator.getFolioTesoreriaService().createFolDiaCob(userSession, folDiaCobAdapterVO.getFolDiaCob());
				
	            // Tiene errores recuperables
				if (folDiaCobVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folDiaCobVO.infoString()); 
					saveDemodaErrors(request, folDiaCobVO);
					return forwardErrorRecoverable(mapping, request, userSession, FolDiaCobAdapter.NAME, folDiaCobAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (folDiaCobVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + folDiaCobVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, FolDiaCobAdapter.NAME, folDiaCobAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, FolDiaCobAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FolDiaCobAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_FOLDIACOB, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				FolDiaCobAdapter folDiaCobAdapterVO = (FolDiaCobAdapter) userSession.get(FolDiaCobAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (folDiaCobAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + FolDiaCobAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, FolDiaCobAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(folDiaCobAdapterVO, request);
				
				for(FolDiaCobColVO folDiaCobCol: folDiaCobAdapterVO.getFolDiaCob().getListFolDiaCobCol()){
					try{
						Double importe = Double.valueOf(request.getParameter("importe"+folDiaCobCol.getTipoCob().getIdView()));
						folDiaCobCol.setImporte(importe);							
					}catch(Exception e){
						folDiaCobAdapterVO.addRecoverableError(BalError.FOLDIACOBCOL_IMPORTE_ERROR);
						break;
					}
				}

	            // Tiene errores recuperables
				if (folDiaCobAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folDiaCobAdapterVO.infoString()); 
					saveDemodaErrors(request, folDiaCobAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						FolDiaCobAdapter.NAME, folDiaCobAdapterVO);
				}
				
				// llamada al servicio
				FolDiaCobVO folDiaCobVO = BalServiceLocator.getFolioTesoreriaService().updateFolDiaCob(userSession, folDiaCobAdapterVO.getFolDiaCob());
				
	            // Tiene errores recuperables
				if (folDiaCobVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folDiaCobAdapterVO.infoString()); 
					saveDemodaErrors(request, folDiaCobVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						FolDiaCobAdapter.NAME, folDiaCobAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (folDiaCobVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + folDiaCobAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						FolDiaCobAdapter.NAME, folDiaCobAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, FolDiaCobAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FolDiaCobAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_FOLDIACOB, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				FolDiaCobAdapter folDiaCobAdapterVO = (FolDiaCobAdapter) userSession.get(FolDiaCobAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (folDiaCobAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + FolDiaCobAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, FolDiaCobAdapter.NAME); 
				}

				// llamada al servicio
				FolDiaCobVO folDiaCobVO = BalServiceLocator.getFolioTesoreriaService().deleteFolDiaCob(userSession, folDiaCobAdapterVO.getFolDiaCob());
				
	            // Tiene errores recuperables
				if (folDiaCobVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folDiaCobAdapterVO.infoString());
					saveDemodaErrors(request, folDiaCobVO);				
					request.setAttribute(FolDiaCobAdapter.NAME, folDiaCobAdapterVO);
					return mapping.findForward(BalConstants.FWD_FOLDIACOB_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (folDiaCobVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + folDiaCobAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						FolDiaCobAdapter.NAME, folDiaCobAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, FolDiaCobAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FolDiaCobAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, FolDiaCobAdapter.NAME);
		}
}
