//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import coop.tecso.demoda.iface.helper.DateUtil;

public class ConstanciaDeudaAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	private Date fechaConfeccion;
	private Date fechaAuto;
	
	private String fechaConfeccionView = "";
	private String fechaAutoView = "";
	
	private List<LiqDeudaAdapter> listLiqDeudaAdapter = new ArrayList<LiqDeudaAdapter>();
	
	
	public ConstanciaDeudaAdapter(String sweActionName) {
		super(sweActionName);		
	}

	// Contructores
	public ConstanciaDeudaAdapter() {
		super();
	}

	// Getters y Setter
	public Date getFechaConfeccion() {
		return fechaConfeccion;
	}

	public void setFechaConfeccion(Date fechaConfeccion) {
		this.fechaConfeccion = fechaConfeccion;
		this.fechaConfeccionView = DateUtil.formatDate(fechaConfeccion, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaAuto() {
		return fechaAuto;
	}
	public void setFechaAuto(Date fechaAuto) {
		this.fechaAuto = fechaAuto;
		this.fechaAutoView = DateUtil.formatDate(fechaAuto, DateUtil.ddSMMSYYYY_MASK);
	}

	public List<LiqDeudaAdapter> getListLiqDeudaAdapter() {
		return listLiqDeudaAdapter;
	}
	public void setListLiqDeudaAdapter(List<LiqDeudaAdapter> listLiqDeudaAdapter) {
		this.listLiqDeudaAdapter = listLiqDeudaAdapter;
	}

	// view getters
	public String getFechaConfeccionView() {
		return this.fechaConfeccionView;
	}
	public void setFechaConfeccionView(String fechaConfeccionView) {
		this.fechaConfeccionView = fechaConfeccionView;
	}

	public String getFechaAutoView() {
		return fechaAutoView;
	}
	public void setFechaAutoView(String fechaAutoView) {
		this.fechaAutoView = fechaAutoView;
	}
	
}
