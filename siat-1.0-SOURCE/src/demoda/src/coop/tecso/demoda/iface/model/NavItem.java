//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

public class NavItem  {
	
	private static final long serialVersionUID = -1L;
	private Long selectedId = 0L;
	private Long auxId = 0L;
	private String accion = "";
	private String metodo = "";

	public NavItem(String accion, String metodo, Long selectedId) {
		this.selectedId = selectedId;
		this.accion = accion;
		this.metodo = metodo;
	}

	public Long getSelectedId() {
		return this.selectedId;
	}
	public void setSelectedId(Long selectedId) {
		this.selectedId = selectedId;
	}

	public Long getAuxId() {
		return this.auxId;
	}
	public void setAuxId(Long auxId) {
		this.auxId = auxId;
	}

	public String getAccion() {
		return this.accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getMetodo() {
		return this.metodo;
	}
	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

}
