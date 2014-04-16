//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.seg.iface.util.SegSecurityConstants;

/**
 * SearchPage del Oficina
 * 
 * @author Tecso
 *
 */
public class OficinaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "oficinaSearchPageVO";
	
	private OficinaVO oficina= new OficinaVO();
	
	// Constructores
	public OficinaSearchPage() {       
       super(SegSecurityConstants.ABM_OFICINA);        
    }
	
	// Getters y Setters
	public OficinaVO getOficina() {
		return oficina;
	}
	public void setOficina(OficinaVO oficina) {
		this.oficina = oficina;
	}

	// View getters
}