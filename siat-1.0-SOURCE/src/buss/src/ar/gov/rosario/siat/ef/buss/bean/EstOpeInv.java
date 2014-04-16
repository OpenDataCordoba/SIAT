//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a EstOpeInv
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_estOpeInv")
public class EstOpeInv extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String OBS_CREADO = "Operativo Creado";

	public static final Long ID_EST_ANALISIS = 1L;
	public static final Long ID_EST_INCIADO = 2L;
	public static final Long ID_EST_FINALIZADO = 3L;

	@Column(name = "desEstOpeInv")
	private String desEstOpeInv;

	// <#Propiedades#>

	// Constructores
	public EstOpeInv() {
		super();
		// Seteo de valores default
	}

	public EstOpeInv(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static EstOpeInv getById(Long id) {
		return (EstOpeInv) EfDAOFactory.getEstOpeInvDAO().getById(id);
	}

	public static EstOpeInv getByIdNull(Long id) {
		return (EstOpeInv) EfDAOFactory.getEstOpeInvDAO().getByIdNull(id);
	}

	public static List<EstOpeInv> getList() {
		return (List<EstOpeInv>) EfDAOFactory.getEstOpeInvDAO().getList();
	}

	public static List<EstOpeInv> getListActivos() {
		return (List<EstOpeInv>) EfDAOFactory.getEstOpeInvDAO().getListActiva();
	}

	// Getters y setters

	public String getDesEstOpeInv() {
		return desEstOpeInv;
	}

	public void setDesEstOpeInv(String desEstOpeInv) {
		this.desEstOpeInv = desEstOpeInv;
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
	 * Activa el EstOpeInv. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getEstOpeInvDAO().update(this);
	}

	/**
	 * Desactiva el EstOpeInv. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getEstOpeInvDAO().update(this);
	}

	/**
	 * Valida la activacion del EstOpeInv
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
	 * Valida la desactivacion del EstOpeInv
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
