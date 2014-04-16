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
 * Bean correspondiente a EstadoPlanFis
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_estadoPlanFis")
public class EstadoPlanFis extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	public static final Long ID_EST_ABIERTO = 1L;

	public static final Long ID_EST_CERRADO = 2L;
	
	@Column(name = "desEstadoPlanFis")
	private String desEstadoPlanFis;

	//<#Propiedades#>
	
	// Constructores
	public EstadoPlanFis(){
		super();
		// Seteo de valores default			
	}
	
	public EstadoPlanFis(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static EstadoPlanFis getById(Long id) {
		return (EstadoPlanFis) EfDAOFactory.getEstadoPlanFisDAO().getById(id);
	}
	
	public static EstadoPlanFis getByIdNull(Long id) {
		return (EstadoPlanFis) EfDAOFactory.getEstadoPlanFisDAO().getByIdNull(id);
	}
	
	public static List<EstadoPlanFis> getList() {
		return (List<EstadoPlanFis>) EfDAOFactory.getEstadoPlanFisDAO().getList();
	}
	
	public static List<EstadoPlanFis> getListActivos() {			
		return (List<EstadoPlanFis>) EfDAOFactory.getEstadoPlanFisDAO().getListActiva();
	}

	
	
	// Getters y setters
	public String getDesEstadoPlanFis() {
		return desEstadoPlanFis;
	}
	
	public void setDesEstadoPlanFis(String desEstadoPlanFis) {
		this.desEstadoPlanFis = desEstadoPlanFis;
	}
	
}
