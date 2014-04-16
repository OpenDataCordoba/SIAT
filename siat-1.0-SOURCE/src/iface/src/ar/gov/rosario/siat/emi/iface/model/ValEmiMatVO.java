//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.EmiMatVO;

/**
 * Value Object del ValEmiMat
 * @author tecso
 *
 */
public class ValEmiMatVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "valEmiMatVO";
	
	private EmiMatVO emiMat	= new EmiMatVO();
	
	private String valores = "";
	
	// Constructores
	public ValEmiMatVO() {
		super();
	}

	// Getters y Setters
	public EmiMatVO getEmiMat() {
		return emiMat;
	}

	public void setEmiMat(EmiMatVO emiMat) {
		this.emiMat = emiMat;
	}

	public String getValores() {
		return valores;
	}

	public void setValores(String valores) {
		this.valores = valores;
	}
}
