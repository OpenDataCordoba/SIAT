//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.LocalidadVO;
import ar.gov.rosario.siat.rod.iface.util.RodSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del TramiteRA
 * 
 * @author tecso
 */
public class TramiteRAAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "tramiteRAAdapterVO";
	
    private TramiteRAVO tramiteRA = new TramiteRAVO();
    private boolean esPropActual = false;
    
    private List<TipoFabricacionVO> listTipoFabricacion = new ArrayList<TipoFabricacionVO>();
    private List<TipoVehiculoVO> listTipoVehiculo = new ArrayList<TipoVehiculoVO>();
    private List<TipoCargaVO> listTipoCarga = new ArrayList<TipoCargaVO>();
    private List<TipoDocVO> listTipoDoc = new ArrayList<TipoDocVO>();
    private List<EstadoCivilVO> listEstadoCivil = new ArrayList<EstadoCivilVO>();
    private List<TipoMotorVO> listTipoMotor = new ArrayList<TipoMotorVO>();
    private List<TipoPagoVO> listTipoPago = new ArrayList<TipoPagoVO>();
    private List<TipoTramiteVO> listTipoTramite = new ArrayList<TipoTramiteVO>();
    private List<ModeloVO> listModelo = new ArrayList<ModeloVO>();
    private List<LocalidadVO> listLocalidad = new ArrayList<LocalidadVO>();
    private List<TipoUsoVO> listTipoUso = new ArrayList<TipoUsoVO>();
    private List<TipoPropietarioVO> listTipoPropietario = new ArrayList<TipoPropietarioVO>();
    private List<TipoMotorVO> listETipoMotor = new ArrayList<TipoMotorVO>();    

	private List<SiNo>   listSiNo = SiNo.getList(SiNo.OpcionSelecionar);  
	   // es bis o no utilizada en domicilio
	
	// cambio de estado
	private List<EstadoTramiteRAVO> listEstTramiteRA = new ArrayList<EstadoTramiteRAVO>();
	private Long idHisEstTra = new Long(-1) ;
	
	// para el reporte
    private List<TipoTramiteVO> listTipoTramite1 = new ArrayList<TipoTramiteVO>();
    private List<TipoTramiteVO> listTipoTramite2 = new ArrayList<TipoTramiteVO>();
    private Integer sizeLista;
    private Integer sizeLista1;
    private Integer sizeLista2;
    
    private boolean validarDatoDomicilio= false;
	
	// Constructores
    public TramiteRAAdapter(){
    	super(RodSecurityConstants.ABM_TRAMITERA);
    }
    
    //  Getters y Setters
	public TramiteRAVO getTramiteRA() {
		return tramiteRA;
	}

	public void setTramiteRA(TramiteRAVO tramiteRAVO) {
		this.tramiteRA = tramiteRAVO;
	}

	public String getName(){
		return NAME;
	}
	

    public List<TipoFabricacionVO> getListTipoFabricacion() {
		return listTipoFabricacion;
	}

	public void setListTipoFabricacion(List<TipoFabricacionVO> listTipoFabricacion) {
		this.listTipoFabricacion = listTipoFabricacion;
	}

	public List<TipoVehiculoVO> getListTipoVehiculo() {
		return listTipoVehiculo;
	}

	public void setListTipoVehiculo(List<TipoVehiculoVO> listTipoVehiculo) {
		this.listTipoVehiculo = listTipoVehiculo;
	}

	public List<TipoCargaVO> getListTipoCarga() {
		return listTipoCarga;
	}

	public void setListTipoCarga(List<TipoCargaVO> listTipoCarga) {
		this.listTipoCarga = listTipoCarga;
	}

	public List<TipoDocVO> getListTipoDoc() {
		return listTipoDoc;
	}

	public void setListTipoDoc(List<TipoDocVO> listTipoDoc) {
		this.listTipoDoc = listTipoDoc;
	}

	public List<EstadoCivilVO> getListEstadoCivil() {
		return listEstadoCivil;
	}

	public void setListEstadoCivil(List<EstadoCivilVO> listEstadoCivil) {
		this.listEstadoCivil = listEstadoCivil;
	}

	public List<TipoMotorVO> getListTipoMotor() {
		return listTipoMotor;
	}

	public void setListTipoMotor(List<TipoMotorVO> listTipoMotor) {
		this.listTipoMotor = listTipoMotor;
	}

	public List<TipoPagoVO> getListTipoPago() {
		return listTipoPago;
	}

	public void setListTipoPago(List<TipoPagoVO> listTipoPago) {
		this.listTipoPago = listTipoPago;
	}

	public List<TipoTramiteVO> getListTipoTramite() {
		return listTipoTramite;
	}

	public void setListTipoTramite(List<TipoTramiteVO> listTipoTramite) {
		this.listTipoTramite = listTipoTramite;
	}
	
	public List<ModeloVO> getListModelo() {
		return listModelo;
	}

	public void setListModelo(List<ModeloVO> listModelo) {
		this.listModelo = listModelo;
	}

	public List<LocalidadVO> getListLocalidad() {
		return listLocalidad;
	}

	public void setListLocalidad(List<LocalidadVO> listLocalidad) {
		this.listLocalidad = listLocalidad;
	}

	public List<TipoUsoVO> getListTipoUso() {
		return listTipoUso;
	}

	public void setListTipoUso(List<TipoUsoVO> listTipoUso) {
		this.listTipoUso = listTipoUso;
	}

	public List<TipoPropietarioVO> getListTipoPropietario() {
		return listTipoPropietario;
	}

	public void setListTipoPropietario(List<TipoPropietarioVO> listTipoPropietario) {
		this.listTipoPropietario = listTipoPropietario;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public List<EstadoTramiteRAVO> getListEstTramiteRA() {
		return listEstTramiteRA;
	}

	public void setListEstTramiteRA(List<EstadoTramiteRAVO> listEstTramiteRA) {
		this.listEstTramiteRA = listEstTramiteRA;
	}

	public List<TipoMotorVO> getListETipoMotor() {
		return listETipoMotor;
	}

	public void setListETipoMotor(List<TipoMotorVO> listETipoMotor) {
		this.listETipoMotor = listETipoMotor;
	}

	public boolean isEsPropActual() {
		return esPropActual;
	}

	public void setEsPropActual(boolean esPropActual) {
		this.esPropActual = esPropActual;
	}

	public List<TipoTramiteVO> getListTipoTramite1() {
		return listTipoTramite1;
	}

	public void setListTipoTramite1(List<TipoTramiteVO> listTipoTramite1) {
		this.listTipoTramite1 = listTipoTramite1;
	}

	public List<TipoTramiteVO> getListTipoTramite2() {
		return listTipoTramite2;
	}

	public void setListTipoTramite2(List<TipoTramiteVO> listTipoTramite2) {
		this.listTipoTramite2 = listTipoTramite2;
	}

	public Integer getSizeLista() {
		return sizeLista;
	}

	public void setSizeLista(Integer sizeLista) {
		this.sizeLista = sizeLista;
	}

	public Integer getSizeLista1() {
		return sizeLista1;
	}

	public void setSizeLista1(Integer sizeLista1) {
		this.sizeLista1 = sizeLista1;
	}

	public Integer getSizeLista2() {
		return sizeLista2;
	}

	public void setSizeLista2(Integer sizeLista2) {
		this.sizeLista2 = sizeLista2;
	}

	public Long getIdHisEstTra() {
		return idHisEstTra;
	}

	public void setIdHisEstTra(Long idHisEstTra) {
		this.idHisEstTra = idHisEstTra;
	}

	public boolean isValidarDatoDomicilio() {
		return validarDatoDomicilio;
	}

	public void setValidarDatoDomicilio(boolean validarDatoDomicilio) {
		this.validarDatoDomicilio = validarDatoDomicilio;
	}

	// Metodos para la seguridad en la vista de los propietarios actuales
	public String getVerPropietarioActualEnabled() {
		return SiatBussImageModel.hasEnabledFlag(RodSecurityConstants.ABM_PROPIETARIO, BaseSecurityConstants.VER);
	}
	
	public String getModificarPropietarioActualEnabled() {
		return SiatBussImageModel.hasEnabledFlag(RodSecurityConstants.ABM_PROPIETARIO, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarPropietarioActualEnabled() {
		return SiatBussImageModel.hasEnabledFlag(RodSecurityConstants.ABM_PROPIETARIO, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarPropietarioActualEnabled() {
		return SiatBussImageModel.hasEnabledFlag(RodSecurityConstants.ABM_PROPIETARIO, BaseSecurityConstants.AGREGAR);
	}
	public String getMarcarPrincipalEnabled() {
		return SiatBussImageModel.hasEnabledFlag(RodSecurityConstants.ABM_PROPIETARIO, RodSecurityConstants.MTD_MARCAR_PRINCIPAL);
	}

	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de TramiteRA");     
		 report.setReportBeanName("TramiteRA");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportTramiteRA = new ReportVO();
		 reportTramiteRA.setReportTitle("Datos del TramiteRA");
		 // carga de datos
	     
	     //Código
		 reportTramiteRA.addReportDato("Código", "codTramiteRA");
		 //Descripción
		 reportTramiteRA.addReportDato("Descripción", "desTramiteRA");
	     
		 report.getListReport().add(reportTramiteRA);
	
	}
	
	// View getters
}