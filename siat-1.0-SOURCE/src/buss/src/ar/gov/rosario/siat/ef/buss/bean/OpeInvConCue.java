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
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a OpeInvCon
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_opeInvConCue")
public class OpeInvConCue extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idopeinvcon")
	private OpeInvCon opeInvCon;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCuenta")
	private Cuenta cuenta;
	
	@Column(name="seleccionada")
	private Boolean seleccionada;

	// <#Propiedades#>

	// Constructores
	public OpeInvConCue() {
		super();
		// Seteo de valores default
	}

	public OpeInvConCue(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static OpeInvConCue getById(Long id) {
		return (OpeInvConCue) EfDAOFactory.getOpeInvConCueDAO().getById(id);
	}

	public static OpeInvConCue getByIdNull(Long id) {
		return (OpeInvConCue) EfDAOFactory.getOpeInvConCueDAO().getByIdNull(id);
	}

	public static List<OpeInvConCue> getList() {
		return (List<OpeInvConCue>) EfDAOFactory.getOpeInvConCueDAO().getList();
	}

	public static List<OpeInvConCue> getListActivos() {
		return (List<OpeInvConCue>) EfDAOFactory.getOpeInvConCueDAO().getListActiva();
	}
	
	/**
	 *Elimina los registros de opeInvConCue para el valor pasado como parametro
	 */ 
	public static void delete(OpeInvCon opeInvCon) {
		EfDAOFactory.getOpeInvConCueDAO().delete(opeInvCon);		
	}

	//Metodos de instancia

	// Getters y setters
	public OpeInvCon getOpeInvCon() {
		return opeInvCon;
	}

	public void setOpeInvCon(OpeInvCon opeInvCon) {
		this.opeInvCon = opeInvCon;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Boolean getSeleccionada() {
		return seleccionada;
	}

	public void setSeleccionada(Boolean seleccionada) {
		this.seleccionada = seleccionada;
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
	 * Activa el OpeInvCon. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getOpeInvConCueDAO().update(this);
	}

	/**
	 * Desactiva el OpeInvCon. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getOpeInvConCueDAO().update(this);
	}

	/**
	 * Valida la activacion del OpeInvCon
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
	 * Valida la desactivacion del OpeInvCon
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
