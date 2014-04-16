//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;

/**
 * SearchPage del Localidad
 * 
 * @author Tecso
 *
 */
public class LocalidadSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "localidadSearchPageVO";
	
	private LocalidadVO localidad= new LocalidadVO();

	private List<ProvinciaVO> listProvincia = new ArrayList<ProvinciaVO>();
	
	
	// Constructor
	public LocalidadSearchPage() {       
       super(PadSecurityConstants.ABM_LOCALIDAD);        
    }
	
	// Getters y Setters
	public LocalidadVO getLocalidad() {
		return localidad;
	}
	public void setLocalidad(LocalidadVO localidad) {
		this.localidad = localidad;
	}
	

	public List<ProvinciaVO> getListProvincia() {
		return listProvincia;
	}
	public void setListProvincia(List<ProvinciaVO> listProvincia) {
		this.listProvincia = listProvincia;
	}

}
