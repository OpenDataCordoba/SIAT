//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.model.SiNo;

public class ConAtrVO extends SiatBussImageModel {
	
	// Propiedades
	private static final long serialVersionUID = 1L;

	private AtributoVO atributo = new AtributoVO();
	private SiNo esAtrSegmentacion = SiNo.OpcionSelecionar;
	private String valorDefecto;
	private SiNo esVisConDeu = SiNo.OpcionSelecionar;
	private SiNo esAtributoBus = SiNo.OpcionSelecionar;
	private SiNo admBusPorRan = SiNo.OpcionSelecionar;
	
	
	// Constructores
	public ConAtrVO(){
		super();
	}

	// Getters y setters	
	public AtributoVO getAtributo() {
		return atributo;
	}
	public void setAtributo(AtributoVO atributo) {
		this.atributo = atributo;
	}

	public SiNo getEsAtrSegmentacion() {
		return esAtrSegmentacion;
	}

	public void setEsAtrSegmentacion(SiNo esAtrSegmentacion) {
		this.esAtrSegmentacion = esAtrSegmentacion;
	}

	public String getValorDefecto() {
		return valorDefecto;
	}

	public void setValorDefecto(String valorDefecto) {
		this.valorDefecto = valorDefecto;
	}

	public SiNo getEsVisConDeu() {
		return esVisConDeu;
	}

	public void setEsVisConDeu(SiNo esVisConDeu) {
		this.esVisConDeu = esVisConDeu;
	}

	public SiNo getEsAtributoBus() {
		return esAtributoBus;
	}

	public void setEsAtributoBus(SiNo esAtributoBus) {
		this.esAtributoBus = esAtributoBus;
	}

	public SiNo getAdmBusPorRan() {
		return admBusPorRan;
	}

	public void setAdmBusPorRan(SiNo admBusPorRan) {
		this.admBusPorRan = admBusPorRan;
	}
	
}
