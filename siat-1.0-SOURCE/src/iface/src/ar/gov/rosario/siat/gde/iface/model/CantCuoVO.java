//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del CantCuoProcurador
 * @author tecso
 * Contiene la cantidad de cuotas con el mismo numero, que contiene el Procurador, para un mes/anio determinado, indepte del convenio 
 */
public class CantCuoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "cantCuoVO";
	
	private long idProcurador;
	private String desProcurador;
	private ProcuradorVO procurador = new ProcuradorVO();
	
	private Integer nroCuota =0;
	private Integer cantCuotas=0;
	private Integer mes=0;
	private Integer anio=0;
	private Double importeTotal=0D;
	private Double subTotal=0D;
	// Buss Flags
	
	
	// View Constants
	
	private String importeTotalView = "";
	
	// Constructores
	public CantCuoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public CantCuoVO(int id) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters

	public Integer getNroCuota() {
		return nroCuota;
	}

	public void setNroCuota(Integer nroCuota) {
		this.nroCuota = nroCuota;		
	}

	public Integer getCantCuotas() {
		return cantCuotas;
	}

	public long getIdProcurador() {
		return idProcurador;
	}

	public void setIdProcurador(long idProcurador) {
		this.idProcurador = idProcurador;
	}

	public String getDesProcurador() {
		return desProcurador;
	}

	public void setDesProcurador(String desProcurador) {
		this.desProcurador = desProcurador;
	}

	public ProcuradorVO getProcurador() {
		return procurador;
	}

	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}

	public void setCantCuotas(Integer cantCuotas) {
		this.cantCuotas = cantCuotas;		
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;		
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;		
	}

	public Double getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(Double importeTotal) {
		this.importeTotal = importeTotal;
		this.importeTotalView = StringUtil.formatDouble(importeTotal);
	}
	
	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	/**
	 * Incrementa la cantidad de cuotas y el importe total en las cant. pasadas como parametro
	 * @param cantCuotas
	 * @param importeCuota
	 */
	public void actualizar(int cantCuotas, Double importeCuota) {
		this.cantCuotas+=cantCuotas;
		this.importeTotal +=importeCuota;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public void setImporteTotalView(String importeTotalView) {
		this.importeTotalView = importeTotalView;
	}
	public String getImporteTotalView() {
		return StringUtil.completarCaracteresIzq(StringUtil.redondearDecimales(importeTotal, 1, 2), 10, ' ');
	}
	public String getSubTotalView() {
		return StringUtil.completarCaracteresIzq(StringUtil.redondearDecimales(subTotal, 1, 2), 10, ' ');
				}



}
