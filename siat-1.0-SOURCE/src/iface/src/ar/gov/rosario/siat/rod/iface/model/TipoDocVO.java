//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del TipoDoc
 * @author tecso
 *
 */
public class TipoDocVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoDocVO";
	
	private Integer codTipoDoc;
	private String desTipoDoc="";
	
	// Buss Flags
	
	
	// View Constants
	private String codTipoDocView="";
	
	// Constructores
	public TipoDocVO() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoDocVO(Integer cod, String desc) {
		super();
		setCodTipoDoc(cod);
		setDesTipoDoc(desc);
	}

	
	
	public Integer getCodTipoDoc() {
		return codTipoDoc;
	}

	public void setCodTipoDoc(Integer codTipoDoc) {
		this.codTipoDoc = codTipoDoc;
		this.codTipoDocView = StringUtil.formatInteger(codTipoDoc);
	}

	public String getDesTipoDoc() {
		return desTipoDoc;
	}

	public void setDesTipoDoc(String desTipoDoc) {
		this.desTipoDoc = desTipoDoc;
	}


	
	// Getters y Setters

	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	
	
	public String getTipoDocView(){
		return getCodTipoDocView()+" - "+getDesTipoDoc();
	}

	public String getCodTipoDocView() {
		return codTipoDocView;
	}

	public void setCodTipoDocView(String codTipoDocView) {
		this.codTipoDocView = codTipoDocView;
	}
}
