//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.ConDeuTitVO;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuVO;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.iface.model.DocumentoVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.TipoDocumentoVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a ConDeuTit.<br>
 * Corresponde a un titular de una Constancia de Deuda (ConstanciaDeu)
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_conDeuTit")
public class ConDeuTit extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idConstanciaDeu")
	private ConstanciaDeu constanciaDeu;

	@Column(name = "idPersona")
	private Long idPersona;
	
	@Transient
	private Persona persona;

	// <#Propiedades#>

	// Constructores
	public ConDeuTit() {
		super();
		// Seteo de valores default
	}

	public ConDeuTit(ConstanciaDeu constanciaDeu, Long idPersona) {
		super();
		this.constanciaDeu = constanciaDeu;
		this.idPersona = idPersona;
	}

	public ConDeuTit(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static ConDeuTit getById(Long id) throws Exception {
		ConDeuTit conDeutit = (ConDeuTit) GdeDAOFactory.getConDeuTitDAO().getById(id);
		conDeutit.cargarPersona();
		return conDeutit;
	}

	public static ConDeuTit getByIdNull(Long id) {
		return (ConDeuTit) GdeDAOFactory.getConDeuTitDAO().getByIdNull(id);
	}

	public static List<ConDeuTit> getList() {
		return (List<ConDeuTit>) GdeDAOFactory.getConDeuTitDAO().getList();
	}

	public static List<ConDeuTit> getListActivos() {
		return (List<ConDeuTit>) GdeDAOFactory.getConDeuTitDAO()
				.getListActiva();
	}

	// Getters y setters

	public ConstanciaDeu getConstanciaDeu() {
		return constanciaDeu;
	}

	public void setConstanciaDeu(ConstanciaDeu constanciaDeu) {
		this.constanciaDeu = constanciaDeu;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
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
		// limpiamos la lista de errores
		clearError();

		// <#ValidateDelete#>

		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones
		if (hasError()) {
			return false;
		}
		return true;
	}

	// Metodos de instancia
	/**
	 * Carga la Persona con el idPersona que tiene seteado
	 */
	public void cargarPersona() throws Exception{
		setPersona(Persona.getById(idPersona));
	}
	
	public ConDeuTitVO toVOForABM(boolean copiarList) throws Exception{
		ConDeuTitVO conDeuTitVO = (ConDeuTitVO) this.toVO(0, false);
		//conDeuTitVO.setConstanciaDeu((ConstanciaDeuVO) this.constanciaDeu.toVOLight());
		conDeuTitVO.setConstanciaDeu(new ConstanciaDeuVO(this.constanciaDeu.getId()));
		
		//conDeuTitVO.setPersona((PersonaVO) this.persona.toVO(2, false));
		PersonaVO personaVO = new PersonaVO();
		personaVO.setApellido(this.persona.getApellido());
		personaVO.setNombres(this.persona.getNombres());
		personaVO.setCuit(this.persona.getCuitFull());
		personaVO.setRepresent(this.persona.getRepresent());
		personaVO.setRazonSocial(this.persona.getRazonSocial());
		personaVO.setTipoPersona(this.persona.getTipoPersona());
		
		DocumentoVO documentoVO = new DocumentoVO();
		documentoVO.setTipoDocumento(new TipoDocumentoVO());
		personaVO.setDocumento(documentoVO);
		if (this.persona.getDocumento()!=null){
			personaVO.getDocumento().setTipoyNumeroView(this.persona.getDocumento().getTipoyNumeroView());
			personaVO.getDocumento().getTipoDocumento().setAbreviatura(this.persona.getDocumento().getTipoDocumento().getAbreviatura());
			personaVO.getDocumento().setNumero(this.persona.getDocumento().getNumero());
		}else{
			personaVO.getDocumento().setTipoyNumeroView("");
		}
		
		conDeuTitVO.setPersona(personaVO);
		return conDeuTitVO;
	}
	
	// Metodos de negocio

	/**
	 * Activa el ConDeuTit. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getConDeuTitDAO().update(this);
	}

	/**
	 * Desactiva el ConDeuTit. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getConDeuTitDAO().update(this);
	}

	/**
	 * Valida la activacion del ConDeuTit
	 * 
	 * @return boolean
	 */
	private boolean validateActivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	/**
	 * Valida la desactivacion del ConDeuTit
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	// <#MetodosBeanDetalle#>
}
