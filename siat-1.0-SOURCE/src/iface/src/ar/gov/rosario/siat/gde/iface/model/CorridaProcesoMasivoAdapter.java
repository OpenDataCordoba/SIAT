//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

/**
 * Adapter de Corridas del Envio Judicial sin uso de ADP
 * 
 * @author tecso
 */
public class CorridaProcesoMasivoAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "corridaProcesoMasivoAdapterVO";
	
	private ProcesoMasivoVO procesoMasivo = new ProcesoMasivoVO(); 
 

	// Constructores
	public CorridaProcesoMasivoAdapter() {       
		//super(ProSecurityConstants.ABM_CORRIDA);        
    }

	// Getters y Setters
	public ProcesoMasivoVO getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivoVO procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
	
	
}
