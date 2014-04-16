//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Canal
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_canal")
public class Canal extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final long ID_CANAL_WEB = 1;

	public static final long ID_CANAL_CMD = 2;

	@Column(name = "desCanal")
	private String desCanal;

	// <#Propiedades#>

	// Constructores
	public Canal() {
		super();
		// Seteo de valores default
	}

	public Canal(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static Canal getById(Long id) {
		return (Canal) BalDAOFactory.getCanalDAO().getById(id);
	}

	public static Canal getByIdNull(Long id) {
		return (Canal) BalDAOFactory.getCanalDAO().getByIdNull(id);
	}

	public static List<Canal> getList() {
		return (ArrayList<Canal>) BalDAOFactory.getCanalDAO().getList();
	}

	public static List<Canal> getListActivos() {
		return (ArrayList<Canal>) BalDAOFactory.getCanalDAO().getListActiva();
	}

	// Getters y setters
	public String getDesCanal() {
		return desCanal;
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

		if (StringUtil.isNullOrEmpty(getDesCanal())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					BalError.CANAL_DESCANAL);
		}

		if (hasError()) {
			return false;
		}

		// Validaciones de unique
		/*
		 * UniqueMap uniqueMap = new UniqueMap();
		 * uniqueMap.addString("codCanal"); if(!GenericDAO.checkIsUnique(this,
		 * uniqueMap)) { addRecoverableError(BaseError.MSG_CAMPO_UNICO,
		 * BalError.CANAL_CODCANAL); }
		 */
		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el Canal. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getCanalDAO().update(this);
	}

	/**
	 * Desactiva el Canal. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getCanalDAO().update(this);
	}

	/**
	 * Valida la activacion del Canal
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
	 * Valida la desactivacion del Canal
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
