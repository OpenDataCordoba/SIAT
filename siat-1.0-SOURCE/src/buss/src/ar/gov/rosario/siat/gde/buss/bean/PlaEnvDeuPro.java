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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuVO;
import ar.gov.rosario.siat.gde.iface.model.EstPlaEnvDeuPrVO;
import ar.gov.rosario.siat.gde.iface.model.HistEstPlaEnvDPVO;
import ar.gov.rosario.siat.gde.iface.model.LiqAtrValorVO;
import ar.gov.rosario.siat.gde.iface.model.PlaEnvDeuProPrint;
import ar.gov.rosario.siat.gde.iface.model.PlaEnvDeuProVO;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.CuentaTitular;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.buss.bean.PersonaFacade;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.TablaVO;

/**
 * Bean correspondiente a la Planilla que agrupa la Deuda enviada a un Procurador
 * 
 * @author tecso
 */

@Entity
@Table(name = "gde_plaEnvDeuPro")
public class PlaEnvDeuPro extends BaseBO {

	@Transient
	Logger log = Logger.getLogger(PlaEnvDeuPro.class);
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "plaEnvDeuPro";

	@Column(name = "anioPlanilla")
	private Integer anioPlanilla;    // NOT NULL,

	@Column(name = "nroPlanilla")
	private Long nroPlanilla;        // NOT NULL,

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idProcurador") 
	private Procurador procurador;           // NOT NULL
	
	@Column(name = "fechaEnvio")
	private Date fechaEnvio;         // DATETIME YEAR TO DAY NOT NULL,

	@Column(name = "fechaRecepcion")
	private Date fechaRecepcion;     // DATETIME YEAR TO DAY,

	@Column(name = "fechaHabilitacion")
	private Date fechaHabilitacion;         // DATETIME YEAR TO DAY NOT NULL,

	@Column(name = "observaciones")
	private String observaciones;

	@ManyToOne( fetch=FetchType.LAZY) 
	@JoinColumn(name="idProcesoMasivo") 
	private ProcesoMasivo procesoMasivo;   // es nuleable

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idEstPlaEnvDeuPr") 
	private EstPlaEnvDeuPr estPlaEnvDeuPr; // NOT NULL
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idPlaEnvDeuPro")
	//@OrderBy(clause="numero")
	private List<ConstanciaDeu> listConstanciaDeu;

	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idPlaEnvDeuPro")
	@OrderBy("fechaDesde DESC")
	private List<HistEstPlaEnvDP> listHistEstPlaEnvDP;

	@Column(name = "totalRegistros")
	private Long totalRegistros;     
	
	// importeTotal
	@Column(name = "importeTotal")
	private Double importeTotal;
	
	// cantidadCuentas
	@Column(name = "cantidadCuentas")
	private Long cantidadCuentas;

    @Column(name="idCaso") 
	private String idCaso;


	// Constructores
	public PlaEnvDeuPro(){
		super();
	}
	
