//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.BussImageModel;

public class ConvenioCuotaVO extends BussImageModel {

	private static final long serialVersionUID = 1L;

	private Long idPlan;
	
	private String desPlan;
	
	private Long idConvenio;
	
	private Integer nroConvenio;
	
	private String desConvenio;
	
	private String perFor;
	
	private String desViaDeuda;
		
	private Long idProcurador;
	
	private String desProcurador;
	
	private Integer numeroCuota;
	
	private Integer mes;
	
	private Integer anio;

	private Double capitalCuota;
	
	private Double interes;
	
	private Double importeCuota;
	
	private Double actualizacion;
	
	private Date fechaPago;
	
	private Double importeSellado;
	
	private Long codRefPag;

	private String codRefPagView = "";
	
	public Integer getNumeroCuota() {
		return numeroCuota;
	}

	public void setNumeroCuota(Integer numeroCuota) {
		this.numeroCuota = numeroCuota;
	}

	public Double getCapitalCuota() {
		return capitalCuota;
	}

	public void setCapitalCuota(Double capitalCuota) {
		this.capitalCuota = capitalCuota;
	}

	public Double getInteres() {
		return interes;
	}

	public void setInteres(Double interes) {
		this.interes = interes;
	}

	public Double getImporteCuota() {
		return importeCuota;
	}

	public void setImporteCuota(Double importeCuota) {
		this.importeCuota = importeCuota;
	}

	public Double getActualizacion() {
		return actualizacion;
	}

	public void setActualizacion(Double actualizacion) {
		this.actualizacion = actualizacion;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Long getIdPlan() {
		return idPlan;
	}

	public void setIdPlan(Long idPlan) {
		this.idPlan = idPlan;
	}

	public String getDesPlan() {
		return desPlan;
	}

	public void setDesPlan(String desPlan) {
		this.desPlan = desPlan;
	}

	public Long getIdConvenio() {
		return idConvenio;
	}

	public void setIdConvenio(Long idConvenio) {
		this.idConvenio = idConvenio;
	}

	public String getDesConvenio() {
		return desConvenio;
	}

	public void setDesConvenio(String desConvenio) {
		this.desConvenio = desConvenio;
	}

	public String getDesViaDeuda() {
		return desViaDeuda;
	}

	public void setDesViaDeuda(String desViaDeuda) {
		this.desViaDeuda = desViaDeuda;
	}

	public void setIdProcurador(Long idProcurador) {
		this.idProcurador = idProcurador;
	}

	public Long getIdProcurador() {
		return idProcurador;
	}

	public void setDesProcurador(String desProcurador) {
		this.desProcurador = desProcurador;
	}

	public String getDesProcurador() {
		return desProcurador;
	}

	public Integer getNroConvenio() {
		return nroConvenio;
	}

	public void setNroConvenio(Integer nroConvenio) {
		this.nroConvenio = nroConvenio;		
	}

	public String getPerFor() {
		return perFor;
	}

	public void setPerFor(String perFor) {
		this.perFor = perFor;
	}
	

	public Double getImporteSellado() {
		return importeSellado;
	}

	public void setImporteSellado(Double importeSellado) {
		this.importeSellado = importeSellado;
	}

	// View Getters
	public String getCapitalCuotaView() {
		return StringUtil.parsePointToComa(StringUtil.redondearDecimales(capitalCuota, 1, 2));
	}

	public String getImporteCuotaView() {
		return StringUtil.parsePointToComa(StringUtil.redondearDecimales(importeCuota, 1, 2));
	}

	public String getActualizacionView() {
		return StringUtil.parsePointToComa(StringUtil.redondearDecimales(actualizacion, 1, 2));		
	}

	public String getInteresView() {
		return StringUtil.parsePointToComa(StringUtil.redondearDecimales(interes, 1, 2));		
	}

	public String getFechaPagoView() {
		return DateUtil.formatDate(fechaPago, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public String getImporteSelladoView(){
		return StringUtil.parsePointToComa(StringUtil.redondearDecimales(importeSellado, 1, 2));
	}

	public Long getCodRefPag() {
		return codRefPag;
	}

	public void setCodRefPag(Long codRefPag) {
		this.codRefPag = codRefPag;
		this.codRefPagView = StringUtil.formatLong(codRefPag);
	}

	public String getCodRefPagView() {
		return codRefPagView;
	}

	public void setCodRefPagView(String codRefPagView) {
		this.codRefPagView = codRefPagView;
	}
	
	
}
