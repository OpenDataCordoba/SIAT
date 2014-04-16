//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;

public class ContribuyenteSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "contribuyenteSearchPageVO";
	
	public ContribuyenteSearchPage() {
    	super(PadSecurityConstants.ABM_CONTRIBUYENTE);
    }
	
	// Getters y Setters

}
