//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;

/**
 * SearchPage del Informacion de Cuenta para Emision
 * 
 * @author Tecso
 *
 */
public class EmiInfCueSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "emiInfCueSearchPageVO";
	
	private EmiInfCueVO emiInfCue = new EmiInfCueVO();
	
	private ResLiqDeuVO resLiqDeu = new ResLiqDeuVO();

	// Constructores
	public EmiInfCueSearchPage() {       
       super(EmiSecurityConstants.ABM_RESLIQDEU);        
    }

	// Getters y Setters
	public EmiInfCueVO getEmiInfCue() {
		return emiInfCue;
	}

	public void setEmiInfCue(EmiInfCueVO emiInfCue) {
		this.emiInfCue = emiInfCue;
	}

	public ResLiqDeuVO getResLiqDeu() {
		return resLiqDeu;
	}

	public void setResLiqDeu(ResLiqDeuVO resLiqDeu) {
		this.resLiqDeu = resLiqDeu;
	}
	
}
