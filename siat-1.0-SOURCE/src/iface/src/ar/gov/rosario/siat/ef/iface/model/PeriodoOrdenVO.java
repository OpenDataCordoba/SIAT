//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del PeriodoOrden
 * @author tecso
 *
 */
public class PeriodoOrdenVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "periodoOrdenVO";
	
	private OrdenControlVO ordenControl;

	private OrdConCueVO ordConCue;

    private Integer periodo;

    private Integer anio;
	
	// Buss Flags
	
	
	// View Constants
	
	
	private String anioView = "";
	private String periodoView = "";


	// Constructores
	public PeriodoOrdenVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public PeriodoOrdenVO(int id, String desc) {
		super();
		setId(new Long(id));
	}

	// Getters y Setters
	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControl) {
		this.ordenControl = ordenControl;
	}

	public OrdConCueVO getOrdConCue() {
		return ordConCue;
	}

	public void setOrdConCue(OrdConCueVO ordConCue) {
		this.ordConCue = ordConCue;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
		this.periodoView = StringUtil.formatInteger(periodo);
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
		this.anioView = StringUtil.formatInteger(anio);
	}
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public void setAnioView(String anioView) {
		this.anioView = anioView;
	}
	public String getAnioView() {
		return anioView;
	}

	public void setPeriodoView(String periodoView) {
		this.periodoView = periodoView;
	}
	public String getPeriodoView() {
		return periodoView;
	}
	
	public String getPeriodoAnioView(){
		return (StringUtil.completarCerosIzq(this.periodoView,2)+"/"+this.anioView);
	}

}
