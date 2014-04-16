//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.NonUniqueResultException;

import ar.gov.rosario.siat.bal.buss.bean.AuxDeuMdf;
import ar.gov.rosario.siat.bal.buss.bean.Canal;
import ar.gov.rosario.siat.bal.buss.bean.EstadoReclamo;
import ar.gov.rosario.siat.bal.buss.bean.IndeterminadoFacade;
import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.cyq.buss.bean.Procedimiento;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.AtrEmisionDefinition;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.exe.buss.bean.CueExeCache;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.gde.buss.dao.DeudaDAO;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.DeuAdmRecConVO;
import ar.gov.rosario.siat.gde.iface.model.DeuJudRecConVO;
import ar.gov.rosario.siat.gde.iface.model.DeudaJudicialVO;
import ar.gov.rosario.siat.gde.iface.model.DeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCodRefPagSearchPage;
import ar.gov.rosario.siat.pad.buss.bean.Broche;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.ExentaAreaCache;
import ar.gov.rosario.siat.pad.buss.bean.Repartidor;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Esta es de la que debieran hereadar los BO de Deuda.
 * (Ej: DeudaAdmin, DeudaJudicial)
 * 
 */
@MappedSuperclass
public abstract class Deuda extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	public static final String FECHA_VENCIMIENTO = "fechaVencimiento";	
	
	
	@Transient
	Logger log = Logger.getLogger(Deuda.class);
	

	@Column(name = "codRefPag") 
	private Long codRefPag;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idCuenta") 
	private Cuenta cuenta;

	@Column(name = "idCuenta", insertable=false, updatable=false)
	private Long idCuenta;

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idRecClaDeu") 
	private RecClaDeu recClaDeu;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idViaDeuda") 
	private ViaDeuda viaDeuda;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idEstadoDeuda") 
	private EstadoDeuda estadoDeuda;

	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idEmision") 
	private Emision emision;

	@Column(name = "anio")
	private Long anio;

	@Column(name = "periodo")
	private Long periodo;

	@Column(name = "fechaEmision")
	private Date fechaEmision;

	@Column(name = "fechaVencimiento")
	private Date fechaVencimiento;

	@Column(name = "importe")
	private Double importe; 

	@Column(name = "importeBruto")
	private Double importeBruto;

	@Column(name = "saldo")
	private Double saldo;

	@Column(name = "actualizacion")
	private Double actualizacion;
	
	@Column(name = "strConceptosProp")
	private String strConceptosProp;

	@Column(name = "strEstadoDeuda")
	private String strEstadoDeuda;

	@Column(name = "fechaPago")
	private Date fechaPago;

	@Column(name = "estaImpresa")
	private Integer estaImpresa;

	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idRepartidor") 
	private Repartidor repartidor;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idProcurador") 
	private Procurador procurador;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idRecurso")
	private Recurso recurso;
	
    @Column(name="idProcedimientoCyQ")
    private Long idProcedimientoCyQ;
    
    @Transient
	private Procedimiento procedimientoCyQ;

	@Column(name = "actualizacionCyQ")
	private Double actualizacionCyQ;

	@Column(name = "obsMotNoPre")
	private String obsMotNoPre;

	@Column(name = "reclamada")
	private Integer reclamada;

	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idSistema") 
	private Sistema sistema;

	@Column(name = "resto")
	private Long resto;

	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idConvenio") 
	private Convenio convenio;
	
	@Column(name = "idConvenio", insertable = false, updatable = false) 
	private Long idConvenio;
	
	
	@Column(name = "idProcurador", insertable = false, updatable = false) 
	private Long idProcurador;

	/*
	 * Serializacion del valor de los atributos
	 * utilizados durante la emision. 20/08/2009.
	 */
	@Column(name = "atrAseVal")
	private String emiAtrVal; 
	
	
	//contiene la lista de conceptos de la deuda
	/*
	@OneToMany(fetch=FetchType.EAGER , targetEntity=DeuAdmRecCon.class)
	@JoinColumn(name="idDeuda")
	private abstract List<DeuRecCon> listDeuRecCon;
	*/

	// Contructores 
	public Deuda() {
		super();
	}

	// Getters y Setters
	// se bebe redefinir en la subclase
	public abstract List<? extends DeuRecCon> getListDeuRecCon();
	
	public Long getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(Long idCuenta) {
		this.idCuenta = idCuenta;
	}

	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	
	public RecClaDeu getRecClaDeu() {
		return recClaDeu;
	}
	
	public void setRecClaDeu(RecClaDeu recClaDeu) {
		this.recClaDeu = recClaDeu;
	}

	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}
	
	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

	public Long getAnio() {
		return anio;
	}

	public void setAnio(Long anio) {
		this.anio = anio;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	/**
	 * Importe orginal de la duda que se emite
	 * @return
	 */
	public Double getImporte() {
		return importe;
	}

	/**
	 * Importe orginal de la duda que se emite
	 */
	public void setImporte(Double importe) {
		this.importe = importe;
	}

	/**
	 * Lo que se emite sin considerar excenciones que pudiera tener
	 * @return
	 */
	public Double getImporteBruto() {
		return importeBruto;
	}

	/**
	 * Lo que se emite sin considerar excenciones que pudiera tener
	 */
	public void setImporteBruto(Double importeBruto) {
		this.importeBruto = importeBruto;
	}

	public Long getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
	}

	/**
	 * Cuando se emite saldo=importe, luego si hay algun parcial es lo que resta 
	 * @return
	 */
	public Double getSaldo() {
		return saldo;
	}

	/**
	 * Cuando se emite saldo=importe, luego si hay algun parcial es lo que resta 
	 */
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public String getStrConceptosProp() {
		return strConceptosProp;
	}

	public void setStrConceptosProp(String strConceptosProp) {
		this.strConceptosProp = strConceptosProp;
	}

	public Integer getEstaImpresa() {
		return estaImpresa;
	}

	public void setEstaImpresa(Integer estaImpresa) {
		this.estaImpresa = estaImpresa;
	}

	public EstadoDeuda getEstadoDeuda() {
		return estadoDeuda;
	}

	public void setEstadoDeuda(EstadoDeuda estadoDeuda) {
		this.estadoDeuda = estadoDeuda;
	}

	/**
	 * Valor del recargo al momento de pago
	 * @return
	 */
	public Double getActualizacion() {
		return actualizacion;
	}

	/**
	 * Valor del recargo al momento de pago
	 */
	public void setActualizacion(Double actualizacion) {
		this.actualizacion = actualizacion;
	}

	/**
	 * Valor del recargo a la fecha de concurso
	 * @return
	 */
	public Double getActualizacionCyQ() {
		return actualizacionCyQ;
	}

	/**
	 * Valor del recargo a la fecha de concurso
	 */
	public void setActualizacionCyQ(Double actualizacionCyQ) {
		this.actualizacionCyQ = actualizacionCyQ;
	}

	public Long getCodRefPag() {
		return codRefPag;
	}

	public void setCodRefPag(Long codRefPag) {
		this.codRefPag = codRefPag;
	}

	public String getObsMotNoPre() {
		return obsMotNoPre;
	}

	public void setObsMotNoPre(String obsMotNoPre) {
		this.obsMotNoPre = obsMotNoPre;
	}

	public Integer getReclamada() {
		return reclamada;
	}

	public void setReclamada(Integer reclamada) {
		this.reclamada = reclamada;
	}

	public Long getResto() {
		return resto;
	}

	public void setResto(Long resto) {
		this.resto = resto;
	}

	public String getStrEstadoDeuda() {
		return strEstadoDeuda;
	}

	public void setStrEstadoDeuda(String strEstadoDeuda) {
		this.strEstadoDeuda = strEstadoDeuda;
	}

	public Procedimiento getProcedimientoCyQ() {
		
		if (getIdProcedimientoCyQ() != null)
			procedimientoCyQ = Procedimiento.getByIdNull(Math.abs(getIdProcedimientoCyQ().longValue())); 
		
		return procedimientoCyQ;
	}
	
	public void setProcedimientoCyQ(Procedimiento procedimientoCyQ) {
		this.procedimientoCyQ = procedimientoCyQ;
	}
	
	public Procurador getProcurador() {
		return procurador;
	}
	
	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}
	
	public Long getIdConvenio() {
		return idConvenio;
	}

	public void setIdConvenio(Long idConvenio) {
		this.idConvenio = idConvenio;
	}

	public Repartidor getRepartidor() {
		return repartidor;
	}
	
	public void setRepartidor(Repartidor repartidor) {
		this.repartidor = repartidor;
	}
	
	
	public Convenio getConvenio() {
		return convenio;
	}
	
	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}
	
	public Emision getEmision() {
		return emision;
	}
	
	public void setEmision(Emision emision) {
		this.emision = emision;
	}
		
	public String getEmiAtrVal() {
		return this.emiAtrVal;
	}
	
	public void setEmiAtrVal(String emiAtrVal) {
		this.emiAtrVal = emiAtrVal;
	}
	
	public Long getIdProcurador() {
		return idProcurador;
	}
	public void setIdProcurador(Long idProcurador) {
		this.idProcurador = idProcurador;
	}
	
	public Long getIdProcedimientoCyQ() {
		return idProcedimientoCyQ;
	}
	public void setIdProcedimientoCyQ(Long idProcedimientoCyQ) {
		this.idProcedimientoCyQ = idProcedimientoCyQ;
	}
	
	// Metodos de clase

	public static Deuda getById(Long id, Long idEstadoDeuda){
		Deuda deuda = null;
		if(idEstadoDeuda == EstadoDeuda.ID_ADMINISTRATIVA){
			deuda = DeudaAdmin.getById(id);
		}
		if(idEstadoDeuda == EstadoDeuda.ID_JUDICIAL){
			deuda = DeudaJudicial.getById(id);
		}
		
		if(idEstadoDeuda == EstadoDeuda.ID_CANCELADA){
			
			deuda = DeudaCancelada.getByIdNull(id);
			
			if (deuda == null)
				deuda = DeudaAdmin.getByIdNull(id);
			
			if (deuda == null)
				deuda = DeudaJudicial.getByIdNull(id);
		}
		
		if(idEstadoDeuda == EstadoDeuda.ID_ANULADA ||
				idEstadoDeuda == EstadoDeuda.ID_CONDONADA ||
				idEstadoDeuda == EstadoDeuda.ID_PRESCRIPTA ){
			deuda = DeudaAnulada.getById(id);
		}
		
		return deuda;
	}
	public static Deuda getByIdNull(Long id, Long idEstadoDeuda){
		Deuda deuda = null;
		if(idEstadoDeuda == EstadoDeuda.ID_ADMINISTRATIVA){
			deuda = DeudaAdmin.getByIdNull(id);
		}
		if(idEstadoDeuda == EstadoDeuda.ID_JUDICIAL){
			deuda = DeudaJudicial.getByIdNull(id);
		}
		
		if(idEstadoDeuda == EstadoDeuda.ID_CANCELADA){
			
			deuda = DeudaCancelada.getByIdNull(id);
			
			if (deuda == null)
				deuda = DeudaAdmin.getByIdNull(id);
			
			if (deuda == null)
				deuda = DeudaJudicial.getByIdNull(id);
		}
		
		if(idEstadoDeuda == EstadoDeuda.ID_ANULADA ||
				idEstadoDeuda == EstadoDeuda.ID_CONDONADA ||
				idEstadoDeuda == EstadoDeuda.ID_PRESCRIPTA ){
			deuda = DeudaAnulada.getByIdNull(id);
		}
		
		return deuda;
	}
	/**
	 * Busca el id de la deuda solamente en la tabla de estado pasada como parametro
	 * si no la encuentra retorna null
	 * @param id
	 * @param idEstadoDeuda
	 * @return deuda
	 */
	public static Deuda getByIdNullUniqueTable(Long id, Long idEstadoDeuda){
		Deuda deuda = null;
		if(idEstadoDeuda == EstadoDeuda.ID_ADMINISTRATIVA){
			deuda = DeudaAdmin.getByIdNull(id);
		}
		if(idEstadoDeuda == EstadoDeuda.ID_JUDICIAL){
			deuda = DeudaJudicial.getByIdNull(id);
		}
		
		if(idEstadoDeuda == EstadoDeuda.ID_CANCELADA){
			
			deuda = DeudaCancelada.getByIdNull(id);
			
		}
		
		if(idEstadoDeuda == EstadoDeuda.ID_ANULADA ||
				idEstadoDeuda == EstadoDeuda.ID_CONDONADA ||
				idEstadoDeuda == EstadoDeuda.ID_PRESCRIPTA ){
			deuda = DeudaAnulada.getByIdNull(id);
		}
		
		return deuda;
	}
	
	
	
	/**
	 * Obtiene una Deuda por Id. Busca en todas las tablas de deuda hasta que encuentra una.
	 * Si no encuentra ninguna devuelve null.
	 * @param id
	 * @return
	 */
	public static Deuda getById(Long id){
		Deuda deuda =null;
		deuda = (Deuda) DeudaAdmin.getByIdNull(id);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaJudicial.getByIdNull(id);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaCancelada.getByIdNull(id);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaAnulada.getByIdNull(id);
		if(deuda != null)
			return deuda;
		return null;
	}
	
	/**
	 * Obtiene una Deuda por Id. Busca en todas las tablas comenzando por judicial y cancelada hasta encuentra una.
	 * Si no encuentra ninguna devuelve null.
	 * @param id
	 * @return
	 */
	public static Deuda getByIdFirstJud(Long id){
		Deuda deuda =null;
		deuda = (Deuda) DeudaJudicial.getByIdNull(id);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaCancelada.getByIdNull(id);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaAdmin.getByIdNull(id);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaAnulada.getByIdNull(id);
		if(deuda != null)
			return deuda;
		return null;
	}
	
	
	/**
	 * Obtiene una Deuda por Id. Buscando unicamente en deudaAdmin o judicial.
	 * Si no encuentra ninguna devuelve null.
	 * @param id
	 * @return
	 */
	public static Deuda getByIdForSalPorCad(Long id){
		Deuda deuda =null;
		deuda = (Deuda) DeudaAdmin.getByIdNull(id);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaJudicial.getByIdNull(id);
		if(deuda != null)
			return deuda;
		
		return null;
	}
	
	/**
	 * Obtiene una Deuda por Codigo de Referencia de Pago. Busca en todas las tablas de deuda hasta que encuentra una.
	 * Si no encuentra ninguna devuelve null.
	 * @param codRefPag
	 * @return
	 */
	public static Deuda getByCodRefPag(Long codRefPag){
		Deuda deuda =null;
		if(codRefPag == null || codRefPag == 0)
			return null;
		deuda = (Deuda) DeudaAdmin.getByCodRefPag(codRefPag);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaJudicial.getByCodRefPag(codRefPag);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaCancelada.getByCodRefPag(codRefPag);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaAnulada.getByCodRefPag(codRefPag);
		if(deuda != null)
			return deuda;
		return null;
	}

	public static List<Deuda> getByCodRefPagImporte(LiqCodRefPagSearchPage liqCodRefPagSearchPage) throws Exception {
		List<Deuda> listDeuda = new ArrayList<Deuda>();

		listDeuda.addAll(DeudaAdmin.getByCodRefPagBySearchPage(liqCodRefPagSearchPage));	
		
		if (listDeuda.size() == 0){
			listDeuda.addAll(DeudaJudicial.getByCodRefPagBySearchPage(liqCodRefPagSearchPage));
		} 
		
		if (listDeuda.size() == 0){
			listDeuda.addAll(DeudaCancelada.getByCodRefPagBySearchPage(liqCodRefPagSearchPage));
		}
		
		return listDeuda;
	}
	
	
	/**
	 *  Obtiene una Deuda por Nro de Cuenta, Periodo, Anio, y Sistema. Busca en todas las tablas de deuda hasta que encuentra una.
	 *  Si no encuentra ninguna devuelve null.
	 *  La busqueda se hace por JDBC.
	 * 
	 * @param nroCta
	 * @param periodo
	 * @param anio
	 * @param nroSistema
	 * @return
	 * @throws Exception
	 */
	public static Deuda getByCtaPerAnioSisUsingJDBC(Long nroCta, Long periodo, Long anio, Long nroSistema) throws Exception{
		return (Deuda) GdeDAOFactory.getDeudaAdminDAO().getByCtaPerAnioSisUsingJDBC(nroCta,periodo,anio,nroSistema);
	}
	
	/**
	 * Obtiene una Deuda por Nro de Cuenta, Periodo, Anio, Sistema y Resto. Busca en todas las tablas de deuda hasta que encuentra una.
	 * Si no encuentra ninguna devuelve null.
	 * @param nroCta, periodo, anio, nroSistema
	 * @return
	 */
	public static Deuda getByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		Deuda deuda =null;
		deuda = (Deuda) DeudaAdmin.getByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema,resto);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaJudicial.getByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema,resto);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaCancelada.getByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema,resto);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaAnulada.getByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema,resto);
		if(deuda != null)
			return deuda;
		return null;
	}

	
	/**
	 * Obtiene un registro de deuda por Cuenta, Periodo y Anio.
	 * 
	 * @author Cristian
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @return
	 */
	public static Deuda getByCuentaPeriodoAnio(Cuenta cuenta, Long periodo, Integer anio){
		Deuda deuda =null;
		deuda = (Deuda) DeudaAdmin.getByCuentaPeriodoAnio(cuenta,periodo,anio);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaJudicial.getByCuentaPeriodoAnio(cuenta,periodo,anio);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaCancelada.getByCuentaPeriodoAnio(cuenta,periodo,anio);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaAnulada.getByCuentaPeriodoAnio(cuenta,periodo,anio);
		if(deuda != null)
			return deuda;
		return null;
	}
	
	
	/**
	 * Obtiene una Deuda por Nro de Cuenta, Periodo, Anio, Sistema y Resto y con Clasificacion de Deuda de Regimen Simplificado. Busca en todas las tablas de deuda hasta que encuentra una.
	 * Si no encuentra ninguna devuelve null.
	 * @param nroCta, periodo, anio, nroSistema
	 * @return
	 */
	public static Deuda getByCtaPerAnioSisResForRS(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		Deuda deuda =null;
		deuda = (Deuda) DeudaAdmin.getByCtaPerAnioSisResForRS(nroCta,periodo,anio,nroSistema,resto);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaJudicial.getByCtaPerAnioSisResForRS(nroCta,periodo,anio,nroSistema,resto);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaCancelada.getByCtaPerAnioSisResForRS(nroCta,periodo,anio,nroSistema,resto);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaAnulada.getByCtaPerAnioSisResForRS(nroCta,periodo,anio,nroSistema,resto);
		if(deuda != null)
			return deuda;
		return null;
	}

	/**
	 * Obtiene una Deuda por Nro de Cuenta, Periodo, Anio, Sistema y Resto con Clasificacion de Deuda Original. Busca en todas las tablas de deuda hasta que encuentra una.
	 * Si no encuentra ninguna devuelve null.
	 * @param nroCta, periodo, anio, nroSistema
	 * @return
	 */
	public static Deuda getOriginalByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		Deuda deuda =null;
		deuda = (Deuda) DeudaAdmin.getOriginalByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema,resto);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaJudicial.getOriginalByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema,resto);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaCancelada.getOriginalByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema,resto);
		if(deuda != null)
			return deuda;
		deuda = (Deuda) DeudaAnulada.getOriginalByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema,resto);
		if(deuda != null)
			return deuda;
		return null;
	}

	
	public static List<Deuda> getListByCuentaPeriodoAnio(Cuenta cuenta, Long periodo, Integer anio){
		return DeudaAdmin.getListByCuentaPeriodoAnio(cuenta,periodo,anio);
	}
	
	public static List<Deuda> getListRectifForPagoByCtaPer(Long idCuenta, Long periodo, Long anio, Long idRecClaDeu){
		return DeudaAdmin.getListRectifForPagoByCtaPer(idCuenta,periodo,anio,idRecClaDeu);
	}
	
	public static Deuda getByCtaPerAnioSisForIRS(Long idCuenta,Long periodo, Long anio, Long nroSistema){
		return DeudaAdmin.getByCtaPerAnioSisForIRS(idCuenta, periodo, anio, nroSistema);
	}
	
	public static List<Deuda> getListDeudaIRSByCtaPerAnioSis(Long idCuenta,Long periodo,Long anio){
		return DeudaAdmin.getListDeudaIRSByCtaPerAnioSis(idCuenta, periodo, anio);
	} 
	
	/**
	 * Obtiene un registro de deuda original de DReI por Cuenta, Periodo y Anio.
	 * 
	 * @author 
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @return
	 */
	public static Deuda getPerOriByCuentaPeriodoAnio(Cuenta cuenta, Long periodo, Integer anio){
		Deuda deuda =null;
		deuda = (Deuda) DeudaAdmin.getByCuentaPeriodoAnioParaDJ(cuenta,periodo,anio);
		if(deuda != null)
			return deuda;
		
		
		return null;
	}
	
	
	/**
	 * Obtiene una lista de ids de deuda de todas las tabla 
	 * donde se pueden llegar a encontrar, dado un id de cuenta.
	 * 
	 * @param idCuenta
	 * @return List<Long>
	 */
	public static List<Long> getListAllIdByCuentaVO(CuentaVO cuenta){
		
		ArrayList<Long> listId = new ArrayList<Long>();
		
		listId.addAll(GdeDAOFactory.getDeudaAdminDAO().getListIdDeudaByIdCuenta(cuenta));
		
		listId.addAll(GdeDAOFactory.getDeudaJudicialDAO().getListIdDeudaByIdCuenta(cuenta));
		
		listId.addAll(GdeDAOFactory.getDeudaCanceladaDAO().getListIdDeudaByIdCuenta(cuenta));
		
		listId.addAll(GdeDAOFactory.getDeudaAnuladaDAO().getListIdDeudaByIdCuenta(cuenta));
		
		return listId;
	}
	
	
	/**
	 * Verifica si la deuda esta en un juicio (Busca en GesJudDeu)
	 * @return
	 */
	public boolean estaEnGestionJudicial(){
		if(this.getGetJudDeu() == null)
			return false;
		return true;
	}
	
	/** 
	 * Obtiene la GesJudDeu de la deuda
	 * @return GesJudDeu
	 */
	public GesJudDeu getGetJudDeu(){
		return GesJudDeu.getByIdDeuda(this.getId());
	}
	
	/**
	 * 
	 * Checkea si exite deuda sin seleccionar que coincida con los criterios de:
     * 
     * Estado de Deuda
     * TipoDeudaPlan
     * Clasificacion/es de Deuda
     * Rango de Fechas de Vencimiento
     * 
     * 
	 * @author Cristian
	 * @param cuenta
	 * @param listIdsAExcluir
	 * @param estadoDeuda
	 * @param idTipoDeudaPlan
	 * @param listRecClaDeuPlan
	 * @param fechaVenDeuDes
	 * @param fechaVenDeuHas
	 * @return boolean
	 */  
    public static List<Deuda> existeDeudaNotIn(Cuenta cuenta, Long[] listIdsAExcluir, Long idEstadoDeuda, 
    		Long[] listIdsRecClaDeuExcluir, Date fechaVenDeuDes, Date fechaVenDeuHas, Long idViaDeudaSelected){
    	    	 
    	if(idEstadoDeuda.longValue() == EstadoDeuda.ID_ADMINISTRATIVA){
    		return DeudaAdmin.existeDeudaNotIn(cuenta, listIdsAExcluir,  
    				listIdsRecClaDeuExcluir, fechaVenDeuDes, fechaVenDeuHas, idViaDeudaSelected);
		} 
    	
    	if(idEstadoDeuda.longValue() == EstadoDeuda.ID_JUDICIAL){
    		return DeudaJudicial.existeDeudaNotIn(cuenta, listIdsAExcluir, 
    				listIdsRecClaDeuExcluir, fechaVenDeuDes, fechaVenDeuHas, idViaDeudaSelected);
		}
    	
    	return null;
    }	
    
	
	/**
	 * @return true si la deuda esta en concurso y quiebra.
	 */
    public boolean getEsCyQ() throws Exception {
    	Long idVia = this.getViaDeuda().getId();
    	if (idVia.longValue() == ViaDeuda.ID_VIA_CYQ) {
    		return true;
    	}
    	return false;
    }
	
    /**
     * @return ture si la deuda esta Prescripta 
     */
    public boolean getEsPrescripta()  throws Exception {
    	//ver si cuenta.getEstadoCuenta es '6 - prescripta'
    	return false;
    }
	
    /**
     * @return true si la deuda esta excenta de pago. Osea Importe es 0 && Fecha Vto es null
     */
    public boolean getEsExcentaPago() throws Exception {
    	// Los recursos autoliquidables no cumplen la regla
    	if (this.getRecurso().getEsAutoliquidable().intValue() == 1){
    		return false;
    	} else {
    		// si saldo cero y fecha de pago nula, es una exencion
	    	if (this.getSaldo().doubleValue()==0D
	    			&& this.getFechaPago()==null ) {
	    		return true;
	    	} else {
	    		return false;
	    	}
    	}
    }
	
	
    /**
     * @return true si la deuda es indeterminada. Esta funcion realiza una consulta SQL al sistema 
     * de indeterminados.
     */
    public boolean getEsIndeterminada() throws Exception {
    	//DemodaTimer dt = new DemodaTimer();
    	
    	boolean esIndet = IndeterminadoFacade.getInstance().getEsIndeterminada(this);
    	
    	//log.info(dt.stop("		LiqDeuda :: getEsIndeterminada"));
    	
    	return esIndet;
    }
	    
	/**
	 * @return true si la deuda posee marca de reclamada en siat
	 */
    public boolean getEsReclamada() throws Exception {
    	if (this.getReclamada() != null && this.getReclamada().intValue() == 1)
    		return true;
    	else
    		return false;
    }
	
    /**
     * se utiliza para verificar que una deuda no se encuentre en el asentamiento
     * deberia generarse otra columna, pero no se hacer por el impacto que representa alterar las tablas
     * @return
     * @throws Exception
     */
    public boolean getEstaEnAsentamiento() {
    	if (EstadoReclamo.EN_ASENTAMIENTO.intValue() == this.getReclamada().intValue() ||
    			(RecClaDeu.ABR_IRS.equals(this.getRecClaDeu().getAbrClaDeu()) && null != AuxDeuMdf.getByIdDeuda(this.getId()))){
    		return true;
    	}else{
    		return false;
    	}	
    }
    
    
    /**
     * @return true si la deuda esta en convenio
     */
    public boolean getEsConvenio() throws Exception {
    	if (getIdConvenio() != null)
    		return true;
    	else
    		return false;
    }

    /**
     * Chequea si existe en constancia de envio y 
     * si la misma se encuentra en estado habilitada.
     * 
     * @author Cristian
     * @return true si existe en constancia y misma se encuentra habilitada
     * @throws Exception
     */
    public boolean existeEnConstanciaHabilitada() throws Exception {
    	return ConDeuDet.existeEnConstanciaHabilitada(this.getId());
    }
    
    /**
     * Calcula el recargo segun la actualizacion del saldo al dia de hoy.
     * @return recargo al dia de hoy correspondiente al saldo 
     */
    public DeudaAct actualizacionSaldo() throws Exception {
    	
    	boolean exentaAct = this.getCuenta().exentaActualizacion( new Date(), getFechaVencimiento());
    	log.debug(" ### actualizarSaldo: " + this.getStrPeriodo() + " exentaActualizacion: " + exentaAct);
    	
    	return ActualizaDeuda.actualizar(this.getFechaVencimiento(), this.getSaldo(), exentaAct, this.esActualizable());
    }
    
    
    /**
     * Calcula el recargo segun la actualizacion del importe al dia de hoy.
     * @return recargo al dia de hoy correspondiente al importe 
     */
    public DeudaAct actualizacionImporte(Date fechaActualizacion) throws Exception {    	    	
    	return ActualizaDeuda.actualizar(fechaActualizacion, this.getFechaVencimiento(), this.getImporte(), false, true);
    }
    
    
    /**
     * Calcula el recargo segun la actualizacion para la fecha recibida.
     * 
     * @return recargo a la fecha recibida.
     * @throws Exception
     */
    public DeudaAct actualizacionSaldo(Date fechaActualizacion) throws Exception {
    	
    	boolean exentaAct = this.getCuenta().exentaActualizacion(fechaActualizacion, getFechaVencimiento());
    	log.debug(" ### actualizarSaldo: " + this.getStrPeriodo() + " exentaActualizacion: " + exentaAct);
 
    	return ActualizaDeuda.actualizar(fechaActualizacion, this.getFechaVencimiento(), this.getSaldo(), exentaAct, this.esActualizable());
    }

    /**
     * Calcula el recargo seguan la acutalizacion para la fecha recibida.
     * Toma como parametro el cache de Exenciones que tiene que verificar.
     * 
     * @return recargo a la fecha recibida.
     * @throws Exception
     */
    public DeudaAct actualizacionSaldo(Date fechaActualizacion, CueExeCache cache) throws Exception {
    	
    	boolean exentaAct = false;
    	List<Exencion> listExe = cache.getListExencion(this.getCuenta().getId());
    	
    	if (listExe != null) {
	    	for (Exencion exe: listExe) {
	    		if (exe.getActualizaDeuda() != null && exe.getActualizaDeuda().longValue() == 0) {
	    			exentaAct = true;
	    			break;
	    		}
	   	    }
    	}
 
    	return ActualizaDeuda.actualizar(fechaActualizacion, this.getFechaVencimiento(), this.getSaldo(), exentaAct, this.esActualizable());
    }

    /**
     * Calcula el recargo segun la actualizacion para la fecha recibida.
     * Toma como parametro la lista de Exenciones que tiene que verificar.
     * 
     * @return recargo a la fecha recibida.
     * @throws Exception
     */
    public DeudaAct actualizacionSaldo(Date fechaActualizacion, List<CueExe> listCueExeNoActDeu) throws Exception {
    	
    	boolean exentaAct = false;
    	for (CueExe cueExe : listCueExeNoActDeu){
    		// fedel: 2009-may-19, bug: 687, para que este exenta solo importa si la la cuexe estaba vigente al
    		//        vencimiento. Por eso comnetamos las dos lineas de abajo.
    		//if (DateUtil.isDateInRange(fechaActualizacion, cueExe.getFechaDesde(), cueExe.getFechaHasta())){
   	    	//	exentaAct = true;
   	    	//	break;
   	    	//}
   	    	if (DateUtil.isDateInRange(getFechaVencimiento(), cueExe.getFechaDesde(), cueExe.getFechaHasta())){
   	    		exentaAct = true;
   	    		break;
   	    	}
   	    }
 
    	return ActualizaDeuda.actualizar(fechaActualizacion, this.getFechaVencimiento(), this.getSaldo(), exentaAct, this.esActualizable());
    }
    
    /**
     * Devuelve: 
     *   Abreviatura de Clasificacion Deuda + Periodo + "/" + Anio
     * 
     * @author Cristian
     * @return
     */
    public String getStrPeriodo(){
    	
    	String desRecClaDeu = "";
    	    	
    	if (getRecClaDeu() != null){
    		desRecClaDeu = getRecClaDeu().getAbrClaDeu();
    	}
    	
    	if (!StringUtil.isNullOrEmpty(getStrEstadoDeuda())){
    		if (getPeriodo().intValue() != 0 && getAnio().intValue() != 0){
    			desRecClaDeu += " " + getPeriodo() + "/" + getAnio();
    		}
    		
    		return desRecClaDeu + " " + getStrEstadoDeuda();
    	} else {
    		return desRecClaDeu + " " + getPeriodo() + "/" + getAnio();
    	}
    }
    
    
    /**
     * Retorna cuota/totalCuotas sin la leyenda cuota adelante
     * @return
     */
    public String getStrPeriodoCdmSinCuota(){
    	if (!StringUtil.isNullOrEmpty(getStrEstadoDeuda())){
    		return getStrEstadoDeuda();
    	} else {
    		return getPeriodo() + "/" + getAnio();
    	}
    }

    /**
     * Determina si la deuda es excluida por algun area.
     * Obtiene la lista de CueExcSel activas de la cuenta
     * Por cada CueExcSel: 
     * 		obtiene la lista de la cueExcSelDeu activas:
     * 		Si la lista de cueExcSelDeu activas esta vacia, agrega el area de la cueExcSel.
     * 		Sino: Determina si la deuda esta excluida para cada cueExcSelDeu activa.
     * 			  Si esta excluida: agrega el area de la cueExcSel.
     *  
     * @return List<Area>
     * @throws Exception
     */
    public List<Area> getEsExcluidaPorArea(ExentaAreaCache cache) throws Exception {
    	/*
    	List<Area> listAreaExcluye = new ArrayList<Area>();    	
    	for (CueExcSel cueExcSel : this.getCuenta().getListCueExcSelActivas()) {
    		List<CueExcSelDeu> listCueExcSelDeu = cueExcSel.getListCueExcSelDeuActivas();
    		if(listCueExcSelDeu.size() == 0){
    			listAreaExcluye.add(cueExcSel.getArea());
    		}else{
    			Iterator iter = listCueExcSelDeu.iterator();
    			boolean excluye = false;
    			while (iter.hasNext() && !excluye ) {
    				CueExcSelDeu cueExcSelDeu = (CueExcSelDeu) iter.next();
    				excluye = cueExcSelDeu.getIdDeuda().equals(this.getId());
				}
    			if (excluye){
    				listAreaExcluye.add(cueExcSel.getArea());
    			}
    		}
		}
    	return listAreaExcluye;
    	*/
    	List<Area> listAreaExcluye = new ArrayList<Area>();    	
		List<Long> listIdArea = cache.esDeudaExcluida(this);
		for (Long idArea : listIdArea) {
			Area area = Area.getById(idArea);
			listAreaExcluye.add(area);
		}
		return listAreaExcluye;    	
    }
    
    /**
     * Posee exenciones vigentes que no permiten el envio a judicial
     * @return null si no posee exenciones sino, la primer excencion
     * que no permite el envio.
     * @throws Exception
     */
    public Exencion getEsExentaEnvioJud() throws Exception {
    	// por cada execue en this.cuenta.listExeCueVigentes
    	//    buscar si alguna posee exencion.enviaJudicial
    	//    verficar por tema de periodos de exencion
    	
		//1.13.Si no lo está, validar que la deuda no se encuentre afectada por ninguna exención que impida el envío a judiciales. Esto es, 
		//1.13.1.Que no exista en la tabla exe_cueExe la cuenta asociada a la deuda, considerando la vigencia de los registros respecto de la fecha de envío, 
    	// es decir, si no está vigente, la deuda no se encuentra afectada por la exención..

    	List<CueExe> listCueExe = this.getCuenta().getListCueExeVigente(this.getFechaEmision());
    	for (CueExe cueExe : listCueExe) {
    		if (!SiNo.SI.getId().equals(cueExe.getExencion().getEnviaJudicial())){
    			// indica que la exencion no envia a judicial, es decir, la exencion genera la exclusion del envio a judiciales 
    			return cueExe.getExencion();
    			//1.13.2.Si existe y está vigente, validar que dicha exención no permita al envío a judiciales (campo envioJudicial de la tabla exe_exencion igual a 1).
    		}
		}
    	// no aplica ninguna exencion: retorna nulo
    	return null;
    }
 
    /**
     * Indica si la deuda esta vencida al dia de hoy
     * @return
     */
    public boolean estaVencida(){
    	if(fechaVencimiento.before(new Date()))
    		return true;
    	return false;
    }

    /**
     * Devuelve el Valor del Atributo asociado a la Deuda vigente en la Fecha pasada.
     * 
     * @param idAtributo
     * @param fechaVigencia
     * @return
     */
    public String getValorAtributo(Long idAtributo, Date fechaVigencia){
    	return this.getCuenta().getValorAtributo(idAtributo, fechaVigencia);
    }
    
    /**
     * Obtiene el Detalle de Constancia de la deuda en estado activo. (Vigente)
     * @return conDeuDet
     */
    public ConDeuDet obtenerConDeuDetVigente(){
    	
    	ConDeuDet conDeuDet = GdeDAOFactory.getConDeuDetDAO().getConDeuDetVigenteByDeuda(this);
    	
    	return conDeuDet;
    }
    
	public DeudaVO toVOForView() throws Exception{
		
		if (this.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_JUDICIAL){
			DeudaJudicialVO deudaJudicialVO = (DeudaJudicialVO) this.toVO(1, false);
			deudaJudicialVO.setListDeuRecCon(new ArrayList());
			if(this.getListDeuRecCon() != null){
				List<DeuJudRecConVO> listDeuJudRecConVO = new ArrayList<DeuJudRecConVO>();
				for(DeuRecCon deuRecCon: this.getListDeuRecCon()){
					DeuJudRecConVO deuJudRecConVO = new DeuJudRecConVO();
					deuJudRecConVO.setDescripcion(deuRecCon.getRecCon().getDesRecCon());
					deuJudRecConVO.setImporte(String.valueOf(deuRecCon.getImporte()));
					listDeuJudRecConVO.add(deuJudRecConVO);
				}
				deudaJudicialVO.setListDeuRecCon(listDeuJudRecConVO);
			}
			return deudaJudicialVO;
		}
		
		DeudaVO deudaVO = (DeudaVO) this.toVO(1, false);
		deudaVO.setListDeuRecCon(new ArrayList());
		
		if (this.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_ADMINISTRATIVA){
			if(this.getListDeuRecCon() != null){
				List<DeuAdmRecConVO> listDeuAdmRecConVO = new ArrayList<DeuAdmRecConVO>();
				for(DeuRecCon deuRecCon: this.getListDeuRecCon()){
					DeuAdmRecConVO deuAdmRecConVO = new DeuAdmRecConVO();
					deuAdmRecConVO.setDescripcion(deuRecCon.getRecCon().getDesRecCon());
					deuAdmRecConVO.setImporte(String.valueOf(deuRecCon.getImporte()));
					listDeuAdmRecConVO.add(deuAdmRecConVO);
				}
				deudaVO.setListDeuRecCon(listDeuAdmRecConVO);
			}
		}
		return deudaVO;
	}

	/**
	 * Hace el toVO de nivel 0 sin las listas
	 * @return DeudaJudicialVO o DeudaVO
	 */
	public DeudaVO toVOForViewSinConceptos() throws Exception{
		if (this.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_JUDICIAL){
			DeudaJudicialVO deudaJudicialVO = (DeudaJudicialVO) this.toVO(0, false);
			deudaJudicialVO.setListDeuRecCon(new ArrayList());
			return deudaJudicialVO;
		}
		
		DeudaVO deudaVO = (DeudaVO) this.toVO(0, false);
		deudaVO.setListDeuRecCon(new ArrayList());
		
		return deudaVO;
	}
	
	/* input deve estar en formato <id>valor</id>...
	 * id deve ser entero positivo
	 * valor deve ser un decimal con PUNTO como separador de decimal
	 * pej: <1>12.1</1><45>456.9</45><3>23.2</3>
	 */ 
	public List<DeuRecCon> getListDeuRecConByString() {
		Pattern p = Pattern.compile("<(\\S+?)>(.*?)</(\\1)>");
		Matcher m = p.matcher(this.getStrConceptosProp());

		List<DeuRecCon> deudas = new ArrayList<DeuRecCon>();
		
		int pos = 0;
		while (m.find(pos)) {
			Long idConcepto;
			Double valorConcepto;
			
			//TODO parseValoersDeuda validar que m.groupCount sea 3
			//TODO parseValoresDeuda manejar mejor los errores
			idConcepto = Long.valueOf(m.group(1));
			valorConcepto = Double.valueOf(m.group(2));
			pos = m.end();
			
			if(valorConcepto != 0D){

				DeuRecCon deuRecCon; 
				if(this.getEstadoDeuda().getId() == EstadoDeuda.ID_JUDICIAL) {
				     deuRecCon = new DeuJudRecCon();
				} else if(this.getEstadoDeuda().getId() == EstadoDeuda.ID_ADMINISTRATIVA) {
				     deuRecCon = new DeuAdmRecCon();
				} else if(this.getEstadoDeuda().getId() == EstadoDeuda.ID_ANULADA) {
				     deuRecCon = new DeuAnuRecCon();
				} else if(this.getEstadoDeuda().getId() == EstadoDeuda.ID_CONDONADA) {
				     deuRecCon = new DeuAnuRecCon();
				} else if(this.getEstadoDeuda().getId() == EstadoDeuda.ID_PRESCRIPTA) {
				     deuRecCon = new DeuAnuRecCon();
				} else if(this.getEstadoDeuda().getId() == EstadoDeuda.ID_CANCELADA) {
				     deuRecCon = new DeuCanRecCon();
				} else {
				   //XXX logear
				   deuRecCon = new DeuAdmRecCon();
				}
		    	
				RecCon recCon = new RecCon();
				recCon.setId(idConcepto);
				
				deuRecCon.setRecCon( recCon);
				deuRecCon.setImporte(valorConcepto); 
				deuRecCon.setImporteBruto(valorConcepto);
				deuRecCon.setSaldo(valorConcepto); 
				
				deuRecCon.setDeuda(this);
				
				deudas.add(deuRecCon);				
			}
		}
		return deudas;
	}

	/**
	 * Ordena la lista de deudas pasada como parametro, por periodo y anio
	 * @param listDeuda
	 * @return
	 * @author arobledo
	 */
    public static List<Deuda> ordenarPeriodoAnio(List<Deuda> listDeuda){
    	Comparator<Deuda> comparator = new Comparator<Deuda>(){
			public int compare(Deuda d1, Deuda d2) {
				// Se compara el anio
				if(d1.getAnio().longValue()>d2.getAnio().longValue()){
					return 1;
				}else if(d1.getAnio().longValue()<d2.getAnio().longValue()){
					return -1;
				}else{
					// Se compara el periodo
					if(d1.getPeriodo().longValue()>d2.getPeriodo().longValue()){
						return 1;
					}else if(d1.getPeriodo().longValue()<d2.getPeriodo().longValue()){
						return -1;
					}													
				}
				//Si son iguales
				return 0;
			}    		
    	};    	
    	Collections.sort(listDeuda, comparator);
    	return listDeuda;
    }

	/** Genera el String de Conceptos de Recurso de la deuda para 
	 * los conceptos en la lista listRecCon
	 *
	 * @param listRecCon
	 * @return void
	 */
	public void setStrConceptosByListRecCon(List<? extends DeuRecCon> listRecCon) {

    	String strCon ="";
    	for(DeuRecCon d:listRecCon){
    		log.debug(d.getRecCon().getId()+"    "+d.getImporte());
    		strCon +="<"+d.getRecCon().getId()+">";
    		strCon += NumberUtil.truncate(d.getImporte(), SiatParam.DEC_IMPORTE_DB);
    		strCon +="</"+d.getRecCon().getId()+">";
    	}
    	
    	this.setStrConceptosProp(strCon);
	}
	
	public static List<Long> getListIdDeudaAuxPagDeu(List<Long> listIdDeudas) throws Exception{
		
		return BalDAOFactory.getAuxPagDeuDAO().getListIdDeudaByListId(listIdDeudas);
	}

	/**
	 * Obtiene una lista de Deudas con importe>0 y  saldo=0, filtrando por los valores pasados como parametro.<br>
	 * Busca en las tablas admin, judicial y canceladas
	 * @param cuenta
	 * @param listIdsRecClaDeu
	 * @param fechaPagoDes
	 * @param fechaPagoHasta
	 * @return
	 */
	public static List<Deuda> GetListImporteCancelado(Cuenta cuenta, Long[] listIdsRecClaDeu,Date fechaPagoDes, 
																						Date fechaPagoHasta) {
		List<Deuda> listDeuda = new ArrayList<Deuda>();
		
		// Busca en admin
		listDeuda.addAll(GdeDAOFactory.getDeudaAdminDAO().GetListImporteCancelado(cuenta, listIdsRecClaDeu, 
																				fechaPagoDes, fechaPagoHasta));
		// Busca en judicial
		listDeuda.addAll(GdeDAOFactory.getDeudaJudicialDAO().GetListImporteCancelado(cuenta, listIdsRecClaDeu,
																				fechaPagoDes, fechaPagoHasta));
		// Busca en cancelada
		listDeuda.addAll(GdeDAOFactory.getDeudaCanceladaDAO().GetListImporteCancelado(cuenta, listIdsRecClaDeu,
																				fechaPagoDes, fechaPagoHasta));
		
		return listDeuda;
	}


	/**
	 * Determina si la deuda prescribe o no.
	 * Una deuda prescribe a una determinada fecha (fechaTope)
	 * si y solo si: 
	 * 	 - No esta en un convenio vigente.
	 * 	 - No esta en un procedimiento CyQ.
	 * 	 - No esta en gestion judicial.
	 *   - Si estuvo en un convenio, hoy recompuesto, que esta
	 *     caduco a la fechaTope.
	 *
	 * @param fechaTope
	 * @return string "Prescribe" si la deuda prescribe o 
	 * 		   una string con el motivo de no prescripcion 
	 *		   en caso contrario  
	 * */
	public String prescribe(Date fechaTope) throws Exception {
		// Obtenemos el convenio de la deuda
		Convenio convenio = getConvenio();
		
		// Si esta en un convenio, no prescribe
		if (getEsConvenio()) {
			return "Deuda en convenio. Nro. Convenio: " + convenio.getNroConvenio();
		}
		
		// Si esta en procedimiento de CyQ, no prescribe
		if (getEsCyQ()) {
			return  "Deuda en procedimiento de CyQ. Nro. Procedimiento: " 
							+ getProcedimientoCyQ().getNumero() ;
		}

		// Si esta en gestion judicial, no prescribe
		if (estadoDeuda.getId().equals(EstadoDeuda.ID_JUDICIAL) && 
				estaEnGestionJudicial()) {
			return "Deuda en gestion judicial.";
		}

		// Si no esta en un convenio, pero el ultimo en el que estuvo 
		// (si es que estuvo en alguno) esta recompuesto,y la fecha de 
		// caducidad es posterior a la fecha tope, no prescribe
		if (!getEsConvenio()) {
			// Obtenemos el ultimo convenio en el que estuvo
			Convenio ultCon = Convenio.getLastByDeuda(this); 
			if (ultCon != null) {
				// Fecha de Formalizacion de este
				Date ultConfecFor = ultCon.getFechaFor();
				if (ultConfecFor.after(fechaTope) || !ultCon.estaCaduco(fechaTope)) {
					return "Deuda con convenio recompuesto y caducidad posterior a " + 
										DateUtil.formatDate(fechaTope, DateUtil.ddSMMSYYYY_MASK);
				}
			}
		}
		
		return "Prescribe";
	}

	public void cambiarEstado(EstadoDeuda estadoDestino) throws Exception {
		// Obtenemos el DAO
		DeudaDAO deudaDAO = new DeudaDAO(this.getClass());
		// Cambiamos el estado de la deuda
		deudaDAO.moverDeudaDeEstado(this, getEstadoDeuda(), estadoDestino);
	}

	
	/**
	 * Obtiene una lista de Deudas por Nro de Cuenta, Periodo, Anio, Sistema y Resto. Busca en todas las tablas de deuda y las agrega.
	 * Si no encuentra ninguna devuelve null.
	 * @param nroCta, periodo, anio, nroSistema
	 * @return
	 */
	public static List<Deuda> getListByCtaPerAnioSisRes(Long nroCta, Long periodo, Long anio, Long nroSistema, Long resto){
		List<Deuda> listDeuda = new ArrayList<Deuda>();
		listDeuda.addAll(DeudaAdmin.getListByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema,resto));
		listDeuda.addAll(DeudaJudicial.getListByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema,resto));
		listDeuda.addAll(DeudaCancelada.getListByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema,resto));
		listDeuda.addAll(DeudaAnulada.getListByCtaPerAnioSisRes(nroCta,periodo,anio,nroSistema,resto));
		if(ListUtil.isNullOrEmpty(listDeuda))
			return null;
		return listDeuda;
	}

	
	/**
	 * Devuelve el reccladeu que sea recticficativo para el recurso de la deuda.
	 * 
	 * @return RecClaDeu
	 * @throws Exception
	 */
	public RecClaDeu getRecClaDeuRectificativa() throws Exception {

		RecClaDeu recClaDeuRectif = null;
		
		// Para Drei
		if (this.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_DReI)){
			recClaDeuRectif = RecClaDeu.getById(RecClaDeu.ID_DDJJ_RECTIFICATIVA_DREI);
		// para Etur	
		} else if (this.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_ETuR)){
			recClaDeuRectif = RecClaDeu.getById(RecClaDeu.ID_DDJJ_RECTIFICATIVA_ETUR);
		// para Derecho Publicitario
		} else if (this.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_DPUB)){
			recClaDeuRectif = RecClaDeu.getById(RecClaDeu.ID_DDJJ_RECTIFICATIVA_DERPUB);
		}	
		
		return recClaDeuRectif;
	}
	
	/**
	 * Devuelve true o false segun lo indique la Clasificacion de Deuda.
	 * 
	 * @return boolean
	 */
	public boolean esActualizable(){
		if (this.getRecClaDeu().getActualizaDeuda().intValue() == 1)
			return true;
		else
			return false;
	} 

	public Recibo getReciboImpresion() {
		Recibo recibo = new Recibo();
		Broche broche = this.getCuenta().getBroche();
		recibo.setIdBroche((broche != null) ? broche.getId() : null);
		recibo.setEsReimpresionDeuda(true);
		recibo.setEstaImpreso(SiNo.NO.getBussId());
    	recibo.setCodRefPag(this.getCodRefPag());
    	recibo.setServicioBanco(this.getSistema().getServicioBanco() );
    	recibo.setRecurso(this.getRecurso());
    	recibo.setViaDeuda(this.getViaDeuda());
    	recibo.setCuenta(this.getCuenta());
    	recibo.setCanal(Canal.getById(Canal.ID_CANAL_CMD));
    	recibo.setNroRecibo(0L);
    	recibo.setAnioRecibo(new Integer(0));
    	recibo.setFechaVencimiento(this.getFechaVencimiento());
       	recibo.setAnio(this.getAnio());
    	recibo.setPeriodo(this.getPeriodo());
    	recibo.setIdSistema(this.getSistema().getId());
    	recibo.setTotImporteRecibo(this.getImporte());

		ReciboDeuda reciboDeuda = new ReciboDeuda();
		reciboDeuda.setIdDeuda(this.getId());
		reciboDeuda.setCapitalOriginal(this.getSaldo());
		reciboDeuda.setActualizacion(new Double(0));
		reciboDeuda.setPeriodoDeuda(this.getStrPeriodo());
		reciboDeuda.setConceptos(this.getStrConceptosProp());
		reciboDeuda.setTotCapital(this.getSaldo());
		reciboDeuda.setTotActualizacion(new Double(0));
		reciboDeuda.setTotalReciboDeuda(this.getSaldo());
		reciboDeuda.setAtrEmisionDefinition(this.getAtributosEmisionDefValue());
		
		recibo.getListReciboDeuda().add(reciboDeuda);
		
		return recibo;
	}
	
	/**
	 * Retorna un mapa con los valores de los atributos
	 * utilizados durante el calculo al momento de la 
	 * emision. 
	 * 
	 * @return Map<idAtributo, valor> 
	 */
	public Map<Long,String> getAtributosEmision() {
		HashMap<Long,String> mapAtributos = new HashMap<Long,String>();
		
		if (this.getEmiAtrVal() != null ) {
			Pattern pattern = Pattern.compile("<A(\\d+?)>(.*?)</A(\\1)>");
			Matcher matcher = pattern.matcher(this.getEmiAtrVal());
			
			
			int pos = 0;
			while (matcher.find(pos)) {
				Long idAtributo = Long.valueOf(matcher.group(1));
				String valor = matcher.group(2);
				mapAtributos.put(idAtributo, valor);
				pos = matcher.end();
			}
		}
		
		return mapAtributos;
		
	}

	/**
	 * Retorna el valor del atributo de asentamiento
	 * al momento de la emision.
	 * 
	 * @return atrAseVal 
	 */
	public String getAtrAseVal() {

		// Obtenemos los atributos serializados
		String atrValList = this.getEmiAtrVal();

		if (StringUtil.isNullOrEmpty(atrValList)) {
			return atrValList;
		}
		
		// Por compatibilidad con versiones anteriores 
		// a R 5.1, donde se guardaba unicamente el 
		// atributo de asentamiento.
		if ("0".equals(atrValList) || "1".equals(atrValList) || "2".equals(atrValList)) {
			return atrValList;
		}

		// Retornamos el valor del atributo marcado como 
		// atributo de asentamiento
		return StringUtil.getXMLContentByTag(atrValList, "AtrAse");
	}

	/**
	 * Modifica el valor del atributo de asentamiento.
	 * 
 	 */
	public void setAtrAseVal(String atrAseVal) {

		// Obtenemos los atributos serializados
		String atrValList =  this.getEmiAtrVal();

		// Por compatibilidad con versiones anteriores 
		// a R 5.1, donde se guardaba unicamente el 
		// atributo de asentamiento.
		if (StringUtil.isNullOrEmpty(atrValList) || "0".equals(atrValList) || 
				"1".equals(atrValList) || "2".equals(atrValList)) {
			this.emiAtrVal = StringUtil.writeXMLNode("AtrAse", atrAseVal);
		}
		else { 
			this.emiAtrVal = StringUtil.setXMLContentByTag(atrValList, "AtrAse", atrAseVal);
		}
	}

	
	/**
	 * Paresa la string de atributos de emision y 
	 * construye un definition especial para mostrarlos.
	 * 
	 * @return atrEmisionDefinition
	 */
   public AtrEmisionDefinition getAtributosEmisionDefValue() {

	   AtrEmisionDefinition  atributosEmisionDef = new AtrEmisionDefinition();
	   log.debug("Obteniendo los atributos de emision");
	   try {
		   // Obtenemos el mapa con los atributos al momento de la emision
		   Map<Long,String> mapAtributosEmision = this.getAtributosEmision();
		   // Obtenemos el atributo que representa tablas de atributos
		   Atributo tablaAtr =  Atributo.getByCodigo(Atributo.COD_TABLAATR);
		   
		   for (Long idAtributo: mapAtributosEmision.keySet()) {
			   // Si es un atributo normal
			   if (!tablaAtr.getId().equals(idAtributo)) {
					GenericAtrDefinition genAtrDef = new GenericAtrDefinition();
					genAtrDef.setAtributo((AtributoVO) Atributo.getById(idAtributo).toVO(2));
				    log.debug("Copiando Atributo: " + genAtrDef.getAtributo().getDesAtributo());
					genAtrDef.addValor(mapAtributosEmision.get(idAtributo));
					log.debug("Copiando Valor: " + genAtrDef.getValorString());
					atributosEmisionDef.addAtributo(genAtrDef);

				// Si es un atributo tabla
			   } else {
				    String[] strTabla = mapAtributosEmision.get(idAtributo).split(";");

				    String header = strTabla[0];
				    log.debug("Header de Tabla de Atributos: " + header);

				    String body = strTabla[1];
					log.debug("Body de Tabla de Atributos: " + body);
					
					List<List<GenericAtrDefinition>> tabla = new ArrayList<List<GenericAtrDefinition>>();
					
					List<Atributo> tablaAtrColumns = new ArrayList<Atributo>();
					for (String column: header.split("\\|")) {
						if (column.equals("")) break;
						Long idAtr = Long.parseLong(column); 
						Atributo colAtr = Atributo.getById(idAtr);
						tablaAtrColumns.add(colAtr);
					}

					for (String strRow: body.split(",")) {
						if (strRow.equals("")) continue;
						List<GenericAtrDefinition> row = new ArrayList<GenericAtrDefinition>();
						int i=0;
						for (String rowEntry: strRow.split("\\|")) {
							if (rowEntry.equals("")) continue;
							GenericAtrDefinition genAtrDef = new GenericAtrDefinition();
							genAtrDef.setAtributo((AtributoVO) tablaAtrColumns.get(i).toVO(2));
							genAtrDef.addValor(rowEntry);
							row.add(genAtrDef);	
							i++;
						}
						tabla.add(row);
					}
			
					atributosEmisionDef.setTablaAtributos(tabla);
			   }
		  }
		  
		  log.debug("Se han obteniendo los atributos de emision");
		   
		  return atributosEmisionDef;
		  
	   } catch (Exception e) {
		   log.error("No se pudieron obtener los atributos de emision", e);
		   return null;
	   }    
   } 
   
   
   public String[] obtenerPlanilla(){
	   return GdeDAOFactory.getDeudaDAO().obtenerPlanilla(this);
   }
   
   public Double getImporteIndexEnvio() throws Exception{
	   Double indexEnvio=0D;
	   
	   Recurso tgi = Recurso.getTGI();
	   
	   if(this.getRecurso().getId().longValue()== tgi.getId().longValue()){
		   if (this.viaDeuda.equals(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN))){
			   DeuAdmRecCon deuAdmRecCon = GdeDAOFactory.getDeuAdmRecConDAO().getByDeudaAndRecCon(this, RecCon.getByIdRecursoAndCodigo(tgi.getId(), RecCon.COD_INDEX_ENVIO));
			   if (deuAdmRecCon != null)
				   indexEnvio = deuAdmRecCon.getImporte();
		   }
		   
		   if (this.viaDeuda.equals(ViaDeuda.getById(ViaDeuda.ID_VIA_JUDICIAL))){
			   DeuJudRecCon deuJudRecCon = GdeDAOFactory.getDeuJudRecConDAO().getByDeudaAndRecCon(this, RecCon.getByIdRecursoAndCodigo(tgi.getId(), RecCon.COD_INDEX_ENVIO));
			   if (deuJudRecCon != null)
				   indexEnvio = deuJudRecCon.getImporte();
		   }
	   }
	   
	   return indexEnvio;
   }
   
   /**
    * Retorna true si y solo si existe una deuda en Gestion 
    * Administrativa, Gestion Judicial o Cancelada para la 
    * cuenta, periodo y anio suministrados como argumentos, 
    * con clasificacion de dueda "Original".
    * 
    * En caso de existan mas de una deuda, lanza una 
    * NonUniqueResultException.
    */
   public static boolean existeOriginal(Cuenta cuenta, Long periodo, Long anio) throws NonUniqueResultException {
	   
	   if (cuenta == null || periodo == null || anio == null)
		   throw new IllegalArgumentException("Null parameter");
		   
	   int intAnio = anio.intValue();
	   Deuda deuda = null;	   
	   // Look up en la tabla de deuda administrativa
	   deuda = (Deuda) DeudaAdmin.getByCuentaPeriodoAnio(cuenta,periodo,intAnio);
	   // Si no se encontro la deuda, o se encontro pero no es original
	   if (deuda == null || !deuda.getRecClaDeu().getEsOriginal().equals(1)) {
		   // Look up en la tabla de deuda judicial
		   deuda = (Deuda) DeudaJudicial.getByCuentaPeriodoAnio(cuenta,periodo,intAnio);
	   }
	   // Si no se encontro la deuda, o se encontro pero no es original
	   if (deuda == null || !deuda.getRecClaDeu().getEsOriginal().equals(1)) {
		   // Look up en la tabla de deuda cancelada
		   deuda = (Deuda) DeudaCancelada.getByCuentaPeriodoAnio(cuenta,periodo,intAnio);
	   }
	
	   return (deuda != null) && (deuda.getRecClaDeu() != null) && (deuda.getRecClaDeu().getEsOriginal().equals(1));
   }
}