//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.ef.iface.model.DetAjuDetVO;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del MultaDet
 * @author tecso
 *
 */
public class MultaDetVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "multaDetVO";
	
	private MultaVO multa = new MultaVO();
	private DetAjuDetVO detAjuDet = new DetAjuDetVO();
	private Integer periodo;
	private Integer anio;
	private Double porOri=0.0;
	private Double porDes=0.0;
	private Double porApl=0.0;
	private Double importeBase=0.0;
	private Double importeAplicado=0.0;
	private Double importeAct=0.0;
	
	private Double pagoContadoOBueno=0.0;
	private Double resto=0.0;
	private Double pagoActualizado=0.0;
	private Double restoActualizado=0.0;
	private Double aplicado=0.0;
	// Buss Flags
	
	
	// View Constants
	private String periodoView;
	private String anioView;
	private String porOriView;
	private String porDesView;
	private String porAplView;
	private String importeBaseView;
	private String importeAplicadoView;
	private String importeActView;
	
	private String pagoContadoOBuenoView="";
	private String restoView="";
	private String pagoActualizadoView="";
	private String restoActualizadoView="";
	private String aplicadoView="";
	
		// Constructores
	public MultaDetVO() {
		super();
	}

	// Getters y Setters

	public MultaVO getMulta() {
		return multa;
	}

	public void setMulta(MultaVO multa) {
		this.multa = multa;
	}

	public DetAjuDetVO getDetAjuDet() {
		return detAjuDet;
	}

	public void setDetAjuDet(DetAjuDetVO detAjuDet) {
		this.detAjuDet = detAjuDet;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
		this.periodoView = StringUtil.formatInteger(periodo);
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
		this.anioView = StringUtil.formatInteger(anio);
	}

	public Double getPorOri() {
		return porOri;
	}

	public void setPorOri(Double porOri) {
		this.porOri = porOri;
		this.porOriView = StringUtil.formatDouble(porOri);
	}

	public Double getPorDes() {
		return porDes;
	}

	public void setPorDes(Double porDes) {
		this.porDes = porDes;
		this.porDesView = StringUtil.formatDouble(porDes);
	}

	public Double getPorApl() {
		return porApl;
	}

	public void setPorApl(Double porApl) {
		this.porApl = porApl;
		this.porAplView = StringUtil.formatDouble(porApl);
	}

	public Double getImporteBase() {
		return importeBase;
	}

	public void setImporteBase(Double importeBase) {
		this.importeBase = importeBase;
		this.importeBaseView = StringUtil.formatDouble(importeBase);
	}

	public Double getImporteAplicado() {
		return importeAplicado;
	}

	public void setImporteAplicado(Double importeAplicado) {
		this.importeAplicado = importeAplicado;
		this.importeAplicadoView = NumberUtil.round(importeAplicado,SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	public String getPeriodoView() {
		return periodoView;
	}

	public void setPeriodoView(String periodoView) {
		this.periodoView = periodoView;
	}

	public String getAnioView() {
		return anioView;
	}

	public void setAnioView(String anioView) {
		this.anioView = anioView;
	}

	public String getPorOriView() {
		return (this.porOri!=null)?NumberUtil.round(porOri * 100, SiatParam.DEC_IMPORTE_VIEW).toString()+"%":"0.00 %";
	}

	public void setPorOriView(String porOriView) {
		this.porOriView = porOriView;
	}

	public String getPorDesView() {
		return (this.porDes!=null)?NumberUtil.round(porDes * 100, SiatParam.DEC_IMPORTE_VIEW).toString()+"%":"0.00 %";
	}

	public void setPorDesView(String porDesView) {
		this.porDesView = porDesView;
	}

	public String getPorAplView() {
		return (this.porApl!=null)?NumberUtil.round(porApl * 100, SiatParam.DEC_IMPORTE_VIEW).toString()+"%":"0.00 %";
	}

	public void setPorAplView(String porAplView) {
		this.porAplView = porAplView;
	}

	public String getImporteBaseView() {
		return (importeBase!=null)?NumberUtil.round(importeBase, SiatParam.DEC_IMPORTE_VIEW).toString():"0.00";
	}

	public void setImporteBaseView(String importeBaseView) {
		this.importeBaseView = importeBaseView;
	}

	public String getImporteAplicadoView() {
		return NumberUtil.round(importeAplicado, SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	public void setImporteAplicadoView(String importeAplicadoView) {
		this.importeAplicadoView = importeAplicadoView;
	}

	public Double getImporteAct() {
		return importeAct;
	}

	public void setImporteAct(Double importeAct) {
		this.importeAct = importeAct;
	}

	public Double getPagoContadoOBueno() {
		return pagoContadoOBueno;
	}

	public void setPagoContadoOBueno(Double pagoContadoOBueno) {
		this.pagoContadoOBueno = pagoContadoOBueno;
		this.pagoContadoOBuenoView = NumberUtil.round(pagoContadoOBueno,SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	public String getPagoContadoOBuenoView() {
		return pagoContadoOBuenoView;
	}

	public void setPagoContadoOBuenoView(String pagoContadoOBuenoView) {
		this.pagoContadoOBuenoView = pagoContadoOBuenoView;
	}

	public String getImporteActView() {
		return (importeAct!=null)?NumberUtil.round(importeAct, SiatParam.DEC_IMPORTE_VIEW).toString():"0.00";
	}

	public void setImporteActView(String importeActView) {
		this.importeActView = importeActView;
	}
	
	public Double getResto() {
		return resto;
	}

	public void setResto(Double resto) {
		this.resto = resto;
		this.restoView = NumberUtil.round(resto,SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	public Double getPagoActualizado() {
		return pagoActualizado;
	}

	public void setPagoActualizado(Double pagoActualizado) {
		this.pagoActualizado = pagoActualizado;
		this.pagoActualizadoView = NumberUtil.round(pagoActualizado,SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	public Double getRestoActualizado() {
		return restoActualizado;
	}

	public void setRestoActualizado(Double restoActualizado) {
		this.restoActualizado = restoActualizado;
		this.restoActualizadoView = NumberUtil.round(restoActualizado,SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	public Double getAplicado() {
		return aplicado;
	}

	public void setAplicado(Double aplicado) {
		this.aplicado = aplicado;
		this.aplicadoView = NumberUtil.round(aplicado,SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	public String getRestoView() {
		return restoView;
	}

	public void setRestoView(String restoView) {
		this.restoView = restoView;
	}

	public String getPagoActualizadoView() {
		return pagoActualizadoView;
	}

	public void setPagoActualizadoView(String pagoActualizadoView) {
		this.pagoActualizadoView = pagoActualizadoView;
	}

	public String getRestoActualizadoView() {
		return restoActualizadoView;
	}

	public void setRestoActualizadoView(String restoActualizadoView) {
		this.restoActualizadoView = restoActualizadoView;
	}

	public String getAplicadoView() {
		return aplicadoView;
	}

	public void setAplicadoView(String aplicadoView) {
		this.aplicadoView = aplicadoView;
	}

	public String getTotalOriginalView(){
		Double importeActualizado=(importeAct!=null)?importeAct:0D;
		Double totalOriginal=(porOri!=null)?porOri*importeActualizado:null;
		return (totalOriginal!=null)?NumberUtil.round(totalOriginal, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
}
