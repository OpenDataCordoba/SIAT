//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.bean;

import java.util.ArrayList;
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

import ar.gov.rosario.siat.afi.buss.dao.AfiDAOFactory;
import ar.gov.rosario.siat.afi.iface.util.AfiError;
import ar.gov.rosario.siat.bal.buss.bean.EnvioOsiris;
import ar.gov.rosario.siat.bal.buss.bean.TranAfip;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.RecConADec;
import ar.gov.rosario.siat.def.buss.bean.RecTipUni;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.bean.AgeRet;
import ar.gov.rosario.siat.gde.buss.bean.DecJur;
import ar.gov.rosario.siat.gde.buss.bean.DecJurDet;
import ar.gov.rosario.siat.gde.buss.bean.DecJurPag;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.OriDecJur;
import ar.gov.rosario.siat.gde.buss.bean.TipDecJur;
import ar.gov.rosario.siat.gde.buss.bean.TipDecJurRec;
import ar.gov.rosario.siat.gde.buss.bean.TipPagDecJur;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UnidadesMedidasAfip;


/**
 * Bean correspondiente a ForDecJur - Datos de cabecera y generales del 
 * Formulario de Declaración Jurada proveniente de AFIP.
 * 
 * @author tecso
 */
@Entity
@Table(name = "afi_fordecjur")
public class ForDecJur extends BaseBO {
	
	private static final long serialVersionUID = 1L;
		
	@Column(name = "observaciones")
	private String observaciones;
	
	@Column(name = "codjurcab")
	private Integer codJurCab;
	
	@Column(name = "nroformulario")
	private Integer nroFormulario;
	
	@Column(name = "impuesto")
	private Long impuesto;
	
	@Column(name = "concepto")
	private Integer concepto;
	
	@Column(name = "cuit")
	private String cuit;
	
	@Column(name = "nroinscripcion")
	private String nroInscripcion;
	
	@Column(name = "periodo")
	private Integer periodo;
	
	@Column(name = "anio")
	private Integer anio ;
	 
	@Column(name = "cuota")
	private Integer cuota;
	
	@Column(name = "codrectif")
	private Integer codRectif;
	
	@Column(name = "hora")
	private String hora;	
	 
	@Column(name = "version")
	private Integer version;
	
	@Column(name = "release")
	private Integer release;
	
	@Column(name = "versioninterna")
	private String versionInterna;
	
	@Column(name = "nroverificador")
	private Long nroVerificador;
	
	@Column(name = "fechavencimiento")
	private Date fechaVencimiento;
	   
	@Column(name = "tipoorg")
	private Integer tipoOrg;
	
	@Column(name = "categoria")
	private Integer categoria;
	
	@Column(name = "nroinsimpiibb")
	private String nroInsImpIIBB;
	
	@Column(name = "fechainsiibb")
	private Date fechaInsIIBB;
	
	@Column(name = "fechabajaiibb")
	private Date fechaBajaIIBB;
	
