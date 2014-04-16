//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.pad.iface.model.BrocheVO;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object de la Exencion de un contribuyente
 * @author tecso
 *
 */
public class ContribExeVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "contribExeVO";

	private String desContribExe;
	private ContribuyenteVO contribuyente = new ContribuyenteVO();
	private ExencionVO exencion = new ExencionVO();
	private BrocheVO broche = new BrocheVO();
	private Date fechaDesde;
	private Date fechaHasta;
	
	// Buss Flags
	
	// View Flags
	private String fechaDesdeView; 
	private String fechaHastaView;	
	
	// Constructores
	public ContribExeVO() {
		super();
	}

	// Getters y Setters
	public String getDesContribExe() {
		return desContribExe;
	}

	public void setDesContribExe(String desContribExe) {
		this.desContribExe = desContribExe;
	}
	
	public ContribuyenteVO getContribuyente() {
		return contribuyente;
	}

	public void setContribuyente(ContribuyenteVO contribuyente) {
		this.contribuyente = contribuyente;
	}

	public ExencionVO getExencion() {
		return exencion;
	}

	public void setExencion(ExencionVO exencion) {
		this.exencion = exencion;
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

	public BrocheVO getBroche() {
		return broche;
	}

	public void setBroche(BrocheVO broche) {
		this.broche = broche;
	}

}
