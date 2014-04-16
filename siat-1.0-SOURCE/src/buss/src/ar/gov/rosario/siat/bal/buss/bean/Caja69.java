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
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Caja69 (Transacciones por Indeterminados y Duplices, Transacciones Manuales)
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_caja69")
public class Caja69 extends BaseBO {

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

	@Column(name = "tipoCancelacion")
	private Integer tipoCancelacion;
	
	//Constructores 
	public Caja69(){
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
	public Integer getTipoCancelacion() {
		return tipoCancelacion;
	}
	public void setTipoCancelacion(Integer tipoCancelacion) {
		this.tipoCancelacion = tipoCancelacion;
	}

	// Metodos de Clase
	public static Caja69 getById(Long id) {
		return (Caja69) BalDAOFactory.getCaja69DAO().getById(id);
	}
	
	public static Caja69 getByIdNull(Long id) {
		return (Caja69) BalDAOFactory.getCaja69DAO().getByIdNull(id);
	}
	
	public static List<Caja69> getList() {
		return (ArrayList<Caja69>) BalDAOFactory.getCaja69DAO().getList();
	}
	
	public static List<Caja69> getListActivos() {			
		return (ArrayList<Caja69>) BalDAOFactory.getCaja69DAO().getListActiva();
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
       
		if (this.getSistema()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_SISTEMA);
		}
		if (this.getClave()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_CLAVE);
		}
		if (this.getFechaPago()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_FECHAPAGO);
		}
		if (this.getNroComprobante()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_NROCOMPROBANTE);
		}
		if (this.getFechaBalance()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_FECHABALANCE);
		}
		if (this.getResto()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_RESTO);
		}
		if (this.getImporte()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_IMPORTECOBRADO);
		}
		if (this.getCaja()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_CAJA);
		}

		if (hasError()) {
			return false;
		}
		
		return true;
	}

	
}
