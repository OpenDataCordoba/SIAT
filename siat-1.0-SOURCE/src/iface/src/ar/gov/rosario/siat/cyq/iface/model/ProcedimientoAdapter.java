//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del Procedimiento
 * 
 * @author tecso
 */
public class ProcedimientoAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "procedimientoAdapterVO";
	public static final String ENC_NAME = "encProcedimientoAdapterVO";
	public static final String CAMBIOESTADO_NAME = "cambioEstadoProcedimientoAdapterVO";
	
    private ProcedimientoVO procedimiento = new ProcedimientoVO();
    
	private List<TipoProcesoVO> listTipoProceso = new ArrayList<TipoProcesoVO>();
	private List<JuzgadoVO> listJuzgado = new ArrayList<JuzgadoVO>();
	private List<AbogadoVO> listAbogado = new ArrayList<AbogadoVO>();
	private List<EstadoProcedVO> listEstadoProced = new ArrayList<EstadoProcedVO>();
    
	private List<MotivoBajaVO> listMotivoBaja = new ArrayList<MotivoBajaVO>();
	private List<MotivoResInfVO> listMotivoResInf = new ArrayList<MotivoResInfVO>();
	
	private List<SiNo>  listSiNo = new ArrayList<SiNo>();
	
	private String[] listIdDeudaSelected;
	private Boolean superaMaxCantCuentas = false;
	
	
    // Constructores
    public ProcedimientoAdapter(){
    	super(CyqSecurityConstants.ABM_PROCEDIMIENTO_CyQ);
    	ACCION_MODIFICAR_ENCABEZADO = CyqSecurityConstants.ABM_PROCEDIMIENTO_ENC_CyQ;
    }
    
    //  Getters y Setters
	public ProcedimientoVO getProcedimiento() {
		return procedimiento;
	}

	public void setProcedimiento(ProcedimientoVO procedimientoVO) {
		this.procedimiento = procedimientoVO;
	}

	public List<TipoProcesoVO> getListTipoProceso() {
		return listTipoProceso;
	}
	public void setListTipoProceso(List<TipoProcesoVO> listTipoProceso) {
		this.listTipoProceso = listTipoProceso;
	}

	public List<JuzgadoVO> getListJuzgado() {
		return listJuzgado;
	}
	public void setListJuzgado(List<JuzgadoVO> listJuzgado) {
		this.listJuzgado = listJuzgado;
	}

	public List<AbogadoVO> getListAbogado() {
		return listAbogado;
	}
	public void setListAbogado(List<AbogadoVO> listAbogado) {
		this.listAbogado = listAbogado;
	}

	public List<EstadoProcedVO> getListEstadoProced() {
		return listEstadoProced;
	}
	public void setListEstadoProced(List<EstadoProcedVO> listEstadoProced) {
		this.listEstadoProced = listEstadoProced;
	}
	
	public String[] getListIdDeudaSelected() {
		return listIdDeudaSelected;
	}
	public void setListIdDeudaSelected(String[] listIdDeudaSelected) {
		this.listIdDeudaSelected = listIdDeudaSelected;
	}

	// View getters
	public Boolean getSuperaMaxCantCuentas() {
		return superaMaxCantCuentas;
	}
	public void setSuperaMaxCantCuentas(Boolean superaMaxCantCuentas) {
		this.superaMaxCantCuentas = superaMaxCantCuentas;
	}
	
	// liquidacionDeudaEnabled
	public String getLiquidacionDeudaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(CyqSecurityConstants.ABM_PROCEDIMIENTO_CyQ, 
				CyqSecurityConstants.MTD_LIQUIDACION_DEUDA); 
	}
	// estadoCuentaEnabled
	public String getEstadoCuentaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(CyqSecurityConstants.ABM_PROCEDIMIENTO_CyQ, 
				CyqSecurityConstants.MTD_ESTADO_CUENTA); 
	}

	
	public String getAgregarDeudaAdminEnabled() {
		return SiatBussImageModel.hasEnabledFlag(CyqSecurityConstants.ABM_PROCEDIMIENTO_CyQ, 
				CyqSecurityConstants.MTD_AGREGAR_DEUDA_ADMIN); 
	}
	
	public String getQuitarDeudaAdminEnabled() {
		return SiatBussImageModel.hasEnabledFlag(CyqSecurityConstants.ABM_PROCEDIMIENTO_CyQ, 
				CyqSecurityConstants.MTD_QUITAR_DEUDA_ADMIN); 
	}
	
	public String getAgregarDeudaJudicialEnabled() {
		return SiatBussImageModel.hasEnabledFlag(CyqSecurityConstants.ABM_PROCEDIMIENTO_CyQ, 
				CyqSecurityConstants.MTD_AGREGAR_DEUDA_JUDICIAL); 
	}
	
	public String getQuitarDeudaJudicialEnabled() {
		return SiatBussImageModel.hasEnabledFlag(CyqSecurityConstants.ABM_PROCEDIMIENTO_CyQ, 
				CyqSecurityConstants.MTD_QUITAR_DEUDA_JUDICIAL); 
	}
	
	public String getInformarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(CyqSecurityConstants.ABM_PROCEDIMIENTO_ENC_CyQ, 
				CyqSecurityConstants.INFORMAR); 
	}

	public String getBajaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(CyqSecurityConstants.ABM_PROCEDIMIENTO_ENC_CyQ, 
				CyqSecurityConstants.BAJA); 
	}
	
	public String getConversionEnabled() {
		return SiatBussImageModel.hasEnabledFlag(CyqSecurityConstants.ABM_PROCEDIMIENTO_ENC_CyQ, 
				CyqSecurityConstants.CONVERSION); 
	}
	
	public List<MotivoBajaVO> getListMotivoBaja() {
		return listMotivoBaja;
	}
	public void setListMotivoBaja(List<MotivoBajaVO> listMotivoBaja) {
		this.listMotivoBaja = listMotivoBaja;
	}

	public List<MotivoResInfVO> getListMotivoResInf() {
		return listMotivoResInf;
	}
	public void setListMotivoResInf(List<MotivoResInfVO> listMotivoResInf) {
		this.listMotivoResInf = listMotivoResInf;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
	
	// Permisos para ABM ProCueNoDeu
	public String getVerProCueNoDeuEnabled(){
		return SiatBussImageModel.hasEnabledFlag(CyqSecurityConstants.ABM_PROCUENODEU, BaseSecurityConstants.VER);
	}
	public String getModificarProCueNoDeuEnabled(){
		return SiatBussImageModel.hasEnabledFlag(CyqSecurityConstants.ABM_PROCUENODEU, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarProCueNoDeuEnabled(){
		return SiatBussImageModel.hasEnabledFlag(CyqSecurityConstants.ABM_PROCUENODEU, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarProCueNoDeuEnabled(){
		return SiatBussImageModel.hasEnabledFlag(CyqSecurityConstants.ABM_PROCUENODEU, BaseSecurityConstants.AGREGAR);
	}
}
