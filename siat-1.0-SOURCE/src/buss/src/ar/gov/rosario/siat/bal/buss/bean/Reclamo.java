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
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Banco;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboConvenio;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.TipoBoleta;


/**
 * Bean correspondiente a Reclamo
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_reclamo")
public class Reclamo extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "nroCuenta")
	private Long nroCuenta;

	@Column(name = "tipoBoleta")
	private Long tipoBoleta;

	@Column(name = "periodo")
	private Long periodo;

	@Column(name = "anio")
	private Long anio;

	@Column(name = "nroConvenio")
	private Long nroConvenio;

	@Column(name = "nroCuota")
	private Long nroCuota;

	@Column(name = "nroRecibo")
	private Long nroRecibo;

	@Column(name = "fechaPago")
	private Date fechaPago;

	@Column(name = "fechaAlta")
	private Date fechaAlta;

	@Column(name = "importePagado")
	private Double importePagado;

	@Column(name = "observacion")
	private String observacion;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "apellido")
	private String apellido;

	@Column(name = "tipoDoc")
	private Long tipoDoc;

	@Column(name = "nroDoc")
	private Long nroDoc;

	@Column(name = "telefono")
	private String telefono;

	@Column(name = "correoelectronico")
	private String correoElectronico;
	
	@Column(name = "idElemento")
	private Long idElemento;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "idBanco")
	private Banco banco;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "idViaDeuda")
	private ViaDeuda viaDeuda;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "idProcurador")
	private Procurador procurador;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "idEstadoReclamo")
	private EstadoReclamo estadoReclamo;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idRecurso") 
	private Recurso recurso;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idCuenta") 
	private Cuenta cuenta;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idSistema") 
	private Sistema sistema;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idArea") 
	private Area area;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idCanal") 
	private Canal canal;
	
	@Column(name="respuesta") 
	private String respuesta;

	@Column(name="usuarioAlta")
	private String usuarioAlta;

	@Transient
	private List<Recibo> listReciboDeuda; //lista de recibos de deuda donde esta aparece esta deuda

	@Transient
	private List<ReciboConvenio> listReciboConvenio; //lista de recibos de convenio donde aparace esta cuota
	
	@Transient
	private Deuda deudaReclamada;
	
	@Transient
	private ConvenioCuota cuotaReclamada;
	
	@Transient 
	private Boolean esMigrada;
	
	@Transient 
	private Boolean esDeuda;
	
	@Transient 
	private Boolean esCuota;

	@Transient 
	private Boolean tieneRecibo;

	@Transient
	private String infoUsuarioAlta;
	
	// Constructores
	public Reclamo(){
		super();
		// Seteo de valores default			
	}

	public Reclamo(Long id){
		super();
		setId(id);
	}

	// Metodos de Clase
	public static Reclamo getById(Long id) {
		return (Reclamo) BalDAOFactory.getReclamoDAO().getById(id);
	}

	public static Reclamo getByIdNull(Long id) {
		return (Reclamo) BalDAOFactory.getReclamoDAO().getByIdNull(id);
	}

	public static List<Reclamo> getList() {
		return (List<Reclamo>) BalDAOFactory.getReclamoDAO().getList();
	}

	public static List<Reclamo> getListActivos() {			
		return (List<Reclamo>) BalDAOFactory.getReclamoDAO().getListActiva();
	}


	// Getters y setters
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Long getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuenta(Long nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public Long getTipoBoleta() {
		return tipoBoleta;
	}

	public void setTipoBoleta(Long tipoBoleta) {
		this.tipoBoleta = tipoBoleta;
	}

	public Long getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
	}

	public Long getAnio() {
		return anio;
	}

	public void setAnio(Long anio) {
		this.anio = anio;
	}

	public Long getNroCuota() {
		return nroCuota;
	}

	public void setNroCuota(Long nroCuota) {
		this.nroCuota = nroCuota;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Double getImportePagado() {
		return importePagado;
	}

	public void setImportePagado(Double importePagado) {
		this.importePagado = importePagado;
	}


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Long getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(Long tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	public Long getNroDoc() {
		return nroDoc;
	}

	public void setNroDoc(Long nroDoc) {
		this.nroDoc = nroDoc;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Canal getCanal() {
		return canal;
	}

	public void setCanal(Canal canal) {
		this.canal = canal;
	}

	public Long getNroRecibo() {
		return nroRecibo;
	}

	public void setNroRecibo(Long nroRecibo) {
		this.nroRecibo = nroRecibo;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public void setNroConvenio(Long nroConvenio) {
		this.nroConvenio = nroConvenio;
	}

	public Long getNroConvenio() {
		return nroConvenio;
	}
	
	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}

		//que no exista un reclamo ya realizado sobre esta deuda o cuota
		List<Reclamo> reclamos = BalDAOFactory.getReclamoDAO().getByIdElemento(getIdElemento(), getTipoBoleta());
		if (!reclamos.isEmpty()) {
			this.addMessageValue("El reclamo ya esta registrado. No necesita enviarlo nuevamente.");
			return false;
		}

		// Validaciones de Negocio
		if (this.getTipoBoleta().intValue() == TipoBoleta.TIPODEUDA.getId()) {
			Deuda deudaReclamada = Deuda.getById(this.getIdElemento());
			// que no haya un asentamiento corriendo
			ArrayList<Long> listIdDeudas = new ArrayList<Long>();
			listIdDeudas.add(deudaReclamada.getId());			
			List<Long>listIdEnAsentam=Deuda.getListIdDeudaAuxPagDeu(listIdDeudas);
			if(!ListUtil.isNullOrEmpty(listIdEnAsentam)){
				String msg;
				msg = "El per\u00EDodo " + deudaReclamada.getStrPeriodo();
				msg += " se encuentra en proceso de asentamiento de pagos. Por favor vuelva a verificar su pago más tarde.";
				this.addMessageValue(msg);
			}
		} else if (this.getTipoBoleta().intValue() == TipoBoleta.TIPOCUOTA.getId()) {
			ConvenioCuota cuotaReclamada = ConvenioCuota.getById(this.getIdElemento());
			Convenio convenio = cuotaReclamada.getConvenio();
			List<Long> idCuotasEnAsent = convenio.getIdCuotasEnProcesoAsentamiento();
			if (!ListUtil.isNullOrEmpty(idCuotasEnAsent)){
				String msg;
				msg = "La cuota " + cuotaReclamada.getNumeroCuota();
				msg += " se encuentra en proceso de asentamiento de pagos. Por favor vuelva a verificar su pago más tarde.";
				this.addMessageValue(msg);
			}
		}
		
		if (hasError() || hasMessage()) {
			return false;
		}
		
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
		if (getRecurso()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.RECLAMO_DESRECURSO_LABEL );
		}

		if (getFechaPago()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.RECLAMO_FECHAPAGO_LABEL);
		}

		//mayor que cero
		if (getImportePagado() != null && getImportePagado().doubleValue() < 0)
			addRecoverableError(GdeError.LIQRECLAMO_IMPORTEPAGADO_NEGATIVO_ERROR);

		//banco requerido
		if (getBanco() == null) {
			addRecoverableError(GdeError.LIQRECLAMO_BANCO_REQUIRED);
		}

		//nombre
		if (StringUtil.isNullOrEmpty(getNombre())) {
			addRecoverableError(GdeError.LIQRECLAMO_NOMBRE_REQUIRED);
		}

		//apellido
		if (StringUtil.isNullOrEmpty(getApellido())) {
			addRecoverableError(GdeError.LIQRECLAMO_APELLIDO_REQUIRED);			 
		}
		
		//nrodoc
		if (getNroDoc() == null) {
			addRecoverableError(GdeError.LIQRECLAMO_NRODOC_REQUIRED); 
		}
		
		//email requerido
		if (StringUtil.isNullOrEmpty(getCorreoElectronico())) {
			addRecoverableError(GdeError.LIQRECLAMO_EMAIL_REQUIRED);
		}
		
		// Validamos que la fecha de pago sea menor a la de ultimo asentamiento
		// No importa que otro errores tenga, mostramos solo este como excluyente para registrar el reclamo
		Date fechaUltAsentamiento = getRecurso().getFecUltPag();
		Date fechaPago = getFechaPago();
		if (fechaPago != null && fechaUltAsentamiento != null &&
			DateUtil.isDateAfter(fechaPago, fechaUltAsentamiento)) {
			clearError();
			addRecoverableError(GdeError.LIQRECLAMO_DEUDA_FECHAPAGO_MENORASENT_ERROR);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el Reclamo. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getReclamoDAO().update(this);
	}

	/**
	 * Desactiva el Reclamo. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getReclamoDAO().update(this);
	}

	/**
	 * Valida la activacion del Reclamo
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();

		//Validaciones 
		return true;
	}

	/**
	 * Valida la desactivacion del Reclamo
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();

		//Validaciones 
		return true;
	}

	public EstadoReclamo getEstadoReclamo() {
		return estadoReclamo;
	}

	public void setEstadoReclamo(EstadoReclamo estadoReclamo) {
		this.estadoReclamo = estadoReclamo;
	}

	public Procurador getProcurador() {
		return procurador;
	}

	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}

	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getDesTipoBoleta(){
		String rta;
		if(nroCuenta == null) {
			return " ";
		}
		
		rta = TipoBoleta.getById(this.getTipoBoleta().intValue()).getValue();
		return rta;
	}

	public Long getIdElemento() {
		return idElemento;
	}

	public void setIdElemento(Long idElemento) {
		this.idElemento = idElemento;
	}
		
	/** Ahora llenamos los datos del ReclamoVO, con los datos extras
	 * utiles para averiguar la causa del reclamo y que se pueda buscar en indeterminados
	 */
	public void loadDatosExtra() throws Exception {
		Long codRefPag = null;
		
		if (getTipoBoleta().intValue() == TipoBoleta.TIPODEUDA.getId().intValue()) {
			this.deudaReclamada = Deuda.getById(getIdElemento());
			codRefPag = this.deudaReclamada.getCodRefPag();
			this.listReciboDeuda = GdeDAOFactory.getReciboDAO().getListReciboByDeuda(this.deudaReclamada);
		} else if (getTipoBoleta().intValue() == TipoBoleta.TIPOCUOTA.getId().intValue()) {
			this.cuotaReclamada = ConvenioCuota.getById(getIdElemento());
			codRefPag = new Long(this.cuotaReclamada.getCodRefPag());
			this.listReciboConvenio = GdeDAOFactory.getReciboConvenioDAO().getReciboConvenioByCuota(this.cuotaReclamada);
		}
		
		setEsMigrada(codRefPag == null || codRefPag.longValue() == 0L);
		setEsDeuda(this.deudaReclamada != null);
		setEsCuota(this.cuotaReclamada != null);
		setTieneRecibo(!ListUtil.isNullOrEmpty(this.listReciboDeuda) || !ListUtil.isNullOrEmpty(this.listReciboConvenio));
	}

	public Boolean getEsCuota() {
		return esCuota;
	}

	public void setEsCuota(Boolean esCuota) {
		this.esCuota = esCuota;
	}

	public Boolean getEsDeuda() {
		return esDeuda;
	}

	public void setEsDeuda(Boolean esDeuda) {
		this.esDeuda = esDeuda;
	}

	public Boolean getEsMigrada() {
		return esMigrada;
	}

	public void setEsMigrada(Boolean esMigrada) {
		this.esMigrada = esMigrada;
	}

	public Boolean getTieneRecibo() {
		return tieneRecibo;
	}

	public void setTieneRecibo(Boolean tieneRecibo) {
		this.tieneRecibo = tieneRecibo;
	}

	public void setUsuarioAlta(String usuarioAlta) {
		this.usuarioAlta = usuarioAlta;
	}

	public String getUsuarioAlta() {
		return usuarioAlta;
	}

	public String getInfoUsuarioAlta() {
		if (Canal.ID_CANAL_CMD == this.getCanal().getId()) {
			String ret = "(" + StringUtil.nulltrim(this.getUsuarioAlta()) + ")";
			ret += " / " + this.getArea().getDesArea();
			
			/*List<Oficina> ofs = this.getArea().getListOficina();
			for(Oficina oficina : ofs) {
				ret += oficina.getDesOficina() + ",";
			}
			ret = ret.substring(0, ret.length()-1);
			*/
			return ret;
		} else {
			return this.getUsuarioAlta();
		}		
	}

	public void setListReciboConvenio(List<ReciboConvenio> listReciboConvenio) {
		this.listReciboConvenio = listReciboConvenio;
	}

	public List<ReciboConvenio> getListReciboConvenio() {
		return listReciboConvenio;
	}
	
	public List<Recibo> getListReciboDeuda() {
		return listReciboDeuda;
	}

	public void setListReciboDeuda(List<Recibo> listReciboDeuda) {
		this.listReciboDeuda = listReciboDeuda;
	}

	public void setDeudaReclamada(Deuda deudaReclamada) {
		this.deudaReclamada = deudaReclamada;
	}

	public Deuda getDeudaReclamada() {
		return deudaReclamada;
	}

	public void setCuotaReclamada(ConvenioCuota cuotaReclamada) {
		this.cuotaReclamada = cuotaReclamada;
	}

	public ConvenioCuota getCuotaReclamada() {
		return cuotaReclamada;
	}
	
}