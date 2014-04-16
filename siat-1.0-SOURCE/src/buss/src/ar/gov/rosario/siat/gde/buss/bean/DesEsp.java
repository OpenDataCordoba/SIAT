//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipoDeuda;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a DesEsp
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_desEsp")
public class DesEsp extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Transient
	Logger log = Logger.getLogger(DesEsp.class);
	
	@Column(name = "desDesEsp")
	private String desDesEsp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idRecurso")
	private Recurso recurso;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idViaDeuda")
	private ViaDeuda viaDeuda;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idTipoDeuda")
	private TipoDeuda tipoDeuda;

	@Column(name = "fechaVtoDeudaDesde")
	private Date fechaVtoDeudaDesde;

	@Column(name = "fechaVtoDeudaHasta")
	private Date fechaVtoDeudaHasta;

	@Column(name = "porDesCap")
	private Double porDesCap;

	@Column(name = "porDesAct")
	private Double porDesAct;

	@Column(name = "porDesInt")
	private Double porDesInt;

	@Column(name = "leyendaDesEsp")
	private String leyendaDesEsp;
		
	@Column(name = "idCaso")
	private String idCaso;

	@OneToMany( mappedBy="desEsp")
	@Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
	@JoinColumn(name="idDescuento")
	@OrderBy(clause="fechaDesde")
	private List<DesRecClaDeu> listDesRecClaDeu;
	
	@OneToMany( mappedBy="desEsp")
	@Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
	@JoinColumn(name="idDescuento")
	@OrderBy(clause="fechaDesde")
	private List<DesAtrVal> listDesAtrVal;
	
	@OneToMany( mappedBy="desEsp")
	@Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
	@JoinColumn(name="idDesEsp")
	@OrderBy(clause="fechaDesde")
	private List<DesEspExe> listDesEspExe;	
	
	// <#Propiedades#>

	// Constructores
	public DesEsp() {
		super();
		// Seteo de valores default
		// propiedad_ejemplo = valorDefault;
	}

	public DesEsp(Long id) {
		super();
		setId(id);
	}

	// Metodos de instancia
	
	/**
	 * el Map que devuelve tiene pares idAtributo-valorAtributo, que se obtienen de la lista de DesAtrVal que contiene la instancia de DesEsp
	 */
	public Map<Long, String> getMapAtrVal(){
		Map<Long, String> mapAtrDesEsp = new HashMap<Long, String>();
		for(DesAtrVal desAtrVal: getListDesAtrVal()){
			mapAtrDesEsp.put(desAtrVal.getAtributo().getId(), desAtrVal.getValor());
		}
		return mapAtrDesEsp;
	}
	
	/**
	 * Devuelve un Map con pares idAtributo-valorAtributo, que se obtienen de la lista de DesAtrVal que contiene la instancia de DesEsp, de los atributos VIGENTES a la fecha que llega, para el descuento.
	 */
	public Map<Long, String> getMapAtrValVigentes(Date fecha){
		Map<Long, String> mapAtrDesEsp = new HashMap<Long, String>();
		for(DesAtrVal desAtrVal: getListDesAtrVal()){
			if(DateUtil.isDateAfterOrEqual(fecha, desAtrVal.getFechaDesde()) &&
			  (desAtrVal.getFechaHasta()== null || DateUtil.isDateBeforeOrEqual(fecha, desAtrVal.getFechaHasta())))
				mapAtrDesEsp.put(desAtrVal.getAtributo().getId(), desAtrVal.getValor());
		}
		return mapAtrDesEsp;
	}	
	
	/**
	 * Calcula si el descuento es aplicable a la deuda pasada como parametro, en la fechaVto, validando tambien la clasificacion de la deuda
	 * @param deuda
	 * @return
	 */
	public boolean esAplicable(Deuda deuda, Date fechaVto){
		if(DateUtil.isDateBeforeOrEqual(getFechaVtoDeudaDesde(),fechaVto) && 
				(getFechaVtoDeudaHasta()==null || DateUtil.isDateAfterOrEqual(getFechaVtoDeudaHasta(),fechaVto))){
			if(getListDesRecClaDeu()!=null && !getListDesRecClaDeu().isEmpty()){
				if(contieneRecClaDeuVigente(deuda.getRecClaDeu(), fechaVto))
					return true;
			}else //Si valida la fecha y no tiene definido una lista de DesRecClaDeu es aplicable.
				log.debug("####" + getDesDesEsp() + " es aplicable");
				return true;					
		}
		return false;
	}
	

	
	public boolean contieneRecClaDeu(RecClaDeu recClaDeu){
		for(DesRecClaDeu desRecClaDeu: getListDesRecClaDeu()){
			if(desRecClaDeu.getRecClaDeu().getId()==recClaDeu.getId())
				return true;
		}
		return false;
	}
	
	/**
	 * Valida si además de tener la clasificacion pasada por parametro, la misma esta vigente en el descuento, a la fecha pasada por parametro
	 * @param recClaDeu
	 * @return
	 */
	public boolean contieneRecClaDeuVigente(RecClaDeu recClaDeu, Date fecha){
		for(DesRecClaDeu desRecClaDeu: getListDesRecClaDeu()){
			if(desRecClaDeu.getRecClaDeu().getId()==recClaDeu.getId() &&
				DateUtil.isDateBeforeOrEqual(desRecClaDeu.getFechaDesde(), fecha) &&
				(desRecClaDeu.getFechaHasta()==null || DateUtil.isDateAfterOrEqual(desRecClaDeu.getFechaHasta(), fecha))
			   )
				return true;
		}
		return false;		
	}
	
	// Metodos de Clase
	public static DesEsp getById(Long id) {
		return (DesEsp) GdeDAOFactory.getDesEspDAO().getById(id);
	}

	public static DesEsp getByIdNull(Long id) {
		return (DesEsp) GdeDAOFactory.getDesEspDAO().getByIdNull(id);
	}

	public static List<DesEsp> getList() {
		return (ArrayList<DesEsp>) GdeDAOFactory.getDesEspDAO().getList();
	}

	/**
	 * Devuelve los descuentos especiales vigenets a la fecha de Vto. de la deuda, pasada como parametro, además de filtrar por el resto de los parametros
	 * @param idRecurso
	 * @param idTipoDeuda
	 * @param idViaDeuda
	 * @param fechaVtoDeuda
	 * @param activos
	 * @return
	 */
	public static List<DesEsp> getListVigentes(Long idRecurso, Long idTipoDeuda, Long idViaDeuda, Date fechaVtoDeuda, Boolean activos) {
		return (ArrayList<DesEsp>) GdeDAOFactory.getDesEspDAO().getListVigentes(idRecurso, idTipoDeuda, idViaDeuda, fechaVtoDeuda, activos);
	}
	
	public static List<DesEsp> getListActivos() {
		return (ArrayList<DesEsp>) GdeDAOFactory.getDesEspDAO().getListActiva();
	}

	// Getters y setters

	public String getDesDesEsp() {
		return desDesEsp;
	}

	public void setDesDesEsp(String desDesEsp) {
		this.desDesEsp = desDesEsp;
	}

	public Date getFechaVtoDeudaDesde() {
		return fechaVtoDeudaDesde;
	}

	public void setFechaVtoDeudaDesde(Date fechaVtoDeudaDesde) {
		this.fechaVtoDeudaDesde = fechaVtoDeudaDesde;
	}

	public Date getFechaVtoDeudaHasta() {
		return fechaVtoDeudaHasta;
	}

	public void setFechaVtoDeudaHasta(Date fechaVtoDeudaHasta) {
		this.fechaVtoDeudaHasta = fechaVtoDeudaHasta;
	}

	public String getLeyendaDesEsp() {
		return leyendaDesEsp;
	}

	public void setLeyendaDesEsp(String leyendaDesEsp) {
		this.leyendaDesEsp = leyendaDesEsp;
	}

	public Double getPorDesAct() {
		return porDesAct;
	}

	public void setPorDesAct(Double porDesAct) {
		this.porDesAct = porDesAct;
	}

	public Double getPorDesCap() {
		return porDesCap;
	}

	public void setPorDesCap(Double porDesCap) {
		this.porDesCap = porDesCap;
	}

	public Double getPorDesInt() {
		return porDesInt;
	}

	public void setPorDesInt(Double porDesInt) {
		this.porDesInt = porDesInt;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public TipoDeuda getTipoDeuda() {
		return tipoDeuda;
	}

	public void setTipoDeuda(TipoDeuda tipoDeuda) {
		this.tipoDeuda = tipoDeuda;
	}

	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

	public List<DesRecClaDeu> getListDesRecClaDeu() {
		return listDesRecClaDeu;
	}

	public void setListDesRecClaDeu(List<DesRecClaDeu> listDesRecClaDeu) {
		this.listDesRecClaDeu = listDesRecClaDeu;
	}

	public List<DesAtrVal> getListDesAtrVal() {
		return listDesAtrVal;
	}

	public void setListDesAtrVal(List<DesAtrVal> listDesAtrVal) {
		this.listDesAtrVal = listDesAtrVal;
	}

	public List<DesEspExe> getListDesEspExe() {
		return listDesEspExe;
	}

	public void setListDesEspExe(List<DesEspExe> listDesEspExe) {
		this.listDesEspExe = listDesEspExe;
	}

	/**
	 * Extrae todas las exenciones de los registros de listDesEspExe
	 * @return
	 */
	public List<Exencion> getListExencion(){
		List<Exencion> listExencion = new ArrayList<Exencion>();
		for(DesEspExe desEspExe: listDesEspExe){
			listExencion.add(desEspExe.getExencion());
		}
		return listExencion;
	}
	
	/**
	 * Devuelve la lista de exenciones VIGENTES del descuento, a la fecha que llega.
	 * <br>El descuento puede tener alguna/s exenion/es que no esten vigentes a la fecha que llega, esas las saca
	 * @return
	 */
	public List<Exencion> getListExencionesVigentes(Date fecha){
		if (this.getListDesEspExe().size()==0 ) {
			return null;
			
		} else {
				
			List<Exencion> listExencionVigentes = new ArrayList<Exencion>();
			for(DesEspExe desEspExe: this.getListDesEspExe() ){
				if(DateUtil.isDateAfterOrEqual(fecha, desEspExe.getFechaDesde()) && 
				   (desEspExe.getFechaHasta()==null || DateUtil.isDateBeforeOrEqual(fecha, desEspExe.getFechaHasta()))
				 )
					listExencionVigentes.add(desEspExe.getExencion());
			}
			return listExencionVigentes;
		}
	}	
	
	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
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
		// limpiamos la lista de errores
		clearError();

		// <#ValidateDelete#>

		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones
		if (StringUtil.isNullOrEmpty(getDesDesEsp())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESESP_DESDESESP);
		}
		if (getRecurso()==null || getRecurso().getId()<0) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESESP_RECURSODESESP);
		}		
		if (getTipoDeuda()==null || getTipoDeuda().getId()<0) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESESP_TIPODEUDADESESP);
		}		
		if (getViaDeuda()==null || getViaDeuda().getId()<0) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESESP_VIADEUDADESESP);
		}		
		if (getFechaVtoDeudaDesde()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESESP_FECHAVTODESDEDESESP);
		}		
		if (getFechaVtoDeudaHasta()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESESP_FECHAVTOHASTADESESP);
		}
		if( getFechaVtoDeudaDesde()!=null && getFechaVtoDeudaHasta()!=null && getFechaVtoDeudaDesde().after(getFechaVtoDeudaHasta())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, GdeError.DESESP_FECHAVTOHASTADESESP, GdeError.DESESP_FECHAVTODESDEDESESP);
		}
		if(getPorDesCap()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESESP_PORDESCAP);
		}
		if(getPorDesAct()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESESP_PORDESACT);
		}
		if(getPorDesInt()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESESP_PORDESINT);
		}
		if (StringUtil.isNullOrEmpty(getLeyendaDesEsp())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.DESESP_LEYENDADESESP);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de unique
