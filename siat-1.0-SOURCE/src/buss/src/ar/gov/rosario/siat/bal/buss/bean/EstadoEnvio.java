//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Estados de envios de Osiris 
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_estadoEnvio")
public class EstadoEnvio extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final long ID_ESTADO_INCONSISTENTE=1L;
	public static final long ID_ESTADO_ELIMINADO=2L;
	public static final long ID_ESTADO_PENDIENTE=3L;
	public static final long ID_ESTADO_PROCESADO_EXITO=4L;
	public static final long ID_ESTADO_PROCESADO_ERROR=5L;
	public static final long ID_ESTADO_CONCILIADO=6L;

	@Column
	private String desEstado;
	
	// Constructores
	public EstadoEnvio(){
		super();
	}
	
	
	//Metodos de clase
	public static EstadoEnvio getById(Long id) {
		return (EstadoEnvio) BalDAOFactory.getEstadoEnvioDAO().getById(id);  
	}
	
	public static EstadoEnvio getByIdNull(Long id) {
		return (EstadoEnvio) BalDAOFactory.getEstadoEnvioDAO().getByIdNull(id);
	}
	
	public static List<EstadoEnvio> getList() {
		return (List<EstadoEnvio>) BalDAOFactory.getEstadoEnvioDAO().getList();
	}
	
	public static List<EstadoEnvio> getListActivos() {			
		return (List<EstadoEnvio>) BalDAOFactory.getEstadoEnvioDAO().getListActiva();
	}


	public String getDesEstado() {
		return desEstado;
	}


	public void setDesEstado(String desEstado) {
		this.desEstado = desEstado;
	}

	
	
	
	
	
	
	


	
	
	// Metodos de Instancia


	
	
}
