//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ar.gov.rosario.siat.bal.buss.bean.Canal;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Categoria;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.exe.buss.bean.CueExeCache;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.buss.dao.ProMasDeuExcDAO;
import ar.gov.rosario.siat.gde.buss.dao.ProMasDeuIncDAO;
import ar.gov.rosario.siat.gde.iface.model.DeudaExcProMasAgregarSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaExcProMasEliminarSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaIncProMasEliminarSearchPage;
import ar.gov.rosario.siat.gde.iface.model.LiqAtrValorVO;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqCuotaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqNotificacionPrinter;
import ar.gov.rosario.siat.gde.iface.model.LiqNotificacionVO;
import ar.gov.rosario.siat.gde.iface.model.LiqReciboVO;
import ar.gov.rosario.siat.gde.iface.model.ProMasProExcVO;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.AsignaRepartidor;
import ar.gov.rosario.siat.pad.buss.bean.Broche;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaTitular;
import ar.gov.rosario.siat.pad.buss.bean.ExentaAreaCache;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.buss.bean.PersonaFacade;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.FileCorrida;
import ar.gov.rosario.siat.pro.buss.bean.PasoCorrida;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import ar.gov.rosario.siat.rec.buss.bean.Obra;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.SplitedFileWriter;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TablaVO;

/**
 * Envio de deuda a cobro a judiciales
 * 
 */
@Entity
@Table(name = "gde_procesoMasivo")
public class ProcesoMasivo extends BaseBO {

	public class ProMasPrinter {
		public ProMasPrinter() {}
		public PrintModel print;
		public Writer writer;
		public Writer txtWriter;
		public String writerFilename;
		public String txtFilename;
	}

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(ProcesoMasivo.class);
	
	public static final String FILE_NAME_DEUDA_ENV_PROC = "deudaEnviadaProcuradores";
	public static final String FILE_NAME_DEUDA_EXC_PROC = "deudaExcluidaProcuradores";
	public static final String FILE_NAME_DEUDA_ENV_POR_PROC = "deudaEnviadaPorProcurador";
	public static final String FILE_NAME_DEUDA_ENV_POR_PERIODO = "deudaEnviadaPorPeriodo";
	public static final String FILE_NAME_PLANILLA_PROC = "planillaProcurador";

