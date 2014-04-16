//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.gde.iface.model.DeudaVO;

public class CueExcSelDeuVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "cueExcSelDeuVO";
	
    private CueExcSelVO cueExcSel;
	
    private Long idDeuda;
	
    private DeudaVO deuda;
    
	private CasoVO caso = new CasoVO();
	
	private String observacion;  
	
	// Constructores
	public CueExcSelDeuVO() {
		super();
	}

	// Getters y Setters
	public CueExcSelVO getCueExcSel() {
		return cueExcSel;
	}
	public void setCueExcSel(CueExcSelVO cueExcSel) {
		this.cueExcSel = cueExcSel;
	}
	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public DeudaVO getDeuda() {
		return deuda;
	}
	public void setDeuda(DeudaVO deuda) {
		this.deuda = deuda;
	}

	
	public CasoVO getCaso() {
		return caso;
	}

	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	
	
}
