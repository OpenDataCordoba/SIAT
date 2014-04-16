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
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean que representa la relación entre cada cuota paga y la deuda original. 
 * (puede ser por pagos totales o parciales)(solo para pagos buenos, no pagos a cuenta)
 * @author tecso
 */
@Entity
@Table(name = "gde_conDeuCuo")
public class ConDeuCuo extends BaseBO {

	private static final long serialVersionUID = 1L;
	
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
	
	@Column(name = "fechaPago")
	private Date fechaPago;
	
	//Constructores 
	
	public ConDeuCuo(){
		super();
	}

	// Getters Y Setters
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
	
	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	// Metodos de Clase
	public static ConDeuCuo getById(Long id) {
		return (ConDeuCuo) GdeDAOFactory.getConDeuCuoDAO().getById(id);
	}
	
	public static ConDeuCuo getByIdNull(Long id) {
		return (ConDeuCuo) GdeDAOFactory.getConDeuCuoDAO().getByIdNull(id);
	}
	
	public static List<ConDeuCuo> getList() {
		return (ArrayList<ConDeuCuo>) GdeDAOFactory.getConDeuCuoDAO().getList();
	}
	
	public static List<ConDeuCuo> getListActivos() {			
		return (ArrayList<ConDeuCuo>) GdeDAOFactory.getConDeuCuoDAO().getListActiva();
	}
	public static Boolean getEsPagoTotalByIdConvenioDeuda (Long idConvenioDeuda)throws Exception{
		return GdeDAOFactory.getConDeuCuoDAO().getEsPagoTotal(idConvenioDeuda);
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


	/**
	 * Obtiene el conDeuCuo con menor idConvenioDeuda, para el convenioCuota pasado como parametro
	 * @param id
	 * @return
	 */
	public static ConDeuCuo getConMenorConvenioDeuda(Long idConvenioCuota) {
		return GdeDAOFactory.getConDeuCuoDAO().getConMenorConvenioDeuda(idConvenioCuota);
	}
	
	public static List<ConDeuCuo> getConDeuCuoByConvenioCuota (ConvenioCuota convenioCuota) throws Exception{
		return GdeDAOFactory.getConDeuCuoDAO().getConDeuCuoByConvenioCuota(convenioCuota);
	}
	
}
