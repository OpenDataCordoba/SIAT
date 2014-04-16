//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del Local
 * @author tecso
 *
 */
public class LocalVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "localVO";
	
	private ForDecJurVO fordecjur = new ForDecJurVO();

	private Long		idCuenta;
	
	private Integer		centralizaIngresos;	

	private Integer 	contribEtur;	

	private Integer 	radio;		

	private Integer 	cantPersonal;
	
	private Integer 	paga;

	private Integer 	pagaMinimo;	
	
	private Double  	derDetTot;	

	private Double 		minimoGeneral;	

	private Double  	derecho;	

	private Double  	alicuotaPub;	

	private Double  	publicidad;	

	private Double  	alicuotaMesYSil;	

	private Double   	mesasYSillas;	

	private Double   	subTotal1;	

	private Double   	otrosPagos;	

	private Double   	computado;	

	private Double		resto;	

	private Double   	derechoTotal;	
	
	private Date 		fecIniAct;

	private Date  		fecCesAct;
	
	private String 		nombreFantasia = "";

	private String  	numeroCuenta   = "";
	
	private String 		fecIniActView  = "";

	private String  	fecCesActView  = "";
	
	private List<OtrosPagosVO> 	    listOtrosPagos	 		= new ArrayList<OtrosPagosVO>();

	private List<DecActLocVO> 	    listDecActLoc			= new ArrayList<DecActLocVO>();
	
	private List<HabLocVO> 			listHabLoc				= new ArrayList<HabLocVO>();	

	private List<ActLocVO> 			listActLoc				= new ArrayList<ActLocVO>();
	
	private List<DatosPagoCtaVO>  	listDatosPagoCta		= new ArrayList<DatosPagoCtaVO>();
	
	private DatosDomicilioVO  		datosDomicilio			= new DatosDomicilioVO();
		
	
	
	// Constructores
	public LocalVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public LocalVO(int id) {
		super();
	}

	// Getters y Setters
	public Long getIdCuenta() {
		return idCuenta;
	}

	public Integer getCentralizaIngresos() {
		return centralizaIngresos;
	}

	public Integer getContribEtur() {
		return contribEtur;
	}

	public Integer getRadio() {
		return radio;
	}

	public Integer getCantPersonal() {
		return cantPersonal;
	}

	public Date getFecIniAct() {
		return fecIniAct;
	}

	public Date getFecCesAct() {
		return fecCesAct;
	}

	public String getNombreFantasia() {
		return nombreFantasia;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	
	public List<OtrosPagosVO> getListOtrosPagos() {
		return listOtrosPagos;
	}

	public List<DecActLocVO> getListDecActLoc() {
		return listDecActLoc;
	}

	public List<HabLocVO> getListHabLoc() {
		return listHabLoc;
	}

	public void setListHabLoc(List<HabLocVO> listHabLoc) {
		this.listHabLoc = listHabLoc;
	}

	public List<ActLocVO> getListActLoc() {
		return listActLoc;
	}

	public void setListActLoc(List<ActLocVO> listActLoc) {
		this.listActLoc = listActLoc;
	}


	public Integer getPaga() {
		return paga;
	}

	public void setPaga(Integer paga) {
		this.paga = paga;
	}

	public Integer getPagaMinimo() {
		return pagaMinimo;
	}

	public void setPagaMinimo(Integer pagaMinimo) {
		this.pagaMinimo = pagaMinimo;
	}

	public Double getDerDetTot() {
		return derDetTot;
	}

	public void setDerDetTot(Double derDetTot) {
		this.derDetTot = derDetTot;
	}

	public Double getMinimoGeneral() {
		return minimoGeneral;
	}

	public void setMinimoGeneral(Double minimoGeneral) {
		this.minimoGeneral = minimoGeneral;
	}

	public Double getDerecho() {
		return derecho;
	}

	public void setDerecho(Double derecho) {
		this.derecho = derecho;
	}

	public Double getAlicuotaPub() {
		return alicuotaPub;
	}

	public void setAlicuotaPub(Double alicuotaPub) {
		this.alicuotaPub = alicuotaPub;
	}

	public Double getPublicidad() {
		return publicidad;
	}

	public void setPublicidad(Double publicidad) {
		this.publicidad = publicidad;
	}

	public Double getAlicuotaMesYSil() {
		return alicuotaMesYSil;
	}

	public void setAlicuotaMesYSil(Double alicuotaMesYSil) {
		this.alicuotaMesYSil = alicuotaMesYSil;
	}

	public Double getMesasYSillas() {
		return mesasYSillas;
	}

	public void setMesasYSillas(Double mesasYSillas) {
		this.mesasYSillas = mesasYSillas;
	}

	public Double getSubTotal1() {
		return subTotal1;
	}

	public void setSubTotal1(Double subTotal1) {
		this.subTotal1 = subTotal1;
	}

	public Double getOtrosPagos() {
		return otrosPagos;
	}

	public void setOtrosPagos(Double otrosPagos) {
		this.otrosPagos = otrosPagos;
	}

	public Double getComputado() {
		return computado;
	}

	public void setComputado(Double computado) {
		this.computado = computado;
	}

	public Double getResto() {
		return resto;
	}

	public void setResto(Double resto) {
		this.resto = resto;
	}

	public Double getDerechoTotal() {
		return derechoTotal;
	}

	public void setDerechoTotal(Double derechoTotal) {
		this.derechoTotal = derechoTotal;
	}

	public void setFecIniActView(String fecIniActView) {
		this.fecIniActView = fecIniActView;
	}

	public void setFecCesActView(String fecCesActView) {
		this.fecCesActView = fecCesActView;
	}

	public void setListDecActLoc(List<DecActLocVO> listDecActLoc) {
		this.listDecActLoc = listDecActLoc;
	}

	public void setIdCuenta(Long idCuenta) {
		this.idCuenta = idCuenta;
	}

	public void setCentralizaIngresos(Integer centralizaIngresos) {
		this.centralizaIngresos = centralizaIngresos;
	}

	public void setContribEtur(Integer contribEtur) {
		this.contribEtur = contribEtur;
	}

	public void setRadio(Integer radio) {
		this.radio = radio;
	}

	public void setCantPersonal(Integer cantPersonal) {
		this.cantPersonal = cantPersonal;
	}

	public void setFecIniAct(Date fecIniAct) {
		this.fecIniAct = fecIniAct;
		this.fecIniActView = DateUtil.formatDate(fecIniAct, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setFecCesAct(Date fecCesAct) {
		this.fecCesAct = fecCesAct;
		this.fecCesActView = DateUtil.formatDate(fecCesAct, DateUtil.ddSMMSYYYY_MASK);
	}

	public void setNombreFantasia(String nombreFantasia) {
		this.nombreFantasia = nombreFantasia;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public void setListOtrosPagos(List<OtrosPagosVO> listOtrosPagos) {
		this.listOtrosPagos = listOtrosPagos;
	}

	public void setListDeclaracionActLocal(List<DecActLocVO> listDecActLoc) {
		this.listDecActLoc = listDecActLoc;
	}


	
	
	public void setListDatosPagoCta(List<DatosPagoCtaVO> listDatosPagoCta) {
		this.listDatosPagoCta = listDatosPagoCta;
	}

	public List<DatosPagoCtaVO> getListDatosPagoCta() {
		return listDatosPagoCta;
	}

	public void setDatosDomicilio(DatosDomicilioVO datosDomicilio) {
		this.datosDomicilio = datosDomicilio;
	}

	public DatosDomicilioVO getDatosDomicilio() {
		return datosDomicilio;
	}

	public void setFordecjur(ForDecJurVO fordecjur) {
		this.fordecjur = fordecjur;
	}

	public ForDecJurVO getFordecjur() {
		return fordecjur;
	}

	// View getters 	
	public String getFecIniActView() {
		return fecIniActView;
	}

	public String getFecCesActView() {
		return fecCesActView;
	}
	
	public String getIdCuentaView() {
		return (this.idCuenta!=null)?idCuenta.toString():"";			
	}
		
	public String getContribEturView() {	
		return SiNo.getById(contribEtur).getValue();			
	}
	
	public String getRadioView() {
		return (this.radio!=null)?radio.toString():"";	
	}
	
	public String getCantPersonalView() {
		return (this.cantPersonal!=null)?cantPersonal.toString():"";			
	}
	
	public String getDerDetTotView() {
		return (this.derDetTot!=null)?derDetTot.toString():"";		
	}

	public String getMinimoGeneralView() {
		return (this.minimoGeneral!=null)?minimoGeneral.toString():"";				
	}

	public String getDerechoView() {
		return (this.derecho!=null)?derecho.toString():"";
	}

	public String getAlicuotaPubView() {
		return (this.alicuotaPub!=null)?alicuotaPub.toString():"";
	}

	public String getPublicidadView() {
		return (this.publicidad!=null)?publicidad.toString():"";
	}

	public String getAlicuotaMesYSilView() {
		return (this.alicuotaMesYSil!=null)?alicuotaMesYSil.toString():"";
	}

	public String getMesasYSillasView() {
		return (this.mesasYSillas!=null)?mesasYSillas.toString():"";
	}

	public String getSubTotal1View() {
		return (this.subTotal1!=null)?subTotal1.toString():"";
	}

	public String getOtrosPagosView() {
		return (this.otrosPagos!=null)?otrosPagos.toString():"";		
	}

	public String getComputadoView() {
		return (this.computado!=null)?computado.toString():"";				
	}

	public String getRestoView() {
		return (this.resto!=null)?resto.toString():"";				
	}

	public String getDerechoTotalView() {
		return (this.derechoTotal!=null)?derechoTotal.toString():"";		
	}

	public String getPagaView() {
		return SiNo.getById(paga).getValue();	
	}

	public String getPagaMinimoView() {
		return SiNo.getById(pagaMinimo).getValue();			
	}

	public String getCentralizaIngresosView() {
		return SiNo.getById(centralizaIngresos).getValue();			
	}

	public String getFecVigActView(){
		return this.fecIniActView+" - "+this.fecCesActView;
	}
}
