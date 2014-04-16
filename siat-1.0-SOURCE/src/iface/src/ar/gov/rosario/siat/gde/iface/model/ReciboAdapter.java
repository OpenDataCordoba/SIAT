//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.TipoRecibo;

/**
 * Adapter para la visualizacion de Recibos de Deuda o Recibos de Cuotas.
 * 
 * @author Tecso
 */
public class ReciboAdapter extends SiatAdapterModel {

	public static final String NAME = "reciboAdapterVO";
	
	private TipoRecibo tipoRecibo;
	
	private LiqCuentaVO cuenta;
	private List<LiqDeudaVO> listDeuda = new ArrayList<LiqDeudaVO>();
	private List<LiqCuentaVO> listCuentaRel = new ArrayList<LiqCuentaVO>();
	private LiqExencionesVO exenciones = new LiqExencionesVO();
	private LiqConvenioVO convenio = new LiqConvenioVO();

	private List<LiqReciboVO> listRecibos = new ArrayList<LiqReciboVO>();
	private List<LiqCuotaVO> listCuotas = new ArrayList<LiqCuotaVO>();
	
	@Deprecated
	private String fechaReconfSelected;
	@Deprecated
	private Boolean esReimpresionCuotas = false;
	@Deprecated
	private Boolean esCuotaSaldo=false;
	@Deprecated
	private Integer cuotaDesCuoSal;
		
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
	public ReciboAdapter() {
		super(GdeSecurityConstants.CONSULTAR_RECIBO);
	}

    //  Getters y Setters
	public LiqCuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(LiqCuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public List<LiqDeudaVO> getListDeuda() {
		return listDeuda;
	}

	public void setListDeuda(List<LiqDeudaVO> listDeuda) {
		this.listDeuda = listDeuda;
	}

	public List<LiqReciboVO> getListRecibos() {
		return listRecibos;
	}

	public void setListRecibos(List<LiqReciboVO> listRecibos) {
		this.listRecibos = listRecibos;
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

	public LiqExencionesVO getExenciones() {
		return exenciones;
	}

	public void setExenciones(LiqExencionesVO exenciones) {
		this.exenciones = exenciones;
	}


	public String getFechaReconfSelected() {
		return fechaReconfSelected;
	}

	public void setFechaReconfSelected(String fechaReconfSelected) {
		this.fechaReconfSelected = fechaReconfSelected;
	}
	
	public List<LiqCuotaVO> getListCuotas() {
		return listCuotas;
	}

	public void setListCuotas(List<LiqCuotaVO> listCuotas) {
		this.listCuotas = listCuotas;
	}

	
	public LiqConvenioVO getConvenio() {
		return convenio;
	}

	public void setConvenio(LiqConvenioVO convenio) {
		this.convenio = convenio;
	}

	public Boolean getEsReimpresionCuotas() {
		return esReimpresionCuotas;
	}

	public void setEsReimpresionCuotas(Boolean esReimpresionCuotas) {
		this.esReimpresionCuotas = esReimpresionCuotas;
	}
	
	
	public Boolean getEsCuotaSaldo() {
		return esCuotaSaldo;
	}

	public void setEsCuotaSaldo(Boolean esCuotaSaldo) {
		this.esCuotaSaldo = esCuotaSaldo;
	}
	
	public Integer getCuotaDesCuoSal() {
		return cuotaDesCuoSal;
	}
	public void setCuotaDesCuoSal(Integer cuotaDesCuoSal) {
		this.cuotaDesCuoSal = cuotaDesCuoSal;
	}
	
	public TipoRecibo getTipoRecibo() {
		return tipoRecibo;
	}
	public void setTipoRecibo(TipoRecibo tipoRecibo) {
		this.tipoRecibo = tipoRecibo;
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
	//View getters

	
}
