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
 * SearchPage del Convenio
 * 
 * @author Tecso
 *
 */
public class ConvenioSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "convenioSearchPageVO";
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private RecursoVO recurso = new RecursoVO();
	private ConvenioVO convenio= new ConvenioVO();
	
	// Lista de Recurso permitidos. Agregado para filtrar por recursos permitidos por area del usuario.
	private String listIdRecursoFiltro = null;
	
	// Constructores
	public ConvenioSearchPage() {       
       super(GdeSecurityConstants.ABM_CONVENIO);        
    }
	
	// Getters y Setters
	public ConvenioVO getConvenio() {
		return convenio;
	}
	public void setConvenio(ConvenioVO convenio) {
		this.convenio = convenio;
	}

	
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public String getListIdRecursoFiltro() {
		return listIdRecursoFiltro;
	}

	public void setListIdRecursoFiltro(String listIdRecursoFiltro) {
		this.listIdRecursoFiltro = listIdRecursoFiltro;
	}

	
	
	
	// View getters
}
