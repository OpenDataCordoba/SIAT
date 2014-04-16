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

import ar.gov.rosario.siat.afi.buss.bean.ForDecJur;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.RecCon;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a DecJur
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_decJur")
public class DecJur extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	private Logger log = Logger.getLogger(DecJur.class);
	
	public static final String CREADO = "Creado";
	public static final String MODIFICADO = "Modificado";
	public static final Long CANT_DIAS_HABILES_SIGTES = 5L;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idCuenta")
	private Cuenta cuenta;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idTipDecJurRec")
	private TipDecJurRec tipDecJurRec;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idOriDecJur")
	private OriDecJur oriDecJur;
	
	@Column(name="fechaPresentacion", nullable=false)
	private Date fechaPresentacion;
	
	@Column(name="fechaNovedad", nullable=false)
	private Date fechaNovedad;
	
	@Column(name="periodo")
	private Integer periodo;
	
	@Column(name="anio")
	private Integer anio;
	
	@Column(name="subtotal", nullable=false)
	private Double subtotal;
	
	@Column(name="aliPub")
	private Double aliPub;
	
	@Column(name="totalPublicidad")
	private Double totalPublicidad;
	
	@Column(name="aliMesYSil")
	private Double aliMesYSil;
	
	@Column(name="totMesYSil")
	private Double totMesYSil;
	
	@Column(name="totalDeclarado", nullable=false)
	private Double totalDeclarado;
	
	@Column(name="otrosPagos", nullable=false)
	private Double otrosPagos;
	
	@Column(name="idDeuda")
	private Long idDeuda;
	
	@Column(name="idDeudaVueltaAtras")
	private Long idDeudaVueltaAtras;
	
	@Column(name="valRefMin")
	private Double valRefMin;
	
	@Column(name="minRec")
	private Double minRec;
	
	@Column(name="importeAntDeu")
	private Double importeAntDeu;
	
	@Column(name="saldoAntDeu")
	private Double saldoAntDeu;
	
	@Column(name="observaciones")
	private String observaciones;
	
	@Column(name="codRectificativa")
	private Integer codRectificativa;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idForDecJur")
	private ForDecJur forDecJur;
	
	@OneToMany(mappedBy="decJur", fetch=FetchType.LAZY)
	@JoinColumn(name="idDecJur")
	private List<DecJurDet> listDecJurDet;
	
	@OneToMany(mappedBy="decJur", fetch=FetchType.LAZY)
	@JoinColumn(name="idDecJur")
	private List<DecJurPag> listDecJurPag;


	// Constructores
	public DecJur(){
		super();
	}
	
	// Metodos de Clase
	public static DecJur getById(Long id) {
		return (DecJur) GdeDAOFactory.getDecJurDAO().getById(id);
	}
	
	public static DecJur getByIdNull(Long id) {
		return (DecJur) GdeDAOFactory.getDecJurDAO().getByIdNull(id);
	}
	
	public static List<DecJur> getList() {
		return (List<DecJur>) GdeDAOFactory.getDecJurDAO().getList();
	}
	
	public static List<DecJur> getListActivos() {			
		return (List<DecJur>) GdeDAOFactory.getDecJurDAO().getListActiva();
	}
	
	public static List<DecJur>getListByDeuda(Deuda deuda){
		return GdeDAOFactory.getDecJurDAO().getListByDeuda(deuda);
	}
	
	public static List<DecJur> getListByDeudaDecJurExcluir(Deuda deuda, DecJur decJur){
		return GdeDAOFactory.getDecJurDAO().getListByDeudaDecJurExcluir(deuda, decJur);
	}
	
	public static DecJur getLastByCuentaPeriodoYAnio(Cuenta cuenta, Integer periodo, Integer anio){
		return GdeDAOFactory.getDecJurDAO().getLastByCuentaPeriodoYAnio(cuenta, periodo, anio);
	}
	
	public static List<DecJur> getByDeudaFechaNovedad(Long idDeuda, Date fecha){
		return GdeDAOFactory.getDecJurDAO().getByDeudaFechaNovedad(idDeuda, fecha);
	}
	
	public static List<DecJur> getListByCuentaPeriodoYAnio(Cuenta cuenta, Integer periodo, Integer anio){
		return GdeDAOFactory.getDecJurDAO().getListByCuentaPeriodoYAnio(cuenta, periodo, anio);
	}
	// Getters y setters
	
	public Recurso getRecurso() {
		return recurso;
	}


	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}


	public Cuenta getCuenta() {
		return cuenta;
	}


	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}


	public TipDecJurRec getTipDecJurRec() {
		return tipDecJurRec;
	}


	public void setTipDecJurRec(TipDecJurRec tipDecJurRec) {
		this.tipDecJurRec = tipDecJurRec;
	}


	public OriDecJur getOriDecJur() {
		return oriDecJur;
	}


	public void setOriDecJur(OriDecJur oriDecJur) {
		this.oriDecJur = oriDecJur;
	}


	public Date getFechaPresentacion() {
		return fechaPresentacion;
	}


	public void setFechaPresentacion(Date fechaPresentacion) {
		this.fechaPresentacion = fechaPresentacion;
	}


	public Date getFechaNovedad() {
		return fechaNovedad;
	}


	public void setFechaNovedad(Date fechaNovedad) {
		this.fechaNovedad = fechaNovedad;
	}


	public Integer getPeriodo() {
		return periodo;
	}


	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}


	public Integer getAnio() {
		return anio;
	}


	public void setAnio(Integer anio) {
		this.anio = anio;
	}


	public Double getSubtotal() {
		return subtotal;
	}


	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}


	public Double getTotalPublicidad() {
		return totalPublicidad;
	}


	public void setTotalPublicidad(Double totalPublicidad) {
		this.totalPublicidad = totalPublicidad;
	}


	public Double getTotMesYSil() {
		return totMesYSil;
	}


	public void setTotMesYSil(Double totMesYSil) {
		this.totMesYSil = totMesYSil;
	}


	public Double getTotalDeclarado() {
		return totalDeclarado;
	}


	public void setTotalDeclarado(Double totalDeclarado) {
		this.totalDeclarado = totalDeclarado;
	}


	public Double getOtrosPagos() {
		return otrosPagos;
	}


	public void setOtrosPagos(Double otrosPagos) {
		this.otrosPagos = otrosPagos;
	}


	public Long getIdDeuda() {
		return idDeuda;
	}


	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}

	public Long getIdDeudaVueltaAtras() {
		return idDeudaVueltaAtras;
	}

	public void setIdDeudaVueltaAtras(Long idDeudaVueltaAtras) {
		this.idDeudaVueltaAtras = idDeudaVueltaAtras;
	}

	public List<DecJurDet> getListDecJurDet() {
		return listDecJurDet;
	}


	public void setListDecJurDet(List<DecJurDet> listDecJurDet) {
		this.listDecJurDet = listDecJurDet;
	}	


	public Double getValRefMin() {
		return valRefMin;
	}


	public void setValRefMin(Double valRefMin) {
		this.valRefMin = valRefMin;
	}


	public Double getAliPub() {
		return aliPub;
	}


	public void setAliPub(Double aliPub) {
		this.aliPub = aliPub;
	}


	public Double getAliMesYSil() {
		return aliMesYSil;
	}


	public void setAliMesYSil(Double aliMesYSil) {
		this.aliMesYSil = aliMesYSil;
	}


	public List<DecJurPag> getListDecJurPag() {
		return listDecJurPag;
	}


	public void setListDecJurPag(List<DecJurPag> listDecJurPag) {
		this.listDecJurPag = listDecJurPag;
	}


	public Double getMinRec() {
		return minRec;
	}


	public void setMinRec(Double minRec) {
		this.minRec = minRec;
	}

	public Double getImporteAntDeu() {
		return importeAntDeu;
	}

	public void setImporteAntDeu(Double importeAntDeu) {
		this.importeAntDeu = importeAntDeu;
	}

	public Double getSaldoAntDeu() {
		return saldoAntDeu;
	}

	public void setSaldoAntDeu(Double saldoAntDeu) {
		this.saldoAntDeu = saldoAntDeu;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Integer getCodRectificativa() {
		return codRectificativa;
	}

	public void setCodRectificativa(Integer codRectificativa) {
		this.codRectificativa = codRectificativa;
	}

	public ForDecJur getForDecJur() {
		return forDecJur;
	}

	public void setForDecJur(ForDecJur forDecJur) {
		this.forDecJur = forDecJur;
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
		
		// Si el estado no es "Creado" no permitimos eliminar
		if (this.getEstado().intValue() != -1 )
			addRecoverableValueError("No esta permitido eliminar este registro.");
		
		if (GenericDAO.hasReference(this, DecJurDet.class, "decJur")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				GdeError.DECJUR_LABEL, GdeError.DECJURDET_LABEL);
		}
		
		if (GenericDAO.hasReference(this, DecJurPag.class, "decJur")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				GdeError.DECJUR_LABEL, GdeError.DECJURPAG_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
	
		
		return true;
	}
	
	// Metodos de negocio
	public Deuda getDeuda(){
		return Deuda.getById(this.idDeuda);
	}
	
	
	/**
	 * Sobrecarga del metodo para soportar simulacion. 
	 * 
	 * @param persistir
	 * @throws Exception
	 */
	public void recalcularValores(boolean persistir) throws Exception{
		
		Double subtotal=0D;
		Double total=0D;
		if (getListDecJurDet()!=null){
			for (DecJurDet decJurDet: getListDecJurDet()){
				subtotal +=decJurDet.getTotalConcepto();
			}
		}
		if (this.minRec!=null){
			Double[]arrayDouble={this.minRec,subtotal};
			subtotal = NumberUtil.getMayorValor(arrayDouble);
		}
		this.subtotal=NumberUtil.truncate(subtotal,SiatParam.DEC_IMPORTE_DB);
		total = subtotal;
		if (getAliPub()!=null){
			this.totalPublicidad=NumberUtil.truncate(getAliPub()*subtotal,SiatParam.DEC_IMPORTE_DB);
			total += getAliPub()*subtotal;
		}
		if(getAliMesYSil()!=null){
			this.totMesYSil=NumberUtil.truncate(subtotal * getAliMesYSil(),SiatParam.DEC_IMPORTE_DB);
			total += subtotal * getAliMesYSil();
		}
		this.totalDeclarado=NumberUtil.truncate(total, SiatParam.DEC_IMPORTE_DB);
		
		Double totalPagos=0D;
		if (getListDecJurPag()!=null){
			for (DecJurPag decJurPag : getListDecJurPag()){
				totalPagos+=decJurPag.getImporte();
			}
		}
		
		this.otrosPagos=totalPagos;
		
		if (persistir && this.validate()){
			GdeDAOFactory.getDecJurDAO().update(this);
		}
	}
	
	/**
	 * Realiza la sumatoria de los importes de Otros Pagos segun la bandera "sumarMismoPeriodo"
	 * 
	 * Suma para los  TipPagDecJur = a 3 o distintos de 3.
	 * 
	 * @param sumarMismoPeriodo
	 * @return
	 */
	public Double getSumatoriaOtrosPagos(boolean sumarMismoPeriodo){
		Double total = 0D;
		
		for(DecJurPag decJurPag:getListDecJurPag()){
			if (sumarMismoPeriodo && (decJurPag.getTipPagDecJur().getId().longValue() == TipPagDecJur.ID_DJMISMOPERIODO.longValue()
					|| decJurPag.getTipPagDecJur().getId().longValue() == TipPagDecJur.ID_PAGO_OSIRIS.longValue())){
				total += decJurPag.getImporte();
			}
			
			if (!sumarMismoPeriodo && decJurPag.getTipPagDecJur().getId().longValue() != TipPagDecJur.ID_DJMISMOPERIODO.longValue() 
					&& decJurPag.getTipPagDecJur().getId().longValue() != TipPagDecJur.ID_PAGO_OSIRIS.longValue()){
				total += decJurPag.getImporte();
			}
		}
		
		return total;
	}
	
	/**
	 *  Realiza la sumatoria de los importes de Otros Pagos omitiendo aquellos pagos del tipo contenido en el arreglo
	 *  pasado como parametro.
	 */
	public Double getSumOtrosPagosNotInTipPagDecJur(Long[] listIdsAExcluir) {
		return GdeDAOFactory.getDecJurPagDAO().getSumOtrosPagosNotInTipPagDecJur(getId(), listIdsAExcluir);
	}

	/**
	 * Obtiene Pago declarado Afip (hecho por Osiris) asociado a la Declaracion Jurada
	 * @return
	 */
	public DecJurPag getPagoOsirisDeclarado(){
		return GdeDAOFactory.getDecJurPagDAO().getByDecJurAndTipPagDecJur(getId(), TipPagDecJur.ID_PAGO_OSIRIS);
	}
	
	/**
	 * Obtiene Retencion/Percepcion declarada Afip (hecha por Osiris) asociado a la Declaracion Jurada
	 * @return
	 */
	public DecJurPag getRetencionOsirisDeclarada(){
		return GdeDAOFactory.getDecJurPagDAO().getByDecJurAndTipPagDecJur(getId(), TipPagDecJur.ID_RETENCION_OSIRIS);
	}
	
	/**
	 * Recalcula los valores de la DecJur como el 'recalcularValores' pero solo genera un log en un string que retorna. 
	 * 
	 * @throws Exception
	 */
	public String recalcularValoresParaLog() throws Exception{
		String log = "";
		Double subtotal=0D;
		Double total=0D;
		if (getListDecJurDet()!=null){
			for (DecJurDet decJurDet: getListDecJurDet()){
				subtotal +=decJurDet.getTotalConcepto();
			}
		}
		log += "\nSumatoria total de detalle de DJ: "+NumberUtil.truncate(subtotal,SiatParam.DEC_IMPORTE_DB);
		if (this.minRec!=null){
			Double[] arrayDouble={this.minRec,subtotal};
			subtotal = NumberUtil.getMayorValor(arrayDouble);
		}
		if (this.minRec!=null){
			log += "\nMinimo del Recurso: "+NumberUtil.truncate(this.minRec,SiatParam.DEC_IMPORTE_DB);			
		}
		log += "\nSubtotal: "+NumberUtil.truncate(subtotal,SiatParam.DEC_IMPORTE_DB);
		total = subtotal;
		if (getAliPub()!=null){
			this.totalPublicidad=NumberUtil.truncate(getAliPub()*subtotal,SiatParam.DEC_IMPORTE_DB);
			total += getAliPub()*subtotal;
			log += "\nSubtotal+Alicuota Publicidad: "+NumberUtil.truncate(total,SiatParam.DEC_IMPORTE_DB);
		}
		if(getAliMesYSil()!=null){
			this.totMesYSil=NumberUtil.truncate(subtotal * getAliMesYSil(),SiatParam.DEC_IMPORTE_DB);
			total += subtotal * getAliMesYSil();
			log += "\nSubtotal+Alicuota Mesas y Sillas: "+NumberUtil.truncate(total,SiatParam.DEC_IMPORTE_DB);
		}
		log += "\nTotal Declarado: "+NumberUtil.truncate(total,SiatParam.DEC_IMPORTE_DB);
		Double totalPagos=0D;
		if (getListDecJurPag()!=null){
			for (DecJurPag decJurPag : getListDecJurPag()){
				totalPagos+=decJurPag.getImporte();
			}
		}
		log += "\nTotal Pagos: "+NumberUtil.truncate(totalPagos,SiatParam.DEC_IMPORTE_DB);
		
		return log;
	}
	
	
	// Metodos para procesar Declaraciones Juradas
	public DecJur procesarDDJJ(List<LiqDeudaVO> listDeuda, Date fechaAct, boolean persistir) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName + ": enter");

		if (this.getOriDecJur().getId().equals(OriDecJur.ID_ENVIO_OSIRIS)) {
			/* La DecJur es de Origen AFIP
			 * - Mantis #7817: si existe decjur para el periodo, impactar la deuda solo si la fecha de 
			 * presentacion es > a las fechas de presentacion de la/s decjur existentes.
			 */
			List<DecJur> listDecJurMisPer = getListByCuentaPeriodoYAnio(cuenta, periodo, anio);
			for (DecJur decJurMP : listDecJurMisPer) {
				if (DateUtil.isDateBefore(getFechaPresentacion(), decJurMP.getFechaPresentacion())) {
					//Si la fecha de presentacion es anterior, no impacto deuda
					log.debug("Existe DJ con una fecha de presentación superior para la cuenta: "+cuenta.getNumeroCuenta()+" - Anio/Periodo: "+anio+"/"+periodo);
					if (log.isDebugEnabled()) log.debug(funcName + ": exit");
					return this;
				}
			}
			
			// Vuelvo a calcular los campos totales y genero log
			log.debug(funcName + " recalcularValoresParaLog");
			if(this.getObservaciones() != null)
				this.setObservaciones(this.getObservaciones()+this.recalcularValoresParaLog());
			else
				this.setObservaciones(this.recalcularValoresParaLog());
			
			this.recalcularValores(persistir);
		}else{
			// Vuelvo a calcular los campos totales
			// y actualizamos la declaracion jurada.
			log.debug(funcName + " recalcularValores");
			this.recalcularValores(false);
		}
		
		// obtenemos el registro de deuda asociada.
		DeudaAdmin deuda = DeudaAdmin.getById(this.getIdDeuda());
		if (deuda == null){
			this.addRecoverableValueError("No se encontro deuda original en Via administrativa para el periodo " +this.getPeriodo() +", IdDeuda: " + this.getIdDeuda());
			this.addRecoverableValueError(GdeError.DECJURADAPTER_DEUDA_NOENCONTRADA);
			return this;
		}
		
		if (null == fechaAct){
			fechaAct = this.getFechaPresentacion();
		} 

		// Obtenemos la Clasificacion Deuda Rectificativa para el Recurso de la Deuda.
		RecClaDeu clasifRectif = deuda.getRecClaDeuRectificativa();

		// Actualizada importe o fechaPago de deuda o crea nuevo regitro de deuda segun otros pagos de la declaracion.
		this.procesarDeudaSegunOtrosPagos(deuda, clasifRectif, listDeuda,fechaAct, persistir);
		//Seteo procesada la declaracion jurada
		this.setEstado(Estado.ACTIVO.getId());

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return this;
	}
		
	
	/**
	 * Procesa deuda, actualiza o crea nueva segun otros pagos
	 * 
	 * @param deuda
	 * @param recClaDeu
	 * @return
	 * @throws DemodaServiceException
	 */
	private DecJur procesarDeudaSegunOtrosPagos(DeudaAdmin deuda, RecClaDeu clasifRectif, List<LiqDeudaVO> listDeuda,Date fechaAct, boolean persistir)	throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			Boolean esDJOrigenAfip = false;
			// La DJ es de origen AFIP
			if (this.getOriDecJur().getId().equals(OriDecJur.ID_ENVIO_OSIRIS)) {
				esDJOrigenAfip = true;

				//Obtengo la sumatoria de pagos asentados para el periodo fiscal declarado en la DJ
				Double totalPagosAsentados = DeudaAdmin.getTotalPagosMismoPeriodo(this.getCuenta().getId(), this.getPeriodo(), this.getAnio());

				//Obtengo los otros pagos de tipo "Pago declarado en Osiris"
				Double totalPagosDeclarados = 0D;
				DecJurPag pagDecAfip = this.getPagoOsirisDeclarado();
				if (null != pagDecAfip) 
					totalPagosDeclarados = pagDecAfip.getImporte(); 

				//Si la sumatoria de pagos asentados, no coincide con la sumatoria de pagos declarados
				if (!totalPagosAsentados.equals(0D) && totalPagosAsentados >= totalPagosDeclarados) {

					if (persistir){
						List<DecJurPag> listDJPToDelete = new ArrayList<DecJurPag>();
						for (DecJurPag decJurPag : this.getListDecJurPag()) {
							if (!decJurPag.getTipPagDecJur().getId().equals(TipPagDecJur.ID_RETENCION_OSIRIS)) {
								listDJPToDelete.add(decJurPag);
							}
						}
						for (DecJurPag decJurPag : listDJPToDelete) {
							this.getListDecJurPag().remove(decJurPag);
							GdeDAOFactory.getDecJurPagDAO().delete(decJurPag);
						}
					} 

					//Creo un Pago de tipo "Mismo Periodo"
					DecJurPag decJurPag = new DecJurPag();
					decJurPag.setDecJur(this);
					decJurPag.setFechaPago(this.getFechaPresentacion());
					decJurPag.setTipPagDecJur(TipPagDecJur.getById(TipPagDecJur.ID_DJMISMOPERIODO));
					decJurPag.setImporte(totalPagosAsentados);

					//Lo agrego a la DJ
					this.getListDecJurPag().add(decJurPag);
					if (persistir) {
						// creo el nuevo pago del mimso periodo
						GdeDAOFactory.getDecJurPagDAO().update(decJurPag);
					}
					this.recalcularValores(persistir);
				}
			}

			//Tipo de Pago Mismo Periodo
			TipPagDecJur tipPagMismoPeriodo = TipPagDecJur.getById(TipPagDecJur.ID_DJMISMOPERIODO);

			// Obtenemos una lista de TipPagDecJur
			List<TipPagDecJur> listTipPagDecJur = new ArrayList<TipPagDecJur>();	
			for (DecJurPag djp:this.getListDecJurPag()){
				listTipPagDecJur.add(djp.getTipPagDecJur());
			}
			LiqDeudaVO liqDeudaVO = null;
			DeudaAdmin nuevaDeuda = null;

			//Si la deuda esta paga O en convenio O (no es de DJ AFIP Y deuda esta impaga Y tiene otros pagos del mismo periodo)
			if (null != deuda.getFechaPago() || null != deuda.getIdConvenio() ||
					(!esDJOrigenAfip && null == deuda.getFechaPago() && (ListUtil.isInList(listTipPagDecJur, tipPagMismoPeriodo)))) {

				//Crear Deuda Rectificativa
				nuevaDeuda = this.createDeuda4DecJur(deuda, clasifRectif, persistir);

				// Agregamos la deuda original antes de crear la nueva
				liqDeudaVO = LiqDeudaBeanHelper.deudaToLiqDeudaVO(deuda, fechaAct);
				listDeuda.add(liqDeudaVO);

				if (null != nuevaDeuda) {
					// Relacionamos la Declaracion Jurada con la nueva deuda creada
					this.setIdDeuda(deuda.getId());
					this.setIdDeudaVueltaAtras(nuevaDeuda.getId());
					if (persistir) 
						GdeDAOFactory.getDecJurDAO().update(this);

					liqDeudaVO = LiqDeudaBeanHelper.deudaToLiqDeudaVO(nuevaDeuda, fechaAct);
					liqDeudaVO.setDesEstado(CREADO);
					listDeuda.add(liqDeudaVO);
				}
			}else{
				//Actualizar Deuda Original
				deuda = this.updateDeuda4DecJur(deuda,persistir);

				this.setIdDeuda(deuda.getId());
				this.setIdDeudaVueltaAtras(deuda.getId());
				if (persistir) 
					GdeDAOFactory.getDecJurDAO().update(this);

				liqDeudaVO = LiqDeudaBeanHelper.deudaToLiqDeudaVO(deuda, fechaAct);
				liqDeudaVO.setDesEstado(MODIFICADO);
				listDeuda.add(liqDeudaVO);	
			}

			deuda.passErrorMessages(this);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return this;

		} catch(Exception e){
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		}
	}
	
	/**
	 * Crea registro de deuda con clasificacion recibida para los datos de periodo de la declaracion recibida.
	 * 
	 * @param deuda
	 * @param clasifRectif
	 * @param decJur
	 * @return
	 * @throws DemodaServiceException
	 */
	private DeudaAdmin createDeuda4DecJur(DeudaAdmin deuda, RecClaDeu clasifRectif, boolean persistir) throws DemodaServiceException {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			//.Obtengo sumatoria de deuda impaga de clasificacion Original y/o Rectificativa para el periodo fiscal declarado
			Double importeDetAnt = DeudaAdmin.getTotalDeudaImpagaMismoPeriodo(this.getCuenta().getId(), this.getPeriodo(), this.getAnio());
			//.Total de Pagos del mismo periodo
			Double impPagMisPer = this.getSumatoriaOtrosPagos(true);

			Double importeARestar = 0D;
			//Si el importe determinado anterior es mayor a total pagos del mismo periodo  
			if (importeDetAnt > impPagMisPer) {
				importeARestar = importeDetAnt;
			}else {
				importeARestar = impPagMisPer;
			}

			//Si el Total Declarado menos el importe a restar es negativo.
			if (this.getTotalDeclarado() - importeARestar <= 0) {
				// No creo deuda
				return null;	
			}

			//Determino importe y saldo
			Double importe = this.getTotalDeclarado() - importeARestar;
			Double saldo = importe - this.getSumatoriaOtrosPagos(false);

			// Setea los datos en la deudaAdmin que se va a crear, con la lista de conceptos 
			DeudaAdmin deudaAdmin = new DeudaAdmin();
			deudaAdmin.setRecurso(deuda.getRecurso());
			deudaAdmin.setEstadoDeuda(EstadoDeuda.getById(EstadoDeuda.ID_ADMINISTRATIVA));
			deudaAdmin.setViaDeuda(ViaDeuda.getById(ViaDeuda.ID_VIA_ADMIN));
			deudaAdmin.setFechaEmision(new Date());
			deudaAdmin.setEstaImpresa(SiNo.NO.getId());
			deudaAdmin.setSistema(deuda.getSistema());

			// Segun bandera simulacion						
			if (persistir)       
				deudaAdmin.setCodRefPag(GdeDAOFactory.getDeudaDAO().getNextCodRefPago());

			deudaAdmin.setAnio(deuda.getAnio());
			deudaAdmin.setPeriodo(deuda.getPeriodo());
			deudaAdmin.setCuenta(deuda.getCuenta());
			deudaAdmin.setFechaVencimiento(deuda.getFechaVencimiento());
			deudaAdmin.setRecClaDeu(clasifRectif);
			deudaAdmin.setReclamada(SiNo.NO.getId());
			deudaAdmin.setResto(1L); // Se graba con resto distinto de cero para evitar problemas de Asentamiento de la Deuda Original migrada. (Fix Mantis 5077)
			deudaAdmin.setEstado(Estado.ACTIVO.getId());
			deudaAdmin.setAtrAseVal(deuda.getAtrAseVal());
			deudaAdmin.setImporte(importe);
			deudaAdmin.setImporteBruto(importe);

			if (saldo <= 0) {
				saldo = 0D;
				deudaAdmin.setFechaPago(deuda.getFechaVencimiento());
			}
			deudaAdmin.setSaldo(saldo);

			log.debug(funcName + " importeDedua: " + deudaAdmin.getImporte());
			log.debug(funcName + " saldo: " + deudaAdmin.getSaldo());

			// Calcula el importe y setea los conceptos            
			List<DeuAdmRecCon> listDeuAdmRecCon= new ArrayList<DeuAdmRecCon>();
			//deudaAdmin.setListDeuRecCon(listDeuAdmRecCon);

			for(RecCon recCon: deuda.getRecurso().getListRecCon()){

				DeuAdmRecCon deuAdmRecConNuevo = new DeuAdmRecCon();

				deuAdmRecConNuevo.setDeuda(deudaAdmin);
				deuAdmRecConNuevo.setRecCon(recCon); 

				Double porcentajeRecCon = deuAdmRecConNuevo.getRecCon().getPorcentaje();
				Double importeDeuRecCon=NumberUtil.round(importe * porcentajeRecCon, SiatParam.DEC_IMPORTE_DB);
				deuAdmRecConNuevo.setImporte(importeDeuRecCon);
				deuAdmRecConNuevo.setImporteBruto(importeDeuRecCon);
				deuAdmRecConNuevo.setSaldo(0D);

				listDeuAdmRecCon.add(deuAdmRecConNuevo);
			}
			
			//Actualizo informacion de Pagos
			this.updateInfoPagos(deuda, persistir);

			// Segun bandera simulacion
			if (persistir)        
				deudaAdmin = GdeGDeudaManager.getInstance().createDeudaAdmin(deudaAdmin, listDeuAdmRecCon);

			if (log.isDebugEnabled()) log.debug(funcName + ": exit");

			return deudaAdmin;

		} catch(Exception e){
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		}
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * @param deuda
	 * @return
	 * @throws DemodaServiceException
	 */
	private DeudaAdmin updateDeuda4DecJur(DeudaAdmin deuda, boolean persistir) throws DemodaServiceException {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			//.Importe igual al Total Declarado en la DJ
			Double importe = this.getTotalDeclarado();
			//.Saldo igual al Total Declarado menos el Total de Otros Pagos
			Double saldo = this.getTotalDeclarado() - this.getOtrosPagos();

			// Guardo valor de deuda antes de determinar
			this.setImporteAntDeu(deuda.getImporte());
			// Guardo valor de saldo de deuda antes de determinar
			this.setSaldoAntDeu(deuda.getSaldo());
			
			//Si saldo es menor o igual a cero
			if (saldo <= 0) {
				//Cancelo la deuda
				deuda.setFechaPago(deuda.getFechaVencimiento());
				saldo = 0D;
			}

			//Seteo el importe de la deuda
			deuda.setImporte(importe);
			//Seteo el saldo de la deuda
			deuda.setSaldo(saldo);
			
			//Obtengo la lista de deuRecCon
			List<DeuAdmRecCon> listDeuRecCon = deuda.getListDeuRecCon();
			
			//Seteo el importe de los conceptos
			for (DeuAdmRecCon deuAdmRecCon : listDeuRecCon){
				Double porcentajeRecCon = deuAdmRecCon.getRecCon().getPorcentaje();
				Double importeDeuRecCon=NumberUtil.round(importe* porcentajeRecCon,SiatParam.DEC_IMPORTE_DB);
				deuAdmRecCon.setImporte(importeDeuRecCon);
				deuAdmRecCon.setImporteBruto(importeDeuRecCon);
				
				// Segun bandera simulacion				
				if(persistir)
					GdeDAOFactory.getDeuAdmRecConDAO().update(deuAdmRecCon);
			}
			
			//Armo el string de conceptos de la deuda
			deuda.setStrConceptosByListRecCon(listDeuRecCon);
		
			//Actualizo informacion de Pagos
			this.updateInfoPagos(deuda, persistir);

			// Segun bandera simulacion						
			if (persistir){
				GdeDAOFactory.getDeudaDAO().update(deuda);
				GdeDAOFactory.getDecJurDAO().update(this);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": exit");
			return deuda;
			
		} catch(Exception e){
			log.error("ServiceError en: ", e);
			throw new DemodaServiceException(e); 
		}
	}
	
	private void updateInfoPagos(DeudaAdmin deuda, boolean persistir) throws Exception {
		
		//Si la DJ es rectificativa verifico si hubo pagos registrados de declaraciones y los borro
		if (this.getTipDecJurRec().getId().equals(TipDecJurRec.ID_DJ_RECTIFICATIVA)){
			//Lista de DecJur asociadas al periodo original
			List<DecJur>listDecJur = DecJur.getListByDeudaDecJurExcluir(deuda, this);	

			//id's de TipoPago a eliminar (separados por coma)
			String idsTipoPago = TipoPago.ID_RETENCION_DECLARADA+",";
				   idsTipoPago+= TipoPago.ID_PAGO_DECLARADO_AFIP+",";
				   idsTipoPago+= TipoPago.ID_RETENCION_DECLARADA_AFIP+",";
				   //-->Candidatos a desaparecer
				   idsTipoPago+= TipoPago.ID_PERCEPCION_DECLARADA+",";
				   idsTipoPago+= TipoPago.ID_CAMBIO_DE_COEFICIENTE+",";
				   idsTipoPago+= TipoPago.ID_POR_RESOLUCION+",";
				   idsTipoPago+= TipoPago.ID_RESTO_PERIODO_ANTERIOR;
				   //<--
			// Segun bandera simulacion
			if (persistir)
				GdeDAOFactory.getPagoDeudaDAO().deletePagosDDJJ(listDecJur,idsTipoPago);
		}
		
		// Por cada Pago que no sea del mismo periodo, guarda un PagoDeuda en periodo original
		for (DecJurPag decJurPag : this.getListDecJurPag()){
			Long idTipoPago = decJurPag.getTipPagDecJur().getId().longValue();
			log.debug("id tipo pago: "+idTipoPago);
			
			if (idTipoPago != TipPagDecJur.ID_DJMISMOPERIODO){
				PagoDeuda pagoDeuda = new PagoDeuda();
				Date fechaPago = decJurPag.getFechaPago();
				if(null == fechaPago) 
					fechaPago = this.getFechaPresentacion();
				pagoDeuda.setFechaPago(fechaPago);
				pagoDeuda.setIdDeuda(this.getIdDeuda());
				pagoDeuda.setIdPago(this.getId());
				pagoDeuda.setImporte(decJurPag.getImporte());
				pagoDeuda.setActualizacion(0D);
				pagoDeuda.setEsPagoTotal(0);
				
				log.debug("creando pago deuda");
				pagoDeuda.setTipoPago(TipoPago.getByIdTipPagDecJur(idTipoPago));
				// Segun bandera simulacion						
				if (persistir)
					GdeDAOFactory.getPagoDeudaDAO().update(pagoDeuda);
			}
		}
		
	}
}