	public static final String COD_FRM_PROCESOMASIVO_TOTALES = "PROCESOMASIVO_TOTALES";


	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name = "idTipProMas") 
	private TipProMas tipProMas;

	@Column(name = "fechaEnvio") 
	private Date fechaEnvio;     // DATETIME YEAR TO DAY NOT NULL

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idRecurso") 
	private Recurso recurso;  // NOT NULL

	@Column(name = "observacion") 
	private String observacion; // VARCHAR(255)

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idSelAlmInc") 
	private SelAlmDeuda selAlmInc;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idSelAlmExc") 
	private SelAlmDeuda selAlmExc;

	@Column(name = "utilizaCriterio")
	private Integer utilizaCriterio;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idProcurador") 
	private Procurador procurador;

	// Considera cuentas excluidas seleccionadas
	@Column(name = "conCuentaExcSel")
	private Integer conCuentaExcSel;  // NOT NULL ( en la base es nuleable y en la interfaz es requerido)

    @Column(name="idCaso") 
	private String idCaso;

	@Column(name = "esVueltaAtras")
	private Integer esVueltaAtras;  // NOT NULL
	
	@Column(name="fechaReconfeccion")
	private Date fechaReconfeccion;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idProcesoMasivo") 
	private ProcesoMasivo procesoMasivo;

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idCorrida") 
	private Corrida corrida;

	@Column(name = "usuarioAlta")
	private String    usuarioAlta; // CHAR(10) NOT NULL 

	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idProcesoMasivo")
	private List<ProMasProExc> listProMasProExc;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idViaDeuda") 
	private ViaDeuda viaDeuda;
	
	@Column(name = "enviadoContr")
	private Long enviadoContr;

	@Column(name = "generaConstancia")
	private Integer generaConstancia;  //solo valida cuando no es TGI
	
	@Transient
	private String errorMessage = "";

	// Cache de cueExe vigentes
	@Transient
	private CueExeCache cueExeCache;

	// mapa de broches exentos, almacena idBroche, desBroche
	// sin un broche esta en este mapa es que tiene el flag exentoEnvioJub en true
	// esto causa que las cuentas con este broche no tiene que ser enviada a judicial
	@Transient
	private Map<Long, String> mapBrocheExentoEnvioJud = new HashMap<Long, String>();
	
	// Mapa de todas las deudas cargadas en gde_selalmdet de excluision.
	// suelen ser pocas o ninguna, y este mapa hace que que se eviten 
	// miles de query en el segundo paso
	@Transient
	private Map<String, String> mapDeudasSelAlmExc = new HashMap<String, String>();;

	@Transient
	private ExentaAreaCache exentaAreaCache; 
	
	// Contructores 
	public ProcesoMasivo() {
		super();
	}
	
	public static ProcesoMasivo getByIdSelAlmInc (Long idSelAlmInc)throws Exception{
		return GdeDAOFactory.getProcesoMasivoDAO().getByIdSelAlmInc(idSelAlmInc);
	}

	// Getters y Setters

	/**
	 * Indica si este proceso realiza la asignacion de deuda a procuradores..
	 * Segun el tipo de poceso masivo. Por ahora solo valido es true para el Envio de Deuda Judicial
	 */
	public Boolean getCriterioProcuradorEnabled() {
		if (this.getTipProMas() == null)
			return false;
		return this.getTipProMas().getId().equals(TipProMas.ID_ENVIO_JUDICIAL);
	}

	public TipProMas getTipProMas() {
		return tipProMas;
	}
	public void setTipProMas(TipProMas tipProMas) {
		this.tipProMas = tipProMas;
	}

	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}
	
	public Corrida getCorrida() {
		return corrida;
	}
	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}
	public ProcesoMasivo getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivo procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
	public Date getFechaEnvio() {
		return fechaEnvio;
	}
	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Procurador getProcurador() {
		return procurador;
	}
	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}
	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}
	public SelAlmDeuda getSelAlmExc() {
		return selAlmExc;
	}
	public void setSelAlmExc(SelAlmDeuda selAlmExc) {
		this.selAlmExc = selAlmExc;
	}
	public SelAlmDeuda getSelAlmInc() {
		return selAlmInc;
	}
	public void setSelAlmInc(SelAlmDeuda selAlmInc) {
		this.selAlmInc = selAlmInc;
	}
	public String getUsuarioAlta() {
		return usuarioAlta;
	}
	public void setUsuarioAlta(String usuarioAlta) {
		this.usuarioAlta = usuarioAlta;
	}
	public Integer getUtilizaCriterio() {
		return utilizaCriterio;
	}
	public void setUtilizaCriterio(Integer utilizaCriterio) {
		this.utilizaCriterio = utilizaCriterio;
	}
	public Integer getConCuentaExcSel() {
		return conCuentaExcSel;
	}
	public void setConCuentaExcSel(Integer conCuentaExcSel) {
		this.conCuentaExcSel = conCuentaExcSel;
	}
	public Integer getEsVueltaAtras() {
		return esVueltaAtras;
	}
	public void setEsVueltaAtras(Integer esVueltaAtras) {
		this.esVueltaAtras = esVueltaAtras;
	}
	public List<ProMasProExc> getListProMasProExc() {
		return listProMasProExc;
	}
	public void setListProMasProExc(List<ProMasProExc> listProMasProExc) {
		this.listProMasProExc = listProMasProExc;
	}
	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}
	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
	}
	public Date getFechaReconfeccion() {
		return fechaReconfeccion;
	}

	public void setFechaReconfeccion(Date fechaReconfeccion) {
		this.fechaReconfeccion = fechaReconfeccion;
	}

	// Metodos de clase
	public static ProcesoMasivo getById(Long id) {
		return (ProcesoMasivo) GdeDAOFactory.getProcesoMasivoDAO().getById(id);
	}

	public static ProcesoMasivo getByIdNull(Long id) {
		return (ProcesoMasivo) GdeDAOFactory.getProcesoMasivoDAO().getByIdNull(id);
	}

	public static List<ProcesoMasivo> getList() {
		return (ArrayList<ProcesoMasivo>) GdeDAOFactory.getProcesoMasivoDAO().getList();
	}

	public static List<ProcesoMasivo> getListActivos() {			
		return (ArrayList<ProcesoMasivo>) GdeDAOFactory.getProcesoMasivoDAO().getListActiva();
	}

	/**
	 * Obtiene la lista de ProcesoMasivo para el Reporte de Respuesta de Operativos 
	 * <p><i>Solo Tipo Proceso: Pre Envio Judicial (id=2), Reconfeccion Masiva (id=3)</i></p>
	 *  
	 * @return List<ProcesoMasivo>
	 */
	public static List<ProcesoMasivo> getListForReporte(Long idTipProMas) {			
		return (ArrayList<ProcesoMasivo>) GdeDAOFactory.getProcesoMasivoDAO().getListForReporte(idTipProMas);
	}
	/**
	 * Obtiene la lista de ProcesoMasivo para un recurso y la lista de estados de corrida 
	 * @param recurso
	 * @param listEstadoCorrida lista de estados de corrida del envio Judicial
	 * @return List<ProcesoMasivo>
	 */
	public static List<ProcesoMasivo> getListByRecursoListEstadoCorrida(Recurso recurso, List<EstadoCorrida> listEstadoCorrida) {			
		return (ArrayList<ProcesoMasivo>) GdeDAOFactory.getProcesoMasivoDAO().getListByRecursoListEstadoCorrida(recurso, listEstadoCorrida);
	}

	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author 
	 */
	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();

		// Validaciones de requeridos y unicidad comunes
		this.validate();

		if (hasError()) {
			return false;
		}

		// No existe otro envio para el mismo recurso con estado "En Preparacion", "En espera comenzar", "Procesando" o "En espera continual".
		List<EstadoCorrida> listEstadoCorrida = new ArrayList<EstadoCorrida>();

		listEstadoCorrida.add(EstadoCorrida.getById(EstadoCorrida.ID_EN_PREPARACION));
		listEstadoCorrida.add(EstadoCorrida.getById(EstadoCorrida.ID_EN_ESPERA_COMENZAR));
		listEstadoCorrida.add(EstadoCorrida.getById(EstadoCorrida.ID_PROCESANDO));
		listEstadoCorrida.add(EstadoCorrida.getById(EstadoCorrida.ID_EN_ESPERA_CONTINUAR));
		List<ProcesoMasivo> listProMasDuplicadas = ProcesoMasivo.getListByRecursoListEstadoCorrida(this.getRecurso(), listEstadoCorrida);

		if (listProMasDuplicadas.size() > 0){
			log.debug("ATENCION: habilitar cuando terminemos las pruebas"); // TODO
			//addRecoverableError(GdeError.PROCESO_MASIVO_RECURSOS_ESTADOSCORRIDA);
		}

		return !hasError();
	}

	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();

		// Validaciones de requeridos y unicidad comunes
		this.validate();

		if (hasError()) {
			return false;
		}

		Long idEstadoCorrida = this.getCorrida().getEstadoCorrida().getId();
		
		// si es preEnvio o reconf y el estado es != de ID_EN_ESPERA_COMENZAR y ID_PROCESANDO, se puede modificar
		boolean esPreEnvio = getTipProMas().getEsPreEnvioJudicial();
		boolean esReconf = getTipProMas().getEsReconfeccion();
		boolean enEstadoPermitdo = (!idEstadoCorrida.equals(EstadoCorrida.ID_EN_ESPERA_COMENZAR) &&
									!idEstadoCorrida.equals(EstadoCorrida.ID_PROCESANDO));

		if(esPreEnvio  || esReconf){
			if(!enEstadoPermitdo)		
				addRecoverableError(GdeError.PROCESO_MASIVO_MODIFICAR_NO_PERMITIDO);
		}else{
			// Solo se puede si el estado del envio es "En Preparacion" o "En Espera comenzar".
			boolean modificarBussEnabled = (EstadoCorrida.ID_EN_PREPARACION.equals(idEstadoCorrida)  || 
					EstadoCorrida.ID_EN_ESPERA_COMENZAR.equals(idEstadoCorrida) 	); 
			if (!modificarBussEnabled){
				addRecoverableError(GdeError.PROCESO_MASIVO_MODIFICAR_NO_PERMITIDO);
			}
		}

		return !hasError();
	}


	private boolean validate(){
		//limpiamos la lista de errores
		clearError();

		//Validaciones de VO

		// fechaEnvio
		if(this.fechaEnvio == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROCESO_MASIVO_FECHA_ENVIO);
		}
		// TipProMas 
		if(this.tipProMas == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROCESO_MASIVO_TIPPROMAS);
		}

		// 
		if(this.generaConstancia == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROCESO_MASIVO_GENERACONSTANCIA);
		}

		
		// Recurso 
		if(this.recurso == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROCESO_MASIVO_RECURSO);
		}
		if(this.conCuentaExcSel == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROCESO_MASIVO_CONCUENTAEXCSEL);
		}
		if(this.esVueltaAtras == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROCESO_MASIVO_ESVUELTAATRAS);
		}
		if(StringUtil.isNullOrEmpty(this.getUsuarioAlta())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROCESO_MASIVO_USUARIOALTA);
		}
		if(this.viaDeuda == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROCESO_MASIVO_VIADEUDA);
		}

		if (hasError()) {
			return false;
		}

		//Validaciones de Negocio
		if (this.getCriterioProcuradorEnabled()) {
			if(this.utilizaCriterio == null){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROCESO_MASIVO_UTILIZACRITERIO);
			}
			if(SiNo.NO.getId().equals(this.utilizaCriterio) && this.getProcurador() == null) {
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PROCESO_MASIVO_PROCURADOR);
			}
		}


		return (!hasError());
	}

	/**
	 * Valida la eliminacion
	 * @author 
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();

		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		// Solo se puede eliminar si el estado del envio es "En Preparacion".
		Long idEstadoCorrida = this.getCorrida().getEstadoCorrida().getId();
		boolean eliminarBussEnabled = (EstadoCorrida.ID_EN_PREPARACION.equals(idEstadoCorrida) ); 
		if (!eliminarBussEnabled){
			addRecoverableError(GdeError.PROCESO_MASIVO_ELIMINAR_NO_PERMITIDO);
		}

		return !hasError();
	}

	//	 Metodos de negocio

	// --> ABM PlaEnvDeuPro
	/**
	 * Realiza la creacion de la Planilla de Envio de Deuda al Procurador, a partir del Procurador y del Envio Judicial
	 * @param  procurador
	 * @return PlaEnvDeuPro
	 * @throws Exception
	 */ 
	public PlaEnvDeuPro createPlaEnvDeuPro(Procurador procurador) throws Exception{
		
			PlaEnvDeuPro plaEnvDeuPro = new PlaEnvDeuPro();
			
			//anioPlanilla: el aÃ±o de la fecha de envÃ­o
			Integer anio = DateUtil.getAnio(this.getFechaEnvio());
			plaEnvDeuPro.setAnioPlanilla(anio);
			//nroPlanilla: no es incremental por recurso del envio.
			plaEnvDeuPro.setNroPlanilla(PlaEnvDeuPro.getNextNroPlanilla());
			//idProcurador: el id del procurador.
			plaEnvDeuPro.setProcurador(procurador);
			//fechaEnvio: la fecha del envÃ­o
			plaEnvDeuPro.setFechaEnvio(this.getFechaEnvio());
			//fechaRecepcion: nula
			plaEnvDeuPro.setFechaRecepcion(null);
			//idProcesoMasivo: el id del envÃ­o
			plaEnvDeuPro.setProcesoMasivo(this);
			//idEstPlaEnvDeuPr: emitida
			plaEnvDeuPro.setEstPlaEnvDeuPr(EstPlaEnvDeuPr.getById(EstPlaEnvDeuPr.ID_EMITIDA));
			//inicializo los contadores en cero
			plaEnvDeuPro.setCantidadCuentas(0L);
			plaEnvDeuPro.setTotalRegistros(0L);
			plaEnvDeuPro.setImporteTotal(0D);
			
			plaEnvDeuPro.setIdCaso(this.getIdCaso());				
			plaEnvDeuPro.setObservaciones(this.getObservacion());
			
			return GdeGDeudaJudicialManager.getInstance().updatePlaEnvDeuPro(plaEnvDeuPro, HistEstPlaEnvDP.getLogEstado(plaEnvDeuPro.getEstPlaEnvDeuPr().getId()));
		}
	
	/**
	 * Actualiza la Planilla de Deuda de Envio a Procuradores y la creacion del Historico de la misma
	 * @param  plaEnvDeuPro
	 * @return PlaEnvDeuPro
	 * @throws Exception
	 */
	public PlaEnvDeuPro updatePlaEnvDeuPro(PlaEnvDeuPro plaEnvDeuPro) throws Exception {
		
		// TODO ver si hay que ponerle un logEstado especial. Como esta toma el de modificada, VER CLASE HistEstPlaEnvDP
		return GdeGDeudaJudicialManager.getInstance().updatePlaEnvDeuPro(plaEnvDeuPro);
	}	

	/**
	 * Elimina la Planilla de Deuda de Envio a Procuradores y la creacion del Historico de la misma
	 * @param  plaEnvDeuPro
	 * @return PlaEnvDeuPro
	 * @throws Exception
	 */
	public PlaEnvDeuPro deletePlaEnvDeuPro(PlaEnvDeuPro plaEnvDeuPro) throws Exception {
		
				// TODO ver ensambles de otras clases a la PlaEnvDeuPro
		return GdeGDeudaJudicialManager.getInstance().deletePlaEnvDeuPro(plaEnvDeuPro);
	}	
	// <-- ABM PlaEnvDeuPro
	
	
	// --> ABM SelAlmDetIn

	/**
	 * Borra la lista de SelAlmDet incluidas de acuerdo a los filtros en el SearchPage
	 * @param deudaIncProMasEliminarSearchPageVO
	 * @throws Exception
	 */
	public long deleteListSelAlmDet(DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPageVO) throws Exception {

		//TODO FACTORIZAR LAS VALIDACIONES con las de la busqueda de deuda a incluir
		// Validaciones de negocio

		SelAlmLog selAlmLog = new SelAlmLog();
		selAlmLog.setAccionLog(AccionLog.getById(AccionLog.ID_ELIMINAR_MASIVO));
		selAlmLog.setDetalleLog(deudaIncProMasEliminarSearchPageVO.getDetalleLog());
		selAlmLog = this.getSelAlmInc().createSelAlmLog(selAlmLog);
		if (selAlmLog.hasError()){
			selAlmLog.passErrorMessages(this);
			return 0;
		}

		return GdeDAOFactory.getSelAlmDetDAO().deleteListSelAlmDet(deudaIncProMasEliminarSearchPageVO);
	}	

	/**
	 * Borra la lista de SelAlmDet incluidas de acuerdo a la seleccion individual cargada en el SearchPage
	 * @param deudaIncProMasEliminarSearchPageVO
	 * @throws Exception
	 */
	public void deleteListSelAlmIncDetConSeleccionIndividual(DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPageVO) throws Exception {

		//TODO FACTORIZAR LAS VALIDACIONES con las de la busqueda de deuda a incluir
		// Validaciones de negocio

		SelAlmLog selAlmLog = new SelAlmLog();
		selAlmLog.setAccionLog(AccionLog.getById(AccionLog.ID_ELIMINAR_MASIVO_CON_CHECKS));
		selAlmLog.setDetalleLog(deudaIncProMasEliminarSearchPageVO.getDetalleLog());
		selAlmLog = this.getSelAlmInc().createSelAlmLog(selAlmLog);
		if (selAlmLog.hasError()){
			selAlmLog.passErrorMessages(this);
			return;
		}

		GdeDAOFactory.getSelAlmDetDAO().deleteListSelAlmDetByIds(deudaIncProMasEliminarSearchPageVO.getListIdSelAlmDet());
	}	

	// DEUDA EXCLUIDA

	/**
	 * Crea la lista de SelAlmDet de deuda excluida de acuerdo al SearchPage
	 * @param deudaExcProMasAgregarSearchPageVO
	 * @return long cantidad de SelAlmDet excluidos creados
	 * @throws Exception
	 */
	public long createListSelAlmDetExc(DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPageVO) throws Exception {

		// Validaciones de negocio

		/*
		if (!domAtrVal.validateCreate()) {
			return domAtrVal;
		}
		 */
		
		SelAlmLog selAlmLog = new SelAlmLog();
		selAlmLog.setAccionLog(AccionLog.getById(AccionLog.ID_AGREGAR_MASIVO));
		selAlmLog.setDetalleLog(deudaExcProMasAgregarSearchPageVO.getDetalleLog());
		selAlmLog = this.getSelAlmExc().createSelAlmLog(selAlmLog);
		if (selAlmLog.hasError()){
			log.debug("error al crear la SelAlmLog");
			selAlmLog.passErrorMessages(this);
			return 0;
		}
		
		ProcesoMasivo procesoMasivo = ProcesoMasivo.getById(deudaExcProMasAgregarSearchPageVO.getProcesoMasivo().getId());
		
		long ctd = GdeDAOFactory.getSelAlmDetDAO().createListSelAlmDetExc(procesoMasivo, deudaExcProMasAgregarSearchPageVO);
		 
		if(deudaExcProMasAgregarSearchPageVO.hasError()){
			log.debug("borrado de la SelAlmLog");
			GdeDAOFactory.getSelAlmLogDAO().delete(selAlmLog);
		}
		
		return ctd;
	}

	/**
	 * Crea la lista de SelAlmDet de deudas excluidas de acuerdo a la seleccion individual 
	 * @param deudaExcProMasAgregarSearchPage
	 * @return int ctd de SelAlmDet excluidos creados
	 * @throws Exception
	 */
	public int createListSelAlmDetExcConSeleccionIndividual(DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPage) throws Exception {

		//TODO FACTORIZAR LAS VALIDACIONES con las de la busqueda de deuda a excluir
		// Validaciones de negocio

		SelAlmLog selAlmLog = new SelAlmLog();
		selAlmLog.setAccionLog(AccionLog.getById(AccionLog.ID_AGREGAR_MASIVO_CON_CHECKS));
		selAlmLog.setDetalleLog(deudaExcProMasAgregarSearchPage.getDetalleLog());
		selAlmLog = this.getSelAlmExc().createSelAlmLog(selAlmLog);
		if (selAlmLog.hasError()){
			selAlmLog.passErrorMessages(this);
			return 0;
		}
		
		TipoSelAlm tipoSelAlmDet = null;
		if(this.getViaDeuda().getEsViaAdmin()){
			tipoSelAlmDet = TipoSelAlm.getTipoSelAlmDetDeudaAdm();
		}else{
			tipoSelAlmDet = TipoSelAlm.getTipoSelAlmDetDeudaJud();
		}
		
		return GdeDAOFactory.getSelAlmDetDAO().createListSelAlmDet(
				this.getSelAlmExc(), 
				deudaExcProMasAgregarSearchPage.getListIdDeudaAdmin(),
				tipoSelAlmDet);
	}	

	/**
	 * Borra la lista de SelAlmDet de la seleccion almanacenda excluida del envio judicial acuerdo a los filtros cargados en el SearchPage
	 * @param deudaExcProMasEliminarSearchPageVO
	 * @throws Exception
	 */
	public long deleteListSelAlmDet(DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPageVO) throws Exception {

		//TODO FACTORIZAR LAS VALIDACIONES con las de la busqueda de deuda a excluir
		// Validaciones de negocio

		SelAlmLog selAlmLog = new SelAlmLog();
		selAlmLog.setAccionLog(AccionLog.getById(AccionLog.ID_ELIMINAR_MASIVO));
		selAlmLog.setDetalleLog(deudaExcProMasEliminarSearchPageVO.getDetalleLog());
		selAlmLog = this.getSelAlmExc().createSelAlmLog(selAlmLog);
		if (selAlmLog.hasError()){
			selAlmLog.passErrorMessages(this);
			return 0;
		}

		return GdeDAOFactory.getSelAlmDetDAO().deleteListSelAlmDet(deudaExcProMasEliminarSearchPageVO);
	}	

	/**
	 * Borra la lista de SelAlmDet de la seleccion almanacenda excluida del envio judicial acuerdo a los ids cargados en el SearchPage 
	 * @param deudaExcProMasEliminarSearchPageVO
	 * @throws Exception
	 */
	public void deleteListSelAlmDetConSeleccionIndividual(DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPageVO) throws Exception {

		//TODO FACTORIZAR LAS VALIDACIONES con las de la busqueda de deuda a excluir
		// Validaciones de negocio

		SelAlmLog selAlmLog = new SelAlmLog();
		selAlmLog.setAccionLog(AccionLog.getById(AccionLog.ID_ELIMINAR_MASIVO_CON_CHECKS));
		selAlmLog.setDetalleLog(deudaExcProMasEliminarSearchPageVO.getDetalleLog());
		selAlmLog = this.getSelAlmInc().createSelAlmLog(selAlmLog);
		if (selAlmLog.hasError()){
			selAlmLog.passErrorMessages(this);
			return;
		}

		GdeDAOFactory.getSelAlmDetDAO().deleteListSelAlmDetByIds(deudaExcProMasEliminarSearchPageVO.getListIdSelAlmDet());
	}	

	/**
	 * ToVO adecuado para la administracion del proceso de envio a judicial
	 * @return ProcesoMasivoVO
	 * @throws Exception
	 */
	public ProcesoMasivoVO toVOForAdmProceso() throws Exception{

		ProcesoMasivoVO procesoMasivoVO = (ProcesoMasivoVO) this.toVO(1,false);
		
		// seteo del usuario de ult mdf
		procesoMasivoVO.setUsuario(this.getUsuarioUltMdf());

		//lista de procuradores Asignados
		procesoMasivoVO.setListProMasProExc(
				(ArrayList<ProMasProExcVO>) ListUtilBean.toVO(this.getListProMasProExc(),1));
		
		// siempre obtenemos la info de la corrida del procesoMasivo(difiere de la desc. del cus)
		// estadoCorrida de la corrida del proceso masivo.
		procesoMasivoVO.getCorrida().setEstadoCorrida((EstadoCorridaVO) this.getCorrida().getEstadoCorrida().toVO(0)); 
		
		procesoMasivoVO.setGeneraConstanciaPostEnvio(this.getGeneraConstancia());
		return procesoMasivoVO;
	}
	
	public void cargarTotales(ProcesoMasivoVO procesoMasivoVO, Boolean totalesExtendidos) throws Exception{

		// TOTALES SelAlmInc
		if(this.getTipProMas().getEsPreEnvioJudicial() || this.getTipProMas().getEsSeleccionDeuda()){
			// cargamos los totales basicos para el pre envio judicial y la seleccion de deudas
			// que trabajan con deudas y cuotas de convenios.
			this.cargarTotalesBasicosPreEnvJudSelecDeuda(procesoMasivoVO);
		}else{
			// tipo de proceso masivo envio judicial o reconfeccion: solo trabajan con deuda administrativa
			// Cantidad total de registros de incluida
			Long ctdTotalRegInc = this.getSelAlmInc().obtenerCantidadRegistros(TipoSelAlm.getTipoSelAlmDetDeudaAdm());
			String ctdTotalRegIncView = StringUtil.formatLong(ctdTotalRegInc);
			procesoMasivoVO.getSelAlmInc().setCantidadDeudasView(ctdTotalRegIncView);
			procesoMasivoVO.getSelAlmInc().setCantidadRegistrosView(ctdTotalRegIncView);

			// Cantidad total de registros de excluida: solo trabajan con deudas administrativas
			Long ctdTotalRegExc = this.getSelAlmExc().obtenerCantidadRegistros(TipoSelAlm.getTipoSelAlmDetDeudaAdm());
			String ctdTotalRegExcView = StringUtil.formatLong(ctdTotalRegExc); 
			procesoMasivoVO.getSelAlmExc().setCantidadDeudasView(ctdTotalRegExcView);
			procesoMasivoVO.getSelAlmExc().setCantidadRegistrosView(ctdTotalRegExcView);
		}

		if(totalesExtendidos == null || !totalesExtendidos ){
			return ;
		}
		
		// Calcula los TOTALES Extendidos de los Detalles de la seleccion almacenada incluida y excluida
		if(this.getTipProMas().getEsPreEnvioJudicial() || this.getTipProMas().getEsSeleccionDeuda()){
			this.cargarTotalesExtendidosPreEnvJudSelecDeuda(procesoMasivoVO);
			
		}else{ 
			// tipo de proceso masivo: envio judicial o reconfeccion que solo trabajan con deuda administrativa
			// Obtiene la cantidad de cuentas distintas existentes en la SelAlm
			procesoMasivoVO.getSelAlmInc().setCantidadCuentasDeudaView(
					StringUtil.formatLong(this.getSelAlmInc().obtenerCantidadCuentas(TipoSelAlm.getTipoSelAlmDetDeudaAdm())));
			
			// Sumatoria de saldos historicos de todos los registros de deuda administrativas existentes
			Double importeHistorico = this.getSelAlmInc().obtenerImporteHistorico(TipoSelAlm.getTipoSelAlmDetDeudaAdm());
			String importeHistoricoView = StringUtil.formatDouble(importeHistorico);
			procesoMasivoVO.getSelAlmInc().setImporteHistoricoDeudaView(importeHistoricoView);
			procesoMasivoVO.getSelAlmInc().setImporteHistoricoTotalView(importeHistoricoView);

			// no implementado Sumatoria de saldos de todos los registros de deuda existentes, actualizados a la fecha de envio

			// TOTALES SelAlmExc: solo trabaja con deudas
			// Obtiene la cantidad de cuentas distintas existentes en la SelAlm
			procesoMasivoVO.getSelAlmExc().setCantidadCuentasView(
					StringUtil.formatLong(this.getSelAlmExc().obtenerCantidadCuentas(TipoSelAlm.getTipoSelAlmDetDeudaAdm())));

			// Sumatoria de saldos historicos de todos los registros de deuda existentes
			procesoMasivoVO.getSelAlmExc().setImporteHistoricoTotalView(
					StringUtil.formatDouble(this.getSelAlmExc().obtenerImporteHistorico(TipoSelAlm.getTipoSelAlmDetDeudaAdm())));

			// no se realiza la Sumatoria de saldos de todos los registros de deuda existentes, actualizados a la fecha de envio
		}

		return;
	}
	
	private void cargarTotalesBasicosPreEnvJudSelecDeuda(ProcesoMasivoVO procesoMasivoVO) throws Exception{

		// TOTALES SelAlmInc
		Long ctdDeudasInc = 0L;
		Long ctdConveniosCuota = 0L;
		Long ctdDeudasExc = 0L;
		
		if (this.getViaDeuda().getEsViaAdmin()){
			// cantidad de deudas de la via administrativa del proceso masivo
			ctdDeudasInc = this.getSelAlmInc().obtenerCantidadRegistros(TipoSelAlm.getTipoSelAlmDetDeudaAdm());
			// cantidad de cuotas de convenio en via administrativa 
			ctdConveniosCuota = this.getSelAlmInc().obtenerCantidadRegistros(TipoSelAlm.getTipoSelAlmDetConvCuotAdm());
			// 
			ctdDeudasExc = this.getSelAlmExc().obtenerCantidadRegistros(TipoSelAlm.getTipoSelAlmDetDeudaAdm());
		}else{
			// cantidad de deudas de la via judicial del proceso masivo				
			ctdDeudasInc = this.getSelAlmInc().obtenerCantidadRegistros(TipoSelAlm.getTipoSelAlmDetDeudaJud());
			// cantidad de cuotas de convenio en via judicial
			ctdConveniosCuota = this.getSelAlmInc().obtenerCantidadRegistros(TipoSelAlm.getTipoSelAlmDetConvCuotJud());
			//
			ctdDeudasExc = this.getSelAlmExc().obtenerCantidadRegistros(TipoSelAlm.getTipoSelAlmDetDeudaJud());
		}
		procesoMasivoVO.getSelAlmInc().setCantidadDeudasView(StringUtil.formatLong(ctdDeudasInc));
		procesoMasivoVO.getSelAlmInc().setCantidadCuotasConvenioView(StringUtil.formatLong(ctdConveniosCuota));
		procesoMasivoVO.getSelAlmInc().setCantidadRegistrosView(StringUtil.formatLong(ctdDeudasInc + ctdConveniosCuota));

		// TOTALES SelAlmExc
		procesoMasivoVO.getSelAlmExc().setCantidadRegistrosView(StringUtil.formatLong(ctdDeudasExc));

		return;
	}
	
	private void cargarTotalesExtendidosPreEnvJudSelecDeuda(ProcesoMasivoVO procesoMasivoVO) throws Exception{

		Long ctdCuentasDeudaInc = 0L;
		Long ctdCuentasConveniosDeudaInc = 0L;
		Long ctdCuentasDeudaExc = 0L;

		Double importeHistoricoDeudaInc = 0D;
		Double importeHistoricoConvCuotaInc = 0D;
		Double importeHistoricoDeudaExc = 0D;

		if (this.getViaDeuda().getEsViaAdmin()){
			// cantidad de cuentas de los selAlmDet de la selAlm incluida de la via administrativa del proceso masivo
			ctdCuentasDeudaInc = this.getSelAlmInc().obtenerCantidadCuentas(TipoSelAlm.getTipoSelAlmDetDeudaAdm());
			// cantidad de cuotas de convenio en via administrativa
			// cantidad de cuentas de los selAlmDet de la selAlm incluida de la via administrativa del proceso masivo
			ctdCuentasConveniosDeudaInc = this.getSelAlmInc().obtenerCantidadCuentas(TipoSelAlm.getTipoSelAlmDetConvCuotAdm());
			
			ctdCuentasDeudaExc = this.getSelAlmExc().obtenerCantidadCuentas(TipoSelAlm.getTipoSelAlmDetDeudaAdm());
		}else{
			// cantidad de deudas de la via judicial del proceso masivo				
			ctdCuentasDeudaInc = this.getSelAlmInc().obtenerCantidadCuentas(TipoSelAlm.getTipoSelAlmDetDeudaJud());
			// cantidad de cuotas de convenio en via judicial
			ctdCuentasConveniosDeudaInc = this.getSelAlmInc().obtenerCantidadCuentas(TipoSelAlm.getTipoSelAlmDetConvCuotJud());
			
			ctdCuentasDeudaExc = this.getSelAlmExc().obtenerCantidadCuentas(TipoSelAlm.getTipoSelAlmDetDeudaJud());
		}

		procesoMasivoVO.getSelAlmInc().setCantidadCuentasDeudaView(StringUtil.formatLong(ctdCuentasDeudaInc));
		procesoMasivoVO.getSelAlmInc().setCantidadCuentasCuotaConvenioView(StringUtil.formatLong(ctdCuentasConveniosDeudaInc));
		procesoMasivoVO.getSelAlmInc().setCantidadCuentasView(StringUtil.formatLong(ctdCuentasDeudaInc + ctdCuentasConveniosDeudaInc));

		procesoMasivoVO.getSelAlmExc().setCantidadCuentasDeudaView(StringUtil.formatLong(ctdCuentasDeudaExc));
		// no existen Cantidad Cuentas Cuota Convenio para la selAlmExc
		procesoMasivoVO.getSelAlmExc().setCantidadCuentasView(StringUtil.formatLong(ctdCuentasDeudaExc ));
		
		if (this.getViaDeuda().getEsViaAdmin()){
			
			// Sumatoria de saldos historicos de todos los registros de deuda existentes
			//procesoMasivoVO.getSelAlmInc().setImporteHistoricoTotalView(
			//		StringUtil.formatDouble(this.getSelAlmInc().obtenerImporteHistoricoTotal()));

			// suma de importe historico de deudas de los selAlmDet de la selAlm incluida de la via administrativa del proceso masivo
			importeHistoricoDeudaInc = this.getSelAlmInc().obtenerImporteHistorico(TipoSelAlm.getTipoSelAlmDetDeudaAdm());
 
			// suma importe historico de cuotas de convenio de los selAlmDet de la selAlm incluida de la via administrativa del proceso masivo
			importeHistoricoConvCuotaInc = this.getSelAlmInc().obtenerImporteHistorico(TipoSelAlm.getTipoSelAlmDetConvCuotAdm());
			
			importeHistoricoDeudaExc = this.getSelAlmExc().obtenerImporteHistorico(TipoSelAlm.getTipoSelAlmDetDeudaAdm());
			// no se calcula el importe Historico Conv Cuota Exc
		}else{
			
			// suma de importe historico de deudas de los selAlmDet de la selAlm incluida de la via administrativa del proceso masivo
			importeHistoricoDeudaInc = this.getSelAlmInc().obtenerImporteHistorico(TipoSelAlm.getTipoSelAlmDetDeudaJud());

			// suma importe historico de cuotas de convenio de los selAlmDet de la selAlm incluida de la via administrativa del proceso masivo
			importeHistoricoConvCuotaInc = this.getSelAlmInc().obtenerImporteHistorico(TipoSelAlm.getTipoSelAlmDetConvCuotJud());
			
			importeHistoricoDeudaExc = this.getSelAlmExc().obtenerImporteHistorico(TipoSelAlm.getTipoSelAlmDetDeudaJud());
			// no se calcula el importe Historico Conv Cuota Exc
		}
		
		procesoMasivoVO.getSelAlmInc().setImporteHistoricoDeudaView(StringUtil.formatDouble(importeHistoricoDeudaInc));
		procesoMasivoVO.getSelAlmInc().setImporteHistoricoCuotaConvenioView(StringUtil.formatDouble(importeHistoricoConvCuotaInc));
		procesoMasivoVO.getSelAlmInc().setImporteHistoricoTotalView(StringUtil.formatDouble(importeHistoricoDeudaInc + importeHistoricoConvCuotaInc));

		// TOTALES SelAlmExc
		procesoMasivoVO.getSelAlmExc().setImporteHistoricoDeudaView(StringUtil.formatDouble(importeHistoricoDeudaExc));
		// no existen Cantidad Cuentas Cuota Convenio para la selAlmExc
		procesoMasivoVO.getSelAlmExc().setImporteHistoricoTotalView(StringUtil.formatDouble(importeHistoricoDeudaExc));

		// no implementado Sumatoria de saldos de todos los registros de deuda existentes, actualizados a la fecha de envio

		return;
	}


	/**
	 * Obtiene la lista de procuradores a excluir.
	 * Son los Procuradores asociados al recurso del envio, 
	 * vigentes a la fecha de envio,
	 * que no fueron ya excluidos.
	 * @return List<Procurador>
	 */
	public List<Procurador> getListProcuradoresForExcluir(){

		List<Procurador> listProcuradorHabilitados = new ArrayList<Procurador>();

		List<Procurador> listProcurador = Procurador.getListActivosByRecursoFecha(
				this.getRecurso(), this.getFechaEnvio());

		for (Procurador procurador : listProcurador) {
			if (!this.excluyeAlProcurador(procurador)){
				listProcuradorHabilitados.add(procurador);
			}
		}
		return listProcuradorHabilitados;
	}

	/**
	 * Determina si el Envio Judicial excluye al procurador
	 * @param procurador
	 * @return boolean
	 */
	private boolean excluyeAlProcurador(Procurador procurador){

		for (ProMasProExc proMasProExc : this.getListProMasProExc()) {
			if (proMasProExc.getProcurador().getId().equals(procurador.getId())){
				return true;
			}
		}
		return false;
	}


	// ---> ABM ProMasProExc	
	
	/**
	 * crea un ProMasProExc del ProcesoMasivo
	 * @param  proMasProExc
	 * @return ProMasProExc
	 * @throws Exception
	 */
	public ProMasProExc createProMasProExc(ProMasProExc proMasProExc) throws Exception {

		// Validaciones de negocio
		if (!proMasProExc.validateCreate()) {
			return proMasProExc;
		}

		GdeDAOFactory.getProMasProExcDAO().update(proMasProExc);

		return proMasProExc;
	}

	/**
	 * Borra un ProMasProExc del ProcesoMasivo
	 * @param  proMasProExc
	 * @return ProMasProExc
	 * @throws Exception
	 */
	public ProMasProExc deleteProMasProExc(ProMasProExc proMasProExc) throws Exception {

		// Validaciones de negocio
		if (!proMasProExc.validateDelete()) {
			return proMasProExc;
		}

		GdeDAOFactory.getProMasProExcDAO().delete(proMasProExc);

		return proMasProExc;
	}

	/**
	 * Borra la lista de ProMasProExc del ProcesoMasivo
	 * @throws Exception
	 */
	public void deleteListProMasProExc() throws Exception {
		GdeDAOFactory.getProMasProExcDAO().deleteListProMasProExcByProMas(this);
	}	
	// <--- ABM ProMasProExc	

	public List<PlanillaVO> exportReportesProMasDeuInc() throws Exception{
		
		List<PlanillaVO> listPlanilla = new ArrayList<PlanillaVO>();
		ProMasDeuIncDAO dao = GdeDAOFactory.getProMasDeuIncDAO();
		
		AdpRun.changeRunMessage("Generando Reporte de Deuda Incluida...", 0);
		listPlanilla.addAll(dao.exportReportesDeudaIncluidaByProcesoMasivo(this.getId()));
		
		if (this.getTipProMas().getEsPreEnvioJudicial() || this.getTipProMas().getEsSeleccionDeuda() ){
			AdpRun.changeRunMessage("Generando Reporte de Cuotas de Convenios Incluida...", 0);
			listPlanilla.addAll(dao.exportReportesConvenioCuotaIncluidoByProcesoMasivo(this.getId()));
		}
		
		AdpRun.changeRunMessage("Generando Reporte de Cuentas Incluida...", 0);
		listPlanilla.addAll(dao.exportReportesCuentaIncluida(this.getId()));
		
		return listPlanilla;
	}

	/**
	 * Exporta los reportes de Deudas y Convenios de cuotas excluidos
	 * @return List<PlanillaVO>
	 * @throws Exception
	 */
	public List<PlanillaVO> exportReportesProMasDeuExc() throws Exception{
		
		List<PlanillaVO> listPlanilla = new ArrayList<PlanillaVO>();
		ProMasDeuExcDAO dao = GdeDAOFactory.getProMasDeuExcDAO();
		
		AdpRun.changeRunMessage("Generando Reporte de Deuda Excluida...", 0);
		listPlanilla = dao.exportReportesDeudaExcluidaByProcesoMasivo(this.getId());
		
		if (this.getTipProMas().getEsPreEnvioJudicial() || this.getTipProMas().getEsSeleccionDeuda() ){
			AdpRun.changeRunMessage("Generando Reporte de Cuotas de Convenio Excluida...", 0);
			listPlanilla.addAll(dao.exportReportesConvenioCuotaExcluidoByProcesoMasivo(this.getId()));
		} 
		return listPlanilla;
	}

	/**
	 * Obtiene el procurador que le corresponde a la deuda, segun 
	 * la configuracion de este envio.
	 * Si el envio no utiliza criterio retorna el procurador 
	 * especificado durante el alta de este envio.
	 * Si el envio utiliza criterio, busca en el manzanero de procuradores.
	 * NOTA: este metodo no tiene encuenta si el procurador esta excluido del envio. 
	 * Siempre retorna el procurador que encuentra.
	 * Para saber si un procurador esta excluido usar el metodo: getEsProcuradorExcluido()
	 * Si obtiene mas de un procurador dispara una excepcion.
	 * Si no encuentra a ningun procurador devuelve null.
	 * TODO ver que hacemos con cdm
	 * @param deuda
	 * @return Procurador correspondiente a la deuda. Null si no pudo encontrar procurador para esta deuda.
	 * @throws Exception
	 */
	public Procurador obtenerProcurador(Cuenta cuenta, StringBuffer razon, Date fechaVencimientoDeuda) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		if (SiNo.SI.getId().equals(this.getUtilizaCriterio())){


			// APLICAR EL CRITERIO DE ASIGNACION DE PROCURADORES A LA DEUDA

			//Para el recurso TGI el rango de asignaciÃ³n por procurador estÃ¡ dado por seccion/manzana desde y seccion/manzana hasta.

			//1.Obtener los procuradores del recurso, consultar la tabla gde_proRec, considerar vigencias de los registros respecto de la fecha del envÃ­o.
			// lista de Procuradores activos asociados al recurso con (proRec.fechaDesde <= fechaEnvio <= proRec.fechaHasta )
			//List<Procurador> listProcurador = Procurador.getListActivosByRecursoFecha(this.getRecurso(), this.getFechaEnvio());

			String descripcion = "";

			// Si el criterio de asignacion de procuradores que utiliza es por catastral:
			if(CriAsiPro.ID_CATASTRAL == this.getRecurso().getCriAsiPro().getId()){				
				//un atajo para que se mas rapido si es tgi
				if (Recurso.COD_RECURSO_TGI.equals(this.getRecurso().getCodRecurso())) {
					descripcion = cuenta.getObjImp().getClaveFuncional(); 				
				} else {
					TipObjImpDefinition def = cuenta.getObjImp().getDefinitionValue(this.getFechaEnvio());
					descripcion = (String) def.getValor("Catastral");
				}
				
				descripcion = descripcion.substring(0, 6);   // 10/172/028/000/000  resultado 10/172
			} else if(CriAsiPro.ID_ALFABETICO_DOMICILIO == this.getRecurso().getCriAsiPro().getId()){
				
				descripcion = cuenta.getDesDomEnv();
			} else if(CriAsiPro.ID_ALFABETICO_TITULAR == this.getRecurso().getCriAsiPro().getId()){
				boolean deudaSigueTitular = Integer.valueOf(1).equals(this.getRecurso().getEsDeudaTitular());
				
				//si la deuda sigue al titular, buscamos el nombre del cuenta titular que
				//corresponde a la fecha de analisis pasada.
				//sino, usamos la descripcion del titular principal que esta en pad_cuenta.destitpri
				if (deudaSigueTitular) {
					List<CuentaTitular> listTit = cuenta.getListCuentaTitularVigentesCerrado(fechaVencimientoDeuda);
					CuentaTitular tit = null;
					if (listTit.size() > 0) {
						tit = listTit.get(0);
						Long idContr = tit.getIdContribuyente();
						Persona persona = Persona.getByIdLight(idContr);
						if (persona != null) {
							descripcion = persona.getRepresent().toLowerCase().substring(0,1); //tomamos la primer letra
						}
					}
				} else {
					descripcion = cuenta.getNomTitPri().toLowerCase().substring(0,1); //tomamos la primer letra
				}
			}

			List<Procurador> listProcurador = Procurador.getListActivosByRecursoFechaDescripcion(this.getRecurso(), this.getFechaEnvio(), descripcion);
			//2.Para cada procurador,
			if (listProcurador.size() > 1){
				String filtrosAplicados = 	"recurso: " + this.getRecurso().getDesRecurso() +"; fecha de Envio: " +  this.getFechaEnvio() + "; desc: " + descripcion ; 
				log.error("Se ha obtenido mas de un procurador aplicando los siguientes filtros " + filtrosAplicados);
				
				//se decidio CORTAR EL PROCESO disparando una excepcion
				throw new Exception("De acuerdo al Criterio de Asignaci&oacute;n de Procuradores se ha encontrado mas de un procurador. Filtros aplicados: " + filtrosAplicados);

			}else if(listProcurador.size() == 1) {
				// obtiene el unico procurador encontrado
				return (Procurador) listProcurador.get(0);
			}

			//TODO Para el recurso CdM, no sabemos, ya que se realizar envio a judiciales una unica vez (a definir).
			
			log.debug("Procurador no encontrado");
			return null;
		}else{
			log.debug("No utiliza el criterio: devuelve el procurador asignado al envio judicial");
			return this.getProcurador();
		}
	}

	/**
	 * Determina si la deuda es excluida si existe en la seleccion almacenada de la deuda a excluir
	 * 
	 * @param deudaAdmin
	 * @return boolean
	 */
	private boolean esExcluida(DeudaAdmin deudaAdmin) {
		String key = deudaAdmin.getId() + "-" + TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM;
		return this.mapDeudasSelAlmExc.containsKey(key);
		//return this.getSelAlmExc().contieneElemento(deudaAdmin.getId(), TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM);
	}
	
	private boolean esExcluida(DeudaJudicial deudaJudicial){
		String key = deudaJudicial.getId() + "-" + TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_JUD;
		return this.mapDeudasSelAlmExc.containsKey(key);
		//return this.getSelAlmExc().contieneElemento(deudaJudicial.getId(), TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_JUD);
	}


	/**
	 * Crea una deuda a excluir en la tabla de deuda exlcuida del envio.
	 * @param deuda deuda a excluir
	 * @param procurador procurador excluido del envio si existiese. requerido si el idMotExcl es por exclusion de procurador, puede ser null
	 * @param idMotExc motivo de exclusion
	 * @param observacion
	 * @return el objeto creado
	 * @throws Exception
	 */
	private ProMasDeuExc instanciarProMasDeuExc(Deuda deuda, Procurador procurador, Long idMotExc, String observacion, String desCuentaTitular, TipoSelAlm tipoSelAlmDet) throws Exception {

		ProMasDeuExc proMasDeuExc = new ProMasDeuExc();
		MotExc motExc=MotExc.getById(idMotExc);
		proMasDeuExc.setProcesoMasivo(this);
		proMasDeuExc.setIdDeuda(deuda.getId());
		proMasDeuExc.setProcurador(procurador);
		proMasDeuExc.setMotExc(motExc);
		proMasDeuExc.setObservacion(observacion);
		proMasDeuExc.setDesTitularPrincipal(desCuentaTitular);
		proMasDeuExc.setTipoSelAlmDet(tipoSelAlmDet);
		proMasDeuExc.setEstado(Estado.ACTIVO.getId());

		log.info(String.format("instanciarProMasDeuExc: excluyendo deuda: %s %s - %s %s - %s",
							   deuda.getId(), 
							   deuda.getCuenta().getNumeroCuenta(),
							   motExc == null ? "null" : motExc.getDesMotExc(), 
							   observacion, desCuentaTitular));

		return proMasDeuExc;
	}

	private ProMasDeuExc instanciarProMasDeuExc(ConvenioCuota convenioCuota, Long idMotExc, String observacion, String desCuentaTitular) throws Exception {

		ProMasDeuExc proMasDeuExc = new ProMasDeuExc();
		MotExc motExc=MotExc.getById(idMotExc);
		proMasDeuExc.setProcesoMasivo(this);
		proMasDeuExc.setIdDeuda(convenioCuota.getId());
		proMasDeuExc.setProcurador(convenioCuota.getConvenio().getProcurador());
		proMasDeuExc.setMotExc(motExc);
		proMasDeuExc.setObservacion(observacion);
		proMasDeuExc.setDesTitularPrincipal(desCuentaTitular);
		if (convenioCuota.getConvenio().getViaDeuda().getEsViaAdmin()){
			proMasDeuExc.setTipoSelAlmDet(TipoSelAlm.getTipoSelAlmDetConvCuotAdm());
		}else{
			proMasDeuExc.setTipoSelAlmDet(TipoSelAlm.getTipoSelAlmDetConvCuotJud());
		}

		proMasDeuExc.setEstado(Estado.ACTIVO.getId());

		log.info(String.format("instanciarProMasDeuExc: excluyendo convenioCuota: %s %s - %s %s - %s",
							   convenioCuota.getId(), 
							   convenioCuota.getConvenio().getCuenta().getNumeroCuenta(),
							   motExc == null ? "null" : motExc.getDesMotExc(), 
							   observacion, desCuentaTitular));

		return proMasDeuExc;
	}

	/**
	 * Crea una deuda a incluir en la tabla de deuda incluida del envio.
	 * @param deuda deuda a incluir en el envio
	 * @param procurador Procurador asignado a la deuda
	 * @param obsMotNoVueAtras observacion del motivo de no vuelta a atras 
	 * @return en objeto insertado
	 * @throws Exception
	 */
	private ProMasDeuInc instanciarProMasDeuInc(DeudaAdmin deudaAdmin, Procurador procurador, String obsMotNoVueAtras, String desCuentaTitular, CueExeCache exeCache) throws Exception {
		// idProcesoMasivo: id del envio
		//idDeuda: id de la deuda
		//idProcurador: id del procurador asignado
		//obsMotNoVueAtr: nula
		//saldoHistorico: saldo histÃ³rico de la deuda.
		//saldoActualizado: saldo de la deuda actualizado a la fecha del envÃ­o. NO ACTUALIZAR SALDO

		ProMasDeuInc proMasDeuInc = new ProMasDeuInc();
		proMasDeuInc.setProcesoMasivo(this);
		proMasDeuInc.setIdDeuda(deudaAdmin.getId());
		proMasDeuInc.setProcurador(procurador);
		proMasDeuInc.setSaldoHistorico(deudaAdmin.getSaldo());
		// calculo del saldo actualizado: en la bdd es requerido a la fecha de envio
		proMasDeuInc.setSaldoActualizado(deudaAdmin.actualizacionSaldo(this.getFechaEnvio(), exeCache).getImporteAct());
		proMasDeuInc.setObsMotNoVueAtr(null);
		proMasDeuInc.setDesTitularPrincipal(desCuentaTitular);
		proMasDeuInc.setTipoSelAlmDet(TipoSelAlm.getTipoSelAlmDetDeudaAdm());
		proMasDeuInc.setEstado(Estado.ACTIVO.getId());

		log.info(String.format("instanciarProMasDeuInc: incluyendo deuda: %s %s - %s %s - %s", 
							   deudaAdmin.getId(), 
							   deudaAdmin.getCuenta().getNumeroCuenta(),
							   procurador == null ? "null" : procurador.getId() , 
							   procurador == null ? "null" : procurador.getDescripcion(), 
							   desCuentaTitular));

		return proMasDeuInc;
	}
	
	private ProMasDeuInc instanciarProMasDeuInc(DeudaJudicial deudaJudicial, String obsMotNoVueAtras, String desCuentaTitular) throws Exception {
		// idProcesoMasivo: id del envio
		//idDeuda: id de la deuda
		//idProcurador: id del procurador asignado de la deuda judicial
		//obsMotNoVueAtr: nula
		//saldoHistorico: saldo historico de la deuda.
		//saldoActualizado: saldo de la deuda actualizado a la fecha del envi�o.

		ProMasDeuInc proMasDeuInc = new ProMasDeuInc();
		proMasDeuInc.setProcesoMasivo(this);
		proMasDeuInc.setIdDeuda(deudaJudicial.getId());
		Procurador procurador = deudaJudicial.getProcurador();
		proMasDeuInc.setProcurador(procurador);
		proMasDeuInc.setSaldoHistorico(deudaJudicial.getSaldo());
		// calculo del saldo actualizado: en la bdd es requerido a la fecha de envio
		proMasDeuInc.setSaldoActualizado(deudaJudicial.actualizacionSaldo(this.getFechaEnvio()).getImporteAct());
		proMasDeuInc.setObsMotNoVueAtr(null);
		proMasDeuInc.setDesTitularPrincipal(desCuentaTitular);
		proMasDeuInc.setTipoSelAlmDet(TipoSelAlm.getTipoSelAlmDetDeudaJud());
		proMasDeuInc.setEstado(Estado.ACTIVO.getId());

		log.info(String.format("instanciarProMasDeuInc: incluyendo deuda: %s %s - %s %s - %s", 
							   deudaJudicial.getId(), 
							   deudaJudicial.getCuenta().getNumeroCuenta(),
							   procurador == null ? "null" : procurador.getId() , 
							   procurador == null ? "null" : procurador.getDescripcion(), 
							   desCuentaTitular));

		return proMasDeuInc;
	} 

	private ProMasDeuInc instanciarProMasDeuInc(ConvenioCuota convenioCuota, String obsMotNoVueAtras, String desCuentaTitular) throws Exception {
		//idProcesoMasivo: id del envio
		//idDeuda: id del convenioCuota
		//idProcurador: id del procurador del convenio
		//obsMotNoVueAtr: nula
		//saldoHistorico: importe de la cuota
		//saldoActualizado: se determino no actualizar el importe de la cuota. En la bdd el saldo actualizado es requerido

		ProMasDeuInc proMasDeuInc = new ProMasDeuInc();
		proMasDeuInc.setProcesoMasivo(this);
		proMasDeuInc.setIdDeuda(convenioCuota.getId());
		Procurador procurador = convenioCuota.getConvenio().getProcurador();
		proMasDeuInc.setProcurador(convenioCuota.getConvenio().getProcurador());
		proMasDeuInc.setSaldoHistorico(convenioCuota.getImporteCuota());
		
		// se determino no actualizar el importe de la cuota. En la bdd el saldo actualizado es requerido
		proMasDeuInc.setSaldoActualizado(convenioCuota.getImporteCuota());
		proMasDeuInc.setObsMotNoVueAtr(null);
		proMasDeuInc.setDesTitularPrincipal(desCuentaTitular);
		if (convenioCuota.getConvenio().getViaDeuda().getEsViaAdmin()){
			proMasDeuInc.setTipoSelAlmDet(TipoSelAlm.getTipoSelAlmDetConvCuotAdm());
		}else{
			proMasDeuInc.setTipoSelAlmDet(TipoSelAlm.getTipoSelAlmDetConvCuotJud());
		}
		proMasDeuInc.setEstado(Estado.ACTIVO.getId());

		log.info(String.format("instanciarProMasDeuInc: incluyendo convenio de cuota: %s %s - %s %s - %s", 
							   convenioCuota.getId(), 
							   convenioCuota.getConvenio().getCuenta().getNumeroCuenta(),
							   procurador == null ? "null" : procurador.getId() , 
							   procurador == null ? "null" : procurador.getDescripcion(), 
							   desCuentaTitular));

		return proMasDeuInc;
	}


	/**
	 * Crea un ProMasDeuInc del Envio Judicial
	 * @param proMasDeuInc
	 * @return ProMasDeuInc  
	 * @throws Exception
	 */
	public ProMasDeuInc createProMasDeuInc(ProMasDeuInc proMasDeuInc) throws Exception {

		// Validaciones de negocio
		if (!proMasDeuInc.validateCreate()) {
			return proMasDeuInc;
		}

		GdeDAOFactory.getProMasDeuIncDAO().update(proMasDeuInc);

		return proMasDeuInc;
	}

	/**
	 * Elimina un ProMasDeuInc del Envio Judicial
	 * @param  proMasDeuInc
	 * @return ProMasDeuInc
	 * @throws Exception
	 */
	public ProMasDeuInc deleteProMasDeuInc(ProMasDeuInc proMasDeuInc) throws Exception {

		// Validaciones de negocio
		if (!proMasDeuInc.validateDelete()) {
			return proMasDeuInc;
		}

		GdeDAOFactory.getProMasDeuIncDAO().delete(proMasDeuInc);

		return proMasDeuInc;
	}
	
	/**
	 * Borra los ProMasDeuExc del Envio Judicial
	 * @return int
	 * @throws Exception
	 */
	public int deleteListProMasDeuInc() throws Exception {

		// Validaciones de negocio
		

		return GdeDAOFactory.getProMasDeuIncDAO().deleteListByProcesoMasivo(this);
	}


	/**
	 * Crea una ProMasDeuExc del envio judicial
	 * @param proMasDeuExc
	 * @return ProMasDeuExc
	 * @throws Exception
	 */
	public ProMasDeuExc createProMasDeuExc(ProMasDeuExc proMasDeuExc) throws Exception {

		// Validaciones de negocio
		if (!proMasDeuExc.validateCreate()) {
			return proMasDeuExc;
		}

		GdeDAOFactory.getProMasDeuExcDAO().update(proMasDeuExc);

		return proMasDeuExc;
	}

	/**
	 * Elimina el ProMasDeuExc del envio judicial
	 * @param  proMasDeuExc
	 * @return ProMasDeuExc
	 * @throws Exception
	 */
	public ProMasDeuExc deleteProMasDeuInc(ProMasDeuExc proMasDeuExc) throws Exception {

		// Validaciones de negocio
		if (!proMasDeuExc.validateDelete()) {
			return proMasDeuExc;
		}

		GdeDAOFactory.getProMasDeuExcDAO().delete(proMasDeuExc);

		return proMasDeuExc;
	}
	
	/**
	 * Borra los ProMasDeuExc del Envio Judicial
	 * @return int
	 * @throws Exception
	 */
	public int deleteListProMasDeuExc() throws Exception {

		// Validaciones de negocio
		

		return GdeDAOFactory.getProMasDeuExcDAO().deleteListByProcesoMasivo(this);
	}


	/**
	 * Incluye y excluye las deudas cargadas en los detalles de la seleccion almancenada incluida del envio judicial.
	 * <p>Ejecuta las validaciones sobre cada deuda de la lista de deudas
	 * <br>Si no son correctas, excluye a la deuda.
	 * <br>Si son correctas incluye a la deuda.
	 * @throws Exception
	 */
	public void incluirExcluirDeudasAlmacenadas() throws Exception{
		//Si pm.tipProMas es EJ:
		//	pm.via es solo administrativa.
		//Si pm.tipProMas es Reconfeccion:
		//	pm.via es solo administrativa

		//Si pm.tipProMas es PEJ:
		//	Si pm.via es administrativa:
		//		usar deudas administrativas y cuotas de convenios en via administrativa.
		//	Si pm.via es judicial:
		//		usar deudas judiciales y cuotas de convenios en via judicial.

		//Si pm.tipProMas es Seleccion Deuda:
		//	Si pm.via es administrativa:
		//		usar deudas administrativas y cuotas de convenios en via administrativa.
		//	Si pm.via es judicial:
		//		usar deudas judiciales y cuotas de convenios en via judicial.
		
		if (this.getTipProMas().getEsEnvioJudicial() || this.getTipProMas().getEsReconfeccion()){
			
			this.incluirExcluirDeudasAdministrativas();
			
		}else if (this.getTipProMas().getEsPreEnvioJudicial() || this.getTipProMas().getEsSeleccionDeuda() ){
			
			if(this.getViaDeuda().getEsViaAdmin()){
				
				this.incluirExcluirDeudasAdministrativas();
				this.incluirExcluirConveniosCuotas(TipoSelAlm.getTipoSelAlmDetConvCuotAdm());
				
			} else if(this.getViaDeuda().getEsViaJudicial()){
				
				this.incluirExcluirDeudasJudiciales();
				this.incluirExcluirConveniosCuotas(TipoSelAlm.getTipoSelAlmDetConvCuotJud());
				
			} 
		} 
	}

	private void incluirExcluirDeudasAdministrativas() throws Exception{
		AdpRun.changeRunMessage("Procesando Deudas Administrativas...", 0);
		
		List<Long> listIdDeuda = GdeDAOFactory.getSelAlmDetDAO().getListIdElemento(this.getSelAlmInc().getId(), TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM);
		this.incluirExcluirListDeudaAdm(listIdDeuda);
		
		AdpRun.changeRunMessage("Deudas Administrativas procesadas totalmente con exito.", 0);
	}

	private void incluirExcluirDeudasJudiciales() throws Exception{
		AdpRun.changeRunMessage("Procesando Deudas Judiciales...", 0);
		
		List<Long> listIdDeuda = GdeDAOFactory.getSelAlmDetDAO().getListIdElemento(this.getSelAlmInc().getId(), TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_JUD);
		this.incluirExcluirListDeudaJudicial(listIdDeuda);
		
		AdpRun.changeRunMessage("Deudas Judiciales procesadas totalmente con exito.", 0);		
	}

	private void incluirExcluirConveniosCuotas(TipoSelAlm tipoSelAlmDet) throws Exception{
		AdpRun.changeRunMessage("Procesando Cuotas de Convenios...", 0);
		
		List<Long> listIdCuotaAdm = GdeDAOFactory.getSelAlmDetDAO().getListIdElemento(this.getSelAlmInc().getId(), TipoSelAlm.TIPO_SEL_ALM_DET_CONV_CUOT_ADM);
		List<Long> listIdCuotaJud = GdeDAOFactory.getSelAlmDetDAO().getListIdElemento(this.getSelAlmInc().getId(), TipoSelAlm.TIPO_SEL_ALM_DET_CONV_CUOT_JUD);
		List<Long>  listIds = new ArrayList<Long>();
		listIds.addAll(listIdCuotaAdm);
		listIds.addAll(listIdCuotaJud);
		
		this.incluirExcluirListConvenioCuota(listIds);
		
		AdpRun.changeRunMessage("Cuotas de Convenio procesada totalmente con exito.", 0);
	}

	private void incluirExcluirListDeudaAdm(List<Long> listIdDeuda) throws Exception {
		Cuenta cuenta = null; //cuenta de la deuda que se esta procesando.
		String desTitularPrincipal = ""; //descripcion del titular principal de la cuenta
		long idCuentaOld = 0;
		StringBuffer razonExclusionProcur = null;
		Procurador procurador = null; //el procurador asignado para la cuenta que se esta procesando. (solo valido en envio judicial)
		long count = 0;
		boolean deudaSigueTitular = Integer.valueOf(1).equals(recurso.getEsDeudaTitular());
		
		for(Long idDeuda: listIdDeuda) {			
			if (++count % 1000 == 0) {
				SiatHibernateUtil.currentSession().getTransaction().commit();
				SiatHibernateUtil.closeSession();
				SiatHibernateUtil.currentSession().beginTransaction();
				if (cuenta != null)
					SiatHibernateUtil.currentSession().refresh(cuenta);
			}
			AdpRun.changeRunMessage("Procesando Deuda Administrativa - " + count + " de " + listIdDeuda.size(), 60);		
 
			log.info("Procesando idDeuda: " + idDeuda);
			MotExc motExc = null;
			ProMasDeuExc proMasDeuExc = null; 
			DeudaAdmin deudaAdmin = DeudaAdmin.getByIdNull(idDeuda);
			if (deudaAdmin == null) {
				Deuda deuda = Deuda.getById(idDeuda);
				cuenta = deuda.getCuenta();
				String str = " Cuenta: " + cuenta.getNumeroCuenta() + " Anio: " + deuda.getAnio() + " Periodo:" + deuda.getPeriodo();
				proMasDeuExc = this.instanciarProMasDeuExc(deuda, null, MotExc.ID_POR_NO_DEU_ADMIN , null, str, TipoSelAlm.getTipoSelAlmDetDeudaAdm());
				GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
				continue;  // continua con la otra deuda
			}
			
			if (cuenta == null || idCuentaOld != deudaAdmin.getIdCuenta()) {
				cuenta = deudaAdmin.getCuenta();
				desTitularPrincipal = cuenta.getDesTitularPrincipal();
				idCuentaOld = cuenta.getId();

				//si cambia la cuenta, y es un envio judicial busco el procurador asignado.
				if (this.getTipProMas().getId().equals(TipProMas.ID_ENVIO_JUDICIAL)) {
					razonExclusionProcur = new StringBuffer();
					procurador = this.obtenerProcurador(cuenta, razonExclusionProcur, deudaAdmin.getFechaVencimiento());
				}
			}
			
			//Si deudaSigue al titular, tenemos que buscarlo para cada deuda, 
			//porque puede cambiar el titular segun la fecha de vencimiento.
			if (deudaSigueTitular) {
				razonExclusionProcur = new StringBuffer();
				procurador = this.obtenerProcurador(cuenta, razonExclusionProcur, deudaAdmin.getFechaVencimiento());				
			}
			
			// 1.1.Validar que no exista en la seleccion almacenada correspondiente a la deuda a excluir.
			boolean esExcluida = this.esExcluida(deudaAdmin);
			if (esExcluida){
				/* 1.2.Si existe, grabar la deuda en el detalle de deuda excluida (tabla gde_proMasDeuExc):
				   idProcesoMasivo: id del envio
				   idDeuda: id de la deuda
				   idProcurador: nulo
				   idMotExc: Excluida del envi�o manualmente TODO es ID_POR_EXC_ENVIO 
				   observacion: nula.
				*/
				proMasDeuExc = this.instanciarProMasDeuExc(deudaAdmin, null, MotExc.ID_POR_EXC_ENVIO, null, desTitularPrincipal, TipoSelAlm.getTipoSelAlmDetDeudaAdm());
				GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
				continue;  // continua con la otra deuda
			}
			
			EstadoDeuda estadoDeuda = deudaAdmin.getEstadoDeuda();
			ViaDeuda    viaDeuda    = deudaAdmin.getViaDeuda();		
			// 1.3. Si no existe, validar si el estadoDeuda es Administrativa y la VÃ­a de la Deuda es Administrativa.
			if(!(EstadoDeuda.ID_ADMINISTRATIVA == estadoDeuda.getId().longValue() && 
				 ViaDeuda.ID_VIA_ADMIN == viaDeuda.getId().longValue())){
				//1.4.Si no lo es, grabar la deuda en el detalle de deuda excluida (tabla gde_proMasDeuExc):
				//idProcesoMasivo: id del envio
				//idDeuda: id de la deuda
				//idProcurador: nulo
				//idMotExc: Estado o via invalido
				//observacion: nula

				motExc = MotExc.getById(MotExc.ID_DEUDA_ESTADO_VIA_INVALIDO);
				proMasDeuExc = this.instanciarProMasDeuExc(deudaAdmin, null, motExc.getId(), null, desTitularPrincipal, TipoSelAlm.getTipoSelAlmDetDeudaAdm());
				GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
				continue;  // continua con la otra deuda
			}

			//1.5.Si lo es, validar si la deuda se encuentra Reclamada. 
			if(deudaAdmin.getEsReclamada() || deudaAdmin.getEstaEnAsentamiento() ){
				//1.6.Si lo esta, grabar la deuda en el detalle de deuda excluida (tabla gde_proMasDeuExc):
				//idProcesoMasivo: id del envio
				//idDeuda: id de la deuda
				//idProcurador: nulo
				//idMotExc: Por Deuda Reclamada
				//observacion: nula para este release
				proMasDeuExc = this.instanciarProMasDeuExc(deudaAdmin, null, MotExc.ID_POR_DEU_RECLAMADA, null, desTitularPrincipal, TipoSelAlm.getTipoSelAlmDetDeudaAdm());
				GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
				continue;  // continua con la otra deuda    			
			}

			//1.7.Si no lo esta, validar si la deuda se encuentra Indeterminada. 
			//Esta validacion se debe realizar verificando si la deuda se encuentra marcada como indeterminada, esto es, buscandola en el sistema actual de Indeterminados que existe en Balance (a definir).
			if(deudaAdmin.getEsIndeterminada()){ 
				//1.8.Si lo esta, grabar la deuda en el detalle de deuda excluida (tabla gde_proMasDeuExc):
				//idProcesoMasivo: id del envio
				//idDeuda: id de la deuda
				//idProcurador: nulo
				//idMotExc: Por Deuda Indeterminada
				//observacion: (a definir dependiendo de los datos que administre el sistema de indeterminados)
				proMasDeuExc = this.instanciarProMasDeuExc(deudaAdmin, null, MotExc.ID_POR_DEU_INDETERMINADA, null, desTitularPrincipal, TipoSelAlm.getTipoSelAlmDetDeudaAdm());
				GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
				continue;  // continua con la otra deuda    			
			}

			//1.9.Si no lo esta, validar si la deuda se encuentra en Convenio vigente (no debe estar en la tabla gde_convenioDeuda, excepto que el convenio se encuentre en estado âRecompuestoâ o âAnuladoâ, en la tabla gde_convenio).
			Convenio convenio = deudaAdmin.getConvenio(); 
			if(convenio != null){
				//1.10.Si se encuentra, grabar la deuda en el detalle de deuda excluida (tabla gde_proMasDeuExc):
				//idProcesoMasivo: id del envio
				//idDeuda: id de la deuda
				//idProcurador: nulo
				//idMotExc: Por Deuda Convenio
				//observacion: Nro. de convenio y estado del convenio (armar cadena)
				String obs = convenio.getNroConvenio() + " " + convenio.getEstadoConvenio().getDesEstadoConvenio();
				proMasDeuExc = this.instanciarProMasDeuExc(deudaAdmin, null, MotExc.ID_POR_DEU_CONVENIO, obs, desTitularPrincipal, TipoSelAlm.getTipoSelAlmDetDeudaAdm());
				GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
				continue;  // continua con la otra deuda    			
			}

			//1.13.Si no lo esta, y se trate de un envio o pre envio, validar que la deuda no se encuentre afectada por ninguna exenciÃ³n que impida el envÃ­o a judiciales. Esto es, 
			//1.13.1.Que no exista en la tabla exe_cueExe la cuenta asociada a la deuda, considerando la vigencia de los registros respecto de la fecha de envÃ­o, es decir, si no estÃ¡ vigente, la deuda no se encuentra afectada por la exenciÃ³n.. 
			//1.13.2.Si existe y esta vigente, validar que dicha exencion no permita al envi�o a judiciales (campo procesoMasivo de la tabla exe_exencion igual a 1).
			//1.13.3.Si no permite el Envio a Judiciales validar que la deuda no exista en la tabla XXX (a definir), considerando la vigencia de los registros respecto de la fecha de envÃ­o, es decir, si no estÃ¡ vigente la deuda no se encuentra afectada por la exenciÃ³n.
			if (this.getTipProMas().getId().equals(TipProMas.ID_PRE_ENVIO_JUDICIAL) 
				|| this.getTipProMas().getId().equals(TipProMas.ID_ENVIO_JUDICIAL)) {

				//Tiene alguna exencion vigente y que diga que es exenta de envio a judicial?
				Exencion exencion = cuenta.exentaEnvioJud(this.getFechaEnvio(), deudaAdmin.getFechaVencimiento(), this.getCueExeCache()); 
				if(exencion != null){
					//1.14.Si se encuentra afectada por una exenciÃ³n, grabar la deuda en el detalle de deuda excluida (tabla gde_proMasDeuExc):
					//idProcesoMasivo: id del envio
					//idDeuda: id de la deuda
					//idProcurador: nulo
					//idMotExc: Por Deuda ExcluÃ­da por ExenciÃ³n
					//observacion: CÃ³digo y DescripciÃ³n de la ExenciÃ³n (armar cadena)
					String obs = exencion.getCodExencion() + " " + exencion.getDesExencion();
					proMasDeuExc = this.instanciarProMasDeuExc(deudaAdmin, null, MotExc.ID_POR_EXENCION, obs, desTitularPrincipal, TipoSelAlm.getTipoSelAlmDetDeudaAdm());
					GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
					proMasDeuExc.addErrorMessages(this);
					continue;  // continua con la otra deuda    			
				}
			}
			
			//la cuenta tiene algun broche que lo excluye de envioJudicial
			Broche broche = cuenta.getBroche();
			String desBrocheExento = null;
			if (broche != null) desBrocheExento = this.mapBrocheExentoEnvioJud.get(broche.getId());
			if (desBrocheExento != null) {
				String obs = desBrocheExento;
				proMasDeuExc = this.instanciarProMasDeuExc(deudaAdmin, null, MotExc.ID_POR_BROCHE_EXENTO_ENVIO_JUD, obs, desTitularPrincipal, TipoSelAlm.getTipoSelAlmDetDeudaAdm());
				GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
				continue;  // continua con la otra deuda				
			}

			//1.15.Si no lo estÃ¡, y se trata de un Envio Judicial, tratar de asignar el procurador a la deuda, esto es, 
			if (this.getTipProMas().getId().equals(TipProMas.ID_ENVIO_JUDICIAL)) {
				if (procurador == null){
					//1.16.Si no se pudo asignar el procurador, grabar la deuda en el detalle de deuda excluida (tabla gde_proMasDeuExc):
					//idProcesoMasivo: id del envio
					//idDeuda: id de la deuda
					//idProcurador: nulo 
					//idMotExc: Por no poder asignarle el Procurador
					proMasDeuExc = this.instanciarProMasDeuExc(deudaAdmin, null, MotExc.ID_POR_NO_ASIG_PROC, razonExclusionProcur.toString(), desTitularPrincipal, TipoSelAlm.getTipoSelAlmDetDeudaAdm());
					GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
					proMasDeuExc.addErrorMessages(this);
					continue;  // continua con la otra deuda
				}
				
				//1.17.Si se pudo asignar el procurador, 
				// validar que el mismo no se encuentre en la lista de procuradores excluÃ­dos para el envÃ­o (este caso se dÃ¡ si se configurÃ³ el envÃ­o para que utilice criterio). Para el caso de que en el envÃ­o no se considera el criterio ir al paso 1.17 (sin considerar la lista de procuradores excluÃ­dos en el envÃ­o).
				if (this.excluyeAlProcurador(procurador)){
					//1.18.Si se encuentra en la lista de procuradores excluÃ­dos en el envÃ­o, grabar la deuda en el detalle de deuda excluida (tabla gde_proMasDeuExc):
					//idProcesoMasivo: id del envio
					//idDeuda: id de la deuda
					//idProcurador: id del procurador asignado 
					//idMotExc: Por exclusiÃ³n de Procuradores en el EnvÃ­o 
					//observacion: la misma que se cargÃ³ al excluir el procurador del EnvÃ­o. 
					proMasDeuExc = this.instanciarProMasDeuExc(deudaAdmin, procurador, MotExc.ID_POR_EXC_PROC, MotExc.getById(MotExc.ID_POR_EXC_PROC).getDesMotExc(), desTitularPrincipal, TipoSelAlm.getTipoSelAlmDetDeudaAdm());
					GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
					proMasDeuExc.addErrorMessages(this);
					continue;  // continua con la otra deuda
				}
			}

			//1.11.Si no lo estÃ¡, validar que la deuda no se encuentre excluÃ­da por otras Ã¡reas, 
			// esto es, no debe existir en la tabla pad_cueExcSelDeu, considerar la vigencia de los registros respecto de la fecha de envÃ­o, es decir, si no estÃ¡ vigente no excluir.
			//List<Area> listArea = deudaAdmin.getEsExcluidaPorArea();
			if (new Integer(1).equals(this.getConCuentaExcSel())) {
				List<Area> listArea = deudaAdmin.getEsExcluidaPorArea(this.exentaAreaCache);
				if(listArea.size() > 0){
					String desAreas = "";
					for (Area area : listArea) {
						if(desAreas.equals("")){
							desAreas += area.getDesArea() ;
						} else{
							desAreas += ", " + area.getDesArea() ;
						}
					}
					proMasDeuExc = this.instanciarProMasDeuExc(deudaAdmin, null, MotExc.ID_POR_EXC_OTRAS_AREAS, desAreas, desTitularPrincipal, TipoSelAlm.getTipoSelAlmDetDeudaAdm());
					GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
					proMasDeuExc.addErrorMessages(this);
					continue;  // continua con la otra deuda    			
				}
			}

			//1.19.Si no se encuentra en la lista de procuradores excluÃ­dos en el envÃ­o, grabar la deuda en el detalle de deuda incluida (tabla gde_proMasDeuInc):
			//idProcesoMasivo: id del envio
			//idDeuda: id de la deuda
			//idProcurador: id del procurador asignado
			//obsMotNoVueAtr: nula
			//saldoHistorico: saldo histÃ³rico de la deuda.
			//saldoActualizado: saldo de la deuda actualizado a la fecha del envÃ­o. en la base de datos es requerido
			// asunto titular principal
			ProMasDeuInc proMasDeuInc = this.instanciarProMasDeuInc(deudaAdmin, procurador, null, desTitularPrincipal, this.cueExeCache);
			GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuInc(proMasDeuInc);
			proMasDeuInc.addErrorMessages(this);
		}
		SiatHibernateUtil.currentSession().getTransaction().commit();
		SiatHibernateUtil.closeSession();
		SiatHibernateUtil.currentSession().beginTransaction();
	}

	private void incluirExcluirListDeudaJudicial(List <Long> listIdDeuda) throws Exception {
		Cuenta cuenta = null; //cuenta de la deuda que se esta procesando.
		String desTitularPrincipal = ""; //descripcion del titular principal de la cuenta
		long idCuentaOld = 0;
		long count = 0;
		
		for(Long idDeuda: listIdDeuda) { 
			//commit por cada registro
			if (++count % 1000 == 0) {
				SiatHibernateUtil.currentSession().getTransaction().commit();
				SiatHibernateUtil.closeSession();
				SiatHibernateUtil.currentSession().beginTransaction();
				if (cuenta != null)
					SiatHibernateUtil.currentSession().refresh(cuenta);
			}
			AdpRun.changeRunMessage("Procesando Deuda Judicial - " + count + " de " + listIdDeuda.size(), 60);

			log.info("Procesando idDeuda: " + idDeuda);
			MotExc motExc = null;
			ProMasDeuExc proMasDeuExc = null; 
			DeudaJudicial deudaJudicial = DeudaJudicial.getByIdNull(idDeuda);
			if (deudaJudicial == null) {
				Deuda deuda = Deuda.getById(idDeuda);
				cuenta = deuda.getCuenta();
				String str = " Cuenta: " + cuenta.getNumeroCuenta() + " Anio: " + deuda.getAnio() + " Periodo:" + deuda.getPeriodo();
				proMasDeuExc = this.instanciarProMasDeuExc(deuda, null, MotExc.ID_POR_NO_DEU_JUDICIAL, null, str, TipoSelAlm.getTipoSelAlmDetDeudaJud());
				GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
				continue;  // continua con la otra deuda
			}
			
			if (cuenta == null || idCuentaOld != deudaJudicial.getCuenta().getId()) {
				cuenta = deudaJudicial.getCuenta();
				desTitularPrincipal = cuenta.getDesTitularPrincipal();
				idCuentaOld = cuenta.getId();

			}

			// 1.3. Si no existe, validar si el estadoDeuda es Judicial y la Via de la Deuda es Judicial.
			EstadoDeuda estadoDeuda = deudaJudicial.getEstadoDeuda();
			ViaDeuda    viaDeuda    = deudaJudicial.getViaDeuda();
			if(!(EstadoDeuda.ID_JUDICIAL == estadoDeuda.getId().longValue() && 
				 ViaDeuda.ID_VIA_JUDICIAL == viaDeuda.getId().longValue())){
				//1.4.Si no lo es, grabar la deuda en el detalle de deuda excluida (tabla gde_proMasDeuExc):
				//idProcesoMasivo: id del envio
				//idDeuda: id de la deuda
				//idProcurador: nulo
				//idMotExc: Estado o Via invalido
				//observacion: nula
				
				motExc = MotExc.getById(MotExc.ID_DEUDA_ESTADO_VIA_INVALIDO);
				proMasDeuExc = this.instanciarProMasDeuExc(deudaJudicial, null, motExc.getId(), null, desTitularPrincipal, TipoSelAlm.getTipoSelAlmDetDeudaAdm());
				GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
				continue;  // continua con la otra deuda
			}

			//1.5.Si lo es, validar si la deuda se encuentra Reclamada. 
			if(deudaJudicial.getEsReclamada() || deudaJudicial.getEstaEnAsentamiento() ){
				//1.6.Si lo estÃ¡, grabar la deuda en el detalle de deuda excluida (tabla gde_proMasDeuExc):
				//idProcesoMasivo: id del envio
				//idDeuda: id de la deuda
				//idProcurador: nulo
				//idMotExc: Por Deuda Reclamada
				//observacion: nula para este release
				proMasDeuExc = this.instanciarProMasDeuExc(deudaJudicial, null, MotExc.ID_POR_DEU_RECLAMADA, null, desTitularPrincipal, TipoSelAlm.getTipoSelAlmDetDeudaJud());
				GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
				continue;  // continua con la otra deuda    			
			}

			//1.7.Si no lo estÃ¡, validar si la deuda se encuentra Indeterminada. 
			//Esta validaciÃ³n se debe realizar verificando si la deuda se encuentra âmarcadaâ como indeterminada, esto es, buscÃ¡ndola en el sistema actual de âIndeterminadosâ que existe en Balance (a definir).
			if( deudaJudicial.getEsIndeterminada()){ 
				//1.8.Si lo estÃ¡, grabar la deuda en el detalle de deuda excluida (tabla gde_proMasDeuExc):
				//idProcesoMasivo: id del envio
				//idDeuda: id de la deuda
				//idProcurador: nulo
				//idMotExc: Por Deuda Indeterminada
				//observacion: (a definir dependiendo de los datos que administre el sistema de indeterminados)
				proMasDeuExc = this.instanciarProMasDeuExc(deudaJudicial, null, MotExc.ID_POR_DEU_INDETERMINADA, null, desTitularPrincipal, TipoSelAlm.getTipoSelAlmDetDeudaJud());
				GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
				continue;  // continua con la otra deuda    			
			}

			//1.9.Si no lo estÃ¡, validar si la deuda se encuentra en Convenio vigente (no debe estar en la tabla gde_convenioDeuda, excepto que el convenio se encuentre en estado âRecompuestoâ o âAnuladoâ, en la tabla gde_convenio).
			Convenio convenio = deudaJudicial.getConvenio(); 
			if(convenio != null){
				//1.10.Si se encuentra, grabar la deuda en el detalle de deuda excluida (tabla gde_proMasDeuExc):
				//idProcesoMasivo: id del envio
				//idDeuda: id de la deuda
				//idProcurador: nulo
				//idMotExc: Por Deuda Convenio
				//observacion: Nro. de convenio y estado del convenio (armar cadena)
				String obs = convenio.getNroConvenio() + " " + convenio.getEstadoConvenio().getDesEstadoConvenio();
				proMasDeuExc = this.instanciarProMasDeuExc(deudaJudicial, null, MotExc.ID_POR_DEU_CONVENIO, obs, desTitularPrincipal, TipoSelAlm.getTipoSelAlmDetDeudaJud());
				GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
				continue;  // continua con la otra deuda    			
			}

			//1.13.Si no lo estÃ¡, y se trate de un envio o pre envio, validar que la deuda no se encuentre afectada por ninguna exenciÃ³n que impida el envÃ­o a judiciales. Esto es, 
			//1.13.1.Que no exista en la tabla exe_cueExe la cuenta asociada a la deuda, considerando la vigencia de los registros respecto de la fecha de envÃ­o, es decir, si no estÃ¡ vigente, la deuda no se encuentra afectada por la exenciÃ³n.. 
			//1.13.2.Si existe y estÃ¡ vigente, validar que dicha exenciÃ³n no permita al envÃ­o a judiciales (campo procesoMasivo de la tabla exe_exencion igual a 1).
			//1.13.3.Si no permite el Envio a Judiciales validar que la deuda no exista en la tabla XXX (a definir), considerando la vigencia de los registros respecto de la fecha de envÃ­o, es decir, si no estÃ¡ vigente la deuda no se encuentra afectada por la exenciÃ³n.
			if (this.getTipProMas().getId().equals(TipProMas.ID_PRE_ENVIO_JUDICIAL)) {
				
				Exencion exencion = cuenta.exentaEnvioJud(this.getFechaEnvio(), deudaJudicial.getFechaVencimiento(), this.getCueExeCache());
				if(exencion != null){
					//1.14.Si se encuentra afectada por una exenciÃ³n, grabar la deuda en el detalle de deuda excluida (tabla gde_proMasDeuExc):
					//idProcesoMasivo: id del envio
					//idDeuda: id de la deuda
					//idProcurador: nulo
					//idMotExc: Por Deuda ExcluÃ­da por ExenciÃ³n
					//observacion: CÃ³digo y DescripciÃ³n de la ExenciÃ³n (armar cadena)
					String obs = exencion.getCodExencion() + " " + exencion.getDesExencion();
					proMasDeuExc = this.instanciarProMasDeuExc(deudaJudicial, null, MotExc.ID_POR_EXENCION, obs, desTitularPrincipal, TipoSelAlm.getTipoSelAlmDetDeudaJud());
					GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
					proMasDeuExc.addErrorMessages(this);
					continue;  // continua con la otra deuda    			
				}
			}

			// 1.1.Validar que no exista en la seleccion almacenada correspondiente a la deuda a excluir.
			boolean esExcluida = this.esExcluida(deudaJudicial);
			if (esExcluida){
				/* 1.2.Si existe, grabar la deuda en el detalle de deuda exclui�da (tabla gde_proMasDeuExc):
				   idProcesoMasivo: id del envio
				   idDeuda: id de la deuda
				   idProcurador: nulo
				   idMotExc: Excluida para el Envio.  
				   observacion: nula.
				*/
				proMasDeuExc = this.instanciarProMasDeuExc(deudaJudicial, null, MotExc.ID_POR_EXC_ENVIO, null, desTitularPrincipal, TipoSelAlm.getTipoSelAlmDetDeudaJud());
				GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
				continue;  // continua con la otra deuda
			}

			//1.11.Si no lo esta, validar que la deuda no se encuentre exclui�da por otras Areas, 
			// esto es, no debe existir en la tabla pad_cueExcSelDeu, considerar la vigencia de los registros respecto de la fecha de envÃ­o, es decir, si no estÃ¡ vigente no excluir.
			if (new Integer(1).equals(this.getConCuentaExcSel())) {
				List<Area> listArea = deudaJudicial.getEsExcluidaPorArea(this.exentaAreaCache);
				if(listArea.size() > 0){
					String desAreas = "";
					for (Area area : listArea) {
						if(desAreas.equals("")){
							desAreas += area.getDesArea() ;
						}else{
							desAreas += ", " + area.getDesArea() ;
						}
					}
					proMasDeuExc = this.instanciarProMasDeuExc(deudaJudicial, null, MotExc.ID_POR_EXC_OTRAS_AREAS, desAreas, desTitularPrincipal, TipoSelAlm.getTipoSelAlmDetDeudaJud());
					GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
					proMasDeuExc.addErrorMessages(this);
					continue;  // continua con la otra deuda    			
				}			
			}
			//1.19.Si no se encuentra en la lista de procuradores excluÃ­dos en el envÃ­o, grabar la deuda en el detalle de deuda incluida (tabla gde_proMasDeuInc):
			//idProcesoMasivo: id del envio
			//idDeuda: id de la deuda
			//idProcurador: id del procurador asignado de la deuda judicial
			//obsMotNoVueAtr: nula
			//saldoHistorico: saldo historico de la deuda.
			//saldoActualizado: saldo de la deuda actualizado a la fecha del envi�o. en la base de datos es requerido
			// asunto titular principal
			ProMasDeuInc proMasDeuInc = this.instanciarProMasDeuInc(deudaJudicial, null, desTitularPrincipal);
			GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuInc(proMasDeuInc);
			proMasDeuInc.addErrorMessages(this);
		}
		SiatHibernateUtil.currentSession().getTransaction().commit();
		SiatHibernateUtil.closeSession();
		SiatHibernateUtil.currentSession().beginTransaction();
	}
		
	private void incluirExcluirListConvenioCuota(List<Long> listIdCuota) throws Exception {
		Cuenta cuenta = null; //cuenta de la deuda que se esta procesando.
		String desTitularPrincipal = ""; //descripcion del titular principal de la cuenta
		long idCuentaOld = 0;
		int count = 0;
		
		for(Long idConvenioCuota: listIdCuota) { 
			//commit por cada registro
			if (++count % 1000 == 0) {
				SiatHibernateUtil.currentSession().getTransaction().commit();
				SiatHibernateUtil.closeSession();
				SiatHibernateUtil.currentSession().beginTransaction();
				if (cuenta != null)
					SiatHibernateUtil.currentSession().refresh(cuenta);
			}
			AdpRun.changeRunMessage("Procesando Convenios cuota - " + count + " de " + listIdCuota.size(), 60);
			
			log.debug("Procesando idConvenioCuota: " + idConvenioCuota);
			ConvenioCuota convenioCuota = ConvenioCuota.getById(idConvenioCuota);
			
			if (cuenta == null || idCuentaOld != convenioCuota.getConvenio().getCuenta().getId()) {
				cuenta = convenioCuota.getConvenio().getCuenta();
				desTitularPrincipal = cuenta.getDesTitularPrincipal();
				idCuentaOld = cuenta.getId();
			}

			EstadoConCuo estadoConCuo = convenioCuota.getEstadoConCuo(); 
			// IMPAGO, PAGO_BUENO, PAGO_A_CUENTA
			
			MotExc motExc = null;
			ProMasDeuExc proMasDeuExc = null; 

			// validar que estado sea impago
			if(!(EstadoConCuo.ID_IMPAGO == estadoConCuo.getId().longValue())){
				//1.4.Si no lo es, grabar la deuda en el detalle de deuda excluida (tabla gde_proMasDeuExc):
				//idProcesoMasivo: id del envio
				//idDeuda: id de la deuda
				//idProcurador: nulo
				//idMotExc: Estado o via invalido
				//observacion: nula

				motExc = MotExc.getById(MotExc.ID_CONV_CUOT_NO_IMPAGO);
				proMasDeuExc = this.instanciarProMasDeuExc(convenioCuota, motExc.getId(), null, desTitularPrincipal);
				GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
				continue;  // continua con la otra deuda
			}

			//1.5.Si lo es, validar si la deuda se encuentra Reclamada. 
			if(convenioCuota.getEsReclamada()){
				//1.6.Si lo estÃ¡, grabar la deuda en el detalle de deuda excluida (tabla gde_proMasDeuExc):
				//idProcesoMasivo: id del envio
				//idDeuda: id de la deuda
				//idProcurador: nulo
				//idMotExc: Por Deuda Reclamada
				//observacion: nula para este release
				proMasDeuExc = this.instanciarProMasDeuExc(convenioCuota, MotExc.ID_POR_CONV_CUOT_RECLAMADA, null, desTitularPrincipal);
				GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
				continue;  // continua con la otra deuda    			
			}

			//1.7.Si no lo estÃ¡, validar si la deuda se encuentra Indeterminada. 
			//Esta validaciÃ³n se debe realizar verificando si la deuda se encuentra âmarcadaâ como indeterminada, esto es, buscÃ¡ndola en el sistema actual de âIndeterminadosâ que existe en Balance (a definir).
			if(convenioCuota.getEsIndeterminada()){ 
				//1.8.Si lo estÃ¡, grabar la deuda en el detalle de deuda excluida (tabla gde_proMasDeuExc):
				//idProcesoMasivo: id del envio
				//idDeuda: id de la deuda
				//idProcurador: nulo
				//idMotExc: Por Deuda Indeterminada
				//observacion: (a definir dependiendo de los datos que administre el sistema de indeterminados)
				proMasDeuExc = this.instanciarProMasDeuExc(convenioCuota, MotExc.ID_POR_CONV_CUOT_INDETERMINADA, null, desTitularPrincipal);
				GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
				continue;  // continua con la otra deuda    			
			}

			//1.9.Si no lo estÃ¡, validar si la deuda se encuentra en Convenio vigente (no debe estar en la tabla gde_convenioDeuda, excepto que el convenio se encuentre en estado âRecompuestoâ o âAnuladoâ, en la tabla gde_convenio).
			Convenio convenio = convenioCuota.getConvenio();
			if(EstadoConvenio.ID_VIGENTE != convenio.getEstadoConvenio().getId().longValue()){
				
				//1.10.Si se encuentra, grabar la deuda en el detalle de deuda excluida (tabla gde_proMasDeuExc):
				//idProcesoMasivo: id del envio
				//idDeuda: id de la deuda
				//idProcurador: nulo
				//idMotExc: Por Deuda Convenio
				//observacion: Nro. de convenio y estado del convenio (armar cadena)
				String obs = convenio.getNroConvenio() + " " + convenio.getEstadoConvenio().getDesEstadoConvenio();
				proMasDeuExc = this.instanciarProMasDeuExc(convenioCuota, MotExc.ID_POR_CONV_CUOT_NO_VIGENTE, obs, desTitularPrincipal);
				GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
				continue;  // continua con la otra deuda    			
			}

			// Se decidio no aplicar la determinacion si la cuenta del convenio esta exenta del envio a judicial			
			// No se cargan registros de convenios de cuotas en la seleccion almacenada excluida
			
			//1.19.Si no se encuentra en la lista de procuradores excluÃ­dos en el envÃ­o, grabar la deuda en el detalle de deuda incluida (tabla gde_proMasDeuInc):
			//idProcesoMasivo: id del envio
			//idDeuda: id de la deuda
			//idProcurador: id del procurador asignado
			//obsMotNoVueAtr: nula
			//saldoHistorico: saldo histÃ³rico de la deuda.
			//saldoActualizado: saldo de la deuda actualizado a la fecha del envÃ­o. en la base de datos es requerido
			// asunto titular principal
			ProMasDeuInc proMasDeuInc = this.instanciarProMasDeuInc(convenioCuota, null, desTitularPrincipal);
			GdeDAOFactory.getProcesoMasivoDAO().insertProMasDeuInc(proMasDeuInc);
			proMasDeuInc.addErrorMessages(this);
		}
		SiatHibernateUtil.currentSession().getTransaction().commit();
		SiatHibernateUtil.closeSession();
		SiatHibernateUtil.currentSession().beginTransaction();
	}


	
	/**
	 * Determina si contiene Deudas Incluidas el Envio Judicial
	 * @return boolean
	 */
	public boolean contieneDeudasIncluidas(){
		return GdeDAOFactory.getProMasDeuIncDAO().contieneDeudasIncluidas(this);
	}

	/**
	 * Determina si contiene Deudas Excluidas el Envio Judicial
	 * @return boolean
	 */
	public boolean contieneDeudasExcluidas(){ 
		return GdeDAOFactory.getProMasDeuExcDAO().contieneDeudasExcluidas(this); 
	}

	///// Realizacion del Envio de Deuda a procuradores

	/**
	 * Realiza el envio de deuda a procuradores:
	 * Por cada deuda incluida: ejecuta las validaciones.
	 * Genera la planilla de deuda para cada procurador asignado.
	 * Genera las constancias de deuda con sus detalles 
	 * Mueve la deuda administrativa y sus conceptos a judicial,
	 * Marca el procesamiento sobre cada ProMasDeuInc. (1 procesada, 0 no procesada). 
	 * Generar planillas y archivos xml de salida.
	 * Genera los pdf de planillas de envio de deuda a cada procurador
	 * Realiza commit y beginTransaction.
	 * @param path
	 * @throws Exception
	 */
	public void realizarEnvio(String path) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		// Para cada deuda de la deuda a incluida:
		Integer firstResult = 0;
		Integer maxResults = 100;

		// iterar la lista de ProMasDeuInc del Envio de manera paginada
		boolean contieneProMasDeuInc = true;

		log.info("ProcesoMasivo: Comienza Envio: idProcesoMasivo=" + this.getId());
		
		//limpiamos algun archivo generado en el paso 3 (por si es un reproceso)
		this.getCorrida().deleteListFileCorridaByPaso(3);

		while (contieneProMasDeuInc){
			SiatHibernateUtil.currentSession().getTransaction().commit();
			SiatHibernateUtil.closeSession(); // cierro y abro la sesion para mejorar la performance
			SiatHibernateUtil.currentSession().beginTransaction();
			
			// obtiene la lista de proMasDeuInc
			log.info("ProcesoMasivo: Recuperando lista de DeudaIncluida");
			List <ProMasDeuInc> listProMasDeuInc = this.getListProMasDeuInc(firstResult, maxResults, TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM); 
			contieneProMasDeuInc = (listProMasDeuInc.size() > 0);
			for (ProMasDeuInc proMasDeuInc : listProMasDeuInc) {				
				//limpia de la promasdeuinc las deudas no enviables. 		
				boolean valid = validateRealizarEnvio(proMasDeuInc);
				//if (valid && SiNo.SI.getId().equals(this.getGeneraConstancia())) {
				//	planillaConstancia(proMasDeuInc, procPlaMap);
				//}
			}	
			firstResult += maxResults; // incremento el indice del 1er registro
			AdpRun.changeRunMessage("Validando deuda, verficando que este apta para envio. Deudas validadas:" + firstResult, 0);			
		}
		// agregamos a la corrida el parametro de los procuradores con sus idconstancias y planillas
		// este parametro luego lo usamos en en los adapter para mostrar los links a las planilla y envios
		//for(Long idProcurador : procPlaMap.keySet()) {
		//	String pvalue = idProcurador + "," + procPlaMap.get(idProcurador); 
		//	AdpRun.currentRun().putParameter(ProcesoMasivoVO.ADP_PARAM_PROCPLAMAP, pvalue);
		//}
		
		//generamos constancias y planillas
		if (SiNo.SI.getId().equals(this.getGeneraConstancia())) {
			GdeDAOFactory.getProcesoMasivoDAO().generarPlanillasConstancias(this);
		}

		SiatHibernateUtil.currentSession().getTransaction().commit();
		SiatHibernateUtil.closeSession(); // cierro 
		
		//Ahora movemos la deuda.
		GdeDAOFactory.getProcesoMasivoDAO().moverDeudaAdmAJud(this);		

		//reestablecemos session hibernate
		SiatHibernateUtil.currentSession().beginTransaction();
		
		if (!this.hasError()) {
			log.info("ProcesoMasivo: Comienza Generar Reportes de Deuda Enviada");
			AdpRun.changeRunMessage("ProcesoMasivo: Comienza Generar Reportes de Deuda Enviada." + this.getErrorMessage(), 0);
			List<Procurador> listProcuEnvio = GdeDAOFactory.getProMasDeuIncDAO().getListProcuradorEnvio(this);
			for(Procurador procurador : listProcuEnvio) {
				log.info("ProcesoMasivo: Generando Reportes Deuda Enviada Procurador:" + procurador.getId());
				AdpRun.changeRunMessage("ProcesoMasivo: Generar Reportes de Deuda Enviada. Procurador: " + procurador.getId() + ". " + this.getErrorMessage(), 0);
				//if (procurador.getId().longValue() == 83L) { // Cobranza Judicial que esta en la gde_prorec para tgi 
				//	//es cobranza judicial, no le generamos planilla.
				//	continue;
				//}
				
				String filename = "ReporteDeudaEnvioProcurador_" + this.getId() + "_" + procurador.getId() + ".csv";
				this.generarCsvDeudaEnvioProcurador(procurador, path, filename);
			}

			log.info("ProcesoMasivo: Comienza Generar Archivos CD Procuradores");
			AdpRun.changeRunMessage("Generando Archivos CD para procuradores.", 0);
			this.generarArchivosCDProcuradores();
			
			SiatHibernateUtil.currentSession().getTransaction().commit();
			SiatHibernateUtil.closeSession();
			SiatHibernateUtil.currentSession().beginTransaction();
		}
		

		
		// genera los pdf de totales del envio
		log.info("ProcesoMasivo: Generar Reportes de Totales de Deuda");
		AdpRun.changeRunMessage("ProcesoMasivo: Generar Reportes de Totales de Deuda." + this.getErrorMessage(), 0);
		this.generarTotalesEnvio();

		AdpRun.changeRunMessage("ProcesoMasivo: Generar Reportes de Deuda Enviada. Finalizado." + this.getErrorMessage(), 0);
		log.info("ProcesoMasivo: Fin Realizar Envio: idProcesoMasivo=" + this.getId());
	}

	public void generarArchivosCDProcuradores() throws Exception {
		List<Procurador> listProcuEnvio;
		Map<String, String> cacheObjImp = new HashMap<String, String>();
		String processDir;

		processDir = AdpRun.getRun(this.getCorrida().getId()).getProcessDir(AdpRunDirEnum.SALIDA);
		GdeDAOFactory.getProcesoMasivoDAO().loadCacheCDProcuradores(cacheObjImp,
				this.getFechaEnvio(),
				this.getRecurso().getId());
		
		/*
		int i=0;
		for(String key : cacheObjImp.keySet()) {
			String value = cacheObjImp.get(key);
			log.debug("dump cache: key:" + key + " value: " + value);
		}
		*/
		
		listProcuEnvio = GdeDAOFactory.getProMasDeuIncDAO().getListProcuradorEnvio(this);
		for(Procurador procurador : listProcuEnvio) {
			AdpRun.changeRunMessage("Generando archivo CD para procurador: " + procurador.getId() + "-" + procurador.getDescripcion(), 0);
			String filename = GdeDAOFactory.getProcesoMasivoDAO().generarArchivoCDProcurador(processDir, this.getId(), procurador.getId(), cacheObjImp);
			
			File file = new File(filename);
			String desProcurador = procurador.getId() + "-" + procurador.getDescripcion();
			String descripcion = "Archivo CD Procurador: " + desProcurador;			
			this.getCorrida().addOutputFileByPaso(3, file.getName(), descripcion, file.getAbsolutePath());
		}
	}

	public List<FileCorrida> getListFileCorrida(Procurador procurador) throws Exception {
		List<FileCorrida> ret = new ArrayList<FileCorrida>();
		List<FileCorrida> listFileCorridaPorPaso = FileCorrida.getListByCorridaYPaso(this.getCorrida(), PasoCorrida.PASO_TRES);
		String pattern = "_" + this.getId() + "_"  + procurador.getId();
		
		for(FileCorrida fc : listFileCorridaPorPaso) {
			String filename = fc.getFileName();
			if (filename.contains(pattern)) {
				ret.add(fc);
			}
		}
		
		return ret;
	}
	
	
	/**
	 * Limpia la realizacion del envio.
	 * 1 y 2 borra la lista de fileCorrida del paso 3:
	 * 		1 implicaria borrar los archivos pdf de las planillas de envio de deuda a procuradores
	 * 		2 implicaria borrar los archivos planillas: deuda enviada, deuda excluida, total por procurador, total por periodo");
	 * 3 pasar las deudas judiciales a deudas administrativas que se encuentran en cada promasdeuinc con sus DeuJudRecCon
	 * 4 borrar cada ConDeuDet de cada constancia
	 * 5 borrar los ConDeuTit de cada constancia
	 * 6 borra los HistEstConDeu
	 * 7 borrar cada constancia del proceso masivo
	 * 8 borrar cada historico de la planilla creados HistEstPlaEnvDP
	 * 9 borrar cada planilla creada
	 * 10 no pasar las promasdeuex a promasdeuinc
	 * Marcar como no procesada la proMasDeuInc.
	 * 
	 * Realiza commit y beginTransaction por cada proMasDeuInc.
	 * @throws Exception
	 */
	public void limpiarRealizarEnvio() throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		// borra la lista de fileCorrida del paso 3: 
		//1 borrar los archivos pdf de las planillas de envio de deuda a procuradores
		//2 borrar los archivos planillas: deuda enviada, deuda excluida, total por procurador, total por periodo");
		
		log.info("Eliminacion de la lista de FilCorrida asociada a la corrida y al paso 3");
		this.getCorrida().deleteListFileCorridaByPaso(3);
		// se realiza un commit. No se tienen en cuenta errores previos
		SiatHibernateUtil.currentSession().getTransaction().commit();
		
		//3 pasar las deudas judiciales a deudas administrativas que se encuentran en cada promasdeuinc con sus DeuJudRecCon
		//4 borrar cada ConDeuDet de cada constancia
		//5 borrar los ConDeuTit de cada constancia
		//6 borra los HistEstConDeu  
		//7 borrar cada constancia del proceso masivo
		//8 borrar cada historico de la planilla creados HistEstPlaEnvDP
		//9 borrar cada planilla creada
		//10 no pasar las promasdeuex a promasdeuinc
		// marcar como no procesada la proMasDeuInc.
		
		Integer firstResult = 0;
		Integer maxResults = 100; 

		// iterar la lista de ProMasDeuInc del Envio de manera paginada
		boolean contieneProMasDeuInc = true;
		Long idProMasDeuInc = 0L;

		while (contieneProMasDeuInc){
			// cierro la sesion para mejorar la performance
			SiatHibernateUtil.closeSession();
			// obtiene la lista de proMasDeuInc procesada. 
			// Descartamos los registros que no fueron procesados y menores o iguales al idProMasDeuInc
			List <ProMasDeuInc> listProMasDeuInc = this.getListProMasDeuIncProcesada(firstResult, maxResults, SiNo.SI.getId(), idProMasDeuInc);
			contieneProMasDeuInc = (listProMasDeuInc.size() > 0);
			
			for (ProMasDeuInc proMasDeuInc : listProMasDeuInc) {
					SiatHibernateUtil.currentSession().beginTransaction();
					this.limpiarRealizarEnvio(proMasDeuInc);
					log.info("Vuelta a DeudaAdmin idDeuda: " + proMasDeuInc.getIdDeuda() + " idProMasDeuInc: " + proMasDeuInc.getId());
					SiatHibernateUtil.currentSession().getTransaction().commit();
					idProMasDeuInc = proMasDeuInc.getId();
			}
			
		}

		SiatHibernateUtil.currentSession().beginTransaction();
		if(log.isDebugEnabled()) log.debug(funcName + ": exit");
	}
	
	
	private void limpiarRealizarEnvio(ProMasDeuInc proMasDeuInc) throws Exception{
		
		Long idDeuda = proMasDeuInc.getIdDeuda();
		DeudaJudicial deudaJudicial = DeudaJudicial.getByIdNull(idDeuda);
		if (deudaJudicial != null){
			//3 pasar la deuda judicial a deuda administrativa de la promasdeuinc con sus DeuJudRecCon
			this.moverDeudaAAdmin(deudaJudicial);
		}else{
			log.warn("Deuda Judicial no encontrada en la tabla judicial: " + idDeuda + " Continua la limpieza");
		}
		
		/*
		// obtencion de la lista de ConDeuDet para la deuda y el proceso masivo.
		// Por regla de negocios tendria que venir a lo sumo una sola instancia, pero no confiamos en los datos
		List<ConDeuDet> listConDet = GdeDAOFactory.getConDeuDetDAO().getListConDeuDetByProMasIdDeuda(this, idDeuda);
		for (ConDeuDet conDeuDet : listConDet) {
			
			ConstanciaDeu constanciaDeu = conDeuDet.getConstanciaDeu();
			
			//4 borrar la ConDeuDet asociada a la Deuda y al proceso masivo
			if(log.isDebugEnabled()) log.debug("borrar la ConDeuDet");
			constanciaDeu.deleteConDeuDet(conDeuDet);
			
			SiatHibernateUtil.currentSession().flush();
			
			if(constanciaDeu.getListConDeuDet().size() == 0){

				//5 borrar los ConDeuTit de cada constancia
				if(log.isDebugEnabled()) log.debug("borrar los ConDeuTit de cada constancia");
				constanciaDeu.deleteListConDeuTit();
				SiatHibernateUtil.currentSession().flush();
				
				//6 borra los HistEstConDeu de la constancia
				if(log.isDebugEnabled()) log.debug("borra los HistEstConDeu de la constancia");
				constanciaDeu.deleteListHistEstConDeu();
				SiatHibernateUtil.currentSession().flush();
				
				PlaEnvDeuPro plaEnvDeuPro = constanciaDeu.getPlaEnvDeuPro();
				
				//7 borrar la constancia
				if(log.isDebugEnabled()) log.debug("borrar la constancia");
				GdeDAOFactory.getConstanciaDeuDAO().delete(constanciaDeu);
				SiatHibernateUtil.currentSession().flush();
				
				if (plaEnvDeuPro.getListConstanciaDeu().size() == 0){
					
					//8 borrar cada historico de la planilla creados HistEstPlaEnvDP
					if(log.isDebugEnabled()) log.debug("borrar cada historico de la planilla creados HistEstPlaEnvDP");
					plaEnvDeuPro.deleteListHistEstPlaEnvDP();
					SiatHibernateUtil.currentSession().flush();
					
					//9 borrar la planilla Envio Deuda a Procurador
					if(log.isDebugEnabled()) log.debug("borrar la planilla Envio Deuda a Procurador");
					GdeDAOFactory.getPlaEnvDeuProDAO().delete(plaEnvDeuPro);
					SiatHibernateUtil.currentSession().flush();
				}
			}
		}
		*/
		// marco como no procesada
		proMasDeuInc.setProcesada(SiNo.NO.getId());
		
		//10 no pasar las promasdeuex a promasdeuinc
	}

	/**
	 * Realiza el pre envio de deuda judicial.
	 * Ejecuta las mismas validaciones que el envio comun.
	 * @param path lugar donde generar los archivos.
	 * @throws Exception
	 */
	public void realizarPreEnvio(String path) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		// Para cada deuda de la deuda a incluida:
		Long idRecurso = this.getRecurso().getId();
		Integer firstResult = 0;
		Integer maxResults = 1000;
		long n = 0;
		Long total = GdeDAOFactory.getProMasDeuIncDAO().getCantidadRegistrosProMasDeuInc(this);

		// iterar la lista de ProMasDeuInc del Envio de manera paginada
		boolean contieneProMasDeuInc = true;

		// Primer paso limpia la ProMasDeuInc que talvez no pase las validaciones
		// Este paso va sacando las deuda no validas a la tabla de excluidas.
		AdpRun.changeRunMessage(DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_HH_MM_MASK) + " - Realizando validacion sobre la deuda Incluida. La deuda no valida pasa a deuda Excluida.", 0);

		while (contieneProMasDeuInc && !this.hasError()) {
			// obtiene la lista de proMasDeuInc ordenada por 
			List<ProMasDeuInc> listProMasDeuInc = this.getListProMasDeuInc(firstResult, maxResults, null);

			contieneProMasDeuInc = (listProMasDeuInc.size() > 0);
			if (contieneProMasDeuInc) {
				for (ProMasDeuInc proMasDeuInc : listProMasDeuInc) {
					//realizamos validacion del tercer paso
					validatePreEnvio(proMasDeuInc);
					n++;
					AdpRun.changeRunMessage("Realizando Validaciones en Deudas y Convenios - " + (int) n*100/total + "%", 60);
				}
				firstResult += maxResults; // incremento el indice del 1er registro
				Session session = SiatHibernateUtil.currentSession();
				session.flush();
				session.clear();
			}
		}
		
		//flush a batch of updates and release memory:
		SiatHibernateUtil.currentSession().getTransaction().commit();
		SiatHibernateUtil.closeSession();
		SiatHibernateUtil.currentSession().beginTransaction();		
		
		SiatHibernateUtil.currentSession().refresh(this);
		
		// OK. Ahora por cada ProMasDeuInc, obtenemos idDeuda y idCuenta al que pertenecen ordenados por idCuenta.
		// Luego hacemos un corte de control por cuenta, y generamos
		// los Recibo para esa cuenta con la lista de idDeuda que agrupa el idCuenta.
		// Al mismo tiempo por cada recibo vamos generando el XML de salida para luego generar los PDF.
		AdpRun.changeRunMessage("Generando recibos de deuda para cada cuenta.", 0);
		List<Deuda> listDeuda = new ArrayList<Deuda>();
		List<ConvenioCuota> listConvenioCuota = new ArrayList<ConvenioCuota>();
		int ncuentas = 0; //contador de cuentas.

		// Inicializamos el archivo para genearar los xml, y los PDF
		AdpRun run = AdpRun.currentRun();
		String prefix = null;
		String descripcion = "";
		Integer maxCantDeudaxRecibo = Cuenta.MAX_CANT_DEUDA_X_RECIBO_RECONFECCION;
		if (this.getTipProMas().getId().equals(TipProMas.ID_PRE_ENVIO_JUDICIAL)) {
			//prefix = "/notifpreenvio_" + DateUtil.formatDate(this.getFechaEnvio(), DateUtil.YYYYMMDD_MASK) + "_" + this.getId();
			String codRecurso = this.getRecurso().getCodRecurso().toLowerCase();
			prefix = "/Tn_" + codRecurso + "_adm_deuda_z" + (this.getCorrida().getId() % 100);
			descripcion = "Archivo con las notificaciones resultado del Pre Envio Judicial Masiva.";
		} else if (this.getTipProMas().getId().equals(TipProMas.ID_RECONFECCION)) {
			prefix = "/reconfmasiva_" + DateUtil.formatDate(this.getFechaEnvio(), DateUtil.YYYYMMDD_MASK) + "_" + this.getId();
			descripcion = "Archivo Pdf con recibos resultado de la Reconfecci&oacute;n Masiva.";
			maxCantDeudaxRecibo = Cuenta.MAX_CANT_DEUDA_X_RECIBO_TGI;
		} else {
			throw new Exception("No se pudo asignar formulario: Tipo Proceso Masivo (gde_tipProMas) desconocido: " + this.getTipProMas().getId());
		}

		//Determinacion del PrintModel a usar para generar la salida
		PrintModel pmRecibo = null;
		String codFormulario = run.getParameter("codFormulario");
		if (codFormulario != null) {
			int outputFormat = Integer.parseInt(run.getParameter("outputFormat"));
			pmRecibo = Formulario.getPrintModelForPDF(codFormulario);
			pmRecibo.setRenderer(outputFormat);
		} else {
			throw new Exception("No se pudo asignar formulario: No existe parametro codFormulario. IdProcesoMasivo:" + this.getId());		
		}

		n = 0; //contador de registros para porcentage
		String fileNamePrefix = path + prefix;		
		long idCuenta = 0L;
		long idElem = 0L;
		long idTipoElem = 0L;
		long idCuentaOld = 0L;
		Object[] row = null;
		boolean exit = false;
		boolean deudaSigueTitular = Integer.valueOf(1).equals(this.getRecurso().getEsDeudaTitular());
		HashMap<String, ProMasPrinter> printerMap = new HashMap<String, ProMasPrinter>(); 
		//Esta clase se encarga de buscar los repartidores segun la catastral
		AsignaRepartidor asignadorTgi = new AsignaRepartidor(idRecurso, this.getFechaEnvio());

		// Mapa para Generar Reporte de total de notificaciones/boletas generadas por repartidor (broche)
		Map<String,Integer> mapForTotalReport = new HashMap<String, Integer>();

		List listDeuCta = GdeDAOFactory.getProMasDeuIncDAO().getListCuentaDeudaByProcesoMasivo(this);
		Iterator it = listDeuCta.iterator();
		while (!exit) {
			if (!it.hasNext()) {
				exit = true;
			}
			
			if (it.hasNext()) {
				n++; //contador de avance
				row = (Object[]) it.next();
				idCuenta = ((Long) row[0]).longValue();
				idElem = ((Long) row[1]).longValue();
				idTipoElem = ((Long) row[2]).longValue();
				
				if (idCuentaOld == 0) { //si es la primer cuenta que leo, la seteo como old 
					idCuentaOld = idCuenta; 
				}
				log.debug("procesomasivo: leyendo idcuenta:" + idCuenta + " idElem:" + idElem + " idTipoElem:" + idTipoElem);
			}

			if (exit || idCuenta != idCuentaOld) {
				// Ordenamos la lista de deuda y la lista de convenioscuota
				Deuda.ordenarPeriodoAnio(listDeuda);
				ConvenioCuota.ordenarNroCuota(listConvenioCuota);
				
				// si tengo que salir, genero el recibo con la cuenta actual, 
				// sino, uso el viejo.
				Long idCuentaTmp = exit ? idCuenta : idCuentaOld; 

				if (deudaSigueTitular) {
					// para estos casos es posible que se genere mas de un recibo para una cuenta.
					generarRecibosPorCuentaTitular(idCuentaTmp, listDeuda, listConvenioCuota, printerMap, fileNamePrefix, asignadorTgi, maxCantDeudaxRecibo, mapForTotalReport);
				} else {
					generarRecibo(idCuentaTmp, null, listDeuda, listConvenioCuota, printerMap, fileNamePrefix, asignadorTgi, maxCantDeudaxRecibo, mapForTotalReport);
				}

				// limpiamos listas y caches de hibernate, preparamos para el proximo corte.
				idCuentaOld = idCuenta;
				listDeuda.clear();
				listConvenioCuota.clear();
				if (exit || ++ncuentas % 50 == 0) { // cada n cuentas, limpiamos todo para que no explote la memoria
					log.debug("Cuentas procesadas: " + ncuentas + " reiniciando Hibernate...");
					AdpRun.changeRunMessage("Generando recibos XML..." + (int) n*100/total + "%", 0);
					//flush a batch of updates and release memory:
					SiatHibernateUtil.currentSession().getTransaction().commit();
					SiatHibernateUtil.closeSession();
					SiatHibernateUtil.currentSession().beginTransaction();
					SiatHibernateUtil.currentSession().refresh(this);
				}
			}
			
			if (idTipoElem == TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_ADM) {				
				Deuda deuda = DeudaAdmin.getByIdNull(idElem);
				if (deuda != null) {
					listDeuda.add(deuda);
				} else {
					AdpRun.logRun("No se encontro Deuda en tabla Administrativa. Deuda Ignorada.");
				}
			} else if(idTipoElem == TipoSelAlm.TIPO_SEL_ALM_DET_DEUDA_JUD) {
				Deuda deuda = DeudaJudicial.getByIdNull(idElem);
				if (deuda != null) {
					listDeuda.add(deuda);
				} else {
					AdpRun.logRun("No se encontro Deuda en tabla Judicial. Deuda Ignorada.");
				}
			} else if (idTipoElem == TipoSelAlm.TIPO_SEL_ALM_DET_CONV_CUOT_ADM || idTipoElem == TipoSelAlm.TIPO_SEL_ALM_DET_CONV_CUOT_JUD) {
				listConvenioCuota.add(ConvenioCuota.getById(idElem));
			}
	
		}

		// cerramos todos los printer / spool
		for(String key: printerMap.keySet()) {
			ProMasPrinter printer = printerMap.get(key);
			endPrintModel(printer);
		}
		
		//Liberamos la lista de lista de id deudas y id cuenta por que puede ser muy grande
		//y el proximo paso consume bastante memoria.
		listDeuCta.clear();

		// OK. Ya generamos el xml gigante con los datos de todos los recibos a imprimir
		// Ahora llamos a fop para que lo convierta a pdf.
		// Este paso es muy largo y consume cerca de un 1G de memoria aprox para 
		// generar 22mil recibos aprox. 		
		AdpRun.changeRunMessage("Generando Archivos de salida... (" + ncuentas + " cuentas)", 0);
		
		int i = 0;
		List<String> listOutFilename = new ArrayList<String>(); //lista de archivos de salida generados

		for(String key: printerMap.keySet()) {
			i++;
			String nombre = "";
			if (this.getTipProMas().getId().equals(TipProMas.ID_PRE_ENVIO_JUDICIAL)) {
				if (this.recurso.getCategoria().getId().longValue()==Categoria.ID_TGI.longValue()){
					nombre = "Pre Envio Judicial. Broche " + key;
				}else if (this.recurso.getCategoria().getId().longValue()==Categoria.ID_CDM.longValue()){
					nombre = "Pre Envio Judicial. Obra " + key;
				}
			} else if (this.getTipProMas().getId().equals(TipProMas.ID_RECONFECCION)) {
				nombre = "Reconf. Masiva. Broche " + key;
			}
			AdpRun.changeRunMessage(nombre + ": " + i + " de " + printerMap.size(), 0);

			String filename = "";
			File fileXml;
			if (this.getTipProMas().getId().equals(TipProMas.ID_PRE_ENVIO_JUDICIAL)) {
				// Formato de nombre especificado por ImpriPost
				filename = printerMap.get(key).txtFilename;
				fileXml = new File(printerMap.get(key).writerFilename + ".xml");
			} else {
				filename = printerMap.get(key).writerFilename;
				fileXml = new File(filename + ".xml");
			}
			
			String extSalida = "";
			if(pmRecibo.getRenderer()==PrintModel.RENDER_PDF)
				extSalida= ".pdf";
			else if(pmRecibo.getRenderer()==PrintModel.RENDER_TXT){				
				extSalida= ".txt";				
			}

			if (this.getTipProMas().getId().equals(TipProMas.ID_PRE_ENVIO_JUDICIAL) && pmRecibo.getRenderer()==PrintModel.RENDER_TXT) {
				// si es un pre envio y formato txt, no hacemos la conversion via xsl, 
				// porque ya se fue generando durante el procesamiento en el metodod: generarRecibo()
				// ver en ese metodo llamada a format() de LiqNotificacionPrinter()
			} else {
				FileOutputStream outPdf = new FileOutputStream(filename + extSalida, false);
				pmRecibo.fopRender(fileXml, outPdf);
				outPdf.close();
			}
			
			Long orden = NumberUtil.getLong(key);
			if(orden==null)
				orden = 1000000L; //para que los que sean "fuera de zona" o etc, aparezcan al final

			listOutFilename.add(filename + extSalida);
			this.getCorrida().addOutputFileOrdenada(nombre, descripcion, filename + extSalida, orden);
		}

		AdpRun.changeRunMessage("Generarando Reporte de total de notificaciones/boletas generadas por broche ", 0);
		// Generar Reporte de total de notificaciones/boletas generadas por repartidor (broche)
		if (this.getTipProMas().getId().equals(TipProMas.ID_PRE_ENVIO_JUDICIAL) || this.getTipProMas().getId().equals(TipProMas.ID_RECONFECCION)) {
			this.generarTotalesNotifOReciboPorBrocheEnvio(mapForTotalReport);
		}
			
		AdpRun.changeRunMessage("Copiando a directorio 'ultimo'", 0);
		String dirSalida = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);
		File dirUltimo = new File(dirSalida, "ultimo");
		dirUltimo.mkdir();
		AdpRun.deleteDirFiles(dirUltimo);
		for(String filename: listOutFilename) {
			File src = new File(filename);
			AdpRun.copyFile(src, dirUltimo);
		}
		
		AdpRun.changeRunMessage("Proceso de Reconfecci�n finalizado con Exito.", 0);
		if(log.isDebugEnabled()) log.debug(funcName + ": exit");
	}

	
	//
	private void beginPrintModel(ProMasPrinter printer) throws Exception {
		printer.print.writeDataBegin(printer.writer);
		printer.print.writeString(printer.writer, "<LiqRecibos><ListLiqReciboVO>\n");
	}

	private void endPrintModel(ProMasPrinter printer) throws Exception {
		printer.print.writeString(printer.writer, "</ListLiqReciboVO></LiqRecibos>\n");
		printer.print.writeDataEnd(printer.writer);
		printer.writer.close();
		printer.txtWriter.close();
	}


	/**
	 * issue 5307
	 * Analiza el listadeuda, y genera varios listDeuda por idCuentaTitular segun
	 * los criterios habituales deudaSigue al titular.
	 * Luego con esta lista de lista de deudas llama a cada generarReciboDeuda pasandole 
	 * el idCuentaTitular que tiene que usar.
	 * NOTA: Esta manera de resolver el problema no es la de mas performance pero esperamos que los
	 * envios con recursos de deudasiguetitular no tienen el volumen de los de tgi o drei.
	 */
	private void generarRecibosPorCuentaTitular(Long idCuenta, List<Deuda> listDeuda, List<ConvenioCuota> listConvenioCuota, HashMap<String, ProMasPrinter> printerMap, String fileNamePrefix, AsignaRepartidor asignadorTgi, Integer maxCantDeudaxRecibo, Map<String,Integer> mapForTotalReport) throws Exception {
		Map<Long, List<Deuda>> mapTitDeudas = new HashMap<Long, List<Deuda>>(); //lista de deudas de un id titular
		
		//Armamos un mapa de deudas por cada titular que haya en la listDeuda
		for (Deuda d : listDeuda) {
			Cuenta cuenta = Cuenta.getById(idCuenta);
			List<CuentaTitular> listTit = cuenta.getListCuentaTitularVigentesCerrado(d.getFechaVencimiento());
			if (listTit.size() > 0) {
				CuentaTitular tit = listTit.get(0);
				
				List<Deuda> deudas = mapTitDeudas.get(tit.getId());
				if (deudas == null) {
					deudas = new ArrayList<Deuda>();
					mapTitDeudas.put(tit.getId(), deudas);					
				}
				deudas.add(d);
			}
		}
		
		// por cada titular con su listDeudaTit llamamos a generarRecibo
		for(Long idCuentaTitular : mapTitDeudas.keySet()) {
			List<Deuda> deudas = mapTitDeudas.get(idCuentaTitular); 
			generarRecibo(idCuenta, idCuentaTitular, deudas, listConvenioCuota, printerMap, fileNamePrefix, asignadorTgi, maxCantDeudaxRecibo, mapForTotalReport);
		}
	}
	
	/**
	 * Genera un recibo.
	 */
	private void generarRecibo(Long idCuenta, Long idCuentaTitular, List<Deuda> listDeuda, List<ConvenioCuota> listConvenioCuota, HashMap<String, ProMasPrinter> printerMap, String fileNamePrefix, AsignaRepartidor asignadorTgi, Integer maxCantDeudaxRecibo, Map<String,Integer> mapForTotalReport) throws Exception {
		//genera recibos para idCuentaOld
		// Cambio la cuenta: hago unos recibitos con los id de cuenta acumulados.
		ProMasPrinter printer = null; //spool de broches
		Cuenta cuenta = Cuenta.getById(idCuenta);
		Canal canalCmd = Canal.getById(Canal.ID_CANAL_CMD);

		String logMsg = "Generando recibos para Cuenta: numero: " + cuenta.getNumeroCuenta() + " id: " + cuenta.getId() + " lista idDeuda: " + "(" + listDeuda.size() + " elementos): " + ListUtil.getStringIds(listDeuda);
		log.info(logMsg);
		if (AdpRun.currentRun() != null) {
			AdpRun.currentRun().logDebug(new Date() + ": " + logMsg);
		}

		// determinamos el broche del repartidor que le corresponde a la cuenta
		// una vez que tengamos el brocheKey vamos a poder saber que spooler o printer a usar.
		String printerKey = "";
		Broche broche = cuenta.getBroche();
		if (getRecurso().getCategoria().getId().longValue()==Categoria.ID_TGI.longValue()){
			if (broche == null) {
				String catastral = cuenta.getObjImp().getClaveFuncional();
				Long idBroche = asignadorTgi.buscarIdBrochePorCatastral(catastral);
				if (idBroche == null) {
					printerKey = "sinrepartidor";
				} else {
					broche = Broche.getById(idBroche);
					printerKey = idBroche.toString();
				}
			} else {
				printerKey = "fuerazona";
			}
		}else if (getRecurso().getCategoria().getId().longValue()==Categoria.ID_CDM.longValue()){
			Obra obra = cuenta.getObra();
			if (obra !=null){
				printerKey=cuenta.getObra().getNumeroObra().toString();
			}else{
				printerKey="fuerazona";
			}
/// Agregado para DReI/ETuR		
		}else if ((getRecurso().getCategoria().getId().longValue()==Categoria.ID_DREI.longValue())
		        ||(getRecurso().getCategoria().getId().longValue()==Categoria.ID_ETUR.longValue()))
			{
			String catastral = cuenta.getObjImp().getDefinitionValue().getValor("Catastral").toString();
		        String logMsg2 = "Catastral recuperada para cuenta: " + cuenta.getNumeroCuenta() + ": " + catastral;
		        log.info(logMsg2);

			Long idBroche = asignadorTgi.buscarIdBrochePorCatastral(catastral);
			if (idBroche == null) {
				printerKey = "sinrepartidor";
			} else {
				broche = Broche.getById(idBroche);
				printerKey = idBroche.toString();
			}
         }else{
		        printerKey = "sinrepartidor";
         }


		//!!!De aca salimos con broche seteado al que tenemos que usar. puede ser null
		//   para los casos que no se pudo asignar broche
		

		// buscamos el printer para el broche adecuado
		printer = printerMap.get(printerKey);
		if (printer == null) { //no existe, creamos el printer, su archivo y su printmodel.
			printer = new ProMasPrinter();
			//printer.writer = new BufferedWriter(new FileWriter(fileNamePrefix + "_" + brocheKey + ".xml", false));
			printer.print = new PrintModel();
			printer.print.putCabecera("FileSharePath", SiatParam.getString("FileSharePath"));
			printer.print.setExcludeFileName("/publico/general/reportes/reconfmasiva.exclude");
			//Pasamos todos los parametros de la corrida al PrintModel.
			Map<String, String> map = AdpRun.currentRun().getParameters();
			for(String key : map.keySet()) {
				String value = map.get(key);
				printer.print.putFormulario(key, "", value==null?"":value);
			}
			
			//creamos archivos de salida
			printer.writerFilename = fileNamePrefix + "_" + printerKey;
			printer.writer = new OutputStreamWriter(new FileOutputStream(printer.writerFilename + ".xml"), "ISO-8859-1");
			printer.txtFilename = fileNamePrefix + "_" + printerKey + "." + DateUtil.formatDate(this.getFechaEnvio(), DateUtil.yyMMdd_MASK);
			printer.txtWriter = new OutputStreamWriter(new FileOutputStream(printer.txtFilename + ".txt"), "ISO-8859-1");
			beginPrintModel(printer);
			
			//almacenamos el printer en el tablero/mapa
			printerMap.put(printerKey, printer);
		}

		if (this.getTipProMas().getId().equals(TipProMas.ID_PRE_ENVIO_JUDICIAL)) {
			LiqNotificacionVO liqNotifVO = obtenerLiqNotificacionVO(cuenta, idCuentaTitular, listDeuda, listConvenioCuota); 
			//seteamos el broche calculado por el asignador
			if (broche == null) {
				liqNotifVO.getCuenta().setDesBroche("");
			} else {
				liqNotifVO.getCuenta().setDesBroche(broche.getId().toString());
			}

			int outputFormat = Integer.parseInt(AdpRun.currentRun().getParameter("outputFormat"));
			if (outputFormat == PrintModel.RENDER_TXT ) {
				LiqNotificacionPrinter lnp = new LiqNotificacionPrinter();
				String str = LiqNotificacionPrinter.format(liqNotifVO, AdpRun.currentRun().getParameters());
				printer.txtWriter.write(str);
			} else {
				printer.print.writeDataObject(printer.writer, liqNotifVO, 2); // se necesita nivel 4 antes era 2
			}
		} else if (this.getTipProMas().getId().equals(TipProMas.ID_RECONFECCION)) {
			//Ahora ya se en que spool/printer lo tengo que generar.
			//Genero el recibo en laDB y luego lo imprimo.
			List<Recibo> listRecibo = cuenta.reconfeccionar(listDeuda, this.getFechaEnvio(), this.getFechaEnvio(), canalCmd, null, this, false, broche!=null?broche.getId():null, maxCantDeudaxRecibo,false);
			//Serializamos la lista de Recibos
			LiqDeudaBeanHelper deudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
			LiqCuentaVO liqCuenta = deudaBeanHelper.getCuenta(LiqDeudaBeanHelper.OBJIMP);

			if (broche == null) {
				liqCuenta.setDesBroche("");
			} else {
				liqCuenta.setDesBroche(broche.getId().toString());
			}
			for(Recibo recibo : listRecibo) {
				LiqReciboVO liqReciboVO = ReciboBeanHelper.getReciboVO(recibo, liqCuenta);
				printer.print.writeDataObject(printer.writer, liqReciboVO, 4); // se necesita nivel 4 antes era 2
			}			
		}
		
		// Se agrega informacion al mapa para generar el Reporte de total de notificaciones/boletas generadas por repartidor (broche)
		if (this.getTipProMas().getId().equals(TipProMas.ID_PRE_ENVIO_JUDICIAL) || this.getTipProMas().getId().equals(TipProMas.ID_RECONFECCION)) {
			Integer totalPorBroche = mapForTotalReport.get(printerKey);
			if (totalPorBroche == null) {
				mapForTotalReport.put(printerKey, 1);
			} else {
				mapForTotalReport.put(printerKey, totalPorBroche + 1);
			}
		}
		
	}

	/**
	 * Genera un LiqNotificacionVO con los datos de la cuenta pasada como parametro, la lista de deudas y 
	 * la lista de convenioCuota. Ademas obtiene de esta ultima lista, los distintos convenios y los setea en 
	 * la lista de convenios.
	 * @param cuenta
	 * @param listDeuda
	 * @param listConvenioCuota
	 * @return
	 * @throws Exception
	 */
	private LiqNotificacionVO obtenerLiqNotificacionVO(Cuenta cuenta, Long idCuentaTitular,
			List<Deuda> listDeuda, List<ConvenioCuota> listConvenioCuota) throws Exception {
		LiqNotificacionVO liqNotificacionVO = new LiqNotificacionVO();
		
		String beneficios = AdpRun.currentRun().getParameter("Beneficios");
		liqNotificacionVO.setBeneficios(beneficios!=null?beneficios:"");
		
		String lugarConcurrencia = AdpRun.currentRun().getParameter("lugarConcurrencia");
		liqNotificacionVO.setLugarConcurrencia(lugarConcurrencia!=null?lugarConcurrencia:"");
					
		//Setea los datos de la cuenta
		if(cuenta!=null) {
			LiqCuentaVO notifCuenta = liqNotificacionVO.getCuenta(); 
			notifCuenta.setIdCuenta(cuenta.getId());
			notifCuenta.setCodRecurso(cuenta.getRecurso().getCodRecurso());
			notifCuenta.setDesRecurso(cuenta.getRecurso().getDesRecurso());
			notifCuenta.setNroCuenta(cuenta.getNumeroCuenta());
			if(cuenta.getObjImp() != null) {
				notifCuenta.setDesClaveFuncional(cuenta.getObjImp().getClaveFuncional());
			} else {
				notifCuenta.setDesClaveFuncional("");
			}
			
			if(cuenta.getBroche()!=null) {
				notifCuenta.setDesBroche(cuenta.getBroche().getDesBroche());
			}

			if (idCuentaTitular == null) {
				notifCuenta.setCuitTitularPrincipalContr(cuenta.getCuitTitularPrincipalContr());
				notifCuenta.setNombreTitularPrincipal(cuenta.getNombreTitularPrincipal());			
				notifCuenta.setDesDomEnv(cuenta.getDesDomEnv());
			} else {
				CuentaTitular ct = CuentaTitular.getById(idCuentaTitular);
				Persona p = PersonaFacade.getInstance().getPersonaById(ct.getIdContribuyente());
				notifCuenta.setCuitTitularPrincipalContr(p.getCuitContr());
				notifCuenta.setNombreTitularPrincipal(p.getRepresent());			
				notifCuenta.setDesDomEnv(p.getDomicilio().getViewDomicilio());
			}
			
			LiqDeudaBeanHelper deudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
			LiqCuentaVO liqCuenta = deudaBeanHelper.getCuenta(LiqDeudaBeanHelper.OBJIMP);
			for(LiqAtrValorVO atr:liqCuenta.getListAtributoObjImp()){
			// Obtiene la Ubicacion
				if(atr.getKey().equals("Ubicacion"))
					liqNotificacionVO.getCuenta().getListAtributoObjImp().add(atr);
			// Obtiene la Catastral (valor de atributo del OI)
				if(atr.getKey().equals("Catastral"))
					liqNotificacionVO.getCuenta().getListAtributoObjImp().add(atr);

			}
		}

		
		// Setea la lista de deudas, previamente las ordena por periodo/anio
		Deuda.ordenarPeriodoAnio(listDeuda);
		
		Double totDeuda = 0D;
		if(listDeuda!=null){
			for(Deuda deuda: listDeuda){
				LiqDeudaVO liqDeuda = new LiqDeudaVO();
				liqDeuda.setIdDeuda(deuda.getId());
				//liqDeuda.setPeriodoDeuda(deuda.getStrPeriodo());
				if (cuenta.getRecurso().getCategoria().getId().longValue() == Categoria.ID_CDM.longValue()){
					String prd= deuda.getStrEstadoDeuda();
					if (!StringUtil.isNullOrEmpty(prd)){
						liqDeuda.setAnio(prd.substring(prd.indexOf("/")+1, prd.length()));
						liqDeuda.setPeriodo(prd.substring(0, prd.indexOf("/")));
					}
				}else{
					liqDeuda.setAnio(deuda.getAnio()!=null?deuda.getAnio().toString():"");
					liqDeuda.setPeriodo(deuda.getPeriodo()!=null?deuda.getPeriodo().toString():"");
				}
				liqDeuda.setActualizacion(deuda.getActualizacion());
				liqDeuda.setDesEstado(deuda.getEstadoView());
				liqDeuda.setDesViaDeuda(deuda.getViaDeuda().getDesViaDeuda());
				liqDeuda.setFechaVencimiento(deuda.getFechaVencimiento());
				liqDeuda.setImporte(deuda.getImporte());

				// Actualiza la deuda
				DeudaAct deuAct = deuda.actualizacionSaldo(this.fechaEnvio);
				liqDeuda.setSaldo(deuda.getSaldo());
				liqDeuda.setActualizacion(deuAct.getRecargo());
				liqDeuda.setTotal(deuAct.getImporteAct());
				totDeuda += liqDeuda.getTotal();
				
				liqNotificacionVO.getListDeuda().add(liqDeuda);
				
			}
			if (totDeuda > 0)
				liqNotificacionVO.setTotalDeudaView(StringUtil.redondearDecimales(totDeuda, 0, 2));
		}
		
		// Setea la lista de Convenio cuota y al mismo tiempo obtiene los distintos convenios
		Double totCuota = 0D;
		List<Long> listAuxIdsCovenio = new ArrayList<Long>();
		
		if(listConvenioCuota!=null){
			for(ConvenioCuota convenioCuota: listConvenioCuota){
				LiqCuotaVO liqCuotaVO = new LiqCuotaVO();
				liqCuotaVO.setIdCuota(convenioCuota.getId());
				liqCuotaVO.setNroCuota(String.valueOf(convenioCuota.getNumeroCuota()));				
				liqCuotaVO.setFechaVto(DateUtil.formatDate(convenioCuota.getFechaVencimiento(), DateUtil.ddSMMSYY_MASK));
				liqCuotaVO.setCapital(convenioCuota.getCapitalCuota());
				liqCuotaVO.setInteres(convenioCuota.getInteres());

				DeudaAct deuAct = convenioCuota.actualizacionImporteCuota(this.fechaEnvio);
				liqCuotaVO.setActualizacion(deuAct.getRecargo());
				liqCuotaVO.setImporteCuota(deuAct.getImporteAct());
				liqCuotaVO.setImporteSellado(convenioCuota.getImporteSellado());
				liqCuotaVO.setFechaPago(DateUtil.formatDate(convenioCuota.getFechaPago(), DateUtil.ddSMMSYY_MASK));
				liqCuotaVO.setDesEstado(convenioCuota.getEstadoConCuo().getDesEstadoConCuo());
				liqCuotaVO.setNroConvenio(convenioCuota.getConvenio().getNroConvenio().toString());
				liqCuotaVO.setOrdenanza(convenioCuota.getConvenio().getPlan().getDesPlan());
				
				totCuota += convenioCuota.getImporteCuota();
				
				// Agrega la cuota a la notificacion
				liqNotificacionVO.getListCuota().add(liqCuotaVO);
				
				//Si el convenio al que pertenece la cuota no esta en la lista auxiliar de ids lo agrega
				Convenio convenio = convenioCuota.getConvenio();
				if(!listAuxIdsCovenio.contains(convenio.getId())){
					LiqConvenioVO liqConvenioVO = new LiqConvenioVO();
					liqConvenioVO.setId(convenio.getId());
					liqConvenioVO.setNroConvenio(String.valueOf(convenio.getNroConvenio()));
					liqConvenioVO.setDesPlan(convenio.getPlan().getDesPlan());
					liqConvenioVO.setDesViaDeuda(convenio.getViaDeuda().getDesViaDeuda());
					liqConvenioVO.setCanCuotasPlan(String.valueOf(convenio.getCantidadCuotasPlan()));
					liqConvenioVO.setDesEstadoConvenio(convenio.getEstadoConvenio().getDesEstadoConvenio());
					liqConvenioVO.setFechaAltaView(DateUtil.formatDate(convenio.getFechaAlta(), DateUtil.ddSMMSYY_MASK));
					liqConvenioVO.setFechaFor(DateUtil.formatDate(convenio.getFechaFor(), DateUtil.ddSMMSYY_MASK));
					liqConvenioVO.setObservacionFor(convenio.getObservacionFor());
					liqConvenioVO.setTotImporteConvenio(convenio.getTotImporteConvenio());
					liqConvenioVO.setUsusarioFor(convenio.getUsuarioFor());					
					
					// Agrega a la lista de ids
					listAuxIdsCovenio.add(convenio.getId());
					
					// Agrega a la notificacion
					liqNotificacionVO.getListConvenio().add(liqConvenioVO);
				}
			}
			
			if (totCuota > 0D)
				liqNotificacionVO.setTotalCuotaView(StringUtil.redondearDecimales(totCuota, 0, 2));
		}
		
		// Genera las 4 lineas con los periodos de deuda para la impresion en TXT
		liqNotificacionVO.generarLineasDeuda();
		
		// Genera las 4 lineas con los ConvenioCuota para la impresion en TXT
		liqNotificacionVO.generarLineasConvenioCuota();
		
		return liqNotificacionVO;
	}
	
	/**
	 * Obtiene la lista de ProMasDeuInc para el Envio Judicial de manera paginada
	 * @param  firstResult
	 * @param  maxResults
	 * @return List<ProMasDeuInc>
	 */
	public List<ProMasDeuInc> getListProMasDeuInc(Integer firstResult, Integer maxResults, Long idtiposelalmdet) {
		return GdeDAOFactory.getProMasDeuIncDAO().getListByProcesoMasivo(this, firstResult, maxResults, idtiposelalmdet); 
	}
	
	/**
	 * Obtiene la lista de ProMasDeuInc procesada o no segun el parametro procesada, del ProcesoMasivo,
	 * a partir del idProMasDeuInc sin incluirlo
	 * de manera paginada
	 * @param  firstResult
	 * @param  maxResults
	 * @param  procesada
	 * @param  idProMasDeuInc
	 * @return List<ProMasDeuInc>
	 */
	public List<ProMasDeuInc> getListProMasDeuIncProcesada(Integer firstResult, Integer maxResults, Integer procesada, Long idProMasDeuInc){
		return GdeDAOFactory.getProMasDeuIncDAO().getListByProcesoMasivoProcesada(this, firstResult, maxResults, procesada, idProMasDeuInc); 
	}

	
	/**
	 * Obtiene la lista de ProMasDeuExc para el Envio Judicial de manera paginada 
	 * @param  firstResult
	 * @param  maxResults
	 * @return List<ProMasDeuExc>
	 */
	public List<ProMasDeuExc> getListProMasDeuExc(Integer firstResult, Integer maxResults){
		return GdeDAOFactory.getProMasDeuExcDAO().getListByProcesoMasivo(this, firstResult, maxResults); 
	}
	

	/**
	 * Realiza las validacion para el tercer paso del envio.
	 * Ya sea para un envio o un PreEnvio.
	 * En caso de no pasar las validaciones excluye la proMasDeuInc
	 * @return true si paso todas las validaciones.
	 */
	private boolean validateRealizarEnvio(ProMasDeuInc proMasDeuInc) throws Exception {
		log.debug("proMasDeuInc.getId(): " + proMasDeuInc.getId());

		DeudaAdmin  deudaAdmin  = proMasDeuInc.getDeudaAdmin();
		ProMasDeuExc proMasDeuExc = null; 
		MotExc motExc = null;
		
		if(deudaAdmin == null){
			//fedel: los retiramos para que se puedan hacer re-activados del tercer paso cuantas veces quieran.
			//motExc = MotExc.getById(MotExc.ID_POR_NO_DEU_ADMIN);
			//proMasDeuExc = excluirDeuInc(proMasDeuInc, motExc.getId(), null);
			return false;  // continua con la otra deuda
		}
		
		EstadoDeuda estadoDeuda = deudaAdmin.getEstadoDeuda();
		ViaDeuda    viaDeuda    = deudaAdmin.getViaDeuda();

		// 2.2.1.Validar si el estadoDeuda es Administrativa y la VÃ­a de la Deuda es Administrativa.
		if(!(EstadoDeuda.ID_ADMINISTRATIVA == estadoDeuda.getId().longValue() && 
			 ViaDeuda.ID_VIA_ADMIN == viaDeuda.getId().longValue())){
			// 2.2.2.Si no lo es, eliminar la deuda del detalle de deuda incluida (tabla gde_proMasDeuInc) y grabarla en el detalle de deuda excluida (tabla gde_proMasDeuExc):
			// idProcesoMasivo: id del envio
			// idDeuda: id de la deuda
			// idProcurador: el asignado en deuda incluida.
			// idMotExc: SegÃºn la siguiente tabla.

			motExc = MotExc.getByEstadoDeudaViaDeuda(estadoDeuda, viaDeuda);
			proMasDeuExc = excluirDeuInc(proMasDeuInc, motExc.getId(), null); 
			proMasDeuExc.addErrorMessages(this);
			return false;  // continua con la otra deuda
		}

		//2.2.3.Si lo es, validar si la deuda se encuentra Reclamada. 
		// Esta validaciÃ³n se debe realizar verificando si la deuda se encuentra âmarcadaâ como reclamada, 
		// esto es, buscÃ¡ndola en la tabla gde_deudaReclamada (a definir).
		if(deudaAdmin.getEsReclamada() || deudaAdmin.getEstaEnAsentamiento() ){
			//2.2.4.Si lo estÃ¡, eliminar la deuda del detalle de deuda incluida (tabla gde_proMasDeuInc) y 
			// grabarla en el detalle de deuda excluida (tabla gde_proMasDeuExc):
			//	idProcesoMasivo: id del envio
			//	idDeuda: id de la deuda
			//	idProcurador: el asignado en deuda incluida.
			//	idMotExc: Por Deuda Reclamada
			//	observacion: nula para este release
			proMasDeuExc = excluirDeuInc(proMasDeuInc, MotExc.ID_POR_DEU_RECLAMADA, null);
			proMasDeuExc.addErrorMessages(this);
			return false;  // continua con la otra deuda    			
		}

		//2.2.5.Si no lo estÃ¡, validar si la deuda se encuentra Indeterminada. 
		//Esta validaciÃ³n se debe realizar verificando si la deuda se encuentra âmarcadaâ como indeterminada, esto es, buscÃ¡ndola en el sistema actual de âIndeterminadosâ que existe en Balance (a definir).
		if(deudaAdmin.getEsIndeterminada()){ 
			//2.2.6.Si lo estÃ¡, eliminar la deuda del detalle de deuda incluida (tabla gde_proMasDeuInc) y grabarla en el detalle de deuda excluida (tabla gde_proMasDeuExc):
			//idProcesoMasivo: id del envio
			//idDeuda: id de la deuda
			//idProcurador: el asignado en deuda incluida.
			//idMotExc: Por Deuda Indeterminada
			//observacion: (a definir dependiendo de los datos que administre el sistema de indeterminados)
			proMasDeuExc = excluirDeuInc(proMasDeuInc, MotExc.ID_POR_DEU_INDETERMINADA, null);
			proMasDeuExc.addErrorMessages(this);
			return false;  // continua con la otra deuda    			
		}

		//2.2.7.Si no lo estÃ¡, validar si la deuda se encuentra en Convenio vigente (no debe estar en la tabla gde_convenioDeuda, excepto que el convenio se encuentre en estado âRecompuestoâ o âAnuladoâ, en la tabla gde_convenio).
		//Se hace simplemente asi
		Convenio convenio = deudaAdmin.getConvenio(); 
		if(convenio != null){
			// 2.2.8. 1.1.1.Si se encuentra, eliminar la deuda del detalle de deuda incluida (tabla gde_proMasDeuInc) y grabarla en el detalle de deuda excluida (tabla gde_proMasDeuExc):
			//idProcesoMasivo: id del envio
			//idDeuda: id de la deuda
			//idProcurador: el asignado en deuda incluida.
			//idMotExc: Por Deuda Convenio
			//observacion: Nro. de convenio y estado del convenio (armar cadena)
			String obs = convenio.getNroConvenio() + " " + convenio.getEstadoConvenio().getDesEstadoConvenio();
			proMasDeuExc = excluirDeuInc(proMasDeuInc, MotExc.ID_POR_DEU_CONVENIO, obs);
			proMasDeuExc.addErrorMessages(this);
			return false;  // continua con la otra deuda    			
		}
		if(this.hasError()){
			return false;
		}
		return true;
	}

	private boolean validatePreEnvio(ProMasDeuInc proMasDeuInc) throws Exception {
		log.debug("proMasDeuInc.getId(): " + proMasDeuInc.getId());
		
		if(proMasDeuInc.getTipoSelAlmDet().getEsTipoSelAlmDetDeudaAdm()){
			return this.validatePreEnvioParaDeudaAdministrativa(proMasDeuInc);
		} else if(proMasDeuInc.getTipoSelAlmDet().getEsTipoSelAlmDetDeudaJud()){
			return this.validatePreEnvioParaDeudaJudicial(proMasDeuInc);
		} else if(proMasDeuInc.getTipoSelAlmDet().getEsTipoSelAlmDetConvCuot()){
			return this.validatePreEnvioParaConvenioCuota(proMasDeuInc);
		}else{
			log.error("Tipo de Detalle de SelAlmDet no tenido en cuenta");
			return false;
		}
	}
	
	private boolean validatePreEnvioParaDeudaAdministrativa(ProMasDeuInc proMasDeuInc) throws Exception {
		log.debug("proMasDeuInc.getId(): " + proMasDeuInc.getId());
		ProMasDeuExc proMasDeuExc = null; 
		MotExc motExc = null;
		EstadoDeuda estadoDeuda = null;
		ViaDeuda    viaDeuda    = null;
		
		if(proMasDeuInc.getTipoSelAlmDet().getEsTipoSelAlmDetDeudaAdm()){
			DeudaAdmin  deudaAdmin  = proMasDeuInc.getDeudaAdmin();
			if(deudaAdmin == null){
				motExc = MotExc.getById(MotExc.ID_POR_NO_DEU_ADMIN);
				proMasDeuExc = excluirDeuInc(proMasDeuInc, motExc.getId(), null);
				return false;  // continua con la otra deuda
			}
			
			estadoDeuda = deudaAdmin.getEstadoDeuda();
			viaDeuda    = deudaAdmin.getViaDeuda();

			// 2.2.1.Validar si el estadoDeuda es Administrativa y la VÃ­a de la Deuda es Administrativa.
			if(!(EstadoDeuda.ID_ADMINISTRATIVA == estadoDeuda.getId().longValue() && 
				 ViaDeuda.ID_VIA_ADMIN == viaDeuda.getId().longValue())){
				// 2.2.2.Si no lo es, eliminar la deuda del detalle de deuda incluida (tabla gde_proMasDeuInc) y grabarla en el detalle de deuda excluida (tabla gde_proMasDeuExc):
				// idProcesoMasivo: id del envio
				// idDeuda: id de la deuda
				// idProcurador: el asignado en deuda incluida.
				// idMotExc: SegÃºn la siguiente tabla.

				motExc = MotExc.getByEstadoDeudaViaDeuda(estadoDeuda, viaDeuda);
				proMasDeuExc = excluirDeuInc(proMasDeuInc, motExc.getId(), null); 
				proMasDeuExc.addErrorMessages(this);
				return false;  // continua con la otra deuda
			}

			//2.2.3.Si lo es, validar si la deuda se encuentra Reclamada. 
			// Esta validaciÃ³n se debe realizar verificando si la deuda se encuentra âmarcadaâ como reclamada, 
			// esto es, buscÃ¡ndola en la tabla gde_deudaReclamada (a definir).
			if(deudaAdmin.getEsReclamada() || deudaAdmin.getEstaEnAsentamiento() ){
				//2.2.4.Si lo estÃ¡, eliminar la deuda del detalle de deuda incluida (tabla gde_proMasDeuInc) y 
				// grabarla en el detalle de deuda excluida (tabla gde_proMasDeuExc):
				//	idProcesoMasivo: id del envio
				//	idDeuda: id de la deuda
				//	idProcurador: el asignado en deuda incluida.
				//	idMotExc: Por Deuda Reclamada
				//	observacion: nula para este release
				proMasDeuExc = excluirDeuInc(proMasDeuInc, MotExc.ID_POR_DEU_RECLAMADA, null);
				proMasDeuExc.addErrorMessages(this);
				return false;  // continua con la otra deuda    			
			}

			//2.2.5.Si no lo estÃ¡, validar si la deuda se encuentra Indeterminada. 
			//Esta validaciÃ³n se debe realizar verificando si la deuda se encuentra âmarcadaâ como indeterminada, esto es, buscÃ¡ndola en el sistema actual de âIndeterminadosâ que existe en Balance (a definir).
			if(deudaAdmin.getEsIndeterminada()){ 
				//2.2.6.Si lo estÃ¡, eliminar la deuda del detalle de deuda incluida (tabla gde_proMasDeuInc) y grabarla en el detalle de deuda excluida (tabla gde_proMasDeuExc):
				//idProcesoMasivo: id del envio
				//idDeuda: id de la deuda
				//idProcurador: el asignado en deuda incluida.
				//idMotExc: Por Deuda Indeterminada
				//observacion: (a definir dependiendo de los datos que administre el sistema de indeterminados)
				proMasDeuExc = excluirDeuInc(proMasDeuInc, MotExc.ID_POR_DEU_INDETERMINADA, null);
				proMasDeuExc.addErrorMessages(this);
				return false;  // continua con la otra deuda    			
			}

			//2.2.7.Si no lo estÃ¡, validar si la deuda se encuentra en Convenio vigente (no debe estar en la tabla gde_convenioDeuda, excepto que el convenio se encuentre en estado âRecompuestoâ o âAnuladoâ, en la tabla gde_convenio).
			//Se hace simplemente asi
			Convenio convenio = deudaAdmin.getConvenio(); 
			if(convenio != null){
				// 2.2.8. 1.1.1.Si se encuentra, eliminar la deuda del detalle de deuda incluida (tabla gde_proMasDeuInc) y grabarla en el detalle de deuda excluida (tabla gde_proMasDeuExc):
				//idProcesoMasivo: id del envio
				//idDeuda: id de la deuda
				//idProcurador: el asignado en deuda incluida.
				//idMotExc: Por Deuda Convenio
				//observacion: Nro. de convenio y estado del convenio (armar cadena)
				String obs = convenio.getNroConvenio() + " " + convenio.getEstadoConvenio().getDesEstadoConvenio();
				proMasDeuExc = excluirDeuInc(proMasDeuInc, MotExc.ID_POR_DEU_CONVENIO, obs);
				proMasDeuExc.addErrorMessages(this);
				return false;  // continua con la otra deuda    			
			}
		} 		
		if(this.hasError()){
			return false;
		}
		return true;
	}
	
	private boolean validatePreEnvioParaDeudaJudicial(ProMasDeuInc proMasDeuInc) throws Exception {
		log.debug("proMasDeuInc.getId(): " + proMasDeuInc.getId());
		ProMasDeuExc proMasDeuExc = null; 
		MotExc motExc = null;
		EstadoDeuda estadoDeuda = null;
		ViaDeuda    viaDeuda    = null;

		DeudaJudicial  deudaJudicial  = proMasDeuInc.getDeudaJudicial();
		if(deudaJudicial == null){
			motExc = MotExc.getById(MotExc.ID_POR_NO_DEU_JUDICIAL);
			proMasDeuExc = excluirDeuInc(proMasDeuInc, motExc.getId(), null);
			return false;  // continua con la otra deuda
		}

		estadoDeuda = deudaJudicial.getEstadoDeuda();
		viaDeuda    = deudaJudicial.getViaDeuda();

		// 2.2.1.Validar si el estadoDeuda es Judicial y la Via de la Deuda es Judicial.
		if(!(EstadoDeuda.ID_JUDICIAL == estadoDeuda.getId().longValue() && 
				ViaDeuda.ID_VIA_JUDICIAL == viaDeuda.getId().longValue())){

			motExc = MotExc.getById(MotExc.ID_DEUDA_ESTADO_VIA_INVALIDO);
			proMasDeuExc = excluirDeuInc(proMasDeuInc, motExc.getId(), null); 
			proMasDeuExc.addErrorMessages(this);
			return false;  // continua con la otra deuda
		}

		//2.2.3.Si lo es, validar si la deuda se encuentra Reclamada. 
		if(deudaJudicial.getEsReclamada() || deudaJudicial.getEstaEnAsentamiento() ){
			proMasDeuExc = excluirDeuInc(proMasDeuInc, MotExc.ID_POR_DEU_RECLAMADA, null);
			proMasDeuExc.addErrorMessages(this);
			return false;  // continua con la otra deuda    			
		}

		//2.2.5.Si no lo estÃ¡, validar si la deuda se encuentra Indeterminada. 
		//Esta validaciÃ³n se debe realizar verificando si la deuda se encuentra âmarcadaâ como indeterminada, esto es, buscÃ¡ndola en el sistema actual de âIndeterminadosâ que existe en Balance (a definir).
		if(deudaJudicial.getEsIndeterminada()){ 
			proMasDeuExc = excluirDeuInc(proMasDeuInc, MotExc.ID_POR_DEU_INDETERMINADA, null);
			proMasDeuExc.addErrorMessages(this);
			return false;  // continua con la otra deuda    			
		}

		if(this.hasError()){
			return false;
		}
		return true;
	}

	private boolean validatePreEnvioParaConvenioCuota(ProMasDeuInc proMasDeuInc) throws Exception {
		log.debug("proMasDeuInc.getId(): " + proMasDeuInc.getId());
		ProMasDeuExc proMasDeuExc = null; 
		MotExc motExc = null;
		
		ConvenioCuota  convenioCuota  = proMasDeuInc.getConvenioCuota();
			if(convenioCuota == null){
				motExc = MotExc.getById(MotExc.ID_POR_NO_CONV_CUOTA);
				proMasDeuExc = excluirDeuInc(proMasDeuInc, motExc.getId(), null);
				return false;  // continua con el otro convenioCuota
			}
			
			Convenio convenio = convenioCuota.getConvenio();
			// validar que estado sea impago
			
			if(proMasDeuInc.getTipoSelAlmDet().getEsTipoSelAlmDetConvCuotAdm()){
				if(!convenio.getViaDeuda().getEsViaAdmin()){
					motExc = MotExc.getById(MotExc.ID_CONVENIO_VIA_INVALIDO);
					proMasDeuExc = this.instanciarProMasDeuExc(convenioCuota, motExc.getId(), null, proMasDeuInc.getDesTitularPrincipal());
					this.createProMasDeuExc(proMasDeuExc);
					proMasDeuExc.addErrorMessages(this);
				}
			}else if(proMasDeuInc.getTipoSelAlmDet().getEsTipoSelAlmDetConvCuotJud()){
				if(!convenio.getViaDeuda().getEsViaJudicial()){
					motExc = MotExc.getById(MotExc.ID_CONVENIO_VIA_INVALIDO);
					proMasDeuExc = this.instanciarProMasDeuExc(convenioCuota, motExc.getId(), null, proMasDeuInc.getDesTitularPrincipal());
					this.createProMasDeuExc(proMasDeuExc);
					proMasDeuExc.addErrorMessages(this);
				}
			} 

			// validar que estado sea impago
			if(!(EstadoConCuo.ID_IMPAGO == convenioCuota.getEstadoConCuo().getId().longValue())){
				motExc = MotExc.getById(MotExc.ID_CONV_CUOT_NO_IMPAGO);
				proMasDeuExc = this.instanciarProMasDeuExc(convenioCuota, motExc.getId(), null, proMasDeuInc.getDesTitularPrincipal());
				this.createProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
			}

			//1.5.Si lo es, validar si la deuda se encuentra Reclamada. 
			if(convenioCuota.getEsReclamada()){
				proMasDeuExc = this.instanciarProMasDeuExc(convenioCuota, MotExc.ID_POR_CONV_CUOT_RECLAMADA, null, proMasDeuInc.getDesTitularPrincipal());
				this.createProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
			}

			//1.7.Si no lo estÃ¡, validar si la deuda se encuentra Indeterminada. 
			//Esta validaciÃ³n se debe realizar verificando si la deuda se encuentra âmarcadaâ como indeterminada, esto es, buscÃ¡ndola en el sistema actual de âIndeterminadosâ que existe en Balance (a definir).
			if(convenioCuota.getEsIndeterminada()){ 
				proMasDeuExc = this.instanciarProMasDeuExc(convenioCuota, MotExc.ID_POR_CONV_CUOT_INDETERMINADA, null, proMasDeuInc.getDesTitularPrincipal());
				this.createProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
			}

			//1.9.Si no lo estÃ¡, validar si la deuda se encuentra en Convenio vigente (no debe estar en la tabla gde_convenioDeuda, excepto que el convenio se encuentre en estado âRecompuestoâ o âAnuladoâ, en la tabla gde_convenio).
			
			if(EstadoConvenio.ID_VIGENTE != convenio.getEstadoConvenio().getId().longValue()){
				String obs = convenio.getNroConvenio() + " " + convenio.getEstadoConvenio().getDesEstadoConvenio();
				proMasDeuExc = this.instanciarProMasDeuExc(convenioCuota, MotExc.ID_POR_CONV_CUOT_NO_VIGENTE, obs, proMasDeuInc.getDesTitularPrincipal());
				this.createProMasDeuExc(proMasDeuExc);
				proMasDeuExc.addErrorMessages(this);
			}
		
		if(this.hasError()){
			return false;
		}
		return true;
	}

	/**
	 * Realiza la exclusion a partir de una deuda incluida del envio judicial
	 * Borra el registro de la ProMasDeuInc y crea un registro en la ProMasDeuExc
	 * @param  proMasDeuInc
	 * @param  idMotExc
	 * @param  observacion
	 * @return ProMasDeuExc
	 * @throws Exception
	 */
	private ProMasDeuExc excluirDeuInc(ProMasDeuInc proMasDeuInc, Long idMotExc, String observacion) throws Exception{
		ProMasDeuExc deuExc = this.instanciarProMasDeuExc(proMasDeuInc.getDeuda(), proMasDeuInc.getProcurador(), idMotExc, observacion, proMasDeuInc.getDesTitularPrincipal(), proMasDeuInc.getTipoSelAlmDet());		
		proMasDeuInc = this.deleteProMasDeuInc(proMasDeuInc); 
		return this.createProMasDeuExc(deuExc);
	}


	/**
	 * Mueve la deuda administrativa a judicial
	 * Mueve los Conceptos que componen la deuda administrativa a judicial
	 * @throws Exception
	 */
	private void moverDeudaAJudicial(DeudaAdmin deudaAdmin, Procurador procurador) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		AdpRun adpRun = AdpRun.currentRun();
		if (adpRun != null) {
			adpRun.logDebug("Moviendo la siguiente deuda de Admin a Judicial");
			adpRun.logDebug(StringUtil.formatLong(deudaAdmin.getId()));
		}

		GdeDAOFactory.getDeudaJudicialDAO().copiarDeudaAJudicial(deudaAdmin, procurador, this.fechaEnvio); 
		GdeDAOFactory.getDeuJudRecConDAO().copiarAJudicial(deudaAdmin);  
		GdeDAOFactory.getDeuAdmRecConDAO().deleteListDeuAdmRecConByDeudaAdmin(deudaAdmin);
		GdeDAOFactory.getDeudaAdminDAO().delete(deudaAdmin);
		//GdeDAOFactory.getDeudaAdminDAO().deleteListDeudaAdminByProcesoMasivo(this); 

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
	}


	/**
	 * Mueve la deuda judicial y sus conceptos judiciales a deuda administrativa y conceptos administrativos
	 * 
	 * @param  deudaJudicial
	 * @throws Exception
	 */
	public void moverDeudaAAdmin(DeudaJudicial deudaJudicial) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		GdeDAOFactory.getDeudaAdminDAO().copiarDeudaJudicialAAdmin(deudaJudicial);   // TODO ver delegaciones de estos metodos
		GdeDAOFactory.getDeuAdmRecConDAO().copiarAAdministrativa(deudaJudicial);  
		GdeDAOFactory.getDeuJudRecConDAO().deleteListDeuJudRecConByDeudaJudicial(deudaJudicial);
		GdeDAOFactory.getDeudaJudicialDAO().delete(deudaJudicial);

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");

	}

	
	/**
	 * Genera las planilla de la realizacion de Deuda enviada a procuradores
	 * @param  path
	 * @return List<String>
	 * @throws Exception
	 */
	private List<String> exportDeudaEnviadaProcuradores(String path) throws Exception{
		
		Integer firstResult = 0;
		Integer maxResults = 100;   
		long    c = 0;             // contador de registros de cada archivo generado
		int     indiceArchivo = 0; // indice de archivos generados 
		
		List<String> listPlanillaName = new ArrayList<String>();
		
		String idProcesoMasivo = StringUtil.formatLong(this.getId());
		String fileName = path + File.separator + ProcesoMasivo.FILE_NAME_DEUDA_ENV_PROC + "_" + idProcesoMasivo + "_" + indiceArchivo + ".csv";
		
		BufferedWriter buffer = this.crearEncabezadoDeudaEnviadaProcuradores(fileName);
		listPlanillaName.add(fileName);
		// iterar la lista de ConDeuDet de la constancia de deuda que corresponde al envio judicial de manera paginada
		boolean contieneConDeuDet = true;

		while (contieneConDeuDet){

			// obtiene la lista de contieneConDeuDet  
			List <ConDeuDet> listConDeuDet = this.getListConDeuDet(firstResult, maxResults);

			contieneConDeuDet = (listConDeuDet.size() > 0);

			if(contieneConDeuDet){
				
				for (ConDeuDet conDeuDet : listConDeuDet) {

					log.debug("proMasDeuInc.getId(): " + conDeuDet.getId());
					// recurso
						buffer.write(conDeuDet.getDeudaJudicial().getRecurso().getDesRecurso());
						// cuenta
						buffer.write(", " + conDeuDet.getDeudaJudicial().getCuenta().getNumeroCuenta() );
						// anio
						buffer.write(", " + conDeuDet.getDeudaJudicial().getAnio());
						// periodo
						buffer.write(", " + conDeuDet.getDeudaJudicial().getPeriodo());
						// clasif deuda
						buffer.write(", " + conDeuDet.getDeudaJudicial().getRecClaDeu().getDesClaDeu() );
						// fecha de vencimiento
						buffer.write(", " + DateUtil.formatDate(conDeuDet.getDeudaJudicial().getFechaVencimiento(), DateUtil.dd_MM_YYYY_MASK) );
						// importe
						buffer.write(", " + conDeuDet.getDeudaJudicial().getImporte() );
						c++; // incremento el contador de registros
						if(c == 30000 ){ // incluyendo a las filas del encabezado y considera que c arranca en cero
							buffer.close();
							indiceArchivo++;
							fileName = path + File.separator + ProcesoMasivo.FILE_NAME_DEUDA_ENV_PROC + "_" + idProcesoMasivo + "_" + indiceArchivo + ".csv";
							buffer = this.crearEncabezadoDeudaEnviadaProcuradores(fileName);
							listPlanillaName.add(fileName);
							c = 0;
						}else{
							// crea una nueva linea
							buffer.newLine();
						}
					}
				firstResult += maxResults; // incremento el indice del 1er registro
			}
		}

		if(indiceArchivo == 0 && c == 0){
			// --> Resultado vacio
			buffer.write("No se encontraron registros de Deudas Enviada a procuradores "  );
			// <-- Fin Resultado vacio
		}
		
		buffer.close();
		
		return listPlanillaName;
	}

	/**
	 * Obtiene la lista de ConDeuDet del ProcesoMasivo de manera paginada.
	 * @param firstResult
	 * @param maxResults
	 * @return List<ConDeuDet>
	 */
	public List<ConDeuDet> getListConDeuDet(Integer firstResult, Integer maxResults){
		return GdeDAOFactory.getConDeuDetDAO().getListConDeuDetByProMas(this, firstResult, maxResults);
	}

