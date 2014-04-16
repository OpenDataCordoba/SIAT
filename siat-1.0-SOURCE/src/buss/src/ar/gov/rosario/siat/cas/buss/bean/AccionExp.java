//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.cas.buss.dao.CasDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a AccionExp
 * 
 * @author tecso
 */
@Entity
@Table(name = "cas_accionExp")
public class AccionExp extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	// Exe
	public static final Long ID_CREACION_TIPO_EXENCION = 1L; //*
	public static final Long ID_ASOCIAR_EXENCION_A_CUENTA = 2L; //*
	
	//Gde
	public static final Long ID_CREACION_PLAN = 5L; //*
	public static final Long ID_CREACION_PLANPRORROGA = 16L; //*
	public static final Long ID_FORMALIZACION_PLAN = 6L; //*
	public static final Long ID_ENVIO_PLANILLA_DEUDA_PROCURADOR = 9L;
	public static final Long ID_RECONFECCION_MASIVA = 13L; //*
	public static final Long ID_CREACION_DESCUENTO = 4L; //*
	public static final Long ID_CREACION_GESTION_JUDICIAL = 22L; //*
	public static final Long ID_CREACION_LIQ_COM = 23L;//*
	public static final Long ID_CREACION_SALDO_POR_CADUCIDAD = 24L;//*
	public static final Long ID_CREACION_VUELTA_ATRAS_SAL_POR_CAD = 25L;//*
	public static final Long ID_ANULACION_DEUDA = 26L;//*
	public static final Long ID_ENVIO_JUDICIAL = 27L;//*
	public static final Long ID_PRE_ENVIO_JUDICIAL = 28L;//*
	public static final Long ID_RESCATE = 29L;//*
	public static final Long ID_SALDO_POR_CAD_MASIVO = 30L;//*	
	public static final Long ID_SELECCION_DEUDA_GENERICA = 20L;
	public static final Long ID_RECONFECCION_ESPECIAL = 35L;
	public static final Long ID_COMPENSACION = 37L; //*
	public static final Long ID_CIERRE_COMERCIO = 38L; //*
	public static final Long ID_CIERRE_COMERCIO_NOEMIMUL = 39L; //*
	
	
	// Pad
	public static final Long ID_ASOCIAR_BROCHE_A_CUENTA = 3L; //*

	
	public static final Long ID_VALORIZAR_ATRIBUTO_CUENTA = 7L;
	public static final Long ID_VALORIZAR_ATRIBUTO_CONTRIBUYENTE = 8L;
	public static final Long ID_DEFINIR_DOMICILIO_FISCAL_CONTRIB = 12L; //*
	
	public static final Long ID_SELECT_EXCLUCION_DEUDA_CUENTA = 21L; //*
	
	// Ef
	public static final Long ID_CREACION_ORDEN_CONTROL_FISCAL = 10L;
	public static final Long ID_MODIFICACION_ORDEN_CONTROL_FISCAL = 36L;
	
	// Cdm
	public static final Long ID_CREACION_OBRA_CDM = 11L; //*
	public static final Long ID_ANULACION_OBRA_CDM = 31L; //*
	public static final Long ID_CREACION_OBRREPVEN_CDM = 32L; //*
	
	// Emi
	public static final Long ID_EMISION_EXTRAORDINARIA = 14L; //*
	
	// Bal
	public static final Long ID_ASENTAMIENTO_MANUAL = 15L;	//*
	public static final Long ID_ASEDEL_MANUAL = 40L;	//*
	
	public static final Long ID_SALDOAFAVOR = 33L;	//*
	
	public static final Long ID_DISTRIB_PARTIDA_PLAN = 17L; //*
	public static final Long ID_DISTRIB_PARTIDA_RECURSO = 18L; //*
	
	// Cas
	public static final Long ID_CREACION_SOLICITUD = 19L; //*
	
	// Cyq
	public static final Long ID_CREACION_PROCEDIMIENTO_CYQ = 34L; //*


	
	// Last id = 40
	//* = Utilizado 
	
	@Column(name = "desAccionExp")
	private String desAccionExp;


	// Constructores
	public AccionExp(){
		super();
		// Seteo de valores default			
	}
	
	public AccionExp(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static AccionExp getById(Long id) {
		return (AccionExp) CasDAOFactory.getAccionExpDAO().getById(id);
	}
	
	public static AccionExp getByIdNull(Long id) {
		return (AccionExp) CasDAOFactory.getAccionExpDAO().getByIdNull(id);
	}
	
	public static List<AccionExp> getList() {
		return (List<AccionExp>) CasDAOFactory.getAccionExpDAO().getList();
	}
	
	public static List<AccionExp> getListActivos() {			
		return (List<AccionExp>) CasDAOFactory.getAccionExpDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesAccionExp() {
		return desAccionExp;
	}

	public void setDesAccionExp(String desAccionExp) {
		this.desAccionExp = desAccionExp;
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
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
	
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el AccionExp. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		CasDAOFactory.getAccionExpDAO().update(this);
	}

	/**
	 * Desactiva el AccionExp. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		CasDAOFactory.getAccionExpDAO().update(this);
	}
	
	/**
	 * Valida la activacion del AccionExp
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del AccionExp
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//<#MetodosBeanDetalle#>
}
