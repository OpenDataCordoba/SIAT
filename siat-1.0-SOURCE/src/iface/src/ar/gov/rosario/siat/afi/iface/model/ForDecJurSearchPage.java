//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.afi.iface.util.AfiSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.model.TipPagDecJurVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.FormularioAfip;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del ForDecJur
 * 
 * @author Tecso
 *
 */
public class ForDecJurSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "forDecJurSearchPageVO";
	
	private ForDecJurVO forDecJur= new ForDecJurVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<FormularioAfip> listFormulario = new ArrayList<FormularioAfip>();
	private List<EstForDecJurVO> listEstForDecJur = new ArrayList<EstForDecJurVO>();
	
	private Date fechaPresentacionDesde;
	private Date fechaPresentacionHasta;
	private Integer codActividad;
	
	private Double baseImponibleDesde;
	private Double baseImponibleHasta;
	
	private Double alicuotaDesde;
	private Double alicuotaHasta;
	
	private Double adiMesasYSillasDesde;
	private Double adiMesasYSillasHasta;
	
	private Double adiPublicidadDesde;
	private Double adiPublicidadHasta;
	
	private List<TipPagDecJurVO> listTipPagDecJur = new ArrayList<TipPagDecJurVO>();
	private TipPagDecJurVO tipPagDecJur = new TipPagDecJurVO();
	
	 // Flags
    private Boolean generarDecJurBussEnabled = true;
    
	// Constructores
	public ForDecJurSearchPage() {       
       super(AfiSecurityConstants.ABM_FORDECJUR);        
    }
	
	// Getters y Setters
	public String getName(){    
		return NAME;
	}
	
	public ForDecJurVO getForDecJur() {
		return forDecJur;
	}
	
	public void setForDecJur(ForDecJurVO forDecJur) {
		this.forDecJur = forDecJur;
	}           
	
    public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<FormularioAfip> getListFormulario() {
		return listFormulario;
	}

	public void setListFormulario(List<FormularioAfip> listFormulario) {
		this.listFormulario = listFormulario;
	}
	
	public List<EstForDecJurVO> getListEstForDecJur() {
		return listEstForDecJur;
	}

	public void setListEstForDecJur(List<EstForDecJurVO> listEstForDecJur) {
		this.listEstForDecJur = listEstForDecJur;
	}

	public Date getFechaPresentacionDesde() {
		return fechaPresentacionDesde;
	}

	public void setFechaPresentacionDesde(Date fechaPresentacionDesde) {
		this.fechaPresentacionDesde = fechaPresentacionDesde;
	}

	public Date getFechaPresentacionHasta() {
		return fechaPresentacionHasta;
	}

	public void setFechaPresentacionHasta(Date fechaPresentacionHasta) {
		this.fechaPresentacionHasta = fechaPresentacionHasta;
	}
	
	public Integer getCodActividad() {
		return codActividad;
	}

	public void setCodActividad(Integer codActividad) {
		this.codActividad = codActividad;
	}

	public Double getBaseImponibleDesde() {
		return baseImponibleDesde;
	}

	public void setBaseImponibleDesde(Double baseImponibleDesde) {
		this.baseImponibleDesde = baseImponibleDesde;
	}

	public Double getBaseImponibleHasta() {
		return baseImponibleHasta;
	}

	public void setBaseImponibleHasta(Double baseImponibleHasta) {
		this.baseImponibleHasta = baseImponibleHasta;
	}

	public Double getAlicuotaDesde() {
		return alicuotaDesde;
	}

	public void setAlicuotaDesde(Double alicuotaDesde) {
		this.alicuotaDesde = alicuotaDesde;
	}

	public Double getAlicuotaHasta() {
		return alicuotaHasta;
	}

	public void setAlicuotaHasta(Double alicuotaHasta) {
		this.alicuotaHasta = alicuotaHasta;
	}

	public Double getAdiMesasYSillasDesde() {
		return adiMesasYSillasDesde;
	}

	public void setAdiMesasYSillasDesde(Double adiMesasYSillasDesde) {
		this.adiMesasYSillasDesde = adiMesasYSillasDesde;
	}

	public Double getAdiMesasYSillasHasta() {
		return adiMesasYSillasHasta;
	}

	public void setAdiMesasYSillasHasta(Double adiMesasYSillasHasta) {
		this.adiMesasYSillasHasta = adiMesasYSillasHasta;
	}

	public Double getAdiPublicidadDesde() {
		return adiPublicidadDesde;
	}

	public void setAdiPublicidadDesde(Double adiPublicidadDesde) {
		this.adiPublicidadDesde = adiPublicidadDesde;
	}

	public Double getAdiPublicidadHasta() {
		return adiPublicidadHasta;
	}

	public void setAdiPublicidadHasta(Double adiPublicidadHasta) {
		this.adiPublicidadHasta = adiPublicidadHasta;
	}

	public List<TipPagDecJurVO> getListTipPagDecJur() {
		return listTipPagDecJur;
	}

	public void setListTipPagDecJur(List<TipPagDecJurVO> listTipPagDecJur) {
		this.listTipPagDecJur = listTipPagDecJur;
	}

	public TipPagDecJurVO getTipPagDecJur() {
		return tipPagDecJur;
	}

	public void setTipPagDecJur(TipPagDecJurVO tipPagDecJur) {
		this.tipPagDecJur = tipPagDecJur;
	}

	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listado de Formularios de Declaraci\u00F3n Jurada");
		report.setReportBeanName("ForDecJur");
		report.setReportFileName(this.getClass().getName());

		
		RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				this.getForDecJur().getRecurso().getId(),
				this.getListRecurso());
		if (recursoVO != null){
			report.addReportFiltro("Recurso", recursoVO.getDesRecurso());
		}
		
		report.addReportFiltro("Recurso", getForDecJur().getRecurso().getDesRecurso());
		
		if(!StringUtil.isNullOrEmpty(getForDecJur().getEnvioOsiris().getIdEnvioAfipView()))
			report.addReportFiltro("Env\u00EDo AFIP", this.getForDecJur().getEnvioOsiris().getIdEnvioAfipView());	
		if(!StringUtil.isNullOrEmpty(getForDecJur().getTranAfip().getIdTransaccionAfipView()))
			report.addReportFiltro("Id. Transacci\u00F3n", getForDecJur().getTranAfip().getIdTransaccionAfipView());	
		
		report.addReportFiltro("Nro. de Formulario", this.getForDecJur().getNroFormularioView());
		if(!StringUtil.isNullOrEmpty(getForDecJur().getCuit()))
			report.addReportFiltro("CUIT", this.getForDecJur().getCuit());
		
 		if(!StringUtil.isNullOrEmpty(getFechaPresentacionDesdeView()))
 			report.addReportFiltro("Fecha de Pres. Desde", getFechaPresentacionDesdeView());
 		if(!StringUtil.isNullOrEmpty(getFechaPresentacionHastaView()))
 			report.addReportFiltro("Fecha de Pres. Hasta", getFechaPresentacionHastaView());
 		
 		if(!StringUtil.isNullOrEmpty(getBaseImponibleDesdeView()))
 			report.addReportFiltro("Base Imponible Desde", getBaseImponibleDesdeView());
 		if(!StringUtil.isNullOrEmpty(getBaseImponibleHastaView()))
 			report.addReportFiltro("Base Imponible Hasta", getBaseImponibleHastaView());
 		
 		if(!StringUtil.isNullOrEmpty(getAlicuotaDesdeView()))
 			report.addReportFiltro("Al\u00Ecuota Desde", getAlicuotaDesdeView());
 		if(!StringUtil.isNullOrEmpty(getAlicuotaHastaView()))
 			report.addReportFiltro("Al\u00Ecuota Hasta", getAlicuotaHastaView());
 		
 		if(!StringUtil.isNullOrEmpty(getAdiMesasYSillasDesdeView()))
 			report.addReportFiltro("Adi. Mesas y Sillas Desde", getAdiMesasYSillasDesdeView());
 		if(!StringUtil.isNullOrEmpty(getAdiMesasYSillasHastaView()))
 			report.addReportFiltro("Adi. Mesas y Sillas Hasta", getAdiMesasYSillasHastaView());
 		
 		if(!StringUtil.isNullOrEmpty(getAdiPublicidadDesdeView()))
 			report.addReportFiltro("Adi. Publicidad Desde", getAdiPublicidadDesdeView());
 		if(!StringUtil.isNullOrEmpty(getAdiPublicidadHastaView()))
 			report.addReportFiltro("Adi. Publicidad Hasta", getAdiPublicidadHastaView());
 		
 		if(!StringUtil.isNullOrEmpty(getCodActividadView()))
 			report.addReportFiltro("Cod. Act. AFIP", getCodActividadView());
 		
		TipPagDecJurVO tipPagDecJurVO = (TipPagDecJurVO) ModelUtil.getBussImageModelByIdForList(
				this.getTipPagDecJur().getId(),
				this.getListTipPagDecJur());
		if (tipPagDecJurVO != null){
			report.addReportFiltro("Tipo de Pago", tipPagDecJurVO.getDesTipPag());
		}
 		
		EstForDecJurVO estForDecJurVO = (EstForDecJurVO) ModelUtil.getBussImageModelByIdForList(
				this.getForDecJur().getEstForDecJur().getId(),
				this.getListEstForDecJur());
		if (estForDecJurVO != null){
			report.addReportFiltro("Estado", estForDecJurVO.getDescripcion());
		}
 		
 		

		ReportTableVO rtFormularioDJ = new ReportTableVO("rtFormularioDJ");
		rtFormularioDJ.setTitulo("B\u00FAsqueda de ForDecJur");

		// carga de columnas
		rtFormularioDJ.addReportColumn("Per\u00EDodo Fiscal","periodoFiscal");
		rtFormularioDJ.addReportColumn("CUIT", "cuit");
		rtFormularioDJ.addReportColumn("Fecha Presentaci\u00F3n","fechaPresentacion");
		rtFormularioDJ.addReportColumn("Nro. de Formulario", "nroFormulario");
		rtFormularioDJ.addReportColumn("C\u00F3d. Rec.", "codRectif");
		rtFormularioDJ.addReportColumn("Estado", "estForDecJur.descripcion");
		
		 
	    report.getReportListTable().add(rtFormularioDJ);

	}
	
	// View getters
	public String getFechaPresentacionHastaView() {
		return DateUtil.formatDate(fechaPresentacionHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public String getFechaPresentacionDesdeView() {
		return DateUtil.formatDate(fechaPresentacionDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public String getAlicuotaDesdeView() {
		return StringUtil.formatDouble(alicuotaDesde,"###0.00");
	}
	
	public String getAlicuotaHastaView() {
		return StringUtil.formatDouble(alicuotaHasta,"###0.00");
	}
	
	public String getBaseImponibleDesdeView() {
		return StringUtil.formatDouble(baseImponibleDesde,"###0.00");
	}
	
	public String getBaseImponibleHastaView() {
		return StringUtil.formatDouble(baseImponibleHasta,"###0.00");
	}

	public String getAdiPublicidadDesdeView() {
		return StringUtil.formatDouble(adiPublicidadDesde,"###0.00");
	}
	
	public String getAdiPublicidadHastaView() {
		return StringUtil.formatDouble(adiPublicidadHasta,"###0.00");
	}
	public String getAdiMesasYSillasDesdeView() {
		return StringUtil.formatDouble(adiMesasYSillasDesde,"###0.00");
	}
	
	public String getAdiMesasYSillasHastaView() {
		return StringUtil.formatDouble(adiMesasYSillasHasta,"###0.00");
	}
	
	public String getCodActividadView() {
		return StringUtil.formatInteger(codActividad);
	}
	
	// Flags Seguridad
	public Boolean getGenerarDecJurBussEnabled() {
		return generarDecJurBussEnabled;
	}

	public void setGenerarDecJurBussEnabled(Boolean generarDecJurBussEnabled) {
		this.generarDecJurBussEnabled = generarDecJurBussEnabled;
	}
	
	public String getGenerarDecJurEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getGenerarDecJurBussEnabled(), 
				AfiSecurityConstants.ABM_FORDECJUR, AfiSecurityConstants.MTD_GENERARDECJUR);
	}
}
