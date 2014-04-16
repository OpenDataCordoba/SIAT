//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

public class LiqExencionesVO {
	
    private List<LiqExencionVO> listExeVigentes = new ArrayList<LiqExencionVO>();
    private List<LiqExencionVO> listExeDenegados = new ArrayList<LiqExencionVO>();
    private List<LiqExencionVO> listExeTramite = new ArrayList<LiqExencionVO>();
    
    //  Propiedades para la asignacion de permisos
    private boolean verCasoEnabled = false;// Poder ver detalles de casos: Expedientes, notas, etc.
    private boolean verHistoricoExeEnabled = false; // Poder ver historico de Solicitudes de Execcion
    
    //  Getters y Setters
	public List<LiqExencionVO> getListExeDenegados() {
		return listExeDenegados;
	}

	public void setListExeDenegados(List<LiqExencionVO> listExeDenegados) {
		this.listExeDenegados = listExeDenegados;
	}

	public List<LiqExencionVO> getListExeTramite() {
		return listExeTramite;
	}

	public void setListExeTramite(List<LiqExencionVO> listExeTramite) {
		this.listExeTramite = listExeTramite;
	}

	public List<LiqExencionVO> getListExeVigentes() {
		return listExeVigentes;
	}

	public void setListExeVigentes(List<LiqExencionVO> listExeVigentes) {
		this.listExeVigentes = listExeVigentes;
	}
	
	public boolean isVerCasoEnabled() {
		return verCasoEnabled;
	}

	public void setVerCasoEnabled(boolean verCasoEnabled) {
		this.verCasoEnabled = verCasoEnabled;
	}

	public boolean isVerHistoricoExeEnabled() {
		return verHistoricoExeEnabled;
	}

	public void setVerHistoricoExeEnabled(boolean verHistoricoExeEnabled) {
		this.verHistoricoExeEnabled = verHistoricoExeEnabled;
	}

	// View getters
	/**
	 * Devuelve true o false segun la cuenta posea o no exencion en alguno de los pasos Vigentes, Denegadas o en Tramite.
	 */
	public boolean getPoseeExencion(){		
		if (getListExeVigentes().size() > 0 ||
				getListExeDenegados().size() > 0 ||
					getListExeTramite().size() > 0)
			return true;
		else
			return false;
	}
}
