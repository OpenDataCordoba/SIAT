//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.model.IntegerVO;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.pro.iface.util.ProSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del Proceso
 * 
 * @author tecso
 */
public class ProcesoAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "procesoAdapterVO";
	
    private ProcesoVO proceso = new ProcesoVO();
    
    private List<SiNo> listSiNo = new ArrayList<SiNo>();
    
    private List<TipoProgEjecVO> listTipoProgEjec = new ArrayList<TipoProgEjecVO>();
    
    private List<TipoEjecucionVO> listTipoEjecucion = new ArrayList<TipoEjecucionVO>();
    
    private List<IntegerVO> listUnidadPeriodo = new ArrayList<IntegerVO>();
    
    private Boolean paramPeriodic = false;
    
    // Constructores
    public ProcesoAdapter(){
    	super(ProSecurityConstants.ABM_PROCESO);
    }
    
    //  Getters y Setters
	public ProcesoVO getProceso() {
		return proceso;
	}

	public void setProceso(ProcesoVO procesoVO) {
		this.proceso = procesoVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public List<TipoProgEjecVO> getListTipoProgEjec() {
		return listTipoProgEjec;
	}

	public void setListTipoProgEjec(List<TipoProgEjecVO> listTipoProgEjecVO) {
		this.listTipoProgEjec = listTipoProgEjecVO;
	}

	public List<TipoEjecucionVO> getListTipoEjecucion() {
		return listTipoEjecucion;
	}
	public void setListTipoEjecucion(List<TipoEjecucionVO> listTipoEjecucionVO) {
		this.listTipoEjecucion = listTipoEjecucionVO;
	}
	public List<IntegerVO> getListUnidadPeriodo() {
		return listUnidadPeriodo;
	}
	public Boolean getParamPeriodic() {
		return paramPeriodic;
	}
	public void setParamPeriodic(Boolean paramPeriodic) {
		this.paramPeriodic = paramPeriodic;
	}	
	// View getters
}
