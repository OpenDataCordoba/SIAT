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

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.OrdConCueVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.ef.iface.model.PeriodoOrdenVO;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a PeriodoOrden
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_periodoOrden")
public class PeriodoOrden extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdenControl")
	private OrdenControl ordenControl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdConCue")
	private OrdConCue ordConCue;

	@Column(name = "periodo")
	private Integer periodo;

	@Column(name = "anio")
	private Integer anio;

	// <#Propiedades#>

	// Constructores
	public PeriodoOrden() {
		super();
		// Seteo de valores default
	}

	public PeriodoOrden(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static PeriodoOrden getById(Long id) {
		return (PeriodoOrden) EfDAOFactory.getPeriodoOrdenDAO().getById(id);
	}

	public static PeriodoOrden getByIdNull(Long id) {
		return (PeriodoOrden) EfDAOFactory.getPeriodoOrdenDAO().getByIdNull(id);
	}

	public static List<PeriodoOrden> getList() {
		return (ArrayList<PeriodoOrden>) EfDAOFactory.getPeriodoOrdenDAO()
				.getList();
	}

	public static List<PeriodoOrden> getListActivos() {
		return (ArrayList<PeriodoOrden>) EfDAOFactory.getPeriodoOrdenDAO()
				.getListActiva();
	}
	
	public static List<PeriodoOrden> getListByOrdConCue(OrdConCue ordConCue) {
		return (ArrayList<PeriodoOrden>) EfDAOFactory.getPeriodoOrdenDAO()
				.getListByOrdConCue(ordConCue);
	}
	


	// Getters y setters
	public OrdenControl getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControl ordenControl) {
		this.ordenControl = ordenControl;
	}

	public OrdConCue getOrdConCue() {
		return ordConCue;
	}

	public void setOrdConCue(OrdConCue ordConCue) {
		this.ordConCue = ordConCue;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
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
		if (GenericDAO.hasReference(this, DetAjuDet.class, "periodoOrden")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							EfError.PERIODOORDEN_LABEL, EfError.DETAJUDET_LABEL );
		}

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
	 * Activa el PeriodoOrden. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getPeriodoOrdenDAO().update(this);
	}

	/**
	 * Desactiva el PeriodoOrden. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getPeriodoOrdenDAO().update(this);
	}

	/**
	 * Valida la activacion del PeriodoOrden
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
	 * Valida la desactivacion del PeriodoOrden
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	/**
	 * HAce toVO de nivel 0, sin listas. Setea la ordenControl con nivel 0 sin listas. Setea OrdConCue con la cuenta 
	 * @return
	 * @throws Exception
	 */
	public PeriodoOrdenVO toVO4Admin() throws Exception {
		PeriodoOrdenVO periodoOrdenVO = (PeriodoOrdenVO) this.toVO(0, false);
		periodoOrdenVO.setOrdenControl(ordenControl!=null?(OrdenControlVO) ordenControl.toVO(0, false):null);
		
		if(ordConCue!=null){
			periodoOrdenVO.setOrdConCue((OrdConCueVO) ordConCue.toVO(0, false));
			periodoOrdenVO.getOrdConCue().setCuenta(ordConCue.getCuenta().toVOWithRecurso());
		}
		
		return periodoOrdenVO;
	}
	
	/**
	 * HAce toVO de nivel 0, sin listas. Setea la ordenControl con nivel 0 sin listas. Setea OrdConCue con la cuenta 
	 * @return
	 * @throws Exception
	 */
	public PeriodoOrdenVO toVO4Print() throws Exception {
	
		PeriodoOrdenVO periodoOrdenVO = (PeriodoOrdenVO) this.toVO(0, false);
		
		if(ordConCue!=null){
			periodoOrdenVO.setOrdConCue((OrdConCueVO) ordConCue.toVO(0, false));
			periodoOrdenVO.getOrdConCue().setCuenta(ordConCue.getCuenta().toVOWithRecurso());
		}
		
		return periodoOrdenVO;
	}
	
	
	// <#MetodosBeanDetalle#>
}
