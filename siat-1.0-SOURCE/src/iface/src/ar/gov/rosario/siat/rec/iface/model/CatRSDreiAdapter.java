//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del CatRSDrei
 * 
 * @author tecso
 */
public class CatRSDreiAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "catRSDreiAdapterVO";
	
    private CatRSDreiVO catRSDrei = new CatRSDreiVO();
    
    private List<SiNo> listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public CatRSDreiAdapter(){
    	super(RecSecurityConstants.ABM_CATEGORIARS);
    }
    
    //  Getters y Setters
	public CatRSDreiVO getCatRSDrei() {
		return catRSDrei;
	}

	public void setCatRSDrei(CatRSDreiVO catRSDreiVO) {
		this.catRSDrei = catRSDreiVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de CatRSDrei");     
		 report.setReportBeanName("CatRSDrei");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportCatRSDrei = new ReportVO();
		 reportCatRSDrei.setReportTitle("Datos de Categoría");
		 // carga de datos
	     
	     //Código
		 reportCatRSDrei.addReportDato("Número de Categoria", "nroCategoria");
		 
		 //Descripción
		 reportCatRSDrei.addReportDato("Ingresos Brutos anuales","ingBruAnu");
		 reportCatRSDrei.addReportDato("Superficie","superficie");
		 reportCatRSDrei.addReportDato("Importe","importe");
		 reportCatRSDrei.addReportDato("Cantidad de Empleados","cantEmpleados");
		 reportCatRSDrei.addReportDato("Fecha Desde","fechaDesde");
		 reportCatRSDrei.addReportDato("Fecha Hasta","fechaHasta");		 
	     
		 report.getListReport().add(reportCatRSDrei);
	
	}
	
	// View getters
}