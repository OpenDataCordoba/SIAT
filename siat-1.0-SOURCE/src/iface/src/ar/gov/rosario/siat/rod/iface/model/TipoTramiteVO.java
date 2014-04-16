//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del TipoTramite
 * @author tecso
 *
 */
public class TipoTramiteVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoTramiteVO";
	
	private Integer codTipoTramite;
	private String desTipoTramite="";
	private String rubros="";
	
	// Buss Flags
	
	
	// View Constants
	private String codTipoTramiteView="";
	
	// Constructores
	public TipoTramiteVO() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoTramiteVO(Integer cod, String desc) {
		super();
		setCodTipoTramite(cod);
		setDesTipoTramite(desc);
	}

	
	
	public Integer getCodTipoTramite() {
		return codTipoTramite;
	}

	public void setCodTipoTramite(Integer codTipoTramite) {
		this.codTipoTramite = codTipoTramite;
		this.codTipoTramiteView = StringUtil.formatInteger(codTipoTramite);
	}

	public String getDesTipoTramite() {
		return desTipoTramite;
	}

	public void setDesTipoTramite(String desTipoTramite) {
		this.desTipoTramite = desTipoTramite;
	}

	public String getRubros() {
		return rubros;
	}

	public void setRubros(String rubros) {
		this.rubros = rubros;
	}

	// View flags getters
	
	public String getTipoTramiteView() {
		return getCodTipoTramiteView()+" - "+getDesTipoTramite();
	}

	public String getCodTipoTramiteView() {
		return codTipoTramiteView;
	}

	public void setCodTipoTramiteView(String codTipoTramiteView) {
		this.codTipoTramiteView = codTipoTramiteView;
	}

	// Getters y Setters

	

	// Buss flags getters y setters

	
	// View getters
	
	
}
