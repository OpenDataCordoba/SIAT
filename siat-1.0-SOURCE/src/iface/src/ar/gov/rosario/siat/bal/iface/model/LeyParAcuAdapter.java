//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;

/**
 * Adapter del LeyParAcu
 * 
 * @author tecso
 */
public class LeyParAcuAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "leyParAcuAdapterVO";

    private LeyParAcuVO leyParAcu = new LeyParAcuVO();
       
    // Constructores
    public LeyParAcuAdapter(){
    	super(BalSecurityConstants.ABM_LEYPARACU);
    }
    
    // Permisos para ABM ParCueBan
	public String getAgregarParCueBanEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_PARCUEBAN, BaseSecurityConstants.AGREGAR);
	}
	public String getVerParCueBanEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_PARCUEBAN, BaseSecurityConstants.VER);
	}
	public String getModificarParCueBanEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_PARCUEBAN, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarParCueBanEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_PARCUEBAN, BaseSecurityConstants.ELIMINAR);
	}	
    
    //  Getters y Setters
	public LeyParAcuVO getLeyParAcu() {
		return leyParAcu;
	}
	public void setLeyParAcu(LeyParAcuVO leyParAcuVO) {
		this.leyParAcu = leyParAcuVO;
	}


	public String getName(){
		return NAME;
	}
	

}
