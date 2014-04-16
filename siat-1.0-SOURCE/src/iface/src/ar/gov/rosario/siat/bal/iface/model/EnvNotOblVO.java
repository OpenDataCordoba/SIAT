//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del EnvNotObl
 * @author tecso
 *
 */
public class EnvNotOblVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "envNotOblVO";
	
	private Integer banco;
	
	private Integer nroCierreBanco;
	
	private Integer bancoOriginal;
	
	private Integer nroCieBanOrig;
	
	private Double totalAcreditado;
	
	private Double totalCredito;
	
	private Date fechaRegistro;
	
	private EnvioOsirisVO envioOsiris = new EnvioOsirisVO(); 
	
	private String fechaRegistroView = "";
	// Buss Flags
	
	// View Constants
	
	// Constructores
	public EnvNotOblVO() {
		super();
	}

	// Getters y Setters
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
		this.fechaRegistroView = DateUtil.formatDate(fechaRegistro, DateUtil.ddSMMSYYYY_MASK);
	}

	public Integer getBanco() {
		return banco;
	}

	public void setBanco(Integer banco) {
		this.banco = banco;
	}

	public Integer getNroCierreBanco() {
		return nroCierreBanco;
	}

	public void setNroCierreBanco(Integer nroCierreBanco) {
		this.nroCierreBanco = nroCierreBanco;
	}

	public Integer getBancoOriginal() {
		return bancoOriginal;
	}

	public void setBancoOriginal(Integer bancoOriginal) {
		this.bancoOriginal = bancoOriginal;
	}

	public Integer getNroCieBanOrig() {
		return nroCieBanOrig;
	}

	public void setNroCieBanOrig(Integer nroCieBanOrig) {
		this.nroCieBanOrig = nroCieBanOrig;
	}

	public Double getTotalAcreditado() {
		return totalAcreditado;
	}

	public void setTotalAcreditado(Double totalAcreditado) {
		this.totalAcreditado = totalAcreditado;
	}

	public void setTotalCredito(Double totalCredito) {
		this.totalCredito = totalCredito;
	}

	public Double getTotalCredito() {
		return totalCredito;
	}

	public EnvioOsirisVO getEnvioOsiris() {
		return envioOsiris;
	}

	public void setEnvioOsiris(EnvioOsirisVO envioOsiris) {
		this.envioOsiris = envioOsiris;
	}
	// Buss flags getters y setters
	
	// View flags getters
	
	// View getters
	public String getFechaRegistroView() {
		return fechaRegistroView;
	}
	
	public String getBancoView() {
		return (this.banco!=null)?banco.toString():"";
	}
	
	public String getNroCierreBancoView() {
		return (this.nroCierreBanco!=null)?nroCierreBanco.toString():"";
	}
	
	public String getBancoOriginalView() {
		return (this.bancoOriginal!=null)?bancoOriginal.toString():"";
	}
	
	public String getNroCieBanOrigView() {
		return (this.nroCieBanOrig!=null)?nroCieBanOrig.toString():"";
	}
	
	public String getTotalAcreditadoView() {
		return (this.totalAcreditado!=null)?totalAcreditado.toString():"";
	}
	
	public String getTotalCreditoView() {
		return (this.totalCredito!=null)?totalCredito.toString():"";
	}
}
