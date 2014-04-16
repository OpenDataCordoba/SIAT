//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.AlcanceEturAfip;
import coop.tecso.demoda.iface.model.UnidadesMedidasAfip;

/**
 * Value Object del DecActLoc
 * @author tecso
 *
 */
public class DecActLocVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "decActLocVO";	

	private Double  	baseImpExenta;	
	private Double 		baseImponible;	
	private Double    	ajuCamCoe;	
	private Double   	baseImpAjustada;	
	private Double    	aliCuota;	
	private Double    	derechoCalculado;	
	private Double    	cantidad;
	private Double    	minimoPorUnidad;	
	private Double   	minimoCalculado;	
	private Double    	derechoDet;	
	private Integer 	unidadMedida;	
	private Integer 	tipoUnidad;
	private Integer  	alcanceEtur;	
	private Long	 	codActividad;	
	private Double 		difBaseEnero;	
	private Double 		difBaseFebrero;	
	private Double 		difBaseMarzo;	
	private Double 		difBaseAbril;	
	private Double 		difBaseMayo;	
	private Double 		difBaseJunio;	
	private Double  	difBaseJulio;	
	private Double		difBaseAgosto;
	private Double 		difBaseSeptiembre;	
	private Double 		difBaseOctubre;	
	private Double  	difBaseNoviembre;	
	private Double  	difBaseDiciembre;
	private String  	numeroCuenta="";
	private String  	baseImpExentaView="";	
	private String 		baseImponibleView="";	
	private String    	ajuCamCoeView="";	
	private String   	baseImpAjustadaView="";	
	private String    	aliCuotaView="";	
	private String    	derechoCalculadoView="";	
	private String    	cantidadView="";
	private String    	minimoPorUnidadView="";	
	private String   	minimoCalculadoView="";	
	private String    	derechoDetView="";	
	private String 		unidadMedidaView="";	
	private String 		tipoUnidadView="";		
	private String	 	codActividadView="";	
	private String 		difBaseEneroView="";	
	private String 		difBaseFebreroView="";	
	private String 		difBaseMarzoView="";	
	private String 		difBaseAbrilView="";	
	private String 		difBaseMayoView="";	
	private String 		difBaseJunioView="";	
	private String  	difBaseJulioView="";	
	private String		difBaseAgostoView="";	
	private String 		difBaseSeptiembreView="";	
	private String 		difBaseOctubreView="";	
	private String  	difBaseNoviembreView="";	
	private String  	difBaseDiciembreView="";
	
	private LocalVO 	local		= new LocalVO();

	// Constructores
	public DecActLocVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public DecActLocVO(int id) {
		super();
		setId(new Long(id));		
	}

	
	// Getters y Setters
	public Double getBaseImpExenta() {
		return baseImpExenta;
	}

	public void setBaseImpExenta(Double baseImpExenta) {
		this.baseImpExenta = baseImpExenta;
		this.baseImpExentaView = StringUtil.formatDouble(baseImpExenta);
	}

	public Double getBaseImponible() {
		return baseImponible;
	}

	public void setBaseImponible(Double baseImponible) {
		this.baseImponible = baseImponible;
		this.baseImponibleView = StringUtil.formatDouble(baseImponible);
	}

	public Double getAjuCamCoe() {
		return ajuCamCoe;
	}

	public void setAjuCamCoe(Double ajuCamCoe) {
		this.ajuCamCoe = ajuCamCoe;
		this.ajuCamCoeView = StringUtil.formatDouble(ajuCamCoe);
	}

	public Double getBaseImpAjustada() {
		return baseImpAjustada;
	}

	public void setBaseImpAjustada(Double baseImpAjustada) {
		this.baseImpAjustada = baseImpAjustada;
		this.baseImpAjustadaView = StringUtil.formatDouble(baseImpAjustada);
	}

	public Double getAliCuota() {
		return aliCuota;
	}

	public void setAliCuota(Double aliCuota) {
		this.aliCuota = aliCuota;
		this.aliCuotaView = StringUtil.formatDouble(aliCuota);
	}

	public Double getDerechoCalculado() {
		return derechoCalculado;
	}

	public void setDerechoCalculado(Double derechoCalculado) {
		this.derechoCalculado = derechoCalculado;
		this.derechoCalculadoView = StringUtil.formatDouble(derechoCalculado);
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
		this.cantidadView = StringUtil.formatDouble(cantidad);
	}

	public Double getMinimoPorUnidad() {
		return minimoPorUnidad;
	}

	public void setMinimoPorUnidad(Double minimoPorUnidad) {
		this.minimoPorUnidad = minimoPorUnidad;
		this.minimoPorUnidadView = StringUtil.formatDouble(minimoPorUnidad);
	}

	public Double getMinimoCalculado() {
		return minimoCalculado;
	}

	public void setMinimoCalculado(Double minimoCalculado) {
		this.minimoCalculado = minimoCalculado;
		this.minimoCalculadoView = StringUtil.formatDouble(minimoCalculado);
	}

	public Double getDerechoDet() {
		return derechoDet;
	}

	public void setDerechoDet(Double derechoDet) {
		this.derechoDet = derechoDet;
		this.derechoDetView = StringUtil.formatDouble(derechoDet);
	}

	public Integer getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(Integer unidadMedida) {
		this.unidadMedida = unidadMedida;
		this.unidadMedidaView = UnidadesMedidasAfip.getById(unidadMedida).getValue();
	}

	public Integer getTipoUnidad() {
		return tipoUnidad;
	}

	public void setTipoUnidad(Integer tipoUnidad) {
		this.tipoUnidad = tipoUnidad;
		this.tipoUnidadView = StringUtil.formatInteger(tipoUnidad);
	}

	public Integer getAlcanceEtur() {
		return alcanceEtur;
	}

	public void setAlcanceEtur(Integer alcanceEtur) {
		this.alcanceEtur = alcanceEtur;
	}
	
	public Long getCodActividad() {
		return codActividad;
	}

	public Double getDifBaseEnero() {
		return difBaseEnero;
	}

	public Double getDifBaseFebrero() {
		return difBaseFebrero;
	}

	public Double getDifBaseMarzo() {
		return difBaseMarzo;
	}

	public Double getDifBaseAbril() {
		return difBaseAbril;
	}

	public Double getDifBaseMayo() {
		return difBaseMayo;
	}

	public Double getDifBaseJunio() {
		return difBaseJunio;
	}

	public Double getDifBaseJulio() {
		return difBaseJulio;
	}

	public Double getDifBaseAgosto() {
		return difBaseAgosto;
	}

	public Double getDifBaseSeptiembre() {
		return difBaseSeptiembre;
	}

	public Double getDifBaseOctubre() {
		return difBaseOctubre;
	}

	public Double getDifBaseNoviembre() {
		return difBaseNoviembre;
	}

	public Double getDifBaseDiciembre() {
		return difBaseDiciembre;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setCodActividad(Long codActividad) {
		this.codActividad = codActividad;
		this.codActividadView = StringUtil.formatLong(codActividad);
	}

	public void setDifBaseEnero(Double difBaseEnero) {
		this.difBaseEnero = difBaseEnero;
		this.difBaseEneroView = StringUtil.formatDouble(difBaseEnero);
	}

	public void setDifBaseFebrero(Double difBaseFebrero) {
		this.difBaseFebrero = difBaseFebrero;
		this.difBaseFebreroView = StringUtil.formatDouble(difBaseFebrero);
	}

	public void setDifBaseMarzo(Double difBaseMarzo) {
		this.difBaseMarzo = difBaseMarzo;
		this.difBaseMarzoView = StringUtil.formatDouble(difBaseMarzo);
	}

	public void setDifBaseAbril(Double difBaseAbril) {
		this.difBaseAbril = difBaseAbril;
		this.difBaseAbrilView = StringUtil.formatDouble(difBaseAbril);
	}

	public void setDifBaseMayo(Double difBaseMayo) {
		this.difBaseMayo = difBaseMayo;
		this.difBaseMayoView = StringUtil.formatDouble(difBaseMayo);
	}

	public void setDifBaseJunio(Double difBaseJunio) {
		this.difBaseJunio = difBaseJunio;
		this.difBaseJunioView = StringUtil.formatDouble(difBaseJunio);
	}

	public void setDifBaseJulio(Double difBaseJulio) {
		this.difBaseJulio = difBaseJulio;
		this.difBaseJulioView = StringUtil.formatDouble(difBaseJulio);
	}

	public void setDifBaseAgosto(Double difBaseAgosto) {
		this.difBaseAgosto = difBaseAgosto;
		this.difBaseAgostoView = StringUtil.formatDouble(difBaseAgosto);
	}

	public void setDifBaseSeptiembre(Double difBaseSeptiembre) {
		this.difBaseSeptiembre = difBaseSeptiembre;
		this.difBaseSeptiembreView = StringUtil.formatDouble(difBaseSeptiembre);
	}

	public void setDifBaseOctubre(Double difBaseOctubre) {
		this.difBaseOctubre = difBaseOctubre;
		this.difBaseOctubreView = StringUtil.formatDouble(difBaseOctubre);
	}

	public void setDifBaseNoviembre(Double difBaseNoviembre) {
		this.difBaseNoviembre = difBaseNoviembre;
		this.difBaseNoviembreView = StringUtil.formatDouble(difBaseNoviembre);
	}

	public void setDifBaseDiciembre(Double difBaseDiciembre) {
		this.difBaseDiciembre = difBaseDiciembre;
		this.difBaseDiciembreView = StringUtil.formatDouble(difBaseDiciembre);
	}

	public void setLocal(LocalVO local) {
		this.local = local;
	}

	public LocalVO getLocal() {
		return local;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getBaseImpExentaView() {
		return baseImpExentaView;
	}

	public String getBaseImponibleView() {
		return baseImponibleView;
	}

	public String getAjuCamCoeView() {
		return ajuCamCoeView;
	}

	public String getBaseImpAjustadaView() {
		return baseImpAjustadaView;
	}

	public String getAliCuotaView() {
		return aliCuotaView;
	}

	public String getDerechoCalculadoView() {
		return derechoCalculadoView;
	}

	public String getCantidadView() {
		return cantidadView;
	}

	public String getMinimoPorUnidadView() {
		return minimoPorUnidadView;
	}

	public String getMinimoCalculadoView() {
		return minimoCalculadoView;
	}

	public String getDerechoDetView() {
		return derechoDetView;
	}

	public String getUnidadMedidaView() {
		return unidadMedidaView;
	}

	public String getTipoUnidadView() {
		return tipoUnidadView;
	}

	public String getAlcanceEturView() {
		return AlcanceEturAfip.getById(this.alcanceEtur).getValue();
	}

	public String getCodActividadView() {
		return codActividadView;
	}

	public String getDifBaseEneroView() {
		return difBaseEneroView;
	}

	public String getDifBaseFebreroView() {
		return difBaseFebreroView;
	}

	public String getDifBaseMarzoView() {
		return difBaseMarzoView;
	}

	public String getDifBaseAbrilView() {
		return difBaseAbrilView;
	}

	public String getDifBaseMayoView() {
		return difBaseMayoView;
	}

	public String getDifBaseJunioView() {
		return difBaseJunioView;
	}

	public String getDifBaseJulioView() {
		return difBaseJulioView;
	}

	public String getDifBaseAgostoView() {
		return difBaseAgostoView;
	}

	public String getDifBaseSeptiembreView() {
		return difBaseSeptiembreView;
	}

	public String getDifBaseOctubreView() {
		return difBaseOctubreView;
	}

	public String getDifBaseNoviembreView() {
		return difBaseNoviembreView;
	}

	public String getDifBaseDiciembreView() {
		return difBaseDiciembreView;
	}

	// View getters	
	public String getBaseImpOCantidadView(){
		if(this.unidadMedida != null && this.unidadMedida > 0){
			return this.cantidadView;
		}else{
			return this.baseImpAjustadaView;			
		}
	}
	
	public String getAliOMinPorUniView(){
		if(this.unidadMedida != null && this.unidadMedida > 0){
			return this.minimoPorUnidadView;
		}else{
			return this.aliCuotaView;			
		}
	}

	public String getDerechoOMinimoView(){
		if(this.unidadMedida != null && this.unidadMedida > 0){
			return this.minimoCalculadoView;
		}else{
			return this.derechoCalculadoView;			
		}
	}

}
