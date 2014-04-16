//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.model.CalleVO;
import ar.gov.rosario.siat.pad.iface.model.RepartidorVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del PlanillaCuadra
 * @author tecso
 *
 */
public class PlanillaCuadraVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "planillaCuadraVO";

	private RecursoVO recurso = new RecursoVO(); 
	private ContratoVO contrato = new ContratoVO();
	private TipoObraVO tipoObra = new TipoObraVO();
	private String descripcion;
	private Date fechaCarga;
	private Double costoCuadra;
	private CalleVO callePpal = new CalleVO();
	private CalleVO calleDesde = new CalleVO();
	private CalleVO calleHasta = new CalleVO();	
	private String observacion;
	private String manzana1; 
	private String manzana2;
	private Integer numeroCuadra;
	private EstPlaCuaVO estPlaCua = new EstPlaCuaVO();
	private ObraVO obra = new ObraVO();
	private RepartidorVO repartidor = new RepartidorVO();
	
	private List<PlaCuaDetVO> listPlaCuaDet = new ArrayList<PlaCuaDetVO>();	
	private List<HisEstPlaCuaVO> listHisEstPlaCua = new ArrayList<HisEstPlaCuaVO>();
	
	// viwe
	private String fechaCargaView;
	private String costoCuadraView;
	private String numeroCuadraView;
	
	// lista de detalles de la planilla cuadra agrupada por carpetas
	private List<PlaCuaDetVO> listPlaCuaDetAgrupados = new ArrayList<PlaCuaDetVO>();	

	// Buss Flags
	private boolean informarCatastralesBussEnabled = true;	
	private boolean cambiarEstadoBussEnabled = true;
	private boolean seleccionarBussEnabled = true;	
	
	//Monto Total a Pagar
	Double montoTotal;
	String montoTotalView = "";
	// Total de Cuentas
	Integer totalCuentas;
	String totalCuentasView = "";
	
	// View Constants
	// leyanda a mostrar cdo no se pueda seleccionar un item
	private String leyenda = "";

	// Constructores
	public PlanillaCuadraVO() {
		super();
	}
	
	public PlanillaCuadraVO(long id, String desPlanillaCuadra) {
		super();
		setId(id);
		setDescripcion(desPlanillaCuadra);
	}

	// Getters y Setters
	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public ContratoVO getContrato() {
		return contrato;
	}

	public void setContrato(ContratoVO contrato) {
		this.contrato = contrato;
	}

	public TipoObraVO getTipoObra() {
		return tipoObra;
	}

	public void setTipoObra(TipoObraVO tipoObra) {
		this.tipoObra = tipoObra;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaCarga() {
		return fechaCarga;
	}

	public void setFechaCarga(Date fechaCarga) {
		this.fechaCarga = fechaCarga;
		this.fechaCargaView = DateUtil.formatDate(fechaCarga, DateUtil.ddSMMSYYYY_MASK);
																       
	}

	public Double getCostoCuadra() {
		return costoCuadra;
	}

	public void setCostoCuadra(Double costoCuadra) {
		this.costoCuadra = costoCuadra;
		this.costoCuadraView = StringUtil.formatDouble(costoCuadra);
	}

	public CalleVO getCallePpal() {
		return callePpal;
	}

	public void setCallePpal(CalleVO callePpal) {
		this.callePpal = callePpal;
	}

	public CalleVO getCalleDesde() {
		return calleDesde;
	}

	public void setCalleDesde(CalleVO calleDesde) {
		this.calleDesde = calleDesde;
	}

	public CalleVO getCalleHasta() {
		return calleHasta;
	}

	public void setCalleHasta(CalleVO calleHasta) {
		this.calleHasta = calleHasta;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}


	public EstPlaCuaVO getEstPlaCua() {
		return estPlaCua;
	}

	public void setEstPlaCua(EstPlaCuaVO estPlaCua) {
		this.estPlaCua = estPlaCua;
	}

	public List<PlaCuaDetVO> getListPlaCuaDet() {
		return listPlaCuaDet;
	}
	
	public void setListPlaCuaDet(List<PlaCuaDetVO> listPlaCuaDet) {
		this.listPlaCuaDet = listPlaCuaDet;
	}
	
	public List<HisEstPlaCuaVO> getListHisEstPlaCua() {
		return listHisEstPlaCua;
	}

	public void setListHisEstPlaCua(List<HisEstPlaCuaVO> listHisEstPlaCua) {
		this.listHisEstPlaCua = listHisEstPlaCua;
	}
	
	public ObraVO getObra() {
		return obra;
	}

	public void setObra(ObraVO obra) {
		this.obra = obra;
	}

	// Buss flags getters y setters

	public String getManzana1() {
		return manzana1;
	}

	public void setManzana1(String manzana1) {
		this.manzana1 = manzana1;
	}

	public String getManzana2() {
		return manzana2;
	}

	public void setManzana2(String manzana2) {
		this.manzana2 = manzana2;
	}

	public RepartidorVO getRepartidor() {
		return repartidor;
	}

	public void setRepartidor(RepartidorVO repartidor) {
		this.repartidor = repartidor;
	}

	public boolean getInformarCatastralesBussEnabled() {
		return informarCatastralesBussEnabled;
	}

	public void setInformarCatastralesBussEnabled(
			boolean informarCatastralesBussEnabled) {
		this.informarCatastralesBussEnabled = informarCatastralesBussEnabled;
	}

	public boolean getCambiarEstadoBussEnabled() {
		return cambiarEstadoBussEnabled;
	}

	public void setCambiarEstadoBussEnabled(boolean cambiarEstadoBussEnabled) {
		this.cambiarEstadoBussEnabled = cambiarEstadoBussEnabled;
	}
	
	public boolean getSeleccionarBussEnabled() {
		return seleccionarBussEnabled;
	}

	public void setSeleccionarBussEnabled(boolean seleccionarBussEnabled) {
		this.seleccionarBussEnabled = seleccionarBussEnabled;
	}

	public String getLeyenda() {
		return leyenda;
	}

	public void setLeyenda(String leyenda) {
		this.leyenda = leyenda;
	}
	
	// View flags getters
	
	
	// View getters

	public Long getIdRecurso() {
		Long idRecurso = this.getRecurso().getId();
		if (idRecurso == null) {
			idRecurso = new Long(-1);
		}
		return idRecurso;
	}

	public String getFechaCargaView() {
		return fechaCargaView;
	}

	public void setFechaCargaView(String fechaCargaView) {
		this.fechaCargaView = fechaCargaView;
	}

	public String getCostoCuadraView() {
		return costoCuadraView;
	}

	public void setCostoCuadraView(String costoCuadraView) {
		this.costoCuadraView = costoCuadraView;
	}

	public List<PlaCuaDetVO> getListPlaCuaDetAgrupados() {
		return listPlaCuaDetAgrupados;
	}

	public void setListPlaCuaDetAgrupados(List<PlaCuaDetVO> listPlaCuaDetAgrupados) {
		this.listPlaCuaDetAgrupados = listPlaCuaDetAgrupados;
	}

	public Integer getNumeroCuadra() {
		return numeroCuadra;
	}

	public void setNumeroCuadra(Integer numeroCuadra) {
		this.numeroCuadra = numeroCuadra;
		this.numeroCuadraView = StringUtil.formatInteger(numeroCuadra);
	}

	public String getNumeroCuadraView() {
		return numeroCuadraView;
	}

	public void setNumeroCuadraView(String numeroCuadraView) {
		this.numeroCuadraView = numeroCuadraView;
	}

	public Double getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(Double montoTotal) {
		this.montoTotal = montoTotal;
		this.montoTotalView = StringUtil.redondearDecimales(montoTotal, 1, 2);
	}

	public String getMontoTotalView() {
		return montoTotalView;
	}

	public void setMontoTotalView(String montoTotalView) {
		this.montoTotalView = montoTotalView;
	}

	public Integer getTotalCuentas() {
		return totalCuentas;
	}

	public void setTotalCuentas(Integer totalCuentas) {
		this.totalCuentas = totalCuentas;
		this.totalCuentasView = StringUtil.formatInteger(totalCuentas);
	}

	public String getTotalCuentasView() {
		return totalCuentasView;
	}

	public void setTotalCuentasView(String totalCuentasView) {
		this.totalCuentasView = totalCuentasView;
	}

	
}
