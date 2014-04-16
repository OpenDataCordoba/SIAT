//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.def.buss.bean.Banco;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente al auxiliar para aplicar pagos a cuenta
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_auxAplPagCue")
public class AuxAplPagCue extends BaseBO {

	private static final long serialVersionUID = 1L;
	@ManyToOne(optional=false ,fetch=FetchType.LAZY)
	@JoinColumn(name = "idConvenio")
	private Convenio convenio;
	
	@ManyToOne(optional=true ,fetch=FetchType.LAZY) 
	@JoinColumn(name="idResDet") 
	private ResDet resDet;
	
	@ManyToOne(optional=true ,fetch=FetchType.LAZY)
	@JoinColumn(name="idConvenioCuota") 
	private ConvenioCuota convenioCuota;
	
	@ManyToOne(optional=false ,fetch=FetchType.LAZY)
	@JoinColumn(name="idEstadoConCuo")
	private EstadoConCuo estadoConCuo;
	
	@Column(name = "idPago")
	private Long idPago;
	
	@ManyToOne(optional=true ,fetch=FetchType.LAZY)
	@JoinColumn(name = "idTipoPago")
	private TipoPago tipoPago;
		
	@Column(name= "fechaPago")
	private Date fechaPago;
	
	@ManyToOne(optional=true ,fetch=FetchType.LAZY)
	@JoinColumn(name="idBancoPago")
	private Banco bancoPago;
	
	@Column(name="importe")
	private Double importe;
	
	@Column(name="actualizacion")
	private Double actualizacion;
	
	@ManyToOne(optional=true ,fetch=FetchType.LAZY)
	@JoinColumn(name="idAsentamiento")
	private Asentamiento asentamiento;
	
	@Column(name="codPago")
	private Long codPago;
	
	// Constructores
	public AuxAplPagCue(){
		super();
	}
	public AuxAplPagCue(ConvenioCuota convenioCuota){
		super();
		this.convenioCuota = convenioCuota;
		this.convenio=convenioCuota.getConvenio();
		this.convenioCuota=convenioCuota;
		this.idPago=convenioCuota.getIdPago();
		this.tipoPago=convenioCuota.getTipoPago();
		this.fechaPago=convenioCuota.getFechaPago();
		this.bancoPago=convenioCuota.getBancoPago();
		this.importe=convenioCuota.getImporteCuota();
		this.actualizacion=convenioCuota.getActualizacion();
		this.asentamiento=convenioCuota.getAsentamiento();
		this.codPago=convenioCuota.getCodPago();
		this.estadoConCuo = convenioCuota.getEstadoConCuo();
	}

	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

	public ResDet getResDet() {
		return resDet;
	}

	public void setResDet(ResDet resDet) {
		this.resDet = resDet;
	}

	public ConvenioCuota getConvenioCuota() {
		return convenioCuota;
	}

	public void setConvenioCuota(ConvenioCuota convenioCuota) {
		this.convenioCuota = convenioCuota;
	}

	public Long getIdPago() {
		return idPago;
	}

	public void setIdPago(Long idPago) {
		this.idPago = idPago;
	}

	public TipoPago getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Banco getBancoPago() {
		return bancoPago;
	}

	public void setBancoPago(Banco bancoPago) {
		this.bancoPago = bancoPago;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

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
	
	public Long getCodPago() {
		return codPago;
	}

	public void setCodPago(Long codPago) {
		this.codPago = codPago;
	}
	
	public EstadoConCuo getEstadoConCuo() {
		return estadoConCuo;
	}
	
	public void setEstadoConCuo(EstadoConCuo estadoConCuo) {
		this.estadoConCuo = estadoConCuo;
	}

	// Metodos de clase



	public static AuxAplPagCue getById (Long id){
		
		AuxAplPagCue auxAplPagCue = (AuxAplPagCue)GdeDAOFactory.getAuxAplPagCueDAO().getById(id);
		return auxAplPagCue;
	}
	
	public static List<AuxAplPagCue> getListByIdResDet (Long idResDet){
		List<AuxAplPagCue> listAuxAplPagCue = GdeDAOFactory.getAuxAplPagCueDAO().getListByIdResDet(idResDet);
		
		return listAuxAplPagCue;
	}
	
	public static List<AuxAplPagCue> getListByIdConvenio (Long idConvenio){
		List<AuxAplPagCue> listAuxAplPagCue = GdeDAOFactory.getAuxAplPagCueDAO().getListByIdConvenio(idConvenio);
		
		return listAuxAplPagCue;
	}
	// Metodos de Instancia
	

	
}