/*		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codDesEsp");
		if (!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO,
					GdeError.DESESP_CODDESESP);
		}*/

		return true;
	}

	// Metodos de negocio

	/**
	 * Activa el DesEsp. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getDesEspDAO().update(this);
	}

	/**
	 * Desactiva el DesEsp. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getDesEspDAO().update(this);
	}

	/**
	 * Valida la activacion del DesEsp
	 * 
	 * @return boolean
	 */
	private boolean validateActivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	/**
	 * Valida la desactivacion del DesEsp
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}
	
	// Metodos para Administrar DesRecClaDeu
	public DesRecClaDeu createDesEsp(DesRecClaDeu desRecClaDeu) throws Exception {
		// Validaciones de negocio
		if (!desRecClaDeu.validateCreate()) {
			return desRecClaDeu;
		}

		GdeDAOFactory.getDesRecClaDeuDAO().update(desRecClaDeu);	
		
		return desRecClaDeu;
	}

	public DesRecClaDeu updateDesEsp(DesRecClaDeu desRecClaDeu) throws Exception {
		// Validaciones de negocio
		if (!desRecClaDeu.validateUpdate()) {
			return desRecClaDeu;
		}

		GdeDAOFactory.getDesRecClaDeuDAO().update(desRecClaDeu);	
		
		return desRecClaDeu;
	}

	public DesRecClaDeu deleteDesRecClaDeu(DesRecClaDeu desRecClaDeu) {
		// Validaciones de negocio
		if (!desRecClaDeu.validateDelete()) {
			return desRecClaDeu;
		}

		GdeDAOFactory.getDesRecClaDeuDAO().delete(desRecClaDeu);	
		
		return desRecClaDeu;
	}

	// Metodos para Administrar DesAtrVal
	public DesAtrVal createDesAtrVal(DesAtrVal desAtrVal) throws Exception {
		// Validaciones de negocio
		if (!desAtrVal.validateCreate()) {
			return desAtrVal;
		}

		GdeDAOFactory.getDesAtrValDAO().update(desAtrVal);	
		
		return desAtrVal;
	}

	public DesAtrVal updateDesAtrVal(DesAtrVal desAtrVal) throws Exception {
		// Validaciones de negocio
		if (!desAtrVal.validateUpdate()) {
			return desAtrVal;
		}

		GdeDAOFactory.getDesAtrValDAO().update(desAtrVal);	
		
		return desAtrVal;
	}

	public DesAtrVal deleteDesAtrVal(DesAtrVal desAtrVal) {
		// Validaciones de negocio
		if (!desAtrVal.validateDelete()) {
			return desAtrVal;
		}

		GdeDAOFactory.getDesAtrValDAO().delete(desAtrVal);	
		
		return desAtrVal;
	}

	// Metodos para Administrar DesEspExe
	public DesEspExe createDesEspExe(DesEspExe desEspExe) throws Exception {
		// Validaciones de negocio
		if (!desEspExe.validateCreate()) {
			return desEspExe;
		}

		GdeDAOFactory.getDesEspExeDAO().update(desEspExe);	
		
		return desEspExe;
	}

	public DesEspExe updateDesEspExe(DesEspExe desEspExe) throws Exception {
		// Validaciones de negocio
		if (!desEspExe.validateUpdate()) {
			return desEspExe;
		}

		GdeDAOFactory.getDesEspExeDAO().update(desEspExe);	
		
		return desEspExe;
	}

	public DesEspExe deleteDesEspExe(DesEspExe desEspExe) {
		// Validaciones de negocio
		if (!desEspExe.validateDelete()) {
			return desEspExe;
		}

		GdeDAOFactory.getDesEspExeDAO().delete(desEspExe);	
		
		return desEspExe;
	}

	@Override
	public String infoString() {
		String ret =" Descuento Especial";
		
		if(desDesEsp!=null){
			ret+=" - descripcion: "+desDesEsp;
		}

		if(recurso!=null){
			ret+=" - Recurso: "+recurso.getDesRecurso();
		}
		
		if(viaDeuda!=null){
			ret+=" - Via Deuda: "+viaDeuda.getDesViaDeuda();
		}
		
		if(tipoDeuda!=null){
			ret+=" - Tipo Deuda: "+tipoDeuda.getDesTipoDeuda();
		}
		
		if (fechaVtoDeudaDesde!=null){
			ret+=" - Fecha Vencimiento Desde: " + DateUtil.formatDate(fechaVtoDeudaDesde , DateUtil.ddSMMSYYYY_MASK);
		}

		if (fechaVtoDeudaHasta!=null){
			ret+=" - Fecha Vencimiento Hasta: " + DateUtil.formatDate(fechaVtoDeudaHasta , DateUtil.ddSMMSYYYY_MASK);
		}

		if (porDesCap!=null){
			ret += " - Porcentaje Desc. Capital: " + porDesCap + " %";
		}

		if (porDesAct!=null){
			ret += " - Porcentaje Desc. Actualizacion: " + porDesAct + " %";
		}

		if (porDesInt!=null){
			ret += " - Porcentaje Desc. Interes: " + porDesInt + " %";
		}

		if (leyendaDesEsp!=null){
			ret += " - Leyenda Desc. Especial: " + leyendaDesEsp;
		}
		
		if(idCaso!=null){
			ret+=" - Caso: "+idCaso;
		}
				
		return ret;
	}
}


