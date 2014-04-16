//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

import java.util.Date;

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
import ar.gov.rosario.siat.def.iface.model.TipObjImpAdapter;
import ar.gov.rosario.siat.def.iface.model.TipObjImpVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarTipObjImpDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTipObjImpDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TipObjImpAdapter tipObjImpAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;

		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTipObjImpAdapterForView(userSession, commonKey)";
				tipObjImpAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_TIPOBJIMP_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getTipObjImpAdapterForUpdate(userSession, commonKey)";
				tipObjImpAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_TIPOBJIMP_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getTipObjImpAdapterForDelete(userSession, commonKey)";
				tipObjImpAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAdapterForView(userSession, commonKey);
				tipObjImpAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.TIPOBJIMP_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_TIPOBJIMP_VIEW_ADAPTER);					
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getTipObjImpAdapterForView(userSession)";
				tipObjImpAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAdapterForView(userSession, commonKey);
				tipObjImpAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.TIPOBJIMP_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_TIPOBJIMP_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getTipObjImpAdapterForView(userSession)";
				tipObjImpAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAdapterForView(userSession, commonKey);
				tipObjImpAdapterVO.getTipObjImp().setFechaBaja(new Date());
				tipObjImpAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.TIPOBJIMP_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_TIPOBJIMP_VIEW_ADAPTER);				
			}
			
			if (act.equals(DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ADM_AREA_ORIGEN)) {
				stringServicio = "getTipObjImpAdapterForView(userSession)";
				tipObjImpAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAdapterForView(userSession, commonKey);
				tipObjImpAdapterVO.getTipObjImp().setFechaBaja(new Date());
				actionForward = mapping.findForward(DefConstants.FWD_TIPOBJIMPAREO_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			if (tipObjImpAdapterVO == null){
				log.debug(funcName + ": " + stringServicio + " RETURN NULL");
				return actionForward;
			}
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tipObjImpAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tipObjImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAdapter.NAME, tipObjImpAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tipObjImpAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TipObjImpAdapter.NAME + ": "+ tipObjImpAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TipObjImpAdapter.NAME, tipObjImpAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipObjImpAdapter.NAME, tipObjImpAdapterVO);
			
			saveDemodaMessages(request, tipObjImpAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			DefConstants.ACTION_ADMINISTRAR_ENCTIPOBJIMP, BaseConstants.ACT_MODIFICAR);
 
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipObjImpAdapter tipObjImpAdapterVO = (TipObjImpAdapter) userSession.get(TipObjImpAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipObjImpAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipObjImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAdapter.NAME); 
			}

			// llamada al servicio
			TipObjImpVO tipObjImpVO = DefServiceLocator.getObjetoImponibleService().deleteTipObjImp
				(userSession, tipObjImpAdapterVO.getTipObjImp());
			
            // Tiene errores recuperables
			if (tipObjImpVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpAdapterVO.infoString());
				saveDemodaErrors(request, tipObjImpVO);				
				request.setAttribute(TipObjImpAdapter.NAME, tipObjImpAdapterVO);
				return mapping.findForward(DefConstants.FWD_TIPOBJIMP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipObjImpVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipObjImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAdapter.NAME, tipObjImpAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipObjImpAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipObjImpAdapter tipObjImpAdapterVO = (TipObjImpAdapter) userSession.get(TipObjImpAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipObjImpAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipObjImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAdapter.NAME); 
			}

			// llamada al servicio
			TipObjImpVO tipObjImpVO = DefServiceLocator.getObjetoImponibleService().activarTipObjImp
				(userSession, tipObjImpAdapterVO.getTipObjImp());
			
            // Tiene errores recuperables
			if (tipObjImpVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpAdapterVO.infoString());
				saveDemodaErrors(request, tipObjImpVO);				
				request.setAttribute(TipObjImpAdapter.NAME, tipObjImpAdapterVO);
				return mapping.findForward(DefConstants.FWD_TIPOBJIMP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipObjImpVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipObjImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAdapter.NAME, tipObjImpAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipObjImpAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipObjImpAdapter tipObjImpAdapterVO = (TipObjImpAdapter) userSession.get(TipObjImpAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipObjImpAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipObjImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipObjImpAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipObjImpAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpAdapterVO.infoString()); 
				saveDemodaErrors(request, tipObjImpAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipObjImpAdapter.NAME, tipObjImpAdapterVO, DefConstants.FWD_TIPOBJIMP_VIEW_ADAPTER);
			}

			// llamada al servicio
			TipObjImpVO tipObjImpVO = DefServiceLocator.getObjetoImponibleService().desactivarTipObjImp
				(userSession, tipObjImpAdapterVO.getTipObjImp());
			
            // Tiene errores recuperables
			if (tipObjImpVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpAdapterVO.infoString());
				saveDemodaErrors(request, tipObjImpVO);				
				request.setAttribute(TipObjImpAdapter.NAME, tipObjImpAdapterVO);
				return mapping.findForward(DefConstants.FWD_TIPOBJIMP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipObjImpVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipObjImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAdapter.NAME, tipObjImpAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipObjImpAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipObjImpAdapter.NAME);
		
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, TipObjImpAdapter.NAME);
		
	}
	
	// Metodos relacionados TipObjImpAtr

	public ActionForward verTipObjImpAtr(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_TIPOBJIMPATR);

	}

	public ActionForward modificarTipObjImpAtr(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_TIPOBJIMPATR);

	}

	public ActionForward eliminarTipObjImpAtr(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_TIPOBJIMPATR);

	}
	
	public ActionForward agregarTipObjImpAtr(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// seteo la accion y el parametro para volver
		navModel.setPrevAction(mapping.getPath());
		navModel.setPrevActionParameter(BaseConstants.ACT_REFILL);

		// seteo las acciones seleccionar
		navModel.setSelectAction("/def/AdministrarTipObjImpAtr");
		navModel.setSelectActionParameter("inicializar");
		navModel.setSelectAct(BaseConstants.ACT_AGREGAR); 
		
		// seteo que no pueda agregar en la seleccion
		navModel.setAgregarEnSeleccion(false);

		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);

		// Bajo el adapter del userSession
		TipObjImpAdapter tipObjImpAdapterVO = (TipObjImpAdapter) userSession.get(TipObjImpAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (tipObjImpAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + TipObjImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAdapter.NAME); 
		}

		// Seteo la lista de atributos que seran excluidos en la busqueda
		userSession.getNavModel().setListVOExcluidos(tipObjImpAdapterVO.getTipObjImp().getListAtributo());

		return mapping.findForward(DefConstants.ACTION_BUSCAR_ATRIBUTO);
	}


	// Metodos relacionados TipObjImpAreO

	public ActionForward verTipObjImpAreO(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_TIPOBJIMPAREO);
	}

	public ActionForward eliminarTipObjImpAreO(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_TIPOBJIMPAREO);
	}
	
	public ActionForward agregarTipObjImpAreO(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_TIPOBJIMPAREO);
	}

	public ActionForward activarTipObjImpAreO(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardActivarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_TIPOBJIMPAREO);
		}

	public ActionForward desactivarTipObjImpAreO(ActionMapping mapping, ActionForm form,

			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardDesactivarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_TIPOBJIMPAREO);
		}
	
}
	
