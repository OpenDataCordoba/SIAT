//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;

/**
 * Value Object del Desglose
 * @author tecso
 *
 */
public class DesgloseVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "desgloseVO";
	
	private CasoVO caso= new CasoVO();
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public DesgloseVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public DesgloseVO(int id, String desc) {
		super();
		setId(new Long(id));
	
	}

	public CasoVO getCaso() {
		return caso;
	}

	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	
	// Getters y Setters


	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
