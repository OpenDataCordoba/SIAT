//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;

/**
 * Adapter de BroCue
 * 
 * @author tecso
 */
public class BroCueAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "broCueAdapterVO";

	private BroCueVO broCue = new BroCueVO();
	
	private int paramTipoBroche = 0;
	
	public BroCueAdapter(){
		super(PadSecurityConstants.ABM_BROCUE);
	}

	// Getters y Setters
	
	public BroCueVO getBroCue() {
		return broCue;
	}
	public void setBroCue(BroCueVO broCue) {
		this.broCue = broCue;
	}
	public int getParamTipoBroche() {
		return paramTipoBroche;
	}
	public void setParamTipoBroche(int paramTipoBroche) {
		this.paramTipoBroche = paramTipoBroche;
	}
	
}
