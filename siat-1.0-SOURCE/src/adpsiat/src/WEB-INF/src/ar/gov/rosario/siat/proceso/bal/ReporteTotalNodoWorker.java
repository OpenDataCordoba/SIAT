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

import ar.gov.rosario.siat.bal.buss.bean.InformeConsultaNodo;
import ar.gov.rosario.siat.bal.buss.bean.Nodo;
import ar.gov.rosario.siat.bal.iface.model.NodoVO;
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
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.Datum;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.RangoFechaVO;
import coop.tecso.demoda.iface.model.TablaVO;

/**
 * Genera el Reporte de Total por Nodo en forma desconectada.
 * 
 * @author Tecso Coop. Ltda.
 */
public class ReporteTotalNodoWorker implements AdpWorker {

	private static Logger log = Logger.getLogger(ReporteTotalNodoWorker.class);
		
	public void cancel(AdpRun adpRun) throws Exception {
	}

	public void execute(AdpRun adpRun) throws Exception {
		// Verfica numero paso y estado en adprun,
		// Llama a cada metodo segun el numero de paso
		Long pasoActual = adpRun.getPasoActual();
		if (pasoActual.equals(1L)){ // Paso 1 del Reporte de Total por Nodo: Generar PDF
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
	 *  Generar Reporte de Total por Nodo (PDF).
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
			
			String ID_NODO_PARAM = "idNodo";
			String FECHA_DESDE_PARAM = "fechaDesde";
			String FECHA_HASTA_PARAM = "fechaHasta";
			String FECHA_EXTRA_PARAM = "rangoExtra";

			String ESPECIAL_PARAM = "especial";
			
			String USER_NAME_PARAM = "UserName";
	
			AdpParameter paramIdNodo = adpRun.getAdpParameter(ID_NODO_PARAM);
			AdpParameter paramFechaHasta = adpRun.getAdpParameter(FECHA_HASTA_PARAM);
			AdpParameter paramFechaDesde = adpRun.getAdpParameter(FECHA_DESDE_PARAM);
			AdpParameter paramUserName = adpRun.getAdpParameter(USER_NAME_PARAM);
			AdpParameter paramFechaExtra = adpRun.getAdpParameter(FECHA_EXTRA_PARAM);
			AdpParameter paramEspecial = adpRun.getAdpParameter(ESPECIAL_PARAM);
			
			Long idNodo = NumberUtil.getLong(paramIdNodo.getValue());
			String fechaDesdeView = paramFechaDesde.getValue();
			String fechaHastaView = paramFechaHasta.getValue();
			Date fechaDesde= DateUtil.getDate(fechaDesdeView, DateUtil.ddSMMSYYYY_MASK);
			Date fechaHasta= DateUtil.getDate(fechaHastaView, DateUtil.ddSMMSYYYY_MASK);
			boolean fechaExtra = "true".equals(paramFechaExtra.getValue());
			boolean especial = "true".equals(paramEspecial.getValue());
			String userName = paramUserName.getValue();
		
			String fechasDesdeExtras = "";
			String fechasHastaExtras = "";
			List<RangoFechaVO> listRangoFecha = new ArrayList<RangoFechaVO>();
			Integer nivelHasta = null;
			if(especial){
				String NIVELHASTA_PARAM = "nivelHasta";
				AdpParameter paramNivelHasta = adpRun.getAdpParameter(NIVELHASTA_PARAM);
				nivelHasta = NumberUtil.getInt(paramNivelHasta.getValue());

				RangoFechaVO rangoFecha = new RangoFechaVO();
				rangoFecha.setIndice("0");
				rangoFecha.setFechaDesde(fechaDesde);
				rangoFecha.setFechaHasta(fechaHasta);
				listRangoFecha.add(0,rangoFecha);
			}
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
					rangoFecha.setIndice(String.valueOf(i+1));
					rangoFecha.setFechaDesde(DateUtil.getDate(fechaDesdeDatum.getCols(i)));
					rangoFecha.setFechaHasta(DateUtil.getDate(fechaHastaDatum.getCols(i)));
					listRangoFecha.add(rangoFecha);
				}
			}
			
			// Seteamos el UserName en el UserContext para que al modificarse la corrida quede grabado.
			DemodaUtil.currentUserContext().setUserName(userName);
			
			Nodo nodo = Nodo.getByIdNull(idNodo);
			if(nodo == null){
		     	adpRun.changeState(AdpRunState.FIN_ERROR, "Error al obtener el nodo", false);
            	adpRun.logError("Error al obtener el nodo");
            	return;
			}
					
			// Levanto la Corrida
			Corrida corrida = Corrida.getByIdNull(adpRun.getId());
			if(corrida == null){
		     	adpRun.changeState(AdpRunState.FIN_ERROR, "Error al leer la corrida del proceso", false);
            	adpRun.logError("Error al leer la corrida del proceso");
            	return;
			}
			
			// Obtiene la Clave y Calcula los Totales
			nodo.obtenerClave();
			nodo.calcularTotal(fechaDesde, fechaHasta);
			
