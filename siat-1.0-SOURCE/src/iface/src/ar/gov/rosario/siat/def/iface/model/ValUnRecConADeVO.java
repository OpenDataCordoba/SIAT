//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;

/**
 * Indica los valores de un tipo de unidad de un recurso.
 *
 * @author tecso
 *
 */
public class ValUnRecConADeVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "valUnRecConADeVO";

	private RecConADecVO recConADec = new RecConADecVO();
	
	private Double valorUnitario;
	
	private Double valRefDes;
	
	private Double valRefHas;
	
	private Date fechaDesde;
	
	private Date fechaHasta;
	
	private String valorUnitarioView="";
	
	private String valRefDesView="";
	
	private String valRefHasView="";
	
	private RecAliVO recAli=new RecAliVO();
	
	// Constructores
	public ValUnRecConADeVO(){
		super();
	}
	public ValUnRecConADeVO(int id, String desc) {
		super();
		setId(new Long(id));
		setValorUnitarioView(desc);
	}
	
	// Getters y Setters
	public RecConADecVO getRecConADec() {
		return recConADec;
	}
	public void setRecConADec(RecConADecVO recConADec) {
		this.recConADec = recConADec;
	}
	
	public Double getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	public Double getValRefDes() {
		return valRefDes;
	}
	public void setValRefDes(Double valRefDes) {
		this.valRefDes = valRefDes;
	}
	public Double getValRefHas() {
		return valRefHas;
	}
	public void setValRefHas(Double valRefHas) {
		this.valRefHas = valRefHas;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	
	
	public RecAliVO getRecAli() {
		return recAli;
	}
	public void setRecAli(RecAliVO recAli) {
		this.recAli = recAli;
	}
	public String getValorUnitarioView() {
		return (this.valorUnitario!=null)?NumberUtil.round(this.valorUnitario,SiatParam.DEC_IMPORTE_VIEW).toString():valorUnitarioView;
	}
	public void setValorUnitarioView(String valorUnitarioView) {
		this.valorUnitarioView = valorUnitarioView;
	}
	
	public String getFechaDesdeView(){
		return DateUtil.formatDate(this.fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public String getFechaHastaView(){
		return DateUtil.formatDate(this.fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getValRefDesView() {
		return (this.valRefDes!=null)?NumberUtil.round(this.valRefDes,SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	public void setValRefDesView(String valRefDesView) {
		this.valRefDesView = valRefDesView;
	}
	public String getValRefHasView() {
		return (this.valRefHas!=null)?NumberUtil.round(this.valRefHas,SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	public void setValRefHasView(String valRefHasView) {
		this.valRefHasView = valRefHasView;
	}
	
	


	
}