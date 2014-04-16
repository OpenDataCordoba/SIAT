//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;

/**
 * Adapter de la Excencion de un contribuyente 
 * 
 * @author tecso
 */
public class ContribExeAdapter extends SiatAdapterModel{

	public static final String NAME = "contribExeAdapterVO";

    private ContribExeVO contribExe = new ContribExeVO();

    private List<ExencionVO>  listExencion = new ArrayList<ExencionVO>();
    
    private boolean poseeExencion = false;
    
    // Constructores
    public ContribExeAdapter(){
    	super(ExeSecurityConstants.ABM_CONTRIBEXE);
    }
    
    //  Getters y Setters
	public ContribExeVO getContribExe() {
		return contribExe;
	}

	public void setContribExe(ContribExeVO contribExe) {
		this.contribExe = contribExe;
	}

	public List<ExencionVO> getListExencion() {
		return listExencion;
	}

	public void setListExencion(List<ExencionVO> listExencion) {
		this.listExencion = listExencion;
	}

	public boolean isPoseeExencion() {
		return poseeExencion;
	}
	public void setPoseeExencion(boolean poseeExencion) {
		this.poseeExencion = poseeExencion;
	}

	// Metodos para la seguridad en la vista de los broches
	public String getAsignarBrocheEnabled() {
		return SiatBussImageModel.hasEnabledFlag(PadSecurityConstants.ABM_BROCHE, PadSecurityConstants.ABM_BROCHE_ADM_BROCHE_CUENTA);
	}

	public boolean getPoseeBroche() {
		return !ModelUtil.isNullOrEmpty(getContribExe().getBroche());
	}

}
