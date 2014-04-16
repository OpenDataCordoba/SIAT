//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ZonaVO;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;

/**
 * Adapter de Repartidor
 * 
 * @author tecso
 */
public class RepartidorAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "repartidorAdapterVO";
	public static final String ENC_NAME = "encRepartidorAdapterVO";

	private RepartidorVO repartidor = new RepartidorVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<TipoRepartidorVO> listTipoRepartidor = new ArrayList<TipoRepartidorVO>();
	private List<ZonaVO> listZona = new ArrayList<ZonaVO>();
	private List<BrocheVO> listBroche = new ArrayList<BrocheVO>();

	// Flags
	private int paramRecurso = 0;
	
	public RepartidorAdapter(){
		super(PadSecurityConstants.ABM_REPARTIDOR);
		ACCION_MODIFICAR_ENCABEZADO = PadSecurityConstants.ABM_REPARTIDOR_ENC;
	}

	// Getters y Setters
	
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public List<TipoRepartidorVO> getListTipoRepartidor() {
		return listTipoRepartidor;
	}
	public void setListTipoRepartidor(List<TipoRepartidorVO> listTipoRepartidor) {
		this.listTipoRepartidor = listTipoRepartidor;
	}
	public List<ZonaVO> getListZona() {
		return listZona;
	}
	public void setListZona(List<ZonaVO> listZona) {
		this.listZona = listZona;
	}
	public RepartidorVO getRepartidor() {
		return repartidor;
	}
	public void setRepartidor(RepartidorVO repartidor) {
		this.repartidor = repartidor;
	}	
	public int getParamRecurso() {
		return paramRecurso;
	}
	public void setParamRecurso(int paramRecurso) {
		this.paramRecurso = paramRecurso;
	}
	public List<BrocheVO> getListBroche() {
		return listBroche;
	}
	public void setListBroche(List<BrocheVO> listBroche) {
		this.listBroche = listBroche;
	}

	// Permisos para ABM CriRepCat
	public String getVerCriRepCatEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(PadSecurityConstants.ABM_CRIREPCAT, BaseSecurityConstants.VER);
	}
	public String getModificarCriRepCatEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(PadSecurityConstants.ABM_CRIREPCAT, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarCriRepCatEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(PadSecurityConstants.ABM_CRIREPCAT, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarCriRepCatEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(PadSecurityConstants.ABM_CRIREPCAT, BaseSecurityConstants.AGREGAR);
	}
	// Permisos para ABM CriRepCalle
	public String getVerCriRepCalleEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(PadSecurityConstants.ABM_CRIREPCALLE, BaseSecurityConstants.VER);
	}
	public String getModificarCriRepCalleEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(PadSecurityConstants.ABM_CRIREPCALLE, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarCriRepCalleEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(PadSecurityConstants.ABM_CRIREPCALLE, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarCriRepCalleEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(PadSecurityConstants.ABM_CRIREPCALLE, BaseSecurityConstants.AGREGAR);
	}
}
