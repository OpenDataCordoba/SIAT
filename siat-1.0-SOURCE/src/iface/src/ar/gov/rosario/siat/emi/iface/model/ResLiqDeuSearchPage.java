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
 * SearchPage del ResLiqDeu
 * 
 * @author Tecso
 *
 */
public class ResLiqDeuSearchPage extends SiatPageModel {

	public static final String NAME = "resLiqDeuSearchPageVO";
	
	private static final long serialVersionUID = 1L;

	private ResLiqDeuVO resLiqDeu= new ResLiqDeuVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	private List<EstadoCorridaVO> listEstadoCorrida = new ArrayList<EstadoCorridaVO>();
	
	// Constructores
	public ResLiqDeuSearchPage() {       
       super(EmiSecurityConstants.ABM_RESLIQDEU);        
    }
	
	// Getters y Setters
	public ResLiqDeuVO getResLiqDeu() {
		return resLiqDeu;
	}
	public void setResLiqDeu(ResLiqDeuVO resLiqDeu) {
		this.resLiqDeu = resLiqDeu;
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
			(EmiSecurityConstants.ABM_RESLIQDEU, EmiSecurityConstants.ACT_ADMINISTRAR_PROCESO);
	}
}
