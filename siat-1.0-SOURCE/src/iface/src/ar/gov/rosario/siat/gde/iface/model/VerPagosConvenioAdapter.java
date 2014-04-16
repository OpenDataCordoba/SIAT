//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del verPagosConvenio
 * 
 * @author tecso
 */
public class VerPagosConvenioAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "verPagosConvenioAdapterVO";
	
	private LiqCuentaVO cuenta = new LiqCuentaVO();
    
	private LiqConvenioVO convenio = new LiqConvenioVO();
   
    private String[] listIdCuotaSelected;
    
    private List<CuotaDeudaVO>listCuotaDeuda = new ArrayList<CuotaDeudaVO>();
    
    
    
      

    // ---> Propiedades para la asignacion de permisos
    private boolean verDeudaContribEnabled = false; // Poder ver el resto de las cuentas de un contribuyente  
    private boolean verCuentaEnabled = false;		// Poder ver desgloses y unificaciones o cuentas relacionadas
    private boolean verConvenioEnabled = false; 	// Poder ver detalles de convenios
    private boolean verCuentaDesgUnifEnabled = false;  	// Poder ver desgloses y unificaciones 
    private boolean verCuentaRelEnabled = false; 		// Poder Ver Cuentas Relacionadas al objeto Imponible
    private boolean verDetalleObjImpEnabled = false;    // Permiso para ver el Detalle del Objeto Imponible
    private boolean verHistoricoContribEnabled = false;	// Permiso para ver el Historico de los Contribuyentes de la cuenta
    private boolean buzonCambiosEnabled = false; 		// Permiso para ir al buzon de cambio de datos de persona.
    // ---> Propiedades para la asignacion de permisos
   
    
    // Constructores
    public VerPagosConvenioAdapter(){
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
	
	public String[] getListIdCuotaSelected() {
		return listIdCuotaSelected;
	}

	public void setListIdCuotaSelected(String[] listIdCuotaSelected) {
		this.listIdCuotaSelected = listIdCuotaSelected;
	}
	





	public List<CuotaDeudaVO> getListCuotaDeuda() {
		return listCuotaDeuda;
	}

	public void setListCuotaDeuda(List<CuotaDeudaVO> listCuotaDeuda) {
		this.listCuotaDeuda = listCuotaDeuda;
	}

	// Permisos
	public boolean isVerConvenioEnabled() {
		return verConvenioEnabled;
	}

	public void setVerConvenioEnabled(boolean verConvenioEnabled) {
		this.verConvenioEnabled = verConvenioEnabled;
	}

	public boolean isVerCuentaEnabled() {
		return verCuentaEnabled;
	}

	public void setVerCuentaEnabled(boolean verCuentaEnabled) {
		this.verCuentaEnabled = verCuentaEnabled;
	}

	public boolean isVerDeudaContribEnabled() {
		return verDeudaContribEnabled;
	}

	public void setVerDeudaContribEnabled(boolean verDeudaContribEnabled) {
		this.verDeudaContribEnabled = verDeudaContribEnabled;
	}

	public boolean isVerCuentaDesgUnifEnabled() {
		return verCuentaDesgUnifEnabled;
	}

	public void setVerCuentaDesgUnifEnabled(boolean verCuentaDesgUnifEnabled) {
		this.verCuentaDesgUnifEnabled = verCuentaDesgUnifEnabled;
	}

	public boolean isVerCuentaRelEnabled() {
		return verCuentaRelEnabled;
	}

	public void setVerCuentaRelEnabled(boolean verCuentaRelEnabled) {
		this.verCuentaRelEnabled = verCuentaRelEnabled;
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
	
	public boolean isBuzonCambiosEnabled() {
		return buzonCambiosEnabled;
	}
	public void setBuzonCambiosEnabled(boolean buzonCambiosEnabled) {
		this.buzonCambiosEnabled = buzonCambiosEnabled;
	}
	
}