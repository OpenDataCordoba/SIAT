//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del usoCdM
 * @author tecso
 *
 */
public class UsoCdMVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "UsoCdMVO";
	
	private String desUsoCdM;
	
	private Double factor;
	
	private String usosCatastro;
	
	// View Constants
	private String factorView = "";
	
	// Constructores
	public UsoCdMVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public UsoCdMVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesUsoCdM(desc);
	}

	// Getters y Setters
	public String getDesUsoCdM() {
		return desUsoCdM;
	}

	public void setDesUsoCdM(String desUsoCdM) {
		this.desUsoCdM = desUsoCdM;
	}

	
	public Double getFactor() {
		return factor;
	}

	public void setFactor(Double factor) {
		this.factor = factor;
		this.factorView = StringUtil.formatDouble(factor);
	}

	public String getUsosCatastro() {
		return usosCatastro;
	}

	public void setUsosCatastro(String usosCatastro) {
		this.usosCatastro = usosCatastro;
	}

	//View Constants
	public String getFactorView() {
		return factorView;
	}

	public void setFactorView(String factorView) {
		this.factorView = factorView;
	}	
	
}
