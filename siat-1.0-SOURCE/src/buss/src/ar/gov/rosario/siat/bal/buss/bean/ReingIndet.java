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
 * Bean correspondiente a Reingresos de Indeterminados
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_reingIndet")
public class ReingIndet extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name="sistema") 
	private Long sistema;

	@Column(name = "nroComprobante")
	private Long nroComprobante;
	
	@Column(name = "clave")
	private String clave;

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
		
	@Column(name = "fechaPago")
	private Date fechaPago;
	
	@Column(name = "importe")
	private Double importe;
	
	@Column(name = "importeBasico")
	private Double importeBasico;
	
	@Column(name = "importeCalculado")
	private Double importeCalculado;
	
	@Column(name = "recargo")
	private Double recargo;

	@Column(name = "indice")
	private Double indice;
		
	@Column(name = "reciboTr")
	private Long reciboTr;
	
	@Column(name = "fechaBalance")
	private Date fechaBalance;
	
	@Column(name = "fechaReingreso")
	private Date fechaReingreso;
	
	@Column(name = "idOrigen")
	private Long idOrigen;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;
		
	@Column(name = "idTranOriginal")
	private Long idTranOriginal;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idPartida") 
	private Partida partida;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoIndet") 
	private TipoIndet tipoIndet;
	
	@Column(name = "nroRedoOrigen")
	private String nroRedoOrigen;

	//Constructores 
	public ReingIndet(){
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

	public Long getCaja() {
		return caja;
	}

	public void setCaja(Long caja) {
		this.caja = caja;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Long getCodPago() {
		return codPago;
	}

	public void setCodPago(Long codPago) {
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

	public Long getIdTranOriginal() {
		return idTranOriginal;
	}

	public void setIdTranOriginal(Long idTranOriginal) {
		this.idTranOriginal = idTranOriginal;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Double getImporteCalculado() {
		return importeCalculado;
	}

	public void setImporteCalculado(Double importeCalculado) {
		this.importeCalculado = importeCalculado;
	}

	public Long getNroComprobante() {
		return nroComprobante;
	}

	public void setNroComprobante(Long nroComprobante) {
		this.nroComprobante = nroComprobante;
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
	
	public Date getFechaReingreso() {
		return fechaReingreso;
	}

	public void setFechaReingreso(Date fechaReingreso) {
		this.fechaReingreso = fechaReingreso;
	}

	public Double getImporteBasico() {
		return importeBasico;
	}

	public void setImporteBasico(Double importeBasico) {
		this.importeBasico = importeBasico;
	}

	public Double getIndice() {
		return indice;
	}

	public void setIndice(Double indice) {
		this.indice = indice;
	}
	
	public Long getIdOrigen() {
		return idOrigen;
	}

	public void setIdOrigen(Long idOrigen) {
		this.idOrigen = idOrigen;
	}

	public String getNroRedoOrigen() {
		return nroRedoOrigen;
	}

	public void setNroRedoOrigen(String nroRedoOrigen) {
		this.nroRedoOrigen = nroRedoOrigen;
	}

	// Metodos de clase	
	public static ReingIndet getById(Long id) {
		return (ReingIndet) BalDAOFactory.getReingIndetDAO().getById(id);
	}
	
	public static ReingIndet getByIdNull(Long id) {
		return (ReingIndet) BalDAOFactory.getReingIndetDAO().getByIdNull(id);
	}
	
	public static List<ReingIndet> getList() {
		return (ArrayList<ReingIndet>) BalDAOFactory.getReingIndetDAO().getList();
	}
	
	public static List<ReingIndet> getListActivos() {			
		return (ArrayList<ReingIndet>) BalDAOFactory.getReingIndetDAO().getListActiva();
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
	 * Prepara un IndetVO con los datos del Reingreso
	 * 
	 * @return
	 */
	public IndetVO toIndetVO(){
		IndetVO indetVO = new IndetVO();
		
		indetVO.setId(this.getId());
		indetVO.setNroIndeterminado(this.getIdOrigen());
		indetVO.setSistema(this.getSistema().toString());
		indetVO.setNroComprobante(this.getNroComprobante().toString());
		indetVO.setClave(this.getClave());
		indetVO.setResto(this.getResto().toString());
		indetVO.setImporteCobrado(this.getImporte());
		indetVO.setImporteBasico(this.getImporteBasico());
		indetVO.setImporteCalculado(this.getImporteCalculado());
		indetVO.setIndice(this.getIndice());
		indetVO.setRecargo(this.getRecargo());
		if(this.getPartida() != null)
			indetVO.setPartida(this.getPartida().getCodPartida());
		else
			indetVO.setPartida("");
		if(this.getTipoIndet() != null)
			indetVO.setCodIndet(Integer.valueOf(this.getTipoIndet().getCodTipoIndet()));
		else
			indetVO.setCodIndet(0);
		indetVO.setFechaPago(this.getFechaPago());
		indetVO.setCaja(this.getCaja().intValue());
		indetVO.setPaquete(0);
		indetVO.setCodPago(this.getCodPago().intValue());
		indetVO.setFechaBalance(this.getFechaBalance());
		indetVO.setCodTr(0L);
		indetVO.setFiller("");
		indetVO.setReciboTr(this.getReciboTr());
		indetVO.setTipoIngreso(0);
		indetVO.setUsuario(this.getUsuario());
		indetVO.setFechaHora(this.getFechaUltMdf());
		
		indetVO.setNroReing(this.getId());
		indetVO.setFechaReing(this.getFechaReingreso());
		
		return indetVO;
	}
}
