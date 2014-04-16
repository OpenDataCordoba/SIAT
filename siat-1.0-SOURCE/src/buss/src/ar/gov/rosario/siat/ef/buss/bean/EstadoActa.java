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
 * Bean correspondiente a EstadoActa
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_estadoActa")
public class EstadoActa extends BaseBO {
	
	private static final long serialVersionUID = 1L;


	public static final Long ID_CREADA = 1L;
	public static final Long ID_EN_ESPERA_APROBACION = 2L;
	public static final Long ID_REVISION = 3L;
	public static final Long ID_APROBADA = 4L;
	
	
	@Column(name = "desEstadoActa")
	private String desEstadoActa;

	//<#Propiedades#>
	
	// Constructores
	public EstadoActa(){
		super();
		// Seteo de valores default			
	}
	
	public EstadoActa(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static EstadoActa getById(Long id) {
		return (EstadoActa) EfDAOFactory.getEstadoActaDAO().getById(id);
	}
	
	public static EstadoActa getByIdNull(Long id) {
		return (EstadoActa) EfDAOFactory.getEstadoActaDAO().getByIdNull(id);
	}
	
	public static List<EstadoActa> getList() {
		return (List<EstadoActa>) EfDAOFactory.getEstadoActaDAO().getList();
	}
	
	public static List<EstadoActa> getListActivos() {			
		return (List<EstadoActa>) EfDAOFactory.getEstadoActaDAO().getListActiva();
	}


	// Getters y setters
	public String getDesEstadoActa() {
		return desEstadoActa;
	}

	public void setDesEstadoActa(String desEstadoActa) {
		this.desEstadoActa = desEstadoActa;
	}
	
	
	
	// Validaciones 

	// Metodos de negocio
	

	//<#MetodosBeanDetalle#>
}
