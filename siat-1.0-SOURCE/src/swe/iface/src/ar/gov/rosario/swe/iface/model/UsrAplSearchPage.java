//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import ar.gov.rosario.swe.iface.util.SweSecurityConstants;


public class UsrAplSearchPage extends SwePageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "usrAplSearchPageVO";
	
	// fields length according to database tables structure
	public static int USER_NAME_LENGTH = 10;
		
	
	private String username = "";
	
	private AplicacionVO aplicacion = new AplicacionVO();
	
	
    public UsrAplSearchPage() {
        super(SweSecurityConstants.ABM_USUARIOS);
    }

    public UsrAplSearchPage(AplicacionVO aplicacionVO) {
    	this();
    	this.aplicacion = aplicacionVO;
    }
    
    public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public AplicacionVO getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(AplicacionVO aplicacion) {
		this.aplicacion = aplicacion;
	}

	public String infoString(){
		String infoString = 
			" username:          " + this.username + "\r\n" +
			" aplicacion.id:     " + this.getAplicacion().getId() + "\r\n" +
			" aplicacion.codigo: " + this.getAplicacion().getCodigo() + "\r\n";
		return infoString;
	}
	
	// Getter para struts ------------------------------------------------------------------------------------------------


}
