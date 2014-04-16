//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoVO;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.EstadoPeriodo;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del LiqDeuda
 * 
 * @author tecso
 */
public class LiqDeudaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "liqDeudaAdapterVO";
	
	// Propiedades para los distintos logueos
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
    private List<EstadoPeriodo> listEstadoPeriodo = new ArrayList<EstadoPeriodo>();
    private List<RecClaDeuVO> listRecClaDeu = new ArrayList<RecClaDeuVO>();
	
	// Propiedades para mostrar
	private Double totalDeudaCyQ = 0D;
	private Double totalDeudaProcurador = 0D;
	private Double total=0D;	
    private String fechaAcentamiento = "";
    private ProcedimientoVO procedimiento = new ProcedimientoVO(-1);
    
    private LiqCuentaVO cuenta = new LiqCuentaVO(); 
    
    private List<LiqCuentaVO> listCuentaRel = new ArrayList<LiqCuentaVO>();
    private List<LiqDeudaCyQVO> listProcedimientoCyQ = new ArrayList<LiqDeudaCyQVO>();
    private List<LiqDeudaProcuradorVO> listProcurador = new ArrayList<LiqDeudaProcuradorVO>();
    private List<LiqDeudaAdminVO> listGestionDeudaAdmin = new ArrayList<LiqDeudaAdminVO>();  // se espera siempre un unico elemento.
    private List<LiqDeudaAnuladaVO> listBlockDeudaAnulada = new ArrayList<LiqDeudaAnuladaVO>();  // tambien se espera siempre un unico elemento.
    private List<LiqConvenioCuentaAdapter> listConvenioCuentaAdapter = new ArrayList<LiqConvenioCuentaAdapter>();
    
    private LiqExencionesVO exenciones = new LiqExencionesVO();
    
    // ---> Propiedades para ser utilizadas en el CUS Anular Deuda
    private List<MotAnuDeuVO> listMotAnuDeu;
    private AnulacionVO anulacion;
    // <--- Propiedades para ser utilizadas en el CUS Anular Deuda  
    
 	//Flags para controlar la visibilidad de las columnas Seleccionar, Ver y Solicitar. 
	//Por defecto, se muestran todas.
	private Boolean mostrarColumnaSeleccionar = true;
	private Boolean mostrarColumnaVer = true;
	private Boolean mostrarColumnaSolicitar = true;
    
	// Banderas para mostrar o no los checkBox que permiten seleccionar todo un bloque.
	private Boolean mostrarChkAllAdmin = false;
	
	// La bandera "mostrarChkAllJudicial" pasa a cada bloque de deuda procurador
	
    // ---> Propiedad para utilizar en los metodos de SINC
    private String 	selectedId = "";
    private List<ProcedimientoVO>    listProcedimiento = new ArrayList<ProcedimientoVO>();
    private Long 	idProcedimiento;
    private Date 	fechaEnvioCyq;
    private String 	fechaEnvioCyqView;
    private String[] listIdDeudaSelected;
    //  <--- Propiedad para utilizar en los metodos de SINC

    
    // ---> Propiedades para la asignacion de permisos
    // Bandera para ayuda a la definicion de permisos del bloque de deuda judicial.
    private boolean poseeDeudaProcurador = false;
    
    // Banderas para resolver la visibilidad de botones de las acciones
    // Si el usuario posee permisos en SWE, los botones se muestran, y despues se habilitan o no segun reglas de negocio
    private boolean reconfeccionarVisible = false;
    private boolean reconfeccionarEspVisible = false;
    private boolean imprimirInformeDeudaVisible = false;
    private boolean formalizarConvenioVisible = false;
    private boolean formalizarConvenioEspVisible = false;
    private boolean generarEstadoCuentaVisible = false;
    private boolean infDeudaEscribanoVisible = false;
    private boolean cambioPlanCDMVisible = false;
    private boolean cuotaSaldoCDMVisible = false;
    private boolean desglosarAjusteVisible = false;
    private boolean cierreComercioVisible = false;
    private boolean imprimirCierreComercioVisible = false;
    private boolean decJurMasivaVisible = false;
    private boolean reclamarAcentVisible = false;	// Permite ir a la pantalla de Reclamar asentamiento
    private boolean ddjjEntVenHabVisible = false;
    private boolean verNovedadesRSVisible = false;
    private boolean volantePagoIntRSVisible = false;
    
    // Links
    private boolean verDetalleObjImpEnabled = false;    // Permiso para ver el Detalle del Objeto Imponible
    private boolean verHistoricoContribEnabled = false;	// Permiso para ver el Historico de los Contribuyentes de la cuenta
    private boolean verDeudaContribEnabled = false;    	// Permiso para ver el resto de las cuentas de un contribuyente  
    private boolean verCuentaDesgUnifEnabled = false;  	// Permiso para ver desgloses y unificaciones 
    private boolean verCuentaRelEnabled = false; 		// Permiso para Ver Cuentas Relacionadas al objeto Imponible
    private boolean verConvenioEnabled = false; 		// Permiso para ver detalles de convenios
    private boolean buzonCambiosEnabled = false; 		// Permiso para ir al buzon de cambio de datos de persona.
    private boolean verHistoricoExeEnabled = false;	// Permiso para ver el Historico de Exenciones de la cuenta
    private boolean reclamarAcentEnabled = false;	// Permite ir a la pantalla de Reclamar asentamiento
    private boolean verPlanillaEnabled = false;     // Permite ir a la pantalla de planillas
    
    // Acciones sobre la deuda
    private boolean reconfeccionarEnabled = false;
    private boolean reconfeccionarEspEnabled = false;
    private boolean imprimirInformeDeudaEnabled = false;
    private boolean formalizarConvenioEnabled = false;
    private boolean formalizarConvenioEspEnabled = false;
    private boolean infDeudaEscribanoEnabled = false;
    
    // Para los recursos con categoria "Contribucion de Mejoras"
    private boolean cambioPlanCDMEnabled = false;
    private boolean cuotaSaldoCDMEnabled = false;
    
    // Para Recursos Autoliquidables
    private boolean desglosarAjusteEnabled = false;
    
    // Para Recurso DreI
    private boolean cierreComercioEnabled = false;
    private boolean imprimirCierreComercioEnabled = false;
    private boolean decJurMasivaEnabled = false;
    
    // Para Recursos de Espectaculos Publicos
    private boolean ddjjEntVenHabEnabled = false;
    
    // Para Recursos Autoliquidables que implementen Regimen Simplificado
    private boolean verNovedadesRSEnabled = false;
    private boolean volantePagoIntRSEnabled = false;

    // Para Seleccion de Cuenta/Deuda en Cyq
    @Deprecated
    private boolean simularSalPorCad = false;
    // Bandera para decidir de que modo se realiza en evnvio de Deuda a CyQ. Para que se pueda seguir gestionando por la via original o no.
    private SiNo continuaGesViaOri = SiNo.NO; //SiNo.OpcionSelecionar; TODO: Descomentar cuando se implemente completamente Cyq  
    private List<SiNo> listSiNo = new ArrayList<SiNo>();
    
    private boolean poseeDeudaAdmin4Cyq = false;
    private boolean poseeDeudaJudicial4Cyq = false;
    private boolean poseeConvenio = false; // Si existe deuda no seleccionable por estar en convenio.
    private boolean seleccionarDeuda4Cyq = false;
    private boolean seleccionarCuenta4Cyq = false;
    // <--- Propiedades para la asignacion de permisos
    
    // Bandera para permitir que algunos recursos no soliciten el Codigo de Gestion Personal al acceder a la liquidacion desde la web.   
    private Boolean codGesPerRequerido = true;
    
    private boolean verCambioDomicilio = true;
    
	// Constructores
    public LiqDeudaAdapter(){
    	super(GdeSecurityConstants.LIQ_DEUDA);
    }

    //  Getters y Setters
	public LiqCuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(LiqCuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public String getFechaAcentamiento() {
		return fechaAcentamiento;
	}
	public void setFechaAcentamiento(String fechaAcentamiento) {
		this.fechaAcentamiento = fechaAcentamiento;
	}
	
	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public List<LiqCuentaVO> getListCuentaRel() {
		return listCuentaRel;
	}

	public void setListCuentaRel(List<LiqCuentaVO> listCuentaRel) {
		this.listCuentaRel = listCuentaRel;
	}
	
	public List<LiqDeudaAdminVO> getListGestionDeudaAdmin() {
		return listGestionDeudaAdmin;
	}

	public void setListGestionDeudaAdmin(
			List<LiqDeudaAdminVO> listGestionDeudaAdmin) {
		this.listGestionDeudaAdmin = listGestionDeudaAdmin;
	}

	public List<LiqDeudaCyQVO> getListProcedimientoCyQ() {
		return listProcedimientoCyQ;
	}

	public void setListProcedimientoCyQ(
			List<LiqDeudaCyQVO> listProcedimientoCyQ) {
		this.listProcedimientoCyQ = listProcedimientoCyQ;
	}

	public List<LiqDeudaProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public void setListProcurador(List<LiqDeudaProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}
	
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public LiqExencionesVO getExenciones() {
		return exenciones;
	}

	public void setExenciones(LiqExencionesVO exenciones) {
		this.exenciones = exenciones;
	}
	
	public boolean getVerDeudaContribEnabled() {
		return verDeudaContribEnabled;
	}

	public void setVerDeudaContribEnabled(boolean verDeudaContribEnabled) {
		this.verDeudaContribEnabled = verDeudaContribEnabled;
	}
	
	public boolean getVerConvenioEnabled() {
		return verConvenioEnabled;
	}
	public void setVerConvenioEnabled(boolean verConvenioEnabled) {
		this.verConvenioEnabled = verConvenioEnabled;
	}
	
	public boolean isVerCuentaDesgUnifEnabled() {
		return verCuentaDesgUnifEnabled;
	}

	public void setVerCuentaDesgUnifEnabled(boolean verCuentaDesgUnifEnabled) {
		this.verCuentaDesgUnifEnabled = verCuentaDesgUnifEnabled;
	}

	public boolean isVerCuentaRelEnabled() {
		return verCuentaRelEnabled;
	}

	public void setVerCuentaRelEnabled(boolean verCuentaRelEnabled) {
		this.verCuentaRelEnabled = verCuentaRelEnabled;
	}

	public List<LiqConvenioCuentaAdapter> getListConvenioCuentaAdapter() {
		return listConvenioCuentaAdapter;
	}

	public void setListConvenioCuentaAdapter(List<LiqConvenioCuentaAdapter> listConvenios) {
		this.listConvenioCuentaAdapter = listConvenios;
	}

    public Double getTotalDeudaCyQ() {
		return totalDeudaCyQ;
	}

	public void setTotalDeudaCyQ(Double totalDeudaCyQ) {
		this.totalDeudaCyQ = totalDeudaCyQ;
	}

	public Double getTotalDeudaProcurador() {
		return totalDeudaProcurador;
	}

	public void setTotalDeudaProcurador(Double totalDeudaProcurador) {
		this.totalDeudaProcurador = totalDeudaProcurador;
	}
	
	public ProcedimientoVO getProcedimiento() {
		return procedimiento;
	}

	public void setProcedimiento(ProcedimientoVO procedimiento) {
		this.procedimiento = procedimiento;
	}

	public Boolean getMostrarColumnaSeleccionar() {
		return mostrarColumnaSeleccionar;
	}

	public void setMostrarColumnaSeleccionar(Boolean mostrarColumnaSeleccionar) {
		this.mostrarColumnaSeleccionar = mostrarColumnaSeleccionar;
	}

	public Boolean getMostrarColumnaSolicitar() {
		return mostrarColumnaSolicitar;
	}

	public void setMostrarColumnaSolicitar(Boolean mostrarColumnaSolicitar) {
		this.mostrarColumnaSolicitar = mostrarColumnaSolicitar;
	}

	public Boolean getMostrarColumnaVer() {
		return mostrarColumnaVer;
	}

	public void setMostrarColumnaVer(Boolean mostrarColumnaVer) {
		this.mostrarColumnaVer = mostrarColumnaVer;
	}

	public boolean isPoseeDeudaProcurador() {
		return poseeDeudaProcurador;
	}

	public void setPoseeDeudaProcurador(boolean poseeDeudaProcurador) {
		this.poseeDeudaProcurador = poseeDeudaProcurador;
	}

	public Boolean getMostrarChkAllAdmin() {
		return mostrarChkAllAdmin;
	}

	public void setMostrarChkAllAdmin(Boolean mostrarChkAllAdmin) {
		this.mostrarChkAllAdmin = mostrarChkAllAdmin;
	}


	public AnulacionVO getAnulacion() {
		return anulacion;
	}
	public void setAnulacion(AnulacionVO anulacion) {
		this.anulacion = anulacion;
	}

	public List<LiqDeudaAnuladaVO> getListBlockDeudaAnulada() {
		return listBlockDeudaAnulada;
	}
	public void setListBlockDeudaAnulada(List<LiqDeudaAnuladaVO> listBlockDeudaAnulada) {
		this.listBlockDeudaAnulada = listBlockDeudaAnulada;
	}

	public List<MotAnuDeuVO> getListMotAnuDeu() {
		return listMotAnuDeu;
	}
	public void setListMotAnuDeu(List<MotAnuDeuVO> listMotAnuDeu) {
		this.listMotAnuDeu = listMotAnuDeu;
	}
	
	public boolean isCambioPlanCDMEnabled() {
		return cambioPlanCDMEnabled;
	}
	public void setCambioPlanCDMEnabled(boolean cambioPlanCDMEnabled) {
		this.cambioPlanCDMEnabled = cambioPlanCDMEnabled;
	}
	
	public boolean isDdjjEntVenHabEnabled() {
		return ddjjEntVenHabEnabled;
	}
	
	public void setDdjjEntVenHabEnabled(boolean ddjjEntVenHabEnabled) {
		this.ddjjEntVenHabEnabled = ddjjEntVenHabEnabled;
	}
	
	public boolean isVerNovedadesRSEnabled() {
		return verNovedadesRSEnabled;
	}
	
	public void setVerNovedadesRSEnabled(boolean verNovedadesRSEnabled) {
		this.verNovedadesRSEnabled = verNovedadesRSEnabled;
	}
	
	public boolean isVolantePagoIntRSEnabled() {
		return volantePagoIntRSEnabled;
	}

	public void setVolantePagoIntRSEnabled(boolean volantePagoIntRSEnabled) {
		this.volantePagoIntRSEnabled = volantePagoIntRSEnabled;
	}

	public boolean isFormalizarConvenioEnabled() {
		return formalizarConvenioEnabled;
	}
	public void setFormalizarConvenioEnabled(boolean formalizarConvenioEnabled) {
		this.formalizarConvenioEnabled = formalizarConvenioEnabled;
	}

	public boolean isFormalizarConvenioEspEnabled() {
		return formalizarConvenioEspEnabled;
	}
	public void setFormalizarConvenioEspEnabled(boolean formalizarConvenioEspEnabled) {
		this.formalizarConvenioEspEnabled = formalizarConvenioEspEnabled;
	}

	public boolean isImprimirInformeDeudaEnabled() {
		return imprimirInformeDeudaEnabled;
	}
	public void setImprimirInformeDeudaEnabled(boolean imprimirInformeDeudaEnabled) {
		this.imprimirInformeDeudaEnabled = imprimirInformeDeudaEnabled;
	}

	public boolean isInfDeudaEscribanoEnabled() {
		return infDeudaEscribanoEnabled;
	}
	public void setInfDeudaEscribanoEnabled(boolean infDeudaEscribanoEnabled) {
		this.infDeudaEscribanoEnabled = infDeudaEscribanoEnabled;
	}

	public boolean isReconfeccionarEnabled() {
		return reconfeccionarEnabled;
	}
	public void setReconfeccionarEnabled(boolean reconfeccionarEnabled) {
		this.reconfeccionarEnabled = reconfeccionarEnabled;
	}
	
	public boolean isReconfeccionarEspEnabled() {
		return reconfeccionarEspEnabled;
	}

	public void setReconfeccionarEspEnabled(boolean reconfeccionarEspEnabled) {
		this.reconfeccionarEspEnabled = reconfeccionarEspEnabled;
	}

	public boolean isVerDetalleObjImpEnabled() {
		return verDetalleObjImpEnabled;
	}
	public void setVerDetalleObjImpEnabled(boolean verDetalleObjImpEnabled) {
		this.verDetalleObjImpEnabled = verDetalleObjImpEnabled;
	}

	public boolean isVerHistoricoContribEnabled() {
		return verHistoricoContribEnabled;
	}
	public void setVerHistoricoContribEnabled(boolean verHistoricoContribEnabled) {
		this.verHistoricoContribEnabled = verHistoricoContribEnabled;
	}
	
	public boolean isCambioPlanCDMVisible() {
		return cambioPlanCDMVisible;
	}
	public void setCambioPlanCDMVisible(boolean cambioPlanCDMVisible) {
		this.cambioPlanCDMVisible = cambioPlanCDMVisible;
	}

	public boolean isDdjjEntVenHabVisible() {
		return ddjjEntVenHabVisible;
	}
	
	public void setDdjjEntVenHabVisible(boolean ddjjEntVenHabVisible) {
		this.ddjjEntVenHabVisible = ddjjEntVenHabVisible;
	}
	
	public boolean isVerNovedadesRSVisible() {
		return verNovedadesRSVisible;
	}
	
	public void setVerNovedadesRSVisible(boolean verNovedadesRSVisible) {
		this.verNovedadesRSVisible = verNovedadesRSVisible;
	}

	public boolean isVolantePagoIntRSVisible() {
		return volantePagoIntRSVisible;
	}

	public void setVolantePagoIntRSVisible(boolean volantePagoIntRSVisible) {
		this.volantePagoIntRSVisible = volantePagoIntRSVisible;
	}

	public boolean isFormalizarConvenioEspVisible() {
		return formalizarConvenioEspVisible;
	}
	public void setFormalizarConvenioEspVisible(boolean formalizarConvenioEspVisible) {
		this.formalizarConvenioEspVisible = formalizarConvenioEspVisible;
	}

	public boolean isFormalizarConvenioVisible() {
		return formalizarConvenioVisible;
	}
	public void setFormalizarConvenioVisible(boolean formalizarConvenioVisible) {
		this.formalizarConvenioVisible = formalizarConvenioVisible;
	}

	public boolean isGenerarEstadoCuentaVisible() {
		return generarEstadoCuentaVisible;
	}
	public void setGenerarEstadoCuentaVisible(boolean generarEstadoCuentaVisible) {
		this.generarEstadoCuentaVisible = generarEstadoCuentaVisible;
	}

	public boolean isImprimirInformeDeudaVisible() {
		return imprimirInformeDeudaVisible;
	}
	public void setImprimirInformeDeudaVisible(boolean imprimirInformeDeudaVisible) {
		this.imprimirInformeDeudaVisible = imprimirInformeDeudaVisible;
	}

	public boolean isInfDeudaEscribanoVisible() {
		return infDeudaEscribanoVisible;
	}
	public void setInfDeudaEscribanoVisible(boolean infDeudaEscribanoVisible) {
		this.infDeudaEscribanoVisible = infDeudaEscribanoVisible;
	}

	public boolean isReconfeccionarVisible() {
		return reconfeccionarVisible;
	}
	public void setReconfeccionarVisible(boolean reconfeccionarVisible) {
		this.reconfeccionarVisible = reconfeccionarVisible;
	}

	public boolean isReconfeccionarEspVisible() {
		return reconfeccionarEspVisible;
	}

	public void setReconfeccionarEspVisible(boolean reconfeccionarEspVisible) {
		this.reconfeccionarEspVisible = reconfeccionarEspVisible;
	}

	public boolean isCuotaSaldoCDMEnabled() {
		return cuotaSaldoCDMEnabled;
	}
	public void setCuotaSaldoCDMEnabled(boolean cuotaSaldoCDMEnabled) {
		this.cuotaSaldoCDMEnabled = cuotaSaldoCDMEnabled;
	}

	public boolean isCuotaSaldoCDMVisible() {
		return cuotaSaldoCDMVisible;
	}
	public void setCuotaSaldoCDMVisible(boolean cuotaSaldoCDMVisible) {
		this.cuotaSaldoCDMVisible = cuotaSaldoCDMVisible;
	}

	public boolean isBuzonCambiosEnabled() {
		return buzonCambiosEnabled;
	}
	public void setBuzonCambiosEnabled(boolean buzonCambiosEnabled) {
		this.buzonCambiosEnabled = buzonCambiosEnabled;
	}

	public boolean isVerHistoricoExeEnabled() {
		return verHistoricoExeEnabled;
	}
	public void setVerHistoricoExeEnabled(boolean verHistoricoExeEnabled) {
		this.verHistoricoExeEnabled = verHistoricoExeEnabled;
	}

	public boolean isDesglosarAjusteVisible() {
		return desglosarAjusteVisible;
	}
	public void setDesglosarAjusteVisible(boolean desglosarAjusteVisible) {
		this.desglosarAjusteVisible = desglosarAjusteVisible;
	}

	public boolean isDesglosarAjusteEnabled() {
		return desglosarAjusteEnabled;
	}
	public void setDesglosarAjusteEnabled(boolean desglosarAjusteEnabled) {
		this.desglosarAjusteEnabled = desglosarAjusteEnabled;
	}

	public boolean isImprimirCierreComercioVisible() {
		return imprimirCierreComercioVisible;
	}

	public void setImprimirCierreComercioVisible(
			boolean imprimirCierreComercioVisible) {
		this.imprimirCierreComercioVisible = imprimirCierreComercioVisible;
	}

	public boolean isImprimirCierreComercioEnabled() {
		return imprimirCierreComercioEnabled;
	}

	public void setImprimirCierreComercioEnabled(
			boolean imprimirCierreComercioEnabled) {
		this.imprimirCierreComercioEnabled = imprimirCierreComercioEnabled;
	}
	
	public List<EstadoPeriodo> getListEstadoPeriodo() {
		return listEstadoPeriodo;
	}
	public void setListEstadoPeriodo(List<EstadoPeriodo> listEstadoPeriodo) {
		this.listEstadoPeriodo = listEstadoPeriodo;
	}

	public List<RecClaDeuVO> getListRecClaDeu() {
		return listRecClaDeu;
	}
	public void setListRecClaDeu(List<RecClaDeuVO> listRecClaDeu) {
		this.listRecClaDeu = listRecClaDeu;
	}
	
	public boolean isCierreComercioVisible() {
		return cierreComercioVisible;
	}
	public void setCierreComercioVisible(boolean cierreComercioVisible) {
		this.cierreComercioVisible = cierreComercioVisible;
	}

	public boolean isCierreComercioEnabled() {
		return cierreComercioEnabled;
	}
	public void setCierreComercioEnabled(boolean cierreComercioEnabled) {
		this.cierreComercioEnabled = cierreComercioEnabled;
	}

	public boolean isDecJurMasivaVisible() {
		return decJurMasivaVisible;
	}
	public void setDecJurMasivaVisible(boolean decJurMasivaVisible) {
		this.decJurMasivaVisible = decJurMasivaVisible;
	}

	public boolean isDecJurMasivaEnabled() {
		return decJurMasivaEnabled;
	}
	public void setDecJurMasivaEnabled(boolean decJurMasivaEnabled) {
		this.decJurMasivaEnabled = decJurMasivaEnabled;
	}

	public boolean isReclamarAcentVisible() {
		return reclamarAcentVisible;
	}
	public void setReclamarAcentVisible(boolean reclamarAcentVisible) {
		this.reclamarAcentVisible = reclamarAcentVisible;
	}

	public boolean isReclamarAcentEnabled() {
		return reclamarAcentEnabled;
	}
	public void setReclamarAcentEnabled(boolean reclamarAcentEnabled) {
		this.reclamarAcentEnabled = reclamarAcentEnabled;
	}

	public boolean isVerPlanillaEnabled() {
		return verPlanillaEnabled;
	}
	public void setVerPlanillaEnabled(boolean verPlanillaEnabled) {
		this.verPlanillaEnabled = verPlanillaEnabled;
	}

	// view getters
	public String getTotalView(){
		return (total!=null?StringUtil.redondearDecimales(total, 1, 2):"");
	}
	
	public String getTotalDeudaCyQView(){
		return (totalDeudaCyQ!=null?StringUtil.redondearDecimales(totalDeudaCyQ, 1, 2):"");
	}
	
	public String getTotalDeudaProcuradorView(){
		return (totalDeudaProcurador!=null?StringUtil.redondearDecimales(totalDeudaProcurador, 1, 2):"");
	}

	// Metodos de calculo
	/**
	 * Calcula el total de la deuda, y ademas setea el total de la deudaCyQ y el total de la deudaProcurador
	 */
	public Double calcularTotal(){
		Double total = 0D;
		Double totalDeudaCyQ = 0D;
		Double totalDeudaProcurador = 0D;
		
		// Totalizacion de deuda en CyQ
		for (LiqDeudaCyQVO procedimientoCyQ:this.getListProcedimientoCyQ()){
			totalDeudaCyQ += new Double(procedimientoCyQ.getSubTotal());
		}		
		
		// Totalizacion de deuda en Gestion Judicial
		for(LiqDeudaProcuradorVO deudaProcurador:this.getListProcurador()){
			totalDeudaProcurador += new Double(deudaProcurador.getSubTotal());
		}
		
		// Totalizacion de la deuda en via Administrativa
		for (LiqDeudaAdminVO deudaAdmin:this.getListGestionDeudaAdmin()){
			total += new Double(deudaAdmin.getSubTotal());
		}
		
		total += totalDeudaCyQ + totalDeudaProcurador;
		setTotalDeudaCyQ(totalDeudaCyQ);
		setTotalDeudaProcurador(totalDeudaProcurador);
		
		return total;
	}
	
	/**
	 * 
	 * Si se encuentra logueado un procurador, y la deuda consultada no le corresponde
	 * Mostramos el mendaje.
	 * 
	 * Ademas se setea la bandere PoseeDeudaProcurador
	 * 
	 * @author Cristian
	 * @param idProcurador
	 */
	public void setMsgProcurador(){
		this.addMessage(GdeError.MSG_DEUDA_NO_ASIGNADA_A_PROCURADOR);
	}
	
	// Metodos para SINC
	public String getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}

	public Date getFechaEnvioCyq() {
		return fechaEnvioCyq;
	}

	
	public void setFechaEnvioCyq(Date fechaEnvioCyq) {
		this.fechaEnvioCyq = fechaEnvioCyq;
		this.fechaEnvioCyqView = DateUtil.formatDate(fechaEnvioCyq, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaEnvioCyqView() {
		return fechaEnvioCyqView;
	}

	public void setFechaEnvioCyqView(String fechaEnvioCyqView) {
		this.fechaEnvioCyqView = fechaEnvioCyqView;
	}

	public Long getIdProcedimiento() {
		return idProcedimiento;
	}

	public void setIdProcedimiento(Long idProcedimiento) {
		this.idProcedimiento = idProcedimiento;
	}

	public List getListProcedimiento() {
		return listProcedimiento;
	}

	public void setListProcedimiento(List listProcedimiento) {
		this.listProcedimiento = listProcedimiento;
	}

	public String[] getListIdDeudaSelected() {
		return listIdDeudaSelected;
	}

	public void setListIdDeudaSelected(String[] listIdDeudaSelected) {
		this.listIdDeudaSelected = listIdDeudaSelected;
	}

	/**
	 * Devuelve true si posee permiso para al menos una de la acciones de ver historico
	 * 
	 * @return boolean
	 */
	public boolean isPoseePermisoAHistorico() {
	
		if (verDetalleObjImpEnabled || verHistoricoContribEnabled || verHistoricoExeEnabled)
			return true;
		else
			return false;
	}

	public boolean isSeleccionarDeuda4Cyq() {
		return seleccionarDeuda4Cyq;
	}
	public void setSeleccionarDeuda4Cyq(boolean seleccionarDeuda4Cyq) {
		this.seleccionarDeuda4Cyq = seleccionarDeuda4Cyq;
	}

	public boolean isSeleccionarCuenta4Cyq() {
		return seleccionarCuenta4Cyq;
	}
	public void setSeleccionarCuenta4Cyq(boolean seleccionarCuenta4Cyq) {
		this.seleccionarCuenta4Cyq = seleccionarCuenta4Cyq;
	}

	public boolean isPoseeConvenio() {
		return poseeConvenio;
	}
	public void setPoseeConvenio(boolean poseeConvenio) {
		this.poseeConvenio = poseeConvenio;
	}

	public boolean isPoseeDeudaAdmin4Cyq() {
		return poseeDeudaAdmin4Cyq;
	}
	public void setPoseeDeudaAdmin4Cyq(boolean poseeDeudaAdmin4Cyq) {
		this.poseeDeudaAdmin4Cyq = poseeDeudaAdmin4Cyq;
	}

	public boolean isPoseeDeudaJudicial4Cyq() {
		return poseeDeudaJudicial4Cyq;
	}
	public void setPoseeDeudaJudicial4Cyq(boolean poseeDeudaJudicial4Cyq) {
		this.poseeDeudaJudicial4Cyq = poseeDeudaJudicial4Cyq;
	}

	public boolean getSimularSalPorCad() {
		return simularSalPorCad;
	}
	public void setSimularSalPorCad(boolean simularSalPorCad) {
		this.simularSalPorCad = simularSalPorCad;
	}
	
	public SiNo getContinuaGesViaOri() {
		return continuaGesViaOri;
	}

	public void setContinuaGesViaOri(SiNo continuaGesViaOri) {
		this.continuaGesViaOri = continuaGesViaOri;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public Long getIdCuenta() {
		return getCuenta().getIdCuenta();
	}
	public void setIdCuenta(Long idCuenta) {
		this.getCuenta().setIdCuenta(idCuenta);
	}
	
	public Long getIdRecurso() {
		return getCuenta().getIdRecurso();
	}
	public void setIdRecurso(Long idRecurso) {
		this.getCuenta().setIdRecurso(idRecurso);
	}

	public String getNumeroCuenta() {
		return getCuenta().getNumeroCuenta();
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.getCuenta().setNumeroCuenta(numeroCuenta);
	}

	public String getCodGesPer() {
		return getCuenta().getCodGesPer();
	}
	public void setCodGesPer(String codGesPer) {
		this.getCuenta().setCodGesPer(codGesPer);
	}

	public Long getIdObjImp() {
		return getCuenta().getIdObjImp();
	}
	public void setIdObjImp(Long idObjImp) {
		this.getCuenta().setIdObjImp(idObjImp);
	}

	public RecursoVO getRecurso() {
		return getCuenta().getRecurso();
	}
	public void setRecurso(RecursoVO recurso) {
		this.getCuenta().setRecurso(recurso);
	}

	public Boolean getCodGesPerRequerido() {
		return codGesPerRequerido;
	}

	public void setCodGesPerRequerido(Boolean codGesPerRequerido) {
		this.codGesPerRequerido = codGesPerRequerido;
	}

	/**
	 * @return the verCambioDomicilio
	 */
	public boolean isVerCambioDomicilio() {
		return verCambioDomicilio;
	}

	/**
	 * @param verCambioDomicilio the verCambioDomicilio to set
	 */
	public void setVerCambioDomicilio(boolean verCambioDomicilio) {
		this.verCambioDomicilio = verCambioDomicilio;
	}
	
}