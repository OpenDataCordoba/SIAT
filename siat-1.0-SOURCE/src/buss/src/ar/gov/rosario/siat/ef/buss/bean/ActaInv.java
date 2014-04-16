//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a ActaInv
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_actaInv")
public class ActaInv extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "numeroActa")
	private Integer numeroActa;

	@Column(name = "anioActa")
	private Integer anioActa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idInvestigador")
	private Investigador investigador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idestadoActa")
	private EstadoActa estadoActa;

	@Column(name = "observacion")
	private String observacion;

	@Column(name = "cuit")
	private String cuit;

	@Column(name = "otrosDatos")
	private String otrosDatos;

	@Column(name = "fechaInicio")
	private Date fechaInicio;

	@Column(name = "fechaFin")
	private Date fechaFin;

	@Column(name = "habNom")
	private String habNom;

	@Column(name = "nroCuenta")
	private String nroCuenta;

	@Column(name = "nroFicha")
	private String nroFicha;

	@Column(name = "nroIsib")
	private String nroIsib;

	@Column(name = "fecIniAct")
	private Date fecIniAct;

	@Column(name = "perEnRelDep")
	private Integer perEnRelDep;

	@Column(name = "actDes")
	private String actDes;

	@Column(name = "locRosario")
	private Integer locRosario;

	@Column(name = "locOtrPro")
	private Integer locOtrPro;

	@Column(name = "pubRod")
	private Integer pubRod;

	@Column(name = "locFueRosEnSFe")
	private Integer locFueRosEnSFe;

	@Column(name = "ubicacionLocales")
	private String ubicacionLocales;

	@Column(name = "cartelesPubl")
	private Integer cartelesPubl;

	@Column(name = "ticFacNom")
	private String ticFacNom;

	@Column(name = "copiaContrato")
	private String copiaContrato;

	@Column(name = "terceros")
	private String terceros;

	// <#Propiedades#>

	// Constructores
	public ActaInv() {
		super();
		// Seteo de valores default
	}

	public ActaInv(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static ActaInv getById(Long id) {
		return (ActaInv) EfDAOFactory.getActaInvDAO().getById(id);
	}

	public static ActaInv getByIdNull(Long id) {
		return (ActaInv) EfDAOFactory.getActaInvDAO().getByIdNull(id);
	}

	public static List<ActaInv> getList() {
		return (List<ActaInv>) EfDAOFactory.getActaInvDAO().getList();
	}

	public static List<ActaInv> getListActivos() {
		return (List<ActaInv>) EfDAOFactory.getActaInvDAO().getListActiva();
	}

	// Getters y setters
	public Integer getNumeroActa() {
		return numeroActa;
	}

	public void setNumeroActa(Integer numeroActa) {
		this.numeroActa = numeroActa;
	}

	public Integer getAnioActa() {
		return anioActa;
	}

	public void setAnioActa(Integer anioActa) {
		this.anioActa = anioActa;
	}

	public Investigador getInvestigador() {
		return investigador;
	}

	public void setInvestigador(Investigador investigador) {
		this.investigador = investigador;
	}

	public EstadoActa getEstadoActa() {
		return estadoActa;
	}

	public void setEstadoActa(EstadoActa estadoActa) {
		this.estadoActa = estadoActa;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getOtrosDatos() {
		return otrosDatos;
	}

	public void setOtrosDatos(String otrosDatos) {
		this.otrosDatos = otrosDatos;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getHabNom() {
		return habNom;
	}

	public void setHabNom(String habNom) {
		this.habNom = habNom;
	}

	public String getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public String getNroFicha() {
		return nroFicha;
	}

	public void setNroFicha(String nroFicha) {
		this.nroFicha = nroFicha;
	}

	public String getNroIsib() {
		return nroIsib;
	}

	public void setNroIsib(String nroIsib) {
		this.nroIsib = nroIsib;
	}

	public Date getFecIniAct() {
		return fecIniAct;
	}

	public void setFecIniAct(Date fecIniAct) {
		this.fecIniAct = fecIniAct;
	}

	public Integer getPerEnRelDep() {
		return perEnRelDep;
	}

	public void setPerEnRelDep(Integer perEnRelDep) {
		this.perEnRelDep = perEnRelDep;
	}

	public String getActDes() {
		return actDes;
	}

	public void setActDes(String actDes) {
		this.actDes = actDes;
	}

	public Integer getLocRosario() {
		return locRosario;
	}

	public void setLocRosario(Integer locRosario) {
		this.locRosario = locRosario;
	}

	public Integer getLocOtrPro() {
		return locOtrPro;
	}

	public void setLocOtrPro(Integer locOtrPro) {
		this.locOtrPro = locOtrPro;
	}

	public Integer getPubRod() {
		return pubRod;
	}

	public void setPubRod(Integer pubRod) {
		this.pubRod = pubRod;
	}

	public Integer getLocFueRosEnSFe() {
		return locFueRosEnSFe;
	}

	public void setLocFueRosEnSFe(Integer locFueRosEnSFe) {
		this.locFueRosEnSFe = locFueRosEnSFe;
	}

	public String getUbicacionLocales() {
		return ubicacionLocales;
	}

	public void setUbicacionLocales(String ubicacionLocales) {
		this.ubicacionLocales = ubicacionLocales;
	}

	public Integer getCartelesPubl() {
		return cartelesPubl;
	}

	public void setCartelesPubl(Integer cartelesPubl) {
		this.cartelesPubl = cartelesPubl;
	}

	public String getTicFacNom() {
		return ticFacNom;
	}

	public void setTicFacNom(String ticFacNom) {
		this.ticFacNom = ticFacNom;
	}

	public String getCopiaContrato() {
		return copiaContrato;
	}

	public void setCopiaContrato(String copiaContrato) {
		this.copiaContrato = copiaContrato;
	}

	public String getTerceros() {
		return terceros;
	}

	public void setTerceros(String terceros) {
		this.terceros = terceros;
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
	 * Activa el ActaInv. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getActaInvDAO().update(this);
	}

	/**
	 * Desactiva el ActaInv. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getActaInvDAO().update(this);
	}

	/**
	 * Valida la activacion del ActaInv
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
	 * Valida la desactivacion del ActaInv
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
