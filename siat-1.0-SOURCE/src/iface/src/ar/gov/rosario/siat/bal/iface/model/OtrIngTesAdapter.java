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
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;

/**
 * Adapter de Otros Ingresos de Tesoreria
 * 
 * @author tecso
 */
public class OtrIngTesAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "otrIngTesAdapterVO";
	
	private OtrIngTesVO otrIngTes = new OtrIngTesVO();
		
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<AreaVO> listAreaOrigen = new ArrayList<AreaVO>();
	private List<CuentaBancoVO> listCuentaBanco = new ArrayList<CuentaBancoVO>();
	
	private DisParVO disPar = new DisParVO(); 
	
	// Flags
	private boolean paramRecurso = false;
	private boolean paramUnicoConcepto = false;
	private boolean paramArea = false;
	private boolean modificarDistribucion = true;
	
	private boolean distribuirBussEnabled = false;
	
	// Constructores
	public OtrIngTesAdapter(){
		super(BalSecurityConstants.ABM_OTRINGTES);
	}

	// Getters Y Setters
	public OtrIngTesVO getOtrIngTes() {
		return otrIngTes;
	}
	public void setOtrIngTes(OtrIngTesVO otrIngTes) {
		this.otrIngTes = otrIngTes;
	}
	public List<AreaVO> getListAreaOrigen() {
		return listAreaOrigen;
	}
	public void setListAreaOrigen(List<AreaVO> listAreaOrigen) {
		this.listAreaOrigen = listAreaOrigen;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public boolean isParamRecurso() {
		return paramRecurso;
	}
	public void setParamRecurso(boolean paramRecurso) {
		this.paramRecurso = paramRecurso;
	}
	public boolean isParamArea() {
		return paramArea;
	}
	public void setParamArea(boolean paramArea) {
		this.paramArea = paramArea;
	}
	public List<CuentaBancoVO> getListCuentaBanco() {
		return listCuentaBanco;
	}
	public void setListCuentaBanco(List<CuentaBancoVO> listCuentaBanco) {
		this.listCuentaBanco = listCuentaBanco;
	}
	public DisParVO getDisPar() {
		return disPar;
	}
	public void setDisPar(DisParVO disPar) {
		this.disPar = disPar;
	}
	public boolean isParamUnicoConcepto() {
		return paramUnicoConcepto;
	}
	public void setParamUnicoConcepto(boolean paramUnicoConcepto) {
		this.paramUnicoConcepto = paramUnicoConcepto;
	}
	public boolean isModificarDistribucion() {
		return modificarDistribucion;
	}
	public void setModificarDistribucion(boolean modificarDistribucion) {
		this.modificarDistribucion = modificarDistribucion;
	}
	public boolean isDistribuirBussEnabled() {
		return distribuirBussEnabled;
	}
	public void setDistribuirBussEnabled(boolean distribuirBussEnabled) {
		this.distribuirBussEnabled = distribuirBussEnabled;
	}

	// Permisos para Distribuir
	public String getDistribuirEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_OTRINGTES, BalSecurityConstants.DISTRIBUIR);
	}
	
	// Permisos para ABM OtrIngTesPar
	public String getVerOtrIngTesParEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_OTRINGTESPAR, BaseSecurityConstants.VER);
	}
	public String getModificarOtrIngTesParEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_OTRINGTESPAR, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarOtrIngTesParEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_OTRINGTESPAR, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarOtrIngTesParEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_OTRINGTESPAR, BaseSecurityConstants.AGREGAR);
	}


}
