//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoCarga
 * @author tecso
 *
 */
public class TipoCargaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoCargaVO";
	
	private String codTipoCarga="";
	private String desTipoCarga="";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipoCargaVO() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoCargaVO(String cod, String desc) {
		super();
		setCodTipoCarga(cod);
		setDesTipoCarga(desc);
	}

	// Getters y Setters	
	
	public String getCodTipoCarga() {
		return codTipoCarga;
	}

	public void setCodTipoCarga(String codTipoCarga) {
		this.codTipoCarga = codTipoCarga;
	}

	public String getDesTipoCarga() {
		return desTipoCarga;
	}

	public void setDesTipoCarga(String desTipoCarga) {
		this.desTipoCarga = desTipoCarga;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	
	public String getTipoCargaView(){
		return getCodTipoCarga()+" - "+getDesTipoCarga();
	}
}
