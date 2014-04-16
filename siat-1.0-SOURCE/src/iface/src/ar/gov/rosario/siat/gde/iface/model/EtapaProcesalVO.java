//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del EtapaProcesal
 * @author tecso
 *
 */
public class EtapaProcesalVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "etapaProcesalVO";
	
	private String desEtapaProcesal;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public EtapaProcesalVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EtapaProcesalVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEtapaProcesal(desc);
	}

	// Getters y Setters
	public String getDesEtapaProcesal() {
		return desEtapaProcesal;
	}

	public void setDesEtapaProcesal(String desEtapaProcesal) {
		this.desEtapaProcesal = desEtapaProcesal;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
