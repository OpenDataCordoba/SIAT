//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del ActaInv
 * @author tecso
 *
 */
public class ActaInvVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "actaInvVO";
	
	private Integer numeroActa;

	private Integer anioActa;

	private InvestigadorVO investigador = new InvestigadorVO();

	private EstadoActaVO estadoActa = new EstadoActaVO();

	private String observacion;

	private String cuit;

	private String otrosDatos;

	private Date fechaInicio;

	private Date fechaFin;

	private String habNom;

	private String nroCuenta;

	private String nroFicha;

	private String nroIsib;

	private Date fecIniAct;

	private Integer perEnRelDep;

	private String actDes;

	private Integer locRosario;

	private Integer locOtrPro;

	private Integer pubRod;

	private Integer locFueRosEnSFe;

	private String ubicacionLocales;

	private Integer cartelesPubl;

	private String ticFacNom;

	private String copiaContrato;

	private String terceros;
	
	
	
	private String fechaInicioView="";
	private String fechaFinView="";
	private String fecIniActView="";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public ActaInvVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ActaInvVO(int id, String desc) {
		super();
		setId(new Long(id));		
	}

	// Getters y Setters
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

	public InvestigadorVO getInvestigador() {
		return investigador;
	}

	public void setInvestigador(InvestigadorVO investigador) {
		this.investigador = investigador;
	}

	public EstadoActaVO getEstadoActa() {
		return estadoActa;
	}

	public void setEstadoActa(EstadoActaVO estadoActa) {
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
		this.fechaInicioView = DateUtil.formatDate(fechaInicio, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
		this.fechaFinView = DateUtil.formatDate(fechaFin, DateUtil.ddSMMSYYYY_MASK);
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
		this.fecIniActView = DateUtil.formatDate(fecIniAct, DateUtil.ddSMMSYYYY_MASK);
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

	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public String getFechaInicioView() {
		return fechaInicioView;
	}
	
	public void setFechaInicioView(String fechaInicioView) {
		this.fechaInicioView = fechaInicioView;
	}
	
	public String getFechaFinView() {
		return fechaFinView;
	}
	
	public void setFechaFinView(String fechaFinView) {
		this.fechaFinView = fechaFinView;
	}

	public String getFecIniActView() {
		return fecIniActView;
	}

	public void setFecIniActView(String fecIniActView) {
		this.fecIniActView = fecIniActView;
	}
	
	
}
