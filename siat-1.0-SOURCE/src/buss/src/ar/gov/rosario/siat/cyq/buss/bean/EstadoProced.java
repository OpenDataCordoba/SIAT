//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import ar.gov.rosario.siat.def.buss.bean.Area;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Estados del Procedimiento.
 * Representa los estado de los procedimientos de CyQ definidos.
 * 
 * @author tecso
 */
@Entity
@Table(name = "cyq_estadoProced")
public class EstadoProced extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final long ID_En_Relevamiento = 1L;
	public static final long ID_Modificacion_Datos= 2L;
	public static final long ID_Sin_Interes = 3L;
	public static final long ID_En_Gestion = 4L;
	public static final long ID_Baja_Conversion = 5L;
	public static final long ID_Enviado_Abogado = 6L;
	public static final long ID_Enviado_Gestion_Admin = 7L;
	public static final long ID_Enviado_Gestion_Judicial = 8L;
	public static final long ID_Incorporacion_Deuda = 9L;
	public static final long ID_Quita_Deuda = 10L;
	public static final long ID_Agrega_Cuenta = 11L;
	public static final long ID_Quita_Cuenta = 12L;
	public static final long ID_Modifica_Cuenta = 13L;
	
	@Column(name = "desEstadoProced")
	private String desEstadoProced;
	
	@Column(name = "tipo")
	private String tipo;

	@Column(name = "transiciones")
	private String transiciones;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idArea") 
	private Area area;

	@Column(name = "permiteModificar")
	private Integer permiteModificar;
	
	@Column(name = "esInicial")
	private Integer esInicial;
	
	@Column(name="idEstadoEnProced") 
	private Long idEstadoEnProced;

	// Constructores
	public EstadoProced(){
		super();
	}
	// Getters y Setters
	public String getDesEstadoProced() {
		return desEstadoProced;
	}
	public void setDesEstadoProced(String desEstadoProced) {
		this.desEstadoProced = desEstadoProced;
	}

	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTransiciones() {
		return transiciones;
	}
	public void setTransiciones(String transiciones) {
		this.transiciones = transiciones;
	}

	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}

	public Integer getPermiteModificar() {
		return permiteModificar;
	}
	public void setPermiteModificar(Integer permiteModificar) {
		this.permiteModificar = permiteModificar;
	}

	public Integer getEsInicial() {
		return esInicial;
	}
	public void setEsInicial(Integer esInicial) {
		this.esInicial = esInicial;
	}
	
	public Long getIdEstadoEnProced() {
		return idEstadoEnProced;
	}
	public void setIdEstadoEnProced(Long idEstadoEnProced) {
		this.idEstadoEnProced = idEstadoEnProced;
	}

	
	// Metodos de Clase
	public static EstadoProced getById(Long id) {
		return (EstadoProced) CyqDAOFactory.getEstadoProcedDAO().getById(id);
	}
	
	public static EstadoProced getByIdNull(Long id) {
		return (EstadoProced) CyqDAOFactory.getEstadoProcedDAO().getByIdNull(id);
	}
	
	public static List<EstadoProced> getList() {
		return (ArrayList<EstadoProced>) CyqDAOFactory.getEstadoProcedDAO().getList();
	}
	
	public static List<EstadoProced> getListActivos() {			
		return (ArrayList<EstadoProced>) CyqDAOFactory.getEstadoProcedDAO().getListActiva();
	}
	
	public static List<EstadoProced> getListTransicionesForEstado(EstadoProced estadoProced) throws Exception {			
		return (List<EstadoProced>)  CyqDAOFactory.getEstadoProcedDAO().getListTransicionesForEstado(estadoProced);
	}
	
	public static List<EstadoProced> getListEstados() throws Exception {			
		return (ArrayList<EstadoProced>) CyqDAOFactory.getEstadoProcedDAO().getListEstados();
	}
	
	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones		
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
}
