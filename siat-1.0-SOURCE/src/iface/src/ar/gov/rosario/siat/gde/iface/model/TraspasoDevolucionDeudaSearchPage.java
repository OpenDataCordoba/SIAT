//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Search Page de Traspasos de Deuda entre Procuradores y Devoluciones de Deuda a Via Administrativa
 * @author tecso
 *
 */
public class TraspasoDevolucionDeudaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;
									   			
	public static final String NAME = "traspasoDevolucionDeudaSearchPageVO";
	
	// sacarpublic static final String ID_ACCION_SELECCIONADA = "idAccionSeleccionada";

	private AccionTraspasoDevolucion accionTraspasoDevolucion = AccionTraspasoDevolucion.OpcionSelecionar;
	
	private RecursoVO    recurso           = new RecursoVO();
	private ProcuradorVO procuradorOrigen  = new ProcuradorVO();
	private ProcuradorVO procuradorDestino = new ProcuradorVO();

	private CuentaVO cuenta = new CuentaVO();
	
	private Date 	fechaDesde; 
	private Date 	fechaHasta; 

	private String 	fechaDesdeView = "";
	private String 	fechaHastaView = "";
	
	private List<AccionTraspasoDevolucion> listAccionTraspasoDevolucion = new ArrayList<AccionTraspasoDevolucion>();
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();

	private List<ProcuradorVO> listProcuradorOrigen  = new ArrayList<ProcuradorVO>();
	private List<ProcuradorVO> listProcuradorDestino = new ArrayList<ProcuradorVO>();


	// Constructor
	public TraspasoDevolucionDeudaSearchPage(){
		super(GdeSecurityConstants.ABM_TRASPASO_DEVOLUCION_DEUDA);
    	ACCION_AGREGAR  = GdeSecurityConstants.ABM_TRASPASO_DEVOLUCION_DEUDA_ENC;    // necesaria
    	ACCION_MODIFICAR_ENCABEZADO = GdeSecurityConstants.ABM_TRASPASO_DEVOLUCION_DEUDA_ENC;   // necesaria
	}

	// Getters y Setters
	public AccionTraspasoDevolucion getAccionTraspasoDevolucion() {
		return accionTraspasoDevolucion;
	}
	public void setAccionTraspasoDevolucion(
			AccionTraspasoDevolucion accionTraspasoDevolucion) {
		this.accionTraspasoDevolucion = accionTraspasoDevolucion;
	}
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
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
	public List<ProcuradorVO> getListProcuradorDestino() {
		return listProcuradorDestino;
	}
	public void setListProcuradorDestino(List<ProcuradorVO> listProcuradorDestino) {
		this.listProcuradorDestino = listProcuradorDestino;
	}
	public List<ProcuradorVO> getListProcuradorOrigen() {
		return listProcuradorOrigen;
	}
	public void setListProcuradorOrigen(List<ProcuradorVO> listProcuradorOrigen) {
		this.listProcuradorOrigen = listProcuradorOrigen;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public ProcuradorVO getProcuradorDestino() {
		return procuradorDestino;
	}
	public void setProcuradorDestino(ProcuradorVO procuradorDestino) {
		this.procuradorDestino = procuradorDestino;
	}
	public ProcuradorVO getProcuradorOrigen() {
		return procuradorOrigen;
	}
	public void setProcuradorOrigen(ProcuradorVO procuradorOrigen) {
		this.procuradorOrigen = procuradorOrigen;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public List<AccionTraspasoDevolucion> getListAccionTraspasoDevolucion() {
		return listAccionTraspasoDevolucion;
	}
	public void setListAccionTraspasoDevolucion(
			List<AccionTraspasoDevolucion> listAccionTraspasoDevolucion) {
		this.listAccionTraspasoDevolucion = listAccionTraspasoDevolucion;
	}

	// Flags Seguridad


}