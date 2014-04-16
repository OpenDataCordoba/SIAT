//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del ExeRecCon
 * 
 * @author tecso
 */
public class ExeRecConAdapter extends SiatAdapterModel{
	
	public static final String NAME = "exeRecConAdapterVO";
	
    private ExeRecConVO exeRecCon = new ExeRecConVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    private List<RecConVO>		 listRecCon = new ArrayList<RecConVO>(); 
    
    
    // Constructores
    public ExeRecConAdapter(){
    	super(ExeSecurityConstants.ABM_EXERECCON);
    }
    
    //  Getters y Setters
	public ExeRecConVO getExeRecCon() {
		return exeRecCon;
	}

	public void setExeRecCon(ExeRecConVO exeRecConVO) {
		this.exeRecCon = exeRecConVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public List<RecConVO> getListRecCon() {
		return listRecCon;
	}

	public void setListRecCon(List<RecConVO> listRecCon) {
		this.listRecCon = listRecCon;
	}

	
	
}