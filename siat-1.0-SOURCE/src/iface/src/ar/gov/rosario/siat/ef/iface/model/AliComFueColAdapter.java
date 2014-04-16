//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.RecConADecVO;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del AliComFueCol
 * 
 * @author tecso
 */
public class AliComFueColAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "aliComFueColAdapterVO";
	
	private DetAjuVO detAju;
	
    private AliComFueColVO aliComFueCol = new AliComFueColVO();
    
    private CompFuenteColVO	compFuenteCol = new CompFuenteColVO();
    
    private List<CompFuenteVO> listCompFuente = new ArrayList<CompFuenteVO>();

	private boolean agregarAliComFueColEnabled=true;
	
	private List<RecConADecVO>listTipoUnidad = new ArrayList<RecConADecVO>();
    
    // Constructores
    public AliComFueColAdapter(){
    	super(EfSecurityConstants.ABM_ALICOMFUECOL);
    }
    
    //  Getters y Setters   
	public DetAjuVO getDetAju() {
		return detAju;
	}

	public void setDetAju(DetAjuVO detAju) {
		this.detAju = detAju;
	}

	public AliComFueColVO getAliComFueCol() {
		return aliComFueCol;
	}

	public void setAliComFueCol(AliComFueColVO aliComFueColVO) {
		this.aliComFueCol = aliComFueColVO;
	}
	
	public List<CompFuenteVO> getListCompFuente() {
		return listCompFuente;
	}

	public void setListCompFuente(List<CompFuenteVO> listCompFuente) {
		this.listCompFuente = listCompFuente;
	}

	public CompFuenteColVO getCompFuenteCol() {
		return compFuenteCol;
	}

	public void setCompFuenteCol(CompFuenteColVO compFuenteCol) {
		this.compFuenteCol = compFuenteCol;
	}

	public List<RecConADecVO> getListTipoUnidad() {
		return listTipoUnidad;
	}

	public void setListTipoUnidad(List<RecConADecVO> listTipoUnidad) {
		this.listTipoUnidad = listTipoUnidad;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de AliComFueCol");     
		 report.setReportBeanName("AliComFueCol");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportAliComFueCol = new ReportVO();
		 reportAliComFueCol.setReportTitle("Datos del AliComFueCol");
		 // carga de datos
	     
	     //C�digo
		 reportAliComFueCol.addReportDato("C�digo", "codAliComFueCol");
		 //Descripci�n
		 reportAliComFueCol.addReportDato("Descripci�n", "desAliComFueCol");
	     
		 report.getListReport().add(reportAliComFueCol);
	
	}
	
	// View getters
	
	// flags getters
	public String getAgregarAliComFueColEnabled(){
		return SiatBussImageModel.hasEnabledFlag(agregarAliComFueColEnabled, EfSecurityConstants.ABM_ALICOMFUECOL, BaseSecurityConstants.AGREGAR);
	}
}
