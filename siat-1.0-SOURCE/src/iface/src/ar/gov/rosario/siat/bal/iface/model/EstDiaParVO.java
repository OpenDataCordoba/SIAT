//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import coop.tecso.demoda.iface.helper.StringUtil;

public class EstDiaParVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private String desEstDiaPar = "";
	private String tipo = "";
	private String transiciones = "";
	private AreaVO area = new AreaVO();
	private String exCodigos = "";
	private Integer permiteModificar;
	private Integer esInicial;
	private Long idEstadoEnDiaPar;
	private Integer esResolucion;
	
	private String permiteModificarView = "";
	private String esInicialView = "";
	private String esResolucionView = "";
	private String idEstadoEnDiaParView = "";
	
	// Constructores 
	public EstDiaParVO(){
		super();
	}
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstDiaParVO(int id, String descripcion) {
		super();
		setId(new Long(id));
		setDesEstDiaPar(descripcion);
	}
	
	// Getters y Setters 
	public AreaVO getArea() {
		return area;
	}
	public void setArea(AreaVO area) {
		this.area = area;
	}
	public String getDesEstDiaPar() {
		return desEstDiaPar;
	}
	public void setDesEstDiaPar(String desEstDiaPar) {
		this.desEstDiaPar = desEstDiaPar;
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
	public Integer getEsResolucion() {
		return esResolucion;
	}
	public void setEsResolucion(Integer esResolucion) {
		this.esResolucion = esResolucion;
		this.esResolucionView = StringUtil.formatInteger(esResolucion);
	}
	public String getEsResolucionView() {
		return esResolucionView;
	}
	public void setEsResolucionView(String esResolucionView) {
		this.esResolucionView = esResolucionView;
	}
	public String getExCodigos() {
		return exCodigos;
	}
	public void setExCodigos(String exCodigos) {
		this.exCodigos = exCodigos;
	}
	public Long getIdEstadoEnDiaPar() {
		return idEstadoEnDiaPar;
	}
	public void setIdEstadoEnDiaPar(Long idEstadoEnDiaPar) {
		this.idEstadoEnDiaPar = idEstadoEnDiaPar;
		this.idEstadoEnDiaParView = StringUtil.formatLong(idEstadoEnDiaPar);
	}
	public String getIdEstadoEnDiaParView() {
		return idEstadoEnDiaParView;
	}
	public void setIdEstadoEnDiaParView(String idEstadoEnDiaParView) {
		this.idEstadoEnDiaParView = idEstadoEnDiaParView;
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
