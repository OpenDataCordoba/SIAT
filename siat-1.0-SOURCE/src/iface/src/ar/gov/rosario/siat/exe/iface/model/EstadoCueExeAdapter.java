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
import coop.tecso.demoda.iface.model.TipoEstadoCueExe;

/**
 * Adapter del EstadoCueExe
 * 
 * @author tecso
 */
public class EstadoCueExeAdapter extends SiatAdapterModel{
	
	public static final String NAME = "estadoCueExeAdapterVO";
	
    private EstadoCueExeVO estadoCueExe = new EstadoCueExeVO();
    
    private List<SiNo>  listSiNo = new ArrayList<SiNo>();
    private List<TipoEstadoCueExe>  listTipoEstadoCueExe = new ArrayList<TipoEstadoCueExe>();
    
    // Constructores
    public EstadoCueExeAdapter(){
    	super(ExeSecurityConstants.ABM_ESTADOCUEEXE);
    }
    
    //  Getters y Setters
	public EstadoCueExeVO getEstadoCueExe() {
		return estadoCueExe;
	}

	public void setEstadoCueExe(EstadoCueExeVO estadoCueExeVO) {
		this.estadoCueExe = estadoCueExeVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public List<TipoEstadoCueExe> getListTipoEstadoCueExe() {
		return listTipoEstadoCueExe;
	}

	public void setListTipoEstadoCueExe(List<TipoEstadoCueExe> listTipoEstadoCueExe) {
		this.listTipoEstadoCueExe = listTipoEstadoCueExe;
	}
	
	// View getters
}
