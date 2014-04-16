//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a LiqComProDeu
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_liqcomprodeu")
public class LiqComProDeu extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idLiqComPro")
	private LiqComPro liqComPro;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idProRecCom")
	private ProRecCom proRecCom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idConDeuCuo")
	private ConDeuCuo conDeuCuo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idReciboDeuda")
	private ReciboDeuda reciboDeuda;

	@Column(name = "importeAplicado")
	private Double importeAplicado;

	@Column(name = "importeComision")
	private Double importeComision;

	// <#Propiedades#>

	// Constructores
	public LiqComProDeu() {
		super();
		// Seteo de valores default
	}

	public LiqComProDeu(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static LiqComProDeu getById(Long id) {
		return (LiqComProDeu) GdeDAOFactory.getLiqComProDeuDAO().getById(id);
	}

	public static LiqComProDeu getByIdNull(Long id) {
		return (LiqComProDeu) GdeDAOFactory.getLiqComProDeuDAO()
				.getByIdNull(id);
	}

	public static List<LiqComProDeu> getList() {
		return (ArrayList<LiqComProDeu>) GdeDAOFactory.getLiqComProDeuDAO()
				.getList();
	}

	public static List<LiqComProDeu> getListActivos() {
		return (ArrayList<LiqComProDeu>) GdeDAOFactory.getLiqComProDeuDAO()
				.getListActiva();
	}

	// Getters y setters

	public LiqComPro getLiqComPro() {
		return liqComPro;
	}

	public void setLiqComPro(LiqComPro liqComPro) {
		this.liqComPro = liqComPro;
	}

	public ProRecCom getProRecCom() {
		return proRecCom;
	}

	public void setProRecCom(ProRecCom proRecCom) {
		this.proRecCom = proRecCom;
	}

	public ConDeuCuo getConDeuCuo() {
		return conDeuCuo;
	}

	public void setConDeuCuo(ConDeuCuo conDeuCuo) {
		this.conDeuCuo = conDeuCuo;
	}

	public ReciboDeuda getReciboDeuda() {
		return reciboDeuda;
	}

	public void setReciboDeuda(ReciboDeuda reciboDeuda) {
		this.reciboDeuda = reciboDeuda;
	}

	public Double getImporteAplicado() {
		return importeAplicado;
	}

	public void setImporteAplicado(Double importeAplicado) {
		this.importeAplicado = importeAplicado;
	}

	public Double getImporteComision() {
		return importeComision;
	}

	public void setImporteComision(Double importeComision) {
		this.importeComision = importeComision;
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
/*
		// Validaciones
		if (StringUtil.isNullOrEmpty(getCodLiqComProDeu())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					GdeError.LIQCOMPRODEU_CODLIQCOMPRODEU);
		}

		if (StringUtil.isNullOrEmpty(getDesLiqComProDeu())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					GdeError.LIQCOMPRODEU_DESLIQCOMPRODEU);
		}

		if (hasError()) {
			return false;
		}

		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codLiqComProDeu");
		if (!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO,
					GdeError.LIQCOMPRODEU_CODLIQCOMPRODEU);
		}
*/
		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el LiqComProDeu. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getLiqComProDeuDAO().update(this);
	}

	/**
	 * Desactiva el LiqComProDeu. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getLiqComProDeuDAO().update(this);
	}

	/**
	 * Valida la activacion del LiqComProDeu
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
	 * Valida la desactivacion del LiqComProDeu
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
