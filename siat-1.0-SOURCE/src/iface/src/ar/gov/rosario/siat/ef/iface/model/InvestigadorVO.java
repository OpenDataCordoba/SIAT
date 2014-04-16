//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del Investigador
 * @author tecso
 *
 */
public class InvestigadorVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "investigadorVO";
	
	private String desInvestigador;

	private Long idPersona;

	private String desPersona="";
	
	private Date fechaDesde;

	private Date fechaHasta;

	
	// view properties
	private String fechaDesdeView ="";
	private String fechaHastaView ="";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public InvestigadorVO() {
		super();
	}
	
	// Constructor para utiliDesdezar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public InvestigadorVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesInvestigador(desc);
	}
	
	// Getters y Setters

	public String getDesInvestigador() {
		return desInvestigador;
	}

	public void setDesInvestigador(String desInvestigador) {
		this.desInvestigador = desInvestigador;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
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

	public String getDesPersona() {
		return desPersona;
	}
	
	public void setDesPersona(String desPersona) {
		this.desPersona = desPersona;
	}


	// Buss flags getters y setters
	
	/**
	 * Valida la vigencia del Investigador con la fechaDesde, fechaHasta y la fecha del dia
	 */
	public boolean getEsVigente() {
		
		if(DateUtil.isDateBefore(this.fechaDesde, new Date()) &&
			(this.fechaHasta==null || DateUtil.isDateAfter(this.fechaHasta, new Date())))
			return true;
		
		return false;
	}
	
	
	// View flags getters
	
	

	// View getters
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

}
