//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

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
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.DeudaProMasPlanillasDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoAdmProcesoAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoVO;
import ar.gov.rosario.siat.gde.iface.model.SelAlmAgregarParametrosSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarProcesoProcesoMasivoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProcesoProcesoMasivoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
				GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO)) {
				stringServicio = "getProcesoMasivoAdmProcesoAdapterInit(userSession, commonKey)";
				procesoMasivoAdmProcesoAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getProcesoMasivoAdmProcesoAdapterInit(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PROCESO_PROCESO_MASIVO_ADAPTER);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (procesoMasivoAdmProcesoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + procesoMasivoAdmProcesoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoMasivoAdmProcesoAdapter.NAME, procesoMasivoAdmProcesoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			procesoMasivoAdmProcesoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcesoMasivoAdmProcesoAdapter.NAME + ": "+ procesoMasivoAdmProcesoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ProcesoMasivoAdmProcesoAdapter.NAME, procesoMasivoAdmProcesoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcesoMasivoAdmProcesoAdapter.NAME, procesoMasivoAdmProcesoAdapterVO);
			 
			saveDemodaMessages(request, procesoMasivoAdmProcesoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoMasivoAdmProcesoAdapter.NAME);
		}
	}
	
	public ActionForward modificarProcesoMasivo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter (mapping, request, funcName, 
				GdeConstants.FWD_ADMINISTRAR_PROCESO_MASIVO , BaseConstants.ACT_MODIFICAR);
	}

	public ActionForward seleccionarDeudaEnviarIncluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			// ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapterVO = (ProcesoMasivoAdmProcesoAdapter) userSession.get(ProcesoAsentamientoAdapter.NAME);
			ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapterVO = (ProcesoMasivoAdmProcesoAdapter) userSession.get(ProcesoMasivoAdmProcesoAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoMasivoAdmProcesoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoMasivoAdmProcesoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoAdmProcesoAdapter.NAME); 
			}
			// Cargo el id de la selAlmDeuda
			navModel.putParameter(SelAlmAgregarParametrosSearchPage.ID_SEL_ALM_INC, procesoMasivoAdmProcesoAdapterVO.getProcesoMasivo().getSelAlmInc().getId());

			// Cargo el id de la corrida
			navModel.putParameter(SelAlmAgregarParametrosSearchPage.ID_CORRIDA, procesoMasivoAdmProcesoAdapterVO.getProcesoMasivo().getCorrida().getId());

			// Cargo el id del recurso
			navModel.putParameter(SelAlmAgregarParametrosSearchPage.ID_RECURSO, procesoMasivoAdmProcesoAdapterVO.getProcesoMasivo().getRecurso().getId());
			
			//Cargo el id del tipo de Proceso Masivo
			navModel.putParameter(SelAlmAgregarParametrosSearchPage.ID_TIPO_PROC_MAS, procesoMasivoAdmProcesoAdapterVO.getProcesoMasivo().getTipProMas().getId());

			//Cargo el id de la  via de la deuda del Proceso Masivo
			navModel.putParameter(SelAlmAgregarParametrosSearchPage.ID_VIA_DEUDA, procesoMasivoAdmProcesoAdapterVO.getProcesoMasivo().getViaDeuda().getId());

			// vuelve al refill. Hace falta el act
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_SEL_ALM_AGREGAR_PARAMETROS_SEARCHPAGE,
					GdeSecurityConstants.MTD_AGREGAR_PARAMETROS);
		}

	public ActionForward eliminarDeudaEnviarIncluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			// vuelve al refill
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_BUSCAR_DEUDA_INC_PRO_MAS_ELIMINAR, "");
		}


	public ActionForward logsArmadoDeudaEnviarIncluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();

			// vuelve al refill.
			// act = logsArmadoDeudaEnviar
			// podriamos usar el MTD como act
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_ARMADO_SELECCION_DEUDA_INC_PRO_MAS, 
					GdeConstants.ACT_LOGS_ARMADO_DEUDA_PRO_MAS);
		}
	 
	public ActionForward planillasDeudaEnviarIncluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			// vuelve al refill, act de deudas
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_PLANILLAS_DEUDA_INC_PRO_MAS, GdeConstants.ACT_PLANILLAS_DEUDA_INC_PRO_MAS);
	}

	public ActionForward planillasCuentaEnviarIncluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			// vuelve al refill, act de deudas
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_PLANILLAS_DEUDA_INC_PRO_MAS, GdeConstants.ACT_PLANILLAS_CUENTA_INC_PRO_MAS);
	}

	public ActionForward planillasConvenioCuotaEnviarIncluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			// vuelve al refill, act de cuotas de convenio
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_PLANILLAS_DEUDA_INC_PRO_MAS, GdeConstants.ACT_PLANILLAS_CONVENIO_CUOTA_INC_PRO_MAS);
	}


	public ActionForward limpiarSeleccionDeudaEnviarIncluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			// NO Bajo el adapter del userSession

			// vuelve al refill.
			// act = limpiarSeleccionDeudaEnviar
			// podriamos usar el MTD como act
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_ARMADO_SELECCION_DEUDA_INC_PRO_MAS, 
					GdeConstants.ACT_LIMPIAR_SELECCION_DEUDA_PRO_MAS);
		}

	public ActionForward consultarDeudaEnviarIncluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);
			
			// seteo del procesoMasivo sin bajar el adapter del userSession
			NavModel navModel = userSession.getNavModel();
			navModel.putParameter(
					BuscarDeudaIncProMasConsPorCtaDAction.PROCESO_MASIVO_KEY, 
					new CommonKey(navModel.getSelectedId()));

			// vuelve al refill
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_CONS_POR_CTA_DEUDA_INC_PRO_MAS, "");
		}

	// DEUDA A EXCLUIR
	
	public ActionForward seleccionarDeudaEnviarExcluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();

			// NO Bajo el adapter del userSession

			// vuelve al refill. No hace falta el act porque en el inicializar no pregunta por el act
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_BUSCAR_DEUDA_EXC_PRO_MAS_AGREGAR, "");
		}

	public ActionForward eliminarDeudaEnviarExcluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			// NO Bajo el adapter del userSession

			// vuelve al refill, no hace falta el act
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_BUSCAR_DEUDA_EXC_PRO_MAS_ELIMINAR, "");
		}

	public ActionForward logsArmadoDeudaEnviarExcluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();

			// No hace falta bajar el adapter del userSession
			
			// vuelve al refill.
			// act = logsArmadoDeudaEnviar usado en el AdministrarDeudaExcProMasArmadoSeleccionDAction
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_ARMADO_SELECCION_DEUDA_EXC_PRO_MAS, 
					GdeConstants.ACT_LOGS_ARMADO_DEUDA_PRO_MAS);
		}
	
	public ActionForward limpiarSeleccionDeudaEnviarExcluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			// NO Bajo el adapter del userSession
			
			// vuelve al refill.
			// act = limpiarSeleccionDeudaEnviar usado en el Administrar
			// podriamos usar el MTD como act
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_ARMADO_SELECCION_DEUDA_EXC_PRO_MAS, 
					GdeConstants.ACT_LIMPIAR_SELECCION_DEUDA_PRO_MAS);
		}

	public ActionForward planillasDeudaEnviarExcluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			// vuelve al refill, no hace falta el act
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_PLANILLAS_DEUDA_EXC_PRO_MAS, 
					GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO);
		}

	public ActionForward planillasCuentaEnviarExcluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			// vuelve al refill, no hace falta el act
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_PLANILLAS_DEUDA_EXC_PRO_MAS, 
					GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO);
		}

	public ActionForward consultarDeudaEnviarExcluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);
			
			// seteo del procesoMasivo sin bajar el adapter del userSession
			NavModel navModel = userSession.getNavModel();
			navModel.putParameter(
					BuscarDeudaExcProMasConsPorCtaDAction.PROCESO_MASIVO_KEY, 
					new CommonKey(navModel.getSelectedId()));

			// vuelve al refill
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_CONS_POR_CTA_DEUDA_EXC_PRO_MAS, "");
		}

	// Procuradores a Excluir del Envio
	
	public ActionForward agregarProMasProExc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			// vuelve al refill
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_ADMINISTRAR_PRO_MAS_PRO_EXC, BaseConstants.ACT_AGREGAR);
	}
	
	public ActionForward eliminarProMasProExc(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			// vuelve al refill
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_ADMINISTRAR_PRO_MAS_PRO_EXC, BaseConstants.ACT_ELIMINAR);
		}

	// Reportes disponibles para el perfeccionamiento de la deuda incluida y de la deuda excluida
	
	public ActionForward verReportesDeudaIncluida(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			// vuelve al refill, no hace falta el act
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_PRO_MAS_REPORTES_DEUDA_INC, GdeConstants.ACT_REPORTES_DEUDA_PRO_MAS);
		}

	public ActionForward verReportesCuentaIncluida(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			// vuelve al refill, no hace falta el act
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_PRO_MAS_REPORTES_DEUDA_INC, GdeConstants.ACT_REPORTES_CUENTA_PRO_MAS);
		}

	public ActionForward verReportesDeudaExcluida(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			// vuelve al refill, no hace falta el act
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_PRO_MAS_REPORTES_DEUDA_EXC, GdeConstants.ACT_REPORTES_DEUDA_PRO_MAS);
	}
	
	public ActionForward verReportesConvenioCuotaIncluida(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			// vuelve al refill, 
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_PRO_MAS_REPORTES_DEUDA_INC, GdeConstants.ACT_REPORTES_CONVENIO_CUOTA_PRO_MAS);
		}

	public ActionForward verReportesConvenioCuotaExcluida(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			// vuelve al refill, no hace falta el act
			return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
					GdeConstants.FWD_PRO_MAS_REPORTES_DEUDA_EXC, GdeConstants.ACT_REPORTES_CONVENIO_CUOTA_PRO_MAS);
		}

	//verReporteEnvioRealizado
	public ActionForward verReporteEnvioRealizado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

 		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = request.getParameter("fileParam");
			baseResponseFile(response,fileName);
            
			log.debug("finalizando: " + funcName);
			return null;			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaProMasPlanillasDeudaAdapter.NAME);
		}
	}

	// administrarAdpCorridaProcesoMasivo
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			// ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapterVO = (ProcesoMasivoAdmProcesoAdapter) userSession.get(ProcesoAsentamientoAdapter.NAME);
			ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapterVO = (ProcesoMasivoAdmProcesoAdapter) userSession.get(ProcesoMasivoAdmProcesoAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoMasivoAdmProcesoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoMasivoAdmProcesoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoAdmProcesoAdapter.NAME); 
			}

			//DemodaUtil.populateVO(procesoMasivoAdmProcesoAdapterVO, request);
			//Nueva validacion para la reconfeccion masiva para que se cargue la fecha de vencimiento
			/* 
			procesoMasivoAdmProcesoAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getProcMasivoActProcRecofAdapter (userSession,procesoMasivoAdmProcesoAdapterVO);
			
			if (procesoMasivoAdmProcesoAdapterVO.hasErrorRecoverable()){
				request.setAttribute(ProcesoMasivoAdmProcesoAdapter.NAME, procesoMasivoAdmProcesoAdapterVO);
				saveDemodaErrors(request, procesoMasivoAdmProcesoAdapterVO);
				userSession.put(ProcesoMasivoAdmProcesoAdapter.NAME, procesoMasivoAdmProcesoAdapterVO);
				
				return mapping.findForward(GdeConstants.FWD_PROCESO_PROCESO_MASIVO_ADAPTER);
			}
			*/

			// Si no tuvo errores subo la fecha de Vencimiento
			// Cargo el id de la corrida como parametro			
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoMasivoAdmProcesoAdapterVO.getProcesoMasivo().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_PRO_MAS, ProConstants.ACT_ACTIVAR);
	}

	public ActionForward cancelar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)	throws Exception {

			String funcName = DemodaUtil.currentMethodName();

			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapterVO = (ProcesoMasivoAdmProcesoAdapter) userSession.get(ProcesoMasivoAdmProcesoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoMasivoAdmProcesoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoMasivoAdmProcesoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoAdmProcesoAdapter.NAME); 
			}
			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoMasivoAdmProcesoAdapterVO.getProcesoMasivo().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_PRO_MAS, ProConstants.ACT_CANCELAR);
	}

	public ActionForward reiniciar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		// Bajo el adapter del userSession
		// ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapterVO = (ProcesoMasivoAdmProcesoAdapter) userSession.get(ProcesoAsentamientoAdapter.NAME);
		ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapterVO = (ProcesoMasivoAdmProcesoAdapter) userSession.get(ProcesoMasivoAdmProcesoAdapter.NAME);
			
		// Si es nulo no se puede continuar
		if (procesoMasivoAdmProcesoAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + ProcesoMasivoAdmProcesoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoAdmProcesoAdapter.NAME); 
		}
		
		return baseForwardAdapter(mapping, request, funcName, GdeConstants.FWD_ADMINISTRAR_CORRIDA_PRO_MAS, GdeConstants.ACT_REINICIAR);

	}
	
	

	public ActionForward siguientePaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			// ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapterVO = (ProcesoMasivoAdmProcesoAdapter) userSession.get(ProcesoAsentamientoAdapter.NAME);
			ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapterVO = (ProcesoMasivoAdmProcesoAdapter) userSession.get(ProcesoMasivoAdmProcesoAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoMasivoAdmProcesoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoMasivoAdmProcesoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoAdmProcesoAdapter.NAME); 
			}
			// Cargo el id de la corrida como parametro
			navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, procesoMasivoAdmProcesoAdapterVO.getProcesoMasivo().getCorrida().getId());
			return baseForwardAdapter(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_ADP_PRO_MAS, ProConstants.ACT_SIGUIENTE);
	}
	
	
	
	/// Retroceder un paso sin uso de adp
	
	public ActionForward retroceder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			// ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapterVO = (ProcesoMasivoAdmProcesoAdapter) userSession.get(ProcesoAsentamientoAdapter.NAME);
			ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapterVO = (ProcesoMasivoAdmProcesoAdapter) userSession.get(ProcesoMasivoAdmProcesoAdapter.NAME);
			navModel.setSelectedId(procesoMasivoAdmProcesoAdapterVO.getProcesoMasivo().getIdView());
			// Si es nulo no se puede continuar
			if (procesoMasivoAdmProcesoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoMasivoAdmProcesoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoAdmProcesoAdapter.NAME); 
			}
			
			return baseForwardAdapter(mapping, request, funcName, GdeConstants.FWD_ADMINISTRAR_CORRIDA_PRO_MAS, GdeConstants.ACT_RETROCEDER);
	}
	
	public ActionForward verTotalesDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapterVO = (ProcesoMasivoAdmProcesoAdapter) userSession.get(ProcesoMasivoAdmProcesoAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoMasivoAdmProcesoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoMasivoAdmProcesoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoAdmProcesoAdapter.NAME); 
			}
			
			procesoMasivoAdmProcesoAdapterVO.setCalcularTotalesDeuda(Boolean.TRUE);
			
			procesoMasivoAdmProcesoAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().cargarTotalesProcesoMasivoAdmProcesoAdapter(userSession, procesoMasivoAdmProcesoAdapterVO);
			
            // Tiene errores recuperables
			if (procesoMasivoAdmProcesoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoAdmProcesoAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoMasivoAdmProcesoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoMasivoAdmProcesoAdapter.NAME, procesoMasivoAdmProcesoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procesoMasivoAdmProcesoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoMasivoAdmProcesoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoMasivoAdmProcesoAdapter.NAME, procesoMasivoAdmProcesoAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ProcesoMasivoAdmProcesoAdapter.NAME, procesoMasivoAdmProcesoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcesoMasivoAdmProcesoAdapter.NAME, procesoMasivoAdmProcesoAdapterVO);
			 
			return mapping.findForward(GdeConstants.FWD_PROCESO_PROCESO_MASIVO_ADAPTER);
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, ProcesoMasivoAdmProcesoAdapter.NAME);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ProcesoMasivoAdmProcesoAdapter.NAME);
			
		}

	public ActionForward enviadoContr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapterVO = (ProcesoMasivoAdmProcesoAdapter) userSession.get(ProcesoMasivoAdmProcesoAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoMasivoAdmProcesoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoMasivoAdmProcesoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoAdmProcesoAdapter.NAME); 
			}
			
			String enviadoContr = request.getParameter("enviadoContr");
			procesoMasivoAdmProcesoAdapterVO.getProcesoMasivo().setEnviadoContr("1".equals(enviadoContr) ? 1L : 0L); 
			ProcesoMasivoVO procesoMasivoVO = GdeServiceLocator.getGestionDeudaJudicialService().enviadoContr(userSession, procesoMasivoAdmProcesoAdapterVO.getProcesoMasivo());
			
            // Tiene errores recuperables
			if (procesoMasivoAdmProcesoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoAdmProcesoAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoMasivoAdmProcesoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoMasivoAdmProcesoAdapter.NAME, procesoMasivoAdmProcesoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procesoMasivoAdmProcesoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoMasivoAdmProcesoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoMasivoAdmProcesoAdapter.NAME, procesoMasivoAdmProcesoAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ProcesoMasivoAdmProcesoAdapter.NAME, procesoMasivoAdmProcesoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcesoMasivoAdmProcesoAdapter.NAME, procesoMasivoAdmProcesoAdapterVO);
			 
			return mapping.findForward(GdeConstants.FWD_PROCESO_PROCESO_MASIVO_ADAPTER);
	}
	
	
	public ActionForward generarArchivosCDProcuradores(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			// Bajo el adapter del userSession
			ProcesoMasivoAdmProcesoAdapter procesoMasivoAdmProcesoAdapterVO = (ProcesoMasivoAdmProcesoAdapter) userSession.get(ProcesoMasivoAdmProcesoAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoMasivoAdmProcesoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoMasivoAdmProcesoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoAdmProcesoAdapter.NAME); 
			}
			
			procesoMasivoAdmProcesoAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().generarArchivosCDProcuradores(userSession, procesoMasivoAdmProcesoAdapterVO);
			
            // Tiene errores recuperables
			if (procesoMasivoAdmProcesoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoAdmProcesoAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoMasivoAdmProcesoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoMasivoAdmProcesoAdapter.NAME, procesoMasivoAdmProcesoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procesoMasivoAdmProcesoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoMasivoAdmProcesoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoMasivoAdmProcesoAdapter.NAME, procesoMasivoAdmProcesoAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ProcesoMasivoAdmProcesoAdapter.NAME, procesoMasivoAdmProcesoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcesoMasivoAdmProcesoAdapter.NAME, procesoMasivoAdmProcesoAdapterVO);
			 
			return mapping.findForward(GdeConstants.FWD_PROCESO_PROCESO_MASIVO_ADAPTER);
	}

	public ActionForward imprimirPadron(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return  baseForward(mapping, request, funcName, BaseConstants.ACT_BUSCAR, GdeConstants.ACTION_ADMINISTRAR_PLAENVDEUPRO, GdeConstants.ACTION_ADM_PLAENVDEUPRO_IMP_PAD);
		}

	public ActionForward imprimirConstancia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, GdeSecurityConstants.MTD_IMPRIMIR_CONSTANCIA);
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();

			//leemos constancia
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			ConstanciaDeuAdapter constanciaDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConstanciaDeuAdapterForView(userSession, commonKey);				
			constanciaDeuAdapterVO.setAct("impresionConstancia");
			
			// Seteo los valores de navegacion en el adapter
			constanciaDeuAdapterVO.setValuesFromNavModel(navModel);
			
			request.setAttribute(ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
			userSession.put(ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
			
			return mapping.findForward("imprimirConstancia");
	}

	/*
	public ActionForward imprimirPlanilla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return  baseForward(mapping, request, funcName, BaseConstants.ACT_INICIALIZAR, GdeConstants.ACTION_ADMINISTRAR_PLAENVDEUPRO, GdeConstants.ACTION_ADM_PLAENVDEUPRO_IMP_PAD);
		}

	public ActionForward imprimirConstancia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();		
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
		
		ConstanciaDeuAdapter constanciaDeuAdapterVO = new ConstanciaDeuAdapter();
		
		
		// llamada al servicio
		PrintModel print = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().imprimirConstanciaDeu(userSession, constanciaDeuAdapterVO);

		// Tiene errores recuperables
		if (constanciaDeuAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + constanciaDeuAdapterVO.infoString()); 
			saveDemodaErrors(request, constanciaDeuAdapterVO);
			return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
		}

		// Tiene errores no recuperables
		if (constanciaDeuAdapterVO.hasErrorNonRecoverable()) {
			log.error("error en: "  + funcName + ": " + constanciaDeuAdapterVO.errorString()); 
			return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuAdapter.NAME, constanciaDeuAdapterVO);
		}

		if (log.isDebugEnabled()) log.debug(funcName + ": " + ConstanciaDeuAdapter.NAME + ": "+ constanciaDeuAdapterVO.infoString());
		baseResponsePrintModel(response, print);
		return null;
	}
*/

}
