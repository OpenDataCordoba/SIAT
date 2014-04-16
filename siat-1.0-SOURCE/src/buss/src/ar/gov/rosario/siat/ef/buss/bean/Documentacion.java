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
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Documentacion
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_documentacion")
public class Documentacion extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "desDoc")
	private String desDoc;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idTipoDoc")
	private TipoDoc tipoDoc;

	// <#Propiedades#>

	// Constructores
	public Documentacion() {
		super();
		// Seteo de valores default
	}

	public Documentacion(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static Documentacion getById(Long id) {
		return (Documentacion) EfDAOFactory.getDocumentacionDAO().getById(id);
	}

	public static Documentacion getByIdNull(Long id) {
		return (Documentacion) EfDAOFactory.getDocumentacionDAO().getByIdNull(
				id);
	}

	public static List<Documentacion> getList() {
		return (ArrayList<Documentacion>) EfDAOFactory.getDocumentacionDAO()
				.getList();
	}

	public static List<Documentacion> getListActivos() {
		return (ArrayList<Documentacion>) EfDAOFactory.getDocumentacionDAO()
				.getListActiva();
	}
	
	public static List<Documentacion> getListActivosOrderByTipoNroOrden() {
		return (ArrayList<Documentacion>) EfDAOFactory.getDocumentacionDAO()
				.getListActivosOrderByTipoNroOrden();
	}

	// Getters y setters
	public String getDesDoc() {
		return desDoc;
	}

	public void setDesDoc(String desDocumentacion) {
		this.desDoc = desDocumentacion;
	}

	public TipoDoc getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(TipoDoc tipoDoc) {
		this.tipoDoc = tipoDoc;
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
	 * Activa el Documentacion. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getDocumentacionDAO().update(this);
	}

	/**
	 * Desactiva el Documentacion. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getDocumentacionDAO().update(this);
	}

	/**
	 * Valida la activacion del Documentacion
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
	 * Valida la desactivacion del Documentacion
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
	
	public DocumentacionVO toVO4Print() throws Exception {
		DocumentacionVO documentacionVO = (DocumentacionVO) this.toVO(0, false);
		
		return documentacionVO;
	}
}
