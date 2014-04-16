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
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a EstadoReclamo
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_estadoReclamo")
public class EstadoReclamo extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "desEstadoReclamo")
	private String desEstadoReclamo;

	@Column(name = "respuesta")
	private String respuesta;

	public static final long ACTIVO = 1L;
	public static final long GENERADO_WEB = 2L;
	public static final long GENERADO_CMD = 3L;
	public static final long DESESTIMADO = 4L;
	public static final long PRESENTASE_CMD = 5L;
	public static final long RESUELTO_SIN_INFORMAR = 6L;
	public static final long CERRADO = 7L;

	// este estado no tiene que ver con los reclamos. se usa para identificar deuda que esta siendo procesada
	// por un asentamiento
	public static final Long EN_ASENTAMIENTO = 10L;

	// Constructores
	public EstadoReclamo(){
		super();
		// Seteo de valores default			
	}

	public EstadoReclamo(Long id){
		super();
		setId(id);
	}

	// Metodos de Clase
	public static EstadoReclamo getById(Long id) {
		return (EstadoReclamo) BalDAOFactory.getEstadoReclamoDAO().getById(id);
	}

	public static EstadoReclamo getByIdNull(Long id) {
		return (EstadoReclamo) BalDAOFactory.getEstadoReclamoDAO().getByIdNull(id);
	}

	public static List<EstadoReclamo> getList() {
		return (List<EstadoReclamo>) BalDAOFactory.getEstadoReclamoDAO().getList();
	}

	public static List<EstadoReclamo> getListActivos() {			
		return (List<EstadoReclamo>) BalDAOFactory.getEstadoReclamoDAO().getListActiva();
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
		/*	if (StringUtil.isNullOrEmpty(getCodEstadoReclamo())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.ESTADORECLAMO_CODESTADORECLAMO );
		}

		if (StringUtil.isNullOrEmpty(getDesEstadoReclamo())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.ESTADORECLAMO_DESESTADORECLAMO);
		}*/

		if (hasError()) {
			return false;
		}

		// Validaciones de unique
		/*	UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codEstadoReclamo");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, BalError.ESTADORECLAMO_CODESTADORECLAMO);			
		}*/

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el EstadoReclamo. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getEstadoReclamoDAO().update(this);
	}

	/**
	 * Desactiva el EstadoReclamo. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getEstadoReclamoDAO().update(this);
	}

	/**
	 * Valida la activacion del EstadoReclamo
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();

		//Validaciones 
		return true;
	}

	/**
	 * Valida la desactivacion del EstadoReclamo
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();

		//Validaciones 
		return true;
	}

	public String getDesEstadoReclamo() {
		return desEstadoReclamo;
	}

	public void setDesEstadoReclamo(String desEstadoReclamo) {
		this.desEstadoReclamo = desEstadoReclamo;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public String getRespuesta() {
		return respuesta;
	}

	//<#MetodosBeanDetalle#>
}
