//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Caja7 (Ajustes a Partidas)
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_caja7")
public class Caja7 extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idBalance") 
	private Balance balance;

	@Column(name = "fecha")
	private Date fecha;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPartida") 
	private Partida partida;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "observacion")
	private String observacion;
		
	@Column(name = "importeEjeAct")
	private Double importeEjeAct;
	
	@Column(name = "importeEjeVen")
	private Double importeEjeVen;
	
	@Transient
	private Double importe;
	
	// Constructores
	public Caja7(){
		super();		
	}
	
	public Caja7(Long id){
		super();
		setId(id);
	}

	// Getters Y Setters
	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Partida getPartida() {
		return partida;
	}
	public void setPartida(Partida partida) {
		this.partida = partida;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Double getImporteEjeAct() {
		return importeEjeAct;
	}
	public void setImporteEjeAct(Double importeEjeAct) {
		this.importeEjeAct = importeEjeAct;
	}
	public Double getImporteEjeVen() {
		return importeEjeVen;
	}
	public void setImporteEjeVen(Double importeEjeVen) {
		this.importeEjeVen = importeEjeVen;
	}
	public Balance getBalance() {
		return balance;
	}
	public void setBalance(Balance balance) {
		this.balance = balance;
	}
	
	public Double getImporte() {
		
		if(this.getImporteEjeAct().equals(0D)){
			importe=this.getImporteEjeVen();
		}
		
		if(this.getImporteEjeVen().equals(0D)){
			importe=this.getImporteEjeAct();
		}
		
		return importe;
	}
	
	public void setImporte(Double importe) {
		this.importe = importe;
	}

	// Metodos de Clase
	public static Caja7 getById(Long id) {
		return (Caja7) BalDAOFactory.getCaja7DAO().getById(id);
	}
	
	public static Caja7 getByIdNull(Long id) {
		return (Caja7) BalDAOFactory.getCaja7DAO().getByIdNull(id);
	}
	
	public static List<Caja7> getList() {
		return (ArrayList<Caja7>) BalDAOFactory.getCaja7DAO().getList();
	}
	
	public static List<Caja7> getListActivos() {			
		return (ArrayList<Caja7>) BalDAOFactory.getCaja7DAO().getListActiva();
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
		
		//	Validaciones        
		if (this.getFecha()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.CAJA7_FECHA);
		}
		
		if (this.getImporteEjeAct()==null||this.getImporteEjeVen()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.CAJA7_IMPORTE);
		}
		
		if (this.getBalance()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.BALANCE_LABEL);
		}
		
		if (this.getPartida()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.CAJA7_PARTIDA);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el Caja7. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getCaja7DAO().update(this);
	}

	/**
	 * Desactiva el Caja7. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getCaja7DAO().update(this);
	}
	
	/**
	 * Valida la activacion del Caja7
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Caja7
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
}
