//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Desglose - 
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_desglose")
public class Desglose extends BaseBO {
	
private static final long serialVersionUID = 1L;
	
@ManyToOne(optional=true, fetch=FetchType.LAZY)
@JoinColumn(name="idCuenta")
private Cuenta cuenta;	

@Column(name = "fechalimite")
private Date fechalimite;

@Column(name = "idCaso")
private String idCaso;

@Column(name = "observacion")
private String observacion;

@OneToMany()
@JoinColumn(name="idDesglose")
private List<DesgloseDet> listDesgloseDet = new ArrayList<DesgloseDet>();

	//<#Propiedades#>
	
	// Constructores
	public Desglose(){
		super();
		// Seteo de valores default			
	}
	
	public Desglose(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static Desglose getById(Long id) {
		return (Desglose) GdeDAOFactory.getDesgloseDAO().getById(id);
	}
	
	public static Desglose getByIdNull(Long id) {
		return (Desglose) GdeDAOFactory.getDesgloseDAO().getByIdNull(id);
	}
	
	public static List<Desglose> getList() {
		return (ArrayList<Desglose>) GdeDAOFactory.getDesgloseDAO().getList();
	}
	
	public static List<Desglose> getListActivos() {			
		return (ArrayList<Desglose>) GdeDAOFactory.getDesgloseDAO().getListActiva();
	}
	
	
	// Getters y setters
	
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
	
		//<#ValidateDelete#>
		
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
		
		// Validaciones de unique
	/*	UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codDesglose");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, GdeError.DESGLOSE_CODDESGLOSE);			
		}*/
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el Desglose. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getDesgloseDAO().update(this);
	}

	/**
	 * Desactiva el Desglose. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getDesgloseDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Desglose
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Desglose
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	public Date getFechalimite() {
		return fechalimite;
	}

	public void setFechalimite(Date fechalimite) {
		this.fechalimite = fechalimite;
	}

	public String getIdCaso() {
		return idCaso;
	}

	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public List<DesgloseDet> getListDesgloseDet() {
		return listDesgloseDet;
	}

	public void setListDesgloseDet(List<DesgloseDet> listDesgloseDet) {
		this.listDesgloseDet = listDesgloseDet;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	
	//<#MetodosBeanDetalle#>
}
