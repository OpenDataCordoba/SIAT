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
 * SearchPage del ImpMasDeu
 * 
 * @author Tecso
 *
 */
public class ImpMasDeuSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "impMasDeuSearchPageVO";
	
	private ImpMasDeuVO impMasDeu= new ImpMasDeuVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	private List<EstadoCorridaVO> listEstadoCorrida = new ArrayList<EstadoCorridaVO>();
	
	// Constructores
	public ImpMasDeuSearchPage() {       
       super(EmiSecurityConstants.ABM_IMPMASDEU);        
    }
	
	// Getters y Setters
	public ImpMasDeuVO getImpMasDeu() {
		return impMasDeu;
	}
	public void setImpMasDeu(ImpMasDeuVO impMasDeu) {
		this.impMasDeu = impMasDeu;
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
			(EmiSecurityConstants.ABM_IMPMASDEU, EmiSecurityConstants.ACT_ADMINISTRAR_PROCESO);
	}

}
