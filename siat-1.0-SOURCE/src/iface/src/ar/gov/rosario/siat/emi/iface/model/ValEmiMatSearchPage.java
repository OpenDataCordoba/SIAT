//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;

/**
 * SearchPage del ValEmiMat
 * 
 * @author Tecso
 *
 */
public class ValEmiMatSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "valEmiMatSearchPageVO";
	
	private ValEmiMatVO valEmiMat= new ValEmiMatVO();

	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	// Constructores
	public ValEmiMatSearchPage() {       
       super(EmiSecurityConstants.ABM_VALEMIMAT);        
    }
	
	// Getters y Setters
	public ValEmiMatVO getValEmiMat() {
		return valEmiMat;
	}
	public void setValEmiMat(ValEmiMatVO valEmiMat) {
		this.valEmiMat = valEmiMat;
	}           
	
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	// View getters
}
