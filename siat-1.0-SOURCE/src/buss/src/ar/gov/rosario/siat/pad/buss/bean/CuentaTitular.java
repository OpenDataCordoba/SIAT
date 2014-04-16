//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaTitularVO;
import ar.gov.rosario.siat.pad.iface.model.ObjImpVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.model.TipoTitularVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Cuenta
 * @author tecso
 *
 */
@Entity
@Table(name = "pad_cuentaTitular")
public class CuentaTitular extends BaseBO {

	private static Log log = LogFactory.getLog(CuentaTitular.class);
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idCuenta") 
	private Cuenta cuenta;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idContribuyente") 
	private Contribuyente contribuyente;
	
	@Column(name="idContribuyente", insertable=false, updatable=false)
	private Long idContribuyente;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoTitular") 
	private TipoTitular tipoTitular;
	
	@Column(name = "esTitularPrincipal")
	private Integer esTitularPrincipal;

	@Column(name = "fechaDesde")
	private Date fechaDesde;

	@Column(name = "fechaHasta")
	private Date fechaHasta;

	@Column(name = "fechaNovedad")
	private Date fechaNovedad;

    @Column(name="idCaso") 
	private String idCaso;

	@Column(name = "esAltaManual")
	private Integer esAltaManual;

	// Constructores
	public CuentaTitular() {
		super();
	}
	
