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
import javax.persistence.Transient;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a SinIndet (tabla de sincronismo para indeterminados)
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_sinIndet")
public class SinIndet extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	public static final String FILLER_AFIP = "A";
	// Antes usabamos "AFIP" en vez de "A", pero traia problemas porque ese campo 
	// era del tipo "char" en alguna de las tablas y se truncaba, lo cual traia problemas al comparar
	public static final Integer ID_TPO_INGRESO_AFIP = 5;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;
		
	@Column(name="sistema") 
	private Long sistema;

	@Column(name = "nroComprobante")
	private Long nroComprobante;
	
	@Column(name = "anioComprobante")
	private Long anioComprobante;
	
	@Column(name = "periodo")
	private Long periodo;

	@Transient
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
	
	@Column(name = "importeCobrado")
	private Double importeCobrado;
	
	@Column(name = "basico")
	private Double basico;
	
	@Column(name = "calculado")
	private Double calculado;
	
	@Column(name = "indice")
	private Double indice;
	
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
	
	@Column(name = "codIndeterminado")
	private String codIndeterminado;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idPartida") 
	private Partida partida;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idTipoIndet") 
	private TipoIndet tipoIndet;
	
	// Constructores 
	public SinIndet(){
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
	public Double getBasico() {
		return basico;
	}
	public void setBasico(Double basico) {
		this.basico = basico;
	}
	public Long getCaja() {
		return caja;
	}
	public void setCaja(Long caja) {
		this.caja = caja;
	}
	public Double getCalculado() {
		return calculado;
	}
	public void setCalculado(Double calculado) {
		this.calculado = calculado;
	}
	public String getCodIndeterminado() {
		return codIndeterminado;
	}
	public void setCodIndeterminado(String codIndeterminado) {
		this.codIndeterminado = codIndeterminado;
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
	public Partida getPartida() {
		return partida;
	}
	public void setPartida(Partida partida) {
		this.partida = partida;
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
	public TipoIndet getTipoIndet() {
		return tipoIndet;
	}
	public void setTipoIndet(TipoIndet tipoIndet) {
		this.tipoIndet = tipoIndet;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}

	// Metodos de clase	
	public static SinIndet getById(Long id) {
		return (SinIndet) BalDAOFactory.getSinIndetDAO().getById(id);
	}
	
	public static SinIndet getByIdNull(Long id) {
		return (SinIndet) BalDAOFactory.getSinIndetDAO().getByIdNull(id);
	}
	
	public static List<SinIndet> getList() {
		return (ArrayList<SinIndet>) BalDAOFactory.getSinIndetDAO().getList();
	}
	
	public static List<SinIndet> getListActivos() {			
		return (ArrayList<SinIndet>) BalDAOFactory.getSinIndetDAO().getListActiva();
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
