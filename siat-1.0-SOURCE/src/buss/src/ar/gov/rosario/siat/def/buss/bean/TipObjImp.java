//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import ar.gov.rosario.siat.base.buss.dao.SiatBussCache;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.AtrValDefinition;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAtrVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.pad.buss.bean.ObjImp;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Bean correspondiente a Tipo de Objeto Imponible
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_tipobjimp")
public class TipObjImp extends BaseBO {
	@Transient
	Logger log = Logger.getLogger(TipObjImp.class);

	// Propiedades
	private static final long serialVersionUID = 1L;

	public static final Long PARCELA = 1L;
	public static final Long COMERCIO = 2L;
	
	public final static long FOR_BUSQUEDA   = 1; 
	public final static long FOR_INTERFACE  = 2;
	public final static long FOR_MANUAL     = 3;
	public final static long FOR_WEB        = 4;
	public final static long FOR_BUS_MASIVA = 5;
	
	@Column(name = "codtipobjimp")
	private String codTipObjImp;
	
	@Column(name = "destipobjimp")
	private String desTipObjImp;
	
	@Column(name = "essiat")
	private Integer esSiat;

	@Column(name = "fechaAlta")
	private Date fechaAlta;

	@Column(name = "fechaBaja")
	private Date fechaBaja;

	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
    @JoinColumn(name="idProceso") 
	private Proceso proceso;
	
	@OneToMany()
	@JoinColumn(name="idtipobjimp")
	private List<TipObjImpAtr> listTipObjImpAtr;

	@OneToMany()
	@JoinColumn(name="idtipobjimp")
	private List<TipObjImpAreO> listTipObjImpAreO;
	
	@OneToMany()
	@JoinColumn(name="idtipobjimp")
	private List<Recurso> listRecurso;
	
	// Constructores
	public TipObjImp(){
		super();
		// Seteo de valores default	
		setEsSiat(0);
	}
	
