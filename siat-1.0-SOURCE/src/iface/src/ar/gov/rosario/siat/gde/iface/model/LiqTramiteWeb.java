//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;


/**
 * Value Object utilizado para registrar los accesos de usuarios anonimos
 * 
 * @author tecso
 *
 */
public class LiqTramiteWeb {

	private static final long serialVersionUID = 1L;

	public static final int COD_CONSULTA_RECURSOCUENTA = 1; 	 // 01 - Consulta por Rec/Cta.
	public static final int COD_RECONFECCION_DEUDA = 2; 		 // 02 - Reimpresion de deuda.
	public static final int COD_RECLAMO_ASENTAMIENTO_DEUDA = 3;  // 03 - Reclamo de asentamiento de deuda.
	public static final int COD_CONSULTA_PLAN_PAGO = 19; 		 // 19 - Consulta de plan de pago de TGI
	public static final int COD_IMPRIMIR_LIQUIDACION_DEUDA = 20; // 20 - Liquidacion de TGI y MEJORAS
	public static final int COD_RECONFECCION_CUOTA_PLAN = 21; 	 // 21 - Reconfeccion de Cuotas de plan de pago de TGI
	public static final int COD_RECLAMO_ASENTAMIENTO_CUOTA = 48; // 48 - Reclamo de asentamiento de cuota
	public static final int COD_CAMBIO_DOMICILIO_WEB = 18; 		 // 18 - Cambio domicilio web 
	public static final int COD_REIMP_DEUDA = 53;                // 53 - Reimpresion de deuda.
	
	
	private int tipoTramite;
	private String idObjeto=""; // Codigo del recurso + "-" + Id Recurso
	private String nombre="";
	private String apellido=""; 
	private String direccion = ""; 
	private String telefono="";
	private String email=""; 
	private String tipoDoc=""; 
	private String nroDoc=""; 

	
	
	// Constructores
	public LiqTramiteWeb() {
	
	}

	// Getters y Setters
	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdObjeto() {
		return idObjeto;
	}

	public void setIdObjeto(String idObjeto) {
		this.idObjeto = idObjeto;
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

	public int getTipoTramite() {
		return tipoTramite;
	}
	public void setTipoTramite(int tipoTramite) {
		this.tipoTramite = tipoTramite;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	

}
