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
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.ListUtil;

/**
 * Bean correspondiente a una tabla Auxiliar de Deuda Pagas.
 * Contiene todas las Deudas que pueden posteriormente asentarse como pagadas.
 * @author tecso
 */
@Entity
@Table(name = "bal_auxPagDeu")
public class AuxPagDeu extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;
	
	@Column(name = "idDeuda")
	private Long  idDeuda;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idTransaccion") 
	private Transaccion transaccion;
	
	@Column(name = "actualizacion")
	private Double actualizacion;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idRecibo") 
	private Recibo recibo;
	
	//Constructores 
	
	public AuxPagDeu(){
		super();
	}

	// Getters y Setters
	public Double getActualizacion() {
		return actualizacion;
	}
	public void setActualizacion(Double actualizacion) {
		this.actualizacion = actualizacion;
	}
	public Asentamiento getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(Asentamiento asentamiento) {
		this.asentamiento = asentamiento;
	}
	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}
	public Recibo getRecibo() {
		return recibo;
	}
	public void setRecibo(Recibo recibo) {
		this.recibo = recibo;
	}
	public Transaccion getTransaccion() {
		return transaccion;
	}
	public void setTransaccion(Transaccion transaccion) {
		this.transaccion = transaccion;
	}
	
	// Metodos de clase	
	public static AuxPagDeu getById(Long id) {
		return (AuxPagDeu) BalDAOFactory.getAuxPagDeuDAO().getById(id);
	}
	
	public static AuxPagDeu getByIdNull(Long id) {
		return (AuxPagDeu) BalDAOFactory.getAuxPagDeuDAO().getByIdNull(id);
	}
	
	public static AuxPagDeu getByAsentamientoYDeuda(Asentamiento asentamiento,Deuda deuda) {
		return (AuxPagDeu) BalDAOFactory.getAuxPagDeuDAO().getByAsentamientoYDeuda(asentamiento,deuda);
	}
	
	@Deprecated
	public static AuxPagDeu getByAsentamientoYRecibo(Asentamiento asentamiento,Recibo recibo) {
		return (AuxPagDeu) BalDAOFactory.getAuxPagDeuDAO().getByAsentamientoYRecibo(asentamiento,recibo);
	}
	
	public static List<AuxPagDeu> getList() {
		return (ArrayList<AuxPagDeu>) BalDAOFactory.getAuxPagDeuDAO().getList();
	}
	
	public static List<AuxPagDeu> getListActivos() {			
		return (ArrayList<AuxPagDeu>) BalDAOFactory.getAuxPagDeuDAO().getListActiva();
	}

	/**
	 *  Obtiene la lista de registros de AuxPagDeu a asentar para la Transaccion. (Caso de Transaccion de Recibo)
	 *  
	 * @param asentamiento
	 * @param transaccion
	 * @return listAuxPagDeu
	 * @throws Exception
	 */
	public static List<AuxPagDeu> getListByAsentamientoYTransaccion(Asentamiento asentamiento, Transaccion transaccion) throws Exception {
		return BalDAOFactory.getAuxPagDeuDAO().getListByAsentamientoYTransaccion(asentamiento, transaccion);
	}
	
	/**
	 *  Obtiene el registros de AuxPagDeu a asentar para la Transaccion. (Caso de Transaccion de Deuda)
	 *  
	 * @param asentamiento
	 * @param transaccion
	 * @return auxPagDeu
	 * @throws Exception
	 */
	public static AuxPagDeu getByAsentamientoYTransaccion(Asentamiento asentamiento, Transaccion transaccion) throws Exception {
		List<AuxPagDeu> listAuxPagDeu = BalDAOFactory.getAuxPagDeuDAO().getListByAsentamientoYTransaccion(asentamiento, transaccion);
		if(ListUtil.isNullOrEmpty(listAuxPagDeu) || listAuxPagDeu.size() > 1){
			return null;
		}
		return listAuxPagDeu.get(0);
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
