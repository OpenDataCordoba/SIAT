//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del ValoresCargados
 * 
 * @author tecso
 */
public class ValoresCargadosAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "valoresCargadosAdapterVO";
	
    private ValoresCargadosVO valoresCargados = new ValoresCargadosVO();
    
    private List<PartidaVO> listPartida = new ArrayList<PartidaVO>();
    
    // Constructores
    public ValoresCargadosAdapter(){
    	super(EspSecurityConstants.ABM_VALORESCARGADOS);
    }
    
    //  Getters y Setters
	public ValoresCargadosVO getValoresCargados() {
		return valoresCargados;
	}

	public void setValoresCargados(ValoresCargadosVO valoresCargadosVO) {
		this.valoresCargados = valoresCargadosVO;
	}

	public String getName(){
		return NAME;
	}
			
	
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de ValoresCargados");     
		 report.setReportBeanName("ValoresCargados");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportValoresCargados = new ReportVO();
		 reportValoresCargados.setReportTitle("Datos del ValoresCargados");
		 // carga de datos
	     
	     //Descripción
		 reportValoresCargados.addReportDato("Descripción", "descripcion");
	     
		 report.getListReport().add(reportValoresCargados);
	
	}

	public List<PartidaVO> getListPartida() {
		return listPartida;
	}

	public void setListPartida(List<PartidaVO> listPartida) {
		this.listPartida = listPartida;
	}
	
	// View getters
}