//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

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
import ar.gov.rosario.siat.def.iface.model.RecConADecAdapter;
import ar.gov.rosario.siat.def.iface.model.ValUnRecConADeAdapter;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarValUnRecConADeDAction extends BaseDispatchAction {

	
	private Log log = LogFactory.getLog(AdministrarValUnRecConADeDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_VALUNRECCONADE, act);		
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();
		log.debug("ACT: "+act + "; navModel.getAct= "+navModel.getAct());
		//Agrego esta linea ya que el canAccess me esta modificando el act y no se por que.
		if (!act.equals(navModel.getAct()))navModel.setAct(act);

		RecConADecAdapter recConADecAdapterVO = null;
		ValUnRecConADeAdapter valUnRecConADeAdapterVO=null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			// Bajo el adapter del userSession
			recConADecAdapterVO = (RecConADecAdapter) userSession.get(RecConADecAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (recConADecAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RecConADecAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecConADecAdapter.NAME); 
			}
			
			CommonKey atributoCommonKey = new CommonKey(navModel.getSelectedId());
			CommonKey recConADecCommonKey = new CommonKey(recConADecAdapterVO.getRecConADec().getId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getValUnRecConADeAdapterForView(userSession, atributoCommonKey)";
				valUnRecConADeAdapterVO = DefServiceLocator.getGravamenService().getValUnRecConADeAdapterForView
					(userSession, atributoCommonKey);
				actionForward = mapping.findForward(DefConstants.FWD_VALUNRECCONADE_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getValUnRecConADeAdapterForUpdate(userSession, atributoCommonKey)";
				valUnRecConADeAdapterVO = DefServiceLocator.getGravamenService().getValUnRecConADeAdapterForUpdate
					(userSession, atributoCommonKey);
				actionForward = mapping.findForward(DefConstants.FWD_VALUNRECCONADE_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getValUnRecConADeAdapterForDelete(userSession, recursoCommonKey, atributoCommonKey)";
				valUnRecConADeAdapterVO = DefServiceLocator.getGravamenService().getValUnRecConADeAdapterForView
					(userSession,  atributoCommonKey);
				valUnRecConADeAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.VALUNRECCONADEC_VALORUNITARIO_LABEL);				
				actionForward = mapping.findForward(DefConstants.FWD_VALUNRECCONADE_VIEW_ADAPTER);					
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getValUnRecConADeAdapterForCreate(userSession, recursoCommonKey)";
				valUnRecConADeAdapterVO = DefServiceLocator.getGravamenService().getValUnRecConADeAdapterForCreate(userSession, recConADecCommonKey);
				actionForward = mapping.findForward(DefConstants.FWD_VALUNRECCONADE_EDIT_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (valUnRecConADeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + valUnRecConADeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ValUnRecConADeAdapter.NAME, valUnRecConADeAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			navModel.setAct(BaseConstants.ACT_MODIFICAR);
			valUnRecConADeAdapterVO.setValuesFromNavModel(navModel);
			
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ValUnRecConADeAdapter.NAME + ": "+ valUnRecConADeAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ValUnRecConADeAdapter.NAME, valUnRecConADeAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ValUnRecConADeAdapter.NAME, valUnRecConADeAdapterVO);
			
			saveDemodaMessages(request, valUnRecConADeAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ValUnRecConADeAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				DefSecurityConstants.ABM_VALUNRECCONADE, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ValUnRecConADeAdapter valUnRecConADeAdapterVO = (ValUnRecConADeAdapter) userSession.get(ValUnRecConADeAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (valUnRecConADeAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ValUnRecConADeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ValUnRecConADeAdapter.NAME); 
				}
				DemodaUtil.populateVO(valUnRecConADeAdapterVO, request);
				
				// llamada al servicio
				valUnRecConADeAdapterVO = DefServiceLocator.getGravamenService().createValUnRecConADe( userSession, valUnRecConADeAdapterVO);
				
	            // Tiene errores recuperables
				if (valUnRecConADeAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + valUnRecConADeAdapterVO.infoString()); 
					saveDemodaErrors(request, valUnRecConADeAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ValUnRecConADeAdapter.NAME, valUnRecConADeAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (valUnRecConADeAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + valUnRecConADeAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ValUnRecConADeAdapter.NAME, valUnRecConADeAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, ValUnRecConADeAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ValUnRecConADeAdapter.NAME);
			}
		}
	
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_VALUNRECCONADE, BaseSecurityConstants.MODIFICAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ValUnRecConADeAdapter valUnRecConADeAdapterVO = (ValUnRecConADeAdapter) userSession.get(ValUnRecConADeAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (valUnRecConADeAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ValUnRecConADeAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ValUnRecConADeAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(valUnRecConADeAdapterVO, request);
	
				
	            // Tiene errores recuperables
				if (valUnRecConADeAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + valUnRecConADeAdapterVO.infoString()); 
					saveDemodaErrors(request, valUnRecConADeAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							ValUnRecConADeAdapter.NAME, valUnRecConADeAdapterVO);
				}
				
				// llamada al servicio
				valUnRecConADeAdapterVO = DefServiceLocator.getGravamenService().updateValUnRecConADe
					(userSession, valUnRecConADeAdapterVO);
				
	            // Tiene errores recuperables
				if (valUnRecConADeAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + valUnRecConADeAdapterVO.infoString()); 
					saveDemodaErrors(request, valUnRecConADeAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							ValUnRecConADeAdapter.NAME, valUnRecConADeAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (valUnRecConADeAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + valUnRecConADeAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							ValUnRecConADeAdapter.NAME, valUnRecConADeAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, ValUnRecConADeAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ValUnRecConADeAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_VALUNRECCONADE, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ValUnRecConADeAdapter valUnRecCOnADeAdapterVO = (ValUnRecConADeAdapter) userSession.get(ValUnRecConADeAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (valUnRecCOnADeAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ValUnRecConADeAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ValUnRecConADeAdapter.NAME); 
				}

				// llamada al servicio
				valUnRecCOnADeAdapterVO = DefServiceLocator.getGravamenService().deleteValUnRecConADe
					(userSession, valUnRecCOnADeAdapterVO);
				
	            // Tiene errores recuperables
				if (valUnRecCOnADeAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + valUnRecCOnADeAdapterVO.infoString());
					saveDemodaErrors(request, valUnRecCOnADeAdapterVO);				
					request.setAttribute(ValUnRecConADeAdapter.NAME, valUnRecCOnADeAdapterVO);
					return mapping.findForward(DefConstants.FWD_RECCONADEC_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (valUnRecCOnADeAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + valUnRecCOnADeAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							ValUnRecConADeAdapter.NAME, valUnRecCOnADeAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, ValUnRecConADeAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ValUnRecConADeAdapter.NAME);
			}
		}
	
	
	

	
		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, ValUnRecConADeAdapter.NAME);
		}
		
		
		
		
}
