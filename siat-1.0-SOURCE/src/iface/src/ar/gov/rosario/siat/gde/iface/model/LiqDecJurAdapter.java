//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecAliVO;
import ar.gov.rosario.siat.def.iface.model.RecConADecVO;
import ar.gov.rosario.siat.def.iface.model.RecTipUniVO;
import ar.gov.rosario.siat.def.iface.model.ValUnRecConADeVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.PORCENTAJE;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Adapter del Declaracion Jurada Masiva
 * 
 * @author tecso
 */
public class LiqDecJurAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "liqDecJurAdapterVO";
	
	private Log log = LogFactory.getLog(LiqDecJurAdapter.class);
	
	// Ids de deuda seleccionadas
	private String[] listIdDeudaSelected;
	private boolean esEtur = false;
	
	// Listas del primer Adapter (init) ######################################################################################
	private Date fechaFormalizacion;
	private String fechaFormalizacionView;
	private List<Date> listFechasForm = new ArrayList<Date>();
	
	private RecConADecVO actividad = new RecConADecVO(); 
	private List<RecConADecVO> listActividad = new ArrayList<RecConADecVO>();
	private List<RecConADecVO> listActividadDec = new ArrayList<RecConADecVO>(); // Actividades Declaradas
    
	private LiqRecMinVO periodoDesde = new LiqRecMinVO();
	private LiqRecMinVO periodoHasta = new LiqRecMinVO();
	private LiqRecMinVO cantPersonal = new LiqRecMinVO();
	private List<LiqRecMinVO> listPeriodos = new ArrayList<LiqRecMinVO>(); // Periodos Seleccionaos
	private List<LiqRecMinVO> listPeriodosDesde = new ArrayList<LiqRecMinVO>();
	private List<LiqRecMinVO> listPeriodosHasta = new ArrayList<LiqRecMinVO>();
	private List<LiqRecMinVO> listRecMin = new ArrayList<LiqRecMinVO>(); // Rangos de cantidad de personal
	
	private boolean irPersonal = false;
	
	
	// Lista para el segundo Adapter (Detalle)  ###############################################################################
	private List<LiqDetalleDecJurVO> listDetalle = new ArrayList<LiqDetalleDecJurVO>();
	// Ids de detalles selccionado para setear cantidades de unidad
	private String[] listIdDetalleSelected;
	
	private RecTipUniVO unidad = new RecTipUniVO();  // Unidad
	private List<RecTipUniVO> listUnidad = new ArrayList<RecTipUniVO>(); // Lista de Unidad
	
	private RecConADecVO tipoUnidad = new RecConADecVO(); // TipoUnidad
	private List<RecConADecVO> listTipoUnidad = new ArrayList<RecConADecVO>(); // Lista de TipoUnidad
	private Map<Long, List<RecConADecVO>> mapTipoUnidad = new HashMap<Long, List<RecConADecVO>>();
	
	private Integer cantUni;
	private String  cantUniView = "";
	
	private boolean irUnidad = false;
	
	private List<RecAliVO> listAlicuota = new ArrayList<RecAliVO>();
	private RecAliVO alicuota = new RecAliVO();
	
	private List<ValUnRecConADeVO> listMinimo = new ArrayList<ValUnRecConADeVO>();
	private ValUnRecConADeVO minimo = new ValUnRecConADeVO();
	
	// Lista para el tercer Adapter (General) ####################################################################################
	private List<LiqDetalleDecJurVO> listGeneral = new ArrayList<LiqDetalleDecJurVO>();
	// Ids de periodos general seleccionados para setear porcentajes
	private String[] listIdGeneralSelected;
	
	private Double porcentaje;
	private String porcentajeView = "";
	
	private DecJurPagVO retencion = new DecJurPagVO();
	private List<AgeRetVO> listAgeRet = new ArrayList<AgeRetVO>();
	
	private List<RecAliVO> listAliPub = new ArrayList<RecAliVO>(); // Alicuotas Publicidad
	private RecAliVO aliPub = new RecAliVO();					   // Alicuota Publicidad	
	
	private List<RecAliVO> listAliMyS = new ArrayList<RecAliVO>(); // Alicuotas Mesas y Sillas
	private RecAliVO aliMyS = new RecAliVO();					   // Alicuota Mesas y Sillas
	
	
	// Lista para la simulacion y la impresion #################################################################################
	private List<LiqDeudaVO> listDeudaSimulada = new ArrayList<LiqDeudaVO>();
	private Double totalImporte = 0D;
	private Double totalSaldo = 0D;
	private Double totalActualizacion = 0D;
	private Double total = 0D;
	
	
	// ids de deuda generada para ir a la formalizacion de convenio
	private String[] listIdDeudaGenerada;
	
	private boolean generarDeudaEnabled;
	
	// Cuenta seleccionada, para navegacion
	private LiqCuentaVO cuenta = new LiqCuentaVO();

    
    // ---> Propiedades para la asignacion de permisos

    // <--- Propiedades para la asignacion de permisos
    
	// Constructores
    public LiqDecJurAdapter(){
    	super(GdeSecurityConstants.DECLARACION_JURADA_MAS);
    	setFechaFormalizacion(new Date());
    }

    //  Getters y Setters
	public LiqCuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(LiqCuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public String[] getListIdDeudaSelected() {
		return listIdDeudaSelected;
	}
	public void setListIdDeudaSelected(String[] listIdDeudaSelected) {
		this.listIdDeudaSelected = listIdDeudaSelected;
	}

	public List<RecConADecVO> getListActividad() {
		return listActividad;
	}
	public void setListActividad(List<RecConADecVO> listActividad) {
		this.listActividad = listActividad;
	}

	public List<RecConADecVO> getListActividadDec() {
		return listActividadDec;
	}
	public void setListActividadDec(List<RecConADecVO> listActividadDec) {
		this.listActividadDec = listActividadDec;
	}

	public Date getFechaFormalizacion() {
		return fechaFormalizacion;
	}
	public void setFechaFormalizacion(Date fechaFormalizacion) {
		this.fechaFormalizacion = fechaFormalizacion;
		this.fechaFormalizacionView = DateUtil.formatDate(fechaFormalizacion, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaFormalizacionView() {
		return fechaFormalizacionView;
	}
	public void setFechaFormalizacionView(String fechaFormalizacionView) {
		this.fechaFormalizacionView = fechaFormalizacionView;
	}

	public String getTotalImporteView(){
		return (totalImporte!=null?StringUtil.redondearDecimales(totalImporte, 1, 2):"");
	}
	
	public String getTotalSaldoView(){
		return (totalSaldo!=null?StringUtil.redondearDecimales(totalSaldo, 1, 2):"");
	}
	
	public String getTotalActualizacionView(){
		return (totalActualizacion!=null?StringUtil.redondearDecimales(totalActualizacion, 1, 2):"");
	}
	
	public String getTotalView(){
		return (total!=null?StringUtil.redondearDecimales(total, 1, 2):"");
	}
	
	public RecConADecVO getActividad() {
		return actividad;
	}
	public void setActividad(RecConADecVO actividad) {
		this.actividad = actividad;
	}

	public LiqRecMinVO getCantPersonal() {
		return cantPersonal;
	}
	public void setCantPersonal(LiqRecMinVO cantPersonal) {
		this.cantPersonal = cantPersonal;
	}

	public List<LiqRecMinVO> getListRecMin() {
		return listRecMin;
	}
	public void setListRecMin(List<LiqRecMinVO> listRecMin) {
		this.listRecMin = listRecMin;
	}

	public List<LiqRecMinVO> getListPeriodos() {
		return listPeriodos;
	}
	public void setListPeriodos(List<LiqRecMinVO> listPeriodos) {
		this.listPeriodos = listPeriodos;
	}
	
	public List<LiqRecMinVO> getListPeriodosDesde() {
		return listPeriodosDesde;
	}
	public void setListPeriodosDesde(List<LiqRecMinVO> listPeriodosDesde) {
		this.listPeriodosDesde = listPeriodosDesde;
	}

	public List<LiqRecMinVO> getListPeriodosHasta() {
		return listPeriodosHasta;
	}
	public void setListPeriodosHasta(List<LiqRecMinVO> listPeriodosHasta) {
		this.listPeriodosHasta = listPeriodosHasta;
	}

	public LiqRecMinVO getPeriodoDesde() {
		return periodoDesde;
	}
	public void setPeriodoDesde(LiqRecMinVO periodoDesde) {
		this.periodoDesde = periodoDesde;
	}

	public LiqRecMinVO getPeriodoHasta() {
		return periodoHasta;
	}
	public void setPeriodoHasta(LiqRecMinVO periodoHasta) {
		this.periodoHasta = periodoHasta;
	}

	public List<LiqDetalleDecJurVO> getListDetalle() {
		return listDetalle;
	}
	public void setListDetalle(List<LiqDetalleDecJurVO> listDetalle) {
		this.listDetalle = listDetalle;
	}

	public List<LiqDetalleDecJurVO> getListGeneral() {
		return listGeneral;
	}
	public void setListGeneral(List<LiqDetalleDecJurVO> listGeneral) {
		this.listGeneral = listGeneral;
	}
	
	public List<Date> getListFechasForm() {
		return listFechasForm;
	}
	public void setListFechasForm(List<Date> listFechasForm) {
		this.listFechasForm = listFechasForm;
	}

	public List<RecAliVO> getListAlicuota() {
		return listAlicuota;
	}
	public void setListAlicuota(List<RecAliVO> listAlicuota) {
		this.listAlicuota = listAlicuota;
	}

	public List<RecAliVO> getListAliPub() {
		return listAliPub;
	}
	public void setListAliPub(List<RecAliVO> listAliPub) {
		this.listAliPub = listAliPub;
	}

	/**
	 * Getter con funcionalidad de obtener el VO desde la lista, que esta mas conpleto. 
	 * 
	 * @return
	 */
	public RecAliVO obtenerAliPub() {
			
		if (!ModelUtil.isNullOrEmpty(aliPub)){
			for (RecAliVO recAliVO:getListAliPub()){
				if (recAliVO.getId().longValue() == aliPub.getId().longValue()){
					RecAliVO aliPubVO = new RecAliVO();
					aliPubVO.setId(recAliVO.getId());
					aliPubVO.setAlicuota( recAliVO.getAlicuota());
					return aliPubVO;
				}
			}
		}
		return aliPub;
	}
	
	public RecAliVO getAliPub() {
		return aliPub;
	}
	public void setAliPub(RecAliVO aliPub) {
		this.aliPub = aliPub;
	}

	public List<RecAliVO> getListAliMyS() {
		return listAliMyS;
	}
	public void setListAliMyS(List<RecAliVO> listAliMyS) {
		this.listAliMyS = listAliMyS;
	}

	/**
	 * Getter con funcionalidad de obtener el VO desde la lista, que esta mas conpleto. 
	 * 
	 * @return
	 */
	public RecAliVO obtenerAliMyS() {
			
		if (!ModelUtil.isNullOrEmpty(aliMyS)){
			for (RecAliVO recAliVO:getListAliMyS()){
				if (recAliVO.getId().longValue() == aliMyS.getId().longValue()){
					RecAliVO aliMySVO = new RecAliVO();
					aliMySVO.setId(recAliVO.getId());
					aliMySVO.setAlicuota( recAliVO.getAlicuota());
					return aliMySVO;
				}
			}
		}
		return aliMyS;
	}
	
	public RecAliVO getAliMyS() {
		return aliMyS;
	}
	public void setAliMyS(RecAliVO aliMyS) {
		this.aliMyS = aliMyS;
	}

	/**
	 * Getter con funcionalidad de obtener el VO desde la lista, que esta mas conpleto. 
	 * 
	 * @return
	 */
	public RecAliVO obtenerAlicuota() {
		
		if (!ModelUtil.isNullOrEmpty(alicuota)){
			for (RecAliVO recAliVO:getListAlicuota()){
				if (recAliVO.getId().longValue() == alicuota.getId().longValue()){
					RecAliVO alicuotaVO = new RecAliVO();
					alicuotaVO.setId(recAliVO.getId());
					alicuotaVO.setAlicuota( recAliVO.getAlicuota());
					return alicuotaVO;
				}
			}
		}
		return alicuota;
	}
	
	public RecAliVO getAlicuota() {
		return alicuota;
	}
	public void setAlicuota(RecAliVO alicuota) {
		this.alicuota = alicuota;
	}

	/**
	 * Getter con funcionalidad de obtener el VO desde la lista, que esta mas conpleto. 
	 * 
	 * @return
	 */
	public RecTipUniVO obtenerUnidad() {
		
		if (!ModelUtil.isNullOrEmpty(unidad)){
			for (RecTipUniVO recTipUniVO:getListUnidad()){
				if (recTipUniVO.getId().longValue() == unidad.getId().longValue()){
					return recTipUniVO;
				}
			}
		}
		
		return unidad;
	}
	public RecTipUniVO getUnidad() {
		return unidad;
	}
	public void setUnidad(RecTipUniVO unidad) {
		this.unidad = unidad;
	}

	public List<RecTipUniVO> getListUnidad() {
		return listUnidad;
	}
	public void setListUnidad(List<RecTipUniVO> listUnidad) {
		this.listUnidad = listUnidad;
	}

	public RecConADecVO obtenerTipoUnidad() {
		
		if (!ModelUtil.isNullOrEmpty(tipoUnidad)){
			for (RecConADecVO recConADecVO:getListTipoUnidad()){
				if (recConADecVO.getId().longValue() == tipoUnidad.getId().longValue()){
					
					RecConADecVO recConADecRet = new RecConADecVO();
					
					recConADecRet.setId(recConADecVO.getId());
					recConADecRet.setRecurso(recConADecVO.getRecurso());
					recConADecRet.setTipRecConADec(recConADecVO.getTipRecConADec());
					recConADecRet.setRecTipUni(recConADecVO.getRecTipUni());
					recConADecRet.setCodConcepto(recConADecVO.getCodConcepto());
					recConADecRet.setDesConcepto(recConADecVO.getDesConcepto());
					recConADecRet.setObservacion(recConADecVO.getObservacion());
					recConADecRet.setFechaDesde(recConADecVO.getFechaDesde());
					recConADecRet.setFechaHasta(recConADecVO.getFechaHasta());
					recConADecRet.setListValUnRecConADe(recConADecVO.getListValUnRecConADe());
					
					return recConADecRet;
				}
			}
		}
		
		return tipoUnidad;
	}
	
	public RecConADecVO getTipoUnidad() {
		return tipoUnidad;
	}
	public void setTipoUnidad(RecConADecVO tipoUnidad) {
		this.tipoUnidad = tipoUnidad;
	}

	public List<RecConADecVO> getListTipoUnidad() {
		return listTipoUnidad;
	}
	public void setListTipoUnidad(List<RecConADecVO> listTipoUnidad) {
		this.listTipoUnidad = listTipoUnidad;
	}
	
	public Map<Long, List<RecConADecVO>> getMapTipoUnidad() {
		return mapTipoUnidad;
	}
	public void setMapTipoUnidad(Map<Long, List<RecConADecVO>> mapTipoUnidad) {
		this.mapTipoUnidad = mapTipoUnidad;
	}

	public Integer getCantUni() {
		return cantUni;
	}
	public void setCantUni(Integer cantUni) {
		this.cantUni = cantUni;
		this.cantUniView = StringUtil.formatInteger(cantUni);
	}

	public String getCantUniView() {
		return cantUniView;
	}
	public void setCantUniView(String cantUniView) {
		this.cantUniView = cantUniView;
	}

	public String[] getListIdDetalleSelected() {
		return listIdDetalleSelected;
	}
	public void setListIdDetalleSelected(String[] listIdDetalleSelected) {
		this.listIdDetalleSelected = listIdDetalleSelected;
	}

	public String[] getListIdGeneralSelected() {
		return listIdGeneralSelected;
	}
	public void setListIdGeneralSelected(String[] listIdGeneralSelected) {
		this.listIdGeneralSelected = listIdGeneralSelected;
	}

	@PORCENTAJE
	public Double getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
		this.porcentajeView = StringUtil.formatDouble(porcentaje);
	}

	public String getPorcentajeView() {
		return porcentajeView;
	}
	public void setPorcentajeView(String porcentajeView) {
		this.porcentajeView = porcentajeView;
	}

	public boolean isIrPersonal() {
		return irPersonal;
	}
	public void setIrPersonal(boolean irPersonal) {
		this.irPersonal = irPersonal;
	}

	public boolean isIrUnidad() {
		return irUnidad;
	}
	public void setIrUnidad(boolean irUnidad) {
		this.irUnidad = irUnidad;
	}

	public DecJurPagVO getRetencion() {
		return retencion;
	}
	public void setRetencion(DecJurPagVO retencion) {
		this.retencion = retencion;
	}

	public List<AgeRetVO> getListAgeRet() {
		return listAgeRet;
	}

	public void setListAgeRet(List<AgeRetVO> listAgeRet) {
		this.listAgeRet = listAgeRet;
	}

	
	public List<LiqDeudaVO> getListDeudaSimulada() {
		return listDeudaSimulada;
	}
	public void setListDeudaSimulada(List<LiqDeudaVO> listDeudaSimulada) {
		this.listDeudaSimulada = listDeudaSimulada;
	}

	public Double getTotalImporte() {
		return totalImporte;
	}
	public void setTotalImporte(Double totalImporte) {
		this.totalImporte = totalImporte;
	}

	public Double getTotalSaldo() {
		return totalSaldo;
	}
	public void setTotalSaldo(Double totalSaldo) {
		this.totalSaldo = totalSaldo;
	}

	public Double getTotalActualizacion() {
		return totalActualizacion;
	}

	public void setTotalActualizacion(Double totalActualizacion) {
		this.totalActualizacion = totalActualizacion;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
	
	public String[] getListIdDeudaGenerada() {
		return listIdDeudaGenerada;
	}
	public void setListIdDeudaGenerada(String[] listIdDeudaGenerada) {
		this.listIdDeudaGenerada = listIdDeudaGenerada;
	}

	public boolean getEsEtur() {
		return esEtur;
	}
	public void setEsEtur(boolean esEtur) {
		this.esEtur = esEtur;
	}

	public boolean getEsDReI() {
		return !esEtur;
	}
	
	public void calcularTotalDeuda(){
		this.totalImporte = 0D;
		this.totalSaldo = 0D;
		this.totalActualizacion = 0D;
		this.total = 0D;
		
		// Totalizacion de la deuda en via Administrativa
		for (LiqDeudaVO deuda:this.getListDeudaSimulada()){
			totalImporte += new Double(deuda.getImporte()==null?0D:deuda.getImporte());
			totalSaldo += new Double(deuda.getSaldo()==null?0D:deuda.getSaldo());
			totalActualizacion += new Double(deuda.getActualizacion()==null?0D:deuda.getActualizacion());
			total += new Double(deuda.getTotal()==null?0D:deuda.getTotal());
		}
	}
	
	public List<ValUnRecConADeVO> getListMinimo() {
		return listMinimo;
	}
	public void setListMinimo(List<ValUnRecConADeVO> listMinimo) {
		this.listMinimo = listMinimo;
	}

	public ValUnRecConADeVO obtenerMinimo() {
		
		log.debug(" getMinimo Enter.....");
		
		if (!ModelUtil.isNullOrEmpty(minimo)){
			
			log.debug(" getMinimo id: " + minimo.getId());
			
			for (ValUnRecConADeVO valUnRecConADeVO:getListMinimo()){
			
				log.debug(" getMinimo comparando con: " + valUnRecConADeVO.getId().longValue());	
				
				if (valUnRecConADeVO.getId().longValue() == minimo.getId().longValue()){
					
					log.debug(" Encontrado ... Valor Minimo: " + valUnRecConADeVO.getValorUnitario());
					
					ValUnRecConADeVO valUnRecConADeVORet = new ValUnRecConADeVO();
					
					valUnRecConADeVORet.setId(valUnRecConADeVO.getId());
					valUnRecConADeVORet.setValorUnitario(valUnRecConADeVO.getValorUnitario());
					valUnRecConADeVORet.setRecConADec(valUnRecConADeVO.getRecConADec());
					valUnRecConADeVORet.setValRefDes(valUnRecConADeVO.getValRefDes());
					valUnRecConADeVORet.setValRefHas(valUnRecConADeVO.getValRefHas());
					valUnRecConADeVORet.setFechaDesde(valUnRecConADeVO.getFechaDesde());
					valUnRecConADeVORet.setFechaHasta(valUnRecConADeVO.getFechaHasta());
					
					return valUnRecConADeVORet;
				}
			}
		}
		
		return minimo;
	}
	
	public ValUnRecConADeVO getMinimo() {
		return minimo;
	}	
	public void setMinimo(ValUnRecConADeVO minimo) {
		this.minimo = minimo;
	}

	public void cargarPeriodos(){
		
		/*LiqRecMinVO periodoD, periodoH;
		for(LiqRecMinVO periodo: getListPeriodos()){
			
			periodoD = new LiqRecMinVO(periodo.getPeriodo(), periodo.getAnio(), periodo.getId());
			
			periodoH = new LiqRecMinVO(periodo.getPeriodo(), periodo.getAnio(), periodo.getId());
			
			getListPeriodosDesde().add(periodoD);
			getListPeriodosHasta().add(periodoH);
		}*/

		
		/*
		 * Codigo para carga secuencial
		 */ 
		LiqRecMinVO periodoD, periodoH;
		
		// Desde = primer periodo
		periodoD = new LiqRecMinVO(getListPeriodos().get(0).getPeriodo(), getListPeriodos().get(0).getAnio(), getListPeriodos().get(0).getId());
		getListPeriodosDesde().add(periodoD);
		
		// Hasta = El resto de los periodos
		for(LiqRecMinVO periodo: getListPeriodos()){
			if (periodo.getId().longValue() >= periodoD.getId().longValue()){
				periodoH = new LiqRecMinVO(periodo.getPeriodo(), periodo.getAnio(), periodo.getId());
				getListPeriodosHasta().add(periodoH);
			}
		}
	}
	
	
	public void resetForDetalle(){

		this.setListDetalle(new ArrayList<LiqDetalleDecJurVO>());
		this.setListUnidad(new ArrayList<RecTipUniVO>());
		this.setListTipoUnidad(new ArrayList<RecConADecVO>());
		this.setMapTipoUnidad(new HashMap<Long, List<RecConADecVO>>());
	}
	
	public void resetForGeneral(){
		this.setListGeneral(new ArrayList<LiqDetalleDecJurVO>());
	}	

	/**
	 * Dados periodo y anio, devuelve la cantidad de personal ingresada por el usuario en (Init). 
	 * 
	 * @param periodo
	 * @param anio
	 * @return
	 */
	public Integer getCantPersonal(Integer periodo, Integer anio){
		
		log.debug("Para el periodo: " + periodo +"/"+anio);
				
		for (LiqRecMinVO liqRecMinVO:getListRecMin()){
			
			log.debug(" PD: " + liqRecMinVO.getPeriodoDesde() + "/" + liqRecMinVO.getAnioDesde() +
					" PH: " + liqRecMinVO.getPeriodoHasta() + "/" + liqRecMinVO.getAnioHasta() + 
					" - " + liqRecMinVO.getValor());
			
			// Si esta dentro del rango de anios del "rango".
			if(liqRecMinVO.getAnioDesde().intValue() <=  anio.intValue() && 
					liqRecMinVO.getAnioHasta().intValue() >= anio.intValue()){
				
				// Para periodos del mismo anio
				if (liqRecMinVO.getAnioDesde().intValue() == liqRecMinVO.getAnioHasta().intValue()){
					
					if (liqRecMinVO.getPeriodoDesde().intValue() <= periodo.intValue() && 
						liqRecMinVO.getPeriodoHasta().intValue() >= periodo.intValue()) {

						log.debug(" 	Cantidad para el mesmo anio: " +  liqRecMinVO.getValor());
						log.debug(" ---------------------------------------------------------");
						return new Integer(liqRecMinVO.getValor());
					}
				
				} else {
					
					// 2007
					// 2008
					// 2009
					
					// Primer anio del rango
					if (anio.intValue() == liqRecMinVO.getAnioDesde().intValue() && 
							periodo.intValue() >= liqRecMinVO.getPeriodoDesde().intValue()){
						
						log.debug(" 	Primer anio -> cantidad : " +  liqRecMinVO.getValor());
						log.debug(" ---------------------------------------------------------");
						return new Integer(liqRecMinVO.getValor());
					}
					
					// Ultimo anio del rango
					if (anio.intValue() == liqRecMinVO.getAnioHasta().intValue() && 
								periodo.intValue() <= liqRecMinVO.getPeriodoHasta().intValue()){
						
						log.debug(" 	Ultimo anio -> cantidad: " +  liqRecMinVO.getValor());
						log.debug(" ---------------------------------------------------------");
						return new Integer(liqRecMinVO.getValor());
					}

					// Si huriera anios intermedios
					if (liqRecMinVO.getAnioDesde().intValue() + 1 < liqRecMinVO.getAnioHasta().intValue() && 
							anio.intValue() != liqRecMinVO.getAnioDesde().intValue() && anio.intValue() != liqRecMinVO.getAnioHasta().intValue()){
						
						log.debug(" 	Anio intermedio -> cantidad: " +  liqRecMinVO.getValor());
						log.debug(" ---------------------------------------------------------");
						return new Integer(liqRecMinVO.getValor());
					}
					
				}
			}
		}
		
		return 0;
	}
	
	
	public List<String> getListFechasFormView() {
		List<String> diasStr = new ArrayList<String>();
		for(Date fecha: listFechasForm){
			diasStr.add(DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK));
		}
		return diasStr;
	}
	
	public String getEditPagoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.DECLARACION_JURADA_MAS, GdeSecurityConstants.MTD_EDIT_PAGO);
	}

	public boolean isGenerarDeudaEnabled() {
		return generarDeudaEnabled;
	}
	public void setGenerarDeudaEnabled(boolean generarDeudaEnabled) {
		this.generarDeudaEnabled = generarDeudaEnabled;
	}
	
}