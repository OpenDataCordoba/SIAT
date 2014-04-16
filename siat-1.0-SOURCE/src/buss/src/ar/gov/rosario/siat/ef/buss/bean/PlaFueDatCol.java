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
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatColVO;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a PlaFueDatCol
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_plaFueDatCol")
public class PlaFueDatCol extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPlaFueDat")
	private PlaFueDat plaFueDat;

	@Column(name = "colName")
	private String colName;

	@Column(name = "nroColumna")
	private Integer nroColumna;

	@Column(name = "orden")
	private Integer orden;

	@Column(name = "oculta")
	private Integer oculta;

	@Column(name = "sumaEnTotal")
	private Integer sumaEnTotal;

	// <#Propiedades#>

	// Constructores
	public PlaFueDatCol() {
		super();
		// Seteo de valores default
	}

	public PlaFueDatCol(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static PlaFueDatCol getById(Long id) {
		return (PlaFueDatCol) EfDAOFactory.getPlaFueDatColDAO().getById(id);
	}

	public static PlaFueDatCol getByIdNull(Long id) {
		return (PlaFueDatCol) EfDAOFactory.getPlaFueDatColDAO().getByIdNull(id);
	}

	public static List<PlaFueDatCol> getList() {
		return (ArrayList<PlaFueDatCol>) EfDAOFactory.getPlaFueDatColDAO()
				.getList();
	}

	public static List<PlaFueDatCol> getListActivos() {
		return (ArrayList<PlaFueDatCol>) EfDAOFactory.getPlaFueDatColDAO()
				.getListActiva();
	}

	// Getters y setters
	public PlaFueDat getPlaFueDat() {
		return plaFueDat;
	}

	public void setPlaFueDat(PlaFueDat plaFueDat) {
		this.plaFueDat = plaFueDat;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public Integer getNroColumna() {
		return nroColumna;
	}

	public void setNroColumna(Integer nroColumna) {
		this.nroColumna = nroColumna;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public Integer getOculta() {
		return oculta;
	}

	public void setOculta(Integer oculta) {
		this.oculta = oculta;
	}

	public Integer getSumaEnTotal() {
		return sumaEnTotal;
	}

	public void setSumaEnTotal(Integer sumaEnTotal) {
		this.sumaEnTotal = sumaEnTotal;
	}

	// Validaciones
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio
		
		if(hasError())
			return false;
		
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
		if (plaFueDat == null || plaFueDat.getId() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					EfError.PLAFUEDAT_LABEL);
		}

		if(StringUtil.isNullOrEmpty(colName)){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.PLAFUEDATCOL_COLNAME_LABEL);			
		}

		if (nroColumna == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					EfError.PLAFUEDATCOL_NROCOLUMNA_LABEL);
		}

		if(orden==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.PLAFUEDATCOL_ORDEN_LABEL);
		}
			

		if (sumaEnTotal == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,	EfError.PLAFUEDATCOL_SUMAENTOTAL_LABEL);
		}

		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el PlaFueDatCol. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getPlaFueDatColDAO().update(this);
	}

	/**
	 * Desactiva el PlaFueDatCol. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getPlaFueDatColDAO().update(this);
	}

	/**
	 * Valida la activacion del PlaFueDatCol
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
	 * Valida la desactivacion del PlaFueDatCol
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
	public PlaFueDatColVO toVO4Print()throws Exception{
		PlaFueDatColVO plaFueDatColVO = (PlaFueDatColVO) this.toVO(0, false);
		plaFueDatColVO.setOcultaView(this.oculta);

		return plaFueDatColVO;
	}


}
