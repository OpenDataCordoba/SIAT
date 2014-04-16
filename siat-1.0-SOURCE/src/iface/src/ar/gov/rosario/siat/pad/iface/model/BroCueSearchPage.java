//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Search Page de BroCue
 * @author tecso
 *
 */
public class BroCueSearchPage extends SiatPageModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "broCueSearchPageVO";
	
	private BroCueVO broCue = new BroCueVO();
	
	private List<BroCueVO> listBroCue = new ArrayList<BroCueVO>();
	
	private int paramTipoBroche = 0;
	
	public BroCueSearchPage(){
		super(PadSecurityConstants.ABM_BROCUE);
	}

	// Getters y Setters
	
	public BroCueVO getBroCue() {
		return broCue;
	}
	public void setBroCue(BroCueVO broCue) {
		this.broCue = broCue;
	}
	public List<BroCueVO> getListBroCue() {
		return listBroCue;
	}
	public void setListBroCue(List<BroCueVO> listBroCue) {
		this.listBroCue = listBroCue;
	}
	public int getParamTipoBroche() {
		return paramTipoBroche;
	}
	public void setParamTipoBroche(int paramTipoBroche) {
		this.paramTipoBroche = paramTipoBroche;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva	
		 report.setReportFormat(format);	
		 report.setReportTitle("Asignación de Broche a Cuentas");
		 report.setReportBeanName("broCue");
		 report.setReportFileName(this.getClass().getName());
				
		 ReportVO reportDatosBro = new ReportVO();
		 reportDatosBro.setReportTitle("Datos del Broche");
	   
		 // carga de columnas
		 report.addReportFiltro("Recurso", this.getBroCue().getBroche().getRecurso().getDesRecurso());	 
		 report.addReportFiltro("Tipo de Broche", this.getBroCue().getBroche().getTipoBroche().getDesTipoBroche());
		 report.addReportFiltro("Número", this.getBroCue().getBroche().getIdView());
		 report.addReportFiltro("Titular (Descripción)", this.getBroCue().getBroche().getDesBroche());
		 report.addReportFiltro("Domicilio de Envio", this.getBroCue().getBroche().getStrDomicilioEnvio());
		 report.addReportFiltro("Teléfono",  this.getBroCue().getBroche().getTelefono());
		 report.addReportFiltro("Repartidor", this.getBroCue().getBroche().getRepartidor().getDesRepartidor());
		
	     ReportTableVO rtCuenta = new ReportTableVO("rtCuenta");
	     rtCuenta.setTitulo("Listado de Asignación de Broche a Cuenta");
	     
		
	     rtCuenta.addReportColumn("Fecha Alta", "fechaAlta");
	     
	     rtCuenta.addReportColumn("fecha Baja", "fechaBaja");
	  
	     rtCuenta.addReportColumn("Cuenta", "cuenta.numeroCuenta"); 
	     
	     rtCuenta.addReportColumn("Domicilio de Envío", "cuenta.domicilioEnvio.represent");
	     
	     report.getReportListTable().add(rtCuenta);
	}
	
}
