//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import coop.tecso.demoda.iface.helper.StringUtil;

public class EstOtrIngTesVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private String desEstOtrIngTes = "";
	private String tipo = "";
	private String transiciones = "";
	private AreaVO area = new AreaVO();
	private Integer permiteModificar;
	private Integer esInicial;
	private Long idEstEnOtr;
		
	private String permiteModificarView = "";
	private String esInicialView = "";
	private String idEstEnOtrView = "";
	
	// Constructores 
	public EstOtrIngTesVO(){
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstOtrIngTesVO(int id, String desEstOtrIngTes) {
		super();
		setId(new Long(id));
		setDesEstOtrIngTes(desEstOtrIngTes);
	}

	// Getters y Setters
	public AreaVO getArea() {
		return area;
	}
	public void setArea(AreaVO area) {
		this.area = area;
	}
	public String getDesEstOtrIngTes() {
		return desEstOtrIngTes;
	}
	public void setDesEstOtrIngTes(String desEstOtrIngTes) {
		this.desEstOtrIngTes = desEstOtrIngTes;
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
	public Long getIdEstEnOtr() {
		return idEstEnOtr;
	}
	public void setIdEstEnOtr(Long idEstEnOtr) {
		this.idEstEnOtr = idEstEnOtr;
		this.idEstEnOtrView = StringUtil.formatLong(idEstEnOtr);
	}
	public String getIdEstEnOtrView() {
		return idEstEnOtrView;
	}
	public void setIdEstEnOtrView(String idEstEnOtrView) {
		this.idEstEnOtrView = idEstEnOtrView;
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
