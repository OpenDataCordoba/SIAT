//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.per.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.gov.rosario.siat.per.buss.dao.PerDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Provincia
 * 
 * @author leonardo.fagnano
 *
 */
@Entity
@Table(name = "per_provincia")
public class PerProvincia extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue
	private Long id;
	
	public Long getId() {
        return id;
    }
	
	// Propiedades
	@Column(name = "descripcion")
    private String descripcion;

    // Constructores
    public PerProvincia() {
        super();
    }
    
    public PerProvincia(Long id, String descripcion) {
		super(id);
		this.descripcion = descripcion;
	}

	// Geters y Setters
    public String getDescripcion() {

        return descripcion;
    }
    public void setDescripcion(String descripcion) {

        this.descripcion = descripcion;
    }

    @SuppressWarnings("unchecked")
	public static List<PerProvincia> getList() {
    	return PerDAOFactory.getPerProvinciaDAO().getList();    	
	}

    public static PerProvincia getByIdNull(Long id) throws Exception {
    	return (PerProvincia) PerDAOFactory.getPerProvinciaDAO().getByIdNull(id);		
	} 
    
    public static PerProvincia getById(Long id) {
    	return (PerProvincia) PerDAOFactory.getPerProvinciaDAO().getById(id);
	}
}
