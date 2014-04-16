//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.iface.model;

import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Aplicaciones
 * @author tecso
 *
 */
public class AplicacionVO extends SweBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "aplicacionVO";
	
	private String codigo;
	private String descripcion;
	private Long segTimeOut = new Long(0);
	private Integer maxNivelMenu = null; // NO: new Integer(0);
	private String  maxNivelMenuView = "";
	private TipoAuthVO tipoAuth = new TipoAuthVO();

	public AplicacionVO() {
		super();
	}

	public AplicacionVO(Long id, String cod, String desc) {
		super();
		setId(id);
		setCodigo(cod);
		setDescripcion(desc);
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Long getSegTimeOut() {
		return this.segTimeOut;
	}
	public void setSegTimeOut(Long segTimeOut) {
		this.segTimeOut = segTimeOut;
	}
	public void setTipoAuth(TipoAuthVO tipoAuth) {
		this.tipoAuth = tipoAuth;
	}

	public TipoAuthVO getTipoAuth() {
		return tipoAuth;
	}

	public Integer getMaxNivelMenu() {
		return maxNivelMenu;
	}
	public void setMaxNivelMenu(Integer maxNivelMenu) {
		this.maxNivelMenu = maxNivelMenu;
		this.maxNivelMenuView = StringUtil.formatInteger(maxNivelMenu);
	}
	public String getMaxNivelMenuView() {
		return maxNivelMenuView;
	}
	public void setMaxNivelMenuView(String maxNivelMenuView) {
		this.maxNivelMenuView = maxNivelMenuView;
	}

}
