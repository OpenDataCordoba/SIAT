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
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.bean.Canal;
import ar.gov.rosario.siat.bal.buss.bean.Sellado;
import ar.gov.rosario.siat.def.buss.bean.Banco;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.LiqCodRefPagSearchPage;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a Recibo
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_recibo")
public class Recibo extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Transient
	private static Logger log = Logger.getLogger(Recibo.class);

	public static final String COD_FRM_RECONF_DEUDA_TGI="TGI_RD";
	public static final String COD_FRM_RECIBO_CUOTA="RECIBO_CUOTA";
	public static final String COD_FRM_RECIBO_CDM="RECIBO_CDM";
	public static final String COD_FRM_PADRON_CDM="REP_PADRON_CDM";
	public static final String COD_FRM_REPOBRA_IMP_CDM="REP_OBRA_IMPCDM";
	public static final String COD_FRM_RECIBO_TGI="RECIBO_TGI";
	public static final String COD_FRM_RECONF_DEUDA_DREI_BLANCO="DREI_RD";
	public static final String COD_FRM_RECONF_DEUDA_DERPUB="DERPUB_RD";
	public static final String COD_FRM_RECIBO_DEUDA="RECIBO_DEUDA";
	
	public static final String COD_FRM_NOTIF_PREENVIO_JUD="NOTIF_PREENVIO_JUD";
	
	@Column(name = "codRefPag")
	private Long codRefPag;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idServicioBanco")
	private ServicioBanco servicioBanco;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idRecurso")
	private Recurso recurso;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idViaDeuda")
	private ViaDeuda viaDeuda;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCuenta")
	private Cuenta cuenta;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCanal")
	private Canal canal;

	@Column(name = "nroRecibo")
	private Long nroRecibo=0L;

	@Column(name = "anioRecibo")
	private Integer anioRecibo=0;

	@Column(name = "fechaGeneracion")
	private Date fechaGeneracion;

	@Column(name = "fechaVencimiento")
	private Date fechaVencimiento;

	@Column(name = "fechaPago")
	private Date fechaPago;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idDesGen")
	private DesGen desGen;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idDesEsp")
	private DesEsp desEsp;

	@Column(name = "totCapitalOriginal")
	private Double totCapitalOriginal=0D;

	@Column(name = "desCapitalOriginal")
	private Double desCapitalOriginal=0D;

	@Column(name = "totActualizacion")
	private Double totActualizacion=0D;

	@Column(name = "desActualizacion")
	private Double desActualizacion=0D;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idSellado")
	private Sellado sellado;

	@Column(name = "importeSellado")
	private Double importeSellado=0D;

	@Column(name = "totImporteRecibo")
	private Double totImporteRecibo=0D;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idBancoPago")
	private Banco bancoPago;
	
	@Column(name = "estaImpreso")
	private Integer estaImpreso;

	@Column(name = "noLiqComPro")
	private Integer noLiqComPro;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idProcesoMasivo")
	private ProcesoMasivo procesoMasivo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idprocurador")
	private Procurador procurador;
	
	@OneToMany( mappedBy="recibo")
	@JoinColumn(name="idRecibo")
	@Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
	private List<ReciboDeuda> listReciboDeuda = new ArrayList<ReciboDeuda>();
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;

	@Column(name = "idBroche")
	private Long idBroche;

	@Column(name = "observacion")
	private String observacion = "";
	
	@Column(name = "esVolPagIntRS")
	private Integer esVolPagIntRS;

	@Transient
	private Boolean aplicarSellado = true;// s'olo es TRUE para los primeros recibos de la lista de recibos cortados, de un recibo original 

	
	/*
	 * agregados 29.04.08 - para poder reimprimir deuda tal cual esta - por temas de recibos
	 */
	
	@Transient
	private Boolean esReimpresionDeuda = false; // que sea reimpresionDeuda implica que se usa para la reconfeccion, pero no se graba para nada.
	                                            // se utiliza para reimprimir deuda no vencida sin generar ningun recibo

	@Transient
	private Long  anio = 0L;
	
	@Transient
	private Long periodo = 0L;
	
	@Transient
	private Long idSistema = 0L;
	
	@Transient
	private Date fechaGracia = new Date();
	
	@Column(name="idLiqComPro")
	private Long idLiqComPro;
	
	/**
	 * Datos especificos a un recibo de 
	 * Contribucion de Mejoras
	 * */
	@Transient
	private DatosReciboCdM datosReciboCdM;
	
	
	@Column(name = "esCuotaSaldo")
	private Integer esCuotaSaldo;
	
	@Transient
	private boolean pagaVencido=true;// Esta variable se setea en el metodo getNroCodigoBarra() 
	
	@Transient 
	private String nroCodigoBarra = null; 

	@Transient 
	private String codBarForTxt = null; 

	
	// Constructores
	public Recibo() {
		super();
		// Seteo de valores default
	}

	public Recibo(Long id) {
		super();
		setId(id);
	}
   
	// Metodos de Clase
	public static Recibo getById(Long id) {
		return (Recibo) GdeDAOFactory.getReciboDAO().getById(id);
	}

	public static Recibo getByIdNull(Long id) {
		return (Recibo) GdeDAOFactory.getReciboDAO().getByIdNull(id);
	}

	public static Recibo getByCodRefPag(Long codRefPag){
		return (Recibo) GdeDAOFactory.getReciboDAO().getByCodRefPag(codRefPag);
	}
	
	public static List<Recibo> getByCodRefPagImporte(LiqCodRefPagSearchPage liqCodRefPagSearchPage) throws Exception {
		return (List<Recibo>) GdeDAOFactory.getReciboDAO().getByCodRefPagImporte(liqCodRefPagSearchPage);
	}
	
	
	/**
	 * TODO: Ver si lo podemos reemplazar por el getByNumero.
	 * 
	 */
	@Deprecated
	public static Recibo getByNroAnioSis(Long numero, Long anio, Long nroSistema){
		return (Recibo) GdeDAOFactory.getReciboDAO().getByNroAnioSis(numero,anio,nroSistema);
	}
	
	public static Recibo getByNumero(Long numero){
		return (Recibo) GdeDAOFactory.getReciboDAO().getByNumero(numero);
	}
	
	public static List<Recibo> getList() {
		return (ArrayList<Recibo>) GdeDAOFactory.getReciboDAO().getList();
	}

	public static List<Recibo> getListActivos() {
		return (ArrayList<Recibo>) GdeDAOFactory.getReciboDAO().getListActiva();
	}

	/**
	 * Cualquiera de los parametros, si es null o <0, no se tiene en cuenta
	 * @param numeroRecibo
	 * @param idRecurso
	 * @param fechaEmisionDesde
	 * @param fechaEmisionHasta
	 * @param noLiquidables - valores posibles: null, 0 y 1
	 * @return
	 */
	public static List<Recibo> getList(Long numeroRecibo, Long idRecurso, Date fechaEmisionDesde, 
			Date fechaEmisionHasta, Long idVia, Long idProcurador, Integer noLiquidables) {
		return (ArrayList<Recibo>) GdeDAOFactory.getReciboDAO().getList(numeroRecibo, idRecurso, fechaEmisionDesde, 
				fechaEmisionHasta, idVia, idProcurador, noLiquidables);
	}

	public static List<Recibo> getList(List<Procurador> listProcurador,
			Long idRecurso, long idViaDeuda, Integer firstResult, Integer maxResults) throws Exception {
		return (ArrayList<Recibo>) GdeDAOFactory.getReciboDAO().getList(listProcurador,idRecurso, idViaDeuda,
																		firstResult, maxResults);
	}
	
	public static List<Recibo> getListByListRecurso(List<Procurador> listProcurador,
			List<Recurso> listRecurso, long idViaDeuda, Integer firstResult, Integer maxResults) throws Exception {
		return (ArrayList<Recibo>) GdeDAOFactory.getReciboDAO().getListByListRecurso(listProcurador,listRecurso, idViaDeuda,
																		firstResult, maxResults);
	}
	
	// Getters y setters

	public Integer getAnioRecibo() {
		return anioRecibo;
	}

	public void setAnioRecibo(Integer anioRecibo) {
		this.anioRecibo = anioRecibo;
	}

	public Canal getCanal() {
		return canal;
	}

	public void setCanal(Canal canal) {
		this.canal = canal;
	}

	public Long getCodRefPag() {
		return codRefPag;
	}

	public void setCodRefPag(Long codRefPag) {
		this.codRefPag = codRefPag;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Double getDesActualizacion() {
		return desActualizacion;
	}

	public void setDesActualizacion(Double desActualizacion) {
		this.desActualizacion = desActualizacion;
	}

	public Double getDesCapitalOriginal() {
		return desCapitalOriginal;
	}

	public void setDesCapitalOriginal(Double desCapitalOriginal) {
		this.desCapitalOriginal = desCapitalOriginal;
	}

	public DesEsp getDesEsp() {
		return desEsp;
	}

	public void setDesEsp(DesEsp desEsp) {
		this.desEsp = desEsp;
	}

	public DesGen getDesGen() {
		return desGen;
	}

	public void setDesGen(DesGen desGen) {
		this.desGen = desGen;
	}

	public Integer getEstaImpreso() {
		return estaImpreso;
	}

	public void setEstaImpreso(Integer estaImpreso) {
		this.estaImpreso = estaImpreso;
	}

	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}

	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Double getImporteSellado() {
		return this.importeSellado;
	}

	public void setImporteSellado(Double importeSellado) {
		this.importeSellado = importeSellado;
	}
	

	public Long getNroRecibo() {
		return nroRecibo;
	}

	public void setNroRecibo(Long nroRecibo) {
		this.nroRecibo = nroRecibo;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Sellado getSellado() {
		return sellado;
	}

	public void setSellado(Sellado sellado) {
		this.sellado = sellado;
	}

	public ServicioBanco getServicioBanco() {
		return servicioBanco;
	}

	public void setServicioBanco(ServicioBanco servicioBanco) {
		this.servicioBanco = servicioBanco;
	}

	public Double getTotActualizacion() {
		return totActualizacion;
	}

	public void setTotActualizacion(Double totActualizacion) {
		this.totActualizacion = totActualizacion;
	}

	public Double getTotCapitalOriginal() {
		return totCapitalOriginal;
	}

	public void setTotCapitalOriginal(Double totCapitalOriginal) {
		this.totCapitalOriginal = totCapitalOriginal;
	}

	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

	public List<ReciboDeuda> getListReciboDeuda() {
		return listReciboDeuda;
	}

	public void setListReciboDeuda(List<ReciboDeuda> listReciboDeuda) {
		this.listReciboDeuda = listReciboDeuda;
	}

	public Double getTotImporteRecibo() {
		return totImporteRecibo;
	}

	public void setTotImporteRecibo(Double totImporteRecibo) {
		this.totImporteRecibo = totImporteRecibo;
	}

	public Boolean getAplicarSellado() {
		return aplicarSellado;
	}

	public void setAplicarSellado(Boolean aplicarSellado) {
		this.aplicarSellado = aplicarSellado;
	}
	
	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Banco getBancoPago() {
		return bancoPago;
	}
	
	public void setBancoPago(Banco bancoPago) {
		this.bancoPago = bancoPago;
	}
			
	public Asentamiento getAsentamiento() {
		return asentamiento;
	}

	public void setAsentamiento(Asentamiento asentamiento) {
		this.asentamiento = asentamiento;
	}
	

	public Long getIdLiqComPro() {
		return idLiqComPro;
	}

	public void setIdLiqComPro(Long idLiqComPro) {
		this.idLiqComPro = idLiqComPro;
	}
	

	public Integer getNoLiqComPro() {
		return noLiqComPro;
	}

	public void setNoLiqComPro(Integer noLiqComPro) {
		this.noLiqComPro = noLiqComPro;
	}
	
	public DatosReciboCdM getDatosReciboCdM() {
		return datosReciboCdM;
	}

	public void setDatosReciboCdM(DatosReciboCdM datosReciboCdM) {
		this.datosReciboCdM = datosReciboCdM;
	}

	
	public Integer getEsCuotaSaldo() {
		return esCuotaSaldo;
	}
	public void setEsCuotaSaldo(Integer esCuotaSaldo) {
		this.esCuotaSaldo = esCuotaSaldo;
	}

	public boolean getPagaVencido() {
		return pagaVencido;
	}

	public void setPagaVencido(boolean pagaVencido) {
		this.pagaVencido = pagaVencido;
	}

	public Integer getEsVolPagIntRS() {
		return esVolPagIntRS;
	}

	public void setEsVolPagIntRS(Integer esVolPagIntRS) {
		this.esVolPagIntRS = esVolPagIntRS;
	}

	// metodos de instancia
	public Procurador getProcurador() {
		return procurador;
	}

	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}

	/**
	 * Esta funcion arma el codigo de barras para el recibo considerando los siguientes casos:
	 *  . Reimpresion
	 *    . TGI
	 *       . Deuda vieja migrada 
	 *       . Deuda generada por SIAT
	 *    . CdM
	 *       . Deuda vieja migrada
	 *       . Deuda generada por SIAT
	 *           
	 *  . Reconfecci'on
	 *    . TGI
	 *    
	 *  NOTA IMPORTANTE: Debemos ver como vamos a tratar a la reimpresion/reconfecci'on de Cuotas de convenios
	 *  <br><br>Ademas setea la variable "pagaVencido" teniendo en cuenta la segunda fecVto y el segundo importe del codBar 
	 * @return
	 */
	/*
	public String getNroCodigoBarra(){
	
		if (StringUtil.isNullOrEmpty(nroCodigoBarra)) {
			
			String strTotalPagar = "";
			String strFecVto = "";
			String strNroCodBarra = "";
	
			// total a pagar
			strTotalPagar = StringUtil.redondearDecimales(totImporteRecibo, 8, 2);
			strTotalPagar = strTotalPagar.replace(",", "");
			
			// fecha de vencimiento
			strFecVto = DateUtil.formatDate(fechaVencimiento, DateUtil.yyMMdd_MASK);
	
			// por ahora vamos a reimprimir/reconfeccionar deuda de TGI
	        if (this.getEsReimpresionDeuda() ) {
	        	
	        	log.debug("getNroCodigoBarra -> Es Reimpresion Deuda");
	        	
        		String strSndTotalAPagar = "0000000000";
        		String strSndFecVto = "000000";
        		
        		//En caso de que sea un deuda de CdM
        		if (this.getServicioBanco().getCodServicioBanco().trim().equals("82")) {
        			strSndTotalAPagar = strTotalPagar;
        			strSndFecVto = strFecVto; 
        		}
	        	
	        	// impresion de la deuda tal cual esta
	        	if (this.getCodRefPag() == 0L) {
	        		
	        		log.debug("		getNroCodigoBarra -> codRefPago = 0");
	        		
	        		// es deuda vieja generada por Cobol
		        	strNroCodBarra += StringUtil.completarCerosIzq(this.getCuenta().getNumeroCuenta().trim(),9 ); // nro-cuenta (9)
		        	if ("82".equals(this.getServicioBanco().getCodServicioBanco())){
		        		strNroCodBarra += StringUtil.completarCerosIzq(this.getPeriodo().toString(), 4);
		        	}else{
			        	String anio = StringUtil.completarCerosIzq(this.getAnio().toString().trim(), 2); 
			        	// cambio 17-11-08. ahora sale: MMAA
			        	strNroCodBarra += StringUtil.completarCerosIzq(this.getPeriodo().toString().trim(), 2); // periodo (2)
			        	strNroCodBarra += anio.substring(anio.length()-2); // anio (2)
		        	}
		        	strNroCodBarra += strTotalPagar; // monto a pagar (10)
		        	strNroCodBarra += StringUtil.completarCerosIzq(this.getIdSistema().toString().trim(), 2); // sistema (2)
		        	strNroCodBarra += strSndTotalAPagar; // ceros (10)
		        	strNroCodBarra += strFecVto; // fecha vencimiento (6)
		        	strNroCodBarra += strSndFecVto;
		        	
	        	} else {
	        		
	        		log.debug("		getNroCodigoBarra -> codRefPago <> 0");
	        		
	           		// es una reimresion de deuda generada por SIAT.
	        		// siempre tiene servicioBanco. 
	        		// hay alguons recursos que no cobran con ID (CdM) en ese caso, habria que mandar los segundos importes 
	        		// y vencimientos en cero para que el banco se de cuenta y los cobre.
	        		strNroCodBarra += StringUtil.completarCerosIzq(String.valueOf(codRefPag), 9); // codRefPago(9)
	        		strNroCodBarra += "1"; // 1(1) es una deuda original
	        		strNroCodBarra += "000"; // 000(3)
	        		strNroCodBarra += strTotalPagar;  // importe(10)
	        		strNroCodBarra += StringUtil.completarCerosIzq(this.getServicioBanco().getCodServicioBanco().trim(), 2); // 81 para TGI
	        		strNroCodBarra += strSndTotalAPagar; // ceros (10)
	        		strNroCodBarra += strFecVto; // fecVenc(6)
		        	strNroCodBarra += strSndFecVto;
	        	}
	        	
	        } else {
	        	
	        	log.debug("getNroCodigoBarra -> NO Es Reimpresion Deuda");
	        	
	        	// impresion de un recibo
	        	// el codigo de barras es el mismo para todos los casos.
	    		strNroCodBarra += StringUtil.completarCerosIzq(String.valueOf(codRefPag), 9); // codRefPago(9)
	    		
	    			log.debug("1 strNroCodBarra " + strNroCodBarra); 
	    		
	    		strNroCodBarra += "2"; // 2(1) es un recibo de deuda
	    		
	    			log.debug("2 strNroCodBarra " + strNroCodBarra);
	    		
	    		strNroCodBarra += "000"; // 000(3)
	    		
	    			log.debug("3 strNroCodBarra " + strNroCodBarra);
	    		
	    		strNroCodBarra += strTotalPagar;  // importe(10)
	    		
	    			log.debug("4 strNroCodBarra " + strNroCodBarra);
	    		
	    		strNroCodBarra += StringUtil.completarCerosIzq(this.getServicioBanco().getCodServicioBanco().trim(), 2); // 81 para TGI
	    		
	    			log.debug("5 strNroCodBarra " + strNroCodBarra);
	    		
	    		strNroCodBarra += strTotalPagar; // importe(10) se repite porque no se cobra recibo con ID
	    		
	    			log.debug("6 strNroCodBarra " + strNroCodBarra);
	    		
	    		strNroCodBarra += strFecVto; // fecVenc(6)
	    		
	    			log.debug("7 strNroCodBarra " + strNroCodBarra);
	    		
	    		strNroCodBarra += strFecVto; // fecVenc(6) se repite porque no se cobra recibo con ID
	    		pagaVencido = false;// cuando la segunda fecVto y el segundo importe son != 0, el recibo es valido solo hasta la fecha de Vto.
	        }
	        
	        log.debug("strNroCodBarra antes de digito verificador: " + strNroCodBarra);
	        
			nroCodigoBarra = StringUtil.agregarDigitoVerificadorCodBar(strNroCodBarra);
		}
		
		return nroCodigoBarra;
	}*/
		
	/**
	 * Devuelve el codigo de barras entre los delimitadores "<" y ">" 
	 * @return
	 */
	public String getNroCodigoBarraDelim(){
		return "<" + getNroCodigoBarra() + ">";
	}

	/**
	 * Devuelve el una representacion del codigo de barras  
	 * con n,N,w,W.
	 * @return
	 */
	public String getCodBarForTxt() throws Exception {
		
		if (StringUtil.isNullOrEmpty(codBarForTxt)) 
			codBarForTxt = StringUtil.genCodBarForTxt(getNroCodigoBarra());
		
		return codBarForTxt;
	}

	/**
	 * Devuelve el codigo de barras para txt entre los delimitadores 
	 * "<" y ">" 
	 * @return
	 */
	public String getCodBarForTxtDelim() throws Exception{
		return "<" + getCodBarForTxt() + ">";
	}

	/**
	 * Setea el descuento, dependiendo de si es un DesGen o un DesEsp
	 */
	public void setDescuento(Object descuento){
		if(descuento instanceof DesGen)
			setDesGen((DesGen) descuento);
		else
			setDesEsp((DesEsp)descuento);
	}

	/**
	 * Parte el recibo en varios recibos (si es necesario), donde c/u tiene como m�ximo cantMaximaRegistros.
	 * <br>S�lo el 1� de la listaRecibosCortados que devuelve tiene seteado el Sellado, el resto lo tiene en NULL.
	 * <BR>A cada recibo se le recalcula los valores (capital, actualizacion, total)  
	 */
	public List<Recibo> cortar(int cantMaximaRegistros){
		int cantReciboDeuda = listReciboDeuda.size();
		List<Recibo> listReciboCortado = new ArrayList<Recibo>();
		List<ReciboDeuda> listReciboDeudaSacar = new ArrayList<ReciboDeuda>();
		listReciboCortado.add(this);//agrega el recibo actual a la lista a devolver
		Recibo reciboActual = null;
		boolean cortado=false;
		boolean creaNuevoRecibo=false;
		if (recurso.getEsAutoliquidable().intValue() == SiNo.SI.getId().intValue()){
			listReciboDeuda=ReciboDeuda.ordenarImporteCero(listReciboDeuda);
		}
		for(int i=0;i<cantReciboDeuda;i++){//recorre todos los reciboDeuda que contiene
			
			ReciboDeuda reciboDeuda = (ReciboDeuda) listReciboDeuda.get(i);
			log.debug("cantReciboDeuda" + i);
			log.debug("Importe: "+ reciboDeuda.getDeuda().getImporte().doubleValue());
			log.debug("cortar: "+String.valueOf(cortado));
			
			if(i>=cantMaximaRegistros || ((reciboDeuda.isValorCero() && i > 0) || cortado)){// los primeros los deja en este recibo
				
				// Si igualamos la cantidad de registros de deuda por recibo, o el importe de la deuda es 0, 
				// Creamos nuevo recibo.
				if(i % cantMaximaRegistros == 0 || reciboDeuda.isValorCero() || creaNuevoRecibo){//a partir de este registro de reciboDeuda, hay que crear otro recibo
					reciboActual = cuenta.crearRecibo(fechaVencimiento, getCanal(), getViaDeuda(), null, getProcurador(), getIdBroche());
					reciboActual.setSellado(null);//se le aplica el sellado s�lo al primer recibo
					listReciboCortado.add(reciboActual);//lo agrega a la lista que va a devolver
					cortado=true;
					log.debug("++++++++++++++++++++++entro con importe: "+reciboDeuda.getDeuda().getImporte().doubleValue());
				}
				
				// 20-01-09: faltaba setear el nuevo recibo en el reciboDeuda pasado al otro recibo
				
				reciboDeuda.setRecibo(reciboActual);
				log.debug("SETEO RECIBODEUDA: "+reciboDeuda.getDeuda().getStrPeriodo());
				log.debug("AL RECIBO "+reciboActual.getNroRecibo());
				reciboActual.getListReciboDeuda().add(listReciboDeuda.get(i));//agrega el registro actual al recibo que se est� llenando
				reciboActual.recalcularValores();
				listReciboDeudaSacar.add(listReciboDeuda.get(i));//agrega el registro a la lista que hay que sacar al finalizar la iteracion, del recibo original
				
				
			}else{
				log.debug("EL RECIBODEUDA: "+reciboDeuda.getDeuda().getStrPeriodo());
			}
			if (reciboDeuda.isValorCero())
				creaNuevoRecibo=true;
			else
				creaNuevoRecibo=false;
		}
		listReciboDeuda.removeAll(listReciboDeudaSacar);//Elimina del recibo, los registros que fueron puestos en otros recibos
		recalcularValores();
		return listReciboCortado;
	}
	
	public void recalcularValores(){
		double totCapitalOriginalNuevo = 0D;
		double desCapitalOriginalNuevo = 0D;
		double totActualizacionNuevo = 0D;
		double desActualizacionNuevo = 0D;
		for(ReciboDeuda reciboDeuda: listReciboDeuda){
			totActualizacionNuevo += reciboDeuda.getTotActualizacion();
			totCapitalOriginalNuevo += reciboDeuda.getTotCapital();
			desCapitalOriginalNuevo += reciboDeuda.getDesCapitalOriginal();
			desActualizacionNuevo += reciboDeuda.getDesActualizacion();
		}
		setTotCapitalOriginal(totCapitalOriginalNuevo);
		setDesCapitalOriginal(desCapitalOriginalNuevo);
		setTotActualizacion(totActualizacionNuevo);
		setDesActualizacion(desActualizacionNuevo);
		double importeSellado = (getSellado()!=null?getSellado().getImporteSellado():0);
		setImporteSellado(importeSellado);
		if (totCapitalOriginalNuevo!=0)
			setTotImporteRecibo(getTotCapitalOriginal() + getTotActualizacion() + importeSellado);
		else
			setTotImporteRecibo(0D);
	}



	public int getCantPeriodos() {
		// TODO Ver como se obtiene la cantidad de periodods de un recibo - arobledo
		return listReciboDeuda.size();
	}

	public Boolean getEsReimpresionDeuda() {
		return esReimpresionDeuda;
	}

	public void setEsReimpresionDeuda(Boolean esReimpresionDeuda) {
		this.esReimpresionDeuda = esReimpresionDeuda;
	}

	public Long getAnio() {
		return anio;
	}

	public void setAnio(Long anio) {
		this.anio = anio;
	}

	public Date getFechaGracia() {
		return fechaGracia;
	}

	public void setFechaGracia(Date fechaGracia) {
		this.fechaGracia = fechaGracia;
	}

	public Long getIdSistema() {
		return idSistema;
	}

	public void setIdSistema(Long idSistema) {
		this.idSistema = idSistema;
	}

	public Long getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
	}

	public ProcesoMasivo getProcesoMasivo() {
		return procesoMasivo;
	}

	public void setProcesoMasivo(ProcesoMasivo procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}

	public Long getIdBroche() {
		return idBroche;
	}

	public void setIdBroche(Long idBroche) {
		this.idBroche = idBroche;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	/**
     * Devuelve el Valor del Atributo asociado a las Deudas del convenio.
     * 
     * @param idAtributo
     * @param fechaVigencia
     * @return
     */
    public String getValorAtributo(){
    	List<ReciboDeuda> listReciboDeuda = this.getListReciboDeuda();
    	if(listReciboDeuda != null && listReciboDeuda.isEmpty()){
    			ReciboDeuda reciboDeuda = listReciboDeuda.get(0);
    			if(reciboDeuda != null){
    				Deuda deuda = reciboDeuda.getDeuda();
    				if(deuda != null)
    					return deuda.getAtrAseVal();
    			}
    	}
    	return null;
    }


	
	public String getNroCodigoBarra() {
		// Si no estaba cargado
		if (StringUtil.isNullOrEmpty(this.nroCodigoBarra)) {
	        
			boolean esVolPagIntRS = false;
			if(this.getEsVolPagIntRS() != null && this.getEsVolPagIntRS().intValue() == SiNo.SI.getId().intValue()){
					esVolPagIntRS = true;
			}
			
			// Generamos el codigo
			this.nroCodigoBarra = createNumCodBar(this.cuenta, this.codRefPag, this.servicioBanco, 
	        		this.idSistema, this.anio, this.periodo,this.totImporteRecibo, this.fechaVencimiento, 
	        		this.esReimpresionDeuda, esVolPagIntRS);
		
			if (!this.esReimpresionDeuda) {
				this.pagaVencido = false; // cuando la segunda fecVto y el segundo importe son != 0, 
										  // el recibo es valido solo hasta la fecha de Vto.
			}
		}
		
		return this.nroCodigoBarra;
	}
	
    
	/**
	 * Generacion de numero de barra para recibos. 
	 * 
	 * @param cuenta
	 * @param codRefPag
	 * @param servicioBanco
	 * @param idSistema
	 * @param anio
	 * @param periodo
	 * @param totImporteRecibo
	 * @param fechaVencimiento
	 * @param esReimpresionDeuda
	 * @return
	 */
	public static String createNumCodBar(Cuenta cuenta, Long codRefPag, ServicioBanco servicioBanco,
	    		Long idSistema, Long anio, Long periodo, Double totImporteRecibo, Date fechaVencimiento,
	    		boolean esReimpresionDeuda) {
		  return createNumCodBar(cuenta,codRefPag,servicioBanco,idSistema,anio,periodo,totImporteRecibo,fechaVencimiento,esReimpresionDeuda,false);
    }
	  
	/**
	 *  Generacion de Numero de Barra
	 *  
	 *  (Nota: Se sobre carga agregando otro parametro para generar volantes de pagos de intereses de rs.)
	 * 
	 * @param cuenta
	 * @param codRefPag
	 * @param servicioBanco
	 * @param idSistema
	 * @param anio
	 * @param periodo
	 * @param totImporteRecibo
	 * @param fechaVencimiento
	 * @param esReimpresionDeuda
	 * @param esVolantePagoIntRS
	 * @return
	 */
    public static String createNumCodBar(Cuenta cuenta, Long codRefPag, ServicioBanco servicioBanco,
    		Long idSistema, Long anio, Long periodo, Double totImporteRecibo, Date fechaVencimiento,
    		boolean esReimpresionDeuda, boolean esVolantePagoIntRS) {
    	
		String strTotalPagar = "";
		String strFecVto = "";
		String strNroCodBarra = "";

		// total a pagar
		strTotalPagar = StringUtil.redondearDecimales(totImporteRecibo, 8, 2);
		strTotalPagar = strTotalPagar.replace(",", "");
		
		// fecha de vencimiento
		strFecVto = DateUtil.formatDate(fechaVencimiento, DateUtil.yyMMdd_MASK);

		// por ahora vamos a reimprimir/reconfeccionar deuda de TGI
        if (esReimpresionDeuda) {
        	
        	log.debug("getNroCodigoBarra -> Es Reimpresion Deuda");
        	
    		String strSndTotalAPagar = "0000000000";
    		String strSndFecVto = "000000";
    		
    		//En caso de que sea un deuda de CdM
    		if (servicioBanco.getCodServicioBanco().trim().equals("82") || 
    				servicioBanco.getCodServicioBanco().trim().equals("85")) {
    			strSndTotalAPagar = strTotalPagar;
    			strSndFecVto = strFecVto; 
    		}
        	
        	// impresion de la deuda tal cual esta
        	if (codRefPag == 0L) {
        		
        		log.debug("		getNroCodigoBarra -> codRefPago = 0");
        		
        		// es deuda vieja generada por Cobol
	        	strNroCodBarra += StringUtil.completarCerosIzq(cuenta.getNumeroCuenta().trim(),9 ); // nro-cuenta (9)
	        	if ("82".equals(servicioBanco.getCodServicioBanco())){
	        		strNroCodBarra += StringUtil.completarCerosIzq(periodo.toString(), 4);
	        	}else{
		        	String strAnio = StringUtil.completarCerosIzq(anio.toString().trim(), 2); 
		        	// cambio 17-11-08. ahora sale: MMAA
		        	strNroCodBarra += StringUtil.completarCerosIzq(periodo.toString().trim(), 2); // periodo (2)
		        	strNroCodBarra += strAnio.substring(strAnio.length()-2); // anio (2)
	        	}
	        	strNroCodBarra += strTotalPagar; // monto a pagar (10)
	        	strNroCodBarra += StringUtil.completarCerosIzq(idSistema.toString().trim(), 2); // sistema (2)
	        	strNroCodBarra += strSndTotalAPagar; // ceros (10)
	        	strNroCodBarra += strFecVto; // fecha vencimiento (6)
	        	strNroCodBarra += strSndFecVto;
	        	
        	} else {
        		
        		log.debug("		getNroCodigoBarra -> codRefPago <> 0");
        		
           		// es una reimresion de deuda generada por SIAT.
        		// siempre tiene servicioBanco. 
        		// hay alguons recursos que no cobran con ID (CdM) en ese caso, habria que mandar los segundos importes 
        		// y vencimientos en cero para que el banco se de cuenta y los cobre.
        		strNroCodBarra += StringUtil.completarCerosIzq(String.valueOf(codRefPag), 9); // codRefPago(9)
        		strNroCodBarra += "1"; // 1(1) es una deuda original
        		strNroCodBarra += "000"; // 000(3)
        		strNroCodBarra += strTotalPagar;  // importe(10)
        		strNroCodBarra += StringUtil.completarCerosIzq(servicioBanco.getCodServicioBanco().trim(), 2); // 81 para TGI
        		strNroCodBarra += strSndTotalAPagar; // ceros (10)
        		strNroCodBarra += strFecVto; // fecVenc(6)
	        	strNroCodBarra += strSndFecVto;
        	}
        	
        } else {
        	
        	log.debug("getNroCodigoBarra -> NO Es Reimpresion Deuda");
        	
        	// impresion de un recibo
        	// el codigo de barras es el mismo para todos los casos.
    		strNroCodBarra += StringUtil.completarCerosIzq(String.valueOf(codRefPag), 9); // codRefPago(9)
    		
    			log.debug("1 strNroCodBarra " + strNroCodBarra); 
    		
    		strNroCodBarra += "2"; // 2(1) es un recibo de deuda
    		
    			log.debug("2 strNroCodBarra " + strNroCodBarra);
    		
    		strNroCodBarra += "000"; // 000(3)
    		
    			log.debug("3 strNroCodBarra " + strNroCodBarra);
    		
    		strNroCodBarra += strTotalPagar;  // importe(10)
    		
    			log.debug("4 strNroCodBarra " + strNroCodBarra);
    		
    		strNroCodBarra += StringUtil.completarCerosIzq(servicioBanco.getCodServicioBanco().trim(), 2); // 81 para TGI
    		
    			log.debug("5 strNroCodBarra " + strNroCodBarra);
    		
    		if(!esVolantePagoIntRS){
    			strNroCodBarra += strTotalPagar; // importe(10) se repite porque no se cobra recibo con ID    			
    		}else{
    			strNroCodBarra += "0000000000"; // Se deja en cero el 2do importe para permitir que se cobre vencida la voleta de pago de intereses RS
    		}
    		
    			log.debug("6 strNroCodBarra " + strNroCodBarra);
    		
    		strNroCodBarra += strFecVto; // fecVenc(6)
    		
    			log.debug("7 strNroCodBarra " + strNroCodBarra);

    		if(!esVolantePagoIntRS){
        		strNroCodBarra += strFecVto; // fecVenc(6) se repite porque no se cobra recibo con ID    			
    		}else{
    			strNroCodBarra += "000000";	// Se deja en cero el 2do vencimiento para permitir que se cobre vencida la voleta de pago de intereses RS
    		}

        }
        
        log.debug("strNroCodBarra antes de digito verificador: " + strNroCodBarra);
        
		return  StringUtil.agregarDigitoVerificadorCodBar(strNroCodBarra);
	}
    

}
