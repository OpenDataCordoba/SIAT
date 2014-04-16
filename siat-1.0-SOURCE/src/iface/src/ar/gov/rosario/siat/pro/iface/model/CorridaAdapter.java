//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.pro.iface.util.ProSecurityConstants;

/**
 * Adapter del Corrida
 * 
 * @author tecso
 */
public class CorridaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "corridaAdapterVO";
	
	private CorridaVO corrida= new CorridaVO();
	private Date 	fechaDesde; 
	private Date 	fechaHasta; 
	
	private String 	fechaDesdeView; 
	private String 	fechaHastaView; 
	
	private List<ProcesoVO> listProceso = new ArrayList<ProcesoVO>();
	
	// Su utilizan para mostrar los reportes del proceso
	private List<FileCorridaVO> listFileCorrida = new ArrayList<FileCorridaVO>();
	private boolean paramProcesadoOk = false;
	
	// Constructores
	public CorridaAdapter() {       
       super(ProSecurityConstants.ABM_CORRIDA);        
    }
	
	// Getters y Setters
	public CorridaVO getCorrida() {
		return corrida;
	}
	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	
	public ProcesoVO getProceso() {
		return corrida.getProceso();
	}
	public void setProceso(ProcesoVO proceso) {
		this.corrida.setProceso(proceso);
	}
		
	public List<FileCorridaVO> getListFileCorrida() {
		return listFileCorrida;
	}

	public void setListFileCorrida(List<FileCorridaVO> listFileCorrida) {
		this.listFileCorrida = listFileCorrida;
	}

	public boolean isParamProcesadoOk() {
		return paramProcesadoOk;
	}

	public void setParamProcesadoOk(boolean paramProcesadoOk) {
		this.paramProcesadoOk = paramProcesadoOk;
	}

	// View getters
	public String getFechaDesdeView() { 
		return fechaDesdeView;
	} 

	public String getFechaHastaView () {
		return fechaHastaView;
	}
	
	public List<ProcesoVO> getListProceso() {
		return listProceso;
	}

	public void setListProceso(List<ProcesoVO> listProceso) {
		this.listProceso = listProceso;
	}
}
