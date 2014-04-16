//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del MesaEntrada
 * 
 * @author tecso
 */
public class MesaEntradaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "mesaEntradaAdapterVO";
	
    private MesaEntradaVO mesaEntrada = new MesaEntradaVO();
    
    private List<EstadoOrdenVO>  listEstadoOrden = new ArrayList<EstadoOrdenVO>();
    
    // Constructores
    public MesaEntradaAdapter(){
    	super(EfSecurityConstants.ABM_MESAENTRADA);
    }
    
    //  Getters y Setters
	public MesaEntradaVO getMesaEntrada() {
		return mesaEntrada;
	}

	public void setMesaEntrada(MesaEntradaVO mesaEntradaVO) {
		this.mesaEntrada = mesaEntradaVO;
	}
	
	public List<EstadoOrdenVO> getListEstadoOrden() {
		return listEstadoOrden;
	}

	public void setListEstadoOrden(List<EstadoOrdenVO> listEstadoOrden) {
		this.listEstadoOrden = listEstadoOrden;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de MesaEntrada");     
		 report.setReportBeanName("MesaEntrada");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportMesaEntrada = new ReportVO();
		 reportMesaEntrada.setReportTitle("Datos del MesaEntrada");
		 // carga de datos
	     
	     //C�digo
		 reportMesaEntrada.addReportDato("C�digo", "codMesaEntrada");
		 //Descripci�n
		 reportMesaEntrada.addReportDato("Descripci�n", "desMesaEntrada");
	     
		 report.getListReport().add(reportMesaEntrada);
	
	}
	
	// View getters
}
