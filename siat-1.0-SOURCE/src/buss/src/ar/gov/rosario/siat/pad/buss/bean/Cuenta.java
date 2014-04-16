//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.bal.buss.bean.Accion;
import ar.gov.rosario.siat.bal.buss.bean.BalDefinicionManager;
import ar.gov.rosario.siat.bal.buss.bean.Canal;
import ar.gov.rosario.siat.bal.buss.bean.Sellado;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.CasSolicitudManager;
import ar.gov.rosario.siat.cas.buss.bean.Solicitud;
import ar.gov.rosario.siat.cas.buss.bean.TipoSolicitud;
import ar.gov.rosario.siat.cas.iface.util.CasError;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.Calendario;
import ar.gov.rosario.siat.def.buss.bean.Categoria;
import ar.gov.rosario.siat.def.buss.bean.RecAtrCue;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.Seccion;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.buss.bean.TipoDeuda;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.buss.bean.Zona;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.model.GenericDefinition;
import ar.gov.rosario.siat.def.iface.model.RecAtrCueVO;
import ar.gov.rosario.siat.def.iface.model.RecursoDefinition;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.exe.buss.bean.CueExeCache;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.buss.dao.ExeDAOFactory;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.ActualizaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.DatosReciboCdM;
import ar.gov.rosario.siat.gde.buss.bean.DesEsp;
import ar.gov.rosario.siat.gde.buss.bean.DesGen;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAct;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAnulada;
import ar.gov.rosario.siat.gde.buss.bean.DeudaCancelada;
import ar.gov.rosario.siat.gde.buss.bean.DeudaJudicial;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConvenio;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.LiqDeudaBeanHelper;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import ar.gov.rosario.siat.gde.buss.bean.Procurador;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.DatosReciboCdMVO;
import ar.gov.rosario.siat.gde.iface.model.EstadoCuentaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.InformeDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.InformeDeudaCaratula;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.dao.CatastroJDBCDAO;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.BroCueVO;
import ar.gov.rosario.siat.pad.iface.model.BrocheVO;
import ar.gov.rosario.siat.pad.iface.model.CalleVO;
import ar.gov.rosario.siat.pad.iface.model.ConAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteDefinition;
import ar.gov.rosario.siat.pad.iface.model.CuentaTitularVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.model.LocalidadVO;
import ar.gov.rosario.siat.pad.iface.model.ObjImpVO;
import ar.gov.rosario.siat.pad.iface.model.RecAtrCueDefinition;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.rec.buss.bean.Obra;
import ar.gov.rosario.siat.rec.buss.bean.PlaCuaDet;
import ar.gov.rosario.siat.rec.buss.bean.TipoObra;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaTimer;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.DemodaStringMsg;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Cuenta
 * Esta clase se encarga de manejar lo referente a las cuentas de usuarios siat.
 * @author tecso
 *
 */
