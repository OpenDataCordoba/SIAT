//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;

/**
 * SearchPage del Domicilio
 * 
 * @author Tecso
 *
 */
public class DomicilioSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "domicilioSearchPageVO";
	
	private DomicilioVO domicilio= new DomicilioVO();
	
	// Constructor
	public DomicilioSearchPage() {       
       super(PadSecurityConstants.ABM_DOMICILIO);        
    }

	public DomicilioSearchPage(DomicilioVO domicilioVO) {       
	       this();
	       if(domicilioVO != null){
	    	   this.domicilio = domicilioVO;
	       }
	    }
	
	// Getters y Setters
		public DomicilioVO getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(DomicilioVO domicilio) {
		this.domicilio = domicilio;
	}


}
