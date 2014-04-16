//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a OpeInv (Operativo de Investigacion)
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_opeinv")
public class OpeInv extends BaseBO {
	
	@Transient
	private Log log = LogFactory.getLog(OpeInv.class);

	private static final long serialVersionUID = 1L;

	@Column(name = "desOpeInv")
	private String desOpeInv;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPlanFiscal")
	private PlanFiscal planFiscal;

	@Column(name = "fechaInicio")
	private Date fechaInicio;

	@Column(name = "observacion")
	private String observacion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idEstOpeInv")
	private EstOpeInv estOpeInv;

	// <#Propiedades#>

	// Constructores
	public OpeInv() {
		super();
		// Seteo de valores default
	}

	public OpeInv(Long id) {
		super();
		setId(id);
	}

	// Metodos de Clase
	public static OpeInv getById(Long id) {
		return (OpeInv) EfDAOFactory.getOpeInvDAO().getById(id);
	}

	public static OpeInv getByIdNull(Long id) {
		return (OpeInv) EfDAOFactory.getOpeInvDAO().getByIdNull(id);
	}

	public static List<OpeInv> getList() {
		return (List<OpeInv>) EfDAOFactory.getOpeInvDAO().getList();
	}

	public static List<OpeInv> getListActivos() {
		return (List<OpeInv>) EfDAOFactory.getOpeInvDAO().getListActiva();
	}

	public static List<OpeInv> getListActivosByPlan(PlanFiscal plan) {
		return (List<OpeInv>) EfDAOFactory.getOpeInvDAO().getListActivosByPlan(plan);
	}

	//Metodos de instancia
	public List<OpeInvBus> getListOpeInvBus(){
		return (List<OpeInvBus>)EfDAOFactory.getOpeInvBusDAO().getListByOpeInv(this);
	}

	// Getters y setters

	public String getDesOpeInv() {
		return desOpeInv;
	}

	public void setDesOpeInv(String desOpeInv) {
		this.desOpeInv = desOpeInv;
	}

	public PlanFiscal getPlanFiscal() {
		return planFiscal;
	}

	public void setPlanFiscal(PlanFiscal planFiscal) {
		this.planFiscal = planFiscal;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public EstOpeInv getEstOpeInv() {
		return estOpeInv;
	}

	public void setEstOpeInv(EstOpeInv estOpeInv) {
		this.estOpeInv = estOpeInv;
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

		// Valida si tiene referencias a opeInvCon 
		if(GenericDAO.hasReference(this, OpeInvCon.class, "opeInv")){
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					EfError.OPEINV_LABEL , EfError.OPEINVCON_LABEL);
		}

		// Valida si tiene referencias a busquedas masivas
		if(GenericDAO.hasReference(this, OpeInvBus.class, "opeInv")){
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					EfError.OPEINV_LABEL , EfError.OPEINVBUS_LABEL);
		}
		
		
		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones

		if (StringUtil.isNullOrEmpty(getDesOpeInv())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					EfError.OPEINV_DESOPEINV_LABEL);
		}

		if (fechaInicio == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					EfError.OPEINV_FECINICIO_LABEL);
		}

		if (planFiscal == null || planFiscal.getId().longValue() <= 0)
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					EfError.PLANFISCAL_LABEL);

		if (hasError()) {
			return false;
		}

		// Validaciones de unique
		/*
		 * UniqueMap uniqueMap = new UniqueMap();
		 * uniqueMap.addString("codOpeInv"); if(!GenericDAO.checkIsUnique(this,
		 * uniqueMap)) { addRecoverableError(BaseError.MSG_CAMPO_UNICO,
		 * EfError.OPEINV_CODOPEINV); }
		 */
		return true;
	}

	// Metodos de negocio

	/**
	 * Devuelve la lista de los ids de opeInvCon que tiene el operativo.
	 */
	public List<Long> getlistIdsOpeInvCon(){
		List<OpeInvCon> listOpeInvCon = getListOpeInvCon();
		List<Long> listIds = new ArrayList<Long>();
		for(OpeInvCon opeInvCon:listOpeInvCon){
			listIds.add(opeInvCon.getId());
		}
		return listIds;
	}

	/**
	 * Devuelve la lista de los ids de los contribuyentes incluidos en el operativo (de la lista de opeInvCon).
	 */
	public List<Long> getlistIdsContribuyentes(){
		List<OpeInvCon> listOpeInvCon = getListOpeInvCon();
		List<Long> listIds = new ArrayList<Long>();
		for(OpeInvCon opeInvCon:listOpeInvCon){
			listIds.add(opeInvCon.getIdContribuyente());
		}
		return listIds;
	}

	/**
	 * Devuelve la lista de los ids de las cuentas seleccionadas del operativo (la cuenta seleccionada de cada registro de opeInvCon) 
	 * @return
	 */
	public List<Long> getListIdsCuentasSelec(){
		List<OpeInvCon> listOpeInvCon = getListOpeInvCon();
		List<Long> listIds = new ArrayList<Long>();
		for(OpeInvCon opeInvCon:listOpeInvCon){
			OpeInvConCue opeInvconCueCuentaSelec = opeInvCon.getOpeInvConCueCuentaSelec();
			if(opeInvconCueCuentaSelec!=null)
				listIds.add(opeInvconCueCuentaSelec.getCuenta().getId());
		}
		return listIds;		
	}
	/**
	 * Verifica si el operativo contiene al contribuyente pasado como parametro
	 * @param idPersonaSelec
	 * @return
	 */
	public boolean contieneContribuyente(Long idPersonaSelec) {
		List<OpeInvCon> listOpeInvCon = getListOpeInvCon();
		for(OpeInvCon opeInvCon:listOpeInvCon){
			if(opeInvCon.getIdContribuyente().equals(idPersonaSelec))
				return true;
		}
		return false;
	}

	/**
	 * Verifica si el operativo contiene un registro de opeInvCon con la cuenta seleccionada 
	 * (en la lista de opeInvConCue) igual a la pasada como parametro, excluyendo del analisis al opeInvCon con el
	 * id pasado como parametro
	 * @param cuentaParam
	 * @param idOpeInvConExcluir - Si es null no se tiene en cuenta
	 * @return
	 */
