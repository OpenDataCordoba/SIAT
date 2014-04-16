//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.per.buss.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.per.buss.dao.PerDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Localidad
 * 
 * @author leonardo.fagnano
 *
 */
@Entity
@Table(name = "per_localidad")
public class PerLocalidad extends BaseBO {
	
	private static final long serialVersionUID = 1L;	
	
	@Id @GeneratedValue
	private Long id;
	
	public Long getId() {
        return id;
    }

	@Column(name = "codPostal")
	private String codPostal;
	
	@Column(name = "descripcion")
	private String descripcion;
	

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idProvincia") 
	private PerProvincia provincia;
	

	/**
	 * Constructor
	 */
	public PerLocalidad(){
		
	}
	
	public String getCodPostal() {
		return codPostal;
	}


	public void setCodPostal(String codPostal) {
		this.codPostal = codPostal;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	

	public boolean isRosario() throws Exception{
        
		PerLocalidad rosario = PerLocalidad.getRosario();
        
		String COD_POSTAL_ROSARIO = rosario.getCodPostal();  
        
        if (COD_POSTAL_ROSARIO != null  && 
        		COD_POSTAL_ROSARIO.equals(this.getCodPostal())) {
            return true;
        }
        return false;
    }

    
    /**
     * @return Devuelve el atributo provincia.
     */
    public PerProvincia getProvincia() {
    
        return provincia;
    }

    
    /**
     * @param provincia Fija el atributo provincia
     */
    public void setProvincia(PerProvincia provincia) {
    
        this.provincia = provincia;
    }
	
    
	public boolean equals(Object obj){
		
		return true;
		
	}
	
	public int hashCode(){
		
		return super.hashCode();
	}
	
	// Metodos de clase
	public static PerLocalidad getByIdNull(Long id) throws Exception {
		return (PerLocalidad) PerDAOFactory.getPerLocalidadDAO().getByIdNull(id);
	}

	public static PerLocalidad getById(Long id){

		return(PerLocalidad) PerDAOFactory.getPerLocalidadDAO().getById(id);	
		
	}
	
	public static PerLocalidad getByCodPostal(Long codPostal) throws Exception{
		
		return PerDAOFactory.getPerLocalidadDAO().getByCodPostal(codPostal);
	}
	
	public static PerLocalidad getRosario() throws Exception{
		//Hardcode Rosario
		return PerDAOFactory.getPerLocalidadDAO().getByExactName("ROSARIO");
	}
	
}
