//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;

/**
 * Adapter de Exenciones
 * 
 * @author tecso
 */
public class HabExeAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "habExeAdapterVO";

	private List<ExencionVO> listExencion = new ArrayList<ExencionVO>();
	HabExeVO habExe = new HabExeVO();
	
	
	public HabExeAdapter(){
		super(EspSecurityConstants.ABM_HABEXE);
	}

	// Getters & Setters
	public HabExeVO getHabExe() {
		return habExe;
	}
	public void setHabExe(HabExeVO habExe) {
		this.habExe = habExe;
	}

	public List<ExencionVO> getListExencion() {
		return listExencion;
	}

	public void setListExencion(List<ExencionVO> listExencion) {
		this.listExencion = listExencion;
	}
	
	

}
