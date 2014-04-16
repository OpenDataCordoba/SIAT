//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del DetAju
 * 
 * @author tecso
 */
public class DetAjuAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "detAjuAdapterVO";

	public static final String ENC_NAME = "encDetAjuAdapterVO";
	
    private DetAjuVO detAju = new DetAjuVO();
    
    private List<OrdConCueVO>  listOrdConCue = new ArrayList<OrdConCueVO>();

    private List<AliComFueColVO> listAliComFueCol = new ArrayList<AliComFueColVO>();
    
    // campos para agregarMasivo
    private Integer tipoAgregarMasivo;// puede ser: agregarPersonal (1), adicional publicidad (2), adicional mesas y sillas (3), porcentaje multa (4)

    private Integer cantPersonalAgregarMasivo;
    
    private Double porcentajeAgregarMasivo;
    
    private Long idDetAjuDetDesde;
    
    private Long idDetAjuDetHasta;
    
    // campos para modificar retenciones
    private DetAjuDetVO DetAjuDet = new DetAjuDetVO(); 
    
    // flags
	private boolean calcularBasesEnabled = true;

	private boolean agregarPersonalEnabled= true;

	private boolean adicionalPublicidadEnabled= true;

	private boolean adicionalMesasYSillasEnabled = true;
	
	private boolean determPorMultaEnabled = true;
    
    // Constructores
    public DetAjuAdapter(){
    	super(EfSecurityConstants.ABM_DETAJU);
    	ACCION_MODIFICAR_ENCABEZADO = EfSecurityConstants.ABM_DETAJU_ENC;
    }
    
    //  Getters y Setters
	public DetAjuVO getDetAju() {
		return detAju;
	}

	public void setDetAju(DetAjuVO detAjuVO) {
		this.detAju = detAjuVO;
	}
	
	public List<OrdConCueVO> getListOrdConCue() {
		return listOrdConCue;
	}

	public void setListOrdConCue(List<OrdConCueVO> listOrdConCue) {
		this.listOrdConCue = listOrdConCue;
	}

	public List<AliComFueColVO> getListAliComFueCol() {
		return listAliComFueCol;
	}

	public void setListAliComFueCol(List<AliComFueColVO> listAliComFueCol) {
		this.listAliComFueCol = listAliComFueCol;
	}

	public Integer getTipoAgregarMasivo() {
		return tipoAgregarMasivo;
	}

	public void setTipoAgregarMasivo(Integer tipoAgregarMasivo) {
		this.tipoAgregarMasivo = tipoAgregarMasivo;
	}
		
	public Integer getCantPersonalAgregarMasivo() {
		return cantPersonalAgregarMasivo;
	}

	public void setCantPersonalAgregarMasivo(Integer cantPersonalAgregarMasivo) {
		this.cantPersonalAgregarMasivo = cantPersonalAgregarMasivo;
	}

	public Double getPorcentajeAgregarMasivo() {
		return porcentajeAgregarMasivo;
	}


	public void setPorcentajeAgregarMasivo(Double porcentajeAgregarMasivo) {
		this.porcentajeAgregarMasivo = porcentajeAgregarMasivo;
	}

	public Long getIdDetAjuDetDesde() {
		return idDetAjuDetDesde;
	}

	public void setIdDetAjuDetDesde(Long idDetAjuDetDesde) {
		this.idDetAjuDetDesde = idDetAjuDetDesde;
	}

	public Long getIdDetAjuDetHasta() {
		return idDetAjuDetHasta;
	}

	public void setIdDetAjuDetHasta(Long idDetAjuDetHasta) {
		this.idDetAjuDetHasta = idDetAjuDetHasta;
	}

	public DetAjuDetVO getDetAjuDet() {
		return DetAjuDet;
	}

	public void setDetAjuDet(DetAjuDetVO detAjuDet) {
		DetAjuDet = detAjuDet;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport_OLD (Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setPageHeight(21);
		 report.setPageWidth(29.7);
		 report.setReportFormat(format);	
		 report.setReportTitle("Determinación de Deuda Fiscal");     
		 report.setReportBeanName("DetAju");
		 report.setReportFileName(this.getClass().getName());
		
		 ReportVO reportDatos = new ReportVO();
		 reportDatos.addReportDato("CUIT", "ordenControl.CUIT4Report");
		 reportDatos.addReportDato("Contribuyente", "ordenControl.contribuyente.persona.represent");
		 reportDatos.addReportDato("Domicilio", "ordenControl.domicilio4Report");		
		 reportDatos.addReportDato("Recurso", "ordConCue.cuenta.recurso.codRecurso");
		 reportDatos.addReportDato("Cuenta", "ordConCue.cuenta.numeroCuenta");
		 reportDatos.addReportDato(detAju.getOrdenControl().getTipoOrden().getDesTipoOrden()+" N°", "ordenControl.numeroOrden");
		 reportDatos.addReportDato("Rubros","rubrosObjImp4Report");
		 reportDatos.addReportDato("Catastral","catastral4Report");
		 reportDatos.addReportDato("Ficha Número","nroPermisoHab4Report");
		 reportDatos.addReportDato("Vigencia","fecIniAct4Report");
		 reportDatos.addReportDato("Expet. / Actuación","ordenControl.idCaso");
		 
		 report.getListReport().add(reportDatos);
		
		 
		 ReportTableVO table = new ReportTableVO("detAju");
		table.setReportMetodo("listDetAjuDet");
		
		table.addReportColumn("Período", "plaFueDatCom.periodoAnioView");
		String[] basesImponibles = {"plaFueDatCom.col1BasRos", "plaFueDatCom.col2BasRos","plaFueDatCom.col3BasRos", 
									"plaFueDatCom.col4BasRos", "plaFueDatCom.col5BasRos","plaFueDatCom.col6BasRos",
									"plaFueDatCom.col7BasRos", "plaFueDatCom.col8BasRos","plaFueDatCom.col9BasRos",
									"plaFueDatCom.col10BasRos", "plaFueDatCom.col11BasRos","plaFueDatCom.col12BasRos"};
		table.addReportColumn("Base Imponible", basesImponibles);
		table.addReportColumn("Alícuota", "listAliComFueCol4Report.valorAlicuotaView4Report");
		table.addReportColumn("Tributo", "tributoView");
		table.addReportColumn("Cant. Personal", "cantPersonal");
		table.addReportColumn("Mínimo", "minimoView");
		table.addReportColumn("Tributo Determinado", "triDetView");
		table.addReportColumn("Publicidad", "publicidadView");
		table.addReportColumn("Mesas Y Sillas", "mesasYSillasView");
		table.addReportColumn("Total Tributo", "totalTributoView");
		//table.addReportColumn("Declarado", "pagoView");
		table.addReportColumn("No Pagado", "noPagadoView");
		table.addReportColumn("Pagado", "pagadoView");
		table.addReportColumn("Convenio", "convenioView");
		table.addReportColumn("Retención", "retencionView");
		table.addReportColumn("Ajuste (+)", "ajustePos");
		table.addReportColumn("Ajuste (-)", "ajusteNeg");		
		
		
		
		report.getReportListTable().add(table);
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setPageHeight(21);
		 report.setPageWidth(29.7);
		 report.setReportFormat(format);	
		 report.setReportTitle("Determinación de Deuda Fiscal");     
		 report.setReportBeanName("DetAju");
		 report.setReportFileName(this.getClass().getName());
	
	}
	
	
	// View getters

	// flags getters
	public String getCalcularBasesEnabled(){
		return SiatBussImageModel.hasEnabledFlag(calcularBasesEnabled, EfSecurityConstants.ABM_ALICOMFUECOL, BaseSecurityConstants.AGREGAR);
	}
	
	public String getAgregarPersonalEnabled(){
		return SiatBussImageModel.hasEnabledFlag(agregarPersonalEnabled, EfSecurityConstants.ABM_DETAJU, EfSecurityConstants.MTD_AGREGAR_PERSONAL);
	}
	
	public String getAdicionalPublicidadEnabled(){
		return SiatBussImageModel.hasEnabledFlag(adicionalPublicidadEnabled, EfSecurityConstants.ABM_DETAJU, EfSecurityConstants.MTD_ADICIONAL_PUBL);
	}
	
	public String getAdicionalMesasYSillasEnabled(){
		return SiatBussImageModel.hasEnabledFlag(adicionalMesasYSillasEnabled, EfSecurityConstants.ABM_DETAJU, EfSecurityConstants.MTD_ADICIONAL_MYS);
	}
		
	public String getDetermPorMultaEnabled(){
		return SiatBussImageModel.hasEnabledFlag(determPorMultaEnabled, EfSecurityConstants.ABM_DETAJU, EfSecurityConstants.MTD_DETERM_POR_MULTA);
	}
}
