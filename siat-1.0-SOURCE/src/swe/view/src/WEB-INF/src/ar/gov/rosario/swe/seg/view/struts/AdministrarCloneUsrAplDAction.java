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
import ar.gov.rosario.swe.iface.model.CloneUsrAplAdapter;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.model.UsrAplVO;
import ar.gov.rosario.swe.iface.util.SegBussConstants;
import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarCloneUsrAplDAction extends SweBaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCloneUsrAplDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		String funcName = SweUtil.currentMethodName();
		System.out.println("entrando en AdministrarCloneUsrAplDAction." + funcName);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_USUARIOS, "agregar"); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (navModel == null){
			if (log.isDebugEnabled()) log.debug("-----EL NAVMODEL ES NULL !! -----");
		}
		CloneUsrAplAdapter cloneUsrAplAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			// puede ser la clave de UsrApl o de la aplicacion
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
						
			if (act.equals(SweConstants.ACT_CLONAR)) {
				stringServicio = "getCloneUsrAplAdapterForCreate(userSession)";
				if (log.isDebugEnabled()) log.debug("Entrando a getCloneUsrAplAdapterForCreate()");
				cloneUsrAplAdapterVO = SweServiceLocator.getSweService().getCloneUsrAplAdapterForCreate(userSession, commonKey);
				// se copian los datos
				cloneUsrAplAdapterVO.setUsrAplToClone(cloneUsrAplAdapterVO.getUsrApl()); 
				//cloneUsrAplAdapterVO.getUsrApl().setUsername("");
				if (log.isDebugEnabled()) log.debug("Pasó getCloneUsrAplAdapterForCreate()");
				actionForward = mapping.findForward(SweSegConstants.FWD_CLONEUSRAPL_ADAPTER);
				if (log.isDebugEnabled()) log.debug("Pasó mapping.findForward()");
			}

			// Nunca Tiene errores recuperables
			if (log.isDebugEnabled()) log.debug("llegamos hasta antes de forwardErrorNonRecoverable");
			
			// Tiene errores no recuperables
			if (cloneUsrAplAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cloneUsrAplAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CloneUsrAplAdapter.NAME, cloneUsrAplAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug("llegamos hasta antes de pasarNavModelActualAHistorial");
			
			// Incremento el historial de navegacion del userSession
			pasarNavModelActualAHistorial(mapping, funcName, userSession);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CloneUsrAplAdapter.NAME + ": "+ cloneUsrAplAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CloneUsrAplAdapter.NAME, cloneUsrAplAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CloneUsrAplAdapter.NAME, cloneUsrAplAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
	
	
	public ActionForward clonar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_USUARIOS, SweSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			CloneUsrAplAdapter cloneUsrAplAdapterVO = (CloneUsrAplAdapter) userSession.get(CloneUsrAplAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cloneUsrAplAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CloneUsrAplAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CloneUsrAplAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cloneUsrAplAdapterVO, request);
			
            // Tiene errores recuperables
			if (cloneUsrAplAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cloneUsrAplAdapterVO.infoString()); 
				saveDemodaErrors(request, cloneUsrAplAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CloneUsrAplAdapter.NAME, cloneUsrAplAdapterVO);
			}
			//Si es usuario de SWE se obtienen los ids de las aplicaciones seleccionadas 
			if(cloneUsrAplAdapterVO.getUsrApl().getAplicacion().getCodigo().equals(SegBussConstants.CODIGO_SWE)){
				Long[] listIdsAppSelected = (Long[]) PropertyUtils.getSimpleProperty(form, "listIdsAppSelected");
				cloneUsrAplAdapterVO.getUsrApl().setListIdsAppSelected(listIdsAppSelected);
			}
			
			// llamada al servicio 
			UsrAplVO usrAplVO = SweServiceLocator.getSweService().cloneUsrApl(userSession, cloneUsrAplAdapterVO.getUsrApl(), cloneUsrAplAdapterVO.getUsrAplToClone());
			
			
            // Tiene errores recuperables
			if (usrAplVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usrAplVO.infoString()); 
				saveDemodaErrors(request, usrAplVO);
				return forwardErrorRecoverable(mapping, request, userSession, CloneUsrAplAdapter.NAME, cloneUsrAplAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (usrAplVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usrAplVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CloneUsrAplAdapter.NAME, cloneUsrAplAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CloneUsrAplAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	
	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return baseVolver(mapping, form, request, response, CloneUsrAplAdapter.NAME);
	}
	
}
