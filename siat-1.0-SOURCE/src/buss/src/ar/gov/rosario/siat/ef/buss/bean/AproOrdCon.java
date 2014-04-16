//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.ArrayList;
import java.util.Date;
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
 * Bean correspondiente a AproOrdCon - Aprobacion de OrdenControl
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_aproOrdCon")
public class AproOrdCon extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdenControl")
	private OrdenControl ordenControl;
	
	@Column(name = "fecha")
	private Date fecha;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idEstadoOrden")
	private EstadoOrden estadoOrden;

	@Column(name = "observacion")
	private String observacion;

	// Constructores
	public AproOrdCon() {
		super();
		// Seteo de valores default
	}

	public AproOrdCon(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static AproOrdCon getById(Long id) {
		return (AproOrdCon) EfDAOFactory.getAproOrdConDAO().getById(id);
	}

	public static AproOrdCon getByIdNull(Long id) {
		return (AproOrdCon) EfDAOFactory.getAproOrdConDAO().getByIdNull(id);
	}

	public static List<AproOrdCon> getList() {
		return (ArrayList<AproOrdCon>) EfDAOFactory.getAproOrdConDAO()
				.getList();
	}

	public static List<AproOrdCon> getListActivos() {
		return (ArrayList<AproOrdCon>) EfDAOFactory.getAproOrdConDAO()
				.getListActiva();
	}

	// Getters y setters
	public OrdenControl getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControl ordenControl) {
		this.ordenControl = ordenControl;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public EstadoOrden getEstadoOrden() {
		return estadoOrden;
	}

	public void setEstadoOrden(EstadoOrden estadoOrden) {
		this.estadoOrden = estadoOrden;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
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
	 * Activa el AproOrdCon. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getAproOrdConDAO().update(this);
	}

	/**
	 * Desactiva el AproOrdCon. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getAproOrdConDAO().update(this);
	}

	/**
	 * Valida la activacion del AproOrdCon
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
	 * Valida la desactivacion del AproOrdCon
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
