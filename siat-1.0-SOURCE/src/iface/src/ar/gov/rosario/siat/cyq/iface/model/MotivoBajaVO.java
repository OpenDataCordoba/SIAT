//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del MotivoBaja
 * @author tecso
 *
 */
public class MotivoBajaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "motivoBajaVO";
	
	private String desMotivoBaja;
	private SiNo devuelveDeuda = SiNo.OpcionSelecionar;
	private String tipo;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public MotivoBajaVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public MotivoBajaVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesMotivoBaja(desc);
	}
	
	// Getters y Setters
	public String getDesMotivoBaja() {
		return desMotivoBaja;
	}
	public void setDesMotivoBaja(String desMotivoBaja) {
		this.desMotivoBaja = desMotivoBaja;
	}

	public SiNo getDevuelveDeuda() {
		return devuelveDeuda;
	}
	public void setDevuelveDeuda(SiNo devuelveDeuda) {
		this.devuelveDeuda = devuelveDeuda;
	}

	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	
	
}
