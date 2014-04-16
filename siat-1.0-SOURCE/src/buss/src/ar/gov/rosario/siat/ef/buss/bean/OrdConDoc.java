//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.DocumentacionVO;
import ar.gov.rosario.siat.ef.iface.model.OrdConDocVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a OrdConDoc
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_ordConDoc")
public class OrdConDoc extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idDocumentacion")
	private Documentacion documentacion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idActa")
	private Acta acta;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idActaProc")
	private Acta actaProc;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdenControl")
	private OrdenControl ordenControl;

	@Column(name = "observaciones")
	private String observaciones;
	
	// <#Propiedades#>

	// Constructores
	public OrdConDoc() {
		super();
		// Seteo de valores default
	}

	public OrdConDoc(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static OrdConDoc getById(Long id) {
		return (OrdConDoc) EfDAOFactory.getOrdConDocDAO().getById(id);
	}

	public static OrdConDoc getByIdNull(Long id) {
		return (OrdConDoc) EfDAOFactory.getOrdConDocDAO().getByIdNull(id);
	}

	public static List<OrdConDoc> getList() {
		return (ArrayList<OrdConDoc>) EfDAOFactory.getOrdConDocDAO().getList();
	}

	public static List<OrdConDoc> getListActivos() {
		return (ArrayList<OrdConDoc>) EfDAOFactory.getOrdConDocDAO()
				.getListActiva();
	}

	/**
	 * Obtiene una lista de OrdConDoc filtrando por el campo idActaProc con el valor pasado como parametro
	 * @param id
	 * @return
	 */
	public static List<OrdConDoc> getListByActaProcedimiento(Acta actaProcedimiento) {
		return EfDAOFactory.getOrdConDocDAO().getListByActaProc(actaProcedimiento);
	}

	/**
	 * Obtiene los OrdconDoc de todas las actas de la orden de control pasada como parametro,
	 * que sean del tipo INICIO o REQ y que el idActaProc sea null
	 * @param ordenControl
	 * @return
	 */
	public static List<OrdConDoc> getList4ActaProc(OrdenControl ordenControl, Acta actaProc){
		return EfDAOFactory.getOrdConDocDAO().getList4ActaProc(ordenControl, actaProc);
	}
	
	// Getters y setters
	public Documentacion getDocumentacion() {
		return documentacion;
	}

	public void setDocumentacion(Documentacion documentacion) {
		this.documentacion = documentacion;
	}

	public Acta getActa() {
		return acta;
	}

	public void setActa(Acta acta) {
		this.acta = acta;
	}

	public OrdenControl getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControl ordenControl) {
		this.ordenControl = ordenControl;
	}

	public Acta getActaProc() {
		return actaProc;
	}

	public void setActaProc(Acta actaProc) {
		this.actaProc = actaProc;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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

	// Metodos de negocio

	/**
	 * Activa el OrdConDoc. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getOrdConDocDAO().update(this);
	}

	/**
	 * Desactiva el OrdConDoc. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getOrdConDocDAO().update(this);
	}

	/**
	 * Valida la activacion del OrdConDoc
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
	 * Valida la desactivacion del OrdConDoc
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	public OrdConDocVO toVO4Print() throws Exception {
		OrdConDocVO ordConDocVO = (OrdConDocVO) this.toVO(0, false);
		ordConDocVO.setDocumentacion((DocumentacionVO) this.documentacion.toVO4Print());
		
		return ordConDocVO;
	}
	
}
