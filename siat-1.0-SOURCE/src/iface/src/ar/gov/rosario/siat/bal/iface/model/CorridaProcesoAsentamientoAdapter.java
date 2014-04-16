//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

/**
 * Adapter de Corridas del Asentamiento sin uso de ADP
 * 
 * @author tecso
 */
public class CorridaProcesoAsentamientoAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "corridaProcesoAsentamientoAdapterVO";
	
	private AsentamientoVO asentamiento = new AsentamientoVO(); 
 
	// Constructores
	public CorridaProcesoAsentamientoAdapter() {       
    }

	// Getters y Setters
	public AsentamientoVO getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(AsentamientoVO asentamiento) {
		this.asentamiento = asentamiento;
	}

}
