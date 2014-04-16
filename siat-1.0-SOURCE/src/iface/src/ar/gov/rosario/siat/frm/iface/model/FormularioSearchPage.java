//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.DesImpVO;
import ar.gov.rosario.siat.frm.iface.util.FrmSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Formulario
 * 
 * @author Tecso
 *
 */
public class FormularioSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "formularioSearchPageVO";
	
	private FormularioVO formulario= new FormularioVO();
	
	private List<DesImpVO> listDesImp = new ArrayList<DesImpVO>();
	
	// Constructores
	public FormularioSearchPage() {       
       super(FrmSecurityConstants.ABM_FORMULARIO);        
    }
	
	// Getters y Setters
	public FormularioVO getFormulario() {
		return formulario;
	}
	public void setFormulario(FormularioVO formulario) {
		this.formulario = formulario;
	}

	public List<DesImpVO> getListDesImp() {
		return listDesImp;
	}

	public void setListDesImp(List<DesImpVO> listDesImp) {
		this.listDesImp = listDesImp;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Formulario");
		 report.setReportBeanName("formulario");
		 report.setReportFileName(this.getClass().getName());
		 
		//Destino Impresión
		 report.addReportFiltro("Código", this.getFormulario().getCodFormulario());
		 
		//Descripción
		 report.addReportFiltro("Descripción", this.getFormulario().getDesFormulario());
		 
		 //Destino Impresión
		 String desImp= "";
			
		 DesImpVO desImpVO = (DesImpVO) ModelUtil.getBussImageModelByIdForList(
				 this.getFormulario().getDesImp().getId(),
				 this.getListDesImp());
		 if (desImpVO != null){
			 desImp = desImpVO.getDesDesImp();
		 }
		 report.addReportFiltro("Destino Impresión", desImp);
		 
		 
	     // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtFor = new ReportTableVO("rtFor");
	     rtFor.setTitulo("Listado de Formulario");
	   
	     // carga de columnas
	     rtFor.addReportColumn("Código", "codFormulario");
	     rtFor.addReportColumn("Descripción", "desFormulario");
	     rtFor.addReportColumn("Destino Impresión", "desImp.desDesImp");
	     rtFor.addReportColumn("Estado", "estadoView");
	     report.getReportListTable().add(rtFor);

	    }
}
