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
 * Indica cuales son los atributos que se deben valorizar al momento de la emision.
 *
 * @author tecso
 *
 */
public class RecAtrCueEmiVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recAtrCueEmiVaVO";

	private RecursoVO recurso = new RecursoVO();
	private AtributoVO atributo = new AtributoVO();
	private Date fechaDesde;
	private String fechaDesdeView = "";
	private Date fechaHasta;
	private String fechaHastaView = "";
	private SiNo esVisConDeu = SiNo.OpcionSelecionar;
	private SiNo esVisRec = SiNo.OpcionSelecionar;
	
	// Constructores
	public RecAtrCueEmiVO(){
		super();
	}
	// Getters y Setter
	public RecursoVO getRecurso(){
		return recurso;
	}
	
	public void setRecurso(RecursoVO recurso){
		this.recurso = recurso;
	}
	
	public AtributoVO getAtributo(){
		return atributo;
	}
	
	public void setAtributo(AtributoVO atributo){
		this.atributo = atributo;
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
	
	public SiNo getEsVisConDeu() {
		return esVisConDeu;
	}
	
	public void setEsVisConDeu(SiNo esVisConDeu) {
		this.esVisConDeu = esVisConDeu;
	}
	
	public SiNo getEsVisRec() {
		return esVisRec;
	}

	public void setEsVisRec(SiNo esVisRec) {
		this.esVisRec = esVisRec;
	}
	
}
