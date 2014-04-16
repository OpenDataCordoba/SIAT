//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a una Solicitud de accion sobre un Inmueble.
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_solicitudInmueble")
public class SolicitudInmueble extends BaseBO {

	// Propiedades
	private static final long serialVersionUID = 1L;

	@Column(name = "desSolicitudInmueble") 
	private String desSolicitudInmueble;

	@Column(name = "porcAplicSolic") 
	private Double porcAplicSolic;

	// Constructores
	public SolicitudInmueble() {
		super();
	}

	// Metodos de clase
	public static SolicitudInmueble getById(Long id) {
		return (SolicitudInmueble) DefDAOFactory.getSolicitudInmuebleDAO().getById(id);
	}
	
	public static SolicitudInmueble getByIdNull(Long id) {
		return (SolicitudInmueble) DefDAOFactory.getSolicitudInmuebleDAO().getByIdNull(id);
	}

	public static List<SolicitudInmueble> getListActivas() {			
		return (ArrayList<SolicitudInmueble>) DefDAOFactory.getSolicitudInmuebleDAO().getListActiva();
	}

	// Getters y setters
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
	}
}