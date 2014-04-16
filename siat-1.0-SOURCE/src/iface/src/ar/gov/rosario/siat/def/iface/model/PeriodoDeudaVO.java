//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Representa la periodicidad de la deuda a emitir para un determinado Recurso.
 * @author tecso
 *
 */
public class PeriodoDeudaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "periodoDeudaVO";

	private String desPeriodoDeuda;
	
	private Integer periodos;
	
	private String periodoView;
	
	// Contructores
	public PeriodoDeudaVO(){
		super();
	}
	public PeriodoDeudaVO(int id, String desPeriodoDeuda) {
		super(id);
		setDesPeriodoDeuda(desPeriodoDeuda);
	}
	
	// Getters y Setters
	public String getDesPeriodoDeuda(){
		return desPeriodoDeuda;
	}
	public void setDesPeriodoDeuda(String desPeriodoDeuda){
		this.desPeriodoDeuda = desPeriodoDeuda;
	}
	public Integer getPeriodos() {
		return periodos;
	}
	public void setPeriodos(Integer periodos) {
		this.periodos = periodos;
		this.periodoView = StringUtil.formatInteger(periodos);
	}
	public String getPeriodoView() {
		return periodoView;
	}
	public void setPeriodoView(String periodoView) {
		this.periodoView = periodoView;
	}
	
}
