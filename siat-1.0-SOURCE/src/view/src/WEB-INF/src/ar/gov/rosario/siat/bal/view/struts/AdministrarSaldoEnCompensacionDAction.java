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

import ar.gov.rosario.siat.bal.iface.model.CompensacionAdapter;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarSaldoEnCompensacionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarSaldoEnCompensacionDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_COMPENSACION, act);		

			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			CompensacionAdapter compensacionAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());

				if (navModel.getAct().equals(BalConstants.ACT_INCLUIR_SALDO_A_FAVOR)) {
					stringServicio = "getCompensacionAdapterForIncluirSaldo(userSession, commonKey)";
					compensacionAdapterVO = BalServiceLocator.getCompensacionService().getCompensacionAdapterForIncluirSaldo
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_COMPENSACION_INCLUIR_SALDO_ADAPTER);
				}
				if (navModel.getAct().equals(BalConstants.ACT_EXCLUIR_SALDO_A_FAVOR)) {
					stringServicio = "getCompensacionAdapterForExcluirSaldo(userSession, commonKey)";
					compensacionAdapterVO = BalServiceLocator.getCompensacionService().getCompensacionAdapterForExcluirSaldo
						(userSession, commonKey);
					compensacionAdapterVO.addMessage(BaseError.MSG_QUITAR, BalError.SALDOAFAVOR_LABEL);
					actionForward = mapping.findForward(BalConstants.FWD_COMPENSACION_EXCLUIR_SALDO_ADAPTER);
				}
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (compensacionAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + compensacionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CompensacionAdapter.ADM_SALDO_NAME, compensacionAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				compensacionAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + CompensacionAdapter.ADM_SALDO_NAME + ": "+ compensacionAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(CompensacionAdapter.ADM_SALDO_NAME, compensacionAdapterVO);
				// Subo el apdater al userSession
				userSession.put(CompensacionAdapter.ADM_SALDO_NAME, compensacionAdapterVO);
				
				saveDemodaMessages(request, compensacionAdapterVO);
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CompensacionAdapter.ADM_SALDO_NAME);
			}
		}

	
	public ActionForward incluirSaldoAFavor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SALDOAFAVOR, 
				BaseSecurityConstants.INCLUIR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				CompensacionAdapter compensacionAdapterVO = (CompensacionAdapter) userSession.get(CompensacionAdapter.ADM_SALDO_NAME);
				
				// Si es nulo no se puede continuar
				if (compensacionAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CompensacionAdapter.ADM_SALDO_NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CompensacionAdapter.ADM_SALDO_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(compensacionAdapterVO, request);
				
	            // Tiene errores recuperables
				if (compensacionAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + compensacionAdapterVO.infoString()); 
					saveDemodaErrors(request, compensacionAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, CompensacionAdapter.ADM_SALDO_NAME, compensacionAdapterVO);
				}
				
				// llamada al servicio		
				compensacionAdapterVO = BalServiceLocator.getCompensacionService().incluirSaldoAFavor
						(userSession, compensacionAdapterVO);
				
	            // Tiene errores recuperables
				if (compensacionAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + compensacionAdapterVO.infoString());
					saveDemodaErrors(request, compensacionAdapterVO);				
					request.setAttribute(CompensacionAdapter.ADM_SALDO_NAME, compensacionAdapterVO);
					//return mapping.findForward(BalConstants.FWD_COMPENSACION_INCLUIR_SALDO_ADAPTER);
					return forwardErrorRecoverable(mapping, request, userSession, CompensacionAdapter.ADM_SALDO_NAME, compensacionAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (compensacionAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + compensacionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CompensacionAdapter.ADM_SALDO_NAME, compensacionAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, CompensacionAdapter.ADM_SALDO_NAME);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CompensacionAdapter.ADM_SALDO_NAME);
			}
		}

	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, CompensacionAdapter.ADM_SALDO_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, CompensacionAdapter.ADM_SALDO_NAME);
	}
	
	public ActionForward verSaldoAFavor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_SALDOAFAVOR);
	}

	public ActionForward excluirSaldoAFavor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SALDOAFAVOR, 
				BaseSecurityConstants.EXCLUIR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				CompensacionAdapter compensacionAdapterVO = (CompensacionAdapter) userSession.get(CompensacionAdapter.ADM_SALDO_NAME);
				
				// Si es nulo no se puede continuar
				if (compensacionAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CompensacionAdapter.ADM_SALDO_NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CompensacionAdapter.ADM_SALDO_NAME); 
				}
				// Seteo el id selecionado
				NavModel navModel = userSession.getNavModel();
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				compensacionAdapterVO.setIdSaldoAFavor(commonKey.getId());
		
				// llamada al servicio
				compensacionAdapterVO = BalServiceLocator.getCompensacionService().excluirSaldoAFavor
						(userSession, compensacionAdapterVO);
				
		        // Tiene errores recuperables
				if (compensacionAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + compensacionAdapterVO.infoString());
					saveDemodaErrors(request, compensacionAdapterVO);				
					request.setAttribute(CompensacionAdapter.ADM_SALDO_NAME, compensacionAdapterVO);
					return mapping.findForward(BalConstants.FWD_COMPENSACION_EXCLUIR_SALDO_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (compensacionAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + compensacionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CompensacionAdapter.ADM_SALDO_NAME, compensacionAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(CompensacionAdapter.ADM_SALDO_NAME, compensacionAdapterVO);
				// Subo el apdater al userSession
				userSession.put(CompensacionAdapter.ADM_SALDO_NAME, compensacionAdapterVO);
		
				//return mapping.findForward(BalConstants.FWD_COMPENSACION_ADAPTER);
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, CompensacionAdapter.ADM_SALDO_NAME);
		
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CompensacionAdapter.ADM_SALDO_NAME);
			}
	}
}
