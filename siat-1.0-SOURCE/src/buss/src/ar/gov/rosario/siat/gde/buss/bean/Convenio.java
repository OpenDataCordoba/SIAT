//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Accion;
import ar.gov.rosario.siat.bal.buss.bean.BalDefinicionManager;
import ar.gov.rosario.siat.bal.buss.bean.Canal;
import ar.gov.rosario.siat.bal.buss.bean.IndeterminadoFacade;
import ar.gov.rosario.siat.bal.buss.bean.Sellado;
import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.cyq.buss.bean.Procedimiento;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Feriado;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.TipoDeuda;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.AreaReport;
import ar.gov.rosario.siat.gde.iface.model.ConvenioFormReport;
import ar.gov.rosario.siat.gde.iface.model.ConvenioVO;
import ar.gov.rosario.siat.gde.iface.model.CuotaDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.PlanReport;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorReport;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.model.TotalesConvenioReport;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.seg.buss.bean.Oficina;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a Convenio
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_convenio")
public class Convenio extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	public static final String ACCION_APLICA_PAGO_CUENTA= "Aplicar Pagos a Cuenta";
	public static final String ACCION_SALDO_POR_CADUCIDAD= "Saldo Por Caducidad";
	public static final String ACCION_REHABILITAR= "Rehabilitar Convenios";
	public static final String ACCION_SELECCIONAR= "Seleccionar...";
	
	/*
	 * Estas constantes se utilizan para comparar los estados que devuelve el metodo estaCaduco personalizado para el
	 * Asentamiento de Pagos.
	 */
	public static final Integer NO_CADUCO_SIN_PAGO_A_CTA = 0;
	public static final Integer NO_CADUCO_CON_PAGO_A_CTA = 1;
	public static final Integer CADUCO = 2;
	
	@Transient
	private Logger log = Logger.getLogger(Convenio.class);
	
	@Column(name = "nroConvenio")
	private Integer nroConvenio;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPlan") 
	private Plan plan;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idCuenta") 
	private Cuenta cuenta;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idViaDeuda") 
	private ViaDeuda viaDeuda;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idCanal") 
	private Canal canal;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPlanDescuento") 
	private PlanDescuento planDescuento;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPlanIntFin") 
	private PlanIntFin planIntFin;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idProcurador") 
	private Procurador procurador;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idEstadoConvenio") 
	private EstadoConvenio estadoConvenio;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idSistema") 
	private Sistema sistema;
	
	@Column(name = "usuarioFor")
	private String usuarioFor;
	
	@Column(name = "fechaFor")
	private Date fechaFor;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idTipoPerFor") 
	private TipoPerFor tipoPerFor;
	
	@Transient
	private Persona perFor;
	
	@Column(name="idPerFor")
	private Long idPerFor;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idTipoDocApo") 
	private TipoDocApo tipoDocApo;
	
	@Column(name = "observacionFor")
	private String observacionFor;
	
	@Column(name = "totCapitalOriginal")
	private Double totCapitalOriginal;
	
	@Column(name = "desCapitalOriginal")
	private Double desCapitalOriginal;
	
	@Column(name = "totActualizacion")
	private Double totActualizacion;
	
	@Column(name = "desActualizacion")
	private Double desActualizacion;
	
	@Column(name = "totInteres")
	private Double totInteres;
	
	@Column(name = "desInteres")
	private Double desInteres;
	
	@Column(name = "totImporteConvenio")
	private Double totImporteConvenio;
	
	@Column(name = "cantidadCuotasPlan")
	private Integer cantidadCuotasPlan;
	
	@Column(name = "ultCuoImp")
	private Integer ultCuoImp;
	
    @Column(name="idCasoManual") 
	private String idCaso; // casoManual
    
	@Column(name = "noLiqComPro")
	private Integer noLiqComPro;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@Column(name="fechaAlta")
	private Date fechaAlta;
	
	@Column(name="aplicaPagCue")
	private Integer aplicaPagCue;
	
	/*@ManyToOne(optional=false) 
	@JoinColumn(name="idRescate") 
	private Rescate rescate;*/
	
	@Column(name = "ip")
	private String ip;
		
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idArea")
	private Area area;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idOficina")
	private Oficina oficina;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idProcedimientoCyq")
	private Procedimiento procedimiento;
	
	@OneToMany()
	@JoinColumn(name="idConvenio")
	private List<ConvenioDeuda> listConvenioDeuda; // representa los periodos incluidos en el convenio
	
	@OneToMany()
	@JoinColumn(name="idConvenio")
	@OrderBy("numeroCuota ASC")
	private List<ConvenioCuota> listConvenioCuota;
	
	@Transient
	private String desMotivoCaducidad="";
	
	@Transient
	private Integer cuotaCaducidad=0;
	
	@Transient
	private String desMotInc="";
	
	@Transient
	private  HashMap<Long, Deuda> mapDeudaSimulaSalPorCad;
		
	// Constructores
	public Convenio(){
		super();
	}
	
	public Convenio(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static Convenio getById(Long id) {
		return (Convenio) GdeDAOFactory.getConvenioDAO().getById(id);
	}
	
	public static Convenio getByIdNull(Long id) {
		return (Convenio) GdeDAOFactory.getConvenioDAO().getByIdNull(id);
	}

	public static Convenio getByNroConvenio(String nroConvenio) throws Exception {
		return (Convenio) GdeDAOFactory.getConvenioDAO().getByNroConvenio(nroConvenio);
	}
	
	public static Convenio getByNroSistemaYNroConvenio(String nroSistema, String nroConvenio) throws Exception {
		return (Convenio) GdeDAOFactory.getConvenioDAO().getByNroSistemaYNroConvenio(nroSistema, nroConvenio);
	}
	
	public static List<Convenio> getList() {
		return (ArrayList<Convenio>) GdeDAOFactory.getConvenioDAO().getList();
	}
	
	public static List<Convenio> getListByPlanYNumero(Plan plan, Long numero) throws Exception {
		return (List<Convenio>) GdeDAOFactory.getConvenioDAO().getListByPlanYNumero(plan, numero);
	}
		
	public static List<Convenio> getListActivos() {			
		return (ArrayList<Convenio>) GdeDAOFactory.getConvenioDAO().getListActiva();
	}
	
	/**
	 * Obtiene el ultimo Convenio en el que estuvo la deuda
	 * pasada como parametro.
	 * Si no lo encuntra, retorna null;
	 * 
	 * @param  deuda
	 * @return convenio
	 */
    public static Convenio getLastByDeuda(Deuda deuda) {
    	return GdeDAOFactory.getConvenioDAO().getLastByIdDeuda(deuda.getId());
    }

	/**
	 *  
	 *	Verifica que la deuda incluida en el convenio se encuentre en DeudaAdmin o DeudaJudicial (segun la via del convenio)
	 *  y con el estado de deuda indicado.  
	 * 
	 * @param convenio
	 * @param idEstadoDeuda
	 * @return boolean
	 * @throws Exception
	 */
	public boolean esConvenioConsistente() throws Exception{			
		return GdeDAOFactory.getConvenioDeudaDAO().esConvenioConsistente(this);
	}
	
	/**
	 * Cualquiera de los parametros, si es null o <0, no se tiene en cuenta
	 * @param numeroConvenio
	 * @param idRecurso
	 * @param fechaFormalizacionDesde
	 * @param fechaFormalizacionHasta
	 * @param idViaDeuda
	 * @param noLiquidables - valores posibles: null, 0 y 1
	 * @return
	 */
	public static List<Convenio> getList(Integer numeroConvenio, Long idRecurso,
			Date fechaFormalizacionDesde, Date fechaFormalizacionHasta, Long idViaDeuda, Long idProcurador, Integer noLiquidables) {
		return (ArrayList<Convenio>) GdeDAOFactory.getConvenioDAO().getList(numeroConvenio, 
				idRecurso, fechaFormalizacionDesde, fechaFormalizacionHasta, idViaDeuda, idProcurador, noLiquidables);
	}
	
	/**
	 * cualquier de los parametros puede ser null.<br>
	 * Ordena por procurador<br>
	 * @param listProcurador
	 * @param idRecurso
	 * @param idViaDeuda
	 * @param firstResult
	 * @param maxResults
	 * @param liquidable - Para filtrar los que son liquidables (noLiqComPro null o con valor 0). Si es false no se tiene en cuenta
	 * @return
	 * @throws Exception
	 */
	public static List<Convenio> getList(List<Procurador> listProcurador,
			Long id, Long idViaDeuda, boolean liquidable, Integer firstResult, Integer maxResults) throws Exception {
		return GdeDAOFactory.getConvenioDAO().getList(listProcurador,id, idViaDeuda, liquidable, firstResult, 
														maxResults);
	}
	
	/**
	 * cualquier de los parametros puede ser null.<br>
	 * Ordena por procurador<br>
	 * @param listProcurador
	 * @param listRecurso
	 * @param idViaDeuda
	 * @param firstResult
	 * @param maxResults
	 * @param liquidable - Para filtrar los que son liquidables (noLiqComPro null o con valor 0). Si es false no se tiene en cuenta
	 * @return
	 * @throws Exception
	 */
	public static List<Convenio> getListByListRecurso(List<Procurador> listProcurador,
			List<Recurso> listRecurso, Long idViaDeuda, boolean liquidable, Integer firstResult, Integer maxResults) throws Exception {
		return GdeDAOFactory.getConvenioDAO().getListByListRecurso(listProcurador,listRecurso, idViaDeuda, liquidable, firstResult, 
														maxResults);
	}

	// Getters y setters
	public Integer getCantidadCuotasPlan() {
		return cantidadCuotasPlan;
	}

	public void setCantidadCuotasPlan(Integer cantidadCuotasPlan) {
		this.cantidadCuotasPlan = cantidadCuotasPlan;
	}

	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Double getDesActualizacion() {
		return desActualizacion;
	}

	public void setDesActualizacion(Double desActualizacion) {
		this.desActualizacion = desActualizacion;
	}

	public Double getDesCapitalOriginal() {
		return desCapitalOriginal;
	}

	public void setDesCapitalOriginal(Double desCapitalOriginal) {
		this.desCapitalOriginal = desCapitalOriginal;
	}

	public Double getDesInteres() {
		return desInteres;
	}

	public void setDesInteres(Double desInteres) {
		this.desInteres = desInteres;
	}

	public EstadoConvenio getEstadoConvenio() {
		return estadoConvenio;
	}

	public void setEstadoConvenio(EstadoConvenio estadoConvenio) {
		this.estadoConvenio = estadoConvenio;
	}

	public Date getFechaFor() {
		return fechaFor;
	}

	public void setFechaFor(Date fechaFor) {
		this.fechaFor = fechaFor;
	}

	public Integer getNroConvenio() {
		return nroConvenio;
	}

	public void setNroConvenio(Integer nroConvenio) {
		this.nroConvenio = nroConvenio;
	}

	public String getObservacionFor() {
		return observacionFor;
	}

	public void setObservacionFor(String observacionFor) {
		this.observacionFor = observacionFor;
	}

	public Persona getPerFor() {
		return perFor;
	}

	public void setPerFor(Persona perFor) {
		this.perFor = perFor;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public PlanDescuento getPlanDescuento() {
		return planDescuento;
	}

	public void setPlanDescuento(PlanDescuento planDescuento) {
		this.planDescuento = planDescuento;
	}

	public PlanIntFin getPlanIntFin() {
		return planIntFin;
	}

	public void setPlanIntFin(PlanIntFin planIntFin) {
		this.planIntFin = planIntFin;
	}

	public Procurador getProcurador() {
		return procurador;
	}

	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}

	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public TipoDocApo getTipoDocApo() {
		return tipoDocApo;
	}

	public void setTipoDocApo(TipoDocApo tipoDocApo) {
		this.tipoDocApo = tipoDocApo;
	}

	public TipoPerFor getTipoPerFor() {
		return tipoPerFor;
	}

	public void setTipoPerFor(TipoPerFor tipoPerFor) {
		this.tipoPerFor = tipoPerFor;
	}

	public Double getTotActualizacion() {
		return totActualizacion;
	}

	public void setTotActualizacion(Double totActualizacion) {
		this.totActualizacion = totActualizacion;
	}

	public Double getTotCapitalOriginal() {
		return totCapitalOriginal;
	}

	public void setTotCapitalOriginal(Double totCapitalOriginal) {
		this.totCapitalOriginal = totCapitalOriginal;
	}

	public Double getTotImporteConvenio() {
		return totImporteConvenio;
	}

	public void setTotImporteConvenio(Double totImporteConvenio) {
		this.totImporteConvenio = totImporteConvenio;
	}

	public Double getTotInteres() {
		return totInteres;
	}

	public void setTotInteres(Double totInteres) {
		this.totInteres = totInteres;
	}

	public Integer getUltCuoImp() {
		return ultCuoImp;
	}

	public void setUltCuoImp(Integer ultCuoImp) {
		this.ultCuoImp = ultCuoImp;
	}

	public String getUsuarioFor() {
		return usuarioFor;
	}

	public void setUsuarioFor(String usuarioFor) {
		this.usuarioFor = usuarioFor;
	}

	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
	}
	
	public Long getIdPerFor() {
		return idPerFor;
	}

	public void setIdPerFor(Long idPerFor) {
		this.idPerFor = idPerFor;
	}

	public List<ConvenioCuota> getListConvenioCuota() {
		return listConvenioCuota;
	}

	public void setListConvenioCuota(List<ConvenioCuota> listConvenioCuota) {
		this.listConvenioCuota = listConvenioCuota;
	}

	public List<ConvenioDeuda> getListConvenioDeuda() {
		return listConvenioDeuda;
	}

	public void setListConvenioDeuda(List<ConvenioDeuda> listConvenioDeuda) {
		this.listConvenioDeuda = listConvenioDeuda;
	}

	public Canal getCanal() {
		return canal;
	}

	public void setCanal(Canal canal) {
		this.canal = canal;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getNoLiqComPro() {
		return noLiqComPro;
	}

	public void setNoLiqComPro(Integer noLiqComPro) {
		this.noLiqComPro = noLiqComPro;
	}
	
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}
	

	public Integer getAplicaPagCue() {
		return aplicaPagCue;
	}

	public void setAplicaPagCue(Integer aplicaPagCue) {
		this.aplicaPagCue = aplicaPagCue;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}

	public Oficina getOficina() {
		return oficina;
	}
	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}
	
	public Procedimiento getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(Procedimiento procedimiento) {
		this.procedimiento = procedimiento;
	}

	public String getDesMotInc() {
		return desMotInc;
	}

	public void setDesMotInc(String desMotInc) {
		this.desMotInc = desMotInc;
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();
		
		// Persona que formaliza
		if (getIdPerFor() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONVENIO_PERFOR);
		}
		
		// Tipo Persona Formaliza
		if (getTipoPerFor() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONVENIO_TIPOPERFOR);
		}
		
		// Tipo Doc Aportada
		if (getTipoDocApo() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONVENIO_TIPODOCAPO);
		}
		
		// Observacion no es requerido
		
		
		if (hasError()) {
			return false;
		}
		
		/*
		 * TODO: hay que elegir bien la clave y hacer el todo que esta en checkConcurrencyGen
		 * 
		// tipoObjetoImponible + atributo unicos
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addEntity("cuenta").addEntity("plan");
		
		// Validaciones de Negocio
		if (!GenericDAO.checkConcurrency(this, uniqueMap, DemodaUtil.currentUserContext().getUserName())) {
			addRecoverableError(BaseError.MSG_ERROR_CONCURRENCIA);
		}

		if (hasError()) {
			return false;
		}*/
		
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
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el Convenio. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getConvenioDAO().update(this);
	}

	/**
	 * Desactiva el Convenio. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getConvenioDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Convenio
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Convenio
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Devuelve las cuotas de este convenio en estado Distinto de Pendiente de Pago
	 * 
	 * @author Cristian
	 * @return
	 */
	public List<ConvenioCuota> getListCuotasPagas(){
		List<ConvenioCuota> listCuotasPagas = new ArrayList<ConvenioCuota>();
		
		for (ConvenioCuota cc:getListConvenioCuota()){
			
			if (!EstadoConCuo.ID_IMPAGO.equals(cc.getEstadoConCuo().getId()) )
				listCuotasPagas.add(cc);
			
		}
		
		return listCuotasPagas;
	}
	
	/**
	 * 
	 * Devuelte la cuotas de este convenio en Estado Pendiente de pago
	 * 
	 * @author Cristian
	 * @return
	 */
	public List<ConvenioCuota> getListCuotasImpagas(){
		List<ConvenioCuota> listCuotasImpagas = new ArrayList<ConvenioCuota>();
		
		for (ConvenioCuota cc:getListConvenioCuota()){
			
			if (EstadoConCuo.ID_IMPAGO.equals(cc.getEstadoConCuo().getId()))
				listCuotasImpagas.add(cc);
			
		}
		
		return listCuotasImpagas;
	}

	/**
	 * Obtiene las cuotas pagas que no fueron liquidadas a la fecha pasada como parametro.<br>
	 * @param fechaLiquidacion - si es nula no se tiene en cuenta (trae todas).
	 * @return
	 * @throws Exception
	 */
	public List<ConvenioCuota> getListCuotaPagasNoLiq(Date fechaLiquidacion) throws Exception {
		return ConvenioCuota.getList(getId(),null,fechaLiquidacion,false, true, null);
	}

	public Boolean tienePagosIndet ()throws Exception{
		
		if (getListCuotasImpagas() != null){
			for (ConvenioCuota convenioCuota : getListCuotasImpagas()){
				if (IndeterminadoFacade.getInstance().getEsIndeterminada(convenioCuota)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Devuelve si se incluyo el sellado por la formalizacion del convenio en la cuota uno
	 * @return boolean
	 */
	public Boolean isSelladoEnCuotaUno() {
		
		if (this.getListConvenioCuota().size()>0 && this.getListConvenioCuota().get(0).getSellado()!=null){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Verifica si tiene pagos a cuenta dentro de las fechas del rescate pasado
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param rescate
	 * @return
	 * @throws Exception
	 */
	public Boolean tienePagosACuentaRescate(Date fechaDesde, Date fechaHasta, Rescate rescate)throws Exception{
		
		if (this.tienePagoCue()){
			List<ConvenioCuota>listConvenioCuota= getListCuotasPagas();
			for (ConvenioCuota convenioCuota:listConvenioCuota){
				if(convenioCuota.getEstadoConCuo().getId().equals(EstadoConCuo.ID_PAGO_A_CUENTA)&& DateUtil.isDateAfterOrEqual(convenioCuota.getFechaPago(), rescate.getFechaRescate())
						&& DateUtil.isDateBeforeOrEqual(convenioCuota.getFechaPago(), rescate.getFechaVigRescate())){
					return true;
				}
			}
			return false;
		}else{
			return false;
		}
	}


	/**
	 *  Verifica si esta caduco el Convenio.
	 * 
	 * @param fechaPago
	 * @return
	 */
	public boolean estaCaduco(Date fechaPago) throws Exception{
		
		// Si tiene un rescate individual retorno false
		if (this.getAplicaPagCue().intValue()==SiNo.NO.getId().intValue()){
			log.debug("El convenio tiene un rescate individual");
			return false;
		}
		// Obtener rescate, si existe, para la Fecha Pago.
		Rescate rescate = this.getPlan().obtenerRescate(fechaPago);
		if(rescate != null){
			log.debug("Hay un rescate vigente para esta fecha");
			return false;
		}
		// Si no se obtuvo un rescate, se analizan los criterios de caducidad.
		PlanMotCad planMotCad = this.getPlan().getPlanMotCadVigente(fechaPago);
		if (planMotCad==null){
			log.debug("No se obtuvieron motivos de caducidad del plan");
			return false;
		}
		
		//List<ConvenioCuota> listConvenioCuota = ConvenioCuota.getListByConvenioYFecha(this, fechaPago);
		List<ConvenioCuota> listConvenioCuota = getListConvenioCuota();
		
		// Obtener si hay Pagos Indeterminados consultando la db de Indetermindos.
		for(ConvenioCuota convenioCuota: listConvenioCuota){
			// Identificamos Cuotas Impagas y verificamos contra la db de Indeterminados.
			Date fechaPagoCuota= null;
			// Obtengo la fecha de pago de la cuota
			fechaPagoCuota = convenioCuota.getFechaPago();
			// Si tiene fecha de pago se la paso a FecPag4Cad
			if (fechaPagoCuota != null)convenioCuota.setFecPag4Cad(fechaPagoCuota);
			if(fechaPagoCuota==null && IndeterminadoFacade.getInstance().getEsIndeterminada(convenioCuota)){
				IndetVO indetVO = IndeterminadoFacade.getInstance().getIndeterminada(convenioCuota);
				if (indetVO.getFechaPago()!=null){
					convenioCuota.setFecPag4Cad(indetVO.getFechaPago()); 
				}else{
					convenioCuota.setFecPag4Cad(convenioCuota.getFechaVencimiento());
				}
				//Si la cuota esta indeterminada se fuerza que es pagada a termino en favor del contribuyente
			}
			Date fechaVen = convenioCuota.getFechaVencimiento();
			
			//Se modifico para que no verifique el feriado si la fecha de vencimiento es mayor a hoy
			if (DateUtil.isDateAfter(fechaVen, new Date())){
				convenioCuota.setFecVenNoFeriado(fechaVen);
			}else{
				convenioCuota.setFecVenNoFeriado(Feriado.nextDiaHabil(fechaVen));
			}
		}
		
		boolean cuotasImpagasConsecutivas= validarCuotasImpagasConsecutivas(planMotCad, listConvenioCuota, fechaPago);
		boolean cuotasImpagasAlternadas = validarCuotasImpagasAlternadas(planMotCad, listConvenioCuota, fechaPago);
		boolean diasCorridos = validarDiasCorridos(planMotCad, listConvenioCuota, fechaPago);
		

		// Validar Caducidad
		boolean esCaduco = cuotasImpagasConsecutivas || cuotasImpagasAlternadas	|| diasCorridos;
		
		return esCaduco;
	}
	
	/**
	 *  Verifica si es necesario validar los motivos de caducidad.
	 * <p>( se usa en el Reporte de Convenios a Caducar )</p>
	 * @param fechaPago
	 * @return
	 */
	public boolean necesitaValidarCaducidad(Date fechaPago) throws Exception{
		
		// Si tiene un rescate individual retorno false
		if (this.getAplicaPagCue().intValue()==SiNo.NO.getId().intValue()){
			return false;
		}
		// Obtener rescate, si existe, para la Fecha Pago.
		Rescate rescate = this.getPlan().obtenerRescate(fechaPago);
		if(rescate != null){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Devuelve la descripion del Estado del Convenio, teniendo en cuenta la Caducidad del Mismo para la fecha acutal.
	 * CUIDADO: este metodo levanta el plan, los rescate, la lista de motivos de caducidad, la lista de cuotas y los feriados. 
	 * Tener en cuenta performance al llamarlo
	 * @author Cristian
	 * @return
	 * @throws Exception 
	 */
	public String getDescEstadoConvenio() throws Exception{
		if (this.getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_VIGENTE ){
			if (this.getRecurso() != null && estaCaduco(this.getRecurso().getFecUltPag())){
				// Caduco
				return EstadoConvenio.DESC_CADUCO + " " + this.desMotivoCaducidad + this.cuotaCaducidad.toString();				
			} else {
				// Vigente
				return this.getEstadoConvenio().getDesEstadoConvenio();
			}
		} else {			
			// Si es Recompuesto o Cancelado
			return this.getEstadoConvenio().getDesEstadoConvenio(); 
		}	
	}
	
	
	/**
	 *  Verifica si el Convenio tiene Pagos a Cuenta.
	 *  
	 * @return boolean
	 */
	public boolean tienePagoCue() throws Exception{
		return GdeDAOFactory.getConvenioCuotaDAO().tienePagoACuenta(this);
	}
	/**
	 * Verifica si tiene una cuota saldo emitida sin vencer
	 */
	
	public boolean tieneCuotaSaldo() throws Exception{
		return GdeDAOFactory.getReciboConvenioDAO().tieneCuotaSaldo(this);
	}
	
	/**
	 *  Verifica si el Convenio tiene Pagos a Cuenta.
	 *  
	 * @return boolean
	 */
	public boolean tienePagosBuenos() throws Exception{
		return GdeDAOFactory.getConvenioCuotaDAO().tienePagosBuenos(this);
	}
	
	/**
	 * Verifica si el convenio tiene todas las cuotas vencidas
	 * @return
	 * @throws Exception
	 */
	public boolean tieneTodasLasCuotasVencidas() throws Exception{
		return GdeDAOFactory.getConvenioCuotaDAO().tieneTodasLasCuotasVencidas(this);
	}
	
	/**
	 * Verifica si el Convenio registra Pagos (buenos o a cuenta)
	 */
	public boolean registraPagos()throws Exception{
		if (this.tienePagoCue()|| this.tienePagosBuenos()){
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 *  Verifica si el Convenio tiene todas las cuotas pagas y como pagos buenos.
	 *
	 * @return boolean
	 */
	public boolean tieneTodasCuotasPagosBuenos() throws Exception{
		return GdeDAOFactory.getConvenioCuotaDAO().tieneTodasCuotasPagosBuenos(this);
	}
	
	/**
	 *  Valida si el Convenio pasado como parametro tiene todas las cuotas pagas y al menos un pago a cuenta.
	 *
	 * @return boolean
	 */
	public boolean pagoConCuotasPagosACuenta() throws Exception{
		return GdeDAOFactory.getConvenioCuotaDAO().pagoConCuotasPagosACuenta(this);
	}
	
	/**
	 *  Busca la mayor de las Fecha de Pago de las Cuotas del Convenio.
	 *
	 * @return Date
	 */
	public Date ultimaFechaPago() throws Exception{
		return GdeDAOFactory.getConvenioCuotaDAO().ultimaFechaPago(this);
	}
	
	/**
	 *  Valida el Criterio de Caducidad por Cuotas Impagas Consecutivas
	 *  <i>
	 *  <p>1. 	CantidadDeCuotasImpagas = 0</p>
	 *	<p>2. 	Para cada cuota:</p>
	 *	<p>		2.1. Si es Pago Bueno</p>
	 *	<p>		2.1.1. Cantidad de Cuotas Impagas = 0</p>
	 *	<p>		2.2. Si no, si la fecha de pago de la cuota es mayor a fecha o nula, sumar uno a CantidadDeCuotasImpagas</p>
	 *	<p>		2.3. Si CantidadDeCuotasImpagas es igual a Cantidad de Cuotas Impagas, salir del Para.</p>
	 *	<p>3. 	Si CantidadDeCuotasImpagas es igual a Cantidad de Cuotas Impagas, se cumple el criterio de Caducidad.</p>
	 *	<p>4. 	Sino, no se cumple el criterio de Caducidad</p>
	 *  </i>
	 * @param planMotCad
	 * @param listConvenioCuota
	 * @param fechaPago
	 * @return boolean
	 * @throws Exception
	 */
	public boolean validarCuotasImpagasConsecutivas(PlanMotCad planMotCad, List<ConvenioCuota> listConvenioCuota, Date fechaPago) throws Exception{
		if (planMotCad.getCantCuoCon() == null)return false;
		int cantDeCuotasImpagas = 0;
		
		List<ConvenioCuota>listConvenioCuotaRepl = new ArrayList<ConvenioCuota>();
		listConvenioCuotaRepl.addAll(listConvenioCuota);
		
		for(ConvenioCuota convenioCuota: listConvenioCuota){
			//Obtener solo las cuotas cuya fecha de vencimiento sea < a la fecha de pago
			if (DateUtil.isDateBefore(convenioCuota.getFecVenNoFeriado(), fechaPago)){
				//Si la cuota se asento como pago a Cuenta vuelvo a verificar si sigue cumpliendose la condicion de caducidad
				if (convenioCuota.getFecPag4Cad()!=null && convenioCuota.getEstadoConCuo().equals(EstadoConCuo.getById(EstadoConCuo.ID_PAGO_A_CUENTA))){
					int cantCuoImpPagos = 0;
					for (ConvenioCuota conCuota: listConvenioCuotaRepl){
						if (DateUtil.isDateBefore(conCuota.getFecVenNoFeriado(), convenioCuota.getFecPag4Cad()) && DateUtil.isDateBefore(conCuota.getFecVenNoFeriado(), fechaPago)){
							if (conCuota.getFecPag4Cad()==null || DateUtil.isDateAfterOrEqual(conCuota.getFecPag4Cad() , convenioCuota.getFecPag4Cad())){
								cantCuoImpPagos ++;
								if (cantCuoImpPagos == planMotCad.getCantCuoCon().intValue()){
									if (this.cuotaCaducidad > convenioCuota.getNumeroCuota() || this.cuotaCaducidad==0){
										this.desMotivoCaducidad= "por cuotas impagas consecutivas al pago de cuota nro: ";
										this.cuotaCaducidad=convenioCuota.getNumeroCuota();
									}
									log.debug("---- CADUCO POR CUOTAS IMPAGAS CONSECUTIVAS ----EN CUOTA:"+convenioCuota.getNumeroCuota()+"CONDICION DADA A CUOTA: "+conCuota.getNumeroCuota());
									return true;
								}
							}else{
								cantCuoImpPagos=0;
							}
						}else{
							break;
						}
					}
				}
				if(convenioCuota.getEstadoConCuo().getId().longValue() == EstadoConCuo.ID_PAGO_BUENO)
					cantDeCuotasImpagas = 0;				
				else
					if(convenioCuota.getFecPag4Cad() == null 
							|| DateUtil.isDateAfter(convenioCuota.getFecPag4Cad(), fechaPago))
						cantDeCuotasImpagas++;
				if(cantDeCuotasImpagas == planMotCad.getCantCuoCon().intValue()){
					if (this.cuotaCaducidad > convenioCuota.getNumeroCuota() || this.cuotaCaducidad==0){
						this.desMotivoCaducidad= "por cuotas impagas consecutivas en cuota nro: ";
						this.cuotaCaducidad=convenioCuota.getNumeroCuota();
					}
					break;
				}
			}else{
				break;
			}
		}
		if(cantDeCuotasImpagas == planMotCad.getCantCuoCon().intValue()){
			log.debug("---- CADUCO POR CUOTAS IMPAGAS CONSECUTIVAS ----");
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 *  Valida el Criterio de Caducidad por Cuotas Impagas Alternadas
	 *  <i>
	 *  <p>1.	CantidadDeCuotasImpagas = 0</p>
	 *	<p>2.	Para cada cuota</p>
	 *	<p>		2.1.	Si la fecha de pago de la cuota es nula o (es mayor a fecha y No es Pago Bueno)</p>
	 *	<p>		2.1.1.	Sumar uno a CantidadDeCuotasImpagas.</p>
	 *	<p>		2.2 	Si CantidadDeCuotasImpagas es igual a Cantidad de Cuotas Impagas, salir del Para.</p>
	 *	<p>3.	Si CantidadDeCuotasImpagas es igual a Cantidad de Cuotas Impagas, se cumple el criterio de Caducidad.</p>
	 *	<p>4.	Sino, no se cumple el criterio de Caducidad.</p>
	 *  </i>
	 * @param planMotCad
	 * @param listConvenioCuota
	 * @param fechaPago
	 * @return boolean
	 * @throws Exception
	 */
	public boolean validarCuotasImpagasAlternadas(PlanMotCad planMotCad, List<ConvenioCuota> listConvenioCuota, Date fechaPago) throws Exception{
		if (planMotCad.getCantCuoAlt()==null)return false;
		int cantDeCuotasImpagas = 0;
		
		List<ConvenioCuota>listConvenioCuotaRepl = new ArrayList<ConvenioCuota>();
		listConvenioCuotaRepl.addAll(listConvenioCuota);
		
		for(ConvenioCuota convenioCuota: listConvenioCuota){
			if (DateUtil.isDateBefore(convenioCuota.getFecVenNoFeriado(), fechaPago)){
				if (convenioCuota.getFecPag4Cad()!= null && convenioCuota.getEstadoConCuo().equals(EstadoConCuo.getById(EstadoConCuo.ID_PAGO_A_CUENTA))){
					int cantCuoImpPagos = 0;
					for (ConvenioCuota conCuota:listConvenioCuotaRepl){
						if (DateUtil.isDateBefore(conCuota.getFecVenNoFeriado(),convenioCuota.getFecPag4Cad()) && DateUtil.isDateBefore(conCuota.getFecVenNoFeriado(), fechaPago)){
							if (conCuota.getFecPag4Cad()==null || DateUtil.isDateAfter(conCuota.getFecPag4Cad(), convenioCuota.getFecPag4Cad())){
								cantCuoImpPagos ++;
								if (cantCuoImpPagos == planMotCad.getCantCuoAlt()){
									log.debug("------- CADUCO POR CUOTAS IMPAGAS ALTERNADAS --------");
									if (this.cuotaCaducidad > convenioCuota.getNumeroCuota() || this.cuotaCaducidad==0){
										this.desMotivoCaducidad= "por cuotas impagas alternadas al pago de cuota nro: ";
										this.cuotaCaducidad=convenioCuota.getNumeroCuota();
									}
									log.debug(desMotivoCaducidad + conCuota.getNumeroCuota());
									return true;
								}
							}
						}else{
							break;
						}
					}
				}
				if(convenioCuota.getFecPag4Cad() == null 
							|| (DateUtil.isDateAfter(convenioCuota.getFecPag4Cad(), fechaPago) 
									&& convenioCuota.getEstadoConCuo().getId().longValue() != EstadoConCuo.ID_PAGO_BUENO))
						cantDeCuotasImpagas++;
				if(cantDeCuotasImpagas == planMotCad.getCantCuoAlt().intValue()){
					if (this.cuotaCaducidad > convenioCuota.getNumeroCuota() || this.cuotaCaducidad==0){
						this.desMotivoCaducidad= "por cuotas impagas alternadas en cuota nro: ";
						this.cuotaCaducidad= convenioCuota.getNumeroCuota();
					}
					break;
				}
			}else{
				break;
			}
		}
		if(cantDeCuotasImpagas == planMotCad.getCantCuoAlt().intValue()){
			log.debug("------- CADUCO POR CUOTAS IMPAGAS ALTERNADAS --------");
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 *   Valida el Criterio de Caducidad por Dias Corridos desde el Vencimiento de la primera Cuota Impaga.
	 *  <i>
	 *  <p>1.	Para cada cuota</p>
	 *	<p>		1.1.	Obtener Fecha de Vencimiento Cuota/Recibo</p>
	 *	<p>2.	Se obtiene de {cuota} la cuota con menor fecha de vencimiento y  (fechaPago nula o (mayor a fecha y No es Pago Bueno))</p>
	 *	<p>		2.1.	Se obtiene la cantidad de dï¿½as entre dicha fecha de vencimiento de la cuota y fecha.</p>
	 *	<p>3.	Si la cantidad de dï¿½as es mayor a Cantidad de Dï¿½as desde el Vencimiento, se cumple el criterio de Caducidad.</p>
	 *	<p>4.	Sino, no se cumple el criterio de Caducidad.</p>
	 *  </i>
	 * @param planMotCad
	 * @param listConvenioCuota
	 * @param fechaPago
	 * @return boolean
	 * @throws Exception
	 */
	public boolean validarDiasCorridos(PlanMotCad planMotCad, List<ConvenioCuota> listConvenioCuota, Date fechaPago) throws Exception{
		if (planMotCad.getCantDias() ==null)return false;
		 
		Log log =LogFactory.getLog(Convenio.class);
		if(ListUtil.isNullOrEmpty(listConvenioCuota)){
			return false;
		}
		
		
		
		for(ConvenioCuota convenioCuota: listConvenioCuota){
			if (DateUtil.isDateBefore(convenioCuota.getFechaVencimiento(), fechaPago)){
				log.debug("-----CUOTA "+convenioCuota.getNumeroCuota());
				
				if (convenioCuota.getFecPag4Cad() !=null && convenioCuota.getEstadoConCuo().equals(EstadoConCuo.getById(EstadoConCuo.ID_PAGO_A_CUENTA))){
					if (DateUtil.isDateAfter(convenioCuota.getFecPag4Cad(), convenioCuota.getFechaVencimiento())){
						Integer cantDias = 0;
						if (DateUtil.isDateAfter(convenioCuota.getFecPag4Cad(), fechaPago)){
							cantDias=DateUtil.getCantDias(convenioCuota.getFechaVencimiento(), fechaPago);
						}else{
							cantDias=DateUtil.getCantDias(convenioCuota.getFechaVencimiento(), convenioCuota.getFecPag4Cad());
						}
						//se modifica segun bug 709 de >= a >
						if (cantDias>planMotCad.getCantDias().intValue()){
							if (this.cuotaCaducidad > convenioCuota.getNumeroCuota() || this.cuotaCaducidad==0){
								this.desMotivoCaducidad= "por cantidad de d\u00EDas desde la primer cuota impaga en cuota nro: ";
								this.cuotaCaducidad=convenioCuota.getNumeroCuota();
							}
							log.debug("-------- CADUCO POR CANTIDAD DE DIAS DESDE LA PRIMER CUOTA IMPAGA ---------");
							return true;
						}
					}
					
				}else if (convenioCuota.getFecPag4Cad()==null){
					//se modifica segun bug 709 de >= a >
					if (DateUtil.getCantDias(convenioCuota.getFechaVencimiento(), fechaPago)>planMotCad.getCantDias().intValue()){
						if (this.cuotaCaducidad > convenioCuota.getNumeroCuota() || this.cuotaCaducidad==0){
							this.desMotivoCaducidad= "por cantidad de d\u00EDas desde la primer cuota impaga en cuota nro: ";
							this.cuotaCaducidad=convenioCuota.getNumeroCuota();
						}
						log.debug("-------- CADUCO POR CANTIDAD DE DIAS DESDE LA PRIMER CUOTA IMPAGA ---------");
						return true;
					}
				}
			}else{
				break;
			}
		}
		
		
		return false;
	}
	

    public ReciboConvenio crearReciboConvenio(Date fechaVto, Canal canal, Procurador procurador) {
    	Calendar cal = Calendar.getInstance();
      	
    	ReciboConvenio reciboConvenio = new ReciboConvenio();
    	reciboConvenio.setEstaImpreso(0);
    	reciboConvenio.setCodRefPag(GdeDAOFactory.getReciboConvenioDAO().getNextCodRefPago());
    	reciboConvenio.setServicioBanco(this.getSistema().getServicioBanco());
     	reciboConvenio.setConvenio(this);
     	if (procurador!=null){
     		reciboConvenio.setProcurador(procurador);
     	}
    	reciboConvenio.setCanal(canal);
    	reciboConvenio.setNroRecibo(GdeDAOFactory.getReciboConvenioDAO().getNextNroRecibo());
    	Integer anio=cal.get(Calendar.YEAR);
    	reciboConvenio.setAnioRecibo(anio);
    	reciboConvenio.setFechaGeneracion(cal.getTime());
    	reciboConvenio.setFechaVencimiento(fechaVto);
    	reciboConvenio.setEsCuotaSaldo(0);
    	
    	
    	return reciboConvenio;
    }
    
    /**
     * Graba los pagos de las Deudas del Convenio en PagoDeuda
     * @param convenioDeuda
     * @param idTipoPago
     * @param importe
     * @param actualizacion
     * @param esPagoTotal
     * @throws Exception
     */
	public void registrarPagoDeudaConv(ConvenioDeuda convenioDeuda,Long idTipoPago,Double importe,Double actualizacion,Boolean esPagoTotal, Date fechaPago)
		throws Exception{
		Integer pagoTotal=0;
		if (esPagoTotal){
			pagoTotal = 1;
		}

		PagoDeuda pagoDeuda = new PagoDeuda();
		pagoDeuda.setIdDeuda(convenioDeuda.getDeuda().getId());
		pagoDeuda.setTipoPago(TipoPago.getById(idTipoPago));
		pagoDeuda.setIdPago(this.getId());
		if (fechaPago==null){
			if (TipoPago.getById(idTipoPago).equals(TipoPago.getById(TipoPago.ID_PAGO_BUENO))){
				fechaPago = GdeDAOFactory.getConDeuCuoDAO().getMayorFechaPagoByConvenioDeuda(convenioDeuda);
			}	
		}
		pagoDeuda.setFechaPago(fechaPago);
		pagoDeuda.setImporte(NumberUtil.truncate(importe, SiatParam.DEC_IMPORTE_DB));
		pagoDeuda.setActualizacion(NumberUtil.truncate(actualizacion,SiatParam.DEC_IMPORTE_DB));
		pagoDeuda.setEsPagoTotal(pagoTotal);
		GdeDAOFactory.getPagoDeudaDAO().update(pagoDeuda);
	}

	/**
	 * Cancela las deudas de un Convenio en un Saldo por Caducidad Pagos Buenos
	 * @param convenioDeuda
	 * @throws Exception
	 */
	
	public void cancelarDeudaPagoBueno (ConvenioDeuda convenioDeuda)throws Exception{
		Deuda deuda= convenioDeuda.getDeuda();
		if(deuda.getRecurso().getEsAutoliquidable().intValue() == SiNo.NO.getId().intValue()){
			deuda.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_CANCELADA));							
		}
		deuda.setSaldo(0D);
		deuda.setConvenio(null);
		deuda.setFechaPago(convenioDeuda.getFechaPago());
		Double actualizacion = 0D;
		List<PagoDeuda>listPagoDeuda = GdeDAOFactory.getPagoDeudaDAO().getListPagosByIdConvenio(convenioDeuda.getDeuda().getId(), this.getId());
		if (listPagoDeuda !=null){
			for (PagoDeuda pagoDeuda : listPagoDeuda){
				actualizacion += pagoDeuda.getActualizacion();
			}
		}
		actualizacion += convenioDeuda.getActEnPlan();
		deuda.setActualizacion(NumberUtil.truncate(actualizacion,SiatParam.DEC_IMPORTE_DB));
		GdeGDeudaManager.getInstance().update(deuda);
	}
	
	
	/**
	 * Cancela las deudas incluidas Convenio en un Saldo por Caducidad por Pagos A Cuenta
	 * @param deuda
	 * @param actualizacion
	 * @param fecha
	 * @throws Exception
	 */
	public void cancelarDeudaPagoACuenta (Deuda deuda, Double actualizacion, Date fecha)throws Exception{
		if(deuda.getRecurso().getEsAutoliquidable().intValue() == SiNo.NO.getId().intValue()){
			deuda.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_CANCELADA));			
		}
		deuda.setSaldo(0D);
		deuda.setConvenio(null);
		deuda.setFechaPago(fecha);
		List<PagoDeuda>listPagoDeuda = GdeDAOFactory.getPagoDeudaDAO().getListPagosByIdConvenio(deuda.getId(), this.getId());
		if (listPagoDeuda !=null){
			for (PagoDeuda pagoDeuda : listPagoDeuda){
				actualizacion += pagoDeuda.getActualizacion();
			}
		}
		deuda.setActualizacion(NumberUtil.truncate(actualizacion,SiatParam.DEC_IMPORTE_DB));

		GdeGDeudaManager.getInstance().update(deuda);
	}
	
	/**
	 * Actualiza el Detalle de Deuda de la Constancia pasandolo a inactivo. Devuelve la Constancia.
	 * 	@return constanciaDeu
	 * <i>(paso 3.1.a)</i>
	 */
	public ConstanciaDeu actualizarConstancia(Deuda deuda) throws Exception{
		
		ConDeuDet conDeuDet = deuda.obtenerConDeuDetVigente();
		if(conDeuDet != null){
			conDeuDet.setEstado(Estado.INACTIVO.getId());
			conDeuDet.getConstanciaDeu().updateConDeuDet(conDeuDet);
			return conDeuDet.getConstanciaDeu();
		}else
			return null;
	}
	public RecConCuo mejorDescuento(Long idCuota, Date fechaVto, List<DesGen> listDesGenVigente, List<DesEsp> listDesEspVigente) throws Exception{    	
    	// Se crea el reciboDeuda que se va a devolver
		Log log = LogFactory.getLog(Convenio.class);
		ConvenioCuota cuota=ConvenioCuota.getById(idCuota);
    	RecConCuo recConCuo = new RecConCuo();
    	recConCuo.setConvenioCuota(cuota);
    	recConCuo.setCapitalOriginal(cuota.getCapitalCuota());
    	recConCuo.setTotCapitalOriginal(cuota.getCapitalCuota());
    	recConCuo.setInteresFinanciero(cuota.getInteres());
    	recConCuo.setTotIntFin(cuota.getInteres());
    	recConCuo.setDesCapitalOriginal(0D);
    	recConCuo.setDesActualizacion(0D);
    	Double importeSellado = 0D;
    	if (cuota.getSellado()!=null){
    		importeSellado = cuota.getImporteSellado();
    	}
    	recConCuo.setImporteSellado(importeSellado);
    	recConCuo.setDesIntFin(0D);
    	Double actualizacion = NumberUtil.round(cuota.actualizacionImporteCuota(fechaVto).getRecargo(),SiatParam.DEC_IMPORTE_CALC);
    	DeudaAct cuotaActualizada = cuota.actualizacionImporteCuota(fechaVto);
    	recConCuo.setActualizacion(NumberUtil.truncate(actualizacion,SiatParam.DEC_IMPORTE_DB));
    	recConCuo.setTotActualizacion(NumberUtil.truncate(actualizacion, SiatParam.DEC_IMPORTE_DB));
		
		List dtos = new ArrayList();
    	dtos.addAll(listDesGenVigente);
    	dtos.addAll(listDesEspVigente);    	
    	for(Object dto: dtos){    		
    		boolean esAplicable =false;
    		double porDesAct = 0;//Representa el porcentaje de descuento sobre la actualizacion, del descuento actual    		
    		if(dto instanceof DesGen){
				porDesAct = ((DesGen)dto).getPorDes();
				esAplicable = true;    			
    		}else if (dto instanceof DesEsp){
    			porDesAct = ((DesEsp)dto).getPorDesAct();
				esAplicable = true;
    		}
    		if(esAplicable){
    			// Se calcula la actualizacion(el recargo) sobre la deuda actualizada a la fecha de vto
    			double descuentoActualizacion =cuotaActualizada.getRecargo()*porDesAct;
    			descuentoActualizacion=NumberUtil.round(descuentoActualizacion, SiatParam.DEC_IMPORTE_CALC);
    			double totActualizacionActualizada = NumberUtil.round(cuotaActualizada.getRecargo(),SiatParam.DEC_IMPORTE_CALC) - descuentoActualizacion;
	    		if(totActualizacionActualizada<recConCuo.getTotActualizacion()){// Si el recargo es menor que el que tiene, se aplica el descuento
	    			recConCuo.setDesActualizacion(NumberUtil.truncate(descuentoActualizacion, SiatParam.DEC_IMPORTE_DB));
	    			recConCuo.setTotActualizacion(NumberUtil.truncate(recConCuo.getActualizacion()-recConCuo.getDesActualizacion(),SiatParam.DEC_IMPORTE_DB));
	        		if(dto instanceof DesGen){
    	    			recConCuo.setDesGen((DesGen)dto);    
    					log.debug("setea descuento general a reciboConvenioCuota");
    	    		}else if(dto instanceof DesEsp){
    	    			recConCuo.setDesEsp((DesEsp)dto);
    					log.debug("setea descuento ESPECIAL a reciboConvenioCuota");
	    			}
    			}
    		}	
    	}
        	
    	return recConCuo;    	
    }
	
	/**
	 * Reconfecciona o reimprime Cuotas de un Convenio
	 * @param listaIdCuotas
	 * @param fechaVencimiento
	 * @param canal
	 * @return
	 * @throws Exception
	 */
	public List<ReciboConvenio> reconfeccionar(List<Long> listaIdCuotas, Date fechaVencimiento, Canal canal, Procurador procurador)throws Exception{
		// inicializa lista de recibos deuda y recibos
		List<RecConCuo> listReciboConCuo = new ArrayList<RecConCuo>();
    	List<ReciboConvenio> listReciboConvenio = new ArrayList<ReciboConvenio>();
    	Log log = LogFactory.getLog(Convenio.class);
    	Cuenta cuenta=this.getCuenta();
    	
    	boolean cobraSellado = false;
    	//boolean esReimpresion = false;
    	
		// obtiene descuentos generales y especiales aplicables a la cuenta 
    	List<DesGen> listDesGenVigente; // = DesGen.getVigente(cuenta.getRecurso());
    	List<DesEsp> listDesEsp; // = cuenta.getListDesEspVigentes(TipoDeuda.ID_PLAN_DE_PAGOS, this.getViaDeuda().getId(), fechaVencimiento);
    	
    	if (cuenta != null){
    		listDesGenVigente = DesGen.getVigente(cuenta.getRecurso());
    		listDesEsp = cuenta.getListDesEspVigentes(TipoDeuda.ID_PLAN_DE_PAGOS, this.getViaDeuda().getId(), fechaVencimiento);    		
    	} else {
    		listDesGenVigente = new ArrayList<DesGen>();
    		listDesEsp = new ArrayList<DesEsp>();
    	}
    	
    	
    	//recorro la lista de Cuotas
    	for (Long idCuota: listaIdCuotas){
    		ConvenioCuota convenioCuota = ConvenioCuota.getById(idCuota);
    		if (DateUtil.isDateBefore(convenioCuota.getFechaVencimiento(),new Date())){
    			
    			//**************************************************************************
    			//  Ver si se aplica y como se aplicaria sellado, y quitar cuenta != null &&
    			//**************************************************************************
    			
    			if (cuenta != null && DateUtil.isDateBefore(convenioCuota.getFechaVencimiento(), fechaVencimiento)) cobraSellado = true;
    			listReciboConCuo.add(mejorDescuento(idCuota, fechaVencimiento, listDesGenVigente, listDesEsp));
    		} else {
				// esta es una cuota que no esta vencida
	
				// si es una reconfeccion Web, 
				//    vamos a REIMPRIR (puede ser una reimpresion de deuda migrada o una reimpresion de deuda generada por SIAT)
				// si no es una reconfeccion web
				//    se ignora la deuda no vencida
				ReciboConvenio reciboConvenio = new ReciboConvenio();
				reciboConvenio.setEsReimpresionCuota(true);
				reciboConvenio.setEstaImpreso(0);
	
			    	reciboConvenio.setCodRefPag(convenioCuota.getCodRefPag().longValue());
			    	// TODO: aqui se deberia sacar el servicio banco de la deuda, pero en la migracion viene nula
			    	
			    	if (convenioCuota.getSistema().getServicioBanco()==null) {
				    	reciboConvenio.setServicioBanco(new ServicioBanco());
			    		
			    	} else {
				    	reciboConvenio.setServicioBanco(convenioCuota.getSistema().getServicioBanco() );
			    		
			    	}
			    	reciboConvenio.setConvenio(this);
			    	Integer anio = DateUtil.getAnio(convenioCuota.getFechaVencimiento());
			    	reciboConvenio.setAnioRecibo(anio);
			    	reciboConvenio.setCanal(canal);
			    	reciboConvenio.setFechaGeneracion(new Date());
			    	reciboConvenio.setEsCuotaSaldo(new Integer(0));
			    	reciboConvenio.setFechaVencimiento(convenioCuota.getFechaVencimiento());
			    	reciboConvenio.setTotImporteRecibo(convenioCuota.getImporteCuota());
			    	if (procurador !=null){
			    		reciboConvenio.setProcurador(procurador);
			    	}

					RecConCuo reciboConCuo = new RecConCuo();
					reciboConCuo.setConvenioCuota(convenioCuota);
					reciboConCuo.setCapitalOriginal(convenioCuota.getCapitalCuota());
					reciboConCuo.setDesCapitalOriginal(0D);
					reciboConCuo.setTotCapitalOriginal(convenioCuota.getCapitalCuota());
					Double importeSellado=0D;
					if (convenioCuota.getSellado() != null){
						importeSellado = convenioCuota.getImporteSellado();
					}
					reciboConCuo.setImporteSellado(importeSellado);
					reciboConCuo.setActualizacion(0D);
					reciboConCuo.setDesActualizacion(0D);
					reciboConCuo.setTotActualizacion(0D);
					reciboConCuo.setInteresFinanciero(convenioCuota.getInteres());
					reciboConCuo.setDesIntFin(0D);
					reciboConCuo.setTotIntFin(convenioCuota.getInteres());
					
					reciboConvenio.getListRecConCuo().add(reciboConCuo);
					
					listReciboConvenio.add(reciboConvenio);
				

			}
    	
    	}
		// aqui tenemos una lista de reciboConCuo y otra de reciboConvenio para las reimpresiones

		HashMap<Object, ReciboConvenio> hashRecibosConvenios = new HashMap<Object, ReciboConvenio>(); // El hash es a modo de apoyo, lo que vale es la lista de recibos 
    	for(RecConCuo reciboConCuo: listReciboConCuo){
    		Object descuentoRecConCuo = reciboConCuo.getDescuento();
    		ReciboConvenio reciboConvenio = (ReciboConvenio) hashRecibosConvenios.get(descuentoRecConCuo);
    		if(reciboConvenio == null){//No existe el recibo en el hash, para ese descuento, se lo crea y se lo agrea a la lista y al hash
    			reciboConvenio = crearReciboConvenio(fechaVencimiento, canal, procurador);
    			reciboConvenio.setDescuento(descuentoRecConCuo);
    			hashRecibosConvenios.put(descuentoRecConCuo, reciboConvenio);
    			listReciboConvenio.add(reciboConvenio);
    		}

			
			reciboConvenio.getListRecConCuo().add(reciboConCuo);
			reciboConCuo.setReciboConvenio(reciboConvenio);
    	}
    	
    	// aqui tenemos la lista de recibos 
    	// obtiene el sellado que corresponda.
    	Sellado s = null;
    	if (cobraSellado ) {
    		s = BalDefinicionManager.aplicarSellado(this.getCuenta().getRecurso().getId(), Accion.ID_ACCION_RECONFECCIONAR_CUOTA, fechaVencimiento, 1, 1);
    	}
    	
    	// recorre los recibos generados y en caso que existan muchas lineas, los corta
    	List<ReciboConvenio> listRecibosCortados = new ArrayList<ReciboConvenio>();
    	for(ReciboConvenio reciboConvenio: listReciboConvenio){
    		listRecibosCortados.addAll(reciboConvenio.cortar(ReciboConvenio.MAX_CANT_CUOTA_X_RECIBO_RECONFECCION));
    	}
    	
     	// en caso que corresponda asocia sellado al primer recibo y luego graba
    	// solo vamos a grabar los recibos que no son reimpresiones
    	for(ReciboConvenio reciboConvenio: listRecibosCortados){
        	if (!reciboConvenio.getEsReimpresionCuota()) {
        		if (s!=null) {
        			reciboConvenio.setSellado(s);
        			reciboConvenio.recalcularValores();
        			s = null;
        		}
        		log.debug("Valores a ingresar NroRecibo:"+reciboConvenio.getNroRecibo()+" codRefPag: "+reciboConvenio.getCodRefPag()+" idServicioBanco: "+reciboConvenio.getServicioBanco().getId());
        		log.debug("anio recibo: "+ reciboConvenio.getAnioRecibo()+" idCanal: "+reciboConvenio.getCanal().getId()+ " fechaGeneracion: "+reciboConvenio.getFechaGeneracion());
        		log.debug("convenio: "+reciboConvenio.getConvenio().getId());
        		GdeDAOFactory.getReciboConvenioDAO().update(reciboConvenio);
        		for (RecConCuo recConCuo:reciboConvenio.getListRecConCuo()){
        			GdeDAOFactory.getRecConCuoDAO().update(recConCuo);
        		}
        	}
    	}
		return listRecibosCortados;
	}
	
	
	public List<ReciboConvenio> getCuotaSaldo (List<ConvenioCuota> listaCuotas, Canal canal, String usuario)throws Exception{
		List<RecConCuo> listReciboConCuo = new ArrayList<RecConCuo>();
    	ReciboConvenio reciboConvenio = new ReciboConvenio();
    	reciboConvenio.setNroRecibo(GdeDAOFactory.getReciboConvenioDAO().getNextNroRecibo());
    	reciboConvenio.setEsCuotaSaldo(1);
    	reciboConvenio.setCodRefPag(GdeDAOFactory.getReciboConvenioDAO().getNextCodRefPago());
    	reciboConvenio.setFechaGeneracion(new Date());
    	reciboConvenio.setUsuarioCuotaSaldo(usuario);
    	reciboConvenio.setConvenio(this);
    	reciboConvenio.setCanal(canal);
    	Integer anioRec=DateUtil.getAnio(new Date());
    	reciboConvenio.setAnioRecibo(anioRec);
    	reciboConvenio.setServicioBanco(this.getSistema().getServicioBanco());
    	Log log = LogFactory.getLog(Convenio.class);
    	
    	
    	Double totalCapital = 0D;
    	Double totalInteres = 0D;
    	Date vencimiento = listaCuotas.get(0).getFechaVencimiento();
    	for (ConvenioCuota convenioCuota : listaCuotas){
    		RecConCuo reciboConCuo = new RecConCuo();
    		reciboConCuo.setReciboConvenio(reciboConvenio);
    		reciboConCuo.setConvenioCuota(convenioCuota);
    		reciboConCuo.setCapitalOriginal(convenioCuota.getCapitalCuota());
    		reciboConCuo.setDesCapitalOriginal(0D);
    		reciboConCuo.setActualizacion(0D);
    		Double importeSellado=0D;
    		if (convenioCuota.getSellado() !=null)importeSellado=convenioCuota.getImporteSellado();
    		reciboConCuo.setImporteSellado(importeSellado);
    		reciboConCuo.setDesActualizacion(0D);
    		reciboConCuo.setInteresFinanciero(convenioCuota.getInteres());
    		reciboConCuo.setDesIntFin(convenioCuota.getInteres());
    		
    		//Si el vencimiento de la cuota es menor a la fecha de hoy mas un mes, cobra interes (descuento interes=0)
    		if (!DateUtil.isDateAfter(convenioCuota.getFechaVencimiento(), DateUtil.addMonthsToDate(new Date(), 1))){
    			reciboConCuo.setDesIntFin(0D);
    		}
    
    		reciboConCuo.setTotCapitalOriginal(reciboConCuo.getCapitalOriginal()-reciboConCuo.getDesCapitalOriginal());
    		reciboConCuo.setTotActualizacion(reciboConCuo.getActualizacion()-reciboConCuo.getDesActualizacion());
    		reciboConCuo.setTotIntFin(reciboConCuo.getInteresFinanciero()-reciboConCuo.getDesIntFin());
    		totalCapital += reciboConCuo.getTotCapitalOriginal();
    		totalInteres += reciboConCuo.getTotIntFin();
    		log.debug("agregando cuota: "+ convenioCuota.getNumeroCuota());
    		reciboConvenio.getListRecConCuo().add(reciboConCuo);
    		listReciboConCuo.add(reciboConCuo);
    	}
    	

    	reciboConvenio.setSellado(BalDefinicionManager.aplicarSellado(this.getCuenta().getRecurso().getId(), Accion.ID_ACCION_CUOTASALDO, new Date(), 1, 1));
    	if (reciboConvenio.getSellado()!=null){
    		reciboConvenio.setImporteSellado(NumberUtil.truncate(reciboConvenio.getSellado().getImporteSellado(),SiatParam.DEC_IMPORTE_DB));
    	}else{
    		reciboConvenio.setImporteSellado(0D);
    	}
    	reciboConvenio.setTotImporteRecibo(totalCapital + totalInteres + reciboConvenio.getImporteSellado());
    	//Seteo el vencimiento = al vencimiento de la primer cuota a incluir en la cuota saldo
    	reciboConvenio.setFechaVencimiento(vencimiento);
    	reciboConvenio.setEstaImpreso(0);
    	List<ReciboConvenio>listRecibos = new ArrayList<ReciboConvenio>();
    	listRecibos.add(reciboConvenio);

   		GdeDAOFactory.getReciboConvenioDAO().update(reciboConvenio);
   		for (RecConCuo recConCuo:reciboConvenio.getListRecConCuo()){
   			GdeDAOFactory.getRecConCuoDAO().update(recConCuo);
   		}
    	
    	
    	return listRecibos;
    	
	}
	
	
	public void registrarPagoCuentaConDeuCuo (ConvenioCuota pagoACuenta, ConvenioDeuda convenioDeuda, Double saldoEnPlanCub, Boolean esPagoTotal)throws Exception{
		
		ConDeuCuo conDeuCuo = new ConDeuCuo();
		conDeuCuo.setConvenioCuota(pagoACuenta);
		conDeuCuo.setConvenioDeuda(convenioDeuda);
		Integer pagoTotal=0;
		if (esPagoTotal)pagoTotal=1;
		conDeuCuo.setEsPagoTotal(pagoTotal);
		conDeuCuo.setSaldoEnPlanCub(saldoEnPlanCub);
		conDeuCuo.setFechaPago(pagoACuenta.getFechaPago());
		GdeDAOFactory.getConDeuCuoDAO().update(conDeuCuo);
	}
	
	public boolean faltanConDeuCuo(){
		
		return GdeDAOFactory.getConvenioDeudaDAO().faltanConDeuCuo(this);
	}
	/**
	 * Realiza el saldo por caducidad de un convenio o la anulacion si es true el parametro
	 * @param observacion
	 * @param caso
	 * @param esAnulacion
	 * @throws Exception
	 */
	public ConEstCon realizarSaldoPorCaducidad(String observacion, CasoVO caso, boolean esAnulacion )throws Exception{
		//verificar si tiene pagos
		Boolean tienePagosBuenos = this.tienePagosBuenos();
		Boolean tienePagosACuenta = this.tienePagoCue();
		Deuda deuda;
		Double importeCapitalPago=0D;
		Double actualizacion=0D;
		Double actualizacionSaldo=0D;
		Boolean esPagoTotal;
		Double montoPagoACuenta=0D;
		Double rateActualizacion=0D;
		Double capitalCalculado=0D;
		Double capitalOriginal=0D;
		Double totalEnPlan=0D;
		Double importeCapitalCancelado=0D;
		Log log = LogFactory.getLog(Convenio.class);
		List<ConstanciaDeu> listConstanciaDeu = new ArrayList<ConstanciaDeu>();
		
		//validaciones
		
		//Estado Convenio distinto de vigente
		if (this.getEstadoConvenio()!= EstadoConvenio.getById(EstadoConvenio.ID_VIGENTE)){
			this.addRecoverableError(GdeError.SALPORCAD_ESTADOCONVENIO);
		}
		
		//Tiene indeterminados
		if (this.tienePagosIndet()){
			this.addRecoverableError(GdeError.SALPORCAD_INDETERMINADOS);
		}
		
		//Tiene ConDeuCuo
		if ((tienePagosBuenos || tienePagosACuenta) && this.faltanConDeuCuo()){
			this.addRecoverableError(GdeError.SALPORCAD_FALTAN_CONDEUCUO);
		}
		
		ConEstCon nuevoHistEstCon = new ConEstCon();
		
		try{
			if (!this.hasError()){
				if(tienePagosBuenos){
					log.debug("Ingresando a tiene PagosBuenos ..........");
					for (ConvenioDeuda convenioDeuda : this.getListConvenioDeuda()){
						//valido si no hay total en plan por si se trata de convenio migrado
						if (convenioDeuda.getTotalEnPlan()==null || convenioDeuda.getTotalEnPlan().doubleValue()==0D){
							totalEnPlan=convenioDeuda.getCapitalEnPlan()+ convenioDeuda.getActEnPlan();
						}else{
							totalEnPlan=convenioDeuda.getTotalEnPlan();
						}
						if (convenioDeuda.getSaldoEnPlan().doubleValue()==0D){
							deuda = convenioDeuda.getDeudaForSalPorCad();
							if (deuda == null){
								this.addNonRecoverableError(GdeError.SALPORCAD_NOSERECUPERODEUDA);
								break;
							}
							importeCapitalPago = convenioDeuda.getCapitalEnPlan();
							actualizacion = convenioDeuda.getActEnPlan();
							esPagoTotal = ConDeuCuo.getEsPagoTotalByIdConvenioDeuda(convenioDeuda.getId());
							this.cancelarDeudaPagoBueno(convenioDeuda);
							this.registrarPagoDeudaConv(convenioDeuda,TipoPago.ID_PAGO_BUENO,importeCapitalPago, actualizacion, esPagoTotal,convenioDeuda.getFechaPago());
							log.debug("DEUDA CANCELADA POR PAGOS BUENOS "+deuda.getId());
						}else if (convenioDeuda.getSaldoEnPlan().doubleValue()< totalEnPlan){
							esPagoTotal=false;
							Double importePago=totalEnPlan - convenioDeuda.getSaldoEnPlan().doubleValue();
							//modificado 141009 para que tome indexacion en envio -cambiado 11/11/09 para que solo lo haga para convenios migrados
							Double indiceActualizacion = NumberUtil.round((convenioDeuda.getActEnPlan()-convenioDeuda.getImporteIndexEnvio())/totalEnPlan,SiatParam.DEC_IMPORTE_CALC);
							deuda = convenioDeuda.getDeudaForSalPorCad();
							if (deuda == null){
								this.addNonRecoverableError(GdeError.SALPORCAD_NOSERECUPERODEUDA);
								break;
							}
							//Obtengo el capital original por si se trata de convenio migrado
							if (convenioDeuda.getCapitalOriginal()==0){
								capitalOriginal = deuda.getSaldo();
							}else{
								capitalOriginal = convenioDeuda.getCapitalOriginal();
							}
							importeCapitalPago = NumberUtil.round(importePago * (1-indiceActualizacion),SiatParam.DEC_IMPORTE_CALC);
							importeCapitalCancelado = NumberUtil.round(importeCapitalPago * (capitalOriginal/convenioDeuda.getCapitalEnPlan()),SiatParam.DEC_IMPORTE_CALC);
							actualizacion= NumberUtil.round(importePago * indiceActualizacion, SiatParam.DEC_IMPORTE_CALC);
							
							deuda.setSaldo(deuda.getSaldo()-importeCapitalCancelado);
							if (deuda.getActualizacion()!=null){
								deuda.setActualizacion(NumberUtil.truncate(deuda.getActualizacion()+actualizacion, SiatParam.DEC_IMPORTE_DB));
							}else{
								deuda.setActualizacion(NumberUtil.truncate(actualizacion,SiatParam.DEC_IMPORTE_DB));
							}
							deuda.setConvenio(null);
							GdeGDeudaManager.getInstance().update(deuda);
							this.registrarPagoDeudaConv(convenioDeuda, TipoPago.ID_PAGO_BUENO, importeCapitalPago, actualizacion, esPagoTotal,convenioDeuda.getFechaPago());
							log.debug("DEUDA PAGO PARCIAL POR PAGOS BUENOS "+deuda.getId());
						}
					}
					log.debug("Saliendo de tiene PagosBuenos ..........");
				}
				if (tienePagosACuenta){
					log.debug("Ingresando a tiene PagosCuenta ..........");
					List<ConvenioCuota> pagosACuenta = GdeDAOFactory.getConvenioCuotaDAO().getListPagoACuentaByConvenioOrderByFechaPago(this);
					for (ConvenioCuota pagoACuenta: pagosACuenta){
						Double actualizacionCuota =0D;
						if (pagoACuenta.getActualizacion()!=null)actualizacionCuota = pagoACuenta.getActualizacion();
						montoPagoACuenta = NumberUtil.round(pagoACuenta.getImporteCuota() + actualizacionCuota,SiatParam.DEC_IMPORTE_DB);
						boolean noHayMasSaldoEnDeuda=false;
						while (montoPagoACuenta>0D && !this.hasErrorNonRecoverable()&& noHayMasSaldoEnDeuda==false){
							List<ConvenioDeuda> listConvenioDeuda = GdeDAOFactory.getConvenioDeudaDAO().getListConDeuConSaldo(this);
							if(ListUtil.isNullOrEmpty(listConvenioDeuda)){
								this.addNonRecoverableError(GdeError.SALPORCAD_NOSERECUPERODEUDA);
								return null;
							}
							
							for (ConvenioDeuda convenioDeuda: listConvenioDeuda){
								deuda = convenioDeuda.getDeudaForSalPorCad();
								if (deuda == null){
									this.addNonRecoverableError(GdeError.SALPORCAD_NOSERECUPERODEUDA);
									return null;
								}
								if (deuda.getSaldo().doubleValue() >= 0.009D){
									actualizacionSaldo = NumberUtil.round(deuda.actualizacionSaldo(pagoACuenta.getFechaPago()).getRecargo(),SiatParam.DEC_IMPORTE_CALC);
									if (montoPagoACuenta >= deuda.getSaldo().doubleValue()+actualizacionSaldo){
										importeCapitalPago = deuda.getSaldo();
										if (importeCapitalPago == deuda.getImporte().doubleValue()){
											esPagoTotal=true;
										}else{
											esPagoTotal=false;										
										}
										log.debug("DEUDA A CANCELAR PAGO A CUENTA: "+deuda.getPeriodo()+"/"+deuda.getAnio());
										log.debug("SALDO: "+deuda.getSaldo()+" ACTUALIZACION: "+actualizacionSaldo);
										log.debug("CANCELADA CON CUOTA: "+ pagoACuenta.getNumeroCuota());
										log.debug("IMPORTE CAPITAL PAGO: "+importeCapitalPago);
										log.debug("IMPORTE DEL PAGO A CUENTA: "+montoPagoACuenta);
										log.debug("RESTO PAGO A CUENTA: "+ (montoPagoACuenta - (importeCapitalPago + actualizacionSaldo)));
										this.cancelarDeudaPagoACuenta(deuda, actualizacionSaldo,pagoACuenta.getFechaPago());
										Double saldoEnPlanCub = NumberUtil.truncate(importeCapitalPago + actualizacionSaldo, SiatParam.DEC_IMPORTE_DB);
										this.registrarPagoCuentaConDeuCuo (pagoACuenta, convenioDeuda, saldoEnPlanCub, esPagoTotal);
										this.registrarPagoDeudaConv(convenioDeuda, TipoPago.ID_PAGO_A_CUENTA, importeCapitalPago, actualizacionSaldo, esPagoTotal,pagoACuenta.getFechaPago());
										montoPagoACuenta = NumberUtil.truncate(montoPagoACuenta - (importeCapitalPago + actualizacionSaldo),SiatParam.DEC_IMPORTE_DB);
									}else {
										rateActualizacion = NumberUtil.round(actualizacionSaldo/(deuda.getSaldo()+actualizacionSaldo),SiatParam.DEC_IMPORTE_CALC);
										capitalCalculado = NumberUtil.round(montoPagoACuenta * (1-rateActualizacion), SiatParam.DEC_IMPORTE_CALC);
										actualizacion = NumberUtil.round(montoPagoACuenta * rateActualizacion,SiatParam.DEC_IMPORTE_CALC);
										esPagoTotal=false;
										this.registrarPagoDeudaConv(convenioDeuda, TipoPago.ID_PAGO_A_CUENTA, capitalCalculado, actualizacion, esPagoTotal, pagoACuenta.getFechaPago());
										this.registrarPagoCuentaConDeuCuo (pagoACuenta, convenioDeuda, montoPagoACuenta,esPagoTotal);
										log.debug("DEUDA PAGO PARCIAL PAGO A CUENTA: "+deuda.getId());
										log.debug("SALDO INICIAL: "+deuda.getSaldo()+" ACTUALIZACION: "+actualizacionSaldo);
										log.debug("CANCELADA CON CUOTA: "+ pagoACuenta.getNumeroCuota());
										log.debug("SALDO DEL PAGO A CUENTA: "+montoPagoACuenta);
										deuda.setSaldo(NumberUtil.truncate(deuda.getSaldo()-capitalCalculado, SiatParam.DEC_IMPORTE_DB));
										if (deuda.getActualizacion()!= null){
											deuda.setActualizacion(NumberUtil.truncate(deuda.getActualizacion()+actualizacion,SiatParam.DEC_IMPORTE_DB));
										}else{
											deuda.setActualizacion(NumberUtil.truncate(actualizacion,SiatParam.DEC_IMPORTE_DB));
										}
										GdeGDeudaManager.getInstance().update(deuda);
										montoPagoACuenta = 0D;
										break;
									}
								}
							}
							noHayMasSaldoEnDeuda=true;
							log.debug("SALDO A FAVOR: "+montoPagoACuenta + " DE CUOTA: "+pagoACuenta.getNumeroCuota()+ " FECHA DE PAGO: "+pagoACuenta.getFechaPago());
						}
					}
				}
				for (ConvenioDeuda convenioDeuda: this.getListConvenioDeuda()){
						deuda =convenioDeuda.getDeudaForSalPorCad();
						if (deuda == null){
							this.addNonRecoverableError(GdeError.SALPORCAD_NOSERECUPERODEUDA);
							break;
						}
						deuda.setConvenio(null);
						GdeGDeudaManager.getInstance().update(deuda);
						// Si la Deuda se encuentra en Via Judicial, se debe actualizar la constancia.
						if(deuda.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL && deuda.getSaldo() < 0.009D){
							ConstanciaDeu constanciaDeu = this.actualizarConstancia(deuda);
							if(constanciaDeu != null && !listConstanciaDeu.contains(constanciaDeu)){
								listConstanciaDeu.add(constanciaDeu);					
							}
						}
				}
				// Al finalizar recorro la lista de Constancias Deu actualizadas.
				for(ConstanciaDeu constanciaDeu: listConstanciaDeu) {
					if (!constanciaDeu.tieneConDeuDetActivos()) {
						constanciaDeu.cambiarEstConDeu(EstConDeu.ID_CANCELADA, "Detalle de Deuda totalmente cancelado durante saldo por Caducidad");
					} else {
						constanciaDeu.cambiarEstConDeu(EstConDeu.ID_MODIFICADA, "Detalle de Deuda parcialmente cancelado durante saldo por Caducidad");
					}
				}
				
				nuevoHistEstCon.setConvenio(this);
				EstadoConvenio estadoConvenio;
				String obs="";
				if (esAnulacion){
					estadoConvenio = EstadoConvenio.getById(EstadoConvenio.ID_ANULADO);
					obs="Realizada Anulacion. ";
				}else{
					estadoConvenio = EstadoConvenio.getById(EstadoConvenio.ID_RECOMPUESTO);
					obs="Realizado saldo por Caducidad. ";
				}
				nuevoHistEstCon.setEstadoConvenio(estadoConvenio);
				nuevoHistEstCon.setFechaConEstCon(new Date());
				nuevoHistEstCon.setObservacion(obs+observacion);
				if (caso != null){
					nuevoHistEstCon.setIdCaso(caso.getIdFormateado());
				}
				GdeDAOFactory.getConEstConDAO().update(nuevoHistEstCon);
				
				this.setEstadoConvenio(estadoConvenio);
				GdeDAOFactory.getConvenioDAO().update(this);
			}
			
		}catch (Exception e){
			log.debug("AGREGANDO NO RECOVERABLE"+e);
			//this.addNonRecoverableError(BaseError.MSG_FUERA_DE_DOMINIO);
			throw (e);
		}
		
		return nuevoHistEstCon;
		
	}
	
	
	/**
	 * Dado un registro de deuda que se encuentra en convenio, 
	 * se simula el saldo por caducidad y se devuelve otro registro de deuda, el valor de saldo correspondinte. 
	 * 
	 * @param deuda
	 * @returnstatic
	 * @throws Exception
	 */
	public Deuda obtenerDeudaSimulaSalPorCad(Deuda deuda)throws Exception{
		
		String funcName = DemodaUtil.currentMethodName();
		
		log.debug(funcName + " enter -> idDedua: " + deuda.getId());
		
		Deuda deudaSimula = null;
		
		// Calculamos la simulacion si es necesario
		if (mapDeudaSimulaSalPorCad == null || mapDeudaSimulaSalPorCad.size() <= 0){
		
			this.simularSaldoPorCaducidad();
			
			if (this.hasErrorOrMessage()){
				log.debug(funcName + " Error encontrado en simularSaldoPorCaducidad ...");
				this.passErrorMessages(deuda);
				return deuda;
			}			
			log.debug(funcName + " mapDeudaSimulaSalPorCad.size(): " + mapDeudaSimulaSalPorCad.size());
		} 
		
		// Devolvemos un registro de deuda simulada
		if(mapDeudaSimulaSalPorCad.containsKey(deuda.getId())){
			deudaSimula = mapDeudaSimulaSalPorCad.get(deuda.getId());
			log.debug(funcName + " Obteniendo deuda simulada: " + deudaSimula.getStrPeriodo());
		} else {
			deudaSimula = deuda;
			log.debug(funcName + " deuda simulada no fue encontrada: " + deudaSimula.getStrPeriodo());
		}
		
		log.debug(funcName + " exit");
	
		return deudaSimula;
	}
	
	/**
	 * Simula un saldo por caducidad llenando el mapa "mapDeudaSimulaSalPorCad" con objetos deudaAdmin con el saldo resultante.
	 * Si surge algun error, lo carga en la instancia. 
	 * 
	 * @return Convenio
	 * @throws Exception
	 */
	public Convenio simularSaldoPorCaducidad() throws Exception{
		String funcName = DemodaUtil.currentMethodName();

		log.debug(funcName + " enter -> convenio nro: " + this.getNroConvenio());
		
		// Instanciamos le mapa de deuda si es necesario
		if (mapDeudaSimulaSalPorCad == null){
			mapDeudaSimulaSalPorCad = new HashMap<Long, Deuda>();
		}
		
		//verificar si tiene pagos
		Boolean tienePagosBuenos = this.tienePagosBuenos();
		Boolean tienePagosACuenta = this.tienePagoCue();
		Deuda deuda;
		Double importeCapitalPago=0D;
		Double actualizacionSaldo=0D;
		Double montoPagoACuenta=0D;
		Double rateActualizacion=0D;
		Double capitalCalculado=0D;
		Double capitalOriginal=0D;
		Double totalEnPlan=0D;
		Double importeCapitalCancelado=0D;
		DeudaAdmin deuAdm = null;

		//validaciones
		//Estado Convenio distinto de vigente
		if (this.getEstadoConvenio()!= EstadoConvenio.getById(EstadoConvenio.ID_VIGENTE)){
			//this.addRecoverableError(GdeError.SALPORCAD_ESTADOCONVENIO);
			this.addRecoverableValueError("El estado del convenio Nro.: " + this.getNroConvenio() + " debe ser Vigente o Caduco" );
		}
		
		//Tiene indeterminados
		if (this.tienePagosIndet()){
			//this.addRecoverableError(GdeError.SALPORCAD_INDETERMINADOS);
			this.addRecoverableValueError("El convenio Nro.: " + this.getNroConvenio() + " posee Pagos Indeterminados" );
		}
		
		//Tiene ConDeuCuo
		if (this.faltanConDeuCuo()){
			//this.addRecoverableError(GdeError.SALPORCAD_FALTAN_CONDEUCUO);
			this.addRecoverableValueError("El convenio Nro.: " + this.getNroConvenio() + " es Inconsistente, faltan detalles de imputaciones de cuota a deuda" );
		}
	
		log.debug(funcName + " hasErr: " + this.hasError() +
				" tienePagosBuenos: " + tienePagosBuenos() +
				" tienePagosACuenta: " + tienePagosACuenta);	
		
		for(String str:SiatUtil.getStringValueErrors(this.getListError())){
			log.debug(funcName + " msg: " + str);	
		}
		
		try{
			if (!this.hasError()){
				// Si tiene pagos buenos
				if(tienePagosBuenos){
					log.debug(funcName +" Ingresando a tiene PagosBuenos ..........");
					for (ConvenioDeuda convenioDeuda : this.getListConvenioDeuda()){
						//valido si no hay total en plan por si se trata de convenio migrado
						if (convenioDeuda.getTotalEnPlan()==null || convenioDeuda.getTotalEnPlan().doubleValue()==0D){
							totalEnPlan=convenioDeuda.getCapitalEnPlan()+ convenioDeuda.getActEnPlan();
						}else{
							totalEnPlan=convenioDeuda.getTotalEnPlan();
						}
						if (convenioDeuda.getSaldoEnPlan().doubleValue()==0D){
							deuda = convenioDeuda.getDeudaForSalPorCad();
							if (deuda == null){
								this.addNonRecoverableError(GdeError.SALPORCAD_NOSERECUPERODEUDA);
								this.addRecoverableValueError("El convenio Nro.: " + this.getNroConvenio() + " es Inconsistente, No se encontro la Deuda en Vía Administrativa o Judicial" );
								break;
							}
							
							// Clonamos la deuda y setear valores sin afecta la original.
							deuAdm = (DeudaAdmin) this.clonarForSimulacionSalPorCad(deuda);

							deuAdm.setSaldo(0D);
							
							// Agregamos deuda clonada al mapa
							mapDeudaSimulaSalPorCad.put(deuAdm.getId(), deuAdm);

							log.debug(funcName +" DEUDA CANCELADA POR PAGOS BUENOS "+deuda.getStrPeriodo());
							
						}else if (convenioDeuda.getSaldoEnPlan().doubleValue()< totalEnPlan){
							//esPagoTotal=false;
							Double importePago=totalEnPlan - convenioDeuda.getSaldoEnPlan().doubleValue();
							Double indiceActualizacion = NumberUtil.round(convenioDeuda.getActEnPlan()/totalEnPlan,SiatParam.DEC_IMPORTE_CALC);
							deuda = convenioDeuda.getDeudaForSalPorCad();
							if (deuda == null){
								this.addNonRecoverableError(GdeError.SALPORCAD_NOSERECUPERODEUDA);
								this.addRecoverableValueError("El convenio Nro.: " + this.getNroConvenio() + " es Inconsistente, No se encontro la Deuda en Vía Administrativa o Judicial" );
								break;
							}
							
							// Clonamos la deuda y setear valores sin afecta la original.
							deuAdm = (DeudaAdmin) this.clonarForSimulacionSalPorCad(deuda);
							
							//Obtengo el capital original por si se trata de convenio migrado
							if (convenioDeuda.getCapitalOriginal()==0){
								capitalOriginal = deuda.getSaldo();
							}else{
								capitalOriginal = convenioDeuda.getCapitalOriginal();
							}
							importeCapitalPago = NumberUtil.round(importePago * (1-indiceActualizacion),SiatParam.DEC_IMPORTE_CALC);
							importeCapitalCancelado = NumberUtil.round(importeCapitalPago * (capitalOriginal/convenioDeuda.getCapitalEnPlan()),SiatParam.DEC_IMPORTE_CALC);
							//actualizacion= NumberUtil.round(importePago * indiceActualizacion, SiatParam.DEC_IMPORTE_CALC);
							
							deuAdm.setSaldo(deuda.getSaldo()-importeCapitalCancelado);
							
							// Agregamos deuda clonada al mapa
							mapDeudaSimulaSalPorCad.put(deuAdm.getId(), deuAdm);
							
							log.debug(funcName +" DEUDA PAGO PARCIAL POR PAGOS BUENOS "+deuda.getStrPeriodo());
						}
					}
					log.debug(funcName +" Saliendo de tiene PagosBuenos ..........");
				} // Fin tienePagosBuenos

				// Si tiene pagos a cuenta
				if (tienePagosACuenta){
					log.debug(funcName +" Ingresando a tiene PagosCuenta ..........");
					List<ConvenioCuota> pagosACuenta = GdeDAOFactory.getConvenioCuotaDAO().getListPagoACuentaByConvenioOrderByFechaPago(this);
					for (ConvenioCuota pagoACuenta: pagosACuenta){
						Double actualizacionCuota =0D;
						if (pagoACuenta.getActualizacion()!=null)actualizacionCuota = pagoACuenta.getActualizacion();
						montoPagoACuenta = NumberUtil.round(pagoACuenta.getImporteCuota() + actualizacionCuota,SiatParam.DEC_IMPORTE_DB);
						boolean noHayMasSaldoEnDeuda=false;
						while (montoPagoACuenta>0D && !this.hasErrorNonRecoverable()&& noHayMasSaldoEnDeuda==false){
							List<ConvenioDeuda> listConvenioDeuda = GdeDAOFactory.getConvenioDeudaDAO().getListConDeuConSaldo(this);
							if(ListUtil.isNullOrEmpty(listConvenioDeuda)){
								this.addNonRecoverableError(GdeError.SALPORCAD_NOSERECUPERODEUDA);
								this.addRecoverableValueError("El convenio Nro.: " + this.getNroConvenio() + " es Inconsistente, No se encontro la Deuda en Vía Administrativa o Judicial" );
								return this;
							}
							
							for (ConvenioDeuda convenioDeuda: listConvenioDeuda){
								
								deuda = convenioDeuda.getDeudaForSalPorCad();

								if (deuda == null){
									this.addNonRecoverableError(GdeError.SALPORCAD_NOSERECUPERODEUDA);
									this.addRecoverableValueError("El convenio Nro.: " + this.getNroConvenio() + " es Inconsistente, No se encontro la Deuda en Vía Administrativa o Judicial" );
									return this;
								}

								// Si ya lo pusimos en el mapa, lo obtenemos desde ahi.
								if (mapDeudaSimulaSalPorCad.containsKey(deuda.getId())){
									deuAdm = (DeudaAdmin) mapDeudaSimulaSalPorCad.get(deuda.getId());
								} else {
									// SiNo Clonamos la deuda y setear valores sin afecta la original.
									deuAdm = (DeudaAdmin) this.clonarForSimulacionSalPorCad(deuda);
								}
								
								if (deuAdm.getSaldo().doubleValue() >= 0.009D){
									actualizacionSaldo = NumberUtil.round(deuAdm.actualizacionSaldo(pagoACuenta.getFechaPago()).getRecargo(),SiatParam.DEC_IMPORTE_CALC);
									if (montoPagoACuenta >= deuAdm.getSaldo().doubleValue()+actualizacionSaldo){
										importeCapitalPago = deuAdm.getSaldo();

										log.debug(funcName +" DEUDA A CANCELAR PAGO A CUENTA: "+deuda.getStrPeriodo());
										log.debug(funcName +" SALDO: "+deuda.getSaldo()+" ACTUALIZACION: "+actualizacionSaldo);
										log.debug(funcName +" CANCELADA CON CUOTA: "+ pagoACuenta.getNumeroCuota());
										log.debug(funcName +" IMPORTE CAPITAL PAGO: "+importeCapitalPago);
										log.debug(funcName +" IMPORTE DEL PAGO A CUENTA: "+montoPagoACuenta);
										log.debug(funcName +" RESTO PAGO A CUENTA: "+ (montoPagoACuenta - (importeCapitalPago + actualizacionSaldo)));
										
										deuAdm.setSaldo(0D);
									
										// Agregamos deuda clonada al mapa
										mapDeudaSimulaSalPorCad.put(deuAdm.getId(), deuAdm);
										
										montoPagoACuenta = NumberUtil.truncate(montoPagoACuenta - (importeCapitalPago + actualizacionSaldo),SiatParam.DEC_IMPORTE_DB);
									}else {
										rateActualizacion = NumberUtil.round(actualizacionSaldo/(deuAdm.getSaldo()+actualizacionSaldo),SiatParam.DEC_IMPORTE_CALC);
										capitalCalculado = NumberUtil.round(montoPagoACuenta * (1-rateActualizacion), SiatParam.DEC_IMPORTE_CALC);
										
										log.debug(funcName +" DEUDA PAGO PARCIAL PAGO A CUENTA: "+deuAdm.getStrPeriodo());
										log.debug(funcName +" SALDO INICIAL: "+deuAdm.getSaldo()+" ACTUALIZACION: "+actualizacionSaldo);
										log.debug(funcName +" CANCELADA CON CUOTA: "+ pagoACuenta.getNumeroCuota());
										log.debug(funcName +" SALDO DEL PAGO A CUENTA: "+montoPagoACuenta);
										
										deuAdm.setSaldo(NumberUtil.truncate(deuAdm.getSaldo()-capitalCalculado, SiatParam.DEC_IMPORTE_DB));
										
										// Agregamos deuda clonada al mapa
										mapDeudaSimulaSalPorCad.put(deuAdm.getId(), deuAdm);
										
										montoPagoACuenta = 0D;
										break;
									}
								}
							}
							noHayMasSaldoEnDeuda=true;
							
							log.debug(funcName +" SALDO A FAVOR: "+montoPagoACuenta + " DE CUOTA: "+pagoACuenta.getNumeroCuota()+ " FECHA DE PAGO: "+pagoACuenta.getFechaPago());
						}
					}
				} // Fin tienePagosACuenta
				
			
			} // Fin !hasError 

			
			// Colocamos en el mapa la deuda que no entro por no tener pagoBueno ni Pago a cuenta.
			for (ConvenioDeuda convenioDeuda: this.getListConvenioDeuda()){
				deuda =convenioDeuda.getDeudaForSalPorCad();
				if (deuda == null){
					this.addNonRecoverableError(GdeError.SALPORCAD_NOSERECUPERODEUDA);
					this.addRecoverableValueError("El convenio Nro.: " + this.getNroConvenio() + " es Inconsistente, No se encontro la Deuda en Vía Administrativa o Judicial" );
					break;
				}
				
				// Colocamos las deuda que no se hallan obtenido de los pasos anteriores
				if (!mapDeudaSimulaSalPorCad.containsKey(deuda.getId())){
				
					// Clonamos la deuda y setear valores sin afecta la original.
					deuAdm = (DeudaAdmin) this.clonarForSimulacionSalPorCad(deuda);;
					deuAdm.setSaldo(deuda.getSaldo());
					
					mapDeudaSimulaSalPorCad.put(deuAdm.getId(), deuAdm);
				}	
			}				
			
			
			log.debug(funcName + " exit -> hasErr: " + this.hasError());
			return this;
			
		}catch (Exception e){
			log.debug("AGREGANDO NO RECOVERABLE"+e);
			//this.addNonRecoverableError(BaseError.MSG_FUERA_DE_DOMINIO);
			throw (e);
		}
	}
	
	/**
	 * Clona un registro de deuda, solo con los datos necesarios para simular el saldo por caducidad. 
	 *
	 * @param deuda
	 * @return
	 */
	private Deuda clonarForSimulacionSalPorCad(Deuda deuda){
		
		DeudaAdmin deuAdm = new DeudaAdmin();
		
		deuAdm.setId(deuda.getId());
		deuAdm.setPeriodo(deuda.getPeriodo());
		deuAdm.setAnio(deuda.getAnio());
		deuAdm.setCuenta(deuda.getCuenta());
		deuAdm.setFechaVencimiento(deuda.getFechaVencimiento());
		deuAdm.setRecClaDeu(deuda.getRecClaDeu());
		deuAdm.setImporte(deuda.getImporte());
		deuAdm.setSaldo(deuda.getSaldo());
		
		return deuAdm;
	}
	
	
	
	private void borrarConDeuCuoParaPagoACuenta()throws Exception{
		List<ConvenioCuota> listPagosACuenta = GdeDAOFactory.getConvenioCuotaDAO().getListPagoACuentaByConvenio(this);
		for (ConvenioCuota convenioCuota : listPagosACuenta){
			List<ConDeuCuo> listConDeuCuo = GdeDAOFactory.getConDeuCuoDAO().getConDeuCuoByConvenioCuota(convenioCuota);
			for (ConDeuCuo conDeuCuo:listConDeuCuo){
				GdeDAOFactory.getConDeuCuoDAO().delete(conDeuCuo);
			}
		}
	}
	
	/**
	 * Vuelta atras de un saldo Por Caducidad
	 * @param observacion
	 * @param caso
	 * @throws Exception
	 */
	public ConEstCon vueltaAtrasSalPorCad(String observacion, CasoVO caso)throws Exception{
		List<ConvenioDeuda> listConvenioDeuda = this.getListConvenioDeuda();
		
		boolean huboPagos=false;
		boolean cambioDeVia = false;
		Deuda deuda;
		Log log = LogFactory.getLog(Convenio.class);
		boolean errores=false;
		ConEstCon conEstCon = null;
		if (this.estadoConvenio.getId().longValue() != EstadoConvenio.ID_RECOMPUESTO){
			this.addNonRecoverableError(GdeError.SALPORCADVUELTA_ESTADOERRONEO);
			return conEstCon;
		}
		Date fechaSalPorCad = GdeDAOFactory.getConEstConDAO().getFechaUltEstRecompuesto(this.getId());
		
		
		//Validaciones
		for (ConvenioDeuda convenioDeuda: listConvenioDeuda){
			//Determinar si se aplico algun pago a las deudas despues de realizar el saldo por caducidad
			if (GdeDAOFactory.getPagoDeudaDAO().tienePagosPostASalPorCadConvenio(convenioDeuda.getDeuda().getId(), convenioDeuda.getConvenio().getId(), fechaSalPorCad)){
				this.addNonRecoverableError(GdeError.SALPORCADVUELTA_HUBOPAGOS);
				huboPagos = true;
				errores=true;
				log.debug("tiene pagos posteriores: "+huboPagos);
				break;
			}
			//Determinar si la deuda que estaba en Convenio cambio de via
			if (!errores && convenioDeuda.getDeuda().getViaDeuda().getId() != this.getViaDeuda().getId()){
				cambioDeVia = true;
				errores=true;
				log.debug("cambio de via: "+cambioDeVia);
				this.addNonRecoverableError(GdeError.SALPORCADVUELTA_CAMBIODEVIA);
				break;
			}
			//Determinar si la deuda se incluyo en otro convenio
			if (!errores && !cambioDeVia && convenioDeuda.getDeuda().getIdConvenio() !=null){
				log.debug("esta en otro convenio: "+cambioDeVia);
				errores=true;
				this.addNonRecoverableError(GdeError.SALPORCADVUELTA_INCLUIDAOTROCONV);
				break;
			}
			if (!errores){
				Long idEstadoDeuda = convenioDeuda.getDeuda().getEstadoDeuda().getId(); 
				if (idEstadoDeuda == EstadoDeuda.ID_ANULADA
						|| idEstadoDeuda == EstadoDeuda.ID_CONDONADA
						|| idEstadoDeuda == EstadoDeuda.ID_PRESCRIPTA){
					this.addNonRecoverableError(GdeError.SALPORCADVUELTA_ESTADO_DEUDA_INCORRECTO);
					errores=true;
					break;
				}
			}
		}
				
		//Realizar la Vuelta Atras
		if (!errores){
			log.debug("No tienen errores las deudas..............");
			EstadoDeuda estadoDeudaOrig;
			//Modifica el Estado de la Deuda a la Original
			Session session= SiatHibernateUtil.currentSession();
			for (ConvenioDeuda convenioDeuda: listConvenioDeuda){
				deuda = convenioDeuda.getDeuda();
				log.debug("Obtuvo la deuda.........");
				//Obtengo el Estado de la Deuda a travï¿½s de la ViaDeuda del Convenio
				if (this.getViaDeuda().getId().longValue()== ViaDeuda.ID_VIA_ADMIN){
					estadoDeudaOrig = EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA);
				}else{
					estadoDeudaOrig = EstadoDeuda.getById(EstadoDeuda.ID_JUDICIAL);
				}
				//Si el Estado es Cancelado se modifica el Estado Deuda y si se movio el registro de tabla lo vuelvo atras
				log.debug("Deuda "+deuda.getId()+"estado="+deuda.getEstadoDeuda().getId());
				if (deuda.getEstadoDeuda().getId() == EstadoDeuda.ID_CANCELADA){
					log.debug("Esta Cancelada.........");
					if (DeudaCancelada.getByIdNullUniqueTable(convenioDeuda.getDeuda().getId(),EstadoDeuda.ID_CANCELADA)!=null){
						GdeGDeudaManager.getInstance().moverDeuda(deuda, EstadoDeuda.getById(EstadoDeuda.ID_CANCELADA), estadoDeudaOrig);
						log.debug("Movi la deuda .........");
					}
				}
				session.flush();
				//Obtengo la deuda modificada por si cambiï¿½ de tabla
				Deuda deudaModificada = Deuda.getById(deuda.getId());
				deudaModificada.setEstadoDeuda(estadoDeudaOrig);
				//Vuelvo atras los datos de la Deuda
				deudaModificada.setFechaPago(null);
				deudaModificada.setSaldo(convenioDeuda.getCapitalOriginal());
				deudaModificada.setConvenio(this);
				deudaModificada.setIdConvenio(this.getId());
				//Verifico si la Deuda tenia un importe de actualizacion anterior al convenio si fue modificada por el mismo
				deudaModificada.setActualizacion(null);
				//Guardo la vuelta atras de la Deuda
				log.debug("ID Convenio " + deudaModificada.getIdConvenio()+" ID Deuda "+deudaModificada.getId()+ " estado: "+deudaModificada.getEstadoDeuda().getId());
				GdeGDeudaManager.getInstance().update(deudaModificada);
				
				session.flush();
				log.debug("ID Convenio " + deudaModificada.getIdConvenio()+" ID Deuda "+deudaModificada.getId());
			}
			//Anulo los Pagos realizados por el Saldo por Caducidad si hubo
			if(this.registraPagos()){
				GdeDAOFactory.getPagoDeudaDAO().deletePagosDeConvenio(this.getId());
			}
			
			//Borro las relaciones creadas en ConDeuCuo por Pagos A Cuenta
			if (this.tienePagoCue()){
				borrarConDeuCuoParaPagoACuenta();
			}
			
			//Vuelvo al convenio a Vigente
			this.setEstadoConvenio(EstadoConvenio.getById(EstadoConvenio.ID_VIGENTE));
			GdeDAOFactory.getConvenioDAO().update(this);
			
			//Agrego el cambio de Estado al Convenio
			conEstCon = new ConEstCon();
			conEstCon.setConvenio(this);
			conEstCon.setEstadoConvenio(EstadoConvenio.getById(EstadoConvenio.ID_VIGENTE));
			conEstCon.setFechaConEstCon(new Date());
			conEstCon.setObservacion("Convenio Rehabilitado. " + observacion);
			conEstCon.setIdCaso(caso.getIdFormateado());
			
			GdeDAOFactory.getConEstConDAO().update(conEstCon);
			
		}
		
		return conEstCon;
	}
	
	/**
	 * Realiza el reasentamiento de una Cuota, sin distribuir partidas, como Pago a Cuenta
	 * @param auxPagoCuenta
	 */
	public void reasentarPagoACuenta (AuxAplPagCue auxPagoCuenta){
		ConvenioCuota convenioCuota = auxPagoCuenta.getConvenioCuota();
		convenioCuota.setTipoPago(auxPagoCuenta.getTipoPago());
		convenioCuota.setIdPago(auxPagoCuenta.getIdPago());
		convenioCuota.setFechaPago(auxPagoCuenta.getFechaPago());
		convenioCuota.setBancoPago(auxPagoCuenta.getBancoPago());
		convenioCuota.setActualizacion(auxPagoCuenta.getActualizacion());
		convenioCuota.setAsentamiento(auxPagoCuenta.getAsentamiento());
		convenioCuota.setEstadoConCuo(EstadoConCuo.getById(EstadoConCuo.ID_PAGO_A_CUENTA));
		convenioCuota.setCodPago(auxPagoCuenta.getCodPago());
		GdeDAOFactory.getConvenioCuotaDAO().update(convenioCuota);
	}
	/**
	 * Realiza el reasentamiento de una Cuota, sin distribuir partidas, como Pago Bueno
	 * @param auxPagoBueno
	 */
	public void reasentarPagoBueno (AuxAplPagCue auxPagoBueno)throws Exception{
		Log log = LogFactory.getLog(Convenio.class);
		Integer cuotaAImputar = 1;
		
		
		ConvenioCuota convenioCuota = auxPagoBueno.getConvenioCuota();
		log.debug("CONVENIO CUOTA ORIG"+ convenioCuota.getNumeroCuota());
		if (convenioCuota.getNumeroCuota()!= 1 && this.getUltCuoImp()>0){
			cuotaAImputar = this.getUltCuoImp()+1;
		}else if (convenioCuota.getNumeroCuota()!=1){
			cuotaAImputar = 2;
		}
		log.debug("cuota a imputar "+cuotaAImputar);
		ConvenioCuota convenioCuotaImputar = this.getListConvenioCuota().get(cuotaAImputar-1);
		log.debug("CONVENIO CUOTA IMPUTAR "+ convenioCuotaImputar.getNumeroCuota());
		Double capitalAAsentar = convenioCuotaImputar.getCapitalCuota();
		Double saldoEnPlanCub;
		Integer esPagoTotal = 0;
		convenioCuota.setTipoPago(auxPagoBueno.getTipoPago());
		convenioCuota.setIdPago(auxPagoBueno.getIdPago());
		convenioCuota.setFechaPago(auxPagoBueno.getFechaPago());
		convenioCuota.setBancoPago(auxPagoBueno.getBancoPago());
		convenioCuota.setActualizacion(auxPagoBueno.getActualizacion());
		convenioCuota.setCodPago(auxPagoBueno.getCodPago());
		convenioCuota.setAsentamiento(auxPagoBueno.getAsentamiento());
		convenioCuota.setEstadoConCuo(EstadoConCuo.getById(EstadoConCuo.ID_PAGO_BUENO));
		log.debug("CUOTA ACTUALIZADA PAGO BUENO: "+convenioCuota.getNumeroCuota());
		log.debug("CON FECHAPAGO: "+auxPagoBueno.getFechaPago());
		convenioCuota.setNroCuotaImputada(cuotaAImputar);
		GdeDAOFactory.getConvenioCuotaDAO().update(convenioCuota);
		Double saldoEnPlan;
		try {
			while (capitalAAsentar > 0){
				log.debug("CONVENIO NRO: "+this.getNroConvenio() + " ID: "+this.getId());
				log.debug("CAPITAL A ASENTAR: "+capitalAAsentar);
				ConvenioDeuda convenioDeuda = GdeDAOFactory.getConvenioDeudaDAO().getDeudaMasAntiguaACancelar(this);
				if (convenioDeuda == null){
					if (capitalAAsentar < 0.5){
						capitalAAsentar=0D;
						break;
					}else{
						this.addRecoverableValueError("No se pudo obtener deuda para cancelar capital de cuota");
					}
				}
				log.debug("DEUDA OBTENIDA: "+convenioDeuda.getDeuda().getPeriodo()+" "+convenioDeuda.getDeuda().getAnio());
				log.debug("SALDO EN PLAN: "+convenioDeuda.getSaldoEnPlan());
				if (capitalAAsentar > convenioDeuda.getSaldoEnPlan()){
					saldoEnPlanCub = convenioDeuda.getSaldoEnPlan();
					convenioDeuda.setSaldoEnPlan(0D);
				}else{
					saldoEnPlanCub = capitalAAsentar;
					saldoEnPlan =NumberUtil.truncate(convenioDeuda.getSaldoEnPlan() - saldoEnPlanCub,SiatParam.DEC_IMPORTE_DB);
					convenioDeuda.setSaldoEnPlan(saldoEnPlan);
				}
				convenioDeuda.setFechaPago(auxPagoBueno.getFechaPago());
				ConDeuCuo conDeuCuo = new ConDeuCuo();
				conDeuCuo.setConvenioCuota(convenioCuota);
				conDeuCuo.setConvenioDeuda(convenioDeuda);
				conDeuCuo.setSaldoEnPlanCub(NumberUtil.truncate(saldoEnPlanCub, SiatParam.DEC_IMPORTE_DB));
				if (convenioDeuda.getDeuda().getImporte()<=saldoEnPlanCub){
					esPagoTotal = 1;
				}else{
					esPagoTotal = 0;
				}
				conDeuCuo.setEsPagoTotal(esPagoTotal);
				GdeDAOFactory.getConvenioDeudaDAO().update(convenioDeuda);
				GdeDAOFactory.getConDeuCuoDAO().update(conDeuCuo);
				capitalAAsentar = NumberUtil.truncate(capitalAAsentar - saldoEnPlanCub,SiatParam.DEC_IMPORTE_DB);
			}
			this.setUltCuoImp(cuotaAImputar);
			
			GdeDAOFactory.getConvenioDAO().update(this);
		}catch (Exception e){
			this.addNonRecoverableError(e.getMessage());
		}
	}
	
	
	/**
	 * Aplica Pagos a Cuenta en un convenio vigente o caduco, el proceso obtiene el 
	 * primer pago a cuenta, a partir de este vuelve atras el asentamiento de todas las cuotas, ordena las cuotas por fecha de pago
	 * y aplica un reasentamiento especial (sin distribucion de partidas) a las mismas pudiendo revertirse estos Pagos a Cuenta a Pagos Buenos segun los criterios de caducidad
	 * del Plan
	 * Si la aplicacion de pagos a cuenta se llama desde un rescate recibe el idResDet como parametro sino pasar id=0
	 * @param idResDet
	 * @throws Exception
	 */
	public void aplicarPagosACuenta(Long idResDet)throws Exception{
		Log log = LogFactory.getLog(Convenio.class);
	/**	List<ConvenioCuota>pagosACuenta = GdeDAOFactory.getConvenioCuotaDAO().getListPagoACuentaByConvenio(this);
		if (pagosACuenta ==null){
			this.addRecoverableError(GdeError.PAGOCUENTA_NOTIENE);
		}
		log.debug("PRIMER PAGO A CUENTA EN CUOTA: "+pagosACuenta.get(0).getNumeroCuota());
		**/
		//List<ConvenioCuota>pagosAReasentar = GdeDAOFactory.getConvenioCuotaDAO().getListPagasByNroCuotaMayorIgualA(this, pagosACuenta.get(0).getNumeroCuota()); A partir del 17/10/08 se vuelven a asentar todos los pagos
		List<ConvenioCuota>pagosAReasentar = GdeDAOFactory.getConvenioCuotaDAO().getListPagasByNroCuotaMayorIgualA(this,1);
		List<AuxAplPagCue>auxPagosAReasentar = new ArrayList<AuxAplPagCue>();
		ResDet resDet=null;
		if (idResDet != 0L){
			resDet = ResDet.getById(idResDet);
		}
		try{
			//copio en tabla auxiliar las cuotas a reasentar
			for (ConvenioCuota convenioCuota:pagosAReasentar){
				AuxAplPagCue auxPagCue = new AuxAplPagCue(convenioCuota);
				if (resDet != null){
					auxPagCue.setResDet(resDet);
				}
				GdeDAOFactory.getAuxAplPagCueDAO().update(auxPagCue);
				auxPagosAReasentar.add(auxPagCue);
			}
			
			//vuelvo atras los cambios realizados por el asentamiento en convenioDeuda, conDeuCuo, convenioCuota y convenio
			log.debug("---------------------------VUELTA ATRAS PAGOS--------------------");
			for (AuxAplPagCue auxPagoCuenta : auxPagosAReasentar){
				ConvenioCuota convenioCuota = auxPagoCuenta.getConvenioCuota();
				log.debug("CUOTA PARA VUELTA ATRAS: "+convenioCuota.getNumeroCuota());
				if (convenioCuota.getEstadoConCuo().getId().longValue() == EstadoConCuo.ID_PAGO_BUENO.longValue()){
					log.debug("DANDO VUELTA PAGO BUENO CUOTA: "+ convenioCuota.getNumeroCuota());
					List<ConDeuCuo> listConDeuCuo = GdeDAOFactory.getConDeuCuoDAO().getConDeuCuoByConvenioCuota(convenioCuota);
					for (ConDeuCuo conDeuCuo : listConDeuCuo){
						GdeDAOFactory.getConDeuCuoDAO().delete(conDeuCuo);
					}
					for (ConvenioDeuda convenioDeuda : getListConvenioDeuda()){
						Double total =0D;
						if (convenioDeuda.getTotalEnPlan()!= null && convenioDeuda.getTotalEnPlan()==0){
							total = convenioDeuda.getTotalEnPlan();
						}else{
							total = convenioDeuda.getCapitalEnPlan()+convenioDeuda.getActEnPlan();
						}
						convenioDeuda.setSaldoEnPlan(total);
						GdeDAOFactory.getConvenioDeudaDAO().update(convenioDeuda);
					}
					this.setUltCuoImp(this.getUltCuoImp()-1);
				}
				log.debug("ACTUALIZANDO CONVENIOCUOTA PARA CUOTA: "+convenioCuota.getNumeroCuota());
				convenioCuota.setFechaPago(null);
				convenioCuota.setNroCuotaImputada(null);
				convenioCuota.setEstadoConCuo(EstadoConCuo.getById(EstadoConCuo.ID_IMPAGO));
				convenioCuota.setBancoPago(null);
				convenioCuota.setIdPago(null);
				convenioCuota.setCodPago(null);
				convenioCuota.setAsentamiento(null);
				GdeDAOFactory.getConvenioCuotaDAO().update(convenioCuota);
				if(!this.tienePagosBuenos()&& this.ultCuoImp>0){
					this.setUltCuoImp(0);
				}
				GdeDAOFactory.getConvenioDAO().update(this);
				
			}
			
			log.debug("ULTIMACUOIMPUTADA: "+this.getUltCuoImp());
			log.debug("---------------------------APLICANDO PAGOS--------------------");
			//Se obtienen los registros de la tabla auxiliar y se procede a reasentarlos
			auxPagosAReasentar = new ArrayList<AuxAplPagCue>();
			auxPagosAReasentar = AuxAplPagCue.getListByIdConvenio(this.getId());
			for (AuxAplPagCue auxPagoCuenta : auxPagosAReasentar){
				boolean estaCaduco=false;
				
				if (this.getPlan().getAplicaPagCue()==null || this.getPlan().getAplicaPagCue().intValue()== SiNo.SI.getId().intValue()){
					estaCaduco = this.estaCaduco(auxPagoCuenta.getFechaPago());
				}
				
				if (estaCaduco){
					reasentarPagoACuenta (auxPagoCuenta);
				}else{
					reasentarPagoBueno (auxPagoCuenta);
				}
				GdeDAOFactory.getAuxAplPagCueDAO().delete(auxPagoCuenta);
			}
			String obs="";
			ConEstCon conEstCon = new ConEstCon();
			conEstCon.setFechaConEstCon(new Date());
			conEstCon.setEstadoConvenio(this.getEstadoConvenio());
			conEstCon.setObservacion("Se Reasentaron los Pagos del Convenio");
			conEstCon.setConvenio(this);
			GdeDAOFactory.getConEstConDAO().update(conEstCon);
			log.debug("HAY QUE CANCELAR: "+tieneTodasCuotasPagosBuenos());
			if (tieneTodasCuotasPagosBuenos() && this.getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_VIGENTE){
				if (resDet != null){
					obs = "Convenio Cancelado durante un Proceso de Rescate";
				}else{
					obs="Convenio Cancelado por aplicar Pagos a Cuenta";
				}
				cancelarConvenio(obs);
			}
		}catch (Exception e){
			this.addNonRecoverableError(e.getMessage());
			log.debug(e.getMessage());
		}

		
		
	}

	
	public void cancelarConvenio(String observacion) throws Exception {
		// Cambiar el estado del convenio a cancelado
		EstadoConvenio estadoConvenio = EstadoConvenio.getById(EstadoConvenio.ID_CANCELADO);
		setEstadoConvenio(estadoConvenio);
		GdeDAOFactory.getConvenioDAO().update(this);
		Log log = LogFactory.getLog(Convenio.class);
		try{
			// Registrar el cambio de estado en el log de cambio
			ConEstCon conEstCon = new ConEstCon();
			conEstCon.setConvenio(this);
			conEstCon.setEstadoConvenio(estadoConvenio);
			conEstCon.setFechaConEstCon(new Date());
			conEstCon.setIdCaso(null);
			conEstCon.setObservacion(observacion);
			GdeDAOFactory.getConEstConDAO().update(conEstCon);
			
			// Obtener la deuda incluida en el convenio. Y por cada Deuda:
			List<ConstanciaDeu> listConstanciaDeu = new ArrayList<ConstanciaDeu>();
			for(ConvenioDeuda convenioDeuda: this.getListConvenioDeuda()){
				Deuda deuda = convenioDeuda.getDeuda();
				Double importe = deuda.getSaldo();
				// Mover la Deuda a "Cancelada" (Por ahora solo actualizamos la deuda en deuAdmin o deudaJudicial y le cambiamos el estado a cancelada)
				EstadoDeuda estadoDeuda = EstadoDeuda.getById(EstadoDeuda.ID_CANCELADA);
				if(deuda.getRecurso().getEsAutoliquidable().intValue() == SiNo.NO.getId().intValue()){				
					deuda.setEstadoDeuda(estadoDeuda);
				}
				if(deuda.getActualizacion()!=null){
					deuda.setActualizacion(deuda.getActualizacion()+convenioDeuda.getActEnPlan());
				}else{
					deuda.setActualizacion(convenioDeuda.getActEnPlan());
				}
				deuda.setFechaPago(convenioDeuda.getFechaPago());
				deuda.setSaldo(0D);
				deuda.setConvenio(null);
				if(deuda.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_ADMIN)
					GdeDAOFactory.getDeudaAdminDAO().update(deuda);
				else if(deuda.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL)
					GdeDAOFactory.getDeudaJudicialDAO().update(deuda);
				
				// Registrar el PagoDeuda
				PagoDeuda pagoDeuda = new PagoDeuda();
				pagoDeuda.setIdDeuda(deuda.getId());
				TipoPago tipoPago = TipoPago.getById(TipoPago.ID_PAGO_BUENO);
				pagoDeuda.setTipoPago(tipoPago);
				pagoDeuda.setIdPago(this.getId());
				log.debug("DEUDA PAGA: "+convenioDeuda.getDeuda().getPeriodo()+"/"+convenioDeuda.getDeuda().getAnio());
				log.debug ("FECHA PAGO: "+convenioDeuda.getFechaPago());
				pagoDeuda.setFechaPago(convenioDeuda.getFechaPago());
				pagoDeuda.setImporte(importe);
				pagoDeuda.setActualizacion(convenioDeuda.getActEnPlan());
				pagoDeuda.setBancoPago(null);
				pagoDeuda.setIdCaso(null);
				Integer esPagoTotal=SiNo.NO.getId();
				if (ConDeuCuo.getEsPagoTotalByIdConvenioDeuda(convenioDeuda.getId())){
					esPagoTotal = SiNo.SI.getId();
				}
				pagoDeuda.setEsPagoTotal(esPagoTotal);
				
				GdeDAOFactory.getPagoDeudaDAO().update(pagoDeuda);
		
				/* Se comenta, por pedido de Sistemas MR, no por no usarse esta funcionalidad y ocasionar problemas
				 * en constancias muy grandes como las de tgi.
				 */
				/*
				// Si la Deuda se encuentra en Via Judicial, se debe actualizar la constancia.
				if(deuda.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL){
					ConstanciaDeu constanciaDeu = this.actualizarConstancia(deuda);
					if(constanciaDeu != null && !listConstanciaDeu.contains(constanciaDeu)){
						listConstanciaDeu.add(constanciaDeu);					
					}
				}
				*/
		
			}
			/* Se comenta, por pedido de Sistemas MR, no por no usarse esta funcionalidad y ocasionar problemas
			 * en constancias muy grandes como las de tgi.
			 */
			/*
			// Al finalizar recorro la lista de Constancias Deu actualizadas.
			for(ConstanciaDeu constanciaDeu: listConstanciaDeu){
				if(!constanciaDeu.tieneConDeuDetActivos()){
					constanciaDeu.cambiarEstConDeu(EstConDeu.ID_CANCELADA, "Detalle de Deuda totalmente cancelado durante el Asentamiento");
				}else{
					constanciaDeu.cambiarEstConDeu(EstConDeu.ID_MODIFICADA, "Detalle de Deuda parcialmente cancelado durante el Asentamiento");
				}
			}
			*/
		}catch (Exception e){
				this.addNonRecoverableError(e.getMessage());
		}
}
	
	/**
	 * Calcula el Total del Capital del Convenio aplicando Descuentos de capital y actualizacion.
	 * 
	 * @author Cristian
	 * @return
	 */
	public Double calcularTotCapDesc(){
		Double totCapOri = getTotCapitalOriginal() == null ? 0D : getTotCapitalOriginal();
		Double decCapOri = getDesCapitalOriginal() == null ? 0D : getDesCapitalOriginal();
		Double totAct = getTotActualizacion() == null ? 0D : getTotActualizacion(); 
		Double decAct = getDesActualizacion() == null ? 0D : getDesActualizacion();

		return totCapOri - decCapOri + totAct - decAct ;
	}
	
	
	public void	createConvenioDeuda(ConvenioDeuda convenioDeuda){				
		convenioDeuda.setConvenio(this);
		GdeDAOFactory.getConvenioDeudaDAO().update(convenioDeuda);		
	}
	
	public void	createConvenioCuota(ConvenioCuota convenioCuota){		
		
		convenioCuota.setConvenio(this);
		
		convenioCuota.setCodRefPag(convenioCuota.obtenerCodRefPag());		
		
		/*if (convenioCuota.getNumeroCuota().intValue() <= this.getPlan().getCanCuoAImpEnForm().intValue()) 
			convenioCuota.setFechaImpresion(this.getFechaFor());*/
		
		convenioCuota.setSistema(this.getSistema());
		convenioCuota.setActualizacion(0D);
		convenioCuota.setFechaEmision(this.getFechaFor());
		convenioCuota.setNroCuotaImputada(0);
		convenioCuota.setReclamada(0);
		
		GdeDAOFactory.getConvenioCuotaDAO().update(convenioCuota);		
	}
	
	public boolean	updateConvenioCuota(ConvenioCuota convenioCuota) throws Exception{		
		
		if(!convenioCuota.validateUpdate())
			return false;
		
		GdeDAOFactory.getConvenioCuotaDAO().update(convenioCuota);
		return true;
	}	
	
	public Double calcularDescCapital(){
		return getDesCapitalOriginal()/getTotCapitalOriginal();
	}
	public Double calcularDescActualizacion(){
		return getDesActualizacion()/getTotActualizacion();
	}
	
	
	/**
	 * toVO liviano para la busqueda de convenios
	 * 
	 *  convenio / plan / via y cuenta
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ConvenioVO toVOForSearch() throws Exception{
		
		ConvenioVO convenioVO = (ConvenioVO) this.toVO(0);
		
		convenioVO.setPlan(this.getPlan().toVOForSearch());
		
		convenioVO.setViaDeuda(convenioVO.getPlan().getViaDeuda());
		
		convenioVO.setCuenta((CuentaVO)this.getCuenta().toVO(0, false));
		
		return convenioVO;
		
	}

	/**
	 * Obtiene la lista de Cuotas de Deuda VO
	 * Metodo factorizado utilizado directamente por los Servicios:
	 * 		GdePlanPagoServiceHbmImpl
	 * 		GdeGDeudaServiceHbmImpl
	 * 
	 * @return List<CuotaDeudaVO>
	 * @throws Exception
	 */
	public List<CuotaDeudaVO> getListCuotaDeudaVO() throws Exception{

		List<ConvenioCuota> listConvenioCuota = GdeDAOFactory.getConvenioCuotaDAO().getPagosBuenosbyIdConvenio(this.getId());
		
		List<ConDeuCuo>listConDeuCuo = new ArrayList<ConDeuCuo>();
		for (ConvenioCuota convenioCuota: listConvenioCuota){
			listConDeuCuo.addAll(GdeDAOFactory.getConDeuCuoDAO().getConDeuCuoByConvenioCuotaOrderByConvenioDeuda(convenioCuota));
		}
		
		List<CuotaDeudaVO>listCuotaDeuda = new ArrayList<CuotaDeudaVO>();
		int i = -1;
		double saldoDeuda=0D;
		//Para Pagos Buenos
		
		for (ConDeuCuo conDeuCuo : listConDeuCuo){
			CuotaDeudaVO cuotaDeudaVO = new CuotaDeudaVO();
			ConvenioCuota convenioCuota = conDeuCuo.getConvenioCuota();
			List<ConvenioCuota>listTodosConvenioCuota = this.getListConvenioCuota();
			log.debug("CUOTA: "+convenioCuota.getNumeroCuota());
			Deuda deuda = conDeuCuo.getConvenioDeuda().getDeuda();
			log.debug("DEUDA: "+deuda.getPeriodo()+"/"+deuda.getAnio());
			if (i==-1 || conDeuCuo.getConvenioDeuda().getId().longValue()!= listConDeuCuo.get(i).getConvenioDeuda().getId().longValue()){
				saldoDeuda = conDeuCuo.getConvenioDeuda().getCapitalEnPlan()+conDeuCuo.getConvenioDeuda().getActEnPlan();
			}
			log.debug("SALDO: "+saldoDeuda);
			cuotaDeudaVO.setNroCuota(convenioCuota.getNumeroCuota().toString());
			cuotaDeudaVO.setNroCuotaImputada(convenioCuota.getNroCuotaImputada().toString());
			cuotaDeudaVO.setFechaPago(DateUtil.formatDate(convenioCuota.getFechaPago(), DateUtil.ddSMMSYYYY_MASK));
			cuotaDeudaVO.setFechaVto(DateUtil.formatDate(convenioCuota.getFechaVencimiento(),DateUtil.ddSMMSYYYY_MASK));
			if (convenioCuota.getNroCuotaImputada().intValue()==convenioCuota.getNumeroCuota().intValue()){
				cuotaDeudaVO.setImporteCuota(convenioCuota.getImporteCuota());
				cuotaDeudaVO.setCapital(convenioCuota.getCapitalCuota());
				cuotaDeudaVO.setInteres(convenioCuota.getInteres());
			}else{
				ConvenioCuota convenioCuotaImputada = listTodosConvenioCuota.get(convenioCuota.getNroCuotaImputada()-1);
				cuotaDeudaVO.setImporteCuota(convenioCuotaImputada.getImporteCuota());
				cuotaDeudaVO.setCapital(convenioCuotaImputada.getCapitalCuota());
				cuotaDeudaVO.setInteres(convenioCuotaImputada.getInteres());
			}


			log.debug("SETEADA LA CUOTA");
			cuotaDeudaVO.setPeriodo(deuda.getPeriodo());
			cuotaDeudaVO.setAnio(deuda.getAnio());
			cuotaDeudaVO.setRecClaDeu((RecClaDeuVO) conDeuCuo.getConvenioDeuda().getDeuda().getRecClaDeu().toVO());
			cuotaDeudaVO.setFechaVencimiento(deuda.getFechaVencimiento());

			cuotaDeudaVO.setActualizacion(conDeuCuo.getConvenioDeuda().getActEnPlan());
			cuotaDeudaVO.setSaldoEnPlanCub(conDeuCuo.getSaldoEnPlanCub());
			cuotaDeudaVO.setTotal(conDeuCuo.getConvenioDeuda().getActEnPlan()+conDeuCuo.getConvenioDeuda().getCapitalEnPlan());
			
			saldoDeuda = saldoDeuda - conDeuCuo.getSaldoEnPlanCub();
			if (saldoDeuda <0)saldoDeuda=0D;
			cuotaDeudaVO.setSaldo(saldoDeuda);
			log.debug("SETEADA LA DEUDA");
			listCuotaDeuda.add(cuotaDeudaVO);
			i++;
		}
		
		//Para Pagos a Cuenta
		listConvenioCuota.clear();
		listConDeuCuo.clear();
		
		if(this.getEstadoConvenio().equals(EstadoConvenio.getById(EstadoConvenio.ID_RECOMPUESTO))&& this.tienePagoCue()){
			log.debug("RECOMPUESTO ");
			listConvenioCuota.addAll(GdeDAOFactory.getConvenioCuotaDAO().getListPagoACuentaByConvenioOrderByFechaPago(this));
			log.debug("LISTA PAGOS A CUENTA: "+listConvenioCuota.size());
			for (ConvenioCuota convenioCuota: listConvenioCuota){
				listConDeuCuo.addAll(GdeDAOFactory.getConDeuCuoDAO().getConDeuCuoByConvenioCuotaOrderByConvenioDeuda(convenioCuota));
			}
			List<PagoDeuda>listPagoDeuda =GdeDAOFactory.getPagoDeudaDAO().getByConvenioyPagoCuenta(this);
			i=0;
			
			for (ConDeuCuo conDeuCuo : listConDeuCuo){
				CuotaDeudaVO cuotaDeudaVO = new CuotaDeudaVO();
				ConvenioCuota convenioCuota = conDeuCuo.getConvenioCuota();
				ConvenioDeuda convenioDeuda = conDeuCuo.getConvenioDeuda();
				Deuda deuda = convenioDeuda.getDeuda();
				
				//int sizePagoDeuda = listPagoDeuda.size();

				log.debug("SALDO: "+saldoDeuda);
				cuotaDeudaVO.setNroCuota(convenioCuota.getNumeroCuota().toString());
				cuotaDeudaVO.setFechaPago(DateUtil.formatDate(convenioCuota.getFechaPago(), DateUtil.ddSMMSYYYY_MASK));
				cuotaDeudaVO.setFechaVto(DateUtil.formatDate(convenioCuota.getFechaVencimiento(),DateUtil.ddSMMSYYYY_MASK));
				cuotaDeudaVO.setImporteCuota(convenioCuota.getImporteCuota());
				Double actualizacion=0D;
				if (convenioCuota.getActualizacion()!=null){
					actualizacion = convenioCuota.getActualizacion();
				}
				cuotaDeudaVO.setCapital(convenioCuota.getCapitalCuota()+actualizacion+convenioCuota.getInteres());
				cuotaDeudaVO.setInteres(convenioCuota.getInteres());
				cuotaDeudaVO.setNroCuotaImputada("-");
				cuotaDeudaVO.setRecClaDeu((RecClaDeuVO) convenioDeuda.getDeuda().getRecClaDeu().toVO());
				cuotaDeudaVO.setPeriodo(deuda.getPeriodo());
				cuotaDeudaVO.setAnio(deuda.getAnio());
				cuotaDeudaVO.setFechaVencimiento(deuda.getFechaVencimiento());
				Double total = convenioDeuda.getSaldoEnPlan()*(convenioDeuda.getCapitalEnPlan()/(convenioDeuda.getCapitalEnPlan()+convenioDeuda.getActEnPlan()));
				total = NumberUtil.round(total, SiatParam.DEC_IMPORTE_CALC);
				//cuotaDeudaVO.setActualizacion(conDeuCuo.getConvenioDeuda().getActEnPlan());
				cuotaDeudaVO.setSaldoEnPlanCub(conDeuCuo.getSaldoEnPlanCub());
				//cuotaDeudaVO.setTotal(ActualizaDeuda.actualizar(deuda.getFechaVencimiento(), total).getImporteAct());
				if (i<=listPagoDeuda.size()-1){
					if (convenioDeuda.getDeuda().getId().longValue()== listPagoDeuda.get(i).getIdDeuda().longValue()){
						cuotaDeudaVO.setSaldo(listPagoDeuda.get(i).getImporte());
						Double saldo = deuda.getSaldo();
						for (PagoDeuda pagoDeuda : listPagoDeuda){
							if(pagoDeuda.getIdDeuda().longValue()== deuda.getId()){
								saldo += pagoDeuda.getImporte();
							}
						}
						cuotaDeudaVO.setTotal(saldo);
					}
				}else{
					cuotaDeudaVO.setTotal(null);
					cuotaDeudaVO.setSaldo(null);
				}

				log.debug("SETEADA LA DEUDA");
				listCuotaDeuda.add(cuotaDeudaVO);
				i++;
			}
		}
		
		return listCuotaDeuda;
	}

	@Override
	public String infoString() {
		String ret =" Convenio";
		
		if(nroConvenio!=null){
			ret+=" - Numero: "+nroConvenio;
		}
		
		if(plan!=null){
			ret+=" - Plan: "+plan.getDesPlan();
		}

		if(cuenta!=null){
			ret+=" - para la cuenta: "+cuenta.getNumeroCuenta();
		}

		if(recurso!=null){
			ret+=" - recurso: "+recurso.getDesRecurso();
		}
		
		if(procurador!=null){
			ret+=" - procurador: "+procurador.getDescripcion();
		}
		
		return ret;
	}

	
	/**
	 *  Devuelve true si el convenio es exento (por posee exencion)
	 *  o si la cuenta tiene exencion o la tuvo a la fecha de fencimiento de la cuota.
	 * 
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
    public Boolean exentoActualizacion(Date fechaAnalisis)throws Exception{
    	
    	List<PlanExe> listPlanExe =  GdeDAOFactory.getPlanExeDAO().getListNoActDeudaByPlan(this.getPlan());
    	
    	if (listPlanExe != null && listPlanExe.size() > 0){
    		log.debug(" ### exentoActualizacion - convenio. " + this.getNroConvenio() + " posee exencion que no actualiza deuda" );
    		return true;
    	} else {
    		
    		boolean exencionesVigentesCuenta = false;
    		
    		if (this.getCuenta() != null)
    			exencionesVigentesCuenta = this.getCuenta().exentaActualizacion(fechaAnalisis, null);
    		
    		log.debug(" ### exentoActualizacion - checkeando exenciones vigentes en la cuenta: " + exencionesVigentesCuenta);
    		
    		// Se le pregunta a la cuenta si posee una exencion vigente a la fecha de hoy
    		// llamando a la funcion con "fechaVencimietno=null"
    		return exencionesVigentesCuenta;
    	}
    }
    
	public static AuxConvenioFormReport generarPdfForReport(AuxConvenioFormReport auxConvenioFormReport) throws Exception{
		// A partir de la implementacion de Adp en la generacion de Reporte se tuvo que utilizar una clase auxiliar
		// para mantener el codigo de generacion existente. Entonces en este punto se crea un convenioFormReport y
		// se pasan los datos necesarios.
		ConvenioFormReport convenioFormReport = new ConvenioFormReport();
		convenioFormReport.setFechaConvenioDesde(auxConvenioFormReport.getFechaConvenioDesde());
		convenioFormReport.setFechaConvenioHasta(auxConvenioFormReport.getFechaConvenioHasta());

		if(auxConvenioFormReport.getViaDeuda() != null){
			convenioFormReport.getConvenio().setViaDeuda((ViaDeudaVO) auxConvenioFormReport.getViaDeuda().toVO(0, false));
		}else{
			convenioFormReport.getConvenio().setViaDeuda(new ViaDeudaVO());
			convenioFormReport.getConvenio().getViaDeuda().setId(-1L);
		}
		
		if(auxConvenioFormReport.getProcurador() != null && !auxConvenioFormReport.getProcurador().equals(-1L)){
			convenioFormReport.getConvenio().setProcurador((ProcuradorVO) auxConvenioFormReport.getProcurador().toVO(0, false));
		}else{
			convenioFormReport.getConvenio().setProcurador(new ProcuradorVO());
			convenioFormReport.getConvenio().getProcurador().setId(-1L);
		}
		
		// en este punto se termino de armar el ConvenioFormReport que antes se pasaba como parametro. 
	
		
		Long idViaDeuda = convenioFormReport.getConvenio().getViaDeuda().getId();
		Date fechaDesde = convenioFormReport.getFechaConvenioDesde();
		Date fechaHasta = convenioFormReport.getFechaConvenioHasta();
		Long idProcurador = convenioFormReport.getConvenio().getProcurador().getId();
		
		/* 
		  
		 Recorremos (en forma paginada) los convenios que cumplen los criterios 
		 Acumulamos las cantidades , anticipos y resto, segun formapago (cantidadcuotasplan: contado = 1, financiado > 1 )  
		 en el lugar del mapa que corresponda segun: idPlan, idArea, idOficina, formaPago
		 
		- Obtenemos la lista de Planes-Recurso, Areas y Oficinas
		- Recorremos el mapa y llenamos la lista de PlanReport de ConvenioFormReport
		- Le pasamos la informacion a un printModel para que la renderize
		- Guardamos el archivo
		- Seteamos el path en planillaVO
		
		// Estructua del contenedor del reporte
	    List<PlanReport>
			PlanReport
				- idPlan
				- desPlan (Plan - Recurso)
				List<AreaReport>
					AreaReport
						- idArea
						- desArea - (Area - Oficina)
						List<TotalesConvenio>
							TotalesConvenioReport
							- desFormaPago (Contado - Financiado)
							- cantidad
							- anticipo
							- resto

		*/		
		
		String contado = "Contado";
		String financiado = "Financiado";



		// obtiene la lista de Resultado de cuentas
		List<Object[]> listTotalesConvenios = GdeDAOFactory.getConvenioDAO().getConveniosFormReport(idViaDeuda, fechaDesde, fechaHasta, idProcurador);


		HashMap<String, HashMap<String, HashMap<String, HashMap<String, TotalesConvenioReport>>>> procuradores = new HashMap();

		for (Object[] reg:listTotalesConvenios){

			/*	
				reg[0] = idplan;
				reg[1] = idrecurso;
				reg[2] = idarea;
				reg[3] = idoficina;
				reg[4] = idProdurador;
				reg[5] = formaPago;
				reg[6] = cantidad;
				reg[7] = anticipo;
				reg[8] = resto;	
			 */

			Long idPlan = (Long)reg[0];
			Long idRecurso = (Long)reg[1];
			Long idArea = (Long)reg[2];
			Long idOficina = reg[3]!= null?(Long)reg[3]:null;
			Long idProc = (Long)reg[4];

			String keyProcurador = idProc + ""; 
			String keyPlanRecurso = idPlan + "-" + idRecurso;
			String keyAreaOficina = idArea + "-" + idOficina;


			String formaPago = contado;

			if (((Integer)reg[5]).intValue() == 1 )
				formaPago = contado;
			else 
				formaPago = financiado;

			Integer cantidad = (Integer)reg[6];
			Double anticipo = (Double)reg[7];
			Double resto = (Double)reg[8];

			//         idPlan-idRecurso        idArea-idOficina   procurador     formaPago -> TotalesConvenio (cantidad, anticipo, resto)
			// HashMap<String, HashMap<String, HashMap<String, HashMap<String, TotalesConvenio>>> planes

			HashMap<String, HashMap<String, HashMap<String, TotalesConvenioReport>>> planes = null;
			HashMap<String, HashMap<String, TotalesConvenioReport>> areas= null;
			HashMap<String,TotalesConvenioReport> totales = null;
			TotalesConvenioReport totalConvenio = null;

			planes = procuradores.get(keyProcurador);

			if (planes == null){
				planes = new HashMap<String, HashMap<String, HashMap<String,TotalesConvenioReport>>>();
			} 

			areas = planes.get(keyPlanRecurso);

			if (areas == null){
				areas = new HashMap<String, HashMap<String,TotalesConvenioReport>>();
			} 

			totales = areas.get(keyAreaOficina);

			if (totales == null){
				totales = new HashMap<String,TotalesConvenioReport>();
			} 

			totalConvenio = totales.get(formaPago);

			if (totalConvenio == null){
				totalConvenio = new TotalesConvenioReport();
			} 

			totalConvenio.setDesFormaPago(formaPago);
			totalConvenio.setCantidad(cantidad);
			totalConvenio.setAnticipo(anticipo);
			totalConvenio.setResto(resto);

			totales.put(formaPago, totalConvenio);
			areas.put(keyAreaOficina, totales);
			planes.put(keyPlanRecurso, areas);
			procuradores.put(keyProcurador, planes);

		}
		System.out.println("Log del mapa de procuradores obtenido: ################## ");
		// Solo para Log
		for (String keyProcuradores:procuradores.keySet()){
			System.out.println("# keyProcuradores: " + keyProcuradores);
			HashMap<String, HashMap<String, HashMap<String, TotalesConvenioReport>>> planes = procuradores.get(keyProcuradores);

			for (String keyPlanRecurso: planes.keySet()){
				System.out.println("# 	-> keyPlanRecurso: " + keyPlanRecurso);
				HashMap<String, HashMap<String, TotalesConvenioReport>> areas = planes.get(keyPlanRecurso);
				for (String keyAreaOficina: areas.keySet() ){
					System.out.println("# 	-> keyAreaOficina: " + keyAreaOficina);
					HashMap<String, TotalesConvenioReport> totales = areas.get(keyAreaOficina);
					for (String formaPago: totales.keySet() ){

						System.out.println("# 		-> forma de Pago: " + formaPago);
						TotalesConvenioReport totalConvenio = totales.get(formaPago);
						System.out.println("# 			-> totalConvenio - cantidad :" + totalConvenio.getCantidad() +
								" anticipo: " + totalConvenio.getAnticipo() +
								" resto: " + totalConvenio.getResto());
					}
				}
			}
		}

		System.out.println("Populado del reporte: ################## ");

		// ordenamiento de las claves
		Object[] claves = procuradores.keySet().toArray();
		Arrays.sort(claves);

		System.out.println(" ####### claves.class " + claves.getClass().getName());

		// Populado del reporte
		// 
		for (Object obj:(Object[])claves){

			String keyProcuradores = (String)obj;

			System.out.println("# keyProcuradores: " + keyProcuradores);


			Long idProc = new Long(keyProcuradores);

			Procurador procurador = Procurador.getById(idProc);

			ProcuradorReport procuradorReport = new ProcuradorReport();

			procuradorReport.setIdProcurador(idProc);
			procuradorReport.setDesProcurador(procurador.getDescripcion()); 

			HashMap<String, HashMap<String, HashMap<String, TotalesConvenioReport>>> planes = procuradores.get(keyProcuradores);

			for (Object keyPlanRecurso:planes.keySet()){

				//String keyPlanRecurso = (String)obj;

				System.out.println("# keyPlanRecurso: " + keyPlanRecurso);

				String[] planRec = ((String) keyPlanRecurso).split("-");

				Long idPlan = new Long(planRec[0]);
				Long idRecurso = new Long(planRec[1]);

				Plan plan = Plan.getById(idPlan);
				Recurso recurso = Recurso.getById(idRecurso);


				PlanReport planReport = new PlanReport();

				planReport.setIdPlan(idPlan);
				planReport.setDesPlan(plan.getDesPlan() + " - " + recurso.getDesRecurso()); 

				HashMap<String, HashMap<String, TotalesConvenioReport>> areas = planes.get(keyPlanRecurso);

				for (String keyAreaOficina: areas.keySet()){

					System.out.println("# 	-> keyAreaOficina: " + keyAreaOficina);

					String[] areaOfi = keyAreaOficina.split("-");

					Long idArea = new Long(areaOfi[0]);
					Long idOficina = areaOfi[1]!=null? new Long(areaOfi[1]):null; 

					Area area = Area.getByIdNull(idArea);
					if(area == null)
						area = Area.getByCodigo(Area.COD_AREA_DEFAULT_SIAT);
					Oficina oficina = Oficina.getByIdNull(idOficina);

					String desAreaOfi = area.getDesArea() + (oficina!=null?oficina.getDesOficina():""); 

					AreaReport areaReport = new AreaReport();

					areaReport.setIdArea(idArea);
					areaReport.setDesArea(desAreaOfi);

					HashMap<String,TotalesConvenioReport> totales = areas.get(keyAreaOficina);

					for (String formaPago: totales.keySet() ){

						System.out.println("# 		-> forma de Pago: " + formaPago);

						TotalesConvenioReport totalConvenio = totales.get(formaPago);

						System.out.println("# 			-> totalConvenio - cantidad :" + totalConvenio.getCantidad() +
								" anticipo: " + totalConvenio.getAnticipo() +
								" resto: " + totalConvenio.getResto());

						System.out.println("# agregando totalConvenio: " + totalConvenio.getDesFormaPago());
						areaReport.getListTotales().add(totalConvenio);
					}
					System.out.println("# agregando area id: " + areaReport.getIdArea());
					planReport.getListArea().add(areaReport);
				}
				System.out.println("# agregando plan id: " + planReport.getIdPlan());
				procuradorReport.getListPlanReport().add(planReport);
			}
			System.out.println("# agregando procurador id: " + procuradorReport.getIdProcurador());
			convenioFormReport.getListProcuradores().add(procuradorReport);
		}
		
		
		System.out.println(" # convenioFormReport.getListProcuradores().size(): " + convenioFormReport.getListProcuradores().size());

		
		// Aqui se resuelve la busqueda y se genera el archivo resultado
		PlanillaVO reporteGenerado = new PlanillaVO();  
		
		PrintModel printModel = Formulario.getPrintModelForPDF(Formulario.COD_REP_CONVENIOS_FORM);
		
		printModel.putCabecera("TituloReporte", "Reporte de Convenios Formalizados");
		printModel.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
		printModel.putCabecera("Usuario", auxConvenioFormReport.getUserName());
		
		// Cargamos los datos en el Print Model
		printModel.setData(convenioFormReport);
		printModel.setTopeProfundidad(4);
		
		String idUsuario = auxConvenioFormReport.getUserId();
		String idCorrida = AdpRun.currentRun().getId().toString();
		
		//String fileSharePath = SiatParam.getString("FileSharePath"); 
		//String fileDir = fileSharePath + "/tmp"; 	
		String fileDir = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);
	
		// Archivo pdf a generar
		String fileNamePdf = idCorrida+"ReporteConvenioFormalizado"+ idUsuario +".pdf"; 
		File pdfFile = new File(fileDir+"/"+fileNamePdf);
		
		OutputStream out = new java.io.FileOutputStream(pdfFile);
		
		out.write(printModel.getByteArray());
					
		reporteGenerado.setFileName(fileDir+"/"+fileNamePdf);
		reporteGenerado.setDescripcion("Reporte de Convenios Formalizados");
		reporteGenerado.setCtdResultados(1L);
		
		convenioFormReport.setReporteGenerado(reporteGenerado);
		
		convenioFormReport.passErrorMessages(auxConvenioFormReport);
		auxConvenioFormReport.setReporteGenerado(reporteGenerado);
		
		return auxConvenioFormReport;
	}
    
    
	/**
	 *  Valida el Criterio de Caducidad por Cuotas Impagas Consecutivas y retorna la Cuota que causa la caducidad.
	 *  <p>( se usa en el Reporte de Convenios a Caducar )</p>
	 *  
	 * @param planMotCad
	 * @param listConvenioCuota
	 * @param fechaPago
	 * @return convenioCuota
	 * @throws Exception
	 */
	public ConvenioCuota obtieneCuotaPorValidacionCuotasImpagasConsecutivas(PlanMotCad planMotCad, List<ConvenioCuota> listConvenioCuota, Date fechaPago) throws Exception{
		if (planMotCad.getCantCuoCon() == null)return null;
		int cantDeCuotasImpagas = 0;
		ConvenioCuota cuotaMotivo = null;
		
		List<ConvenioCuota> listConvenioCuotaRepl = new ArrayList<ConvenioCuota>();
		listConvenioCuotaRepl.addAll(listConvenioCuota);
		
		for(ConvenioCuota convenioCuota: listConvenioCuota){
			//Obtener solo las cuotas cuya fecha de vencimiento sea < a la fecha de pago
			if (DateUtil.isDateBefore(convenioCuota.getFechaVencimiento(), fechaPago)){
				//Si la cuota se asento como pago a Cuenta vuelvo a verificar si sigue cumpliendose la condicion de caducidad
				if (convenioCuota.getFechaPago()!=null && convenioCuota.getEstadoConCuo().equals(EstadoConCuo.getById(EstadoConCuo.ID_PAGO_A_CUENTA))){
					int cantCuoImpPagos = 0;
					for (ConvenioCuota conCuota: listConvenioCuotaRepl){
						if (DateUtil.isDateBefore(conCuota.getFechaVencimiento(), convenioCuota.getFechaPago())){
							if (conCuota.getFechaPago()==null || DateUtil.isDateAfterOrEqual(conCuota.getFechaPago() , convenioCuota.getFechaPago())){
								cantCuoImpPagos ++;
								if (cantCuoImpPagos == planMotCad.getCantCuoCon().intValue()){
									log.debug("---- CADUCO POR CUOTAS IMPAGAS CONSECUTIVAS ----");
									return convenioCuota;
								}
							}else{
								cantCuoImpPagos=0;
							}
						}else{
							break;
						}
					}
				}
				if(convenioCuota.getEstadoConCuo().getId().longValue() == EstadoConCuo.ID_PAGO_BUENO)
					cantDeCuotasImpagas = 0;				
				else
					if(convenioCuota.getFechaPago() == null 
							|| DateUtil.isDateAfter(convenioCuota.getFechaPago(), fechaPago))
						cantDeCuotasImpagas++;
				if(cantDeCuotasImpagas == planMotCad.getCantCuoCon().intValue()){
					cuotaMotivo = convenioCuota;
					break;
				}
			}else{
				break;
			}
		}
		if(cantDeCuotasImpagas == planMotCad.getCantCuoCon().intValue()){
			log.debug("---- CADUCO POR CUOTAS IMPAGAS CONSECUTIVAS ----");
			return cuotaMotivo;
		}else{
			return null;
		}
	}
	
	/**
	 *  Valida el Criterio de Caducidad por Cuotas Impagas Alternadas y retorna la Cuota que causa la caducidad.
	 *  <p>( se usa en el Reporte de Convenios a Caducar )</p>
	 * @param planMotCad
	 * @param listConvenioCuota
	 * @param fechaPago
	 * @return boolean
	 * @throws Exception
	 */
	public ConvenioCuota obtieneCuotaPorValidacionOCuotasImpagasAlternadas(PlanMotCad planMotCad, List<ConvenioCuota> listConvenioCuota, Date fechaPago) throws Exception{
		if (planMotCad.getCantCuoAlt()==null)return null;
		int cantDeCuotasImpagas = 0;
		
		ConvenioCuota cuotaMotivo = null;
		List<ConvenioCuota>listConvenioCuotaRepl = new ArrayList<ConvenioCuota>();
		listConvenioCuotaRepl.addAll(listConvenioCuota);
		
		for(ConvenioCuota convenioCuota: listConvenioCuota){
			if (DateUtil.isDateBefore(convenioCuota.getFechaVencimiento(), fechaPago)){
				if (convenioCuota.getFechaPago()!= null && convenioCuota.getEstadoConCuo().equals(EstadoConCuo.getById(EstadoConCuo.ID_PAGO_A_CUENTA))){
					int cantCuoImpPagos = 0;
					for (ConvenioCuota conCuota:listConvenioCuotaRepl){
						if (DateUtil.isDateBefore(conCuota.getFechaVencimiento(),convenioCuota.getFechaPago())){
							if (conCuota.getFechaPago()==null || DateUtil.isDateAfterOrEqual(conCuota.getFechaPago(), convenioCuota.getFechaPago())){
								cantCuoImpPagos ++;
								if (cantCuoImpPagos == planMotCad.getCantCuoAlt()){
									log.debug("------- CADUCO POR CUOTAS IMPAGAS ALTERNADAS --------");
									return convenioCuota;
								}
							}
						}else{
							break;
						}
					}
				}
				if(convenioCuota.getFechaPago() == null 
							|| (DateUtil.isDateAfter(convenioCuota.getFechaPago(), fechaPago) 
									&& convenioCuota.getEstadoConCuo().getId().longValue() != EstadoConCuo.ID_PAGO_BUENO))
						cantDeCuotasImpagas++;
				if(cantDeCuotasImpagas == planMotCad.getCantCuoAlt().intValue()){
					cuotaMotivo = convenioCuota;
					break;
				}
			}else{
				break;
			}
		}
		if(cantDeCuotasImpagas == planMotCad.getCantCuoAlt().intValue()){
			log.debug("------- CADUCO POR CUOTAS IMPAGAS ALTERNADAS --------");
			return cuotaMotivo;
		}else{
			return null;
		}
	}
	
	/**
	 *  Valida el Criterio de Caducidad por Dias Corridos desde el Vencimiento de la primera Cuota Impaga y retorna la Cuota que causa la caducidad.
	 *  <p>( se usa en el Reporte de Convenios a Caducar )</p>

	 * @param planMotCad
	 * @param listConvenioCuota
	 * @param fechaPago
	 * @return boolean
	 * @throws Exception
	 */
	public ConvenioCuota obtieneCuotaPorValidacionDiasCorridos(PlanMotCad planMotCad, List<ConvenioCuota> listConvenioCuota, Date fechaPago) throws Exception{
		if (planMotCad.getCantDias() ==null)return null;
		 
		Log log =LogFactory.getLog(Convenio.class);
		if(ListUtil.isNullOrEmpty(listConvenioCuota)){
			return null;
		}
		
		for(ConvenioCuota convenioCuota: listConvenioCuota){
			if (DateUtil.isDateBefore(convenioCuota.getFechaVencimiento(), fechaPago)){
				log.debug("-----CUOTA "+convenioCuota.getNumeroCuota());
				
				if (convenioCuota.getFechaPago() !=null && convenioCuota.getEstadoConCuo().equals(EstadoConCuo.getById(EstadoConCuo.ID_PAGO_A_CUENTA))){
					if (DateUtil.isDateAfter(convenioCuota.getFechaPago(), convenioCuota.getFechaVencimiento())){
						if (DateUtil.getCantDias(convenioCuota.getFechaVencimiento(), convenioCuota.getFechaPago())>planMotCad.getCantDias().intValue()){
							log.debug("-------- CADUCO POR CANTIDAD DE DIAS DESDE LA PRIMER CUOTA IMPAGA ---------");
							return convenioCuota;
						}
					}
					
				}else if (convenioCuota.getFechaPago()==null){
					if (DateUtil.getCantDias(convenioCuota.getFechaVencimiento(), fechaPago)>planMotCad.getCantDias().intValue()){
						log.debug("-------- CADUCO POR CANTIDAD DE DIAS DESDE LA PRIMER CUOTA IMPAGA ---------");
						return convenioCuota;
					}
				}
			}else{
				break;
			}
		}
		
		return null;
	}
	
	public boolean tieneCuotaSaldoPaga()throws Exception{
		return GdeDAOFactory.getConvenioDAO().tieneCuotaSaldoPaga(this);
	}
	
	public boolean esConsistente() throws Exception{
		if (this.getEstadoConvenio().getId().longValue() != EstadoConvenio.ID_VIGENTE){
			return true;
		}
		
		Double saldo = 0D;
		boolean deudaOtroEstado = false;
		boolean saldoDeudaDif=false;
		boolean saldoEnPlanDifCapitalCuota=false;
		boolean cuotasAsentadasNOK=false;
		
		
		for (ConvenioDeuda convenioDeuda : getListConvenioDeuda()){
			Deuda deuda = convenioDeuda.getDeuda();
			Long idEstadoDeuda = null;
			saldo += convenioDeuda.getSaldoEnPlan();
			// Verifico que la deuda se encuentre en la misma tabla que la via del convenio y en estado impago
			if(this.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_ADMIN)
				idEstadoDeuda = EstadoDeuda.ID_ADMINISTRATIVA;
			if(this.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL)
				idEstadoDeuda = EstadoDeuda.ID_JUDICIAL;
			if (deuda.getEstadoDeuda().getId().longValue()!= idEstadoDeuda){
				deudaOtroEstado= true;
			}
			Double saldoDeuda=0D;
			
			// Verifico que el saldo de la deuda sea igual al saldo cuando se incluyo en el covenio
			if (convenioDeuda.getCapitalOriginal() != null){
				saldoDeuda = convenioDeuda.getCapitalOriginal();
			}
			if (saldoDeuda == 0D && convenioDeuda.getCapitalEnPlan() !=null){
				saldoDeuda = convenioDeuda.getCapitalEnPlan();
			}
			
			if (deuda.getSaldo().doubleValue() != saldoDeuda){
				saldoDeudaDif=true;
			}
		}
		
		if (deudaOtroEstado){
			this.desMotInc="Existe deuda en otro estado o via diferente al convenio";
		}
		
		Double capitalSinAsentar = 0D;
		
		for (ConvenioCuota convenioCuota : getListConvenioCuota()){
			if (!convenioCuota.estaImputada()){
				capitalSinAsentar += convenioCuota.getCapitalCuota();
			}
			if (convenioCuota.getEstadoConCuo().getId().longValue() != EstadoConCuo.ID_IMPAGO){
				log.debug("VERIFICAR PAGO DE CUOTA "+convenioCuota.getNumeroCuota());
				if (this.estaCaduco(convenioCuota.getFechaPago())){
					if (convenioCuota.getEstadoConCuo().getId().longValue()!= EstadoConCuo.ID_PAGO_A_CUENTA.longValue()){
						log.debug("Pago Bueno mal imputado para cuota nro "+convenioCuota.getNumeroCuota());
						cuotasAsentadasNOK=true;
					}
				}else{
					if (convenioCuota.getEstadoConCuo().getId().longValue()!= EstadoConCuo.ID_PAGO_BUENO.longValue()){
						log.debug("Pago a Cuenta mal imputado para cuota nro "+convenioCuota.getNumeroCuota());
						cuotasAsentadasNOK=true;
					}
				}
			}
		}
		
		if (!(capitalSinAsentar > (saldo - 0.5) && capitalSinAsentar < (saldo + 0.5))){
			saldoEnPlanDifCapitalCuota= true;
			this.desMotInc += !this.desMotInc.equals("") ? "; ":"";
			this.desMotInc += "El saldo en plan difiere del capital de las cuotas a asentar en un monto mayor a la tolerancia";
		}
		
		if(cuotasAsentadasNOK){
			this.desMotInc += !this.desMotInc.equals("") ? "; ":"";
			this.desMotInc += "Las cuotas no fueron imputadas correctamente (pagos a cuenta y pagos bueno)";
		}
		
		if(saldoDeudaDif){
			this.desMotInc += !this.desMotInc.equals("") ? "; ":"";
			this.desMotInc += "El saldo de la deuda no es igual al incluido en el convenio";
		}
		
		
		return !(deudaOtroEstado || saldoDeudaDif || saldoEnPlanDifCapitalCuota || cuotasAsentadasNOK);
	}
	
	public List<Long> getIdCuotasEnProcesoAsentamiento() throws Exception{
		return ConvenioCuota.getListIdCuotaAuxPagCuo(this);
	}
	
	
	
	/**
	 *  Verifica si esta caduco el Convenio. Recibe la lista de Rescates, la lista de Motivos de Caducidad del Plan y un
	 *  mapa de Feriados. 
	 * 	Retorna un entero:
	 * 						0 - Si el convenio NO esta caduco y NO tiene pagos a cuenta
	 * 						1 - Si el convenio NO esta caduco y SI tiene pagos a cuenta
	 * 						2 - Si el convenio SI esta caduco
	 * 
	 * @param fechaPago
	 * @return 0,1,2
	 */
	public Integer estaCaduco(Date fechaPago, List<Rescate> listRescate
								, List<PlanMotCad> listPlanMotCad, Map<Date, Feriado> mapFeriado) throws Exception{
		boolean tienePagoACta = false;
		//List<ConvenioCuota> listConvenioCuota = ConvenioCuota.getListByConvenioYFecha(this, fechaPago);
		List<ConvenioCuota> listConvenioCuota = getListConvenioCuota();
	
		// Si tiene un rescate individual retorno false
		if (this.getAplicaPagCue().intValue()==SiNo.NO.getId().intValue()){
			log.debug("El convenio tiene un rescate individual");
			for(ConvenioCuota convenioCuota: listConvenioCuota){
				if(!tienePagoACta && convenioCuota.getEstadoConCuo().getId().longValue() == EstadoConCuo.ID_PAGO_A_CUENTA.longValue()){
					tienePagoACta = true;				
					return 1;
				}
			}
			return 0;
		}
		// Obtener rescate, si existe, para la Fecha Pago.
		Rescate rescate = null;
		for(Rescate res: listRescate){
			if(DateUtil.isDateAfterOrEqual(fechaPago, res.getFechaRescate()) && 
					(res.getFechaVigRescate() == null || DateUtil.isDateBeforeOrEqual(fechaPago, res.getFechaVigRescate()))){
				rescate = res;
				break;
			}
		}			
		if(rescate != null){
			log.debug("Hay un rescate vigente para esta fecha");
			for(ConvenioCuota convenioCuota: listConvenioCuota){
				if(!tienePagoACta && convenioCuota.getEstadoConCuo().getId().longValue() == EstadoConCuo.ID_PAGO_A_CUENTA.longValue()){
					tienePagoACta = true;				
					return 1;
				}
			}
			return 0;
		}
		// Si no se obtuvo un rescate, se analizan los criterios de caducidad.
		PlanMotCad planMotCad = null;
		for(PlanMotCad pmc: listPlanMotCad){
			if(DateUtil.isDateAfterOrEqual(fechaPago, pmc.getFechaDesde()) && 
					(pmc.getFechaHasta() == null || DateUtil.isDateBeforeOrEqual(fechaPago, pmc.getFechaHasta()))){
				planMotCad = pmc;
				break;
			}
		}
		if (planMotCad==null){
			log.debug("No se obtuvieron motivos de caducidad del plan");
			for(ConvenioCuota convenioCuota: listConvenioCuota){
				if(!tienePagoACta && convenioCuota.getEstadoConCuo().getId().longValue() == EstadoConCuo.ID_PAGO_A_CUENTA.longValue()){
					tienePagoACta = true;				
					return 1;
				}
			}
			return 0;
		}
	
		// Obtener si hay Pagos Indeterminados consultando la db de Indetermindos.
		for(ConvenioCuota convenioCuota: listConvenioCuota){
			if(!tienePagoACta && convenioCuota.getEstadoConCuo().getId().longValue() == EstadoConCuo.ID_PAGO_A_CUENTA.longValue()){
				tienePagoACta = true;				
			}
			// Identificamos Cuotas Impagas y verificamos contra la db de Indeterminados.
			Date fechaPagoCuota= null;
			// Obtengo la fecha de pago de la cuota
			fechaPagoCuota = convenioCuota.getFechaPago();
			// Si tiene fecha de pago se la paso a FecPag4Cad
			if (fechaPagoCuota != null)convenioCuota.setFecPag4Cad(fechaPagoCuota);
			if(fechaPagoCuota==null && IndeterminadoFacade.getInstance().getEsIndeterminada(convenioCuota)){
				IndetVO sinIndet = IndeterminadoFacade.getInstance().getIndeterminada(convenioCuota);
				if (sinIndet.getFechaPago()!=null){
					convenioCuota.setFecPag4Cad(sinIndet.getFechaPago()); 
				}else{
					convenioCuota.setFecPag4Cad(convenioCuota.getFechaVencimiento());
				}
				//Si la cuota esta indeterminada se fuerza que es pagada a termino en favor del contribuyente
			}
			Date fechaVen = convenioCuota.getFechaVencimiento();
			
			//Se modifico para que no verifique el feriado si la fecha de vencimiento es mayor a hoy
			if (DateUtil.isDateAfter(fechaVen, new Date())){
				convenioCuota.setFecVenNoFeriado(fechaVen);
			}else{
				convenioCuota.setFecVenNoFeriado(Feriado.nextDiaHabilUsingMap(fechaVen, mapFeriado));
			}
		}
		boolean cuotasImpagasConsecutivas= validarCuotasImpagasConsecutivas(planMotCad, listConvenioCuota, fechaPago);
		boolean cuotasImpagasAlternadas = validarCuotasImpagasAlternadas(planMotCad, listConvenioCuota, fechaPago);
		boolean diasCorridos = validarDiasCorridos(planMotCad, listConvenioCuota, fechaPago);
		
		// Validar Caducidad
		boolean esCaduco = cuotasImpagasConsecutivas || cuotasImpagasAlternadas	|| diasCorridos;
		
		
		if(!esCaduco && !tienePagoACta)
			return 0;
		if(!esCaduco && tienePagoACta)
			return 1;

		return 2;
		//return esCaduco;
	}
	
	/**
     * Devuelve el Valor del Atributo asociado a las Deudas del convenio.
     * 
     * @param idAtributo
     * @param fechaVigencia
     * @return
     */
    public String getValorAtributo(){
    	List<ConvenioDeuda> listConvenioDeuda = this.getListConvenioDeuda();
    	if(listConvenioDeuda != null){
    			ConvenioDeuda convenioDeuda = listConvenioDeuda.get(0);
    			if(convenioDeuda != null){
    				Deuda deuda = convenioDeuda.getDeuda();
    				if(deuda != null)
    					return deuda.getAtrAseVal();
    			}
    	}
    	return null;
    }
    
    /**
	 *  Verifica si el Convenio tiene todas las cuotas pagas y como pagos buenos. 
	 *  O valida si el Convenio pasado como parametro tiene todas las cuotas pagas y al menos un pago a cuenta.
	 *  Devuelve: 
	 *  			0 - si el Convenio tiene todas las cuotas pagas y como pagos buenos.
	 *  			1 - si el Convenio pasado como parametro tiene todas las cuotas pagas y al menos un pago a cuenta.
	 *	 			2 - si no cumple ninguna de las dos anteriores
	 *
	 * @return 0,1,2
	 */
	public Integer validarEstadoCuotas() throws Exception{
		boolean tieneTodasCuotasPagosBuenos = true;
		boolean tieneTodasCuotasPagas = true;
		boolean pagoACuenta = false;
		for(ConvenioCuota convenioCuota: listConvenioCuota){
			if(convenioCuota.getEstadoConCuo().getId().longValue() != EstadoConCuo.ID_PAGO_BUENO){
				tieneTodasCuotasPagosBuenos = false;
			}
			if(convenioCuota.getEstadoConCuo().getId().longValue() == EstadoConCuo.ID_IMPAGO){
				tieneTodasCuotasPagas = false;
			}
			if(!pagoACuenta && convenioCuota.getEstadoConCuo().getId().longValue() == EstadoConCuo.ID_PAGO_A_CUENTA){
				pagoACuenta = true;
			}
		}
		if(tieneTodasCuotasPagosBuenos)
			return 0;
		if(tieneTodasCuotasPagas && pagoACuenta)
			return 1;
		return 2;
	}
}

