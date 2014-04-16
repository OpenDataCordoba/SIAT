//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.pro.buss.dao.ProDAOFactory;
import ar.gov.rosario.siat.pro.iface.util.ProError;
import coop.tecso.adpcore.AdpProcess;
import coop.tecso.adpcore.engine.CronProcessor;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean correspondiente a Proceso
 * Son los tipos de procesos del SIAT.
 * 
 * @author tecso
 */

@Entity
@Table(name = "pro_proceso")
public class Proceso extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "proceso";
	
	public static final String PROCESO_PROCESO_MASIVO = "ProcesoMasivo";
	public static final String PROCESO_NOVEDADES_CATASTRO = "NovCatastro";
	public static final String PROCESO_EMISION_CDM = "EmisionCdm";	
	public static final String PROCESO_IMPRESION_CDM = "ImpresionCdm";
	public static final String PROCESO_EMISION_COR_CDM = "EmisionCorCdm";
	public static final String PROCESO_ANULACION_OBRA_CDM = "AnulacionObraCdm";
	public static final String PROCESO_RESCATE_MASIVO = "RescateMasivo";
	public static final String PROCESO_LIQ_COM = "LiqCom";
	public static final String PROCESO_SALPORCAD_MASIVO = "SaldoPorCadMasivo";
	public static final String PROCESO_REPORTE_RESPUESTA_OPERATIVOS = "ReporteResOpe";
	public static final String PROCESO_REPORTE_RECAUDACION = "ReporteRecaudacion";
	public static final String PROCESO_REPORTE_CONVENIO = "ReporteConvenio";
	public static final String PROCESO_REPORTE_IMPORTE_RECAUDAR = "ReporteImpRecaudar";
	public static final String PROCESO_REPORTE_IMPORTE_RECAUDADO = "ReporteImpRecaudado";
	public static final String PROCESO_REPORTE_CONVENIO_FORM = "ReporteConvenioForm";
	public static final String PROCESO_REPORTE_CONVENIO_A_CADUCAR = "ReporteConACaducar";
	public static final String PROCESO_REPORTE_GESJUD = "ReporteGesJud";
	public static final String PROCESO_REPORTE_DEUDA_ANULADA = "ReporteDeudaAnulada";
	public static final String PROCESO_REPORTE_EMISION = "ReporteTotalEmision";
	public static final String PROCESO_REPORTE_DISTRIBUCION = "ReporteDistribucion";
	public static final String PROCESO_REPORTE_RECAUDADO = "ReporteRecaudado";
	public static final String PROCESO_REPORTE_CONTRIBUYENTECER = "ReporteContribuyente";
	public static final String PROCESO_REPORTE_RECCONCER = "ReporteRecCon";
	public static final String PROCESO_REPORTE_RECAUDACIONCER = "ReporteRecCer";
	public static final String PROCESO_REPORTE_DETRECAUDACIONCER = "ReporteDetRecCer";
	public static final String PROCESO_REPORTE_DEUDA_PROCURADOR = "ReporteDeudaProcurad";
	public static final String PROCESO_OPEINVBUS = "BusqMasivaOpeInvBus";
	public static final String PROCESO_PRESCRIPCION_DEUDA = "PrescripcionDeuda";
	public static final String PROCESO_REPORTE_CLASIFICADOR = "ReporteClasificador";
	public static final String PROCESO_REPORTE_RENTAS = "ReporteRentas";
	public static final String PROCESO_REPORTE_TOTAL_NODO = "ReporteTotalNodo";
	public static final String PROCESO_REPORTE_TOTAL_PAR = "ReporteTotalPar";
	public static final String PROCESO_REPORTE_CLACOM = "ReporteClaCom";
	public static final String PROCESO_BALANCE = "Balance";
	public static final String PROCESO_ACCIONES_CONVENIO="AccionesConvenio";
	public static final String PROCESO_ASENTAMIENTO = "AsentamientoPago";
	public static final String PROCESO_ASEDEL = "AsentamientoDelegado";
	public static final String PROCESO_IMPRESION_MASIVA_DEUDA = "ImpMasDeu";
	public static final String PROCESO_EMISION_TGI_ALFAX = "EmisionTgiAlfax";
	public static final String PROCESO_EMISION_MAS = "EmisionMasiva";
	public static final String PROCESO_RES_LIQ_DEU = "ResLiqDeu";
	public static final String PROCESO_RES_LIQ_DEU_ALFAX = "ResLiqDeuAlfax";
	public static final String PROCESO_EMISION_EXTERNA = "EmisionExterna";
	public static final String PROCESO_PROPASDEB = "ProPasDeb";
	public static final String PROCESO_NOVEDADRS = "NovedadRS";
	public static final String PROCESO_NOVOSIRIS = "NovOsiris";
	public static final String PROCESO_ENVIO_OSIRIS = "ProcesarEnviosOsiris";
	public static final String PROCESO_REPORTE_SOLPEND = "ReporteSolPend";
	

	@Column(name = "codProceso")
	private String codProceso;
	@Column(name = "desProceso")
	private String desProceso;
	@Column(name = "esAsincronico")
	private Integer esAsincronico;
	@ManyToOne()  
    @JoinColumn(name="idTipoEjecucion")
	private TipoEjecucion tipoEjecucion;
	@Column(name = "directorioInput")
	private String directorioInput;
	@Column(name = "cantPasos")
	private Long cantPasos;
	@ManyToOne()  
    @JoinColumn(name="idTipoProgEjec")
	private TipoProgEjec tipoProgEjec;
	@Column(name = "classForName")
	private String classForName;
	@Column(name = "spValidate")
	private String spValidate;
	@Column(name = "spExecute")
	private String spExecute;
	@Column(name = "spResume")
	private String spResume;
	@Column(name = "spCancel")
	private String spCancel;
	@Column(name = "ejecNodo")
	private String ejecNodo;
	@Column(name = "locked")
	private Integer locked;
	@Column(name = "cronExpression")
	private String cronExpression;
	
	// Constructores
	public Proceso(){
		super();
	}
	// Getters y Setters
	public String getCodProceso(){
		return codProceso;
	}
	public void setCodProceso(String codProceso){
		this.codProceso = codProceso;
	}
	public String getDesProceso(){
		return desProceso;
	}
	public void setDesProceso(String desProceso){
		this.desProceso = desProceso;
	}
	public Integer getEsAsincronico(){
		return esAsincronico;
	}
	public void setEsAsincronico(Integer esAsincronico){
		this.esAsincronico = esAsincronico;
	}
	public TipoEjecucion getTipoEjecucion(){
		return tipoEjecucion;
	}
	public void setTipoEjecucion(TipoEjecucion tipoEjecucion){
		this.tipoEjecucion = tipoEjecucion;
	}
	public String getDirectorioInput(){
		return directorioInput;
	}
	public void setDirectorioInput(String directorioInput){
		this.directorioInput = directorioInput;
	}
	public Long getCantPasos(){
		return cantPasos;
	}
	public void setCantPasos(Long cantPasos){
		this.cantPasos = cantPasos;
	}
	public TipoProgEjec getTipoProgEjec(){
		return tipoProgEjec;
	}
	public void setTipoProgEjec(TipoProgEjec tipoProgEjec){
		this.tipoProgEjec = tipoProgEjec;
	}
	public String getClassForName(){
		return classForName;
	}
	public void setClasForName(String classForName){
		this.classForName = classForName;
	}
	public String getSpValidate(){
		return spValidate;
	}
	public void setSpValidate(String spValidate){
		this.spValidate = spValidate;
	}
	public String getSpExecute(){
		return spExecute;
	}
	public void setSpExecute(String spExecute){
		this.spExecute = spExecute;
	}
	public String getSpResume(){
		return spResume;
	}
	public void setSpResume(String spResume){
		this.spResume = spResume;
	}
	public String getSpCancel(){
		return spCancel;
	}
	public void setSpCancel(String spCancel){
		this.spCancel = spCancel;
	}

	public Integer getLocked() {
		return locked;
	}
	public void setLocked(Integer locked) {
		this.locked = locked;
	}
	
	public String getEjecNodo() {
		return ejecNodo;
	}
	public void setEjecNodo(String ejecNodo) {
		this.ejecNodo = ejecNodo;
	}
	public void setClassForName(String classForName) {
		this.classForName = classForName;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	
	// Metodos de Clase
	public static Proceso getById(Long id) {
		return (Proceso) ProDAOFactory.getProcesoDAO().getById(id);  
	}
	
	public static Proceso getByIdNull(Long id) {
		return (Proceso) ProDAOFactory.getProcesoDAO().getByIdNull(id);
	}
	
	/** Recupera un proceso a partir de su codigo
	 * 
	 * @param id
	 * @return
	 */
	public static Proceso getByCodigo(String codigo) throws Exception {
		return ProDAOFactory.getProcesoDAO().getByCodigo(codigo);  
	}
	
	public static List<Proceso> getList() {
		return (List<Proceso>) ProDAOFactory.getProcesoDAO().getList();
	}
	
	public static List<Proceso> getListActivos() {			
		return (List<Proceso>) ProDAOFactory.getProcesoDAO().getListActiva();
	}

	/**
	 * Obtiene todos los Codigos de los diferentes procesos cargados
	 * @return
	 */
	public static List<String> getListCodigos(){
		return (List<String>) ProDAOFactory.getProcesoDAO().getListCodigos();
	}
	
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		
		if (hasError()) {
			return false;
		}

				
		return !hasError();
	}
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
	
		if (hasError()) {
			return false;
		}

			
		return !hasError();
	}

	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();
		
		//UniqueMap uniqueMap = new UniqueMap();

		//Validaciones de Requeridos
		if (StringUtil.isNullOrEmpty(getCodProceso())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.PROCESO_CODPROCESO);
		}
		if (StringUtil.isNullOrEmpty(getDesProceso())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.PROCESO_DESPROCESO);
		}
		if(getEsAsincronico()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.PROCESO_ESASINCRONICO);
		}
		if (getTipoEjecucion()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.PROCESO_TIPOEJECUCION);
		}
		if (getCantPasos()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.PROCESO_CANTPASOS);
		}
		if (getTipoProgEjec()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.PROCESO_TIPOPROGEJEC);
		}
		
		if (hasError()) {
			return false;
		}
		
		
		if(getTipoEjecucion().getId().longValue()==AdpProcess.TIPO_PERIODIC){
			if(StringUtil.isNullOrEmpty(getCronExpression())){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.PROCESO_CRONEXPRESSION);
			}
			
			if (hasError()) {
				return false;
			}
			
			String validateResult = CronProcessor.validateCronExpression(getCronExpression());
			if(!StringUtil.isNullOrEmpty(validateResult)) addRecoverableValueError(validateResult);
			
		}
		
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		
		// Otras Validaciones

		return !hasError();
	}

	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		//Valida que no tenga registros de corrida asociados
		if(ProDAOFactory.getProcesoDAO().hasReferenceGen(this, Corrida.class, "proceso"))
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, ProError.PROCESO_LABEL ,
					ProError.CORRIDA_LABEL);

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		
		return true;
	}

	// Metodos de negocio


}
