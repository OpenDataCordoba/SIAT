//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Feriado
 * 
 * @author tecso
 */
public class FeriadoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "feriadoVO";
		
	private Date 	fechaFeriado;
	private String  fechaFeriadoView = "";
	private String 	desFeriado;
	
	// Constructores
	public FeriadoVO() {
		super();
	}
	
	public FeriadoVO(Long id, Date fechaFeriado, String desFeriado) {
		super(id);
		
	}
	
	// Getters y setters
	public String getDesFeriado() {
		return desFeriado;
	}

	public void setDesFeriado(String desFeriado) {
		this.desFeriado = desFeriado;
	}

	public Date getFechaFeriado() {
		return fechaFeriado;
	}

	public void setFechaFeriado(Date fechaFeriado) {
		this.fechaFeriado = fechaFeriado;
		this.fechaFeriadoView = DateUtil.formatDate(fechaFeriado, DateUtil.ddSMMSYYYY_MASK);
	}
	
	// View getters
	public String getFechaFeriadoView() {
		return fechaFeriadoView;
	}
	public void setFechaFeriadoView(String fechaFeriadoView) {
		this.fechaFeriadoView = fechaFeriadoView;
	}
		
}