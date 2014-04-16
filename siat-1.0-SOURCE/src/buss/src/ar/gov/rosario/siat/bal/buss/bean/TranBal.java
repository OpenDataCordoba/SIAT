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
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a TranBal (Transacciones obtenidas de Archivos de Banco incluidos en Balance)
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_tranBal")
public class TranBal extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name="sistema") 
	private Long sistema;

	@Column(name = "nroComprobante")
	private Long nroComprobante;
	
	@Column(name = "clave")
	private String clave;
		
	@Column(name = "resto")
	private Long resto;
	
	@Column(name = "codPago")
	private Long codPago;
	
	@Column(name = "caja")
	private Long caja;
	
	@Column(name = "codTr")
	private Long codTr;
	
	@Column(name = "fechaPago")
	private Date fechaPago;
	
	@Column(name = "importe")
	private Double importe;
	
	@Column(name = "recargo")
	private Double recargo;
	
	@Column(name = "filler")
	private String filler;
	
	@Column(name = "paquete")
	private Long paquete;

	@Column(name = "marcaTr")
	private Long marcaTr;
	
	@Column(name = "reciboTr")
	private Long reciboTr;
	
	@Column(name = "fechaBalance")
	private Date fechaBalance;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idBalance") 
	private Balance balance;
		
	@Column(name = "nroLinea")
	private Long nroLinea;

	@Column(name = "strLinea")
	private String strLinea;
	
	@Column(name = "origen")
	private Integer origen;
	
	@Column(name="formulario")
	private Integer formulario;

	@Column(name="idTranAfip")
	private Long idTranAfip;

	//Constructores 
	public TranBal(){
		super();
	}

	// Getters Y Setters
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public Balance getBalance() {
		return balance;
	}
	public void setBalance(Balance balance) {
		this.balance = balance;
	}
	public Long getCaja() {
		return caja;
	}
	public void setCaja(Long caja) {
		this.caja = caja;
	}
	public Long getCodPago() {
		return codPago;
	}
	public void setCodPago(Long codPago) {
		this.codPago = codPago;
	}
	public Long getCodTr() {
		return codTr;
	}
	public void setCodTr(Long codTr) {
		this.codTr = codTr;
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
	public String getFiller() {
		return filler;
	}
	public void setFiller(String filler) {
		this.filler = filler;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	public Long getMarcaTr() {
		return marcaTr;
	}
	public void setMarcaTr(Long marcaTr) {
		this.marcaTr = marcaTr;
	}
	public Long getNroComprobante() {
		return nroComprobante;
	}
	public void setNroComprobante(Long nroComprobante) {
		this.nroComprobante = nroComprobante;
	}
	public Long getNroLinea() {
		return nroLinea;
	}
	public void setNroLinea(Long nroLinea) {
		this.nroLinea = nroLinea;
	}
	public Long getPaquete() {
		return paquete;
	}
	public void setPaquete(Long paquete) {
		this.paquete = paquete;
	}
	public Double getRecargo() {
		return recargo;
	}
	public void setRecargo(Double recargo) {
		this.recargo = recargo;
	}
	public Long getReciboTr() {
		return reciboTr;
	}
	public void setReciboTr(Long reciboTr) {
		this.reciboTr = reciboTr;
	}
	public Long getResto() {
		return resto;
	}
	public void setResto(Long resto) {
		this.resto = resto;
	}
	public Long getSistema() {
		return sistema;
	}
	public void setSistema(Long sistema) {
		this.sistema = sistema;
	}
	public String getStrLinea() {
		return strLinea;
	}
	public void setStrLinea(String strLinea) {
		this.strLinea = strLinea;
	}
	public Integer getOrigen() {
		return origen;
	}
	public void setOrigen(Integer origen) {
		this.origen = origen;
	}
	public Integer getFormulario() {
		return formulario;
	}
	public void setFormulario(Integer formulario) {
		this.formulario = formulario;
	}
	public void setIdTranAfip(Long idTranAfip) {
		this.idTranAfip = idTranAfip;
	}
	public Long getIdTranAfip() {
		return idTranAfip;
	}

	// Metodos de Clase
	public static TranBal getById(Long id) {
		return (TranBal) BalDAOFactory.getTranBalDAO().getById(id);
	}
	
	public static TranBal getByIdNull(Long id) {
		return (TranBal) BalDAOFactory.getTranBalDAO().getByIdNull(id);
	}
	
	public static List<TranBal> getList() {
		return (ArrayList<TranBal>) BalDAOFactory.getTranBalDAO().getList();
	}
	
	public static List<TranBal> getListActivos() {			
		return (ArrayList<TranBal>) BalDAOFactory.getTranBalDAO().getListActiva();
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
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
}
