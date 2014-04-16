//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import ar.gov.rosario.swe.iface.util.SweSecurityConstants;

public class AplicacionSearchPage extends SwePageModel {

	public static final String NAME = "aplicacionSearchPageVO";
	
	// fields length according to database tables structure
	public static int CODIGO_LENGTH = 100;
	public static int DESCRIPCION_LENGTH = 100;

	private String 	    codigo="";
	private String      descripcion = "";
	private Long        idUsrAplFilter = 0L;
	
	/**
	 * Id de Usuario por el cual filtrar las aplicaciones segun la tabla swe_usrapladmswe
	 * @return
	 */
	public Long getIdUsrAplFilter() {
		return idUsrAplFilter;
	}
	/**
	 * Id de Usuario por el cual filtrar las aplicaciones segun la tabla swe_usrapladmswe
	 */
	public void setIdUsrAplFilter(Long idUsrAplFilter) {
		this.idUsrAplFilter = idUsrAplFilter;
	}
	public AplicacionSearchPage() {
        super(SweSecurityConstants.ABM_APLICACION);
    }
	// ------------------------ Getters y Setters --------------------------------//
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}		

	public String infoString(){
		String infoString = " descripcion: " + this.getDescripcion() + "\r\n" +  
							" codigo: " + this.getCodigo() + "\r\n" +
							" pageNumber: " + this.getPageNumber() + "\r\n";
		return infoString;
	}

	// Getter para struts ------------------------------------------------------------------------------------------------
	
}
