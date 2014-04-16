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
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a TipoActa
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_tipoActa")
public class TipoActa extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final Long ID_TIPO_INICIO_PROCEDIMIENTO = 1l;
	
	public static final Long ID_TIPO_PROCEDIMIENTO = 2L;

	public static final Long ID_TIPO_REQ_INF = 3L;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idFormulario")
	private Formulario formulario;

	@Column(name = "desTipoActa")
	private String desTipoActa;

	// <#Propiedades#>

	// Constructores
	public TipoActa() {
		super();
		// Seteo de valores default
	}

	public TipoActa(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static TipoActa getById(Long id) {
		return (TipoActa) EfDAOFactory.getTipoActaDAO().getById(id);
	}

	public static TipoActa getByIdNull(Long id) {
		return (TipoActa) EfDAOFactory.getTipoActaDAO().getByIdNull(id);
	}

	public static List<TipoActa> getList() {
		return (ArrayList<TipoActa>) EfDAOFactory.getTipoActaDAO().getList();
	}

	public static List<TipoActa> getListActivos() {
		return (ArrayList<TipoActa>) EfDAOFactory.getTipoActaDAO()
				.getListActiva();
	}

	// Getters y setters
	public Formulario getFormulario() {
		return formulario;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}

	public String getDesTipoActa() {
		return desTipoActa;
	}

	public void setDesTipoActa(String desTipoActa) {
		this.desTipoActa = desTipoActa;
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
	 * Activa el TipoActa. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getTipoActaDAO().update(this);
	}

	/**
	 * Desactiva el TipoActa. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getTipoActaDAO().update(this);
	}

	/**
	 * Valida la activacion del TipoActa
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
	 * Valida la desactivacion del TipoActa
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
