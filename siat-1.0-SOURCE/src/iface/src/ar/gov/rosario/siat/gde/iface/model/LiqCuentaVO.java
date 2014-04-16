//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.pad.iface.model.EstCueVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.EstadoPeriodo;
import coop.tecso.demoda.iface.model.SiNo;

public class LiqCuentaVO {

	private Long 	idCuenta;
	private Long    idObjImp;
	
	private String 	codServicioBanco="";
	private String 	desCategoria="";
	private String 	codRecurso="";
	private String 	desRecurso="";
	private String  codGestionPersonal="";
    private String 	nroCuenta="";
    private Boolean esActiva = false;
	private String nombreTitularPrincipal = "";
	private String cuitTitularPrincipal = "";
	private String desDomEnv = "";
	private String cuitTitularPrincipalContr ="";
	private String desBroche = "";
	private String desClaveFuncional = "";
	private Integer esLitoralGas;
	private Integer esObraPeatonal;
	private String nroIsib="";
	private String convMultilateral=SiNo.NO.getValue();
	private String codTipObjImp="";
	private EstCueVO estado = new EstCueVO();
	private String expedienteCierre="";
	private String observacion="";
	
    private List<LiqAtrValorVO> listAtributoObjImp = new ArrayList<LiqAtrValorVO>();
    private List<LiqAtrValorVO> listAtributoCuenta = new ArrayList<LiqAtrValorVO>();
    private List<LiqTitularVO>  listTitular = new ArrayList<LiqTitularVO>();
    private List<LiqAtrValorVO> listAtributoContr = new ArrayList<LiqAtrValorVO>();
    private List<LiqConvenioVO> listConvenio = new ArrayList<LiqConvenioVO>();
    private List<LiqConvenioVO> listConvenioRecompuesto = new ArrayList<LiqConvenioVO>();
    private List<LiqCuentaVO>   listCuentaUnifDes = new ArrayList<LiqCuentaVO>();
    private List<OrdenControlVO>listOrdenControl = new ArrayList<OrdenControlVO>();
    
    // Propiedades para los distintos logueos
    private RecursoVO recurso = new RecursoVO();
	private Long   idRecurso; 
	private String numeroCuenta = "";
	private String codGesPer = "";
    
    
    // Propiedades para mantener los filtros para recursos autoliquidables para recursos autoliquidables R4
    private EstadoPeriodo estadoPeriodo = EstadoPeriodo.OpcionTodo;
    private RecClaDeuVO recClaDeu = new RecClaDeuVO();
    private Date fechaVtoDesde; 
    private Date fechaVtoHasta;
    
    private String fechaVtoDesdeView = "";
    private String fechaVtoHastaView = "";
    
    private boolean esRecursoAutoliquidable = false;
    private boolean esRecursoFiscalizable = false;
    
    // Propiedades para aplicar logica de Deuda sigue titular 
    private boolean deudaSigueTitular = false;  // Esta bandera se corresponde con "esDeudaTitular" del recurso. 
    private Long idCuentaTitular = null; // Con la nulidad o no de esta propiedad se aplica la logica de "esDeudaTitular" del recurso.
    
    private boolean  deuExiVen = false; // Esta bandera se corresponde con "deuExiVen" del recurso. Deuda exigible una vez vencida. 
    
