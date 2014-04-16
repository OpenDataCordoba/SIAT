//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.swe.iface.util.SweSecurityConstants;

public class AccModAplSearchPage extends SwePageModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "accModAplSearchPageVO";
	
	public static final String INIT_FOR_MODULO    = "initForModulo";
	public static final String INIT_FOR_ROL       = "initForRol";
	public static final String INIT_FOR_ITEM_MENU = "initForItemMenu";

	private String          nombreAccion = "";
	private String 	        nombreMetodo = "";
	private ModAplVO 		modApl = new ModAplVO();
	private AplicacionVO 	aplicacion = new AplicacionVO();
	private List<ModAplVO> 	listModApl = new ArrayList<ModAplVO>();
	private List<String> listAcciones = new ArrayList<String>();
		
	// Usado para filtrar las Acciones Modulo que un rol no tenga asignado. 
	private RolAplVO 			rolApl = new RolAplVO(); 
	private String[]		listId;
	
	private boolean habilitarFiltroModulo = false; 
	
	
	private boolean multiSelectEnabled   = false; 
	
	private String initFor = INIT_FOR_MODULO;
	
	public AccModAplSearchPage() {
        super(SweSecurityConstants.ABM_ACCIONESMODULO);
    }
	// ------------------------ Getters y Setters --------------------------------//
	
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
	
	public boolean isMultiSelectEnabled() {
		return multiSelectEnabled;
	}
	public void setMultiSelectEnabled(boolean multiSelectEnabled) {
		this.multiSelectEnabled = multiSelectEnabled;
	}
	public List<ModAplVO> getListModApl() {
		return listModApl;
	}
	public void setListModApl(List<ModAplVO> listModApl) {
		this.listModApl = listModApl;
	}
	public String[] getListId() {
		return listId;
	}
	public void setListId(String[] listId) {
		this.listId = listId;
	}
	public String getInitFor() {
		return initFor;
	}
	public void setInitFor(String initFor) {
		this.initFor = initFor;
	}

	public String infoString() {
		String infoString = " nombreAccion: " + this.getNombreAccion() + "\r\n" +
							" nombreMetodo: " + this.getNombreMetodo() + "\r\n" +							
							" pageNumber: "   + this.getPageNumber() + "\r\n";
		return infoString;
	}

	public boolean getHabilitarFiltroModulo() {
		return habilitarFiltroModulo;
	}
	public void setHabilitarFiltroModulo(boolean filtraPorModulo) {
		this.habilitarFiltroModulo = filtraPorModulo;
	}


	public List<String> getListAcciones() {
		return listAcciones;
	}

	public void setListAcciones(List<String> listAcciones) {
		this.listAcciones = listAcciones;
	}



	
	
	
	// Getter para struts ------------------------------------------------------------------------------------------------

	
}
