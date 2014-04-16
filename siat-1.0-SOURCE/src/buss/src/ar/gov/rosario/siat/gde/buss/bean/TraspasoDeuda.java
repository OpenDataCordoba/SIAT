//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

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

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.ConDeuDetVO;
import ar.gov.rosario.siat.gde.iface.model.DeudaVO;
import ar.gov.rosario.siat.gde.iface.model.TraDeuDetVO;
import ar.gov.rosario.siat.gde.iface.model.TraspasoDeudaVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Representa el traspaso de deuda entre procuradores
 * 
 */
@Entity
@Table(name = "gde_traspasoDeuda")
public class TraspasoDeuda extends BaseBO {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(TraspasoDeuda.class);
	
	//fechaTraspaso
	@Column(name = "fechaTraspaso") 
	private Date fechaTraspaso;     // DATETIME YEAR TO DAY NOT NULL
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idRecurso") 
	private Recurso recurso;  // NOT NULL

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idProOri") 
	private Procurador proOri;  // NOT NULL

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idProDes") 
	private Procurador proDes;  // NOT NULL
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idCuenta") 
	private Cuenta cuenta;  // NOT NULL

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPlaEnvDeuProDest") 
	private PlaEnvDeuPro plaEnvDeuProDest;
	
	@Column(name = "observacion") 
	private String observacion; // VARCHAR(255)

	@Column(name = "usuarioAlta")
	private String    usuarioAlta; // CHAR(10) NOT NULL 
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idTraspasoDeuda")
	private List<TraDeuDet> listTraDeuDet;

	// Contructores 
	public TraspasoDeuda() {
		super();
	}
	
	//	 Getters y Setters
	public Cuenta getCuenta() {
		return cuenta;
	}
	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	public Date getFechaTraspaso() {
		return fechaTraspaso;
	}
	public void setFechaTraspaso(Date fechaTraspaso) {
		this.fechaTraspaso = fechaTraspaso;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public PlaEnvDeuPro getPlaEnvDeuProDest() {
		return plaEnvDeuProDest;
	}
	public void setPlaEnvDeuProDest(PlaEnvDeuPro plaEnvDeuProDest) {
		this.plaEnvDeuProDest = plaEnvDeuProDest;
	}
	public Procurador getProDes() {
		return proDes;
	}
	public void setProDes(Procurador proDes) {
		this.proDes = proDes;
	}
	public Procurador getProOri() {
		return proOri;
	}
	public void setProOri(Procurador proOri) {
		this.proOri = proOri;
	}
	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}
	public String getUsuarioAlta() {
		return usuarioAlta;
	}
	public void setUsuarioAlta(String usuarioAlta) {
		this.usuarioAlta = usuarioAlta;
	}
	public List<TraDeuDet> getListTraDeuDet() {
		return listTraDeuDet;
	}
	public void setListTraDeuDet(List<TraDeuDet> listTraDeuDet) {
		this.listTraDeuDet = listTraDeuDet;
	}

	// Metodos de clase
	public static TraspasoDeuda getById(Long id) {
		return (TraspasoDeuda) GdeDAOFactory.getTraspasoDeudaDAO().getById(id);
	}

	public static TraspasoDeuda getByIdNull(Long id) {
		return (TraspasoDeuda) GdeDAOFactory.getTraspasoDeudaDAO().getByIdNull(id);
	}

	public static List<TraspasoDeuda> getList() {
		return (ArrayList<TraspasoDeuda>) GdeDAOFactory.getTraspasoDeudaDAO().getList();
	}

	public static List<TraspasoDeuda> getListActivos() {			
		return (ArrayList<TraspasoDeuda>) GdeDAOFactory.getTraspasoDeudaDAO().getListActiva();
	}

	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author 
	 */
	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();

		// Validaciones de requeridos y unicidad comunes
		this.validate();
		
