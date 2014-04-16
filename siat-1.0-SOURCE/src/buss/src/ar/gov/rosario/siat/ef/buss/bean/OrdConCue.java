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
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a OrdConCue
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_ordconcue")
public class OrdConCue extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdenControl")
	private OrdenControl ordenControl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCuenta")
	private Cuenta cuenta;
	
	@Column(name="fiscalizar")
	private Integer fiscalizar;

	// <#Propiedades#>

	// Constructores
	public OrdConCue() {
		super();
		// Seteo de valores default
	}

	public OrdConCue(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static OrdConCue getById(Long id) {
		return (OrdConCue) EfDAOFactory.getOrdConCueDAO().getById(id);
	}

	public static OrdConCue getByIdNull(Long id) {
		return (OrdConCue) EfDAOFactory.getOrdConCueDAO().getByIdNull(id);
	}

	public static List<OrdConCue> getList() {
		return (ArrayList<OrdConCue>) EfDAOFactory.getOrdConCueDAO().getList();
	}

	public static List<OrdConCue> getListActivos() {
		return (ArrayList<OrdConCue>) EfDAOFactory.getOrdConCueDAO()
				.getListActiva();
	}

	/**
	 * Obtiene un ordConCue con los valores pasados como parametro
	 * 
	 * @param ordenControl2
	 * @param cuenta2
	 * @return
	 */
	public static OrdConCue getByCuentaOrdCon(OrdenControl ordenControl,
			Cuenta cuenta) {
		return EfDAOFactory.getOrdConCueDAO().getByCuentaOrdCon(ordenControl,
				cuenta);

	}

	public static List<OrdConCue> getList(OrdenControl ordenControl, List<OrdConCue> listNotIn){
		return EfDAOFactory.getOrdConCueDAO().getByOrdCon(ordenControl,listNotIn);
	}

	public static List<OrdConCue> getDistinct(OrdenControl ordenControl){
		return EfDAOFactory.getOrdConCueDAO().getDistinct(ordenControl);
	}
	

	
	// Getters y setters

	public OrdenControl getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControl ordenControl) {
		this.ordenControl = ordenControl;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Integer getFiscalizar() {
		return fiscalizar;
	}

	public void setFiscalizar(Integer fiscalizar) {
		this.fiscalizar = fiscalizar;
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
	 * Activa el OrdConCue. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getOrdConCueDAO().update(this);
	}

	/**
	 * Desactiva el OrdConCue. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getOrdConCueDAO().update(this);
	}

	/**
	 * Valida la activacion del OrdConCue
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
	 * Valida la desactivacion del OrdConCue
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
