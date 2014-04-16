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

import ar.gov.rosario.siat.bal.buss.bean.EstadoCom;
import ar.gov.rosario.siat.bal.iface.model.CompensacionSearchPage;
import ar.gov.rosario.siat.bal.iface.model.FolComAdapter;
import ar.gov.rosario.siat.bal.iface.model.FolComVO;
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

public class AdministrarFolComDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarFolComDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_FOLCOM, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			FolComAdapter folComAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getFolComAdapterForView(userSession, commonKey)";
					folComAdapterVO = BalServiceLocator.getFolioTesoreriaService().getFolComAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_FOLCOM_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getFolComAdapterForUpdate(userSession, commonKey)";
					folComAdapterVO = BalServiceLocator.getFolioTesoreriaService().getFolComAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_FOLCOM_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getFolComAdapterForDelete(userSession, commonKey)";
					folComAdapterVO = BalServiceLocator.getFolioTesoreriaService().getFolComAdapterForView
						(userSession, commonKey);
					folComAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.FOLCOM_LABEL);				
					actionForward = mapping.findForward(BalConstants.FWD_FOLCOM_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getFolComAdapterForCreate(userSession)";
					folComAdapterVO = BalServiceLocator.getFolioTesoreriaService().getFolComAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_FOLCOM_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (folComAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + folComAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, FolComAdapter.NAME, folComAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				folComAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + FolComAdapter.NAME + ": "+ folComAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(FolComAdapter.NAME, folComAdapterVO);
				// Subo el apdater al userSession
				userSession.put(FolComAdapter.NAME, folComAdapterVO);
				
				saveDemodaMessages(request, folComAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FolComAdapter.NAME);
			}
		}
	

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				BalSecurityConstants.ABM_FOLCOM, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				FolComAdapter folComAdapterVO = (FolComAdapter) userSession.get(FolComAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (folComAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + FolComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, FolComAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(folComAdapterVO, request);
				
	            // Tiene errores recuperables
				if (folComAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folComAdapterVO.infoString()); 
					saveDemodaErrors(request, folComAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, FolComAdapter.NAME, folComAdapterVO);
				}
				
				// llamada al servicio
				FolComVO folComVO = BalServiceLocator.getFolioTesoreriaService().createFolCom(userSession, folComAdapterVO.getFolCom());
				
	            // Tiene errores recuperables
				if (folComVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folComVO.infoString()); 
					saveDemodaErrors(request, folComVO);
					return forwardErrorRecoverable(mapping, request, userSession, FolComAdapter.NAME, folComAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (folComVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + folComVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, FolComAdapter.NAME, folComAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, FolComAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FolComAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_FOLCOM, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				FolComAdapter folComAdapterVO = (FolComAdapter) userSession.get(FolComAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (folComAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + FolComAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, FolComAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(folComAdapterVO, request);
				
	            // Tiene errores recuperables
				if (folComAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folComAdapterVO.infoString()); 
					saveDemodaErrors(request, folComAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						FolComAdapter.NAME, folComAdapterVO);
				}
				
				// llamada al servicio
				FolComVO folComVO = BalServiceLocator.getFolioTesoreriaService().updateFolCom(userSession, folComAdapterVO.getFolCom());
				
	            // Tiene errores recuperables
				if (folComVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folComAdapterVO.infoString()); 
					saveDemodaErrors(request, folComVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						FolComAdapter.NAME, folComAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (folComVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + folComAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						FolComAdapter.NAME, folComAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, FolComAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FolComAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_FOLCOM, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				FolComAdapter folComAdapterVO = (FolComAdapter) userSession.get(FolComAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (folComAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + FolComAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, FolComAdapter.NAME); 
				}

				// llamada al servicio
				FolComVO folComVO = BalServiceLocator.getFolioTesoreriaService().deleteFolCom(userSession, folComAdapterVO.getFolCom());
				
	            // Tiene errores recuperables
				if (folComVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folComAdapterVO.infoString());
					saveDemodaErrors(request, folComVO);				
					request.setAttribute(FolComAdapter.NAME, folComAdapterVO);
					return mapping.findForward(BalConstants.FWD_FOLCOM_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (folComVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + folComAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						FolComAdapter.NAME, folComAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, FolComAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FolComAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, FolComAdapter.NAME);
		}

		
		public ActionForward buscarCompensacion(ActionMapping mapping, ActionForm form,
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
			navModel.setSelectAction("/bal/AdministrarFolCom");
			navModel.setSelectActionParameter("paramCompensacion");
			navModel.setAgregarEnSeleccion(false);
			
			FolComAdapter  folComAdapter = (FolComAdapter) userSession.get(FolComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (folComAdapter == null) {
				log.error("error en: "  + funcName + ": " + FolComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FolComAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(folComAdapter, request);
			
	        // Tiene errores recuperables
			if (folComAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + folComAdapter.infoString()); 
				saveDemodaErrors(request, folComAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, FolComAdapter.NAME, folComAdapter);
			}
			
			CompensacionSearchPage compensacionFiltro = new CompensacionSearchPage();
			compensacionFiltro.getCompensacion().getEstadoCom().setId(EstadoCom.ID_LISTA_PARA_FOLIO);
			
			navModel.putParameter(BuscarCompensacionDAction.COMPENSACION_SEARCHPAGE_FILTRO, compensacionFiltro);
			
			// seteo el act a ejecutar en el accion al cual me dirijo		
			navModel.setAct(BaseConstants.ACT_SELECCIONAR);
			
			return forwardSeleccionar(mapping, request, "paramCompensacion", BalConstants.ACTION_BUSCAR_COMPENSACION , false);
			
		}
		
		public ActionForward paramCompensacion(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
				
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				FolComAdapter folComAdapterVO = (FolComAdapter) userSession.get(FolComAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (folComAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + FolComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, FolComAdapter.NAME); 
				}

				// Seteo el id selecionado
				NavModel navModel = userSession.getNavModel();
				
				// Si el id esta vacio, pq selecciono volver, forwardeo al SearchPage
				if (StringUtil.isNullOrEmpty(navModel.getSelectedId())) {
					// Envio el VO al request				
					request.setAttribute(FolComAdapter.NAME, folComAdapterVO);
					
					return mapping.findForward(BalConstants.FWD_FOLCOM_EDIT_ADAPTER);				
				}
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				folComAdapterVO.getFolCom().getCompensacion().setId(commonKey.getId());
				
				// llamada al servicio
				folComAdapterVO = BalServiceLocator.getFolioTesoreriaService().getFolComAdapterParamCompensacion(userSession, folComAdapterVO);
				
	            // Tiene errores recuperables
				if (folComAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folComAdapterVO.infoString()); 
					saveDemodaErrors(request, folComAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, FolComAdapter.NAME, folComAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (folComAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + folComAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, FolComAdapter.NAME, folComAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(FolComAdapter.NAME, folComAdapterVO);
				// Subo el apdater al userSession
				userSession.put(FolComAdapter.NAME, folComAdapterVO);
				
				return mapping.findForward(BalConstants.FWD_FOLCOM_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FolComAdapter.NAME);
			}
		}

}
