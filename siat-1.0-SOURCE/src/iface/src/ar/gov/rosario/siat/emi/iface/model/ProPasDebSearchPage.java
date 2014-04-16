//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;

/**
 * SearchPage del ProPasDeb
 * 
 * @author Tecso
 *
 */
public class ProPasDebSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "proPasDebSearchPageVO";
	
	private ProPasDebVO proPasDeb= new ProPasDebVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	private List<EstadoCorridaVO> listEstadoCorrida = new ArrayList<EstadoCorridaVO>();
	
	// Constructores
	public ProPasDebSearchPage() {       
       super(EmiSecurityConstants.ABM_PROPASDEB);        
    }
	
	// Getters y Setters
	public ProPasDebVO getProPasDeb() {
		return proPasDeb;
	}
	
	public void setProPasDeb(ProPasDebVO proPasDeb) {
		this.proPasDeb = proPasDeb;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<EstadoCorridaVO> getListEstadoCorrida() {
		return listEstadoCorrida;
	}

	public void setListEstadoCorrida(List<EstadoCorridaVO> listEstadoCorrida) {
		this.listEstadoCorrida = listEstadoCorrida;
	}
	
	public String getAdministrarProcesoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(EmiSecurityConstants.ABM_PROPASDEB, EmiSecurityConstants.ACT_ADMINISTRAR_PROCESO);
	}

}
