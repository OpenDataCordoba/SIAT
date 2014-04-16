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
 * Value Object del NotaConc
 * @author tecso
 *
 */
public class NotaConcVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "notaConcVO";

	private ConciliacionVO conciliacionVO = new ConciliacionVO();
	private Date fechaConciliacion;
	private String nroCuenta = "";
	private Long impuesto;
	private Integer moneda;
	private Integer tipoNota;
	private Double importe;
	private Date fechaAcredit;
		
	private String tipoNotaView = "";
	private String impuestoView = "";
	private String monedaView = "";
	private String importeView = "";
	private String fechaConciliacionView = "";
	private String fechaAcreditView = "";

	// Buss Flags


	// Constructores
	public NotaConcVO() {
		super();
	}

	// Getters y Setters
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
	public Long getImpuesto() {
		return impuesto;
	}
	public void setImpuesto(Long impuesto) {
		this.impuesto = impuesto;
		this.impuestoView = StringUtil.formatLong(impuesto);
	}
	public String getImpuestoView() {
		return impuestoView;
	}
	public void setImpuestoView(String impuestoView) {
		this.impuestoView = impuestoView;
	}
	public Integer getMoneda() {
		return moneda;
	}
	public void setMoneda(Integer moneda) {
		this.moneda = moneda;
		this.monedaView = StringUtil.formatInteger(moneda);
	}
	public String getMonedaView() {
		return monedaView;
	}
	public void setMonedaView(String monedaView) {
		this.monedaView = monedaView;
	}
	public String getNroCuenta() {
		return nroCuenta;
	}
	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}
	public Integer getTipoNota() {
		return tipoNota;
	}
	public void setTipoNota(Integer tipoNota) {
		this.tipoNota = tipoNota;
		this.tipoNotaView = StringUtil.formatInteger(tipoNota);
	}
	public String getTipoNotaView() {
		return tipoNotaView;
	}
	public void setTipoNotaView(String tipoNotaView) {
		this.tipoNotaView = tipoNotaView;
	}
	public ConciliacionVO getConciliacionVO() {
		return conciliacionVO;
	}
	public void setConciliacionVO(ConciliacionVO conciliacionVO) {
		this.conciliacionVO = conciliacionVO;
	}
	public Date getFechaAcredit() {
		return fechaAcredit;
	}
	public void setFechaAcredit(Date fechaAcredit) {
		this.fechaAcredit = fechaAcredit;
		this.fechaAcreditView = DateUtil.formatDate(fechaAcredit, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaAcreditView() {
		return fechaAcreditView;
	}
	public void setFechaAcreditView(String fechaAcreditView) {
		this.fechaAcreditView = fechaAcreditView;
	}
	public Date getFechaConciliacion() {
		return fechaConciliacion;
	}
	public void setFechaConciliacion(Date fechaConciliacion) {
		this.fechaConciliacion = fechaConciliacion;
		this.fechaConciliacionView = DateUtil.formatDate(fechaConciliacion, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaConciliacionView() {
		return fechaConciliacionView;
	}
	public void setFechaConciliacionView(String fechaConciliacionView) {
		this.fechaConciliacionView = fechaConciliacionView;
	}
	
}
