//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * VO correspondiente a logs de una Seleccion Almacenada
 * 
 * @author tecso
 *
 */
public class SelAlmLogVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "selAlmLogVO";
	
	private SelAlmVO    selAlm = new SelAlmVO();
	
	private AccionLogVO accionLog = new AccionLogVO();
	
	private String      detalleLog = "";
	
	// Contructores
	public SelAlmLogVO(){
		super();
	}

	// Getters y Setters
	public AccionLogVO getAccionLog() {
		return accionLog;
	}
	public void setAccionLog(AccionLogVO accionLog) {
		this.accionLog = accionLog;
	}
	public String getDetalleLog() {
		return detalleLog;
	}
	public void setDetalleLog(String detalleLog) {
		this.detalleLog = detalleLog;
	}
	public SelAlmVO getSelAlm() {
		return selAlm;
	}
	public void setSelAlm(SelAlmVO selAlm) {
		this.selAlm = selAlm;
	}
	
	// metodo para lograr que el tovo copie el usuarioUltMdf al usuario
	public void setUsuarioUltMdf(String usuarioUltMdf){
		this.setUsuario(usuarioUltMdf);
	}
	
}
