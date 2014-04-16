//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.ObjImpVO;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;

/**
 * SearchPage del CuentaPorObjImp
 * 
 * @author Tecso
 *
 */
public class CuentaObjImpSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "cuentaObjImpSearchPageVO";
	
	private CuentaVO cuenta= new CuentaVO();
	
	private ObjImpVO objImp = new ObjImpVO();
	private TipObjImpDefinition tipObjImpDefinition = new TipObjImpDefinition();
	
	// Constructores
	public CuentaObjImpSearchPage() {       
       super(GdeSecurityConstants.ABM_CUENTA_OBJIMP);        
    }

	// Getters y Setters
	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public ObjImpVO getObjImp() {
		return objImp;
	}

	public void setObjImp(ObjImpVO objImp) {
		this.objImp = objImp;
	}
	
	public TipObjImpDefinition getTipObjImpDefinition() {
		return tipObjImpDefinition;
	}

	public void setTipObjImpDefinition(TipObjImpDefinition tipObjImpDefinition) {
		this.tipObjImpDefinition = tipObjImpDefinition;
	}

	// liquidacionDeudaEnabled
	public String getLiquidacionDeudaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ABM_CUENTA_OBJIMP, 
				GdeSecurityConstants.MTD_LIQUIDACION_DEUDA); 
	}
	// estadoCuentaEnabled
	public String getEstadoCuentaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ABM_CUENTA_OBJIMP, 
				GdeSecurityConstants.MTD_ESTADO_CUENTA); 
	}

}
