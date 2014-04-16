//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;


/**
 * Adapter de Dominio de Atributo
 * 
 * @author tecso
 */
public class DomAtrAdapter extends SiatAdapterModel{
	
	public static final String NAME = "domAtrAdapterVO";
	public static final String ENC_NAME = "encDomAtrAdapterVO";	
	
    private List<TipoAtributoVO> listTipoAtributo = new ArrayList<TipoAtributoVO>(); 
    private DomAtrVO             domAtr = new DomAtrVO();
    

    public DomAtrAdapter(){
    	super(DefSecurityConstants.ABM_DOMINIO_ATRIBUTO);
    	ACCION_MODIFICAR_ENCABEZADO = DefSecurityConstants.ABM_DOMINIO_ATRIBUTO_ENC;
    }
    
    public DomAtrAdapter(String sweActionName){
    	super(sweActionName);    	
    }

	public DomAtrVO getDomAtr() {
		return domAtr;
	}
	public void setDomAtr(DomAtrVO domAtr) {
		this.domAtr = domAtr;
	}
	public List<TipoAtributoVO> getListTipoAtributo() {
		return listTipoAtributo;
	}
	public void setListTipoAtributo(List<TipoAtributoVO> listTipoAtributo) {
		this.listTipoAtributo = listTipoAtributo;
	}

	public String getVerDomAtrValEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_DOMINIO_ATRIBUTO_VALOR, BaseSecurityConstants.VER);
	}
	
	public String getModificarDomAtrValEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_DOMINIO_ATRIBUTO_VALOR, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarDomAtrValEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_DOMINIO_ATRIBUTO_VALOR, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarDomAtrValEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_DOMINIO_ATRIBUTO_VALOR, BaseSecurityConstants.AGREGAR);
	}
	
}