//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.afi.iface.model.ForDecJurVO;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.iface.model.RecAliVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del TipoDeudaPlan
 * @author tecso
 *
 */
public class DecJurVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "decJurVO";
	public static final Long ID_DJMISMOPERIODO=3L;
	
	private RecursoVO recurso= new RecursoVO();
	
	private CuentaVO cuenta = new CuentaVO();
	
	private TipDecJurRecVO tipDecJurRec = new TipDecJurRecVO();
	
	private OriDecJurVO oriDecJud = new OriDecJurVO();
	
	private Date fechaPresentacion;
	
	private Date fechaNovedad;
	
	private Integer periodo;
	
	private Integer anio;
	
	private Double subtotal;
	
	private Double aliPub;
	
	private Double totalPublicidad;
	
	private Double aliMesYSil;
	
	private Double totMesYSil;
	
	private Double totalDeclarado;
	
	private Double otrosPagos;
	
	private Long idDeuda;
	
	private Long idDeudaVueltaAtras;
	
	private Double minRec;
	
	private String desPeriodo="";
	
	private String observaciones="";
	
	private Integer codRectificativa;
	
	private String codRectificativaView = "";
	
	private ForDecJurVO forDecJur = new ForDecJurVO();
	
	private List<DecJurDetVO> listDecJurDet = new ArrayList<DecJurDetVO>();
	
	private List<DecJurPagVO> listDecJurPag= new ArrayList<DecJurPagVO>();
	
	private boolean esDrei=false;
	
	private boolean esEtur=false;
	
	private Double valRefMin;
	
	private String aliPubView="";
	
	private String aliMesYSilView="";
	
	private boolean declaraAdicionales=false;
	
	private boolean declaraPorCantidad=false;
	
	private boolean editaAdicionales=false;
	
	private boolean mostrarTotales=false;
	
	private boolean declaraBaseImp=false;
	
	private boolean declaraOtrosPagos=false;

	private Boolean vueltaAtrasBussEnabled  = false;
	private Boolean mostrarDatosAfip  = false;
	
	// Constructores
	public DecJurVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public DecJurVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesPeriodo(desc);
	}
	
	
	// Getters y Setters

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public TipDecJurRecVO getTipDecJurRec() {
		return tipDecJurRec;
	}

	public void setTipDecJurRecVO(TipDecJurRecVO tipDecJurRec) {
		this.tipDecJurRec = tipDecJurRec;
	}

	public OriDecJurVO getOriDecJud() {
		return oriDecJud;
	}

	public void setOriDecJud(OriDecJurVO oriDecJud) {
		this.oriDecJud = oriDecJud;
	}

	public Date getFechaPresentacion() {
		return fechaPresentacion;
	}

	public void setFechaPresentacion(Date fechaPresentacion) {
		this.fechaPresentacion = fechaPresentacion;
	}

	public Date getFechaNovedad() {
		return fechaNovedad;
	}

	public void setFechaNovedad(Date fechaNovedad) {
		this.fechaNovedad = fechaNovedad;
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

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getTotalPublicidad() {
		return totalPublicidad;
	}

	public void setTotalPublicidad(Double totalPublicidad) {
		this.totalPublicidad = totalPublicidad;
	}

	public Double getTotMesYSil() {
		return totMesYSil;
	}

	public void setTotMesYSil(Double totMesYSil) {
		this.totMesYSil = totMesYSil;
	}

	public Double getTotalDeclarado() {
		return totalDeclarado;
	}

	public void setTotalDeclarado(Double totalDeclarado) {
		this.totalDeclarado = totalDeclarado;
	}

	public Double getOtrosPagos() {
		return otrosPagos;
	}

	public void setOtrosPagos(Double otrosPagos) {
		this.otrosPagos = otrosPagos;
	}

	public Long getIdDeuda() {
		return idDeuda;
	}

	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}

	public void setIdDeudaVueltaAtras(Long idDeudaVueltaAtras) {
		this.idDeudaVueltaAtras = idDeudaVueltaAtras;
	}

	public Long getIdDeudaVueltaAtras() {
		return idDeudaVueltaAtras;
	}

	public String getDesPeriodo() {
		return this.periodo + "/"+ this.anio;
	}

	public void setDesPeriodo(String desPeriodo) {
		this.desPeriodo = desPeriodo;
	}
	
	public List<DecJurDetVO> getListDecJurDet() {
		return listDecJurDet;
	}

	public void setListDecJurDet(List<DecJurDetVO> listDecJurDet) {
		this.listDecJurDet = listDecJurDet;
	}

	public void setTipDecJurRec(TipDecJurRecVO tipDecJurRec) {
		this.tipDecJurRec = tipDecJurRec;
	}
	
	public boolean isEsDrei() {
		return esDrei;
	}

	public void setEsDrei(boolean esDrei) {
		this.esDrei = esDrei;
	}
	
	public boolean isEsEtur() {
		return esEtur;
	}

	public void setEsEtur(boolean esEtur) {
		this.esEtur = esEtur;
	}
	
	public List<DecJurPagVO> getListDecJurPag() {
		return listDecJurPag;
	}

	public void setListDecJurPag(List<DecJurPagVO> listDecJurPag) {
		this.listDecJurPag = listDecJurPag;
	}

	public Double getValRefMin() {
		return valRefMin;
	}

	public void setValRefMin(Double valRefMin) {
		this.valRefMin = valRefMin;
	}

	public Double getMinRec() {
		return minRec;
	}

	public void setMinRec(Double minRec) {
		this.minRec = minRec;
	}

	public Double getAliPub() {
		return aliPub;
	}

	public void setAliPub(Double aliPub) {
		this.aliPub = aliPub;
		this.aliPubView = (aliPub!=null)?NumberUtil.round(aliPub, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}

	public Double getAliMesYSil() {
		return aliMesYSil;
	}

	public void setAliMesYSil(Double aliMesYSil) {
		this.aliMesYSil = aliMesYSil;
		this.aliMesYSilView = (aliMesYSil!=null)?NumberUtil.round(aliMesYSil, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}

	public ForDecJurVO getForDecJur() {
		return forDecJur;
	}

	public void setForDecJur(ForDecJurVO forDecJur) {
		this.forDecJur = forDecJur;
	}

	// View getters
	public String getFechaPresentacionView(){
		return (this.fechaPresentacion!=null)?DateUtil.formatDate(this.fechaPresentacion, DateUtil.ddSMMSYY_MASK):"";
	}
	
	public String getFechaNovedadView(){
		return (this.fechaNovedad!=null)?DateUtil.formatDate(this.fechaNovedad, DateUtil.ddSMMSYY_MASK):"";
	}
	
	public String getTotalDeclaradoView(){
		return (this.totalDeclarado!=null)?NumberUtil.round(this.totalDeclarado, SiatParam.DEC_IMPORTE_VIEW).toString():"";
	}
	
	public String getDesEstado(){
		return this.getEstado().getValue();	 
	}
	
	public String getValRefMinIntView(){
		return (this.valRefMin!=null)?NumberUtil.round(this.valRefMin, 0).toString():"";
	}
	
	public String getSubtotalView(){
		return (this.subtotal!=null)?NumberUtil.round(this.subtotal, SiatParam.DEC_IMPORTE_VIEW).toString():"0.00";
	}

	public String getAliPubView() {
		Double alicuotaPub=0D;
		if (this.aliPub!=null)alicuotaPub=this.aliPub;
		RecAliVO recAli = new RecAliVO(alicuotaPub);
		return recAli.getAlicuotaView();
	}

	public void setAliPubView(String aliPubView) {
		this.aliPubView = aliPubView;
	}

	public String getAliMesYSilView() {
		Double alicuotaMes=0D;
		if (this.aliMesYSil!=null)alicuotaMes=this.aliMesYSil;
		RecAliVO recAli = new RecAliVO(alicuotaMes);
		return recAli.getAlicuotaView();
		
	}

	public void setAliMesYSilView(String aliMesYSilView) {
		this.aliMesYSilView = aliMesYSilView;
	}
	
	public String getTotMesYSilView(){
		return (this.totMesYSil!=null)?NumberUtil.round(this.totMesYSil, SiatParam.DEC_IMPORTE_VIEW).toString():"0.00";
	}
	
	public String getTotalPublicidadView(){
		return (this.totalPublicidad!=null)?NumberUtil.round(this.totalPublicidad, SiatParam.DEC_IMPORTE_VIEW).toString():"0.00";
	}
	
	public String getOtrosPagosView(){
		return (this.otrosPagos!=null)?NumberUtil.round(this.otrosPagos, SiatParam.DEC_IMPORTE_VIEW).toString():"0.00";
	}
	
	public String getTotalDJView(){
		Double totalDec =0D;
		Double otrosPag=0D;
		if(this.totalDeclarado!=null)totalDec=this.totalDeclarado;
		if(this.otrosPagos!=null)otrosPag=this.otrosPagos;
		
		return NumberUtil.round(totalDec-otrosPag , SiatParam.DEC_IMPORTE_VIEW).toString();
	}
	
	
	
	
	public String getMinRecView(){
		return (this.minRec!=null)?NumberUtil.round(this.minRec, SiatParam.DEC_IMPORTE_VIEW).toString():"0.00";
	}

	public boolean isDeclaraAdicionales() {
		return declaraAdicionales;
	}

	public void setDeclaraAdicionales(boolean declaraAdicionales) {
		this.declaraAdicionales = declaraAdicionales;
	}

	public boolean isDeclaraPorCantidad() {
		return declaraPorCantidad;
	}

	public void setDeclaraPorCantidad(boolean declaraPorCantidad) {
		this.declaraPorCantidad = declaraPorCantidad;
	}

	public boolean isEditaAdicionales() {
		return editaAdicionales;
	}

	public void setEditaAdicionales(boolean editaAdicionales) {
		this.editaAdicionales = editaAdicionales;
	}

	public boolean isMostrarTotales() {
		return mostrarTotales;
	}

	public void setMostrarTotales(boolean mostrarTotales) {
		this.mostrarTotales = mostrarTotales;
	}
	
	public String getSubtotalDeclaradoView(){
		Double subtot=0D;
		for (DecJurDetVO decJurDet: getListDecJurDet()){
			subtot += decJurDet.getTotalConcepto();
		}
		return NumberUtil.roundNull(subtot, 2).toString();
	}

	public boolean isDeclaraBaseImp() {
		return declaraBaseImp;
	}

	public void setDeclaraBaseImp(boolean declaraBaseImp) {
		this.declaraBaseImp = declaraBaseImp;
	}

	public boolean isDeclaraOtrosPagos() {
		return declaraOtrosPagos;
	}

	public void setDeclaraOtrosPagos(boolean declaraOtrosPagos) {
		this.declaraOtrosPagos = declaraOtrosPagos;
	}
	
	public Integer getCodRectificativa() {
		return codRectificativa;
	}

	public void setCodRectificativa(Integer codRectificativa) {
		this.codRectificativa = codRectificativa;
		this.codRectificativaView = StringUtil.formatInteger(codRectificativa);
	}

	public String getCodRectificativaView() {
		return codRectificativaView;
	}

	public void setCodRectificativaView(String codRectificativaView) {
		this.codRectificativaView = codRectificativaView;
	}

	/**
	 * Devuelve true si posee al menos un pago para el mismo periodo
	 * 
	 * @return
	 */
	public boolean poseePagosMismoPeriodo(){
		
		for (DecJurPagVO decJurPagVO:getListDecJurPag()){
			if (decJurPagVO.getTipPagDecJur().getId().longValue() == ID_DJMISMOPERIODO){
				return true;
			}
		}
		return false;	
	}
	
	//	 Buss flags getters y setters
	public Boolean getVueltaAtrasBussEnabled() {
		return vueltaAtrasBussEnabled;
	}
	public void setVueltaAtrasBussEnabled(Boolean vueltaAtrasBussEnabled) {
		this.vueltaAtrasBussEnabled = vueltaAtrasBussEnabled;
	}
	public String getVueltaAtrasEnabled() {
		return this.getVueltaAtrasBussEnabled() ? ENABLED : DISABLED;
	}

	public Boolean getMostrarDatosAfip() {
		return mostrarDatosAfip;
	}

	public void setMostrarDatosAfip(Boolean mostrarDatosAfip) {
		this.mostrarDatosAfip = mostrarDatosAfip;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}
