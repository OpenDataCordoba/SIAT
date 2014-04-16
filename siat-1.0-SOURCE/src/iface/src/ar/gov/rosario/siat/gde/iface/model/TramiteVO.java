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
 * Value Object del Tramite
 * @author tecso
 *
 */
public class TramiteVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tramiteVO";
	
	private Integer nroTramite;	
	private TipoTramiteVO tipoTramite= new TipoTramiteVO();	
	private Integer nroRecibo;	
	private Integer anioRecibo;	
	private CuentaVO cuenta = new CuentaVO();	
	private Date fechaAlta;	
	private Date fechaImpresion;	
	private String nroLiquidacion;	
	private String observacion;
	private String codRefPag;
	
	// Buss Flags
	
	
	// View Constants
	
	
	private String anioReciboView = "";
	private String fechaAltaView = "";
	private String fechaImpresionView = "";
	private String nroReciboView = "";
	private String nroTramiteView = "";


	// Constructores
	public TramiteVO() {
		super();
	}

	
	// Getters y Setters
	
	public Integer getAnioRecibo() {
		return anioRecibo;
	}
	public void setAnioRecibo(Integer anioRecibo) {
		this.anioRecibo = anioRecibo;
		this.anioReciboView = StringUtil.formatInteger(anioRecibo);
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
		this.fechaAltaView = DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaImpresion() {
		return fechaImpresion;
	}
	public void setFechaImpresion(Date fechaImpresion) {
		this.fechaImpresion = fechaImpresion;
		this.fechaImpresionView = DateUtil.formatDate(fechaImpresion, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getNroLiquidacion() {
		return nroLiquidacion;
	}
	public void setNroLiquidacion(String nroLiquidacion) {
		this.nroLiquidacion = nroLiquidacion;
	}

	public Integer getNroRecibo() {
		return nroRecibo;
	}
	public void setNroRecibo(Integer nroRecibo) {
		this.nroRecibo = nroRecibo;
		this.nroReciboView = StringUtil.formatInteger(nroRecibo);
	}

	public Integer getNroTramite() {
		return nroTramite;
	}
	public void setNroTramite(Integer nroTramite) {
		this.nroTramite = nroTramite;
		this.nroTramiteView = StringUtil.formatInteger(nroTramite);
	}

	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public TipoTramiteVO getTipoTramite() {
		return tipoTramite;
	}
	public void setTipoTramite(TipoTramiteVO tipoTramite) {
		this.tipoTramite = tipoTramite;
	}
	



	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public void setAnioReciboView(String anioReciboView) {
		this.anioReciboView = anioReciboView;
	}
	public String getAnioReciboView() {
		return anioReciboView;
	}

	public void setFechaAltaView(String fechaAltaView) {
		this.fechaAltaView = fechaAltaView;
	}
	public String getFechaAltaView() {
		return fechaAltaView;
	}

	public void setFechaImpresionView(String fechaImpresionView) {
		this.fechaImpresionView = fechaImpresionView;
	}
	public String getFechaImpresionView() {
		return fechaImpresionView;
	}

	public void setNroReciboView(String nroReciboView) {
		this.nroReciboView = nroReciboView;
	}
	public String getNroReciboView() {
		return nroReciboView;
	}

	public void setNroTramiteView(String nroTramiteView) {
		this.nroTramiteView = nroTramiteView;
	}
	public String getNroTramiteView() {
		return nroTramiteView;
	}
	
	
	public String getNroReciboForCaratula(){
		Long numero = 0L, anio = 0L;
		String sCodRefPag = this.getCodRefPag();
		if (sCodRefPag.indexOf("/") >= 0) {
			try { numero = Long.valueOf(sCodRefPag.split("/")[0]); } catch (Exception e) {}
			try { anio = Long.valueOf(sCodRefPag.split("/")[1]); } catch (Exception e) {}
		} else {
			try { numero = Long.valueOf(sCodRefPag); } catch (Exception e) {}
		}

		return numero + " - " + anio;
	}

	public void setCodRefPag(String codRefPag) {
		this.codRefPag = codRefPag;
	}

	public String getCodRefPag() {
		return codRefPag;
	}
}
