//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.iface.model.RecConADecVO;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.PORCENTAJE;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del AliComFueCol
 * @author tecso
 *
 */
public class AliComFueColVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "aliComFueColVO";
	
	private Log log = LogFactory.getLog(AliComFueColVO.class);
	
	private CompFuenteColVO compFuenteCol;

	private DetAjuVO detAju = new DetAjuVO();
	
	private Double valorAlicuota;
	
    private Integer periodoDesde;

    private Integer anioDesde;

    private Integer periodoHasta;

    private Integer anioHasta;
    
    private Double cantidad;
    
    private Double valorUnitario;
    
    private boolean esOrdConCueEtur=false;
    
    private RecConADecVO tipoUnidad=new RecConADecVO();
    
    private Integer radio;
    
	
	// Buss Flags
	
	
	// View Constants
	
    private String valorAlicuotaView = "";
	private String anioDesdeView = "";
	private String anioHastaView = "";
	private String periodoDesdeView = "";
	private String periodoHastaView = "";
	private String cantidadView="";
	private String valorUnitarioView="";


	// Constructores
	public AliComFueColVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public AliComFueColVO(int id, String desc) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters
    public DetAjuVO getDetAju() {
		return detAju;
	}

	public void setDetAju(DetAjuVO detAju) {
		this.detAju = detAju;
	}
	
	public CompFuenteColVO getCompFuenteCol() {
		return compFuenteCol;
	}

	public void setCompFuenteCol(CompFuenteColVO compFuenteCol) {
		this.compFuenteCol = compFuenteCol;
	}

	public Integer getPeriodoDesde() {
		return periodoDesde;
	}

	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
		this.periodoDesdeView = StringUtil.formatInteger(periodoDesde);
	}

	public Integer getAnioDesde() {
		return anioDesde;
	}

	public void setAnioDesde(Integer anioDesde) {
		this.anioDesde = anioDesde;
		this.anioDesdeView = StringUtil.formatInteger(anioDesde);
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
		this.periodoHastaView = StringUtil.formatInteger(periodoHasta);
	}

	public Integer getAnioHasta() {
		return anioHasta;
	}

	public void setAnioHasta(Integer anioHasta) {
		this.anioHasta = anioHasta;
		this.anioHastaView = StringUtil.formatInteger(anioHasta);
	}

	public Double getValorAlicuota() {
		return valorAlicuota;
	}
	
	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
		this.cantidadView=(cantidad!=null)?NumberUtil.truncate(cantidad, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}

	public Double getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
		this.valorUnitarioView=(valorUnitario!=null)?NumberUtil.truncate(valorUnitario, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}

	public RecConADecVO getTipoUnidad() {
		return tipoUnidad;
	}

	public void setTipoUnidad(RecConADecVO tipoUnidad) {
		this.tipoUnidad = tipoUnidad;
	}

	@PORCENTAJE
	public void setValorAlicuota(Double valor) {
		this.valorAlicuota = valor;
		this.valorAlicuotaView = StringUtil.parsePointToComa(StringUtil.getValorPorcent(valor));
	}
	

	public boolean getEsOrdConCueEtur() {
		return esOrdConCueEtur;
	}

	public void setEsOrdConCueEtur(boolean esOrdConCueEtur) {
		this.esOrdConCueEtur = esOrdConCueEtur;
	}
	
	public Integer getRadio() {
		return radio;
	}

	public void setRadio(Integer radio) {
		this.radio = radio;
	}
	
	
	
	
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	

	// View getters
	public void setAnioDesdeView(String anioDesdeView) {
		this.anioDesdeView = anioDesdeView;
	}
	public String getAnioDesdeView() {
		return anioDesdeView;
	}

	public void setAnioHastaView(String anioHastaView) {
		this.anioHastaView = anioHastaView;
	}
	public String getAnioHastaView() {
		return anioHastaView;
	}

	public void setPeriodoDesdeView(String periodoDesdeView) {
		this.periodoDesdeView = periodoDesdeView;
	}
	public String getPeriodoDesdeView() {
		return periodoDesdeView;
	}

	public void setPeriodoHastaView(String periodoHastaView) {
		this.periodoHastaView = periodoHastaView;
	}
	public String getPeriodoHastaView() {
		return periodoHastaView;
	}

	public String getValorAlicuotaView() {
		return valorAlicuotaView;
	}
	
	public String getMultiploView() {
		String detalle="";
		if(valorAlicuota!=null)
			detalle +=valorAlicuotaView;
		if(valorUnitario!=null && cantidad!=null){
			if(valorAlicuota!=null)
				detalle+="<br>";
			detalle+=this.cantidadView+" un. x"+this.valorUnitarioView;
		}
		/*log.debug("ESETUR: "+String.valueOf(esOrdConCueEtur));
		if(esOrdConCueEtur){
			detalle="Actividad: "+tipoUnidad.getCodYDescripcion();
			detalle+="<br>";
			detalle+="Radio: "+radio.toString();
		}*/
		return detalle;
	}

	public void setValorAlicuotaView(String valorAlicuotaView) {
		this.valorAlicuotaView = valorAlicuotaView;
	}

	public String getPeriodoAnioDesdeView(){
		return getPeriodoDesdeView()+"/"+getAnioDesdeView();
	}
	
	public String getPeriodoAnioHastaView(){
		return getPeriodoHastaView()+"/"+getAnioHastaView();
	}

	public String getCantidadView() {
		return cantidadView;
	}

	public void setCantidadView(String cantidadView) {
		this.cantidadView = cantidadView;
	}

	public String getValorUnitarioView() {
		return valorUnitarioView;
	}

	public void setValorUnitarioView(String valorUnitarioView) {
		this.valorUnitarioView = valorUnitarioView;
	}
	
	public String getRadioView(){
		return (this.radio!=null)?this.radio.toString():"";
	}
}
