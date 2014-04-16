//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.bal;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.ImpPar;
import ar.gov.rosario.siat.bal.buss.bean.InformeTotalPar;
import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.adpcore.AdpRunState;
import coop.tecso.adpcore.AdpWorker;
import coop.tecso.adpcore.engine.AdpParameter;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.Datum;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.RangoFechaVO;

/**
 * Genera el Reporte de Total por Partida en forma desconectada.
 * 
 * @author Tecso Coop. Ltda.
 */
public class ReporteTotalParWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(ReporteTotalParWorker.class);
		
	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
		// Verfica numero paso y estado en adprun,
		// Llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)){ // Paso 1 del Reporte de Total por Partida: Generar PDF
			this.generarReporte(adpRun);
		}else{
			// No existen otros pasos para el Proceso.
		}
	}

	public void reset(AdpRun adpRun) throws Exception {
	}

	public boolean validate(AdpRun adpRun) throws Exception {
		return false;
	}

	/**
	 *  Generar Reporte de Total por Partida (PDF).
	 * 
	 * @param adpRun
	 * @throws Exception
	 */
	public void generarReporte(AdpRun adpRun) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		Session session = null;
		Transaction tx  = null; 

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			String ID_PARTIDA_PARAM = "idPartida";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String FECHA_EXTRA_PARAM = "rangoExtra";
			String USER_NAME_PARAM = "UserName";
	
			AdpParameter paramIdPartida = adpRun.getAdpParameter(ID_PARTIDA_PARAM);
			AdpParameter paramFechaHasta = adpRun.getAdpParameter(FECHA_HASTA_PARAM);
			AdpParameter paramFechaDesde = adpRun.getAdpParameter(FECHA_DESDE_PARAM);
			AdpParameter paramUserName = adpRun.getAdpParameter(USER_NAME_PARAM);
			AdpParameter paramFechaExtra = adpRun.getAdpParameter(FECHA_EXTRA_PARAM);
			
			Long idPartida = NumberUtil.getLong(paramIdPartida.getValue());
			String fechaDesdeView = paramFechaDesde.getValue();
			String fechaHastaView = paramFechaHasta.getValue();
			Date fechaDesde= DateUtil.getDate(fechaDesdeView, DateUtil.ddSMMSYYYY_MASK);
			Date fechaHasta= DateUtil.getDate(fechaHastaView, DateUtil.ddSMMSYYYY_MASK);
			boolean fechaExtra = "true".equals(paramFechaExtra.getValue());
			String userName = paramUserName.getValue();
		
			String fechasDesdeExtras = "";
			String fechasHastaExtras = "";
			List<RangoFechaVO> listRangoFecha = new ArrayList<RangoFechaVO>();
			if(fechaExtra){
				String FECHAS_DESDE_EXTRAS_PARAM = "fechaDesdeExtra";
				String FECHAS_HASTA_EXTRAS_PARAM = "fechaHastaExtra";
				AdpParameter paramFechasDesdeExtras = adpRun.getAdpParameter(FECHAS_DESDE_EXTRAS_PARAM);
				AdpParameter paramFechasHastaExtras = adpRun.getAdpParameter(FECHAS_HASTA_EXTRAS_PARAM);
				
				fechasDesdeExtras = paramFechasDesdeExtras.getValue();				
				fechasHastaExtras = paramFechasHastaExtras.getValue();
				
				Datum fechaDesdeDatum = Datum.parseAtChar(fechasDesdeExtras,',');
				Datum fechaHastaDatum = Datum.parseAtChar(fechasHastaExtras,',');
				for(int i = 0; i < fechaDesdeDatum.getColNumMax(); i++){
					RangoFechaVO rangoFecha = new RangoFechaVO();
					rangoFecha.setFechaDesde(DateUtil.getDate(fechaDesdeDatum.getCols(i)));
					rangoFecha.setFechaHasta(DateUtil.getDate(fechaHastaDatum.getCols(i)));
					listRangoFecha.add(rangoFecha);
				}
			}
			
			// Seteamos el UserName en el UserContext para que al modificarse la corrida quede grabado.
			DemodaUtil.currentUserContext().setUserName(userName);
			
			Partida partida = Partida.getByIdNull(idPartida);
			if(partida == null){
		     	adpRun.changeState(AdpRunState.FIN_ERROR, "Error al obtener la partida", false);
            	adpRun.logError("Error al obtener la partida");
            	return;
			}
					
			// Levanto la Corrida
			Corrida corrida = Corrida.getByIdNull(adpRun.getId());
			if(corrida == null){
		     	adpRun.changeState(AdpRunState.FIN_ERROR, "Error al leer la corrida del proceso", false);
            	adpRun.logError("Error al leer la corrida del proceso");
            	return;
			}
			
			// Calcular total para partida
			Double totalVencido = 0D;
			Double totalActual = 0D;
			Double total = 0D;
			Object totales[] = ImpPar.getTotalActVenByIdPartidaYFechas(partida.getId(), fechaDesde, fechaHasta, null);
			if(totales != null){
				if(totales[0] != null)
					totalVencido += (Double) totales[0];
				if(totales[1] != null)
					totalActual += (Double) totales[1];
				if(totales[2] != null)
					total += (Double) totales[2];
			}
			
			PartidaVO partidaVO = (PartidaVO) partida.toVO(0,false);
			partidaVO.setFechaDesdeView(fechaDesdeView);
			partidaVO.setFechaHastaView(fechaHastaView);
			partidaVO.setTotalActView(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalActual, 2)));
			partidaVO.setTotalVenView(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalVencido, 2)));
			partidaVO.setTotalView(StringUtil.formatDoubleWithComa(NumberUtil.truncate(total, 2)));
			
			List<Object[]> listTotales = ImpPar.getListTotalActVenByIdPartidaYFechas(partida.getId(), fechaDesde, fechaHasta);
			for(Object[] totalesPorFecha : listTotales){				
				Date fecha = null;
				Double totalVenPorFecha = 0D;
				Double totalActPorFecha = 0D;
				Double totalPorFecha = 0D;
				if(totalesPorFecha != null){
					if(totalesPorFecha[0] != null)
						fecha = (Date) totalesPorFecha[0];
					if(totalesPorFecha[1] != null)
						totalVenPorFecha += (Double) totalesPorFecha[1];
					if(totalesPorFecha[2] != null)
						totalActPorFecha += (Double) totalesPorFecha[2];
					if(totalesPorFecha[3] != null)
						totalPorFecha += (Double) totalesPorFecha[3];
				}
				//log.debug("---------> Fecha:"+DateUtil.formatDateForReport(fecha)+" Vencido:"+StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalVenPorFecha, 2))+" Actual:"+
				//		StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalActPorFecha, 2))+" Total:"+StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalPorFecha, 2)));
				if(fecha != null){
					FilaVO fila = new FilaVO();
					fila.add(new CeldaVO(DateUtil.formatDateForReport(fecha)));
					fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalVenPorFecha, 2))));
					fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalActPorFecha, 2))));
					fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalPorFecha, 2))));
					
					partidaVO.getListTotalPorFecha().add(fila);
				}
			}
			
			String totalParaPantalla = StringUtil.formatDoubleWithComa(NumberUtil.truncate(total, 2));
			
			// Armar lista de Filas para mostrar totales agrupados por fecha
			
			// Armar el contenedor con los datos del reporte
			InformeTotalPar informe = new InformeTotalPar();
			
			informe.setFechaDesde(fechaDesdeView);
			informe.setFechaHasta(fechaHastaView);
				
			informe.setPartida(partidaVO);
			if(fechaExtra){
				Double totalRangos = total;
				informe.setRangosFechaExtra("true");
				List<PartidaVO> listPartidaVO = new ArrayList<PartidaVO>();
				for(RangoFechaVO rangoFecha: listRangoFecha){
					totalVencido = 0D;
					totalActual = 0D;
					total = 0D;
					totales = ImpPar.getTotalActVenByIdPartidaYFechas(partida.getId(), rangoFecha.getFechaDesde(), rangoFecha.getFechaHasta(), null);
					if(totales != null){
						if(totales[0] != null)
							totalVencido += (Double) totales[0];
						if(totales[1] != null)
							totalActual += (Double) totales[1];
						if(totales[2] != null)
							total += (Double) totales[2];
					}
					partidaVO = (PartidaVO) partida.toVO(0,false);
					partidaVO.setFechaDesdeView(rangoFecha.getFechaDesdeView());
					partidaVO.setFechaHastaView(rangoFecha.getFechaHastaView());
					partidaVO.setTotalActView(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalActual, 2)));
					partidaVO.setTotalVenView(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalVencido, 2)));
					partidaVO.setTotalView(StringUtil.formatDoubleWithComa(NumberUtil.truncate(total, 2)));
					
					List<Object[]> listTotalesExtra = ImpPar.getListTotalActVenByIdPartidaYFechas(partida.getId(), rangoFecha.getFechaDesde(), rangoFecha.getFechaHasta());
					for(Object[] totalesPorFecha : listTotalesExtra){				
						Date fecha = null;
						Double totalVenPorFecha = 0D;
						Double totalActPorFecha = 0D;
						Double totalPorFecha = 0D;
						if(totalesPorFecha != null){
							if(totalesPorFecha[0] != null)
								fecha = (Date) totalesPorFecha[0];
							if(totalesPorFecha[1] != null)
								totalVenPorFecha += (Double) totalesPorFecha[1];
							if(totalesPorFecha[2] != null)
								totalActPorFecha += (Double) totalesPorFecha[2];
							if(totalesPorFecha[3] != null)
								totalPorFecha += (Double) totalesPorFecha[3];
						}
						if(fecha != null){
							FilaVO fila = new FilaVO();
							fila.add(new CeldaVO(DateUtil.formatDateForReport(fecha)));
							fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalVenPorFecha, 2))));
							fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalActPorFecha, 2))));
							fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalPorFecha, 2))));
							
							partidaVO.getListTotalPorFecha().add(fila);
						}
					}
					
					listPartidaVO.add(partidaVO);
					totalRangos += total;
					
				}
				informe.setListPartida(listPartidaVO);
				informe.setTotalRangos(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalRangos, 2)));
			}
			
			// Generamos el printModel
			PrintModel print =null;
			
			print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_TOTAL_PAR);
			
			print.setNoAplicarTrim(true);
			print.putCabecera("TituloReporte", "Consulta de Total por Partida");
			print.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
			print.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
			
			print.putCabecera("Usuario", userName);
			print.setData(informe);
			print.setTopeProfundidad(4);
						
			String fileDir = adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator; 
						
			// Archivo pdf a generar
			String fileNamePdf = adpRun.getId()+"TotalPartida.pdf"; 
			File pdfFile = new File(fileDir+fileNamePdf);
			
			OutputStream out = new java.io.FileOutputStream(pdfFile);
			
			out.write(print.getByteArray());
			
			String nombre = partida.getCodDesPartida();
			String descripcion = totalParaPantalla;
			
			// Guardamos el archivo generado en como archivo de salida del proceso.
			corrida.addOutputFile(nombre, descripcion, fileDir+fileNamePdf);
				
			 if (corrida.hasError()) {
		        	tx.rollback();
		        	if(log.isDebugEnabled()){log.debug(funcName + ": tx.rollback");}
		        	String error = "Error al generar PDF para el formulario";
		        	adpRun.changeState(AdpRunState.FIN_ERROR, error, false);
		        	adpRun.logError(error);
		     } else {
					tx.commit();
					if(log.isDebugEnabled()){log.debug(funcName + ": tx.commit");}
					adpRun.changeState(AdpRunState.FIN_OK, "Reporte Generado Ok", true);
					String adpMessage = "Resultado de la peticion del usuario "+userName
					+" hecha el "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
					adpRun.changeDesCorrida(adpMessage);
			}
		    log.debug(funcName + ": exit");
			
		} catch (Exception e) {
			log.error("Service Error: ",  e);
			if(tx != null) tx.rollback();
			throw new DemodaServiceException(e);
		} finally {
			SiatHibernateUtil.closeSession();
		}
	}
}
