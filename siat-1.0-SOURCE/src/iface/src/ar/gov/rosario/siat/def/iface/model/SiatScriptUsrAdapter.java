//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.pro.iface.model.ProcesoVO;
import ar.gov.rosario.siat.seg.iface.model.UsuarioSiatVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del SiatScriptUsr
 * 
 * @author tecso
 */
public class SiatScriptUsrAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "siatScriptUsrAdapterVO";
	
    private SiatScriptUsrVO siatScriptUsr = new SiatScriptUsrVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
	private List<ProcesoVO> listProceso = new ArrayList<ProcesoVO>();	
	
	private List<UsuarioSiatVO> listUsuarioSiat = new ArrayList<UsuarioSiatVO>(); 
    
    // Constructores
    public SiatScriptUsrAdapter(){
    	super(DefSecurityConstants.ABM_SIATSCRIPTUSR);
    }
    
    //  Getters y Setters
	public SiatScriptUsrVO getSiatScriptUsr() {
		return siatScriptUsr;
	}

	public void setSiatScriptUsr(SiatScriptUsrVO siatScriptUsrVO) {
		this.siatScriptUsr = siatScriptUsrVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de SiatScriptUsr");     
		 report.setReportBeanName("SiatScriptUsr");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportSiatScriptUsr = new ReportVO();
		 reportSiatScriptUsr.setReportTitle("Datos del SiatScriptUsr");
		 // carga de datos
	     
	     //Código
		 reportSiatScriptUsr.addReportDato("Proceso", "proceso.desProceso");
		 //Descripción
		 reportSiatScriptUsr.addReportDato("Usiario SIAT", "usuarioSiat.usuarioSIAT");
	     
		 report.getListReport().add(reportSiatScriptUsr);
	
	}

	public void setListProceso(List<ProcesoVO> listProceso) {
		this.listProceso = listProceso;
	}

	public List<ProcesoVO> getListProceso() {
		return listProceso;
	}

	public void setListUsuarioSiat(List<UsuarioSiatVO> listUsuarioSiat) {
		this.listUsuarioSiat = listUsuarioSiat;
	}

	public List<UsuarioSiatVO> getListUsuarioSiat() {
		return listUsuarioSiat;
	}
	
	// View getters
}