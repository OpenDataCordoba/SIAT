//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

public class PlaEnvDeuProPrint extends SiatAdapterModel{

	private static final long serialVersionUID = 1L;

	private Integer anioPlanilla;
	private Long   nroPlanilla;
	private Long   idProcurador;
	private String desProcurador;
	private Date   fechaEnvio; 
	private Long   totalRegistros;     
	private Double importeTotal;
	private Long   cantidadCuentas;

	private List<ConstanciaDeuPrint> listConstanciaDeuPrint = new ArrayList<ConstanciaDeuPrint>(); 

	public PlaEnvDeuProPrint(){
		super("");
	}

	public Integer getAnioPlanilla() {
		return anioPlanilla;
	}
	public void setAnioPlanilla(Integer anioPlanilla) {
		this.anioPlanilla = anioPlanilla;
	}
	public Long getCantidadCuentas() {
		return cantidadCuentas;
	}
	public void setCantidadCuentas(Long cantidadCuentas) {
		this.cantidadCuentas = cantidadCuentas;
	}
	public String getDesProcurador() {
		return desProcurador;
	}
	public void setDesProcurador(String desProcurador) {
		this.desProcurador = desProcurador;
	}
	public Date getFechaEnvio() {
		return fechaEnvio;
	}
	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}
	public Long getIdProcurador() {
		return idProcurador;
	}
	public void setIdProcurador(Long idProcurador) {
		this.idProcurador = idProcurador;
	}
	public Double getImporteTotal() {
		return importeTotal;
	}
	public void setImporteTotal(Double importeTotal) {
		this.importeTotal = importeTotal;
	}
	public List<ConstanciaDeuPrint> getListConstanciaDeuPrint() {
		return listConstanciaDeuPrint;
	}
	public void setListConstanciaDeuPrint(
			List<ConstanciaDeuPrint> listConstanciaDeuPrint) {
		this.listConstanciaDeuPrint = listConstanciaDeuPrint;
	}
	public Long getNroPlanilla() {
		return nroPlanilla;
	}
	public void setNroPlanilla(Long nroPlanilla) {
		this.nroPlanilla = nroPlanilla;
	}
	public Long getTotalRegistros() {
		return totalRegistros;
	}
	public void setTotalRegistros(Long totalRegistros) {
		this.totalRegistros = totalRegistros;
	}
	public void addConstanciaDeuPrint(ConstanciaDeuPrint constanciaDeuPrint){
		this.getListConstanciaDeuPrint().add(constanciaDeuPrint);
	}
	
}


