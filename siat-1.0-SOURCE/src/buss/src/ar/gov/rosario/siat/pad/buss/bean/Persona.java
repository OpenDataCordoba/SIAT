//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.LetraCuit;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Bean correspondiente a Persona Fisica y Juridica
 * 
 * @author leonardo.fagnano
 */
@Entity
@Table(name = "pad_persona")
public class Persona extends BaseBO {
		
	private static final long serialVersionUID = 1L;
	
	@Transient
	private static Logger log = Logger.getLogger(Persona.class);
	
	// Propiedades
	public static final String CARAC_TEL_NO_ING = "0";
	
	@Column(name = "tipoPersona")
    private Character  	tipoPersona;       
	      
	@Column(name = "nombres")
    private String    nombres;          // nombre_per
	
	@Column(name = "apellido")
    private String    apellido;         // apellido
	
	@Column(name = "apellidoMaterno")
    private String    apellidoMaterno;  // ap_materno
	
	@Column(name = "cuit")
    private String  cuit;               // por ahora es un String.
	
	@Column(name = "razonSocial")
    private String razonSocial;
	
	@Column(name = "sexo")
    private Integer   sexo;             // per.sexo 1 Masculino, 0 Femenino -> Enum Sexo
	
	@Column(name = "fechaNacimiento")
    private Date      fechaNacimiento;  // per.fe_naci
	
	@Column(name = "telefono")
    private String  telefono;           // nro_telef
		
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idDomicilio") 
	private Domicilio domicilio;
	    
	/*
	@Column(name = "nroDocumento")
    private String nroDocumento;    // contiene el tipo de documento

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idTipoDocumento") 
	private TipoDocumento   tipoDocumento;
    */
	
	@Embedded
    @AttributeOverrides( {
        @AttributeOverride(name="estado", column = @Column(name="estado", insertable=false, updatable=false) ),
        @AttributeOverride(name="fechaUltMdf", column = @Column(name="fechaUltMdf", insertable=false, updatable=false) ),
        @AttributeOverride(name="usuarioUltMdf", column = @Column(name="usuario", insertable=false, updatable=false) ) 
        
    } )
	private Documento documento; //compatiblidad siatmr

	@Transient
	private Integer letraCuit = LetraCuit.R.getId(); //compatibilidad siatmr

	@Transient
	private String caracTelefono = ""; //compatibilidad siatmr
	
	// Constructores
	public Persona(){
		super();
	}

	// Getters y Setters
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
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public Integer getSexo() {
		return sexo;
	}
	public void setSexo(Integer sexo) {
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
	
	public Character getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(Character tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	// Metodos de clase
	public static Persona getById(Long id) throws Exception {	
		System.out.println("///ID en persona: " + id);
		return (Persona)PadDAOFactory.getPersonaDAO().getById(id);			
	}
	
	public static Persona getByIdNull(Long id) throws Exception {		
		return (Persona)PadDAOFactory.getPersonaDAO().getByIdNull(id);		
	}
	
	//TODO:ver/analizar como implementar Persona Ligth
	public static Persona getByIdLight(Long id) throws Exception {
		System.out.println("///ID ligth en persona: " + id);
		//	return GeneralFacade.getInstance().getPersonaByIdLight(id);
		return getById(id);
	}	
	
	// Metodos de Instancia
	// Validaciones

	// Validaciones
	public boolean validateCreate() throws Exception {
		//limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		//limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	/**
	 * Validaciones comunes a la creacion y actualizacion
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception {
		
		//Validaciones requeridos
		
		// Tipo persona
		if (getTipoPersona() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.PERSONA_TIPOPERSONA);
		}
		
		if (this.getEsPersonaJuridica()) {
			// Razon social
			if (StringUtil.isNullOrEmpty(getRazonSocial())) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.PERSONA_RAZONSOCIAL);
			}
			// Cuit
			if (StringUtil.isNullOrEmpty(getCuit())) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.PERSONA_CUIT);
			}
		}

		if (this.getEsPersonaFisica()) {
			// Nombres
			if (StringUtil.isNullOrEmpty(getNombres())) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.PERSONA_NOMBRES);
			}
			
			// Apellido
			if (StringUtil.isNullOrEmpty(getApellido())) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.PERSONA_APELLIDO);
			}
			
			// Sexo
			if (this.getSexo() == null) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.PERSONA_SEXO);
			}
			
			// Documento -----------------------
//			if (getDocumento().getNumero() == null) {
//				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.DOCUMENTO_NUMERO);
//			}
			// Tipo Documento
