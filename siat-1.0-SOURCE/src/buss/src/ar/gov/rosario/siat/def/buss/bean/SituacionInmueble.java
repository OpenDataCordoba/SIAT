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
 * Bean correspondiente al Situacion del Inmueble.
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_situacionInmueble")
public class SituacionInmueble extends BaseBO {

	// Propiedades
	private static final long serialVersionUID = 1L;

	@Column(name = "desSituacionInmueble") 
	private String desSituacionInmueble;

	// Constructores
	public SituacionInmueble() {
		super();
	}

	// Metodos de clase
	public static SituacionInmueble getById(Long id) {
		return (SituacionInmueble) DefDAOFactory.getSituacionInmuebleDAO().getById(id);
	}
	
	public static SituacionInmueble getByIdNull(Long id) {
		return (SituacionInmueble) DefDAOFactory.getSituacionInmuebleDAO().getByIdNull(id);
	}

	public static List<SituacionInmueble> getListActivas() {			
		return (ArrayList<SituacionInmueble>) DefDAOFactory.getSituacionInmuebleDAO().getListActiva();
	}

	// Getters y setters
	public String getDesSituacionInmueble() {
		return desSituacionInmueble;
	}

	public void setDesSituacionInmueble(String desSituacionInmueble) {
		this.desSituacionInmueble = desSituacionInmueble;
	}
}