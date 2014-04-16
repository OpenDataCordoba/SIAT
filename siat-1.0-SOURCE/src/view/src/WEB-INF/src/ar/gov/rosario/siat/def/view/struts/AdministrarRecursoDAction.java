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
import ar.gov.rosario.siat.def.iface.model.RecursoAdapter;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarRecursoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRecursoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECURSO, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			RecursoAdapter recursoAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getRecursoAdapterForView(userSession, commonKey)";
					recursoAdapterVO = DefServiceLocator.getGravamenService().getRecursoAdapterForView
						(userSession, commonKey);
					if (recursoAdapterVO.isEsNoTrib())
						actionForward = mapping.findForward(DefConstants.FWD_RECURSONOTRIB_VIEW_ADAPTER);
					else
						actionForward = mapping.findForward(DefConstants.FWD_RECURSO_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getRecursoAdapterForView(userSession, commonKey)";
					recursoAdapterVO = DefServiceLocator.getGravamenService().getRecursoAdapterForView
						(userSession, commonKey);
					if (recursoAdapterVO.isEsNoTrib())
						actionForward = mapping.findForward(DefConstants.FWD_RECURSO_NOTRIB_ADAPTER);
					else
						actionForward = mapping.findForward(DefConstants.FWD_RECURSO_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getRecursoAdapterForDelete(userSession, commonKey)";
					recursoAdapterVO = DefServiceLocator.getGravamenService().getRecursoAdapterForView
						(userSession, commonKey);
					recursoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.RECURSO_LABEL);
					actionForward = mapping.findForward(DefConstants.FWD_RECURSO_VIEW_ADAPTER);					
				}
				if (act.equals(BaseConstants.ACT_ACTIVAR)) {
					stringServicio = "getRecursoAdapterForView(userSession)";
					recursoAdapterVO = DefServiceLocator.getGravamenService().getRecursoAdapterForView
						(userSession, commonKey);
					recursoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.RECURSO_LABEL);					
					actionForward = mapping.findForward(DefConstants.FWD_RECURSO_VIEW_ADAPTER);				
				}
				if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
					stringServicio = "getRecursoAdapterForView(userSession)";
					recursoAdapterVO = DefServiceLocator.getGravamenService().getRecursoAdapterForView
						(userSession, commonKey);
					recursoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.RECURSO_LABEL);				
					actionForward = mapping.findForward(DefConstants.FWD_RECURSO_VIEW_ADAPTER);				
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (recursoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + recursoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAdapter.NAME, recursoAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				recursoAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					RecursoAdapter.NAME + ": " + recursoAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(RecursoAdapter.NAME, recursoAdapterVO);
				// Subo el apdater al userSession
				userSession.put(RecursoAdapter.NAME, recursoAdapterVO);
				
				saveDemodaMessages(request, recursoAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecursoAdapter.NAME);
			}
		}
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				DefConstants.ACTION_ADMINISTRAR_ENCRECURSO, BaseConstants.ACT_MODIFICAR);

		}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECURSO, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecursoAdapter recursoAdapterVO = (RecursoAdapter) userSession.get(RecursoAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recursoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecursoAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecursoAdapter.NAME); 
				}

				// llamada al servicio
				RecursoVO recursoVO = DefServiceLocator.getGravamenService().deleteRecurso
					(userSession, recursoAdapterVO.getRecurso());
				
	            // Tiene errores recuperables
				if (recursoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString());
					saveDemodaErrors(request, recursoVO);				
					request.setAttribute(RecursoAdapter.NAME, recursoAdapterVO);
					return mapping.findForward(DefConstants.FWD_RECURSO_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (recursoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recursoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAdapter.NAME, recursoAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecursoAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecursoAdapter.NAME);
			}
		}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECURSO, 
				BaseSecurityConstants.ACTIVAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecursoAdapter recursoAdapterVO = (RecursoAdapter) userSession.get(RecursoAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recursoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecursoAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecursoAdapter.NAME); 
				}
				
				// llamada al servicio
				RecursoVO recursoVO = DefServiceLocator.getGravamenService().activarRecurso
					(userSession, recursoAdapterVO.getRecurso());
				
	            // Tiene errores recuperables
				if (recursoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString());
					saveDemodaErrors(request, recursoVO);				
					request.setAttribute(RecursoAdapter.NAME, recursoAdapterVO);
					return mapping.findForward(DefConstants.FWD_RECURSO_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (recursoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recursoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAdapter.NAME, recursoAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecursoAdapter.NAME);
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecursoAdapter.NAME);
			}	
		}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECURSO, 
			BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RecursoAdapter recursoAdapterVO = (RecursoAdapter) userSession.get(RecursoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (recursoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RecursoAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecursoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(recursoAdapterVO, request);
			
            // Tiene errores recuperables
			if (recursoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
				saveDemodaErrors(request, recursoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.NAME, recursoAdapterVO, DefConstants.FWD_RECURSO_VIEW_ADAPTER);
			}
			
			// llamada al servicio
			RecursoVO recursoVO = DefServiceLocator.getGravamenService().desactivarRecurso
				(userSession, recursoAdapterVO.getRecurso());
			
            // Tiene errores recuperables
			if (recursoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString());
				saveDemodaErrors(request, recursoVO);				
				request.setAttribute(RecursoAdapter.NAME, recursoAdapterVO);
				return mapping.findForward(DefConstants.FWD_RECURSO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (recursoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recursoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAdapter.NAME, recursoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RecursoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecursoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, RecursoAdapter.NAME);
		}

		public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, RecursoAdapter.NAME);
			
		}

		// Metodos relacionados RecAtrVal

		public ActionForward verRecAtrVal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECATRVAL);

		}

		public ActionForward modificarRecAtrVal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECATRVAL);

		}

		public ActionForward eliminarRecAtrVal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECATRVAL);

		}
		
		public ActionForward agregarRecAtrVal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECATRVAL);
			
		}

		// Metodos relacionados RecCon

		public ActionForward verRecCon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECCON);

		}

		public ActionForward modificarRecCon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECCON);

		}

		public ActionForward eliminarRecCon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECCON);

		}
		
		public ActionForward agregarRecCon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECCON);
			
		}

		// Metodos relacionados RecClaDeu

		public ActionForward verRecClaDeu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECCLADEU);

		}

		public ActionForward modificarRecClaDeu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECCLADEU);

		}

		public ActionForward eliminarRecClaDeu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECCLADEU);

		}
		
		public ActionForward agregarRecClaDeu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECCLADEU);
			
		}

		// Metodos relacionados RecGenCueAtrVa

		public ActionForward verRecGenCueAtrVa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECGENCUEATRVA);

		}
		

		public ActionForward modificarRecGenCueAtrVa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECGENCUEATRVA);

		}

		public ActionForward eliminarRecGenCueAtrVa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECGENCUEATRVA);

		}
		
		public ActionForward agregarRecGenCueAtrVa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECGENCUEATRVA);
			
		}
		
		// Metodos relacionados RecEmi

		public ActionForward verRecEmi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECEMI);

		}

		public ActionForward modificarRecEmi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECEMI);

		}

		public ActionForward eliminarRecEmi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECEMI);

		}
		
		public ActionForward agregarRecEmi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECEMI);
			
		}

		// Metodos relacionados RecAtrCueEmi

		public ActionForward verRecAtrCueEmi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECATRCUEEMI);

		}

		public ActionForward modificarRecAtrCueEmi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECATRCUEEMI);

		}

		public ActionForward eliminarRecAtrCueEmi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECATRCUEEMI);

		}
		
		public ActionForward agregarRecAtrCueEmi(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECATRCUEEMI);
			
		}
		
		//	 Metodos relacionados RecAtrCue
		public ActionForward verRecAtrCue(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECATRCUE);

		}

		public ActionForward modificarRecAtrCue(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECATRCUE);

		}

		public ActionForward eliminarRecAtrCue(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECATRCUE);

		}
		
		public ActionForward agregarRecAtrCue(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECATRCUE);
			
		}
		

}
