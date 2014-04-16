//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

/**
 * Adapter del DetalleDJ
 * 
 * @author tecso
 */
public class DetalleDJAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "detalleDJAdapterVO";
	
    private DetalleDJVO detalleDJ = new DetalleDJVO();
    
    private Boolean paramContenidoParseado = false;
        
    // Constructores
    public DetalleDJAdapter(){
    	super(BalSecurityConstants.ABM_DETALLEDJ);
    }
    
    //  Getters y Setters
	public DetalleDJVO getDetalleDJ() {
		return detalleDJ;
	}

	public void setDetalleDJ(DetalleDJVO detalleDJVO) {
		this.detalleDJ = detalleDJVO;
	}
	
	public String getName(){
		return NAME;
	}

	public Boolean getParamContenidoParseado() {
		return paramContenidoParseado;
	}

	public void setParamContenidoParseado(Boolean paramContenidoParseado) {
		this.paramContenidoParseado = paramContenidoParseado;
	}
			
	
}