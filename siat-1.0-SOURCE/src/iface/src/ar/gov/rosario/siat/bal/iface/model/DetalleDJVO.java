//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.RegistrosDetalleDJAfip;

public class DetalleDJVO extends SiatBussImageModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "detalleDJVO";		

	private TranAfipVO tranAfip = new TranAfipVO();
	
	private Date fechaProceso;
	
	private Integer registro;

	private Integer fila;
		
	private String contenido = "";
		
	private EstDetDJVO estDetDJ = new EstDetDJVO();	

	private Double c01n;	

	private Double c02n;	

	private Double c03n;	

	private Double c04n;	
	
	private Double c05n;	

	private Double c06n;	

	private Double c07n;	

	private Double c08n;	

	private Double c09n;	
	
	private Double c10n;	
	
	private Double c11n;	
	
	private Double c12n;	

	private Double c13n;	
	
	private Double c14n;	

	private Double c15n;	
	
	private Double c16n;	

	private Double c17n;	

	private Double c18n;	

	private Double c19n;	

	private Double c20n;	

	private Double c21n;	

	private Double c22n;	

	private Double c23n;	
	
	private Double c24n;	
	
	private Double c25n;	
	
	private Double c26n;	
	
	private Double c27n;	
	
	private Double c28n;	
	
	private Double c29n;	
	
	private Double c30n;
	
	private String fechaProcesoView = "";
	
	private String dataStrView = "";
	private String contenidoParseadoView = "";
	
	
	// Constructores
	public DetalleDJVO() {
		super();
	}

	// Getters y Setters
	public Integer getRegistro() {
		return registro;
	}

	public void setRegistro(Integer registro) {
		this.registro = registro;
	}

	public Integer getFila() {
		return fila;
	}

	public void setFila(Integer fila) {
		this.fila = fila;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public EstDetDJVO getEstDetDJ() {
		return estDetDJ;
	}

	public void setEstDetDJ(EstDetDJVO estDetDJ) {
		this.estDetDJ = estDetDJ;
	}

	public Double getC01n() {
		return c01n;
	}

	public void setC01n(Double c01n) {
		this.c01n = c01n;
	}

	public Double getC02n() {
		return c02n;
	}

	public void setC02n(Double c02n) {
		this.c02n = c02n;
	}

	public Double getC03n() {
		return c03n;
	}

	public void setC03n(Double c03n) {
		this.c03n = c03n;
	}

	public Double getC04n() {
		return c04n;
	}

	public void setC04n(Double c04n) {
		this.c04n = c04n;
	}

	public Double getC05n() {
		return c05n;
	}

	public void setC05n(Double c05n) {
		this.c05n = c05n;
	}

	public Double getC06n() {
		return c06n;
	}

	public void setC06n(Double c06n) {
		this.c06n = c06n;
	}

	public Double getC07n() {
		return c07n;
	}

	public void setC07n(Double c07n) {
		this.c07n = c07n;
	}

	public Double getC08n() {
		return c08n;
	}

	public void setC08n(Double c08n) {
		this.c08n = c08n;
	}

	public Double getC09n() {
		return c09n;
	}

	public void setC09n(Double c09n) {
		this.c09n = c09n;
	}

	public Double getC10n() {
		return c10n;
	}

	public void setC10n(Double c10n) {
		this.c10n = c10n;
	}

	public Double getC11n() {
		return c11n;
	}

	public void setC11n(Double c11n) {
		this.c11n = c11n;
	}

	public Double getC12n() {
		return c12n;
	}

	public void setC12n(Double c12n) {
		this.c12n = c12n;
	}

	public Double getC13n() {
		return c13n;
	}

	public void setC13n(Double c13n) {
		this.c13n = c13n;
	}

	public Double getC14n() {
		return c14n;
	}

	public void setC14n(Double c14n) {
		this.c14n = c14n;
	}

	public Double getC15n() {
		return c15n;
	}

	public void setC15n(Double c15n) {
		this.c15n = c15n;
	}

	public Double getC16n() {
		return c16n;
	}

	public void setC16n(Double c16n) {
		this.c16n = c16n;
	}

	public Double getC17n() {
		return c17n;
	}

	public void setC17n(Double c17n) {
		this.c17n = c17n;
	}

	public Double getC18n() {
		return c18n;
	}

	public void setC18n(Double c18n) {
		this.c18n = c18n;
	}

	public Double getC19n() {
		return c19n;
	}

	public void setC19n(Double c19n) {
		this.c19n = c19n;
	}

	public Double getC20n() {
		return c20n;
	}

	public void setC20n(Double c20n) {
		this.c20n = c20n;
	}

	public Double getC21n() {
		return c21n;
	}

	public void setC21n(Double c21n) {
		this.c21n = c21n;
	}

	public Double getC22n() {
		return c22n;
	}

	public void setC22n(Double c22n) {
		this.c22n = c22n;
	}

	public Double getC23n() {
		return c23n;
	}

	public void setC23n(Double c23n) {
		this.c23n = c23n;
	}

	public Double getC24n() {
		return c24n;
	}

	public void setC24n(Double c24n) {
		this.c24n = c24n;
	}

	public Double getC25n() {
		return c25n;
	}

	public void setC25n(Double c25n) {
		this.c25n = c25n;
	}

	public Double getC26n() {
		return c26n;
	}

	public void setC26n(Double c26n) {
		this.c26n = c26n;
	}

	public Double getC27n() {
		return c27n;
	}

	public void setC27n(Double c27n) {
		this.c27n = c27n;
	}

	public Double getC28n() {
		return c28n;
	}

	public void setC28n(Double c28n) {
		this.c28n = c28n;
	}

	public Double getC29n() {
		return c29n;
	}

	public void setC29n(Double c29n) {
		this.c29n = c29n;
	}

	public Double getC30n() {
		return c30n;
	}

	public void setC30n(Double c30n) {
		this.c30n = c30n;
	}

	public Date getFechaProceso() {
		return fechaProceso;
	}

	public void setTranAfip(TranAfipVO tranAfip) {
		this.tranAfip = tranAfip;
	}

	public TranAfipVO getTranAfip() {
		return tranAfip;
	}

	public String getFechaProcesoView() {
		return fechaProcesoView;
	}
	
	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;		
		this.fechaProcesoView = DateUtil.formatDate(fechaProceso, DateUtil.ddSMMSYYYY_MASK);
	}
	
	// View getters
	public String getC01nView(){
		return (this.c01n!=null)?c01n.toString():"";
	}
	
	public String getC02nView(){
		return (this.c02n!=null)?c02n.toString():"";
	}
	
	public String getC03nView(){
		return (this.c03n!=null)?c03n.toString():"";
	}
	
	public String getC04nView(){
		return (this.c04n!=null)?c04n.toString():"";
	}
	
	public String getC05nView(){
		return (this.c05n!=null)?c05n.toString():"";
	}
	
	public String getC06nView(){
		return (this.c06n!=null)?c06n.toString():"";
	}
	
	public String getC07nView(){
		return (this.c07n!=null)?c07n.toString():"";
	}
	
	public String getC08nView(){
		return (this.c08n!=null)?c08n.toString():"";
	}
	
	public String getC09nView(){
		return (this.c09n!=null)?c09n.toString():"";
	}
	
	public String getC10nView(){
		return (this.c10n!=null)?c10n.toString():"";
	}
	
	public String getC11nView(){
		return (this.c11n!=null)?c11n.toString():"";
	}
	
	public String getC12nView(){
		return (this.c12n!=null)?c12n.toString():"";
	}
	
	public String getC13nView(){
		return (this.c13n!=null)?c13n.toString():"";
	}
	
	public String getC14nView(){
		return (this.c14n!=null)?c14n.toString():"";
	}
	
	public String getC15nView(){
		return (this.c15n!=null)?c15n.toString():"";
	}
	
	public String getC16nView(){
		return (this.c16n!=null)?c16n.toString():"";
	}
	
	public String getC17nView(){
		return (this.c17n!=null)?c17n.toString():"";
	}
	
	public String getC18nView(){
		return (this.c18n!=null)?c18n.toString():"";
	}
	
	public String getC19nView(){
		return (this.c19n!=null)?c19n.toString():"";
	}
	
	public String getC20nView(){
		return (this.c20n!=null)?c20n.toString():"";
	}
	
	public String getC21nView(){
		return (this.c21n!=null)?c21n.toString():"";
	}
	
	public String getC22nView(){
		return (this.c22n!=null)?c22n.toString():"";
	}
	
	public String getC23nView(){
		return (this.c23n!=null)?c23n.toString():"";
	}
	
	public String getC24nView(){
		return (this.c24n!=null)?c24n.toString():"";
	}
	
	public String getC25nView(){
		return (this.c25n!=null)?c25n.toString():"";
	}
	
	public String getC26nView(){
		return (this.c26n!=null)?c26n.toString():"";
	}
	
	public String getC27nView(){
		return (this.c27n!=null)?c27n.toString():"";
	}
	
	public String getC28nView(){
		return (this.c28n!=null)?c28n.toString():"";
	}
	
	public String getC29nView(){
		return (this.c29n!=null)?c29n.toString():"";
	}
	
	public String getC30nView(){
		return (this.c30n!=null)?c30n.toString():"";
	}
	
	public String getRegistroView(){
		return (this.registro!=null)?registro.toString():"";
	}
	
	public String getDesRegistroView(){
		return (this.registro!=null)?registro.toString()+" - "+RegistrosDetalleDJAfip.getById(this.registro).getValue():"";
	}
		
	public String getFilaView(){
		return (this.fila!=null)?fila.toString():"";
	}

	public String getDataStrView() {
		return dataStrView;
	}

	public void setDataStrView(String dataStrView) {
		this.dataStrView = dataStrView;
	}

	public String getContenidoParseadoView() {
		return contenidoParseadoView;
	}

	public void setContenidoParseadoView(String contenidoParseadoView) {
		this.contenidoParseadoView = contenidoParseadoView;
	}	
	
	
}
