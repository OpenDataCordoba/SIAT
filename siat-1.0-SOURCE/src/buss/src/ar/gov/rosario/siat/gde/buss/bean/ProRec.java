//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

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

import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Recursos que administra un Procurador
 * 
 */
@Entity
@Table(name = "gde_proRec")
public class ProRec extends BaseBO {

	private static final long serialVersionUID = 1L;

	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idProcurador") 
	private Procurador procurador;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idRecurso") 
	private Recurso recurso;

	@Column(name = "fechaDesde") 
	private Date fechaDesde;     // DATETIME YEAR TO DAY NOT NULL
	
	@Column(name = "fechaHasta") 
	private Date fechaHasta;     // DATETIME YEAR TO DAY nuleable

	@OneToMany( mappedBy="proRec")
	@JoinColumn(name="idProRec")
	@OrderBy(clause="fechaDesde, desde")
	private List<ProRecDesHas> listProRecDesHas;
	
	@OneToMany( mappedBy="proRec")
	@JoinColumn(name="idProRec")
	@OrderBy(clause="fechaDesde")
	private List<ProRecCom> listProRecCom;
	
	// Contructores 
	public ProRec() {
		super();
	}

	// Getters y Setters
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
	public Procurador getProcurador() {
		return procurador;
	}
	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}
	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}
	
	public List<ProRecDesHas> getListProRecDesHas() {
		return listProRecDesHas;
	}

	public void setListProRecDesHas(List<ProRecDesHas> listProRecDesHas) {
		this.listProRecDesHas = listProRecDesHas;
	}
	
	public List<ProRecCom> getListProRecCom() {
		return listProRecCom;
	}

	public void setListProRecCom(List<ProRecCom> listProRecCom) {
		this.listProRecCom = listProRecCom;
	}

	// Metodos de clase
	public static ProRec getById(Long id) {
		return (ProRec) GdeDAOFactory.getProRecDAO().getById(id);
	}
	
	public static ProRec getByIdNull(Long id) {
		return (ProRec) GdeDAOFactory.getProRecDAO().getByIdNull(id);
	}
	
	public static List<ProRec> getList() {
		return (ArrayList<ProRec>) GdeDAOFactory.getProRecDAO().getList();
	}
	
	public static List<ProRec> getListActivos() {			
		return (ArrayList<ProRec>) GdeDAOFactory.getProRecDAO().getListActiva();
	}
	
	/**
	 * Obtiene un ProRec a partir del id de un Procurador y del id del Recurso
	 * @param  idProcurador
	 * @param  idRecurso
	 * @return ProRec
	 */
	public static ProRec getByIdProcuradorRecurso(Long idProcurador, Long idRecurso) {
		return GdeDAOFactory.getProRecDAO().getByIdProcuradorRecurso(idProcurador, idRecurso);
	} 
	
	/**
	 * Chekea si existe un Recurso determinado para un Procurador en el periodo delimitado por fechaDesde-fechaHasta.
	 * @param  idProRecActual
	 * @param  idProcurador
	 * @param  idRecurso
	 * @param  fechaDesde
	 * @param  fechaHasta
	 * @return Boolean
	 */
	public static Boolean existeRecurso(Long idProRecActual, Long idProcurador, Long idRecurso, Date fechaDesde, Date fechaHasta) {
		return GdeDAOFactory.getProRecDAO().existeRecurso(idProRecActual, idProcurador, idRecurso, fechaDesde, fechaHasta);
	} 
	
	// Metodos de Instancia
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
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		
		Boolean check=true;
		
		if (getRecurso()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					GdeError.PROREC_RECURSO_LABEL);
			check=false;
		}
		if (getFechaDesde()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					GdeError.PROREC_FECHADESDE_LABEL);
			check=false;
		}
		if(getFechaDesde()!=null && getFechaHasta()!=null && getFechaDesde().after(getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE,GdeError.PROREC_FECHAHASTA_LABEL, GdeError.PROREC_FECHADESDE_LABEL);
			check=false;
		}
		if (check &&  getFechaDesde()!=null && 
				ProRec.existeRecurso(getId(),getProcurador().getId(), getRecurso().getId(),getFechaDesde(),getFechaHasta())){
			addRecoverableError(GdeError.PROREC_RECURSOASIGNADO_LABEL);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unicidad
		if (hasError()) {
			return false;
		}
		
		return true;
	}

	// Metodos de negocio
 
	// ---> ABM ProRecDesHas	
	public ProRecDesHas createProRecDesHas(ProRecDesHas proRecDesHas) throws Exception {

		// Validaciones de negocio
		if (!proRecDesHas.validateCreate()) {
			return proRecDesHas;
		}

		GdeDAOFactory.getProRecDesHasDAO().update(proRecDesHas);

		return proRecDesHas;
	}
	
	public ProRecDesHas updateProRecDesHas(ProRecDesHas proRecDesHas) throws Exception {
		
		// Validaciones de negocio
		if (!proRecDesHas.validateUpdate()) {
			return proRecDesHas;
		}
		
		GdeDAOFactory.getProRecDesHasDAO().update(proRecDesHas);
		
	    return proRecDesHas;
	}
	
	public ProRecDesHas deleteProRecDesHas(ProRecDesHas proRecDesHas) throws Exception {

		// Validaciones de negocio
		if (!proRecDesHas.validateDelete()) {
			return proRecDesHas;
		}
		
		GdeDAOFactory.getProRecDesHasDAO().delete(proRecDesHas);
		
		return proRecDesHas;
	}
	// <--- ABM ProRecDesHas	

	// ---> ABM ProRecCom	
	public ProRecCom createProRecCom(ProRecCom proRecCom) throws Exception {

		// Validaciones de negocio
		if (!proRecCom.validateCreate()) {
			return proRecCom;
		}

		GdeDAOFactory.getProRecComDAO().update(proRecCom);

		return proRecCom;
	}
	
	public ProRecCom updateProRecCom(ProRecCom proRecCom) throws Exception {
		
		// Validaciones de negocio
		if (!proRecCom.validateUpdate()) {
			return proRecCom;
		}
		
		GdeDAOFactory.getProRecComDAO().update(proRecCom);
		
	    return proRecCom;
	}
	
	public ProRecCom deleteProRecCom(ProRecCom proRecCom) throws Exception {

		// Validaciones de negocio
		if (!proRecCom.validateDelete()) {
			return proRecCom;
		}
		
		GdeDAOFactory.getProRecComDAO().delete(proRecCom);
		
		return proRecCom;
	}
	// <--- ABM ProRecCom	

	
}
