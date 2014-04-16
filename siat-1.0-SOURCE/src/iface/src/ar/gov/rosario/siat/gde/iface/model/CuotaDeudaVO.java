//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * 
 * Esta clase se utiliza para visualizar cuotas de un convenio desde la gestion de la deuda.
 * Tambien en la formalizacion de un convenio para mostrar las alternativas cuotas nro, anticipo y restantes
 * y para mostrar la simulacion de convenio  
 * 
 * 
 * @author Tecso
 */
public class CuotaDeudaVO {
	//Cuota
	private Long idCuota;
	
	private String nroCuota;
	private String fechaVto;
    private Double capital;
    private Double interes;
    private Double actualizacion;
    private String nroCuotaImputada = "";
    private Double importeCuota; 	// Importe original de la cuota. 
    private Double recargo; 		// Utilizado cuando se realiza una reconfeccion.
    
    private Double total; // Importe mas recargo.
    
    private Double anticipo;
    private Double valorCuotasRestantes;
    
    //  de aca para bajo es para cuotas pagas.
    
    private String fechaPago;  
    private Double importe;
    private Long caja;
    private Long codPago;   //?
    private String desEstado;
    private Double saldoEnPlanCub;
    
    private String msgErrorCuota = "";
    
    //Deuda
	private Long anio;
	private Long periodo;
	private Date fechaEmision;
	private Date fechaVencimiento;
	private Double saldo;
	private Double saldoActualizado;
	private RecClaDeuVO recClaDeu = new RecClaDeuVO();
	private EstadoDeudaVO estadoDeuda = new EstadoDeudaVO();
	
	private String anioView = "";
	private String periodoView = "";
	private String importeView = "";
	private String saldoView = "";
	private String saldoEnPlanCubView ="";
	
	private String fechaVencimientoView = "";
	private String fechaEmisionView ="";
    
	//  Propiedades para la asignacion de permisos
	private boolean esSeleccionable = false; // Poder operar sobre una deuda, por negocio o permiso
	private boolean reclamarAcentEnabled = false;
	
	
	
	// Getters y Setters
	public Long getCaja() {
		return caja;
	}

	public void setCaja(Long caja) {
		this.caja = caja;
	}

	public Double getCapital() {
		return capital;
	}

	public void setCapital(Double capital) {
		this.capital = capital;
	}

	public Long getCodPago() {
		return codPago;
	}

	public void setCodPago(Long codPago) {
		this.codPago = codPago;
	}

	public String getDesEstado() {
		return desEstado;
	}

	public void setDesEstado(String desEstado) {
		this.desEstado = desEstado;
	}

	public String getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}

	public String getFechaVto() {
		return fechaVto;
	}

	public void setFechaVto(String fechaVto) {
		this.fechaVto = fechaVto;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Double getInteres() {
		return interes;
	}

	public void setInteres(Double interes) {
		this.interes = interes;
	}

	public String getNroCuota() {
		return nroCuota;
	}

	public void setNroCuota(String nroCuota) {
		this.nroCuota = nroCuota;
	}

	public Double getActualizacion() {
		return actualizacion;
	}

	public void setActualizacion(Double recargo) {
		this.actualizacion = recargo;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Long getIdCuota() {
		return idCuota;
	}

	public void setIdCuota(Long idCuota) {
		this.idCuota = idCuota;
	}

	public Double getAnticipo() {
		return anticipo;
	}

	public void setAnticipo(Double anticipo) {
		this.anticipo = anticipo;
	}

	public Double getValorCuotasRestantes() {
		return valorCuotasRestantes;
	}

	public void setValorCuotasRestantes(Double valorCuotasRestantes) {
		this.valorCuotasRestantes = valorCuotasRestantes;
	}

	public Double getRecargo() {
		return recargo;
	}

	public void setRecargo(Double recargo) {
		this.recargo = recargo;
	}

	public Double getImporteCuota() {
		return importeCuota;
	}

	public void setImporteCuota(Double importeCuota) {
		this.importeCuota = importeCuota;
	}

	public String getMsgErrorCuota() {
		return msgErrorCuota;
	}
	public void setMsgErrorCuota(String msgErrorCuota) {
		this.msgErrorCuota = msgErrorCuota;
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
	

	
	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
		this.saldoView = StringUtil.formatDouble(saldo);
	}
	
	public Double getSaldoActualizado() {
		return saldoActualizado;
	}
	public void setSaldoActualizado(Double saldoActualizado) {
		this.saldoActualizado = saldoActualizado;
	}



	public Double getSaldoEnPlanCub() {
		return saldoEnPlanCub;
	}

	public void setSaldoEnPlanCub(Double saldoEnPlanCub) {
		this.saldoEnPlanCub = saldoEnPlanCub;
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
		return (this.recClaDeu.getAbrClaDeu()!=null?this.recClaDeu.getAbrClaDeu()+" "+ periodoView:periodoView);
	}
	public void setPeriodoView(String periodoView) {
		this.periodoView = periodoView;
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
		return (saldo!=null?StringUtil.redondearDecimales(saldo, 1, SiatParam.DEC_IMPORTE_VIEW):"");
	}
	public void setSaldoView(String saldoView) {
		this.saldoView = saldoView;
	}

	public String getSaldoActualizadoView(){
		return (saldoActualizado!=null?StringUtil.redondearDecimales(saldoActualizado, 1, SiatParam.DEC_IMPORTE_VIEW):"");
	}
	
	

	public String getNroCuotaImputada() {
		return nroCuotaImputada;
	}

	public void setNroCuotaImputada(String nroCuotaImputada) {
		this.nroCuotaImputada = nroCuotaImputada;
	}

	// view getters
	public String getCapitalView(){
		return (capital!=null?StringUtil.redondearDecimales(capital, 1, SiatParam.DEC_IMPORTE_VIEW):"");
	}
	
	public String getSaldoEnPlanCubView(){
		return (saldoEnPlanCub!=null?StringUtil.redondearDecimales(saldoEnPlanCub, 1, SiatParam.DEC_IMPORTE_VIEW):"");
	}
	
	public String getInteresView(){
		return (interes!=null?StringUtil.redondearDecimales(interes, 1, SiatParam.DEC_IMPORTE_VIEW):"");
	}
	
	public String getActualizacionView(){
		return (actualizacion!=null?StringUtil.redondearDecimales(actualizacion, 1, SiatParam.DEC_IMPORTE_VIEW):"");
	}	
	
	public String getTotalView(){
		return (total!=null?StringUtil.redondearDecimales(total, 1, SiatParam.DEC_IMPORTE_VIEW):"");
	}
	
	public String getRecargoView(){
		return (recargo!=null?StringUtil.redondearDecimales(recargo, 1, SiatParam.DEC_IMPORTE_VIEW):"");
	}
	
	public String getImporteCuotaView(){
		return (importeCuota!=null?StringUtil.redondearDecimales(importeCuota, 1, SiatParam.DEC_IMPORTE_VIEW):"");
	}
	
	public String getPeriodoDeudaView(){
		return (this.getPeriodoView()+"/"+this.getAnioView());
	}
	
	// Metod para resolver logica o permisos en la vista
	public boolean getEsSeleccionable() {
		return esSeleccionable;
	}

	public void setEsSeleccionable(boolean esSeleccionable) {
		this.esSeleccionable = esSeleccionable;
	}

	
	public boolean getReclamarAcentEnabled() {
		return reclamarAcentEnabled;
	}

	public void setReclamarAcentEnabled(boolean reclamarAcentEnabled) {
		this.reclamarAcentEnabled = reclamarAcentEnabled;
	}

	
}
