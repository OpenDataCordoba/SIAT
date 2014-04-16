//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;

/**
 * Helper para realizar el reporte de Recaudado NUEVO
 * @author alejandro
 *
 */
public class RecaudadoReportHelper {

	private Log log = LogFactory.getLog(RecaudadoReportHelper.class);
	
	private static final int TIPO_DEUDA_EMITIDA =0;
	private static final int TIPO_DEUDA_PAGADA_ANTES_VTO =1;
	private static final int TIPO_DEUDA_PAGADA_DESPUES_VTO =2;
	private static final int TIPO_DEUDA_IMPAGA_EN_CONVENIO =3;
	private static final int TIPO_DEUDA_PAGA_EN_CONVENIO =4;
	private static final int TIPO_DEUDA_ANULADA =5;
	private static final int TIPO_DEUDA_EN_CONVENIO =6;
	
	private Recurso recurso;
	private Date fechaDesde;
	private Date fechaHasta;
	private Date fechaPagoHasta;
	
	/** Contiene la lista de periodos de deuda con los totales: pagos antes del Vto, despues del Vto., los impagos y los % de cada uno respecto de lo emitido
	 *  estructura del map : "periodo/anio", [totalDeudaEmitida, totalDeudaPagadaAntesVto,totalDeudaPagadaDespuesVto ]
	 */
	private Map<String, Double[]> mapPeriodosDeudas = new LinkedHashMap<String, Double[]>();

	
	public RecaudadoReportHelper(Recurso recurso, Date fechaDesde, Date fechaHasta, Date fechaPagoHasta) {
		super();
		this.recurso = recurso;
		this.fechaDesde = fechaDesde;
		this.fechaHasta = fechaHasta;
		this.fechaPagoHasta = fechaPagoHasta;
	}

