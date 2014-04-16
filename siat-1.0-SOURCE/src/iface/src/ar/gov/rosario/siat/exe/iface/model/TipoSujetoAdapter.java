//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del TipoSujeto
 * 
 * @author tecso
 */
public class TipoSujetoAdapter extends SiatAdapterModel{
	
	public static final String NAME = "tipoSujetoAdapterVO";
	public static final String ENC_NAME = "encTipoSujetoAdapterVO";
	
    private TipoSujetoVO tipoSujeto = new TipoSujetoVO();
    

    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public TipoSujetoAdapter(){
    	super(ExeSecurityConstants.ABM_TIPOSUJETO);
    	ACCION_MODIFICAR_ENCABEZADO = ExeSecurityConstants.ABM_TIPOSUJETO_ENC;
    }
    
    //  Getters y Setters
	public TipoSujetoVO getTipoSujeto() {
		return tipoSujeto;
	}

	public void setTipoSujeto(TipoSujetoVO tipoSujetoVO) {
		this.tipoSujeto = tipoSujetoVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
	public String getAgregarTipSujExeEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(ExeSecurityConstants.ABM_TIPSUJEXE, BaseSecurityConstants.AGREGAR);
	}
	
	// flag getters
	public String getEliminarTipSujExeEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(ExeSecurityConstants.ABM_TIPSUJEXE, BaseSecurityConstants.ELIMINAR);
	}
	
	// View getters
	
	
}
