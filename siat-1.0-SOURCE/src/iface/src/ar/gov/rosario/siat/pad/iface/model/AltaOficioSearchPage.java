//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del AltaOficio
 * 
 * @author Tecso
 *
 */
public class AltaOficioSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "altaOficioSearchPageVO";
	
	private String nroComercio="";
	
	private CuentaVO cuenta = new CuentaVO();
	
	private Integer idEstadoSel = -1;
	
	private Date fechaDesde;
	private Date fechaHasta;
		
	private List<Estado> listEstado 		= new ArrayList<Estado>();
	
	private String fechaDesdeView;
	private String fechaHastaView;
	
	// Constructores
	public AltaOficioSearchPage() {       
       super(EfSecurityConstants.ABM_ALTAOFICIO);        
    }
	
	// Getters y Setters

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDese) {
		this.fechaDesde = fechaDese;
		this.fechaDesdeView = DateUtil.formatDate(fechaDese, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public String getNroComercio() {
		return nroComercio;
	}

	public void setNroComercio(String nroComercio) {
		this.nroComercio = nroComercio;
	}

	public List<Estado> getListEstado() {
		return listEstado;
	}

	public void setListEstado(List<Estado> listEstado) {
		this.listEstado = listEstado;
	}

	public Integer getIdEstadoSel() {
		return idEstadoSel;
	}

	public void setIdEstadoSel(Integer idEstadoSel) {
		this.idEstadoSel = idEstadoSel;
	}

	// View getters
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	
	public String getFechaHastaView() {
		return fechaHastaView;
	}
	
	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Alta Oficio");
		 report.setReportBeanName("cuenta");
		 report.setReportFileName(this.getClass().getName());
		 // Cuenta
	
	     report.addReportFiltro("Cuenta", this.getCuenta().getNumeroCuenta());
		 
		 // Nro. Comercio
		 report.addReportFiltro("Nro. Comercio", this.getNroComercio());
		 
		 //Estado
	
		 report.addReportFiltro("Estado", this.getCuenta().getEstado().getValue());
		 
		 // fecha desde
		 report.addReportFiltro("Fecha Desde", this.getFechaDesdeView());
	     
         // fecha Hasta
		 report.addReportFiltro("Fecha Hasta", this.getFechaHastaView());
		 
	
	     // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtOF = new ReportTableVO("rtOF");
	     rtOF.setTitulo("Listado de Alta de Oficio");
	   
	     // carga de columnas
	     rtOF.addReportColumn("Número Cuenta", "numeroCuenta");
	     rtOF.addReportColumn("Nro. Comercio", "objImp.claveFuncional");
	     rtOF.addReportColumn("Fecha Alta", "objImp.fechaAlta");
	     rtOF.addReportColumn("Estado", "estadoView");
	     report.getReportListTable().add(rtOF);

	    }
}
