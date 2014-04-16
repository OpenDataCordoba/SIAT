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

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.bean.Canal;
import ar.gov.rosario.siat.bal.buss.bean.Sellado;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.Banco;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.LiqCodRefPagSearchPage;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean correspondiente a ReciboConvenio
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_reciboConvenio")
public class ReciboConvenio extends BaseBO {

	private static final long serialVersionUID = 1L;
	public static final Integer MAX_CANT_CUOTA_X_RECIBO_RECONFECCION = 23;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idConvenio") 
	private Convenio convenio;
	
	@Column(name = "codRefPag")
	private Long codRefPag;
	
	@Column(name = "nroRecibo")
	private Long nroRecibo;
	
	@Column(name = "anioRecibo")
	private Integer anioRecibo;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idServicioBanco") 
	private ServicioBanco servicioBanco;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idCanal") 
	private Canal canal;

	@Column(name = "fechaGeneracion")
	private Date fechaGeneracion;
	
	@Column(name = "esCuotaSaldo")
	private Integer esCuotaSaldo;
	
	@Column(name = "usuarioCuotaSaldo")
	private String usuarioCuotaSaldo;
	
	@Column(name = "fechaVencimiento")
	private Date fechaVencimiento;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idDesGen") 
	private DesGen desGen;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idDesEsp") 
	private DesEsp desEsp;

	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idSellado") 
	private Sellado sellado;

	@Column(name = "importeSellado")
	private Double importeSellado;
	
	@Column(name = "totImporteRecibo")
	private Double totImporteRecibo;
	
	@Column(name = "estaImpreso")
	private Integer estaImpreso;
	