	// Getters y Setters
	public Contribuyente getContribuyente() {
		return contribuyente;
	}
	public void setContribuyente(Contribuyente contribuyente) {
		this.contribuyente = contribuyente;
	}
	public Cuenta getCuenta() {
		return cuenta;
	}
	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	public Integer getEsAltaManual() {
		return esAltaManual;
	}
	public void setEsAltaManual(Integer esAltaManual) {
		this.esAltaManual = esAltaManual;
	}
	public Integer getEsTitularPrincipal() {
		return esTitularPrincipal;
	}
	public void setEsTitularPrincipal(Integer esTitularPrincipal) {
		this.esTitularPrincipal = esTitularPrincipal;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public Date getFechaNovedad() {
		return fechaNovedad;
	}
	public void setFechaNovedad(Date fechaNovedad) {
		this.fechaNovedad = fechaNovedad;
	}
	public TipoTitular getTipoTitular() {
		return tipoTitular;
	}
	public void setTipoTitular(TipoTitular tipoTitular) {
		this.tipoTitular = tipoTitular;
	}
	
	
	public Long getIdContribuyente() {
		return idContribuyente;
	}

	public void setIdContribuyente(Long idContribuyente) {
		this.idContribuyente = idContribuyente;
	}

	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	// Metodos de clase	
	public static CuentaTitular getById(Long id) {
		return (CuentaTitular) PadDAOFactory.getCuentaTitularDAO().getById(id);
	}
	
	public static CuentaTitular getByIdNull(Long id) {
		return (CuentaTitular) PadDAOFactory.getCuentaTitularDAO().getByIdNull(id);
	}
	
	public static List<CuentaTitular> getList() {
		return (ArrayList<CuentaTitular>) PadDAOFactory.getCuentaTitularDAO().getList();
	}
	
	public static List<CuentaTitular> getListActivos() {			
		return (ArrayList<CuentaTitular>) PadDAOFactory.getCuentaTitularDAO().getListActiva();
	}
	
	
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		
		//UniqueMap uniqueMap = new UniqueMap();
		
		//limpiamos la lista de errores
		clearError();
		
		this.validate();

		if (hasError()) {
			return false;
		}
		
		// Validaciones de Unicidad
		//uniqueMap.addEntity("cuenta").addEntity("contribuyente");
		
		/* desactivamos validacion de titular duplicado.
		 * por validacion incorrecta.
		 * La validacion tendria que ser el mismo cuentaTitular solapado en los rangos.
		if(!GenericDAO.checkIsUnique(this, uniqueMap)){
			addRecoverableError(PadError.CUENTATITULAR_TITULAR_DUPLICADO);
		}
		*/
		
		/* Valida que no exista un registro para el mismo titular y cuenta vigente a la fecha desde indicada
		 * (primera aproximacion a la validacion de solapamiento por rango. Se solicito en el Mantis 4837: Validaciones en el alta de titulares de una cuenta.) 
		 */
		if(PadDAOFactory.getCuentaTitularDAO().existeComoTitularVigente(getCuenta(), getContribuyente(), getFechaDesde())){
			addRecoverableError(PadError.CUENTATITULAR_REGISTRO_CONFLICTO);			
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
		if(this.getCuenta() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTATITULAR_CUENTA);
		}		
		if(this.getContribuyente() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTATITULAR_CONTRIBUYENTE);
		}
		if(this.getTipoTitular() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTATITULAR_TIPO_TITULAR);
		}		
		if(this.getFechaDesde() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTATITULAR_FECHA_DESDE);
		}		
		if(this.getFechaNovedad() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTATITULAR_FECHA_NOVEDAD);
		}
		if(this.getEsAltaManual() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTATITULAR_ES_ALTA_MANUAL);
		}
		if(!SiNo.getEsValido(this.getEsTitularPrincipal())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTATITULAR_ES_TITULAR_PRINCIPAL);
		}
	
		if (hasError()) {
			return false;
		}

		/* fedel - 20081027 - sacamos la validacion. Las novedades de catastro mandan titulares mas viejos que las cuentas.!
		 *
		if(DateUtil.isDateAfter(this.getCuenta().getFechaAlta(), getFechaDesde())){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
				PadError.CUENTA_FECHAALTA, PadError.CUENTATITULAR_FECHA_DESDE);			
		}
		*/
		
		/* catastro informa bajas y modificacion con fechas desde muy antiguas, esto
		 * hace que esta validacion se cumpla y no se puedan procesar novedades.
		 * Por eso la desactivamos.
		if(this.getFechaHasta() != null && DateUtil.isDateAfter(getFechaDesde(), getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
				PadError.CUENTATITULAR_FECHA_DESDE, PadError.CUENTATITULAR_FECHA_HASTA);			
		}
		*/
		

		return !hasError();
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
				
		return true;
	}

	public CuentaTitularVO toVOForContribuyente() throws Exception{

		// contribuyente, cuenta y tipoTitular		
		CuentaTitularVO cuentaTitularVO = (CuentaTitularVO) this.toVO(1);
		// objeto imponible
		if(this.getCuenta().getObjImp()!=null){
			ObjImpVO objImpVO = (ObjImpVO) this.getCuenta().getObjImp().toVO(0);
			cuentaTitularVO.getCuenta().setObjImp(objImpVO);
		}
		// persona;
		this.getContribuyente().loadPersonaFromMCR(); // carga los datos de persona desde MCR
		PersonaVO personaVO = (PersonaVO) this.getContribuyente().getPersona().toVO(2);
		cuentaTitularVO.getContribuyente().setPersona(personaVO);
		// recurso
		RecursoVO recursoVO = (RecursoVO) this.getCuenta().getRecurso().toVO(0);
		cuentaTitularVO.getCuenta().setRecurso(recursoVO);
		
		return cuentaTitularVO;
	}

	public CuentaTitularVO toVOForCuenta() throws Exception{

		// cuentaTitular de nivel cero
		CuentaTitularVO cuentaTitularVO = (CuentaTitularVO) this.toVO(0);
		// tipoTitular
		cuentaTitularVO.setTipoTitular((TipoTitularVO) this.getTipoTitular().toVO(0));
		// contribuyente
		cuentaTitularVO.setContribuyente((ContribuyenteVO) this.getContribuyente().toVO(0));
		// persona
		this.getContribuyente().loadPersonaFromMCR(); // carga los datos de persona desde MCR
		PersonaVO personaVO = (PersonaVO) this.getContribuyente().getPersona().toVO(2);
		cuentaTitularVO.getContribuyente().setPersona(personaVO);
		
		return cuentaTitularVO;
	}

	/**
	 * Desactiva esta Cuenta Titular poniendo el fechaHasta con el valor del parametro
	 * Verifica si debe dar de baja CuentaTitular para las cuentas secundarias relacionadas y actua en consecuencia.
	 * Si la cuentaTitular es titular principal, 
	 * reasigna como titular principal de la cuenta al 1er cuentaTitular vigente a la fecha hasta.
	 * También realiza la reasignacion de titular principal de las cuentas secundarias afectadas.
	 * @param fechaHasta fecha hasta a setear en esta cuenta titular.
	 * @throws Exception
	 */
	public void desactivar(Date fechaHasta) throws Exception {
		
		this.setFechaHasta(fechaHasta);

		if(!this.validateDesactivar()){
			return;
		}
		
		if (SiNo.SI.getId() == this.getEsTitularPrincipal()){
			this.reasigAutoNuevoTitularPrinc(fechaHasta);
		}
		
		PadDAOFactory.getCuentaTitularDAO().update(this);
		
		// Analizamos impacto en Cuentas Secundarias
		if(SiNo.SI.getId().equals(this.getCuenta().getRecurso().getEsPrincipal())) {
			List <Cuenta> listCuentaSecundaria = this.getCuenta().getObjImp().getListCuentaSecundariaActiva();
			if(!ListUtil.isNullOrEmpty(listCuentaSecundaria)){
				for (Cuenta cuentaSec : listCuentaSecundaria) {
					if (SiNo.SI.getId().equals(cuentaSec.getRecurso().getModiTitCtaPorPri())) {
						CuentaTitular cuentaTitularSec = cuentaSec.getCuentaTitularByContribuyente(this.getContribuyente());
						if(cuentaTitularSec!=null){
							cuentaTitularSec.setFechaHasta(fechaHasta);
							if (SiNo.SI.getId().equals(cuentaTitularSec.getEsTitularPrincipal())) {
								cuentaTitularSec.reasigAutoNuevoTitularPrinc(fechaHasta);
							}
							PadDAOFactory.getCuentaDAO().update(cuentaTitularSec);
						}
					}
				}
			}
		}
	}

	
	/**
	 * Reasignacion de un nuevo titular principal
	 * Toma la primer cuenta titular de la lista de de cuentas titulares vigentes a la fecha hasta de la cuenta
	 * y la asigna como titular principal
	 * 
	 * @param fechaHasta 
	 */
	private void reasigAutoNuevoTitularPrinc(Date fechaHasta){
		
		// Si la cuenta que desactivo es titular principal:
		// obtengo la 1er cuenta titular de la lista de cuentas titulares vigentes de la cuenta a la fecha hasta
		// y la establezco como titular principal
		if (SiNo.SI.getId() == this.getEsTitularPrincipal()){
			List listCtaTitularVig = this.getCuenta().getListCuentaTitularVigentes(fechaHasta);
			if (listCtaTitularVig.size() >= 1){
				CuentaTitular ctaTiVig = (CuentaTitular) listCtaTitularVig.get(0);
				ctaTiVig.setEsTitularPrincipal(SiNo.SI.getId());
			}
		}

		
	}
	
	/**
	 * Valida la desactivacion del Recurso
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
	
		if (getFechaHasta()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTATITULAR_FECHA_HASTA);
		}
		
		// Valida que la Fecha Desde no sea mayor que la fecha Hasta, y que la fecha Hasta no sea mayor que la actual
		if(this.fechaHasta!=null){
			// Sacamos validaciones porque traen muchos problemas a las novedades que envia catastro.
			// Ojo: Si se activan se rechazan muchas novedades. (Estas validaciones de fechas pasadas
			// tambien se sacaron de baja objimp y altaymodi de cta titular) 
			
			/*
			if(!DateUtil.isDateBeforeOrEqual(this.fechaDesde, this.fechaHasta)){
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, PadError.CUENTATITULAR_FECHA_DESDE, PadError.CUENTATITULAR_FECHA_HASTA);
			}			
			if(!DateUtil.isDateAfterOrEqual(new Date(), this.fechaHasta)){
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, PadError.CUENTATITULAR_FECHA_HASTA, BaseError.MSG_FECHA_ACTUAL);
			}
			*/		
		}
	
		if (hasError()) {
			return false;
		}
	
		return !hasError();
	}

	
	/**
	 * Verifica si las 2 lista pasadas como parametros son iguales
	 * @param listCuTit
	 * @param listCueTi2
	 * @return
	 */
	public static boolean compararListaTit(List<CuentaTitular> listCueTit,List<CuentaTitular> listCueTit2) {
		log.debug("compararListaTit - enter");
		
		log.debug("Lista 1:");
		for(CuentaTitular cuentaTit: listCueTit){
			log.debug("id cuentaTit:"+cuentaTit.getId());
		}
		
		log.debug("Lista 2:");
		for(CuentaTitular cuentaTit: listCueTit2){
			log.debug("id cuentaTit:"+cuentaTit.getId());
		}
		
		if(listCueTit.size()!=listCueTit2.size())
			return false;
		
		for(CuentaTitular cuentatit: listCueTit){
			if(!listCueTit2.contains(cuentatit))
				return false;
		}
		
		log.debug("compararListaTit - exit");
		return true;
	}

	/**
	 * Obtiene una lista de cuentasTitulares para el contribuyente pasado como parametro y excluye de la lista 
	 * que retorna, la cuenta pasada como parametro
	 * @param contribuyente
	 * @param cuentaExcluir  - Esta cuenta no se incluira en la lista que retorna
	 * @return
	 */
	public static List<CuentaTitular> getList(Contribuyente contribuyente,Cuenta cuentaExcluir) {
		return PadDAOFactory.getCuentaTitularDAO().getList(contribuyente, cuentaExcluir);
	}
	
	public static List<CuentaTitular> getListByContribuyente(Contribuyente contribuyente) {
		return PadDAOFactory.getCuentaTitularDAO().getListByContribuyente(contribuyente);
	}
}
