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
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Indeterminados temporales de Balance. 
 * (Al finalizar el proceso de balance graban en la base de Indeterminados usando el IndeterminadoFacade)
 * @author tecso
 */
@Entity
@Table(name = "bal_indetBal")
public class IndetBal extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idBalance") 
	private Balance balance;

	@Column(name = "sistema")
	private String sistema;
	
	@Column(name = "nroComprobante")
	private String nroComprobante;
	
	@Column(name = "clave")
	private String clave;
	
	@Column(name = "resto")
	private String resto;
	
	@Column(name = "importeCobrado")
	private Double importeCobrado;

	@Column(name = "importeBasico")
	private Double importeBasico;

	@Column(name = "importeCalculado")
	private Double importeCalculado;

	@Column(name = "indice")
	private Double indice;

	@Column(name = "recargo")
	private Double recargo;

	@Column(name = "partida")
	private String partida;

	@Column(name = "codIndet")
	private Integer codIndet;

	@Column(name = "fechaPago")
	private Date fechaPago;

	@Column(name = "caja")
	private Integer caja;

	@Column(name = "paquete")
	private Integer paquete;

	@Column(name = "codPago")
	private Integer codPago;

	@Column(name = "fechaBalance")
	private Date fechaBalance;

	@Column(name = "filler")
	private String filler;

	@Column(name = "reciboTr")
	private Long reciboTr;

	@Column(name = "pasoBalance")
	private Integer pasoBalance;

	// Constructores
	public IndetBal(){
		super();		
	}

	// Getters Y Setters 
	public Integer getCaja() {
		return caja;
	}

	public void setCaja(Integer caja) {
		this.caja = caja;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Integer getCodIndet() {
		return codIndet;
	}

	public void setCodIndet(Integer codIndet) {
		this.codIndet = codIndet;
	}

	public Integer getCodPago() {
		return codPago;
	}

	public void setCodPago(Integer codPago) {
		this.codPago = codPago;
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

	public Double getImporteBasico() {
		return importeBasico;
	}

	public void setImporteBasico(Double importeBasico) {
		this.importeBasico = importeBasico;
	}

	public Double getImporteCalculado() {
		return importeCalculado;
	}

	public void setImporteCalculado(Double importeCalculado) {
		this.importeCalculado = importeCalculado;
	}

	public Double getImporteCobrado() {
		return importeCobrado;
	}

	public void setImporteCobrado(Double importeCobrado) {
		this.importeCobrado = importeCobrado;
	}

	public Double getIndice() {
		return indice;
	}

	public void setIndice(Double indice) {
		this.indice = indice;
	}

	public String getNroComprobante() {
		return nroComprobante;
	}

	public void setNroComprobante(String nroComprobante) {
		this.nroComprobante = nroComprobante;
	}

	public Integer getPaquete() {
		return paquete;
	}

	public void setPaquete(Integer paquete) {
		this.paquete = paquete;
	}

	public String getPartida() {
		return partida;
	}

	public void setPartida(String partida) {
		this.partida = partida;
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

	public String getResto() {
		return resto;
	}

	public void setResto(String resto) {
		this.resto = resto;
	}

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	
	public Balance getBalance() {
		return balance;
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
	}

	public Integer getPasoBalance() {
		return pasoBalance;
	}

	public void setPasoBalance(Integer pasoBalance) {
		this.pasoBalance = pasoBalance;
	}

	// Metodos de Clase
	public static IndetBal getById(Long id) {
		return (IndetBal) BalDAOFactory.getIndetBalDAO().getById(id);
	}
	
	public static IndetBal getByIdNull(Long id) {
		return (IndetBal) BalDAOFactory.getIndetBalDAO().getByIdNull(id);
	}
	
	public static List<IndetBal> getList() {
		return (ArrayList<IndetBal>) BalDAOFactory.getIndetBalDAO().getList();
	}
	
	public static List<IndetBal> getListActivos() {			
		return (ArrayList<IndetBal>) BalDAOFactory.getIndetBalDAO().getListActiva();
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
	 * Devuelve un IndetVO copia del IndetBal para grabarlo usando el Facade.
	 * 
	 * @return
	 */
	public IndetVO toIndetVO(){
		IndetVO indet = new IndetVO();
		
	
		
		return indet;
	}
	
	
}
