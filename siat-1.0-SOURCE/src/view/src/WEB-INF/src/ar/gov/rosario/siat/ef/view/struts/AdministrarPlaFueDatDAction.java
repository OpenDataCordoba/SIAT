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
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatAdapter;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarPlaFueDatDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlaFueDatDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PLAFUEDAT, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlaFueDatAdapter plaFueDatAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPlaFueDatAdapterForView(userSession, commonKey)";
				plaFueDatAdapterVO = EfServiceLocator.getFiscalizacionService().getPlaFueDatAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_PLAFUEDAT_VIEW_ADAPTER);
			}
			
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlaFueDatAdapterForUpdate(userSession, commonKey)";
				plaFueDatAdapterVO = EfServiceLocator.getFiscalizacionService().getPlaFueDatAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_PLAFUEDAT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlaFueDatAdapterForDelete(userSession, commonKey)";
				plaFueDatAdapterVO = EfServiceLocator.getFiscalizacionService().getPlaFueDatAdapterForView
					(userSession, commonKey);
				plaFueDatAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.PLAFUEDAT_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_PLAFUEDAT_VIEW_ADAPTER);					
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (plaFueDatAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + plaFueDatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			plaFueDatAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				PlaFueDatAdapter.NAME + ": " + plaFueDatAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
			
			saveDemodaMessages(request, plaFueDatAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaFueDatAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			EfConstants.ACTION_ADMINISTRAR_ENC_PLAFUEDAT, BaseConstants.ACT_MODIFICAR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PLAFUEDAT, 
			BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlaFueDatAdapter plaFueDatAdapterVO = (PlaFueDatAdapter) userSession.get(PlaFueDatAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (plaFueDatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlaFueDatAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaFueDatAdapter.NAME); 
			}

			// llamada al servicio
			PlaFueDatVO plaFueDatVO = EfServiceLocator.getFiscalizacionService().deletePlaFueDat
				(userSession, plaFueDatAdapterVO.getPlaFueDat());
			
            // Tiene errores recuperables
			if (plaFueDatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaFueDatAdapterVO.infoString());
				saveDemodaErrors(request, plaFueDatVO);				
				request.setAttribute(PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
				return mapping.findForward(EfConstants.FWD_PLAFUEDAT_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (plaFueDatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + plaFueDatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlaFueDatAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaFueDatAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlaFueDatAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, PlaFueDatAdapter.NAME);
		
	}
	
	// metodos para la planilla
	public ActionForward generarModificarPlanilla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PLAFUEDAT, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PlaFueDatAdapter plaFueDatAdapterVO = (PlaFueDatAdapter) userSession.get(PlaFueDatAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (plaFueDatAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PlaFueDatAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PlaFueDatAdapter.NAME); 
				}

				// llamada al servicio
				plaFueDatAdapterVO = EfServiceLocator.getFiscalizacionService().generarModificarPlanilla(userSession, plaFueDatAdapterVO);
				
	            // Tiene errores recuperables
				if (plaFueDatAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + plaFueDatAdapterVO.infoString());
					saveDemodaErrors(request, plaFueDatAdapterVO);				
					request.setAttribute(PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
					return mapping.findForward(EfConstants.FWD_PLAFUEDAT_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (plaFueDatAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + plaFueDatAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
				}
				
				request.setAttribute("irA", "bloquePlanilla");
				
				// Envio el VO al request
				request.setAttribute(PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
				// Subo el apdater al userSession
				userSession.put(PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
				
				saveDemodaMessages(request, plaFueDatAdapterVO);			

				return mapping.findForward(EfConstants.FWD_PLAFUEDAT_ADAPTER);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PlaFueDatAdapter.NAME);
			}
		}
	
	public ActionForward irModificarPlaFueDatDet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PLAFUEDAT, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PlaFueDatAdapter plaFueDatAdapterVO = (PlaFueDatAdapter) userSession.get(PlaFueDatAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (plaFueDatAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PlaFueDatAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PlaFueDatAdapter.NAME); 
				}

				plaFueDatAdapterVO.getPlaFueDatDet().setId(new Long(request.getParameter("selectedId")));
				
				// llamada al servicio
				plaFueDatAdapterVO = EfServiceLocator.getFiscalizacionService().getPlaFueDatAdapterForUpdateDetalle(userSession, plaFueDatAdapterVO);
				
	            // Tiene errores recuperables
				if (plaFueDatAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + plaFueDatAdapterVO.infoString());
					saveDemodaErrors(request, plaFueDatAdapterVO);				
					request.setAttribute(PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
					return mapping.findForward(EfConstants.FWD_PLAFUEDAT_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (plaFueDatAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + plaFueDatAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
				}
				
				request.setAttribute("irA", "bloquePlanilla");
				
				// Envio el VO al request
				request.setAttribute(PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
				// Subo el apdater al userSession
				userSession.put(PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
				
				saveDemodaMessages(request, plaFueDatAdapterVO);			

				return mapping.findForward(EfConstants.FWD_PLAFUEDAT_ADAPTER);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PlaFueDatAdapter.NAME);
			}
		}

	public ActionForward modificarPlaFueDatDet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PLAFUEDATDET,BaseSecurityConstants.MODIFICAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PlaFueDatAdapter plaFueDatAdapterVO = (PlaFueDatAdapter) userSession.get(PlaFueDatAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (plaFueDatAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PlaFueDatAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PlaFueDatAdapter.NAME); 
				}

				
				
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(plaFueDatAdapterVO, request);

				
	            // Tiene errores recuperables
				if (plaFueDatAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + plaFueDatAdapterVO.infoString()); 
					// Envio el VO al request
					request.setAttribute(PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
					// Subo el apdater al userSession
					userSession.put(PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
					saveDemodaErrors(request, plaFueDatAdapterVO);				
					return mapping.findForward(EfConstants.FWD_PLAFUEDAT_ADAPTER);
				}
				
				// llamada al servicio
				plaFueDatAdapterVO = EfServiceLocator.getFiscalizacionService().updatePlaFueDatDet(userSession, plaFueDatAdapterVO);
				
	            // Tiene errores recuperables
				if (plaFueDatAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + plaFueDatAdapterVO.infoString());
					saveDemodaErrors(request, plaFueDatAdapterVO);				
					request.setAttribute(PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
					return mapping.findForward(EfConstants.FWD_PLAFUEDAT_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (plaFueDatAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + plaFueDatAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
				}
				
				request.setAttribute("irA", "bloquePlanilla");
				
				// Envio el VO al request
				request.setAttribute(PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
				// Subo el apdater al userSession
				userSession.put(PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
				
				saveDemodaMessages(request, plaFueDatAdapterVO);			

				return mapping.findForward(EfConstants.FWD_PLAFUEDAT_ADAPTER);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PlaFueDatAdapter.NAME);
			}
		}

	public ActionForward eliminarPlaFueDatDet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_PLAFUEDATDET);

	}
	
	public ActionForward agregarPlaFueDatDet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_PLAFUEDATDET);

	}
	
	// FIN metodos para la planilla		

	
	// Metodos relacionados PlaFueDatCol
	public ActionForward verPlaFueDatCol(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_PLAFUEDATCOL);

	}

	public ActionForward modificarPlaFueDatCol(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_PLAFUEDATCOL);

	}

	public ActionForward eliminarPlaFueDatCol(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_PLAFUEDATCOL);

	}
	
	public ActionForward agregarPlaFueDatCol(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_PLAFUEDATCOL);
		
	}
	
	public ActionForward imprimir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ACTA, EfSecurityConstants.IMPRIMIR);

		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "imprimir";
		try {
			// Bajo el adapter del userSession
			PlaFueDatAdapter plaFueDatAdapterVO = (PlaFueDatAdapter) userSession.get(PlaFueDatAdapter.NAME);

			// Si es nulo no se puede continuar
			if (plaFueDatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlaFueDatAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaFueDatAdapter.NAME); 
			}
			
			PrintModel	 print  = EfServiceLocator.getFiscalizacionService().imprimirPlaFueDat(userSession, plaFueDatAdapterVO);
			
			// Tiene errores recuperables
			if (plaFueDatAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaFueDatAdapterVO.infoString()); 
				saveDemodaErrors(request, plaFueDatAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
			}

			// Tiene errores no recuperables
			if (plaFueDatAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + plaFueDatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaFueDatAdapter.NAME, plaFueDatAdapterVO);
			}

			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlaFueDatAdapter.NAME + ": "+ plaFueDatAdapterVO.infoString());
			baseResponsePrintModel(response, print);
			return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					PlaFueDatAdapter.NAME);
		}
	}
	
}
	
