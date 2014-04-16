//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;

/**
 * Adapter del ResLiqDeu
 * 
 * @author tecso
 */
public class ResLiqDeuAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "resLiqDeuAdapterVO";
	
    private ResLiqDeuVO resLiqDeu = new ResLiqDeuVO();
    
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
    
    // Constructores
    public ResLiqDeuAdapter(){
    	super(EmiSecurityConstants.ABM_RESLIQDEU);
    }
    
    //  Getters y Setters
	public ResLiqDeuVO getResLiqDeu() {
		return resLiqDeu;
	}

	public void setResLiqDeu(ResLiqDeuVO resLiqDeuVO) {
		this.resLiqDeu = resLiqDeuVO;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
}
