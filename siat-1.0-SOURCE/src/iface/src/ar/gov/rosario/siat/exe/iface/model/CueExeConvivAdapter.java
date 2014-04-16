//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;

/**
 * Adapter del CueExeConviv
 * 
 * @author tecso
 */
public class CueExeConvivAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "cueExeConvivAdapterVO";
	
    private ConvivienteVO conviviente = new ConvivienteVO();
    
    private CueExeVO cueExe = new CueExeVO(); 
    
    
    private boolean esExencionJubilado = false;
    private boolean poseeSolicFechas  = false;

    // flags
	private boolean agregarConvivEnabled= true;

    // Constructores
    public CueExeConvivAdapter(){
    	super(ExeSecurityConstants.ABM_CUEEXECONVIV);
    }

    //  Getters y Setters
	public ConvivienteVO getConviviente() {
		return conviviente;
	}

	public void setConviviente(ConvivienteVO conviviente) {
		this.conviviente = conviviente;
	}

	public CueExeVO getCueExe() {
		return cueExe;
	}

	public void setCueExe(CueExeVO cueExe) {
		this.cueExe = cueExe;
	}

	public boolean getEsExencionJubilado() {
		return esExencionJubilado;
	}

	public void setEsExencionJubilado(boolean esExencionJubilado) {
		this.esExencionJubilado = esExencionJubilado;
	}

	public boolean getPoseeSolicFechas() {
		return poseeSolicFechas;
	}

	public void setPoseeSolicFechas(boolean poseeSolicFechas) {
		this.poseeSolicFechas = poseeSolicFechas;
	}
    
	// flag getters
	public String getAgregarConvivEnabled() {
		return SiatBussImageModel.hasEnabledFlag(agregarConvivEnabled, ExeSecurityConstants.ABM_CUEEXECONVIV, BaseSecurityConstants.AGREGAR);
	}

	// View getters
	
}
