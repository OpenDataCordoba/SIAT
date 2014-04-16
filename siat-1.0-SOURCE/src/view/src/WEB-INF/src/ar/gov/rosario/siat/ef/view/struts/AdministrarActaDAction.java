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
import ar.gov.rosario.siat.ef.buss.bean.Acta;
import ar.gov.rosario.siat.ef.buss.bean.TipoActa;
import ar.gov.rosario.siat.ef.iface.model.ActaAdapter;
import ar.gov.rosario.siat.ef.iface.model.ActaVO;
import ar.gov.rosario.siat.ef.iface.model.OrdConDocVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarActaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarActaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ACTA, act);		
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();
		ActaAdapter actaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {

			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getActaAdapterForView(userSession, commonKey)";
				actaAdapterVO = EfServiceLocator.getFiscalizacionService().getActaAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_ACTA_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getActaAdapterForUpdate(userSession, commonKey)";
				actaAdapterVO = EfServiceLocator.getFiscalizacionService().getActaAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_ACTA_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getActaAdapterForDelete(userSession, commonKey)";
				actaAdapterVO = EfServiceLocator.getFiscalizacionService().getActaAdapterForView(userSession, commonKey);
				actaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.ACTA_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_ACTA_VIEW_ADAPTER);					
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables

			// Tiene errores no recuperables
			if (actaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + actaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ActaAdapter.NAME, actaAdapterVO);
			}

			// setea a que parte de la pantalla vuelve
			userSession.put("irA", "bloqueActas");

			// Seteo los valores de navegacion en el adapter
			actaAdapterVO.setValuesFromNavModel(navModel);

			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					ActaAdapter.NAME + ": " + actaAdapterVO.infoString());

			// Envio el VO al request
			request.setAttribute(ActaAdapter.NAME, actaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ActaAdapter.NAME, actaAdapterVO);

			saveDemodaMessages(request, actaAdapterVO);			

			return actionForward;

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ActaAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
				EfSecurityConstants.ABM_ACTA, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			ActaAdapter actaAdapterVO = (ActaAdapter) userSession.get(ActaAdapter.NAME);

			// Si es nulo no se puede continuar
			if (actaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ActaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ActaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(actaAdapterVO, request);

			// Tiene errores recuperables
			if (actaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actaAdapterVO.infoString()); 
				saveDemodaErrors(request, actaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ActaAdapter.NAME, actaAdapterVO);
			}

			if(request.getParameter("idsOrdConDocSelected")==null){
				actaAdapterVO.setIdsOrdConDocSelected(null);
			}

			// llamada al servicio
			ActaVO actaVO = EfServiceLocator.getFiscalizacionService().updateActa(userSession, actaAdapterVO);

			// Tiene errores recuperables
			if (actaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actaAdapterVO.infoString()); 
				saveDemodaErrors(request, actaVO);
				return forwardErrorRecoverable(mapping, request, userSession, ActaAdapter.NAME, actaAdapterVO);
			}

			// Tiene errores no recuperables
			if (actaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + actaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ActaAdapter.NAME, actaAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ActaAdapter.NAME,
					EfConstants.PATH_ADMINISTRAR_ORDENCONTROLFIS,"inicializar",EfConstants.ACT_ADMINISTRAR);
			// el id de la ordenControl se envia en el boton modificar del actaAdapter.jsp

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ActaAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
				EfConstants.ACTION_ADMINISTRAR_ENC_ACTA, BaseConstants.ACT_MODIFICAR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ACTA, 
				BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			ActaAdapter actaAdapterVO = (ActaAdapter) userSession.get(ActaAdapter.NAME);

			// Si es nulo no se puede continuar
			if (actaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ActaAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ActaAdapter.NAME); 
			}

			// llamada al servicio
			ActaVO actaVO = EfServiceLocator.getFiscalizacionService().deleteActa(userSession, actaAdapterVO.getActa());

			// Tiene errores recuperables
			if (actaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actaAdapterVO.infoString());
				saveDemodaErrors(request, actaVO);				
				request.setAttribute(ActaAdapter.NAME, actaAdapterVO);
				return mapping.findForward(EfConstants.FWD_ACTA_VIEW_ADAPTER);
			}

			// Tiene errores no recuperables
			if (actaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + actaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ActaAdapter.NAME, actaAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ActaAdapter.NAME);


		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ActaAdapter.NAME);
		}
	}

	public ActionForward imprimir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ACTA, EfSecurityConstants.IMPRIMIR);

		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "imprimir";
		try {
			// Bajo el adapter del userSession
			ActaAdapter actaAdapterVO = (ActaAdapter) userSession.get(ActaAdapter.NAME);

			// Si es nulo no se puede continuar
			if (actaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ActaAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ActaAdapter.NAME); 
			}
			
			Acta acta = Acta.getById(actaAdapterVO.getActa().getId());
			TipoActa tipoActa= acta.getTipoActa();
			PrintModel print= new PrintModel();
			if (tipoActa.getId().equals(TipoActa.ID_TIPO_REQ_INF)){
				 print  = EfServiceLocator.getFiscalizacionService().imprimirActaReqInf(userSession, actaAdapterVO);
				
			}
			if (tipoActa.getId().equals(TipoActa.ID_TIPO_PROCEDIMIENTO)){
				// llamada al servicio
				 print  = EfServiceLocator.getFiscalizacionService().imprimirActaProc(userSession, actaAdapterVO);
				 log.debug("entro papa");
			}
			if (tipoActa.getId().equals(TipoActa.ID_TIPO_INICIO_PROCEDIMIENTO)){
				// llamada al servicio
				 print  = EfServiceLocator.getFiscalizacionService().imprimirActaIni(userSession, actaAdapterVO);
			}
			
			// Tiene errores recuperables
			if (actaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actaAdapterVO.infoString()); 
				saveDemodaErrors(request, actaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ActaAdapter.NAME, actaAdapterVO);
			}

			// Tiene errores no recuperables
			if (actaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + actaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ActaAdapter.NAME, actaAdapterVO);
			}

			if (log.isDebugEnabled()) log.debug(funcName + ": " + ActaAdapter.NAME + ": "+ actaAdapterVO.infoString());
			baseResponsePrintModel(response, print);
			return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					ActaAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_ORDENCONTROLFIS, 
				BaseSecurityConstants.MODIFICAR);
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			ActaAdapter actaAdapterVO = (ActaAdapter) userSession.get(ActaAdapter.NAME);

			// Si es nulo no se puede continuar
			if (actaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ActaAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ActaAdapter.NAME); 
			}

			// pone en la session el idOrdenControl
			userSession.getNavModel().setSelectedId(actaAdapterVO.getActa().getOrdenControl().getId().toString());
			userSession.getNavModel().setAct(EfConstants.ACT_ADMINISTRAR);
			userSession.getNavModel().setPrevAction(EfConstants.PATH_BUSCAR_ORDENCONTROLFIS);
			userSession.getNavModel().setPrevActionParameter(BaseConstants.ACT_BUSCAR);
			return new ActionForward(EfConstants.PATH_ADMINISTRAR_ORDENCONTROLFIS+".do?method=inicializar");


		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ActaAdapter.NAME);
		}

	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ActaAdapter.NAME);

	}

	// Metodos relacionados OrdConDoc
	public ActionForward eliminarOrdConDoc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_ORDCONDOC);

	}

	public ActionForward agregarOrdConDoc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_ORDCONDOC);

	}

	public ActionForward irModificarObs(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ACTA, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			ActaAdapter actaAdapterVO = (ActaAdapter) userSession.get(ActaAdapter.NAME);

			// Si es nulo no se puede continuar
			if (actaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ActaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ActaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(actaAdapterVO, request);

			// Tiene errores recuperables
			if (actaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actaAdapterVO.infoString()); 
				saveDemodaErrors(request, actaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ActaAdapter.NAME, actaAdapterVO);
			}

			actaAdapterVO.getOrdConDoc().setId(new Long(request.getParameter("selectedId")));

			// llamada al servicio
			actaAdapterVO = EfServiceLocator.getFiscalizacionService().getActaAdapter4UpdateObsDoc(userSession, actaAdapterVO);

			// Tiene errores recuperables
			if (actaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actaAdapterVO.infoString()); 
				saveDemodaErrors(request, actaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ActaAdapter.NAME, actaAdapterVO);
			}

			// Tiene errores no recuperables
			if (actaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + actaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ActaAdapter.NAME, actaAdapterVO);
			}

			request.setAttribute("irA", "actaProcedimiento");

			// Envio el VO al request
			request.setAttribute(ActaAdapter.NAME, actaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ActaAdapter.NAME, actaAdapterVO);

			saveDemodaMessages(request, actaAdapterVO);			

			return mapping.findForward(EfConstants.FWD_ACTA_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ActaAdapter.NAME);
		}
	}

	public ActionForward modificarObs(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ACTA, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			ActaAdapter actaAdapterVO = (ActaAdapter) userSession.get(ActaAdapter.NAME);

			// Si es nulo no se puede continuar
			if (actaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ActaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ActaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(actaAdapterVO, request);

			// Tiene errores recuperables
			if (actaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actaAdapterVO.infoString()); 
				saveDemodaErrors(request, actaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ActaAdapter.NAME, actaAdapterVO);
			}

			// llamada al servicio
			OrdConDocVO ordConDocVO = EfServiceLocator.getFiscalizacionService().updateObsOrdConDoc(userSession, actaAdapterVO.getOrdConDoc());

			// Tiene errores recuperables
			if (ordConDocVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + actaAdapterVO.infoString()); 
				saveDemodaErrors(request, actaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ActaAdapter.NAME, actaAdapterVO);
			}

			// Tiene errores no recuperables
			if (ordConDocVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + actaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ActaAdapter.NAME, actaAdapterVO);
			}

			return new ActionForward(EfConstants.PATH_ADMINISTRAR_ACTA+".do?method=inicializar&selectedId="+actaAdapterVO.getActa().getId());

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ActaAdapter.NAME);
		}
	}

}

