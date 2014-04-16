//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

/**
 * Adapter de Archivos de Transacciones para Balance
 * 
 * @author tecso
 */
public class ArchivoAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "archivoAdapterVO";
	
	private ArchivoVO archivo = new ArchivoVO();

	public ArchivoAdapter(){
		super(BalSecurityConstants.ABM_ARCHIVO);
	}

	// Getters Y Setters
	public ArchivoVO getArchivo() {
		return archivo;
	}
	public void setArchivo(ArchivoVO archivo) {
		this.archivo = archivo;
	}

	
	
}
