//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.model.SiNo;

public class TipoImporteVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private String codTipoImporte;
	private String desTipoImporte;
	private SiNo abreConceptos = SiNo.OpcionSelecionar;
	
	//Constructores 	
	public TipoImporteVO(){
		super();
	}

	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoImporteVO(int id, String desTipoImporte) {
		super();
		setId(new Long(id));
		setDesTipoImporte(desTipoImporte);
	}

	// Getters Y Setters
	public SiNo getAbreConceptos() {
		return abreConceptos;
	}
	public void setAbreConceptos(SiNo abreConceptos) {
		this.abreConceptos = abreConceptos;
	}
	public String getCodTipoImporte() {
		return codTipoImporte;
	}
	public void setCodTipoImporte(String codTipoImporte) {
		this.codTipoImporte = codTipoImporte;
	}
	public String getDesTipoImporte() {
		return desTipoImporte;
	}
	public void setDesTipoImporte(String desTipoImporte) {
		this.desTipoImporte = desTipoImporte;
	}
	
	
}
