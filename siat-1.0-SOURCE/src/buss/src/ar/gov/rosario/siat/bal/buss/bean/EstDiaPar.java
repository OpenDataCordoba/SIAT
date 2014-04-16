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
 * Bean que representa los Estados de DiarioPartida.
 * 
 * @author Tecso
 *
 */
@Entity
@Table(name = "bal_estDiaPar")
public class EstDiaPar extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final long ID_CREADA = 1L;
	public static final long ID_IMPUTADA = 2L;
	public static final long ID_ANULADA = 3L;
	
	@Column(name = "desEstDiaPar")
	private String desEstDiaPar;
	
	@Column(name = "tipo")
	private String tipo;
	
	@Column(name = "transiciones")
	private String transiciones;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idArea") 
	private Area area;
	
	@Column(name = "exCodigos")
	private String exCodigos;
	
	@Column(name = "permiteModificar")
	private Integer permiteModificar;
	
	@Column(name = "esInicial")
	private Integer esInicial;
	
	@Column(name="idEstadoEnDiaPar") 
	private Long idEstadoEnDiaPar;
	
	@Column(name = "esResolucion")
	private Integer esResolucion;
	

	// Constructores
	public EstDiaPar(){
		super();		
	}
	
	public EstDiaPar(Long id){
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
	public String getDesEstDiaPar() {
		return desEstDiaPar;
	}
	public void setDesEstDiaPar(String desEstDiaPar) {
		this.desEstDiaPar = desEstDiaPar;
	}
	public Integer getEsInicial() {
		return esInicial;
	}
	public void setEsInicial(Integer esInicial) {
		this.esInicial = esInicial;
	}
	public Integer getEsResolucion() {
		return esResolucion;
	}
	public void setEsResolucion(Integer esResolucion) {
		this.esResolucion = esResolucion;
	}
	public String getExCodigos() {
		return exCodigos;
	}
	public void setExCodigos(String exCodigos) {
		this.exCodigos = exCodigos;
	}
	public Long getIdEstadoEnDiaPar() {
		return idEstadoEnDiaPar;
	}
	public void setIdEstadoEnDiaPar(Long idEstadoEnDiaPar) {
		this.idEstadoEnDiaPar = idEstadoEnDiaPar;
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
	public static EstDiaPar getById(Long id) {
		return (EstDiaPar) BalDAOFactory.getEstDiaParDAO().getById(id);
	}
	
	public static EstDiaPar getByIdNull(Long id) {
		return (EstDiaPar) BalDAOFactory.getEstDiaParDAO().getByIdNull(id);
	}
	
	public static List<EstDiaPar> getList() {
		return (ArrayList<EstDiaPar>) BalDAOFactory.getEstDiaParDAO().getList();
	}
	
	public static List<EstDiaPar> getListActivos() {			
		return (ArrayList<EstDiaPar>) BalDAOFactory.getEstDiaParDAO().getListActiva();
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
		if (GenericDAO.hasReference(this, DiarioPartida.class, "estDiaPar")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.ESTDIAPAR_LABEL , BalError.DIARIOPARTIDA_LABEL);
		}
		
		if (GenericDAO.hasReference(this, HisEstDiaPar.class, "estDiaPar")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.ESTDIAPAR_LABEL , BalError.HISESTDIAPAR_LABEL);
		}
		 */
		
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
	
}
