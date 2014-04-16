//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del DeuCueGesJud.<br>
 * Deudas de Cuentas en Juicio
 * @author Tecso
 *
 */
public class DeuCueGesJudSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "deuCueGesJudSearchPageVO";
	
	private CuentaVO cuenta = new CuentaVO(); 
	
	private List<GesJudDeuVO> listGesJudDeu = new ArrayList<GesJudDeuVO>();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	// Constructores
	public DeuCueGesJudSearchPage() {       
       super(GdeSecurityConstants.CONSULTAR_DEUCUEGESJUD);        
    }
	
	// Getters y Setters
	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public List<GesJudDeuVO> getListGesJudDeu() {
		return listGesJudDeu;
	}

	public void setListGesJudDeu(List<GesJudDeuVO> listGesJudDeu) {
		this.listGesJudDeu = listGesJudDeu;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public int getCantResult(){
		return getListResult().size();
	}
	// View getters
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 	 
		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Consulta de deudas de Cuenta en Gestión Judicial");
		report.setReportBeanName("GesJudDeu");
		report.setReportFileName(this.getClass().getName());		 	
	
		 // carga de filtros
		  // recurso
		 String desRecurso = "";
			
		 RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				 this.getCuenta().getRecurso().getId(),
				 this.getListRecurso());
		 if (recursoVO != null){
			 desRecurso = recursoVO.getDesRecurso();
		 }
		 report.addReportFiltro("Recurso", desRecurso);
	
		 report.addReportFiltro("Cuenta", this.getCuenta().getNumeroCuenta());
	
		 // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtCta = new ReportTableVO("rtCta");
	     rtCta.setTitulo("Listado de deudas de Cuenta en Gestión Judicial");
	   
	     // carga de columnas
	     rtCta.addReportColumn("Gestión Judicial ", "gesJud.desGesJud");
	     rtCta.addReportColumn("Procurador", "gesJud.procurador.descripcion");
	     rtCta.addReportColumn("Juzgado", "gesJud.juzgado");
	     rtCta.addReportColumn("Caso", "gesJud.casoView");
	     rtCta.addReportColumn("Observaciones", "gesJud.observacion");
	     rtCta.addReportColumn("Deuda", "deuda.strPeriodo");
	     rtCta.addReportColumn("Constancia", "constanciaDeu.strNumeroAnio");
	     rtCta.addReportColumn("Estado", "deuda.estadoDeuda.desEstadoDeuda");
	     report.getReportListTable().add(rtCta);
	     
    }
	
}
