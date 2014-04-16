//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecConADecVO;
import ar.gov.rosario.siat.def.iface.model.RecTipUniVO;
import coop.tecso.demoda.iface.helper.StringUtil;

public class LiqDetalleDecJurVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(LiqDetalleDecJurVO.class);
	
	// ---> propiedades para ambos
	private Integer periodo;
	private Integer anio;
	// <--- propiedades para ambos
		
	// ---> propiedades para detalle
	private RecConADecVO actividad = new RecConADecVO();
	private Double montoImponible = 0D;
	private Double alicuota = 0D;
	private Double subTotal1 = 0D;
	
	private Integer cantUni;
	private RecTipUniVO unidad = new RecTipUniVO();  // Unidad
	private RecConADecVO tipoUnidad = new RecConADecVO(); // TipoUnidad
	private Double valorUni = 0D;
	private Double subTotal2 = 0D;
	// <--- propiedades para detalle
	
	// ---> propiedades para general
	private Integer cantPer = 0;
	private Double minCantPer = 0D;
	private Double determinado = 0D;
	private Double porcAdicPub = 0D;
	private Double adicPub = 0D;
	private Double porcAdicOtro = 0D;
	private Double adicOtro = 0D;
	private Double retencion = 0D;
	private Double pago = 0D;
	private Double declarado = 0D;
	private Double enConvenio = 0D;
	private Double total = 0D;
	private List<DecJurPagVO> listDecJurPag = new ArrayList<DecJurPagVO>(); // soporte para retenciones imputadas
	
	private List<DecJurDetVO> listDetJurDet = new ArrayList<DecJurDetVO>();  
	// <--- propiedades para general
	
	
    private String periodoView = "";
	private String anioView = "";
	private String montoImponibleView = "";
	private String alicuotaView = "";	
	private String subTotal1View = "";
	private String cantUniView = "";
	private String valorUniView = "";
	private String subTotal2View = "";
	
	private String cantPerView = "";
	private String minCantPerView = "";
	private String determinadoView = "";
	private String porcAdicPubView = "";
	private String porcAdicOtroView = "";
	private String adicPubView = "";
	private String adicOtroView = "";
	private String retencionView = "";
	private String pagoView = "";
	private String declaradoView = "";;
	private String enConvenioView = "";;
	private String totalView = "";
	
	
    // Constructor
    public LiqDetalleDecJurVO(){
    	
    }

    //  Getters y Setters
	public Integer getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
		this.periodoView = StringUtil.formatInteger(periodo);
	}

	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
		this.anioView = StringUtil.formatInteger(anio);
	}

	public RecConADecVO getActividad() {
		return actividad;
	}
	public void setActividad(RecConADecVO actividad) {
		this.actividad = actividad;
	}

	public Double getMontoImponible() {
		return montoImponible;
	}
	public void setMontoImponible(Double montoImponible) {
		this.montoImponible = montoImponible;
		this.montoImponibleView = StringUtil.formatDouble(montoImponible);
	}

	public Double getAlicuota() {
		return alicuota;
	}
	public void setAlicuota(Double alicuota) {
		this.alicuota = alicuota;
		this.alicuotaView = StringUtil.formatDouble(alicuota);
	}

	public Double getSubTotal1() {
		return subTotal1;
	}
	public void setSubTotal1(Double subTotal1) {
		this.subTotal1 = subTotal1;
		this.subTotal1View = StringUtil.formatDouble(subTotal1, "##########.##");
	}

	public Integer getCantUni() {
		return cantUni;
	}
	public void setCantUni(Integer cantUni) {
		this.cantUni = cantUni;
		this.cantUniView = StringUtil.formatInteger(cantUni);
	}

	public RecTipUniVO getUnidad() {
		return unidad;
	}
	public void setUnidad(RecTipUniVO unidad) {
		this.unidad = unidad;
	}

	public RecConADecVO getTipoUnidad() {
		return tipoUnidad;
	}
	public void setTipoUnidad(RecConADecVO tipoUnidad) {
		this.tipoUnidad = tipoUnidad;
	}

	public String getCantUniView() {
		return cantUniView;
	}
	public void setCantUniView(String cantUniView) {
		this.cantUniView = cantUniView;
	}

	public Double getValorUni() {
		return valorUni;
	}
	public void setValorUni(Double valorUni) {
		this.valorUni = valorUni;
		this.valorUniView = StringUtil.formatDouble(valorUni);
	}
	
	public Double getSubTotal2() {
		return subTotal2;
	}
	public void setSubTotal2(Double subTotal2) {
		this.subTotal2 = subTotal2;
		this.subTotal2View = StringUtil.formatDouble(subTotal2, "##########.##");
	}

	public Integer getCantPer() {
		return cantPer;
	}
	public void setCantPer(Integer cantPer) {
		this.cantPer = cantPer;
		this.cantPerView = StringUtil.formatInteger(cantPer);
	}

	public Double getMinCantPer() {
		return minCantPer;
	}
	public void setMinCantPer(Double minCantPer) {
		this.minCantPer = minCantPer;
		this.minCantPerView = StringUtil.formatDouble(minCantPer);
	}

	public Double getDeterminado() {
		return determinado;
	}
	public void setDeterminado(Double determinado) {
		this.determinado = determinado;
		this.determinadoView = StringUtil.formatDouble(determinado, "##########.##");
	}

	public Double getAdicPub() {
		return adicPub;
	}
	public void setAdicPub(Double adicPub) {
		this.adicPub = adicPub;
		this.adicPubView = StringUtil.formatDouble(adicPub, "##########.##");
	}

	public Double getAdicOtro() {
		return adicOtro;
	}
	public void setAdicOtro(Double adicOtro) {
		this.adicOtro = adicOtro;
		this.adicOtroView = StringUtil.formatDouble(adicOtro, "##########.##");
	}

	public Double getRetencion() {
		return retencion;
	}
	public void setRetencion(Double retencion) {
		this.retencion = retencion;
		this.retencionView = StringUtil.formatDouble(retencion, "##########.##");
	}

	public Double getPago() {
		return pago;
	}
	public void setPago(Double pago) {
		this.pago = pago;
		this.pagoView = StringUtil.formatDouble(pago);
	}

	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
		this.totalView = StringUtil.formatDouble(total, "##########.##");
	}

	public Double getPorcAdicPub() {
		return porcAdicPub;
	}
	public void setPorcAdicPub(Double porcAdicPub) {
		this.porcAdicPub = porcAdicPub;
		this.porcAdicPubView = StringUtil.formatDouble(porcAdicPub==null?0D:porcAdicPub*100);
	}

	public Double getPorcAdicOtro() {
		return porcAdicOtro;
	}
	public void setPorcAdicOtro(Double porcAdicOtro) {
		this.porcAdicOtro = porcAdicOtro;
		this.porcAdicOtroView = StringUtil.formatDouble(porcAdicOtro==null?0D:porcAdicOtro*100);
	}

	public Double getDeclarado() {
		return declarado;
	}
	public void setDeclarado(Double declarado) {
		this.declarado = declarado;
		this.declaradoView = StringUtil.formatDouble(declarado);
	}

	public Double getEnConvenio() {
		return enConvenio;
	}
	public void setEnConvenio(Double enConvenio) {
		this.enConvenio = enConvenio;
		this.enConvenioView = StringUtil.formatDouble(enConvenio);
	}

	public List<DecJurPagVO> getListDecJurPag() {
		return listDecJurPag;
	}
	public void setListDecJurPag(List<DecJurPagVO> listDecJurPag) {
		this.listDecJurPag = listDecJurPag;
	}
	
	public List<DecJurDetVO> getListDetJurDet() {
		return listDetJurDet;
	}
	public void setListDetJurDet(List<DecJurDetVO> listDetJurDet) {
		this.listDetJurDet = listDetJurDet;
	}

	public void calcularTotal(){
		Double total = 0D;
		Double pago = 0D;
		Double deuda = 0D;
		
		if (this.getDeterminado()!= null){
			total += this.getDeterminado();
		}
			
		if (this.getAdicPub() != null){
			total += this.getAdicPub(); 
		}
		
		if (this.getAdicOtro() != null){
			total += this.getAdicOtro(); 
		}
		
		if (this.getRetencion() != null){
			total -= this.getRetencion(); 
		}
		
		// Pago
		if (this.getPago() != null ){
			pago = this.getPago();
		}
		
		// Suma de declarado y en convenio 
		if (this.getDeclarado() != null && this.getEnConvenio() != null){
			deuda = this.getDeclarado() + this.getEnConvenio();
		} else if (this.getDeclarado() != null && this.getEnConvenio() == null){
			deuda = this.getDeclarado();
		} else if (this.getDeclarado() == null && this.getEnConvenio() != null){
			deuda = this.getEnConvenio();
		}
		
		// El total no puede ser negativo
		if (pago.doubleValue() >= total.doubleValue() || deuda.doubleValue() >= total.doubleValue()){
			total = 0D;
		} else {
			// Seteamos el mayor de los dos valores, si son iguales, queda uno de los dos :)
			if (pago.doubleValue() > deuda.doubleValue()){
				total -= pago;
			} else {
				total -= deuda;
			}	
		}
	
		setTotal(total);
	}
	
	
	
	public Double getMayorOtrosPagos(){
		
		Double pago = 0D;
		Double deuda = 0D;
		
		// Pago
		if (this.getPago() != null ){
			pago = this.getPago();
		}
		
		// Suma de declarado y en convenio 
		if (this.getDeclarado() != null && this.getEnConvenio() != null){
			deuda = this.getDeclarado() + this.getEnConvenio();
		} else if (this.getDeclarado() != null && this.getEnConvenio() == null){
			deuda = this.getDeclarado();
		} else if (this.getDeclarado() == null && this.getEnConvenio() != null){
			deuda = this.getEnConvenio();
		}
		
		// Pago mayor Declarado + EnConvenio
		if (pago.doubleValue() > deuda.doubleValue()){
			log.debug(" MayorOtrosPagos -> pago " + pago);
			return pago;
		} else {
		// Pago menor o igual Declarado + EnConvenio
			log.debug(" MayorOtrosPagos -> deuda " + deuda);
			return  deuda;
		}	
	}
	
	
	/**
	 * Devuelve una cadena con el o los numeros de certificados.
	 * 
	 * @return
	 */
	public String getCertificadosView(){
		String ret = "";
		
		for (DecJurPagVO djp:getListDecJurPag()){
			ret += djp.getCertificado() + ", "; 
		}
		
		if (!"".equals(ret)){
			ret = "Certificado Nro.: " + ret; 
		}
			
		return ret;
	}
	
	// View Getters
	
	public String getPeriodoView() {
		return periodoView;
	}
	public void setPeriodoView(String periodoView) {
		this.periodoView = periodoView;
	}

	public String getAnioView() {
		return anioView;
	}
	public void setAnioView(String anioView) {
		this.anioView = anioView;
	}

	public String getPeriodoAnioView() {
		return periodoView + "/"+ anioView;
	}

	public String getMontoImponibleView() {
		return montoImponibleView;
	}
	public void setMontoImponibleView(String montoImponibleView) {
		this.montoImponibleView = montoImponibleView;
	}

	public String getAlicuotaView() {
		return StringUtil.isNullOrEmpty(alicuotaView) || "0".equals(alicuotaView) ?"0.00":alicuotaView;
	}
	public void setAlicuotaView(String alicuotaView) {
		this.alicuotaView = alicuotaView;
	}

	public String getSubTotal1View() {
		return StringUtil.isNullOrEmpty(subTotal1View) || "0".equals(subTotal1View)?"0.00":subTotal1View;
	}
	public void setSubTotal1View(String subTotal1View) {
		this.subTotal1View = subTotal1View;
	}

	public String getValorUniView() {
		return StringUtil.isNullOrEmpty(valorUniView) || "0".equals(valorUniView)?"0.00":valorUniView;
	}
	public void setValorUniView(String valorUniView) {
		this.valorUniView = valorUniView;
	}

	public String getSubTotal2View() {
		return StringUtil.isNullOrEmpty(subTotal2View) || "0".equals(subTotal2View)?"0.00":subTotal2View;
	}
	public void setSubTotal2View(String subTotal2View) {
		this.subTotal2View = subTotal2View;
	}

	public String getCantPerView() {
		return cantPerView;
	}
	public void setCantPerView(String cantPerView) {
		this.cantPerView = cantPerView;
	}

	public String getMinCantPerView() {
		return StringUtil.isNullOrEmpty(minCantPerView) || "0".equals(minCantPerView)?"0.00":minCantPerView;
	}
	public void setMinCantPerView(String minCantPerView) {
		this.minCantPerView = minCantPerView;
	}

	public String getDeterminadoView() {
		return StringUtil.isNullOrEmpty(determinadoView) || "0".equals(determinadoView)?"0.00":determinadoView;
	}
	public void setDeterminadoView(String determinadoView) {
		this.determinadoView = determinadoView;
	}

	public String getAdicPubView() {
		return StringUtil.isNullOrEmpty(adicPubView) || "0".equals(adicPubView)?"0.00":adicPubView;
	}
	public void setAdicPubView(String adicPubView) {
		this.adicPubView = adicPubView;
	}

	public String getAdicOtroView() {
		return StringUtil.isNullOrEmpty(adicOtroView) || "0".equals(adicOtroView)?"0.00":adicOtroView;
	}
	public void setAdicOtroView(String adicOtroView) {
		this.adicOtroView = adicOtroView;
	}

	public String getRetencionView() {
		return StringUtil.isNullOrEmpty(retencionView) || "0".equals(retencionView)?"0.00":retencionView;
	}
	public void setRetencionView(String retencionView) {
		this.retencionView = retencionView;
	}

	public String getPagoView() {
		return StringUtil.isNullOrEmpty(pagoView) || "0".equals(pagoView)?"0.00":pagoView;
	}
	public void setPagoView(String pagoView) {
		this.pagoView = pagoView;
	}

	public String getTotalView() {
		return StringUtil.isNullOrEmpty(totalView) || "0".equals(totalView)?"0.00":totalView;
	}
	public void setTotalView(String totalView) {
		this.totalView = totalView;
	}

	public String getPorcAdicPubView() {
		return porcAdicPubView;
	}
	public void setPorcAdicPubView(String porcAdicPubView) {
		this.porcAdicPubView = porcAdicPubView;
	}

	public String getPorcAdicOtroView() {
		return porcAdicOtroView;
	}
	public void setPorcAdicOtroView(String porcAdicOtroView) {
		this.porcAdicOtroView = porcAdicOtroView;
	}

	public String getDeclaradoView() {
		return StringUtil.isNullOrEmpty(declaradoView) || "0".equals(declaradoView)?"0.00":declaradoView;
	}
	public void setDeclaradoView(String declaradoView) {
		this.declaradoView = declaradoView;
	}

	public String getEnConvenioView() {
		return StringUtil.isNullOrEmpty(enConvenioView) || "0".equals(enConvenioView)?"0.00":enConvenioView;
	}
	public void setEnConvenioView(String enConvenioView) {
		this.enConvenioView = enConvenioView;
	}

}
