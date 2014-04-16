//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

/**
 * Clase de session de swe
 */
package ar.gov.rosario.swe.iface.model;


import coop.tecso.demoda.iface.model.DemodaUserSession;


public class SweUserSession extends DemodaUserSession {

	private static final long serialVersionUID = 1L;
	
	//private UsuarioVO usuario;
    private String idsAccionesModuloUsuario = ""; // Concatenacion de "," + id de las accion permitada para el usuario + ",".
    private String idsRoles = ""; // Concatenacion de "," + id de los roles para el usuario + ",".
    private String codsRoles = ""; // Concatenacion de "," + codigo de rol para el usuario + ",".
	private String longUserName = "";
	private String codApplication = "";
	private Long idUsuarioSwe = 0L;
	   
	public String getCodsRoles() {
		return codsRoles;
	}

	public void setCodsRoles(String codsRoles) {
		this.codsRoles = codsRoles;
	}

	public String getIdsRoles() {
		return idsRoles;
	}

	public void setIdsRoles(String idsRoles) {
		this.idsRoles = idsRoles;
	}

	public Long getIdUsuarioSwe() {
		return idUsuarioSwe;
	}
	public void setIdUsuarioSwe(Long idUsuarioSwe) {
		this.idUsuarioSwe = idUsuarioSwe;
	}
	public String getIdsAccionesModuloUsuario() {
		return idsAccionesModuloUsuario;
	}
	public void setIdsAccionesModuloUsuario(String idsAccionesModuloUsuario) {
		this.idsAccionesModuloUsuario = idsAccionesModuloUsuario;
	}
	/**
	 * @return the longUserName
	 */
	public String getLongUserName() {
		return longUserName;
	}
	/**
	 * @param longUserName the longUserName to set
	 */
	public void setLongUserName(String longUserName) {
		this.longUserName = longUserName;
	}
	/**
	 * @return the codApplication
	 */
	public String getCodApplication() {
		return codApplication;
	}
	/**
	 * @param codApplication the codApplication to set
	 */
	public void setCodApplication(String codApplication) {
		this.codApplication = codApplication;
	}
}
