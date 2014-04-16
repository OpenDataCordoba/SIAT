//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;



/**
 * Model de Rol Aplicacion
 * @author tesco
 */
public class RolAplVO extends SweBussImageModel {
		
	private static final long serialVersionUID = 1116007784822376503L;
	
	public static final String NAME = "rolAplVO";
	private String codigo = "";
	private String descripcion = "";
	private AplicacionVO aplicacion = new AplicacionVO();
	private PermiteWeb permiteWeb = PermiteWeb.NO_PERMITE_WEB;
	
	public RolAplVO() {
		super();
	}
	
	public AplicacionVO getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(AplicacionVO aplicacion) {
		this.aplicacion = aplicacion;
	}

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

	public PermiteWeb getPermiteWeb() {
		return permiteWeb;
	}

	public void setPermiteWeb(PermiteWeb permiteWeb) {
		this.permiteWeb = permiteWeb;
	}
}
