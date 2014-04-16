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
 * Bean correspondiente a InicioInv
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_inicioInv")
public class InicioInv extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "detalle")
	private String detalle;

	@Column(name = "fecha")
	private Date fecha;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdenControl")
	private OrdenControl ordenControl;

	// <#Propiedades#>

	// Constructores
	public InicioInv() {
		super();
		// Seteo de valores default
	}

	public InicioInv(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static InicioInv getById(Long id) {
		return (InicioInv) EfDAOFactory.getInicioInvDAO().getById(id);
	}

	public static InicioInv getByIdNull(Long id) {
		return (InicioInv) EfDAOFactory.getInicioInvDAO().getByIdNull(id);
	}

	public static List<InicioInv> getList() {
		return (ArrayList<InicioInv>) EfDAOFactory.getInicioInvDAO().getList();
	}

	public static List<InicioInv> getListActivos() {
		return (ArrayList<InicioInv>) EfDAOFactory.getInicioInvDAO()
				.getListActiva();
	}

	public static InicioInv getByOrdenControl(OrdenControl ordenControl) throws Exception{
		return EfDAOFactory.getInicioInvDAO().getByOrdenControl(ordenControl);
	}
	
	// Getters y setters
	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

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
	 * Activa el InicioInv. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getInicioInvDAO().update(this);
	}

	/**
	 * Desactiva el InicioInv. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getInicioInvDAO().update(this);
	}

	/**
	 * Valida la activacion del InicioInv
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
	 * Valida la desactivacion del InicioInv
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
