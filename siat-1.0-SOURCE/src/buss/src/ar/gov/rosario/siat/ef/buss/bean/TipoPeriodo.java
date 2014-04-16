//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a TipoPeriodo
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_tipoPeriodo")
public class TipoPeriodo extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "desTipoPeriodo")
	private String desTipoPeriodo;
	
	
	// Constructores
	public TipoPeriodo(){
		super();
	}
	
	public TipoPeriodo(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipoPeriodo getById(Long id) {
		return (TipoPeriodo) EfDAOFactory.getTipoPeriodoDAO().getById(id);
	}
	
	public static TipoPeriodo getByIdNull(Long id) {
		return (TipoPeriodo) EfDAOFactory.getTipoPeriodoDAO().getByIdNull(id);
	}
	
	public static List<TipoPeriodo> getList() {
		return (List<TipoPeriodo>) EfDAOFactory.getTipoPeriodoDAO().getList();
	}
	
	public static List<TipoPeriodo> getListActivos() {			
		return (List<TipoPeriodo>) EfDAOFactory.getTipoPeriodoDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesTipoPeriodo() {
		return desTipoPeriodo;
	}

	public void setDesTipoPeriodo(String desTipoPeriodo) {
		this.desTipoPeriodo = desTipoPeriodo;
	}
	
	// Validaciones 

	// Metodos de negocio
	
}
