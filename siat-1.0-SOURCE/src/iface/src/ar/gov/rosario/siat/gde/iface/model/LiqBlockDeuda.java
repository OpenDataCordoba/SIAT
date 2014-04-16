//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public abstract class LiqBlockDeuda {
	
	private Long idCuenta;
	private String desRecurso="";
	private String numeroCuenta="";
		
	private List<LiqDeudaVO> listDeuda = new ArrayList<LiqDeudaVO>();
	private String[] listIdDeudaSelected;
	
	// Getters y Setters
	public List<LiqDeudaVO> getListDeuda() {
		return listDeuda;
	}
	public void setListDeuda(List<LiqDeudaVO> listDeuda) {
		this.listDeuda = listDeuda;
	}

	public Long getIdCuenta() {
		return idCuenta;
	}
	public void setIdCuenta(Long idCuenta) {
		this.idCuenta = idCuenta;
	}
	public String getDesRecurso() {
		return desRecurso;
	}
	public void setDesRecurso(String desRecurso) {
		this.desRecurso = desRecurso;
	}
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	
	/**
	 * Calcula y devuelve el subtotal de la lista de deuda, 
	 * redondeando previamente cada valor.
	 * 
	 * @author Cristian
	 * @return
	 */
	public Double getSubTotal() {
		Double subTotal = 0D;
		for (LiqDeudaVO deuda:listDeuda){
			subTotal += NumberUtil.round(deuda.getTotal(), 2);
		}
		return subTotal;
	}

	public Double getSubTotalImporte() {
		Double subTotal = 0D;
		for (LiqDeudaVO deuda:listDeuda){
			subTotal += NumberUtil.round(deuda.getImporte(), 2);
		}
		return subTotal;
	}

	public Double getSubTotalSaldo() {
		Double subTotal = 0D;
		for (LiqDeudaVO deuda:listDeuda){
			subTotal += NumberUtil.round(deuda.getSaldo(), 2);
		}
		return subTotal;
	}
	

	public Double getSubTotalActualizacion() {
		Double subTotal = 0D;
		for (LiqDeudaVO deuda:listDeuda){
			subTotal += NumberUtil.round(deuda.getActualizacion(), 2);
		}
		return subTotal;
	}
	
	
	public Double getSubTotalImportePago() {
		Double subTotal = 0D;
		for (LiqDeudaVO deuda:listDeuda){
			subTotal += NumberUtil.round(deuda.getImportePago(), 2);
		}
		return subTotal;
	}
	
	public String getSubTotalActualizacionView(){
		return (getSubTotalActualizacion()!=null?StringUtil.redondearDecimales(getSubTotalActualizacion(), 1, SiatParam.DEC_IMPORTE_VIEW):"");
	}
	
	public String getSubTotalImporteView(){
		return (getSubTotalImporte()!=null?StringUtil.redondearDecimales(getSubTotalImporte(), 1, SiatParam.DEC_IMPORTE_VIEW):"");
	}

	public String getSubTotalSaldoView(){
		return (getSubTotalSaldo()!=null?StringUtil.redondearDecimales(getSubTotalSaldo(), 1, SiatParam.DEC_IMPORTE_VIEW):"");
	}
	
	public String getSubTotalView(){
		return (getSubTotal()!=null?StringUtil.redondearDecimales(getSubTotal(), 1, SiatParam.DEC_IMPORTE_VIEW):"");
	}
	  	  	
	
	public String[] getListIdDeudaSelected() {
		return listIdDeudaSelected;
	}
	public void setListIdDeudaSelected(String[] listIdDeudaSelected) {
		this.listIdDeudaSelected = listIdDeudaSelected;
	}
	

	/**
	 * Si existe al menos una registro de la lista de deuda que sea "seleccionable", retorna true.
	 * 
	 * 
	 * @author Cristian
	 * @return
	 */
	public boolean poseeDeudaSeleccionable(){
		boolean ret = false; 
		
		for (LiqDeudaVO liqDeudaVO:listDeuda){
			if (liqDeudaVO.getEsSeleccionable())
				ret = true;			
		}
		
		return ret;		
	}
}
