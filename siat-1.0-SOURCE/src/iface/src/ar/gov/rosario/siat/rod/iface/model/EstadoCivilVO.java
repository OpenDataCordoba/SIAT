//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del EstadoCivil
 * @author tecso
 *
 */
public class EstadoCivilVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estadoCivilVO";
	
	private Integer codEstCiv;
	private String desEstCiv="";
	
	// Buss Flags
	
	
	// View Constants
	private String codEstCivView="";
	
	// Constructores
	public EstadoCivilVO() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstadoCivilVO(Integer cod, String desc) {
		super();
		setCodEstCiv(cod);
		setDesEstCiv(desc);
	}

	
	
	public Integer getCodEstCiv() {
		return codEstCiv;
	}

	public void setCodEstCiv(Integer codEstCiv) {
		this.codEstCiv = codEstCiv;
		this.codEstCivView = StringUtil.formatInteger(codEstCiv);
	}

	public String getDesEstCiv() {
		return desEstCiv;
	}

	public void setDesEstCiv(String desEstCiv) {
		this.desEstCiv = desEstCiv;
	}


	// View getters
	
	
	public String getCodEstCivView() {
		return codEstCivView;
	}

	public void setCodEstCivView(String codEstCivView) {
		this.codEstCivView = codEstCivView;
	}

	public String getEstadoCivilView(){
		return getCodEstCivView()+" - "+getDesEstCiv();
	}
	
	// Getters y Setters

	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
}
