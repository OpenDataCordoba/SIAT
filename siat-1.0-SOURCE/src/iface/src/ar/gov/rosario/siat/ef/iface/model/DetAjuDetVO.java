//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del DetAjuDet
 * @author tecso
 *
 */
public class DetAjuDetVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "detAjuDetVO";
	
	private DetAjuVO detAju;

	private PeriodoOrdenVO periodoOrden;

	private PlaFueDatComVO plaFueDatCom;

    private Double tributo;

    private Integer cantPersonal;

    private Double minimo;

    private Double triDet;

    private Double publicidad;

    private Double mesasYSillas;

    private Double pago;
    
    private Double pagPosFecIni=0.0;

    private Double retencion;

    private Double ajuste;
    
    private Double totalTributo;
    
    private Double compensacion;
    
    private Double porMulta;
    
    private Double ajusteActualizado;
    
    private List<AliComFueColVO> listAliComFueCol = new ArrayList<AliComFueColVO>();
	// Buss Flags
	
	
	// View Constants
	
	
	private String ajusteView = "";
	private String mesasYSillasView = "";
	private String minimoView = "";
	private String pagoView = "";
	private String pagPosFecIniView = "";
	private String publicidadView = "";
	private String retencionView = "";
	private String triDetView = "";
	private String tributoView = "";
	private String cantPersonalView="";
	private String totalTributoView = "";
	private String porMultaView;
	
	// Constructores
	public DetAjuDetVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public DetAjuDetVO(int id, String desc) {
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

	public PeriodoOrdenVO getPeriodoOrden() {
		return periodoOrden;
	}

	public void setPeriodoOrden(PeriodoOrdenVO periodoOrden) {
		this.periodoOrden = periodoOrden;
	}

	public PlaFueDatComVO getPlaFueDatCom() {
		return plaFueDatCom;
	}

	public void setPlaFueDatCom(PlaFueDatComVO plaFueDatCom) {
		this.plaFueDatCom = plaFueDatCom;
	}

	public Double getTributo() {
		return tributo;
	}

	public void setTributo(Double tributo) {
		this.tributo = tributo;
		this.tributoView = StringUtil.formatDouble(tributo);
	}

	public Integer getCantPersonal() {
		return cantPersonal;
	}

	public void setCantPersonal(Integer cantPersonal) {
		this.cantPersonal = cantPersonal;
		this.cantPersonalView= cantPersonal.toString();
	}

	public Double getMinimo() {
		return minimo;
	}

	public void setMinimo(Double minimo) {
		this.minimo = minimo;
		this.minimoView = StringUtil.formatDouble(minimo);
	}

	public Double getTriDet() {
		return triDet;
	}

	public void setTriDet(Double triDet) {
		this.triDet = triDet;
		this.triDetView = StringUtil.formatDouble(triDet);
	}

	public Double getPublicidad() {
		return publicidad;
	}

	public void setPublicidad(Double publicidad) {
		this.publicidad = publicidad;
		this.publicidadView = StringUtil.formatDouble(publicidad);
	}

	public Double getMesasYSillas() {
		return mesasYSillas;
	}

	public void setMesasYSillas(Double mesasYSillas) {
		this.mesasYSillas = mesasYSillas;
		this.mesasYSillasView = StringUtil.formatDouble(mesasYSillas);
	}

	public Double getPago() {
		return pago;
	}

	public void setPago(Double pago) {
		this.pago = pago;
		this.pagoView = StringUtil.formatDouble(pago);
	}

	public Double getRetencion() {
		return retencion;
	}

	public void setRetencion(Double retencion) {
		this.retencion = retencion;
		this.retencionView = StringUtil.formatDouble(retencion);
	}

	public Double getAjuste() {
		return ajuste;
	}

	public void setAjuste(Double ajuste) {
		this.ajuste = ajuste;
		this.ajusteView = StringUtil.redondearDecimales(ajuste,1,2);
	}

	public Double getTotalTributo() {
		return totalTributo;
	}

	public void setTotalTributo(Double totalTributo) {
		this.totalTributo = totalTributo;
		this.totalTributoView = StringUtil.formatDouble(totalTributo);
	}

	public Double getCompensacion() {
		return compensacion;
	}

	public void setCompensacion(Double compensacion) {
		this.compensacion = compensacion;
	}

	public Double getPorMulta() {
		return porMulta;
	}

	public void setPorMulta(Double porMulta) {
		this.porMulta = porMulta;
		this.porMultaView = StringUtil.formatDouble(porMulta);
	}

	public List<AliComFueColVO> getListAliComFueCol() {
		return listAliComFueCol;
	}
	
	public void setListAliComFueCol(List<AliComFueColVO> listAliComFueCol) {
		this.listAliComFueCol = listAliComFueCol;
	}
	
	public Double getAjusteActualizado() {
		return ajusteActualizado;
	}

	public void setAjusteActualizado(Double ajusteActualizado) {
		this.ajusteActualizado = ajusteActualizado;
	}

	public Double getPagPosFecIni() {
		return pagPosFecIni;
	}

	public void setPagPosFecIni(Double pagPosFecIni) {
		this.pagPosFecIni = pagPosFecIni;
		this.pagPosFecIniView = StringUtil.formatDouble(pagPosFecIni);
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	

	// View getters
	public void setAjusteView(String ajusteView) {
		this.ajusteView = ajusteView;
	}
	public String getAjusteView() {
		return ajusteView;
	}

	public void setMesasYSillasView(String mesasYSillasView) {
		this.mesasYSillasView = mesasYSillasView;
	}
	public String getMesasYSillasView() {
		return mesasYSillasView;
	}

	public void setMinimoView(String minimoView) {
		this.minimoView = minimoView;
	}
	public String getMinimoView() {
		return minimoView;
	}

	public void setPagoView(String pagoView) {
		this.pagoView = pagoView;
	}
	public String getPagoView() {
		return pagoView;
	}

	public void setPublicidadView(String publicidadView) {
		this.publicidadView = publicidadView;
	}
	public String getPublicidadView() {
		return publicidadView;
	}

	public void setRetencionView(String retencionView) {
		this.retencionView = retencionView;
	}
	public String getRetencionView() {
		return retencionView;
	}

	public void setTriDetView(String triDetView) {
		this.triDetView = triDetView;
	}
	public String getTriDetView() {
		return triDetView;
	}

	public void setTributoView(String tributoView) {
		this.tributoView = tributoView;
	}
	public String getTributoView() {
		return tributoView;
	}

	public void setCantPersonalView(String cantPersonalView) {
		this.cantPersonalView = cantPersonalView;
	}
	public String getCantPersonalView() {
		return cantPersonalView;
	}

	public String getTotalTributoView() {
		return totalTributoView;
	}

	public void setTotalTributoView(String totalTributoView) {
		this.totalTributoView = totalTributoView;
	}

	public String getValorPublicidad(){
		if(triDet!=null && publicidad!=null){
			return StringUtil.redondearDecimales(triDet*publicidad, 1, 2);
		}
		return "";
	}
	
	public String getValorMesasYSillas(){
		if(triDet!=null && mesasYSillas!=null){
			return StringUtil.redondearDecimales(triDet*mesasYSillas,1,2);
		}
		return "";
	}

	public String getPorMultaView() {
		return (porMulta!=null)?NumberUtil.round(porMulta*100, SiatParam.DEC_IMPORTE_VIEW).toString()+"%":"";
	}
	public void setPorMultaView(String porMultaView) {
		this.porMultaView = porMultaView;
	}

	public String getPagPosFecIniView() {
		return pagPosFecIniView;
	}

	public void setPagPosFecIniView(String pagPosFecIniView) {
		this.pagPosFecIniView = pagPosFecIniView;
	}
	
	public String getAjusteActualizadoView(){
		return (this.ajusteActualizado!=null)?NumberUtil.round(this.ajusteActualizado, 2).toString():"";
	}
	
}
