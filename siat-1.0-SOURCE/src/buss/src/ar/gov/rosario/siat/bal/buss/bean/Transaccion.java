//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.gov.rosario.siat.bal.buss.cache.AsentamientoCache;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.cas.buss.bean.TipoSolicitud;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioDeuda;
import ar.gov.rosario.siat.gde.buss.bean.DesGen;
import ar.gov.rosario.siat.gde.buss.bean.DeuAdmRecCon;
import ar.gov.rosario.siat.gde.buss.bean.DeuRecCon;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAct;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConCuo;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConvenio;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaManager;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.PlanExe;
import ar.gov.rosario.siat.gde.buss.bean.RecConCuo;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboConvenio;
import ar.gov.rosario.siat.gde.buss.bean.ReciboDeuda;
import ar.gov.rosario.siat.gde.buss.bean.SerBanDesGen;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Bean correspondiente a Transaccion
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_transaccion")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "codServicioBanco", discriminatorType=DiscriminatorType.STRING)
public class Transaccion extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final Long TIPO_BOLETA_DEUDA = 1L;
	public static final Long TIPO_BOLETA_RECIBO_DEUDA = 2L;
	public static final Long TIPO_BOLETA_CUOTA = 3L;
	public static final Long TIPO_BOLETA_RECIBO_CUOTA = 4L;
	public static final Long TIPO_BOLETA_SELLADO = 5L;
	
	public static final Integer TIPO_CANCELACION_PARCIAL = 1;
	public static final Integer TIPO_CANCELACION_POR_MENOS = 2;
	
	public static final Integer ORIGEN_OSIRIS = 1;
	
	@Column(name="sistema") 
	private Long sistema;

	@Column(name = "nroComprobante")
	private Long nroComprobante;
	
	@Column(name = "anioComprobante")
	private Long anioComprobante;
	
	@Column(name = "periodo")
	private Long periodo;
	
	@Column(name = "resto")
	private Long resto;
	
	@Column(name = "codPago")
	private Long codPago;
	
	@Column(name = "caja")
	private Long caja;
	
	@Column(name = "codTr")
	private Long codTr;
	
	@Column(name = "fechaPago")
	private Date fechaPago;
	
	@Column(name = "importe")
	private Double importe;
	
	@Column(name = "recargo")
	private Double recargo;
	
	@Column(name = "filler")
	private String filler;
	
	@Column(name = "paquete")
	private Long paquete;

	@Column(name = "marcaTr")
	private Long marcaTr;
	
	@Column(name = "reciboTr")
	private Long reciboTr;
	
	@Column(name = "fechaBalance")
	private Date fechaBalance;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;
	
	@Column(name = "estaAsentada")
	private Integer estaAsentada;
	
	@Column(name = "esIndet")
	private Integer esIndet;
	
	@Column(name = "conError")
	private Integer conError;

	@Column(name = "codServicioBanco", insertable=false, updatable=false)
	private String codServicioBanco;
	
	@Column(name = "nroLinea")
	private Long nroLinea;
	
	@Column(name = "tipoBoleta")
	private Long tipoBoleta;

	@Column(name = "idPlan")
	private Long idPlan;

	@Column(name = "esRectificativa")
	private Integer esRectificativa;

	@Column(name = "origen")
	private Integer origen;

	@Column(name = "delegada")
	private Integer delegada;

	@Column(name = "tipoCancelacion")
	private Integer tipoCancelacion;
	
	@Column(name="formulario")
	private Integer formulario;
	
	@Column(name="idTranAfip")
	private Long idTranAfip;

	@Transient
	private Integer esDesgloce = 0;
	
	@Transient
	private Double importeParaValidacion = 0D;
	
	@Transient
	private Partida partidaSaldosAFavor = null;
	
	//Constructores 
	public Transaccion(){
		super();
	}

	// Getters y Setters
	
	public Long getAnioComprobante() {
		return anioComprobante;
	}
	public void setAnioComprobante(Long anioComprobante) {
		this.anioComprobante = anioComprobante;
	}
	public Asentamiento getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(Asentamiento asentamiento) {
		this.asentamiento = asentamiento;
	}
	public Long getCaja() {
		return caja;
	}
	public void setCaja(Long caja) {
		this.caja = caja;
	}
	public Long getCodPago() {
		return codPago;
	}
	public void setCodPago(Long codPago) {
		this.codPago = codPago;
	}
	public Long getCodTr() {
		return codTr;
	}
	public void setCodTr(Long codTr) {
		this.codTr = codTr;
	}
	public Integer getEsDesgloce() {
		return esDesgloce;
	}
	public void setEsDesgloce(Integer esDesgloce) {
		this.esDesgloce = esDesgloce;
	}
	public Date getFechaBalance() {
		return fechaBalance;
	}
	public void setFechaBalance(Date fechaBalance) {
		this.fechaBalance = fechaBalance;
	}
	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}
	public String getFiller() {
		return filler;
	}
	public void setFiller(String filler) {
		this.filler = filler;
	}
	public Double getImporte() {
		return importe;
	}
	public void setImporte(Double importe) {
		this.importe = importe;
	}
	public Long getMarcaTr() {
		return marcaTr;
	}
	public void setMarcaTr(Long marcaTr) {
		this.marcaTr = marcaTr;
	}
	public Long getNroComprobante() {
		return nroComprobante;
	}
	public void setNroComprobante(Long nroComprobante) {
		this.nroComprobante = nroComprobante;
	}
	public Long getPaquete() {
		return paquete;
	}
	public void setPaquete(Long paquete) {
		this.paquete = paquete;
	}
	public Long getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
	}
	public Double getRecargo() {
		return recargo;
	}
	public void setRecargo(Double recargo) {
		this.recargo = recargo;
	}
	public Long getReciboTr() {
		return reciboTr;
	}
	public void setReciboTr(Long reciboTr) {
		this.reciboTr = reciboTr;
	}
	public Long getResto() {
		return resto;
	}
	public void setResto(Long resto) {
		this.resto = resto;
	}
	public Long getSistema() {
		return sistema;
	}
	public void setSistema(Long sistema) {
		this.sistema = sistema;
	}	
	public Integer getEsIndet() {
		return esIndet;
	}
	public void setEsIndet(Integer esIndet) {
		this.esIndet = esIndet;
	}
	public String getCodServicioBanco() {
		return codServicioBanco;
	}
	public void setCodServicioBanco(String codServicioBanco) {
		this.codServicioBanco = codServicioBanco;
	}
	public Long getNroLinea() {
		return nroLinea;
	}
	public void setNroLinea(Long nroLinea) {
		this.nroLinea = nroLinea;
	}
	public Double getImporteParaValidacion() {
		return importeParaValidacion;
	}
	public void setImporteParaValidacion(Double importeParaValidacion) {
		this.importeParaValidacion = importeParaValidacion;
	}	
	public Integer getEstaAsentada() {
		return estaAsentada;
	}
	public void setEstaAsentada(Integer estaAsentada) {
		this.estaAsentada = estaAsentada;
	}
	public Long getTipoBoleta() {
		return tipoBoleta;
	}
	public void setTipoBoleta(Long tipoBoleta) {
		this.tipoBoleta = tipoBoleta;
	}
	public Long getIdPlan() {
		return idPlan;
	}
	public void setIdPlan(Long idPlan) {
		this.idPlan = idPlan;
	}
	public Integer getConError() {
		return conError;
	}
	public void setConError(Integer conError) {
		this.conError = conError;
	}
	public Integer getEsRectificativa() {
		return esRectificativa;
	}
	public void setEsRectificativa(Integer esRectificativa) {
		this.esRectificativa = esRectificativa;
	}
	public Integer getOrigen() {
		return origen;
	}
	public void setOrigen(Integer origen) {
		this.origen = origen;
	}	
	public Partida getPartidaSaldosAFavor() {
		return partidaSaldosAFavor;
	}
	public void setPartidaSaldosAFavor(Partida partidaSaldosAFavor) {
		this.partidaSaldosAFavor = partidaSaldosAFavor;
	}
	public Integer getDelegada() {
		return delegada;
	}
	public void setDelegada(Integer delegada) {
		this.delegada = delegada;
	}
	public Integer getTipoCancelacion() {
		return tipoCancelacion;
	}
	public void setTipoCancelacion(Integer tipoCancelacion) {
		this.tipoCancelacion = tipoCancelacion;
	}
	public Integer getFormulario() {
		return formulario;
	}
	public void setFormulario(Integer formulario) {
		this.formulario = formulario;
	}
	public Long getIdTranAfip() {
		return idTranAfip;
	}
	public void setIdTranAfip(Long idTranAfip) {
		this.idTranAfip = idTranAfip;
	}

	// Metodos de clase	
	public static Transaccion getById(Long id) {
		return (Transaccion) BalDAOFactory.getTransaccionDAO().getById(id);
	}
	
	public static Transaccion getByIdNull(Long id) {
		return (Transaccion) BalDAOFactory.getTransaccionDAO().getByIdNull(id);
	}
	
	public static List<Transaccion> getList() {
		return (ArrayList<Transaccion>) BalDAOFactory.getTransaccionDAO().getList();
	}
	
	public static List<Transaccion> getListActivos() {			
		return (ArrayList<Transaccion>) BalDAOFactory.getTransaccionDAO().getListActiva();
	}
	
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		//Validaciones de Negocio
				
		if (hasError()) {
			return false;
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
		
		if (hasError()) {
			return false;
		}

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

	// Metodos Relacionados con el Proceso de Asentamiento
		
	/**
	 * Procesar Deuda.
	 * 	
	 * <i>(paso 2.1.a)</i>
	 */
	public void procesarDeuda(Deuda deuda) throws Exception{
		this.getAsentamiento().logDetallado("Entrando a Procesar Deuda...");
		// Obtener Distribuidor de Partidas Vigentes asociadas al Recurso.		
		Recurso recurso = AsentamientoCache.getInstance().getRecursoById(deuda.getRecurso().getId());

		// Validar si la transaccion ya fue procesada en el mismo Asentamiento.
		if(this.getAsentamiento().existAuxPagDeu(deuda.getId())){
			this.registrarIndeterminado("Indeterminado por Deuda Cancelada: Pago Dúplice en el mismo Asentamiento.", deuda, null, "10");
			return;																		
		}

		// Validar que Fecha de Balance > Fecha Pago, si no registrar indeterminado.
		if(DateUtil.isDateAfter(this.getFechaPago(), this.getFechaBalance())){
			this.registrarIndeterminado("Indeterminado por Fecha de Pago mayor a Fecha de Balance.", deuda, null, "11");
			return;																					
		}
		
		// Validar Estado Deuda
		if(deuda.getRecurso().getEsAutoliquidable().intValue() == SiNo.NO.getId().intValue()){
			if(deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_CANCELADA){
				this.registrarIndeterminado("Indeterminado por Deuda Cancelada: Pago Dúplice.", deuda, null, "12");
				return;																								
			}			
		}else{
			if(deuda.getFechaPago() != null){
				this.registrarIndeterminado("Indeterminado por Deuda Cancelada: Pago Dúplice.", deuda, null, "12");
				return;																								
			}
		}
	
		if(deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_ANULADA){
			this.registrarIndeterminado("Indeterminado por Deuda Anulada.", deuda, null, "13");
			return;																								
		}
		
		// Validar si la Deuda se encuentra en un Convenio
		Convenio convenio = deuda.getConvenio(); 
		if(convenio != null){
			if(convenio.getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_VIGENTE){
				this.registrarIndeterminado("Indeterminado por Deuda en Convenio Vigente Número "+convenio.getNroConvenio()+".", deuda, null, "14");
				return;																												
			}
		}
		
		double capitalADistribuir = 0;
		double actualizacionADistribuir = 0;
		double saldoAFavorADistribuir = 0;
		boolean generaSaldoAFavor = false;
		Double importeCalculado = 0D;
		DeudaAct deudaAct = null;
		Recibo recibo = null;
		
		// NOTA: Antes se recibian transacciones de deuda provenientes de degloses de recibos. Ahora solo pueden venir los 
		// reingresos de Indeterminados provenientes de desgloce para su indeterminacion.
		
		// Caso Normal: Deuda que no proviene de un desglose
		if(this.getReciboTr()==null || this.getReciboTr().longValue()==0){
			
			//Validar si la Via de la Deuda es Administrativa
			if(deuda.getViaDeuda().getId().longValue() != ViaDeuda.ID_VIA_ADMIN){
				this.registrarIndeterminado("Indeterminado por Deuda en Vía distinta de Administrativa.", deuda, null, "16");
				return;
			}
			
			// Obtengo la fecha de vencimiento
			Date fechaVencimiento = deuda.getFechaVencimiento();
			boolean verificarVencimiento = false;
			if(DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
				fechaVencimiento = recurso.obtenerFechaVencimientoDeudaORecibo(deuda.getFechaVencimiento(), AsentamientoCache.getInstance().getMapFeriado());
				verificarVencimiento = true;
			}
			
			importeCalculado = deuda.getSaldo(); 
						
			//Si la Fecha de Pago es posterior a la Fecha de Vencimiento recalculo el importe
			if(verificarVencimiento && DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
				
				long queryTime = System.currentTimeMillis();
				List<CueExe> listCueExeNoActDeu = AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId())
															.getCueExeCache().getListCueExe(deuda.getCuenta().getId());
				if(listCueExeNoActDeu == null)
					listCueExeNoActDeu = new ArrayList<CueExe>();
				queryTime = System.currentTimeMillis() - queryTime;
				AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).addStats("Al procesar deuda, 'prepararActualizacionSaldo'", queryTime);
				//this.getAsentamiento().logStats("<-> Tiempo consumido al preparar 'deuda.actualizacionSaldo' para transaccion nro "+this.getNroLinea()+": "+queryTime+" ms <->");
					
				queryTime = System.currentTimeMillis();
				// cambiar llamada a metodo que recibe las listas.
				deudaAct = deuda.actualizacionSaldo(this.getFechaPago(), listCueExeNoActDeu);
			    queryTime = System.currentTimeMillis() - queryTime;
				AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).addStats("Al procesar deuda, 'deuda.actualizacionSaldo'", queryTime);
				//this.getAsentamiento().logStats("<-> Tiempo consumido al procesar 'deuda.actualizacionSaldo' para transaccion nro "+this.getNroLinea()+": "+queryTime+" ms <->");
				
				// Obtiene el descuento general vigente. Solicita lista. Si viene vacia, OK. Si viene un elemento OK. Si viene m'as de uno ERROR
				List<SerBanDesGen> listSerBanDesGen = AsentamientoCache.getInstance().getSerBanDesGen(this.getAsentamiento().getServicioBanco().getId(), this.getFechaPago() );
				
				if (listSerBanDesGen.size()==0) {
					// OK, no hay descuento
					
				} else if (listSerBanDesGen.size()==1) {
					// OK, hay descuento y lo aplicamos
					DesGen desGen = listSerBanDesGen.get(0).getDesGen();
					deudaAct.aplicarDescuento(desGen.getPorDes());
					
				} else {
					// ERROR, mal cargado el mantenedoir
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Existen mas de dos descuentos generales cargados. Fecha: "+this.getFechaPago()+", Deuda: "+deuda.getCuenta().getNumeroCuenta()+" "+deuda.getPeriodo()+"/"+deuda.getAnio()+" Fecha de Vencimiento: "+deuda.getFechaVencimiento());
					return;
					
				}

				// si la actualizacion queda menor que cero, hay error
				if(deudaAct.getImporteAct()<0){
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontró el indice de actualizacion. Fecha de Pago: "+this.getFechaPago()+", Deuda: "+deuda.getCuenta().getNumeroCuenta()+" "+deuda.getPeriodo()+"/"+deuda.getAnio()+" Fecha de Vencimiento: "+deuda.getFechaVencimiento());
					return;
				}
				importeCalculado = deudaAct.getImporteAct();
				this.getAsentamiento().logDetallado("	Importe de Deuda Actualizado por Fecha de Pago: "+this.getFechaPago()+" posterior a Fecha de Vencimiento: "+fechaVencimiento+". Importe Calculado: "+importeCalculado);
			}
			
			// obtiene la tolerancia para el servicio banco
			Tolerancia tolerancia = AsentamientoCache.getInstance().getToleranciaById(this.getAsentamiento().getServicioBanco().getId());
			
			
			// Se verifica el tipo de cancelacion. Si corresponde a un pago normal se continua con las validaciones normales.
			if(this.getTipoCancelacion() != null && this.getTipoCancelacion().intValue() == Transaccion.TIPO_CANCELACION_PARCIAL.intValue()){
				// Caso de pago parcial de Deuda
				
				// En este caso no se realizan las validaciones normales por importe pago, salvo que el importe pago sea mayor al importeActualizado de la deuda. 
				// Si no lo es se indetermina por no corresponder a un pago parcial.
				if(this.getImporte().doubleValue()>importeCalculado.doubleValue()){
					this.registrarIndeterminado("Indeterminado por pago de más en la Deuda.", deuda, deudaAct, "18");
					return;
				}
				generaSaldoAFavor = false;
				// El capital a distribuir se calcula proporcionalmente al capital total a pagar sobre el importecalculado
				capitalADistribuir = (deuda.getSaldo()/importeCalculado)*this.getImporte();
				// El resto del importe pago que no corresponde a capital se distribuye como actualizacion
				actualizacionADistribuir = this.getImporte() - capitalADistribuir;
			
			}else if(this.getTipoCancelacion() != null && this.getTipoCancelacion().intValue() == Transaccion.TIPO_CANCELACION_POR_MENOS.intValue()){
				// Caso de cancelacion de deuda por menor importe
				
				// En este caso no se realizan las validaciones normales por importe pago, salvo que el importe pago sea mayor al importeActualizado de la deuda. 
				// Si no lo es se indetermina por no corresponder a un pago por menos.
				if(this.getImporte().doubleValue()>importeCalculado.doubleValue()){
					this.registrarIndeterminado("Indeterminado por pago de más en la Deuda.", deuda, deudaAct, "18");
					return;
				}

				generaSaldoAFavor = false;
				
				// El capital a distribuir se calcula proporcionalmente a capital total a pagar sobre el importecalculado
				capitalADistribuir = (deuda.getSaldo()/importeCalculado)*this.getImporte();
				// El resto del importe pago que no corresponde a capital se distribuye como actualizacion
				actualizacionADistribuir = this.getImporte() - capitalADistribuir;
								
			}else{
				// Caso normal de pago de deuda:
				
				// Si el Importe Pagado es inferior al Importe Calculado, registrar indeterminado.
				if(this.getImporte().doubleValue()<importeCalculado.doubleValue()-Tolerancia.VALOR_FIJO){
					this.registrarIndeterminado("Indeterminado por pago de menos en la Deuda.", deuda, deudaAct, "17");
					return;
				}
				
				// Si el Importe Pagado es hasta un toleranciaSaldo% mayor al Importe Calculado, Pago Bueno
				if(this.getImporte().doubleValue()<=importeCalculado.doubleValue()+Tolerancia.VALOR_FIJO.doubleValue()){
					//Pago Bueno, sin saldo a favor
					generaSaldoAFavor = false;
				}else if(this.getImporte().doubleValue()<=importeCalculado*(1+tolerancia.getToleranciaSaldo())){ // Comentario: toleranciaSaldo = 20%
					// Pago Bueno, con saldo a favor
					generaSaldoAFavor = true;
				}else{
					this.registrarIndeterminado("Indeterminado por pago de más en la Deuda.", deuda, deudaAct, "18");
					return;				
				}
				
				// Obtener Capital a Distribuir y Actualizacion a Distribuir.
				// Si no se genero un Saldo a Favor:
				if(!generaSaldoAFavor){
					// El importe pagado es mayor o igual al calculado - 50 centavos, y menor al importe calculado + 50 centavos
					capitalADistribuir = deuda.getSaldo();
					actualizacionADistribuir = this.getImporte() - deuda.getSaldo();
					
					// Si se genero un Saldo a Favor
				}else{
					// El importe pagado es mayor al calculado + 50 centavos, y menor o igual al importe calculado + un 20%
					capitalADistribuir = deuda.getSaldo();
					actualizacionADistribuir = importeCalculado - deuda.getSaldo();
					saldoAFavorADistribuir = this.getImporte() - importeCalculado;
				}				
			}
			
		} else {
			// (Para el caso de un reingreso de Indeterminado proveniente de un recibo) 
			// (En una transaccion que no sea un reingreso reciboTr deberia ser null)
			this.getAsentamiento().logDetallado("	Reingreso de Indeterminado, Deuda proveniente del Recibo Nro:"+this.getReciboTr());
			// Obtiene el Recibo.
			recibo = Recibo.getByNumero(this.getReciboTr());
			if(recibo ==null){
				this.registrarIndeterminado("Reingreso de Indeterminado: No existen el Recibo indicado en la transaccion. Nro Recibo Tr: "+this.getReciboTr(), deuda, null, "44");
				return;
			}
			
			// Valida si la Via de la Deuda coincide con la Via del Recibo
			if(deuda.getViaDeuda().getId().longValue() != recibo.getViaDeuda().getId().longValue()){
				this.registrarIndeterminado("Indeterminado por Deuda en Vía "+deuda.getViaDeuda().getDesViaDeuda()
						+" en un Recibo en Vía "+recibo.getViaDeuda().getDesViaDeuda()+".", deuda, null, "19");
				return;
			}
			
			// Obtiene el el ReciboDeuda (Relacion entre la Deuda y el Recibo).
			ReciboDeuda reciboDeuda = ReciboDeuda.getByReciboYDeuda(recibo, deuda);
			if(reciboDeuda == null){
				this.registrarIndeterminado("Reingreso de Indeterminado: No existen la relación entre Recibo y la Deuda. Nro Recibo: "+this.getReciboTr()+". Deuda: "+deuda.getCuenta().getNumeroCuenta()+" "+deuda.getPeriodo()+"/"+deuda.getAnio(), deuda, null, "45");
				return;
			}
			
			// Valida que el importe de la transaccion sea igual al del ReciboDeuda
			if(!NumberUtil.isDoubleEqualToDouble(this.getImporte(),reciboDeuda.getTotCapital()+reciboDeuda.getTotActualizacion(),0.01)){
				this.registrarIndeterminado("Reingreso de Indeterminado: Indeterminado por importe de transacción distinto del importe en la relación del Recibo con la Deuda. Importe Transacción: "+this.getImporte()+". Importe ReciboDeuda: "+reciboDeuda.getTotCapital()+reciboDeuda.getTotActualizacion(), deuda, null, "46");
				return;
			}
			
			// Valida que el Capital Original del ReciboDeuda sea igual al saldo de la Deuda
			if(!NumberUtil.isDoubleEqualToDouble(reciboDeuda.getCapitalOriginal(),reciboDeuda.getDeuda().getSaldo(),0.01)){
				this.registrarIndeterminado("Reingreso de Indeterminado: Indeterminado por Saldo de la Deuda distinto del Capital Original en la relación del Recibo con la Deuda. Saldo de la Deuda: "+reciboDeuda.getDeuda().getSaldo()+". Capital Original del ReciboDeuda: "+reciboDeuda.getCapitalOriginal(), deuda, null, "47");
				return;
			}
			
			// Obtener Capital a Distribuir y Actualizacion a Distribuir.
			capitalADistribuir = reciboDeuda.getTotCapital();
			actualizacionADistribuir = reciboDeuda.getTotActualizacion();
		}
		
		DisParRec disParRec=null;
		this.getAsentamiento().logDetallado("	Valor de Atributo de Asentamiento: "+deuda.getAtrAseVal());
		// recupera una lista de DisParRec
		List<DisParRec> listDisParRec =  AsentamientoCache.getInstance().getListByRecursoViaDeudaValAtrAse(recurso, deuda.getViaDeuda(), deuda.getAtrAseVal());
		
		if (listDisParRec.size() == 0) {
			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro Distribuidor de partidas para: idRecurso: " + recurso.getId() + 
					" ViaDeuda: " + deuda.getViaDeuda().getId() + " valorAtributoAsentamiento: " + deuda.getAtrAseVal());
			return;
			
		} else if (listDisParRec.size()>1) {
			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Se encontro mas de un Distribuidor de partidas para: idRecurso: " + recurso.getId() + 
					" ViaDeuda: " + deuda.getViaDeuda().getId() + " valorAtributoAsentamiento: " + deuda.getAtrAseVal());			
			return;
		} else {
			// toma el DisParRec
			disParRec = listDisParRec.get(0);
		}
		
		this.getAsentamiento().logDetallado("	Capital a Distribuir: "+capitalADistribuir+", Actualizacion a Distribuir: "+actualizacionADistribuir);
		this.getAsentamiento().logDetallado("	Realizando Distribucion...");
		// DISTRIBUCION DE PARTIDAS 
		if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){
			// Para cada concepto de la deuda, distribuye Capital y Actualizacion
			this.getAsentamiento().logDetallado("	String con Lista de Conceptos de la Deuda: "+deuda.getStrConceptosProp());
			List<DeuRecCon> listDeuRecCon = ((Deuda) deuda).getListDeuRecConByString();
			// Validar Total Conceptos con Importe, y logear Advertencia.
			if(!this.validaConceptos(listDeuRecCon, deuda.getImporte(), 0.01)){
				AdpRun.currentRun().logDebug("Advertencia!: Transaccion de Id: "+this.getId()+", Linea Nro "+this.getNroLinea()+", Se encontró diferencias entre el importe de la deuda y la suma de sus conceptos. Deuda de id: "+deuda.getId()+" Importe Deuda: "+deuda.getImporte()+" Lista de Conceptos: "+deuda.getStrConceptosProp());
				AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).incWarnings();
			}				
			
			for(DeuRecCon deuRecCon: listDeuRecCon){
				if(deuRecCon.getImporte().doubleValue() != 0){
					// Distribuir Capital
					this.distribuir(disParRec.getDisPar(), deuRecCon, capitalADistribuir, TipoImporte.ID_CAPITAL, deuda);
					if(this.hasError()){
						return;
					}
					// Distribuir Actualizacion
					this.distribuir(disParRec.getDisPar(), deuRecCon, actualizacionADistribuir, TipoImporte.ID_ACTUALIZACION, deuda);
					if(this.hasError()){
						return;
					}					
				}
			}
			// Si se Genero Saldo a Favor
			if(generaSaldoAFavor){
				// Distribuir Saldo a Reintegrar
				this.distribuir(null, null, saldoAFavorADistribuir, TipoImporte.ID_SALDO_A_REINGRESAR, deuda);
				if(this.hasError()){
					return;
				}				
			}
		}
		Double importeDistribuido = capitalADistribuir+actualizacionADistribuir+saldoAFavorADistribuir;
		if(!NumberUtil.isDoubleEqualToDouble(importeDistribuido,this.getImporte(),0.001)){
			AdpRun.currentRun().logDebug("Advertencia!: Transaccion de Id: "+this.getId()+", de Linea Nro "+this.getNroLinea()+" Importe: "+this.getImporte()+" Importe Distribuido: "+importeDistribuido);
			AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).incWarnings();
		}
		// -> Registrar el Pago Bueno
		AuxPagDeu auxPagDeu = new AuxPagDeu();
		auxPagDeu.setAsentamiento(this.getAsentamiento());
		auxPagDeu.setIdDeuda(deuda.getId());
		auxPagDeu.setTransaccion(this);
		if(recibo != null){
			auxPagDeu.setRecibo(recibo);			
		}
		Double actualizacionPagDeu = 0D; 
		// Se verifica el tipo de cancelacion. Si corresponde a un pago normal se continua con las validaciones normales.
		if(this.getTipoCancelacion() != null && this.getTipoCancelacion().intValue() == Transaccion.TIPO_CANCELACION_PARCIAL.intValue()){
			// Caso de pago parcial de Deuda
			actualizacionPagDeu = this.getImporte() - capitalADistribuir;
		}else if(this.getTipoCancelacion() != null && this.getTipoCancelacion().intValue() == Transaccion.TIPO_CANCELACION_POR_MENOS.intValue()){
			// Caso de cancelacion de deuda por menor importe
			actualizacionPagDeu = this.getImporte() - capitalADistribuir; 
		}else{
			// Caso normal de pago de deuda:
			actualizacionPagDeu = this.getImporte() - deuda.getSaldo();
		}
		if(actualizacionPagDeu<0)
			actualizacionPagDeu=0D;
		auxPagDeu.setActualizacion(actualizacionPagDeu);
		
		this.getAsentamiento().createAuxPagDeu(auxPagDeu);
		this.getAsentamiento().addAuxPagDeuToIndex(deuda.getId());
	
		// -> Registrar el Saldo a Favor
		if(generaSaldoAFavor){
			SinSalAFav sinSalAFav = new SinSalAFav();
			sinSalAFav.setSistema(this.getSistema());
			sinSalAFav.setNroComprobante(Long.valueOf(deuda.getCuenta().getNumeroCuenta()));
			sinSalAFav.setAnioComprobante(deuda.getAnio());
			sinSalAFav.setCuota(deuda.getPeriodo());
			sinSalAFav.setFiller_o(0L);
			sinSalAFav.setImportePago(this.getImporte());
			sinSalAFav.setImporteDebPag(importeCalculado);
			sinSalAFav.setFechaPago(this.getFechaPago());
			sinSalAFav.setFechaBalance(this.getFechaBalance());
			sinSalAFav.setTransaccion(this);
			sinSalAFav.setAsentamiento(this.getAsentamiento());

			if(this.getAsentamiento().getBalance() != null){
				sinSalAFav.setPartida(this.getPartidaSaldosAFavor());
				sinSalAFav.setCuenta(deuda.getCuenta());
			}
			
			this.getAsentamiento().createSinSalAFav(sinSalAFav);
		}
		this.getAsentamiento().logDetallado("Saliendo de Procesar Deuda...");
	}
	
	/**
	 * Procesar Recibo Deuda.
	 * 	
	 * <i>(paso 2.1.b)</i>
	 */
	public void procesarReciboDeuda(Recibo recibo) throws Exception{
		this.getAsentamiento().logDetallado("Entrando a Procesar Recibo de Deuda...");
		// Validar que Fecha de Balance > Fecha Pago, si no registrar indeterminado.
		if(DateUtil.isDateAfter(this.getFechaPago(), this.getFechaBalance())){
			this.registrarIndeterminado("Indeterminado por Fecha de Pago mayor a Fecha de Balance.", recibo, "11");
			return;																					
		}
		// Validar si la transaccion ya fue procesada en el mismo Asentamiento.
		if(this.getAsentamiento().existAuxPagRec(recibo.getId())){
			this.registrarIndeterminado("Indeterminado por Recibo de Deuda Cancelada: Pago Dúplice en el mismo Asentamiento.", recibo, "24");
			return;																		
		}
		// Validar si el Recibo se encuentra Pago
		if(recibo.getFechaPago()!=null){
			this.registrarIndeterminado("Indeterminado por Recibo de Deuda Cancelado: Pago Dúplice, Recibo de Deuda "+recibo.getNroRecibo()+"/"+recibo.getAnioRecibo()+".", recibo, "25");
			return;
		}
		
		// Obtengo la fecha de vencimiento
		Recurso recurso = AsentamientoCache.getInstance().getRecursoById(recibo.getRecurso().getId());
		Date fechaVencimiento = recibo.getFechaVencimiento();
		boolean verificarVencimiento = false;
		if(DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
			fechaVencimiento = recurso.obtenerFechaVencimientoDeudaORecibo(recibo.getFechaVencimiento(),AsentamientoCache.getInstance().getMapFeriado());
			verificarVencimiento = true;
		}
		//Si la Fecha de Pago es posterior a la Fecha de Vencimiento registrar indeterminado.
		if(verificarVencimiento && DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
			this.registrarIndeterminado("Indeterminado por Fecha de Pago mayor a Fecha de Vencimiento en Recibo de Deuda "+recibo.getNroRecibo()+"/"+recibo.getAnioRecibo()+".", recibo, "26");
			return;
		}
		
		// Obtener la Deuda asociada al Recibo.
		List<ReciboDeuda> listReciboDeuda = recibo.getListReciboDeuda();
		// Validar si la suma de los importes actualizados de la deuda coincide con el importe del recibo.
		Double sumImporteDetalle = 0D; 
		for(ReciboDeuda reciboDeuda: listReciboDeuda)
			sumImporteDetalle += reciboDeuda.getTotCapital()+reciboDeuda.getTotActualizacion();
		
		Double importeSellado = 0D;
		if(recibo.getImporteSellado() != null)
			importeSellado = recibo.getImporteSellado();
		
		// Si el Importe Pagado es inferior al Importe del Recibo menos una valor fijo de tolerancia, registrar indeterminado.
		if(this.getImporte().doubleValue()<(sumImporteDetalle+importeSellado)-Tolerancia.VALOR_FIJO){//recibo.getTotImporteRecibo().doubleValue()-Tolerancia.VALOR_FIJO){
			this.registrarIndeterminado("Indeterminado por pago de menos en el Recibo de Deuda "+recibo.getNroRecibo()+"/"+recibo.getAnioRecibo()+".", recibo, "27");
			return;
		}
		// Si el Importe Pagado es hasta un valor fijo de tolerancia mayor al Importe del Recibo, Pago Bueno. Si es mayor a este valor, registrar indeterminado.
		if(this.getImporte().doubleValue()>(sumImporteDetalle+importeSellado)+Tolerancia.VALOR_FIJO){//recibo.getTotImporteRecibo().doubleValue()+Tolerancia.VALOR_FIJO){
			this.registrarIndeterminado("Indeterminado por pago de más en el Recibo de Deuda "+recibo.getNroRecibo()+"/"+recibo.getAnioRecibo()+".", recibo, "28");
			return;				
		}

		if(!NumberUtil.isDoubleEqualToDouble(recibo.getTotImporteRecibo(),sumImporteDetalle+importeSellado,0.001)){ 
			AdpRun.currentRun().logDebug("Advertencia!: Transaccion de Id: "+this.getId()+", Linea Nro "+this.getNroLinea()+", El Importe del Recibo de Deuda no coincide con la sumatoria de los importes del detalle considerando el sellado. Importe Recibo: "+recibo.getTotImporteRecibo()+" Importe Sumado del Detalle(con sellado incluido): "+(sumImporteDetalle+importeSellado)+", Importe Pago: "+this.getImporte()+". Recibo de Deuda "+recibo.getNroRecibo()+"/"+recibo.getAnioRecibo());
			AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).incWarnings();
		}
		// Se procesa cada Deuda incluida en el recibo.
		for(ReciboDeuda reciboDeuda: listReciboDeuda){
			this.getAsentamiento().logDetallado("	Deuda Incluida en Recibo. IdDeuda: "+reciboDeuda.getIdDeuda());
			this.procesarDeudaDeRecibo(reciboDeuda);
			if(this.hasError()){
				return;
			}
		}
		// Se guarda un registro en el mapa de indice de Pagos de Recibo
		this.getAsentamiento().addAuxPagRecToIndex(recibo.getId());
		
		//-> Si es un Asentamiento Comun 
		if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO) {
			
			// si tiene sellado, lo distribuye
			if (recibo.getSellado() == null) {
				this.getAsentamiento().logDetallado("	No existe el sellado indicado en el Recibo de Deuda "+recibo.getNroRecibo()+"/"+recibo.getAnioRecibo()+", o no aplica sellado.");
				return;											
				
			} else {
				// toma el idSellado del recibo
				Long idSellado = recibo.getSellado().getId();
				
				List<ParSel> listParSel = AsentamientoCache.getInstance().getListParSel(idSellado);
			
				// calcula el importe de sellado a distribuir como porcentajes
				Double importeSelladoSinMontoFijo = recibo.getImporteSellado();
				for(ParSel parSel: listParSel){
					if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO){
						importeSelladoSinMontoFijo -= parSel.getMonto();
					}									
				}
				this.getAsentamiento().logDetallado("	Distribuir Sellado... ");
				
				// distribuye
				for(ParSel parSel: listParSel){
					
					AuxSellado auxSellado = this.getAsentamiento().getAuxSellado(this, parSel);
					
					if(auxSellado != null) {
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO){
							auxSellado.setImporteFijo(auxSellado.getImporteFijo()+parSel.getMonto());
							this.getAsentamiento().logDetallado("	Acumula Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Monto Fijo: "+parSel.getMonto()+", Importe Acumulado: "+auxSellado.getImporteFijo());
						}					
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_PORCENTAJE){
							auxSellado.setImporteEjeAct(auxSellado.getImporteEjeAct()+(importeSelladoSinMontoFijo));
							this.getAsentamiento().logDetallado("	Acumula Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Sin Monto Fijo: "+importeSelladoSinMontoFijo+", Importe Acumulado: "+auxSellado.getImporteEjeAct()+", Porcentaje: "+parSel.getMonto());
						}
					
					} else {
						auxSellado = new AuxSellado();
						auxSellado.setAsentamiento(this.getAsentamiento());
						Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), 
				                 this.getSistema());
						auxSellado.setSistema(sistema);
						auxSellado.setSellado(parSel.getSellado());
						auxSellado.setFechaPago(this.getFechaPago());
						auxSellado.setPartida(parSel.getPartida());
						auxSellado.setImporteEjeAct(0D);
						auxSellado.setImporteEjeVen(0D);
						auxSellado.setImporteFijo(0D);
					
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO){
							auxSellado.setEsImporteFijo(SiNo.SI.getId());
							auxSellado.setImporteFijo(parSel.getMonto());
							auxSellado.setPorcentaje(0D);
							this.getAsentamiento().logDetallado("	Crea Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Monto Fijo: "+parSel.getMonto()+", Importe Acumulado: "+auxSellado.getImporteFijo());
						}
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_PORCENTAJE){
							auxSellado.setEsImporteFijo(SiNo.NO.getId());
							auxSellado.setImporteEjeAct((importeSelladoSinMontoFijo));
							auxSellado.setPorcentaje(parSel.getMonto());
							this.getAsentamiento().logDetallado("	Crea Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Sin Monto Fijo: "+importeSelladoSinMontoFijo+", Importe Acumulado: "+auxSellado.getImporteEjeAct()+", Porcentaje: "+parSel.getMonto());
						}
						
						this.getAsentamiento().createAuxSellado(auxSellado);
					}
				}
			}
			
			//Obtiene el Distribuidor para Redondeo
			this.getAsentamiento().logDetallado("	Buscando Distribuidor de Redondeo.");
			
			String atrVal = null;
			if(recurso.getAtributoAse() != null)
				atrVal = recibo.getCuenta().getValorAtributo(recurso.getAtributoAse().getId(), recibo.getFechaVencimiento());
			DisParRec disParRec = null;
			List<DisParRec> listDisParRec = AsentamientoCache.getInstance().getListByRecursoViaDeudaValAtrAse(recurso, recibo.getViaDeuda(), atrVal);
			if (listDisParRec.size()==0 ) {
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro Distribuidor de partidas para: idRecurso: " + recurso.getId() + 
						" ViaDeuda: " + recibo.getViaDeuda().getId() + " valorAtributoAsentamiento: " + atrVal);
				return;
				
			} else if (listDisParRec.size()>1) {
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Se encontro mas de un Distribuidor de partidas para: idRecurso: " + recurso.getId() + 
						" ViaDeuda: " + recibo.getViaDeuda().getId() + " valorAtributoAsentamiento: " + atrVal);
				return;
				
			} else {
				// toma el DisParRec
				disParRec = listDisParRec.get(0);
			}
			DisPar disPar = disParRec.getDisPar();
			
			Double redondeo = this.getImporte().doubleValue() - recibo.getTotImporteRecibo().doubleValue();
			
			List<DisParDet> listDisParDet = disPar.getListDisParDetByidTipoImporteYRecCon(TipoImporte.ID_REDONDEO, null);
			if(ListUtil.isNullOrEmpty(listDisParDet)){
				String message = "";
				message += "Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No existe el Detalle del Distribuidor de Partidas asociado al Recurso "+disPar.getRecurso().getDesRecurso();
				message +=", Distribuidor de id: "+disPar.getId();
				message +="y Tipo de Importe Redondeo.";
				this.addRecoverableValueError(message);
				return;
			}

			for(DisParDet disParDet: listDisParDet){				
				AuxRecaudado auxRecaudado = this.getAsentamiento().getAuxRecaudado(this, disParDet.getPartida(), disParDet.getPorcentaje(), null, null, this.getTipoBoleta());			
				// Si se encontro un registro
				if(auxRecaudado != null){
					// Actualizar el importe acumulando el Importe Pago 
					auxRecaudado.setImporteEjeAct(auxRecaudado.getImporteEjeAct()+redondeo);
					// Si no se encontro un registro
				}else{
					Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), this.getSistema());
					// Insertar registro en AuxRecaudado
					auxRecaudado = new AuxRecaudado();
					auxRecaudado.setAsentamiento(this.getAsentamiento());
					auxRecaudado.setSistema(sistema);
					auxRecaudado.setFechaPago(this.getFechaPago());
					auxRecaudado.setPartida(disParDet.getPartida());
					auxRecaudado.setViaDeuda(null);
					auxRecaudado.setPlan(null);
					auxRecaudado.setTipoBoleta(this.getTipoBoleta());
					auxRecaudado.setImporteEjeAct(redondeo);
					auxRecaudado.setImporteEjeVen(0D);
					auxRecaudado.setPorcentaje(disParDet.getPorcentaje());
					this.getAsentamiento().createAuxRecaudado(auxRecaudado);
					Double paraLog = 0D;
					if(AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).isLogDetalladoEnabled())
						paraLog = auxRecaudado.getImporteEjeAct()+auxRecaudado.getImporteEjeVen();
					this.getAsentamiento().logDetallado("		AuxRecaudado, Partida: "+disParDet.getPartida().getDesPartida()+" Importe Agregado: "+redondeo+" Importe Acumulado: "+paraLog+" Porcentaje: "+auxRecaudado.getPorcentaje()*100+" %");
				}
			
			}
		}
		this.getAsentamiento().logDetallado("Saliendo de Procesar Recibo de Deuda...");
	}

	/**
	 * Procesar Deuda que Proviene de un Recibo (Generado en Siat).
	 * 	
	 * <i>(paso 2.1.a)</i>
	 */
	public void procesarDeudaDeRecibo(ReciboDeuda reciboDeuda) throws Exception{
		this.getAsentamiento().logDetallado("	Entrando a Procesar Deuda de Recibo...");
		Deuda deuda = Deuda.getById(reciboDeuda.getIdDeuda());
		Recibo recibo = reciboDeuda.getRecibo();
		// Obtener Partidas Vigentes asociadas al Recurso.
		Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), 
                 this.getSistema());
		if(sistema == null){
			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro el sistema de la transaccion." );
			return;
		}
		Recurso recurso = AsentamientoCache.getInstance().getRecursoById(deuda.getRecurso().getId());
			
		this.getAsentamiento().logDetallado("		Se arma Transaccion Desglosada para la Deuda del Recibo.");
		// Armamos una transaccion (desgloce) para indeterminar esta deuda del recibo y no el recibo entero. 
		Transaccion transaccionDesgloce = new Transaccion();
		if(deuda.getSistema().getEsServicioBanco().intValue() == SiNo.SI.getId().intValue()){
			transaccionDesgloce.setSistema(deuda.getSistema().getNroSistema());
			transaccionDesgloce.setNroComprobante(Long.valueOf(deuda.getCodRefPag()));
			transaccionDesgloce.setAnioComprobante(Transaccion.TIPO_BOLETA_DEUDA);
			transaccionDesgloce.setPeriodo(0L);
			transaccionDesgloce.setResto(0L);
		}else{
			// Si la transaccion se de Servicio Banco 85 y el sistema no es servicio banco el desgloce se anula y se copian los datos del la transaccion original
			// El importe se deja el del detalle y ademas se graba el nro de recibo, para identificar que proviene de un desgloce.
			if(ServicioBanco.COD_OTROS_TRIBUTOS.equals(this.getAsentamiento().getServicioBanco().getCodServicioBanco())){
				transaccionDesgloce.setSistema(this.getSistema());
				transaccionDesgloce.setNroComprobante(this.getNroComprobante());
				transaccionDesgloce.setAnioComprobante(this.getAnioComprobante());
				transaccionDesgloce.setPeriodo(this.getPeriodo());
				transaccionDesgloce.setResto(this.getResto());			
			}else{
				transaccionDesgloce.setSistema(deuda.getSistema().getNroSistema());
				transaccionDesgloce.setNroComprobante(Long.valueOf(deuda.getCuenta().getNumeroCuenta()));
				transaccionDesgloce.setAnioComprobante(deuda.getAnio());
				transaccionDesgloce.setPeriodo(deuda.getPeriodo());
				transaccionDesgloce.setResto(deuda.getResto());					
			}
		}
		transaccionDesgloce.setCodPago(this.getCodPago());
		transaccionDesgloce.setCaja(this.getCaja());
		transaccionDesgloce.setCodTr(this.getCodTr());
		transaccionDesgloce.setFechaPago(this.getFechaPago());
		transaccionDesgloce.setImporte(reciboDeuda.getTotCapital()+reciboDeuda.getTotActualizacion());			

		transaccionDesgloce.setRecargo(0D);
		transaccionDesgloce.setPaquete(this.getPaquete()); 
		transaccionDesgloce.setMarcaTr(this.getMarcaTr());
		transaccionDesgloce.setReciboTr(reciboDeuda.getRecibo().getNroRecibo());
		transaccionDesgloce.setFechaBalance(this.getFechaBalance());
		transaccionDesgloce.setAsentamiento(this.getAsentamiento());
		transaccionDesgloce.setEsDesgloce(SiNo.SI.getId());
		transaccionDesgloce.setNroLinea(this.getNroLinea());
		transaccionDesgloce.setTipoBoleta(this.getTipoBoleta());

		// Validar Estado Deuda
		if(deuda.getRecurso().getEsAutoliquidable().intValue() == SiNo.NO.getId().intValue()){
			if(deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_CANCELADA){
				transaccionDesgloce.registrarIndeterminado("Indeterminado por Deuda Cancelada: Pago Dúplice.", deuda, null, "12");
				transaccionDesgloce.passErrorMessages(this);
				return;																								
			}
		}else{
			if(deuda.getFechaPago() != null){
				transaccionDesgloce.registrarIndeterminado("Indeterminado por Deuda Cancelada: Pago Dúplice.", deuda, null, "12");
				transaccionDesgloce.passErrorMessages(this);
				return;																								
			}
		}
		
		if(deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_ANULADA){
			transaccionDesgloce.registrarIndeterminado("Indeterminado por Deuda Anulada.", deuda, null, "13");
			transaccionDesgloce.passErrorMessages(this);
			return;																								
		}
		
		// Validar si la deuda ya fue procesada en el mismo Asentamiento.
		if(this.getAsentamiento().existAuxPagDeu(deuda.getId())){
			transaccionDesgloce.registrarIndeterminado("Indeterminado por Deuda Cancelada: Pago Dúplice en el mismo Asentamiento.", deuda, null, "10");
			transaccionDesgloce.passErrorMessages(this);
			return;																		
		}
		
		// Validar si la Deuda se encuentra en un Convenio
		Convenio convenio = deuda.getConvenio(); 
		if(convenio != null){
			if(convenio.getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_VIGENTE){
				transaccionDesgloce.registrarIndeterminado("Indeterminado por Deuda en Convenio Vigente Número "+convenio.getNroConvenio()+".", deuda, null, "14");
				transaccionDesgloce.passErrorMessages(this);
				return;																												
			}
		}
		
		// Validar si la Via de la Deuda coincide con la Via del Recibo
		if(deuda.getViaDeuda().getId().longValue() != recibo.getViaDeuda().getId().longValue()){
			transaccionDesgloce.registrarIndeterminado("Indeterminado por Deuda en Vía "+deuda.getViaDeuda().getDesViaDeuda()
					+" en un Recibo en Vía "+recibo.getViaDeuda().getDesViaDeuda()+".", deuda, null, "19");
			transaccionDesgloce.passErrorMessages(this);
			return;
		}
		
		// Obtener Distribuidor de Partidas Vigentes asociadas al Recurso.				
		DisParRec disParRec=null;
		
		this.getAsentamiento().logDetallado("		Valor de Atributo de Asentamiento de la Deuda: "+deuda.getAtrAseVal());
		// recupera una lista de DisParRec
		List<DisParRec> listDisParRec =  AsentamientoCache.getInstance().getListByRecursoViaDeudaValAtrAse(recurso, deuda.getViaDeuda(), deuda.getAtrAseVal());
		
		if (listDisParRec.size()==0 ) {
			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro Distribuidor de partidas para: idRecurso: " + recurso.getId() + 
					" ViaDeuda: " + deuda.getViaDeuda().getId() + " valorAtributoAsentamiento: " + deuda.getAtrAseVal());
			return;
		} else if (listDisParRec.size()>1) {
			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Se encontro mas de un Distribuidor de partidas para: idRecurso: " + recurso.getId() + 
					" ViaDeuda: " + deuda.getViaDeuda().getId() + " valorAtributoAsentamiento: " + deuda.getAtrAseVal());
			return;
		} else {
			// toma el DisParRec
			disParRec = listDisParRec.get(0);
		}
		
		// Obtener Capital a Distribuir y Actualizacion a Distribuir.
		Double capitalADistribuir = reciboDeuda.getTotCapital();
		Double actualizacionADistribuir = reciboDeuda.getTotActualizacion();
		this.getAsentamiento().logDetallado("		Capital a Distribuir: "+capitalADistribuir+", Actualizacion a Distribuir: "+actualizacionADistribuir);
		this.getAsentamiento().logDetallado("		Realizando Distribucion...");

		//-> Si es un Asentamiento Comun 
		if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){
			// Obtener los Conceptos de la Deuda, y por cada uno Distribuir Capital y Actualizacion.
			List<DeuRecCon> listDeuRecCon =((Deuda) deuda).getListDeuRecConByString();
			this.getAsentamiento().logDetallado("		String con Lista de Conceptos de la Deuda: "+deuda.getStrConceptosProp());
			// Validar Total Conceptos con Importe, y logear Advertencia.
			if(!this.validaConceptos(listDeuRecCon, deuda.getImporte(), 0.01)){
				AdpRun.currentRun().logDebug("Advertencia!: Transaccion de Id: "+this.getId()+", de Linea Nro "+this.getNroLinea()+", Se encontró diferencias entre el importe de la deuda y la suma de sus conceptos. Deuda de id: "+deuda.getId()+" Importe Deuda: "+deuda.getImporte()+" Lista de Conceptos: "+deuda.getStrConceptosProp());
				AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).incWarnings();
			}				

			for(DeuRecCon deuRecCon: listDeuRecCon){
				if(deuRecCon.getImporte().doubleValue() != 0){
					// Distribuir Capital
					this.distribuir(disParRec.getDisPar(), deuRecCon, capitalADistribuir, TipoImporte.ID_CAPITAL, deuda);
					if(this.hasError()){
						return;
					}
					// Distribuir Actualizacion
					this.distribuir(disParRec.getDisPar(), deuRecCon, actualizacionADistribuir, TipoImporte.ID_ACTUALIZACION, deuda);
					if(this.hasError()){
						return;
					}					
				}
			}
		}
		Double importeDistribuido = capitalADistribuir+actualizacionADistribuir;
		if(!NumberUtil.isDoubleEqualToDouble(importeDistribuido,transaccionDesgloce.getImporte(),0.01)){
			AdpRun.currentRun().logDebug("Advertencia!: Transaccion Desglosada a partir de la correspondiente de Id: "+this.getId()+", Linea Nro "+this.getNroLinea()+" Importe: "+transaccionDesgloce.getImporte()+" Importe Distribuido: "+importeDistribuido+" Importe de la Transaccion Original: "+this.getImporte());
			AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).incWarnings();
		}

		// -> Registrar el Pago Bueno
		AuxPagDeu auxPagDeu = new AuxPagDeu();
		auxPagDeu.setAsentamiento(this.getAsentamiento());
		auxPagDeu.setIdDeuda(deuda.getId());
		auxPagDeu.setTransaccion(this);
		if(recibo != null){
			auxPagDeu.setRecibo(recibo);			
		}
		Double actualizacionPagDeu = transaccionDesgloce.getImporte()-deuda.getSaldo(); 
		if(actualizacionPagDeu<0)
			actualizacionPagDeu=0D;
		auxPagDeu.setActualizacion(actualizacionPagDeu);
		this.getAsentamiento().createAuxPagDeu(auxPagDeu);
		this.getAsentamiento().addAuxPagDeuToIndex(deuda.getId());
		this.getAsentamiento().logDetallado("	Saliendo de Procesar Deuda de Recibo...");
	}
	
	/**
	 * Procesar Cuota. 
	 * 	
	 * <i>(paso 2.1.c)</i>
	 */
	public void procesarCuota(ConvenioCuota convenioCuota) throws Exception{
		this.getAsentamiento().logDetallado("	Entrando a Procesar Cuota...");
		long validarTime = System.currentTimeMillis();
		
		// Validar si la transaccion ya fue procesada en el mismo Asentamiento.
		if(this.getAsentamiento().existAuxPagCuo(convenioCuota.getId())){
			this.registrarIndeterminado("Indeterminado por Cuota Cancelada: Pago Dúplice en el mismo Asentamiento.", convenioCuota,null,"31");
			return;																		
		}

		// Validar que Fecha de Balance > Fecha Pago, si no registrar indeterminado.
		if(DateUtil.isDateAfter(this.getFechaPago(), this.getFechaBalance())){
			this.registrarIndeterminado("Indeterminado por Fecha de Pago mayor a Fecha de Balance.", convenioCuota,null, "11");
			return;																					
		}

		// Validar el Estado del Convenio
		if(convenioCuota.getConvenio().getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_CANCELADO){
			this.registrarIndeterminado("Indeterminado por Convenio Cancelado.", convenioCuota,null, "33");
			return;																								
		}
		if(convenioCuota.getConvenio().getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_RECOMPUESTO){
			this.registrarIndeterminado("Indeterminado por Convenio Recompuesto.", convenioCuota,null, "34");
			// Cargar en lista para envio de Solicitud de Revision de Caso a Balance.
			this.getAsentamiento().logDetallado("	Se carga en lista para envio de Solicitud de Revision de Caso a Balance.");
			TipoSolicitud tipoSolicitud = null;
			tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_VERIFICAR_CONVENIO_RECOMPUESTO);
			AuxConvenio auxConvenio = this.getAsentamiento().getAuxConvenio(convenioCuota.getConvenio(), tipoSolicitud);			
			if(auxConvenio == null){
				auxConvenio = new AuxConvenio();
				auxConvenio.setAsentamiento(this.getAsentamiento());
				auxConvenio.setConvenio(convenioCuota.getConvenio());
				auxConvenio.setTipoSolicitud(tipoSolicitud);
				this.getAsentamiento().createAuxConvenio(auxConvenio);					
			}
			return;																								
		}
		
		// Validar Estado de la Cuota
		if(convenioCuota.getEstadoConCuo().getId().longValue() != EstadoConCuo.ID_IMPAGO){
			this.registrarIndeterminado("Indeterminado por Cuota Cancelada: Pago Dúplice.", convenioCuota,null, "33");
			return;																								
		}
	    validarTime = System.currentTimeMillis() - validarTime;
		AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).addStats("Validaciones iniciales al procesar Cuota", validarTime);
		//this.getAsentamiento().logStats("<-> Tiempo consumido en Validaciones iniciales al procesar Cuota para transaccion nro "+this.getNroLinea()+": "+validarTime+" ms <->");

		long queryTime = System.currentTimeMillis();
		// Validar Consistencia en la Deuda incluida en el Convenio
		if(!convenioCuota.getConvenio().esConvenioConsistente()){
			this.registrarIndeterminado("Indeterminado por Convenio Inconsistente: La Deuda incluida en el Convenio no se encuentra en el estado que corresponde.", convenioCuota,null, "51"); 
			return;																								
		}
		queryTime = System.currentTimeMillis() - queryTime;
		AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).addStats("Validacion en procesar cuota, 'esConvenioInconsistente'", queryTime);
		//this.getAsentamiento().logStats("<-> Tiempo consumido en 'esConvenioInconsistente' para transaccion nro "+this.getNroLinea()+": "+queryTime+" ms <->");
		
		
		// Si el Pago corresponde a una Cuota no proveniente de un Recibo.
		Date fechaVencimiento = convenioCuota.getFechaVencimiento(); 
		DeudaAct cuotaAct = null;
		Double importeCalculado = convenioCuota.getImporteCuotaSinSellado();//convenioCuota.getImporteCuota();
		Double importeSellado = 0D;
		if(convenioCuota.getSellado() != null){
			importeSellado = convenioCuota.getImporteSellado();
		}
		boolean generaSaldoAFavor = false;
		if(this.getReciboTr()==null || this.getReciboTr().longValue()==0){
			// Si la Fecha de Pago es posterior a la Fecha de Vencimiento:
			boolean verificarVencimiento = false;
			if(DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
				fechaVencimiento = convenioCuota.getConvenio().getPlan().obtenerFechaVencimientoCuotaORecibo(fechaVencimiento,AsentamientoCache.getInstance().getMapFeriado());
				verificarVencimiento = true;
			}
			if(verificarVencimiento && DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){				
				queryTime = System.currentTimeMillis();
				List<CueExe> listCueExeNoActDeu = AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId())
									.getCueExeCache().getListCueExe(convenioCuota.getConvenio().getCuenta().getId());
				if(listCueExeNoActDeu == null)
					listCueExeNoActDeu = new ArrayList<CueExe>();
				// . buscar en mapPlanExe la lista de PlanExe para el plan
				List<PlanExe> listPlanExeNoActDeu = AsentamientoCache.getInstance().getListPlanExeNoActDeuByIdPlan(convenioCuota.getConvenio().getPlan().getId());
			    queryTime = System.currentTimeMillis() - queryTime;
				AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).addStats("Al procesar Cuota, 'prepararActualizacionImporteCuota'", queryTime);
				queryTime = System.currentTimeMillis();
				// cambiar llamada a metodo que recibe las listas.
				cuotaAct = convenioCuota.actualizacionImporteCuota(this.getFechaPago(),listCueExeNoActDeu, listPlanExeNoActDeu);		
			    queryTime = System.currentTimeMillis() - queryTime;
				AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).addStats("Al procesar Cuota, 'convenioCuota.actualizacionImporteCuota'", queryTime);
				
				//Obtengo el Descuento General Vigente para el ServicioBanco del Asentamiento.
				SerBanDesGen serBanDesGen = this.getAsentamiento().getServicioBanco().obtenerDescuentoGeneralVigente(this.getFechaPago());
				if(serBanDesGen != null){
					DesGen desGen = serBanDesGen.getDesGen();
					cuotaAct.aplicarDescuento(desGen.getPorDes());
				}				
				importeCalculado = cuotaAct.getImporteAct();
				this.getAsentamiento().logDetallado("	Importe de Cuota Actualizado por Fecha de Pago: "+this.getFechaPago()+" posterior a Fecha de Vencimiento: "+fechaVencimiento+". Importe Calculado: "+importeCalculado);
			}
			
			// Verificar Tolerancia en la diferencia entre el Importe Pagado y el Importe de la Cuota/Calculado
			Tolerancia tolerancia = AsentamientoCache.getInstance().getToleranciaById(this.getAsentamiento().getServicioBanco().getId());

			// Si el Importe Pagado es inferior al Importe de la Cuota/Calculado, registrar indeterminado.
			if(this.getImporte().doubleValue()<(importeCalculado+importeSellado)-Tolerancia.VALOR_FIJO){
				this.registrarIndeterminado("Indeterminado por pago de menos en Cuota.", convenioCuota, cuotaAct, "35");
				return;
			}
			// Si el Importe Pagado es hasta un toleranciaSaldo% mayor al Importe de la Cuota/Calculado, Pago Bueno
			if(this.getImporte().doubleValue()<=(importeCalculado+importeSellado)+Tolerancia.VALOR_FIJO){
				//Pago Bueno, sin saldo a favor
				generaSaldoAFavor = false;
			}else if(this.getImporte().doubleValue()<=(importeCalculado+importeSellado)*(1+tolerancia.getToleranciaSaldo())){ // Comentario: toleranciaSaldo = 20%
				// Pago Bueno, con saldo a favor
				generaSaldoAFavor = true;
			}else{
				this.registrarIndeterminado("Indeterminado por pago de más en la Cuota.", convenioCuota, cuotaAct, "36");
				return;				
			}
			// Validar si el Convenio se encuentra en estado Caduco.
			Convenio convenio = convenioCuota.getConvenio();
			
			// Si el Plan No Aplica Pago a Cuenta.
			if(convenio.getPlan().getAplicaPagCue().intValue() == SiNo.NO.getId().intValue()){
				this.procesarCuotaPagoBueno(convenioCuota, importeCalculado, generaSaldoAFavor);				
			}else{
			// Si el Plan Aplica Pago a Cuenta.
				queryTime = System.currentTimeMillis();
				int estaCaduco = convenio.estaCaduco(this.getFechaPago(), 
						AsentamientoCache.getInstance().getListRescateByIdPlan(convenio.getPlan().getId()), 
						AsentamientoCache.getInstance().getListPlanMotCadByIdPlan(convenio.getPlan().getId()),
						AsentamientoCache.getInstance().getMapFeriado());
				queryTime = System.currentTimeMillis() - queryTime;
				AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).addStats("Calcular en procesar cuota, 'convenio.estaCaduco'", queryTime);

				if(convenio.getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_VIGENTE
						&& estaCaduco == 0){
					// Asentar Cuota como Pago Bueno.
					this.procesarCuotaPagoBueno(convenioCuota, importeCalculado, generaSaldoAFavor);
				}
				if(estaCaduco == 1){
					// Cargar en lista para envio de Solicitud de Revision de Caso a Balance.
					TipoSolicitud tipoSolicitud = null;
					if(convenio.getViaDeuda().getId().longValue()==ViaDeuda.ID_VIA_ADMIN){
						tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_RESCATE_CONVENIO_VIA_ADM);
					}else { //if(convenio.getViaDeuda().getId().longValue()==ViaDeuda.ID_VIA_JUDICIAL){ Se comenta por si es via CyQ TODO ver si corresponde abrir nuevamente
						tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_RESCATE_CONVENIO_VIA_JUD);
					}					
					AuxConvenio auxConvenio = this.getAsentamiento().getAuxConvenio(convenio, tipoSolicitud);					
					if(auxConvenio == null){
						auxConvenio = new AuxConvenio();
						auxConvenio.setAsentamiento(this.getAsentamiento());
						auxConvenio.setConvenio(convenio);
						auxConvenio.setTipoSolicitud(tipoSolicitud);
						this.getAsentamiento().createAuxConvenio(auxConvenio);					
					}
					// Asentar Cuota como Pago a Cuenta
					this.procesarCuotaPagoACuenta(convenioCuota, importeCalculado, generaSaldoAFavor);
				}
				if(estaCaduco == 2){
					this.procesarCuotaPagoACuenta(convenioCuota, importeCalculado, generaSaldoAFavor);
				}				
			}
						
			// Si se Genero Saldo a Favor
			if(generaSaldoAFavor){
				Double saldoAFavorADistribuir = this.getImporte() - (importeCalculado+importeSellado);
				// Distribuir Saldo a Reintegrar
				this.distribuir(null, null, saldoAFavorADistribuir, TipoImporte.ID_SALDO_A_REINGRESAR, convenioCuota);
				if(this.hasError()){
					return;
				}		
			}
			
			// -> Registrar el Saldo a Favor
			if(generaSaldoAFavor){
				SinSalAFav sinSalAFav = new SinSalAFav();
				sinSalAFav.setSistema(convenioCuota.getSistema().getNroSistema());
				sinSalAFav.setNroComprobante(Long.valueOf(convenio.getNroConvenio()));
				sinSalAFav.setAnioComprobante(Long.valueOf(DateUtil.getAnio(convenio.getFechaFor())));
				sinSalAFav.setCuota(Long.valueOf(convenioCuota.getNumeroCuota()));
				sinSalAFav.setFiller_o(0L);
				sinSalAFav.setImportePago(this.getImporte());
				sinSalAFav.setImporteDebPag(importeCalculado+importeSellado);
				sinSalAFav.setFechaPago(this.getFechaPago());
				sinSalAFav.setFechaBalance(this.getFechaBalance());
				sinSalAFav.setTransaccion(this);
				sinSalAFav.setAsentamiento(this.getAsentamiento());

				if(this.getAsentamiento().getBalance() != null){
					sinSalAFav.setPartida(this.getPartidaSaldosAFavor());
					sinSalAFav.setCuenta(convenioCuota.getConvenio().getCuenta());					
				}
				
				this.getAsentamiento().createSinSalAFav(sinSalAFav);
			}	
		}else{
			// (Para el caso de un reingreso de Indeterminado proveniente de un recibo) 
			// (En una transaccion que no sea un reingreso reciboTr deberia ser null)
			this.getAsentamiento().logDetallado("	Reingreso de Indeterminado, Cuota proveniente del Recibo de Cuota Nro:"+this.getReciboTr());

			// Obtengo el ReciboConvenio.
			ReciboConvenio reciboConvenio = ReciboConvenio.getByNumero(this.getReciboTr());
			if(reciboConvenio ==null){
				this.registrarIndeterminado("Reingreso de Indeterminado: No existe el Recibo de Cuota indicado en la transaccion. Nro Recibo Tr: "+this.getReciboTr(), convenioCuota, null, "48");
				return;
			}
		
			// Obtengo el RecConCuo (Relacion entre la Cuota y el Recibo).
			RecConCuo recConCuo = RecConCuo.getByReciboConvenioYConvenioCuota(reciboConvenio, convenioCuota);
			if(recConCuo == null){
				this.registrarIndeterminado("Reingreso de Indeterminado: No existe la relación entre el Recibo y la Cuota. Nro Recibo: "+this.getReciboTr()+". Convenio: "+convenioCuota.getConvenio().getPlan()+"/"+convenioCuota.getConvenio().getNroConvenio()+" Cuota: "+convenioCuota.getNumeroCuota(), convenioCuota, null, "49");
				return;
			}

			// Validar que el importe de la transaccion sea igual al de la Cuota
			if(!NumberUtil.isDoubleEqualToDouble(this.getImporte(),convenioCuota.getImporteCuota(),0.01)){
				this.registrarIndeterminado("Reingreso de Indeterminado: Indeterminado por importe de transacción distinto del importe de la Cuota. Importe Transacción: "+this.getImporte()+". Importe de la Cuota: "+convenioCuota.getImporteCuota(), convenioCuota, null, "50");
				return;
			}

			Convenio convenio = convenioCuota.getConvenio();
			
			boolean procesarPagoBueno = false;	
			
			// Si el Plan No Aplica Pago a Cuenta.
			if(convenio.getPlan().getAplicaPagCue().intValue() == SiNo.NO.getId().intValue()){
				procesarPagoBueno = true;				
			}else{
			// Si el Plan Aplica Pago a Cuenta.
				boolean estaCaduco = convenio.estaCaduco(this.getFechaPago());
				boolean tienePagoCue = convenio.tienePagoCue();
				
				if(convenio.getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_VIGENTE
						&& !estaCaduco
						&& !tienePagoCue){
					// Asentar Cuota como Pago Bueno.
					procesarPagoBueno = true;
				}
				if(!estaCaduco && tienePagoCue){
					// Cargar en lista para envio de Solicitud de Revision de Caso a Balance.
					TipoSolicitud tipoSolicitud = null;
					if(convenio.getViaDeuda().getId().longValue()==ViaDeuda.ID_VIA_ADMIN){
						tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_RESCATE_CONVENIO_VIA_ADM);
					}else { //if(convenio.getViaDeuda().getId().longValue()==ViaDeuda.ID_VIA_JUDICIAL){ Se comenta por si es via CyQ TODO ver si corresponde abrir nuevamente
						tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_RESCATE_CONVENIO_VIA_JUD);
					}					
					AuxConvenio auxConvenio = this.getAsentamiento().getAuxConvenio(convenio, tipoSolicitud);
					if(auxConvenio == null){
						auxConvenio = new AuxConvenio();
						auxConvenio.setAsentamiento(this.getAsentamiento());
						auxConvenio.setConvenio(convenio);
						auxConvenio.setTipoSolicitud(tipoSolicitud);
						this.getAsentamiento().createAuxConvenio(auxConvenio);					
					}
					// Asentar Cuota como Pago a Cuenta
					procesarPagoBueno = false;
				}
				if(estaCaduco){
					procesarPagoBueno = false;
				}						
			}
		 
			if(procesarPagoBueno){
				this.procesarCuotaDeReciboPagoBueno(recConCuo);
			}else{
				this.procesarCuotaDeReciboPagoACuenta(recConCuo);
			}
		}
	
		//-> Si la Cuota Tiene Sellado, se distribuye.
		if(convenioCuota.getSellado() != null){
			if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){
				Sellado sellado = convenioCuota.getSellado();
				this.getAsentamiento().logDetallado("	Distribuir Sellado... ");
				
				if(sellado == null){
					this.getAsentamiento().logDetallado("	No posee sellado.");
					return;											
				}
				List<ParSel> listParSel = sellado.getListParSel();
				Double importeSelladoSinMontoFijo = convenioCuota.getImporteSellado();
				for(ParSel parSel: listParSel){
					if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO){
						importeSelladoSinMontoFijo -= parSel.getMonto();
					}									
				}
				for(ParSel parSel: listParSel){
					AuxSellado auxSellado = this.getAsentamiento().getAuxSellado(this, parSel);
					if(auxSellado != null){
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO){
							auxSellado.setImporteFijo(auxSellado.getImporteFijo()+parSel.getMonto());
							this.getAsentamiento().logDetallado("	Acumula Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Monto Fijo: "+parSel.getMonto()+", Importe Acumulado: "+auxSellado.getImporteFijo());
						}					
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_PORCENTAJE){
							auxSellado.setImporteEjeAct(auxSellado.getImporteEjeAct()+(importeSelladoSinMontoFijo));
							this.getAsentamiento().logDetallado("	Acumula Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Sin Monto Fijo: "+importeSelladoSinMontoFijo+", Importe Acumulado: "+auxSellado.getImporteEjeAct()+", Porcentaje: "+parSel.getMonto());
						}
					}else{
						auxSellado = new AuxSellado();
						auxSellado.setAsentamiento(this.getAsentamiento());
						Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), 
				                 this.getSistema());
						auxSellado.setSistema(sistema);
						auxSellado.setSellado(sellado);
						auxSellado.setFechaPago(this.getFechaPago());
						auxSellado.setPartida(parSel.getPartida());
						auxSellado.setImporteEjeAct(0D);
						auxSellado.setImporteEjeVen(0D);
						auxSellado.setImporteFijo(0D);
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO){
							auxSellado.setEsImporteFijo(SiNo.SI.getId());
							auxSellado.setImporteFijo(parSel.getMonto());
							auxSellado.setPorcentaje(0D);
							this.getAsentamiento().logDetallado("	Crea Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Monto Fijo: "+parSel.getMonto()+", Importe Acumulado: "+auxSellado.getImporteFijo());
						}
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_PORCENTAJE){
							auxSellado.setEsImporteFijo(SiNo.NO.getId());
							auxSellado.setImporteEjeAct((importeSelladoSinMontoFijo));
							auxSellado.setPorcentaje(parSel.getMonto());
							this.getAsentamiento().logDetallado("	Crea Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Sin Monto Fijo: "+importeSelladoSinMontoFijo+", Importe Acumulado: "+auxSellado.getImporteEjeAct()+", Porcentaje: "+parSel.getMonto());
						}
						this.getAsentamiento().createAuxSellado(auxSellado);
					}
				}			
			}	
		}

		
		this.getAsentamiento().logDetallado("	Saliendo de Procesar Cuota...");

	}

	/**
	 * Procesar Cuota como Pago Bueno.
	 * 	
	 * <i>(paso 2.1.f)</i>
	 */
	public void procesarCuotaPagoBueno(ConvenioCuota convenioCuota, Double importeCalculado, boolean generaSaldoAFavor) throws Exception{
		this.getAsentamiento().logDetallado("	Entrando a Procesar Pago Bueno...");
		// Obtener el Numero de Cuota a Imputar el Pago.
		long nroCuotaTime = System.currentTimeMillis();
		Integer nroCuotaAImputar = 0;
		if(convenioCuota.getNumeroCuota().longValue() == 1L){
			nroCuotaAImputar = 1;
		}else{			
			Integer ultimaCuotaPaga = convenioCuota.getConvenio().getUltCuoImp();
			Integer cantCuotasAsen = this.getAsentamiento().getCuotaCounter(convenioCuota.getConvenio().getId());
			this.getAsentamiento().logDetallado("		Cant. de Cuotas Previamente Asentadas: "+cantCuotasAsen);
			if(ultimaCuotaPaga == 0)
				ultimaCuotaPaga++;
			nroCuotaAImputar = ultimaCuotaPaga + cantCuotasAsen;
			nroCuotaAImputar++;
		}
		this.getAsentamiento().logDetallado("		Nro de Cuota a Imputar: "+nroCuotaAImputar);
		nroCuotaTime = System.currentTimeMillis() - nroCuotaTime;
		AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).addStats("Obtener nro de cuota a imputar en procesar cuota", nroCuotaTime);
		//this.getAsentamiento().logStats("<-> Tiempo consumido al obtener nro de cuota a imputar para transaccion nro "+this.getNroLinea()+": "+nroCuotaTime+" ms <->");

		// Obtener el "Capital de la Cuota a asentar" e "Interes Financiero de la Cuota a asentar"
		Double capitalCuotaAAsentar = 0D;
		Double interesCuotaAAsentar = 0D;
		ConvenioCuota convenioCuotaAAsentar = null;
		if(nroCuotaAImputar.intValue() != convenioCuota.getNumeroCuota().intValue()){
			long queryTime = System.currentTimeMillis();
			//convenioCuotaAAsentar = ConvenioCuota.getByNroCuoNroConSis(new Long(nroCuotaAImputar), new Long(convenioCuota.getConvenio().getNroConvenio()), convenioCuota.getSistema().getId());			
			convenioCuotaAAsentar = ConvenioCuota.getByNroCuoIdCon(new Long(nroCuotaAImputar), convenioCuota.getConvenio().getId());
			queryTime = System.currentTimeMillis() - queryTime;
			AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).addStats("Al procesar cuota, 'ConvenioCuota.getByNroCuoNroConSis'", queryTime);
			//this.getAsentamiento().logStats("<-> Tiempo consumido al procesar 'ConvenioCuota.getByNroCuoNroConSis' para transaccion nro "+this.getNroLinea()+": "+queryTime+" ms <->");

		}else{
			convenioCuotaAAsentar = convenioCuota;
		}
		if(convenioCuotaAAsentar == null){
			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encuentra la Cuota en la DB. Cuota: "+nroCuotaAImputar+", Convenio: "+convenioCuota.getConvenio().getNroConvenio()+", Nro Sistema: "+convenioCuota.getSistema().getNroSistema());
			return;
		}
		capitalCuotaAAsentar = convenioCuotaAAsentar.getCapitalCuota();
		interesCuotaAAsentar = convenioCuotaAAsentar.getInteres();
		this.getAsentamiento().logDetallado("		Capital de Cuota a Asentar: "+capitalCuotaAAsentar+" Interes de Cuota a Asentar: "+interesCuotaAAsentar);
		
		// Obtener el "Capital", "Interes Financiero" y "Actualizacion"  de la cuota a distribuir
		Double actualizacionCalculada = 0D;
		Double capitalADistribuir = 0D;
		Double interesADistribuir = 0D;
		Double actualizacionADistribuir = 0D;
		//-> Si la Cuota a procesar no proviene de un Recibo.
		actualizacionCalculada = importeCalculado - convenioCuota.getImporteCuotaSinSellado();//convenioCuota.getImporteCuota(); 
		// Si no se genera un saldo a favor
		Double importePago = this.getImporte();
		if(convenioCuota.getSellado() != null){
			importePago = this.getImporte()-convenioCuota.getImporteSellado();
		}
		if(!generaSaldoAFavor){
			if(importePago.doubleValue() >= importeCalculado){
				capitalADistribuir = capitalCuotaAAsentar;
				interesADistribuir = interesCuotaAAsentar;
				actualizacionADistribuir = actualizacionCalculada + (importePago- importeCalculado);
			}else{
				if(actualizacionCalculada.doubleValue() >= (importeCalculado - importePago)){
					capitalADistribuir = capitalCuotaAAsentar;
					interesADistribuir = interesCuotaAAsentar;
					actualizacionADistribuir = actualizacionCalculada - (importeCalculado - importePago); 					
				}else{
					capitalADistribuir = capitalCuotaAAsentar - ((actualizacionCalculada 
							-(importeCalculado-importePago)*capitalCuotaAAsentar/
							(capitalCuotaAAsentar+interesCuotaAAsentar)));
					interesADistribuir = interesCuotaAAsentar - ((actualizacionCalculada - (importeCalculado - 
							importePago))*interesCuotaAAsentar/(capitalCuotaAAsentar+interesCuotaAAsentar));
					actualizacionADistribuir = 0D; 											
				}
			}
			// Si se genera un saldo a favor
		}else{
			capitalADistribuir = capitalCuotaAAsentar;
			interesADistribuir = interesCuotaAAsentar;
			actualizacionADistribuir = actualizacionCalculada; 					
		}
		
		this.getAsentamiento().logDetallado("		Capital a Distribuir: "+capitalADistribuir);
		this.getAsentamiento().logDetallado("		Interes a Distribuir: "+interesADistribuir);
		this.getAsentamiento().logDetallado("		Actualizacion a Distribuir: "+actualizacionADistribuir);
		this.getAsentamiento().logDetallado("		(la actualizacion se distribuye junto al interes");
		interesADistribuir += actualizacionADistribuir;
		
		// Acumuladores para Verificacion de Importe Distribuido
		Double capitalDistribuido = 0D;
		Double actualizacionDistribuido = 0D;
		Double interesDistribuido = 0D;
		
		//. buscar pila en mapStackConDeu, si no lo encuentra, tira el query para obtener la lista de convenioDeuda
		//  con saldo mayor a cero y la carga en el mapa para el idConvenio.
		if(!this.getAsentamiento().existStackConDeu(convenioCuota.getConvenio().getId())){
			//. buscar lista de convenioDeuda con saldo mayor a cero
			List<ConvenioDeuda> listConvenioDeuda = ConvenioDeuda.getListWithSaldo(convenioCuota.getConvenio());
			this.getAsentamiento().putStackConDeu(convenioCuota.getConvenio().getId(), listConvenioDeuda);
		}
		
		//-> Mientras el Capital de la cuota a asentar sea mayor a cero.
		Double capitalTotalCuotaAAsentar = capitalCuotaAAsentar; 
		this.getAsentamiento().logDetallado("		Entrando al bucle con Capital de Cuota a Asentar: "+capitalCuotaAAsentar);
		while(capitalCuotaAAsentar>0){
			// Obtener la Deuda a Cancelar
			long queryTime = System.currentTimeMillis();
			
			//. obtiene el auxConDeu con saldo mayor a cero del mapa de session
			AuxConDeu auxConDeu = this.getAsentamiento().getAuxConDeuConSaldoMayorCero(convenioCuota.getConvenio().getId());
			
			queryTime = System.currentTimeMillis() - queryTime;
			AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).addStats("Obtener para la cuota, 'AuxConDeu.getDeudaACancelarByAsentamientoYConvenio'", queryTime);
			//this.getAsentamiento().logStats("<-> Tiempo consumido al procesar 'AuxConDeu.getDeudaACancelarByAsentamientoYConvenio' para transaccion nro "+this.getNroLinea()+": "+queryTime+" ms <->");
			

			ConvenioDeuda convenioDeuda = null;
			Double saldoEnPlan = 0D;
			if(auxConDeu !=  null){
				convenioDeuda = auxConDeu.getConvenioDeuda();
				saldoEnPlan = auxConDeu.getSaldoEnPlan();
			}else{
				queryTime = System.currentTimeMillis();

				//. obtener el convenioDeuda de la pila de convenioDeuda con saldo mayor a cero guardada en el mapStackConDeu
				convenioDeuda = this.getAsentamiento().popFromStackConDeu(convenioCuota.getConvenio().getId());				
				
				queryTime = System.currentTimeMillis() - queryTime;
				AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).addStats("Obtener para la cuota, 'ConvenioDeuda.getDeudaACancelarByConvenioYAse'", queryTime);
				//this.getAsentamiento().logStats("<-> Tiempo consumido al procesar 'ConvenioDeuda.getDeudaACancelarByConvenioYAse' para transaccion nro "+this.getNroLinea()+": "+queryTime+" ms <->");
				

				if(convenioDeuda == null && capitalCuotaAAsentar<0.5){
					capitalCuotaAAsentar = 0D;
					break;
				}
				if(convenioDeuda == null && capitalCuotaAAsentar>=0.5){
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encuentra Deuda relacionada al Convenio para cancelar. Cuota: "+nroCuotaAImputar+", Convenio: "+convenioCuota.getConvenio().getNroConvenio()+", Nro Sistema: "+convenioCuota.getSistema().getNroSistema()+", Capital de Cuota A Asentar restante: "+capitalCuotaAAsentar);
					return;
				}
				saldoEnPlan = convenioDeuda.getSaldoEnPlan();
			}
			this.getAsentamiento().logDetallado("			Deuda relacionada al Convenio para Cancelar con id "+convenioDeuda.getIdDeuda()+", Saldo en Plan: "+saldoEnPlan);
			// Determinar el Saldo de la Deuda obtenida que cubre el Capital de la Cuota a Asentar
			Double saldoEnPlanCub = 0D;
			if(capitalCuotaAAsentar.doubleValue() > saldoEnPlan.doubleValue()){
				saldoEnPlanCub = saldoEnPlan;
				this.getAsentamiento().logDetallado("			Capital de Cuota a Asentar: "+capitalCuotaAAsentar+" > Saldo en Plan: "+saldoEnPlan+"............Saldo en Plan Cubierto: "+saldoEnPlanCub);				
			}else{
				saldoEnPlanCub = capitalCuotaAAsentar;
				this.getAsentamiento().logDetallado("			Capital de Cuota a Asentar: "+capitalCuotaAAsentar+" < Saldo en Plan: "+saldoEnPlan+"............Saldo en Plan Cubierto: "+saldoEnPlanCub);				
			}
			// Insertar/Actualizar registro en AuxConDeu.
			if(auxConDeu != null){
				auxConDeu.setSaldoEnPlan(saldoEnPlan - saldoEnPlanCub);
				auxConDeu.setFechaPago(this.getFechaPago());
				auxConDeu.setTransaccion(this);
				this.getAsentamiento().updateAuxConDeu(auxConDeu);
			}else{
				auxConDeu = new AuxConDeu();
				auxConDeu.setAsentamiento(this.getAsentamiento());
				auxConDeu.setConvenioDeuda(convenioDeuda);
				auxConDeu.setSaldoEnPlan(saldoEnPlan - saldoEnPlanCub);
				auxConDeu.setFechaPago(this.getFechaPago());
				auxConDeu.setTransaccion(this); 
				this.getAsentamiento().createAuxConDeu(auxConDeu);
			}
			// Grabar la relacion de la deuda y la cuota en AuxConDeuCuo
			AuxConDeuCuo auxConDeuCuo = new AuxConDeuCuo();
			auxConDeuCuo.setAsentamiento(this.getAsentamiento());
			auxConDeuCuo.setConvenioCuota(convenioCuota);
			auxConDeuCuo.setConvenioDeuda(convenioDeuda);
			auxConDeuCuo.setSaldoEnPlanCub(saldoEnPlanCub);
			Integer esPagoTotal = SiNo.NO.getId();
			if(convenioDeuda.getDeuda().getImporte().doubleValue()==saldoEnPlanCub)
				esPagoTotal = SiNo.SI.getId();
			auxConDeuCuo.setEsPagoTotal(esPagoTotal);
			auxConDeuCuo.setTransaccion(this); 
			this.getAsentamiento().createAuxConDeuCuo(auxConDeuCuo);
		
			//-> Si es un Asentamiento Comun 
			if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){
				// Obtener Distribuidor de Partidas Vigentes asociadas al Plan del Convenio de la Cuota.
				Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), 
		                 this.getSistema());
				if(sistema == null){
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro el sistema de la transaccion." );
					return;
				}		
				Plan plan = convenioCuota.getConvenio().getPlan();
				Recurso recurso = AsentamientoCache.getInstance().getRecursoById(convenioCuota.getConvenio().getRecurso().getId());
				DisParPla disParPla=null;
				String valorAtrVig = null; 
				if(recurso.getAtributoAse() != null){
					valorAtrVig = convenioDeuda.getDeuda().getAtrAseVal();
					this.getAsentamiento().logDetallado("			Valor de Atributo de Asentamiento de la Deuda: "+valorAtrVig);
				}
				// Recupera el DisParPla
				List<DisParPla> listDisParPla =  AsentamientoCache.getInstance().getListByPlanRecValAtrAse(plan, recurso, valorAtrVig);
				if (listDisParPla.size()==0 ) {
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro Distribuidor de partidas para: idPlan: " + plan.getId() + 
							" valorAtributoAsentamiento: " + valorAtrVig);
					return;					
				} else if (listDisParPla.size()>1) {
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Se encontro mas de un Distribuidor de partidas para: idPlan: " + plan.getId() + 
							" valorAtributoAsentamiento: " + valorAtrVig);
					return;
				} else {
					// Toma el DisParPla
					disParPla = listDisParPla.get(0);
				}
				DisPar disPar = disParPla.getDisPar();

				// Obtener los conceptos asociados a la Deuda en el convenio.
				List<DeuRecCon> listDeuRecCon = convenioDeuda.getDeuda().getListDeuRecConByString();
				this.getAsentamiento().logDetallado("			String con Lista de Conceptos de la Deuda: "+convenioDeuda.getDeuda().getStrConceptosProp());
				// Validar Total Conceptos con Importe, y logear Advertencia.
				if(!this.validaConceptos(listDeuRecCon, convenioDeuda.getDeuda().getImporte(), 0.01)){
					AdpRun.currentRun().logDebug("Advertencia!: Transaccion de Id: "+this.getId()+", Linea Nro "+this.getNroLinea()+", Se encontró diferencias entre el importe de la deuda y la suma de sus conceptos. Deuda de id: "+convenioDeuda.getDeuda().getId()+" Importe Deuda: "+convenioDeuda.getDeuda().getImporte()+" Lista de Conceptos: "+convenioDeuda.getDeuda().getStrConceptosProp());
					AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).incWarnings();
				}				

				// Para cada concepto:
				Double capitalCubiertoADistribuir = (saldoEnPlanCub * capitalADistribuir/capitalTotalCuotaAAsentar)
				* (convenioDeuda.getCapitalEnPlan()/convenioDeuda.getTotalEnPlan());
				Double actualizacionCubiertoADistribuir = ((saldoEnPlanCub) * capitalADistribuir/capitalTotalCuotaAAsentar)
				* convenioDeuda.getActEnPlan()/convenioDeuda.getTotalEnPlan();
				Double interesFinancieroADistribuir = (capitalCubiertoADistribuir+actualizacionCubiertoADistribuir)
				* interesADistribuir/capitalADistribuir;

				this.getAsentamiento().logDetallado("			Capital Cubierto a Distribuir: "+capitalCubiertoADistribuir+", Actualizacion Cubierta a Distribuir: "+actualizacionCubiertoADistribuir+", Interes Financiero a Distribuir: "+interesFinancieroADistribuir);
				this.getAsentamiento().logDetallado("			Realizando Distribucion...");
				for(DeuRecCon deuRecCon: listDeuRecCon){
					if(deuRecCon.getImporte().doubleValue() != 0){
						// Distribuir Capital:
						this.distribuir(disPar, deuRecCon, capitalCubiertoADistribuir, TipoImporte.ID_CAPITAL, deuRecCon.getDeuda());
						if(this.hasError()){
							return;
						}
						// Distribuir Actualizacion:
						this.distribuir(disPar, deuRecCon, actualizacionCubiertoADistribuir, TipoImporte.ID_ACTUALIZACION, deuRecCon.getDeuda());
						if(this.hasError()){
							return;
						}
						// Distribuir Interes Financiero:
						this.distribuir(disPar, deuRecCon, interesFinancieroADistribuir, TipoImporte.ID_INTERES_FINANCIERO, deuRecCon.getDeuda());
						if(this.hasError()){
							return;
						}						
					}
				}
				// Acumulo los importes Distribuidos para control y logs.
				capitalDistribuido += capitalCubiertoADistribuir;
				actualizacionDistribuido += actualizacionCubiertoADistribuir;
				interesDistribuido += interesFinancieroADistribuir;
			}
			// Descontar del Capital de la Cuota a procesar el SaldoPlanCub
			capitalCuotaAAsentar = capitalCuotaAAsentar - saldoEnPlanCub;
			this.getAsentamiento().logDetallado("			Capital de Cuota a Asentar-Saldo en Plan Cubierto: "+capitalCuotaAAsentar);
			this.getAsentamiento().logDetallado("			Saldo en Plan Cubierto: "+saldoEnPlanCub);
		}
		Double importeDistribuido = capitalDistribuido+actualizacionDistribuido+interesDistribuido;
		if(generaSaldoAFavor){
			importeDistribuido += this.getImporte()-importeCalculado;
		}
		// (Para considerar cuotas con sellado reemplazamos "this.getImporte()" por "importePago" donde si existe sellado se quita el valor que corresponde)
		if(!NumberUtil.isDoubleEqualToDouble(importeDistribuido,importePago,0.01)){
			AdpRun.currentRun().logDebug("Advertencia!: Transaccion de Id: "+this.getId()+", Linea Nro "+this.getNroLinea()+" Importe: "+importePago+" Importe Distribuido: "+importeDistribuido+" (no incluye importe sellado)");
			AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).incWarnings();
			
		}
		
		//-> Registrar el Pago Bueno en AuxPagCuo.
		AuxPagCuo auxPagCuo = new AuxPagCuo();
		auxPagCuo.setAsentamiento(this.getAsentamiento());
		auxPagCuo.setConvenioCuota(convenioCuota);
		auxPagCuo.setTransaccion(this);
		auxPagCuo.setEstadoConCuo(EstadoConCuo.getByIdNull(EstadoConCuo.ID_PAGO_BUENO));
		auxPagCuo.setActualizacion(actualizacionADistribuir);  
		auxPagCuo.setNroCuotaImputada(nroCuotaAImputar);
		
		this.getAsentamiento().createAuxPagCuo(auxPagCuo);
		this.getAsentamiento().addAuxPagCuoToIndex(convenioCuota.getId());
		
		if(nroCuotaAImputar.intValue() != 1)
			this.getAsentamiento().incCuotaCounter(convenioCuota.getConvenio().getId());
		
		this.getAsentamiento().logDetallado("	Saliendo de Procesar Pago Bueno...");
	}
	
	/**
	 * Procesar Cuota como Pago A Cuenta.
	 * 	
	 * <i>(paso 2.1.g)</i>
	 */
	public void procesarCuotaPagoACuenta(ConvenioCuota convenioCuota, Double importeCalculado, boolean generaSaldoAFavor) throws Exception{
		this.getAsentamiento().logDetallado("	Entrando a Procesar Pago a Cuenta...");
		AuxPagCuo auxPagCuo = new AuxPagCuo();
		auxPagCuo.setAsentamiento(this.getAsentamiento());
		auxPagCuo.setConvenioCuota(convenioCuota);
		auxPagCuo.setTransaccion(this);
		auxPagCuo.setReciboConvenio(null);
		auxPagCuo.setEstadoConCuo(EstadoConCuo.getByIdNull(EstadoConCuo.ID_PAGO_A_CUENTA));
		auxPagCuo.setActualizacion(this.getImporte()-convenioCuota.getImporteCuota());
		auxPagCuo.setNroCuotaImputada(null);
		
		this.getAsentamiento().createAuxPagCuo(auxPagCuo);
		this.getAsentamiento().addAuxPagCuoToIndex(convenioCuota.getId());
		
		//-> Si es un Asentamiento Comun 
		if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){
			// Obtener Distribuidor de Partidas Vigentes asociadas al Plan del Convenio de la Cuota.
			Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), 
	                 this.getSistema());
			if(sistema == null){
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro el sistema de la transaccion." );
				return;
			}		

			Plan plan = convenioCuota.getConvenio().getPlan();
			Recurso recurso = AsentamientoCache.getInstance().getRecursoById(convenioCuota.getConvenio().getRecurso().getId());
			DisParPla disParPla=null;
			String atrAseVal = null; 
			long queryTime = System.currentTimeMillis();		
			if(recurso.getAtributoAse() != null){
				atrAseVal = convenioCuota.getConvenio().getValorAtributo();
				if(atrAseVal == null)
					atrAseVal = convenioCuota.getConvenio().getCuenta().getValorAtributo(recurso.getAtributoAse().getId(), convenioCuota.getConvenio().getFechaFor());
				this.getAsentamiento().logDetallado("		Valor de Atributo de Asentamiento de la Cuenta para Fecha de Formalizacion del Plan: "+atrAseVal);
			}
		    queryTime = System.currentTimeMillis() - queryTime;
			AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).addStats("Al procesar Cuota Pago a Cuenta, 'obtener valor de atributo'", queryTime);
			//this.getAsentamiento().logStats("<-> Tiempo consumido al obtener valor de atr de asentamiento en pago a Cuenta para transaccion nro "+this.getNroLinea()+": "+queryTime+" ms <->");
	
			// Recupera el DisParPla
			List<DisParPla> listDisParPla =  AsentamientoCache.getInstance().getListByPlanRecValAtrAse(plan, recurso, atrAseVal);
			if (listDisParPla.size()==0 ) {
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro Distribuidor de partidas para: idPlan: " + plan.getId() + 
						" valorAtributoAsentamiento: " + atrAseVal);
				return;					
			} else if (listDisParPla.size()>1) {
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Se encontro mas de un Distribuidor de partidas para: idPlan: " + plan.getId() + 
						" valorAtributoAsentamiento: " + atrAseVal);
				return;
			} else {
				// Toma el DisParPla
				disParPla = listDisParPla.get(0);
			}
			DisPar disPar = disParPla.getDisPar();

			// Obtener los Detalles del Distribuidor de Partida para el tipo importe Pago a Cuenta.
			this.getAsentamiento().logDetallado("		Distribuidor encontrado. Buscando Detalle para el Tipo de Importe Pago a Cuenta.");	
			List<DisParDet> listDisParDet = disPar.getListDisParDetByidTipoImporteYRecCon(TipoImporte.ID_PAGO_A_CUENTA, null);
			if(ListUtil.isNullOrEmpty(listDisParDet)){
				String message = "";
				message += "Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No existe el Detalle del Distribuidor de Partidas asociado al Plan "+plan.getDesPlan();
				message +=", Distribuidor de id: "+disPar.getId();
				message +=", Tipo de Importe Pago A Cuenta." ;
				this.addRecoverableValueError(message);
				return;
			}
			// Determinar si corresponde a Ejercicio Actual o Vencido, validando si la fecha de vencimiento y la fecha de 
			// pago corresponden al mismo ejercicio.
			boolean esActual = false;
			Date fechaVencimiento = convenioCuota.getFechaVencimiento();
			if(DateUtil.isDateAfterOrEqual(fechaVencimiento,this.getAsentamiento().getEjercicio().getFecIniEje()) 
					&& DateUtil.isDateBeforeOrEqual(fechaVencimiento, this.getAsentamiento().getEjercicio().getFecFinEje())
					&& DateUtil.isDateAfterOrEqual(this.getFechaPago(),this.getAsentamiento().getEjercicio().getFecIniEje())
					&& DateUtil.isDateBeforeOrEqual(this.getFechaPago(), this.getAsentamiento().getEjercicio().getFecFinEje())){
						esActual = true;
			}
			Double importeSellado = 0D;
			if(convenioCuota.getSellado() != null )
				importeSellado = convenioCuota.getImporteSellado();
			Double importe = this.getImporte()-importeSellado;//this.getImporte();
			if(generaSaldoAFavor){
				importe = importeCalculado; 								
			}
			
			// Por cada detalle:
			for(DisParDet disParDet: listDisParDet){
		 
				AuxRecaudado auxRecaudado = this.getAsentamiento().getAuxRecaudado(this, disParDet.getPartida(), disParDet.getPorcentaje(), null, convenioCuota.getConvenio().getPlan(), Transaccion.TIPO_BOLETA_CUOTA);			
				// Si se encontro un registro
				if(auxRecaudado != null){
					// Actualizar el importe acumulando el Importe Pago 
					if(esActual)
						auxRecaudado.setImporteEjeAct(auxRecaudado.getImporteEjeAct()+importe);
					else
						auxRecaudado.setImporteEjeVen(auxRecaudado.getImporteEjeVen()+importe);
					
				// Si no se encontro un registro
				}else{
					// Insertar registro en AuxRecaudado
					auxRecaudado = new AuxRecaudado();
					auxRecaudado.setAsentamiento(this.getAsentamiento());
					auxRecaudado.setSistema(sistema);
					auxRecaudado.setFechaPago(this.getFechaPago());
					auxRecaudado.setPartida(disParDet.getPartida());
					auxRecaudado.setViaDeuda(null);
					auxRecaudado.setPlan(convenioCuota.getConvenio().getPlan());
					auxRecaudado.setTipoBoleta(Transaccion.TIPO_BOLETA_CUOTA);
					if(esActual){
						auxRecaudado.setImporteEjeAct(importe);
						auxRecaudado.setImporteEjeVen(0D);
					}else{
						auxRecaudado.setImporteEjeAct(0D);
						auxRecaudado.setImporteEjeVen(importe);	
					}
					auxRecaudado.setPorcentaje(disParDet.getPorcentaje());
					this.getAsentamiento().createAuxRecaudado(auxRecaudado);
					Double paraLog = 0D;
					if(AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).isLogDetalladoEnabled())
						paraLog = auxRecaudado.getImporteEjeAct()+auxRecaudado.getImporteEjeVen();
					this.getAsentamiento().logDetallado("		AuxRecaudado, Partida: "+disParDet.getPartida().getDesPartida()+" Importe Agregado: "+importe+" Importe Acumulado: "+paraLog+" Porcentaje: "+auxRecaudado.getPorcentaje()*100+" %");
				}
			}
			
			// Si corresponde a Servicio Banco: 85 - Otros Tributos (ex Gravamenes Especiales), guardamos datos para reporte por Recurso.
			if(ServicioBanco.COD_OTROS_TRIBUTOS.equals(this.getAsentamiento().getServicioBanco().getCodServicioBanco())){
				// Buscamos un registro acumulador de importe por Recurso y Tipo de Importe
				AuxImpRec auxImpRec = null;
				auxImpRec = AuxImpRec.getForAsentamiento(recurso.getId(), TipoImporte.ID_PAGO_A_CUENTA, this.getAsentamiento().getId());
				if(auxImpRec != null){
					auxImpRec.setImporte(auxImpRec.getImporte()+this.getImporte());
					this.getAsentamiento().updateAuxImpRec(auxImpRec);				
				}else{
					// Si no existe lo creamos
					auxImpRec = new AuxImpRec();
					auxImpRec.setRecurso(recurso);
					TipoImporte tipoImporte = TipoImporte.getById(TipoImporte.ID_PAGO_A_CUENTA);
					auxImpRec.setTipoImporte(tipoImporte);
					auxImpRec.setAsentamiento(this.getAsentamiento());
					auxImpRec.setImporte(this.getImporte());
					this.getAsentamiento().createAuxImpRec(auxImpRec);
				}
			}
		}
		this.getAsentamiento().logDetallado("	Saliendo de Procesar Pago a Cuenta...");
	}
	
	/**
	 * Procesar Recibo Cuota.
	 * 	
	 * <i>(paso 2.1.d)</i>
	 */
	public void procesarReciboCuota(ReciboConvenio reciboConvenio) throws Exception{
		this.getAsentamiento().logDetallado("Entrando a Procesar Recibo de Cuota...");
		// Validar que Fecha de Balance > Fecha Pago, si no registrar indeterminado.
		if(DateUtil.isDateAfter(this.getFechaPago(), this.getFechaBalance())){
			this.registrarIndeterminado("Indeterminado por Fecha de Pago mayor a Fecha de Balance.", reciboConvenio, "11");
			return;																					
		}

		// Validar si la transaccion ya fue procesada en el mismo Asentamiento.
		if(this.getAsentamiento().existAuxPagRecCon(reciboConvenio.getId())){
			this.registrarIndeterminado("Indeterminado por Recibo de Cuota Cancelado: Pago Dúplice en el mismo Asentamiento.", reciboConvenio,"37");
			return;																		
		}		
		// Validar si el Recibo se encuentra Pago
		if(reciboConvenio.getFechaPago() != null){
			this.registrarIndeterminado("Indeterminado por Recibo de Cuota Cancelada: Pago Dúplice.", reciboConvenio, "38");
			return;																								
		}
		
		boolean convenioConsistente = true;
		// Validar Consistencia en la Deuda incluida en el Convenio
		if(!reciboConvenio.getConvenio().esConvenioConsistente()){
			//this.registrarIndeterminado("Indeterminado por Convenio Inconsistente: La Deuda incluida en el Convenio no se encuentra en el estado que corresponde.", reciboConvenio, "51");
			convenioConsistente = false;
			//return;		
		}

		// Obtengo la fecha de vencimiento
		Date fechaVencimiento = reciboConvenio.getFechaVencimiento();
		boolean verificarVencimiento = false;
		if(DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
			fechaVencimiento = reciboConvenio.getConvenio().getPlan().obtenerFechaVencimientoCuotaORecibo(reciboConvenio.getFechaVencimiento(),AsentamientoCache.getInstance().getMapFeriado());
			verificarVencimiento = true;
		}
		//Si la Fecha de Pago es posterior a la Fecha de Vencimiento registrar indeterminado.
		if(verificarVencimiento && DateUtil.isDateAfter(this.getFechaPago(), fechaVencimiento)){
			this.registrarIndeterminado("Indeterminado por Fecha de Pago mayor a Fecha de Vencimiento en Recibo de Cuota "+reciboConvenio.getNroRecibo()+"/"+reciboConvenio.getAnioRecibo()+".", reciboConvenio, "39");
			return;
		}
				
		// Obtener las Cuotas asociadas al Recibo.
		List<RecConCuo> listRecConCuo	 = reciboConvenio.getListRecConCuo();
		// Validar si la suma de los importes actualizados de las Cuotas coincide con el importe del recibo.
		Double sumImporteDetalle = 0D; 
		for(RecConCuo recConCuo: listRecConCuo)
			sumImporteDetalle += recConCuo.getTotCapitalOriginal()+recConCuo.getTotIntFin()+recConCuo.getTotActualizacion() + recConCuo.getImporteSellado();//+recConCuo.getTotSellado();
		Double importeSellado = 0D;
		if(reciboConvenio.getImporteSellado() != null)	
			importeSellado = reciboConvenio.getImporteSellado();
		
		// Si el Importe Pagado es inferior al Importe del Recibo menos una valor fijo de tolerancia, registrar indeterminado.
		if(this.getImporte().doubleValue()<(sumImporteDetalle+importeSellado)-Tolerancia.VALOR_FIJO){//reciboConvenio.getTotImporteRecibo().doubleValue()-Tolerancia.VALOR_FIJO){
			this.registrarIndeterminado("Indeterminado por pago de menos en el Recibo de Cuota "+reciboConvenio.getNroRecibo()+"/"+reciboConvenio.getAnioRecibo()+".", reciboConvenio, "40");
			return;
		}
		// Si el Importe Pagado es hasta un valor fijo de tolerancia mayor al Importe del Recibo, Pago Bueno. Si es mayor a este valor, registrar indeterminado.
		if(this.getImporte().doubleValue()>(sumImporteDetalle+importeSellado)+Tolerancia.VALOR_FIJO){//reciboConvenio.getTotImporteRecibo().doubleValue()+Tolerancia.VALOR_FIJO){
			this.registrarIndeterminado("Indeterminado por pago de más en el Recibo de Cuota "+reciboConvenio.getNroRecibo()+"/"+reciboConvenio.getAnioRecibo()+".", reciboConvenio, "41");
			return;				
		}

		if(!NumberUtil.isDoubleEqualToDouble(reciboConvenio.getTotImporteRecibo(), sumImporteDetalle+importeSellado, 0.001)){
			AdpRun.currentRun().logDebug("Advertencia!: Transaccion de Id: "+this.getId()+", Linea Nro "+this.getNroLinea()+", El Importe del Recibo de Cuota no coincide con la sumatoria de los importes del detalle considerando el sellado. Importe Recibo: "+reciboConvenio.getTotImporteRecibo()+" Importe Sumado del Detalle(con sellado incluido): "+(sumImporteDetalle+importeSellado)+", Importe Pago: "+this.getImporte()+". Recibo de Cuota "+reciboConvenio.getNroRecibo()+"/"+reciboConvenio.getAnioRecibo());
			AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).incWarnings();
		}

		// Validaciones sobre el Convenio
		Convenio convenio = reciboConvenio.getConvenio();

		// Validar el Estado del Convenio
		if(convenio.getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_CANCELADO){
			this.registrarIndeterminado("Indeterminado por Convenio Cancelado.", reciboConvenio, "33");
			return;																								
		}
		if(convenio.getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_RECOMPUESTO){
			this.registrarIndeterminado("Indeterminado por Convenio Recompuesto.", reciboConvenio, "34");
			// Cargar en lista para envio de Solicitud de Revision de Caso a Balance.
			this.getAsentamiento().logDetallado("	Se Carga en lista para envio de Solicitud de Revision de Caso a Balance.");
			TipoSolicitud tipoSolicitud = null;
			tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_VERIFICAR_CONVENIO_RECOMPUESTO);
			AuxConvenio auxConvenio = this.getAsentamiento().getAuxConvenio(convenio, tipoSolicitud);
			if(auxConvenio == null){
				auxConvenio = new AuxConvenio();
				auxConvenio.setAsentamiento(this.getAsentamiento());
				auxConvenio.setConvenio(convenio);
				auxConvenio.setTipoSolicitud(tipoSolicitud);
				this.getAsentamiento().createAuxConvenio(auxConvenio);					
			}
			return;																								
		}
			
		// Validar si el Convenio se encuentra en estado Caduco.
		boolean procesarPagoBueno = false;
		if(convenioConsistente){
			// Si el Plan No Aplica Pago a Cuenta.
			if(convenio.getPlan().getAplicaPagCue().intValue() == SiNo.NO.getId().intValue()){
				procesarPagoBueno = true;
				this.getAsentamiento().logDetallado("	El Plan no aplica Pago a Cuenta");
			}else{
			// Si el Plan Aplica Pago a Cuenta.
				int estaCaduco = convenio.estaCaduco(this.getFechaPago(), 
						AsentamientoCache.getInstance().getListRescateByIdPlan(convenio.getPlan().getId()), 
						AsentamientoCache.getInstance().getListPlanMotCadByIdPlan(convenio.getPlan().getId()),
						AsentamientoCache.getInstance().getMapFeriado());
				if(estaCaduco == 2)
					this.getAsentamiento().logDetallado("	El Convenio esta Caduco");
				else if(estaCaduco != 2)
					this.getAsentamiento().logDetallado("	El Convenio esta Vigente");
				if(estaCaduco == 1)//tienePagoCue)
					this.getAsentamiento().logDetallado("	El Convenio tiene Pagos a Cuenta");
				else if(estaCaduco == 0)
					this.getAsentamiento().logDetallado("	El Convenio no tiene Pagos a Cuenta");
				
				if(convenio.getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_VIGENTE
						&& estaCaduco == 0){
					// Asentar Cuota como Pago Bueno.
					procesarPagoBueno = true;
				}
				if(estaCaduco == 1){
					// Cargar en lista para envio de Solicitud de Revision de Caso a Balance.
					this.getAsentamiento().logDetallado("	Se cargar en lista para envio de Solicitud de Revision de Caso a Balance.");
					TipoSolicitud tipoSolicitud = null;
					if(convenio.getViaDeuda().getId().longValue()==ViaDeuda.ID_VIA_ADMIN){
						tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_RESCATE_CONVENIO_VIA_ADM);
					}else if(convenio.getViaDeuda().getId().longValue()==ViaDeuda.ID_VIA_JUDICIAL){
						tipoSolicitud = TipoSolicitud.getByCodigo(TipoSolicitud.COD_RESCATE_CONVENIO_VIA_JUD);
					}					
					AuxConvenio auxConvenio = this.getAsentamiento().getAuxConvenio(convenio, tipoSolicitud);
					if(auxConvenio == null){
						auxConvenio = new AuxConvenio();
						auxConvenio.setAsentamiento(this.getAsentamiento());
						auxConvenio.setConvenio(convenio);
						auxConvenio.setTipoSolicitud(tipoSolicitud);
						this.getAsentamiento().createAuxConvenio(auxConvenio);					
					}
					// Asentar Cuota como Pago a Cuenta
					procesarPagoBueno = false;
				}
				if(estaCaduco == 2){
					procesarPagoBueno = false; 
				}						
			}
		}
		
		// Se procesa cada Cuota incluida en el recibo.
		for(RecConCuo recConCuo: listRecConCuo){
			this.getAsentamiento().logDetallado("	Procesando Cuota de Recibo: "+recConCuo.getConvenioCuota().getNumeroCuota());
			this.procesarCuotaDeRecibo(recConCuo, procesarPagoBueno, convenioConsistente);
			if(this.hasError()){
				return;
			}
		}
		// Se guarda el recibo en el mapa de indices.
		this.getAsentamiento().addAuxPagRecConToIndex(reciboConvenio.getId());
				
		//-> Si es un Asentamiento Comun 
		if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){
			Sellado sellado = reciboConvenio.getSellado();
			this.getAsentamiento().logDetallado("	Distribuir Sellado... ");
			
			if(sellado == null){
				this.getAsentamiento().logDetallado("	No hay sellado en el Recibo de Cuota "+reciboConvenio.getNroRecibo()+"/"+reciboConvenio.getAnioRecibo());
				return;											
			}
			List<ParSel> listParSel = sellado.getListParSel();
			Double importeSelladoSinMontoFijo = reciboConvenio.getImporteSellado();
			for(ParSel parSel: listParSel){
				if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO){
					importeSelladoSinMontoFijo -= parSel.getMonto();
				}									
			}
			for(ParSel parSel: listParSel){
				AuxSellado auxSellado = this.getAsentamiento().getAuxSellado(this, parSel);
				if(auxSellado != null){
					if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO){
						auxSellado.setImporteFijo(auxSellado.getImporteFijo()+parSel.getMonto());
						this.getAsentamiento().logDetallado("	Acumula Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Monto Fijo: "+parSel.getMonto()+", Importe Acumulado: "+auxSellado.getImporteFijo());
					}					
					if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_PORCENTAJE){
						auxSellado.setImporteEjeAct(auxSellado.getImporteEjeAct()+(importeSelladoSinMontoFijo));
						this.getAsentamiento().logDetallado("	Acumula Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Sin Monto Fijo: "+importeSelladoSinMontoFijo+", Importe Acumulado: "+auxSellado.getImporteEjeAct()+", Porcentaje: "+parSel.getMonto());
					}
				}else{
					auxSellado = new AuxSellado();
					auxSellado.setAsentamiento(this.getAsentamiento());
					Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), 
			                 this.getSistema());
					auxSellado.setSistema(sistema);
					auxSellado.setSellado(sellado);
					auxSellado.setFechaPago(this.getFechaPago());
					auxSellado.setPartida(parSel.getPartida());
					auxSellado.setImporteEjeAct(0D);
					auxSellado.setImporteEjeVen(0D);
					auxSellado.setImporteFijo(0D);
					if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO){
						auxSellado.setEsImporteFijo(SiNo.SI.getId());
						auxSellado.setImporteFijo(parSel.getMonto());
						auxSellado.setPorcentaje(0D);
						this.getAsentamiento().logDetallado("	Crea Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Monto Fijo: "+parSel.getMonto()+", Importe Acumulado: "+auxSellado.getImporteFijo());
					}
					if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_PORCENTAJE){
						auxSellado.setEsImporteFijo(SiNo.NO.getId());
						auxSellado.setImporteEjeAct((importeSelladoSinMontoFijo));
						auxSellado.setPorcentaje(parSel.getMonto());
						this.getAsentamiento().logDetallado("	Crea Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Sin Monto Fijo: "+importeSelladoSinMontoFijo+", Importe Acumulado: "+auxSellado.getImporteEjeAct()+", Porcentaje: "+parSel.getMonto());
					}
					this.getAsentamiento().createAuxSellado(auxSellado);
				}
			}		
			
			//Obtiene el Distribuidor para Redondeo
			this.getAsentamiento().logDetallado("	Buscando Distribuidor de Redondeo.");
			Recurso recurso = convenio.getRecurso();
			Plan plan = convenio.getPlan();
			String atrVal = null;
			if(recurso.getAtributoAse() != null)
				atrVal = convenio.getCuenta().getValorAtributo(recurso.getAtributoAse().getId(), convenio.getFechaFor());
			DisParPla disParPla = null;
			DisPar disPar = null;
			List<DisParPla> listDisParPla =  AsentamientoCache.getInstance().getListByPlanRecValAtrAse(plan, recurso,  atrVal);
			
			if (listDisParPla.size()==0 ) {
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro Distribuidor de partidas para: idPlan: " + plan.getId() + 
						" valorAtributoAsentamiento: " + atrVal);
				return;					
			} else if (listDisParPla.size()>1) {
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Se encontro mas de un Distribuidor de partidas para: idPlan: " + plan.getId() + 
						" valorAtributoAsentamiento: " + atrVal);
				return;
			} else {
				// Toma el DisParPla
				disParPla = listDisParPla.get(0);
			}
			disPar = disParPla.getDisPar();
						
			Double redondeo = this.getImporte().doubleValue() - reciboConvenio.getTotImporteRecibo().doubleValue();
						
			List<DisParDet> listDisParDet = disPar.getListDisParDetByidTipoImporteYRecCon(TipoImporte.ID_REDONDEO, null);
			if(ListUtil.isNullOrEmpty(listDisParDet)){
				String message = "";
				message += "Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No existe el Detalle del Distribuidor de Partidas asociado al Recurso "+disPar.getRecurso().getDesRecurso();
				message +=", Distribuidor de id: "+disPar.getId();
				message +="y Tipo de Importe Redondeo.";
				this.addRecoverableValueError(message);
				return;
			}

			for(DisParDet disParDet: listDisParDet){				
				AuxRecaudado auxRecaudado = this.getAsentamiento().getAuxRecaudado(this, disParDet.getPartida(), disParDet.getPorcentaje(), null, null, this.getTipoBoleta());			
				// Si se encontro un registro
				if(auxRecaudado != null){
					// Actualizar el importe acumulando el Importe Pago 
					auxRecaudado.setImporteEjeAct(auxRecaudado.getImporteEjeAct()+redondeo);
					// Si no se encontro un registro
				}else{
					Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), this.getSistema());
					// Insertar registro en AuxRecaudado
					auxRecaudado = new AuxRecaudado();
					auxRecaudado.setAsentamiento(this.getAsentamiento());
					auxRecaudado.setSistema(sistema);
					auxRecaudado.setFechaPago(this.getFechaPago());
					auxRecaudado.setPartida(disParDet.getPartida());
					auxRecaudado.setViaDeuda(null);
					auxRecaudado.setPlan(null);
					auxRecaudado.setTipoBoleta(this.getTipoBoleta());
					auxRecaudado.setImporteEjeAct(redondeo);
					auxRecaudado.setImporteEjeVen(0D);
					auxRecaudado.setPorcentaje(disParDet.getPorcentaje());
					this.getAsentamiento().createAuxRecaudado(auxRecaudado);
					Double paraLog = 0D;
					if(AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).isLogDetalladoEnabled())
						paraLog = auxRecaudado.getImporteEjeAct()+auxRecaudado.getImporteEjeVen();
					this.getAsentamiento().logDetallado("		AuxRecaudado, Partida: "+disParDet.getPartida().getDesPartida()+" Importe Agregado: "+redondeo+" Importe Acumulado: "+paraLog+" Porcentaje: "+auxRecaudado.getPorcentaje()*100+" %");
				}
			
			}
		}

		this.getAsentamiento().logDetallado("Saliendo de Procesar Recibo de Cuota...");
	}

	/**
	 * Procesar Cuota que Proviene de un Recibo (Generado en Siat).
	 * 	
	 * <i>(paso 2.1.c)</i>
	 */
	public void procesarCuotaDeRecibo(RecConCuo recConCuo, boolean procesarPagoBueno, boolean convenioConsistente) throws Exception{		
		this.getAsentamiento().logDetallado("	Entrando a Procesar Cuota de Recibo...");
		ConvenioCuota convenioCuota = recConCuo.getConvenioCuota();
		this.getAsentamiento().logDetallado("		Se arma Transaccion Desglozada para la Cuota del Recibo.");
		// Armamos una transaccion (desgloce) para indeterminar esta cuota del recibo y no el recibo entero. 
		Transaccion transaccionDesgloce = new Transaccion();
		if(convenioCuota.getSistema().getEsServicioBanco().intValue() == SiNo.SI.getId().intValue()){
			transaccionDesgloce.setSistema(convenioCuota.getSistema().getNroSistema());
			transaccionDesgloce.setNroComprobante(new Long(convenioCuota.getCodRefPag()));
			transaccionDesgloce.setAnioComprobante(Transaccion.TIPO_BOLETA_CUOTA);
			transaccionDesgloce.setPeriodo(0L);						
		}else{
			transaccionDesgloce.setSistema(convenioCuota.getSistema().getNroSistema());
			transaccionDesgloce.setNroComprobante(new Long(convenioCuota.getConvenio().getNroConvenio().longValue()));
			transaccionDesgloce.setAnioComprobante(0L);
			transaccionDesgloce.setPeriodo(new Long(convenioCuota.getNumeroCuota().longValue()));			
		}
		transaccionDesgloce.setResto(0L);
		transaccionDesgloce.setCodPago(this.getCodPago());
		transaccionDesgloce.setCaja(this.getCaja());
		transaccionDesgloce.setCodTr(this.getCodTr());
		transaccionDesgloce.setFechaPago(this.getFechaPago());
		transaccionDesgloce.setImporte(recConCuo.getTotCapitalOriginal()+recConCuo.getTotActualizacion()+recConCuo.getTotIntFin()+recConCuo.getImporteSellado());
		transaccionDesgloce.setRecargo(0D);
		transaccionDesgloce.setPaquete(this.getPaquete());
		transaccionDesgloce.setMarcaTr(this.getMarcaTr());
		transaccionDesgloce.setReciboTr(recConCuo.getReciboConvenio().getNroRecibo());
		transaccionDesgloce.setFechaBalance(this.getFechaBalance()); 
		transaccionDesgloce.setAsentamiento(this.getAsentamiento());
		transaccionDesgloce.setEsDesgloce(SiNo.SI.getId());
		transaccionDesgloce.setNroLinea(this.getNroLinea());
		transaccionDesgloce.setTipoBoleta(this.getTipoBoleta());

		// Indeterminar por Convenio Inconsistente
		if(!convenioConsistente){
			transaccionDesgloce.registrarIndeterminado("Indeterminado por Convenio Inconsistente: La Deuda incluida en el Convenio no se encuentra en el estado que corresponde.", convenioCuota,null, "51");
			transaccionDesgloce.passErrorMessages(this);
			return;
		}
		// Validar Estado de la Cuota
		if(convenioCuota.getEstadoConCuo().getId().longValue() != EstadoConCuo.ID_IMPAGO){
			transaccionDesgloce.registrarIndeterminado("Indeterminado por Cuota Cancelada: Pago Dúplice.", convenioCuota,null, "33");
			transaccionDesgloce.passErrorMessages(this);
			return;																								
		}
		// Validar si la cuota ya fue procesada en el mismo Asentamiento.
		if(this.getAsentamiento().existAuxPagCuo(convenioCuota.getId())){
			transaccionDesgloce.registrarIndeterminado("Indeterminado por Cuota Cancelada: Pago Dúplice en el mismo Asentamiento.", convenioCuota,null,"31");
			transaccionDesgloce.passErrorMessages(this);
			return;																		
		}

		// Procesar Cuota como Pago Bueno o Pago A Cuenta según se decidió al analizar el Convenio en el procesamiento del Recibo 
		if(procesarPagoBueno){
			this.procesarCuotaDeReciboPagoBueno(recConCuo);
		}else{
			this.procesarCuotaDeReciboPagoACuenta(recConCuo);
		}
		Double importeDistribuido = this.getImporteParaValidacion()+recConCuo.getImporteSellado();
		if(!NumberUtil.isDoubleEqualToDouble(importeDistribuido,transaccionDesgloce.getImporte(),0.01)){
			AdpRun.currentRun().logDebug("Advertencia!: Transaccion Desglosada a partir de la correspondiente de Id: "+this.getId()+", Linea Nro "+this.getNroLinea()+" Importe: "+transaccionDesgloce.getImporte()+" Importe Distribuido: "+importeDistribuido+" Importe de la Transaccion Original: "+this.getImporte());
			AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).incWarnings();
		}

		//-> Si la Cuota Tiene Sellado, se distribuye.
		if(convenioCuota.getSellado() != null){
			if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){
				Sellado sellado = convenioCuota.getSellado();
				this.getAsentamiento().logDetallado("	Distribuir Sellado... ");
				
				if(sellado == null){
					this.getAsentamiento().logDetallado("	No posee sellado.");
					return;											
				}
				List<ParSel> listParSel = sellado.getListParSel();
				Double importeSelladoSinMontoFijo = convenioCuota.getImporteSellado();
				for(ParSel parSel: listParSel){
					if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO){
						importeSelladoSinMontoFijo -= parSel.getMonto();
					}									
				}
				for(ParSel parSel: listParSel){
					AuxSellado auxSellado = this.getAsentamiento().getAuxSellado(this, parSel);
					if(auxSellado != null){
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO){
							auxSellado.setImporteFijo(auxSellado.getImporteFijo()+parSel.getMonto());
							this.getAsentamiento().logDetallado("	Acumula Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Monto Fijo: "+parSel.getMonto()+", Importe Acumulado: "+auxSellado.getImporteFijo());
						}					
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_PORCENTAJE){
							auxSellado.setImporteEjeAct(auxSellado.getImporteEjeAct()+(importeSelladoSinMontoFijo));
							this.getAsentamiento().logDetallado("	Acumula Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Sin Monto Fijo: "+importeSelladoSinMontoFijo+", Importe Acumulado: "+auxSellado.getImporteEjeAct()+", Porcentaje: "+parSel.getMonto());
						}
					}else{
						auxSellado = new AuxSellado();
						auxSellado.setAsentamiento(this.getAsentamiento());
						Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), 
				                 this.getSistema());
						auxSellado.setSistema(sistema);
						auxSellado.setSellado(sellado);
						auxSellado.setFechaPago(this.getFechaPago());
						auxSellado.setPartida(parSel.getPartida());
						auxSellado.setImporteEjeAct(0D);
						auxSellado.setImporteEjeVen(0D);
						auxSellado.setImporteFijo(0D);
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO){
							auxSellado.setEsImporteFijo(SiNo.SI.getId());
							auxSellado.setImporteFijo(parSel.getMonto());
							auxSellado.setPorcentaje(0D);
							this.getAsentamiento().logDetallado("	Crea Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Monto Fijo: "+parSel.getMonto()+", Importe Acumulado: "+auxSellado.getImporteFijo());
						}
						if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_PORCENTAJE){
							auxSellado.setEsImporteFijo(SiNo.NO.getId());
							auxSellado.setImporteEjeAct((importeSelladoSinMontoFijo));
							auxSellado.setPorcentaje(parSel.getMonto());
							this.getAsentamiento().logDetallado("	Crea Partida Sellado: "+parSel.getPartida().getCodDesPartida()+", Importe Sin Monto Fijo: "+importeSelladoSinMontoFijo+", Importe Acumulado: "+auxSellado.getImporteEjeAct()+", Porcentaje: "+parSel.getMonto());
						}
						this.getAsentamiento().createAuxSellado(auxSellado);
					}
				}			
			}	
		}
		
		this.getAsentamiento().logDetallado("	Saliendo de Procesar Cuota de Recibo...");
	}
	
	/**
	 * Procesar Cuota como Pago A Cuenta.
	 * 	
	 * <i>(paso 2.1.g)</i>
	 */
	public void procesarCuotaDeReciboPagoACuenta(RecConCuo recConCuo) throws Exception{
		this.getAsentamiento().logDetallado("		Entrando a Procesar Cuota de Recibo como Pago a Cuenta...");
		ConvenioCuota convenioCuota = recConCuo.getConvenioCuota();
		AuxPagCuo auxPagCuo = new AuxPagCuo();
		auxPagCuo.setAsentamiento(this.getAsentamiento());
		auxPagCuo.setConvenioCuota(convenioCuota);
		auxPagCuo.setReciboConvenio(recConCuo.getReciboConvenio());
		auxPagCuo.setTransaccion(this);
		auxPagCuo.setEstadoConCuo(EstadoConCuo.getByIdNull(EstadoConCuo.ID_PAGO_A_CUENTA));
		auxPagCuo.setActualizacion(recConCuo.getTotActualizacion());
		auxPagCuo.setNroCuotaImputada(null);
		
		this.getAsentamiento().createAuxPagCuo(auxPagCuo);
		this.getAsentamiento().addAuxPagCuoToIndex(convenioCuota.getId());
		
		//-> Si es un Asentamiento Comun 
		if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){
			this.getAsentamiento().logDetallado("			Importe a Distribuir: "+(recConCuo.getTotCapitalOriginal()+recConCuo.getTotActualizacion()+recConCuo.getTotIntFin()));
			// Obtener Distribuidor de Partidas Vigentes asociadas al Plan del Convenio de la Cuota.
			Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), 
	                 this.getSistema());
			if(sistema == null){
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro el sistema de la transaccion." );
				return;
			}		
			Plan plan = convenioCuota.getConvenio().getPlan();
			Recurso recurso = AsentamientoCache.getInstance().getRecursoById(convenioCuota.getConvenio().getRecurso().getId());
			DisParPla disParPla=null;
			String atrAseVal = null; 
			if(recurso.getAtributoAse() != null){
				atrAseVal = convenioCuota.getConvenio().getValorAtributo();
				if(atrAseVal == null)
					atrAseVal = convenioCuota.getConvenio().getCuenta().getValorAtributo(recurso.getAtributoAse().getId(), convenioCuota.getConvenio().getFechaFor());
				this.getAsentamiento().logDetallado("			Valor de Atributo de Asentamiento de la Cuenta para Fecha de Formalizacion del Plan: "+atrAseVal);				
			}

			// Recupera el DisParPla
			List<DisParPla> listDisParPla =  AsentamientoCache.getInstance().getListByPlanRecValAtrAse(plan, recurso, atrAseVal);
			if (listDisParPla.size()==0 ) {
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro Distribuidor de partidas para: idPlan: " + plan.getId() + 
						" valorAtributoAsentamiento: " + atrAseVal);
				return;					
			} else if (listDisParPla.size()>1) {
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Se encontro mas de un Distribuidor de partidas para: idPlan: " + plan.getId() + 
						" valorAtributoAsentamiento: " + atrAseVal);
				return;
			} else {
				// Toma el DisParPla
				disParPla = listDisParPla.get(0);
			}
			DisPar disPar = disParPla.getDisPar();

			// Obtener los Detalles del Distribuidor de Partida para el tipo importe Pago a Cuenta.
			this.getAsentamiento().logDetallado("			Distribuidor encontrado. Buscando Detalle para el Tipo de Importe Pago a Cuenta.");
			
			List<DisParDet> listDisParDet = disPar.getListDisParDetByidTipoImporteYRecCon(TipoImporte.ID_PAGO_A_CUENTA, null);
			if(ListUtil.isNullOrEmpty(listDisParDet)){
				String message = "";
				message += "Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No existe el Detalle del Distribuidor de Partidas asociado al Plan "+plan.getDesPlan();
				message +=", Distribuidor de id: "+disPar.getId();
				message +=", Tipo de Importe Pago A Cuenta." ;
				this.addRecoverableValueError(message);
				return;
			}
			// Determinar si corresponde a Ejercicio Actual o Vencido, validando si la fecha de vencimiento y la fecha de 
			// pago corresponden al mismo ejercicio.
			boolean esActual = false;
			Date fechaVencimiento = convenioCuota.getFechaVencimiento();
			if(DateUtil.isDateAfterOrEqual(fechaVencimiento,this.getAsentamiento().getEjercicio().getFecIniEje()) 
					&& DateUtil.isDateBeforeOrEqual(fechaVencimiento, this.getAsentamiento().getEjercicio().getFecFinEje())
					&& DateUtil.isDateAfterOrEqual(this.getFechaPago(),this.getAsentamiento().getEjercicio().getFecIniEje())
					&& DateUtil.isDateBeforeOrEqual(this.getFechaPago(), this.getAsentamiento().getEjercicio().getFecFinEje())){
						esActual = true;
			}	
			
			// Por cada detalle:
			for(DisParDet disParDet: listDisParDet){
				AuxRecaudado auxRecaudado = this.getAsentamiento().getAuxRecaudado(this, disParDet.getPartida(), disParDet.getPorcentaje(), null, convenioCuota.getConvenio().getPlan(), Transaccion.TIPO_BOLETA_RECIBO_CUOTA);
				Double importe = recConCuo.getTotCapitalOriginal()+recConCuo.getTotActualizacion()+recConCuo.getTotIntFin(); 
				// Si se encontro un registro 
				if(auxRecaudado != null){
					// Actualizar el importe acumulando el Importe Pago 
					if(esActual)
						auxRecaudado.setImporteEjeAct(auxRecaudado.getImporteEjeAct()+importe);
					else
						auxRecaudado.setImporteEjeVen(auxRecaudado.getImporteEjeVen()+importe);
					
				// Si no se encontro un registro
				}else{
					// Insertar registro en AuxRecaudado
					auxRecaudado = new AuxRecaudado();
					auxRecaudado.setAsentamiento(this.getAsentamiento());
					auxRecaudado.setSistema(sistema);
					auxRecaudado.setFechaPago(this.getFechaPago());
					auxRecaudado.setPartida(disParDet.getPartida());
					auxRecaudado.setViaDeuda(null);
					auxRecaudado.setTipoBoleta(Transaccion.TIPO_BOLETA_RECIBO_CUOTA);
					auxRecaudado.setPlan(convenioCuota.getConvenio().getPlan());
					if(esActual){
						auxRecaudado.setImporteEjeAct(importe);
						auxRecaudado.setImporteEjeVen(0D);
					}else{
						auxRecaudado.setImporteEjeAct(0D);
						auxRecaudado.setImporteEjeVen(importe);	
					}
					auxRecaudado.setPorcentaje(disParDet.getPorcentaje());
					this.getAsentamiento().createAuxRecaudado(auxRecaudado);
				}
				Double paraLog = 0D;
				if(AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).isLogDetalladoEnabled())
					paraLog = auxRecaudado.getImporteEjeAct()+auxRecaudado.getImporteEjeVen();
				this.getAsentamiento().logDetallado("			AuxRecaudado, Partida: "+disParDet.getPartida().getDesPartida()+" Importe Agregado: "+importe+" Importe Acumulado: "+paraLog+" Porcentaje: "+auxRecaudado.getPorcentaje()*100+" %");
			}
			Double importeDistribuido = recConCuo.getTotCapitalOriginal()+recConCuo.getTotActualizacion()+recConCuo.getTotIntFin();
			this.setImporteParaValidacion(importeDistribuido);

		}
		this.getAsentamiento().logDetallado("		Saliendo de Procesar Cuota de Recibo como Pago a Cuenta...");
	}
	
	/**
	 * Procesar Cuota como Pago Bueno.
	 * 	
	 * <i>(paso 2.1.f)</i>
	 */
	public void procesarCuotaDeReciboPagoBueno(RecConCuo recConCuo) throws Exception{
		this.getAsentamiento().logDetallado("		Entrando a Procesar Cuota de Recibo como Pago Bueno...");
		ConvenioCuota convenioCuota = recConCuo.getConvenioCuota();
		// Obtener el Numero de Cuota a Imputar el Pago.
		Integer nroCuotaAImputar = 0;
		if(convenioCuota.getNumeroCuota().longValue() == 1L){
			nroCuotaAImputar = 1;
		}else{			
			Integer ultimaCuotaPaga = convenioCuota.getConvenio().getUltCuoImp();
			Integer cantCuotasAsen = this.getAsentamiento().getCuotaCounter(convenioCuota.getConvenio().getId());
			this.getAsentamiento().logDetallado("			Cant. de Cuotas Previamente Asentadas: "+cantCuotasAsen);
				if(ultimaCuotaPaga == 0)
					ultimaCuotaPaga++;
				nroCuotaAImputar = ultimaCuotaPaga + cantCuotasAsen;
				nroCuotaAImputar++;
		}
		this.getAsentamiento().logDetallado("			Nro de Cuota a Imputar: "+nroCuotaAImputar);
		// Obtener el "Capital de la Cuota a asentar" e "Interes Financiero de la Cuota a asentar"
		Double capitalCuotaAAsentar = 0D;
		Double interesCuotaAAsentar = 0D;
		ConvenioCuota convenioCuotaAAsentar=null;
		// Si el recibo no es cuota saldo obtengo 
		if (recConCuo.getReciboConvenio().getEsCuotaSaldo()== SiNo.NO.getId().intValue() && nroCuotaAImputar.intValue() != convenioCuota.getNumeroCuota().intValue()){
			convenioCuotaAAsentar = ConvenioCuota.getByNroCuoIdCon(new Long(nroCuotaAImputar), convenioCuota.getConvenio().getId());
		}else{
			convenioCuotaAAsentar = convenioCuota;
		}
		if(convenioCuotaAAsentar == null){
			convenioCuotaAAsentar = ConvenioCuota.getPrimImpByIdConvenio(convenioCuota.getConvenio().getId());
			if (convenioCuotaAAsentar == null || convenioCuotaAAsentar.getImporteCuota().doubleValue() != convenioCuota.getImporteCuota().doubleValue()){
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encuentra la Cuota en la DB. Cuota: "+nroCuotaAImputar+", Convenio: "+convenioCuota.getConvenio().getNroConvenio()+", Nro Sistema: "+convenioCuota.getSistema().getNroSistema());
				return;
			}else{
				nroCuotaAImputar = convenioCuotaAAsentar.getNumeroCuota();
			}
		}
		capitalCuotaAAsentar = convenioCuotaAAsentar.getCapitalCuota();
		if(recConCuo.getReciboConvenio().getEsCuotaSaldo().intValue() == SiNo.NO.getId().intValue()){
			interesCuotaAAsentar = convenioCuotaAAsentar.getInteres();			
		}else{
			interesCuotaAAsentar = recConCuo.getTotIntFin();
		}
		this.getAsentamiento().logDetallado("			Capital de Cuota a Asentar: "+capitalCuotaAAsentar+" Interes de Cuota a Asentar: "+interesCuotaAAsentar);
		
		// Obtener el "Capital", "Interes Financiero" y "Actualizacion"  de la cuota a distribuir
		Double capitalADistribuir = capitalCuotaAAsentar;
		Double interesADistribuir = interesCuotaAAsentar+recConCuo.getTotActualizacion();

		this.getAsentamiento().logDetallado("			Capital a Distribuir: "+capitalADistribuir);
		this.getAsentamiento().logDetallado("			Interes a Distribuir: "+interesCuotaAAsentar);
		this.getAsentamiento().logDetallado("			Actualizacion a Distribuir: "+recConCuo.getTotActualizacion());
		this.getAsentamiento().logDetallado("			(la actualizacion se distribuye junto al interes");

		// Acumuladores para Verificacion de Importe Distribuido
		Double capitalDistribuido = 0D;
		Double actualizacionDistribuido = 0D;
		Double interesDistribuido = 0D;

		//. buscar pila en mapStackConDeu, si no lo encuentra, tira el query para obtener la lista de convenioDeuda
		//  con saldo mayor a cero y la carga en el mapa para el idConvenio.
		if(!this.getAsentamiento().existStackConDeu(convenioCuota.getConvenio().getId())){
			//. buscar lista de convenioDeuda con saldo mayor a cero
			List<ConvenioDeuda> listConvenioDeuda = ConvenioDeuda.getListWithSaldo(convenioCuota.getConvenio());
			this.getAsentamiento().putStackConDeu(convenioCuota.getConvenio().getId(), listConvenioDeuda);
		}
		
		//-> Mientras el Capital de la cuota a asentar sea mayor a cero.
		Double capitalTotalCuotaAAsentar = capitalCuotaAAsentar; 
		this.getAsentamiento().logDetallado("			Entrando al bucle con Capital de Cuota a Asentar: "+capitalCuotaAAsentar);
		while(capitalCuotaAAsentar>0){
			// Obtener la Deuda a Cancelar
	
			//. obtiene el auxConDeu con saldo mayor a cero del mapa de session
			AuxConDeu auxConDeu = this.getAsentamiento().getAuxConDeuConSaldoMayorCero(convenioCuota.getConvenio().getId());
			//AuxConDeu auxConDeu = AuxConDeu.getDeudaACancelarByAsentamientoYConvenio(this.getAsentamiento(), convenioCuota.getConvenio());
			
			ConvenioDeuda convenioDeuda = null;
			Double saldoEnPlan = 0D;
			if(auxConDeu !=  null){
				convenioDeuda = auxConDeu.getConvenioDeuda();
				saldoEnPlan = auxConDeu.getSaldoEnPlan();
			}else{
				
				//. obtener el convenioDeuda de la pila de convenioDeuda con saldo mayor a cero guardada en el mapStackConDeu
				convenioDeuda = this.getAsentamiento().popFromStackConDeu(convenioCuota.getConvenio().getId());				
				//convenioDeuda = ConvenioDeuda.getDeudaACancelarByConvenioYAse(convenioCuota.getConvenio(),this.getAsentamiento());

				if(convenioDeuda == null && capitalCuotaAAsentar<0.5){
					capitalCuotaAAsentar = 0D;
					break;
				}
				if(convenioDeuda == null && capitalCuotaAAsentar>=0.5){
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encuentra Deuda relacionada al Convenio para cancelar. Cuota: "+nroCuotaAImputar+", Convenio: "+convenioCuota.getConvenio().getNroConvenio()+", Nro Sistema: "+convenioCuota.getSistema().getNroSistema()+", Capital de Cuota A Asentar restante: "+capitalCuotaAAsentar);
					return;
				}
				saldoEnPlan = convenioDeuda.getSaldoEnPlan();
			}
			this.getAsentamiento().logDetallado("				Deuda relacionada al Convenio para Cancelar con id "+convenioDeuda.getIdDeuda()+", Saldo en Plan: "+saldoEnPlan);
			// Determinar el Saldo de la Deuda obtenida que cubre el Capital de la Cuota a Asentar
			Double saldoEnPlanCub = 0D;
			if(capitalCuotaAAsentar.doubleValue() > saldoEnPlan.doubleValue()){
				saldoEnPlanCub = saldoEnPlan;
				this.getAsentamiento().logDetallado("				Capital de Cuota a Asentar: "+capitalCuotaAAsentar+" > Saldo en Plan: "+saldoEnPlan+"............Saldo en Plan Cubierto: "+saldoEnPlanCub);								
			}else{
				saldoEnPlanCub = capitalCuotaAAsentar; // En la especificacion decia: saldoEnPlan - capitalCuotaAAsentar;
				this.getAsentamiento().logDetallado("				Capital de Cuota a Asentar: "+capitalCuotaAAsentar+" < Saldo en Plan: "+saldoEnPlan+"............Saldo en Plan Cubierto: "+saldoEnPlanCub);								
			}
			// Insertar/Actualizar registro en AuxConDeu.
			if(auxConDeu != null){
				auxConDeu.setSaldoEnPlan(saldoEnPlan - saldoEnPlanCub);
				auxConDeu.setFechaPago(this.getFechaPago());
				auxConDeu.setTransaccion(this); 
				this.getAsentamiento().updateAuxConDeu(auxConDeu);
			}else{
				auxConDeu = new AuxConDeu();
				auxConDeu.setAsentamiento(this.getAsentamiento());
				auxConDeu.setConvenioDeuda(convenioDeuda);
				auxConDeu.setSaldoEnPlan(saldoEnPlan - saldoEnPlanCub);
				auxConDeu.setFechaPago(this.getFechaPago());
				auxConDeu.setTransaccion(this);
				this.getAsentamiento().createAuxConDeu(auxConDeu);
			}
			// Grabar la relacion de la deuda y la cuota en AuxConDeuCuo
			AuxConDeuCuo auxConDeuCuo = new AuxConDeuCuo();
			auxConDeuCuo.setAsentamiento(this.getAsentamiento());
			auxConDeuCuo.setConvenioCuota(convenioCuota);
			auxConDeuCuo.setConvenioDeuda(convenioDeuda);
			auxConDeuCuo.setSaldoEnPlanCub(saldoEnPlanCub);
			Integer esPagoTotal = SiNo.NO.getId();
			if(convenioDeuda.getDeuda().getImporte().doubleValue()==saldoEnPlanCub)
				esPagoTotal = SiNo.SI.getId();
			auxConDeuCuo.setEsPagoTotal(esPagoTotal);
			auxConDeuCuo.setTransaccion(this); 
			this.getAsentamiento().createAuxConDeuCuo(auxConDeuCuo);
						
			//-> Si es un Asentamiento Comun 
			if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){
				// Obtener Distribuidor de Partidas Vigentes asociadas al Plan del Convenio de la Cuota.
				Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), 
		                 this.getSistema());
				if(sistema == null){
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro el sistema de la transaccion." );
					return;
				}		
				
				Plan plan = convenioCuota.getConvenio().getPlan();
				Recurso recurso = AsentamientoCache.getInstance().getRecursoById(convenioCuota.getConvenio().getRecurso().getId());
				DisParPla disParPla=null;
				String valorAtrVig = null; 
				if(recurso.getAtributoAse() != null){
					valorAtrVig = convenioDeuda.getDeuda().getAtrAseVal();
					this.getAsentamiento().logDetallado("				Valor de Atributo de Asentamiento de la Deuda: "+valorAtrVig);
				}
				// Recupera el DisParPla
				List<DisParPla> listDisParPla =  AsentamientoCache.getInstance().getListByPlanRecValAtrAse(plan, recurso,valorAtrVig);
				if (listDisParPla.size()==0 ) {
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro Distribuidor de partidas para: idPlan: " + plan.getId() + 
							" valorAtributoAsentamiento: " + valorAtrVig);
					return;					
				} else if (listDisParPla.size()>1) {
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Se encontro mas de un Distribuidor de partidas para: idPlan: " + plan.getId() + 
							" valorAtributoAsentamiento: " + valorAtrVig);
					return;
				} else {
					// Toma el DisParPla
					disParPla = listDisParPla.get(0);
				}
				DisPar disPar = disParPla.getDisPar();

				// Obtener los conceptos asociados a la Deuda en el convenio.
				List<DeuRecCon> listDeuRecCon = convenioDeuda.getDeuda().getListDeuRecConByString();
				this.getAsentamiento().logDetallado("				String con Lista de Conceptos de la Deuda: "+convenioDeuda.getDeuda().getStrConceptosProp());
				// Validar Total Conceptos con Importe, y logear Advertencia.
				if(!this.validaConceptos(listDeuRecCon, convenioDeuda.getDeuda().getImporte(), 0.01)){
					AdpRun.currentRun().logDebug("Advertencia!: Transaccion de Id: "+this.getId()+", Linea Nro "+this.getNroLinea()+", Se encontró diferencias entre el importe de la deuda y la suma de sus conceptos. Deuda de id: "+convenioDeuda.getDeuda().getId()+" Importe Deuda: "+convenioDeuda.getDeuda().getImporte()+" Lista de Conceptos: "+convenioDeuda.getDeuda().getStrConceptosProp());
					AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).incWarnings();
				}				

				Double capitalCubiertoADistribuir = (saldoEnPlanCub * capitalADistribuir/capitalTotalCuotaAAsentar)
				* (convenioDeuda.getCapitalEnPlan()/convenioDeuda.getTotalEnPlan());
				Double actualizacionCubiertoADistribuir = ((saldoEnPlanCub) * capitalADistribuir/capitalTotalCuotaAAsentar)
				* convenioDeuda.getActEnPlan()/convenioDeuda.getTotalEnPlan();
				Double interesFinancieroADistribuir = (capitalCubiertoADistribuir+actualizacionCubiertoADistribuir)
				* interesADistribuir/capitalADistribuir;
				this.getAsentamiento().logDetallado("				Capital Cubierto a Distribuir: "+capitalCubiertoADistribuir+", Actualizacion Cubierta a Distribuir: "+actualizacionCubiertoADistribuir+", Interes Financiero a Distribuir: "+interesFinancieroADistribuir);
				this.getAsentamiento().logDetallado("				Realizando Distribucion...");

				// Para cada concepto:
				for(DeuRecCon deuRecCon: listDeuRecCon){
					if(deuRecCon.getImporte().doubleValue() != 0){
						// Distribuir Capital:
						if(capitalCubiertoADistribuir!=0){
							this.distribuir(disPar, deuRecCon, capitalCubiertoADistribuir, TipoImporte.ID_CAPITAL, deuRecCon.getDeuda());
							if(this.hasError()){
								return;
							}						
						}
						// Distribuir Actualizacion:
						if(actualizacionCubiertoADistribuir!=0){
							this.distribuir(disPar, deuRecCon, actualizacionCubiertoADistribuir, TipoImporte.ID_ACTUALIZACION, deuRecCon.getDeuda());
							if(this.hasError()){
								return;
							}
						}
						// Distribuir Interes Financiero:						
						if(interesFinancieroADistribuir!=0){
							this.distribuir(disPar, deuRecCon, interesFinancieroADistribuir, TipoImporte.ID_INTERES_FINANCIERO, deuRecCon.getDeuda());
							if(this.hasError()){
								return;
							}						
						}	
					}
				}
				// Acumulo los importes Distribuidos para control y logs.
				capitalDistribuido += capitalCubiertoADistribuir;
				actualizacionDistribuido += actualizacionCubiertoADistribuir;
				interesDistribuido += interesFinancieroADistribuir;
			}
			// Descontar del Capital de la Cuota a procesar el SaldoPlanCub
			capitalCuotaAAsentar = capitalCuotaAAsentar - saldoEnPlanCub;
			this.getAsentamiento().logDetallado("				Capital de Cuota a Asentar-Saldo en Plan Cubierto: "+capitalCuotaAAsentar);
			this.getAsentamiento().logDetallado("				Saldo en Plan Cubierto: "+saldoEnPlanCub);
		}
		Double importeDistribuido = capitalDistribuido+actualizacionDistribuido+interesDistribuido;
		this.setImporteParaValidacion(importeDistribuido);

		//-> Registrar el Pago Bueno en AuxPagCuo.
		AuxPagCuo auxPagCuo = new AuxPagCuo();
		auxPagCuo.setAsentamiento(this.getAsentamiento());
		auxPagCuo.setConvenioCuota(convenioCuota);
		auxPagCuo.setReciboConvenio(recConCuo.getReciboConvenio());
		auxPagCuo.setTransaccion(this);
		auxPagCuo.setEstadoConCuo(EstadoConCuo.getByIdNull(EstadoConCuo.ID_PAGO_BUENO));
		auxPagCuo.setActualizacion(recConCuo.getTotActualizacion()); 
		auxPagCuo.setNroCuotaImputada(nroCuotaAImputar);
		this.getAsentamiento().createAuxPagCuo(auxPagCuo);
		this.getAsentamiento().addAuxPagCuoToIndex(convenioCuota.getId());

		if(nroCuotaAImputar.intValue() != 1 && recConCuo.getReciboConvenio().getEsCuotaSaldo().intValue() == 0)
			this.getAsentamiento().incCuotaCounter(convenioCuota.getConvenio().getId());

		this.getAsentamiento().logDetallado("		Saliendo de Procesar Cuota de Recibo como Pago Bueno...");
	}

	/**
	 * Registrar Indeterminado. Tipo Boleta: Deuda
	 * 
	 * <i>(paso 2.1.e)</i>
	 */
	public void registrarIndeterminado(String message, Deuda deuda, DeudaAct deudaAct, String codTipoIndet) throws Exception{
		this.registrarIndeterminado(message,deuda,null, null,null,deudaAct,codTipoIndet);
	}
	
	/**
	 * Registrar Indetermindo. Tipo Boleta: Recibo de Deuda
	 * 
	 * <i>(paso 2.1.e)</i>
	 */
	public void registrarIndeterminado(String message, Recibo recibo, String codTipoIndet) throws Exception{
		this.registrarIndeterminado(message,null,recibo, null, null,null,codTipoIndet);
	}
	
	/**
	 * Registrar Indetermindo. Tipo Boleta: Cuota
	 * 
	 * <i>(paso 2.1.e)</i>
	 */
	public void registrarIndeterminado(String message, ConvenioCuota convenioCuota, DeudaAct cuotaAct, String codTipoIndet) throws Exception{
		this.registrarIndeterminado(message,null,null, convenioCuota,null,null,codTipoIndet);
	}
	
	/**
	 * Registrar Indetermindo. Tipo Boleta: Recibo de Cuota
	 * 
	 * <i>(paso 2.1.e)</i>
	 */
	public void registrarIndeterminado(String message, ReciboConvenio reciboConvenio, String codTipoIndet) throws Exception{
		this.registrarIndeterminado(message,null,null, null,reciboConvenio,null,codTipoIndet);
	}
	
	/**
	 * Registrar Indeterminado. Tipo Boleta: Sellado
	 * 
	 * <i>(paso 2.1.e)</i>
	 */
	public void registrarIndeterminado(String message, String codTipoIndet) throws Exception{
		this.registrarIndeterminado(message,null,null, null,null,null,codTipoIndet);
	}
	
	/**
	 * Registrar Indetermindo.
	 * 
	 * <i>(paso 2.1.e)</i>
	 */
	public void registrarIndeterminado(String message, Deuda deuda, Recibo recibo,ConvenioCuota convenioCuota, ReciboConvenio reciboConvenio, DeudaAct deudaAct, String codTipoIndet) throws Exception{

		long indTime = System.currentTimeMillis();
		
		AdpRun.currentRun().logDebug("Indeterminado!. Transaccion de Linea Id: "+this.getId()+", NroLinea:"+this.getNroLinea()+" . "+message);
		this.getAsentamiento().logDetallado("Entrando a Registrar Indeterminado...");
		TipoIndet tipoIndet = TipoIndet.getByCodigo(codTipoIndet);
		if(tipoIndet == null){
			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No existe el Tipo Indeterminado con Codigo "+codTipoIndet);
			return;
		}
		TipoImporte tipoImporte = null;
		if(tipoIndet.getCodTipoIndet().equals("12") || tipoIndet.getCodTipoIndet().equals("25") 
				|| tipoIndet.getCodTipoIndet().equals("32") || tipoIndet.getCodTipoIndet().equals("38") ){
	
			tipoImporte = TipoImporte.getByIdNull(TipoImporte.ID_DUPLICES);
			if(tipoImporte == null){
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No existe el Tipo Importe con id "+TipoImporte.ID_DUPLICES);;
				return;			
			}
		}else {
			tipoImporte = this.getAsentamiento().getTipoImporteIndet(); 
			if(tipoImporte == null){
				tipoImporte = TipoImporte.getByIdNull(TipoImporte.ID_INDETERMINADO);
				this.getAsentamiento().setTipoImporteIndet(tipoImporte);
			}
			if(tipoImporte == null){
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No existe el Tipo Importe con id "+TipoImporte.ID_INDETERMINADO);
				return;			
			}			
		}	
		this.getAsentamiento().logDetallado("	Tipo de Importe: "+tipoImporte.getDesTipoImporte());
		Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), this.getSistema());
		
		Long tipoBoleta = this.getTipoBoleta();
		this.getAsentamiento().logDetallado("	Tipo de Boleta: "+tipoBoleta);
		// Si el Tipo de Boleta es: Deuda o Recibo de Deuda, buscamos el distribuidor por Recurso.
		DisPar disPar = null;
		Partida partida = null;
		Double porcentaje = null;
		if(tipoBoleta.longValue() == Transaccion.TIPO_BOLETA_DEUDA
							|| tipoBoleta.longValue() == Transaccion.TIPO_BOLETA_RECIBO_DEUDA
							|| (convenioCuota==null && reciboConvenio==null)){
			DisParRec disParRec = null;
			if(deuda != null || recibo != null){
				Long idRecurso = null;
				Long idViaDeuda = null;
				String atrAseVal = null;
				if(deuda != null){
					idRecurso = deuda.getRecurso().getId();		
					idViaDeuda = deuda.getViaDeuda().getId();
				}else{
					idRecurso = recibo.getRecurso().getId();		
					idViaDeuda = recibo.getViaDeuda().getId();
				}
				Recurso recurso = AsentamientoCache.getInstance().getRecursoById(idRecurso);
				if(recurso.getAtributoAse() != null){
					if(deuda != null){
						atrAseVal = deuda.getAtrAseVal();
						this.getAsentamiento().logDetallado("	Valor de Atributo de Asentamiento de la Deuda: "+atrAseVal);
					}else{
						atrAseVal = recibo.getValorAtributo();
						if(atrAseVal == null)
							atrAseVal = recibo.getCuenta().getValorAtributo(recibo.getRecurso().getAtributoAse().getId(), recibo.getFechaGeneracion());
						this.getAsentamiento().logDetallado("	Valor de Atributo de Asentamiento de la Cuenta para Fecha de Generacion del Recibo: "+atrAseVal);
					}					
				}
				
				// recupera una lista de DisParRec
				List<DisParRec> listDisParRec =  AsentamientoCache.getInstance().getListByRecursoViaDeudaValAtrAse(recurso, ViaDeuda.getById(idViaDeuda), atrAseVal);
				
				if (listDisParRec.size()==0 ) {
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro Distribuidor de partidas para: idRecurso: " + recurso.getId() + 
							" ViaDeuda: " + idViaDeuda + " valorAtributoAsentamiento: " + atrAseVal);
					return;
					
				} else if (listDisParRec.size()>1) {
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Se encontro mas de un Distribuidor de partidas para: idRecurso: " + recurso.getId() + 
							" ViaDeuda: " + idViaDeuda + " valorAtributoAsentamiento: " + atrAseVal );
					return;
					
				} else {
					// toma el DisParRec
					disParRec = listDisParRec.get(0);
				}
					
				disPar = disParRec.getDisPar();
			}else{
				//Obtiene el Distribuidor para Indeterminado
				this.getAsentamiento().logDetallado("	Buscando Partida de Indeterminado asociada al Servicio Banco.");
				partida = this.getAsentamiento().getServicioBanco().getPartidaIndet();
				if(partida == null){
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No existe la Partida de Indeterminados asociada al Servicio Banco.");
					return;
				}
				porcentaje = 1D;
			}
		// Si el Tipo de Boleta es: Cuota o Recibo de Cuota, lo buscamos por Plan.
		}else{		
			if(convenioCuota != null || reciboConvenio != null){
				// Obtener Distribuidor de Partidas Vigentes asociadas al Plan.
				Plan plan = null;
				Convenio convenio = null;
				Recurso recurso = null;
				if(convenioCuota!=null){
					plan = convenioCuota.getConvenio().getPlan();
					convenio = convenioCuota.getConvenio();
					recurso = AsentamientoCache.getInstance().getRecursoById(convenioCuota.getConvenio().getRecurso().getId());
				}else{
					plan = reciboConvenio.getConvenio().getPlan();
					convenio = reciboConvenio.getConvenio();
					recurso = AsentamientoCache.getInstance().getRecursoById(reciboConvenio.getConvenio().getRecurso().getId());					
				}
				
				DisParPla disParPla=null;
				String atrAseVal = null; 
				if(recurso.getAtributoAse() != null){
					atrAseVal = convenio.getValorAtributo();
					if(atrAseVal == null)
						atrAseVal = convenio.getCuenta().getValorAtributo(recurso.getAtributoAse().getId(), convenio.getFechaFor());
					this.getAsentamiento().logDetallado("	Valor de Atributo de Asentamiento de la Cuenta para Fecha de Formalizacion del Plan: "+atrAseVal);
				}
				// Recupera el DisParPla
				List<DisParPla> listDisParPla =  AsentamientoCache.getInstance().getListByPlanRecValAtrAse(plan, recurso, atrAseVal);
				if (listDisParPla.size()==0 ) {
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro Distribuidor de partidas para: idPlan: " + plan.getId() + 
							" valorAtributoAsentamiento: " + atrAseVal);
					return;					
				} else if (listDisParPla.size()>1) {
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Se encontro mas de un Distribuidor de partidas para: idPlan: " + plan.getId() + 
							" valorAtributoAsentamiento: " + atrAseVal);
					return;
				} else {
					// Toma el DisParPla
					disParPla = listDisParPla.get(0);
				}
				disPar = disParPla.getDisPar();				
			}else{
				// Si no puede encontrar el del plan porque no se encontro la transaccion se busca el Distribuidor
				// generico para Indeterminado
				this.getAsentamiento().logDetallado("	Buscando Partida de Indeterminado asociada al Servicio Banco.");
				partida = this.getAsentamiento().getServicioBanco().getPartidaIndet();
				if(partida == null){
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No existe la Partida de Indeterminados asociada al Servicio Banco.");
					return;
				}
				porcentaje = 1D;
			}
		}
		if(disPar != null){
			this.getAsentamiento().logDetallado("	Distribuidor encontrado. Buscando Detalle para el Tipo de Importe correspondiente.");
			// Obtener el Detalle del Distribuidor
			List<DisParDet> listDisParDet = DisParDet.getListByDisParYidTipoImporteYRecCon(disPar, tipoImporte.getId(), null );
			if(ListUtil.isNullOrEmpty(listDisParDet)){
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No existe el Detalle del Distribuidor de Partidas asociado al Recurso "+disPar.getRecurso().getDesRecurso()
						+", Tipo de Importe "+tipoImporte.getDesTipoImporte());
				return;
			}
			if(listDisParDet.size()>1){
				this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Existe más de un Detalle del Distribuidor de Partidas asociado al Recurso "+disPar.getRecurso().getDesRecurso()
						+", Tipo de Importe "+tipoImporte.getDesTipoImporte());
				return;			
			}
			DisParDet disParDet = listDisParDet.get(0);			
			partida = disParDet.getPartida();
			porcentaje = disParDet.getPorcentaje();
		}
		if(this.getAsentamiento().getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){			
			// Determinar si corresponde a Ejercicio Actual o Vencido, validando si la fecha de vencimiento y la fecha de 
			// pago corresponden al mismo ejercicio. (valido para codigo <> 1)
			boolean esActual = false;
			Date fechaVencimiento = null;
			if(deuda != null){
				fechaVencimiento = deuda.getFechaVencimiento();
				if(DateUtil.isDateAfterOrEqual(fechaVencimiento,this.getAsentamiento().getEjercicio().getFecIniEje()) 
						&& DateUtil.isDateBeforeOrEqual(fechaVencimiento, this.getAsentamiento().getEjercicio().getFecFinEje())
						&& DateUtil.isDateAfterOrEqual(this.getFechaPago(),this.getAsentamiento().getEjercicio().getFecIniEje())
						&& DateUtil.isDateBeforeOrEqual(this.getFechaPago(), this.getAsentamiento().getEjercicio().getFecFinEje())){
					esActual = true;
				}			
			}else{
				esActual = true;
			}
			// Validar si existe un registro en la tabla AuxRecaudado a modificar
			ViaDeuda viaDeuda = null;
			if(deuda != null){
				viaDeuda = deuda.getViaDeuda(); 			
			}
			if(recibo != null){
				viaDeuda = recibo.getViaDeuda(); 			
			}
			if(viaDeuda == null){
				viaDeuda = ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN);
			}
			AuxRecaudado auxRecaudado = this.getAsentamiento().getAuxRecaudado(this, partida, porcentaje, viaDeuda, null, tipoBoleta);
			// Si se encontro un registro
			Double importe = this.getImporte();
			if(auxRecaudado != null){
				// Actualizar el importe acumulando el importe pagado
				if(esActual)
					auxRecaudado.setImporteEjeAct(auxRecaudado.getImporteEjeAct()+importe);
				else
					auxRecaudado.setImporteEjeVen(auxRecaudado.getImporteEjeVen()+importe);
				
				// Si no se encontro un registro
			}else{
				// Insertar registro en AuxRecaudado
				auxRecaudado = new AuxRecaudado();
				auxRecaudado.setAsentamiento(this.getAsentamiento());
				auxRecaudado.setSistema(sistema);
				auxRecaudado.setFechaPago(this.getFechaPago());
				auxRecaudado.setPartida(partida);
				auxRecaudado.setViaDeuda(viaDeuda);	
				auxRecaudado.setTipoBoleta(tipoBoleta);
				if(esActual){
					auxRecaudado.setImporteEjeAct(importe);
					auxRecaudado.setImporteEjeVen(0D);
				}else{
					auxRecaudado.setImporteEjeAct(0D);
					auxRecaudado.setImporteEjeVen(importe);	
				}
				auxRecaudado.setPorcentaje(porcentaje);
				this.getAsentamiento().createAuxRecaudado(auxRecaudado);
			}
			Double paraLog = 0D;
			if(AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).isLogDetalladoEnabled())
				paraLog = auxRecaudado.getImporteEjeAct()+auxRecaudado.getImporteEjeVen();
			this.getAsentamiento().logDetallado("	Se acumula en AuxRecaudado. Partida: "+partida.getDesPartida()+" Importe Agregado: "+importe+" Importe Acumulado: "+paraLog+" Porcentaje: "+auxRecaudado.getPorcentaje()*100+" %");
		}

		// -> Grabar en SinIndet.
		SinIndet sinIndet = new SinIndet();
		sinIndet.setAsentamiento(this.getAsentamiento());
		sinIndet.setSistema(this.getSistema());
		sinIndet.setNroComprobante(this.getNroComprobante());
		sinIndet.setAnioComprobante(this.getAnioComprobante());
		sinIndet.setPeriodo(this.getPeriodo());
		sinIndet.setResto(this.getResto());
		sinIndet.setCodPago(this.getCodPago());
		sinIndet.setCaja(this.getCaja());
		sinIndet.setCodTr(this.getCodTr());
		sinIndet.setFechaPago(this.getFechaPago());
		sinIndet.setImporteCobrado(this.getImporte());
		if(deuda != null){
			sinIndet.setBasico(deuda.getSaldo());
			if(deudaAct != null){
				sinIndet.setCalculado(deudaAct.getImporteAct());
				sinIndet.setIndice(deudaAct.getCoeficiente());
				sinIndet.setRecargo(deudaAct.getRecargo());				
			}
		}
		// Si la transaccion es de origen Osiris
		if (Transaccion.ORIGEN_OSIRIS.equals(this.getOrigen())) {
			sinIndet.setFiller(SinIndet.FILLER_AFIP);	
			sinIndet.setReciboTr(this.getIdTranAfip());
		}else {
			sinIndet.setFiller("");
			sinIndet.setReciboTr(this.getReciboTr());
		}
		sinIndet.setPaquete(this.getPaquete());
		sinIndet.setMarcaTr(this.getMarcaTr());
		sinIndet.setCodIndeterminado(tipoIndet.getCodIndetMR());
		sinIndet.setPartida(partida);
		sinIndet.setFechaBalance(this.getFechaBalance());
		sinIndet.setTipoIndet(tipoIndet);
		this.getAsentamiento().createSinIndet(sinIndet);

		// -> Marcar Transaccion
		if(this.getEsDesgloce() != null && this.getEsDesgloce().intValue() == SiNo.NO.getId().intValue()){
			this.setEsIndet(SiNo.SI.getId());
			this.getAsentamiento().updateTransaccion(this);
		}
	
		// Si corresponde a Servicio Banco: 85 - Otros Tributos (ex Gravamenes Especiales), guardamos datos para reporte por Recurso.
		if(ServicioBanco.COD_OTROS_TRIBUTOS.equals(this.getAsentamiento().getServicioBanco().getCodServicioBanco()) && disPar != null){
			Recurso recurso = disPar.getRecurso();
			// Buscamos un registro acumulador de importe por Recurso y Tipo de Importe
			AuxImpRec auxImpRec = null;
			auxImpRec = AuxImpRec.getForAsentamiento(recurso.getId(), tipoImporte.getId(), this.getAsentamiento().getId());
			if(auxImpRec != null){
				auxImpRec.setImporte(auxImpRec.getImporte()+this.getImporte());
				this.getAsentamiento().updateAuxImpRec(auxImpRec);				
			}else{
			// Si no existe lo creamos
				auxImpRec = new AuxImpRec();
				auxImpRec.setRecurso(recurso);
				auxImpRec.setTipoImporte(tipoImporte);
				auxImpRec.setAsentamiento(this.getAsentamiento());
				auxImpRec.setImporte(this.getImporte());
				this.getAsentamiento().createAuxImpRec(auxImpRec);
			}
		}
		
		// Si corresponde a Servicio Banco de Tipo Auto Liquidable y tenemos la Deuda
		if(this.getAsentamiento().getServicioBanco().getEsAutoliquidable() != null && this.getAsentamiento().getServicioBanco().getEsAutoliquidable().intValue() == SiNo.SI.getId().intValue()
				&& deuda != null){
			// Buscamos la deuda en auxDeuMdf.
			AuxDeuMdf auxDeuMdf = AuxDeuMdf.getByIdDeudaYAse(deuda.getId(),this.getAsentamiento().getId(), this.getId());
			// Si existe, restauramos la deuda o eliminamos segun corresponda.
			if(auxDeuMdf != null){
				if(auxDeuMdf.getEsNueva().intValue() == SiNo.NO.getId().intValue()){
					DeudaAdmin deudaARestaurar = DeudaAdmin.getById(auxDeuMdf.getIdDeuda());
					if(deudaARestaurar != null){
						deudaARestaurar.setImporte(auxDeuMdf.getImporteOrig());
						deudaARestaurar.setImporteBruto(auxDeuMdf.getImporteOrig());
						deudaARestaurar.setSaldo(auxDeuMdf.getSaldoOrig());
						deudaARestaurar.setReclamada(0);
						List<DeuAdmRecCon> listDeuAdmRecCon = deudaARestaurar.getListDeuRecCon();
						for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
							RecCon recCon = deuAdmRecCon.getRecCon();
							deuAdmRecCon.setImporte(NumberUtil.truncate(deudaARestaurar.getImporte()*recCon.getPorcentaje(), SiatParam.DEC_IMPORTE_DB));
							deuAdmRecCon.setImporteBruto(deuAdmRecCon.getImporte());
						}
						deudaARestaurar.setStrConceptosByListRecCon(listDeuAdmRecCon);
						GdeDAOFactory.getDeudaDAO().update(deudaARestaurar);
						
						for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
							GdeDAOFactory.getDeuAdmRecConDAO().update(deuAdmRecCon);
						}									
					}
				}else{
					DeudaAdmin deudaAEliminar = DeudaAdmin.getById(auxDeuMdf.getIdDeuda());
					if(deudaAEliminar != null){
						List<DeuAdmRecCon> listDeuAdmRecCon = deudaAEliminar.getListDeuRecCon();
						for(DeuAdmRecCon deuAdmRecCon: listDeuAdmRecCon){
							deudaAEliminar.deleteDeuAdmRecCon(deuAdmRecCon);
						}
						SiatHibernateUtil.currentSession().flush();
						
						GdeGDeudaManager.getInstance().deleteDeudaAdmin(deudaAEliminar);						
					}
				} 
			}
		}
	

		this.getAsentamiento().logDetallado("Saliendo de Registrar Indeterminado.");
		indTime = System.currentTimeMillis() - indTime;
		AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).addStats("Registrar Indeterminado", indTime);
	}
	
	/**
	 * Analiza la transaccion para identificar si se trata de una Deuda
	 * 
	 * @return
	 */
	public boolean esDeuda()throws Exception{
		if(this.getTipoBoleta().longValue() == Transaccion.TIPO_BOLETA_DEUDA)
			return true;
		else
			return false;
	}
	
	/**
	 * Analiza la transaccion para identificar si se trata de un Recibo de Deuda
	 * 
	 * @return
	 */
	public boolean esReciboDeuda()throws Exception{
		if(this.getTipoBoleta().longValue() == Transaccion.TIPO_BOLETA_RECIBO_DEUDA)
			return true;
		else
			return false;
	}
	
	/**
	 * Analiza la transaccion para identificar si se trata de una Cuota
	 * 
	 * @return
	 */
	public boolean esCuota()throws Exception{
		if(this.getTipoBoleta().longValue() == Transaccion.TIPO_BOLETA_CUOTA)
			return true;
		else
			return false;
	}
	
	/**
	 * Analiza la transaccion para identificar si se trata de un Recibo de Cuota
	 * 
	 * @return
	 */
	public boolean esReciboCuota()throws Exception{
		if(this.getTipoBoleta().longValue() == Transaccion.TIPO_BOLETA_RECIBO_CUOTA)
			return true;
		else
			return false;
	}
	
	/**
	 * Analiza la transaccion para identificar si se trata de un Sellado
	 * 
	 * @return
	 */
	public boolean esSellado()throws Exception{
		if(this.getTipoBoleta().longValue() == Transaccion.TIPO_BOLETA_SELLADO)
			return true;
		else
			return false;
	}
	
	/**
	 * Analiza la transaccion para identificar si se trata de una transaccion nueva o vieja.
	 * (O sea si el Sistema es un Servicio Banco o no.)
	 * 
	 * @return
	 */
	public boolean esNueva() throws Exception{
		// Obtener el sistema asociado
		Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), 
                this.getSistema());
		if(sistema != null){
			// Sistema corresponde a Servicio Banco
			if(sistema.getEsServicioBanco().intValue()==SiNo.SI.getId().intValue())
				return true;
			else 
				return false;
		}		
		return false;
	}
	

	/**
	 * Obtiene la Tolerancia permitida para una transaccion, el porcentaje maximo para Saldo a Favor y la tolerancia
	 * para toda la Partida devolviendo el unico registro vigente segun la fecha de ejecucion del asentamiento, de la
	 * tabla bal_tolerancia, consultado por idServicioBanco. Si no existe vigente se devuelve null y las tolerancias
	 * se consideran cero.
	 * @return tolerancia
	 */
	public Tolerancia obtenerTolerancia() throws Exception {
		return Tolerancia.getBySerBanYFecha(this.getAsentamiento().getServicioBanco(), new Date());
	}

	
	/**
	 * Distribuye el Importe pasado para el Concepto (si es distinto de null) y Distribuidor tambien indicados.
	 * (subpaso del paso 2.1 del Proceso de Asentamiento)
	 * 
	 * @param disPar
	 * @param deuAdmRecCon
	 * @param importeADistribuir
	 * @param idTipoImporte
	 * @param deuda
	 */
	public void distribuir(DisPar disPar, DeuRecCon deuRecCon,Double importeADistribuir,Long idTipoImporte, Deuda deuda) throws Exception{
		this.distribuir(disPar, deuRecCon, importeADistribuir, idTipoImporte, deuda, null);
	}
	
	/**
	 * Distribuye el Importe pasado para el Concepto (si es distinto de null) y Distribuidor tambien indicados.
	 * (subpaso del paso 2.1 del Proceso de Asentamiento)
	 * 
	 * @param disPar
	 * @param deuAdmRecCon
	 * @param importeADistribuir
	 * @param idTipoImporte
	 * @param deuda
	 */
	public void distribuir(DisPar disPar, DeuRecCon deuRecCon,Double importeADistribuir,Long idTipoImporte, ConvenioCuota convenioCuota) throws Exception{
		this.distribuir(disPar, deuRecCon, importeADistribuir, idTipoImporte, null, convenioCuota);
	}
	
	
	/**
	 * Distribuye el Importe pasado para el Concepto (si es distinto de null) y Distribuidor tambien indicados.
	 * (subpaso del paso 2.1 del Proceso de Asentamiento)
	 * 
	 * @param disPar
	 * @param deuAdmRecCon
	 * @param importeADistribuir
	 * @param idTipoImporte
	 * @param deuda
	 */
	public void distribuir(DisPar disPar, DeuRecCon deuRecCon,Double importeADistribuir,Long idTipoImporte, Deuda deuda, ConvenioCuota convenioCuota) throws Exception{
		long disTime = System.currentTimeMillis();
		this.getAsentamiento().logDetallado("Entrando a Distribuir...");
		this.getAsentamiento().logDetallado("	Transaccion de Linea Nro: "+this.getNroLinea()+" Importe a Distribuir: "+importeADistribuir);
		if(disPar!=null) 
			this.getAsentamiento().logDetallado("DisPar: "+disPar.getDesDisPar()+", Importe a Distribuir: "+importeADistribuir+", Id Tipo de Importe: "+idTipoImporte);
		Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getAsentamiento().getServicioBanco().getId(), 
                this.getSistema());
		Long tipoBoleta = this.getTipoBoleta();
		this.getAsentamiento().logDetallado("	Tipo Boleta: "+tipoBoleta);
		TipoImporte tipoImporte = TipoImporte.getByIdNull(idTipoImporte);
		if(tipoImporte == null){
			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No existe el Tipo Importe con id "+idTipoImporte);
			return;			
		}
		this.getAsentamiento().logDetallado("	Tipo de Importe: "+tipoImporte.getDesTipoImporte());
		// Obtener la lista de Detalles del Distribuidor por Concepto y para el Tipo de Importe que corresponda
		RecCon recCon = null;
		if(deuRecCon != null)
			recCon = deuRecCon.getRecCon(); 
		// Obtiene el Distribuidor para "Saldo a Reingresar"
		if(disPar == null) {
			// Si el Tipo de Boleta es: Deuda, buscamos el distribuidor por Recurso.
			if(tipoBoleta.longValue() == Transaccion.TIPO_BOLETA_DEUDA
					|| tipoBoleta.longValue() == Transaccion.TIPO_BOLETA_RECIBO_DEUDA
					|| (convenioCuota==null)){
				// Obtiene el Distribuidor para "Saldo a Reingresar"
				Recurso recurso = AsentamientoCache.getInstance().getRecursoById(deuda.getRecurso().getId());
				
				DisParRec disParRec=null;
				this.getAsentamiento().logDetallado("	Valor de Atributo de Asentamiento de la Deuda: "+deuda.getAtrAseVal());
				// recupera una lista de DisParRec
				List<DisParRec> listDisParRec =  AsentamientoCache.getInstance().getListByRecursoViaDeudaValAtrAse(recurso, deuda.getViaDeuda(), deuda.getAtrAseVal());
				
				if (listDisParRec.size()==0 ) {
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro Distribuidor de partidas para: idRecurso: " + recurso.getId() + 
							" ViaDeuda: " + deuda.getViaDeuda().getId() + " valorAtributoAsentamiento: " + deuda.getAtrAseVal());
					return;
					
				} else if (listDisParRec.size()>1) {
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Se encontro mas de un Distribuidor de partidas para: idRecurso: " + recurso.getId() + 
							" ViaDeuda: " + deuda.getViaDeuda().getId() + " valorAtributoAsentamiento: " + deuda.getAtrAseVal());
					return;
					
				} else {
					// toma el DisParRec
					disParRec = listDisParRec.get(0);
				}
				disPar = disParRec.getDisPar();
				
				// Si el Tipo de Boleta es: Cuota, lo buscamos por Plan.
			}else{		
				// Obtener Distribuidor de Partidas Vigentes asociadas al Plan.
				Plan plan = null;
				Convenio convenio = null;
				plan = convenioCuota.getConvenio().getPlan();
				convenio = convenioCuota.getConvenio();
				Recurso recurso = AsentamientoCache.getInstance().getRecursoById(convenio.getRecurso().getId());
				
				DisParPla disParPla=null;
				String atrAseVal = null; 
				if(recurso.getAtributoAse() != null){
					if(deuda!=null){
						atrAseVal = deuda.getAtrAseVal();
						this.getAsentamiento().logDetallado("	Valor de Atributo de Asentamiento de la Deuda: "+atrAseVal);
					}else if(deuRecCon != null){
						atrAseVal = deuRecCon.getDeuda().getAtrAseVal();
						this.getAsentamiento().logDetallado("	Valor de Atributo de Asentamiento de la Deuda: "+atrAseVal);
					}else{
						atrAseVal =convenio.getCuenta().getValorAtributo(recurso.getAtributoAse().getId(), convenio.getFechaFor());
						this.getAsentamiento().logDetallado("	Valor de Atributo de Asentamiento de la Cuenta para Fecha de Formalizacion del Plan: "+atrAseVal);
					}					
				}
				// Recupera una lista de DisParPla
				// Obtiene el Recurso del cache
				List<DisParPla> listDisParPla =  AsentamientoCache.getInstance().getListByPlanRecValAtrAse(plan, recurso,  atrAseVal);
				
				if (listDisParPla.size()==0) {
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro Distribuidor de partidas para: idPlan: " + plan.getId() + 
							" valorAtributoAsentamiento: " + atrAseVal);
					return;					
				} else if (listDisParPla.size()>1) {
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". Se encontro mas de un Distribuidor de partidas para: idPlan: " + plan.getId() + 
							" valorAtributoAsentamiento: " + atrAseVal);
					return;
				} else {
					// Toma el DisParPla
					disParPla = listDisParPla.get(0);
				}
				disPar = disParPla.getDisPar();
				
			}
		}
		
		Long idRecCon = null;
		if(recCon != null)
				idRecCon = recCon.getId();
		this.getAsentamiento().logDetallado("	Distribuidor encontrado. Buscando Detalle para el Tipo de Importe "+idTipoImporte+" y Concepto "+idRecCon);
		List<DisParDet> listDisParDet = disPar.getListDisParDetByidTipoImporteYRecCon(idTipoImporte, idRecCon);
		if(ListUtil.isNullOrEmpty(listDisParDet)){
			String message = "";
			message += "Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No existe el Detalle del Distribuidor de Partidas asociado al Recurso "+disPar.getRecurso().getDesRecurso();
			message +=", Distribuidor de id: "+disPar.getId();
			message +=", Tipo de Importe "+tipoImporte.getDesTipoImporte();
			if(deuRecCon != null)
				message +=" y Concepto con id: "+deuRecCon.getRecCon().getId();
			this.addRecoverableValueError(message);
			return;
		}
		// Determinar si corresponde a Ejercicio Actual o Vencido, validando si la fecha de vencimiento y la fecha de 
		// pago corresponden al mismo ejercicio.
		boolean esActual = false;
		Date fechaVencimiento = null;
		if(deuda != null){
			fechaVencimiento = deuda.getFechaVencimiento();
		}else{
			fechaVencimiento = convenioCuota.getFechaVencimiento();
		}
		if(DateUtil.isDateAfterOrEqual(fechaVencimiento,this.getAsentamiento().getEjercicio().getFecIniEje()) 
				&& DateUtil.isDateBeforeOrEqual(fechaVencimiento, this.getAsentamiento().getEjercicio().getFecFinEje())
				&& DateUtil.isDateAfterOrEqual(this.getFechaPago(),this.getAsentamiento().getEjercicio().getFecIniEje())
				&& DateUtil.isDateBeforeOrEqual(this.getFechaPago(), this.getAsentamiento().getEjercicio().getFecFinEje())){
					esActual = true;
		}
		ViaDeuda viaDeuda = null;
		Plan plan = null;
		Convenio convenio = null;
		if(deuda != null){
			convenio = deuda.getConvenio();
		}else{
			convenio = convenioCuota.getConvenio();
		}
		if(tipoBoleta.longValue() == Transaccion.TIPO_BOLETA_DEUDA 
				|| tipoBoleta.longValue() == Transaccion.TIPO_BOLETA_RECIBO_DEUDA){
			if(deuda != null)
				viaDeuda = deuda.getViaDeuda();
		}else{
			if(convenio!=null){
				plan = convenio.getPlan();						
			}else{				
				if(convenioCuota != null)
					convenio = convenioCuota.getConvenio();
				if(convenio != null){
					plan = convenio.getPlan();
				}else {
					this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se encontro el Convenio.");
					return;
				}
			}
		}
		// Por cada detalle:
		for(DisParDet disParDet: listDisParDet){
			AuxRecaudado auxRecaudado = this.getAsentamiento().getAuxRecaudado(this, disParDet.getPartida(), disParDet.getPorcentaje(), viaDeuda, plan, tipoBoleta);
			Double importe = 0D;
			if(idTipoImporte.longValue() == TipoImporte.ID_SALDO_A_REINGRESAR){
				importe = importeADistribuir;
				this.setPartidaSaldosAFavor(disParDet.getPartida());
			}else{
				importe = (deuRecCon.getImporte()/deuda.getImporte())*importeADistribuir; 				
			}
			// Si se encontro un registro
			if(auxRecaudado != null){
				// Actualizar el importe acumulando la proporcion del concepto en la deuda, aplicado al Capital a Distribuir
				if(esActual)
					auxRecaudado.setImporteEjeAct(auxRecaudado.getImporteEjeAct()+importe);
				else
					auxRecaudado.setImporteEjeVen(auxRecaudado.getImporteEjeVen()+importe);

				Double paraLog = 0D;
				if(AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).isLogDetalladoEnabled())
					paraLog = auxRecaudado.getImporteEjeAct()+auxRecaudado.getImporteEjeVen();
				this.getAsentamiento().logDetallado("	AuxRecaudado, Partida: "+disParDet.getPartida().getDesPartida()+" Importe Agregado: "+importe+" Importe Acumulado: "+paraLog+" Porcentaje: "+auxRecaudado.getPorcentaje()*100+" %");
				// Si no se encontro un registro
			}else{
				// Insertar registro en AuxRecaudado
				auxRecaudado = new AuxRecaudado();
				auxRecaudado.setAsentamiento(this.getAsentamiento());
				auxRecaudado.setSistema(sistema);
				auxRecaudado.setFechaPago(this.getFechaPago());
				auxRecaudado.setPartida(disParDet.getPartida());
				auxRecaudado.setViaDeuda(viaDeuda);
				auxRecaudado.setPlan(plan);
				auxRecaudado.setTipoBoleta(tipoBoleta);
				if(esActual){
					auxRecaudado.setImporteEjeAct(importe);
					auxRecaudado.setImporteEjeVen(0D);
				}else{
					auxRecaudado.setImporteEjeAct(0D);
					auxRecaudado.setImporteEjeVen(importe);	
				}
				auxRecaudado.setPorcentaje(disParDet.getPorcentaje());
				this.getAsentamiento().createAuxRecaudado(auxRecaudado);
				Double paraLog = 0D;
				if(AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).isLogDetalladoEnabled())
					paraLog = auxRecaudado.getImporteEjeAct()+auxRecaudado.getImporteEjeVen();
				this.getAsentamiento().logDetallado("	AuxRecaudado, Partida: "+disParDet.getPartida().getDesPartida()+" Importe Agregado: "+importe+" Importe Acumulado: "+paraLog+" Porcentaje: "+auxRecaudado.getPorcentaje()*100+" %");
			}
		}
		
		// Si corresponde a Servicio Banco: 85 - Otros Tributos (ex Gravamenes Especiales), guardamos datos para reporte por Recurso.
		if(ServicioBanco.COD_OTROS_TRIBUTOS.equals(this.getAsentamiento().getServicioBanco().getCodServicioBanco())){
			Recurso recurso = disPar.getRecurso();
			Double importeAAcumular = 0D;
			if(tipoImporte.getId().longValue() == TipoImporte.ID_SALDO_A_REINGRESAR){
				importeAAcumular = importeADistribuir;
			}else{
				importeAAcumular = (deuRecCon.getImporte()/deuda.getImporte())*importeADistribuir; 				
			}
			
			// Buscamos un registro acumulador de importe por Recurso y Tipo de Importe
			AuxImpRec auxImpRec = null;
			auxImpRec = AuxImpRec.getForAsentamiento(recurso.getId(), tipoImporte.getId(), this.getAsentamiento().getId());
			if(auxImpRec != null){
				auxImpRec.setImporte(auxImpRec.getImporte()+importeAAcumular);
				this.getAsentamiento().updateAuxImpRec(auxImpRec);				
			}else{
			// Si no existe lo creamos
				auxImpRec = new AuxImpRec();
				auxImpRec.setRecurso(recurso);
				auxImpRec.setTipoImporte(tipoImporte);
				auxImpRec.setAsentamiento(this.getAsentamiento());
				auxImpRec.setImporte(importeAAcumular);
				this.getAsentamiento().createAuxImpRec(auxImpRec);
			}
		}
		
		
		this.getAsentamiento().logDetallado("Saliendo de Distribuir...");

		disTime = System.currentTimeMillis() - disTime;
		AsentamientoCache.getInstance().getSession(this.getAsentamiento().getId()).addStats("Distribuir", disTime);
		//AdpRun.currentRun().logDebug("<-> Tiempo consumido en 'distribuir' para transaccion nro "+this.getNroLinea()+": "+disTime+" ms <->");
		
	}

	/**
	 *	Se utiliza para realizar Tareas adicionales relacionadas con el Serivicio Banco.
	 *	<b>Se debe implementar en la clase TransaccionZZZ correspondiente al Servicio Banco ZZZ</b>   
	 *
	 */
	public void realizarTareasComplementarias(Deuda deuda) throws Exception{
	}
	
	/**
	 *	Analiza si todas las Deudas del Convenio estan en la Via y Estado Correctos.  
	 *
	 */
	public boolean verificaDeudaDeConvenio(Convenio convenio) throws Exception{
		List<ConvenioDeuda> listConvenioDeuda = ConvenioDeuda.getListWithSaldo(convenio);
		for(ConvenioDeuda convenioDeuda: listConvenioDeuda){
			Long idEstadoDeuda = null; 
			if(convenio.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_ADMIN)
				idEstadoDeuda = EstadoDeuda.ID_ADMINISTRATIVA;
			if(convenio.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL)
				idEstadoDeuda = EstadoDeuda.ID_JUDICIAL;
			if(idEstadoDeuda == null)
				return false;
			Deuda deuda = Deuda.getByIdNull(convenioDeuda.getIdDeuda(), idEstadoDeuda);
			if(deuda == null || deuda.getEstadoDeuda().getId().longValue() != idEstadoDeuda.longValue())
				return false;
		}
		
		return true;
	}

	/**
	 *  Suma los importes de los Conceptos pasados en la lista y lo compara con el importe con tolerancia 
	 *  ambos indicados en los parametros.
	 * 
	 * @param listDeuRecCon
	 * @param importe
	 * @param tolerancia
	 * @return true si valida, false si encuentra diferencia
	 */
	public boolean validaConceptos(List<DeuRecCon> listDeuRecCon, Double importe, Double tolerancia){
		Double totalConceptos = 0D;
		for(DeuRecCon deuRecCon: listDeuRecCon){
			totalConceptos += deuRecCon.getImporte();
		}
		if(NumberUtil.isDoubleEqualToDouble(totalConceptos, importe, tolerancia))
			return true;
		else
			return false;
	}

	/**
	 * Procesar Sellado.
	 * 	
	 * <i>(paso 2.1.z)</i>
	 */
	public void procesarSellado(Sellado sellado) throws Exception{
		// El procesamiento de sellado se encuentra en TransaccionSEL.
	}

	/**
	 * Delegar Transaccion al Proceso de Asentamiento Delegado correspondiente.
	 * 	
	 */
	public void delegar() throws Exception{
		AseDel aseDel = AseDel.getByBalanceYServicioBanco(this.getAsentamiento().getBalance(), this.getAsentamiento().getServicioBanco());
		if(aseDel == null){
 			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se pudo delegar la transaccion. No se encontró el Proceso de Asentamiento Delegado para el Servicio Banco y Balance asociados a este proceso.");
			return;     			
 		}	
		
		// Armar Transaccion para Delegador de Asentamiento
		TranDel tranDel = new TranDel();
		
 		tranDel.setAseDel(aseDel);
 		tranDel.setSistema(this.getSistema());
 		tranDel.setNroComprobante(this.getNroComprobante());
 		tranDel.setAnioComprobante(this.getAnioComprobante());
		tranDel.setPeriodo(this.getPeriodo());	     				     				
 		tranDel.setResto(this.getResto());
 		tranDel.setFechaPago(this.getFechaPago());
 		tranDel.setCodPago(this.getCodPago()); 
 		tranDel.setCaja(this.getCaja()); 
 		tranDel.setCodTr(this.getCodTr()); 
 		tranDel.setImporte(this.getImporte()); 
 		tranDel.setRecargo(this.getRecargo());
 		tranDel.setPaquete(this.getPaquete());
 		tranDel.setMarcaTr(this.getMarcaTr());
 		tranDel.setReciboTr(this.getReciboTr());
		tranDel.setFechaBalance(this.getFechaBalance());
 		tranDel.setNroLinea(this.getNroLinea());
 			    		
 		// Creamos el registro de tranDel
 		aseDel.createTranDel(tranDel); 
 		if(tranDel.hasError()){
 			this.addRecoverableValueError("Error!. Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+". No se pudo delegar la transaccion al proceso de Asentamiento Delegado Nro:"+aseDel.getId()+" .");
			return;     			
 		}	
 		
 		// Marcar la transaccion como Delegada.
 		this.setDelegada(SiNo.SI.getId());  
		this.getAsentamiento().updateTransaccion(this);
 		AdpRun.currentRun().logDebug("Transaccion Delegada!. La Transaccion de Id: "+this.getId()+", Linea Nro: "+this.getNroLinea()+" se pasa al Asentamiento Delegado Nro:"+aseDel.getId()+" para su asentamiento por proceso externo. ");
	}

}
