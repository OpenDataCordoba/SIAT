//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * VO correspondiente a los recursos del procurador
 * 
 * @author tecso
 *
 */
public class ProRecVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "proRecVO";
	
	private ProcuradorVO procurador = new ProcuradorVO();
	private RecursoVO    recurso = new RecursoVO();
	private Date         fechaDesde;     // DATETIME YEAR TO DAY NOT NULL
	private Date         fechaHasta;     // DATETIME YEAR TO DAY NOT NULL
	
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	
	private List<ProRecDesHasVO> listProRecDesHas;
	private List<ProRecComVO> listProRecCom;
	
	// Contructores
	public ProRecVO(){
		super();
	}

	// Getters y Setters
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public ProcuradorVO getProcurador() {
		return procurador;
	}
	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}
	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	public List<ProRecDesHasVO> getListProRecDesHas() {
		return listProRecDesHas;
	}
	public void setListProRecDesHas(List<ProRecDesHasVO> listProRecDesHas) {
		this.listProRecDesHas = listProRecDesHas;
	}
	public List<ProRecComVO> getListProRecCom() {
		return listProRecCom;
	}
	public void setListProRecCom(List<ProRecComVO> listProRecCom) {
		this.listProRecCom = listProRecCom;
	}
}
