//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del Estado de la Planilla de Deuda Enviada al Procurador
 * @author tecso
 *
 */
public class EstPlaEnvDeuPrVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estPlaEnvDeuPrVO";
	
	private String desEstPlaEnvDeuPro;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public EstPlaEnvDeuPrVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstPlaEnvDeuPrVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEstPlaEnvDeuPro(desc);
	}

	// Getters y Setters
	public String getDesEstPlaEnvDeuPro() {
		return desEstPlaEnvDeuPro;
	}

	public void setDesEstPlaEnvDeuPro(String desEstPlaEnvDeuPro) {
		this.desEstPlaEnvDeuPro = desEstPlaEnvDeuPro;
	}
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
