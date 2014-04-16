//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.emi.buss.bean.CdMCuota;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.pad.buss.bean.Repartidor;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a Obra
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_obra")
public class Obra extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	public static final String ID_OBRA = "idObra";	
	public static final String FECHA_VENCIMIENTO = "fechaVencimiento";	

	@Column(name = "desObra")
	private String desObra;
	
	@Column(name = "numeroObra")
	private Integer numeroObra;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idEstadoObra")	
	private EstadoObra estadoObra;	// CREADA - A EMITIR - SUSPENDIDA - EMITIDA -ANULADA
	
	@Column(name = "permiteCamPlaMay")
	private Integer permiteCamPlaMay;
	
	@Column(name = "esPorValuacion")
	private Integer esPorValuacion;

	@Column(name = "esCostoEsp")
	private Integer esCostoEsp;

	@Column(name = "costoEsp")
	private Double costoEsp;

    @Column(name="idCaso") 
	private String idCaso;

     //Leyendas
    @Column(name="leyCon") 
	private String leyCon;

    @Column(name="leyPriCuo") 
	private String leyPriCuo;

    @Column(name="leyResCuo") 
	private String leyResCuo;

    @Column(name="leyCamPla") 
	private String leyCamPla;

   	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idObra")
	@OrderBy(clause="cantCuotas")
	private List<ObraFormaPago> listObraFormaPago;

	@OneToMany(mappedBy="obra",fetch=FetchType.LAZY)
	@JoinColumn(name="idObra")
	@OrderBy(clause="numeroCuadra")
	private List<PlanillaCuadra> listPlanillaCuadra;
	
	@OneToMany(mappedBy="obra", fetch=FetchType.LAZY)
	@JoinColumn(name="idObra")
	private List<HisEstadoObra> listHisEstadoObra;
	
	@OneToMany(mappedBy="obra", fetch=FetchType.LAZY)
	@JoinColumn(name="idObra")
	private List<ObrRepVen> listObrRepVen;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idRecurso") 
	private Recurso recurso;
	
	@Transient
	private Integer numCuaActual = null;
	
	// Constructores
	public Obra(){
		super();
		// Seteo de valores default	
	}
	
	public Obra(Long id){
		super();
		setId(id);
	}

	// Metodos de Clase
	public static Obra getById(Long id) {
		return (Obra) RecDAOFactory.getObraDAO().getById(id);
	}
	
	public static Obra getByIdNull(Long id) {
		return (Obra) RecDAOFactory.getObraDAO().getByIdNull(id);
	}
	
	public static Obra getByNumero(Integer numeroObra) {
		return (Obra) RecDAOFactory.getObraDAO().getByNumero(numeroObra);
	}
	
	public static List<Obra> getListByRecursoYEstado(Long idRecurso, Long idEstadoObra) {
		return (ArrayList<Obra>) RecDAOFactory.getObraDAO().getListByRecursoYEstado(idRecurso,idEstadoObra );
	}
	
	public static List<Obra> getList() {
		return (ArrayList<Obra>) RecDAOFactory.getObraDAO().getList();
	}
	
	public static List<Obra> getListByEstado(Long idEstadoObra) {
		return RecDAOFactory.getObraDAO().getListByEstado(idEstadoObra);
	}
	
	public static List<Obra> getListActivos() {
		return (ArrayList<Obra>) RecDAOFactory.getObraDAO().getListActiva();
	}

	public static List<Obra> getListEstadoNoCreada() {			
		return (List<Obra>) RecDAOFactory.getObraDAO().getListObraEstadoNoCreada();
	}
	
	/**
	 * Retorna una lista con las obras que: se encuentran anuladas, tienen planillas anuladas 
	 * o tienen cuentas anuladas.
	 * Ordenada por numero de obra.
	 * 
	 * @return List<Obra>
	 * */
	public static List<Obra> getListEnAnulacion() {			
		return (List<Obra>) RecDAOFactory.getObraDAO().getListEnAnulacion();
	}
	
	/** Calcula de fecha de gracia para
	 *  la fecha pasada como parametro
	 * 
	 * @return
	 */
	public static Date calcularFechaGracia(Date fecha) {
		
		final int DIAS_GRACIA = 10; 
		// obtengo el mes para obtener el siguiente
		Calendar fechaVto = new GregorianCalendar();
		fechaVto.setTime(fecha);

		Calendar fechaGracia = new GregorianCalendar();
		int mes = DateUtil.getMes(fecha);
		
		// si el mes es menor a 12
		if (mes < 12) {
			// subo un mes
			mes = mes + 1;
			fechaGracia.set(fechaVto.get(Calendar.YEAR), fechaVto.get(Calendar.MONTH), DIAS_GRACIA);
		}
		
		// si cae justo en el mes 12
		if (mes == 12) {
			fechaGracia.set(fechaVto.get(Calendar.YEAR) + 1, 1, DIAS_GRACIA);			
		}
		
		return DateUtil.getDateFromDate( fechaGracia.getTime() );
	}

	/** Calcula el importe de una cuota, de acuerdo a un
	 *  total una cantidad de cuptas y un interes
	 *  pasados como parametro, para esto usa un
	 *  interes frances un poco modificado
	 * 
	 * @param totalImporte
	 * @param cantCuotas
	 * @param interesFinaciero
	 * @return
	 */
	public static Double calcularImporteCuota(Double totalImporte, Integer cantCuotas, Double interesFinaciero) {

		Double cuotaBase = null; 
		
		if (interesFinaciero == 0D) {
			cuotaBase = totalImporte / cantCuotas;
		}
		
		else {
			Double numerador = totalImporte * interesFinaciero;
			Double denominador = 1 - Math.pow((interesFinaciero + 1), (cantCuotas * -1));
			
			cuotaBase =  numerador / denominador;
		}
		
		return cuotaBase;
	}
	
	/** Devuelve un lista de cuotas
	 *  para un totalImporte, cantCuotas, 
	 *  interesFinaciero, fechaVtoPrimerCuota 
	 * 
	 * @param totalImporte
	 * @param cantCuotas
	 * @param interesFinaciero
	 * @param fechaVtoPrimerCuota
	 * @return
	 */
	public static List<CdMCuota> calcularCuotas(Double importeCuotas, Integer cantCuotas, 
		Double interesFinaciero, Date fechaVtoPrimerCuota) {

		List<CdMCuota> listCdMCuota = new ArrayList<CdMCuota>();

		for (int i=1; i<= cantCuotas; i++) {
			
			CdMCuota cdMCuota = new CdMCuota();
			Double capital = null;
			Double interes = null;
			Date fechaVto = null;
			
			if (interesFinaciero > 0) {
				capital = (importeCuotas / (Math.pow((1 + interesFinaciero), cantCuotas))) 
									* (Math.pow(1 + interesFinaciero, i - 1 ));
				interes = importeCuotas - capital;
				// si estoy en la primer cuota
				if (i == 1) {
					fechaVto = fechaVtoPrimerCuota;
				}
				// si estoy en una cuota posterior
				if (i > 1) {
					fechaVto = DateUtil.addMonthsToDate(fechaVtoPrimerCuota, i - 1);
				}

			}

			if (interesFinaciero == 0) {
				capital = importeCuotas;
				interes = 0D;
				fechaVto = DateUtil.addMonthsToDate(fechaVtoPrimerCuota, i - 1);
			}

			cdMCuota.setCapital(capital);
			cdMCuota.setInteres(interes);
			cdMCuota.setFechaVto(fechaVto);
			cdMCuota.setFechaGracia(Obra.calcularFechaGracia(fechaVto));
			
			// agrego la cuota a la lista
			listCdMCuota.add(cdMCuota);

		}

		return listCdMCuota;
	}

	// Getters y setters
	public String getIdCaso() {
		return idCaso;
	}

	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public String getDesObra() {
		return desObra;
	}

	public void setDesObra(String desObra) {
		this.desObra = desObra;
	}

	public Integer getPermiteCamPlaMay() {
		return permiteCamPlaMay;
	}

	public void setPermiteCamPlaMay(Integer permiteCamPlaMay) {
		this.permiteCamPlaMay = permiteCamPlaMay;
	}

	public EstadoObra getEstadoObra() {
		return estadoObra;
	}

	public void setEstadoObra(EstadoObra estadoObra) {
		this.estadoObra = estadoObra;
	}

	public Integer getNumeroObra() {
		return numeroObra;
	}

	public void setNumeroObra(Integer numeroObra) {
		this.numeroObra = numeroObra;
	}

	public List<ObraFormaPago> getListObraFormaPago() {
		return listObraFormaPago;
	}

	public void setListObraFormaPago(List<ObraFormaPago> listObraFormaPago) {
		this.listObraFormaPago = listObraFormaPago;
	}
	
	public List<PlanillaCuadra> getListPlanillaCuadra() {
		return listPlanillaCuadra;
	}

	public void setListPlanillaCuadra(List<PlanillaCuadra> listPlanillaCuadra) {
		this.listPlanillaCuadra = listPlanillaCuadra;
	}
	
	public List<HisEstadoObra> getListHisEstadoObra() {
		return listHisEstadoObra;
	}

	public void setListHisEstadoObra(List<HisEstadoObra> listHisEstadoObra) {
		this.listHisEstadoObra = listHisEstadoObra;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Double getCostoEsp() {
		return costoEsp;
	}

	public void setCostoEsp(Double costoEsp) {
		this.costoEsp = costoEsp;
	}

	public Integer getEsCostoEsp() {
		return esCostoEsp;
	}

	public void setEsCostoEsp(Integer esCostoEsp) {
		this.esCostoEsp = esCostoEsp;
	}

	public Integer getEsPorValuacion() {
		return esPorValuacion;
	}

	public void setEsPorValuacion(Integer esPorValuacion) {
		this.esPorValuacion = esPorValuacion;
	}
	
	public String getLeyCamPla() {
		return leyCamPla;
	}

	public void setLeyCamPla(String leyCamPla) {
		this.leyCamPla = leyCamPla;
	}

	public String getLeyCon() {
		return leyCon;
	}

	public void setLeyCon(String leyCon) {
		this.leyCon = leyCon;
	}

	public String getLeyPriCuo() {
		return leyPriCuo;
	}

	public void setLeyPriCuo(String leyPriCuo) {
		this.leyPriCuo = leyPriCuo;
	}

	public String getLeyResCuo() {
		return leyResCuo;
	}

	public void setLeyResCuo(String leyResCuo) {
		this.leyResCuo = leyResCuo;
	}

	public List<ObrRepVen> getListObrRepVen() {
		return listObrRepVen;
	}

	public void setListObrRepVen(List<ObrRepVen> listObrRepVen) {
		this.listObrRepVen = listObrRepVen;
	}


	public Integer getNumCuaActual() {
		// Si es la primera vez, lo inicializamos
		if (this.numCuaActual == null) {
			
			Integer numCua = RecDAOFactory.getPlanillaCuadraDAO().getMaxNumCua(this.getId());
			
			this.numCuaActual = (numCua != null) ? numCua : 0;
		}
		
		// Incrementamos
		this.numCuaActual = this.numCuaActual + 1;
		
		return this.numCuaActual;
	}

	//	 Metodos de negocio

	// Metodos de Forma de Pago
	public ObraFormaPago createObraFormaPago(ObraFormaPago obraFormaPago) throws Exception {

		// Validaciones 
		if (!obraFormaPago.validateCreate()) {
			return obraFormaPago;
		}

		//Si los datos estan completos, generamos la descripcion
		obraFormaPago.setDesFormaPago(null);
		
		obraFormaPago.setObra(this);
		RecDAOFactory.getObraFormaPagoDAO().update(obraFormaPago);

		return obraFormaPago;
	}
	
	public ObraFormaPago updateObraFormaPago(ObraFormaPago obraFormaPago) throws Exception {
		// Validaciones 
		if (!obraFormaPago.validateUpdate()) {
			return obraFormaPago;
		}
		
		//Si los datos estan completos, generamos la descripcion
		obraFormaPago.setDesFormaPago(null);

		RecDAOFactory.getObraFormaPagoDAO().update(obraFormaPago);

		return obraFormaPago;
	}

	public ObraFormaPago deleteObraFormaPago(ObraFormaPago obraFormaPago) throws Exception {
		// Validaciones 
		if (!obraFormaPago.validateDelete()) {
			return obraFormaPago;
		}

		RecDAOFactory.getObraFormaPagoDAO().delete(obraFormaPago);

		return obraFormaPago;
	}

	//metodos de planilla cuadra
	public PlanillaCuadra agregarPlanillaCuadra(PlanillaCuadra planillaCuadra) throws Exception {

		// Validaciones 
		if (!this.validateAgregarPlanillaCuadra(planillaCuadra)) {
			return planillaCuadra;
		}

		planillaCuadra.setObra(this);
		planillaCuadra.setNumeroCuadra(getNumCuaActual());
		
		RecDAOFactory.getPlanillaCuadraDAO().update(planillaCuadra);

		return planillaCuadra;
	}

	public PlanillaCuadra quitarPlanillaCuadra(PlanillaCuadra planillaCuadra) throws Exception {

		// Validaciones 
		if (!this.validateQuitarPlanillaCuadra(planillaCuadra)) {
			return planillaCuadra;
		}

		//Limpiamos el Historial de Repartidores de la Planilla
		for(HisObrRepPla hisObrRepPla: planillaCuadra.getListHisObrRepPla())
			planillaCuadra.deleteHisObrRepPla(hisObrRepPla);
		
		planillaCuadra.setObra(null);
		RecDAOFactory.getPlanillaCuadraDAO().update(planillaCuadra);

		return planillaCuadra;
	}

	// Metodos de ObrRepVen
	public ObrRepVen createObrRepVen(ObrRepVen obrRepVen) throws Exception {
		// Validaciones 
		if (!obrRepVen.validateCreate()) {
			return obrRepVen;
		}

		obrRepVen.setObra(this);
		
		// Reprogramamos la deuda
		obrRepVen.reprogramarVencimientos();
		
		// Actualizamos los datos
		RecDAOFactory.getObrRepVenDAO().update(obrRepVen);

		return obrRepVen;
	}
	
	/**
	 * Cambia el estado de la obra 
	 *
	 */
	public void cambiarEstado(EstadoObra estadoObra) 
		throws Exception {

		if(!this.validateCambioEstado(estadoObra)){
			return;
		}
		
		if (estadoObra.getId().equals(EstadoObra.ID_ACTIVA)) {
			for (PlanillaCuadra planilla: this.getListPlanillaCuadra()) {
				planilla.cambiarEstado(EstPlaCua.getById(EstPlaCua.ID_EMITIDA), "La planilla ha sido emitida");
			}
		} 
			

		this.setEstadoObra(estadoObra);
		RecDAOFactory.getObraDAO().update(this);

		// creo el historico
		HisEstadoObra hisEstadoObra = this.createHisEstadoObra(estadoObra, "");

		// copio la lista de errores
		this.getListError().addAll(hisEstadoObra.getListError());

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
	
		if(!ListUtil.isNullOrEmpty(this.getListObraFormaPago())){
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, RecError.OBRA_LABEL, RecError.OBRAFORMAPAGO_LABEL);
		}
		
		if (GenericDAO.hasReference(this, PlanillaCuadra.class, "obra")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					RecError.OBRA_LABEL , RecError.PLANILLACUADRA_LABEL);
		}	
		
		if (GenericDAO.hasReference(this, ObrRepVen.class, "obra")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					RecError.OBRA_LABEL , RecError.OBRREPVEN_LABEL);
		}	
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	/**
	 * Valida el cambio de estado
	 * @return boolean
	 */
	private boolean validateCambioEstado(EstadoObra estadoObra) throws Exception {
		//limpiamos la lista de errores
		clearError();

		if (estadoObra == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.ESTADOOBRA_LABEL);
		}

		//Si la obra esta para emitir, verificamos que la informacion catastral este completa
		if ((estadoObra != null) && (estadoObra.getId().equals(EstadoObra.ID_A_EMITIR))) {
			
			// Verificamos que tenga formas de pago y planillas cargadas
			if (ListUtil.isNullOrEmpty(this.getListPlanillaCuadra()))
				addRecoverableError(RecError.OBRA_SIN_PLANILLAS); 

			for (PlanillaCuadra planilla: this.getListPlanillaCuadra()) { 
				//Validamos que la informacion cargada por catastro para esta Obra 
				//este completa
				if (!planilla.isInfoCatastralCompleta(this))
					addRecoverableError(RecError.PLANILLACUADRA_INFO_CATASTRAL_NO_COMPLETA, 
								planilla.getId().toString());
				//Validamos que la planilla este disponibilizada a emision
				if (!planilla.getEstPlaCua().getId().equals(EstPlaCua.ID_ENVIADA_A_EMISION))
					addRecoverableError(RecError.PLANILLACUADRA_NO_DISPONIBILIZADA_A_EMISION, 
								planilla.getId().toString());
				//Validamos que la planilla tenga un repartidor asignado
				if (planilla.getRepartidor() == null)
					addRecoverableError(RecError.PLANILLACUADRA_SIN_REPARTIDOR, 
							planilla.getId().toString());
				//Validamos que el repartidor asignado este vigente
				if (planilla.getRepartidor() != null && !planilla.getRepartidor().getEstado().equals(Estado.ACTIVO.getBussId()))
					addRecoverableError(RecError.PLANILLACUADRA_REPARTIDOR_NO_VIGENTE, 
							planilla.getId().toString());
			}
		}
		
		if (hasError()) {
			return false;
		}

		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida el agregado de una planilla
	 * @return boolean
	 */
	private boolean validateAgregarPlanillaCuadra(PlanillaCuadra planillaCuadra) {
		//limpiamos la lista de errores
		clearError();

		if (hasError()) {
			return false;
		}

		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la quita de una planilla
	 * @return boolean
	 */
	private boolean validateQuitarPlanillaCuadra(PlanillaCuadra planillaCuadra) {
		//limpiamos la lista de errores
		clearError();

		if (hasError()) {
			return false;
		}

		//Validaciones 
		return true;
	}
	
	
	private boolean validate() throws Exception {
		
		//	Validaciones   
		if (getRecurso() == null ) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);			
		}
		
		if (getNumeroObra() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRA_NUMEROOBRA);
		}
		
		// Validamos que el numero de obra se encuentre en el rango permitido
		if (getRecurso() != null && getNumeroObra() != null) {
			if (getRecurso().getEsObraGas() && (getNumeroObra() < 0 || getNumeroObra() >= 600)) {
				addRecoverableError(BaseError.MSG_RANGO_INVALIDO, RecError.OBRA_NUMEROOBRA);
			}
			if (getRecurso().getEsObraPavimento() && (getNumeroObra() < 0 || getNumeroObra() >= 400)) {
				addRecoverableError(BaseError.MSG_RANGO_INVALIDO, RecError.OBRA_NUMEROOBRA);
			}
		} 
		
		if (StringUtil.isNullOrEmpty(getDesObra())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRA_DESOBRA);
		}
		
		if (getPermiteCamPlaMay() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRA_PERMITECAMPLAMAY);			
		}
		
		if (getEsPorValuacion() == null ) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRA_ESPORVALUACION);			
		}

		if (getEsPorValuacion() != null && getEsPorValuacion().equals(SiNo.SI.getId()) 
						&& getEsCostoEsp() == null ) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRA_ESCOSTOESP);			
		}

		if (getEsCostoEsp() != null && getEsCostoEsp().equals(SiNo.SI.getId()) 
						&& getCostoEsp() == null  ) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRA_COSTOESP);			
		}
		
		if (getEsCostoEsp() != null && getEsCostoEsp().equals(SiNo.SI.getId()) 
				&& getCostoEsp() != null && getCostoEsp() < 0D) {
			addRecoverableError(BaseError.MSG_VALORMENORQUECERO, RecError.OBRA_COSTOESP);			
		}

		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addInteger("numeroObra");
		if (!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO,
					RecError.OBRA_NUMEROOBRA);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	//	---> ABM HisEstadoObra
	public HisEstadoObra createHisEstadoObra(EstadoObra estPlaCua, String descripcion) throws Exception {

		HisEstadoObra hisEstadoObra = new HisEstadoObra(estPlaCua, this, descripcion);
		hisEstadoObra = this.createHisEstadoObra(hisEstadoObra);

		return hisEstadoObra;
	}
	
	public HisEstadoObra createHisEstadoObra(HisEstadoObra hisEstadoObra) throws Exception {

		// Validaciones de negocio
		if (!hisEstadoObra.validateCreate()) {
			return hisEstadoObra;
		}

		RecDAOFactory.getHisEstadoObraDAO().update(hisEstadoObra);

		return hisEstadoObra;
	}
	
	public HisEstadoObra updateHisEstadoObra(HisEstadoObra hisEstadoObra) throws Exception {
		
		// Validaciones de negocio
		if (!hisEstadoObra.validateUpdate()) {
			return hisEstadoObra;
		}

		RecDAOFactory.getHisEstadoObraDAO().update(hisEstadoObra);
		
		return hisEstadoObra;
	}
	
	public HisEstadoObra deleteHisEstadoObra(HisEstadoObra hisEstadoObra) throws Exception {
	
		// Validaciones de negocio
		if (!hisEstadoObra.validateDelete()) {
			return hisEstadoObra;
		}
		
		RecDAOFactory.getHisEstadoObraDAO().delete(hisEstadoObra);
		
		return hisEstadoObra;
	}
	// <--- ABM HisEstadoObra
	
	/**
	 * Retorna true si y solo si la obra tiene plan Contado
	 * */
	public boolean hasPlanContado() {
		return this.getObraFormaPagoContado() != null;
	}

	/**
	 * Retorna true si y solo si la obra tiene plan Largo
	 * */
	public boolean hasPlanLargo() {
		
		for (ObraFormaPago formaPago: this.getListObraFormaPago()) {
			if (formaPago.getCantCuotas() > 1)
				return true;
		}
		
		return false;
	}

	
	/** Recupara la obra forma de pago que corresponde al plan  
	 *  contado. Si hay mas de uno, devuelve el primero
	 *  que encuentra
	 * 
	 * @return
	 */
	public ObraFormaPago getObraFormaPagoContado() {
		ObraFormaPago obraFormaPago = null;
		List<ObraFormaPago> listObraFormaPago = this.getListObraFormaPago();
		// itero hasta encontrar la de plan contado
		for (ObraFormaPago OFP:listObraFormaPago) {

			// es plan contado
			if (OFP.getCantCuotas() == 1) {
				obraFormaPago = OFP;
			}

		}

		return obraFormaPago;
	}
	
	/** Recupara la obra forma de pago que corresponde 
	 *  al plan mas largo, para un determinado monto
	 *  total a pagar, si no encuentra devuelve null
	 * 
	 * @return
	 */
	public ObraFormaPago getObraFormaPagoPlanLargo(Double totalAPagar){
		ObraFormaPago obraFormaPago = null;

		// recupera la lista de obra forma de pago de esta obra
		// ordenada en forma descendente
		List<ObraFormaPago> listObraFormaPago =
			RecDAOFactory.getObraFormaPagoDAO().getListByObraDes(this);

		// itero las formas de pago recuperadas y me fijo cual encaja
		for (ObraFormaPago OFP:listObraFormaPago) {
			if ( OFP.getCantCuotas() > 1 && OFP.isValidForMonto(totalAPagar) ) {
				obraFormaPago = OFP;
				break;
			}
		}

		return obraFormaPago;
	}

	/** Recupera todas los detalles de 
	 *  las planilla cuadra de la obra que
	 *  no sean carpeta
	 * 
	 */
	public List<PlaCuaDet> getListPlaCuaDetNoCarpetas() {
		List<PlaCuaDet> listPlaCuaDet = new ArrayList<PlaCuaDet>();
		for (PlanillaCuadra planillaCuadra:this.getListPlanillaCuadra()) {
			listPlaCuaDet.addAll(planillaCuadra.getListPlaCuaDetNoCarpetas());
		}
		return listPlaCuaDet;
	}
	
	/** Verifica que todas las planillas de la 
	 *  obra esten disponibilizadas a emision
	 * 
	 * @return
	 */
	public boolean isDisponibilizadaAEmision(){
		boolean isDisponibilizadaAEmision = true;
		
		for (PlanillaCuadra planillaCuadra:this.getListPlanillaCuadra()) {
			if (planillaCuadra.getEstPlaCua().getId().equals(EstPlaCua.ID_ENVIADA_A_EMISION)){
				isDisponibilizadaAEmision = false;
			}
		}

		return isDisponibilizadaAEmision;
	}

    public String getPermiteCamPlaMayView(){
		return SiNo.getById(this.getPermiteCamPlaMay()).getValue();
	}
    
    public String getEsPorValuacionView(){
		return SiNo.getById(this.getEsPorValuacion()).getValue();
	}
    
    public String getEsCostoEspView(){
		return SiNo.getById(this.getEsCostoEsp()).getValue();
	}
    
    //Plan Contado:
    public String getLeyConReport(){
    	String leyCon = this.getLeyCon();
    	return "Plan Contado: " + (leyCon==null?"":leyCon);
	}
    //Primera Cuota (Plan Largo):
    public String getLeyPriCuoReport(){
    	String leyPriCuo = this.getLeyPriCuo();
		return "Primera Cuota (Plan Largo): " + (leyPriCuo==null?"":leyPriCuo);
	}
    //Cuotas Restantes (Plan Largo):
    public String getLeyResCuoReport(){
    	String leyResCuo= this.getLeyResCuo();
		return "Primera Cuota (Plan Largo): " + (leyResCuo==null?"":leyResCuo);
	}
    //Cambio de Plan:
    public String getLeyCamPlaReport(){
    	String leyCamPla= this.getLeyCamPla();
		return "Cambio de Plan: " + (leyCamPla==null?"":leyCamPla);
	}

    // Devuelve true si y solo si la obra esta en estado "Suspendida".
    public boolean estaSuspendida() {
    	return this.getEstadoObra().getId().equals(EstadoObra.ID_SUSPENDIDA);
    } 

    /** Devuelve las planillas de la obra para un determinado 
     	repartidor */
    public List<PlanillaCuadra> getListPlanillaCuadraByRepartidor(Repartidor repartidor) {
    	List<PlanillaCuadra> listPlanillas = new ArrayList<PlanillaCuadra>();
    	
    	for (PlanillaCuadra p:this.getListPlanillaCuadra()) {
    		if (p.getRepartidor().equals(repartidor))
    			listPlanillas.add(p);
    	}
    
    	return listPlanillas;
    }
    
    /**
     * Devuelve la lista de Repartidores asociados a la obra
     * */
    
    public List<Repartidor> getListRepartidores() {

    	List<Repartidor> listRepartidores = new ArrayList<Repartidor>();
    	
    	// Recorremos las planillas de la obra
    	for (PlanillaCuadra planilla : this.getListPlanillaCuadra()) {
    		// Si no estaba en la lista, lo agregamos
    		if (!listRepartidores.contains(planilla.getRepartidor()))
    		listRepartidores.add(planilla.getRepartidor());
    	}
    	
    	return listRepartidores;
    }

    @Override
    public String infoString() {
    	String ret ="Obra";

		if(numeroObra!=null){
			ret+=" - Numero: "+numeroObra;
		}

		if(recurso!=null){
			ret+=" - Recurso: " + recurso.getDesRecurso(); 
		}
		
		if(desObra!=null){
			ret+=" - descripcion: "+desObra;
		}

		
		if(idCaso!=null){
			ret+=" - Caso: "+idCaso;
		}

		if(estadoObra!=null){
			ret+=" - Estado: "+estadoObra.getDesEstadoObra();
		}

		
    	return ret;
    }
    
    /**
     * Calcula la suma de los montos totales a pagar por
     * planilla 
     * */
	public Double getMontoTotal() {
		
		Double total = 0D;
		
		try {
			for (PlanillaCuadra planillaCuadra: this.getListPlanillaCuadra())
				// Calculamos el monto total a pagar por planilla
				total += planillaCuadra.getMontoTotal();
		}
		
		catch (Exception e) {
			return 0D;
		}

		return total;
	}

	public String getMontoTotalForReport() {
		return StringUtil.formatDouble(this.getMontoTotal());
	}
	
	/** 
	 * Calcula la cantidad de cuentas de la obra
	 * */
	public Integer getTotalCuentas() {
		Integer totCuentas = 0;
		try {
			for (PlanillaCuadra planillaCuadra: this.getListPlanillaCuadra())
				// Calculamos el total de cuentas por planilla
				totCuentas += planillaCuadra.getTotalCuentas(); 
		}
		
		catch (Exception e) {
			return 0;
		}
		
		return totCuentas;
	}
	
	public String getTotalCuentasForReport() {
		return StringUtil.formatInteger(this.getTotalCuentas());
	}
	
	/**
	 * Devuelve una String con las calles afectadas por la obra.
	 * Utilizada en el reporte: Informe de Obra.
	 *
	 * */
	public String getCuaEjecForReporte() {
		String cuadEjec = "";
		
		try {
			for (PlanillaCuadra planilla: this.getListPlanillaCuadra()) {
				if (planilla.hasCalles()) {
					cuadEjec += planilla.getCallePpal().getNombreCalle();
					cuadEjec += " entre " + planilla.getCalleDesde().getNombreCalle() + " y " 
										  + planilla.getCalleHasta().getNombreCalle();
				}
				else {
					cuadEjec += planilla.getDescripcion();
				}
				
				cuadEjec += ";";
			}
		}
		
		catch (Exception e) {
			return null;
		}
		
		return cuadEjec;
	}

	/**
	 * Dada la lista de Exenciones de una cuenta reciba, 
	 * checkea si al menos una de las ObraFormaPago de la Obra posee una de las exenciones recibidas.  
	 * 
	 * @param listExencion
	 * @return boolean
	 */
	public boolean poseeObraFormaPago4AlgunaExencion(List<Exencion> listExencion){
	
		for (ObraFormaPago ofp:getListObraFormaPago()){
			if (ofp.getEsEspecial() != null && ofp.getEsEspecial().intValue() == 1 &&  
					ListUtilBean.contains(listExencion, ofp.getExencion())){
				return true;
			}			
		}
		
		return false;
	}
	
}
