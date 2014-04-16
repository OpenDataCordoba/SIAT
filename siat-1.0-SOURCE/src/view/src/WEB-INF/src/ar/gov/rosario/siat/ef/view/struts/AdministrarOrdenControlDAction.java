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

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlAdapter;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarOrdenControlDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarOrdenControlDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.EMITIR_ORDENCONTROL, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		OrdenControlAdapter ordenControlAdapterVO = new OrdenControlAdapter();
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(EfConstants.ACT_IMPRESION_ORDENCONTROL)) {
				String[] listIdOrdenControlSelected = (String[])userSession.getNavModel().getParameter("listIdOrdenControlSelected");
				log.debug("listIdOrdenControlSelected:"+listIdOrdenControlSelected.length);
				ordenControlAdapterVO.setListIds(ListUtil.getArrLongIdFromArrStringId(listIdOrdenControlSelected));
				// No llama a ningun servicio
				actionForward = mapping.findForward(EfConstants.FWD_ORDENCONTROL_IMPRIMIR_ADAPTER);
			}
			
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio="getOrdenControlAdapterForUpdate(userSession, commonKey)";
				ordenControlAdapterVO = EfServiceLocator.getInvestigacionService().getOrdenControlAdapterForUpdate(userSession, commonKey); 
				// No llama a ningun servicio
				actionForward = mapping.findForward(EfConstants.FWD_ORDENCONTROLINV_ADAPTER);
			}

			log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (ordenControlAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + ordenControlAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlAdapter.NAME, ordenControlAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			ordenControlAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + OrdenControlAdapter.NAME + ": "+ ordenControlAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(OrdenControlAdapter.NAME, ordenControlAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OrdenControlAdapter.NAME, ordenControlAdapterVO);
			 
			saveDemodaMessages(request, ordenControlAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlAdapter.NAME);
		}
	}
	
	public ActionForward impresionPDF(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.EMITIR_ORDENCONTROL, EfConstants.ACT_IMPRESION_ORDENCONTROL); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OrdenControlAdapter ordenControlAdapterVO = (OrdenControlAdapter) userSession.get(OrdenControlAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ordenControlAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordenControlAdapterVO, request);
			
            // Tiene errores recuperables
			if (ordenControlAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlAdapter.NAME, ordenControlAdapterVO);
			}
			
			// llamada al servicio
			PrintModel printModel= EfServiceLocator.getInvestigacionService().getPDFImpresionOrdenControl(userSession, ordenControlAdapterVO);
			
			if(printModel.hasError()){
				saveDemodaErrors(request, printModel);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlAdapter.NAME, ordenControlAdapterVO);
			}
			
			baseResponsePrintModel(response, printModel);
			
			return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, OrdenControlAdapter.NAME);
			
	}
	
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.EMITIR_ORDENCONTROL, BaseConstants.ACT_MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OrdenControlAdapter ordenControlAdapterVO = (OrdenControlAdapter) userSession.get(OrdenControlAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ordenControlAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlAdapter.NAME); 
			}
			
			log.debug("lista nula antes populate: "+ String.valueOf(ordenControlAdapterVO.getListIdOrdConCue()==null));
			
			//Fuerzo a false la bandera de ordconcue para que funcione el populate del checkbox
			ordenControlAdapterVO.setListIdOrdConCue(null);
			
			
			DemodaUtil.populateVO(ordenControlAdapterVO, request);
			
			log.debug("lista nula despues del populate: "+ String.valueOf(ordenControlAdapterVO.getListIdOrdConCue()==null));
			
			if (ordenControlAdapterVO.hasErrorRecoverable()){
				log.error("recoverable error en: "  + funcName + ": " + ordenControlAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlAdapter.NAME, ordenControlAdapterVO);
			}
			
			
			ordenControlAdapterVO = EfServiceLocator.getInvestigacionService().updateOrdenControl(userSession, ordenControlAdapterVO);
			
			if (ordenControlAdapterVO.hasErrorRecoverable()){
				log.error("recoverable error en: "  + funcName + ": " + ordenControlAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlAdapter.NAME, ordenControlAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (ordenControlAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": "  + ordenControlAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlAdapter.NAME, ordenControlAdapterVO);
			}
			
			NavModel navModel=userSession.getNavModel();
			
			navModel.setPrevActionParameter(BaseConstants.ACT_BUSCAR);
			
			return forwardConfirmarOk(mapping, request, funcName, OrdenControlAdapter.NAME);
			
		}catch (Exception exception){
			return baseException(mapping, request, funcName, exception, OrdenControlAdapter.NAME);
		}
			
	}

}
