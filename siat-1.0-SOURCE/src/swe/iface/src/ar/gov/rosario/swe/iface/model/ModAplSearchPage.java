//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
public class ModAplSearchPage extends SwePageModel {

	public static final String NAME = "modAplSearchPageVO";

	private String 	      nombreModulo = "";
	private AplicacionVO  aplicacion = new AplicacionVO();
                                                                                                             	                                                                                                                   
	public ModAplSearchPage() {
        super(SweSecurityConstants.ABM_MODULOS);
    }
	// ------------------------ Getters y Setters --------------------------------//

	public AplicacionVO getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(AplicacionVO aplicacion) {
		this.aplicacion = aplicacion;
	}
	public String getNombreModulo() {
		return nombreModulo;
	}
	public void setNombreModulo(String nombreModulo) {
		this.nombreModulo = nombreModulo;
	}

	public String infoString(){
		String infoString = " nombreModulo: " + this.getNombreModulo() + "\r\n" +  
							" aplicacion: "   + this.getAplicacion()   + "\r\n" +
							" pageNumber: "   + this.getPageNumber()   + "\r\n";
		return infoString;
	}

	// Getter para struts ------------------------------------------------------------------------------------------------
	
}
