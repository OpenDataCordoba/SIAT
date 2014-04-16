//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del RecursoArea
 * @author tecso
 *
 */
public class RecursoAreaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recursoAreaVO";
	
	private RecursoVO recurso = new RecursoVO();
	private AreaVO area = new AreaVO();
	private SiNo perCreaEmi = SiNo.OpcionSelecionar; // Permite Creacion de Cuenta y Emision de Deuda.
	
	
	// Constructores
	public RecursoAreaVO() {
		super();
	}

	
	// Getters y Setters
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	
	public AreaVO getArea() {
		return area;
	}
	public void setArea(AreaVO area) {
		this.area = area;
	}

	public SiNo getPerCreaEmi() {
		return perCreaEmi;
	}
	public void setPerCreaEmi(SiNo perCreaEmi) {
		this.perCreaEmi = perCreaEmi;
	}

}