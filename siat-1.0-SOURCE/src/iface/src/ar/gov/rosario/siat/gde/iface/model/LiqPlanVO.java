//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

public class LiqPlanVO  {

	private Long idPlan;
		
	private String linkNormativa = "";
	private String desPlan = "";
	private String leyendaPlan = "";
	private String desViaDeuda = "";
	private Long esManual=0L;
	
	
	/*private Double totalDescuento = 0D;	
	private Double totalActualizacion = 0D;*/
	
	private Double desCapitalOriginal = 0D;;
	private Double desActualizacion = 0D;
	
	private Double interes = 0D;
	private Double desInteres = 0D;
	private Double interesAplicado = 0D;

		
	private Double totalCapital = 0D;	
	private Double totalInteres = 0D;
	private Double totalImporte = 0D;
	
	// Alternativas de Cuotas para presentar al usuario.
	private List<LiqCuotaVO> listAltCuotas = new ArrayList<LiqCuotaVO>();
	
	// Lista utilizada para mostra la simulacion y luego para la formalizacion.
	private List<LiqCuotaVO> listCuotasForm = new ArrayList<LiqCuotaVO>();
	
	private boolean esSeleccionable = true;
	private String msgDeshabilitado = "";
	
	public Long getIdPlan() {
		return idPlan;
	}
	public void setIdPlan(Long idPlan) {
		this.idPlan = idPlan;
	}
	public Long getEsManual () {
			return esManual;
	}
	public void setEsManual (Long esManual){
		this.esManual=esManual;
	}
	public String getLeyendaPlan() {
		return leyendaPlan;
	}
	public void setLeyendaPlan(String leyendaPlan) {
		this.leyendaPlan = leyendaPlan;
	}
	public String getLinkNormativa() {
		return linkNormativa;
	}
	public void setLinkNormativa(String linkNormativa) {
		this.linkNormativa = linkNormativa;
	}
	public String getDesPlan() {
		return desPlan;
	}
	public void setDesPlan(String desPlan) {
		this.desPlan = desPlan;
	}

	public boolean isEsSeleccionable() {
		return esSeleccionable;
	}
	public void setEsSeleccionable(boolean esSeleccionable) {
		this.esSeleccionable = esSeleccionable;
	}
	
	public String getMsgDeshabilitado() {
		return msgDeshabilitado;
	}
	public void setMsgDeshabilitado(String msgDeshabilitado) {
		this.msgDeshabilitado = msgDeshabilitado;
	}
	
	public List<LiqCuotaVO> getListAltCuotas() {
		return listAltCuotas;
	}
	public void setListAltCuotas(List<LiqCuotaVO> listAltCuotas) {
		this.listAltCuotas = listAltCuotas;
	}
	
	public List<LiqCuotaVO> getListCuotasForm() {
		return listCuotasForm;
	}
	public void setListCuotasForm(List<LiqCuotaVO> listCuotasForm) {
		this.listCuotasForm = listCuotasForm;
	}
	
	public Double getTotalCapital() {
		return totalCapital;
	}
	public void setTotalCapital(Double totalCapital) {
		this.totalCapital = totalCapital;
	}
	public Double getTotalImporte() {
		return totalImporte;
	}
	public void setTotalImporte(Double totalImporte) {
		this.totalImporte = totalImporte;
	}
	public Double getTotalInteres() {
		return totalInteres;
	}
	public void setTotalInteres(Double totalInteres) {
		this.totalInteres = totalInteres;
	}
	public String getDesViaDeuda() {
		return desViaDeuda;
	}
	public void setDesViaDeuda(String desViaDeuda) {
		this.desViaDeuda = desViaDeuda;
	}
	
	public Double getDesActualizacion() {
		return desActualizacion;
	}
	public void setDesActualizacion(Double desActualizacion) {
		this.desActualizacion = desActualizacion;
	}
	public Double getDesCapitalOriginal() {
		return desCapitalOriginal;
	}
	public void setDesCapitalOriginal(Double desCapitalOriginal) {
		this.desCapitalOriginal = desCapitalOriginal;
	}
	public Double getDesInteres() {
		return desInteres;
	}
	public void setDesInteres(Double desInteres) {
		this.desInteres = desInteres;
	}
	public Double getInteres() {
		return interes;
	}
	public void setInteres(Double interes) {
		this.interes = interes;
	}
	public Double getInteresAplicado() {
		return interesAplicado;
	}
	public void setInteresAplicado(Double interesAplicado) {
		this.interesAplicado = interesAplicado;
	}
	
	/*public Double getTotalActualizacion() {
		return totalActualizacion;
	}
	public void setTotalActualizacion(Double totalActualizacion) {
		this.totalActualizacion = totalActualizacion;
	}
	public Double getTotalDescuento() {
		return totalDescuento;
	}
	public void setTotalDescuento(Double totalDescuento) {
		this.totalDescuento = totalDescuento;
	}*/
	
	
	
}
