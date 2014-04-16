//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter de Rescate Masivo
 * 
 * @author tecso
 */
public class RescateAdapter extends SiatPageModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME     = "rescateAdapterVO";
	public static final String ENC_NAME = "encRescateAdapterVO";
	
    private RescateVO rescate = new RescateVO();
    
      private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
    
    private List<PlanVO> listPlan = new ArrayList<PlanVO>();
    
   
    
    public RescateAdapter(){
    	// setea ACCION_AGREGAR, ACCION_MODIFICAR, ACCION_ELIMINAR, ACCION_VER, ACCION_ACTIVAR, ACCION_DESACTIVAR
    	super(GdeSecurityConstants.ABM_RESCATE);
    }

    // Getters y setters
	public List<PlanVO> getListPlan() {
		return listPlan;
	}
	public void setListPlan(List<PlanVO> listPlan) {
		this.listPlan = listPlan;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public RescateVO getRescate() {
		return rescate;
	}
	public void setRescate(RescateVO rescate) {
		this.rescate = rescate;
	}

    
}
