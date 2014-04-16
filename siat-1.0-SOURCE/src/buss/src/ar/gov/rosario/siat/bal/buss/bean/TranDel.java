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
 * Bean correspondiente a Transaccion para Asentamiento Delegado
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_tranDel")
public class TranDel extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name="sistema") 
	private Long sistema;

	@Column(name = "nroComprobante")
	private Long nroComprobante;
	
	@Column(name = "anioComprobante")
	private Long anioComprobante;
	
	@Column(name = "periodo")
	private Long periodo;
	
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
    @JoinColumn(name="idAseDel") 
	private AseDel aseDel;
		
	@Column(name = "nroLinea")
	private Long nroLinea;
		
	
	//Constructores 
	public TranDel(){
		super();
	}

	// Getters y Setters
	
	public Long getAnioComprobante() {
		return anioComprobante;
	}
	public void setAnioComprobante(Long anioComprobante) {
		this.anioComprobante = anioComprobante;
	}
	public AseDel getAseDel() {
		return aseDel;
	}
	public void setAseDel(AseDel aseDel) {
		this.aseDel = aseDel;
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
	public Long getPaquete() {
		return paquete;
	}
	public void setPaquete(Long paquete) {
		this.paquete = paquete;
	}
	public Long getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
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
	public Long getNroLinea() {
		return nroLinea;
	}
	public void setNroLinea(Long nroLinea) {
		this.nroLinea = nroLinea;
	}

	// Metodos de clase	
	public static TranDel getById(Long id) {
		return (TranDel) BalDAOFactory.getTranDelDAO().getById(id);
	}
	
	public static TranDel getByIdNull(Long id) {
		return (TranDel) BalDAOFactory.getTranDelDAO().getByIdNull(id);
	}
	
	public static List<TranDel> getList() {
		return (ArrayList<TranDel>) BalDAOFactory.getTranDelDAO().getList();
	}
	
	public static List<TranDel> getListActivos() {			
		return (ArrayList<TranDel>) BalDAOFactory.getTranDelDAO().getListActiva();
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
