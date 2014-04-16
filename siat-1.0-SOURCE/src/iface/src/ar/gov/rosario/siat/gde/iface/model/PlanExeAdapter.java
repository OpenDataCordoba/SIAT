//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del PlanExe
 * 
 * @author tecso
 */
public class PlanExeAdapter extends SiatAdapterModel{
	
	public static final String NAME = "planExeAdapterVO";
	
    private PlanExeVO planExe = new PlanExeVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    private List<ExencionVO>	listExencion = new ArrayList<ExencionVO>(); 
    private List<RecursoVO>		listRecurso = new ArrayList<RecursoVO>();
    
    // Constructores
    public PlanExeAdapter(){
    	super(GdeSecurityConstants.ABM_PLANEXE);
    }
    
    //  Getters y Setters
	public PlanExeVO getPlanExe() {
		return planExe;
	}

	public void setPlanExe(PlanExeVO planExeVO) {
		this.planExe = planExeVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	
	public List<ExencionVO> getListExencion() {
		return listExencion;
	}

	public void setListExencion(List<ExencionVO> listExencion) {
		this.listExencion = listExencion;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	
	
	
	// View getters
}
