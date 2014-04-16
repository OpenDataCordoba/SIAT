//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a DesEspExe
 * Representa una exencion asociada a un descuento especial, de la lista de exenciones del recurso asociado al descuento especial
 * @author tecso
 */
@Entity
@Table(name = "gde_desespexe")
public class DesEspExe extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idDesEsp")
	private DesEsp desEsp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idExencion")
	private Exencion exencion;

	@Column(name = "fechaDesde")
	private Date fechaDesde;

	@Column(name = "fechaHasta")
	private Date fechaHasta;

	// <#Propiedades#>

	// Constructores
	public DesEspExe() {
		super();
		// Seteo de valores default
	}

	public DesEspExe(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static DesEspExe getById(Long id) {
		return (DesEspExe) GdeDAOFactory.getDesEspExeDAO().getById(id);
	}

	public static DesEspExe getByIdNull(Long id) {
		return (DesEspExe) GdeDAOFactory.getDesEspExeDAO().getByIdNull(id);
	}

	public static List<DesEspExe> getList() {
		return (ArrayList<DesEspExe>) GdeDAOFactory.getDesEspExeDAO().getList();
	}

	public static List<DesEspExe> getListActivos() {
		return (ArrayList<DesEspExe>) GdeDAOFactory.getDesEspExeDAO()
				.getListActiva();
	}

	// Getters y setters

	public DesEsp getDesEsp() {
		return desEsp;
	}

	public void setDesEsp(DesEsp desEsp) {
		this.desEsp = desEsp;
	}

	public Exencion getExencion() {
		return exencion;
	}

	public void setExencion(Exencion exencion) {
		this.exencion = exencion;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
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
		if(getExencion()==null || getExencion().getId()<0)
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.EXENCION_LABEL);
		if(getFechaDesde()==null)
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESESPEXE_FECHADESDE);
		if(getFechaHasta()==null)
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESESPEXE_FECHAHASTA);		
		
		if(getFechaDesde()!=null && getFechaHasta()!=null && getFechaDesde().after(getFechaHasta()))
			addRecoverableError(BaseError.MSG_VALORMENORQUE, GdeError.DESESPEXE_FECHADESDE, GdeError.DESESPEXE_FECHAHASTA);

		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el DesEspExe. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getDesEspExeDAO().update(this);
	}

	/**
	 * Desactiva el DesEspExe. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getDesEspExeDAO().update(this);
	}

	/**
	 * Valida la activacion del DesEspExe
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
	 * Valida la desactivacion del DesEspExe
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
