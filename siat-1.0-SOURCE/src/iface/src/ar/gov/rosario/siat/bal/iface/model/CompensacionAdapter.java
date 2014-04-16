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
import ar.gov.rosario.siat.def.iface.model.RecursoVO;

/**
 * Adapter de Compensacion
 * 
 * @author tecso
 */
public class CompensacionAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "compensacionAdapterVO";
	public static final String ENC_NAME = "encCompensacionAdapterVO";
	public static final String ADM_SALDO_NAME = "admSaldoEnCompensacionAdapterVO";
	
	private CompensacionVO compensacion = new CompensacionVO();
	
	private RecursoVO recurso = new RecursoVO();
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	private Long idSaldoAFavor = null;
	private String totalSaldoAFavor = "";
	private String totalComDeu = "";
		
	// Para la inclusión de Saldos a Favor
	private List<SaldoAFavorVO> listSaldoAFavor = new ArrayList<SaldoAFavorVO>();
	private String[] listIdSaldoSelected;
	// Para excluir Saldos a Favor
	private SaldoAFavorVO saldoAFavor = new SaldoAFavorVO();
	
	public CompensacionAdapter(){
		super(BalSecurityConstants.ABM_COMPENSACION);
		ACCION_MODIFICAR_ENCABEZADO = BalSecurityConstants.ABM_COMPENSACION_ENC;
	}

	// Getters Y Setters
	public CompensacionVO getCompensacion() {
		return compensacion;
	}
	public void setCompensacion(CompensacionVO compensacion) {
		this.compensacion = compensacion;
	}
	public Long getIdSaldoAFavor() {
		return idSaldoAFavor;
	}
	public void setIdSaldoAFavor(Long idSaldoAFavor) {
		this.idSaldoAFavor = idSaldoAFavor;
	}
	public String getTotalComDeu() {
		return totalComDeu;
	}
	public void setTotalComDeu(String totalComDeu) {
		this.totalComDeu = totalComDeu;
	}
	public String getTotalSaldoAFavor() {
		return totalSaldoAFavor;
	}
	public void setTotalSaldoAFavor(String totalSaldoAFavor) {
		this.totalSaldoAFavor = totalSaldoAFavor;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public List<SaldoAFavorVO> getListSaldoAFavor() {
		return listSaldoAFavor;
	}
	public void setListSaldoAFavor(List<SaldoAFavorVO> listSaldoAFavor) {
		this.listSaldoAFavor = listSaldoAFavor;
	}
	public String[] getListIdSaldoSelected() {
		return listIdSaldoSelected;
	}
	public void setListIdSaldoSelected(String[] listIdSaldoSelected) {
		this.listIdSaldoSelected = listIdSaldoSelected;
	}
	public SaldoAFavorVO getSaldoAFavor() {
		return saldoAFavor;
	}
	public void setSaldoAFavor(SaldoAFavorVO saldoAFavor) {
		this.saldoAFavor = saldoAFavor;
	}

	// Permisos para ABM ComDeu
	public String getVerComDeuEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_COMDEU, BaseSecurityConstants.VER);
	}
	public String getModificarComDeuEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_COMDEU, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarComDeuEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_COMDEU, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarComDeuEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_COMDEU, BaseSecurityConstants.AGREGAR);
	}

	// Permisos para ABM SaldoAFavor
	public String getVerSaldoAFavorEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_SALDOAFAVOR, BaseSecurityConstants.VER);
	}
	public String getIncluirSaldoAFavorEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_SALDOAFAVOR, BaseSecurityConstants.INCLUIR);
	}
	public String getExcluirSaldoAFavorEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_SALDOAFAVOR, BaseSecurityConstants.EXCLUIR);
	}
	
}
