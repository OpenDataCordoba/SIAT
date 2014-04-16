//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del HabLoc
 * @author tecso
 *
 */
public class HabLocVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "habLocVO";
	
	private LocalVO	    local = new LocalVO();
	
	private Long	 	codRubro;	
	
	private Date 		fechaHabilitacion;

	private String  	numeroCuenta="";	
	
	private String  	fechaHabilitacionView="";	

	// Constructores
	public HabLocVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public HabLocVO(int id) {
		super();
		setId(new Long(id));		
	}	
	
	// Getters y Setters
	public Long getCodRubro() {
		return codRubro;
	}

	public Date getFechaHabilitacion() {
		return fechaHabilitacion;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setCodRubro(Long codRubro) {
		this.codRubro = codRubro;
	}

	public void setLocal(LocalVO local) {
		this.local = local;
	}

	public LocalVO getLocal() {
		return local;
	}

	public void setFechaHabilitacion(Date fechaHabilitacion) {
		this.fechaHabilitacion = fechaHabilitacion;
		this.fechaHabilitacionView = DateUtil.formatDate(fechaHabilitacion, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	
	// View getters
	public String getFechaHabilitacionView() {
		return fechaHabilitacionView;
	}
	
	public String getCodRubroView() {
		return codRubro.toString();
	}
}
