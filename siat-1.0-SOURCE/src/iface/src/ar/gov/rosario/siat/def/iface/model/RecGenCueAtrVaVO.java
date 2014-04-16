//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Indica los valores de un atributo del objeto imponible para los cuales debe generarse la cuenta del recurso indicado.
 *
 * @author tecso
 *
 */
public class RecGenCueAtrVaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recGenCueAtrVaVO";

	private RecursoVO recurso = new RecursoVO();
	private AtributoVO atributo = new AtributoVO();
	private String strValor;
	private Date fechaDesde;
	private String fechaDesdeView = "";
	private Date fechaHasta;
	private String fechaHastaView = "";
	
	// Constructores
	public RecGenCueAtrVaVO(){
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
	public String getStrValor(){
		return strValor;
	}
	public void setStrValor(String strValor){
		this.strValor = strValor;
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

}
