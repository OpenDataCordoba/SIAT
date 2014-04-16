//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a CamDomWeb
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_camdom")
public class CamDomWeb extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false)
    @JoinColumn(name="idcuenta")
	private Cuenta cuenta;

	@ManyToOne(optional=true)
    @JoinColumn(name="iddomvie") 
	private Domicilio domVie;
	
	@ManyToOne(optional=false)
    @JoinColumn(name="iddomnue") 
	private Domicilio domNue;
	
	@Column(name = "cod_doc")
	private Long cod_doc;
	
	@Column(name = "abrev_doc")
	private String abrev_doc;

	@Column(name = "numdoc")
	private Long numDoc;

	@Column(name = "nomSolicitante")
	private String nomSolicitante;

	@Column(name = "apeSolicitante")
	private String apeSolicitante;

	@Column(name = "nrotramite")
	private Integer nroTramite;
	
	@Column(name = "mail")
	private String mail;
	
	@Column(name = "esorigenweb")
	private Integer esOrigenWeb;
	
	// Constructores
	public CamDomWeb(){
		super();
	}

	public CamDomWeb(Long id){
		super();
		setId(id);
	}

	// Metodos de Clase
	public static CamDomWeb getById(Long id) {
		return (CamDomWeb) PadDAOFactory.getCamDomWebDAO().getById(id);
	}
	
	public static CamDomWeb getByIdNull(Long id) {
		return (CamDomWeb) PadDAOFactory.getCamDomWebDAO().getByIdNull(id);
	}
	
	public static List<CamDomWeb> getList() {
		return (List<CamDomWeb>) PadDAOFactory.getCamDomWebDAO().getList();
	}
	
	public static List<CamDomWeb> getListActivos() {			
		return (List<CamDomWeb>) PadDAOFactory.getCamDomWebDAO().getListActiva();
	}
	
	// Getters y setters
	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Domicilio getDomVie() {
		return domVie;
	}

	public void setDomVie(Domicilio domVie) {
		this.domVie = domVie;
	}

	public Domicilio getDomNue() {
		return domNue;
	}

	public void setDomNue(Domicilio domNue) {
		this.domNue = domNue;
	}

	public String getAbrev_doc() {
		return abrev_doc;
	}

	public void setAbrev_doc(String abrev_doc) {
		this.abrev_doc = abrev_doc;
	}

	public String getNomSolicitante() {
		return nomSolicitante;
	}

	public void setNomSolicitante(String nomSolicitante) {
		this.nomSolicitante = nomSolicitante;
	}

	public String getApeSolicitante() {
		return apeSolicitante;
	}

	public void setApeSolicitante(String apeSolicitante) {
		this.apeSolicitante = apeSolicitante;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public Long getCod_doc() {
		return cod_doc;
	}

	public void setCod_doc(Long cod_doc) {
		this.cod_doc = cod_doc;
	}

	public Long getNumDoc() {
		return numDoc;
	}

	public void setNumDoc(Long numDoc) {
		this.numDoc = numDoc;
	}

	public Integer getNroTramite() {
		return nroTramite;
	}

	public void setNroTramite(Integer nroTramite) {
		this.nroTramite = nroTramite;
	}

	public Integer getEsOrigenWeb() {
		return esOrigenWeb;
	}

	public void setEsOrigenWeb(Integer esOrigenWeb) {
		this.esOrigenWeb = esOrigenWeb;
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		/*Ejemplo:
		if (GenericDAO.hasReference(this, ${BeanRelacionado}.class, "CamDomWeb")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							padError.CamDomWeb_LABEL, padError.${BEAN_RELACIONADO}_LABEL );
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {

		//	Validaciones
		if (getCuenta() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
		}
		
		if (getDomNue() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CAMBIAR_DOM_ENVIO_DOMICILIO_NUEVO);
		}

		if (getEsOrigenWeb() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CAMBIAR_DOM_ENVIO_ES_ORIGEN_WEB);
		}

		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el CamDomWeb. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		PadDAOFactory.getCamDomWebDAO().update(this);
	}

	/**
	 * Desactiva el CamDomWeb. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		PadDAOFactory.getCamDomWebDAO().update(this);
	}
	
	/**
	 * Valida la activacion del CamDomWeb
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del CamDomWeb
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

}
