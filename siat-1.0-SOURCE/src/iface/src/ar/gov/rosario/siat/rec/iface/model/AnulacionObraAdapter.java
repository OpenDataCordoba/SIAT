//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;

/**
 * Adapter del AnulacionObra
 * 
 * @author tecso
 */
public class AnulacionObraAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "anulacionObraAdapterVO";
	
    private AnulacionObraVO anulacionObra = new AnulacionObraVO();
    
    private List<ObraVO> listObra = new ArrayList<ObraVO>();
    private List<PlanillaCuadraVO> listPlanillaCuadra = new ArrayList<PlanillaCuadraVO>();
    private List<PlaCuaDetVO> listPlaCuaDet = new ArrayList<PlaCuaDetVO>();
    
    // Constructores
    public AnulacionObraAdapter(){
    	super(RecSecurityConstants.ABM_ANULACIONOBRA);
    }
    
    //  Getters y Setters
	public AnulacionObraVO getAnulacionObra() {
		return anulacionObra;
	}

	public void setAnulacionObra(AnulacionObraVO anulacionObraVO) {
		this.anulacionObra = anulacionObraVO;
	}

	public List<ObraVO> getListObra() {
		return listObra;
	}

	public void setListObra(List<ObraVO> listObra) {
		this.listObra = listObra;
	}

	public List<PlanillaCuadraVO> getListPlanillaCuadra() {
		return listPlanillaCuadra;
	}

	public void setListPlanillaCuadra(List<PlanillaCuadraVO> listPlanillaCuadra) {
		this.listPlanillaCuadra = listPlanillaCuadra;
	}

	public List<PlaCuaDetVO> getListPlaCuaDet() {
		return listPlaCuaDet;
	}

	public void setListPlaCuaDet(List<PlaCuaDetVO> listPlaCuaDet) {
		this.listPlaCuaDet = listPlaCuaDet;
	}
}
