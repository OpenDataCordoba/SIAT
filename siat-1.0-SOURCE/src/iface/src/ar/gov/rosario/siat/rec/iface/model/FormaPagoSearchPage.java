//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del FormaPago
 * 
 * @author Tecso
 *
 */
public class FormaPagoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "formaPagoSearchPageVO";
	
	private FormaPagoVO formaPago= new FormaPagoVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	// Constructores
	public FormaPagoSearchPage() {       
       super(RecSecurityConstants.ABM_FORMAPAGO);        
    }
	
	// Getters y Setters
	public FormaPagoVO getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(FormaPagoVO formaPago) {
		this.formaPago = formaPago;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	
	// View getters
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Consulta de Forma Pago");
		report.setReportBeanName("formaPago");
		report.setReportFileName(this.getClass().getName());
		 
		// recurso
		String desRecurso = "";
			
		RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
			this.getFormaPago().getRecurso().getId(),
			 this.getListRecurso());
		if (recursoVO != null){
			desRecurso = recursoVO.getDesRecurso();
		}
		
		report.addReportFiltro("Recurso", desRecurso);
		 
		// Order by
		report.setReportOrderBy("formaPago.recurso.desRecurso, formaPago.cantCuotas");
	     
	    ReportTableVO rtForPag = new ReportTableVO("rtForPag");
	    rtForPag.setTitulo("Listado de Forma de Pago");
	   
	    // carga de columnas
	    rtForPag.addReportColumn("Recurso", "recurso.desRecurso");
	    rtForPag.addReportColumn("Cuotas Fijas", "esCantCuotasFijasForReport");
	    rtForPag.addReportColumn("Cantidad de Cuotas", "cantCuotas");
	    rtForPag.addReportColumn("Es Especial", "esEspecialForReport");
	    rtForPag.addReportColumn("Estado", "estadoView");
	    report.getReportListTable().add(rtForPag);
	}

}