	@Column(name = "nrotelefono")
	private String nroTelefono;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "otrlocfueciu")
	private Integer otrLocFueCiu;
	
	@Column(name = "concursado")
	private Integer concursado;
	
	@Column(name = "fechaprecon")
	private Date fechaPreCon;
	
	@Column(name = "contribfallido")
	private Integer contribFallido;
	
	@Column(name = "fechadecqui")
	private Date fechaDecQui;
	
	@Column(name = "regimengeneral")
	private Integer regimenGeneral;
	
	@Column(name = "coeficientesf")
	private Double coeficienteSF;

	@Column(name = "regimenespecial")
	private Integer regimenEspecial;
	
	@Column(name = "articulo6")
	private Integer articulo6;
	
	@Column(name = "articulo7")
	private Integer articulo7;
	
	@Column(name = "articulo8")
	private Integer articulo8;
	
	@Column(name = "articulo9")
	private Integer articulo9;
	
	@Column(name = "articulo10")
	private Integer  articulo10;
	
	@Column(name = "articulo11")
	private Integer articulo11;
	
	@Column(name = "articulo12")
	private Integer  articulo12;
	
	@Column(name = "articulo13")
	private Integer  articulo13;
	
	@Column(name = "otrlocfueciuporcon")
	private Integer otrLocFueCiuPorCon;
	
	@Column(name = "coefintercomunal")
	private Double coefIntercomunal;
	
	@Column(name = "otrlocfueprovporcon")
	private Integer otrLocFueProvPorCon;
	
	@Column(name = "fechainsconmul")
	private Date fechaInsConMul;
	
	@Column(name = "fechabajaconmul")
	private Date fechaBajaConMul;	
	
	@Column(name = "fechapresentacion")
	private Date fechaPresentacion;
	
	@Column(name = "derecho")
	private Double derecho;
	
	@Column(name = "totretyper")
	private Double totRetYPer;
	
	@Column(name = "retyperperant")
	private Integer retYPerPerAnt;
	
	@Column(name = "perretyperperant")
	private String perRetYPerPerAnt;
	
	@Column(name = "codrecretperperant")
	private Integer codRecRetPerPerAnt;
	
	@Column(name = "montoretperperant")
	private Double montoRetPerPerAnt;
	
	@Column(name = "afavorcontrib")
	private Double aFavorContrib;
	
	@Column(name = "afavordirmun")
	private Double aFavorDirMun;
	
	@Column(name = "fecvenliqmen")
	private Date fecVenLiqMen;
	
	@Column(name = "fecpagpre")
	private Date fecPagPre;
	
	@Column(name = "tasainteres")
	private Double tasaInteres;
	
	@Column(name = "recargointeres")
	private Double recargoInteres;
	
	@Column(name = "derechoadeuda")
	private Double derechoAdeuda;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="idrecurso") 
	private Recurso recurso;	
	
	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="idenvioosiris") 
	private EnvioOsiris envioOsiris;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="idtranafip") 
	private TranAfip tranAfip;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="idestfordecjur") 
	private EstForDecJur estForDecJur;
	
	@OneToMany(mappedBy="forDecJur")
	@JoinColumn(name="idfordecjur")
	private List<Socio> listSocio;
	
	@OneToMany(mappedBy="forDecJur")
	@JoinColumn(name="idfordecjur")
	private List<RetYPer> listRetYPer;	

	@OneToMany(mappedBy="forDecJur")
	@JoinColumn(name="idfordecjur")
	private List<TotDerYAccDJ> listTotDerYAccDJ;
	
	@OneToMany(mappedBy="forDecJur")
	@JoinColumn(name="idfordecjur")
	@OrderBy("numeroCuenta")
	private List<Local> listLocal;
	
	@OneToMany(mappedBy="forDecJur")
	@JoinColumn(name="idfordecjur")
	private List<DatosDomicilio> listDatosDomicilio;		

	@OneToMany(mappedBy="forDecJur")
	@JoinColumn(name="idFordecjur")
	private List<DecJur> listDecJur;
	
	// Mapas Auxiliares usados para la generacion a partir de las trasnacciones afip.
	@Transient
	private Map<String, Local> mapLocal = null;
	@Transient
	private Map<String, ActLoc> mapActLoc = null;
	@Transient
	private Map<String, DecActLoc> mapDecActLoc = null;	
	
	// Constructores
	public ForDecJur(){
		super();
		// Seteo de valores default			
	}
	
	public ForDecJur(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ForDecJur getById(Long id) {
		return (ForDecJur) AfiDAOFactory.getForDecJurDAO().getById(id);
	}
	
	public static ForDecJur getByIdNull(Long id) {
		return (ForDecJur) AfiDAOFactory.getForDecJurDAO().getByIdNull(id);
	}
	
	public static List<ForDecJur> getList() {
		return (ArrayList<ForDecJur>) AfiDAOFactory.getForDecJurDAO().getList();
	}
	
	public static List<ForDecJur> getListActivos() {			
		return (ArrayList<ForDecJur>) AfiDAOFactory.getForDecJurDAO().getListActiva();
	}
	
	// Getters y setters
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Integer getCodJurCab() {
		return codJurCab;
	}

	public void setCodJurCab(Integer codJurCab) {
		this.codJurCab = codJurCab;
	}

	public Integer getNroFormulario() {
		return nroFormulario;
	}

	public void setNroFormulario(Integer nroFormulario) {
		this.nroFormulario = nroFormulario;
	}

	public Long getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(Long impuesto) {
		this.impuesto = impuesto;
	}

	public Integer getConcepto() {
		return concepto;
	}

	public void setConcepto(Integer concepto) {
		this.concepto = concepto;
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getNroInscripcion() {
		return nroInscripcion;
	}

	public void setNroInscripcion(String nroInscripcion) {
		this.nroInscripcion = nroInscripcion;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Integer getCuota() {
		return cuota;
	}

	public void setCuota(Integer cuota) {
		this.cuota = cuota;
	}

	public Integer getCodRectif() {
		return codRectif;
	}

	public void setCodRectif(Integer codRectif) {
		this.codRectif = codRectif;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getRelease() {
		return release;
	}

	public void setRelease(Integer release) {
		this.release = release;
	}

	public String getVersionInterna() {
		return versionInterna;
	}

	public void setVersionInterna(String versionInterna) {
		this.versionInterna = versionInterna;
	}

	public Long getNroVerificador() {
		return nroVerificador;
	}

	public void setNroVerificador(Long nroVerificador) {
		this.nroVerificador = nroVerificador;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Integer getTipoOrg() {
		return tipoOrg;
	}

	public void setTipoOrg(Integer tipoOrg) {
		this.tipoOrg = tipoOrg;
	}

	public Integer getCategoria() {
		return categoria;
	}

	public void setCategoria(Integer categoria) {
		this.categoria = categoria;
	}

	public String getNroInsImpIIBB() {
		return nroInsImpIIBB;
	}

	public void setNroInsImpIIBB(String nroInsImpIIBB) {
		this.nroInsImpIIBB = nroInsImpIIBB;
	}

	public Date getFechaInsIIBB() {
		return fechaInsIIBB;
	}

	public void setFechaInsIIBB(Date fechaInsIIBB) {
		this.fechaInsIIBB = fechaInsIIBB;
	}

	public Date getFechaBajaIIBB() {
		return fechaBajaIIBB;
	}

	public void setFechaBajaIIBB(Date fechaBajaIIBB) {
		this.fechaBajaIIBB = fechaBajaIIBB;
	}

	public String getNroTelefono() {
		return nroTelefono;
	}

	public void setNroTelefono(String nroTelefono) {
		this.nroTelefono = nroTelefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getOtrLocFueCiu() {
		return otrLocFueCiu;
	}

	public void setOtrLocFueCiu(Integer otrLocFueCiu) {
		this.otrLocFueCiu = otrLocFueCiu;
	}

	public Integer getConcursado() {
		return concursado;
	}

	public void setConcursado(Integer concursado) {
		this.concursado = concursado;
	}

	public Date getFechaPreCon() {
		return fechaPreCon;
	}

	public void setFechaPreCon(Date fechaPreCon) {
		this.fechaPreCon = fechaPreCon;
	}

	public Integer getContribFallido() {
		return contribFallido;
	}

	public void setContribFallido(Integer contribFallido) {
		this.contribFallido = contribFallido;
	}

	public Date getFechaDecQui() {
		return fechaDecQui;
	}

	public void setFechaDecQui(Date fechaDecQui) {
		this.fechaDecQui = fechaDecQui;
	}

	public Integer getRegimenGeneral() {
		return regimenGeneral;
	}

	public void setRegimenGeneral(Integer regimenGeneral) {
		this.regimenGeneral = regimenGeneral;
	}

	public Double getCoeficienteSF() {
		return coeficienteSF;
	}

	public void setCoeficienteSF(Double coeficienteSF) {
		this.coeficienteSF = coeficienteSF;
	}

	public Integer getRegimenEspecial() {
		return regimenEspecial;
	}

	public void setRegimenEspecial(Integer regimenEspecial) {
		this.regimenEspecial = regimenEspecial;
	}

	public Integer getArticulo6() {
		return articulo6;
	}

	public void setArticulo6(Integer articulo6) {
		this.articulo6 = articulo6;
	}

	public Integer getArticulo7() {
		return articulo7;
	}

	public void setArticulo7(Integer articulo7) {
		this.articulo7 = articulo7;
	}

	public Integer getArticulo8() {
		return articulo8;
	}

	public void setArticulo8(Integer articulo8) {
		this.articulo8 = articulo8;
	}

	public Integer getArticulo9() {
		return articulo9;
	}

	public void setArticulo9(Integer articulo9) {
		this.articulo9 = articulo9;
	}

	public Integer getArticulo10() {
		return articulo10;
	}

	public void setArticulo10(Integer articulo10) {
		this.articulo10 = articulo10;
	}

	public Integer getArticulo11() {
		return articulo11;
	}

	public void setArticulo11(Integer articulo11) {
		this.articulo11 = articulo11;
	}

	public Integer getArticulo12() {
		return articulo12;
	}

	public void setArticulo12(Integer articulo12) {
		this.articulo12 = articulo12;
	}

	public Integer getArticulo13() {
		return articulo13;
	}

	public void setArticulo13(Integer articulo13) {
		this.articulo13 = articulo13;
	}

	public Integer getOtrLocFueCiuPorCon() {
		return otrLocFueCiuPorCon;
	}

	public void setOtrLocFueCiuPorCon(Integer otrLocFueCiuPorCon) {
		this.otrLocFueCiuPorCon = otrLocFueCiuPorCon;
	}

	public Double getCoefIntercomunal() {
		return coefIntercomunal;
	}

	public void setCoefIntercomunal(Double coefIntercomunal) {
		this.coefIntercomunal = coefIntercomunal;
	}

	public Integer getOtrLocFueProvPorCon() {
		return otrLocFueProvPorCon;
	}

	public void setOtrLocFueProvPorCon(Integer otrLocFueProvPorCon) {
		this.otrLocFueProvPorCon = otrLocFueProvPorCon;
	}

	public Date getFechaInsConMul() {
		return fechaInsConMul;
	}

	public void setFechaInsConMul(Date fechaInsConMul) {
		this.fechaInsConMul = fechaInsConMul;
	}

	public Date getFechaBajaConMul() {
		return fechaBajaConMul;
	}

	public void setFechaBajaConMul(Date fechaBajaConMul) {
		this.fechaBajaConMul = fechaBajaConMul;
	}

	public Double getDerecho() {
		return derecho;
	}

	public void setDerecho(Double derecho) {
		this.derecho = derecho;
	}

	public Double getTotRetYPer() {
		return totRetYPer;
	}

	public void setTotRetYPer(Double totRetYPer) {
		this.totRetYPer = totRetYPer;
	}

	public Integer getRetYPerPerAnt() {
		return retYPerPerAnt;
	}

	public void setRetYPerPerAnt(Integer retYPerPerAnt) {
		this.retYPerPerAnt = retYPerPerAnt;
	}

	public String getPerRetYPerPerAnt() {
		return perRetYPerPerAnt;
	}

	public void setPerRetYPerPerAnt(String perRetYPerPerAnt) {
		this.perRetYPerPerAnt = perRetYPerPerAnt;
	}

	public Integer getCodRecRetPerPerAnt() {
		return codRecRetPerPerAnt;
	}

	public void setCodRecRetPerPerAnt(Integer codRecRetPerPerAnt) {
		this.codRecRetPerPerAnt = codRecRetPerPerAnt;
	}

	public Double getMontoRetPerPerAnt() {
		return montoRetPerPerAnt;
	}

	public void setMontoRetPerPerAnt(Double montoRetPerPerAnt) {
		this.montoRetPerPerAnt = montoRetPerPerAnt;
	}

	public Double getAFavorContrib() {
		return aFavorContrib;
	}

	public void setAFavorContrib(Double aFavorContrib) {
		this.aFavorContrib = aFavorContrib;
	}

	public Double getAFavorDirMun() {
		return aFavorDirMun;
	}

	public void setAFavorDirMun(Double aFavorDirMun) {
		this.aFavorDirMun = aFavorDirMun;
	}

	public Date getFecVenLiqMen() {
		return fecVenLiqMen;
	}

	public void setFecVenLiqMen(Date fecVenLiqMen) {
		this.fecVenLiqMen = fecVenLiqMen;
	}

	public Date getFecPagPre() {
		return fecPagPre;
	}

	public void setFecPagPre(Date fecPagPre) {
		this.fecPagPre = fecPagPre;
	}

	public Double getTasaInteres() {
		return tasaInteres;
	}

	public void setTasaInteres(Double tasaInteres) {
		this.tasaInteres = tasaInteres;
	}

	public void setFechaPresentacion(Date fechaPresentacion) {
		this.fechaPresentacion = fechaPresentacion;
	}

	public Date getFechaPresentacion() {
		return fechaPresentacion;
	}

	public Double getRecargoInteres() {
		return recargoInteres;
	}

	public void setRecargoInteres(Double recargoInteres) {
		this.recargoInteres = recargoInteres;
	}

	public Double getDerechoAdeuda() {
		return derechoAdeuda;
	}

	public void setDerechoAdeuda(Double derechoAdeuda) {
		this.derechoAdeuda = derechoAdeuda;
	}

	public List<Socio> getListSocio() {
		return listSocio;
	}

	public void setListSocio(List<Socio> listSocio) {
		this.listSocio = listSocio;
	}

	public List<RetYPer> getListRetYPer() {
		return listRetYPer;
	}

	public void setListRetYPer(List<RetYPer> listRetYPer) {
		this.listRetYPer = listRetYPer;
	}

	public List<TotDerYAccDJ> getListTotDerYAccDJ() {
		return listTotDerYAccDJ;
	}

	public void setListTotDerYAccDJ(List<TotDerYAccDJ> listTotDerechoAcc) {
		this.listTotDerYAccDJ = listTotDerechoAcc;
	}

	public List<Local> getListLocal() {
		return listLocal;
	}

	public void setListLocal(List<Local> listLocal) {
		this.listLocal = listLocal;
	}

	public List<DatosDomicilio> getListDatosDomicilio() {
		return listDatosDomicilio;
	}

	public void setListDatosDomicilio(List<DatosDomicilio> listDatosDomicilio) {
		this.listDatosDomicilio = listDatosDomicilio;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public void setEstForDecJur(EstForDecJur estForDecJur) {
		this.estForDecJur = estForDecJur;
	}

	public EstForDecJur getEstForDecJur() {
		return estForDecJur;
	}

	public EnvioOsiris getEnvioOsiris() {
		return envioOsiris;
	}

	public void setEnvioOsiris(EnvioOsiris envioOsiris) {
		this.envioOsiris = envioOsiris;
	}

	public TranAfip getTranAfip() {
		return tranAfip;
	}

	public void setTranAfip(TranAfip tranAfip) {
		this.tranAfip = tranAfip;
	}
	
	public List<DecJur> getListDecJur() {
		return listDecJur;
	}

	public void setListDecJur(List<DecJur> listDecJur) {
		this.listDecJur = listDecJur;
	}

	public Map<String, ActLoc> getMapActLoc() {
		return mapActLoc;
	}

	public void setMapActLoc(Map<String, ActLoc> mapActLoc) {
		this.mapActLoc = mapActLoc;
	}

	public Map<String, Local> getMapLocal() {
		return mapLocal;
	}

	public void setMapLocal(Map<String, Local> mapLocal) {
		this.mapLocal = mapLocal;
	}
	
	public Map<String, DecActLoc> getMapDecActLoc() {
		return mapDecActLoc;
	}
	
	public void setMapDecActLoc(Map<String, DecActLoc> mapDecActLoc) {
		this.mapDecActLoc = mapDecActLoc;
	}
	
	public String getPeriodoFiscal(){
		return periodo+"/"+anio;
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

	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (null == getRecurso()) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.FORDECJUR_RECURSO);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
//		UniqueMap uniqueMap = new UniqueMap();
//		uniqueMap.addString("cuit");
//		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
//			addRecoverableError(BaseError.MSG_CAMPO_UNICO, AfiError.SOCIO_CUIT);			
//		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el ForDecJur. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		AfiDAOFactory.getForDecJurDAO().update(this);
	}

	/**
	 * Desactiva el ForDecJur. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		AfiDAOFactory.getForDecJurDAO().update(this);
	}
	
	/**
	 * Valida la activacion del ForDecJur
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del ForDecJur
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
		
	//	---> ABM RetYPer
	public RetYPer createRetYPer(RetYPer retYPer) throws Exception {

		// Validaciones de negocio
		if (!retYPer.validateCreate()) {
			return retYPer;
		}

		AfiDAOFactory.getRetYPerDAO().update(retYPer);

		return retYPer;
	}
	
	public RetYPer updateRetYPer(RetYPer retYPer) throws Exception {
		
		// Validaciones de negocio
		if (!retYPer.validateUpdate()) {
			return retYPer;
		}

		AfiDAOFactory.getRetYPerDAO().update(retYPer);
		
		return retYPer;
	}
	
	public RetYPer deleteRetYPer(RetYPer retYPer) throws Exception {
	
		// Validaciones de negocio
		if (!retYPer.validateDelete()) {
			return retYPer;
		}
		
		AfiDAOFactory.getRetYPerDAO().delete(retYPer);
		
		return retYPer;
	}
	
	//	<--- ABM RetYPer

	//	---> ABM TotDerYAccDJ
	public TotDerYAccDJ createTotDerYAccDJ(TotDerYAccDJ totDerYAccDJ) throws Exception {

		// Validaciones de negocio
		if (!totDerYAccDJ.validateCreate()) {
			return totDerYAccDJ;
		}

		AfiDAOFactory.getTotDerYAccDJDAO().update(totDerYAccDJ);

		return totDerYAccDJ;
	}
	
	public TotDerYAccDJ updateTotDerYAccDJ(TotDerYAccDJ totDerYAccDJ) throws Exception {
		
		// Validaciones de negocio
		if (!totDerYAccDJ.validateUpdate()) {
			return totDerYAccDJ;
		}

		AfiDAOFactory.getTotDerYAccDJDAO().update(totDerYAccDJ);
		
		return totDerYAccDJ;
	}
	
	public TotDerYAccDJ deleteTotDerYAccDJ(TotDerYAccDJ totDerYAccDJ) throws Exception {
	
		// Validaciones de negocio
		if (!totDerYAccDJ.validateDelete()) {
			return totDerYAccDJ;
		}
		
		AfiDAOFactory.getTotDerYAccDJDAO().delete(totDerYAccDJ);
		
		return totDerYAccDJ;
	}
	//	<--- ABM TotDerYAccDJ
	
	
	//	---> ABM Socio
	public Socio createSocio(Socio socio) throws Exception {

		// Validaciones de negocio
		if (!socio.validateCreate()) {
			return socio;
		}

		AfiDAOFactory.getSocioDAO().update(socio);

		return socio;
	}
	
	public Socio updateSocio(Socio socio) throws Exception {
		
		// Validaciones de negocio
		if (!socio.validateUpdate()) {
			return socio;
		}

		AfiDAOFactory.getSocioDAO().update(socio);
		
		return socio;
	}
	
	public Socio deleteSocio(Socio socio) throws Exception {
	
		// Validaciones de negocio
		if (!socio.validateDelete()) {
			return socio;
		}
		
		AfiDAOFactory.getSocioDAO().delete(socio);
		
		return socio;
	}
	//	<--- ABM Socio
	
	//	---> ABM DatosDomicilio
	public DatosDomicilio createDatosDomicilio(DatosDomicilio datosDomicilio) throws Exception {

		// Validaciones de negocio
		if (!datosDomicilio.validateCreate()) {
			return datosDomicilio;
		}

		AfiDAOFactory.getDatosDomicilioDAO().update(datosDomicilio);

		return datosDomicilio;
	}
	
	public DatosDomicilio updateDatosDomicilio(DatosDomicilio datosDomicilio) throws Exception {
		
		// Validaciones de negocio
		if (!datosDomicilio.validateUpdate()) {
			return datosDomicilio;
		}

		AfiDAOFactory.getDatosDomicilioDAO().update(datosDomicilio);
		
		return datosDomicilio;
	}
	
	public DatosDomicilio deleteDatosDomicilio(DatosDomicilio datosDomicilio) throws Exception {
	
		// Validaciones de negocio
		if (!datosDomicilio.validateDelete()) {
			return datosDomicilio;
		}
		
		AfiDAOFactory.getDatosDomicilioDAO().delete(datosDomicilio);
		
		return datosDomicilio;
	}
	//	<--- ABM DatosDomicilio
	
	//	---> ABM Local
	public Local createLocal(Local local) throws Exception {

		// Validaciones de negocio
		if (!local.validateCreate()) {
			return local;
		}

		AfiDAOFactory.getLocalDAO().update(local);

		return local;
	}
	
	public Local updateLocal(Local local) throws Exception {
		
		// Validaciones de negocio
		if (!local.validateUpdate()) {
			return local;
		}

		AfiDAOFactory.getLocalDAO().update(local);
		
		return local;
	}
	
	public Local deleteLocal(Local local) throws Exception {
	
		// Validaciones de negocio
		if (!local.validateDelete()) {
			return local;
		}
		
		AfiDAOFactory.getLocalDAO().delete(local);
		
		return local;
	}
	//	<--- ABM Local
	
	
	/**
	 * Genera Declaraciones Juradas para el Formulario de Declaraciones Juradas.
	 * 
	 */
	public List<DecJur> generarDecJur() throws Exception{
		
		// Obtener Datos generales
		Integer anio = this.getAnio();
		Integer periodo = this.getPeriodo();
		Recurso recurso = this.getRecurso();
		Date fechaPresentacion = this.getFechaPresentacion();
		Date fechaNovedad = new Date();
		OriDecJur oriDecJur = OriDecJur.getById(OriDecJur.ID_ENVIO_OSIRIS);
		TipDecJur tipDecJur = null;
		TipDecJurRec tipDecJurRec = null;
		int codRectificativa = this.getCodRectif();
		
		// Determinar el Tipo de Declaracion Jurada (Original o Rectificativa)
		if(codRectificativa > 0){
			tipDecJur = TipDecJur.getById(TipDecJur.ID_RECTIFICATIVA);
		}else{
			tipDecJur = TipDecJur.getById(TipDecJur.ID_ORIGINAL);
		}
		// Determinar Asociacion de Tipo de Declaracion Jurada con Recurso
		tipDecJurRec = TipDecJurRec.getVigenteByRecursoYTipDecJur(fechaPresentacion, recurso, tipDecJur);
		
		// Por cada registro de Retenciones y Percepciones, lo cargamos en un mapa para imputar otros pagos a las decJur que se creen.
		Map<Long, Double> mapaRetYPerAImputar = new HashMap<Long, Double>();
		if(this.getListRetYPer() != null){
			for(RetYPer retYPer: this.getListRetYPer()){
				mapaRetYPerAImputar.put(retYPer.getId(), retYPer.getImporte());
			}			
		}
		// Ademas se toma el valor del resto de retenciones y percepciones de periodos anteriores para imputar en otros pagos a las decJur que se creen.
		Double restoRetYPerPerAnt = 0D; 
		if(this.getRetYPerPerAnt() != null && this.getRetYPerPerAnt().intValue() == SiNo.SI.getId().intValue())
			restoRetYPerPerAnt = this.getMontoRetPerPerAnt();
		
		
		List<Local> listLocal = this.getListLocal();
		//Si no existen locales, retorno null
		if (ListUtil.isNullOrEmpty(listLocal)) {
			return null;
		}

		List<DecJur> listDecJur = new ArrayList<DecJur>();
		//Por cada Local (Cuenta Contributiva) creamos una DecJur
		for(Local local: listLocal){
			
			/*- Mantis #7819: al procesar DJ de Etur, solo generar DecJur e impactar 
			 * deuda de los locales que poseen en '1' el campo 'contrib etur'. 
			 */
			if (Recurso.COD_RECURSO_ETuR.equals(recurso.getCodRecurso())
					&& SiNo.getById(local.getContribEtur()).getEsNO()) {
				continue;
			}

			// Creamos DecJur y seteamos datos generales
			DecJur decJur = new DecJur();
			
			decJur.setRecurso(recurso);
			decJur.setTipDecJurRec(tipDecJurRec);
			decJur.setOriDecJur(oriDecJur);
			decJur.setFechaPresentacion(fechaPresentacion);
			decJur.setFechaNovedad(fechaNovedad);
			decJur.setPeriodo(periodo);
			decJur.setAnio(anio);
			decJur.setCodRectificativa(codRectificativa);
			decJur.setValRefMin(local.getCantPersonal().doubleValue()); 
			decJur.setForDecJur(this);
			decJur.setEstado(Estado.CREADO.getId()); 

			// Si no se encuentra la cuenta generar error 
			if(local.getCuenta() == null){								
				Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(this.getRecurso().getId(), local.getNumeroCuenta());
				local.setCuenta(cuenta);
				if(cuenta == null){			
					String err = "ForDecJur de id: "+this.getId()+". No se encuentra la Cuenta con número ";
						   err+=local.getNumeroCuenta()+" para el Recurso de codigo "+recurso.getCodRecurso();
					addRecoverableValueError(err);
					AdpRun.logRun(err);
					return null;
				}				
				AfiDAOFactory.getLocalDAO().update(local);
			}
			decJur.setCuenta(local.getCuenta());

			// Buscar deuda original declarada
			DeudaAdmin deudaOriginal = (DeudaAdmin) DeudaAdmin.getByCuentaPeriodoAnioParaDJ(decJur.getCuenta(), periodo.longValue(), anio);
			// Si no se encuentra la deuda original cargar error
			if(deudaOriginal == null){
				String err = "ForDecJur de id: "+this.getId()+". No se encuentra la Deuda Original de la cuenta ";
					   err+=decJur.getCuenta().getNumeroCuenta()+" para el Recurso de codigo "+recurso.getCodRecurso()+" periodo "+periodo+"/"+anio;
				addRecoverableValueError(err);
				AdpRun.logRun(err);
				return null;
			}
			decJur.setIdDeuda(deudaOriginal.getId());

			if(Recurso.COD_RECURSO_DReI.equals(this.getRecurso().getCodRecurso())){ 
				decJur.setMinRec(local.getMinimoGeneral()); 	
				decJur.setAliPub(local.getAlicuotaPub());
				decJur.setTotalPublicidad(local.getPublicidad());
				decJur.setAliMesYSil(local.getAlicuotaMesYSil());
				decJur.setTotMesYSil(local.getMesasYSillas());
				decJur.setSubtotal(local.getDerecho()); // Derecho (mayor entre el minimo y el der det)
				decJur.setOtrosPagos(local.getOtrosPagos());
				decJur.setTotalDeclarado(local.getSubTotal1());					
			}else{
				decJur.setMinRec(0D);
				decJur.setAliPub(0D); 
				decJur.setTotalPublicidad(0D);
				decJur.setAliMesYSil(0D);
				decJur.setTotMesYSil(0D);
				decJur.setSubtotal(0D); 
				decJur.setOtrosPagos(local.getOtrosPagos());
				decJur.setTotalDeclarado(local.getOtrosPagos()+local.getDerechoTotal());		
			}

			// Por cada Declaracion de Actividades para el Local creamos una DecJurDet
			if(decJur.getListDecJurDet() == null){
				decJur.setListDecJurDet(new ArrayList<DecJurDet>());
			}
			
			//Lista de Actividades declaradas por Local
			List<DecActLoc> listDecActLoc = local.getListDecActLoc();
			if(!ListUtil.isNullOrEmpty(listDecActLoc)){
				for(DecActLoc decActLoc: local.getListDecActLoc()){
					// Creamos DecJurDet y seteamos datos generales
					DecJurDet decJurDet = new DecJurDet();
					decJurDet.setDecJur(decJur);

					// Obtenemos la Actividad
					RecConADec recConADec =  RecConADec.getByCodConceptoRecurso(recurso.getId(), decActLoc.getCodActividad().toString());
					if (recConADec == null) {
						// Si no la puedo relacionar, intento relacionarla la busqueda por DReI 
						recConADec =  RecConADec.getByCodConceptoRecurso(Recurso.getDReI().getId(), decActLoc.getCodActividad().toString());
						if(recConADec == null){
							//verificar nuevamente y marcar advertencia
							String err="ForDecJur de id: "+this.getId()+". No se encuentra en SIAT la Actividad de código: "+decActLoc.getCodActividad()+" para el Recurso "+recurso.getCodRecurso();
							this.addRecoverableValueError(err);
							AdpRun.logRun(err);
							return null;
						}
					}

					decJurDet.setRecConADec(recConADec);
					decJurDet.setDetalle(recConADec.getDesConcepto());

					decJurDet.setBase(decActLoc.getBaseImpAjustada());
					decJurDet.setMultiplo(decActLoc.getAliCuota());
					decJurDet.setSubtotal1(decActLoc.getDerechoCalculado());
					if(Recurso.COD_RECURSO_DReI.equals(recurso.getCodRecurso())){
						decJurDet.setCanUni(decActLoc.getCantidad());
						// Obtenemos la Unidad de Medida
						RecTipUni recTipUni = RecTipUni.getByCodigoAfip(decActLoc.getUnidadMedida().toString()); 
						decJurDet.setRecTipUni(recTipUni);
						decJurDet.setUnidad(recTipUni==null?null:recTipUni.getNomenclatura());
						// Obtenemos el Tipo de Unidad
						RecConADec tipoUnidad = null;
						if(UnidadesMedidasAfip.M2.getId().intValue() == decActLoc.getUnidadMedida().intValue()){
							Long idRecConADec = null;
							if(decActLoc.getCantidad() <= 40)
								idRecConADec = RecConADec.ID_CIBER1;
							else if(decActLoc.getCantidad() <= 200)
								idRecConADec = RecConADec.ID_CIBER2;
							else 
								idRecConADec = RecConADec.ID_CIBER3;
							tipoUnidad =  RecConADec.getByIdNull(idRecConADec);
						}else if(UnidadesMedidasAfip.M2_UTILES.getId().intValue() == decActLoc.getUnidadMedida().intValue()){
							Long idRecConADec = null;
							if(decActLoc.getCantidad() <= 250)
								idRecConADec = RecConADec.ID_BAILABLE1;
							else if(decActLoc.getCantidad() <= 500)
								idRecConADec = RecConADec.ID_BAILABLE2;
							else if(decActLoc.getCantidad() <= 750)
								idRecConADec = RecConADec.ID_BAILABLE3;
							else if(decActLoc.getCantidad() <= 1000)
								idRecConADec = RecConADec.ID_BAILABLE4;
							else if(decActLoc.getCantidad() <= 2425)
								idRecConADec = RecConADec.ID_BAILABLE5;
							else
								idRecConADec = RecConADec.ID_BAILABLE6;
							tipoUnidad =  RecConADec.getByIdNull(idRecConADec);								
						}else{
							tipoUnidad =  RecConADec.getByCodigoAfip(decActLoc.getTipoUnidad().toString());								
						}
						decJurDet.setTipoUnidad(tipoUnidad);
						decJurDet.setDesTipoUnidad(tipoUnidad==null?null:tipoUnidad.getDesConcepto());
						decJurDet.setValUnidad(decActLoc.getMinimoPorUnidad());
						decJurDet.setSubtotal2(decActLoc.getMinimoCalculado());					
					}else{
						decJurDet.setMinimo(decActLoc.getMinimoCalculado());
						decJurDet.setCanUni(0D);
						decJurDet.setRecTipUni(null);
						decJurDet.setUnidad(null);
						decJurDet.setValUnidad(0D);
						decJurDet.setSubtotal2(0D);
						decJurDet.setAlcanceEtur(decActLoc.getAlcanceEtur());
					}
					decJurDet.setTotalConcepto(decActLoc.getDerechoDet());

					decJur.getListDecJurDet().add(decJurDet);	
					
					// issue 7947: AFIP - Procesar Envío. EtuR Actividad más gravosa
					if(Recurso.COD_RECURSO_ETuR.equals(recurso.getCodRecurso())){
						/* Si el recurso es ETuR, tomo la actividad (DecActLoc) mas gravosa (mayor derechoDet).
						 * Como la lista de actividades viene ordenada por derechoDet, tomo la primera y salgo del bucle.
						 */
						break;
					}
				}
			}

			if(decJur.getListDecJurPag() == null) 
				decJur.setListDecJurPag(new ArrayList<DecJurPag>());

			if (null != local.getComputado() && local.getComputado().doubleValue() != 0D) {
				// Creamos DecJurPag
				DecJurPag decJurPag = new DecJurPag();

				decJurPag.setDecJur(decJur);
				decJurPag.setTipPagDecJur(TipPagDecJur.getById(TipPagDecJur.ID_PAGO_OSIRIS));
				decJurPag.setImporte(local.getComputado());

				decJur.getListDecJurPag().add(decJurPag);	
			}

			Double importeAPagar = local.getDerechoTotal();

			// Por cada registro de Retenciones y Percepciones con saldo para imputar, creamos una DecJurPag del Tipo correspondiente
			for(Long idRetYPer: mapaRetYPerAImputar.keySet()){
				if(importeAPagar <= 0) break;
				Double importeParaImputar = mapaRetYPerAImputar.get(idRetYPer);
				if(importeParaImputar > 0){
					RetYPer retYPer = RetYPer.getById(idRetYPer);

					// Creamos DecJurPag
					DecJurPag decJurPag = new DecJurPag();
					decJurPag.setDecJur(decJur);
					decJurPag.setRetYPer(retYPer);
					decJurPag.setTipPagDecJur(TipPagDecJur.getById(TipPagDecJur.ID_RETENCION_OSIRIS));
					decJurPag.setCertificado(retYPer.getNroConstancia());				
					decJurPag.setFechaPago(retYPer.getFecha());
					decJurPag.setDetalle(retYPer.getDenominacion());
					decJurPag.setCuitAgente(retYPer.getCuitAgente());
					AgeRet ageRet = retYPer.getAgeRet();
					if(ageRet == null){
						ageRet = AgeRet.getByCuitYRecurso(retYPer.getCuitAgente(), recurso.getId()); 
					}
					decJurPag.setAgeRet(ageRet);

					Double importePago = null;
					if((importeAPagar - importeParaImputar) <= 0){
						importePago = importeAPagar;
					}else{
						importePago = importeParaImputar;
					}
					importeAPagar = importeAPagar - importePago;
					decJurPag.setImporte(importePago);

					// Se actualiza el importe de la retencion restante en el mapa
					mapaRetYPerAImputar.put(idRetYPer, importeParaImputar - importePago);

					decJur.getListDecJurPag().add(decJurPag);							
				}
			}
			// Se cargan Otros Pagos por Retenciones y Percepciones de Periodos Anteriores declaradas en el Formulario.
			if(importeAPagar > 0 && restoRetYPerPerAnt > 0){
				// Creamos DecJurPag
				DecJurPag decJurPag = new DecJurPag();
				decJurPag.setDecJur(decJur);
				decJurPag.setRetYPer(null);
				TipPagDecJur tipPagDecJur = TipPagDecJur.getById(TipPagDecJur.ID_RETENCION);
				decJurPag.setTipPagDecJur(tipPagDecJur);
				decJurPag.setCertificado(null);				
				decJurPag.setFechaPago(this.getFechaPresentacion());
				decJurPag.setCuitAgente(null);
				decJurPag.setAgeRet(null);
				decJurPag.setDetalle("Por Resto de Retenciones y Percepciones del Periodo "+this.getPerRetYPerPerAnt());

				Double importePago = null;
				if((importeAPagar - restoRetYPerPerAnt) <= 0){
					importePago = importeAPagar;
				}else{
					importePago = restoRetYPerPerAnt;
				}
				importeAPagar = importeAPagar - importePago;
				decJurPag.setImporte(importePago);

				// Se actualiza el importe de la retencion restante en el mapa
				restoRetYPerPerAnt = restoRetYPerPerAnt /*- importeAPagar*/ - importePago;

				decJur.getListDecJurPag().add(decJurPag);							
			}
			//				}

			// LLamo al update del DAO
			GdeDAOFactory.getDecJurDAO().update(decJur);

			for(DecJurDet decJurDet:decJur.getListDecJurDet()){
				decJurDet.setDecJur(decJur);
				GdeDAOFactory.getDecJurDetDAO().update(decJurDet);
			}

			for(DecJurPag decJurPag:decJur.getListDecJurPag()){
				decJurPag.setDecJur(decJur);
				GdeDAOFactory.getDecJurPagDAO().update(decJurPag);
			}
			listDecJur.add(decJur);
		}

		return listDecJur;
	}

}

	