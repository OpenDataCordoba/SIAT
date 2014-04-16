//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;

/**
 * Adapter de DisPar
 * 
 * @author tecso
 */
public class DisParAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "disParAdapterVO";
	public static final String ENC_NAME = "encDisParAdapterVO";
	
	private DisParVO disPar = new DisParVO();
	private TipoImporteVO tipoImporteSelected = new TipoImporteVO();
	private RecConVO recConSelected = new RecConVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<TipoImporteVO> listTipoImporte = new ArrayList<TipoImporteVO>();
	private List<RecConVO> listRecCon = new ArrayList<RecConVO>();
	
	private List<DisParDetVO> listDisParDet = new ArrayList<DisParDetVO>();

	// Flags
	private boolean paramTipoImporte = true;
	
	public DisParAdapter(){
		super(BalSecurityConstants.ABM_DISPAR);
		ACCION_MODIFICAR_ENCABEZADO = BalSecurityConstants.ABM_DISPAR_ENC;
	}

	// Getters y Setters
	public DisParVO getDisPar() {
		return disPar;
	}
	public void setDisPar(DisParVO disPar) {
		this.disPar = disPar;
	}
	public List<DisParDetVO> getListDisParDet() {
		return listDisParDet;
	}
	public void setListDisParDet(List<DisParDetVO> listDisParDet) {
		this.listDisParDet = listDisParDet;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public List<TipoImporteVO> getListTipoImporte() {
		return listTipoImporte;
	}
	public void setListTipoImporte(List<TipoImporteVO> listTipoImporte) {
		this.listTipoImporte = listTipoImporte;
	}
	public TipoImporteVO getTipoImporteSelected() {
		return tipoImporteSelected;
	}
	public void setTipoImporteSelected(TipoImporteVO tipoImporteSelected) {
		this.tipoImporteSelected = tipoImporteSelected;
	}
	public List<RecConVO> getListRecCon() {
		return listRecCon;
	}
	public void setListRecCon(List<RecConVO> listRecCon) {
		this.listRecCon = listRecCon;
	}
	public RecConVO getRecConSelected() {
		return recConSelected;
	}
	public void setRecConSelected(RecConVO recConSelected) {
		this.recConSelected = recConSelected;
	}
	public boolean isParamTipoImporte() {
		return paramTipoImporte;
	}
	public void setParamTipoImporte(boolean paramTipoImporte) {
		this.paramTipoImporte = paramTipoImporte;
	}

	// Permisos para ABM DisParDet
	public String getVerDisParDetEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_DISPARDET, BaseSecurityConstants.VER);
	}
	public String getModificarDisParDetEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_DISPARDET, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarDisParDetEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_DISPARDET, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarDisParDetEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_DISPARDET, BaseSecurityConstants.AGREGAR);
	}
}
