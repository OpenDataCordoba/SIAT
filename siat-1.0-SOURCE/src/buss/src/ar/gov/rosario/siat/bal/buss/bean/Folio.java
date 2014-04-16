//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean correspondiente a Folios de Tesoreria
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_folio")
public class Folio extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "fechaFolio")
	private Date fechaFolio;
	
	@Column(name = "numero")
	private Long numero;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "desDiaCob")
	private String desDiaCob;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idEstadoFol") 
	private EstadoFol estadoFol;

	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idBalance") 
	private Balance balance;

	// Listas de Entidades Relacionadas con Folio
	@OneToMany(mappedBy="folio")
	@JoinColumn(name="idFolio")
	private List<FolCom> listFolCom;

	@OneToMany(mappedBy="folio")
	@JoinColumn(name="idFolio")
	private List<OtrIngTes> listOtrIngTes;

	@OneToMany(mappedBy="folio")
	@JoinColumn(name="idFolio")
	private List<FolDiaCob> listFolDiaCob;

	//Constructores 
	public Folio(){
		super();
	}
	
	// Getters Y Setters
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public EstadoFol getEstadoFol() {
		return estadoFol;
	}
	public void setEstadoFol(EstadoFol estadoFol) {
		this.estadoFol = estadoFol;
	}
	public Date getFechaFolio() {
		return fechaFolio;
	}
	public void setFechaFolio(Date fechaFolio) {
		this.fechaFolio = fechaFolio;
	}
	public List<FolCom> getListFolCom() {
		return listFolCom;
	}
	public void setListFolCom(List<FolCom> listFolCom) {
		this.listFolCom = listFolCom;
	}
	public List<FolDiaCob> getListFolDiaCob() {
		return listFolDiaCob;
	}
	public void setListFolDiaCob(List<FolDiaCob> listFolDiaCob) {
		this.listFolDiaCob = listFolDiaCob;
	}
	public List<OtrIngTes> getListOtrIngTes() {
		return listOtrIngTes;
	}
	public void setListOtrIngTes(List<OtrIngTes> listOtrIngTes) {
		this.listOtrIngTes = listOtrIngTes;
	}
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	public Balance getBalance() {
		return balance;
	}
	public void setBalance(Balance balance) {
		this.balance = balance;
	}
	public String getDesDiaCob() {
		return desDiaCob;
	}
	public void setDesDiaCob(String desDiaCob) {
		this.desDiaCob = desDiaCob;
	}

	// Metodos de clase	
	public static Folio getById(Long id) {
		return (Folio) BalDAOFactory.getFolioDAO().getById(id);
	}
	
	public static Folio getByIdNull(Long id) {
		return (Folio) BalDAOFactory.getFolioDAO().getByIdNull(id);
	}
		
	public static List<Folio> getList() {
		return (ArrayList<Folio>) BalDAOFactory.getFolioDAO().getList();
	}
	
	public static List<Folio> getListActivos() {			
		return (ArrayList<Folio>) BalDAOFactory.getFolioDAO().getListActiva();
	}
	
	
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		
		if (hasError()) {
			return false;
		}
		return !hasError();
	}
	
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
				
		if (hasError()) {
			return false;
		}
		return !hasError();
	}

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
		if(StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.FOLIO_DESCRIPCION_LABEL);
		}
		if(getFechaFolio()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.FOLIO_FECHAFOLIO_LABEL);
		}
		if(getNumero()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.FOLIO_NUMERO_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		if (GenericDAO.hasReference(this, FolCom.class, "folio")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.FOLIO_LABEL , BalError.FOLCOM_LABEL);
		}

		if (GenericDAO.hasReference(this, FolDiaCob.class, "folio")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.FOLIO_LABEL , BalError.FOLDIACOB_LABEL);
		}
		
		if (GenericDAO.hasReference(this, OtrIngTes.class, "folio")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.FOLIO_LABEL , BalError.OTRINGTES_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	// Administracion de FolCom
	public FolCom createFolCom(FolCom folCom) throws Exception {
		
		// Validaciones de negocio
		if (!folCom.validateCreate()) {
			return folCom;
		}

		BalDAOFactory.getFolComDAO().update(folCom);
		
		return folCom;
	}	

	public FolCom updateFolCom(FolCom folCom) throws Exception {
		
		// Validaciones de negocio
		if (!folCom.validateUpdate()) {
			return folCom;
		}

		BalDAOFactory.getFolComDAO().update(folCom);
		
		return folCom;
	}	

	public FolCom deleteFolCom(FolCom folCom) throws Exception {
		
		// Validaciones de negocio
		if (!folCom.validateDelete()) {
			return folCom;
		}
				
		BalDAOFactory.getFolComDAO().delete(folCom);
		
		return folCom;
	}	
	
	// Administracion de FolDiaCob
	public FolDiaCob createFolDiaCob(FolDiaCob folDiaCob) throws Exception {
		
		// Validaciones de negocio
		if (!folDiaCob.validateCreate()) {
			return folDiaCob;
		}

		BalDAOFactory.getFolDiaCobDAO().update(folDiaCob);
		
		return folDiaCob;
	}	

	public FolDiaCob updateFolDiaCob(FolDiaCob folDiaCob) throws Exception {
		
		// Validaciones de negocio
		if (!folDiaCob.validateUpdate()) {
			return folDiaCob;
		}

		BalDAOFactory.getFolDiaCobDAO().update(folDiaCob);
		
		return folDiaCob;
	}	

	public FolDiaCob deleteFolDiaCob(FolDiaCob folDiaCob) throws Exception {
		
		// Validaciones de negocio
		if (!folDiaCob.validateDelete()) {
			return folDiaCob;
		}
				
		BalDAOFactory.getFolDiaCobDAO().delete(folDiaCob);
		
		return folDiaCob;
	}	
	
}
