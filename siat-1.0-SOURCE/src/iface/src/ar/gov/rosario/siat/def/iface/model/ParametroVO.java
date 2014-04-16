//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;


/**
 * Value Object del Parametro
 * @author tecso
 *
 */
public class ParametroVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "parametroVO";
	
	private String codParam;
	
	private String desParam;

	private String valor;
	
		
	// Constructores
	public ParametroVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ParametroVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesParam(desc);
	}
	
	public String getCodParam() {
		return codParam;
	}

	public void setCodParam(String codParametro) {
		this.codParam = codParametro;
	}

	public String getDesParam() {
		return desParam;
	}

	public void setDesParam(String desParametro) {
		this.desParam = desParametro;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}
