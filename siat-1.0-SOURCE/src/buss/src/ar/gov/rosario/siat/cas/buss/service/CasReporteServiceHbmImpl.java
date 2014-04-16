//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.service;

/**
 * Implementacion de servicios de submodulo Reporte del modulo cas
 * @author Andrei
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.TipoSolicitud;
import ar.gov.rosario.siat.cas.iface.model.SolPendReport;
import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudVO;
import ar.gov.rosario.siat.cas.iface.service.ICasReporteService;
import ar.gov.rosario.siat.cas.iface.util.CasError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.UserContext;

public class CasReporteServiceHbmImpl implements ICasReporteService { 
	
	private Logger log = Logger.getLogger(CasReporteServiceHbmImpl.class);

		
	// ---> Reporte de Solicitudes Pendientes
	public SolPendReport getSolPendReportInit(UserContext userContext) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			SolPendReport solPendReport = new SolPendReport();
			
			// Carga de lista de areas y tipo de solicitudes

			List<Area> listArea = Area.getListActivasHasTipoSolicitud();
			
			String listAreaIds = ListUtil.getStringIds(listArea);
			
			solPendReport.getListArea().add(new AreaVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (Area item: listArea){				
				solPendReport.getListArea().add((AreaVO) item.toVO());		
			}
			
			
			List<TipoSolicitud> listTipoSolicitud = TipoSolicitud.getListActivosHasAreaList(listAreaIds);
			solPendReport.getListTipoSolicitud().add(new TipoSolicitudVO(-1, StringUtil.SELECT_OPCION_TODOS));
			for (TipoSolicitud item: listTipoSolicitud){				
				solPendReport.getListTipoSolicitud().add((TipoSolicitudVO) item.toVO());		
			}
			
			// Verificar si existe una corrida en estado "Procesando" y buscar el archivo de salida de la ultima
			// Corrida "Finalizada OK".		
			AdpRun runningRun = AdpRun.getRunning(Proceso.PROCESO_REPORTE_SOLPEND, DemodaUtil.currentUserContext().getUserName());
			// Si existe una corrida en estado "Procesando" se setea una bandera para deshabilitar 
			// la generecion de un nuevo reporte
			if(runningRun != null){
				solPendReport.setProcesando(true);
				solPendReport.setDesRunningRun(runningRun.getDesCorrida());
				solPendReport.setEstRunningRun(runningRun.getMensajeEstado());
			}else{
				solPendReport.setProcesando(false);
			}
			// Si existe una corrida "Finalizada OK", busca el archivo generado y lo carga en la planilla.
			AdpRun lastEndOkRun = AdpRun.getLastEndOk(Proceso.PROCESO_REPORTE_SOLPEND, DemodaUtil.currentUserContext().getUserName());
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
										
					solPendReport.setReporteGenerado(reporteGenerado);
					solPendReport.setExisteReporteGenerado(true);
				}else{
					solPendReport.setReporteGenerado(new PlanillaVO());
					solPendReport.setExisteReporteGenerado(false);
				}
			}else{
				solPendReport.setReporteGenerado(new PlanillaVO());
				solPendReport.setExisteReporteGenerado(false);
			}
			
			//Si la última corrida es nula o no es OK la ultima Corrida
			if(runningRun == null && lastEndOkRun == null){
				

				AdpRun lastEndWrongRun = AdpRun.getLastEndWrong(Proceso.PROCESO_REPORTE_SOLPEND, DemodaUtil.currentUserContext().getUserName());
				if(lastEndWrongRun != null){
					solPendReport.setError(true);
					solPendReport.setDesErrorRun(lastEndWrongRun.getDesCorrida());
					solPendReport.setEstErrorRun(lastEndWrongRun.getMensajeEstado());
				}else{
					solPendReport.setError(false);
				}
			}
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return solPendReport;
			
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public SolPendReport getSolPendReportResult(UserContext userContext, SolPendReport solPendReport) throws DemodaServiceException {

		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// limpio la lista de resultados y errores
		solPendReport.setListResult(new ArrayList());
		solPendReport.clearError();
		
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();

			// Validaciones:
			/**
			 * TODO Rearmar las validaciones cuando estén definidos los campos necesarios del reporte.
			 */

			// fecha hasta no sea menor a fecha desde
			Date fecEmiDesde = solPendReport.getFechaEmiDesde();
			Date fecEmiHasta = solPendReport.getFechaEmiHasta();

			if(fecEmiDesde==null)
				solPendReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.SOLPEND_REPORT_FECHA_EMI_DESDE);

			if(fecEmiHasta==null)
				solPendReport.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.SOLPEND_REPORT_FECHA_EMI_HASTA);
			
			if (fecEmiDesde!=null && fecEmiHasta!=null && DateUtil.isDateAfter(fecEmiDesde, fecEmiHasta)){
				solPendReport.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
						CasError.SOLPEND_REPORT_FECHA_EMI_DESDE,CasError.SOLPEND_REPORT_FECHA_EMI_HASTA);
			}
					
			if(solPendReport.hasError())
				return solPendReport;
			
			// Disparar el proceso adp.
			String adpMessage = "Reporte de Solicitudes Pendientes - Fecha: " + DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
			AdpRun run = AdpRun.newRun(Proceso.PROCESO_REPORTE_SOLPEND, adpMessage);
			run.create();//ACCESO A LA DB
			
			/**
			 * TODO Validar porque están hardcodeadas estas variables en esta clase y en el Worker o DAO
			 */
			
			String ID_AREA_PARAM = "idArea";
			String ID_TIPO_SOLICITUD_PARAM = "idTipoSolicitud";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
									
			String idArea = solPendReport.getArea().getIdView();
			String idTipoSolicitud = solPendReport.getTipoSolicitud().getIdView();
			String fechaDesde = solPendReport.getFechaEmiDesdeView();
			String fechaHasta = solPendReport.getFechaEmiHastaView();
			
			// Carga de parametros para ADP en la tabla correspondiente.
			
			run.putParameter(ID_AREA_PARAM, idArea);
			run.putParameter(ID_TIPO_SOLICITUD_PARAM, idTipoSolicitud);
			run.putParameter(FECHA_DESDE_PARAM, fechaDesde);
			run.putParameter(FECHA_HASTA_PARAM, fechaHasta);
			run.putParameter(USER_NAME_PARAM, DemodaUtil.currentUserContext().getUserName());
			run.putParameter(USER_ID_PARAM, StringUtil.formatLong(DemodaUtil.currentUserContext().getIdUsuarioSiat()));
			
			// Schedulea una ejecución inmediata
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
			solPendReport.setProcesando(true);
			solPendReport.setDesRunningRun(run.getDesCorrida());
			solPendReport.setEstRunningRun(run.getMensajeEstado());
			solPendReport.setReporteGenerado(new PlanillaVO());
			solPendReport.setExisteReporteGenerado(false);
			solPendReport.setError(false);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return solPendReport;
		} catch (Exception e) {
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
	
	public SolPendReport getSolPendReportParamArea(UserContext userContext, SolPendReport solPendReport) throws DemodaServiceException {
		
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			DemodaUtil.setCurrentUserContext(userContext);
			SiatHibernateUtil.currentSession();
			
			solPendReport.clearError();
						
			
			solPendReport.getListTipoSolicitud().clear();
			
			if (!ModelUtil.isNullOrEmpty(solPendReport.getArea())){
				Area listArea = Area.getById(solPendReport.getArea().getId()); 
				
				List<TipoSolicitud> listTipoSolicitud = TipoSolicitud.getListActivosByArea(listArea);
				solPendReport.getListTipoSolicitud().add(new TipoSolicitudVO(-1, StringUtil.SELECT_OPCION_TODOS));
				for (TipoSolicitud item: listTipoSolicitud){				
					solPendReport.getListTipoSolicitud().add((TipoSolicitudVO) item.toVO());		
				}
			} else {
				
				solPendReport.getListArea().clear();
				
				List<Area> listArea = Area.getListActivasHasTipoSolicitud();
				solPendReport.getListArea().add(new AreaVO(-1, StringUtil.SELECT_OPCION_TODOS));
				for (Area item: listArea){				
					solPendReport.getListArea().add((AreaVO) item.toVO());		
				}
				 
				
				String listAreaIds = ListUtil.getStringIds(listArea);
				
				List<TipoSolicitud> listTipoSolicitud = TipoSolicitud.getListActivosHasAreaList(listAreaIds);
				solPendReport.getListTipoSolicitud().add(new TipoSolicitudVO(-1, StringUtil.SELECT_OPCION_TODOS));
				for (TipoSolicitud item: listTipoSolicitud){				
					solPendReport.getListTipoSolicitud().add((TipoSolicitudVO) item.toVO());		
				}
			}
			
			if(solPendReport.hasError())
				return solPendReport;

			log.debug(funcName + ": exit");
			return solPendReport;
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
		
	}
	// <--- Reporte de Solicitudes Pendientes




}
