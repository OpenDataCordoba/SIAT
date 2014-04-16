//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del HistCodEmi
 * @author tecso
 *
 */
public class HistCodEmiVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "histCodEmiVO";
	
	private CodEmiVO codEmi = new CodEmiVO();
	
	private String desHistCodEmi;
	
	// Constructores
	public HistCodEmiVO() {
		super();
	}
	
	// Getters y Setters
	public CodEmiVO getCodEmi() {
		return codEmi;
	}

	public void setCodEmi(CodEmiVO codEmi) {
		this.codEmi = codEmi;
	}

	public String getDesHistCodEmi() {
		return desHistCodEmi;
	}

	public void setDesHistCodEmi(String desHistCodEmi) {
		this.desHistCodEmi = desHistCodEmi;
	}
	
	public String getFechaUltMdfView() {
		return DateUtil.formatDate(getFechaUltMdf(), DateUtil.ddSMMSYYYY_HH_MM_MASK);
	}
}
