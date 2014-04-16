//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.HOUR_MINUTE_MASK;
import coop.tecso.demoda.iface.helper.StringUtil;

public class HabilitacionVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private Integer numero;
	private Integer anio;
	private RecursoVO recurso = new RecursoVO();
	private CuentaVO cuenta = new CuentaVO();
	private ValoresCargadosVO valoresCargados = new ValoresCargadosVO();
	private TipoHabVO tipoHab = new TipoHabVO();
	private TipoCobroVO tipoCobro = new TipoCobroVO();
	private Date fechaHab;
	private Date fecEveDes;
	private Date fecEveHas;
	private String descripcion = "";
	private String lugarEventoStr = "";
	private Date horaAcceso;
	private PersonaVO perHab = new PersonaVO();
	private Long idPerHab;
	private String cuit = "";
	private String observaciones = "";
	private EstHabVO estHab = new EstHabVO();
	private TipoEventoVO tipoEvento = new TipoEventoVO();

	
	private LugarEventoVO lugarEvento = new LugarEventoVO();
	private Long factorOcupacional;
	private ClaHab claHab = ClaHab.OCASIONAL;
	private ClaOrg claOrg = ClaOrg.HABITUAL;
	private Integer cantFunciones;
	private Long idDeudaInicial;

	private String numeroView = "";
	private String anioView = "";
	private String fechaHabView = "";
	private String fecEveDesView = "";
	private String fecEveHasView = "";
	private String horaAccesoView = "";
	private String idPerHabView = "";
	private String factorOcupacionalView = "";
	private String cantFuncionesView = "";
	
	private List<PrecioEventoVO> listPrecioEvento = new ArrayList<PrecioEventoVO>();
	private List<EntHabVO> listEntHab = new ArrayList<EntHabVO>();
	private List<EntVenVO> listEntVen = new ArrayList<EntVenVO>();
	private List<HisEstHabVO> listHisEstHab = new ArrayList<HisEstHabVO>();
	private List<HabExeVO> listHabExe = new ArrayList<HabExeVO>();

	private String paramTipoHab = "EXT";
	
	private Boolean entVenInternaBussEnabled  = true;
	private Boolean entVenExternaBussEnabled  = true;

	//Constructores 
	public HabilitacionVO(){
		super();
	}

	// Getters & Setters
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
		this.numeroView = StringUtil.formatInteger(numero);
	}
	public String getNumeroView() {
		return numeroView;
	}
	public void setNumeroView(String numeroView) {
		this.numeroView = numeroView;
	}
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
		this.anioView = StringUtil.formatInteger(anio);
	}
	public String getAnioView() {
		return anioView;
	}
	public void setAnioView(String anioView) {
		this.anioView = anioView;
	}
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	public String getCuit() {
		return cuit;
	}
	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public EstHabVO getEstHab() {
		return estHab;
	}
	public void setEstHab(EstHabVO estHab) {
		this.estHab = estHab;
	}
	public Date getFecEveDes() {
		return fecEveDes;
	}
	public void setFecEveDes(Date fecEveDes) {
		this.fecEveDes = fecEveDes;
		this.fecEveDesView = DateUtil.formatDate(fecEveDes, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFecEveDesView() {
		return fecEveDesView;
	}
	public void setFecEveDesView(String fecEveDesView) {
		this.fecEveDesView = fecEveDesView;
	}
	public Date getFecEveHas() {
		return fecEveHas;
	}
	public void setFecEveHas(Date fecEveHas) {
		this.fecEveHas = fecEveHas;
		this.fecEveHasView = DateUtil.formatDate(fecEveHas, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFecEveHasView() {
		return fecEveHasView;
	}
	public void setFecEveHasView(String fecEveHasView) {
		this.fecEveHasView = fecEveHasView;
	}
	public Date getFechaHab() {
		return fechaHab;
	}
	public void setFechaHab(Date fechaHab) {
		this.fechaHab = fechaHab;
		this.fechaHabView = DateUtil.formatDate(fechaHab, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaHabView() {
		return fechaHabView;
	}
	public void setFechaHabView(String fechaHabView) {
		this.fechaHabView = fechaHabView;
	}
	public Date getHoraAcceso() {
		return horaAcceso;
	}
	@HOUR_MINUTE_MASK
	public void setHoraAcceso(Date horaAcceso) {
		this.horaAcceso = horaAcceso;
		this.horaAccesoView = DateUtil.formatDate(horaAcceso, DateUtil.HOUR_MINUTE_MASK);
	}
	public String getHoraAccesoView() {
		return horaAccesoView;
	}
	public void setHoraAccesoView(String horaAccesoView) {
		this.horaAccesoView = horaAccesoView;
	}
	public Long getIdPerHab() {
		return idPerHab;
	}
	public void setIdPerHab(Long idPerHab) {
		this.idPerHab = idPerHab;
		this.idPerHabView = StringUtil.formatLong(idPerHab);
	}
	public String getIdPerHabView() {
		return idPerHabView;
	}
	public void setIdPerHabView(String idPerHabView) {
		this.idPerHabView = idPerHabView;
	}
	public List<EntHabVO> getListEntHab() {
		return listEntHab;
	}
	public void setListEntHab(List<EntHabVO> listEntHab) {
		this.listEntHab = listEntHab;
	}
	public List<EntVenVO> getListEntVen() {
		return listEntVen;
	}
	public void setListEntVen(List<EntVenVO> listEntVen) {
		this.listEntVen = listEntVen;
	}
	public List<HisEstHabVO> getListHisEstHab() {
		return listHisEstHab;
	}
	public void setListHisEstHab(List<HisEstHabVO> listHisEstHab) {
		this.listHisEstHab = listHisEstHab;
	}
	public List<PrecioEventoVO> getListPrecioEvento() {
		return listPrecioEvento;
	}
	public void setListPrecioEvento(List<PrecioEventoVO> listPrecioEvento) {
		this.listPrecioEvento = listPrecioEvento;
	}
	public String getLugarEventoStr() {
		return lugarEventoStr;
	}
	public void setLugarEventoStr(String lugarEventoStr) {
		this.lugarEventoStr = lugarEventoStr;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public PersonaVO getPerHab() {
		return perHab;
	}
	public void setPerHab(PersonaVO perHab) {
		this.perHab = perHab;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public TipoCobroVO getTipoCobro() {
		return tipoCobro;
	}
	public void setTipoCobro(TipoCobroVO tipoCobro) {
		this.tipoCobro = tipoCobro;
	}
	public TipoHabVO getTipoHab() {
		return tipoHab;
	}
	public void setTipoHab(TipoHabVO tipoHab) {
		this.tipoHab = tipoHab;
	}
	public ValoresCargadosVO getValoresCargados() {
		return valoresCargados;
	}
	public void setValoresCargados(ValoresCargadosVO valoresCargados) {
		this.valoresCargados = valoresCargados;
	}
	public String getParamTipoHab() {
		return paramTipoHab;
	}
	public void setParamTipoHab(String paramTipoHab) {
		this.paramTipoHab = paramTipoHab;
	}
	public String getIdView(){
		return StringUtil.formatLong(getId());
	}
	public TipoEventoVO getTipoEvento() {
		return tipoEvento;
	}
	public void setTipoEvento(TipoEventoVO tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	public List<HabExeVO> getListHabExe() {
		return listHabExe;
	}
	public void setListHabExe(List<HabExeVO> listHabExe) {
		this.listHabExe = listHabExe;
	}
	public ClaHab getClaHab() {
		return claHab;
	}
	public void setClaHab(ClaHab claHab) {
		this.claHab = claHab;
	}
	public ClaOrg getClaOrg() {
		return claOrg;
	}
	public void setClaOrg(ClaOrg claOrg) {
		this.claOrg = claOrg;
	}
	public void setLugarEvento(LugarEventoVO lugarEvento) {
		this.lugarEvento = lugarEvento;
	}
	public LugarEventoVO getLugarEvento() {
		return lugarEvento;
	}
	public Long getFactorOcupacional() {
		return factorOcupacional;
	}
	public void setFactorOcupacional(Long factorOcupacional) {
		this.factorOcupacional = factorOcupacional;
		this.factorOcupacionalView = StringUtil.formatLong(factorOcupacional);
	}
	public String getFactorOcupacionalView() {
		return factorOcupacionalView;
	}
	public void setFactorOcupacionalView(String factorOcupacionalView) {
		this.factorOcupacionalView = factorOcupacionalView;
	}
	public Integer getCantFunciones() {
		return cantFunciones;
	}
	public void setCantFunciones(Integer cantFunciones) {
		this.cantFunciones = cantFunciones;
		this.cantFuncionesView = StringUtil.formatInteger(cantFunciones);
	}
	public String getCantFuncionesView() {
		return cantFuncionesView;
	}
	public void setCantFuncionesView(String cantFuncionesView) {
		this.cantFuncionesView = cantFuncionesView;
	}
	public Long getIdDeudaInicial() {
		return idDeudaInicial;
	}
	public void setIdDeudaInicial(Long idDeudaInicial) {
		this.idDeudaInicial = idDeudaInicial;
	}

	public String getDesLugarEvento(){
		if(this.lugarEvento != null)
			return this.lugarEvento.getDescripcion();
		else
			return this.lugarEventoStr;
	}
	
	public Boolean getEntVenInternaBussEnabled() {
		return entVenInternaBussEnabled;
	}

	public void setEntVenInternaBussEnabled(Boolean entVenInternaBussEnabled) {
		this.entVenInternaBussEnabled = entVenInternaBussEnabled;
	}

	public Boolean getEntVenExternaBussEnabled() {
		return entVenExternaBussEnabled;
	}

	public void setEntVenExternaBussEnabled(Boolean entVenExternaBussEnabled) {
		this.entVenExternaBussEnabled = entVenExternaBussEnabled;
	}
	
	public String getEntVenExternaEnabled() {
		return this.getEntVenExternaBussEnabled() ? ENABLED : DISABLED;
	}
	
	public String getEntVenInternaEnabled() {
		return this.getEntVenInternaBussEnabled() ? ENABLED : DISABLED;
	}
	
}