    private String detalleFiltros="";
    
    
    //  Getters y Setters
    public String getDesBroche() {
		return desBroche;
	}
	public void setDesBroche(String desBroche) {
		this.desBroche = desBroche;
	}
	public String getCodGestionPersonal() {
		return codGestionPersonal;
	}
	public void setCodGestionPersonal(String codGestionPersonal) {
		this.codGestionPersonal = codGestionPersonal;
	}
	public String getDesCategoria() {
		return desCategoria;
	}
	public void setDesCategoria(String desCategoria) {
		this.desCategoria = desCategoria;
	}
	public String getDesRecurso() {
		return desRecurso;
	}
	public void setDesRecurso(String desRecurso) {
		this.desRecurso = desRecurso;
	}
	public Long getIdCuenta() {
		return idCuenta;
	}
	public void setIdCuenta(Long idCuenta) {
		this.idCuenta = idCuenta;
	}
	public String getNroCuenta() {
		return nroCuenta;
	}
	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}
	public List<LiqAtrValorVO> getListAtributoContr() {
		return listAtributoContr;
	}
	public void setListAtributoContr(List<LiqAtrValorVO> listAtributoContr) {
		this.listAtributoContr = listAtributoContr;
	}
	public List<LiqAtrValorVO> getListAtributoObjImp() {
		return listAtributoObjImp;
	}
	public void setListAtributoObjImp(List<LiqAtrValorVO> listAtributoObjImp) {
		this.listAtributoObjImp = listAtributoObjImp;
	}
	public List<LiqTitularVO> getListTitular() {
		return listTitular;
	}
	public void setListTitular(List<LiqTitularVO> listTitular) {
		this.listTitular = listTitular;
	}
	public List<LiqConvenioVO> getListConvenio() {
		return listConvenio;
	}
	public void setListConvenio(List<LiqConvenioVO> listConvenio) {
		this.listConvenio = listConvenio;
	}
	public List<LiqCuentaVO> getListCuentaUnifDes() {
		return listCuentaUnifDes;
	}
	public void setListCuentaUnifDes(List<LiqCuentaVO> listCuentaUnifDes) {
		this.listCuentaUnifDes = listCuentaUnifDes;
	}
	public Long getIdObjImp() {
		return idObjImp;
	}
	public void setIdObjImp(Long idObjImp) {
		this.idObjImp = idObjImp;
	}
	
	public String getCuitTitularPrincipalContr() {
		return cuitTitularPrincipalContr;
	}
	public void setCuitTitularPrincipalContr(String cuitTitularPrincipalContr) {
		this.cuitTitularPrincipalContr = cuitTitularPrincipalContr;
	}
	
	public String getCodRecurso() {
		return codRecurso;
	}
	public void setCodRecurso(String codRecurso) {
		this.codRecurso = codRecurso;
	}

	public Integer getEsLitoralGas() {
		return esLitoralGas;
	}
	public void setEsLitoralGas(Integer esLitoralGas) {
		this.esLitoralGas = esLitoralGas;
	}
	public Integer getEsObraPeatonal() {
		return esObraPeatonal;
	}
	public void setEsObraPeatonal(Integer esObraPeatonal) {
		this.esObraPeatonal = esObraPeatonal;
	}
	public EstCueVO getEstado() {
		return estado;
	}
	public void setEstado(EstCueVO estado) {
		this.estado = estado;
	}
	
	public EstadoPeriodo getEstadoPeriodo() {
		return estadoPeriodo;
	}
	public void setEstadoPeriodo(EstadoPeriodo estadoPeriodo) {
		this.estadoPeriodo = estadoPeriodo;
	}

	public RecClaDeuVO getRecClaDeu() {
		return recClaDeu;
	}
	public void setRecClaDeu(RecClaDeuVO recClaDeu) {
		this.recClaDeu = recClaDeu;
	}
	
	public Date getFechaVtoDesde() {
		return fechaVtoDesde;
	}
	public void setFechaVtoDesde(Date fechaVtoDesde) {
		this.fechaVtoDesde = fechaVtoDesde;
		this.fechaVtoDesdeView = DateUtil.formatDate(fechaVtoDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaVtoHasta() {
		return fechaVtoHasta;
	}
	public void setFechaVtoHasta(Date fechaVtoHasta) {
		this.fechaVtoHasta = fechaVtoHasta;
		this.fechaVtoHastaView = DateUtil.formatDate(fechaVtoHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public boolean getDeuExiVen() {
		return deuExiVen;
	}
	public void setDeuExiVen(boolean deuExiVen) {
		this.deuExiVen = deuExiVen;
	}
	
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public List<LiqConvenioVO> getListConvenioRecompuesto() {
		return listConvenioRecompuesto;
	}
	public void setListConvenioRecompuesto(
			List<LiqConvenioVO> listConvenioRecompuesto) {
		this.listConvenioRecompuesto = listConvenioRecompuesto;
	}
	
	
	// View getters
	public String getCodGestionPersonalView(){
		return StringUtil.completarCerosIzq(String.valueOf(getCodGestionPersonal()), 10);
	}
	
	public String getNroCuentaView(){
		if(StringUtil.isNumeric(getNroCuenta()))
			return StringUtil.completarCerosIzq(String.valueOf(getNroCuenta()), 10);
		else
			return String.valueOf(getNroCuenta());
	}
	public Boolean getEsActiva() {
		return esActiva;
	}
	public void setEsActiva(Boolean esActiva) {
		this.esActiva = esActiva;
	}

	public String getCuitTitularPrincipal() {
		return cuitTitularPrincipal;
	}
	public void setCuitTitularPrincipal(String cuitTitularPrincipal) {
		this.cuitTitularPrincipal = cuitTitularPrincipal;
	}

	public String getNombreTitularPrincipal() {
		return nombreTitularPrincipal;
	}
	public void setNombreTitularPrincipal(String nombreTitularPrincipal) {
		this.nombreTitularPrincipal = nombreTitularPrincipal;
	}

	public String getDesDomEnv() {
		return desDomEnv;
	}
	public void setDesDomEnv(String desDomEnv) {
		this.desDomEnv = desDomEnv;
	}
	public String getDesClaveFuncional() {
		return desClaveFuncional;
	}
	public void setDesClaveFuncional(String desClaveFuncional) {
		this.desClaveFuncional = desClaveFuncional;
	}

	public List<LiqAtrValorVO> getListAtributoCuenta() {
		return listAtributoCuenta;
	}
	public void setListAtributoCuenta(List<LiqAtrValorVO> listAtributoCuenta) {
		this.listAtributoCuenta = listAtributoCuenta;
	}

	public String getFechaVtoDesdeView() {
		return fechaVtoDesdeView;
	}
	public void setFechaVtoDesdeView(String fechaVtoDesdeView) {
		this.fechaVtoDesdeView = fechaVtoDesdeView;
	}

	public String getFechaVtoHastaView() {
		return fechaVtoHastaView;
	}
	public void setFechaVtoHastaView(String fechaVtoHastaView) {
		this.fechaVtoHastaView = fechaVtoHastaView;
	}
	
	public LiqAtrValorVO getAtrObjImpByCod(String cod) {
	
		for (LiqAtrValorVO atr : this.getListAtributoObjImp()) {
			if (atr.getKey().equals(cod))
					return atr;
		}
	
		return null;
	}
	
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public Long getIdRecurso() {
		return idRecurso;
	}
	public void setIdRecurso(Long idRecurso) {
		this.idRecurso = idRecurso;
	}
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	public String getCodGesPer() {
		return codGesPer;
	}
	public void setCodGesPer(String codGesPer) {
		this.codGesPer = codGesPer;
	}
	
	public boolean getEsRecursoAutoliquidable() {
		return esRecursoAutoliquidable;
	}
	public void setEsRecursoAutoliquidable(boolean esRecursoAutoliquidable) {
		this.esRecursoAutoliquidable = esRecursoAutoliquidable;
	}
	
	public boolean getDeudaSigueTitular() {
		return deudaSigueTitular;
	}
	public void setDeudaSigueTitular(boolean deudaSigueTitular) {
		this.deudaSigueTitular = deudaSigueTitular;
	}
	
	public Long getIdCuentaTitular() {
		return idCuentaTitular;
	}
	public void setIdCuentaTitular(Long idCuentaTitular) {
		this.idCuentaTitular = idCuentaTitular;
	}
	
	public String getCodTipObjImp() {
		return codTipObjImp;
	}
	public void setCodTipObjImp(String codTipObjImp) {
		this.codTipObjImp = codTipObjImp;
	}
	/**
	 * Devuelve un string con la descripcion del estado,  para los estados validos para Drei.  
	 * 
	 * @return
	 */
	public String getDesEstado(){
		if (getEstado().getId().intValue() > EstCueVO.ID_CANCELADO.intValue()){
			return getEstado().getDescripcion();
		} else {
			return "";
		}
	}
	public String getNroIsib() {
		return nroIsib;
	}
	public void setNroIsib(String nroIsib) {
		this.nroIsib = nroIsib;
	}
	public String getConvMultilateral() {
		return convMultilateral;
	}
	public void setConvMultilateral(String convMultilateral) {
		this.convMultilateral = convMultilateral;
	}
	public List<OrdenControlVO> getListOrdenControl() {
		return listOrdenControl;
	}
	public void setListOrdenControl(List<OrdenControlVO> listOrdenControl) {
		this.listOrdenControl = listOrdenControl;
	}
	public boolean isEsRecursoFiscalizable() {
		return esRecursoFiscalizable;
	}
	public void setEsRecursoFiscalizable(boolean esRecursoFiscalizable) {
		this.esRecursoFiscalizable = esRecursoFiscalizable;
	}
	public String getDetalleFiltros() {
		return detalleFiltros;
	}
	public void setDetalleFiltros(String detalleFiltros) {
		this.detalleFiltros = detalleFiltros;
	}
	public String getExpedienteCierre() {
		return expedienteCierre;
	}
	public void setExpedienteCierre(String expedienteCierre) {
		this.expedienteCierre = expedienteCierre;
	}
	
	public String getCodServicioBanco() {
		return codServicioBanco;
	}
	public void setCodServicioBanco(String codServicioBanco) {
		this.codServicioBanco = codServicioBanco;
	}
	
}
