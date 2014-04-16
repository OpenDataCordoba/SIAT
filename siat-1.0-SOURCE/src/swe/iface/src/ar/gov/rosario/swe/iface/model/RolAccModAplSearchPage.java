//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.swe.iface.util.SweSecurityConstants;

public class RolAccModAplSearchPage extends SwePageModel {

	private static final long serialVersionUID = 221378660853867731L;
	public static final String NAME = "rolAccModAplSearchPageVO";

	public RolAccModAplSearchPage() {
        super(SweSecurityConstants.ABM_ACCIONESROL);
    }
	
	private List<ModAplVO> 		listModApl = new ArrayList<ModAplVO>();	
	private String       		nombreAccion = "";
	private String       		nombreMetodo = "";
	
	private RolAplVO 			rolApl = new RolAplVO();
	private AplicacionVO		aplicacion= new AplicacionVO();
	private ModAplVO			modApl = new ModAplVO();
	
	public List<ModAplVO> getListModApl() {
		return listModApl;
	}
	public void setListModApl(List<ModAplVO> listModApl) {
		this.listModApl = listModApl;
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
	
	public AplicacionVO getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(AplicacionVO aplicacion) {
		this.aplicacion = aplicacion;
	}
	
	public RolAplVO getRolApl() {
		return rolApl;
	}
	
	public void setRolApl(RolAplVO rolApl) {
		this.rolApl = rolApl;
	}
	public ModAplVO getModApl() {
		return modApl;
	}
	public void setModApl(ModAplVO modApl) {
		this.modApl = modApl;
	}

	
	
}
