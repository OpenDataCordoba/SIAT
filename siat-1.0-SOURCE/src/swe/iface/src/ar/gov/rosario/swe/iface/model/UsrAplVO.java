//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import java.util.Date;

import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class UsrAplVO extends SweBussImageModel {
	
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "usrAplVO";
	
	private String       username   = "";
	private String       password   = "";
	private String       passRetype = "";	
	private AplicacionVO aplicacion = new AplicacionVO();
	private Long         uid        = new Long(0); 
	private Date         fechaAlta ;
	private Date         fechaBaja;        
	
	private String uidView       = "";
	private String fechaAltaView = "";
	private String fechaBajaView = "";
	private Long[] listIdsAppSelected;
	
	private PermiteWeb permiteWeb = PermiteWeb.NO_PERMITE_WEB;
	
	public UsrAplVO(){
		super();
	}

	public AplicacionVO getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(AplicacionVO aplicacion) {
		this.aplicacion = aplicacion;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uidView = StringUtil.formatLong(uid);
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public void setPassRetype(String passRetype) {
		this.passRetype = passRetype;
	}
	public String getPassRetype() {
		return passRetype;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		
        this.fechaAltaView = DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_MASK);
		this.fechaAlta = fechaAlta;
	}

	public Long[] getListIdsAppSelected() {
		return listIdsAppSelected;
	}

	public void setListIdsAppSelected(Long[] listIdsAppSelected) {
		this.listIdsAppSelected = listIdsAppSelected;
	}
	
	public PermiteWeb getPermiteWeb() {
		return permiteWeb;
	}

	public void setPermiteWeb(PermiteWeb permiteWeb) {
		this.permiteWeb = permiteWeb;
	}

	
	
	public String getFechaAltaView() {
		return fechaAltaView;
	}
	public void setFechaAltaView(String fechaAltaView) {
		this.fechaAltaView = fechaAltaView;
	}
	public Date getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(Date fechaBaja) {
		this.fechaBajaView = DateUtil.formatDate(fechaBaja, DateUtil.ddSMMSYYYY_MASK);
		this.fechaBaja = fechaBaja;
	}
	public String getFechaBajaView() {
		return fechaBajaView;
	}
	public void setFechaBajaView(String fechaBajaView) {
		this.fechaBajaView = fechaBajaView;
	}
	public String getUidView() {
		return uidView;
	}
	public void setUidView(String uidView) {
		this.uidView = uidView;
	}
	
}
