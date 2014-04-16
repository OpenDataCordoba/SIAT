//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a TipoProcurador
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_tipoProcurador")
public class TipoProcurador extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "desTipoProcurador")
	private String desTipoProcurador;

	// Constructores
	public TipoProcurador(){
		super();
	}
	
	public TipoProcurador(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipoProcurador getById(Long id) {
		return (TipoProcurador) GdeDAOFactory.getTipoProcuradorDAO().getById(id);
	}
	
	public static TipoProcurador getByIdNull(Long id) {
		return (TipoProcurador) GdeDAOFactory.getTipoProcuradorDAO().getByIdNull(id);
	}
	
	public static List<TipoProcurador> getList() {
		return (ArrayList<TipoProcurador>) GdeDAOFactory.getTipoProcuradorDAO().getList();
	}
	
	public static List<TipoProcurador> getListActivos() {			
		return (ArrayList<TipoProcurador>) GdeDAOFactory.getTipoProcuradorDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesTipoProcurador() {
		return desTipoProcurador;
	}

	public void setDesTipoProcurador(String desTipoProcurador) {
		this.desTipoProcurador = desTipoProcurador;
	}
	
	
	// Validaciones 
	
}
