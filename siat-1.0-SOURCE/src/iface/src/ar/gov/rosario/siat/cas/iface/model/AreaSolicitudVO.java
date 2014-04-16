//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;

/**
 * Value Object del AreaSolicitud
 * @author tecso
 *
 */
public class AreaSolicitudVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "areaSolicitudVO";
	
	private AreaVO areaDestino = new AreaVO();
	private TipoSolicitudVO tipoSolicitud = new TipoSolicitudVO(); 
	
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public AreaSolicitudVO() {
		super();
	}


	// Getters y Setters
	public AreaVO getAreaDestino() {
		return areaDestino;
	}
	public void setAreaDestino(AreaVO areaDestino) {
		this.areaDestino = areaDestino;
	}

	public TipoSolicitudVO getTipoSolicitud() {
		return tipoSolicitud;
	}
	public void setTipoSolicitud(TipoSolicitudVO tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}
	


	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
