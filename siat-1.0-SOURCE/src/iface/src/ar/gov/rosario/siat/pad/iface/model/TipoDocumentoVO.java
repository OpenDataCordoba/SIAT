//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class TipoDocumentoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private String descripcion = "";
    private String abreviatura = "";

	// View Constants


	// Constructores
    public TipoDocumentoVO(){}
    
    public TipoDocumentoVO(Long id)
    {
    	this.setId(id);
    }    

    public TipoDocumentoVO(Long id, String abreviatura, String descripcion){
    	super(id);
    	this.abreviatura = abreviatura;
    	this.descripcion = descripcion;
    }
    
    public TipoDocumentoVO(Long id, String abreviatura){
    	this(id, abreviatura, abreviatura);
    }
    
    public TipoDocumentoVO(long id, String abreviatura){
    	this(id, abreviatura, abreviatura);
    }
    
	// Getters y Setters    
	public String getAbreviatura() {
		return abreviatura;
	}
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	// View getters	
}
