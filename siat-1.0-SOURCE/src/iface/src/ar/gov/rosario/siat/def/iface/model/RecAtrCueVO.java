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
 * Son los atributos que deberan ser valorizados cada vez que se relacione el Recurso con una Cuenta
 *
 * @author tecso
 *
 */
public class RecAtrCueVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recCueAtrVaVO";

	private RecursoVO recurso = new RecursoVO();
	private AtributoVO atributo = new AtributoVO();
	private String valorDefecto;
	private Date fechaDesde;
	private SiNo poseeVigencia = SiNo.OpcionSelecionar;
	private SiNo esVisConDeu = SiNo.OpcionSelecionar;
	private SiNo esVisRec = SiNo.OpcionSelecionar;
	private SiNo esRequerido = SiNo.OpcionSelecionar;
	
	private String fechaDesdeView = "";
	private Date fechaHasta;
	private String fechaHastaView = "";
	
	// Constructores
	public RecAtrCueVO(){
		super();
	}
	// Getters y Setters
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
	public String getValorDefecto(){
		return valorDefecto;
	}
	public void setValorDefecto(String valorDefecto){
		this.valorDefecto = valorDefecto;
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
		this.fechaDesdeView = fechaDesdeView;
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
		this.fechaHastaView = fechaHastaView;
	}
	public SiNo getPoseeVigencia() {
		return poseeVigencia;
	}
	public void setPoseeVigencia(SiNo poseeVigencia) {
		this.poseeVigencia = poseeVigencia;
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
	public SiNo getEsRequerido() {
		return esRequerido;
	}
	public void setEsRequerido(SiNo esRequerido) {
		this.esRequerido = esRequerido;
	}
	
}
