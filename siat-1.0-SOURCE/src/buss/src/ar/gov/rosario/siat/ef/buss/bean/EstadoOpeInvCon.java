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
 * Bean correspondiente a EstadoOpeInvCon
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_estadoOpeInvCon")
public class EstadoOpeInvCon extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final Long ID_SELECCIONADO = 1L;

	public static final Long ID_EXCLUIR_SELEC = 2L;
	
	public static final Long ID_NO_VISITAR = 3L;
	
	public static final Long ID_VISITAR = 4L;

	public static final Long ID_INCLUIDO_OPERATIVO = 5L;
	
	public static final Long ID_ASIG_INV = 6L;

	public static final Long ID_CON_ACTA = 7L;
	
	public static final Long CON_INTERES_FISCAL = 8L;
	
	public static final Long SIN_INTERES_FISCAL = 9L;

	public static final Long CON_INTERES_A_FUTURO = 12L;

	public static final Long ID_NO_EXISTE = 13L;


	@Column(name = "desEstadoOpeInvCon")
	private String desEstadoOpeInvCon;

	// <#Propiedades#>

	// Constructores
	public EstadoOpeInvCon() {
		super();
		// Seteo de valores default
	}

	public EstadoOpeInvCon(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static EstadoOpeInvCon getById(Long id) {
		return (EstadoOpeInvCon) EfDAOFactory.getEstadoOpeInvConDAO().getById(
				id);
	}

	public static EstadoOpeInvCon getByIdNull(Long id) {
		return (EstadoOpeInvCon) EfDAOFactory.getEstadoOpeInvConDAO()
				.getByIdNull(id);
	}

	public static List<EstadoOpeInvCon> getList() {
		return (List<EstadoOpeInvCon>) EfDAOFactory.getEstadoOpeInvConDAO()
				.getList();
	}

	public static List<EstadoOpeInvCon> getListActivos() {
		return (List<EstadoOpeInvCon>) EfDAOFactory.getEstadoOpeInvConDAO()
				.getListActiva();
	}
	
	public static List<EstadoOpeInvCon> getListEstadosIniciales(Long idEstado){
		return (List<EstadoOpeInvCon>)EfDAOFactory.getEstadoOpeInvConDAO().getListEstadosIniciales(idEstado);
	}
	
	public static List<EstadoOpeInvCon> getListActivos(Long[] idsEstados) {
		return (List<EstadoOpeInvCon>)EfDAOFactory.getEstadoOpeInvConDAO().getList(idsEstados);
	}

	// Getters y setters

	public String getDesEstadoOpeInvCon() {
		return desEstadoOpeInvCon;
	}

	public void setDesEstadoOpeInvCon(String desEstadoOpeInvCon) {
		this.desEstadoOpeInvCon = desEstadoOpeInvCon;
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
	 * Activa el EstadoOpeInvCon. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getEstadoOpeInvConDAO().update(this);
	}

	/**
	 * Desactiva el EstadoOpeInvCon. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getEstadoOpeInvConDAO().update(this);
	}

	/**
	 * Valida la activacion del EstadoOpeInvCon
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
	 * Valida la desactivacion del EstadoOpeInvCon
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
