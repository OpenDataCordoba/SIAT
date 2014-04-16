//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.pro.iface.model.ProcesoVO;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter de TipObjImp
 * 
 * @author tecso
 */
public class TipObjImpAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
		
	public static final String NAME      = "tipObjImpAdapterVO";
	public static final String ENC_NAME  = "tipObjImpEncAdapterVO";
	
    private TipObjImpVO tipObjImp = new TipObjImpVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    private List<Estado>		 listEstado = new ArrayList<Estado>();    
    private List<ProcesoVO> 	listProceso = new ArrayList<ProcesoVO>();  
    
    private boolean selectProcesoEnabled = false;
    
    // Constructores
    public TipObjImpAdapter(){
    	super(DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE);
    	ACCION_MODIFICAR_ENCABEZADO = DefSecurityConstants.ABM_DOMINIO_ATRIBUTO_ENC;
    }
	
	// Getters y Setters
	public TipObjImpVO getTipObjImp() {
		return tipObjImp;
	}

	public void setTipObjImp(TipObjImpVO tipObjImpVO) {
		this.tipObjImp = tipObjImpVO;
	}
	
		public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
	public List<Estado> getListEstado() {
		return listEstado;
	}

	public void setListEstado(List<Estado> listEstado) {
		this.listEstado = listEstado;
	}
	
	public String getVerTipObjImpAtrEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ATRIBUTO, BaseSecurityConstants.VER);
	}
	
	public String getModificarTipObjImpAtrEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ATRIBUTO, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarTipObjImpAtrEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ATRIBUTO, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarTipObjImpAtrEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ATRIBUTO, BaseSecurityConstants.AGREGAR);
	}

	public String getAgregarTipObjImpAreOEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ARE_O, BaseSecurityConstants.AGREGAR);
	}
	
	public String getActivarTipObjImpAreOEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ARE_O, BaseSecurityConstants.ACTIVAR);
	}
	
	public String getDesactivarTipObjImpAreOEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ARE_O, BaseSecurityConstants.DESACTIVAR);
	}

	public String getVerTipObjImpAreOEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ARE_O, BaseSecurityConstants.VER);
	}

	public String getEliminarTipObjImpAreOEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ARE_O, BaseSecurityConstants.ELIMINAR);
	}

	public void setSelectProcesoEnabled(boolean selectProcesoEnabled) {
		this.selectProcesoEnabled = selectProcesoEnabled;
	}

	public boolean isSelectProcesoEnabled() {
		return selectProcesoEnabled;
	}

	public void setListProceso(List<ProcesoVO> listProceso) {
		this.listProceso = listProceso;
	}

	public List<ProcesoVO> getListProceso() {
		return listProceso;
	}



}
