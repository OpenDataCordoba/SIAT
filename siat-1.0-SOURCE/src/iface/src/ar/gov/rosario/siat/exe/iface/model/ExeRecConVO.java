//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del ExeRecCon
 * @author tecso
 *
 */
public class ExeRecConVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "exeRecConVO";
	
	private RecConVO recCon = new RecConVO();
	private ExencionVO exencion = new ExencionVO();
	private Double porcentaje;
	private Double montoFijo;
	private Date fechaDesde;
	private Date fechaHasta;
	
	private String porcentajeView = "";
	private String montoFijoView = "";
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public ExeRecConVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ExeRecConVO(int id, String desc) {
		super();
		setId(new Long(id));
		recCon.setDesRecCon(desc);
	}

	
	// Getters y Setters
	public ExencionVO getExencion() {
		return exencion;
	}

	public void setExencion(ExencionVO exencion) {
		this.exencion = exencion;
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

	public Double getMontoFijo() {
		return montoFijo;
	}

	public void setMontoFijo(Double montoFijo) {
		this.montoFijo = montoFijo;
		this.montoFijoView = StringUtil.formatDouble(montoFijo);
	}

	public Double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
		this.porcentajeView = StringUtil.formatDouble(porcentaje);
	}

	public RecConVO getRecCon() {
		return recCon;
	}

	public void setRecCon(RecConVO recCon) {
		this.recCon = recCon;
	}

	
	
	

	// Buss flags getters y setters
	
	
	// View flags getters
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

	public String getMontoFijoView() {
		return montoFijoView;
	}

	public void setMontoFijoView(String montoFijoView) {
		this.montoFijoView = montoFijoView;
	}

	public String getPorcentajeView() {
		return porcentajeView;
	}

	public void setPorcentajeView(String porcentajeView) {
		this.porcentajeView = porcentajeView;
	}
	
	// View getters
}
