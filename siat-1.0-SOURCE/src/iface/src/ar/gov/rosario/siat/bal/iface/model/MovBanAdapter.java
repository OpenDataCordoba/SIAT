//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del MovBan
 * 
 * @author tecso
 */
public class MovBanAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "movBanAdapterVO";
	public static final String ENC_NAME = "encMovBanAdapterVO";
	
    private MovBanVO movBan = new MovBanVO();
    
    private String fileName = "";
	private byte[] fileData;
	private Integer cantLineas = 0;
    
	private List<TotMovBanDetHelper> listSubtotalesPorCuenta = new ArrayList<TotMovBanDetHelper>();
	
	private Boolean conciliarMovBanDetBussEnabled   = true;
	
    // Constructores
    public MovBanAdapter(){
    	super(BalSecurityConstants.ABM_MOVBAN);
    	ACCION_MODIFICAR_ENCABEZADO = BalSecurityConstants.ABM_MOVBAN_ENC;
    }
    
    //  Getters y Setters
	public MovBanVO getMovBan() {
		return movBan;
	}
	public void setMovBan(MovBanVO movBanVO) {
		this.movBan = movBanVO;
	}    
	
	public Integer getCantLineas() {
		return cantLineas;
	}

	public void setCantLineas(Integer cantLineas) {
		this.cantLineas = cantLineas;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public List<TotMovBanDetHelper> getListSubtotalesPorCuenta() {
		return listSubtotalesPorCuenta;
	}

	public void setListSubtotalesPorCuenta(
			List<TotMovBanDetHelper> listSubtotalesPorCuenta) {
		this.listSubtotalesPorCuenta = listSubtotalesPorCuenta;
	}

	/**
	 * Devuelve la ruta completa de la carpeta de trabajo tmp para el archivo.<br>
	 * 
	 * @return
	 */
	public String getPathTmp(){
		return "/mnt/publico/tmp/" + getFileName();
	}

	public String getName(){
		return NAME;
	}
	
		
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Movimientos Bancarios AFIP");     
		 report.setReportBeanName("MovBan");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportMovBan = new ReportVO();
		 reportMovBan.setReportTitle("Datos del Movimiento Bancario AFIP");
		 // carga de datos
	    
		 reportMovBan.addReportDato("Banco Administrador","bancoAdm");
		 reportMovBan.addReportDato("Fecha de Acreditaci\u00F3n","fechaAcredit");
		 reportMovBan.addReportDato("Total D\u00E9bito", "totalDebito");
		 reportMovBan.addReportDato("Total Cr\u00E9dito", "totalCredito");
		 reportMovBan.addReportDato("Cant. Detalles", "cantDetalle");
	     
		 report.getListReport().add(reportMovBan);
		 
		 // Detalle
		 ReportTableVO rtDetalle = new ReportTableVO("Detalle");
		 rtDetalle.setTitulo("Detalles");
		 rtDetalle.setReportMetodo("listMovBanDet");
				     	 
		 // carga de columnas (Banco Rec.  	Nro Cierre Banco 	Cuenta Corriente  	Impuesto  	Debito  	Credito  	Moneda  	Conciliado)
		 //Banco Rec.
		 rtDetalle.addReportColumn("Banco Rec.", "bancoRec");
		 //Nro Cierre Banco
		 rtDetalle.addReportColumn("Nro Cierre Banco", "nroCierreBanco");
	 	 //Impuesto
	 	 rtDetalle.addReportColumn("Impuesto", "impuesto");
	 	 //Debito
	 	 rtDetalle.addReportColumn("D\u00E9bito", "debito");
	 	 //Credito
	 	 rtDetalle.addReportColumn("Cr\u00E9dito", "credito");
	 	 //Moneda
	 	 rtDetalle.addReportColumn("Moneda", "moneda");
	 	 //Conciliado
	 	 rtDetalle.addReportColumn("Conciliado", "conciliado" ,SiNo.class);
	 	 
		 report.getReportListTable().add(rtDetalle);
	}
	
	// Permisos para ABM MovBanDet
	public String getAgregarMovBanDetEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_MOVBANDET, BaseSecurityConstants.AGREGAR);
	}
	public String getVerMovBanDetEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_MOVBANDET, BaseSecurityConstants.VER);
	}
	public String getModificarMovBanDetEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_MOVBANDET, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarMovBanDetEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(BalSecurityConstants.ABM_MOVBANDET, BaseSecurityConstants.ELIMINAR);
	}	
    
	// Flags Seguridad
	public Boolean getConciliarMovBanDetBussEnabled() {
		return conciliarMovBanDetBussEnabled;
	}
	public void setConciliarMovBanDetBussEnabled(Boolean conciliarMovBanDetBussEnabled) {
		this.conciliarMovBanDetBussEnabled = conciliarMovBanDetBussEnabled;
	}
	public String getConciliarMovBanDetEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getConciliarMovBanDetBussEnabled(), 
				BalSecurityConstants.ABM_MOVBAN, BaseSecurityConstants.CONCILIAR);
	}
	
}
