//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class FolComVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private FolioVO folio = new FolioVO();
	private Date fecha;
	private String concepto = "";
	private Double importe;
	private CuentaBancoVO cueBan = new CuentaBancoVO();
	private String desCueBan = "";
	private String nroComp = "";
	private CompensacionVO compensacion = new CompensacionVO();

	private String fechaView = "";
	private String importeView = "";
	
	//Constructores 
	public FolComVO(){
		super();
	}

	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public CuentaBancoVO getCueBan() {
		return cueBan;
	}
	public void setCueBan(CuentaBancoVO cueBan) {
		this.cueBan = cueBan;
	}
	public String getDesCueBan() {
		return desCueBan;
	}
	public void setDesCueBan(String desCueBan) {
		this.desCueBan = desCueBan;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
		this.fechaView = DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaView() {
		return fechaView;
	}
	public void setFechaView(String fechaView) {
		this.fechaView = fechaView;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView = StringUtil.formatDouble(importe);
	}
	public String getImporteView() {
		return importeView;
	}
	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}
	public String getNroComp() {
		return nroComp;
	}
	public void setNroComp(String nroComp) {
		this.nroComp = nroComp;
	}
	public FolioVO getFolio() {
		return folio;
	}
	public void setFolio(FolioVO folio) {
		this.folio = folio;
	}
	public CompensacionVO getCompensacion() {
		return compensacion;
	}
	public void setCompensacion(CompensacionVO compensacion) {
		this.compensacion = compensacion;
	}
	
	
}