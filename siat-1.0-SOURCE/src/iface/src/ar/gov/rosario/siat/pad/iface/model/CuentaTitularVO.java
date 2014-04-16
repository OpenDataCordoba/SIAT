//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.model.SiNo;

public class CuentaTitularVO extends SiatBussImageModel {

	private static final long serialVersionUID = 0;

	public static final String NAME = "cuentaTitularVO";
	
	private CuentaVO cuenta = new CuentaVO();

	private ContribuyenteVO contribuyente = new ContribuyenteVO();
	
	private TipoTitularVO   tipoTitular   = new TipoTitularVO();
	
	private SiNo esTitularPrincipal = SiNo.OpcionNula;

	private Date fechaDesde;

	private Date fechaHasta;

	private Date fechaNovedad;

	private SiNo esAltaManual = SiNo.OpcionNula;
	
	private List<LiqDeudaVO> listDeuda = new ArrayList<LiqDeudaVO>();
	
	private String fechaDesdeView   = "";
	private String fechaHastaView   = "";
	private String fechaNovedadView = "";
	
	private Boolean marcarPrincipalBussEnabled = Boolean.TRUE;
	
	private boolean recursoPermiteEmision = false;
	
	// para reporte de deuda cotribuyente: suma de todos los totales d liqDeudaVO
	private Double total=0.0;
	private String totalView="";
	private Double totalViaJudicial=0.0;
	private String totalViaJudicialView="";
	private Double totalViaAdmin=0.0;
	private String totalViaAdminView="";
	private Double totalViaCyQ=0.0;
	private String totalViaCyQView="";
	
	
	// Constructores	
	public CuentaTitularVO() {
		super();
	}

	public ContribuyenteVO getContribuyente() {
		return contribuyente;
	}
	public void setContribuyente(ContribuyenteVO contribuyente) {
		this.contribuyente = contribuyente;
	}
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	public SiNo getEsAltaManual() {
		return esAltaManual;
	}
	public void setEsAltaManual(SiNo esAltaManual) {
		this.esAltaManual = esAltaManual;
	}
	public SiNo getEsTitularPrincipal() {
		return esTitularPrincipal;
	}
	public void setEsTitularPrincipal(SiNo esTitularPrincipal) {
		this.esTitularPrincipal = esTitularPrincipal;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}
	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	public Date getFechaNovedad() {
		return fechaNovedad;
	}
	public void setFechaNovedad(Date fechaNovedad) {
		this.fechaNovedad = fechaNovedad;
		this.fechaNovedadView = DateUtil.formatDate(fechaNovedad, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaNovedadView() {
		return fechaNovedadView;
	}
	public void setFechaNovedadView(String fechaNovedadView) {
		this.fechaNovedadView = fechaNovedadView;
	}
	public TipoTitularVO getTipoTitular() {
		return tipoTitular;
	}
	public void setTipoTitular(TipoTitularVO tipoTitular) {
		this.tipoTitular = tipoTitular;
	}
	
	public Boolean getMarcarPrincipalBussEnabled() {
		return marcarPrincipalBussEnabled;
	}
	public void setMarcarPrincipalBussEnabled(Boolean marcarPrincipalBussEnabled) {
		this.marcarPrincipalBussEnabled = marcarPrincipalBussEnabled;
	}

	public boolean isRecursoPermiteEmision() {
		return recursoPermiteEmision;
	}
	public void setRecursoPermiteEmision(boolean recursoPermiteEmision) {
		this.recursoPermiteEmision = recursoPermiteEmision;
	}

	public String getInformacionView(){
		String inf = this.getContribuyente().getPersona().getTipoPersonaView();
		
		if(this.getEsTitularPrincipal().getEsSI()){
			inf += " / Principal";
		}else{
			inf += " / Secundario";
		}
		 
		return inf;
	}

	public List<LiqDeudaVO> getListDeuda() {
		return listDeuda;
	}

	public void setListDeuda(List<LiqDeudaVO> listDeuda) {
		this.listDeuda = listDeuda;
	}
	
	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
		this.totalView = NumberUtil.round(total,SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	public String getTotalView() {
		return totalView;
	}

	public void setTotalView(String totalView) {
		this.totalView = totalView;
	}

	public Double getTotalViaJudicial() {
		return totalViaJudicial;
	}

	public void setTotalViaJudicial(Double totalViaJudicial) {
		this.totalViaJudicial = totalViaJudicial;
		this.totalViaJudicialView = NumberUtil.round(totalViaJudicial,SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	public String getTotalViaJudicialView() {
		return totalViaJudicialView;
	}

	public void setTotalViaJudicialView(String totalViaJudicialView) {
		this.totalViaJudicialView = totalViaJudicialView;
	}

	public Double getTotalViaAdmin() {
		return totalViaAdmin;
	}

	public void setTotalViaAdmin(Double totalViaAdmin) {
		this.totalViaAdmin = totalViaAdmin;
		this.totalViaAdminView = NumberUtil.round(totalViaAdmin,SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	public String getTotalViaAdminView() {
		return totalViaAdminView;
	}

	public void setTotalViaAdminView(String totalViaAdminView) {
		this.totalViaAdminView = totalViaAdminView;
	}

	public Double getTotalViaCyQ() {
		return totalViaCyQ;
	}

	public void setTotalViaCyQ(Double totalViaCyQ) {
		this.totalViaCyQ = totalViaCyQ;
		this.totalViaCyQView = NumberUtil.round(totalViaCyQ,SiatParam.DEC_IMPORTE_VIEW).toString();
	}

	public String getTotalViaCyQView() {
		return totalViaCyQView;
	}

	public void setTotalViaCyQView(String totalViaCyQView) {
		this.totalViaCyQView = totalViaCyQView;
	}

	
}
