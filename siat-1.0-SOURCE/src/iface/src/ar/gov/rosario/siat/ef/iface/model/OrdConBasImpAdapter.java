//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del OrdConBasImp
 * 
 * @author tecso
 */
public class OrdConBasImpAdapter extends SiatAdapterModel{
	
	public static final Long ID_TIPO_COEF_STA_FE=1L;
	
	public static final Long ID_TIPO_COEF_ROS=2L;
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "ordConBasImpAdapterVO";
	
    private OrdConBasImpVO ordConBasImp = new OrdConBasImpVO();
        
    private PlaFueDatComVO plaFueDatCom = new PlaFueDatComVO();
    
    private Long idCompFuenteResSelec;
    
    private Long idSelecFuente;
    
    private List<CompFuenteResVO> listCompFuenteRes = new ArrayList<CompFuenteResVO>();
    
    private List<CompFuenteVO> listCompFuente = new ArrayList<CompFuenteVO>();

    private boolean verCamposVigencia = false;
    
    private Double coeficiente;
    
    private String perOrdDesde="";
    
    private String perOrdHasta="";
    
    private List<PeriodoOrdenVO> listPeriodoDesde= new ArrayList<PeriodoOrdenVO>();
    
    private List<PeriodoOrdenVO> listPeriodoHasta= new ArrayList<PeriodoOrdenVO>();
    
    private List<CeldaVO>listTipoCoeficiente=new ArrayList<CeldaVO>();
    
    private List<OrdConCueVO>listOrdConCue = new ArrayList<OrdConCueVO>();
    
    private String valorTipoCoef="";
    // campos para ajustar Periodos
    Long nroColumnaSelec;
    Integer periodoDesde;
    Integer periodoHasta;
    Integer anioDesde;
    Integer anioHasta;
    Double totalAjustar;
    
    // Constructores
    public OrdConBasImpAdapter(){
    	super(EfSecurityConstants.ABM_ORDCONBASIMP);
    }
    
    //  Getters y Setters
	public OrdConBasImpVO getOrdConBasImp() {
		return ordConBasImp;
	}

	public void setOrdConBasImp(OrdConBasImpVO ordConBasImpVO) {
		this.ordConBasImp = ordConBasImpVO;
	}

	public List<CompFuenteResVO> getListCompFuenteRes() {
		return listCompFuenteRes;
	}

	public void setListCompFuenteRes(List<CompFuenteResVO> listCompFuenteResVO) {
		this.listCompFuenteRes = listCompFuenteResVO;
	}

	public Long getIdCompFuenteResSelec() {
		return idCompFuenteResSelec;
	}

	public void setIdCompFuenteResSelec(Long idCompFuenteResSelec) {
		this.idCompFuenteResSelec = idCompFuenteResSelec;
	}

	public List<CompFuenteVO> getListCompFuente() {
		return listCompFuente;
	}

	public void setListCompFuente(List<CompFuenteVO> listCompFuente) {
		this.listCompFuente = listCompFuente;
	}

	public Long getIdSelecFuente() {
		return idSelecFuente;
	}

	public void setIdSelecFuente(Long idSelecFuente) {
		this.idSelecFuente = idSelecFuente;
	}
	
	public PlaFueDatComVO getPlaFueDatCom() {
		return plaFueDatCom;
	}

	public void setPlaFueDatCom(PlaFueDatComVO plaFueDatComVO) {
		this.plaFueDatCom = plaFueDatComVO;
	}
	
	public Integer getPeriodoDesde() {
		return periodoDesde;
	}

	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
	}

	public Integer getAnioDesde() {
		return anioDesde;
	}

	public void setAnioDesde(Integer anioDesde) {
		this.anioDesde = anioDesde;
	}

	public Integer getAnioHasta() {
		return anioHasta;
	}

	public void setAnioHasta(Integer anioHasta) {
		this.anioHasta = anioHasta;
	}

	public Long getNroColumnaSelec() {
		return nroColumnaSelec;
	}

	public void setNroColumnaSelec(Long nroColumnaSelec) {
		this.nroColumnaSelec = nroColumnaSelec;
	}

	public Double getTotalAjustar() {
		return totalAjustar;
	}

	public void setTotalAjustar(Double totalAjustar) {
		this.totalAjustar = totalAjustar;
	}

	public String getName(){
		return NAME;
	}
			
	public List<OrdConCueVO> getListOrdConCue() {
		return listOrdConCue;
	}

	public void setListOrdConCue(List<OrdConCueVO> listOrdConCue) {
		this.listOrdConCue = listOrdConCue;
	}

	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de OrdConBasImp");     
		 report.setReportBeanName("OrdConBasImp");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportOrdConBasImp = new ReportVO();
		 reportOrdConBasImp.setReportTitle("Datos del OrdConBasImp");
		 // carga de datos
	     
	     //C�digo
		 reportOrdConBasImp.addReportDato("C�digo", "codOrdConBasImp");
		 //Descripci�n
		 reportOrdConBasImp.addReportDato("Descripci�n", "desOrdConBasImp");
	     
		 report.getListReport().add(reportOrdConBasImp);
	
	}

	
	// View getters
	public boolean getVerCamposVigencia() {
		return verCamposVigencia;
	}
	
	public void setVerCamposVigencia(boolean verCamposVigencia) {
		this.verCamposVigencia = verCamposVigencia;
	}

	public Double getCoeficiente() {
		return coeficiente;
	}

	public void setCoeficiente(Double coeficiente) {
		this.coeficiente = coeficiente;
	}
	
	public String getCoeficienteView(){
		return (this.coeficiente!=null)?this.coeficiente.toString():""; 
	}


	public String getPerOrdDesde() {
		return perOrdDesde;
	}

	public void setPerOrdDesde(String perOrdDesde) {
		this.perOrdDesde = perOrdDesde;
	}

	public String getPerOrdHasta() {
		return perOrdHasta;
	}

	public void setPerOrdHasta(String perOrdHasta) {
		this.perOrdHasta = perOrdHasta;
	}

	public List<PeriodoOrdenVO> getListPeriodoDesde() {
		return listPeriodoDesde;
	}

	public void setListPeriodoDesde(List<PeriodoOrdenVO> listPeriodoDesde) {
		this.listPeriodoDesde = listPeriodoDesde;
	}

	public List<PeriodoOrdenVO> getListPeriodoHasta() {
		return listPeriodoHasta;
	}

	public void setListPeriodoHasta(List<PeriodoOrdenVO> listPeriodoHasta) {
		this.listPeriodoHasta = listPeriodoHasta;
	}

	public List<CeldaVO> getListTipoCoeficiente() {
		return listTipoCoeficiente;
	}

	public void setListTipoCoeficiente(List<CeldaVO> listTipoCoeficiente) {
		this.listTipoCoeficiente = listTipoCoeficiente;
	}

	public String getValorTipoCoef() {
		return valorTipoCoef;
	}

	public void setValorTipoCoef(String valorTipoCoef) {
		this.valorTipoCoef = valorTipoCoef;
	}
}
