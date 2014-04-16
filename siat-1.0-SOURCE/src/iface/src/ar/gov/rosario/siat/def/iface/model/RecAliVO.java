//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Indica los valores de un atributo del objeto imponible para los cuales debe generarse la cuenta del recurso indicado.
 *
 * @author tecso
 *
 */
public class RecAliVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recConADecVO";

	private RecursoVO recurso = new RecursoVO();
	private RecTipAliVO recTipAli=new RecTipAliVO();
	private Double alicuota;
	private String observacion="";
	private Date fechaDesde;
	private String fechaDesdeView = "";
	private Date fechaHasta;
	private String fechaHastaView = "";
	private String alicuotaView="";
	
	
	// Constructores
	public RecAliVO(){
		super();
	}
	public RecAliVO(int id, String desc) {
		super();
		setId(new Long(id));
		setAlicuotaView(desc);
	}
	
	public RecAliVO(Double alicuota) {
		super();
		setAlicuota(alicuota);
	}
	
	// Getters y Setters
	public RecursoVO getRecurso(){
		return recurso;
	}
	public void setRecurso(RecursoVO recurso){
		this.recurso = recurso;
	}
	public RecTipAliVO getRecTipAli() {
		return recTipAli;
	}
	public void setRecTipAli(RecTipAliVO recTipAli) {
		this.recTipAli = recTipAli;
	}
	public Double getAlicuota() {
		return alicuota;
	}
	public void setAlicuota(Double alicuota) {
		this.alicuota = alicuota;
		if (alicuota!=null)
			this.alicuotaView=StringUtil.getValorPorcent(alicuota);
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	

	
	
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
	public String getAlicuotaView() {
		return alicuotaView;
	}
	public void setAlicuotaView(String alicuotaView) {
		this.alicuotaView = alicuotaView;
	}
	
	
	

}
