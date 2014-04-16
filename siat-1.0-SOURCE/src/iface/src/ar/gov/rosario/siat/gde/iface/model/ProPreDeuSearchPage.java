//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del ProPreDeu
 * 
 * @author Tecso
 *
 */
public class ProPreDeuSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "proPreDeuSearchPageVO";
	
	private ProPreDeuVO proPreDeu= new ProPreDeuVO();
	private Date fechaDesde;
	private Date fechaHasta;
	private List<ServicioBancoVO> listServicioBanco = new ArrayList<ServicioBancoVO>();
	private List<EstadoCorridaVO> listEstadoCorrida = new ArrayList<EstadoCorridaVO>();
	
	// View 
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	
	// Constructores
	public ProPreDeuSearchPage() {       
       super(GdeSecurityConstants.ABM_PROPREDEU);        
    }
	
	// Getters y Setters
	public ProPreDeuVO getProPreDeu() {
		return proPreDeu;
	}
	public void setProPreDeu(ProPreDeuVO proPreDeu) {
		this.proPreDeu = proPreDeu;
	}
	
	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public List<ServicioBancoVO> getListServicioBanco() {
		return listServicioBanco;
	}

	public void setListServicioBanco(List<ServicioBancoVO> listServicioBanco) {
		this.listServicioBanco = listServicioBanco;
	}

	public List<EstadoCorridaVO> getListEstadoCorrida() {
		return listEstadoCorrida;
	}

	public void setListEstadoCorrida(List<EstadoCorridaVO> listEstadoCorrida) {
		this.listEstadoCorrida = listEstadoCorrida;
	}

	// View getters
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}

	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}

	public String getFechaHastaView() {
		return fechaHastaView;
	}

	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	
	public String getName(){
		return NAME;
	}

	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listado de Procesos de Prescripci\u00F3n de Deuda");
		report.setReportBeanName("ProPreDeu");
		report.setReportFileName(this.getClass().getName());

		// Servicio Banco
        String desServicioBanco = "";
		ServicioBancoVO servicioBancoVO = (ServicioBancoVO) ModelUtil.getBussImageModelByIdForList(
				this.getProPreDeu().getServicioBanco().getId(),
				this.getListServicioBanco());
		if (servicioBancoVO != null){
			desServicioBanco = servicioBancoVO.getDesServicioBanco();
		}
		report.addReportFiltro("Servicio Banco", desServicioBanco);

		// Estado Corrida
        String desEstadoCorrida = "";
		EstadoCorridaVO estadoCorridaVO = (EstadoCorridaVO) ModelUtil.getBussImageModelByIdForList(
				this.getProPreDeu().getCorrida().getEstadoCorrida().getId(),
				this.getListEstadoCorrida());
		if (estadoCorridaVO != null){
			desEstadoCorrida = estadoCorridaVO.getDesEstadoCorrida();
		}
		
		// Estado
		report.addReportFiltro("Estado", desEstadoCorrida);
		
		// Fecha Desde
		report.addReportFiltro("Fecha Desde", this.getFechaDesdeView());
		
		// Fecha Hasta
		report.addReportFiltro("Fecha Hasta", this.getFechaHastaView());
		
		// Order by
		report.setReportOrderBy("fechaUltMdf desc");
		
		ReportTableVO rtProPreDeu = new ReportTableVO("rtProPreDeu");
		rtProPreDeu.setTitulo("B\u00FAsqueda de Procesos de Prescripci\u00F3n de Deuda");

		// Columna Servicio Banco
		rtProPreDeu.addReportColumn("Servicio Banco","servicioBanco.desServicioBanco");

		// Columna Fecha Tope
		rtProPreDeu.addReportColumn("Fecha Tope","fechaTope");
		
		// Columna Estado
		rtProPreDeu.addReportColumn("Estado", "corrida.estadoCorrida.desEstadoCorrida");

		report.getReportListTable().add(rtProPreDeu);

	}

	public String getAdministrarProcesoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PROPREDEU, GdeSecurityConstants.ABM_PROPREDEU_ADM_PROCESO);
	}

}
