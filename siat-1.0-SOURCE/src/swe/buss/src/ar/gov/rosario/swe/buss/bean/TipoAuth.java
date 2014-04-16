//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ar.gov.rosario.swe.SweDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

@Entity
@Table(name = "swe_tipoauth")
@SequenceGenerator(
		name="tipoauth_seq", 
		sequenceName="swe_tipoauth_id_seq",  
		allocationSize = 0
		)
public class TipoAuth extends BaseBO{
	

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator="tipoauth_seq",strategy=GenerationType.SEQUENCE)
    private Long id;
 
    public Long getId() {
        return id;
    }
    
    @Column(name = "descripcion")
    private String descripcion; 
    
    public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
	public static List<TipoAuth> getListActivos() {                        
        return (ArrayList<TipoAuth>) SweDAOFactory.getTipoAuthDAO().getListActiva();
	}

	public static TipoAuth getById(Long id) {
		return (TipoAuth)SweDAOFactory.getTipoAuthDAO().getById(id);		
	}
	
}