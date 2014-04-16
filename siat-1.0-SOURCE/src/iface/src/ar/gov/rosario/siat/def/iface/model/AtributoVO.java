//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;


import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Atributos
 * @author tecso
 *
 */
public class AtributoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "atributoVO";
	
	private String codAtributo;
	private String desAtributo;
	private TipoAtributoVO tipoAtributo = new TipoAtributoVO();
	private DomAtrVO domAtr = new DomAtrVO();
	private String mascaraVisual;
	
	// Constructores
	public AtributoVO() {
		super();
	}
	
	public AtributoVO(int id, String desAtributo) {
		super(id);
		setDesAtributo(desAtributo);
	}

	// Getters y Setters
	public String getCodAtributo() {
		return codAtributo;
	}
	public void setCodAtributo(String codAtributo) {
		this.codAtributo = codAtributo;
	}

	public String getDesAtributo() {
		return desAtributo;
	}
	public void setDesAtributo(String desAtributo) {
		this.desAtributo = desAtributo;
	}

	public DomAtrVO getDomAtr() {
		return domAtr;
	}
	public void setDomAtr(DomAtrVO domAtr) {
		this.domAtr = domAtr;
	}

	public TipoAtributoVO getTipoAtributo() {
		return tipoAtributo;
	}
	public void setTipoAtributo(TipoAtributoVO tipoAtributo) {
		this.tipoAtributo = tipoAtributo;
	}

	public String getMascaraVisual() {
		return mascaraVisual;
	}

	public void setMascaraVisual(String mascaraVisual) {
		this.mascaraVisual = mascaraVisual;
	}

	// View Getters

}
