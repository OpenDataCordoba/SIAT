//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del ConstanciaDeu
 * 
 * @author tecso
 */
public class ConDeuTitAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "conDeuTitAdapterVO";
	
   private ConDeuTitVO conDeuTit = new ConDeuTitVO();
   
   private String strDomEnv = "";
	
    // Constructores
    public ConDeuTitAdapter(){
    	super(GdeSecurityConstants.ADM_TITULARES_CONSTANCIA_DEUDA_JUDICIAL);    	
    }

    //  Getters y Setters
	public ConDeuTitVO getConDeuTit() {
		return conDeuTit;
	}

	public void setConDeuTit(ConDeuTitVO conDeuTit) {
		this.conDeuTit = conDeuTit;
	}

	public String getStrDomEnv() {
		return strDomEnv;
	}

	public void setStrDomEnv(String strDomEnv) {
		this.strDomEnv = strDomEnv;
	}
    
    
	// View getters

}
