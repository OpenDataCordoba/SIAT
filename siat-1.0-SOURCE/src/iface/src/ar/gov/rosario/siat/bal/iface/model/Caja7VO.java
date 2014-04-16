//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Value Object del Caja7
 * @author tecso
 *
 */
public class Caja7VO extends SiatBussImageModel {

	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "caja7VO";

	private BalanceVO balance = new BalanceVO();
	private Date fecha;
	private PartidaVO partida = new PartidaVO();
	private String descripcion="";
	private String observacion="";
	private Double importeEjeAct;
	private Double importeEjeVen;
	private Double importe ;

	private String importeView="";
	private String importeEjeActView="";
	private String importeEjeVenView="";
	private String fechaView="";

	private Integer actualOVencido = 0;
	
	// Buss Flags

	// View Constants

	// Constructores
	public Caja7VO() {
		super();
	}

	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public Caja7VO(int id, String desc) {
		super();
		setId(new Long(id));

	}

	// Getters y Setters
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
		this.fechaView = DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK);
	}
	public PartidaVO getPartida() {
		return partida;
	}
	public void setPartida(PartidaVO partida) {
		this.partida = partida;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getFechaView() {
		return fechaView;
	}
	public void setFechaView(String fechaView) {
		this.fechaView = fechaView;
	}
	public Double getImporteEjeAct() {
		return importeEjeAct;
	}
	public void setImporteEjeAct(Double importeEjeAct) {
		this.importeEjeAct = importeEjeAct;
		this.importeEjeActView = StringUtil.formatDouble(importeEjeAct);
	}
	public String getImporteEjeActView() {
		return importeEjeActView;
	}
	public void setImporteEjeActView(String importeEjeActView) {
		this.importeEjeActView = importeEjeActView;
	}
	public String getImporteEjeVenView() {
		return importeEjeVenView;
	}
	public void setImporteEjeVenView(String importeEjeVenView) {
		this.importeEjeVenView = importeEjeVenView;
	}
	public Double getImporteEjeVen() {
		return importeEjeVen;

	}
	public void setImporteEjeVen(Double importeEjeVen) {
		this.importeEjeVen = importeEjeVen;
		this.importeEjeVenView = StringUtil.formatDouble(importeEjeVen);
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe);
	}
	public String getImporteView() {
		return importeView;
	}
	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}
	public BalanceVO getBalance() {
		return balance;
	}
	public void setBalance(BalanceVO balance) {
		this.balance = balance;
	}
	public Integer getActualOVencido() {
		return actualOVencido;
	}
	public void setActualOVencido(Integer actualOVencido) {
		this.actualOVencido = actualOVencido;
	}
	

}
