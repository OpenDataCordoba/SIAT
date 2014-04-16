//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del ${Bean}
 * @author tecso
 *
 */
public class IndetVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "indetVO";
		
	private Long nroIndeterminado;
	private String sistema;
	private String nroComprobante;
	private String clave;
	private String resto;
	private Double importeCobrado;
	private Double importeBasico;
	private Double importeCalculado;
	private Double indice;
	private Double recargo;
	private String partida;
	private Integer codIndet;
	private Date fechaPago;
	private Integer caja;
	private Integer paquete;
	private Integer codPago;
	private Date fechaBalance;
	private Long codTr;
	private String filler;
	private Long reciboTr;
	private Integer tipoIngreso;
	private String usuario;
	private Date fechaHora;
	
	// Campos extra para reingreso
	private Long nroReing;
	private Date fechaReing;
		
	private String nroIndeterminadoView = "";
	private String importeCobradoView = "";
	private String importeBasicoView = "";
	private String importeCalculadoView = "";
	private String indiceView = "";
	private String recargoView = "";
	private String codIndetView = "";
	private String fechaPagoView = "";
	private String cajaView = "";
	private String paqueteView = "";
	private String codPagoView = "";
	private String fechaBalanceView = "";
	private String codTrView = "";
	private String reciboTrView = "";
	private String tipoIngresoView = "";
	private String fechaHoraView = "";
	private String nroReingView;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public IndetVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public IndetVO(int id, String desc) {
		super();
		setId(new Long(id));
	}

	// Getters y Setters
	
	public Integer getCaja() {
		return caja;
	}
	public void setCaja(Integer caja) {
		this.caja = caja;
		this.cajaView = StringUtil.formatInteger(caja);
	}
	public String getCajaView() {
		return cajaView;
	}
	public void setCajaView(String cajaView) {
		this.cajaView = cajaView;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public Integer getCodIndet() {
		return codIndet;
	}
	public void setCodIndet(Integer codIndet) {
		this.codIndet = codIndet;
		this.codIndetView = StringUtil.formatInteger(codIndet);
	}
	public String getCodIndetView() {
		return codIndetView;
	}
	public void setCodIndetView(String codIndetView) {
		this.codIndetView = codIndetView;
	}
	public Integer getCodPago() {
		return codPago;
	}
	public void setCodPago(Integer codPago) {
		this.codPago = codPago;
		this.codPagoView = StringUtil.formatInteger(codPago);
	}
	public String getCodPagoView() {
		return codPagoView;
	}
	public void setCodPagoView(String codPagoView) {
		this.codPagoView = codPagoView;
	}
	public Long getCodTr() {
		return codTr;
	}
	public void setCodTr(Long codTr) {
		this.codTr = codTr;
		this.codTrView = StringUtil.formatLong(codTr);
	}
	public String getCodTrView() {
		return codTrView;
	}
	public void setCodTrView(String codTrView) {
		this.codTrView = codTrView;
	}
	public Date getFechaBalance() {
		return fechaBalance;
	}
	public void setFechaBalance(Date fechaBalance) {
		this.fechaBalance = fechaBalance;
		this.fechaBalanceView = DateUtil.formatDate(fechaBalance, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaBalanceView() {
		return fechaBalanceView;
	}
	public void setFechaBalanceView(String fechaBalanceView) {
		this.fechaBalanceView = fechaBalanceView;
	}
	public Date getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
		this.fechaHoraView = DateUtil.formatDate(fechaHora, DateUtil.ddSMMSYYYY_HH_MM_MASK);
	}
	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
		this.fechaPagoView = DateUtil.formatDate(fechaPago, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaPagoView() {
		return fechaPagoView;
	}
	public void setFechaPagoView(String fechaPagoView) {
		this.fechaPagoView = fechaPagoView;
	}
	public String getFiller() {
		return filler;
	}
	public void setFiller(String filler) {
		this.filler = filler;
	}
	public Double getImporteBasico() {
		return importeBasico;
	}
	public void setImporteBasico(Double importeBasico) {
		this.importeBasico = importeBasico;
		this.importeBasicoView = StringUtil.formatDouble(importeBasico);
	}
	public String getImporteBasicoView() {
		return importeBasicoView;
	}
	public void setImporteBasicoView(String importeBasicoView) {
		this.importeBasicoView = importeBasicoView;
	}
	public Double getImporteCalculado() {
		return importeCalculado;
	}
	public void setImporteCalculado(Double importeCalculado) {
		this.importeCalculado = importeCalculado;
		this.importeCalculadoView = StringUtil.formatDouble(importeCalculado);
	}
	public String getImporteCalculadoView() {
		return importeCalculadoView;
	}
	public void setImporteCalculadoView(String importeCalculadoView) {
		this.importeCalculadoView = importeCalculadoView;
	}
	public Double getImporteCobrado() {
		return importeCobrado;
	}
	public void setImporteCobrado(Double importeCobrado) {
		this.importeCobrado = importeCobrado;
		this.importeCobradoView = StringUtil.formatDouble(importeCobrado);
	}
	public String getImporteCobradoView() {
		return importeCobradoView;
	}
	public void setImporteCobradoView(String importeCobradoView) {
		this.importeCobradoView = importeCobradoView;
	}
	public Double getIndice() {
		return indice;
	}
	public void setIndice(Double indice) {
		this.indice = indice;
		this.indiceView = StringUtil.formatDouble(indice);
	}
	public String getIndiceView() {
		return indiceView;
	}
	public void setIndiceView(String indiceView) {
		this.indiceView = indiceView;
	}
	public String getNroComprobante() {
		return nroComprobante;
	}
	public void setNroComprobante(String nroComprobante) {
		this.nroComprobante = nroComprobante;
	}
	public Long getNroIndeterminado() {
		return nroIndeterminado;
	}
	public void setNroIndeterminado(Long nroIndeterminado) {
		this.nroIndeterminado = nroIndeterminado;
		this.nroIndeterminadoView = StringUtil.formatLong(nroIndeterminado);
	}
	public Integer getPaquete() {
		return paquete;
	}
	public void setPaquete(Integer paquete) {
		this.paquete = paquete;
		this.paqueteView = StringUtil.formatInteger(paquete);
	}
	public String getPaqueteView() {
		return paqueteView;
	}
	public void setPaqueteView(String paqueteView) {
		this.paqueteView = paqueteView;
	}
	public String getPartida() {
		return partida;
	}
	public void setPartida(String partida) {
		this.partida = partida;
	}
	public Double getRecargo() {
		return recargo;
	}
	public void setRecargo(Double recargo) {
		this.recargo = recargo;
		this.recargoView = StringUtil.formatDouble(recargo);
	}
	public String getRecargoView() {
		return recargoView;
	}
	public void setRecargoView(String recargoView) {
		this.recargoView = recargoView;
	}
	public Long getReciboTr() {
		return reciboTr;
	}
	public void setReciboTr(Long reciboTr) {
		this.reciboTr = reciboTr;
		this.reciboTrView = StringUtil.formatLong(reciboTr);
	}
	public String getReciboTrView() {
		return reciboTrView;
	}
	public void setReciboTrView(String reciboTrView) {
		this.reciboTrView = reciboTrView;
	}
	public String getResto() {
		return resto;
	}
	public void setResto(String resto) {
		this.resto = resto;
	}
	public String getSistema() {
		return sistema;
	}
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	public Integer getTipoIngreso() {
		return tipoIngreso;
	}
	public void setTipoIngreso(Integer tipoIngreso) {
		this.tipoIngreso = tipoIngreso;
		this.tipoIngresoView = StringUtil.formatInteger(tipoIngreso);
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getFechaHoraView() {
		return fechaHoraView;
	}
	public void setFechaHoraView(String fechaHoraView) {
		this.fechaHoraView = fechaHoraView;
	}
	public String getNroIndeterminadoView() {
		return nroIndeterminadoView;
	}
	public void setNroIndeterminadoView(String nroIndeterminadoView) {
		this.nroIndeterminadoView = nroIndeterminadoView;
	}
	public String getTipoIngresoView() {
		return tipoIngresoView;
	}
	public void setTipoIngresoView(String tipoIngresoView) {
		this.tipoIngresoView = tipoIngresoView;
	}
	public Date getFechaReing() {
		return fechaReing;
	}
	public void setFechaReing(Date fechaReing) {
		this.fechaReing = fechaReing;
	}
	public Long getNroReing() {
		return nroReing;
	}
	public void setNroReing(Long nroReing) {
		this.nroReing = nroReing;
		this.nroReingView = StringUtil.formatLong(nroReing);
	}
	public String getNroReingView() {
		return nroReingView;
	}
	public void setNroReingView(String nroReingView) {
		this.nroReingView = nroReingView;
	}

	public Boolean validate(){
		if(this.getSistema() == null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_SISTEMA);
		}
		if(this.getPartida() == null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_PARTIDA);
		}
		if(this.getFechaPago() == null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_FECHAPAGO);
		}
		if(this.getFechaBalance() == null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_FECHABALANCE);
		}
		
		if(this.hasError()){
			return true;
		}
		
		if(!StringUtil.isNumeric(this.getSistema())){
			this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, BalError.INDET_PARTIDA);
		}
		if(!StringUtil.isNumeric(this.getPartida())){
			this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, BalError.INDET_PARTIDA);
		}
		return !this.hasError();
	}
	
	
	public Boolean validateCreate(){
		if(StringUtil.isNullOrEmpty(this.getSistema())){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_SISTEMA);
		}
		if(StringUtil.isNullOrEmpty(this.getPartida())){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_PARTIDA);
		}
		if(this.getFechaPago() == null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_FECHAPAGO);
		}
		if(this.getFechaBalance() == null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_FECHABALANCE);
		}
		if(StringUtil.isNullOrEmpty(this.getNroComprobante())){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_NROCOMPROBANTE);
		}
		if(StringUtil.isNullOrEmpty(this.getClave())){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_CLAVE);
		}
		if(StringUtil.isNullOrEmpty(this.getResto())){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_RESTO);
		}
		if(this.getCodIndet() == null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_CODINDET);
		}
		if(this.getCaja() == null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_CAJA);
		}
		if(this.getCodPago() == null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_CODPAGO);
		}
		if(this.getReciboTr() == null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_RECIBOTR);
		}
		if(this.getPaquete() == null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_PAQUETE);
		}
		if(this.getImporteCobrado() == null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_IMPORTECOBRADO);
		}
		if(this.getImporteBasico() == null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_IMPORTEBASICO);
		}
		if(this.getImporteCalculado() == null){
			this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.INDET_IMPORTECALCULADO);
		}
	
		if(this.hasError()){
			return true;
		}
		
		if(!StringUtil.isNumeric(this.getSistema())){
			this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, BalError.INDET_PARTIDA);
		}
		if(!StringUtil.isNumeric(this.getPartida())){
			this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, BalError.INDET_PARTIDA);
		}
		return !this.hasError();
	}
	
}
