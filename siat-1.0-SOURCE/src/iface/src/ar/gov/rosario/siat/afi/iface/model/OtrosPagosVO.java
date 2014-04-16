//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.TipoPagoAfip;

/**
 * Value Object del OtrosPagos
 * @author tecso
 *
 */
public class OtrosPagosVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "otrosPagosVO";	

	private Date  		fechaPago;
	private Double		importePago;
	private Integer  	anio;		
	private Integer 	tipoPago;	
	private Integer 	periodoPago;	
	private String 		nroResolucion="";	
	private String 		numeroCuenta="";	
	private String		fechaPagoView=""; 
	
	private LocalVO		local 		= new LocalVO();

		
	// Constructores
	public OtrosPagosVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public OtrosPagosVO(int id, String desc) {
		super();
		setId(new Long(id));		
	}

	//	Getters y setters
	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
		this.fechaPagoView = DateUtil.formatDate(fechaPago, DateUtil.ddSMMSYYYY_MASK);
	}

	public Double getImportePago() {
		return importePago;
	}

	public void setImportePago(Double importePago) {
		this.importePago = importePago;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Integer getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(Integer tipoPago) {
		this.tipoPago = tipoPago;
	}

	public Integer getPeriodoPago() {
		return periodoPago;
	}

	public void setPeriodoPago(Integer periodoPago) {
		this.periodoPago = periodoPago;
	}

	public String getNroResolucion() {
		return nroResolucion;
	}

	public void setLocal(LocalVO local) {
		this.local = local;
	}

	public LocalVO getLocal() {
		return local;
	}

	public void setNroResolucion(String nroResolucion) {
		this.nroResolucion = nroResolucion;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	
	// View getters
	public String getFechaPagoView() {
		return fechaPagoView;
	}
	
	public String getImportePagoView() {
		return (this.importePago != null)?importePago.toString():"";
	}
	
	public String getAnioView() {
		return (this.anio != null)? anio.toString():"";
	}
	
	public String getTipoPagoView() {
		return TipoPagoAfip.getById(tipoPago).getValue();
	}
	
	public String getPeriodoPagoView() {
		return (this.periodoPago != null)? periodoPago.toString():"";
	}	

	public String getResolucionView(){
		return this.nroResolucion+"/"+this.anio;
	}
	
}
