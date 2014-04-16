//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Adapter de la Cuenta del Titular (Contribuyente)
 * 
 * @author tecso
 */
public class CuentaTitularAdapter extends SiatAdapterModel{
	
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "cuentaTitularAdapterVO";
	
    private CuentaTitularVO cuentaTitular = new CuentaTitularVO();
    
    private List<TipoTitularVO> listTipoTitular = new ArrayList<TipoTitularVO>();
    
    private List<SiNo> listEsTitularPrincipal = new ArrayList<SiNo>();
    
    // Constructores
    public CuentaTitularAdapter(){
    	super(PadSecurityConstants.ABM_CONTRIBUYENTE_CUENTA_TITULAR);
    }

    //  Getters y Setters
	public CuentaTitularVO getCuentaTitular() {
		return cuentaTitular;
	}
	public void setCuentaTitular(CuentaTitularVO cuentaTitular) {
		this.cuentaTitular = cuentaTitular;
	}
	public List<TipoTitularVO> getListTipoTitular() {
		return listTipoTitular;
	}
	public void setListTipoTitular(List<TipoTitularVO> listTipoTitular) {
		this.listTipoTitular = listTipoTitular;
	}
	public List<SiNo> getListEsTitularPrincipal() {
		return listEsTitularPrincipal;
	}
	public void setListEsTitularPrincipal(List<SiNo> listEsTitularPrincipal) {
		this.listEsTitularPrincipal = listEsTitularPrincipal;
	}
	

}