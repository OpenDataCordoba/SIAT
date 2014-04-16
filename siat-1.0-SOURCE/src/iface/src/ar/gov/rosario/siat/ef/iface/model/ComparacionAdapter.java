//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del Comparacion
 * 
 * @author tecso
 */
public class ComparacionAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "comparacionAdapterVO";

	public static final String ENC_NAME = "encComparacionAdapterVO";
	
    private ComparacionVO comparacion = new ComparacionVO();
    
    private CompFuenteResVO compFuenteResVO = new CompFuenteResVO();
    
    private Long difPositivo;
    
    private Long difNegativo;

	private boolean agregarCompFuenteEnabled=true;

	private boolean calcularDifEnabled=true;

	private boolean eliminarCompFuenteResEnabled=true;

	private boolean imprimirComparacionEnabled=true;

    // Constructores
    public ComparacionAdapter(){
    	super(EfSecurityConstants.ABM_COMPARACION);
    	ACCION_MODIFICAR_ENCABEZADO = EfSecurityConstants.ABM_COMPARACION_ENC;
    }
    
    //  Getters y Setters
	public ComparacionVO getComparacion() {
		return comparacion;
	}

	public void setComparacion(ComparacionVO comparacionVO) {
		this.comparacion = comparacionVO;
	}

	public Long getDifPositivo() {
		return difPositivo;
	}

	public void setDifPositivo(Long difPositivo) {
		this.difPositivo = difPositivo;
	}

	public Long getDifNegativo() {
		return difNegativo;
	}

	public void setDifNegativo(Long difNegativo) {
		this.difNegativo = difNegativo;
	}

	public CompFuenteResVO getCompFuenteResVO() {
		return compFuenteResVO;
	}

	public void setCompFuenteResVO(CompFuenteResVO compFuenteResVO) {
		this.compFuenteResVO = compFuenteResVO;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Comparación");     
		 report.setReportBeanName("Comparacion");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportComparacion = new ReportVO();
		 reportComparacion.setReportTitle("Datos del Comparación");
		 // carga de datos
	     
	     //Fecha
		 reportComparacion.addReportDato("Fecha", "fecha");
		 //Descripción
		 reportComparacion.addReportDato("Descripción", "descripcion");
	     
		 report.getListReport().add(reportComparacion);
		 
		 // Lista de fuentes
		 ReportTableVO rtCompFuente = new ReportTableVO("CompFuenteVO");
    	 rtCompFuente.setTitulo("Lista de fuentes");
    	 rtCompFuente.setReportMetodo("listCompFuente");
    		// carga de columnas
			//Descripción
    	 rtCompFuente.addReportColumn("Descripción", "plaFueDat.tituloView");
			//Desde
    	 rtCompFuente.addReportColumn("Desde", "periodoDesde4View");
			//Hasta
    	 rtCompFuente.addReportColumn("Hasta", "periodoHasta4View");
    		//Total
    	 rtCompFuente.addReportColumn("Total", "TotalView");
		 
	     report.getReportListTable().add(rtCompFuente);
	     
	     // Diferencias
		 ReportTableVO rtCompFuenteRes = new ReportTableVO("CompFuenteRes");
    	 rtCompFuenteRes.setTitulo("Diferencias");
    	 rtCompFuenteRes.setReportMetodo("listCompFuenteRes");
    		// carga de columnas
			//Operación
    	 rtCompFuenteRes.addReportColumn("Operación", "operacion");
			//Diferencia
    	 rtCompFuenteRes.addReportColumn("Diferencia", "diferenciaReport");
    	 
		
	     report.getReportListTable().add(rtCompFuenteRes);
	
	
	}
	
	// View getters
	
	//flag getters
	public String getAgregarCompFuenteEnabled(){
		return SiatBussImageModel.hasEnabledFlag(agregarCompFuenteEnabled, EfSecurityConstants.ABM_COMPFUENTE, BaseSecurityConstants.AGREGAR);
	}
	
	public String getVerCompFuenteEnabled(){
		return SiatBussImageModel.hasEnabledFlag(agregarCompFuenteEnabled, EfSecurityConstants.ABM_COMPFUENTE, BaseSecurityConstants.VER);
	}
	
	public String getEliminarCompFuenteEnabled(){
		return SiatBussImageModel.hasEnabledFlag(agregarCompFuenteEnabled, EfSecurityConstants.ABM_COMPFUENTE, BaseSecurityConstants.ELIMINAR);
	}

	public String getCalcularDifEnabled(){
		return SiatBussImageModel.hasEnabledFlag(calcularDifEnabled, EfSecurityConstants.ABM_COMPARACION, EfSecurityConstants.MTD_CALCULARDIF);
	}
	
	public String getEliminarCompFuenteResEnabled(){
		return SiatBussImageModel.hasEnabledFlag(eliminarCompFuenteResEnabled, EfSecurityConstants.ABM_COMPFUENTERES, BaseSecurityConstants.ELIMINAR);
	}
	
	public String getImprimirComparacionEnabled(){
		return SiatBussImageModel.hasEnabledFlag(imprimirComparacionEnabled, EfSecurityConstants.ABM_COMPARACION, EfSecurityConstants.IMPRIMIR);
	}
	
}