//			if (getDocumento().getTipoDocumento() == null) {
//				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.TIPODOCUMENTO_LABEL);
//			}							
			// Fin Documento -----------------------		

			if(this.getFechaNacimiento() != null && 
			   DateUtil.isDateAfter(this.getFechaNacimiento(), new Date())){
				addRecoverableError( BaseError.MSG_VALORMAYORQUE, 
									 PadError.PERSONA_FECHANACIMIENTO, BaseError.MSG_FECHA_ACTUAL );
			}
			if (StringUtil.isNullOrEmpty(this.getTelefono())) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.PERSONA_TELEFONO);
			}
		}	

		if (hasError()) {
			return false;
		}
		
		// Valido el domicilio ingresado en caso de ser de rosario
		if (this.getDomicilio().getLocalidad().isRosario()) {
			
			//TODO: reemplazar servicio
//			Domicilio domicilioVal = UbicacionFacade.getInstance().validarDomicilio(this.getDomicilio());
//			if (domicilioVal.hasError()) {
//				// agregacion de errores del domicilio validado a la persona 
//				
//				domicilioVal.addErrorMessages(this);
//				return false;
//			}
		}
		return true;
	}

	// Metodos de negocio
	
	/**
	 * Determina si la persona es contribuyente de SAT
	 * @return Boolean
	 */
	public Boolean getEsContribuyente(){
		return PadDAOFactory.getContribuyenteDAO().esContribuyenteById(this.getId());
	}
	
	/**
	 * Determina si la persona es de tipo Fisica
	 * @return Boolean
	 */
	public Boolean getEsPersonaFisica(){
		return PersonaVO.FISICA.equals(this.getTipoPersona());
	}
	
	/**
	 * Determina si la persona es de tipo Juridica
	 * @return Boolean
	 */
	public Boolean getEsPersonaJuridica(){
		return PersonaVO.JURIDICA.equals(this.getTipoPersona());
	}
	
	/**
	 * Metodo utilizado directamente por el toVO para establecer si se puede modificar la persona.
	 * Se puede modificar si es persona Fisica o (si es Juridica y es Contribuyente).
	 * @return Boolean
	 */
	public Boolean getModificarBussEnabled(){
		return (this.getEsPersonaFisica() || (this.getEsPersonaJuridica() && this.getEsContribuyente()));
	}
	
	/**
	 * 
	 * Devuelve la denominacion de la persona segun sea fisica o juridica
	 * 
	 * @author Cristian
	 * @return String
	 */
	public String getRepresent() {
		if (this.getEsPersonaFisica()) {
			String apellido = getApellido() == null ? "" : getApellido();
			String nombres = getNombres() == null ? "" : getNombres();
			String denom = apellido.trim() + " " + nombres.trim();
			return denom.trim().toUpperCase();
		} else {
			String denom = getRazonSocial() == null ? "" : getRazonSocial();
			return denom.trim().toUpperCase();
		}
	}
	
	public String getEsContribuyenteView(){
		if(this.getEsContribuyente()){
			return SiNo.SI.getValue();
		}else{
			return SiNo.NO.getValue();
		}
	}
	
	/**
	 * Retorna descripcion del cuit junto con la letra: R|C|F nroCuit 
	 * Este metodo es similar al de cuenta.getCuitTitPri()
	 */
	public String getCuitFull() {
		String cuit = this.getCuit() == null ? "" : this.getCuit();
		try {
			int len = cuit.length();
			String cuit01 = cuit.substring(0, 2);
			String cuit02 = cuit.substring(2, len - 1);
			String cuit03 = cuit.substring(len - 1);
			cuit = cuit01 + "-" + cuit02 + "-" + cuit03; 
		} catch (Exception e) {
			log.error("getCuitFull(): formato de cuit incorrecto:" + cuit);
		}	
		return cuit.trim();
	}

	/**
	 * Retorna descripcion del cuit para un contribuyente (no muestra letras).
	 * Si letra es F retona "".<br/>
	 * Este metodo es similar al de cuenta.getCuitTitPriContr()
	 */
	public String getCuitContr() {
//		LetraCuit letraCuit = LetraCuit.getById(this.getLetraCuit());
		
		String cuit = "";
		
//		try {
//			String letra = letraCuit.getCodigo().toString();
//			
//			if (letra.equals("R") || letra.equals("C")) {
//				cuit = this.getCuit();
//					int len = cuit.length();
//					String cuit01 = cuit.substring(0, 2);
//					String cuit02 = cuit.substring(2, len - 1);
//					String cuit03 = cuit.substring(len - 1);
//					cuit = cuit01 + "-" + cuit02 + "-" + cuit03; 
//			}
//
//		} catch (Exception e) {
//			log.error("formato de cuit incorrecto:" + this.getCuit());
//		}
			
		return this.getCuit().trim();
	}

	
	public String getCuit00(){
//		String letra = "";
//		try {
//			LetraCuit letraCuit = LetraCuit.getById(this.getLetraCuit());
//			letra = letraCuit.getCodigo() == null ? "" : letraCuit.getCodigo().toString();
//		} catch (Exception e) {
//			log.error("formato de cuit incorrecto:" + cuit);
//		}
//		return letra;
		
		return this.getCuit().trim();
	}

	public String getCuit01(){
		
		cuit = this.getCuit();
		String cuit01 = "";
		try {
			cuit01 = cuit.substring(0, 2);
		} catch (Exception e) {
			log.error("formato de cuit incorrecto:" + cuit);
		}
		
		return cuit01;
	}
	
	
	public String getCuit02(){
		
		cuit = this.getCuit();
		String cuit02 = "";
		try {
			int len = cuit.length();
			cuit02 = cuit.substring(2, len - 1);
		} catch (Exception e) {
			log.error("formato de cuit incorrecto:" + cuit);
		}
		
		return cuit02;
	}
	
	public String getCuit03(){
		cuit = this.getCuit();
		String cuit03 = "";
		try {
			int len = cuit.length();
			cuit03 = cuit.substring(len - 1);
		} catch (Exception e) {
			log.error("formato de cuit incorrecto:" + cuit);
		}
		return cuit03;
	}
/*
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public Documento getDocumento() {
		return documento;
	}
*/
	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}

	public Domicilio getDomicilio() {
		return domicilio;
	}

	public void setLetraCuit(Integer letraCuit) {
		this.letraCuit = letraCuit;
	}

	public Integer getLetraCuit() {
		return letraCuit;
	}

	public void setCaracTelefono(String caracTelefono) {
		this.caracTelefono = caracTelefono;
	}

	public String getCaracTelefono() {
		return caracTelefono;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public Documento getDocumento() {
		return documento;
	}

	
}
