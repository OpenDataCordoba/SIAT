//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

public class PrecioEventoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private HabilitacionVO habilitacion = new HabilitacionVO();
	private TipoEntradaVO tipoEntrada = new TipoEntradaVO();
	private Double precioPublico;
	private Double precio;
	
	private String precioPublicoView = "";
	private String precioView = "";
	
	//Constructores 
	public PrecioEventoVO(){
		super();
	}
	
	public PrecioEventoVO(int id, String descripcion){
		super();
		setId(new Long(id));
		tipoEntrada.setDescripcion(descripcion);
	}

	// Getters & Setters
	public HabilitacionVO getHabilitacion() {
		return habilitacion;
	}

	public void setHabilitacion(HabilitacionVO habilitacion) {
		this.habilitacion = habilitacion;
	}

	public TipoEntradaVO getTipoEntrada() {
		return tipoEntrada;
	}
	
	public void setTipoEntrada(TipoEntradaVO tipoEntrada) {
		this.tipoEntrada = tipoEntrada;
	}
	
	public Double getPrecioPublico() {
		return precioPublico;
	}

	public void setPrecioPublico(Double precioPublico) {
		this.precioPublico = precioPublico;
		this.precioPublicoView = StringUtil.formatDouble(precioPublico);
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
		this.precioView = StringUtil.formatDouble(precio);
	}

	public String getPrecioPublicoView() {
		return precioPublicoView;
	}

	public void setPrecioPublicoView(String precioPublicoView) {
		this.precioPublicoView = precioPublicoView;
	}

	public String getPrecioView() {
		return precioView;
	}

	public void setPrecioView(String precioView) {
		this.precioView = precioView;
	}

	public String getDescripcion(){
		String descripcion = tipoEntrada.getDescripcion();
		if(tipoEntrada != null && tipoEntrada.getDescripcion() != null && precioPublico != null){
			descripcion = tipoEntrada.getDescripcion()+" - $" + precioPublico.toString();
		}
		return descripcion;
	}
	
}
