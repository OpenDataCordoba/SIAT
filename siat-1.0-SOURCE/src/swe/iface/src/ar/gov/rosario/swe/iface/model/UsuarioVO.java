//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import coop.tecso.demoda.iface.model.BussImageModel;

/**
 * Utilizada para el Login.
 * @author tecso
 *
 */
public class UsuarioVO extends BussImageModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "usuarioVO";
	
	private String username;
	private String password;
	
	private String newPassword;
	private String newPasswordReentry;
	
	private String apellido;
	private String nombres;
 
	private Date   fechaAlta;
	private Date   fechaBaja; 
		
	private String idSession;
	
	private List listUsuarioRol;
	
	private Log log = LogFactory.getLog(UsuarioVO.class);
		
	public UsuarioVO(){
		
	}
	
	public UsuarioVO(String username, String password) {
		super();
		this.password = password;
		this.username = username;
	}


	public UsuarioVO(String username, String password, String idSession) {
		super();
		
		this.username = username;
		this.password = password;
		this.idSession = idSession;
	}

	
	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getIdSession() {
		return idSession;
	}


	public void setIdSession(String idSession) {
		this.idSession = idSession;
	}
	
	
	
	
	public String getNewPassword() {
		return newPassword;
	}


	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	
	

	public String getNewPasswordReentry() {
		return newPasswordReentry;
	}


	public void setNewPasswordReentry(String newPasswordReentry) {
		this.newPasswordReentry = newPasswordReentry;
	}


	


	public String getApellido() {
		return apellido;
	}


	public void setApellido(String apellido) {
		this.apellido = apellido;
	}


	public Date getFechaAlta() {
		return fechaAlta;
	}


	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}


	public Date getFechaBaja() {
		return fechaBaja;
	}


	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}


	public Log getLog() {
		return log;
	}


	public void setLog(Log log) {
		this.log = log;
	}


	public String getNombres() {
		return nombres;
	}


	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	
	public List getListUsuarioRol() {
		return listUsuarioRol;
	}

	public void setListUsuarioRol(List listUsuarioRol) {
		this.listUsuarioRol = listUsuarioRol;
	}

	
}
