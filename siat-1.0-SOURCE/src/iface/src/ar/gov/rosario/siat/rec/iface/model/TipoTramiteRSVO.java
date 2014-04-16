//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del Novedad de tipo tramite de regimen simplificado
 * @author tecso
 *
 */
public class TipoTramiteRSVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	public static final Long ID_TRAMITE_ALTA=1L;
	public static final Long ID_TRAMITE_MODIFICACION=2L;
	public static final Long ID_TRAMITE_RECATEGORIZACION=3L;
	public static final Long ID_TRAMITE_BAJA=4L;
	public static final Long ID_TRAMITE_LISTARTRAMITES=5L;
	public static final Long ID_TRAMITE_VERTRAMITE=6L;

	
	private String desTipoTramite="";
	
	// Buss Flags

	
	//Constructores
	public TipoTramiteRSVO() {
		super();
	}
	
	public TipoTramiteRSVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoTramite(desc);
	}

	public String getDesTipoTramite() {
		return desTipoTramite;
	}

	public void setDesTipoTramite(String desTipoTramite) {
		this.desTipoTramite = desTipoTramite;
	}
	
	// Getters y Setters
	


}
