//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Tipos de Titulares de Cuentas
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_tipoTitular")
public class TipoTitular extends BaseBO {

	// Propiedades
	private static final long serialVersionUID = 1L;

	public static final Long ID_TITULAR = 1L;
	
	@Column(name = "desTipoTitular")
	private String desTipoTitular;
	
	// Constructores
	public TipoTitular(){
		super();
	}
	
	// Getters y setters
	public String getDesTipoTitular() {
		return desTipoTitular;
	}
	public void setDesTipoTitular(String desTipoTitular) {
		this.desTipoTitular = desTipoTitular;
	}
	
	// Metodos de clase
	public static TipoTitular getById(Long id) {
		return (TipoTitular) PadDAOFactory.getTipoTitularDAO().getById(id);
	}
	
	public static TipoTitular getByIdNull(Long id) {
		return (TipoTitular) PadDAOFactory.getTipoTitularDAO().getByIdNull(id);
	}

	public static List<TipoTitular> getListActivos() {			
		return (ArrayList<TipoTitular>) PadDAOFactory.getTipoTitularDAO().getListActiva();
	}	

	// Metodos de Instancia
	// Validaciones

	// Metodos de negocio
	
}
