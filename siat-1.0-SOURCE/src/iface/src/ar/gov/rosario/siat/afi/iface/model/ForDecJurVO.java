//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.model.EnvioOsirisVO;
import ar.gov.rosario.siat.bal.iface.model.TranAfipVO;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.model.DecJurVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CategoriaEmpresaAfip;
import coop.tecso.demoda.iface.model.FormularioAfip;
import coop.tecso.demoda.iface.model.ImpuestoAfip;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TipoPersona;

/**
 * Value Object del ForDecJur
 * @author tecso
 *
 */
public class ForDecJurVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "forDecJurVO";
	
	private Date 		fechaPreCon;	
	private Date 		fechaDecQui;	
	private Date 		fechaInsConMul;	
	private Date 		fechaBajaConMul;	
	private Date 		fecVenLiqMen;	
	private Date 		fechaVencimiento;	   
	private Date 		fechaInsIIBB;	
	private Date 		fechaBajaIIBB;	
	private Date 		fecPagPre;
	private Date 		fechaPresentacion;	
	private Integer 	codJurCab;
	private Integer		nroFormulario;;	
	private Long	 	impuesto;	
	private Integer 	concepto;	
	private Integer 	periodo;	
	private Integer 	anio ;	 
	private Integer 	cuota;
	private Integer 	codRectif;		
	private Integer 	version;
	private Integer 	release;	
	private Long 		nroVerificador;	
	private Integer 	tipoOrg;	
	private Integer 	categoria;		
	private Integer 	otrLocFueCiu;	
	private Integer 	concursado;	
	private Integer 	contribFallido;	
	private Integer 	regimenGeneral;	
	private Integer 	regimenEspecial;	
	private Integer 	articulo6;	
	private Integer 	articulo7;	
	private Integer 	articulo8;	
	private Integer 	articulo9;	
	private Integer  	articulo10;	
	private Integer 	articulo11;	
	private Integer  	articulo12;	
	private Integer  	articulo13;	
	private Integer 	otrLocFueCiuPorCon;	
	private Integer 	codRecRetPerPerAnt;	
	private Integer 	otrLocFueProvPorCon;
	private Integer 	retYPerPerAnt;	
	private Double 		coeficienteSF;
	private Double 		coefIntercomunal;	
	private Double 		derecho;
	private Double 		totRetYPer;	
	private Double 		montoRetPerPerAnt;	
	private Double 		aFavorContrib;	
	private Double 		aFavorDirMun;	
	private Double 		tasaInteres;	
	private Double 		recargoInteres;	
	private Double 		derechoAdeuda;
	private String 		nroInsImpIIBB="";	
	private String 		nroTelefono="";	
	private String 		email="";	
	private String 		versionInterna="";	
	private String 		hora="";
	private String 		cuit="";	
	private String 		observaciones="";	
	private String 		nroInscripcion="";	
	private String 		perRetYPerPerAnt="";
	
	private String 		fechaPreConView="";	
	private String 		fechaDecQuiView="";	
	private String 		fechaInsConMulView="";	
	private String 		fechaBajaConMulView="";	
	private String 		fecVenLiqMenView="";	
	private String 		fechaVencimientoView="";	   
	private String 		fechaInsIIBBView="";
	private String 		fechaBajaIIBBView="";	
	private String 		fecPagPreView="";	
	private String 		fechaPresentacionView="";
	private String 		codJurCabView="";	
	private String	 	nroFormularioView="";	
	private String	 	impuestoView="";	
	private String 		conceptoView="";	
	private String 		periodoView="";	
	private String 		anioView="";	 
	private String 		cuotaView="";
	private String 		codRectifView="";		
	private String 		versionView="";
	private String 		releaseView="";	
	private String 		nroVerificadorView="";	
	private String 		tipoOrgView="";	
	private String 		categoriaView="";		
	private String 		otrLocFueCiuView="";	
	private String 		concursadoView="";	
	private String 		contribFallidoView="";	
	private String 		regimenGeneralView="";	
	private String 		regimenEspecialView="";	
	private String 		articulo6View="";	
	private String 		articulo7View="";	
	private String 		articulo8View="";	
	private String 		articulo9View="";	
	private String  	articulo10View="";	
	private String 		articulo11View="";	
	private String  	articulo12View="";	
	private String  	articulo13View="";	
	private String 		otrLocFueCiuPorConView="";	
	private String 		codRecRetPerPerAntView="";		
	private String 		otrLocFueProvPorConView="";
	private String 		retYPerPerAntView="";	
	private String 		coeficienteSFView="";
	private String 		coefIntercomunalView="";	
	private String 		derechoView="";	
	private String 		totRetYPerView="";	
	private String 		montoRetPerPerAntView="";	
	private String 		aFavorContribView="";	
	private String 		aFavorDirMunView="";	
	private String 		tasaInteresView="";	
	private String 		recargoInteresView="";	
	private String 		derechoAdeudaView="";	
   
	private RecursoVO 				recurso = new RecursoVO();
	private TranAfipVO 				tranAfip = new TranAfipVO();
	private EstForDecJurVO			estForDecJur = new EstForDecJurVO();
	private EnvioOsirisVO 			envioOsiris = new EnvioOsirisVO();
	private List<SocioVO>			listSocio = new ArrayList<SocioVO>();
	private List<LocalVO> 			listLocal = new ArrayList<LocalVO>();
	private List<RetYPerVO> 		listRetYPer = new ArrayList<RetYPerVO>();
	private List<TotDerYAccDJVO> 	listTotDerYAccDJ = new ArrayList<TotDerYAccDJVO>();
	private List<DatosDomicilioVO> 	listDatosDomicilio = new ArrayList<DatosDomicilioVO>();
	private List<DecJurVO> 			listDecJur = new ArrayList<DecJurVO>();
			
	
	// Buss Flags
	private Boolean generarDecJurBussEnabled  = true;

	// Constructores
	public ForDecJurVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ForDecJurVO(int id) {
		super();
		setId(new Long(id));
	}	
	// Getters y Setters

	public Date getFechaPreCon() {
		return fechaPreCon;
	}

	public Date getFechaDecQui() {
		return fechaDecQui;
	}

	public Date getFechaInsConMul() {
		return fechaInsConMul;
	}

	public Date getFechaBajaConMul() {
		return fechaBajaConMul;
	}

	public Date getFecVenLiqMen() {
		return fecVenLiqMen;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public Date getFechaInsIIBB() {
		return fechaInsIIBB;
	}

	public Date getFechabajaIIBB() {
		return fechaBajaIIBB;
	}

	public Date getFecPagPre() {
		return fecPagPre;
	}

	public Integer getCodJurCab() {
		return codJurCab;
	}

	public Long getImpuesto() {
		return impuesto;
	}

	public Integer getConcepto() {
		return concepto;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public Integer getAnio() {
		return anio;
	}

	public Integer getCuota() {
		return cuota;
	}

	public Integer getCodRectif() {
		return codRectif;
	}

	public Integer getVersion() {
		return version;
	}

	public Integer getRelease() {
		return release;
	}

	public Long getNroVerificador() {
		return nroVerificador;
	}

	public Integer getTipoOrg() {
		return tipoOrg;
	}

	public Integer getCategoria() {
		return categoria;
	}

	public Integer getOtrLocFueCiu() {
		return otrLocFueCiu;
	}

	public Integer getConcursado() {
		return concursado;
	}

	public Integer getContribFallido() {
		return contribFallido;
	}

	public Integer getRegimenGeneral() {
		return regimenGeneral;
	}

	public Integer getRegimenEspecial() {
		return regimenEspecial;
	}

	public Integer getArticulo6() {
		return articulo6;
	}

	public Integer getArticulo7() {
		return articulo7;
	}

	public Integer getArticulo8() {
		return articulo8;
	}

	public Integer getArticulo9() {
		return articulo9;
	}

	public Integer getArticulo10() {
		return articulo10;
	}

	public Integer getArticulo11() {
		return articulo11;
	}

	public Integer getArticulo12() {
		return articulo12;
	}

	public Integer getArticulo13() {
		return articulo13;
	}

	public Integer getOtrLocFueCiuPorCon() {
		return otrLocFueCiuPorCon;
	}

	public Integer getCodRecRetPerPerAnt() {
		return codRecRetPerPerAnt;
	}

	public Integer getOtrLocFueProvPorCon() {
		return otrLocFueProvPorCon;
	}

	public Integer getRetYPerPerAnt() {
		return retYPerPerAnt;
	}

	public Double getCoeficienteSF() {
		return coeficienteSF;
	}

	public Double getCoefIntercomunal() {
		return coefIntercomunal;
	}

	public Double getDerecho() {
		return derecho;
	}

	public Double getTotRetYPer() {
		return totRetYPer;
	}

	public Double getMontoRetPerPerAnt() {
		return montoRetPerPerAnt;
	}

	public Double getaFavorContrib() {
		return aFavorContrib;
	}

	public Double getaFavorDirMun() {
		return aFavorDirMun;
	}

	public Double getTasaInteres() {
		return tasaInteres;
	}

	public Double getRecargoInteres() {
		return recargoInteres;
	}

	public Double getDerechoAdeuda() {
		return derechoAdeuda;
	}

	public String getNroInsImpIIBB() {
		return nroInsImpIIBB;
	}

	public String getNroTelefono() {
		return nroTelefono;
	}

	public String getEmail() {
		return email;
	}

	public String getVersionInterna() {
		return versionInterna;
	}

	public String getHora() {
		return hora;
	}

	public String getCuit() {
		return cuit;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public String getNroInscripcion() {
		return nroInscripcion;
	}

	public String getPerRetYPerPerAnt() {
		return perRetYPerPerAnt;
	}

	public String getFechaPreConView() {
		return fechaPreConView;
	}

	public String getFechaDecQuiView() {
		return fechaDecQuiView;
	}

	public String getFechaInsConMulView() {
		return fechaInsConMulView;
	}

	public String getFechaBajaConMulView() {
		return fechaBajaConMulView;
	}

	public String getFecVenLiqMenView() {
		return fecVenLiqMenView;
	}

	public String getFechaVencimientoView() {
		return fechaVencimientoView;
	}

	public String getFechaInsIIBBView() {
		return fechaInsIIBBView;
	}

	public String getFechaBajaIIBBView() {
		return fechaBajaIIBBView;
	}
	
	public String getFechaPresentacionView() {
		return fechaPresentacionView;
	}

	public String getFecPagPreView() {
		return fecPagPreView;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public EnvioOsirisVO getEnvioOsiris() {
		return envioOsiris;
	}

	public TranAfipVO getTranAfip() {
		return tranAfip;
	}

	public List<SocioVO> getListSocio() {
		return listSocio;
	}

	public List<LocalVO> getListLocal() {
		return listLocal;
	}

	public List<RetYPerVO> getListPercepcion() {
		return listRetYPer;
	}

	public List<TotDerYAccDJVO> getListTotDerechoAcc() {
		return listTotDerYAccDJ;
	}

	public List<DatosDomicilioVO> getListDatosDomicilio() {
		return listDatosDomicilio;
	}

	public List<DecJurVO> getListDecJur() {
		return listDecJur;
	}

	public void setListDecJur(List<DecJurVO> listDecJur) {
		this.listDecJur = listDecJur;
	}

	public void setFechaPreCon(Date fechaPreCon) {
		this.fechaPreCon = fechaPreCon;
		this.fechaPreConView = DateUtil.formatDate(fechaPreCon, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setFechaDecQui(Date fechaDecQui) {
		this.fechaDecQui = fechaDecQui;
		this.fechaDecQuiView = DateUtil.formatDate(fechaDecQui, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setFechaInsConMul(Date fechaInsConMul) {
		this.fechaInsConMul = fechaInsConMul;
		this.fechaInsConMulView = DateUtil.formatDate(fechaInsConMul, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setFechaBajaConMul(Date fechaBajaConMul) {
		this.fechaBajaConMul = fechaBajaConMul;
		this.fechaBajaConMulView = DateUtil.formatDate(fechaBajaConMul, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setFecVenLiqMen(Date fecVenLiqMen) {
		this.fecVenLiqMen = fecVenLiqMen;
		this.fecVenLiqMenView = DateUtil.formatDate(fecVenLiqMen, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
		this.fechaVencimientoView = DateUtil.formatDate(fechaVencimiento, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setFechaInsIIBB(Date fechaInsIIBB) {
		this.fechaInsIIBB = fechaInsIIBB;
		this.fechaInsIIBBView = DateUtil.formatDate(fechaInsIIBB, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setFechabajaIIBB(Date fechabajaIIBB) {
		this.fechaBajaIIBB = fechabajaIIBB;
		this.fechaBajaIIBBView = DateUtil.formatDate(fechaBajaIIBB, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setFecPagPre(Date fecPagPre) {
		this.fecPagPre = fecPagPre;
		this.fecPagPreView = DateUtil.formatDate(fecPagPre, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setCodJurCab(Integer codJurCab) {
		this.codJurCab = codJurCab;
		this.codJurCabView = StringUtil.formatInteger(codJurCab);
	}


	public void setImpuesto(Long impuesto) {
		this.impuesto = impuesto;
		ImpuestoAfip imp = ImpuestoAfip.getById(impuesto.intValue());
		if(imp != null)
			this.impuestoView = imp.getFullValue();
		else
			this.impuestoView = "Desconocido";
	}

	public void setConcepto(Integer concepto) {
		this.concepto = concepto;
		this.conceptoView = StringUtil.formatInteger(concepto);
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
		this.periodoView = StringUtil.formatInteger(periodo);
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
		this.anioView = StringUtil.formatInteger(anio);
	}

	public void setCuota(Integer cuota) {
		this.cuota = cuota;
		this.cuotaView = StringUtil.formatInteger(cuota);
	}

	public void setCodRectif(Integer codRectif) {
		this.codRectif = codRectif;
		this.codRectifView = StringUtil.formatInteger(codRectif);
	}

	public void setVersion(Integer version) {
		this.version = version;
		this.versionView = StringUtil.formatInteger(version);
	}

	public void setRelease(Integer release) {
		this.release = release;
		this.releaseView = StringUtil.formatInteger(release);
	}

	public void setNroVerificador(Long nroVerificador) {
		this.nroVerificador = nroVerificador;
		this.nroVerificadorView = StringUtil.formatLong(nroVerificador);
	}

	public void setTipoOrg(Integer tipoOrg) {
		this.tipoOrg = tipoOrg;
		this.tipoOrgView = StringUtil.formatInteger(tipoOrg);
	}

	public void setCategoria(Integer categoria) {
		this.categoria = categoria;
		this.categoriaView = StringUtil.formatInteger(categoria);
		CategoriaEmpresaAfip cat = CategoriaEmpresaAfip.getById(categoria);
		if(cat != null)
			this.categoriaView = cat.getValue();
		else
			this.categoriaView = "Desconocido";

	}

	public void setOtrLocFueCiu(Integer otrLocFueCiu) {
		this.otrLocFueCiu = otrLocFueCiu;
		this.otrLocFueCiuView = SiNo.getById(otrLocFueCiu).getValue();
	}

	public void setConcursado(Integer concursado) {
		this.concursado = concursado;
		this.concursadoView = SiNo.getById(concursado).getValue();
	}

	public void setContribFallido(Integer contribFallido) {
		this.contribFallido = contribFallido;
		this.contribFallidoView = SiNo.getById(contribFallido).getValue();
	}

	public void setRegimenGeneral(Integer regimenGeneral) {
		this.regimenGeneral = regimenGeneral;
		this.regimenGeneralView = SiNo.getById(regimenGeneral).getValue();
	}

	public void setRegimenEspecial(Integer regimenEspecial) {
		this.regimenEspecial = regimenEspecial;
		this.regimenEspecialView = SiNo.getById(regimenEspecial).getValue();
	}

	public void setArticulo6(Integer articulo6) {
		this.articulo6 = articulo6;
		this.articulo6View = SiNo.getById(articulo6).getValue();
	}

	public void setArticulo7(Integer articulo7) {
		this.articulo7 = articulo7;
		this.articulo7View = SiNo.getById(articulo7).getValue();
	}

	public void setArticulo8(Integer articulo8) {
		this.articulo8 = articulo8;
		this.articulo8View = SiNo.getById(articulo8).getValue();
	}

	public void setArticulo9(Integer articulo9) {
		this.articulo9 = articulo9;
		this.articulo9View = SiNo.getById(articulo9).getValue();
	}

	public void setArticulo10(Integer articulo10) {
		this.articulo10 = articulo10;
		this.articulo10View = SiNo.getById(articulo10).getValue();
	}

	public void setArticulo11(Integer articulo11) {
		this.articulo11 = articulo11;
		this.articulo11View = SiNo.getById(articulo11).getValue();
	}

	public void setArticulo12(Integer articulo12) {
		this.articulo12 = articulo12;
		this.articulo12View = SiNo.getById(articulo12).getValue();
	}

	public void setArticulo13(Integer articulo13) {
		this.articulo13 = articulo13;
		this.articulo13View = SiNo.getById(articulo13).getValue();
	}

	public void setOtrLocFueCiuPorCon(Integer otrLocFueCiuPorCon) {
		this.otrLocFueCiuPorCon = otrLocFueCiuPorCon;
		this.otrLocFueCiuPorConView = StringUtil.formatInteger(otrLocFueCiuPorCon);
	}

	public void setCodRecRetPerPerAnt(Integer codRecRetPerPerAnt) {
		this.codRecRetPerPerAnt = codRecRetPerPerAnt;
		this.codRecRetPerPerAntView = StringUtil.formatInteger(codRecRetPerPerAnt);
	}

	public void setOtrLocFueProvPorCon(Integer otrLocFueProvPorCon) {
		this.otrLocFueProvPorCon = otrLocFueProvPorCon;
		this.otrLocFueProvPorConView = StringUtil.formatInteger(otrLocFueCiuPorCon);
	}

	public void setRetYPerPerAnt(Integer retYPerPerAnt) {
		this.retYPerPerAnt = retYPerPerAnt;
		this.retYPerPerAntView = StringUtil.formatInteger(retYPerPerAnt);
	}

	public void setCoeficienteSF(Double coeficienteSF) {
		this.coeficienteSF = coeficienteSF;
		this.coeficienteSFView = StringUtil.formatDouble(coeficienteSF);
	}

	public void setCoefIntercomunal(Double coefIntercomunal) {
		this.coefIntercomunal = coefIntercomunal;
		this.coefIntercomunalView = StringUtil.formatDouble(coefIntercomunal);
	}

	public void setDerecho(Double derecho) {
		this.derecho = derecho;
		this.derechoView = StringUtil.formatDouble(derecho);
	}

	public void setTotRetYPer(Double totRetYPer) {
		this.totRetYPer = totRetYPer;
		this.totRetYPerView = StringUtil.formatDouble(totRetYPer);
	}

	public void setMontoRetPerPerAnt(Double montoRetPerPerAnt) {
		this.montoRetPerPerAnt = montoRetPerPerAnt;
		this.montoRetPerPerAntView = StringUtil.formatDouble(montoRetPerPerAnt);
	}

	public void setaFavorContrib(Double aFavorContrib) {
		this.aFavorContrib = aFavorContrib;
		this.aFavorContribView = StringUtil.formatDouble(aFavorContrib);
	}

	public void setaFavorDirMun(Double aFavorDirMun) {
		this.aFavorDirMun = aFavorDirMun;
		this.aFavorDirMunView = StringUtil.formatDouble(aFavorDirMun);
	}

	public void setTasaInteres(Double tasaInteres) {
		this.tasaInteres = tasaInteres;
		this.tasaInteresView = StringUtil.formatDouble(tasaInteres);
	}

	public void setRecargoInteres(Double recargoInteres) {
		this.recargoInteres = recargoInteres;
		this.recargoInteresView = StringUtil.formatDouble(recargoInteres);
	}

	public void setDerechoAdeuda(Double derechoAdeuda) {
		this.derechoAdeuda = derechoAdeuda;
		this.derechoAdeudaView = StringUtil.formatDouble(derechoAdeuda);
	}

	public void setNroInsImpIIBB(String nroInsImpIIBB) {
		this.nroInsImpIIBB = nroInsImpIIBB;
	}

	public void setNroTelefono(String nroTelefono) {
		this.nroTelefono = nroTelefono;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setVersionInterna(String versionInterna) {
		this.versionInterna = versionInterna;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public void setNroInscripcion(String nroInscripcion) {
		this.nroInscripcion = nroInscripcion;
	}

	public void setPerRetYPerPerAnt(String perRetYPerPerAnt) {
		this.perRetYPerPerAnt = perRetYPerPerAnt;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public void setEnvioOsiris(EnvioOsirisVO envioOsiris) {
		this.envioOsiris = envioOsiris;
	}

	public void setTranAfip(TranAfipVO tranAfip) {
		this.tranAfip = tranAfip;
	}

	public void setListSocio(List<SocioVO> listSocio) {
		this.listSocio = listSocio;
	}

	public void setListLocal(List<LocalVO> listLocal) {
		this.listLocal = listLocal;
	}

	public void setListPercepcion(List<RetYPerVO> listRetYPer) {
		this.listRetYPer = listRetYPer;
	}


	public void setListTotDerechoAcc(List<TotDerYAccDJVO> listTotDerechoAcc) {
		this.listTotDerYAccDJ = listTotDerechoAcc;
	}

	public void setListDatosDomicilio(List<DatosDomicilioVO> listDatosDomicilio) {
		this.listDatosDomicilio = listDatosDomicilio;
	}	
	
	public Integer getNroFormulario() {
		return nroFormulario;
	}

	public void setNroFormulario(Integer nroFormulario) {
		this.nroFormulario = nroFormulario;
		FormularioAfip form = FormularioAfip.getById(nroFormulario);
		if(form != null)
			this.nroFormularioView = form.getFullValue();
		else
			this.nroFormularioView = "Desconocido";
	}


	// View getters	
	public String getNroFormularioView() {
		return nroFormularioView;
	}
	
	public void setNroFormularioView(String nroFormularioView) {
		this.nroFormularioView = nroFormularioView;
	}

	public void setFechaPresentacion(Date fechaPresentacion) {
		this.fechaPresentacion = fechaPresentacion;
		this.fechaPresentacionView = DateUtil.formatDate(fechaPresentacion, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setEstForDecJur(EstForDecJurVO estForDecJur) {
		this.estForDecJur = estForDecJur;
	}

	public EstForDecJurVO getEstForDecJur() {
		return estForDecJur;
	}

	public Date getFechaPresentacion() {
		return fechaPresentacion;
	}

	public Date getFechaBajaIIBB() {
		return fechaBajaIIBB;
	}

	public String getCodJurCabView() {
		return codJurCabView;
	}

	public String getImpuestoView() {
		return impuestoView;
	}

	public String getConceptoView() {
		return conceptoView;
	}

	public String getPeriodoView() {
		return periodoView;
	}

	public String getAnioView() {
		return anioView;
	}

	public String getCuotaView() {
		return cuotaView;
	}

	public String getCodRectifView() {
		return codRectifView;
	}

	public String getVersionView() {
		return versionView;
	}

	public String getReleaseView() {
		return releaseView;
	}

	public String getNroVerificadorView() {
		return nroVerificadorView;
	}

	public String getTipoOrgView() {
		return tipoOrgView+" - "+TipoPersona.getById(tipoOrg).getValue();
	}

	public String getCategoriaView() {
		return categoriaView;
	}

	public String getOtrLocFueCiuView() {
		return otrLocFueCiuView;
	}

	public String getConcursadoView() {
		return concursadoView;
	}

	public String getContribFallidoView() {
		return contribFallidoView;
	}

	public String getRegimenGeneralView() {
		return regimenGeneralView;
	}

	public String getRegimenEspecialView() {
		return regimenEspecialView;
	}

	public String getArticulo6View() {
		return articulo6View;
	}

	public String getArticulo7View() {
		return articulo7View;
	}

	public String getArticulo8View() {
		return articulo8View;
	}

	public String getArticulo9View() {
		return articulo9View;
	}

	public String getArticulo10View() {
		return articulo10View;
	}

	public String getArticulo11View() {
		return articulo11View;
	}

	public String getArticulo12View() {
		return articulo12View;
	}

	public String getArticulo13View() {
		return articulo13View;
	}

	public String getOtrLocFueCiuPorConView() {
		return otrLocFueCiuPorConView;
	}

	public String getCodRecRetPerPerAntView() {
		return codRecRetPerPerAntView;
	}

	public String getOtrLocFueProvPorConView() {
		return otrLocFueProvPorConView;
	}

	public String getRetYPerPerAntView() {
		return retYPerPerAntView;
	}

	public String getCoeficienteSFView() {
		return coeficienteSFView;
	}

	public String getCoefIntercomunalView() {
		return coefIntercomunalView;
	}

	public String getDerechoView() {
		return derechoView;
	}

	public String getTotRetYPerView() {
		return totRetYPerView;
	}

	public String getMontoRetPerPerAntView() {
		return montoRetPerPerAntView;
	}

	public String getaFavorContribView() {
		return aFavorContribView;
	}

	public String getaFavorDirMunView() {
		return aFavorDirMunView;
	}

	public String getTasaInteresView() {
		return tasaInteresView;
	}

	public String getRecargoInteresView() {
		return recargoInteresView;
	}

	public String getDerechoAdeudaView() {
		return derechoAdeudaView;
	}

	public List<RetYPerVO> getListRetYPer() {
		return listRetYPer;
	}

	public List<TotDerYAccDJVO> getListTotDerYAccDJ() {
		return listTotDerYAccDJ;
	}

	public void setFechaBajaIIBB(Date fechaBajaIIBB) {
		this.fechaBajaIIBB = fechaBajaIIBB;
	}

	public void setListRetYPer(List<RetYPerVO> listRetYPer) {
		this.listRetYPer = listRetYPer;
	}

	public void setListTotDerYAccDJ(List<TotDerYAccDJVO> listTotDerYAccDJ) {
		this.listTotDerYAccDJ = listTotDerYAccDJ;
	}

	public String getPeriodoFiscalView(){
		return this.getPeriodoView()+"/"+this.getAnioView();
	}
	
	//	 Buss flags getters y setters
	public Boolean getGenerarDecJurBussEnabled() {
		return generarDecJurBussEnabled;
	}
	public void setGenerarDecJurBussEnabled(Boolean generarDecJurBussEnabled) {
		this.generarDecJurBussEnabled = generarDecJurBussEnabled;
	}
	public String getGenerarDecJurEnabled() {
		return this.getGenerarDecJurBussEnabled() ? ENABLED : DISABLED;
	}
}
