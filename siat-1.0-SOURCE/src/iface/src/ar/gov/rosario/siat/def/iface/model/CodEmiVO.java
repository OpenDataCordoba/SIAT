//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del CodEmi
 * @author tecso
 *
 */
public class CodEmiVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "codEmiVO";
	
	private RecursoVO recurso = new RecursoVO();
	
	private TipCodEmiVO tipCodEmi = new TipCodEmiVO();
	
	private String nombre = "";
	
	private String descripcion = "";
	
	private String codigo = "";
	
	private Date fechaDesde;
	
	private Date fechaHasta;
	
	private String fechaDesdeView = "";
	
	private String fechaHastaView = "";

	private List<HistCodEmiVO> listHistCodEmi = new ArrayList<HistCodEmiVO>(); 
	
	// Atributos a valirizar al momento de la emision
	private RecursoDefinition recAtrCueEmiDefinition = new RecursoDefinition();

	// View Constants
	
	// Constructores
	public CodEmiVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public CodEmiVO(int id, String codigo) {
		super();
		setId(new Long(id));
		setCodigo(codigo);
	}

	// Getters y Setters
	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public TipCodEmiVO getTipCodEmi() {
		return tipCodEmi;
	}

	public void setTipCodEmi(TipCodEmiVO tipCodEmi) {
		this.tipCodEmi = tipCodEmi;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

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

	public List<HistCodEmiVO> getListHistCodEmi() {
		return listHistCodEmi;
	}

	public void setListHistCodEmi(List<HistCodEmiVO> listHistCodEmi) {
		this.listHistCodEmi = listHistCodEmi;
	}

	public RecursoDefinition getRecAtrCueEmiDefinition() {
		return recAtrCueEmiDefinition;
	}

	public void setRecAtrCueEmiDefinition(RecursoDefinition recAtrCueEmiDefinition) {
		this.recAtrCueEmiDefinition = recAtrCueEmiDefinition;
	}
}
