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

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAdapter;
import ar.gov.rosario.siat.pad.iface.model.CambiarDomEnvioAdapter;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.SiNo;

public final class AdministrarCambiarDomEnvioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCambiarDomEnvioDAction.class);

	public ActionForward inicializarTGI(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CAMBIAR_DOMICILIO_ENVIO, PadSecurityConstants.MTD_INGRESAR);
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CambiarDomEnvioAdapter cambiarDomEnvioAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			stringServicio = "getCambiarDomEnvioAdapterInitTGI(userSession)";
			cambiarDomEnvioAdapterVO = PadServiceLocator.getCuentaService().getCambiarDomEnvioAdapterInitTGI(userSession); 
			actionForward = mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_INGRESO);
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cambiarDomEnvioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			}

			// Seteo los valores de navegacion en el adapter
			//cambiarDomEnvioAdapterVO.setValuesFromNavModel(navModel);
			cambiarDomEnvioAdapterVO.setPrevAction("/pad/AdministrarCambiarDomEnvio");
			cambiarDomEnvioAdapterVO.setPrevActionParameter("inicializarTGI");
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CambiarDomEnvioAdapter.NAME + ": "+ cambiarDomEnvioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			
			// Subo el apdater al userSession
			userSession.put(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			 
			saveDemodaMessages(request, cambiarDomEnvioAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambiarDomEnvioAdapter.NAME);
		}
	}
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CAMBIAR_DOMICILIO_ENVIO, PadSecurityConstants.MTD_INGRESAR);
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			CambiarDomEnvioAdapter cambiarDomEnvioAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				CommonKey idRecurso = new CommonKey(request.getParameter("id"));
				userSession.setIdRecurso(idRecurso.getId().toString());
				
				stringServicio = "getCambiarDomEnvioAdapterInit(userSession, commonKey)";
				cambiarDomEnvioAdapterVO = PadServiceLocator.getCuentaService().getCambiarDomEnvioAdapterInit(userSession, idRecurso); 
				actionForward = mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_INGRESO);
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (cambiarDomEnvioAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cambiarDomEnvioAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
				}
				
				// Verificamos en la lista de IdRecurso del parametro para no requerir el codigo de gestion personal (Solicitado en mantis 5105)
				String listIdRecSinCodGesPer = null;
				try{ listIdRecSinCodGesPer = SiatParam.getString(SiatParam.LISTA_ID_REC_SIN_COD_GES_PER); }catch (Exception e) {}
				String idRecursoStr = "|" + cambiarDomEnvioAdapterVO.getCuenta().getRecurso().getId().toString() + "|";
				if (!StringUtil.isNullOrEmpty(listIdRecSinCodGesPer) && listIdRecSinCodGesPer.indexOf(idRecursoStr) >= 0){
					cambiarDomEnvioAdapterVO.setCodGesPerRequerido(false);
				}else{
					cambiarDomEnvioAdapterVO.setCodGesPerRequerido(true);
				}
				

				// Seteo los valores de navegacion en el adapter
				//cambiarDomEnvioAdapterVO.setValuesFromNavModel(navModel);
				cambiarDomEnvioAdapterVO.setPrevAction("/pad/AdministrarCambiarDomEnvio");
				cambiarDomEnvioAdapterVO.setPrevActionParameter("inicializar&id=" + idRecurso.getId());
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + CambiarDomEnvioAdapter.NAME + ": "+ cambiarDomEnvioAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
				
				// Subo el apdater al userSession
				userSession.put(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
				 
				saveDemodaMessages(request, cambiarDomEnvioAdapterVO);
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CambiarDomEnvioAdapter.NAME);
			}
		}
	
	public ActionForward ingresar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			PadSecurityConstants.ABM_CAMBIAR_DOMICILIO_ENVIO, PadSecurityConstants.MTD_INGRESAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CambiarDomEnvioAdapter cambiarDomEnvioAdapterVO = (CambiarDomEnvioAdapter) userSession.get(CambiarDomEnvioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cambiarDomEnvioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CambiarDomEnvioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CambiarDomEnvioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cambiarDomEnvioAdapterVO, request);
			
            // Tiene errores recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambiarDomEnvioAdapterVO.infoString()); 
				saveDemodaErrors(request, cambiarDomEnvioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			}
			 
			// llamada al servicio
			cambiarDomEnvioAdapterVO = PadServiceLocator.getCuentaService().getCambiarDomEnvioIngresar(userSession, cambiarDomEnvioAdapterVO);  
			
            // Tiene errores recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambiarDomEnvioAdapterVO.infoString()); 
				saveDemodaErrors(request, cambiarDomEnvioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cambiarDomEnvioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			}
			
			// seteo que vuelva al ingreso
			//cambiarDomEnvioAdapterVO.setForwardVolverWizard(PadConstants.FWD_CAMBIAR_DOM_ENVIO_INGRESO);

			// Envio el VO al request
			request.setAttribute(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);

			// Fue Exitoso
			return mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_CUENTAS_REL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambiarDomEnvioAdapter.NAME);
		}
	}

	public ActionForward seleccionarCuentas(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			PadSecurityConstants.ABM_CAMBIAR_DOMICILIO_ENVIO, PadSecurityConstants.MTD_INGRESAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CambiarDomEnvioAdapter cambiarDomEnvioAdapterVO = (CambiarDomEnvioAdapter) 
				userSession.get(CambiarDomEnvioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cambiarDomEnvioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CambiarDomEnvioAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CambiarDomEnvioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cambiarDomEnvioAdapterVO, request);

			// cargo los id de cuentas seleccionadas.
			String[] listIdSeleccionados = request.getParameterValues("listIdCuentasSeleccionadas");

			// si se selecciono algun elemento
			if (listIdSeleccionados != null ) {
				cambiarDomEnvioAdapterVO.marcarCuentasSeleccionadas(listIdSeleccionados);
			}
			
			// si no se seleccionaron elementos			
			if (listIdSeleccionados == null ) {
				cambiarDomEnvioAdapterVO.limpiarMarcasCuentas();
			}
			
			// valido la seleccion de cuentas
			cambiarDomEnvioAdapterVO.validateSeleccionarCuentas();

			// Tiene errores no recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cambiarDomEnvioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			}

            // Tiene errores recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambiarDomEnvioAdapterVO.infoString()); 
				saveDemodaErrors(request, cambiarDomEnvioAdapterVO);
				request.setAttribute(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);				
				return mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_CUENTAS_REL);
			}

			// seteo el forward a donde tiene que volver
			cambiarDomEnvioAdapterVO.setForwardVolverWizard(PadConstants.FWD_CAMBIAR_DOM_ENVIO_CUENTAS_REL);

			request.setAttribute(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);			

			// Fue Exitoso
			return mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_NUEVO_DOM);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambiarDomEnvioAdapter.NAME);
		}
	}

	/** Se ejecuta cuando el usuario hace click en el
	 *  radio buton rosario
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward seleccionarRosario(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CAMBIAR_DOMICILIO_ENVIO, PadSecurityConstants.MTD_INGRESAR); 
		if (userSession==null) return forwardErrorSession(request);

		try {

			// Bajo el adapter del userSession
			CambiarDomEnvioAdapter cambiarDomEnvioAdapterVO = (CambiarDomEnvioAdapter) 
				userSession.get(CambiarDomEnvioAdapter.NAME);

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cambiarDomEnvioAdapterVO, request);

            // Tiene errores recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambiarDomEnvioAdapterVO.infoString()); 
				saveDemodaErrors(request, cambiarDomEnvioAdapterVO);
				mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_NUEVO_DOM);
			}

			// seteo los valores necesarios al seleccionar rosario
			cambiarDomEnvioAdapterVO.seleccionarRosario();

			request.setAttribute(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);			
			// Fue Exitoso
			return mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_NUEVO_DOM);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambiarDomEnvioAdapter.NAME);
		}
	}

	/** Se ejecuta cuando el usuario hace click en el radio button
	 *  otra localidad
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward seleccionarOtraLocalidad(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CAMBIAR_DOMICILIO_ENVIO, PadSecurityConstants.MTD_INGRESAR); 
		if (userSession==null) return forwardErrorSession(request);

		try {

			// Bajo el adapter del userSession
			CambiarDomEnvioAdapter cambiarDomEnvioAdapterVO = (CambiarDomEnvioAdapter) 
				userSession.get(CambiarDomEnvioAdapter.NAME);

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cambiarDomEnvioAdapterVO, request);

            // Tiene errores recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambiarDomEnvioAdapterVO.infoString()); 
				saveDemodaErrors(request, cambiarDomEnvioAdapterVO);
				mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_NUEVO_DOM);
			}

			// seteo los valores necesarios
			cambiarDomEnvioAdapterVO.seleccionarOtraLocalidad();

			request.setAttribute(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);			
			// Fue Exitoso
			return mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_NUEVO_DOM);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambiarDomEnvioAdapter.NAME);
		}
	}

	/** Se ejecuta cuando el usuario hace click en el boton 
	 *  buscar localidad
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward buscarOtraLocalidad(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			PadSecurityConstants.ABM_CAMBIAR_DOMICILIO_ENVIO, PadSecurityConstants.MTD_INGRESAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CambiarDomEnvioAdapter cambiarDomEnvioAdapterVO = (CambiarDomEnvioAdapter) 
				userSession.get(CambiarDomEnvioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cambiarDomEnvioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + 
					CambiarDomEnvioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CambiarDomEnvioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cambiarDomEnvioAdapterVO, request);

            // Tiene errores recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambiarDomEnvioAdapterVO.infoString()); 
				saveDemodaErrors(request, cambiarDomEnvioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			}

			// llamada al servicio
			cambiarDomEnvioAdapterVO = PadServiceLocator.getCuentaService().getCambiarDomEnvioBuscarOtraLocalidad
				(userSession, cambiarDomEnvioAdapterVO);

            // Tiene errores recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambiarDomEnvioAdapterVO.infoString());
				saveDemodaErrors(request, cambiarDomEnvioAdapterVO);				
				request.setAttribute(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
				return mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_NUEVO_DOM);
			}
			
			// Tiene errores no recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cambiarDomEnvioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			}

			// Envio el VO al request
			request.setAttribute(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			// Fue Exitoso
			return mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_NUEVO_DOM);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambiarDomEnvioAdapter.NAME);
		}	
	}

	/** Se ejecuta cuando el usuario selecciona un localidad
	 *  de la lista de localidades
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward seleccionarLocalidad(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			PadSecurityConstants.ABM_CAMBIAR_DOMICILIO_ENVIO, PadSecurityConstants.MTD_INGRESAR); 
		if (userSession==null) return forwardErrorSession(request);

		try {

			NavModel navModel = userSession.getNavModel();
			// Bajo el adapter del userSession
			CambiarDomEnvioAdapter cambiarDomEnvioAdapterVO = (CambiarDomEnvioAdapter) 
				userSession.get(CambiarDomEnvioAdapter.NAME);

			// Si es nulo no se puede continuar
			if (cambiarDomEnvioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + 
						CambiarDomEnvioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CambiarDomEnvioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cambiarDomEnvioAdapterVO, request);

            // Tiene errores recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambiarDomEnvioAdapterVO.infoString()); 
				saveDemodaErrors(request, cambiarDomEnvioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			}

			//recupero la localidad seleccionada
			CommonKey localidadKey = new CommonKey( navModel.getSelectedId() );
			
			// recupero la localidad seleccionada y se la seteo al domicilio
			cambiarDomEnvioAdapterVO.setLocalidadById(localidadKey.getId());

			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				TipObjImpAdapter.NAME + ": "+ cambiarDomEnvioAdapterVO.infoString());

			// Envio el VO al request
			request.setAttribute(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			saveDemodaMessages(request, cambiarDomEnvioAdapterVO);
			
			// Fue Exitoso
			return mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_NUEVO_DOM);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambiarDomEnvioAdapter.NAME);
		}
	}
	
	/** Se ejecuta cuando el usuario seleccionas un calle
	 *  de la lista de calles
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward seleccionarCalle(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			PadSecurityConstants.ABM_CAMBIAR_DOMICILIO_ENVIO, PadSecurityConstants.MTD_INGRESAR); 
		if (userSession==null) return forwardErrorSession(request);

		try {

			NavModel navModel = userSession.getNavModel();
			// Bajo el adapter del userSession
			CambiarDomEnvioAdapter cambiarDomEnvioAdapterVO = (CambiarDomEnvioAdapter) 
				userSession.get(CambiarDomEnvioAdapter.NAME);

			// Si es nulo no se puede continuar
			if (cambiarDomEnvioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + 
						CambiarDomEnvioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CambiarDomEnvioAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cambiarDomEnvioAdapterVO, request);

			//recupero la calle seleccionada
			Long idCalle = new Long( navModel.getSelectedId() );
			cambiarDomEnvioAdapterVO.getCamDomWeb().getDomNue().getCalle().setId(idCalle);
			
			// valido los requeridos
			cambiarDomEnvioAdapterVO.validateNuevoDom();

			// si notiene errores llamada al servicio
			if (!cambiarDomEnvioAdapterVO.hasError()) {
				// llamada al servicio
				cambiarDomEnvioAdapterVO = PadServiceLocator.getCuentaService().cambiarDomEnvioSeleccionarCalle
					(userSession, cambiarDomEnvioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				TipObjImpAdapter.NAME + ": "+ cambiarDomEnvioAdapterVO.infoString());

            // Tiene errores recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambiarDomEnvioAdapterVO.infoString());
				saveDemodaErrors(request, cambiarDomEnvioAdapterVO);				
				request.setAttribute(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
				return mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_NUEVO_DOM);
			}
			
			// Tiene errores no recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cambiarDomEnvioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			}

			// Envio el VO al request
			request.setAttribute(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			
			saveDemodaMessages(request, cambiarDomEnvioAdapterVO);

			// seteo que vuelva a la seleccion de cuentas relacionadas
			cambiarDomEnvioAdapterVO.setForwardVolverWizard(PadConstants.FWD_CAMBIAR_DOM_ENVIO_NUEVO_DOM);

			return mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_SOLICITANTE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambiarDomEnvioAdapter.NAME);
		}
	}

	/** Se ejecuata cuando el usuario hace click en el boton siguiente en
	 *  en la pagina de ingreso del nuevo domicilio
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward cargarNuevoDomicilio(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			PadSecurityConstants.ABM_CAMBIAR_DOMICILIO_ENVIO, PadSecurityConstants.MTD_INGRESAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CambiarDomEnvioAdapter cambiarDomEnvioAdapterVO = 
				(CambiarDomEnvioAdapter) userSession.get(CambiarDomEnvioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cambiarDomEnvioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + 
					CambiarDomEnvioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CambiarDomEnvioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cambiarDomEnvioAdapterVO, request);
			// recupero el bis
			String bis = request.getParameter("camDomWeb.domNue.bisView");
			SiNo SiNoBis = SiNo.NO;
			
			if (bis != null && bis.equalsIgnoreCase("on")) {
				SiNoBis = SiNo.SI;
			}

			cambiarDomEnvioAdapterVO.getCamDomWeb().getDomNue().setBis(SiNoBis);

			// valido los requeridos
			cambiarDomEnvioAdapterVO.validateNuevoDom();

			// si notiene errores llamada al servicio
			if (!cambiarDomEnvioAdapterVO.hasError()) {
				cambiarDomEnvioAdapterVO = PadServiceLocator.getCuentaService().cambiarDomEnvioCargarNuevoDom
					(userSession, cambiarDomEnvioAdapterVO);
			}

            // Tiene errores recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambiarDomEnvioAdapterVO.infoString());
				saveDemodaErrors(request, cambiarDomEnvioAdapterVO);				
				request.setAttribute(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
				return mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_NUEVO_DOM);
			}

			// Tiene errores no recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cambiarDomEnvioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			}

			// Envio el VO al request
			request.setAttribute(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			
			saveDemodaMessages(request, cambiarDomEnvioAdapterVO);

			// si hay calles en la lista las muestro
			if (cambiarDomEnvioAdapterVO.getViewResultCalles()) {
				return mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_NUEVO_DOM);
			}
			
			// seteo la pantalla a donde volver
			cambiarDomEnvioAdapterVO.setForwardVolverWizard(PadConstants.FWD_CAMBIAR_DOM_ENVIO_NUEVO_DOM);

			return mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_SOLICITANTE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambiarDomEnvioAdapter.NAME);
		}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			PadSecurityConstants.ABM_CAMBIAR_DOMICILIO_ENVIO, PadSecurityConstants.MTD_INGRESAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CambiarDomEnvioAdapter cambiarDomEnvioAdapterVO = (CambiarDomEnvioAdapter) 
				userSession.get(CambiarDomEnvioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cambiarDomEnvioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CambiarDomEnvioAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CambiarDomEnvioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cambiarDomEnvioAdapterVO, request);

            // Tiene errores recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + 
					cambiarDomEnvioAdapterVO.infoString()); 
				saveDemodaErrors(request, cambiarDomEnvioAdapterVO);
				request.setAttribute(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
				return mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_SOLICITANTE);			
			}
			
			// valido el solicitante
			cambiarDomEnvioAdapterVO.validateSolicitante();

			// si no hay errores llamo al servicio
			if (!cambiarDomEnvioAdapterVO.hasError()) {
				cambiarDomEnvioAdapterVO.getCamDomWeb().setEsOrigenWeb(SiNo.SI);
				cambiarDomEnvioAdapterVO = PadServiceLocator.getCuentaService().updateDomEnvio
					(userSession, cambiarDomEnvioAdapterVO);
			}
			
            // Tiene errores recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambiarDomEnvioAdapterVO.infoString());
				saveDemodaErrors(request, cambiarDomEnvioAdapterVO);				
				request.setAttribute(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
				return mapping.findForward(PadConstants.FWD_CAMBIAR_DOM_ENVIO_SOLICITANTE);
			}
			
			// Tiene errores no recuperables
			if (cambiarDomEnvioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cambiarDomEnvioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CambiarDomEnvioAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambiarDomEnvioAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		// Bajo el adapter del userSession
		CambiarDomEnvioAdapter cambiarDomEnvioAdapterVO = (CambiarDomEnvioAdapter) 
			userSession.get(CambiarDomEnvioAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (cambiarDomEnvioAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + CambiarDomEnvioAdapter.NAME 
				+ " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, CambiarDomEnvioAdapter.NAME); 
		}

		String forwardVolverWizard = cambiarDomEnvioAdapterVO.obtainForwardVolverWizard();

		log.debug("forwardVolverWizard = " + forwardVolverWizard);
		if (!StringUtil.isNullOrEmpty(forwardVolverWizard)){
			request.setAttribute(CambiarDomEnvioAdapter.NAME, cambiarDomEnvioAdapterVO);
			return mapping.findForward(forwardVolverWizard);
		}
		
		return baseVolver(mapping, form, request, response, CambiarDomEnvioAdapter.NAME);
		
	}
}
