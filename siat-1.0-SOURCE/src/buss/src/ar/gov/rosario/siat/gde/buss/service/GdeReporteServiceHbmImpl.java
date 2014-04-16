//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.service;

/**
 * Implementacion de servicios de submodulo Reporte del modulo gde
 * @author tecso
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Categoria;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.CategoriaVO;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConvenio;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.bean.TipProMas;
import ar.gov.rosario.siat.gde.iface.model.ContribuyenteCerReport;
import ar.gov.rosario.siat.gde.iface.model.ConvenioACaducarReport;
import ar.gov.rosario.siat.gde.iface.model.ConvenioFormReport;
import ar.gov.rosario.siat.gde.iface.model.ConvenioReport;
import ar.gov.rosario.siat.gde.iface.model.DeudaAnuladaReport;
import ar.gov.rosario.siat.gde.iface.model.DeudaProcuradorReport;
import ar.gov.rosario.siat.gde.iface.model.DistribucionReport;
import ar.gov.rosario.siat.gde.iface.model.EmisionReport;
import ar.gov.rosario.siat.gde.iface.model.EstadoConvenioVO;
import ar.gov.rosario.siat.gde.iface.model.ImporteRecaudadoReport;
import ar.gov.rosario.siat.gde.iface.model.ImporteRecaudarReport;
import ar.gov.rosario.siat.gde.iface.model.PlanReport;
import ar.gov.rosario.siat.gde.iface.model.PlanVO;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.model.RecaudacionReport;
import ar.gov.rosario.siat.gde.iface.model.RecaudadoReport;
import ar.gov.rosario.siat.gde.iface.model.RespuestaOperativosReport;
import ar.gov.rosario.siat.gde.iface.model.TipProMasVO;
import ar.gov.rosario.siat.gde.iface.service.IGdeReporteService;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.TipoReporte;
import coop.tecso.demoda.iface.model.UserContext;

public class GdeReporteServiceHbmImpl implements IGdeReporteService { 
	
	private Logger log = Logger.getLogger(GdeReporteServiceHbmImpl.class);

	// ---> Reporte Convenios
	
	public ConvenioReport getConvenioReportInit(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ConvenioReport convenioReport = new ConvenioReport();

			// Obtengo el id del procurador de la sesion
			Long idProcuradorSesion = userContext.getIdProcurador();
			convenioReport.setIdProcuradorSesion(idProcuradorSesion);
			if(idProcuradorSesion != null){
				// solo podra acceder a los convenios del procurador de la sesion
				Procurador procurador = Procurador.getById(idProcuradorSesion);
				ProcuradorVO procuradorVO = (ProcuradorVO) procurador.toVO(0);
				convenioReport.getListProcurador().add(procuradorVO);
				convenioReport.getConvenio().setProcurador(procuradorVO);
				convenioReport.setVisualizarComboProcurador(true);
			}else{
				// selecciono la opcion Todos del Procurador
				convenioReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS));
				convenioReport.getConvenio().getProcurador().setId(-1L);
			}
			
			// carga de lista de todos los recursos, selecciona la opcion Seleccionar
			List<Recurso> listRecurso = Recurso.getListActivos();
			convenioReport.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				convenioReport.getListRecurso().add(item.toVOWithCategoria());							
			}
			convenioReport.getRecurso().setId(-1L);
			
			// lista vacia de planes, selecciona la opcion Todos
			convenioReport.getListPlan().add(new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS));
			convenioReport.getConvenio().getPlan().setId(-1L);
			
			// lista ViaDeuda, selecciona la opcion todos
			List<ViaDeuda> listViaDeuda = ViaDeuda.getList();
			convenioReport.setListViaDeuda((ArrayList<ViaDeudaVO>)ListUtilBean.toVO(listViaDeuda, 
					new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			convenioReport.getConvenio().getViaDeuda().setId(-1L);
			
			// carga la lista de todos los Estado Convenio, selecciona la opcion Todos
			List<EstadoConvenio> listEstadoConvenio = EstadoConvenio.getList();
			convenioReport.setListEstadoConvenio((ArrayList<EstadoConvenioVO>)ListUtilBean.toVO(listEstadoConvenio, 
					new EstadoConvenioVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			convenioReport.getConvenio().getEstadoConvenio().setId(-1L);
			
			// carga la lista de todos los TipoReporte, selecciona la opcion resumido
			List<TipoReporte> listTipoReporte = TipoReporte.getList();
			convenioReport.setListTipoReporte(listTipoReporte);
			convenioReport.setTipoReporte(TipoReporte.RESUMIDO);
			
			// Carga la fecha hasta con el dia de hoy
			convenioReport.setFechaConvenioHasta(new Date());
			
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_CONVENIO, DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				convenioReport.setProcesando(true);
				convenioReport.setDesRunningRun(runningRun.getDesCorrida());
				convenioReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				convenioReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_CONVENIO, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					PlanillaVO reporteGenerado = new PlanillaVO();						
					 
					FileCorrida fileCorrida = listFileCorrida.get(0);
					
					reporteGenerado.setTitulo(ultimaCorrida.getDesCorrida());
					reporteGenerado.setFileName(fileCorrida.getFileName().replace('\\' , '/'));
					reporteGenerado.setDescripcion(fileCorrida.getNombre()); 
					reporteGenerado.setCtdResultados(1L);
										
					convenioReport.setReporteGenerado(reporteGenerado);
					convenioReport.setExisteReporteGenerado(true);
				}else{
					convenioReport.setReporteGenerado(new PlanillaVO());
					convenioReport.setExisteReporteGenerado(false);
				}
			}else{
				convenioReport.setReporteGenerado(new PlanillaVO());
				convenioReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_CONVENIO, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					convenioReport.setError(true);
					convenioReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					convenioReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					convenioReport.setError(false);
				}
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return convenioReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

		// Params ConvenioReport
	
	public ConvenioReport getConvenioReportParamRecurso(UserContext userContext, ConvenioReport convenioReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes
			convenioReport.clearErrorMessages();

			// si el recurso no esta seleccionado
			if (ModelUtil.isNullOrEmpty(convenioReport.getRecurso())){
				
				// lista vacia de planes, selecciona la opcion Todos
				convenioReport.setListPlan(new ArrayList<PlanVO>());
				convenioReport.getListPlan().add(new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS));
				convenioReport.getConvenio().getPlan().setId(-1L);

				// lista vacia de procuradores, selecciona la opcion Todos, 
				// solo si NO tenemos un procurador en la sesion
				if (convenioReport.getIdProcuradorSesion() == null){
					convenioReport.setListProcurador(new ArrayList<ProcuradorVO>());
					convenioReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS));
					convenioReport.getConvenio().getProcurador().setId(-1L);
				}
				
				// selecciono la opcion Todos del plan
				convenioReport.getConvenio().getPlan().setId(-1L);

				// selecciono la opcion Todos de la ViaDeuda
				convenioReport.getConvenio().getViaDeuda().setId(-1L);
				
				if (convenioReport.getIdProcuradorSesion() == null){
					// selecciono la opcion Todos del Procurador
					convenioReport.getConvenio().getProcurador().setId(-1L);
				}
				
			}else{
				// si el recurso esta seleccionado, obtencion del recurso
				Recurso recurso = Recurso.getById(convenioReport.getRecurso().getId());
				
				// lista de planes asociadas al recurso
				List<Plan> listPlan = Plan.getListByIdRecurso(recurso.getId());
				convenioReport.setListPlan((ArrayList<PlanVO>)ListUtilBean.toVO(listPlan, 
						new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				convenioReport.getConvenio().getPlan().setId(-1L);
				
				// Lista de procuradores asociados al recurso
				// Solo si NO tenemos un procurador en la sesion
				if (convenioReport.getIdProcuradorSesion() == null){
					List<Procurador> listProcurador = Procurador.getListByRecurso(recurso);
					convenioReport.setListProcurador((ArrayList<ProcuradorVO>) ListUtilBean.toVO(listProcurador));

					//Si no hay procuradores:
					if (convenioReport.getListProcurador().size()<=1){
						convenioReport.setVisualizarComboProcurador(false);
						convenioReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
					} else {
						convenioReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS));
					}

					convenioReport.getConvenio().getProcurador().setId(-1L);
				}			
			}
			
			boolean esViaJudicial = (ViaDeuda.ID_VIA_JUDICIAL == convenioReport.getConvenio().getViaDeuda().getId());
			boolean visualizarComboProcurador = (convenioReport.getIdProcuradorSesion() != null || esViaJudicial);
			convenioReport.setVisualizarComboProcurador(visualizarComboProcurador );

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return convenioReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ConvenioReport getConvenioReportParamPlan(UserContext userContext, ConvenioReport convenioReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes
			convenioReport.clearErrorMessages();
			
			// si el plan esta seleccionado 
			if (!ModelUtil.isNullOrEmpty(convenioReport.getConvenio().getPlan())){
				// selecciono la opcion Todos de la ViaDeuda
				//convenioReport.getConvenio().getViaDeuda().setId(-1L);
				//Setea la via que corresponda al plan pero deshabilita el combo
				Plan plan = Plan.getById(convenioReport.getConvenio().getPlan().getId());
				convenioReport.getConvenio().getViaDeuda().setId(plan.getViaDeuda().getId());
			}
			
			boolean esViaJudicial = (ViaDeuda.ID_VIA_JUDICIAL == convenioReport.getConvenio().getViaDeuda().getId());
			boolean visualizarComboProcurador = (convenioReport.getIdProcuradorSesion() != null || esViaJudicial);
			convenioReport.setVisualizarComboProcurador(visualizarComboProcurador);
			if(!visualizarComboProcurador){
				// selecciono TODOS los procuradores.
				convenioReport.getConvenio().getProcurador().setId(-1L);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return convenioReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ConvenioReport getConvenioReportParamViaDeuda(UserContext userContext, ConvenioReport convenioReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes
			convenioReport.clearErrorMessages();
			
			boolean esViaJudicial = (ViaDeuda.ID_VIA_JUDICIAL == convenioReport.getConvenio().getViaDeuda().getId());
			boolean visualizarComboProcurador = (convenioReport.getIdProcuradorSesion() != null || esViaJudicial);
			convenioReport.setVisualizarComboProcurador(visualizarComboProcurador );
			if(!visualizarComboProcurador){
				// selecciono la opcion Todos del Procurador
				convenioReport.getConvenio().getProcurador().setId(-1L);
			}

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return convenioReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
		// Fin Params ConvenioReport
	
	public ConvenioReport getConvenioReportResult(UserContext userContext, ConvenioReport convenioReport) throws DemodaServiceException{

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		convenioReport.setListResult(new ArrayList());
		convenioReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();


			// Validaciones: 
			
			//recurso requerido
			if ( ModelUtil.isNullOrEmpty(convenioReport.getRecurso())){
				convenioReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONVENIO_REPORT_RECURSO);
				return convenioReport;
			}

			// fecha convenio hasta no sea menor a fecha convenio desde (si se ingresaron)
			Date fecConvDesde = convenioReport.getFechaConvenioDesde();
			Date fecConvHasta = convenioReport.getFechaConvenioHasta();
			
			if ( fecConvDesde != null && fecConvHasta != null &&
					DateUtil.isDateAfter(fecConvDesde, fecConvHasta)){
				convenioReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.CONVENIO_REPORT_FECHA_CONVENIO_DESDE, 
					GdeError.CONVENIO_REPORT_FECHA_CONVENIO_HASTA);
			}

			// cuota Hasta no sea menor a cuota Desde (si se ingresaron)
			Integer cuotaDesde = convenioReport.getCuotaDesde();
			Integer cuotaHasta = convenioReport.getCuotaHasta();

			if ( cuotaDesde != null && cuotaDesde < 1){
				convenioReport.addRecoverableError(BaseError.MSG_VALORMENORQUE, 
					GdeError.CONVENIO_REPORT_CUOTA_DESDE, "1");
			}

			if ( cuotaHasta != null && cuotaHasta < 1){
				convenioReport.addRecoverableError(BaseError.MSG_VALORMENORQUE, 
					GdeError.CONVENIO_REPORT_CUOTA_HASTA, "1");
			}

			if ( cuotaDesde != null && cuotaHasta != null &&
					cuotaDesde >= 1 && cuotaHasta >= 1 &&
					cuotaHasta < cuotaDesde){
				convenioReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.CONVENIO_REPORT_CUOTA_DESDE, 
					GdeError.CONVENIO_REPORT_CUOTA_HASTA);
			}
			
			if(convenioReport.getVisualizarComboProcurador()){
				if(!convenioReport.getConvenio().getProcurador().getId().equals(-1L)){
					/*
					if (ModelUtil.isNullOrEmpty(convenioReport.getConvenio().getPlan().getProcurador())){
						convenioReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.PROCURADOR_LABEL);
					}
					*/
				}
			}
			
			if(convenioReport.hasError()){
				log.debug("Errores en las validaciones");
				return convenioReport;
			}
			
			// Disparar el proceso adp.
			String adpMessage = "La peticion del usuario "+DemodaUtil.currentUserContext().getUserName()
								+" hecha el "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_CONVENIO, adpMessage);
			run.create();
			
			String ID_RECURSO_PARAM = "idRecurso";
			String ID_PLAN_PARAM = "idPlan";
			String ID_VIA_DEUDA_PARAM = "idViaDeuda";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String ID_PROCURADOR_PARAM = "idProcurador";
			String CUOTA_DESDE_PARAM = "cuotaDesde";
			String CUOTA_HASTA_PARAM = "cuotaHasta";
			String ID_ESTADO_CONVENIO_PARAM = "idEstadoConvenio";
			String ID_TIPO_REPORTE_PARAM = "idTipoReporte";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
			
			String idRecurso = convenioReport.getRecurso().getId().toString();
			String idPlan = convenioReport.getConvenio().getPlan().getId().toString();
			String idViaDeuda = convenioReport.getConvenio().getViaDeuda().getId().toString();
			String fechaDesde= convenioReport.getFechaConvenioDesdeView();
			String fechaHasta= convenioReport.getFechaConvenioHastaView();
			String idProcurador = convenioReport.getConvenio().getProcurador().getId().toString();
			String cuotaDesdeView= convenioReport.getCuotaDesdeView();
			String cuotaHastaView= convenioReport.getCuotaHastaView();
			String idEstadoConvenio= convenioReport.getConvenio().getEstadoConvenio().getId().toString();
			String idTipoReporte= convenioReport.getTipoReporte().getId().toString();
			
			// Carga de parametros para adp
			run.putParameter(ID_PLAN_PARAM, idPlan);
			run.putParameter(ID_RECURSO_PARAM, idRecurso);
			run.putParameter(ID_VIA_DEUDA_PARAM, idViaDeuda);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(ID_PROCURADOR_PARAM, idProcurador);
			run.putParameter(CUOTA_DESDE_PARAM, cuotaDesdeView);
			run.putParameter(CUOTA_HASTA_PARAM, cuotaHastaView);
			run.putParameter(ID_ESTADO_CONVENIO_PARAM, idEstadoConvenio);
			run.putParameter(ID_TIPO_REPORTE_PARAM, idTipoReporte);
			
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			convenioReport.setProcesando(true);
			convenioReport.setDesRunningRun(run.getDesCorrida());
			convenioReport.setEstRunningRun(run.getMensajeEstado());
			convenioReport.setReporteGenerado(new PlanillaVO());
			convenioReport.setExisteReporteGenerado(false);
			convenioReport.setError(false);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return convenioReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	// <--- Reporte Convenios
	
	public RecaudacionReport getRecaudacionReportInit(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			RecaudacionReport recaudacionReport = new RecaudacionReport();

			// carga de lista de todas las categorias, selecciona la opcion Todos
			List<Categoria> listCategoria = Categoria.getList();
			recaudacionReport.setListCategoria((ArrayList<CategoriaVO>) ListUtilBean.toVO(listCategoria, 0, new CategoriaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			recaudacionReport.getRecurso().getCategoria().setId(-1L);
			
			// carga de lista de todos los recursos, selecciona la opcion Todos
			List<Recurso> listRecurso = Recurso.getListActivos();
			recaudacionReport.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				recaudacionReport.getListRecurso().add(item.toVOWithCategoria());							
			}
			recaudacionReport.getRecurso().setId(-1L);
			
			// carga de lista de todas las via deuda, selecciona la opcion Todos
			List<ViaDeuda> listViaDeuda = ViaDeuda.getListActivos();
			recaudacionReport.setListViaDeuda((ArrayList<ViaDeudaVO>) ListUtilBean.toVO(listViaDeuda, 0, new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			recaudacionReport.getViaDeuda().setId(-1L);
			
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_RECAUDACION);
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				recaudacionReport.setProcesando(true);
				recaudacionReport.setDesRunningRun(runningRun.getDesCorrida());
				recaudacionReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				recaudacionReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_RECAUDACION);
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					PlanillaVO reporteGenerado = new PlanillaVO();						
					 
					FileCorrida fileCorrida = listFileCorrida.get(0);
					
					reporteGenerado.setTitulo(ultimaCorrida.getDesCorrida());
					reporteGenerado.setFileName(fileCorrida.getFileName().replace('\\' , '/'));
					reporteGenerado.setDescripcion(fileCorrida.getNombre()); 
					reporteGenerado.setCtdResultados(1L);
										
					recaudacionReport.setReporteGenerado(reporteGenerado);
					recaudacionReport.setExisteReporteGenerado(true);
				}else{
					recaudacionReport.setReporteGenerado(new PlanillaVO());
					recaudacionReport.setExisteReporteGenerado(false);
				}
			}else{
				recaudacionReport.setReporteGenerado(new PlanillaVO());
				recaudacionReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_RECAUDACION);
				if(lastEndWrongRun != null){
					recaudacionReport.setError(true);
					recaudacionReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					recaudacionReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					recaudacionReport.setError(false);
				}
			}
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return recaudacionReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
	public RecaudacionReport getRecaudacionReportParamCategoria(UserContext userContext, RecaudacionReport recaudacionReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes
			recaudacionReport.clearErrorMessages();
			
			recaudacionReport.setListRecurso(new ArrayList<RecursoVO>());
			List<Recurso> listRecurso = new ArrayList<Recurso>();
			
			// si la categoria no esta seleccionado
			if (ModelUtil.isNullOrEmpty(recaudacionReport.getRecurso().getCategoria())){
				
				// carga de lista de todos los recursos, selecciona la opcion Todos
				listRecurso = Recurso.getListActivos();
			}else{
				// si la categoria esta seleccionada, no hace falta obtener la categoria
				// lista de recursos asociadas a la categoria
				listRecurso = Recurso.getListByIdCategoria(recaudacionReport.getRecurso().getCategoria().getId());
			}
			
			// toVo adecuado de la lista de recursos y selecciono la opcion todos
			recaudacionReport.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				recaudacionReport.getListRecurso().add(item.toVOWithCategoria());							
			}
			recaudacionReport.getRecurso().setId(-1L);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return recaudacionReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public RecaudacionReport getRecaudacionReportResult(UserContext userContext, RecaudacionReport recaudacionReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		recaudacionReport.setListResult(new ArrayList());
		recaudacionReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Validaciones: 
			Date fecDesde = recaudacionReport.getFechaDesde();
			Date fecHasta = recaudacionReport.getFechaHasta();
			
			// tipo de fecha
			if(recaudacionReport.getTipoFecha().intValue()<=0){
				recaudacionReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
						GdeError.RECAUDACION_REPORT_TIPO_FECHA);	
			}
			
			// fecha desde requerida
			if ( fecDesde == null){
				recaudacionReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
						GdeError.RECAUDACION_REPORT_FECHA_DESDE);
			} 
			// fecha hasta requerida
			if ( fecHasta == null){
				recaudacionReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
						GdeError.RECAUDACION_REPORT_FECHA_HASTA);
			} 
			
			if(recaudacionReport.hasError()){
				log.debug("Errores en las validaciones");
				return recaudacionReport;
			}

			// fecha hasta no sea menor a fecha desde
			if ( DateUtil.isDateAfter(fecDesde, fecHasta)){
				recaudacionReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.RECAUDACION_REPORT_FECHA_DESDE, 
					GdeError.RECAUDACION_REPORT_FECHA_HASTA);
			}
			
			if(recaudacionReport.hasError()){
				log.debug("Errores en las validaciones");
				return recaudacionReport;
			}
					
			// Disparar el proceso adp.
			String adpMessage = "La peticion del usuario "+DemodaUtil.currentUserContext().getUserName()
								+" hecha el "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_RECAUDACION, adpMessage);
			run.create();
			
			String ID_CATEGORIA_PARAM = "idCategoria";
			String ID_RECURSO_PARAM = "idRecurso";
			String ID_VIA_DEUDA_PARAM = "idViaDeuda";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String TIPO_FECHA_PARAM ="tipoFecha";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
			
			String idCategoria = recaudacionReport.getRecurso().getCategoria().getId().toString();
			String idRecurso = recaudacionReport.getRecurso().getId().toString();
			String idViaDeuda = recaudacionReport.getViaDeuda().getId().toString();
			String fechaDesde= recaudacionReport.getFechaDesdeView();
			String fechaHasta= recaudacionReport.getFechaHastaView();
			String idTipoFecha = String.valueOf(recaudacionReport.getTipoFecha().intValue());
			
			// Carga de parametros para adp
			run.putParameter(ID_CATEGORIA_PARAM, idCategoria);
			run.putParameter(ID_RECURSO_PARAM, idRecurso);
			run.putParameter(ID_VIA_DEUDA_PARAM, idViaDeuda);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(TIPO_FECHA_PARAM, idTipoFecha);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId();
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld();
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			recaudacionReport.setProcesando(true);
			recaudacionReport.setDesRunningRun(run.getDesCorrida());
			recaudacionReport.setEstRunningRun(run.getMensajeEstado());
			recaudacionReport.setReporteGenerado(new PlanillaVO());
			recaudacionReport.setExisteReporteGenerado(false);
			recaudacionReport.setError(false);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return recaudacionReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	// ---> Consultar Importe a Recaudar
	
	public ImporteRecaudarReport getImporteRecaudarReportInit(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ImporteRecaudarReport importeRecaudarReport = new ImporteRecaudarReport();
			
			// Obtengo el id del procurador de la sesion
			Long idProcuradorSesion = userContext.getIdProcurador();
			importeRecaudarReport.setIdProcuradorSesion(idProcuradorSesion);
			if(idProcuradorSesion != null){
				// solo podra acceder a los convenios del procurador de la sesion
				Procurador procurador = Procurador.getById(idProcuradorSesion);
				ProcuradorVO procuradorVO = (ProcuradorVO) procurador.toVO(0);
				importeRecaudarReport.getListProcurador().add(procuradorVO);
				importeRecaudarReport.getPlan().setProcurador(procuradorVO);
				importeRecaudarReport.setVisualizarComboProcurador(true);
			}else{
				// selecciono la opcion Todos del Procurador
				importeRecaudarReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS));
				importeRecaudarReport.getPlan().getProcurador().setId(-1L);
			}
			
			// carga de lista de todos los recursos, selecciona la opcion Todos
			List<Recurso> listRecurso = Recurso.getListActivos();
			importeRecaudarReport.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				importeRecaudarReport.getListRecurso().add(item.toVOWithCategoria());
			}
			importeRecaudarReport.getPlan().getRecurso().setId(-1L);
			
			importeRecaudarReport.getListPlan().add(new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS));			
			importeRecaudarReport.getPlan().getRecurso().setId(-1L);
			
			// Setea la fechaHasta en la fecha del dia
			importeRecaudarReport.setFechaVencimientoHasta(new Date());
			
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_IMPORTE_RECAUDAR, DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				importeRecaudarReport.setProcesando(true);
				importeRecaudarReport.setDesRunningRun(runningRun.getDesCorrida());
				importeRecaudarReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				importeRecaudarReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_IMPORTE_RECAUDAR, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					PlanillaVO reporteGenerado = new PlanillaVO();						
					 
					FileCorrida fileCorrida = listFileCorrida.get(0);
					
					reporteGenerado.setTitulo(ultimaCorrida.getDesCorrida());
					reporteGenerado.setFileName(fileCorrida.getFileName().replace('\\' , '/'));
					reporteGenerado.setDescripcion(fileCorrida.getNombre()); 
					reporteGenerado.setCtdResultados(1L);
										
					importeRecaudarReport.setReporteGenerado(reporteGenerado);
					importeRecaudarReport.setExisteReporteGenerado(true);
				}else{
					importeRecaudarReport.setReporteGenerado(new PlanillaVO());
					importeRecaudarReport.setExisteReporteGenerado(false);
				}
			}else{
				importeRecaudarReport.setReporteGenerado(new PlanillaVO());
				importeRecaudarReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_IMPORTE_RECAUDAR, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					importeRecaudarReport.setError(true);
					importeRecaudarReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					importeRecaudarReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					importeRecaudarReport.setError(false);
				}
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return importeRecaudarReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
		// Params Consultar Importe a Recaudar
	
	public ImporteRecaudarReport getImporteRecaudarReportParamRecurso(UserContext userContext, ImporteRecaudarReport importeRecaudarReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Limpio errores, mensajes
			importeRecaudarReport.clearErrorMessages();
			
			//////////////////////////////////////////////
			// Si el recurso no esta seleccionado
			if (ModelUtil.isNullOrEmpty(importeRecaudarReport.getPlan().getRecurso())){
				
				// Lista vacia de planes, selecciona la opcion Todos
				importeRecaudarReport.setListPlan(new ArrayList<PlanVO>());
				importeRecaudarReport.getListPlan().add(new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS));
				importeRecaudarReport.getPlan().setId(-1L);

				// Lista vacia de procuradores, selecciona la opcion Todos, 
				// Solo si NO tenemos un procurador en la sesion
				if (importeRecaudarReport.getIdProcuradorSesion() == null){
					importeRecaudarReport.setListProcurador(new ArrayList<ProcuradorVO>());
					importeRecaudarReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS));
					importeRecaudarReport.getPlan().getProcurador().setId(-1L);
				}	
				
			}else{
				// Si el recurso esta seleccionado, obtencion del recurso
				Recurso recurso = Recurso.getById(importeRecaudarReport.getPlan().getRecurso().getId());
				
				// Lista de planes asociadas al recurso
				List<Plan> listPlan = Plan.getListByIdRecurso(recurso.getId());
				importeRecaudarReport.setListPlan((ArrayList<PlanVO>)ListUtilBean.toVO(listPlan, new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				importeRecaudarReport.getPlan().setId(-1L);
				
				// Lista de procuradores asociados al recurso
				// Solo si NO tenemos un procurador en la sesion
				if (importeRecaudarReport.getIdProcuradorSesion() == null){
					List<Procurador> listProcurador = Procurador.getListByRecurso(recurso);
					//importeRecaudarReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS));			
					importeRecaudarReport.setListProcurador((ArrayList<ProcuradorVO>) ListUtilBean.toVO(listProcurador, new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
					importeRecaudarReport.getPlan().getProcurador().setId(-1L);
				}			
			}
			//////////////////////////////////////////////		
			

			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return importeRecaudarReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
	public ImporteRecaudarReport getImporteRecaudarReportParamPlan(UserContext userContext, ImporteRecaudarReport importeRecaudarReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {	

			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes
			importeRecaudarReport.clearErrorMessages();
			
			//Si selecciono un plan no permite seleccionar viaDeuda
			Long idPlan = importeRecaudarReport.getPlan().getId();
			if(idPlan!=null && idPlan.longValue()>0){
				//Setea la via que corresponda al plan pero deshabilita el combo
				Plan plan = Plan.getById(importeRecaudarReport.getPlan().getId());
				importeRecaudarReport.getPlan().getViaDeuda().setId(plan.getViaDeuda().getId());
			}
			
			boolean esViaJudicial = (ViaDeuda.ID_VIA_JUDICIAL == importeRecaudarReport.getPlan().getViaDeuda().getId());
			boolean visualizarComboProcurador = (importeRecaudarReport.getIdProcuradorSesion() != null || esViaJudicial);
			
			importeRecaudarReport.setVisualizarComboProcurador(visualizarComboProcurador);
			
			if (importeRecaudarReport.getIdProcuradorSesion() == null){
				if(!visualizarComboProcurador) {
					importeRecaudarReport.getListProcurador().clear();
					importeRecaudarReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
					// selecciono la opcion Ninguno del Procurador
					importeRecaudarReport.getPlan().getProcurador().setId(-1L);
				} else {
					Recurso recurso = Recurso.getById(importeRecaudarReport.getPlan().getRecurso().getId());
					
					List<Procurador> listProcurador = Procurador.getListByRecurso(recurso);
					//importeRecaudarReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS));			
					importeRecaudarReport.setListProcurador((ArrayList<ProcuradorVO>) ListUtilBean.toVO(listProcurador, new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
					importeRecaudarReport.getPlan().getProcurador().setId(-1L);
				}	
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return importeRecaudarReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
		// Fin Params Consultar Importe a Recaudar
	
	public ImporteRecaudarReport getImporteRecaudarReportResult(UserContext userContext, ImporteRecaudarReport importeRecaudarReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		importeRecaudarReport.setListResult(new ArrayList<PlanVO>());
		importeRecaudarReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();


			// Validaciones: 
			if(importeRecaudarReport.getPlan().getRecurso().getId().longValue()<0){
				log.debug("Errores en las validaciones");
				importeRecaudarReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);				
			}
			Date fecVtoDesde = importeRecaudarReport.getFechaVencimientoDesde();
			if(fecVtoDesde==null){
				log.debug("Errores en las validaciones");
				importeRecaudarReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
												GdeError.IMPORTE_RECAUDAR_FECHADESDE_LABEL);				
			}			
			Date fecVtoHasta = importeRecaudarReport.getFechaVencimientoHasta();
			if(fecVtoHasta==null){
				log.debug("Errores en las validaciones");
				importeRecaudarReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
												GdeError.IMPORTE_RECAUDAR_FECHAHASTA_LABEL);				
			}
			if(fecVtoDesde!=null && fecVtoHasta!=null && DateUtil.isDateAfter(fecVtoDesde, fecVtoHasta)){
				log.debug("Errores en las validaciones");
				importeRecaudarReport.addRecoverableError(BaseError.MSG_VALORMENORQUE, 
						GdeError.IMPORTE_RECAUDAR_FECHAHASTA_LABEL, GdeError.IMPORTE_RECAUDAR_FECHADESDE_LABEL);				
			}
			
			/*
			if(importeRecaudarReport.isVisualizarComboProcurador()){
				if (ModelUtil.isNullOrEmpty(importeRecaudarReport.getPlan().getProcurador())){
					importeRecaudarReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.PROCURADOR_LABEL);
				}
			}
			*/

			if(importeRecaudarReport.hasError()){
				return importeRecaudarReport;
			}	
			
			// Disparar el proceso adp.
			String adpMessage = "La peticion del usuario "+DemodaUtil.currentUserContext().getUserName()
								+" hecha el "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_IMPORTE_RECAUDAR, adpMessage);
			run.create();
			
			String ID_RECURSO_PARAM = "idRecurso";
			String ID_PLAN_PARAM = "idPlan";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
			String ID_PROCURADOR_PARAM = "idProcurador";
			
			String idRecurso = importeRecaudarReport.getPlan().getRecurso().getId().toString();
			String idPlan = importeRecaudarReport.getPlan().getId().toString();
			String fechaDesde= importeRecaudarReport.getFechaVencimientoDesdeView();
			String fechaHasta= importeRecaudarReport.getFechaVencimientoHastaView();
			String idProcurador = importeRecaudarReport.getPlan().getProcurador().getId().toString();
			
			// Carga de parametros para adp
			run.putParameter(ID_RECURSO_PARAM, idRecurso);
			run.putParameter(ID_PLAN_PARAM, idPlan);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			run.putParameter(ID_PROCURADOR_PARAM, idProcurador);
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			importeRecaudarReport.setProcesando(true);
			importeRecaudarReport.setDesRunningRun(run.getDesCorrida());
			importeRecaudarReport.setEstRunningRun(run.getMensajeEstado());
			importeRecaudarReport.setReporteGenerado(new PlanillaVO());
			importeRecaudarReport.setExisteReporteGenerado(false);
			importeRecaudarReport.setError(false);
			
			// Seteamos el recurso seleccionado
			
			// carga de lista de todos los recursos.
			List<Recurso> listRecurso = Recurso.getList();
			importeRecaudarReport.setListRecurso(new ArrayList<RecursoVO>());
			importeRecaudarReport.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				importeRecaudarReport.getListRecurso().add(item.toVOWithCategoria());							
			}
			
			Recurso recurso = Recurso.getById(importeRecaudarReport.getPlan().getRecurso().getId());
			importeRecaudarReport.getPlan().setRecurso((RecursoVO) recurso.toVO(0));
			
			
			List<Plan> listPlan = Plan.getListByIdRecurso(importeRecaudarReport.getPlan().getRecurso().getId());
			importeRecaudarReport.setListPlan((ArrayList<PlanVO>)ListUtilBean.toVO(listPlan, 0, 
									new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS)));	
					
				
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return importeRecaudarReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	// <--- Consultar Importe a Recaudar
	
	// ---> Consultar Importe Recaudado de planes
	
	public ImporteRecaudadoReport getImporteRecaudadoPlanesReportInit(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ImporteRecaudadoReport importeRecaudadoReport = new ImporteRecaudadoReport();

			// Setea la fechaHasta en la fecha del dia
			importeRecaudadoReport.setFechaPagoHasta(new Date());
			
			// Obtengo el id del procurador de la sesion
			Long idProcuradorSesion = userContext.getIdProcurador();
			importeRecaudadoReport.setIdProcuradorSesion(idProcuradorSesion);
			if(idProcuradorSesion != null){
				// solo podra acceder a los convenios del procurador de la sesion
				Procurador procurador = Procurador.getById(idProcuradorSesion);
				ProcuradorVO procuradorVO = (ProcuradorVO) procurador.toVO(0);
				importeRecaudadoReport.getListProcurador().add(procuradorVO);
				importeRecaudadoReport.getPlan().setProcurador(procuradorVO);
				importeRecaudadoReport.setVisualizarComboProcurador(true);
			}else{
				// selecciono la opcion Todos del Procurador
				importeRecaudadoReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS));
				importeRecaudadoReport.getPlan().getProcurador().setId(-1L);
			}
			
			// carga de lista de todos los recursos, selecciona la opcion Todos
			List<Recurso> listRecurso = Recurso.getListActivos();
			
			importeRecaudadoReport.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				importeRecaudadoReport.getListRecurso().add(item.toVOWithCategoria());							
			}
			importeRecaudadoReport.getPlan().getRecurso().setId(-1L);
			
			// Se inicializa la lista de planes vacia
			importeRecaudadoReport.getListPlan().add(new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS));			

			// Se setea la lista de viaDeuda
			importeRecaudadoReport.setListViaDeuda((ArrayList<ViaDeudaVO>) ListUtilBean.toVO(ViaDeuda.getListActivos(), 0, new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_IMPORTE_RECAUDADO, DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				importeRecaudadoReport.setProcesando(true);
				importeRecaudadoReport.setDesRunningRun(runningRun.getDesCorrida());
				importeRecaudadoReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				importeRecaudadoReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_IMPORTE_RECAUDADO, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					PlanillaVO reporteGenerado = new PlanillaVO();						
					 
					FileCorrida fileCorrida = listFileCorrida.get(0);
					
					reporteGenerado.setTitulo(ultimaCorrida.getDesCorrida());
					reporteGenerado.setFileName(fileCorrida.getFileName().replace('\\' , '/'));
					reporteGenerado.setDescripcion(fileCorrida.getNombre()); 
					reporteGenerado.setCtdResultados(1L);
										
					importeRecaudadoReport.setReporteGenerado(reporteGenerado);
					importeRecaudadoReport.setExisteReporteGenerado(true);
				}else{
					importeRecaudadoReport.setReporteGenerado(new PlanillaVO());
					importeRecaudadoReport.setExisteReporteGenerado(false);
				}
			}else{
				importeRecaudadoReport.setReporteGenerado(new PlanillaVO());
				importeRecaudadoReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_IMPORTE_RECAUDADO, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					importeRecaudadoReport.setError(true);
					importeRecaudadoReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					importeRecaudadoReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					importeRecaudadoReport.setError(false);
				}
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return importeRecaudadoReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
		// Params Consultar Importe Recaudado de planes
	
	public ImporteRecaudadoReport getImporteRecaudadoPlanesReportParamRecurso(UserContext userContext, ImporteRecaudadoReport importeRecaudadoReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {	

			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes
			importeRecaudadoReport.clearErrorMessages();
			
			//////////////////////////////////////////////
			// si el recurso no esta seleccionado
			if (ModelUtil.isNullOrEmpty(importeRecaudadoReport.getPlan().getRecurso())){
				
				// lista vacia de planes, selecciona la opcion Todos
				importeRecaudadoReport.setListPlan(new ArrayList<PlanVO>());
				importeRecaudadoReport.getListPlan().add(new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS));
				importeRecaudadoReport.getPlan().setId(-1L);

				// lista vacia de procuradores, selecciona la opcion Todos, 
				// solo si NO tenemos un procurador en la sesion
				if (importeRecaudadoReport.getIdProcuradorSesion() == null){
					importeRecaudadoReport.setListProcurador(new ArrayList<ProcuradorVO>());
					importeRecaudadoReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS));
					importeRecaudadoReport.getPlan().getProcurador().setId(-1L);
				}
				
				// selecciono la opcion Todos del plan
				importeRecaudadoReport.getPlan().setId(-1L);

				// selecciono la opcion Todos de la ViaDeuda
				importeRecaudadoReport.getPlan().getViaDeuda().setId(-1L);
				
				if (importeRecaudadoReport.getIdProcuradorSesion() == null){
					// selecciono la opcion Todos del Procurador
					importeRecaudadoReport.getPlan().getProcurador().setId(-1L);
				}
				
			}else{
				// si el recurso esta seleccionado, obtencion del recurso
				Recurso recurso = Recurso.getById(importeRecaudadoReport.getPlan().getRecurso().getId());
				
				// lista de planes asociadas al recurso
				List<Plan> listPlan = Plan.getListByIdRecurso(recurso.getId());
				importeRecaudadoReport.setListPlan((ArrayList<PlanVO>)ListUtilBean.toVO(listPlan, new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				importeRecaudadoReport.getPlan().setId(-1L);
				
				// Lista de procuradores asociados al recurso
				// Solo si NO tenemos un procurador en la sesion
				if (importeRecaudadoReport.getIdProcuradorSesion() == null){
					List<Procurador> listProcurador = Procurador.getListByRecurso(recurso);
					importeRecaudadoReport.setListProcurador((ArrayList<ProcuradorVO>) ListUtilBean.toVO(listProcurador));

					//Si no hay procuradores:
					if (importeRecaudadoReport.getListProcurador().size()<=1){
						importeRecaudadoReport.setVisualizarComboProcurador(false);
						importeRecaudadoReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
					} else {
						importeRecaudadoReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS));
					}

					importeRecaudadoReport.getPlan().getProcurador().setId(-1L);
				}			
			}
			//////////////////////////////////////////////
			
			// habilita el combo de viaDeuda
			importeRecaudadoReport.setViaDeudaEnabled("true");
			
			boolean esViaJudicial = (ViaDeuda.ID_VIA_JUDICIAL == importeRecaudadoReport.getPlan().getViaDeuda().getId());
			boolean visualizarComboProcurador = (importeRecaudadoReport.getIdProcuradorSesion() != null || esViaJudicial);
			importeRecaudadoReport.setVisualizarComboProcurador(visualizarComboProcurador);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return importeRecaudadoReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ImporteRecaudadoReport getImporteRecaudadoPlanesReportParamPlan(UserContext userContext, ImporteRecaudadoReport importeRecaudadoReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {	

			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes
			importeRecaudadoReport.clearErrorMessages();
			
			//Si selecciono un plan no permite seleccionar viaDeuda
			Long idPlan = importeRecaudadoReport.getPlan().getId();
			if(idPlan!=null && idPlan.longValue()>0){
				//Setea la via que corresponda al plan pero deshabilita el combo
				Plan plan = Plan.getById(importeRecaudadoReport.getPlan().getId());
				importeRecaudadoReport.getPlan().getViaDeuda().setId(plan.getViaDeuda().getId());
				
				importeRecaudadoReport.setViaDeudaEnabled("false");
			}else{
				//habilita el combo
				importeRecaudadoReport.setViaDeudaEnabled("true");
			}
			
			boolean esViaJudicial = (ViaDeuda.ID_VIA_JUDICIAL == importeRecaudadoReport.getPlan().getViaDeuda().getId());
			boolean visualizarComboProcurador = (importeRecaudadoReport.getIdProcuradorSesion() != null || esViaJudicial);
			importeRecaudadoReport.setVisualizarComboProcurador(visualizarComboProcurador);
			
			if (importeRecaudadoReport.getIdProcuradorSesion() == null){
				if(!visualizarComboProcurador) {
					importeRecaudadoReport.getListProcurador().clear();
					importeRecaudadoReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
					// SDselecciono la opcion Ninguno del Procurador
					importeRecaudadoReport.getPlan().getProcurador().setId(-1L);
				} else {
					Recurso recurso = Recurso.getById(importeRecaudadoReport.getPlan().getRecurso().getId());
					
					List<Procurador> listProcurador = Procurador.getListByRecurso(recurso);
					//importeRecaudarReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS));			
					importeRecaudadoReport.setListProcurador((ArrayList<ProcuradorVO>) ListUtilBean.toVO(listProcurador, new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
					importeRecaudadoReport.getPlan().getProcurador().setId(-1L);
				}	
			}
			
			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return importeRecaudadoReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
	public ImporteRecaudadoReport getImporteRecaudadoPlanesReportParamViaDeuda(UserContext userContext, ImporteRecaudadoReport importeRecaudadoReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes
			importeRecaudadoReport.clearErrorMessages();
			
			boolean esViaJudicial = (ViaDeuda.ID_VIA_JUDICIAL == importeRecaudadoReport.getPlan().getViaDeuda().getId() || importeRecaudadoReport.getPlan().getViaDeuda().getId().equals(-1L)) ;
			boolean visualizarComboProcurador = (importeRecaudadoReport.getIdProcuradorSesion() != null || esViaJudicial);
			importeRecaudadoReport.setVisualizarComboProcurador(visualizarComboProcurador);

			if (importeRecaudadoReport.getIdProcuradorSesion() == null){
				if(!visualizarComboProcurador) {
					importeRecaudadoReport.getListProcurador().clear();
					importeRecaudadoReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_NINGUNO));
					// SDselecciono la opcion Ninguno del Procurador
					importeRecaudadoReport.getPlan().getProcurador().setId(-1L);
				} else {
					Recurso recurso = Recurso.getById(importeRecaudadoReport.getPlan().getRecurso().getId());
					
					List<Procurador> listProcurador = Procurador.getListByRecurso(recurso);
					//importeRecaudarReport.getListProcurador().add(new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS));			
					importeRecaudadoReport.setListProcurador((ArrayList<ProcuradorVO>) ListUtilBean.toVO(listProcurador, new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
					importeRecaudadoReport.getPlan().getProcurador().setId(-1L);
				}	
			}

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return importeRecaudadoReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
		// Fin Params Consultar Importe Recaudado de planes
	
	public ImporteRecaudadoReport getImporteRecaudadoPlanesReportResult(UserContext userContext, ImporteRecaudadoReport importeRecaudadoReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		importeRecaudadoReport.setListResult(new ArrayList<PlanVO>());
		importeRecaudadoReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();


			// Validaciones: 
			if(importeRecaudadoReport.getPlan().getRecurso().getId().longValue()<0){
				log.debug("Errores en las validaciones");
				importeRecaudadoReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);				
			}
			
			if(importeRecaudadoReport.getTipoReporte().longValue() == 0){
				if(importeRecaudadoReport.getPlan().getId().longValue()<0){
					log.debug("Errores en las validaciones");
					importeRecaudadoReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_LABEL);				
				}
			}
			
			Date fecPagoDesde = importeRecaudadoReport.getFechaPagoDesde();
			if(fecPagoDesde==null){
				log.debug("Errores en las validaciones");
				importeRecaudadoReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
												GdeError.IMPORTE_RECAUDAR_FECHADESDE_LABEL);				
			}			
			Date fecPagoHasta = importeRecaudadoReport.getFechaPagoHasta();
			if(fecPagoHasta==null){
				log.debug("Errores en las validaciones");
				importeRecaudadoReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, 
												GdeError.IMPORTE_RECAUDAR_FECHAHASTA_LABEL);				
			}
			if(fecPagoDesde!=null && fecPagoHasta!=null && DateUtil.isDateAfter(fecPagoDesde, fecPagoHasta)){
				log.debug("Errores en las validaciones");
				importeRecaudadoReport.addRecoverableError(BaseError.MSG_VALORMENORQUE, 
						GdeError.IMPORTE_RECAUDAR_FECHAHASTA_LABEL, GdeError.IMPORTE_RECAUDAR_FECHADESDE_LABEL);				
			}

			/*
			if(importeRecaudadoReport.isVisualizarComboProcurador()){
				if (ModelUtil.isNullOrEmpty(importeRecaudadoReport.getPlan().getProcurador())){
					importeRecaudadoReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.PROCURADOR_LABEL);
				}
			}
			*/
			
			if(importeRecaudadoReport.hasError()){
				return importeRecaudadoReport;
			}
			
			// Disparar el proceso adp.
			String adpMessage = "La peticion del usuario "+DemodaUtil.currentUserContext().getUserName()
								+" hecha el "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_IMPORTE_RECAUDADO, adpMessage);
			run.create();
			
			String ID_RECURSO_PARAM = "idRecurso";
			String ID_PLAN_PARAM = "idPlan";
			String ID_VIA_DEUDA_PARAM = "idViaDeuda";
			String TIPO_REPORTE_PARAM = "tipoReporte";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
			String ID_PROCURADOR_PARAM = "idProcurador";
			
			String idRecurso = importeRecaudadoReport.getPlan().getRecurso().getId().toString();
			String idPlan = importeRecaudadoReport.getPlan().getId().toString();
			String idViaDeuda = importeRecaudadoReport.getPlan().getViaDeuda().getId().toString();
			String fechaDesde= importeRecaudadoReport.getFechaPagoDesdeView();
			String fechaHasta= importeRecaudadoReport.getFechaPagoHastaView();
			String tipoReporte = importeRecaudadoReport.getTipoReporte().toString();
			String idProcurador = importeRecaudadoReport.getPlan().getProcurador().getId().toString();
			
			// Carga de parametros para adp
			run.putParameter(ID_RECURSO_PARAM, idRecurso);
			run.putParameter(ID_PLAN_PARAM, idPlan);
			run.putParameter(ID_VIA_DEUDA_PARAM, idViaDeuda);
			run.putParameter(TIPO_REPORTE_PARAM, tipoReporte);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			run.putParameter(ID_PROCURADOR_PARAM, idProcurador);
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			importeRecaudadoReport.setProcesando(true);
			importeRecaudadoReport.setDesRunningRun(run.getDesCorrida());
			importeRecaudadoReport.setEstRunningRun(run.getMensajeEstado());
			importeRecaudadoReport.setReporteGenerado(new PlanillaVO());
			importeRecaudadoReport.setExisteReporteGenerado(false);
			importeRecaudadoReport.setError(false);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return importeRecaudadoReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// <--- Consultar Importe Recaudado de planes
	// ---> Reporte Convenio Formalizado
	
	public ConvenioFormReport getConvenioFormReportInit(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ConvenioFormReport convenioFormReport = new ConvenioFormReport();

			// Obtengo el id del procurador de la sesion
			Long idProcuradorSesion = userContext.getIdProcurador();
			convenioFormReport.setIdProcuradorSesion(idProcuradorSesion);
			if(idProcuradorSesion != null){
				// solo podra acceder a los convenios del procurador de la sesion
				Procurador procurador = Procurador.getById(idProcuradorSesion);
				ProcuradorVO procuradorVO = (ProcuradorVO) procurador.toVO(0);
				convenioFormReport.getListProcurador().add(procuradorVO);
				convenioFormReport.getConvenio().setProcurador(procuradorVO);
				convenioFormReport.setVisualizarComboProcurador(true);
			}else{
				// selecciono la opcion Todos del Procurador
				// lista de planes asociadas al recurso
				List<Procurador> listProcurador = Procurador.getListActivos();
				convenioFormReport.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 
						new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				convenioFormReport.getConvenio().getProcurador().setId(-1L);
			}
			
			// lista ViaDeuda, selecciona la opcion todos
			List<ViaDeuda> listViaDeuda = ViaDeuda.getList();
			convenioFormReport.setListViaDeuda((ArrayList<ViaDeudaVO>)ListUtilBean.toVO(listViaDeuda, 
					new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			convenioFormReport.getConvenio().getViaDeuda().setId(-1L);
			
			// Setea la fechaHasta en la fecha del dia
			convenioFormReport.setFechaConvenioHasta(new Date());
			
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_CONVENIO_FORM, DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				convenioFormReport.setProcesando(true);
				convenioFormReport.setDesRunningRun(runningRun.getDesCorrida());
				convenioFormReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				convenioFormReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_CONVENIO_FORM, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					PlanillaVO reporteGenerado = new PlanillaVO();						
					 
					FileCorrida fileCorrida = listFileCorrida.get(0);
					
					reporteGenerado.setTitulo(ultimaCorrida.getDesCorrida());
					reporteGenerado.setFileName(fileCorrida.getFileName().replace('\\' , '/'));
					reporteGenerado.setDescripcion(fileCorrida.getNombre()); 
					reporteGenerado.setCtdResultados(1L);
										
					convenioFormReport.setReporteGenerado(reporteGenerado);
					convenioFormReport.setExisteReporteGenerado(true);
				}else{
					convenioFormReport.setReporteGenerado(new PlanillaVO());
					convenioFormReport.setExisteReporteGenerado(false);
				}
			}else{
				convenioFormReport.setReporteGenerado(new PlanillaVO());
				convenioFormReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_CONVENIO_FORM, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					convenioFormReport.setError(true);
					convenioFormReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					convenioFormReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					convenioFormReport.setError(false);
				}
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return convenioFormReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
		// Params Reporte Convenio Formalizado
	
	public ConvenioFormReport getConvenioFormReportParamViaDeuda(UserContext userContext, ConvenioFormReport convenioFormReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Limpio errores, mensajes
			convenioFormReport.clearErrorMessages();
			
			boolean esViaJudicial = (ViaDeuda.ID_VIA_JUDICIAL == convenioFormReport.getConvenio().getViaDeuda().getId());
			boolean visualizarComboProcurador = (convenioFormReport.getIdProcuradorSesion() != null || esViaJudicial);
			convenioFormReport.setVisualizarComboProcurador(visualizarComboProcurador );
			if(!visualizarComboProcurador){
				// Selecciono la opcion Todos del Procurador
				convenioFormReport.getConvenio().getProcurador().setId(-1L);
			}

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return convenioFormReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

		// Fin Params Reporte Convenio Formalizado
	
	public ConvenioFormReport getConvenioFormReportResult(UserContext userContext, ConvenioFormReport convenioFormReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		convenioFormReport.setListResult(new ArrayList());
		convenioFormReport.setListPlanReport(new ArrayList<PlanReport>());
		convenioFormReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Validaciones: 
			// fecha convenio hasta no sea menor a fecha convenio desde (si se ingresaron)
			Date fecConvDesde = convenioFormReport.getFechaConvenioDesde();
			Date fecConvHasta = convenioFormReport.getFechaConvenioHasta();
			
			if (fecConvDesde == null){
				convenioFormReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONVENIO_REPORT_FECHA_CONVENIO_DESDE);
			}
			
			if (fecConvHasta == null){
				convenioFormReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONVENIO_REPORT_FECHA_CONVENIO_HASTA);
			}
			
			if ( fecConvDesde != null && fecConvHasta != null &&
					DateUtil.isDateAfter(fecConvDesde, fecConvHasta)){
				convenioFormReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.CONVENIO_REPORT_FECHA_CONVENIO_DESDE, 
					GdeError.CONVENIO_REPORT_FECHA_CONVENIO_HASTA);
			}
			
			if ( ModelUtil.isNullOrEmpty(convenioFormReport.getConvenio().getViaDeuda())){
				convenioFormReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.VIADEUDA_LABEL);
				return convenioFormReport;
			}
			
			/*
			if(convenioFormReport.isVisualizarComboProcurador()){
				if (ModelUtil.isNullOrEmpty(convenioFormReport.getConvenio().getProcurador())){
					convenioFormReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.PROCURADOR_LABEL);
				}
			}			
			*/
			
			if(convenioFormReport.hasError()){
				log.debug("Errores en las validaciones");
				return convenioFormReport;
			}
			
			// recarga de la via de la deuda
			/*if(convenioFormReport.getViaDeudaSeleccionada()){
				ViaDeuda viaDeuda = ViaDeuda.getById(convenioFormReport.getConvenio().getViaDeuda().getId());
				convenioFormReport.getConvenio().setViaDeuda((ViaDeudaVO) viaDeuda.toVO(0));
			}*/
			// Disparar el proceso adp.
			String adpMessage = "La peticion del usuario "+DemodaUtil.currentUserContext().getUserName()
								+" hecha el "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_CONVENIO_FORM, adpMessage);
			run.create();
			
			String ID_VIA_DEUDA_PARAM = "idViaDeuda";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
			String PROCURADOR_ID_PARAM = "ProcuradorId";
			
			String idViaDeuda = convenioFormReport.getConvenio().getViaDeuda().getId().toString();
			String fechaDesde= convenioFormReport.getFechaConvenioDesdeView();
			String fechaHasta= convenioFormReport.getFechaConvenioHastaView();
			String idProcurador= convenioFormReport.getConvenio().getProcurador().getId().toString();
			
			// Carga de parametros para adp
			run.putParameter(ID_VIA_DEUDA_PARAM, idViaDeuda);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			run.putParameter(PROCURADOR_ID_PARAM, idProcurador);
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			convenioFormReport.setProcesando(true);
			convenioFormReport.setDesRunningRun(run.getDesCorrida());
			convenioFormReport.setEstRunningRun(run.getMensajeEstado());
			convenioFormReport.setReporteGenerado(new PlanillaVO());
			convenioFormReport.setExisteReporteGenerado(false);
			convenioFormReport.setError(false);
			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return convenioFormReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	// <--- Reporte Convenio Formalizado
	public RespuestaOperativosReport getRespuestaOperativosReportInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			RespuestaOperativosReport respuestaOperativosReport = new RespuestaOperativosReport();

			// Carga la lista de todos los Tipo de Procesos Masivo.
			TipProMas tipProMas = null;
			List<TipProMas> listTipProMas = new ArrayList<TipProMas>();
			tipProMas = TipProMas.getByIdNull(TipProMas.ID_PRE_ENVIO_JUDICIAL);
			if(tipProMas != null)
				listTipProMas.add(tipProMas);
			tipProMas = TipProMas.getByIdNull(TipProMas.ID_RECONFECCION);
			if(tipProMas != null)
				listTipProMas.add(tipProMas);
			respuestaOperativosReport.setListTipProMas((ArrayList<TipProMasVO>) ListUtilBean.toVO(listTipProMas, 
					new TipProMasVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			
			// Carga la lista de todos los Procesos/Corridas.
			List<ProcesoMasivo> listProcesoMasivo = ProcesoMasivo.getListForReporte(null);
			List<ProcesoMasivoVO> listProcesoMasivoVO = new ArrayList<ProcesoMasivoVO>();
			ProcesoMasivoVO proMasSeleccionar = new ProcesoMasivoVO();
			proMasSeleccionar.setCorrida(new CorridaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			proMasSeleccionar.setId(-1L);
			listProcesoMasivoVO.add(proMasSeleccionar);
			listProcesoMasivoVO.addAll((ArrayList<ProcesoMasivoVO>) ListUtilBean.toVO(listProcesoMasivo,1,false));
						
			respuestaOperativosReport.setListProcesoMasivo(listProcesoMasivoVO);
			respuestaOperativosReport.getProcesoMasivo().setId(-1L);
			
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_RESPUESTA_OPERATIVOS,DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				respuestaOperativosReport.setProcesando(true);
				respuestaOperativosReport.setDesRunningRun(runningRun.getDesCorrida());
				respuestaOperativosReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				respuestaOperativosReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_RESPUESTA_OPERATIVOS, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){// && listFileCorrida.size()==1){
					PlanillaVO reporteGenerado = new PlanillaVO();						
					 
					FileCorrida fileCorrida = listFileCorrida.get(0);
					
					reporteGenerado.setTitulo(ultimaCorrida.getDesCorrida());
					reporteGenerado.setFileName(fileCorrida.getFileName().replace('\\' , '/'));
					reporteGenerado.setDescripcion(fileCorrida.getNombre()); //"Reporte de Respuesta Operativos");
					reporteGenerado.setCtdResultados(1L);
										
					respuestaOperativosReport.setReporteGenerado(reporteGenerado);
					respuestaOperativosReport.setExisteReporteGenerado(true);
				}else{
					respuestaOperativosReport.setReporteGenerado(new PlanillaVO());
					respuestaOperativosReport.setExisteReporteGenerado(false);
				}
			}else{
				respuestaOperativosReport.setReporteGenerado(new PlanillaVO());
				respuestaOperativosReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_RESPUESTA_OPERATIVOS, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					respuestaOperativosReport.setError(true);
					respuestaOperativosReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					respuestaOperativosReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					respuestaOperativosReport.setError(false);
				}
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return respuestaOperativosReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RespuestaOperativosReport getRespuestaOperativosReportResult(UserContext userContext, RespuestaOperativosReport respuestaOperativosReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		respuestaOperativosReport.setListResult(new ArrayList<PlanVO>());
		respuestaOperativosReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();


			// Validaciones: 
		
			ProcesoMasivo procesoMasivo = ProcesoMasivo.getByIdNull(respuestaOperativosReport.getProcesoMasivo().getId());
			if(procesoMasivo == null){
				log.debug("Errores en las validaciones");
				respuestaOperativosReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROCESO_MASIVO_LABEL);				
			}

			if(respuestaOperativosReport.hasError()){
				return respuestaOperativosReport;
			}
			
			// Disparar el proceso adp.
			String adpMessage = "La peticion del usuario "+DemodaUtil.currentUserContext().getUserName()
								+" hecha el "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);

			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_RESPUESTA_OPERATIVOS, adpMessage);
			run.create();
			
			String ID_PROCESOMASIVO_PARAM = "idProcesoMasivo";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
			
			String idProcesoMasivo = respuestaOperativosReport.getProcesoMasivo().getId().toString();
			
			// Carga de parametros para adp
			run.putParameter(ID_PROCESOMASIVO_PARAM, idProcesoMasivo);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			respuestaOperativosReport.setProcesando(true);
			respuestaOperativosReport.setDesRunningRun(run.getDesCorrida());
			respuestaOperativosReport.setEstRunningRun(run.getMensajeEstado());
			respuestaOperativosReport.setReporteGenerado(new PlanillaVO());
			respuestaOperativosReport.setExisteReporteGenerado(false);
			respuestaOperativosReport.setError(false);
	
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return respuestaOperativosReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public RespuestaOperativosReport getRespuestaOperativosReportParamTipProMas(UserContext userContext, RespuestaOperativosReport respuestaOperativosReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes
			respuestaOperativosReport.clearErrorMessages();
			TipProMasVO tipProMasVO = respuestaOperativosReport.getTipProMas();
			Long idTipProMas = null;
			if(!ModelUtil.isNullOrEmpty(tipProMasVO)){
				idTipProMas = tipProMasVO.getId();
			}
			// Carga la lista de todos los Procesos/Corridas.
			List<ProcesoMasivo> listProcesoMasivo = ProcesoMasivo.getListForReporte(idTipProMas);
			List<ProcesoMasivoVO> listProcesoMasivoVO = new ArrayList<ProcesoMasivoVO>();
			ProcesoMasivoVO proMasSeleccionar = new ProcesoMasivoVO();
			proMasSeleccionar.setCorrida(new CorridaVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			proMasSeleccionar.setId(-1L);
			listProcesoMasivoVO.add(proMasSeleccionar);
			listProcesoMasivoVO.addAll((ArrayList<ProcesoMasivoVO>) ListUtilBean.toVO(listProcesoMasivo,1,false));				
			respuestaOperativosReport.setListProcesoMasivo(listProcesoMasivoVO);
			respuestaOperativosReport.getProcesoMasivo().setId(-1L);		
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return respuestaOperativosReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ConvenioACaducarReport getConvenioACaducarReportInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ConvenioACaducarReport convenioACaducarReport = new ConvenioACaducarReport();

			// Carga las lista para filtros de busquedas.
			
			// lista de todos los recursos, selecciona la opcion Seleccionar
			List<Recurso> listRecurso = Recurso.getListActivos();
			convenioACaducarReport.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				convenioACaducarReport.getListRecurso().add(item.toVOWithCategoria());							
			}
			convenioACaducarReport.getRecurso().setId(-1L);
			
			// lista vacia de planes, selecciona la opcion Todos
			convenioACaducarReport.getListPlan().add(new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS));
			convenioACaducarReport.getConvenio().getPlan().setId(-1L);
			
			// lista ViaDeuda, selecciona la opcion todos
			List<ViaDeuda> listViaDeuda = ViaDeuda.getList();
			convenioACaducarReport.setListViaDeuda((ArrayList<ViaDeudaVO>)ListUtilBean.toVO(listViaDeuda, 
					new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			convenioACaducarReport.getConvenio().getViaDeuda().setId(-1L);
			
			// carga la lista de todos los Estado Convenio, selecciona la opcion Todos
			List<EstadoConvenio> listEstadoConvenio = new ArrayList<EstadoConvenio>();
			listEstadoConvenio.add(EstadoConvenio.getById(EstadoConvenio.ID_VIGENTE));
			convenioACaducarReport.setListEstadoConvenio((ArrayList<EstadoConvenioVO>)ListUtilBean.toVO(listEstadoConvenio, 
					new EstadoConvenioVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			convenioACaducarReport.getConvenio().getEstadoConvenio().setId(-1L);
			
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_CONVENIO_A_CADUCAR, DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				convenioACaducarReport.setProcesando(true);
				convenioACaducarReport.setDesRunningRun(runningRun.getDesCorrida());
				convenioACaducarReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				convenioACaducarReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_CONVENIO_A_CADUCAR, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					PlanillaVO reporteGenerado = new PlanillaVO();						
					 
					FileCorrida fileCorrida = listFileCorrida.get(0);
					
					reporteGenerado.setTitulo(ultimaCorrida.getDesCorrida());
					reporteGenerado.setFileName(fileCorrida.getFileName().replace('\\' , '/'));
					reporteGenerado.setDescripcion(fileCorrida.getNombre()); //"Reporte de Convenios A Caducar");
					reporteGenerado.setCtdResultados(1L);
										
					convenioACaducarReport.setReporteGenerado(reporteGenerado);
					convenioACaducarReport.setExisteReporteGenerado(true);
				}else{
					convenioACaducarReport.setReporteGenerado(new PlanillaVO());
					convenioACaducarReport.setExisteReporteGenerado(false);
				}
			}else{
				convenioACaducarReport.setReporteGenerado(new PlanillaVO());
				convenioACaducarReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_CONVENIO_A_CADUCAR, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					convenioACaducarReport.setError(true);
					convenioACaducarReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					convenioACaducarReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					convenioACaducarReport.setError(false);
				}
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return convenioACaducarReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ConvenioACaducarReport getConvenioACaducarReportResult(UserContext userContext, ConvenioACaducarReport convenioACaducarReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		convenioACaducarReport.setListResult(new ArrayList<PlanVO>());
		convenioACaducarReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Validaciones: 
			
			//recurso requerido
			if ( ModelUtil.isNullOrEmpty(convenioACaducarReport.getRecurso())){
				convenioACaducarReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONVENIO_A_CADUCAR_REPORT_RECURSO);
				return convenioACaducarReport;
			}
			// Fecha Caducidad Requerido
			if ( convenioACaducarReport.getFechaCaducidad()==null){
				convenioACaducarReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONVENIO_A_CADUCAR_REPORT_FECHA_CADUCIDAD);
				return convenioACaducarReport;
			}
			// fecha convenio hasta no sea menor a fecha convenio desde (si se ingresaron)
			Date fecConvDesde = convenioACaducarReport.getFechaConvenioDesde();
			Date fecConvHasta = convenioACaducarReport.getFechaConvenioHasta();
			
			if ( fecConvDesde != null && fecConvHasta != null &&
					DateUtil.isDateAfter(fecConvDesde, fecConvHasta)){
				convenioACaducarReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.CONVENIO_A_CADUCAR_REPORT_FECHA_CONVENIO_DESDE, 
					GdeError.CONVENIO_A_CADUCAR_REPORT_FECHA_CONVENIO_HASTA);
			}

			// cuota Hasta no sea menor a cuota Desde (si se ingresaron)
			Integer cuotaDesde = convenioACaducarReport.getCuotaDesde();
			Integer cuotaHasta = convenioACaducarReport.getCuotaHasta();

			if ( cuotaDesde != null && cuotaDesde < 1){
				convenioACaducarReport.addRecoverableError(BaseError.MSG_VALORMENORQUE, 
					GdeError.CONVENIO_A_CADUCAR_REPORT_CUOTA_DESDE, "1");
			}

			if ( cuotaHasta != null && cuotaHasta < 1){
				convenioACaducarReport.addRecoverableError(BaseError.MSG_VALORMENORQUE, 
					GdeError.CONVENIO_A_CADUCAR_REPORT_CUOTA_HASTA, "1");
			}

			if ( cuotaDesde != null && cuotaHasta != null &&
					cuotaDesde >= 1 && cuotaHasta >= 1 &&
					cuotaHasta < cuotaDesde){
				convenioACaducarReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.CONVENIO_A_CADUCAR_REPORT_CUOTA_DESDE, 
					GdeError.CONVENIO_A_CADUCAR_REPORT_CUOTA_HASTA);
			}
		
			// importe cuota Hasta no sea menor a importe cuota Desde (si se ingresaron)
			Double importeCuotaDesde = convenioACaducarReport.getImporteCuotaDesde();
			Double importeCuotaHasta = convenioACaducarReport.getImporteCuotaHasta();

			if ( importeCuotaDesde != null && importeCuotaDesde < 1){
				convenioACaducarReport.addRecoverableError(BaseError.MSG_VALORMENORQUE, 
					GdeError.CONVENIO_A_CADUCAR_REPORT_IMPORTE_CUOTA_DESDE, "1");
			}

			if ( importeCuotaHasta != null && importeCuotaHasta < 1){
				convenioACaducarReport.addRecoverableError(BaseError.MSG_VALORMENORQUE, 
					GdeError.CONVENIO_A_CADUCAR_REPORT_IMPORTE_CUOTA_HASTA, "1");
			}

			if ( importeCuotaDesde != null && importeCuotaHasta != null &&
					importeCuotaDesde >= 1 && importeCuotaHasta >= 1 &&
					importeCuotaHasta < importeCuotaDesde){
				convenioACaducarReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
					GdeError.CONVENIO_A_CADUCAR_REPORT_IMPORTE_CUOTA_DESDE, 
					GdeError.CONVENIO_A_CADUCAR_REPORT_IMPORTE_CUOTA_HASTA);
			}
			
			if(convenioACaducarReport.hasError()){
				return convenioACaducarReport;
			}
			
			// Disparar el proceso adp.
			String adpMessage = "La peticion del usuario "+DemodaUtil.currentUserContext().getUserName()
								+" hecha el "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);

			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_CONVENIO_A_CADUCAR, adpMessage);
			run.create();
			
			String ID_RECURSO_PARAM = "idRecurso";
			String ID_PLAN_PARAM = "idPlan";
			String ID_VIA_DEUDA_PARAM = "idViaDeuda";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String FECHA_CADUCIDAD_PARAM = "fechaCaducidad";
			String CUOTA_DESDE_PARAM = "cuotaDesde";
			String CUOTA_HASTA_PARAM = "cuotaHasta";
			String IMP_CUOTA_DESDE_PARAM = "importeCuotaDesde";
			String IMP_CUOTA_HASTA_PARAM = "importeCuotaHasta";
			String ID_ESTADO_CONVENIO_PARAM = "idEstadoConvenio";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
			
			String idRecurso = convenioACaducarReport.getRecurso().getId().toString();
			String idPlan = convenioACaducarReport.getConvenio().getPlan().getId().toString();
			String idViaDeuda = convenioACaducarReport.getConvenio().getViaDeuda().getId().toString();
			String fechaDesde= convenioACaducarReport.getFechaConvenioDesdeView();
			String fechaHasta= convenioACaducarReport.getFechaConvenioHastaView();
			String fechaCaducidad= convenioACaducarReport.getFechaCaducidadView();
			String cuotaDesdeView= convenioACaducarReport.getCuotaDesdeView();
			String cuotaHastaView= convenioACaducarReport.getCuotaHastaView();
			String importeCuotaDesdeView= convenioACaducarReport.getImporteCuotaDesdeView();
			String importeCuotaHastaView= convenioACaducarReport.getImporteCuotaHastaView();
			String idEstadoConvenio= convenioACaducarReport.getConvenio().getEstadoConvenio().getId().toString();
			
			// Carga de parametros para adp
			run.putParameter(ID_PLAN_PARAM, idPlan);
			run.putParameter(ID_RECURSO_PARAM, idRecurso);
			run.putParameter(ID_VIA_DEUDA_PARAM, idViaDeuda);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(FECHA_CADUCIDAD_PARAM, fechaCaducidad);
			run.putParameter(CUOTA_DESDE_PARAM, cuotaDesdeView);
			run.putParameter(CUOTA_HASTA_PARAM, cuotaHastaView);
			run.putParameter(IMP_CUOTA_DESDE_PARAM, importeCuotaDesdeView);
			run.putParameter(IMP_CUOTA_HASTA_PARAM, importeCuotaHastaView);
			run.putParameter(ID_ESTADO_CONVENIO_PARAM, idEstadoConvenio);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			convenioACaducarReport.setProcesando(true);
			convenioACaducarReport.setDesRunningRun(run.getDesCorrida());
			convenioACaducarReport.setEstRunningRun(run.getMensajeEstado());
			convenioACaducarReport.setReporteGenerado(new PlanillaVO());
			convenioACaducarReport.setExisteReporteGenerado(false);
			convenioACaducarReport.setError(false);
	
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return convenioACaducarReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ConvenioACaducarReport getConvenioACaducarReportParamRecurso(UserContext userContext, ConvenioACaducarReport convenioACaducarReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes
			convenioACaducarReport.clearErrorMessages();
			
			// si el recurso no esta seleccionado
			if (ModelUtil.isNullOrEmpty(convenioACaducarReport.getRecurso())){
				
				// lista vacia de planes, selecciona la opcion Todos
				convenioACaducarReport.setListPlan(new ArrayList<PlanVO>());
				convenioACaducarReport.getListPlan().add(new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS));
				convenioACaducarReport.getConvenio().getPlan().setId(-1L);
				
				// selecciono la opcion Todos del plan
				convenioACaducarReport.getConvenio().getPlan().setId(-1L);

				// selecciono la opcion Todos de la ViaDeuda
				convenioACaducarReport.getConvenio().getViaDeuda().setId(-1L);
								
			}else{
				// si el recurso esta seleccionado, obtencion del recurso
				Recurso recurso = Recurso.getById(convenioACaducarReport.getRecurso().getId());
				
				// lista de planes asociadas al recurso
				List<Plan> listPlan = Plan.getListByIdRecurso(recurso.getId());
				convenioACaducarReport.setListPlan((ArrayList<PlanVO>)ListUtilBean.toVO(listPlan, 
						new PlanVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				convenioACaducarReport.getConvenio().getPlan().setId(-1L);
				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return convenioACaducarReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public ConvenioACaducarReport getConvenioACaducarReportParamPlan(UserContext userContext, ConvenioACaducarReport convenioACaducarReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes
			convenioACaducarReport.clearErrorMessages();
			
			// si el plan esta seleccionado 
			if (!ModelUtil.isNullOrEmpty(convenioACaducarReport.getConvenio().getPlan())){
				// selecciono la opcion Todos de la ViaDeuda
				convenioACaducarReport.getConvenio().getViaDeuda().setId(-1L);
			}
				
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return convenioACaducarReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}

	}

	
	// ---> Reporte de Anulacion de Deuda
	public DeudaAnuladaReport getDeudaAnuladaReportInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			DeudaAnuladaReport deudaAnuladaReport = new DeudaAnuladaReport();

			// Carga las lista para filtros de busquedas.
			
			// lista de todos los recursos, selecciona la opcion Seleccionar
			List<Recurso> listRecurso = Recurso.getListActivos();
			deudaAnuladaReport.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				deudaAnuladaReport.getListRecurso().add(item.toVOWithCategoria());							
			}
			deudaAnuladaReport.getRecurso().setId(-1L);
			
		
			// lista ViaDeuda, selecciona la opcion todos
			List<ViaDeuda> listViaDeuda = ViaDeuda.getList();
			deudaAnuladaReport.setListViaDeuda((ArrayList<ViaDeudaVO>)ListUtilBean.toVO(listViaDeuda, 
					new ViaDeudaVO(-1, StringUtil.SELECT_OPCION_TODOS)));
			deudaAnuladaReport.getViaDeuda().setId(-1L);
			
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_DEUDA_ANULADA, DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				deudaAnuladaReport.setProcesando(true);
				deudaAnuladaReport.setDesRunningRun(runningRun.getDesCorrida());
				deudaAnuladaReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				deudaAnuladaReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_DEUDA_ANULADA, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					PlanillaVO reporteGenerado = new PlanillaVO();						
					 
					FileCorrida fileCorrida = listFileCorrida.get(0);
					
					reporteGenerado.setTitulo(ultimaCorrida.getDesCorrida());
					reporteGenerado.setFileName(fileCorrida.getFileName().replace('\\' , '/'));
					reporteGenerado.setDescripcion(fileCorrida.getNombre()); //"Reporte de Convenios A Caducar");
					reporteGenerado.setCtdResultados(1L);
										
					deudaAnuladaReport.setReporteGenerado(reporteGenerado);
					deudaAnuladaReport.setExisteReporteGenerado(true);
				}else{
					deudaAnuladaReport.setReporteGenerado(new PlanillaVO());
					deudaAnuladaReport.setExisteReporteGenerado(false);
				}
			}else{
				deudaAnuladaReport.setReporteGenerado(new PlanillaVO());
				deudaAnuladaReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_DEUDA_ANULADA, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					deudaAnuladaReport.setError(true);
					deudaAnuladaReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					deudaAnuladaReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					deudaAnuladaReport.setError(false);
				}
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaAnuladaReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public DeudaAnuladaReport getDeudaAnuladaReportResult(UserContext userContext, DeudaAnuladaReport deudaAnuladaReport)
			throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		deudaAnuladaReport.setListResult(new ArrayList());
		deudaAnuladaReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Validaciones:
			
			// Recurso
			//if(ModelUtil.isNullOrEmpty(deudaAnuladaReport.getRecurso()))
			//	deudaAnuladaReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
			
			// Via Deuda
			
			// fecha hasta no sea menor a fecha desde
			Date fechaDesde = deudaAnuladaReport.getFechaDesde();
			Date fechaHasta = deudaAnuladaReport.getFechaHasta();
			
			if(fechaDesde==null)
				deudaAnuladaReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDAANULADA_REPORT_FECHA_DESDE);

			if(fechaHasta==null)
				deudaAnuladaReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDAANULADA_REPORT_FECHA_HASTA);
			
			if (fechaDesde!=null && fechaHasta!=null && DateUtil.isDateAfter(fechaDesde, fechaHasta)){
				deudaAnuladaReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
						GdeError.DEUDAANULADA_REPORT_FECHA_DESDE,GdeError.DEUDAANULADA_REPORT_FECHA_HASTA);
			}
						
			if(deudaAnuladaReport.hasError())
				return deudaAnuladaReport;
			

			// Disparar el proceso adp.
			String adpMessage = "La peticion del usuario "+DemodaUtil.currentUserContext().getUserName()
								+" hecha el "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);

			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_DEUDA_ANULADA, adpMessage);
			run.create();

			
			String ID_RECURSO_PARAM = "idRecurso";
			String ID_VIADEUDA_PARAM = "idViaDeuda";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";			
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
									
			String idRecurso = deudaAnuladaReport.getRecurso().getId()==null?"":deudaAnuladaReport.getRecurso().getId().toString();
			String idViaDeuda = deudaAnuladaReport.getViaDeuda().getId()==null?"":deudaAnuladaReport.getViaDeuda().getIdView();
			String fechaAnuDesde= deudaAnuladaReport.getFechaDesdeView();
			String fechaAnuHasta= deudaAnuladaReport.getFechaHastaView();
			
			// Carga de parametros para adp
			run.putParameter(ID_RECURSO_PARAM, idRecurso);
			run.putParameter(ID_VIADEUDA_PARAM, idViaDeuda);
			run.putParameter(FECHA_DESDE_PARAM, fechaAnuDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaAnuHasta);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			deudaAnuladaReport.setProcesando(true);
			deudaAnuladaReport.setDesRunningRun(run.getDesCorrida());
			deudaAnuladaReport.setEstRunningRun(run.getMensajeEstado());
			deudaAnuladaReport.setReporteGenerado(new PlanillaVO());
			deudaAnuladaReport.setExisteReporteGenerado(false);
			deudaAnuladaReport.setError(false);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaAnuladaReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Reporte de Anulacion de Deuda	
	
	// ---> Reporte de totales de emision
	public EmisionReport getEmisionReportInit(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			EmisionReport emisionReport = new EmisionReport();
			
			// carga de lista de todos los recursos, selecciona la opcion Todos
			List<Recurso> listRecurso = Recurso.getListActivos();
			emisionReport.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				emisionReport.getListRecurso().add(item.toVOWithCategoria());							
			}
			emisionReport.getRecurso().setId(-1L);
			
			emisionReport.getListRecClaDeu().add(new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			emisionReport.getRecClaDeu().setId(-1L);
			
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_EMISION, DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				emisionReport.setProcesando(true);
				emisionReport.setDesRunningRun(runningRun.getDesCorrida());
				emisionReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				emisionReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_EMISION, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					PlanillaVO reporteGenerado = new PlanillaVO();						
					 
					FileCorrida fileCorrida = listFileCorrida.get(0);
					
					reporteGenerado.setTitulo(ultimaCorrida.getDesCorrida());
					reporteGenerado.setFileName(fileCorrida.getFileName().replace('\\' , '/'));
					reporteGenerado.setDescripcion(fileCorrida.getNombre()); 
					reporteGenerado.setCtdResultados(1L);
										
					emisionReport.setReporteGenerado(reporteGenerado);
					emisionReport.setExisteReporteGenerado(true);
				}else{
					emisionReport.setReporteGenerado(new PlanillaVO());
					emisionReport.setExisteReporteGenerado(false);
				}
			}else{
				emisionReport.setReporteGenerado(new PlanillaVO());
				emisionReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_EMISION, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					emisionReport.setError(true);
					emisionReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					emisionReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					emisionReport.setError(false);
				}
			}
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return emisionReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public EmisionReport getEmisionReportReportParamRecurso(UserContext userContext, EmisionReport emisionReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// limpio errores, mensajes
			emisionReport.clearErrorMessages();
			
			// si el recurso no esta seleccionado
			if (ModelUtil.isNullOrEmpty(emisionReport.getRecurso())){
				
				// lista vacia de RecClaDeu, selecciona la opcion Todos
				emisionReport.setListRecClaDeu(new ArrayList<RecClaDeuVO>());
				emisionReport.getListRecClaDeu().add(new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
								
			}else{
				// si el recurso esta seleccionado, obtencion del recurso
				Recurso recurso = Recurso.getById(emisionReport.getRecurso().getId());
				
				// lista de RecClaDeu asociadas al recurso
				List<RecClaDeu> listRecClaDeu = RecClaDeu.getListByIdRecurso(recurso.getId());
				emisionReport.setListRecClaDeu((ArrayList<RecClaDeuVO>)ListUtilBean.toVO(listRecClaDeu, 
						new RecClaDeuVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
				emisionReport.getRecClaDeu().setId(-1L);
				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return emisionReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}

	public EmisionReport getEmisionReportResult(UserContext userContext, EmisionReport emisionReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		emisionReport.setListResult(new ArrayList());
		emisionReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Validaciones:
			
			// Recurso
			if(ModelUtil.isNullOrEmpty(emisionReport.getRecurso()))
				emisionReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
						
			// fecha hasta no sea menor a fecha desde
			Date fecEmiDesde = emisionReport.getFechaEmiDesde();
			Date fecEmiHasta = emisionReport.getFechaEmiHasta();
			
			if(fecEmiDesde==null)
				emisionReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.EMISION_REPORT_FECHA_EMI_DESDE);

			if(fecEmiHasta==null)
				emisionReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.EMISION_REPORT_FECHA_EMI_HASTA);
			
			if (fecEmiDesde!=null && fecEmiHasta!=null && DateUtil.isDateAfter(fecEmiDesde, fecEmiHasta)){
				emisionReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
						GdeError.EMISION_REPORT_FECHA_EMI_DESDE,GdeError.EMISION_REPORT_FECHA_EMI_HASTA);
			}
						
			if(emisionReport.hasError())
				return emisionReport;
			
			// Disparar el proceso adp.
			String adpMessage = "Reporte Emision - fecha: "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_EMISION, adpMessage);
			run.create();
			
			String ID_RECURSO_PARAM = "idRecurso";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String ID_RECCLADEU_PARAM = "idRecClaDeu";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
									
			String idRecurso = emisionReport.getRecurso().getId().toString();
			String fechaDesde= emisionReport.getFechaEmiDesdeView();
			String fechaHasta= emisionReport.getFechaEmiHastaView();
			String idRecClaDeu = emisionReport.getRecClaDeu().getIdView();
			
			// Carga de parametros para adp
			run.putParameter(ID_RECURSO_PARAM, idRecurso);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(ID_RECCLADEU_PARAM, idRecClaDeu);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			emisionReport.setProcesando(true);
			emisionReport.setDesRunningRun(run.getDesCorrida());
			emisionReport.setEstRunningRun(run.getMensajeEstado());
			emisionReport.setReporteGenerado(new PlanillaVO());
			emisionReport.setExisteReporteGenerado(false);
			emisionReport.setError(false);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return emisionReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Reporte de totales de emision
	
	
	
	
	// ---> Reporte total Contribuyente CER
	public ContribuyenteCerReport getContribuyenteCerReportInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ContribuyenteCerReport contribuyenteCerReport = new ContribuyenteCerReport();
			
			// carga de lista de todos los recursos, selecciona la opcion Todos
			List<Recurso> listRecurso = Recurso.getListActivos();
		
			contribuyenteCerReport.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				contribuyenteCerReport.getListRecurso().add(item.toVOWithCategoria());							
			}
			contribuyenteCerReport.getRecurso().setId(-1L);
					
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_CONTRIBUYENTECER, DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				contribuyenteCerReport.setProcesando(true);
				contribuyenteCerReport.setDesRunningRun(runningRun.getDesCorrida());
				contribuyenteCerReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				contribuyenteCerReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_CONTRIBUYENTECER, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					PlanillaVO reporteGenerado = new PlanillaVO();						
					 
					FileCorrida fileCorrida = listFileCorrida.get(0);
					
					reporteGenerado.setTitulo(ultimaCorrida.getDesCorrida());
					reporteGenerado.setFileName(fileCorrida.getFileName().replace('\\' , '/'));
					reporteGenerado.setDescripcion(fileCorrida.getNombre()); 
					reporteGenerado.setCtdResultados(1L);
										
					contribuyenteCerReport.setReporteGenerado(reporteGenerado);
					contribuyenteCerReport.setExisteReporteGenerado(true);
				}else{
					log.debug("listFileCorrida vacia");
					contribuyenteCerReport.setReporteGenerado(new PlanillaVO());
					contribuyenteCerReport.setExisteReporteGenerado(false);
				}
			}else{
				log.debug("No existe lastEndOkRun");
				contribuyenteCerReport.setReporteGenerado(new PlanillaVO());
				contribuyenteCerReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_CONTRIBUYENTECER, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					contribuyenteCerReport.setError(true);
					contribuyenteCerReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					contribuyenteCerReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					contribuyenteCerReport.setError(false);
				}
			}
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return contribuyenteCerReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	

	public ContribuyenteCerReport getContribuyenteCerReportResult(UserContext userContext, ContribuyenteCerReport contribuyenteCerReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		contribuyenteCerReport.setListResult(new ArrayList());
		contribuyenteCerReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Validaciones:
			if(contribuyenteCerReport.getFechaReporte()==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_FECHA_REPORTE);
					
			// fecha hasta no sea menor a fecha desde
			Date fechaPagoDesde = contribuyenteCerReport.getFechaDesde();
			Date fechaPagoHasta = contribuyenteCerReport.getFechaHasta();
			

			if(contribuyenteCerReport.getAnioDesde()==null || contribuyenteCerReport.getPeriodoDesde()==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_PERIODO_DESDE);

			if(contribuyenteCerReport.getAnioHasta()==null || contribuyenteCerReport.getPeriodoHasta()==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_PERIODO_HASTA);

			if(fechaPagoDesde==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_FECHA_PAGO_DESDE);

			if(fechaPagoHasta==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_FECHA_PAGO_HASTA);
			
			if (fechaPagoDesde!=null && fechaPagoHasta!=null && DateUtil.isDateAfter(fechaPagoDesde, fechaPagoHasta)){
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
						GdeError.CONTRIBUYENTECER_REPORT_FECHA_PAGO_DESDE,GdeError.CONTRIBUYENTECER_REPORT_FECHA_PAGO_HASTA);
			}
						
			if(contribuyenteCerReport.hasError())
				return contribuyenteCerReport;
			
			// Disparar el proceso adp.
			String adpMessage = "Reporte Contribuyente Cer - fecha: "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_CONTRIBUYENTECER, adpMessage);
			run.create();
			
			String FECHA_REPORTE_PARAM = "fechaReporte";
			String ID_RECURSO_PARAM = "idRecurso";
			String PERIODO_DESDE_PARAM = "periodoDesde";
			String PERIODO_HASTA_PARAM = "periodoHasta";
			String ANIO_DESDE_PARAM = "anioDesde";
			String ANIO_HASTA_PARAM = "anioHasta";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
			
			String fechaReporte= contribuyenteCerReport.getFechaReporteView();
			if( contribuyenteCerReport.getRecurso()!=null){
			String idRecurso = contribuyenteCerReport.getRecurso().getId().toString();
			run.putParameter(ID_RECURSO_PARAM, idRecurso);
			}
			String fechaDesde= contribuyenteCerReport.getFechaDesdeView();
			String fechaHasta= contribuyenteCerReport.getFechaHastaView();
			String periodoDesde= contribuyenteCerReport.getPeriodoDesdeView();
			String periodoHasta= contribuyenteCerReport.getPeriodoHastaView();
			String anioDesde= contribuyenteCerReport.getAnioDesdeView();
			String anioHasta= contribuyenteCerReport.getAnioHastaView();
		
			// Carga de parametros para adp
			run.putParameter(FECHA_REPORTE_PARAM, fechaReporte);
			
			run.putParameter(PERIODO_DESDE_PARAM, periodoDesde);
			run.putParameter(PERIODO_HASTA_PARAM, periodoHasta);
			run.putParameter(ANIO_DESDE_PARAM, anioDesde);
			run.putParameter(ANIO_HASTA_PARAM, anioHasta);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			contribuyenteCerReport.setProcesando(true);
			contribuyenteCerReport.setDesRunningRun(run.getDesCorrida());
			contribuyenteCerReport.setEstRunningRun(run.getMensajeEstado());
			contribuyenteCerReport.setReporteGenerado(new PlanillaVO());
			contribuyenteCerReport.setExisteReporteGenerado(false);
			contribuyenteCerReport.setError(false);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return contribuyenteCerReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	// ---> Reporte total Contribuyente CER	

	// ---> Reporte total Contribuyente CER X Recurso
	public ContribuyenteCerReport getRecConCerReportInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ContribuyenteCerReport contribuyenteCerReport = new ContribuyenteCerReport();
			
			// carga de lista de todos los recursos, selecciona la opcion Todos
			List<Recurso> listRecurso = Recurso.getListActivos();
		
			contribuyenteCerReport.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				contribuyenteCerReport.getListRecurso().add(item.toVOWithCategoria());							
			}
			contribuyenteCerReport.getRecurso().setId(-1L);
					
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_RECCONCER, DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				contribuyenteCerReport.setProcesando(true);
				contribuyenteCerReport.setDesRunningRun(runningRun.getDesCorrida());
				contribuyenteCerReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				contribuyenteCerReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_RECCONCER, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					PlanillaVO reporteGenerado = new PlanillaVO();						
					 
					FileCorrida fileCorrida = listFileCorrida.get(0);
					
					reporteGenerado.setTitulo(ultimaCorrida.getDesCorrida());
					reporteGenerado.setFileName(fileCorrida.getFileName().replace('\\' , '/'));
					reporteGenerado.setDescripcion(fileCorrida.getNombre()); 
					reporteGenerado.setCtdResultados(1L);
										
					contribuyenteCerReport.setReporteGenerado(reporteGenerado);
					contribuyenteCerReport.setExisteReporteGenerado(true);
				}else{
					log.debug("listFileCorrida vacia");
					contribuyenteCerReport.setReporteGenerado(new PlanillaVO());
					contribuyenteCerReport.setExisteReporteGenerado(false);
				}
			}else{
				log.debug("No existe lastEndOkRun");
				contribuyenteCerReport.setReporteGenerado(new PlanillaVO());
				contribuyenteCerReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_RECCONCER, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					contribuyenteCerReport.setError(true);
					contribuyenteCerReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					contribuyenteCerReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					contribuyenteCerReport.setError(false);
				}
			}
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return contribuyenteCerReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	

	public ContribuyenteCerReport getRecConCerReportResult(UserContext userContext, ContribuyenteCerReport contribuyenteCerReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		contribuyenteCerReport.setListResult(new ArrayList());
		contribuyenteCerReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Validaciones:
			if(contribuyenteCerReport.getFechaReporte()==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_FECHA_REPORTE);
					
			// fecha hasta no sea menor a fecha desde
			Date fechaPagoDesde = contribuyenteCerReport.getFechaDesde();
			Date fechaPagoHasta = contribuyenteCerReport.getFechaHasta();
			

			if(contribuyenteCerReport.getAnioDesde()==null || contribuyenteCerReport.getPeriodoDesde()==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_PERIODO_DESDE);

			if(contribuyenteCerReport.getAnioHasta()==null || contribuyenteCerReport.getPeriodoHasta()==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_PERIODO_HASTA);

			if(fechaPagoDesde==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_FECHA_PAGO_DESDE);

			if(fechaPagoHasta==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_FECHA_PAGO_HASTA);
			
			if (fechaPagoDesde!=null && fechaPagoHasta!=null && DateUtil.isDateAfter(fechaPagoDesde, fechaPagoHasta)){
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
						GdeError.CONTRIBUYENTECER_REPORT_FECHA_PAGO_DESDE,GdeError.CONTRIBUYENTECER_REPORT_FECHA_PAGO_HASTA);
			}
						
			if(contribuyenteCerReport.hasError())
				return contribuyenteCerReport;
			
			// Disparar el proceso adp.
			String adpMessage = "Reporte Contribuyente Cer X Recurso - fecha: "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_RECCONCER, adpMessage);
			run.create();
			
			String FECHA_REPORTE_PARAM = "fechaReporte";
			String ID_RECURSO_PARAM = "idRecurso";
			String PERIODO_DESDE_PARAM = "periodoDesde";
			String PERIODO_HASTA_PARAM = "periodoHasta";
			String ANIO_DESDE_PARAM = "anioDesde";
			String ANIO_HASTA_PARAM = "anioHasta";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
			
			String fechaReporte= contribuyenteCerReport.getFechaReporteView();
			if( contribuyenteCerReport.getRecurso()!=null){
			String idRecurso = contribuyenteCerReport.getRecurso().getId().toString();
			run.putParameter(ID_RECURSO_PARAM, idRecurso);
			}
			String fechaDesde= contribuyenteCerReport.getFechaDesdeView();
			String fechaHasta= contribuyenteCerReport.getFechaHastaView();
			String periodoDesde= contribuyenteCerReport.getPeriodoDesdeView();
			String periodoHasta= contribuyenteCerReport.getPeriodoHastaView();
			String anioDesde= contribuyenteCerReport.getAnioDesdeView();
			String anioHasta= contribuyenteCerReport.getAnioHastaView();
		
			// Carga de parametros para adp
			run.putParameter(FECHA_REPORTE_PARAM, fechaReporte);
			
			run.putParameter(PERIODO_DESDE_PARAM, periodoDesde);
			run.putParameter(PERIODO_HASTA_PARAM, periodoHasta);
			run.putParameter(ANIO_DESDE_PARAM, anioDesde);
			run.putParameter(ANIO_HASTA_PARAM, anioHasta);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			contribuyenteCerReport.setProcesando(true);
			contribuyenteCerReport.setDesRunningRun(run.getDesCorrida());
			contribuyenteCerReport.setEstRunningRun(run.getMensajeEstado());
			contribuyenteCerReport.setReporteGenerado(new PlanillaVO());
			contribuyenteCerReport.setExisteReporteGenerado(false);
			contribuyenteCerReport.setError(false);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return contribuyenteCerReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	// ---> Reporte total Contribuyente CER	X Recurso
	
	// ---> Reporte total Recaudación CER X Sector
	public ContribuyenteCerReport getRecaudacionCerReportInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ContribuyenteCerReport contribuyenteCerReport = new ContribuyenteCerReport();
			
			// carga de lista de todos los recursos, selecciona la opcion Todos
			List<Recurso> listRecurso = Recurso.getListActivos();
		
			contribuyenteCerReport.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				contribuyenteCerReport.getListRecurso().add(item.toVOWithCategoria());							
			}
			contribuyenteCerReport.getRecurso().setId(-1L);
					
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_RECAUDACIONCER, DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				contribuyenteCerReport.setProcesando(true);
				contribuyenteCerReport.setDesRunningRun(runningRun.getDesCorrida());
				contribuyenteCerReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				contribuyenteCerReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_RECAUDACIONCER, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					PlanillaVO reporteGenerado = new PlanillaVO();						
					 
					FileCorrida fileCorrida = listFileCorrida.get(0);
					
					reporteGenerado.setTitulo(ultimaCorrida.getDesCorrida());
					reporteGenerado.setFileName(fileCorrida.getFileName().replace('\\' , '/'));
					reporteGenerado.setDescripcion(fileCorrida.getNombre()); 
					reporteGenerado.setCtdResultados(1L);
										
					contribuyenteCerReport.setReporteGenerado(reporteGenerado);
					contribuyenteCerReport.setExisteReporteGenerado(true);
				}else{
					log.debug("listFileCorrida vacia");
					contribuyenteCerReport.setReporteGenerado(new PlanillaVO());
					contribuyenteCerReport.setExisteReporteGenerado(false);
				}
			}else{
				log.debug("No existe lastEndOkRun");
				contribuyenteCerReport.setReporteGenerado(new PlanillaVO());
				contribuyenteCerReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_RECAUDACIONCER, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					contribuyenteCerReport.setError(true);
					contribuyenteCerReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					contribuyenteCerReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					contribuyenteCerReport.setError(false);
				}
			}
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return contribuyenteCerReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ContribuyenteCerReport getRecaudacionCerReportResult(UserContext userContext, ContribuyenteCerReport contribuyenteCerReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		contribuyenteCerReport.setListResult(new ArrayList());
		contribuyenteCerReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Validaciones:
			if(contribuyenteCerReport.getFechaReporte()==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_FECHA_REPORTE);
					
			// fecha hasta no sea menor a fecha desde
			Date fechaPagoDesde = contribuyenteCerReport.getFechaDesde();
			Date fechaPagoHasta = contribuyenteCerReport.getFechaHasta();
			

			if(contribuyenteCerReport.getAnioDesde()==null || contribuyenteCerReport.getPeriodoDesde()==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_PERIODO_DESDE);

			if(contribuyenteCerReport.getAnioHasta()==null || contribuyenteCerReport.getPeriodoHasta()==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_PERIODO_HASTA);

			if(fechaPagoDesde==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_FECHA_PAGO_DESDE);

			if(fechaPagoHasta==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_FECHA_PAGO_HASTA);
			
			if (fechaPagoDesde!=null && fechaPagoHasta!=null && DateUtil.isDateAfter(fechaPagoDesde, fechaPagoHasta)){
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
						GdeError.CONTRIBUYENTECER_REPORT_FECHA_PAGO_DESDE,GdeError.CONTRIBUYENTECER_REPORT_FECHA_PAGO_HASTA);
			}
						
			if(contribuyenteCerReport.hasError())
				return contribuyenteCerReport;
			
			// Disparar el proceso adp.
			String adpMessage = "Reporte Total Recaudación Cer - fecha: "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_RECAUDACIONCER, adpMessage);
			run.create();
			
			String FECHA_REPORTE_PARAM = "fechaReporte";
			String ID_RECURSO_PARAM = "idRecurso";
			String PERIODO_DESDE_PARAM = "periodoDesde";
			String PERIODO_HASTA_PARAM = "periodoHasta";
			String ANIO_DESDE_PARAM = "anioDesde";
			String ANIO_HASTA_PARAM = "anioHasta";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
			
			String fechaReporte= contribuyenteCerReport.getFechaReporteView();
			if( contribuyenteCerReport.getRecurso()!=null){
			String idRecurso = contribuyenteCerReport.getRecurso().getId().toString();
			run.putParameter(ID_RECURSO_PARAM, idRecurso);
			}
			String fechaDesde= contribuyenteCerReport.getFechaDesdeView();
			String fechaHasta= contribuyenteCerReport.getFechaHastaView();
			String periodoDesde= contribuyenteCerReport.getPeriodoDesdeView();
			String periodoHasta= contribuyenteCerReport.getPeriodoHastaView();
			String anioDesde= contribuyenteCerReport.getAnioDesdeView();
			String anioHasta= contribuyenteCerReport.getAnioHastaView();
		
			// Carga de parametros para adp
			run.putParameter(FECHA_REPORTE_PARAM, fechaReporte);
			
			run.putParameter(PERIODO_DESDE_PARAM, periodoDesde);
			run.putParameter(PERIODO_HASTA_PARAM, periodoHasta);
			run.putParameter(ANIO_DESDE_PARAM, anioDesde);
			run.putParameter(ANIO_HASTA_PARAM, anioHasta);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			contribuyenteCerReport.setProcesando(true);
			contribuyenteCerReport.setDesRunningRun(run.getDesCorrida());
			contribuyenteCerReport.setEstRunningRun(run.getMensajeEstado());
			contribuyenteCerReport.setReporteGenerado(new PlanillaVO());
			contribuyenteCerReport.setExisteReporteGenerado(false);
			contribuyenteCerReport.setError(false);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return contribuyenteCerReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	
	// --->Reporte total detallado Recaudación CER	
	public ContribuyenteCerReport getDetRecaudacionCerReportInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			ContribuyenteCerReport contribuyenteCerReport = new ContribuyenteCerReport();
			
			// carga de lista de todos los recursos, selecciona la opcion Todos
			List<Recurso> listRecurso = Recurso.getListActivos();
		
			contribuyenteCerReport.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Recurso item: listRecurso){				
				contribuyenteCerReport.getListRecurso().add(item.toVOWithCategoria());							
			}
			contribuyenteCerReport.getRecurso().setId(-1L);
					
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_DETRECAUDACIONCER, DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				contribuyenteCerReport.setProcesando(true);
				contribuyenteCerReport.setDesRunningRun(runningRun.getDesCorrida());
				contribuyenteCerReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				contribuyenteCerReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_DETRECAUDACIONCER, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					PlanillaVO reporteGenerado = new PlanillaVO();						
					 
					FileCorrida fileCorrida = listFileCorrida.get(0);
					
					reporteGenerado.setTitulo(ultimaCorrida.getDesCorrida());
					reporteGenerado.setFileName(fileCorrida.getFileName().replace('\\' , '/'));
					reporteGenerado.setDescripcion(fileCorrida.getNombre()); 
					reporteGenerado.setCtdResultados(1L);
										
					contribuyenteCerReport.setReporteGenerado(reporteGenerado);
					contribuyenteCerReport.setExisteReporteGenerado(true);
				}else{
					log.debug("listFileCorrida vacia");
					contribuyenteCerReport.setReporteGenerado(new PlanillaVO());
					contribuyenteCerReport.setExisteReporteGenerado(false);
				}
			}else{
				log.debug("No existe lastEndOkRun");
				contribuyenteCerReport.setReporteGenerado(new PlanillaVO());
				contribuyenteCerReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_DETRECAUDACIONCER, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					contribuyenteCerReport.setError(true);
					contribuyenteCerReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					contribuyenteCerReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					contribuyenteCerReport.setError(false);
				}
			}
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return contribuyenteCerReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public ContribuyenteCerReport getDetRecaudacionCerReportResult(UserContext userContext, ContribuyenteCerReport contribuyenteCerReport) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		contribuyenteCerReport.setListResult(new ArrayList());
		contribuyenteCerReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Validaciones:
			if(contribuyenteCerReport.getFechaReporte()==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_FECHA_REPORTE);
					
			// fecha hasta no sea menor a fecha desde
			Date fechaPagoDesde = contribuyenteCerReport.getFechaDesde();
			Date fechaPagoHasta = contribuyenteCerReport.getFechaHasta();
			

			if(contribuyenteCerReport.getAnioDesde()==null || contribuyenteCerReport.getPeriodoDesde()==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_PERIODO_DESDE);

			if(contribuyenteCerReport.getAnioHasta()==null || contribuyenteCerReport.getPeriodoHasta()==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_PERIODO_HASTA);

			if(fechaPagoDesde==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_FECHA_PAGO_DESDE);

			if(fechaPagoHasta==null)
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONTRIBUYENTECER_REPORT_FECHA_PAGO_HASTA);
			
			if (fechaPagoDesde!=null && fechaPagoHasta!=null && DateUtil.isDateAfter(fechaPagoDesde, fechaPagoHasta)){
				contribuyenteCerReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
						GdeError.CONTRIBUYENTECER_REPORT_FECHA_PAGO_DESDE,GdeError.CONTRIBUYENTECER_REPORT_FECHA_PAGO_HASTA);
			}
						
			if(contribuyenteCerReport.hasError())
				return contribuyenteCerReport;
			
			// Disparar el proceso adp.
			String adpMessage = "Reporte Total Recaudación Cer - fecha: "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_DETRECAUDACIONCER, adpMessage);
			run.create();
			
			String FECHA_REPORTE_PARAM = "fechaReporte";
			String ID_RECURSO_PARAM = "idRecurso";
			String PERIODO_DESDE_PARAM = "periodoDesde";
			String PERIODO_HASTA_PARAM = "periodoHasta";
			String ANIO_DESDE_PARAM = "anioDesde";
			String ANIO_HASTA_PARAM = "anioHasta";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
			
			String fechaReporte= contribuyenteCerReport.getFechaReporteView();
			if( contribuyenteCerReport.getRecurso()!=null){
			String idRecurso = contribuyenteCerReport.getRecurso().getId().toString();
			run.putParameter(ID_RECURSO_PARAM, idRecurso);
			}
			String fechaDesde= contribuyenteCerReport.getFechaDesdeView();
			String fechaHasta= contribuyenteCerReport.getFechaHastaView();
			String periodoDesde= contribuyenteCerReport.getPeriodoDesdeView();
			String periodoHasta= contribuyenteCerReport.getPeriodoHastaView();
			String anioDesde= contribuyenteCerReport.getAnioDesdeView();
			String anioHasta= contribuyenteCerReport.getAnioHastaView();
		
			// Carga de parametros para adp
			run.putParameter(FECHA_REPORTE_PARAM, fechaReporte);
			
			run.putParameter(PERIODO_DESDE_PARAM, periodoDesde);
			run.putParameter(PERIODO_HASTA_PARAM, periodoHasta);
			run.putParameter(ANIO_DESDE_PARAM, anioDesde);
			run.putParameter(ANIO_HASTA_PARAM, anioHasta);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			contribuyenteCerReport.setProcesando(true);
			contribuyenteCerReport.setDesRunningRun(run.getDesCorrida());
			contribuyenteCerReport.setEstRunningRun(run.getMensajeEstado());
			contribuyenteCerReport.setReporteGenerado(new PlanillaVO());
			contribuyenteCerReport.setExisteReporteGenerado(false);
			contribuyenteCerReport.setError(false);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return contribuyenteCerReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// ---> Reporte total detallado Recaudación CER	

	// ---> Reporte de Recaudado

	public RecaudadoReport getRecaudadoReportInit(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			RecaudadoReport recaudadoReport = new RecaudadoReport();

			// Obtengo el id del procurador de la sesion
			Long idProcuradorSesion = userContext.getIdProcurador();
			recaudadoReport.setIdProcuradorSesion(idProcuradorSesion);
			recaudadoReport.setFechaHasta(new Date());
			
			if(idProcuradorSesion != null){
				// solo podra acceder a los convenios del procurador de la sesion
				Procurador procurador = Procurador.getById(idProcuradorSesion);
				ProcuradorVO procuradorVO = (ProcuradorVO) procurador.toVO(0);
				recaudadoReport.getListProcurador().add(procuradorVO);
				recaudadoReport.setProcurador(procuradorVO);
				recaudadoReport.setVisualizarComboProcurador(true);
			}else{
				// selecciono la opcion Todos del Procurador
				// lista de planes asociadas al recurso
				List<Procurador> listProcurador = Procurador.getListActivos();
				recaudadoReport.setListProcurador((ArrayList<ProcuradorVO>)ListUtilBean.toVO(listProcurador, 
						new ProcuradorVO(-1, StringUtil.SELECT_OPCION_TODOS)));
				recaudadoReport.getProcurador().setId(-1L);
			}

			// carga de lista de todos los recursos, selecciona la opcion Seleccionar
			List<Recurso> listRecurso = Recurso.getListActivos();
			recaudadoReport.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				recaudadoReport.getListRecurso().add(item.toVOWithCategoria());							
			}
			recaudadoReport.getRecurso().setId(-1L);
			
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_RECAUDADO, DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				recaudadoReport.setProcesando(true);
				recaudadoReport.setDesRunningRun(runningRun.getDesCorrida());
				recaudadoReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				recaudadoReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_RECAUDADO, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					PlanillaVO reporteGenerado = new PlanillaVO();						

					FileCorrida fileCorrida = listFileCorrida.get(0);

					reporteGenerado.setTitulo(ultimaCorrida.getDesCorrida());
					reporteGenerado.setFileName(fileCorrida.getFileName().replace('\\' , '/'));
					reporteGenerado.setDescripcion(fileCorrida.getNombre()); 
					reporteGenerado.setCtdResultados(1L);

					recaudadoReport.setReporteGenerado(reporteGenerado);
					recaudadoReport.setExisteReporteGenerado(true);
				}else{
					log.debug("listFileCorrida vacia");
					recaudadoReport.setReporteGenerado(new PlanillaVO());
					recaudadoReport.setExisteReporteGenerado(false);
				}
			}else{
				log.debug("No existe lastEndOkRun");
				recaudadoReport.setReporteGenerado(new PlanillaVO());
				recaudadoReport.setExisteReporteGenerado(false);
			}

			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_RECAUDADO, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					recaudadoReport.setError(true);
					recaudadoReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					recaudadoReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					recaudadoReport.setError(false);
				}
			}
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return recaudadoReport;

		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
		
	public RecaudadoReport getRecaudadoReportResult(UserContext userContext, RecaudadoReport recaudadoReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		// limpio la lista de resultados y errores
		recaudadoReport.setListResult(new ArrayList());
		recaudadoReport.clearError();

		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Validaciones:

			// Recurso
			if(ModelUtil.isNullOrEmpty(recaudadoReport.getRecurso()))
				recaudadoReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);

			// fecha hasta no sea menor a fecha desde
			Date fecEmiDesde = recaudadoReport.getFechaDesde();
			Date fecEmiHasta = recaudadoReport.getFechaHasta();
			//Date fecPagoHasta = recaudadoReport.getFechaPagoHasta();
			
			if(fecEmiDesde==null)
				recaudadoReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.RECAUDADO_REPORT_FECHA_EMI_DESDE);

			if(fecEmiHasta==null)
				recaudadoReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.RECAUDADO_REPORT_FECHA_EMI_HASTA);

			if (fecEmiDesde!=null && fecEmiHasta!=null && DateUtil.isDateAfter(fecEmiDesde, fecEmiHasta)){
				recaudadoReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
						GdeError.RECAUDADO_REPORT_FECHA_EMI_DESDE,GdeError.RECAUDADO_REPORT_FECHA_EMI_HASTA);
			}
		
			if(recaudadoReport.hasError())
				return recaudadoReport;

			// Disparar el proceso adp.
			String adpMessage = "Reporte Recaudado - fecha: "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_RECAUDADO, adpMessage);
			run.create();

			String ID_RECURSO_PARAM = "idRecurso";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String FECHA_PAGO_HASTA_PARAM = "fechaPagoHasta";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
			
			String idRecurso = recaudadoReport.getRecurso().getId().toString();
			String fechaDesde= recaudadoReport.getFechaDesdeView();
			String fechaHasta= recaudadoReport.getFechaHastaView();
			String fechaPagoHasta = recaudadoReport.getFechaHastaPagoView();
			
			// Carga de parametros para adp
			run.putParameter(ID_RECURSO_PARAM, idRecurso);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			run.putParameter(FECHA_PAGO_HASTA_PARAM, fechaPagoHasta);
			
			run.execute(new Date());

			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			recaudadoReport.setProcesando(true);
			recaudadoReport.setDesRunningRun(run.getDesCorrida());
			recaudadoReport.setEstRunningRun(run.getMensajeEstado());
			recaudadoReport.setReporteGenerado(new PlanillaVO());
			recaudadoReport.setExisteReporteGenerado(false);
			recaudadoReport.setError(false);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return recaudadoReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	// <--- Reporte de Recaudado	

	
	
	// ---> Reporte de Deuda de Procurador
	public DeudaProcuradorReport getDeudaProcuradorReportInit(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			DeudaProcuradorReport deudaProcuradorReport = new DeudaProcuradorReport();
			
			// carga de lista de todos los procuradores, selecciona la opcion Todos
			List<Procurador> listProcurador = Procurador.getList();
			deudaProcuradorReport.setListProcurador(ListUtilBean.toVO(listProcurador, new ProcuradorVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR)));
			deudaProcuradorReport.getProcurador().setId(-1L);
					
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_DEUDA_PROCURADOR, DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				deudaProcuradorReport.setProcesando(true);
				deudaProcuradorReport.setDesRunningRun(runningRun.getDesCorrida());
				deudaProcuradorReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				deudaProcuradorReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_DEUDA_PROCURADOR,DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					deudaProcuradorReport.setTituloReportes(ultimaCorrida.getDesCorrida());
					for(FileCorrida fileCorrida : listFileCorrida){
						PlanillaVO reporteGenerado = new PlanillaVO();						
						 
						reporteGenerado.setFileName(fileCorrida.getFileName().replace('\\' , '/'));
						reporteGenerado.setDescripcion(fileCorrida.getNombre()); 
						reporteGenerado.setCtdResultados(1L);
						
						deudaProcuradorReport.getListReporteGenerado().add(reporteGenerado);
					}
					//deudaProcuradorReport.setReporteGenerado(reporteGenerado);
					deudaProcuradorReport.setExisteReporteGenerado(true);
				}else{					
					deudaProcuradorReport.setExisteReporteGenerado(false);
				}
			}else{				
				deudaProcuradorReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_DEUDA_PROCURADOR, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					deudaProcuradorReport.setError(true);
					deudaProcuradorReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					deudaProcuradorReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					deudaProcuradorReport.setError(false);
				}
			}
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaProcuradorReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public DeudaProcuradorReport getDeudaProcuradorReportResult(UserContext userContext, DeudaProcuradorReport deudaProcuradorReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		deudaProcuradorReport.setListResult(new ArrayList());
		deudaProcuradorReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Validaciones:
									
			// fecha hasta no sea menor a fecha desde
			Date fecVtoDesde = deudaProcuradorReport.getFechaVtoDesde();
			Date fecVtoHasta = deudaProcuradorReport.getFechaVtoHasta();
			
			if(fecVtoDesde==null)
				deudaProcuradorReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDA_PROCURADOR_REPORT_FECHA_VTO_DESDE);

			if(fecVtoHasta==null)
				deudaProcuradorReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDA_PROCURADOR_REPORT_FECHA_VTO_HASTA);
			
			if (fecVtoDesde!=null && fecVtoHasta!=null && DateUtil.isDateAfter(fecVtoDesde, fecVtoHasta)){
				deudaProcuradorReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
						GdeError.DEUDA_PROCURADOR_REPORT_FECHA_VTO_DESDE,GdeError.DEUDA_PROCURADOR_REPORT_FECHA_VTO_HASTA);
			}
			
			if(deudaProcuradorReport.getTipoReporte()==null ||
					deudaProcuradorReport.getTipoReporte().intValue()<0)
				deudaProcuradorReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DEUDA_PROCURADOR_REPORT_TIPO_REPORTE);
			
			if(deudaProcuradorReport.hasError())
				return deudaProcuradorReport;
			
			// Disparar el proceso adp.
			String adpMessage = "Reporte Deuda por Procurador - fecha: "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_DEUDA_PROCURADOR, adpMessage);
			run.create();
			
			String ID_PROCURADOR_PARAM = "idProcurador";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String TIPO_REPORTE_PARAM = "tipoReporte";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
									
			String idProcurador = deudaProcuradorReport.getProcurador().getId().toString();
			String fechaDesde= deudaProcuradorReport.getFechaVtoDesdeView();
			String fechaHasta= deudaProcuradorReport.getFechaVtoHastaView();
			String tipoReporte = deudaProcuradorReport.getTipoReporte().toString();

			// Carga de parametros para adp
			run.putParameter(ID_PROCURADOR_PARAM, idProcurador);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(TIPO_REPORTE_PARAM, tipoReporte);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			deudaProcuradorReport.setProcesando(true);
			deudaProcuradorReport.setDesRunningRun(run.getDesCorrida());
			deudaProcuradorReport.setEstRunningRun(run.getMensajeEstado());			
			deudaProcuradorReport.setExisteReporteGenerado(false);
			deudaProcuradorReport.setError(false);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deudaProcuradorReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Reporte de Deuda de Procurador	

	
	// ---> Reporte de Distribucion de Totales Emitidos
	public DistribucionReport getDistribucionReportInit(UserContext userContext) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			DistribucionReport distribucionReport = new DistribucionReport();
			
			// carga de lista de todos los recursos, selecciona la opcion Todos
			List<Recurso> listRecurso = Recurso.getListActivos();
			distribucionReport.getListRecurso().add(new RecursoVO(-1, StringUtil.SELECT_OPCION_SELECCIONAR));
			for (Recurso item: listRecurso){				
				distribucionReport.getListRecurso().add(item.toVOWithCategoria());							
			}
			distribucionReport.getRecurso().setId(-1L);
			
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_DISTRIBUCION, DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				distribucionReport.setProcesando(true);
				distribucionReport.setDesRunningRun(runningRun.getDesCorrida());
				distribucionReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				distribucionReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_DISTRIBUCION, DemodaUtil.currentUserContext().getUserName());
			if(lastEndOkRun != null){
				Corrida ultimaCorrida = Corrida.getById(lastEndOkRun.getId());
				List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(ultimaCorrida, 1);
				if(!ListUtil.isNullOrEmpty(listFileCorrida)){
					PlanillaVO reporteGenerado = new PlanillaVO();						
					 
					FileCorrida fileCorrida = listFileCorrida.get(0);
					
					reporteGenerado.setTitulo(ultimaCorrida.getDesCorrida());
					reporteGenerado.setFileName(fileCorrida.getFileName().replace('\\' , '/'));
					reporteGenerado.setDescripcion(fileCorrida.getNombre()); 
					reporteGenerado.setCtdResultados(1L);
										
					distribucionReport.setReporteGenerado(reporteGenerado);
					distribucionReport.setExisteReporteGenerado(true);
				}else{
					distribucionReport.setReporteGenerado(new PlanillaVO());
					distribucionReport.setExisteReporteGenerado(false);
				}
			}else{
				distribucionReport.setReporteGenerado(new PlanillaVO());
				distribucionReport.setExisteReporteGenerado(false);
			}
			
			if(runningRun == null && lastEndOkRun == null){
				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_DISTRIBUCION, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					distribucionReport.setError(true);
					distribucionReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					distribucionReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					distribucionReport.setError(false);
				}
			}
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return distribucionReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	

	public DistribucionReport getDistribucionReportResult(UserContext userContext, DistribucionReport distribucionReport) throws DemodaServiceException{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		distribucionReport.setListResult(new ArrayList());
		distribucionReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Validaciones:
			
			// Recurso
			if(ModelUtil.isNullOrEmpty(distribucionReport.getRecurso()))
				distribucionReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
						
			// fecha hasta no sea menor a fecha desde
			Date fecEmiDesde = distribucionReport.getFechaEmiDesde();
			Date fecEmiHasta = distribucionReport.getFechaEmiHasta();
			
			if(fecEmiDesde==null)
				distribucionReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DISTRIBUCION_REPORT_FECHA_EMI_DESDE);

			if(fecEmiHasta==null)
				distribucionReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DISTRIBUCION_REPORT_FECHA_EMI_HASTA);
			
			if (fecEmiDesde!=null && fecEmiHasta!=null && DateUtil.isDateAfter(fecEmiDesde, fecEmiHasta)){
				distribucionReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
						GdeError.DISTRIBUCION_REPORT_FECHA_EMI_DESDE,GdeError.DISTRIBUCION_REPORT_FECHA_EMI_HASTA);
			}
						
			if(distribucionReport.hasError())
				return distribucionReport;
			
			// Disparar el proceso adp.
			String adpMessage = "Reporte Distribucion de Totales Emitidos - fecha: "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_DISTRIBUCION, adpMessage);
			run.create();
			
			String ID_RECURSO_PARAM = "idRecurso";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
									
			String idRecurso = distribucionReport.getRecurso().getId().toString();
			String fechaDesde= distribucionReport.getFechaEmiDesdeView();
			String fechaHasta= distribucionReport.getFechaEmiHastaView();
			
			// Carga de parametros para adp
			run.putParameter(ID_RECURSO_PARAM, idRecurso);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			
			run.execute(new Date());
			
			// Elimino las corridas y reportes anteriores
			List<Long> listIdRun = run.getListOldRunId(DemodaUtil.currentUserContext().getUserName());
			if(listIdRun!=null){
				for(Long idCorrida: listIdRun){
					Corrida corrida = Corrida.getByIdNull(idCorrida);
					if(corrida != null){
						List<FileCorrida> listFileCorrida = FileCorrida.getListByCorridaYPaso(corrida, 1);
						for(FileCorrida fileCorrida: listFileCorrida){
							if(!StringUtil.isNullOrEmpty(fileCorrida.getFileName())){
								try{
									File deleteFile = new File(fileCorrida.getFileName());
									if(deleteFile.exists()){
										deleteFile.delete();											
									}
								}catch(Exception e){
									log.debug("Excepcion al Tratar de Eliminar: "+e);
								}
							}
						}									
					}
				}				
				run.cleanOld(DemodaUtil.currentUserContext().getUserName());
			}

			// Limpio la planilla de Reportes, cargo los strings de la nueva corrida y
			distribucionReport.setProcesando(true);
			distribucionReport.setDesRunningRun(run.getDesCorrida());
			distribucionReport.setEstRunningRun(run.getMensajeEstado());
			distribucionReport.setReporteGenerado(new PlanillaVO());
			distribucionReport.setExisteReporteGenerado(false);
			distribucionReport.setError(false);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return distribucionReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	// <--- Reporte de Distribucion de Totales Emitidos


}
