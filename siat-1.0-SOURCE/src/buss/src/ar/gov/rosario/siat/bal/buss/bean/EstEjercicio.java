//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Estado de Ejercicio
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_estEjercicio")
public class EstEjercicio extends BaseBO {

	private static final long serialVersionUID = 1L;

	//1-Abierto
	public static final Long ID_ABIERTO = 1L;
	//2-Cerrado
	public static final Long ID_CERRADO = 2L;
	//3-Fururo
	public static final Long ID_FUTURO = 3L;
	
	@Column(name = "desEjeBal")
	private String desEjeBal;
	
	//Constructores 
	
	public EstEjercicio(){
		super();
	}

	// Getters Y Setters
	public String getDesEjeBal() {
		return desEjeBal;
	}
	public void setDesEjeBal(String desEjeBal) {
		this.desEjeBal = desEjeBal;
	}
	
	// Metodos de clase	
	public static EstEjercicio getById(Long id) {
		return (EstEjercicio) BalDAOFactory.getEstEjercicioDAO().getById(id);
	}
	
	public static EstEjercicio getByIdNull(Long id) {
		return (EstEjercicio) BalDAOFactory.getEstEjercicioDAO().getByIdNull(id);
	}
	
	public static List<EstEjercicio> getList() {
		return (ArrayList<EstEjercicio>) BalDAOFactory.getEstEjercicioDAO().getList();
	}
	
	public static List<EstEjercicio> getListActivos() {			
		return (ArrayList<EstEjercicio>) BalDAOFactory.getEstEjercicioDAO().getListActiva();
	}
	
}
