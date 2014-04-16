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
 * Bean correspondiente a MesaEntrada
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_mesaEntrada")
public class MesaEntrada extends BaseBO {

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

	// <#Propiedades#>

	// Constructores
	public MesaEntrada() {
		super();
		// Seteo de valores default
	}

	public MesaEntrada(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static MesaEntrada getById(Long id) {
		return (MesaEntrada) EfDAOFactory.getMesaEntradaDAO().getById(id);
	}

	public static MesaEntrada getByIdNull(Long id) {
		return (MesaEntrada) EfDAOFactory.getMesaEntradaDAO().getByIdNull(id);
	}

	public static List<MesaEntrada> getList() {
		return (ArrayList<MesaEntrada>) EfDAOFactory.getMesaEntradaDAO()
				.getList();
	}

	public static List<MesaEntrada> getListActivos() {
		return (ArrayList<MesaEntrada>) EfDAOFactory.getMesaEntradaDAO()
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

		// Validaciones de unique
	

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el MesaEntrada. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getMesaEntradaDAO().update(this);
	}

	/**
	 * Desactiva el MesaEntrada. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getMesaEntradaDAO().update(this);
	}

	/**
	 * Valida la activacion del MesaEntrada
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
	 * Valida la desactivacion del MesaEntrada
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
