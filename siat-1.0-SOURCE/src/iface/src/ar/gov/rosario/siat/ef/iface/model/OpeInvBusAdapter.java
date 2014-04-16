//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del OpeInvCon
 * 
 * @author tecso
 */
public class OpeInvBusAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "opeInvBusAdapterVO";
	
	private Long idOpeInv;
	OpeInvBusVO opeInvBus = new OpeInvBusVO();
 
	private ContribuyenteVO contribuyente = new ContribuyenteVO();
	private SiNo cer = SiNo.OpcionTodo;
	private Double promedioPagoContr = 0D;
	private Date fecPromedioPagoContrDesde;
	private Date fecPromedioPagoContrHasta;
	private String fecPromedioPagoContrDesdeView="";
	private String fecPromedioPagoContrHastaView="";

	private Double promedioPagoDeCuenta = 0D;
	private Integer cantPeriodosNoDeclarados;
	private Integer cantPersonal;
	private Date fecPromedioPagoDeCtaDesde;
	private Date fecPromedioPagoDeCtaHasta;
	private String fecPromedioPagoDeCtaDesdeView;
	private String fecPromedioPagoDeCtaHastaView;
		
	private EstadoOpeInvConVO estadoOtroOperativo = new EstadoOpeInvConVO();
	
	private Integer altaFiscal;
	
	
	private TipObjImpDefinition tipObjImpDefinition4Contr = new TipObjImpDefinition();
	private TipObjImpDefinition tipObjImpDefinition4Comercio = new TipObjImpDefinition();
	
	private List<SiNo> listSiNo = SiNo.getList(SiNo.OpcionTodo);
	
	private List<EstadoOpeInvConVO> listEstadoOpeInvConVO = new ArrayList<EstadoOpeInvConVO>();
	
	// flags para ver observaciones del estado seleccinoado
	
	// flags de permisos
	
    // Constructores
    public OpeInvBusAdapter(){
    	super(EfSecurityConstants.ADM_OPEINVCON);
    }


    //  Getters y Setters
	public OpeInvBusVO getOpeInvBus() {
		return opeInvBus;
	}

	public void setOpeInvBus(OpeInvBusVO opeInvBusVO) {
		this.opeInvBus = opeInvBusVO;
	}

	public TipObjImpDefinition getTipObjImpDefinition4Contr() {
		return tipObjImpDefinition4Contr;
	}


	public void setTipObjImpDefinition4Contr(
			TipObjImpDefinition tipObjImpDefinition4Contr) {
		this.tipObjImpDefinition4Contr = tipObjImpDefinition4Contr;
	}


	public TipObjImpDefinition getTipObjImpDefinition4Comercio() {
		return tipObjImpDefinition4Comercio;
	}


	public void setTipObjImpDefinition4Comercio(
			TipObjImpDefinition tipObjImpDefinition4Comercio) {
		this.tipObjImpDefinition4Comercio = tipObjImpDefinition4Comercio;
	}


	public ContribuyenteVO getContribuyente() {
		return contribuyente;
	}


	public void setContribuyente(ContribuyenteVO contribuyente) {
		this.contribuyente = contribuyente;
	}


	public SiNo getCer() {
		return cer;
	}


	public void setCer(SiNo cer) {
		this.cer = cer;
	}


	public List<SiNo> getListSiNo() {
		return listSiNo;
	}


	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}


	public Double getPromedioPagoContr() {
		return promedioPagoContr;
	}


	public void setPromedioPagoContr(Double promedioPagoContr) {
		this.promedioPagoContr = promedioPagoContr;
	}


	public Date getFecPromedioPagoContrDesde() {
		return fecPromedioPagoContrDesde;
	}


	public void setFecPromedioPagoContrDesde(Date fechaPromedioDesde) {
		this.fecPromedioPagoContrDesde = fechaPromedioDesde;
		this.fecPromedioPagoContrDesdeView = DateUtil.formatDate(fechaPromedioDesde, DateUtil.ddSMMSYYYY_MASK);
	}


	public Date getFecPromedioPagoContrHasta() {
		return fecPromedioPagoContrHasta;
	}


	public void setFecPromedioPagoContrHasta(Date fechaPromedioHasta) {
		this.fecPromedioPagoContrHasta = fechaPromedioHasta;
		this.fecPromedioPagoContrHastaView = DateUtil.formatDate(fechaPromedioHasta, DateUtil.ddSMMSYYYY_MASK);
	}


	public String getFecPromedioPagoContrDesdeView() {
		return fecPromedioPagoContrDesdeView;
	}


	public void setFecPromedioPagoContrDesdeView(String fechaPromedioDesdeView) {
		this.fecPromedioPagoContrDesdeView = fechaPromedioDesdeView;
	}


	public String getFecPromedioPagoContrHastaView() {
		return fecPromedioPagoContrHastaView;
	}


	public void setFecPromedioPagoContrHastaView(String fechaPromedioHastaView) {
		this.fecPromedioPagoContrHastaView = fechaPromedioHastaView;
	}


	public Double getPromedioPagoDeCuenta() {
		return promedioPagoDeCuenta;
	}


	public void setPromedioPagoDeCuenta(Double promedioPagoDeCuenta) {
		this.promedioPagoDeCuenta = promedioPagoDeCuenta;
	}


	public Integer getCantPeriodosNoDeclarados() {
		return cantPeriodosNoDeclarados;
	}


	public void setCantPeriodosNoDeclarados(Integer cantPeriodosNoDeclarados) {
		this.cantPeriodosNoDeclarados = cantPeriodosNoDeclarados;
	}


	public Integer getCantPersonal() {
		return cantPersonal;
	}


	public void setCantPersonal(Integer cantPersonal) {
		this.cantPersonal = cantPersonal;
	}


	public Date getFecPromedioPagoDeCtaDesde() {
		return fecPromedioPagoDeCtaDesde;
	}


	public void setFecPromedioPagoDeCtaDesde(Date fecPromedioPagoDeCtaDesde) {
		this.fecPromedioPagoDeCtaDesde = fecPromedioPagoDeCtaDesde;
	}


	public Date getFecPromedioPagoDeCtaHasta() {
		return fecPromedioPagoDeCtaHasta;
	}


	public void setFecPromedioPagoDeCtaHasta(Date fecPromedioPagoDeCtaHasta) {
		this.fecPromedioPagoDeCtaHasta = fecPromedioPagoDeCtaHasta;
	}


	public String getFecPromedioPagoDeCtaDesdeView() {
		return fecPromedioPagoDeCtaDesdeView;
	}


	public void setFecPromedioPagoDeCtaDesdeView(
			String fecPromedioPagoDeCtaDesdeView) {
		this.fecPromedioPagoDeCtaDesdeView = fecPromedioPagoDeCtaDesdeView;
	}


	public String getFecPromedioPagoDeCtaHastaView() {
		return fecPromedioPagoDeCtaHastaView;
	}


	public void setFecPromedioPagoDeCtaHastaView(String fecPromedioPagoDeCtaHastaView) {
		this.fecPromedioPagoDeCtaHastaView = fecPromedioPagoDeCtaHastaView;
	}


	public List<EstadoOpeInvConVO> getListEstadoOpeInvConVO() {
		return listEstadoOpeInvConVO;
	}


	public void setListEstadoOpeInvConVO(List<EstadoOpeInvConVO> listEstadoOpeInvConVO) {
		this.listEstadoOpeInvConVO = listEstadoOpeInvConVO;
	}


	public EstadoOpeInvConVO getEstadoOtroOperativo() {
		return estadoOtroOperativo;
	}


	public void setEstadoOtroOperativo(EstadoOpeInvConVO estadoOtroOperativo) {
		this.estadoOtroOperativo = estadoOtroOperativo;
	}


	public Long getIdOpeInv() {
		return idOpeInv;
	}


	public void setIdOpeInv(Long idOpeInv) {
		this.idOpeInv = idOpeInv;
	}


	public Integer getAltaFiscal() {
		return altaFiscal;
	}


	public void setAltaFiscal(Integer altaFiscal) {
		this.altaFiscal = altaFiscal;
	}
    
	public String getAltaFiscalView(){
		return (this.altaFiscal!=null)?this.altaFiscal.toString():"";
	}
	

    // View getters
	
}
