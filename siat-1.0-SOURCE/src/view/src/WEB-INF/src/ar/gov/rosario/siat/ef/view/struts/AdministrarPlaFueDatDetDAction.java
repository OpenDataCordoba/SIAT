//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

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
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatDetAdapter;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatDetVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlaFueDatDetDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlaFueDatDetDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PLAFUEDATDET, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlaFueDatDetAdapter plaFueDatDetAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlaFueDatDetAdapterForView(userSession, commonKey)";
				plaFueDatDetAdapterVO = EfServiceLocator.getFiscalizacionService().getPlaFueDatDetAdapterForView(userSession, commonKey);				
				plaFueDatDetAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.PLAFUEDATDET_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_PLAFUEDATDET_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlaFueDatDetAdapterForCreate(userSession, commonKey)";
				plaFueDatDetAdapterVO = EfServiceLocator.getFiscalizacionService().getPlaFueDatDetAdapterForCreate(userSession, commonKey);				
				actionForward = mapping.findForward(EfConstants.FWD_PLAFUEDATDET_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (plaFueDatDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + plaFueDatDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaFueDatDetAdapter.NAME, plaFueDatDetAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			plaFueDatDetAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlaFueDatDetAdapter.NAME + ": "+ plaFueDatDetAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlaFueDatDetAdapter.NAME, plaFueDatDetAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlaFueDatDetAdapter.NAME, plaFueDatDetAdapterVO);
			 
			saveDemodaMessages(request, plaFueDatDetAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaFueDatDetAdapter.NAME);
		}
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PLAFUEDATDET, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlaFueDatDetAdapter PlaFueDatDetAdapterVO = (PlaFueDatDetAdapter) userSession.get(PlaFueDatDetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (PlaFueDatDetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlaFueDatDetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaFueDatDetAdapter.NAME); 
			}

			// llamada al servicio
			PlaFueDatDetVO plaFueDatDetVO = EfServiceLocator.getFiscalizacionService().deletePlaFueDatDet(userSession, PlaFueDatDetAdapterVO.getPlaFueDatDet());
			
            // Tiene errores recuperables
			if (plaFueDatDetVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + PlaFueDatDetAdapterVO.infoString());
				saveDemodaErrors(request, plaFueDatDetVO);				
				request.setAttribute(PlaFueDatDetAdapter.NAME, PlaFueDatDetAdapterVO);
				return mapping.findForward(EfConstants.FWD_PLAFUEDATDET_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (plaFueDatDetVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + PlaFueDatDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaFueDatDetAdapter.NAME, PlaFueDatDetAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlaFueDatDetAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaFueDatDetAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PLAFUEDATDET, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlaFueDatDetAdapter plaFueDatDetAdapterVO = (PlaFueDatDetAdapter) userSession.get(PlaFueDatDetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (plaFueDatDetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlaFueDatDetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaFueDatDetAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(plaFueDatDetAdapterVO, request);

			// llamada al servicio
			plaFueDatDetAdapterVO = EfServiceLocator.getFiscalizacionService().createPlaFueDatDet(userSession, plaFueDatDetAdapterVO);
			
            // Tiene errores recuperables
			if (plaFueDatDetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaFueDatDetAdapterVO.infoString());
				saveDemodaErrors(request, plaFueDatDetAdapterVO);				
				request.setAttribute(PlaFueDatDetAdapter.NAME, plaFueDatDetAdapterVO);
				return mapping.findForward(EfConstants.FWD_PLAFUEDATDET_EDIT_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (plaFueDatDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + plaFueDatDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaFueDatDetAdapter.NAME, plaFueDatDetAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlaFueDatDetAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaFueDatDetAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlaFueDatDetAdapter.NAME);
		
	}
		
}
