//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

/**
 * Adapter de Dias de Cobranza
 * 
 * @author tecso
 */
public class FolDiaCobAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "folDiaCobAdapterVO";
	
	private FolDiaCobVO folDiaCob = new FolDiaCobVO();

	public FolDiaCobAdapter(){
		super(BalSecurityConstants.ABM_FOLDIACOB);
	}

	// Getters Y Setters
	public FolDiaCobVO getFolDiaCob() {
		return folDiaCob;
	}
	public void setFolDiaCob(FolDiaCobVO folDiaCob) {
		this.folDiaCob = folDiaCob;
	}
}
