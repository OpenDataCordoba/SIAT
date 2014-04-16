//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;

/**
 * SearchPage del CodEmi
 * 
 * @author Tecso
 *
 */
public class CodEmiSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "codEmiSearchPageVO";
	
	private CodEmiVO codEmi= new CodEmiVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	private List<TipCodEmiVO> listTipCodEmi = new ArrayList<TipCodEmiVO>();
	
	// Constructores
	public CodEmiSearchPage() {       
       super(DefSecurityConstants.ABM_CODEMI);        
    }
	
	// Getters y Setters
	public CodEmiVO getCodEmi() {
		return codEmi;
	}
	
	public void setCodEmi(CodEmiVO codEmi) {
		this.codEmi = codEmi;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<TipCodEmiVO> getListTipCodEmi() {
		return listTipCodEmi;
	}

	public void setListTipCodEmi(List<TipCodEmiVO> listTipCodEmi) {
		this.listTipCodEmi = listTipCodEmi;
	}

}
