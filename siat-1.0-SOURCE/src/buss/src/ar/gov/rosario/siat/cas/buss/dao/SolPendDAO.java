//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.dao;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.cas.buss.bean.AuxSolPendReport;
import ar.gov.rosario.siat.cas.buss.bean.Solicitud;
import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudVO;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.frm.iface.model.FormatoSalida;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.TablaVO;



/**
 * DAO para reporte de Solicitudes Pendientes.
 * TODO No esta terminado, todavia está copiado de Deuda del modulo GDE.
 * @author Andrei
 * 
 */
public class SolPendDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(SolPendDAO.class);
	/*
	 * Tablas usadso 
	 */
	public static String SEQUENCE_COD_REF_PAGO = "deu_codrefpag_sq";	
	public static String TABLA_DEUDA_ADMIN      = "_deudaAdmin";
	public static String TABLA_DEUDA_ANULADA    = "gde_deudaAnulada";
	public static String TABLA_DEUDA_CANCELADA  = "gde_deudaCancelada";
	public static String TABLA_DEUDA_JUDICIAL   = "gde_deudaJudicial";
	
	
	public SolPendDAO() {
		super(null);
	}
	
	@SuppressWarnings("rawtypes")
	public SolPendDAO(Class boClass) {
		super(boClass);
	}

	
	public AuxSolPendReport generarPDF4Report(AuxSolPendReport auxSolPendReport) throws Exception{
		log.debug("generarPDF4Report - enter");
		
		String fileDir = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);						
		String idCorrida = AdpRun.currentRun().getId().toString();
		String fileName = idCorrida+"_ReporteSolicitudesPendientes_"+ auxSolPendReport.getUserId();

		PlanillaVO planilla = new PlanillaVO();
		ContenedorVO contenedorVO = new ContenedorVO("Contenedor");			

		
		// Genera la tabla de filtros de la busqueda
		FilaVO filaDeCabecera = new FilaVO();	
		
		String tiraArea = "";
		String tiraTipoSolicitud = "";
		
		List<AreaVO> listArea = auxSolPendReport.getListArea();
		if(listArea.size()>1){
			for (AreaVO item: listArea){				
				tiraArea+= item.getDesArea() + ", ";		
			}
			//TODO SACAR LA ULTIMA COMA
		}else{
			tiraArea = (auxSolPendReport.getListArea().get(0)).getDesArea();
		}
		
		List<TipoSolicitudVO> listTipoSolicitud = auxSolPendReport.getListTipoSolicitud();
		if(listTipoSolicitud.size()>1){
			for (TipoSolicitudVO item: listTipoSolicitud){				
				tiraTipoSolicitud+= item.getDescripcion() + ", ";		
			}
			//TODO SACAR LA ULTIMA COMA
		}else{
			tiraTipoSolicitud = (auxSolPendReport.getListTipoSolicitud().get(0)).getDescripcion();
		}
		
		filaDeCabecera.add(new CeldaVO(listArea.size()==1?tiraArea:"Todas", "area", "Área"));
		filaDeCabecera.add(new CeldaVO(listTipoSolicitud.size()==1?tiraTipoSolicitud:"Todas", "tipoSolicitud", "TIpo de Solicitud"));
		filaDeCabecera.add(new CeldaVO(auxSolPendReport.getFechaDesdeView(), "fechaDesde", "Fecha Desde"));
		filaDeCabecera.add(new CeldaVO(auxSolPendReport.getFechaHastaView(), "fechaHasta", "Fecha Hasta"));
		
		TablaVO tablaFiltros = new TablaVO("Filtros Aplicados");
		tablaFiltros.add(filaDeCabecera);
		tablaFiltros.setTitulo("Filtros de Búsqueda");
		contenedorVO.setTablaFiltros(tablaFiltros);
		
		// Busca Todas las Solicitudes
		
		List<Solicitud> listResultSolicitudesPendientes = CasDAOFactory.getSolicitudDAO().getSolicitudesPendientesAreaTipoSolicitud(listArea, listTipoSolicitud);
		List<Solicitud> listResultSolicitudes = CasDAOFactory.getSolicitudDAO().getSolicitudesAreaTipoSolicitud(listArea, listTipoSolicitud);

		
		//Long totCantSolicitudes = (long) listResultSolicitudes.size();
		
		//Tablas de contenido
		Double countSolPend = 0D;
		Double countSol = 0D;
		Double ratio = 0D;

		Double totCountSolPend = 0D;
		Double totCountSol = 0D;
		Double totRatio = 0D;
		
		FilaVO filaCabecera = new FilaVO();

		filaCabecera.add(new CeldaVO("Tipo de Solicitud"));
		filaCabecera.add(new CeldaVO("Solicitudes"));
		filaCabecera.add(new CeldaVO("Solicitudes Pendientes"));
		filaCabecera.add(new CeldaVO("% Pendientes"));

		for(AreaVO area:listArea){

			totCountSolPend = 0D;
			totCountSol = 0D;
			totRatio = 0D;
			
			TablaVO tablacontenido = new TablaVO(area.getDesArea());
			FilaVO filaPie = new FilaVO();
			
			/**
			 * TODO Quitar las tablas vacias.
			 */
			for(TipoSolicitudVO tipoSolicitud:listTipoSolicitud){

				countSolPend = 0D;
				countSol = 0D;
				ratio = 0D;

				FilaVO filaContenido = new FilaVO();

				for(Solicitud solPend:listResultSolicitudesPendientes){
					//.debug("SOL PEND: "+ solPend.getAreaDestino().getDesArea() +" - "+ area.getDesArea() +" - "+ solPend.getAreaDestino().getId().equals(area.getId()));
					if(solPend.getAreaDestino().getId().equals(area.getId()) && (solPend.getTipoSolicitud().getId().equals(tipoSolicitud.getId()))){
						countSolPend++;
						totCountSolPend++;
					}
				}
				for(Solicitud sol:listResultSolicitudes){
					//log.debug("SOL NO PEND: "+ sol.getAreaDestino().getDesArea() +" - "+ area.getDesArea() +" - "+ sol.getAreaDestino().getId().equals(area.getId()));
					if(sol.getAreaDestino().getId().equals(area.getId()) && (sol.getTipoSolicitud().getId().equals(tipoSolicitud.getId()))){
						countSol++;
						totCountSol++;
					}
				}

				ratio = countSol==0D?0D:(countSolPend/countSol);

				if(!countSol.equals(0D)){
					
					tablacontenido.setTitulo(area.getDesArea());
					tablacontenido.setFilaCabecera(filaCabecera);
					
					filaContenido.add(new CeldaVO(tipoSolicitud.getDescripcion()));
					filaContenido.add(new CeldaVO(String.valueOf(countSol.longValue())));
					filaContenido.add(new CeldaVO(String.valueOf(countSolPend.longValue())));
					filaContenido.add(new CeldaVO(StringUtil.formatDouble(ratio, "###0.00")));

					log.debug("Solicitud: "+ countSol +" - "+ countSolPend +" - "+ StringUtil.formatDouble(ratio, "###0.00"));

					tablacontenido.add(filaContenido);


				}
			
			}
			
			totRatio = totCountSolPend==0D?0D:(totCountSolPend/totCountSol);
			if(!totCountSolPend.equals(0D)){
				filaPie.add(new CeldaVO("\tTotal"));
				filaPie.add(new CeldaVO(String.valueOf(totCountSol.longValue())));
				filaPie.add(new CeldaVO(String.valueOf(totCountSolPend.longValue())));
				filaPie.add(new CeldaVO(StringUtil.formatDouble(totRatio, "###0.00")));
				
				log.debug("Total Area: "+ totCountSol +" - "+ totCountSolPend +" - "+ StringUtil.formatDouble(totRatio, "###0.00"));

				tablacontenido.add(filaPie);
			}
			contenedorVO.add(tablacontenido);
		}


		
		// Generacion del PrintModel		
		PrintModel printModel = new PrintModel();
		
		printModel.setRenderer(FormatoSalida.PDF.getId());
		printModel.putCabecera("FileSharePath", SiatParam.getString("FileSharePath"));
		printModel.setExcludeFileName("/publico/general/reportes/default.exclude");
		printModel.cargarXsl("/mnt/publico/general/reportes/pageModel.xsl", PrintModel.RENDER_PDF);
		printModel.setTopeProfundidad(5);
		printModel.setData(contenedorVO);
		
		printModel.putCabecera("TituloReporte", "Reporte de Solicitudes Pendientes");
		printModel.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
		printModel.putCabecera("Usuario", auxSolPendReport.getUserName());
							
		// Genera el PDF
		String fileNamePdf = fileName + ".pdf";			
		byte[] byteStream = printModel.getByteArray();
		FileOutputStream gesJudReportFile = new FileOutputStream(fileDir+"/"+fileNamePdf);
		gesJudReportFile.write(byteStream);
		gesJudReportFile.close();
		
		// Setea en el adapter, los datos del archivo generado
		planilla.setFileName(fileDir+"/"+fileNamePdf);
		planilla.setDescripcion("Reporte de Solicitudes Pendientes");									
		auxSolPendReport.setReporteGenerado(planilla);
		
		log.debug("generarPDF4Report - exit");
		return auxSolPendReport;
	}
	
	
}


