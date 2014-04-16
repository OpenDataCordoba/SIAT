//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Representa los conceptos que componen un determinado Recurso
 * @author tecso
 *
 */
public class RecConVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recConVO";

	private RecursoVO recurso = new RecursoVO();
	private String codRecCon;
	private String desRecCon;
	private String abrRecCon;
	private Double porcentaje;
	private String porcentajeView;
	private SiNo incrementa = SiNo.OpcionSelecionar;
	private Date fechaDesde;
	private String fechaDesdeView = "";
	private Date fechaHasta;
	private String fechaHastaView = "";
	private SiNo esVisible = SiNo.OpcionSelecionar;
	private Long ordenVisualizacion;
	private String ordenVisualizacionView;
	
	// Propiedades utilizadas para la emision externa
	private Double total = 0D;
	private String totalView="";
	
	// Constructores
	public RecConVO(){
		super();
	}
	
	//	 Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public RecConVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesRecCon(desc);
	}
	
	// Getters y Setters
	public RecursoVO getRecurso(){
		return recurso;
	}
	public void setRecurso(RecursoVO recurso){
		this.recurso = recurso;
	}
	public String getCodRecCon(){
		return codRecCon;
	}
	public void setCodRecCon(String codRecCon){
		this.codRecCon = codRecCon;
	}
	public String getDesRecCon(){
		return desRecCon;
	}
	public void setDesRecCon(String desRecCon){
		this.desRecCon = desRecCon;
	}
	public String getAbrRecCon(){
		return abrRecCon;
	}
	public void setAbrRecCon(String abrRecCon){
		this.abrRecCon = abrRecCon;
	}
	public Double getPorcentaje(){
		return porcentaje;
	}
	public void setPorcentaje(Double porcentaje){
		this.porcentaje = porcentaje;
		this.porcentajeView = StringUtil.formatDouble(porcentaje);
	}
	public String getPorcentajeView(){
		return porcentajeView;
	}
	public void setPorcentajeView(String porcentajeView){
	}
	public SiNo getIncrementa(){
		return incrementa;
	}
	public void setIncrementa(SiNo incrementa){
		this.incrementa = incrementa;
	}
	public Date getFechaDesde(){
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde){
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaDesdeView(){
		return fechaDesdeView;
	}
	public void setFechaDesdeView(String fechaDesdeView){
	}
	public Date getFechaHasta(){
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta){
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaHastaView(){
		return fechaHastaView;
	}
	public void setFechaHastaView(String fechaHastaView){
	}
	public SiNo getEsVisible(){
		return esVisible;
	}
	public void setEsVisible(SiNo esVisible){
		this.esVisible = esVisible;
	}
	public Long getOrdenVisualizacion(){
		return ordenVisualizacion;
	}
	public void setOrdenVisualizacion(Long ordenVisualizacion){
		this.ordenVisualizacion = ordenVisualizacion;
		this.ordenVisualizacionView = StringUtil.formatLong(ordenVisualizacion);
	}
	public String getOrdenVisualizacionView(){
		return ordenVisualizacionView;
	}
	public void setOrdenVisualizacionView(String ordenVisualizacionView){
	}

	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
		this.totalView = StringUtil.formatDouble(total);
	}

	public String getTotalView() {
		return totalView;
	}
	public void setTotalView(String totalView) {
		this.totalView = totalView;
	}
	
	
}
