//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.bal.iface.model.SistemaVO;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del Convenio
 * @author tecso
 *
 */
public class ConvenioVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "convenioVO";
	
	private Integer nroConvenio;
	private PlanVO plan = new PlanVO();
	private CuentaVO cuenta = new CuentaVO();
	private RecursoVO recurso = new RecursoVO();
	private ViaDeudaVO viaDeuda = new ViaDeudaVO();
	private PlanDescuentoVO planDescuento = new PlanDescuentoVO();
	private PlanIntFinVO planIntFin = new PlanIntFinVO();
	private ProcuradorVO procurador = new ProcuradorVO();
	private EstadoConvenioVO estadoConvenio = new EstadoConvenioVO();
	private SistemaVO sistema = new SistemaVO();
	private String usuarioFor;
	private Date fechaFor;
	private TipoPerForVO tipoPerFor = new TipoPerForVO();
	private PersonaVO perFor = new PersonaVO();
	private TipoDocApoVO tipoDocApo = new TipoDocApoVO();
	private String observacionFor;
	private Double totCapitalOriginal;
	private Double desCapitalOriginal;
	private Double totActualizacion;
	private Double desActualizacion;
	private Double totInteres;
	private Double desInteres;
	private Double totImporteConvenio;
	private Integer cantidadCuotasPlan;
	private Integer ultCuoImp;
	private CasoVO casoManual = new CasoVO();
	private Integer noLiqComPro=0;
	private Date fechaAlta;

	private String cantidadCuotasPlanView = "";
	private String desActualizacionView = "";
	private String desCapitalOriginalView = "";
	private String desInteresView = "";
	private String fechaForView = "";
	private String fechaAltaView = "";
	private String nroConvenioView = "";
	private String totActualizacionView = "";
	private String totCapitalOriginalView = "";
	private String totImporteConvenioView = "";
	private String totInteresView = "";
	private String ultCuoImpView = "";


	// Constructores
	public ConvenioVO() {
		super();
	}

	// Getters y Setters
	public Integer getCantidadCuotasPlan() {
		return cantidadCuotasPlan;
	}

	public void setCantidadCuotasPlan(Integer cantidadCuotasPlan) {
		this.cantidadCuotasPlan = cantidadCuotasPlan;
		this.cantidadCuotasPlanView = StringUtil.formatInteger(cantidadCuotasPlan);
	}

	public CasoVO getCasoManual() {
		return casoManual;
	}

	public void setCasoManual(CasoVO casoManual) {
		this.casoManual = casoManual;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public Double getDesActualizacion() {
		return desActualizacion;
	}

	public void setDesActualizacion(Double desActualizacion) {
		this.desActualizacion = desActualizacion;
		this.desActualizacionView = StringUtil.formatDouble(desActualizacion);
	}

	public Double getDesCapitalOriginal() {
		return desCapitalOriginal;
	}

	public void setDesCapitalOriginal(Double desCapitalOriginal) {
		this.desCapitalOriginal = desCapitalOriginal;
		this.desCapitalOriginalView = StringUtil.formatDouble(desCapitalOriginal);
	}

	public Double getDesInteres() {
		return desInteres;
	}

	public void setDesInteres(Double desInteres) {
		this.desInteres = desInteres;
		this.desInteresView = StringUtil.formatDouble(desInteres);
	}

	public EstadoConvenioVO getEstadoConvenio() {
		return estadoConvenio;
	}

	public void setEstadoConvenio(EstadoConvenioVO estadoConvenio) {
		this.estadoConvenio = estadoConvenio;
	}

	public Date getFechaFor() {
		return fechaFor;
	}

	public void setFechaFor(Date fechaFor) {
		this.fechaFor = fechaFor;
		this.fechaForView = DateUtil.formatDate(fechaFor, DateUtil.ddSMMSYYYY_MASK);
	}

	public Integer getNroConvenio() {
		return nroConvenio;
	}

	public void setNroConvenio(Integer nroConvenio) {
		this.nroConvenio = nroConvenio;
		this.nroConvenioView = StringUtil.formatInteger(nroConvenio);
	}

	public String getObservacionFor() {
		return observacionFor;
	}

	public void setObservacionFor(String observacionFor) {
		this.observacionFor = observacionFor;
	}

	public PersonaVO getPerFor() {
		return perFor;
	}

	public void setPerFor(PersonaVO perFor) {
		this.perFor = perFor;
	}

	public PlanVO getPlan() {
		return plan;
	}

	public void setPlan(PlanVO plan) {
		this.plan = plan;
	}

	public PlanDescuentoVO getPlanDescuento() {
		return planDescuento;
	}

	public void setPlanDescuento(PlanDescuentoVO planDescuento) {
		this.planDescuento = planDescuento;
	}

	public PlanIntFinVO getPlanIntFin() {
		return planIntFin;
	}

	public void setPlanIntFin(PlanIntFinVO planIntFin) {
		this.planIntFin = planIntFin;
	}

	public ProcuradorVO getProcurador() {
		return procurador;
	}

	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}

	public SistemaVO getSistema() {
		return sistema;
	}

	public void setSistema(SistemaVO sistema) {
		this.sistema = sistema;
	}

	public TipoDocApoVO getTipoDocApo() {
		return tipoDocApo;
	}

	public void setTipoDocApo(TipoDocApoVO tipoDocApo) {
		this.tipoDocApo = tipoDocApo;
	}

	public TipoPerForVO getTipoPerFor() {
		return tipoPerFor;
	}

	public void setTipoPerFor(TipoPerForVO tipoPerFor) {
		this.tipoPerFor = tipoPerFor;
	}

	public Double getTotActualizacion() {
		return totActualizacion;
	}

	public void setTotActualizacion(Double totActualizacion) {
		this.totActualizacion = totActualizacion;
		this.totActualizacionView = StringUtil.formatDouble(totActualizacion);
	}

	public Double getTotCapitalOriginal() {
		return totCapitalOriginal;
	}

	public void setTotCapitalOriginal(Double totCapitalOriginal) {
		this.totCapitalOriginal = totCapitalOriginal;
		this.totCapitalOriginalView = StringUtil.formatDouble(totCapitalOriginal);
	}

	public Double getTotImporteConvenio() {
		return totImporteConvenio;
	}

	public void setTotImporteConvenio(Double totImporteConvenio) {
		this.totImporteConvenio = totImporteConvenio;
		this.totImporteConvenioView = StringUtil.formatDouble(totImporteConvenio);
	}

	public Double getTotInteres() {
		return totInteres;
	}

	public void setTotInteres(Double totInteres) {
		this.totInteres = totInteres;
		this.totInteresView = StringUtil.formatDouble(totInteres);
	}

	public Integer getUltCuoImp() {
		return ultCuoImp;
	}

	public void setUltCuoImp(Integer ultCuoImp) {
		this.ultCuoImp = ultCuoImp;
		this.ultCuoImpView = StringUtil.formatInteger(ultCuoImp);
	}

	public String getUsuarioFor() {
		return usuarioFor;
	}

	public void setUsuarioFor(String usuarioFor) {
		this.usuarioFor = usuarioFor;
	}

	public ViaDeudaVO getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeudaVO viaDeuda) {
		this.viaDeuda = viaDeuda;
	}
	
	
	
	
	
	
	// View flags getters
	
	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public Integer getNoLiqComPro() {
		return noLiqComPro;
	}

	public void setNoLiqComPro(Integer noLiqComPro) {
		this.noLiqComPro = noLiqComPro;
	}
	
	
	// Buss flags getters y setters
	
	

	/**
	 * Getter y Setter para que toVO() soporte utilizar la funcion "getDescEstadoConvenio()" del Bean Convenio.    
	 * 
	 */
	public String getDescEstadoConvenio() {
		return this.estadoConvenio.getDesEstadoConvenio();
	}
	public void setDescEstadoConvenio(String desc){
		this.estadoConvenio.setDesEstadoConvenio(desc);		
	} 
	
	// View getters
	public void setCantidadCuotasPlanView(String cantidadCuotasPlanView) {
		this.cantidadCuotasPlanView = cantidadCuotasPlanView;
	}
	public String getCantidadCuotasPlanView() {
		return cantidadCuotasPlanView;
	}

	public void setDesActualizacionView(String desActualizacionView) {
		this.desActualizacionView = desActualizacionView;
	}
	public String getDesActualizacionView() {
		return desActualizacionView;
	}

	public void setDesCapitalOriginalView(String desCapitalOriginalView) {
		this.desCapitalOriginalView = desCapitalOriginalView;
	}
	public String getDesCapitalOriginalView() {
		return desCapitalOriginalView;
	}

	public void setDesInteresView(String desInteresView) {
		this.desInteresView = desInteresView;
	}
	public String getDesInteresView() {
		return desInteresView;
	}

	public void setFechaForView(String fechaForView) {
		this.fechaForView = fechaForView;
	}
	public String getFechaForView() {
		return fechaForView;
	}

	public void setNroConvenioView(String nroConvenioView) {
		this.nroConvenioView = nroConvenioView;
	}
	public String getNroConvenioView() {
		return nroConvenioView;
	}

	public void setTotActualizacionView(String totActualizacionView) {
		this.totActualizacionView = totActualizacionView;
	}
	public String getTotActualizacionView() {
		return totActualizacionView;
	}

	public void setTotCapitalOriginalView(String totCapitalOriginalView) {
		this.totCapitalOriginalView = totCapitalOriginalView;
	}
	public String getTotCapitalOriginalView() {
		return totCapitalOriginalView;
	}

	public void setTotImporteConvenioView(String totImporteConvenioView) {
		this.totImporteConvenioView = totImporteConvenioView;
	}
	public String getTotImporteConvenioView() {
		return totImporteConvenioView;
	}

	public void setTotInteresView(String totInteresView) {
		this.totInteresView = totInteresView;
	}
	public String getTotInteresView() {
		return totInteresView;
	}

	public void setUltCuoImpView(String ultCuoImpView) {
		this.ultCuoImpView = ultCuoImpView;
	}
	public String getUltCuoImpView() {
		return ultCuoImpView;
	}

	public String getFechaAltaView() {
		return (fechaAlta != null)?DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_MASK):fechaAltaView;
	}

	public void setFechaAltaView(String fechaAltaView) {
		this.fechaAltaView = fechaAltaView;
	}
	
}
