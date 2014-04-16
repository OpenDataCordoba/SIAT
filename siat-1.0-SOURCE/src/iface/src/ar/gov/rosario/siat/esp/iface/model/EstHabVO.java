//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import coop.tecso.demoda.iface.helper.StringUtil;

public class EstHabVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private String desEstHab = "";
	private String tipo = "";
	private String transiciones = "";
	private AreaVO area = new AreaVO();
	private Integer permiteModificar;
	private Integer esInicial;
	private Long idEstEnIng;
		
	private String permiteModificarView = "";
	private String esInicialView = "";
	private String idEstEnIngView = "";
	
	// Constructores 
	public EstHabVO(){
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstHabVO(int id, String desEstHab) {
		super();
		setId(new Long(id));
		setDesEstHab(desEstHab);
	}

	// Getters y Setters
	public AreaVO getArea() {
		return area;
	}
	public void setArea(AreaVO area) {
		this.area = area;
	}
	public String getDesEstHab() {
		return desEstHab;
	}
	public void setDesEstHab(String desEstHab) {
		this.desEstHab = desEstHab;
	}
	public Integer getEsInicial() {
		return esInicial;
	}
	public void setEsInicial(Integer esInicial) {
		this.esInicial = esInicial;
		this.esInicialView = StringUtil.formatInteger(esInicial);
	}
	public String getEsInicialView() {
		return esInicialView;
	}
	public void setEsInicialView(String esInicialView) {
		this.esInicialView = esInicialView;
	}
	public Long getIdEstEnIng() {
		return idEstEnIng;
	}
	public void setIdEstEnIng(Long idEstEnIng) {
		this.idEstEnIng = idEstEnIng;
		this.idEstEnIngView = StringUtil.formatLong(idEstEnIng);
	}
	public String getIdEstEnIngView() {
		return idEstEnIngView;
	}
	public void setIdEstEnIngView(String idEstEnIngView) {
		this.idEstEnIngView = idEstEnIngView;
	}
	public Integer getPermiteModificar() {
		return permiteModificar;
	}
	public void setPermiteModificar(Integer permiteModificar) {
		this.permiteModificar = permiteModificar;
		this.permiteModificarView = StringUtil.formatInteger(permiteModificar);
	}
	public String getPermiteModificarView() {
		return permiteModificarView;
	}
	public void setPermiteModificarView(String permiteModificarView) {
		this.permiteModificarView = permiteModificarView;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getTransiciones() {
		return transiciones;
	}
	public void setTransiciones(String transiciones) {
		this.transiciones = transiciones;
	}

}
