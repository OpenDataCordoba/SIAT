//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipProMas
 * @author tecso
 *
 */
public class TipProMasVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipProMasVO";
	public static final String ID_TIPOPROMAS = "idTipProMas";
	
	private String desTipProMas;
	
	private Boolean esPreEnvioJudicial = Boolean.FALSE;
	private Boolean esEnvioJudicial  = Boolean.FALSE;
	private Boolean esReconfeccion   = Boolean.FALSE;
	private Boolean esSeleccionDeuda = Boolean.FALSE;
	
	// Constructores
	public TipProMasVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipProMasVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipProMas(desc);
	}

	
	// Getters y Setters
	public String getDesTipProMas() {
		return desTipProMas;
	}
	public void setDesTipProMas(String desTipProMas) {
		this.desTipProMas = desTipProMas;
	}
	public Boolean getEsEnvioJudicial() {
		return esEnvioJudicial;
	}
	public void setEsEnvioJudicial(Boolean esEnvioJudicial) {
		this.esEnvioJudicial = esEnvioJudicial;
	}
	public Boolean getEsPreEnvioJudicial() {
		return esPreEnvioJudicial;
	}
	public void setEsPreEnvioJudicial(Boolean esPreEnvioJudicial) {
		this.esPreEnvioJudicial = esPreEnvioJudicial;
	}
	public Boolean getEsReconfeccion() {
		return esReconfeccion;
	}
	public void setEsReconfeccion(Boolean esReconfeccion) {
		this.esReconfeccion = esReconfeccion;
	}
	public Boolean getEsSeleccionDeuda() {
		return esSeleccionDeuda;
	}
	public void setEsSeleccionDeuda(Boolean esSeleccionDeuda) {
		this.esSeleccionDeuda = esSeleccionDeuda;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
