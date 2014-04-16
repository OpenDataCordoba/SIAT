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
 * Adapter del OrdenControl
 * 
 * @author tecso
 */
public class OrdenControlAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "ordenControlAdapterVO";
	
	private OrdenControlVO ordenControl = new OrdenControlVO();
	
	private PeriodoOrdenVO periodoOrdenDesde = new PeriodoOrdenVO();
	private PeriodoOrdenVO periodoOrdenHasta = new PeriodoOrdenVO();

	private OrdConCueVO ordConCue = new OrdConCueVO();
	
	private List<TipoOrdenVO>listTipoOrden=new ArrayList<TipoOrdenVO>();
	
	private List<TipoPeriodoVO>listTipoPeriodo=new ArrayList<TipoPeriodoVO>();
	
	List<OrdenControlVO> listOrdenControl = new ArrayList<OrdenControlVO>();
	
	List<OrdConCueVO> listOrdConCue = new ArrayList<OrdConCueVO>();
	
	List<EstadoOrdenVO>listEstadoOrden = new ArrayList<EstadoOrdenVO>();
	
    private Long[]	listIds;
    
    private String[] listIdOrdConCue;
    
    private String observacion = "";
    
    private String descripcion = "";
    
    
    
    public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	// Constructores
    public OrdenControlAdapter(){
    	super(EfSecurityConstants.EMITIR_ORDENCONTROL);
    }
    
    //  Getters y Setters
	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControlVO) {
		this.ordenControl = ordenControlVO;
	}
	
	public Long[] getListIds() {
		return listIds;
	}

	public void setListIds(Long[] listIds) {
		this.listIds = listIds;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Orden Control");     
		 report.setReportBeanName("OrdenControl");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportOrdenControl = new ReportVO();
		 reportOrdenControl.setReportTitle("Datos del OrdenControl");
		 // carga de datos
	     
	     //Cï¿½digo
		 reportOrdenControl.addReportDato("Cï¿½digo", "codOrdenControl");
		 //Descripciï¿½n
		 reportOrdenControl.addReportDato("Descripciï¿½n", "desOrdenControl");
	     
		 report.getListReport().add(reportOrdenControl);
	
	}

	public List<OrdenControlVO> getListOrdenControl() {
		return listOrdenControl;
	}

	public void setListOrdenControl(List<OrdenControlVO> listOrdenControl) {
		this.listOrdenControl = listOrdenControl;
	}

	public List<OrdConCueVO> getListOrdConCue() {
		return listOrdConCue;
	}

	public void setListOrdConCue(List<OrdConCueVO> listOrdConCue) {
		this.listOrdConCue = listOrdConCue;
	}

	public OrdConCueVO getOrdConCue() {
		return ordConCue;
	}

	public void setOrdConCue(OrdConCueVO ordConCue) {
		this.ordConCue = ordConCue;
	}

	
	public String getDescripcionArmada() {
		descripcion+="Nro Orden:"+this.getOrdenControl().getOrdenControlView()+" ";
		descripcion+="Nro Cuenta: "+this.getOrdConCue().getCuenta().getNumeroCuenta()+" ";
		descripcion+="Período Retroactivo Desde: "+this.getPeriodoOrdenDesde().getPeriodoView()+"/"+this.getPeriodoOrdenDesde().getAnioView()+" ";
		descripcion+="Hasta: "+this.getPeriodoOrdenHasta().getPeriodoView()+"/"+this.getPeriodoOrdenHasta().getAnioView()+" ";
	   
		return descripcion;
	}

	public void setDescripcionArmada(String descripcion) {
		this.descripcion = descripcion;
	}

	public PeriodoOrdenVO getPeriodoOrdenDesde() {
		return periodoOrdenDesde;
	}

	public void setPeriodoOrdenDesde(PeriodoOrdenVO periodoOrdenDesde) {
		this.periodoOrdenDesde = periodoOrdenDesde;
	}

	public PeriodoOrdenVO getPeriodoOrdenHasta() {
		return periodoOrdenHasta;
	}

	public void setPeriodoOrdenHasta(PeriodoOrdenVO periodoOrdenHasta) {
		this.periodoOrdenHasta = periodoOrdenHasta;
	}

	public List<TipoOrdenVO> getListTipoOrden() {
		return listTipoOrden;
	}

	public void setListTipoOrden(List<TipoOrdenVO> listTipoOrden) {
		this.listTipoOrden = listTipoOrden;
	}

	public List<TipoPeriodoVO> getListTipoPeriodo() {
		return listTipoPeriodo;
	}

	public void setListTipoPeriodo(List<TipoPeriodoVO> listTipoPeriodo) {
		this.listTipoPeriodo = listTipoPeriodo;
	}

	public List<EstadoOrdenVO> getListEstadoOrden() {
		return listEstadoOrden;
	}

	public void setListEstadoOrden(List<EstadoOrdenVO> listEstadoOrden) {
		this.listEstadoOrden = listEstadoOrden;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String[] getListIdOrdConCue() {
		return listIdOrdConCue;
	}

	public void setListIdOrdConCue(String[] listIdOrdConCue) {
		this.listIdOrdConCue = listIdOrdConCue;
	}



	// View getters
}
