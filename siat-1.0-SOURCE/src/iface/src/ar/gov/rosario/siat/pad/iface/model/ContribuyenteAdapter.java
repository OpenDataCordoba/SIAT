//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;


/**
 * Adapter de Contribuyente
 * 
 * @author tecso
 */
public class ContribuyenteAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "contribuyenteAdapterVO";
	public static final String ENC_NAME = "encContribuyenteAdapterVO";	
	
    private ContribuyenteVO contribuyente = new ContribuyenteVO();
    private ContribuyenteDefinition contribuyenteDefinition = new ContribuyenteDefinition(); 

	// Constructores
    public ContribuyenteAdapter(){
    	super(PadSecurityConstants.ABM_CONTRIBUYENTE);
    	ACCION_MODIFICAR_ENCABEZADO = PadSecurityConstants.ABM_CONTRIBUYENTE_ENC; 
    }

    //  Getters y Setters
	public ContribuyenteVO getContribuyente() {
		return contribuyente;
	}
	public void setContribuyente(ContribuyenteVO contribuyente) {
		this.contribuyente = contribuyente;
	}
	
    public ContribuyenteDefinition getContribuyenteDefinition() {
		return contribuyenteDefinition;
	}
	public void setContribuyenteDefinition(
			ContribuyenteDefinition contribuyenteDefinition) {
		this.contribuyenteDefinition = contribuyenteDefinition;
	}
	
	public String getVerConAtrValEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(PadSecurityConstants.ABM_CONTRIBUYENTE_ATRIBUTO_VALOR, BaseSecurityConstants.VER);
	}
	
	public String getModificarConAtrValEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(PadSecurityConstants.ABM_CONTRIBUYENTE_ATRIBUTO_VALOR, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarConAtrValEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(PadSecurityConstants.ABM_CONTRIBUYENTE_ATRIBUTO_VALOR, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarConAtrValEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(PadSecurityConstants.ABM_CONTRIBUYENTE_ATRIBUTO_VALOR, BaseSecurityConstants.AGREGAR);
	}
	
	public String getModificarPersonaEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(PadSecurityConstants.ABM_PERSONA, BaseSecurityConstants.MODIFICAR);
	}
	
	public String getVerCuentaTitularEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(PadSecurityConstants.ABM_CONTRIBUYENTE_CUENTA_TITULAR, BaseSecurityConstants.VER);
	}

}