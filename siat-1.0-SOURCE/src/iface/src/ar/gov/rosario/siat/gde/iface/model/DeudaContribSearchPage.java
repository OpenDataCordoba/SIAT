//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import coop.tecso.demoda.iface.helper.NumberUtil;

/**
 * Search Page de Cuentas del contribuyente
 * @author tecso
 *
 */
public class DeudaContribSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "deudaContribSearchPageVO";

	private ContribuyenteVO contribuyente = new ContribuyenteVO();
	
	private List<ServicioBancoVO> listServicioBanco = new ArrayList<ServicioBancoVO>();
	private ServicioBancoVO servicioBanco = new ServicioBancoVO();
	
	private Boolean muestraBuscarContrib = true;
	
	private Boolean superaMaxCantCuentas = false;
	
	private Double totalGeneral = 0D;
	
	private String totalGeneralView = "";
	
	public DeudaContribSearchPage(){
		super(GdeSecurityConstants.ABM_DEUDA_CONTRIB);
	}

	// Getters y Setters	
	public ContribuyenteVO getContribuyente() {
		return contribuyente;
	}

	public void setContribuyente(ContribuyenteVO contribuyente) {
		this.contribuyente = contribuyente;
	}

	
	// liquidacionDeudaEnabled
	public String getLiquidacionDeudaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ABM_DEUDA_CONTRIB, 
				GdeSecurityConstants.MTD_LIQUIDACION_DEUDA); 
	}
	// estadoCuentaEnabled
	public String getEstadoCuentaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ABM_DEUDA_CONTRIB, 
				GdeSecurityConstants.MTD_ESTADO_CUENTA); 
	}
	// imprimirListDeudaContribEnabled
	public String getImprimirListDeudaContribEnabled() {
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ABM_DEUDA_CONTRIB, 
				GdeSecurityConstants.MTD_IMPRIMIR_LIST_DEUDA_CONTRIB); 
	}
	
	// Agregar Cuenta
	public String getAgregarCuentaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(GdeSecurityConstants.ABM_DEUDA_CONTRIB, 
				GdeSecurityConstants.MTD_AGREGAR_CUENTA); 
	}

	public Boolean getMuestraBuscarContrib() {
		return muestraBuscarContrib;
	}
	public void setMuestraBuscarContrib(Boolean muestraBuscarContrib) {
		this.muestraBuscarContrib = muestraBuscarContrib;
	}

	public Boolean getSuperaMaxCantCuentas() {
		return superaMaxCantCuentas;
	}
	public void setSuperaMaxCantCuentas(Boolean superaMaxCantCuentas) {
		this.superaMaxCantCuentas = superaMaxCantCuentas;
	}

	public List<ServicioBancoVO> getListServicioBanco() {
		return listServicioBanco;
	}
	public void setListServicioBanco(List<ServicioBancoVO> listServicioBanco) {
		this.listServicioBanco = listServicioBanco;
	}

	public ServicioBancoVO getServicioBanco() {
		return servicioBanco;
	}
	public void setServicioBanco(ServicioBancoVO servicioBanco) {
		this.servicioBanco = servicioBanco;
	}

	public Double getTotalGeneral() {
		return totalGeneral;
	}
	public void setTotalGeneral(Double totalGeneral) {
		this.totalGeneral = totalGeneral;
		this.totalGeneralView = NumberUtil.round(totalGeneral, SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	public String getTotalGeneralView() {
		return totalGeneralView;
	}
	public void setTotalGeneralView(String totalGeneralView) {
		this.totalGeneralView = totalGeneralView;
	}
	
}