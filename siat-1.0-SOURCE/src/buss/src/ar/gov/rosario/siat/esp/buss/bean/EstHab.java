//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.esp.buss.dao.EspDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean corresponiente al Estado de la Habilitacion.
 * 
 * @author tecso
 */
@Entity
@Table(name = "esp_estHab")
public class EstHab extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final long ID_ACTIVA = 1L;
	public static final long ID_ANULADA = 2L;
	public static final long ID_CANCELADA = 3L;
	
	@Column(name = "desEstHab")
	private String desEstHab;
	
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
	
	@Column(name="idEstEnHab") 
	private Long idEstEnHab;
	

	// Constructores
	public EstHab(){
		super();		
	}
	
	public EstHab(Long id){
		super();
		setId(id);
	}

	// Getters y Setters
	
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public String getDesEstHab() {
		return desEstHab;
	}
	public void setDesEstHab(String desEstHab) {
		this.desEstHab = desEstHab;
	}
	public Integer getEsInicial() {
		return esInicial;
	}
	public void setEsInicial(Integer esInicial) {
		this.esInicial = esInicial;
	}
	public Long getIdEstEnHab() {
		return idEstEnHab;
	}
	public void setIdEstEnHab(Long idEstEnHab) {
		this.idEstEnHab = idEstEnHab;
	}
	public Integer getPermiteModificar() {
		return permiteModificar;
	}
	public void setPermiteModificar(Integer permiteModificar) {
		this.permiteModificar = permiteModificar;
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
	
	// Metodos de clase	
	public static EstHab getById(Long id) {
		return (EstHab) EspDAOFactory.getEstHabDAO().getById(id);
	}
	
	public static EstHab getByIdNull(Long id) {
		return (EstHab) EspDAOFactory.getEstHabDAO().getByIdNull(id);
	}
	
	public static List<EstHab> getList() {
		return (ArrayList<EstHab>) EspDAOFactory.getEstHabDAO().getList();
	}
	
	public static List<EstHab> getListActivos() {			
		return (ArrayList<EstHab>) EspDAOFactory.getEstHabDAO().getListActiva();
	}
		
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();

		
		if (hasError()) {
			return false;
		}
		return !hasError();
	}
	
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
				
		if (hasError()) {
			return false;
		}
		return !hasError();
	}

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
		
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

}
