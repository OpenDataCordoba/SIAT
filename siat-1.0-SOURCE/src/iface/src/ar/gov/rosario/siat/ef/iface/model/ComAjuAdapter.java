//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del ComAju
 * 
 * @author tecso
 */
public class ComAjuAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "comAjuAdapterVO";

	public static final String ENC_NAME = "encComAjuAdapterVO";
	
    private ComAjuVO comAju = new ComAjuVO();
    
    private List<DetAjuVO> listDetAju = new ArrayList<DetAjuVO>();
    
    // Constructores
    public ComAjuAdapter(){
    	super(EfSecurityConstants.ABM_COMAJU);
    	ACCION_MODIFICAR_ENCABEZADO = EfSecurityConstants.ABM_COMAJU_ENC;
    }
    
    //  Getters y Setters
	public ComAjuVO getComAju() {
		return comAju;
	}

	public void setComAju(ComAjuVO comAjuVO) {
		this.comAju = comAjuVO;
	}

	public List<DetAjuVO> getListDetAju() {
		return listDetAju;
	}

	public void setListDetAju(List<DetAjuVO> listDetAju) {
		this.listDetAju = listDetAju;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Compensación");     
		 report.setReportBeanName("ComAju");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportComAju = new ReportVO();
		 reportComAju.setReportTitle("Datos de la Compensaciones");
		 // carga de datos
	     
	     //Fecha Aplicación
		 reportComAju.addReportDato("Fecha Aplicación", "fechaAplicacion");
		 //Cuenta
		 reportComAju.addReportDato("Cuenta", "detAju.ordConCue.cuenta.numeroCuenta");
		 //Saldo a favor original
		 reportComAju.addReportDato("Saldo a favor original", "saldoFavorOriginal");
		//Total compensado
		 reportComAju.addReportDato("Total compensado", "totalCompensado");
	     
		 report.getListReport().add(reportComAju);
		 
		 // Lista de Compensaciones
		 ReportTableVO rtComAjuDet = new ReportTableVO("ComAjuDet");
    	 rtComAjuDet.setTitulo("Lista de Compensaciones");
    	 rtComAjuDet.setReportMetodo("listComAjuDet");
    		// carga de columnas
    	
			//Período
    	 rtComAjuDet.addReportColumn("Período", "detAjuDet.plaFueDatCom.periodo");
    		//Anio
    	 rtComAjuDet.addReportColumn("Año", "detAjuDet.plaFueDatCom.anio");
			//Ajuste Original
    	 rtComAjuDet.addReportColumn("Ajuste Original", "ajusteOriginal");
    	   //Actualización
    	 rtComAjuDet.addReportColumn("Actualización", "actualizacion");
    	   //Capital Compensado
    	 rtComAjuDet.addReportColumn("Capital Compensado", "capitalCompensado");
    	   //Actualización Compensada
    	 rtComAjuDet.addReportColumn("Actualización Compensada", "actCom");
    	 
		
	     report.getReportListTable().add(rtComAjuDet);
	
	}
	
	// View getters
}
