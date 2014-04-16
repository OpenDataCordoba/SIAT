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

import ar.gov.rosario.siat.bal.buss.bean.EstOtrIngTes;
import ar.gov.rosario.siat.bal.iface.model.EstOtrIngTesVO;
import ar.gov.rosario.siat.bal.iface.model.FolioAdapter;
import ar.gov.rosario.siat.bal.iface.model.FolioVO;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarFolioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarFolioDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_FOLIO, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			FolioAdapter folioAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getFolioAdapterForView(userSession, commonKey)";
					folioAdapterVO = BalServiceLocator.getFolioTesoreriaService().getFolioAdapterForView(userSession, commonKey, null);
					actionForward = mapping.findForward(BalConstants.FWD_FOLIO_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getFolioAdapterForUpdate(userSession, commonKey)";
					folioAdapterVO = BalServiceLocator.getFolioTesoreriaService().getFolioAdapterForView(userSession, commonKey, null);
					actionForward = mapping.findForward(BalConstants.FWD_FOLIO_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getFolioAdapterForDelete(userSession, commonKey)";
					folioAdapterVO = BalServiceLocator.getFolioTesoreriaService().getFolioAdapterForView(userSession, commonKey, null);
					folioAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.FOLIO_LABEL);
					actionForward = mapping.findForward(BalConstants.FWD_FOLIO_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ENVIAR)) {
					stringServicio = "getFolioAdapterForDelete(userSession, commonKey)";
					folioAdapterVO = BalServiceLocator.getFolioTesoreriaService().getFolioAdapterForView(userSession, commonKey, null);
					folioAdapterVO.addMessage(BalError.MSG_ENVIAR, BalError.FOLIO_LABEL);
					actionForward = mapping.findForward(BalConstants.FWD_FOLIO_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_DEVOLVER)) {
					stringServicio = "getFolioAdapterForDelete(userSession, commonKey)";
					folioAdapterVO = BalServiceLocator.getFolioTesoreriaService().getFolioAdapterForView(userSession, commonKey, null);
					folioAdapterVO.addMessage(BalError.MSG_DEVOLVER, BalError.FOLIO_LABEL);
					actionForward = mapping.findForward(BalConstants.FWD_FOLIO_VIEW_ADAPTER);					
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (folioAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + folioAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, FolioAdapter.NAME, folioAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				folioAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					FolioAdapter.NAME + ": " + folioAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(FolioAdapter.NAME, folioAdapterVO);
				// Subo el apdater al userSession
				userSession.put(FolioAdapter.NAME, folioAdapterVO);
				
				saveDemodaMessages(request, folioAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FolioAdapter.NAME);
			}
		}
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				BalConstants.ACTION_ADMINISTRAR_ENCFOLIO, BaseConstants.ACT_MODIFICAR);

	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_FOLIO, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				FolioAdapter folioAdapterVO = (FolioAdapter) userSession.get(FolioAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (folioAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + FolioAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, FolioAdapter.NAME); 
				}

				// llamada al servicio
				FolioVO folioVO = BalServiceLocator.getFolioTesoreriaService().deleteFolio
					(userSession, folioAdapterVO.getFolio());
				
	            // Tiene errores recuperables
				if (folioVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folioAdapterVO.infoString());
					saveDemodaErrors(request, folioVO);				
					request.setAttribute(FolioAdapter.NAME, folioAdapterVO);
					return mapping.findForward(BalConstants.FWD_FOLIO_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (folioVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + folioAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, FolioAdapter.NAME, folioAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, FolioAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FolioAdapter.NAME);
			}
		}
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, FolioAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, FolioAdapter.NAME);
			
	}
	
	// Metodos relacionados FolCom
	public ActionForward verFolCom(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_FOLCOM);

	}

	public ActionForward modificarFolCom(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_FOLCOM);

	}

	public ActionForward eliminarFolCom(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_FOLCOM);

	}
	
	public ActionForward agregarFolCom(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_FOLCOM);
		
	}
	
	// Metodos Relacionados a  Otros Ingresos de Tesoreria
	public ActionForward incluirOtrIngTes(ActionMapping mapping, ActionForm form,
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
		navModel.setSelectAction("/bal/AdministrarFolio");
		navModel.setSelectActionParameter("paramIncluirOtrIngTes");
		navModel.setAgregarEnSeleccion(false);
		
		FolioAdapter  folioAdapter = (FolioAdapter) userSession.get(FolioAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (folioAdapter == null) {
			log.error("error en: "  + funcName + ": " + FolioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, FolioAdapter.NAME); 
		}

		OtrIngTesSearchPage otrIngTesFiltro = new OtrIngTesSearchPage();
		EstOtrIngTes estOtrIngTes = EstOtrIngTes.getById(EstOtrIngTes.ID_REGISTRADO);
		otrIngTesFiltro.getOtrIngTes().setEstOtrIngTes((EstOtrIngTesVO) estOtrIngTes.toVO(0));
		otrIngTesFiltro.setFolio(folioAdapter.getFolio());
		
		navModel.putParameter(BuscarOtrIngTesDAction.OTRINGTES_SEARCHPAGE_FILTRO, otrIngTesFiltro);

		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);
		
		//return mapping.findForward(BalConstants.ACTION_BUSCAR_OTRINGTES);
		return forwardSeleccionar(mapping, request, "paramIncluirOtrIngTes", BalConstants.ACTION_BUSCAR_OTRINGTES , false);
	}
	
	public ActionForward paramIncluirOtrIngTes (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FolioAdapter folioAdapterVO = (FolioAdapter) userSession.get(FolioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (folioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FolioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FolioAdapter.NAME); 
			}

			// Seteo el id selecionado
			NavModel navModel = userSession.getNavModel();
			// Si el id esta vacio, pq selecciono volver, forwardeo 
			if (StringUtil.isNullOrEmpty(navModel.getSelectedId())) {
				// Envio el VO al request				
				request.setAttribute(FolioAdapter.NAME, folioAdapterVO);
	
				return mapping.findForward(BalConstants.FWD_FOLIO_ADAPTER);				
			}
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			folioAdapterVO.setIdOtrIngTes(commonKey.getId());
			
			// llamada al servicio
			folioAdapterVO = BalServiceLocator.getFolioTesoreriaService().getFolioAdapterForView(userSession, commonKey,folioAdapterVO);
			
            // Tiene errores recuperables
			if (folioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + folioAdapterVO.infoString()); 
				saveDemodaErrors(request, folioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, FolioAdapter.NAME, folioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (folioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + folioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FolioAdapter.NAME, folioAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(FolioAdapter.NAME, folioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(FolioAdapter.NAME, folioAdapterVO);
			
			return mapping.findForward(BalConstants.FWD_FOLIO_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FolioAdapter.NAME);
		}
		
	}
	
	public ActionForward verOtrIngTes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_OTRINGTES);
	}

	public ActionForward excluirOtrIngTes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_OTRINGTES, 
				BaseSecurityConstants.EXCLUIR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				FolioAdapter folioAdapterVO = (FolioAdapter) userSession.get(FolioAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (folioAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + FolioAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, FolioAdapter.NAME); 
				}
				// Seteo el id selecionado
				NavModel navModel = userSession.getNavModel();
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				folioAdapterVO.setIdOtrIngTes(commonKey.getId());
		
				// llamada al servicio
				folioAdapterVO = BalServiceLocator.getFolioTesoreriaService().excluirOtrIngTes
						(userSession, folioAdapterVO);
				
	            // Tiene errores recuperables
				if (folioAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folioAdapterVO.infoString());
					saveDemodaErrors(request, folioAdapterVO);				
					request.setAttribute(FolioAdapter.NAME, folioAdapterVO);
					return mapping.findForward(BalConstants.FWD_FOLIO_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (folioAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + folioAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, FolioAdapter.NAME, folioAdapterVO);
				}
				
				// Fue Exitoso
				//return forwardConfirmarOk(mapping, request, funcName, FolioAdapter.NAME);
				// Envio el VO al request
				request.setAttribute(FolioAdapter.NAME, folioAdapterVO);
				// Subo el apdater al userSession
				userSession.put(FolioAdapter.NAME, folioAdapterVO);
		
				return mapping.findForward(BalConstants.FWD_FOLIO_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FolioAdapter.NAME);
			}
		}
	
	// Metodos relacionados FolDiaCob
	public ActionForward verFolDiaCob(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_FOLDIACOB);

	}

	public ActionForward modificarFolDiaCob(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_FOLDIACOB);

	}

	public ActionForward eliminarFolDiaCob(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_FOLDIACOB);

	}
	
	public ActionForward agregarFolDiaCob(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_FOLDIACOB);
		
	}
	
	public ActionForward enviar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_FOLIO, BaseSecurityConstants.ENVIAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FolioAdapter folioAdapterVO = (FolioAdapter) userSession.get(FolioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (folioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FolioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FolioAdapter.NAME); 
			}

			// llamada al servicio
			FolioVO folioVO = BalServiceLocator.getFolioTesoreriaService().enviarFolio(userSession, folioAdapterVO.getFolio());
			
            // Tiene errores recuperables
			if (folioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + folioAdapterVO.infoString());
				saveDemodaErrors(request, folioVO);				
				request.setAttribute(FolioAdapter.NAME, folioAdapterVO);
				return mapping.findForward(BalConstants.FWD_FOLIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (folioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + folioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FolioAdapter.NAME, folioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FolioAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FolioAdapter.NAME);
		}
	}
	
	public ActionForward devolver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_FOLIO, BaseSecurityConstants.DEVOLVER); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FolioAdapter folioAdapterVO = (FolioAdapter) userSession.get(FolioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (folioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FolioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FolioAdapter.NAME); 
			}

			// llamada al servicio
			FolioVO folioVO = BalServiceLocator.getFolioTesoreriaService().devolverFolio(userSession, folioAdapterVO.getFolio());
			
            // Tiene errores recuperables
			if (folioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + folioAdapterVO.infoString());
				saveDemodaErrors(request, folioVO);				
				request.setAttribute(FolioAdapter.NAME, folioAdapterVO);
				return mapping.findForward(BalConstants.FWD_FOLIO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (folioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + folioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FolioAdapter.NAME, folioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FolioAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FolioAdapter.NAME);
		}
	}

	public ActionForward imprimirReportFromAdapter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			String name = FolioAdapter.NAME;
			String reportFormat = request.getParameter("report.reportFormat");
			
			// **Bajo el searchPage del userSession
			String responseFile = request.getParameter("responseFile");
			if ("1".equals(responseFile)) {
				String fileName = (String) userSession.get("baseImprimir.reportFilename");
				// realiza la visualizacion del reporte
				baseResponseEmbedContent(response, fileName, "application/pdf");
				return null;
			}
			
			// Bajo el adapter del userSession
			FolioAdapter folioAdapterVO = (FolioAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (folioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}

			// prepara el report del adapter para luego generar el reporte
			//folioAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			folioAdapterVO = BalServiceLocator.getFolioTesoreriaService().imprimirFolio(userSession, folioAdapterVO);

			// limpia la lista de reports y la lista de tablas
			folioAdapterVO.getReport().getListReport().clear();
			folioAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (folioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + folioAdapterVO.infoString());
				saveDemodaErrors(request, folioAdapterVO);				
				request.setAttribute(name, folioAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (folioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + folioAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, folioAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = folioAdapterVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}
}