	// Getters y Setters
	public Integer getAnioPlanilla() {
		return anioPlanilla;
	}
	public void setAnioPlanilla(Integer anioPlanilla) {
		this.anioPlanilla = anioPlanilla;
	}
	public ProcesoMasivo getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivo procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
	public EstPlaEnvDeuPr getEstPlaEnvDeuPr() {
		return estPlaEnvDeuPr;
	}
	public void setEstPlaEnvDeuPr(EstPlaEnvDeuPr estPlaEnvDeuPr) {
		this.estPlaEnvDeuPr = estPlaEnvDeuPr;
	}
	public Date getFechaEnvio() {
		return fechaEnvio;
	}
	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}
	public Date getFechaRecepcion() {
		return fechaRecepcion;
	}
	public void setFechaRecepcion(Date fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}
	public Long getNroPlanilla() {
		return nroPlanilla;
	}
	public void setNroPlanilla(Long nroPlanilla) {
		this.nroPlanilla = nroPlanilla;
	}
	public Procurador getProcurador() {
		return procurador;
	}
	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}
	public Long getCantidadCuentas() {
		return cantidadCuentas;
	}
	public void setCantidadCuentas(Long cantidadCuentas) {
		this.cantidadCuentas = cantidadCuentas;
	}
	public Double getImporteTotal() {
		return importeTotal;
	}
	public void setImporteTotal(Double importeTotal) {
		this.importeTotal = importeTotal;
	}
	public Long getTotalRegistros() {
		return totalRegistros;
	}
	public void setTotalRegistros(Long totalRegistros) {
		this.totalRegistros = totalRegistros;
	}
	public List<ConstanciaDeu> getListConstanciaDeu() {
		return listConstanciaDeu;
	}
	public void setListConstanciaDeu(List<ConstanciaDeu> listConstanciaDeu) {
		this.listConstanciaDeu = listConstanciaDeu;
	}
	public Date getFechaHabilitacion() {
		return fechaHabilitacion;
	}

	public void setFechaHabilitacion(Date fechaHabilitacion) {
		this.fechaHabilitacion = fechaHabilitacion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	
	public List<HistEstPlaEnvDP> getListHistEstPlaEnvDP() {
		return listHistEstPlaEnvDP;
	}

	public void setListHistEstPlaEnvDP(List<HistEstPlaEnvDP> listHistEstPlaEnvDP) {
		this.listHistEstPlaEnvDP = listHistEstPlaEnvDP;
	}

	// Metodos de Clase
	public static PlaEnvDeuPro getById(Long id) {
		return (PlaEnvDeuPro) GdeDAOFactory.getPlaEnvDeuProDAO().getById(id);  
	}
	 
	public static PlaEnvDeuPro getByIdNull(Long id) {
		return (PlaEnvDeuPro) GdeDAOFactory.getPlaEnvDeuProDAO().getByIdNull(id);
	}
	
	public static List<PlaEnvDeuPro> getList() {
		return (ArrayList<PlaEnvDeuPro>) GdeDAOFactory.getPlaEnvDeuProDAO().getList();
	}
	
	public static List<PlaEnvDeuPro> getListActivos() {			
		return (ArrayList<PlaEnvDeuPro>) GdeDAOFactory.getPlaEnvDeuProDAO().getListActiva();
	}
	
	public static Long getNextNroPlanilla() {			
		return GdeDAOFactory.getPlaEnvDeuProDAO().getNextVal();
	}


	/**
	 * Obtiene el PlaEnvDeuPro para un Procurador y un ProcesoMasivo
	 * En caso de no existir devuelve null
	 * @param  procurador
	 * @param  procesoMasivo
	 * @return PlaEnvDeuPro
	 */
	public static PlaEnvDeuPro getPlaEnvDeuProByProcProMas(Procurador procurador, ProcesoMasivo procesoMasivo) {			
		return GdeDAOFactory.getPlaEnvDeuProDAO().getPlaEnvDeuProByProcProMas(procurador, procesoMasivo);
	}
	

	// Metodos de Instancia
	
	/** Realiza la habilitacion de la planilla:
	 * <br> Verifica si todas las constancias que tiene estan en estado EMITIDA o RECOMPUESTA
	 * <br> Luego habilita cada Constancia (TODAS las que contiene)<br>
	 * Luego setea la fechaHabilitacion
	 * Luego graba la planilla con estado HABILITADA
	 * <br>Si no se puede habilitar, se agrega un msj.
	 * @throws Exception */
	public void habilitar() throws Exception{
		int c = 0;
	
		if(validarConstanciasParaHabilitacion()){
			ArrayList<Long> ids = new ArrayList<Long>();
			for(ConstanciaDeu constancia: listConstanciaDeu){
				ids.add(constancia.getId());
			}
			
			for(Long id : ids){
				// La habilita si no esta ANULADA
				ConstanciaDeu constancia = ConstanciaDeu.getById(id);
				if(!constancia.getEstConDeu().getId().equals(EstConDeu.ID_ANULADA))
					constancia.habilitar(HistEstConDeu.LOG_HABILITADA_POR_HABILITACION_DE_PLANILLA);
			
				// Commit cada 50 constancias
				if (++c % 50 == 0) {
						log.debug("habilitar(): Commit " + c + " constancias. Reiniciando Session Hibernate...");
						SiatHibernateUtil.currentSession().getTransaction().commit();
						SiatHibernateUtil.closeSession();
						SiatHibernateUtil.currentSession().beginTransaction();
						SiatHibernateUtil.currentSession().refresh(this);
				}
			}
			setEstPlaEnvDeuPr(EstPlaEnvDeuPr.getById(EstPlaEnvDeuPr.ID_HABILITADA));
			setFechaHabilitacion(new Date());
			GdeGDeudaJudicialManager.getInstance().updatePlaEnvDeuPro(this);
		}else{
			addMessage(GdeError.PLA_ENV_DEU_PRO_MSG_CONSTANCIAS_NO_PREPARADAS_PARA_HABILITACION);
		}
	}
	
	/** Verifica si TODAS las constancias de deuda que contiene estan en estado EMITIDA o RECOMPUESTA.
	 * Las ANULADA no las tiene en cuenta, total no se habilitan*/
	public boolean validarConstanciasParaHabilitacion() throws Exception {
		int c = 0;
		ArrayList<Long> ids = new ArrayList<Long>();
		for(ConstanciaDeu constancia: listConstanciaDeu){
			ids.add(constancia.getId());
		}

		for(Long id : ids) {
			ConstanciaDeu constancia = ConstanciaDeu.getById(id);
			if(!constancia.getEstConDeu().getId().equals(EstConDeu.ID_EMITIDA) && 
				!constancia.getEstConDeu().getId().equals(EstConDeu.ID_RECOMPUESTA) &&
				!constancia.getEstConDeu().getId().equals(EstConDeu.ID_ANULADA)) {				
				return false;
			}
			
			if (++c % 50 == 0) {
				log.debug("validarHabilitar(): " + c + " constancias. Reiniciando Session Hibernate...");
				SiatHibernateUtil.currentSession().getTransaction().commit();
				SiatHibernateUtil.closeSession();
				SiatHibernateUtil.currentSession().beginTransaction();
				SiatHibernateUtil.currentSession().refresh(this);
			}
		}
		return true;
	}

	/**
	 * Verifica si Todas las Constancias de la Planilla estan en estado Cancelado.
	 * 
	 * @return boolean
	 */
	public boolean validarConstanciasCanceladas(){
		for(ConstanciaDeu constancia: listConstanciaDeu){
			if(constancia.getEstConDeu().getId().longValue()!=EstConDeu.ID_CANCELADA.longValue())
			  return false;
		}
		return true;
	}
	
	/**
	 * Recorre las constancias que posee y devuelve el String del archivo que se genera
	 * @return
	 * @throws Exception 
	 */
	public String getArchivoCD() throws Exception{
		String contenido = "";
		if(listConstanciaDeu!=null){
			for(ConstanciaDeu c: listConstanciaDeu){
				contenido +=c.getStringArchivoCD()+"\n";
			}
		}
		return contenido;
	}
	
	// Validaciones
	public boolean validateCreate() throws Exception {
		//limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		//limpiamos la lista de errores
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
		
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {

		// validaciones comunes 

		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio

	/**
	 * Obtiene la Constancia de Deuda para la Cuenta de la Planilla de Envio de Deuda a Procuradores
	 * @param  cuenta
	 * @return ConstanciaDeu
	 */
	public ConstanciaDeu getConstanciaDeuByCuenta(Cuenta cuenta) {			
		return GdeDAOFactory.getConstanciaDeuDAO().getConstanciaDeuByPlaEnvDeuProCuenta(this, cuenta);
	}
	
	// --> ABM ConstanciaDeu
	/**
	 * Realiza la creacion de la Constancia de la Deuda y la creacion del Historico de la misma 
	 * a partir de la cuenta y la observacion
	 * 
	 * @param  cuenta
	 * @param  observacion
	 * @return ConstanciaDeu
	 * @throws Exception
	 */
	public ConstanciaDeu createConstanciaDeu(Cuenta cuenta, String observacion, List<ConDeuTit> listConDeuTit) throws Exception{
		
		ConstanciaDeu constanciaDeu = new ConstanciaDeu();
		constanciaDeu.setNumero(ConstanciaDeu.getNextNumero()); // obtiene el nro de un sequence 
		constanciaDeu.setAnio(this.getAnioPlanilla());
		constanciaDeu.setProcurador(this.getProcurador());
		constanciaDeu.setCuenta(cuenta);
		constanciaDeu.setEstConDeu(EstConDeu.getById(EstConDeu.ID_EMITIDA)); // es emitida y no habilitada
//			constanciaDeu.setDomicilio(cuenta.getDomicilioEnvio());
		constanciaDeu.setDesDomEnv(cuenta.getDesDomEnv());
		
		// Setea la ubicacion del objImp de la cuenta
		String ubicacion = "";
		LiqDeudaBeanHelper liqDeudaBeanHelper = new LiqDeudaBeanHelper(cuenta);
		List<LiqAtrValorVO> listAtrValor = liqDeudaBeanHelper.getListAtrObjImp(false);
		for(LiqAtrValorVO atrValorVO: listAtrValor){
			if(atrValorVO.getKey().equals(Atributo.COD_UBICACION)){
				ubicacion = atrValorVO.getValue();					
				break;
			}
		}
		constanciaDeu.setDesDomUbi(ubicacion);
		
		constanciaDeu.setProcesoMasivo(this.getProcesoMasivo());
		constanciaDeu.setPlaEnvDeuPro(this);
		constanciaDeu.setObservacion(observacion);
		
		createConstanciaDeu(constanciaDeu);
		
		if(constanciaDeu.hasError()){
			return constanciaDeu;
		}
		
		// Se setea la propiedad desTitulares de la constancia
		String desTitulares = "";
		boolean esPrimero = true;
		Map<String, Boolean> mapTitularCargado = new HashMap<String, Boolean>();
		for(CuentaTitular cuentaTitular: cuenta.getListCuentaTitularVigentes(new Date())){
			constanciaDeu.cargarTitular(cuentaTitular);
			mapTitularCargado.put(cuentaTitular.getContribuyente().getId().toString(), true);
			
			Contribuyente contribuyente = cuentaTitular.getContribuyente();
			if(contribuyente!=null && contribuyente.getPersona()!=null){
				// seteo de la persona usando el getByIdLight
				Long idPersona = contribuyente.getPersona().getId();
				Persona personaLight = Persona.getByIdLight(contribuyente.getPersona().getId());
				if (personaLight == null){
					log.error("Persona no encontrada: " + idPersona + ". De la cuentaTitular " + cuentaTitular.getId());
					constanciaDeu.addRecoverableError(GdeError.CONSTANCIADEU_CUENTA_TITULAR_PERS_NO_ENC);
					return constanciaDeu;
				}
				contribuyente.setPersona(personaLight);
				
				desTitulares += esPrimero?"":" - ";
				desTitulares += personaLight.getRepresent();
				
				esPrimero=false;
			}
			
		}
		
		if(listConDeuTit != null){
			// Cargar titulares pasados como parametro (adicionales)
			for(ConDeuTit conDeuTitAd: listConDeuTit){
				if(!mapTitularCargado.containsKey(conDeuTitAd.getIdPersona().toString())){
					ConDeuTit conDeuTit = new ConDeuTit(constanciaDeu, conDeuTitAd.getIdPersona());
					conDeuTit.setEstado(Estado.ACTIVO.getId());
					constanciaDeu.createConDeutit(conDeuTit);
					
					// seteo de la persona usando el getByIdLight
					Long idPersona = conDeuTitAd.getIdPersona();
					Persona personaLight = Persona.getByIdLight(idPersona);
					if (personaLight == null){
						log.error("Persona no encontrada: " + idPersona);
						constanciaDeu.addRecoverableError(GdeError.CONSTANCIADEU_CUENTA_TITULAR_PERS_NO_ENC);
						return constanciaDeu;
					}
					
					desTitulares += esPrimero?"":" - ";
					desTitulares += personaLight.getRepresent();
					
					esPrimero=false;
				}
			}			
		}
		
		constanciaDeu.setDesTitulares(desTitulares);

		return constanciaDeu;
	}

	
	
	/**
	 * Creacion de ConstanciaDeu, optimizada para envio a judicial. 
	 * 
	 * 
	 * @param cuenta
	 * @param observacion
	 * @param idCuentaTitular, si es null, usa los titulares de la cuenta, sino, usa el id del parametro.
	 * @return
	 * @throws Exception
	 */
	public ConstanciaDeu createConstanciaDeu4Envio(Cuenta cuenta, String ubicacion, String observacion, Long idCuentaTitular) throws Exception{
		
		ConstanciaDeu constanciaDeu = new ConstanciaDeu();
		constanciaDeu.setNumero(ConstanciaDeu.getNextNumero()); // obtiene el nro de un sequence 
		constanciaDeu.setAnio(this.getAnioPlanilla());
		constanciaDeu.setProcurador(this.getProcurador());
		constanciaDeu.setCuenta(cuenta);
		constanciaDeu.setEstConDeu(EstConDeu.getById(EstConDeu.ID_EMITIDA)); // es emitida y no habilitada

		// Domicilio de Envio. 
		// Si informan idCuentaTitular Buscamos el domicilio de este en la db de personas.
		// Sino, usamos el desDomEnv de la cuenta.
		if (idCuentaTitular == null) {
			constanciaDeu.setDesDomEnv(cuenta.getDesDomEnv());
			constanciaDeu.setDesDomUbi(ubicacion);
		} else {
			CuentaTitular ct = CuentaTitular.getById(idCuentaTitular);
			Persona p = PersonaFacade.getInstance().getPersonaById(ct.getIdContribuyente());
			constanciaDeu.setDesDomEnv(p.getDomicilio().getViewDomicilio());
		}
		
		constanciaDeu.setProcesoMasivo(this.getProcesoMasivo());
		constanciaDeu.setPlaEnvDeuPro(this);
		constanciaDeu.setObservacion(observacion);
		
		constanciaDeu.setIdCaso(this.getIdCaso());
		constanciaDeu.setObservacion(this.getObservaciones());

		createConstanciaDeu(constanciaDeu);
		
		if(constanciaDeu.hasError()){
			return constanciaDeu;
		}
		
		String desTitular = "";
		// issue #6491: Generación de constancias de deuda en envío a CJ
		boolean esPrimero = true;
		for(CuentaTitular cuentaTitular: cuenta.getListCuentaTitularVigentes(new Date())){
			constanciaDeu.cargarTitular(cuentaTitular);
			Contribuyente contribuyente = cuentaTitular.getContribuyente();
			if(contribuyente!=null && contribuyente.getPersona()!=null){
				// seteo de la persona usando el getByIdLight
				Persona personaLight = Persona.getByIdLight(contribuyente.getPersona().getId());
				if (personaLight == null){
					desTitular = "Sin Datos: idCuentaTitular = " + idCuentaTitular;
				}else{
					desTitular += esPrimero?"":" - ";
					desTitular += personaLight.getRepresent();
					esPrimero=false;
				}
			}
		}
		
		constanciaDeu.setDesTitulares(desTitular);			
		
		return constanciaDeu;
	}
	
	/**
	 * Realiza la creacion de la Constancia de la Deuda y la creacion del Historico de la misma
	 * @param  constanciaDeu
	 * @return ConstanciaDeu
	 * @throws Exception
	 */
	public ConstanciaDeu createConstanciaDeu(ConstanciaDeu constanciaDeu) throws Exception {
		return GdeGDeudaJudicialManager.getInstance().createConstanciaDeu(constanciaDeu);
	}	

	/**
	 * Actualiza la Constancia de deuda
	 * @param constanciaDeu
	 * @return ConstanciaDeu
	 * @throws Exception
	 */
	public ConstanciaDeu updateConstanciaDeu(ConstanciaDeu constanciaDeu) throws Exception {		
		return GdeGDeudaJudicialManager.getInstance().updateConstanciaDeu(constanciaDeu);
	}	

	/**
	 * Actualiza la Constancia de deuda con el log pasado como parametro
	 * @return ConstanciaDeu
	 * @throws Exception
	 */
	public ConstanciaDeu updateConstanciaDeu(ConstanciaDeu constanciaDeu, String logEstado) throws Exception {		
		return GdeGDeudaJudicialManager.getInstance().updateConstanciaDeu(constanciaDeu, logEstado);
	}		
	/**
	 * Elimina la Constancia de deuda
	 * @param  constanciaDeu
	 * @return ConstanciaDeu 
	 * @throws Exception
	 */
	public ConstanciaDeu deleteConstanciaDeu(ConstanciaDeu constanciaDeu) throws Exception {
		return GdeGDeudaJudicialManager.getInstance().deleteConstanciaDeu(constanciaDeu);
	}	
	// <-- ABM ConstanciaDeu

	
	/**
	 * Incrementa en 1 la cantidad total de registros de la Planilla de Envio de Deuda a Procuradores
	 */
	public void incTotalRegistros() {
		this.setTotalRegistros(1L + this.getTotalRegistros());
	}

	/**
	 * agrega el importe al importe total de la Planilla de Envio de Deuda a Procuradores 
	 * @param importeTotal
	 */
	public void addImporteTotal(Double importe) {
		this.setImporteTotal(importe + this.getImporteTotal());
	}
	
	/**
	 * Incrementa en 1 la cantidad de cuentas de la Planilla de Envio de Deuda a Procuradores
	 *
	 */
	public void incCantidaCuentas() {
		this.setCantidadCuentas(1L + this.getCantidadCuentas());
	}
	
	/**
	 *  Cambia el Estado de la Planilla al de id pasado como parametro. Registra el cambio en el Historico.
	 *  @param idEstPlaEnvDeuPr
	 */
	public void cambiarEstPlaEnvDeuPr(Long idEstPlaEnvDeuPr, String logEstado) throws Exception{		
		this.setEstPlaEnvDeuPr(EstPlaEnvDeuPr.getById(idEstPlaEnvDeuPr));		
		GdeGDeudaJudicialManager.getInstance().updatePlaEnvDeuPro(this, logEstado);
	}
	
	/**
	 * Obtiene la PlaEnvDeuProPrint con el contenido adecuado para la generacion del archivo padron de procuradores xml
	 * @return PlaEnvDeuProPrint
	 * @throws Exception
	 */
	public PlaEnvDeuProPrint getPlaEnvDeuProPrint() throws Exception{
		PlaEnvDeuProPrint plaEnvDeuProPrint = new PlaEnvDeuProPrint();
		
		plaEnvDeuProPrint.setAnioPlanilla(this.getAnioPlanilla());
		plaEnvDeuProPrint.setNroPlanilla(this.getNroPlanilla());
		Procurador procurador = this.getProcurador();
		plaEnvDeuProPrint.setIdProcurador(procurador.getId());
		plaEnvDeuProPrint.setDesProcurador(procurador.getDescripcion());
		plaEnvDeuProPrint.setFechaEnvio(this.getFechaEnvio()); 
		plaEnvDeuProPrint.setTotalRegistros(this.getTotalRegistros());     
		plaEnvDeuProPrint.setImporteTotal(this.getImporteTotal());
		plaEnvDeuProPrint.setCantidadCuentas(this.getCantidadCuentas());
		
		for (ConstanciaDeu constanciaDeu : this.getListConstanciaDeu()) {
			plaEnvDeuProPrint.addConstanciaDeuPrint(constanciaDeu.getConstanciaDeuPrint());
		}
		
		return plaEnvDeuProPrint;
	}
	
	/**
	 * Determina si tiene todas las ConstanciaDeu Anuladas
	 * @return boolean
	 */
	public boolean tieneTodasConstanciaDeuAnuladas(){
		boolean tieneTodasConstanciaDeuAnuladas = true;
		
		for (ConstanciaDeu constanciaDeu : this.getListConstanciaDeu()) {
			
			if (!constanciaDeu.getEstConDeu().getId().equals(EstConDeu.ID_ANULADA)){
				tieneTodasConstanciaDeuAnuladas = false;
				break;
			}
		}
		return tieneTodasConstanciaDeuAnuladas;
	}

	
	public PlaEnvDeuProVO toVOForSearch() throws Exception{
		PlaEnvDeuProVO plaEnvDeuProVO = new PlaEnvDeuProVO();
		plaEnvDeuProVO.setId(this.getId());
		plaEnvDeuProVO.setFechaEnvio(this.fechaEnvio);
		plaEnvDeuProVO.setEstPlaEnvDeuPr((EstPlaEnvDeuPrVO) this.estPlaEnvDeuPr.toVO(0, false));
		plaEnvDeuProVO.setNroPlanilla(this.nroPlanilla);
		plaEnvDeuProVO.setAnioPlanilla(this.anioPlanilla);
		plaEnvDeuProVO.setProcurador((ProcuradorVO) this.procurador.toVO(0, false));
		if(this.procesoMasivo!=null){
			// Setea el procesoMasivo para obtener el recurso
			RecursoVO recursoVO = new RecursoVO();
			recursoVO.setId(this.procesoMasivo.getRecurso().getId());
			recursoVO.setDesRecurso(this.procesoMasivo.getRecurso().getDesRecurso());
			ProcesoMasivoVO pm = new ProcesoMasivoVO();
			pm.setId(this.procesoMasivo.getId());
			pm.setRecurso(recursoVO);
			plaEnvDeuProVO.setProcesoMasivo(pm);
		}else{
			// Setea las constancias para sacar el recurso de la primera de la lista
			if(this.listConstanciaDeu!=null){
				List<ConstanciaDeuVO> listConstanciaDeuVO = new ArrayList<ConstanciaDeuVO>();
				for(ConstanciaDeu constanciaDeu: this.listConstanciaDeu){
					ConstanciaDeuVO c = new ConstanciaDeuVO();
					c.setId(constanciaDeu.getId());
					c.setCuenta(constanciaDeu.getCuenta().toVOWithRecurso());
					listConstanciaDeuVO.add(c);
				}
				plaEnvDeuProVO.setListConstanciaDeu(listConstanciaDeuVO);
			}
		}
				
		return plaEnvDeuProVO;
	}


	
	/**
	 * Hace toVO(0, false) y para cada constancia, toVOForImprimir().<br>
	 * Para el procurador y el estado hace toVO(0, false).<br>
	 * Setea el procesoMasivo en null.
	 * @return
	 * @throws Exception 
	 */
	public PlaEnvDeuProVO toVOForImprimir() throws Exception {
		PlaEnvDeuProVO plaEnvDeuProVO = (PlaEnvDeuProVO) this.toVO(0, false);
		plaEnvDeuProVO.setListHistEstPlaEnvDP(null);
		plaEnvDeuProVO.setProcurador((ProcuradorVO) this.procurador.toVO(0, false));
		plaEnvDeuProVO.getProcurador().setListProRec(null);
		plaEnvDeuProVO.setEstPlaEnvDeuPr((EstPlaEnvDeuPrVO) this.estPlaEnvDeuPr.toVO(0, false));
		plaEnvDeuProVO.setProcesoMasivo(null);
		
		if(this.listConstanciaDeu!=null){
			List<ConstanciaDeuVO> listConstanciaDeuVO = new ArrayList<ConstanciaDeuVO>();
			for(ConstanciaDeu constanciaDeu: this.listConstanciaDeu){
				ConstanciaDeuVO constanciaDeuVO = constanciaDeu.toVOForView2();
				constanciaDeuVO.setPlaEnvDeuPro(plaEnvDeuProVO);
				ProcuradorVO procuradorVO = (ProcuradorVO)constanciaDeu.getProcurador().toVO(0, false);
				procuradorVO.setListProRec(null);
				constanciaDeuVO.setProcurador(procuradorVO);
				listConstanciaDeuVO.add(constanciaDeuVO);
			}
			plaEnvDeuProVO.setListConstanciaDeu(listConstanciaDeuVO);
		}
		return plaEnvDeuProVO;
	}
	
	/**
	 * Hace toVO(0, false) y trae la lista de historicos
	 * @return
	 * @throws Exception 
	 */
	public PlaEnvDeuProVO toVOWithHist() throws Exception{
		PlaEnvDeuProVO plaEnvDeuProVO = (PlaEnvDeuProVO) this.toVO(0, false);
		if(listHistEstPlaEnvDP!=null){
			List<HistEstPlaEnvDPVO> listHist = new ArrayList<HistEstPlaEnvDPVO>();
			for(HistEstPlaEnvDP hist: listHistEstPlaEnvDP){
				listHist.add((HistEstPlaEnvDPVO) hist.toVO(1, false));
			}
			plaEnvDeuProVO.setListHistEstPlaEnvDP(listHist);
		}
		return plaEnvDeuProVO;
	}
	
	/**
	 * Hace toVO(0, false) y trae la lista de historicos, el procurador y el estPlaEnvDeuPro
	 * @return
	 * @throws Exception
	 */
	public PlaEnvDeuProVO toVOWithHistProcuEstado() throws Exception{
		PlaEnvDeuProVO plaEnvDeuProVO = this.toVOWithHist();
		plaEnvDeuProVO.setProcurador((ProcuradorVO) this.procurador.toVO(0, false));
		plaEnvDeuProVO.setEstPlaEnvDeuPr((EstPlaEnvDeuPrVO) this.estPlaEnvDeuPr.toVO(0, false));
		return plaEnvDeuProVO;
	}
	
	
	/**
	 * Hace toVO(0, false) y trae la lista de constancias, el procurador y el estPlaEnvdeuPro
	 * @return
	 * @throws Exception 
	 */
	public PlaEnvDeuProVO toVOWithConstancias() throws Exception{
		PlaEnvDeuProVO plaEnvDeuProVO = (PlaEnvDeuProVO) this.toVO(0, false);
		plaEnvDeuProVO.setProcurador((ProcuradorVO) this.procurador.toVO(0, false));
		plaEnvDeuProVO.setEstPlaEnvDeuPr((EstPlaEnvDeuPrVO) this.estPlaEnvDeuPr.toVO(0, false));
		if(listConstanciaDeu!=null){
			List<ConstanciaDeuVO> listConst = new ArrayList<ConstanciaDeuVO>();
			for(ConstanciaDeu c: listConstanciaDeu){
				listConst.add((ConstanciaDeuVO) c.toVOLight());
			}
			plaEnvDeuProVO.setListConstanciaDeu(listConst);
		}
		return plaEnvDeuProVO;
	}
	
	/**
	 * Obtiene el Report que contiene la definicion del Reporte de la Planilla de Envio de Deuda a procuradores
	 * @param  reportFileDir
	 * @return ReportVO
	 */
	public ReportVO getReport(String reportFileDir) throws Exception{
		
		ReportVO report = new ReportVO(); 
		report.setReportFormat(1L);	

		report.setReportBeanName("PlaEnvDeuPro");
		report.setReportFileName(this.getClass().getName());
		report.setReportFileDir(reportFileDir); 
		report.setReportFileSharePath(SiatParam.getFileSharePath());
		
		ReportTableVO rtDeudas = new ReportTableVO("PlaEnvDeuPro");

		report.setReportTitle("Planilla de Envío de Deuda a Judiciales");

		rtDeudas.setTitulo("Listado de Deudas de la Planilla");

		report.getReportListTable().add(rtDeudas);
		

		return report;
	}
	
	/**
	 * Obtiene el Contenedor de datos cargado para ser utilizado en la generacion de reporte
	 * @return ContenedorVO
	 * @throws Exception
	 */
	public ContenedorVO cargarContenedor() throws Exception{
		if(log.isDebugEnabled()) log.debug("cargarContenedor");
	
		ContenedorVO contenedorPrincipal = new ContenedorVO("contenedorPrincipal");
		contenedorPrincipal.setPageHeight(21.0D);
		contenedorPrincipal.setPageWidth(29.7D);

		ContenedorVO contenedorDatos = new ContenedorVO("datosPlanilla");
		
		TablaVO tablaCabecera = new TablaVO("tablaCabecera");
		
		FilaVO f = new FilaVO();
		f.add(new CeldaVO("tributo","","Tributo"));
		f.add(new CeldaVO(this.getNroPlanilla() + "/" + this.getAnioPlanilla(),"", "Nro/Año")); 
		f.add(new CeldaVO(DateUtil.formatDate(this.getFechaEnvio(), DateUtil.dd_MM_YYYY_MASK),"", "Fecha Envío"));
		f.add(new CeldaVO(this.getProcurador().getDescripcion(),"", "Procurador"));
		tablaCabecera.setFilaCabecera(f);
		tablaCabecera.setTitulo("Datos de la Planilla");
		//tablaCabecera.setFilaTitulo(f);
		tablaCabecera.add(f);
		contenedorDatos.setTablaCabecera(tablaCabecera);
		//contenedorDatos.setTablaFiltros(tablaCabecera);
		contenedorPrincipal.getListBloque().add(contenedorDatos);
	
		//contenedorPrincipal.setTablaCabecera(tablaCabecera);
		
		TablaVO tablaDeudas = new TablaVO("Deudas");
		tablaDeudas.setTitulo("Listado de Deudas de la Planilla");
		
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Períodos"));
		filaCabecera.add(new CeldaVO("Años"));
		filaCabecera.add(new CeldaVO("Importe"));
		filaCabecera.add(new CeldaVO("Estado"));
		filaCabecera.add(new CeldaVO("Constancia"));
		filaCabecera.add(new CeldaVO("Cuenta"));
		filaCabecera.add(new CeldaVO("Catastral"));
		filaCabecera.add(new CeldaVO("Titular Principal", 45));
		tablaDeudas.setFilaCabecera(filaCabecera);
		
		// lista de filas del contenido
		List<FilaVO> listFila = new ArrayList<FilaVO>();
		String tributo = "";
		Double sumaImporte = 0D;
		for (ConstanciaDeu constanciaDeu : this.getListConstanciaDeu()) {
			String constancia = StringUtil.formatLong(constanciaDeu.getNumero());
			String cuenta = constanciaDeu.getCuenta().getNumeroCuenta();
			String catastral = constanciaDeu.getCuenta().getObjImp().getClaveFuncional();
			String titularPrincipal = constanciaDeu.getCuenta().getNombreTitularPrincipal();
			if (tributo == ""){
				tributo = constanciaDeu.getCuenta().getRecurso().getDesRecurso();
				contenedorPrincipal.getListBloque().get(0).getTablaCabecera().getListFila().get(0).getListCelda().get(0).setValor(tributo);
			}
			
			for (ConDeuDet conDeuDet : constanciaDeu.getListConDeuDet()) {
				DeudaJudicial dj = conDeuDet.getDeudaJudicial();
				if (dj == null){
					log.error("Deuda Judicial no encontrada en la tabla deudaJudicial. Id Deuda: " + conDeuDet.getIdDeuda());
					continue;
				}
				FilaVO filaContenido = new FilaVO();
				//Períodos
				filaContenido.add(new CeldaVO(StringUtil.formatLong(dj.getPeriodo())));
				//Años
				filaContenido.add(new CeldaVO(StringUtil.formatLong(dj.getAnio())));
				//Importe
				Double importe = dj.getImporte();
				if(importe != null) sumaImporte += importe;
				filaContenido.add(new CeldaVO(StringUtil.formatDouble(importe)));
				//Estado
				filaContenido.add(new CeldaVO(dj.getEstadoView()));
				// constancia
				filaContenido.add(new CeldaVO(constancia));
				// cuenta
				filaContenido.add(new CeldaVO(cuenta));
				// catastral
				filaContenido.add(new CeldaVO(catastral));
				// titular principal
				filaContenido.add(new CeldaVO(titularPrincipal));
				
				listFila.add(filaContenido);
			}
		}

		tablaDeudas.setListFila(listFila);
		
		//List<FilaVO> listFilaPie = new ArrayList<FilaVO>();
		//FilaVO  filaDeFiltros = new FilaVO();
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO());
		filaPie.add(new CeldaVO());
		filaPie.add(new CeldaVO(StringUtil.formatDouble(sumaImporte)));
		tablaDeudas.getListFilaPie().add(filaPie);
		filaPie.add(new CeldaVO());
		filaPie.add(new CeldaVO());
		filaPie.add(new CeldaVO());
		filaPie.add(new CeldaVO());
		filaPie.add(new CeldaVO());
		
		contenedorPrincipal.add(tablaDeudas);
		
		return contenedorPrincipal;
	}

	public int deleteListHistEstPlaEnvDP() throws Exception {

		return GdeDAOFactory.getHistEstPlaEnvDPDAO().deleteByPlaEnvDeuPro(this);
		
	}

	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public String getIdCaso() {
		return idCaso;
	}	

	public PlaEnvDeuPDet createPlaEnvDeuPDet(PlaEnvDeuPDet plaEnvDeuPDet) throws Exception {

		GdeDAOFactory.getPlaEnvDeuProDAO().update(plaEnvDeuPDet);

		return plaEnvDeuPDet;
	}
}
