//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;

/**
 * SearchPage del TipSujExe
 * 
 * @author Tecso
 *
 */
public class TipSujExeSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipSujExeSearchPageVO";
	
	private TipSujExeVO tipSujExe= new TipSujExeVO();
	
	// Constructores
	public TipSujExeSearchPage() {       
       super(ExeSecurityConstants.ABM_TIPSUJEXE);        
    }
	
	// Getters y Setters
	public TipSujExeVO getTipSujExe() {
		return tipSujExe;
	}
	public void setTipSujExe(TipSujExeVO tipSujExe) {
		this.tipSujExe = tipSujExe;
	}

	// View getters
}
