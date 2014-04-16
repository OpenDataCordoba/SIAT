//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del DesEspExe
 * 
 * @author tecso
 */
public class DesEspExeAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "desEspExeAdapterVO";
	
    private DesEspExeVO desEspExe = new DesEspExeVO();
    private List<ExencionVO> listExencion = new ArrayList<ExencionVO>();
    
    
    // Constructores
    public DesEspExeAdapter(){
    	super(GdeSecurityConstants.ABM_DESESPEXE);
    }
    
    //  Getters y Setters
	public DesEspExeVO getDesEspExe() {
		return desEspExe;
	}

	public void setDesEspExe(DesEspExeVO desEspExeVO) {
		this.desEspExe = desEspExeVO;
	}

	public List<ExencionVO> getListExencion() {
		return listExencion;
	}

	public void setListExencion(List<ExencionVO> listExencion) {
		this.listExencion = listExencion;
	}


	
	// View getters
}
