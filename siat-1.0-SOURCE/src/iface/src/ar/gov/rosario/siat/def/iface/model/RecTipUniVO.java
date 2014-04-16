//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Indica los valores de un tipo de unidad de un recurso.
 *
 * @author tecso
 *
 */
public class RecTipUniVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recTipUniVO";

	private RecursoVO recurso = new RecursoVO();
	
	private String nomenclatura="";
	
	private String descripcion="";

	private String codigoAfip="";
	
	// Constructores
	public RecTipUniVO(){
		super();
	}
	public RecTipUniVO(int id, String desc) {
		super();
		setId(new Long(id));
		setNomenclatura(desc);
	}
	
	
	// Getters y Setters
	public RecursoVO getRecurso(){
		return recurso;
	}
	
	public void setRecurso(RecursoVO recurso){
		this.recurso = recurso;
	}
	
	public String getNomenclatura() {
		return nomenclatura;
	}
	
	public void setNomenclatura(String nomenclatura) {
		this.nomenclatura = nomenclatura;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getCodigoAfip() {
		return codigoAfip;
	}
	
	public void setCodigoAfip(String codigoAfip) {
		this.codigoAfip = codigoAfip;
	}
	
}