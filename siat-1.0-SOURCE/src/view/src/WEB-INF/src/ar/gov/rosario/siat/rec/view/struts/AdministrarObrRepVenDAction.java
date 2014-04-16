//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.rec.iface.model.ObrRepVenAdapter;
import ar.gov.rosario.siat.rec.iface.model.ObrRepVenVO;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarObrRepVenDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarObrRepVenDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_OBRREPVEN, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ObrRepVenAdapter obrRepVenAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getObrRepVenAdapterForCreate(userSession)";
				obrRepVenAdapterVO = RecServiceLocator.getCdmService().getObrRepVenAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_OBRREPVEN_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (obrRepVenAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + obrRepVenAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObrRepVenAdapter.NAME, obrRepVenAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			obrRepVenAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ObrRepVenAdapter.NAME + ": "+ obrRepVenAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ObrRepVenAdapter.NAME, obrRepVenAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ObrRepVenAdapter.NAME, obrRepVenAdapterVO);
			 
			saveDemodaMessages(request, obrRepVenAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObrRepVenAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_OBRREPVEN, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObrRepVenAdapter obrRepVenAdapterVO = (ObrRepVenAdapter) userSession.get(ObrRepVenAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (obrRepVenAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObrRepVenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObrRepVenAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(obrRepVenAdapterVO, request);
			
            // Tiene errores recuperables
			if (obrRepVenAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obrRepVenAdapterVO.infoString()); 
				saveDemodaErrors(request, obrRepVenAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObrRepVenAdapter.NAME, obrRepVenAdapterVO);
			}
			
			// llamada al servicio
			ObrRepVenVO obrRepVenVO = RecServiceLocator.getCdmService().createObrRepVen(userSession, obrRepVenAdapterVO.getObrRepVen());
			
            // Tiene errores recuperables
			if (obrRepVenVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obrRepVenVO.infoString()); 
				saveDemodaErrors(request, obrRepVenVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObrRepVenAdapter.NAME, obrRepVenAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (obrRepVenVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + obrRepVenVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObrRepVenAdapter.NAME, obrRepVenAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ObrRepVenAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObrRepVenAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ObrRepVenAdapter.NAME);
		
	}
	
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObrRepVenAdapter adapterVO = (ObrRepVenAdapter)userSession.get(ObrRepVenAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObrRepVenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObrRepVenAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getObrRepVen().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getObrRepVen()); 
			
			adapterVO.getObrRepVen().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(ObrRepVenAdapter.NAME, adapterVO);
			
			return mapping.findForward(RecConstants.FWD_OBRREPVEN_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObrRepVenAdapter.NAME);
		}	
	}
}
