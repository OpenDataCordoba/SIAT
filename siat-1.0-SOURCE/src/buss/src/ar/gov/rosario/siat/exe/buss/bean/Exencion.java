//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.exe.buss.dao.ExeDAOFactory;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.rec.buss.bean.FormaPago;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a Exencion
 * 
 * @author tecso
 */
@Entity
@Table(name = "exe_exencion")
public class Exencion extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	public static final String COD_EXENCION_QUITA_SOBRETASA = "1";
	public static final String COD_EXENCION_CONDONACION = "2";
	public static final String COD_EXENCION_LIBERACION = "3";
	public static final String COD_EXENCION_CASO_SOCIAL = "4";
	public static final String COD_EXENCION_EXENTO_TOTAL = "5";
	public static final String COD_EXENCION_EXENTO_5_MINIMOS = "6";
	public static final String COD_EXENCION_EXENTO_50_PORCIENTO = "7";
	public static final String COD_EXENCION_EXENTO_25_PORCIENTO = "30";
	public static final String COD_EXENCION_PREDIO_SUJETO_EXPROPIACION = "27";
	public static final String COD_EXENCION_QUITA_SOBRETASA_INMUEBLES = "28";
	public static final String COD_EXENCION_QUITA_SOBRETASA_NU = "31";

	public static final Long ID_EXENCION_QUITA_SOBRETASA = 1L;
	public static final Long ID_EXENCION_CONDONACION = 2L;
	public static final Long ID_EXENCION_LIBERACION = 3L;
	public static final Long ID_EXENCION_CASO_SOCIAL = 4L;
	public static final Long ID_EXENCION_EXENTO_TOTAL = 5L;
	public static final Long ID_EXENCION_EXENTO_5_MINIMOS = 6L;
	public static final Long ID_EXENCION_EXENTO_50_PORCIENTO = 7L;
	public static final Long ID_EXENCION_EXENTO_25_PORCIENTO = 30L;
	public static final Long ID_EXENCION_PREDIO_SUJETO_EXPROPIACION = 27L;
	public static final Long ID_EXENCION_QUITA_SOBRETASA_INMUEBLES = 28L;
	public static final Long ID_EXENCION_QUITA_SOBRETASA_NU = 31L;
	
	@Column(name = "codExencion")
	private String codExencion;
	
	@Column(name = "desExencion")
	private String desExencion;
	
	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="idRecurso") 
	private Recurso recurso;
	
    @Column(name="idCaso") 
	private String idCaso;

	@Column(name="montoMinimo")
	private Double montoMinimo;
	
	@Column(name="aplicaMinimo")
	private Integer aplicaMinimo;
	
	@Column(name="afectaEmision")
	private Integer afectaEmision;
	
	@Column(name="actualizaDeuda")
	private Integer actualizaDeuda;
	
	@Column(name="enviaJudicial")
	private Integer enviaJudicial;
	
	@Column(name="enviaCyQ")
	private Integer enviaCyQ;
	
	@Column(name="esParcial")
	private Integer esParcial;
		
	@Column(name="permiteManPad")
	private Integer permiteManPad; // Permite Mantenimiento Padron
	
	@OneToMany()
	@JoinColumn(name="idExencion")
	private List<ExeRecCon> listExeRecCon; // Es la lista de Conceptos del Recurso
	
	
	// Constructores
	public Exencion(){
		super();		
	}
	
	public Exencion(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static Exencion getById(Long id) {
		return (Exencion) ExeDAOFactory.getExencionDAO().getById(id);
	}

	public static Exencion getByIdNull(Long id) {
		return (Exencion) ExeDAOFactory.getExencionDAO().getByIdNull(id);
	}

	public static List<Exencion> getList() {
		return (ArrayList<Exencion>) ExeDAOFactory.getExencionDAO().getList();
	}
	
	public static List<Exencion> getListActivos() {
		return (ArrayList<Exencion>) ExeDAOFactory.getExencionDAO().getListActiva();
	}

	public static Exencion getByCogido(String codExencion) throws Exception {
		return (Exencion) ExeDAOFactory.getExencionDAO().getByCodigo(codExencion);
	}
	
	public static List<Exencion> getListActivosByIdRecurso(Long id) {
		return (List<Exencion>) ExeDAOFactory.getExencionDAO().getListActivosByIdRecurso(id);
	}
	
	public static List<Exencion> getListActivosByIdRecursoPerManPad(Long id, boolean permiteManPad) {
		return (List<Exencion>) ExeDAOFactory.getExencionDAO().getListActivosByIdRecursoPerManPad(id, permiteManPad);
	}
	
	public static List<Exencion> getListActivosByPlan (Plan plan){
		return (List<Exencion>) ExeDAOFactory.getExencionDAO().getListActivosByPlan(plan);
	}
	
	/**
	 * Obtiene la lista de Excenciones que permiten el envio a Judicial
	 * @return List<Exencion>
	 */
	public static List<Exencion> getListPermitenEnvioJudicial() {
		return (List<Exencion>) ExeDAOFactory.getExencionDAO().getListPermitenEnvioJudicial();
	}
	
	// Getters y setters
	public String getCodExencion() {
		return codExencion;
	}

	public void setCodExencion(String codExencion) {
		this.codExencion = codExencion;
	}

	public String getDesExencion() {
		return desExencion;
	}

	public void setDesExencion(String desExencion) {
		this.desExencion = desExencion;
	}
	
	
	public Integer getActualizaDeuda() {
		return actualizaDeuda;
	}

	public void setActualizaDeuda(Integer actualizaDeuda) {
		this.actualizaDeuda = actualizaDeuda;
	}

	public Integer getAfectaEmision() {
		return afectaEmision;
	}

	public void setAfectaEmision(Integer afectaEmision) {
		this.afectaEmision = afectaEmision;
	}

	public Integer getAplicaMinimo() {
		return aplicaMinimo;
	}

	public void setAplicaMinimo(Integer aplicaMinimo) {
		this.aplicaMinimo = aplicaMinimo;
	}

	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public Integer getEnviaCyQ() {
		return enviaCyQ;
	}

	public void setEnviaCyQ(Integer enviaCyQ) {
		this.enviaCyQ = enviaCyQ;
	}

	public Integer getEnviaJudicial() {
		return enviaJudicial;
	}

	public void setEnviaJudicial(Integer enviaJudicial) {
		this.enviaJudicial = enviaJudicial;
	}

	public Double getMontoMinimo() {
		return montoMinimo;
	}

	public void setMontoMinimo(Double montoMinimo) {
		this.montoMinimo = montoMinimo;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}
	
	public List<ExeRecCon> getListExeRecCon() {
		return listExeRecCon;
	}

	public void setListExeRecCon(List<ExeRecCon> listExeRecCon) {
		this.listExeRecCon = listExeRecCon;
	}

	public Integer getEsParcial() {
		return esParcial;
	}

	public void setEsParcial(Integer esParcial) {
		this.esParcial = esParcial;
	}

	public Integer getPermiteManPad() {
		return permiteManPad;
	}

	public void setPermiteManPad(Integer permiteManPad) {
		this.permiteManPad = permiteManPad;
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
		
		// Conceptos
		if (GenericDAO.hasReference(this, ExeRecCon.class, "exencion")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							ExeError.EXENCION_LABEL, ExeError.EXERECCON_LABEL );
		}
		
		
		// Sujeto Exento
		if (GenericDAO.hasReference(this, ContribExe.class, "exencion")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							ExeError.EXENCION_LABEL, ExeError.CONTRIBEXE_LABEL);
		}
		
		// Cuenta Exencion
		if (GenericDAO.hasReference(this, CueExe.class, "exencion")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							ExeError.EXENCION_LABEL, ExeError.CUEEXE_REF);
		}
		
		// CDM: Forma de Pago
		if (GenericDAO.hasReference(this, FormaPago.class, "exencion")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							ExeError.EXENCION_LABEL, RecError.FORMAPAGO_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (getRecurso() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
		}

		if (StringUtil.isNullOrEmpty(getCodExencion())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.EXENCION_CODEXENCION );
		}
		
		if (StringUtil.isNullOrEmpty(getDesExencion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.EXENCION_DESEXENCION);
		}
		
		if (getActualizaDeuda() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.EXENCION_ACTUALIZADEUDA);
		}
		
		if (getEnviaJudicial() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.EXENCION_ENVIAJUDICIAL);
		}
		
		if (getEnviaCyQ() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.EXENCION_ENVIACYQ);
		}
		
		if (getAfectaEmision() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.EXENCION_AFECTAEMISION);
		}
		
		if (getEsParcial() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.EXENCION_ESPARCIAL);
		}
		
		if (getPermiteManPad() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.EXENCION_PERMITEMANPAD);
		}
		
		if (getAplicaMinimo()!= null && SiNo.getById(getAplicaMinimo())==SiNo.SI){
			if (getMontoMinimo() == null || getMontoMinimo().intValue()<0){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.EXENCION_MONTOMINIMO);
			}			
		}

		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codExencion");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, ExeError.EXENCION_CODEXENCION);			
		}
		
		return true;
	}
	
	// Metodos de negocio
	//	---> ABM ExeRecCon
	public ExeRecCon createExeRecCon(ExeRecCon exeRecCon) throws Exception {

		// Validaciones de negocio
		if (!exeRecCon.validateCreate()) {
			return exeRecCon;
		}

		ExeDAOFactory.getExeRecConDAO().update(exeRecCon);

		return exeRecCon;
	}
	
	public ExeRecCon updateExeRecCon(ExeRecCon exeRecCon) throws Exception {
		
		// Validaciones de negocio
		if (!exeRecCon.validateUpdate()) {
			return exeRecCon;
		}

		ExeDAOFactory.getExeRecConDAO().update(exeRecCon);
		
		return exeRecCon;
	}
	
	public ExeRecCon deleteExeRecCon(ExeRecCon exeRecCon) throws Exception {
	
		// Validaciones de negocio
		if (!exeRecCon.validateDelete()) {
			return exeRecCon;
		}
		
		ExeDAOFactory.getExeRecConDAO().delete(exeRecCon);
		
		return exeRecCon;
	}
	// <--- ABM ExeRecCon
		
	/**
	 * Activa el Exencion. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		ExeDAOFactory.getExencionDAO().update(this);
	}

	/**
	 * Desactiva el Exencion. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		ExeDAOFactory.getExencionDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Exencion
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Exencion
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	public boolean esCondonacion() {
		boolean esCondonacion = false; 
		if (this.getCodExencion().equals(Exencion.COD_EXENCION_CONDONACION)) {
			esCondonacion = true;
		}
		
		return esCondonacion;
	}
	
	public boolean esLiberacion() {
		boolean esLiberacion = false; 
		if (this.getCodExencion().equals(Exencion.COD_EXENCION_LIBERACION)) {
			esLiberacion = true;
		}
		
		return esLiberacion;
	}
	
	public boolean esCasoSocial() {
		boolean esCasoSocial = false; 
		if (this.getCodExencion().equals(Exencion.COD_EXENCION_CASO_SOCIAL)) {
			esCasoSocial = true;
		}
		
		return esCasoSocial;
	}
	
	public boolean esJubilado(){
		boolean esJubilado = false; 
		if (this.getCodExencion().equals(Exencion.COD_EXENCION_EXENTO_50_PORCIENTO) ||
				this.getCodExencion().equals(Exencion.COD_EXENCION_EXENTO_5_MINIMOS)) {
			esJubilado = true;
		}
		
		return esJubilado;
		
	} 
		
	public boolean esExentoTotal() {
		boolean esExentoTotal = false; 
		if (this.getCodExencion().equals(Exencion.COD_EXENCION_EXENTO_TOTAL)) {
			esExentoTotal = true;
		}
		
		return esExentoTotal;
	}
	
	/**
	 * Hace toVO(0, false) y setea las listas y el recurso en null
	 * @return
	 * @throws Exception
	 */
	public ExencionVO toVOLightForPDF() throws Exception{
		ExencionVO exencionVO = (ExencionVO) this.toVO(0, false);
		
		exencionVO.setListExeRecCon(null);
		exencionVO.setRecurso(null);
		
		return exencionVO;
	}


	@Override
	public String infoString() {
		String ret =" Exencion: ";
		
		if(codExencion!=null){
			ret +=" - Codigo: "+codExencion;
		}
		
		if(desExencion!=null){
			ret +=" - Descripcion: "+desExencion;
		}

		if(recurso!=null){
			ret +=" Recurso: "+recurso.getDesRecurso();
		}
		
		if(idCaso!=null){
			ret +=" - Para el Caso: "+idCaso;
		}
		
		return ret;
	}
}


