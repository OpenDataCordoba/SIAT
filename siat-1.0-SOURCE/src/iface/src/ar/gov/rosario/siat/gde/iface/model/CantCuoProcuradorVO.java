//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del CantCuoProcurador
 * @author tecso
 * Contiene la cantidad de cuotas con el mismo numero, que contiene el Procurador, para un mes/anio determinado, indepte del convenio 
 */
public class CantCuoProcuradorVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "cantCuoProcuradorVO";
		
	private ProcuradorVO procurador = new ProcuradorVO();
	
	private Double total=0D;
	private List<CantCuoVO> listCantCuo = new ArrayList<CantCuoVO>();

	// Buss Flags
	
	
	// View Constants
	private String totalView = "";

	// Constructores
	public CantCuoProcuradorVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public CantCuoProcuradorVO(int id) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters
	public ProcuradorVO getProcurador() {
		return procurador;
	}

	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}

	public void setTotal(Double importeTotal) {
		this.total = importeTotal;
		this.totalView = StringUtil.formatDouble(importeTotal);
	}
	
	public Double getTotal() {
		return total;
	}
	public void setListCantCuo(List<CantCuoVO> listCantCuo) {
		this.listCantCuo = listCantCuo;
	}

	public List<CantCuoVO> getListCantCuo() {
		return listCantCuo;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	// View getters
	public Double getImporteTotal(){
		Double total =0D;
		for(CantCuoVO cantCuo : listCantCuo){
			total +=cantCuo.getImporteTotal();
		}
		this.setTotal(total);
		return this.getTotal();
	}

	public int getCantCuotas() {
			int cantCuotas = 0;
			for(CantCuoVO cantCuo : listCantCuo){
				cantCuotas +=cantCuo.getCantCuotas();
			}
			return cantCuotas;
	}

	// View getters
	public void setTotalView(String importeTotalView) {
		this.totalView = importeTotalView;
	}
	public String getTotalView() {
		return StringUtil.redondearDecimales(total, 1, 2);
	}


}
