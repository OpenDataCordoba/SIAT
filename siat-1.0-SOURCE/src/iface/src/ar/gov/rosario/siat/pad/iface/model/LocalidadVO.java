//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.BussImageModel;

/**
 * LocalidadVO
 * 
 * @author tecso
 *
 */
public class LocalidadVO extends BussImageModel {
	
	// Propiedades
	private Long codPostal;
	private Long codSubPostal;
	private String descripcionPostal;
    private ProvinciaVO provincia = new ProvinciaVO();
    
    /**
	 * Constructor
	 */
	public LocalidadVO(){
		
	}

	// Geters y Seters
	public Long getCodPostal() {
		return codPostal;
	}
	public void setCodPostal(Long codPostal) {
		this.codPostal = codPostal;
	}
	public Long getCodSubPostal() {
		return codSubPostal;
	}
	public void setCodSubPostal(Long codSubPostal) {
		this.codSubPostal = codSubPostal;
	}
	public String getDescripcionPostal() {
		return descripcionPostal;
	}
	public void setDescripcionPostal(String descripcionPostal) {
		this.descripcionPostal = descripcionPostal;
	}
    public ProvinciaVO getProvincia() {
    
        return provincia;
    }
    public void setProvincia(ProvinciaVO provincia) {
    
        this.provincia = provincia;
    }
	
    /**
     * 
     * @return
     */
    public LocalidadVO getDuplicate(){
    	
    	LocalidadVO localidadNueva = new LocalidadVO();
    	
    	localidadNueva.setCodPostal(codPostal);
    	localidadNueva.setCodSubPostal(codSubPostal);
    	localidadNueva.setDescripcionPostal(descripcionPostal);
    	if(provincia != null)
    	localidadNueva.setProvincia(provincia.getDuplicate());
    	
    	return localidadNueva;
    }
    
    public boolean getEsRosario() {
        
        final Long COD_POSTAL_ROSARIO = new Long(2000);
        final Long SUB_POSTAL_ROSARIO = new Long(8);
        
        if (COD_POSTAL_ROSARIO.equals(this.getCodPostal()) 
                && SUB_POSTAL_ROSARIO.equals(this.getCodSubPostal())) {
            return true;
        }
        return false;
        
    }
    
    public String getLocalidadPciaView(){
    	
    	String resultado = this.getDescripcionPostal();
    	String descProvincia = this.getProvincia().getDescripcion();
    	if (!StringUtil.isNullOrEmpty(descProvincia)){
    		resultado += " (" + this.getProvincia().getDescripcion() + " )";
    	}
    	return resultado;
    }
    
	public String getDescripcionPostalView() {

		String dp = "";
		if (this.getDescripcionPostal() != null) {
			dp = this.getDescripcionPostal();
		}

		return dp;
	}
   
}
