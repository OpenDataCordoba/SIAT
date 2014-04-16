//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a TipoDoc
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_tipoDoc")
public class TipoDoc extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "desTipoDoc")
	private String desTipoDoc;

	@Column(name = "orden")
	private Integer orden;

	// <#Propiedades#>

	// Constructores
	public TipoDoc() {
		super();
		// Seteo de valores default
	}

	public TipoDoc(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static TipoDoc getById(Long id) {
		return (TipoDoc) EfDAOFactory.getTipoDocDAO().getById(id);
	}

	public static TipoDoc getByIdNull(Long id) {
		return (TipoDoc) EfDAOFactory.getTipoDocDAO().getByIdNull(id);
	}

	public static List<TipoDoc> getList() {
		return (ArrayList<TipoDoc>) EfDAOFactory.getTipoDocDAO().getList();
	}

	public static List<TipoDoc> getListActivos() {
		return (ArrayList<TipoDoc>) EfDAOFactory.getTipoDocDAO()
				.getListActiva();
	}

	// Getters y setters
	public String getDesTipoDoc() {
		return desTipoDoc;
	}

	public void setDesTipoDoc(String desTipoDoc) {
		this.desTipoDoc = desTipoDoc;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
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
	 * Activa el TipoDoc. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getTipoDocDAO().update(this);
	}

	/**
	 * Desactiva el TipoDoc. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getTipoDocDAO().update(this);
	}

	/**
	 * Valida la activacion del TipoDoc
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
	 * Valida la desactivacion del TipoDoc
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
