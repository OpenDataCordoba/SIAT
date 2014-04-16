//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.seg.iface.util.SegSecurityConstants;

/**
 * Adapter del Oficina
 * 
 * @author tecso
 */
public class OficinaAdapter extends SiatAdapterModel{

	private static final long serialVersionUID = 1L;

	public static final String NAME = "oficinaAdapterVO";

	private OficinaVO oficina = new OficinaVO();
    
    // Constructores
    public OficinaAdapter(){
    	super(SegSecurityConstants.ABM_OFICINA);
    }
    
    //  Getters y Setters
	public OficinaVO getOficina() {
		return oficina;
	}

	public void setOficina(OficinaVO oficinaVO) {
		this.oficina = oficinaVO;
	}
	
	// View getters
}