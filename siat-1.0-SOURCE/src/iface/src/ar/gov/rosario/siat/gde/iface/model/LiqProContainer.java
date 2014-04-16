//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;

public class LiqProContainer extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	private ProcuradorVO procurador = new ProcuradorVO();
	
	private Double totalComision;
	
	private List<FilaConvenioCuotaVO> listFilaConvenioCuota  = new ArrayList<FilaConvenioCuotaVO>();
	
	private List<LiqReciboVO> listLiqRecibo = new ArrayList<LiqReciboVO>();

	private int cantTotalConvCuota=0;
	private int cantTotalRecibos=0;

	private Double totalImporteConvCuota=0D;
	private Double totalImporteRecibos=0D;

	private Double totalImporteLiquidado=0D;
		
	
	public ProcuradorVO getProcurador() {
		return procurador;
	}

	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}


	public Double getTotalComision() {
		return totalComision;
	}

	public void setTotalComision(Double totalComision) {
		this.totalComision = totalComision;
	}

	public List<FilaConvenioCuotaVO> getListFilaConvenioCuota() {
		return listFilaConvenioCuota;
	}

	public void setListFilaConvenioCuota(
			List<FilaConvenioCuotaVO> listFilaConvenioCuota) {
		this.listFilaConvenioCuota = listFilaConvenioCuota;
	}

	public List<LiqReciboVO> getListLiqRecibo() {
		return listLiqRecibo;
	}

	public void setListLiqRecibo(List<LiqReciboVO> listLiqRecibo) {
		this.listLiqRecibo = listLiqRecibo;
	}

	public int getCantTotalConvCuota() {
		return cantTotalConvCuota;
	}

	public void setCantTotalConvCuota(int cantTotalConvCuota) {
		this.cantTotalConvCuota = cantTotalConvCuota;
	}

	public Double getTotalImporteConvCuota() {
		return totalImporteConvCuota;
	}

	public void setTotalImporteConvCuota(Double totalImporteConvDeuda) {
		this.totalImporteConvCuota = totalImporteConvDeuda;
	}

	public int getCantTotalRecibos() {
		return cantTotalRecibos;
	}

	public void setCantTotalRecibos(int cantTotalRecibos) {
		this.cantTotalRecibos = cantTotalRecibos;
	}

	public Double getTotalImporteRecibos() {
		return totalImporteRecibos;
	}

	public void setTotalImporteRecibos(Double totalImporteRecibos) {
		this.totalImporteRecibos = totalImporteRecibos;
	}

	public Double getTotalImporteLiquidado() {
		return totalImporteLiquidado;
	}

	public void setTotalImporteLiquidado(Double totalImporteLiquidado) {
		this.totalImporteLiquidado = totalImporteLiquidado;
	}

	// view getters
	public String getTotalComisionView(){
		return StringUtil.redondearDecimales(totalComision, 1, 2);
	}
	
	public String getTotalImporteConvCuotaView(){
		return StringUtil.redondearDecimales(totalImporteConvCuota, 1, 2);
	}
	
	public String getTotalImporteRecibosView(){
		return StringUtil.redondearDecimales(totalImporteRecibos, 1, 2);
	}
	
	public String getTotalImporteLiquidadoView(){
		return StringUtil.redondearDecimales(totalImporteLiquidado, 1, 2);
	}
	
}
