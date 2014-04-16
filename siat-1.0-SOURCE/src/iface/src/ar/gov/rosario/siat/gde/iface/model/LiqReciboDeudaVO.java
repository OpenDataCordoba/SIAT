//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class LiqReciboDeudaVO {
	private static Logger log = Logger.getLogger(LiqReciboDeudaVO.class);
	
	private LiqDeudaVO deuda;
	
	private Double capitalOriginal =0D;

	private Double actualizacion =0D;
	
	private String conceptosDeuda;

	private String periodoDeuda;
	
	private Double desCapitalOriginal =0D; 
	
	private Double desActualizacion =0D;
	
	private Double totActualizacion =0D;
	
	private Double totCapital =0D;
	
	private Long idDescuentoAplicado;
	
	private Double totalReciboDeuda;
	
	private String tipoPago ="";
	
	private Double porcentajeComisionProc = 0D;// Es el % que se aplica en la liquidacion de comision a procuradores
	
	private Double importeComisionProc = 0D;// Es el importe que se aplica en la liquidacion de comision a procuradores
	
	private List<LiqConceptoDeudaVO> listConceptosDeuda= new ArrayList<LiqConceptoDeudaVO>();
	
	private LiqAtrEmisionVO liqAtrEmisionVO = new LiqAtrEmisionVO();
	
	public LiqReciboDeudaVO(){
		
	}
	
	public LiqReciboDeudaVO(LiqDeudaVO deuda, Double capitalOriginal, Double actualizacion, String conceptosDeuda) {
		super();
		this.deuda = deuda;
		this.capitalOriginal = capitalOriginal;
		this.actualizacion = actualizacion;
		this.conceptosDeuda = conceptosDeuda;
	}

	
	
	public Double getActualizacion() {
		return actualizacion;
	}

	public void setActualizacion(Double actualizacion) {
		this.actualizacion = actualizacion;
	}

	public Double getCapitalOriginal() {
		return capitalOriginal;
	}

	public void setCapitalOriginal(Double capitalOriginal) {
		this.capitalOriginal = capitalOriginal;
	}

	public String getConceptosDeuda() {
		return conceptosDeuda;
	}

	public void setConceptosDeuda(String conceptosDeuda) {
		this.conceptosDeuda = conceptosDeuda;
	}

	public LiqDeudaVO getDeuda() {
		return deuda;
	}

	public void setDeuda(LiqDeudaVO deuda) {
		this.deuda = deuda;
	}

	public Double getDesCapitalOriginal() {
		return desCapitalOriginal;
	}

	public void setDesCapitalOriginal(Double desCapitalOriginal) {
		this.desCapitalOriginal = desCapitalOriginal;
	}

	public Double getDesActualizacion() {
		return desActualizacion;
	}

	public void setDesActualizacion(Double desActualizacion) {
		this.desActualizacion = desActualizacion;
	}

	public Double getTotActualizacion() {
		return totActualizacion;
	}

	public void setTotActualizacion(Double totActualizacion) {
		this.totActualizacion = totActualizacion;
	}

	public Double getTotCapital() {
		return totCapital;
	}

	public void setTotCapital(Double totCapital) {
		this.totCapital = totCapital;
	}

	public Long getIdDescuentoAplicado() {
		return idDescuentoAplicado;
	}

	public void setIdDescuentoAplicado(Long idDescuentoAplicado) {
		this.idDescuentoAplicado = idDescuentoAplicado;
	}

	public String getPeriodoDeuda() {
		return periodoDeuda;
	}

	public void setPeriodoDeuda(String periodoDeuda) {
		this.periodoDeuda = periodoDeuda;
	}

	public Double getTotalReciboDeuda() {
		return totalReciboDeuda;
	}

	public void setTotalReciboDeuda(Double totalReciboDeuda) {
		this.totalReciboDeuda = totalReciboDeuda;
	}
		 
	public List<LiqConceptoDeudaVO> getListConceptosDeuda() {
		return listConceptosDeuda;
	}
	
	public void setListConceptosDeuda(List<LiqConceptoDeudaVO> listConceptosDeuda) {
		this.listConceptosDeuda = listConceptosDeuda;
	}
	
	public Double getDeudaConInteres(){
		return totCapital + actualizacion;
	}

	public Double getDeudaConDescuento(){
		return totCapital + totActualizacion;
	}
	
	public Double getPorcentajeComisionProc() {
		return porcentajeComisionProc;
	}

	public void setPorcentajeComisionProc(Double porcentajeComisionProc) {
		this.porcentajeComisionProc = porcentajeComisionProc;
	}
	
	public Double getImporteComisionProc() {
		return importeComisionProc;
	}

	public void setImporteComisionProc(Double importeComisionProc) {
		this.importeComisionProc = importeComisionProc;
	}

	public String getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}
	
	public LiqAtrEmisionVO getLiqAtrEmisionVO() {
		return liqAtrEmisionVO;
	}

	public void setLiqAtrEmisionVO(LiqAtrEmisionVO liqAtrEmisionVO) {
		this.liqAtrEmisionVO = liqAtrEmisionVO;
	}

	//View Getters
	public String getCapitalOriginalView(){
		return StringUtil.redondearDecimales(capitalOriginal, 1, 2);
	}
	
	public String getTotActualizacionView(){
		return StringUtil.redondearDecimales(totActualizacion, 1, 2);
	}
	
	public String getTotCoefActualizacionView(){
		return (this.totActualizacion!=null)?NumberUtil.truncate(totActualizacion, 6).toString():"";
	}
	
	public String getTotReciboDeudaView(){
		return StringUtil.redondearDecimales(totalReciboDeuda, 1, 2);
	}

	public String getDeudaConInteresView(){
		return StringUtil.redondearDecimales(getDeudaConInteres(), 1, 2);
	}

	public String getDeudaConDescuentoView(){
		return StringUtil.redondearDecimales(getDeudaConDescuento(), 1, 2);
	}
	
	public String getPorcentajeComisionProcView(){
		return StringUtil.redondearDecimales(getPorcentajeComisionProc(), 1, 2);
	}
	
	public String getImporteComisionProcView(){
		return StringUtil.redondearDecimales(getImporteComisionProc(), 1, 2);
	}

	/**
	 * Devuelve el total del recibo deuda, ya que este dato no se guarda.
	 * 
	 * @return Double
	 */
	public Double getTotalReciboDeudaCalculado(){
		Double totCalc = 0D;
		if (this.totCapital != null && this.totActualizacion != null)
			totCalc = this.totCapital + this.totActualizacion;
		
		return totCalc;
	}
	
	/**
	 * pregunta si el recibo es Cero
	 * 
	 * @return boolean
	 */
	public boolean isValorCero() {
	   Boolean rta=false;
       if (this.getTotCapital().doubleValue()==0D) rta=true;
		return rta;
	}

}
