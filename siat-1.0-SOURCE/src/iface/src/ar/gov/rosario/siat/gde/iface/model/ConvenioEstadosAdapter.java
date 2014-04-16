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
 * Adapter del LiqConvenioCuenta
 * 
 * @author tecso
 */
public class ConvenioEstadosAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "convenioEstadosAdapterVO";
	
	private String desPlan;
    
	private LiqConvenioVO convenio = new LiqConvenioVO();
   
   private List<ConEstConVO> listConEstCon = new ArrayList<ConEstConVO>();
    // ---> Propiedades para la asignacion de permisos
   
    
    // Constructores
    public ConvenioEstadosAdapter(){
    	super(GdeSecurityConstants.LIQ_CONVENIOCUENTA);
    }
    
    //  Getters y Setters
	public LiqConvenioVO getConvenio() {
		return convenio;
	}
	public void setConvenio(LiqConvenioVO convenio) {
		this.convenio = convenio;
	}

	
	public List<ConEstConVO> getListConEstCon() {
		return listConEstCon;
	}

	public void setListConEstCon(List<ConEstConVO> listConEstCon) {
		this.listConEstCon = listConEstCon;
	}
	
	

	public String getDesPlan() {
		return desPlan;
	}

	public void setDesPlan(String desPlan) {
		this.desPlan = desPlan;
	}

	public String getName(){
		return NAME;
	}
	
	
	
}