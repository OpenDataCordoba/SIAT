//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Search Page de la Consulta de Deudas por Cuenta del Proceso de Envio Judicial.
 *  
 * @author tecso
 *
 */
public class DeudaProMasConsPorCtaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "deudaProMasConsPorCtaSearchPageVO";
	
	// Envio Judicial de la deuda incluida.
	private ProcesoMasivoVO procesoMasivo = new ProcesoMasivoVO(); 
	
	// parametros de la cuenta
	private CuentaVO cuenta = new CuentaVO();

	private Double sumaImporte;
	private Double sumaSaldo;
	private Double sumaSaldoActualizado;
	
	private LiqDeudaAdapter liqDeudaAdapter = new LiqDeudaAdapter();
	
	private boolean esDeudaAdministrativa = true;
	
	public DeudaProMasConsPorCtaSearchPage(){
		super(GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO);
	}

	// Getters y Setters
	
	public ProcesoMasivoVO getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivoVO procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	public Double getSumaImporte() {
		return sumaImporte;
	}
	public void setSumaImporte(Double sumaImporte) {
		this.sumaImporte = sumaImporte;
	}
	public Double getSumaSaldo() {
		return sumaSaldo;
	}
	public void setSumaSaldo(Double sumaSaldo) {
		this.sumaSaldo = sumaSaldo;
	}
	public Double getSumaSaldoActualizado() {
		return sumaSaldoActualizado;
	}
	public void setSumaSaldoActualizado(Double sumaSaldoActualizado) {
		this.sumaSaldoActualizado = sumaSaldoActualizado;
	}

	public String getSumaImporteView() {
		return StringUtil.formatDouble(this.getSumaImporte());
	}
	public String getSumaSaldoView() {
		return StringUtil.formatDouble(this.getSumaSaldo());
	}
	public String getSumaSaldoActualizadoView() {
		return StringUtil.formatDouble(this.getSumaSaldoActualizado());
	}
	
	
	public LiqDeudaAdapter getLiqDeudaAdapter() {
		return liqDeudaAdapter;
	}
	public void setLiqDeudaAdapter(LiqDeudaAdapter liqDeudaAdapter) {
		this.liqDeudaAdapter = liqDeudaAdapter;
	}

	public boolean getEsDeudaAdministrativa() {
		return esDeudaAdministrativa;
	}
	public void setEsDeudaAdministrativa(boolean esDeudaAdministrativa) {
		this.esDeudaAdministrativa = esDeudaAdministrativa;
	}
	
	
}
