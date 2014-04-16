//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del EmiMat
 * @author tecso
 *
 */
public class EmiMatVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "emiMatVO";
	
	private String codEmiMat;
	
	private RecursoVO recurso = new RecursoVO();

	private List<ColEmiMatVO> listColEmiMat = new ArrayList<ColEmiMatVO>();
	
	// Buss Flags
	
	// View Constants
	
	// Constructores
	public EmiMatVO() {
		super();
	}
	
	public EmiMatVO(int id, String codEmiMat) {
		super(id);
		setCodEmiMat(codEmiMat);
	}
	
	// Getters y Setters
	public String getCodEmiMat() {
		return codEmiMat;
	}
	
	public void setCodEmiMat(String codEmiMat) {
		this.codEmiMat = codEmiMat;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public List<ColEmiMatVO> getListColEmiMat() {
		return listColEmiMat;
	}

	public void setListColEmiMat(List<ColEmiMatVO> listColEmiMat) {
		this.listColEmiMat = listColEmiMat;
	}
}
