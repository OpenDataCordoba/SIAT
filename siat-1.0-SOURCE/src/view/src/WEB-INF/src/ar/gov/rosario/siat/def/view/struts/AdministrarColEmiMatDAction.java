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
import ar.gov.rosario.siat.def.iface.model.ColEmiMatAdapter;
import ar.gov.rosario.siat.def.iface.model.ColEmiMatVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarColEmiMatDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarColEmiMatDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_COLEMIMAT, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ColEmiMatAdapter colEmiMatAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getColEmiMatAdapterForView(userSession, commonKey)";
				colEmiMatAdapterVO = DefServiceLocator.getEmisionService().getColEmiMatAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_COLEMIMAT_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getColEmiMatAdapterForUpdate(userSession, commonKey)";
				colEmiMatAdapterVO = DefServiceLocator.getEmisionService().getColEmiMatAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_COLEMIMAT_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getColEmiMatAdapterForView(userSession, commonKey)";
				colEmiMatAdapterVO = DefServiceLocator.getEmisionService().getColEmiMatAdapterForView(userSession, commonKey);				
				colEmiMatAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.COLEMIMAT_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_COLEMIMAT_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getColEmiMatAdapterForCreate(userSession)";
				colEmiMatAdapterVO = DefServiceLocator.getEmisionService().getColEmiMatAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_COLEMIMAT_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getColEmiMatAdapterForView(userSession)";
				colEmiMatAdapterVO = DefServiceLocator.getEmisionService().getColEmiMatAdapterForView(userSession, commonKey);
				colEmiMatAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.COLEMIMAT_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_COLEMIMAT_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getColEmiMatAdapterForView(userSession)";
				colEmiMatAdapterVO = DefServiceLocator.getEmisionService().getColEmiMatAdapterForView(userSession, commonKey);
				colEmiMatAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.COLEMIMAT_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_COLEMIMAT_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (colEmiMatAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + colEmiMatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ColEmiMatAdapter.NAME, colEmiMatAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			colEmiMatAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ColEmiMatAdapter.NAME + ": "+ colEmiMatAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ColEmiMatAdapter.NAME, colEmiMatAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ColEmiMatAdapter.NAME, colEmiMatAdapterVO);
			 
			saveDemodaMessages(request, colEmiMatAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ColEmiMatAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_COLEMIMAT, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ColEmiMatAdapter colEmiMatAdapterVO = (ColEmiMatAdapter) userSession.get(ColEmiMatAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (colEmiMatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ColEmiMatAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ColEmiMatAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(colEmiMatAdapterVO, request);
			
            // Tiene errores recuperables
			if (colEmiMatAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + colEmiMatAdapterVO.infoString()); 
				saveDemodaErrors(request, colEmiMatAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ColEmiMatAdapter.NAME, colEmiMatAdapterVO);
			}
			
			// llamada al servicio
			ColEmiMatVO colEmiMatVO = DefServiceLocator.getEmisionService().createColEmiMat(userSession, colEmiMatAdapterVO.getColEmiMat());
			
            // Tiene errores recuperables
			if (colEmiMatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + colEmiMatVO.infoString()); 
				saveDemodaErrors(request, colEmiMatVO);
				return forwardErrorRecoverable(mapping, request, userSession, ColEmiMatAdapter.NAME, colEmiMatAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (colEmiMatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + colEmiMatVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ColEmiMatAdapter.NAME, colEmiMatAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ColEmiMatAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ColEmiMatAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_COLEMIMAT, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ColEmiMatAdapter colEmiMatAdapterVO = (ColEmiMatAdapter) userSession.get(ColEmiMatAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (colEmiMatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ColEmiMatAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ColEmiMatAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(colEmiMatAdapterVO, request);
			
            // Tiene errores recuperables
			if (colEmiMatAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + colEmiMatAdapterVO.infoString()); 
				saveDemodaErrors(request, colEmiMatAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ColEmiMatAdapter.NAME, colEmiMatAdapterVO);
			}
			
			// llamada al servicio
			ColEmiMatVO colEmiMatVO = DefServiceLocator.getEmisionService().updateColEmiMat(userSession, colEmiMatAdapterVO.getColEmiMat());
			
            // Tiene errores recuperables
			if (colEmiMatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + colEmiMatAdapterVO.infoString()); 
				saveDemodaErrors(request, colEmiMatVO);
				return forwardErrorRecoverable(mapping, request, userSession, ColEmiMatAdapter.NAME, colEmiMatAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (colEmiMatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + colEmiMatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ColEmiMatAdapter.NAME, colEmiMatAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ColEmiMatAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ColEmiMatAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_COLEMIMAT, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ColEmiMatAdapter colEmiMatAdapterVO = (ColEmiMatAdapter) userSession.get(ColEmiMatAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (colEmiMatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ColEmiMatAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ColEmiMatAdapter.NAME); 
			}

			// llamada al servicio
			ColEmiMatVO colEmiMatVO = DefServiceLocator.getEmisionService().deleteColEmiMat
				(userSession, colEmiMatAdapterVO.getColEmiMat());
			
            // Tiene errores recuperables
			if (colEmiMatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + colEmiMatAdapterVO.infoString());
				saveDemodaErrors(request, colEmiMatVO);				
				request.setAttribute(ColEmiMatAdapter.NAME, colEmiMatAdapterVO);
				return mapping.findForward(DefConstants.FWD_COLEMIMAT_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (colEmiMatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + colEmiMatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ColEmiMatAdapter.NAME, colEmiMatAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ColEmiMatAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ColEmiMatAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ColEmiMatAdapter.NAME);
		
	}
}