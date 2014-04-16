//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Indet
 * 
 * @author Tecso
 *
 */
public class IndetSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "indetSearchPageVO";
	
	private IndetVO indet= new IndetVO();
	
	private Boolean reingresarEnabled    = true;
	private Boolean desgloceEnabled    = true;
	private Boolean generarSaldoAFavorEnabled    = true;
	
	private String[] listIndetVOSelected;	
	
	// Constructores
	public IndetSearchPage() {       
       super(BalSecurityConstants.ABM_INDET);
    }
	
	// Getters y Setters
	public IndetVO getIndet() {
		return indet;
	}
	public void setIndet(IndetVO indet) {
		this.indet = indet;
	}           
    public String[] getListIndetVOSelected() {
		return listIndetVOSelected;
	}
	public void setListIndetVOSelected(String[] listIndetVOSelected) {
		this.listIndetVOSelected = listIndetVOSelected;
	}

	public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de Indeterminados");
		report.setReportBeanName("Indet");
		report.setReportFileName(this.getClass().getName());

		ReportTableVO rtIndet = new ReportTableVO("rtIndet");
		rtIndet.setTitulo("B\u00FAsqueda de Indet");
		
		 
	    report.getReportListTable().add(rtIndet);

	}
	// View getters

	
	public Boolean getReingresarBussEnabled() {
		return reingresarEnabled;
	}
	public void setReingresarBussEnabled(Boolean reingresarEnabled) {
		this.reingresarEnabled = reingresarEnabled;
	}
	
	public String getReingresarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getReingresarBussEnabled(), 
				BalSecurityConstants.ABM_INDET, BalSecurityConstants.ABM_INDET_REINGRESAR);
	}
	
	public Boolean getDesgloceBussEnabled() {
		return desgloceEnabled;
	}
	public void setDesgloceBussEnabled(Boolean desgloceEnabled) {
		this.desgloceEnabled = desgloceEnabled;
	}
	
	public String getDesgloceEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getDesgloceBussEnabled(), 
				BalSecurityConstants.ABM_INDET, BalSecurityConstants.ABM_INDET_DESGLOCE);
	}
	public Boolean getGenerarSaldoAFavorBussEnabled() {
		return generarSaldoAFavorEnabled;
	}
	public void setGenerarSaldoAFavorBussEnabled(Boolean generarSaldoAFavorEnabled) {
		this.generarSaldoAFavorEnabled = generarSaldoAFavorEnabled;
	}
	public String getGenerarSaldoAFavorEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getGenerarSaldoAFavorBussEnabled(), 
				BalSecurityConstants.ABM_INDET, BalSecurityConstants.GENERAR_SALDO);
	}

	
	/**
	 *  Valida que se haya cargado algun filtro
	 *  
	 * @return
	 */
	public boolean existeAlgunFiltro(){	
		if(this.getIndet().getFechaPago() != null){
			return true;
		}
		if(this.getIndet().getFechaBalance() != null){
			return true;
		}
		if(!StringUtil.isNullOrEmpty(this.getIndet().getSistema())){
			return true;
		}
		if(this.getIndet().getCodIndet() != null){
			return true;
		}
		if(this.getIndet().getReciboTr() != null){
			return true;
		}
		if(this.getIndet().getCaja() != null){
			return true;
		}
		if(this.getIndet().getPaquete() != null){
			return true;
		}
		if(this.getIndet().getImporteCobrado() != null){
			return true;
		}
		return false;
	}
}
