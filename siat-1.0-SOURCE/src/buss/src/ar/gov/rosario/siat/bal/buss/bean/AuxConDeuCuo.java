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
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioDeuda;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a una tabla Auxiliar que Relaciona Cuotas con Convenio Deuda.
 * Contiene la relación entre las deudas de la tabla bal_auxConDeu y las cuotas que se están asentando.
 * @author tecso
 */
@Entity
@Table(name = "bal_auxConDeuCuo")
public class AuxConDeuCuo extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idConvenioCuota") 
	private ConvenioCuota  convenioCuota;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idConvenioDeuda") 
	private ConvenioDeuda  convenioDeuda;
	
	@Column(name = "saldoEnPlanCub")
	private Double saldoEnPlanCub;
	
	@Column(name = "esPagoTotal")
	private Integer esPagoTotal;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idTransaccion") 
	private Transaccion transaccion;
	
	//Constructores 
	
	public AuxConDeuCuo(){
		super();
	}

	// Getters Y Setters
	
	public Asentamiento getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(Asentamiento asentamiento) {
		this.asentamiento = asentamiento;
	}
	public ConvenioCuota getConvenioCuota() {
		return convenioCuota;
	}
	public void setConvenioCuota(ConvenioCuota convenioCuota) {
		this.convenioCuota = convenioCuota;
	}
	public ConvenioDeuda getConvenioDeuda() {
		return convenioDeuda;
	}
	public void setConvenioDeuda(ConvenioDeuda convenioDeuda) {
		this.convenioDeuda = convenioDeuda;
	}
	public Integer getEsPagoTotal() {
		return esPagoTotal;
	}
	public void setEsPagoTotal(Integer esPagoTotal) {
		this.esPagoTotal = esPagoTotal;
	}
	public Double getSaldoEnPlanCub() {
		return saldoEnPlanCub;
	}
	public void setSaldoEnPlanCub(Double saldoEnPlanCub) {
		this.saldoEnPlanCub = saldoEnPlanCub;
	}
	public Transaccion getTransaccion() {
		return transaccion;
	}
	public void setTransaccion(Transaccion transaccion) {
		this.transaccion = transaccion;
	}

	// Metodos de clase	
	public static AuxConDeuCuo getById(Long id) {
		return (AuxConDeuCuo) BalDAOFactory.getAuxConDeuCuoDAO().getById(id);
	}
	
	public static AuxConDeuCuo getByIdNull(Long id) {
		return (AuxConDeuCuo) BalDAOFactory.getAuxConDeuCuoDAO().getByIdNull(id);
	}
		
	public static List<AuxConDeuCuo> getList() {
		return (ArrayList<AuxConDeuCuo>) BalDAOFactory.getAuxConDeuCuoDAO().getList();
	}
	
	public static List<AuxConDeuCuo> getListActivos() {			
		return (ArrayList<AuxConDeuCuo>) BalDAOFactory.getAuxConDeuCuoDAO().getListActiva();
	}
	
	/**
	 *  Obtiene la lista de registros de AuxConDeuCuo a asentar para la Transaccion.
	 *  
	 * @param asentamiento
	 * @param transaccion
	 * @return listAuxConDeuCuo
	 * @throws Exception
	 */
	public static List<AuxConDeuCuo> getListByAsentamientoYTransaccion(Asentamiento asentamiento, Transaccion transaccion) throws Exception {
		return BalDAOFactory.getAuxConDeuCuoDAO().getListByAsentamientoYTransaccion(asentamiento, transaccion);
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
