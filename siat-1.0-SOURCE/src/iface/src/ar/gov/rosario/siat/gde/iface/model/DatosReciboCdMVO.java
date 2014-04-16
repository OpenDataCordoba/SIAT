//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import coop.tecso.demoda.iface.helper.StringUtil;

public class DatosReciboCdMVO {
	
	private Double cantMetrosFrente;
	private Double cantUT;
	private Double valuacion;
	private Long cuota;
	private Long cantCuotas;
	private String numeroCuentaTGI;
	private String leyPlanContado;
	private String leyPlanLargo;
	private String desObra;
	private Integer numeroObra;
	private Integer numeroCuadra;
	private Integer cantCuotasAnt; // Cantidad Cuotas plan anterior
	private Double descuento;
	private String localidad;
	private String desTipoObra;
	private String desObraFormaPago;
	

	// View
	private String cantMetrosFrenteView;
	private String cantUTView;
	private String valuacionView;
	
    // Leyenda del Recibo 
	private String leyenda;
	
	public Double getCantMetrosFrente() {
		return cantMetrosFrente;
	}
	public void setCantMetrosFrente(Double cantMetrosFrente) {
		this.cantMetrosFrente = cantMetrosFrente;
		this.cantMetrosFrenteView = StringUtil.redondearDecimales(cantMetrosFrente, 1, 2);
	}
	public Double getCantUT() {
		return cantUT;
	}
	public void setCantUT(Double cantUT) {
		this.cantUT = cantUT;
		this.cantUTView = StringUtil.redondearDecimales(cantUT, 1, 2);
	}
	public Long getCantCuotas() {
		return cantCuotas;
	}
	public void setCantCuotas(Long cantCuotas) {
		this.cantCuotas = cantCuotas;
	}
	public String getLeyPlanContado() {
		return leyPlanContado;
	}
	public void setLeyPlanContado(String leyPlanContado) {
		this.leyPlanContado = leyPlanContado;
	}
	public String getLeyPlanLargo() {
		return leyPlanLargo;
	}
	public void setLeyPlanLargo(String leyPlanLargo) {
		this.leyPlanLargo = leyPlanLargo;
	}
	public Integer getNumeroCuadra() {
		return numeroCuadra;
	}
	public void setNumeroCuadra(Integer numeroCuadra) {
		this.numeroCuadra = numeroCuadra;
	}
	public String getNumeroCuentaTGI() {
		return numeroCuentaTGI;
	}
	public void setNumeroCuentaTGI(String numeroCuentaTGI) {
		this.numeroCuentaTGI = numeroCuentaTGI;
	}
	public String getDesObra() {
		return desObra;
	}
	public void setDesObra(String desObra) {
		this.desObra = desObra;
	}
	public Integer getNumeroObra() {
		return numeroObra;
	}
	public void setNumeroObra(Integer numeroObra) {
		this.numeroObra = numeroObra;
	}
	public Integer getCantCuotasAnt() {
		return cantCuotasAnt;
	}
	public void setCantCuotasAnt(Integer cantCuotasAnt) {
		this.cantCuotasAnt = cantCuotasAnt;
	}

	public Double getDescuento() {
		return descuento;
	}
	public void setDescuento(Double descuento) {
		this.descuento = descuento;
	}
	public Double getValuacion() {
		return valuacion;
	}
	public void setValuacion(Double valuacion) {
		this.valuacion = valuacion;
		this.valuacionView = StringUtil.redondearDecimales(valuacion, 1, 2);
	}
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	public String getLeyenda() {
		return leyenda;
	}
	public void setLeyenda(String leyenda) {
		this.leyenda = leyenda;
	}
	public Long getCuota() {
		return cuota;
	}
	public void setCuota(Long cuota) {
		this.cuota = cuota;
	}
	public String getDesTipoObra() {
		return desTipoObra;
	}
	public void setDesTipoObra(String desTipoObra) {
		this.desTipoObra = desTipoObra;
	}
	public String getDesObraFormaPago() {
		return desObraFormaPago;
	}
	public void setDesObraFormaPago(String desObraFormaPago) {
		this.desObraFormaPago = desObraFormaPago;
	}
	
	// View getters
	public String getCantMetrosFrenteView() {
		return cantMetrosFrenteView;
	}
	public void setCantMetrosFrenteView(String cantMetrosFrenteView) {
		this.cantMetrosFrenteView = cantMetrosFrenteView;
	}
	public String getCantUTView() {
		return cantUTView;
	}
	public void setCantUTView(String cantUTView) {
		this.cantUTView = cantUTView;
	}
	public String getValuacionView() {
		return valuacionView;
	}
	public void setValuacionView(String valuacionView) {
		this.valuacionView = valuacionView;
	}

}
