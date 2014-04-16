//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Contrato
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_contrato")
public class Contrato extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idRecurso") 
	private Recurso recurso;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idTipoContrato") 
	private TipoContrato tipoContrato;
	
	@Column(name = "numero")
	private String numero;
	
	@Column(name = "importe")
	private Double importe;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	// Constructores
	public Contrato(){
		super();
	}
	
	// Metodos de Clase
	public static Contrato getById(Long id) {
		return (Contrato) RecDAOFactory.getContratoDAO().getById(id);
	}
	
	public static Contrato getByIdNull(Long id) {
		return (Contrato) RecDAOFactory.getContratoDAO().getByIdNull(id);
	}
	
	public static List<Contrato> getList() {
		return (ArrayList<Contrato>) RecDAOFactory.getContratoDAO().getList();
	}
	
	public static List<Contrato> getListActivos() {			
		return (ArrayList<Contrato>) RecDAOFactory.getContratoDAO().getListActiva();
	}
	
	public static List<Contrato> getListByRecurso(Recurso recurso) {
		return (ArrayList<Contrato>) RecDAOFactory.getContratoDAO().getListByIdRecurso(recurso.getId());
	}
	
	public static List<Contrato> getListActivosByIdRecurso(Long id) {
		return (ArrayList<Contrato>) RecDAOFactory.getContratoDAO().getListActivosByIdRecurso(id);
	}
	
	// Getters y setters	
	
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public TipoContrato getTipoContrato() {
		return tipoContrato;
	}

	public void setTipoContrato(TipoContrato tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	
		// verificamos que no tenga Planillas de Cuadra Asociadas
		if (GenericDAO.hasReference(this, PlanillaCuadra .class, "contrato")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							RecError.CONTRATO_LABEL, RecError. PLANILLACUADRA_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (getRecurso() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
		}
		
		if (getTipoContrato() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.TIPOCONTRATO_LABEL);
		}

		if (StringUtil.isNullOrEmpty(getNumero())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.CONTRATO_NUMERO);
		}
		
		if (getImporte() != null && getImporte() < 0D){
			addRecoverableError(BaseError.MSG_VALORMENORQUECERO, RecError.CONTRATO_IMPORTE);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unicidad del contrato
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("numero");
		uniqueMap.addEntity("tipoContrato");
		uniqueMap.addEntity("recurso");
		if (!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO,
					RecError.CONTRATO_NUMERO);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el Contrato. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		RecDAOFactory.getContratoDAO().update(this);
	}

	/**
	 * Desactiva el Contrato. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		RecDAOFactory.getContratoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Contrato
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Contrato
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	public String getImporteForReport() {
		return StringUtil.formatDouble(this.getImporte());
	}

}
