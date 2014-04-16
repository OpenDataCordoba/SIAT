//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a HisCamEstRec
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_hisCamEstRec")
public class HisCamEstRec extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
@Column(name = "idReclamo")
private Integer idReclamo;

@Column(name = "idEstadoReclamo")
private Integer idEstadoReclamo;

@Column(name = "fechaEstado")
private Date fechaEstado;

@Column(name = "desHisCamEstRec")
private String desHisCamEstRec;

	//<#Propiedades#>
	
	// Constructores
	public HisCamEstRec(){
		super();
		// Seteo de valores default			
	}
	
	public HisCamEstRec(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static HisCamEstRec getById(Long id) { 
		return (HisCamEstRec) BalDAOFactory.getHisCamEstRecDAO().getById(id);
	}
	
	public static HisCamEstRec getByIdNull(Long id) {
		return (HisCamEstRec) BalDAOFactory.getHisCamEstRecDAO().getByIdNull(id);
	}
	
	public static List<HisCamEstRec> getList() {
		return (List<HisCamEstRec>) BalDAOFactory.getHisCamEstRecDAO().getList();
	}
	
	public static List<HisCamEstRec> getListActivos() {			
		return (List<HisCamEstRec>) BalDAOFactory.getHisCamEstRecDAO().getListActiva();
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
/*		if (StringUtil.isNullOrEmpty(getCodHisCamEstRec())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.HISCAMESTREC_CODHISCAMESTREC );
		}
		
		if (StringUtil.isNullOrEmpty(getDesHisCamEstRec())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.HISCAMESTREC_DESHISCAMESTREC);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codHisCamEstRec");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, BalError.HISCAMESTREC_CODHISCAMESTREC);			
		}*/
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el HisCamEstRec. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getHisCamEstRecDAO().update(this);
	}

	/**
	 * Desactiva el HisCamEstRec. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getHisCamEstRecDAO().update(this);
	}
	
	/**
	 * Valida la activacion del HisCamEstRec
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del HisCamEstRec
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	public Integer getIdReclamo() {
		return idReclamo;
	}

	public void setIdReclamo(Integer idReclamo) {
		this.idReclamo = idReclamo;
	}

	public Integer getIdEstadoReclamo() {
		return idEstadoReclamo;
	}

	public void setIdEstadoReclamo(Integer idEstadoReclamo) {
		this.idEstadoReclamo = idEstadoReclamo;
	}

	public Date getFechaEstado() {
		return fechaEstado;
	}

	public void setFechaEstado(Date fechaEstado) {
		this.fechaEstado = fechaEstado;
	}

	public String getDesHisCamEstRec() {
		return desHisCamEstRec;
	}

	public void setDesHisCamEstRec(String desHisCamEstRec) {
		this.desHisCamEstRec = desHisCamEstRec;
	}
	
	//<#MetodosBeanDetalle#>
}
