//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Value Object del LiqPagoDeuda
 * @author tecso
 *
 */
public class LiqPagoDeudaVO {

	private String desTipoPago = "";
	
	private Date fechaPago = new Date();
	
	private Double importe = 0D;
	private Double actualizacion = 0D;
	
	private String descripcion="";
	private String desBancoPago ="";
	
	private String fechaPagoView="";
	private String importeView ="";	
	private String actualizacionView ="";
	private Long idPagoDeuda=null;
	
	public String getDesTipoPago() {
		return desTipoPago;
	}

	public void setDesTipoPago(String desTipoPago) {
		this.desTipoPago = desTipoPago;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
		this.fechaPagoView = DateUtil.formatDate(fechaPago, DateUtil.ddSMMSYYYY_MASK);
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.redondearDecimales(importe, 1, 2);
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDesBancoPago() {
		return desBancoPago;
	}

	public void setDesBancoPago(String desBancoPago) {
		this.desBancoPago = desBancoPago;
	}

	public Double getActualizacion() {
		return actualizacion;
	}

	public void setActualizacion(Double actualizacion) {
		this.actualizacion = actualizacion;
		this.actualizacionView = StringUtil.redondearDecimales(actualizacion, 1, 2);
	}

	// View Getters
	public String getFechaPagoView() {
		return fechaPagoView;
	}

	public void setFechaPagoView(String fechaPagoView) {
		this.fechaPagoView = fechaPagoView;
	}

	public String getImporteView() {
		return importeView;
	}

	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}


	public String getActualizacionView() {
		return actualizacionView;
	}

	public void setActualizacionView(String actualizacionView) {
		this.actualizacionView = actualizacionView;
	}

	public Long getIdPagoDeuda() {
		return idPagoDeuda;
	}

	public void setIdPagoDeuda(Long idPagoDeuda) {
		this.idPagoDeuda = idPagoDeuda;
	}
	
	public String getIdPagoDeudaView(){
		return (this.idPagoDeuda != null) ? idPagoDeuda.toString():"";
	}

	public String getImporteTotalView(){
		return StringUtil.redondearDecimales(getImporte() + getActualizacion(), 1, 2);
	}
	
}
