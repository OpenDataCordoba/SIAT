//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Localidad
 * 
 * @author tecso
 *
 */
@Embeddable
public class Localidad extends BaseBO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long codPostal;
	
	private Long codSubPostal;

	@Transient
	private String descripcionPostal;
	@Transient
    private Provincia provincia;

	/**
	 * Constructor
	 */
	public Localidad(){
		
	}
	
	/**
	 * @return Devuelve el atributo codPostal.
	 */
	public Long getCodPostal() {
		return codPostal;
	}

	/**
	 * @param codPostal Fija el atributo codPostal.
	 */
	public void setCodPostal(Long codPostal) {
		this.codPostal = codPostal;
	}

	/**
	 * @return Devuelve el atributo codSubPostal.
	 */
	public Long getCodSubPostal() {
		return codSubPostal;
	}

	/**
	 * @param codSubPostal Fija el atributo codSubPostal.
	 */
	public void setCodSubPostal(Long codSubPostal) {
		this.codSubPostal = codSubPostal;
	}

	/**
	 * @return Devuelve el atributo descripcionPostal.
	 */
	public String getDescripcionPostal() {
		return descripcionPostal;
	}

	/**
	 * @param descripcionPostal Fija el atributo descripcionPostal.
	 */
	public void setDescripcionPostal(String descripcionPostal) {
		this.descripcionPostal = descripcionPostal;
	}

	public boolean isRosario() throws Exception{
        if (true) return false; //XXX demo rosario gpl
        
		Localidad rosario = Localidad.getRosario();
        
		Long COD_POSTAL_ROSARIO = rosario.getCodPostal();
        Long SUB_POSTAL_ROSARIO = rosario.getCodSubPostal();
        
        if (COD_POSTAL_ROSARIO != null && 
        		SUB_POSTAL_ROSARIO != null && 
        		COD_POSTAL_ROSARIO.equals(this.getCodPostal()) && 
        		SUB_POSTAL_ROSARIO.equals(this.getCodSubPostal())) {
            return true;
        }
        return false;
    }

    
    /**
     * @return Devuelve el atributo provincia.
     */
    public Provincia getProvincia() {
    
        return provincia;
    }

    
    /**
     * @param provincia Fija el atributo provincia
     */
    public void setProvincia(Provincia provincia) {
    
        this.provincia = provincia;
    }
	
    	
	public int hashCode(){
		
		return super.hashCode();
	}
	
	// Metodos de clase
	public static Localidad getByIdNull(Long id) throws Exception {
		return UbicacionFacade.getInstance().getLocalidad(id);
	}

	public static Localidad getById(Long id) throws Exception {

		Localidad localidad = UbicacionFacade.getInstance().getLocalidad(id);
		
		if (localidad == null){
			throw new Exception("localidad nula");
		}
		
		return localidad;
	}
	
	public static Localidad getByCodPostSubPost(Long codPostal, Long codSubPostal) throws Exception{
		
		return UbicacionFacade.getInstance().getLocalidad(codPostal, codSubPostal);
	}
	
	public static Localidad getRosario() throws Exception{
		return UbicacionFacade.getInstance().getRosario();
	}
	
}
