//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.pro.buss.dao.ProDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente al Estado de la Corrida
 * 
 * @author tecso
 */

@Entity
@Table(name = "pro_estadoCorrida")
public class EstadoCorrida extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estadoCorrida";
	
	//1-En Preparacion
	public static final Long ID_EN_PREPARACION = 1L;
	//2-En espera comenzar
	public static final Long ID_EN_ESPERA_COMENZAR = 2L;
	//3-En espera continuar
	public static final Long ID_EN_ESPERA_CONTINUAR = 3L;
	//4-Procesando
	public static final Long ID_PROCESANDO = 4L;
	//5-Procesado con exito
	public static final Long ID_PROCESADO_CON_EXITO = 5L;
	//6-Procesado con error
	public static final Long ID_PROCESADO_CON_ERROR = 6L;
	//7-Validación con error
	public static final Long ID_VALIDACION_CON_ERROR = 7L;
	//8-Cancelado
	public static final Long ID_CANCELADO = 8L;
	//9-Abortado por excepción
	public static final Long ID_ABORTADO_POR_EXCEPCION = 9L;
	//10-Sin Procesar
	public static final Long ID_SIN_PROCESAR = 10L;

	@Column(name = "desEstadoCorrida")
	private String desEstadoCorrida; // VARCHAR(100) NOT NULL,
	
	// Constructores
	public EstadoCorrida(){
		super();
	}
	
	// Getters y Setters
	public String getDesEstadoCorrida() {
		return desEstadoCorrida;
	}
	public void setDesEstadoCorrida(String desEstadoCorrida) {
		this.desEstadoCorrida = desEstadoCorrida;
	}

	// Metodos de Clase
	public static EstadoCorrida getById(Long id) {
		return (EstadoCorrida) ProDAOFactory.getEstadoCorridaDAO().getById(id);  
	}
	
	public static EstadoCorrida getByIdNull(Long id) {
		return (EstadoCorrida) ProDAOFactory.getEstadoCorridaDAO().getByIdNull(id);
	}
	
	public static List<EstadoCorrida> getList() {
		return (ArrayList<EstadoCorrida>) ProDAOFactory.getEstadoCorridaDAO().getList();
	}
	
	public static List<EstadoCorrida> getListActivos() {			
		return (ArrayList<EstadoCorrida>) ProDAOFactory.getEstadoCorridaDAO().getListActiva();
	}


	// Metodos de Instancia

	// Validaciones

	// Metodos de negocio
	
}
