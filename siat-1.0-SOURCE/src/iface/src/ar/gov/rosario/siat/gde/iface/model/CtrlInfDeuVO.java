//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del CtrlInfDeu
 * @author tecso
 *
 */
public class CtrlInfDeuVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "ctrlInfDeuVOVO";
	
	private Integer nroTramite;
	private Long nroRecibo;
	private Long anioRecibo;
	private String codId = "";
	private CuentaVO cuenta = new CuentaVO();
	private Date fechaHoraGen;	// Generacion
	private Date fechaHoraImp;  // Impresion
	private String nroLiquidacion = "";
	private String observacion = "";

	private String nroTramiteView = "";
	private String nroReciboView = "";
	private String anioReciboView = "";
	private String fechaHoraGenView = "";
	private String fechaHoraImpView = "";
	
	// Constructores
	public CtrlInfDeuVO() {
		super();
	}

	// Getters y Setters
	public Long getAnioRecibo() {
		return anioRecibo;
	}
	public void setAnioRecibo(Long anioRecibo) {
		this.anioRecibo = anioRecibo;
		this.anioReciboView = StringUtil.formatLong(anioRecibo);
	}
	public String getAnioReciboView() {
		return anioReciboView;
	}
	public void setAnioReciboView(String anioReciboView) {
		this.anioReciboView = anioReciboView;
	}
	public String getCodId() {
		return codId;
	}
	public void setCodId(String codId) {
		this.codId = codId;
	}
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	public Date getFechaHoraGen() {
		return fechaHoraGen;
	}
	public void setFechaHoraGen(Date fechaHoraGen) {
		this.fechaHoraGen = fechaHoraGen;
		this.fechaHoraGenView = DateUtil.formatDate(fechaHoraGen, DateUtil.ddSMMSYYYY_HH_MM_MASK);
	}
	public String getFechaHoraGenView() {
		return fechaHoraGenView;
	}
	public void setFechaHoraGenView(String fechaHoraGenView) {
		this.fechaHoraGenView = fechaHoraGenView;
	}
	public Date getFechaHoraImp() {
		return fechaHoraImp;
	}
	public void setFechaHoraImp(Date fechaHoraImp) {
		this.fechaHoraImp = fechaHoraImp;
		this.fechaHoraImpView = DateUtil.formatDate(fechaHoraImp, DateUtil.ddSMMSYYYY_HH_MM_MASK);
	}
	public String getFechaHoraImpView() {
		return fechaHoraImpView;
	}
	public void setFechaHoraImpView(String fechaHoraImpView) {
		this.fechaHoraImpView = fechaHoraImpView;
	}
	public String getNroLiquidacion() {
		return nroLiquidacion;
	}
	public void setNroLiquidacion(String nroLiquidacion) {
		this.nroLiquidacion = nroLiquidacion;
	}
	public Long getNroRecibo() {
		return nroRecibo;
	}
	public void setNroRecibo(Long nroRecibo) {
		this.nroRecibo = nroRecibo;
		this.nroReciboView = StringUtil.formatLong(nroRecibo);
	}
	public String getNroReciboView() {
		return nroReciboView;
	}
	public void setNroReciboView(String nroReciboView) {
		this.nroReciboView = nroReciboView;
	}
	public Integer getNroTramite() {
		return nroTramite;
	}
	public void setNroTramite(Integer nroTramite) {
		this.nroTramite = nroTramite;
		this.nroTramiteView = StringUtil.formatInteger(nroTramite);
	}
	public String getNroTramiteView() {
		return nroTramiteView;
	}
	public void setNroTramiteView(String nroTramiteView) {
		this.nroTramiteView = nroTramiteView;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	


}
