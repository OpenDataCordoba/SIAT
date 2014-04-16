//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;
import ar.gov.rosario.siat.gde.iface.model.CriAsiProVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Recurso
 * @author tecso
 *
 */
public class RecursoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "recursoVO";
	
	// IMPORTANTE: estas constantes estan repetidas en el Bean Recurso
	public static final String COD_RECURSO_TGI = "TGI";
	public static final String COD_RECURSO_OdP = "OdP";
	public static final String COD_RECURSO_OdG = "OdG";
	public static final String COD_RECURSO_PLANOS = "PLANOS";
	
	public static final String COD_RECURSO_GENERICO = "GEN";
	
	public static final String COD_RECURSO_DReI = "DReI";
	public static final String COD_RECURSO_ETuR = "ETuR";
	public static final String COD_RECURSO_DPUB="DERPUB";
	
	public static final String COD_RECURSO_PAGCYQ="PAGCYQ";
	
	private CategoriaVO categoria = new CategoriaVO();
	private String codRecurso;
	private String desRecurso;
	private SiNo esAutoliquidable = SiNo.OpcionSelecionar;
	private Date fecEsAut;
	private String fecEsAutView = "";
	private SiNo esFiscalizable = SiNo.OpcionSelecionar;
	private Date fecEsFis;
	private String fecEsFisView = "";
	private SiNo esLibreDeuda = SiNo.OpcionSelecionar;
	private Date fecDesIntDiaBan;
	private String fecDesIntDiaBanView = "";
	private SiNo gesDeuNoVen = SiNo.OpcionSelecionar;
	private SiNo deuExiVen = SiNo.OpcionSelecionar;
	private SiNo gesCobranza = SiNo.OpcionSelecionar;
	private SiNo perEmiDeuMas = SiNo.OpcionSelecionar;
	private AtributoVO atrEmiMas = new AtributoVO();
	private SiNo perEmiDeuPuntual = SiNo.OpcionSelecionar;
	private SiNo perEmiDeuExt = SiNo.OpcionSelecionar;
	private VencimientoVO vencimiento = new VencimientoVO();
	private SiNo perImpMasDeu = SiNo.OpcionSelecionar;
	private FormularioVO formImpMasDeu = new FormularioVO(); 
	private SiNo genNotif = SiNo.OpcionSelecionar;
	private FormularioVO formNotif = new FormularioVO();
	private SiNo esDeudaTitular = SiNo.OpcionSelecionar;
	private TipObjImpVO tipObjImp = new TipObjImpVO();
	private SiNo esPrincipal = SiNo.OpcionSelecionar;
	private SiNo altaCtaPorIface = SiNo.OpcionSelecionar;
	private SiNo bajaCtaPorIface = SiNo.OpcionSelecionar;
	private SiNo modiTitCtaPorIface = SiNo.OpcionSelecionar;
	private RecursoVO recPri;
	private SiNo altaCtaPorPri = SiNo.OpcionSelecionar;
	private SiNo bajaCtaPorPri = SiNo.OpcionSelecionar;
	private SiNo modiTitCtaPorPri = SiNo.OpcionSelecionar;
	private SiNo altaCtaManual = SiNo.OpcionSelecionar;
	private SiNo bajaCtaManual = SiNo.OpcionSelecionar;
	private SiNo modiTitCtaManual = SiNo.OpcionSelecionar;
	private SiNo enviaJudicial = SiNo.OpcionSelecionar;
	private CriAsiProVO criAsiPro = new CriAsiProVO(); 
	private GenCueVO genCue = new GenCueVO();
	private GenCodGesVO genCodGes = new GenCodGesVO();
	private String ultPerEmi;
	private Date fechaAlta;
	private String fechaAltaView = "";
	private Date fechaBaja;
	private String fechaBajaView = "";
	private String fecUltEnvProView = "";
	private AtributoVO atributoAse = new AtributoVO();
	private PeriodoDeudaVO periodoDeuda = new PeriodoDeudaVO();
	private boolean declaraAdicionales=false;
	private Boolean esCategoriaCdM = Boolean.FALSE;
	private SiNo genPadFir = SiNo.NO;
	private FormularioVO formPadFir = new FormularioVO();
	
	private Date fecUltPag;
	private Date fecUltEnvPro;
	
	// Listas de Entidades Relacionadas con Recurso y otra Entidad
	private List<RecAtrValVO> listRecAtrVal = new ArrayList<RecAtrValVO>();
	private List<RecConVO> listRecCon = new ArrayList<RecConVO>();
	private List<RecClaDeuVO> listRecClaDeu = new ArrayList<RecClaDeuVO>();
	private List<RecGenCueAtrVaVO> listRecGenCueAtrVa = new ArrayList<RecGenCueAtrVaVO>();
	private List<RecEmiVO> listRecEmi = new ArrayList<RecEmiVO>();
	private List<RecAtrCueEmiVO> listRecAtrCueEmi = new ArrayList<RecAtrCueEmiVO>();
	private List<RecAtrCueVO> listRecAtrCue = new ArrayList<RecAtrCueVO>();
	private List<RecConADecVO>listRecConADec = new ArrayList<RecConADecVO>();
	private List<RecMinDecVO>listRecMinDec = new ArrayList<RecMinDecVO>();
	private List<RecAliVO>listRecAli = new ArrayList<RecAliVO>();
	private List<RecursoAreaVO>listRecursoArea = new ArrayList<RecursoAreaVO>();
	
	// Constructores
	public RecursoVO() {
		super();
	}

	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public RecursoVO(int id, String desRecurso) {
		super();
		setId(new Long(id));
		setDesRecurso(desRecurso);
	}
	
	// Getters y Setters
	public CategoriaVO getCategoria() {
		return categoria;
	}
	public void setCategoria(CategoriaVO categoria) {
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
	public SiNo getEsAutoliquidable(){
		return esAutoliquidable;
	}
	public void setEsAutoliquidable(SiNo esAutoliquidable){
		this.esAutoliquidable = esAutoliquidable;
	}
	public Date getFecEsAut(){
		return fecEsAut;
	}
	public void setFecEsAut(Date fecEsAut){
		this.fecEsAut = fecEsAut;
		this.fecEsAutView = DateUtil.formatDate(fecEsAut, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFecEsAutView(){
		return fecEsAutView;
	}
	public void setFecEsAutView(){
	}
	public SiNo getEsFiscalizable(){
		return esFiscalizable;
	}
	public void setEsFiscalizable(SiNo esFiscalizable){
		this.esFiscalizable = esFiscalizable;
	}
	public Date getFecEsFis(){
		return fecEsFis;
	}
	public String getFecEsFisView(){
		return fecEsFisView;
	}
	public void setFecEsFisView(){
	}
	public void setFecEsFis(Date fecEsFis){
		this.fecEsFis = fecEsFis;
		this.fecEsFisView = DateUtil.formatDate(fecEsFis, DateUtil.ddSMMSYYYY_MASK);
	}
	public SiNo getEsLibreDeuda(){
		return esLibreDeuda;
	}
	public void setEsLibreDeuda(SiNo esLibreDeuda){
		this.esLibreDeuda = esLibreDeuda;
	}
	public Date getFecDesIntDiaBan(){
		return fecDesIntDiaBan;
	}
	public void setFecDesIntDiaBan(Date fecDesIntDiaBan){
		this.fecDesIntDiaBan = fecDesIntDiaBan;
		this.fecDesIntDiaBanView = DateUtil.formatDate(fecDesIntDiaBan, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFecDesIntDiaBanView(){
		return fecDesIntDiaBanView;
	}
	public void setFecDesIntDiaBanView(){
	}
	public SiNo getGesDeuNoVen(){
		return gesDeuNoVen;
	}
	public void setGesDeuNoVen(SiNo gesDeuNoVen){
		this.gesDeuNoVen = gesDeuNoVen;
	}
	public SiNo getDeuExiVen(){
		return deuExiVen;
	}
	public void setDeuExiVen(SiNo deuExiVen){
		this.deuExiVen = deuExiVen;
	}
	public SiNo getGesCobranza(){
		return gesCobranza;
	}
	public void setGesCobranza(SiNo gesCobranza){
		this.gesCobranza = gesCobranza;
	}
	public SiNo getPerEmiDeuMas() {
		return perEmiDeuMas;
	}
	public void setPerEmiDeuMas(SiNo perEmiDeuMas) {
		this.perEmiDeuMas = perEmiDeuMas;
	}

	public AtributoVO getAtrEmiMas() {
		return atrEmiMas;
	}
	
	public void setAtrEmiMas(AtributoVO atrEmiMas) {
		this.atrEmiMas = atrEmiMas;
	}
	
	public SiNo getPerEmiDeuExt() {
		return perEmiDeuExt;
	}
	
	public SiNo getPerEmiDeuPuntual() {
		return perEmiDeuPuntual;
	}
	
	public void setPerEmiDeuPuntual(SiNo perEmiDeuPuntual) {
		this.perEmiDeuPuntual = perEmiDeuPuntual;
	}
	
	public void setPerEmiDeuExt(SiNo perEmiDeuExt) {
		this.perEmiDeuExt = perEmiDeuExt;
	}
	public VencimientoVO getVencimiento() {
		return vencimiento;
	}
	public void setVencimiento(VencimientoVO vencimiento) {
		this.vencimiento = vencimiento;
	}

	public SiNo getPerImpMasDeu() {
		return perImpMasDeu;
	}

	public void setPerImpMasDeu(SiNo perImpMasDeu) {
		this.perImpMasDeu = perImpMasDeu;
	}

	public FormularioVO getFormImpMasDeu() {
		return formImpMasDeu;
	}

	public void setFormImpMasDeu(FormularioVO formImpMasDeu) {
		this.formImpMasDeu = formImpMasDeu;
	}

	public void setFecUltEnvPro(Date fecUltEnvPro) {
		this.fecUltEnvPro = fecUltEnvPro;
		this.fecUltEnvProView = DateUtil.formatDate(fecUltEnvPro, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public String getFecUltEnvProView(){
		return fecUltEnvProView;
	}

	public Date getFecUltEnvPro() {
		return fecUltEnvPro;
	}

	public SiNo getGenNotif() {
		return genNotif;
	}

	public void setGenNotif(SiNo genNotif) {
		this.genNotif = genNotif;
	}

	public FormularioVO getFormNotif() {
		return formNotif;
	}

	public void setFormNotif(FormularioVO formNotif) {
		this.formNotif = formNotif;
	}

	public SiNo getEsDeudaTitular(){
		return esDeudaTitular;
	}
	public void setEsDeudaTitular(SiNo esDeudaTitular){
		this.esDeudaTitular = esDeudaTitular;
	}
	public TipObjImpVO getTipObjImp(){
		return tipObjImp;
	}
	public void setFormPadFir(FormularioVO formPadFir) {
		this.formPadFir = formPadFir;
	}
	public FormularioVO getFormPadFir() {
		return formPadFir;
	}
	public void setTipObjImp(TipObjImpVO tipObjImp){
		this.tipObjImp = tipObjImp;
	}
	public SiNo getEsPrincipal(){
		return esPrincipal;
	}
	public void setEsPrincipal(SiNo esPrincipal){
		this.esPrincipal = esPrincipal;
	}
	public SiNo getAltaCtaPorIface(){
		return altaCtaPorIface;
	}
	public void setAltaCtaPorIface(SiNo altaCtaPorIface){
		this.altaCtaPorIface = altaCtaPorIface;
	}
	public SiNo getBajaCtaPorIface(){
		return bajaCtaPorIface;
	}
	public void setBajaCtaPorIface(SiNo bajaCtaPorIface){
		this.bajaCtaPorIface = bajaCtaPorIface;
	}
	public SiNo getModiTitCtaPorIface(){
		return modiTitCtaPorIface;
	}
	public void setModiTitCtaPorIface(SiNo modiTitCtaPorIface){
		this.modiTitCtaPorIface = modiTitCtaPorIface;
	}
	public RecursoVO getRecPri(){
		return recPri;
	}
	public void setRecPri(RecursoVO recPri){
		this.recPri = recPri;
	}
	public void setGenPadFir(SiNo genPadFir) {
		this.genPadFir = genPadFir;
	}

	public SiNo getGenPadFir() {
		return genPadFir;
	}

	public SiNo getAltaCtaPorPri(){
		return altaCtaPorPri;
	}
	public void setAltaCtaPorPri(SiNo altaCtaPorPri){
		this.altaCtaPorPri = altaCtaPorPri;
	}
	public SiNo getBajaCtaPorPri(){
		return bajaCtaPorPri;
	}
	public void setBajaCtaPorPri(SiNo bajaCtaPorPri){
		this.bajaCtaPorPri = bajaCtaPorPri;
	}
	public SiNo getModiTitCtaPorPri(){
		return modiTitCtaPorPri;
	}
	public void setModiTitCtaPorPri(SiNo modiTitCtaPorPri){
		this.modiTitCtaPorPri = modiTitCtaPorPri;
	}
	public SiNo getAltaCtaManual(){
		return altaCtaManual;
	}
	public void setAltaCtaManual(SiNo altaCtaManual){
		this.altaCtaManual = altaCtaManual;
	}
	public SiNo getBajaCtaManual(){
		return bajaCtaManual;
	}
	public void setBajaCtaManual(SiNo bajaCtaManual){
		this.bajaCtaManual = bajaCtaManual;
	}
	public SiNo getModiTitCtaManual(){
		return modiTitCtaManual;
	}
	public void setModiTitCtaManual(SiNo modiTitCtaManual){
		this.modiTitCtaManual = modiTitCtaManual;
	}
	public SiNo getEnviaJudicial(){
		return enviaJudicial;
	}
	public void setEnviaJudicial(SiNo enviaJudicial){
		this.enviaJudicial = enviaJudicial;
	}
	public CriAsiProVO getCriAsiPro(){
		return criAsiPro;
	}
	public void setCriAsiPro(CriAsiProVO criAsiPro){
		this.criAsiPro = criAsiPro;
	}
	public GenCueVO getGenCue(){
		return genCue;
	}
	public void setGenCue(GenCueVO genCue){
		this.genCue = genCue;
	}
	public GenCodGesVO getGenCodGes(){
		return genCodGes;
	}
	public void setGenCodGes(GenCodGesVO genCodGes){
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
		this.fechaAltaView = DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaAltaView(){
		return fechaAltaView;
	}
	public void setFechaAltaView(){
	}
	public Date getFechaBaja(){
		return fechaBaja;
	}
	public void setFechaBaja(Date fechaBaja){
		this.fechaBaja = fechaBaja;
		this.fechaBajaView = DateUtil.formatDate(fechaBaja, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaBajaView(){
		return fechaBajaView;
	}
	public void setFechaBajaView(){
	}
	
	public List<RecAtrValVO> getListRecAtrVal(){
		return listRecAtrVal;
	}
	public void setListRecAtrVal(List<RecAtrValVO> listRecAtrVal){
		this.listRecAtrVal = listRecAtrVal;
	} 
	public List<RecConVO> getListRecCon(){
		return listRecCon;
	}
	public void setListRecCon(List<RecConVO> listRecCon){
		this.listRecCon = listRecCon;
	} 
	public List<RecClaDeuVO> getListRecClaDeu(){
		return listRecClaDeu;
	}
	public void setListRecClaDeu(List<RecClaDeuVO> listRecClaDeu){
		this.listRecClaDeu = listRecClaDeu;
	} 
	public List<RecGenCueAtrVaVO> getListRecGenCueAtrVa(){
		return listRecGenCueAtrVa;
	}
	public void setListRecGenCueAtrVa(List<RecGenCueAtrVaVO> listRecGenCueAtrVa){
		this.listRecGenCueAtrVa = listRecGenCueAtrVa;
	}
	public List<RecEmiVO> getListRecEmi(){
		return listRecEmi;
	}
	public void setListRecEmi(List<RecEmiVO> listRecEmi){
		this.listRecEmi = listRecEmi;
	}
	public List<RecAtrCueEmiVO> getListRecAtrCueEmi(){
		return listRecAtrCueEmi;
	}
	public void setListRecAtrCueEmi(List<RecAtrCueEmiVO> listRecAtrCueEmi){
		this.listRecAtrCueEmi = listRecAtrCueEmi;
	}
	public List<RecAtrCueVO> getListRecAtrCue(){
		return listRecAtrCue;
	}
	public void setListRecAtrCue(List<RecAtrCueVO> listRecAtrCue){
		this.listRecAtrCue = listRecAtrCue;
	}

	public Boolean getEsCategoriaCdM() {
		return esCategoriaCdM;
	}
	public void setEsCategoriaCdM(Boolean esCategoriaCdM) {
		this.esCategoriaCdM = esCategoriaCdM;
	}

	public AtributoVO getAtributoAse() {
		return atributoAse;
	}
	public void setAtributoAse(AtributoVO atributoAse) {
		this.atributoAse = atributoAse;
	}

	public Date getFecUltPag() {
		return fecUltPag;
	}

	public void setFecUltPag(Date fecUltPag) {
		this.fecUltPag = fecUltPag;
	}
	
	public PeriodoDeudaVO getPeriodoDeuda() {
		return periodoDeuda;
	}

	public void setPeriodoDeuda(PeriodoDeudaVO periodoDeuda) {
		this.periodoDeuda = periodoDeuda;
	}

	public List<RecConADecVO> getListRecConADec() {
		return listRecConADec;
	}

	public void setListRecConADec(List<RecConADecVO> listRecConADec) {
		this.listRecConADec = listRecConADec;
	}
	
	public List<RecMinDecVO> getListRecMinDec() {
		return listRecMinDec;
	}

	public void setListRecMinDec(List<RecMinDecVO> listRecMinDec) {
		this.listRecMinDec = listRecMinDec;
	}

	public List<RecAliVO> getListRecAli() {
		return listRecAli;
	}

	public void setListRecAli(List<RecAliVO> listRecAli) {
		this.listRecAli = listRecAli;
	}

	public boolean getDeclaraAdicionales() {
		return declaraAdicionales;
	}

	public void setDeclaraAdicionales(boolean declaraAdicionales) {
		this.declaraAdicionales = declaraAdicionales;
	}

	public List<RecursoAreaVO> getListRecursoArea() {
		return listRecursoArea;
	}
	public void setListRecursoArea(List<RecursoAreaVO> listRecursoArea) {
		this.listRecursoArea = listRecursoArea;
	}

	// View getters
	public String getFecUltPagView(){
		return DateUtil.formatDate(fecUltPag, DateUtil.ddSMMSYYYY_MASK);
	}

	/**
	 *	Devuelve el total de conceptos para un recurso. 
	 * 
	 * @return
	 */
	public String getTotalRecConView(){
		Double total = 0D;
		
		for (RecConVO recConVO:this.getListRecCon()){
			total += new Double(recConVO.getTotal());
		}
		
		return StringUtil.formatDouble(total);
	}
	
}