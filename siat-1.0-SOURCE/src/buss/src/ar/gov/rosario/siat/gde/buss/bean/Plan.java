//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.bal.buss.bean.DisParPla;
import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.bal.iface.model.SistemaVO;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cyq.buss.bean.DeudaPrivilegio;
import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import ar.gov.rosario.siat.def.buss.bean.Feriado;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.Vencimiento;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;
import ar.gov.rosario.siat.frm.iface.util.FrmError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.CantCuoProcuradorVO;
import ar.gov.rosario.siat.gde.iface.model.CantCuoVO;
import ar.gov.rosario.siat.gde.iface.model.ConvenioCuotaVO;
import ar.gov.rosario.siat.gde.iface.model.ImporteRecaudadoReport;
import ar.gov.rosario.siat.gde.iface.model.ImporteRecaudarReport;
import ar.gov.rosario.siat.gde.iface.model.InformeRecaudacionConvenios;
import ar.gov.rosario.siat.gde.iface.model.PlanRecPeriodoVO;
import ar.gov.rosario.siat.gde.iface.model.PlanVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorPlanVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorPlanVOContainer;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.model.TipoDeudaPlanVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.Vigencia;


/**
 * Bean correspondiente a Plan
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_plan")
public class Plan extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	private Log log = LogFactory.getLog(Plan.class);
	
	public static final Long ID_PLAN_VIA_ADMIN_2005 = 1L;
	public static final Long ID_PLAN_VIA_JUDICIAL_2005 = 2L;
	public static final Long ID_MORATORIA_TGI_2001_ADMIN = 3L;
	public static final Long ID_MORATORIA_TGI_2001_JUDICIAL = 4L;
	public static final Long ID_MORATORIA_TGI_2000_ADMIN = 5L;
	public static final Long ID_MORATORIA_TGI_2000_JUDICIAL = 6L;
	public static final Long ID_PLAN_JUDICIAL_2004 = 7L;
	public static final Long ID_PLAN_VIA_JUDICIAL_2007 = 8L;
	public static final Long ID_PLAN_CASOS_SOCIALES_1999 = 9L;
	public static final Long ID_MORATORIA_CDM_2001 = 12L;
	public static final Long ID_MORATORIA_CDM_2000 = 13L;
	public static final Long ID_PLAN_FACILIDADES_CDM = 14L;

	
	public static final String DEFAULT_SEQUENCE_NAME = "gde_conven_nro_sq";
	
	@Column(name = "desPlan")
	private String desPlan;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idViaDeuda") 
	private ViaDeuda viaDeuda;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idSistema") 
	private Sistema sistema;
	
	@Column(name = "fecVenDeuDes")
	private Date fecVenDeuDes;
	
	@Column(name = "fecVenDeuHas")
	private Date fecVenDeuHas;
		
	@Column(name = "aplicaTotalImpago")
	private Integer aplicaTotalImpago;
	
	@Column(name = "aplicaPagCue")
	private Integer aplicaPagCue;

	
	@Column(name = "canMaxCuo")
	private Integer canMaxCuo;
	
	@Column(name = "canMinPer")
	private Integer canMinPer;
	
	@Column(name = "canCuoAImpEnForm")
	private Integer canCuoAImpEnForm;
	
	@Column(name = "canMinCuoParCuoSal")
	private Integer canMinCuoParCuoSal;
	
	@Column(name = "cuoDesParaRec")
	private Integer cuoDesParaRec;
	
	@Column(name = "poseeActEsp")
	private Integer poseeActEsp;
	
	@Column(name = "esManual")
	private Integer esManual;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idTipoDeudaPlan") 
	private TipoDeudaPlan tipoDeudaPlan;
	
    @Column(name="idCaso") 
	private String idCaso;
	
	@Column(name = "leyendaPlan")
	private String leyendaPlan;
	
	@Column(name = "linkNormativa")
	private String linkNormativa;
	
	@Column(name = "fechaAlta")
	private Date fechaAlta;
	
	@Column(name = "fechaBaja")
	private Date fechaBaja;
				
	@Column(name = "nameSequence")
	private String nameSequence;
	
	@Column(name="leyendaForm")
	private String leyendaForm;
	
	@Column(name="ordenanza")
	private String ordenanza;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idFormulario")	
	private Formulario formulario; 
	
	@OneToMany()
	@JoinColumn(name="idPlan")
	private List<PlanClaDeu> listPlanClaDeu;
	
	@OneToMany()
	@JoinColumn(name="idPlan")
	private List<PlanMotCad> listPlanMotCad;
	
	@OneToMany()
	@JoinColumn(name="idPlan")
	private List<PlanForActDeu> listPlanForActDeu;
	
	@OneToMany()
	@JoinColumn(name="idPlan")
	@OrderBy(clause="cantidadCuotasPlan")
	private List<PlanDescuento> listPlanDescuento;
	
	@OneToMany()
	@JoinColumn(name="idPlan")
	@OrderBy(clause="cuotaHasta")
	private List<PlanIntFin> listPlanIntFin;
	
	@OneToMany()
	@JoinColumn(name="idPlan")
	@OrderBy(clause="cuotaHasta")
	private List<PlanVen> listPlanVen;
	
	@OneToMany()
	@JoinColumn(name="idPlan")
	private List<PlanAtrVal> listPlanAtrVal;
	
	@OneToMany()
	@JoinColumn(name="idPlan")
	private List<PlanExe> listPlanExe;
	
	@OneToMany()
	@JoinColumn(name="idPlan")
	private List<PlanProrroga> listPlanProrroga;
	
	@OneToMany()
	@JoinColumn(name="idPlan")
	@OrderBy(clause="cantidadCuotas")
	private List<PlanImpMin> listPlanImpMin;
	
	
	@Transient
	private DatosPlanEspecial datosPlanEspecial;
	

	//<#Propiedades>
	
	// Constructores
	public Plan(){
		super();
	}
	
	public Plan(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public DatosPlanEspecial getDatosPlanEspecial() {
		return datosPlanEspecial;
	}

	public void setDatosPlanEspecial(DatosPlanEspecial datosPlanEspecial) {
		this.datosPlanEspecial = datosPlanEspecial;
	}
	
	public static Plan getById(Long id) {
		return (Plan) GdeDAOFactory.getPlanDAO().getById(id);
	}
	
	public static Plan getByIdNull(Long id) {
		return (Plan) GdeDAOFactory.getPlanDAO().getByIdNull(id);
	}
	
	public static List<Plan> getList() {
		return (List<Plan>) GdeDAOFactory.getPlanDAO().getList();
	}
	
	public static List<Plan> getListActivos() {			
		return (List<Plan>) GdeDAOFactory.getPlanDAO().getListActiva();
	}
	
	public static List<Plan> getListVigentesyActivos(Long idRecurso, Long idVia, Date fecha) throws Exception {			
		return (List<Plan>) GdeDAOFactory.getPlanDAO().getListVigentesyActivos(idRecurso, idVia, fecha);
	}
	public static List<Plan> getListVigentesyActivosManuales(Long idRecurso, Long idVia, Date fecha) throws Exception {			
		return (List<Plan>) GdeDAOFactory.getPlanDAO().getListVigentesyActivosManuales(idRecurso, idVia, fecha);
	}
	
	
	public static List<Plan> getListVigentesyActivos(Long idVia, Date fecha) throws Exception {			
		return (List<Plan>) GdeDAOFactory.getPlanDAO().getListVigentesyActivos(idVia, fecha);
	}
	public static List<Plan> getListVigentesyActivosManuales(Long idVia, Date fecha) throws Exception {			
		return (List<Plan>) GdeDAOFactory.getPlanDAO().getListVigentesyActivosManuales(idVia, fecha);
	}
	
	/**
	 * Obtiene todos los planes asociados al recurso
	 * @param  idRecurso
	 * @return List<Plan>
	 * @throws Exception
	 */
	public static List<Plan> getListByIdRecurso(Long idRecurso) throws Exception {			
		return GdeDAOFactory.getPlanDAO().getListByIdRecurso(idRecurso);
	}
	/**
	 * Devuelve la lista de "Exenciones" vigentes que tenga el plan a una determinada fecha.
	 * 
	 * @author Cristian
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
	public List<Exencion> getListExencionesVigentes(Date fecha) throws Exception {
		
		List<PlanExe> listPlanExe = (List<PlanExe>) GdeDAOFactory.getPlanExeDAO().getListPlanExeVigentes(this, fecha);
		
		List<Exencion> listExencion = new ArrayList<Exencion>();
		
		for (PlanExe planExe:listPlanExe){
			Exencion exe = planExe.getExencion();
			listExencion.add(exe);
		}
		
		return 	listExencion;	
	} 
	
	/**
	 * 
	 * Devuelve la lista de "clasificaciones de deuda" vigenes para el plan y la fecha recibida 
	 * 
	 * @author Cristian
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
	public List<RecClaDeu> getListClaDeuVigentes(Date fecha) throws Exception {
		
		List<PlanClaDeu> listPlanClaDeu = (List<PlanClaDeu>) GdeDAOFactory.getPlanClaDeuDAO().getListPlanClaDeuVigentes(this, fecha);
		
		List<RecClaDeu> listRecClaDeu = new ArrayList<RecClaDeu>();
		
		for (PlanClaDeu planClaDeu:listPlanClaDeu){
			RecClaDeu recClaDeu = planClaDeu.getRecClaDeu();
			listRecClaDeu.add(recClaDeu);
		}
		
		return listRecClaDeu;
		
	}
	
	/**
	 * Obtiene la lista vigentes a la fecha pasada de Importes Minimos (de Deuda y Cuota) para las alternativas de cuotas. 
	 * 
	 * @return
	 */
	public List<PlanImpMin> getListPlanImpMinVigentes(Date fecha) {			
		return (List<PlanImpMin>) GdeDAOFactory.getPlanImpMinDAO().getListVigentes(fecha, this);
	}

	
	public List<Recurso> getListRecursos (){
		return GdeDAOFactory.getPlanDAO().getListRecurso(this);
	}
	
	/**
	 * Obtiene la Lista de PlanRecurso del Plan
	 * @return
	 * @throws Exception
	 */
	public List<PlanRecurso> getListPlanRecurso(){
		
		return GdeDAOFactory.getPlanDAO().getListPlanRecurso(this);
		
	}
	
	public String getDesRecursos (){
		String desRecursos ="";
		int i =0;
		for (PlanRecurso planRecurso: getListPlanRecurso()){
			Recurso recurso = planRecurso.getRecurso();
			if (i>0){
				desRecursos += " - ";
			}
			desRecursos += recurso.getDesRecurso();
			i++;
		}
		
		return desRecursos;
	}
	
	// Getters y setters
	public Integer getAplicaTotalImpago() {
		return aplicaTotalImpago;
	}

	public void setAplicaTotalImpago(Integer aplicaTotalImpago) {
		this.aplicaTotalImpago = aplicaTotalImpago;
	}

	public Integer getCanCuoAImpEnForm() {
		return canCuoAImpEnForm;
	}

	public void setCanCuoAImpEnForm(Integer canCuoAImpEnForm) {
		this.canCuoAImpEnForm = canCuoAImpEnForm;
	}

	public Integer getCanMaxCuo() {
		return canMaxCuo;
	}

	public void setCanMaxCuo(Integer canMaxCuo) {
		this.canMaxCuo = canMaxCuo;
	}

	public Integer getCanMinCuoParCuoSal() {
		return canMinCuoParCuoSal;
	}

	public void setCanMinCuoParCuoSal(Integer canMinCuoParCuoSal) {
		this.canMinCuoParCuoSal = canMinCuoParCuoSal;
	}

	public Integer getCanMinPer() {
		return canMinPer;
	}

	public void setCanMinPer(Integer canMinPer) {
		this.canMinPer = canMinPer;
	}

	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public Integer getCuoDesParaRec() {
		return cuoDesParaRec;
	}

	public void setCuoDesParaRec(Integer cuoDesParaRec) {
		this.cuoDesParaRec = cuoDesParaRec;
	}

	public String getDesPlan() {
		return desPlan;
	}

	public void setDesPlan(String desPlan) {
		this.desPlan = desPlan;
	}

	public Integer getEsManual() {
		return esManual;
	}

	public void setEsManual(Integer esManual) {
		this.esManual = esManual;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public Date getFecVenDeuDes() {
		return fecVenDeuDes;
	}

	public void setFecVenDeuDes(Date fecVenDeuDes) {
		this.fecVenDeuDes = fecVenDeuDes;
	}

	public Date getFecVenDeuHas() {
		return fecVenDeuHas;
	}

	public void setFecVenDeuHas(Date fecVenDeuHas) {
		this.fecVenDeuHas = fecVenDeuHas;
	}

	
	public String getLeyendaPlan() {
		return leyendaPlan;
	}

	public void setLeyendaPlan(String leyendaPlan) {
		this.leyendaPlan = leyendaPlan;
	}

	public String getLinkNormativa() {
		return linkNormativa;
	}

	public void setLinkNormativa(String linkNormativa) {
		this.linkNormativa = linkNormativa;
	}

	public Integer getPoseeActEsp() {
		return poseeActEsp;
	}

	public void setPoseeActEsp(Integer poseeActEsp) {
		this.poseeActEsp = poseeActEsp;
	}

	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public TipoDeudaPlan getTipoDeudaPlan() {
		return tipoDeudaPlan;
	}

	public void setTipoDeudaPlan(TipoDeudaPlan tipoDeudaPlan) {
		this.tipoDeudaPlan = tipoDeudaPlan;
	}

	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
	}
		
	public List<PlanAtrVal> getListPlanAtrVal() {
		return listPlanAtrVal;
	}

	public void setListPlanAtrVal(List<PlanAtrVal> listPlanAtrVal) {
		this.listPlanAtrVal = listPlanAtrVal;
	}

	public List<PlanClaDeu> getListPlanClaDeu() {
		return listPlanClaDeu;
	}

	public void setListPlanClaDeu(List<PlanClaDeu> listPlanClaDeu) {
		this.listPlanClaDeu = listPlanClaDeu;
	}

	public List<PlanDescuento> getListPlanDescuento() {
		return listPlanDescuento;
	}
	
	public PlanDescuento getPlanDescuentoTotImpago (Integer nroCuota, Date fechaFor){
		return GdeDAOFactory.getPlanDAO().getPlanDescuentoTotImp(this, nroCuota, fechaFor);
	}

	public void setListPlanDescuento(List<PlanDescuento> listPlanDescuento) {
		this.listPlanDescuento = listPlanDescuento;
	}

	public List<PlanExe> getListPlanExe() {
		return listPlanExe;
	}

	public void setListPlanExe(List<PlanExe> listPlanExe) {
		this.listPlanExe = listPlanExe;
	}

	public List<PlanForActDeu> getListPlanForActDeu() {
		return listPlanForActDeu;
	}

	public void setListPlanForActDeu(List<PlanForActDeu> listPlanForActDeu) {
		this.listPlanForActDeu = listPlanForActDeu;
	}

	public List<PlanIntFin> getListPlanIntFin() {
		return listPlanIntFin;
	}

	public void setListPlanIntFin(List<PlanIntFin> listPlanIntFin) {
		this.listPlanIntFin = listPlanIntFin;
	}

	public List<PlanMotCad> getListPlanMotCad() {
		return listPlanMotCad;
	}

	public void setListPlanMotCad(List<PlanMotCad> listPlanMotCad) {
		this.listPlanMotCad = listPlanMotCad;
	}

	public List<PlanVen> getListPlanVen() {
		return listPlanVen;
	}

	public void setListPlanVen(List<PlanVen> listPlanVen) {
		this.listPlanVen = listPlanVen;
	}
	
	public List<PlanProrroga> getListPlanProrroga() {
		return listPlanProrroga;
	}

	public void setListPlanProrroga(List<PlanProrroga> listPlanProrroga) {
		this.listPlanProrroga = listPlanProrroga;
	}

	public Integer getAplicaPagCue() {
		return aplicaPagCue;
	}

	public void setAplicaPagCue(Integer aplicaPagCue) {
		this.aplicaPagCue = aplicaPagCue;
	}

	public String getNameSequence() {
		return nameSequence;
	}

	public void setNameSequence(String nameSequence) {
		this.nameSequence = nameSequence;
	}
	
	public Formulario getFormulario() {
		return formulario;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}
	
	public List<PlanImpMin> getListPlanImpMin() {
		return listPlanImpMin;
	}

	public void setListPlanImpMin(List<PlanImpMin> listPlanImpMin) {
		this.listPlanImpMin = listPlanImpMin;
	}
	
	public String getLeyendaForm() {
		return leyendaForm;
	}

	public void setLeyendaForm(String leyendaForm) {
		this.leyendaForm = leyendaForm;
	}

	public String getOrdenanza() {
		return ordenanza;
	}

	public void setOrdenanza(String ordenanza) {
		this.ordenanza = ordenanza;
	}

	public List<Long> getListIdConvVigConPagCue(Rescate rescate){
		return GdeDAOFactory.getPlanDAO().getListIdConvVigConPagCueEnRescate(rescate, this);
	}
	
	public PlanMotCad getPlanMotCadVigente (Date fecha){
		return GdeDAOFactory.getPlanMotCadDAO().getVigenteByPlan(this, fecha);
	}
	
	public List<PlanRecurso> getListPlanRecursoVigentes (Date fecha){
		return GdeDAOFactory.getPlanDAO().getListPlanRecursoVigentes(this, fecha);
	}
	
	public Boolean contieneRecursoVigente (Recurso recurso, Date date){
		List<PlanRecurso>listPlanRecVigentes = getListPlanRecursoVigentes(date);
		
		return listPlanRecVigentes.contains(recurso);
	}
	
	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		// Convenio
		if (GenericDAO.hasReference(this, Convenio.class, "plan")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.PLAN_LABEL, GdeError.CONVENIO_LABEL );
		}
		// DisParPla
		if (GenericDAO.hasReference(this, DisParPla.class, "plan")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.PLAN_LABEL, BalError.DISPARPLA_LABEL );
		}
		// PlanClaDeu		
		if (GenericDAO.hasReference(this, PlanClaDeu.class, "plan")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.PLAN_LABEL, GdeError.PLANCLADEU_LABEL );
		}
		// PlanMotCad
		if (GenericDAO.hasReference(this, PlanMotCad.class, "plan")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.PLAN_LABEL, GdeError.PLANMOTCAD_LABEL );
		}
		// PlanForActDeu
		if (GenericDAO.hasReference(this, PlanForActDeu.class, "plan")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.PLAN_LABEL, GdeError.PLANFORACTDEU_LABEL);
		}
		// PlanDescuento
		if (GenericDAO.hasReference(this, PlanDescuento.class, "plan")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.PLAN_LABEL, GdeError.PLANDESCUENTO_LABEL );
		}
		// PlanIntFin
		if (GenericDAO.hasReference(this, PlanIntFin.class, "plan")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.PLAN_LABEL, GdeError.PLANINTFIN_LABEL );
		}
		// PlanVen
		if (GenericDAO.hasReference(this, PlanVen.class, "plan")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.PLAN_LABEL, GdeError.PLANVEN_LABEL );
		}
		// PlanAtrVal
		if (GenericDAO.hasReference(this, PlanAtrVal.class, "plan")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.PLAN_LABEL, GdeError.PLANATRVAL_LABEL );
		}
		// PlanExe
		if (GenericDAO.hasReference(this, PlanExe.class, "plan")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.PLAN_LABEL, GdeError.PLANEXE_LABEL );
		}
		// PlanProrroga
		if (GenericDAO.hasReference(this, PlanProrroga.class, "plan")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.PLAN_LABEL, GdeError.PLANPRORROGA_LABEL);
		}
		
		if (hasError()) {
			return false;
		}
	
		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (StringUtil.isNullOrEmpty(getDesPlan())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_DESPLAN);
		}
		
		if (StringUtil.isNullOrEmpty(ordenanza)){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_ORDENANZA_LABEL);
		}
		
		if (viaDeuda == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.VIADEUDA_LABEL);
		}
		
        if (esManual == null){
        	addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_ESMANUAL);
		}
        
        if (hasError()) {
			return false;
		}
        
        // Validacion de propiedades requeridas para planes no manuales. 
        if (esManual == 0) {
			if (fecVenDeuDes == null){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_FECVENDEUDES);
			}
			
	        if (fecVenDeuHas == null){
	        	addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_FECVENDEUHAS);
			}
	        
	        // valida el rango de fechas ingresado
	        if(fecVenDeuDes!=null && fecVenDeuHas!=null && DateUtil.isDateAfter(fecVenDeuDes, fecVenDeuHas))
	        	addRecoverableError(BaseError.MSG_VALORMAYORQUE, GdeError.PLAN_FECVENDEUDES, GdeError.PLAN_FECVENDEUHAS);
	        	
	        if (aplicaTotalImpago == null){
	        	addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_APLICATOTALIMPAGO);
			}
	
	        if (canMaxCuo == null){
	        	addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_CANMAXCUO);
			}else if(canMaxCuo<0)
				addRecoverableError(BaseError.MSG_RANGO_INVALIDO, GdeError.PLAN_CANMAXCUO);
	        
	        if (canMinPer == null){
	        	addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_CANMINPER);
			}else if(canMinPer<0)
				addRecoverableError(BaseError.MSG_RANGO_INVALIDO, GdeError.PLAN_CANMINPER);
			
	        if (canCuoAImpEnForm == null){
	        	addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_CANCUOAIMPENFORM);
			}else if(canCuoAImpEnForm<0)
				addRecoverableError(BaseError.MSG_RANGO_INVALIDO, GdeError.PLAN_CANCUOAIMPENFORM);
			
	        if(cuoDesParaRec!=null && cuoDesParaRec<0)
	        	addRecoverableError(BaseError.MSG_RANGO_INVALIDO, GdeError.PLAN_CUODESPARAREC);
	        
	        if (canMinCuoParCuoSal == null){
	        	addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_CANMINCUOPARCUOSAL);
			}else if(canMinCuoParCuoSal<0)
				addRecoverableError(BaseError.MSG_RANGO_INVALIDO, GdeError.PLAN_CANMINCUOPARCUOSAL);
			
	        if (poseeActEsp == null){
	        	addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_POSEEACTESP);
			}
			
	        if (tipoDeudaPlan == null){
	        	addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TIPODEUDAPLAN_LABEL);
			}
			
			if (sistema == null){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.SISTEMA_LABEL);
			}
			
			if (aplicaPagCue == null){
	        	addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_APLICAPAGCUE);
	        }
		
        } // Fin No Manual

        if (fechaAlta == null){
        	addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_FECHAALTA);
        }
		
        if (StringUtil.isNullOrEmpty(leyendaPlan)){
        	addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_LEYENDAPLAN);
        }
		
        if (formulario == null){
        	addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, FrmError.FORMULARIO_LABEL);
		}

		if (getEstado()==1 && getEsManual()!=1){
	        Integer cantMaxCuoPlanVen=0;
			for (PlanVen planVen:getListPlanVen()){
				if (planVen.getCuotaHasta()>cantMaxCuoPlanVen ){
					cantMaxCuoPlanVen = planVen.getCuotaHasta();
				}
			}
			if (this.canMaxCuo > cantMaxCuoPlanVen){
				addRecoverableError(GdeError.PLAN_LIST_PLANVEN_CUOMAX);
			}
		}
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("desPlan");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			//addRecoverableError(BaseError.MSG_CAMPO_UNICO, GdeError.PLAN_CODPLAN);			
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el Plan. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getPlanDAO().update(this);
	}

	/**
	 * Desactiva el Plan. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getPlanDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Plan
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		if (this.esManual.intValue() == 1){
			
			if (getListPlanVen() == null || 
					(getListPlanVen() != null && getListPlanVen().size() == 0)){
				addRecoverableError(BaseError.MSG_LISTA_DETALLE_REQUERIDO, GdeError.PLAN_LISTPLANVEN);
			}
			
			if (getListPlanVen() != null &&
					getListPlanVen().size() != 1){
				addRecoverableError(GdeError.PLAN_LISTPLANVEN_MANUAL);
			}
			
			if (getListPlanRecurso()==null || (getListPlanRecurso() != null && getListPlanRecurso().size()==0)){
				addRecoverableError(BaseError.MSG_LISTA_DETALLE_REQUERIDO, GdeError.PLAN_LISTPLANRECURSO);
			}
			
		} else {
		
			if (getListPlanClaDeu() == null || 
					(getListPlanClaDeu() != null && getListPlanClaDeu().size() == 0)){			
				addRecoverableError(BaseError.MSG_LISTA_DETALLE_REQUERIDO, GdeError.PLAN_LISTPLANCLADEU);			
			}
			
			if (getListPlanMotCad() == null || 
					(getListPlanMotCad() != null && getListPlanMotCad().size() == 0)){
				addRecoverableError(BaseError.MSG_LISTA_DETALLE_REQUERIDO, GdeError.PLAN_LISTPLANMOTCAD);
			} 
				
			if (getListPlanDescuento() == null || 
					(getListPlanDescuento() != null && getListPlanDescuento().size() == 0)){
				addRecoverableError(BaseError.MSG_LISTA_DETALLE_REQUERIDO, GdeError.PLAN_LISTPLANDESCUENTO);
			}
			
			if (getListPlanRecurso()==null || (getListPlanRecurso() != null && getListPlanRecurso().size()==0)){
				addRecoverableError(BaseError.MSG_LISTA_DETALLE_REQUERIDO, GdeError.PLAN_LISTPLANRECURSO);
			}
				
			// Se valida que la lista de Interes Financiero no sea vacia y que exista un Interes para la ultima Cuota
			Boolean hayPlanIntFinParaCuoMax=false;
			if (getListPlanIntFin() == null || 
					(getListPlanIntFin() != null && getListPlanIntFin().size() == 0)){
				addRecoverableError(BaseError.MSG_LISTA_DETALLE_REQUERIDO, GdeError.PLAN_LISTPLANINTFIN);
			} else {
				for (PlanIntFin planIntFin:getListPlanIntFin()){
					if (planIntFin.getCuotaHasta() >= getCanMaxCuo()){
						hayPlanIntFinParaCuoMax = true;
					}
				}
			}
			if (!hayPlanIntFinParaCuoMax){
				addRecoverableError(GdeError.PLAN_LIST_PLANINTFIN_CUOMAX);
			}
			
			// Se valida que la lista de Vencimientos no sea vacia y que exista un Vencimiento para la ultima Cuota
			Boolean hayPlanVenParaCuoMax=false;
			if (getListPlanVen() == null || 
					(getListPlanVen() != null && getListPlanVen().size() == 0)){
				addRecoverableError(BaseError.MSG_LISTA_DETALLE_REQUERIDO, GdeError.PLAN_LISTPLANVEN);
			} else {
				for (PlanVen planVen:getListPlanVen()){
					if (planVen.getCuotaHasta() >= getCanMaxCuo()){
						hayPlanVenParaCuoMax = true;
					}
				}
			}
			if (!hayPlanVenParaCuoMax){
				addRecoverableError(GdeError.PLAN_LIST_PLANVEN_CUOMAX);
			}
			
			if (getListPlanImpMin() == null || 
					(getListPlanImpMin() != null && getListPlanImpMin().size() == 0)){
				addRecoverableError(BaseError.MSG_LISTA_DETALLE_REQUERIDO, GdeError.PLAN_LISTPLANIMPMIN);
			}		
		}

		if (hasError()){
			return false;
		}
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Plan
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//	---> ABM PlanClaDeu
	public PlanClaDeu createPlanClaDeu(PlanClaDeu planClaDeu) throws Exception {

		// Validaciones de negocio
		if (!planClaDeu.validateCreate()) {
			return planClaDeu;
		}

		GdeDAOFactory.getPlanClaDeuDAO().update(planClaDeu);

		return planClaDeu;
	}
	
	public PlanClaDeu updatePlanClaDeu(PlanClaDeu planClaDeu) throws Exception {
		
		// Validaciones de negocio
		if (!planClaDeu.validateUpdate()) {
			return planClaDeu;
		}

		GdeDAOFactory.getPlanClaDeuDAO().update(planClaDeu);
		
		return planClaDeu;
	}
	
	public PlanClaDeu deletePlanClaDeu(PlanClaDeu planClaDeu) throws Exception {
	
		// Validaciones de negocio
		if (!planClaDeu.validateDelete()) {
			return planClaDeu;
		}
		
		GdeDAOFactory.getPlanClaDeuDAO().delete(planClaDeu);
		
		return planClaDeu;
	}
	
	// ---> ABM PlanRecurso
	public PlanRecurso createPlanRecurso(PlanRecurso planRecurso)throws Exception{
		
		if (!planRecurso.validateCreate()){
			return planRecurso;
		}
		
		GdeDAOFactory.getPlanRecursoDAO().update(planRecurso);
		
		return planRecurso;
	}
	
	public PlanRecurso updatePlanRecurso(PlanRecurso planRecurso)throws Exception{
		
		if (!planRecurso.validateUpdate()){
			return planRecurso;
		}
		
		GdeDAOFactory.getPlanRecursoDAO().update(planRecurso);
		
		return planRecurso;
	}
	
	public PlanRecurso deletePlanRecurso(PlanRecurso planRecurso)throws Exception{
		
		if (!planRecurso.validateDelete()){
			return planRecurso;
		}
		
		GdeDAOFactory.getPlanRecursoDAO().delete(planRecurso);
		
		return planRecurso;
	}
	
	//	---> ABM PlanMotCad
	public PlanMotCad createPlanMotCad(PlanMotCad planMotCad) throws Exception {

		// Validaciones de negocio
		if (!planMotCad.validateCreate()) {
			return planMotCad;
		}

		GdeDAOFactory.getPlanMotCadDAO().update(planMotCad);

		return planMotCad;
	}
	
	public PlanMotCad updatePlanMotCad(PlanMotCad planMotCad) throws Exception {
		
		// Validaciones de negocio
		if (!planMotCad.validateUpdate()) {
			return planMotCad;
		}

		GdeDAOFactory.getPlanMotCadDAO().update(planMotCad);
		
		return planMotCad;
	}
	
	public PlanMotCad deletePlanMotCad(PlanMotCad planMotCad) throws Exception {
	
		// Validaciones de negocio
		if (!planMotCad.validateDelete()) {
			return planMotCad;
		}
		
		GdeDAOFactory.getPlanMotCadDAO().delete(planMotCad);
		
		return planMotCad;
	}
	
	//	---> ABM PlanForActDeu
	public PlanForActDeu createPlanForActDeu(PlanForActDeu planForActDeu) throws Exception {

		// Validaciones de negocio
		if (!planForActDeu.validateCreate()) {
			return planForActDeu;
		}

		GdeDAOFactory.getPlanForActDeuDAO().update(planForActDeu);

		return planForActDeu;
	}
	
	public PlanForActDeu updatePlanForActDeu(PlanForActDeu planForActDeu) throws Exception {
		
		// Validaciones de negocio
		if (!planForActDeu.validateUpdate()) {
			return planForActDeu;
		}

		GdeDAOFactory.getPlanForActDeuDAO().update(planForActDeu);
		
		return planForActDeu;
	}
	
	public PlanForActDeu deletePlanForActDeu(PlanForActDeu planForActDeu) throws Exception {
	
		// Validaciones de negocio
		if (!planForActDeu.validateDelete()) {
			return planForActDeu;
		}
		
		GdeDAOFactory.getPlanForActDeuDAO().delete(planForActDeu);
		
		return planForActDeu;
	}
	
	//	---> ABM PlanDescuento
	public PlanDescuento createPlanDescuento(PlanDescuento planDescuento) throws Exception {

		// Validaciones de negocio
		if (!planDescuento.validateCreate()) {
			return planDescuento;
		}

		GdeDAOFactory.getPlanDescuentoDAO().update(planDescuento);

		return planDescuento;
	}
	
	public PlanDescuento updatePlanDescuento(PlanDescuento planDescuento) throws Exception {
		
		// Validaciones de negocio
		if (!planDescuento.validateUpdate()) {
			return planDescuento;
		}

		GdeDAOFactory.getPlanDescuentoDAO().update(planDescuento);
		
		return planDescuento;
	}
	
	public PlanDescuento deletePlanDescuento(PlanDescuento planDescuento) throws Exception {
	
		// Validaciones de negocio
		if (!planDescuento.validateDelete()) {
			return planDescuento;
		}
		
		GdeDAOFactory.getPlanDescuentoDAO().delete(planDescuento);
		
		return planDescuento;
	}
	
	
	//	---> ABM PlanIntFin
	public PlanIntFin createPlanIntFin(PlanIntFin planIntFin) throws Exception {

		// Validaciones de negocio
		if (!planIntFin.validateCreate()) {
			return planIntFin;
		}

		GdeDAOFactory.getPlanIntFinDAO().update(planIntFin);

		return planIntFin;
	}
	
	public PlanIntFin updatePlanIntFin(PlanIntFin planIntFin) throws Exception {
		
		// Validaciones de negocio
		if (!planIntFin.validateUpdate()) {
			return planIntFin;
		}

		GdeDAOFactory.getPlanIntFinDAO().update(planIntFin);
		
		return planIntFin;
	}
	
	public PlanIntFin deletePlanIntFin(PlanIntFin planIntFin) throws Exception {
	
		// Validaciones de negocio
		if (!planIntFin.validateDelete()) {
			return planIntFin;
		}
		
		GdeDAOFactory.getPlanIntFinDAO().delete(planIntFin);
		
		return planIntFin;
	}
	
	//	---> ABM PlanVen
	public PlanVen createPlanVen(PlanVen planVen) throws Exception {

		// Validaciones de negocio
		if (!planVen.validateCreate()) {
			return planVen;
		}

		GdeDAOFactory.getPlanVenDAO().update(planVen);

		return planVen;
	}
	
	public PlanVen updatePlanVen(PlanVen planVen) throws Exception {
		
		// Validaciones de negocio

		if (!planVen.validateUpdate()) {
			return planVen;
		}

		
		
		
		GdeDAOFactory.getPlanVenDAO().update(planVen);
		
		return planVen;
	}
	
	public PlanVen deletePlanVen(PlanVen planVen) throws Exception {
	
		// Validaciones de negocio
		if (!planVen.validateDelete()) {
			return planVen;
		}
		
		GdeDAOFactory.getPlanVenDAO().delete(planVen);
		
		return planVen;
	}
	
	//	---> ABM PlanExe
	public PlanExe createPlanExe(PlanExe planExe) throws Exception {

		// Validaciones de negocio
		if (!planExe.validateCreate()) {
			return planExe;
		}

		GdeDAOFactory.getPlanExeDAO().update(planExe);

		return planExe;
	}
	
	public PlanExe updatePlanExe(PlanExe planExe) throws Exception {
		
		// Validaciones de negocio
		if (!planExe.validateUpdate()) {
			return planExe;
		}

		GdeDAOFactory.getPlanExeDAO().update(planExe);
		
		return planExe;
	}
	
	public PlanExe deletePlanExe(PlanExe planExe) throws Exception {
	
		// Validaciones de negocio
		if (!planExe.validateDelete()) {
			return planExe;
		}
		
		GdeDAOFactory.getPlanExeDAO().delete(planExe);
		
		return planExe;
	}
	
	//	---> ABM PlanProrroga
	public PlanProrroga createPlanProrroga(PlanProrroga planProrroga) throws Exception {

		// Validaciones de negocio
		if (!planProrroga.validateCreate()) {
			return planProrroga;
		}

		GdeDAOFactory.getPlanProrrogaDAO().update(planProrroga);

		return planProrroga;
	}
	
	public PlanProrroga updatePlanProrroga(PlanProrroga planProrroga) throws Exception {
		
		// Validaciones de negocio
		if (!planProrroga.validateUpdate()) {
			return planProrroga;
		}

		GdeDAOFactory.getPlanProrrogaDAO().update(planProrroga);
		
		return planProrroga;
	}
	
	public PlanProrroga deletePlanProrroga(PlanProrroga planProrroga) throws Exception {
	
		// Validaciones de negocio
		if (!planProrroga.validateDelete()) {
			return planProrroga;
		}
		
		GdeDAOFactory.getPlanProrrogaDAO().delete(planProrroga);
		
		return planProrroga;
	}
	
	//	---> ABM PlanAtrVal
	public PlanAtrVal createPlanAtrVal(PlanAtrVal planAtrVal) throws Exception {

		// Validaciones de negocio
		if (!planAtrVal.validateCreate()) {
			return planAtrVal;
		}

		GdeDAOFactory.getPlanAtrValDAO().update(planAtrVal);

		return planAtrVal;
	}
	
	public PlanAtrVal updatePlanAtrVal(PlanAtrVal planAtrVal) throws Exception {
		
		// Validaciones de negocio
		if (!planAtrVal.validateUpdate()) {
			return planAtrVal;
		}

		GdeDAOFactory.getPlanAtrValDAO().update(planAtrVal);
		
		return planAtrVal;
	}
	
	public PlanAtrVal deletePlanAtrVal(PlanAtrVal planAtrVal) throws Exception {
	
		// Validaciones de negocio
		if (!planAtrVal.validateDelete()) {
			return planAtrVal;
		}
		
		GdeDAOFactory.getPlanAtrValDAO().delete(planAtrVal);
		
		return planAtrVal;
	}
	// <--- ABM PlanAtrVal
	
	//	---> ABM PlanImpMin
	public PlanImpMin createPlanImpMin(PlanImpMin planImpMin) throws Exception {

		// Validaciones de negocio
		if (!planImpMin.validateCreate()) {
			return planImpMin;
		}

		GdeDAOFactory.getPlanImpMinDAO().update(planImpMin);

		return planImpMin;
	}
	
	public PlanImpMin updatePlanImpMin(PlanImpMin planImpMin) throws Exception {
		
		// Validaciones de negocio
		if (!planImpMin.validateUpdate()) {
			return planImpMin;
		}

		GdeDAOFactory.getPlanImpMinDAO().update(planImpMin);
		
		return planImpMin;
	}
	
	public PlanImpMin deletePlanImpMin(PlanImpMin planImpMin) throws Exception {
	
		// Validaciones de negocio
		if (!planImpMin.validateDelete()) {
			return planImpMin;
		}
		
		GdeDAOFactory.getPlanImpMinDAO().delete(planImpMin);
		
		return planImpMin;
	}
	// <--- ABM PlanImpMin
	
	//<#MetodosBeanDetalle>
	
	/**
	 * toVO liviano para ser utilizado en la busqueda
	 * 
	 * @author Cristian
	 * @return PlanVO
	 * @throws Exception
	 * 
	 */
	public PlanVO toVOForSearch() throws Exception {
		
		PlanVO planVO = (PlanVO) this.toVO(false);
		
		
		if (getViaDeuda() != null)
			planVO.setViaDeuda((ViaDeudaVO)this.getViaDeuda().toVO(false));
		
		return planVO;
	}
	
	/**
	 * toVO utilizado para recuperar toda la info del plan con o sin sus detalles.
	 * 
	 * 
	 * @author Cristian
	 * @return PlanVO
	 * @throws Exception
	 */
	public PlanVO toVOForView() throws Exception {
		
		PlanVO planVO = (PlanVO) this.toVOForSearch();
		
		if (getSistema() != null)
			planVO.setSistema((SistemaVO) this.getSistema().toVO(false));
			
		if (getTipoDeudaPlan() != null)
			planVO.setTipoDeudaPlan((TipoDeudaPlanVO) this.getTipoDeudaPlan().toVO(false));
				
		if (getFormulario() != null)
			planVO.setFormulario((FormularioVO) this.getFormulario().toVO(false));
			
		// Debo inicializar las listas para que no pinche.
		planVO.setListPlanClaDeu(new ArrayList());		// PlanClaDeu
		planVO.setListPlanMotCad(new ArrayList()); 		// PlanMotCad
		planVO.setListPlanForActDeu(new ArrayList());	// PlanForActDeu
		planVO.setListPlanDescuento(new ArrayList());	// PlanDescuento
		planVO.setListPlanIntFin(new ArrayList());		// PlanIntFin
		planVO.setListPlanVen(new ArrayList());			// PlanVen
		planVO.setListPlanAtrVal(new ArrayList());		// PlanAtrVal
		planVO.setListPlanExe(new ArrayList());			// PlanExe			
		planVO.setListPlanImpMin(new ArrayList()); 		// PlanImpMin
		
		return planVO;
	}
	
	/**
	 * Devuelte el Plan Descuento para una cuota Hasta 
	 * y que se encuentre vigente a una fecha dada. 
	 * 
	 * @author Cristian
	 * @param cuotaHasta
	 * @param fecha
	 * @return
	 * @throws Exception 
	 */
	public PlanDescuento getPlanDescuento(Integer nroCuota, Date fecha) throws Exception{
		
		PlanDescuento planDescuento = null;
		
		if (this.esManual.intValue()==1){
			planDescuento= new PlanDescuento();
			planDescuento.setPorDesAct(datosPlanEspecial.getDescActualizacion());
			planDescuento.setPorDesCap(datosPlanEspecial.getDescCapital());
			return planDescuento;
		}
		
		int cuotaDesde = 1;
		int cuotaHasta;
		
		// Se supone la lista ordenada por PlanDescuento.cantidadCuotasPlan 
		for (PlanDescuento planDescItem: getListPlanDescuento()){
			if (planDescItem.getAplTotImp().intValue()!= 1){
				cuotaHasta = planDescItem.getCantidadCuotasPlan().intValue();
				
				log.debug(" ### Plan.getPlanDescuento("+ nroCuota + ", " + 
						  DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + " ) - " +
						  " cuotaDesde: " + cuotaDesde + 
						  " cuotaHasta: " + cuotaHasta);
				
				// Mientras sea cantidadCuotasPlan menor o igual a cuotaHasta. 
				if (nroCuota.intValue() >= cuotaDesde && 
						nroCuota.intValue() <= cuotaHasta){
					// Si se encuentra vigente.
					if (planDescItem.getVigenciaForDate(fecha).intValue() == Vigencia.VIGENTE.getId().intValue())
						planDescuento = planDescItem;					
				}
				
				cuotaDesde = planDescItem.getCantidadCuotasPlan().intValue() + 1;
			}
		}
		
		if (planDescuento != null)
			log.debug(" ### Plan.getPlanDescuento: cap.: " + planDescuento.getPorDesCap() +
				" act.: " + planDescuento.getPorDesAct() + 
				" int.: " + planDescuento.getPorDesInt());
		
		return planDescuento;
	}
	
	/**
	 * Devuelve un valor PlanIntFin configurado en el plan para una cuota hasta y 
	 * que se encuentre vigente a una fecha dada 
	 * 
	 * @author Cristian
	 * @param nroCuota
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
	public PlanIntFin getPlanIntFin(Integer nroCuota, Date fecha) throws Exception{
		
		PlanIntFin planIntFin = null;
		int cuotaDesde = 1;
		int cuotaHasta;
		
		// Se supone la lista ordenada por PlanDescuento.cantidadCuotasPlan 
		for (PlanIntFin planIntFinItem: getListPlanIntFin()){
			
			cuotaHasta = planIntFinItem.getCuotaHasta().intValue();
			
			log.debug(" ### Plan.getInteresFinanciero("+ nroCuota + ", " + 
					  DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + " ) - " +
					  " cuotaDesde: " + cuotaDesde + 
					  " cuotaHasta: " + cuotaHasta);
			
			// Mientras sea cantidadCuotasPlan menor o igual a cuotaHasta. 
			if (nroCuota.intValue() >= cuotaDesde && 
					nroCuota.intValue() <= cuotaHasta){
				// Si se encuentra vigente.
				if (planIntFinItem.getVigenciaForDate(fecha).intValue() == Vigencia.VIGENTE.getId().intValue())
					planIntFin = planIntFinItem;					
			}
			
			cuotaDesde = planIntFinItem.getCuotaHasta().intValue() + 1;			
		}
		
		return planIntFin;	
	}
	
	/**
	 * Devuelve un valor de interes financiero configurado en el plan para una cuota hasta y 
	 * que se encuentre vigente a una fecha dada
	 * 
	 * @author Cristian
	 * @param cuotaHasta
	 * @param fecha
	 * @return
	 * @throws Exception 
	 */
	public Double getInteresFinanciero(Integer nroCuota, Date fecha) throws Exception{
		
		if (this.esManual.intValue()==1){
			return getDatosPlanEspecial().getInteres();
		}else{
			PlanIntFin planIntFin = getPlanIntFin(nroCuota,fecha);
		
			if (planIntFin != null)
				return planIntFin.getInteres();
			else
				return null;
		}
	}
	
	/**
	 * Devuelve un valor PlanVen configurado en el plan para una cuota hasta y 
	 * que se encuentre vigente a una fecha dada 
	 * 
	 * @author Cristian
	 * @param nroCuota
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
	public PlanVen getPlanVen(Integer nroCuota, Date fecha) throws Exception{
		
		PlanVen planVen = null;
		int cuotaDesde = 1;
		int cuotaHasta;
		
		// Se supone la lista ordenada por PlanVen.cuotaHasta
		for (PlanVen planVenItem: getListPlanVen()){
			
			cuotaHasta = planVenItem.getCuotaHasta().intValue();
			
			log.debug(" ### Plan.getPlanVen("+ nroCuota + ", " + 
					  DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + " ) - " +
					  " cuotaDesde: " + cuotaDesde + 
					  " cuotaHasta: " + cuotaHasta);
			
			// Mientras sea cantidadCuotas menor o igual a cuotaHasta. 
			if (nroCuota.intValue() >= cuotaDesde && 
					nroCuota.intValue() <= cuotaHasta){
				// Si se encuentra vigente.
				if (planVenItem.getVigenciaForDate(fecha).intValue() == Vigencia.VIGENTE.getId().intValue())
					planVen = planVenItem;					
			}
			
			if (planVenItem.getVigenciaForDate(fecha).intValue() == Vigencia.VIGENTE.getId().intValue())
					cuotaDesde = planVenItem.getCuotaHasta().intValue() + 1;			
		}
		
		if (planVen != null)
			log.debug(" ### Plan.getPlanVen id: " + planVen.getId() + 
					  " cant. cuo: " + planVen.getCuotaHasta() + 
					  " idVencimiento: " + planVen.getVencimiento().getId());
		
		return planVen;
	}
	
	/**
	 * Devuelve la fecha de vencimiento configurara en el plan para una cuota hasta
	 * y vigente a una fecha dada.
	 * 
	 * - La fecha formalizacion se utiliza siempre para obtener el Vencimiento vigente configurado en el plan y
	 * 	 para obtener el la fecha de vencimiento de la primer cuota.
	 * - La fechaVenAnt se utiliza para obtener las fechas de vencimiento de la cuota 2 en adelante.
	 * 
	 * @author Cristian
	 * @param nroCuota
	 * @param fechaForm
	 * @param fechaVenAnt
	 * @return
	 * @throws Exception 
	 */
	public Date getVencimiento(Integer nroCuota, Date fechaForm, Date fechaVenAnt) throws Exception{
		
		Date fechaVencimientoRet = null;
		
		if (this.esManual.intValue() == 1){
			
			// Si el plan es manual, debe tener cargado el vencimiento para la cuota nro 1
			PlanVen planVen = getPlanVen(1, fechaForm);
			
			if (planVen == null)
				return null;
					
			// Para la primer cuota, la "fecha vencimiento anterior" llega nula.
			if (fechaVenAnt == null)		
				fechaVencimientoRet = datosPlanEspecial.getVenPrimeraCuota();
			else
				fechaVencimientoRet = Vencimiento.getFechaVencimiento(fechaVenAnt, planVen.getVencimiento().getId());
			
			return fechaVencimientoRet;
				
			
		} else {
			
			PlanVen planVen = getPlanVen(nroCuota, fechaForm);
			
			if (planVen == null)
				return null;
			
			// Para la primer cuota, la "fecha vencimiento anterior" llega nula.
			if (fechaVenAnt == null)		
				fechaVencimientoRet = Vencimiento.getFechaVencimiento(fechaForm, planVen.getVencimiento().getId());
			else
				fechaVencimientoRet = Vencimiento.getFechaVencimiento(fechaVenAnt, planVen.getVencimiento().getId());
			
			return fechaVencimientoRet;
			
		}	
		
	}
	
	/**
	 * Devuelve el menor de los importes minimos de del plan
	 * Si no exite ninguno, devuelve 0. 
	 * 
	 * @author Cristian
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
	public Double getMinimoImpMinDeu(Date fecha) throws Exception {
		
		PlanImpMin planImpMin = null;
		
		Double minimoImpMinDeu = null;

		log.debug(" ### Plan.getMinimoImpMinDeu( " + DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + " )");
		
		// Se supone la lista ordenada por PlanImpMin.cantidadCuotas 
		for (PlanImpMin planImpMinItem: getListPlanImpMin()){
			
			// Si se encuentra vigente.
			if (planImpMinItem.getVigenciaForDate(fecha).intValue() == Vigencia.VIGENTE.getId().intValue()) {
				if (minimoImpMinDeu == null || 
						planImpMinItem.getImpMinDeu().doubleValue() < minimoImpMinDeu.doubleValue()){
					minimoImpMinDeu= planImpMinItem.getImpMinDeu();
				}			
			}
		}
		
		if (planImpMin != null)
			log.debug(" ### Plan.getMinimoImpMinDeu: id: " + planImpMin.getId() +
				" cant. Cuo.: " + planImpMin.getCantidadCuotas() + 
				" impMinDeu.: " + planImpMin.getImpMinDeu());
		
		
		if (minimoImpMinDeu == null)
			return 0D;
		else 
			return minimoImpMinDeu;
		
	}
	
	/**
	 * Devuelve un valor PlanImpMin configurado en el plan para una cuota hasta y 
	 * que se encuentre vigente a una fecha dada 
	 * 
	 * @author Cristian
	 * @param nroCuota
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
	public PlanImpMin getPlanImpMin(Integer nroCuota, Date fecha) throws Exception{
		
		PlanImpMin planImpMin = null;
		int cuotaDesde = 1;
		int cuotaHasta;
		List<PlanImpMin> listPlanImpMinVigentes = this.getListPlanImpMinVigentes(fecha); // Se modifica obtiene la lista de Vigentes para arreglar mantis 5256.
		
		// Se supone la lista ordenada por PlanImpMin.cantidadCuotas 
		for (PlanImpMin planImpMinItem: listPlanImpMinVigentes){ //getListPlanImpMin()){
			
			cuotaHasta = planImpMinItem.getCantidadCuotas().intValue();
			
			log.debug(" ### Plan.getImpMinDeu("+ nroCuota + ", " + 
					  DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + " ) - " +
					  " cuotaDesde: " + cuotaDesde + 
					  " cuotaHasta: " + cuotaHasta);
			
			// Mientras sea cantidadCuotas menor o igual a cuotaHasta. 
			if (nroCuota.intValue() >= cuotaDesde && 
					nroCuota.intValue() <= cuotaHasta){
				// Si se encuentra vigente.
				//if (planImpMinItem.getVigenciaForDate(fecha).intValue() == Vigencia.VIGENTE.getId().intValue())
				planImpMin = planImpMinItem;					
			}
			
			cuotaDesde = planImpMinItem.getCantidadCuotas().intValue() + 1;			
		}
		
		if (planImpMin != null)
			log.debug(" ### Plan.getImpMinDeu id: " + planImpMin.getId() + 
					  " cant. cuo: " + planImpMin.getCantidadCuotas() + 
					  " min Deu.: " + planImpMin.getImpMinDeu() + 
					  " min Cuo.: " + planImpMin.getImpMinCuo() );
		
		return planImpMin;
	}
	
	/**
	 * Devuelve un valor de importe minimo de deuda configurado en el plan para una cuota hasta y 
	 * que se encuentre vigente a una fecha dada 
	 * 
	 * @author Cristian
	 * @param nroCuota
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
	public Double getImpMinDeu(Integer nroCuota, Date fecha) throws Exception{
		
		PlanImpMin planImpMin = getPlanImpMin(nroCuota,fecha);
		
		if (planImpMin != null)
			return planImpMin.getImpMinDeu();
		else
			return 0d;
		
	}
	
	/**
	 * Devuelve un valor de importe minimo de cuota configurado en el plan para una cuota hasta y 
	 * que se encuentre vigente a una fecha dada 
	 * 
	 * @author Cristian
	 * @param nroCuota
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
	public Double getImpMinCuo(Integer nroCuota, Date fecha) throws Exception{
		
		if (this.esManual.intValue()==1){
			if (datosPlanEspecial.getImpMinCuo()!=null){
				return datosPlanEspecial.getImpMinCuo();
			}else{
				return 0D;
			}
		}else{
			PlanImpMin planImpMin = getPlanImpMin(nroCuota,fecha);

			if (planImpMin != null)
				return planImpMin.getImpMinCuo();
			else
				return 0d;
		}
	}
	
	
	public Double getAnticipo(int nroCuotas, Double monto){
		
		if (this.esManual.intValue()==1 && nroCuotas!=1){
			if (datosPlanEspecial.getImporteAnticipo()==null ||
					datosPlanEspecial.getImporteAnticipo().doubleValue() == 0){
				return monto / nroCuotas;
			}else{
				return getDatosPlanEspecial().getImporteAnticipo();
			}
		}else{
			return monto / nroCuotas;
		}
		
	}
	
	/**
	 * 	Obtiene el Rescate Vigente para la fecha pasada. Si no encuentra un Rescate devuelve null
	 * 
	 * @param fecha
	 * @return
	 */
	public Rescate obtenerRescate(Date fecha) throws Exception{
		return (Rescate) GdeDAOFactory.getRescateDAO().getByPlanYFecha(this, fecha);
	}
	
	/**
	 * Obtiene el numero de convenio segun el nombre de la secuencia 
	 * cargada desde el mantenedor u utilizando la default si la misma es nula. 
	 * 
	 * @author Cristian
	 * @return
	 */
	public Integer obtenerNroConvenio(){
		
		if (getNameSequence() == null)
			setNameSequence(DEFAULT_SEQUENCE_NAME);
		
		return new Integer("" + GdeDAOFactory.getPlanDAO().getNextVal(this.getNameSequence()));		
	}
	
	public Integer obtenerCanMaxCuo(){
		if (this.esManual.intValue()==1){
			if (getDatosPlanEspecial().getCantMaxCuo()!=null){
				return getDatosPlanEspecial().getCantMaxCuo();
			}else{
				return 10000;
			}
		}
		return getCanMaxCuo();
		
		
	}

	/**
	 * - Formalizacion de un Convenio de pago 
	 * 	- Crea el convenio
	 *  - Inicia el historico de estados del convenio
	 *  - Crea las cuotas del convenio
	 *  - Crea las convenioDeuda
	 *  - Actualiza la deuda con idConvenio.  
	 * 
	 * 
	 * @author Cristian
	 * @param cuenta
	 * @param viaDeuda
	 * @throws Exception 
	 */
	public Convenio formalizarConvenio(Convenio convenio, List<Deuda> listDeuda,
			List<ConvenioDeuda> listConvenioDeuda, 
			List<ConvenioCuota> listConvenioCuota) throws Exception{
		
		// Creacion del Convenio
		if (!convenio.validateCreate())
			return convenio;
		
		// Truncamos a dos decimales antes de guardar aca y en las ConvenioDeuda y ConvenioCuota
		// Total Capital Original
		convenio.setTotCapitalOriginal(NumberUtil.truncate(convenio.getTotCapitalOriginal(), SiatParam.DEC_IMPORTE_DB));
		// Descuento al Capital Original
		convenio.setDesCapitalOriginal(NumberUtil.truncate(convenio.getDesCapitalOriginal(), SiatParam.DEC_IMPORTE_DB));
		// Total Actualizacion
		convenio.setTotActualizacion(NumberUtil.truncate(convenio.getTotActualizacion(), SiatParam.DEC_IMPORTE_DB));
		// Descuento de la Actulizacion
		convenio.setDesActualizacion(NumberUtil.truncate(convenio.getDesActualizacion(), SiatParam.DEC_IMPORTE_DB));
		// Total Interes
		convenio.setTotInteres(NumberUtil.truncate(convenio.getTotInteres(), SiatParam.DEC_IMPORTE_DB));
		// Descuento Interes
		convenio.setDesInteres(NumberUtil.truncate(convenio.getDesInteres(), SiatParam.DEC_IMPORTE_DB));
		// Importe Convenio
		convenio.setTotImporteConvenio(NumberUtil.truncate(convenio.getTotImporteConvenio(), SiatParam.DEC_IMPORTE_DB));
		
		
		GdeDAOFactory.getConvenioDAO().update(convenio);
		
		log.debug( " formalizarConvenio()  idConvenio: " + convenio.getId() + " nroConvenio: " + convenio.getNroConvenio());
		
		// Creacion del Historico de Estados del Convenio
		ConEstCon conEstCon = new ConEstCon();
		conEstCon.setConvenio(convenio);
		conEstCon.setEstadoConvenio(convenio.getEstadoConvenio());
		conEstCon.setFechaConEstCon(new Date());
		conEstCon.setObservacion("Convenio creado. "+ convenio.getObservacionFor());
		
		GdeDAOFactory.getConEstConDAO().update(conEstCon);
		
		// Creacion de los Convenio Deuda		
		for (ConvenioDeuda convenioDeuda:listConvenioDeuda){			
			// Truncamos los valores antes de guardarlos.
			convenioDeuda.setCapitalOriginal(NumberUtil.truncate(convenioDeuda.getCapitalOriginal(), SiatParam.DEC_IMPORTE_DB ));
			convenioDeuda.setCapitalEnPlan(NumberUtil.truncate(convenioDeuda.getCapitalEnPlan(), SiatParam.DEC_IMPORTE_DB ));
			convenioDeuda.setActualizacion(NumberUtil.truncate(convenioDeuda.getActualizacion(), SiatParam.DEC_IMPORTE_DB ));
			convenioDeuda.setActEnPlan(NumberUtil.truncate(convenioDeuda.getActEnPlan(), SiatParam.DEC_IMPORTE_DB ));
			convenioDeuda.setTotalEnPlan(NumberUtil.truncate(convenioDeuda.getTotalEnPlan(), SiatParam.DEC_IMPORTE_DB ));
			convenioDeuda.setCapitalEnPlan(NumberUtil.truncate(convenioDeuda.getCapitalEnPlan(), SiatParam.DEC_IMPORTE_DB ));
			convenioDeuda.setSaldoEnPlan(NumberUtil.truncate(convenioDeuda.getSaldoEnPlan(), SiatParam.DEC_IMPORTE_DB ));
			
			convenio.createConvenioDeuda(convenioDeuda);
		}
		
		EstadoConCuo estadoImpago = EstadoConCuo.getById(EstadoConCuo.ID_IMPAGO);

		// Creacion de los Convenio Cuota
		for (ConvenioCuota convenioCuota:listConvenioCuota){			
			// Truncamos los valores antes de guardarlos.
			convenioCuota.setCapitalCuota(NumberUtil.truncate(convenioCuota.getCapitalCuota(), SiatParam.DEC_IMPORTE_DB ));
			convenioCuota.setInteres(NumberUtil.truncate(convenioCuota.getInteres(), SiatParam.DEC_PORCENTAJE_DB));
			convenioCuota.setImporteCuota(NumberUtil.truncate(convenioCuota.getImporteCuota(), SiatParam.DEC_IMPORTE_DB ));
			convenioCuota.setActualizacion(NumberUtil.truncate(convenioCuota.getActualizacion(), SiatParam.DEC_IMPORTE_DB ));
			
			convenioCuota.setEstadoConCuo(estadoImpago);
			convenio.createConvenioCuota(convenioCuota);
		}
		
		// Seteo del Id de Convenio creado en la Deuda seleccionada.
		for (Deuda deuda: listDeuda){
			deuda.setConvenio(convenio);
			deuda.setIdConvenio(convenio.getId());
			GdeGDeudaManager.getInstance().update(deuda);
		}
				
		return convenio;
	}

	
	/**
	 * - Formalizacion de un Convenio de pago 
	 * 	- Crea el convenio
	 *  - Inicia el historico de estados del convenio
	 *  - Crea las cuotas del convenio
	 *  - Crea las convenioDeuda
	 *  - Actualiza la deuda con idConvenio.  
	 * 
	 * 
	 * @author Cristian
	 * @param cuenta
	 * @param viaDeuda
	 * @throws Exception 
	 */
	public Convenio formalizarConvenioCyq(Convenio convenio, List<DeudaPrivilegio> listDeuda,
			List<ConvenioDeuda> listConvenioDeuda, 
			List<ConvenioCuota> listConvenioCuota) throws Exception{
		
		// Creacion del Convenio
		if (!convenio.validateCreate())
			return convenio;
		
		// Truncamos a dos decimales antes de guardar aca y en las ConvenioDeuda y ConvenioCuota
		// Total Capital Original
		convenio.setTotCapitalOriginal(NumberUtil.truncate(convenio.getTotCapitalOriginal(), SiatParam.DEC_IMPORTE_DB));
		// Descuento al Capital Original
		convenio.setDesCapitalOriginal(NumberUtil.truncate(convenio.getDesCapitalOriginal(), SiatParam.DEC_IMPORTE_DB));
		// Total Actualizacion
		convenio.setTotActualizacion(NumberUtil.truncate(convenio.getTotActualizacion(), SiatParam.DEC_IMPORTE_DB));
		// Descuento de la Actulizacion
		convenio.setDesActualizacion(NumberUtil.truncate(convenio.getDesActualizacion(), SiatParam.DEC_IMPORTE_DB));
		// Total Interes
		convenio.setTotInteres(NumberUtil.truncate(convenio.getTotInteres(), SiatParam.DEC_IMPORTE_DB));
		// Descuento Interes
		convenio.setDesInteres(NumberUtil.truncate(convenio.getDesInteres(), SiatParam.DEC_IMPORTE_DB));
		// Importe Convenio
		convenio.setTotImporteConvenio(NumberUtil.truncate(convenio.getTotImporteConvenio(), SiatParam.DEC_IMPORTE_DB));
				
		GdeDAOFactory.getConvenioDAO().update(convenio);
		
		log.debug( " formalizarConvenio()  idConvenio: " + convenio.getId() + " nroConvenio: " + convenio.getNroConvenio());
		
		// Creacion del Historico de Estados del Convenio
		ConEstCon conEstCon = new ConEstCon();
		conEstCon.setConvenio(convenio);
		conEstCon.setEstadoConvenio(convenio.getEstadoConvenio());
		conEstCon.setFechaConEstCon(new Date());
		conEstCon.setObservacion("Convenio creado. "+ convenio.getObservacionFor());
		
		GdeDAOFactory.getConEstConDAO().update(conEstCon);
		
		// Creacion de los Convenio Deuda		
		for (ConvenioDeuda convenioDeuda:listConvenioDeuda){			
			// Truncamos los valores antes de guardarlos.
			convenioDeuda.setCapitalOriginal(NumberUtil.truncate(convenioDeuda.getCapitalOriginal(), SiatParam.DEC_IMPORTE_DB ));
			convenioDeuda.setCapitalEnPlan(NumberUtil.truncate(convenioDeuda.getCapitalEnPlan(), SiatParam.DEC_IMPORTE_DB ));
			convenioDeuda.setActualizacion(NumberUtil.truncate(convenioDeuda.getActualizacion(), SiatParam.DEC_IMPORTE_DB ));
			convenioDeuda.setActEnPlan(NumberUtil.truncate(convenioDeuda.getActEnPlan(), SiatParam.DEC_IMPORTE_DB ));
			convenioDeuda.setTotalEnPlan(NumberUtil.truncate(convenioDeuda.getTotalEnPlan(), SiatParam.DEC_IMPORTE_DB ));
			convenioDeuda.setCapitalEnPlan(NumberUtil.truncate(convenioDeuda.getCapitalEnPlan(), SiatParam.DEC_IMPORTE_DB ));
			convenioDeuda.setSaldoEnPlan(NumberUtil.truncate(convenioDeuda.getSaldoEnPlan(), SiatParam.DEC_IMPORTE_DB ));
			
			// Es Deuda Privilegio
			convenioDeuda.setEsDeudaPriv(1);
			
			convenio.createConvenioDeuda(convenioDeuda);
		}
		
		EstadoConCuo estadoImpago = EstadoConCuo.getById(EstadoConCuo.ID_IMPAGO);

		// Creacion de los Convenio Cuota
		for (ConvenioCuota convenioCuota:listConvenioCuota){			
			// Truncamos los valores antes de guardarlos.
			convenioCuota.setCapitalCuota(NumberUtil.truncate(convenioCuota.getCapitalCuota(), SiatParam.DEC_IMPORTE_DB ));
			convenioCuota.setInteres(NumberUtil.truncate(convenioCuota.getInteres(), SiatParam.DEC_PORCENTAJE_DB));
			convenioCuota.setImporteCuota(NumberUtil.truncate(convenioCuota.getImporteCuota(), SiatParam.DEC_IMPORTE_DB ));
			convenioCuota.setActualizacion(NumberUtil.truncate(convenioCuota.getActualizacion(), SiatParam.DEC_IMPORTE_DB ));
			
			convenioCuota.setEstadoConCuo(estadoImpago);
			convenio.createConvenioCuota(convenioCuota);
		}
		
		// Seteo del Id de Convenio creado en la Deuda seleccionada.
		for (DeudaPrivilegio deuda: listDeuda){
			deuda.setConvenio(convenio);
			CyqDAOFactory.getDeudaPrivilegioDAO().update(deuda);
		}
				
		return convenio;
	}
	
	
	//	---> ABM Rescate
	public Rescate createRescate(Rescate rescate) throws Exception {
		
		// Validaciones de negocio
		if (!rescate.validateCreate()) {
			log.debug("Errores de validacion en la creacion del rescate");
			return rescate;
		}

		return this.createRescateValidado(rescate);
	}
	
	public Rescate createRescateValidado(Rescate rescate) throws Exception {
		
		GdeDAOFactory.getRescateDAO().update(rescate);

		return rescate;
	}

	
	public Rescate updateRescate(Rescate rescate) throws Exception {
		
		// Validaciones de negocio
		if (!rescate.validateUpdate()) {
			log.debug("Errores de validacion en la actualizacion del rescate");
			return rescate;
		}

		GdeDAOFactory.getRescateDAO().update(rescate);
		
		return rescate;
	}
	
	public Rescate deleteRescate(Rescate rescate) throws Exception {
	
		// Validaciones de negocio
		if (!rescate.validateDelete()) {
			log.debug("Errores de validacion en la eliminacion del rescate");
			return rescate;
		}
		
		GdeDAOFactory.getRescateDAO().delete(rescate);
		
		return rescate;
	}
	//	<--- ABM Rescate	
	
	
	/**
	 * Obtiene la Fecha de Vencimiento para Cuota o Recibo de Cuota. Para esto verifica que sea un dia habil, si no
	 * busca el proximo dia habil. Ademas verifica si existe una prorroga vigente para el Plan asociado y en dicho
	 * caso recalcula la fecha.
	 * 
	 * @param fechaVencimiento
	 * @return fechaVencimiento
	 */
	public Date obtenerFechaVencimientoCuotaORecibo(Date fechaVencimiento){
		Date nuevaFecha = fechaVencimiento;
		PlanProrroga planProrroga = GdeDAOFactory.getPlanProrrogaDAO().getVigenteByPlanYFecVen(this, fechaVencimiento);
		if(planProrroga != null)
			nuevaFecha = planProrroga.getFecVtoNue();
		nuevaFecha = Feriado.nextDiaHabil(nuevaFecha);
		return nuevaFecha;
	}

	/**
	 * Obtiene la Fecha de Vencimiento para Cuota o Recibo de Cuota. Para esto verifica que sea un dia habil, si no
	 * busca el proximo dia habil. Ademas verifica si existe una prorroga vigente para el Plan asociado y en dicho
	 * caso recalcula la fecha. (Usa el mapa de Feriado)
	 * 
	 * @param fechaVencimiento
	 * @param mapFeriado
	 * @return fechaVencimiento
	 */
	public Date obtenerFechaVencimientoCuotaORecibo(Date fechaVencimiento,Map<Date, Feriado> mapFeriado){
		Date nuevaFecha = fechaVencimiento;
		PlanProrroga planProrroga = GdeDAOFactory.getPlanProrrogaDAO().getVigenteByPlanYFecVen(this, fechaVencimiento);
		if(planProrroga != null)
			nuevaFecha = planProrroga.getFecVtoNue();
		nuevaFecha = Feriado.nextDiaHabilUsingMap(nuevaFecha, mapFeriado);
		return nuevaFecha;
	}
	
	@Override
	public String infoString() {
		String ret =" Plan";
		
		if(desPlan!=null){
			ret+=" - descripcion: "+desPlan;
		}
		
		if(viaDeuda!=null){
			ret+=" - para Deudas en Via: "+viaDeuda.getDesViaDeuda();
		}
		
		if(sistema!=null){
			ret+=" - Sistema: "+sistema.getDesSistema();
		}
		
		if(idCaso!=null){
			ret+=" - Caso: "+idCaso;
		}
		
		if(ordenanza!=null){
			ret+=" - Ordenanza: "+ordenanza;
		}
		
		return ret;
	}
	
	public static AuxImporteRecaudarReport generarPdfForReport(AuxImporteRecaudarReport auxImporteRecaudarReport) throws Exception{
		// A partir de la implementacion de Adp en la generacion de Reporte se tuvo que utilizar una clase auxiliar
		// para mantener el codigo de generacion existente. Entonces en este punto se crea un importeRecaudarReport y
		// se pasan los datos necesarios.
		ImporteRecaudarReport importeRecaudarReport = new ImporteRecaudarReport();
		importeRecaudarReport.setFechaVencimientoDesde(auxImporteRecaudarReport.getFechaVencimientoDesde());
		importeRecaudarReport.setFechaVencimientoHasta(auxImporteRecaudarReport.getFechaVencimientoHasta());
		if(auxImporteRecaudarReport.getPlan() != null){			
			importeRecaudarReport.setPlan((PlanVO) auxImporteRecaudarReport.getPlan().toVO(1, false));
		}else{
			importeRecaudarReport.getPlan().setId(-1L);
		}
		if(auxImporteRecaudarReport.getRecurso() != null){
			importeRecaudarReport.getPlan().setRecurso((RecursoVO) auxImporteRecaudarReport.getRecurso().toVO(0, false));
		}
		
		if(auxImporteRecaudarReport.getProcurador() != null && !auxImporteRecaudarReport.getProcurador().getId().equals(-1L)){
			importeRecaudarReport.getPlan().setProcurador((ProcuradorVO) auxImporteRecaudarReport.getProcurador().toVO(0, false));
		}else{
			importeRecaudarReport.getPlan().setProcurador(new ProcuradorVO());
			importeRecaudarReport.getPlan().getProcurador().setId(-1L);
		}
		
		// En este punto se termino de armar el ImporteRecaudarReport que antes se pasaba como parametro. 
	
		Date fecVtoDesde = importeRecaudarReport.getFechaVencimientoDesde();
		Date fecVtoHasta = importeRecaudarReport.getFechaVencimientoHasta();
		Long idProcurador = importeRecaudarReport.getPlan().getProcurador().getId();
		Long idPlan = importeRecaudarReport.getPlan().getId();
		Long idRecurso = importeRecaudarReport.getPlan().getRecurso().getId();
		
		Integer firstResult = 0;
		Integer maxResults = 15000;
		
		boolean contieneTransacciones = true;
		
		
		// Genera el objeto que se va a parsear
		ProcuradorPlanVOContainer container = new ProcuradorPlanVOContainer();
		//VO de Plan y Procuradores
		CantCuoVO cantCuoVO = new CantCuoVO();
		ProcuradorPlanVO procuradorPlanVO = new ProcuradorPlanVO();
		CantCuoProcuradorVO cantCuoProcuradorVO = new CantCuoProcuradorVO();
		//Id para comparar y agrupar.
		Long planIdOld = 0L;
		Long procuradorIdOld = 0L;
		//SubTotal por mes y a�o.
		Integer anioOld = 0;
		Integer mesOld = 0;
		boolean esPrimera = true;
		boolean esJudicial = true;
		boolean esPrimerProcurador = true;
		while (contieneTransacciones){

			List<Object> listResultSetCantCuoProcuradorVO = GdeDAOFactory.getImporteRecaudarDAO().getList(idRecurso, idPlan, idProcurador, fecVtoDesde, fecVtoHasta, firstResult, maxResults);
			
			contieneTransacciones = (listResultSetCantCuoProcuradorVO.size() > 0);
			
			/*
			 * IDPLan
			 * IDProcurador
			 * A�o
			 * Mes
			 * NroCuota
			 * CantCuotas
			 * ImporteTotal
			 */
			
			if(contieneTransacciones){
				for(Object obj:listResultSetCantCuoProcuradorVO){
					Object[] registro = (Object[]) obj;
					Long planId = new Long((Integer)registro[0]);
					Long procuradorId = 0L;
					
					if(esPrimera){
						planIdOld = planId;
						procuradorPlanVO.setPlan((PlanVO)Plan.getById(planId).toVO());
					}
					
					//Datos de Cuota
						cantCuoVO = new CantCuoVO();
						
						cantCuoVO.setProcurador(null);
						cantCuoVO.setAnio(new Integer(String.valueOf((Short)registro[2])));
						cantCuoVO.setMes(new Integer((Short)registro[3]));
						cantCuoVO.setNroCuota((Integer)registro[4]);
						cantCuoVO.setCantCuotas(new Integer(String.valueOf((BigDecimal)registro[5])));
						cantCuoVO.setImporteTotal(new Double(String.valueOf((BigDecimal)registro[6])));
						
						
					//Si esto ocurre: Hay procurador asignado y el plan es Judicial.
					if(registro[1] != null){
						esJudicial = true;
						procuradorId = new Long((Integer)registro[1]);
												
						if(esPrimera || esPrimerProcurador){
							procuradorIdOld = procuradorId;
							cantCuoProcuradorVO.setProcurador((ProcuradorVO)Procurador.getById(procuradorId).toVO());
							esPrimera=false;
							esPrimerProcurador=false;
						}
						
						if(cantCuoProcuradorVO.getListCantCuo().size()>0){
							cantCuoVO.setSubTotal(cantCuoProcuradorVO.getListCantCuo().get(cantCuoProcuradorVO.getListCantCuo().size()-1).getSubTotal()+cantCuoVO.getImporteTotal());
						} else {
							cantCuoVO.setSubTotal(cantCuoVO.getImporteTotal());
						}
						
						if((!cantCuoVO.getAnio().equals(anioOld) || !cantCuoVO.getMes().equals(mesOld))){
							cantCuoVO.setSubTotal(cantCuoVO.getImporteTotal());
						}
						
						//Cambio de Plan
						if(!planIdOld.equals(planId)){
							cantCuoVO.setSubTotal(cantCuoVO.getImporteTotal());
							container.getListProcuradorPlanVO().add(procuradorPlanVO);
							procuradorPlanVO = new ProcuradorPlanVO();
							procuradorPlanVO.setPlan((PlanVO)Plan.getById(planId).toVO());
						}
						
						//Cambio de Procurador
						if(!procuradorIdOld.equals(procuradorId)){
							System.out.println("Cambio de procurador: " + Procurador.getById(procuradorId).getDescripcion());
							cantCuoVO.setSubTotal(cantCuoVO.getImporteTotal());
							procuradorPlanVO.getListCantCuoProcurador().add(cantCuoProcuradorVO);
							cantCuoProcuradorVO = new CantCuoProcuradorVO();
							cantCuoProcuradorVO.setProcurador((ProcuradorVO)Procurador.getById(procuradorId).toVO());
						}
						cantCuoProcuradorVO.getListCantCuo().add(cantCuoVO);	
						
						//Variables para comparar y generar CdC.
						anioOld = cantCuoVO.getAnio();
						mesOld = cantCuoVO.getMes();
						planIdOld = planId;
						procuradorIdOld = procuradorId;
						
					} else {
					
						//Banderas Control
						esPrimera=false;
						esJudicial=false;
						esPrimerProcurador=true;
						
						if(procuradorPlanVO.getListCantCuo().size()>0){
							cantCuoVO.setSubTotal(procuradorPlanVO.getListCantCuo().get(procuradorPlanVO.getListCantCuo().size()-1).getSubTotal()+cantCuoVO.getImporteTotal());
						} else {
							cantCuoVO.setSubTotal(cantCuoVO.getImporteTotal());
						}
						
						if((!cantCuoVO.getAnio().equals(anioOld) || !cantCuoVO.getMes().equals(mesOld))){
							cantCuoVO.setSubTotal(cantCuoVO.getImporteTotal());
						}
						
						//Cambio de Plan
						if(!planIdOld.equals(planId)){
							cantCuoVO.setSubTotal(cantCuoVO.getImporteTotal());
							container.getListProcuradorPlanVO().add(procuradorPlanVO);
							procuradorPlanVO = new ProcuradorPlanVO();
							procuradorPlanVO.setPlan((PlanVO)Plan.getById(planId).toVO());
						}
					
						procuradorPlanVO.getListCantCuo().add(cantCuoVO);

						//Variables para comparar y generar CdC.
						anioOld = cantCuoVO.getAnio();
						mesOld = cantCuoVO.getMes();
						planIdOld = planId;
						
					}		
					
					
				}
				firstResult += maxResults; // Incremento el indice del 1er registro
				
				//Agrego el ultimo plan al final de la lista.
				if(esJudicial) procuradorPlanVO.getListCantCuoProcurador().add(cantCuoProcuradorVO);
				container.getListProcuradorPlanVO().add(procuradorPlanVO);
				
			}
			//Log Debug
			System.out.println("---------------------------------------");
			for(ProcuradorPlanVO plan:container.getListProcuradorPlanVO()){
				System.out.println("Pl: "+plan.getPlan().getDesPlan());
				if(plan.getListCantCuoProcurador().size()>0){
					for(CantCuoProcuradorVO procurador:plan.getListCantCuoProcurador()){
						System.out.println("\t Pr: "+procurador.getProcurador().getDescripcion());
						for(CantCuoVO cuota:procurador.getListCantCuo()){
							System.out.println("\t\t C: "+cuota.getCantCuotas());
						}
					}
				} else {
					for(CantCuoVO cuota:plan.getListCantCuo()){
						System.out.println("\t Cu: "+cuota.getCantCuotas());
					}
				}
				
				
			}
			System.out.println("---------------------------------------");
		
			//Parte vieja inexpandible...
			
			/*if(contieneTransacciones){
				
				for(Object obj:listResultSetCantCuoProcuradorVO){
					Object[] objs = (Object[]) obj;

					CantCuoProcuradorVO cantCuoProcuradorVO = new CantCuoProcuradorVO();
					cantCuoProcuradorVO.setIdProcurador(new Long((Integer)objs[0]));

					// Busca el plan solo si es distinto del anterior
					if(plan==null || !plan.getId().equals(cantCuoProcuradorVO.getIdProcurador())){
						plan = Plan.getById(cantCuoProcuradorVO.getIdProcurador());
						cantCuoProcuradorVO.setDesProcurador(plan.getDesPlan());							
					}
					
					cantCuoProcuradorVO.setProcurador(null);//lo setea null para cuando se haga el XML, que sean menos filas
					cantCuoProcuradorVO.setAnio(new Integer(String.valueOf((Short)objs[1])));
					cantCuoProcuradorVO.setMes(new Integer((Short)objs[2]));
					cantCuoProcuradorVO.setNroCuota((Integer)objs[3]);
					cantCuoProcuradorVO.setCantCuotas(new Integer(String.valueOf((BigDecimal)objs[4])));
					cantCuoProcuradorVO.setImporteTotal(new Double(String.valueOf((BigDecimal)objs[5])));
					importeRecaudarReport.getListResult().add(cantCuoProcuradorVO);
					AdpRun.currentRun().logDebug("contador:"+ ++contador+"    Agrego un cantCuoProcuradorVO - "+cantCuoProcuradorVO.getIdProcurador()+
							"    "+cantCuoProcuradorVO.getAnio()+"   "+cantCuoProcuradorVO.getMes()+"   "+cantCuoProcuradorVO.getNroCuota()+
							"   "+cantCuoProcuradorVO.getCantCuotas());
				}
				firstResult += maxResults; // Incremento el indice del 1er registro
				
			}*/
		}			
		
		// Generamos el pdf
		importeRecaudarReport.prepararParaPDF();
		container.setRecurso((RecursoVO)Recurso.getById(idRecurso).toVO());
		
		container.setFechaVencimientoDesdeView(importeRecaudarReport.getFechaVencimientoDesdeView());
		container.setFechaVencimientoHastaView(importeRecaudarReport.getFechaVencimientoHastaView());

		container.calcularTotal();
		
		// Generamos el printModel
		PlanillaVO reporteGenerado = new PlanillaVO();
		
		PrintModel print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_IMPORTE_RECAUDAR_PROCURADOR_PLAN);		
		
		//Como su nombre lo indica, no elimina los espacios, este es un comentario inncesario.
		print.setNoAplicarTrim(true);
		
		print.putCabecera("TituloReporte", "Reporte de Importe a Recaudar");
		print.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
		print.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
		print.putCabecera("Usuario", auxImporteRecaudarReport.getUserName());
		
		print.setData(container);
		print.setTopeProfundidad(4);

		String idUsuario = auxImporteRecaudarReport.getUserId();
		String idCorrida = AdpRun.currentRun().getId().toString();
		
		//String fileSharePath = SiatParam.getString("FileSharePath"); 
		//String fileDir = fileSharePath + "/tmp"; 
		String fileDir = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);
					
		// Archivo pdf a generar
		String fileNamePdf = idCorrida+"ImportesARecaudarPorPlan"+ idUsuario +".pdf"; 
		File pdfFile = new File(fileDir+"/"+fileNamePdf);
		
		OutputStream out = new java.io.FileOutputStream(pdfFile);
		
		out.write(print.getByteArray());
					
		reporteGenerado.setFileName(fileDir+"/"+fileNamePdf);
		reporteGenerado.setDescripcion("Reporte de Importes a Recaudar de Planes");
		reporteGenerado.setCtdResultados(1L);
		
		importeRecaudarReport.setReporteGenerado(reporteGenerado);
		auxImporteRecaudarReport.setReporteGenerado(reporteGenerado);
		
		return auxImporteRecaudarReport;
	}
	
	public static AuxImporteRecaudadoReport generarPdfForReport(AuxImporteRecaudadoReport auxImporteRecaudadoReport) throws Exception{
		// A partir de la implementacion de Adp en la generacion de Reporte se tuvo que utilizar una clase auxiliar
		// para mantener el codigo de generacion existente. Entonces en este punto se crea un importeRecaudadoReport y
		// se pasan los datos necesarios.
		ImporteRecaudadoReport importeRecaudadoReport = new ImporteRecaudadoReport();
		importeRecaudadoReport.setFechaPagoDesde(auxImporteRecaudadoReport.getFechaPagoDesde());
		importeRecaudadoReport.setFechaPagoHasta(auxImporteRecaudadoReport.getFechaPagoHasta());
		
		//Plan
		if(auxImporteRecaudadoReport.getPlan() != null){			
			importeRecaudadoReport.setPlan((PlanVO) auxImporteRecaudadoReport.getPlan().toVO(1, false));
		}else{
			importeRecaudadoReport.getPlan().setId(-1L);
		}
		//Recurso
		if(auxImporteRecaudadoReport.getRecurso() != null){
			importeRecaudadoReport.getPlan().setRecurso((RecursoVO) auxImporteRecaudadoReport.getRecurso().toVO(0, false));
		}else{
			importeRecaudadoReport.getPlan().setRecurso(new RecursoVO());
			importeRecaudadoReport.getPlan().getRecurso().setId(-1L);
		}
		//Via Deuda
		if(auxImporteRecaudadoReport.getViaDeuda() != null){
			importeRecaudadoReport.getPlan().setViaDeuda((ViaDeudaVO) auxImporteRecaudadoReport.getViaDeuda().toVO(0, false));
		}else{
			importeRecaudadoReport.getPlan().setViaDeuda(new ViaDeudaVO());
			importeRecaudadoReport.getPlan().getViaDeuda().setId(-1L);
		}
		//Procurador
		if(auxImporteRecaudadoReport.getProcurador() != null){
			importeRecaudadoReport.getPlan().setProcurador((ProcuradorVO) auxImporteRecaudadoReport.getProcurador().toVO(0, false));
		}else{
			importeRecaudadoReport.getPlan().setProcurador(new ProcuradorVO());
			importeRecaudadoReport.getPlan().getProcurador().setId(-1L);
		}
		
		importeRecaudadoReport.setTipoReporte(auxImporteRecaudadoReport.getTipoReporte());
		// en este punto se termino de armar el ImporteRecaudadoReport que antes se pasaba como parametro. 
	
		Integer firstResult = 0;
		Integer maxResults = 1000;
		
		boolean contieneTransacciones = true;
		double cantidadLineas = 0D;
		int contador =0;
		if(importeRecaudadoReport.getTipoReporte().equals(0)){
			//Reporte Detallado
			System.out.println("Reporte Detallado");
			
			List<ConvenioCuotaVO> listConvenioCuotaVO = new ArrayList<ConvenioCuotaVO>();
			while (contieneTransacciones){

				// Consulta Reporte Detallado
				List<Object> listResult = GdeDAOFactory.getImporteRecaudadoDAO().getForReporteDetallado(
						importeRecaudadoReport.getPlan().getRecurso().getId(),
						importeRecaudadoReport.getPlan().getId(), importeRecaudadoReport.getFechaPagoDesde(),
						importeRecaudadoReport.getFechaPagoHasta(), 
						importeRecaudadoReport.getPlan().getViaDeuda().getId(),
						importeRecaudadoReport.getPlan().getProcurador().getId(),
						firstResult, maxResults);
				
				contieneTransacciones = (listResult.size() > 0);
				cantidadLineas += listResult.size();
				if(contieneTransacciones){
					Persona contribuyente = null;
					for(Object obj:listResult){
						Object[] objs = (Object[]) obj;
						ConvenioCuotaVO convenioCuota  = new ConvenioCuotaVO();
						
						/*
						 * 0 plan.id idPlan
						 * 1 plan.desplan plan
						 * 2 viaDeuda.desviadeuda
						 * 3 convenio.idproducrador
						 * 4 year(cuota.fechapago) anio
						 * 5 month(cuota.fechapago) mes
						 * 6 convenio.nroconvenio nroConvenio
						 * 7 convenio.idperfor idContribuyente
						 * 8 cuota.numeroCuota
						 * 9 cuota.fechapago
						 * 10 cuota.capitalcuota
						 * 11 cuota.interes
						 * 12 cuota.actualizacion
						 * 13 cuota.importecuota
						 * 14 cuota.importePago 
						 */
						
						// Seteo los datos del plan
						convenioCuota.setIdPlan((new Long((Integer)objs[0])));
						convenioCuota.setDesPlan((String)objs[1]);
						convenioCuota.setDesViaDeuda((String)objs[2]);
						if((Integer)objs[3]!=null){
							convenioCuota.setIdProcurador((new Long((Integer)objs[3])));
							convenioCuota.setDesProcurador(Procurador.getById(convenioCuota.getIdProcurador()).getDescripcion());
						}
						convenioCuota.setAnio(new Integer(String.valueOf((Short)objs[4])));
						convenioCuota.setMes(new Integer((Short)objs[5]));
						
						convenioCuota.setNroConvenio((Integer)objs[6]);
						
						if(objs[7]!=null){
							Long idContrib = new Long((Integer)objs[7]);
							if(contribuyente==null || !contribuyente.getId().equals(idContrib)){
								AdpRun.currentRun().logDebug("Va a buscar contibuyente con id:"+idContrib);
								contribuyente = Persona.getByIdLight(new Long(idContrib));
							}
							convenioCuota.setPerFor(contribuyente != null ? contribuyente.getRepresent() : "");												
						} else {
							AdpRun.currentRun().logDebug("No tiene un contribuyente asociado");
							convenioCuota.setPerFor("");
						}
						
						convenioCuota.setNumeroCuota((Integer)objs[8]);
						convenioCuota.setFechaPago((Date)objs[9]);
						
						BigDecimal capital = (BigDecimal) (objs[10]!=null?objs[10]:0D);
						BigDecimal interes = (BigDecimal) (objs[11]!=null?objs[11]:0D);
						
						Double actualizacion=0D;
						if(objs[12]==null){
							actualizacion=0D;
						}else if(objs[12] instanceof BigDecimal) {
							actualizacion = new Double(String.valueOf((BigDecimal) (objs[11])));
						}else if(objs[12] instanceof Double){
							actualizacion = (Double) (objs[11]);
						}
							
						BigDecimal importeTotal = (BigDecimal) (objs[13]!=null?objs[13]:0D);
						
						Double totPago = new Double (String.valueOf((BigDecimal) (objs[14]!=null?objs[14]:new BigDecimal(0))));
						
						convenioCuota.setCapitalCuota(new Double(String.valueOf(capital)));
						convenioCuota.setInteres(new Double(String.valueOf(interes)));
						convenioCuota.setActualizacion(actualizacion);
						convenioCuota.setImporteCuota(new Double(String.valueOf(importeTotal))+convenioCuota.getActualizacion());
						Double ajuste = 0D;
						if (totPago < convenioCuota.getImporteCuota() && totPago >0D){
							ajuste = convenioCuota.getImporteCuota()-totPago;
							//Si el total pago es menor al importe de la cuota (ej en pago por cuota saldo) resto la diferencia al interes y al total pago que voy a mostrar
							convenioCuota.setInteres(convenioCuota.getInteres()-ajuste);
							convenioCuota.setImporteCuota(convenioCuota.getImporteCuota()-ajuste);
						}
												
						listConvenioCuotaVO.add(convenioCuota);
						System.out.println(
								"Contador:"+ ++contador +
								"\n\tAgrego un planRecPeriodo: "+convenioCuota.getDesPlan()+" / "+convenioCuota.getAnio()+" / "+convenioCuota.getMes() +
								"\n\tProcurador" + convenioCuota.getDesProcurador()
								);
								
					}
					firstResult += maxResults; // Incremento el indice del 1er registro
				}
				
				if(cantidadLineas >= 9500) {
					importeRecaudadoReport.setMsgReporteExcesivo("Reporte excesivamente grande, ("+ cantidadLineas +" lineas)por favor disminuya los l�mites de b�squeda.");
					break;
				}
				
			}
			importeRecaudadoReport.setListResult(listConvenioCuotaVO);
		}else{
			// Reporte Resumido
			System.out.println("Reporte Resumido");
			
			List<PlanRecPeriodoVO> listPlanRecPeriodo = new ArrayList<PlanRecPeriodoVO>();
			while (contieneTransacciones){
				
				// Consulta Reporte Resumido
				List<Object> listResult = GdeDAOFactory.getImporteRecaudadoDAO().getForReporteResumido(
						importeRecaudadoReport.getPlan().getRecurso().getId(),
						importeRecaudadoReport.getPlan().getId(), importeRecaudadoReport.getFechaPagoDesde(),
						importeRecaudadoReport.getFechaPagoHasta(), 
						importeRecaudadoReport.getPlan().getViaDeuda().getId(), 
						importeRecaudadoReport.getPlan().getProcurador().getId(), 
						firstResult, maxResults);
				
				contieneTransacciones = (listResult.size() > 0);

				if(contieneTransacciones){
					for(Object obj:listResult){
						Object[] objs = (Object[]) obj;

						/*
						 * 0 plan.id idPlan
						 * 1 plan.desplan
						 * 2 viaDeuda.desviadeuda
						 * 3 convenio.idprocurador
						 * 4 year(cuota.fechapago) anio
						 * 5 month(cuota.fechapago) mes
						 * 6 count(convenio.id) cantConvenios
						 * 7 sum(cuota.capitalcuota) totCapital
						 * 8 sum(cuota.interes) totInteres
						 * 9 sum(cuota.actualizacion) totAct
						 * 10 sum(cuota.importecuota) totImporte
						 * 11 sum(cuota.importePago) totImpPag
						 */
						
						PlanRecPeriodoVO planRecPeriodo  = new PlanRecPeriodoVO();
												
						// Seteo los datos del plan
						planRecPeriodo.setIdPlan((new Long((Integer)objs[0])));
						planRecPeriodo.setDesPlan((String)objs[1]);
						planRecPeriodo.setDesViaDeuda((String)objs[2]);
						if((Integer)objs[3]!=null){
							planRecPeriodo.setIdProcurador((new Long((Integer)objs[3])));
							planRecPeriodo.setDesProcurador(Procurador.getById(planRecPeriodo.getIdProcurador()).getDescripcion());
						}
						
						planRecPeriodo.setAnio(new Integer(String.valueOf((Short)objs[4])));
						planRecPeriodo.setMes(new Integer((Short)objs[5]));
						planRecPeriodo.setCantCuotas(new Integer(String.valueOf((BigDecimal)objs[6])));
						
						BigDecimal capital = (BigDecimal) (objs[6]!=null?objs[7]:0D);
						BigDecimal interes = (BigDecimal) (objs[7]!=null?objs[8]:0D);
						Double actualizacion=0D;
						if(objs[9]==null){
							actualizacion=0D;
						}else if(objs[9] instanceof BigDecimal) {
							actualizacion = new Double(String.valueOf((BigDecimal) (objs[9])));
						}else if(objs[9] instanceof Double){
							actualizacion = (Double) (objs[9]);
						}
							
						BigDecimal importeTotal = (BigDecimal) (objs[9]!=null?objs[10]:0D);
						
						Double totImpPago = 0D;
						if(objs[11]==null){
							totImpPago=0D;
						}else if(objs[11] instanceof BigDecimal) {
							totImpPago = new Double(String.valueOf((BigDecimal) (objs[11])));
						}else if(objs[11] instanceof Double){
							totImpPago = (Double) (objs[11]);
						}
						
						planRecPeriodo.setTotCapital(new Double(String.valueOf(capital)));
						planRecPeriodo.setTotInteres(new Double(String.valueOf(interes)));
						planRecPeriodo.setTotActualiz(actualizacion);
						planRecPeriodo.setTotImporte(new Double(String.valueOf(importeTotal))+planRecPeriodo.getTotActualiz());
											
						Double ajuste = 0D;
						if (totImpPago < planRecPeriodo.getTotImporte() && totImpPago >0D){
							ajuste = planRecPeriodo.getTotImporte()-totImpPago;
							//Si el total pago es menor al importe de la cuota (ej en pago por cuota saldo) resto la diferencia al interes y al total pago que voy a mostrar
							planRecPeriodo.setTotInteres(planRecPeriodo.getTotInteres()-ajuste);
							planRecPeriodo.setTotImporte(planRecPeriodo.getTotImporte()-ajuste);
						}
						
						listPlanRecPeriodo.add(planRecPeriodo);
						System.out.println(
								"Contador:"+ ++contador +
								"\n\tAgrego un planRecPeriodo: "+planRecPeriodo.getDesPlan()+" / "+planRecPeriodo.getAnio()+" / "+planRecPeriodo.getMes() +
								"\n\tProcurador" + planRecPeriodo.getDesProcurador()
								);
					}
					firstResult += maxResults; // Incremento el indice del 1er registro
				}
			}
			importeRecaudadoReport.setListResult(listPlanRecPeriodo);
		}
					
		// Genera el objeto que se va a parsear
		InformeRecaudacionConvenios informe = new InformeRecaudacionConvenios();
		
		informe.setUsr(auxImporteRecaudadoReport.getUserName());
		informe.setDesRecurso(Recurso.getById(importeRecaudadoReport.getPlan().getRecurso().getId()).getDesRecurso());
		Long idPlan = importeRecaudadoReport.getPlan().getId();
		informe.setDesPlan(idPlan!=null && idPlan.longValue()>0?Plan.getById(idPlan).getDesPlan():StringUtil.SELECT_OPCION_TODOS);
		
		Long idVia = importeRecaudadoReport.getPlan().getViaDeuda().getId();
		Long idProcurador = importeRecaudadoReport.getPlan().getProcurador().getId();

		if(idVia!=null && idVia.longValue()>0) {
			informe.setDesViaDeuda(ViaDeuda.getById(idVia).getDesViaDeuda());
		} else {
			informe.setDesViaDeuda(StringUtil.SELECT_OPCION_TODOS);
		}
		
		if(!idVia.equals(ViaDeuda.ID_VIA_ADMIN))
			if(idProcurador!=null && idProcurador.longValue()>0) 
				informe.setDesProcurador(Procurador.getById(idProcurador).getDescripcion());
			else informe.setDesProcurador(StringUtil.SELECT_OPCION_TODOS);
		else informe.setDesProcurador(StringUtil.SELECT_OPCION_NINGUNO);
		
		System.out.println("Valores en informe:\n\tViaDeuda:"+informe.getDesViaDeuda()+"\n\tProcurador:"+informe.getDesProcurador());
				
		informe.setFechaPagoDesde(importeRecaudadoReport.getFechaPagoDesde());
		informe.setFechaPagoHasta(importeRecaudadoReport.getFechaPagoHasta());
		
		// Generamos el printModel
		PlanillaVO reporteGenerado = new PlanillaVO();

		PrintModel print =null;
		
		if(importeRecaudadoReport.getTipoReporte().intValue()==0){
			// Reporte Detallado
			informe.setListConvenioCuota(importeRecaudadoReport.getListResult());
			informe.calcualrTotalesReporteDetallado();		
			print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_IMPORTE_RECAUDADO_PLANES_DETALLADO);
		}else{
			// Reporte Resumido
			informe.setListPlanRecPeriodo(importeRecaudadoReport.getListResult());
			informe.calcualrTotalesReporteResumido();		
			print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_IMPORTE_RECAUDADO_PLANES_RESUMIDO);
		}
		
		print.putCabecera("TituloReporte", "Reporte de Importes Recaudados por Planes");
		print.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
		print.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
		print.putCabecera("Usuario", auxImporteRecaudadoReport.getUserName());
		
		print.setData(informe);
		print.setTopeProfundidad(3);
		
		String idUsuario = auxImporteRecaudadoReport.getUserId();
		String idCorrida = AdpRun.currentRun().getId().toString();
		
		//String fileSharePath = SiatParam.getString("FileSharePath"); 
		//String fileDir = fileSharePath + "/tmp"; 
		String fileDir = AdpRun.currentRun().getProcessDir(AdpRunDirEnum.SALIDA);
					
		// Archivo pdf a generar
		String fileNamePdf = idCorrida+"ImporteRecaudadoPorPlan"+ idUsuario +".pdf"; 
		File pdfFile = new File(fileDir+"/"+fileNamePdf);
		
		OutputStream out = new java.io.FileOutputStream(pdfFile);
		
		out.write(print.getByteArray());
					
		reporteGenerado.setFileName(fileDir+"/"+fileNamePdf);
		reporteGenerado.setDescripcion("Reporte de  Importes Recaudados por Planes");
		reporteGenerado.setCtdResultados(1L);
		
		importeRecaudadoReport.setReporteGenerado(reporteGenerado);

		auxImporteRecaudadoReport.setReporteGenerado(reporteGenerado);
		
		return auxImporteRecaudadoReport;
	}

	public String getCasoView(){		
		CasoVO caso = CasServiceLocator.getCasCasoService().construirCasoVO(getIdCaso());		
		return caso.getCasoView();
	}
}
