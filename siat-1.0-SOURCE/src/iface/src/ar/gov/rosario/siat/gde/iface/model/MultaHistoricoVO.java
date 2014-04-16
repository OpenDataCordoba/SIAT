//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;

/**
 * Value Object del MultaHistorico
 * @author tecso
 *
 */
public class MultaHistoricoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "multaHistoricoVO";
	
	private MultaVO multa = new MultaVO();
	private Integer periodoDesde;
	private Integer anioDesde;
	private Integer periodoHasta;
	private Integer anioHasta;
	private Date fecha;
	private Double porcentaje;
	private Double importeTotal;
	private CasoVO caso = new CasoVO();
	private String observacion="";
	
	// View Constants
	private String periodoDesdeView="";
	private String anioDesdeView="";
	private String periodoHastaView="";
	private String anioHastaView = "";
	private String fechaView = "";
	private String porcentajeView="";
	private String idCaso="";

	// Constructores
	public MultaHistoricoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public MultaHistoricoVO(int id, String desc) {
		super();
		setId(new Long(id));
	}
	
	// Getters y Setters

	public MultaVO getMulta() {
		return multa;
	}

	public void setMulta(MultaVO multa) {
		this.multa = multa;
	}

	public Integer getPeriodoDesde() {
		return periodoDesde;
	}

	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
	}

	public Integer getAnioDesde() {
		return anioDesde;
	}

	public void setAnioDesde(Integer anioDesde) {
		this.anioDesde = anioDesde;
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
	}

	public Integer getAnioHasta() {
		return anioHasta;
	}

	public void setAnioHasta(Integer anioHasta) {
		this.anioHasta = anioHasta;
	}

	public Double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public Double getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(Double importeTotal) {
		this.importeTotal = importeTotal;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public CasoVO getCaso() {
		return caso;
	}

	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getPeriodoDesdeView() {
		return periodoDesdeView;
	}

	public void setPeriodoDesdeView(String periodoDesdeView) {
		this.periodoDesdeView = periodoDesdeView;
	}

	public String getAnioDesdeView() {
		return anioDesdeView;
	}

	public void setAnioDesdeView(String anioDesdeView) {
		this.anioDesdeView = anioDesdeView;
	}

	public String getPeriodoHastaView() {
		return periodoHastaView;
	}

	public void setPeriodoHastaView(String periodoHastaView) {
		this.periodoHastaView = periodoHastaView;
	}

	public String getAnioHastaView() {
		return anioHastaView;
	}

	public void setAnioHastaView(String anioHastaView) {
		this.anioHastaView = anioHastaView;
	}

	public String getFechaView() {
		return DateUtil.formatDate(this.fecha, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setFechaView(String fechaView) {
		this.fechaView = fechaView;
	}

	public String getPorcentajeView() {
		return (this.porcentaje != null)?NumberUtil.round(porcentaje * 100, SiatParam.DEC_IMPORTE_VIEW).toString()+"%":"";
	}

	public void setPorcentajeView(String porcentajeView) {
		this.porcentajeView = porcentajeView;
	}
	
	public String getImporteTotalView() {
		return (this.importeTotal != null)?NumberUtil.round(importeTotal, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}

	public String getPeriodoAnioDesdeView (){
		return (this.periodoDesde!=null)?this.periodoDesde+"/"+this.anioDesde:"";
	}
	
	public String getPeriodoAnioHastaView (){
		return (this.periodoHasta!=null)?this.periodoHasta+"/"+this.anioHasta:"";
	}

	public String getIdCaso() {
		return idCaso;
	}

	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}
	

}
