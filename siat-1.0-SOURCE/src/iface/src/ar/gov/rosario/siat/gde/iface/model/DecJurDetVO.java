//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.iface.model.RecAliVO;
import ar.gov.rosario.siat.def.iface.model.RecConADecVO;
import ar.gov.rosario.siat.def.iface.model.RecTipUniVO;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.model.AlcanceEturAfip;

/**
 * Value Object del DecJurDet
 * @author tecso
 *
 */
public class DecJurDetVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "decJurDetVO";
	
	private DecJurVO decJur=new DecJurVO();
	
	private String detalle="";
	
	private RecConADecVO recConADec=new RecConADecVO();
	
	private Double base;
	
	private Double multiplo;
	
	private Double subtotal1;
	
	private Double canUni;
	
	private String unidad = "";
	
	private RecTipUniVO recTipUni = new RecTipUniVO();
	
	private RecConADecVO tipoUnidad = new RecConADecVO();
	
	private String desTipoUnidad="";
	
	private Double valRef;
	
	private Double valUnidad;
	
	private Double subtotal2;
	
	private Double minimo;
	
	private Double totalConcepto;
	
	private String totalConceptoView="";
	
	private String baseView="";
	
	private String subtotal1View="";
	
	private String valUnidadView="";
	
	private String subtotal2View="";
	
	private String baseAplicadaView="";
	
	private String multiploAplicadoView="";
	
	private AlcanceEturAfip alcanceEtur = AlcanceEturAfip.DESCONOCIDO;
	
	// Constructores
	public DecJurDetVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public DecJurDetVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDetalle(desc);
	}

	
	// Getters y Setters
	public DecJurVO getDecJur() {
		return decJur;
	}

	public void setDecJur(DecJurVO decJur) {
		this.decJur = decJur;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public RecConADecVO getRecConADec() {
		return recConADec;
	}

	public void setRecConADec(RecConADecVO recConADec) {
		this.recConADec = recConADec;
	}

	public Double getBase() {
		return base;
	}

	public void setBase(Double base) {
		this.base = base;
	}

	public Double getMultiplo() {
		return multiplo;
	}

	public void setMultiplo(Double multiplo) {
		this.multiplo = multiplo;
	}

	public Double getSubtotal1() {
		return subtotal1;
	}

	public void setSubtotal1(Double subtotal1) {
		this.subtotal1 = subtotal1;
		this.subtotal1View= (this.subtotal1!=null)?NumberUtil.truncate(this.subtotal1, SiatParam.DEC_IMPORTE_DB).toString():"";
	}

	public Double getCanUni() {
		return canUni;
	}

	public void setCanUni(Double canUni) {
		this.canUni = canUni;
	}

	public RecTipUniVO getRecTipUni() {
		return recTipUni;
	}

	public void setRecTipUni(RecTipUniVO recTipUni) {
		this.recTipUni = recTipUni;
	}

	public RecConADecVO getTipoUnidad() {
		return tipoUnidad;
	}

	public void setTipoUnidad(RecConADecVO tipoUnidad) {
		this.tipoUnidad = tipoUnidad;
	}

	public String getDesTipoUnidad() {
		return desTipoUnidad;
	}

	public void setDesTipoUnidad(String desTipoUnidad) {
		this.desTipoUnidad = desTipoUnidad;
	}

	public Double getValRef() {
		return valRef;
	}

	public void setValRef(Double valRef) {
		this.valRef = valRef;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public Double getValUnidad() {
		return valUnidad;
	}

	public void setValUnidad(Double valUnidad) {
		this.valUnidad = valUnidad;
	}

	public Double getSubtotal2() {
		return subtotal2;
	}

	public void setSubtotal2(Double subtotal2) {
		this.subtotal2 = subtotal2;
	}

	public Double getMinimo() {
		return minimo;
	}

	public void setMinimo(Double minimo) {
		this.minimo = minimo;
	}

	public Double getTotalConcepto() {
		return totalConcepto;
	}

	public void setTotalConcepto(Double totalConcepto) {
		this.totalConcepto = totalConcepto;
	}

	public String getTotalConceptoView() {
		return (this.totalConcepto!=null)?NumberUtil.truncate(this.totalConcepto, SiatParam.DEC_IMPORTE_DB).toString():"";
	}

	public void setTotalConceptoView(String totalConceptoView) {
		this.totalConceptoView = totalConceptoView;
	}

	public String getBaseView() {
		return (this.base!=null)?NumberUtil.truncate(this.base, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}

	public void setBaseView(String baseView) {
		this.baseView = baseView;
	}

	public String getSubtotal1View() {
		return subtotal1View;
	}

	public void setSubtotal1View(String subtotal1View) {
		this.subtotal1View = subtotal1View;
	}
	
	public AlcanceEturAfip getAlcanceEtur() {
		return alcanceEtur;
	}
	
	public void setAlcanceEtur(AlcanceEturAfip alcanceEtur) {
		this.alcanceEtur = alcanceEtur;
	}
	
	// Buss flags getters y setters
	
	

	// View flags getters
	// View getters
	public String getSubtotal2View() {
		return (this.subtotal2!=null)?NumberUtil.round(this.subtotal2, SiatParam.DEC_IMPORTE_VIEW).toString():"0.00";
	}

	public void setSubtotal2View(String subtotal2View) {
		this.subtotal2View = subtotal2View;
	}
	
	public String getValUnidadView() {
		return (this.valUnidad!=null)?NumberUtil.round(this.valUnidad, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}

	public void setValUnidadView(String valUnidadView) {
		this.valUnidadView = valUnidadView;
	}

	public String getMultiploView(){
		RecAliVO recAli = new RecAliVO(this.multiplo);
		return recAli.getAlicuotaView();
	}

	public String getBaseAplicadaView() {
		return baseAplicadaView;
	}

	public void setBaseAplicadaView(String baseAplicadaView) {
		this.baseAplicadaView = baseAplicadaView;
	}

	public String getMultiploAplicadoView() {
		return multiploAplicadoView;
	}

	public void setMultiploAplicadoView(String multiploAplicadoView) {
		this.multiploAplicadoView = multiploAplicadoView;
	}
	
	public String getMinimoView() {
		return (this.minimo!=null)?NumberUtil.round(this.minimo, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
}
