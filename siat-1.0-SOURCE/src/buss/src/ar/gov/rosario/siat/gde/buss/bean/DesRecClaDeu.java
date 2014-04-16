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
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a DesRecClaDeu Representa una clasificación de deuda
 * para un descuento especial, teniendo en cuenta el recurso del DesEsp
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_desRecClaDeu")
public class DesRecClaDeu extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idDescuento")
	private DesEsp desEsp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idRecClaDeu")
	private RecClaDeu recClaDeu;

	@Column(name = "fechaDesde")
	private Date fechaDesde;

	@Column(name = "fechaHasta")
	private Date fechaHasta;

	// <#Propiedades#>

	// Constructores
	public DesRecClaDeu() {
		super();
		// Seteo de valores default
	}

	public DesRecClaDeu(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static DesRecClaDeu getById(Long id) {
		return (DesRecClaDeu) GdeDAOFactory.getDesRecClaDeuDAO().getById(id);
	}

	public static DesRecClaDeu getByIdNull(Long id) {
		return (DesRecClaDeu) GdeDAOFactory.getDesRecClaDeuDAO()
				.getByIdNull(id);
	}

	public static List<DesRecClaDeu> getList() {
		return (ArrayList<DesRecClaDeu>) GdeDAOFactory.getDesRecClaDeuDAO()
				.getList();
	}

	public static List<DesRecClaDeu> getListActivos() {
		return (ArrayList<DesRecClaDeu>) GdeDAOFactory.getDesRecClaDeuDAO()
				.getListActiva();
	}

	// Getters y setters

	public DesEsp getDesEsp() {
		return desEsp;
	}

	public void setDesEsp(DesEsp desEsp) {
		this.desEsp = desEsp;
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

	public RecClaDeu getRecClaDeu() {
		return recClaDeu;
	}

	public void setRecClaDeu(RecClaDeu recClaDeu) {
		this.recClaDeu = recClaDeu;
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
		if (getRecClaDeu()==null || getRecClaDeu().getId()<0) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESRECCLADEU_CLASIFICACION);
		}

		if (getFechaDesde()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESRECCLADEU_FECHADESDE);
		}else if(getFechaHasta()!=null && getFechaDesde().after(getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, GdeError.DESRECCLADEU_FECHADESDE, GdeError.DESRECCLADEU_FECHAHASTA);
		}


		if (hasError()) {
			return false;
		}
		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el DesRecClaDeu. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getDesRecClaDeuDAO().update(this);
	}

	/**
	 * Desactiva el DesRecClaDeu. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		} 
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getDesRecClaDeuDAO().update(this);
	}

	/**
	 * Valida la activacion del DesRecClaDeu
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
	 * Valida la desactivacion del DesRecClaDeu
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
