//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import java.util.Date;

import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class LiqDeudaPrivilegioVO {

	private Long 	idDeuda;
	private Long    idViaDeuda;
	private Long 	idLink;
	private Long    idEstadoDeuda;
	private Date 	fechaVencimiento;

	private String 	periodoDeuda=""; // anio/periodo: para liquidacion  anio/periodo/ajuste para convenios
	private String 	desViaDeuda ="";
	private String 	desEstado="";
	private String 	fechaVto="";
	private String 	fechaEmision="";
	private Double 	importe =0D; // = Declarado para recursos autoliquidables
	private Double 	saldo=0D;
	private Double	actualizacion=0D;
	private Double 	total=0D;
	private String 	observacion="";
	private String 	sistema="";     //para deuda en convenio
	private Double 	recargo=0D;     //para deuda en convenio
	private String 	fechaPago="";   //para deuda en convenio
	private String  anio ="";
	private String  periodo="";
	private String  nroCuenta="";
	private String  desRecurso="";

	private Long   idTipoPrivilegio;
	private String desTipoPrivilegio=""; // General, Especial o Quirografario.
		
	private Long idCuenta;
	private String numeroCuenta="";
	
	private boolean poseeObservacion = false; //indica si se debe mostrar la observacion o mostrar los valores de la deuda
	private boolean poseeConvenio = false;
	private boolean esExentoPago = false;
	private boolean esReclamada = false;
	private boolean esIndeterminada = false;
	private boolean esNoHabilitada = false; // Si la constanica de la deuda no esta habilitada
	
	//  Propiedades para la asignacion de permisos
	private boolean esSeleccionable = false; 		// Poder operar sobre una deuda, por negocio o permiso SWE
	private boolean verDetalleDeudaEnabled = false;	// Poder ver el detalle de los conceptos, pagos, etc de la deuda
	private boolean reclamarAcentEnabled = false;	// Permite ir a la pantalla de Reclamar asentamiento
	private boolean vueltaAtrasAnularEnabled = true; // Poder volver atrar una anulacion de deuda, por negocio o permiso SWE
	
	
	// Getters y Setters
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

	public Long getIdDeuda() {
		return idDeuda;
	}
	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}

	public Long getIdLink() {
		return idLink;
	}
	public void setIdLink(Long idLink) {
		this.idLink = idLink;
	}

	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getPeriodoDeuda() {
		return periodoDeuda;
	}
	public void setPeriodoDeuda(String periodoDeuda) {
		this.periodoDeuda = periodoDeuda;
	}

	public String getSistema() {
		return sistema;
	}
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	
	public Double getActualizacion() {
		return actualizacion;
	}
	public void setActualizacion(Double actualizacion) {
		this.actualizacion = actualizacion;
	}

	public Double getRecargo() {
		return recargo;
	}
	public void setRecargo(Double recargo) {
		this.recargo = recargo;
	}

	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
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

	// Metod para resolver logica o permisos en la vista
	public boolean getEsSeleccionable() {
		return esSeleccionable;
	}
	public void setEsSeleccionable(boolean esSeleccionable) {
		this.esSeleccionable = esSeleccionable;
	}
	
	public boolean isReclamarAcentEnabled() {
		return reclamarAcentEnabled;
	}
	public void setReclamarAcentEnabled(boolean reclamarAcentEnabled) {
		this.reclamarAcentEnabled = reclamarAcentEnabled;
	}

	public boolean isVerDetalleDeudaEnabled() {
		return verDetalleDeudaEnabled;
	}
	public void setVerDetalleDeudaEnabled(boolean verDetalleDeudaEnabled) {
		this.verDetalleDeudaEnabled = verDetalleDeudaEnabled;
	}

	public boolean getPoseeConvenio() {
		return poseeConvenio;
	}
	public void setPoseeConvenio(boolean poseeConvenio) {
		this.poseeConvenio = poseeConvenio;
	}
	
	public boolean getPoseeObservacion() {
		return poseeObservacion;
	}
	public void setPoseeObservacion(boolean poseeObservacion) {
		this.poseeObservacion = poseeObservacion;
	}
	
	public boolean isEsReclamada() {
		return esReclamada;
	}
	public void setEsReclamada(boolean esReclamada) {
		this.esReclamada = esReclamada;
	}
	
	public boolean isEsIndeterminada() {
		return esIndeterminada;
	}
	public void setEsIndeterminada(boolean esIndeterminada) {
		this.esIndeterminada = esIndeterminada;
	}
	

	public boolean isEsExentoPago() {
		return esExentoPago;
	}
	public void setEsExentoPago(boolean esExentoPago) {
		this.esExentoPago = esExentoPago;
	}

	public boolean isEsNoHabilitada() {
		return esNoHabilitada;
	}
	public void setEsNoHabilitada(boolean esNoHabilitada) {
		this.esNoHabilitada = esNoHabilitada;
	}

	/**
	 * Devuelve al concatenacion de idDeuda + idEstadoDeuda para realizar la acciones que impliquen seleccion de registros de deuda.
	 * 
	 * @author Cristian
	 * @return idDeuda + "-" + idEstadoDeuda
	 */
	public String getIdSelect(){
		String strIdDeuda = "" + getIdDeuda();
		String strIdEstadoDeuda = "" + getIdEstadoDeuda();
		
		return strIdDeuda + "-" + strIdEstadoDeuda;
	}
	
	
	public String getDesViaDeuda() {
		return desViaDeuda;
	}

	public void setDesViaDeuda(String desViaDeuda) {
		this.desViaDeuda = desViaDeuda;
	}

	public String getDesEstado() {
		return desEstado;
	}

	public void setDesEstado(String desEstado) {
		this.desEstado = desEstado;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public String getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	
	public boolean isVueltaAtrasAnularEnabled() {
		return vueltaAtrasAnularEnabled;
	}
	public void setVueltaAtrasAnularEnabled(boolean vueltaAtrasAnularEnabled) {
		this.vueltaAtrasAnularEnabled = vueltaAtrasAnularEnabled;
	}
	
	public Long getIdTipoPrivilegio() {
		return idTipoPrivilegio;
	}
	public void setIdTipoPrivilegio(Long idTipoPrivilegio) {
		this.idTipoPrivilegio = idTipoPrivilegio;
	}
	public String getDesTipoPrivilegio() {
		return desTipoPrivilegio;
	}
	public void setDesTipoPrivilegio(String desTipoPrivilegio) {
		this.desTipoPrivilegio = desTipoPrivilegio;
	}
	
	// View getters
	public String getTotalView(){
		return (total!=null?StringUtil.redondearDecimales(total, 1, 2):"");
	}
	
	public String getSaldoView(){
		return (saldo!=null?StringUtil.redondearDecimales(saldo, 1, 2):"");
	}

	public String getActualizacionView(){
		return (actualizacion!=null?StringUtil.redondearDecimales(actualizacion, 1, 2):"");
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
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
	/**
	 * Si es de 4 digitos devuelve los ultimos 2 digitos
	 * Si es de largo 2, devuelve la cadena. Si es vacio devuelve una cadena vacia 
	 * @return
	 */
	public String getAnioCorto(){

		if(this.anio.length()==2)
			return getAnio();
		
		return (this.anio.length()==4?this.anio.substring(2):"");
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
		this.fechaVto = DateUtil.formatDate(getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK);
	}
	
	public Long getIdCuenta() {
		return idCuenta;
	}
	public void setIdCuenta(Long idCuenta) {
		this.idCuenta = idCuenta;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	
	
	public String getLinkVerDetalle(){
		
		String link = "/siat/gde/AdministrarLiqDeuda.do?method=verDetalleDeuda";
		
	    link += "&selectedId=" + this.getIdDeuda(); 
		
	    link += "&idEstadoDeuda=" + this.getIdEstadoDeuda();
		
	    return link;
	    
	}
	
}

