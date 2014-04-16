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
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a CompFuenteRes
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_compFuenteRes")
public class CompFuenteRes extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idComparacion")
	private Comparacion comparacion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idComFueMin")
	private CompFuente comFueMin;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idComFueSus")
	private CompFuente ComFueSus;

	@Column(name = "operacion")
	private String operacion;

	@Column(name = "diferencia")
	private Double diferencia;
	
	
	

	// <#Propiedades#>

	// Constructores
	public CompFuenteRes() {
		super();
		// Seteo de valores default
	}

	public CompFuenteRes(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static CompFuenteRes getById(Long id) {
		return (CompFuenteRes) EfDAOFactory.getCompFuenteResDAO().getById(id);
	}

	public static CompFuenteRes getByIdNull(Long id) {
		return (CompFuenteRes) EfDAOFactory.getCompFuenteResDAO().getByIdNull(
				id);
	}

	public static List<CompFuenteRes> getList() {
		return (ArrayList<CompFuenteRes>) EfDAOFactory.getCompFuenteResDAO()
				.getList();
	}

	public static List<CompFuenteRes> getListActivos() {
		return (ArrayList<CompFuenteRes>) EfDAOFactory.getCompFuenteResDAO()
				.getListActiva();
	}

	public static List<CompFuenteRes> getListByOrdCon(Long idOrdenControl) {
		return EfDAOFactory.getCompFuenteResDAO().getListByOrdCon(idOrdenControl);
	}

	// Getters y setters	
	public Comparacion getComparacion() {
		return comparacion;
	}

	public void setComparacion(Comparacion comparacion) {
		this.comparacion = comparacion;
	}

	public CompFuente getComFueMin() {
		return comFueMin;
	}
	
	public void setComFueMin(CompFuente comFueMin) {
		this.comFueMin = comFueMin;
	}

	public CompFuente getComFueSus() {
		return ComFueSus;
	}

	public void setComFueSus(CompFuente comFueSus) {
		ComFueSus = comFueSus;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public Double getDiferencia() {
		return diferencia;
	}

	public void setDiferencia(Double diferencia) {
		this.diferencia = diferencia;
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
	 * Activa el CompFuenteRes. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getCompFuenteResDAO().update(this);
	}

	/**
	 * Desactiva el CompFuenteRes. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getCompFuenteResDAO().update(this);
	}

	/**
	 * Valida la activacion del CompFuenteRes
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
	 * Valida la desactivacion del CompFuenteRes
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
	public String getDiferenciaReport() {
		return "$"+StringUtil.formatDouble(this.diferencia);
	}
	
	
}