		// Recurso 
		if(this.getRecurso() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TRASPASODEUDA_RECURSO);
		}
		// procurador origen
		if(this.getProOri() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TRASPASODEUDA_PRO_ORI);
		}
		// procurador destino
		if(this.getProDes() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TRASPASODEUDA_PRO_DES);
		}
		// cuenta
		if(this.getCuenta() == null){
			addRecoverableError(GdeError.TRASPASODEUDA_CUENTA_NO_ASOCIADA_RECURSO);
		}
		// fecha
		if(this.getFechaTraspaso() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TRASPASODEUDA_FECHA_TRASPASO);
		}
		
		// que el procurador destino sea distinto del procurador origen
		if (this.getProOri() != null && this.getProDes() != null && 
				this.getProOri().getId().equals(this.getProDes().getId())){
			addRecoverableError(GdeError.TRASPASODEUDA_PRO_ORI_PRO_DES_COINCIDENTES);
		}
		
		if (hasError()) {
			return false;
		}

		// que la cuenta ingresada este asociada a por lo menos a un registro de deuda en via judicial asignado al procurador origen
		List<DeudaJudicial> listDeudaJudicial = DeudaJudicial.getByNroCtaYProcurador(
				this.getCuenta().getNumeroCuenta(), this.getProOri().getId());
		
		if (listDeudaJudicial.size() == 0){
			addRecoverableError(GdeError.TRASPASODEUDA_CUENTA_PRO_ORI_SIN_DEUDAS_JUDICIAL);
		}

		return !hasError();
	}

	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();

		// Validaciones de requeridos y unicidad comunes
		this.validate();

		if (hasError()) {
			return false;
		}

		return !hasError();
	}


	private boolean validate(){
		//limpiamos la lista de errores
		clearError();

		//Validaciones de VO


		if (hasError()) {
			return false;
		}

		//Validaciones de Negocio

		return (!hasError());
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

		return !hasError();
	}
	
	
	/**
	 * Crea un TraDeuDet
	 * Ademas si la deuda esta incluida en una gestion judicial, crea un historico de la gestion de deuda.
	 * @param traDeuDet
	 * @return TraDeuDet
	 * @throws Exception
	 */
	public TraDeuDet createTraDeuDet(TraDeuDet traDeuDet) throws Exception {

		// Validaciones de negocio
		if (!traDeuDet.validateCreate()) { 
			return traDeuDet;
		}
		
		GdeDAOFactory.getTraDeuDetDAO().update(traDeuDet);
		
		DeudaJudicial dj = traDeuDet.getDeudaJudicial();
		
		GesJudDeu gesJudDeu = dj.getGetJudDeu();
		
		if (gesJudDeu != null){
			GesJud gesJud = gesJudDeu.getGesJud();
			
			HistGesJud histGesJud = new HistGesJud();
			histGesJud.setFecha(this.getFechaTraspaso());
			histGesJud.setDescripcion("Traspaso de deudas incluidas en la Gestión Judicial");
			histGesJud.setGesJud(gesJud);
			histGesJud.setUsuarioUltMdf(this.getUsuarioUltMdf());
			histGesJud = gesJudDeu.getGesJud().createHistGesJud(histGesJud);
			
			// paso de errores desde el historico al traDeuDet creado
			histGesJud.addErrorMessages(traDeuDet);
		}
		
		dj.setProcurador(this.getProDes());
		return traDeuDet;
	}


	/**
	 * Generar una nueva planilla de envio de deuda,
	 * generar una nueva constancia de deuda,
	 * generar cada detalle de la constancia de deuda.
	 * Creacion de cada ConDeuTit de la constancia de deuda:
	 * 		los idPersona se obtienen de la lista de titulares de cada constancia original del trapaso
	 * 		y de la lista de cuenta titular vigentes de la cuenta
	 * @return PlaEnvDeuPro
	 * @throws Exception
	 */
	public PlaEnvDeuPro createPlaEnvDeuPro() throws Exception {
		
		PlaEnvDeuPro plaEnvDeuPro = new PlaEnvDeuPro();
		
		//anioPlanilla: el anio de la fecha actual
		Integer anio = DateUtil.getAnioActual();
		plaEnvDeuPro.setAnioPlanilla(anio);
		//nroPlanilla: no es incremental por recurso del envio.
		plaEnvDeuPro.setNroPlanilla(PlaEnvDeuPro.getNextNroPlanilla());
		//idProcurador: el id del procurador.
		plaEnvDeuPro.setProcurador(this.getProDes());
		//fechaEnvio: nula
		plaEnvDeuPro.setFechaEnvio(null); 
		//fechaRecepcion: nula
		plaEnvDeuPro.setFechaRecepcion(null);
		//idProcesoMasivo: nulo
		plaEnvDeuPro.setProcesoMasivo(null);
		//idEstPlaEnvDeuPr: emitida
		plaEnvDeuPro.setEstPlaEnvDeuPr(EstPlaEnvDeuPr.getById(EstPlaEnvDeuPr.ID_EMITIDA));
		//inicializo los contadores en cero
		plaEnvDeuPro.setCantidadCuentas(0L);
		plaEnvDeuPro.setTotalRegistros(0L);
		plaEnvDeuPro.setImporteTotal(0D);
		plaEnvDeuPro.setObservaciones("Planilla generada automaticamente por Traspaso de Deuda entre Procuradores");
		
		plaEnvDeuPro = GdeGDeudaJudicialManager.getInstance().createPlaEnvDeuPro(plaEnvDeuPro);
		
		List<ConDeuTit> listConDeuTit = new ArrayList<ConDeuTit>();
		// obtener la lista de los titulares de cada constancia original del trapaso
		for (TraDeuDet traDeuDet : this.getListTraDeuDet()) {
			if (traDeuDet.getConstanciaDeuOri() != null) {
				for (ConDeuTit conDeuTit : traDeuDet.getConstanciaDeuOri().getListConDeuTit()) {
					listConDeuTit.add(conDeuTit);
				}
			}
		}  
		
		Cuenta cuenta = this.getCuenta();
		String observacion = "Constancia generada automaticamente por Traspaso de Deuda entre Procuradores";
		ConstanciaDeu constanciaDeu = plaEnvDeuPro.createConstanciaDeu(cuenta, observacion, listConDeuTit);
		if(constanciaDeu.hasError()){
			log.error("Error en la creacion de la constancia");
			constanciaDeu.addErrorMessages(plaEnvDeuPro);
		}else{
			observacion = "Inclusion automática de deuda a la Constancia de Deuda por Traspaso entre Procuradores";
			constanciaDeu.createListConDeuDet(this.getListIdDeudaJudicial(), observacion);
		}

		this.setPlaEnvDeuProDest(plaEnvDeuPro);
		
		/*
		// Creacion de cada ConDeuTit de la contancia de deuda:
		// mapa que contiene los id unicos de personas que van a ser los titulares de la constancia de deuda creada
		Map<Long,Long> mapIdPersonaDeuTit = new HashMap<Long, Long>();
		
		// obtener la lista de los titulares de cada constancia original del trapaso
		for (TraDeuDet traDeuDet : this.getListTraDeuDet()) {
			if (traDeuDet.getConstanciaDeuOri() != null) {
				for (ConDeuTit conDeuTit : traDeuDet.getConstanciaDeuOri().getListConDeuTit()) {
				
					mapIdPersonaDeuTit.put(conDeuTit.getId(), conDeuTit.getId());
				}
			}
		}  
		 
		// obtener la lista de cuenta titular vigentes de la cuenta
		for (CuentaTitular cuentaTitular : cuenta.getListCuentaTitularVigentes(new Date())) {

			Long idContribuyente = cuentaTitular.getContribuyente().getId();
			mapIdPersonaDeuTit.put(idContribuyente, idContribuyente);
		}
		
		// creacion de cada ConDeuTit usando el mapa de id de personas cargado en los bucles anteriores
		for (Long idPersona : mapIdPersonaDeuTit.keySet()) {
			ConDeuTit conDeuTit = new ConDeuTit(constanciaDeu, idPersona);
			constanciaDeu.createConDeutit(conDeuTit);
		}*/
		
		return plaEnvDeuPro; 
	}

	public TraspasoDeudaVO toVOForTraDeuDet() throws Exception{
		TraspasoDeudaVO traspasoDeudaVO = (TraspasoDeudaVO) this.toVO(1);
		// es necesario limpiar la lista de TraDeuDet porque el toVO de nivel 1 la carga
		traspasoDeudaVO.setListTraDeuDet(new ArrayList<TraDeuDetVO>());
		
		for (TraDeuDet traDeuDet : this.getListTraDeuDet()) {
			TraDeuDetVO     traDeuDetVO     = (TraDeuDetVO) traDeuDet.toVO(1);
			
			DeudaVO deudaVO = (DeudaVO) traDeuDet.getDeuda().toVO(1,false);
			traDeuDetVO.setDeuda(deudaVO);
			if (traDeuDet.getConstanciaDeuOri() != null){
				traDeuDetVO.setVerConstanciaBussEnabled(true);
				ConDeuDet conDeuDet = traDeuDet.getConstanciaDeuOri().getConDeuDetByIdDeuda(traDeuDet.getIdDeuda());
				if(conDeuDet == null){
					log.error("Error de Datos: TraDeuDet con constanciaOrigen y la deuda no pertenece a ningun detalle de la constanciaOrigen");
				}
				traDeuDetVO.setConDeuDet((ConDeuDetVO) conDeuDet.toVO(0));
			}else{
				traDeuDetVO.setVerConstanciaBussEnabled(false);
			}
			traspasoDeudaVO.getListTraDeuDet().add(traDeuDetVO);
		}
		return traspasoDeudaVO;
	}

	//	 Metodos de negocio

	public List<Long> getListIdDeudaJudicial(){
		List<Long> listIdDeudaJudicial = new ArrayList<Long>();
		for (TraDeuDet traDeuDet :  getListTraDeuDet()) {
			listIdDeudaJudicial.add(traDeuDet.getIdDeuda());
		}
		return listIdDeudaJudicial;
	}
}
