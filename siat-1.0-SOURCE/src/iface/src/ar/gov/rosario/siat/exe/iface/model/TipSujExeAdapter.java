//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;

/**
 * Adapter del TipSujExe
 * 
 * @author tecso
 */
public class TipSujExeAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipSujExeAdapterVO";
	
    private TipSujExeVO tipSujExe = new TipSujExeVO();
    
    private List<RecursoVO>           listRecurso = new ArrayList<RecursoVO>();

    private List<ExencionVO> listExencion = new ArrayList<ExencionVO>();
    // Constructores
    public TipSujExeAdapter(){
    	super(ExeSecurityConstants.ABM_TIPSUJEXE);
    }
    
    //  Getters y Setters
	public TipSujExeVO getTipSujExe() {
		return tipSujExe;
	}

	public void setTipSujExe(TipSujExeVO tipSujExeVO) {
		this.tipSujExe = tipSujExeVO;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<ExencionVO> getListExencion() {
		return listExencion;
	}

	public void setListExencion(List<ExencionVO> listExencion) {
		this.listExencion = listExencion;
	}

	// View getters
}
