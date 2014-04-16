//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.def.buss.bean.Area;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean que representa los Estados de los Otros Ingresos de Tesoreria
 * 
 * @author Tecso
 *
 */
@Entity
@Table(name = "bal_estOtrIngTes")
public class EstOtrIngTes extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final long ID_REGISTRADO = 1L;
	public static final long ID_PROCESADO = 2L;
	public static final long ID_ANULADO = 3L;
	
	@Column(name = "desEstOtrIngTes")
	private String desEstOtrIngTes;
	
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
	
	@Column(name="idEstEnOtr") 
	private Long idEstEnOtr;
	

	// Constructores
	public EstOtrIngTes(){
		super();		
	}
	
	public EstOtrIngTes(Long id){
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
	public String getDesEstOtrIngTes() {
		return desEstOtrIngTes;
	}
	public void setDesEstOtrIngTes(String desEstOtrIngTes) {
		this.desEstOtrIngTes = desEstOtrIngTes;
	}
	public Integer getEsInicial() {
		return esInicial;
	}
	public void setEsInicial(Integer esInicial) {
		this.esInicial = esInicial;
	}
	public Long getIdEstEnOtr() {
		return idEstEnOtr;
	}
	public void setIdEstEnOtr(Long idEstEnOtr) {
		this.idEstEnOtr = idEstEnOtr;
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
	public static EstOtrIngTes getById(Long id) {
		return (EstOtrIngTes) BalDAOFactory.getEstOtrIngTesDAO().getById(id);
	}
	
	public static EstOtrIngTes getByIdNull(Long id) {
		return (EstOtrIngTes) BalDAOFactory.getEstOtrIngTesDAO().getByIdNull(id);
	}
	
	public static List<EstOtrIngTes> getList() {
		return (ArrayList<EstOtrIngTes>) BalDAOFactory.getEstOtrIngTesDAO().getList();
	}
	
	public static List<EstOtrIngTes> getListActivos() {			
		return (ArrayList<EstOtrIngTes>) BalDAOFactory.getEstOtrIngTesDAO().getListActiva();
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
		
		/*
		if (GenericDAO.hasReference(this, DiarioPartida.class, "estOtrIngTes")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.ESTOTRINGTES_LABEL , BalError.DIARIOPARTIDA_LABEL);
		}
		
		if (GenericDAO.hasReference(this, HisEstOtrIngTes.class, "estOtrIngTes")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.ESTOTRINGTES_LABEL , BalError.HISESTOTRINGTES_LABEL);
		}
		 */
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
}
