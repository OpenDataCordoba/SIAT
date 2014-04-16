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
import ar.gov.rosario.siat.def.iface.model.RecursoAdapter;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarRecConADecDAction extends BaseDispatchAction {

	
	private Log log = LogFactory.getLog(AdministrarRecConADecDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECCONADEC, act);		
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();

		RecConADecAdapter recConADecAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			// Bajo el adapter del userSession
			RecursoAdapter recursoAdapterVO = (RecursoAdapter) userSession.get(RecursoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (recursoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RecursoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecursoAdapter.NAME); 
			}
			
			CommonKey atributoCommonKey = new CommonKey(navModel.getSelectedId());
			CommonKey recursoCommonKey = new CommonKey(recursoAdapterVO.getRecurso().getId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getRecConADecAdapterForView(userSession, atributoCommonKey)";
				recConADecAdapterVO = DefServiceLocator.getGravamenService().getRecConADecAdapterForView
					(userSession, atributoCommonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECCONADEC_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getRecConADecAdapterForUpdate(userSession, recursoCommonKey, atributoCommonKey)";
				recConADecAdapterVO = DefServiceLocator.getGravamenService().getRecConADecAdapterForView
					(userSession, atributoCommonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECCONADEC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getRecConADecAdapterForDelete(userSession, recursoCommonKey, atributoCommonKey)";
				recConADecAdapterVO = DefServiceLocator.getGravamenService().getRecConADecAdapterForView
					(userSession,  atributoCommonKey);
				recConADecAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.RECGENCUEATRVA_LABEL);				
				actionForward = mapping.findForward(DefConstants.FWD_RECCONADEC_VIEW_ADAPTER);					
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getRecConADecAdapterForCreate(userSession, recursoCommonKey)";
				recConADecAdapterVO = DefServiceLocator.getGravamenService().getRecConADecAdapterForCreate(userSession, recursoCommonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECCONADEC_EDIT_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (recConADecAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + recConADecAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecConADecAdapter.NAME, recConADecAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			recConADecAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + RecConADecAdapter.NAME + ": "+ recConADecAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(RecConADecAdapter.NAME, recConADecAdapterVO);
			// Subo el apdater al userSession
			userSession.put(RecConADecAdapter.NAME, recConADecAdapterVO);
			
			saveDemodaMessages(request, recConADecAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecConADecAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				DefSecurityConstants.ABM_RECCONADEC, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecConADecAdapter recConADecAdapterVO = (RecConADecAdapter) userSession.get(RecConADecAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recConADecAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecConADecAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecConADecAdapter.NAME); 
				}
				DemodaUtil.populateVO(recConADecAdapterVO, request);
				
				// llamada al servicio
				recConADecAdapterVO = DefServiceLocator.getGravamenService().createRecConADec( userSession, recConADecAdapterVO);
				
	            // Tiene errores recuperables
				if (recConADecAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recConADecAdapterVO.infoString()); 
					saveDemodaErrors(request, recConADecAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecConADecAdapter.NAME, recConADecAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recConADecAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recConADecAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecConADecAdapter.NAME, recConADecAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecConADecAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecConADecAdapter.NAME);
			}
		}
	
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECCONADEC, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecConADecAdapter recConADecAdapterVO = (RecConADecAdapter) userSession.get(RecConADecAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recConADecAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecConADecAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecConADecAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recConADecAdapterVO, request);
	
				
	            // Tiene errores recuperables
				if (recConADecAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recConADecAdapterVO.infoString()); 
					saveDemodaErrors(request, recConADecAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							RecConADecAdapter.NAME, recConADecAdapterVO);
				}
				
				// llamada al servicio
				recConADecAdapterVO = DefServiceLocator.getGravamenService().updateRecConADec
					(userSession, recConADecAdapterVO);
				
	            // Tiene errores recuperables
				if (recConADecAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recConADecAdapterVO.infoString()); 
					saveDemodaErrors(request, recConADecAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							RecConADecAdapter.NAME, recConADecAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recConADecAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recConADecAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							RecConADecAdapter.NAME, recConADecAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecConADecAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecConADecAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECCONADEC, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecConADecAdapter recConADecAdapterVO = (RecConADecAdapter) userSession.get(RecConADecAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recConADecAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecConADecAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecConADecAdapter.NAME); 
				}

				// llamada al servicio
				recConADecAdapterVO = DefServiceLocator.getGravamenService().deleteRecConADec
					(userSession, recConADecAdapterVO);
				
	            // Tiene errores recuperables
				if (recConADecAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recConADecAdapterVO.infoString());
					saveDemodaErrors(request, recConADecAdapterVO);				
					request.setAttribute(RecConADecAdapter.NAME, recConADecAdapterVO);
					return mapping.findForward(DefConstants.FWD_RECCONADEC_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (recConADecAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recConADecAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							RecConADecAdapter.NAME, recConADecAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecConADecAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecConADecAdapter.NAME);
			}
		}

	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, RecConADecAdapter.NAME);
	}
	
	public ActionForward agregarValUnRecConADe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String funcName=DemodaUtil.currentMethodName();
		
		return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_VALUNRECCONADEC);
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, RecConADecAdapter.NAME);
			
	}
	
	public ActionForward modificarValUnRecConADe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String funcName=DemodaUtil.currentMethodName();
		
		return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_VALUNRECCONADEC);
	}
	
	public ActionForward verValUnRecConADe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String funcName=DemodaUtil.currentMethodName();
		
		return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_VALUNRECCONADEC);
	}
	
	public ActionForward eliminarValUnRecConADe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String funcName=DemodaUtil.currentMethodName();
		
		return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_VALUNRECCONADEC);
	}
	
	
	public ActionForward paramTipRecConADec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecConADecAdapter recConADecAdapterVO = (RecConADecAdapter) userSession.get(RecConADecAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (recConADecAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecConADecAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecConADecAdapter.NAME); 
				}
	
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recConADecAdapterVO, request);
				
	            // Tiene errores recuperables
				if (recConADecAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recConADecAdapterVO.infoString()); 
					saveDemodaErrors(request, recConADecAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecConADecAdapter.NAME, recConADecAdapterVO);
				}
				
				// llamada al servicio
				recConADecAdapterVO = DefServiceLocator.getGravamenService().getRecConADecAdapterParamTipRecConADec(userSession, recConADecAdapterVO);
				
	            // Tiene errores recuperables
				if (recConADecAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recConADecAdapterVO.infoString()); 
					saveDemodaErrors(request, recConADecAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecConADecAdapter.NAME, recConADecAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recConADecAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recConADecAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecConADecAdapter.NAME, recConADecAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(RecConADecAdapter.NAME, recConADecAdapterVO);
				// Subo el adapter al userSession
				userSession.put(RecConADecAdapter.NAME, recConADecAdapterVO);
				
				return mapping.findForward(DefConstants.FWD_RECCONADEC_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecConADecAdapter.NAME);
			}
		}
}
