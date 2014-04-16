//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Value Object del LiqDetalleDeuda
 * @author tecso
 *
 */
public class LiqDetalleDeudaVO {
	
	// Propiedad utiliza para retornar al adapter de la Deuda.
	private Long  idCuenta;
	private Long  idDeuda;
	private Long  idViaDeuda;
	private Long  idEstadoDeuda;
	
	private String periodoDeuda = "";
	private String fechaEmision = ""; 
	private String desRecurso = "";
	private String nroCuenta = "";
	private String desViaDeuda = "";
	private String desClasificacionDeuda = "";
	private String desServicioBanco = "";
	private String desEstadoDeuda = "";
	private Double importe = 0D;
	private Double saldo = 0D;
	private String fechaVencimiento = "";
	private Double actualizacion = 0D;
	
	private Date fechaPago;
	private String fechaPagoView = "";
	
	private List<LiqConceptoDeudaVO> listConceptos = new ArrayList<LiqConceptoDeudaVO>();
	private List<LiqPagoDeudaVO> listPagos = new ArrayList<LiqPagoDeudaVO>();
	private LiqAtrEmisionVO liqAtrEmisionVO = new LiqAtrEmisionVO();

	private String importeView = "";
	private String saldoView = "";
	private String actualizacionView = "";
	
	// Datos del Procurador
	private boolean poseeProcurador = false;
	private String desProcurador = ""; 
	private String domProcurador = ""; 
	private String telProcurador = ""; 
	private String horAteProcurador = "";

	// Datos de la anulacion
	private boolean deudaAnulada = false;
	private String fechaAnulacionView = "";
	private String desMotAnuDeu = "";
	private String usuarioAnuDeu = "";

	// Usuario que Emitio la deuda
	private String usuarioEmision = "";
	
	//  Getters y Setters
	public String getDesClasificacionDeuda() {
		return desClasificacionDeuda;
	}

	public void setDesClasificacionDeuda(String desClasificacionDeuda) {
		this.desClasificacionDeuda = desClasificacionDeuda;
	}

	public String getDesEstadoDeuda() {
		return desEstadoDeuda;
	}

	public void setDesEstadoDeuda(String desEstadoDeuda) {
		this.desEstadoDeuda = desEstadoDeuda;
	}

	public String getDesRecurso() {
		return desRecurso;
	}

	public void setDesRecurso(String desRecurso) {
		this.desRecurso = desRecurso;
	}

	public String getDesServicioBanco() {
		return desServicioBanco;
	}

	public void setDesServicioBanco(String desServicioBanco) {
		this.desServicioBanco = desServicioBanco;
	}

	public String getDesViaDeuda() {
		return desViaDeuda;
	}

	public void setDesViaDeuda(String desViaDeuda) {
		this.desViaDeuda = desViaDeuda;
	}

	public String getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Long getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(Long idCuenta) {
		this.idCuenta = idCuenta;
	}

	public Long getIdDeuda() {
		return idDeuda;
	}

	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe);
	}

	public String getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public String getPeriodoDeuda() {
		return periodoDeuda;
	}

	public void setPeriodoDeuda(String periodoDeuda) {
		this.periodoDeuda = periodoDeuda;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
		this.saldoView = StringUtil.formatDouble(saldo);
	}

	public List<LiqConceptoDeudaVO> getListConceptos() {
		return listConceptos;
	}

	public void setListConceptos(List<LiqConceptoDeudaVO> listConceptos) {
		this.listConceptos = listConceptos;
	}

	public List<LiqPagoDeudaVO> getListPagos() {
		return listPagos;
	}

	public void setListPagos(List<LiqPagoDeudaVO> listPagos) {
		this.listPagos = listPagos;
	}

	
	public Long getIdViaDeuda() {
		return idViaDeuda;
	}

	public void setIdViaDeuda(Long idViaDeuda) {
		this.idViaDeuda = idViaDeuda;
	}

	public Long getIdEstadoDeuda() {
		return idEstadoDeuda;
	}

	public void setIdEstadoDeuda(Long idEstadoDeuda) {
		this.idEstadoDeuda = idEstadoDeuda;
	}

	public LiqAtrEmisionVO getLiqAtrEmisionVO() {
		return liqAtrEmisionVO;
	}

	public void setLiqAtrEmisionVO(LiqAtrEmisionVO liqAtrEmisionVO) {
		this.liqAtrEmisionVO = liqAtrEmisionVO;
	}

	public String getImporteView() {
		return importeView;
	}

	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}

	public String getSaldoView() {
		return saldoView;
	}

	public void setSaldoView(String saldoView) {
		this.saldoView = saldoView;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
		this.fechaPagoView = DateUtil.formatDate(fechaPago, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaPagoView() {
		return fechaPagoView;
	}
	
	public void setFechaPagoView(String fechaPagoView) {
		this.fechaPagoView = fechaPagoView;
	}

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

	public String getDesMotAnuDeu() {
		return desMotAnuDeu;
	}

	public void setDesMotAnuDeu(String desMotAnuDeu) {
		this.desMotAnuDeu = desMotAnuDeu;
	}
	
	public String getUsuarioAnuDeu() {
		return usuarioAnuDeu;
	}
	
	public void setUsuarioAnuDeu(String usuarioAnuDeu) {
		this.usuarioAnuDeu = usuarioAnuDeu;
	}

	public String getDesProcurador() {
		return desProcurador;
	}

	public void setDesProcurador(String desProcurador) {
		this.desProcurador = desProcurador;
	}

	public boolean isDeudaAnulada() {
		return deudaAnulada;
	}

	public void setDeudaAnulada(boolean deudaAnulada) {
		this.deudaAnulada = deudaAnulada;
	}

	public String getDomProcurador() {
		return domProcurador;
	}

	public void setDomProcurador(String domProcurador) {
		this.domProcurador = domProcurador;
	}

	public String getFechaAnulacionView() {
		return fechaAnulacionView;
	}

	public void setFechaAnulacionView(String fechaAnulacionView) {
		this.fechaAnulacionView = fechaAnulacionView;
	}

	public String getHorAteProcurador() {
		return horAteProcurador;
	}

	public void setHorAteProcurador(String horAteProcurador) {
		this.horAteProcurador = horAteProcurador;
	}

	public boolean isPoseeProcurador() {
		return poseeProcurador;
	}

	public void setPoseeProcurador(boolean poseeProcurador) {
		this.poseeProcurador = poseeProcurador;
	}

	public String getTelProcurador() {
		return telProcurador;
	}

	public void setTelProcurador(String telProcurador) {
		this.telProcurador = telProcurador;
	}

	public String getUsuarioEmision() {
		return usuarioEmision;
	}

	public void setUsuarioEmision(String usuarioEmision) {
		this.usuarioEmision = usuarioEmision;
	}

}
