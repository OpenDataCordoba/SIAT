//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.ef.iface.model.DetAjuDetVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.NumberUtil;

/**
 * Value Object de Detalle de Gestion Cobranza
 * @author tecso
 *
 */
public class CobranzaDetVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "cobranzaDetVO";
	
	private CobranzaVO cobranza = new CobranzaVO();
	
	private Date fecha;
	
	private CuentaVO cuenta = new CuentaVO();
	
	private DetAjuDetVO detAjuDet = new DetAjuDetVO();
	
	private Integer periodo;
	
	private Integer anio;
	
	private Double importeInicial;
	
	private Double ajuste;
	
	private EstadoAjusteVO estadoAjuste = new EstadoAjusteVO();
	
	private Long idDeuda;
	
	private DeudaVO deudaVO = new DeudaVO();
	
	private Double pagos;
	
	private Double enConVig;
	
	private Double enConCad;
	
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public CobranzaDetVO() {
		super();
	}

	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.


	// Getters y Setters

	
	


	public CobranzaVO getCobranza() {
		return cobranza;
	}


	public void setCobranza(CobranzaVO cobranza) {
		this.cobranza = cobranza;
	}


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public CuentaVO getCuenta() {
		return cuenta;
	}


	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}


	public DetAjuDetVO getDetAjuDet() {
		return detAjuDet;
	}


	public void setDetAjuDet(DetAjuDetVO detAjuDet) {
		this.detAjuDet = detAjuDet;
	}


	public Integer getPeriodo() {
		return periodo;
	}


	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}


	public Integer getAnio() {
		return anio;
	}


	public void setAnio(Integer anio) {
		this.anio = anio;
	}


	public Double getImporteInicial() {
		return importeInicial;
	}


	public void setImporteInicial(Double importeInicial) {
		this.importeInicial = importeInicial;
	}


	public Double getAjuste() {
		return ajuste;
	}


	public void setAjuste(Double ajuste) {
		this.ajuste = ajuste;
	}


	public EstadoAjusteVO getEstadoAjuste() {
		return estadoAjuste;
	}


	public void setEstadoAjuste(EstadoAjusteVO estadoAjuste) {
		this.estadoAjuste = estadoAjuste;
	}


	public Long getIdDeuda() {
		return idDeuda;
	}


	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}


	public DeudaVO getDeudaVO() {
		return deudaVO;
	}


	public void setDeudaVO(DeudaVO deudaVO) {
		this.deudaVO = deudaVO;
	}
	
	public Double getEnConVig() {
		return enConVig;
	}


	public void setEnConVig(Double enConVig) {
		this.enConVig = enConVig;
	}


	public Double getEnConCad() {
		return enConCad;
	}


	public void setEnConCad(Double enConCad) {
		this.enConCad = enConCad;
	}


	public Double getPagos() {
		return pagos;
	}


	public void setPagos(Double pagos) {
		this.pagos = pagos;
	}
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	



	// View getters
	public String getCuentaView(){
		return cuenta.getRecurso().getCodRecurso()+" "+cuenta.getNumeroCuenta();
	}
	
	public String getPeriodoAnioView(){
		return periodo.toString()+"/"+anio.toString();
	}
	
	public String getImporteInicialView(){
		return (this.importeInicial!=null)?NumberUtil.round(this.importeInicial, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getAjusteView(){
		return (this.ajuste!=null)?NumberUtil.round(this.ajuste, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getTotalPeriodoView(){
		Double aj = (this.ajuste!=null && this.ajuste>0)?this.ajuste:0D;
		Double impIni = (this.importeInicial!=null)?this.importeInicial:0D;
		return NumberUtil.round((aj+impIni),SiatParam.DEC_IMPORTE_VIEW).toString();
	}
	
	public String getPagosView(){
		return (this.pagos!=null)?NumberUtil.round(this.pagos, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getEnConVigView(){
		return (this.enConVig!=null)?NumberUtil.round(this.enConVig,SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getEnConCadView(){
		return (this.enConCad!=null)?NumberUtil.round(this.enConCad,SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getDiferenciaView(){
		Double aj = (this.ajuste!=null && this.ajuste>0)?this.ajuste:0D;
		Double impIni = (this.importeInicial!=null)?this.importeInicial:0D;
		Double pag = (this.pagos!=null)?this.pagos:0D;
		Double conVig = (this.enConVig!=null)?this.enConVig:0D;
		Double conCad = (this.enConCad!=null)?this.enConCad:0D;
		return NumberUtil.round(aj+impIni-pag-conVig-conCad,SiatParam.DEC_IMPORTE_VIEW).toString();
	}
}
