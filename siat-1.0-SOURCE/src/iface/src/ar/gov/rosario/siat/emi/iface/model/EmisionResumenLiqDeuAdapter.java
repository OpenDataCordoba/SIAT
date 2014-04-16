//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Adapter del Emision de CdM
 * 
 * @author tecso
 */
public class EmisionResumenLiqDeuAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "emisionResumenLiqDeuAdapterVO";

	private Date fechaVencimiento;
	
	// fecha de vencimiento de View
	private String fechaVencimientoView = "";
	
    // Constructores
    public EmisionResumenLiqDeuAdapter(){
    	super(EmiSecurityConstants.ABM_RESLIQDEU);
    }
    
    //  Getters y Setters
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
		this.fechaVencimientoView = DateUtil.formatDate(fechaVencimiento, DateUtil.ddSMMSYYYY_MASK);
	}

	// View getters
	public String getFechaVencimientoView() {
		return fechaVencimientoView;
	}

	public void setFechaVencimientoView(String fechaVencimientoView) {
		this.fechaVencimientoView = fechaVencimientoView;
	}
}