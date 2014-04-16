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
import ar.gov.rosario.siat.ef.iface.model.HisEstOpeInvConVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a HisEstOpeInvCon
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_hisestopeinvcon")
public class HisEstOpeInvCon extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String OBS_SELECCIONADO = "Selección puntual de Contribuyente";

	public static final String OBS_EXLUIDO_SELEC = "Excluído de Selección";

	public static final String OBS_CAMBIO_ESTADO = "Se cambió el estado a ";

	public static final String OBS_CAMBIO_CUENTA_SELEC = "Se cambió la cuenta seleccionada a la cuenta nro: ";

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idopeinvcon")
	private OpeInvCon opeInvCon;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idEstadoOpeInvCon")
	private EstadoOpeInvCon estadoOpeInvCon;

	@Column(name = "observacion")
	private String observacion;

	// <#Propiedades#>

	// Constructores
	public HisEstOpeInvCon() {
		super();
		// Seteo de valores default
	}

	public HisEstOpeInvCon(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static HisEstOpeInvCon getById(Long id) {
		return (HisEstOpeInvCon) EfDAOFactory.getHisEstOpeInvConDAO().getById(
				id);
	}

	public static HisEstOpeInvCon getByIdNull(Long id) {
		return (HisEstOpeInvCon) EfDAOFactory.getHisEstOpeInvConDAO()
				.getByIdNull(id);
	}

	public static List<HisEstOpeInvCon> getList() {
		return (List<HisEstOpeInvCon>) EfDAOFactory.getHisEstOpeInvConDAO()
				.getList();
	}

	public static List<HisEstOpeInvCon> getListActivos() {
		return (List<HisEstOpeInvCon>) EfDAOFactory.getHisEstOpeInvConDAO()
				.getListActiva();
	}

	/**
	 * Elimina los registro de historico para el valor pasado como parametro
	 * @param opeInvCon
	 */
	public static void delete(OpeInvCon opeInvCon) {
		EfDAOFactory.getHisEstOpeInvConDAO().delete(opeInvCon);
	}

	// Getters y setters

	public OpeInvCon getOpeInvCon() {
		return opeInvCon;
	}

	public void setOpeInvCon(OpeInvCon opeInvCon) {
		this.opeInvCon = opeInvCon;
	}

	public EstadoOpeInvCon getEstadoOpeInvCon() {
		return estadoOpeInvCon;
	}

	public void setEstadoOpeInvCon(EstadoOpeInvCon estadoOpeInvCon) {
		this.estadoOpeInvCon = estadoOpeInvCon;
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
	 * Activa el HisEstOpeInvCon. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getHisEstOpeInvConDAO().update(this);
	}

	/**
	 * Desactiva el HisEstOpeInvCon. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getHisEstOpeInvConDAO().update(this);
	}

	/**
	 * Valida la activacion del HisEstOpeInvCon
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
	 * Valida la desactivacion del HisEstOpeInvCon
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
	public HisEstOpeInvConVO toVO() throws Exception {
		HisEstOpeInvConVO hisEstOpeInvConVO = new HisEstOpeInvConVO();
		hisEstOpeInvConVO.setDesEstado(this.estadoOpeInvCon.getDesEstadoOpeInvCon());
		hisEstOpeInvConVO.setFechaEstado(this.getFechaUltMdf());
		hisEstOpeInvConVO.setObservaciones(this.observacion);
		hisEstOpeInvConVO.setUsuario(this.getUsuarioUltMdf());
		
		return hisEstOpeInvConVO;
	}

}
