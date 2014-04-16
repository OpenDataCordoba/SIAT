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
 * Bean correspondiente a DetAjuDocSop - Resoluciones de la OrdenControl
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_detAjuDocSop")
public class DetAjuDocSop extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idDetAju")
	private DetAju detAju;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idDocSop")
	private DocSop docSop;

	@Column(name = "fechaGenerada")
	private Date fechaGenerada;

	@Column(name = "fechaNotificada")
	private Date fechaNotificada;
	
	@Column(name="fechaResolucion")
	private Date fechaResolucion;

	@Column(name = "notificadaPor")
	private String notificadaPor;
	
	@Column(name="fechaPresentacion")
	private Date fechaPresentacion; 

	@Column(name = "observacion")
	private String observacion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdenControl")
	private OrdenControl ordenControl;

	// <#Propiedades#>

	// Constructores
	public DetAjuDocSop() {
		super();
		// Seteo de valores default
	}

	public DetAjuDocSop(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static DetAjuDocSop getById(Long id) {
		return (DetAjuDocSop) EfDAOFactory.getDetAjuDocSopDAO().getById(id);
	}

	public static DetAjuDocSop getByIdNull(Long id) {
		return (DetAjuDocSop) EfDAOFactory.getDetAjuDocSopDAO().getByIdNull(id);
	}

	public static List<DetAjuDocSop> getList() {
		return (ArrayList<DetAjuDocSop>) EfDAOFactory.getDetAjuDocSopDAO()
				.getList();
	}

	public static List<DetAjuDocSop> getListActivos() {
		return (ArrayList<DetAjuDocSop>) EfDAOFactory.getDetAjuDocSopDAO()
				.getListActiva();
	}

	// Getters y setters
	public DetAju getDetAju() {
		return detAju;
	}

	public void setDetAju(DetAju detAju) {
		this.detAju = detAju;
	}

	public DocSop getDocSop() {
		return docSop;
	}

	public void setDocSop(DocSop docSop) {
		this.docSop = docSop;
	}

	public Date getFechaGenerada() {
		return fechaGenerada;
	}

	public void setFechaGenerada(Date fechaGenerada) {
		this.fechaGenerada = fechaGenerada;
	}

	public Date getFechaNotificada() {
		return fechaNotificada;
	}

	public void setFechaNotificada(Date fechaNotificada) {
		this.fechaNotificada = fechaNotificada;
	}

	public String getNotificadaPor() {
		return notificadaPor;
	}

	public void setNotificadaPor(String notificadaPor) {
		this.notificadaPor = notificadaPor;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public OrdenControl getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControl ordenControl) {
		this.ordenControl = ordenControl;
	}

	public Date getFechaResolucion() {
		return fechaResolucion;
	}

	public void setFechaResolucion(Date fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}

	public Date getFechaPresentacion() {
		return fechaPresentacion;
	}

	public void setFechaPresentacion(Date fechaPresentacion) {
		this.fechaPresentacion = fechaPresentacion;
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
	 * Activa el DetAjuDocSop. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getDetAjuDocSopDAO().update(this);
	}

	/**
	 * Desactiva el DetAjuDocSop. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getDetAjuDocSopDAO().update(this);
	}

	/**
	 * Valida la activacion del DetAjuDocSop
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
	 * Valida la desactivacion del DetAjuDocSop
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
