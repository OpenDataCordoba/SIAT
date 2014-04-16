//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * VO correspondiente a una Accion de Logeo
 * 
 * @author tecso
 *
 */
public class AccionLogVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "accionLogVO";
	
	private String desAccionLog;
	 
	// Contructores
	public AccionLogVO(){
		super();
	}

	// Getters y Setters
	public String getDesAccionLog() {
		return desAccionLog;
	}
	public void setDesAccionLog(String desAccionLog) {
		this.desAccionLog = desAccionLog;
	}
	
}
