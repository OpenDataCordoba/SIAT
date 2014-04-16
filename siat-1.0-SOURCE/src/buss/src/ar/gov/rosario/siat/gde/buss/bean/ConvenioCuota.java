//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.bean.IndeterminadoFacade;
import ar.gov.rosario.siat.bal.buss.bean.Sellado;
import ar.gov.rosario.siat.bal.buss.bean.Sistema;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Banco;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.LiqCodRefPagSearchPage;
import ar.gov.rosario.siat.pad.buss.bean.CueExcSel;
import ar.gov.rosario.siat.pad.buss.bean.CueExcSelDeu;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a ConvenioCuota
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_convenioCuota")
public class ConvenioCuota extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	private static final String SEQUENCE_COD_REF_PAG = "gde_concuo_cref_sq"; 
	
	@Transient
	Logger log = Logger.getLogger(ConvenioCuota.class);
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idConvenio") 
	private Convenio convenio;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idSistema") 
	private Sistema sistema;
	
	@Column(name = "codRefPag")
	private Integer codRefPag;
	
	@Column(name = "numeroCuota")
	private Integer numeroCuota;
	
	@Column(name = "fechaVencimiento")
	private Date fechaVencimiento;
	
	@Column(name = "fechaImpresion")
	private Date fechaImpresion;
	
	@Column(name = "capitalCuota")
	private Double capitalCuota;
	
	@Column(name = "interes")
	private Double interes;
	
	@Column(name = "importeCuota")
	private Double importeCuota;
	
	@Column(name="importePago")
	private Double importePago;
	
	@Column(name="importeSellado")
	private Double importeSellado;
	
	@Column(name = "actualizacion")
	private Double actualizacion;
	
	@Column(name = "fechaEmision")
	private Date fechaEmision;
	
	@Column(name = "fechaPago")
	private Date fechaPago;
	
	@Column(name = "nroCuotaImputada")
	private Integer nroCuotaImputada;
	
	@ManyToOne (optional=true)
	@JoinColumn(name="idSellado")
	private Sellado sellado;
	
	/*@ManyToOne(optional=false) 
	@JoinColumn(name="idLiqComPro") 
	private LiqComPro liqComPro; //Liquidacion Comision Procurador */ 
	@Column(name = "idLiqComPro")
	private Long idLiqComPro;
	
	@ManyToOne(optional=false) 
	@JoinColumn(name="idEstadoConCuo") 
	private EstadoConCuo estadoConCuo;
	
	@Column(name = "reclamada")
	private Integer reclamada;
	
	@Column(name = "codPago")
	private Long codPago;	
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idBancoPago") 
	private Banco bancoPago;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;
	
	@Column(name = "idPago")
	private Long idPago;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idTipoPago") 
	private TipoPago tipoPago;
	
	@Transient
	private Date fecVenNoFeriado;
	
	@Transient
	private Date fecPag4Cad;
	
	//<#Propiedades#>
	
	// Constructores
	public ConvenioCuota(){
		super();
		// Seteo de valores default	
		// propiedad_ejemplo = valorDefault;
	}
	
	public ConvenioCuota(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ConvenioCuota getById(Long id) {
		return (ConvenioCuota) GdeDAOFactory.getConvenioCuotaDAO().getById(id);
	}
	
	public static ConvenioCuota getByIdNull(Long id) {
		return (ConvenioCuota) GdeDAOFactory.getConvenioCuotaDAO().getByIdNull(id);
	}
	
	public static ConvenioCuota getByNroCuoIdCon(Long nroCuota, Long idConvenio) throws Exception{
		return (ConvenioCuota) GdeDAOFactory.getConvenioCuotaDAO().getByNroCuoIdCon(nroCuota, idConvenio);
	}
	
	public static ConvenioCuota getByNroCuoNroConSis(Long nroCuota, Long nroConvenio, Long idSistema) throws Exception{
		return (ConvenioCuota) GdeDAOFactory.getConvenioCuotaDAO().getByNroCuoNroConSis(nroCuota, nroConvenio, idSistema);
	}
	
	public static ConvenioCuota getPrimImpByIdConvenio (Long idConvenio) throws Exception {
		return (ConvenioCuota) GdeDAOFactory.getConvenioCuotaDAO().getPrimImpByIdConvenio(idConvenio);
	}
	
	public static ConvenioCuota getByCodRefPag(Long codRefPag) throws Exception{
		return (ConvenioCuota) GdeDAOFactory.getConvenioCuotaDAO().getByCodRefPag(codRefPag);
	}
	
	public static List<ConvenioCuota> getByCodRefPagImporte(LiqCodRefPagSearchPage liqCodRefPagSearchPage) throws Exception{
		return (ArrayList<ConvenioCuota>) GdeDAOFactory.getConvenioCuotaDAO().getByCodRefPagImporte(liqCodRefPagSearchPage);
	}
	
	public static List<ConvenioCuota> getList() {
		return (ArrayList<ConvenioCuota>) GdeDAOFactory.getConvenioCuotaDAO().getList();
	}
	
	public static List<ConvenioCuota> getListActivos() {			
		return (ArrayList<ConvenioCuota>) GdeDAOFactory.getConvenioCuotaDAO().getListActiva();
	}
	
	public static List<ConvenioCuota> getListByConvenioYFecha(Convenio convenio, Date fechaPago) throws Exception {			
		return (ArrayList<ConvenioCuota>) GdeDAOFactory.getConvenioCuotaDAO().getListByConvenioYFecha(convenio, fechaPago);
	}
	
	/**
	 * Cualquiera de los parametros, si es null o <0, no se tiene en cuenta
	 * @param numeroConvenio
	 * @param idRecurso
	 * @param fechaVtoDesde
	 * @param fechaVtoHasta
	 * @param idViaJudicial
	 * @param noLiquidables - valores posibles: null, 0 y 1
	 * @return
	 */
	public static List<ConvenioCuota> getList(Integer numeroConvenio, Long idRecurso,
			Date fechaVtoDesde, Date fechaVtoHasta, Long idViaDeuda, Integer noLiquidables) {
		return (ArrayList<ConvenioCuota>) GdeDAOFactory.getConvenioCuotaDAO().getList(numeroConvenio, 
				idRecurso, fechaVtoDesde, fechaVtoHasta, idViaDeuda, noLiquidables);
	}
	
	/**
	 * Todos los parametros si son nulos o 0 no se tienen en cuenta  
	 * @return
	 * @throws Exception 
	 */
	public static List<ConvenioCuota> getList(Long idConvenio, Date fechaPagoDesde, Date fechaPagoHasta,
			boolean liquidadas, boolean noLiquidadas, Long idEstadoConCuo) throws Exception {
		return (ArrayList<ConvenioCuota>) GdeDAOFactory.getConvenioCuotaDAO().getList(idConvenio, fechaPagoDesde,
				fechaPagoHasta, liquidadas, noLiquidadas, idEstadoConCuo);
	}
	
	public static List<ConvenioCuota> getListByConvenioYFechaPagoHasta(Convenio convenio, Date fechaPago) throws Exception {
		return (ArrayList<ConvenioCuota>) GdeDAOFactory.getConvenioCuotaDAO().getListByConvenioYFechaPagoHasta(convenio, fechaPago);		
	}
	
	// Getters y setters
	public Double getActualizacion() {
		return actualizacion;
	}

	public void setActualizacion(Double actualizacion) {
		this.actualizacion = actualizacion;
	}

	public Double getCapitalCuota() {
		return capitalCuota;
	}

	public void setCapitalCuota(Double capitalCuota) {
		this.capitalCuota = capitalCuota;
	}

	public Integer getCodRefPag() {
		return codRefPag;
	}

	public void setCodRefPag(Integer codRefPag) {
		this.codRefPag = codRefPag;
	}

	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

	public EstadoConCuo getEstadoConCuo() {
		return estadoConCuo;
	}

	public void setEstadoConCuo(EstadoConCuo estadoConCuo) {
		this.estadoConCuo = estadoConCuo;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public Date getFechaImpresion() {
		return fechaImpresion;
	}

	public void setFechaImpresion(Date fechaImpresion) {
		this.fechaImpresion = fechaImpresion;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Double getImporteCuota() {
		return importeCuota;
	}

	public void setImporteCuota(Double importeCuota) {
		this.importeCuota = importeCuota;
	}

	public Double getInteres() {
		return interes;
	}

	public void setInteres(Double interes) {
		this.interes = interes;
	}

	public Integer getNroCuotaImputada() {
		return nroCuotaImputada;
	}

	public void setNroCuotaImputada(Integer nroCuotaImputada) {
		this.nroCuotaImputada = nroCuotaImputada;
	}

	public Integer getNumeroCuota() {
		return numeroCuota;
	}

	public void setNumeroCuota(Integer numeroCuota) {
		this.numeroCuota = numeroCuota;
	}

	public Integer getReclamada() {
		return reclamada;
	}

	public void setReclamada(Integer reclamada) {
		this.reclamada = reclamada;
	}

	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public Long getCodPago() {
		return codPago;
	}

	public void setCodPago(Long codPago) {
		this.codPago = codPago;
	}

	public Long getIdLiqComPro() {
		return idLiqComPro;
	}

	public void setIdLiqComPro(Long idLiqComPro) {
		this.idLiqComPro = idLiqComPro;
	}

	public Asentamiento getAsentamiento() {
		return asentamiento;
	}

	public void setAsentamiento(Asentamiento asentamiento) {
		this.asentamiento = asentamiento;
	}

	public Banco getBancoPago() {
		return bancoPago;
	}

	public void setBancoPago(Banco bancoPago) {
		this.bancoPago = bancoPago;
	}

	public Long getIdPago() {
		return idPago;
	}

	public void setIdPago(Long idPago) {
		this.idPago = idPago;
	}

	public TipoPago getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

	public Double getImportePago() {
		return importePago;
	}

	public void setImportePago(Double importePago) {
		this.importePago = importePago;
	}

	public Double getImporteSellado() {
		return importeSellado;
	}

	public void setImporteSellado(Double importeSellado) {
		this.importeSellado = importeSellado;
	}

	public Sellado getSellado() {
		return sellado;
	}

	public void setSellado(Sellado sellado) {
		this.sellado = sellado;
	}

	public Date getFecVenNoFeriado() {
		return fecVenNoFeriado;
	}

	public void setFecVenNoFeriado(Date fecVenNoFeriado) {
		this.fecVenNoFeriado = fecVenNoFeriado;
	}
	

	public Date getFecPag4Cad() {
		return fecPag4Cad;
	}

	public void setFecPag4Cad(Date fecPag4Cad) {
		this.fecPag4Cad = fecPag4Cad;
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
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		/*/	Validaciones        
		if (StringUtil.isNullOrEmpty(getCodConvenioCuota())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONVENIOCUOTA_CODCONVENIOCUOTA );
		}
		
		if (StringUtil.isNullOrEmpty(getDesConvenioCuota())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CONVENIOCUOTA_DESCONVENIOCUOTA);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codConvenioCuota");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, GdeError.CONVENIOCUOTA_CODCONVENIOCUOTA);			
		}*/
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el ConvenioCuota. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getConvenioCuotaDAO().update(this);
	}

	/**
	 * Desactiva el ConvenioCuota. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getConvenioCuotaDAO().update(this);
	}
	
	/**
	 * Valida la activacion del ConvenioCuota
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del ConvenioCuota
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	
	 /**
     * Calcula el recargo segun la actualizacion del saldo al dia de hoy.
     * @return recargo al dia de hoy correspondiente al importe de la cuota 
     */
    protected DeudaAct actualizacionImporteCuota() throws Exception {
    	
    	boolean exentoAct = this.getConvenio().exentoActualizacion(new Date());
    	log.debug(" ### actualizacionImporteCuota: " + this.getNumeroCuota() + " exentoActualizacion: " + exentoAct);
    	
    	return ActualizaDeuda.actualizar(this.getFechaVencimiento(), 
						    			 this.getCapitalCuota()+ this.getInteres(), 
						    			 exentoAct, true);
    }    
    
    /**
     * Calcula el recargo segun la acutalizacion para la fecha recibida.
     * 
     * @return recargo a la fecha recibida.
     * @throws Exception
     */
    public DeudaAct actualizacionImporteCuota(Date fechaActualizacion) throws Exception {
    	
    	boolean exentoAct = this.getConvenio().exentoActualizacion(fechaActualizacion);
    	log.debug(" ### actualizacionImporteCuota: " + this.getNumeroCuota() + " exentoActualizacion: " + exentoAct);
    	
    	return ActualizaDeuda.actualizar(fechaActualizacion, 
						    			this.getFechaVencimiento(), 
						    			this.getCapitalCuota()+ this.getInteres(),
						    			exentoAct, true);

    }
    
    /**
     * Calcula el recargo segun la actualizacion para la fecha de actualizacion y fecha de vencimiento recibidas.
     * 
     * @return recargo a la fecha recibida.
     * @throws Exception
     */
    public DeudaAct actualizacionImporteCuota(Date fechaActualizacion, Date fechaVencimiento) throws Exception {
    	
    	boolean exentoAct = this.getConvenio().exentoActualizacion(fechaActualizacion);
    	log.debug(" ### actualizacionImporteCuota: " + this.getNumeroCuota() + " exentoActualizacion: " + exentoAct);
    	
    	return ActualizaDeuda.actualizar(fechaActualizacion, 
						    			fechaVencimiento, 
						    			this.getCapitalCuota()+ this.getInteres(),
						    			exentoAct, true);
    }
    
    
    /**
     * Calcula el recargo segun la actualizacion para la fecha recibida. Toma como parametro la lista de Exenciones
     * que tiene que verificar, y la lista de PlanExe.
     * 
     * @return recargo a la fecha recibida.
     * @throws Exception
     */
    public DeudaAct actualizacionImporteCuota(Date fechaActualizacion, List<CueExe> listCueExeNoActDeu,
    		List<PlanExe> listPlanExeNoActDeu) throws Exception {
    	
    	boolean exentoAct = false;
    	if (listPlanExeNoActDeu != null && listPlanExeNoActDeu.size() > 0){
    		exentoAct = true;
    	} else {
    		// fedel: 2009-may-19, bug: 687, para que este exenta solo importa si la la cuexe estaba vigente al
    		//        vencimiento. Por eso comnetamos las dos lineas de abajo.
   	    	//for (CueExe cueExe : listCueExeNoActDeu){
   	    	//	if (DateUtil.isDateInRange(fechaActualizacion, cueExe.getFechaDesde(), cueExe.getFechaHasta())){
   	    	//		exentoAct = true;
   	    	//		break;
   	    	//	}
   	    	//}
    	}
    	return ActualizaDeuda.actualizar(fechaActualizacion, 
						    			this.getFechaVencimiento(), 
						    			this.getCapitalCuota()+ this.getInteres(),
						    			exentoAct, true);
    }
    
    /**
     * Calcula el total de la cuota sumando Capital + Interes + Actualizacion.
     * 
     * @author Cristian
     * @return
     */
    public Double calcularTotal(){
    	
    	Double capCuota = 0D;
    	Double interes = 0D;
    	Double actualizacion = 0D;
    	Double sellado = 0D;
    	
    	if (getCapitalCuota() != null)
    		capCuota = getCapitalCuota();
    		
    	if (getInteres() != null) 
    		interes = getInteres();
    		
    	if (getActualizacion() != null)
    		actualizacion = getActualizacion();
    	
    	if (getSellado()!=null)
    		sellado = getImporteSellado();
    	
    	return capCuota + interes + sellado + actualizacion;
    }

    /**
     * Obtiene el codigo de referencia de pago.
     * 
     * @author Cristian
     * @return
     */
    public Integer obtenerCodRefPag(){
    	return new Integer("" + GdeDAOFactory.getConvenioCuotaDAO().getNextVal(SEQUENCE_COD_REF_PAG));
    }
    
    /**
     * Devuelve el codigo de barra del Convenio Cuota
     * 
     * @author Cristian
     * @return
     */
	public String getNroCodigoBarra(){
		
		String strTotalPagar = "";
		String strFecVto = "";
		String strNroCodBarra = "";
		String strCodServicioBanco = "";
		
		// total a pagar
		strTotalPagar = StringUtil.redondearDecimales(this.getImporteCuota(), 8, 2);
		strTotalPagar = strTotalPagar.replace(",", "");
		
		// fecha de vencimiento
		strFecVto = DateUtil.formatDate(fechaVencimiento, DateUtil.yyMMdd_MASK);
		
		// Servicio Banco
		strCodServicioBanco = this.getSistema().getServicioBanco().getCodServicioBanco();
		
        //if (this.getEsReimpresionCuota() ) {
		
			// codRefPago(9)
			// 3(1) - Significa que es cuota
			// 000(3) - Nu se utiliza actualmente
			// importe(10)
			// 81(2) - Servicio Banco
			// importe(10)
			// fechaVenc(6)
			// fechaVenc(6)
			// DV(1)
			
			
			strNroCodBarra += StringUtil.completarCerosIzq(this.getCodRefPag().toString().trim(),9 ); // codRefPago(9)
			strNroCodBarra += "3"; // 3(1) - Significa que es cuota
			strNroCodBarra += "000"; // 000(3) - Nu se utiliza actualmente
			strNroCodBarra += strTotalPagar; // importe(10)
			strNroCodBarra += StringUtil.completarCerosIzq(strCodServicioBanco.trim(), 2) ; // 81(2) - Servicio Banco
			strNroCodBarra += strTotalPagar; // importe(10)
			strNroCodBarra += strFecVto; // fechaVenc(6)
			strNroCodBarra += strFecVto;// fechaVenc(6)
        
		return StringUtil.agregarDigitoVerificadorCodBar(strNroCodBarra);
	}
	

	
	/**
	 * Devuelve el codigo de barras entre los delimitadores "<" y ">" 
	 * @return
	 */
	public String getNroCodigoBarraDelim(){
		return "<" + getNroCodigoBarra() + ">";
	}

	/**
	 * Devuelve true o false segun sea una cuota indeterminada o no.
	 * 
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public boolean getEsIndeterminada() throws Exception {
		
		return IndeterminadoFacade.getInstance().getEsIndeterminada(this);
	}
	
	/**
	 *  Verifica si tiene Sellado, y en ese caso resta el importeSellado al importeCuota.
	 * 
	 * @return
	 */
	public Double getImporteCuotaSinSellado() {
		if(this.sellado != null){
			return this.importeCuota-this.importeSellado;
		}
		return this.importeCuota;
	}
	

	/**
	 * Determina si la cuota de convenio es reclamada
	 * @return boolean
	 */
	public boolean getEsReclamada(){
		if (this.getReclamada() != null && this.getReclamada().intValue() == 1)
			return true;
		else
			return false;
	}

	/**
	 * Obtiene la lista de Areas que excluyen a la cuenta del convenio del convenioCuota
	 * Un convenioCuota esta excluido si: la lista de CueExcSel activas de la cuenta del convenio,
	 * son solo exenciones por cuenta, (no son exenciones activas por deuda).
	 * @return List<Area>
	 * @throws Exception
	 */
	public List<Area> getEsExcluidaPorArea() throws Exception {

		List<Area> listAreaExcluye = new ArrayList<Area>();

		for (CueExcSel cueExcSel : this.getConvenio().getCuenta().getListCueExcSelActivas()) {

			List<CueExcSelDeu> listCueExcSelDeu = cueExcSel.getListCueExcSelDeuActivas();

			if(listCueExcSelDeu.size() == 0){
				listAreaExcluye.add(cueExcSel.getArea());
			}
		}
		return listAreaExcluye;
	}

	/**
	 * Ordena la lista de Cuotas pasada como parametro, por periodo y anio
	 * @param listCuota
	 * @return
	 * @author arobledo
	 */
    public static List<ConvenioCuota> ordenarNroCuota(List<ConvenioCuota> listConvenioCuota){
    	Comparator<ConvenioCuota> comparator = new Comparator<ConvenioCuota>() {
			public int compare(ConvenioCuota d1, ConvenioCuota d2) {
				//Compara el convenio
				if (d1.getConvenio().getId() > d2.getConvenio().getId()){
					return 1;
				}else if (d1.getConvenio().getId() < d2.getConvenio().getId()){
					return -1;
				}else{
					if (d1.getNumeroCuota().longValue() > d2.getNumeroCuota().longValue()){
						return 1;
					}else if (d1.getNumeroCuota().longValue() < d2.getNumeroCuota().longValue()){
						return -1;
					}
				}
				return 0;
				
			}    		
    	};
    	Collections.sort(listConvenioCuota, comparator);
    	return listConvenioCuota;
    }
    
    public boolean estaImputada()throws Exception{
    	return GdeDAOFactory.getConvenioCuotaDAO().getImputada(this)!=null;
    }
    
    public static List<Long> getListIdCuotaAuxPagCuo(Convenio convenio) throws Exception{
    	List <Long>listIdCuotas = new ArrayList<Long>();
    	for (ConvenioCuota convenioCuota : convenio.getListCuotasImpagas()){
    		listIdCuotas.add(convenioCuota.getId());
    	}
		
		return BalDAOFactory.getAuxPagCuoDAO().getListIdCuotaAuxPagCuo(listIdCuotas);
	}
}
