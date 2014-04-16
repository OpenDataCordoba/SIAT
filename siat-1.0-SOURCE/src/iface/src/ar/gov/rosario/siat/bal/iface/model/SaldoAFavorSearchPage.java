//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del SaldoAFavor
 * 
 * @author Tecso
 *
 */
public class SaldoAFavorSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "saldoAFavorSearchPageVO";
	
	private SaldoAFavorVO saldoAFavor= new SaldoAFavorVO();
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	// Constructores
	public SaldoAFavorSearchPage() {       
       super(BalSecurityConstants.ABM_SALDOAFAVOR);        
    }
	
	// Getters y Setters
	public SaldoAFavorVO getSaldoAFavor() {
		return saldoAFavor;
	}
	public void setSaldoAFavor(SaldoAFavorVO saldoAFavor) {
		this.saldoAFavor = saldoAFavor;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public String getName(){
		return NAME;
	}

	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de Saldo a Favor");
		report.setReportBeanName("SaldoAFavor");
		report.setReportFileName(this.getClass().getName());

		String desRecurso = "";
		RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				this.getSaldoAFavor().getCuenta().getRecurso().getId(),
				this.getListRecurso());
		if (recursoVO != null){
			desRecurso = recursoVO.getDesRecurso();
		}
		report.addReportFiltro("Recurso", desRecurso);

		//Cuenta
		report.addReportFiltro("Cuenta", this.getSaldoAFavor().getCuenta().getNumeroCuenta());

		ReportTableVO rtSaldo = new ReportTableVO("rtSaldo");
		rtSaldo.setTitulo("B\u00FAsqueda de Saldo a Favor");

		// carga de columnas
		rtSaldo.addReportColumn("Fecha Generación","fechaGeneracion");
		rtSaldo.addReportColumn("Tipo Origen ", "tipoOrigen.desTipoOrigen");
		rtSaldo.addReportColumn("Importe", "importe");
		 
	    report.getReportListTable().add(rtSaldo);

	}}
