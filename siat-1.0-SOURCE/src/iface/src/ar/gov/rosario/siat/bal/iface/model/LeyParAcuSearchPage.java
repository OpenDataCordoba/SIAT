//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;

/**
 * SearchPage del LeyParAcu
 * 
 * @author Tecso
 *
 */
public class LeyParAcuSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "leyParAcuSearchPageVO";
	
	private LeyParAcuVO leyParAcu= new LeyParAcuVO();
	
	// Constructores
	public LeyParAcuSearchPage() {       
       super(BalSecurityConstants.ABM_PARTIDA);        
    }
	
	// Getters y Setters
	public LeyParAcuVO getLeyParAcu() {
		return leyParAcu;
	}
	public void setLeyParAcu(LeyParAcuVO leyParAcu) {
		this.leyParAcu = leyParAcu;
	}

	public String getName(){
		return NAME;
	}
		
}
