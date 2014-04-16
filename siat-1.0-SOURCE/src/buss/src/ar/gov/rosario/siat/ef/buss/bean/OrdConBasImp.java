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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.CompFuenteVO;
import ar.gov.rosario.siat.ef.iface.model.OrdConBasImpVO;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a ordConBasImp
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_ordConBasImp")
public class OrdConBasImp extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdenControl")
	private OrdenControl ordenControl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcompFuente")
	private CompFuente compFuente;

	@Column(name = "periodoDesde")
	private Integer periodoDesde;

	@Column(name = "anioDesde")
	private Integer anioDesde;

	@Column(name = "periodoHasta")
	private Integer periodoHasta;

	@Column(name = "anioHasta")
	private Integer anioHasta;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idOrdConCue")
	private OrdConCue ordConCue;

	// <#Propiedades#>

	// Constructores
	public OrdConBasImp() {
		super();
		// Seteo de valores default
	}

	public OrdConBasImp(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static OrdConBasImp getById(Long id) {
		return (OrdConBasImp) EfDAOFactory.getOrdConBasImpDAO().getById(id);
	}

	public static OrdConBasImp getByIdNull(Long id) {
		return (OrdConBasImp) EfDAOFactory.getOrdConBasImpDAO().getByIdNull(id);
	}

	public static List<OrdConBasImp> getList() {
		return (ArrayList<OrdConBasImp>) EfDAOFactory.getOrdConBasImpDAO()
				.getList();
	}

	public static List<OrdConBasImp> getListActivos() {
		return (ArrayList<OrdConBasImp>) EfDAOFactory.getOrdConBasImpDAO()
				.getListActiva();
	}
	


	// Getters y setters
	public OrdenControl getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControl ordenControl) {
		this.ordenControl = ordenControl;
	}

	public CompFuente getCompFuente() {
		return compFuente;
	}

	public void setCompFuente(CompFuente compFuente) {
		this.compFuente = compFuente;
	}

	public Integer getPeriodoDesde() {
		return periodoDesde;
	}

	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
	}

	public Integer getAnioDesde() {
		return anioDesde;
	}

	public void setAnioDesde(Integer anioDesde) {
		this.anioDesde = anioDesde;
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
	}

	public Integer getAnioHasta() {
		return anioHasta;
	}

	public void setAnioHasta(Integer anioHasta) {
		this.anioHasta = anioHasta;
	}

	public OrdConCue getOrdConCue() {
		return ordConCue;
	}

	public void setOrdConCue(OrdConCue ordConCue) {
		this.ordConCue = ordConCue;
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

/*		// <#ValidateDelete#>
		if (GenericDAO.hasReference(this, ${BeanRelacionado}.class, "${bean}")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							${Modulo}Error.${BEAN}_LABEL, ${Modulo}Error.${BEAN_RELACIONADO}_LABEL );
		}
		*/
		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones
		if (ordenControl == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					EfError.ORDENCONTROL_LABEL);
		}

		if (periodoDesde == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					EfError.ORDCONBASIMP_PERIODODESDE_LABEL);
		}

		if (periodoHasta == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					EfError.ORDCONBASIMP_PERIODOHASTA_LABEL);
		}

		if (anioDesde == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					EfError.ORDCONBASIMP_ANIODESDE_LABEL);
		}

		if (anioHasta == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					EfError.ORDCONBASIMP_ANIOHASTA_LABEL);
		}

		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el ordConBasImp. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getOrdConBasImpDAO().update(this);
	}

	/**
	 * Desactiva el ordConBasImp. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getOrdConBasImpDAO().update(this);
	}

	/**
	 * Valida la activacion del ordConBasImp
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
	 * Valida la desactivacion del ordConBasImp
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	public OrdConBasImpVO toVO4Print()throws Exception{
		OrdConBasImpVO ordConBasImpVO = (OrdConBasImpVO) this.toVO(0, false);
		ordConBasImpVO.setCompFuente((CompFuenteVO) this.compFuente.toVO4Print());
	    
		return ordConBasImpVO;
	}
}
