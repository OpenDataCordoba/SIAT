//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

public class TipoCobVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private String codColumna = "";
	private String descripcion = "";
	private Integer tipoDato;
	private Long orden;
	private PartidaVO partida = new PartidaVO();
	
	private String tipoDatoView = "";
	private String ordenView = "";
	
	// Listas de Entidades Relacionadas con TipoCob
	private List<FolDiaCobColVO> listFolDiaCobCol = new ArrayList<FolDiaCobColVO>();
	
	//Constructores 
	public TipoCobVO(){
		super();
	}

	// Getters Y Setters
	public String getCodColumna() {
		return codColumna;
	}
	public void setCodColumna(String codColumna) {
		this.codColumna = codColumna;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Long getOrden() {
		return orden;
	}
	public void setOrden(Long orden) {
		this.orden = orden;
		this.ordenView = StringUtil.formatLong(orden);
	}
	public String getOrdenView() {
		return ordenView;
	}
	public void setOrdenView(String ordenView) {
		this.ordenView = ordenView;
	}
	public PartidaVO getPartida() {
		return partida;
	}
	public void setPartida(PartidaVO partida) {
		this.partida = partida;
	}
	public Integer getTipoDato() {
		return tipoDato;
	}
	public void setTipoDato(Integer tipoDato) {
		this.tipoDato = tipoDato;
		this.tipoDatoView = StringUtil.formatInteger(tipoDato);
	}
	public String getTipoDatoView() {
		return tipoDatoView;
	}
	public void setTipoDatoView(String tipoDatoView) {
		this.tipoDatoView = tipoDatoView;
	}
	public List<FolDiaCobColVO> getListFolDiaCobCol() {
		return listFolDiaCobCol;
	}
	public void setListFolDiaCobCol(List<FolDiaCobColVO> listFolDiaCobCol) {
		this.listFolDiaCobCol = listFolDiaCobCol;
	}
	
	
}
