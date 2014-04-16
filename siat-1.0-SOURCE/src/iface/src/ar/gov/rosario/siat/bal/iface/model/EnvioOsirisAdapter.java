//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.pro.iface.model.ProcesoVO;


/**
 * Adapter de EnvioOsiris
 * 
 * @author tecso
 */
public class EnvioOsirisAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "envioOsirisAdapterVO";
	
	private EnvioOsirisVO envioOsiris = new EnvioOsirisVO();
		
    private ProcesoVO proceso = new ProcesoVO();
    private Boolean paramPeriodic = false;
    
    private Boolean paramForzar = false;
    
	public EnvioOsirisAdapter(){
		super(BalSecurityConstants.ABM_ENVIOOSIRIS);
	}

	// Getters y Setters
	public EnvioOsirisVO getEnvioOsiris() {
		return envioOsiris;
	}
	public void setEnvioOsiris(EnvioOsirisVO envioOsiris) {
		this.envioOsiris = envioOsiris;
	}
	public Boolean getParamPeriodic() {
		return paramPeriodic;
	}
	public void setParamPeriodic(Boolean paramPeriodic) {
		this.paramPeriodic = paramPeriodic;
	}
	public ProcesoVO getProceso() {
		return proceso;
	}
	public void setProceso(ProcesoVO proceso) {
		this.proceso = proceso;
	}
	public Boolean getParamForzar() {
		return paramForzar;
	}
	public void setParamForzar(Boolean paramForzar) {
		this.paramForzar = paramForzar;
	}

}
