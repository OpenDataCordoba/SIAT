//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;



/**
 * VO correspondiente a la Seleccion Almacenada de Deuda
 * 
 * @author tecso
 *
 */
public class SelAlmDeudaVO extends SelAlmVO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "selAlmDeudaVO";
	
	// banderas validas para incluida y excluida 
	private Boolean seleccionarDeudaEnviarBussEnabled      = true;  
	private Boolean eliminarDeudaEnviarBussEnabled         = true;
	private Boolean limpiarSeleccionDeudaEnviarBussEnabled = true;
	private Boolean logsArmadoDeudaEnviarBussEnabled       = true;
	private Boolean planillaDeudaEnviarBussEnabled         = true;
	private Boolean consultarDeudaEnviarBussEnabled        = true;
	
	private Boolean planillaConvenioCuotaEnviarBussEnabled = true;
	
	// Contructores
	public SelAlmDeudaVO(){
		super();
	}

	// Getters y Setters
	public Boolean getConsultarDeudaEnviarBussEnabled() {
		return consultarDeudaEnviarBussEnabled;
	}
	public void setConsultarDeudaEnviarBussEnabled(
			Boolean consultarDeudaEnviarBussEnabled) {
		this.consultarDeudaEnviarBussEnabled = consultarDeudaEnviarBussEnabled;
	}
	public Boolean getEliminarDeudaEnviarBussEnabled() {
		return eliminarDeudaEnviarBussEnabled;
	}
	public void setEliminarDeudaEnviarBussEnabled(
			Boolean eliminarDeudaEnviarBussEnabled) {
		this.eliminarDeudaEnviarBussEnabled = eliminarDeudaEnviarBussEnabled;
	}
	public Boolean getLimpiarSeleccionDeudaEnviarBussEnabled() {
		return limpiarSeleccionDeudaEnviarBussEnabled;
	}
	public void setLimpiarSeleccionDeudaEnviarBussEnabled(
			Boolean limpiarSeleccionDeudaEnviarBussEnabled) {
		this.limpiarSeleccionDeudaEnviarBussEnabled = limpiarSeleccionDeudaEnviarBussEnabled;
	}
	public Boolean getLogsArmadoDeudaEnviarBussEnabled() {
		return logsArmadoDeudaEnviarBussEnabled;
	}
	public void setLogsArmadoDeudaEnviarBussEnabled(
			Boolean logsArmadoDeudaEnviarBussEnabled) {
		this.logsArmadoDeudaEnviarBussEnabled = logsArmadoDeudaEnviarBussEnabled;
	}
	public Boolean getPlanillaDeudaEnviarBussEnabled() {
		return planillaDeudaEnviarBussEnabled;
	}
	public void setPlanillaDeudaEnviarBussEnabled(
			Boolean planillaDeudaEnviarBussEnabled) {
		this.planillaDeudaEnviarBussEnabled = planillaDeudaEnviarBussEnabled;
	}
	public Boolean getSeleccionarDeudaEnviarBussEnabled() {
		return seleccionarDeudaEnviarBussEnabled;
	}
	public void setSeleccionarDeudaEnviarBussEnabled(
			Boolean seleccionarDeudaEnviarBussEnabled) {
		this.seleccionarDeudaEnviarBussEnabled = seleccionarDeudaEnviarBussEnabled;
	}
	public Boolean getPlanillaConvenioCuotaEnviarBussEnabled() {
		return planillaConvenioCuotaEnviarBussEnabled;
	}
	public void setPlanillaConvenioCuotaEnviarBussEnabled(
			Boolean planillaConvenioCuotaEnviarBussEnabled) {
		this.planillaConvenioCuotaEnviarBussEnabled = planillaConvenioCuotaEnviarBussEnabled;
	}
	

}
