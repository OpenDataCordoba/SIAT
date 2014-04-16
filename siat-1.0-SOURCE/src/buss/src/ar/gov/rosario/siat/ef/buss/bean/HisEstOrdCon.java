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

/**
 * Bean correspondiente a HisEstOrdCon
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_hisEstOrdCon")
public class HisEstOrdCon extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "fecha")
	private Date fecha;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdenControl")
	private OrdenControl ordenControl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idEstadoOrden")
	private EstadoOrden estadoOrden;

	@Column(name = "observacion")
	private String observacion;

	// <#Propiedades#>

	// Constructores
	public HisEstOrdCon() {
		super();
		// Seteo de valores default
	}

	public HisEstOrdCon(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static HisEstOrdCon getById(Long id) {
		return (HisEstOrdCon) EfDAOFactory.getHisEstOrdConDAO().getById(id);
	}

	public static HisEstOrdCon getByIdNull(Long id) {
		return (HisEstOrdCon) EfDAOFactory.getHisEstOrdConDAO().getByIdNull(id);
	}

	public static List<HisEstOrdCon> getList() {
		return (ArrayList<HisEstOrdCon>) EfDAOFactory.getHisEstOrdConDAO()
				.getList();
	}

	public static List<HisEstOrdCon> getListActivos() {
		return (ArrayList<HisEstOrdCon>) EfDAOFactory.getHisEstOrdConDAO()
				.getListActiva();
	}

	// Getters y setters
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public OrdenControl getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControl ordenControl) {
		this.ordenControl = ordenControl;
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

	// <#MetodosBeanDetalle#>
}
