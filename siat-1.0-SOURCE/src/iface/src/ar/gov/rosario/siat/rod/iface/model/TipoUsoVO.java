//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del TipoUso
 * @author tecso
 *
 */
public class TipoUsoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoUsoVO";
	
	private Integer codUso;
	private String desUso="";
	
	// Buss Flags
	
	
	// View Constants
	private String codUsoView;
	
	// Constructores
	public TipoUsoVO() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoUsoVO(Integer cod, String desc) {
		super();
		setCodUso(cod);
		setDesUso(desc);
	}

	
	
	public Integer getCodUso() {
		return codUso;
	}

	public void setCodUso(Integer codUso) {
		this.codUso = codUso;
		this.codUsoView = StringUtil.formatInteger(codUso);
	}

	public String getDesUso() {
		return desUso;
	}

	public void setDesUso(String desUso) {
		this.desUso = desUso;
	}

	public String getCodUsoView() {
		return codUsoView;
	}

	public void setCodUsoView(String codUsoView) {
		this.codUsoView = codUsoView;
	}

	public String getTipoUsoView(){
		return getCodUsoView()+" - "+getDesUso();
	}
	
	// Getters y Setters

	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
