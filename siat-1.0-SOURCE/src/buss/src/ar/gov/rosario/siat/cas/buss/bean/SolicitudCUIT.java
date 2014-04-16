//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.cas.buss.dao.CasDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a SolicitudCUIT
 * 
 * @author tecso
 */
@Entity
@Table(name = "cas_solicitudcuit")
public class SolicitudCUIT extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idsolicitud")
	private Solicitud solicitud;

	@Column(name = "idPersona")
	private Long idPersona;

	@Column(name = "tipoModificacion")
	private Integer tipoModificacion;

	@Column(name = "tipoPersona")
	private Integer tipoPersona;

	@Column(name = "apellidoReportado")
	private String apellidoReportado;

	@Column(name = "nombreReportado")
	private String nombreReportado;

	@Column(name = "razonSocReportado")
	private String razonSocReportado;

	@Column(name = "tipoDocReportado")
	private String tipoDocReportado;

	@Column(name = "docReportado")
	private Long docReportado;

	@Column(name = "cuit01aprobado")
	private Integer cuit01Aprobado;

	@Column(name = "cuit02Aprobado")
	private Long cuit02Aprobado;

	@Column(name = "cuit03Aprobado")
	private Integer cuit03Aprobado;

	@Column(name = "apellidoAprobado")
	private String apellidoAprobado;

	@Column(name = "nombreAprobado")
	private String nombreAprobado;

	@Column(name = "razonSocAprobado")
	private String razonSocAprobado;

	@Column(name = "datosRegPadron")
	private String datosRegPadron;
	// <#Propiedades#>

	// Constructores
	public SolicitudCUIT() {
		super();
		// Seteo de valores default
	}

	public SolicitudCUIT(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static SolicitudCUIT getById(Long id) {
		return (SolicitudCUIT) CasDAOFactory.getSolicitudCUITDAO().getById(id);
	}

	public static SolicitudCUIT getByIdNull(Long id) {
		return (SolicitudCUIT) CasDAOFactory.getSolicitudCUITDAO().getByIdNull(
				id);
	}

	public static List<SolicitudCUIT> getList() {
		return (List<SolicitudCUIT>) CasDAOFactory.getSolicitudCUITDAO()
				.getList();
	}

	public static List<SolicitudCUIT> getListActivos() {
		return (List<SolicitudCUIT>) CasDAOFactory.getSolicitudCUITDAO()
				.getListActiva();
	}
	
	public static SolicitudCUIT getBySolicitud(Solicitud solicitud) {
		return CasDAOFactory.getSolicitudCUITDAO().getBySolicitud(solicitud);
	}

	// Getters y setters
	public Solicitud getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(Solicitud solicitud) {
		this.solicitud = solicitud;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public Integer getTipoModificacion() {
		return tipoModificacion;
	}

	public void setTipoModificacion(Integer tipoModificacion) {
		this.tipoModificacion = tipoModificacion;
	}

	public Integer getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(Integer tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getApellidoReportado() {
		return apellidoReportado;
	}

	public void setApellidoReportado(String apellidoReportado) {
		this.apellidoReportado = apellidoReportado;
	}

	public String getNombreReportado() {
		return nombreReportado;
	}

	public void setNombreReportado(String nombreReportado) {
		this.nombreReportado = nombreReportado;
	}

	public String getRazonSocReportado() {
		return razonSocReportado;
	}

	public void setRazonSocReportado(String razonSocReportado) {
		this.razonSocReportado = razonSocReportado;
	}

	public String getTipoDocReportado() {
		return tipoDocReportado;
	}

	public void setTipoDocReportado(String tipoDocReportado) {
		this.tipoDocReportado = tipoDocReportado;
	}

	public Long getDocReportado() {
		return docReportado;
	}

	public void setDocReportado(Long docReportado) {
		this.docReportado = docReportado;
	}

	public Integer getCuit01Aprobado() {
		return cuit01Aprobado;
	}

	public void setCuit01Aprobado(Integer cuit01aprobado) {
		this.cuit01Aprobado = cuit01aprobado;
	}

	public Long getCuit02Aprobado() {
		return cuit02Aprobado;
	}

	public void setCuit02Aprobado(Long cuit02Aprobado) {
		this.cuit02Aprobado = cuit02Aprobado;
	}

	public Integer getCuit03Aprobado() {
		return cuit03Aprobado;
	}

	public void setCuit03Aprobado(Integer cuit03Aprobado) {
		this.cuit03Aprobado = cuit03Aprobado;
	}

	public String getApellidoAprobado() {
		return apellidoAprobado;
	}

	public void setApellidoAprobado(String apellidoAprobado) {
		this.apellidoAprobado = apellidoAprobado;
	}

	public String getNombreAprobado() {
		return nombreAprobado;
	}

	public void setNombreAprobado(String nombreAprobado) {
		this.nombreAprobado = nombreAprobado;
	}

	public String getRazonSocAprobado() {
		return razonSocAprobado;
	}

	public void setRazonSocAprobado(String razonSocAprobado) {
		this.razonSocAprobado = razonSocAprobado;
	}

	public String getDatosRegPadron() {
		return datosRegPadron;
	}

	public void setDatosRegPadron(String datosRegPadron) {
		this.datosRegPadron = datosRegPadron;
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
/*		if (StringUtil.isNullOrEmpty(getCodSolicitudCUIT())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					CasError.SOLICITUDCUIT_CODSOLICITUDCUIT);
		}

		if (StringUtil.isNullOrEmpty(getDesSolicitudCUIT())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					CasError.SOLICITUDCUIT_DESSOLICITUDCUIT);
		}

		if (hasError()) {
			return false;
		}

		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codSolicitudCUIT");
		if (!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO,
					CasError.SOLICITUDCUIT_CODSOLICITUDCUIT);
		}
*/
		return true;
	}


	// Metodos de negocio


}
