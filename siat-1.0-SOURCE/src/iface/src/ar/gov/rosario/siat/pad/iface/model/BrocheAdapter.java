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
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter de Broche
 * 
 * @author tecso
 */
public class BrocheAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "brocheAdapterVO";
	
	private BrocheVO broche = new BrocheVO(); 
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<TipoBrocheVO> listTipoBroche = new ArrayList<TipoBrocheVO>();
	private List<RepartidorVO> listRepartidor = new ArrayList<RepartidorVO>();

	private List<BroCueVO> listBroCue = new ArrayList<BroCueVO>();
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
	
	private int paramTipoBroche = 0;
	
	public BrocheAdapter(){
		super(PadSecurityConstants.ABM_BROCHE);
	}
	
	// Getters y Setters
	
	public BrocheVO getBroche() {
		return broche;
	}
	public void setBroche(BrocheVO broche) {
		this.broche = broche;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public List<RepartidorVO> getListRepartidor() {
		return listRepartidor;
	}
	public void setListRepartidor(List<RepartidorVO> listRepartidor) {
		this.listRepartidor = listRepartidor;
	}
	public List<TipoBrocheVO> getListTipoBroche() {
		return listTipoBroche;
	}
	public void setListTipoBroche(List<TipoBrocheVO> listTipoBroche) {
		this.listTipoBroche = listTipoBroche;
	}
	public int getParamTipoBroche() {
		return paramTipoBroche;
	}
	public void setParamTipoBroche(int paramTipoBroche) {
		this.paramTipoBroche = paramTipoBroche;
	}
	public List<BroCueVO> getListBroCue() {
		return listBroCue;
	}
	public void setListBroCue(List<BroCueVO> listBroCue) {
		this.listBroCue = listBroCue;
	}

	// Permisos para ABM BroCue
	public String getVerBroCueEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(PadSecurityConstants.ABM_BROCUE, BaseSecurityConstants.VER);
	}
	public String getModificarBroCueEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(PadSecurityConstants.ABM_BROCUE, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarBroCueEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(PadSecurityConstants.ABM_BROCUE, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarBroCueEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(PadSecurityConstants.ABM_BROCUE, BaseSecurityConstants.AGREGAR);
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
}
