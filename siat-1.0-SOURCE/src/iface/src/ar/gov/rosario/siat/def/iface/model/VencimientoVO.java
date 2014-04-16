//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Vencimientos
 * @author tecso
 *
 */
public class VencimientoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "vencimientoVO";
	
	private String desVencimiento;
	private Long dia;
	private SiNo esHabil = SiNo.OpcionSelecionar;
	private Long mes;
	private Long cantDias;
	private Long cantMes;
	private Long cantSemana;
	private SiNo esUltimo = SiNo.OpcionSelecionar;
	private SiNo primeroSemana = SiNo.OpcionSelecionar;
	private SiNo ultimoSemana = SiNo.OpcionSelecionar;
	private String diaView = "";
	private String mesView = "";
	private String cantDiasView = "";
	private String cantMesView = "";
	private String cantSemanaView = "";
	
	
	
	// Constructores
	public VencimientoVO() {
		super();
	}
	public VencimientoVO(int id, String desVencimiento) {
		super(id);
		setDesVencimiento(desVencimiento);
	}
	
	// Getters y Setters
	public String getDesVencimiento(){
		return	desVencimiento;
	}
	public void setDesVencimiento(String desVencimiento){
		this.desVencimiento = desVencimiento;
	}
	public Long getDia(){
		return dia;
	}
	public void setDia(Long dia){
		this.dia = dia;
		this.diaView = StringUtil.formatLong(dia);
	}
	public Long getMes(){
		return mes;
	}
	public void setMes(Long mes){
		this.mes = mes;
		this.mesView = StringUtil.formatLong(mes);
	}
	public SiNo getEsHabil(){
		return esHabil;
	}
	public void setEsHabil(SiNo esHabil){
		this.esHabil = esHabil;
	}
	public Long getCantDias(){
		return cantDias;
	}
	public void setCantDias(Long cantDias){
		this.cantDias = cantDias;
		this.cantDiasView = StringUtil.formatLong(cantDias);
	}
	public Long getCantMes(){
		return cantMes;
	}
	public void setCantMes(Long cantMes){
		this.cantMes = cantMes;
		this.cantMesView = StringUtil.formatLong(cantMes);
	}
	public Long getCantSemana () {
		return cantSemana;
	}
	public void setCantSemana (Long cantSemana){
		this.cantSemana = cantSemana;
		this.cantSemanaView = StringUtil.formatLong(cantSemana);
	}
	public SiNo getEsUltimo(){
		return esUltimo;
	}
	public void setEsUltimo(SiNo esUltimo){
		this.esUltimo = esUltimo;		
	}
	public SiNo getPrimeroSemana() {
		return primeroSemana;
	}
	public void setPrimeroSemana (SiNo primeroSemana) {
		this.primeroSemana = primeroSemana;
	}
	public SiNo getUltimoSemana () {
		return ultimoSemana;
	}
	public void setUltimoSemana (SiNo ultimoSemana){
		this.ultimoSemana = ultimoSemana;
	}
	// View Getters y Setters
	
	public String getCantDiasView() {
		return cantDiasView;
	}
	public void setCantDiasView(String cantDiasView) {
		this.cantDiasView = cantDiasView;
	}
	public String getCantMesView() {
		return cantMesView;
	}
	public void setCantMesView(String cantMesView) {
		this.cantMesView = cantMesView;
	}
	public void setCantSemanaView(String cantSemanaView) {
		this.cantSemanaView = cantSemanaView;
	}
	public String getCantSemanaView (){
		return this.cantSemanaView;
	}
	public String getDiaView() {
		return diaView;
	}
	public void setDiaView(String diaView) {
		this.diaView = diaView;
	}
	public String getMesView() {
		return mesView;
	}
	public void setMesView(String mesView) {
		this.mesView = mesView;
	}
	
}
