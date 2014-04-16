//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

public class EjercicioVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private String desEjercicio;
	private Date fecIniEje;
	private Date fecFinEje;
	private EstEjercicioVO estEjercicio = new EstEjercicioVO();
	private Date fechaCierre;
	
	private String fecIniEjeView = "";
	private String fecFinEjeView = "";
	private String fechaCierreView = "";
	
	//Constructores 
	public EjercicioVO(){
		super();
	}

	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EjercicioVO(int id, String desEjercicio) {
		super();
		setId(new Long(id));
		setDesEjercicio(desEjercicio);
	}

	// Getters y Setters
	public String getDesEjercicio() {
		return desEjercicio;
	}
	public void setDesEjercicio(String desEjercicio) {
		this.desEjercicio = desEjercicio;
	}
	public EstEjercicioVO getEstEjercicio() {
		return estEjercicio;
	}
	public void setEstEjercicio(EstEjercicioVO estEjercicio) {
		this.estEjercicio = estEjercicio;
	}
	public Date getFecFinEje() {
		return fecFinEje;
	}
	public void setFecFinEje(Date fecFinEje) {
		this.fecFinEje = fecFinEje;
		this.fecFinEjeView = DateUtil.formatDate(fecFinEje, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFecFinEjeView() {
		return fecFinEjeView;
	}
	public void setFecFinEjeView(String fecFinEjeView) {
		this.fecFinEjeView = fecFinEjeView;
	}
	public Date getFechaCierre() {
		return fechaCierre;
	}
	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
		this.fechaCierreView = DateUtil.formatDate(fechaCierre, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaCierreView() {
		return fechaCierreView;
	}
	public void setFechaCierreView(String fechaCierreView) {
		this.fechaCierreView = fechaCierreView;
	}
	public Date getFecIniEje() {
		return fecIniEje;
	}
	public void setFecIniEje(Date fecIniEje) {
		this.fecIniEje = fecIniEje;
		this.fecIniEjeView = DateUtil.formatDate(fecIniEje, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFecIniEjeView() {
		return fecIniEjeView;
	}
	public void setFecIniEjeView(String fecIniEjeView) {
		this.fecIniEjeView = fecIniEjeView;
	}
	
}
