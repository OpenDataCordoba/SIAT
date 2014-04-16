//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.per.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.per.buss.dao.PerDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Esta clase representa una Calle tanto para el modelo como para el mapeo de
 * hibernate. Reemplaza la clase Calle de la framework de MCR 
 * 
 */
@Entity
@Table(name = "per_calle")
public class PerCalle extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "nomcalle")
	private String 	nombreCalle;
	
	@Column(name = "nomabrev")
	private String nombreAbreviado;

	@Column(name = "obscalle")
	private String observacion;
        
    /**
     * Constructor requerido por hibernate.
     */
    public PerCalle() {
    	super();
    }
    
    public PerCalle(String nombreCalle) {   	
    	super();
        this.nombreCalle = nombreCalle;        
    }

	// Getters y Setters
    public String getNombreCalle() {
		return nombreCalle;
	}
	public void setNombreCalle(String nombreCalle) {
		this.nombreCalle = nombreCalle;
	}

	public String getNombreAbreviado() {
		return nombreAbreviado;
	}

	public void setNombreAbreviado(String nombreAbreviado) {
		this.nombreAbreviado = nombreAbreviado;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	/**
	 * Obtiene una Calle a partir de su id
	 * @param id
	 * @return Calle
	 * @throws Exception
	 */
	public static PerCalle getByIdNull(Long id){
		
		return (PerCalle) PerDAOFactory.getPerCalleDAO().getByIdNull(id);
	}
	
	/** Recupera una lista de calles validas para
	 * un determinado nombre de calle y altura
	 * 
	 */
	public static List<PerCalle> getListCalle(String nombreCalle, Long altura) throws Exception{

		return null;

	}

	/** Recupera una lista de calles validas para
	 * un determinado nombre de calle
	 * 
	 */
	public static List<PerCalle> getListCalle(String nombreCalle) throws Exception {

		List<PerCalle> listCalles = 
			PerDAOFactory.getPerCalleDAO().getListCalleByNombre(nombreCalle);

		return listCalles;
	}

	/**
	 * Obtiene una calle desde por id de calle
	 * @param codCalle id de la calle
	 * @return
	 * @throws Exception
	 */
	public static PerCalle getById(Long codCalle){		
		return (PerCalle) PerDAOFactory.getPerCalleDAO().getById(codCalle);
	}

	public static PerCalle getByNombre(String nombreCalle) {		
		return (PerCalle) PerDAOFactory.getPerCalleDAO().getByNombre(nombreCalle);
	}
	
}
