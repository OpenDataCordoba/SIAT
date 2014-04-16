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
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Saldo A Favor temporales de Balance. 
 * (Al finalizar el proceso de balance se mueven a bal_saldoAFavor)
 * @author tecso
 */
@Entity
@Table(name = "bal_saldoBal")
public class SaldoBal extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "idBalance")
	private Balance balance;
	
	// atributos que siempre estan
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "idArea")
	private Area area = new Area();
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "idTipoOrigen")
	private TipoOrigen tipoOrigen;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "idCuenta")
	private Cuenta cuenta;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "fechaGeneracion")
	private Date fechaGeneracion;
	
	@Column(name = "importe")
	private Double importe;
	
	// atributos que estan para el caso de area
	@Column(name = "nroComprobante")
	private String nroComprobante;
	
	@Column(name = "desComprobante")
	private String desComprobante;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "idCuentaBanco")
	private CuentaBanco cuentaBanco;
	
	@Column(name = "idCaso")
	private String idCaso;
	
	
	// atributos que no sabemos si se usa
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "idEstSalAFav")
	private EstSalAFav estSalAFav;
	
	// atributos para el tipo asentamiento
	@Column(name = "idDeuda")
	private Long idDeuda = 0L;
	
	@Column(name = "idConvenioCuota")
	private Long idConvenioCuota = 0L;
	
	@Column(name = "fechapago")
	private Date fechaPago;
	
	@ManyToOne(optional=true,fetch=FetchType.LAZY) 
	@JoinColumn(name = "idPartida")
	private Partida partida = new Partida();
	
	@ManyToOne(optional=true,fetch=FetchType.LAZY) 
	@JoinColumn(name = "idAsentamiento")
	private Asentamiento asentamiento;
		
	// Constructores
	public SaldoBal(){
		super();
		// Seteo de valores default			
	}
	
	public SaldoBal(Long id){
		super();
		setId(id);
	}

	// Getters Y Setters
	public Area getArea() {
		return area;
	}
	
	public void setArea(Area area) {
		this.area = area;
	}
	
	public Asentamiento getAsentamiento() {
		return asentamiento;
	}
	
	public void setAsentamiento(Asentamiento asentamiento) {
		this.asentamiento = asentamiento;
	}
	
	public Cuenta getCuenta() {
		return cuenta;
	}
	
	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	
	public CuentaBanco getCuentaBanco() {
		return cuentaBanco;
	}
	
	public void setCuentaBanco(CuentaBanco cuentaBanco) {
		this.cuentaBanco = cuentaBanco;
	}
	
	public String getDesComprobante() {
		return desComprobante;
	}
	
	public void setDesComprobante(String desComprobante) {
		this.desComprobante = desComprobante;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public EstSalAFav getEstSalAFav() {
		return estSalAFav;
	}
	
	public void setEstSalAFav(EstSalAFav estSalAFav) {
		this.estSalAFav = estSalAFav;
	}
	
	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}
	
	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}
	
	public Date getFechaPago() {
		return fechaPago;
	}
	
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}
	
	public String getIdCaso() {
		return idCaso;
	}
	
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}
	
	public Long getIdConvenioCuota() {
		return idConvenioCuota;
	}
	
	public void setIdConvenioCuota(Long idConvenioCuota) {
		this.idConvenioCuota = idConvenioCuota;
	}
	
	public Long getIdDeuda() {
		return idDeuda;
	}
	
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}
	
	public Double getImporte() {
		return importe;
	}
	
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	
	public String getNroComprobante() {
		return nroComprobante;
	}
	
	public void setNroComprobante(String nroComprobante) {
		this.nroComprobante = nroComprobante;
	}
	
	public Partida getPartida() {
		return partida;
	}
	
	public void setPartida(Partida partida) {
		this.partida = partida;
	}
	
	public TipoOrigen getTipoOrigen() {
		return tipoOrigen;
	}
	
	public void setTipoOrigen(TipoOrigen tipoOrigen) {
		this.tipoOrigen = tipoOrigen;
	}
	
	public Balance getBalance() {
		return balance;
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
	}
	
	// Metodos de Clase
	public static SaldoBal getById(Long id) {
		return (SaldoBal) BalDAOFactory.getSaldoBalDAO().getById(id);
	}
	
	public static SaldoBal getByIdNull(Long id) {
		return (SaldoBal) BalDAOFactory.getSaldoBalDAO().getByIdNull(id);
	}
	
	public static List<SaldoBal> getList() {
		return (ArrayList<SaldoBal>) BalDAOFactory.getSaldoBalDAO().getList();
	}
	
	public static List<SaldoBal> getListActivos() {			
		return (ArrayList<SaldoBal>) BalDAOFactory.getSaldoBalDAO().getListActiva();
	}
		
	// Getters y setters
	
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
        	
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Devuelve un Saldo A Favor copia del SaldoBal.
	 * 
	 * @return
	 */
	public SaldoAFavor toSaldoAFavor(){
		SaldoAFavor saldoAFavor = new SaldoAFavor();
		
		saldoAFavor.setArea(this.getArea());
		saldoAFavor.setAsentamiento(this.getAsentamiento());
		saldoAFavor.setCompensacion(null);
		saldoAFavor.setCuenta(this.getCuenta());
		saldoAFavor.setCuentaBanco(this.getCuentaBanco());
		saldoAFavor.setDesComprobante(this.getDesComprobante());
		saldoAFavor.setDescripcion(this.getDescripcion());
		saldoAFavor.setEstSalAFav(this.getEstSalAFav());
		saldoAFavor.setFechaGeneracion(this.getFechaGeneracion());
		saldoAFavor.setFechaPago(this.getFechaPago());
		saldoAFavor.setIdConvenioCuota(this.getIdConvenioCuota());
		saldoAFavor.setIdDeuda(this.getIdDeuda());
		saldoAFavor.setImporte(this.getImporte());
		saldoAFavor.setNroComprobante(this.getNroComprobante());
		saldoAFavor.setPartida(this.getPartida());
		saldoAFavor.setTipoOrigen(this.getTipoOrigen());
		
		return saldoAFavor;
	}
	
}
