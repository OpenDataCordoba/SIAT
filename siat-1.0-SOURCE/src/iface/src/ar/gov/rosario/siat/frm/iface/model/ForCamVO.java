//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del ForCam
 * @author tecso
 *
 */
public class ForCamVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "forCamVO";
	
	private String codForCam = "";

	private String desForCam = "";
	
	private String valorDefecto = "";

	private Integer largoMax;
	
	private Date fechaDesde;

	private Date fechaHasta;
	
	private FormularioVO formulario;

	private String fechaDesdeView = "";
	
	private String fechaHastaView = "";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public ForCamVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ForCamVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesForCam(desc);
	}
	
	// Getters y Setters

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

	public String getValorDefecto() {
		return valorDefecto;
	}

	public void setValorDefecto(String valorDefecto) {
		this.valorDefecto = valorDefecto;
	}

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

	public FormularioVO getFormulario() {
		return formulario;
	}

	public void setFormulario(FormularioVO formulario) {
		this.formulario = formulario;
	}

	public Integer getLargoMax() {
		return largoMax;
	}
	
	public void setLargoMax(Integer largoMax) {
		this.largoMax = largoMax;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	

	// View getters
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}

	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}

	public String getFechaHastaView() {
		return fechaHastaView;
	}

	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}

	public String getLargoMaxView(){
		if(largoMax!=null)
			return String.valueOf(largoMax);
			
		return "";
	}
}
