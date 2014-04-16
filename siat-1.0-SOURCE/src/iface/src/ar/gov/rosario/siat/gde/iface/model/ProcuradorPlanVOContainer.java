//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.StringUtil;


public class ProcuradorPlanVOContainer extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private String fechaVencimientoDesdeView = "";
	private String fechaVencimientoHastaView = "";
	private String usr="";
	private Double total;
	private RecursoVO recurso= new RecursoVO();
	
	private List<ProcuradorPlanVO> listProcuradorPlanVO = new ArrayList<ProcuradorPlanVO>();

	public void calcularTotal(){
		Double total =0D;
		for(ProcuradorPlanVO procuradorPlanVO : listProcuradorPlanVO){
			total += procuradorPlanVO.getImporteTotal();
		}
		this.total = total;
	}

	
	public List<ProcuradorPlanVO> getListProcuradorPlanVO() {
		return listProcuradorPlanVO;
	}

	public void setListProcuradorPlanVO(List<ProcuradorPlanVO> listProcuradorPlanVO) {
		this.listProcuradorPlanVO = listProcuradorPlanVO;
	}
	
	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
	
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}


	public RecursoVO getRecurso() {
		return recurso;
	}


	public String getUsr() {
		return usr;
	}

	public void setUsr(String usuario) {
		this.usr = usuario;
	}

	// View getters
	public String getFechaVencimientoDesdeView() {
		return fechaVencimientoDesdeView;
	}
	
	public void setFechaVencimientoDesdeView(String fechaVencimientoDesdeView) {
		this.fechaVencimientoDesdeView = fechaVencimientoDesdeView;
	}
	
	public String getFechaVencimientoHastaView() {
		return fechaVencimientoHastaView;
	}
	
	public void setFechaVencimientoHastaView(String fechaVencimientoHastaView) {
		this.fechaVencimientoHastaView = fechaVencimientoHastaView;
	}
	
	public String getTotalView(){
		return StringUtil.redondearDecimales(total, 1, 2);
	}
	
	public int getCantCuotas(){
		int cantCuotas = 0;
		for(ProcuradorPlanVO procuradorPlanVO : listProcuradorPlanVO){
			cantCuotas +=procuradorPlanVO.getCantCuotas();
		}
		return cantCuotas;
	}
}
