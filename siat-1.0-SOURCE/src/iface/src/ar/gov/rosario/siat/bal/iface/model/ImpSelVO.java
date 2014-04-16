//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del SelladoImporte
 * @author tecso
 *
 */
public class ImpSelVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "impSelVO";
	
	private SelladoVO sellado = new SelladoVO();
	private TipoSelladoVO tipoSellado = new TipoSelladoVO();
	
	private Double costo;
	private Date fechaDesde;
	private Date fechaHasta;
	
	// Buss Flags
	
	
	// View Constants
	
	
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private String costoView = "";


	// Constructores
	public ImpSelVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ImpSelVO(int id, String desc) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, "dd/MM/yyyy");
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, "dd/MM/yyyy");
	}

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
		this.costoView = StringUtil.formatDouble(costo);
	}

	public SelladoVO getSellado() {
		return sellado;
	}

	public void setSellado(SelladoVO sellado) {
		this.sellado = sellado;
	}
	
	public TipoSelladoVO getTipoSellado() {
		return tipoSellado;
	}

	public void setTipoSellado(TipoSelladoVO tipoSellado) {
		this.tipoSellado = tipoSellado;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}

	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}

	public void setCostoView(String costoView) {
		this.costoView = costoView;
	}
	public String getCostoView() {
		return costoView;
	}

}