/*	public boolean contieneCuentaSeleccionada(Cuenta cuentaParam, Long idOpeInvConExcluir) {
		log.debug("contieneCuentaSeleccionada - enter");
		OpeInvCon opeInvconCuentaSelec = getOpeInvConCuentaSelec(cuentaParam);
		if(opeInvconCuentaSelec!=null){
			if(idOpeInvConExcluir!=null){
				if(opeInvconCuentaSelec.getId().equals(idOpeInvConExcluir))
					return false;
				else
					return true;				
			}
		}
		
		log.debug("contieneCuentaSeleccionada - exit");
		return false;
	}
*/
	/**
	 * Devuelve el opeInvCon del operativo, que tiene como cuenta seleccionada a la cuenta pasada como parametro,
	 * excluyendo del analisis al opeInvCon con el id pasado como parametro 
	 * @param cuentaParam
	 * @param idOpeInvConExcluir - Si es null no se tiene en cuenta
	 * @return null si no encuentra ningun registro que coincida
	 */
	public OpeInvCon getOpeInvConCuentaSelec(Cuenta cuentaParam, Long idOpeInvConExcluir){
		List<OpeInvCon> listOpeInvCon = getListOpeInvCon();
		for(OpeInvCon opeInvCon:listOpeInvCon){
			OpeInvConCue opeInvconCueCuentaSelec = opeInvCon.getOpeInvConCueCuentaSelec();
			if(opeInvconCueCuentaSelec!=null && 
									opeInvconCueCuentaSelec.getCuenta().getId().equals(cuentaParam.getId())){
				if(idOpeInvConExcluir!=null){
					if(opeInvCon.getId().equals(idOpeInvConExcluir))
						return null;
					else
						return opeInvCon;				
				}

				return opeInvCon;				
			}
		}
		
		log.debug("contieneCuentaSeleccionada - exit");
		return null;		
	}
	
	/**
	 * Devuelve la lista de los OpeInvCon que tiene el operativo.
	 */
	public List<OpeInvCon> getListOpeInvCon(){
		return OpeInvCon.getListByOpeInv(this);
	}
	
	/**
	 * Activa el OpeInv. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getOpeInvDAO().update(this);
	}

	/**
	 * Desactiva el OpeInv. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getOpeInvDAO().update(this);
	}

	/**
	 * Valida la activacion del OpeInv
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
	 * Valida la desactivacion del OpeInv
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	// ---> ABM HisEstOpeInv
	public HisEstOpeInv createHisEstOpeInv(HisEstOpeInv hisEstOpeInv)
			throws Exception {
		// Validaciones de negocio
		if (!hisEstOpeInv.validateCreate()) {
			return hisEstOpeInv;
		}
		EfDAOFactory.getHisEstOpeInvDAO().update(hisEstOpeInv);
		return hisEstOpeInv;
	}

	public HisEstOpeInv updateHisEstOpeInv(HisEstOpeInv hisEstOpeInv)
			throws Exception {

		// Validaciones de negocio
		if (!hisEstOpeInv.validateUpdate()) {
			return hisEstOpeInv;
		}
		EfDAOFactory.getHisEstOpeInvDAO().update(hisEstOpeInv);

		return hisEstOpeInv;
	}

	public HisEstOpeInv deleteHisEstOpeInv(HisEstOpeInv hisEstOpeInv)
			throws Exception {

		// Validaciones de negocio
		if (!hisEstOpeInv.validateDelete()) {
			return hisEstOpeInv;
		}

		EfDAOFactory.getHisEstOpeInvDAO().delete(hisEstOpeInv);

		return hisEstOpeInv;
	}
	// <--- ABM HisEstOpeInv

	public void deleteHistoricos() {
		EfDAOFactory.getOpeInvDAO().deleteHisEstOpeInv(this.getId());
	}

	// ---> ABM OpeInvCon
	
	/** Crea el OpeInvCon y el historico */
	public OpeInvCon createOpeInvCon(OpeInvCon opeInvCon, String obs) throws Exception {
		// Validaciones de negocio
		if (!opeInvCon.validateCreate()) {
			return opeInvCon;
		}
		EfDAOFactory.getOpeInvConDAO().update(opeInvCon);
		
		// Crea el historico
		HisEstOpeInvCon hisEstOpeInvCon = new HisEstOpeInvCon();
		hisEstOpeInvCon.setObservacion(obs);
		hisEstOpeInvCon.setOpeInvCon(opeInvCon);
		hisEstOpeInvCon.setEstadoOpeInvCon(opeInvCon.getEstadoOpeInvCon());
		
		EfDAOFactory.getHisEstOpeInvConDAO().update(hisEstOpeInvCon);
		
		return opeInvCon;
	}
	
	public OpeInvCon updateOpeInvCon(OpeInvCon opeInvCon, String obs) throws Exception {

		// Validaciones de negocio
		if (!opeInvCon.validateUpdate()) {
			return opeInvCon;
		}
		EfDAOFactory.getOpeInvConDAO().update(opeInvCon);

		// Crea el historico
		if(obs!=null){
			HisEstOpeInvCon hisEstOpeInvCon = new HisEstOpeInvCon();
			hisEstOpeInvCon.setObservacion(obs);
			hisEstOpeInvCon.setOpeInvCon(opeInvCon);
			hisEstOpeInvCon.setEstadoOpeInvCon(opeInvCon.getEstadoOpeInvCon());
			
			EfDAOFactory.getHisEstOpeInvConDAO().update(hisEstOpeInvCon);
		}
		
		return opeInvCon;
	}

	public OpeInvCon deleteOpeInvCon(OpeInvCon opeInvCon) throws Exception {

		// Validaciones de negocio
		if (!opeInvCon.validateDelete()) {
			return opeInvCon;
		}

		EfDAOFactory.getOpeInvConDAO().delete(opeInvCon);

		return opeInvCon;
	}
	// <--- ABM OpeInvCon

	// ---> ABM OpeInvBus
	public OpeInvBus createOpeInvBus(OpeInvBus opeInvBus) throws Exception {
		// Validaciones de negocio
		if (!opeInvBus.validateCreate()) {
			return opeInvBus;
		}
		EfDAOFactory.getOpeInvBusDAO().update(opeInvBus);
				
		return opeInvBus;
	}
	
	public OpeInvBus updateOpeInvBus(OpeInvBus opeInvBus) throws Exception {

		// Validaciones de negocio
		if (!opeInvBus.validateUpdate()) {
			return opeInvBus;
		}
		EfDAOFactory.getOpeInvBusDAO().update(opeInvBus);
	
		return opeInvBus;
	}

	public OpeInvBus deleteOpeInvBus(OpeInvBus opeInvBus) throws Exception {

		// Validaciones de negocio
		if (!opeInvBus.validateDelete()) {
			return opeInvBus;
		}

		EfDAOFactory.getOpeInvBusDAO().delete(opeInvBus);
		
		return opeInvBus;
	}


	/**
	 * Devuelve las busquedas asociadas, del tipo AGREGAR
	 * @return
	 */
	public List<OpeInvBus> getListOpeInvBusAgregar() {
		List<OpeInvBus> listOpeInvBus = new ArrayList<OpeInvBus>();
		for(OpeInvBus opeInvBus: getListOpeInvBus()){
			if(opeInvBus.getTipBus().equals(OpeInvBus.TIPO_AGREGAR))
				listOpeInvBus.add(opeInvBus);
		}
		return listOpeInvBus;
	}



		
	// <--- ABM OpeInvBus	
}
