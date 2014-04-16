//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.exe.buss.dao.ExeDAOFactory;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.rec.buss.bean.FormaPago;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.TipoEstadoCueExe;


/**
 * Bean correspondiente a EstadoCueExe
 * 
 * @author tecso
 */
@Entity
@Table(name = "exe_estadoCueExe")
public class EstadoCueExe extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final long ID_CREADA = 1L;
	public static final long ID_EN_ANALISIS = 2L;
	public static final long ID_MODIFICACION_DATOS = 3L;	
	public static final long ID_HA_LUGAR = 10L;
	public static final long ID_NO_HA_LUGAR = 11L;
	public static final long ID_REVOCADA = 12L;
	public static final long ID_PRORROGADA = 13L;
	public static final long ID_DESESTIMACION = 14L;
	public static final long ID_ENVIADO_SYNTIS = 20L;
	public static final long ID_ENVIADO_DIR_GRAL = 21L;
	public static final long ID_ENVIADO_CATASTRO = 22L;
	public static final long ID_REVOCACION_PARCIAL = 16L;

	public static final long ID_SOLICITUD_RECONSIDERACION = 30L;
	public static final long ID_SOLICITUD_APELACION = 31L;
	
	@Column(name = "desEstadoCueExe")
	private String desEstadoCueExe;
	
	@Column(name = "tipo")
	private String tipo;
	
	@Column(name = "transiciones")
	private String transiciones;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idArea") 
	private Area area;
	
	@Column(name = "exCodigos")
	private String exCodigos;
	
	@Column(name = "permiteModificar")
	private Integer permiteModificar;
	
	@Column(name = "esInicial")
	private Integer esInicial;
	
	@Column(name="idEstadoEnCueExe") 
	private Long idEstadoEnCueExe;
	
	@Column(name = "esresolucion")
	private Integer esResolucion;
	

	//<#Propiedades#>
	
	// Constructores
	public EstadoCueExe(){
		super();
		// Seteo de valores default			
	}
	
	public EstadoCueExe(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static EstadoCueExe getById(Long id) {
		return (EstadoCueExe) ExeDAOFactory.getEstadoCueExeDAO().getById(id);
	}
	
	public static EstadoCueExe getByIdNull(Long id) {
		return (EstadoCueExe) ExeDAOFactory.getEstadoCueExeDAO().getByIdNull(id);
	}
	
	public static List<EstadoCueExe> getList() {
		return (List<EstadoCueExe>) ExeDAOFactory.getEstadoCueExeDAO().getList();
	}
	
	public static List<EstadoCueExe> getListActivos() {			
		return (List<EstadoCueExe>) ExeDAOFactory.getEstadoCueExeDAO().getListActiva();
	}

	public static List<EstadoCueExe> getListEstados() throws Exception {			
		return (List<EstadoCueExe>) ExeDAOFactory.getEstadoCueExeDAO().getListEstados();
	}
	
	public static List<EstadoCueExe> getListEstadosIniciales() throws Exception {			
		return (List<EstadoCueExe>) ExeDAOFactory.getEstadoCueExeDAO().getListEstadosIniciales();
	}
	
	public static List<EstadoCueExe> getListAcciones() throws Exception {			
		return (List<EstadoCueExe>) ExeDAOFactory.getEstadoCueExeDAO().getListAcciones();
	}
	
	public static List<EstadoCueExe> getListSolicitudes() throws Exception {			
		return (List<EstadoCueExe>) ExeDAOFactory.getEstadoCueExeDAO().getListSolicitudes();
	}
	
	
	public static List<EstadoCueExe> getListTransicionesForEstado(EstadoCueExe estadoCueExe) throws Exception {			
		return (List<EstadoCueExe>) ExeDAOFactory.getEstadoCueExeDAO().getListTransicionesForEstado(estadoCueExe);
	}
	
	
	// Getters y setters
	public String getDesEstadoCueExe() {
		return desEstadoCueExe;
	}

	public void setDesEstadoCueExe(String desEstadoCueExe) {
		this.desEstadoCueExe = desEstadoCueExe;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTransiciones() {
		return transiciones;
	}

	public void setTransiciones(String transiciones) {
		this.transiciones = transiciones;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getExCodigos() {
		return exCodigos;
	}

	public void setExCodigos(String exCodigos) {
		this.exCodigos = exCodigos;
	}

	public Integer getPermiteModificar() {
		return permiteModificar;
	}

	public void setPermiteModificar(Integer permiteModificar) {
		this.permiteModificar = permiteModificar;
	}

	public Integer getEsInicial() {
		return esInicial;
	}

	public void setEsInicial(Integer esInicial) {
		this.esInicial = esInicial;
	}

	public Long getIdEstadoEnCueExe() {
		return idEstadoEnCueExe;
	}

	public void setIdEstadoEnCueExe(Long idEstadoEnCueExe) {
		this.idEstadoEnCueExe = idEstadoEnCueExe;
	}

	public Integer getEsResolucion() {
		return esResolucion;
	}

	public void setEsResolucion(Integer esResolucion) {
		this.esResolucion = esResolucion;
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
		
		// CDM: historia de Estado de Cuenta/Exencion
       /*if (GenericDAO.hasReference(this, HisEstCueExe.class, "exencion")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							ExeError.EXENCION_LABEL, ExeError.HISESTCUEEXE_LABEL);
		}
		
		// CDM: historia de Estado de Cuenta/Exencion
		if (GenericDAO.hasReference(this, CueExe.class, "exencion")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							ExeError.EXENCION_LABEL, ExeError.CUEEXE_LABEL);
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones     
		
		
	if (getEsValido(getTipo())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.ESTADOCUEEXE_TIPO);
		}

		if (getEsResolucion()== null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.ESTADOCUEEXE_ESRESOLUCION );
		}
		
		if (StringUtil.isNullOrEmpty(getDesEstadoCueExe())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.ESTADOCUEEXE_DES );
		}
			
		return true;
	}

	/**
	 * Activa el Exencion. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		ExeDAOFactory.getEstadoCueExeDAO().update(this);
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
		ExeDAOFactory.getEstadoCueExeDAO().update(this);
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
	
	public static boolean getEsValido(String tipo){

		return (TipoEstadoCueExe.SELECCIONAR == TipoEstadoCueExe.SELECCIONAR.getByCod(tipo)||TipoEstadoCueExe.NULO == TipoEstadoCueExe.NULO.getByCod(tipo));
	}
	
}
