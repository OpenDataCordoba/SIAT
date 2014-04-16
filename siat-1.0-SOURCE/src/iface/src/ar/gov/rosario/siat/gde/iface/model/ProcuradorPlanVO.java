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
 * Value Object del ProcuradorPlan
 * @author tecso
 * Contiene la los precuradores con el mismo Plan, que contiene el Plan
 */
public class ProcuradorPlanVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "procuradorPlanVO";
	
	private PlanVO plan = new PlanVO();
	
	private List<CantCuoProcuradorVO> listCantCuoProcurador = new ArrayList<CantCuoProcuradorVO>();
	private List<CantCuoVO> listCantCuo = new ArrayList<CantCuoVO>();
	
	private Double total = 0D;

	// Buss Flags
	
	
	// View Constants
	private String totalView = "";

	// Constructores
	public ProcuradorPlanVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public ProcuradorPlanVO(int id) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters
	public PlanVO getPlan() {
		return plan;
	}

	public void setPlan(PlanVO plan) {
		this.plan = plan;
	}

	public void setListCantCuoProcurador(List<CantCuoProcuradorVO> listCantCuoProcurador) {
		this.listCantCuoProcurador = listCantCuoProcurador;
	}

	public List<CantCuoProcuradorVO> getListCantCuoProcurador() {
		return listCantCuoProcurador;
	}

	public void setListCantCuo(List<CantCuoVO> listCantCuo) {
		this.listCantCuo = listCantCuo;
	}

	public List<CantCuoVO> getListCantCuo() {
		return listCantCuo;
	}

	public void setTotal(Double total) {
		this.total = total;
		this.totalView = StringUtil.formatDouble(total);
	}

	public Double getTotal() {
		return total;
	}
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public Double getImporteTotal(){
		Double total = 0D;
		if (listCantCuoProcurador.size() > 0) {
			for (CantCuoProcuradorVO cantCuoProcurador : listCantCuoProcurador) {
				total += cantCuoProcurador.getImporteTotal();
			}
		} else {
			for (CantCuoVO cantCuoVO : listCantCuo) {
				total += cantCuoVO.getImporteTotal();
			}
		}	
		this.setTotal(total);
		return this.getTotal();
	}

	public int getCantCuotas() {
			int cantCuotas = 0;
			if (listCantCuoProcurador.size() > 0) {
				for (CantCuoProcuradorVO cantCuoProcurador : listCantCuoProcurador) {
					cantCuotas +=cantCuoProcurador.getCantCuotas();
				}
			} else {
				for (CantCuoVO cantCuoVO : listCantCuo) {
					cantCuotas +=cantCuoVO.getCantCuotas();
				}
			}	
			return cantCuotas;
	}
	public void setTotalView(String totalView) {
		this.totalView = totalView;
	}
	public String getTotalView() {
		return StringUtil.redondearDecimales(total, 1, 2);
	}


}
