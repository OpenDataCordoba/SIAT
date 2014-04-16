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

import ar.gov.rosario.siat.bal.iface.model.BalanceAdapter;
import ar.gov.rosario.siat.bal.iface.model.BalanceVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarBalanceDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarBalanceDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_BALANCE, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			BalanceAdapter balanceAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getBalanceAdapterForView(userSession, commonKey)";
					balanceAdapterVO = BalServiceLocator.getBalanceService().getBalanceAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_BALANCE_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getBalanceAdapterForCreate(userSession, commonKey)";
					balanceAdapterVO = BalServiceLocator.getBalanceService().getBalanceAdapterForCreate
						(userSession);
					actionForward = mapping.findForward(BalConstants.FWD_BALANCE_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getBalanceAdapterForUpdate(userSession, commonKey)";
					balanceAdapterVO = BalServiceLocator.getBalanceService().getBalanceAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_BALANCE_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getBalanceAdapterForView(userSession, commonKey)";
					balanceAdapterVO = BalServiceLocator.getBalanceService().getBalanceAdapterForView
						(userSession, commonKey);
					balanceAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.BALANCE_LABEL);
					actionForward = mapping.findForward(BalConstants.FWD_BALANCE_VIEW_ADAPTER);					
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (balanceAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + balanceAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, BalanceAdapter.NAME, balanceAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				balanceAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					BalanceAdapter.NAME + ": " + balanceAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(BalanceAdapter.NAME, balanceAdapterVO);
				// Subo el apdater al userSession
				userSession.put(BalanceAdapter.NAME, balanceAdapterVO);
				
				saveDemodaMessages(request, balanceAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, BalanceAdapter.NAME);
			}
		}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_BALANCE, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				BalanceAdapter balanceAdapterVO = (BalanceAdapter) userSession.get(BalanceAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (balanceAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + BalanceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, BalanceAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(balanceAdapterVO, request);
				
	            // Tiene errores recuperables
				if (balanceAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + balanceAdapterVO.infoString()); 
					saveDemodaErrors(request, balanceAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, BalanceAdapter.NAME, balanceAdapterVO);
				}
				
				// llamada al servicio
				BalanceVO balanceVO = BalServiceLocator.getBalanceService().createBalance(userSession, balanceAdapterVO.getBalance());
				
	            // Tiene errores recuperables
				if (balanceVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + balanceVO.infoString()); 
					saveDemodaErrors(request, balanceVO);
					return forwardErrorRecoverable(mapping, request, userSession, BalanceAdapter.NAME, balanceAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (balanceVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + balanceVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, BalanceAdapter.NAME, balanceAdapterVO);
				}

				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, BalanceAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, BalanceAdapter.NAME);
			}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_BALANCE, BaseSecurityConstants.MODIFICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				BalanceAdapter balanceAdapterVO = (BalanceAdapter) userSession.get(BalanceAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (balanceAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + BalanceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, BalanceAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(balanceAdapterVO, request);
				
	            // Tiene errores recuperables
				if (balanceAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + balanceAdapterVO.infoString()); 
					saveDemodaErrors(request, balanceAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, BalanceAdapter.NAME, balanceAdapterVO);
				}
				
				// llamada al servicio
				BalanceVO balanceVO = BalServiceLocator.getBalanceService().updateBalance(userSession, balanceAdapterVO.getBalance());
				
	            // Tiene errores recuperables
				if (balanceVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + balanceAdapterVO.infoString()); 
					saveDemodaErrors(request, balanceVO);
					return forwardErrorRecoverable(mapping, request, userSession, BalanceAdapter.NAME, balanceAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (balanceVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + balanceAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, BalanceAdapter.NAME, balanceAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, BalanceAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, BalanceAdapter.NAME);
			}
	}

	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_BALANCE, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				BalanceAdapter balanceAdapterVO = (BalanceAdapter) userSession.get(BalanceAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (balanceAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + BalanceAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, BalanceAdapter.NAME); 
				}

				// llamada al servicio
				BalanceVO balanceVO = BalServiceLocator.getBalanceService().deleteBalance
					(userSession, balanceAdapterVO.getBalance());
				
	            // Tiene errores recuperables
				if (balanceVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + balanceAdapterVO.infoString());
					saveDemodaErrors(request, balanceVO);				
					request.setAttribute(BalanceAdapter.NAME, balanceAdapterVO);
					return mapping.findForward(BalConstants.FWD_BALANCE_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (balanceVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + balanceAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, BalanceAdapter.NAME, balanceAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, BalanceAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, BalanceAdapter.NAME);
			}
		}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, BalanceAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, BalanceAdapter.NAME);
			
	}
	

	public ActionForward paramEjercicio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				BalanceAdapter balanceAdapterVO = (BalanceAdapter) userSession.get(BalanceAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (balanceAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + BalanceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, BalanceAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(balanceAdapterVO, request);
				
	            // Tiene errores recuperables
				if (balanceAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + balanceAdapterVO.infoString()); 
					saveDemodaErrors(request, balanceAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, BalanceAdapter.NAME, balanceAdapterVO);
				}
				
				// Llamada al servicio
				balanceAdapterVO = BalServiceLocator.getBalanceService().getBalanceAdapterParamEjercicio(userSession, balanceAdapterVO);
				
	            // Tiene errores recuperables
				if (balanceAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + balanceAdapterVO.infoString()); 
					saveDemodaErrors(request, balanceAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, BalanceAdapter.NAME, balanceAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (balanceAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + balanceAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, BalanceAdapter.NAME, balanceAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(BalanceAdapter.NAME, balanceAdapterVO);
				// Subo el adapter al userSession
				userSession.put(BalanceAdapter.NAME, balanceAdapterVO);
				
				return mapping.findForward(BalConstants.FWD_BALANCE_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, BalanceAdapter.NAME);
			}
	}

	public ActionForward downloadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);

			try {
					
				String fileId = request.getParameter("fileParam");	
				CommonKey commonKey = new CommonKey(fileId);
				
				// Obtenemos el id del archivo seleccionado
				Long idFileCorrida = commonKey.getId();			

				// Obtenemos el nombre del archivo seleccionado mediante una llamada a un servicio
				String fileName = ProServiceLocator.getAdpProcesoService().obtenerFileCorridaName(idFileCorrida);
				
				baseResponseFile(response,fileName);

				log.debug("exit: " + funcName);
				
				
				return null;
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, BalanceAdapter.NAME);
			}
	}
}
