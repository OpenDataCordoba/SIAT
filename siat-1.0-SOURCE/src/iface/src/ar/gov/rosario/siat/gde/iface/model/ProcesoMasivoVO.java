//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.PasoCorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.SiNoLuego;

public class ProcesoMasivoVO extends SiatBussImageModel {
	private static final long serialVersionUID = 0;

	public static String ADP_PARAM_PROCPLAMAP = "procPlaMap"; // para armar los links de constancias y planillas 
	
	private Date      fechaEnvio;  
	private RecursoVO recurso = new RecursoVO(); 
	private TipProMasVO tipProMas = new TipProMasVO(); 
	private String    observacion;
	private SelAlmDeudaVO  selAlmInc = new SelAlmDeudaVO();
	private SelAlmDeudaVO  selAlmExc = new SelAlmDeudaVO();
	private SiNo      utilizaCriterio = SiNo.OpcionNula;
	private ProcuradorVO procurador = new ProcuradorVO();
	// Considera cuentas excluidas seleccionadas
	private SiNo   conCuentaExcSel = SiNo.OpcionNula;  
	private CasoVO caso = new CasoVO();
	private SiNo   esVueltaAtras = SiNo.OpcionNula;  // NOT NULL
	private ProcesoMasivoVO procesoMasivo = null;    // para evitar la recursividad
	private CorridaVO corrida = new CorridaVO();
    private String    usuarioAlta;  
    private Boolean criterioProcuradorEnabled = false;
	private Boolean seleccionFormularioEnabled = false;
	private Date fechaReconfeccion;
	private String fechaReconfeccionView="";
	private Long enviadoContr; 
	private SiNoLuego generaConstancia = SiNoLuego.SI;  
	private Integer generaConstanciaPostEnvio; 

	private List<FileCorridaVO> listFileProcurador = new ArrayList<FileCorridaVO>(); //archivos para el procurador
    
	// Datos de los Reportes
	FormularioVO formulario = new FormularioVO();
	
	private List<ProMasProExcVO> listProMasProExc = new ArrayList<ProMasProExcVO>() ;

	private List<PlanillaVO> listPlanillaPlaEnvDeuPro = new ArrayList<PlanillaVO>() ;
	private List<PlanillaVO> listPlanillaConstanciaDeu = new ArrayList<PlanillaVO>() ;
    
	private String fechaEnvioView = "";
	
	// PasoCorrida que se origina en la seleccion almacenada incluida
	private PasoCorridaVO pasoCorridaSelAlm = null; // indica que no tiene el paso 

	// PasoCorrida que se origina en la asignacion de procuradores
	private PasoCorridaVO pasoCorridaAsigProc = null; // indica que no tiene el paso 

	// PasoCorrida que se origina en la realizacion del envio
	private PasoCorridaVO pasoCorridaRealizarEnvio = null; // indica que no tiene el paso 

	private ViaDeudaVO viaDeuda = new ViaDeudaVO();

	// business flag
	private Boolean admProcesoBussEnabled          = Boolean.TRUE;
	private Boolean agregarProMasProExcBussEnabled = Boolean.TRUE;
	
	private Boolean verReportesDeudaIncBussEnabled = Boolean.TRUE;
	private Boolean verReportesDeudaExcBussEnabled = Boolean.TRUE;
	
	// indica que existen ProMasDeuInc asociadas al proceso masivo: utilizado para habilitar el Activar de la realizacion del envio
	private Boolean contieneDeudasIncluidas = Boolean.TRUE;
	
	private Boolean viaDeudaBussEnabled = Boolean.TRUE;
	
    // lista de Reportes de deuda incluida y excluida
    private List<PlanillaVO> listReportesDeudaIncluida = new ArrayList<PlanillaVO>();
    
    private List<PlanillaVO> listReportesDeudaExcluida = new ArrayList<PlanillaVO>();
    
    private List<FileCorridaVO> listFileCorridaRealizarEnvio = new ArrayList<FileCorridaVO>();
    
    
    
	public ProcesoMasivoVO() {
		super();
	}
	
