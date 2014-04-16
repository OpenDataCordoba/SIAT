//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * ${Bean}
 * @author tecso
 *
 */
public class DomAtrValVO extends SiatBussImageModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String NAME = "domAtrValVO";
	
	private String valor = "";
	private String desValor = "";  
	private DomAtrVO domAtr = new DomAtrVO();
	private Date fechaDesde;
	private Date fechaHasta;
	
	private String fechaDesdeView;
	private String fechaHastaView;

	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public DomAtrValVO() {
		super();
        // Acciones y Metodos para seguridad
        super.ACCION_VER 		    = "/seg/AdministrarDomAtrVal";
        super.ACCION_MODIFICAR 		= "/seg/AdministrarDomAtrVal";
        super.ACCION_ELIMINAR 		= "/seg/AdministrarDomAtrVal";
	}
	
	public DomAtrValVO(int id, String desValor) {
		super();
		setId(new Long(id));
		setDesValor(desValor);
	}
	
	// Getters y Setters
	public String getDesValor() {
		return desValor;
	}
	public void setDesValor(String desValor) {
		this.desValor = desValor;
	}
	public DomAtrVO getDomAtr() {
		return domAtr;
	}
	public void setDomAtr(DomAtrVO domAtr) {
		this.domAtr = domAtr;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}

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
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public String getStrId(){
		return this.getId().toString();	
	}
	
}
