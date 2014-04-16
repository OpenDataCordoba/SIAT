//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

public class EstadoCuentaAdapter extends SiatAdapterModel {

	public static final String NAME = "estadoCuentaAdapterVO";
	private static final long serialVersionUID = 1L;
	
	private LiqCuentaVO cuenta;
	private List<LiqDeudaPagoDeudaVO> listDeudaPagoDeuda = new ArrayList<LiqDeudaPagoDeudaVO>();
	  
	private List<LiqCuentaVO> listCuentaRel = new ArrayList<LiqCuentaVO>();
	private LiqExencionesVO exenciones = new LiqExencionesVO();
	
	private String propietario ="";
	private String fechaVtoDesde="";
	private String fechaVtoHasta="";
	
    // Propiedades para la asignacion de permisos
    private boolean verDeudaContribEnabled = false;    	// Poder ver el resto de las cuentas de un contribuyente  
    private boolean verCuentaEnabled = false;			// Ver Cuenta desde  
    private boolean verCuentaDesgUnifEnabled = false;  	// Poder ver desgloses y unificaciones 
    private boolean verCuentaRelEnabled = false; 		// Poder Ver Cuentas Relacionadas al objeto Imponible
    private boolean verConvenioEnabled = false; 		// Poder ver detalles de convenios
    private boolean verDetalleObjImpEnabled = false;    // Permiso para ver el Detalle del Objeto Imponible
    private boolean verHistoricoContribEnabled = false;	// Permiso para ver el Historico de los Contribuyentes de la cuenta
    private boolean buzonCambiosEnabled = false; 		// Permiso para ir al buzon de cambio de datos de persona.
    
    
    // Constructores
	public EstadoCuentaAdapter() {
		super(GdeSecurityConstants.CONSULTAR_ESTADOCUENTA);
	}

	/**
	 * 
	 */
	
    //  Getters y Setters
	public LiqCuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(LiqCuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public boolean getVerConvenioEnabled() {
		return verConvenioEnabled;
	}

	public void setVerConvenioEnabled(boolean verConvenioEnabled) {
		this.verConvenioEnabled = verConvenioEnabled;
	}

	public boolean getVerCuentaDesgUnifEnabled() {
		return verCuentaDesgUnifEnabled;
	}

	public void setVerCuentaDesgUnifEnabled(boolean verCuentaDesgUnifEnabled) {
		this.verCuentaDesgUnifEnabled = verCuentaDesgUnifEnabled;
	}

	public boolean getVerCuentaEnabled() {
		return verCuentaEnabled;
	}

	public void setVerCuentaEnabled(boolean verCuentaEnabled) {
		this.verCuentaEnabled = verCuentaEnabled;
	}

	public boolean getVerCuentaRelEnabled() {
		return verCuentaRelEnabled;
	}

	public void setVerCuentaRelEnabled(boolean verCuentaRelEnabled) {
		this.verCuentaRelEnabled = verCuentaRelEnabled;
	}

	public boolean getVerDeudaContribEnabled() {
		return verDeudaContribEnabled;
	}

	public void setVerDeudaContribEnabled(boolean verDeudaContribEnabled) {
		this.verDeudaContribEnabled = verDeudaContribEnabled;
	}

	public List<LiqCuentaVO> getListCuentaRel() {
		return listCuentaRel;
	}

	public void setListCuentaRel(List<LiqCuentaVO> listCuentaRel) {
		this.listCuentaRel = listCuentaRel;
	}

	public List<LiqDeudaPagoDeudaVO> getListDeudaPagoDeuda() {
		return listDeudaPagoDeuda;
	}

	public void setListDeudaPagoDeuda(List<LiqDeudaPagoDeudaVO> listDeudaPagoDeuda) {
		this.listDeudaPagoDeuda = listDeudaPagoDeuda;
	}

	public String getPropietario() {
		return propietario;
	}
	public void setPropietario(String propietario) {
		this.propietario = propietario;
	}

	public LiqExencionesVO getExenciones() {
		return exenciones;
	}
	public void setExenciones(LiqExencionesVO liqExenciones) {
		this.exenciones = liqExenciones;
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

	public String getFechaVtoDesde() {
		return fechaVtoDesde;
	}

	public void setFechaVtoDesde(String fechaVtoDesde) {
		this.fechaVtoDesde = fechaVtoDesde;
	}

	public String getFechaVtoHasta() {
		return fechaVtoHasta;
	}

	public void setFechaVtoHasta(String fechaVtoHasta) {
		this.fechaVtoHasta = fechaVtoHasta;
	}

	//View getters

}
