//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.bal.iface.model.SistemaVO;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.EstadoDeudaVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.RepartidorVO;
import ar.gov.rosario.siat.rec.iface.model.ObraFormaPagoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del AuxDeuda
 * @author tecso
 *
 */
public class AuxDeudaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "auxDeudaVO";
	
	private Long codRefPag;
	private CuentaVO cuenta = new CuentaVO();
	private RecClaDeuVO recClaDeu = new RecClaDeuVO();
	private RecursoVO recurso = new RecursoVO();
	private ViaDeudaVO viaDeuda = new ViaDeudaVO();
	private EstadoDeudaVO estadoDeuda = new EstadoDeudaVO();
	private ServicioBancoVO servicioBanco = new ServicioBancoVO();
	private Long anio;
	private Long periodo;
	private Date fechaEmision;
	private Date fechaVencimiento;
	private Double importe; 
	private Double importeBruto;
	private Double saldo;
	private Double actualizacion;
	private SistemaVO sistema = new SistemaVO();
	private Long resto;
	private Double conc1;
	private Double conc2;
	private Double conc3;
	private Double conc4;
	private ObraFormaPagoVO obraFormaPago = new ObraFormaPagoVO();
	private RepartidorVO repartidor = new RepartidorVO();
	private EmisionVO emision = new EmisionVO();
	private Date fechaGracia;
	private String leyenda = "";
	private String atrAseVal = "";
	
	private String codRefPagView="";
	private String anioView="";
	private String periodoView="";
	private String fechaEmisionView="";
	private String fechaVencimientoView="";
	private String importeView=""; 
	private String importeBrutoView="";
	private String saldoView="";
	private String actualizacionView="";
	private String restoView="";
	private String conc1View="";
	private String conc2View="";
	private String conc3View="";
	private String conc4View="";
	private String fechaGraciaView="";
	
	// Constructores
	public AuxDeudaVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public AuxDeudaVO(int id, String desc) {
		super();
		setId(new Long(id));
	}

	// Getters y Setters
	public Double getActualizacion() {
		return actualizacion;
	}

	public void setActualizacion(Double actualizacion) {
		this.actualizacion = actualizacion;
		this.actualizacionView = StringUtil.formatDouble(actualizacion);
	}

	public String getActualizacionView() {
		return actualizacionView;
	}

	public void setActualizacionView(String actualizacionView) {
		this.actualizacionView = actualizacionView;
	}

	public Long getAnio() {
		return anio;
	}

	public void setAnio(Long anio) {
		this.anio = anio;
		this.anioView = StringUtil.formatLong(anio);
	}

	public String getAnioView() {
		return anioView;
	}

	public void setAnioView(String anioView) {
		this.anioView = anioView;
	}

	public Long getCodRefPag() {
		return codRefPag;
	}

	public void setCodRefPag(Long codRefPag) {
		this.codRefPag = codRefPag;
		this.codRefPagView = StringUtil.formatLong(codRefPag);
	}

	public String getCodRefPagView() {
		return codRefPagView;
	}

	public void setCodRefPagView(String codRefPagView) {
		this.codRefPagView = codRefPagView;
	}

	public Double getConc1() {
		return conc1;
	}

	public void setConc1(Double conc1) {
		this.conc1 = conc1;
		this.conc1View = StringUtil.formatDouble(conc1);
	}

	public String getConc1View() {
		return conc1View;
	}

	public void setConc1View(String conc1View) {
		this.conc1View = conc1View;
	}

	public Double getConc2() {
		return conc2;
	}

	public void setConc2(Double conc2) {
		this.conc2 = conc2;
		this.conc2View = StringUtil.formatDouble(conc2);
	}

	public String getConc2View() {
		return conc2View;
	}

	public void setConc2View(String conc2View) {
		this.conc2View = conc2View;
	}

	public Double getConc3() {
		return conc3;
	}

	public void setConc3(Double conc3) {
		this.conc3 = conc3;
		this.conc3View = StringUtil.formatDouble(conc3);
	}

	public String getConc3View() {
		return conc3View;
	}

	public void setConc3View(String conc3View) {
		this.conc3View = conc3View;
	}

	public Double getConc4() {
		return conc4;
	}

	public void setConc4(Double conc4) {
		this.conc4 = conc4;
		this.conc4View = StringUtil.formatDouble(conc4);
	}

	public String getConc4View() {
		return conc4View;
	}

	public void setConc4View(String conc4View) {
		this.conc4View = conc4View;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public EmisionVO getEmision() {
		return emision;
	}

	public void setEmision(EmisionVO emision) {
		this.emision = emision;
	}

	public EstadoDeudaVO getEstadoDeuda() {
		return estadoDeuda;
	}

	public void setEstadoDeuda(EstadoDeudaVO estadoDeuda) {
		this.estadoDeuda = estadoDeuda;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
		this.fechaEmisionView = DateUtil.formatDate(fechaEmision, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaEmisionView() {
		return fechaEmisionView;
	}

	public void setFechaEmisionView(String fechaEmisionView) {
		this.fechaEmisionView = fechaEmisionView;
	}

	public Date getFechaGracia() {
		return fechaGracia;
	}

	public void setFechaGracia(Date fechaGracia) {
		this.fechaGracia = fechaGracia;
		this.fechaGraciaView = DateUtil.formatDate(fechaGracia, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaGraciaView() {
		return fechaGraciaView;
	}

	public void setFechaGraciaView(String fechaGraciaView) {
		this.fechaGraciaView = fechaGraciaView;
	}

	public String getLeyenda() {
		return leyenda;
	}

	public void setLeyenda(String leyenda) {
		this.leyenda = leyenda;
	}

	public String getAtrAseVal() {
		return atrAseVal;
	}

	public void setAtrAseVal(String atrAseVal) {
		this.atrAseVal = atrAseVal;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
		this.fechaVencimientoView = DateUtil.formatDate(fechaVencimiento, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaVencimientoView() {
		return fechaVencimientoView;
	}

	public void setFechaVencimientoView(String fechaVencimientoView) {
		this.fechaVencimientoView = fechaVencimientoView;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe);
	}

	public Double getImporteBruto() {
		return importeBruto;
	}

	public void setImporteBruto(Double importeBruto) {
		this.importeBruto = importeBruto;
		this.importeBrutoView = StringUtil.formatDouble(importeBruto);
	}

	public String getImporteBrutoView() {
		return importeBrutoView;
	}

	public void setImporteBrutoView(String importeBrutoView) {
		this.importeBrutoView = importeBrutoView;
	}

	public String getImporteView() {
		return importeView;
	}

	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}

	public ObraFormaPagoVO getObraFormaPago() {
		return obraFormaPago;
	}

	public void setObraFormaPago(ObraFormaPagoVO obraFormaPago) {
		this.obraFormaPago = obraFormaPago;
	}

	public Long getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
		this.periodoView = StringUtil.formatLong(periodo);
	}

	public String getPeriodoView() {
		return periodoView;
	}

	public void setPeriodoView(String periodoView) {
		this.periodoView = periodoView;
	}

	public RecClaDeuVO getRecClaDeu() {
		return recClaDeu;
	}

	public void setRecClaDeu(RecClaDeuVO recClaDeu) {
		this.recClaDeu = recClaDeu;
	}
	
	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public RepartidorVO getRepartidor() {
		return repartidor;
	}

	public void setRepartidor(RepartidorVO repartidor) {
		this.repartidor = repartidor;
	}

	public Long getResto() {
		return resto;
	}

	public void setResto(Long resto) {
		this.resto = resto;
		this.restoView = StringUtil.formatLong(resto);
	}

	public String getRestoView() {
		return restoView;
	}

	public void setRestoView(String restoView) {
		this.restoView = restoView;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
		this.saldoView = StringUtil.formatDouble(saldo);
	}

	public String getSaldoView() {
		return saldoView;
	}

	public void setSaldoView(String saldoView) {
		this.saldoView = saldoView;
	}

	public ServicioBancoVO getServicioBanco() {
		return servicioBanco;
	}

	public void setServicioBanco(ServicioBancoVO servicioBanco) {
		this.servicioBanco = servicioBanco;
	}

	public SistemaVO getSistema() {
		return sistema;
	}

	public void setSistema(SistemaVO sistema) {
		this.sistema = sistema;
	}

	public ViaDeudaVO getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeudaVO viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

}
