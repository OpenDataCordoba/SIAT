//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Esta clase representa el tipo de documento.
 * Reemplaza jar mcr-frameworks.
 * 
 * @author leonardo.fagnano
 */
@Entity
@Table(name = "pad_tipodocumento")
public final class TipoDocumento  extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	private String descripcion;

    private String abreviatura;
   
    // Constructores
    public TipoDocumento() {
    }
   
    public TipoDocumento(String descripcion, String abreviatura) {
	super();
	this.descripcion = descripcion;
	this.abreviatura = abreviatura;
    }

    public TipoDocumento(Long id,String descripcion, String abreviatura) {
    	super(id);
    	this.descripcion = descripcion;
    	this.abreviatura = abreviatura;
   }

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

    public static List<TipoDocumento> getList() {    	
    	
    	return PadDAOFactory.getTipoDocumentoDAO().getList();
	}

    public static TipoDocumento getByIdNull(Long id) {
    	
    	return (TipoDocumento)PadDAOFactory.getTipoDocumentoDAO().getByIdNull(id);
	}
    
    public static TipoDocumento getById(Long id) throws Exception {
    	
    	return (TipoDocumento)PadDAOFactory.getTipoDocumentoDAO().getById(id);
	}

}
