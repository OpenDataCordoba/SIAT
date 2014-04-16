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

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioDeuda;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a una tabla Auxiliar de Convenio Deuda.
 * Contiene todas las deudas involucradas en convenios, que se ven afectadas por el asentamiento de una cuota.
 * @author tecso
 */
@Entity
@Table(name = "bal_auxConDeu")
public class AuxConDeu extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idConvenioDeuda") 
	private ConvenioDeuda  convenioDeuda;
	
	@Column(name = "saldoEnPlan")
	private Double saldoEnPlan;
	
	@Column(name = "fechaPago")
	private Date fechaPago;

	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idTransaccion") 
	private Transaccion transaccion;
	
	//Constructores 
	
	public AuxConDeu(){
		super();
	}

	// Getters Y Setters
	
	public Asentamiento getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(Asentamiento asentamiento) {
		this.asentamiento = asentamiento;
	}
	public ConvenioDeuda getConvenioDeuda() {
		return convenioDeuda;
	}
	public void setConvenioDeuda(ConvenioDeuda convenioDeuda) {
		this.convenioDeuda = convenioDeuda;
	}
	public Double getSaldoEnPlan() {
		return saldoEnPlan;
	}
	public void setSaldoEnPlan(Double saldoEnPlan) {
		this.saldoEnPlan = saldoEnPlan;
	}
	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}
	public Transaccion getTransaccion() {
		return transaccion;
	}
	public void setTransaccion(Transaccion transaccion) {
		this.transaccion = transaccion;
	}

	// Metodos de clase	
	public static AuxConDeu getById(Long id) {
		return (AuxConDeu) BalDAOFactory.getAuxConDeuDAO().getById(id);
	}
	
	public static AuxConDeu getByIdNull(Long id) {
		return (AuxConDeu) BalDAOFactory.getAuxConDeuDAO().getByIdNull(id);
	}
	
	/**
	 *  Obtiene el registro de AuxConDeu con la Deuda a Cancelar. Buscando el registro para el asentamiento y convenio
	 *  pasados y con SaldoEnPlan mayor que cero.
	 * 
	 * @param asentamiento
	 * @param convenio
	 * @return auxConDeu
	 * @throws Exception
	 */
	public static AuxConDeu getDeudaACancelarByAsentamientoYConvenio(Asentamiento asentamiento, Convenio convenio) throws Exception {
		return (AuxConDeu) BalDAOFactory.getAuxConDeuDAO().getDeudaACancelarByAsentamientoYConvenio(asentamiento, convenio);
	}

	/**
	 *  Obtiene la lista de registros de AuxConDeu a asentar para la Transaccion.
	 *  
	 * @param asentamiento
	 * @param transaccion
	 * @return listAuxConDeu
	 * @throws Exception
	 */
	public static List<AuxConDeu> getListByAsentamientoYTransaccion(Asentamiento asentamiento, Transaccion transaccion) throws Exception {
		return BalDAOFactory.getAuxConDeuDAO().getListByAsentamientoYTransaccion(asentamiento, transaccion);
	}
	
	public static List<AuxConDeu> getList() {
		return (ArrayList<AuxConDeu>) BalDAOFactory.getAuxConDeuDAO().getList();
	}
	
	public static List<AuxConDeu> getListActivos() {			
		return (ArrayList<AuxConDeu>) BalDAOFactory.getAuxConDeuDAO().getListActiva();
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
		//Validaciones de Negocio
				
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
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
}
