//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.cas.iface.model.CasoContainer;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Adapter del LiqConvenioCuenta
 * 
 * @author tecso
 */
public class LiqConvenioSalPorCadAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "liqConvenioSalPorCadAdapterVO";
	
	private LiqCuentaVO cuenta = new LiqCuentaVO();
    
	private LiqConvenioVO convenio = new LiqConvenioVO();
   
    private CasoContainer casoContainer = new CasoContainer();
    
    private Date fechaSaldo=new Date();
    
    private String Observacion="";
    
    private String fechaSaldoView=DateUtil.formatDate(fechaSaldo, DateUtil.ddSMMSYYYY_MASK);
    
    private Boolean esVueltaAtras=false;
    
    private boolean esAnulacion = false;

    // Propiedades para la asignacion de permisos
    private boolean verDeudaContribEnabled = false; // Poder ver el resto de las cuentas de un contribuyente  
    private boolean verCuentaEnabled = false;		// Poder ver desgloses y unificaciones o cuentas relacionadas
    private boolean verConvenioEnabled = false; 	// Poder ver detalles de convenios
    private boolean verDetalleObjImpEnabled = false;
    private boolean verHistoricoContribEnabled = false;
    private boolean verCuentaDesgUnifEnabled = false;    
    private boolean buzonCambiosEnabled = false; 		// Permiso para ir al buzon de cambio de datos de persona.
    
    // Constructores
    public LiqConvenioSalPorCadAdapter(){
    	super(GdeSecurityConstants.LIQ_CONVENIOCUENTA);
    }
    
    //  Getters y Setters
	public LiqConvenioVO getConvenio() {
		return convenio;
	}

	public void setConvenio(LiqConvenioVO convenio) {
		this.convenio = convenio;
	}

	public LiqCuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(LiqCuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	
	public CasoContainer getCasoContainer() {
		return casoContainer;
	}
	public void setCasoContainer(CasoContainer casoContainer) {
		this.casoContainer = casoContainer;
	}

	public Date getFechaSaldo() {
		return fechaSaldo;
	}

	public void setFechaSaldo(Date fechaSaldo) {
		this.fechaSaldo = fechaSaldo;
		this.fechaSaldoView = DateUtil.formatDate(fechaSaldo, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getObservacion() {
		return Observacion;
	}

	public void setObservacion(String observacion) {
		Observacion = observacion;
	}

	public boolean isVerDeudaContribEnabled() {
		return verDeudaContribEnabled;
	}

	public void setVerDeudaContribEnabled(boolean verDeudaContribEnabled) {
		this.verDeudaContribEnabled = verDeudaContribEnabled;
	}

	public boolean isVerCuentaEnabled() {
		return verCuentaEnabled;
	}

	public void setVerCuentaEnabled(boolean verCuentaEnabled) {
		this.verCuentaEnabled = verCuentaEnabled;
	}

	public boolean isVerConvenioEnabled() {
		return verConvenioEnabled;
	}

	public void setVerConvenioEnabled(boolean verConvenioEnabled) {
		this.verConvenioEnabled = verConvenioEnabled;
	}

	public String getFechaSaldoView() {
		return fechaSaldoView;
	}
	public void setFechaSaldoView(String fechaSaldoView) {
		this.fechaSaldoView = fechaSaldoView;
	}

	public Boolean getEsVueltaAtras() {
		return esVueltaAtras;
	}
	public void setEsVueltaAtras(Boolean esVueltaAtras) {
		this.esVueltaAtras = esVueltaAtras;
	}

	public boolean isVerDetalleObjImpEnabled() {
		return verDetalleObjImpEnabled;
	}
	public void setVerDetalleObjImpEnabled(boolean verDetalleObjImpEnabled) {
		this.verDetalleObjImpEnabled = verDetalleObjImpEnabled;
	}

	public boolean isVerHistoricoContribEnabled() {
		return verHistoricoContribEnabled;
	}
	public void setVerHistoricoContribEnabled(boolean verHistoricoContribEnabled) {
		this.verHistoricoContribEnabled = verHistoricoContribEnabled;
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

	public boolean isEsAnulacion() {
		return esAnulacion;
	}

	public void setEsAnulacion(boolean esAnulacion) {
		this.esAnulacion = esAnulacion;
	}
	
}