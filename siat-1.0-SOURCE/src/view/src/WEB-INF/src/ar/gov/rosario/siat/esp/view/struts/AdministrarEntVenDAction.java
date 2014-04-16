//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.view.struts;

import java.util.ArrayList;

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
import ar.gov.rosario.siat.emi.iface.model.EmisionPuntualAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionPuntualPreviewAdapter;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import ar.gov.rosario.siat.esp.buss.bean.EntHab;
import ar.gov.rosario.siat.esp.iface.model.EntHabVO;
import ar.gov.rosario.siat.esp.iface.model.EntVenAdapter;
import ar.gov.rosario.siat.esp.iface.model.EntVenVO;
import ar.gov.rosario.siat.esp.iface.model.HabilitacionAdapter;
import ar.gov.rosario.siat.esp.iface.service.EspServiceLocator;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import ar.gov.rosario.siat.esp.view.util.EspConstants;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.CommonNavegableView;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarEntVenDAction extends BaseDispatchAction {

	private static final String PARAM_VOLVER_PREVIEW = "volverPreview";
	private Log log = LogFactory.getLog(AdministrarEntVenDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_ENTVEN, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();
			
			EntVenAdapter entVenAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				// Busca variable origen para detectar si proviene de una declaracion jurada de entradas vendidas
				String origen = (String) userSession.getNavModel().getParameter("origen");
		
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(EspConstants.MTD_ANULAR)) {
					//stringServicio = "getEntVenAdapterForView(userSession)";					
					 entVenAdapterVO = EspServiceLocator.getHabilitacionService().getEntVenAdapterForCreate(userSession, commonKey, origen);
					 //entVenApdater.passErrorMessages(habilitacionAdapterVO);					 
					 actionForward = mapping.findForward(EspConstants.FWD_ENTVEN_EDIT_ADAPTER);						 
				}
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getEntVenAdapterForView(userSession, commonKey)";
					entVenAdapterVO = EspServiceLocator.getHabilitacionService().getEntVenAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(EspConstants.FWD_ENTVEN_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getEntVenAdapterForUpdate(userSession, commonKey)";
					entVenAdapterVO = EspServiceLocator.getHabilitacionService().getEntVenAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(EspConstants.FWD_ENTVEN_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getEntVenAdapterForDelete(userSession, commonKey)";
					entVenAdapterVO = EspServiceLocator.getHabilitacionService().getEntVenAdapterForView
						(userSession, commonKey);//log.debug("EN INICIALIZAR ELIMINAR DE ADM ENTVEN ");
					entVenAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EspError.ENTVEN_LABEL);				
					actionForward = mapping.findForward(EspConstants.FWD_ENTVEN_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getEntVenAdapterForCreate(userSession)";
					entVenAdapterVO = EspServiceLocator.getHabilitacionService().getEntVenAdapterForCreate
										(userSession, commonKey, origen);					
					actionForward = mapping.findForward(EspConstants.FWD_ENTVEN_EDIT_ADAPTER);				
				}
				
				if(entVenAdapterVO.getEntVen().getHabilitacion().getTipoHab().getCodigo().equals("INT")){
					entVenAdapterVO.setEsInterna(1);
					
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (entVenAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + entVenAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, EntVenAdapter.NAME, entVenAdapterVO);
				}
				
				// Cargo valor de idCuenta que debe setear al volver. (La variable sirve para detectar que el origen necesita este id en el selectedId)
				String idCuenta = (String) userSession.getNavModel().getParameter("idCuenta");
				if(idCuenta != null){
					entVenAdapterVO.setIdCuenta(idCuenta);
				}

				
				// Seteo los valores de navegacion en el adapter
				entVenAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + EntVenAdapter.NAME + ": "+ entVenAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(EntVenAdapter.NAME, entVenAdapterVO);
				// Subo el apdater al userSession
				userSession.put(EntVenAdapter.NAME, entVenAdapterVO);
				
				saveDemodaMessages(request, entVenAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, EntVenAdapter.NAME);
			}
		}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				EspSecurityConstants.ABM_ENTVEN, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				EntVenAdapter entVenAdapterVO = (EntVenAdapter) userSession.get(EntVenAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (entVenAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + EntVenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, EntVenAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(entVenAdapterVO, request);
				
	            // Tiene errores recuperables
				if (entVenAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + entVenAdapterVO.infoString()); 
					saveDemodaErrors(request, entVenAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, EntVenAdapter.NAME, entVenAdapterVO);
				}
				
				// llamada al servicio
				EntVenAdapter entVenVO = EspServiceLocator.getHabilitacionService().createEntVen(userSession, entVenAdapterVO);
				
	            // Tiene errores recuperables
				if (entVenVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + entVenVO.infoString()); 
					saveDemodaErrors(request, entVenVO);
					return forwardErrorRecoverable(mapping, request, userSession, EntVenAdapter.NAME, entVenAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (entVenVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + entVenVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, EntVenAdapter.NAME, entVenAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, EntVenAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, EntVenAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				EspSecurityConstants.ABM_ENTVEN, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				EntVenAdapter entVenAdapterVO = (EntVenAdapter) userSession.get(EntVenAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (entVenAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + EntVenAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, EntVenAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(entVenAdapterVO, request);
				
	            // Tiene errores recuperables
				if (entVenAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + entVenAdapterVO.infoString()); 
					saveDemodaErrors(request, entVenAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						EntVenAdapter.NAME, entVenAdapterVO);
				}
				
				// llamada al servicio
				EntVenVO entVenVO = EspServiceLocator.getHabilitacionService().updateEntVen(userSession, entVenAdapterVO.getEntVen());
				
	            // Tiene errores recuperables
				if (entVenVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + entVenAdapterVO.infoString()); 
					saveDemodaErrors(request, entVenVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						EntVenAdapter.NAME, entVenAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (entVenVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + entVenAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						EntVenAdapter.NAME, entVenAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, EntVenAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, EntVenAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				EspSecurityConstants.ABM_ENTVEN, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				EntVenAdapter entVenAdapterVO = (EntVenAdapter) userSession.get(EntVenAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (entVenAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + EntVenAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, EntVenAdapter.NAME); 
				}

				// llamada al servicio
				EntVenVO entVenVO = EspServiceLocator.getHabilitacionService().deleteEntVen(userSession, entVenAdapterVO.getEntVen());
				
	            // Tiene errores recuperables
				if (entVenVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + entVenAdapterVO.infoString());
					saveDemodaErrors(request, entVenVO);				
					request.setAttribute(EntVenAdapter.NAME, entVenAdapterVO);
					return mapping.findForward(EspConstants.FWD_ENTVEN_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (entVenVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + entVenAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						EntVenAdapter.NAME, entVenAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, EntVenAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, EntVenAdapter.NAME);
			}
		}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		if(request.getParameter("isOTT")!=null){
			UserSession userSession = getCurrentUserSession(request, mapping);
			EntVenAdapter entVenAdapterVO = (EntVenAdapter) userSession.get(EntVenAdapter.NAME);
			request.setAttribute(EntVenAdapter.NAME, entVenAdapterVO);
			entVenAdapterVO.setListEntHab( (ArrayList<EntHabVO>)
					ListUtilBean.toVO(EntHab.getListByHabilitacion(entVenAdapterVO.getEntVen().getHabilitacion().getId()),2));			
			
			return mapping.findForward(EspConstants.FWD_ENTVEN_EDIT_ADAPTER);
		}
		
		// Se verifica si se cargo el parametro 'idCuenta'. Si es asi, significa que proviene de una declaracion jurada de entradas vendidas y se debe restaurar el selectedId
		// con el valor del parametro
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		EntVenAdapter entVenAdapterVO = (EntVenAdapter) userSession.get(EntVenAdapter.NAME);
		String idCuenta = entVenAdapterVO.getIdCuenta();
		log.debug("................................................()()()()() => idCuenta:"+idCuenta);
		if(idCuenta != null){
			// Se carga el id de cuenta en la variable selectedId
			//userSession.getNavModel().setSelectedId(idCuenta);
			
			NavModel navModel = userSession.getNavModel();		
			try {
				CommonNavegableView cnv = (CommonNavegableView) userSession.get(EntVenAdapter.NAME);
				
				// Si es null, viene de un Refresh o esta mal seteado
				if (cnv == null) {
					String currentActionForRefresh = (String)userSession.get("currentActionForRefresh");
					String currentParameterForRefresh = (String) userSession.get("currentParameterForRefresh");
					
					return new ActionForward (StringUtil.getActionPath(currentActionForRefresh, 
							currentParameterForRefresh));
				} else {
					// Si no es null, setemaos los parametros para el Refresh
					userSession.put("currentActionForRefresh", cnv.getPrevAction());
					userSession.put("currentParameterForRefresh", cnv.getPrevActionParameter());
				
					userSession.remove(EntVenAdapter.NAME);
				
					// limpio el mapa de parametros, la lista de excluidos el selected id
					//navModel.clearParametersMap();
					//navModel.cleanListVOExcluidos();
					// Se carga el id de cuenta en la variable selectedId
					navModel.setSelectedId(idCuenta);
					CommonNavegableView cnvVolver = (CommonNavegableView) userSession.get(HabilitacionAdapter.NAME);
					cnvVolver.setSelectedId(idCuenta);
				}	
				return new ActionForward (cnv.getAccionVolver());
			}catch (Exception e) {
				log.error("Exception - ", e);
				e.printStackTrace();
				// falta definir llamada o no a logout 
				return (mapping.findForward(BaseConstants.FWD_ERROR_NAVEGACION));
			}
		}
		
		return baseVolver(mapping, form, request, response, EntVenAdapter.NAME);
	}

	public ActionForward calcular(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);
	
		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_ENTVEN, EspSecurityConstants.MTD_CALCULAR);
		
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "calcular";
		try {
			EntVenAdapter entVenAdapterVO = (EntVenAdapter)userSession.get(EntVenAdapter.NAME);
			
			entVenAdapterVO.clearErrorMessages();
			
			DemodaUtil.populateVO(entVenAdapterVO, request);
		
			int i=0;
			for (EntHabVO entHabVO: entVenAdapterVO.getListEntHab()){
				
				log.debug( " *********************  i= "+ i+" " + entHabVO.getId() );
				
				log.debug( request.getParameter("vendidas" + entHabVO.getId()));
				
				// Vendidas
				String vendidasString = request.getParameter("vendidas" + entHabVO.getId());					
				try{
					Integer vendidas = null;
											
					// Si es "" o espacios, nuleo el valor.
					 if(StringUtil.isNullOrEmpty(vendidasString) ){
						entVenAdapterVO.getListEntHab().get(i).setVendidas(0);
						entVenAdapterVO.getListEntHab().get(i).setVendidasView("0");
					 }else{
						vendidas = new Integer(vendidasString);
						if(entHabVO.getTotalRestantes()<vendidas){
							entVenAdapterVO.addRecoverableValueError("Cantidad vendidas para la entrada habilitada " + i + " no puede ser mayor que la cantidad restantes.");
						}else{
							entVenAdapterVO.getListEntHab().get(i).setVendidas(vendidas);
							entVenAdapterVO.getListEntHab().get(i).setVendidasView(vendidasString);																							
						}
					 }
		 
					
				} catch (Exception e){
					e.printStackTrace();
					entVenAdapterVO.getListEntHab().get(i).setVendidas(0);					
					entVenAdapterVO.addRecoverableValueError("El formato de la cantidad vendidas para la entrada habilitada  " + i + " es invalido.");
				}
				i++;
			}
			
			// Llamada al servicio
			if (!entVenAdapterVO.hasError()){
				log.debug("antes de llamar al servicio");
				
				 if(canAccess(request, mapping, EmiSecurityConstants.ABM_EMISIONPUNTUAL, EmiConstants.ACT_PREVIEW)==null){
					entVenAdapterVO.addRecoverableValueError("El usuario no posee permisos para generar la deuda");
					saveDemodaErrors(request, entVenAdapterVO);				
					request.setAttribute(EntVenAdapter.NAME, entVenAdapterVO);
					return mapping.findForward(EspConstants.FWD_ENTVEN_EDIT_ADAPTER);
				 }
				
				entVenAdapterVO = EspServiceLocator.getHabilitacionService().calcular(userSession, entVenAdapterVO);

				// Tiene errores recuperables
				if (entVenAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + entVenAdapterVO.infoString());
					saveDemodaErrors(request, entVenAdapterVO);				
					request.setAttribute(EntVenAdapter.NAME, entVenAdapterVO);
					return mapping.findForward(EspConstants.FWD_ENTVEN_EDIT_ADAPTER);

				}
			}
			// Tiene errores no recuperables
			if (entVenAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + entVenAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EntVenAdapter.NAME, entVenAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EntVenAdapter.NAME + ": "+ entVenAdapterVO.infoString());
			
			
			// Envio el VO al request
			request.setAttribute(EntVenAdapter.NAME, entVenAdapterVO);				
			
			userSession.put(EntVenAdapter.NAME, entVenAdapterVO);

			saveDemodaErrors(request, entVenAdapterVO);

			saveDemodaMessages(request, entVenAdapterVO);

			// Comenzamos con la emision puntual
			EmisionPuntualAdapter emisionPuntualAdapter = new EmisionPuntualAdapter();
			if(!entVenAdapterVO.isParamDDJJ()){
				emisionPuntualAdapter.setPrevAction(entVenAdapterVO.getPrevAction());
				emisionPuntualAdapter.setPrevActionParameter(entVenAdapterVO.getPrevActionParameter());				
			}else{
				HabilitacionAdapter habilitacionAdapter = (HabilitacionAdapter) userSession.get(HabilitacionAdapter.NAME);
				emisionPuntualAdapter.setPrevAction(habilitacionAdapter.getPrevAction());
				emisionPuntualAdapter.setPrevActionParameter(habilitacionAdapter.getPrevActionParameter());
			}

			// Envio el VO al request
			request.setAttribute(EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapter);

			// Subo el apdater al userSession
			userSession.put(EmisionPuntualAdapter.ENC_NAME, emisionPuntualAdapter);

			// Seteamos la emision creada en el adapter para Preview
			EmisionPuntualPreviewAdapter emisionPuntualPreviewAdapter = new EmisionPuntualPreviewAdapter();
			emisionPuntualPreviewAdapter.setEmision(entVenAdapterVO.getEmision());
			
			// Envio el VO al request
			request.setAttribute(EmisionPuntualPreviewAdapter.NAME, emisionPuntualPreviewAdapter);

			// Pasamos los mensajes del EntVenAdapter.
			entVenAdapterVO.passErrorMessages(emisionPuntualPreviewAdapter);
			
			
			// Subo el apdater al userSession
			userSession.put(EmisionPuntualPreviewAdapter.NAME, emisionPuntualPreviewAdapter);

			// lo dirijo al adapter de modificacion
			return baseForward(mapping, request, funcName, PARAM_VOLVER_PREVIEW, 
					EmiConstants.ACTION_ADMINISTRAR_EMISIONPUNTUAL_PREVIEW, EmiConstants.ACT_PREVIEW);
			
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,EntVenAdapter.NAME);
		}
	}
	
	public ActionForward volverPreview(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);		
		if (userSession==null) return forwardErrorSession(request);
		
		EntVenAdapter entVenAdapterVO = null;
		try {
			// Bajo el adapter del userSession
			entVenAdapterVO = (EntVenAdapter) userSession.get(EntVenAdapter.NAME);

			// Si es nulo no se puede continuar
			if (entVenAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EntVenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EntVenAdapter.NAME); 
			}
	
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EntVenAdapter.NAME + ": "+ entVenAdapterVO.infoString());
			
			// Limpiamos los errores si habia
			entVenAdapterVO.clearErrorMessages();
			
			// Volvemos atras los cambios que se ingresaron
			for (EntHabVO entHabVO: entVenAdapterVO.getListEntHab()) {
				entHabVO.setTotalRestantes(entHabVO.getTotalRestantes() + entHabVO.getVendidas());				
			}
			
			// Envio el VO al request
			request.setAttribute(EntVenAdapter.NAME, entVenAdapterVO);

			// Subo el apdater al userSession
			userSession.put(EntVenAdapter.NAME, entVenAdapterVO);

			// lo dirijo al adapter de modificacion
			return mapping.findForward(EspConstants.FWD_ENTVEN_EDIT_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EntVenAdapter.NAME);
		}
	}

	public ActionForward calcularOIT(ActionMapping mapping, ActionForm form, 
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);
	
		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_ENTVEN, EspSecurityConstants.MTD_CALCULAR);
		
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "calcularOIT";
		try {
			EntVenAdapter entVenAdapterVO = (EntVenAdapter)userSession.get(EntVenAdapter.NAME);
			
			entVenAdapterVO.clearErrorMessages();
			
			DemodaUtil.populateVO(entVenAdapterVO, request);
		
			int i=0;
			for (EntHabVO entHabVO: entVenAdapterVO.getListEntHab()){
				
				log.debug( " *********************  i= "+ i+" " + entHabVO.getId() );
				
				log.debug( request.getParameter("vendidas" + entHabVO.getId()));
				
				// Vendidas
				String vendidasString = request.getParameter("vendidas" + entHabVO.getId());					
				try{
					Integer vendidas = null;
											
					// Si es "" o espacios, nuleo el valor.
					 if(StringUtil.isNullOrEmpty(vendidasString) ){
						entVenAdapterVO.getListEntHab().get(i).setVendidas(0);
						entVenAdapterVO.getListEntHab().get(i).setVendidasView("0");
					 }else{
						vendidas = new Integer(vendidasString);
						if(entHabVO.getTotalRestantes()<vendidas){
							entVenAdapterVO.addRecoverableValueError("Cantidad vendidas para la entrada habilitada " + i + " no puede ser mayor que la cantidad restantes.");
						}else{
							entVenAdapterVO.getListEntHab().get(i).setVendidas(vendidas);
							entVenAdapterVO.getListEntHab().get(i).setVendidasView(vendidasString);																							
						}
					 }
		 
					
				} catch (Exception e){
					e.printStackTrace();
					entVenAdapterVO.getListEntHab().get(i).setVendidas(0);					
					entVenAdapterVO.addRecoverableValueError("El formato de la cantidad vendidas para la entrada habilitada  " + i + " es invalido.");
				}
				i++;
			}
			
			// Llamada al servicio
			if (!entVenAdapterVO.hasError()){
				log.debug("antes de llamar al servicio");
				entVenAdapterVO = EspServiceLocator.getHabilitacionService().calcularOIT(userSession, entVenAdapterVO);
			}
			
			// Tiene errores recuperables
			if (entVenAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + entVenAdapterVO.infoString());
				saveDemodaErrors(request, entVenAdapterVO);				
				request.setAttribute(EntVenAdapter.NAME, entVenAdapterVO);
				
				return mapping.findForward(EspConstants.FWD_ENTVEN_EDIT_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (entVenAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + entVenAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EntVenAdapter.NAME, entVenAdapterVO);
			}
			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EntVenAdapter.NAME + ": "+ entVenAdapterVO.infoString());
			
			
			// Envio el VO al request
			request.setAttribute(EntVenAdapter.NAME, entVenAdapterVO);				
			
			userSession.put(EntVenAdapter.NAME, entVenAdapterVO);

			saveDemodaErrors(request, entVenAdapterVO);
			saveDemodaMessages(request, entVenAdapterVO);
			
			if(request.getParameter("isOTT")==null){
				return mapping.findForward(EspConstants.FWD_ENTVEN_OTRINGTES_ADAPTER);
			}else{
				return	 mapping.findForward(EspConstants.FWD_ENTVEN_EDIT_ADAPTER);						 
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					EntVenAdapter.NAME);
		}
	}
				
	public ActionForward anular(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);
	
		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_ENTVEN, EspSecurityConstants.MTD_ANULAR);
		
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "anular";
		try {
			EntVenAdapter entVenAdapterVO = (EntVenAdapter)userSession.get(EntVenAdapter.NAME);
			
			entVenAdapterVO.clearErrorMessages();
			
			DemodaUtil.populateVO(entVenAdapterVO, request);
			// Recuperamos datos del form en el vo
			//Integer cantidadVendidas = entVenAdapterVO.getListEntHab().size();
			int i=0;
			for (EntHabVO entHabVO: entVenAdapterVO.getListEntHab()){
				
				log.debug( " *********************  i= "+ i+" " + entHabVO.getId() );
				
				log.debug( request.getParameter("anuladas" + entHabVO.getId()));
				
				// Anuladas
				String anuladasString = request.getParameter("anuladas" + entHabVO.getId());
				//if(entHabVO.getTotalRestantes()>0){
					try{
						Integer anuladas = null;
												
						// Si es "" o espacios, nuleo el valor.
		
						// Validacion de Formato
			
							 if(StringUtil.isNullOrEmpty(anuladasString) ){
								entVenAdapterVO.getListEntHab().get(i).setAnuladas(0);
								entVenAdapterVO.getListEntHab().get(i).setAnuladasView("0");
							 }else{
								anuladas = new Integer(anuladasString);
								if(entHabVO.getTotalRestantes()<anuladas){
									entVenAdapterVO.addRecoverableValueError("Cantidad anuladas para la entrada habilitada " + i + " no puede ser mayor que la cantidad vendidas.");
									
								}else{
									entVenAdapterVO.getListEntHab().get(i).setAnuladas(anuladas);
									entVenAdapterVO.getListEntHab().get(i).setAnuladasView(anuladasString);
									//entVenAdapterVO.getListEntHab().get(i).setTotalRestantes(entVenAdapterVO.getListEntHab().get(i).getTotalRestantes()-vendidas);																
								}
							 }
			 
						
					} catch (Exception e){
						e.printStackTrace();
						entVenAdapterVO.getListEntHab().get(i).setAnuladas(0);
						//entVenAdapterVO.getListEntHab().get(i).setVendidasView(vendidasString);
						entVenAdapterVO.addRecoverableValueError("El formato de la cantidad anulada para la entrada habilitada  " + i + " es invalido.");
					}
					i++;
				}
			//}
			
			// Llamada al servicio
			if (!entVenAdapterVO.hasError()){
				log.debug("antes de llamar al servicio");
				entVenAdapterVO = EspServiceLocator.getHabilitacionService().anular(userSession, entVenAdapterVO);
			}
			// Tiene errores no recuperables
			if (entVenAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + entVenAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EntVenAdapter.NAME, entVenAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EntVenAdapter.NAME + ": "+ entVenAdapterVO.infoString());
			
			// Tiene errores recuperables
			if (entVenAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + entVenAdapterVO.infoString());
				saveDemodaErrors(request, entVenAdapterVO);				
				request.setAttribute(EntVenAdapter.NAME, entVenAdapterVO);
				return mapping.findForward(EspConstants.FWD_ENTVEN_EDIT_ADAPTER);
			}
			
			// Envio el VO al request
			request.setAttribute(EntVenAdapter.NAME, entVenAdapterVO);
			
			userSession.put(EntVenAdapter.NAME, entVenAdapterVO);
			
			saveDemodaErrors(request, entVenAdapterVO);
			saveDemodaMessages(request, entVenAdapterVO);
			return forwardConfirmarOk(mapping, request, funcName, EntVenAdapter.NAME);
			//return forwardConfirmarOk(mapping, request, funcName, HabilitacionAdapter.NAME);//mapping.findForward(EspConstants.FWD_ENTVEN_VIEW_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					EntVenAdapter.NAME);
		}
	}

	public ActionForward paramArea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				EntVenAdapter entVenAdapter = (EntVenAdapter) userSession.get(EntVenAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (entVenAdapter == null) {
					log.error("error en: "  + funcName + ": " + EntVenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, EntVenAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(entVenAdapter, request);
				
	            // Tiene errores recuperables
				if (entVenAdapter.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + entVenAdapter.infoString()); 
					saveDemodaErrors(request, entVenAdapter);
					return forwardErrorRecoverable(mapping, request, userSession, EntVenAdapter.NAME, entVenAdapter);
				}
				
				// Llamada al servicio
				entVenAdapter = EspServiceLocator.getHabilitacionService().paramArea(userSession, entVenAdapter);
				
	            // Tiene errores recuperables
				if (entVenAdapter.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + entVenAdapter.infoString()); 
					saveDemodaErrors(request, entVenAdapter);
					return forwardErrorRecoverable(mapping, request, userSession, EntVenAdapter.NAME, entVenAdapter);
				}
				
				// Tiene errores no recuperables
				if (entVenAdapter.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + entVenAdapter
							.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, EntVenAdapter.NAME, entVenAdapter);
				}

				// Envio el VO al request
				request.setAttribute(EntVenAdapter.NAME, entVenAdapter);
				// Subo el adapter al userSession
				userSession.put(EntVenAdapter.NAME, entVenAdapter);
				
				saveDemodaMessages(request, entVenAdapter);
				
				if(request.getParameter("isOTT")!=null){
					return mapping.findForward(EspConstants.FWD_ENTVEN_OTRINGTES_ADAPTER);
				}else{ //if(act.equals("agregar")){
					return mapping.findForward(EspConstants.FWD_ENTVEN_EDIT_ADAPTER);
				}
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, EntVenAdapter.NAME);
			}
	}

	public ActionForward agregarOTT(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping,
			EspSecurityConstants.ABM_ENTVEN, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EntVenAdapter entVenAdapterVO = (EntVenAdapter) userSession.get(EntVenAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (entVenAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EntVenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EntVenAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(entVenAdapterVO, request);
			
            // Tiene errores recuperables
			if (entVenAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + entVenAdapterVO.infoString()); 
				saveDemodaErrors(request, entVenAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EntVenAdapter.NAME, entVenAdapterVO);
			}
			
			// llamada al servicio
			EntVenAdapter entVenVO = EspServiceLocator.getHabilitacionService().createEntVenOIT(userSession, entVenAdapterVO);
			
          	// Tiene errores recuperables
			if (entVenAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + entVenAdapterVO.infoString());
				saveDemodaErrors(request, entVenAdapterVO);				
				request.setAttribute(EntVenAdapter.NAME, entVenAdapterVO);
				
				return mapping.findForward(EspConstants.FWD_ENTVEN_OTRINGTES_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (entVenVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + entVenVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EntVenAdapter.NAME, entVenAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EntVenAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EntVenAdapter.NAME);
		}
	}
	
	public ActionForward agregarForInt(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled())
				log.debug("entrando en " + funcName);
		
			UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_ENTVEN, BaseSecurityConstants.AGREGAR);
			
			if (userSession == null) return forwardErrorSession(request);
			
			String stringServicio = "agregarForInt";
			try {
				EntVenAdapter entVenAdapterVO = (EntVenAdapter)userSession.get(EntVenAdapter.NAME);
				
				entVenAdapterVO.clearErrorMessages();
				
				DemodaUtil.populateVO(entVenAdapterVO, request);
			
				int i=0;
				for (EntHabVO entHabVO: entVenAdapterVO.getListEntHab()){
					
					log.debug( " *********************  i= "+ i+" " + entHabVO.getId() );
					
					log.debug( request.getParameter("vendidas" + entHabVO.getId()));
					
					// Vendidas
					String vendidasString = request.getParameter("vendidas" + entHabVO.getId());					
					try{
						Integer vendidas = null;
												
						// Si es "" o espacios, nuleo el valor.
						 if(StringUtil.isNullOrEmpty(vendidasString) ){
							entVenAdapterVO.getListEntHab().get(i).setVendidas(0);
							entVenAdapterVO.getListEntHab().get(i).setVendidasView("0");
						 }else{
							vendidas = new Integer(vendidasString);
							if(entHabVO.getTotalRestantes()<vendidas){
								entVenAdapterVO.addRecoverableValueError("Cantidad vendidas para la entrada habilitada " + i + " no puede ser mayor que la cantidad restantes.");
							}else{
								entVenAdapterVO.getListEntHab().get(i).setVendidas(vendidas);
								entVenAdapterVO.getListEntHab().get(i).setVendidasView(vendidasString);																							
							}
						 }
			 
						
					} catch (Exception e){
						e.printStackTrace();
						entVenAdapterVO.getListEntHab().get(i).setVendidas(0);					
						entVenAdapterVO.addRecoverableValueError("El formato de la cantidad vendidas para la entrada habilitada  " + i + " es invalido.");
					}
					i++;
				}
				
				// Llamada al servicio
				if (!entVenAdapterVO.hasError()){
					log.debug("antes de llamar al servicio");
					EntVenAdapter entVenVO = EspServiceLocator.getHabilitacionService().createEntVen(userSession, entVenAdapterVO);
					entVenVO.passErrorMessages(entVenAdapterVO);
				}
				
				// Tiene errores recuperables
				if (entVenAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + entVenAdapterVO.infoString());
					saveDemodaErrors(request, entVenAdapterVO);				
					request.setAttribute(EntVenAdapter.NAME, entVenAdapterVO);
					
					return mapping.findForward(EspConstants.FWD_ENTVEN_EDIT_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (entVenAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + entVenAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, EntVenAdapter.NAME, entVenAdapterVO);
				}
				
				
				if (log.isDebugEnabled()) log.debug(funcName + ": " + EntVenAdapter.NAME + ": "+ entVenAdapterVO.infoString());
				
				
				// Envio el VO al request
				//request.setAttribute(EntVenAdapter.NAME, entVenAdapterVO);				
				
				//userSession.put(EntVenAdapter.NAME, entVenAdapterVO);

				//saveDemodaErrors(request, entVenAdapterVO);
				//saveDemodaMessages(request, entVenAdapterVO);
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, EntVenAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception,
						EntVenAdapter.NAME);
			}
		}

}