///// Creacion de Archivos de la Realizacion del Envio a Procuradores
	
	/**
	 * Obtiene el buffer utilizado para crea la deuda enviada a procuradores, con su encabezado. 
	 * @param fileName
	 * @return BufferedWriter
	 * @throws Exception
	 */
	private BufferedWriter crearEncabezadoDeudaEnviadaProcuradores(String fileName) throws Exception{
		//genero la planilla de texto usando el buffer
		//PlanillaVO planillaVO = new PlanillaVO(fileName);
		
		BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName, false));

		// --> Creacion del Encabezado del Resultado
		// recurso
		buffer.write("Recurso");
		// cuenta
		buffer.write(", Cuenta");
		// anio
		buffer.write(", A�o");
		// periodo
		buffer.write(", Periodo");
		// clasif deuda
		buffer.write(", Clasif. Deuda");
		// fecha de vencimiento
		buffer.write(", Fecha Vto.");
		// importe
		buffer.write(", Importe");
		// <-- Fin Creacion del Encabezado del Resultado

		buffer.newLine();

		return buffer;
	}
	
	/**
	 * Genera la planilla de deuda excluida.
	 * Retorna la lista de nombres de planillas generadas.
	 * @param  path
	 * @return List<String>
	 * @throws Exception
	 */
	private List<String> exportDeudaExcluidaProcuradores(String path) throws Exception{
		
		Integer firstResult = 0;
		Integer maxResults = 100;   
		long    c = 0;             // contador de registros de cada archivo generado
		int     indiceArchivo = 0; // indice de archivos generados 
		
		List<String> listPlanillaName = new ArrayList<String>();
		
		String idProcesoMasivo = StringUtil.formatLong(this.getId());
		String fileName = path + File.separator + ProcesoMasivo.FILE_NAME_DEUDA_EXC_PROC + "_" + idProcesoMasivo + "_" + indiceArchivo + ".csv";
		
		BufferedWriter buffer = this.crearEncabezadoDeudaExcluidaProcuradores(fileName);
		listPlanillaName.add(fileName);
		
		// iterar la lista de proMasDeuExc que corresponde al envio judicial de manera paginada
		boolean contieneProMasDeuExc = true;

		while (contieneProMasDeuExc){

			// obtiene la lista de ProMasDeuExc ordenada por TODO ver 
			List <ProMasDeuExc> listProMasDeuExc = this.getListProMasDeuExc(firstResult, maxResults);
			
			contieneProMasDeuExc = (listProMasDeuExc.size() > 0); 

			if(contieneProMasDeuExc){
				
				for (ProMasDeuExc proMasDeuExc : listProMasDeuExc) {
					log.debug("proMasDeuExc.getId(): " + proMasDeuExc.getId());
					log.debug("c = " + c);
					Deuda deuda = proMasDeuExc.getDeuda();
					if(deuda == null){
						log.debug("Deuda de ProMasDeuEx no encontrada en las tablas de deuda");
						continue;
					}
					// recurso
					buffer.write(deuda.getRecurso().getDesRecurso());
					// cuenta
					buffer.write(", " + deuda.getCuenta().getNumeroCuenta() );
					// anio
					buffer.write(", " + deuda.getAnio());
					// periodo
					buffer.write(", " + deuda.getPeriodo());
					// clasif deuda
					buffer.write(", " + deuda.getRecClaDeu().getDesClaDeu() );
					// fecha de vencimiento
					buffer.write(", " + DateUtil.formatDate(deuda.getFechaVencimiento(), DateUtil.dd_MM_YYYY_MASK) );
					// importe
					buffer.write(", " + deuda.getImporte() );
					// motivo exclusion
					buffer.write(", " + proMasDeuExc.getMotExc().getDesMotExc() );
					
					c++; // incremento el contador de registros
					if(c == 30000 ){ // incluyendo a las filas del encabezado y considera que c arranca en cero
						buffer.close();
						indiceArchivo++;
						fileName = path + File.separator + ProcesoMasivo.FILE_NAME_DEUDA_EXC_PROC + "_" + idProcesoMasivo + "_" + indiceArchivo + ".csv";
						buffer = this.crearEncabezadoDeudaEnviadaProcuradores(fileName);
						listPlanillaName.add(fileName);
						c = 0;
					}else{
						// crea una nueva linea
						buffer.newLine();
					}
				}
				firstResult += maxResults; // incremento el indice del 1er registro
			}
		}

		if(indiceArchivo == 0 && c == 0){
			// --> Resultado vacio
			buffer.write("No se encontraron registros de Deudas Excluidas a Procuradores "  );
			// <-- Fin Resultado vacio
		}
		
		buffer.close();
		
		return listPlanillaName;
	}

	/**
	 * Obtiene el buffer utilizado para crea la deuda excluida a procuradores, con su encabezado.
	 * @param  fileName
	 * @return BufferedWriter
	 * @throws Exception
	 */
	private BufferedWriter crearEncabezadoDeudaExcluidaProcuradores(String fileName) throws Exception{
		//genero la planilla de texto usando el buffer
		
		BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName, false));

		// --> Creacion del Encabezado del Resultado
		// recurso
		buffer.write("Recurso");
		// cuenta
		buffer.write(", Cuenta");
		// anio
		buffer.write(", Anio");
		// periodo
		buffer.write(", Periodo");
		// clasif deuda
		buffer.write(", Clasif. Deuda");
		// fecha de vencimiento
		buffer.write(", Fecha Vto.");
		// importe
		buffer.write(", Importe");
		// motivo exclusion
		buffer.write(", Motivo Exclusion");
		// <-- Fin Creacion del Encabezado del Resultado

		buffer.newLine();

		return buffer;
	}

	/**
	 * Genera la planilla de deuda enviada a procuradores agrupada por anio y periodo
	 * Retorna la lista de nombres de planillas generadas. 
	 * @param  path
	 * @return List<String>
	 * @throws Exception
	 */
	private List<String> exportDeudaEnviadaPorPeriodo(String path) throws Exception{
		
		List<String> listPlanillaName = new ArrayList<String>();
		
		String idProcesoMasivo = StringUtil.formatLong(this.getId());
		String fileName = path + File.separator + ProcesoMasivo.FILE_NAME_DEUDA_ENV_POR_PERIODO + "_" + idProcesoMasivo + ".csv";
		
		BufferedWriter buffer = this.crearEncabezadoDeudaEnvPorPeriodo(fileName);
		listPlanillaName.add(fileName);
		
		List<Object[]> listTotPorAnioPer = this.totalesPorAnioPeriodo();
		
		for (Object[] object : listTotPorAnioPer) {
			// anio
			buffer.write("" + object[0]); // necesario leerlo de esta manera
			// periodo
			buffer.write(", " + object[1]);
			// ctdReg
			buffer.write(", " + object[2]);
			// importe
			buffer.write(", " + object[3]);
			// ctdCtas
			buffer.write(", " + object[4]);
			buffer.newLine();
		}
		
		if(listTotPorAnioPer.size() == 0){
			// --> Resultado vacio
			buffer.write("No se encontraron registros de Totales de Deudas Enviada a procuradores "  );
			// <-- Fin Resultado vacio
		}
		
		buffer.close();
		
		return listPlanillaName;
	}

	/**
	 * Obtiene el buffer del archivo de la planilla de deuda enviada a procuradores por periodo, con su encabezado.
	 * @param  fileName
	 * @return BufferedWriter
	 * @throws Exception
	 */
	private BufferedWriter crearEncabezadoDeudaEnvPorPeriodo(String fileName) throws Exception{

		BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName, false));
		// procurador, planilla anio/nro, total deuda sumarizada, ctd registros, ctd de cuentas.
		// --> Creacion del Encabezado del Resultado
		// Anio
		buffer.write("Anio");
		// Periodo
		buffer.write(", Periodo");
		// total de registros
		buffer.write(", total de registros");
		// Importe total
		buffer.write(", Importe total");
		// Ctd. Cuentas
		buffer.write(", Ctd Cuentas");
		
		// <-- Fin Creacion del Encabezado del Resultado

		buffer.newLine();

		return buffer;
	}
	
	/**
	 * Obtiene anio, periodo, cantidad de deuda, suma de saldos, cantidad de constancias de deuda, 
	 * agrupada por anio y periodo, 
	 * para el Envio Judicial
	 * @return List<Object[]>
	 */
	private List<Object[]> totalesPorAnioPeriodo(){
		return GdeDAOFactory.getDeudaJudicialDAO().totalesPorAnioPeriodo(this);
	}

	/**
	 * Obtiene la lista de Planillas de Envio de Deuda a Procuradores del Envio Judicial
	 * @return List<PlaEnvDeuPro>
	 */
	public List<PlaEnvDeuPro> getListPlaEnvDeuPro() {
		return GdeDAOFactory.getPlaEnvDeuProDAO().getListByProcesoMasivo(this);
	}

	/**
	 * Obtiene la lista de Constancias de Envio de Deuda a Procuradores del Envio Judicial
	 * @return List<PlaEnvDeuPro>
	 */
	public List<ConstanciaDeu> getListConstanciaDeu() {
		return GdeDAOFactory.getConstanciaDeuDAO().getListByProcesoMasivo(this);
	}
	
	/**
	 *  Retrocede el paso de la corrida del envio judicial:
	 *  Por el momento solo es posible retroceder del paso 2 al paso 1 y 
	 *  del paso 3 si esta en espera comenzar al paso 2:
	 * 	Borra la lista de proMasDeuInc.
	 *  Borra la lista de proMasDeuExc.
	 *  Decrementa el pasoActual de la corrida del envio judicial.
	 *  Crea un logCorrida asociada al nivel sobre el cual se retrocede.
	 *  En caso de error los carga en el procesoMasivo y retorna null.
	 *  
	 *  @return PasoCorrida
	 *  @throws Exception
	 * 
	 */
	public PasoCorrida retrocederPasoCorrida() throws Exception{
	
		// si se encuentra en el paso 2 o en el paso 3 con estado en espera comenzar:
		Integer pasoActual = this.getCorrida().getPasoActual();
		EstadoCorrida estadoCorrida = this.getCorrida().getEstadoCorrida();
		if(pasoActual.equals(PasoCorrida.PASO_DOS)){
		
			// ej.corrida.pasoActual -1.
			// creo un nuevo LogCorrida
			// borrar la lista de proMasDeuInc
			this.deleteListProMasDeuInc();
			
			// borrar la lista de proMasDeuExc
			this.deleteListProMasDeuExc();
			
			this.getCorrida().deleteListFileCorridaByPaso(2);

			PasoCorrida pasoCorrida = this.getCorrida().retrocederPaso();
			if (pasoCorrida == null){
				// no se pudo retroceder, la corrida contiene el mensaje de error
				this.getCorrida().addErrorMessages(this);
			}
			return pasoCorrida;
			
		}else if(pasoActual.equals(PasoCorrida.PASO_TRES) && 
					(estadoCorrida.getId().equals(EstadoCorrida.ID_EN_ESPERA_CONTINUAR) ||
					estadoCorrida.getId().equals(EstadoCorrida.ID_PROCESADO_CON_ERROR) ||
					estadoCorrida.getId().equals(EstadoCorrida.ID_ABORTADO_POR_EXCEPCION))){
			
				// borrar la lista de proMasDeuInc
				this.deleteListProMasDeuInc();
				
				// borrar la lista de proMasDeuExc
				this.deleteListProMasDeuExc();
				
				this.getCorrida().deleteListFileCorridaByPaso(3);
				
				PasoCorrida pasoCorrida = this.getCorrida().retrocederPaso();
				if (pasoCorrida == null){
					// no se pudo retroceder, la corrida contiene el mensaje de error
					this.getCorrida().addErrorMessages(this);
				}
				return pasoCorrida;
		} else if(pasoActual.equals(PasoCorrida.PASO_CUATRO)){
			this.getCorrida().deleteListFileCorridaByPaso(PasoCorrida.PASO_TRES);
			
			PasoCorrida pasoCorrida = this.getCorrida().retrocederPaso();
			if (pasoCorrida == null){
				// no se pudo retroceder, la corrida contiene el mensaje de error
				this.getCorrida().addErrorMessages(this);
			}
			this.getCorrida().setEstadoCorrida(EstadoCorrida.getById(EstadoCorrida.ID_EN_ESPERA_CONTINUAR));
			
			return pasoCorrida;
		}else{
			log.error("por el momento solo es posible retroceder del paso 2 al paso 1 y del paso3 en espera continuar al paso 2");
			return null;
		}
	}

	/**
	 * Reinicia el paso uno de la corrida
	 * @throws Exception
	 */
	public void reiniciarPasoCorrida() throws Exception{
		
		// borrar la lista de SelAlmDet de la seleccion almacenada incluida del envio judicial
		this.getSelAlmInc().deleteListSelAlmDet();
		this.getSelAlmInc().deleteListSelAlmLog();
		
		// borrar la lista de SelAlmDet de la seleccion almacenada excluida del envio judicial
		this.getSelAlmExc().deleteListSelAlmDet();
		this.getSelAlmExc().deleteListSelAlmLog();
		
		// pasa a en preparacion el estado de la corrida.
		AdpRun.getRun(this.getCorrida().getId()).reset();

		
	}

	@Override
	public String infoString() {
		
		String ret= getTipProMas().getPrefijoInfoString();
		
		if(fechaEnvio!=null){
			ret+=" - fecha Envio: "+DateUtil.formatDate(fechaEnvio, DateUtil.ddSMMSYYYY_MASK);
		}

		if(recurso!=null){
			ret+=" - Recurso: "+recurso.getDesRecurso();
		}
		
		if(idCaso!=null){
			ret+=" - Caso: "+idCaso;
		}

		if(usuarioAlta!=null){
			ret+=" - Usuario Alta: "+usuarioAlta;
		}
		
		if(observacion!=null){
			ret+=" - Observacion: "+observacion;
		}

		if(procurador!=null){
			ret+=" - Procurador: "+procurador.getDescripcion();
		}
		
		return ret;
	}
	
	
	/**
	 * Borra los detalles de la Seleccion almacenada incluida y excluida del proceso masivo
	 * @throws Exception
	 */
	public void deleteListSelAlmDet()throws Exception{
		// Borrado de los SelAlmDet de la SelAlmInc y de la SelAlmExc. Creacion de los SelAlmLog
			// es necesario re-abrir la transaccion

			SiatHibernateUtil.currentSession().beginTransaction(); 
			if(log.isDebugEnabled()) log.debug("Borrado de los SelAlmDet de la SelAlmInc");
			this.getSelAlmInc().deleteListSelAlmDet();
			
			SelAlmLog selAlmLog = new SelAlmLog();
			selAlmLog.setAccionLog(AccionLog.getById(AccionLog.ID_LIMPIAR_SELECCION));
			selAlmLog.setDetalleLog("Limpiamos el detalle de la seleccion almacenada incluida");
			selAlmLog = this.getSelAlmInc().createSelAlmLog(selAlmLog);
			if (selAlmLog.hasError()){
				log.error("error al crea la selAlmLog ");
			}
			
			if(log.isDebugEnabled()) log.debug("Borrado de los SelAlmDet de la SelAlmExc");
			this.getSelAlmExc().deleteListSelAlmDet();
			
			selAlmLog = new SelAlmLog();
			selAlmLog.setAccionLog(AccionLog.getById(AccionLog.ID_LIMPIAR_SELECCION));
			selAlmLog.setDetalleLog("Limpiamos el detalle de la seleccion almacenada excluida");
			selAlmLog = this.getSelAlmExc().createSelAlmLog(selAlmLog);
			if (selAlmLog.hasError()){
				log.error("error al crea la selAlmLog");
			}
	}

	public void setCueExeCache(CueExeCache cueExeCache) {
		this.cueExeCache = cueExeCache;
	}

	public CueExeCache getCueExeCache() {
		return cueExeCache;
	}

	/**
	 * Inicializa los caches para procesos largos.
	 * Antes de realizar procesos largos llamar a este metodo
	 */
	public void initializeCaches() throws Exception {
		//Inicialimos cache de exenciones y se lo pasamos al proceso masivo.
		CueExeCache ceCache = new CueExeCache();
		ceCache.initialize(this.getRecurso());
		this.setCueExeCache(ceCache);
		
		//Iniciamos Mapa con broches de este recurso que no permiten Envio Judicial
		List<Broche> listBroche = Broche.getListActivosByIdTipoBrocheYIdRecurso(null, this.getRecurso().getId());
		for(Broche broche : listBroche) {
			if (broche.getExentoEnvioJud() != null && broche.getExentoEnvioJud().intValue() == SiNo.SI.getId().intValue()) {
				this.mapBrocheExentoEnvioJud.put(broche.getId(), broche.getDesBroche());
			}
		}
		
		//cache de areas
		this.exentaAreaCache = new ExentaAreaCache();
		this.exentaAreaCache.initializeCache();
		
		// caches de deudas en seleccion exlcuida
		GdeDAOFactory.getSelAlmDetDAO().loadAllIdElementSelAlmDet(this.mapDeudasSelAlmExc, this.getSelAlmExc().getId());
	}
	
	public void generarCsvDeudaEnvioProcurador(Procurador procurador, String path, String filename) throws Exception {	
		Double sumaImporte = 0D;
		long skip, first;
		SplitedFileWriter sfw = new SplitedFileWriter(path + "/" + filename, 30000L);
		String desProcurador = procurador.getId() + "-" + procurador.getDescripcion();
		//sfw.writeln("Tributo," + this.getRecurso().getCodRecurso() + "-" + this.getRecurso().getDesRecurso());
		//sfw.writeln("FechaEnvio," + DateUtil.formatDate(this.getFechaEnvio(), DateUtil.dd_MM_YYYY_MASK));
		//sfw.writeln("Procurador," + desProcurador);
		//sfw.writeln("");

		boolean wrtFirsLine = false;
		boolean hasObjImp = true;
		boolean deudaSigueTitular = false;
		
		first = 500;
		skip = 0;		
		while (true) {
			List<DeudaJudicial> listDeudaJud = GdeDAOFactory.getProMasDeuIncDAO().getListDeudaJudicial(this, procurador, skip, first);
			if (listDeudaJud.size() == 0) {
				break;
			}
			
			if(!wrtFirsLine){
				if (listDeudaJud.get(0).getCuenta().getObjImp() == null)
					hasObjImp = false;
				
				deudaSigueTitular = Integer.valueOf(1).equals(this.getRecurso().getEsDeudaTitular());
				sfw.writeln("Procurador,Per�odo,A�o,Importe,Nro. Cuenta, Recurso, " + ( hasObjImp?"Clave,":"") +  "Titular Principal");
				wrtFirsLine = true;
			}
			
			String clave = "";
			
			for (DeudaJudicial dj : listDeudaJud) {
				StringBuilder sb = new StringBuilder();
				Cuenta cuenta = dj.getCuenta();
				String nroCuenta = cuenta.getNumeroCuenta();
				String desTitular = "";
				
				if(hasObjImp)
					clave = dj.getCuenta().getObjImp().getClaveFuncional();
								
				if (deudaSigueTitular) {
					List<CuentaTitular> listTit = cuenta.getListCuentaTitularVigentesCerrado(dj.getFechaVencimiento());
					CuentaTitular tit = null;
					if (listTit.size() > 0) {
						tit = listTit.get(0);
						Long idContr = tit.getIdContribuyente();
						Persona persona = Persona.getByIdLight(idContr);
						if (persona != null) {
							desTitular = persona.getRepresent();
						} else {
							desTitular = "Sin Datos: idPersona = " + idContr;
						}
					} else {
						desTitular = "Sin Datos: sin titular vigente a fecha vencimiento deuda:" + dj.getFechaVencimiento();
					}					
				} else {
					desTitular = cuenta.getNombreTitularPrincipal();					
				}
				
				String recurso = this.getRecurso().getDesRecurso();

				//procurador
				sb.append(StringUtil.formatLong(procurador.getId(), 3)).append(",");
				//periodos
				sb.append(StringUtil.formatLong(dj.getPeriodo(), 2)).append(",");
				//A�os
				sb.append(StringUtil.formatLong(dj.getAnio())).append(",");
				//Importe
				Double importe = dj.getImporte();
				if(importe != null) sumaImporte += importe;
				sb.append(StringUtil.formatDouble(importe)).append(",");
				// cuenta
				sb.append(nroCuenta).append(",");
				// recurso
				sb.append(recurso).append(",");				
				// Clave
				if (hasObjImp)
					sb.append(clave).append(",");
				
				// titular principal
				sb.append(desTitular);
				
				sfw.writeln(sb.toString());
			}
			SiatHibernateUtil.currentSession().getTransaction().commit();
			SiatHibernateUtil.closeSession();
			SiatHibernateUtil.currentSession().beginTransaction();
			SiatHibernateUtil.currentSession().refresh(this);
			skip += first;
		}
		sfw.writeln(",,," + sumaImporte);
		sfw.close();
		
		int c = 0;
		for(File file: sfw.getListSplitedFiles()) {	
			c++;
			String nombre = file.getName();
			String descripcion = "Envio de Deuda Judicial: " + desProcurador + ". Parte " + c +" de " + sfw.getListSplitedFiles().size();;
			this.getCorrida().addOutputFile(nombre, descripcion, file.getAbsolutePath());
		}
	}

	public void setEnviadoContr(Long enviadoContr) {
		this.enviadoContr = enviadoContr;
	}

	public Long getEnviadoContr() {
		return enviadoContr;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setGeneraConstancia(Integer generaConstancia) {
		this.generaConstancia = generaConstancia;
	}

	public Integer getGeneraConstancia() {
		return generaConstancia;
	}
	
	public void generarTotalesEnvio() throws Exception {
		{	// totales del envio
			String fileDir = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);
			PrintModel pm = GdeDAOFactory.getProcesoMasivoDAO().getTotalesEnvioReport(this);
			byte[] bytes = pm.getByteArray();
			
			String fileName = "TotalesEnvio_" + this.getId() + ".pdf";
			File pdfFile = new File(fileDir + "/" + fileName);
			FileOutputStream pdfStream = new FileOutputStream(pdfFile);
			pdfStream.write(bytes);
			pdfStream.close();
			this.getCorrida().addOutputFile(pdfFile.getName(), "Totales", pdfFile.getAbsolutePath());
		}
		
		{	// totales del envio
			String fileDir = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);
			PrintModel pm = GdeDAOFactory.getProcesoMasivoDAO().getTotalesEnvioProcurReport(this);
			byte[] bytes = pm.getByteArray();
			
			String fileName = "TotalesEnvioProcurador_" + this.getId() + ".pdf";
			File pdfFile = new File(fileDir + "/" + fileName);
			FileOutputStream pdfStream = new FileOutputStream(pdfFile);
			pdfStream.write(bytes);
			pdfStream.close();
			this.getCorrida().addOutputFile(pdfFile.getName(), "Totales por Procurador", pdfFile.getAbsolutePath());
		}
	}
	
	public void generarTotalesNotifOReciboPorBrocheEnvio(Map<String,Integer> mapForTotalReport) throws Exception {
			String fileDir = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);
			PrintModel printModel = Formulario.getPrintModelForPDF(Formulario.COD_FRM_PRO_MAS_TOT_NOTIF);
	
			// Datos del Encabezado Generico
			if (this.getTipProMas().getId().equals(TipProMas.ID_PRE_ENVIO_JUDICIAL)){
				printModel.putCabecera("TituloReporte", "Notificaci\u00F3n Masiva: Totales");
			}else if (this.getTipProMas().getId().equals(TipProMas.ID_RECONFECCION)) {
				printModel.putCabecera("TituloReporte", "Reconfecci\u00F3n Masiva: Totales");
			}
			printModel.putCabecera("Fecha", DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
			printModel.putCabecera("Hora" ,DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
			printModel.putCabecera("Usuario", this.getUsuarioUltMdf());
			
			// Datos del Encabezado del PDF
			printModel.putCabecera("Recurso", this.getRecurso().getDesRecurso());
			
			// Se arma un contenedor de tablas para guardas los datos.
			ContenedorVO contenedor = new ContenedorVO("Contenedor");
			
			TablaVO tabla = new TablaVO("RegistrosTotales");
			tabla.setTitulo("Registros por Broche");
			
			FilaVO filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Nro. Broche","broche"));
			if (this.getTipProMas().getId().equals(TipProMas.ID_PRE_ENVIO_JUDICIAL)){
				filaCabecera.add(new CeldaVO("Notificaciones","recibos"));
			}else if (this.getTipProMas().getId().equals(TipProMas.ID_RECONFECCION)) {
				filaCabecera.add(new CeldaVO("Recibos","recibos"));				
			}
			tabla.setFilaCabecera(filaCabecera);

			Integer totalRegistros = 0;
			for(String broche: sortKeys(mapForTotalReport.keySet())) {
				FilaVO fila = new FilaVO();
				if(broche.length() < 4)
					broche = StringUtil.completarCerosIzq(broche, 4);
				fila.add(new CeldaVO(broche ,"broche"));
				Integer registros = mapForTotalReport.get(broche);
				fila.add(new CeldaVO(StringUtil.formatInteger(registros) ,"recibos"));
				totalRegistros += registros;
				tabla.add(fila);
			}
			FilaVO filaPie = new FilaVO();
			filaPie.add(new CeldaVO("Total","total"));
			filaPie.add(new CeldaVO(StringUtil.formatInteger(totalRegistros) ,"totalRecibos"));
			tabla.addFilaPie(filaPie);

			contenedor.add(tabla);

			printModel.setData(contenedor);
			printModel.setTopeProfundidad(5);
			
			byte[] bytes = printModel.getByteArray();
			String fileName = "TotalesPorBroche_" + this.getId() + ".pdf";
			File pdfFile = new File(fileDir + "/" + fileName);
			FileOutputStream pdfStream = new FileOutputStream(pdfFile);
			pdfStream.write(bytes);
			pdfStream.close();
			this.getCorrida().addOutputFile(pdfFile.getName(), "Reporte de total de notificaciones/boletas generadas por broche", pdfFile.getAbsolutePath());
	}
	
	private List<String> sortKeys(Set<String> keySet) {
		List<String> listKeys = new ArrayList<String>();
		for (String k: keySet) 
			listKeys.add(k);
		
		Collections.sort(listKeys);
		
		return listKeys;
	}
	
}
