//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.cas.iface.model.CasoContainer;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;

public class LiqReconfeccionAdapter extends SiatAdapterModel {

	public static final String NAME = "liqReconfeccionAdapterVO";
	private static final long serialVersionUID = 1L;
	
	private LiqCuentaVO cuenta;
	private LiqCuentaVO cuentaFilter;
	
	private ProcedimientoVO procedimiento;
	
	private List<LiqDeudaVO> listDeuda = new ArrayList<LiqDeudaVO>();
	private List<Date> listFechasReconf = new ArrayList<Date>();
	private List<LiqReciboVO> listRecibos = new ArrayList<LiqReciboVO>(); // Lista de recibos generados por la reconfeccion
	private List<LiqCuotaVO> listCuotas = new ArrayList<LiqCuotaVO>();
	private List<LiqCuentaVO> listCuentaRel = new ArrayList<LiqCuentaVO>();
	private List<TipDecJurRecVO> listTipDecJurRec = new ArrayList<TipDecJurRecVO>();
	private LiqExencionesVO exenciones = new LiqExencionesVO();
	private LiqConvenioVO convenio = new LiqConvenioVO();
	private DecJurVO decJur = new DecJurVO();
	private String fechaReconfSelected;
	private Boolean esReimpresionCuotas = false;
	private Boolean esCuotaSaldo=false;
	private Integer cuotaDesCuoSal;
	private boolean tieneDeudaVencida = false;
	private boolean esReconfeccionEspecial = false;
	
    // Propiedades para la asignacion de permisos
    private boolean verDeudaContribEnabled = false;    // Poder ver el resto de las cuentas de un contribuyente  
    private boolean verCuentaEnabled = false;	// Ver Cuenta desde  
    private boolean verCuentaDesgUnifEnabled = false;  // Poder ver desgloses y unificaciones 
    private boolean verCuentaRelEnabled = false; // Poder Ver Cuentas Relacionadas al objeto Imponible
    private boolean verConvenioEnabled = false; 	// Poder ver detalles de convenios
    private boolean verDetalleObjImpEnabled = false;    // Permiso para ver el Detalle del Objeto Imponible
    private boolean verHistoricoContribEnabled = false;	// Permiso para ver el Historico de los Contribuyentes de la cuenta
    private boolean buzonCambiosEnabled = false; 		// Permiso para ir al buzon de cambio de datos de persona.
    private boolean verMensajeCaducidad = false;
    private boolean recEnBlanco=false;
    private String mensajeCaducidad = "";

	private Date fechaVencimientoEsp = new Date();
	private Date fechaActualizacionEsp = new Date();
	private CasoContainer casoContainer = new CasoContainer();
	private String fechaVencimientoEspView = "";
	private String fechaActualizacionEspView = "";
	private boolean reconfeccionAutoliquidable=false;
	
	// Para la generacion del volante de pago de Intereses de RS
	private Date fechaPago = new Date();
	private String fechaPagoView = "";
	
