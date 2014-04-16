//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * ModApl
 * @author tecso 
 */
public class AccModAplVO extends SweBussImageModel {
	
	private ModAplVO     modApl = new ModAplVO();
	private String       nombreAccion = "";
	private String       nombreMetodo = "";
	private String       descripcion = "";	

	public AccModAplVO() {
		super();
	}

	public ModAplVO getModApl() {
		return modApl;
	}

	public void setModApl(ModAplVO modApl) {
		this.modApl = modApl;
	}

	public String getNombreAccion() {
		return nombreAccion;
	}

	public void setNombreAccion(String nombreAccion) {
		this.nombreAccion = nombreAccion;
	}

	public String getNombreMetodo() {
		return nombreMetodo;
	}

	public void setNombreMetodo(String nombreMetodo) {
		this.nombreMetodo = nombreMetodo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getModAplView(){
		if (ModelUtil.isNullOrEmpty(this.modApl)){
			return StringUtil.NO_POSEE_DESCRIPCION;
		}
		return this.modApl.getNombreModulo();
	}

	/**
	 * Concatena el Modulo Accion - Metodo
	 * @return String
	 */
	public String getModAccMetAplView(){
		return this.getModApl().getNombreModulo() + " " + 
		this.getNombreAccion() + " - " + this.getNombreMetodo();
	}

	
}
