//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import coop.tecso.demoda.iface.helper.NumberUtil;

/**
 * Value Object del DecJurPag
 * @author tecso
 *
 */
public class DecJurPagVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "decJurPagVO";
	
	private DecJurVO decJur=new DecJurVO();
	
	private TipPagDecJurVO tipPagDecJur = new TipPagDecJurVO();
	
	private String detalle="";
	
	private Double importe;
	
	private Double saldo;
	
	private String certificado;
	
	private String cuitAgente;
	
	private Date fechaPago;
	
	private AgeRetVO ageRet = new AgeRetVO();
	
	private String importeView;

	private int ingresaCertificado=0;
	
	private boolean conCertificado=false;
	
	// Constructores
	public DecJurPagVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public DecJurPagVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDetalle(desc);
	}

	
	// Getters y Setters

	public DecJurVO getDecJur() {
		return decJur;
	}

	public void setDecJur(DecJurVO decJur) {
		this.decJur = decJur;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
		this.importeView=NumberUtil.round(importe, SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	

	public int getIngresaCertificado() {
		return ingresaCertificado;
	}

	public void setIngresaCertificado(int ingresaCertificado) {
		this.ingresaCertificado = ingresaCertificado;
	}

	public String getCertificado() {
		return certificado;
	}

	public void setCertificado(String certificado) {
		this.certificado = certificado;
	}

	public String getCuitAgente() {
		return cuitAgente;
	}

	public void setCuitAgente(String cuitAgente) {
		this.cuitAgente = cuitAgente;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public TipPagDecJurVO getTipPagDecJur() {
		return tipPagDecJur;
	}

	public void setTipPagDecJur(TipPagDecJurVO tipPagDecJur) {
		this.tipPagDecJur = tipPagDecJur;
	}

	public String getImporteView() {
		return (importe != null)?NumberUtil.round(this.importe, SiatParam.DEC_IMPORTE_VIEW).toString():"0.00";
	}

	public void setImporteView(String importeView) {
		this.importeView = importeView;
	}

	public boolean isConCertificado() {
		return conCertificado;
	}

	public void setConCertificado(boolean conCertificado) {
		this.conCertificado = conCertificado;
	}

	public AgeRetVO getAgeRet() {
		return ageRet;
	}
	public void setAgeRet(AgeRetVO ageRet) {
		this.ageRet = ageRet;
	}
	
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters

}
