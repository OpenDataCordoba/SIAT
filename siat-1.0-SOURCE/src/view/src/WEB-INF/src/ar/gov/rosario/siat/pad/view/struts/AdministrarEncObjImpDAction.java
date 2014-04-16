//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.struts;

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
import ar.gov.rosario.siat.pad.iface.model.ObjImpAdapter;
import ar.gov.rosario.siat.pad.iface.model.ObjImpVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncObjImpDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncObjImpDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_OBJIMP_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ObjImpAdapter objImpAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getObjImpAdapterForUpdate(userSession, commonKey)";
				objImpAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getObjImpAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_OBJIMP_ENC_EDIT_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (objImpAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio 
					+ ": " + objImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					ObjImpAdapter.ENC_NAME, objImpAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			objImpAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " 
				+ ObjImpAdapter.ENC_NAME + ": "+ objImpAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ObjImpAdapter.ENC_NAME, objImpAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ObjImpAdapter.ENC_NAME, objImpAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			PadSecurityConstants.ABM_OBJIMP_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObjImpAdapter objImpAdapterVO = (ObjImpAdapter) 
				userSession.get(ObjImpAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (objImpAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + 
					ObjImpAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(objImpAdapterVO, request);
			
            // Tiene errores recuperables
			if (objImpAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAdapterVO.infoString()); 
				saveDemodaErrors(request, objImpAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					ObjImpAdapter.ENC_NAME, objImpAdapterVO);
			}
			
			// llamada al servicio
			ObjImpVO objImpVO = PadServiceLocator.getPadObjetoImponibleService().updateObjImp
				(userSession, objImpAdapterVO.getObjImp());
			
            // Tiene errores recuperables
			if (objImpVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAdapterVO.infoString()); 
				saveDemodaErrors(request, objImpVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					ObjImpAdapter.ENC_NAME, objImpAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (objImpVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + objImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					ObjImpAdapter.ENC_NAME, objImpAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ObjImpAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ObjImpAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ObjImpAdapter.ENC_NAME);
		
	}

}
	
