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
 * Bean correspondiente a una Categoria de Inmueble.
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_categoriaInmueble")
public class CategoriaInmueble extends BaseBO {

	// Propiedades
	private static final long serialVersionUID = 1L;

	@Column(name = "desCategoriaInmueble") 
	private String desCategoriaInmueble;

	// Constructores
	public CategoriaInmueble() {
		super();
	}

	// Metodos de clase
	public static CategoriaInmueble getById(Long id) {
		return (CategoriaInmueble) DefDAOFactory.getCategoriaInmuebleDAO().getById(id);
	}
	
	public static CategoriaInmueble getByIdNull(Long id) {
		return (CategoriaInmueble) DefDAOFactory.getCategoriaInmuebleDAO().getByIdNull(id);
	}

	public static List<CategoriaInmueble> getListActivas() {			
		return (ArrayList<CategoriaInmueble>) DefDAOFactory.getCategoriaInmuebleDAO().getListActiva();
	}

	// Getters y setters
	public String getDesCategoriaInmueble() {
		return desCategoriaInmueble;
	}

	public void setDesCategoriaInmueble(String desCategoriaInmueble) {
		this.desCategoriaInmueble = desCategoriaInmueble;
	}
	
}