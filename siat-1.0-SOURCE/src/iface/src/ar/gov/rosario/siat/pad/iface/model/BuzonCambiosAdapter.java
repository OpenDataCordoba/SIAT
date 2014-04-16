//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.model.TipoModificacion;
import coop.tecso.demoda.iface.model.TipoPersona;

/**
 * Adapter del BuzonCambios
 * 
 * @author tecso
 */
public class BuzonCambiosAdapter extends SiatAdapterModel{
	
	public static final String NAME = "buzonCambiosAdapterVO";
	
    private BuzonCambiosVO buzonCambios = new BuzonCambiosVO();
    
    private List<TipoModificacion>	listTipoModificacion = new ArrayList<TipoModificacion>();
    private List<TipoPersona>		listTipoPersona = new ArrayList<TipoPersona>();
    private List<TipoDocumentoVO>	listTipoDocumento = new ArrayList<TipoDocumentoVO>();
    
    
    
    // Constructores
    public BuzonCambiosAdapter(){
    	super(PadSecurityConstants.ADM_BUZONCAMBIOS);
    }
    
    //  Getters y Setters
	public BuzonCambiosVO getBuzonCambios() {
		return buzonCambios;
	}
	public void setBuzonCambios(BuzonCambiosVO buzonCambiosVO) {
		this.buzonCambios = buzonCambiosVO;
	}

	public List<TipoModificacion> getListTipoModificacion() {
		return listTipoModificacion;
	}
	public void setListTipoModificacion(List<TipoModificacion> listTipoModificacion) {
		this.listTipoModificacion = listTipoModificacion;
	}

	public List<TipoPersona> getListTipoPersona() {
		return listTipoPersona;
	}
	public void setListTipoPersona(List<TipoPersona> listTipoPersona) {
		this.listTipoPersona = listTipoPersona;
	}

	public List<TipoDocumentoVO> getListTipoDocumento() {
		return listTipoDocumento;
	}
	public void setListTipoDocumento(List<TipoDocumentoVO> listTipoDocumento) {
		this.listTipoDocumento = listTipoDocumento;
	}

	

	
	// View getters
}
