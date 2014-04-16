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
 * Adapter del AuxCaja7
 * 
 * @author tecso
 */
public class AuxCaja7Adapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "auxCaja7AdapterVO";
	
    private AuxCaja7VO auxAuxCaja7 = new AuxCaja7VO();
   
    private List<PartidaVO> listPartida = new ArrayList<PartidaVO>();
	
    private List<CeldaVO> opcionActualVencido = new ArrayList<CeldaVO>();
    
    // Constructores
    public AuxCaja7Adapter(){
    	super(BalSecurityConstants.ABM_AUXCAJA7);
    }
    
    //  Getters y Setters
	public AuxCaja7VO getAuxCaja7() {
		return auxAuxCaja7;
	}
	public void setAuxCaja7(AuxCaja7VO auxAuxCaja7VO) {
		this.auxAuxCaja7 = auxAuxCaja7VO;
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
		 report.setReportTitle("Reporte de  AuxCaja7");     
		 report.setReportBeanName("AuxCaja7");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportAuxCaja7 = new ReportVO();
		 reportAuxCaja7.setReportTitle("Datos del  AuxCaja7");
		 // carga de datos
	     
		 reportAuxCaja7.addReportDato("Fecha","fecha");
		 reportAuxCaja7.addReportDato("Partida", "partida.desPartida");
		 reportAuxCaja7.addReportDato("Importe", "importe");
		 reportAuxCaja7.addReportDato("Descripcion", "descripcion");
	     
		 report.getListReport().add(reportAuxCaja7);
	
	}

}
