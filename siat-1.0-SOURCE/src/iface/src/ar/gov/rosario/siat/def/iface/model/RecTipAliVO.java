//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Indica los valores de un atributo del objeto imponible para los cuales debe generarse la cuenta del recurso indicado.
 *
 * @author tecso
 *
 */
public class RecTipAliVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recTipAliVO";

	private RecursoVO recurso = new RecursoVO();
	private String cod="";
	private String desTipoAlicuota="";
	
	
	// Constructores
	public RecTipAliVO(){
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR,
	// TODOS, etc.
	public RecTipAliVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoAlicuota(desc);
	}

	// Getters y Setters
	public RecursoVO getRecurso(){
		return recurso;
	}
	public void setRecurso(RecursoVO recurso){
		this.recurso = recurso;
	}
	public String getCod() {
		return cod;
	}
	public void setCod(String cod) {
		this.cod = cod;
	}
	public String getDesTipoAlicuota() {
		return desTipoAlicuota;
	}
	public void setDesTipoAlicuota(String desTipoAlicuota) {
		this.desTipoAlicuota = desTipoAlicuota;
	}
	
	

}
