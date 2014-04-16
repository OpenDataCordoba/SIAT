//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.dao.CasDAOFactory;
import ar.gov.rosario.siat.cas.iface.model.EstSolicitudVO;
import ar.gov.rosario.siat.cas.iface.model.SolicitudVO;
import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudVO;
import ar.gov.rosario.siat.cas.iface.util.CasError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Solicitud
 * 
 * @author tecso
 */
@Entity
@Table(name = "cas_solicitud")
public class Solicitud extends BaseBO {
	
	private static final String LOG_CREADA = "Creada";

	private static final String LOG_CAMBIO_ESTADO = "Cambio estado";

	private static final long serialVersionUID = 1L;
	
	@Column(name = "fechaalta")
	private Date fechaAlta;

	@ManyToOne(optional=true) 
    @JoinColumn(name="idareaOrigen") 
	private Area areaOrigen;
	
	@ManyToOne(optional=false) 
    @JoinColumn(name="idareaDestino") 
	private Area areaDestino;

	@Column(name = "usuarioalta")	
	private String usuarioAlta;
	
	@ManyToOne(optional=false) 
    @JoinColumn(name="idtiposolicitud") 
	private TipoSolicitud tipoSolicitud;
	
	@Column(name = "asuntosolicitud")
	private String asuntoSolicitud;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idrecurso") 
	private Recurso recurso;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idcuenta") 
	private Cuenta cuenta;
	
    @Column(name="idCaso") 
	private String idCaso;

	@Column(name = "fechacamest")
	private Date fechaCamEst;

	@Column(name = "descripcion")
	private String descripcion;
	
	@ManyToOne(optional=false) 
    @JoinColumn(name="idestsolicitud") 
	private EstSolicitud estSolicitud;

	@Column(name = "obsestsolicitud")
	private String obsestsolicitud;

	@Column(name = "logsolicitud")
	private String logsolicitud;
	
	// Constructores
	public Solicitud(){
		super();
		this.setFechaAlta(new Date());
	}

	public void setInitValues() throws Exception {
		Long idUsuarioSIAT = DemodaUtil.currentUserContext().getIdUsuarioSiat();
		UsuarioSiat usuarioSiat = UsuarioSiat.getByIdNull(idUsuarioSIAT);
		
		if (usuarioSiat != null) { 
			this.setUsuarioAlta(usuarioSiat.getUsuarioSIAT());
			this.setAreaOrigen(usuarioSiat.getArea());
		} else {
			//Los hilos creados por ADP, no posee informacion de usuarioSiat, 
			//en estos casos ponemos como que las creo el user siat, con area reservada siat.
			//Esto se decidio hacerlo asi, para los procesos no genern solicitudes en nombre de otro,
			//ya que podria causar conflictos entre humanos.
			this.setUsuarioAlta("siat");
			this.setAreaOrigen(Area.getByCodigo(Area.COD_AREA_DEFAULT_SIAT));
		}

		EstSolicitud estSolicitud = EstSolicitud.getById(EstSolicitud.ID_PENDIENTE); 
		this.setEstSolicitud(estSolicitud);
	}

	public Solicitud(TipoSolicitud tipoSolicitud) throws Exception { 
		this();
		this.setInitValues();
		if (tipoSolicitud != null) {
			this.setTipoSolicitud(tipoSolicitud);
			this.setAreaDestino(tipoSolicitud.getAreaDestino());
		}
	}
	
	public Solicitud(TipoSolicitud tipoSolicitud, String asunto) throws Exception{
		this(tipoSolicitud);
		this.setAsuntoSolicitud(asunto);
	}

	public Solicitud(TipoSolicitud tipoSolicitud, String asunto, String descripcion) throws Exception {
		this(tipoSolicitud, asunto);
		this.setDescripcion(descripcion);
	}
	
