//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del OpeInvCon
 * 
 * @author tecso
 */
public class OpeInvConAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "opeInvConAdapterVO";
	
    private OpeInvConVO opeInvCon = new OpeInvConVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
	private List<EstadoOpeInvConVO> listEstadoOpeInvCon = new ArrayList <EstadoOpeInvConVO>();
    
	// flags para ver observaciones del estado seleccinoado
	private boolean verObsClasificacion = false;
	private boolean verObsExclusion = false;
	
	// flags de permisos
	private boolean liquidacionDeudaEnabled = true;
	private boolean estadoCuentaEnabled=true;
	
    // Constructores
    public OpeInvConAdapter(){
    	super(EfSecurityConstants.ADM_OPEINVCON);
    }
    
    //  Getters y Setters
	public OpeInvConVO getOpeInvCon() {
		return opeInvCon;
	}

	public void setOpeInvCon(OpeInvConVO opeInvConVO) {
		this.opeInvCon = opeInvConVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public List<EstadoOpeInvConVO> getListEstadoOpeInvCon() {
		return listEstadoOpeInvCon;
	}

	public void setListEstadoOpeInvCon(List<EstadoOpeInvConVO> listEstadoOpeInvCon) {
		this.listEstadoOpeInvCon = listEstadoOpeInvCon;
	}
	
	
	public boolean isVerObsClasificacion() {
		return verObsClasificacion;
	}

	public void setVerObsClasificacion(boolean verObsClasificacion) {
		this.verObsClasificacion = verObsClasificacion;
	}

	public boolean isVerObsExclusion() {
		return verObsExclusion;
	}

	public void setVerObsExclusion(boolean verObsExclusion) {
		this.verObsExclusion = verObsExclusion;
	}

	// View getters
	public String getLiquidacionDeudaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(liquidacionDeudaEnabled, EfSecurityConstants.ADM_OPEINVCON, EfSecurityConstants.MTD_LIQUIDACION_DEUDA);
	}

	public void setLiquidacionDeudaEnabled(boolean liquidacionDeudaEnabled) {
		this.liquidacionDeudaEnabled = liquidacionDeudaEnabled;
	}

	public String getEstadoCuentaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(estadoCuentaEnabled, EfSecurityConstants.ADM_OPEINVCON, EfSecurityConstants.MTD_ESTADO_CUENTA);
	}

	public void setEstadoCuentaEnabled(boolean estadoCuentaEnabled) {
		this.estadoCuentaEnabled = estadoCuentaEnabled;
	}

	
}
