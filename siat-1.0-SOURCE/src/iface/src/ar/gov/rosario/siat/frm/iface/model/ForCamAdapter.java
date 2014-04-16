//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.frm.iface.util.FrmSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del ForCam
 * 
 * @author tecso
 */
public class ForCamAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "forCamAdapterVO";
	
    private ForCamVO forCam = new ForCamVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public ForCamAdapter(){
    	super(FrmSecurityConstants.ABM_FORCAM);
    }
    
    //  Getters y Setters
	public ForCamVO getForCam() {
		return forCam;
	}

	public void setForCam(ForCamVO forCamVO) {
		this.forCam = forCamVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
	// View getters
}
