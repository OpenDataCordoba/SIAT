//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del RecursoArea
 * 
 * @author tecso
 */
public class RecursoAreaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "recursoAreaAdapterVO";
	
	private RecursoAreaVO recursoArea = new RecursoAreaVO();

	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	private List<SiNo> listSiNo= new ArrayList<SiNo>();
	
    // Constructores
    
    //  Getters y Setters
	public RecursoAreaAdapter(){
		super(DefSecurityConstants.ABM_RECURSOAREA);
	}
	
	public RecursoAreaVO getRecursoArea() {
		return recursoArea;
	}
	public void setRecursoArea(RecursoAreaVO recursoArea) {
		this.recursoArea = recursoArea;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
    

	// View getters
}