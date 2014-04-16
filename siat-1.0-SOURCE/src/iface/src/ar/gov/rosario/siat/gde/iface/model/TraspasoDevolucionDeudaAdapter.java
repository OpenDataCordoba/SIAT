//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;

/**
 * Adapter de Traspasos de Deuda entre Procuradores y Devoluciones de Deuda a Via Administrativa
 * 
 * @author tecso
 */
public class TraspasoDevolucionDeudaAdapter extends SiatAdapterModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME = "traspasoDevolucionDeudaAdapterVO";

	public static final String ENC_NAME = "encTraspasoDevolucionDeudaAdapterVO";
	
	// clave de parametros para recuperar el valor y asi determinar si utilizo un traspaso o una devolucion
	public static final String ACCION_KEY = "accionTraspasoDevolucionKey";   
	// clave de parametros para recuperar el valor del id de la constancia del traspaso	
	public static final String CONSTANCIA_KEY = "constanciaKey";
	
    private TraspasoDeudaVO   traspasoDeuda   = new TraspasoDeudaVO();
    private DevolucionDeudaVO devolucionDeuda = new DevolucionDeudaVO();
    
    private AccionTraspasoDevolucion accionTraspasoDevolucion = AccionTraspasoDevolucion.OpcionSelecionar;
    
    // desgloce de todas las propiedades.
    private RecursoVO    recurso            = new RecursoVO();
    private ProcuradorVO procuradorOrigen   = new ProcuradorVO();
    private ProcuradorVO procuradorDestino  = new ProcuradorVO();
    private CuentaVO     cuenta             = new CuentaVO();
    private Date         fecha;
    private String       fechaView;
    private String       observacion;
    
    private ConstanciaDeuVO constanciaDeuVO = new ConstanciaDeuVO();    // usamos las deudas del detalle de la constancia
     
    private List<AccionTraspasoDevolucion> listAccionTraspasoDevolucion = new ArrayList<AccionTraspasoDevolucion>();
	private List<RecursoVO>    listRecurso           = new ArrayList<RecursoVO>();
	private List<ProcuradorVO> listProcuradorOrigen  = new ArrayList<ProcuradorVO>();
	private List<ProcuradorVO> listProcuradorDestino = new ArrayList<ProcuradorVO>();

	private String[] idsTraDevDeuDetsSelected;     // ids seleccionadas en las agregaciones a los Traspasos y Devoluciones
	
	// Constructores
    public TraspasoDevolucionDeudaAdapter(){
    	super(GdeSecurityConstants.ABM_TRASPASO_DEVOLUCION_DEUDA);
    	ACCION_MODIFICAR_ENCABEZADO = GdeSecurityConstants.ABM_TRASPASO_DEVOLUCION_DEUDA_ENC;
    	ACCION_AGREGAR  = GdeSecurityConstants.ABM_TRASPASO_DEVOLUCION_DEUDA_ENC;    // necesaria
    }
    
    public String getAgregarTraDevDeuDetEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(GdeSecurityConstants.ABM_TRADEVDEUDET, BaseSecurityConstants.AGREGAR);
	}
    public String getVerTraDevDeuDetEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(GdeSecurityConstants.ABM_TRADEVDEUDET, BaseSecurityConstants.VER);
	}
	public String getModificarTraDevDeuDetEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(GdeSecurityConstants.ABM_TRADEVDEUDET, BaseSecurityConstants.MODIFICAR);
	}
	public String getEliminarTraDevDeuDetEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(GdeSecurityConstants.ABM_TRADEVDEUDET, BaseSecurityConstants.ELIMINAR);
	}	
	
	//  Getters y Setters
	public DevolucionDeudaVO getDevolucionDeuda() {
		return devolucionDeuda;
	}
	public void setDevolucionDeuda(DevolucionDeudaVO devolucionDeuda) {
		this.devolucionDeuda = devolucionDeuda;
	}
	public List<AccionTraspasoDevolucion> getListAccionTraspasoDevolucion() {
		return listAccionTraspasoDevolucion;
	}
	public void setListAccionTraspasoDevolucion(
			List<AccionTraspasoDevolucion> listAccionTraspasoDevolucion) {
		this.listAccionTraspasoDevolucion = listAccionTraspasoDevolucion;
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
	public TraspasoDeudaVO getTraspasoDeuda() {
		return traspasoDeuda;
	}
	public void setTraspasoDeuda(TraspasoDeudaVO traspasoDeuda) {
		this.traspasoDeuda = traspasoDeuda;
	}
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
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
		this.fechaView = DateUtil.formatDate(fecha, "dd/MM/yyyy");
	}
	public String getFechaView() {
		return fechaView;
	}
	public void setFechaView(String fechaView) {
		this.fechaView = fechaView;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
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
	public String[] getIdsTraDevDeuDetsSelected() {
		return idsTraDevDeuDetsSelected;
	}
	public void setIdsTraDevDeuDetsSelected(String[] idsTraDevDeuDetsSelected) {
		this.idsTraDevDeuDetsSelected = idsTraDevDeuDetsSelected;
	}
	public ConstanciaDeuVO getConstanciaDeuVO() {
		return constanciaDeuVO;
	}
	public void setConstanciaDeuVO(ConstanciaDeuVO constanciaDeuVO) {
		this.constanciaDeuVO = constanciaDeuVO;
	}

	// View getters
	
	public boolean getCreateByConstancia() {
		return !(ModelUtil.isNullOrEmpty(this.getConstanciaDeuVO()));
	}

	public void cargarDatosTraspaso(){
		this.getTraspasoDeuda().setRecurso(this.getRecurso());
		this.getTraspasoDeuda().setProOri(this.getProcuradorOrigen());
		this.getTraspasoDeuda().setProDes(this.getProcuradorDestino());
		this.getTraspasoDeuda().setCuenta(this.getCuenta());
		this.getTraspasoDeuda().setFechaTraspaso(this.getFecha());
		this.getTraspasoDeuda().setObservacion(this.getObservacion());
	}
	
	public void cargarDatosDevolucion(){
		this.getDevolucionDeuda().setRecurso(this.getRecurso());
		this.getDevolucionDeuda().setProcurador(this.getProcuradorOrigen());
		this.getDevolucionDeuda().setCuenta(this.getCuenta());
		this.getDevolucionDeuda().setFechaDevolucion(this.getFecha());
		this.getDevolucionDeuda().setObservacion(this.getObservacion());
	}

}
