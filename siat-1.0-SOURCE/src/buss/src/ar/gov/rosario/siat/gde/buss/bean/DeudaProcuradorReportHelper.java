//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;

/**
 * Helper para realizar el reporte de Recaudado NUEVO
 * @author alejandro
 *
 */
public class DeudaProcuradorReportHelper {

	private Log log = LogFactory.getLog(DeudaProcuradorReportHelper.class);
	
	public static final int TIPO_REPORTE_RESUMIDO =0;
	public static final int TIPO_REPORTE_DETALLADO =1;
	
	private Procurador procurador;
	private Date fechaVtoDesde;
	private Date fechaVtoHasta;	
	
	
	List<FilaReporteDeudaProcurador> listFilas = new ArrayList<FilaReporteDeudaProcurador>();

	private int contador;
	
	public DeudaProcuradorReportHelper(Procurador procurador, Date fechaVtoDesde, Date fechaVtoHasta) {
		super();
		this.procurador = procurador;
		this.fechaVtoDesde = fechaVtoDesde;
		this.fechaVtoHasta = fechaVtoHasta;		
	}

	// Getters y setters
	public Date getFechaDesde() {
		return fechaVtoDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaVtoDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaVtoHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaVtoHasta = fechaHasta;
	}

	public List<FilaReporteDeudaProcurador> getListFilaReporte() {
		return listFilas;
	}


	// metodos para generar el reporte
	/**
	 * Genera la lista de filas para los reportes.<br>
	 * Cada fila: procurador-zona-seccion-nrocuenta-periodo-importeHist-importeAct-saldoHist-saldoAct
	 */
	public void generarListaFilasReporte() throws Exception{
		log.debug("generarListaFilasReporte - enter");
		
		Long idProcurador = procurador!=null?procurador.getId():null;
				
		Integer firstResult = 0;
		contador=1;		
		Integer maxResults = 2000;
		boolean salir = false;
		
		// con la consulta posta
		boolean tieneDeuJudicial = true;
		boolean tieneDeuCancel = true;

		while(!salir){
			
			List<Object[]> listDeudaJudicial = new ArrayList<Object[]>();
			List<Object[]> listDeudaCancel = new ArrayList<Object[]>();
			
			if(tieneDeuJudicial){// cuando venga vacia la lista, no vuelve a entrar, ya que no hay mas deudas
				listDeudaJudicial = GdeDAOFactory.getDeudaJudicialDAO().getList4DeuProReport("gde_deudajudicial"
										, idProcurador, fechaVtoDesde,fechaVtoHasta, firstResult, maxResults);
				tieneDeuJudicial = listDeudaJudicial.size()>0;				
			}			
			
			// fede: bug 696. lo solucionamos rapidamente asi, aunque es probable que este reporte
			// sea usado por otras areas y requiera esta funcionalidad. 
			// si ocurre esto, seria bueno ver que hacer para cubrir ambos requerimientos.
			tieneDeuCancel = false;
			if(tieneDeuCancel){// cuando venga vacia la lista, no vuelve a entrar, ya que no hay mas deudas
				listDeudaCancel = GdeDAOFactory.getDeudaCanceladaDAO().getList4DeuProReport("gde_deudaCancelada"
										, idProcurador,	fechaVtoDesde,fechaVtoHasta, firstResult, maxResults);
				tieneDeuCancel = listDeudaCancel.size()>0;				
			}			
			
			if(tieneDeuJudicial || tieneDeuCancel){
				// junta las 2 listas y llama al procesarListResult
				List<Object[]> listResult = new ArrayList<Object[]>();
				if(listDeudaJudicial!=null)listResult.addAll(listDeudaJudicial);
				if(listDeudaCancel!=null)listResult.addAll(listDeudaCancel);
				procesarListResult(listResult);
								
				SiatHibernateUtil.closeSession();
				firstResult += maxResults; // Incremento el indice del 1er registro
			}else{
				// no tiene ninguna deuda, sale del while
				salir = true;
			}
			
		}
		
		ordenarLista();
			
	}
	
	private void procesarListResult(List<Object[]>listResult) throws Exception{
		for(Object[] obj: listResult){

			Long idProcurador = new Long((Integer)obj[0]);
			Long idDeuda = new Long((Integer)obj[1]);
			String valorTipObjImpZona = (String)obj[2];
			String valorTipObjImpSeccion = (String)obj[3];			
			
			log.debug("Procesando deuda:"+idDeuda+"    - contador:"+(contador++) +" ----------------------------------------------------------------------------------------------");

			FilaReporteDeudaProcurador fila = new FilaReporteDeudaProcurador();
			Deuda deuda = Deuda.getById(idDeuda);
			
			fila.setIdProcurador(idProcurador.toString());
			fila.setDesProcurador(Procurador.getById(idProcurador).getDescripcion());
			
			fila.setZona(valorTipObjImpZona);
			fila.setSeccion(valorTipObjImpSeccion);				
			fila.setNroCuenta(deuda.getCuenta().getNumeroCuenta());
			fila.setStrPeriodoAnio(deuda.getStrPeriodo());
			
			// calcula los valores actualizados e hist.
			fila.setImporteAct(deuda.actualizacionImporte(new Date()).getImporteAct());
			fila.setImportehist(deuda.getImporte());
			
			Double saldo = deuda.getSaldo();
			if(saldo>0)
				fila.setSaldoAct(deuda.actualizacionSaldo(new Date()).getImporteAct());
			else
				fila.setSaldoAct(0D);
			
			fila.setSaldoHist(saldo);
			
			listFilas.add(fila);
		}
	}

	private void ordenarLista() {
		// ordena la lista, ya que si bien vienen ordenadas por tabla, puede ser que quede desordenada entre las tablas
    	Comparator<FilaReporteDeudaProcurador> comparatorKey = new Comparator<FilaReporteDeudaProcurador>(){    		
			public int compare(FilaReporteDeudaProcurador fila1, FilaReporteDeudaProcurador fila2) { //formato str: periodo/anio
				Long idProcurador1 = Long.parseLong(fila1.getIdProcurador());
				Integer zona1 = new Integer(fila1.getZona());
				Integer seccion1 = new Integer(fila1.getSeccion());
				
				Long idProcurador2 = Long.parseLong(fila2.getIdProcurador());
				Integer zona2 = new Integer(fila2.getZona());
				Integer seccion2 = new Integer(fila2.getSeccion());
				
				// Se compara el procurador
				if(idProcurador1>idProcurador2){
					return 1;
				}else if(idProcurador1<idProcurador2){
					return -1;
				}else{
					// Se compara la zona
					if(zona1>zona2){
						return 1;
					}else if(zona1<zona2){
						return -1;
					}else{
						// se compara la seccion
						if(seccion1>seccion2){
							return 1;
						}else if(seccion1<seccion2){
							return -1;
						}	
					}
					
				}
				//Si son iguales
				return 0;
			}    		
    	};
    	
    	Collections.sort(listFilas, comparatorKey);
	}
}
