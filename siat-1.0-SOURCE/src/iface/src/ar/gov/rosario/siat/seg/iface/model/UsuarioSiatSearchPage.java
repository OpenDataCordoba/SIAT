//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.seg.iface.util.SegSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del UsuarioSiat
 * 
 * @author Tecso
 *
 */
public class UsuarioSiatSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "usuarioSiatSearchPageVO";
	
	private UsuarioSiatVO usuarioSiat= new UsuarioSiatVO();

    private List<AreaVO> listArea = new ArrayList<AreaVO>();	

	// Constructores
	public UsuarioSiatSearchPage() {       
       super(SegSecurityConstants.ABM_USUARIOSIAT);        
    }
	
	// Getters y Setters
	public UsuarioSiatVO getUsuarioSiat() {
		return usuarioSiat;
	}
	public void setUsuarioSiat(UsuarioSiatVO usuarioSiat) {
		this.usuarioSiat = usuarioSiat;
	}
	public List<AreaVO> getListArea() {
		return listArea;
	}
	public void setListArea(List<AreaVO> listArea) {
		this.listArea = listArea;
	}
	

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta  de Usuario SIAT");
		 report.setReportBeanName("UsuarioSiat");
		 report.setReportFileName(this.getClass().getName());
		
		
		 // Area
		 String desArea = "";
			
		 AreaVO areaVO = (AreaVO) ModelUtil.getBussImageModelByIdForList(
				 this.getUsuarioSiat().getArea().getId(),
				 this.getListArea());
		 if (areaVO != null){
			 desArea = areaVO.getDesArea();
		 }
	
		
		 report.addReportFiltro("Area", desArea);
		 
		 // Nombre
		 report.addReportFiltro("Nombre", this.getUsuarioSiat().getUsuarioSIAT());
	     
		 // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtUsu = new ReportTableVO("rtUsu");
	     rtUsu.setTitulo("Listado de Usuario SIAT");
	   
	     // carga de columnas
	     rtUsu.addReportColumn("Nombre", "usuarioSIAT");
	     rtUsu.addReportColumn("Area", "area.desArea");
	     rtUsu.addReportColumn("Estado", "estadoView");
	     
	     report.getReportListTable().add(rtUsu);

	    }
}
