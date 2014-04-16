//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.PORCENTAJE;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

public class DisParDetVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private DisParVO disPar = new DisParVO();
	private TipoImporteVO tipoImporte = new TipoImporteVO();
	private RecConVO recCon = new RecConVO();
	private PartidaVO partida = new PartidaVO();
	private Double porcentaje;
	private Date fechaDesde;
	private Date fechaHasta;
	
	private SiNo esEjeAct = SiNo.OpcionNula;
	
	private String porcentajeView = "";
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	
	//Constructores 
	public DisParDetVO(){
		super();
	}

	// Getters y Setters
	public DisParVO getDisPar() {
		return disPar;
	}
	public void setDisPar(DisParVO disPar) {
		this.disPar = disPar;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public PartidaVO getPartida() {
		return partida;
	}
	public void setPartida(PartidaVO partida) {
		this.partida = partida;
	}
	public Double getPorcentaje() {
		return porcentaje;
	}
	@PORCENTAJE
	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
		this.porcentajeView = StringUtil.formatDouble(porcentaje);
	}
	public RecConVO getRecCon() {
		return recCon;
	}
	public void setRecCon(RecConVO recCon) {
		this.recCon = recCon;
	}
	public TipoImporteVO getTipoImporte() {
		return tipoImporte;
	}
	public void setTipoImporte(TipoImporteVO tipoImporte) {
		this.tipoImporte = tipoImporte;
	}
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}
	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	public String getPorcentajeView() {
		return porcentajeView;
	}
	public void setPorcentajeView(String porcentajeView) {
		this.porcentajeView = porcentajeView;
	}
	public SiNo getEsEjeAct() {
		return esEjeAct;
	}
	public void setEsEjeAct(SiNo esEjeAct) {
		this.esEjeAct = esEjeAct;
	}
	

}
