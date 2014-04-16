//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.rec.iface.model.ObraFormaPagoVO;
import ar.gov.rosario.siat.rec.iface.model.PlaCuaDetVO;

/**
 * Adapter del CambioPlanCDM
 * 
 * @author tecso
 */
public class CambioPlanCDMAdapter extends SiatAdapterModel{
	
	public static final String NAME = "cambioPlanCDMAdapterVO";
	
    private PlaCuaDetVO plaCuaDet = new PlaCuaDetVO();
    
    // Bandera para swichear entre cambio de plan y cuota saldo.
    private boolean esCuotaSaldo = false;
    
    private Double capitalCancelado;
    private Double interesCancelado;
    private Double totalPendiente;
    
    private LiqCuentaVO cuenta = new LiqCuentaVO();
    
    private boolean poseeDeudaVencida = false;
    private boolean poseeDeudaNoVencida = false;
    private List<LiqDeudaVO> listDeudaVencida = new ArrayList<LiqDeudaVO>();
    private Double totalDeudaVencida;
    
    private List<ObraFormaPagoVO> listPlanes = new ArrayList<ObraFormaPagoVO>(); 
    
    private Long idPlanSelected;  
    
    // Propiedades para mostra una vez realizado en cambio
    private Integer cuotasAPagar;
    private List<LiqDeudaVO> listDeudaGenerada = new ArrayList<LiqDeudaVO>();
    private Double totalDeudaGenerada;
    
    // Contenedor de recibos de Cuota Saldo
    private LiqRecibos liqRecibos = new LiqRecibos(); 
    
    
    // Propiedades para asignacion de permisos que bloque de cuenta
    private boolean verDetalleObjImpEnabled = false;
    private boolean verDeudaContribEnabled = false;
    private boolean verHistoricoContribEnabled = false;
    private boolean verConvenioEnabled = false;
    private boolean verCuentaDesgUnifEnabled = false;
    private boolean buzonCambiosEnabled = false; 		// Permiso para ir al buzon de cambio de datos de persona.
    
    private boolean poseeFormaPagoSeleccionable = false;
    
    // Constructores
    public CambioPlanCDMAdapter(){
    	super(GdeSecurityConstants.LIQ_DEUDA);
    }


    //  Getters y Setters
	public LiqCuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(LiqCuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public List<LiqDeudaVO> getListDeudaVencida() {
		return listDeudaVencida;
	}
	public void setListDeudaVencida(List<LiqDeudaVO> listDeudaVencida) {
		this.listDeudaVencida = listDeudaVencida;
	}

	public Double getCapitalCancelado() {
		return capitalCancelado;
	}
	public void setCapitalCancelado(Double capitalCancelado) {
		this.capitalCancelado = capitalCancelado;
	}

	public PlaCuaDetVO getPlaCuaDet() {
		return plaCuaDet;
	}
	public void setPlaCuaDet(PlaCuaDetVO plaCuaDet) {
		this.plaCuaDet = plaCuaDet;
	}

	public Double getInteresCancelado() {
		return interesCancelado;
	}
	public void setInteresCancelado(Double interesCancelado) {
		this.interesCancelado = interesCancelado;
	}

	public Double getTotalPendiente() {
		return totalPendiente;
	}
	public void setTotalPendiente(Double totalPendiente) {
		this.totalPendiente = totalPendiente;
	}

	public List<ObraFormaPagoVO> getListPlanes() {
		return listPlanes;
	}
	public void setListPlanes(List<ObraFormaPagoVO> listPlanes) {
		this.listPlanes = listPlanes;
	}

	public boolean isPoseeDeudaVencida() {
		return poseeDeudaVencida;
	}
	public void setPoseeDeudaVencida(boolean poseeDeudaVencida) {
		this.poseeDeudaVencida = poseeDeudaVencida;
	}

	public boolean isPoseeDeudaNoVencida() {
		return poseeDeudaNoVencida;
	}
	public void setPoseeDeudaNoVencida(boolean poseeDeudaNoVencida) {
		this.poseeDeudaNoVencida = poseeDeudaNoVencida;
	}

	public Double getTotalDeudaVencida() {
		return totalDeudaVencida;
	}
	public void setTotalDeudaVencida(Double totalDeudaVencida) {
		this.totalDeudaVencida = totalDeudaVencida;
	}
	
	public Long getIdPlanSelected() {
		return idPlanSelected;
	}
	public void setIdPlanSelected(Long idPlanSelected) {
		this.idPlanSelected = idPlanSelected;
	}

	public Integer getCuotasAPagar() {
		return cuotasAPagar;
	}
	public void setCuotasAPagar(Integer cuotasAPagar) {
		this.cuotasAPagar = cuotasAPagar;
	}

	public List<LiqDeudaVO> getListDeudaGenerada() {
		return listDeudaGenerada;
	}
	public void setListDeudaGenerada(List<LiqDeudaVO> listDeudaGenerada) {
		this.listDeudaGenerada = listDeudaGenerada;
	}

	public Double getTotalDeudaGenerada() {
		return totalDeudaGenerada;
	}
	public void setTotalDeudaGenerada(Double totalDeudaGenerada) {
		this.totalDeudaGenerada = totalDeudaGenerada;
	}

	public LiqRecibos getLiqRecibos() {
		return liqRecibos;
	}
	public void setLiqRecibos(LiqRecibos liqRecibos) {
		this.liqRecibos = liqRecibos;
	}

	public boolean isEsCuotaSaldo() {
		return esCuotaSaldo;
	}
	public void setEsCuotaSaldo(boolean esCuotaSaldo) {
		this.esCuotaSaldo = esCuotaSaldo;
	}


	public boolean isVerDetalleObjImpEnabled() {
		return verDetalleObjImpEnabled;
	}
	public void setVerDetalleObjImpEnabled(boolean verDetalleObjImpEnabled) {
		this.verDetalleObjImpEnabled = verDetalleObjImpEnabled;
	}

	public boolean isVerDeudaContribEnabled() {
		return verDeudaContribEnabled;
	}
	public void setVerDeudaContribEnabled(boolean verDeudaContribEnabled) {
		this.verDeudaContribEnabled = verDeudaContribEnabled;
	}

	public boolean isVerHistoricoContribEnabled() {
		return verHistoricoContribEnabled;
	}
	public void setVerHistoricoContribEnabled(boolean verHistoricoContribEnabled) {
		this.verHistoricoContribEnabled = verHistoricoContribEnabled;
	}

	public boolean isVerConvenioEnabled() {
		return verConvenioEnabled;
	}
	public void setVerConvenioEnabled(boolean verConvenioEnabled) {
		this.verConvenioEnabled = verConvenioEnabled;
	}

	public boolean isVerCuentaDesgUnifEnabled() {
		return verCuentaDesgUnifEnabled;
	}
	public void setVerCuentaDesgUnifEnabled(boolean verCuentaDesgUnifEnabled) {
		this.verCuentaDesgUnifEnabled = verCuentaDesgUnifEnabled;
	}
	
	public boolean isBuzonCambiosEnabled() {
		return buzonCambiosEnabled;
	}
	public void setBuzonCambiosEnabled(boolean buzonCambiosEnabled) {
		this.buzonCambiosEnabled = buzonCambiosEnabled;
	}

	public boolean getPoseeFormaPagoSeleccionable() {
		return poseeFormaPagoSeleccionable;
	}
	public void setPoseeFormaPagoSeleccionable(boolean poseeFormaPagoSeleccionable) {
		this.poseeFormaPagoSeleccionable = poseeFormaPagoSeleccionable;
	}

	// View getters
}