	// Getters y setters
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}


	public Map<String, Double[]> getMapPeriodosDeudas() {
		return mapPeriodosDeudas;
	}

	public void setMapPeriodosDeudas(Map<String, Double[]> mapDeudas) {
		this.mapPeriodosDeudas = mapDeudas;
	}

	// metodos para generar el reporte
	/**
	 * Genera la lista de periodos (en el mapPeriodosDeuda) de deuda con los totales: pagos antes del Vto, despues del Vto., los impagos y los % de cada uno respecto de lo emitido
	 */
	public void generarPeriodosDeudas(){	
		
	/*	log.debug("Consultando DEUDA EMITIDA -------------------------------------------------");
		Object[] obj = {01, 2008, new BigDecimal(1300.50)};
		Object[] obj1 = {02, 2008, new BigDecimal(6000.00)};
		Object[] obj2 = {03, 2008, new BigDecimal(7000.80)};
		List<Object[]> listObj = new ArrayList<Object[]>();
		listObj.add(obj);
		listObj.add(obj1);
		listObj.add(obj2);
		agregarDeuda(listObj, TIPO_DEUDA_EMITIDA);
		
		log.debug("Consultando DEUDA CON PAGO ANTES DEL VTO -------------------------------------------------");
		Object[] obj3 = {01, 2008, new BigDecimal(600.00)};
		Object[] obj4 = {02, 2006, new BigDecimal(200.00)};
		Object[] obj5 = {03, 2008, new BigDecimal(1000.00)};
		List<Object[]> listObj1 = new ArrayList<Object[]>();
		listObj1.add(obj3);
		listObj1.add(obj4);
		listObj1.add(obj5);
		agregarDeuda(listObj1, TIPO_DEUDA_PAGADA_ANTES_VTO);
		
		log.debug("Consultando DEUDA CON PAGO VENCIDO -------------------------------------------------");
		Object[] obj6 = {01, 2008, new BigDecimal(700.50)};
		Object[] obj7 = {02, 2008, new BigDecimal(500)};
		Object[] obj8 = {03, 2005, new BigDecimal(3000.80)};
		List<Object[]> listObj2 = new ArrayList<Object[]>();
		listObj2.add(obj6);
		listObj2.add(obj7);
		listObj2.add(obj8);
		agregarDeuda(listObj2, TIPO_DEUDA_PAGADA_DESPUES_VTO);
		*/
		log.debug("Consultando DEUDA EMITIDA ------------------------------------------------------------------------");
		buscarDeudas(TIPO_DEUDA_EMITIDA);
		log.debug("Consultando DEUDA CON PAGO ANTES DEL VTO ---------------------------------------------------------");
		buscarDeudas(TIPO_DEUDA_PAGADA_ANTES_VTO);
		log.debug("Consultando DEUDA CON PAGO VENCIDO ---------------------------------------------------------------");
		buscarDeudas(TIPO_DEUDA_PAGADA_DESPUES_VTO);
		log.debug("Consultando DEUDA IMPAGA EN CONVENIO TENIENDO EN CUENTA DESCUENTOS -------------------------------");
		buscarDeudas(TIPO_DEUDA_IMPAGA_EN_CONVENIO);
		log.debug("Consultando DEUDA PAGA EN CONVENIO TENIENDO EN CUENTA DESCUENTOS ---------------------------------");
		buscarDeudas(TIPO_DEUDA_PAGA_EN_CONVENIO);
		log.debug("Consultando DEUDA PAGA EN CONVENIO TENIENDO EN CUENTA DESCUENTOS ---------------------------------");
		buscarDeudas(TIPO_DEUDA_EN_CONVENIO);
		log.debug("Consultando DEUDA ANULADA ------------------------------------------------------------------------");
		buscarDeudas(TIPO_DEUDA_ANULADA);
		
		ordenarLista();
	}
	
	private void buscarDeudas(int tipoDeuda){
		Boolean pagadasAntesVto = null;
		Boolean aplicarFechaHasta = false;
		/*if(tipoDeuda==TIPO_DEUDA_EMITIDA)
			pagadasAntesVto=null;
		else if(tipoDeuda==TIPO_DEUDA_PAGADA_ANTES_VTO)
			pagadasAntesVto=true;
		else
			pagadasAntesVto=false;
		*/
		if (tipoDeuda == TIPO_DEUDA_PAGADA_ANTES_VTO) {
			pagadasAntesVto = true;
			aplicarFechaHasta = true;
		}
		else if (tipoDeuda == TIPO_DEUDA_PAGADA_DESPUES_VTO)
		{
			pagadasAntesVto = false;
			aplicarFechaHasta = true;
		}
		
		Boolean deudaEnConvenio = false;
		Boolean deudaAnulada = false;
		Boolean deudaImpagaEnConvenio = null;
		
		if (tipoDeuda == TIPO_DEUDA_PAGA_EN_CONVENIO)
		{	
			deudaEnConvenio = true;
			deudaImpagaEnConvenio = false;
		}
		if (tipoDeuda == TIPO_DEUDA_IMPAGA_EN_CONVENIO)
		{	
			deudaEnConvenio = true;
			deudaImpagaEnConvenio = true;
		}		
		if (tipoDeuda == TIPO_DEUDA_EN_CONVENIO)
		{	
			deudaEnConvenio = true;
		}
		if (tipoDeuda == TIPO_DEUDA_ANULADA)
			deudaAnulada = true;
		 
		
		// busca deudas emitidas en administrativas
		agregarDeuda(GdeDAOFactory.getDeudaDAO().getDeudaEmiAgrupAnioPer(recurso.getId(),
				"gde_deudaadmin",fechaDesde, fechaHasta, fechaPagoHasta, aplicarFechaHasta, pagadasAntesVto,deudaEnConvenio, deudaAnulada, deudaImpagaEnConvenio), tipoDeuda);

		// busca deudas emitidas en judicial
		agregarDeuda(GdeDAOFactory.getDeudaDAO().getDeudaEmiAgrupAnioPer(recurso.getId(),
				"gde_deudaJudicial",fechaDesde, fechaHasta,fechaPagoHasta, aplicarFechaHasta, pagadasAntesVto,deudaEnConvenio, deudaAnulada, deudaImpagaEnConvenio), tipoDeuda);
		
		// busca deudas emitidas en cancelada
		agregarDeuda(GdeDAOFactory.getDeudaDAO().getDeudaEmiAgrupAnioPer(recurso.getId(),
				"gde_deudaCancelada",fechaDesde, fechaHasta, fechaPagoHasta, aplicarFechaHasta, pagadasAntesVto,deudaEnConvenio, deudaAnulada, deudaImpagaEnConvenio), tipoDeuda);
		
		// busca deudas emitidas en anulada
		agregarDeuda(GdeDAOFactory.getDeudaDAO().getDeudaEmiAgrupAnioPer(recurso.getId(),
				"gde_deudaanulada",fechaDesde, fechaHasta, fechaPagoHasta, aplicarFechaHasta, pagadasAntesVto,deudaEnConvenio, deudaAnulada, deudaImpagaEnConvenio), tipoDeuda);
		
	}

	private void ordenarLista() {
		// ordena la lista, ya que si bien vienen ordenadas por tabla, puede ser que quede desordenada por periodos de otras tablas
    	Comparator<String> comparatorKey = new Comparator<String>(){    		
			public int compare(String str1, String str2) { //formato str: periodo/anio
				int anio1 = Integer.parseInt(str1.split("/")[1]);
				int periodo1 = Integer.parseInt(str1.split("/")[0]);
				
				int anio2 = Integer.parseInt(str2.split("/")[1]);
				int periodo2 = Integer.parseInt(str2.split("/")[0]);
				
				// Se compara el anio
				if(anio1>anio2){
					return 1;
				}else if(anio1<anio2){
					return -1;
				}else{
					// Se compara el periodo
					if(periodo1>periodo2){
						return 1;
					}else if(periodo1<periodo2){
						return -1;
					}													
				}
				//Si son iguales
				return 0;
			}    		
    	};
    	
    	Set<String> setKeys = mapPeriodosDeudas.keySet();
    	List<String> listKeys = new ArrayList<String>();
    	for(String key:setKeys){
    		listKeys.add(key);
    	}
    	Collections.sort(listKeys, comparatorKey);
    	Map<String, Double[]> mapPeriodosDeudasTmp = new LinkedHashMap<String, Double[]>();
    	for(String key:listKeys){
    		mapPeriodosDeudasTmp.put(key, mapPeriodosDeudas.get(key));
    	}
    	mapPeriodosDeudas=mapPeriodosDeudasTmp;
	}

	/**
	 * Agrega las deudas pasadas como parametro al mapDeudas, teniendo en cuenta si ya existen o no, 
	 * actualizando los valores de los totales dependiendo del tipoDeuda pasado como parametro 
	 * @param listPeriodosDeuda
	 * @param tipoDeuda
	 */
	private void agregarDeuda(List<Object[]> listPeriodosDeuda, int tipoDeuda){
		log.debug("agregarDeuda - enter");
		for(Object[] obj: listPeriodosDeuda){
			BigDecimal anioPeriodo = (BigDecimal) obj[0];// ej: obj[0]=200812
			String anio =  anioPeriodo.toString().substring(0, 4); 
			String periodo = anioPeriodo.toString().substring(4);
			String strPeriodo = periodo+"/"+anio;
			Double total = ((BigDecimal)obj[1]).doubleValue();
			log.debug("\nFECHA AGREGAR: \nanio:"+anio+"\nperiodo:"+periodo+"\nstrPeriodo:"+strPeriodo);
			Double[] totalesPeriodo = {0D,0D,0D,0D,0D,0D,0D};
			if(mapPeriodosDeudas.containsKey(strPeriodo)){
				totalesPeriodo = mapPeriodosDeudas.get(strPeriodo);
			}
			
			totalesPeriodo[tipoDeuda] += total;
			mapPeriodosDeudas.put(strPeriodo, totalesPeriodo);
		}

	}

}
