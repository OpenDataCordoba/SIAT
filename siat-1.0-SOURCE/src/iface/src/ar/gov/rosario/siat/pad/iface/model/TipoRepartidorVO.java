//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;

public class TipoRepartidorVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private String desTipoRepartidor;
	
	private RecursoVO recurso;
	
	private CriRepVO criRep;
	
	// Constructores
	
	public TipoRepartidorVO(){
		super();
	}
	
	public TipoRepartidorVO(int id, String desTipoRepartidor) {
		super(id);
		setDesTipoRepartidor(desTipoRepartidor);
	}

	// Getters y Setters
	
	public CriRepVO getCriRep() {
		return criRep;
	}
	public void setCriRep(CriRepVO criRep) {
		this.criRep = criRep;
	}
	public String getDesTipoRepartidor() {
		return desTipoRepartidor;
	}
	public void setDesTipoRepartidor(String desTipoRepartidor) {
		this.desTipoRepartidor = desTipoRepartidor;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

}
