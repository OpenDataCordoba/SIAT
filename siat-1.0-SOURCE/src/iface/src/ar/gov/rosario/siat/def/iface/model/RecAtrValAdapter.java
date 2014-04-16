//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;

/**
 * Adapter de RecAtrVal
 * 
 * @author tecso
 */
public class RecAtrValAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "recAtrValAdapterVO";
	
	private RecursoVO recurso = new RecursoVO();
	private GenericAtrDefinition genericAtrDefinition = new GenericAtrDefinition();
	
	// Flags para mostrar o no la carga del atributo segun se haya seleccionado o no.
	private boolean paramAtributo = false;
	
	public RecAtrValAdapter(){
		super(DefSecurityConstants.ABM_RECATRVAL);
	}
	
	// Getters y Setter
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

	
}
