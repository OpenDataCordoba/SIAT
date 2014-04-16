//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del ProRec
 * 
 * @author tecso
 */
public class ProRecAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "proRecAdapterVO";
	public static final String ENC_NAME = "encProRecAdapterVO";
	
    private ProRecVO proRec = new ProRecVO();
    
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
    
    // Constructores
    public ProRecAdapter(){
    	super(GdeSecurityConstants.ABM_PROREC);
    	ACCION_MODIFICAR_ENCABEZADO = GdeSecurityConstants.ABM_PROREC_ENC;
    }
    
    //  Getters y Setters
	public ProRecVO getProRec() {
		return proRec;
	}

	public void setProRec(ProRecVO proRecVO) {
		this.proRec = proRecVO;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	// View getters
}
