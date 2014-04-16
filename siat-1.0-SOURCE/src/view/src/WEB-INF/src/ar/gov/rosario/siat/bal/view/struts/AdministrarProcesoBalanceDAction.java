//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.buss.bean.EstadoArc;
import ar.gov.rosario.siat.bal.buss.bean.EstadoCom;
import ar.gov.rosario.siat.bal.buss.bean.EstadoFol;
import ar.gov.rosario.siat.bal.iface.model.ArchivoSearchPage;
import ar.gov.rosario.siat.bal.iface.model.AuxCaja7SearchPage;
import ar.gov.rosario.siat.bal.iface.model.BalanceVO;
import ar.gov.rosario.siat.bal.iface.model.CompensacionSearchPage;
import ar.gov.rosario.siat.bal.iface.model.EstadoArcVO;
import ar.gov.rosario.siat.bal.iface.model.EstadoComVO;
import ar.gov.rosario.siat.bal.iface.model.EstadoFolVO;
import ar.gov.rosario.siat.bal.iface.model.FolioSearchPage;
import ar.gov.rosario.siat.bal.iface.model.ProcesoBalanceAdapter;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarProcesoBalanceDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProcesoBalanceDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
			UserSession userSession = getCurrentUserSession(request, mapping);
			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			ProcesoBalanceAdapter procesoBalanceAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
			
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());

				if (navModel.getAct().equals(BalConstants.ACT_ADM_PROCESO_BALANCE)) {
					stringServicio = "getProcesoBalanceAdapterForInit(userSession, commonKey)";
					procesoBalanceAdapterVO = BalServiceLocator.getBalanceService().getProcesoBalanceAdapterInit(userSession, commonKey, null);
					actionForward = mapping.findForward(BalConstants.FWD_PROCESO_BALANCE_ADAPTER);
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (procesoBalanceAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + procesoBalanceAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				procesoBalanceAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcesoBalanceAdapter.NAME + ": "+ procesoBalanceAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ProcesoBalanceAdapter.NAME);
			}
		}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoBalanceAdapter procesoBalanceAdapterVO = (ProcesoBalanceAdapter) userSession.get(ProcesoBalanceAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoBalanceAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoBalanceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoBalanceAdapter.NAME); 
			}
						
			// Llamar a un servicio especifico para el Proceso de Balance
			BalanceVO balanceVO = BalServiceLocator.getBalanceService().activar(userSession, procesoBalanceAdapterVO.getBalance());

			// Tiene errores recuperables
			if (balanceVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + balanceVO.infoString()); 
				saveDemodaErrors(request, balanceVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (balanceVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + balanceVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			}
					
			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoBalanceAdapterVO.getBalance().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_BAL, ProConstants.ACT_ACTIVAR);
	}

	public ActionForward reprogramar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)	throws Exception {

			String funcName = DemodaUtil.currentMethodName();

			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoBalanceAdapter procesoBalanceAdapterVO = (ProcesoBalanceAdapter) userSession.get(ProcesoBalanceAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoBalanceAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoBalanceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoBalanceAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Balance
			BalanceVO balanceVO = BalServiceLocator.getBalanceService().reprogramar(userSession, procesoBalanceAdapterVO.getBalance());

			// Tiene errores recuperables
			if (balanceVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + balanceVO.infoString()); 
				saveDemodaErrors(request, balanceVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (balanceVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + balanceVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			}

			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoBalanceAdapterVO.getBalance().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_BAL, ProConstants.ACT_ACTIVAR);
	}

	public ActionForward cancelar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoBalanceAdapter procesoBalanceAdapterVO = (ProcesoBalanceAdapter) userSession.get(ProcesoBalanceAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoBalanceAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoBalanceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoBalanceAdapter.NAME); 
			}
			
			// Llamar a un servicio especifico para el Proceso de Balance
			BalanceVO balanceVO = BalServiceLocator.getBalanceService().cancelar(userSession, procesoBalanceAdapterVO.getBalance());

			// Tiene errores recuperables
			if (balanceVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + balanceVO.infoString()); 
				saveDemodaErrors(request, balanceVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (balanceVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + balanceVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			}

			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoBalanceAdapterVO.getBalance().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_BAL, ProConstants.ACT_CANCELAR);
	}
	
	public ActionForward reiniciar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoBalanceAdapter procesoBalanceAdapterVO = (ProcesoBalanceAdapter) userSession.get(ProcesoBalanceAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoBalanceAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoBalanceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoBalanceAdapter.NAME); 
			}
					
			return baseForwardAdapter(mapping, request, funcName, BalConstants.FWD_ADMINISTRAR_CORRIDA_PRO_BAL, BalConstants.ACT_REINICIAR);
	}

	public ActionForward retroceder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoBalanceAdapter procesoBalanceAdapterVO = (ProcesoBalanceAdapter) userSession.get(ProcesoBalanceAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoBalanceAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoBalanceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoBalanceAdapter.NAME); 
			}
					
			return baseForwardAdapter(mapping, request, funcName, BalConstants.FWD_ADMINISTRAR_CORRIDA_PRO_BAL, BalConstants.ACT_RETROCEDER);
	}
	
	

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_BALANCE);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, ProcesoBalanceAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ProcesoBalanceAdapter.NAME);
	}
	
	public ActionForward downloadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);

			try {
					
				String fileId = request.getParameter("fileParam");	
				CommonKey commonKey = new CommonKey(fileId);
				
				// Obtenemos el id del archivo seleccionado
				Long idFileCorrida = commonKey.getId();			

				// Obtenemos el nombre del archivo seleccionado mediante una llamada a un servicio
				String fileName = ProServiceLocator.getAdpProcesoService().obtenerFileCorridaName(idFileCorrida);
				
				baseResponseFile(response,fileName);

				log.debug("exit: " + funcName);
				
				
				return null;
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ProcesoBalanceAdapter.NAME);
			}
	}
	
	public ActionForward downloadLogFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)	throws Exception {

			String funcName = DemodaUtil.currentMethodName();

			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoBalanceAdapter procesoBalanceAdapterVO = (ProcesoBalanceAdapter) userSession.get(ProcesoBalanceAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoBalanceAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoBalanceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoBalanceAdapter.NAME); 
			}
			
			CommonKey idCorrida = new CommonKey(procesoBalanceAdapterVO.getBalance().getCorrida().getId());

			// llamada al servicio
			String fileName = ProServiceLocator.getAdpProcesoService().getLogFile(userSession, idCorrida);
			
			if(fileName == null){			
				log.error("error en: "  + funcName + ": No se pudo formar logFileName para la corrida con id="+idCorrida.getId()+".");
				return forwardErrorSession(request); 
			}
			try{
				baseResponseFile(response,fileName);								
			}catch(Exception e){
				log.error("Error al abrir archivo: ", e);
				return null;
			}

			log.debug("exit: " + funcName);
			
			return null;
		}

	
	// Metodos Relacionados a Folios de Tesoreria
	public ActionForward incluirFolio(ActionMapping mapping, ActionForm form,
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

		// seteo los parametros para cuando oprima seleccionar
		navModel.setSelectAction("/bal/AdministrarProcesoBalance");
		navModel.setSelectActionParameter("paramIncluirFolio");
		navModel.setAgregarEnSeleccion(false);
		
		ProcesoBalanceAdapter  procesoBalanceAdapter = (ProcesoBalanceAdapter) userSession.get(ProcesoBalanceAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (procesoBalanceAdapter == null) {
			log.error("error en: "  + funcName + ": " + ProcesoBalanceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoBalanceAdapter.NAME); 
		}
		
		FolioSearchPage folioFiltro = new FolioSearchPage();
		EstadoFol estadoFol = EstadoFol.getById(EstadoFol.ID_ENVIADO);
		folioFiltro.getFolio().setEstadoFol((EstadoFolVO) estadoFol.toVO(0));
		
		navModel.putParameter(BuscarFolioDAction.FOLIO_SEARCHPAGE_FILTRO, folioFiltro);

		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);
		
		return forwardSeleccionar(mapping, request, "paramIncluirFolio", BalConstants.ACTION_BUSCAR_FOLIO , false);
	}
	
	public ActionForward paramIncluirFolio (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcesoBalanceAdapter procesoBalanceAdapterVO = (ProcesoBalanceAdapter) userSession.get(ProcesoBalanceAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoBalanceAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoBalanceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoBalanceAdapter.NAME); 
			}

			// Seteo el id selecionado
			NavModel navModel = userSession.getNavModel();
			
			// Si el id esta vacio, pq selecciono volver, forwardeo 
			if (StringUtil.isNullOrEmpty(navModel.getSelectedId())) {
				// Envio el VO al request				
				request.setAttribute(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
	
				return mapping.findForward(BalConstants.FWD_PROCESO_BALANCE_ADAPTER);				
			}
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			procesoBalanceAdapterVO.setIdFolio(commonKey.getId());
			
			// llamada al servicio
			procesoBalanceAdapterVO = BalServiceLocator.getBalanceService().incluirFolio(userSession, procesoBalanceAdapterVO);
			
            // Tiene errores recuperables
			if (procesoBalanceAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoBalanceAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoBalanceAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procesoBalanceAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoBalanceAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			
			return mapping.findForward(BalConstants.FWD_PROCESO_BALANCE_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoBalanceAdapter.NAME);
		}
		
	}
	
	public ActionForward verFolio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_FOLIO);
	}

	public ActionForward excluirFolio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_FOLIO, 
				BaseSecurityConstants.EXCLUIR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ProcesoBalanceAdapter procesoBalanceAdapterVO = (ProcesoBalanceAdapter) userSession.get(ProcesoBalanceAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (procesoBalanceAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ProcesoBalanceAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoBalanceAdapter.NAME); 
				}
				// Seteo el id selecionado
				NavModel navModel = userSession.getNavModel();
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				procesoBalanceAdapterVO.setIdFolio(commonKey.getId());
		
				// llamada al servicio
				procesoBalanceAdapterVO = BalServiceLocator.getBalanceService().excluirFolio
						(userSession, procesoBalanceAdapterVO);
				
	            // Tiene errores recuperables
				if (procesoBalanceAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + procesoBalanceAdapterVO.infoString());
					saveDemodaErrors(request, procesoBalanceAdapterVO);				
					request.setAttribute(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
					return mapping.findForward(BalConstants.FWD_PROCESO_BALANCE_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (procesoBalanceAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + procesoBalanceAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
				}
				
				// Fue Exitoso
				// Envio el VO al request
				request.setAttribute(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
		
				return mapping.findForward(BalConstants.FWD_PROCESO_BALANCE_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ProcesoBalanceAdapter.NAME);
			}
		}
	
	// Metodos Relacionados a Archivos de Transacciones
	
	public ActionForward incluirArchivo(ActionMapping mapping, ActionForm form,
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

		// seteo los parametros para cuando oprima seleccionar
		navModel.setSelectAction("/bal/AdministrarProcesoBalance");
		navModel.setSelectActionParameter("paramIncluirArchivo");
		navModel.setAgregarEnSeleccion(false);
		
		ProcesoBalanceAdapter  procesoBalanceAdapter = (ProcesoBalanceAdapter) userSession.get(ProcesoBalanceAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (procesoBalanceAdapter == null) {
			log.error("error en: "  + funcName + ": " + ProcesoBalanceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoBalanceAdapter.NAME); 
		}
		
		ArchivoSearchPage archivoFiltro = new ArchivoSearchPage();
		EstadoArc estadoArc = EstadoArc.getById(EstadoArc.ID_ACEPTADO);
		archivoFiltro.getArchivo().setEstadoArc((EstadoArcVO) estadoArc.toVO(0));
		archivoFiltro.setBalance(procesoBalanceAdapter.getBalance());
		
		navModel.putParameter(BuscarArchivoDAction.ARCHIVO_SEARCHPAGE_FILTRO, archivoFiltro);

		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);
		
		return forwardSeleccionar(mapping, request, "paramIncluirArchivo", BalConstants.ACTION_BUSCAR_ARCHIVO , false);
	}
	
	public ActionForward paramIncluirArchivo (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcesoBalanceAdapter procesoBalanceAdapterVO = (ProcesoBalanceAdapter) userSession.get(ProcesoBalanceAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoBalanceAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoBalanceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoBalanceAdapter.NAME); 
			}

			// Seteo el id selecionado
			NavModel navModel = userSession.getNavModel();
			// Si el id esta vacio, pq selecciono volver, forwardeo 
			if (StringUtil.isNullOrEmpty(navModel.getSelectedId())) {
				// Envio el VO al request				
				request.setAttribute(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
	
				return mapping.findForward(BalConstants.FWD_PROCESO_BALANCE_ADAPTER);				
			}
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			procesoBalanceAdapterVO.setIdArchivo(commonKey.getId());
			
			// llamada al servicio
			procesoBalanceAdapterVO = BalServiceLocator.getBalanceService().getProcesoBalanceAdapterInit(userSession, commonKey,procesoBalanceAdapterVO);
			
            // Tiene errores recuperables
			if (procesoBalanceAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoBalanceAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoBalanceAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procesoBalanceAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoBalanceAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			
			return mapping.findForward(BalConstants.FWD_PROCESO_BALANCE_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoBalanceAdapter.NAME);
		}
		
	}
	
	public ActionForward verArchivo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ARCHIVO);
	}

	public ActionForward excluirArchivo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ARCHIVO, 
				BaseSecurityConstants.EXCLUIR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ProcesoBalanceAdapter procesoBalanceAdapterVO = (ProcesoBalanceAdapter) userSession.get(ProcesoBalanceAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (procesoBalanceAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ProcesoBalanceAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoBalanceAdapter.NAME); 
				}
				// Seteo el id selecionado
				NavModel navModel = userSession.getNavModel();
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				procesoBalanceAdapterVO.setIdArchivo(commonKey.getId());
		
				// llamada al servicio
				procesoBalanceAdapterVO = BalServiceLocator.getBalanceService().excluirArchivo
						(userSession, procesoBalanceAdapterVO);
				
	            // Tiene errores recuperables
				if (procesoBalanceAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + procesoBalanceAdapterVO.infoString());
					saveDemodaErrors(request, procesoBalanceAdapterVO);				
					request.setAttribute(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
					return mapping.findForward(BalConstants.FWD_PROCESO_BALANCE_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (procesoBalanceAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + procesoBalanceAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
				}
				
				// Fue Exitoso
				// Envio el VO al request
				request.setAttribute(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
		
				return mapping.findForward(BalConstants.FWD_PROCESO_BALANCE_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ProcesoBalanceAdapter.NAME);
			}
		}

	// Metodos Relacionados a Caja7
	public ActionForward verCaja7(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CAJA7);
	}

	public ActionForward agregarCaja7(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CAJA7);
	}

	public ActionForward modificarCaja7(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CAJA7);
	}

	public ActionForward eliminarCaja7(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CAJA7);
	}
	
	public ActionForward incluirAuxCaja7(ActionMapping mapping, ActionForm form,
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

		// seteo los parametros para cuando oprima seleccionar
		navModel.setSelectAction("/bal/AdministrarProcesoBalance");
		navModel.setSelectActionParameter("paramIncluirAuxCaja7");
		navModel.setAgregarEnSeleccion(false);
		
		ProcesoBalanceAdapter  procesoBalanceAdapter = (ProcesoBalanceAdapter) userSession.get(ProcesoBalanceAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (procesoBalanceAdapter == null) {
			log.error("error en: "  + funcName + ": " + ProcesoBalanceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoBalanceAdapter.NAME); 
		}
		
		AuxCaja7SearchPage auxCaja7Filtro = new AuxCaja7SearchPage();
		auxCaja7Filtro.getAuxCaja7().setEstado(Estado.ACTIVO);
		auxCaja7Filtro.setBalance(procesoBalanceAdapter.getBalance());
		
		navModel.putParameter(BuscarAuxCaja7DAction.AUXCAJA7_SEARCHPAGE_FILTRO, auxCaja7Filtro);

		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);
		
		return forwardSeleccionar(mapping, request, "paramIncluirAuxCaja7", BalConstants.ACTION_BUSCAR_AUXCAJA7 , false);
	}
	
	public ActionForward paramIncluirAuxCaja7 (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcesoBalanceAdapter procesoBalanceAdapterVO = (ProcesoBalanceAdapter) userSession.get(ProcesoBalanceAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoBalanceAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoBalanceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoBalanceAdapter.NAME); 
			}

			// Seteo el id selecionado
			NavModel navModel = userSession.getNavModel();
			// Si el id esta vacio, pq selecciono volver, forwardeo 
			if (StringUtil.isNullOrEmpty(navModel.getSelectedId())) {
				// Envio el VO al request				
				request.setAttribute(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
	
				return mapping.findForward(BalConstants.FWD_PROCESO_BALANCE_ADAPTER);				
			}
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			// llamada al servicio
			procesoBalanceAdapterVO = BalServiceLocator.getBalanceService().getProcesoBalanceAdapterInit(userSession, commonKey,procesoBalanceAdapterVO);
			
            // Tiene errores recuperables
			if (procesoBalanceAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoBalanceAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoBalanceAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procesoBalanceAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoBalanceAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			
			return mapping.findForward(BalConstants.FWD_PROCESO_BALANCE_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoBalanceAdapter.NAME);
		}
		
	}
	
	
	// Metodos Relacionados a Reingresos
	public ActionForward incluirReingreso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_REINGRESO, BalConstants.ACT_INCLUIR);
	}

	public ActionForward excluirReingreso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_REINGRESO, BalConstants.ACT_EXCLUIR);
	}

	public ActionForward verReingreso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_REINGRESO, BaseConstants.ACT_VER);
	}

	// Metodos Relacionados a Caja69
	public ActionForward verCaja69(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CAJA69);
	}

	public ActionForward agregarCaja69(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CAJA69);
	}

	public ActionForward modificarCaja69(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CAJA69);
	}

	public ActionForward eliminarCaja69(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CAJA69);
	}
	
	// Metodos Relacionados a TranBal
	public ActionForward verTranBal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TRANBAL);
	}

	public ActionForward agregarTranBal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TRANBAL);
	}

	public ActionForward modificarTranBal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TRANBAL);
	}

	public ActionForward eliminarTranBal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TRANBAL);
	}
	
	public ActionForward buscarTranBal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, BalConstants.ACTION_BUSCAR_TRANBAL, BaseConstants.ACT_BUSCAR);
	}
	
	// Metodos Relacionados a Asentamiento
	public ActionForward admProcesoAsentamiento(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_PROCESO_ASENTAMIENTO, BalConstants.ACT_ADM_PROCESO_ASENTAMIENTO);
	}

	public ActionForward verAsentamiento(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ASENTAMIENTO, BaseConstants.ACT_VER);
	}

	// Metodos Relacionados a Asentamiento Delegado
	public ActionForward admProcesoAseDel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_PROCESO_ASEDEL, BalConstants.ACT_ADM_PROCESO_ASEDEL);
	}

	public ActionForward verAseDel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ASEDEL, BaseConstants.ACT_VER);
	}

	
	// Metodos Relacionados a Compensaciones
	public ActionForward incluirCompensacion(ActionMapping mapping, ActionForm form,
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

		// seteo los parametros para cuando oprima seleccionar
		navModel.setSelectAction("/bal/AdministrarProcesoBalance");
		navModel.setSelectActionParameter("paramIncluirCompensacion");
		navModel.setAgregarEnSeleccion(false);
		
		ProcesoBalanceAdapter  procesoBalanceAdapter = (ProcesoBalanceAdapter) userSession.get(ProcesoBalanceAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (procesoBalanceAdapter == null) {
			log.error("error en: "  + funcName + ": " + ProcesoBalanceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoBalanceAdapter.NAME); 
		}
		
		CompensacionSearchPage compensacionFiltro = new CompensacionSearchPage();
		EstadoCom estadoCom = EstadoCom.getById(EstadoCom.ID_LISTA_PARA_FOLIO);
		compensacionFiltro.getCompensacion().setEstadoCom((EstadoComVO) estadoCom.toVO(0));
		compensacionFiltro.setBalance(procesoBalanceAdapter.getBalance());
		
		navModel.putParameter(BuscarCompensacionDAction.COMPENSACION_SEARCHPAGE_FILTRO, compensacionFiltro);

		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);
		
		return forwardSeleccionar(mapping, request, "paramIncluirCompensacion", BalConstants.ACTION_BUSCAR_COMPENSACION , false);
	}
	
	public ActionForward paramIncluirCompensacion (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcesoBalanceAdapter procesoBalanceAdapterVO = (ProcesoBalanceAdapter) userSession.get(ProcesoBalanceAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoBalanceAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoBalanceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoBalanceAdapter.NAME); 
			}

			// Seteo el id selecionado
			NavModel navModel = userSession.getNavModel();
			// Si el id esta vacio, pq selecciono volver, forwardeo 
			if (StringUtil.isNullOrEmpty(navModel.getSelectedId())) {
				// Envio el VO al request				
				request.setAttribute(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
	
				return mapping.findForward(BalConstants.FWD_PROCESO_BALANCE_ADAPTER);				
			}
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			procesoBalanceAdapterVO.setIdCompensacion(commonKey.getId()); 
			
			// llamada al servicio
			procesoBalanceAdapterVO = BalServiceLocator.getBalanceService().getProcesoBalanceAdapterInit(userSession, commonKey,procesoBalanceAdapterVO);
			
            // Tiene errores recuperables
			if (procesoBalanceAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoBalanceAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoBalanceAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procesoBalanceAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoBalanceAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
			
			return mapping.findForward(BalConstants.FWD_PROCESO_BALANCE_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoBalanceAdapter.NAME);
		}
		
	}
	
	public ActionForward verCompensacion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_COMPENSACION);
	}

	public ActionForward excluirCompensacion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_COMPENSACION, 
				BaseSecurityConstants.EXCLUIR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ProcesoBalanceAdapter procesoBalanceAdapterVO = (ProcesoBalanceAdapter) userSession.get(ProcesoBalanceAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (procesoBalanceAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ProcesoBalanceAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoBalanceAdapter.NAME); 
				}
				// Seteo el id selecionado
				NavModel navModel = userSession.getNavModel();
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				 procesoBalanceAdapterVO.setIdCompensacion(commonKey.getId()); 
		
				// llamada al servicio
				procesoBalanceAdapterVO = BalServiceLocator.getBalanceService().excluirCompensacion(userSession, procesoBalanceAdapterVO);
				
	            // Tiene errores recuperables
				if (procesoBalanceAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + procesoBalanceAdapterVO.infoString());
					saveDemodaErrors(request, procesoBalanceAdapterVO);				
					request.setAttribute(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
					return mapping.findForward(BalConstants.FWD_PROCESO_BALANCE_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (procesoBalanceAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + procesoBalanceAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
				}
				
				// Fue Exitoso
				// Envio el VO al request
				request.setAttribute(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ProcesoBalanceAdapter.NAME, procesoBalanceAdapterVO);
		
				return mapping.findForward(BalConstants.FWD_PROCESO_BALANCE_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ProcesoBalanceAdapter.NAME);
			}
		}

}