@Entity
@Table(name = "pad_cuenta")
public class Cuenta extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	public static final String SEQUENCE_COD_GES_PER = "pad_cuenta_cges_sq";
		
	public static final String VALOR_REGIMEN_GENERAL = "1";
	public static final String VALOR_REGIMEN_SIMPLIFICADO="2";
	/**
	 * Es la cant maxima de registros que puede haber por recibo, en la reconfeccion.<br>
	 * estaba en 20, se cambio a 52 porque ahora el recibo se hace en 2 columnas y con una letra mas chica.
	 */
	public static final Integer MAX_CANT_DEUDA_X_RECIBO_RECONFECCION = 48;
	public static final Integer MAX_CANT_DEUDA_X_RECIBO_RECONFECCION_GRE = 12;
	public static final Integer MAX_CANT_DEUDA_X_RECIBO_RECONFECCION_DREI = 12;
	public static final Integer MAX_CANT_DEUDA_X_RECIBO_TGI = 16;
	
	@Transient
	Logger log = Logger.getLogger(Cuenta.class);
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idRecurso") 
	private Recurso recurso;

	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idObjImp") 
	private ObjImp objImp;
	
	@Column(name = "numeroCuenta")
	private String numeroCuenta;

	@Column(name = "codGesCue")
	private String codGesCue;
	
	@Column(name = "catDomEnv")
	private String catDomEnv;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idDomicilioEnvio") 
	private Domicilio domicilioEnvio;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idBroche") 
	private Broche broche;

	@Column(name = "fechaAlta")
	private Date fechaAlta;

	@Column(name = "fechaBaja")
	private Date fechaBaja;

	@Column(name = "esLitoralGas")
	private Integer esLitoralGas;
	
	@Column(name = "esObraPeatonal")
	private Integer esObraPeatonal;
	
	@Column(name = "desDomEnv")
	private String desDomEnv;

	@Column(name = "nomTitPri")
	private String nomTitPri;

	@Column(name = "cuitTitPri")
	private String cuitTitPri;
	
	@Column(name = "esExcluidaEmision")
	private Integer esExcluidaEmision;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idCuenta")
	@OrderBy(clause="fechaDesde")
	private List<CuentaTitular> listCuentaTitular;

	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idCuenta")
	private List<CamDomWeb> listCamDomWeb;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idCuenta")
	@OrderBy(clause="fechaFor desc")
	private List<Convenio> listConvenios;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="cuenta")
	@JoinColumn(name="idCuenta")
	@OrderBy(clause="fechaDesde")
	private List<RecAtrCueV> listRecAtrCueV;

	@Column(name = "permiteEmision")
	private Integer permiteEmision;

	@Column(name = "permiteImpresion")
	private Integer permiteImpresion;

	@Column(name = "observacion")
	private String observacion;
	
	@Transient
	private boolean esHija;

	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idEstObjImp")
	private EstObjImp estObjImp;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idEstCue")
	private EstCue estCue;
	
	/*
	 * Usada como 'cache' para acerlar las consultas de deudas exentas.
	 */
	@Transient
	private List<CueExe> listCueExeExentaEnvioCache = null; 

	/*
	 * Utilizada para optimizar la liquidacion de la deuda.
	 */
	@Transient
	private List<CueExe> listCueExeVigentesCache = null; 
	@Transient
	private List<CueExe> listCueExeEnTramiteCache = null; 
	@Transient
	private List<CueExe> listCueExeNoActDeudaCache = null;
	
	
	/*
	 * - Se utiliza para liquidacion de la deuda
	 * - Se utiliza para recuperar la deuda en via admin y via cyq. 
	 */
	@Transient
	private List<DeudaAdmin> listDeudaAdminRaw  = null; 
    
	/*	
	 * - Se utiliza para liquidacion de la deuda
	 * - Se utiliza para recuperar la deuda en via judicial y via cyq.
	 */
	@Transient
	private List<DeudaJudicial> listDeudaJudicialRaw   = null; 
	
	/*
	 * - Se utiliza para liquidacion de la deuda
	 * - representa los titulares de la cuenta con solo el id seteado
	 * - Los recupera el metodo getListTitularesCuentaLight.
	 * - Los utiliza el metodo getUnionConAtrVal
	 */
	@Transient
	private Long[] listIdsTitulares = null;
	//private List<Persona> listTitulContribuyentesRaw = null;
	
	
	/**
	 * Objeto contenedor de los filtros de ingreso a liquidacion de deuda a pertir de R4 
	 * para soportar recursos autoliquidables.
	 * 
	 */
	@Transient
	private LiqCuentaVO liqCuentaFilter = null;
	
	public Cuenta() {
		super();
	}

	/* no borrar hasta finalizar R11.1*/
	@Deprecated
	public List<DeudaAdmin> obtenerDeudaAdmin() {
		List<DeudaAdmin> listDeudaAdmin = GdeDAOFactory.getDeudaAdminDAO().obtenerDeudaAdmin(this);
		return listDeudaAdmin;
	}
	
	public static List<Cuenta> obtenerCuentaTGIbySeccionManzana(Integer seccion, Integer manzana){		
		List<Cuenta> listCuentasTGI = new ArrayList<Cuenta>();
		return listCuentasTGI;
	}
	
	// Getters y Setters
	public String getCodGesCue() {
		return codGesCue;
	}
	public void setCodGesCue(String codGesCue) {
		this.codGesCue = codGesCue;
	}
	public Domicilio getDomicilioEnvio() {
		return domicilioEnvio;
	}
	public void setDomicilioEnvio(Domicilio domicilioEnvio) {
		this.domicilioEnvio = domicilioEnvio;
	}
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	public ObjImp getObjImp() {
		return objImp;
	}
	public void setObjImp(ObjImp objImp) {
		this.objImp = objImp;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public Date getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}
	public Integer getEsLitoralGas() {
		return esLitoralGas;
	}
	public void setEsLitoralGas(Integer esLitoralGas) {
		this.esLitoralGas = esLitoralGas;
	}
	public List<CuentaTitular> getListCuentaTitular() {
		return listCuentaTitular;
	}
	public void setListCuentaTitular(List<CuentaTitular> listCuentaTitular) {
		this.listCuentaTitular = listCuentaTitular;
	}
	
	public boolean getEsHija() {
		return esHija;
	}
	public void setEsHija(boolean esHija) {
		this.esHija = esHija;
	}

	public Integer getPermiteEmision() {
		return permiteEmision;
	}
	public void setPermiteEmision(Integer permiteEmision) {
		this.permiteEmision = permiteEmision;
	}

	public Integer getPermiteImpresion() {
		return permiteImpresion;
	}
	public void setPermiteImpresion(Integer permiteImpresion) {
		this.permiteImpresion = permiteImpresion;
	}

	public String getDesDomEnv() {
		return desDomEnv;
	}
	public void setDesDomEnv(String desDomEnv) {
		this.desDomEnv = desDomEnv;
	}

	public String getNomTitPri() {
		return nomTitPri;
	}
	public void setNomTitPri(String nomTitPri) {
		this.nomTitPri = nomTitPri;
	}

	public String getCuitTitPri() {
		return cuitTitPri;
	}
	public void setCuitTitPri(String cuitTitPri) {
		this.cuitTitPri = cuitTitPri;
	}

	public Integer getEsExcluidaEmision() {
		return esExcluidaEmision;
	}

	public void setEsExcluidaEmision(Integer esExcluidaEmision) {
		this.esExcluidaEmision = esExcluidaEmision;
	}

	public List<DeudaAdmin> getListDeudaAdminRaw() {
		return listDeudaAdminRaw;
	}

	public void setListDeudaAdminRaw(List<DeudaAdmin> listDeudaAdminRaw) {
		this.listDeudaAdminRaw = listDeudaAdminRaw;
	}

	public List<DeudaJudicial> getListDeudaJudicialRaw() {
		return listDeudaJudicialRaw;
	}

	public void setListDeudaJudicialRaw(List<DeudaJudicial> listDeudaJudicialRaw) {
		this.listDeudaJudicialRaw = listDeudaJudicialRaw;
	}

	public List<CamDomWeb> getListCamDomWeb() {
		return listCamDomWeb;
	}

	public void setListCamDomWeb(List<CamDomWeb> listCamDomWeb) {
		this.listCamDomWeb = listCamDomWeb;
	}
	public Broche getBroche() {
		return broche;
	}
	public void setBroche(Broche broche) {
		this.broche = broche;
	}
	
	public List<Convenio> getListConvenios() {
		return listConvenios;
	}

	public void setListConvenios(List<Convenio> listConvenios) {
		this.listConvenios = listConvenios;
	}
	
	public List<RecAtrCueV> getListRecAtrCueV() {
		return listRecAtrCueV;
	}

	public void setListRecAtrCueV(List<RecAtrCueV> listRecAtrCueV) {
		this.listRecAtrCueV = listRecAtrCueV;
	}

	
	public List<CueExe> getListCueExeVigentesCache() {
		return listCueExeVigentesCache;
	}
	public void setListCueExeVigentesCache(List<CueExe> listCueExeVigentesCache) {
		this.listCueExeVigentesCache = listCueExeVigentesCache;
	}
	
    public Integer getEsObraPeatonal() {
		return esObraPeatonal;
	}

	public void setEsObraPeatonal(Integer esObraPeatonal) {
		this.esObraPeatonal = esObraPeatonal;
	}
	
	/**
     * Devuelve un array Long con los ids de los titulares(Contribuyentes) de esta cuenta.
     * 
     * @author Cristian
     * @return
     */
    public Long[] getListIdsTitulares() throws Exception {
		return this.listIdsTitulares;
    }
	
	public void setListIdsTitulares(Long[] listIdsTitulares) {
		this.listIdsTitulares = listIdsTitulares;
	}

	public EstObjImp getEstObjImp() {
		return estObjImp;
	}

	public void setEstObjImp(EstObjImp estObjImp) {
		this.estObjImp = estObjImp;
	}

	public void setCatDomEnv(String catDomEnv) {
		this.catDomEnv = catDomEnv;
	}

	public String getCatDomEnv() {
		return catDomEnv;
	}

	public LiqCuentaVO getLiqCuentaFilter() {
		return liqCuentaFilter;
	}
	public void setLiqCuentaFilter(LiqCuentaVO liqCuentaFilter) {
		this.liqCuentaFilter = liqCuentaFilter;
	}

	// Metodos de clase	
	public static Cuenta getById(Long id) {
		return (Cuenta) PadDAOFactory.getCuentaDAO().getById(id);
	}
	
	public static Cuenta getByIdNull(Long id) {
		return (Cuenta) PadDAOFactory.getCuentaDAO().getByIdNull(id);
	}
	
	public static List<Cuenta> getList() {
		return (ArrayList<Cuenta>) PadDAOFactory.getCuentaDAO().getList();
	}
	
	public static List<Cuenta> getListActivos() {			
		return (ArrayList<Cuenta>) PadDAOFactory.getCuentaDAO().getListActiva();
	}

	public static Cuenta getByIdRecursoYNumeroCuenta(Long id, String numeroCuenta) throws Exception{
		return (Cuenta) PadDAOFactory.getCuentaDAO().getByIdRecursoYNumeroCuenta(id, numeroCuenta, null);
	}
	
	public static Cuenta getByIdRecursoYNumeroCuenta(Long id, String numeroCuenta, Estado estado) throws Exception{
		return (Cuenta) PadDAOFactory.getCuentaDAO().getByIdRecursoYNumeroCuenta(id, numeroCuenta, estado);
	}
	
	public static Cuenta getByIdRecursoYNumeroCuentaForEC(Long id, String numeroCuenta) throws Exception{
		return (Cuenta) PadDAOFactory.getCuentaDAO().getByIdRecursoYNumeroCuentaForEC(id, numeroCuenta);
	}
	@Deprecated
	public static Cuenta getByIdRecursoYNumeroCuentaYCodGesPer(Long id, String numeroCuenta, String codGesPer) throws Exception{
		return (Cuenta) PadDAOFactory.getCuentaDAO().getByIdRecursoYNumeroCuentaYCodGesPer(id, numeroCuenta, codGesPer, null);
	}
	@Deprecated
	public static Cuenta getByIdRecursoYNumeroCuentaYCodGesPer(Long id, String numeroCuenta, String codGesPer, Estado estado) throws Exception{
		return (Cuenta) PadDAOFactory.getCuentaDAO().getByIdRecursoYNumeroCuentaYCodGesPer(id, numeroCuenta, codGesPer, estado);
	}
	
	/**
	 * Obtiene la lista de Cuentas excluidas (Con CueExcSel con estado activo)
	 * @return List<Cuenta>
	 */
	public static List<Cuenta> getListCuentasExcluidas(){
		return PadDAOFactory.getCuentaDAO().getListCuentasExcluidas();
	}
	
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
		
		// recurso,
		if (this.getRecurso() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_RECURSO);
		}
		// obj imp, ya no es requerido
				
		// nro de cuenta,
		if (StringUtil.isNullOrEmpty(this.getNumeroCuenta())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_NUMEROCUENTA);
		}
	
		// fecha de alta.
		if (this.getFechaAlta() == null ) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_FECHAALTA);
		}
		
		if (hasError()) {
			return false;
		}
		
		if (!"migradrei".equals(DemodaUtil.currentUserContext().getUserName())) {
			// Domicilio -----------------------
			if (this.getDomicilioEnvio() != null){
				this.getDomicilioEnvio().validateForMCR();
				// pasaje de errores y mensajes del domicilio a la cuenta
				this.getDomicilioEnvio().addErrorMessages(this);
			}
		// Fin Domicilio -----------------------
		}
		
		if(Cuenta.getByIdRecursoYNumeroCuenta(this.getRecurso().getId(), this.getNumeroCuenta())!=null){
			addRecoverableError(PadError.CUENTA_EXISTE_NUMERO_CUENTA_PARA_RECURSO);
		}
		
		if (hasError()) {
			return false;
		}
		
		List<RecAtrCue> listRecAtrCueVigentes = this.getRecurso().getListRecAtrCueVigentes(new Date()); 
		
		// Si en la defincion del recurso existen atributos a valorizar
		if(!ListUtil.isNullOrEmpty(listRecAtrCueVigentes)){
		
			log.debug("cuenta.validateCreate: this.recurso.listRecAtrCue -> " + listRecAtrCueVigentes.size());
			
			/*/ Si la cuenta no posee RecAtrCueV, error generarl.
			if(ListUtil.isNullOrEmpty(this.getListRecAtrCueV())){
				log.debug("cuenta.validateCreate: this.ListRecAtrCueV -> vacio o nulo");
				addRecoverableValueError("La cuenta no posee valorizacion para ningun atributo ");
				return false;
			}*/
			
			log.debug("cuenta.validateCreate: this.ListRecAtrCueV -> " + this.getListRecAtrCueV().size());

			boolean recAtrCueEncontrado = false;
			
			// Si el recurso posee atributos a valorizar, la cuenta deberá poseerlos.
			for(RecAtrCue recAtrCue:listRecAtrCueVigentes){
				
				recAtrCueEncontrado = false;
				
				// Validamos la existencia del recAtrCue y su correcta valorizacion.
				for(RecAtrCueV recAtrCueV:this.getListRecAtrCueV()){
					
					if (recAtrCue.getId().equals(recAtrCueV.getRecAtrCue().getId())){
						recAtrCueEncontrado = true;
						
						if (recAtrCue.getEsRequerido().intValue() == 1 && 
								StringUtil.isNullOrEmpty(recAtrCueV.getValor())){
							addRecoverableValueError("El valor del atributo " + 
									recAtrCue.getAtributo().getCodAtributo() + " es requerido");
						}
						
						break;
					}
				}
			
				if (!recAtrCueEncontrado && recAtrCue.getEsRequerido().intValue() == 1)
					addRecoverableValueError("La cuenta no posee el atributo " + recAtrCue.getAtributo().getCodAtributo() + " para ser valorizado");
				
			}
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
		
		//this.validate();
		
		// Fecha Alta
		if (getFechaAlta() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_FECHAALTA);
		}
		
		if(this.fechaBaja != null && DateUtil.isDateBefore(this.fechaBaja, this.fechaAlta)){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, new Object[]{PadError.CUENTA_FECHABAJA, PadError.CUENTA_FECHAALTA});
		}
		
		// TODO que la fecha de alta no sea mayor a la menor fecha desde de cuentaTitular

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
		
		// No Tiene que tener titulares de cuenta
		if(!ListUtil.isNullOrEmpty(this.getListCuentaTitular())){
			addRecoverableError(PadError.CUENTA_CON_TITULARES);
		} 
		
		if (GenericDAO.hasReference(this, Solicitud.class, "cuenta")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				PadError.CUENTA_LABEL , CasError.SOLICITUD_LABEL);
		}
		if (GenericDAO.hasReference(this, CueExe.class, "cuenta")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				PadError.CUENTA_LABEL , ExeError.CUEEXE_LABEL);
		}
		
		if (GenericDAO.hasReference(this, Convenio.class, "cuenta")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				PadError.CUENTA_LABEL , GdeError.CONVENIO_LABEL);
		}
		if (GenericDAO.hasReference(this, DeudaAdmin.class, "cuenta")
				||GenericDAO.hasReference(this, DeudaJudicial.class, "cuenta")
				||GenericDAO.hasReference(this, DeudaAnulada.class, "cuenta")
				||GenericDAO.hasReference(this, DeudaCancelada.class, "cuenta")) {
					addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
							PadError.CUENTA_LABEL , GdeError.DEUDA_LABEL);
		}
		if (GenericDAO.hasReference(this, BroCue.class, "cuenta")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				PadError.CUENTA_LABEL , PadError.BROCUE_LABEL);
		}
		if (GenericDAO.hasReference(this, CamDomWeb.class, "cuenta")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				PadError.CUENTA_LABEL , PadError.CAMBIAR_DOM_ENVIO_LABEL);
		}
		if (GenericDAO.hasReference(this, CuentaRel.class, "cuentaDestino")
			|| GenericDAO.hasReference(this, CuentaRel.class, "cuentaOrigen")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				PadError.CUENTA_LABEL , PadError.CUENTAREL_LABEL);
		}
		if (GenericDAO.hasReference(this, CuentaTitular.class, "cuenta")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				PadError.CUENTA_LABEL , PadError.CUENTATITULAR_LABEL);
		}
		
		return !hasError();
	}
	
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();

		//Tiene que tener titulares de cuenta
		
		if(ListUtil.isNullOrEmpty(this.getListCuentaTitular())){
			addRecoverableError(PadError.CUENTA_SIN_TITULARES);
		} 
		
		return !hasError();
	}

	
	/**
	 * Valida la desactivacion de la Cuenta.
	 * Valida que la Fecha de baja sea distinta de nula y que no sea menor a la Fecha de Alta.
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();

		//Validaciones  
		if(this.fechaBaja == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_FECHABAJA);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Se sacan las siguientes validaciones por nuevos requerimientos. 
		/*
		if(DateUtil.isDateBefore(this.fechaBaja, this.fechaAlta)){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, new Object[]{PadError.CUENTA_FECHABAJA,PadError.CUENTA_FECHAALTA});
		}*/
		
		return !hasError();
	}


	public void activar() {
		if(!this.validateActivar()) {
			return;
		}
		//this.setFechaBaja(null); asi se decidio...
		this.setEstado(Estado.ACTIVO.getId());
		this.setEstCue(EstCue.getById(EstCue.ID_ACTIVO));
		PadDAOFactory.getCuentaDAO().update(this);
	}

	public void desactivar(Date fechaBaja) {
		this.setFechaBaja(fechaBaja);
		if(!this.validateDesactivar()) {
			return;
		}
		
		PadDAOFactory.getCuentaDAO().update(this);
	}
	
	public GenericDefinition getRecCueAtrDefVal (Date fecha) throws Exception{
		return this.getRecCueAtrDefinitionValue(fecha);
	}

	/**
	 * Setea en True el campo CambioTitularIF de la cuenta.
	 */
	public void solicitudPorCambioTitular(String asuSolicitud, String desSolicitud) throws Exception{
		
		if(!ListUtil.isNullOrEmpty(this.getListCueExeVigente())){
			CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_VERIFICAR_EXENCION_CUENTA
					,asuSolicitud,desSolicitud,this);
		}
			
		if(getRecurso().getEsPrincipal()==1){
			for(Cuenta cuentaSec: this.getObjImp().getListCuentaSecundariaActiva()){
				if(cuentaSec.getRecurso().getModiTitCtaPorPri()==1){
					if(!ListUtil.isNullOrEmpty(cuentaSec.getListCueExeVigente())){
						CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_VERIFICAR_EXENCION_CUENTA
								,asuSolicitud,desSolicitud,cuentaSec);
					}
				}
			}
		}
	}
	
	// Administrar CuentaTitular
	
    /**
	 *  Da de alta una CuentaTitular verificando que exista el Contribuyente con idPersona que sale
	 *  de cuentaTitular.getContribuyente().getPersona().getId(). Si no existe lo da de alta.
	 *  Verifica si debe dar de alta CuentaTitular para las cuentas secundarias relacionadas y actua en consecuencia.
	 *  
	 * @param cuentaTitular Datos de la Cuenta y IdPersona
	 * @return CuentaTitular creada
	 * @throws Exception
	 */
	public CuentaTitular createCuentaTitular(CuentaTitular cuentaTitular) throws Exception {

		 Contribuyente contribuyente = Contribuyente.getByIdNull(cuentaTitular.getContribuyente().getPersona().getId());
         
		 // Si el contribuyente no existe como tal, lo creamos a partir del id de Persona
         if (contribuyente == null){
             // creacion a partir de la persona 
         	Persona persona = Persona.getByIdNull(cuentaTitular.getContribuyente().getPersona().getId());
        	contribuyente = PadContribuyenteManager.getInstance().createContribuyente(persona);
    		
    		if (contribuyente.hasError()) {
    			// pasaje de errores y mensajes
    			contribuyente.passErrorMessages(cuentaTitular);
    			return cuentaTitular;
    		}     	
         }
        
        cuentaTitular.setContribuyente(contribuyente);
		cuentaTitular.setCuenta(this);
			
		// Validaciones de negocio
		if (!cuentaTitular.validateCreate()) {
			return cuentaTitular;
		}
		
		PadDAOFactory.getCuentaTitularDAO().update(cuentaTitular);
		
		// Si es Titular principal
		//(Seteo todos los otros cuentaTitular.esTitularPrincipal = NO y los actualizo)
		if (cuentaTitular.getEsTitularPrincipal()!=null && SiNo.SI.getId().intValue() == cuentaTitular.getEsTitularPrincipal().intValue()){
			this.establecerTitularPrincipal(cuentaTitular);
		}
		
		
		// Analizamos impacto en Cuentas Secundarias
		if(this.getRecurso().getEsPrincipal()!=null && this.getRecurso().getEsPrincipal().intValue() == SiNo.SI.getId().intValue()){
			List <Cuenta> listCuentaSecundaria = this.getObjImp().getListCuentaSecundariaActiva();
			if(!ListUtil.isNullOrEmpty(listCuentaSecundaria)){
				for (Cuenta cuentaSec : listCuentaSecundaria) {
					if (cuentaSec.getRecurso().getModiTitCtaPorPri()!=null && cuentaSec.getRecurso().getModiTitCtaPorPri().intValue() == SiNo.SI.getId().intValue()){
				
						CuentaTitular cuentaTitularSec = new CuentaTitular();
						cuentaTitularSec.setCuenta(cuentaSec);
						cuentaTitularSec.setContribuyente(contribuyente);
						cuentaTitularSec.setTipoTitular(cuentaTitular.getTipoTitular());
						cuentaTitularSec.setEsTitularPrincipal(cuentaTitular.getEsTitularPrincipal()); 
						cuentaTitularSec.setFechaNovedad(cuentaTitular.getFechaNovedad());
						cuentaTitularSec.setFechaDesde(cuentaTitular.getFechaDesde());
						cuentaTitularSec.setFechaHasta(cuentaTitular.getFechaHasta());
						cuentaTitularSec.setEsAltaManual(cuentaTitular.getEsAltaManual());
						cuentaTitularSec.setIdCaso(cuentaTitular.getIdCaso());

						PadDAOFactory.getCuentaTitularDAO().update(cuentaTitularSec);

						if (cuentaTitularSec.getEsTitularPrincipal()!=null && cuentaTitularSec.getEsTitularPrincipal().intValue() == SiNo.SI.getId().intValue()){
							cuentaSec.establecerTitularPrincipal(cuentaTitularSec);
						}
						
						if(!cuentaTitularSec.hasError() && cuentaSec.getEstado().intValue()==Estado.CREADO.getId()){
			        		cuentaSec.setEstado(Estado.ACTIVO.getId());
			        		cuentaSec.setEstCue(EstCue.getById(EstCue.ID_ACTIVO));
			        		
			        		PadDAOFactory.getCuentaDAO().update(cuentaSec);
			        	}
					}
				}
			}
		}
		
		return cuentaTitular;
	}
	
	public CuentaTitular updateCuentaTitular(CuentaTitular cuentaTitular) throws Exception {
		
		// Validaciones de negocio
		if (!cuentaTitular.validateUpdate()) {
			return cuentaTitular;
		}
		
		if (SiNo.SI.getId() == cuentaTitular.getEsTitularPrincipal()){
			this.establecerTitularPrincipal(cuentaTitular);
		}

		PadDAOFactory.getCuentaTitularDAO().update(cuentaTitular);
		
		// Analizamos impacto en Cuentas Secundarias
		if(this.getRecurso().getEsPrincipal().equals(SiNo.SI.getId())){
			List <Cuenta> listCuentaSecundaria = this.getObjImp().getListCuentaSecundariaActiva();
			if(!ListUtil.isNullOrEmpty(listCuentaSecundaria)){
				for (Cuenta cuentaSec : listCuentaSecundaria) {
					if (cuentaSec.getRecurso().getModiTitCtaPorPri().equals(SiNo.SI.getId())){
						CuentaTitular cuentaTitularSec = cuentaSec.getCuentaTitularByContribuyente(cuentaTitular.getContribuyente());
						if(cuentaTitularSec!=null){
							
							cuentaTitularSec.setTipoTitular(cuentaTitular.getTipoTitular());
							cuentaTitularSec.setEsTitularPrincipal(cuentaTitular.getEsTitularPrincipal());
														
							if (SiNo.SI.getId().equals(cuentaTitularSec.getEsTitularPrincipal())){
								this.establecerTitularPrincipal(cuentaTitularSec);
							}
							
							PadDAOFactory.getCuentaDAO().update(cuentaTitularSec);
						}
					}
				}
			}
		}
		
		return cuentaTitular;
	}
	
	public CuentaTitular deleteCuentaTitular(CuentaTitular cuentaTitular) throws Exception {
	
		// Validaciones de negocio
		if (!cuentaTitular.validateDelete()) {
			return cuentaTitular;
		}
		
		PadDAOFactory.getCuentaTitularDAO().delete(cuentaTitular);
		
		return cuentaTitular;
	}
	
	public void establecerTitularPrincipal(CuentaTitular cuentaTitularPrincipal) throws Exception {
		// seteo todos los otros cuentaTitular.esTitularPrincipal = NO y los actualizo de la cuenta
		if(!ListUtil.isNullOrEmpty(this.getListCuentaTitular())){
			for (CuentaTitular ct : this.getListCuentaTitular()) {
				if (SiNo.SI.getId().equals(ct.getEsTitularPrincipal()) && 
						!ct.getId().equals(cuentaTitularPrincipal.getId())) {
					
					ct.setEsTitularPrincipal(SiNo.NO.getId());
					PadDAOFactory.getCuentaTitularDAO().update(ct);
				}
			}			
		}
		// Seteo la cuenta titular principal		
		cuentaTitularPrincipal.setEsTitularPrincipal(SiNo.SI.getId());
		
		// Seteamos los campos nomTitPri, cuitTitPri sacados desde la nueva persona
		Cuenta cuenta = cuentaTitularPrincipal.getCuenta();
		Persona persona = PersonaFacade.getInstance().getPersonaById(cuentaTitularPrincipal.getContribuyente().getId());
		cuenta.setCuitTitPri(persona.getCuitFull());

		// recuperamos la descripcion viendo si podemos usar o no el
		//String nomTitPri = PadDAOFactory.getCuentaTitularDAO().getNombreTitularPrincipal(cuenta, true);
		String nomTitPri = persona.getRepresent();
		log.debug("nomTitPri::nomTitPri:: " + nomTitPri);

		nomTitPri = nomTitPri.toUpperCase().replace(",", " ");
		cuenta.setNomTitPri(nomTitPri.toUpperCase());
		PadDAOFactory.getCuentaDAO().update(cuenta);
	}

	// Administrar CuentaRel (Relacion entre Cuentas)
	public CuentaRel createCuentaRel(CuentaRel cuentaRel) throws Exception {
		
		// Validaciones de negocio
		if (!cuentaRel.validateCreate()) {
			return cuentaRel;
		}

		PadDAOFactory.getCuentaRelDAO().update(cuentaRel);

		return cuentaRel;
	}
	
	public CuentaRel updateCuentaRel(CuentaRel cuentaRel) throws Exception {
		
		// Validaciones de negocio
		if (!cuentaRel.validateUpdate()) {
			return cuentaRel;
		}

		PadDAOFactory.getCuentaRelDAO().update(cuentaRel);
		
		return cuentaRel;
	}
	
	public CuentaRel deleteCuentaRel(CuentaRel cuentaRel) throws Exception {
	
		// Validaciones de negocio
		if (!cuentaRel.validateDelete()) {
			return cuentaRel;
		}
		
		PadDAOFactory.getCuentaRelDAO().delete(cuentaRel);
		
		return cuentaRel;
	}

	/**
	 * toVO de propiedades de nivel 0 mas
	 * el recurso de la cuenta de nivel 0.
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public CuentaVO toVOWithRecurso() throws Exception{
		// recurso
		CuentaVO cuentaVO = (CuentaVO) this.toVO(1,false);// se pasa a nivel 1 por issue #8121: Campo "estado" de la tabla pad_cuenta
		cuentaVO.setRecurso((RecursoVO)this.getRecurso().toVO(0));
		
		return cuentaVO;		
	}
	
	public CuentaVO toVOForView() throws Exception{
		
		// recurso, objeto imponible, domicilio
		CuentaVO cuentaVO = (CuentaVO) this.toVO(1);
	
		// localidad del domicilio
		if (this.getDomicilioEnvio() != null && this.getDomicilioEnvio().getLocalidad() != null){
			Localidad localidad = Localidad.getByCodPostSubPost(this.getDomicilioEnvio().getLocalidad().getCodPostal(),
					this.getDomicilioEnvio().getLocalidad().getCodSubPostal());
			if (localidad!=null)
				cuentaVO.getDomicilioEnvio().setLocalidad((LocalidadVO) localidad.toVO(0));
			else
				log.info("No se encontro localidad con cod postal="+this.getDomicilioEnvio().getLocalidad().getCodPostal()+ " y subCod postal="+
						this.getDomicilioEnvio().getLocalidad().getCodSubPostal());
		}
		
		// calle del domicilio
		if (this.getDomicilioEnvio() != null && this.getDomicilioEnvio().getCalle() != null){
			//si es rosario buscamos datos de calle en JAR, sino, usamos el de la columna de pad_domicilio de siat
			if (getDomicilioEnvio().getLocalidad().isRosario()) {
				Calle calle = Calle.getByCodCalle(this.getDomicilioEnvio().getCalle().getCodCalle());
				if (calle == null) {
					//mmmm domiclio de rosario sin info de codigo de calle. tal vez se deva a un problema de migracion
					//en este caso seteamos calle con la info que tenemos en el nombre de la calle del domicilioEnvio
					CalleVO calleVO = cuentaVO.getDomicilioEnvio().getCalle();
					calleVO.setNombreCalle(getDomicilioEnvio().getNombreCalle());
				} else {
					cuentaVO.getDomicilioEnvio().setCalle((CalleVO) calle.toVO(0));
				}
			} else {
				cuentaVO.getDomicilioEnvio().setCalle((CalleVO) this.getDomicilioEnvio().getCalle().toVO(0));
			}
		}
		
		// listas de cuentaTitular
		cuentaVO.setListCuentaTitular(new ArrayList<CuentaTitularVO>());
		for (CuentaTitular cuentaTitular : this.getListCuentaTitular()) {
			cuentaVO.getListCuentaTitular().add(cuentaTitular.toVOForCuenta());
		}
		
		// Me interesa tipo broche pero no la listas
		Broche  broche = this.getBroche();
		if (broche != null) { 
			cuentaVO.setBroche((BrocheVO)broche.toVO(1, false));
			BroCue broCue = BroCue.getVigenteByCuenta(this);
			if (broCue != null){
				cuentaVO.setBroCue((BroCueVO)broCue.toVO(0, false) );
			}else{
				cuentaVO.setBroCue(new BroCueVO());
			}
		}
		return cuentaVO;
	}
	
    /*public CuentaVO toVO4Print() throws Exception{
		
		// recurso, objeto imponible, domicilio
		CuentaVO cuentaVO = (CuentaVO) this.toVO(1);
		
		cuentaVO.setCuentaTitularPrincipal((CuentaTitularVO) this.get.toVOForCuenta());
		// localidad del domicilio
		if (this.getDomicilioEnvio() != null && this.getDomicilioEnvio().getLocalidad() != null){
			Localidad localidad = Localidad.getByCodPostSubPost(this.getDomicilioEnvio().getLocalidad().getCodPostal(),
					this.getDomicilioEnvio().getLocalidad().getCodSubPostal());
			cuentaVO.getDomicilioEnvio().setLocalidad((LocalidadVO) localidad.toVO(0));
		}
		
		// calle del domicilio
		if (this.getDomicilioEnvio() != null && this.getDomicilioEnvio().getCalle() != null){
			//si es rosario buscamos datos de calle en JAR, sino, usamos el de la columna de pad_domicilio de siat
			if (getDomicilioEnvio().getLocalidad().isRosario()) {
				Calle calle = Calle.getByCodCalle(this.getDomicilioEnvio().getCalle().getCodCalle());
				if (calle == null) {
					//mmmm domiclio de rosario sin info de codigo de calle. tal vez se deva a un problema de migracion
					//en este caso seteamos calle con la info que tenemos en el nombre de la calle del domicilioEnvio
					CalleVO calleVO = cuentaVO.getDomicilioEnvio().getCalle();
					calleVO.setNombreCalle(getDomicilioEnvio().getNombreCalle());
				} else {
					cuentaVO.getDomicilioEnvio().setCalle((CalleVO) calle.toVO(0));
				}
			} else {
				cuentaVO.getDomicilioEnvio().setCalle((CalleVO) this.getDomicilioEnvio().getCalle().toVO(0));
			}
		}
		
		// listas de cuentaTitular
		cuentaVO.setListCuentaTitular(new ArrayList<CuentaTitularVO>());
		for (CuentaTitular cuentaTitular : this.getListCuentaTitular()) {
			cuentaVO.getListCuentaTitular().add(cuentaTitular.toVOForCuenta());
		}
		
		// Me interesa tipo broche pero no la listas
		Broche  broche = this.getBroche();
		if (broche != null) { 
			cuentaVO.setBroche((BrocheVO)broche.toVO(1, false));
			BroCue broCue = BroCue.getVigenteByCuenta(this);
			if (broCue != null){
				cuentaVO.setBroCue((BroCueVO)broCue.toVO(0, false) );
			}else{
				cuentaVO.setBroCue(new BroCueVO());
			}
		}
		return cuentaVO;
	}*/

	public CuentaVO toVOForCambioDomicilio() throws Exception{
		
		// recurso, objeto imponible, domicilio
		CuentaVO cuentaVO = (CuentaVO) this.toVO(false);

		// Recurso
		if (this.getRecurso() != null){
			cuentaVO.setRecurso((RecursoVO) this.getRecurso().toVOWithCategoria());
		}
		
		// Domicilio de Envio
		if (this.getDomicilioEnvio() != null){
			cuentaVO.setDomicilioEnvio((DomicilioVO) this.getDomicilioEnvio().toVO());
		}
		
		// localidad del domicilio
		if (this.getDomicilioEnvio() != null && this.getDomicilioEnvio().getLocalidad() != null){
			Localidad localidad = Localidad.getByCodPostSubPost(this.getDomicilioEnvio().getLocalidad().getCodPostal(),
					this.getDomicilioEnvio().getLocalidad().getCodSubPostal());
			cuentaVO.getDomicilioEnvio().setLocalidad((LocalidadVO) localidad.toVO(0));
		}
		
		// calle del domicilio
		if (this.getDomicilioEnvio() != null && this.getDomicilioEnvio().getCalle() != null){
			//si es rosario buscamos datos de calle en JAR, sino, usamos el de la columna de pad_domicilio de siat
			if (getDomicilioEnvio().getLocalidad().isRosario()) {
				Calle calle = Calle.getByCodCalle(this.getDomicilioEnvio().getCalle().getCodCalle());
				if (calle == null) {
					//mmmm domiclio de rosario sin info de codigo de calle. tal vez se deva a un problema de migracion
					//en este caso seteamos calle con la info que tenemos en el nombre de la calle del domicilioEnvio
					CalleVO calleVO = cuentaVO.getDomicilioEnvio().getCalle();
					calleVO.setNombreCalle(getDomicilioEnvio().getNombreCalle());
				} else {
					cuentaVO.getDomicilioEnvio().setCalle((CalleVO) calle.toVO(0));
				}
			} else {
				cuentaVO.getDomicilioEnvio().setCalle((CalleVO) this.getDomicilioEnvio().getCalle().toVO(0));
			}
		}
		
		return cuentaVO;
	}
	
	/**
	 * Hace toVO de nivel 0, sin listas, y setea en null la lista de titualres y de sistema origen.<br>
	 * Ademas setea el recurso con toVOLightForPDF() y la cuenta titular principal
	 * @return
	 * @throws Exception
	 */
	public CuentaVO toVOLightForPDF() throws Exception{
		CuentaVO cuentaVO = (CuentaVO) this.toVO(0, false);
		
		cuentaVO.setObjImp((ObjImpVO) this.objImp.toVOForPDF());
	
    	cuentaVO.setCuentaTitularPrincipal(this.obtenerCuentaTitularPrincipal().toVOForCuenta());
		cuentaVO.setRecurso(recurso.toVOLightForPDF());		
		return cuentaVO;
	}
	
	/**
	 * Hace toVO de nivel 0, sin listas, y setea en null la lista de titualres y de sistema origen.<br>
	 * Ademas setea el recurso con toVOLightForPDF() y la cuenta titular principal
	 * @return
	 * @throws Exception
	 */
	public CuentaVO toVO4Print() throws Exception{
		CuentaVO cuentaVO = (CuentaVO) this.toVO(0, false);
		cuentaVO.setCuentaTitularPrincipal(this.obtenerCuentaTitularPrincipal().toVOForCuenta());
		cuentaVO.setObjImp((ObjImpVO) this.objImp.toVOForPDF());
		cuentaVO.setRecurso(recurso.toVOLightForPDF());		
		return cuentaVO;
	}
	
	// TODO ver si conviene ir por dao directamente
	public Boolean contieneTitular(Contribuyente contribuyente){
		
		return ListUtilBean.contains(this.getListContribuyente(), contribuyente); 
	}
	
	/**
	 * Devuelve la lista de los titulares(Contribuyentes) de la cuenta.
	 * Era privado y lo pase a public
	 * 
	 * @return
	 */
	public List<Contribuyente> getListContribuyente(){
		
		List<Contribuyente> listContribuyente = new ArrayList<Contribuyente>();
		
		for (CuentaTitular ct : this.getListCuentaTitular()) {
			listContribuyente.add(ct.getContribuyente());
		}
		
		return listContribuyente;
	}
	
	/**
	 * Obtiene la lista de cuentas relacionadas con esta cuenta.
	 * <p>No consulta CuentaRel, sino que trae las cuentas que posean el mismo Objeto Imponible 
	 * que tiene esta instancia.</p> 
	 * @return la lista de cuentas. Se excluye de la lista la cuenta
	 * instanciada. 
	 */
	public List<Cuenta> getListCuentaRelacionadas() throws Exception {
		
		// por ahora para prueba
		return PadDAOFactory.getCuentaDAO().getListCuentasRelacionadas(this);
		
	}
	
	/**
	 * Obtiene la lista de cuentas relacionadas por Objeto imponible con estado activo.
	 * Y agrega informacion de la localidad.
	 * 
	 */
	public List<Cuenta> getListCuentaRelacionadasActivas() throws Exception {
		
		List<Cuenta> listCtaActivas = new ArrayList<Cuenta>();
		List<Cuenta> listCta = this.getListCuentaRelacionadas();
		
		// agrego las cuentas activas
		for (Cuenta cuenta : listCta) {
			if (cuenta.isVigente()) {
				// seteo la localidad a la cuenta
				if (cuenta.getDomicilioEnvio() != null) {
					Localidad localidadCta = cuenta.getDomicilioEnvio().getLocalidad();
					Localidad localidad = Localidad.getByCodPostSubPost
						(localidadCta.getCodPostal(), localidadCta.getCodSubPostal());
					cuenta.getDomicilioEnvio().setLocalidad(localidad);
				}
				listCtaActivas.add(cuenta);
			}
		}
		
		return listCtaActivas;

	}
	
	/**
	 * Obtiene las CuentasRel relacionas a esta cuenta por Unificaciones y/o Desgloses. 
	 * 
	 * @return List<CuentaRel>
	 * @throws Exception
	 */
	public List<CuentaRel> getListCuentaRel() throws Exception {
			
		return PadDAOFactory.getCuentaRelDAO().ListCuentaRelByCuenta(this);
	}
	
	/**
	 * Obtiene las CuentasRel relacionas que tiene a esta cuenta como Origen. 
	 * 
	 * @return List<CuentaRel>
	 * @throws Exception
	 */
	public List<CuentaRel> getListCuentaRelByCuentaOrigen() throws Exception {
			
		return PadDAOFactory.getCuentaRelDAO().ListCuentaRelByCuentaOrigen(this);
	}
	
	/**
	 * Si existen Desgloses o Unificaciones, obtiene una lista con las cuentas paraticipantes en los mismos
	 * como Cuenta Madres o Hijas. 
	 * Obteniendo informacion extra de si se trata de una relacion madre o Hija
	 * 
	 * @author Cristian
	 * @return List<Cuenta>
	 * @throws Exception
	 */
	public List<Cuenta> getListCuentasMadreHija() throws Exception {
		List<Cuenta> listCuentaMH = new ArrayList<Cuenta>();
		
		List<CuentaRel> listCuentaRel = this.getListCuentaRel();
		
		Cuenta cuentaAdd; 
		
		for (CuentaRel cr: listCuentaRel){
			// Si es una cuenta Madre
			if(cr.getCuentaOrigen().getId().equals(this.getId())){
				cuentaAdd = cr.getCuentaDestino();
				cuentaAdd.setEsHija(true);
				
				listCuentaMH.add(cuentaAdd);
			} else {
			// Si es una cuenta Hija	
				cuentaAdd = cr.getCuentaOrigen();
				cuentaAdd.setEsHija(false);
				
				listCuentaMH.add(cuentaAdd);
			}			
		}
		     
		return listCuentaMH;
	}
	
	/**
	 * Desactiva el Titular de la cuenta correpondiente al contribuyente del parametro.
	 * @param contibuyente Debe contener el id de contribuyente (igual que el id de persona)
	 * que correponde a la cuenta a desactivar, fechaHasta Fecha de desactivacion.
	 * <p>Si el id contribuyente no corresponde a ningun titular de cuenta el metodo no hace nada.
	 * @throws Exception
	 */
	public void desactivarCuentaTitular(Contribuyente contribuyente, Date fechaHasta) throws Exception {
		CuentaTitular cuentaTitular = 
			PadDAOFactory.getCuentaTitularDAO().getByCuentaYContribuyente(this, contribuyente);		
		if(cuentaTitular!=null){
			cuentaTitular.desactivar(fechaHasta);
			if(cuentaTitular.hasError()){
				for(DemodaStringMsg error:cuentaTitular.getListError()){
					this.addRecoverableError(error);
				}
				return;
			}
		}
	}
	
	/**
	 * Obtiene la relacion Cuenta Titular de esta cuenta para el contribuyente del parametro.
	 * @param contibuyente Debe contener el id de contribuyente (igual que el id de persona)
	 * <p>Si el id contribuyente no corresponde a ningun titular de la cuenta el metodo devuelve null.
	 * @throws Exception
	 */
	public CuentaTitular getCuentaTitularByContribuyente(Contribuyente contribuyente) throws Exception {
		return PadDAOFactory.getCuentaTitularDAO().getByCuentaYContribuyente(this, contribuyente);		
	}
	
	public Date getFechaInicioVig() {
		if (this.getEstado().intValue() == Estado.CREADO.getId()) return null;
		return this.getFechaAlta();
	}

	public Date getFechaFinVig() {
		return this.getFechaBaja();
	}
	
	/**
     * Desactiva la Relacion de Cuenta que tiene a esta como Origen y al parametro como Destino.
     * @param cuentaDestino Cuenta, fechaHasta Date con la fecha de baja.
     * @return CuentaRel 
     */
    public void desactivarCuentaRel(Cuenta cuentaDestino,Date fechaHasta) throws Exception {

    	CuentaRel cuentaRel = (CuentaRel) 
    	PadDAOFactory.getCuentaRelDAO().getByOrigenYDestino(this, cuentaDestino);
    	if(cuentaRel!=null){
    		cuentaRel.setFechaHasta(fechaHasta);
    		updateCuentaRel(cuentaRel);    		
    	}

    }

    /** 
     * Recupera una lista de todas las exenciones En tramites a la fecha actual
     * La exenciones en tramites son las que estan en estado CREADA
     * @return List<CueExe>
     */
	public List<CueExe> getListCueExeEnTramite() {
    	if (listCueExeEnTramiteCache == null){
    		listCueExeEnTramiteCache = ExeDAOFactory.getCueExeDAO().getListEnTramiteByCuenta(this);
    	}
    	return listCueExeEnTramiteCache;
	}

    /** 
     * Recupera una lista de todas las exenciones vigentes para esta cuenta a la fecha actual.
     * 
     * @return List<CueExe>
     */
    public List<CueExe> getListCueExeVigente() throws Exception {
    	return this.getListCueExeVigente(new Date());
    }
    
    /**
     * Recupera una lista de todas las exenciones vigentes para esta cuenta en la fecha dada
     * @param  fecha
     * @return List<CueExe>
     * @throws Exception
     */
    public List<CueExe> getListCueExeVigente(Date fecha) throws Exception {
    	if (listCueExeVigentesCache == null){
    		listCueExeVigentesCache = ExeDAOFactory.getCueExeDAO().getListVigenteByCuenta(this);
    	}
    	List<CueExe> listCueExeFiltroFecha = new ArrayList<CueExe>();
    	for (CueExe cueExe : this.listCueExeVigentesCache) {
			// Verificamos si es Vigente contra la fechaEmision de la deuda
			if (DateUtil.dateCompare(cueExe.getFechaDesde(), fecha) <= 0 
				&& (cueExe.getFechaHasta() == null || DateUtil.dateCompare(cueExe.getFechaHasta(), fecha) > 0)) {
				listCueExeFiltroFecha.add(cueExe);
			}
		}
    	return listCueExeFiltroFecha;
    }

    
    /**
     * Obtiene las exenciones que pueda tener la cuenta y que No Actualizen Deuda.
     * 
     * @return
     * @throws Exception
     */
    public List<CueExe> getListCueExeNoActDeuda() throws Exception {
    	
    	if (listCueExeNoActDeudaCache == null){
    		listCueExeNoActDeudaCache = ExeDAOFactory.getCueExeDAO().getListNoActDeudaByCuenta(this);
    	}
    	
    	return listCueExeNoActDeudaCache;
    }
    
    
    /**
     * Revisa si esta exenta de actualizar deuda.
     * 
     * Si la fecha de vencimiento llega nula, solo son tenidas la exenciones vigentes a la fecha de analisis 
     * 
     * 
     * @param fecha
     * @return Boolean
     * @throws Exception
     */
    public Boolean exentaActualizacion(Date fechaAnalisis, Date fechaVencimiento)throws Exception{
    	
    	// , boolean exencionesAlVencimiento
    	
    	// Si son tenidas en cuenta las exenciones a la fecha de vencimiento
    	if (fechaVencimiento != null){
	    	// obtener todas las exenciones que posea la cuenta y que no actualizen deuda
	    	/*
	    		Por cada una preguntamos
	    			Si esta vigente a la fecha de hoy, devolver true
	    			Si esta vigente a la fecha de vencimiento recibida, devolver true
	    			sino false
	    	*/	
	    	
	    	List<CueExe> listExencionesNoActDeuda = getListCueExeNoActDeuda();
	    	
	    	for (CueExe cueExe : listExencionesNoActDeuda){
	    		
	    		// Si es vigente a la fecha de hoy, retornamos true sin tener en cuenta la fecha de vencimiento de la deuda,
	    		// porque no actualiza deuda. 
	    		// fedel: 2009-may-08, bug: 687, para que este exenta solo importa si la la cuexe estaba vigente al
	    		//        vencimiento. Por eso comnetamos las dos lineas de abajo.
	    		//if (DateUtil.isDateInRange(fechaAnalisis, cueExe.getFechaDesde(), cueExe.getFechaHasta())){
	    		//	return true;
	    		//}
	    		
	    		// Si fue vigente a la fecha de Vencimiento de la deuda.
	    		// devolvemos true porque estamos dentro del caso "exencionesAlVencimiento=true"
	    		if (DateUtil.isDateInRange(fechaVencimiento, cueExe.getFechaDesde(), cueExe.getFechaHasta())){
	    			return true;
	    		}
	    		
	    	}
	    	return false;
    	
    	} else {
		//No son tenidas las exenciones a la fecha de vencimiento
    		
    		List<CueExe> listExencionesVigentes = getListCueExeVigente(fechaAnalisis);
	    	
	    	for (CueExe cueExe : listExencionesVigentes){
	    		Exencion exencion = cueExe.getExencion();
	    		if (exencion.getActualizaDeuda().equals(SiNo.NO.getId())){
	    			return true;
	    		}
	    	}
	    	
	    	return false;
    		
    	}
    }
    
    
    /** Actualiza el domicilio de envio 
     *  para esta cuenta
     * 
     */
    public CamDomWeb updateDomicilioEnvio (CamDomWeb camDomWeb) throws Exception {
    	String desDomEnv = null;
    	//seteo la cuenta y el domicilio de envio
		camDomWeb.setCuenta(this);
		camDomWeb.setDomVie(this.getDomicilioEnvio());
		
		Integer nroTramite = VariosWebFacade.getInstance().createOperacionAuditable(camDomWeb);		
		camDomWeb.setNroTramite(nroTramite);

		// creo el historica de cambio de domicilio
		PadDAOFactory.getCamDomWebDAO().update(camDomWeb);

		desDomEnv = camDomWeb.getDomNue().getViewDomicilio(); //descripcion con nombre largo
		// ver bug 665 : piden de usar nombre corto para boletas de reparto.
		// ahora tratamos de determinar el nombre corto y reeplazamos el nombre largo 
		// por el corto
		String nomAbrev = null;
		String nomLargo = null;
		try {
			Long codCalle = camDomWeb.getDomNue().getCalle().getCodCalle();
			if (codCalle != null) {
				nomAbrev = UbicacionFacade.getInstance().getNombreAbrevCalle(codCalle);
				nomLargo =  camDomWeb.getDomNue().getCalle().getNombreCalle();
				camDomWeb.getDomNue().getCalle().setNombreCalle(nomAbrev);
				desDomEnv = camDomWeb.getDomNue().getViewDomicilio();
				camDomWeb.getDomNue().getCalle().setNombreCalle(nomLargo);
			}
		} catch (Exception e) {
			log.warn("Fallo determinacion nombre corto de calle");
		}

    	// seteo el nuevo domicilio a esta cuenta, y la descripcion con el 
    	// nombre corto en desDomEnv. luego restauro el nombre largo por cualquier cosa.
    	this.setDomicilioEnvio(camDomWeb.getDomNue());
    	this.setDesDomEnv(desDomEnv);
    	this.updateCatDomEnvio();
    	PadDAOFactory.getCuentaDAO().update(this);
    	if (getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_TGI)) {
    		updateBroche(camDomWeb);
    	}
    	return camDomWeb;
    }

	private void updateBroche(CamDomWeb camDomWeb) throws Exception {
		// seteo el broche a esta cuenta
		Zona zona = null;
		String domicilioEnvioObjImp = ""; 	//Domicilio de envio original del objimp. 
											//Al momento de crear la parcela en catastro
											//domicilio finca y domicilio envio son iguales
											//La diferencia es que dom finca esta en formato para los humanos
											// y dom envio esta encodeado segun los tipos de SIAT

		boolean isRosario = camDomWeb.getDomNue().getLocalidad().isRosario();
		/*
		 * - Si es rosario, 
		 *     - verifica si tiene objeto imponible, para obtener la zona
		 *     - si no pudo determinar zona, envia solicitud asignar broche no se pudo determinar zona
		 *     - si el nuevo domicilio es exactamente igual al domicilio envio informado en objimp, quita broche
		 *     - sino con la zona y el domicilio trata de obtener el broche del repartidor correspondiente
		 *     - si tiene repartidor, asigna el broche de repartidor fuera de zona a la cuenta
		 *     - si no envia solicitud de asignar broche con datos nuevo cambio domiclio
		 * - Si no es rosario,
		 *     - Envia solicitud asignar broche con datos dom fuera de rosario
		 *   
		 * - Graba la operacion auditable para varios_web
		 */
		
		if (isRosario) { 
			// bandera que determina si la catastral del domicilio de envio asociado a la cuenta coincide
			// con la catastral de la ubicación de la parcela (atributo valorizado del obj imp) 
			boolean isCatEquals = false;  
			
			if (this.getObjImp() != null && this.getObjImp().getTipObjImp().isParcela()) {
				String catastral = getObjImp().getClaveFuncional();
				String idseccion = StringUtil.substringHasta(catastral,'/');
				Seccion seccion = Seccion.getById(new Long(idseccion));
				zona = seccion.getZona();
				domicilioEnvioObjImp = objImp.getDefinitionValue().getTipObjImpAtrDefinitionByCodigo("DomicilioEnvio").getValorString();    		

				// Obtengo la Catastral del domicilio de envio asociada a la cuenta
				String catDomEnv = getCatDomEnv();
				// Obtengo la catastral de la ubicación de la parcela (99/999/999)
				String catObjImp = getObjImp().getClaveFuncional().substring(0,10);
				// Determino si las catastrales coinciden
				isCatEquals = catObjImp.equals(catDomEnv);
			}

			// comparamos los string de domicilio envio original del objeto imponible 
			// contra el de que cargaron recien. Si son iguales, quitamos el broche
			// sino hacemos el analisis para saber a que repartidor asignar en caso
			// que sea posible.
			if (domicilioEnvioObjImp.equals(camDomWeb.getDomNue().toStringAtr()) || isCatEquals) {
				this.quitarBroche(new Date());
			} else {
				if (zona != null) {
					// obtengo el broche que corresponda
					Broche broche = PadDistribucionManager.getInstance().obtenerRepartidorFueraDeZona
					(camDomWeb.getDomNue(), zona);

					// si se encuentra el broche se asigna a la cuenta
					if (broche != null) {
						this.asignarBroche(broche, new Date(), null);
					} else { // si no encunentra broche crea una solicitud
						String descripcion = "Nro cuenta: " +  this.getNumeroCuenta() + ", Zona parcela: " + zona.getDescripcion() + 
							", Nuevo domicilio: " + camDomWeb.getDomNue().getViewDomicilio(); 

						CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_ASIGNACION_BROCHE_CUENTA, 
								"No se encontro broche para asignar a la cuenta", descripcion, this);
					}
				} else {
					String descripcion = "Nro cuenta: " +  this.getNumeroCuenta() + ", Nuevo domicilio: " + camDomWeb.getDomNue().getViewDomicilio(); 
					CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_ASIGNACION_BROCHE_CUENTA, 
							"Cambio Domicilio, No se pudo determinar Zona de domicilio", descripcion, this);    				
				}
			}
		} else { 
			// Si no es Rosario se le asigna el broche 900
			this.setBroche(Broche.getById(Broche.ID_BROCHE_FUERA_DE_ROSARIO));
		}
	}

    
    /**
     * - Obtiene un ContribuyenteDefinition con los atributos valirizados correspondientes
     * 	 a la union de los atributos de los contribuyentes(titulares) de la cuenta.
     * 
     * - Teniendo en cuenta el peso del valor de los atributos, "Si" pesa mas que "No".
     * 
     */
    public ContribuyenteDefinition getUnionConAtrVal(boolean formatValues4View) throws Exception {

    	// Recupero la definicion de los atributos del contribuyente para la web
    	// con sus valores por defecto.
    	ContribuyenteDefinition contrDef4Web = Contribuyente.getDefinitionForWeb();
    	
    	
    	// Si no estan cargados los titulares, los cargamos
    	if (this.getListIdsTitulares() == null || this.getListIdsTitulares().length == 0){
    		this.getListTitularesCuentaLight(new Date());
    	}
    	
    	// Si la cuenta no posee titulares activos vigentes, retornamos nulo
    	if (this.getListIdsTitulares() == null || this.getListIdsTitulares().length == 0)
    		return null;
    	
    	List<ConAtrVal> listConAtrVal = ConAtrVal.getListByIdsContribuyentesYIdsConAtr(this.getListIdsTitulares(), contrDef4Web.getListIds());
    	  	
    	for (ConAtrVal conAtrVal:listConAtrVal){
    		
    		// - Si la definicion no esta valorizada, la valorizo.
			// - Si lo esta, ver si el valor tiene mas peso que el que tiene el contribuyente.
			// - Si la del contribuyente tiene mas peso, actualizar la definicion que se devuelve. 
    		ConAtrDefinition cad = contrDef4Web.getConAtrDefinitionById(conAtrVal.getIdConAtr());
    		
			String codAtributo = cad.getConAtr().getAtributo().getCodAtributo();
			String valor = conAtrVal.getValor();
			
			// Si es vacio (por no tener valor por defecto)
			if (StringUtil.isNullOrEmpty(contrDef4Web.getDefConAtrByCodigo(codAtributo).getValorString())){
				contrDef4Web.getDefConAtrByCodigo(codAtributo).setValor(valor);
				
			} else {
				// Solo el valor Si puede cambiar el valor de la definicion a devolver
				if (!StringUtil.isNullOrEmpty(valor) &&
						valor.endsWith("1")){
					contrDef4Web.getDefConAtrByCodigo(codAtributo).setValor(valor);    					
				}    				
			}
    	}
    	
    	if (formatValues4View) {
	    	// Recorro la union de atributos resultante y cambio los valores seteados por en correspondiente para la vista.
	    	for(ConAtrDefinition cad: contrDef4Web.getListConAtrDefinition()) {
	    		String codAtributo = cad.getConAtr().getAtributo().getCodAtributo();
	    		
	    		if(cad.getPoseeDominio())
	    			contrDef4Web.getDefConAtrByCodigo(codAtributo).setValor(cad.getValorFromDominioView() );
					//liqAtrValorVO.setValue(caf.getValorFromDominioView());
				else				
					contrDef4Web.getDefConAtrByCodigo(codAtributo).setValor(cad.getValorView());
					//liqAtrValorVO.setValue(caf.getValorView());
	    	}
    	}
    	
    	return contrDef4Web;
    }
    
    
    
    /**
     * - Obtiene un ContribuyenteDefinition con los atributos valirizados correspondientes
     * 	 a la union de los atributos de los contribuyentes(titulares) de la cuenta.
     * 
     * - Toma todos los atributos, aun los marcado como visibles=No
     * 
     */
    public ContribuyenteDefinition getUnionConAtrValFull(boolean formatValues4View) throws Exception {

    	// Recupero la definicion de los atributos del contribuyente 
    	// con sus valores por defecto.
    	ContribuyenteDefinition contrDefFull = Contribuyente.getDefinitionForManual();
    	
    	
    	// Si no estan cargados los titulares, los cargamos
    	if (this.getListIdsTitulares() == null || this.getListIdsTitulares().length == 0){
    		this.getListTitularesCuentaLight(new Date());
    	}
    	
    	// Si la cuenta no posee titulares activos vigentes, retornamos nulo
    	if (this.getListIdsTitulares() == null || this.getListIdsTitulares().length == 0)
    		return null;
    	
    	List<ConAtrVal> listConAtrVal = ConAtrVal.getListByIdsContribuyentesYIdsConAtr(this.getListIdsTitulares(), contrDefFull.getListIds());
    	  	
    	for (ConAtrVal conAtrVal:listConAtrVal){
    		
    		// - Si la definicion no esta valorizada, la valorizo.
			// - Si lo esta, ver si el valor tiene mas peso que el que tiene el contribuyente.
			// - Si la del contribuyente tiene mas peso, actualizar la definicion que se devuelve. 
    		ConAtrDefinition cad = contrDefFull.getConAtrDefinitionById(conAtrVal.getIdConAtr());
    		
			String codAtributo = cad.getConAtr().getAtributo().getCodAtributo();
			String valor = conAtrVal.getValor();
			
			// Si es vacio (por no tener valor por defecto)
			if (StringUtil.isNullOrEmpty(contrDefFull.getDefConAtrByCodigo(codAtributo).getValorString())){
				contrDefFull.getDefConAtrByCodigo(codAtributo).setValor(valor);
				
			} else {
				// Solo el valor Si puede cambiar el valor de la definicion a devolver
				if (!StringUtil.isNullOrEmpty(valor) &&
						valor.endsWith("1")){
					contrDefFull.getDefConAtrByCodigo(codAtributo).setValor(valor);    					
				}    				
			}
    	}
    	
    	if (formatValues4View) {
	    	// Recorro la union de atributos resultante y cambio los valores seteados por en correspondiente para la vista.
	    	for(ConAtrDefinition cad: contrDefFull.getListConAtrDefinition()) {
	    		String codAtributo = cad.getConAtr().getAtributo().getCodAtributo();
	    		
	    		if(cad.getPoseeDominio())
	    			contrDefFull.getDefConAtrByCodigo(codAtributo).setValor(cad.getValorFromDominioView() );
					//liqAtrValorVO.setValue(caf.getValorFromDominioView());
				else				
					contrDefFull.getDefConAtrByCodigo(codAtributo).setValor(cad.getValorView());
					//liqAtrValorVO.setValue(caf.getValorView());
	    	}
    	}
    	
    	return contrDefFull;
    }
    
    /**
     *  Obtiene las personas correspondientes a titulares(Contribuyentes) de la cuenta vigentes a la fecha parametro.
     *  Este metodo se usa para la liquidacion de la deuda. 
     * 
     * 
     * @author Cristian
     * @param fecha
     * @return List<Persona>
     */
    public List<Persona> getListTitularesCuentaLight(Date fecha) {
    	
    	//Por cada persona recuperada, 
    	// crear un Contribuyente
    	// setearle la persona
    	// setearle el id de la persona como id contribuyente
    	// cargamos la lista de ids de titulares
    	// Para ser utilizado por la funcion: getUnionConAtrVal();
    	
    	List<Persona> listTitulares = new ArrayList<Persona>();
    	List<CuentaTitular> listCuentaTitular = PadDAOFactory.getCuentaTitularDAO().getByCuentaYFechaVigencia(this, fecha);
    	
    	for(CuentaTitular ct:listCuentaTitular) {
    		Persona titular = new Persona();
    		titular.setId(ct.getIdContribuyente());
    		listTitulares.add(titular);
    	}
    	
    	// Cargamos la lista de ids de titulares
    	if (listTitulares != null && listTitulares.size() > 0 ){
	    	this.listIdsTitulares = new Long[listTitulares.size()];
	    	int i=0;	
			for(Persona persona:listTitulares){
				this.listIdsTitulares[i] = persona.getId();
				i++;
			}    	
    	}
    	
    	return listTitulares;
    }
    
    /**
     * Obtiene la deuda administrativa de esta instancia de cuenta.
     * 
     * @author Cristian
     * @return
     */
    public List<DeudaAdmin> getListDeudaAdmin(){
    	
    	DemodaTimer dt = new DemodaTimer();
    	
    	List<DeudaAdmin> listDeudaAdmin = new ArrayList<DeudaAdmin>();
    	
    	if (getListDeudaAdminRaw() == null){
    		setListDeudaAdminRaw(GdeDAOFactory.getDeudaAdminDAO().getListDeudaAdmin(this));
    	} 
    	
    	// Retornamos deuda en via administrativa o
    	// en via CyQ con idProcedimiento negativo
		for (DeudaAdmin deudaAdmin:getListDeudaAdminRaw()){
			if (deudaAdmin.getViaDeuda().getId().equals(ViaDeuda.ID_VIA_ADMIN) || 
					(deudaAdmin.getViaDeuda().getId().equals(ViaDeuda.ID_VIA_CYQ) && deudaAdmin.getIdProcedimientoCyQ() != null && deudaAdmin.getIdProcedimientoCyQ().longValue() < 0 ))
				listDeudaAdmin.add(deudaAdmin);
		}
    	
		log.info(dt.stop("		Cuenta :: getListDeudaAdmin"));
		
    	return listDeudaAdmin;
	}
   
    /**
     * Obtiene la deuda en via Judicial.
     * 
     * @author Cristian
     * @return
     */
    public List<DeudaJudicial> getListDeudaJudicial(){
    	/*(via judicial)
    	 -- si listDeudaJudicialRaw es null, carga la lista desde la tabla deudaJudicial y idProcurador
    	 -- retList.add(cada deuda admin raw en via judicial)*/
    	
    	DemodaTimer dt = new DemodaTimer();
    	
    	List<DeudaJudicial> listDeudaJudicial = new ArrayList<DeudaJudicial>();
    	
    	if (getListDeudaJudicialRaw() == null){
    		setListDeudaJudicialRaw(GdeDAOFactory.getDeudaJudicialDAO().getListDeudaJudicial(this));
    	} 
    		
		for (DeudaJudicial deudaJudicial:getListDeudaJudicialRaw()){
			if (deudaJudicial.getViaDeuda().getId().equals(ViaDeuda.ID_VIA_JUDICIAL) ||
					(deudaJudicial.getViaDeuda().getId().equals(ViaDeuda.ID_VIA_CYQ) && deudaJudicial.getIdProcedimientoCyQ() != null && deudaJudicial.getIdProcedimientoCyQ().longValue() < 0 ))
				listDeudaJudicial.add(deudaJudicial);
		}
    	
		log.info(dt.stop("		Cuenta :: getListDeudaJudicial"));
		
    	return listDeudaJudicial;
    }
    
    /**
     * Obtiene la deuda impaga (administrativa o judicial) de esta instancia de cuenta excluyendo la deuda pasada como parametro.
     * 
     * @author Tecso
     * @return
     */
    public List<Deuda> getListDeudaImpaga(Long idDeudaAExcluir){
    	
    	List<Deuda> listDeuda = new ArrayList<Deuda>();
    	
    	if (getListDeudaAdminRaw() == null){
    		this.setListDeudaAdminRaw(GdeDAOFactory.getDeudaAdminDAO().getListDeudaAdmin(this));
    	} 
    		
		for (DeudaAdmin deudaAdmin:getListDeudaAdminRaw()){
			if (deudaAdmin.getId().longValue() != idDeudaAExcluir.longValue() && 
					deudaAdmin.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_ADMIN && 
					deudaAdmin.getEstadoDeuda().getId().longValue() != EstadoDeuda.ID_CANCELADA &&
					deudaAdmin.getEstadoDeuda().getId().longValue() != EstadoDeuda.ID_ANULADA) 
				listDeuda.add(deudaAdmin);
		}

    	if (getListDeudaJudicialRaw() == null){
    		this.setListDeudaJudicialRaw(GdeDAOFactory.getDeudaJudicialDAO().getListDeudaJudicial(this));
    	} 
    		
		for (DeudaJudicial deudaJudicial:getListDeudaJudicialRaw()){
			if (deudaJudicial.getId().longValue() != idDeudaAExcluir.longValue() && 
					deudaJudicial.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL && 
					deudaJudicial.getEstadoDeuda().getId().longValue() != EstadoDeuda.ID_CANCELADA &&
					deudaJudicial.getEstadoDeuda().getId().longValue() != EstadoDeuda.ID_ANULADA) 
				listDeuda.add(deudaJudicial);
		}
	
    	return listDeuda;
	}
    
    /**
     * Obtiene la deuda En Concurso y Quiebra que puede existir en Deuda Administrativa o Judicial.
     * 
     * @author Cristian
     * @return
     */
    public List<Deuda> getListDeudaCyQ(){ 
    	/* (via cyq)
	    -- llama a cada getListDeuda*()
	    -- retList.add(cada deuda de cada lista en via CYQ) 
	    -- ordenada retList por java por id de procedimiento*/
    	
    	DemodaTimer dt = new DemodaTimer();
    	
    	List<Deuda> listDeudaCyQ = new ArrayList<Deuda>();
    	List<Deuda> listDeudaCyQRetrn = new ArrayList<Deuda>();
    	List<Long> listIdsProcedimiento = new ArrayList<Long>();
    	
    	if (getListDeudaAdminRaw() == null){
    		setListDeudaAdminRaw(GdeDAOFactory.getDeudaAdminDAO().getListDeudaAdmin(this));
    	} 
    	 
    	if (getListDeudaJudicialRaw() == null){
    		setListDeudaJudicialRaw(GdeDAOFactory.getDeudaJudicialDAO().getListDeudaJudicial(this));
    	}
    	
		for (Deuda deuda:getListDeudaAdminRaw()){
			if (deuda.getViaDeuda().getId().equals(ViaDeuda.ID_VIA_CYQ)){
				listDeudaCyQ.add(deuda);
			}
		}
		
		for (Deuda deuda:getListDeudaJudicialRaw()){
			if (deuda.getViaDeuda().getId().equals(ViaDeuda.ID_VIA_CYQ)){
				listDeudaCyQ.add(deuda);
			}
		}
		
		// Obtengo un distinct de procedimientos
		for (Deuda deuda:listDeudaCyQ){
			Long idProcedimiento = deuda.getIdProcedimientoCyQ(); 
			
			if (!listIdsProcedimiento.contains(idProcedimiento)){
				log.debug(" getListDeudaCyQ-> obteniendo: idProcedimiento " + idProcedimiento );
				listIdsProcedimiento.add(idProcedimiento);
			}	
		}
		
		// Genero una lista de deuda ordenada por procedimiento
		for(Long idProcedimiento:listIdsProcedimiento){
			log.debug(" getListDeudaCyQ-> #########################");
			log.debug(" getListDeudaCyQ-> generando: idProcedimiento " + idProcedimiento );
			for (Deuda deuda:listDeudaCyQ){
				log.debug(" getListDeudaCyQ->deuda.IdProcedimientoCyQ " + deuda.getIdProcedimientoCyQ());
				if (idProcedimiento.equals(deuda.getIdProcedimientoCyQ())){
					log.debug(" getListDeudaCyQ-> Agrego deuda.id ="  + deuda.getId()); 
					listDeudaCyQRetrn.add(deuda);
				}
			}	
		}
		
		log.info(dt.stop("		Cuenta :: getListDeudaCyQ"));
		
    	return listDeudaCyQRetrn;
    }
    
    /**
     * Devuelve la deuda anulada correspondiente a la cuenta y ordenada por via, anio y periodo.
     * 
     * @author Cristian
     * @return
     */
    public List<DeudaAnulada> getListDeudaAnulada(){
    	
    	return GdeDAOFactory.getDeudaAnuladaDAO().getListDeudaAnulada(this);
    	
    }
    
    /** Obtiene toda la deuda de la cuenta filtrada por un
     *  rango de fechas. Si la fecha hasta en nula, filtra
     *  a pertir de la desde, si ambas fechas son nulas, 
     *  trae toda la deuda.
     * 
     * @param fechaVtoDesde
     * @param fechaVtoHasta
     * @return
     */
    public List<Deuda> getListDeudaVto(Date fechaVtoDesde, Date fechaVtoHasta) {

    	List<Deuda> listDeuda = new ArrayList<Deuda>();
    	List<Deuda> listDeudaAdmin = GdeDAOFactory.getDeudaAdminDAO().getListByCuentayVto
    		(this, fechaVtoDesde, fechaVtoHasta);
    	List<Deuda> listDeudaAnulada = GdeDAOFactory.getDeudaAnuladaDAO().getListByCuentayVto
			(this, fechaVtoDesde, fechaVtoHasta);
    	List<Deuda> listDeudaCancelada = GdeDAOFactory.getDeudaCanceladaDAO().getListByCuentayVto
			(this, fechaVtoDesde, fechaVtoHasta);
    	List<Deuda> listDeudaJudicial = GdeDAOFactory.getDeudaJudicialDAO().getListByCuentayVto
			(this, fechaVtoDesde, fechaVtoHasta);

    	listDeuda.addAll(listDeudaAdmin);
    	listDeuda.addAll(listDeudaAnulada);
    	listDeuda.addAll(listDeudaCancelada);
    	listDeuda.addAll(listDeudaJudicial);
    	
    	Collections.sort(listDeuda, new CompDeudaByFechaVtoDesc()) ;
    	
    	return listDeuda;
    }
    
    
    /**
	 * Obtiene el titular principal (CuentaTitular Bean) para la cuenta dada.
	 * Si la cuenta no tiene tiene titulares principales vigentes, 
	 * retorna la cuentatitular con mayor id
	 * Si la cuenta no tiene titulares vigentes retorna una excepcion
	 * @param cuenta
     * @return CuentaTitular
     */
    public CuentaTitular obtenerCuentaTitularPrincipal() throws Exception {
    	CuentaTitular cuentaTitular = PadDAOFactory.getCuentaTitularDAO().getPrincipalByCuenta(this);
    	if(cuentaTitular == null && !ListUtil.isNullOrEmpty(this.getListCuentaTitular())){
    		cuentaTitular = this.getListCuentaTitular().get(0);
    	}
    	return cuentaTitular;
    }
    
    /**
     *  Asigna el Broche a la Cuenta. Crea un registro historico en BroCue, y da de baja el registro vigente (si existe)
     * 
     * @param broche
     * @param fechaAlta
     * @param idCaso
     * @return broCue
     * @throws Exception
     */
    public BroCue asignarBroche(Broche broche, Date fechaAlta, String idCaso) throws Exception{
    	
    	BroCue broCue = new BroCue();
        
		broCue.setBroche(broche);
		
		broCue.setCuenta(this);
		broCue.setFechaAlta(fechaAlta);
		broCue.setIdCaso(idCaso);
		
        broCue.setEstado(Estado.ACTIVO.getId());
  
       	// Buscamos algun registro en BroCue de asignacion de algun broche a esta cuenta que este vigente. 
       	BroCue broCueVigente = BroCue.getVigenteByCuenta(this);
       	// Si existe tal registro, seteamos su fechaBaja igual a la fechaAlta de la nueva asignacion.
        if(broCueVigente != null){
        	broCueVigente.setFechaBaja(broCue.getFechaAlta());
        	broCueVigente.getBroche().updateBroCue(broCueVigente);
        }
        // Luego actualizamos la cuenta seteando el nuevo broche asignado.
        this.setBroche(broCue.getBroche());
        PadCuentaManager.getInstance().updateCuenta(this);
        
        // Por ultimo guardamos un nuevo registro de asignacion de broche a cuenta vigente (fechaBaja=null)
        broCue = broche.createBroCue(broCue); 
    	return  broCue;
    }

    public BroCue quitarBroche(Date fechaBaja) throws Exception{
    	
    	BroCue broCue = BroCue.getVigenteByCuenta(this);
    	if (broCue == null) {
    		return null;
    	}
    	
    	broCue.setFechaBaja(fechaBaja);
        broCue.getBroche().updateBroCue(broCue);
        
        // Luego de modificar el registro. Si la fecha de baja es distinta de null. Nulleo la referencia a Broche en la Cuenta.
        if(!broCue.hasError()){
        	this.setBroche(null);
        	PadCuentaManager.getInstance().updateCuenta(this);
        }
		return broCue;
    }
    
    /**
     * Retorna el apellido y nombre del titular principal de la cuenta. 
     * En caso que corresponda agrega "y Otros".<br/>
	 * NOTA 1: Este metodo extrae el caracter "," si lo contiene.<br/>
	 * NOTA 2: Este metodo retorna lo que esta almacenado en el campo desTitPrincipal de la tabla cuenta, 
	 * si este campo es null, trata de obtener la descripcio llendo a genIngreso.
	 * si no se pudo encontrar retorna ""
     * @return String descripcion de titularidad de cuenta con formato: '[C|R|F] CUIT NombreTitular [y Otros]
     * @throws Exception
     */
    public String getDesTitularPrincipal() throws Exception {
		String des = "";

		des = this.getCuitTitPri() != null ? this.getCuitTitPri() : "";
		des = des + " " + (this.getNomTitPri() != null ? this.getNomTitPri() : "");
		des = des.trim().replace(","," ");
		return des;
    }

	/**
	 * Retorna la descripcion del titular principal:
	 * Muestra el dato para un contribuyente
     *       Si cuit empieza con R o C:
     *           retorna getDesTitularPrincipal() sacandole los dos
     *           primeros caracteres
     *       Si cuit empieza con F:
     *           saca toda la info de CUIT, solo queda NombreTitular [y Otro]
     *           
     *  Si existe LiqCuentaFilter, los datos se obtienen para el contribuyente que corresponda.         
     *           
	 */
    public String getDesTitularPrincipalContr() throws Exception {
		
    	String cuit, nom, des="";
    	CuentaTitular ct = null;
    	
    	if (liqCuentaFilter != null && liqCuentaFilter.getDeudaSigueTitular() && liqCuentaFilter.getIdCuentaTitular() != null){
    		ct = CuentaTitular.getByIdNull(liqCuentaFilter.getIdCuentaTitular()); 
    		Contribuyente cont = ct.getContribuyente();
    		cont.loadPersonaFromMCR();
    		Persona persona = cont.getPersona();
    		if (persona != null){
    			cuit = persona.getCuitContr();
    			nom = persona.getRepresent();
    			
    		} else {
    			cuit = getCuitTitularPrincipalContr();
    			nom = this.getNomTitPri() != null ? this.getNomTitPri() : "";
    		}
    	} else {
    		cuit = getCuitTitularPrincipalContr();
    		nom = this.getNomTitPri() != null ? this.getNomTitPri() : "";
    	} 
		
		des = cuit + " " + nom;
		return des.trim();
	}
    
	/**
	 * Retorna el nombre y apellido o razon social del titular principal
	 * Este metodo no retorna informacion sobre Cuit
	 */
	public String getNombreTitularPrincipal() throws Exception {
		String nom = ""; 
		CuentaTitular ct = null;
		
		if (liqCuentaFilter != null && liqCuentaFilter.getDeudaSigueTitular() && liqCuentaFilter.getIdCuentaTitular() != null){
    		ct = CuentaTitular.getByIdNull(liqCuentaFilter.getIdCuentaTitular()); 
    		Contribuyente cont = ct.getContribuyente();
    		cont.loadPersonaFromMCR();
    		Persona persona = cont.getPersona();
    		if (persona != null){
    			nom = persona.getRepresent();
    			
    		} else {
    			nom = this.getNomTitPri() != null ? this.getNomTitPri() : "";
    		}
    	} else {
    		nom = this.getNomTitPri() != null ? this.getNomTitPri() : "";
    	}
		
		return nom.trim();
	}
    
	/**
	 *   Obtiene el cuit del titular principal con el format [C|R|F] CUIT
	 */
	public String getCuitTitularPrincipal() throws Exception {
		String cuit = "";
		CuentaTitular ct = null;
		
		if (liqCuentaFilter != null && liqCuentaFilter.getDeudaSigueTitular() && liqCuentaFilter.getIdCuentaTitular() != null){
    		ct = CuentaTitular.getByIdNull(liqCuentaFilter.getIdCuentaTitular()); 
    		Contribuyente cont = ct.getContribuyente();
    		cont.loadPersonaFromMCR();
    		Persona persona = cont.getPersona();
    		if (persona != null){
    			cuit = persona.getCuitFull();
    			
    		} else {
    			cuit = this.getCuitTitPri() != null ? this.getCuitTitPri() : "";
    		}
    	} else {
    		cuit = this.getCuitTitPri() != null ? this.getCuitTitPri() : "";
    	}
		
		return cuit.trim();
	}

	/**
	 * Obtiene el cuit del titular principal con el format CUIT si es C o R
	 * o "" si es F  
	 */
    public String getCuitTitularPrincipalContr() throws Exception {
		String cuit = getCuitTitularPrincipal();
		log.debug("CUIT del titular ppal"+cuit);
		if (cuit.startsWith("R") || cuit.startsWith("C")) {
			try { 
				cuit = cuit.substring(2).trim();  //cuit sin la R o C
			} catch (Exception e) { 
				cuit =""; 
				log.error("getCuitTitPrincipalContr(): CUIT con formato incorrecto cuenta:" + this.getNumeroCuenta() + " cuit:'" + cuit + "'"); 
			}
		} else {
			cuit = "";
		}
		return cuit.trim();
	}
    
    /**
     * Obtiene las cuentas titular vigentes de la cuenta en una fecha
     * @param  fecha
     * @return List<CuentaTitular>
     */
    public List<CuentaTitular> getListCuentaTitularVigentes(Date fecha){
    	return PadDAOFactory.getCuentaTitularDAO().getListCuentaTitularVigentes(this, fecha);
    }

    /**
     * Obtiene las cuentas titular vigentes de la cuenta para una fecha
     * considerando un intervalo de tiempo cerrado.
     * 
     * @param  fecha
     * @return List<CuentaTitular>
     */
    public List<CuentaTitular> getListCuentaTitularVigentesCerrado(Date fecha){
    	return PadDAOFactory.getCuentaTitularDAO().getListCuentaTitularVigentesCerrado(this, fecha);
    }
    
    /**
     * Devuelve los RecAtrCueV vigentes a la fecha pasada como parametro
     * @param fecha
     * @return null si no encuentra nada
     */
    public List<RecAtrCueV> getListRecAtrCueV(Date fecha){
    	return PadDAOFactory.getRecAtrCueVDAO().getVigentes(getId(), fecha);
    }
    
    // Reconfeccionar Deuda
    
    /** 
     * realiza la reconfeccion de la deuda con la lista y fecha que llegan como parametro:
     * - Aplica el mejor descuento posible (entre generales y especiales)
     * - agrupa las deudas de acuerdo al descuento aplicado, por recibo
     * - aplica el sellado a cada recibo generado 
     *
     * se utiliza para la reconfeccion web, para la reconfeccion masiva y para el preenvio judicial masivo
     * 
     * Devuelve una lista de recibos con la maxima cantidad de registros posibles por cada recibo.
     *
     *    
     * @throws Exception 
     */
    public List<Recibo> reconfeccionar(List<Deuda> listDeuda, Date fechaVencimiento, Date fechaActualizacion, Canal canal, Procurador procurador, ProcesoMasivo preProcesoMasivo, Boolean contemplaSellado, Long  idBroche, Integer maxCantDeudaxRecibo, boolean esRectificativa) throws Exception{

    	// instancia objetos pasados como parametros
    	
    	// Validacion de parametros 
		//if(!validarParametrosReconfeccion(listDeuda, fechaVto, idCanal, idProcurador, idReconfeccion))
			//return null;
				
		// inicializa lista de recibos deuda y recibos
		List<ReciboDeuda> listReciboDeuda = new ArrayList<ReciboDeuda>();
    	List<Recibo> listRecibos = new ArrayList<Recibo>();
    	    	    	
    	// si se contempla el calculo de sellado, obtiene del cronograma la fecha tope para determinar si corresponde
    	// el cobro de sellado
    	// ojo, una cosa es que se contemple el sellado y otra cosa es que se cobre. 
    	Date fechaTope = null;
    	boolean cobraSellado = false;
    	if (contemplaSellado) {
    		fechaTope = Calendario.obtenerFechaAnteUltimoPeriodoVencido(this.getRecurso(), null, fechaVencimiento); //Ex fechaVto
    	}
    	
    	Deuda primerDeuda = listDeuda.get(0);
    	boolean esGre = ServicioBanco.COD_OTROS_TRIBUTOS.equals(primerDeuda.getSistema().getServicioBanco().getCodServicioBanco());

		// determina la via deuda tomandola de la primer deuda (Solicitado en Mantis 5043) - (antes en funcion de si recibe como parametro al procurador o no)
		//long idViaDeuda = (procurador!=null && procurador.getId().longValue()>0?ViaDeuda.ID_VIA_JUDICIAL:ViaDeuda.ID_VIA_ADMIN);
    	ViaDeuda viaDeuda = primerDeuda.getViaDeuda();//ViaDeuda.getByIdNull(new Long(idViaDeuda));
		
		// obtiene descuentos generales y especiales aplicables a la cuenta 
    	List<DesGen> listDesGenVigente = DesGen.getVigente(recurso, fechaVencimiento);
    	List<DesEsp> listDesEsp = getListDesEspVigentes(TipoDeuda.ID_DEUDA_PURA, viaDeuda.getId(), fechaVencimiento); //Ex fechaVto    

    	// Llega nula pasa el caso de REIMPRESION, Y es nesesacia no nula para RECONFECCION de Gre 
    	if (fechaActualizacion == null){
    		fechaActualizacion = new Date();
    	}
    	
    	// a continuacion se recorre la lista de deuda
    	// si esta vencida, 
    	//    se busca el mejor descuento que le aplica
    	//    se verifica si la fecha de vencimiento es anterior a la fecha tope del sellado y se setea la bandera de cobro en caso que corresponda
    	// si no esta vencida
    	//    se crea un recibo que no deber'a ser grabado ni impreso
		for (Deuda deuda: listDeuda){
			
			// 	----- RECONFECCION -------
			if (deuda.estaVencida() || esRectificativa || (esGre && deuda.getCodRefPag().longValue() == 0)) {
				
				boolean cobraSelladoPeriodo = false;
				// si hay alguna deuda con fecha de vencmiento menor al tope, habilitar el cobro de sellado
				if (contemplaSellado &&  DateUtil.isDateBefore(deuda.getFechaVencimiento(), fechaTope)) {
					cobraSelladoPeriodo = true;
					log.debug("Corresponde cobrar sellado");
				}
				
				if (deuda.estaVencida() && this.getRecurso().getEsAutoliquidable().intValue()==SiNo.SI.getId().intValue()){
					int cuatrEmision;
					if (deuda.getPeriodo()<5)
						cuatrEmision=1;
					else if (deuda.getPeriodo()<9)
						cuatrEmision=2;
					else 
						cuatrEmision=3;
					
					int cuatrActual;
					int mes = DateUtil.getMes(new Date());
					if(mes<5)
						cuatrActual=1;
					else if (mes < 9)
						cuatrActual=2;
					else
						cuatrActual=3;
					
					Date ultEmision = DateUtil.getDate(DateUtil.getAnio(new Date()), cuatrActual, 1);
					Date emisionPeriodo = DateUtil.getDate(deuda.getAnio().intValue(), cuatrEmision, 1);
					if (DateUtil.isDateBefore(emisionPeriodo, ultEmision))
						cobraSelladoPeriodo =true;
					else
						cobraSelladoPeriodo =false;
					
					// Excepcion de cobro de sellado para Drei/Etur a partir de la fecha de implementacion de Osiris (Mantis 6565)
					Date fechaRestriccion = DateUtil.getFirstDatOfMonth(SiatParam.getInteger(SiatParam.MES_INICIO_RS)+1, SiatParam.getInteger(SiatParam.ANIO_INICIO_RS));
					if(null != fechaRestriccion && DateUtil.isDateAfter(deuda.getFechaVencimiento(), fechaRestriccion)){
						cobraSelladoPeriodo =false;
					}
				}
				
				cobraSellado = (cobraSellado || cobraSelladoPeriodo);
				
				// esta linea obtiene un ReciboDeuda y lo agrega a una lista de recibos deuda
				listReciboDeuda.add(mejorDescuento(deuda, fechaActualizacion, listDesGenVigente, listDesEsp, esRectificativa));
			
			} else {
			//  ----- REIMPRESION -------
				
				// esta es una deuda que no esta vencida
	
				// si es una reconfeccion Web, 
				//    vamos a REIMPRIR (puede ser una reimpresion de deuda migrada o una reimpresion de deuda generada por SIAT)
				// si no es una reconfeccion web
				//    se ignora la deuda no vencida
				
				if (preProcesoMasivo ==null) {
					
					Recibo recibo = new Recibo();
					recibo.setIdBroche(idBroche);
					recibo.setEsReimpresionDeuda(true);
					recibo.setEstaImpreso(SiNo.SI.getBussId());
	
			    	recibo.setCodRefPag(deuda.getCodRefPag());

			    	// TODO: aqui se deberia sacar el servicio banco de la deuda, pero en la migracion viene nula
			    	if (deuda.getSistema().getServicioBanco()==null) {
				    	recibo.setServicioBanco(new ServicioBanco());
			    		
			    	} else {
				    	recibo.setServicioBanco(deuda.getSistema().getServicioBanco() );
			    		
			    	}
			    	recibo.setRecurso(deuda.getRecurso());
			    	recibo.setViaDeuda(deuda.getViaDeuda());
			    	recibo.setCuenta(this);
			    	recibo.setCanal(canal);
			    	recibo.setNroRecibo(0L );
			    	recibo.setAnioRecibo(new Integer(0) );
	
			    	recibo.setFechaVencimiento(deuda.getFechaVencimiento());
			    	
			    	// datos para recibo
			    	recibo.setAnio(deuda.getAnio());
			    	recibo.setPeriodo(deuda.getPeriodo());
			    	recibo.setIdSistema(deuda.getSistema().getId());
					
					ReciboDeuda reciboDeuda = new ReciboDeuda();
					reciboDeuda.setIdDeuda(deuda.getId());
					
					reciboDeuda.setCapitalOriginal(deuda.getSaldo());
					reciboDeuda.setActualizacion(new Double(0));
					reciboDeuda.setPeriodoDeuda(deuda.getStrPeriodo());
					reciboDeuda.setConceptos(deuda.getStrConceptosProp());
					reciboDeuda.setTotCapital(deuda.getSaldo());
					reciboDeuda.setTotActualizacion(new Double(0));
					reciboDeuda.setTotalReciboDeuda(deuda.getSaldo());
					reciboDeuda.setAtrEmisionDefinition(deuda.getAtributosEmisionDefValue());
					reciboDeuda.setUsuarioUltMdf(DemodaUtil.currentUserContext().getUserName());
					if (recibo.getRecurso().equals(Recurso.getDReI())&& reciboDeuda.getCapitalOriginal()==0D && esRectificativa)
						reciboDeuda.setRectificativa(SiNo.SI.getId());
	
					recibo.getListReciboDeuda().add(reciboDeuda);
					
					// agrega el recibo a la lista de recibos
					listRecibos.add(recibo);
				}
			}
    	}
    	
		// aqui tenemos una lista de reciboDeuda y otra de recibo para las reimpresiones
		double totCapitalOriginal = 0D;
		double desCapitalOriginal = 0D;
		double totActualizacion = 0D;
		double desActualizacion = 0D;
		HashMap<Object, Recibo> hashRecibos = new HashMap<Object, Recibo>(); // El hash es a modo de apoyo, lo que vale es la lista de recibos 
    	for(ReciboDeuda reciboDeuda: listReciboDeuda){
    		Object descuentoReciboDeuda = reciboDeuda.getDescuento();
    		Recibo recibo = (Recibo) hashRecibos.get(descuentoReciboDeuda);
    		if(recibo == null){//No existe el recibo en el hash, para ese descuento, se lo crea y se lo agrea a la lista y al hash
    			recibo = crearRecibo(fechaVencimiento, canal, viaDeuda, preProcesoMasivo, procurador, idBroche);
    			recibo.setDescuento(descuentoReciboDeuda);
    			hashRecibos.put(descuentoReciboDeuda, recibo);
    			listRecibos.add(recibo);
    			totCapitalOriginal = 0D;
    			desCapitalOriginal = 0D;
    			totActualizacion = 0D;
    			desActualizacion = 0D;
    		}
    	
			totCapitalOriginal += reciboDeuda.getCapitalOriginal();
			desCapitalOriginal += reciboDeuda.getDesCapitalOriginal();
			totActualizacion   += reciboDeuda.getActualizacion();
			desActualizacion   += reciboDeuda.getDesActualizacion();
    					
			recibo.setTotCapitalOriginal(totCapitalOriginal);
			recibo.setDesCapitalOriginal(desCapitalOriginal);
			recibo.setTotActualizacion(totActualizacion);
			recibo.setDesActualizacion(desActualizacion);
			
			recibo.getListReciboDeuda().add(reciboDeuda);
			reciboDeuda.setRecibo(recibo);
    	}
    	
    	// aqui tenemos la lista de recibos 
    	// obtiene el sellado que corresponda.
    	Sellado s = null;
    	if (cobraSellado) {
    		log.debug("va a buscar el sellado que corresponde");
    		s = BalDefinicionManager.aplicarSellado(recurso.getId(), Accion.ID_ACCION_RECONFECCIONAR_DEUDA, fechaVencimiento, 1, 1);
    	}
    	
    	// recorre los recibos generados y en caso que existan muchas lineas, los corta
    	List<Recibo> listRecibosCortados = new ArrayList<Recibo>();
    	for(Recibo recibo: listRecibos){
     		listRecibosCortados.addAll(recibo.cortar(maxCantDeudaxRecibo));
    	}

    	// en caso que corresponda asocia sellado al primer recibo y luego graba
    	// solo vamos a grabar los recibos que no son reimpresiones
    	for(Recibo recibo: listRecibosCortados){
    		
    		recibo.setRecurso(this.getRecurso());
    		
    		// Si es una cuenta de Contibucion de Mejoras, seteamos sus datos especificos
    		if (this.getRecurso().getCategoria().getId().longValue() == Categoria.ID_CDM){
    			this.cargarDatosReciboCdM(recibo);
    		}   		
    		
    		// Si no es reimpresion
    		if (!recibo.getEsReimpresionDeuda()) {
        		if (s!=null) {
        			recibo.setSellado(s);
        			recibo.recalcularValores();
        			s = null;
        		}
        		        		
        		// Truncamos a dos decimales antes de guardar
        		recibo.setTotCapitalOriginal(NumberUtil.truncate(recibo.getTotCapitalOriginal(), SiatParam.DEC_IMPORTE_DB));
        		recibo.setDesCapitalOriginal(NumberUtil.truncate(recibo.getDesCapitalOriginal(), SiatParam.DEC_IMPORTE_DB));
        		int decimalesActualizacion=SiatParam.DEC_IMPORTE_DB;
        		if(recibo.getTotCapitalOriginal().doubleValue()==0)
        			decimalesActualizacion=SiatParam.DEC_PORCENTAJE_DB;
        		recibo.setTotActualizacion(NumberUtil.truncate(recibo.getTotActualizacion(), decimalesActualizacion));
        		recibo.setDesActualizacion(NumberUtil.truncate(recibo.getDesActualizacion(), decimalesActualizacion));
        		recibo.setImporteSellado(NumberUtil.truncate(recibo.getImporteSellado(), SiatParam.DEC_IMPORTE_DB));
        		recibo.setTotImporteRecibo(NumberUtil.truncate(recibo.getTotImporteRecibo(), SiatParam.DEC_IMPORTE_DB));
        		
        		// Si es Autoliquidable y no esta vencida
        		if(recibo.getRecurso().getEsAutoliquidable().intValue()==SiNo.SI.getId().intValue()&&
        				DateUtil.isDateAfterOrEqual(recibo.getListReciboDeuda().get(0).getDeuda().getFechaVencimiento(), new Date())){
        			
        			recibo.setFechaVencimiento(recibo.getListReciboDeuda().get(0).getDeuda().getFechaVencimiento());

        		// Si es "reimpreion" gravamen especial	
        		} else if (esGre &&
        				DateUtil.isDateAfterOrEqual(recibo.getListReciboDeuda().get(0).getDeuda().getFechaVencimiento(), new Date())) {
        			
        			recibo.setFechaVencimiento(recibo.getListReciboDeuda().get(0).getDeuda().getFechaVencimiento());
        		} else if (esGre){
        			recibo.setFechaVencimiento(fechaVencimiento);
        		}
        		
        		        		
        		GdeDAOFactory.getReciboDAO().update(recibo);
        	} else {
        	// Si es reimpresion	
        		if(recibo.getRecurso().getEsAutoliquidable().intValue()==SiNo.SI.getId().intValue())
        			recibo.setFechaVencimiento(recibo.getListReciboDeuda().get(0).getDeuda().getFechaVencimiento());
        	}
    	}
    	return listRecibosCortados;
    }
     
    /**
     * Aqui se realizan las validaciones de la reconfeccion.
     * 
     * @param listDeuda
     * @param fechaVto
     * @param idCanal
     * @param idProcurador
     * @param idReconfeccion
     * @return
     * @throws Exception
     */
    private Boolean validarParametrosReconfeccion(List<Deuda> listDeuda, Date fechaVto, Long idCanal, Long idProcurador, Long idReconfeccion) throws Exception {
    /*	if(idProcurador!=null){
			//TODO reconfeccionar() - validarParametros(): Obtener procurador para realizar la validacion - arobledo
		}
		
		if(idCanal<=0){
			return null;
		}
		
		if(fechaVto==null || !Feriado.esDiaHabil(fechaVto))
			return false;
		
		if(idReconfeccion!=null || idReconfeccion.longValue()>0)
			//TODO reconfeccionar() - validarParametros(): Obtener reconfeccion para realizar la validacion - arobledo
			return false;
			
		if(listDeuda==null || listDeuda.isEmpty())
			return false;
		
		for(Deuda deuda: listDeuda){
			if(deuda.getId()==null || deuda.getId().longValue()<=0)
				return false;
						
			if(deuda==null || deuda.getEsCyQ() || deuda.getEsReclamada() || deuda.getEsConvenio() ||
					deuda.getEsIndeterminada() || deuda.getEstado().intValue()!=EstadoDeuda.ID_ADMINISTRATIVA || 
					deuda.getEstado().intValue()!=EstadoDeuda.ID_JUDICIAL)
				return false;
		}*/
		return true;
		
	}

	/**
     * Aplica el mejor descuento SOBRE LA ACTUALIZACION de la deuda (al capital no se le aplica descuento, solo al recargo), entre los DesGen y DesEsp pasados como parametro.
     * <br>Setea a reciboDeuda, el descuento que se aplica en DesGen o DesEsp
     * @param deuda
     * @param desGenVigente Es el Descuento Gral. vigente a la fecha. Solo puede haber 1.
     * @param listDesEsp
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
	public ReciboDeuda mejorDescuento(Deuda deuda, Date fechaActualizacion, List<DesGen> listDesGenVigente, List<DesEsp> listDesEspVigente, boolean esRectificativa) throws Exception{    	
    	// Se crea el reciboDeuda que se va a devolver
    	ReciboDeuda reciboDeuda = new ReciboDeuda();
		reciboDeuda.setIdDeuda(deuda.getId());
		reciboDeuda.setCapitalOriginal(deuda.getSaldo());
		reciboDeuda.setPeriodoDeuda(deuda.getStrPeriodo());
		reciboDeuda.setTotCapital(deuda.getSaldo());
		reciboDeuda.setConceptos(deuda.getStrConceptosProp());
		Double actualizacionImporteCero=NumberUtil.truncate(ActualizaDeuda.actualizar(fechaActualizacion, reciboDeuda.getDeuda().getFechaVencimiento(), 1D, false,true).getRecargo()+1
				, SiatParam.DEC_PORCENTAJE_CALC);
		DeudaAct deudaActualizada = deuda.actualizacionSaldo(fechaActualizacion);
		
		if (esRectificativa){
			reciboDeuda.setRectificativa(SiNo.SI.getId());
		}
		//una deuda de autoliquidables puede estar en convenio e igual reconfeccionarse como rectificativa
		if (reciboDeuda.isValorCero()){
			reciboDeuda.setTotCapital(0D);
			reciboDeuda.setCapitalOriginal(0D);
			reciboDeuda.setActualizacion(NumberUtil.truncate(actualizacionImporteCero,SiatParam.DEC_PORCENTAJE_DB));
			reciboDeuda.setTotActualizacion(reciboDeuda.getActualizacion());
			reciboDeuda.setTotalReciboDeuda(0D);
		}else{
			// Se calcula la deuda al dia de la fecha de vto
			reciboDeuda.setActualizacion(NumberUtil.truncate(deudaActualizada.getRecargo(), SiatParam.DEC_IMPORTE_DB));
			reciboDeuda.setTotalReciboDeuda(NumberUtil.truncate(deudaActualizada.getImporteAct(), SiatParam.DEC_IMPORTE_DB));
			reciboDeuda.setTotActualizacion(NumberUtil.truncate(deudaActualizada.getRecargo(), SiatParam.DEC_IMPORTE_DB));
		}
		List dtos = new ArrayList();
    	dtos.addAll(listDesGenVigente);
    	dtos.addAll(listDesEspVigente);    	
    	for(Object dto: dtos){    		
    		boolean esAplicable =false;
    		double porDesAct = 0;//Representa el porcentaje de descuento sobre la actualizacion, del descuento actual    		
    		if(dto instanceof DesGen){
				porDesAct = ((DesGen)dto).getPorDes();
				esAplicable = true;    			
    		}else if(dto instanceof DesEsp){    			
    			// aqui se verifica si el descuento es aplicable en relacion a la fecha de vencimiento de la deuda
    			if (((DesEsp)dto).esAplicable(deuda, deuda.getFechaVencimiento())){
    				// Se calcula la deudmejorDescuentoa al dia de la fecha de vto
    				deudaActualizada = deuda.actualizacionSaldo(fechaActualizacion);
    				if(reciboDeuda.isValorCero()){
    					reciboDeuda.setActualizacion(NumberUtil.truncate(actualizacionImporteCero,SiatParam.DEC_PORCENTAJE_CALC));
    					reciboDeuda.setTotActualizacion(reciboDeuda.getActualizacion());
    				}else{
    					reciboDeuda.setActualizacion(NumberUtil.truncate(deudaActualizada.getRecargo(), SiatParam.DEC_IMPORTE_DB));
    					reciboDeuda.setTotalReciboDeuda(NumberUtil.truncate(deudaActualizada.getImporteAct(), SiatParam.DEC_IMPORTE_DB));
    					reciboDeuda.setTotActualizacion(NumberUtil.truncate(deudaActualizada.getRecargo(), SiatParam.DEC_IMPORTE_DB));
    				}
    					porDesAct = ((DesEsp)dto).getPorDesAct();
    					esAplicable = true;
    			}
    		}
    		if(esAplicable){
    			// Se calcula la actualizacion(el recargo) sobre la deuda actualizada a la fecha de vto
    			double descuentoActualizacion=0;
    			double totActualizacionActualizada=0;
    			if (reciboDeuda.isValorCero()){
    				//En este caso es solo un indice resultante de actualizar 1
    				descuentoActualizacion=NumberUtil.truncate(reciboDeuda.getActualizacion()*porDesAct,SiatParam.DEC_PORCENTAJE_CALC);
    				totActualizacionActualizada=reciboDeuda.getActualizacion()-descuentoActualizacion;
    			}else{
    				descuentoActualizacion =NumberUtil.round(deudaActualizada.getRecargo()*porDesAct, SiatParam.DEC_IMPORTE_CALC);
    				totActualizacionActualizada = NumberUtil.round(deudaActualizada.getRecargo() - descuentoActualizacion, SiatParam.DEC_IMPORTE_CALC);
    			}
    			
	    		if(totActualizacionActualizada<reciboDeuda.getTotActualizacion()){// Si el recargo es menor que el que tiene, se aplica el descuento
	    			reciboDeuda.setDesActualizacion(descuentoActualizacion);
    	    		reciboDeuda.setTotActualizacion(reciboDeuda.getActualizacion()-reciboDeuda.getDesActualizacion());
    	    		
    	    		//si es una reimpresion en blanco el total es cero
    	    		if(!reciboDeuda.isValorCero())
    	    			reciboDeuda.setTotalReciboDeuda(reciboDeuda.getTotCapital()+reciboDeuda.getTotActualizacion());
    	    		else
    	    			reciboDeuda.setTotalReciboDeuda(0D);
    	    		
    	    		if(dto instanceof DesGen){
    					reciboDeuda.setDesGen((DesGen)dto);    
    					log.debug("setea descuento general a reciboDeuda");
    	    		}else if(dto instanceof DesEsp){
    	    			reciboDeuda.setDesEsp((DesEsp)dto);
    					log.debug("setea descuento ESPECIAL a reciboDeuda");
	    			}
    			}
    		}	
    	}
    	
    	//volvemos a truncar luego de las operaciones
    	if(reciboDeuda.isValorCero()){
    		reciboDeuda.setActualizacion(NumberUtil.truncate(reciboDeuda.getActualizacion(), SiatParam.DEC_PORCENTAJE_DB));
    		reciboDeuda.setTotActualizacion(NumberUtil.truncate(reciboDeuda.getTotActualizacion(), SiatParam.DEC_PORCENTAJE_DB));
    	}else{
	    	reciboDeuda.setActualizacion(NumberUtil.truncate(reciboDeuda.getActualizacion(), SiatParam.DEC_IMPORTE_DB));
			reciboDeuda.setTotalReciboDeuda(NumberUtil.truncate(reciboDeuda.getTotalReciboDeuda(), SiatParam.DEC_IMPORTE_DB));
			reciboDeuda.setTotActualizacion(NumberUtil.truncate(reciboDeuda.getTotActualizacion(), SiatParam.DEC_IMPORTE_DB));
    	}
    	log.debug("Actualizacion de recibo deuda: "+reciboDeuda.getTotActualizacion());
    	return reciboDeuda;    	
    }
 
    /**
     * Obtiene la lista de DesEsp para la cuenta. filtra por recurso, viaDeuda(Judicial o administrativa) y tipoDeuda(Pura)
     * En todos los casos tiene en cuenta la vigencia (fecha desde y hasta)
     * @return
     * @throws Exception 
     */
    public List<DesEsp> getListDesEspVigentes(Long idTipoDeuda, Long idViaDeuda, Date fechaVtoDeuda) throws Exception{    
    	
    	// aqui se solicita la lista de los descuentos especiales vigentes. la fecha va nula porque no corresponde su utilizacion
    	List<DesEsp> listDesEsp = DesEsp.getListVigentes(getRecurso().getId(), idTipoDeuda, idViaDeuda, null, true);
    	List<DesEsp> listDesEspAplicables = new ArrayList<DesEsp>();    	
    	log.debug("#### recorre la lista de descuentos especiales");
    	
    	for(DesEsp desEsp: listDesEsp){
    		log.debug("#### "+ desEsp.getDesDesEsp());
    		
    		boolean esAplicable=true;
    		
    		/*control de exe y atributos*/
    		
    		// Controla que TODAS las exenciones vigentes del desEsp esten en la cuenta
    		List<Exencion> listExencionesVigentes = desEsp.getListExencionesVigentes(fechaVtoDeuda);
			if(listExencionesVigentes!=null && !tieneTodasLasExenciones(listExencionesVigentes, fechaVtoDeuda))
    			esAplicable=false;
    		
			// Controla que TODOS los atributos vigentes del desEsp esten en la cuenta    		
    		if(desEsp.getListDesAtrVal()!=null && !tieneTodosLosAtributos(desEsp.getMapAtrValVigentes(fechaVtoDeuda), fechaVtoDeuda)){
    			esAplicable=false;
    		}
    		
    		
    		if(esAplicable){
    			log.debug("Es aplicable");
    			listDesEspAplicables.add(desEsp);
    		}
    	}    	
    	return listDesEspAplicables;
    }
        
    public Recibo crearRecibo(Date fechaVto, Canal canal, ViaDeuda viaDeuda, ProcesoMasivo preProcesoMasivo, Procurador procurador, Long idBroche) {
    	log.debug("crearRecibo: enter - param ProcesoMasivo:"+preProcesoMasivo);
    	Calendar cal = Calendar.getInstance();
    	List<ServicioBanco> listSerBan = ServicioBanco.getVigentes(this.getRecurso());
    	    	
    	Recibo recibo = new Recibo();

		procurador = procurador!=null?Procurador.getByIdNull(procurador.getId()):null;
		recibo.setProcurador(procurador);

		recibo.setIdBroche(idBroche);
    	recibo.setEstaImpreso(SiNo.SI.getBussId());
    	recibo.setCodRefPag(GdeDAOFactory.getReciboDAO().getNextCodRefPago());
    	recibo.setServicioBanco((listSerBan!=null && !listSerBan.isEmpty()?listSerBan.get(0):null));
    	recibo.setRecurso(getRecurso());
    	recibo.setViaDeuda(viaDeuda);
    	recibo.setCuenta(this);
    	recibo.setCanal(canal);
    	recibo.setNroRecibo(GdeDAOFactory.getReciboDAO().getNextNroRecibo());
    	recibo.setAnioRecibo(cal.get(Calendar.YEAR));
    	recibo.setFechaGeneracion(cal.getTime());
    	recibo.setFechaVencimiento(fechaVto);
    	recibo.setProcesoMasivo(preProcesoMasivo);
    	
    	return recibo;
    }
    
    // Fin Reconfeccionar Deuda
        
    /**
     * El GenericDefinition que devuelve, contiene pares idAtributo-valor Atributo del objImp, del Contribuyente y de la cuenta
     * @param fecha
     * @return
     * @throws Exception
     */
    public GenericDefinition getCuentaDefinitionValue(Date fecha) throws Exception {

    	GenericDefinition genDefReturn = new GenericDefinition();

    	log.debug("Atributos de la cuenta:     Atributo - valor");

    	if (getObjImp() != null) {
	    	// Se obtienen todos los atributos del ObjImp y se los agrega a la lista 
	    	TipObjImpDefinition objImpDef = getObjImp().getDefinitionValue();
	    	if (objImpDef!=null) {
		    	for(TipObjImpAtrDefinition tipAtrDef: objImpDef.getListTipObjImpAtrDefinition()){
		    		GenericAtrDefinition genAtr = new GenericAtrDefinition();
				    genAtr.setAtributo(tipAtrDef.getAtributo());
				    genAtr.addValor(tipAtrDef.getValorString());
				    genDefReturn.getListGenericAtrDef().add(genAtr);
				    log.debug("   del ObjImp:"+tipAtrDef.getAtributo().getDesAtributo()+"   "+tipAtrDef.getValorString());
		    	}
	    	}
    	}
    	// Se obtienen todos los atributos del Contribuyente y se los agrega a la lista
    	ContribuyenteDefinition contribDef = getUnionConAtrVal(false);
    	if (contribDef!=null) {
	    	for(ConAtrDefinition conAtrDef: contribDef.getListConAtrDefinition()){
	    		GenericAtrDefinition genAtr = new GenericAtrDefinition();
			    genAtr.setAtributo(conAtrDef.getAtributo());
			    genAtr.addValor(conAtrDef.getValorString());
			    genDefReturn.getListGenericAtrDef().add(genAtr);
			    log.debug("   del contibuyente:"+conAtrDef.getAtributo().getDesAtributo()+"   "+conAtrDef.getValorString());
	    	}
    	}
    	// Se obtienen todos los atributos de la cuenta y se los agrega a la lista
    	GenericDefinition atrValDef = getRecCueAtrDefinitionValue(fecha);
    	if(atrValDef!=null){
	    	for(GenericAtrDefinition atrDef : atrValDef.getListGenericAtrDef()){
	    		GenericAtrDefinition genAtr = new GenericAtrDefinition();
	    		genAtr.setAtributo(atrDef.getAtributo());
	    		genAtr.addValor(atrDef.getValorString());
	    		genDefReturn.getListGenericAtrDef().add(genAtr);
	    		log.debug("   de la cuenta:"+atrDef.getAtributo().getDesAtributo()+"   "+atrDef.getValorString());
	    	}
    	}
    	return genDefReturn;
	}
    
    /**
     * El GenericDefinition que devuelve, contiene pares idAtributo-valor Atributo del objImp, del Contribuyente full (no solo los visibles) y de la cuenta
     * @param fecha
     * @return
     * @throws Exception
     */
    public GenericDefinition getCuentaDefinitionValueFullContr(Date fecha) throws Exception {

    	GenericDefinition genDefReturn = new GenericDefinition();

    	log.debug("Atributos de la cuenta:     Atributo - valor");

    	if (getObjImp() != null) {
	    	// Se obtienen todos los atributos del ObjImp y se los agrega a la lista 
	    	TipObjImpDefinition objImpDef = getObjImp().getDefinitionValue();
	    	if (objImpDef!=null) {
		    	for(TipObjImpAtrDefinition tipAtrDef: objImpDef.getListTipObjImpAtrDefinition()){
		    		GenericAtrDefinition genAtr = new GenericAtrDefinition();
				    genAtr.setAtributo(tipAtrDef.getAtributo());
				    genAtr.addValor(tipAtrDef.getValorString());
				    genDefReturn.getListGenericAtrDef().add(genAtr);
				    log.debug("   del ObjImp:"+tipAtrDef.getAtributo().getDesAtributo()+"   "+tipAtrDef.getValorString());
		    	}
	    	}
    	}
    	// Se obtienen todos los atributos del Contribuyente y se los agrega a la lista
    	ContribuyenteDefinition contribDef = getUnionConAtrValFull(false);
    	if (contribDef!=null) {
	    	for(ConAtrDefinition conAtrDef: contribDef.getListConAtrDefinition()){
	    		GenericAtrDefinition genAtr = new GenericAtrDefinition();
			    genAtr.setAtributo(conAtrDef.getAtributo());
			    genAtr.addValor(conAtrDef.getValorString());
			    genDefReturn.getListGenericAtrDef().add(genAtr);
			    log.debug("   del contibuyente:"+conAtrDef.getAtributo().getDesAtributo()+"   "+conAtrDef.getValorString());
	    	}
    	}
    	// Se obtienen todos los atributos de la cuenta y se los agrega a la lista
    	GenericDefinition atrValDef = getRecCueAtrDefinitionValue(fecha);
    	if(atrValDef!=null){
	    	for(GenericAtrDefinition atrDef : atrValDef.getListGenericAtrDef()){
	    		GenericAtrDefinition genAtr = new GenericAtrDefinition();
	    		genAtr.setAtributo(atrDef.getAtributo());
	    		genAtr.addValor(atrDef.getValorString());
	    		genDefReturn.getListGenericAtrDef().add(genAtr);
	    		log.debug("   de la cuenta:"+atrDef.getAtributo().getDesAtributo()+"   "+atrDef.getValorString());
	    	}
    	}
    	return genDefReturn;
	}


    public GenericDefinition getCuentaDefinitionValue(TipObjImpDefinition objImpDef,
    												  ContribuyenteDefinition contribDef, 
    												  RecAtrCueDefinition recAtrCueDef, 
    												  Date fechaAnalisis) throws Exception {

		log.debug("Obteniendo definicion de la cuenta");
		GenericDefinition definition = new GenericDefinition();
	
		// Se obtienen todos los atributos del objeto imponible y se los agrega a la lista
		if (objImpDef != null) {
			if (this.getObjImp() != null) {
				log.debug("Obteniendo atributos del Objeto Imponible");
				this.getObjImp().loadDefinition(objImpDef, fechaAnalisis);
	
				for (TipObjImpAtrDefinition tipAtrDef: objImpDef.getListTipObjImpAtrDefinition()){
					GenericAtrDefinition genAtr = new GenericAtrDefinition();
					genAtr.setAtributo(tipAtrDef.getAtributo());
					genAtr.addValor(tipAtrDef.getValorString());
					definition.getListGenericAtrDef().add(genAtr);
					log.debug("Atributo del Objeto Imponible: " + 
					tipAtrDef.getAtributo().getDesAtributo() + " - " + tipAtrDef.getValorString());
				}
			}
		}
	
		// Se obtienen todos los atributos del contribuyente y se los agrega a la lista
		if (contribDef != null)  {
			log.debug("Obteniendo atributos del Contribuyente");
			getUnionConAtrVal(true);
	
			for (ConAtrDefinition conAtrDef: contribDef.getListConAtrDefinition()) {
				GenericAtrDefinition genAtr = new GenericAtrDefinition();
				genAtr.setAtributo(conAtrDef.getAtributo());
				genAtr.addValor(conAtrDef.getValorString());
				definition.getListGenericAtrDef().add(genAtr);
				log.debug("Atributo del Contibuyente: " + 
				conAtrDef.getAtributo().getDesAtributo() + " - "+ conAtrDef.getValorString());
			}
		}
	
		// Se obtienen todos los atributos de la cuenta y se los agrega a la lista
		if (recAtrCueDef != null) {
			log.debug("Obteniendo atributos de la Cuenta");
			GenericDefinition atrValDef = getRecCueAtrDefinitionValue(fechaAnalisis);
	
			for (GenericAtrDefinition atrDef : atrValDef.getListGenericAtrDef()) {
				GenericAtrDefinition genAtr = new GenericAtrDefinition();
				genAtr.setAtributo(atrDef.getAtributo());
				genAtr.addValor(atrDef.getValorString());
				definition.getListGenericAtrDef().add(genAtr);
				log.debug("Atributo de la cuenta: " + 
				atrDef.getAtributo().getDesAtributo() + " - " + atrDef.getValorString());
			}
		}

		return definition;
   }
   
    /**
     * Obtiene un la lista de Atributos de la Cuenta.
     * Devuelve GenericDefinition con la lista de RecAtrCueV valorizada desde la cuenta.
     * 
     * @param fecha
     * @return
     * @throws Exception
     */
    public GenericDefinition getRecCueAtrDefinitionValue(Date fecha) throws Exception{
    	GenericDefinition genDef = new GenericDefinition();
    	List<RecAtrCueV> listRecAtrCueV = getListRecAtrCueV(fecha);
    	if(listRecAtrCueV!=null && !listRecAtrCueV.isEmpty()){
	    	for(RecAtrCueV recAtrCueV: listRecAtrCueV){
	    		GenericAtrDefinition genAtrDef = new GenericAtrDefinition();
	    		genAtrDef.setAtributo((AtributoVO) recAtrCueV.getRecAtrCue().getAtributo().toVO(3));
	    		genAtrDef.setValor(recAtrCueV.getValor());
	    		genDef.getListGenericAtrDef().add(genAtrDef);
	    	}
    	}
    	return genDef;
    }
    
    /**
     * Obtiene un la lista de Atributos de la Cuenta.
     * Devuelve RecursoDefinition con la lista de RecAtrCueDefinition cargada desde el recuro y valorizada desde la cuenta.
     * 
     * 
     * @param fecha
     * @return
     * @throws Exception
     */
    public RecursoDefinition getRecCueAtrValDefinitionValue(Date fecha) throws Exception{
    	// Obtenemos la valorizacion vigente
    	// Por cada una, obtenemos la definicion

    	RecursoDefinition recursoDefinition = new RecursoDefinition();
    	List<RecAtrCueV> listRecAtrCueV = getListRecAtrCueV(fecha);
    	if(listRecAtrCueV!=null && !listRecAtrCueV.isEmpty()){
	    	for(RecAtrCueV recAtrCueV: listRecAtrCueV){
	    		// Solo devolvemos los valorezados
	    		if (!StringUtil.isNullOrEmpty(recAtrCueV.getValor())){	    			
		    		RecAtrCueDefinition recAtrCueDefinition = new RecAtrCueDefinition();
		    		recAtrCueDefinition.setRecAtrCue((RecAtrCueVO) recAtrCueV.getRecAtrCue().toVO(0));
					recAtrCueDefinition.getRecAtrCue().setAtributo((AtributoVO)(recAtrCueV.getRecAtrCue().getAtributo().toVO(2)));
					recAtrCueDefinition.addValor(recAtrCueV.getValor());
		    		recursoDefinition.getListRecAtrCueDefinition().add(recAtrCueDefinition);
	    		}
	    	}
    	}
    	
    	return recursoDefinition;
    }
    
    
    
    /**
     * Retorna true si la cuenta posee "todos" los atributos pasados por parametros en el map y vigentes a la fecha pasada como parametro
     */
    public boolean tieneTodosLosAtributos(Map<Long, String> map, Date fechaVigencia) throws Exception {
    	GenericDefinition genDef = getCuentaDefinitionValue(fechaVigencia);
    	for(GenericAtrDefinition gen:genDef.getListGenericAtrDef()){
    		log.debug("Atributo de la cuenta(id, desc, valor): "+gen.getAtributo().getId()+"  -  "+gen.getAtributo().getDesAtributo()+"  -  -"+gen.getValorString());
    	}
    	if(genDef!=null){
	    	Set<Long> keysIdsAtr = map.keySet();
	    	for(Long idAtr: keysIdsAtr){
	    		GenericAtrDefinition genAtrDef = genDef.getGenericAtrDefinition(idAtr);	    		
	    		if(genAtrDef == null){// Si al menos 1 no esta, devuelve false
	    			log.debug("No encontrï¿½ el id:"+idAtr);
	    			return false;
	    		}else if(!genAtrDef.getValorString().equals(map.get(idAtr))){
	    			log.debug("encontrï¿½ el id:"+idAtr+"  pero con valor distinto");
	    			return false;// Si el valor no es igual, devuelve false
	    		}
	    	}
    	}
    	return true;
    }
    
	/**
     * Retorna true si la cuenta posee "todas" las exenciones que contiene la cuenta de la lista pasada por parametros y vigentes
     * a la fecha recibida.
     * 
     * @param listExencion 
     * @return lista vacia si no hay exenciones en la lista que esten en la cuenta.
     * @throws Exception
     */
    public boolean tieneTodasLasExenciones(List<Exencion> listExencion, Date fecha) throws Exception {
    	
    	String funcName = "tieneTodasLasExenciones";
    	
    	// Recupero las exenciones vigentes de la cuenta
    	List<CueExe> listCueExe = getListCueExeVigente(fecha);
    	
    	log.debug(funcName + " listCueExe.size: " + listCueExe.size());
    	
    	// Si no posee ninguna, salgo por falso
    	if (listCueExe.size() == 0){
    		return false;
    	}
    	
    	// Formo una lista de Exenciones
    	List<Exencion> listExencioneCuenta = new ArrayList<Exencion>();  
    	for (CueExe cueExe:listCueExe){
    		Exencion exencion = cueExe.getExencion();
    		log.debug(funcName + " exencion: " + exencion.getDesExencion());
    		listExencioneCuenta.add(exencion);
    	}	
    	
    	// Si todas las exenciones de la lista tambien las tiene la cuenta.    	
    	for (Exencion exencionCuenta: listExencion){
    		if (!ListUtilBean.contains(listExencioneCuenta, exencionCuenta)){
    			return false;
    		}
    	}
    	// Sale por verdadero despues de for porque no encontro exencion NO contenida en la lista.
    	return true;
    }

    /**
     * Devuelve true si la cuenta posee alguna de las exenciones recibidas. 
     *  
     * @author Cristian
     * @param listExencion
     * @param fecha
     * @return boolean
     * @throws Exception
     */
    public boolean tieneAlgunaExencion(List<Exencion> listExencion, Date fecha) throws Exception {
    	
    	String funcName = "tieneAlgunaExencion";
    	
    	// Recupero las exenciones vigentes de la cuenta.
    	List<CueExe> listCueExe = getListCueExeVigente(fecha);
    	log.debug(funcName + " listCueExe.size: " + listCueExe.size());
    	log.debug(funcName + " listExencion.size: " + listExencion.size());
    	
    	// Si no posee ninguna, salgo por falso.
    	if (listCueExe.size() == 0){
    		return false;
    	}
    	
    	// Si posee al menos una exencion vigente, de las contenidas en la lista recibida.
    	for (CueExe cueExe:listCueExe){
    		Exencion exencion = cueExe.getExencion();
    		log.debug(funcName + " exencion: " + exencion.getDesExencion());
    		
    		if (ListUtilBean.contains(listExencion, exencion)){
    			return true;
    		}
    	}
    	// Sale por falso si ninguna de las exenciones de la cuenta esta en la lista recibida.
    	return false;
    
    }

    /**
     * Obtiene la lista de CueExcSel activas para la cuenta
     * @return List<CueExcSel>
     */
    public List<CueExcSel> getListCueExcSelActivas(){

    	return PadDAOFactory.getCueExcSelDAO().getListCueExcSelActivas(this);
    }

    
    /**
     * Genera el Informe de Deuda de la cuenta.<br>Contiene la cuenta principal con su lista de deudas.
     * Si es para Escribanos setea ademï¿½s las cuentas relacionadas por ObjImp y por desgloce/unificacion
     * @return
     * @throws Exception
     */
    public InformeDeudaAdapter getInformeDeuda(InformeDeudaCaratula informeDeudaCaratula, boolean paraEscribano) throws Exception {
    	InformeDeudaAdapter informe = new InformeDeudaAdapter();
    	
    	LiqDeudaBeanHelper deudaBeanHelper = new LiqDeudaBeanHelper(this);
    	
    	LiqDeudaAdapter liqDeudaAdapterCuentaPpal = null;
    	
    	if(paraEscribano){
    		
    		liqDeudaAdapterCuentaPpal = deudaBeanHelper.getLiqDeudaAdapterConConveniosVigentes();
    		
	    	// - Incluimos las cuentas relacionadas por ObjImp
	    	for(Cuenta cuenta: this.getListCuentaRelacionadas()){
	    		deudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
	    		informe.getListCuentaRelObjImp().add(deudaBeanHelper.getLiqDeudaAdapterConConveniosVigentes());
	    	}    	
	    	
	    	// - Incluimos las cuentas relacionadas por Desgloses / Unificaciones
	    	for(Cuenta cuenta:this.getListCuentasMadreHija()){
	    		deudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
	    		informe.getListCuentaRelDesUni().add(deudaBeanHelper.getLiqDeudaAdapterConConveniosVigentes());
	    	}
    	}else{
    		liqDeudaAdapterCuentaPpal = deudaBeanHelper.getLiqDeudaAdapter();//Sin los convenios
    	}

    	
    	// Seteamos datos de Cuenta CdM - en caso que corresponda.
    	DatosReciboCdM datosCdM = this.cargarDatosCdM();
    	
    	if (datosCdM != null){
    		informe.setDatosCdMVO( (DatosReciboCdMVO) datosCdM.toVO(0));
    	}
    	
    	// seteamos en el informe la cuenta principal
    	informe.setLiqDeudaAdapterCuentaPpal(liqDeudaAdapterCuentaPpal);
    	
    	// seteamos la cartula, si llega nula no se imprime
    	if (informeDeudaCaratula != null){
    		informe.setPoseeCaratula(true);
    		
    		informeDeudaCaratula.setCuenta(liqDeudaAdapterCuentaPpal.getCuenta());
    		informe.setInfomeDeudaCaratula(informeDeudaCaratula);
    	}
    	
    	return informe;
    }
    
    
    /**
     * Genera el PrintModel con el InformeDeudaAdapter, para imprimirlo para Escribanos
     * @return
     * @throws Exception
     */
    public PrintModel getImprimirInformeDeudaEscribano(InformeDeudaCaratula informeDeudaCaratula) throws Exception{
    	
    	InformeDeudaAdapter informeDeudaAdapter = getInformeDeuda(informeDeudaCaratula, true);
		PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_INFORME_DEUDA);
		
		print.putCabecera("NombreMes", DateUtil.getMesEnLetra(new Date()));
		print.putCabecera("Categoria", getRecurso().getCategoria().getDesCategoria().toUpperCase());
		informeDeudaAdapter.setFechaConfeccion(new Date());	
		print.setData(informeDeudaAdapter);
		print.setTopeProfundidad(5);
		
		return print;
    }
    
    /**
     * Genera el PrintModel con el InformeDeudaAdapter para la Liquidacion de la Deuda (Igual que el de escribanos pero para esta cuenta sola y sin los convenios, ni las cuentas relacionadas y demas)
     * @return
     * @throws Exception
     */
    public PrintModel getInformeLiqDeuda() throws Exception{
    	
    	InformeDeudaAdapter informeDeudaAdapter = getInformeDeuda(null, false);
    	if(this.getRecurso().getEsAutoliquidable().intValue()==SiNo.SI.getId().intValue())
    		informeDeudaAdapter.setEsAutoliquidable(true);
    	if(this.getObjImp()!=null && this.getObjImp().getTipObjImp().equals(TipObjImp.getById(TipObjImp.COMERCIO))){
    		informeDeudaAdapter.setPoseeComercio(true);
    	}
    	informeDeudaAdapter.getLiqDeudaAdapterCuentaPpal().getCuenta().setDetalleFiltros(this.getDetalleFiltrosLiqDeuda());
		PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_INFORME_DEUDA);

		print.putCabecera("NombreMes", DateUtil.getMesEnLetra(new Date()));
		print.putCabecera("Categoria", getRecurso().getCategoria().getDesCategoria().toUpperCase());
		informeDeudaAdapter.setFechaConfeccion(new Date());	
		print.setData(informeDeudaAdapter);
		print.setTopeProfundidad(6);
		
		return print;    	
    }
    
    /**
     * Devuelve el Valor del Atributo asociado a la Cuenta, vigente en la Fecha de Actual.
     * 
     * @param idAtributo
     * @return
     */
    public String getValorAtributo(Long idAtributo){
    	ObjImpAtrVal objImpAtrVal = null;
    	TipObjImpAtr tipObjImpAtr = TipObjImpAtr.getByIdAtributo(idAtributo);
    	if(tipObjImpAtr != null)
    		objImpAtrVal = ObjImpAtrVal.getVigenteByIdObjImpAtrVal(tipObjImpAtr.getId(), this.getObjImp().getId());
    	if(objImpAtrVal != null)
    		return objImpAtrVal.getStrValor();
    	return null;
    }
 
    /**
     * Devuelve el Valor del Atributo asociado a la Cuenta, vigente en la Fecha pasada.
     * 
     * @param idAtributo
     * @param fechaVigencia
     * @return
     */
    public String getValorAtributo(Long idAtributo, Date fechaVigencia){
    	// Primero se busca si el atributo es de objeto imponible
    	ObjImpAtrVal objImpAtrVal = null;
    	TipObjImpAtr tipObjImpAtr = TipObjImpAtr.getByIdAtributo(idAtributo);
    	if(tipObjImpAtr != null)
    		objImpAtrVal = ObjImpAtrVal.getVigenteByIdObjImpAtrVal(tipObjImpAtr.getId(), this.getObjImp().getId(), fechaVigencia);
    	if(objImpAtrVal != null){
    		return objImpAtrVal.getStrValor();
    	}
    	// Si no se encuentra para el objeto imponible se busca como atributo de cuenta.
    	RecAtrCueV recAtrCueV = null;
    	RecAtrCue recAtrCue = RecAtrCue.getAbiertoByIdRecAtrCue(this.getRecurso().getId(), idAtributo);
    	if(recAtrCue != null)
    		recAtrCueV = RecAtrCueV.getVigenteByIdRecAtrCue(recAtrCue.getId(), this.getId(), fechaVigencia);
    	if(recAtrCueV != null){
    		return recAtrCueV.getValor();
    	}
    	
    	// Si no se encontro un valor para la fecha de vigencia y el Recurso es TGI se devuelve el valor correspondiene a 'Baldio'
    	if(Recurso.COD_RECURSO_TGI.equals(this.getRecurso().getCodRecurso()))
    		return "2";
    	// Si no se encontro un valor para la fecha de vigencia y el Recurso es Drei se devuelve el valor 0
    	if(Recurso.COD_RECURSO_DReI.equals(this.getRecurso().getCodRecurso()))
    		return "0";
    
    	return null;
    }
    
    /**
     * Devuelve el Valor del Atributo asociado a la Cuenta, vigente en la Fecha pasada considerando las novedades a
     * la fecha actual.
     * 
     * @param idAtributo
     * @param fechaVigencia
     * @return
     */
    @Deprecated
    public String getValorAtributoParaUltimasNovedades(Long idAtributo, Date fechaVigencia){
    	ObjImpAtrVal objImpAtrVal = null;
    	TipObjImpAtr tipObjImpAtr = TipObjImpAtr.getByIdAtributo(idAtributo);
    	if(tipObjImpAtr != null){
    		List<ObjImpAtrVal> listObjImpAtrVal = ObjImpAtrVal.getListByIdObjImpAtrVal(tipObjImpAtr.getId(), this.getObjImp().getId());
    		// La lista viene ordenada por fechaDesde de mayor a menor. Por eso preguntamos si la fechaDesde del 
    		// objImpAtrVal que estamos verificando es menos o igual a la de vigencia. En cuyo caso este es el valor 
    		// que corresponde.
    		for(ObjImpAtrVal oiav: listObjImpAtrVal){
    			if(DateUtil.isDateBeforeOrEqual(oiav.getFechaDesde(), fechaVigencia)){
    				objImpAtrVal = oiav;
    				break;
    			}
    		}
    	}
    	if(objImpAtrVal != null){
    		return objImpAtrVal.getStrValor();
    	}

    	// Si no se encontro un valor para la fecha de vigencia y el Recurso es TGI se devuelve el valor correspondiene a 'Baldio'
    	if(Recurso.COD_RECURSO_TGI.equals(this.getRecurso().getCodRecurso()))
    		return "2";
    	// Si no se encontro un valor para la fecha de vigencia y el Recurso es Drei se devuelve el valor 0
    	if(Recurso.COD_RECURSO_DReI.equals(this.getRecurso().getCodRecurso()))
    		return "0";

       	// Si no se encontro un valor para la fecha de vigencia se devuelve null
    	return null;
    }
    
    /**
     * Devuelve la lista de convenios Vigentes que pueda tener la cuenta.
     * 
     * @author Cristian
     * @return
     */
    public List<Convenio> getListConveniosVigentes(){
    	
    	List<Convenio> listConvenioRet = new ArrayList<Convenio>();
    	
    	for (Convenio convenio:getListConvenios()){
    		if (convenio.getEstadoConvenio().getId().equals(EstadoConvenio.ID_VIGENTE)){
    			listConvenioRet.add(convenio);	
    		}    		    		
    	}
    	
    	return listConvenioRet;
    }
    
    
    /**
     * Devuelve la lista de convenios Recompuestos que pueda tener la cuenta.
     * 
     * @author Cristian
     * @return
     */
    public List<Convenio> getListConveniosRecompuestos(){
    	
    	List<Convenio> listConvenioRet = new ArrayList<Convenio>();
    	
    	for (Convenio convenio:getListConvenios()){
    		if (convenio.getEstadoConvenio().getId().equals(EstadoConvenio.ID_RECOMPUESTO)){
    			listConvenioRet.add(convenio);	
    		}    		    		
    	}
    	
    	return listConvenioRet;
    }
    
    

    /**
     * Obtiene la lista de deudas actuales de la cuenta, de todo tipo, ordenadas por periodo y anio
     * @param searchPage - contiene, entre otras propiedades, el estado de las deudas que se quiere incluir en el estadoCuenta. Si es nulo o con id=0, se incluyen todas las deudas de la cuenta.
     * @return
     */
    public List<Deuda> getListDeudaEstadoCuenta(EstadoCuentaSearchPage searchPage){
    	List<Deuda> listDeuda = new ArrayList<Deuda>();
    	long idEstadoDeuda = (searchPage.getEstadoDeuda()!=null?searchPage.getEstadoDeuda().getId().longValue():0L);
    	
    	if(idEstadoDeuda>0){
    		
	    	if(idEstadoDeuda==EstadoDeuda.ID_ANULADA || idEstadoDeuda==EstadoDeuda.ID_CONDONADA || 
	    	   idEstadoDeuda==EstadoDeuda.ID_PRESCRIPTA){
	    			listDeuda.addAll(GdeDAOFactory.getDeudaAnuladaDAO().getEstadoCuentaBySearchPage(searchPage));
	    	}
	    	
	    	if(idEstadoDeuda==EstadoDeuda.ID_CANCELADA){
	    		listDeuda.addAll(GdeDAOFactory.getDeudaCanceladaDAO().getEstadoCuentaBySearchPage(searchPage));
	    		listDeuda.addAll(GdeDAOFactory.getDeudaAdminDAO().getEstadoCuentaBySearchPage(searchPage));
	    		listDeuda.addAll(GdeDAOFactory.getDeudaJudicialDAO().getEstadoCuentaBySearchPage(searchPage));
	    	}
	    	
	    	if(idEstadoDeuda==EstadoDeuda.ID_JUDICIAL){// es la impaga
	    		
	    		searchPage.getEstadoDeuda().setId(EstadoDeuda.ID_ADMINISTRATIVA);// setea el id para buscar las administrativas impagas
	    		listDeuda.addAll(GdeDAOFactory.getDeudaAdminDAO().getEstadoCuentaBySearchPage(searchPage));
	    		searchPage.getEstadoDeuda().setId(EstadoDeuda.ID_JUDICIAL);// vuelve a setear el id seleccionado para la busqueda
	    		
	    		listDeuda.addAll(GdeDAOFactory.getDeudaJudicialDAO().getEstadoCuentaBySearchPage(searchPage));
	    	}
	    	
    	}else{
    		// busca en todas las tablas
    		listDeuda.addAll(GdeDAOFactory.getDeudaAdminDAO().getEstadoCuentaBySearchPage(searchPage));
    		listDeuda.addAll(GdeDAOFactory.getDeudaAnuladaDAO().getEstadoCuentaBySearchPage(searchPage));
    		listDeuda.addAll(GdeDAOFactory.getDeudaCanceladaDAO().getEstadoCuentaBySearchPage(searchPage));
    		listDeuda.addAll(GdeDAOFactory.getDeudaJudicialDAO().getEstadoCuentaBySearchPage(searchPage));
    	}

    	// Obtiene las deudas
/*    	if(idEstadoDeuda==EstadoDeuda.ID_ADMINISTRATIVA || idEstadoDeuda<=0)
    		listDeuda.addAll(GdeDAOFactory.getDeudaAdminDAO().getEstadoCuentaBySearchPage(searchPage));

    	if(idEstadoDeuda==EstadoDeuda.ID_ANULADA || idEstadoDeuda<=0)
    		listDeuda.addAll(GdeDAOFactory.getDeudaAnuladaDAO().getEstadoCuentaBySearchPage(searchPage));
    	
    	if(idEstadoDeuda==EstadoDeuda.ID_CANCELADA || idEstadoDeuda<=0)
    		listDeuda.addAll(GdeDAOFactory.getDeudaCanceladaDAO().getEstadoCuentaBySearchPage(searchPage));

    	if(idEstadoDeuda==EstadoDeuda.ID_JUDICIAL || idEstadoDeuda<=0)
    		listDeuda.addAll(GdeDAOFactory.getDeudaJudicialDAO().getEstadoCuentaBySearchPage(searchPage));
    	*/
    	// Ordena las deudas
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

    /**
     * Indica si la cuenta posee exenciones vigentes que no permiten el envio a judicial.
     * Osea que tiene una cueExe con una exencion que tiene la marca EnviaJudicial en false. 
	 * @return la cueExe que no permite el envio a judicial. Si posee mas de una es indefinido cual retorna.
	 *         <br>null si permite el envio a judicial.
     * @throws Exception
     */
    public Exencion exentaEnvioJud(Date fechaAnalisis, Date fechaVencimiento, CueExeCache cueExeCache)throws Exception{
    	// obtener todas las exenciones que posea la cuenta y que no actualizen deuda
    	/*	Por cada una preguntamos
	    		Si esta vigente a la fecha de hoy, devolver true
	    		Si esta vigente a la fecha de vencimiento recibida, devolver true
	    		sino false
    	 */	

    	List<CueExe> listExencionesNoActDeuda = cueExeCache.getListCueExeNoEnviaJudicial(this.getId());
    	if (listExencionesNoActDeuda == null)
    		return null;
    	
    	for (CueExe cueExe : listExencionesNoActDeuda) {

    		// Si es vigente a la fecha de hoy, retornamos true sin tener en cuenta la fecha de vencimiento de la deuda,
    		// porque no actualiza deuda. 
    		// fedel: 2009-may-19, bug: 687, para que este exenta solo importa si la la cuexe estaba vigente al
    		//        vencimiento. Por eso comnetamos las dos lineas de abajo.
    		//if (DateUtil.isDateInRange(fechaAnalisis, cueExe.getFechaDesde(), cueExe.getFechaHasta())){
    		//	return true;
    		//}

    		// Si fue vigente a la fecha de Vencimiento de la deuda.
    		// devolvemos true porque estamos dentro del caso "exencionesAlVencimiento=true"
    		if (DateUtil.isDateInRange(fechaVencimiento, cueExe.getFechaDesde(), cueExe.getFechaHasta())){
    			return cueExe.getExencion();
    		}
    	}
    	return null;
    }
	
	public Cuenta createCuentaSecundaria(Recurso recurso, String codGesCue, String numeroCuenta) throws Exception {
		return createCuentaSecundaria(recurso, codGesCue, numeroCuenta, new Date());
	}

    /**
	 * Crea una cuenta secundaria a partir de esta cuenta.
	 * La cuenta secundaria se crea copiando el mismo domicilioEnvio y los titulares Activos de esta cuenta.
	 * La nueva cuenta secundaria tendra el recurso, nroCuenta y codGesCue de los parametros.
	 */
	public Cuenta createCuentaSecundaria(Recurso recurso, String codGesCue, String numeroCuenta, Date fechaAlta) throws Exception {
		Cuenta cuentaSecundaria = new Cuenta();
		
		// Se copian los atributos de la cuenta Primaria
		// y los pasados como parametros
		cuentaSecundaria.setRecurso(recurso);
		cuentaSecundaria.setObjImp(this.getObjImp());
		cuentaSecundaria.setNumeroCuenta(StringUtil.formatNumeroCuenta(numeroCuenta));
		cuentaSecundaria.setCodGesCue(codGesCue);
		cuentaSecundaria.setDomicilioEnvio(this.getDomicilioEnvio());
		cuentaSecundaria.setBroche(this.getBroche());
		cuentaSecundaria.setFechaAlta(fechaAlta);
		cuentaSecundaria.setFechaBaja(null);
		cuentaSecundaria.setCatDomEnv(this.getCatDomEnv());
		cuentaSecundaria.setDesDomEnv(this.getDesDomEnv());
		cuentaSecundaria.setNomTitPri(this.getNomTitPri());
		cuentaSecundaria.setCuitTitPri(this.getCuitTitPri());
		
		PadCuentaManager.getInstance().createCuenta(cuentaSecundaria);
		
		if(cuentaSecundaria.hasError()){
			return cuentaSecundaria;
		}
		// Se copian los titulares de la cuenta Primaria
		for (CuentaTitular cuentaTitularPpal: getListCuentaTitular()){
			CuentaTitular cuentaTitularSec = new CuentaTitular();
			
			cuentaTitularSec.setCuenta(cuentaSecundaria);
			cuentaTitularSec.setContribuyente(cuentaTitularPpal.getContribuyente());
			cuentaTitularSec.setTipoTitular(cuentaTitularPpal.getTipoTitular());
			cuentaTitularSec.setEsTitularPrincipal(cuentaTitularPpal.getEsTitularPrincipal()); 
			cuentaTitularSec.setFechaNovedad(new Date());
			cuentaTitularSec.setFechaDesde(new Date());
			cuentaTitularSec.setFechaHasta(null);
			cuentaTitularSec.setEsAltaManual(SiNo.NO.getId());
			cuentaTitularSec.setIdCaso(null);

			PadDAOFactory.getCuentaTitularDAO().update(cuentaTitularSec);
		
			if(cuentaTitularSec.hasError()){
				cuentaTitularSec.passErrorMessages(cuentaSecundaria);
				return cuentaSecundaria;
			}
		}
		
		return cuentaSecundaria;
	}

	public List<Deuda> getListDeudaCDMCancelada(){
		
		return this.getListDeudaCDMCancelada(false);
	}
	
	
	public List<Deuda> getListDeudaCDMCancelada(boolean obraPoseeAlgunaExencion){
		
    	List<Deuda> listDeuda = new ArrayList<Deuda>();

    	if (!obraPoseeAlgunaExencion) {
	    	List<Deuda> listDeudaAdmin = GdeDAOFactory.getDeudaAdminDAO().getListDeudaCDMCancelada(this);
	    	listDeuda.addAll(listDeudaAdmin);
    	}
    	
    	List<Deuda> listDeudaCancelada = GdeDAOFactory.getDeudaCanceladaDAO().getListDeudaCDMCancelada(this);
    	
    	listDeuda.addAll(listDeudaCancelada);
    	
    	return listDeuda;
	}
 	
	/**
 	 * Retorna la deuda administrativa filtrando por Cuenta, anio, periodo
 	 * @param anio
 	 * @param periodo
 	 * @param estaImpresa
 	 * @return List<DeudaAdmin>
 	 * */
	public List<DeudaAdmin> getListDeudaAdminByAnioAndPeriodo(Integer anio, Integer periodo) {
		return GdeDAOFactory.getDeudaAdminDAO().getList(this, periodo, anio,null);
			
	}
	
 	/**
 	 * Retorna la deuda administrativa (de la tabla gde_DeudaAdmin) 
 	 * de la cuenta con id idCuenta, y con periodos que se encuentran
 	 * en el intervalo [(periodoDesde,anioDesde), (periodoHasta,anioHasta)] 
 	 * 
 	 * @param idCuenta
 	 * @param periodoDesde
 	 * @param anioDesde
 	 * @param periodoHasta
 	 * @param anioHasta
 	 * @return List<DeudaAdmin>
 	 * */
	public List<DeudaAdmin> getListDeudaAdminByRangoPeriodoAnio
			(Integer  periodoDesde, Integer anioDesde, Integer periodoHasta, Integer anioHasta) {
		
		return GdeDAOFactory.getDeudaAdminDAO().getListDeudaAdminByRangoPeriodoAnio
			(this.getId(), periodoDesde, anioDesde, periodoHasta, anioHasta);
	}
	
	/**
 	 * Retorna la deuda administrativa filtrando por Cuenta, anio, periodo.
 	 * @param anio
 	 * @param periodo
 	 * @return List<DeudaAdmin>
 	 * */
	public List<DeudaAdmin> getListDeudaAdminForAsentamiento(Integer anio, Integer periodo) {
		return GdeDAOFactory.getDeudaAdminDAO().getListDeudaAdminForAsentamiento(this.getId(), anio, periodo);
	}
	
	public static List<Cuenta> getListByNumeroCuenta(String nroCuenta) {
		
		return PadDAOFactory.getCuentaDAO().getListByNumeroCuenta(nroCuenta);
	}
	
	
	/**
	 * Crea un bean de Recibo para una cuota de 
	 * Contribucion de Mejoras
	 * @param DeudaAdmin
 	 * @return Recibo
 	 * */
	public Recibo generarReciboCuotaCdM (DeudaAdmin deuda) throws Exception {
		
		Recibo recibo = new Recibo();
		
		recibo.setCuenta(this);
		recibo.setRecurso(deuda.getRecurso());
		recibo.setAnioRecibo(new Integer(0));
		recibo.setCodRefPag(deuda.getCodRefPag());
    	recibo.setDesActualizacion(deuda.getActualizacion());
    	// TODO: aqui se deberia sacar el servicio banco de la deuda, pero en la migracion viene nula
    	recibo.setServicioBanco(deuda.getSistema().getServicioBanco()==null ? new ServicioBanco() : deuda.getSistema().getServicioBanco()); 
       	recibo.setViaDeuda(deuda.getViaDeuda());
    	recibo.setNroRecibo(0L);
    	recibo.setFechaVencimiento(deuda.getFechaVencimiento());
    
    	Long anio = deuda.getAnio();
    	Long periodo = deuda.getPeriodo();
    	
    	// Si es deuda migrada
    	if (!StringUtil.isNullOrEmpty(deuda.getStrEstadoDeuda())) {
    		String[] periodoAnio = deuda.getStrEstadoDeuda().split("/");
    		periodo = Long.parseLong(periodoAnio[0]);
    		anio = Long.parseLong(periodoAnio[1]);
    	}
    		
    	recibo.setAnio(anio);
    	recibo.setPeriodo(periodo);
    	recibo.setIdSistema(deuda.getSistema().getId());
		
    	ReciboDeuda reciboDeuda = new ReciboDeuda();
		reciboDeuda.setIdDeuda(deuda.getId());
		
		reciboDeuda.setCapitalOriginal(deuda.getSaldo());
		reciboDeuda.setActualizacion(new Double(0));
		reciboDeuda.setPeriodoDeuda(deuda.getStrPeriodo());
		reciboDeuda.setConceptos(deuda.getStrConceptosProp());
		reciboDeuda.setTotCapital(deuda.getSaldo());
		reciboDeuda.setTotActualizacion(new Double(0));
		reciboDeuda.setTotalReciboDeuda(deuda.getSaldo());

		recibo.getListReciboDeuda().add(reciboDeuda);
		
		recibo.setTotCapitalOriginal(reciboDeuda.getCapitalOriginal());
		recibo.setDesCapitalOriginal(reciboDeuda.getDesCapitalOriginal());
		recibo.setTotActualizacion(reciboDeuda.getActualizacion());
		recibo.setDesActualizacion(reciboDeuda.getDesActualizacion());
	
		recibo.setEsReimpresionDeuda(true);
		
		recibo.recalcularValores();
		
    	//Seteamos datos especificos a CdM
    	this.cargarDatosReciboCdM(recibo);
    	
    	return recibo;
	}
	
	/**
	 * Carga los datos especificos para un recibo de cuenta de Contribucion de Mejoras.
	 *
	 */
	public void cargarDatosReciboCdM(Recibo recibo) throws Exception {
		
    	PlaCuaDet plaCuaDet = PlaCuaDet.getByCuentaCdM(this);
    	Obra obra = plaCuaDet.getPlanillaCuadra().getObra();
    	TipoObra tipoObra = plaCuaDet.getPlanillaCuadra().getTipoObra();
    	
    	//Seteamos el capital Original de la Deuda
    	recibo.setDesCapitalOriginal(plaCuaDet.getImporteTotal());
    	recibo.setDatosReciboCdM(new DatosReciboCdM());
    	recibo.getDatosReciboCdM().setNumeroCuadra(plaCuaDet.getPlanillaCuadra().getNumeroCuadra());
    	recibo.getDatosReciboCdM().setNumeroCuentaTGI(StringUtil.completarCerosIzq
    			(plaCuaDet.getCuentaTGI().getNumeroCuenta(), 10));
    	recibo.getDatosReciboCdM().setNumeroObra(obra.getNumeroObra());
    	recibo.getDatosReciboCdM().setDesObra(obra.getDesObra());
    	recibo.getDatosReciboCdM().setDesTipoObra(tipoObra.getDesTipoObra().toUpperCase());
    	recibo.getDatosReciboCdM().setCuota(recibo.getPeriodo());
    	recibo.getDatosReciboCdM().setCantCuotas(recibo.getAnio());    	    	
    	
      	//Seteamos los datos dependientes del computo del tributo
    	recibo.getDatosReciboCdM().setCantMetrosFrente(null);
    	recibo.getDatosReciboCdM().setCantUT(null);
    	recibo.getDatosReciboCdM().setValuacion(null);
    	
    	//Si no es por valuacion, seteamos Mt. de Frente y UT
    	if (!obra.getEsPorValuacion().equals(SiNo.SI.getId())) {
    		recibo.getDatosReciboCdM().setCantMetrosFrente(plaCuaDet.getCantidadMetros());
    		recibo.getDatosReciboCdM().setCantUT(plaCuaDet.getCantidadUnidades());
    	}
    	
    	//Si es por valuacion, seteamos la valuacion del terreno
    	if (obra.getEsPorValuacion().equals(SiNo.SI.getId())) {
    		recibo.getDatosReciboCdM().setValuacion(plaCuaDet.getValuacionTerreno());
    	}

    	//Seteamos las leyenda de la Obra
    	recibo.getDatosReciboCdM().setLeyenda(null);

       	// Si es la primera Cuota del plan contado o el plan largo
    	if (recibo.getPeriodo().equals(1L)) {

    		//Si es el plan contado
    		if (recibo.getAnio().equals(1L)) {
    			recibo.getDatosReciboCdM().setLeyenda(obra.getLeyCon());
    			recibo.getDatosReciboCdM().setDesObraFormaPago(
    					obra.getObraFormaPagoContado().getDesFormaPago());
    		}
    		
    		//Si es el plan largo
    		if (!recibo.getAnio().equals(1L)) 
    			recibo.getDatosReciboCdM().setLeyenda(obra.getLeyPriCuo());
    	}
    	
      	// Si no es la primera Cuota del plan largo
    	if (!recibo.getPeriodo().equals(1L)) {
    		recibo.getDatosReciboCdM().setLeyenda(obra.getLeyResCuo());
    	} 

    	
       	if (plaCuaDet.getObrForPag() != null) {
    		recibo.getDatosReciboCdM().setDescuento(plaCuaDet.getObrForPag().getDescuento());
    		recibo.getDatosReciboCdM().setDesObraFormaPago(plaCuaDet.getObrForPag().getDesFormaPago());
       	}
    	
    	
	}
	
	
	
	/**
	 * Devuelve un objeto DatosReciboCdM con los datos extras, en caso de que se trate de una cuenta de CdM.
	 * 
	 * @return DatosReciboCdM
	 * @throws Exception
	 */
	public DatosReciboCdM cargarDatosCdM() throws Exception {
		
		DatosReciboCdM datosCdM = null;
		
		
		if (this.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdG) || 
				this.getRecurso().getCodRecurso().equals(Recurso.COD_RECURSO_OdP)){ 
		
	    	PlaCuaDet plaCuaDet = PlaCuaDet.getByCuentaCdM(this);
	    	Obra obra = plaCuaDet.getPlanillaCuadra().getObra();
	    	TipoObra tipoObra = plaCuaDet.getPlanillaCuadra().getTipoObra();

	    	datosCdM = new DatosReciboCdM();
	    	
	    	datosCdM.setNumeroObra(obra.getNumeroObra());
		}
    	
    	return datosCdM;
	}
	
	
	public String getTipoAdhe() throws Exception{
		return PadDAOFactory.getDebitoAutJDBCDAO().getTipoAdhe( this.getNumeroCuenta());
	}
	
	 /**	
	  * Retorna la deuda administrativa del Plan Contado de 
	  * Contribucion de Mejoras asociada a la cuenta 
	  *
	  * @return DeudaAdmin
	  */
	public DeudaAdmin getDeudaAdminPlanContadoCdM() {
		
		if (!ListUtil.isNullOrEmpty(this.getListDeudaAdminRaw())){
			for (DeudaAdmin deuda: this.getListDeudaAdminRaw()) {
				if (deuda.getAnio().equals(1L)) {
					return deuda;
				}
			}
		}
	
		return (DeudaAdmin) GdeDAOFactory.getDeudaAdminDAO().getDeudaAdminPlanContadoCdMByCuenta(this);
	}
	
	
	
	/**
	 * Devuelve una lista de deuda administrativa vencida a la fecha dada. 
	 * 
	 * @author Cristian
	 * @param fechaVencimiento
	 * @return
	 * @throws Exception
	 */
	public List<DeudaAdmin> getListDeudaAdminVencidaByCuenta(Date fechaVencimiento) throws Exception {

		return GdeDAOFactory.getDeudaAdminDAO().getListDeudaVencidaByIdCuentaIdRecursoFecha(
				this.getId(), 
				this.getRecurso().getId(),
				fechaVencimiento);
	}
	
	
	/**
	 * Devuelve una lista de deuda administrativa vencida a la fecha dada. 
	 * 
	 * @author Cristian
	 * @param fechaVencimiento
	 * @return
	 * @throws Exception
	 */
	public List<DeudaJudicial> getListDeudaJudicialVencidaByCuenta(Date fechaVencimiento) throws Exception {

		return GdeDAOFactory.getDeudaJudicialDAO().getListDeudaVencidaByIdCuentaIdRecursoFecha(
				this.getId(), 
				this.getRecurso().getId(),
				fechaVencimiento);
	}


	/**
	 * <p><b>(Version del metodo para la Migraci'on. Sin validaciones en la creacion de la Cuenta)</b></p>
	 * Crea una cuenta secundaria a partir de esta cuenta.
	 * La cuenta secundaria se crea copiando el mismo domicilioEnvio y los titulares Activos de esta cuenta.
	 * La nueva cuenta secundaria tendra el recurso, nroCuenta y codGesCue de los parametros.
	 */
	public Cuenta createCuentaSecundariaForMigrania(Recurso recurso, String codGesCue, String numeroCuenta) throws Exception {
		Cuenta cuentaSecundaria = new Cuenta();
		
		// Se copian los atributos de la cuenta Primaria
		// y los pasados como parametros
		cuentaSecundaria.setRecurso(recurso);
		cuentaSecundaria.setObjImp(this.getObjImp());
		cuentaSecundaria.setNumeroCuenta(StringUtil.formatNumeroCuenta(numeroCuenta));
		cuentaSecundaria.setCodGesCue(codGesCue);
		cuentaSecundaria.setDomicilioEnvio(this.getDomicilioEnvio());
		cuentaSecundaria.setBroche(this.getBroche());
		cuentaSecundaria.setFechaAlta(this.getFechaAlta());
		cuentaSecundaria.setFechaBaja(null);
		cuentaSecundaria.setDesDomEnv(this.getDesDomEnv());
		cuentaSecundaria.setNomTitPri(this.getNomTitPri());
		cuentaSecundaria.setCuitTitPri(this.getCuitTitPri());
		
		PadDAOFactory.getCuentaDAO().update(cuentaSecundaria);
		
		if(cuentaSecundaria.hasError()){
			return cuentaSecundaria;
		}
		// Se copian los titulares de la cuenta Primaria
		for (CuentaTitular cuentaTitularPpal: getListCuentaTitular()){
			CuentaTitular cuentaTitularSec = new CuentaTitular();
			
			cuentaTitularSec.setCuenta(cuentaSecundaria);
			cuentaTitularSec.setContribuyente(cuentaTitularPpal.getContribuyente());
			cuentaTitularSec.setTipoTitular(cuentaTitularPpal.getTipoTitular());
			cuentaTitularSec.setEsTitularPrincipal(cuentaTitularPpal.getEsTitularPrincipal()); 
			cuentaTitularSec.setFechaNovedad(cuentaTitularPpal.getFechaNovedad());
			cuentaTitularSec.setFechaDesde(cuentaTitularPpal.getFechaDesde());
			cuentaTitularSec.setFechaHasta(null);
			cuentaTitularSec.setEsAltaManual(SiNo.NO.getId());
			cuentaTitularSec.setIdCaso(null);

			PadDAOFactory.getCuentaTitularDAO().update(cuentaTitularSec);
		
			if(cuentaTitularSec.hasError()){
				cuentaTitularSec.passErrorMessages(cuentaSecundaria);
				return cuentaSecundaria;
			}
		}
		
		return cuentaSecundaria;
	}
	
	/**
     * Calcula el Proximo Codigo de Gestion Personal
     * 
     * @author Tecso
     * @return
     */
    public Long obtenerProxCodGesCue(){
    	return new Long("" + PadDAOFactory.getCuentaDAO().getNextVal(SEQUENCE_COD_GES_PER));
    }
	
    /**
     * Obtiene el Proximo numero de cuenta.
     * 
     * @return long
     */
    public String obtenerProxNumeroCuenta() throws Exception {    	
    	Long lastNroCta = PadDAOFactory.getCuentaDAO().getLastNumeroCuenta(this.getRecurso().getId());
    	String nextNroCta = Long.valueOf(lastNroCta + 1L).toString(); 
    	
    	return nextNroCta;
    }
    
	@Override
	public String infoString() {
		String ret= "Cuenta ";
		
		if(recurso!=null){
			ret +=" - del recurso: "+recurso.getDesRecurso();
		}
		
		if(fechaAlta!=null){
			ret += " - con fecha de Alta: " + DateUtil.formatDate(getFechaAlta(), DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(fechaBaja!=null){
			ret += " - con fecha de Baja: " + DateUtil.formatDate(getFechaBaja(), DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(objImp!=null){
			ret += " - para el Objeto Imponible: "+objImp.getClaveFuncional();
		}
		
		if(estObjImp!=null){
			ret +=" - estado del Objeto Imponible: "+estObjImp.getDesEstObjImp();
		}
		if(numeroCuenta!=null){
			ret += " - con Numero de Cuenta: "+numeroCuenta;
		}

		if(codGesCue!=null){
			ret +=" - con Codigo de Gestion Personal: "+codGesCue;
		}
		
		if(desDomEnv!=null){
			ret += " - con Domicilio de Envio: "+desDomEnv;
		}
		
		if(nomTitPri!=null){
			ret +=" - con Titular Principal: "+nomTitPri;
		}
	
		if(cuitTitPri!=null){
			ret +=" - con Cuit de Titular Principal: "+cuitTitPri;
		}
		
		return ret;
	}

	/**
	 * Metodo usado en Cuentas CdM. Cambia el estado de la cuenta a cancelado.
	 * 
	 */
	public void cancelar() {
		this.setEstCue(EstCue.getById(EstCue.ID_CANCELADO));
		PadDAOFactory.getCuentaDAO().update(this);
	}

	 
	/**  
     *  Cambia el Domicilio de Envio de la cuenta
     *  (creado para cambiar Domicilio de Envio desde novedades.)
     *  
     *  @param domicilio
     */
    public void cambiarDomicilioEnvio (Domicilio domicilioEnvio) throws Exception {
    	//seteo la cuenta y el domicilio de envio
		CamDomWeb camDomWeb = new CamDomWeb();
    	camDomWeb.setCuenta(this);
		camDomWeb.setDomVie(this.getDomicilioEnvio());
		camDomWeb.setDomNue(domicilioEnvio);
		camDomWeb.setEsOrigenWeb(SiNo.NO.getId());
		
    	// creo el historica de cambio de domicilio
    	PadDAOFactory.getCamDomWebDAO().update(camDomWeb);

    	// seteo el nuevo domicilio a esta cuenta
    	this.setDomicilioEnvio(domicilioEnvio);
		this.setDesDomEnv(domicilioEnvio.getViewDomicilio());
		this.updateCatDomEnvio();
    	
		PadDAOFactory.getCuentaDAO().update(this);
		
		// seteo el broche a esta cuenta
    	this.updateBroche(camDomWeb);
    	
//    	
//    	Zona zona = null;
//    	//String domicilioEnvioObjImp = ""; //Domicilio de envio original del objimp. 
//    	                                  	//Al momento de crear la parcela en catastro
//    										//domicilio finca y domicilio envio son iguales
//    										//La diferencia es que dom finca esta en formato para los humanos
//    										// y dom envio esta encodeado segun los tipos de SIAT
//    	
//    	boolean isRosario = domicilioEnvio.getLocalidad().isRosario();
//    	
//    	/*
//    	 * - Si es rosario, 
//    	 *     - verifica si tiene objeto imponible, para obtener la zona
//    	 *     - si no pudo determinar zona, envia solicitud asignar broche no se pudo determinar zona
//    	 *     - si el nuevo domicilio es exactamente igual al domicilio envio informado en objimp, quita broche
//    	 *     - sino con la zona y el domicilio trata de obtener el broche del repartidor correspondiente
//    	 *     - si tiene repartidor, asigna el broche de repartidor fuera de zona a la cuenta
//    	 *     - si no envia solicitud de asignar broche con datos nuevo cambio domiclio
//    	 * - Si no es rosario,
//    	 *     - Envia solicitud asignar broche con datos dom fuera de rosario
//    	 *   
//    	 * - Graba la operacion auditable para varios_web
//    	 */
//    	if (isRosario) {
//    		if (this.getObjImp() != null && this.getObjImp().getTipObjImp().isParcela()) {
//    			ObjImp objImp = getObjImp();
//    			String catastral = objImp.getClaveFuncional();
//    			String idseccion = StringUtil.substringHasta(catastral,'/');
//    			Seccion seccion = Seccion.getById(new Long(idseccion));
//    			zona = seccion.getZona();
//    			//domicilioEnvioObjImp = objImp.getDefinitionValue().getTipObjImpAtrDefinitionByCodigo("DomicilioEnvio").getValorString();    		
//    		}
//
//    		// comparamos los string de domicilio envio original del objeto imponible 
//    		// contra el de que cargaron recien. Si son iguales, quitamos el broche
//    		// sino hacemos el analisis para saber a que repartidor asignar en caso
//    		// que sea posible.
//    		//if (domicilioEnvioObjImp.equals(domicilioEnvio.toStringAtr())) {
//    		//	this.quitarBroche(new Date());
//    		//} else {
//    			if (zona != null) {
//    				// obtengo el broche que corresponda
//    				Broche broche = PadDistribucionManager.getInstance().obtenerRepartidorFueraDeZona
//    				(domicilioEnvio, zona);
//
//    				// si se encuentra el broche se asigna a la cuenta
//    				if (broche != null) {
//    					this.asignarBroche(broche, new Date(), null);
//    				} else { // si no encunentra broche crea una solicitud
//    					String descripcion = "Nro cuenta: " +  this.getNumeroCuenta() + ", Zona parcela: " + zona.getDescripcion() + 
//    						", Nuevo domicilio: " + domicilioEnvio.getViewDomicilio(); 
//
//    					CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_ASIGNACION_BROCHE_CUENTA, 
//    							"No se encontro broche para asignar a la cuenta", descripcion, this);
//    				}
//    			} else {
//    	    		String descripcion = "Nro cuenta: " +  this.getNumeroCuenta() + ", Nuevo domicilio: " + domicilioEnvio.getViewDomicilio(); 
//    	    		CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_ASIGNACION_BROCHE_CUENTA, 
//    	    				"Cambio Domicilio, No se pudo determinar Zona de domicilio", descripcion, this);    				
//    			}
//    		//}
//    	} else { //no es rosario
//      		// Si no es Rosario se le asigna el broche 900
//			this.setBroche(Broche.getById(Broche.ID_BROCHE_FUERA_DE_ROSARIO));
//    	}
    }

    /**
     * Devuelve la descripcion de la localidad con el formato: nombreLocalidad (codPostal)
     * @return una cadena vacia si no tiene domicilio de envio y/o localidad
     * @throws Exception
     */
	public String getStrLocalidad() throws Exception{
    	if (this.getDomicilioEnvio() != null) {
    		if (this.getDomicilioEnvio().getLocalidad() != null) {
    			Localidad localidad = Localidad.getByCodPostSubPost(
    					this.getDomicilioEnvio().getLocalidad().getCodPostal(), 
    					this.getDomicilioEnvio().getLocalidad().getCodSubPostal());
    			
    			if (localidad != null)
    				return localidad.getDescripcionPostal() + " ("+ localidad.getCodPostal() +")";
    		}
    	}
    	return "";
	}
	
	public Obra getObra() throws Exception {
		if (this.recurso.getCategoria().getId().longValue()==Categoria.ID_CDM.longValue()){
			return RecDAOFactory.getObraDAO().getByIdCuenta(this.getId());
		}
		
		return null;
	}
	
	public RecAtrCueV getRegimenDreiVigente(Date fecha) throws Exception{
		
		RecAtrCueV recAtrCueV =  PadDAOFactory.getRecAtrCueVDAO().getRegimenVigente(getId(), fecha);
		
		return recAtrCueV;
	}
	
	/*
	public RecAtrCueV getValorEmisionVigente(Date fecha) throws Exception{
		
		RecAtrCueV recAtrCueV =  PadDAOFactory.getRecAtrCueVDAO().getValorEmisionVigente(getId(), fecha);
		
		return recAtrCueV;
	}
	*/
	
	
	public RecAtrCueV getValorCumurVigente(Date fecha) throws Exception{
		RecAtrCueV recAtrCueV =  PadDAOFactory.getRecAtrCueVDAO().getValorCumurVigente(getId(), fecha);
		return recAtrCueV;
	}
	
	
	public List<DeudaAdmin> getDeudaForCierreComercio(){
		return GdeDAOFactory.getDeudaAdminDAO().getDeudaForCierreComercio(this);
	}
	
    public RecAtrCueV getValorCategoriaRSVigente(Date fecha) throws Exception{
		RecAtrCueV recAtrCueV =  PadDAOFactory.getRecAtrCueVDAO().getValorCategoriaRSVigente(getId(), fecha);
		return recAtrCueV;
    }
    
    
    public RecAtrCueV getValorPerIniRSVigente(Date fecha) throws Exception {
		RecAtrCueV recAtrCueV =  PadDAOFactory.getRecAtrCueVDAO().getValorPerIniRSVigente(getId(), fecha);
		return recAtrCueV;
    }
    
    
    public String getDetalleFiltrosLiqDeuda(){
    	String detalle ="";
    	if(this.getLiqCuentaFilter()!=null){
    		boolean coma=false;
    		if(liqCuentaFilter.getEstadoPeriodo().getEsInpago()){
    			detalle += "Per\u00EDodos Determinados Impagos";
    			coma=true;
    		}
    		
    		if(liqCuentaFilter.getEstadoPeriodo().getEsPago()){
    			detalle += "Per\u00EDodos Determinados Pagos";
    			coma=true;
    		}
    		
    		if(liqCuentaFilter.getEstadoPeriodo().getEsNoDeterminado()){
    			detalle += "Per\u00EDodos No Determinados";
    			coma=true;
    		}
    		
    		if (!ModelUtil.isNullOrEmpty(liqCuentaFilter.getRecClaDeu())){
    			detalle += (coma)?", ":"";
    			RecClaDeu recClaDeu= RecClaDeu.getById(liqCuentaFilter.getRecClaDeu().getId());
    			detalle += "Clasificaci\u00F3n de Deuda: "+recClaDeu.getDesClaDeu();
    			coma=true;
    		}
    		
    		if(liqCuentaFilter.getFechaVtoDesde()!=null){
    			detalle += (coma)?", ":"";
    			detalle += "Fechas de Vencimiento mayores o iguales al "+liqCuentaFilter.getFechaVtoDesdeView();
    			coma=true;
    		}
    		
    		if(liqCuentaFilter.getFechaVtoHasta()!=null){
    			detalle += (coma)?", ":"";
    			detalle += "Fechas de Vencimiento menores o iguales al "+liqCuentaFilter.getFechaVtoHastaView();
    			coma=true;
    		}
    		
    		if(StringUtil.isNullOrEmpty(detalle)){
    			detalle = "No se aplicaron Filtros";
    		}
    	}
    	
    	return detalle;
    }
    
    public void updateDomicilioEnvioForComercio(String domicilioCodificado) throws Exception{
    	try{
    		Session session = SiatHibernateUtil.currentSession();
    		Domicilio domicilio=Domicilio.valueOf(domicilioCodificado);
    		if (domicilio==null){
    			this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, PadError.DOMICILIO_LABEL);
    		}else{
    			PadDAOFactory.getDomicilioDAO().update(domicilio);
    			session.flush();
    			this.setDomicilioEnvio(domicilio);
    			this.setDesDomEnv(domicilio.getViewDomicilio());
    			this.updateCatDomEnvio();
    			PadDAOFactory.getCuentaDAO().update(this);
    			for (Cuenta cuentaSec : this.getObjImp().getListCuentaSecundariaActiva()){
    				cuentaSec.setDomicilioEnvio(domicilio);
    				cuentaSec.setDesDomEnv(domicilio.getViewDomicilio());
    				cuentaSec.setCatDomEnv(this.getCatDomEnv());
        			PadDAOFactory.getCuentaDAO().update(cuentaSec);
    			}
    		}
    	}catch (Exception e){
    		log.error(e);
    		this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, PadError.DOMICILIO_LABEL);
    		throw new Exception(e);
    	}
    }

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getObservacion() {
		return observacion;
	}
    
	public void setEstCue(EstCue estCue) {
		this.estCue = estCue;
	}

	public EstCue getEstCue() {
		return estCue;
	}

	/**
	 *  Verifica si la cuenta posee deuda vencida en la cantidad de dias indicados y devuelve true o false.
	 * 
	 * @param cantDias
	 * @return
	 */
	public boolean poseeDeudaVencida(int cantDias, Date fecha) throws Exception{
		// Se llama a funcion que agrega dias y se le pasa la cantidad en negativo para restar.
		fecha = DateUtil.addDaysToDate(fecha, (-1)*cantDias);
		
		return GdeDAOFactory.getDeudaAdminDAO().poseeDeudaVencida(this.getId(), fecha);
	}


	/**
	 * Atajo para obtener la catastral.
	 * 
	 * Nota: Se encuentra en esta clase ya que la catasral 
	 * puede ser atributo tanto del objeto imponible como 
	 * de la cuenta.
	 *  
	 */
	public String getCatastral() {
		try {
			Recurso recurso = this.getRecurso();
			// Si es TGI lo obtenemos desde el Objeto Imponible
			if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_TGI)) {
				return this.getObjImp().getClaveFuncional();
			}
			
			// Si es otro recurso lo buscamos en todos lados.
 			GenericDefinition definition = this.getCuentaDefinitionValue(new Date());
			return (String) definition.getValor(Atributo.COD_CATASTRAL);
			
		} catch (Exception e) {
			return null;
		}
	}

	
	/**
	 *   Verifica si la cuenta posee deuda impaga a la fecha pasada y devuelve true o false.
	 * 
	 * @return
	 */
	public boolean poseeDeudaImpaga(Date fecha) throws Exception{		
		return GdeDAOFactory.getDeudaAdminDAO().poseeDeudaImpaga(this.getId(), fecha);
	}
	
	
	/**
	 * Devuelve el atributo de asentamiento valorizado de objetos imponibles
	 * @return
	 */
	public String getAtrAseValObjImp() {
		try {
 			GenericDefinition definition = this.getCuentaDefinitionValue(new Date());
			return (String) definition.getValor(Atributo.COD_ATR_ASE_VAL_DREI);
			
		} catch (Exception e) {
			return null;
		}
	}
	
	/** 
     *  Generar Volante de Pagos de Intereses de Regimen Simplificado para DREI/ETUR
     *  
     *	(Se genera un recibo marcado con: esVolPagIntRS = SI, y con valor de capital igual a la actualizacion del saldo original del periodo RS actualizado a la fecha de pago indicada
     *	por el contribuyente. En el detalle del recibo se asocia el registro de deuda del periodo para el que se genero el volante de pago)
     *    
     * @throws Exception 
     */
    public List<Recibo> generarVolantePagoIntRS(List<Deuda> listDeuda, Date fechaVencimiento, Date fechaActualizacion, Canal canal, Boolean contemplaSellado) throws Exception{
				
    	List<Recibo> listRecibo = new ArrayList<Recibo>();
    	    	    	
    	// Por ahora no se aplica sellado
    	/*Date fechaTope = null;  
    	boolean cobraSellado = false;
    	if (contemplaSellado) {
    		fechaTope = Calendario.obtenerFechaAnteUltimoPeriodoVencido(this.getRecurso(), null, fechaVencimiento); //Ex fechaVto
    	}*/
    	
    	// Solo se permite una unica deuda
    	Deuda deuda = listDeuda.get(0);
    	
    	ViaDeuda viaDeuda = deuda.getViaDeuda();
		 			
		// 	----- GENERAR VOLANTE -------
		
    	// Se crea el Detalle del Recibo: ReciboDeuda relacionado a la deuda original
    	ReciboDeuda reciboDeuda = new ReciboDeuda();
		reciboDeuda.setIdDeuda(deuda.getId());
		reciboDeuda.setCapitalOriginal(deuda.getSaldo());
		reciboDeuda.setPeriodoDeuda(deuda.getStrPeriodo());
		reciboDeuda.setTotCapital(deuda.getSaldo());
		reciboDeuda.setConceptos(deuda.getStrConceptosProp());
		DeudaAct deudaActualizada = deuda.actualizacionSaldo(fechaActualizacion);
		reciboDeuda.setActualizacion(NumberUtil.truncate(deudaActualizada.getRecargo(), SiatParam.DEC_IMPORTE_DB));
		reciboDeuda.setTotalReciboDeuda(NumberUtil.truncate(deudaActualizada.getImporteAct(), SiatParam.DEC_IMPORTE_DB));
		reciboDeuda.setTotActualizacion(NumberUtil.truncate(deudaActualizada.getRecargo(), SiatParam.DEC_IMPORTE_DB));
		
		// Se crea el Recibo y se lo marca como "volante de pago de intereses de RS"
		Recibo recibo = crearRecibo(fechaVencimiento, canal, viaDeuda, null, null, null);
		recibo.setDescuento(null);
		recibo.setEsVolPagIntRS(SiNo.SI.getId());
		recibo.setTotCapitalOriginal(reciboDeuda.getActualizacion());
		recibo.setDesCapitalOriginal(0D);
		recibo.setTotActualizacion(0D);
		recibo.setDesActualizacion(0D);
		recibo.setTotImporteRecibo(recibo.getTotCapitalOriginal());
		
		recibo.getListReciboDeuda().add(reciboDeuda);
		reciboDeuda.setRecibo(recibo);
	
    	// Obtiene el sellado que corresponda. (Por ahora no se pide sellado)
    	/*Sellado s = null;
    	if (cobraSellado ) {
    		log.debug("va a buscar el sellado que corresponde");
    		s = BalDefinicionManager.aplicarSellado(recurso.getId(), Accion.ID_ACCION_RECONFECCIONAR_DEUDA, fechaVencimiento, 1, 1);
			if (s!=null) {
				recibo.setSellado(s);
				double importeSellado = (recibo.getSellado()!=null?recibo.getSellado().getImporteSellado():0);
				recibo.setImporteSellado(importeSellado);
				if (recibo.getTotCapitalOriginal()!=0)
					recibo.setTotImporteRecibo(recibo.getTotCapitalOriginal() + recibo.getTotActualizacion() + importeSellado);
				else
					recibo.setTotImporteRecibo(0D);
    		}
		}*/
		        		
		// Truncamos a dos decimales antes de guardar
		recibo.setTotCapitalOriginal(NumberUtil.truncate(recibo.getTotCapitalOriginal(), SiatParam.DEC_IMPORTE_DB));
		recibo.setDesCapitalOriginal(NumberUtil.truncate(recibo.getDesCapitalOriginal(), SiatParam.DEC_IMPORTE_DB));
		int decimalesActualizacion=SiatParam.DEC_IMPORTE_DB;
		if(recibo.getTotCapitalOriginal().doubleValue()==0)
			decimalesActualizacion=SiatParam.DEC_PORCENTAJE_DB;
		recibo.setTotActualizacion(NumberUtil.truncate(recibo.getTotActualizacion(), decimalesActualizacion));
		recibo.setDesActualizacion(NumberUtil.truncate(recibo.getDesActualizacion(), decimalesActualizacion));
		recibo.setImporteSellado(NumberUtil.truncate(recibo.getImporteSellado(), SiatParam.DEC_IMPORTE_DB));
		recibo.setTotImporteRecibo(NumberUtil.truncate(recibo.getTotImporteRecibo(), SiatParam.DEC_IMPORTE_DB));
				        		
		GdeDAOFactory.getReciboDAO().update(recibo);
	
		// Carga el recibo en una lista para replicar el funcionamiento del reconfeccionar normal
    	listRecibo.add(recibo);

    	return listRecibo;
    }
    
    /**
     * Setea catastral del domicilio a la cuenta con formato 99/999/999
     * 
     */
    public void updateCatDomEnvio() {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	try {
    		// Obtengo el domicilio de envio asociado a la cuenta
           	Domicilio domicilioEnvio = this.getDomicilioEnvio();
           		
       		if (null != domicilioEnvio) {
       			CatastroJDBCDAO catastroJDBCDAO = new CatastroJDBCDAO();
       			
       			Long nroDomEnv = domicilioEnvio.getNumero();
       			// determino si el domicilio de envio corresponde a BIS
       			if (domicilioEnvio.getBis().intValue() == 1) nroDomEnv*=-1;
       			
       			// obtengo secc/manzana/gráfico de la forma 99/999/999
           		String catDomEnv = catastroJDBCDAO.obtainCatastral(domicilioEnvio.getCalle().getId(),nroDomEnv,domicilioEnvio.getLetraCalle());
    				
    			//Si no se encuentra una catastral se genera una solicitud para Administración de Padrón. 
    			if (StringUtil.isNullOrEmpty(catDomEnv)) {
    				
    				String asunto = "Domicilio sin asignación Catastral ";
    				String descripcion = " Nro cuenta: " + this.getNumeroCuenta();
    					   descripcion+= ", Recurso: "+ this.getRecurso().getCodRecurso() +" - "+ this.getRecurso().getDesRecurso();
    					   descripcion+= ", Nuevo domicilio: "+ this.getDesDomEnv();
    					   
    			    if (null != this.getId()) {
    			    	//Caso: Alta de una Cuenta
    			    	CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_DOMICILIO_SIN_CATASTRAL,asunto,descripcion,this);		
    				} else {
    					//Caso: Modificacion de domicilio de cuenta existente
    					CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_DOMICILIO_SIN_CATASTRAL,asunto,descripcion);
					}
    			}
    			this.setCatDomEnv(catDomEnv);       	
       		}		
		} catch (Exception e) {				
			log.error("No pudo obtenerse la catastral del domicilio de envio: ",  e);
		}
    }

	public static Long getIdCuenta(Long idRecurso, String numeroCuenta) throws Exception {
		return PadDAOFactory.getCuentaDAO().getIdCuenta(idRecurso, numeroCuenta);
	}

}