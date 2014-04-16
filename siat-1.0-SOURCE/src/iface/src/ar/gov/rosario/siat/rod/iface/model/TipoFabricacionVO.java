//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del TipoFabricacion
 * @author tecso
 *
 */
public class TipoFabricacionVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoFabricacionVO";
	
	private Integer codFab;
	private String desFab="";
	
	// Buss Flags
	
	
	// View Constants
	private String codFabView="";
	
	// Constructores
	public TipoFabricacionVO() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoFabricacionVO(Integer cod, String desc) {
		super();
		setCodFab(cod);
		setDesFab(desc);
	}

	// Getters y Setters
	
	public Integer getCodFab() {
		return codFab;
	}

	public void setCodFab(Integer codFab) {
		this.codFab = codFab;
		this.codFabView = StringUtil.formatInteger(codFab);
	}

	public String getDesFab() {
		return desFab;
	}

	public void setDesFab(String desFab) {
		this.desFab = desFab;
	}

	// View getters
	
	public String getCodFabView() {
		return codFabView;
	}

	public void setCodFabView(String codFabView) {
		this.codFabView = codFabView;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	public String getTipoFabView(){
		return getCodFabView()+" - "+getDesFab();
	}

}
