//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.rod.buss.dao.RodDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a EstTramiteRA
 * 
 * @author tecso
 */
@Entity
@Table(name = "rod_estadoTramiteRA")
public class EstadoTramiteRA extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_INGRESADO = 1L;
	public static final Long ID_VERFICADO = 2L;
	public static final Long ID_ENVIADO_REGISTRO = 3L;
	public static final Long ID_ACEPTADO_REGISTRO = 4L;
	public static final Long ID_ACTUALIZADO_SPI = 5L;
	public static final Long ID_NOESESTADO = 7L;	
	
	@Column(name = "desEstTra")	
	private String desEstTra;
	
	@Column(name = "esEstado")	
	private Integer esEstado;
	
	@Column(name = "transiciones")	
	private String transiciones; // las transiciones posibles del estado, separadas por como (1,2,...)
	
	@ManyToOne()
	@JoinColumn(name="idArea")
	private Area area = new Area();
	
	// Constructores
	public EstadoTramiteRA(){
		super();
	}
	
	// Metodos de Clase
	public static EstadoTramiteRA getById(Long id) {
		return (EstadoTramiteRA) RodDAOFactory.getEstTramiteRADAO().getById(id);
	}
	
	public static EstadoTramiteRA getByIdNull(Long id) {
		return (EstadoTramiteRA) RodDAOFactory.getEstTramiteRADAO().getByIdNull(id);
	}
	
	public static List<EstadoTramiteRA> getList() {
		return (List<EstadoTramiteRA>) RodDAOFactory.getEstTramiteRADAO().getList();
	}
	
	public static List<EstadoTramiteRA> getListActivos() {			
		return (List<EstadoTramiteRA>) RodDAOFactory.getEstTramiteRADAO().getListActiva();
	}
	
	/** Devuleve una lista con aquellos EstTramiteRA que
	 *  sean estado y esten activos
	 * 
	 * @return
	 */
	public static List<EstadoTramiteRA> getListEstadosActivos() {
		List<EstadoTramiteRA> listEstados = new ArrayList<EstadoTramiteRA>();
		List<EstadoTramiteRA> listEstTramiteRA = EstadoTramiteRA.getListActivos();
		
		for (EstadoTramiteRA estTramiteRA : listEstTramiteRA) {
			if (estTramiteRA.isEstado()) {
				listEstados.add(estTramiteRA);
			}
		}

		return listEstados;
	}
	
	
	
	// Getters y setters
	public String getDesEstTra() {
		return desEstTra;
	}

	public void setDesEstTram(String desEstTra) {
		this.desEstTra = desEstTra;
	}
	
	public Integer getEsEstado() {
		return esEstado;
	}

	public void setEsEstado(Integer esEstado) {
		this.esEstado = esEstado;
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
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el EstTramiteRA. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		RodDAOFactory.getEstTramiteRADAO().update(this);
	}

	/**
	 * Desactiva el EstTramiteRA. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		RodDAOFactory.getEstTramiteRADAO().update(this);
	}
	
	/**
	 * Valida la activacion del EstTramiteRA
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del EstTramiteRA
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/** Obtine una lista de todos
	 *  los estados a los cuales se puede dirigir
	 *  el estado actual
	 * 
	 */
	@SuppressWarnings({"unchecked"})
	public List<EstadoTramiteRA> getListEstTramiteRADestino() {
		List<EstadoTramiteRA> listEstTramiteRA = new ArrayList<EstadoTramiteRA>(); 

		String transiciones = this.getTransiciones();
		
		if ( !StringUtil.isNullOrEmpty(transiciones) ) {
			listEstTramiteRA = RodDAOFactory.getEstTramiteRADAO().getListActivaByIds(transiciones);
		}
		
		return listEstTramiteRA;

	}
	
	public boolean isEstado () {
		boolean isEstado = false;

		if(this.getEsEstado() == 1) {
			isEstado = true;
		}

		return isEstado;
	}
	
}
