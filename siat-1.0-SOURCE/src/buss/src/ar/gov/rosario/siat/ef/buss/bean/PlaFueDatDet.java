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
import javax.persistence.Transient;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatDetVO;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a PlaFueDatDet
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_plaFueDatDet")
public class PlaFueDatDet extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPlaFueDat")
	private PlaFueDat plaFueDat;

	@Column(name = "periodo")
	private Integer periodo;

	@Column(name = "anio")
	private Integer anio;

	@Column(name = "col1")
	private Double col1;

	@Column(name = "col2")
	private Double col2;

	@Column(name = "col3")
	private Double col3;

	@Column(name = "col4")
	private Double col4;

	@Column(name = "col5")
	private Double col5;

	@Column(name = "col6")
	private Double col6;

	@Column(name = "col7")
	private Double col7;

	@Column(name = "col8")
	private Double col8;

	@Column(name = "col9")
	private Double col9;

	@Column(name = "col10")
	private Double col10;

	@Column(name = "col11")
	private Double col11;

	@Column(name = "col12")
	private Double col12;

	@Transient
	private String periodoAnio="";
	// <#Propiedades#>

	// Constructores
	public PlaFueDatDet() {
		super();
		// Seteo de valores default
	}

	public PlaFueDatDet(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static PlaFueDatDet getById(Long id) {
		return (PlaFueDatDet) EfDAOFactory.getPlaFueDatDetDAO().getById(id);
	}

	public static PlaFueDatDet getByIdNull(Long id) {
		return (PlaFueDatDet) EfDAOFactory.getPlaFueDatDetDAO().getByIdNull(id);
	}

	public static List<PlaFueDatDet> getList() {
		return (ArrayList<PlaFueDatDet>) EfDAOFactory.getPlaFueDatDetDAO()
				.getList();
	}

	public static List<PlaFueDatDet> getListActivos() {
		return (ArrayList<PlaFueDatDet>) EfDAOFactory.getPlaFueDatDetDAO().getListActiva();
	}

	public static PlaFueDatDet getByPeriodoAnio(PlaFueDat plaFueDat, Integer periodo, Integer anio) {
		return EfDAOFactory.getPlaFueDatDetDAO().getByPeriodoAnio(plaFueDat, periodo, anio);
	}

	// Getters y setters
	public PlaFueDat getPlaFueDat() {
		return plaFueDat;
	}

	public void setPlaFueDat(PlaFueDat plaFueDat) {
		this.plaFueDat = plaFueDat;
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
	
	public void setPeriodoAnio(String periodoAnio) {
		this.periodoAnio = periodoAnio;
	}

	public String getPeriodoAnio() {
		periodoAnio=this.periodo.toString()+"/"+this.anio.toString();
		return periodoAnio;
	}
	
	public Double getCol1() {
		return col1;
	}

	public void setCol1(Double col1) {
		this.col1 = col1;
	}

	public Double getCol2() {
		return col2;
	}

	public void setCol2(Double col2) {
		this.col2 = col2;
	}

	public Double getCol3() {
		return col3;
	}

	public void setCol3(Double col3) {
		this.col3 = col3;
	}

	public Double getCol4() {
		return col4;
	}

	public void setCol4(Double col4) {
		this.col4 = col4;
	}

	public Double getCol5() {
		return col5;
	}

	public void setCol5(Double col5) {
		this.col5 = col5;
	}

	public Double getCol6() {
		return col6;
	}

	public void setCol6(Double col6) {
		this.col6 = col6;
	}

	public Double getCol7() {
		return col7;
	}

	public void setCol7(Double col7) {
		this.col7 = col7;
	}

	public Double getCol8() {
		return col8;
	}

	public void setCol8(Double col8) {
		this.col8 = col8;
	}

	public Double getCol9() {
		return col9;
	}

	public void setCol9(Double col9) {
		this.col9 = col9;
	}

	public Double getCol10() {
		return col10;
	}

	public void setCol10(Double col10) {
		this.col10 = col10;
	}

	public Double getCol11() {
		return col11;
	}

	public void setCol11(Double col11) {
		this.col11 = col11;
	}

	public Double getCol12() {
		return col12;
	}

	public void setCol12(Double col12) {
		this.col12 = col12;
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
		if (plaFueDat == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					EfError.PLAFUEDAT_LABEL);
		}

		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el PlaFueDatDet. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getPlaFueDatDetDAO().update(this);
	}

	/**
	 * Desactiva el PlaFueDatDet. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getPlaFueDatDetDAO().update(this);
	}

	/**
	 * Valida la activacion del PlaFueDatDet
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
	 * Valida la desactivacion del PlaFueDatDet
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	public PlaFueDatDetVO toVO4Print()throws Exception{
		PlaFueDatDetVO plaFueDatDetVO = (PlaFueDatDetVO) this.toVO(0, false);
	
		return plaFueDatDetVO;
	}
}
