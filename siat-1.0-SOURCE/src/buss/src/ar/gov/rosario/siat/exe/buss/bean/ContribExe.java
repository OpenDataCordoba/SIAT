//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.exe.buss.dao.ExeDAOFactory;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.pad.buss.bean.Broche;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Exencion
 * 
 * @author tecso
 */
@Entity
@Table(name = "exe_contribexe")
public class ContribExe extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "descontribexe")
	private String desContribExe;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="idcontribuyente") 
	private Contribuyente contribuyente;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idexencion") 
	private Exencion exencion;

	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="idBroche") 
	private Broche broche;
		
	@Column(name="fechaDesde")
	private Date fechaDesde;
	
	@Column(name="fechaHasta")	
	private Date fechaHasta;
	
	
	 
	 
	// Constructores
	public ContribExe() {

	}
	
	// para el calculo de vigencia implemento los sig. metodos
	public Date getFechaInicioVig() {
		return this.getFechaDesde();
	}

	public Date getFechaFinVig() {
		return this.getFechaHasta();
	}

	// Getters y Setters
	public String getDesContribExe() {
		return desContribExe;
	}

	public void setDesContribExe(String desContribExe) {
		this.desContribExe = desContribExe;
	}
	
	public Contribuyente getContribuyente() {
		return contribuyente;
	}

	public void setContribuyente(Contribuyente contribuyente) {
		this.contribuyente = contribuyente;
	}

	public Exencion getExencion() {
		return exencion;
	}

	public void setExencion(Exencion exencion) {
		this.exencion = exencion;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	
	public Broche getBroche() {
		return broche;
	}

	public void setBroche(Broche broche) {
		this.broche = broche;
	}

	
	// Metodos de clase
	
	/**
	 * Recupera el contribExe con el contribuyente y la persona
	 */
	public static ContribExe getById(Long id) throws Exception {
		ContribExe contribExe = (ContribExe) ExeDAOFactory.getContribExeDAO().getById(id);
		
		// seteo el contribuyente a la persona
		Contribuyente contribuyente = contribExe.getContribuyente();
		contribuyente.setPersona(Persona.getById(contribuyente.getId()));

		return contribExe;
	}

	public static ContribExe getByIdNull(Long id) {
		return (ContribExe) ExeDAOFactory.getContribExeDAO().getByIdNull(id);
	}

	/**
	 * Recupera el contribExe Vigente pasando el Id de Contribuyente
	 */
	public static ContribExe getVigenteByIdContribuyente(Long id, Date fechaToCompare) throws Exception {
		ContribExe contribExe = (ContribExe) ExeDAOFactory.getContribExeDAO().getVigenteByIdContribuyente(id, fechaToCompare);		
		return contribExe;
	}

	public static List<ContribExe> getListActivos() {
		return (ArrayList<ContribExe>) ExeDAOFactory.getContribExeDAO().getListActiva();
	}
	
	/**
	 * Valida la creacion
	 * @author Ivan
	 * @throws Exception 
	 */
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}
	/**
	 * Valida la actualizacion
	 * @author Ivan
	 * @throws Exception 
	 */
	public boolean validateUpdate() throws Exception {
		//	limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	/**
	 * Valida la eliminacion
	 * @author Ivan
	 */
	public boolean validateDelete() throws Exception{
		// limpiamos la lista de errores
		clearError();
		
		/*
		 * TODO: esto es importante 
		if (GenericDAO.hasReference(this, DomAtrVal.class, "domAtr")) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.DOMATRVAL_LABEL);
		}
		
		if (hasError()) {
			return false;
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio
	
	/**
	 * Activa el Atributo. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		ExeDAOFactory.getContribExeDAO().update(this);
	}

	/**
	 * Desactiva el Atributo. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		ExeDAOFactory.getContribExeDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Atributo
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Atributo
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	private boolean validate() throws Exception {
		//	Validaciones de Requeridos
		if (StringUtil.isNullOrEmpty(this.getDesContribExe())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.CONTRIBEXE_DESCONTRIBEXE);
		}

		if (this.getContribuyente() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CONTRIBUYENTE_LABEL);
		}
		
		if (this.getExencion() == null && this.getBroche() == null) {
			addRecoverableValueError("Debe seleccionar un Broche o una Exencion");
		}
		
		if (this.getExencion() != null && this.getFechaDesde() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.CONTRIBEXE_FECHADESDE);
		} 
		
		if (hasError()) {
			return false;
		}

		if (this.getFechaHasta() != null){
			if ( DateUtil.isDateBefore(this.getFechaHasta(), this.getFechaDesde()) ) {
				addRecoverableError(BaseError.MSG_VALORMENORQUE, 
					ExeError.CONTRIBEXE_FECHAHASTA, ExeError.CONTRIBEXE_FECHADESDE);			
			}
		}
		
		Long total = ExeDAOFactory.getContribExeDAO().getTotalByContribExe(this);
		if (total > 0) {
			addRecoverableError(ExeError.CONTRIBEXE_REPETIDO);
		}
		
		if (hasError()) {
			return false;
		}

		/*
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codAtributo");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, DefError.ATRIBUTO_CODATRIBUTO);			
		}

		if (hasError()) {
			return false;
		}
		*/

		return true;
	} 
	
	/**
	 * Retorna un Persona recuperado desde la base de datos General, usando el jar de personas.
	 * Usa el idPersona del contribuyente cargado en este contribExe 
	 * @return
	 * @throws Exception
	 */
	 public Persona getPersonaFromGeneral() throws Exception {
		 return Persona.getById(getContribuyente().getPersona().getId());
	 }
}