//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del AnulacionObra
 * @author tecso
 *
 */
public class CatRSDreiVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "catRSDreiVO";
	
	private Integer nroCategoria;
	
	private Double ingBruAnu;
	
	private Long superficie;
	
	private Double importe;
	
	private Integer cantEmpleados;
  
	private Date fechaDesde;
 
	private Date fechaHasta;
	
	private String nroCategoriaView="";
	
	private String fechaDesdeView="";
	
	private String fechaHastaView="";
	
	private String ingBruAnuView="";
	
	private String superficieView="";
	
	private String importeView="";
	
	private String cantEmpleadosView="";
	
	// Buss Flags

	
	//Constructores
	public CatRSDreiVO() {
		super();
	}
	
	public CatRSDreiVO(int id, String desc) {
		super();
		setId(new Long(id));
		setNroCategoriaView(desc);
	}
	
	// Getters y Setters
	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public Integer getNroCategoria() {
		return nroCategoria;
	}

	public void setNroCategoria(Integer nroCategoria) {
		this.nroCategoria = nroCategoria;
		this.nroCategoriaView = StringUtil.formatInteger(nroCategoria);
	}

	public Double getIngBruAnu() {
		return ingBruAnu;
	}

	public void setIngBruAnu(Double ingBruAnu) {
		this.ingBruAnu = ingBruAnu;
		this.ingBruAnuView = StringUtil.formatDouble(ingBruAnu);
	}

	public Long getSuperficie() {
		return superficie;
	}

	public void setSuperficie(Long superficie) {
		this.superficie = superficie;
		this.superficieView = StringUtil.formatLong(superficie);
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe);
	}

	public Integer getCantEmpleados() {
		return cantEmpleados;
	}

	public void setCantEmpleados(Integer cantEmpleados) {
		this.cantEmpleados = cantEmpleados;
		this.cantEmpleadosView = StringUtil.formatInteger(cantEmpleados);
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	
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

	public String getNroCategoriaView() {
		return this.nroCategoriaView;
	}

	public void setNroCategoriaView(String nroCategoriaView) {
		this.nroCategoriaView = nroCategoriaView;
	}
	
	public String getIngBruAnuView() {
		return (this.ingBruAnuView!=null)?this.ingBruAnuView.toString():"";		
	}

	public void setIngBruAnuView(String ingBruAnuView) {
		this.ingBruAnuView = ingBruAnuView;
	}

	public String getSuperficieView() {	
		return (this.superficieView!=null)?this.superficieView.toString():"";
	}

	public void setSuperficieView(String superficieView) {
		this.superficieView = superficieView;
	}

	public String getImporteView() {
		return (this.importeView!=null)?this.importeView.toString():"";		
	}

	public void setImporteView(String importeView) {
		this.importeView = importeView;	
	}

	public String getCantEmpleadosView() {
		return (this.cantEmpleadosView!=null)?this.cantEmpleadosView.toString():"";
	}

	public void setCantEmpleadosView(String cantEmpleadosView) {
		this.cantEmpleadosView = cantEmpleadosView;
	}


}
