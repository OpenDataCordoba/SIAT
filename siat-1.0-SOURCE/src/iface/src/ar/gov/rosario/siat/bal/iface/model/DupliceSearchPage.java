//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Duplice
 * 
 * @author Tecso
 *
 */
public class DupliceSearchPage extends SiatPageModel {


	private static final long serialVersionUID = 1L;

	public static final String NAME = "dupliceSearchPageVO";
	
	private IndetVO duplice= new IndetVO();
	
	private Boolean imputarEnabled    = true;
	private Boolean generarSaldoAFavorEnabled    = true;
	
	// Constructores
	public DupliceSearchPage() {       
       super(BalSecurityConstants.ABM_DUPLICE);
    }
	
	// Getters y Setters
	public IndetVO getDuplice() {
		return duplice;
	}
	public void setDuplice(IndetVO duplice) {
		this.duplice = duplice;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de Duplices");
		report.setReportBeanName("Indet");
		report.setReportFileName(this.getClass().getName());

        /* Codigo de ejemplo para mostrar filtros de Combos en los imprimir
		String desRecurso = "";

		RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				this.getReclamo().getRecurso().getId(),
				this.getListRecurso());
		if (recursoVO != null){
			desRecurso = recursoVO.getDesRecurso();
		}
		report.addReportFiltro("Recurso", desRecurso);*/

		//Codigo
		//report.addReportFiltro("Código", this.getIndet().getCodIndet());
		//Descripción
		//report.addReportFiltro("Descripción", this.getIndet().getDesIndet());
		

		ReportTableVO rtIndet = new ReportTableVO("rtIndet");
		rtIndet.setTitulo("B\u00FAsqueda de Duplice");

		// carga de columnas
		//rtIndet.addReportColumn("Código","codIndet");
		//rtIndet.addReportColumn("Descripción", "desIndet");
		
		 
	    report.getReportListTable().add(rtIndet);

	}

	// View getters

	public Boolean getImputarBussEnabled() {
		return imputarEnabled;
	}
	public void setImputarBussEnabled(Boolean imputarEnabled) {
		this.imputarEnabled = imputarEnabled;
	}
	public String getImputarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getImputarBussEnabled(), 
				BalSecurityConstants.ABM_DUPLICE, BalSecurityConstants.IMPUTAR);
	}

	public Boolean getGenerarSaldoAFavorBussEnabled() {
		return generarSaldoAFavorEnabled;
	}
	public void setGenerarSaldoAFavorBussEnabled(Boolean generarSaldoAFavorEnabled) {
		this.generarSaldoAFavorEnabled = generarSaldoAFavorEnabled;
	}
	public String getGenerarSaldoAFavorEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getGenerarSaldoAFavorBussEnabled(), 
				BalSecurityConstants.ABM_DUPLICE, BalSecurityConstants.GENERAR_SALDO);
	}

}
