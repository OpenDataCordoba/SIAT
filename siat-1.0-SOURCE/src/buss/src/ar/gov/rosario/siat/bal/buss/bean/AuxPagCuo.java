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
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConCuo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboConvenio;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.ListUtil;

/**
 * Bean correspondiente a una tabla Auxiliar de Cuotas Pagas.
 * Contiene todas las Cuotas que pueden posteriormente asentarse como pagadas.
 * @author tecso
 */
@Entity
@Table(name = "bal_auxPagCuo")
public class AuxPagCuo extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idConvenioCuota") 
	private ConvenioCuota  convenioCuota;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idTransaccion") 
	private Transaccion transaccion;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idReciboConvenio") 
	private ReciboConvenio reciboConvenio;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idEstadoConCuo") 
	private EstadoConCuo estadoConCuo;
	
	@Column(name = "actualizacion")
	private Double actualizacion;
	
	@Column(name = "nroCuotaImputada")
	private Integer nroCuotaImputada;
	
	//Constructores 
	
	public AuxPagCuo(){
		super();
	}

	// Getters Y Setters
	
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
	public ConvenioCuota getConvenioCuota() {
		return convenioCuota;
	}
	public void setConvenioCuota(ConvenioCuota convenioCuota) {
		this.convenioCuota = convenioCuota;
	}
	public Integer getNroCuotaImputada() {
		return nroCuotaImputada;
	}
	public void setNroCuotaImputada(Integer nroCuotaImputada) {
		this.nroCuotaImputada = nroCuotaImputada;
	}
	public ReciboConvenio getReciboConvenio() {
		return reciboConvenio;
	}
	public void setReciboConvenio(ReciboConvenio reciboConvenio) {
		this.reciboConvenio = reciboConvenio;
	}
	public Transaccion getTransaccion() {
		return transaccion;
	}
	public void setTransaccion(Transaccion transaccion) {
		this.transaccion = transaccion;
	}
	public EstadoConCuo getEstadoConCuo() {
		return estadoConCuo;
	}
	public void setEstadoConCuo(EstadoConCuo estadoConCuo) {
		this.estadoConCuo = estadoConCuo;
	}

	// Metodos de clase	
	public static AuxPagCuo getById(Long id) {
		return (AuxPagCuo) BalDAOFactory.getAuxPagCuoDAO().getById(id);
	}
	
	public static AuxPagCuo getByIdNull(Long id) {
		return (AuxPagCuo) BalDAOFactory.getAuxPagCuoDAO().getByIdNull(id);
	}
	
	public static AuxPagCuo getByAsentamientoYReciboConvenio(Asentamiento asentamiento,ReciboConvenio reciboConvenio) {
		return (AuxPagCuo) BalDAOFactory.getAuxPagCuoDAO().getByAsentamientoYReciboConvenio(asentamiento,reciboConvenio);
	}
	
	public static AuxPagCuo getByAsentamientoYConvenioCuota(Asentamiento asentamiento,ConvenioCuota convenioCuota) {
		return (AuxPagCuo) BalDAOFactory.getAuxPagCuoDAO().getByAsentamientoYConvenioCuota(asentamiento,convenioCuota);
	}
	
	public static List<AuxPagCuo> getList() {
		return (ArrayList<AuxPagCuo>) BalDAOFactory.getAuxPagCuoDAO().getList();
	}
	
	public static List<AuxPagCuo> getListActivos() {			
		return (ArrayList<AuxPagCuo>) BalDAOFactory.getAuxPagCuoDAO().getListActiva();
	}
	
	/**
	 *  Obtiene la lista de registros de AuxPagCuo a asentar para la Transaccion.
	 *  
	 * @param asentamiento
	 * @param transaccion
	 * @return listAuxPagCuo
	 * @throws Exception
	 */
	public static List<AuxPagCuo> getListByAsentamientoYTransaccion(Asentamiento asentamiento, Transaccion transaccion) throws Exception {
		return BalDAOFactory.getAuxPagCuoDAO().getListByAsentamientoYTransaccion(asentamiento, transaccion);
	}
	
	/**
	 *  Obtiene el registros de AuxPagCuo a asentar para la Transaccion. (Caso de Transaccion de Deuda)
	 *  
	 * @param asentamiento
	 * @param transaccion
	 * @return auxPagCuo
	 * @throws Exception
	 */
	public static AuxPagCuo getByAsentamientoYTransaccion(Asentamiento asentamiento, Transaccion transaccion) throws Exception {
		List<AuxPagCuo> listAuxPagCuo = BalDAOFactory.getAuxPagCuoDAO().getListByAsentamientoYTransaccion(asentamiento, transaccion);
		if(ListUtil.isNullOrEmpty(listAuxPagCuo) || listAuxPagCuo.size() > 1){
			return null;
		}
		return listAuxPagCuo.get(0);
	}
	
	/**
	 *  Cantidad de Registros de AuxPagCuo correspondientes a Cuotas del Convenio pasado, considerando solo los
	 *  pagos buenos y si contar la cuota nro uno.
	 * 
	 * @param asentamiento
	 * @param convenio
	 * @return cantCuotas
	 * @throws Exception
	 */
	public static int getCantCuotas(Asentamiento asentamiento, Convenio convenio) throws Exception{
		return BalDAOFactory.getAuxPagCuoDAO().getCantCuotas(asentamiento, convenio);
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
