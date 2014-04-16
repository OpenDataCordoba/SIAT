//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter de Cobranza
 * 
 * @author tecso
 */
public class CobranzaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "cobranzaAdapterVO";
	
	public static final String NAME_ENC="cobranzaEncAdapterVO";
	
	
	
	private CobranzaVO cobranza = new CobranzaVO();
	
	private GesCobVO gesCob=new GesCobVO();
	
	private List<EstadoCobranzaVO> listEstadoCobranza=new ArrayList<EstadoCobranzaVO>();
    
	private List<PerCobVO>listPerCob=new ArrayList<PerCobVO>();
	   
    // Constructores
    public CobranzaAdapter(){
    	super(GdeSecurityConstants.ABM_COBRANZA);
    }

    //  Getters y Setters

	public CobranzaVO getCobranza() {
		return cobranza;
	}

	public void setCobranza(CobranzaVO cobranza) {
		this.cobranza = cobranza;
	}

	public GesCobVO getGesCob() {
		return gesCob;
	}

	public void setGesCob(GesCobVO gesCob) {
		this.gesCob = gesCob;
	}

	public List<EstadoCobranzaVO> getListEstadoCobranza() {
		return listEstadoCobranza;
	}

	public void setListEstadoCobranza(List<EstadoCobranzaVO> listEstadoCobranza) {
		this.listEstadoCobranza = listEstadoCobranza;
	}

	public List<PerCobVO> getListPerCob() {
		return listPerCob;
	}

	public void setListPerCob(List<PerCobVO> listPerCob) {
		this.listPerCob = listPerCob;
	}

	
}