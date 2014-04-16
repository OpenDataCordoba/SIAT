//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del CierreBanco
 * @author tecso
 *
 */
public class CierreBancoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "cierreBancoVO";
		
	private EnvioOsirisVO envioOsiris = new EnvioOsirisVO();
	private Integer banco;
	private Long cantTransaccion;
	private Double importeTotal;
	private Integer nroCierreBanco;
	private Date fechaCierre;
	private String fechaCierreView = "";
	private SiNo conciliado = SiNo.OpcionTodo;

	private String nroCierreBancoView = "";
	private String bancoView = "";
	
	// Valores de Cant. de Transacciones y Total Importe calculados a partir del detalle y sin considerar las trasnacciones anuladas o con errores
	private Long cantTransaccionCal;
	private Double importeTotalCalPorNotaImpto;
	private String cantTransaccionCalView = "";
	private String importeTotalCalPorNotaImptoView = "";
	

	
	private List<TranAfipVO> listTranAfip = new ArrayList<TranAfipVO>();
	private List<NotaImptoVO> listNotaImpto = new ArrayList<NotaImptoVO>();
	private List<NovedadEnvioVO> listNovedadEnvio = new ArrayList<NovedadEnvioVO>();
	private List<CierreSucursalVO> listCierreSucursal = new ArrayList<CierreSucursalVO>();

	// Buss Flags
	private Boolean conciliarBussEnabled   = true;
	
	// Constructores
	public CierreBancoVO() {
		super();
	}

	
	// Getters y Setters
	public Integer getBanco() {
		return banco;
	}

	public void setBanco(Integer banco) {
		this.banco = banco;
		this.bancoView = StringUtil.formatInteger(banco);
	}

	public Long getCantTransaccion() {
		return cantTransaccion;
	}

	public void setCantTransaccion(Long cantTransaccion) {
		this.cantTransaccion = cantTransaccion;
	}

	public Double getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(Double importeTotal) {
		this.importeTotal = importeTotal;
	}

	public Integer getNroCierreBanco() {
		return nroCierreBanco;
	}

	public void setNroCierreBanco(Integer nroCierreBanco) {
		this.nroCierreBanco = nroCierreBanco;
		this.nroCierreBancoView = StringUtil.formatInteger(nroCierreBanco);
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;		
		this.fechaCierreView = DateUtil.formatDate(fechaCierre, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public SiNo getConciliado() {
		return conciliado;
	}
	
	public void setConciliado(SiNo conciliado) {
		this.conciliado = conciliado;
	}
	
	public void setListTranAfip(List<TranAfipVO> listTranAfip) {
		this.listTranAfip = listTranAfip;
	}

	public List<TranAfipVO> getListTranAfip() {
		return listTranAfip;
	}

	public String getCantTransaccionView(){
		return (this.cantTransaccion!=null)?cantTransaccion.toString():"";
	}
	
	public void setBancoView(String bancoView) {
		this.bancoView = bancoView;
	}

	public String getBancoView(){
		return this.bancoView;
	}
	
	public String getImporteTotalView(){
		return (this.importeTotal!=null)?importeTotal.toString():"";
	}
	
	public void setNroCierreBancoView(String nroCierreBancoView) {
		this.nroCierreBancoView = nroCierreBancoView;
	}

	public String getNroCierreBancoView(){
		return this.nroCierreBancoView;
	}
	
	public String getFechaCierreView(){
		return fechaCierreView;
	}

	public List<CierreSucursalVO> getListCierreSucursal() {
		return listCierreSucursal;
	}

	public void setListCierreSucursal(List<CierreSucursalVO> listCierreSucursal) {
		this.listCierreSucursal = listCierreSucursal;
	}

	public List<NotaImptoVO> getListNotaImpto() {
		return listNotaImpto;
	}

	public void setListNotaImpto(List<NotaImptoVO> listNotaImpto) {
		this.listNotaImpto = listNotaImpto;
	}

	public List<NovedadEnvioVO> getListNovedadEnvio() {
		return listNovedadEnvio;
	}

	public void setListNovedadEnvio(List<NovedadEnvioVO> listNovedadEnvio) {
		this.listNovedadEnvio = listNovedadEnvio;
	}
	
	public Long getCantTransaccionCal() {
		return cantTransaccionCal;
	}

	public void setCantTransaccionCal(Long cantTransaccionCal) {
		this.cantTransaccionCal = cantTransaccionCal;
		this.cantTransaccionCalView = StringUtil.formatLong(cantTransaccionCal);
	}

	public String getCantTransaccionCalView() {
		return cantTransaccionCalView;
	}

	public void setCantTransaccionCalView(String cantTransaccionCalView) {
		this.cantTransaccionCalView = cantTransaccionCalView;
	}

	public Double getImporteTotalCalPorNotaImpto() {
		return importeTotalCalPorNotaImpto;
	}

	public void setImporteTotalCalPorNotaImpto(Double importeTotalCalPorNotaImpto) {
		this.importeTotalCalPorNotaImpto = importeTotalCalPorNotaImpto;
		this.importeTotalCalPorNotaImptoView = StringUtil.formatDouble(importeTotalCalPorNotaImpto);
	}

	public String getImporteTotalCalPorNotaImptoView() {
		return importeTotalCalPorNotaImptoView;
	}

	public void setFechaCierreView(String fechaCierreView) {
		this.fechaCierreView = fechaCierreView;
	}

	public EnvioOsirisVO getEnvioOsiris() {
		return envioOsiris;
	}

	public void setEnvioOsiris(EnvioOsirisVO envioOsiris) {
		this.envioOsiris = envioOsiris;
	}

	// Flags Seguridad
	public Boolean getConciliarBussEnabled() {
		return conciliarBussEnabled;
	}

	public void setConciliarBussEnabled(Boolean conciliarBussEnabled) {
		this.conciliarBussEnabled = conciliarBussEnabled;
	}
	
	public String getConciliarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getConciliarBussEnabled(), 
				BalSecurityConstants.ABM_MOVBAN, BaseSecurityConstants.CONCILIAR);
	}	
	
}
