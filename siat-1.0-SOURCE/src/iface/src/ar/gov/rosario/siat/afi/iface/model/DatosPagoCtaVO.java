//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.model.ImpuestoAfip;

/**
 * Value Object del DatosPagoCta
 * @author tecso
 *
 */
public class DatosPagoCtaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "datosPagoCtaVO";

	private Long  idCuenta;	 
	private Integer  codImpuesto;	
	private Double 	 totalMontoIngresado;
	private String	 numeroCuenta="";	
	
	private LocalVO	 local = new LocalVO();
	
	// Constructores
	public DatosPagoCtaVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public DatosPagoCtaVO(int id) {
		super();
		setId(new Long(id));	
	}
	
	// Getters y Setters
	public Long getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(Long idCuenta) {
		this.idCuenta = idCuenta;
	}

	public Integer getCodImpuesto() {
		return codImpuesto;
	}

	public void setLocal(LocalVO local) {
		this.local = local;
	}

	public LocalVO getLocal() {
		return local;
	}

	public void setCodImpuesto(Integer codImpuesto) {
		this.codImpuesto = codImpuesto;
	}

	public Double getTotalMontoIngresado() {
		return totalMontoIngresado;
	}

	public void setTotalMontoIngresado(Double totalMontoIngresado) {
		this.totalMontoIngresado = totalMontoIngresado;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	
	// View getters
	
	public String getIdCuentaView() {
		return (this.idCuenta!= null)?idCuenta.toString():"";
	}

	public String getCodImpuestoView() {
		return ImpuestoAfip.getById(this.codImpuesto).getFullValue();
	}
	
	public String getTotalMontoIngresadoView() {
		return (this.totalMontoIngresado!= null)?totalMontoIngresado.toString():"";
	}
	
}
