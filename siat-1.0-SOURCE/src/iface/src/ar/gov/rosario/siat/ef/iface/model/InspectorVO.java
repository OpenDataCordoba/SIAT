//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del Inspector
 * @author tecso
 *
 */
public class InspectorVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "inspectorVO";

	private String desInspector;

	private Long idPersona;

	private String desPersona="";

	private Date fechaDesde;

	private Date fechaHasta;

	private SupervisorVO supervisorVigente;

	private String cantOrdenesAbiertas="";

	// view properties
	private String fechaDesdeView ="";
	private String fechaHastaView ="";

	private List<InsSupVO> listInsSup = new ArrayList<InsSupVO>();

	private String detalleOrden = "";
	// Buss Flags


	// View Constants


	// Constructores
	public InspectorVO() {
		super();
	}

	// Constructor para utiliDesdezar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public InspectorVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesInspector(desc);
	}

	// Getters y Setters

	public String getDesInspector() {
		return desInspector;
	}

	public void setDesInspector(String desInspector) {
		this.desInspector = desInspector;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
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

	public String getDesPersona() {
		return desPersona;
	}

	public void setDesPersona(String desPersona) {
		this.desPersona = desPersona;
	}

	public List<InsSupVO> getListInsSup() {
		return listInsSup;
	}

	public void setListInsSup(List<InsSupVO> listInsSup) {
		this.listInsSup = listInsSup;
	}
	public SupervisorVO getSupervisorVigente() {
		return supervisorVigente;
	}

	public void setSupervisorVigente(SupervisorVO supervisorVigente) {
		this.supervisorVigente = supervisorVigente;
	}

	public String getCantOrdenesAbiertas() {
		return cantOrdenesAbiertas;
	}

	public void setCantOrdenesAbiertas(String cantOrdenesAbiertas) {
		this.cantOrdenesAbiertas = cantOrdenesAbiertas;
	}

	public String getDetalleOrden() {
		return detalleOrden;
	}

	public void setDetalleOrden(String detalleOrden) {
		this.detalleOrden = detalleOrden;
	}


	/**
	 * Valida la vigencia del Inspector con la fechaDesde, fechaHasta y la fecha del dia
	 */
	public boolean getEsVigente() {

		if(DateUtil.isDateBefore(this.fechaDesde, new Date()) &&
				(this.fechaHasta==null || DateUtil.isDateAfter(this.fechaHasta, new Date())))
			return true;

		return false;
	}

	// Buss flags getters y setters


	// View flags getters



	// View getters
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
