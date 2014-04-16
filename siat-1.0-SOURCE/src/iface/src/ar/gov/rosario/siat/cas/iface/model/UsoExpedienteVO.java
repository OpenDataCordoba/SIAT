//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del UsoExpediente
 * @author tecso
 *
 */
public class UsoExpedienteVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "usoExpedienteVO";
	
	private String 		numero;
	private CasoVO 		caso = new CasoVO();
	private Date 		fechaAccion;
	private String 		descripcion;
	private SistemaOrigenVO sistemaOrigen = new SistemaOrigenVO();
	private AccionExpVO 	accionExp = new AccionExpVO();
	private CuentaVO 		cuenta = new CuentaVO();
	
	// Buss Flags
	
	
	// View Constants
	
	
	private String fechaAccionView = "";


	// Constructores
	public UsoExpedienteVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public UsoExpedienteVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDescripcion(desc);
	}

	// Getters y Setters
	public AccionExpVO getAccionExp() {
		return accionExp;
	}
	public void setAccionExp(AccionExpVO accionExp) {
		this.accionExp = accionExp;
	}

	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaAccion() {
		return fechaAccion;
	}
	public void setFechaAccion(Date fechaAccion) {
		this.fechaAccion = fechaAccion;
		this.fechaAccionView = DateUtil.formatDate(fechaAccion, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}

	public SistemaOrigenVO getSistemaOrigen() {
		return sistemaOrigen;
	}
	public void setSistemaOrigen(SistemaOrigenVO sistemaOrigen) {
		this.sistemaOrigen = sistemaOrigen;
	}
	
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public void setFechaAccionView(String fechaAccionView) {
		this.fechaAccionView = fechaAccionView;
	}
	public String getFechaAccionView() {
		return fechaAccionView;
	}

}
