//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;

/**
 * Adapter del Solicitud
 * 
 * @author tecso
 */
public class SolicitudAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "solicitudAdapterVO";
	
    private SolicitudVO solicitud = new SolicitudVO();

    private SolicitudCUITVO solicitudCUIT = new SolicitudCUITVO();
    
    private List<TipoSolicitudVO> listTipoSolicitud = new ArrayList<TipoSolicitudVO>();

    private List<EstSolicitudVO> listEstSolicitud = new ArrayList<EstSolicitudVO>();
    
    // Constructores
    public SolicitudAdapter(){
    	super(CasSecurityConstants.ABM_SOLICITUD);
    }
    
    //  Getters y Setters
	public SolicitudVO getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(SolicitudVO solicitudVO) {
		this.solicitud = solicitudVO;
	}

	public List<EstSolicitudVO> getListEstSolicitud() {
		return listEstSolicitud;
	}

	public void setListEstSolicitud(List<EstSolicitudVO> listEstSolicitud) {
		this.listEstSolicitud = listEstSolicitud;
	}

	public List<TipoSolicitudVO> getListTipoSolicitud() {
		return listTipoSolicitud;
	}

	public void setListTipoSolicitud(List<TipoSolicitudVO> listTipoSolicitud) {
		this.listTipoSolicitud = listTipoSolicitud;
	}

	public SolicitudCUITVO getSolicitudCUIT() {
		return solicitudCUIT;
	}

	public void setSolicitudCUIT(SolicitudCUITVO solicitudCUITVO) {
		this.solicitudCUIT = solicitudCUITVO;
	}

	// View getters
}