//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del Caja7
 * 
 * @author tecso
 */
public class Caja7Adapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "caja7AdapterVO";
	
    private Caja7VO caja7 = new Caja7VO();
   
    private List<PartidaVO> listPartida = new ArrayList<PartidaVO>();
	
    private List<CeldaVO> opcionActualVencido = new ArrayList<CeldaVO>();
    
    // Constructores
    public Caja7Adapter(){
    	super(BalSecurityConstants.ABM_CAJA7);
    }
    
    //  Getters y Setters
	public Caja7VO getCaja7() {
		return caja7;
	}
	public void setCaja7(Caja7VO caja7VO) {
		this.caja7 = caja7VO;
	}    
    public List<PartidaVO> getListPartida() {
		return listPartida;
	}
	public void setListPartida(List<PartidaVO> listPartida) {
		this.listPartida = listPartida;
	}
	public List<CeldaVO> getOpcionActualVencido() {
		return opcionActualVencido;
	}
	public void setOpcionActualVencido(List<CeldaVO> opcionActualVencido) {
		this.opcionActualVencido = opcionActualVencido;
	}

	public String getName(){
		return NAME;
	}
	
		
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de  Caja7");     
		 report.setReportBeanName("Caja7");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportCaja7 = new ReportVO();
		 reportCaja7.setReportTitle("Datos del  Caja7");
		 // carga de datos
	     
		 reportCaja7.addReportDato("Fecha","fecha");
		 reportCaja7.addReportDato("Partida", "partida.desPartida");
		 reportCaja7.addReportDato("Importe", "importe");
		 reportCaja7.addReportDato("Descripcion", "descripcion");
	     
		 report.getListReport().add(reportCaja7);
	
	}

}