	public ProcesoMasivoVO(TipProMasVO tipProMasVO) {
		this();
		this.tipProMas = tipProMasVO;
	}
	
	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	public SiNo getConCuentaExcSel() {
		return conCuentaExcSel;
	}
	public void setConCuentaExcSel(SiNo conCuentaExcSel) {
		this.conCuentaExcSel = conCuentaExcSel;
	}
	public CorridaVO getCorrida() {
		return corrida;
	}
	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
	}
	public ProcesoMasivoVO getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivoVO procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
	public SiNo getEsVueltaAtras() {
		return esVueltaAtras;
	}
	public void setEsVueltaAtras(SiNo esVueltaAtras) {
		this.esVueltaAtras = esVueltaAtras;
	}
	public Date getFechaEnvio() {
		return fechaEnvio;
	}
	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
		this.fechaEnvioView = DateUtil.formatDate(fechaEnvio, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public ProcuradorVO getProcurador() {
		return procurador;
	}
	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public TipProMasVO getTipProMas() {
		return tipProMas;
	}
	public void setTipProMas(TipProMasVO tipProMas) {
		this.tipProMas = tipProMas;
	}
	public SelAlmDeudaVO getSelAlmExc() {
		return selAlmExc;
	}
	public void setSelAlmExc(SelAlmDeudaVO selAlmExc) {
		this.selAlmExc = selAlmExc;
	}
	public SelAlmDeudaVO getSelAlmInc() {
		return selAlmInc;
	}
	public void setSelAlmInc(SelAlmDeudaVO selAlmInc) {
		this.selAlmInc = selAlmInc;
	}
	public String getUsuarioAlta() {
		return usuarioAlta;
	}
	public void setUsuarioAlta(String usuarioAlta) {
		this.usuarioAlta = usuarioAlta;
	}
	public SiNo getUtilizaCriterio() {
		return utilizaCriterio;
	}
	public void setUtilizaCriterio(SiNo utilizaCriterio) {
		this.utilizaCriterio = utilizaCriterio;
	}
	public String getFechaEnvioView() {
		return fechaEnvioView;
	}
	public void setFechaEnvioView(String fechaEnvioView) {
		this.fechaEnvioView = fechaEnvioView;
	}
	public List<ProMasProExcVO> getListProMasProExc() {
		return listProMasProExc;
	}
	public void setListProMasProExc(List<ProMasProExcVO> listProMasProExc) {
		this.listProMasProExc = listProMasProExc;
	}
	public List<PlanillaVO> getListReportesDeudaExcluida() {
		return listReportesDeudaExcluida;
	}
	public void setListReportesDeudaExcluida(
			List<PlanillaVO> listReportesDeudaExcluida) {
		this.listReportesDeudaExcluida = listReportesDeudaExcluida;
	}

	public List<PlanillaVO> getListReportesDeudaIncluida() {
		return listReportesDeudaIncluida;
	}
	public void setListReportesDeudaIncluida(
			List<PlanillaVO> listReportesDeudaIncluida) {
		this.listReportesDeudaIncluida = listReportesDeudaIncluida;
	}

	public Boolean getAdmProcesoBussEnabled() {
		return admProcesoBussEnabled;
	}
	public void setAdmProcesoBussEnabled(Boolean admProcesoBussEnabled) {
		this.admProcesoBussEnabled = admProcesoBussEnabled;
	}
	public Boolean getAgregarProMasProExcBussEnabled() {
		return agregarProMasProExcBussEnabled;
	}
	public void setAgregarProMasProExcBussEnabled(
			Boolean agregarProMasProExcBussEnabled) {
		this.agregarProMasProExcBussEnabled = agregarProMasProExcBussEnabled;
	}
	public Boolean getVerReportesDeudaExcBussEnabled() {
		return verReportesDeudaExcBussEnabled;
	}
	public void setVerReportesDeudaExcBussEnabled(
			Boolean verReportesDeudaExcBussEnabled) {
		this.verReportesDeudaExcBussEnabled = verReportesDeudaExcBussEnabled;
	}
	public Boolean getVerReportesDeudaIncBussEnabled() {
		return verReportesDeudaIncBussEnabled;
	}
	public void setVerReportesDeudaIncBussEnabled(
			Boolean verReportesDeudaIncBussEnabled) {
		this.verReportesDeudaIncBussEnabled = verReportesDeudaIncBussEnabled;
	}
	public Boolean getViaDeudaBussEnabled() {
		return viaDeudaBussEnabled;
	}
	public void setViaDeudaBussEnabled(Boolean viaDeudaBussEnabled) {
		this.viaDeudaBussEnabled = viaDeudaBussEnabled;
	}
	public PasoCorridaVO getPasoCorridaAsigProc() {
		return pasoCorridaAsigProc;
	}
	public void setPasoCorridaAsigProc(PasoCorridaVO pasoCorridaAsigProc) {
		this.pasoCorridaAsigProc = pasoCorridaAsigProc;
	}
	public PasoCorridaVO getPasoCorridaRealizarEnvio() {
		return pasoCorridaRealizarEnvio;
	}
	public void setPasoCorridaRealizarEnvio(PasoCorridaVO pasoCorridaRealizarEnvio) {
		this.pasoCorridaRealizarEnvio = pasoCorridaRealizarEnvio;
	}
	public PasoCorridaVO getPasoCorridaSelAlm() {
		return pasoCorridaSelAlm;
	}
	public void setPasoCorridaSelAlm(PasoCorridaVO pasoCorridaSelAlm) {
		this.pasoCorridaSelAlm = pasoCorridaSelAlm;
	}
	public List<FileCorridaVO> getListFileCorridaRealizarEnvio() {
		return listFileCorridaRealizarEnvio;
	}
	public void setListFileCorridaRealizarEnvio(
			List<FileCorridaVO> listFileCorridaRealizarEnvio) {
		this.listFileCorridaRealizarEnvio = listFileCorridaRealizarEnvio;
	}
	public Boolean getContieneDeudasIncluidas() {
		return contieneDeudasIncluidas;
	}
	public void setContieneDeudasIncluidas(Boolean contieneDeudasIncluidas) {
		this.contieneDeudasIncluidas = contieneDeudasIncluidas;
	}
	public ViaDeudaVO getViaDeuda() {
		return viaDeuda;
	}
	public void setViaDeuda(ViaDeudaVO viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

	/**
	 * carga la Habilitacion de la deuda a incluir y excluir 
	 * @param habilitarDeudaIncExc
	 */
	public void setHabilitarDeudaIncExc(boolean habilitarDeudaIncExc ){
		this.getSelAlmInc().setSeleccionarDeudaEnviarBussEnabled(habilitarDeudaIncExc);
		this.getSelAlmInc().setEliminarDeudaEnviarBussEnabled(habilitarDeudaIncExc);
		this.getSelAlmInc().setLimpiarSeleccionDeudaEnviarBussEnabled(habilitarDeudaIncExc);
		//this.getSelAlmInc().setPlanillaDeudaEnviarBussEnabled(habilitarDeudaIncExc);
		//this.getSelAlmInc().setConsultarDeudaEnviarBussEnabled(habilitarDeudaIncExc);
		
		this.getSelAlmExc().setSeleccionarDeudaEnviarBussEnabled(habilitarDeudaIncExc);
		this.getSelAlmExc().setEliminarDeudaEnviarBussEnabled(habilitarDeudaIncExc);
		this.getSelAlmExc().setLimpiarSeleccionDeudaEnviarBussEnabled(habilitarDeudaIncExc);
		//this.getSelAlmExc().setPlanillaDeudaEnviarBussEnabled(habilitarDeudaIncExc);
		//this.getSelAlmExc().setConsultarDeudaEnviarBussEnabled(habilitarDeudaIncExc);
		// Nota: Logs de armado siempre habilitados
	}

	/**
	 * Obtiene la cantidad total de registros que contiene cada planilla de deuda incluida
	 * @return String
	 */
	public String getCtdTotalRegistrosDeudaIncluidaView(){
		Long ctdTotalResultados = 0L;
		for (PlanillaVO planillaVO : this.getListReportesDeudaIncluida()) {
			ctdTotalResultados += planillaVO.getCtdResultados();
		}
		return StringUtil.formatLong(ctdTotalResultados);
	}

	/**
	 * Obtiene la cantidad total de registros que contiene cada planilla de deuda excluida
	 * @return String
	 */
	public String getCtdTotalRegistrosDeudaExcluidaView(){
		Long ctdTotalResultados = 0L;
		for (PlanillaVO planillaVO : this.getListReportesDeudaExcluida()) {
			ctdTotalResultados += planillaVO.getCtdResultados();
		}
		return StringUtil.formatLong(ctdTotalResultados);
	}
	
	/**
	 * Habilita la seleccion de datos de asignacino a procuradores.
	 * Por ahora solo esta disponible si tipo de porceso es Envio Judicial
	 * Definido por el negocio
	 */
	public Boolean getCriterioProcuradorEnabled() {
		return criterioProcuradorEnabled;
	}
	public void setCriterioProcuradorEnabled(Boolean criterioProcuradorEnabled) {
		this.criterioProcuradorEnabled = criterioProcuradorEnabled;
	}

	/**
	 * Habilita la seleccion de formulario de salida
	 */
	public Boolean getSeleccionFormularioEnabled() {
		return seleccionFormularioEnabled;
	}
	public void setSeleccionFormularioEnabled(Boolean seleccionFormularioEnabled) {
		this.seleccionFormularioEnabled = seleccionFormularioEnabled;
	}

	public FormularioVO getFormulario() {
		return formulario;
	}

	public void setFormulario(FormularioVO formulario) {
		this.formulario = formulario;
	}

	public void setFechaReconfeccionView(String fechaReconfeccionView) {
		this.fechaReconfeccionView = fechaReconfeccionView;
	}

	public String getFechaReconfeccionView() {
		return fechaReconfeccionView;
	}

	public Date getFechaReconfeccion() {
		return fechaReconfeccion;
	}

	public void setFechaReconfeccion(Date fechaReconfeccion) {
		this.fechaReconfeccion = fechaReconfeccion;
	}

	public void setEnviadoContr(Long enviadoContr) {
		this.enviadoContr = enviadoContr;
	}

	public Long getEnviadoContr() {
		return enviadoContr;
	}

	public String getEnviadoContrSiNo() {
		return new Long(1).equals(enviadoContr) ? "Si" : "No";
	}
	
	public String getRepresent() {
		String s = "";
		s += this.getId() + " - ";
		s += this.getRecurso().getCodRecurso() + " ";
		s += this.getFechaEnvioView() + " ";
		if (this.getEnviadoContr() != null && this.getEnviadoContr() == 1) {
			s += "(Enviado Contr.)"; 
		}
		return s;
	}

	public void setListFileProcurador(List<FileCorridaVO> listFileProcurador) {
		this.listFileProcurador = listFileProcurador;
	}

	public List<FileCorridaVO> getListFileProcurador() {
		return listFileProcurador;
	}

	public void setGeneraConstancia(SiNoLuego generaConstancia) {
		this.generaConstancia = generaConstancia;
	}

	public SiNoLuego getGeneraConstancia() {
		return generaConstancia;
	}

	/** 
	 * Solo es posible generar constancias en envios para recursos no TGI 
	 */
	public Boolean getGeneraConstanciaEnabled() {
		return this.tipProMas.getId() == 1L;
		
		/* esta bandera se usaba para hacer que no aparezca la opcion de generacion de 
		 * constancias en el recurso TGI.
		 * si se quiere restaurar esta funcionalidad, descomentar el codigo de abajo
		 * y asegurarse de setear valor de generaConstancia en NO para TGI.
		 */
		/*
		if (this.tipProMas.getId() == 1L //envio judicial
				&& this.recurso != null 
				&& this.recurso.getId() != null
				&& this.recurso.getId() != -1L
				&& this.recurso.getId() != 14L) {
			return true;
		} 
		return false;
		*/
	}

	public void setListPlanillaPlaEnvDeuPro(List<PlanillaVO> listPlanillaPlaEnvDeuPro) {
		this.listPlanillaPlaEnvDeuPro = listPlanillaPlaEnvDeuPro;
	}

	public List<PlanillaVO> getListPlanillaPlaEnvDeuPro() {
		return listPlanillaPlaEnvDeuPro;
	}

	public void setListPlanillaConstanciaDeu(
			List<PlanillaVO> listPlanillaConstanciaDeu) {
		this.listPlanillaConstanciaDeu = listPlanillaConstanciaDeu;
	}

	public List<PlanillaVO> getListPlanillaConstanciaDeu() {
		return listPlanillaConstanciaDeu;
	}

	public void setGeneraConstanciaPostEnvio(Integer generaConstanciaPostEnvio) {
		this.generaConstanciaPostEnvio = generaConstanciaPostEnvio;
	}

	public Integer getGeneraConstanciaPostEnvio() {
		return generaConstanciaPostEnvio;
	}
	
}
