//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Adapter del LiqConvenioCuenta
 * 
 * @author tecso
 */
public class LiqConvenioCuotaSaldoAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "liqConvenioCuotaSaldoAdapterVO";
	
	private LiqCuentaVO cuenta = new LiqCuentaVO();
    
	private LiqConvenioVO convenio = new LiqConvenioVO();
   
    private List<Date> listFechas = new ArrayList<Date>();
    
    private Boolean tieneCuotasVencidas=false;
    
    private Date fechaCuota=new Date();
    
    private String Observacion="";
    
    private String fechaCuotaView="";
    
    private Boolean esVueltaAtras=false;
    
    private Integer cuotaDesde;
    
    private String cuotaDesdeView="";

    // Propiedades para la asignacion de permisos
    private boolean verDeudaContribEnabled = false; // Poder ver el resto de las cuentas de un contribuyente  
    private boolean verCuentaEnabled = false;		// Poder ver desgloses y unificaciones o cuentas relacionadas
    private boolean verConvenioEnabled = false; 	// Poder ver detalles de convenios
    private boolean verDetalleObjImpEnabled = false;    // Permiso para ver el Detalle del Objeto Imponible
    private boolean verHistoricoContribEnabled = false;	// Permiso para ver el Historico de los Contribuyentes de la cuenta
    private boolean verCuentaDesgUnifEnabled = false;  	// Permiso para ver desgloses y unificaciones 
    private boolean verCuentaRelEnabled = false; 		// Permiso para Ver Cuentas Relacionadas al objeto Imponible
    private boolean buzonCambiosEnabled = false; 		// Permiso para ir al buzon de cambio de datos de persona.
    
    
    // Constructores
    public LiqConvenioCuotaSaldoAdapter(){
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
	

	public Date getFechaCuota() {
		return fechaCuota;
	}

	public void setFechaCuota(Date fechaCuota) {
		this.fechaCuota = fechaCuota;
		this.fechaCuotaView = DateUtil.formatDate(fechaCuota, DateUtil.ddSMMSYYYY_MASK);
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

	public String getFechaCuotaView() {
		return fechaCuotaView;
	}

	public void setFechaCuotaView(String fechaCuotaView) {
		this.fechaCuotaView = fechaCuotaView;
	}

	public Boolean getEsVueltaAtras() {
		return esVueltaAtras;
	}

	public void setEsVueltaAtras(Boolean esVueltaAtras) {
		this.esVueltaAtras = esVueltaAtras;
	}


	
	public Integer getCuotaDesde() {
		return cuotaDesde;
	}
	public void setCuotaDesde(Integer cuotaDesde) {
		this.cuotaDesde = cuotaDesde;
		this.cuotaDesdeView = StringUtil.formatInteger(cuotaDesde);
	}

	public List<Date> getListFechas() {
		return listFechas;
	}
	public void setListFechas(List<Date> listFechas) {
		this.listFechas = listFechas;
	}
	
	public Boolean getTieneCuotasVencidas() {
		return tieneCuotasVencidas;
	}
	public void setTieneCuotasVencidas(Boolean tieneCuotasVencidas) {
		this.tieneCuotasVencidas = tieneCuotasVencidas;
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
	
	public boolean isVerCuentaRelEnabled() {
		return verCuentaRelEnabled;
	}
	public void setVerCuentaRelEnabled(boolean verCuentaRelEnabled) {
		this.verCuentaRelEnabled = verCuentaRelEnabled;
	}
	public boolean isBuzonCambiosEnabled() {
		return buzonCambiosEnabled;
	}
	public void setBuzonCambiosEnabled(boolean buzonCambiosEnabled) {
		this.buzonCambiosEnabled = buzonCambiosEnabled;
	}
	
	
	//View Getters
	public List<String> getListFechasView() {
		List<String> diasStr = new ArrayList<String>();
		for(Date fecha: listFechas){
			diasStr.add(DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK));
		}
		return diasStr;
	}
	
	public String getCuotaDesdeView() {
		return cuotaDesdeView;
	}

	public void setCuotaDesdeView(String cuotaDesdeView) {
		this.cuotaDesdeView = cuotaDesdeView;
	}


	
}