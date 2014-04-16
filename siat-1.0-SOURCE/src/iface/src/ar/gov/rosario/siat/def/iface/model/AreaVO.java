//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;


import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.seg.iface.model.OficinaVO;

/**
 * Area
 * @author tecso
 *
 */
public class AreaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "areaOrigenVO";
	
	private String codArea = "";
	private String desArea = "";
	private List<OficinaVO> listOficina = new ArrayList<OficinaVO>();
	private List<RecursoAreaVO> listRecursoArea = new ArrayList<RecursoAreaVO>();
	
	// Buss Flag
	private Boolean admOficinaBussEnabled = true;

	// Constructores
	public AreaVO() {
		super();
        // Acciones y Metodos para seguridad
	}

	public AreaVO(int id, String desArea) {
		super();
		setId(new Long(id));
		setDesArea(desArea);
	}

	// Getters y Setters	
	public String getCodArea() {
		return codArea;
	}
	public void setCodArea(String codArea) {
		this.codArea = codArea;
	}
	public String getDesArea() {
		return desArea;
	}
	public void setDesArea(String desArea) {
		this.desArea = desArea;
	}

	public Boolean getAdmOficinaBussEnabled() {
		return admOficinaBussEnabled;
	}

	public void setAdmOficinaBussEnabled(Boolean admOficinaBussEnabled) {
		this.admOficinaBussEnabled = admOficinaBussEnabled;
	}

	public String getAdmOficinaEnabled() {
		//por ahora no hacen falta definir permisos
		return getAdmOficinaBussEnabled() ? "enabled" : "disabled";
	}

	public List<OficinaVO> getListOficina() {
		return listOficina;
	}

	public void setListOficina(List<OficinaVO> listOficina) {
		this.listOficina = listOficina;
	}
	
    public List<RecursoAreaVO> getListRecursoArea() {
    	return listRecursoArea;
    }
    public void setListRecursoArea(List<RecursoAreaVO> listRecursoArea) {
    	this.listRecursoArea = listRecursoArea;
    }
}