	@Column(name = "fechaPago")
	private Date fechaPago;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idBancoPago") 
	private Banco bancoPago;

	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idProcurador") 
	private Procurador procurador;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY) 
	@JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;

	@OneToMany()
	@JoinColumn(name="idReciboConvenio")
	private List<RecConCuo> listRecConCuo = new ArrayList<RecConCuo>();
	
	@Transient
	private Boolean esReimpresionCuota = false; // que sea reimpresionDeuda implica que se usa para la reconfeccion, pero no se graba para nada.
	                                            // se utiliza para reimprimir cuota no vencida sin generar ningun recibo
	@Transient
	private boolean pagaVencido = true;// Esta variable se setea en el metodo getNroCodigoBarra() 
	
	// Constructores
	public ReciboConvenio(){
		super();
	}

	// Getters Y Setters
	
	public Integer getAnioRecibo() {
		return anioRecibo;
	}
	public void setAnioRecibo(Integer anioRecibo) {
		this.anioRecibo = anioRecibo;
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
	public Convenio getConvenio() {
		return convenio;
	}
	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
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
	public Integer getEsCuotaSaldo() {
		return esCuotaSaldo;
	}
	public void setEsCuotaSaldo(Integer esCuotaSaldo) {
		this.esCuotaSaldo = esCuotaSaldo;
	}
	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}
	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
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
	public Double getImporteSellado() {
		return importeSellado;
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
	public Procurador getProcurador() {
		return procurador;
	}
	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
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
	public Double getTotImporteRecibo() {
		return totImporteRecibo;
	}
	public void setTotImporteRecibo(Double totImporteRecibo) {
		this.totImporteRecibo = totImporteRecibo;
	}
	public String getUsuarioCuotaSaldo() {
		return usuarioCuotaSaldo;
	}
	public void setUsuarioCuotaSaldo(String usuarioCuotaSaldo) {
		this.usuarioCuotaSaldo = usuarioCuotaSaldo;
	}
	public List<RecConCuo> getListRecConCuo() {
		return listRecConCuo;
	}
	public void setListRecConCuo(List<RecConCuo> listRecConCuo) {
		this.listRecConCuo = listRecConCuo;
	}

	public Boolean getEsReimpresionCuota() {
		return esReimpresionCuota;
	}

	public void setEsReimpresionCuota(Boolean esReimpresionCuota) {
		this.esReimpresionCuota = esReimpresionCuota;
	}

	public Integer getEstaImpreso() {
		return estaImpreso;
	}

	public void setEstaImpreso(Integer estaImpreso) {
		this.estaImpreso = estaImpreso;
	}

	// Metodos de Clase
	public static ReciboConvenio getById(Long id) {
		return (ReciboConvenio) GdeDAOFactory.getReciboConvenioDAO().getById(id);
	}
	
	public static ReciboConvenio getByIdNull(Long id) {
		return (ReciboConvenio) GdeDAOFactory.getReciboConvenioDAO().getByIdNull(id);
	}
	
	public static List<ReciboConvenio> getList() {
		return (ArrayList<ReciboConvenio>) GdeDAOFactory.getReciboConvenioDAO().getList();
	}
	
	public static List<ReciboConvenio> getListActivos() {			
		return (ArrayList<ReciboConvenio>) GdeDAOFactory.getReciboConvenioDAO().getListActiva();
	}
	
	public static ReciboConvenio getByCodRefPag(Long codRefPag) throws Exception{
		return (ReciboConvenio) GdeDAOFactory.getReciboConvenioDAO().getByCodRefPag(codRefPag);
	}
	
	public static List<ReciboConvenio> getByCodRefPagImporte(LiqCodRefPagSearchPage liqCodRefPagSearchPage) throws Exception {
		return (List<ReciboConvenio>) GdeDAOFactory.getReciboConvenioDAO().getByCodRefPagImporte(liqCodRefPagSearchPage);
	}
	
	/**
	 *	Se tiene que reemplazar por el getByNumero() 
	 */
	@Deprecated
	public static ReciboConvenio getByNroAnioSis(Long numero, Long anio, Long nroSistema) throws Exception{
		return (ReciboConvenio) GdeDAOFactory.getReciboConvenioDAO().getByNroAnioSis(numero, anio, nroSistema);
	}
	
	public static ReciboConvenio getByNumero(Long numero) throws Exception{
		return (ReciboConvenio) GdeDAOFactory.getReciboConvenioDAO().getByNumero(numero);
	}
	
	public static ReciboConvenio getByNroYAnio(Long numero, Long anio) throws Exception{
		return (ReciboConvenio) GdeDAOFactory.getReciboConvenioDAO().getByNroYAnio(numero, anio);
	}
	
	public static ReciboConvenio getByNumeroYSerBan(Long numero, Long idSerBan) throws Exception{
		return (ReciboConvenio) GdeDAOFactory.getReciboConvenioDAO().getByNumeroYSerBan(numero, idSerBan);
	}
	
	public static ReciboConvenio getByNroYAnioYSerBan(Long numero, Long anio, Long idSerBan) throws Exception{
		return (ReciboConvenio) GdeDAOFactory.getReciboConvenioDAO().getByNroYAnioYSerBan(numero, anio, idSerBan);
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
			
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        

		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		
		return !hasError();
	}
	public void setDescuento(Object descuento){
		if(descuento instanceof DesGen)
			setDesGen((DesGen) descuento);
		else
			setDesEsp((DesEsp)descuento);
	}
	
	public void recalcularValores(){
		double totCapitalOriginalNuevo = 0D;
		double totActualizacionNuevo = 0D;
		double totIntFin=0D;
		double importeSelladoCuota=0D;
		for(RecConCuo reciboConCuo: listRecConCuo){
			totActualizacionNuevo += reciboConCuo.getTotActualizacion();
			totCapitalOriginalNuevo += reciboConCuo.getTotCapitalOriginal();
			totIntFin += reciboConCuo.getTotIntFin();
			ConvenioCuota convenioCuota = reciboConCuo.getConvenioCuota();
			if(convenioCuota.getNumeroCuota()==1 && convenioCuota.getSellado()!=null){
				importeSelladoCuota = convenioCuota.getImporteSellado();
			}
		}

		double importeSellado = (getSellado()!=null?getSellado().getImporteSellado():0);
		setImporteSellado(NumberUtil.truncate(importeSellado, SiatParam.DEC_IMPORTE_DB));
		setTotImporteRecibo(NumberUtil.truncate(totCapitalOriginalNuevo + totActualizacionNuevo + totIntFin +importeSelladoCuota+ importeSellado,SiatParam.DEC_IMPORTE_DB));
	}
	
	public List<ReciboConvenio> cortar(int cantMaximaRegistros){
		int cantReciboDeuda = listRecConCuo.size();
		List<ReciboConvenio> listReciboCortado = new ArrayList<ReciboConvenio>();
		List<RecConCuo> listReciboDeudaSacar = new ArrayList<RecConCuo>();
		listReciboCortado.add(this);//agrega el recibo actual a la lista a devolver
		ReciboConvenio reciboActual = null;
		for(int i=0;i<cantReciboDeuda;i++){//recorre todos los reciboDeuda que contiene
			if(i>=cantMaximaRegistros){// los primeros los deja en este recibo
				if(i % cantMaximaRegistros == 0){//a partir de este registro de reciboDeuda, hay que crear otro recibo
					reciboActual = convenio.crearReciboConvenio(fechaVencimiento, getCanal(), this.getProcurador());
					reciboActual.setSellado(null);//se le aplica el sellado s�lo al primer recibo
					listReciboCortado.add(reciboActual);//lo agrega a la lista que va a devolver	
				}
				RecConCuo recConCuoNuevo = listRecConCuo.get(i);
				recConCuoNuevo.setReciboConvenio(reciboActual);
				reciboActual.getListRecConCuo().add(recConCuoNuevo);//agrega el registro actual al recibo que se est� llenando
				reciboActual.recalcularValores();
				listReciboDeudaSacar.add(listRecConCuo.get(i));//agrega el registro a la lista que hay que sacar al finalizar la iteracion, del recibo original
			}
		}
		listRecConCuo.removeAll(listReciboDeudaSacar);//Elimina del recibo, los registros que fueron puestos en otros recibos
		recalcularValores();
		return listReciboCortado;
	}
	
	public String getNroCodigoBarra(){
		
		String strTotalPagar = "";
		String strFecVto = "";
		String strNroCodBarra = "";

		// total a pagar
		strTotalPagar = StringUtil.redondearDecimales(totImporteRecibo, 8, 2);
		strTotalPagar = strTotalPagar.replace(",", "");
		
		// fecha de vencimiento
		strFecVto = DateUtil.formatDate(fechaVencimiento, DateUtil.yyMMdd_MASK);
		pagaVencido = false;// cuando la segunda fecVto y el segundo importe son != 0, el recibo es valido solo hasta la fecha de Vto.
		
		// por ahora vamos a reimprimir/reconfeccionar deuda de TGI
        if (this.getEsReimpresionCuota() ) {

        	// impresion de la deuda tal cual esta
        	if (this.getCodRefPag() == 0L) {
        		// es cuota vieja generada por Cobol
	        	strNroCodBarra += StringUtil.completarCerosIzq(this.getConvenio().getNroConvenio().toString().trim(),9 ); // nro-cuenta (9)
	        	strNroCodBarra += StringUtil.completarCerosIzq(this.getListRecConCuo().get(0).getConvenioCuota().getNumeroCuota().toString().trim(), 4); //nroCuota (4)
	        	strNroCodBarra += strTotalPagar; // monto a pagar (10)
	        	strNroCodBarra += StringUtil.completarCerosIzq(this.getConvenio().getSistema().getNroSistema().toString().trim(), 2); // sistema (2)
	        	strNroCodBarra += strTotalPagar; // ceros (10)
	        	strNroCodBarra += strFecVto; // fecha vencimiento (6)
	        	strNroCodBarra += strFecVto; // fecha vencimiento (6)
	        	
        	} else {
        		// es una reimresion de deuda generada por SIAT.
        		// siempre tiene servicioBanco. 
        		// hay alguons recursos que no cobran con ID (CdM) en ese caso, habria que mandar los segundos importes 
        		// y vencimientos en cero para que el banco se de cuenta y los cobre.
        		strNroCodBarra += StringUtil.completarCerosIzq(String.valueOf(codRefPag), 9); // codRefPago(9)
        		strNroCodBarra += "3"; // 3(1) es una cuota
        		strNroCodBarra += "000"; // 000(3)
        		strNroCodBarra += strTotalPagar;  // importe(10)
        		strNroCodBarra += StringUtil.completarCerosIzq(this.getConvenio().getSistema().getSistemaEsServicioBanco().getNroSistema().toString().trim(), 2); // 81 para TGI
        		strNroCodBarra += strTotalPagar;  // importe(10)
        		strNroCodBarra += strFecVto; // fecVenc(6)
        		strNroCodBarra += strFecVto; // fecVenc(6)

        		
        	}
        	
        } else {
        	// impresion de un recibo
        	// el codigo de barras es el mismo para todos los casos.
    		strNroCodBarra += StringUtil.completarCerosIzq(String.valueOf(codRefPag), 9); // codRefPago(9)
    		strNroCodBarra += "4"; // 4(1) es un recibo de convenio
    		strNroCodBarra += "000"; // 000(3)
    		strNroCodBarra += strTotalPagar;  // importe(10)
    		if (this.getCodRefPag() == 0L){
    			strNroCodBarra += StringUtil.completarCerosIzq(this.getConvenio().getSistema().getNroSistema().toString().trim(), 2); // 81 para TGI
    		}else{
    			strNroCodBarra += StringUtil.completarCerosIzq(this.getConvenio().getSistema().getSistemaEsServicioBanco().getNroSistema().toString().trim(), 2); // 81 para TGI
    		}
    		strNroCodBarra += strTotalPagar; // importe(10) se repite porque no se cobra recibo con ID
    		strNroCodBarra += strFecVto; // fecVenc(6)
    		strNroCodBarra += strFecVto; // fecVenc(6) se repite porque no se cobra recibo con ID    		
        }
		return StringUtil.agregarDigitoVerificadorCodBar(strNroCodBarra);
	}
	/**
	 * Devuelve el codigo de barras entre los delimitadores "<" y ">" 
	 * @return
	 */
	public String getNroCodigoBarraDelim(){
		return "<" + getNroCodigoBarra() + ">";
	}

	public boolean getPagaVencido() {
		return pagaVencido;
	}

	public void setPagaVencido(boolean pagaVencido) {
		this.pagaVencido = pagaVencido;
	}
	
	
}

