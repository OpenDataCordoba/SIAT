//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del AccionSellado
 * @author tecso
 *
 */
public class AccionSelladoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "accionSelladoVO";
	
	private SelladoVO sellado = new SelladoVO();
	private AccionVO accion	  = new AccionVO();
	private RecursoVO recurso = new RecursoVO();
	
	private SiNo esEspecial = SiNo.OpcionSelecionar;
	private Integer cantPeriodos;
	
	private String classForName;
	
	private Date fechaDesde;
	private Date fechaHasta;
	
	//	 View Constants
	private String cantPeriodosView="";
	private String fechaDesdeView="";
	private String fechaHastaView="";
	
	
	
	// Constructores
	public AccionSelladoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public AccionSelladoVO(int id, String desc) {
		super();
		setId(new Long(id));
	}

	// Getters y Setters
	public AccionVO getAccion() {
		return accion;
	}

	public void setAccion(AccionVO accion) {
		this.accion = accion;
	}

	public Integer getCantPeriodos() {
		return cantPeriodos;
	}

	public void setCantPeriodos(Integer cantPeriodos) {
		this.cantPeriodos = cantPeriodos;
		this.cantPeriodosView = StringUtil.formatInteger(cantPeriodos);
	}
	
	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, "dd/MM/yyyy");
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, "dd/MM/yyyy");
	}

	public String getClassForName() {
		return classForName;
	}

	public void setClassForName(String classForName) {
		this.classForName = classForName;
	}

	public SiNo getEsEspecial() {
		return esEspecial;
	}

	public void setEsEspecial(SiNo esEspecial) {
		this.esEspecial = esEspecial;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public SelladoVO getSellado() {
		return sellado;
	}

	public void setSellado(SelladoVO sellado) {
		this.sellado = sellado;
	}

	//	 View getters
	public String getCantPeriodosView() {
		return cantPeriodosView;
	}

	public void setCantPeriodosView(String cantPeriodosView) {
		this.cantPeriodosView = cantPeriodosView;
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
	
}
