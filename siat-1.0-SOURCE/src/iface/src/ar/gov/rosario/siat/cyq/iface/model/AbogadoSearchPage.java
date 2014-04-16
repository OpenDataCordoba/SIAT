//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Abogado
 * 
 * @author Tecso
 *
 */
public class AbogadoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "abogadoSearchPageVO";
	
	private AbogadoVO abogado= new AbogadoVO();
	
    List <JuzgadoVO> listJuzgado = new ArrayList<JuzgadoVO>();
    
	// Constructores
	public AbogadoSearchPage() {       
       super(CyqSecurityConstants.ABM_ABOGADO);        
    }
	
	// Getters y Setters
	public AbogadoVO getAbogado() {
		return abogado;
	}
	public void setAbogado(AbogadoVO abogado) {
		this.abogado = abogado;
	}

	public List<JuzgadoVO> getListJuzgado() {
		return listJuzgado;
	}

	public void setListJuzgado(List<JuzgadoVO> listJuzgado) {
		this.listJuzgado = listJuzgado;
	}
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Abogados");
		 report.setReportBeanName("Abogado");
		 report.setReportFileName(this.getClass().getName());
		 
	
	
		 // carga de filtros
		 report.addReportFiltro("Desc.Abogado", this.getAbogado().getDescripcion());
	
		 // carga de filtros
		 report.addReportFiltro("Domicilio", this.getAbogado().getDomicilio());
	
		 // Juzgado
		 String desJuzgado = "";
			
		 JuzgadoVO juzgadoVO = (JuzgadoVO) ModelUtil.getBussImageModelByIdForList(
				 this.getAbogado().getJuzgado().getId(), 
				 this.getListJuzgado());
		 if (juzgadoVO != null){
			 desJuzgado = juzgadoVO.getDesJuzgado();
		 }
		 
		// carga de filtros
		 report.addReportFiltro("Desc.Juzgado", desJuzgado);
		 
		 // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtJuz = new ReportTableVO("rtJuz");
	     rtJuz.setTitulo("Listado de Juzgados");
	   
	     // carga de columnas
	    
	     rtJuz.addReportColumn("Desc.Abogado", "descripcion");
	     rtJuz.addReportColumn("Domicilio", "domicilio");
	     rtJuz.addReportColumn("Telefono", "telefono");
	     rtJuz.addReportColumn("Juzgado", "juzgado.desJuzgado");
	     rtJuz.addReportColumn("Estado", "estadoView");
	     report.getReportListTable().add(rtJuz);

	    }
	
}
