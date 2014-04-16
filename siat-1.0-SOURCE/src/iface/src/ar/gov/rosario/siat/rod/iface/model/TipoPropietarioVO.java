//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del TipoPropietario
 * @author tecso
 *
 */
public class TipoPropietarioVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoPropietarioVO";
	
	private Integer codTipoProp;
	private String desTipoProp="";
	
	// Buss Flags
	
	
	// View Constants
	private String codTipoPropView="";
	
	// Constructores
	public TipoPropietarioVO() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoPropietarioVO(Integer cod, String desc) {
		super();
		setCodTipoProp(cod);
		setDesTipoProp(desc);
	}

	// Getters y Setters
	
	public Integer getCodTipoProp() {
		return codTipoProp;
	}

	public void setCodTipoProp(Integer codTipoProp) {
		this.codTipoProp = codTipoProp;
		this.codTipoPropView = StringUtil.formatInteger(codTipoProp);
	}

	public String getDesTipoProp() {
		return desTipoProp;
	}

	public void setDesTipoProp(String desTipoProp) {
		this.desTipoProp = desTipoProp;
	}

	// View getters
	
	public String getCodTipoPropView() {
		return codTipoPropView;
	}

	public void setCodTipoPropView(String codTipoPropView) {
		this.codTipoPropView = codTipoPropView;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	public String getTipoPropView(){
		return getCodTipoPropView()+" - "+getDesTipoProp();
	}

}
