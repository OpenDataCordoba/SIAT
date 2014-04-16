//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Servicio Banco - Recurso
 * @author tecso
 *
 */
public class SerBanRecVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "serBanRecVO";
	
	private ServicioBancoVO		servicioBanco = new ServicioBancoVO();
	private RecursoVO	recurso = new RecursoVO();
	private Date	fechaDesde;
	private String	fechaDesdeView = "";
	private Date	fechaHasta;
	private String	fechaHastaView = "";
	
	// Constructores
	public SerBanRecVO() {
		super();
	}

	// Getters y Setters
	public ServicioBancoVO getServicioBanco(){
		return servicioBanco;
	}
	public void setServicioBanco(ServicioBancoVO servicioBanco){
		this.servicioBanco = servicioBanco;
	}
	public RecursoVO getRecurso(){
		return recurso;
	}
	public void setRecurso(RecursoVO recurso){
		this.recurso = recurso;
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
	
}
