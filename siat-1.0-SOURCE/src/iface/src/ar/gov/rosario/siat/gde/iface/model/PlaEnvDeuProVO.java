//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object de la Planilla que agrupa la Deuda enviada a un Procurador
 * @author tecso
 *
 */
public class PlaEnvDeuProVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "plaEnvDeuPrVO";
	
	private Integer anioPlanilla;
	private Long    nroPlanilla;
	private ProcuradorVO procurador = new ProcuradorVO();
	private Date fechaEnvio;
	private Date fechaRecepcion;
	private Date fechaHabilitacion;

	private String observaciones;

	private ProcesoMasivoVO  procesoMasivo = new ProcesoMasivoVO();
	private EstPlaEnvDeuPrVO estPlaEnvDeuPr = new EstPlaEnvDeuPrVO();

	private String fechaEnvioView     = "";
	private String fechaRecepcionView = "";
	private String fechaHabilitacionView = " ";
	
	private Long   totalRegistros;     
	private Double importeTotal;
	private Long   cantidadCuentas;
	
	private CasoVO caso = new CasoVO(); 
	
	private List<HistEstPlaEnvDPVO> listHistEstPlaEnvDP = new ArrayList<HistEstPlaEnvDPVO>();
	private List<ConstanciaDeuVO> listConstanciaDeu = new ArrayList<ConstanciaDeuVO>();
	
	// Buss Flags
	boolean recomponerPlanillaBussEnabled = true;
	boolean habilitarPlanillaBussEnabled = true;
	private boolean imprimirPadronEnabled = true;

	// View Constants
	

	// Constructores
	public PlaEnvDeuProVO() {
		super();
	}
	
	// Getters y Setters
	public Integer getAnioPlanilla() {
		return anioPlanilla;
	}
	public void setAnioPlanilla(Integer anioPlanilla) {
		this.anioPlanilla = anioPlanilla;
	}
	public ProcesoMasivoVO getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivoVO procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
	public EstPlaEnvDeuPrVO getEstPlaEnvDeuPr() {
		return estPlaEnvDeuPr;
	}
	public void setEstPlaEnvDeuPr(EstPlaEnvDeuPrVO estPlaEnvDeuPr) {
		this.estPlaEnvDeuPr = estPlaEnvDeuPr;
	}
	public Date getFechaEnvio() {
		return fechaEnvio;
	}
	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
		this.fechaEnvioView = DateUtil.formatDate(fechaEnvio, DateUtil.ddSMMSYYYY_MASK);
	}
	public Date getFechaRecepcion() {
		return fechaRecepcion;
	}
	public void setFechaRecepcion(Date fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}
	public Long getNroPlanilla() {
		return nroPlanilla;
	}
	public void setNroPlanilla(Long nroPlanilla) {
		this.nroPlanilla = nroPlanilla;
	}
	public ProcuradorVO getProcurador() {
		return procurador;
	}
	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}
	public Long getCantidadCuentas() {
		return cantidadCuentas;
	}
	public void setCantidadCuentas(Long cantidadCuentas) {
		this.cantidadCuentas = cantidadCuentas;
	}
	public Double getImporteTotal() {
		return importeTotal;
	}
	public void setImporteTotal(Double importeTotal) {
		this.importeTotal = importeTotal;
	}
	public Long getTotalRegistros() {
		return totalRegistros;
	}
	public void setTotalRegistros(Long totalRegistros) {
		this.totalRegistros = totalRegistros;
	}
	public Date getFechaHabilitacion() {
		return fechaHabilitacion;
	}

	public void setFechaHabilitacion(Date fechaHabilitacion) {
		this.fechaHabilitacion = fechaHabilitacion;
		this.fechaHabilitacionView = DateUtil.formatDate(fechaHabilitacion, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public List<HistEstPlaEnvDPVO> getListHistEstPlaEnvDP() {
		return listHistEstPlaEnvDP;
	}

	public void setListHistEstPlaEnvDP(List<HistEstPlaEnvDPVO> listHistEstPlaEnvDP) {
		this.listHistEstPlaEnvDP = listHistEstPlaEnvDP;
	}

	public List<ConstanciaDeuVO> getListConstanciaDeu() {
		return listConstanciaDeu;
	}

	public void setListConstanciaDeu(List<ConstanciaDeuVO> listConstanciaDeuVO) {
		this.listConstanciaDeu = listConstanciaDeuVO;
	}

	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}

	// Buss flags getters y setters
	public boolean getRecomponerPlanillaBussEnabled() {
		return recomponerPlanillaBussEnabled;
	}

	public void setRecomponerPlanillaBussEnabled(boolean recomponerPlanillaBussEnabled) {
		this.recomponerPlanillaBussEnabled = recomponerPlanillaBussEnabled;
	}
	
	public boolean getHabilitarPlanillaBussEnabled() {
		return habilitarPlanillaBussEnabled;
	}

	public void setHabilitarPlanillaBussEnabled(boolean habilitarPlanillaBussEnabled) {
		this.habilitarPlanillaBussEnabled = habilitarPlanillaBussEnabled;
	}
	
	// View flags getters

	// View getters
	public String getNroPlanillaView(){
		return StringUtil.formatLong(this.getNroPlanilla());
	}
	
	public String getAnioPlanillaView(){
		return StringUtil.formatInteger(this.getAnioPlanilla());
	}
	
	public String getNroBarraAnioPlanillaView(){
		String nro = this.getNroPlanillaView();
		String anio =this.getAnioPlanillaView();
		if(nro!=null && !nro.equals("") && anio!=null && !anio.equals(""))
			return nro + "/" + anio;
		return "";		
	}
	
	public String getTotalRegistrosView(){
		return StringUtil.formatLong(this.getTotalRegistros());
	}
	
	public String getImporteTotalView(){
		return StringUtil.formatDouble(this.getImporteTotal());
	}     
	
	public String getCantidadCuentasView(){
		return StringUtil.formatLong(this.getCantidadCuentas());
	}

	public String getFechaEnvioView() {
		return fechaEnvioView;
	}

	public void setFechaEnvioView(String fechaEnvioView) {
		this.fechaEnvioView = fechaEnvioView;
	}
	
	public String getFechaRecepcionView() {
		return fechaRecepcionView;
	}
	
	public void setFechaRecepcionView(String fechaRecepcionView) {
		this.fechaRecepcionView = fechaRecepcionView;
	}

	public String getFechaHabilitacionView() {
		return fechaHabilitacionView;
	}

	public void setFechaHabilitacionView(String fechaHabilitacionView) {
		this.fechaHabilitacionView = fechaHabilitacionView;
	}

	public String getFechaEnLetras(){
		return DateUtil.getDateEnLetras(new Date());
	}
	
	public String getNombreMes(){
		return StringUtil.cutAndUpperCase(DateUtil.getMesEnLetra(new Date()));
	}
		
	public String getTotalEnPalabrasView(){
		//hacer que funque con el totalView - arobledo
		String nroEnPalabras = NumberUtil.getNroEnPalabras(this.importeTotal, true);
		return nroEnPalabras;
	}

	/**
	 * Obtiene la descripcion del recurso: <br>
	 * 	- si tiene proceso masivo, devuelve el recurso de ese.<br>
	 * 	- si no tiene proceso masico => es de un traspaso, saca el recurso de la cuenta de la unica constancia que tiene en la lista
	 * @return
	 */
	public String getDesRecurso(){
		try{
			if(ModelUtil.isNullOrEmpty(getProcesoMasivo())){
				return ((ConstanciaDeuVO)getListConstanciaDeu().get(0)).getCuenta().getRecurso().getDesRecurso();
			}else{
				String desRec = getProcesoMasivo().getRecurso().getDesRecurso();
				return desRec;
			}
		}catch(Exception e){
			return "";
		}
	}
	
	/**
	 * Calcula el total sumando el importe de cada deuda en listConDeuDet y o setea en la propiedad "importeTotal"
	 * @return
	 */
	public void calcularTotalImporte(){
		this.importeTotal= 0D;
		if(listConstanciaDeu!=null){
			for(ConstanciaDeuVO constanciaDeu: listConstanciaDeu){
				constanciaDeu.calcularTotalSaldo();
				this.importeTotal += constanciaDeu.getTotal();
			}
		}
		importeTotal = new Double(StringUtil.parseComaToPoint(StringUtil.redondearDecimales(importeTotal, 4, 2)));
	}

	public void setImprimirPadronEnabled(boolean imprimirPadronEnabled) {
		this.imprimirPadronEnabled = imprimirPadronEnabled;
	}

	public boolean getImprimirPadronEnabled() {
		return imprimirPadronEnabled;
	}

	
}
