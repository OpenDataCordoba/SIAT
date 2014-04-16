//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.frm.buss.dao.FrmDAOFactory;
import ar.gov.rosario.siat.frm.iface.util.FrmError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a ForCam
 * Corresponde a un campo de un formulario 
 * @author tecso
 */
@Entity
@Table(name = "for_forcam")
public class ForCam extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "codCampo")
	private String codForCam;

	@Column(name = "desCampo")
	private String desForCam;

	@Column(name = "largoMax")
	private Integer largoMax;

	@Column(name = "valorDefecto")
	private String valorDefecto;

	@Column(name = "fechaDesde")
	private Date fechaDesde;

	@Column(name = "fechaHasta")
	private Date fechaHasta;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idFormulario")
	private Formulario formulario;

	// <#Propiedades#>

	// Constructores
	public ForCam() {
		super();
		// Seteo de valores default
	}

	public ForCam(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static ForCam getById(Long id) {
		return (ForCam) FrmDAOFactory.getForCamDAO().getById(id);
	}

	public static ForCam getByIdNull(Long id) {
		return (ForCam) FrmDAOFactory.getForCamDAO().getByIdNull(id);
	}

	public static List<ForCam> getList() {
		return (ArrayList<ForCam>) FrmDAOFactory.getForCamDAO().getList();
	}

	public static List<ForCam> getListActivos() {
		return (ArrayList<ForCam>) FrmDAOFactory.getForCamDAO().getListActiva();
	}

	// Getters y setters

	public String getCodForCam() {
		return codForCam;
	}

	public void setCodForCam(String codForCam) {
		this.codForCam = codForCam;
	}

	public String getDesForCam() {
		return desForCam;
	}

	public void setDesForCam(String desForCam) {
		this.desForCam = desForCam;
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

	public Formulario getFormulario() {
		return formulario;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}

	public String getValorDefecto() {
		return valorDefecto;
	}

	public void setValorDefecto(String valorDefecto) {
		this.valorDefecto = valorDefecto;
	}

	public Integer getLargoMax() {
		return largoMax;
	}

	public void setLargoMax(Integer largoMax) {
		this.largoMax = largoMax;
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
		if (StringUtil.isNullOrEmpty(getCodForCam())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					FrmError.FORCAM_CODFORCAM);
		}

		if (StringUtil.isNullOrEmpty(getDesForCam())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					FrmError.FORCAM_DESFORCAM);
		}
		
		if (getFechaDesde()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					BalError.IMPSEL_FECHADESDE_LABEL);
		}
		if(getFechaDesde()!=null && getFechaHasta()!=null && getFechaDesde().after(getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE,FrmError.FORCAM_FECHAHASTA_LABEL, FrmError.FORCAM_FECHADESDE_LABEL);
		}
				
		if (hasError()) {
			return false;
		}

		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codForCam");
		uniqueMap.addEntity("formulario");
		if (!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO,
					FrmError.FORCAM_CODFORCAM);
		}

		return true;
	}

	// Metodos de negocio

	/*private static boolean existe(Long idForm, Long idForCamActual, Date fechaDesde, Date fechaHasta) {
		return FrmDAOFactory.getForCamDAO().existe(idForm, idForCamActual, fechaDesde, fechaHasta);
	}*/

	/**
	 * Activa el ForCam. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		FrmDAOFactory.getForCamDAO().update(this);
	}

	/**
	 * Desactiva el ForCam. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		FrmDAOFactory.getForCamDAO().update(this);
	}

	/**
	 * Valida la activacion del ForCam
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
	 * Valida la desactivacion del ForCam
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
