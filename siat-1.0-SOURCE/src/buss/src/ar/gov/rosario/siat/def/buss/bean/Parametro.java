//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Parametro
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_parametro")
public class Parametro extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "codParam")
	private String codParam;

	@Column(name = "desParam")
	private String desParam;

	@Column(name = "valor")
	private String valor;

	// Constructores
	public Parametro() {
		super();
	}

	// Metodos de Clase
	public static Parametro getById(Long id) {
		return (Parametro) DefDAOFactory.getParametroDAO().getById(id);
	}

	public static Parametro getByIdNull(Long id) {
		return (Parametro) DefDAOFactory.getParametroDAO().getByIdNull(id);
	}

	public static List<Parametro> getList() {
		return (ArrayList<Parametro>) DefDAOFactory.getParametroDAO().getList();
	}

	public static List<Parametro> getListActivos() {
		return (ArrayList<Parametro>) DefDAOFactory.getParametroDAO()
				.getListActiva();
	}

	// Getters y setters
	public String getCodParam() {
		return codParam;
	}

	public void setCodParam(String codParam) {
		this.codParam = codParam;
	}

	public String getDesParam() {
		return desParam;
	}

	public void setDesParam(String desParam) {
		this.desParam = desParam;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
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

		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones
		if (StringUtil.isNullOrEmpty(getCodParam())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					DefError.PARAMETRO_CODPARAM);
		}

		if (StringUtil.isNullOrEmpty(getDesParam())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					DefError.PARAMETRO_DESPARAM);
		}

		if (StringUtil.isNullOrEmpty(getValor())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					DefError.PARAMETRO_VALOR);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codParam");
		if (!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO,
					DefError.PARAMETRO_CODPARAM);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el Parametro. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getParametroDAO().update(this);
	}

	/**
	 * Desactiva el Parametro. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		DefDAOFactory.getParametroDAO().update(this);
	}

	/**
	 * Valida la activacion del Parametro
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
	 * Valida la desactivacion del Parametro
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}
}
