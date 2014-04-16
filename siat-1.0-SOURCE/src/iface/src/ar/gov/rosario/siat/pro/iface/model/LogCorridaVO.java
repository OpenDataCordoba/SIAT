//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del LogCorrida
 * @author tecso
 *
 */
public class LogCorridaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "logCorridaVO";
	
	private CorridaVO corrida = new CorridaVO();
	
	private Long paso;
	
	private String log;
	
	private String nivelView;
	
	// Constructores
	public LogCorridaVO() {
		super();
	}

	//	 Getters y Setters
	
	public CorridaVO getCorrida() {
		return corrida;
	}

	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public Long getPaso() {
		return paso;
	}

	public void setPaso(Long paso) {
		this.paso = paso;
	}

	public String getNivelView() {
		return nivelView;
	}

	public void setNivelView(String nivelView) {
		this.nivelView = nivelView;
	}
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
