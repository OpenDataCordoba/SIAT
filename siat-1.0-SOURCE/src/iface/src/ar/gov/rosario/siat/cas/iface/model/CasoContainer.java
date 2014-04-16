//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;


import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Este objeto fue creado para los casos especialies donde no existe otro objeto que lo puede contener
 * 
 * @author tecso
 */
public class CasoContainer extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private CasoVO caso = new CasoVO();
	
	
	// Constructores
	public CasoContainer() {
		super();
	}

	// Getters y Setters
	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	
}
