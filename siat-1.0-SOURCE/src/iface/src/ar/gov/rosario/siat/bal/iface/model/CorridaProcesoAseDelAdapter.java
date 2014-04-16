//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

/**
 * Adapter de Corridas del Asentamiento Delegado sin uso de ADP
 * 
 * @author tecso
 */
public class CorridaProcesoAseDelAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "corridaProcesoAseDelAdapterVO";
	
	private AseDelVO aseDel = new AseDelVO(); 
 
	// Constructores
	public CorridaProcesoAseDelAdapter() {       
    }

	// Getters y Setters
	public AseDelVO getAseDel() {
		return aseDel;
	}
	public void setAseDel(AseDelVO aseDel) {
		this.aseDel = aseDel;
	}

}
