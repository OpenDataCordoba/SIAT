//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;


import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * SolicitudInmuebleVO
 * @author tecso
 *
 */
public class SolicitudInmuebleVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "solicitudInmuebleVO";
	
	private String desSolicitudInmueble = "";
	
	private Double porcAplicSolic;
	
	private String porcAplicSolicView = "";
	
	// Constructores
	public SolicitudInmuebleVO() {
		super();
        // Acciones y Metodos para seguridad
	}

	public SolicitudInmuebleVO(int id, String desSolicitudInmueble) {
		super();
		setId(new Long(id));
		setDesSolicitudInmueble(desSolicitudInmueble);
	}

	// Getters y Setters
	public String getDesSolicitudInmueble() {
		return desSolicitudInmueble;
	}

	public void setDesSolicitudInmueble(String desSolicitudInmueble) {
		this.desSolicitudInmueble = desSolicitudInmueble;
	}

	public Double getPorcAplicSolic() {
		return porcAplicSolic;
	}

	public void setPorcAplicSolic(Double porcAplicSolic) {
		this.porcAplicSolic = porcAplicSolic;
		this.porcAplicSolicView = StringUtil.formatDouble(porcAplicSolic);
	}

	public String getPorcAplicSolicView() {
		return porcAplicSolicView;
	}

	public void setPorcAplicSolicView(String porcAplicSolicView) {
		this.porcAplicSolicView = porcAplicSolicView;
	}

}
