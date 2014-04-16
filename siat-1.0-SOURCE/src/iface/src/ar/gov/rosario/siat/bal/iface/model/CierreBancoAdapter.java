//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Adapter del CierreBanco
 * 
 * @author tecso
 */
public class CierreBancoAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "cierreBancoAdapterVO";
	
    private CierreBancoVO cierreBanco = new CierreBancoVO();
    
    // Para Conciliacion
    private List<TotMovBanDetHelper> listTotMovBanDet = new ArrayList<TotMovBanDetHelper>();
    private Double totalDebito;
    private Double totalCredito;
    private String totalDebitoView = "";
    private String totalCreditoView = "";

    private Double totalNotaImpto;
    private String totalNotaImptoView = "";
    private Double totalMovBan;//Importe Total Calculado sobre Movimientos Bancarios (crédito - débito)
    private Double totalIVANotaImpto;
	private Double totalCalSobreNotaOblig; //Importe Total Calculado sobre Notas de Oblicacion

    private String totalCalSobreNotaObligView="";
    
	// Importe Total Transacciones Inconsistentes ELiminadas
    private Double importeTotalTraIncEli = 0D;
    private String importeTotalTraIncEliView = "";
	
    private Double totalImporteDetallePago; //Importe Total de transacciones SIAT
    private String totalImporteDetallePagoView = "";
    
    private Double totalRendido; //Importe total de transacciones rendidas (s/Notas obligacion)
    private String totalRendidoView = "";
    
    private String totalMovBanView = "";
    private String totalIVANotaImptoView = "";
    
    private Double totalNotaImptoN1N2;//Importe Total Calculado sobre Notas de Abono (notas 1 - notas 2)
    private String totalNotaImptoN1N2View = "";
    
    private Double totalNotaImptoN1N2N3;//Importe Total Calculado sobre Notas de Abono (notas 1 - notas 2 + notas 3)
    private String totalNotaImptoN1N2N3View = "";
    
    // Flags
    private Boolean generarDecJurBussEnabled    = true;
	private Boolean eliminarTranAfipBussEnabled    = true;
    
    // Constructores
    public CierreBancoAdapter(){
    	super(BalSecurityConstants.ABM_CIERREBANCO);
    }
    
	public String getName(){
		return NAME;
	}
    
    //  Getters y Setters
	public CierreBancoVO getCierreBanco() {
		return cierreBanco;
	}
	public void setCierreBanco(CierreBancoVO cierreBancoVO) {
		this.cierreBanco = cierreBancoVO;
	}
	public List<TotMovBanDetHelper> getListTotMovBanDet() {
		return listTotMovBanDet;
	}
	public void setListTotMovBanDet(List<TotMovBanDetHelper> listTotMovBanDet) {
		this.listTotMovBanDet = listTotMovBanDet;
	}
	public Double getTotalCredito() {
		return totalCredito;
	}
	public void setTotalCredito(Double totalCredito) {
		this.totalCredito = totalCredito;
		this.totalCreditoView = StringUtil.formatDouble(totalCredito);
	}
	public Double getImporteTotalTraIncEli() {
		return importeTotalTraIncEli;
	}

	public void setImporteTotalTraIncEli(Double importeTotalTraIncEli) {
		this.importeTotalTraIncEli = importeTotalTraIncEli;
		this.importeTotalTraIncEliView = StringUtil.formatDouble(importeTotalTraIncEli);
	}

	public String getTotalCreditoView() {
		return totalCreditoView;
	}
	public void setTotalCreditoView(String totalCreditoView) {
		this.totalCreditoView = totalCreditoView;
	}
	public void setTotalCalSobreNotaOblig(Double totalCalSobreNotaOblig) {
		this.totalCalSobreNotaOblig = totalCalSobreNotaOblig;
		this.totalCalSobreNotaObligView = StringUtil.formatDouble(totalCalSobreNotaOblig);
	}
	public Double getTotalCalSobreNotaOblig() {
		return totalCalSobreNotaOblig;
	}
	
	public String getImporteTotalTraIncEliView() {
		return importeTotalTraIncEliView;
	}

	public Double getTotalDebito() {
		return totalDebito;
	}
	public void setTotalDebito(Double totalDebito) {
		this.totalDebito = totalDebito;
		this.totalDebitoView = StringUtil.formatDouble(totalDebito);
	}
	public String getTotalDebitoView() {
		return totalDebitoView;
	}
	public void setTotalDebitoView(String totalDebitoView) {
		this.totalDebitoView = totalDebitoView;
	}
	public Double getTotalMovBan() {
		return totalMovBan;
	}
	public void setTotalMovBan(Double totalMovBan) {
		this.totalMovBan = totalMovBan;
		this.totalMovBanView = StringUtil.formatDouble(totalMovBan);
	}
	public Double getTotalNotaImpto() {
		return totalNotaImpto;
	}
	public void setTotalNotaImpto(Double totalNotaImpto) {
		this.totalNotaImpto = totalNotaImpto;
		this.totalNotaImptoView = StringUtil.formatDouble(totalNotaImpto);
	}
	public Double getTotalIVANotaImpto() {
		return totalIVANotaImpto;
	}
	public void setTotalIVANotaImpto(Double totalIVANotaImpto) {
		this.totalIVANotaImpto = totalIVANotaImpto;
		this.totalIVANotaImptoView = StringUtil.formatDouble(totalIVANotaImpto);
	}
	public String getTotalMovBanView() {
		return totalMovBanView;
	}
	public String getTotalNotaImptoView() {
		return totalNotaImptoView;
	}
	public String getTotalIVANotaImptoView() {
		return totalIVANotaImptoView;
	}
	public String getTotalCalSobreNotaObligView() {
		return totalCalSobreNotaObligView;
	}

	public Double getTotalImporteDetallePago() {
		return totalImporteDetallePago;
	}

	public void setTotalImporteDetallePago(Double totalImporteDetallePago) {
		this.totalImporteDetallePago = totalImporteDetallePago;
		this.totalImporteDetallePagoView = StringUtil.formatDouble(totalImporteDetallePago);
	}

	public String getTotalImporteDetallePagoView() {
		return totalImporteDetallePagoView;
	}

	public Double getTotalRendido() {
		return totalRendido;
	}

	public void setTotalRendido(Double totalRendido) {
		this.totalRendido = totalRendido;
		this.totalRendidoView = StringUtil.formatDouble(totalRendido);
	}

	public Double getTotalNotaImptoN1N2() {
		return totalNotaImptoN1N2;
	}
	
	public void setTotalNotaImptoN1N2(Double totalNotaImptoN1N2) {
		this.totalNotaImptoN1N2 = totalNotaImptoN1N2;
		this.totalNotaImptoN1N2View = StringUtil.formatDouble(totalNotaImptoN1N2);
	}
	
	public Double getTotalNotaImptoN1N2N3() {
		return totalNotaImptoN1N2N3;
	}

	public void setTotalNotaImptoN1N2N3(Double totalNotaImptoN1N2N3) {
		this.totalNotaImptoN1N2N3 = totalNotaImptoN1N2N3;
		this.totalNotaImptoN1N2N3View = StringUtil.formatDouble(totalNotaImptoN1N2N3);
	}
	
	//View Getters
	public String getTotalNotaImptoN1N2View() {
		return totalNotaImptoN1N2View;
	}

	public String getTotalNotaImptoN1N2N3View() {
		return totalNotaImptoN1N2N3View;
	}

	public String getTotalRendidoView() {
		return totalRendidoView;
	}
	
	// Flags Seguridad
	public Boolean getGenerarDecJurBussEnabled() {
		return generarDecJurBussEnabled;
	}

	public void setGenerarDecJurBussEnabled(Boolean generarDecJurBussEnabled) {
		this.generarDecJurBussEnabled = generarDecJurBussEnabled;
	}
	
	public Boolean getEliminarTranAfipBussEnabled() {
		return eliminarTranAfipBussEnabled;
	}

	public void setEliminarTranAfipBussEnabled(Boolean eliminarTranAfipBussEnabled) {
		this.eliminarTranAfipBussEnabled = eliminarTranAfipBussEnabled;
	}
	
	public String getGenerarDecJurEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getGenerarDecJurBussEnabled(), 
				BalSecurityConstants.ABM_TRANAFIP, BalSecurityConstants.MTD_GENERARDECJUR);
	}
	
	public String getEliminarTranAfipEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getEliminarTranAfipBussEnabled(),
				BalSecurityConstants.ABM_TRANAFIP, BaseSecurityConstants.ELIMINAR);
	}
}