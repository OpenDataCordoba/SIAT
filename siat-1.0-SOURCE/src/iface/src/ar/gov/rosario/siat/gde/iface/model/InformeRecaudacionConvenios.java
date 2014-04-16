//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class InformeRecaudacionConvenios extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private Date fechaPagoDesde;
	private Date fechaPagoHasta;
	private String desRecurso;
	private String desPlan;
	private String desViaDeuda;
	private String desProcurador;
	private String usr;
	
	private Integer cantCuotas;//para el resumido
	
	private Double totalCapital;
	private Double totalInteres;
	private Double totalActualizacion;	
	private Double importeTotal;
	
	
	private List<PlanRecPeriodoVO> listPlanRecPeriodo = new ArrayList<PlanRecPeriodoVO>();
	private List<ConvenioCuotaVO> listConvenioCuota = new ArrayList<ConvenioCuotaVO>();

	public List<ConvenioCuotaVO> getListConvenioCuota() {
		return listConvenioCuota;
	}

	public void setListConvenioCuota(List<ConvenioCuotaVO> listConvenioCuota) {
		this.listConvenioCuota = listConvenioCuota;
	}
	
	public Integer getCantCuotasTotal(){
		return listConvenioCuota.size();
	}
	
	public void calcualrTotalesReporteDetallado(){
		this.totalCapital = 0D;
		this.totalActualizacion = 0D;
		this.totalInteres = 0D;
		this.importeTotal = 0D;
		for(ConvenioCuotaVO convenioCuotaVO: listConvenioCuota){
			totalCapital+=convenioCuotaVO.getCapitalCuota();
			totalActualizacion+=convenioCuotaVO.getActualizacion();
			totalInteres+=convenioCuotaVO.getInteres();
			importeTotal+=convenioCuotaVO.getImporteCuota();
		}
		
	}

	public void calcualrTotalesReporteResumido(){
		this.totalCapital = 0D;
		this.totalActualizacion = 0D;
		this.totalInteres = 0D;
		this.importeTotal = 0D;
		this.cantCuotas = 0;
		for(PlanRecPeriodoVO p: listPlanRecPeriodo){
			totalCapital+=p.getTotCapital();
			totalActualizacion+=p.getTotActualiz();
			totalInteres+=p.getTotInteres();
			importeTotal+=p.getTotImporte();
			cantCuotas+=p.getCantCuotas();
		}		
	}
	
	public Double getTotalCapital() {
		return totalCapital;
	}

	public void setTotalCapital(Double totalCapital) {
		this.totalCapital = totalCapital;
	}

	public Double getTotalInteres() {
		return totalInteres;
	}

	public void setTotalInteres(Double totalInteres) {
		this.totalInteres = totalInteres;
	}

	public Double getTotalActualizacion() {
		return totalActualizacion;
	}

	public void setTotalActualizacion(Double totalActualizacion) {
		this.totalActualizacion = totalActualizacion;
	}

	public String getUsr() {
		return usr;
	}

	public void setUsr(String usr) {
		this.usr = usr;
	}
	
	
	public Date getFechaPagoDesde() {
		return fechaPagoDesde;
	}

	public void setFechaPagoDesde(Date fechaPagoDesde) {
		this.fechaPagoDesde = fechaPagoDesde;
	}

	public Date getFechaPagoHasta() {
		return fechaPagoHasta;
	}

	public void setFechaPagoHasta(Date fechaPagoHasta) {
		this.fechaPagoHasta = fechaPagoHasta;
	}

	public String getDesRecurso() {
		return desRecurso;
	}

	public void setDesRecurso(String desRecurso) {
		this.desRecurso = desRecurso;
	}

	public String getDesPlan() {
		return desPlan;
	}

	public void setDesPlan(String desPlan) {
		this.desPlan = desPlan;
	}

	public String getDesViaDeuda() {
		return desViaDeuda;
	}

	public void setDesViaDeuda(String desViaDeuda) {
		this.desViaDeuda = desViaDeuda;
	}

	public List<PlanRecPeriodoVO> getListPlanRecPeriodo() {
		return listPlanRecPeriodo;
	}

	public void setListPlanRecPeriodo(List<PlanRecPeriodoVO> listPlanRecPeriodo) {
		this.listPlanRecPeriodo = listPlanRecPeriodo;
	}

	public Integer getCantCuotas() {
		return cantCuotas;
	}

	public void setCantCuotas(Integer cantCuotas) {
		this.cantCuotas = cantCuotas;
	}

	// View getters
	public String getTotalCapitalView(){
		return StringUtil.parsePointToComa(StringUtil.redondearDecimales(totalCapital, 1, 2));
	}
	
	public String getTotalInteresView(){
		return StringUtil.parsePointToComa(StringUtil.redondearDecimales(totalInteres, 1, 2));
	}
	
	public String getTotalActualizacionView(){
		return StringUtil.parsePointToComa(StringUtil.redondearDecimales(totalActualizacion, 1, 2));
	}
	
	public String getFechaPagoDesdeView(){
		return DateUtil.formatDate(fechaPagoDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public String getFechaPagoHastaView(){
		return DateUtil.formatDate(fechaPagoHasta, DateUtil.ddSMMSYYYY_MASK);
	}	
	
	public String getImporteTotalView(){
		return StringUtil.parsePointToComa(StringUtil.redondearDecimales(importeTotal, 1, 2));
	}

	public void setDesProcurador(String desProcurador) {
		this.desProcurador = desProcurador;
	}

	public String getDesProcurador() {
		return desProcurador;
	}
}
