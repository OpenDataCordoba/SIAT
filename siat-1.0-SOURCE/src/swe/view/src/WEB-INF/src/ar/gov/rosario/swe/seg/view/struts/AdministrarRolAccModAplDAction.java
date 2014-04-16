//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.seg.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.swe.SweServiceLocator;
import ar.gov.rosario.swe.base.view.struts.SweBaseDispatchAction;
import ar.gov.rosario.swe.iface.model.AccModAplSearchPage;
import ar.gov.rosario.swe.iface.model.RolAccModAplAdapter;
import ar.gov.rosario.swe.iface.model.RolAccModAplSearchPage;
import ar.gov.rosario.swe.iface.model.RolAccModAplVO;
import ar.gov.rosario.swe.iface.model.RolAplVO;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarRolAccModAplDAction extends SweBaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRolAccModAplDAction.class);

	public static String ACT_AGREGAR_MULTIPLE = "agregarMultiple";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ACCIONESROL, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ActionForward actionForward = null;
		RolAccModAplAdapter rolAccModAplAdapterVO = null;
		
		try {
			
			// Incremento el historial de navegacion del userSession
			pasarNavModelActualAHistorial(mapping, funcName, userSession);
			
			if (act.equals(AdministrarRolAccModAplDAction.ACT_AGREGAR_MULTIPLE)) {
				return this.agregarMultiple(mapping, form, request, response);
			}

			CommonKey rolAccModAplCmmKey = new CommonKey(navModel.getSelectedId());
			String stringServicio = "";
			
			if (act.equals(SweConstants.ACT_ELIMINAR)) {
				stringServicio = "getRolAccModAplAdapterForDelete(userSession, rolAccModAplCmmKey)";
				rolAccModAplAdapterVO = SweServiceLocator.getSweService().getRolAccModAplAdapterForDelete(userSession, rolAccModAplCmmKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_ROLACCMODAPL_ADAPTER_VIEW);					
			}
			
			if (act.equals(SweConstants.ACT_VER)) {
				stringServicio = "getRolAccModAplAdapterForView(userSession, rolAccModAplCmmKey)";
				rolAccModAplAdapterVO = SweServiceLocator.getSweService().getRolAccModAplAdapterForView(userSession, rolAccModAplCmmKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_ROLACCMODAPL_ADAPTER_VIEW);
			}
			
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (rolAccModAplAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + stringServicio + ": " + rolAccModAplAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RolAccModAplAdapter.NAME, rolAccModAplAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + RolAccModAplAdapter.NAME + ": "+ rolAccModAplAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(RolAccModAplAdapter.NAME, rolAccModAplAdapterVO);
			// Subo el apdater al userSession
			userSession.put(RolAccModAplAdapter.NAME, rolAccModAplAdapterVO);
			
			return actionForward;	
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
	
	public ActionForward agregarMultiple(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ACCIONESROL, SweSecurityConstants.AGREGARMULTIPLE); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el Search Page del userSession
			RolAccModAplSearchPage rolAccModAplSearchPageVO = (RolAccModAplSearchPage) userSession.get(RolAccModAplSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (rolAccModAplSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + RolAccModAplSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RolAccModAplSearchPage.NAME); 
			}
			
			RolAplVO rolAplVO = rolAccModAplSearchPageVO.getRolApl();
			
			Long[] listId = (Long[]) PropertyUtils.getSimpleProperty(form, "listId");
			
			// llamada al servicio
			rolAplVO = SweServiceLocator.getSweService().createRolAccModAplMultiple(userSession, rolAplVO, listId);
			
			// TODO: bajar el AccModAplSearchPage y copiarle los errores del rolAplVO
			
            // Tiene errores recuperables
			if (rolAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rolAplVO.infoString()); 
				saveDemodaErrors(request, rolAplVO);
				return forwardErrorRecoverable(mapping, request, userSession, AccModAplSearchPage.NAME, rolAplVO);
			}
			
			// Tiene errores no recuperables
			if (rolAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rolAplVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AccModAplSearchPage.NAME, rolAplVO);
			}
						
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AccModAplSearchPage.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_ACCIONESROL, SweSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();		
		try {
			// Bajo el adapter del userSession
			CommonKey rolAccModAplCommKey = new CommonKey(navModel.getSelectedId());
			
			// Si es nulo no se puede continuar
			if (rolAccModAplCommKey == null) {
				log.error("error en: "  + funcName + ": rolAccModAplCommKey IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ""); 
			}
			
			RolAccModAplVO rolAccModAplVO = new RolAccModAplVO();
			rolAccModAplVO.setId(rolAccModAplCommKey.getId());
			
			// llamada al servicio
			rolAccModAplVO  = SweServiceLocator.getSweService().deleteRolAccModApl(userSession, rolAccModAplVO);
			
            // Tiene errores recuperables
			if (rolAccModAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rolAccModAplVO.infoString());
				RolAccModAplAdapter rolAccModAplAdapterVO = (RolAccModAplAdapter) 
					userSession.get(RolAccModAplAdapter.NAME);				
				saveDemodaErrors(request, rolAccModAplVO);
				request.setAttribute(RolAccModAplAdapter.NAME, rolAccModAplAdapterVO);
				return mapping.findForward(SweSegConstants.FWD_ROLACCMODAPL_ADAPTER_VIEW);
			}
			
			// Tiene errores no recuperables
			if (rolAccModAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rolAccModAplVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, "", null);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, "");
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return baseVolver(mapping, form, request, response, RolAccModAplAdapter.NAME);
	}
}
