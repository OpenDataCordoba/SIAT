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
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.PasoCorridaVO;

/**
 * Adapter de Proceso de Balance
 * 
 * @author tecso
 */
public class ProcesoBalanceAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "procesoBalanceAdapterVO";
	public static final String ENC_NAME = "balanceAdapterVO";

	private BalanceVO balance = new BalanceVO();
	private PasoCorridaVO pasoCorrida1 = new PasoCorridaVO();
	private PasoCorridaVO pasoCorrida2 = new PasoCorridaVO();
	private PasoCorridaVO pasoCorrida3 = new PasoCorridaVO();
	private PasoCorridaVO pasoCorrida4 = new PasoCorridaVO();
	private PasoCorridaVO pasoCorrida5 = new PasoCorridaVO();
	private PasoCorridaVO pasoCorrida6 = new PasoCorridaVO();
	private PasoCorridaVO pasoCorrida7 = new PasoCorridaVO();

	private List<FileCorridaVO> listFileCorrida1 = new ArrayList<FileCorridaVO>();
	private List<FileCorridaVO> listFileCorrida2 = new ArrayList<FileCorridaVO>();
	private List<FileCorridaVO> listFileCorrida3 = new ArrayList<FileCorridaVO>();
	private List<FileCorridaVO> listFileCorrida4 = new ArrayList<FileCorridaVO>();
	private List<FileCorridaVO> listFileCorrida5 = new ArrayList<FileCorridaVO>();
	private List<FileCorridaVO> listFileCorrida6 = new ArrayList<FileCorridaVO>();
	
	private Long idFolio;
	private Long idArchivo;
	private Long idCaja7;
	private Long idCompensacion;
	
	private String cantReingreso = "";
	private String impCobradoReingreso = "";

	private String cantTranBal = "";
	private String impCobradoTranBal = "";

	private String totalCaja7 = "";
	private String totalCaja69 = "";

	//Flags
	private String paramPaso = "";
	private boolean paramActivar = true;
	private boolean paramReprogramar = false;
	private boolean paramCancelar = false;
	private boolean paramReiniciar = false;
	private boolean paramRetroceder = false;
	private boolean paramModificar = false;
	private boolean paramContinuar = false;
	
	private boolean deshabilitarAdm = false;
	
	public ProcesoBalanceAdapter(){
		super(BalSecurityConstants.ABM_PROCESO_BALANCE);
		ACCION_MODIFICAR_ENCABEZADO = BalSecurityConstants.ABM_PROCESO_BALANCE_ENC;
	}

	// Getters y Setters
	public BalanceVO getBalance() {
		return balance;
	}
	public void setBalance(BalanceVO balance) {
		this.balance = balance;
	}
	public String getParamPaso() {
		return paramPaso;
	}
	public void setParamPaso(String paramPaso) {
		this.paramPaso = paramPaso;
	}
	public PasoCorridaVO getPasoCorrida1() {
		return pasoCorrida1;
	}
	public void setPasoCorrida1(PasoCorridaVO pasoCorrida1) {
		this.pasoCorrida1 = pasoCorrida1;
	}
	public PasoCorridaVO getPasoCorrida2() {
		return pasoCorrida2;
	}
	public void setPasoCorrida2(PasoCorridaVO pasoCorrida2) {
		this.pasoCorrida2 = pasoCorrida2;
	}
	public PasoCorridaVO getPasoCorrida3() {
		return pasoCorrida3;
	}
	public void setPasoCorrida3(PasoCorridaVO pasoCorrida3) {
		this.pasoCorrida3 = pasoCorrida3;
	}
	public List<FileCorridaVO> getListFileCorrida1() {
		return listFileCorrida1;
	}
	public void setListFileCorrida1(List<FileCorridaVO> listFileCorrida1) {
		this.listFileCorrida1 = listFileCorrida1;
	}
	public List<FileCorridaVO> getListFileCorrida2() {
		return listFileCorrida2;
	}
	public void setListFileCorrida2(List<FileCorridaVO> listFileCorrida2) {
		this.listFileCorrida2 = listFileCorrida2;
	}
	public List<FileCorridaVO> getListFileCorrida3() {
		return listFileCorrida3;
	}
	public void setListFileCorrida3(List<FileCorridaVO> listFileCorrida3) {
		this.listFileCorrida3 = listFileCorrida3;
	}
	public List<FileCorridaVO> getListFileCorrida4() {
		return listFileCorrida4;
	}
	public void setListFileCorrida4(List<FileCorridaVO> listFileCorrida4) {
		this.listFileCorrida4 = listFileCorrida4;
	}
	public List<FileCorridaVO> getListFileCorrida5() {
		return listFileCorrida5;
	}
	public void setListFileCorrida5(List<FileCorridaVO> listFileCorrida5) {
		this.listFileCorrida5 = listFileCorrida5;
	}
	public List<FileCorridaVO> getListFileCorrida6() {
		return listFileCorrida6;
	}
	public void setListFileCorrida6(List<FileCorridaVO> listFileCorrida6) {
		this.listFileCorrida6 = listFileCorrida6;
	}
	public PasoCorridaVO getPasoCorrida4() {
		return pasoCorrida4;
	}
	public void setPasoCorrida4(PasoCorridaVO pasoCorrida4) {
		this.pasoCorrida4 = pasoCorrida4;
	}
	public PasoCorridaVO getPasoCorrida5() {
		return pasoCorrida5;
	}
	public void setPasoCorrida5(PasoCorridaVO pasoCorrida5) {
		this.pasoCorrida5 = pasoCorrida5;
	}
	public PasoCorridaVO getPasoCorrida6() {
		return pasoCorrida6;
	}
	public void setPasoCorrida6(PasoCorridaVO pasoCorrida6) {
		this.pasoCorrida6 = pasoCorrida6;
	}
	public PasoCorridaVO getPasoCorrida7() {
		return pasoCorrida7;
	}
	public void setPasoCorrida7(PasoCorridaVO pasoCorrida7) {
		this.pasoCorrida7 = pasoCorrida7;
	}

	public boolean isParamActivar() {
		return paramActivar;
	}
	public void setParamActivar(boolean paramActivar) {
		this.paramActivar = paramActivar;
	}
	public boolean isParamCancelar() {
		return paramCancelar;
	}
	public void setParamCancelar(boolean paramCancelar) {
		this.paramCancelar = paramCancelar;
	}
	public boolean isParamReiniciar() {
		return paramReiniciar;
	}
	public void setParamReiniciar(boolean paramReiniciar) {
		this.paramReiniciar = paramReiniciar;
	}
	public boolean isParamRetroceder() {
		return paramRetroceder;
	}
	public void setParamRetroceder(boolean paramRetroceder) {
		this.paramRetroceder = paramRetroceder;
	}

	public boolean isParamReprogramar() {
		return paramReprogramar;
	}
	public void setParamReprogramar(boolean paramReprogramar) {
		this.paramReprogramar = paramReprogramar;
	}
	public boolean isParamModificar() {
		return paramModificar;
	}
	public void setParamModificar(boolean paramModificar) {
		this.paramModificar = paramModificar;
	}
	public boolean isParamContinuar() {
		return paramContinuar;
	}
	public void setParamContinuar(boolean paramContinuar) {
		this.paramContinuar = paramContinuar;
	}
	public Long getIdArchivo() {
		return idArchivo;
	}
	public void setIdArchivo(Long idArchivo) {
		this.idArchivo = idArchivo;
	}
	public Long getIdCompensacion() {
		return idCompensacion;
	}
	public void setIdCompensacion(Long idCompensacion) {
		this.idCompensacion = idCompensacion;
	}
	public Long getIdFolio() {
		return idFolio;
	}
	public void setIdFolio(Long idFolio) {
		this.idFolio = idFolio;
	}
	public Long getIdCaja7() {
		return idCaja7;
	}
	public void setIdCaja7(Long idCaja7) {
		this.idCaja7 = idCaja7;
	}
	public String getCantReingreso() {
		return cantReingreso;
	}
	public void setCantReingreso(String cantReingreso) {
		this.cantReingreso = cantReingreso;
	}
	public String getImpCobradoReingreso() {
		return impCobradoReingreso;
	}
	public void setImpCobradoReingreso(String impCobradoReingreso) {
		this.impCobradoReingreso = impCobradoReingreso;
	}
	public String getCantTranBal() {
		return cantTranBal;
	}
	public void setCantTranBal(String cantTranBal) {
		this.cantTranBal = cantTranBal;
	}
	public String getImpCobradoTranBal() {
		return impCobradoTranBal;
	}
	public void setImpCobradoTranBal(String impCobradoTranBal) {
		this.impCobradoTranBal = impCobradoTranBal;
	}
	public boolean isDeshabilitarAdm() {
		return deshabilitarAdm;
	}
	public void setDeshabilitarAdm(boolean deshabilitarAdm) {
		this.deshabilitarAdm = deshabilitarAdm;
	}
	public String getTotalCaja69() {
		return totalCaja69;
	}
	public void setTotalCaja69(String totalCaja69) {
		this.totalCaja69 = totalCaja69;
	}
	public String getTotalCaja7() {
		return totalCaja7;
	}
	public void setTotalCaja7(String totalCaja7) {
		this.totalCaja7 = totalCaja7;
	}

	// Permisos para ABM Folio
	public String getVerFolioEnabled(){
		return SiatBussImageModel.hasEnabledFlag(BalSecurityConstants.ABM_FOLIO, BaseSecurityConstants.VER);
	}
	public String getIncluirFolioEnabled(){
		return SiatBussImageModel.hasEnabledFlag(!isDeshabilitarAdm(),BalSecurityConstants.ABM_FOLIO, BaseSecurityConstants.INCLUIR);
	}
	public String getExcluirFolioEnabled(){
		return SiatBussImageModel.hasEnabledFlag(!isDeshabilitarAdm(),BalSecurityConstants.ABM_FOLIO, BaseSecurityConstants.EXCLUIR);
	}

	// Permisos para ABM Archivo
	public String getVerArchivoEnabled(){
		return SiatBussImageModel.hasEnabledFlag(BalSecurityConstants.ABM_ARCHIVO, BaseSecurityConstants.VER);
	}
	public String getIncluirArchivoEnabled(){
		return SiatBussImageModel.hasEnabledFlag(!isDeshabilitarAdm(),BalSecurityConstants.ABM_ARCHIVO, BaseSecurityConstants.INCLUIR);
	}
	public String getExcluirArchivoEnabled(){
		return SiatBussImageModel.hasEnabledFlag(!isDeshabilitarAdm(),BalSecurityConstants.ABM_ARCHIVO, BaseSecurityConstants.EXCLUIR);
	}
	
	// Permisos para ABM Compensacion
	public String getVerCompensacionEnabled(){
		return SiatBussImageModel.hasEnabledFlag(BalSecurityConstants.ABM_COMPENSACION, BaseSecurityConstants.VER);
	}
	public String getIncluirCompensacionEnabled(){
		return SiatBussImageModel.hasEnabledFlag(!isDeshabilitarAdm(),BalSecurityConstants.ABM_COMPENSACION, BaseSecurityConstants.INCLUIR);
	}
	public String getExcluirCompensacionEnabled(){
		return SiatBussImageModel.hasEnabledFlag(!isDeshabilitarAdm(),BalSecurityConstants.ABM_COMPENSACION, BaseSecurityConstants.EXCLUIR);
	}
	
	// Permisos para ABM Caja7
	public String getVerCaja7Enabled(){
		return SiatBussImageModel.hasEnabledFlag(BalSecurityConstants.ABM_CAJA7, BaseSecurityConstants.VER);
	}
	public String getAgregarCaja7Enabled(){
		return SiatBussImageModel.hasEnabledFlag(!isDeshabilitarAdm(),BalSecurityConstants.ABM_CAJA7, BaseSecurityConstants.AGREGAR);
	}
	public String getModificarCaja7Enabled(){
		return SiatBussImageModel.hasEnabledFlag(!isDeshabilitarAdm(),BalSecurityConstants.ABM_CAJA7, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarCaja7Enabled(){
		return SiatBussImageModel.hasEnabledFlag(!isDeshabilitarAdm(),BalSecurityConstants.ABM_CAJA7, BaseSecurityConstants.ELIMINAR);
	}
	public String getIncluirAuxCaja7Enabled(){
		return SiatBussImageModel.hasEnabledFlag(!isDeshabilitarAdm(),BalSecurityConstants.ABM_AUXCAJA7, BaseSecurityConstants.INCLUIR);
	}
	
	// Permisos para ABM Caja69
	public String getVerCaja69Enabled(){
		return SiatBussImageModel.hasEnabledFlag(BalSecurityConstants.ABM_CAJA69, BaseSecurityConstants.VER);
	}
	public String getAgregarCaja69Enabled(){
		return SiatBussImageModel.hasEnabledFlag(!isDeshabilitarAdm(),BalSecurityConstants.ABM_CAJA69, BaseSecurityConstants.AGREGAR);
	}
	public String getModificarCaja69Enabled(){
		return SiatBussImageModel.hasEnabledFlag(!isDeshabilitarAdm(),BalSecurityConstants.ABM_CAJA69, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarCaja69Enabled(){
		return SiatBussImageModel.hasEnabledFlag(!isDeshabilitarAdm(),BalSecurityConstants.ABM_CAJA69, BaseSecurityConstants.ELIMINAR);
	}
	
	// Permisos para ABM TranBal
	public String getBuscarTranBalEnabled(){
		return SiatBussImageModel.hasEnabledFlag(!isDeshabilitarAdm(),BalSecurityConstants.ABM_TRANBAL, BaseSecurityConstants.BUSCAR);
	}
	public String getVerTranBalEnabled(){
		return SiatBussImageModel.hasEnabledFlag(BalSecurityConstants.ABM_TRANBAL, BaseSecurityConstants.VER);
	}
	public String getAgregarTranBalEnabled(){
		return SiatBussImageModel.hasEnabledFlag(!isDeshabilitarAdm(),BalSecurityConstants.ABM_TRANBAL, BaseSecurityConstants.AGREGAR);
	}
	public String getModificarTranBalEnabled(){
		return SiatBussImageModel.hasEnabledFlag(!isDeshabilitarAdm(),BalSecurityConstants.ABM_TRANBAL, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarTranBalEnabled(){
		return SiatBussImageModel.hasEnabledFlag(!isDeshabilitarAdm(),BalSecurityConstants.ABM_TRANBAL, BaseSecurityConstants.ELIMINAR);
	}

	// Permisos para Administrar Procesos de Asentamiento
	public String getVerAsentamientoEnabled(){
		return SiatBussImageModel.hasEnabledFlag(BalSecurityConstants.ABM_ASENTAMIENTO, BaseSecurityConstants.VER);
	}
	public String getAdmProcesoAsentamientoEnabled(){
		return SiatBussImageModel.hasEnabledFlag(BalSecurityConstants.ABM_ASENTAMIENTO, BalSecurityConstants.ABM_ASENTAMIENTO_ADM_PROCESO);
	}
	
	// Permisos para Administrar Procesos de Asentamiento Delegado
	public String getVerAseDelEnabled(){
		return SiatBussImageModel.hasEnabledFlag(BalSecurityConstants.ABM_ASEDEL, BaseSecurityConstants.VER);
	}
	public String getAdmProcesoAseDelEnabled(){
		return SiatBussImageModel.hasEnabledFlag(BalSecurityConstants.ABM_ASEDEL, BalSecurityConstants.ABM_ASEDEL_ADM_PROCESO);
	}
	
}
