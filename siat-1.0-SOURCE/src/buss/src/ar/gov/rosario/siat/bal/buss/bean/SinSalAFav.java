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
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a una tabla Auxiliar para Saldos a Favor.
 * Contiene todos los Saldos a Favor generados por pagos mayores a los importes correspondientes.
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_sinSalAFav")
public class SinSalAFav extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;

	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idTransaccion") 
	private Transaccion transaccion;
	
	@Column(name="sistema") 
	private Long sistema;

	@Column(name = "nroComprobante")
	private Long nroComprobante;
	
	@Column(name = "anioComprobante")
	private Long anioComprobante;

	@Column(name = "cuota")
	private Long cuota;
	
	@Column(name = "filler_o")
	private Long filler_o;
	
	@Column(name = "importePago")
	private Double importePago;

	@Column(name = "importeDebPag")
	private Double importeDebPag;

	@Column(name = "fechaPago")
	private Date fechaPago;

	@Column(name = "fechaBalance")
	private Date fechaBalance;

	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idPartida") 
	private Partida partida;

	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idCuenta") 
	private Cuenta cuenta;
	
	//Constructores 
	public SinSalAFav(){
		super();
	}

	// Getters Y Setters
	public Long getAnioComprobante() {
		return anioComprobante;
	}
	public void setAnioComprobante(Long anioComprobante) {
		this.anioComprobante = anioComprobante;
	}
	public Asentamiento getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(Asentamiento asentamiento) {
		this.asentamiento = asentamiento;
	}
	public Long getCuota() {
		return cuota;
	}
	public void setCuota(Long cuota) {
		this.cuota = cuota;
	}
	public Date getFechaBalance() {
		return fechaBalance;
	}
	public void setFechaBalance(Date fechaBalance) {
		this.fechaBalance = fechaBalance;
	}
	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}
	public Long getFiller_o() {
		return filler_o;
	}
	public void setFiller_o(Long filler_o) {
		this.filler_o = filler_o;
	}
	public Double getImporteDebPag() {
		return importeDebPag;
	}
	public void setImporteDebPag(Double importeDebPag) {
		this.importeDebPag = importeDebPag;
	}
	public Double getImportePago() {
		return importePago;
	}
	public void setImportePago(Double importePago) {
		this.importePago = importePago;
	}
	public Long getNroComprobante() {
		return nroComprobante;
	}
	public void setNroComprobante(Long nroComprobante) {
		this.nroComprobante = nroComprobante;
	}
	public Long getSistema() {
		return sistema;
	}
	public void setSistema(Long sistema) {
		this.sistema = sistema;
	}
	public Transaccion getTransaccion() {
		return transaccion;
	}
	public void setTransaccion(Transaccion transaccion) {
		this.transaccion = transaccion;
	}
	public Cuenta getCuenta() {
		return cuenta;
	}
	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	public Partida getPartida() {
		return partida;
	}
	public void setPartida(Partida partida) {
		this.partida = partida;
	}

	// Metodos de clase	
	public static SinSalAFav getById(Long id) {
		return (SinSalAFav) BalDAOFactory.getSinSalAFavDAO().getById(id);
	}
	
	public static SinSalAFav getByIdNull(Long id) {
		return (SinSalAFav) BalDAOFactory.getSinSalAFavDAO().getByIdNull(id);
	}
	
	public static List<SinSalAFav> getList() {
		return (ArrayList<SinSalAFav>) BalDAOFactory.getSinSalAFavDAO().getList();
	}
	
	public static List<SinSalAFav> getListActivos() {			
		return (ArrayList<SinSalAFav>) BalDAOFactory.getSinSalAFavDAO().getListActiva();
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
