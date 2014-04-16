//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del DesRecClaDeu
 * @author tecso
 *
 */
public class DesRecClaDeuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "desRecClaDeuVO";
	
	private DesEspVO desEsp = new DesEspVO();

	private RecClaDeuVO recClaDeu = new RecClaDeuVO();

	private Date fechaDesde;

	private Date fechaHasta;

	private String fechaDesdeView;
	
	private String fechaHastaView;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public DesRecClaDeuVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public DesRecClaDeuVO(int id) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters

	public DesEspVO getDesEsp() {
		return desEsp;
	}

	public void setDesEsp(DesEspVO desEsp) {
		this.desEsp = desEsp;
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

	public RecClaDeuVO getRecClaDeu() {
		return recClaDeu;
	}

	public void setRecClaDeu(RecClaDeuVO recClaDeu) {
		this.recClaDeu = recClaDeu;
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

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
