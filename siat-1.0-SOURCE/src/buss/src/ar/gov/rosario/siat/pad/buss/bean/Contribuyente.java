//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.buss.dao.SiatBussCache;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.ConAtr;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.AtrValDefinition;
import ar.gov.rosario.siat.def.iface.model.ConAtrVO;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.gde.iface.model.DeudaContribSearchPage;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.ConAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteDefinition;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBean;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Vigencia;

/**
 * Bean correspondiente a Contribuyente
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_contribuyente")
public class Contribuyente extends BaseBean {
	
	@Transient
	private static Logger log = Logger.getLogger(Contribuyente.class);
	
	// Propiedades
	private static final long serialVersionUID = 1L;
	public static final int MAX_CUENTAS_BY_CONTRIB = 30;
	
	@Id
	private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="idpersona")
	private Persona persona;
	
	@Transient	
	private Domicilio domicilioFiscal;
	
    @Column(name="idCasoDomFis") 
	private String idCaso; // casoDomFis
	
	@Column(name="fechaDesde")
	private Date fechaDesde;
	
	@Column(name="fechaHasta")	
	private Date fechaHasta;
	
	@Column(name="nroIsib")
	private String nroIsib;
	
	

	@OneToMany()
	@JoinColumn(name="idcontribuyente")
	private List<ConAtrVal> listConAtrVal = new ArrayList<ConAtrVal>();
		
	@OneToMany()
	@JoinColumn(name="idcontribuyente")
	private List<CuentaTitular> listCuentaTitular;

	@Transient
	private Boolean superaMaxCantCuentas = false;
	
	@Transient
	private int countCuentasContrib; 
	
	// Constructores
	public Contribuyente(){
		super();
	}
	
	// Getters y setters
	public Domicilio getDomicilioFiscal() {
		return domicilioFiscal;
	}
	public void setDomicilioFiscal(Domicilio domicilioFiscal) {
		this.domicilioFiscal = domicilioFiscal;
	}
	
	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}
	
	public Persona getPersona() {
		return persona;
	}
	/*
	 * Tambi&eacute;n setea el id del Contribuyente con el idPersona.
	 * 
	 */
	public void setPersona(Persona persona) {
		this.persona = persona;
		this.setId(persona.getId());
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
	public List<ConAtrVal> getListConAtrVal() {
		return listConAtrVal;
	}
	public void setListConAtrVal(List<ConAtrVal> listConAtrVal) {
		this.listConAtrVal = listConAtrVal;
	}
	public List<CuentaTitular> getListCuentaTitular() {
		return listCuentaTitular;
	}
	public void setListCuentaTitular(List<CuentaTitular> listCuentaTitular) {
		this.listCuentaTitular = listCuentaTitular;
	}
	
	public String getNroIsib() {
		return nroIsib;
	}

	public void setNroIsib(String nroIsib) {
		this.nroIsib = nroIsib;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getSuperaMaxCantCuentas() {
		return superaMaxCantCuentas;
	}
	public void setSuperaMaxCantCuentas(Boolean superaMaxCantCuentas) {
		this.superaMaxCantCuentas = superaMaxCantCuentas;
	}

	public int getCountCuentasContrib() {
		return countCuentasContrib;
	}
	public void setCountCuentasContrib(int countCuentasContrib) {
		this.countCuentasContrib = countCuentasContrib;
	}

	/** Recupera el contribuyente, con la persona
	 *  correspondiente,obtenida a travez
	 *  de PersonaFacade.
	 * 
	 * @param id
	 * @return Contribuyente
	 * @throws Exception
	 */
	public static Contribuyente getById(Long id) throws Exception {

		// recupero el contribuyente
		Contribuyente contribuyente = (Contribuyente) PadDAOFactory.getContribuyenteDAO().getById(id);
		
		// recupero y seteo la persona al contribuyente
		if(contribuyente!=null){
			contribuyente.loadPersonaFromMCR();	
		}
		
		
		return contribuyente;
	}
	
	/** Recupera el contribuyente, con la persona
	 *  correspondiente,obtenida a travez
	 *  de PersonaFacade. Si no lo encuentra devuelve null.
	 * 
	 * @param id
	 * @return Contribuyente
	 */
	public static Contribuyente getByIdNull(Long id) throws Exception {

		// recupero el contribuyente
		Contribuyente contribuyente = (Contribuyente) PadDAOFactory.getContribuyenteDAO().getByIdNull(id);
		
		// recupero y seteo la persona al contribuyente
		if(contribuyente!=null){
			contribuyente.loadPersonaFromMCR();	
		}
		
		
		return contribuyente;
	}

	/** Recupera el contribuyente, sin obtener la persona desde PersonaFacade. En Persona asigna una new Persona
	 *  con el id seteado igual al de contribuyente. Si no lo encuentra devuelve null. 
	 *  Este metodo se usa desde la migracion.
	 * 
	 * @param id
	 * @return Contribuyente
	 */
	public static Contribuyente getByIdNullForInterface(Long id) throws Exception {

		Contribuyente contribuyente = (Contribuyente) PadDAOFactory.getContribuyenteDAO().getByIdNull(id);

		return contribuyente;
	}
	
	/** Recupera el contribuyente usando su idPersona en lugar del id
	 * 
	 * @param idPersona
	 * @return Contribuyente
	 * @throws Exception
	 */
	public static Contribuyente getByIdPersona(Long id) throws Exception {

		// recupero el contribuyente
		Contribuyente contribuyente = PadDAOFactory.getContribuyenteDAO().getByIdPersona(id);
		
		// recupero y seteo la persona al contribuyente
		if(contribuyente!=null){
			contribuyente.loadPersonaFromMCR();	
		}
		
		
		return contribuyente;
	}

	// Metodos de Instancia
	// Validaciones

	/**
	 * Valida la creacion
	 * @author Ivan
	 */
	public boolean validateCreate() throws Exception {
		//limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	/**
	 * Valida la actualizacion
	 * @author Ivan
	 */
	public boolean validateUpdate() throws Exception {
		//limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}
	
	/**
	 * Valida la eliminacion
	 * @author Ivan
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		/*Ejemplo:
		if (SiatHibernateUtil.hasReference(this, ${BeanRelacionado1}.class, "${bean}")) {
			addRecoverableError(DefError.ATRIBUTO_${BEANRELACIONADO1}_HASREF);
		}
		if (SiatHibernateUtil.hasReference(this, ${BeanRelacionado2}.class, "${bean}")) {
			addRecoverableError(DefError.ATRIBUTO_${BEANRELACIONADO2}_HASREF);
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		// Requeridos
		if (getFechaDesde() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CONTRIBUYENTE_FECHADESDE);
		}
		
		if (getPersona() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.PERSONA_LABEL);
		}
		
		if (hasError()) {
			return false;
		}
		
		if(this.getFechaHasta() != null && DateUtil.isDateAfter(getFechaDesde(), getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
				PadError.CONTRIBUYENTE_FECHADESDE, PadError.CONTRIBUYENTE_FECHAHASTA);			
		}

		if (hasError()) {
			return false;
		}

		return true;		

	}
	

	// Metodos de negocio
	
	/**
	 * Carga la persona desde MCR
	 * @throws Exception
	 */
	public void loadPersonaFromMCR() throws Exception {
		//	recupero y seteo la persona al contribuyente
		Persona persona = PersonaFacade.getInstance().getPersonaById(this.getPersona().getId());
		this.setPersona(persona);
	}
	
	/**
	 * Obtiene la lista de Cuentas del Contribuyente ordenadas por descripcion del Recurso y por Fecha Desde ascendente
	 * @return
	 */
	public List<CuentaTitular> getListOrderByRecursoFecDesde(){
		return PadDAOFactory.getCuentaTitularDAO().getListOrderByRecursoFecDesde(this);
	}
	
	
	/**
	 * Determina si el Contribuyente registra ser titular de cuenta
	 * @return Boolean
	 */
	public Boolean registraTitularDeCuenta(){
		return PadDAOFactory.getCuentaTitularDAO().registraTitularDeCuentaByContribuyente(this);
	}

	/**
	 * Obtiene la definicion con sus valores del Contribuyente.
	 *  
	 * @throws Exception 
	 */
	public ContribuyenteDefinition getDefinitionValue() throws Exception {

		ContribuyenteDefinition contribuyenteDefinition = Contribuyente.getDefinitionForManual();

		// valorizo los atributos
		for (ConAtrDefinition conAtrDefinition:contribuyenteDefinition.getListConAtrDefinition()) {
			this.valorizarConAtrDefinition(conAtrDefinition);
		}

		return contribuyenteDefinition;

	}

	
	/**
	 * Obtiene la definicion con sus valores del Contribuyente para los atributos marcados como visibles 
	 * en la consulta de deuda.
	 *  
	 * @throws Exception 
	 */
	public ContribuyenteDefinition getDefinitionValueForWeb(ContribuyenteDefinition contribuyenteDefinition) throws Exception {
		
		log.debug("Contribuyente.getDefinitionValueForWeb() : enter #####################");
		
		// ContribuyenteDefinition contribuyenteDefinition = Contribuyente.getDefinitionForWeb();

		// valorizo los atributos
		for (ConAtrDefinition conAtrDefinition:contribuyenteDefinition.getListConAtrDefinition()) {
			this.valorizarConAtrDefinition(conAtrDefinition);
		}
		
		log.debug("Contribuyente.getDefinitionValueForWeb() : exit #####################");
		return contribuyenteDefinition;

	}
	
	/**
	 * Obtiene la definicion del Atributo del contribuyente 
	 * con el id de conAtr pasado como parametro.
	 *
	 * @throws Exception 
	*/
	public ConAtrDefinition getConAtrDefinition(Long idConAtr) throws Exception {

		// recupero un ConAtrDefinition
		ConAtrDefinition conAtrDefinition =
			this.getDefinitionValue().getConAtrDefinitionById(idConAtr);

		return conAtrDefinition;

		/*
		// recupero la definicion para contribuyente
		ContribuyenteDefinition contribuyenteDefinition = Contribuyente.getDefinitionForManual();

		// recupero la definicion del Atributo del contribuyente
		ConAtrDefinition conAtrDefinition = contribuyenteDefinition.getConAtrDefinitionById(idConAtr);

		// valorizo el atributo
		// recupero el valor para el atributo
		ConAtrVal conAtrValVigente = ConAtrVal.getVigente(this.getId(), idConAtr);

		// seteo el valor en la definicion en caso de que exista
		if (conAtrValVigente != null) {
			conAtrDefinition.addValor(conAtrValVigente.getValor());

			// agrego los valores en caso de tenerlos
			for (ConAtrVal conAtrVal:ConAtrVal.getList(this.getId(), idConAtr) ) {
				// agrego los valores de las vigencias
				conAtrDefinition.addAtrValVig
					(conAtrVal.getValor(), null, conAtrVal.getFechaDesde(), conAtrVal.getFechaHasta());
			}

		}

		return conAtrDefinition;
		*/

	}
	
	/**
	 * Setea los valores a la definicion de atributos del 
	 * contribuyente pasada como parametro.  
	 * 
	 * @throws Exception 
	*/
	public ConAtrDefinition valorizarConAtrDefinition(ConAtrDefinition conAtrDefinition) throws Exception {
		
		log.debug("Contribuyente.valorizarConAtrDefinition() : enter #####################");
		Long idConAtr = conAtrDefinition.getConAtr().getId();
		
		// recupero el atributo valorizado vigente
		ConAtrVal conAtrValVigente = this.getConAtrValVigente(idConAtr); 

		// seteo el valor en la definicion en caso de que exista
		if (conAtrValVigente != null) {
			conAtrDefinition.setValor(conAtrValVigente.getValor());

		}

		// agrego los valores de vigencia en caso de tenerlos
		for (ConAtrVal conAtrVal:this.getListConAtrValByConAtr(idConAtr)) {
			String strValor = conAtrDefinition.getValor4VigView(conAtrVal.getValor());
			
			// agrego los valores de las vigencias
			conAtrDefinition.addAtrValVig(strValor, null, conAtrVal.getFechaDesde(), conAtrVal.getFechaHasta());
		}
		
		log.debug("Contribuyente.valorizarConAtrDefinition() : enter #####################");
		return conAtrDefinition;
	}

	/**
	 * Obtiene un objeto de definicion de atributos para el Contribuyente.
	 * @return la definicion con sus atributos y valores en blanco.
	 * @throws Exception 
	 */
	public static ContribuyenteDefinition getDefinitionForManual() throws Exception {
		
		ContribuyenteDefinition contribuyenteDefinition = new ContribuyenteDefinition();
		contribuyenteDefinition.setAct(AtrValDefinition.ACT_MANUAL);
		
		for (ConAtr conAtr:ConAtr.getListActivos()) {
			ConAtrDefinition conAtrDefinition = new ConAtrDefinition(); 
			conAtrDefinition.setConAtr((ConAtrVO) conAtr.toVO(3));
			
			contribuyenteDefinition.addConAtrDefinition(conAtrDefinition);
		}
		
		return contribuyenteDefinition; 

	}
	
	/**
	 * Obtiene la lista de los Atributos del Contribuyente marcados como visibles en la consulta de deuda.
	 * Con su valorizacion por defecto si la posee
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public static ContribuyenteDefinition getDefinitionForWeb() throws Exception {
		
		log.debug("Contribuyente.getDefinitionForWeb() : enter #####################");
		
		ContribuyenteDefinition contribuyenteDefinition = new ContribuyenteDefinition();
		contribuyenteDefinition.setAct(AtrValDefinition.ACT_WEB);
		
		List<ConAtr> listActivosForWeb = SiatBussCache.getInstance().getListActivosForWeb();
		
		for (ConAtr conAtr:listActivosForWeb) {	
			ConAtrDefinition conAtrDefinition = new ConAtrDefinition(); 
			conAtrDefinition.setConAtr((ConAtrVO) conAtr.toVO(3));
			// Si posee valor por defecto lo seteo a la definicion.
			if (!StringUtil.isNullOrEmpty(conAtr.getValorDefecto()))
				conAtrDefinition.addValor(conAtr.getValorDefecto());
			
			contribuyenteDefinition.addConAtrDefinition(conAtrDefinition);
		}
		
		
		log.debug("Contribuyente.getDefinitionForWeb() : exit #####################");
		
		return contribuyenteDefinition; 
	}
	
	/**
	 * Actualiza el valor y/o las vigencias de un atributo valorizado del 
	 * Contribuyente. El metodo actualiza el valor y las vigencias en caso
	 * de ser un atributo que posee vigencias.
	 * @param objImpAtrVal valores a actualizar
	 * @return el objeto actualizado
	 * @throws Exception 
	 */
	public ConAtrDefinition updateConAtrDefinition(ConAtrDefinition conAtrDefinition) throws Exception {

		ConAtrVal conAtrVal = null;
        Long idConAtr = conAtrDefinition.getConAtr().getId(); 

        // si ya hay un atributo valorizado creado lo actualizo
        //if (conAtrValVigente != null) {
        if (conAtrDefinition.getPoseeVigencia()){

        	ConAtrVal conAtrValVigente = this.getConAtrValVigente(idConAtr);
        	ConAtrVal conAtrValPosVigente= null;
        	
        	Date fechaHasta = conAtrDefinition.getFechaHasta();
    		// seteo el vigente con la fecha hasta igual a la fecha desde del ingresada
        	// es caso de ser nula
        	if (conAtrValVigente != null && conAtrValVigente.getFechaHasta() == null) {
        		if(DateUtil.isDateAfterOrEqual(conAtrDefinition.getFechaDesde(), conAtrValVigente.getFechaDesde())){
        			conAtrValVigente.setFechaHasta(conAtrDefinition.getFechaDesde());
        			conAtrValVigente = this.updateConAtrVal(conAtrValVigente);
        		}else{
        			fechaHasta =  conAtrValVigente.getFechaDesde(); 			
        		}
        			
       		}
        	
        	
        	
        	// Insertar un registro nuevo
        	conAtrVal = new ConAtrVal();	            	
        	conAtrVal.setContribuyente(this);
    		conAtrVal.setConAtr(ConAtr.getById(conAtrDefinition.getConAtr().getId()));
    		conAtrVal.setValor(conAtrDefinition.getValorString());
        	conAtrVal.setFechaDesde(conAtrDefinition.getFechaDesde());
        	//Me fijo si hay un Registro a futuro
        	conAtrValPosVigente = this.getConAtrValPosVigente(idConAtr,conAtrVal);
        	if (conAtrValPosVigente != null ) {
        		conAtrDefinition.setFechaHasta(DateUtil.addDaysToDate(conAtrValPosVigente.getFechaDesde(),-1));
        	}
        	conAtrVal.setFechaHasta(fechaHasta);
        	conAtrVal = this.createConAtrVal(conAtrVal);

	 	} else {
	 		//Solo se actualiza el valor del registro.
	 		// recupero en atributo
	 		conAtrVal = this.getConAtrValByIdConAtr(idConAtr);

	 		// si no posee lo creo
	 		if (conAtrVal == null) {
	 			conAtrVal = new ConAtrVal();
	 			conAtrVal.setContribuyente(this);
	 			conAtrVal.setConAtr(ConAtr.getById(idConAtr));
	 		}
	 		
	 		// seteo el valor
	 		conAtrVal.setFechaDesde(new Date());
	 		conAtrVal.setValor(conAtrDefinition.getValorString());
	 		conAtrVal = this.updateConAtrVal(conAtrVal);
	 	}

		return conAtrDefinition;
	}
	
	/**
	 * Actualiza el valor y/o las vigencias de los atributos valorizados del 
	 * Contribuyente. El metodo actualiza el valor y las vigencias en caso
	 * de ser un atributo que posee vigencias.
	 * @param contribuyenteDefinition valores a actualizar
	 * @return contribuyenteDefinition 
	 * @throws Exception 
	 */
	public ContribuyenteDefinition updateConAtrDefinition(ContribuyenteDefinition contribuyenteDefinition) throws Exception {

		for(ConAtrDefinition conAtrDefinition: contribuyenteDefinition.getListConAtrDefinition()){
			updateConAtrDefinition(conAtrDefinition);
		}
		
		return contribuyenteDefinition;
	}

	
	public ConAtrVal updateConAtrVal(ConAtrVal conAtrVal) throws Exception {

		if (!conAtrVal.validateUpdate()) {
			return conAtrVal;
		}

		PadDAOFactory.getConAtrValDAO().update(conAtrVal);

		return conAtrVal;
	} 

	
	public ConAtrVal createConAtrVal(ConAtrVal conAtrVal) throws Exception {

		if (!conAtrVal.validateUpdate()) {
			return conAtrVal;
		}

		PadDAOFactory.getConAtrValDAO().update(conAtrVal);

		return conAtrVal;
	} 
	
	/** Recupera el conAtrVal vigente para el contribuyente.
	 * 
	 * @param idConAtr
	 * @return
	 */
	public ConAtrVal getConAtrValVigente (Long idConAtr) {
		ConAtrVal conAtrValVigente = null;
		for (ConAtrVal conAtrVal:this.getListConAtrVal()) {
			// busco el atributo
			if (conAtrVal.getConAtr().getId().equals(idConAtr)) {
				// me fijo que este vigente
				if (conAtrVal.getVigencia().equals(Vigencia.VIGENTE.getId())) {
					conAtrValVigente = conAtrVal;
				}
				
			}
		}
		return conAtrValVigente;
	}
	
	/** Recupera el conAtrVal posterior al vigente para el contribuyente.
	 * 
	 * @param idConAtr
	 * @return
	 */
	public ConAtrVal getConAtrValPosVigente (Long idConAtr, ConAtrVal conAtrValIng) {
		ConAtrVal conAtrValPosVigente = null;
		for (ConAtrVal conAtrVal:this.getListConAtrVal()) {
			// busco el atributo
			if (conAtrVal.getConAtr().getId().equals(idConAtr)) {
				
				// me fijo que este vigente
				if ( DateUtil.isDateAfter(conAtrVal.getFechaDesde(), conAtrValIng.getFechaDesde())) {
					conAtrValPosVigente = conAtrVal;
				}
				
			}
		}
		return conAtrValPosVigente;
	}
	
	/** Recupera el conAtrVal vigente para el contribuyente mediante el id atributo.
	 * 
	 * @param idConAtr
	 * @return
	 */
	public ConAtrVal getConAtrValVigenteByIdAtributo(Long idAtributo) {
		ConAtrVal conAtrValVigente = null;
		for (ConAtrVal conAtrVal:this.getListConAtrVal()) {
			// busco el atributo
			if (conAtrVal.getConAtr().getAtributo().getId().equals(idAtributo)) {
				// me fijo que este vigente
				if (conAtrVal.getVigencia().equals(Vigencia.VIGENTE.getId())) {
					conAtrValVigente = conAtrVal;
				}
				
			}
		}
		return conAtrValVigente;
	}

	/** Recupera una lista de los atributos valorizados
	 *  para un determinado atributo.
	 * 
	 * @param idConAtr
	 * @return
	 */
	public List<ConAtrVal> getListConAtrValByConAtr(Long idConAtr) {
		List<ConAtrVal> listConAtrValByConAtr = new ArrayList<ConAtrVal>();
		for (ConAtrVal conAtrVal:this.getListConAtrVal()) {
			if (conAtrVal.getConAtr().getId().equals(idConAtr)) {
				listConAtrValByConAtr.add(conAtrVal);
			}
		}
		return listConAtrValByConAtr;
	}

	/** Recupera el atributo valorizado para el atributo
	 *  pasado como parametro, si hay mas de uno devuelve
	 *  el primero, si no hay ninguno, devuelve null.
	 *  Deberia usarse para los atributos sin vigenica.
	 * 
	 * @param idConAtr
	 * @return
	 */
	public ConAtrVal getConAtrValByIdConAtr (Long idConAtr) {
		ConAtrVal conAtrValReturn = null;
		
		for (ConAtrVal conAtrVal:this.getListConAtrVal()) {
			if (conAtrVal.getConAtr().getId().equals(idConAtr)) {
				conAtrValReturn = conAtrVal;
				break;
			}
			
		}
		
		return conAtrValReturn;
	}

	/**
	 * Devulve una lista de cuentas obtenidas de CuentaTitular.
	 * Si el Contribuyente posee mas de 100 cuentas, solo devuelve las primeras 100. 
	 * 
	 * 
	 * @return
	 */
	public List<Cuenta> getListCuenta(){
		List<Cuenta> listCuenta = new ArrayList<Cuenta>();
		
		int countCuentasTitular = PadDAOFactory.getContribuyenteDAO().countCuentasTitular(this);
		
		log.debug("Contribuyente.getListCuenta.countCuentasTitular: " + countCuentasTitular);

		// Si tiene mas de 100, solo mostramos las primeras
		List<CuentaTitular> listCuentaTitularLimit = new ArrayList<CuentaTitular>(); 
		
		if (countCuentasTitular > MAX_CUENTAS_BY_CONTRIB){
			listCuentaTitularLimit = PadDAOFactory.getContribuyenteDAO().getListCuentasTitularLimit(this, MAX_CUENTAS_BY_CONTRIB);
			this.setSuperaMaxCantCuentas(true);
			this.setCountCuentasContrib(countCuentasTitular);
		} else {
			listCuentaTitularLimit = this.getListCuentaTitular();
		}
		
		for (CuentaTitular ct : listCuentaTitularLimit) {
			// Cambio intruducido 22-01-2009 por bug 654, solo mostramos las cuentas para CuentaTitular vigentes.
			if (ct.getFechaHasta() == null || DateUtil.isDateAfterOrEqual(ct.getFechaHasta(), new Date()) )
				listCuenta.add(ct.getCuenta());
		}

		return listCuenta;
	}
	
	
	/**
	 * Devuelve una lista de Cuentas de del contribuyente, si la cantidad supera la constante MAX_CUENTAS_BY_CONTRIB
	 * Utiliza el DeudaContribSearchPage recibido para paginarlas.
	 * 
	 * Solo se devuelven la cuentas de CuentaTitular pertenecientes a titulares vigentes.
	 * 
	 * @param deudaContribSearchPage
	 * @return
	 */
	public List<CuentaTitular> getListCuentaBySearchPage(DeudaContribSearchPage deudaContribSearchPage) throws Exception{
	
		String funcName = DemodaUtil.currentMethodName();
		
		//int countCuentasTitular = PadDAOFactory.getContribuyenteDAO().countCuentasTitular(this);
		int countCuentasTitular = PadDAOFactory.getContribuyenteDAO().countCuentasTitular(deudaContribSearchPage);
		
		log.debug(funcName + " countCuentasTitular: " + countCuentasTitular);
	
		// Si tiene mas de 100, solo mostramos las primeras
		List<CuentaTitular> listCuentaTitularLimit = new ArrayList<CuentaTitular>(); 
		
		if (countCuentasTitular > MAX_CUENTAS_BY_CONTRIB){
			
			deudaContribSearchPage.setRecsByPage(new Long(MAX_CUENTAS_BY_CONTRIB));
			deudaContribSearchPage.setMaxRegistros(new Long(countCuentasTitular));
			
			log.debug(funcName + " getRecsByPage: " + deudaContribSearchPage.getRecsByPage());
			log.debug(funcName + " getPageNumber: " + deudaContribSearchPage.getPageNumber());
			log.debug(funcName + " getFirstResult: " + deudaContribSearchPage.getFirstResult());
			
			//listCuentaTitularLimit = PadDAOFactory.getContribuyenteDAO().getListCuentasTitularPaged(this,deudaContribSearchPage.getFirstResult(),MAX_CUENTAS_BY_CONTRIB);
			listCuentaTitularLimit = PadDAOFactory.getContribuyenteDAO().getListCuentasTitularPaged(deudaContribSearchPage,deudaContribSearchPage.getFirstResult(),MAX_CUENTAS_BY_CONTRIB);

			this.setSuperaMaxCantCuentas(true);
			this.setCountCuentasContrib(countCuentasTitular);
		} else {
			this.setSuperaMaxCantCuentas(false);
			//listCuentaTitularLimit = this.getListCuentaTitular();
			listCuentaTitularLimit = PadDAOFactory.getContribuyenteDAO().getListCuentasTitularPaged(deudaContribSearchPage,null,null);
		}
		
		
		log.debug(funcName + " Exit");
		
		// Cambio cuenta por cuentaTitular en bug 678, para mostrar fechaDesde, fechaHasta y estado
		/*for (CuentaTitular ct : listCuentaTitularLimit) {
			// Cambio intruducido 22-01-2009 por bug 654, solo mostramos las cuentas para CuentaTitular vigentes.
			//if (ct.getFechaHasta() == null || DateUtil.isDateAfterOrEqual(ct.getFechaHasta(), new Date()) )
			listCuenta.add(ct.getCuenta());
		}*/

		return listCuentaTitularLimit;
		
	}
	
	
	/** Recupera una lista de todas las exencines vigentes del 
	 *  contribuyente.
	 * 
	 * @return
	 * @deprecated
	 */
	public List<CueExe> getListCueExeVigente() throws Exception {
		List<CueExe> listCueExe = new ArrayList<CueExe>();
		
		for (Cuenta cuenta:this.getListCuenta()) {
			for (CueExe cueExe:cuenta.getListCueExeVigente()) {
				listCueExe.add(cueExe);
			}
		}
		
		return listCueExe;
		
	}

	/**
	 * Devuelve las cuentas en las que el contribuyente es titular principal
	 * @return
	 */
	public List<Cuenta> getListCuentaVigentesForTitular(Long skip, Long first){
		return PadDAOFactory.getContribuyenteDAO().getListCuentaVigentesForTitular(this.getId(), skip, first);
	}

	/**
	 * Obtiene las cuentas vigentes para el contribuyente, filtrando por el recurso pasado como parametro
	 * @param recurso
	 * @param cuentaExcluir - Esta cuenta se va a excluir de la lista que retorna - si es null no se tiene en cuenta
	 * @return
	 */
	public List<Cuenta> getListCueVig4Titular(Recurso recurso, Cuenta cuentaExcluir) {
		return PadDAOFactory.getContribuyenteDAO().getListCueVig4Titular(recurso, this, cuentaExcluir);
	}
	
	 @Override
	public String infoString() {
		 String ret =" Contribuyente";

		if(id!=null){
			ret+=" - Id: "+id;
		}
			
		if(persona!=null){
			ret+=" - Persona: "+persona.getRepresent();
		}
				
		if(idCaso!=null){
			ret+=" - Caso: "+idCaso;
		}
		return ret;
	}

	/**
	 * Devulve una lista de cuentas para el Recurso indicado obtenidas de CuentaTitular.
	 * 
	 * 
	 * @return
	 */
	public List<Cuenta> getListCuentaByRecurso(Long idRecurso){
		List<Cuenta> listCuenta = new ArrayList<Cuenta>();
		
		for (CuentaTitular ct : this.getListCuentaTitular()) {
			if(ct.getCuenta().getRecurso().getId().longValue() == idRecurso.longValue())
			listCuenta.add(ct.getCuenta());
		}

		return listCuenta;
	}

	/**
	 * Retorna true si y solo si el atributo
	 * es un atributo del contribuyente
	 */
	public static boolean esAtributoContribuyente(Atributo atributo) {
		for (ConAtr conAtr: ConAtr.getListActivos()) {
			if (conAtr.getAtributo().equals(atributo))
				return true;
		}
				
		return false;
	}
	
	public boolean getEsConvenioMultilateral(){
		if(this.getNroIsib()!=null && this.getNroIsib().length()>3 && new Long(this.getNroIsib().substring(0, 3))>900L){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean getEsConvenioMultilateral(String nroIngBrutos){
		if(nroIngBrutos!=null && nroIngBrutos.length()>3 && new Long(nroIngBrutos.substring(0, 3))>900L){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean getEsConribuyenteCER() {
		try {
			Atributo atributo = Atributo.getByCodigo(Atributo.COD_CER);
			ConAtrVal cerAtrVal =  getConAtrValVigenteByIdAtributo(atributo.getId());
			
			// Si es contribuyente CER
			if (cerAtrVal.getValor().equals("1")) {
				return true;
			}
			
		} catch (Exception e) {
			log.error("No se pudo obtener el atributo CER");
		}
		
		return false;
	}
	
}
