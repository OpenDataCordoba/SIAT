//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

public class FolDiaCobVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private Date fechaCob;
	private String descripcion = "";
	private Long idEstadoDia;
	private FolioVO folio = new FolioVO();
	private SiNo estaConciliado = SiNo.OpcionTodo;
	
	private Double total;
	
	private String fechaCobView = "";
	private String idEstadoDiaView = "";
	private String totalView;
	
	// Listas de Entidades Relacionadas con FolDiaCob
	private List<FolDiaCobColVO> listFolDiaCobCol = new ArrayList<FolDiaCobColVO>(); 
	
	//Constructores 
	public FolDiaCobVO(){
		super();
	}

	// Getters Y Setters
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getFechaCob() {
		return fechaCob;
	}
	public void setFechaCob(Date fechaCob) {
		this.fechaCob = fechaCob;
		this.fechaCobView = DateUtil.formatDate(fechaCob, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaCobView() {
		return fechaCobView;
	}
	public void setFechaCobView(String fechaCobView) {
		this.fechaCobView = fechaCobView;
	}
	public FolioVO getFolio() {
		return folio;
	}
	public void setFolio(FolioVO folio) {
		this.folio = folio;
	}
	public Long getIdEstadoDia() {
		return idEstadoDia;
	}
	public void setIdEstadoDia(Long idEstadoDia) {
		this.idEstadoDia = idEstadoDia;
		this.idEstadoDiaView = StringUtil.formatLong(idEstadoDia);
	}
	public String getIdEstadoDiaView() {
		return idEstadoDiaView;
	}
	public void setIdEstadoDiaView(String idEstadoDiaView) {
		this.idEstadoDiaView = idEstadoDiaView;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
		this.totalView = StringUtil.formatDouble(total);
	}
	public String getTotalView() {
		return totalView;
	}
	public void setTotalView(String totalView) {
		this.totalView = totalView;
	}
	public List<FolDiaCobColVO> getListFolDiaCobCol() {
		return listFolDiaCobCol;
	}
	public void setListFolDiaCobCol(List<FolDiaCobColVO> listFolDiaCobCol) {
		this.listFolDiaCobCol = listFolDiaCobCol;
	}
	public SiNo getEstaConciliado() {
		return estaConciliado;
	}
	public void setEstaConciliado(SiNo estaConciliado) {
		this.estaConciliado = estaConciliado;
	}

	/**
	 * Devuelve la Fecha de Cobranza si fue cargada o la Descripcion que la reemplaza. (Si no fue cargado ninguno devuelve "")
	 * @return
	 */
	public String getFechaOrDesc(){
		if(this.fechaCob != null)
			return this.fechaCobView;
		else
			return this.descripcion;
	}
	
}
