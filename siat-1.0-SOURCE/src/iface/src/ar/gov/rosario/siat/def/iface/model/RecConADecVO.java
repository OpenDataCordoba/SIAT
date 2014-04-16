//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Indica los valores de un atributo del objeto imponible para los cuales debe generarse la cuenta del recurso indicado.
 *
 * @author tecso
 *
 */
public class RecConADecVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recConADecVO";

	private RecursoVO recurso = new RecursoVO();
	private TipRecConADecVO tipRecConADec = new TipRecConADecVO();
	private RecTipUniVO recTipUni = new RecTipUniVO();
	private String codConcepto="";
	private String desConcepto="";
	private String observacion="";
	private Date fechaDesde;
	private String fechaDesdeView = "";
	private Date fechaHasta;
	private String fechaHastaView = "";
	private String codigoAfip = "";
	private List<ValUnRecConADeVO> listValUnRecConADe=new ArrayList<ValUnRecConADeVO>();
	
	// Constructores
	public RecConADecVO(){
		super();
	}
	
	public RecConADecVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesConcepto(desc);
	}
	
	// Getters y Setters
	public RecursoVO getRecurso(){
		return recurso;
	}
	public void setRecurso(RecursoVO recurso){
		this.recurso = recurso;
	}
	public String getCodConcepto() {
		return codConcepto;
	}
	public void setCodConcepto(String codConcepto) {
		this.codConcepto = codConcepto;
	}
	public String getDesConcepto() {
		return desConcepto;
	}
	public void setDesConcepto(String desConcepto) {
		this.desConcepto = desConcepto;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	
	
	public TipRecConADecVO getTipRecConADec() {
		return tipRecConADec;
	}

	public void setTipRecConADec(TipRecConADecVO tipRecConADec) {
		this.tipRecConADec = tipRecConADec;
	}

	public List<ValUnRecConADeVO> getListValUnRecConADe() {
		return listValUnRecConADe;
	}

	public void setListValUnRecConADe(List<ValUnRecConADeVO> listValUnRecConADe) {
		this.listValUnRecConADe = listValUnRecConADe;
	}

	public String getCodigoAfip() {
		return codigoAfip;
	}

	public void setCodigoAfip(String codigoAfip) {
		this.codigoAfip = codigoAfip;
	}

	//View getters
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}
	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	/**
	 *  Descripcion larga y completa para utilizar en tool tip text
	 * 
	 */
	public String getCodYDescripcion(){
		return (!StringUtil.isNullOrEmpty(this.codConcepto))? this.codConcepto+"-"+this.desConcepto:this.desConcepto;
	}

	/**
	 *  Para ser utilizado en combo 
	 */
	public String getCodYDescripcionMedio(){
		
		String ret = (!StringUtil.isNullOrEmpty(this.codConcepto))? this.codConcepto+"-"+this.desConcepto:this.desConcepto; 
		
		if (ret != null && ret.length() > 80)
			ret = StringUtil.substring(ret, 80) + " ...";
		
		return ret;
	}
	
	/**
	 * Para utilizar en grilla
	 * 
	 */
	public String getCodYDescripcionCorto() {
		
		String ret = (!StringUtil.isNullOrEmpty(this.codConcepto))? this.codConcepto+"-"+this.desConcepto:this.desConcepto; 
		
		if (ret != null && ret.length() > 20)
			ret = StringUtil.substring(ret, 20) + " ...";
		
		return ret;
	}
	
	public RecTipUniVO getRecTipUni() {
		return recTipUni;
	}

	public void setRecTipUni(RecTipUniVO recTipUni) {
		this.recTipUni = recTipUni;
	}
	
	/*
	  	queryString += " AND v.fechaDesde <= :fecha";
		queryString += " AND (v.fechaHasta IS NULL OR v.fechaHasta >= :fecha )";
		queryString += " AND v.valRefDes <= :valRef AND (v.valRefHas IS NULL OR v.valRefHas >= :valRef )"; 
	 */
	public Double getValorUnitario(Date fecha, Double valRef){
		
		Double valorRet = 0D;
		
		if (this.getListValUnRecConADe() != null){
			for (ValUnRecConADeVO val:getListValUnRecConADe()){
				if ( DateUtil.isDateBeforeOrEqual(val.getFechaDesde(), fecha) && 
						(val.getFechaHasta() == null || DateUtil.isDateAfterOrEqual(val.getFechaHasta(), fecha)) &&
						val.getValRefDes().doubleValue() <= valRef.doubleValue() && 
						(val.getValRefHas() == null || val.getValRefHas().doubleValue() >= valRef.doubleValue())){
					
					valorRet = val.getValorUnitario();
				}
			}
		}
		
		return valorRet;
	}

	
	public Double getValorUnitario(Date fecha){
		
		Double valorRet = 0D;
		
		if (this.getListValUnRecConADe() != null){
			for (ValUnRecConADeVO val:getListValUnRecConADe()){
				if ( DateUtil.isDateBeforeOrEqual(val.getFechaDesde(), fecha) && 
						(val.getFechaHasta() == null || DateUtil.isDateAfterOrEqual(val.getFechaHasta(), fecha))){
					
					valorRet = val.getValorUnitario();
				}
			}
		}
		
		return valorRet;
	}
}
