//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import coop.tecso.demoda.buss.bean.BaseBO;

public class DatosReciboCdM extends BaseBO {
 
	private static final long serialVersionUID = 1L;

	private Double cantMetrosFrente;
	private Double cantUT;
	private Double valuacion;
	private Long cuota;
	private Long cantCuotas;
	private Integer numeroCuadra;
	private String numeroCuentaTGI;
	private String desObra;
	private String desTipoObra;
	private Integer numeroObra;
	private Integer cantCuotasAnt; // Cantidad Cuotas plan anterior
	private Double descuento;
	private String localidad; //Localidad del Contribuyente
	
    // Leyenda del Recibo 
	private String leyenda;
	private String desObraFormaPago;

	
	
	public DatosReciboCdM() {
		super();
	}

	public Double getCantMetrosFrente() {
		return cantMetrosFrente;
	}
	public void setCantMetrosFrente(Double cantMetrosFrente) {
		this.cantMetrosFrente = cantMetrosFrente;
	}

	public Double getCantUT() {
		return cantUT;
	}
	public void setCantUT(Double cantUT) {
		this.cantUT = cantUT;
	}
	
	public Double getValuacion() {
		return valuacion;
	}

	public void setValuacion(Double valuacion) {
		this.valuacion = valuacion;
	}

	public Long getCantCuotas() {
		return cantCuotas;
	}
	public void setCantCuotas(Long cantCuotas) {
		this.cantCuotas = cantCuotas;
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

	public String getLeyenda() {
		return leyenda;
	}

	public void setLeyenda(String leyenda) {
		this.leyenda = leyenda;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
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
}
