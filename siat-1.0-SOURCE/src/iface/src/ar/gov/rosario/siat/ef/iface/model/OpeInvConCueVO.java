//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;

/**
 * Value Object del OpeInvCon
 * @author tecso
 *
 */
public class OpeInvConCueVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "opeInvConCueVO";
	
	private OpeInvConVO opeInvCon = new OpeInvConVO();
	
	private CuentaVO cuenta = new CuentaVO();
	
	private boolean esSeleccionada = false;
	
	private String nroCuenta = "";
	
	private String desRecurso ="";
	
	// Buss Flags
	boolean seleccionarBussEnabled=true;
	
	// View Constants
	
	
	// Constructores
	public OpeInvConCueVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public OpeInvConCueVO(int id, String desc) {
		super();
		setId(new Long(id));
		cuenta.setNumeroCuenta(desc);
	}

	public OpeInvConVO getOpeInvCon() {
		return opeInvCon;
	}

	public void setOpeInvCon(OpeInvConVO opeInvCon) {
		this.opeInvCon = opeInvCon;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public boolean getEsSeleccionada() {
		return esSeleccionada;
	}

	public void setEsSeleccionada(boolean esSeleccionada) {
		this.esSeleccionada = esSeleccionada;
	}
	
	
	// Getters y Setters
		

	// Buss flags getters y setters
	public boolean getSeleccionarBussEnabled() {
		return seleccionarBussEnabled;
	}

	public void setSeleccionarBussEnabled(boolean seleccionarBussEnabled) {
		this.seleccionarBussEnabled = seleccionarBussEnabled;
	}	
	
	// View flags getters
	
	
	public String getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public String getDesRecurso() {
		return desRecurso;
	}

	public void setDesRecurso(String desRecurso) {
		this.desRecurso = desRecurso;
	}

	// View getters
	public String getSeleccionadaStr (){
		return (esSeleccionada?"SI":"NO");
	}


}
