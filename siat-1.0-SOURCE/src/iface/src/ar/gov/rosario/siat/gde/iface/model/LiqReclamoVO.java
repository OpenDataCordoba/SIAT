//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Value Object del LiqReclamo
 * @author tecso
 *
 */
public class LiqReclamoVO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "liqReclamoVO";
	
	// Datos para el retorno, por el no uso de session.
	private Long  idCuenta;
	private Long  idDeuda;
	private Long  idViaDeuda;
	private Long  idEstadoDeuda;
	private Long  idConvenio;
	private Long  idCuota;
	
	// Datos de Deuda (View) (Reclamos de Deuda)
	private String desRecurso="";
	private String numeroCuenta=""; 
	private String periodoAnio="";
	
	// Datos de Cuota (View) (Reclamos de Acentamiento de Cuotas de Convenio)
	private String desPlan="";
	private String numeroCuota=""; 
	
	// Datos comunes a Deuda y Cuota
	private String fechaVto="";
	private Double importe;
	
	// Datos del Comprobante de Pago
	private Date   fechaPago;
	private String fechaPagoView="";
	private Double importePagado;
	private String importePagadoView;
	private String banco="";
	private String observacion="";
	
	// Datos de Contacto
	private String nombre="";
	private String apellido=""; 
	private String tipoDoc=""; 
	private String nroDoc=""; 
	private String telefono="";
	private String email=""; 
	
	// Datos para el facade
	private String  idObjeto=""; // Codigo del recurso + "-" + Id Recurso
	private String  codRecurso =""; // para ReclamoWeb es tributo
	private Integer anio; 
	private Integer periodo;
	private Integer sistema;
	private Integer resto;
	
	private int tipoTramite;
	
	// Constructores
	public LiqReclamoVO() {
	
	}

	// Getters y Setters
	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String cuenta) {
		this.numeroCuenta = cuenta;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFechaPagoView() {
		return fechaPagoView;
	}

	public void setFechaPagoView(String fechaPagoView) {
		this.fechaPagoView = fechaPagoView;
		this.fechaPago = DateUtil.getDate(fechaPagoView);
	}

	public String getFechaVto() {
		return fechaVto;
	}

	public void setFechaVto(String fechaVto) {
		this.fechaVto = fechaVto;
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

	public Long getIdEstadoDeuda() {
		return idEstadoDeuda;
	}

	public void setIdEstadoDeuda(Long idEstadoDeuda) {
		this.idEstadoDeuda = idEstadoDeuda;
	}

	public String getIdObjeto() {
		return idObjeto;
	}

	public void setIdObjeto(String idObjeto) {
		this.idObjeto = idObjeto;
	}

	public Long getIdViaDeuda() {
		return idViaDeuda;
	}

	public void setIdViaDeuda(Long idViaDeuda) {
		this.idViaDeuda = idViaDeuda;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Double getImportePagado() {
		return importePagado;
	}

	public void setImportePagado(Double importePagado) {
		this.importePagado = importePagado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNroDoc() {
		return nroDoc;
	}

	public void setNroDoc(String nroDoc) {
		this.nroDoc = nroDoc;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public String getPeriodoAnio() {
		return periodoAnio;
	}

	public void setPeriodoAnio(String periodoAnio) {
		this.periodoAnio = periodoAnio;
	}

	public Integer getSistema() {
		return sistema;
	}

	public void setSistema(Integer sistema) {
		this.sistema = sistema;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	public String getDesRecurso() {
		return desRecurso;
	}

	public void setDesRecurso(String desRecurso) {
		this.desRecurso = desRecurso;
	}

	public String getImportePagadoView() {
		return importePagadoView;
	}

	public void setImportePagadoView(String importePagadoView) {
		this.importePagadoView = importePagadoView;
	}

	public String getCodRecurso() {
		return codRecurso;
	}

	public void setCodRecurso(String codRecurso) {
		this.codRecurso = codRecurso;
	}

	public String getDesPlan() {
		return desPlan;
	}

	public void setDesPlan(String desPlan) {
		this.desPlan = desPlan;
	}

	public String getNumeroCuota() {
		return numeroCuota;
	}

	public void setNumeroCuota(String numeroCuota) {
		this.numeroCuota = numeroCuota;
	}
	

	public Long getIdConvenio() {
		return idConvenio;
	}

	public void setIdConvenio(Long idConvenio) {
		this.idConvenio = idConvenio;
	}
	
	public Long getIdCuota() {
		return idCuota;
	}
	public void setIdCuota(Long idCuota) {
		this.idCuota = idCuota;
	}

		
	public Integer getResto() {
		return resto;
	}
	public void setResto(Integer resto) {
		this.resto = resto;
	}

	public int getTipoTramite() {
		return tipoTramite;
	}
	public void setTipoTramite(int tipoTramite) {
		this.tipoTramite = tipoTramite;
	}
	
	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
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
	
	/**
	 * Devuelve un String XML con los datos para realizar el envio del email
	 * @return
	 */
	public String getXmlForEnvioMail(){
		String ret ="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
		ret += "<Reclamo>";
		ret += "<nombre>"+getNombre()+" "+getApellido()+"</nombre>";
		ret += "<nroCuenta>"+getNumeroCuenta()+"</nroCuenta>";
		ret += "<tributo>"+getDesRecurso()+"</tributo>";
		ret += "<desPlan>"+getDesPlan()+"</desPlan>";
		ret += "<nroCuota>"+getNumeroCuota()+"</nroCuota>";
		ret += "<periodoAnio>"+getPeriodoAnio()+"</periodoAnio>";
		ret += "<fechaVto>"+getFechaVto()+"</fechaVto>";
		ret += "<importe>"+StringUtil.redondearDecimales(getImporte(),1,2)+"</importe>";
		ret += "<fechaPagoView>"+getFechaPagoView()+"</fechaPagoView>";
		ret += "<importePagadoView>"+StringUtil.redondearDecimales(getImportePagado(), 1, 2)+"</importePagadoView>";
		ret += "<banco>"+getBanco()+"</banco>";
		ret += "<observacion>"+getObservacion()+"</observacion>";
		ret += "<telefono>"+getTelefono()+"</telefono>";
		ret += "<email>"+getEmail()+"</email>";
		ret +="</Reclamo>";
		return ret;
	}
}
