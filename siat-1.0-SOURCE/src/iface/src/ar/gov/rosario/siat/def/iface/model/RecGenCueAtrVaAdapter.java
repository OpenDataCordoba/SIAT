//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;


import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;

/**
 * Adapter de RecGenCueAtrVa
 * 
 * @author tecso
 */
public class RecGenCueAtrVaAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "recGenCueAtrVaAdapterVO";

	private RecursoVO recurso = new RecursoVO();
	private GenericAtrDefinition genericAtrDefinition = new GenericAtrDefinition();
	
	// Flags para mostrar o no la carga del atributo segun se haya seleccionado o no.
	private boolean paramAtributo = false;
	
	private List<AtributoVO> listAtributo = new ArrayList<AtributoVO>();
	
	public RecGenCueAtrVaAdapter(){
		super(DefSecurityConstants.ABM_RECGENCUEATRVA);
	}
	
	//	 Getters y Setter
	public RecursoVO getRecurso(){
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	
	public GenericAtrDefinition getGenericAtrDefinition() {
		return genericAtrDefinition;
	}
	
	public void setGenericAtrDefinition(GenericAtrDefinition genericAtrDefinition) {
		this.genericAtrDefinition = genericAtrDefinition;
	}

	public boolean isParamAtributo() {
		return paramAtributo;
	}

	public void setParamAtributo(boolean paramAtributo) {
		this.paramAtributo = paramAtributo;
	}

	public List<AtributoVO> getListAtributo() {
		return listAtributo;
	}

	public void setListAtributo(List<AtributoVO> listAtributo) {
		this.listAtributo = listAtributo;
	}

}
