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
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatColAdapter;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatColVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlaFueDatColDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlaFueDatColDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PLAFUEDATCOL, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlaFueDatColAdapter plaFueDatColAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlaFueDatColAdapterForUpdate(userSession, commonKey)";
				plaFueDatColAdapterVO = EfServiceLocator.getFiscalizacionService().getPlaFueDatColAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_PLAFUEDATCOL_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlaFueDatColAdapterForView(userSession, commonKey)";
				plaFueDatColAdapterVO = EfServiceLocator.getFiscalizacionService().getPlaFueDatColAdapterForView(userSession, commonKey);				
				plaFueDatColAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.PLAFUEDATCOL_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_PLAFUEDATCOL_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPlaFueDatColAdapterForCreate(userSession)";
				plaFueDatColAdapterVO = EfServiceLocator.getFiscalizacionService().getPlaFueDatColAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_PLAFUEDATCOL_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (plaFueDatColAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + plaFueDatColAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaFueDatColAdapter.NAME, plaFueDatColAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			plaFueDatColAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlaFueDatColAdapter.NAME + ": "+ plaFueDatColAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlaFueDatColAdapter.NAME, plaFueDatColAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlaFueDatColAdapter.NAME, plaFueDatColAdapterVO);
			 
			saveDemodaMessages(request, plaFueDatColAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaFueDatColAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PLAFUEDATCOL, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlaFueDatColAdapter plaFueDatColAdapterVO = (PlaFueDatColAdapter) userSession.get(PlaFueDatColAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (plaFueDatColAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlaFueDatColAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaFueDatColAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(plaFueDatColAdapterVO, request);
			
			// recupera los datos de los checks
			if(request.getParameter("ocultaChecked")!=null)
				plaFueDatColAdapterVO.setOcultaChecked(true);
			else
				plaFueDatColAdapterVO.setOcultaChecked(false);
			
			if(request.getParameter("sumaEnTotalChecked")!=null)
				plaFueDatColAdapterVO.setSumaEnTotalChecked(true);
			else
				plaFueDatColAdapterVO.setSumaEnTotalChecked(false);
			
            // Tiene errores recuperables
			if (plaFueDatColAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaFueDatColAdapterVO.infoString()); 
				saveDemodaErrors(request, plaFueDatColAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaFueDatColAdapter.NAME, plaFueDatColAdapterVO);
			}
			
			// llamada al servicio
			plaFueDatColAdapterVO = EfServiceLocator.getFiscalizacionService().createPlaFueDatCol(userSession, plaFueDatColAdapterVO);
			
            // Tiene errores recuperables
			if (plaFueDatColAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaFueDatColAdapterVO.infoString()); 
				saveDemodaErrors(request, plaFueDatColAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaFueDatColAdapter.NAME, plaFueDatColAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (plaFueDatColAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + plaFueDatColAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaFueDatColAdapter.NAME, plaFueDatColAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlaFueDatColAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaFueDatColAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PLAFUEDATCOL, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlaFueDatColAdapter plaFueDatColAdapterVO = (PlaFueDatColAdapter) userSession.get(PlaFueDatColAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (plaFueDatColAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlaFueDatColAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaFueDatColAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(plaFueDatColAdapterVO, request);
			
			// recupera los datos de los checks
			if(request.getParameter("ocultaChecked")!=null)
				plaFueDatColAdapterVO.setOcultaChecked(true);
			else
				plaFueDatColAdapterVO.setOcultaChecked(false);
			
			if(request.getParameter("sumaEnTotalChecked")!=null)
				plaFueDatColAdapterVO.setSumaEnTotalChecked(true);
			else
				plaFueDatColAdapterVO.setSumaEnTotalChecked(false);

            // Tiene errores recuperables
			if (plaFueDatColAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaFueDatColAdapterVO.infoString()); 
				saveDemodaErrors(request, plaFueDatColAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaFueDatColAdapter.NAME, plaFueDatColAdapterVO);
			}
			
			// llamada al servicio
			plaFueDatColAdapterVO = EfServiceLocator.getFiscalizacionService().updatePlaFueDatCol(userSession, plaFueDatColAdapterVO);
			
            // Tiene errores recuperables
			if (plaFueDatColAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaFueDatColAdapterVO.infoString()); 
				saveDemodaErrors(request, plaFueDatColAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaFueDatColAdapter.NAME, plaFueDatColAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (plaFueDatColAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + plaFueDatColAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaFueDatColAdapter.NAME, plaFueDatColAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlaFueDatColAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaFueDatColAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PLAFUEDATCOL, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlaFueDatColAdapter plaFueDatColAdapterVO = (PlaFueDatColAdapter) userSession.get(PlaFueDatColAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (plaFueDatColAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlaFueDatColAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaFueDatColAdapter.NAME); 
			}

			// llamada al servicio
			PlaFueDatColVO plaFueDatColVO = EfServiceLocator.getFiscalizacionService().deletePlaFueDatCol
				(userSession, plaFueDatColAdapterVO.getPlaFueDatCol());
			
            // Tiene errores recuperables
			if (plaFueDatColVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaFueDatColAdapterVO.infoString());
				saveDemodaErrors(request, plaFueDatColVO);				
				request.setAttribute(PlaFueDatColAdapter.NAME, plaFueDatColAdapterVO);
				return mapping.findForward(EfConstants.FWD_PLAFUEDATCOL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (plaFueDatColVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + plaFueDatColAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaFueDatColAdapter.NAME, plaFueDatColAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlaFueDatColAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaFueDatColAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlaFueDatColAdapter.NAME);
		
	}
		
}
