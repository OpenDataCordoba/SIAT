//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.EmiMatVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;

/**
 * Adapter del ValEmiMat
 * 
 * @author tecso
 */
public class ValEmiMatAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "valEmiMatAdapterVO";
	
    private ValEmiMatVO valEmiMat = new ValEmiMatVO();
    
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
    
    private List<EmiMatVO> listEmiMat = new ArrayList<EmiMatVO>();
    
    // Constructores
    public ValEmiMatAdapter(){
    	super(EmiSecurityConstants.ABM_VALEMIMAT);
    }
    
    //  Getters y Setters
	public ValEmiMatVO getValEmiMat() {
		return valEmiMat;
	}

	public void setValEmiMat(ValEmiMatVO valEmiMatVO) {
		this.valEmiMat = valEmiMatVO;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<EmiMatVO> getListEmiMat() {
		return listEmiMat;
	}

	public void setListEmiMat(List<EmiMatVO> listEmiMat) {
		this.listEmiMat = listEmiMat;
	}
}
