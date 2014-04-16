//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import ar.gov.rosario.siat.rec.iface.model.ObraVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Search Page de Seleccion de Parametros al proceso 
 * @author tecso
 *
 */
public class SelAlmAgregarParametrosSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "selAlmAgregarParametrosSearchPageVO";
	
	public static final String ID_SEL_ALM_INC = "idSelAlmInc";
	
	public static final String CANTIDAD_MINIMA_DEUDA = "cantidadMinimaDeuda";
	public static final String APLICA_AL_TOTAL_DEUDA = "aplicaAlTotalDeuda";
	public static final String IMPORTE_HISTORICO_DESDE = "importeHistoricoDesde";
	public static final String IMPORTE_HISTORICO_HASTA = "importeHistoricoHasta";
	public static final String ID_RECURSO = "idRecurso";
	public static final String ID_TIP_OBJ_IMP = "idTipObjImp";
	public static final String NUMERO_CUENTA = "numeroCuenta";
	
	public static final String IDS_REC_CLA_DEU = "idsRecClaDeu";
	public static final String IDS_EXENCIONES_SI = "idsExencionesSi";
	public static final String IDS_EXENCIONES_NO = "idsExencionesNo";

	public static final String IDS_ATRIBUTOS_SI = "idsAtributosSi";
	public static final String IDS_ATRIBUTOS_NO = "idsAtributosNo";

	
	public static final String FECHA_VENCIMIENTO_DESDE = "fechaVencimientoDesde";
	public static final String FECHA_VENCIMIENTO_HASTA = "fechaVencimientoHasta";

	public static final String ID_OBRA = "idObra";
	
	//public static final String APLICA_FILTRO_OBJ_IMP = "aplicaFiltroObjImp";
	public static final String DETALLE_LOG = "detalleLog";
	public static final String ID_TIPO_PROC_MAS = "idTipoProcMas";
	
	// Parametro para pasar el id de la Corrida seleccionada
	public static final String ID_CORRIDA = "IdCorrida";
	
	// Parametro para pasar el id del TipoSelAlmDet seleccionada
	public static final String ID_TIPO_SEL_ALM_DET = "tipoSelAlmDet";
	
	public static final String ID_VIA_DEUDA = "idViaDeuda";
	
	// parametros de cuotas de convenios
	public static final String FECHA_VENCIMIENTO = "fechaVencimiento";
	public static final String CANTIDAD_CUOTAS_PLAN = "cantidadCuotasPlan";

	public static final String ID_PERSONA = "idContribuyente";
	
	// TipoSelAlmDet
	private List<TipoSelAlmVO> listTipoSelAlmDet = new ArrayList<TipoSelAlmVO>();
	private TipoSelAlmVO tipoSelAlmDet = new TipoSelAlmVO();
	
	
	// Seleccion Almacenada: 
	private SelAlmVO selAlm = new SelAlmVO(); 
	
	private CorridaVO corrida = new CorridaVO();
	
	// parametros de la deuda
	private RecursoVO recurso = new RecursoVO();
	
	// Clasificacion de la deuda:
	private List<RecClaDeuVO> listRecClaDeu = new ArrayList<RecClaDeuVO>();
	private String[] listIdRecClaDeu = {};  // String[] es lo mas adecuado para Struts
	
	private Date   fechaVencimientoDesde;
	private String fechaVencimientoDesdeView;
	private Date   fechaVencimientoHasta;
	private String fechaVencimientoHastaView;
	private Double importeHistoricoDesde;
	private String importeHistoricoDesdeView;
	
	private Double importeHistoricoHasta;
	private String importeHistoricoHastaView;
	private Double importeActualizadoDesde;
	private String importeActualizadoDesdeView;
	private Double importeActualizadoHasta;
	private String importeActualizadoHastaView;
	
	private List<SiNo> listAplicaAlTotalDeuda = SiNo.getList(SiNo.OpcionSelecionar);
	private SiNo       aplicaAlTotalDeuda = SiNo.OpcionSelecionar;
	private Long   cantidadMinimaDeuda;
	private String cantidadMinimaDeudaView;
	private Boolean esEnvioJudicial = false;
	
	// parametros de la cuenta
	private CuentaVO cuenta = new CuentaVO();
	private List<ObraVO> listObra = new ArrayList<ObraVO>();
	private ObraVO obra = new ObraVO();

	// parametros de las exenciones
	private List<ExencionVO> listExencion  = new ArrayList<ExencionVO>();
	private List<SiNo> listSiNoExencion    = SiNo.getList(SiNo.OpcionTodo);
	private Map   exencionesSeleccionadas  = new HashMap();
	 
	// parametros del contribuyente
	private PersonaVO persona = new PersonaVO();
	private List<AtributoVO> listAtributo = new ArrayList<AtributoVO>();
	private List<SiNo> listSiNoAtributo   = SiNo.getList(SiNo.OpcionTodo);
	private Map<String,String>   atributosSeleccionados  = new HashMap<String,String>();
	
	// parametros de objeto imponible
	private TipObjImpDefinition tipObjImpDefinition = new TipObjImpDefinition();
	private Map<String,String> paramObjImp = new HashMap<String, String>();
	
	// parametros de convenioCuota
	private Date   fechaVencimiento;
	private String fechaVencimientoView;
	private Long   cantidadCuotasPlan;
	private String cantidadCuotasPlanView;
	
	// Para TGI y CdM: mostrar los atributos Zona y Radio del TipObjImp parcela
	// Cada uno con los checks correspondientes valores del 
	
	private Boolean verParametrosTipoSelAlmDetBussEnabled = true;
	private Boolean verParametrosDeudaBussEnabled         = true;
	private Boolean verParametrosConvenioBussEnabled      = true;
	
	// Constructor
	public SelAlmAgregarParametrosSearchPage(){
		super(GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO);
	}
	
	public static Date getFechaVencimientoLimite(){
		return DateUtil.getDate(DateUtil.getAnioActual() - 2, 11, 31); // mes doce
	}
	public static String getFechaVencimientoLimiteView(){
		return DateUtil.formatDate(SelAlmAgregarParametrosSearchPage.getFechaVencimientoLimite(), 
				DateUtil.ddSMMSYYYY_MASK);
	}

	// Getters y Setters
	
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public SelAlmVO getSelAlm() {
		return selAlm;
	}
	public void setSelAlm(SelAlmVO selAlm) {
		this.selAlm = selAlm;
	}

	public SiNo getAplicaAlTotalDeuda() {
		return aplicaAlTotalDeuda;
	}
	public void setAplicaAlTotalDeuda(SiNo aplicaAlTotalDeuda) {
		this.aplicaAlTotalDeuda = aplicaAlTotalDeuda;
	}
	public Long getCantidadMinimaDeuda() {
		return cantidadMinimaDeuda;
	}
	public void setCantidadMinimaDeuda(Long cantidadMinimaDeuda) {
		this.cantidadMinimaDeuda = cantidadMinimaDeuda;
		this.cantidadMinimaDeudaView = StringUtil.formatLong(cantidadMinimaDeuda);
	}
	public String getCantidadMinimaDeudaView() {
		return cantidadMinimaDeudaView;
	}
	public void setCantidadMinimaDeudaView(String cantidadMinimaDeudaView) {
		this.cantidadMinimaDeudaView = cantidadMinimaDeudaView;
	}
	public Long getCantidadCuotasPlan() {
		return cantidadCuotasPlan;
	}
	public void setCantidadCuotasPlan(Long cantidadCuotasPlan) {
		this.cantidadCuotasPlan = cantidadCuotasPlan;
		this.cantidadCuotasPlanView = StringUtil.formatLong(cantidadCuotasPlan);
	}
	public String getCantidadCuotasPlanView() {
		return cantidadCuotasPlanView;
	}
	public void setCantidadCuotasPlanView(String cantidadCuotasPlanView) {
		this.cantidadCuotasPlanView = cantidadCuotasPlanView;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	
	public Date getFechaVencimientoDesde() {
		return fechaVencimientoDesde;
	}
	public void setFechaVencimientoDesde(Date fechaVencimientoDesde) {
		this.fechaVencimientoDesde = fechaVencimientoDesde;
		this.fechaVencimientoDesdeView = DateUtil.formatDate(fechaVencimientoDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaVencimientoDesdeView() {
		return fechaVencimientoDesdeView;
	}
	public void setFechaVencimientoDesdeView(String fechaVencimientoDesdeView) {
		this.fechaVencimientoDesdeView = fechaVencimientoDesdeView;
	}
	public Date getFechaVencimientoHasta() {
		return fechaVencimientoHasta;
	}
	public void setFechaVencimientoHasta(Date fechaVencimientoHasta) {
		this.fechaVencimientoHasta = fechaVencimientoHasta;
		this.fechaVencimientoHastaView = DateUtil.formatDate(fechaVencimientoHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaVencimientoHastaView() {
		return fechaVencimientoHastaView;
	}
	public void setFechaVencimientoHastaView(String fechaVencimientoHastaView) {
		this.fechaVencimientoHastaView = fechaVencimientoHastaView;
	}
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
		this.fechaVencimientoView = DateUtil.formatDate(fechaVencimiento, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaVencimientoView() {
		return fechaVencimientoView;
	}
	public void setFechaVencimientoView(String fechaVencimientoView) {
		this.fechaVencimientoView = fechaVencimientoView;
	}
	public Double getImporteActualizadoDesde() {
		return importeActualizadoDesde;
	}
	public void setImporteActualizadoDesde(Double importeActualizadoDesde) {
		this.importeActualizadoDesde = importeActualizadoDesde;
		this.importeActualizadoDesdeView = StringUtil.formatDouble(importeActualizadoDesde);
	}
	public String getImporteActualizadoDesdeView() {
		return importeActualizadoDesdeView;
	}
	public void setImporteActualizadoDesdeView(String importeActualizadoDesdeView) {
		this.importeActualizadoDesdeView = importeActualizadoDesdeView;
	}
	public Double getImporteActualizadoHasta() {
		return importeActualizadoHasta;
	}
	public void setImporteActualizadoHasta(Double importeActualizadoHasta) {
		this.importeActualizadoHasta = importeActualizadoHasta;
		this.importeActualizadoHastaView = StringUtil.formatDouble(importeActualizadoHasta);
	}
	public String getImporteActualizadoHastaView() {
		return importeActualizadoHastaView;
	}
	public void setImporteActualizadoHastaView(String importeActualizadoHastaView) {
		this.importeActualizadoHastaView = importeActualizadoHastaView;
	}
	public Double getImporteHistoricoDesde() {
		return importeHistoricoDesde;
	}
	public void setImporteHistoricoDesde(Double importeHistoricoDesde) {
		this.importeHistoricoDesde = importeHistoricoDesde;
		this.importeHistoricoDesdeView = StringUtil.formatDouble(importeHistoricoDesde);
	}
	public String getImporteHistoricoDesdeView() {
		return importeHistoricoDesdeView;
	}
	public void setImporteHistoricoDesdeView(String importeHistoricoDesdeView) {
		this.importeHistoricoDesdeView = importeHistoricoDesdeView;
	}
	public Double getImporteHistoricoHasta() {
		return importeHistoricoHasta;
	}
	public void setImporteHistoricoHasta(Double importeHistoricoHasta) {
		this.importeHistoricoHasta = importeHistoricoHasta;
		this.importeHistoricoHastaView = StringUtil.formatDouble(importeHistoricoHasta);
	}
	public String getImporteHistoricoHastaView() {
		return importeHistoricoHastaView;
	}
	public void setImporteHistoricoHastaView(String importeHistoricoHastaView) {
		this.importeHistoricoHastaView = importeHistoricoHastaView;
	}
	public List<SiNo> getListAplicaAlTotalDeuda() {
		return listAplicaAlTotalDeuda;
	}
	public void setListAplicaAlTotalDeuda(List<SiNo> listAplicaAlTotalDeuda) {
		this.listAplicaAlTotalDeuda = listAplicaAlTotalDeuda;
	}
	public List<ObraVO> getListObra() {
		return listObra;
	}
	public void setListObra(List<ObraVO> listObra) {
		this.listObra = listObra;
	}
	public ObraVO getObra() {
		return obra;
	}
	public void setObra(ObraVO obra) {
		this.obra = obra;
	}
	public List<RecClaDeuVO> getListRecClaDeu() {
		return listRecClaDeu;
	}
	public void setListRecClaDeu(List<RecClaDeuVO> listRecClaDeu) {
		this.listRecClaDeu = listRecClaDeu;
	}
	public void clearListRecClaDeu() {
		this.listRecClaDeu.clear();
	}
	public String[] getListIdRecClaDeu() {
		return listIdRecClaDeu;
	}
	public void setListIdRecClaDeu(String[] listIdRecClaDeu) {
		this.listIdRecClaDeu = listIdRecClaDeu;
	}
	public List<ExencionVO> getListExencion() {
		return listExencion;
	}
	public void setListExencion(List<ExencionVO> listExencion) {
		this.listExencion = listExencion;
	}
	public List<SiNo> getListSiNoExencion() {
		return listSiNoExencion;
	}
	public void setListSiNoExencion(List<SiNo> listSiNoExencion) {
		this.listSiNoExencion = listSiNoExencion;
	}
	public Map getExencionesSeleccionadas() {
		return exencionesSeleccionadas;
	}
	public void setExencionesSeleccionadas(Map exencionesSeleccionadas) {
		this.exencionesSeleccionadas = exencionesSeleccionadas;
	}
	public void clearExencionesSeleccionadas() {
		this.getExencionesSeleccionadas().clear();
	}
	
	public Map<String,String> getAtributosSeleccionados() {
		return atributosSeleccionados;
	}
	public void setAtributosSeleccionados(Map atributosSeleccionados) {
		this.atributosSeleccionados = atributosSeleccionados;
	}
	public List<AtributoVO> getListAtributo() {
		return listAtributo;
	}
	public void setListAtributo(List<AtributoVO> listAtributo) {
		this.listAtributo = listAtributo;
	}
	public List<SiNo> getListSiNoAtributo() {
		return listSiNoAtributo;
	}
	public void setListSiNoAtributo(List<SiNo> listSiNoAtributo) {
		this.listSiNoAtributo = listSiNoAtributo;
	}
	public void clearAtributosSeleccionados() {
		this.getAtributosSeleccionados().clear();
	}
	public List<TipoSelAlmVO> getListTipoSelAlmDet() {
		return listTipoSelAlmDet;
	}
	public void setListTipoSelAlmDet(List<TipoSelAlmVO> listTipoSelAlmDet) {
		this.listTipoSelAlmDet = listTipoSelAlmDet;
	}
	public TipoSelAlmVO getTipoSelAlmDet() {
		return tipoSelAlmDet;
	}
	public void setTipoSelAlmDet(TipoSelAlmVO tipoSelAlmDet) {
		this.tipoSelAlmDet = tipoSelAlmDet;
	}
	public Boolean getEsEnvioJudicial() {
		return esEnvioJudicial;
	}
	public void setEsEnvioJudicial(Boolean esEnvioJudicial) {
		this.esEnvioJudicial = esEnvioJudicial;
	}
	public TipObjImpDefinition getTipObjImpDefinition() {
		return tipObjImpDefinition;
	}
	public void setTipObjImpDefinition(TipObjImpDefinition tipObjImpDefinition) {
		this.tipObjImpDefinition = tipObjImpDefinition;
	}
	public CorridaVO getCorrida() {
		return corrida;
	}
	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
	}

	public Boolean getVerParametrosConvenioBussEnabled() {
		return verParametrosConvenioBussEnabled;
	}
	public void setVerParametrosConvenioBussEnabled(
			Boolean verParametrosConvenioBussEnabled) {
		this.verParametrosConvenioBussEnabled = verParametrosConvenioBussEnabled;
	}
	public Boolean getVerParametrosDeudaBussEnabled() {
		return verParametrosDeudaBussEnabled;
	}
	public void setVerParametrosDeudaBussEnabled(
			Boolean verParametrosDeudaBussEnabled) {
		this.verParametrosDeudaBussEnabled = verParametrosDeudaBussEnabled;
	}
	public Boolean getVerParametrosTipoSelAlmDetBussEnabled() {
		return verParametrosTipoSelAlmDetBussEnabled;
	}
	public void setVerParametrosTipoSelAlmDetBussEnabled(
			Boolean verParametrosTipoSelAlmDetBussEnabled) {
		this.verParametrosTipoSelAlmDetBussEnabled = verParametrosTipoSelAlmDetBussEnabled;
	}

	public Map<String, String> getParamObjImp() {
		return paramObjImp;
	}
	public void setParamObjImp(Map<String, String> paramObjImp) {
		this.paramObjImp = paramObjImp;
	}
	public void clearParamObjImp() {
		this.getParamObjImp().clear();
	}

	public Object getDynaExcencion(String key)   {
	    return this.getExencionesSeleccionadas().get(key);
	}
	public void setDynaExcencion(String key, Object value)  {
	    this.getExencionesSeleccionadas().put(key, value);
	}
	

	public List<String> getListExencionSI(){
		return this.getListValores(this.getExencionesSeleccionadas(), SiNo.SI);
	}

	public List<String> getListExencionNO(){
		return this.getListValores(this.getExencionesSeleccionadas(), SiNo.NO);
	}

	public Object getDynaAtributo(String key)   {
	    return this.getAtributosSeleccionados().get(key);
	}
	public void setDynaAtributo(String key, String value)  {
	    this.getAtributosSeleccionados().put(key, value);
	}

	public List<String> getListAtributoSI(){
		return this.getListValores(this.getAtributosSeleccionados(), SiNo.SI);
	}

	public List<String> getListAtributoNO(){
		return this.getListValores(this.getAtributosSeleccionados(), SiNo.NO);
	}

	private List<String> getListValores(Map<String,String> mapaValores, SiNo sino){
		
		List<String> listAtributo = new ArrayList<String>();
		
		for (Iterator iter = mapaValores.keySet().iterator(); iter.hasNext();) {
			String clave = (String) iter.next();
			String valor = (String) mapaValores.get(clave);
			if (StringUtil.formatInteger(sino.getId()).equals(valor)){
				listAtributo.add(clave);
			}
		}
		return listAtributo;
	}

	public synchronized PersonaVO getPersona() {
		return persona;
	}

	public synchronized void setPersona(PersonaVO persona) {
		this.persona = persona;
	}

}