	// Getters y setters
	public String getCodTipObjImp() {
		return codTipObjImp;
	}
	public void setCodTipObjImp(String codTipObjImp) {
		this.codTipObjImp = codTipObjImp;
	}
	public String getDesTipObjImp() {
		return desTipObjImp;
	}
	public void setDesTipObjImp(String desTipObjImp) {
		this.desTipObjImp = desTipObjImp;
	}
	public Integer getEsSiat() {
		return esSiat;
	}
	public void setEsSiat(Integer esSiat) {
		this.esSiat = esSiat;
	}
	public List<TipObjImpAreO> getListTipObjImpAreO() {
		return listTipObjImpAreO;
	}
	public void setListTipObjImpAreO(List<TipObjImpAreO> listTipObjImpAreO) {
		this.listTipObjImpAreO = listTipObjImpAreO;
	}
	public List<TipObjImpAtr> getListTipObjImpAtr() {
		return listTipObjImpAtr;
	}
	public void setListTipObjImpAtr(List<TipObjImpAtr> listTipObjImpAtr) {
		this.listTipObjImpAtr = listTipObjImpAtr;
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
	public List<Recurso> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<Recurso> listRecurso) {
		this.listRecurso = listRecurso;
	}
	
	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}

	public Proceso getProceso() {
		return proceso;
	}

	// Metodos de Clase
	/**
	 * Obtiene el Tipo Objeto Imponible a partir de su Id.
	 * return TipObjImp
	 */
	public static TipObjImp getById(Long id) {
		return (TipObjImp) DefDAOFactory.getTipObjImpDAO().getById(id);
	}

	public static TipObjImp getByIdNull(Long id) {
		return (TipObjImp) DefDAOFactory.getTipObjImpDAO().getByIdNull(id);
	}
	/**
	 * Obtiene el Tipo Objeto Imponible a partir de su c&oacute;digo.
	 * return TipObjImp
	 */
	public static TipObjImp getByCodigo(String codigo) throws Exception {
		return (TipObjImp) DefDAOFactory.getTipObjImpDAO().findByCodigo(codigo);
	}
	
	/**
	 * Obtiene la lista de todos los Tipo Objeto Imponible.
	 * @return List<TipObjImp>
	 */
	public static List<TipObjImp> getList() {
		return (ArrayList<TipObjImp>) DefDAOFactory.getTipObjImpDAO().getList();
	}
	
	/**
	 * Obtiene la lista de los Tipo Objeto Imponible activos.
	 * @return List<TipObjImp>
	 */
	public static List<TipObjImp> getListActivos() {			
		return (ArrayList<TipObjImp>) DefDAOFactory.getTipObjImpDAO().getListActiva();
	}	

	/**
	 * Obtiene la lista de los Tipo Objeto Imponible con esSiat = true.
	 * @return List<TipObjImp>
	 */
	public static List<TipObjImp> getListEsSiat() throws Exception{			
		return (ArrayList<TipObjImp>) DefDAOFactory.getTipObjImpDAO().getListEsSiat();
	}	
	
	/**
	 * Obtiene la lista de los Recursos activos asociados al Tipo Objeto Imponible.
	 * @return List<Recurso>
	 */
	public List<Recurso> getListRecursoActivo(){
		return (ArrayList<Recurso>) ListUtilBean.filterByActivo(getListRecurso());
	}

	/**
	 * Obtiene el Recurso Principal Activo asociado al Tipo Objeto Imponible.
	 * @return List<Recurso> Si no encuentra ninguno retorna null. Si encuentra mas de uno retorna null y agrega un error
	 */
	public Recurso getRecursoPrincipal(){
		Recurso recPri = null;
		int cantRecPri = 0;
		for(Recurso recurso: getListRecursoActivo()){
			if(recurso.getEsPrincipal()==1){
				recPri = recurso;
				cantRecPri++;
			}
		}
		// Si encuentro mas de un Recurso Principal informo error.
		if(cantRecPri>1){
			addRecoverableError("Existe m&aacute;s de un recurso principal para el TipObjImp del ObjImp creado. No se puede crear cuenta por recurso principal.");
			return null;
		}
		return recPri;
	}

	/**
	 * Obtiene la lista de los Recursos Secundarios activos asociados al Tipo Objeto Imponible.
	 * @return List<Recurso>
	 */
	public List<Recurso> getListRecursoSecundario(){
		List<Recurso> listRecurso = getListRecursoActivo(); 
		List<Recurso> listRecursoSec =  new ArrayList<Recurso>();
		if(!ListUtil.isNullOrEmpty(listRecurso)){
			for(Recurso recurso: listRecurso){
				if(recurso.getEsPrincipal()==0){
					listRecursoSec.add(recurso);
					/*if(listRecursoSec.size()>1)
						listRecursoSec.remove(recurso);
					else
						return new ArrayList<Recurso>();
					 */
				}
			}
		}
		return listRecursoSec;
	}
	
	// Metodos de instancia 

	// Metodos de negocio
	
	/**
	 * Activa el Tipo Objeto Imponible.
	 * Valida que el contenga atributos asociados. 
	 * Setea la fecha Baja a null y setea el estado a activo
	 */
	public void activar(){
		
		if(!this.validateActivar()){
			return;
		}
		
		this.setFechaBaja(null);
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getTipObjImpDAO().update(this);
	}

	/**
	 * Desactiva el Tipo Objeto Imponible. Previamente valida la desactivacion. 
	 * Setea el estado a Inactivo.
	 */
	public void desactivar(){
		
		if(!this.validateDesactivar()){
			return;
		}
		
		this.setEstado(Estado.INACTIVO.getId());
		
		DefDAOFactory.getTipObjImpDAO().update(this);
	}
	
	// Validaciones 
	/**
	 * Valida la creacion del Tipo Objeto Imponible
	 * @return boolean
	 * @throws Exception
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		// Validaciones de requeridos y unicidad comunes
		this.validate();

		if (hasError()) {
			return false;
		}
		
		return !hasError();
	}

	/**
	 * Valida la actualizacion del Tipo Objeto Imponible
	 * @return boolean
	 * @throws Exception
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();

		// validaciones comunes
		this.validate();
		
		if (hasError()) {
			return false;
		}

		// Validacion fecha de baja no sea menor a fecha de alta
		if(this.fechaBaja != null && DateUtil.isDateBefore(this.fechaBaja, this.fechaAlta)){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, new Object[]{DefError.TIPOBJIMP_FECHABAJA,DefError.TIPOBJIMP_FECHAALTA});
		}

		return !hasError();		
	}
	
	/**
	 * Validaciones comunes de creacion y actualizacion
	 * Requeridos: codTipObjImp, desTipObjImp, esSiat, fechaAlta.
	 * Unicidad de codTipObjImp.
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();
		
		UniqueMap uniqueMap = new UniqueMap();

		//Validaciones de Requeridos
		if(StringUtil.isNullOrEmpty(this.codTipObjImp)){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMP_CODTIPOBJIMP);
		}		
		if(StringUtil.isNullOrEmpty(this.desTipObjImp)){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMP_DESTIPOBJIMP);
		}
		
		if(!SiNo.getEsValido(this.getEsSiat())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMP_ESSIAT);
		}
		if(this.fechaAlta == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMP_FECHAALTA);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Unicidad
		uniqueMap.addString("codTipObjImp");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)){
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, DefError.TIPOBJIMP_CODTIPOBJIMP);
		}

		return !hasError();
	}

	/**
	 * Validaciones de la eliminacion de Tipo Objeto Imponible
	 * Valida que no tenga asociados: TipObjImpAtr, TipObjImpAreO, ObjetoImponible y Recurso.
	 * @return boolean
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		// verificamos que no tenga TipObjImpAtr asociados
		if (GenericDAO.hasReference(this, TipObjImpAtr.class, "tipObjImp")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.TIPOBJIMP_LABEL , DefError.TIPOBJIMPATR_LABEL);
		}
		
		// verificamos que no tenga TipObjImpAreO asociados
		if (GenericDAO.hasReference(this, TipObjImpAreO.class, "tipObjImp")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.TIPOBJIMP_LABEL , DefError.TIPOBJIMPAREO_LABEL);
		}

		// verificamos que no tenga Recurso asociados
		if (GenericDAO.hasReference(this, Recurso.class, "tipObjImp")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.TIPOBJIMP_LABEL , DefError.RECURSO_LABEL);
		}

		// verificamos que no tenga ObjImp asociados
		if (GenericDAO.hasReference(this, ObjImp.class, "tipObjImp")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.TIPOBJIMP_LABEL , PadError.OBJIMP_LABEL);
		}
		
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}

	/**
	 * Valida la Activacion del Tipo de Objeto Imponible.
	 * Valida que contenga Atributos
	 * @return
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();

		//Validaciones
		
		if(ListUtil.isNullOrEmpty(this.getListTipObjImpAtr())){
			addRecoverableError(DefError.TIPOBJIMP_SIN_ATR);
		}
		
		if(!this.hasAtrClave()){
			addRecoverableError(DefError.TIPOBJIMP_SIN_ATR_CLAVE);
		}
		
		if(!this.hasAtrClaveFuncional()){
			addRecoverableError(DefError.TIPOBJIMP_SIN_ATR_CLAVE_FUNCIONAL);
		}
		
		if (hasError()) {
			return false;
		}
		
		return !hasError();
	}

	
	/**
	 * Valida la desactivacion del Tipo Objeto Imponible.
	 * Valida que la Fecha de baja distinta de nula y que no sea menor a la Fecha de Alta.
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();

		//Validaciones  
		if(this.fechaBaja == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMP_FECHABAJA);
		}
		
		if (hasError()) {
			return false;
		}
		
		if(this.fechaBaja != null && DateUtil.isDateBefore(this.fechaBaja, this.fechaAlta)){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, new Object[]{DefError.TIPOBJIMP_FECHABAJA,DefError.TIPOBJIMP_FECHAALTA});
		}
		
		return !hasError();
	}

	
	
	/**
	 * Crea un Tipo Objeto Imponible Area Origen
	 * @param tipObjImpAtr
	 * @return TipObjImpAtr
	 * @throws Exception
	 */
	public TipObjImpAtr createTipObjImpAtr(TipObjImpAtr tipObjImpAtr) throws Exception {
		
		// Validaciones 
		if (!tipObjImpAtr.validateCreate()) {
			return tipObjImpAtr;
		}

		DefDAOFactory.getTipObjImpAtrDAO().update(tipObjImpAtr);

		return tipObjImpAtr;
	}
	
	/**
	 * Actualiza el Tipo Objeto Imponible Area Origen
	 * @param tipObjImpAtr
	 * @return TipObjImpAtr
	 * @throws Exception
	 */
	public TipObjImpAtr updateTipObjImpAtr(TipObjImpAtr tipObjImpAtr) throws Exception {
		
		// Validaciones 
		if (!tipObjImpAtr.validateUpdate()) {
			return tipObjImpAtr;
		}
		
		DefDAOFactory.getTipObjImpAtrDAO().update(tipObjImpAtr);
		
	    return tipObjImpAtr;
	}
	
	/**
	 * Elimina un Tipo Objeto Imponible Area Origen
	 * @param  tipObjImpAtr
	 * @return TipObjImpAtr
	 * @throws Exception
	 */
	public TipObjImpAtr deleteTipObjImpAtr(TipObjImpAtr tipObjImpAtr) throws Exception {

		// Validaciones 
		if (!tipObjImpAtr.validateDelete()) {
			return tipObjImpAtr;
		}
		
		DefDAOFactory.getTipObjImpAtrDAO().delete(tipObjImpAtr);
		
		return tipObjImpAtr;
	}	

	/**
	 * Crea un Tipo Objeto Imponible Area Origen
	 * @param tipObjImpAreO
	 * @return TipObjImpAreO
	 * @throws Exception
	 */
	public TipObjImpAreO createTipObjImpAreO(TipObjImpAreO tipObjImpAreO) throws Exception {
		
		// Validaciones 
		if (!tipObjImpAreO.validateCreate()) {
			return tipObjImpAreO;
		}

		DefDAOFactory.getTipObjImpAreODAO().update(tipObjImpAreO);

		return tipObjImpAreO;
	}

	/**
	 * Actualiza el Tipo Objeto Imponible Area Origen
	 * @param  tipObjImpAreO
	 * @return TipObjImpAreO 
	 * @throws Exception
	 */
	public TipObjImpAreO updateTipObjImpAreO(TipObjImpAreO tipObjImpAreO) throws Exception {
		
		DefDAOFactory.getTipObjImpAreODAO().update(tipObjImpAreO);
		
	    return tipObjImpAreO;
	}
	
	/**
	 * Elimina el Tipo Objeto Imponible Area Origen
	 * @param  tipObjImpAreO
	 * @return TipObjImpAreO 
	 * @throws Exception
	 */
	public TipObjImpAreO deleteTipObjImpAreO(TipObjImpAreO tipObjImpAreO) throws Exception {

		DefDAOFactory.getTipObjImpAreODAO().delete(tipObjImpAreO);
		
		return tipObjImpAreO;
	}	

	/**
	 * Obtiene la lista de Area Origen del Tipo Objeto Imponible
	 * @return List <AreaOrigen>
	 */
	public List <Area> getListAreaOrigen(){
		List<Area> listAreaOrigen = new ArrayList<Area>();
		
		for (TipObjImpAreO toiAo : this.getListTipObjImpAreO()) {
			listAreaOrigen.add(toiAo.getAreaOrigen());
		}
		
		return listAreaOrigen;
	}

	/**
	 * Obtiene un objeto de definicion de atributos y valores para el Tipo Objeto Imponible 
	 * para la migracion. Obtiene todos los atributos vigentes.
	 * 
	 * @return la definicion con sus atributos y valores en blanco.
	 * @throws Exception 
	 */	
	public TipObjImpDefinition getDefinitionForAll() throws Exception {
		return this.getDefinitionForAll(new Date());
	}

	/**
	 * Obtiene un objeto de definicion de atributos y valores para el Tipo Objeto Imponible 
	 * Obtiene todos los atributos vigentes a la fecha pasada como parametro
	 * 
	 * @return la definicion con sus atributos y valores en blanco.
	 * @throws Exception 
	 */	
	public TipObjImpDefinition getDefinitionForAll(Date fechaAnalisis) throws Exception {
		// Crea un TipObjImpDefinition
		// Obtiene todos los TipObjImpAtr que no son esAtributoSiat y que esten vigentes
		// Por cada TipObjImpAtr
		// 	Crea un TipObjImpAtrDefinition y setear TipObjImpAtrVO 
		//  Agrega a TipObjImpDefinition
			
		TipObjImpDefinition tipObjImpDefinition = new TipObjImpDefinition();
		tipObjImpDefinition.setAct(AtrValDefinition.ACT_INTERFACE);  
			
		List<TipObjImpAtr> listTipObjImpAtrInterface = new ArrayList<TipObjImpAtr>(); 

		//-1L hace que sea un valor que no concuerda con ningun if.
		listTipObjImpAtrInterface = DefDAOFactory.getTipObjImpAtrDAO()
			.getListVigentesByIdTipObjImp(-1L, this.getId(), fechaAnalisis);
		
		List<TipObjImpAtrVO> listTipObjImpAtrVO = this.listTipObjImpAtrToVOSinPadre(listTipObjImpAtrInterface);
		
		for (TipObjImpAtrVO tipObjImpAtrVO: listTipObjImpAtrVO){
			TipObjImpAtrDefinition tipObjImpAtrDefinition = new TipObjImpAtrDefinition();
			tipObjImpAtrDefinition.setTipObjImpAtr(tipObjImpAtrVO);
			tipObjImpDefinition.getListTipObjImpAtrDefinition().add(tipObjImpAtrDefinition);
		}
		
		return tipObjImpDefinition;
	}
	
	/**
	 * Obtiene un objeto de definicion de atributos y valores para el Tipo Objeto Imponible para la busqueda.
	 * @return la definicion con sus atributos y valores en blanco.
	 * @throws Exception 
	 */	
	public TipObjImpDefinition getDefinitionForBusqueda(Long param) throws Exception {
		// Crea un TipObjImpDefinition
		// Obtiene todos los TipObjImpAtr con esAtributoBusqueda, esClave y esClaveFuncional, y que esten vigentes
		// Por cada TipObjImpAtr
		//  Crea un TipObjImpAtrDefinition y setear TipObjImpAtrVO 
		//  Agrega a TipObjImpDefinition
		
		TipObjImpDefinition tipObjImpDefinition = new TipObjImpDefinition();
		tipObjImpDefinition.setAct(AtrValDefinition.ACT_BUSCAR);  
			
		List<TipObjImpAtr> listTipObjImpAtrBusqueda = new ArrayList<TipObjImpAtr>(); 
		
		log.debug("          ###################### --> GetDefinitionForBusqueda 1: getListVigentesByIdTipObjImp()");		
		listTipObjImpAtrBusqueda = DefDAOFactory.getTipObjImpAtrDAO().getListVigentesByIdTipObjImp(param ,this.getId(), new Date());
		
		log.debug("          ###################### --> GetDefinitionForBusqueda 2: listTipObjImpAtrToVOSinPadre()");
		List<TipObjImpAtrVO> listTipObjImpAtrVO = this.listTipObjImpAtrToVOSinPadre(listTipObjImpAtrBusqueda);
		
		log.debug("          ###################### --> GetDefinitionForBusqueda 3: bucle seteo TipObjImpAtrDefinition");
		for (TipObjImpAtrVO tipObjImpAtrVO: listTipObjImpAtrVO){
			TipObjImpAtrDefinition tipObjImpAtrDefinition = new TipObjImpAtrDefinition();
			tipObjImpAtrDefinition.setTipObjImpAtr(tipObjImpAtrVO);
			tipObjImpDefinition.getListTipObjImpAtrDefinition().add(tipObjImpAtrDefinition);
		}
		
		log.debug("          ###################### --> GetDefinitionForBusqueda 4: return");
		return tipObjImpDefinition;
	}
	
	/**
	 * Obtiene un objeto de definicion de atributos y valores para el Tipo Objeto Imponible 
	 * para la migracion e interface de novedades. Solo incluye los atributos que no son SIAT.
	 * 
	 * @return la definicion con sus atributos y valores en blanco.
	 * @throws Exception 
	 */	
	public TipObjImpDefinition getDefinitionForInterface() throws Exception {
		// Crea un TipObjImpDefinition
		// Obtiene todos los TipObjImpAtr que no son esAtributoSiat y que esten vigentes
		// Por cada TipObjImpAtr
		//  Crea un TipObjImpAtrDefinition y setear TipObjImpAtrVO 
		//  Agrega a TipObjImpDefinition
			
		TipObjImpDefinition tipObjImpDefinition = new TipObjImpDefinition();
		tipObjImpDefinition.setAct(AtrValDefinition.ACT_INTERFACE);  
			
		List<TipObjImpAtr> listTipObjImpAtrInterface = new ArrayList<TipObjImpAtr>(); 
		log.debug("          ###################### --> GetDefinitionForInterface 1: getListVigentesByIdTipObjImp()");
		listTipObjImpAtrInterface = DefDAOFactory.getTipObjImpAtrDAO().getListVigentesByIdTipObjImp(FOR_INTERFACE,this.getId(), new Date());
		
		log.debug("          ###################### --> GetDefinitionForInterface 2: listTipObjImpAtrToVOSinPadre()");
		List<TipObjImpAtrVO> listTipObjImpAtrVO = this.listTipObjImpAtrToVOSinPadre(listTipObjImpAtrInterface);
		
		log.debug("          ###################### --> GetDefinitionForInterface 3: bucle seteo TipObjImpAtrDefinition");
		for (TipObjImpAtrVO tipObjImpAtrVO: listTipObjImpAtrVO){
			TipObjImpAtrDefinition tipObjImpAtrDefinition = new TipObjImpAtrDefinition();
			tipObjImpAtrDefinition.setTipObjImpAtr(tipObjImpAtrVO);
			tipObjImpDefinition.getListTipObjImpAtrDefinition().add(tipObjImpAtrDefinition);
		}
		
		log.debug("          ###################### --> GetDefinitionForInterface 4: return");
		return tipObjImpDefinition;
	}
	
	/**
	 * Obtiene un objeto de definicion de atributos y valores para el Tipo Objeto Imponible para el alta o modificacion.
	 * @return la definicion con sus atributos y valores en blanco.
	 * @throws Exception 
	 */	
	public TipObjImpDefinition getDefinitionForManual() throws Exception {
		// Crea un TipObjImpDefinition
		// Obtiene todos los TipObjImpAtr que esten vigentes
		// Por cada TipObjImpAtr
		//  Crea un TipObjImpAtrDefinition y setear TipObjImpAtrVO 
		//  Se setea el id del TipObjImpAtrVO al TipObjImpAtrDefinition 
		//  Agrega a TipObjImpDefinition
		
		TipObjImpDefinition tipObjImpDefinition = new TipObjImpDefinition();
		tipObjImpDefinition.setAct(AtrValDefinition.ACT_MANUAL);  
			
		List<TipObjImpAtr> listTipObjImpAtrManual = new ArrayList<TipObjImpAtr>(); 
		
		log.debug("          ###################### --> GetDefinitionForManual 1: getListVigentesByIdTipObjImp()");

		// Prueba de performance
		listTipObjImpAtrManual = SiatBussCache.getInstance().getListTipObjImpAtrManual(this.getId());
		
		log.debug("          ###################### --> GetDefinitionForManual 2: listTipObjImpAtrToVOSinPadre()");
		List<TipObjImpAtrVO> listTipObjImpAtrVO = this.listTipObjImpAtrToVOSinPadre(listTipObjImpAtrManual);
		
		log.debug("          ###################### --> GetDefinitionForManual 3: bucle seteo TipObjImpAtrDefinition");
		for (TipObjImpAtrVO tipObjImpAtrVO: listTipObjImpAtrVO){
			TipObjImpAtrDefinition tipObjImpAtrDefinition = new TipObjImpAtrDefinition();
			
			tipObjImpAtrDefinition.setTipObjImpAtr(tipObjImpAtrVO);
			tipObjImpDefinition.getListTipObjImpAtrDefinition().add(tipObjImpAtrDefinition);
		}
		
		log.debug("          ###################### --> GetDefinitionForManual 4: return");
		return tipObjImpDefinition;
	}
	
	/**
	 * Obtiene un objeto de definicion de atributo para un Tipo Objeto Imponible para el alta o modificacion.
	 * @return la definicion con sus atributos y valores en blanco.
	 * @throws Exception 
	 */	
	public TipObjImpDefinition getDefinitionForManual(Long idTipObjImpAtr) throws Exception {
		// Crea un TipObjImpDefinition
		// Obtiene el TipObjImpAtr dado el id
		// Crea un TipObjImpAtrDefinition y setea TipObjImpAtrVO 
		// Agrega a la lista de TipObjImpDefinition
		
		TipObjImpDefinition tipObjImpDefinition = new TipObjImpDefinition();
		tipObjImpDefinition.setAct(AtrValDefinition.ACT_MANUAL);  
			
		TipObjImpAtr tipObjImpAtr = TipObjImpAtr.getById(idTipObjImpAtr); 
		log.debug("          ###################### --> GetDefinitionForManual 1: gTipObjImpAtr.getById()");
		
		TipObjImpAtrVO tipObjImpAtrVO = tipObjImpAtr.toVOSinPadre(); 
		
		TipObjImpAtrDefinition tipObjImpAtrDefinition = new TipObjImpAtrDefinition();
			
		tipObjImpAtrDefinition.setTipObjImpAtr(tipObjImpAtrVO);
		tipObjImpDefinition.getListTipObjImpAtrDefinition().add(tipObjImpAtrDefinition);
		
		log.debug("          ###################### --> GetDefinitionForManual 2: return");
		return tipObjImpDefinition;
	}
	
	
	/**
	 * Obtiene un objeto de definicion de atributo para un Tipo Objeto Imponible para mostrar en la liquidacion de deuda
	 * en la web
	 * @return la definicion con sus atributos y valores en blanco.
	 * @throws Exception 
	 */
	public TipObjImpDefinition getDefinitionForWeb() throws Exception {
		// Crea un TipObjImpDefinition
		// Obtiene todos los TipObjImpAtr que esten vigentes
		// Por cada TipObjImpAtr
		//  Crea un TipObjImpAtrDefinition y setear TipObjImpAtrVO 
		//  Se setea el id del TipObjImpAtrVO al TipObjImpAtrDefinition 
		//  Agrega a TipObjImpDefinition
		
		TipObjImpDefinition tipObjImpDefinition = new TipObjImpDefinition();
		tipObjImpDefinition.setAct(AtrValDefinition.ACT_WEB);  
			
		List<TipObjImpAtr> listTipObjImpAtrWeb = new ArrayList<TipObjImpAtr>(); 
		
		log.debug("          ###################### --> getDefinitionForWeb 1: getListVigentesByIdTipObjImp()");
		listTipObjImpAtrWeb = DefDAOFactory.getTipObjImpAtrDAO().getListVigentesByIdTipObjImp(FOR_WEB,this.getId(), new Date());
		
		log.debug("          ###################### --> getDefinitionForWeb 2: listTipObjImpAtrToVOSinPadre()");
		List<TipObjImpAtrVO> listTipObjImpAtrVO = this.listTipObjImpAtrToVOSinPadre(listTipObjImpAtrWeb);
		
		log.debug("          ###################### --> getDefinitionForWeb 3: bucle seteo TipObjImpAtrDefinition");
		for (TipObjImpAtrVO tipObjImpAtrVO: listTipObjImpAtrVO){
			TipObjImpAtrDefinition tipObjImpAtrDefinition = new TipObjImpAtrDefinition();
			
			tipObjImpAtrDefinition.setTipObjImpAtr(tipObjImpAtrVO);
			tipObjImpDefinition.getListTipObjImpAtrDefinition().add(tipObjImpAtrDefinition);
		}
		
		log.debug("          ###################### --> getDefinitionForWeb 4: return");
		return tipObjImpDefinition;
	}
	
	
	/**
	 * Rebibe una lista de TipObjImpAtrVO y la pasa a BO utilizando el metodo toVOSinPadre, el cual 
	 * no incluye la conversion de TipObjImp.
	 * 
	 * @param listTipObjImpAtr
	 * @return List<TipObjImpAtrVO>
	 * @throws Exception
	 */
	private List<TipObjImpAtrVO> listTipObjImpAtrToVOSinPadre(List<TipObjImpAtr> listTipObjImpAtr) throws Exception{
		
		List<TipObjImpAtrVO> listTipObjImpAtrVO = new ArrayList<TipObjImpAtrVO>();
		
		for(Iterator it = listTipObjImpAtr.iterator(); it.hasNext();){
			TipObjImpAtr toiBO = (TipObjImpAtr) it.next();
			TipObjImpAtrVO toiVO = toiBO.toVOSinPadre();
			listTipObjImpAtrVO.add(toiVO);		
		}
		
		return listTipObjImpAtrVO;
	}
	
	public boolean isParcela() {
		boolean isParcela = false;
		if (this.getId() != null && this.getId().equals(PARCELA)) {
			isParcela = true;
		}
		return isParcela;
	}

	/**
	 * Devuelve el Atributo asociado al Tipo de Objeto Imponible que es clave. 
	 * (Si no encuentra ninguno devuelve null)
	 *
	 * @return TipObjImpAtr
	 */
	public TipObjImpAtr getAtrClave(){
		return (TipObjImpAtr) DefDAOFactory.getTipObjImpAtrDAO().getTipObjImpAtrClave(this, null);
	}
	
	/**
	 * Devuelve el Atributo asociado al Tipo de Objeto Imponible que es clave. (Excluyendo el Atributo de id pasado
	 * como parametro.)(Si no encuentra ninguno devuelve null)
	 *
	 * @return TipObjImpAtr
	 */
	public TipObjImpAtr getAtrClave(Long idExcluido){
		return (TipObjImpAtr) DefDAOFactory.getTipObjImpAtrDAO().getTipObjImpAtrClave(this, idExcluido);
	}
	
	
	/**
	 * Devuelve el Atributo asociado al Tipo de Objeto Imponible que es clave funcional. 
	 * (Si no encuentra ninguno devuelve null)
	 *
	 * @return TipObjImpAtr
	 */
	public TipObjImpAtr getAtrClaveFuncional(){
		return (TipObjImpAtr) DefDAOFactory.getTipObjImpAtrDAO().getTipObjImpAtrClaveFuncional(this, null);
	}
	
	/**
	 * Devuelve el Atributo asociado al Tipo de Objeto Imponible que es clave funcional. Excluyendo el Atributo de id pasado
	 * como parametro.)(Si no encuentra ninguno devuelve null)
	 *
	 * @return TipObjImpAtr
	 */
	public TipObjImpAtr getAtrClaveFuncional(Long idExcluido){
		return (TipObjImpAtr) DefDAOFactory.getTipObjImpAtrDAO().getTipObjImpAtrClaveFuncional(this, idExcluido);
	}
	
	/**
	 * Devuelve True o False dependiendo de si encuentra o no un Atributo asociado al Tipo de Objeto Imponible que es
	 * clave.
	 * @return
	 */
	public boolean hasAtrClave(){
		if(this.getAtrClave()!=null)
			return true;
		else
			return false;
	}
	
	/**
	 * Devuelve True o False dependiendo de si encuentra o no un Atributo asociado al Tipo de Objeto Imponible que es
	 * clave. (Excluyendo el atributo de id pasado como parametro)
	 * @return
	 */
	public boolean hasAtrClave(Long idExcluido){
		if(this.getAtrClave(idExcluido)!=null)
			return true;
		else
			return false;
	}
	
	/**
	 * Devuelve True o False dependiendo de si encuentra o no un Atributo asociado al Tipo de Objeto Imponible que es
	 * clave Funcional.
	 * @return
	 */
	public boolean hasAtrClaveFuncional(){
		if(this.getAtrClaveFuncional()!=null)
			return true;
		else
			return false;
	}
	
	/**
	 * Devuelve True o False dependiendo de si encuentra o no un Atributo asociado al Tipo de Objeto Imponible que es
	 * clave Funcional. (Excluyendo el atributo de id pasado como parametro)
	 * @return
	 */
	public boolean hasAtrClaveFuncional(Long idExcluido){
		if(this.getAtrClaveFuncional(idExcluido)!=null)
			return true;
		else
			return false;
	}
	
	public String getEsSiatView(){
		return SiNo.getById(this.getEsSiat()).getValue();
	}

	/**
	 * Retorna true si y solo si el atributo
	 * es un atributo del objeto imponible
	 */
	public boolean esAtributoObjImp(Atributo atributo) {
		for (TipObjImpAtr tipObjImpAtr: this.getListTipObjImpAtr()) {
			if (tipObjImpAtr.getAtributo().equals(atributo))
				return true;
		}
				
		return false;
	}

}
