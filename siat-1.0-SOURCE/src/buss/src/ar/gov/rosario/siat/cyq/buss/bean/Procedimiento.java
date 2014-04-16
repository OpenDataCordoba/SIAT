//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoVO;
import ar.gov.rosario.siat.cyq.iface.util.CyqError;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.ef.buss.bean.OrdenControl;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConvenio;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Procedimiento
 * 
 * @author tecso
 */
@Entity
@Table(name = "cyq_procedimiento")
public class Procedimiento extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	public static final String SEQUENCE_NAME = "cyq_proced_nro_sq";
	
	@Column(name = "numero")
	private Integer numero;
	
	@Column(name = "anio")
	private Integer anio;
	
	@Column(name = "fechaAlta")
	private Date fechaAlta; // fecha del procedimiento
	
	@Column(name = "fechaBoletin")
	private Date fechaBoletin;
	
	@Column(name="idcontribuyente")
	private Long idContribuyente;

	@Transient
	private Persona contribuyente;
	
	@Column(name = "desContribuyente")
	private String desContribuyente;
	
	@Column(name = "domicilio")
	private String domicilio;

	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idTipoProceso") 
	private TipoProceso tipoProceso;

	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idJuzgado") 
	private Juzgado juzgado;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idAbogado") 
	private Abogado abogado;
	
	@Column(name = "caratula")
	private String caratula;
	
	@Column(name = "numExp")
	private Integer numExp; // numero del expediente del juzgado
	
	@Column(name = "anioExp")
	private Integer anioExp;  // anio del expediente del juzgado

	@Column(name="idCaso") 
	private String idCaso; // Expediente de la Municipalidad.
	
	@Column(name = "fechaVerOpo")
	private Date fechaVerOpo; // Fecha de Verificacion / Oposicion
	
	@Column(name = "fechaAltaVer")
	private Date fechaAltaVer; // Fecha de Alta Verificacion
	
	@Column(name = "fechaInfInd")
	private Date fechaInfInd; // Fecha Informe Individual
	
	@Column(name = "perOpoDeu")
	private String perOpoDeu; // Sindico Designado / Persona Oponer Deudac
	
	@Column(name = "lugarOposicion")
	private String lugarOposicion; // Domicilio Síndico/Lugar Oposición
	
	@Column(name = "telefonoOposicion")
	private String telefonoOposicion; // Teléfono Sindico/Lugar Oposición
	
	@Column(name = "observacion")
	private String observacion;
	
	@Column(name = "fechaHomo")
	private Date fechaHomo; // Fecha Homologacion
	
	@Column(name = "auto")
	private String auto;
	
	@Column(name = "fechaAuto")
	private Date fechaAuto; // es la "Fecha de Actualizacion de Deuda" mostrada en la liquidacion de la deuda.
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idProcedAnt") 
	private Procedimiento procedAnt; // Procedimiento Anterior
	
	@Column(name = "idProcedAnt", insertable=false, updatable=false)
	private Long idProcedAnt;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idEstadoProced") 
	private EstadoProced estadoProced;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idProCyQ") 
	private Proceso proCyQ;

	@Column(name = "fechaBaja")
	private Date fechaBaja;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idMotivoBaja") 
	private MotivoBaja motivoBaja;
	
	@Column(name = "observacionBaja")
	private String observacionBaja;
	
	@Column(name = "nroSentenciaBaja")
	private String nroSentenciaBaja;
	 
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idMotivoResInf")
	private MotivoResInf motivoResInf;
	 
	@Column(name = "recursoRes")
	private Integer recursoRes;
	 
	@Column(name = "nuevaCaratulaRes")
	private String nuevaCaratulaRes;
	 
	@Column(name = "codExpJudRes")
	private String codExpJudRes; // Codigo Expediente Judicial Informe Resolucion
	 
	@Column(name = "privGeneral")
	private Double privGeneral;
	 
	@Column(name = "privEspecial")
	private Double privEspecial;
	 
	@Column(name = "quirografario")
	private Double quirografario;
	
	@OneToMany()
	@JoinColumn(name="idProcedimiento")
	@OrderBy(clause="id asc")
	private List<HisEstProced> listHisEstProced;
	
	@OneToMany()
	@JoinColumn(name="idProcedimiento")
	private List<OrdenControl> listOrdenControl;
		
	@OneToMany()
	@JoinColumn(name="idProcedimiento")
	private List<ProCueNoDeu> listProCueNoDeu;
		
	@OneToMany()
	@JoinColumn(name="idProcedimiento")
	private List<ProDet> listProDet;
	
	@OneToMany()
	@JoinColumn(name="idProcedimiento")
	@OrderBy(clause="idRecurso, idTipoPrivilegio")
	private List<DeudaPrivilegio> listDeudaPrivilegio;
	
	@OneToMany()
	@JoinColumn(name="idProcedimiento")
	@OrderBy(clause="fecha")
	private List<PagoPriv> listPagoPriv;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idProcedimientoCyq")
	private List<Convenio> listConvenios;
	
	@Transient
	private boolean superaMaxCantCuentas = false;
	
	// Constructores
	public Procedimiento(){
		super();
	}
	
	public Procedimiento(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static Procedimiento getById(Long id) {
		return (Procedimiento) CyqDAOFactory.getProcedimientoDAO().getById(id);
	}
	
	public static Procedimiento getByIdNull(Long id) {
		return (Procedimiento) CyqDAOFactory.getProcedimientoDAO().getByIdNull(id);
	}
	
	public static List<Procedimiento> getList() {
		return (ArrayList<Procedimiento>) CyqDAOFactory.getProcedimientoDAO().getList();
	}
	
	public static List<Procedimiento> getListActivos() {			
		return (ArrayList<Procedimiento>) CyqDAOFactory.getProcedimientoDAO().getListActiva();
	}
	
	public static Integer getNextNumero() throws Exception {
		return CyqDAOFactory.getProcedimientoDAO().getNextNumero();
	}
	
	public static Procedimiento getByNumeroyAnio(Integer numero, Integer anio) throws Exception {
		return (Procedimiento) CyqDAOFactory.getProcedimientoDAO().getByNumeroyAnio(numero, anio);
	}
	
	// Getters y setters
	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
	public Date getFechaAuto() {
		return fechaAuto;
	}

	public void setFechaAuto(Date fechaAuto) {
		this.fechaAuto = fechaAuto;
	}

	public Integer getAnioExp() {
		return anioExp;
	}

	public void setAnioExp(Integer anioExp) {
		this.anioExp = anioExp;
	}

	public String getCaratula() {
		return caratula;
	}

	public void setCaratula(String caratula) {
		this.caratula = caratula;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Integer getNumExp() {
		return numExp;
	}

	public void setNumExp(Integer numExp) {
		this.numExp = numExp;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	
	public String getAuto() {
		return auto;
	}

	public void setAuto(String auto) {
		this.auto = auto;
	}

	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public EstadoProced getEstadoProced() {
		return estadoProced;
	}

	public void setEstadoProced(EstadoProced estadoProced) {
		this.estadoProced = estadoProced;
	}

	public Date getFechaAltaVer() {
		return fechaAltaVer;
	}

	public void setFechaAltaVer(Date fechaAltaVer) {
		this.fechaAltaVer = fechaAltaVer;
	}

	public Date getFechaBoletin() {
		return fechaBoletin;
	}

	public void setFechaBoletin(Date fechaBoletin) {
		this.fechaBoletin = fechaBoletin;
	}

	public Date getFechaHomo() {
		return fechaHomo;
	}

	public void setFechaHomo(Date fechaHomo) {
		this.fechaHomo = fechaHomo;
	}

	public Date getFechaInfInd() {
		return fechaInfInd;
	}

	public void setFechaInfInd(Date fechaInfInd) {
		this.fechaInfInd = fechaInfInd;
	}

	public Date getFechaVerOpo() {
		return fechaVerOpo;
	}

	public void setFechaVerOpo(Date fechaVerOpo) {
		this.fechaVerOpo = fechaVerOpo;
	}

	public Juzgado getJuzgado() {
		return juzgado;
	}

	public void setJuzgado(Juzgado juzgado) {
		this.juzgado = juzgado;
	}

	public String getLugarOposicion() {
		return lugarOposicion;
	}

	public void setLugarOposicion(String lugarOposicion) {
		this.lugarOposicion = lugarOposicion;
	}

	public String getPerOpoDeu() {
		return perOpoDeu;
	}

	public void setPerOpoDeu(String perOpoDeu) {
		this.perOpoDeu = perOpoDeu;
	}

	public Proceso getProCyQ() {
		return proCyQ;
	}

	public void setProCyQ(Proceso proCyQ) {
		this.proCyQ = proCyQ;
	}

	public String getTelefonoOposicion() {
		return telefonoOposicion;
	}

	public void setTelefonoOposicion(String telefonoOposicion) {
		this.telefonoOposicion = telefonoOposicion;
	}

	public TipoProceso getTipoProceso() {
		return tipoProceso;
	}

	public void setTipoProceso(TipoProceso tipoProceso) {
		this.tipoProceso = tipoProceso;
	}

	public String getDesContribuyente() {
		return desContribuyente;
	}

	public void setDesContribuyente(String desContribuyente) {
		this.desContribuyente = desContribuyente;
	}

	public Abogado getAbogado() {
		return abogado;
	}

	public void setAbogado(Abogado abogado) {
		this.abogado = abogado;
	}

	public Procedimiento getProcedAnt() {
		return procedAnt;
	}

	public void setProcedAnt(Procedimiento procedAnt) {
		this.procedAnt = procedAnt;
	}
	
	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public MotivoBaja getMotivoBaja() {
		return motivoBaja;
	}

	public void setMotivoBaja(MotivoBaja motivoBaja) {
		this.motivoBaja = motivoBaja;
	}

	public String getObservacionBaja() {
		return observacionBaja;
	}

	public void setObservacionBaja(String observacionBaja) {
		this.observacionBaja = observacionBaja;
	}

	public String getNroSentenciaBaja() {
		return nroSentenciaBaja;
	}

	public void setNroSentenciaBaja(String nroSentenciaBaja) {
		this.nroSentenciaBaja = nroSentenciaBaja;
	}

	public MotivoResInf getMotivoResInf() {
		return motivoResInf;
	}

	public void setMotivoResInf(MotivoResInf motivoResInf) {
		this.motivoResInf = motivoResInf;
	}

	public Integer getRecursoRes() {
		return recursoRes;
	}

	public void setRecursoRes(Integer recursoRes) {
		this.recursoRes = recursoRes;
	}

	public String getNuevaCaratulaRes() {
		return nuevaCaratulaRes;
	}

	public void setNuevaCaratulaRes(String nuevaCaratulaRes) {
		this.nuevaCaratulaRes = nuevaCaratulaRes;
	}

	public String getCodExpJudRes() {
		return codExpJudRes;
	}

	public void setCodExpJudRes(String codExpJudRes) {
		this.codExpJudRes = codExpJudRes;
	}

	public Double getPrivGeneral() {
		return privGeneral;
	}

	public void setPrivGeneral(Double privGeneral) {
		this.privGeneral = privGeneral;
	}

	public Double getPrivEspecial() {
		return privEspecial;
	}

	public void setPrivEspecial(Double privEspecial) {
		this.privEspecial = privEspecial;
	}

	public Double getQuirografario() {
		return quirografario;
	}

	public void setQuirografario(Double quirografario) {
		this.quirografario = quirografario;
	}

	public List<HisEstProced> getListHisEstProced() {
		return listHisEstProced;
	}

	public void setListHisEstProced(List<HisEstProced> listHisEstProced) {
		this.listHisEstProced = listHisEstProced;
	}
	
	public Long getIdProcedAnt() {
		return idProcedAnt;
	}
	public void setIdProcedAnt(Long idProcedAnt) {
		this.idProcedAnt = idProcedAnt;
	}

	public List<OrdenControl> getListOrdenControl() {
		return listOrdenControl;
	}
	public void setListOrdenControl(List<OrdenControl> listOrdenControl) {
		this.listOrdenControl = listOrdenControl;
	}

	public List<ProCueNoDeu> getListProCueNoDeu() {
		return listProCueNoDeu;
	}

	public void setListProCueNoDeu(List<ProCueNoDeu> listProCueNoDeu) {
		this.listProCueNoDeu = listProCueNoDeu;
	}

	public Long getIdContribuyente() {
		return idContribuyente;
	}
	public void setIdContribuyente(Long idContribuyente) {
		this.idContribuyente = idContribuyente;
	}

	public Persona getContribuyente() {
		return contribuyente;
	}
	public void setContribuyente(Persona contribuyente) {
		this.contribuyente = contribuyente;
	}

	public List<ProDet> getListProDet() {
		return listProDet;
	}
	public void setListProDet(List<ProDet> listProDet) {
		this.listProDet = listProDet;
	}

	public boolean getSuperaMaxCantCuentas() {
		return superaMaxCantCuentas;
	}
	public void setSuperaMaxCantCuentas(boolean superaMaxCantCuentas) {
		this.superaMaxCantCuentas = superaMaxCantCuentas;
	}

	public List<DeudaPrivilegio> getListDeudaPrivilegio() {
		return listDeudaPrivilegio;
	}
	public void setListDeudaPrivilegio(List<DeudaPrivilegio> listDeudaPrivilegio) {
		this.listDeudaPrivilegio = listDeudaPrivilegio;
	}

	public List<PagoPriv> getListPagoPriv() {
		return listPagoPriv;
	}

	public void setListPagoPriv(List<PagoPriv> listPagoPriv) {
		this.listPagoPriv = listPagoPriv;
	}

	public List<Convenio> getListConvenios() {
		return listConvenios;
	}
	public void setListConvenios(List<Convenio> listConvenios) {
		this.listConvenios = listConvenios;
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio
		Procedimiento procedValid = Procedimiento.getByNumeroyAnio(this.getNumero(), this.getAnio());
		
		if (procedValid != null){
			addRecoverableValueError("En N\u00FAmero de procedimiento ya existe para el año actual.");
			return false;
		}	
		
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
	
	
	public boolean validateBaja() throws Exception {
		// limpiamos la lista de errores
		clearError();
		
		
		if(getFechaBaja()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.PROCEDIMIENTO_FECHABAJA);
		}
		
		if(getMotivoBaja()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.MOTIVOBAJA_LABEL);
		}
		
		
		
		if (hasError()) {
			return false;
		}

		return true;		
	}

	public boolean validateInforme() throws Exception {
		// limpiamos la lista de errores
		clearError();
		
		
		if(getFechaInfInd()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.PROCEDIMIENTO_FECHAINFIND);
		}
		
		if(getMotivoResInf()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.MOTIVORESINF_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return true;		
	}
	
	
	
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		if (CyqDAOFactory.getProcedimientoDAO().hasReferenceDeuda(this.getId())) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, CyqError.PROCEDIMIENTO_LABEL , GdeError.DEUDA_LABEL);
		}
		
		if (GenericDAO.hasReference(this, HisEstProced.class, "procedimiento")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, CyqError.PROCEDIMIENTO_LABEL , CyqError.HISESTPROCED_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones		
		if (getNumero()== null || getNumero().intValue()<=0) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.PROCEDIMIENTO_NUMERO);
		}
		
		if (getAnio()==null || getAnio().intValue()<=0){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.PROCEDIMIENTO_ANIO);
		}
		
		if(getFechaAlta()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.PROCEDIMIENTO_FECHAALTA);
		}
		
		if(getTipoProceso() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.TIPOPROCESO_LABEL);
		
		// Se valida fecha auto requerido para los tipos de proceso distintos de TFC.
		} else if (!getTipoProceso().getCodTipoProceso().trim().equals(TipoProceso.COD_TIPO_PROCESO_TFC)){
			
			if (getFechaAuto() == null){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.PROCEDIMIENTO_FECHAAUTO);
			}
		}
		
		if (getIdContribuyente() == null && StringUtil.isNullOrEmpty(getDesContribuyente())){
			addRecoverableValueError("Debe seleccionar un Contribuyente o ingresar una Descripci\u00F3n");
		}
		
		if (StringUtil.isNullOrEmpty(this.getDomicilio())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.PROCEDIMIENTO_DOMICILIO);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique

		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el Procedimiento. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		CyqDAOFactory.getProcedimientoDAO().update(this);
	}

	/**
	 * Desactiva el Procedimiento. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		CyqDAOFactory.getProcedimientoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Procedimiento
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Procedimiento
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	public HisEstProced createHisEstProced(HisEstProced hisEstProced) throws Exception {

		// Validaciones de negocio
		if (!hisEstProced.validateCreate()) {
			return hisEstProced;
		}

		CyqDAOFactory.getHisEstProcedDAO().update(hisEstProced);

		return hisEstProced;
	}
	
	public HisEstProced updateHisEstProced(HisEstProced hisEstProced) throws Exception {

		// Validaciones de negocio

		CyqDAOFactory.getHisEstProcedDAO().update(hisEstProced);

		return hisEstProced;
	}
	
	
	public ProCueNoDeu createProCueNoDeu(ProCueNoDeu proCueNoDeu) throws Exception {

		// Validaciones de negocio
		if (!proCueNoDeu.validateCreate()) {
			return proCueNoDeu;
		}

		CyqDAOFactory.getProCueNoDeuDAO().update(proCueNoDeu);

		return proCueNoDeu;
	}
	
	public ProDet createProDet(ProDet proDet) throws Exception {

		// Validaciones de negocio
		if (!proDet.validateCreate()) {
			return proDet;
		}

		CyqDAOFactory.getProDetDAO().update(proDet);

		return proDet;
	}
	
	
	/**
	 * Obtiene una lista de cuentas que posea el contribuyente, si este se encuentra seteado. 
	 * 
	 * Al utilizar este metodo realizar el "passErrorMessages" y transportar la bandera "superaMaxCantCuentas"
	 * 
	 * @return List CuentaVO
	 * @throws Exception
	 */
	public List<CuentaVO> obtenerListCuentasContribuyente() throws Exception {
		
		Contribuyente contribuyente = Contribuyente.getByIdNull(this.getIdContribuyente());
		
		List<CuentaVO> listCuentaVO = new ArrayList<CuentaVO>();
		
		if (contribuyente != null){
			
			listCuentaVO = (ArrayList<CuentaVO>) ListUtilBean.toVO(contribuyente.getListCuenta(),1);
			
			// seteo de banderas: por el momento sin permiso porque no tienen implementacion
			for (CuentaVO cuentaVO : listCuentaVO) {
				cuentaVO.setEstadoDeudaBussEnabled(Boolean.TRUE);
				cuentaVO.setLiquidacionDeudaBussEnabled(Boolean.TRUE);
			}

			// Seteamos bandera para que muestre el paginado
			if (contribuyente.getSuperaMaxCantCuentas()){
				this.setSuperaMaxCantCuentas(true);
				this.addMessageValue("El Contribuyente posee " + contribuyente.getCountCuentasContrib() + 
						 						  " cuentas, solo se muestran las primeras " + contribuyente.MAX_CUENTAS_BY_CONTRIB);
			}
		
		}
		
		return listCuentaVO;
	}
	
	/**
	 * Devuelve una lista de proDet para este procedimiento y via de la deuda sea Administrativa
	 * 
	 * @return List<ProDet>
	 */
	public List<ProDet> getListProDetDeudaAdmin(){
		ViaDeuda viaDeuda = ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN);
		return CyqDAOFactory.getProDetDAO().getListByProcedimientoViaDeuda(this, viaDeuda);
	}
	
	/**
	 * Devuelve una lista de proDet para este procedimiento y via de la deuda sea Judicial
	 * 
	 * @return List<ProDet>
	 */
	public List<ProDet> getListProDetDeudaJudicial(){
		ViaDeuda viaDeuda = ViaDeuda.getById(ViaDeuda.ID_VIA_JUDICIAL);
		return CyqDAOFactory.getProDetDAO().getListByProcedimientoViaDeuda(this, viaDeuda);
	}
	
	
	public DeudaPrivilegio createDeudaPrivilegio(DeudaPrivilegio deudaPrivilegio) throws Exception {
	
		// Validaciones de negocio
		if (!deudaPrivilegio.validateCreate()) {
			return deudaPrivilegio;
		}

		CyqDAOFactory.getDeudaPrivilegioDAO().update(deudaPrivilegio);

		return deudaPrivilegio;
	} 

	
	public PagoPriv createPagoPriv(PagoPriv pagoPriv) throws Exception {
		
		// Validaciones de negocio
		if (!pagoPriv.validateCreate()) {
			return pagoPriv;
		}

		CyqDAOFactory.getPagoPrivDAO().update(pagoPriv);

		return pagoPriv;
	}
	
	
	/**
	 * ToVo liviano con busqueda y seteo de datos del contribuyente.
	 * 
	 * @return ProcedimientoVO
	 * @throws Exception
	 */
	public ProcedimientoVO toVOWithPersona() throws Exception {
		
		ProcedimientoVO procedimientoVO = (ProcedimientoVO) this.toVO(1, false);
	 		
		Persona persona = Persona.getByIdNull(this.getIdContribuyente());
		
		if (persona != null){
			PersonaVO personaVO = (PersonaVO) persona.toVO(3);			
			procedimientoVO.setContribuyente(personaVO);
		}
		
 		return procedimientoVO;
	}
	
	
    /**
     * Devuelve la lista de convenios Vigentes que pueda tener el procedimiento.
     * 
     * @author Cristian
     * @return
     */
    public List<Convenio> getListConveniosVigentes(){
    	
    	List<Convenio> listConvenioRet = new ArrayList<Convenio>();
    	
    	for (Convenio convenio:getListConvenios()){
    		if (convenio.getEstadoConvenio().getId().equals(EstadoConvenio.ID_VIGENTE)){
    			listConvenioRet.add(convenio);	
    		}    		    		
    	}
    	
    	return listConvenioRet;
    }
    
    
    /**
     * Devuelve true o false se el Procedimiento es "Transferecia de Fondos de Comercio"
     * 
     * @return
     */
    public boolean esTFC(){
    	if (this.getTipoProceso().getCodTipoProceso().trim().equals(TipoProceso.COD_TIPO_PROCESO_TFC))
    		return true;
    	else
    		return false;    	
    }
    
    /**
     * Dado un idRecurso, recupera las cuentas para el recurso y el procedimiento actual. 
     * 
     * @param idRecurso
     * @return
     */
    public List<Cuenta> getListCuentaByIdRecurso(Long idRecurso) {
    	
    	return CyqDAOFactory.getProDetDAO().getListCuentaByIdRecurso(this.getId(), idRecurso); 
    }
}