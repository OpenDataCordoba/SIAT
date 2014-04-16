//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del Marca
 * @author tecso
 *
 */
public class MarcaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "marcaVO";
	
	private Integer codMarca;
	private String desMarca="";
	
	// Buss Flags
	
	
	// View Constants
	private String codMarcaView="";
	
	
	// Constructores
	public MarcaVO() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public MarcaVO(Integer cod, String desc) {
		super();
		setCodMarca(cod);
		setDesMarca(desc);
	}

	// Getters y Setters
	
	public Integer getCodMarca() {
		return codMarca;
	}

	public void setCodMarca(Integer codMarca) {
		this.codMarca = codMarca;
		this.codMarcaView = StringUtil.formatInteger(codMarca);
	}

	public String getDesMarca() {
		return desMarca;
	}

	public void setDesMarca(String desMarca) {
		this.desMarca = desMarca;
	}

	// View getters
	
	public String getCodMarcaView() {
		return codMarcaView;
	}

	public void setCodMarcaView(String codMarcaView) {
		this.codMarcaView = codMarcaView;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
}
