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
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;
import ar.gov.rosario.siat.gde.iface.model.CriAsiProVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter de Recurso
 * 
 * @author tecso
 */
public class RecursoAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "recursoAdapterVO";
	public static final String ENC_NAME = "encRecursoAdapterVO";
	
	private RecursoVO recurso = new RecursoVO();
	
	private RecursoDefinition recursoDefinitionForRecAtrVal = new RecursoDefinition();
	private RecursoDefinition recursoDefinitionForRecAtrCue = new RecursoDefinition();
	private RecursoDefinition recursoDefinitionForRecGenCueAtrVa = new RecursoDefinition();
	
	private List<CategoriaVO> listCategoria = new ArrayList<CategoriaVO>();
	private List<TipObjImpVO> listTipObjImp = new ArrayList<TipObjImpVO>();
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<GenCueVO> listGenCue = new ArrayList<GenCueVO>();
	private List<GenCodGesVO> listGenCodGes = new ArrayList<GenCodGesVO>();
	private List<CriAsiProVO> listCriAsiPro = new ArrayList<CriAsiProVO>();
	private List<AtributoVO> listAtributo = new ArrayList<AtributoVO>();
	private List<AtributoVO> listAtrSegment = new ArrayList<AtributoVO>();
	private List<PeriodoDeudaVO> listPeriodoDeuda = new ArrayList<PeriodoDeudaVO>();
	private List<VencimientoVO> listVencimiento = new ArrayList<VencimientoVO>();
	private List<FormularioVO> listFormulario = new ArrayList<FormularioVO>();
	
	private List<SiNo> listSiNo= new ArrayList<SiNo>();
	
	private boolean esNoTrib=false;
	
	// Flags para habilitar o deshabilitar la carga de campos.
	private int paramTipObjImp = 0;
	private int paramEsPrincipal = -1; 
	private int paramEnviaJudicial = 0;
	private int paramPerEmiDeuMas = -1;
	private int paramPerImpMasDeu = -1;
	private int paramGenNotImpMas = -1;
	private int paramGenPadFir = -1;
		
	public RecursoAdapter(){
		super(DefSecurityConstants.ABM_RECURSO);
		ACCION_MODIFICAR_ENCABEZADO = DefSecurityConstants.ABM_RECURSO_ENC;
	}
	
	// Getters y Setters
	public RecursoVO getRecurso(){
		return recurso;
	}
	public void setRecurso(RecursoVO recurso){
		this.recurso = recurso;
	}
	public List<CategoriaVO> getListCategoria() {
		return listCategoria;
	}
	public void setListCategoria(List<CategoriaVO> listCategoria) {
		this.listCategoria = listCategoria;
	}
	public List<TipObjImpVO> getListTipObjImp() {
		return listTipObjImp;
	}
	public void setListTipObjImp(List<TipObjImpVO> listTipObjImp) {
		this.listTipObjImp = listTipObjImp;
	}
	public List<RecursoVO> getListRecurso(){
		return listRecurso; 
	}
	public void setListRecurso(List<RecursoVO> listRecurso){
		this.listRecurso = listRecurso;
	}
	public List<GenCueVO> getListGenCue() {
		return listGenCue;
	}
	public void setListGenCue(List<GenCueVO> listGenCue) {
		this.listGenCue = listGenCue;
	}
	public List<GenCodGesVO> getListGenCodGes() {
		return listGenCodGes;
	}
	public void setListGenCodGes(List<GenCodGesVO> listGenCodGes) {
		this.listGenCodGes = listGenCodGes;
	}
	public List<CriAsiProVO> getListCriAsiPro() {
		return listCriAsiPro;
	}
	public void setListCriAsiPro(List<CriAsiProVO> listCriAsiPro) {
		this.listCriAsiPro = listCriAsiPro;
	}
	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	public int getParamTipObjImp(){
		return paramTipObjImp;
	}
	public void setParamTipObjImp(int paramTipObjImp){
		this.paramTipObjImp = paramTipObjImp;
	}
	public int getParamEsPrincipal(){
		return paramEsPrincipal;
	}
	public void setParamEsPrincipal(int paramEsPrincipal){
		this.paramEsPrincipal = paramEsPrincipal;
	}
	public int getParamEnviaJudicial(){
		return paramEnviaJudicial;
	}
	public void setParamEnviaJudicial(int paramEnviaJudicial){
		this.paramEnviaJudicial = paramEnviaJudicial;
	}
	public int getParamPerEmiDeuMas() {
		return paramPerEmiDeuMas;
	}
	public void setParamPerEmiDeuMas(int paramPerEmiDeuMas) {
		this.paramPerEmiDeuMas = paramPerEmiDeuMas;
	}
	
	public int getParamPerImpMasDeu() {
		return paramPerImpMasDeu;
	}

	public void setParamPerImpMasDeu(int paramPerImpMasDeu) {
		this.paramPerImpMasDeu = paramPerImpMasDeu;
	}

	public void setParamGenPadFir(int paramGenPadFir) {
		this.paramGenPadFir = paramGenPadFir;
	}

	public int getParamGenPadFir() {
		return paramGenPadFir;
	}

	public int getParamGenNotImpMas() {
		return paramGenNotImpMas;
	}

	public void setParamGenNotImpMas(int paramGenNotImpMas) {
		this.paramGenNotImpMas = paramGenNotImpMas;
	}

	public RecursoDefinition getRecursoDefinitionForRecAtrVal() {
		return recursoDefinitionForRecAtrVal;
	}
	public void setRecursoDefinitionForRecAtrVal(RecursoDefinition recursoDefinitionForRecAtrVal) {
		this.recursoDefinitionForRecAtrVal = recursoDefinitionForRecAtrVal;
	}
	public RecursoDefinition getRecursoDefinitionForRecAtrCue() {
		return recursoDefinitionForRecAtrCue;
	}
	public void setRecursoDefinitionForRecAtrCue(RecursoDefinition recursoDefinitionForRecAtrCue) {
		this.recursoDefinitionForRecAtrCue = recursoDefinitionForRecAtrCue;
	}

	public RecursoDefinition getRecursoDefinitionForRecGenCueAtrVa() {
		return recursoDefinitionForRecGenCueAtrVa;
	}

	public void setRecursoDefinitionForRecGenCueAtrVa(
			RecursoDefinition recursoDefinitionForRecGenCueAtrVa) {
		this.recursoDefinitionForRecGenCueAtrVa = recursoDefinitionForRecGenCueAtrVa;
	}
	public List<AtributoVO> getListAtributo() {
		return listAtributo;
	}
	public void setListAtributo(List<AtributoVO> listAtributo) {
		this.listAtributo = listAtributo;
	}
	
	public List<AtributoVO> getListAtrSegment() {
		return listAtrSegment;
	}

	public void setListAtrSegment(List<AtributoVO> listAtrSegment) {
		this.listAtrSegment = listAtrSegment;
	}

	public boolean isEsNoTrib() {
		return esNoTrib;
	}

	public void setEsNoTrib(boolean esNoTrib) {
		this.esNoTrib = esNoTrib;
	}

	public List<PeriodoDeudaVO> getListPeriodoDeuda() {
		return listPeriodoDeuda;
	}

	public void setListPeriodoDeuda(List<PeriodoDeudaVO> listPeriodoDeuda) {
		this.listPeriodoDeuda = listPeriodoDeuda;
	}
	
	public List<VencimientoVO> getListVencimiento() {
		return listVencimiento;
	}

	public void setListVencimiento(List<VencimientoVO> listVencimiento) {
		this.listVencimiento = listVencimiento;
	}
	
	public List<FormularioVO> getListFormulario() {
		return listFormulario;
	}

	public void setListFormulario(List<FormularioVO> listFormulario) {
		this.listFormulario = listFormulario;
	}

	// Permisos para ABM RecAtrVal
	public String getVerRecAtrValEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECATRVAL, BaseSecurityConstants.VER);
	}
	public String getModificarRecAtrValEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECATRVAL, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarRecAtrValEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECATRVAL, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarRecAtrValEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECATRVAL, BaseSecurityConstants.AGREGAR);
	}
	// Permisos para ABM RecCon
	public String getVerRecConEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECCON, BaseSecurityConstants.VER);
	}
	public String getModificarRecConEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECCON, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarRecConEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECCON, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarRecConEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECCON, BaseSecurityConstants.AGREGAR);
	}
	// Permisos para ABM RecClaDeu
	public String getVerRecClaDeuEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECCLADEU, BaseSecurityConstants.VER);
	}
	public String getModificarRecClaDeuEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECCLADEU, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarRecClaDeuEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECCLADEU, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarRecClaDeuEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECCLADEU, BaseSecurityConstants.AGREGAR);
	}
	// Permisos para ABM RecGenCueAtrVa
	public String getVerRecGenCueAtrVaEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECGENCUEATRVA, BaseSecurityConstants.VER);
	}
	public String getModificarRecGenCueAtrVaEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECGENCUEATRVA, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarRecGenCueAtrVaEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECGENCUEATRVA, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarRecGenCueAtrVaEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECGENCUEATRVA, BaseSecurityConstants.AGREGAR);
	}
	// Permisos para ABM RecEmi
	public String getVerRecEmiEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECEMI, BaseSecurityConstants.VER);
	}
	public String getModificarRecEmiEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECEMI, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarRecEmiEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECEMI, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarRecEmiEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECEMI, BaseSecurityConstants.AGREGAR);
	}
	// Permisos para ABM RecAtrCueEmi
	public String getVerRecAtrCueEmiEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECATRCUEEMI, BaseSecurityConstants.VER);
	}
	public String getModificarRecAtrCueEmiEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECATRCUEEMI, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarRecAtrCueEmiEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECATRCUEEMI, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarRecAtrCueEmiEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECATRCUEEMI, BaseSecurityConstants.AGREGAR);
	}
	// Permisos para ABM RecAtrCue
	public String getVerRecAtrCueEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_RECATRCUE, BaseSecurityConstants.VER);
	}
	public String getModificarRecAtrCueEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_RECATRCUE, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarRecAtrCueEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_RECATRCUE, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarRecAtrCueEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_RECATRCUE, BaseSecurityConstants.AGREGAR);
	}
	//Permisos para ABM RecConADec
	public String getVerRecConADecEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_RECCONADEC, BaseSecurityConstants.VER);
	}
	public String getModificarRecConADecEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_RECCONADEC, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarRecConADecEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_RECCONADEC, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarRecConADecEnabled(){
		return SiatBussImageModel.hasEnabledFlag(DefSecurityConstants.ABM_RECCONADEC, BaseSecurityConstants.AGREGAR);
	}

	// Permisos para ABM RecMinDec
	public String getVerRecMinDecEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECMINDEC, BaseSecurityConstants.VER);
	}
	public String getModificarRecMinDecEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECMINDEC, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarRecMinDecEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECMINDEC, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarRecMinDecEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECMINDEC, BaseSecurityConstants.AGREGAR);
	}
	
	// Permisos para ABM RecAli
	public String getVerRecAliEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECALI, BaseSecurityConstants.VER);
	}
	public String getModificarRecAliEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECALI, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarRecAliEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECALI, BaseSecurityConstants.ELIMINAR);
	}
	public String getAgregarRecAliEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(DefSecurityConstants.ABM_RECALI, BaseSecurityConstants.AGREGAR);
	}
	
	public String getName(){
		return NAME;
	}
	
}
