//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del HisEstCueExe
 * 
 * @author tecso
 */
public class HisEstCueExeAdapter extends SiatAdapterModel{
	
	public static final String NAME = "hisEstCueExeAdapterVO";
	
    private HisEstCueExeVO hisEstCueExe = new HisEstCueExeVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public HisEstCueExeAdapter(){
    	super(ExeSecurityConstants.ABM_HISESTCUEEXE);
    }
    
    //  Getters y Setters
	public HisEstCueExeVO getHisEstCueExe() {
		return hisEstCueExe;
	}

	public void setHisEstCueExe(HisEstCueExeVO hisEstCueExeVO) {
		this.hisEstCueExe = hisEstCueExeVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
	// View getters
}
