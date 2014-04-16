//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.buss.bean;

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

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.esp.buss.dao.EspDAOFactory;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean corresponiente a la Habilitacion de Espectaculos.
 * 
 * @author tecso
 */
@Entity
@Table(name = "esp_habilitacion")
public class Habilitacion extends BaseBO {

	private static Logger log = Logger.getLogger(Habilitacion.class);
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "numero")
	private Integer numero;
	
	@Column(name = "anio")
	private Integer anio;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idRecurso") 
	private Recurso recurso;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idCuenta") 
	private Cuenta cuenta;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idValoresCargados") 
	private ValoresCargados valoresCargados;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoHab") 
	private TipoHab tipoHab;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoCobro") 
	private TipoCobro tipoCobro;
	
	@Column(name = "fechaHab")
	private Date fechaHab;
	
	@Column(name = "fecEveDes")
	private Date fecEveDes;
	
	@Column(name = "fecEveHas")
	private Date fecEveHas;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idLugarEvento") 
	private LugarEvento lugarEvento;

	@Column(name = "factorOcupacional")
	private Long factorOcupacional;

	@Column(name = "lugarEvento")
	private String lugarEventoStr;
	
	@Column(name = "horaAcceso")
	private Date horaAcceso;
	
	@Column(name = "claHab")
	private Integer claHab;

	@Column(name = "claOrg")
	private Integer claOrg;

	@Column(name = "cantFunciones")
	private Integer cantFunciones;

	@Column(name = "idDeudaInicial")
	private Long idDeudaInicial;
	
	@Transient
	private Persona perHab;
	
	@Column(name="idPerHab")
	private Long idPerHab;
	
	// TODO ver si es necesario, o buscamos el cuit de la persona que formalizó y listo.
	@Column(name = "cuit")
	private String cuit;
	
	@Column(name = "observaciones")
	private String observaciones;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idEstHab") 
	private EstHab estHab;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idTipoEvento")
	private TipoEvento tipoEvento;
	
	// Listas de Entidades Relacionadas con Habilitacion
	@OneToMany()
	@JoinColumn(name="idHabilitacion")
	private List<PrecioEvento> listPrecioEvento;

	@OneToMany()
	@JoinColumn(name="idHabilitacion")
	private List<EntHab> listEntHab;

	@OneToMany()
	@JoinColumn(name="idHabilitacion")
	private List<EntVen> listEntVen;
	
	@OneToMany()
	@JoinColumn(name="idHabilitacion")
	private List<HisEstHab> listHisEstHab;

	@OneToMany()
	@JoinColumn(name="idHabilitacion")
	private List<HabExe> listHabExe;

	//Constructores 
	public Habilitacion(){
		super();
	}
	
	// Getters Y Setters
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public Cuenta getCuenta() {
		return cuenta;
	}
	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	public String getCuit() {
		return cuit;
	}
	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public EstHab getEstHab() {
		return estHab;
	}
	public void setEstHab(EstHab estHab) {
		this.estHab = estHab;
	}
	public Date getFecEveDes() {
		return fecEveDes;
	}
	public void setFecEveDes(Date fecEveDes) {
		this.fecEveDes = fecEveDes;
	}
	public Date getFecEveHas() {
		return fecEveHas;
	}
	public void setFecEveHas(Date fecEveHas) {
		this.fecEveHas = fecEveHas;
	}
	public Date getFechaHab() {
		return fechaHab;
	}
	public void setFechaHab(Date fechaHab) {
		this.fechaHab = fechaHab;
	}
	public Date getHoraAcceso() {
		return horaAcceso;
	}
	public void setHoraAcceso(Date horaAcceso) {
		this.horaAcceso = horaAcceso;
	}
	public Long getIdPerHab() {
		return idPerHab;
	}
	public void setIdPerHab(Long idPerHab) {
		this.idPerHab = idPerHab;
	}
	public String getLugarEventoStr() {
		return lugarEventoStr;
	}
	public void setLugarEventoStr(String lugarEventoStr) {
		this.lugarEventoStr = lugarEventoStr;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public Persona getPerHab() {
		return perHab;
	}
	public void setPerHab(Persona perHab) {
		this.perHab = perHab;
	}
	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}
	public TipoCobro getTipoCobro() {
		return tipoCobro;
	}
	public void setTipoCobro(TipoCobro tipoCobro) {
		this.tipoCobro = tipoCobro;
	}
	public TipoHab getTipoHab() {
		return tipoHab;
	}
	public void setTipoHab(TipoHab tipoHab) {
		this.tipoHab = tipoHab;
	}
	public ValoresCargados getValoresCargados() {
		return valoresCargados;
	}
	public void setValoresCargados(ValoresCargados valoresCargados) {
		this.valoresCargados = valoresCargados;
	}
	public List<EntHab> getListEntHab() {
		return listEntHab;
	}
	public void setListEntHab(List<EntHab> listEntHab) {
		this.listEntHab = listEntHab;
	}
	public List<EntVen> getListEntVen() {
		return listEntVen;
	}
	public void setListEntVen(List<EntVen> listEntVen) {
		this.listEntVen = listEntVen;
	}
	public List<HisEstHab> getListHisEstHab() {
		return listHisEstHab;
	}
	public void setListHisEstHab(List<HisEstHab> listHisEstHab) {
		this.listHisEstHab = listHisEstHab;
	}
	public List<PrecioEvento> getListPrecioEvento() {
		return listPrecioEvento;
	}
	public void setListPrecioEvento(List<PrecioEvento> listPrecioEvento) {
		this.listPrecioEvento = listPrecioEvento;
	}
	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}
	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	public List<HabExe> getListHabExe() {
		return listHabExe;
	}
	public void setListHabExe(List<HabExe> listHabExe) {
		this.listHabExe = listHabExe;
	}
	public Integer getClaHab() {
		return claHab;
	}
	public void setClaHab(Integer claHab) {
		this.claHab = claHab;
	}
	public Integer getClaOrg() {
		return claOrg;
	}
	public void setClaOrg(Integer claOrg) {
		this.claOrg = claOrg;
	}
	public LugarEvento getLugarEvento() {
		return lugarEvento;
	}
	public void setLugarEvento(LugarEvento lugarEvento) {
		this.lugarEvento = lugarEvento;
	}
	public Long getFactorOcupacional() {
		return factorOcupacional;
	}
	public void setFactorOcupacional(Long factorOcupacional) {
		this.factorOcupacional = factorOcupacional;
	}
	public Integer getCantFunciones() {
		return cantFunciones;
	}
	public void setCantFunciones(Integer cantFunciones) {
		this.cantFunciones = cantFunciones;
	}
	public Long getIdDeudaInicial() {
		return idDeudaInicial;
	}
	public void setIdDeudaInicial(Long idDeudaInicial) {
		this.idDeudaInicial = idDeudaInicial;
	}

	// Metodos de clase	
	public static Habilitacion getById(Long id) {
		return (Habilitacion) EspDAOFactory.getHabilitacionDAO().getById(id);
	}
	
	public static Habilitacion getByIdNull(Long id) {
		return (Habilitacion) EspDAOFactory.getHabilitacionDAO().getByIdNull(id);
	}
	
	public static List<Habilitacion> getList() {
		return (ArrayList<Habilitacion>) EspDAOFactory.getHabilitacionDAO().getList();
	}
	
	public static List<Habilitacion> getListActivos() {			
		return (ArrayList<Habilitacion>) EspDAOFactory.getHabilitacionDAO().getListActiva();
	}
	
	public static Integer getNextNumero(Integer anio, Long idTipoHab) throws Exception {
		return EspDAOFactory.getHabilitacionDAO().getNextNumero(anio, idTipoHab);
	}
	
	/**
	 *  Obtener Habilitacion para el Numero, Anio y idCuenta indicados.
	 * 
	 * @param numero
	 * @param anio
	 * @param idCuenta
	 * @return
	 * @throws Exception
	 */
	public static Habilitacion getByNroAnioYIdCuenta(Integer numero, Integer anio, Long idCuenta ) throws Exception {
		return (Habilitacion) EspDAOFactory.getHabilitacionDAO().getByNroAnioYIdCuenta(numero,anio,idCuenta);
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

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
		if(getRecurso()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
		}
		
		if(getTipoHab()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.TIPOHAB_LABEL);
		}
		
		if(getTipoHab() != null && TipoHab.COD_EXTERNA.equals(getTipoHab().getCodigo())
				&& getCuenta() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
		}
		
		if(getTipoHab() != null && TipoHab.COD_INTERNA.equals(getTipoHab().getCodigo())
				&& getValoresCargados() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.VALORESCARGADOS_LABEL);
		}
		
		if(getTipoCobro()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.TIPOCOBRO_LABEL);
		}
		
		if(StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.HABILITACION_DESCRIPCION);
		}
		
		if(getFechaHab()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.HABILITACION_FECHAHAB);
		}
		
		if(getFecEveDes()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.HABILITACION_FECEVEDES);
		}
		
		if(getIdPerHab()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.HABILITACION_PERHAB);
		}
		
		if (hasError()) {
			return false;
		}
		// Valida que la Fecha Desde no sea mayor que la fecha Hasta
		if(this.fecEveHas!=null){
			if(!DateUtil.isDateBeforeOrEqual(this.fecEveDes, this.fecEveHas)){
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, EspError.HABILITACION_FECEVEDES, EspError.HABILITACION_FECEVEHAS);
			}			
		}
		
		return !hasError();
	}
	
	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		if (GenericDAO.hasReference(this, PrecioEvento.class, "habilitacion")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			EspError.HABILITACION_LABEL , EspError.PRECIOEVENTO_LABEL);
		}
		if (GenericDAO.hasReference(this, EntHab.class, "habilitacion")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				EspError.HABILITACION_LABEL , EspError.ENTHAB_LABEL);
		}
		if (GenericDAO.hasReference(this, EntVen.class, "habilitacion")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				EspError.HABILITACION_LABEL , EspError.ENTVEN_LABEL);
		}
		if (GenericDAO.hasReference(this, HabExe.class, "habilitacion")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				EspError.HABILITACION_LABEL , EspError.HABEXE_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	// Administracion de PrecioEvento
	public PrecioEvento createPrecioEvento(PrecioEvento precioEvento) throws Exception {
		
		// Validaciones de negocio
		if (!precioEvento.validateCreate()) {
			return precioEvento;
		}

		EspDAOFactory.getPrecioEventoDAO().update(precioEvento);
		
		return precioEvento;
	}	

	public PrecioEvento updatePrecioEvento(PrecioEvento precioEvento) throws Exception {
		
		// Validaciones de negocio
		if (!precioEvento.validateUpdate()) {
			return precioEvento;
		}

		EspDAOFactory.getPrecioEventoDAO().update(precioEvento);
		
		return precioEvento;
	}	

	public PrecioEvento deletePrecioEvento(PrecioEvento precioEvento) throws Exception {
		
		// Validaciones de negocio
		if (!precioEvento.validateDelete()) {
			return precioEvento;
		}
				
		EspDAOFactory.getPrecioEventoDAO().delete(precioEvento);
		
		return precioEvento;
	}	
	
	// Administracion de EntHab
	public EntHab createEntHab(EntHab entHab) throws Exception {
		
		// Validaciones de negocio
		if (!entHab.validateCreate()) {
			return entHab;
		}

		EspDAOFactory.getEntHabDAO().update(entHab);
		
		return entHab;
	}	

	public EntHab updateEntHab(EntHab entHab) throws Exception {
		
		// Validaciones de negocio
		if (!entHab.validateUpdate()) {
			return entHab;
		}

		EspDAOFactory.getEntHabDAO().update(entHab);
		
		return entHab;
	}	

	public EntHab deleteEntHab(EntHab entHab) throws Exception {
		
		// Validaciones de negocio
		if (!entHab.validateDelete()) {
			return entHab;
		}
				
		EspDAOFactory.getEntHabDAO().delete(entHab);
		
		return entHab;
	}	
	
	// Administracion de EntVen
	public EntVen createEntVen(EntVen entVen) throws Exception {
		
		// Validaciones de negocio
		if (!entVen.validateCreate()) {
			return entVen;
		}

		EspDAOFactory.getEntVenDAO().update(entVen);
		
		return entVen;
	}	

	public EntVen updateEntVen(EntVen entVen) throws Exception {
		
		// Validaciones de negocio
		if (!entVen.validateUpdate()) {
			return entVen;
		}

		EspDAOFactory.getEntVenDAO().update(entVen);
		
		return entVen;
	}	

	public EntVen deleteEntVen(EntVen entVen) throws Exception {
		
		// Validaciones de negocio
		if (!entVen.validateDelete()) {
			return entVen;
		}
				
		EspDAOFactory.getEntVenDAO().delete(entVen);
		
		return entVen;
	}
	
	// Administracion de HabExe
	public HabExe createHabExe(HabExe habExe) throws Exception {
		
		// Validaciones de negocio
		if (!habExe.validateCreate()) {
			return habExe;
		}

		EspDAOFactory.getHabExeDAO().update(habExe);
		
		return habExe;
	}	

	public HabExe updateHabExe(HabExe habExe) throws Exception {
		
		// Validaciones de negocio
		if (!habExe.validateUpdate()) {
			return habExe;
		}

		EspDAOFactory.getHabExeDAO().update(habExe);
		
		return habExe;
	}	

	public HabExe deleteHabExe(HabExe habExe) throws Exception {
		
		// Validaciones de negocio
		if (!habExe.validateDelete()) {
			return habExe;
		}
				
		EspDAOFactory.getHabExeDAO().delete(habExe);
		
		return habExe;
	}	

	
	/**
	 * Anula las entradas vendidas correspondientes a una deuda.
	 */
	public static void anularEntradasVendidas(Long idDeuda) {
		try {
			
			for (EntVen entVen: EntVen.getByIdDeuda(idDeuda)) {
				Integer totalVendidas = entVen.getTotalVendidas();
				// Incrementamos el total restante de entradas
				EntHab entHab = entVen.getEntHab();
				Integer totalRestantes = entHab.getTotalRestantes();
				entHab.setTotalRestantes(totalRestantes + totalVendidas);
				// Obtenemos la habilitacion
				Habilitacion habilitacion = entVen.getHabilitacion();
				// Eliminamos la entradas vendidas
				habilitacion.deleteEntVen(entVen);
			}
			
		} catch (Exception e) {
			log.error("Error al eliminar los registros de entradas vendidas", e);
		}
	}
	
	
	// Administrar HisEstHab
	
	public HisEstHab createHisEstHab(HisEstHab hisEstHab) throws Exception {

		// Validaciones de negocio
		if (!hisEstHab.validateCreate()) {
			return hisEstHab;
		}

		EspDAOFactory.getHisEstHabDAO().update(hisEstHab);

		return hisEstHab;
	}
	
	public HisEstHab updateHisEstHab(HisEstHab hisEstHab) throws Exception {

		// Validaciones de negocio

		EspDAOFactory.getHisEstHabDAO().update(hisEstHab);

		return hisEstHab;
	}
	
	public HisEstHab deleteHisEstHab(HisEstHab hisEstHab) throws Exception {
		
		// Validaciones de negocio
		if (!hisEstHab.validateDelete()) {
			return hisEstHab;
		}
		
		EspDAOFactory.getHisEstHabDAO().delete(hisEstHab);
		
		return hisEstHab;
	}
	
	/**
	 *  Cambia el Estado de la Habilitacion al pasado, y crea un registro historico de cambio con la observacion indicada.
	 *
	 * @param idNuevoEstado
	 * @param observacion
	 * @throws Exception
	 */
	public void cambiarEstado(Long idNuevoEstado, String observacion) throws Exception{
		EstHab estHab = EstHab.getById(idNuevoEstado);
		
		HisEstHab hisEstHab = new HisEstHab();
		hisEstHab.setHabilitacion(this);
		hisEstHab.setEstHab(estHab);
		hisEstHab.setObservaciones(observacion);
		hisEstHab.setFecha(new Date());
		String logCambios = "Estado Anterior: "+this.getEstHab().getDesEstHab()
							+" , Estado Nuevo: "+estHab.getDesEstHab()
							+" , Fecha de Cambio de Estado: "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK);
		hisEstHab.setLogCambios(logCambios);
		this.createHisEstHab(hisEstHab);
		
		this.setEstHab(estHab);
		EspDAOFactory.getHabilitacionDAO().update(this);
	}
}

