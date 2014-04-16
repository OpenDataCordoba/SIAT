//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Search Page de Broche
 * @author tecso
 *
 */
public class BrocheSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "brocheSearchPageVO";
	
	private Log log = LogFactory.getLog(BrocheSearchPage.class);	

	// Si hay valor para este parametro entonces el combo de recurso se pone con este valor y en readonly
	public static final String PARAM_RECURSO_READONLY = "paramRecursoReadOnly";

	private RecursoVO paramRecursoReadOnly = null;
	
	private BrocheVO broche = new BrocheVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<TipoBrocheVO> listTipoBroche = new ArrayList<TipoBrocheVO>();
	
	private List<BrocheVO> listBroche = new ArrayList<BrocheVO>();
	
	private Boolean asignarCuentaBussEnabled      = true;
	
	public BrocheSearchPage(){
		super(PadSecurityConstants.ABM_BROCHE);
	}

	// Getters y Setters

	public BrocheVO getBroche() {
		return broche;
	}
	public void setBroche(BrocheVO broche) {
		this.broche = broche;
	}
	public List<BrocheVO> getListBroche() {
		return listBroche;
	}
	public void setListBroche(List<BrocheVO> listBroche) {
		this.listBroche = listBroche;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public List<TipoBrocheVO> getListTipoBroche() {
		return listTipoBroche;
	}
	public void setListTipoBroche(List<TipoBrocheVO> listTipoBroche) {
		this.listTipoBroche = listTipoBroche;
	}
	public RecursoVO getParamRecursoReadOnly() {
		return paramRecursoReadOnly;
	}
	public void setParamRecursoReadOnly(RecursoVO paramRecursoReadOnly) {
		this.paramRecursoReadOnly = paramRecursoReadOnly;
	}

	// Flags Seguridad
	public Boolean getAsignarCuentaBussEnabled() {
		return asignarCuentaBussEnabled;
	}

	public void setAsignarCuentaBussEnabled(Boolean asignarCuentaBussEnabled) {
		this.asignarCuentaBussEnabled = asignarCuentaBussEnabled;
	}
	
	public String getAsignarCuentaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAsignarCuentaBussEnabled(), 
				PadSecurityConstants.ABM_BROCHE, PadSecurityConstants.ABM_BROCHE_ADM_BROCHE_CUENTA);
	}
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		try {
			ReportVO report = this.getReport(); // no instanciar una nueva
			 report.setReportFormat(format);	
			 report.setReportTitle("Consulta de Broches");
			 report.setReportBeanName("broche");
			 report.setReportFileName(this.getClass().getName());
			 
			 // recurso
			 String desRecurso = "";
				
			 RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
					 this.getBroche().getRecurso().getId(),
					 this.getListRecurso());
			 if (recursoVO != null){
				 desRecurso = recursoVO.getDesRecurso();
			 }
			 report.addReportFiltro("Recurso", desRecurso);
			 
			  // Tipo de Broche
			 String desbroche = "";
				
			 TipoBrocheVO tipoBrocheVO = (TipoBrocheVO) ModelUtil.getBussImageModelByIdForList(
					 this.getBroche().getTipoBroche().getId(),
					 this.getListTipoBroche());
			 if (tipoBrocheVO != null){
				 desbroche = tipoBrocheVO.getDesTipoBroche();
			 }
			 report.addReportFiltro("Tipo de Broche", desbroche);
			 
			 //Número
		      
			 report.addReportFiltro("Número", this.getBroche().getIdView());
		     
		     ReportTableVO rtBr = new ReportTableVO("rtBr");
		     rtBr.setTitulo("Listado de Broches");
		   
		     // carga de columnas
		     rtBr.addReportColumn("Recurso", "recurso.desRecurso");
		     rtBr.addReportColumn("Tipo de Broche", "tipoBroche.desTipoBroche");
		     rtBr.addReportColumn("Número", "id");
		     rtBr.addReportColumn("Descripción", "desBroche");
		     rtBr.addReportColumn("Estado", "estadoView");
		     report.getReportListTable().add(rtBr);
	
		    }
		
		catch (Exception e) {
			log.error("Error no Manejado en baseException:", e);
		}
	}
}
