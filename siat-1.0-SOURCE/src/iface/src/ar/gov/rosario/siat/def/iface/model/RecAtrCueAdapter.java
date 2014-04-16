//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.RecAtrCueDefinition;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter de RecAtrCue
 * 
 * @author tecso
 */
public class RecAtrCueAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "recAtrCueAdapterVO";
	
	private RecursoVO recurso = new RecursoVO();

	private RecAtrCueDefinition recAtrCueDefinition = new RecAtrCueDefinition();
	
	private List<SiNo> listSiNo = new ArrayList<SiNo>();
	
	// Flags para mostrar o no la carga del atributo segun se haya seleccionado o no.
	private boolean paramAtributo = false;
	private boolean paramPoseeVigencia = false;
	
	public RecAtrCueAdapter(){
		super(DefSecurityConstants.ABM_RECATRCUE);
	}
	
	// Getters y Setter
	public RecursoVO getRecurso(){
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	
	public boolean isParamAtributo() {
		return paramAtributo;
	}

	public void setParamAtributo(boolean paramAtributo) {
		this.paramAtributo = paramAtributo;
	}

	public RecAtrCueDefinition getRecAtrCueDefinition() {
		return recAtrCueDefinition;
	}
	public void setRecAtrCueDefinition(RecAtrCueDefinition recAtrCueDefinition) {
		this.recAtrCueDefinition = recAtrCueDefinition;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public boolean isParamPoseeVigencia() {
		return paramPoseeVigencia;
	}
	public void setParamPoseeVigencia(boolean paramPoseeVigencia) {
		this.paramPoseeVigencia = paramPoseeVigencia;
	}
	
	
}
