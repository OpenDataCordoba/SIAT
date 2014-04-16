//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.model.EnCaracterDeAfip;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TipoDocumentoAfip;

/**
 * Value Object del Socio
 * @author tecso
 *
 */
public class SocioVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "socioVO";	

	private ForDecJurVO fordecjur = new ForDecJurVO();
	
	private Integer soloFirmante;	

	private Long idPersona;	
	
	private Integer enCaracterDe;	

	private Integer tipoDocumento;	   

	private String apellido="";

	private String apellidoMaterno="";

	private String nombre="";	  

	private String nroDocumento="";

	private String cuit="";
	
	private DatosDomicilioVO  datosDomicilio	= new DatosDomicilioVO();
	
	
	// Constructores
	public SocioVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public SocioVO(int id) {
		super();
		setId(new Long(id));		
	}

	
	public void setFordecjur(ForDecJurVO fordecjur) {
		this.fordecjur = fordecjur;
	}

	public ForDecJurVO getFordecjur() {
		return fordecjur;
	}

	// Getters y Setters
	public Integer getSoloFirmante() {
		return soloFirmante;
	}

	public void setSoloFirmante(Integer soloFirmante) {
		this.soloFirmante = soloFirmante;	
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public Integer getEnCaracterDe() {
		return enCaracterDe;
	}

	public void setEnCaracterDe(Integer enCaracterDe) {
		this.enCaracterDe = enCaracterDe;
	}

	public Integer getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(Integer tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
	
	public void setDatosDomicilio(DatosDomicilioVO datosDomicilio) {
		this.datosDomicilio = datosDomicilio;
	}

	public DatosDomicilioVO getDatosDomicilio() {
		return datosDomicilio;
	}

	// View getters
	public String getSoloFirmanteView() {
		return SiNo.getById(soloFirmante).getValue();
	}

	public String getIdPersonaView() {
		return (this.idPersona!=null)?idPersona.toString():"";
	}

	public String getTipoDocumentoView() {		
		return TipoDocumentoAfip.getById(tipoDocumento).getValue();		
	}
	
	public String getEnCaracterDeView() {
		return EnCaracterDeAfip.getById(enCaracterDe).getValue();				
	}
	
	public String getApellidoYNombreView(){
		return this.apellido+", "+this.nombre;
	}
	
	public String getDocumentoView(){
		return this.getTipoDocumentoView()+" - "+this.nroDocumento;
	}
}
