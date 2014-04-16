//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del Multa
 * @author tecso
 *
 */
public class MultaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "multaVO";
	
	private OrdenControlVO ordenControl = new OrdenControlVO();
	private CuentaVO cuenta = new CuentaVO();
	private TipoMultaVO tipoMulta = new TipoMultaVO();
	private DeudaVO deuda = new DeudaVO();
	private Date fechaEmision;
	private Double importe; // total a emitir
	private Date fechaNotificacion;
	private String observacion="";
	private Date fechaActualizacion;
	private Date fechaVencimiento;
	private Date fechaResolucion;
	private CasoVO caso = new CasoVO();
	private DescuentoMultaVO descuentoMulta = new DescuentoMultaVO();
	private List<MultaDetVO> listMultaDet = new ArrayList<MultaDetVO>();
	private Double minimo;
	private Double canMin;
	
	private Double totalAplicado=0.0;
	private Double importeMultaAnterior=0.0;
	private boolean modificaImporte=true;
	private boolean tieneRangosDeMinimos=false;
	//private Double totalEmitir=0.0;
	
	// View Constants
	private String fechaEmisionView="";
	private String importeView="";
	private String fechaNotificacionView="";
	private String fechaActualizacionView = "";
	private String fechaVencimientoView = "";
	
	private String totalAplicadoView="";
	private String importeMultaAnteriorView="";
	private String totalEmitirView="";
	
	private List<MultaHistoricoVO>listMultaHistorico = new ArrayList<MultaHistoricoVO>();


	// Constructores
	public MultaVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public MultaVO(int id, String desc) {
		super();
		setId(new Long(id));
	}

	// Getters y Setters
	
	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControl) {
		this.ordenControl = ordenControl;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public TipoMultaVO getTipoMulta() {
		return tipoMulta;
	}

	public void setTipoMulta(TipoMultaVO tipoMulta) {
		this.tipoMulta = tipoMulta;
	}

	public DeudaVO getDeuda() {
		return deuda;
	}

	public void setDeuda(DeudaVO deuda) {
		this.deuda = deuda;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
		this.fechaEmisionView = DateUtil.formatDate(fechaEmision, DateUtil.ddSMMSYYYY_MASK);
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe);
	}

	public Date getFechaNotificacion() {
		return fechaNotificacion;
	}

	public void setFechaNotificacion(Date fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
		this.fechaNotificacionView = DateUtil.formatDate(fechaNotificacion, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}
	
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
		this.fechaVencimientoView = DateUtil.formatDate(fechaVencimiento, DateUtil.ddSMMSYYYY_MASK);
	}

	public DescuentoMultaVO getDescuentoMulta() {
		return descuentoMulta;
	}

	public void setDescuentoMulta(DescuentoMultaVO descuentoMulta) {
		this.descuentoMulta = descuentoMulta;
	}

	// View getters

	public String getFechaEmisionView() {
		return fechaEmisionView;
	}

	public void setFechaEmisionView(String fechaEmisionView) {
		this.fechaEmisionView = fechaEmisionView;
	}

	public String getImporteView() {
		return (this.importe!=null)?NumberUtil.round(importe, SiatParam.DEC_IMPORTE_VIEW).toString():"0.00";
	}

	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}

	public String getFechaNotificacionView() {
		return fechaNotificacionView;
	}

	public void setFechaNotificacionView(String fechaNotificacionView) {
		this.fechaNotificacionView = fechaNotificacionView;
	}

	public String getFechaActualizacionView() {
		return fechaActualizacionView;
	}

	public void setFechaActualizacionView(String fechaActualizacionView) {
		this.fechaActualizacionView = fechaActualizacionView;
	}
	
	public String getFechaVencimientoView() {
		return fechaVencimientoView;
	}

	public void setFechaVencimientoView(String fechaVencimientoView) {
		this.fechaVencimientoView = fechaVencimientoView;
	}

	public List<MultaDetVO> getListMultaDet() {
		return listMultaDet;
	}

	public void setListMultaDet(List<MultaDetVO> listMultaDet) {
		this.listMultaDet = listMultaDet;
	}

	public List<MultaHistoricoVO> getListMultaHistorico() {
		return listMultaHistorico;
	}

	public void setListMultaHistorico(List<MultaHistoricoVO> listMultaHistorico) {
		this.listMultaHistorico = listMultaHistorico;
	}

	public Date getFechaResolucion() {
		return fechaResolucion;
	}

	public void setFechaResolucion(Date fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}

	public Double getMinimo() {
		return minimo;
	}

	public void setMinimo(Double minimo) {
		this.minimo = minimo;
	}

	public Double getCanMin() {
		return canMin;
	}

	public void setCanMin(Double canMin) {
		this.canMin = canMin;
	}

	public CasoVO getCaso() {
		return caso;
	}

	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	
	public String getFechaResolucionView(){
		return (this.fechaResolucion!=null)?DateUtil.formatDate(fechaResolucion, DateUtil.ddSMMSYYYY_MASK):"";
	}

	public Double getTotalAplicado() {
		return totalAplicado;
	}

	public void setTotalAplicado(Double totalAplicado) {
		this.totalAplicado = totalAplicado;
		this.totalAplicadoView = NumberUtil.round(totalAplicado,SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	public Double getImporteMultaAnterior() {
		return importeMultaAnterior;
	}

	public void setImporteMultaAnterior(Double importeMultaAnterior) {
		this.importeMultaAnterior = importeMultaAnterior;
		this.importeMultaAnteriorView = NumberUtil.round(importeMultaAnterior,SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	/*public Double getTotalEmitir() {
		return totalEmitir;
	}

	public void setTotalEmitir(Double totalEmitir) {
		this.totalEmitir = totalEmitir;
		this.totalEmitirView = NumberUtil.round(totalEmitir,SiatParam.DEC_IMPORTE_VIEW).toString();
	}*/

	public boolean isModificaImporte() {
		return modificaImporte;
	}

	public void setModificaImporte(boolean modificaImporte) {
		this.modificaImporte = modificaImporte;
	}

	public boolean isTieneRangosDeMinimos() {
		return tieneRangosDeMinimos;
	}

	public void setTieneRangosDeMinimos(boolean tieneRangosDeMinimos) {
		this.tieneRangosDeMinimos = tieneRangosDeMinimos;
	}

	public String getTotalAplicadoView() {
		return totalAplicadoView;
	}

	public void setTotalAplicadoView(String totalAplicadoView) {
		this.totalAplicadoView = totalAplicadoView;		
	}

	public String getImporteMultaAnteriorView() {
		return importeMultaAnteriorView;
	}

	public void setImporteMultaAnteriorView(String importeMultaAnteriorView) {
		this.importeMultaAnteriorView = importeMultaAnteriorView;
	}

	public String getTotalEmitirView() {
		return totalEmitirView;
	}

	public void setTotalEmitirView(String totalEmitirView) {
		this.totalEmitirView = totalEmitirView;
	}

	public String getCanMinView(){
		return (this.canMin!=null)?NumberUtil.round(canMin,SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getMinimoView(){
		return (this.minimo!=null)?NumberUtil.round(minimo,SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}

}
