//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;

/**
 * Value Object de Cobranza
 * @author tecso
 *
 */
public class CobranzaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "cobranzaVO";
	
	private ContribuyenteVO contribuyente = new ContribuyenteVO();
	
	private Date fechaInicio;
	
	private OrdenControlVO ordenControl = new OrdenControlVO();
	
	private CasoVO caso = new CasoVO();
	
	private Date fechaFin;
	
	private AreaVO area = new AreaVO();
	
	private Double impIniPag;
	
	private Double importeACobrar;
	
	private Double impPagGes;
	
	private EstadoCobranzaVO estadoCobranza= new EstadoCobranzaVO();
	
	private PerCobVO perCob = new PerCobVO();
	
	private Date proFecCon;
	
	private String observacion="";
	
	private List<CobranzaDetVO>listCobranzaDet=new ArrayList<CobranzaDetVO>();
	
	private List<GesCobVO>listGesCob=new ArrayList<GesCobVO>();
	
	private Double subtotalDeclarado;
	
	private Double subtotalPagos;
	
	private Double subtotalConVig;
	
	private Double subtotalConCad;
	
	private Double subtotalDiferencia;
	
	private boolean esVerificacion=false;
	
	private Date fechaResolucion;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public CobranzaVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.


	// Getters y Setters
	

	public ContribuyenteVO getContribuyente() {
		return contribuyente;
	}

	public void setContribuyente(ContribuyenteVO contribuyente) {
		this.contribuyente = contribuyente;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControl) {
		this.ordenControl = ordenControl;
	}

	public CasoVO getCaso() {
		return caso;
	}

	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public AreaVO getArea() {
		return area;
	}

	public void setArea(AreaVO area) {
		this.area = area;
	}

	public Double getImpIniPag() {
		return impIniPag;
	}

	public void setImpIniPag(Double impIniPag) {
		this.impIniPag = impIniPag;
	}

	public Double getImporteACobrar() {
		return importeACobrar;
	}

	public void setImporteACobrar(Double importeACobrar) {
		this.importeACobrar = importeACobrar;
	}

	public Double getImpPagGes() {
		return impPagGes;
	}

	public void setImpPagGes(Double impPagGes) {
		this.impPagGes = impPagGes;
	}

	public EstadoCobranzaVO getEstadoCobranza() {
		return estadoCobranza;
	}

	public void setEstadoCobranza(EstadoCobranzaVO estadoCobranza) {
		this.estadoCobranza = estadoCobranza;
	}

	public PerCobVO getPerCob() {
		return perCob;
	}

	public void setPerCob(PerCobVO perCob) {
		this.perCob = perCob;
	}

	public Date getProFecCon() {
		return proFecCon;
	}

	public void setProFecCon(Date proFecCon) {
		this.proFecCon = proFecCon;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public List<CobranzaDetVO> getListCobranzaDet() {
		return listCobranzaDet;
	}

	public void setListCobranzaDet(List<CobranzaDetVO> listCobranzaDet) {
		this.listCobranzaDet = listCobranzaDet;
	}

	public Double getSubtotalDeclarado() {
		return subtotalDeclarado;
	}

	public void setSubtotalDeclarado(Double subtotalDeclarado) {
		this.subtotalDeclarado = subtotalDeclarado;
	}
	
	public boolean isEsVerificacion() {
		return esVerificacion;
	}

	public void setEsVerificacion(boolean esVerificacion) {
		this.esVerificacion = esVerificacion;
	}
	
	public List<GesCobVO> getListGesCob() {
		return listGesCob;
	}

	public void setListGesCob(List<GesCobVO> listGesCob) {
		this.listGesCob = listGesCob;
	}
	
	public Double getSubtotalPagos() {
		return subtotalPagos;
	}

	public void setSubtotalPagos(Double subtotalPagos) {
		this.subtotalPagos = subtotalPagos;
	}

	public Double getSubtotalConVig() {
		return subtotalConVig;
	}

	public void setSubtotalConVig(Double subtotalConVig) {
		this.subtotalConVig = subtotalConVig;
	}
	

	public Double getSubtotalConCad() {
		return subtotalConCad;
	}

	public void setSubtotalConCad(Double subtotalConCad) {
		this.subtotalConCad = subtotalConCad;
	}
	
	public Double getSubtotalDiferencia() {
		return subtotalDiferencia;
	}

	public void setSubtotalDiferencia(Double subtotalDiferencia) {
		this.subtotalDiferencia = subtotalDiferencia;
	}
	
	public Date getFechaResolucion() {
		return fechaResolucion;
	}

	public void setFechaResolucion(Date fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	

	// View getters
	public String getFechaInicioView(){
		return (this.fechaInicio!=null)?DateUtil.formatDate(fechaInicio, DateUtil.ddSMMSYYYY_MASK):"";
	}
	
	public String getFechaFinView(){
		return (this.fechaFin!=null)?DateUtil.formatDate(fechaFin, DateUtil.ddSMMSYYYY_MASK):"";
	}
	
	public String getProFecConView(){
		return (this.proFecCon!=null)?DateUtil.formatDate(proFecCon, DateUtil.ddSMMSYYYY_MASK):"";
	}
	
	public String getFechaResolucionView(){
		return (this.fechaResolucion!=null)?DateUtil.formatDate(fechaResolucion, DateUtil.ddSMMSYYYY_MASK):"";
	}
	
	public String getImpIniPagView(){
		return (this.impIniPag!=null)?NumberUtil.round(impIniPag, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getImpPagGesView(){
		return (this.impPagGes!=null)?NumberUtil.round(impPagGes, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getImporteACobrarView(){
		return (this.importeACobrar!=null)?NumberUtil.round(importeACobrar, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getSubtotalDeclaradoView(){
		return (this.subtotalDeclarado!=null)?NumberUtil.round(subtotalDeclarado, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getSubtotalPagosView(){
		return (this.subtotalPagos!=null)?NumberUtil.round(subtotalPagos, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getSubtotalConVigView(){
		return (this.subtotalConVig!=null)?NumberUtil.round(subtotalConVig, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getSubtotalConCadView(){
		return (this.subtotalConCad!=null)?NumberUtil.round(subtotalConCad, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getSubtotalDiferenciaView(){
		return (this.subtotalDiferencia!=null)?NumberUtil.round(subtotalDiferencia, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
}