    // Constructores
	public LiqReconfeccionAdapter() {
		super(GdeSecurityConstants.MTD_RECONFECCIONAR);
		// TODO Auto-generated constructor stub
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

	public List<LiqDeudaVO> getListDeuda() {
		return listDeuda;
	}

	public void setListDeuda(List<LiqDeudaVO> listDeuda) {
		this.listDeuda = listDeuda;
	}

	public List<Date> getListFechasReconf() {
		return listFechasReconf;
	}

	public void setListFechasReconf(List<Date> listFechasReconf) {
		this.listFechasReconf = listFechasReconf;
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

	public boolean isEsReconfeccionEspecial() {
		return esReconfeccionEspecial;
	}

	public void setEsReconfeccionEspecial(boolean esReconfeccionEspecial) {
		this.esReconfeccionEspecial = esReconfeccionEspecial;
	}

	public ProcedimientoVO getProcedimiento() {
		return procedimiento;
	}

	public void setProcedimiento(ProcedimientoVO procedimiento) {
		this.procedimiento = procedimiento;
	}

	public LiqCuentaVO getCuentaFilter() {
		return cuentaFilter;
	}
	public void setCuentaFilter(LiqCuentaVO cuentaFilter) {
		this.cuentaFilter = cuentaFilter;
	}

	//View getters
	public List<String> getListFechasReconfView() {
		List<String> diasStr = new ArrayList<String>();
		for(Date fecha: listFechasReconf){
			diasStr.add(DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK));
		}
		return diasStr;
	}

	public boolean isTieneDeudaVencida() {
		return tieneDeudaVencida;
	}

	public void setTieneDeudaVencida(boolean tieneDeudaVencida) {
		this.tieneDeudaVencida = tieneDeudaVencida;
	}

	public boolean isBuzonCambiosEnabled() {
		return buzonCambiosEnabled;
	}
	public void setBuzonCambiosEnabled(boolean buzonCambiosEnabled) {
		this.buzonCambiosEnabled = buzonCambiosEnabled;
	}

	public boolean isVerMensajeCaducidad() {
		return verMensajeCaducidad;
	}

	public void setVerMensajeCaducidad(boolean verMensajeCaducidad) {
		this.verMensajeCaducidad = verMensajeCaducidad;
	}

	public String getMensajeCaducidad() {
		return mensajeCaducidad;
	}

	public void setMensajeCaducidad(String mensajeCaducidad) {
		this.mensajeCaducidad = mensajeCaducidad;
	}
	
	public CasoContainer getCasoContainer() {
		return casoContainer;
	}

	public void setCasoContainer(CasoContainer casoContainer) {
		this.casoContainer = casoContainer;
	}

	public boolean isRecEnBlanco() {
		return recEnBlanco;
	}

	public void setRecEnBlanco(boolean recEnBlanco) {
		this.recEnBlanco = recEnBlanco;
	}

	public DecJurVO getDecJur() {
		return decJur;
	}

	public void setDecJur(DecJurVO decJur) {
		this.decJur = decJur;
	}

	public Date getFechaActualizacionEsp() {
		return fechaActualizacionEsp;
	}

	public void setFechaActualizacionEsp(Date fechaActualizacionEsp) {
		this.fechaActualizacionEsp = fechaActualizacionEsp;
		this.fechaActualizacionEspView = DateUtil.formatDate(fechaActualizacionEsp, DateUtil.ddSMMSYYYY_MASK);

	}

	public Date getFechaVencimientoEsp() {
		return fechaVencimientoEsp;
	}

	public void setFechaVencimientoEsp(Date fechaVecimientoEsp) {
		this.fechaVencimientoEsp = fechaVecimientoEsp;
		this.fechaVencimientoEspView = DateUtil.formatDate(fechaVecimientoEsp, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaActualizacionEspView() {
		return fechaActualizacionEspView;
	}

	public void setFechaActualizacionEspView(
			String fechaActualizacionEspView) {
		this.fechaActualizacionEspView = fechaActualizacionEspView;
	}
	public String getFechaVencimientoEspView() {
		return fechaVencimientoEspView;
	}

	public void setFechaVencimientoEspView(String fechaVecimientoEspView) {
		this.fechaVencimientoEspView = fechaVecimientoEspView;
	}

	public List<TipDecJurRecVO> getListTipDecJurRec() {
		return listTipDecJurRec;
	}

	public void setListTipDecJurRec(List<TipDecJurRecVO> listTipDecJurRec) {
		this.listTipDecJurRec = listTipDecJurRec;
	}

	public String reconfEspInfoString(boolean verbose) {
		String ret = String.format("Reconf. Esp: fechaVencimiento: %s, fechaActualizacion: %s",
				getFechaVencimientoEspView(),
				getFechaActualizacionEspView());
		if (getCasoContainer().getCaso() != null) {
			ret += ", Expediente: " + getCasoContainer().getCaso().getCasoView();		
		}
		return ret;
	}

	public boolean isReconfeccionAutoliquidable() {
		return reconfeccionAutoliquidable;
	}

	public void setReconfeccionAutoliquidable(boolean reconfeccionAutoliquidable) {
		this.reconfeccionAutoliquidable = reconfeccionAutoliquidable;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public String getFechaPagoView() {
		return fechaPagoView;
	}

	public void setFechaPagoView(String fechaPagoView) {
		this.fechaPagoView = fechaPagoView;
	}
	
}
