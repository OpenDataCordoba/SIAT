//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import ar.gov.rosario.swe.iface.util.SweSecurityConstants;

public class RolAplSearchPage extends SwePageModel {

	private static final long serialVersionUID = 6323072649347322203L;
	public static final String NAME = "rolAplSearchPageVO";
	
	// business
	private String 				codigo = "";
	private String 				descripcion = "";
	private AplicacionVO  		aplicacion = new AplicacionVO();
	// utilizado para buscar los roles de aplicacion relacionados con un usuario de aplicacion
	private UsrAplVO            usrApl = new UsrAplVO();  

	public RolAplSearchPage() {
        super(SweSecurityConstants.ABM_ROLES);
    }
	
	public AplicacionVO getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(AplicacionVO aplicacion) {
		this.aplicacion = aplicacion;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public UsrAplVO getUsrApl() {
		return usrApl;
	}
	public void setUsrApl(UsrAplVO usrApl) {
		this.usrApl = usrApl;
	}

	
	

}
