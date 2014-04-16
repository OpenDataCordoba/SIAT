//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

 package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
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
public class LiqCuotaVO {

	private Long idCuota;
	
	private String codRefPag = "";
	private String nroCuota;
	private String fechaVto;
    private Double capital;
    private Double interes;
    private Double actualizacion;
    private Double importeSellado;
    private String nroConvenio;
    private Double importeCuota; 	// Importe original de la cuota. 
    private Double recargo; 		// Utilizado cuando se realiza una reconfeccion.
    
    private Double total; // Importe mas recargo.
    
    private Double anticipo;
    private Double valorCuotasRestantes;
    private String nroCuenta;
    
    //  de aca para bajo es para cuotas pagas.
    
    private String fechaPago;  
    private Double importe;
    private Long caja;
    private Long codPago;   //?
    private String desEstado;
    private String desRecurso="";
    
    private String msgErrorCuota = "";
    
    private boolean poseeObservacion = false;
    private boolean esIndeterminada = false;
    private boolean esReclamada = false;
    private String observacion = "";
    private String tipoPago="";
    private String ordenanza="";
    
	//  Propiedades para la asignacion de permisos
	private boolean esSeleccionable = false; // Poder operar sobre una deuda, por negocio o permiso
	private boolean reclamarAcentEnabled = false;
	
	
	private String capitalView = "";
	private String interesView = "";	
	private String actualizacionView = "";
	private String recargoView = "";
	private String importeView = "";
	private String importeCuotaView = "";
	private String totalView = "";
	private String fechaVtoView = "";
	
	
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
		this.capitalView = StringUtil.formatDouble(capital);
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
		this.importeView = StringUtil.formatDouble(importe);
	}

	public Double getInteres() {
		return interes;
	}

	public void setInteres(Double interes) {
		this.interes = interes;
		this.interesView = StringUtil.formatDouble(interes);
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

	public void setActualizacion(Double actualizacion) {
		this.actualizacion = actualizacion;
		this.actualizacionView = StringUtil.formatDouble(actualizacion);
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
		this.totalView = StringUtil.formatDouble(total);
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
		this.recargoView = StringUtil.formatDouble(recargo);
	}

	public Double getImporteCuota() {
		return importeCuota;
	}

	public void setImporteCuota(Double importeCuota) {
		this.importeCuota = importeCuota;
		this.importeCuotaView = StringUtil.formatDouble(importeCuota);
	}

	public String getNroConvenio() {
		return nroConvenio;
	}

	public void setNroConvenio(String nroConvenio) {
		this.nroConvenio = nroConvenio;
	}

	public String getMsgErrorCuota() {
		return msgErrorCuota;
	}
	public void setMsgErrorCuota(String msgErrorCuota) {
		this.msgErrorCuota = msgErrorCuota;
	}

	public boolean isEsIndeterminada() {
		return esIndeterminada;
	}
	public void setEsIndeterminada(boolean esIndeterminada) {
		this.esIndeterminada = esIndeterminada;
	}

	public boolean isEsReclamada() {
		return esReclamada;
	}
	public void setEsReclamada(boolean esReclamada) {
		this.esReclamada = esReclamada;
	}

	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public boolean isPoseeObservacion() {
		return poseeObservacion;
	}
	public void setPoseeObservacion(boolean poseeObservacion) {
		this.poseeObservacion = poseeObservacion;
	}
	

	public String getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}

	// view getters
	public String getCapitalView(){
		return (capital!=null?StringUtil.redondearDecimales(capital, 1, SiatParam.DEC_IMPORTE_VIEW):"");		
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
	
	public String getAnticipoView(){
		return (anticipo!=null?StringUtil.redondearDecimales(anticipo, 1, SiatParam.DEC_IMPORTE_VIEW):"");
	}
	
	public String getValorCuotasRestantesView(){
		return (valorCuotasRestantes!=null?StringUtil.redondearDecimales(valorCuotasRestantes, 1, SiatParam.DEC_IMPORTE_VIEW):"");
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

	public Double getImporteSellado() {
		return importeSellado;
	}

	public void setImporteSellado(Double importeSellado) {
		this.importeSellado = importeSellado;
	}

	public void setOrdenanza(String ordenanza) {
		this.ordenanza = ordenanza;		
	}
	
	public String getOrdenanza() {
		return ordenanza;
	}

	public String getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public String getDesRecurso() {
		return desRecurso;
	}

	public void setDesRecurso(String desRecurso) {
		this.desRecurso = desRecurso;
	}

	
	// View getters y setters
	public void setCapitalView(String capitalView) {
		this.capitalView = capitalView;
	}

	public void setInteresView(String interesView) {
		this.interesView = interesView;
	}

	public void setActualizacionView(String actualizacionView) {
		this.actualizacionView = actualizacionView;
	}

	public void setRecargoView(String recargoView) {
		this.recargoView = recargoView;
	}

	/*public String getCapitalView() {
		return capitalView;
	}
	public String getInteresView() {
		return interesView;
	}
	public String getActualizacionView() {
		return actualizacionView;
	}
	public String getRecargoView() {
		return recargoView;
	}
	public String getImporteCuotaView() {
		return importeCuotaView;
	}
	public String getTotalView() {
		return totalView;
	}*/

	public void setImporteCuotaView(String importeCuotaView) {
		this.importeCuotaView = importeCuotaView;
	}

	public void setTotalView(String totalView) {
		this.totalView = totalView;
	}

	public String getFechaVtoView() {
		return fechaVtoView;
	}
	public void setFechaVtoView(String fechaVtoView) {
		this.fechaVtoView = fechaVtoView;
	}

	public String getImporteView() {
		return importeView;
	}
	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}

	public String getCodRefPag() {
		return codRefPag;
	}
	public void setCodRefPag(String codRefPag) {
		this.codRefPag = codRefPag;
	}
	
	public String getCodRefPagView() {
		return "0".equals(codRefPag)?"":codRefPag;
	}
	
}