//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del DocSop
 * @author tecso
 *
 */
public class DocSopVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "docSopVO";
	
	private SiNo determinaAjuste = SiNo.OpcionSelecionar;
	private SiNo aplicaMulta = SiNo.OpcionSelecionar;
	private SiNo compensaSalAFav = SiNo.OpcionSelecionar;
	private SiNo devuelveSalAFav = SiNo.OpcionSelecionar;
	             
	private String desDocSop="";
	private String plantilla="";

	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public DocSopVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public DocSopVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesDocSop(desc);
	}
	
	// Getters y Setters
	public String getDesDocSop() {
		return desDocSop;
	}

	public void setDesDocSop(String desDocSop) {
		this.desDocSop = desDocSop;
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

	public SiNo getDeterminaAjuste() {
		return determinaAjuste;
	}

	public void setDeterminaAjuste(SiNo determinaAjuste) {
		this.determinaAjuste = determinaAjuste;
	}

	public SiNo getAplicaMulta() {
		return aplicaMulta;
	}

	public void setAplicaMulta(SiNo aplicaMulta) {
		this.aplicaMulta = aplicaMulta;
	}

	public SiNo getCompensaSalAFav() {
		return compensaSalAFav;
	}

	public void setCompensaSalAFav(SiNo compensaSalAFav) {
		this.compensaSalAFav = compensaSalAFav;
	}

	public SiNo getDevuelveSalAFav() {
		return devuelveSalAFav;
	}

	public void setDevuelveSalAFav(SiNo devuelveSalAFav) {
		this.devuelveSalAFav = devuelveSalAFav;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
