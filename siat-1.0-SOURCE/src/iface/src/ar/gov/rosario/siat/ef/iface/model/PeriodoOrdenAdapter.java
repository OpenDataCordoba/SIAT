//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;

/**
 * Adapter del PeriodoOrden
 * 
 * @author tecso
 */
public class PeriodoOrdenAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "periodoOrdenAdapterVO";
	
    private PeriodoOrdenVO periodoOrden = new PeriodoOrdenVO();
    
    private List<OrdConCueVO>	listOrdConCue = new ArrayList<OrdConCueVO>();
        
    private String[] idsSelected ={};
    
    private Map<String, Double[]> mapAgrupPeriodoDeuda = new HashMap<String, Double[]>();
    
    private Integer periodoHasta;
    private Integer anioHasta;
    
    private boolean viewResult = false;
    
    // Constructores
    public PeriodoOrdenAdapter(){
    	super(EfSecurityConstants.ABM_PERIODOORDEN);
    }
    
    //  Getters y Setters
	public PeriodoOrdenVO getPeriodoOrden() {
		return periodoOrden;
	}

	public void setPeriodoOrden(PeriodoOrdenVO periodoOrdenVO) {
		this.periodoOrden = periodoOrdenVO;
	}

	public List<OrdConCueVO> getListOrdConCue() {
		return listOrdConCue;
	}

	public void setListOrdConCue(List<OrdConCueVO> listOrdConCue) {
		this.listOrdConCue = listOrdConCue;
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
	}

	public Integer getAnioHasta() {
		return anioHasta;
	}

	public void setAnioHasta(Integer anioHasta) {
		this.anioHasta = anioHasta;
	}

	public String[] getIdsSelected() {
		return idsSelected;
	}

	public void setIdsSelected(String[] idsSelectec) {
		this.idsSelected = idsSelectec;
	}

	
	public boolean getViewResult() {
		return viewResult;
	}

	public void setViewResult(boolean viewResult) {
		this.viewResult = viewResult;
	}

	public Map<String, Double[]> getMapAgrupPeriodoDeuda() {
		return mapAgrupPeriodoDeuda;
	}

	public void setMapAgrupPeriodoDeuda(Map<String, Double[]> mapAgrupPeriodoDeuda) {
		this.mapAgrupPeriodoDeuda = mapAgrupPeriodoDeuda;
	}
		
	// View getters
	public List<OrdConCueVO> getListOrdConCueOrderByRec(){
		List<OrdConCueVO> listOrdConCueTmp = getListOrdConCue();
		Comparator<OrdConCueVO> comp = new Comparator<OrdConCueVO>(){

			public int compare(OrdConCueVO o1, OrdConCueVO o2) {
				if(o1.getCuenta().getRecurso().getId()>o2.getCuenta().getRecurso().getId())
					return 1;
				if(o1.getCuenta().getRecurso().getId()<o2.getCuenta().getRecurso().getId())
					return -1;
				return 0;
			}
			
							};
		Collections.sort(listOrdConCueTmp, comp);
		return listOrdConCueTmp;
	}


	
	// flags getters

	
}
