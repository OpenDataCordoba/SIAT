//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.BussImageModel;

/**
 * @author tecso
 *
 */
public class DeudaVO extends BussImageModel {
	private static final long serialVersionUID = 0;

	private CuentaVO cuenta = new CuentaVO(); 
	private ProcuradorVO procurador = new ProcuradorVO();
	private Long anio;
	private Long periodo;
	private Date fechaEmision;
	private Date fechaVencimiento;
	private Double importe;
	private Double saldo;
	private Double saldoActualizado;
	private Date fechaPago;
	private RecClaDeuVO recClaDeu = new RecClaDeuVO();
	private EstadoDeudaVO estadoDeuda = new EstadoDeudaVO();
	
	private Long codRefPag;
	
	private String strPeriodo;
	private String codRefPagView = "";
	
	private String anioView = "";
	private String periodoView = "";
	private String importeView = "";
	private String saldoView = "";
	
	private String fechaVencimientoView = "";
	private String fechaEmisionView ="";
	
	private ViaDeudaVO viaDeuda = new ViaDeudaVO();
	private ServicioBancoVO servicioBanco = new ServicioBancoVO();
	
	private EmisionVO emision = new EmisionVO();

	private List listDeuRecCon = new ArrayList(); // tendria que ser <DeuRecConVO>
	public DeudaVO() {
		super();
	}
	
	public ViaDeudaVO getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeudaVO viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

	public ServicioBancoVO getServicioBanco() {
		return servicioBanco;
	}

	public void setServicioBanco(ServicioBancoVO servicioBanco) {
		this.servicioBanco = servicioBanco;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public ProcuradorVO getProcurador() {
		return procurador;
	}
	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}
	
	public Long getAnio() {
		return anio;
	}
	public void setAnio(Long anio) {
		this.anio = anio;
		this.anioView = StringUtil.formatLong(anio);
	}

	public Long getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
		this.periodoView = StringUtil.formatLong(periodo);		
	}
	
	public Date getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
		this.fechaEmisionView = DateUtil.formatDate(fechaEmision, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimientoView = DateUtil.formatDate(fechaVencimiento, DateUtil.ddSMMSYYYY_MASK);
		this.fechaVencimiento = fechaVencimiento;
	}
	
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;

		int cantDec=SiatParam.DEC_IMPORTE_VIEW;
		if (importe< 0.01D && importe > 0D)
			cantDec=6;
		this.importeView = (importe!=null?StringUtil.redondearDecimales(importe, 1, cantDec):"");
	}
	
	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
		
		int cantDec=SiatParam.DEC_IMPORTE_VIEW;
		if (saldo< 0.01D && saldo > 0D)
			cantDec=6;
		this.saldoView = (saldo!=null?StringUtil.redondearDecimales(saldo, 1, cantDec):"");
	}
	
	public Double getSaldoActualizado() {
		return saldoActualizado;
	}
	public void setSaldoActualizado(Double saldoActualizado) {
		this.saldoActualizado = saldoActualizado;
	}

	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public RecClaDeuVO getRecClaDeu() {
		return recClaDeu;
	}
	public void setRecClaDeu(RecClaDeuVO recClaDeu) {
		this.recClaDeu = recClaDeu;
	}

	public EstadoDeudaVO getEstadoDeuda() {
		return estadoDeuda;
	}

	public void setEstadoDeuda(EstadoDeudaVO estadoDeuda) {
		this.estadoDeuda = estadoDeuda;
	}

	public String getAnioView() {
		return anioView;
	}
	public void setAnioView(String anioView) {
		this.anioView = anioView;
	}

	public String getPeriodoView() {
		return periodoView;
	}
	public void setPeriodoView(String periodoView) {
		this.periodoView = periodoView;
	}

	public String getImporteView() {
		return importeView;
	}
	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}

	public String getFechaVencimientoView() {
		return fechaVencimientoView;
	}
	public void setFechaVencimientoView(String fechaVencimientoView) {
		this.fechaVencimientoView = fechaVencimientoView;
	}

	public String getFechaEmisionView() {
		return fechaEmisionView;
	}

	public void setFechaEmisionView(String fechaEmisionView) {
		this.fechaEmisionView = fechaEmisionView;
	}

	public String getSaldoView() {
		return saldoView;
	}
	public void setSaldoView(String saldoView) {
		this.saldoView = saldoView;
	}

	public String getSaldoActualizadoView(){
		return StringUtil.redondearDecimales(this.getSaldoActualizado(), 1, 2);
	}
	
	public List getListDeuRecCon() {
		return listDeuRecCon;
	}
	public void setListDeuRecCon(List listDeuRecCon) {
		this.listDeuRecCon = listDeuRecCon;
	}
	public EmisionVO getEmision() {
		return emision;
	}
	public void setEmision(EmisionVO emision) {
		this.emision = emision;
	}

	public String getStringSolicitud() {
		String deudaString = "Anio: " + this.getAnioView() +
							 " Periodo: " + this.getPeriodoView() +
							 " Fecha Vto: " + DateUtil.formatDate(this.getFechaVencimiento(),DateUtil.dd_MM_YYYY_MASK) +
							 " Fecha Emision: " + DateUtil.formatDate(this.getFechaEmision(),DateUtil.dd_MM_YYYY_MASK) +
							 " Importe: " + this.getImporteView() + 
							 " Saldo: " + this.getSaldoView();
		return deudaString;
	}
	
	public String getAnioPeriodoView(){
		return this.getAnio().toString() + "/" +this.getPeriodo().toString();
	}
	
	public String getNroBarraAnio(){
		return getAnioPeriodoView();
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

	public void setStrPeriodo(String strPeriodo) {
		this.strPeriodo = strPeriodo;
	}

	public String getStrPeriodo() {
		return strPeriodo;
	}
}
