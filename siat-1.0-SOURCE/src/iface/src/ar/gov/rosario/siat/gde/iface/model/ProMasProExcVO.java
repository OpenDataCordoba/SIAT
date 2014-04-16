//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * VO correspondiente al Procurador a excluir del envio a judicial
 * 
 * @author tecso
 *
 */
public class ProMasProExcVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "proMasProExcVO";
	
	private String observacion;  // varchar(255)
	
	private ProcesoMasivoVO procesoMasivo = new ProcesoMasivoVO();

	private ProcuradorVO    procurador = new ProcuradorVO();

	 
	// Contructores
	public ProMasProExcVO(){
		super();
	}

	// Getters y Setters
	public ProcesoMasivoVO getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivoVO procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public ProcuradorVO getProcurador() {
		return procurador;
	}
	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}
	
}
