//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.Date;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.SiNo;

public class PersonaVO extends SiatBussImageModel {

	private static Logger log = Logger.getLogger(PersonaVO.class);
	
	// Propiedades
	private static final long serialVersionUID = 1L;

	public static final Character FISICA   = 'F';
	public static final Character JURIDICA = 'J';
	
    private Character  	tipoPersona = FISICA;
    private String      apellido = "";         
    private String      apellidoMaterno = "";  
    private String      nombres = "";          
    private DomicilioVO domicilio = new DomicilioVO();
    private Sexo        sexo = Sexo.OpcionSeleccionar;
    private Date        fechaNacimiento = null;  
    private String      telefono = "";  
	private String      cuit = ""; // por ahora es un String.
	private String 		razonSocial = "";
	private DocumentoVO documento = new DocumentoVO();
	private String 		represent = "";
	private LetraCuit 	letraCuit = LetraCuit.R; //compatibilidad siatmr
	private String      caracTelefono = ""; //compatibilidad siatmr
	
	private Boolean esContribuyente = false;
	
	private String fechaNacimientoView;
	private String tipoPersonaView ="";
	
	// flags para la busqueda simple
	private boolean personaBuscada = false;
	private boolean personaEncontrada = false;  
	
	// Constructores
	public PersonaVO(){
		super();
	}

	// Getters y setters	
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getApellidoMaterno() {
		return apellidoMaterno;
	}
	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}
	public String getCuit() {
		return cuit;
	}
	public void setCuit(String cuit) {
		this.cuit = cuit;
	}	
	public Character getTipoPersona() {
		return tipoPersona;
	}

	public DomicilioVO getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(DomicilioVO domicilio) {
		this.domicilio = domicilio;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
		this.fechaNacimientoView = DateUtil.formatDate(fechaNacimiento, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public Sexo getSexo() {
		return sexo;
	}

	public void setTipoPersona(Character tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}	
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public boolean getEsPersonaFisica(){
		return FISICA.equals(this.getTipoPersona());
	}
	public boolean getEsPersonaJuridica(){
		return JURIDICA.equals(this.getTipoPersona());
	}
	public Boolean getEsContribuyente() {
		return esContribuyente;
	}
	public String getFechaNacimientoView() {
		return fechaNacimientoView;
	}
	public void setFechaNacimientoView(String fechaNacimientoView) {
		this.fechaNacimientoView = fechaNacimientoView;
	}
	public boolean isPersonaBuscada() {
		return personaBuscada;
	}
	public void setPersonaBuscada(boolean personaBuscada) {
		this.personaBuscada = personaBuscada;
	}

	public boolean isPersonaEncontrada() {
		return personaEncontrada;
	}
	public void setPersonaEncontrada(boolean personaEncontrada) {
		this.personaEncontrada = personaEncontrada;
	}
	
	public String getEsContribuyenteView(){
	
		if (this.esContribuyente)
			return SiNo.SI.getValue();
		else
			return SiNo.NO.getValue();
	}
	
	public String getView(){
		if (this.getEsPersonaFisica()){
			return this.getApellido() + " " + this.getNombres();
		}else{
			return this.getRazonSocial();
		}
	}
	
	public String getTipoPersonaView(){
		if (this.getEsPersonaFisica()){
			return "Fisica";
		}else{
			return "Juridica";
		}
	}
	
	public String getViewWithCuit(){
		if (this.getEsPersonaFisica()){
			return getCuit()+" - "+this.getApellido() + ", " + this.getNombres();
		}else{
			return getCuit()+" - "+this.getRazonSocial();
		}
	}

	public void setDocumento(DocumentoVO documento) {
		this.documento = documento;
	}

	public DocumentoVO getDocumento() {
		return documento;
	}

	public void setRepresent(String represent) {
		this.represent = represent;
	}
	
	@Override
	public String getRepresent() {
		return represent;
	}

	public void setLetraCuit(LetraCuit letraCuit) {
		this.letraCuit = letraCuit;
	}

	public LetraCuit getLetraCuit() {
		return letraCuit;
	}

	public void setCaracTelefono(String caracTelefono) {
		this.caracTelefono = caracTelefono;
	}

	public String getCaracTelefono() {
		return caracTelefono;
	}
	
	//compatibilidad siatmr
	public String getCuitFull() {
		return cuit;
	}
}
