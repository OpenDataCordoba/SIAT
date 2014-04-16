//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del ExeActLoc
 * @author tecso
 *
 */
public class ExeActLocVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "exeActLocVO";
	
	private ActLocVO	actLoc = new ActLocVO();
	
	private Long  		idExencion;	

	private Long	   	codActividad;
	
	private Date   		fechaEmision;	

	private Date 		fechaDesde;	

	private Date 		fechaHasta;
	
	private String 		numeroCuenta="";			

	private String   	nroResolucion="";	

	private String   	fechaEmisionView="";	

	private String 		fechaDesdeView="";	

	private String 		fechaHastaView="";
	
	// Constructores
	public ExeActLocVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ExeActLocVO(int id) {
		super();
		setId(new Long(id));		
	}	
	
	// Getters y Setters

	public Long getIdExencion() {
		return idExencion;
	}

	public Long getCodActividad() {
		return codActividad;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public String getNroResolucion() {
		return nroResolucion;
	}

	public void setActLoc(ActLocVO actLoc) {
		this.actLoc = actLoc;
	}

	public ActLocVO getActLoc() {
		return actLoc;
	}

	public void setIdExencion(Long idExencion) {
		this.idExencion = idExencion;
	}

	public void setCodActividad(Long codActividad) {
		this.codActividad = codActividad;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
		this.fechaEmisionView = DateUtil.formatDate(fechaEmision, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public void setNroResolucion(String nroResolucion) {
		this.nroResolucion = nroResolucion;
	}
	
	// View getters
	public String getFechaEmisionView() {
		return fechaEmisionView;
	}

	public String getFechaDesdeView() {
		return fechaDesdeView;
	}

	public String getFechaHastaView() {
		return fechaHastaView;
	}	
	
	public String getIdExencionView() {
		return (this.idExencion != null)?idExencion.toString():"";
	}
	
	public String getCodActividadView() {
		return (this.codActividad != null)?codActividad.toString():"";
	}

}
