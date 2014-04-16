//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.BancoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.pad.iface.model.TipoDocumentoVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TipoBoleta;

/**
 * Adapter del Reclamo
 * 
 * @author tecso
 */
public class ReclamoAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "reclamoAdapterVO";
	
	public static final String PARAM_MAS_DATOS = "paramMasDatos";
	
    private ReclamoVO reclamo = new ReclamoVO();
    
    private List<SiNo>      listSiNo = new ArrayList<SiNo>();
    
    private List<EstadoReclamoVO> listEstadoReclamo = new ArrayList<EstadoReclamoVO>();
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<TipoBoleta> listTipoBoleta = new ArrayList<TipoBoleta>();
	private List<ViaDeudaVO> listViaDeuda = new ArrayList<ViaDeudaVO>();
	private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
	private List<BancoVO> listBanco = new ArrayList<BancoVO>();
	private List<TipoDocumentoVO> listTipoDocumento = new ArrayList<TipoDocumentoVO>();

	private List<IndetVO> listIndet = new ArrayList<IndetVO>();
	
	// Flags
	private boolean paramMasDatos = false;
	
    // Constructores
    public ReclamoAdapter(){
    	super(BalSecurityConstants.ABM_RECLAMO);
    }
    
    //  Getters y Setters
	public ReclamoVO getReclamo() {
		return reclamo;
	}

	public void setReclamo(ReclamoVO reclamoVO) {
		this.reclamo = reclamoVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public List<EstadoReclamoVO> getListEstadoReclamo() {
		return listEstadoReclamo;
	}

	public void setListEstadoReclamo(List<EstadoReclamoVO> listEstadoReclamo) {
		this.listEstadoReclamo = listEstadoReclamo;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<TipoBoleta> getListTipoBoleta() {
		return listTipoBoleta;
	}

	public void setListTipoBoleta(List<TipoBoleta> listTipoBoleta) {
		this.listTipoBoleta = listTipoBoleta;
	}

	
	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	public void setListViaDeuda(List<ViaDeudaVO> listViaDeuda) {
		this.listViaDeuda = listViaDeuda;
	}

	public List<ViaDeudaVO> getListViaDeuda() {
		return listViaDeuda;
	}

	public List<BancoVO> getListBanco() {
		return listBanco;
	}

	public void setListBanco(List<BancoVO> listBanco) {
		this.listBanco = listBanco;
	}

	public List<TipoDocumentoVO> getListTipoDocumento() {
		return listTipoDocumento;
	}

	public void setListTipoDocumento(List<TipoDocumentoVO> listTipoDocumento) {
		this.listTipoDocumento = listTipoDocumento;
	}
	public String getName(){
		return NAME;
	}
	
		
	public boolean isParamMasDatos() {
		return paramMasDatos;
	}

	public void setParamMasDatos(boolean paramMasDatos) {
		this.paramMasDatos = paramMasDatos;
	}

	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Reclamo");
		 report.setReportBeanName("reclamo");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportReclamo = new ReportVO();
		 reportReclamo.setReportTitle("Datos del Reclamo");
		 // carga de datos
	     
	     //Recurso
		 reportReclamo.addReportDato("Recurso", "recurso.desRecurso");
		 //Cuenta
		 reportReclamo.addReportDato("Cuenta", "nroCuenta");
	     //Tipo Boleta
		 reportReclamo.addReportDato("Tipo Boleta", "desTipoBoleta");
	     //Vía Deuda
		 reportReclamo.addReportDato("Vía Deuda", "viaDeuda.desViaDeuda");
	     //Procurador
		 reportReclamo.addReportDato("Procurador", "procurador.descripcion");
	     //Fecha Pago
		 reportReclamo.addReportDato("Fecha Pago", "fechaPago");
		//Importe Pagado
		 reportReclamo.addReportDato("Importe Pagado", "importePagado");
		//Lugar de Pagos
		 reportReclamo.addReportDato("Lugar de Pagos", "banco.desBanco");
		//Nombre
		 reportReclamo.addReportDato("Nombre", "nombre");
		//Apellido
		 reportReclamo.addReportDato("Apellido", "apellido");
		 //Tipo Doc. 
		 reportReclamo.addReportDato("Tipo Doc.", "tipoDoc");
		//Nro Doc. 
		 reportReclamo.addReportDato("Nro Doc.", "nroDoc");
		 //Observación
		 reportReclamo.addReportDato("Observación", "observacion");

		 //Nro Doc. 
		 reportReclamo.addReportDato("Email", "correoElectronico");		 
		 //estado
		 reportReclamo.addReportDato("Estado Reclamo", "estadoReclamo.desEstadoReclamo");
		 //respuesta
		 reportReclamo.addReportDato("Respuesta", "respuesta");

		 report.getListReport().add(reportReclamo);
	
	}

	public List<IndetVO> getListIndet() {
		return listIndet;
	}

	public void setListIndet(List<IndetVO> listIndet) {
		this.listIndet = listIndet;
	}

}
