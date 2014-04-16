//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;

/**
 * SearchPage de Calle
 * 
 * @author Tecso
 *
 */
public class CalleSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "calleSearchPageVO";
	
	private CalleVO calle = new  CalleVO();
	
	// Constructor
	public CalleSearchPage() {       
       super(PadSecurityConstants.BUSQUEDA_CALLE);        
    }


	// Getters y Setters
	public CalleVO getCalle() {
		return calle;
	}
	public void setCalle(CalleVO calle) {
		this.calle = calle;
	}
	
}
