//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Representa las diversas deudas que pueden generarse para un determinado Recurso.
 * @author tecso
 *
 */
public class RecClaDeuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recClaDeuVO";

	private RecursoVO recurso = new RecursoVO();
	private String desClaDeu;
	private String abrClaDeu;
	private Date fechaDesde;
	private Date fechaHasta;
	private SiNo esOriginal = SiNo.OpcionSelecionar;
	private SiNo actualizaDeuda = SiNo.OpcionSelecionar;
	
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	
	// Constructores
	public RecClaDeuVO(){
		super();
	}
	public RecClaDeuVO(int id, String descripcion){
		super();
		this.setId(new Long(id));
		this.setDesClaDeu(descripcion);
	}
	
	
	// Getters y Setters 
	public RecursoVO getRecurso(){
		return recurso;
	}
	public void setRecurso(RecursoVO recurso){
		this.recurso = recurso;
	}
	public String getDesClaDeu(){
		return desClaDeu;
	}
	public void setDesClaDeu(String desClaDeu){
		this.desClaDeu = desClaDeu;
	}
	public String getAbrClaDeu(){
		return abrClaDeu;
	}
	public void setAbrClaDeu(String abrClaDeu){
		this.abrClaDeu = abrClaDeu;
	}
	public Date getFechaDesde(){
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde){
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public Date getFechaHasta(){
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta){
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public SiNo getEsOriginal() {
		return esOriginal;
	}
	public void setEsOriginal(SiNo esOriginal) {
		this.esOriginal = esOriginal;
	}
	
	public SiNo getActualizaDeuda() {
		return actualizaDeuda;
	}
	public void setActualizaDeuda(SiNo actualizaDeuda) {
		this.actualizaDeuda = actualizaDeuda;
	}
	
	public String getFechaDesdeView(){
		return fechaDesdeView;
	}
	public void setFechaDesdeView(String fechaDesdeView){
		this.fechaDesdeView = fechaDesdeView;
	}
	public String getFechaHastaView(){
		return fechaHastaView;
	}
	public void setFechaHastaView(String fechaHastaView){
		this.fechaHastaView = fechaHastaView;
	}

}