	public Solicitud(TipoSolicitud tipoSolicitud, String asunto, String descripcion, Cuenta cuenta) throws Exception {
		this(tipoSolicitud, asunto, descripcion);
		this.setCuenta(cuenta);
	}
	
	
	public Solicitud(TipoSolicitud tipoSolicitud, String asunto, String descripcion, Area areaDestino) throws Exception {
		this();
		this.setInitValues();
		this.setAsuntoSolicitud(asunto);
		this.setDescripcion(descripcion);
		
		if (areaDestino != null){
			this.setAreaDestino(areaDestino);
		}
		
		if (tipoSolicitud != null) {
			this.setTipoSolicitud(tipoSolicitud);
			
			if(areaDestino == null){
				this.setAreaDestino(tipoSolicitud.getAreaDestino());
			} 
		} 
		
	}
	
	
	// Metodos de Clase
	public static Solicitud getById(Long id) {
		return (Solicitud) CasDAOFactory.getSolicitudDAO().getById(id);
	}
	
	public static Solicitud getByIdNull(Long id) {
		return (Solicitud) CasDAOFactory.getSolicitudDAO().getByIdNull(id);
	}
	
	public static List<Solicitud> getList() {
		return (ArrayList<Solicitud>) CasDAOFactory.getSolicitudDAO().getList();
	}
	
	public static List<Solicitud> getListActivos() {			
		return (ArrayList<Solicitud>) CasDAOFactory.getSolicitudDAO().getListActiva();
	}

	/**
	 * Verifica si existen solicitudes pendientes para el area pasada como parametro
	 * @param idArea
	 * @return
	 * @throws Exception 
	 * @author arobledo
	 */
	public static boolean tienePendientesArea(Long idArea) throws Exception{
		return CasDAOFactory.getSolicitudDAO().tienePendientesArea(idArea);
	}

		
	// Getters y setters
	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Area getAreaOrigen() {
		return areaOrigen;
	}

	public void setAreaOrigen(Area areaOrigen) {
		this.areaOrigen = areaOrigen;
	}

	public String getUsuarioAlta() {
		return usuarioAlta;
	}

	public void setUsuarioAlta(String usuarioAlta) {
		this.usuarioAlta = usuarioAlta;
	}

