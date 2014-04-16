//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.bal.buss.bean.DisPar;
import ar.gov.rosario.siat.bal.buss.bean.DisParDet;
import ar.gov.rosario.siat.bal.buss.bean.DisParRec;
import ar.gov.rosario.siat.bal.buss.bean.TipoImporte;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.Solicitud;
import ar.gov.rosario.siat.cas.iface.util.CasError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.CategoriaVO;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.model.RecAtrCueVO;
import ar.gov.rosario.siat.def.iface.model.RecursoDefinition;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.CriAsiPro;
import ar.gov.rosario.siat.gde.buss.bean.PlanRecurso;
import ar.gov.rosario.siat.gde.buss.bean.ProRec;
import ar.gov.rosario.siat.gde.buss.bean.ProcesoMasivo;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Broche;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Repartidor;
import ar.gov.rosario.siat.pad.buss.bean.TipoRepartidor;
import ar.gov.rosario.siat.pad.iface.model.RecAtrCueDefinition;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.rec.buss.bean.Contrato;
import ar.gov.rosario.siat.rec.buss.bean.FormaPago;
import ar.gov.rosario.siat.rec.buss.bean.PlanillaCuadra;
import ar.gov.rosario.siat.rec.buss.bean.TipoObra;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;


/**
 * Bean correspondiente a Recurso
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_recurso")
public class Recurso extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	// IMPORTANTE: estas constantes estan repetidas en RecursoVO
	public static final String COD_RECURSO_TGI = "TGI";
	public static final String COD_RECURSO_OdP = "OdP";
	public static final String COD_RECURSO_OdG = "OdG";
	public static final String COD_RECURSO_PLANOS = "PLANOS";
	
	public static final String COD_RECURSO_GENERICO = "GEN";
	
	public static final String COD_RECURSO_DReI = "DReI";
	public static final String COD_RECURSO_ETuR = "ETuR";
	public static final String COD_RECURSO_DPUB="DERPUB";
	
	public static final String COD_RECURSO_CEM="CEMTASACEM";
	
	public static final String COD_RECURSO_PAGCYQ="PAGCYQ";
	
	
	@Transient
	private Logger log = Logger.getLogger(Recurso.class);
		
	@Column(name = "codRecurso")
	private String codRecurso;
	@Column(name = "desRecurso")
	private String desRecurso;
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idCategoria") 
	private Categoria categoria;
	@Column(name = "esAutoliquidable")
	private Integer esAutoliquidable;
	@Column(name = "fecEsAut")
	private Date fecEsAut;
	@Column(name = "esFiscalizable")
	private Integer esFiscalizable;
	@Column(name = "fecEsFis")
	private Date fecEsFis;
	@Column(name = "esLibreDeuda")
	private Integer esLibreDeuda;
	@Column(name = "fecDesIntDiaBan")
	private Date fecDesIntDiaBan;
	@Column(name = "gesDeuNoVen")
	private Integer gesDeuNoVen;
	@Column(name = "deuExiVen")
	private Integer deuExiVen;
	@Column(name = "gesCobranza")
	private Integer gesCobranza;
	@Column(name = "perEmiDeu")
	private Integer perEmiDeuMas;
	@ManyToOne(fetch=FetchType.LAZY)  
    @JoinColumn(name="idAtrEmiMas")
	private Atributo atrEmiMas;
	@Column(name = "perEmiDeuPuntual")
	private Integer perEmiDeuPuntual;
	@Column(name = "perEmiDeuExt")
	private Integer perEmiDeuExt;
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idVencimiento") 
	private Vencimiento vencimiento;
	@Column(name = "perImpMasDeu")
	private Integer perImpMasDeu;
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idFormImpMasDeu") 
	private Formulario formImpMasDeu;
	@Column(name = "genNotif")
	private Integer genNotif;
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idFormNotif") 
	private Formulario formNotif;
	@Column(name = "esDeudaTitular")
	private Integer esDeudaTitular;
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idTipObjImp") 
	private TipObjImp tipObjImp;
	@Column(name = "esPrincipal")
	private Integer esPrincipal;
	@Column(name = "altaCtaPorIface")
	private Integer altaCtaPorIface;
	@Column(name = "bajaCtaPorIface")
	private Integer bajaCtaPorIface;
	@Column(name = "modiTitCtaPorIface")
	private Integer modiTitCtaPorIface;
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idRecPri") 
	private Recurso recPri;
	@Column(name = "altaCtaPorPri")
	private Integer altaCtaPorPri;
	@Column(name = "bajaCtaPorPri")
	private Integer bajaCtaPorPri;
	@Column(name = "modiTitCtaPorPri")
	private Integer modiTitCtaPorPri;
	@Column(name = "altaCtaManual")
	private Integer altaCtaManual;
	@Column(name = "bajaCtaManual")
	private Integer bajaCtaManual;
	@Column(name = "modiTitCtaManual")
	private Integer modiTitCtaManual;
	@Column(name = "enviaJudicial")
	private Integer enviaJudicial;
	@Column(name = "genPadFir")
	private Integer genPadFir;
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idFormPadFir") 
	private Formulario formPadFir;
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idCriAsiPro") 
	private CriAsiPro criAsiPro;
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idGenCue") 
	private GenCue genCue;
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idGenCodGes") 
	private GenCodGes genCodGes;
	/**
	 * UltPerEmi:
	 * formato del campo: anio(dddd)periodo(dd).
	 */
	@Column(name = "ultPerEmi")
	private String ultPerEmi;
	@Column(name = "fechaAlta")
	private Date fechaAlta;
	@Column(name = "fechaBaja")
	private Date fechaBaja;
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idAtributoAse") 
	private Atributo atributoAse;
	@Column(name="fecUltPag")
	private Date fecUltPag;
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idPeriodoDeuda")
	private PeriodoDeuda periodoDeuda;
		
	
	// Listas de Entidades Relacionadas con Recurso y otra Entidad
	@OneToMany()
	@JoinColumn(name="idRecurso")
	@OrderBy(clause="fechaDesde")
	private List<RecAtrVal> listRecAtrVal;
	@OneToMany()
	@JoinColumn(name="idRecurso")
	@OrderBy(clause="fechaDesde")
	private List<RecCon> listRecCon;
	@OneToMany()
	@JoinColumn(name="idRecurso")
	@OrderBy(clause="fechaDesde")
	private List<RecClaDeu> listRecClaDeu;
	@OneToMany()
	@JoinColumn(name="idRecurso")
	@OrderBy(clause="fechaDesde")
	private List<RecGenCueAtrVa> listRecGenCueAtrVa;
	@OneToMany()
	@JoinColumn(name="idRecurso")
	@OrderBy(clause="fechaDesde")
	private List<RecEmi> listRecEmi;
	@OneToMany()
	@JoinColumn(name="idRecurso")
	@OrderBy(clause="fechaDesde")
	private List<RecAtrCueEmi> listRecAtrCueEmi;
	@OneToMany()
	@JoinColumn(name="idRecurso")
	@OrderBy(clause="fechaDesde")
	private List<RecAtrCue> listRecAtrCue;
	@OneToMany()
	@JoinColumn(name="idRecurso")
	@OrderBy(clause="fechaDesde")
	private List<RecConADec> listRecConADec;
	@OneToMany(mappedBy="recurso")
	@JoinColumn(name="idRecurso")
	private List<TipRecConADec> listTipRecConADec;
	@OneToMany()
	@JoinColumn(name="idRecurso")
	@OrderBy(clause="fechaDesde")
	private List<RecMinDec> listRecMinDec;
	@OneToMany()
	@JoinColumn(name="idRecurso")
	@OrderBy(clause="fechaDesde")
	private List<RecAli> listRecAli;
	
	@OneToMany()
	@JoinColumn(name="idRecurso")
	private List<RecursoArea> listRecursoArea;
	
	@Column(name = "fecultenvpro")
	private Date fecUltEnvPro;
	
	// Constructores
	public Recurso(){
		super();	
	}
	
	public Recurso(Long id){
		super();
		setId(id);
	}
	
	// Getters y setters
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public String getCodRecurso() {
		return codRecurso;
	}
	public void setCodRecurso(String codRecurso) {
		this.codRecurso = codRecurso;
	}
	public String getDesRecurso() {
		return desRecurso;
	}
	public void setDesRecurso(String desRecurso) {
		this.desRecurso = desRecurso;
	}
	public Integer getEsAutoliquidable(){
		return esAutoliquidable;
	}
	public void setEsAutoliquidable(Integer esAutoliquidable){
		this.esAutoliquidable = esAutoliquidable;
	}
	public Date getFecEsAut(){
		return fecEsAut;
	}
	public void setFecEsAut(Date fecEsAut){
		this.fecEsAut = fecEsAut;
	}
	public Integer getEsFiscalizable(){
		return esFiscalizable;
	}
	public void setEsFiscalizable(Integer esFiscalizable){
		this.esFiscalizable = esFiscalizable;
	}
	public Date getFecEsFis(){
		return fecEsFis;
	}
	public void setFecEsFis(Date fecEsFis){
		this.fecEsFis = fecEsFis;
	}
	public Integer getEsLibreDeuda(){
		return esLibreDeuda;
	}
	public void setEsLibreDeuda(Integer esLibreDeuda){
		this.esLibreDeuda = esLibreDeuda;
	}
	public Date getFecDesIntDiaBan(){
		return fecDesIntDiaBan;
	}
	public void setFecDesIntDiaBan(Date fecDesIntDiaBan){
		this.fecDesIntDiaBan = fecDesIntDiaBan;
	}
	public Integer getGesDeuNoVen(){
		return gesDeuNoVen;
	}
	public void setGesDeuNoVen(Integer gesDeuNoVen){
		this.gesDeuNoVen = gesDeuNoVen;
	}
	public Integer getDeuExiVen(){
		return deuExiVen;
	}
	public void setDeuExiVen(Integer deuExiVen){
		this.deuExiVen = deuExiVen;
	}
	public Integer getGesCobranza(){
		return gesCobranza;
	}
	public void setGesCobranza(Integer gesCobranza){
		this.gesCobranza = gesCobranza;
	}
		public Integer getPerEmiDeuMas() {
		return perEmiDeuMas;
	}
	public void setPerEmiDeuMas(Integer perEmiDeuMas) {
		this.perEmiDeuMas = perEmiDeuMas;
	}
	public Integer getPerEmiDeuPuntual() {
		return perEmiDeuPuntual;
	}
	public void setPerEmiDeuPuntual(Integer perEmiDeuPuntual) {
		this.perEmiDeuPuntual = perEmiDeuPuntual;
	}
	public Atributo getAtrEmiMas() {
		return atrEmiMas;
	}
	public void setAtrEmiMas(Atributo atrEmiMas) {
		this.atrEmiMas = atrEmiMas;
	}
	public Integer getPerEmiDeuExt(){
		return perEmiDeuExt;
	}
	public void setPerEmiDeuExt(Integer perEmiDeuExt){
		this.perEmiDeuExt = perEmiDeuExt;
	}
	public Vencimiento getVencimiento() {
		return vencimiento;
	}
	public void setVencimiento(Vencimiento vencimiento) {
		this.vencimiento = vencimiento;
	}
	public Integer getEsDeudaTitular(){
		return esDeudaTitular;
	}
	public void setEsDeudaTitular(Integer esDeudaTitular){
		this.esDeudaTitular = esDeudaTitular;
	}
	public TipObjImp getTipObjImp(){
		return tipObjImp;
	}
	public void setTipObjImp(TipObjImp tipObjImp){
		this.tipObjImp = tipObjImp;
	}
	public Integer getEsPrincipal(){
		return esPrincipal;
	}
	public void setEsPrincipal(Integer esPrincipal){
		this.esPrincipal = esPrincipal;
	}
	public void setGenPadFir(Integer genPadFir) {
		this.genPadFir = genPadFir;
	}
	public Integer getGenPadFir() {
		return genPadFir;
	}
	public Integer getAltaCtaPorIface(){
		return altaCtaPorIface;
	}
	public void setAltaCtaPorIface(Integer altaCtaPorIface){
		this.altaCtaPorIface = altaCtaPorIface;
	}
	public Integer getBajaCtaPorIface(){
		return bajaCtaPorIface;
	}
	public void setBajaCtaPorIface(Integer bajaCtaPorIface){
		this.bajaCtaPorIface = bajaCtaPorIface;
	}
	public Integer getModiTitCtaPorIface(){
		return modiTitCtaPorIface;
	}
	public void setModiTitCtaPorIface(Integer modiTitCtaPorIface){
		this.modiTitCtaPorIface = modiTitCtaPorIface;
	}
	public Recurso getRecPri(){
		return recPri;
	}
	public void setRecPri(Recurso recPri){
		this.recPri = recPri;
	}
	public Integer getAltaCtaPorPri(){
		return altaCtaPorPri;
	}
	public void setAltaCtaPorPri(Integer altaCtaPorPri){
		this.altaCtaPorPri = altaCtaPorPri;
	}
	public Integer getBajaCtaPorPri(){
		return bajaCtaPorPri;
	}
	public void setBajaCtaPorPri(Integer bajaCtaPorPri){
		this.bajaCtaPorPri = bajaCtaPorPri;
	}
	public Integer getModiTitCtaPorPri(){
		return modiTitCtaPorPri;
	}
	public void setModiTitCtaPorPri(Integer modiTitCtaPorPri){
		this.modiTitCtaPorPri = modiTitCtaPorPri;
	}
	public Integer getAltaCtaManual(){
		return altaCtaManual;
	}
	public void setAltaCtaManual(Integer altaCtaManual){
		this.altaCtaManual = altaCtaManual;
	}
	public Integer getBajaCtaManual(){
		return bajaCtaManual;
	}
	public void setBajaCtaManual(Integer bajaCtaManual){
		this.bajaCtaManual = bajaCtaManual;
	}
	public Integer getModiTitCtaManual(){
		return modiTitCtaManual;
	}
	public void setModiTitCtaManual(Integer modiTitCtaManual){
		this.modiTitCtaManual = modiTitCtaManual;
	}
	public Integer getEnviaJudicial(){
		return enviaJudicial;
	}
	public void setEnviaJudicial(Integer enviaJudicial){
		this.enviaJudicial = enviaJudicial;
	}
	public CriAsiPro getCriAsiPro(){
		return criAsiPro;
	}
	public void setCriAsiPro(CriAsiPro criAsiPro){
		this.criAsiPro = criAsiPro;
	}
	public GenCue getGenCue(){
		return genCue;
	}
	public void setGenCue(GenCue genCue){
		this.genCue = genCue;
	}
	public GenCodGes getGenCodGes(){
		return genCodGes;
	}
	public void setGenCodGes(GenCodGes genCodGes){
		this.genCodGes = genCodGes;
	}
	public String getUltPerEmi(){
		return ultPerEmi;
	}
	public void setUltPerEmi(String ultPerEmi){
		this.ultPerEmi = ultPerEmi;
	}
	public Date getFechaAlta(){
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta){
		this.fechaAlta = fechaAlta;
	}
	public Date getFechaBaja(){
		return fechaBaja;
	}
	public void setFechaBaja(Date fechaBaja){
		this.fechaBaja = fechaBaja;
	}
	public PeriodoDeuda getPeriodoDeuda() {
		return periodoDeuda;
	}
	public void setPeriodoDeuda(PeriodoDeuda periodoDeuda) {
		this.periodoDeuda = periodoDeuda;
	}
	public List<RecAtrVal> getListRecAtrVal(){
		return listRecAtrVal;
	}
	public void setListRecAtrVal(List<RecAtrVal> listRecAtrVal){
		this.listRecAtrVal = listRecAtrVal;
	} 
	public List<RecCon> getListRecCon(){
		return listRecCon;
	}
	public void setListRecCon(List<RecCon> listRecCon){
		this.listRecCon = listRecCon;
	} 
	public List<RecClaDeu> getListRecClaDeu(){
		return listRecClaDeu;
	}
	public void setListRecClaDeu(List<RecClaDeu> listRecClaDeu){
		this.listRecClaDeu = listRecClaDeu;
	} 
	public List<RecGenCueAtrVa> getListRecGenCueAtrVa(){
		return listRecGenCueAtrVa;
	}
	public void setListRecGenCueAtrVa(List<RecGenCueAtrVa> listRecGenCueAtrVa){
		this.listRecGenCueAtrVa = listRecGenCueAtrVa;
	}
	public List<RecEmi> getListRecEmi(){
		return listRecEmi;
	}
	public void setListRecEmi(List<RecEmi> listRecEmi){
		this.listRecEmi = listRecEmi;
	}
	public void setFormPadFir(Formulario formPadFir) {
		this.formPadFir = formPadFir;
	}

	public Formulario getFormPadFir() {
		return formPadFir;
	}
	public List<RecAtrCueEmi> getListRecAtrCueEmi(){
		return listRecAtrCueEmi;
	}
	public void setListRecAtrCueEmi(List<RecAtrCueEmi> listRecAtrCueEmi){
		this.listRecAtrCueEmi = listRecAtrCueEmi;
	}
	public List<RecAtrCue> getListRecAtrCue(){
		return listRecAtrCue;
	}
	public void setListRecAtrCue(List<RecAtrCue> listRecAtrCue){
		this.listRecAtrCue = listRecAtrCue;
	}
	public Atributo getAtributoAse() {
		return atributoAse;
	}
	public void setAtributoAse(Atributo atributoAse) {
		this.atributoAse = atributoAse;
	}

	public Date getFecUltPag() {
		return (fecUltPag!=null)? fecUltPag : new Date();
	}

	public void setFecUltPag(Date fecUltPag) {
		this.fecUltPag = fecUltPag;
	}

	public List<RecConADec> getListRecConADec() {
		return listRecConADec;
	}

	public void setListRecConADec(List<RecConADec> listRecConADec) {
		this.listRecConADec = listRecConADec;
	}

	public List<TipRecConADec> getListTipRecConADec() {
		return listTipRecConADec;
	}

	public void setListTipRecConADec(List<TipRecConADec> listTipRecConADec) {
		this.listTipRecConADec = listTipRecConADec;
	}
	public List<RecMinDec> getListRecMinDec(){
		return listRecMinDec;
	}
	public void setListRecMinDec(List<RecMinDec> listRecMinDec){
		this.listRecMinDec = listRecMinDec;
	}
	
	public List<RecAli> getListRecAli() {
		return listRecAli;
	}

	public void setListRecAli(List<RecAli> listRecAli) {
		this.listRecAli = listRecAli;
	}
	
	public Integer getPerImpMasDeu() {
		return perImpMasDeu;
	}

	public void setPerImpMasDeu(Integer perImpMasDeu) {
		this.perImpMasDeu = perImpMasDeu;
	}

	public Formulario getFormImpMasDeu() {
		return formImpMasDeu;
	}

	public void setFormImpMasDeu(Formulario formImpMasDeu) {
		this.formImpMasDeu = formImpMasDeu;
	}

	public Integer getGenNotif() {
		return genNotif;
	}

	public void setGenNotif(Integer genNotif) {
		this.genNotif = genNotif;
	}

	public Formulario getFormNotif() {
		return formNotif;
	}

	public void setFormNotif(Formulario formNotif) {
		this.formNotif = formNotif;
	}

	public List<RecursoArea> getListRecursoArea() {
		return listRecursoArea;
	}
	public void setListRecursoArea(List<RecursoArea> listRecursoArea) {
		this.listRecursoArea = listRecursoArea;
	}

	// Metodos de Clase
	public static Recurso getById(Long id) {
		return (Recurso) DefDAOFactory.getRecursoDAO().getById(id);
	}
	
	public static Recurso getByIdNull(Long id) {
		return (Recurso) DefDAOFactory.getRecursoDAO().getByIdNull(id);
	}
	
	public static List<Recurso> getList() {
		return (ArrayList<Recurso>) DefDAOFactory.getRecursoDAO().getList();
	}
	
	public static List<Recurso> getListActivos() throws Exception {			
		return filtrarPorArea((List<Recurso>) DefDAOFactory.getRecursoDAO().getListActiva());
	}
	
	/**
	 * Obtiene la lista de recursos que pueden ser emitidos 
	 * durante las emisiones de deuda masivas, individuales, 
	 * corregidas y extraordinarias
	 */
	public static List<Recurso> getListEmitibles() throws Exception {			
		return filtrarPorArea((ArrayList<Recurso>) DefDAOFactory.getRecursoDAO().getListEmitibles());
	}

	/**
	 * Obtiene la lista de recursos activos que permiten
	 * emisiones extraordinarias
	 */
	public static List<Recurso> getListWithEmisionExt() throws Exception {			
		return filtrarPorArea((ArrayList<Recurso>) DefDAOFactory.getRecursoDAO().getListWithEmisionExt());
	}

	/**
	 * Obtiene la lista de recursos que tienen una impresion masiva de dueda.
	 */
	public static List<Recurso> getListWithImpresionMasiva() throws Exception {			
		return filtrarPorArea((ArrayList<Recurso>) DefDAOFactory.getRecursoDAO().getListWithImpresionMasiva());
	}
	
	public static List<Recurso> getListActivosByIdCategoria(Long id) throws Exception {			
		return filtrarPorArea((List<Recurso>) DefDAOFactory.getRecursoDAO().getListActivosByIdCategoria(id));
	}
	
	public static List<Recurso> getListByIdCategoria(Long id) throws Exception {			
		return filtrarPorArea((List<Recurso>) DefDAOFactory.getRecursoDAO().getListByIdCategoria(id));
	}

	public static List<Recurso> getListRecPriActivosByIdTipObjImp(Long id) throws Exception {			
		return filtrarPorArea((List<Recurso>) DefDAOFactory.getRecursoDAO().getListRecPriActivosByIdTipObjImp(id));
	}
	
	public static List<Recurso> getListVigentes(Date fecha) throws Exception {			
		return filtrarPorArea((List<Recurso>) DefDAOFactory.getRecursoDAO().getListVigentes(fecha));
	}

	public static Recurso getByCodigo(String codigo) throws Exception{
		return (Recurso) DefDAOFactory.getRecursoDAO().getByCodigo(codigo);
	}
	
	public static Recurso getTGI(){
		return (Recurso) DefDAOFactory.getRecursoDAO().getByCodigo(COD_RECURSO_TGI);
	}
	
	public static Recurso getDReI(){
		return (Recurso) DefDAOFactory.getRecursoDAO().getByCodigo(COD_RECURSO_DReI);
	}

	public static Recurso getETur() {
		return (Recurso) DefDAOFactory.getRecursoDAO().getByCodigo(COD_RECURSO_ETuR);
	}
	
	public static String getRecInfoVigentes() {
		List<Recurso> listRecursos = (List<Recurso>) DefDAOFactory.getRecursoDAO().getListActiva();
		String ret="";
		for (Recurso recurso : listRecursos) {
			if (recurso.getCategoria().getTipo().getId().equals(1L)) {
				ret += recurso.getRecInfoRaw();
			}
		}
		return ret;
	}
	
	/**
	 * Obtiene la lista de recursos activos con envio de deuda a judicial
	 * @return List<Recurso>
	 */
	public static List<Recurso> getListActivosEnvioJudicial() throws Exception {			
		return filtrarPorArea((ArrayList<Recurso>) DefDAOFactory.getRecursoDAO().getListActivosEnvioJudicial());
	}
	
	public static List<Recurso> getListAutoliquidablesVigentes(Date fecha) throws Exception {			
		return filtrarPorArea((List<Recurso>) DefDAOFactory.getRecursoDAO().getListAutoliquidablesVigentes(fecha));
	}
	
	/**
	 *  Devuelve los RecAtrCue para el recurso y que sean vigentes a una fecha dada. 
	 * 
	 * @param recurso
	 * @param fecha
	 * @return
	 */
	public List<RecAtrCue> getListRecAtrCueVigentes(Date fecha){
		return (ArrayList<RecAtrCue>) DefDAOFactory.getRecursoDAO().getListRecAtrCueVigentes(this, fecha);
	}
	
	/**
	 * Devuelve los recursos que permiten alta manual y son Tributarios
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static List<Recurso> getListPermitenAltaManualCuentaVigentes(Date fecha) throws Exception {			
		return filtrarPorAreaPermiteCrearEmitir((List<Recurso>) DefDAOFactory.getRecursoDAO().getListPermitenAltaManualCuentaVigentes(fecha));
	}

	/**
	 * Devuelve los recursos que son Tributarios.
	 * 
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
	public static List<Recurso> getListTributarios() throws Exception {			
		return filtrarPorArea((List<Recurso>) DefDAOFactory.getRecursoDAO().getListTributarios());
	}
	
	/**
	 * Devuelve los recursos que son Tributarios, vigentes a la fecha dada.
	 * 
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
	public static List<Recurso> getListTributariosVigentes(Date fecha) throws Exception {
		return filtrarPorArea((List<Recurso>) DefDAOFactory.getRecursoDAO().getListTributariosVigentes(fecha));
	}
	
	/**
	 * Devuelve los recursos No Tributarios, vigentes a la fecha dada.
	 * 
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
	public static List<Recurso> getListNoTributariosVigentes(Date fecha) throws Exception {			
		return filtrarPorArea((List<Recurso>) DefDAOFactory.getRecursoDAO().getListNoTributariosVigentes(fecha));
	}
	
	/**
	 * Devuelve los recursos que permiten emisiones puntuales.
	 * 
	 * @return listRecurso
	 * @throws Exception
	 */
	public static List<Recurso> getListWithEmisionPuntual() throws Exception {			
		return filtrarPorArea((List<Recurso>) DefDAOFactory.getRecursoDAO().getListWithEmisionPuntual());
	}
	
	/**
	 * Devuelve los recursos que permiten emisiones puntuales vigentes.
	 * 
	 * @param fecha
	 * @return listRecurso
	 * @throws Exception
	 */
	public static List<Recurso> getListWithEmisionPuntualVigentes(Date fecha) throws Exception {			
		return filtrarPorAreaPermiteCrearEmitir((List<Recurso>) DefDAOFactory.getRecursoDAO().getListWithEmisionPuntualVigentes(fecha));
	}
	
	
	/**
	 * Filtra un lista de recurso segun la configuracion de la tabla Recurso Area.
	 * 
	 * @param listRecurso
	 * @return
	 * @throws Exception
	 */
	public static List<Recurso> filtrarPorArea(List<Recurso> listRecurso) throws Exception {
		
		UserContext userContext = DemodaUtil.currentUserContext();	
		
		List<Recurso> listRecursoRet = new ArrayList<Recurso>();
		
		List<RecursoArea> listRecursoArea = RecursoArea.getListByAreaActivos(userContext.getIdArea());
		
		// Si no existe restriccion, se devuelve la lista tal cual se obtuvo
		if (listRecursoArea != null && !listRecursoArea.isEmpty()){					
			// Solo devolvemos los recursos que esten dentro de la restriccion
			for(RecursoArea recursoArea:listRecursoArea){
				for (Recurso recurso:listRecurso){
					if (recurso.getId().equals(recursoArea.getRecurso().getId())){
						listRecursoRet.add(recurso);
					}
				}
			}
		} else {
			listRecursoRet = listRecurso;
		}
		
		return listRecursoRet;
	} 
	
	/**
	 * Filtra un lista de recurso segun la configuracion de la tabla Recurso Area y
	 * teniendo en cuenta la bandera "Permite Crear/Emitir"
	 * 
	 * @param listRecurso
	 * @return
	 * @throws Exception
	 */
	public static List<Recurso> filtrarPorAreaPermiteCrearEmitir(List<Recurso> listRecurso) throws Exception {
		
		UserContext userContext = DemodaUtil.currentUserContext();	
		
		List<Recurso> listRecursoRet = new ArrayList<Recurso>();
		
		List<RecursoArea> listRecursoArea = RecursoArea.getListByAreaActivos(userContext.getIdArea());
		
		// Si no existe restriccion, se devuelve la lista tal cual se obtuvo
		if (listRecursoArea != null && !listRecursoArea.isEmpty()){					
			// Solo devolvemos los recursos que esten dentro de la restriccion
			for(RecursoArea recursoArea:listRecursoArea){
				for (Recurso recurso:listRecurso){
					if (recurso.getId().equals(recursoArea.getRecurso().getId()) &&
							recursoArea.getPerCreaEmi().intValue() == 1){
						listRecursoRet.add(recurso);
					}
				}
			}
		} else {
			listRecursoRet = listRecurso;
		}
		
		return listRecursoRet;
	} 
	
	/**
	 * Devuelve verdadero o false dependiendo de si el area tiene filtrado el recurso. 
	 * 
	 * @param recurso
	 * @return boolean
	 * @throws Exception
	 */
	public boolean habilitadoParaElArea() throws Exception {
		
		UserContext userContext = DemodaUtil.currentUserContext();	
		
		List<RecursoArea> listRecursoArea = RecursoArea.getListByAreaActivos(userContext.getIdArea());
		
		// Si no existe restriccion, se devuelve la lista tal cual se obtuvo
		if (listRecursoArea != null && !listRecursoArea.isEmpty()){					
			// Solo devolvemos los recursos que esten dentro de la restriccion
			for(RecursoArea recursoArea: listRecursoArea){
				if (this.getId().equals(recursoArea.getRecurso().getId())){
					return true;
				}
			}
		} else {
			return true;
		}
		
		return false;
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
	
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos
		if(getCategoria()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_CATEGORIA);
			return false;
		}
		
		if(getCategoria().getTipo().getId().equals(Tipo.ID_TIPO_TRIBUTARIA)){

			if (StringUtil.isNullOrEmpty(getCodRecurso())) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_CODRECURSO);
			}
			if (StringUtil.isNullOrEmpty(getDesRecurso())) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_DESRECURSO);
			}
			if(!SiNo.getEsValido(getEsAutoliquidable())){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_ESAUTOLIQUIDABLE);
			}else{
				if(getEsAutoliquidable()==1 && getFecEsAut()==null){
					addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_FECESAUT);
				}
			}
			if(!SiNo.getEsValido(getEsFiscalizable())){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_ESFISCALIZABLE);
			}else{
				if(getEsFiscalizable()==1 &&getFecEsFis()==null){
					addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_FECESFIS);
				}
			}
			if(!SiNo.getEsValido(getGesDeuNoVen())){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_GESDEUNOVEN);
			}
			if(!SiNo.getEsValido(getDeuExiVen())){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_DEUEXIVEN);
			}
			if(!SiNo.getEsValido(getGesCobranza())){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_GESCOBRANZA);
			}
			if(!SiNo.getEsValido(getPerEmiDeuMas())){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_PEREMIDEUMAS);
			}
			if(!SiNo.getEsValido(getPerEmiDeuPuntual())){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_PEREMIDEUPUNTUAL);
			}
			if(!SiNo.getEsValido(getPerImpMasDeu())) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_PERIMPMASDEU);
			} else {
				if(SiNo.SI.getBussId().equals(getPerImpMasDeu())) {
					if (getFormImpMasDeu() == null) {
						addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_FORMIMPMASDEU);
					}
					if (!SiNo.getEsValido(getGenNotif())) {
						addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_GENNOTIF);
					} else {
						if (SiNo.SI.getBussId().equals(getGenNotif()) && getFormNotif() == null) {
							addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_FORMNOTIF);
						}							
					}
					if (SiNo.SI.getBussId().equals(getGenPadFir()) && null == getFormPadFir()) {
						addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_FORMPADFIR);
					}
				}
			}

			if(!SiNo.getEsValido(getPerEmiDeuExt())){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_PEREMIDEUEXT);
			}
			if(!SiNo.getEsValido(getEsDeudaTitular())){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_ESDEUDATITULAR);
			}
			if(getTipObjImp()!=null){
				if(!SiNo.getEsValido(getEsPrincipal())){
					addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_ESPRINCIPAL);
				}
				if(!SiNo.getEsValido(getEsLibreDeuda())){
					addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_ESLIBREDEUDA);
				}
				if(SiNo.getEsValido(getEsPrincipal())){
					if(getEsPrincipal()==1){
						if(!SiNo.getEsValido(getAltaCtaPorIface())){
							addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_ALTACTAPORIFACE);
						}
						if(!SiNo.getEsValido(getBajaCtaPorIface())){
							addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_BAJACTAPORIFACE);
						}
						if(!SiNo.getEsValido(getModiTitCtaPorIface())){
							addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_MODITITCTAPORIFACE);
						}
					}
					if(getEsPrincipal()==0){
						if(getRecPri()==null){
							addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_RECPRI);
						}
						if(!SiNo.getEsValido(getAltaCtaPorPri())){
							addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_ALTACTAPORPRI);
						}
						if(!SiNo.getEsValido(getBajaCtaPorPri())){
							addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_BAJACTAPORPRI);
						}
						if(!SiNo.getEsValido(getModiTitCtaPorPri())){
							addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_MODITITCTAPORPRI);
						}
					}
				}
			}
			if(!SiNo.getEsValido(getAltaCtaManual())){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_ALTACTAMANUAL);
			}
			if(!SiNo.getEsValido(getBajaCtaManual())){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_BAJACTAMANUAL);
			}
			if(!SiNo.getEsValido(getModiTitCtaManual())){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_MODITITCTAMANUAL);
			}
			if(SiNo.getEsValido(getEnviaJudicial())){
				if(getEnviaJudicial()==1){
					if(getCriAsiPro()==null){
						addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_CRIASIPRO);
					}
				}
			}else{
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_ENVIAJUDICIAL);
			}
			if (getFechaAlta()==null) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_FECHAALTA);
			}
			if (getPeriodoDeuda()==null) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_PERIODODEUDA);
			}
			
			if(getGenCodGes()==null){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_GENCODGES);
			}
		}
		else{
			if (StringUtil.isNullOrEmpty(getCodRecurso())) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_CODRECURSO);
			}
			if (StringUtil.isNullOrEmpty(getDesRecurso())) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_DESRECURSO);
			}
			if (getFechaAlta()==null) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_FECHAALTA);
			}
		}

	
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de Unicidad
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codRecurso");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, DefError.RECURSO_CODRECURSO);			
		}

		if (hasError()) {
			return false;
		}
		// Otras Validaciones
			
		return !hasError();
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();

		if (GenericDAO.hasReference(this, RecAtrVal.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , DefError.RECATRVAL_LABEL);
		}
		if (GenericDAO.hasReference(this, RecCon.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , DefError.RECCON_LABEL);
		}
		if (GenericDAO.hasReference(this, RecClaDeu.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , DefError.RECCLADEU_LABEL);
		}
		if (GenericDAO.hasReference(this, RecGenCueAtrVa.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , DefError.RECGENCUEATRVA_LABEL);
		}
		if (GenericDAO.hasReference(this, RecAtrCue.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , DefError.RECATRCUE_LABEL);
		}
		if (GenericDAO.hasReference(this, RecEmi.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , DefError.RECEMI_LABEL);
		}
		if (GenericDAO.hasReference(this, RecAtrCueEmi.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , DefError.RECATRCUEEMI_LABEL);
		}
		if (GenericDAO.hasReference(this, Cuenta.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , PadError.CUENTA_LABEL);
		}
		if (GenericDAO.hasReference(this, Solicitud.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , CasError.SOLICITUD_LABEL);
		}
		if (GenericDAO.hasReference(this, Recurso.class, "recPri")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , DefError.RECURSO_LABEL);
		}
		if (GenericDAO.hasReference(this, SerBanRec.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , DefError.SERBANREC_LABEL);
		}
		if (GenericDAO.hasReference(this, Exencion.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , ExeError.EXENCION_LABEL);
		}
		if (GenericDAO.hasReference(this, ProcesoMasivo.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , GdeError.PROCESO_MASIVO_LABEL);
		}
		if (GenericDAO.hasReference(this, PlanRecurso.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , GdeError.PLAN_LABEL);
		}
		if (GenericDAO.hasReference(this, ProRec.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , GdeError.PROREC_LABEL);
		}
		if (GenericDAO.hasReference(this, Broche.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , PadError.BROCHE_LABEL);
		}
		if (GenericDAO.hasReference(this, Repartidor.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , PadError.REPARTIDOR_LABEL);
		}
		if (GenericDAO.hasReference(this, TipoRepartidor.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , PadError.TIPOREPARTIDOR_LABEL);
		}
		if (GenericDAO.hasReference(this, TipoObra.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , RecError.TIPOOBRA_LABEL);
		}
		if (GenericDAO.hasReference(this, Contrato.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , RecError.CONTRATO_LABEL);
		}
		if (GenericDAO.hasReference(this, FormaPago.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , RecError.FORMAPAGO_LABEL);
		}
		if (GenericDAO.hasReference(this, PlanillaCuadra.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , RecError.PLANILLACUADRA_LABEL);
		}
		if (GenericDAO.hasReference(this, DisPar.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , BalError.DISPAR_LABEL);
		}
		if (GenericDAO.hasReference(this, DisParRec.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , BalError.DISPARREC_LABEL);
		}
		if (GenericDAO.hasReference(this, RecMinDec.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , DefError.RECMINDEC_LABEL);
		}
		if (GenericDAO.hasReference(this, RecAli.class, "recurso")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECURSO_LABEL , DefError.RECALI_LABEL);
		}
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	//	 Metodos de negocio
	
	/**
	 * Devuelve la lista de recCon del recurso ordenada por orden de visualizacion de los mismos 
	 */
	public List<RecCon> getListRecConPorOrdenVisualizacion(){
		return DefDAOFactory.getRecConDAO().getListRecConOrderByVisualizacion(getId());
	}
	
	// 	 Administracion de RecAtrVal
	public RecAtrVal createRecAtrVal(RecAtrVal recAtrVal) throws Exception {
		
		// Validaciones de negocio
		if (!recAtrVal.validateCreate()) {
			return recAtrVal;
		}

		DefDAOFactory.getRecAtrValDAO().update(recAtrVal);
		
		return recAtrVal;
	}	

	public RecAtrVal updateRecAtrVal(RecAtrVal recAtrVal) throws Exception {
		
		// Validaciones de negocio
		if (!recAtrVal.validateUpdate()) {
			return recAtrVal;
		}

		DefDAOFactory.getRecAtrValDAO().update(recAtrVal);
		
		return recAtrVal;
	}	

	public RecAtrVal deleteRecAtrVal(RecAtrVal recAtrVal) throws Exception {
		
		// Validaciones de negocio
		if (!recAtrVal.validateDelete()) {
			return recAtrVal;
		}
				
		DefDAOFactory.getRecAtrValDAO().delete(recAtrVal);
		
		return recAtrVal;
	}	
	// Administracion de RecCon
	public RecCon createRecCon(RecCon recCon) throws Exception {
		
		// Validaciones de negocio
		if (!recCon.validateCreate()) {
			return recCon;
		}

		DefDAOFactory.getRecConDAO().update(recCon);
		
		return recCon;
	}	

	public RecCon updateRecCon(RecCon recCon) throws Exception {
		
		// Validaciones de negocio
		if (!recCon.validateUpdate()) {
			return recCon;
		}

		DefDAOFactory.getRecConDAO().update(recCon);
		
		return recCon;
	}	

	public RecCon deleteRecCon(RecCon recCon) throws Exception {
		
		// Validaciones de negocio
		if (!recCon.validateDelete()) {
			return recCon;
		}
				
		DefDAOFactory.getRecConDAO().delete(recCon);
		
		return recCon;
	}	
	// Administracion de RecClaDeu
	public RecClaDeu createRecClaDeu(RecClaDeu recClaDeu) throws Exception {
		
		// Validaciones de negocio
		if (!recClaDeu.validateCreate()) {
			return recClaDeu;
		}

		DefDAOFactory.getRecClaDeuDAO().update(recClaDeu);
		
		return recClaDeu;
	}	

	public RecClaDeu updateRecClaDeu(RecClaDeu recClaDeu) throws Exception {
		
		// Validaciones de negocio
		if (!recClaDeu.validateUpdate()) {
			return recClaDeu;
		}

		DefDAOFactory.getRecClaDeuDAO().update(recClaDeu);
		
		return recClaDeu;
	}	

	public RecClaDeu deleteRecClaDeu(RecClaDeu recClaDeu) throws Exception {
		
		// Validaciones de negocio
		if (!recClaDeu.validateDelete()) {
			return recClaDeu;
		}
				
		DefDAOFactory.getRecClaDeuDAO().delete(recClaDeu);
		
		return recClaDeu;
	}	
	// Administracion de RecGenCueAtrVa
	public RecGenCueAtrVa createRecGenCueAtrVa(RecGenCueAtrVa recGenCueAtrVa) throws Exception {
		
		// Validaciones de negocio
		if (!recGenCueAtrVa.validateCreate()) {
			return recGenCueAtrVa;
		}

		DefDAOFactory.getRecGenCueAtrVaDAO().update(recGenCueAtrVa);
		
		return recGenCueAtrVa;
	}	

	public RecGenCueAtrVa updateRecGenCueAtrVa(RecGenCueAtrVa recGenCueAtrVa) throws Exception {
		
		// Validaciones de negocio
		if (!recGenCueAtrVa.validateUpdate()) {
			return recGenCueAtrVa;
		}

		DefDAOFactory.getRecGenCueAtrVaDAO().update(recGenCueAtrVa);
		
		return recGenCueAtrVa;
	}	

	public RecGenCueAtrVa deleteRecGenCueAtrVa(RecGenCueAtrVa recGenCueAtrVa) throws Exception {
		
		// Validaciones de negocio
		if (!recGenCueAtrVa.validateDelete()) {
			return recGenCueAtrVa;
		}
				
		DefDAOFactory.getRecGenCueAtrVaDAO().delete(recGenCueAtrVa);
		
		return recGenCueAtrVa;
	}	
	// Administracion de RecEmi
	public RecEmi createRecEmi(RecEmi recEmi) throws Exception {
		
		// Validaciones de negocio
		if (!recEmi.validateCreate()) {
			return recEmi;
		}

		DefDAOFactory.getRecEmiDAO().update(recEmi);
		
		return recEmi;
	}	

	public RecEmi updateRecEmi(RecEmi recEmi) throws Exception {
		
		// Validaciones de negocio
		if (!recEmi.validateUpdate()) {
			return recEmi;
		}

		DefDAOFactory.getRecEmiDAO().update(recEmi);
		
		return recEmi;
	}	

	public RecEmi deleteRecEmi(RecEmi recEmi) throws Exception {
		
		// Validaciones de negocio
		if (!recEmi.validateDelete()) {
			return recEmi;
		}
				
		DefDAOFactory.getRecEmiDAO().delete(recEmi);
		
		return recEmi;
	}	
	// Administracion de RecAtrCueEmi
	public RecAtrCueEmi createRecAtrCueEmi(RecAtrCueEmi recAtrCueEmi) throws Exception {
		
		// Validaciones de negocio
		if (!recAtrCueEmi.validateCreate()) {
			return recAtrCueEmi;
		}

		DefDAOFactory.getRecAtrCueEmiDAO().update(recAtrCueEmi);
		
		return recAtrCueEmi;
	}	

	public RecAtrCueEmi updateRecAtrCueEmi(RecAtrCueEmi recAtrCueEmi) throws Exception {
		
		// Validaciones de negocio
		if (!recAtrCueEmi.validateUpdate()) {
			return recAtrCueEmi;
		}

		DefDAOFactory.getRecAtrCueEmiDAO().update(recAtrCueEmi);
		
		return recAtrCueEmi;
	}	

	public RecAtrCueEmi deleteRecAtrCueEmi(RecAtrCueEmi recAtrCueEmi) throws Exception {
		
		// Validaciones de negocio
		if (!recAtrCueEmi.validateDelete()) {
			return recAtrCueEmi;
		}
				
		DefDAOFactory.getRecAtrCueEmiDAO().delete(recAtrCueEmi);
		
		return recAtrCueEmi;
	}	
	// Administracion de RecAtrCue
	public RecAtrCue createRecAtrCue(RecAtrCue recAtrCue) throws Exception {
		
		// Validaciones de negocio
		if (!recAtrCue.validateCreate()) {
			
			System.out.println(" ###############  RecAtrCue No valida");
			
			return recAtrCue;
		}
		
		System.out.println(" ###############  RecAtrCue validado");
		
		DefDAOFactory.getRecAtrCueDAO().update(recAtrCue);
		
		return recAtrCue;
	}	

	public RecAtrCue updateRecAtrCue(RecAtrCue recAtrCue) throws Exception {
		
		// Validaciones de negocio
		if (!recAtrCue.validateUpdate()) {
			return recAtrCue;
		}
		DefDAOFactory.getRecAtrCueDAO().update(recAtrCue);
		
		return recAtrCue;
	}	

	public RecAtrCue deleteRecAtrCue(RecAtrCue recAtrCue) throws Exception {
		
		// Validaciones de negocio
		if (!recAtrCue.validateDelete()) {
			return recAtrCue;
		}
		DefDAOFactory.getRecAtrCueDAO().delete(recAtrCue);
		
		return recAtrCue;
	}	

	// Administracion de RecMinDec
	public RecMinDec createRecMinDec(RecMinDec recMinDec) throws Exception {
		
		// Validaciones de negocio
		if (!recMinDec.validateCreate()) {
			return recMinDec;
		}

		DefDAOFactory.getRecMinDecDAO().update(recMinDec);
		
		return recMinDec;
	}	

	public RecMinDec updateRecMinDec(RecMinDec recMinDec) throws Exception {
		
		// Validaciones de negocio
		if (!recMinDec.validateUpdate()) {
			return recMinDec;
		}

		DefDAOFactory.getRecMinDecDAO().update(recMinDec);
		
		return recMinDec;
	}	

	public RecMinDec deleteRecMinDec(RecMinDec recMinDec) throws Exception {
		
		// Validaciones de negocio
		if (!recMinDec.validateDelete()) {
			return recMinDec;
		}
				
		DefDAOFactory.getRecMinDecDAO().delete(recMinDec);
		
		return recMinDec;
	}

	// Administracion de RecAli
	public RecAli createRecAli(RecAli recAli) throws Exception {
		
		// Validaciones de negocio
		if (!recAli.validateCreate()) {
			return recAli;
		}

		DefDAOFactory.getRecAliDAO().update(recAli);
		
		return recAli;
	}	

	public RecAli updateRecAli(RecAli recAli) throws Exception {
		
		// Validaciones de negocio
		if (!recAli.validateUpdate()) {
			return recAli;
		}

		DefDAOFactory.getRecAliDAO().update(recAli);
		
		return recAli;
	}	

	public RecAli deleteRecAli(RecAli recAli) throws Exception {
		
		// Validaciones de negocio
		if (!recAli.validateDelete()) {
			return recAli;
		}
				
		DefDAOFactory.getRecAliDAO().delete(recAli);
		
		return recAli;
	}

	
	// Administracion de RecursoArea
	public RecursoArea createRecursoArea(RecursoArea recursoArea) throws Exception {
		
		// Validaciones de negocio
		if (!recursoArea.validateCreate()) {
			return recursoArea;
		}

		DefDAOFactory.getRecursoAreaDAO().update(recursoArea);
		
		return recursoArea;
	}
	
	public RecursoArea updateRecursoArea(RecursoArea recursoArea) throws Exception {
		
		// Validaciones de negocio
		if (!recursoArea.validateUpdate()) {
			return recursoArea;
		}

		DefDAOFactory.getRecursoAreaDAO().update(recursoArea);
		
		return recursoArea;
	}
	
	public RecursoArea deleteRecursoArea(RecursoArea recursoArea) throws Exception {
		
		// Validaciones de negocio
		if (!recursoArea.validateDelete()) {
			return recursoArea;
		}

		DefDAOFactory.getRecursoAreaDAO().delete(recursoArea);
		
		return recursoArea;
	}	
	
	
	/**
	 * Activa el Recurso. Previamente valida la activacion 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setFechaBaja(null);
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getRecursoDAO().update(this);
	}

	/**
	 * Desactiva el Recurso. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		DefDAOFactory.getRecursoDAO().update(this);
	}
	/**
	 * Pasa el estado a creado 
	 *
	 */
	public void creado(){
		this.setEstado(Estado.CREADO.getId());
		DefDAOFactory.getRecursoDAO().update(this);
	}	
	
	
	
	/**
	 * Valida la activacion del Recurso
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Recurso
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
	
		if (getFechaBaja()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_FECHABAJA);
		}
		
		// Valida que la Fecha Alta no sea mayor que la fecha Baja, y que la fecha Baja no sea mayor que la actual
		if(this.fechaBaja!=null){
			if(!DateUtil.isDateBeforeOrEqual(this.fechaAlta, this.fechaBaja)){
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, DefError.RECURSO_FECHAALTA, DefError.RECURSO_FECHABAJA);
			}			
			if(!DateUtil.isDateAfterOrEqual(new Date(), this.fechaBaja)){
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, DefError.RECURSO_FECHABAJA, BaseError.MSG_FECHA_ACTUAL);
			}			
		}
	
		if (hasError()) {
			return false;
		}
	
		return !hasError();
	}

	//Metodos Relacionados con RecursoDefinition

	/**
	 * Obtiene la definicion de un unico atributo sin valorizar.
	 * <p>Este metodo sirve para obtener un unico atributo a valorizar.
	 * @param idAtributo id del Atributo a cargar.
	 * @return el RecursoDefinition cargado con el atributo cuyo id es el del parametro (sin valor).
	 * @throws Exception 
	 */
	public RecursoDefinition getDefinition(Long idAtributo) throws Exception {
		RecursoDefinition recursoDefinition = new RecursoDefinition();
		AtributoVO atributo = (AtributoVO) Atributo.getById(idAtributo).toVO(2);		
		// Creo la definicion
		GenericAtrDefinition genericAtrDefinition = new GenericAtrDefinition();
		
		genericAtrDefinition.setAtributo(atributo);
		genericAtrDefinition.setPoseeVigencia(true);
		
		recursoDefinition.addGenericAtrDefinition(genericAtrDefinition);
	 
		return recursoDefinition;
	}

	//--> RecAtrVal
	/**
	 * Obtiene la definicion con sus valores del RecAtrVal.
	 * <p>Este metodo sirve para obtener los atributos y sus valores de un recurso.
	 * @return el RecursoAtrDefinition cargado con los atributos y sus valores.
	 * @throws Exception 
	 */
	public RecursoDefinition getDefinitionRecAtrValValue() throws Exception {
		RecursoDefinition recursoDefinition = new RecursoDefinition();
		List<RecAtrVal> listRecAtrVal =  RecAtrVal.getListByIdRecurso(this.getId());
		boolean genAtrDefCreado = false;
		
		for (RecAtrVal recAtrVal: listRecAtrVal ){
			// Por cada (RecAtrVal) busco un GenericAtrDefinition en recursoDefinition con el codigo atributo, si no existe lo creo
			GenericAtrDefinition genericAtrDefinition = recursoDefinition.getGenericAtrDefinitionById(recAtrVal.getAtributo().getId());

			if(genericAtrDefinition==null){
				genericAtrDefinition = new GenericAtrDefinition();
				genericAtrDefinition.setAtributo((AtributoVO) recAtrVal.getAtributo().toVO(2));
				genAtrDefCreado = true;
			}
			
			// Por ahora para los atributos del recurso, suponemos que todos tienen vigencia. (se setea poseevigencia=true)
			String valorVigencia = "";
			valorVigencia = genericAtrDefinition.getValor4VigView(recAtrVal.getValor()); 
			genericAtrDefinition.setPoseeVigencia(true);
			// Agrega un valor a la lista de vigencias
			genericAtrDefinition.addAtrValVig(valorVigencia, null, recAtrVal.getFechaDesde(), recAtrVal.getFechaHasta());
			
			if(DateUtil.isDateInRange(new Date(), recAtrVal.getFechaDesde(), recAtrVal.getFechaHasta())){
				genericAtrDefinition.addValor(recAtrVal.getValor(), null, recAtrVal.getFechaDesde(), recAtrVal.getFechaHasta());
			}
			
			// Si el GenericAtrDefinition no existia y lo cree, ahora lo agrego al recursoDefinition.
			if(genAtrDefCreado == true){
				recursoDefinition.addGenericAtrDefinition(genericAtrDefinition);
				genAtrDefCreado = false;
			}
		}
		return recursoDefinition;
	}
	
	/**
	 * Obtiene la definicion de un unico atributo con sus valores del RecAtrVal.
	 * <p>Este metodo sirve para obtener un unico atributo y sus valores de un recurso..
	 * @param idAtributo id del Atributo a cargar.
	 * @return el RecursoDefinition cargado con el atributo y sus valores cuyo id es el del parametro.
	 * @throws Exception 
	 */
	public RecursoDefinition getDefinitionRecAtrValValue(Long idAtributo) throws Exception {
		RecursoDefinition recursoDefinition = new RecursoDefinition();
		List<RecAtrVal> listRecAtrVal =  RecAtrVal.getListByIdRecAtr(this.getId(),idAtributo);
		
		// Creo la definicon
		GenericAtrDefinition genericAtrDefinition = new GenericAtrDefinition();
		genericAtrDefinition.setAtributo((AtributoVO) Atributo.getById(idAtributo).toVO(2));
		
		// Seteo el valor o los valores
		for (RecAtrVal item: listRecAtrVal ){
			if(DateUtil.isDateInRange(new Date(), item.getFechaDesde(), item.getFechaHasta())){
				genericAtrDefinition.addValor(item.getValor(), null, item.getFechaDesde(), item.getFechaHasta());
			}
			String valorVigencia = "";
			valorVigencia = genericAtrDefinition.getValor4VigView(item.getValor());
						
			genericAtrDefinition.setPoseeVigencia(true);
			// Agrega un valor a la lista de vigencias
			genericAtrDefinition.addAtrValVig(valorVigencia, null, item.getFechaDesde(), item.getFechaHasta() );
		}	
		
		recursoDefinition.addGenericAtrDefinition(genericAtrDefinition);
	 
		return recursoDefinition;
	}

	/**
	 * Inserta el valor y/o las vigencias de un atributo valorizado del 
	 * Recurso. 
	 * @param RecursoDefinition con los valores a insertar
	 * @return el RecursoDefinition con los atributos insertados
	 * @throws Exception 
	 */
	public RecursoDefinition createRecAtrDefinition(RecursoDefinition definition) throws Exception {

		// Recorremos los Atributos para la definition de Recurso, y por cada uno insertamos un registro en RecAtrVal
		for (GenericAtrDefinition item: definition.getListGenericAtrDefinition()){
			
			RecAtrVal recAtrVal = new RecAtrVal();            
            
           	recAtrVal.setRecurso(this);
			recAtrVal.setAtributo(Atributo.getById(item.getAtributo().getId()));
			recAtrVal.setValor(item.getValorString());
			recAtrVal.setFechaDesde(item.getFechaDesde());
			recAtrVal.setFechaHasta(item.getFechaHasta());
			recAtrVal.setEstado(Estado.ACTIVO.getId());
			
			recAtrVal = this.createRecAtrVal(recAtrVal);
		}
		
		return definition;
 	}
	
	/**
	 * Actualiza el valor y/o las vigencias de un atributo valorizado del 
	 * Recurso. El metodo actualiza el valor y las vigencias en caso
	 * de ser un atributo que posee vigencias.
	 * @param RecursoDefinition con los valores a actualizar
	 * @return el RecursoDefinition actualizado
	 * @throws Exception 
	 */
 	public RecursoDefinition updateRecAtrDefinition(RecursoDefinition definition) throws Exception {
 		// Por cada GenericAtrDefinition
		// si poseeVigencia:
		//      se inserta el nuevo registro de recAtrVal y se modifica la fecha hasta del anterior si era nula. 
		// si no posee vigencia:
		// 		obtener recAtrVal, modificar valor, llamar updateRecAtrVal
		 
		for (GenericAtrDefinition item: definition.getListGenericAtrDefinition()){
			
			RecAtrVal recAtrVal = null;            
            
            Long idAtributo = item.getAtributo().getId(); 
            
            // Recupero el atributo que tenga fechaHasta null si existe.
            RecAtrVal recAtrValAbierto = RecAtrVal.getAbiertoByIdRecAtrVal(this.getId(),idAtributo);
            
            if (item.getPoseeVigencia()){
                if (recAtrValAbierto != null) {
                	// seteo de la fecha Hasta del registro con fechaHasta null con el fechaDesde del nuevo 
                	// si tiene fechaDesde menor que que el nuevo. 
                	if(DateUtil.isDateBefore(recAtrValAbierto.getFechaDesde(),item.getFechaDesde())){
                		recAtrValAbierto.setFechaHasta(DateUtil.addDaysToDate(item.getFechaDesde(), -1));//item.getFechaDesde());
            		    recAtrValAbierto = this.updateRecAtrVal(recAtrValAbierto);
                	}
                }
                
            	// Insertar un registro nuevo             	
            	recAtrVal = new RecAtrVal();
            	recAtrVal.setRecurso(this);
				recAtrVal.setAtributo(Atributo.getById(idAtributo));
				recAtrVal.setValor(item.getValorString());
				recAtrVal.setFechaDesde(item.getFechaDesde());
				recAtrVal.setFechaHasta(item.getFechaHasta());
				recAtrVal.setEstado(Estado.ACTIVO.getId());
				
				recAtrVal = this.createRecAtrVal(recAtrVal);
            	
		 	} else {
		 		// Solo se actualiza el valor del registro.
		 		List<RecAtrVal> listRecAtrVal = RecAtrVal.getListByIdRecAtr(this.getId(),idAtributo); 
		 		if(listRecAtrVal == null || listRecAtrVal.size() == 0)
		 			throw new DemodaServiceException("No se encontro el atributo valorizado del recurso a modificar.");
		 		recAtrVal = listRecAtrVal.get(0);
		 		recAtrVal.setValor(item.getValorString());
		 		recAtrVal = this.updateRecAtrVal(recAtrVal);
		 	}	
		}
		
		return definition;
 	}

	/**
	 * Elimina  el atributo valorizado del Recurso. 
	 * @param RecursoDefinition con el valores a eliminar 
	 * @return el RecursoDefinition actualizado.
	 * @throws Exception 
	 */
 	public RecursoDefinition deleteRecAtrDefinition(RecursoDefinition definition) throws Exception {
 		
 		for (GenericAtrDefinition item: definition.getListGenericAtrDefinition()){
            Long idAtributo = item.getAtributo().getId(); 
            List<RecAtrVal> listRecAtrVal = RecAtrVal.getListByIdRecAtr(this.getId(),idAtributo);
        	for(RecAtrVal recAtrVal: listRecAtrVal){
        		this.deleteRecAtrVal(recAtrVal);
        	}
 		}    
        return definition;
 	}

 	//--> RecAtrCue
 	
	/**
	 * Obtiene la definicion de un unico atributo sin valorizar.
	 * <p>Este metodo sirve para obtener un unico atributo a valorizar.
	 * @param idAtributo id del Atributo a cargar.
	 * @return el RecursoDefinition cargado con el atributo cuyo id es el del parametro (sin valor).
	 * @throws Exception 
	 */
	public RecursoDefinition getDefinitionRecAtrCue(Long idAtributo) throws Exception {
		RecursoDefinition recursoDefinition = new RecursoDefinition();
		AtributoVO atributo = (AtributoVO) Atributo.getById(idAtributo).toVO(2);		
		// Creo la definicion
		RecAtrCueDefinition recAtrCueDefinition = new RecAtrCueDefinition();
		
		recAtrCueDefinition.setRecAtrCue(new RecAtrCueVO());
		recAtrCueDefinition.getRecAtrCue().setAtributo(atributo);
				
		recursoDefinition.addRecAtrCueDefinition(recAtrCueDefinition);
	 
		return recursoDefinition;
	}

 	
 	/**
	 * Obtiene la definicion con sus valores del RecAtrCue.
	 * <p>Este metodo sirve para obtener los atributos y sus valores de un recurso para la creacion de cuentas.
	 * @return el RecursoAtrDefinition cargado con los atributos y sus valores para la creacion de cuentas.
	 * @throws Exception 
	 */
	public static RecursoDefinition getDefinitionRecAtrCueValue(Long idRecurso) throws Exception {
		RecursoDefinition recursoDefinition = new RecursoDefinition();
		
		// Obtenemos la lista de definicion RecAtrCue, ordenada por atributo y fecha desde
		List<RecAtrCue> listRecAtrCue =  RecAtrCue.getListByIdRecurso(idRecurso); //
		boolean genAtrDefCreado = false;
		
		for (RecAtrCue recAtrCue: listRecAtrCue ){
			// Por cada (RecAtrCue) busco un GenericAtrDefinition en recursoDefinition con el codigo atributo, si no existe lo creo
			RecAtrCueDefinition recAtrCueDefinition = recursoDefinition.getRecAtrCueDefinitionById(recAtrCue.getAtributo().getId());

			if(recAtrCueDefinition==null){
				recAtrCueDefinition = new RecAtrCueDefinition();
				recAtrCueDefinition.setRecAtrCue((RecAtrCueVO) recAtrCue.toVO(0));
				recAtrCueDefinition.getRecAtrCue().setAtributo((AtributoVO)(recAtrCue.getAtributo().toVO(2)));
				genAtrDefCreado = true;
			} 
			
			/*log.debug("recAtrCue: id " + recAtrCue.getId() + " vis: " + recAtrCue.getEsVisConDeu() + 
					" rec: " + recAtrCue.getEsRequerido());
			log.debug("getDefinitionRecAtrCueValue vis: " + recAtrCueDefinition.getRecAtrCue().getEsVisConDeu().getId() +
						" rec: " + recAtrCueDefinition.getRecAtrCue().getEsRequerido().getId());*/
						
			if(recAtrCueDefinition.getPoseeVigencia()){
				String valorVigencia = "";
				valorVigencia = recAtrCueDefinition.getValor4VigView(recAtrCue.getValorDefecto()); 
				// Agrega un valor a la lista de vigencias
				recAtrCueDefinition.addAtrValVig(valorVigencia, null, recAtrCue.getFechaDesde(), recAtrCue.getFechaHasta());
			}
			
			if(DateUtil.isDateInRange(new Date(), recAtrCue.getFechaDesde(), recAtrCue.getFechaHasta())){
				recAtrCueDefinition.addValor(recAtrCue.getValorDefecto(), null, recAtrCue.getFechaDesde(), recAtrCue.getFechaHasta());
			}
			
			// Si el GenericAtrDefinition no existia y lo cree, ahora lo agrego al recursoDefinition.
			if(genAtrDefCreado == true){
				recursoDefinition.addRecAtrCueDefinition(recAtrCueDefinition);
				genAtrDefCreado = false;
			}
		}
		return recursoDefinition;
	}
	
	/**
	 * Devuelve la definicion de RecAtrCue para un recurso.
	 * 
	 * 
	 * @return
	 * @throws Exception
	 */
	public RecursoDefinition getDefinitionRecAtrCueValue() throws Exception {
		
		return getDefinitionRecAtrCueValue(this.getId());		
	}
	
	
	/**
	 * Obtiene la definicion de un unico atributo con sus valores del RecAtrCue.
	 * <p>Este metodo sirve para obtener un unico atributo y sus valores de un recurso para la creacion de cuentas.
	 * @param idAtributo id del Atributo a cargar.
	 * @return el RecursoDefinition cargado con el atributo y sus valores cuyo id es el del parametro.
	 * @throws Exception 
	 */
	public RecursoDefinition getDefinitionRecAtrCueValueByIdAtributo(Long idAtributo) throws Exception {
		RecursoDefinition recursoDefinition = new RecursoDefinition();
		List<RecAtrCue> listRecAtrCue =  RecAtrCue.getListByIdRecAtr(this.getId(),idAtributo);
		
		// Creo la definicon
		RecAtrCueDefinition recAtrCueDefinition = new RecAtrCueDefinition();
		
		// Seteamos el atribuyo del primer elemento para evitar errores al setear las vigencias
		if (listRecAtrCue != null && listRecAtrCue.size() > 0){
			recAtrCueDefinition.setRecAtrCue( new RecAtrCueVO());
			recAtrCueDefinition.getRecAtrCue().setAtributo((AtributoVO) listRecAtrCue.get(0).getAtributo().toVO(3));
		}
		
		// Seteo el valor o los valores
		for (RecAtrCue item: listRecAtrCue ){
			if(DateUtil.isDateInRange(new Date(), item.getFechaDesde(), item.getFechaHasta())){
				recAtrCueDefinition.addValor(item.getValorDefecto(), null, item.getFechaDesde(), item.getFechaHasta());
				recAtrCueDefinition.setRecAtrCue((RecAtrCueVO) item.toVO(2));
				recAtrCueDefinition.getRecAtrCue().setAtributo((AtributoVO)item.getAtributo().toVO(3));
			}
					
			String valorVigencia = "";
			valorVigencia = recAtrCueDefinition.getValor4VigView(item.getValorDefecto());
						
			// Agrega un valor a la lista de vigencias
			recAtrCueDefinition.addAtrValVig(valorVigencia, null, item.getFechaDesde(), item.getFechaHasta() );
		}	
		
		recursoDefinition.addRecAtrCueDefinition(recAtrCueDefinition);
	 
		return recursoDefinition;
	}
	
	/**
	 * Inserta el valor y/o las vigencias de un atributo valorizado del 
	 * Recurso para la creacion de cuentas. 
	 * @param RecursoDefinition con los valores a insertar
	 * @return el RecursoDefinition con los atributos insertados
	 * @throws Exception 
	 */
	public RecursoDefinition createRecAtrCueDefinition(RecursoDefinition definition) throws Exception {

		// Recorremos los Atributos para la definition de Recurso, y por cada uno insertamos un registro en RecAtrCue
		for (RecAtrCueDefinition item: definition.getListRecAtrCueDefinition()){
			
			RecAtrCue recAtrCue = new RecAtrCue();            
            
           	recAtrCue.setRecurso(this);
			recAtrCue.setAtributo(Atributo.getById(item.getAtributo().getId()));
			recAtrCue.setValorDefecto(item.getValorString());
			recAtrCue.setFechaDesde(item.getFechaDesde());
			recAtrCue.setFechaHasta(item.getFechaHasta());
			recAtrCue.setEstado(Estado.ACTIVO.getId());
			recAtrCue.setEsRequerido(item.getEsRequerido()?1:0);
			recAtrCue.setPoseeVigencia(item.getPoseeVigencia()?1:0);
			recAtrCue.setEsVisConDeu(item.getEsVisConDeu()?1:0);
			recAtrCue.setEsVisRec(item.getEsVisRec()?1:0);
						
			recAtrCue = this.createRecAtrCue(recAtrCue);
			
			if (recAtrCue.hasError()){
				recAtrCue.passErrorMessages(definition);
			}
			
		}
		
		return definition;
 	}
	
	/**
	 * Actualiza el valor y/o las vigencias de un atributo valorizado del 
	 * Recurso para la creacion de cuentas. El metodo actualiza el valor y las vigencias en caso
	 * de ser un atributo que posee vigencias.
	 * @param RecursoDefinition con los valores a actualizar
	 * @return el RecursoDefinition actualizado
	 * @throws Exception 
	 */
 	public RecursoDefinition updateRecAtrCueDefinition(RecursoDefinition definition) throws Exception {
 		// Por cada GenericAtrDefinition
		// si poseeVigencia:
		//      se inserta el nuevo registro de recAtrCue y se modifica la fecha hasta del anterior si era nula. 
		// si no posee vigencia:
		// 		obtener recAtrCue, modificar valor, llamar updateRecAtrCue
		 
		for (RecAtrCueDefinition item: definition.getListRecAtrCueDefinition()){
			
			RecAtrCue recAtrCue = null;            
            
            Long idAtributo = item.getAtributo().getId(); 
            
            // Recupero el atributo que tenga fechaHasta null si existe.
            RecAtrCue recAtrCueAbierto = RecAtrCue.getAbiertoByIdRecAtrCue(this.getId(),idAtributo);
            
            if (item.getPoseeVigencia()){

                if (recAtrCueAbierto != null) {
                	// seteo de la fecha Hasta del registro con fechaHasta null con el fechaDesde del nuevo 
                	// si tiene fechaDesde menor que que el nuevo. 
                	if(DateUtil.isDateBefore(recAtrCueAbierto.getFechaDesde(),item.getFechaDesde())){
                		recAtrCueAbierto.setFechaHasta(DateUtil.addDaysToDate(item.getFechaDesde(), -1));
            		    recAtrCueAbierto = this.updateRecAtrCue(recAtrCueAbierto);
                	}
                }
                
            	// Insertar un registro nuevo             	
            	recAtrCue = new RecAtrCue();
            	recAtrCue.setRecurso(this);
				recAtrCue.setAtributo(Atributo.getById(idAtributo));
				recAtrCue.setValorDefecto(item.getValorString());
				recAtrCue.setFechaDesde(item.getFechaDesde());
				recAtrCue.setFechaHasta(item.getFechaHasta());
				recAtrCue.setEstado(Estado.ACTIVO.getId());
				recAtrCue.setEsRequerido(item.getEsRequerido()?1:0);
				recAtrCue.setPoseeVigencia(item.getPoseeVigencia()?1:0);
				recAtrCue.setEsVisConDeu(item.getEsVisConDeu()?1:0);
				recAtrCue.setEsVisRec(item.getEsVisRec()?1:0);
				
				recAtrCue = this.createRecAtrCue(recAtrCue);
            	
		 	} else {
		 		// Solo se actualiza el valor del registro.
		 		List<RecAtrCue> listRecAtrCue = RecAtrCue.getListByIdRecAtr(this.getId(),idAtributo); 
		 		if(listRecAtrCue == null || listRecAtrCue.size() == 0)
		 			throw new DemodaServiceException("No se encontro el atributo valorizado del recurso a modificar.");
		 		recAtrCue = listRecAtrCue.get(0);
		 		recAtrCue.setValorDefecto(item.getValorString());
		 		recAtrCue.setEsRequerido(item.getEsRequerido()?1:0);
				recAtrCue.setPoseeVigencia(item.getPoseeVigencia()?1:0);
				recAtrCue.setEsVisConDeu(item.getEsVisConDeu()?1:0);
				recAtrCue.setEsVisRec(item.getEsVisRec()?1:0);
		 		recAtrCue = this.updateRecAtrCue(recAtrCue);
		 	}	
		}
		
		return definition;
 	}

 	/**
	 * Elimina  el atributo valorizado del Recurso para la creacion de cuenta.  
	 * @param RecursoDefinition con el valores a eliminar 
	 * @return el RecursoDefinition actualizado.
	 * @throws Exception 
	 */
 	public RecursoDefinition deleteRecAtrCueDefinition(RecursoDefinition definition) throws Exception {
 		
 		for (RecAtrCueDefinition item: definition.getListRecAtrCueDefinition()){
            Long idAtributo = item.getAtributo().getId(); 
            List<RecAtrCue> listRecAtrCue = RecAtrCue.getListByIdRecAtr(this.getId(),idAtributo);
            for(RecAtrCue recAtrCue: listRecAtrCue){
            	recAtrCue = this.deleteRecAtrCue(recAtrCue);
            	
            	if (recAtrCue.hasError()){
            		recAtrCue.passErrorMessages(definition);
            	}
        	}
 		}    
        return definition;
 	}

 	
	//--> RecGenCueAtrVa
 	/**
	 * Obtiene la definicion con sus valores del RecGenCueAtrVa.
	 * <p>Este metodo sirve para obtener los atributos y sus valores de un recurso para Habilitar la Creaci&oacute;n de Cuentas.
	 * @return el RecursoAtrDefinition cargado con los atributos y sus valores para la creacion de cuentas.
	 * @throws Exception 
	 */
	public RecursoDefinition getDefinitionRecGenCueAtrVaValue() throws Exception {
		RecursoDefinition recursoDefinition = new RecursoDefinition();
		List<RecGenCueAtrVa> listRecGenCueAtrVa =  RecGenCueAtrVa.getListByIdRecurso(this.getId());
		boolean genAtrDefCreado = false;
		
		for (RecGenCueAtrVa recGenCueAtrVa: listRecGenCueAtrVa ){
			// Por cada (RecGenCueAtrVa) busco un GenericAtrDefinition en recursoDefinition con el codigo atributo, si no existe lo creo
			GenericAtrDefinition genericAtrDefinition = recursoDefinition.getGenericAtrDefinitionById(recGenCueAtrVa.getAtributo().getId());

			if(genericAtrDefinition==null){
				genericAtrDefinition = new GenericAtrDefinition();
				genericAtrDefinition.setAtributo((AtributoVO) recGenCueAtrVa.getAtributo().toVO(2));
				genAtrDefCreado = true;
			}
			
			// Seteamos esMultivalor
			genericAtrDefinition.setEsMultivalor(recGenCueAtrVa.getEsMultivalor()==1?true:false);
			
			// Por ahora para los atributos del recurso, suponemos que todos tienen vigencia. (se setea poseevigencia=true)
			String valorVigencia = "";
			valorVigencia = genericAtrDefinition.getValor4VigView(recGenCueAtrVa.getStrValor()); 
			genericAtrDefinition.setPoseeVigencia(true);
			// Agrega un valor a la lista de vigencias
			genericAtrDefinition.addAtrValVig(valorVigencia, null, recGenCueAtrVa.getFechaDesde(), recGenCueAtrVa.getFechaHasta());
			
			if(DateUtil.isDateInRange(new Date(), recGenCueAtrVa.getFechaDesde(), recGenCueAtrVa.getFechaHasta())){
				genericAtrDefinition.addValor(recGenCueAtrVa.getStrValor(), null, recGenCueAtrVa.getFechaDesde(), recGenCueAtrVa.getFechaHasta());
			}
			
			// Si el GenericAtrDefinition no existia y lo cree, ahora lo agrego al recursoDefinition.
			if(genAtrDefCreado == true){
				recursoDefinition.addGenericAtrDefinition(genericAtrDefinition);
				genAtrDefCreado = false;
			}
		}
		return recursoDefinition;
	}
	
	/**
	 * Obtiene la definicion de un unico atributo con sus valores del RecGenCueAtrVa.
	 * <p>Este metodo sirve para obtener un unico atributo y sus valores de un recurso para Habilitar la Creaci&oacute;n de Cuentas.
	 * @param idAtributo id del Atributo a cargar.
	 * @return el RecursoDefinition cargado con el atributo y sus valores cuyo id es el del parametro.
	 * @throws Exception 
	 */
	public RecursoDefinition getDefinitionRecGenCueAtrVaValue(Long idAtributo) throws Exception {
		RecursoDefinition recursoDefinition = new RecursoDefinition();
		List<RecGenCueAtrVa> listRecGenCueAtrVa =  RecGenCueAtrVa.getListByIdRecAtr(this.getId(),idAtributo);
		
		// Creo la definicon
		GenericAtrDefinition genericAtrDefinition = new GenericAtrDefinition();
		genericAtrDefinition.setAtributo((AtributoVO) Atributo.getById(idAtributo).toVO(2));
		
		// Seteo el valor o los valores
		for (RecGenCueAtrVa item: listRecGenCueAtrVa ){
			
			if(DateUtil.isDateInRange(new Date(), item.getFechaDesde(), item.getFechaHasta())){
				genericAtrDefinition.addValor(item.getStrValor(), null, item.getFechaDesde(), item.getFechaHasta());
			}
			String valorVigencia = "";
			valorVigencia = genericAtrDefinition.getValor4VigView(item.getStrValor());
			
			// Seteamos esMultivalor
			genericAtrDefinition.setEsMultivalor(item.getEsMultivalor()==1?true:false);
			
			genericAtrDefinition.setPoseeVigencia(true);
			// Agrega un valor a la lista de vigencias
			genericAtrDefinition.addAtrValVig(valorVigencia, null, item.getFechaDesde(), item.getFechaHasta() );
		}	
		
		genericAtrDefinition.orderListAtrValVigByVal();
		
		recursoDefinition.addGenericAtrDefinition(genericAtrDefinition);
	 
		return recursoDefinition;
	}
	
	/**
	 * Inserta el valor y/o las vigencias de un atributo valorizado del 
	 * Recurso para Habilitar la Creaci&oacute;n de Cuentas. 
	 * @param RecursoDefinition con los valores a insertar
	 * @return el RecursoDefinition con los atributos insertados
	 * @throws Exception 
	 */
	public RecursoDefinition createRecGenCueAtrVaDefinition(RecursoDefinition definition) throws Exception {

		// Recorremos los Atributos para la definition de Recurso, y por cada uno insertamos un registro en RecGenCueAtrVa
		for (GenericAtrDefinition item: definition.getListGenericAtrDefinition()){
			
			RecGenCueAtrVa recGenCueAtrVa = new RecGenCueAtrVa();            
            
           	recGenCueAtrVa.setRecurso(this);
			recGenCueAtrVa.setAtributo(Atributo.getById(item.getAtributo().getId()));
			recGenCueAtrVa.setStrValor(item.getValorString());
			recGenCueAtrVa.setFechaDesde(item.getFechaDesde());
			recGenCueAtrVa.setFechaHasta(item.getFechaHasta());
			recGenCueAtrVa.setEsMultivalor(item.getEsMultivalor()?1:0);
			recGenCueAtrVa.setEstado(Estado.ACTIVO.getId());
			
			recGenCueAtrVa = this.createRecGenCueAtrVa(recGenCueAtrVa);
		}
		
		return definition;
 	}
	
	/**
	 * Actualiza el valor y/o las vigencias de un atributo valorizado del 
	 * Recurso para Habilitar la Creaci&oacuten de Cuentas. El metodo actualiza el valor y las vigencias en caso
	 * de ser un atributo que posee vigencias.
	 * @param RecursoDefinition con los valores a actualizar
	 * @return el RecursoDefinition actualizado
	 * @throws Exception 
	 */
 	public RecursoDefinition updateRecGenCueAtrVaDefinition(RecursoDefinition definition) throws Exception {
 		// Por cada GenericAtrDefinition
		// si poseeVigencia:
		//      se inserta el nuevo registro de recGenCueAtrVa y se modifica la fecha hasta del anterior si era nula. 
		// si no posee vigencia:
		// 		obtener recGenCueAtrVa, modificar valor, llamar updateRecGenCueAtrVam
 		
  	    //  Este for es loco, porque hay un solo elemento.
		for (GenericAtrDefinition itemUpdate: definition.getListGenericAtrDefinition()){
			
			RecGenCueAtrVa recGenCueAtrVa = null;            
            
            Long idAtributo = itemUpdate.getAtributo().getId(); 
            
            // Si es multicalor el tratamiento es distinto
            if (itemUpdate.getEsMultivalor()){
            	log.debug(" Es multivalor ");
            	
            	// Recuperamos la deficion Vigente actualmente	
            	RecursoDefinition definicionActual = this.getDefinitionRecGenCueAtrVaValue(idAtributo);
            	
            	boolean existe = false;
            	
            	// Este for es loco, porque hay un solo elemento.
            	for(GenericAtrDefinition itemActual: definicionActual.getListGenericAtrDefinition()){
            		
            		// Chequeamos si se agregaron valores.
            		for (String cadaValor: itemUpdate.getMultiValor() ){
            			
            			//log.debug("cadaValor: " + cadaValor);
            		
            			for (String cadaValorActual: itemActual.getMultiValor() ){
            				            				
            				if (cadaValorActual.equals(cadaValor)){
            					existe = true; 
            					break;
            				}
            			}
            			
            			if (!existe){
            				log.debug("Agregar valor: insert " + cadaValor );
            				
            				// Insertar un registro nuevo             	
        	            	recGenCueAtrVa = new RecGenCueAtrVa();
        	            	recGenCueAtrVa.setRecurso(this);
        					recGenCueAtrVa.setAtributo(Atributo.getById(idAtributo));
        					recGenCueAtrVa.setStrValor(itemUpdate.getValorString());
        					recGenCueAtrVa.setFechaDesde(itemUpdate.getFechaDesde());
        					recGenCueAtrVa.setEstado(Estado.ACTIVO.getId());
        					recGenCueAtrVa.setEsMultivalor(1);
        					
        					recGenCueAtrVa = this.createRecGenCueAtrVa(recGenCueAtrVa);
            			}
            			
            			existe = false;
            		}
            		
            		
            		// Chequeamos si se quitaron valores.
            		for (String cadaValorActual: itemActual.getMultiValor() ){
            			
            			//log.debug("cadaValorActual: " + cadaValorActual);

            			for (String cadaValor: itemUpdate.getMultiValor() ){
            				            				
            				if (cadaValorActual.equals(cadaValor)){
            					existe = true; 
            					break;
            				}
            			}
            			
            			if (!existe){
            				log.debug("Fin vig valor: " + cadaValorActual + 
            						  " fechaHasta: " + itemUpdate.getFechaHasta() );
            				
            				// Obtenemos el registro vigente
            				// Viene ordenado por fechaDesde desc y tomamoa el primero
        			 		List<RecGenCueAtrVa> listRecGenCueAtrVa = RecGenCueAtrVa.getListByIdRecAtrValor(this.getId(),idAtributo, cadaValorActual); 
        			 		if(listRecGenCueAtrVa == null || listRecGenCueAtrVa.size() == 0)
        			 			throw new DemodaServiceException("No se encontro el atributo valorizado del recurso a modificar.");
        			 		recGenCueAtrVa = listRecGenCueAtrVa.get(0);
        			 		
        			 		recGenCueAtrVa.setFechaHasta(itemUpdate.getFechaDesde());
        			 		recGenCueAtrVa = this.updateRecGenCueAtrVa(recGenCueAtrVa);
            				
            			}

            			existe = false;
            		}
            		
            	}

            	
            } else {
            	log.debug(" No es multivalor ");
         
	            // Recupero el atributo que tenga fechaHasta null si existe.
	            RecGenCueAtrVa recGenCueAtrVaAbierto = RecGenCueAtrVa.getAbiertoByIdRecGenCueAtrVa(this.getId(),idAtributo);
	            
	            if (itemUpdate.getPoseeVigencia()){
	
	                if (recGenCueAtrVaAbierto != null) {
	                	// seteo de la fecha Hasta del registro con fechaHasta null con el fechaDesde del nuevo 
	                	// si tiene fechaDesde menor que que el nuevo. 
	                	if(DateUtil.isDateBefore(recGenCueAtrVaAbierto.getFechaDesde(),itemUpdate.getFechaDesde())){
	                		recGenCueAtrVaAbierto.setFechaHasta(DateUtil.addDaysToDate(itemUpdate.getFechaDesde(), -1));
	            		    recGenCueAtrVaAbierto = this.updateRecGenCueAtrVa(recGenCueAtrVaAbierto);
	                	}
	                }
	                
	            	// Insertar un registro nuevo             	
	            	recGenCueAtrVa = new RecGenCueAtrVa();
	            	recGenCueAtrVa.setRecurso(this);
					recGenCueAtrVa.setAtributo(Atributo.getById(idAtributo));
					recGenCueAtrVa.setStrValor(itemUpdate.getValorString());
					recGenCueAtrVa.setFechaDesde(itemUpdate.getFechaDesde());
					recGenCueAtrVa.setFechaHasta(itemUpdate.getFechaHasta());
					recGenCueAtrVa.setEstado(Estado.ACTIVO.getId());
					recGenCueAtrVa.setEsMultivalor(0);
					
					recGenCueAtrVa = this.createRecGenCueAtrVa(recGenCueAtrVa);
	            	
			 	} else {
			 		// Solo se actualiza el valor del registro.
			 		List<RecGenCueAtrVa> listRecGenCueAtrVa = RecGenCueAtrVa.getListByIdRecAtr(this.getId(),idAtributo); 
			 		if(listRecGenCueAtrVa == null || listRecGenCueAtrVa.size() == 0)
			 			throw new DemodaServiceException("No se encontro el atributo valorizado del recurso a modificar.");
			 		recGenCueAtrVa = listRecGenCueAtrVa.get(0);
			 		recGenCueAtrVa.setStrValor(itemUpdate.getValorString());
			 		recGenCueAtrVa = this.updateRecGenCueAtrVa(recGenCueAtrVa);
			 	}
			}    
		}
		
		return definition;
 	}

 	/**
	 * Elimina  el atributo valorizado del Recurso para Habilitar la Creaci&oacute;n de Cuentas.  
	 * @param RecursoDefinition con el valores a eliminar 
	 * @return el RecursoDefinition actualizado.
	 * @throws Exception 
	 */
 	public RecursoDefinition deleteRecGenCueAtrVaDefinition(RecursoDefinition definition) throws Exception {
 		
 		for (GenericAtrDefinition item: definition.getListGenericAtrDefinition()){
            Long idAtributo = item.getAtributo().getId(); 
            List<RecGenCueAtrVa> listRecGenCueAtrVa = RecGenCueAtrVa.getListByIdRecAtr(this.getId(),idAtributo);
            for(RecGenCueAtrVa recGenCueAtrVa: listRecGenCueAtrVa){
        		this.deleteRecGenCueAtrVa(recGenCueAtrVa);
        	}
 		}    
        return definition;
 	}

  	public RecursoVO toVOWithCategoria() throws Exception{
		RecursoVO recursoVO = (RecursoVO) this.toVO(false); // false para que no traiga las listas 
		recursoVO.setCategoria((CategoriaVO) this.getCategoria().toVO(false));
		return recursoVO;
 	}
 	
 	public RecursoVO toVOLightForPDF() throws Exception{
 		RecursoVO recursoVO = (RecursoVO) this.toVO(0, false);
 		 		
 		return recursoVO;
 	}
 	
 	/**
 	 *  Verifica si existe algún periodo emitido posterior a la fecha pasada como parametro.
 	 * @param fecha
 	 * @return true o false
 	 */
 	public boolean existeEmisionPosterior(Date fecha){
 		if(this.getUltPerEmi() == null)
 			return false;
 		try{ 		
 			Date fechaUltimaEmision = DateUtil.getDate(this.getUltPerEmi(), DateUtil.YYYYMM_MASK);
 			return DateUtil.isDateBefore(fecha, fechaUltimaEmision); 			
 		}catch(Exception e){
 			return false;
 		}
 	}
 	
 	public Boolean getEsCategoriaCdM(){
 		return this.getCategoria().getEsCategoriaCdM();
 	}

 	public Boolean getEsObraPavimento() {
 		return codRecurso.equals(COD_RECURSO_OdP);
 	}
 	
 	public Boolean getEsObraGas() {
 		return codRecurso.equals(COD_RECURSO_OdG);
 	}
 	
 	/**
	 * Obtiene la Fecha de Vencimiento para Deuda o Recibo de Deuda. Para esto verifica que sea un dia habil, si no
	 * busca el proximo dia habil. Ademas verifica si existe una prorroga vigente para el Recurso asociado y en dicho
	 * caso recalcula la fecha.
	 * 
	 * @param fechaVencimiento
	 * @return fechaVencimiento
	 */
	public Date obtenerFechaVencimientoDeudaORecibo(Date fechaVencimiento){
		Date nuevaFecha = null;
		nuevaFecha = Feriado.nextDiaHabil(fechaVencimiento);
		return nuevaFecha;
		/* TODO: Cambiar por lo siguiente cuando se implementen las prorrogas del Recurso.
		Date nuevaFecha = fechaVencimiento;
		RecProrroga recProrroga = DefDAOFactory.getRecProrrogaDAO().getVigenteByRecursoYFecVen(this, fechaVencimiento);
		if(recProrroga != null)
			nuevaFecha = recProrroga.getFecVtoNue();
		nuevaFecha = Feriado.nextDiaHabil(nuevaFecha);
		return nuevaFecha;
		 */
	}

	/**
	 * Obtiene la Fecha de Vencimiento para Deuda o Recibo de Deuda. Para esto verifica que sea un dia habil, si no
	 * busca el proximo dia habil. Ademas verifica si existe una prorroga vigente para el Recurso asociado y en dicho
	 * caso recalcula la fecha.  (Usa el mapa de Feriado)
	 * 
	 * @param fechaVencimiento
	 * @param mapFeriado
	 * @return fechaVencimiento
	 */
	public Date obtenerFechaVencimientoDeudaORecibo(Date fechaVencimiento,Map<Date, Feriado> mapFeriado){
		Date nuevaFecha = null;
		nuevaFecha = Feriado.nextDiaHabilUsingMap(fechaVencimiento, mapFeriado);
		return nuevaFecha;
		/* TODO: Cambiar por lo siguiente cuando se implementen las prorrogas del Recurso.
		Date nuevaFecha = fechaVencimiento;
		RecProrroga recProrroga = DefDAOFactory.getRecProrrogaDAO().getVigenteByRecursoYFecVen(this, fechaVencimiento);
		if(recProrroga != null)
			nuevaFecha = recProrroga.getFecVtoNue();
		nuevaFecha = Feriado.nextDiaHabil(nuevaFecha);
		return nuevaFecha;
		 */
	}
	
	/**
	 * Devuelve un RecCon buscado por codigo en la lista de reccon del recurso.
	 * 
	 * @param cod
	 * @return
	 * @throws Exception
	 */
	public RecCon getRecConByCodigo(String cod) throws Exception {
		return RecCon.getByIdRecursoAndCodigo(this.getId(), cod);
	}
	
	/**
	 * Devuelve un RecCon buscado por orden de visualizacion en la lista de reccon del recurso.
	 * 
	 * @param cod
	 * @return
	 * @throws Exception
	 */
	public RecCon getRecConByOrden(int nroOrden) throws Exception {
		return RecCon.getByIdRecursoAndOrden(this.getId(), nroOrden);
	}
	
	/**
	 * Retorna true si y solo si el atributo
	 * es un atributo de cuenta
	 */
	public boolean esAtributoCuenta(Atributo atributo) {
		for (RecAtrCue recAtrCue: this.getListRecAtrCue()) {
			if (recAtrCue.getAtributo().equals(atributo))
				return true;
		}
				
		return false;
	}
	
	/**
	 * Implementacion de metodos de la superclase para utilizar isVigente()
	 * 
	 */
	public Date getFechaInicioVig(){
		if (this.getEstado().intValue() == Estado.CREADO.getId()) return null;
		return getFechaAlta();
	}
	
	public Date getFechaFinVig(){
		return getFechaBaja();
	}

		public void setFecUltEnvPro(Date fecUltEnvPro) {
		this.fecUltEnvPro = fecUltEnvPro;
	}

	public Date getFecUltEnvPro() {
		return fecUltEnvPro;
	}

		/**
	 * Retorna el algorimto de emision vigente a 
	 * la fecha pasada como parametro
	 * 
	 * @param  fecha
	 * @return algoritmo
	 */
	public String getCodigoEmisionBy(Date fecha) throws Exception {
		CodEmi codEmi = DefDAOFactory.getCodEmiDAO().getCodEmiActivoBy(this, fecha);

		return codEmi.getCodigo();
	}

	/**
	 * Retorna la Clasificacion de Deuda marcada como original
	 * a la fecha pasada como paramtro 
	 * 
	 * @param  fecha
	 * @return recClaDeu
	 */
	public RecClaDeu getRecClaDeuOriginal(Date fecha) {
		 return DefDAOFactory.getRecClaDeuDAO().getRecClaDeuOriginal(this, fecha);
	}

	public List<RecAtrCueEmi> getListRecAtrCueEmiActivosBy(Date fecha) throws Exception {
		 return DefDAOFactory.getRecAtrCueEmiDAO().getListActivosBy(this, fecha);
	}
		
	/**
	 * Retorna true si y solo si el argumento representa un 
	 * periodo valido segun la periodicidad de deuda definida 
	 * para el recurso
	 * 
	 * @param periodo
	 * @return boolean
	 */
	public boolean validatePeriodo(Integer periodo) {

		if (this.periodoDeuda != null) {
			// Si no tiene periodicidad Esporadica
			Long idPeriodoDeuda = this.getPeriodoDeuda().getId();
			if (!idPeriodoDeuda.equals(PeriodoDeuda.ESPORADICO)) {
				Integer cantPeriodos = this.getPeriodoDeuda().getPeriodos();
				return (1 <= periodo) && (periodo <= 12/cantPeriodos);	
			}
		}
		
		return false;
	}
		
	public String getInfo() {
		return "DES: " + getRecInfo() + " CONCEPTOS: " + getConcInfo() + " PARTIDAS: " + getDisParInfo();
	}
	
	public String getRecInfo() {
		String recInfo="";
		try {
			recInfo=this.getCategoria().getDesCategoria() + ": " + this.getDesRecurso();
			
		} catch (Exception e) {
			recInfo="Error al obtener descripcion del recurso";
		}
		return recInfo;
	}
	
	
	public String getConcInfo() {
		String concInfo="";
		try {
			String coma="";
			for(RecCon recCon: getListRecCon()) {
				concInfo += coma + recCon.getAbrRecCon();
				coma=", ";
			}
			
		} catch (Exception e) {
			concInfo="error al obtener informacion de conceptos del recurso";
		}
		return concInfo;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getDisParInfo() {
		String disParInfo="";
		
		try {
			// Buscar Distribuidor de Partidas (DisPar) para para recurso.
			DisPar disPar = null;
			ViaDeuda viaDeuda = ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN);
			List<DisParRec> listDisParRec = DisParRec.getListByRecursoViaDeudaFecha(this, viaDeuda, new Date());
			
			if(ListUtil.isNullOrEmpty(listDisParRec)) {
				disParInfo="No se encuentra distribuidor para el Recurso";
			} else if (listDisParRec.size() > 1) {
				disParInfo="No se encuentra un unico distribuidor para el Recurso";
	
			}else{
				disPar = listDisParRec.get(0).getDisPar();
			}
			
			// Si se encuentra, verifica si puede distribuir todos los recursos
			if(disPar != null){
				disParInfo += disPar.getDesDisPar();
				
				for(RecCon recCon: this.getListRecCon() ){
					// Por cada concepto, buscar detalles del disPar (disParDet)
					List<DisParDet> listDisParDet = DisParDet.getListByDisParYidTipoImporteYRecCon(disPar, TipoImporte.ID_CAPITAL, recCon);
					
					for(DisParDet disParDet: listDisParDet) {
						disParInfo += "[ " + disParDet.getTipoImporte().getDesTipoImporte() + "-" + disParDet.getRecCon().getDesRecCon() + "-" + disParDet.getPartida().getCodDesPartida() + "-" + disParDet.getPorcentaje().toString() +  "]";
					}
				}
				
			}
	
		} catch (Exception e) {
			disParInfo = "Ocurrio un error en la deteccion del distribuidor. Consulte con el administrador del sistema";
		}
		return disParInfo;
	}
	
	
	/**
	 * Devuelve un RecursoDefinition, lleno en su ListGenericAtrDefinition con los datos 
	 * RecAtrCueEmi(atributos a valorizar al momento de la emision) vigentes a la fecha recibida.
	 * 
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
	public RecursoDefinition getRecAtrCueEmiDefinition(Date fecha) throws Exception {
		
		RecursoDefinition recAtrCueEmiDefinition = new RecursoDefinition(); 
		List<RecAtrCueEmi> listRecAtrCueEmi = this.getListRecAtrCueEmiActivosBy(fecha);
		
		for (RecAtrCueEmi recAtrCueEmi: listRecAtrCueEmi){
			GenericAtrDefinition genAtrDef = new GenericAtrDefinition();
			genAtrDef.setAtributo((AtributoVO) recAtrCueEmi.getAtributo().toVO(3));
			// Siempre son Requeridos
			genAtrDef.setEsRequerido(true);
			recAtrCueEmiDefinition.getListGenericAtrDefinition().add(genAtrDef);
		}
		
		return recAtrCueEmiDefinition;
	}
	
	
	public String getRecInfoRaw() {
		String info="";
		String enter="\n";
		
		try {

			info  = enter + "==============================================" + enter;
			info += "Código: " + this.getCodRecurso() + enter;
			info += "Descripción: " + this.getDesRecurso() + enter;
			info += "Categoría: " + this.getCategoria().getDesCategoria() + enter;
			info += "Fecha Alta: " + this.getFechaAlta() + enter;
			info += "Fecha Baja: " + this.getFechaBaja() + enter;
			
			info += "-----------------" + enter;
			info += "Periodicidad: " + this.getPeriodoDeuda().getDesPeriodoDeuda() + enter;
			info += "Permite gestionar la deuda NO vencida: " + this.getGesDeuNoVen() + enter;
			info += "La deuda es exigible una vez vencida: " + this.getDeuExiVen() + enter;
			info += "Permite realizar la gestión de cobranza: " + this.getGesCobranza() +  enter;
			
			info += "-----------------" + enter;
			info += "Permite emisiones masivas: " + this.getPerEmiDeuMas() + enter;
			info += "Permite emisiones puntuales: " + this.getPerEmiDeuPuntual() + enter;
			info += "Permite emisiones extraordinarias: " + this.getPerEmiDeuExt() + enter;
			info += "La deuda generada sigue a los titulares al momento de la emisión: " + this.getEsDeudaTitular() + enter;
			info += "Permite impresiones masivas: " + "no implementado";
			
			info += "Vencimiento: ";
				if (this.getVencimiento() == null)
					info += "null" + enter;
				else
					info += this.getVencimiento().getDesVencimiento() + enter;
				
			info += "Atributo de asentamiento: ";
				if (this.getAtributoAse() == null)
					info += "null" + enter;
				else
					info += this.getAtributoAse().getCodAtributo() + enter;
				
			info += "-----------------" + enter;
			info += "Tipo Objeto Imponible: ";
				if (this.getTipObjImp() == null )
					info += "null" + enter;
				else
					info += this.getTipObjImp().getCodTipObjImp() + " (copie la config de siattest) " + enter;
				
			info += "-----------------" + enter;
			info += "Forma de generar el Id de Cuenta: ";
				if (this.getGenCue()==null)
					info += "null" + enter;
				else
					info += this.getGenCue().getCodGenCue() + enter;
				
			info += "Forma de generar el Código de Gestión: ";
				if (this.getGenCodGes()==null)
					info += "null" + enter;
				else
					info += this.getGenCodGes().getCodGenCodGes() + enter;

			info += "-----------------" + enter;
			info += "Permite alta manual: " + this.getAltaCtaManual() + enter;
			info += "Permite baja manual: " + this.getBajaCtaManual() + enter;
			info += "Permite modificación manual de Titulares: " + this.getModiTitCtaManual() + enter;
			
			info += "-----------------" + enter;
			info += "Envia Deuda a Gestión Judicial: " + this.getEnviaJudicial() + enter;
			info += "Criterio de asignación a procurador: ";
			 	if (this.getCriAsiPro()==null)
			 		info += "null" + enter;
			 	else
			 		info += this.getCriAsiPro().getDesCriAsiPro() + enter;
			
		    info += "-----------------" + enter;
		    info += "Conceptos del recurso" + enter;
			for (RecCon recCon : this.getListRecCon()) {
				info += "   " + recCon.getCodRecCon() + " - " + recCon.getDesRecCon() + " - " + recCon.getAbrRecCon() + " - orden:" + recCon.getOrdenVisualizacion() + " - " + 
				recCon.getFechaDesde() + " - " + recCon.getFechaHasta() + enter;
			}
			
		    info += "-----------------" + enter;
		    info += "Clasificación de deuda" + enter;
		    for (RecClaDeu recClaDeu: this.getListRecClaDeu()) {
		    	info += "   " + recClaDeu.getDesClaDeu() + " - " + recClaDeu.getAbrClaDeu() + " - esorig: " + recClaDeu.getEsOriginal() + " act.deu: " + recClaDeu.getActualizaDeuda() +
		    	" - " + recClaDeu.getFechaDesde() + " - " + 
		    	recClaDeu.getFechaHasta() + enter;
		    }
		    
		    info += "-----------------" + enter;
		    info += "Atributos a valorizar al crear cuenta" + enter;
		    for (RecAtrCue recAtrCue : this.getListRecAtrCue()) {
		    	info += "   atr: " + recAtrCue.getAtributo().getCodAtributo() + " . " + recAtrCue.getAtributo().getDesAtributo() + " . " + recAtrCue.getAtributo().getTipoAtributo().getCodTipoAtributo();
		    	
		    	if (recAtrCue.getAtributo().getDomAtr() != null)
		    		info += " (dom: " + recAtrCue.getAtributo().getDomAtr().getCodDomAtr() + ")";
		    	
		    	info += " - rec: " + recAtrCue.getEsRequerido() + " - vis. deu: " +
		    	recAtrCue.getEsVisConDeu() + " - vis. rec: " + recAtrCue.getEsVisConDeu() + " - pos vig: " + recAtrCue.getPoseeVigencia() + 
		    	" - val.def: " + recAtrCue.getValorDefecto() + " - fec.des: " + recAtrCue.getFechaDesde() + " - fec.has: " + recAtrCue.getFechaHasta() +  enter;
		    }
		    
		    info += "-----------------" + enter;
		    info += "Atributos a valorizar en la emisión" + enter;
		    for (RecAtrCueEmi recCueEmi : this.getListRecAtrCueEmi()) {
		    	info += "   atr: " + recCueEmi.getAtributo().getCodAtributo() + " . " + recCueEmi.getAtributo().getDesAtributo() + " . " + recCueEmi.getAtributo().getTipoAtributo().getCodTipoAtributo();
	
		    	if (recCueEmi.getAtributo().getDomAtr() != null)
		    		info += " (dom: " + recCueEmi.getAtributo().getDomAtr().getCodDomAtr() + ")";
		    	
		    	info += " - vis. deu: " + recCueEmi.getEsVisConDeu() + " - vis. rec: " + recCueEmi.getEsVisRec() + " - fec.des: " + recCueEmi.getFechaDesde() + " - fec.has: " + recCueEmi.getFechaHasta() + enter; 
		    }

		    
		    
		} catch (Exception e) {
			info = "Ocurrio un error en la construccion del string:" + e.getMessage();
		}
		return info;
	}
	
	
	
	/**
	 * Devuelve el ServicioBanco para el cual este recurso se encuentre vigente en listSerBanRec.
	 * 
	 */
	public ServicioBanco obtenerServicioBanco(){
		
		ServicioBanco ret = null;
		
		List<ServicioBanco> listServicioBanco = ServicioBanco.getVigentes(this);
		
		if (listServicioBanco != null && !listServicioBanco.isEmpty()){
			ret = listServicioBanco.get(0);
		}
		
		return ret;
	}
	
	/**
	 * Devuelve true o false si el recurso permite Crea Cuenta/Emitir Deuda para el 
	 * Area del usuario logueado. 
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean permiteCrearEmitir() throws Exception {
		
		UserContext userContext = DemodaUtil.currentUserContext();	
		
		RecursoArea recursoArea = RecursoArea.getByRecursoArea(this.getId(), userContext.getIdArea());
		
		if (recursoArea != null){
			if (recursoArea.getPerCreaEmi().intValue() == 1){
				log.debug("@@ permiteCrearEmitir: true");
				return true;
			} else {
				log.debug("@@ permiteCrearEmitir: false");
				return false;
			}
		} else {
			log.debug("@@ permiteCrearEmitir: true por no existir Recurso Area");
			return true;			
		}
	}
}
