//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import coop.tecso.demoda.iface.model.BussImageModel;


public class ProvinciaVO extends BussImageModel {
	
	private static final long serialVersionUID = 1L;

	// Propiedades
    private String descripcion = "";

    public ProvinciaVO() {
        super();
    }
    
    public ProvinciaVO(Long id, String descripcion){
    	super(id);
    	this.descripcion = descripcion;
    }

    /**
     * Obtiene una instancia duplicada de la provinciaVO
     * @return ProvinciaVO
     */
	public ProvinciaVO getDuplicate(){
		
		return new ProvinciaVO(this.getId(), descripcion);
	}



    // Geters y Seters
    public String getDescripcion() {

        return descripcion;
    }
    public void setDescripcion(String descripcion) {

        this.descripcion = descripcion;
    }

}
