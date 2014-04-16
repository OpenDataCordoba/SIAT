//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Adapter de Habilitacion
 * 
 * @author tecso
 */
public class HabilitacionAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "habilitacionAdapterVO";
	public static final String ENC_NAME = "encHabilitacionAdapterVO";
	public static final String EMI_NAME = "emiHabilitacionAdapterVO";
	
	private HabilitacionVO habilitacion = new HabilitacionVO();
	
	private String obsCambioEstado = "";
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<TipoCobroVO> listTipoCobro = new ArrayList<TipoCobroVO>();
	private List<TipoHabVO> listTipoHab = new ArrayList<TipoHabVO>();
	private List<TipoEventoVO> listTipoEvento = new ArrayList<TipoEventoVO>();
	private List<HabExeVO> listHabExe = new ArrayList<HabExeVO>();
	
	private List<ValoresCargadosVO> listValoresCargados = new ArrayList<ValoresCargadosVO>();
	
	private List<LugarEventoVO> listLugarEvento = new ArrayList<LugarEventoVO>();
	private List<ClaHab> listClaHab = new ArrayList<ClaHab>();
	private List<ClaOrg> listClaOrg = new ArrayList<ClaOrg>();
	
	private List<EstHabVO> listEstHab = new ArrayList<EstHabVO>();
	
	// para mostrar en reporte
	private List<EntHabVO> listEntHab = new ArrayList<EntHabVO>();
	private Date fechaEmision ;
	private String fechaEmisionView = ""; 

	
	// Flags
	private String paramTipoHab = "EXT";
	private boolean poseeDatosPersona = false; 
	private boolean paramAdvertencia = false;
	
	private String cuentaPoseeDeuda = "false";
	private String esTipoHabInterna = "false";
	
	private Integer importe;
	private Date fechaVencimiento;
	
	private String importeView="";
	private String fechaVencimientoView="";
	
	private boolean emisionInicialBussEnabled = false;
	
	public HabilitacionAdapter(){
		super(EspSecurityConstants.ABM_HABILITACION);
		ACCION_MODIFICAR_ENCABEZADO = EspSecurityConstants.ABM_HABILITACION_ENC;
	}

	// Getters & Setters
	public HabilitacionVO getHabilitacion() {
		return habilitacion;
	}
	public void setHabilitacion(HabilitacionVO habilitacion) {
		this.habilitacion = habilitacion;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public List<TipoCobroVO> getListTipoCobro() {
		return listTipoCobro;
	}
	public void setListTipoCobro(List<TipoCobroVO> listTipoCobro) {
		this.listTipoCobro = listTipoCobro;
	}
	public List<TipoHabVO> getListTipoHab() {
		return listTipoHab;
	}
	public void setListTipoHab(List<TipoHabVO> listTipoHab) {
		this.listTipoHab = listTipoHab;
	}
	public String getParamTipoHab() {
		return paramTipoHab;
	}
	public void setParamTipoHab(String paramTipoHab) {
		this.paramTipoHab = paramTipoHab;
	}
	public List<ValoresCargadosVO> getListValoresCargados() {
		return listValoresCargados;
	}
	public void setListValoresCargados(List<ValoresCargadosVO> listValoresCargados) {
		this.listValoresCargados = listValoresCargados;
	}
	public boolean isPoseeDatosPersona() {
		return poseeDatosPersona;
	}
	public void setPoseeDatosPersona(boolean poseeDatosPersona) {
		this.poseeDatosPersona = poseeDatosPersona;
	}
	public List<TipoEventoVO> getListTipoEvento() {
		return listTipoEvento;
	}

	public void setListTipoEvento(List<TipoEventoVO> listTipoEvento) {
		this.listTipoEvento = listTipoEvento;
	}
	
	public List<HabExeVO> getListHabExe() {
		return listHabExe;
	}

	public void setListHabExe(List<HabExeVO> listHabExe) {
		this.listHabExe = listHabExe;
	}

	public Integer getImporte() {
		return importe;
	}

	public void setImporte(Integer importe) {
		this.importe = importe;
	}

	public String getImporteView() {
		return importeView;
	}

	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getFechaVencimientoView() {
		return fechaVencimientoView;
	}

	public void setFechaVencimientoView(String fechaVencimientoView) {
		this.fechaVencimientoView = fechaVencimientoView;
	}

	public List<EntHabVO> getListEntHab() {
		return listEntHab;
	}

	public void setListEntHab(List<EntHabVO> listEntHab) {
		this.listEntHab = listEntHab;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
		this.fechaEmisionView = DateUtil.formatDate(fechaEmision, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaEmisionView() {
		return fechaEmisionView;
	}

	public void setFechaEmisionView(String fechaEmisionView) {
		this.fechaEmisionView = fechaEmisionView;
	}

	public List<ClaHab> getListClaHab() {
		return listClaHab;
	}

	public void setListClaHab(List<ClaHab> listClaHab) {
		this.listClaHab = listClaHab;
	}

	public List<ClaOrg> getListClaOrg() {
		return listClaOrg;
	}

	public void setListClaOrg(List<ClaOrg> listClaOrg) {
		this.listClaOrg = listClaOrg;
	}

	public List<LugarEventoVO> getListLugarEvento() {
		return listLugarEvento;
	}

	public void setListLugarEvento(List<LugarEventoVO> listLugarEvento) {
		this.listLugarEvento = listLugarEvento;
	}

	public boolean isParamAdvertencia() {
		return paramAdvertencia;
	}

	public void setParamAdvertencia(boolean paramAdvertencia) {
		this.paramAdvertencia = paramAdvertencia;
	}

	public String getCuentaPoseeDeuda() {
		return cuentaPoseeDeuda;
	}

	public void setCuentaPoseeDeuda(String cuentaPoseeDeuda) {
		this.cuentaPoseeDeuda = cuentaPoseeDeuda;
	}

	public String getEsTipoHabInterna() {
		return esTipoHabInterna;
	}

	public void setEsTipoHabInterna(String esTipoHabInterna) {
		this.esTipoHabInterna = esTipoHabInterna;
	}

	public String getObsCambioEstado() {
		return obsCambioEstado;
	}

	public void setObsCambioEstado(String obsCambioEstado) {
		this.obsCambioEstado = obsCambioEstado;
	}

	public List<EstHabVO> getListEstHab() {
		return listEstHab;
	}

	public void setListEstHab(List<EstHabVO> listEstHab) {
		this.listEstHab = listEstHab;
	}

	// Permisos para Emision Inicial
	public boolean isEmisionInicialBussEnabled() {
		return emisionInicialBussEnabled;
	}
	public void setEmisionInicialBussEnabled(boolean emisionInicialBussEnabled) {
		this.emisionInicialBussEnabled = emisionInicialBussEnabled;
	}
	public String getEmisionInicialEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_HABILITACION, EspSecurityConstants.EMISION_INICIAL);
	}

	// Permisos para ABM PrecioEvento
	public String getVerPrecioEventoEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_PRECIOEVENTO, BaseSecurityConstants.VER);
	}
	public String getModificarPrecioEventoEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_PRECIOEVENTO, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarPrecioEventoEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_PRECIOEVENTO, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarPrecioEventoEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_PRECIOEVENTO, BaseSecurityConstants.AGREGAR);
	}
	
	//	 Permisos para ABM EntHab
	public String getVerEntHabEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_ENTHAB, BaseSecurityConstants.VER);
	}
	public String getModificarEntHabEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_ENTHAB, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarEntHabEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_ENTHAB, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarEntHabEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_ENTHAB, BaseSecurityConstants.AGREGAR);
	}
	
	//	 Permisos para ABM EntVen
	public String getVerEntVenEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_ENTVEN, BaseSecurityConstants.VER);
	}
	public String getModificarEntVenEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_ENTVEN, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarEntVenEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_ENTVEN, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarEntVenEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_ENTVEN, BaseSecurityConstants.AGREGAR);
	}
	
	//	 Permisos para ABM HabExe
	public String getVerHabExeEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_HABEXE, BaseSecurityConstants.VER);
	}
	public String getModificarHabExeEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_HABEXE, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarHabExeEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_HABEXE, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarHabExeEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_HABEXE, BaseSecurityConstants.AGREGAR);
	}
}
