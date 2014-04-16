//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;

/**
 * SearchPage del Sistema
 * 
 * @author Tecso
 *
 */
public class SistemaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "sistemaSearchPageVO";
	
	private SistemaVO sistema= new SistemaVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	// Constructores
	public SistemaSearchPage() {       
       super(BalSecurityConstants.ABM_SISTEMA);        
    }
	
	// Getters y Setters
	
	public SistemaVO getSistema() {
		return sistema;
	}
	public void setSistema(SistemaVO sistema) {
		this.sistema = sistema;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	// View getters
}
