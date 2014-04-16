//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a HisEstOpeInv
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_hisestopeinv")
public class HisEstOpeInv extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "observacion")
	private String observacion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idopeinv")
	private OpeInv opeInv;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idEstOpeInv")
	private EstOpeInv estOpeInv;

	// <#Propiedades#>

	// Constructores
	public HisEstOpeInv() {
		super();
		// Seteo de valores default
	}

	public HisEstOpeInv(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static HisEstOpeInv getById(Long id) {
		return (HisEstOpeInv) EfDAOFactory.getHisEstOpeInvDAO().getById(id);
	}

	public static HisEstOpeInv getByIdNull(Long id) {
		return (HisEstOpeInv) EfDAOFactory.getHisEstOpeInvDAO().getByIdNull(id);
	}

	public static List<HisEstOpeInv> getList() {
		return (List<HisEstOpeInv>) EfDAOFactory.getHisEstOpeInvDAO().getList();
	}

	public static List<HisEstOpeInv> getListActivos() {
		return (List<HisEstOpeInv>) EfDAOFactory.getHisEstOpeInvDAO()
				.getListActiva();
	}

	// Getters y setters

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public OpeInv getOpeInv() {
		return opeInv;
	}

	public void setOpeInv(OpeInv opeInv) {
		this.opeInv = opeInv;
	}

	public EstOpeInv getEstOpeInv() {
		return estOpeInv;
	}

	public void setEstOpeInv(EstOpeInv estOpeInv) {
		this.estOpeInv = estOpeInv;
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
	 * Activa el HisEstOpeInv. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getHisEstOpeInvDAO().update(this);
	}

	/**
	 * Desactiva el HisEstOpeInv. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getHisEstOpeInvDAO().update(this);
	}

	/**
	 * Valida la activacion del HisEstOpeInv
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
	 * Valida la desactivacion del HisEstOpeInv
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
