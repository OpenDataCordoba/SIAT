//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;

/**
 * Adapter del Supervisor
 * 
 * @author tecso
 */
public class SupervisorAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "supervisorAdapterVO";
	
    private SupervisorVO supervisor = new SupervisorVO();
    
    // Constructores
    public SupervisorAdapter(){
    	super(EfSecurityConstants.ABM_SUPERVISOR);
    }
    
    //  Getters y Setters
	public SupervisorVO getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(SupervisorVO supervisorVO) {
		this.supervisor = supervisorVO;
	}
	
	// View getters
}
