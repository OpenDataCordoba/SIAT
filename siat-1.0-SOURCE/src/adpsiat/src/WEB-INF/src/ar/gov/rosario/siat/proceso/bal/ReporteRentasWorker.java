//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.proceso.bal;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.bal.buss.bean.CuentaBanco;
import ar.gov.rosario.siat.bal.buss.bean.InformeRentas;
import ar.gov.rosario.siat.bal.buss.bean.LeyParAcu;
import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
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
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.PrintModel;

/**
 * Genera los Reporte sobre las Rentas Generales por Partida y por Cuentas Bancaria en forma desconectada.
 * 
 * @author Tecso Coop. Ltda.
 */
public class ReporteRentasWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(ReporteRentasWorker.class);
		
	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
		// Verfica numero paso y estado en adprun,
		// Llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)){ // Paso 1 del Reporte de Rentas: Generar PDF
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
	 *  Genera los Reporte sobre las Rentas Generales por Partida y por Cuentas Bancaria.
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
			
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String ID_BALANCE_PARAM = "idBalance";
			String USER_NAME_PARAM = "UserName";
			String USER_ID_PARAM = "UserId";
	
			AdpParameter paramFechaHasta = adpRun.getAdpParameter(FECHA_HASTA_PARAM);
			AdpParameter paramFechaDesde = adpRun.getAdpParameter(FECHA_DESDE_PARAM);
			AdpParameter paramIdBalance = adpRun.getAdpParameter(ID_BALANCE_PARAM);
			AdpParameter paramUserName = adpRun.getAdpParameter(USER_NAME_PARAM);
			AdpParameter paramUserId = adpRun.getAdpParameter(USER_ID_PARAM);
			
			Date fechaDesde= DateUtil.getDate(paramFechaDesde.getValue(), DateUtil.ddSMMSYYYY_MASK);
			Date fechaHasta= DateUtil.getDate(paramFechaHasta.getValue(), DateUtil.ddSMMSYYYY_MASK);
			Long idBalance = null;
			if(paramIdBalance != null){
				idBalance = Long.valueOf(paramIdBalance.getValue());
			}
			String userName = paramUserName.getValue();
			String userId = paramUserId.getValue();
			
			// Seteamos el UserName en el UserContext para que al modificarse la corrida quede grabado.
			DemodaUtil.currentUserContext().setUserName(userName);
			
			// Levanto la Corrida
			Corrida corrida = Corrida.getByIdNull(adpRun.getId());
			if(corrida == null){
		     	adpRun.changeState(AdpRunState.FIN_ERROR, "Error al leer la corrida del proceso", false);
            	adpRun.logError("Error al leer la corrida del proceso");
            	return;
			}

			
			InformeRentas informe = new InformeRentas();
			
			informe.setFechaReporte(DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
			informe.setFechaDesde(DateUtil.formatDate(fechaDesde, DateUtil.dd_MM_YYYY_MASK));
			informe.setFechaHasta(DateUtil.formatDate(fechaHasta, DateUtil.dd_MM_YYYY_MASK));
			if(idBalance != null){
				informe.setNroBalance(idBalance.toString());				
			}
			
			List<Object[]> listTotalesPorPartida = BalDAOFactory.getImpParDAO().getTotalesPorPartidas(fechaDesde, fechaHasta, idBalance); 
			List<FilaVO> listFilaPartida = new ArrayList<FilaVO>();
			List<FilaVO> listFilaCuenta = new ArrayList<FilaVO>();
			Double total = 0D;
			Double totalEjeAct = 0D;
			Double totalEjeVen = 0D;
			Map<String, Object[]> mapFilaCuenta = new HashMap<String, Object[]>();
			Map<String, Object[]> mapFilaTotales = new HashMap<String, Object[]>();
			for(Object[] object: listTotalesPorPartida){
				FilaVO fila = new FilaVO();
				String codPartida = StringUtil.completarCerosIzq(((String) object[0]).trim(),5); 
				fila.add(new CeldaVO(codPartida));
				fila.add(new CeldaVO((String) object[1]));
				fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate((Double) object[2],2))));
				fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate((Double) object[3],2))));
				fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(((Double) object[2])+((Double) object[3]),2))));
				if((NumberUtil.truncate((Double) object[2],2)+NumberUtil.truncate((Double) object[3],2))!=0D){
					listFilaPartida.add(fila);	
					totalEjeVen += (Double) object[2];
					totalEjeAct += (Double) object[3];
					total += (Double) object[2]+(Double) object[3];					
					// Totalizador por los 3 primeros digitos
					String codTotalParcial = codPartida.substring(0,3); 
					Object[] objTotPar = null; 
					objTotPar = mapFilaTotales.get(codTotalParcial);
					if(objTotPar == null){
						objTotPar = new Object[3];
						objTotPar[0] = codTotalParcial;
						objTotPar[1] = 0D;
						objTotPar[2] = 0D;
					}
					objTotPar[1] = (Double) objTotPar[1] + (Double) object[2];
					objTotPar[2] = (Double) objTotPar[2] + (Double) object[3];
					mapFilaTotales.put(codTotalParcial, objTotPar);
					// Totalizador por los 2 primeros digitos
					codTotalParcial = codPartida.substring(0,2); 
					objTotPar = null;
					objTotPar = mapFilaTotales.get(codTotalParcial);
					if(objTotPar == null){
						objTotPar = new Object[3];
						objTotPar[0] = codTotalParcial;
						objTotPar[1] = 0D;
						objTotPar[2] = 0D;
					}
					objTotPar[1] = (Double) objTotPar[1] + (Double) object[2];
					objTotPar[2] = (Double) objTotPar[2] + (Double) object[3];
					mapFilaTotales.put(codTotalParcial, objTotPar);
					// Totalizador por los 1 primeros digitos
					codTotalParcial = codPartida.substring(0,1); 
					objTotPar = null;
					objTotPar = mapFilaTotales.get(codTotalParcial);
					if(objTotPar == null){
						objTotPar = new Object[3];
						objTotPar[0] = codTotalParcial;
						objTotPar[1] = 0D;
						objTotPar[2] = 0D;
					}
					objTotPar[1] = (Double) objTotPar[1] + (Double) object[2];
					objTotPar[2] = (Double) objTotPar[2] + (Double) object[3];
					mapFilaTotales.put(codTotalParcial, objTotPar);
					
					Partida partida = Partida.getByCod(((String) object[0]).trim());
					if(partida != null){
						CuentaBanco cuentaBanco = partida.getCuentaBancoVigente(fechaDesde);
						Object[] objCta;
						String nroCuenta = "";
						String desBanco = "No existe en SIAT";
						if(cuentaBanco != null){
							if(cuentaBanco.getBanco() != null)
								desBanco = cuentaBanco.getBanco().getDesBanco();
							nroCuenta = cuentaBanco.getNroCuenta();
						}else{
							nroCuenta = "No existe cuenta bancaria relacionada";
							desBanco = " - ";
						}
						
						objCta = mapFilaCuenta.get(nroCuenta);
						if(objCta == null){
							objCta = new Object[4];
							objCta[0] = nroCuenta;
							objCta[1] = desBanco;
							objCta[2] = 0D;
							objCta[3] = 0D;
						}
						objCta[2] = (Double) objCta[2]+(Double) object[2];
						objCta[3] = (Double) objCta[3]+(Double) object[3];
						mapFilaCuenta.put(nroCuenta, objCta);
					}
				}
			}
			// Agregar Filas de Totales y calcula sub total 0+1+2+3+4+5+6+7 (este total es requerido por el area de Balance)
			Double subTotal = 0D;
			Double subTotalEjeAct = 0D;
			Double subTotalEjeVen = 0D;
			for(Object[] object: mapFilaTotales.values()){
				if(NumberUtil.truncate(((Double) object[1]+(Double) object[2]), 2)!=0D){
					 FilaVO fila = new FilaVO();
					 String codLeyParAcu = (String) object[0];
					 fila.add(new CeldaVO(codLeyParAcu));
					 LeyParAcu leyParAcu = LeyParAcu.getByCod(codLeyParAcu);
					 if(leyParAcu != null)
						 fila.add(new CeldaVO(leyParAcu.getDescripcion()));
					 else
						 fila.add(new CeldaVO(""));
					 fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(((Double) object[1]), 2))));
					 fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(((Double) object[2]), 2))));
					 fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(((Double) object[1]+(Double) object[2]), 2))));
					 listFilaPartida.add(fila);
					 if("0".equals(codLeyParAcu) || "1".equals(codLeyParAcu) || "2".equals(codLeyParAcu) || "3".equals(codLeyParAcu) || "4".equals(codLeyParAcu)
							 || "5".equals(codLeyParAcu) || "6".equals(codLeyParAcu) || "7".equals(codLeyParAcu)){
						 subTotal += NumberUtil.truncate(((Double) object[1]+(Double) object[2]), 2);
						 subTotalEjeVen += NumberUtil.truncate(((Double) object[1]), 2);
						 subTotalEjeAct += NumberUtil.truncate(((Double) object[2]), 2);
					 }
				}
			}
			listFilaPartida = this.ordenarListaPartida(listFilaPartida);

			// Agregar Fila Total al principio
			FilaVO fila = new FilaVO();
			fila.add(new CeldaVO(""));
			fila.add(new CeldaVO("TOTAL RECAUDADO"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalEjeVen,2))));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalEjeAct,2))));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(total,2))));
			listFilaPartida.add(0,fila);
			
			// Agregar Fila subTotal en la segunda fila. (Bajo el Total Recaudado)
			fila = new FilaVO();
			fila.add(new CeldaVO(""));
			fila.add(new CeldaVO("SUBTOTAL REC. 0+1+2+3+4+5+6+7"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(subTotalEjeVen,2))));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(subTotalEjeAct,2))));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(subTotal,2))));
			listFilaPartida.add(1,fila);
			
			for(Object[] object: mapFilaCuenta.values()){
				if(NumberUtil.truncate((Double) object[2],2)+NumberUtil.truncate((Double) object[3],2)!=0D){
					 fila = new FilaVO();
					 fila.add(new CeldaVO((String) object[0]));
					 fila.add(new CeldaVO((String) object[1]));
					 //fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate((Double) object[2],2))));
					 //fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate((Double) object[3],2))));
					 fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(((Double) object[2])+((Double) object[3]),2))));
					 listFilaCuenta.add(fila);
				}
			}
			
			fila = new FilaVO();
			fila.add(new CeldaVO("TOTAL RECAUDADO"));
			fila.add(new CeldaVO(""));
			//fila.add(new CeldaVO(""));
			//fila.add(new CeldaVO(""));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(total,2))));
			listFilaCuenta.add(0,fila);
			
			informe.setListPartidasOCuentas(listFilaPartida);
		
			// Generamos el printModel
			PrintModel print =null;
			
			print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_RENTAS);
			
			print.putCabecera("TituloReporte", "Totales por Partidas");
			print.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
			print.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
			
			print.putCabecera("Usuario", userName);
			print.setData(informe);
			print.setTopeProfundidad(2);
						
			String fileDir = adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator; 
						
			// Archivo pdf a generar
			String fileNamePdf = adpRun.getId()+"TotalPartidaRentas"+ userId +".pdf"; 
			File pdfFile = new File(fileDir+fileNamePdf);
			
			OutputStream out = new java.io.FileOutputStream(pdfFile);
			
			out.write(print.getByteArray());
			
			String nombre = "Reporte de Totales por Partidas";
			String descripcion = "Consulta de Totales por Partidas sobre Maestros de Rentas.";
			
			// Guardamos el archivo generado en como archivo de salida del proceso.
			corrida.addOutputFile(nombre, descripcion, fileDir+fileNamePdf);
					
			// Generamos el reporte por de Totales por Cuenta Bancarias.
					
			informe = new InformeRentas();
			
			informe.setFechaReporte(DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
			informe.setFechaDesde(DateUtil.formatDate(fechaDesde, DateUtil.dd_MM_YYYY_MASK));
			informe.setFechaHasta(DateUtil.formatDate(fechaHasta, DateUtil.dd_MM_YYYY_MASK));

			informe.setListPartidasOCuentas(listFilaCuenta);
			informe.setReporteCuenta("true");
			
			if(idBalance != null){
				informe.setNroBalance(idBalance.toString());				
			}
			
			// Generamos el printModel
			print =null;
			
			print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_RENTAS);
			
			print.setNoAplicarTrim(true);
			print.putCabecera("TituloReporte", "Reporte de Totales por Cuentas Bancarias");
			print.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
			print.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
			
			print.putCabecera("Usuario", userName);
			print.setData(informe);
			print.setTopeProfundidad(2);
						
			// Archivo pdf a generar
			fileNamePdf = adpRun.getId()+"TotalCuentaRentas"+ userId +".pdf"; 
			pdfFile = new File(fileDir+fileNamePdf);
			
			out = new java.io.FileOutputStream(pdfFile);
			
			out.write(print.getByteArray());
			
			nombre = "Reporte de Totales por Cuentas Bancarias";
			descripcion = "Consulta de Totales por Cuentas Bancarias sobre Maestros de Rentas.";
			
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
	
	
	public List<FilaVO> ordenarListaPartida(List<FilaVO> listFila){
    	Comparator<FilaVO> comparator = new Comparator<FilaVO>(){
			public int compare(FilaVO fila1, FilaVO fila2) {
				String codPartidaFila1 = fila1.getListCelda().get(0).getValor();
				String codPartidaFila2 = fila2.getListCelda().get(0).getValor();
				return codPartidaFila1.compareTo(codPartidaFila2);
			}    		
    	};    	
    	Collections.sort(listFila, comparator);
    	return listFila;
    }
}
