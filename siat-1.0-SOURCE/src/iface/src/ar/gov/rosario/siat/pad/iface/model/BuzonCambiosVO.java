//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.TipoModificacion;
import coop.tecso.demoda.iface.model.TipoPersona;

/**
 * Value Object del BuzonCambios
 * @author tecso
 *
 */
public class BuzonCambiosVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "buzonCambiosVO";
	
	private TipoModificacion tipoModificacion = TipoModificacion.SELECCIONAR; 
	private TipoPersona tipoPersona = TipoPersona.SELECCIONAR;
	private String cuit00 = "";
	private String cuit01 = "";
	private String cuit02 = "";
	private String cuit03 = "";
	private DocumentoVO documento = new DocumentoVO();
	
	private String  apellido = "";         
	private String  nombres = "";          
	private String	razonSocial = "";
	
	private String origen = "";
	private String contacto = "";
	private String observaciones = "";
	
	private Long idPersona;
	
	// Constructores
	public BuzonCambiosVO() {
		super();
	}

	// Getters y Setters
	public TipoModificacion getTipoModificacion() {
		return tipoModificacion;
	}
	public void setTipoModificacion(TipoModificacion tipoModificacion) {
		this.tipoModificacion = tipoModificacion;
	}

	public TipoPersona getTipoPersona() {
		return tipoPersona;
	}
	public void setTipoPersona(TipoPersona tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getCuit00() {
		return cuit00;
	}
	public void setCuit00(String cuit00) {
		this.cuit00 = cuit00;
	}

	public String getCuit01() {
		return cuit01;
	}
	public void setCuit01(String cuit01) {
		this.cuit01 = cuit01;
	}

	public String getCuit02() {
		return cuit02;
	}
	public void setCuit02(String cuit02) {
		this.cuit02 = cuit02;
	}

	public String getCuit03() {
		return cuit03;
	}
	public void setCuit03(String cuit03) {
		this.cuit03 = cuit03;
	}

	public DocumentoVO getDocumento() {
		return documento;
	}
	public void setDocumento(DocumentoVO documento) {
		this.documento = documento;
	}

	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getContacto() {
		return contacto;
	}
	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	// Validacion particularmente ubicada aqui
	public boolean validate(){
		
		// Cuit reportado
		if (StringUtil.isNullOrEmpty(getCuit00())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BUZONCAMBIOS_CUIT00_LABEL);
		}
		if (StringUtil.isNullOrEmpty(getCuit01())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BUZONCAMBIOS_CUIT01_LABEL);
		}
		if (StringUtil.isNullOrEmpty(getCuit02())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BUZONCAMBIOS_CUIT02_LABEL);
		}
		if (StringUtil.isNullOrEmpty(getCuit03())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BUZONCAMBIOS_CUIT03_LABEL);
		}
		if (StringUtil.isNullOrEmpty(getContacto())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BUZONCAMBIOS_CONTACTO_LABEL);
		}
		
		// Moficicacion de Cuit
		if (getTipoModificacion().getEsCuit()){
			if (getDocumento().getNumero() == null){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BUZONCAMBIOS_TIPOYNRODOCUMENTO_LABEL);
			}

		} else {
		// 	Modificacion de Denominacion
			
			// Persona fisica
			if (getTipoPersona().getEsFisica()){
				
				if (StringUtil.isNullOrEmpty(getNombres())){
					addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BUZONCAMBIOS_NOMBRES_LABEL);
				}
				
				if (StringUtil.isNullOrEmpty(getApellido())){
					addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BUZONCAMBIOS_APELLIDO_LABEL);
				}
				
			} else {
			// Persona Juridica	
				
				if (StringUtil.isNullOrEmpty(getRazonSocial())){
					addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.BUZONCAMBIOS_RAZONSOCIAL_LABEL);
				}
			}			
		}
		
		
				
		if (hasError()) {
			return false;
		}

		return true;
	
	}
	
	
}
