//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * SearchPage del Tramite
 * 
 * @author Tecso
 *
 */
public class TramiteSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tramiteSearchPageVO";
	
	
	private List<TipoTramiteVO> listTipoTramite = new ArrayList<TipoTramiteVO>();
	private TramiteVO tramite= new TramiteVO();
	
	// Constructores
	public TramiteSearchPage() {       
       super(GdeSecurityConstants.ABM_TRAMITE);        
    }
	
	// Getters y Setters
	public TramiteVO getTramite() {
		return tramite;
	}
	public void setTramite(TramiteVO tramite) {
		this.tramite = tramite;
	}

	public List<TipoTramiteVO> getListTipoTramite() {
		return listTipoTramite;
	}

	public void setListTipoTramite(List<TipoTramiteVO> listTipoTramite) {
		this.listTipoTramite = listTipoTramite;
	}
	
	

	// View getters
}
