//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.bal.iface.model.CanalVO;
import ar.gov.rosario.siat.bal.iface.model.SelladoVO;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.BancoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del Recibo de Deuda y Recibo de Convenio.
 * Posee propiedades para soportar cualquiera de los dos
 * 
 * @author tecso
 *
 */
public class ReciboVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "reciboVO";
	
	// Propiedades Comunes
	private Long codRefPag;
	private ServicioBancoVO servicioBanco = new ServicioBancoVO();	
	private CanalVO canal = new CanalVO();
	private Long nroRecibo;
	private Integer anioRecibo;
	private Date fechaGeneracion;
	private Date fechaVencimiento;
	private Date fechaPago;
	private DesGenVO desGen = new DesGenVO();
	private DesEspVO desEsp = new DesEspVO();
	private SelladoVO sellado = new SelladoVO();
	private Double importeSellado;
	private Double totImporteRecibo;
	private BancoVO bancoPago = new BancoVO();
	private SiNo estaImpreso = SiNo.OpcionSelecionar;
	private ProcuradorVO procurador = new ProcuradorVO();
	private String observacion = "";
	private SiNo esVolPagIntRS = SiNo.NO;
	
	// Propiedades de Recibo de Deuda
	private RecursoVO recurso = new RecursoVO();
	private ViaDeudaVO viaDeuda = new ViaDeudaVO();
	private CuentaVO cuenta = new CuentaVO();
	private Double totCapitalOriginal;
	private Double desCapitalOriginal;
	private Double totActualizacion;
	private Double desActualizacion;
	private SiNo noLiqComPro = SiNo.OpcionSelecionar;
	private ProcesoMasivoVO procesoMasivo = new ProcesoMasivoVO();
	
	// Propiedades de Recibo de Convenio.
	private ConvenioVO convenio = new ConvenioVO();
	private SiNo esCuotaSaldo = SiNo.OpcionSelecionar;
	private String usuarioCuotaSaldo;


	private String anioReciboView = "";
	private String codRefPagView = "";
	private String desActualizacionView = "";
	private String desCapitalOriginalView = "";
	private String fechaGeneracionView = "";
	private String fechaPagoView = "";
	private String fechaVencimientoView = "";
	private String importeSelladoView = "";
	private String nroReciboView = "";
	private String totActualizacionView = "";
	private String totCapitalOriginalView = "";
	private String totImporteReciboView = "";

	// Constructores
	public ReciboVO() {
		super();
	}

	// Getters y Setters
	public Integer getAnioRecibo() {
		return anioRecibo;
	}
	public void setAnioRecibo(Integer anioRecibo) {
		this.anioRecibo = anioRecibo;
		this.anioReciboView = StringUtil.formatInteger(anioRecibo);
	}

	public BancoVO getBancoPago() {
		return bancoPago;
	}
	public void setBancoPago(BancoVO bancoPago) {
		this.bancoPago = bancoPago;
	}

	public CanalVO getCanal() {
		return canal;
	}
	public void setCanal(CanalVO canal) {
		this.canal = canal;
	}

	public Long getCodRefPag() {
		return codRefPag;
	}
	public void setCodRefPag(Long codRefPag) {
		this.codRefPag = codRefPag;
		this.codRefPagView = StringUtil.formatLong(codRefPag);
	}

	public ConvenioVO getConvenio() {
		return convenio;
	}
	public void setConvenio(ConvenioVO convenio) {
		this.convenio = convenio;
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

	public DesEspVO getDesEsp() {
		return desEsp;
	}
	public void setDesEsp(DesEspVO desEsp) {
		this.desEsp = desEsp;
	}

	public DesGenVO getDesGen() {
		return desGen;
	}
	public void setDesGen(DesGenVO desGen) {
		this.desGen = desGen;
	}

	public SiNo getEsCuotaSaldo() {
		return esCuotaSaldo;
	}
	public void setEsCuotaSaldo(SiNo esCuotaSaldo) {
		this.esCuotaSaldo = esCuotaSaldo;
	}

	public SiNo getEstaImpreso() {
		return estaImpreso;
	}
	public void setEstaImpreso(SiNo estaImpreso) {
		this.estaImpreso = estaImpreso;
	}

	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}
	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
		this.fechaGeneracionView = DateUtil.formatDate(fechaGeneracion, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
		this.fechaPagoView = DateUtil.formatDate(fechaPago, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
		this.fechaVencimientoView = DateUtil.formatDate(fechaVencimiento, DateUtil.ddSMMSYYYY_MASK);
	}

	public Double getImporteSellado() {
		return importeSellado;
	}
	public void setImporteSellado(Double importeSellado) {
		this.importeSellado = importeSellado;
		this.importeSelladoView = StringUtil.formatDouble(importeSellado);
	}

	public SiNo getNoLiqComPro() {
		return noLiqComPro;
	}
	public void setNoLiqComPro(SiNo noLiqComPro) {
		this.noLiqComPro = noLiqComPro;
	}

	public Long getNroRecibo() {
		return nroRecibo;
	}
	public void setNroRecibo(Long nroRecibo) {
		this.nroRecibo = nroRecibo;
		this.nroReciboView = StringUtil.formatLong(nroRecibo);
	}

	public ProcesoMasivoVO getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivoVO procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}

	public ProcuradorVO getProcurador() {
		return procurador;
	}
	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public SelladoVO getSellado() {
		return sellado;
	}
	public void setSellado(SelladoVO sellado) {
		this.sellado = sellado;
	}

	public ServicioBancoVO getServicioBanco() {
		return servicioBanco;
	}
	public void setServicioBanco(ServicioBancoVO servicioBanco) {
		this.servicioBanco = servicioBanco;
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

	public Double getTotImporteRecibo() {
		return totImporteRecibo;
	}
	public void setTotImporteRecibo(Double totImporteRecibo) {
		this.totImporteRecibo = totImporteRecibo;
		this.totImporteReciboView = StringUtil.formatDouble(totImporteRecibo);
	}

	public String getUsuarioCuotaSaldo() {
		return usuarioCuotaSaldo;
	}
	public void setUsuarioCuotaSaldo(String usuarioCuotaSaldo) {
		this.usuarioCuotaSaldo = usuarioCuotaSaldo;
	}

	public ViaDeudaVO getViaDeuda() {
		return viaDeuda;
	}
	public void setViaDeuda(ViaDeudaVO viaDeuda) {
		this.viaDeuda = viaDeuda;
	}
	
	public SiNo getEsVolPagIntRS() {
		return esVolPagIntRS;
	}
	
	public void setEsVolPagIntRS(SiNo esVolPagIntRS) {
		this.esVolPagIntRS = esVolPagIntRS;
	}
	// Buss flags getters y setters
	
	
	// View flags getters
	

	// View getters
	public void setAnioReciboView(String anioReciboView) {
		this.anioReciboView = anioReciboView;
	}
	public String getAnioReciboView() {
		return anioReciboView;
	}

	public void setCodRefPagView(String codRefPagView) {
		this.codRefPagView = codRefPagView;
	}
	public String getCodRefPagView() {
		return codRefPagView;
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

	public void setFechaGeneracionView(String fechaGeneracionView) {
		this.fechaGeneracionView = fechaGeneracionView;
	}
	public String getFechaGeneracionView() {
		return fechaGeneracionView;
	}

	public void setFechaPagoView(String fechaPagoView) {
		this.fechaPagoView = fechaPagoView;
	}
	public String getFechaPagoView() {
		return fechaPagoView;
	}

	public void setFechaVencimientoView(String fechaVencimientoView) {
		this.fechaVencimientoView = fechaVencimientoView;
	}
	public String getFechaVencimientoView() {
		return fechaVencimientoView;
	}

	public void setImporteSelladoView(String importeSelladoView) {
		this.importeSelladoView = importeSelladoView;
	}
	public String getImporteSelladoView() {
		return importeSelladoView;
	}

	public void setNroReciboView(String nroReciboView) {
		this.nroReciboView = nroReciboView;
	}
	public String getNroReciboView() {
		return nroReciboView;
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

	public void setTotImporteReciboView(String totImporteReciboView) {
		this.totImporteReciboView = totImporteReciboView;
	}
	public String getTotImporteReciboView() {
		return totImporteReciboView;
	}

	
}