	public TipoSolicitud getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(TipoSolicitud tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public String getAsuntoSolicitud() {
		return asuntoSolicitud;
	}

	public void setAsuntoSolicitud(String asuntoSolicitud) {
		this.asuntoSolicitud = asuntoSolicitud;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public Date getFechaCamEst() {
		return fechaCamEst;
	}

	public void setFechaCamEst(Date fechaCamEst) {
		this.fechaCamEst = fechaCamEst;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public EstSolicitud getEstSolicitud() {
		return estSolicitud;
	}

	public void setEstSolicitud(EstSolicitud estSolicitud) {
		this.estSolicitud = estSolicitud;
	}

	public String getObsestsolicitud() {
		return obsestsolicitud;
	}

	public void setObsestsolicitud(String obsestsolicitud) {
		this.obsestsolicitud = obsestsolicitud;
	}

	public String getLogsolicitud() {
		return logsolicitud;
	}

	public void setLogsolicitud(String logsolicitud) {
		this.logsolicitud = logsolicitud;
	}
	
	public Area getAreaDestino() {
		return areaDestino;
	}

	public void setAreaDestino(Area areaDestino) {
		this.areaDestino = areaDestino;
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
	
		/*Ejemplo:
		if (GenericDAO.hasReference(this, ${BeanRelacionado}.class, "${bean}")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							CasError.${BEAN}_LABEL, CasError.${BEAN_RELACIONADO}_LABEL );
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	public boolean validateCambiarEstado() throws Exception {
		//limpiamos la lista de errores
		clearError();
	
		if (!this.validate()) {
			return false;
		}
		
		if (StringUtil.isNullOrEmpty(getObsestsolicitud())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.OBS_EST_SOLICITUD_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones
		if (getTipoSolicitud() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.TIPOSOLICITUD_LABEL);
		}

		if (getFechaAlta() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.SOLICITUD_FECHAALTA);
		}

		// TODO: ver si va requerido el usuario
		/*
		if (StringUtil.isNullOrEmpty(getUsuarioAlta())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.SOLICITUD_USUARIOALTA);
		}
		*/

		if (StringUtil.isNullOrEmpty(getAsuntoSolicitud())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.SOLICITUD_ASUNTOSOLICITUD);
		}

		if (StringUtil.isNullOrEmpty(getDescripcion())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.SOLICITUD_DESCRIPCION);
		}

		if (getEstSolicitud() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CasError.ESTSOLICITUD_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	// Metodos de negocio
	
	public void addLogCreateSolicitud(String usuario) throws Exception{
		addLog(LOG_CREADA, usuario);
	}
	
	public void addLogCambiarEstadoSolicitud(String usuario) throws Exception{
		addLog(LOG_CAMBIO_ESTADO, usuario);
	}
	
	private void addLog(String strInicial, String usuario) throws Exception{
		if(getEstSolicitud()!=null){
			String fecha = DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK);
			String estSolicitud = getEstSolicitud().getDescripcion();
			String log = strInicial+" - "+estSolicitud+" - "+fecha+" - "+usuario; 
			if(StringUtil.isNullOrEmpty(logsolicitud)){
				logsolicitud =log;
			}else{
				logsolicitud +="<br>"+log;
			}
		}
	}
	/**
	 * Activa el Solicitud. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		CasDAOFactory.getSolicitudDAO().update(this);
	}

	/**
	 * Desactiva el Solicitud. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		CasDAOFactory.getSolicitudDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Solicitud
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Solicitud
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	
	public SolicitudVO toVOForView() throws Exception {
		SolicitudVO solicitudVO = (SolicitudVO) this.toVO();
		// Area Origen 
		if (this.getAreaOrigen() != null){
			solicitudVO.setAreaOrigen((AreaVO) this.getAreaOrigen().toVO());		
		}
		// Area Destino
		if (this.getAreaDestino() != null){
			solicitudVO.setAreaDestino((AreaVO) this.getAreaDestino().toVO());
		}
		// TipoSolicitud
		if (this.getTipoSolicitud() != null){
			solicitudVO.setTipoSolicitud((TipoSolicitudVO) this.getTipoSolicitud().toVO());
		}
		// Estado Solicitud
		if (this.getEstSolicitud() != null){
			solicitudVO.setEstSolicitud((EstSolicitudVO) this.getEstSolicitud().toVO());
		}
		// Recurso
		if (this.getRecurso() != null){
			solicitudVO.setRecurso((RecursoVO) this.getRecurso().toVOWithCategoria());
		}
		// Cuenta
		if (this.getCuenta() != null){
			solicitudVO.setCuenta((CuentaVO) this.getCuenta().toVO(false));
		}
		
		return solicitudVO;
	}

	@Override
	public String infoString() {
		String ret =" Solicitud";
		
		if(fechaAlta!=null){
			ret +=" - con Fecha de Alta: "+DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_HH_MM_MASK);
		}

		if(tipoSolicitud!=null){
			ret +=" - de Tipo Solicitud: "+tipoSolicitud.getDescripcion();
		}
		
		if(areaOrigen!=null){
			ret +=" - Area Origen: "+areaOrigen.getDesArea();
		}
		
		if(areaDestino!=null){
			ret +=" - Area Destino: "+areaDestino.getDesArea();
		}
		
		if(usuarioAlta!=null){
			ret +=" - generada por el usuario: "+usuarioAlta;
		}

		if(asuntoSolicitud!=null){
			ret +=" - Asunto: "+ asuntoSolicitud;
		}
		
		if(cuenta!=null){
			ret +=" - para la Cuenta: "+cuenta.getNumeroCuenta();
		}
		
		if(recurso!=null){
			ret +=" - Recurso:"+recurso.getDesRecurso();
		}
		
		if(idCaso!=null){
			ret +=" - para el Caso: "+idCaso;
		}
		
		if(descripcion!=null){
			ret +=" - Descripcion: "+descripcion;
		}
		
		if(estSolicitud!=null){
			ret +=" - Estado: "+estSolicitud.getDescripcion();
		}

		if(fechaCamEst!=null){
			ret +=" - fecha cambio de estado: "+ DateUtil.formatDate(fechaCamEst , DateUtil.ddSMMSYYYY_MASK);
		}
		
		return ret;
	}
}	

