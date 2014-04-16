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
 * Bean correspondiente a TipoObra
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_tipoobra")
public class TipoObra extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idRecurso") 
	private Recurso recurso;

	@Column(name = "desTipoObra")
	private String desTipoObra;

	@Column(name = "costoCuadra")
	private Double costoCuadra;

	@Column(name = "costoMetroFrente")
	private Double costoMetroFrente;

	@Column(name = "costoUT")
	private Double costoUT;
	
	@Column(name = "costoModulo")
	private Double costoModulo;
	
	// Constructores
	public TipoObra(){
		super();
		// Seteo de valores default	
	}
	
	public TipoObra(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipoObra getById(Long id) {
		return (TipoObra) RecDAOFactory.getTipoObraDAO().getById(id);
	}
	
	public static TipoObra getByIdNull(Long id) {
		return (TipoObra) RecDAOFactory.getTipoObraDAO().getByIdNull(id);
	}
	
	public static List<TipoObra> getList() {
		return (ArrayList<TipoObra>) RecDAOFactory.getTipoObraDAO().getList();
	}
	
	public static List<TipoObra> getListActivos() {			
		return (ArrayList<TipoObra>) RecDAOFactory.getTipoObraDAO().getListActiva();
	}

	public static List<TipoObra> getListByRecurso(Recurso recurso) throws Exception {			
		return (ArrayList<TipoObra>) RecDAOFactory.getTipoObraDAO().getListByRecurso(recurso.getId());
	}
	
	public static List<TipoObra> getListActivosByRecurso(Recurso recurso) throws Exception {			
		return (ArrayList<TipoObra>) RecDAOFactory.getTipoObraDAO().getListActivaByRecurso(recurso.getId());
	}
	
	// Getters y setters
	public String getDesTipoObra() {
		return desTipoObra;
	}

	public void setDesTipoObra(String desTipoObra) {
		this.desTipoObra = desTipoObra;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Double getCostoCuadra() {
		return costoCuadra;
	}

	public void setCostoCuadra(Double costoCuadra) {
		this.costoCuadra = costoCuadra;
	}

	public Double getCostoMetroFrente() {
		return costoMetroFrente;
	}

	public void setCostoMetroFrente(Double costoMetroFrente) {
		this.costoMetroFrente = costoMetroFrente;
	}

	public Double getCostoUT() {
		return costoUT;
	}

	public void setCostoUT(Double costoUT) {
		this.costoUT = costoUT;
	}

	public Double getCostoModulo() {
		return costoModulo;
	}

	public void setCostoModulo(Double costoModulo) {
		this.costoModulo = costoModulo;
	}

	// Metodos de negocio

	/**
	 * Activa el TipoObra. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		RecDAOFactory.getTipoObraDAO().update(this);
	}

	/**
	 * Desactiva el TipoObra. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		RecDAOFactory.getTipoObraDAO().update(this);
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
		//	limpiamos la lista de errores
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
		if (GenericDAO.hasReference(this,PlanillaCuadra.class, "tipoObra")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTRO_ASOCIADO, 
								RecError.TIPOOBRA_LABEL, 
								RecError.PLANILLACUADRA_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	/**
	 * Valida la activacion del TipoObra
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipoObra
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	private boolean validate() throws Exception {
		//	Validaciones
		if (getRecurso() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
		}
		
		if (StringUtil.isNullOrEmpty(getDesTipoObra())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.TIPOOBRA_DESTIPOOBRA);
		}
		
		if (getCostoCuadra() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.TIPOOBRA_COSTOCUADRA);
		}
		
		if (getCostoCuadra() != null && getCostoCuadra() < 0D){
			addRecoverableError(BaseError.MSG_VALORMENORQUECERO, RecError.TIPOOBRA_COSTOCUADRA);
		}
		
		if (getCostoMetroFrente() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.TIPOOBRA_COSTOMETROFRENTE);
		}
		
		if (getCostoMetroFrente() != null && getCostoMetroFrente() < 0D){
			addRecoverableError(BaseError.MSG_VALORMENORQUECERO, RecError.TIPOOBRA_COSTOMETROFRENTE);
		}

		if (getCostoUT() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.TIPOOBRA_COSTOUT);
		}

		if (getCostoUT() != null && getCostoUT() < 0D){
			addRecoverableError(BaseError.MSG_VALORMENORQUECERO, RecError.TIPOOBRA_COSTOUT);
		}

		if (getCostoModulo() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.TIPOOBRA_COSTOMODULO);
		}

		if (getCostoModulo() != null && getCostoModulo() < 0D ){
			addRecoverableError(BaseError.MSG_VALORMENORQUECERO, RecError.TIPOOBRA_COSTOMODULO);
		}

		if (hasError()) {
			return false;
		}
		
		// Validaciones de unicidad del Tipo de Obra
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("desTipoObra");
		uniqueMap.addEntity("recurso");
		if (!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, RecError.TIPOOBRA_DESTIPOOBRA);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}

}