			TablaVO tablaReporteEspecial = null;
			if(especial){
				nodo.calcularTotalEnRangos(listRangoFecha);

				tablaReporteEspecial = new TablaVO("tablaReporteEspecial");
				// Fila Cabecera
				FilaVO filaCabecera = new FilaVO();
				filaCabecera.add(new CeldaVO("COD. Rubro"));
				filaCabecera.add(new CeldaVO("DESCRIPCION"));
				for(RangoFechaVO rangoFecha: listRangoFecha){
					filaCabecera.add(new CeldaVO(rangoFecha.getFechaDesdeView()+" a "+rangoFecha.getFechaHastaView()));
				}
				tablaReporteEspecial.setFilaCabecera(filaCabecera);
				// Cargar Listas 
				List<FilaVO> listFila = this.armarTablaReporteEspecial(nodo,nodo.getNivel(), nivelHasta); 
				for(FilaVO fila: listFila)
					tablaReporteEspecial.add(fila);
			}
			
			// Armar el contenedor con los datos del reporte
			InformeConsultaNodo informe = new InformeConsultaNodo();
			
			informe.setFechaDesde(fechaDesdeView);
			informe.setFechaHasta(fechaHastaView);
			informe.setClasificador(nodo.getClasificador().getDescripcion());
			informe.setNivel(nodo.getNivel().toString());
	
			informe.setNodo((NodoVO) nodo.toVO(1,false));
			if(especial){
				informe.setTablaReporteEspecial(tablaReporteEspecial);
				informe.setEspecial("true");
			}
			if(fechaExtra && !especial){
				Double totalRangos = nodo.getTotal();
				informe.setRangosFechaExtra("true");
				List<NodoVO> listNodoVO = new ArrayList<NodoVO>();
				for(RangoFechaVO rangoFecha: listRangoFecha){
					nodo.calcularTotal(rangoFecha.getFechaDesde(), rangoFecha.getFechaHasta());
					NodoVO nodoVO = (NodoVO) nodo.toVO(1,false);
					nodoVO.setFechaDesdeView(rangoFecha.getFechaDesdeView());
					nodoVO.setFechaHastaView(rangoFecha.getFechaHastaView());
					listNodoVO.add(nodoVO);
					totalRangos += nodo.getTotal();
				}
				informe.setListNodo(listNodoVO);
				informe.setTotalRangos(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalRangos, 2)));
			}
			
			// Generamos el printModel
			PrintModel print =null;
			
			if(especial){
				print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_TOTAL_NODO_ESPECIAL); 
			}else{
				print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_TOTAL_NODO);				
			}
			
			print.setNoAplicarTrim(true);
			print.putCabecera("TituloReporte", "Consulta de Total por Nodo");
			print.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
			print.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
			
			print.putCabecera("Usuario", userName);
			print.setData(informe);

			if(especial){
				print.setTopeProfundidad(4);				
			}else{
				print.setTopeProfundidad(2);
			}
						
			String fileDir = adpRun.getProcessDir(AdpRunDirEnum.SALIDA)+File.separator; 
						
			// Archivo pdf a generar
			String fileNamePdf = adpRun.getId()+"ConsultaNodo.pdf"; 
			File pdfFile = new File(fileDir+fileNamePdf);
			
			OutputStream out = new java.io.FileOutputStream(pdfFile);
			
			out.write(print.getByteArray());
			
			String nombre = nodo.getClave()+" - "+nodo.getDescripcion();
			String descripcion = StringUtil.formatDoubleWithComa(nodo.getTotal());
			
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
	
	
	public List<FilaVO> armarTablaReporteEspecial(Nodo nodo, Integer nivelDesde, Integer nivelHasta){
		nodo.obtenerClave();
		List<FilaVO> listFila = new ArrayList<FilaVO>();
		if(!"".equals(nodo.getDescripcion().trim())){
			// Agregar nodo principal
			FilaVO fila = new FilaVO();
			CeldaVO celda = new CeldaVO(nodo.getClave());
			celda.setTextAlignLeft();
			fila.add(celda);
			celda = new CeldaVO(nodo.obtenerDescripcionTab(nivelDesde));
			celda.setTextAlignLeft();
			fila.add(celda);
			for(Double totalRango: nodo.getListTotalRango()){
				celda =new CeldaVO(StringUtil.formatDoubleWithComa(totalRango));
				celda.setTextAlignRight();
				fila.add(celda);
			}
			listFila.add(fila);			
		}
		if(nodo.getNivel().intValue() < nivelHasta.intValue()){
			// Recorrer lista de nodos hijos y llamar a la funciona para agregar las filas
			for(Nodo nodoHijo: nodo.getListNodoHijo()){
				List<FilaVO> listFilasNodoHijo = this.armarTablaReporteEspecial(nodoHijo,nivelDesde,nivelHasta);
				if(!ListUtil.isNullOrEmpty(listFilasNodoHijo))
					listFila.addAll(listFilasNodoHijo);
			}			
		}
		
		return  listFila;
	}
	
	
}
