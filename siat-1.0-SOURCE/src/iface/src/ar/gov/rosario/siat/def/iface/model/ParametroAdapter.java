//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;

/**
 * Adapter del Parametro
 * 
 * @author tecso
 */
public class ParametroAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "parametroAdapterVO";
	
    private ParametroVO parametro = new ParametroVO();
    
    // Constructores
    public ParametroAdapter(){
    	super(DefSecurityConstants.ABM_PARAMETRO);
    }
    
    //  Getters y Setters
	public ParametroVO getParametro() {
		return parametro;
	}

	public void setParametro(ParametroVO parametroVO) {
		this.parametro = parametroVO;
	}

	// View getters
}
