//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.gov.rosario.siat.bal.buss.cache.BalanceCache;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import ar.gov.rosario.siat.bal.iface.model.NodoVO;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Banco;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.TipoDeuda;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import ar.gov.rosario.siat.pro.buss.bean.Proceso;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CeldaVO;
import coop.tecso.demoda.iface.model.ContenedorVO;
import coop.tecso.demoda.iface.model.Datum;
import coop.tecso.demoda.iface.model.DemodaStringMsg;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.FilaVO;
import coop.tecso.demoda.iface.model.FormularioAfip;
import coop.tecso.demoda.iface.model.PrintModel;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TablaVO;
import coop.tecso.demoda.iface.model.TipoBoleta;

/**
 * Bean correspondiente a Proceso de Balance.
 * Se consolidan los datos de bal_diarioPartida y sincroniza con el sistema Contable.
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_balance")
public class Balance extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	public static final String COD_FRM_5018="BAL_TOT_SIS";
	public static final String COD_FRM_CONC_CAJA="BAL_CONC_CAJA";
	public static final String COD_FRM_TOT_CAJA="BAL_TOT_CAJ";
	public static final String COD_FRM_PRO_ASO="BAL_PRO_ASO";
	public static final String COD_FRM_TOT_PAR="BAL_TOT_PAR";
	public static final String COD_FRM_TOT_CUE="BAL_TOT_CUE";
	public static final String COD_FRM_TOT_INDET="BAL_TOT_INDET";
	public static final String COD_FRM_CAJA7="BAL_CAJA7";
	
	public static final String KEY_SIAT_PARAM_PARTIDA_INDET="INDET_SIN_PROCESAR";
	public static final String KEY_SIAT_PARAM_PARTIDA_CTA_PTE="CTA_PTE_DEF";
	
	@Column(name = "fechaBalance")
	private Date fechaBalance;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idEjercicio") 
	private Ejercicio ejercicio;

	@Column(name = "fechaDesde")
	private Date fechaDesde;

	@Column(name = "fechaHasta")
	private Date fechaHasta;

	@Column(name = "observacion")
	private String observacion;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name = "idCorrida")
	private Corrida corrida;

	@Column(name = "fechaAlta")
	private Date fechaAlta;

	@Column(name = "enviado")
	private Integer enviado;

	@OneToMany()
	@JoinColumn(name="idBalance")
	private List<Folio> listFolio;

	@OneToMany()
	@JoinColumn(name="idBalance")
	private List<Archivo> listArchivo;

	@OneToMany()
	@JoinColumn(name="idBalance")
	private List<Caja7> listCaja7;

	@OneToMany()
	@JoinColumn(name="idBalance")
	private List<Caja69> listCaja69;

	@OneToMany()
	@JoinColumn(name="idBalance")
	private List<TranBal> listTranBal;

	@OneToMany()
	@JoinColumn(name="idBalance")
	private List<Reingreso> listReingreso;

	@OneToMany()
	@JoinColumn(name="idBalance")
	private List<ImpPar> listImpPar;

	@OneToMany()
	@JoinColumn(name="idBalance")
	private List<Asentamiento> listAsentamiento;

	@OneToMany()
	@JoinColumn(name="idBalance")
	private List<AseDel> listAseDel;

	@OneToMany()
	@JoinColumn(name="idBalance")
	private List<IndetBal> listIndetBal;

	@OneToMany()
	@JoinColumn(name="idBalance")
	private List<SaldoBal> listSaldoBal;
	
	@OneToMany()
	@JoinColumn(name="idBalance")
	private List<DiarioPartida> listDiarioPartida;

	@OneToMany()
	@JoinColumn(name="idBalance")
	private List<Compensacion> listCompensacion;
	
	@Transient 
	private BalanceSession balSession;
	
	//Constructores 
	public Balance(){
		super();
	}

	// Getters y Setters
	public Corrida getCorrida() {
		return corrida;
	}
	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}
	public Ejercicio getEjercicio() {
		return ejercicio;
	}
	public void setEjercicio(Ejercicio ejercicio) {
		this.ejercicio = ejercicio;
	}
	public Integer getEnviado() {
		return enviado;
	}
	public void setEnviado(Integer enviado) {
		this.enviado = enviado;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public Date getFechaBalance() {
		return fechaBalance;
	}
	public void setFechaBalance(Date fechaBalance) {
		this.fechaBalance = fechaBalance;
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
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public List<ImpPar> getListImpPar() {
		return listImpPar;
	}
	public void setListImpPar(List<ImpPar> listImpPar) {
		this.listImpPar = listImpPar;
	}
	public List<Folio> getListFolio() {
		return listFolio;
	}
	public void setListFolio(List<Folio> listFolio) {
		this.listFolio = listFolio;
	}
	public List<Archivo> getListArchivo() {
		return listArchivo;
	}
	public void setListArchivo(List<Archivo> listArchivo) {
		this.listArchivo = listArchivo;
	}
	public List<Caja7> getListCaja7() {
		return listCaja7;
	}
	public void setListCaja7(List<Caja7> listCaja7) {
		this.listCaja7 = listCaja7;
	}
	public List<Reingreso> getListReingreso() {
		return listReingreso;
	}
	public void setListReingreso(List<Reingreso> listReingreso) {
		this.listReingreso = listReingreso;
	}
	public BalanceSession getBalSession() {
		return balSession;
	}
	public void setBalSession(BalanceSession balSession) {
		this.balSession = balSession;
	}
	public List<Caja69> getListCaja69() {
		return listCaja69;
	}
	public void setListCaja69(List<Caja69> listCaja69) {
		this.listCaja69 = listCaja69;
	}
	public List<TranBal> getListTranBal() {
		return listTranBal;
	}
	public void setListTranBal(List<TranBal> listTranBal) {
		this.listTranBal = listTranBal;
	}
	public List<AseDel> getListAseDel() {
		return listAseDel;
	}
	public void setListAseDel(List<AseDel> listAseDel) {
		this.listAseDel = listAseDel;
	}
	public List<Asentamiento> getListAsentamiento() {
		return listAsentamiento;
	}
	public void setListAsentamiento(List<Asentamiento> listAsentamiento) {
		this.listAsentamiento = listAsentamiento;
	}
	public List<IndetBal> getListIndetBal() {
		return listIndetBal;
	}
	public void setListIndetBal(List<IndetBal> listIndetBal) {
		this.listIndetBal = listIndetBal;
	}
	public List<SaldoBal> getListSaldoBal() {
		return listSaldoBal;
	}
	public void setListSaldoBal(List<SaldoBal> listSaldoBal) {
		this.listSaldoBal = listSaldoBal;
	}
	public List<DiarioPartida> getListDiarioPartida() {
		return listDiarioPartida;
	}
	public void setListDiarioPartida(List<DiarioPartida> listDiarioPartida) {
		this.listDiarioPartida = listDiarioPartida;
	}
	public List<Compensacion> getListCompensacion() {
		return listCompensacion;
	}
	public void setListCompensacion(List<Compensacion> listCompensacion) {
		this.listCompensacion = listCompensacion;
	}

	// Metodos de clase	
	public static Balance getById(Long id) {
		return (Balance) BalDAOFactory.getBalanceDAO().getById(id);
	}
	
	public static Balance getByIdNull(Long id) {
		return (Balance) BalDAOFactory.getBalanceDAO().getByIdNull(id);
	}
	
	public static List<Balance> getList() {
		return (ArrayList<Balance>) BalDAOFactory.getBalanceDAO().getList();
	}
	
	public static List<Balance> getListActivos() {			
		return (ArrayList<Balance>) BalDAOFactory.getBalanceDAO().getListActiva();
	}
	
	public List<String> exportReportesIndeterminados(String fileDir) throws Exception{
		return BalDAOFactory.getBalanceDAO().exportReportesIndeterminadosByBalance(this, fileDir);
	}
	
	/**
	 *  Verifica si existe un Balanace con estado "En Preparacion", 
	 *  "En espera comenzar", "Procesando" o "En espera continuar". Retorna true o false.
	 *  (Excluyendo al Balance que realiza la consulta)
	 * @return boolean
	 * @throws Exception
	 */
	public boolean existBalanceSinTerminar() throws Exception{
		return BalDAOFactory.getBalanceDAO().existBalanceSinTerminar(this);
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
		if(getFechaBalance()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.BALANCE_FECHABALANCE);
		}
		if(getEjercicio()==null &&	(getFechaDesde() == null || getFechaHasta() == null)){
			addRecoverableError(BalError.BALANCE_EJERCICIO_O_INTERVALO_NO_ENCONTRADO);
		}
		if(getCorrida()==null){
			addRecoverableError(BalError.BALANCE_CORRIDA_NO_GENERADA);
		}
		
		if (hasError()) {
			return false;
		}

		if(this.existBalanceSinTerminar()){
			addRecoverableError(BalError.BALANCE_EXISTENTE);
		}
		
		if(this.getEjercicio().getEstEjercicio().getId().longValue() == EstEjercicio.ID_FUTURO){
			addRecoverableError(BalError.BALANCE_EJERCICIO_FUTURO);
		}
		
		// Valida que la Fecha Balance no sea mayor que la fecha Actual
		if(!DateUtil.isDateBeforeOrEqual(this.fechaBalance, new Date())){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, BalError.BALANCE_FECHABALANCE, BaseError.MSG_FECHA_ACTUAL);
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
		
		if (GenericDAO.hasReference(this, ImpPar.class, "balance")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.BALANCE_LABEL , BalError.IMPPAR_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	// Administracion de ImpPar
	public ImpPar createImpPar(ImpPar impPar) throws Exception {
		
		// Validaciones de negocio
		if (!impPar.validateCreate()) {
			return impPar;
		}

		BalDAOFactory.getImpParDAO().update(impPar);
		
		return impPar;
	}	

	public ImpPar updateImpPar(ImpPar impPar) throws Exception {
		
		// Validaciones de negocio
		if (!impPar.validateUpdate()) {
			return impPar;
		}

		BalDAOFactory.getImpParDAO().update(impPar);
		
		return impPar;
	}	

	public ImpPar deleteImpPar(ImpPar impPar) throws Exception {
		
		// Validaciones de negocio
		if (!impPar.validateDelete()) {
			return impPar;
		}
				
		BalDAOFactory.getImpParDAO().delete(impPar);
		
		return impPar;
	}	

	// Metodos Relacionados con el Proceso de Balance
	
	/**
	 * Valida condiciones necesarias para comenzar el proceso.
	 * (si falla alguna condicion agrega un error)
	 * <i>(paso 1.1)</i>
	 */
	public void validarConfiguracion() throws Exception{
		// Validar si el estado del Ejercicio asociado es distinto de Futuro
		if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_FUTURO){
			this.addRecoverableValueError("La Fecha de balance corresponde a un ejercicio Futuro");
		}
		// Si es necesario alguna otra validacion agregar a continuacion.
	}

	/**
	 *  Crea los Ajustes de Partidas en Caja 7 para las Cuentas Puentes, para las descarga de Indeterminados y Duplices
	 *  por los Reingresos, y para compensaciones cuando corresponda.
	 * 
	 * <i>(paso 1.2)</i>
	 */
	public void prepararCaja7() throws Exception{
		// Recorremos los Folios incluidos 
		for(Folio folio: this.getListFolio()){
			// Por cada fila de la tabla (correspondiente a un dia de cobranza)
			for(FolDiaCob folDiaCob: folio.getListFolDiaCob()){
				// Recorremos las columnas, correspondientes a los importes para cada 'tipoCob'
				for(FolDiaCobCol folDiaCobCol: folDiaCob.getListFolDiaCobCol()){
					// Creamos un Ajuste de Caja 7 que carga importe a una Partida de Cuenta Puente
					if(!NumberUtil.isDoubleEqualToDouble(folDiaCobCol.getImporte(), 0.0, 0.001)){
						Caja7 caja7 = new Caja7();
						
						caja7.setBalance(this);
						caja7.setFecha(this.getFechaBalance()); 
						caja7.setPartida(folDiaCobCol.getTipoCob().getPartida());
						caja7.setImporteEjeAct(folDiaCobCol.getImporte());
						caja7.setImporteEjeVen(0D);
						caja7.setDescripcion("Origen: Día de Cobranza");
						String diaCob = "";
						if(folDiaCob.getFechaCob() != null)
							diaCob = DateUtil.formatDate(folDiaCob.getFechaCob(), DateUtil.ddSMMSYYYY_MASK);
						else
							diaCob = folDiaCob.getDescripcion();
						caja7.setObservacion("Ajuste generado por valor de Día de Cobranza: "+diaCob
								+", incluido en Folio nro: "+folio.getNumero());
						
						BalBalanceManager.getInstance().createCaja7(caja7);												
					}
				}
			}
			
			// Por cada 'Otro Ingreso de Tesoreria'
			for(OtrIngTes otrIngTes: folio.getListOtrIngTes()){
				// Por cada OtrIngTesPar (asignacion a partida)
				for(OtrIngTesPar otrIngTesPar: otrIngTes.getListOtrIngTesPar()){
					// Creamos un Ajuste de Caja 7 que carga importe a una Partida
					Caja7 caja7 = new Caja7();
					
					caja7.setBalance(this);
					caja7.setFecha(otrIngTes.getFechaOtrIngTes());
					caja7.setPartida(otrIngTesPar.getPartida());
					if(otrIngTesPar.getEsEjeAct() == null || otrIngTesPar.getEsEjeAct().intValue() == SiNo.SI.getId().intValue()){
						caja7.setImporteEjeAct(otrIngTesPar.getImporte());
						caja7.setImporteEjeVen(0D);
					}else{
						caja7.setImporteEjeAct(0D);
						caja7.setImporteEjeVen(otrIngTesPar.getImporte());
					}
					caja7.setDescripcion("Origen: Otro Ingreso de Tesorería");
					caja7.setObservacion("Ajuste generado por distribución de el Otro Ingreso de Tesorería " +
							"de descripcion: "+otrIngTes.getDescripcion()+", incluido en Folio nro: "+folio.getNumero());
					
					BalBalanceManager.getInstance().createCaja7(caja7);	
				}
			}
			
			// Por cada Compensacion (FolCom)
			for(FolCom folCom: folio.getListFolCom()){
				// Si tiene una compensacion se intenta crea un ajuste para los Saldos
				if(folCom.getCompensacion() != null){
					// Recorremos los Saldos a Favor que se cancelan en la Compensacion
					for(SaldoAFavor saldoAFavor: folCom.getCompensacion().getListSaldoAFavor()){
						// 	Se averigua si corresponde a un Ejercicio Actual o Vencido.
						Ejercicio ejercicio = this.getEjercicio();
						boolean esEjercicioActual = false;
						if(DateUtil.isDateAfterOrEqual(saldoAFavor.getFechaGeneracion(), ejercicio.getFecIniEje())
								&& DateUtil.isDateBeforeOrEqual(saldoAFavor.getFechaGeneracion(), ejercicio.getFecFinEje())){
							esEjercicioActual = true;
						}
						// Si el Saldo A Favor tiene la partida de la cual debe descargarse, se crea el ajuste.
						if(saldoAFavor.getPartida() != null){
							// Creamos un Ajuste de Caja 7 que descarga importe de una Partida.
							Caja7 caja7 = new Caja7();
							
							caja7.setBalance(this);
							caja7.setFecha(saldoAFavor.getFechaGeneracion());
							caja7.setPartida(saldoAFavor.getPartida());
							if(esEjercicioActual){
								caja7.setImporteEjeAct(saldoAFavor.getImporte()*(-1));
								caja7.setImporteEjeVen(0D);
							}else{
								caja7.setImporteEjeAct(0D);
								caja7.setImporteEjeVen(saldoAFavor.getImporte()*(-1));
							}
							caja7.setDescripcion("Origen: Saldo A Favor en Compensacion");
							caja7.setObservacion("Ajuste generado por Saldo a Favor de Cuenta: "+saldoAFavor.getCuenta().getNumeroCuenta()
															+", en Compensacion de descripcion: "+folCom.getCompensacion().getDescripcion()
															+", incluido en Folio nro: "+folio.getNumero());
							
							BalBalanceManager.getInstance().createCaja7(caja7);		
						}else{
							String warning = "Advertencia!: No se creo Ajuste de Caja 7 para el Saldo a Favor de id "
								+saldoAFavor.getId()+" de la Compensacion de id "+folCom.getCompensacion().getId()
								+", incluido en Folio nro: "+folio.getNumero();
							AdpRun.currentRun().logDebug(warning);
							this.getBalSession().addWarning(warning);
						}
					}
				}else{
					String warning = "Advertencia!: No se crearon Ajustes de Caja 7 para Saldos de Compensación. No se encontró Compensación asociada a el registro de 'Compensación de Folio' de id "+folCom.getId()
					+", incluido en Folio nro: "+folio.getNumero();
					AdpRun.currentRun().logDebug(warning);
					this.getBalSession().addWarning(warning);
				}
			}
		}
		
		// Por cada Compensacion incluida en Balance (Mantis 5413)
		for(Compensacion compensacion: this.getListCompensacion()){
			// Recorremos los Saldos a Favor que se cancelan en la Compensacion
			for(SaldoAFavor saldoAFavor: compensacion.getListSaldoAFavor()){
				// 	Se averigua si corresponde a un Ejercicio Actual o Vencido.
				Ejercicio ejercicio = this.getEjercicio();
				boolean esEjercicioActual = false;
				if(DateUtil.isDateAfterOrEqual(saldoAFavor.getFechaGeneracion(), ejercicio.getFecIniEje())
						&& DateUtil.isDateBeforeOrEqual(saldoAFavor.getFechaGeneracion(), ejercicio.getFecFinEje())){
					esEjercicioActual = true;
				}
				// Si el Saldo A Favor tiene la partida de la cual debe descargarse, se crea el ajuste.
				if(saldoAFavor.getPartida() != null){
					// Creamos un Ajuste de Caja 7 que descarga importe de una Partida.
					Caja7 caja7 = new Caja7();
					
					caja7.setBalance(this);
					caja7.setFecha(saldoAFavor.getFechaGeneracion());
					caja7.setPartida(saldoAFavor.getPartida());
					if(esEjercicioActual){
						caja7.setImporteEjeAct(saldoAFavor.getImporte()*(-1));
						caja7.setImporteEjeVen(0D);
					}else{
						caja7.setImporteEjeAct(0D);
						caja7.setImporteEjeVen(saldoAFavor.getImporte()*(-1));
					}
					caja7.setDescripcion("Origen: Saldo A Favor en Compensacion");
					caja7.setObservacion("Ajuste generado por Saldo a Favor de Cuenta: "+saldoAFavor.getCuenta().getNumeroCuenta()
													+", en Compensacion de descripcion: "+compensacion.getDescripcion()
													+", incluido en Balance nro: "+this.getId());
					
					BalBalanceManager.getInstance().createCaja7(caja7);		
				}else{
					String warning = "Advertencia!: No se creo Ajuste de Caja 7 para el Saldo a Favor de id "
						+saldoAFavor.getId()+" de la Compensacion de id "+compensacion.getId()
						+", incluido en Balance nro: "+this.getId();
					AdpRun.currentRun().logDebug(warning);
					this.getBalSession().addWarning(warning);
				}
			}
		}	
	
		
		
		// Mapa para acumular importes totales a descargar por partida y ejercicio
		Map<String, Double> mapTotalReingresoAct = new HashMap<String, Double>();
		Map<String, Double> mapTotalReingresoVen = new HashMap<String, Double>();
		// Por cada Reingreso (de Duplice o Indeterminado) incluido en el Balance.
		for(Reingreso reingreso: this.getListReingreso()) {
			// Buscamos el registro de reingreso en la base de Indeterminados
			IndetVO indet = BalDAOFactory.getIndeterminadoJDBCDAO().getReingresoById(reingreso.getNroReingreso());
			// Si se encontro se busca la partida de descarga por codigo.
			if(indet != null){
				String codPartida = indet.getPartida();
				// Si empieza con el 9 u 8 que corresponde al ejercicio actual o vencido, se quita para buscar la partida.
				if(codPartida != null && (codPartida.startsWith("8") || codPartida.startsWith("9"))){
					codPartida = codPartida.substring(1);
					try{ codPartida = Long.valueOf(codPartida).toString(); }catch(Exception e){ codPartida = "";}  
				}
				Partida partida = Partida.getByCod(codPartida);
				// Si se encontro se crea un Ajuste de Caja 7.
				if(partida != null){
					// 	Se averigua si corresponde a un Ejercicio Actual o Vencido.
					Ejercicio ejercicio = this.getEjercicio();
					boolean esEjercicioActual = false;
					if(DateUtil.isDateAfterOrEqual(indet.getFechaPago(), ejercicio.getFecIniEje())
							&& DateUtil.isDateBeforeOrEqual(indet.getFechaPago(), ejercicio.getFecFinEje())){
						esEjercicioActual = true;
					} 
					
					String clave = partida.getId().toString();
					if(esEjercicioActual){
						Double acumuladoPartida = mapTotalReingresoAct.get(clave);
						if(acumuladoPartida == null)
							acumuladoPartida = 0D;
						acumuladoPartida += reingreso.getImportePago();
						
						mapTotalReingresoAct.put(clave, acumuladoPartida);						
					}else{
						Double acumuladoPartida = mapTotalReingresoVen.get(clave);
						if(acumuladoPartida == null)
							acumuladoPartida = 0D;
						acumuladoPartida += reingreso.getImportePago();
						
						mapTotalReingresoVen.put(clave, acumuladoPartida);	
					}
					
				}else{
					String warning = "Advertencia!: No se creo Ajuste de Caja 7 para el Reingreso de Nro: "
						+reingreso.getNroReingreso()+", de importe pago "+reingreso.getImportePago()+". No se encontró la partida de código: "
						+indet.getPartida()+" en la db.";
					AdpRun.currentRun().logDebug(warning);
					this.getBalSession().addWarning(warning);
				}
			}else{
				this.addRecoverableValueError("Error!. No se encontró el Reingreso Nro: "+reingreso.getNroReingreso()
											+" en la base de Indeterminados.");
			}
		}
		// Creamos un Ajuste de Caja 7 que descarga importe de una Partida para ejercicio actual de lo acumulado para reingresos.
		for(String idPartida: mapTotalReingresoAct.keySet()){
			Partida partida = Partida.getById(Long.valueOf(idPartida));
			
			Double importe = mapTotalReingresoAct.get(idPartida);
			
			Caja7 caja7 = new Caja7();
			
			caja7.setBalance(this);
			caja7.setFecha(this.getFechaBalance());
			caja7.setPartida(partida);
			caja7.setImporteEjeAct(importe*(-1));
			caja7.setImporteEjeVen(0D);
			
			caja7.setDescripcion("Origen: Reingreso de Duplice o Indeterminado");
			caja7.setObservacion("Ajuste generado por acumulación de Reingresos de Duplice o Indeterminado");
			
			BalBalanceManager.getInstance().createCaja7(caja7);					
		}
		// Creamos un Ajuste de Caja 7 que descarga importe de una Partida para ejercicio vencido de lo acumulado para reingresos.
		for(String idPartida: mapTotalReingresoVen.keySet()){
			Partida partida = Partida.getById(Long.valueOf(idPartida));
			
			Double importe = mapTotalReingresoVen.get(idPartida);
			
			Caja7 caja7 = new Caja7();
			
			caja7.setBalance(this);
			caja7.setFecha(this.getFechaBalance());
			caja7.setPartida(partida);
			caja7.setImporteEjeAct(0D);
			caja7.setImporteEjeVen(importe*(-1));
			
			caja7.setDescripcion("Origen: Reingreso de Duplice o Indeterminado");
			caja7.setObservacion("Ajuste generado por acumulación de Reingresos de Duplice o Indeterminado");
			
			BalBalanceManager.getInstance().createCaja7(caja7);					
		}
		
	}

	/**
	 *  Crea las transacciones en Caja69 para las Compensaciones (incluidas en Folios o directamente al Balance)
	 * 
	 * <i>(paso 1.3)</i>
	 */
	public void prepararCaja69() throws Exception{
		for(Folio folio: this.getListFolio()){
			// Por cada Compensacion (FolCom)
			for(FolCom folCom: folio.getListFolCom()){
				// Si tiene una compensacion se intenta crea un transaccion para cada deuda a compensar
				if(folCom.getCompensacion() != null){
					// Recorremos los ComDeu que se cancelan en la Compensacion
					for(ComDeu comDeu: folCom.getCompensacion().getListComDeu()){
						Caja69 caja69 = new Caja69();
						Deuda deuda = Deuda.getById(comDeu.getIdDeuda());
						
						Long sistema = deuda.getSistema().getNroSistema();
						Long nroComprobante = null;
						String clave = null;
						Long resto = null;
						if(deuda.getSistema().getEsServicioBanco().intValue() == SiNo.SI.getId().intValue()
								&& deuda.getCodRefPag() != 0L){
							nroComprobante = deuda.getCodRefPag();
							clave = "000"+TipoBoleta.TIPODEUDA.getId()+"00";
							resto = 0L;
						}else{
							nroComprobante = new Long(deuda.getCuenta().getNumeroCuenta());
							clave=StringUtil.completarCerosIzq(deuda.getAnio().toString(),4)+StringUtil.completarCerosIzq(deuda.getPeriodo().toString(), 2);
							resto = deuda.getResto();							
						}
						caja69.setSistema(sistema);
						caja69.setNroComprobante(nroComprobante);
						caja69.setClave(clave);
						caja69.setResto(resto);
						caja69.setCodPago(4L); 
						caja69.setCaja(69L); 
						caja69.setCodTr(0L);
						caja69.setFechaPago(folCom.getCompensacion().getFechaAlta());
						caja69.setImporte(comDeu.getImporte());
						caja69.setRecargo(0D);
						caja69.setFiller("");
						caja69.setPaquete(0L);
						caja69.setMarcaTr(0L);
						caja69.setReciboTr(0L);
						caja69.setFechaBalance(this.getFechaBalance());
						caja69.setBalance(this);
						caja69.setNroLinea(0L);
						caja69.setStrLinea("");
						// Verificar si corresponde a un pago parcial. Si corresponde la marca como tal.
						if(comDeu.getTipoComDeu().getId().longValue() == TipoComDeu.ID_PAGO_PARCIAL){
							caja69.setTipoCancelacion(Transaccion.TIPO_CANCELACION_PARCIAL);
						}
						// Verificar si corresponde a una cancelacion por menos. Si corresponde la marca como tal.
						if(comDeu.getTipoComDeu().getId().longValue() == TipoComDeu.ID_CANCELACION_POR_MENOS){
							caja69.setTipoCancelacion(Transaccion.TIPO_CANCELACION_POR_MENOS);
						}
						
						caja69.setEstado(Estado.ACTIVO.getId());
						
						BalBalanceManager.getInstance().createCaja69(caja69);
					}
				}
			}			
		}
			
		// Por cada Compensacion inlcuida en Balance (Mantis 5413)
		for(Compensacion compensacion: this.getListCompensacion()){
			// Recorremos los ComDeu que se cancelan en la Compensacion
			for(ComDeu comDeu: compensacion.getListComDeu()){
				Caja69 caja69 = new Caja69();
				Deuda deuda = Deuda.getById(comDeu.getIdDeuda());
				
				Long sistema = deuda.getSistema().getNroSistema();
				Long nroComprobante = null;
				String clave = null;
				Long resto = null;
				if(deuda.getSistema().getEsServicioBanco().intValue() == SiNo.SI.getId().intValue()
						&& deuda.getCodRefPag() != 0L){
					nroComprobante = deuda.getCodRefPag();
					clave = "000"+TipoBoleta.TIPODEUDA.getId()+"00";
					resto = 0L;
				}else{
					nroComprobante = new Long(deuda.getCuenta().getNumeroCuenta());
					clave=StringUtil.completarCerosIzq(deuda.getAnio().toString(),4)+StringUtil.completarCerosIzq(deuda.getPeriodo().toString(), 2);
					resto = deuda.getResto();							
				}
				caja69.setSistema(sistema);
				caja69.setNroComprobante(nroComprobante);
				caja69.setClave(clave);
				caja69.setResto(resto);
				caja69.setCodPago(4L); 
				caja69.setCaja(69L); 
				caja69.setCodTr(0L);
				caja69.setFechaPago(compensacion.getFechaAlta());
				caja69.setImporte(comDeu.getImporte());
				caja69.setRecargo(0D);
				caja69.setFiller("");
				caja69.setPaquete(0L);
				caja69.setMarcaTr(0L);
				caja69.setReciboTr(0L);
				caja69.setFechaBalance(this.getFechaBalance());
				caja69.setBalance(this);
				caja69.setNroLinea(0L);
				caja69.setStrLinea("");
				// Verificar si corresponde a un pago parcial. Si corresponde la marca como tal.
				if(comDeu.getTipoComDeu().getId().longValue() == TipoComDeu.ID_PAGO_PARCIAL){
					caja69.setTipoCancelacion(Transaccion.TIPO_CANCELACION_PARCIAL);
				}
				// Verificar si corresponde a una cancelacion por menos. Si corresponde la marca como tal.
				if(comDeu.getTipoComDeu().getId().longValue() == TipoComDeu.ID_CANCELACION_POR_MENOS){
					caja69.setTipoCancelacion(Transaccion.TIPO_CANCELACION_POR_MENOS);
				}
				
				caja69.setEstado(Estado.ACTIVO.getId());
				
				BalBalanceManager.getInstance().createCaja69(caja69);
			}
		}
	}

	/**
	 *  Crea las transacciones en TranBal obtenidas de los archivos de banco incluidos en el balance.
	 *  Y transacciones para los reingresos (Indeterminados y Duplices).
	 * <i>(paso 1.4)</i>
	 */
	public void prepararTranBal() throws Exception{
		// Recorrer los Archivos incluidos en el Balance
		for(Archivo archivo: this.getListArchivo()){
			// Si el archivo es de Tipo Banco (BMR)
			if(archivo.getTipoArc().getId().longValue() == TipoArc.ID_BMR || archivo.getTipoArc().getId().longValue() == TipoArc.ID_OSIRIS){
				// Armamos la transaccion dependiendo del Prefijo 
				for(TranArc tranArc: archivo.getListTranArc()){
					boolean indetTr = false;
					TranBal tranBal = new TranBal();
					
					tranBal.setSistema(tranArc.getSistema());
					if(tranBal.getSistema() == null){
						tranBal.setSistema(0L);
						indetTr=true;
					}
					tranBal.setNroComprobante(tranArc.getNroComprobante());
					if(tranBal.getNroComprobante() == null){
						tranBal.setNroComprobante(0L);
						indetTr=true;
					}
					tranBal.setClave(tranArc.getClave());
					tranBal.setResto(0L);
					tranBal.setCodPago(tranArc.getCodPago());
					if(tranBal.getCodPago() == null){
						tranBal.setCodPago(0L);
						indetTr=true;
					}
					tranBal.setCaja(tranArc.getCaja());
					if(tranBal.getCaja() == null){
						tranBal.setCaja(0L);
						indetTr=true;
					}
					tranBal.setCodTr(0L); 
					tranBal.setFechaPago(tranArc.getFechaPago());
					if(tranBal.getFechaPago() == null){
						indetTr=true;
					}
					tranBal.setImporte(tranArc.getImportePago());
					if(tranBal.getImporte() == null){
						tranBal.setImporte(0D);
						indetTr=true;
					}
					tranBal.setRecargo(0D);
					tranBal.setPaquete(tranArc.getPaquete());
					if(tranBal.getPaquete() == null){
						tranBal.setPaquete(0L);
						indetTr=true;
					}
					tranBal.setMarcaTr(0L);
					tranBal.setFechaBalance(this.getFechaBalance());
					tranBal.setBalance(this);
					tranBal.setNroLinea(tranArc.getNroLinea());
					tranBal.setStrLinea(tranArc.getLinea());
					tranBal.setEstado(Estado.ACTIVO.getId());
					tranBal.setFormulario(tranArc.getFormulario());
					if(archivo.getTipoArc().getId().longValue() == TipoArc.ID_OSIRIS){
						tranBal.setOrigen(Transaccion.ORIGEN_OSIRIS);
						tranBal.setIdTranAfip(tranArc.getIdTranAfip());
						tranBal.setReciboTr(tranArc.getIdTranAfip());
						tranBal.setFiller(SinIndet.FILLER_AFIP);
						
						//---
						
						//---
					}else{
						tranBal.setOrigen(0);
						tranBal.setIdTranAfip(0L);
						tranBal.setReciboTr(0L);
						tranBal.setFiller("");
					}
					// Conversion de sistemas:
					tranBal.setSistema(this.corregirSistema(tranBal.getSistema(), tranBal.getNroComprobante()));
					
					// Corregir tipo de boleta para transacciones emitidas entre del 14/09/2009  al 18/09/2009
					//(TODO eliminar este correccion luego de que se hayan procesado en balance los dias de banco anteriores al 01/10/2009)
					Date fechaActual = new Date();
					Date fechaLimite = DateUtil.getDate("20091224", DateUtil.YYYYMMDD_MASK);
					if(DateUtil.isDateBefore(fechaActual, fechaLimite)  && tranBal.getSistema().longValue() == 85L && "2000".equals(tranBal.getClave())){
						// Buscar codRefPag en mapa
						if(this.getBalSession().isCodRefPagInFixMap(tranBal.getNroComprobante().toString())){
							// Si esta en el mapa cambia la clave a 1000 (que corresponde a tipoBoleta deuda)
							tranBal.setClave("1000");			
						}
					}
					
					if(!indetTr){
						BalBalanceManager.getInstance().createTranBal(tranBal);							
					}else{
						this.registrarIndeterminado("Error en formato al intentar recuperar los datos del archivo.",tranBal, "56"); 
					}
				}
			}
		}
		// Mapa para importe de reingresos a Cuenta Puente por Servicio Banco
		Map<String, Double> mapImpReingAcum = new HashMap<String, Double>();
		
		// Por cada Reingreso (de Duplice o Indeterminado) incluido en el Balance.
		for(Reingreso reingreso: this.getListReingreso()) {
			// Buscamos el registro de reingreso en la base de Indeterminados
			IndetVO indet = BalDAOFactory.getIndeterminadoJDBCDAO().getReingresoById(reingreso.getNroReingreso());
			// Si se encuentra se crea una transaccion en TranBal
			if(indet != null){
				TranBal tranBal = new TranBal();
			
				try{
					tranBal.setSistema(new Long(indet.getSistema().trim()));
					tranBal.setNroComprobante(new Long(indet.getNroComprobante().trim()));
					tranBal.setClave(indet.getClave().trim());
					if(indet.getResto() != null && !"".equals(indet.getResto().trim()))
						tranBal.setResto(new Long(indet.getResto().trim()));
					else
						tranBal.setResto(0L);
					tranBal.setCodPago(new Long(indet.getCodPago()));
					tranBal.setCaja(80L);
					tranBal.setCodTr(indet.getCodTr());
					tranBal.setFechaPago(indet.getFechaPago());
					tranBal.setImporte(indet.getImporteCobrado()); 
					tranBal.setRecargo(0D);
					tranBal.setFiller(indet.getFiller());
					tranBal.setPaquete(new Long(indet.getPaquete()));
					tranBal.setMarcaTr(0L);
					tranBal.setFechaBalance(this.getFechaBalance());
					tranBal.setReciboTr(indet.getReciboTr());
					tranBal.setBalance(this);
					tranBal.setNroLinea(0L);
					tranBal.setStrLinea("");
					
					//Si el reingreso es de origen Osiris
					if (SinIndet.ID_TPO_INGRESO_AFIP.equals(indet.getTipoIngreso())) {
						tranBal.setOrigen(Transaccion.ORIGEN_OSIRIS);
						tranBal.setIdTranAfip(indet.getReciboTr());
						//Seteo el formulario
						TranAfip tranAfip = TranAfip.getByIdNull(tranBal.getIdTranAfip());
						if (null != tranAfip) tranBal.setFormulario(tranAfip.getFormulario());
					}else {
						tranBal.setOrigen(0);
						tranBal.setIdTranAfip(0L);
					}
					
					tranBal.setEstado(Estado.ACTIVO.getId());
					
					// Conversion de sistemas:
					tranBal.setSistema(this.corregirSistema(tranBal.getSistema(), tranBal.getNroComprobante()));

					// Acumular total de reingresos para Cuenta Puente por Servicio Banco
					Sistema sistema = BalanceCache.getInstance().getSistemaByNro(tranBal.getSistema());
					if(sistema != null){
						String codSerBan = sistema.getServicioBanco().getCodServicioBanco();
						Double impReingAcum = mapImpReingAcum.get(codSerBan);
						if(impReingAcum == null)
							impReingAcum = 0D;
						impReingAcum += tranBal.getImporte();
						mapImpReingAcum.put(codSerBan, impReingAcum);
					}
					
					BalBalanceManager.getInstance().createTranBal(tranBal);
				}catch(Exception e){
					this.addRecoverableValueError("Error!. Existe algún error de formato en el Reingreso Nro: "+reingreso.getNroReingreso()
												+". Excepcion: "+e); 
				}
			}else{
				this.addRecoverableValueError("Error!. No se encontró el Reingreso Nro: "+reingreso.getNroReingreso()
											+" en la base de Indeterminados."); 
			}
		}
		// Crear Caja 7 para Total de Cuenta Puente de Reingresos
		for(String codSerBan: mapImpReingAcum.keySet()){
			ServicioBanco servicioBanco = ServicioBanco.getByCodigo(codSerBan);
			Partida parCuePue = servicioBanco.getParCuePue();
			Double impReinAcum = mapImpReingAcum.get(codSerBan);
			
			Caja7 caja7 = new Caja7();
			
			caja7.setBalance(this);
			caja7.setFecha(this.getFechaBalance()); 
			caja7.setPartida(parCuePue);
			caja7.setImporteEjeAct(impReinAcum);
			caja7.setImporteEjeVen(0D);
			caja7.setDescripcion("Ajuste de carga a Cuenta Puente por Reingresos. Servicio Banco: "+codSerBan);
			
			BalBalanceManager.getInstance().createCaja7(caja7);	
		}
		
	}
	
	/**
	 * <b>Genera Formularios para control del paso 1:</b> 
	 * <p>- Reporte Totales por Sistema (5018): archivo pdf</p>
	 * <p>- Reporte Conciliacion de Caja: archivo pdf</p>
	 * <p>- Advertencias: txt</p>
	 * <i>(paso 1.5)</i>
	 */
	public void generarFormulariosPaso1(String outputDir) throws Exception{
		
		//-> Reporte de Totales por Sistema (5018) (PDF)
		String fileName = this.generarPdfTotalesPorSistema(outputDir);
		String nombre = "Totales por Sistema (5018)";
		String descripcion = "Permite consultar los totales de transacciones e importes por Sistema.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
	
		//-> Conciliación de Cajas (PDF)
		fileName = this.generarPdfConciliacionDeCajas(outputDir);
		nombre = "Conciliación de Cajas";
		descripcion = "Detalle de Totales por Archivos, Dias de Tesorería y Reingresos.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
		
		//-> Lista de Advertencias
		fileName = this.generarTxtAdvertencias(outputDir);
		nombre = "Lista de Advertencias";
		descripcion = "Lista de Advertencias obtenidas al preparar las cajas.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);			

		// -> Planilla de Transacciones de Sellado (Planilla de calculo)
		List<String> listFileName = this.exportReportesTranBalSellado(outputDir);
		int c = 0;
		nombre = "Detalle de Transacciones de Sellado";
		if(listFileName != null){
			for(String fileNameArch: listFileName){
				descripcion = "Planilla con el Detalle de Transaccion de Sellado. Hoja "+c;
				this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileNameArch);
				c++;
			}			
		}
	
		// -> Detalle de Indeterminados (Planilla de calculo)
		listFileName = this.exportReportesIndeterminados(outputDir);
		c = 0;
		nombre = "Detalle de Indeterminados";
		for(String fileNameArch: listFileName){
			descripcion = "Planilla con el Detalle de transacciones Indeterminadas durante la prepación. Hoja "+c;
			this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileNameArch);
			c++;
		}
	}
	
	/**
	 * Genera el Reporte pdf "Conciliación de Cajas" resultado del paso 1 del proceso de Balance.
	 * 
	 * @param outputDir
	 * @return fileName
	 */
	public String generarPdfConciliacionDeCajas(String fileDir) throws Exception{
		
		//	Encabezado:
		String fechaBalance= DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		
		Double totRecAnt = BalDAOFactory.getImpParDAO().getTotalRecaudadoAnio(DateUtil.getAnio(this.getFechaBalance())); 
		//BalDAOFactory.getImpParDAO().getTotalRecaudadoHastaFecha(this.getFechaBalance());
		
		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Balance.COD_FRM_CONC_CAJA);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Conciliación de Cajas");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Conciliación de Cajas");
		printModel.putCabecera("NroBalance", this.getId().toString());
		printModel.putCabecera("TotRecAnt", StringUtil.formatDoubleWithComa(totRecAnt));
		printModel.putCabecera("FechaBal", fechaBalance);
		printModel.putCabecera("FechaBalLarga", DateUtil.getDateEnLetras(this.getFechaBalance()));  
		
		// Se arma un contenedor de tablas para guardas los datos.
		ContenedorVO contenedor = new ContenedorVO("ConciliacionCajas");
		
		// Se define la fila cabecera (con titulos) para todas las tablas
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Archivos Leídos","archivo"));
		filaCabecera.add(new CeldaVO("Nombre","nombre"));
		filaCabecera.add(new CeldaVO("Imp. Parciales","importeParcial"));
		filaCabecera.add(new CeldaVO("Imp. Totales","importeTotal"));
		
		
		// Tabla para Archivos de Grav. Especiales
		TablaVO tabla = new TablaVO("ArchGravEsp");
		tabla.setTitulo("Archivos de Gravámenes Especiales");
		tabla.setFilaCabecera(filaCabecera);
		Double totalGravEsp = 0D;
		List<String> listPrefix = new ArrayList<String>();
		listPrefix.add("ge");
		List<Archivo> listArchivoGE = this.getListArchivoByListPrefix(listPrefix);
		FilaVO fila = new FilaVO();
		Map<String, Double> mapTotalesArchivoGE = new HashMap<String, Double>();
		for(Archivo archivo: listArchivoGE){
			String clave =  DateUtil.formatDate(archivo.getFechaBanco(), DateUtil.ddSMMSYYYY_MASK)+"|"+archivo.getPrefix().trim();
			Double importe = null;
			importe = mapTotalesArchivoGE.get(clave);
			if(importe == null){
				importe = 0D;
			}
			importe += archivo.getTotal();
			mapTotalesArchivoGE.put(clave, importe);
		}
		List<String> listClave = new ArrayList<String>(); 
		listClave.addAll(mapTotalesArchivoGE.keySet());
    	listClave = this.ordenarListClaveArchivo(listClave);
		for(String clave: listClave){
			Double importe = mapTotalesArchivoGE.get(clave);
			Datum datum = Datum.parse(clave);
			fila.add(new CeldaVO("","archivo"));
			fila.add(new CeldaVO(datum.getCols(0)+"-"+datum.getCols(1),"nombre"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importe, SiatParam.DEC_IMPORTE_DB)),"importeParcial"));
			fila.add(new CeldaVO("","importeTotal"));
			tabla.add(fila);
			fila = new FilaVO();
			totalGravEsp += importe;
		}
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("","archivo"));
		filaPie.add(new CeldaVO("","nombre"));
		filaPie.add(new CeldaVO("Total leído","importeParcial"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalGravEsp, SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);
		
		// Tabla para Archivos de Tesoreria
		tabla = new TablaVO("ArchTes");
		tabla.setTitulo("Archivos de Tesorería");
		tabla.setFilaCabecera(filaCabecera);
		Double totalTes = 0D;
		Double totalDiasCobGeneral = 0D;
		for(Folio folio: this.getListFolio()){
			Double totalFolio = 0D;
			fila = new FilaVO();
			fila.add(new CeldaVO(DateUtil.formatDate(folio.getFechaFolio(),DateUtil.ddSMMSYYYY_MASK),"fecha"));
			fila.add(new CeldaVO("","nombre"));
			fila.add(new CeldaVO("","importeParcial"));
			fila.add(new CeldaVO("","importeTotal"));
			tabla.add(fila);
			Double totalDiaCob = 0D;
			for(FolDiaCob folDiaCob: folio.getListFolDiaCob()){
				for(FolDiaCobCol folDiaCobCol: folDiaCob.getListFolDiaCobCol())
					totalDiaCob += folDiaCobCol.getImporte();
			}
			fila = new FilaVO();
			fila.add(new CeldaVO("","archivo"));
			fila.add(new CeldaVO("Dias de Cobranza","nombre"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalDiaCob, SiatParam.DEC_IMPORTE_DB)),"importeParcial"));
			fila.add(new CeldaVO("","importeTotal"));
			tabla.add(fila);

			Double totalFolCom = 0D;
			for(FolCom folCom: folio.getListFolCom()){
				totalFolCom += folCom.getImporte();
			}
			fila = new FilaVO();
			fila.add(new CeldaVO("","archivo"));
			fila.add(new CeldaVO("Compensaciones","nombre"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalFolCom, SiatParam.DEC_IMPORTE_DB)),"importeParcial"));
			fila.add(new CeldaVO("","importeTotal"));
			tabla.add(fila);
			
			Double totalOtrIngTes = 0D;
			for(OtrIngTes otrIngTes: folio.getListOtrIngTes()){
				totalOtrIngTes += otrIngTes.getImporte();
			}
			fila = new FilaVO();
			fila.add(new CeldaVO("","archivo"));
			fila.add(new CeldaVO("Otros Ingresos de Tesorería","nombre"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalOtrIngTes, SiatParam.DEC_IMPORTE_DB)),"importeParcial"));
			fila.add(new CeldaVO("","importeTotal"));
			tabla.add(fila);

			totalFolio = totalDiaCob+totalFolCom+totalOtrIngTes;
			fila = new FilaVO();
			fila.add(new CeldaVO("","archivo"));
			fila.add(new CeldaVO("","nombre"));
			fila.add(new CeldaVO("Total Folio","importeParcial"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalFolio, SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
			tabla.add(fila);
			
			totalTes += totalFolio;
			totalDiasCobGeneral += totalDiaCob;
		}

		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("","archivo"));
		filaPie.add(new CeldaVO("","nombre"));
		filaPie.add(new CeldaVO("Total Días de Cobranza","importeDiaCob"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalDiasCobGeneral, SiatParam.DEC_IMPORTE_DB)),"importeDiaCob"));
		tabla.addFilaPie(filaPie);
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("","archivo"));
		filaPie.add(new CeldaVO("","nombre"));
		filaPie.add(new CeldaVO("Total leído","importeTotal"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalTes, SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);

		// Tabla para Sellados
		tabla = new TablaVO("Sellados");
		tabla.setTitulo("Sellados");
		tabla.setFilaCabecera(filaCabecera);
		fila = new FilaVO();
		Double totalSell = 0D;
		listPrefix = new ArrayList<String>();
		listPrefix.add("se");
		List<Archivo> listArchivoSE = this.getListArchivoByListPrefix(listPrefix);
		Map<String, Double> mapTotalesArchivoSE = new HashMap<String, Double>();
		for(Archivo archivo: listArchivoSE){
			String clave =  DateUtil.formatDate(archivo.getFechaBanco(), DateUtil.ddSMMSYYYY_MASK)+"|"+archivo.getPrefix().trim();
			Double importe = null;
			importe = mapTotalesArchivoSE.get(clave);
			if(importe == null){
				importe = 0D;
			}
			importe += archivo.getTotal();
			mapTotalesArchivoSE.put(clave, importe);
		}
		listClave = new ArrayList<String>(); 
		listClave.addAll(mapTotalesArchivoSE.keySet());
    	listClave = this.ordenarListClaveArchivo(listClave);
		for(String clave: listClave){
			Double importe = mapTotalesArchivoSE.get(clave);
			Datum datum = Datum.parse(clave);
			fila.add(new CeldaVO("","archivo"));
			fila.add(new CeldaVO(datum.getCols(0)+"-"+datum.getCols(1),"nombre"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importe, SiatParam.DEC_IMPORTE_DB)),"importeParcial"));
			fila.add(new CeldaVO("","importeTotal"));
			tabla.add(fila);
			fila = new FilaVO();
			totalSell += importe;
		}
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("","archivo"));
		filaPie.add(new CeldaVO("","nombre"));
		filaPie.add(new CeldaVO("Total leído","importeParcial"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalSell, SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);
		
		// Tabla para Archivos de Banco
		tabla = new TablaVO("ArchBanco");
		tabla.setTitulo("Archivos de Banco");
		tabla.setFilaCabecera(filaCabecera);
		fila = new FilaVO();
		Double totalBanco = 0D;
		listPrefix = new ArrayList<String>();
		listPrefix.add("re");
		listPrefix.add("gc");
		List<Archivo> listArchivoBanco = this.getListArchivoByListPrefix(listPrefix);
		Map<String, Double> mapTotalesArchivoBanco = new HashMap<String, Double>();
		for(Archivo archivo: listArchivoBanco){
			String clave =  DateUtil.formatDate(archivo.getFechaBanco(), DateUtil.ddSMMSYYYY_MASK)+"|"+archivo.getPrefix().trim();
			Double importe = null;
			importe = mapTotalesArchivoBanco.get(clave);
			if(importe == null){
				importe = 0D;
			}
			importe += archivo.getTotal();
			mapTotalesArchivoBanco.put(clave, importe);
		}
		listClave = new ArrayList<String>(); 
		listClave.addAll(mapTotalesArchivoBanco.keySet());
    	listClave = this.ordenarListClaveArchivo(listClave);
		for(String clave: listClave){
			Double importe = mapTotalesArchivoBanco.get(clave);
			Datum datum = Datum.parse(clave);
			fila.add(new CeldaVO("","archivo"));
			fila.add(new CeldaVO(datum.getCols(0)+"-"+datum.getCols(1),"nombre"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importe, SiatParam.DEC_IMPORTE_DB)),"importeParcial"));
			fila.add(new CeldaVO("","importeTotal"));
			tabla.add(fila);
			fila = new FilaVO();
			totalBanco += importe;
		}
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("","archivo"));
		filaPie.add(new CeldaVO("","nombre"));
		filaPie.add(new CeldaVO("Total leído","importeParcial"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalBanco, SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);
		
		// Tabla para Archivos de Indeterminados
		tabla = new TablaVO("Reingresos");
		tabla.setTitulo("Reingreso de Indeterminados");
		tabla.setFilaCabecera(filaCabecera);
		fila = new FilaVO();
		Double totalReing = 0D;
		for(Reingreso reingreso: this.getListReingreso()){
			totalReing += reingreso.getImportePago();
		}
		fila.add(new CeldaVO("","archivo"));
		fila.add(new CeldaVO("inde","nombre"));
		fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalReing, SiatParam.DEC_IMPORTE_DB)),"importeParcial"));
		fila.add(new CeldaVO("","importeTotal"));
		tabla.add(fila);

		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("","archivo"));
		filaPie.add(new CeldaVO("","nombre"));
		filaPie.add(new CeldaVO("Total leído","importeParcial"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalReing, SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);
		
		// Tabla para Otros Archivos
		tabla = new TablaVO("OtrosArch");
		tabla.setTitulo("Otros Archivos");
		tabla.setFilaCabecera(filaCabecera);
		fila = new FilaVO();
		Double totalOtros = 0D;
		listPrefix = new ArrayList<String>();
		listPrefix.add("cj");
		listPrefix.add("tr");
		listPrefix.add("mo");
		List<Archivo> listOtrosArchivo = this.getListArchivoByListPrefix(listPrefix); 
		Map<String, Double> mapTotalesArchivoOtros = new HashMap<String, Double>();
		for(Archivo archivo: listOtrosArchivo){
			String clave =  DateUtil.formatDate(archivo.getFechaBanco(), DateUtil.ddSMMSYYYY_MASK)+"|"+archivo.getPrefix().trim();
			Double importe = null;
			importe = mapTotalesArchivoOtros.get(clave);
			if(importe == null){
				importe = 0D;
			}
			importe += archivo.getTotal();
			mapTotalesArchivoOtros.put(clave, importe);
		}
		listClave = new ArrayList<String>(); 
		listClave.addAll(mapTotalesArchivoOtros.keySet());
    	listClave = this.ordenarListClaveArchivo(listClave);
		for(String clave: listClave){
			Double importe = mapTotalesArchivoOtros.get(clave);
			Datum datum = Datum.parse(clave);
			fila.add(new CeldaVO("","archivo"));
			fila.add(new CeldaVO(datum.getCols(0)+"-"+datum.getCols(1),"nombre"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importe, SiatParam.DEC_IMPORTE_DB)),"importeParcial"));
			fila.add(new CeldaVO("","importeTotal"));
			tabla.add(fila);
			fila = new FilaVO();
			totalOtros += importe;
		}
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("","archivo"));
		filaPie.add(new CeldaVO("","nombre"));
		filaPie.add(new CeldaVO("Total leído","importeParcial"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalOtros, SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);
		
		// Tabla para Compensaciones incluidas directamente en Balance (Se agrega por Mantis 5413, ver si se esta afectando de forma correcta este reporte)
		tabla = new TablaVO("CompensacionesBalance");
		tabla.setTitulo("Compensaciones de Balance");
		tabla.setFilaCabecera(filaCabecera);
		fila = new FilaVO();
		Double totalComBal = 0D;
		for(Compensacion compensacion: this.getListCompensacion()){
			for(ComDeu comDeu: compensacion.getListComDeu()){
				totalComBal += comDeu.getImporte();				
			}
		}
		fila.add(new CeldaVO("","compensacionBalance"));
		fila.add(new CeldaVO("compensacion","nombre"));
		fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalComBal, SiatParam.DEC_IMPORTE_DB)),"importeParcial"));
		fila.add(new CeldaVO("","importeTotal"));
		tabla.add(fila);

		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("","compensacionBalance"));
		filaPie.add(new CeldaVO("","nombre"));
		filaPie.add(new CeldaVO("Total leído","importeParcial"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalComBal, SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);
		
		Double totalRecBal = totalGravEsp+totalTes+totalSell+totalReing+totalOtros+totalBanco+totalComBal;
		printModel.putCabecera("TotRecBal", StringUtil.formatDoubleWithComa(totalRecBal));
		
		Double totalRecaudado = totRecAnt+totalGravEsp+totalTes+totalSell+totalReing+totalOtros+totalBanco+totalComBal;
		printModel.putCabecera("TotRec", StringUtil.formatDoubleWithComa(totalRecaudado));
		
		printModel.setData(contenedor);
		printModel.setTopeProfundidad(5);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"ConciliacionCajas.pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		return fileName;
	}

	/**
	 * Genera el Reporte pdf "Total de transacciones por Caja" resultado del paso 2 del proceso de Balance.
	 * 
	 * @param outputDir
	 * @return fileName
	 */
	public String generarPdfTotalesPorCaja(String fileDir) throws Exception{
		
		//	Encabezado:
		String fechaBalance = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		
		//List<Object[]> listResultTranBal = BalDAOFactory.getTranBalDAO().getTotalPorSistemaForReportByBalance(this);
		List<Object[]> listResultTranBal = BalDAOFactory.getTranBalDAO().getTotalPorCajaForReportByBalance(this);
		List<Object[]> listResultCaja69 = BalDAOFactory.getCaja69DAO().getTotalPorSistemaForReportByBalance(this);
		Double resultCaja7 = BalDAOFactory.getCaja7DAO().getTotalPorSistemaForReportByBalance(this);
		
		if(resultCaja7 == null)
			resultCaja7 = 0D;
		
		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Balance.COD_FRM_TOT_CAJA);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Reporte de Balance - Totales por Caja");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Reporte de Balance - Totales por Caja");
		printModel.putCabecera("FechaBalance", fechaBalance);
		printModel.putCabecera("Ejercicio", ejercicio);
				
		// Armamos un Contenedor para las Tablas
		ContenedorVO contenedor = new ContenedorVO("ContenedorTabla");
		
		// Se arma la tabla en una estructura de objectos. (TablaVO, FilaVO, CeldaVO)
		TablaVO tabla = new TablaVO("TotalesPorCajaTranBal");
		tabla.setTitulo("Transacciones de Archivos");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Caja","caja"));
		filaCabecera.add(new CeldaVO("Cantidad Transacciones","cantTransaccion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		FilaVO fila = new FilaVO();
		Double importeTotal = 0D;
		Long cantTotal = 0L;
		for(Object[] arrayResult: listResultTranBal){
			Long nroCaja = (Long) arrayResult[0]; 
			Banco banco = Banco.getByCodBanco(nroCaja.toString());//BalanceCache.getInstance().getBancoByNro(nroCaja); TODO pasar a cache de bancos
			String desCaja = nroCaja.toString();
			if(banco != null){
				desCaja += " - " + banco.getDesBanco();
			}
			fila.add(new CeldaVO(desCaja,"caja"));//((Long) arrayResult[0]).toString(),"caja"));
			fila.add(new CeldaVO(((Long) arrayResult[1]).toString(),"cantTransaccion"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			fila = new FilaVO();
			importeTotal += (Double) arrayResult[2];
			cantTotal += (Long) arrayResult[1];
		}
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total","total"));
		filaPie.add(new CeldaVO(cantTotal.toString(),"totalTransaccion"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);
		
		
		// Se arma la tabla en una estructura de objectos. (TablaVO, FilaVO, CeldaVO)
		tabla = new TablaVO("TotalesPorSistemaCaja69");
		tabla.setTitulo("Caja 69");
		filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Sistema","sistema"));
		filaCabecera.add(new CeldaVO("Cantidad Transacciones","cantTransaccion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		fila = new FilaVO();
		importeTotal = 0D;
		cantTotal = 0L;
		for(Object[] arrayResult: listResultCaja69){
			Long nroSistema = (Long) arrayResult[0]; 
			Sistema sistema = BalanceCache.getInstance().getSistemaByNro(nroSistema);
			String desSistema = nroSistema + " - ";
			if(sistema != null){
				desSistema += sistema.getDesSistema();
			}else{
				desSistema += " No existe en SIAT";
			}
			fila.add(new CeldaVO(desSistema,"sistema"));
			fila.add(new CeldaVO(((Long) arrayResult[1]).toString(),"cantTransaccion"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			fila = new FilaVO();
			importeTotal += (Double) arrayResult[2];
			cantTotal += (Long) arrayResult[1];		
		}
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total","total"));
		filaPie.add(new CeldaVO(cantTotal.toString(),"totalTransaccion"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);
	
		// Se arma la tabla en una estructura de objectos. (TablaVO, FilaVO, CeldaVO)
		tabla = new TablaVO("TotalCaja7");
		tabla.setTitulo("Caja 7");
		filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("","total"));
		filaCabecera.add(new CeldaVO("","vacio"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		fila = new FilaVO();
		importeTotal = 0D;
		cantTotal = 0L;

		fila.add(new CeldaVO("Total","total"));
		fila.add(new CeldaVO("","vacio"));
		fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(resultCaja7, SiatParam.DEC_IMPORTE_DB)),"importe"));
		tabla.add(fila);
		contenedor.add(tabla);
		
		printModel.setData(contenedor);
		printModel.setTopeProfundidad(5);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"ReporteTotalesPorCaja-"+corrida.getPasoActual().toString()+".pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		return fileName;
	}
	
	/**
	 * Genera el Reporte pdf "Totales por Sistema" resultado del paso 1 y 2 del proceso de Balance.
	 * 
	 * @param outputDir
	 * @return fileName
	 */
	public String generarPdfTotalesPorSistema(String fileDir) throws Exception{
		
		//	Encabezado:
		String fechaBalance = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		
		List<Object[]> listResultTranBal = BalDAOFactory.getTranBalDAO().getTotalPorSistemaForReportByBalance(this);
		List<Object[]> listResultCaja69 = BalDAOFactory.getCaja69DAO().getTotalPorSistemaForReportByBalance(this);
		
		// Acumulamos los resultados para Caja69 con los de TranBal
		Map<Long, Object[]> mapResult = new HashMap<Long, Object[]>();
		for(Object[] arrayResult: listResultTranBal){
			Long sistema = (Long) arrayResult[0]; 
			mapResult.put(sistema, arrayResult);
		}
		for(Object[] arrayResult: listResultCaja69){
			Long sistema = (Long) arrayResult[0]; 
			Object[] totalPorSistema = mapResult.get(sistema);
			if(totalPorSistema != null){
				totalPorSistema[1] = ((Long) totalPorSistema[1])+((Long) arrayResult[1]);
				totalPorSistema[2] = ((Double) totalPorSistema[2])+((Double) arrayResult[2]);
				mapResult.put(sistema, totalPorSistema);	
			}else{
				mapResult.put(sistema, arrayResult);				
			}
		}
		List<Object[]> listResult = new ArrayList<Object[]>();
		listResult.addAll(mapResult.values());
		
		Double importeTotal = 0D;
		Long cantTotal = 0L;

		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Balance.COD_FRM_5018);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Reporte de Balance - Totales por Sistema");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Reporte de Balance - Totales por Sistema");
		printModel.putCabecera("FechaBalance", fechaBalance);
		printModel.putCabecera("Ejercicio", ejercicio);
				
		// Armamos un Contenedor para las Tablas
		ContenedorVO contenedor = new ContenedorVO("ContenedorTabla");

		Map<Long, Object[]> mapResultServBan = new HashMap<Long, Object[]>();
		
		// Se arma la tabla en una estructura de objectos. (TablaVO, FilaVO, CeldaVO)
		TablaVO tabla = new TablaVO("TotalesPorSistema");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Sistema","sistema"));
		filaCabecera.add(new CeldaVO("Cantidad Transacciones","cantTransaccion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		FilaVO fila = new FilaVO();
		for(Object[] arrayResult: listResult){
			Long nroSistema = (Long) arrayResult[0]; 
			//Sistema sistema = Sistema.getByNroSistema(nroSistema);
			Sistema sistema = BalanceCache.getInstance().getSistemaByNro(nroSistema);
			String desSistema = nroSistema + " - ";
			if(sistema != null){
				desSistema += sistema.getDesSistema();
			}else{
				desSistema += " No existe en SIAT";
			}
			fila.add(new CeldaVO(desSistema,"sistema"));
			fila.add(new CeldaVO(((Long) arrayResult[1]).toString(),"cantTransaccion"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			fila = new FilaVO();
			importeTotal += (Double) arrayResult[2];
			cantTotal += (Long) arrayResult[1];
			
			Long idSerBan = -1L; 
			if(sistema != null)
				idSerBan = sistema.getServicioBanco().getId();
			 
			Object[] totalPorSerBan = mapResultServBan.get(idSerBan);
			
			if(totalPorSerBan == null){
				totalPorSerBan = new Object[2]; 
				totalPorSerBan[0] = 0L;
				totalPorSerBan[1] = 0D;				
			}
			totalPorSerBan[0] = ((Long) totalPorSerBan[0])+((Long) arrayResult[1]);
			totalPorSerBan[1] = ((Double) totalPorSerBan[1])+((Double) arrayResult[2]);
			mapResultServBan.put(idSerBan, totalPorSerBan);				
		}
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total","total"));
		filaPie.add(new CeldaVO(cantTotal.toString(),"totalTransaccion"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);
		
		
		// Se arma la tabla en una estructura de objetos. (TablaVO, FilaVO, CeldaVO)
		tabla = new TablaVO("TotalesPorServBan");
		filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Servicio Banco","serBan"));
		filaCabecera.add(new CeldaVO("Cantidad Transacciones","cantTransaccion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		fila = new FilaVO();
		for(Long key: mapResultServBan.keySet()){
			Object[] arrayResult = mapResultServBan.get(key);
			Long idSerBan = key; 
			ServicioBanco servicioBanco = ServicioBanco.getByIdNull(idSerBan);
			String desSerBan = "Desconocido";
			if(servicioBanco != null){
				desSerBan = servicioBanco.getCodServicioBanco()+" - "+servicioBanco.getDesServicioBanco() ;
			}
			
			fila.add(new CeldaVO(desSerBan,"serBan"));
			fila.add(new CeldaVO(((Long) arrayResult[0]).toString(),"cantTransaccion"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate((Double) arrayResult[1], SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			fila = new FilaVO();
		}
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total","total"));
		filaPie.add(new CeldaVO(cantTotal.toString(),"totalTransaccion"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);
		
		
		printModel.setData(contenedor);
		printModel.setTopeProfundidad(5);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"ReporteTotales5018-"+corrida.getPasoActual().toString()+".pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		return fileName;
	}
	
	/**
	 * Genera el txt "Lista de Advertencias" resultado del paso 1 del proceso de Balance.
	 * 
	 * @param outputDir
	 * @return fileName
	 */
	public String generarTxtAdvertencias(String fileDir) throws Exception{
		String idBalance = StringUtil.formatLong(this.getId());
		String fileName = idBalance+"ListaArdvertencias.txt";
		
		BufferedWriter buffer = new BufferedWriter(new FileWriter(fileDir+"/"+fileName, false));
	
		// --> Resultado
		boolean resultadoVacio = true;
		int c = 0;
		List<String> listAdvertencia = this.getBalSession().getListAdvertencias();
		for(String advertencia: listAdvertencia){
			c++;
			resultadoVacio = false;
			buffer.write(c+" - ");
			buffer.write(advertencia);
			buffer.newLine();
		} 
		if(resultadoVacio ){
			// Sin resultados
			buffer.write("No existen advertencias."  );
		}		 
		
		buffer.close();

		return fileName;
	}

	/**
	 * Validaciones generales para el paso 2. (Ej: Verificación totales de por caja) 
	 * 
	 * <i>(paso 2.1)</i>
	 */
	public void validarCajas() throws Exception{
		
		// Si se requieren validaciones agregar.
	
	}

	/**
	 * <b>Genera Formularios para el paso 2:</b> 
	 * <p>- Reporte de Totales por Sistema: archivo pdf (5018)</p>
	 * <p>- Reporte de Totales por Caja: PDF</p>
	 * <i>(paso 2.2)</i>
	 */
	public void generarFormulariosPaso2(String outputDir) throws Exception{
		
		//-> Reporte de Totales por Sistema (5018) (PDF)
		String fileName = this.generarPdfTotalesPorSistema(outputDir);
		String nombre = "Totales por Sistema (5018)";
		String descripcion = "Permite consultar los totales de transacciones e importes por Sistema.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
		
		//-> Total de transacciones por Caja (PDF)
		fileName = this.generarPdfTotalesPorCaja(outputDir);
		nombre = "Resultados por Caja";
		descripcion = "Detalle de Total de Transacciones por Caja y Sistema.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
	}
	
	/**
	 *  Alta de Procesos de Asentamientos de Pago por Servicio Banco, y Procesos de Delegacion de Asentamiento.
	 *  
	 * <i>(paso 3.1)</i>
	 */
	public void altaProcesosAsociados() throws Exception{
		//. Recorrer la lista de Servicios Banco para crear los Proceso de Asentamiento y/o los de Delegación
			//. para cada servicio banco consulta el tipoProceso (1, 2 o 3)
				//. si es 1 (SIAT) -> Crear Proceso de Asentamiento
				//. si es 2 (NO SIAT) -> Crear Proceso de Delegación de Asentamiento
				//. si es 3 (MIXTO) -> Crear Proceso de Asentamiento y Proceso de Delegación de Asentamiento.
			// (guardar el id de cada proceso creado en un mapa con 'idServicioBanco|tipoProceso' como clave)		
		Map<String, ServicioBanco> mapSerBan = new HashMap<String, ServicioBanco>();
		
		for(TranBal tranBal: this.getListTranBal()){
			Sistema sistema = BalanceCache.getInstance().getSistemaByNro (tranBal.getSistema());
			if(sistema != null){
				ServicioBanco servicioBanco = sistema.getServicioBanco();
				if(mapSerBan.get(servicioBanco.getCodServicioBanco()) == null){
					mapSerBan.put(servicioBanco.getCodServicioBanco(), servicioBanco);
				}
			}
		} 
		for(Caja69 caja69: this.getListCaja69()){
			Sistema sistema = BalanceCache.getInstance().getSistemaByNro (caja69.getSistema());
			if(sistema != null){
				ServicioBanco servicioBanco = sistema.getServicioBanco();
				if(mapSerBan.get(servicioBanco.getCodServicioBanco()) == null){
					mapSerBan.put(servicioBanco.getCodServicioBanco(), servicioBanco);
				}
			}
		}
		List<ServicioBanco> listServicioBanco = new ArrayList<ServicioBanco>();//ServicioBanco.getListActivos();
		listServicioBanco.addAll(mapSerBan.values());
		
		for(ServicioBanco servicioBanco: listServicioBanco){
			if(servicioBanco.getTipoAsentamiento().intValue() == ServicioBanco.TIPO_ASENTAMIENTO_SIAT 
					|| servicioBanco.getTipoAsentamiento().intValue() == ServicioBanco.TIPO_ASENTAMIENTO_MIXTO){
				
				Asentamiento asentamiento = new Asentamiento();
				asentamiento.setBalance(this);
				asentamiento.setFechaBalance(this.getFechaBalance());
				asentamiento.setEjercicio(this.getEjercicio());
				asentamiento.setServicioBanco(servicioBanco);
				asentamiento.setObservacion("Creado por el Proceso de Balance nro "+this.getId()+".");
				asentamiento.setUsuarioAlta("Balance");
	            asentamiento.setEstado(Estado.ACTIVO.getId());
	      
	            //--> Crear Corrida para asentamiento   
	            Proceso proceso = Proceso.getByCodigo(Proceso.PROCESO_ASENTAMIENTO);     
	            Corrida corrida = null;
	            AdpRun run = null;
	            if(proceso!=null){
	            	String desCorrida = proceso.getDesProceso()+" - "+servicioBanco.getDesServicioBanco()+" - "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK);
	            	run = AdpRun.newRun(proceso.getCodProceso(), desCorrida);
	            	run.create();
	            	
	            	corrida = Corrida.getByIdNull(run.getId());
	            }
	            //<-- Fin Crear Corrida para asentamiento
	            if(corrida == null){
	            	this.addRecoverableValueError("No se pudo crear el proceso de asentamiento para el Servicio Banco "+servicioBanco.getCodServicioBanco()+" - "+servicioBanco.getDesServicioBanco());	            	
	            }else{
	            	asentamiento.setCorrida(corrida);
	            	BalDAOFactory.getAsentamientoDAO().update(asentamiento); 
	         
	            	if(run!=null && !asentamiento.hasError())
	                	run.putParameter("idAsentamiento", asentamiento.getId().toString());            	
	         
	            }
	            this.getBalSession().getMapProcesos().put(servicioBanco.getId()+"|"+ServicioBanco.TIPO_ASENTAMIENTO_SIAT, asentamiento.getId());
			}
			if(servicioBanco.getTipoAsentamiento().intValue() == ServicioBanco.TIPO_ASENTAMIENTO_NO_SIAT
					|| servicioBanco.getTipoAsentamiento().intValue() == ServicioBanco.TIPO_ASENTAMIENTO_MIXTO){

				AseDel aseDel = new AseDel();
				aseDel.setBalance(this);
				aseDel.setFechaBalance(this.getFechaBalance());
				aseDel.setEjercicio(this.getEjercicio());
				aseDel.setServicioBanco(servicioBanco);
				aseDel.setObservacion("Creado por el Proceso de Balance nro "+this.getId()+".");
				aseDel.setUsuarioAlta("Balance");
	            aseDel.setEstado(Estado.ACTIVO.getId());
	      
	            //--> Crear Corrida para aseDel   
	            Proceso proceso = Proceso.getByCodigo(Proceso.PROCESO_ASEDEL);        
	            Corrida corrida = null;
	            AdpRun run = null;
	            if(proceso!=null){
	            	String desCorrida = proceso.getDesProceso()+" - "+servicioBanco.getDesServicioBanco()+" - "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK);
	            	run = AdpRun.newRun(proceso.getCodProceso(), desCorrida);
	            	run.create();
	            	
	            	corrida = Corrida.getByIdNull(run.getId());
	            }
	            //<-- Fin Crear Corrida para aseDel
	            if(corrida == null){
	            	this.addRecoverableValueError("No se pudo crear el proceso de delegación de asentamiento para el Servicio Banco "+servicioBanco.getCodServicioBanco()+" - "+servicioBanco.getDesServicioBanco());	            	
	            }else{
	            	aseDel.setCorrida(corrida);
	            	BalDAOFactory.getAseDelDAO().update(aseDel); 	
	            	
	            	if(run!=null && !aseDel.hasError())
	                	run.putParameter("idAseDel", aseDel.getId().toString());            	

	            }
	            this.getBalSession().getMapProcesos().put(servicioBanco.getId()+"|"+ServicioBanco.TIPO_ASENTAMIENTO_NO_SIAT, aseDel.getId());
			}
			
		}
	}
	
	/**
	 *  Crea las transacciones en bal_transaccion (y/o bal_tranDel) obtenidas de la tabla bal_tranBal
	 *  asociadas al Asentamiento que corresponda según el Servicio Banco.
	 *  
	 * <i>(paso 3.2)</i>
	 */
	public void procesarTranBal() throws Exception{
		for(TranBal tranBal: this.getListTranBal()){
			Sistema sistema = BalanceCache.getInstance().getSistemaByNro (tranBal.getSistema());
			if(sistema == null){
				this.registrarIndeterminado("No existe el Sistema de Nro: "+tranBal.getSistema()+" en la DB.",tranBal, "56"); 
				continue;
			}
			ServicioBanco servicioBanco = sistema.getServicioBanco();
			if(servicioBanco.getTipoAsentamiento().longValue() == ServicioBanco.TIPO_ASENTAMIENTO_SIAT.longValue() 
					|| servicioBanco.getTipoAsentamiento().longValue() == ServicioBanco.TIPO_ASENTAMIENTO_MIXTO.longValue()){
				// Indeterminamos la transaccion si tiene clave 8888
				if("8888".equals(tranBal.getClave()) || "888888".equals(tranBal.getClave())){
					this.registrarIndeterminado("Transaccion de Recibo Web. Clave: 888888",tranBal, "58");  
					continue;				
				}
				Long idAsentamiento = this.getBalSession().getMapProcesos().get(servicioBanco.getId()+"|"+ServicioBanco.TIPO_ASENTAMIENTO_SIAT);
				if(idAsentamiento == null){
					this.addRecoverableValueError("No se encontró en el mapa el proceso de asentamiento para el Servicio Banco "+servicioBanco.getCodServicioBanco()+" - "+servicioBanco.getDesServicioBanco());
					return;
				}
				Asentamiento asentamiento = Asentamiento.getById(idAsentamiento);
				if(asentamiento == null){
					this.addRecoverableValueError("No se encontró el proceso de asentamiento creado para el Servicio Banco "+servicioBanco.getCodServicioBanco()+" - "+servicioBanco.getDesServicioBanco());
					return;
				}
				// Armar Transaccion para Asentamiento
				Transaccion transaccion = null;
				if(servicioBanco.getCodServicioBanco().equals("81")){
	    			transaccion = new TransaccionTGI();
	    		}else if(servicioBanco.getCodServicioBanco().equals("82")){
	    			transaccion = new TransaccionCDM();
	    		}else if(servicioBanco.getCodServicioBanco().equals("83")){
	    			transaccion = new TransaccionDREI();
	    		}else if(servicioBanco.getCodServicioBanco().equals("84")){
	    			transaccion = new TransaccionETUR();
	    		}else if(servicioBanco.getCodServicioBanco().equals("85")){
	    			transaccion = new TransaccionOTRTRI();
	    		}else if(servicioBanco.getCodServicioBanco().equals("88")){
	    			transaccion = new TransaccionSEL();
	    		}else if(servicioBanco.getCodServicioBanco().equals("86")){
	    			transaccion = new TransaccionCEM();
	    		}else if(servicioBanco.getCodServicioBanco().equals("89")){
	    			transaccion = new TransaccionDER();
	    		}else if(servicioBanco.getCodServicioBanco().equals("99")){
	    			transaccion = new TransaccionRCM();
	    		}else{
	    			this.addRecoverableValueError("El asentamiento para el Servicio Banco: "+servicioBanco.getCodServicioBanco()+" no está implementado en SIAT. Verificar configuración de datos maestros.");
					return;
	    		}
	    		
	     		transaccion.setAsentamiento(asentamiento);
	     		Long nroSistema = tranBal.getSistema();
	     		// Casos particulares sobre algunos nro de sistemas
	     		if(nroSistema == 1)
	     			nroSistema = 2L;
	     		transaccion.setSistema(nroSistema);
	     		transaccion.setNroComprobante(tranBal.getNroComprobante());
	     		transaccion.setFechaPago(tranBal.getFechaPago());

	     		// Formatear clave (readapta el string para el procesamiento posterior)
	     		String clave = this.formatearClave(tranBal.getClave(), sistema);
	     		// Obtener valores para AnioComprobante y Periodo
	     		if(clave.length()<6){
	     			this.addRecoverableValueError("El campo clave del registro de transaccion correspondiente al registro de tranBal de id "+tranBal.getId()+" no tiene el formato esperado.");
	     			continue;
	     		}
	     		if(sistema.getEsServicioBanco().intValue()==SiNo.SI.getId().intValue()){
	     			Long tipoBoleta = -1L;
	     			if(Long.valueOf(clave.substring(0, 4))<1000 && Long.valueOf(clave.substring(4, 6))==0){
	     				// Se divide por diez porque en las transacciones se recibe el tipo de boleta movido una posicion decimal.
	     				if(Long.valueOf(clave.substring(0, 4)).longValue() < 10){
	     					tipoBoleta = new Long(clave.substring(0, 4)); 
	     				}else{
	     					tipoBoleta = new Long(clave.substring(0, 4))/10; 	     					
	     				}
	     			}
     				transaccion.setAnioComprobante(tipoBoleta);
     				transaccion.setPeriodo(0L);	     				     				
	     		}else{
	     			// Caso Servicio Banco Normal
	     			if(!servicioBanco.getCodServicioBanco().equals("85")){
	     				if(Long.valueOf(clave.substring(0, 4))>1000 && Long.valueOf(clave.substring(0, 4))<7700){
	     					// Si corresponde al formato de aaaaMM (anio+periodo)
	     					transaccion.setAnioComprobante(new Long(clave.substring(0, 4)));
	     					transaccion.setPeriodo(new Long(clave.substring(4, 6)));
	     				}else if(Long.valueOf(clave)==999999){
	     					transaccion.setAnioComprobante(99L);
	     					transaccion.setPeriodo(99L);
	     				}else if(Long.valueOf(clave)==888888){ 
	     					transaccion.setAnioComprobante(88L);
	     					transaccion.setPeriodo(88L);
	     				}else if(Long.valueOf(clave)==777777){
	     					transaccion.setAnioComprobante(77L);
	     					transaccion.setPeriodo(77L);
	     				}else{
	     					transaccion.setAnioComprobante(0L);
	     					transaccion.setPeriodo(new Long(clave));	     				     				
	     				}	  
	     				// Caso Especial para Recibo de Convenio de Servicio Banco DREI de transacciones NO SIAT
	         			if(servicioBanco.getCodServicioBanco().equals("83")){
	         				if(sistema.getTipoDeuda().getId().longValue() == TipoDeuda.ID_PLAN_DE_PAGOS.longValue()){
	         					// Si en el campo clave el nro es mayor a 1000 consideramos que es un anio y la transaccion es reciboConvenio 
		     					if(Long.valueOf(clave) > 1000){
		     						transaccion.setAnioComprobante(new Long(clave));
		     						transaccion.setPeriodo(0L);	
		     					}
	         				}
	         			}
	     			// Caso Especial para Servicio Banco 85, Ex Gravamenes Especiales
	     			}else{
	     			    if(Long.valueOf(clave)==999999){
	     					transaccion.setAnioComprobante(99L);
	     					transaccion.setPeriodo(99L);
	     				}else if(Long.valueOf(clave)==888888){ 
	     					transaccion.setAnioComprobante(88L);
	     					transaccion.setPeriodo(88L);
	     				}else if(Long.valueOf(clave)==777777){
	     					transaccion.setAnioComprobante(77L);
	     					transaccion.setPeriodo(77L);
	     				}else{
	     					// Si es Deuda (Recibo de Deuda)
		     				if(sistema.getTipoDeuda().getId().longValue() == TipoDeuda.ID_DEUDA_PURA.longValue()){
		     					transaccion.setAnioComprobante(new Long(clave.substring(2, 6)));
			     				transaccion.setPeriodo(0L);		
		     				// Si es Plan de Pagos (Cuota o Recibo de Cuota)
		     				}else if(sistema.getTipoDeuda().getId().longValue() == TipoDeuda.ID_PLAN_DE_PAGOS.longValue()){
		     					// Si en el campo clave el nro es mayor a 1000 consideramos que es un anio y la transaccion es reciboConvenio 
		     					if(Long.valueOf(clave) > 1000){
		     						transaccion.setAnioComprobante(new Long(clave));
		     						transaccion.setPeriodo(0L);		
		     					// Si en el campo clave el nro es menor a 1000 consideramos que es una cuota y la transaccion es de cuota	     						
		     					}else{
		     						transaccion.setAnioComprobante(0L);
		     						transaccion.setPeriodo(new Long(clave));
		     					}
		     				}
	     				}	     					     				
	     			}
	     		}
	     		// Caso especial para transacciones de Osiris
	     		if(tranBal.getOrigen() != null && tranBal.getOrigen().intValue() == Transaccion.ORIGEN_OSIRIS.intValue()){
	     			if(tranBal.getFormulario() != null && tranBal.getFormulario().intValue() == FormularioAfip.DREI_MULTAS_6053.getId()){
	     				Long tipoBoleta = -1L;
		     			if(Long.valueOf(clave.substring(0, 4))<1000 && Long.valueOf(clave.substring(4, 6))==0){
		     				// Se divide por diez porque en las transacciones se recibe el tipo de boleta movido una posicion decimal.
		     				if(Long.valueOf(clave.substring(0, 4)).longValue() < 10){
		     					tipoBoleta = new Long(clave.substring(0, 4)); 
		     				}else{
		     					tipoBoleta = new Long(clave.substring(0, 4))/10; 	     					
		     				}
		     			}
	     				transaccion.setAnioComprobante(tipoBoleta);
	     				transaccion.setPeriodo(0L);	    
	     			}else{
	     				// Corresponde al formato de aaaaMM (anio+periodo)
     					transaccion.setAnioComprobante(new Long(clave.substring(0, 4)));
     					transaccion.setPeriodo(new Long(clave.substring(4, 6)));
	     			}
	     		}
	     		transaccion.setResto(tranBal.getResto());
	     		transaccion.setCodPago(tranBal.getCodPago()); 
	     		transaccion.setCaja(tranBal.getCaja()); 
	     		transaccion.setCodTr(tranBal.getCodTr()); 
	     		transaccion.setImporte(tranBal.getImporte()); 
	     		transaccion.setRecargo(tranBal.getRecargo());
	     		transaccion.setPaquete(tranBal.getPaquete());
	     		transaccion.setMarcaTr(tranBal.getMarcaTr());
	     		transaccion.setReciboTr(tranBal.getReciboTr());
	    		transaccion.setFechaBalance(tranBal.getFechaBalance());
	     		transaccion.setEsIndet(0);
	     		transaccion.setNroLinea(tranBal.getNroLinea());
	     		// Averiguamos el Tipo Boleta y se lo seteamos a la Transaccion.
	     		Long tipoBoleta = -1L;
	    		if(sistema.getEsServicioBanco().intValue() == SiNo.SI.getId().intValue()){
	    			tipoBoleta = transaccion.getAnioComprobante();
	    		}else {
	    			if(sistema.getTipoDeuda().getId().longValue() == TipoDeuda.ID_DEUDA_PURA){
	    				// Caso Servicio Banco Normal
		     			if(!servicioBanco.getCodServicioBanco().equals("85")){
		     				if((transaccion.getAnioComprobante()== 99L && transaccion.getPeriodo() == 99L) || (transaccion.getAnioComprobante()== 77L && transaccion.getPeriodo() == 77L))
		     					tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_DEUDA;
		     				else
		     					tipoBoleta = Transaccion.TIPO_BOLETA_DEUDA;		     				
		     			// Caso Especial para Servicio Banco 85, Ex Gravamenes Especiales
		     			}else{
		     				tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_DEUDA;
		     			}
	    			}
	    			if(sistema.getTipoDeuda().getId().longValue() == TipoDeuda.ID_PLAN_DE_PAGOS){
	    				// Caso Servicio Banco Normal
	    				if(!servicioBanco.getCodServicioBanco().equals("85")){
	    					if(transaccion.getAnioComprobante()== 99L && transaccion.getPeriodo() == 99L)
	    						tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_CUOTA;
	    					else
	    						tipoBoleta = Transaccion.TIPO_BOLETA_CUOTA;	    
	    					
	    					// Caso Especial para Recibo de Convenio de Servicio Banco 83, DREI 
	     	     			if(servicioBanco.getCodServicioBanco().equals("83")){
     	     					//  Si el anio es distinto de cero y el periodo es cero
	     	     				if(transaccion.getAnioComprobante() != 0 && transaccion.getPeriodo() == 0){
		     						tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_CUOTA;
     	     					}
	     	     			}
	    					
	    				// Caso Especial para Servicio Banco 85, Ex Gravamenes Especiales
		     			}else{
		     				// Si en el campo clave el nro es mayor a 1000 consideramos que es un anio y la transaccion es reciboConvenio 
	     					if(transaccion.getAnioComprobante() != 0 && transaccion.getPeriodo() == 0){
	     						tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_CUOTA;		     								     				
	     					// Si en el campo clave el nro es menor a 1000 consideramos que es una cuota y la transaccion es de cuota	     						
	     					}else{
	     						tipoBoleta = Transaccion.TIPO_BOLETA_CUOTA;	
	     					}
		     			}
	    			}
	    		}
	    		// Caso especial para transacciones de Osiris
	     		if(tranBal.getOrigen() != null && tranBal.getOrigen().intValue() == Transaccion.ORIGEN_OSIRIS.intValue()){
	     			tipoBoleta = Transaccion.TIPO_BOLETA_DEUDA;	
	     		}
	     		transaccion.setTipoBoleta(tipoBoleta);	     		
	     			    		
	     		// Para el caso de transacciones de deuda para un Servicio Banco de Tributos Autoliquidables,
	     		// debemos marcar si corresponde o no a un pago rectificativo.
	     		if(SiNo.SI.getId().intValue() == servicioBanco.getEsAutoliquidable().intValue() 
	     								&& Transaccion.TIPO_BOLETA_DEUDA.equals(tipoBoleta)){
	     			// Verificamos el Resto (para las transacciones nuevas de SIAT, se usa para indicar si es rectificativa o no.)
	     			if(transaccion.getResto().longValue() > 0L && transaccion.getResto().longValue() < 10L){
	     				transaccion.setEsRectificativa(SiNo.SI.getId());
	     			}else{
	     				transaccion.setEsRectificativa(SiNo.NO.getId());
	     			}	  
	     			// Verificamos el Periodo. Si es mayor que 50, lo actualizamos restandole 50 y lo marcamos como Rectificativa (para transacciones anteriores a SIAT)
	     			if(transaccion.getPeriodo().longValue() > 50){
	     				transaccion.setPeriodo(transaccion.getPeriodo()-50);
	     				transaccion.setEsRectificativa(SiNo.SI.getId());
	     			}
	     		}
	     		
	     		// Pasamos el origen de la transaccion
	     		transaccion.setOrigen(tranBal.getOrigen());
	     		
	     		// Pasamos el Formulario (solo tiene valor para transacciones de origen Osiris, sino se graba en 0)
	     		transaccion.setFormulario(tranBal.getFormulario());

	     		// Pasamos el Id de Transaccion Afip (solo tiene valor para transacciones de origen Osiris, sino se graba en 0)
	     		transaccion.setIdTranAfip(tranBal.getIdTranAfip());

	     		// Creamos el registro de transaccion
	     		asentamiento.createTransaccion(transaccion); 
	     		if(transaccion.hasError()){
	     			for(DemodaStringMsg error:transaccion.getListError()){
						this.addRecoverableError(error);
					}     			
	     		}
			}else if(servicioBanco.getTipoAsentamiento().longValue() == ServicioBanco.TIPO_ASENTAMIENTO_NO_SIAT.longValue()){
				Long idDelegador = this.getBalSession().getMapProcesos().get(servicioBanco.getId()+"|"+ServicioBanco.TIPO_ASENTAMIENTO_NO_SIAT);
				if(idDelegador == null){
					this.addRecoverableValueError("No se encontró en el mapa el proceso de delegación de asentamiento para el Servicio Banco "+servicioBanco.getCodServicioBanco()+" - "+servicioBanco.getDesServicioBanco());
					return;
				}
				AseDel aseDel = AseDel.getById(idDelegador);
				if(aseDel == null){
					this.addRecoverableValueError("No se encontró el proceso de delegación de asentamiento creado para el Servicio Banco "+servicioBanco.getCodServicioBanco()+" - "+servicioBanco.getDesServicioBanco());
					return;
				}
				// Armar Transaccion para Delegador de Asentamiento
				TranDel tranDel = new TranDel();
				
	     		tranDel.setAseDel(aseDel);
	     		Long nroSistema = tranBal.getSistema();
	     		// Casos particulares sobre algunos nro de sistemas
	     		if(nroSistema == 1)
	     			nroSistema = 2L;
	     		tranDel.setSistema(nroSistema);
	     		tranDel.setNroComprobante(tranBal.getNroComprobante());
	     		tranDel.setFechaPago(tranBal.getFechaPago());

	     		// Formatear clave (readapta el string para el procesamiento posterior)
				String clave = this.formatearClave(tranBal.getClave(), sistema);
				// Obtener valores para AnioComprobante y Periodo
	     		if(clave.length()<6){
	     			this.addRecoverableValueError("El registro de tranDel correspondiente al registro de tranBal de id "+tranBal.getId()+" no tiene el formato esperado.");
	     			continue;
	     		}
	     		// Si corresponde al formato de aaaaMM (anio+periodo)
	     		if(Long.valueOf(clave.substring(0, 4))>1000 && Long.valueOf(clave.substring(0, 4))<7700){
	     			tranDel.setAnioComprobante(new Long(clave.substring(0, 4)));
	     			tranDel.setPeriodo(new Long(clave.substring(4, 6)));
	     		}else if(Long.valueOf(clave)==999999){
	     			tranDel.setAnioComprobante(9999L);
	     			tranDel.setPeriodo(99L);
	     		}else if(Long.valueOf(clave)==888888){  
	     			tranDel.setAnioComprobante(8888L);
	     			tranDel.setPeriodo(88L);
	     		}else if(Long.valueOf(clave)==777777){
	     			tranDel.setAnioComprobante(7777L);
	     			tranDel.setPeriodo(77L);
	     		}else{
	     			tranDel.setAnioComprobante(0L);
	     			tranDel.setPeriodo(new Long(clave));	     				     				
	     		}
	     		tranDel.setResto(tranBal.getResto());
	     		tranDel.setCodPago(tranBal.getCodPago()); 
	     		tranDel.setCaja(tranBal.getCaja()); 
	     		tranDel.setCodTr(tranBal.getCodTr()); 
	     		tranDel.setImporte(tranBal.getImporte()); 
	     		tranDel.setRecargo(tranBal.getRecargo());
	     		tranDel.setPaquete(tranBal.getPaquete());
	     		tranDel.setMarcaTr(tranBal.getMarcaTr());
	     		tranDel.setReciboTr(tranBal.getReciboTr());
	    		tranDel.setFechaBalance(tranBal.getFechaBalance());
	     		tranDel.setNroLinea(tranBal.getNroLinea());
	     			    		
	     		// Creamos el registro de tranDel
	     		aseDel.createTranDel(tranDel); 
	     		if(tranDel.hasError()){
	     			for(DemodaStringMsg error:tranDel.getListError()){
						this.addRecoverableError(error);
					}     			
	     		}
				
			}
		}
	}
	
	/**
	 *  Crea las transacciones en bal_transaccion (y/o bal_tranDel) obtenidas de la tabla bal_caja69
	 *  asociadas al Asentamiento que corresponda según el Servicio Banco.
	 *  
	 * <i>(paso 3.3)</i>
	 */
	public void procesarCaja69() throws Exception{
		for(Caja69 caja69: this.getListCaja69()){
			Sistema sistema = BalanceCache.getInstance().getSistemaByNro (caja69.getSistema());
			if(sistema == null){
				this.addRecoverableValueError("No existe el Sistema de Nro: "+caja69.getSistema()+" en la DB. Error en registro de id "+caja69.getId()+" de la tabla bal_caja69.");				
				continue;
			}
			ServicioBanco servicioBanco = sistema.getServicioBanco();
			if(servicioBanco.getTipoAsentamiento().longValue() == ServicioBanco.TIPO_ASENTAMIENTO_SIAT.longValue() 
					|| servicioBanco.getTipoAsentamiento().longValue() == ServicioBanco.TIPO_ASENTAMIENTO_MIXTO.longValue()){
				// Indeterminamos la transaccion si tiene clave 8888 
				if("8888".equals(caja69.getClave()) || "888888".equals(caja69.getClave())){
					this.registrarIndeterminado("Transaccion de Recibo Web. Clave: 888888",caja69, "58");  
					continue;				
				}
				Long idAsentamiento = this.getBalSession().getMapProcesos().get(servicioBanco.getId()+"|"+ServicioBanco.TIPO_ASENTAMIENTO_SIAT);
				if(idAsentamiento == null){
					this.addRecoverableValueError("No se encontró en el mapa el proceso de asentamiento para el Servicio Banco "+servicioBanco.getCodServicioBanco()+" - "+servicioBanco.getDesServicioBanco());
					return;
				}
				Asentamiento asentamiento = Asentamiento.getById(idAsentamiento);
				if(asentamiento == null){
					this.addRecoverableValueError("No se encontró el proceso de asentamiento creado para el Servicio Banco "+servicioBanco.getCodServicioBanco()+" - "+servicioBanco.getDesServicioBanco());
					return;
				}
				// Armar Transaccion para Asentamiento
				Transaccion transaccion = null;
				if(servicioBanco.getCodServicioBanco().equals("81")){
	    			transaccion = new TransaccionTGI();
	    		}else if(servicioBanco.getCodServicioBanco().equals("82")){
	    			transaccion = new TransaccionCDM();
	    		}else if(servicioBanco.getCodServicioBanco().equals("83")){
	    			transaccion = new TransaccionDREI();
	    		}else if(servicioBanco.getCodServicioBanco().equals("84")){
	    			transaccion = new TransaccionETUR();
	    		}else if(servicioBanco.getCodServicioBanco().equals("85")){
	    			transaccion = new TransaccionOTRTRI();
	    		}else if(servicioBanco.getCodServicioBanco().equals("88")){ 
	    			transaccion = new TransaccionSEL();
	    		}else if(servicioBanco.getCodServicioBanco().equals("86")){
	    			transaccion = new TransaccionCEM();
	    		}else if(servicioBanco.getCodServicioBanco().equals("89")){
	    			transaccion = new TransaccionDER();
	    		}else if(servicioBanco.getCodServicioBanco().equals("99")){
	    			transaccion = new TransaccionRCM();
	    		}else{
	    			this.addRecoverableValueError("El asentamiento para el Servicio Banco: "+servicioBanco.getCodServicioBanco()+" no está implementado en SIAT. Verificar configuración de datos maestros.");
					return;
	    		}
				
	     		transaccion.setAsentamiento(asentamiento);
	     		Long nroSistema = caja69.getSistema();
	     		// Casos particulares sobre algunos nro de sistemas
	     		if(nroSistema == 1)
	     			nroSistema = 2L;
	     		transaccion.setSistema(nroSistema);
	     		transaccion.setNroComprobante(caja69.getNroComprobante());
	     		transaccion.setFechaPago(caja69.getFechaPago());
	
	     		// Formatear clave (readapta el string para el procesamiento posterior)
				String clave = this.formatearClave(caja69.getClave(), sistema);
				// Obtener valores para AnioComprobante y Periodo
	     		if(clave.length()<6){
	     			this.addRecoverableValueError("El campo clave del registro de transaccion correspondiente al registro de caja69 de id "+caja69.getId()+" no tiene el formato esperado.");
	     			continue;
	     		}
	     		if(sistema.getEsServicioBanco().intValue()==SiNo.SI.getId().intValue()){
	     			Long tipoBoleta = -1L;
	     			if(sistema.getNroSistema().longValue() == 88L){
	     				tipoBoleta = Transaccion.TIPO_BOLETA_SELLADO;
	     			}else if(Long.valueOf(clave.substring(0, 4))<1000 && Long.valueOf(clave.substring(4, 6))==0){
	     				tipoBoleta = new Long(clave.substring(0, 4));
	     			}
	 				transaccion.setAnioComprobante(tipoBoleta);
	 				transaccion.setPeriodo(0L);	     				     				
	     		}else{
	     			// Caso Servicio Banco Normal
	     			if(!servicioBanco.getCodServicioBanco().equals("85")){
	     				if(Long.valueOf(clave.substring(0, 4))>1000 && Long.valueOf(clave.substring(0, 4))<7700){
	     					// Si corresponde al formato de aaaaMM (anio+periodo)
	     					transaccion.setAnioComprobante(new Long(clave.substring(0, 4)));
	     					transaccion.setPeriodo(new Long(clave.substring(4, 6)));
	     				}else if(Long.valueOf(clave)==999999){
	     					transaccion.setAnioComprobante(99L);
	     					transaccion.setPeriodo(99L);
	     				}else if(Long.valueOf(clave)==888888){ 
	     					transaccion.setAnioComprobante(88L);
	     					transaccion.setPeriodo(88L);
	     				}else if(Long.valueOf(clave)==777777){
	     					transaccion.setAnioComprobante(77L);
	     					transaccion.setPeriodo(77L);
	     				}else{
	     					transaccion.setAnioComprobante(0L);
	     					transaccion.setPeriodo(new Long(clave));	     				     				
	     				}	  
	     				// Caso Especial para Recibo de Convenio de Servicio Banco DREI de transacciones NO SIAT
	         			if(servicioBanco.getCodServicioBanco().equals("83")){
	         				if(sistema.getTipoDeuda().getId().longValue() == TipoDeuda.ID_PLAN_DE_PAGOS.longValue()){
	         					// Si en el campo clave el nro es mayor a 1000 consideramos que es un anio y la transaccion es reciboConvenio 
		     					if(Long.valueOf(clave) > 1000){
		     						transaccion.setAnioComprobante(new Long(clave));
		     						transaccion.setPeriodo(0L);	
		     					}
	         				}
	         			}
	     			// Caso Especial para Servicio Banco 85, Ex Gravamenes Especiales
	     			}else{
	     			    if(Long.valueOf(clave)==999999){
	     					transaccion.setAnioComprobante(99L);
	     					transaccion.setPeriodo(99L);
	     				}else if(Long.valueOf(clave)==888888){ 
	     					transaccion.setAnioComprobante(88L);
	     					transaccion.setPeriodo(88L);
	     				}else if(Long.valueOf(clave)==777777){
	     					transaccion.setAnioComprobante(77L);
	     					transaccion.setPeriodo(77L);
	     				}else{
	     					// Si es Deuda (Recibo de Deuda)
		     				if(sistema.getTipoDeuda().getId().longValue() == TipoDeuda.ID_DEUDA_PURA.longValue()){
		     					transaccion.setAnioComprobante(new Long(clave.substring(2, 6)));
			     				transaccion.setPeriodo(0L);		
		     				// Si es Plan de Pagos (Cuota o Recibo de Cuota)
		     				}else if(sistema.getTipoDeuda().getId().longValue() == TipoDeuda.ID_PLAN_DE_PAGOS.longValue()){
		     					// Si en el campo clave el nro es mayor a 1000 consideramos que es un anio y la transaccion es reciboConvenio 
		     					if(Long.valueOf(clave) > 1000){
		     						transaccion.setAnioComprobante(new Long(clave));
		     						transaccion.setPeriodo(0L);		
		     					// Si en el campo clave el nro es menor a 1000 consideramos que es una cuota y la transaccion es de cuota	     						
		     					}else{
		     						transaccion.setAnioComprobante(0L);
		     						transaccion.setPeriodo(new Long(clave));
		     					}
		     				}
	     				}	     					     				
	     			}
	     		}
	     		transaccion.setResto(caja69.getResto());
	     		transaccion.setCodPago(caja69.getCodPago()); 
	     		transaccion.setCaja(caja69.getCaja()); 
	     		transaccion.setCodTr(caja69.getCodTr()); 
	     		transaccion.setImporte(caja69.getImporte()); 
	     		transaccion.setRecargo(caja69.getRecargo());
	     		transaccion.setPaquete(caja69.getPaquete());
	     		transaccion.setMarcaTr(caja69.getMarcaTr());
	     		transaccion.setReciboTr(caja69.getReciboTr());
	    		transaccion.setFechaBalance(caja69.getFechaBalance());
	     		transaccion.setEsIndet(0);
	     		transaccion.setNroLinea(caja69.getNroLinea());
	     		//	 Averiguamos el Tipo Boleta y se lo seteamos a la Transaccion.
	     		Long tipoBoleta = -1L;
	    		if(sistema.getEsServicioBanco().intValue() == SiNo.SI.getId().intValue()){
	    			tipoBoleta = transaccion.getAnioComprobante();
	    		}else {
	    			if(sistema.getTipoDeuda().getId().longValue() == TipoDeuda.ID_DEUDA_PURA){
	    				// Caso Servicio Banco Normal
		     			if(!servicioBanco.getCodServicioBanco().equals("85")){
		     				if((transaccion.getAnioComprobante()== 99L && transaccion.getPeriodo() == 99L) || (transaccion.getAnioComprobante()== 77L && transaccion.getPeriodo() == 77L))
		     					tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_DEUDA;
		     				else
		     					tipoBoleta = Transaccion.TIPO_BOLETA_DEUDA;		     				
		     			// Caso Especial para Servicio Banco 85, Ex Gravamenes Especiales
		     			}else{
		     				tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_DEUDA;
		     			}
	    			}
	    			if(sistema.getTipoDeuda().getId().longValue() == TipoDeuda.ID_PLAN_DE_PAGOS){
	    				// Caso Servicio Banco Normal
	    				if(!servicioBanco.getCodServicioBanco().equals("85")){
	    					if(transaccion.getAnioComprobante()== 99L && transaccion.getPeriodo() == 99L)
	    						tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_CUOTA;
	    					else
	    						tipoBoleta = Transaccion.TIPO_BOLETA_CUOTA;	    
	    					
	    					// Caso Especial para Recibo de Convenio de Servicio Banco 83, DREI 
	     	     			if(servicioBanco.getCodServicioBanco().equals("83")){
     	     					//  Si el anio es distinto de cero y el periodo es cero
	     	     				if(transaccion.getAnioComprobante() != 0 && transaccion.getPeriodo() == 0){
		     						tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_CUOTA;
     	     					}
	     	     			}
	    					
	    				// Caso Especial para Servicio Banco 85, Ex Gravamenes Especiales
		     			}else{
		     				// Si en el campo clave el nro es mayor a 1000 consideramos que es un anio y la transaccion es reciboConvenio 
	     					if(transaccion.getAnioComprobante() != 0 && transaccion.getPeriodo() == 0){
	     						tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_CUOTA;		     								     				
	     					// Si en el campo clave el nro es menor a 1000 consideramos que es una cuota y la transaccion es de cuota	     						
	     					}else{
	     						tipoBoleta = Transaccion.TIPO_BOLETA_CUOTA;	
	     					}
		     			}
	    			}
	    		}
	     		transaccion.setTipoBoleta(tipoBoleta);	     
	     			    		
	     		// Para el caso de transacciones de deuda para un Servicio Banco de Tributos Autoliquidables,
	     		// debemos marcar si corresponde o no a un pago rectificativo.
	     		if(SiNo.SI.getId().intValue() == servicioBanco.getEsAutoliquidable().intValue() 
	     								&& Transaccion.TIPO_BOLETA_DEUDA.equals(tipoBoleta)){
	     			if(transaccion.getResto().longValue() > 0L && transaccion.getResto().longValue() < 10L){
	     				transaccion.setEsRectificativa(SiNo.SI.getId());
	     			}else{
	     				transaccion.setEsRectificativa(SiNo.NO.getId());
	     			}	
	     			// Verificamos el Periodo. Si es mayor que 50, lo actualizamos restandole 50 y lo marcamos como Rectificativa (para transacciones anteriores a SIAT)
	     			if(transaccion.getPeriodo().longValue() > 50){
	     				transaccion.setPeriodo(transaccion.getPeriodo()-50);
	     				transaccion.setEsRectificativa(SiNo.SI.getId());
	     			}
	     		}
	     		// Si tiene seteado el campo Tipo Cancelacion (para indicar pago parcial o pago por menos) se pasa a la transaccion a crear.
	     		if(caja69.getTipoCancelacion() != null){
	     			transaccion.setTipoCancelacion(caja69.getTipoCancelacion());
	     		}
	     		
	     		// Grabamos como origen 0. 
	     		transaccion.setOrigen(0);
	     		
	     		// Creamos el registro de transaccion
	     		asentamiento.createTransaccion(transaccion); 
	     		if(transaccion.hasError()){
	     			for(DemodaStringMsg error:transaccion.getListError()){
						this.addRecoverableError(error);
					}     			
	     		}
			}else if(servicioBanco.getTipoAsentamiento().intValue() == ServicioBanco.TIPO_ASENTAMIENTO_NO_SIAT){
				Long idDelegador = this.getBalSession().getMapProcesos().get(servicioBanco.getId()+"|"+ServicioBanco.TIPO_ASENTAMIENTO_NO_SIAT);
				if(idDelegador == null){
					this.addRecoverableValueError("No se encontró en el mapa el proceso de delegación de asentamiento para el Servicio Banco "+servicioBanco.getCodServicioBanco()+" - "+servicioBanco.getDesServicioBanco());
					return;
				}
				AseDel aseDel = AseDel.getById(idDelegador);
				if(aseDel == null){
					this.addRecoverableValueError("No se encontró el proceso de delegación de asentamiento creado para el Servicio Banco "+servicioBanco.getCodServicioBanco()+" - "+servicioBanco.getDesServicioBanco());
					return;
				}
				// Armar Transaccion para Delegador de Asentamiento
				TranDel tranDel = new TranDel();
				
	     		tranDel.setAseDel(aseDel);
	     		Long nroSistema = caja69.getSistema();
	     		// Casos particulares sobre algunos nro de sistemas
	     		if(nroSistema == 1)
	     			nroSistema = 2L;
	     		tranDel.setSistema(nroSistema);
	     		tranDel.setNroComprobante(caja69.getNroComprobante());
	     		tranDel.setFechaPago(caja69.getFechaPago());
	
	     		// Formatear clave (readapta el string para el procesamiento posterior)
				String clave = this.formatearClave(caja69.getClave(), sistema);
				// Obtener valores para AnioComprobante y Periodo
	     		if(clave.length()<6){
	     			this.addRecoverableValueError("El registro de tranDel correspondiente al registro de caja69 de id "+caja69.getId()+" no tiene el formato esperado.");
	     			continue;
	     		}
	     		
	     		// Si corresponde al formato de aaaaMM (anio+periodo)
	     		if(Long.valueOf(clave.substring(0, 4))>1000 && Long.valueOf(clave.substring(0, 4))<7700){
	     			tranDel.setAnioComprobante(new Long(clave.substring(0, 4)));
	     			tranDel.setPeriodo(new Long(clave.substring(4, 6)));
	     		}else if(Long.valueOf(clave)==999999){
	     			tranDel.setAnioComprobante(9999L);
	     			tranDel.setPeriodo(99L);
	     		}else if(Long.valueOf(clave)==888888){
	     			tranDel.setAnioComprobante(8888L);
	     			tranDel.setPeriodo(88L);
	     		}else if(Long.valueOf(clave)==777777){
	     			tranDel.setAnioComprobante(7777L);
	     			tranDel.setPeriodo(77L);
	     		}else{
	     			tranDel.setAnioComprobante(0L);
	     			tranDel.setPeriodo(new Long(clave));	     				     				
	     		}
	     		tranDel.setResto(caja69.getResto());
	     		tranDel.setCodPago(caja69.getCodPago()); 
	     		tranDel.setCaja(caja69.getCaja()); 
	     		tranDel.setCodTr(caja69.getCodTr()); 
	     		tranDel.setImporte(caja69.getImporte()); 
	     		tranDel.setRecargo(caja69.getRecargo());
	     		tranDel.setPaquete(caja69.getPaquete());
	     		tranDel.setMarcaTr(caja69.getMarcaTr());
	     		tranDel.setReciboTr(caja69.getReciboTr());
	    		tranDel.setFechaBalance(caja69.getFechaBalance());
	     		tranDel.setNroLinea(caja69.getNroLinea());
	     			    		
	     		// Creamos el registro de tranDel
	     		aseDel.createTranDel(tranDel); 
	     		if(tranDel.hasError()){
	     			for(DemodaStringMsg error:tranDel.getListError()){
						this.addRecoverableError(error);
					}     			
	     		}
				
			}
		}
	}
	
	/**
	 * <b>Genera Formularios para el paso 3:</b> 
	 * <p>- Reporte de Procesos Asociados: PDF</p>
	 * <i>(paso 3.4)</i>
	 */
	public void generarFormulariosPaso3(String outputDir) throws Exception{

		//-> Reporte de Procesos Asociados (PDF)
		String fileName = this.generarPdfProcesosAsociados(outputDir);
		String nombre = "Procesos Asociados";
		String descripcion = "Permite consultar los totales de transacciones e importes para los Procesos Asociados.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
		
		// -> Detalle de Indeterminados (Planilla de calculo)
		List<String> listFileName = this.exportReportesIndeterminados(outputDir);
		int c = 0;
		nombre = "Detalle de Indeterminados";
		for(String fileNameArch: listFileName){
			descripcion = "Planilla con el Detalle de transacciones Indeterminadas antes de comenzar los Procesos Asociados.. Hoja "+c;
			this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileNameArch);
			c++;
		}
	}	
	
	/**
	 *  Prepara el campo clave de la transaccion para su separacion en anioComprobante y periodo.
	 *  Se realizan conversiones especiales 
	 * 
	 * @param clave
	 * @return
	 */
	public String formatearClave(String clave, Sistema sistema){
		
		// . si es 999999 o 777777 o 888888 no la modifica
		if("999999".equals(clave) || "777777".equals(clave) || "888888".equals(clave)){
			return clave;
		}
		// . si es 9999 completar con 99 a la izq => '999999'
		// . si es 7777 completar con 77 a la izq => '777777'
		// . si es 8888 completar con 88 a la izq => '888888'
		if("9999".equals(clave) || "7777".equals(clave) || "8888".equals(clave)){
			clave = StringUtil.completarCaracteresIzq(clave, 6, clave.charAt(0));
		}else{
			// Cuando estamos en una transaccion de Deuda para un Sistema que no es Servicio Banco
			// y excluyendo los sistemas de CdM (ServicioBanco 82) porque no usan el formato de clave anioPeriodo)
			// consideramos que el formato de la clave es 'mmAA' donde mm es el periodo y AA los ultimos dos dig del anio
			if(sistema.getTipoDeuda() != null 
					&& TipoDeuda.ID_DEUDA_PURA.longValue() == sistema.getTipoDeuda().getId().longValue()
					&& sistema.getEsServicioBanco().intValue() == SiNo.NO.getId().intValue()
					&& !"82".equals(sistema.getServicioBanco().getCodServicioBanco())){
				// . si el campo clave es del tipo 'mmAA' convertirlo a 'aaaaMM'
				// . verificar primero si el mes 'mm' es mayor a 12 y menor que 50. Si es asi considerar los 4 digitos como un año 'yyyy' (caso de sistemas de gravamentes 10 y 11)
				if(new Integer(clave.substring(0,2)) > 12 && new Integer(clave.substring(0,2)) < 50){
					clave = StringUtil.completarCerosIzq(clave,6);
				}else{
					if(new Integer(clave.substring(2,4)) >= 50)
						clave = "19"+clave.substring(2,4)+clave.substring(0, 2);
					else
						clave = "20"+clave.substring(2,4)+clave.substring(0, 2);									
				}				
			}else{
				clave = StringUtil.completarCerosIzq(clave,6);
			}			
		}
		return clave;
	}

	/**
	 *  Verificar si todos los Procesos de Asentamiento Siat y de Asentamientos Delegados se encuentran
	 *  en estado 'Procesado con Exito'
	 *  
	 * <i>(paso 4.1)</i>
	 */
	public void verificarProcesosAsociados() throws Exception{
		for(Asentamiento asentamiento: this.getListAsentamiento()){
			if(asentamiento.getCorrida().getEstadoCorrida().getId().longValue() != EstadoCorrida.ID_PROCESADO_CON_EXITO.longValue()){
				this.addRecoverableValueError("El proceso de Asentamiento de Pagos número "+asentamiento.getId()+" correspondiente al Servicio Banco de código "+asentamiento.getServicioBanco().getCodServicioBanco()+" no se terminó de procesar correctamente.");
			}
		}
		for(AseDel aseDel: this.getListAseDel()){
			if(aseDel.getCorrida().getEstadoCorrida().getId().longValue() != EstadoCorrida.ID_PROCESADO_CON_EXITO.longValue()){
				this.addRecoverableValueError("El proceso de Asentamiento Delegado número "+aseDel.getId()+" correspondiente al Servicio Banco de código "+aseDel.getServicioBanco().getCodServicioBanco()+" no se terminó de procesar correctamente.");
			}
		}
	}

	/**
	 * <b>Genera Formularios para el paso 4:</b> 
	 * <p>- Reporte de Totales por Partida (4007)  PDF</p>
	 * <p>- Reporte de Duplices e Indeterminados  PDF</p>
	 * <p>- Detalle de Indeterminados (Planilla de calculo)</p>
	 * <i>(paso 4.2)</i>
	 */
	public void generarFormulariosPaso4(String outputDir) throws Exception{
		//-> Reporte de Totales por Partida (4007) (PDF)
		String fileName = this.generarPdfTotalesPorPartidaPaso4(outputDir);
		String nombre = "Totales por Partida (*4007)";
		String descripcion = "Permite consultar los totales imputados a cada partida. Resultado de los Asentamientos, sin los ajustes finales aplicados.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);

		//-> Reporte de Duplices e Indeterminados (PDF)
		fileName = this.generarPdfIndetYDuplice(outputDir);
		nombre = "Totales de Indeterminados y Dúplices";
		descripcion = "Permite consultar los totales de transacciones indeterminadas o duplices por Sistema, Caja y Tipo de Indeterminado.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
		
		// -> Detalle de Indeterminados (Planilla de calculo)
		List<String> listFileName = this.exportReportesIndeterminados(outputDir);
		int c = 0;
		nombre = "Detalle de Indeterminados";
		for(String fileNameArch: listFileName){
			descripcion = "Planilla con el Detalle de los Pagos Indeterminados durante los procesos de Asentamiento. Hoja "+c;
			this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileNameArch);
			c++;
		}
	}
	
	/**
	 * Excluye los registros de Folio del Balance
	 * 
	 */
	public void excluirFolios() throws Exception{
		for(Folio folio: this.getListFolio()){
			folio.setBalance(null);
			BalFolioTesoreriaManager.getInstance().updateFolio(folio);
		}
	}
	
	/**
	 * Excluye los registros de Archivos del Balance
	 * 
	 */
	public void excluirArchivos() throws Exception{
		for(Archivo archivo: this.getListArchivo()){
			archivo.setBalance(null);
			BalArchivosBancoManager.getInstance().updateArchivo(archivo);
		}
	}
	
	/**
	 *  Devuelve la lista de Archivos incluidos en el balance cuyos prefijos esten en la lista pasada.
	 * 
	 * @param listPrefix
	 * @return listArchivo
	 */
	public List<Archivo> getListArchivoByListPrefix(List<String> listPrefix){
		List<Archivo> listResult = new ArrayList<Archivo>(); 
		for(Archivo archivo: this.getListArchivo()){
			for(String prefix: listPrefix){
				if(archivo.getPrefix() != null && archivo.getPrefix().toUpperCase().equals(prefix.toUpperCase())){
					listResult.add(archivo);
					break;
				}
			}
		}
		return listResult;
	}

	/**
	 * Genera el Reporte pdf "Procesos Asociados" resultado del paso 3 del proceso de Balance.
	 * 
	 * @param outputDir
	 * @return fileName
	 */
	public String generarPdfProcesosAsociados(String fileDir) throws Exception{
		
		//	Encabezado:
		String fechaBalance = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		
		List<Object[]> listResultAsentamiento = BalDAOFactory.getTransaccionDAO().getTotalesPorAsentamientoForReportByBalance(this);
		List<Object[]> listResultAseDel = BalDAOFactory.getTranDelDAO().getTotalesPorAseDelForReportByBalance(this);
		
		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Balance.COD_FRM_PRO_ASO);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Reporte de Balance - Totales por Procesos Asociados");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Reporte de Balance - Totales por Procesos Asociados");
		printModel.putCabecera("FechaBalance", fechaBalance);
		printModel.putCabecera("Ejercicio", ejercicio);
				
		// Armamos un Contenedor para las Tablas
		ContenedorVO contenedor = new ContenedorVO("ContenedorTabla");
		
		// Se arma la tabla en una estructura de objectos. (TablaVO, FilaVO, CeldaVO)
		TablaVO tabla = new TablaVO("TotalesPorAsentamiento");
		tabla.setTitulo("Asentamientos SIAT");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Nro. Asentamiento","asentamiento"));
		filaCabecera.add(new CeldaVO("Servicio Banco","servicioBanco"));
		filaCabecera.add(new CeldaVO("Cantidad Transacciones","cantTransaccion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		FilaVO fila = new FilaVO();
		Double importeTotal = 0D;
		Long cantTotal = 0L;
		for(Object[] arrayResult: listResultAsentamiento){
			fila.add(new CeldaVO(((Long) arrayResult[0]).toString(),"asentamiento"));
			fila.add(new CeldaVO((String) arrayResult[2] ,"servicioBanco"));
			fila.add(new CeldaVO(((Long) arrayResult[3]).toString(),"cantTransaccion"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate((Double) arrayResult[4], SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			fila = new FilaVO();
			importeTotal += (Double) arrayResult[4];
			cantTotal += (Long) arrayResult[3];
		}
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("","vacio"));
		filaPie.add(new CeldaVO("Total","total"));
		filaPie.add(new CeldaVO(cantTotal.toString(),"totalTransaccion"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);
		
		
		// Se arma la tabla en una estructura de objectos. (TablaVO, FilaVO, CeldaVO)
		tabla = new TablaVO("TotalesPorAsentamiento");
		tabla.setTitulo("Asentamientos Delegados");
		filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Nro. Asentamiento","asentamiento"));
		filaCabecera.add(new CeldaVO("Servicio Banco","servicioBanco"));
		filaCabecera.add(new CeldaVO("Cantidad Transacciones","cantTransaccion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		fila = new FilaVO();
		importeTotal = 0D;
		cantTotal = 0L;
		for(Object[] arrayResult: listResultAseDel){
			fila.add(new CeldaVO(((Long) arrayResult[0]).toString(),"asentamiento"));
			fila.add(new CeldaVO((String) arrayResult[2] ,"servicioBanco"));
			fila.add(new CeldaVO(((Long) arrayResult[3]).toString(),"cantTransaccion"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate((Double) arrayResult[4], SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			fila = new FilaVO();
			importeTotal += (Double) arrayResult[4];
			cantTotal += (Long) arrayResult[3];
		}
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("","vacio"));
		filaPie.add(new CeldaVO("Total","total"));
		filaPie.add(new CeldaVO(cantTotal.toString(),"totalTransaccion"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);
		
		printModel.setData(contenedor);
		printModel.setTopeProfundidad(5);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"ReporteProcesosAsociados.pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		return fileName;
	}

	/**
	 * Genera el Reporte pdf "Totales por Partida" resultado del paso 4 del proceso de Balance.
	 * 
	 * @param fileDir
	 * @return fileName
	 */
	public String generarPdfTotalesPorPartidaPaso4(String fileDir) throws Exception{
		//	Encabezado:
		String fechaBalance = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		
		List<Object[]> listTotales = BalDAOFactory.getDiarioPartidaDAO().getListTotalesForReportByBalance(this);
	
		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Balance.COD_FRM_TOT_PAR);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Reporte de Balance - Totales por Partidas");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Reporte de Balance - Totales por Partidas");
		printModel.putCabecera("NroBalance", this.getId().toString());
		printModel.putCabecera("FechaBalance", fechaBalance);
		printModel.putCabecera("Ejercicio", ejercicio);
		
		Double importeTotal = 0D;

		// Armamos un Contenedor para las Tablas
		ContenedorVO contenedor = new ContenedorVO("ContenedorTabla");

		// Se arman una tabla para el Reporte Total Por Partidas
		TablaVO tabla = new TablaVO("TotalesPartida");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Código","codigo"));
		filaCabecera.add(new CeldaVO("Descripción","descripcion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		for(Object[] arrayResult: listTotales){
			Double suma = (Double) arrayResult[2]+(Double) arrayResult[3];
			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate(suma, SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}

			FilaVO fila = new FilaVO();
			String codPartida = StringUtil.completarCerosIzq(((String) arrayResult[0]).trim(),5); 
			fila.add(new CeldaVO(codPartida,"codigo"));
			fila.add(new CeldaVO(((String) arrayResult[1]),"descripcion"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(suma, SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			importeTotal += (Double) arrayResult[2]+(Double) arrayResult[3];
		}
		tabla.setListFila(this.ordenarListaPartida(tabla.getListFila()));
		
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("","vacio"));
		filaPie.add(new CeldaVO("Total Distribuido:","totalDistribuido"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporteDetalle"));
		tabla.addFilaPie(filaPie);
		
		contenedor.add(tabla);
				
		// Cargamos los datos en el Print Model
		printModel.setData(contenedor);
		printModel.setTopeProfundidad(4);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"ReporteTotalesPartidas-"+corrida.getPasoActual().toString()+".pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		
		return fileName;
	}
	
	/**
	 * Genera el Reporte pdf "Totales de Indeterminados por Sistemas y Caja" resultado del paso 4 del proceso de Balance.
	 * 
	 * @param outputDir
	 * @return fileName
	 */
	public String generarPdfIndetYDuplice(String fileDir) throws Exception{
		
		//	Encabezado:
		String fechaBalance = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		
		List<Object[]> listResultIndetPorSis = BalDAOFactory.getIndetBalDAO().getTotalesPorSistemaForReportByBalance(this);
		List<Object[]> listResultIndetPorCaja = BalDAOFactory.getIndetBalDAO().getTotalesPorCajaForReportByBalance(this);
		List<Object[]> listResultIndetPorTipo = BalDAOFactory.getIndetBalDAO().getTotalesPorTipoForReportByBalance(this);
		
		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Balance.COD_FRM_TOT_INDET);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Reporte de Balance - Totales de Indeterminados y Duplices");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Reporte de Balance - Totales de Indeterminados y Duplices");
		printModel.putCabecera("FechaBalance", fechaBalance);
		printModel.putCabecera("Ejercicio", ejercicio);
				
		// Armamos un Contenedor para las Tablas
		ContenedorVO contenedor = new ContenedorVO("ContenedorTabla");
		
		// Se arma la tabla en una estructura de objectos. (TablaVO, FilaVO, CeldaVO)
		TablaVO tabla = new TablaVO("TotalesPorSistema");
		tabla.setTitulo("Totales por Sistema");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Sistema","sistema"));
		filaCabecera.add(new CeldaVO("Cantidad Indeterminados","cantIndet"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		FilaVO fila = new FilaVO();
		Double importeTotal = 0D;
		Long cantTotal = 0L;
		for(Object[] arrayResult: listResultIndetPorSis){
			String strNroSistema = (String) arrayResult[0]; 
			Long nroSistema = new Long(strNroSistema.trim());
			Sistema sistema = Sistema.getByNroSistema(nroSistema);
			String desSistema = strNroSistema + " - ";
			if(sistema != null){
				desSistema += sistema.getDesSistema();
			}else{
				desSistema += " No existe en SIAT";
			}
			fila.add(new CeldaVO(desSistema,"sistema"));
			fila.add(new CeldaVO(((Long) arrayResult[1]).toString(),"cantIndet"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			fila = new FilaVO();
			importeTotal += (Double) arrayResult[2];
			cantTotal += (Long) arrayResult[1];
		}
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total","total"));
		filaPie.add(new CeldaVO(cantTotal.toString(),"totalIndet"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);
		
		
		// Se arma la tabla en una estructura de objectos. (TablaVO, FilaVO, CeldaVO)
		tabla = new TablaVO("TotalesPorCaja");
		tabla.setTitulo("Totales por Caja");
		filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Caja","caja"));
		filaCabecera.add(new CeldaVO("Cantidad Indeterminados","cantIndet"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		fila = new FilaVO();
		importeTotal = 0D;
		cantTotal = 0L;
		for(Object[] arrayResult: listResultIndetPorCaja){
			Integer nroCaja = (Integer) arrayResult[0]; 
			Banco banco = Banco.getByCodBanco(nroCaja.toString());
			String desBanco = nroCaja + " - ";
			if(banco != null){
				desBanco += banco.getDesBanco();
			}else{
				desBanco += " No existe en SIAT";
			}
			fila.add(new CeldaVO(desBanco,"caja"));
			fila.add(new CeldaVO(((Long) arrayResult[1]).toString(),"cantIndet"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			fila = new FilaVO();
			importeTotal += (Double) arrayResult[2];
			cantTotal += (Long) arrayResult[1];
		}
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total","total"));
		filaPie.add(new CeldaVO(cantTotal.toString(),"totalIndet"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);
	
		// Se arma la tabla en una estructura de objectos. (TablaVO, FilaVO, CeldaVO)
		tabla = new TablaVO("TotalesPorTipo");
		tabla.setTitulo("Totales por Tipo de Indeterminado");
		filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Tipo de Indeterminado","tipo"));
		filaCabecera.add(new CeldaVO("Cantidad Indeterminados","cantIndet"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		fila = new FilaVO();
		importeTotal = 0D;
		cantTotal = 0L;
		for(Object[] arrayResult: listResultIndetPorTipo){
			fila.add(new CeldaVO(((Integer) arrayResult[0]).toString(),"tipo"));
			fila.add(new CeldaVO(((Long) arrayResult[1]).toString(),"cantIndet"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			fila = new FilaVO();
			importeTotal += (Double) arrayResult[2];
			cantTotal += (Long) arrayResult[1];
		}
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total","total"));
		filaPie.add(new CeldaVO(cantTotal.toString(),"totalIndet"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);
		
		printModel.setData(contenedor);
		printModel.setTopeProfundidad(5);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"ReporteIndetYDuplice-"+corrida.getPasoActual().toString()+".pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		return fileName;
	}

	/**
	 * <b>Genera Formularios para el paso 5:</b> 
	 * <p> - Reporte de Totales por Partida (4007) PDF</p>
	 * <p> - Reporte de Totales por Cuenta Bancaria (7011) PDF</p>
	 * <i>(paso 5.1)</i>
	 */
	public void generarFormulariosPaso5(String outputDir) throws Exception{
		//-> Reporte de Totales por Partida (4007) (PDF)
		String fileName = this.generarPdfTotalesPorPartidaPaso5(outputDir);
		String nombre = "Totales por Partida (4007)";
		String descripcion = "Permite consultar los totales imputados a cada partida.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);

		//-> Reporte de Totales por Partida (7011) (PDF)
		fileName = this.generarPdfTotalesPorCuentaBancariaPaso5(outputDir);
		nombre = "Totales por Cuenta Bancaria (7011)";
		descripcion = "Permite consultar los totales imputados por cuenta bancaria para validar.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);

		//-> Reporte de Caja 7 (PDF)
		fileName = this.generarPdfCaja7(outputDir);
		nombre = "Detalle de Caja 7";
		descripcion = "Permite consultar los registros de Caja 7.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
	}

	/**
	 * Genera el Reporte pdf "Totales por Partida" resultado del paso 5 del proceso de Balance.
	 * 
	 * @param fileDir
	 * @return fileName
	 */
	public String generarPdfTotalesPorPartidaPaso5(String fileDir) throws Exception{
		//	Encabezado:
		String fechaBalance = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		
		List<Object[]> listTotalesDiaPar = BalDAOFactory.getDiarioPartidaDAO().getListTotalesForReportByBalance(this);
		List<Object[]> listTotalesCaja7 = BalDAOFactory.getCaja7DAO().getListTotalesForReportByBalance(this);
		
		// Aplicamos ajustes a los totales por partidas.
		Map<String, Object[]> mapResult = new HashMap<String, Object[]>();
		for(Object[] arrayResult: listTotalesDiaPar){
			String partida = (String) arrayResult[0]; 
			mapResult.put(partida, arrayResult);
		}
		for(Object[] arrayResult: listTotalesCaja7){
			String partida = (String) arrayResult[0];
			Object[] totalPorPartida = mapResult.get(partida);
			if(totalPorPartida != null){
				totalPorPartida[2] = ((Double) totalPorPartida[2])+((Double) arrayResult[2]);
				totalPorPartida[3] = ((Double) totalPorPartida[3])+((Double) arrayResult[3]);
				mapResult.put(partida, totalPorPartida);	
			}else{
				mapResult.put(partida, arrayResult);				
			}
		}
		List<Object[]> listTotales = new ArrayList<Object[]>();
		listTotales.addAll(mapResult.values());
		
		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Balance.COD_FRM_TOT_PAR);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Reporte de Balance - Totales por Partidas (4007)");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Reporte de Balance - Totales por Partidas (4007)");
		printModel.putCabecera("NroBalance", this.getId().toString());
		printModel.putCabecera("FechaBalance", fechaBalance);
		printModel.putCabecera("Ejercicio", ejercicio);
		
		Double importeTotal = 0D;

		// Armamos un Contenedor para las Tablas
		ContenedorVO contenedor = new ContenedorVO("ContenedorTabla");

		// Se arman una tabla para el Reporte Total Por Partidas
		TablaVO tabla = new TablaVO("TotalesPartida");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Código","codigo"));
		filaCabecera.add(new CeldaVO("Descripción","descripcion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		for(Object[] arrayResult: listTotales){
			Double suma = (Double) arrayResult[2]+(Double) arrayResult[3];
			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate(suma, SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}

			FilaVO fila = new FilaVO();
			String codPartida = StringUtil.completarCerosIzq(((String) arrayResult[0]).trim(),5);			
			fila.add(new CeldaVO(codPartida,"codigo"));
			fila.add(new CeldaVO(((String) arrayResult[1]),"descripcion"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(suma, SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			importeTotal += (Double) arrayResult[2]+(Double) arrayResult[3];
		}
		tabla.setListFila(this.ordenarListaPartida(tabla.getListFila()));
		
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("","vacio"));
		filaPie.add(new CeldaVO("Total Distribuido:","totalDistribuido"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporteDetalle"));
		tabla.addFilaPie(filaPie);
		
		contenedor.add(tabla);
				
		// Cargamos los datos en el Print Model
		printModel.setData(contenedor);
		printModel.setTopeProfundidad(4);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"ReporteTotalesPartidas4007-"+corrida.getPasoActual().toString()+".pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		
		return fileName;
	}

	/**
	 * Genera el Reporte pdf "Totales por Partida" resultado del paso 5 del proceso de Balance.
	 * 
	 * @param fileDir
	 * @return fileName
	 */
	public String generarPdfTotalesPorCuentaBancariaPaso5(String fileDir) throws Exception{
		//	Encabezado:
		String fechaBalance = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		
		List<Object[]> listTotalesDiaPar = BalDAOFactory.getDiarioPartidaDAO().getListTotalesForReportByBalance(this);
		List<Object[]> listTotalesCaja7 = BalDAOFactory.getCaja7DAO().getListTotalesForReportByBalance(this);
		
		// Aplicamos ajustes a los totales por partidas.
		Map<String, Object[]> mapResult = new HashMap<String, Object[]>();
		for(Object[] arrayResult: listTotalesDiaPar){
			String partida = (String) arrayResult[0]; 
			mapResult.put(partida, arrayResult);
		}
		for(Object[] arrayResult: listTotalesCaja7){
			String partida = (String) arrayResult[0];
			Object[] totalPorPartida = mapResult.get(partida);
			if(totalPorPartida != null){
				totalPorPartida[2] = ((Double) totalPorPartida[2])+((Double) arrayResult[2]);
				totalPorPartida[3] = ((Double) totalPorPartida[3])+((Double) arrayResult[3]);
				mapResult.put(partida, totalPorPartida);	
			}else{
				mapResult.put(partida, arrayResult);				
			}
		}
		List<Object[]> listTotales = new ArrayList<Object[]>();
		listTotales.addAll(mapResult.values());

		// Buscamos las Cuentas Bancarias vigentes para las Partidas y acumulamos por Cuenta
		Map<String, Object[]> mapCuenta = new HashMap<String, Object[]>();
		for(Object[] arrayResult: listTotales){
			String codPartida = (String) arrayResult[0];
			CuentaBanco cuentaBanco = BalanceCache.getInstance().getCuentaBancoByCodPartida(codPartida);
			String nroCuentaBanco = "-1";
			String desBanco = "No existe en SIAT";
			if(cuentaBanco != null){
				nroCuentaBanco = cuentaBanco.getNroCuenta();
				if(cuentaBanco.getBanco() != null){
					desBanco = cuentaBanco.getBanco().getDesBanco();
				}
			}
			Object[] arrayCuentaBanco = mapCuenta.get(nroCuentaBanco);
			if(arrayCuentaBanco == null){
				arrayCuentaBanco = new Object[3];
				arrayCuentaBanco[0] =  nroCuentaBanco;
				arrayCuentaBanco[1] =  desBanco;
				arrayCuentaBanco[2] = 0D;
			}
			arrayCuentaBanco[2] =  (Double) arrayCuentaBanco[2]+(Double) arrayResult[2]+(Double) arrayResult[3];
			mapCuenta.put(nroCuentaBanco, arrayCuentaBanco);
		}
	
		List<Object[]> listTotalesCuentaBanco = new ArrayList<Object[]>();
		listTotalesCuentaBanco.addAll(mapCuenta.values());

		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Balance.COD_FRM_TOT_CUE);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Reporte de Balance - Totales por Cuenta Bancaria (7011)");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Reporte de Balance - Totales por Cuenta Bancaria (7011)");
		printModel.putCabecera("NroBalance", this.getId().toString());
		printModel.putCabecera("FechaBalance", fechaBalance);
		printModel.putCabecera("Ejercicio", ejercicio);
		
		Double importeTotal = 0D;

		// Armamos un Contenedor para las Tablas
		ContenedorVO contenedor = new ContenedorVO("ContenedorTabla");

		// Se arman una tabla para el Reporte Total Por Cuentas 
		TablaVO tabla = new TablaVO("TotalesCuenta");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Nro. Cuenta","nroCuenta"));
		filaCabecera.add(new CeldaVO("Banco","banco"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		for(Object[] arrayResult: listTotalesCuentaBanco){
			Double importe = (Double) arrayResult[2];
			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate(importe, SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}

			FilaVO fila = new FilaVO();
			fila.add(new CeldaVO((String) arrayResult[0],"nroCuenta"));
			fila.add(new CeldaVO(((String) arrayResult[1]),"banco"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importe, SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			importeTotal += (Double) arrayResult[2];
		}
		
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("","vacio"));
		filaPie.add(new CeldaVO("Total:","total"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);
		
		contenedor.add(tabla);
				
		// Cargamos los datos en el Print Model
		printModel.setData(contenedor);
		printModel.setTopeProfundidad(4);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"ReporteTotalesCuentaBanco7011-"+corrida.getPasoActual().toString()+".pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		
		return fileName;
	}

	/**
	 * Aplicar los registros de DiarioPartida (imputaciones a partidas de los asentamientos) al Maestro de Rentas (ImpPar)  
	 * 
	 * <i>(paso 6.1)</i>
	 */
	public void aplicarDiarioPartida() throws Exception{
		if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){
			List<Object[]> listTotPar = BalDAOFactory.getDiarioPartidaDAO().getImportesGroupByPartidaForBalance(this);
			
			for(Object[] arrayResult: listTotPar){
				Long idPartida = (Long) arrayResult[0];
				Partida partida = BalanceCache.getInstance().getPartidaById(idPartida);
				Double importeEjeAct = NumberUtil.truncate((Double) arrayResult[1], SiatParam.DEC_IMPORTE_DB);
				Double importeEjeVen = NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB);
				ImpPar impPar = new ImpPar();
				
				impPar.setBalance(this);
				impPar.setFecha(this.getFechaBalance());
				impPar.setPartida(partida);
				impPar.setImporteEjeAct(importeEjeAct);
				impPar.setImporteEjeVen(importeEjeVen);
				
				BalDAOFactory.getImpParDAO().update(impPar);
			}			
		}
	}

	/**
	 * Aplicar los ajustes a partida (Caja7) al Maestro de Rentas (ImpPar)  
	 * 
	 * <i>(paso 6.2)</i>
	 */
	public void aplicarCaja7() throws Exception{
		if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){
			List<Object[]> listTotPar = BalDAOFactory.getCaja7DAO().getImportesGroupByPartidaForBalance(this);
			
			for(Object[] arrayResult: listTotPar){
				Long idPartida = (Long) arrayResult[0];
				Partida partida = BalanceCache.getInstance().getPartidaById(idPartida);
				Double importeEjeAct = NumberUtil.truncate((Double) arrayResult[1], SiatParam.DEC_IMPORTE_DB);
				Double importeEjeVen = NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB);
				ImpPar impPar = new ImpPar();
				
				impPar.setBalance(this);
				impPar.setFecha(this.getFechaBalance());
				impPar.setPartida(partida);
				impPar.setImporteEjeAct(importeEjeAct);
				impPar.setImporteEjeVen(importeEjeVen);
				
				BalDAOFactory.getImpParDAO().update(impPar);
			}			
		}
	}

	/**
	 * <b>Genera Formularios para el paso 6:</b> 
	 * <p> - Reporte de Totales por Partida (4007) PDF</p>
	 * <p> - Reporte de Totales por Cuenta Bancaria (7011) PDF</p>
	 * <p> - Reportes de Clasificadores PDF (1 por cada clasificador, tomando como clasificador maestro el de rubro)</p>
	 * <i>(paso 6.3)</i>
	 */
	public void generarFormulariosPaso6(String outputDir) throws Exception{
		//-> Reporte de Totales por Partida (4007) (PDF)
		String fileName = this.generarPdfTotalesPorPartidaPaso6(outputDir);
		String nombre = "Totales por Partida (4007) del Balance";
		String descripcion = "Permite consultar los totales imputados a cada partida para el Balance.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);

		//-> Reporte de Totales por Partida (7011) (PDF)
		fileName = this.generarPdfTotalesPorCuentaBancariaPaso6(outputDir);
		nombre = "Totales por Cuenta Bancaria (7011)";
		descripcion = "Permite consultar los totales imputados por cuenta bancaria para validar.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);

		//-> Reportes de Clasificadores (PDF)
		List<String[]> listFileInfo = this.generarPdfClasificadores(outputDir);
		for(String[] fileInfo: listFileInfo){
			String fileNameArch = fileInfo[0];
			nombre = fileInfo[1];
			descripcion = fileInfo[2];
			this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileNameArch);
		}
	}

	/**
	 * Genera el Reporte pdf "Totales por Partida" resultado del paso 6 del proceso de Balance.
	 * Se calcula sobre los registros de ImpPar (Maestro de Rentas) para este Balance.
	 * 
	 * @param fileDir
	 * @return fileName
	 */
	public String generarPdfTotalesPorPartidaPaso6(String fileDir) throws Exception{
		//	Encabezado:
		String fechaBalance = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		
		List<Object[]> listTotales = BalDAOFactory.getImpParDAO().getListTotalesForReportByBalance(this);
	
		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Balance.COD_FRM_TOT_PAR);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Reporte de Balance - Totales por Partidas");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Reporte de Balance - Totales por Partidas");
		printModel.putCabecera("NroBalance", this.getId().toString());
		printModel.putCabecera("FechaBalance", fechaBalance);
		printModel.putCabecera("Ejercicio", ejercicio);
		
		Double importeTotal = 0D;

		// Armamos un Contenedor para las Tablas
		ContenedorVO contenedor = new ContenedorVO("ContenedorTabla");

		// Se arman una tabla para el Reporte Total Por Partidas
		TablaVO tabla = new TablaVO("TotalesPartida");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Código","codigo"));
		filaCabecera.add(new CeldaVO("Descripción","descripcion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		for(Object[] arrayResult: listTotales){
			Double suma = (Double) arrayResult[2]+(Double) arrayResult[3];
			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate(suma, SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}

			FilaVO fila = new FilaVO();
			String codPartida = StringUtil.completarCerosIzq(((String) arrayResult[0]).trim(),5);
			fila.add(new CeldaVO(codPartida,"codigo"));
			fila.add(new CeldaVO(((String) arrayResult[1]),"descripcion"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(suma, SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			importeTotal += (Double) arrayResult[2]+(Double) arrayResult[3];
		}
		tabla.setListFila(this.ordenarListaPartida(tabla.getListFila()));
		
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("","vacio"));
		filaPie.add(new CeldaVO("Total Distribuido:","totalDistribuido"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporteDetalle"));
		tabla.addFilaPie(filaPie);
		
		contenedor.add(tabla);
				
		// Cargamos los datos en el Print Model
		printModel.setData(contenedor);
		printModel.setTopeProfundidad(4);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"ReporteTotalesPartidas-"+corrida.getPasoActual().toString()+".pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		
		return fileName;
	}
	
	
	
	/**
	 * Realiza la conversion del nro de sistema para algunos casos de sistemas NO SIAT.
	 * 
	 * @param nroSistemaACorregir
	 * @return
	 */
	public Long corregirSistema(Long nroSistemaACorregir, Long nroComprobante){
		Long nroSistema = nroSistemaACorregir;
		// Unificacion de sistemas de Tgi NO SIAT (Actual y Vencido)
		if(nroSistema == 1)
			nroSistema = 2L;
		// Grandes Contribuyentes TGI
		if(nroSistema == 49)
			nroSistema = 2L;
		// Grandes Contribuyentes DREI
		if(nroSistema == 48)
			nroSistema = 3L;
		return nroSistema;
	}
	
	/**
	 * Registrar Indeterminado para TranBal.
	 * 
	 * 
	 * @param mensaje
	 * @param tranBal
	 * @param codIndet
	 */
	public void registrarIndeterminado(String message, TranBal tranBal, String codTipoIndet) throws Exception{

		AdpRun.currentRun().logDebug("Indeterminado!. Transaccion de Linea Nro: "+tranBal.getNroLinea()+" . "+message);
	
		TipoIndet tipoIndet = TipoIndet.getByCodigo(codTipoIndet);
		if(tipoIndet == null){
			this.addRecoverableValueError("Error!. Transaccion de Linea Nro: "+tranBal.getNroLinea()+". No existe el Tipo Indeterminado con Codigo "+codTipoIndet);
			return;
		}
		
		String codPartida = SiatParam.getString(Balance.KEY_SIAT_PARAM_PARTIDA_INDET);
		Partida partida = null;
		if(!StringUtil.isNullOrEmpty(codPartida))
			partida = Partida.getByCod(codPartida);
		if(partida == null){
			this.addRecoverableValueError("Error!. Transaccion de Linea Nro: "+tranBal.getNroLinea()+". No se encontró la partida para Indeterminados sin procesar. CodPartida: "+codPartida);
			return;			
		}

		// Distribuir indeterminado
		if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){
			DiarioPartida diarioPartida = new DiarioPartida();
			diarioPartida.setFecha(this.getFechaBalance());
			diarioPartida.setEjercicio(this.getEjercicio());
			diarioPartida.setPartida(partida); 
			diarioPartida.setImporteEjeAct(NumberUtil.truncate(tranBal.getImporte(),SiatParam.DEC_IMPORTE_DB));
			diarioPartida.setImporteEjeVen(0D);
			TipOriMov tipOriMov = TipOriMov.getById(TipOriMov.ID_OTRO); 
			diarioPartida.setTipOriMov(tipOriMov);
			diarioPartida.setIdOrigen(null); 
			EstDiaPar estDiaPar = EstDiaPar.getById(EstDiaPar.ID_CREADA);
			diarioPartida.setEstDiaPar(estDiaPar);
			diarioPartida.setBalance(this);				
			diarioPartida.setPasoBalance(AdpRun.currentRun().getPasoActual().intValue());
			BalDAOFactory.getDiarioPartidaDAO().update(diarioPartida);			
		}
		
		// -> Grabar en IndetBal.
		IndetBal indetBal = new IndetBal();
		indetBal.setBalance(this);
		indetBal.setSistema(tranBal.getSistema().toString());
		indetBal.setNroComprobante(tranBal.getNroComprobante().toString());
		indetBal.setClave(tranBal.getClave());
		indetBal.setResto(tranBal.getResto().toString());
		indetBal.setCodPago(tranBal.getCodPago().intValue());
		indetBal.setCaja(tranBal.getCaja().intValue());
		indetBal.setFechaPago(tranBal.getFechaPago());
		indetBal.setImporteCobrado(tranBal.getImporte());
		indetBal.setRecargo(0D);
		indetBal.setImporteBasico(0D);
		indetBal.setImporteCalculado(0D);
		indetBal.setIndice(0D);
		if(tranBal.getFiller() != null)
			indetBal.setFiller(tranBal.getFiller());
		else
			indetBal.setFiller("0");
		indetBal.setPaquete(tranBal.getPaquete().intValue());
		indetBal.setReciboTr(tranBal.getReciboTr());
		indetBal.setCodIndet(NumberUtil.getInt(tipoIndet.getCodIndetMR()));
		indetBal.setPartida("9".concat(partida.getCodPartida()));
		indetBal.setFechaBalance(tranBal.getFechaBalance());
		indetBal.setPasoBalance(AdpRun.currentRun().getPasoActual().intValue());
		
		BalDAOFactory.getIndetBalDAO().update(indetBal);
		
		// Crear Ajuste de Caja7 para descarga de Cuenta Puente (siempre que se encuentre el sistema)
		Sistema sistema = BalanceCache.getInstance().getSistemaByNro(tranBal.getSistema());
		ServicioBanco servicioBanco = null;
		Partida parCuePue = null;
		String codServicioBanco = "";
		if(sistema != null){
			servicioBanco = sistema.getServicioBanco();
			parCuePue = servicioBanco.getParCuePue();
			codServicioBanco = servicioBanco.getCodServicioBanco();
		}else{
			String codParCtaPte = SiatParam.getString(Balance.KEY_SIAT_PARAM_PARTIDA_CTA_PTE);
			parCuePue = Partida.getByCod(codParCtaPte);
			codServicioBanco = "desconocido";
		}
		Double importe = NumberUtil.truncate(tranBal.getImporte(),SiatParam.DEC_IMPORTE_DB);
		Caja7 caja7 = new Caja7();
			
		caja7.setBalance(this);
		caja7.setFecha(this.getFechaBalance()); 
		caja7.setPartida(parCuePue);
		caja7.setImporteEjeAct(importe*(-1));
		caja7.setImporteEjeVen(0D);
		caja7.setDescripcion("Ajuste de descarga a Cuenta Puente por Indeterminado en Balance. Servicio Banco: "+codServicioBanco);
			
		BalBalanceManager.getInstance().createCaja7(caja7);	
		
		
		
	}

	/**
	 * Registrar Indeterminado para Caja 69.
	 * 
	 * 
	 * @param mensaje
	 * @param caja69
	 * @param codIndet
	 */
	public void registrarIndeterminado(String message, Caja69 caja69, String codTipoIndet) throws Exception{

		AdpRun.currentRun().logDebug("Indeterminado!. Transaccion de Linea Nro: "+caja69.getNroLinea()+" . "+message);
	
		TipoIndet tipoIndet = TipoIndet.getByCodigo(codTipoIndet);
		if(tipoIndet == null){
			this.addRecoverableValueError("Error!. Transaccion de Linea Nro: "+caja69.getNroLinea()+". No existe el Tipo Indeterminado con Codigo "+codTipoIndet);
			return;
		}
		
		String codPartida = SiatParam.getString(Balance.KEY_SIAT_PARAM_PARTIDA_INDET);
		Partida partida = null;
		if(!StringUtil.isNullOrEmpty(codPartida))
			partida = Partida.getByCod(codPartida);
		if(partida == null){
			this.addRecoverableValueError("Error!. Transaccion de Linea Nro: "+caja69.getNroLinea()+". No se encontró la partida para Indeterminados sin procesar. CodPartida: "+codPartida);
			return;			
		}

		// Distribuir indeterminado
		if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){
			DiarioPartida diarioPartida = new DiarioPartida();
			diarioPartida.setFecha(this.getFechaBalance());
			diarioPartida.setEjercicio(this.getEjercicio());
			diarioPartida.setPartida(partida); 
			diarioPartida.setImporteEjeAct(NumberUtil.truncate(caja69.getImporte(),SiatParam.DEC_IMPORTE_DB));
			diarioPartida.setImporteEjeVen(0D);
			TipOriMov tipOriMov = TipOriMov.getById(TipOriMov.ID_OTRO); 
			diarioPartida.setTipOriMov(tipOriMov);
			diarioPartida.setIdOrigen(null); 
			EstDiaPar estDiaPar = EstDiaPar.getById(EstDiaPar.ID_CREADA);
			diarioPartida.setEstDiaPar(estDiaPar);
			diarioPartida.setBalance(this);				
			diarioPartida.setPasoBalance(AdpRun.currentRun().getPasoActual().intValue());
			BalDAOFactory.getDiarioPartidaDAO().update(diarioPartida);			
		}
		
		// -> Grabar en IndetBal.
		IndetBal indetBal = new IndetBal();
		indetBal.setBalance(this);
		indetBal.setSistema(caja69.getSistema().toString());
		indetBal.setNroComprobante(caja69.getNroComprobante().toString());
		indetBal.setClave(caja69.getClave());
		indetBal.setResto(caja69.getResto().toString());
		indetBal.setCodPago(caja69.getCodPago().intValue());
		indetBal.setCaja(caja69.getCaja().intValue());
		indetBal.setFechaPago(caja69.getFechaPago());
		indetBal.setImporteCobrado(caja69.getImporte());
		indetBal.setRecargo(0D);
		indetBal.setImporteBasico(0D);
		indetBal.setImporteCalculado(0D);
		indetBal.setIndice(0D);
		if(caja69.getFiller() != null)
			indetBal.setFiller(caja69.getFiller());
		else
			indetBal.setFiller("0");
		indetBal.setPaquete(caja69.getPaquete().intValue());
		indetBal.setReciboTr(caja69.getReciboTr());
		indetBal.setCodIndet(NumberUtil.getInt(tipoIndet.getCodIndetMR()));
		indetBal.setPartida("9".concat(partida.getCodPartida()));
		indetBal.setFechaBalance(caja69.getFechaBalance());
		indetBal.setPasoBalance(AdpRun.currentRun().getPasoActual().intValue());
		
		BalDAOFactory.getIndetBalDAO().update(indetBal);
		
		// Crear Ajuste de Caja7 para descarga de Cuenta Puente (siempre que se encuentre el sistema)
		Sistema sistema = BalanceCache.getInstance().getSistemaByNro(caja69.getSistema());
		ServicioBanco servicioBanco = null;
		Partida parCuePue = null;
		String codServicioBanco = "";
		if(sistema != null){
			servicioBanco = sistema.getServicioBanco();
			parCuePue = servicioBanco.getParCuePue();
			codServicioBanco = servicioBanco.getCodServicioBanco();
		}else{
			String codParCtaPte = SiatParam.getString(Balance.KEY_SIAT_PARAM_PARTIDA_CTA_PTE);
			parCuePue = Partida.getByCod(codParCtaPte);
			codServicioBanco = "desconocido";
		}

		Double importe = NumberUtil.truncate(caja69.getImporte(),SiatParam.DEC_IMPORTE_DB);
			
		Caja7 caja7 = new Caja7();
		
		caja7.setBalance(this);
		caja7.setFecha(this.getFechaBalance()); 
		caja7.setPartida(parCuePue);
		caja7.setImporteEjeAct(importe*(-1));
		caja7.setImporteEjeVen(0D);
		caja7.setDescripcion("Ajuste de descarga a Cuenta Puente por Indeterminado en Balance. Servicio Banco: "+codServicioBanco);
		
		BalBalanceManager.getInstance().createCaja7(caja7);	
	}

	/**
	 * Grabar Dúplices e Indeterminados en base de Indeterminados  
	 * 
	 * <i>(paso 7.1)</i>
	 */
	public void aplicarIndeterminados() throws Exception{
		for(IndetBal indetBal: this.getListIndetBal()){
			IndetVO indet = new IndetVO();
		
			indet.setSistema(indetBal.getSistema());
    		indet.setNroComprobante(indetBal.getNroComprobante());
    		indet.setClave(indetBal.getClave());
    		indet.setResto(indetBal.getResto());
    		indet.setImporteCobrado(indetBal.getImporteCobrado());
    		indet.setImporteBasico(indetBal.getImporteBasico());
    		indet.setImporteCalculado(indetBal.getImporteCalculado());
    		indet.setIndice(indetBal.getIndice());
    		indet.setRecargo(indetBal.getRecargo());
    		indet.setPartida(indetBal.getPartida());
    		indet.setCodIndet(indetBal.getCodIndet());
    		indet.setFechaPago(indetBal.getFechaPago());
    		indet.setCaja(indetBal.getCaja());
    		indet.setPaquete(indetBal.getPaquete());
    		indet.setCodPago(indetBal.getCodPago());
    		indet.setFechaBalance(this.getFechaBalance());
    		indet.setReciboTr(indetBal.getReciboTr());
    		indet.setFiller(indetBal.getFiller());
    		
    		//Si el indeterminado es AFIP, cambio el tipo de ingreso
    		if (SinIndet.FILLER_AFIP.equals(indetBal.getFiller())) {
    			indet.setTipoIngreso(SinIndet.ID_TPO_INGRESO_AFIP);
			}else {
				indet.setTipoIngreso(1);	
			}
    		
    		if(indet.getCodIndet().intValue() == TipoIndet.COD_DUPLICE_MR.intValue()){
    				IndeterminadoFacade.getInstance().createDuplice(indet);
    		}else{
    				IndeterminadoFacade.getInstance().createIndet(indet);
			}
    	}
	}
	
	/**
	 * Generar Saldos a Favor 
	 * 
	 * <i>(paso 7.2)</i>
	 */
	public void generarSaldosAFavor() throws Exception{
		for(SaldoBal saldoBal: this.getListSaldoBal()){
			SaldoAFavor saldoAFavor = new SaldoAFavor();
			
			saldoAFavor.setArea(saldoBal.getArea());
			saldoAFavor.setTipoOrigen(saldoBal.getTipoOrigen());
			saldoAFavor.setCuenta(saldoBal.getCuenta());
			saldoAFavor.setDescripcion(saldoBal.getDescripcion());
			saldoAFavor.setFechaGeneracion(saldoBal.getFechaGeneracion());
			saldoAFavor.setImporte(saldoBal.getImporte());
			saldoAFavor.setNroComprobante(saldoBal.getNroComprobante());
			saldoAFavor.setDesComprobante(saldoBal.getDesComprobante());
			saldoAFavor.setCuentaBanco(saldoBal.getCuentaBanco());
			saldoAFavor.setIdCaso(saldoBal.getIdCaso());
			saldoAFavor.setEstSalAFav(saldoBal.getEstSalAFav());
			saldoAFavor.setIdDeuda(saldoBal.getIdDeuda());
			saldoAFavor.setIdConvenioCuota(saldoBal.getIdConvenioCuota());
			saldoAFavor.setFechaPago(saldoBal.getFechaPago());
			saldoAFavor.setPartida(saldoBal.getPartida());
			saldoAFavor.setAsentamiento(saldoAFavor.getAsentamiento());
			saldoAFavor.setCompensacion(null);
			saldoAFavor.setEstado(Estado.ACTIVO.getId());
			
			BalDAOFactory.getSaldoAFavorDAO().update(saldoAFavor);
		}
	}
	
	
	/**
	 * Elimina todas las tablas auxiliares utilizadas en el Balance.
	 * (Solo se llama al finalizar correctamente el ultimo paso)
	 *
	 */
	public void eliminarTablasTemporales(){
		// Eliminar las tablas temporales asociadas al Balance:
		// bal_indetBal, bal_saldoBal, bal_diarioPartida, bal_tranBal, bal_caja69, bal_caja7, bal_reingreso
		BalDAOFactory.getIndetBalDAO().deleteAllByBalance(this);
		BalDAOFactory.getSaldoBalDAO().deleteAllByBalance(this);
		BalDAOFactory.getDiarioPartidaDAO().deleteAllByBalance(this);
		BalDAOFactory.getTranBalDAO().deleteAllByBalance(this);
		BalDAOFactory.getCaja69DAO().deleteAllByBalance(this);
		BalDAOFactory.getCaja7DAO().deleteAllByBalance(this);
		BalDAOFactory.getReingresoDAO().deleteAllByBalance(this);
	}
	
	/**
	 * Genera el Reporte pdf "Totales por Partida" resultado del paso 6 del proceso de Balance.
	 * 
	 * @param fileDir
	 * @return fileName
	 */
	public String generarPdfTotalesPorCuentaBancariaPaso6(String fileDir) throws Exception{
		//	Encabezado:
		String fechaBalance = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		
		List<Object[]> listTotales = BalDAOFactory.getImpParDAO().getListTotalesForReportByBalance(this);
		
		// Buscamos las Cuentas Bancarias vigentes para las Partidas y acumulamos por Cuenta
		Map<String, Object[]> mapCuenta = new HashMap<String, Object[]>();
		for(Object[] arrayResult: listTotales){
			String codPartida = (String) arrayResult[0];
			CuentaBanco cuentaBanco = BalanceCache.getInstance().getCuentaBancoByCodPartida(codPartida);
			String nroCuentaBanco = "-1";
			String desBanco = "No existe en SIAT";
			if(cuentaBanco != null){
				nroCuentaBanco = cuentaBanco.getNroCuenta();
				if(cuentaBanco.getBanco() != null){
					desBanco = cuentaBanco.getBanco().getDesBanco();
				}
			}
			Object[] arrayCuentaBanco = mapCuenta.get(nroCuentaBanco);
			if(arrayCuentaBanco == null){
				arrayCuentaBanco = new Object[3];
				arrayCuentaBanco[0] =  nroCuentaBanco;
				arrayCuentaBanco[1] =  desBanco;
				arrayCuentaBanco[2] = 0D;
			}
			arrayCuentaBanco[2] =  (Double) arrayCuentaBanco[2]+(Double) arrayResult[2]+(Double) arrayResult[3];
			mapCuenta.put(nroCuentaBanco, arrayCuentaBanco);
		}
	
		List<Object[]> listTotalesCuentaBanco = new ArrayList<Object[]>();
		listTotalesCuentaBanco.addAll(mapCuenta.values());

		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Balance.COD_FRM_TOT_CUE);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Reporte de Balance - Totales por Cuenta Bancaria (7011)");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Reporte de Balance - Totales por Cuenta Bancaria (7011)");
		printModel.putCabecera("NroBalance", this.getId().toString());
		printModel.putCabecera("FechaBalance", fechaBalance);
		printModel.putCabecera("Ejercicio", ejercicio);
		
		Double importeTotal = 0D;

		// Armamos un Contenedor para las Tablas
		ContenedorVO contenedor = new ContenedorVO("ContenedorTabla");

		// Se arman una tabla para el Reporte Total Por Cuentas 
		TablaVO tabla = new TablaVO("TotalesCuenta");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Nro. Cuenta","nroCuenta"));
		filaCabecera.add(new CeldaVO("Banco","banco"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		for(Object[] arrayResult: listTotalesCuentaBanco){
			Double importe = (Double) arrayResult[2];
			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate(importe, SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}

			FilaVO fila = new FilaVO();
			fila.add(new CeldaVO((String) arrayResult[0],"nroCuenta"));
			fila.add(new CeldaVO(((String) arrayResult[1]),"banco"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importe, SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			importeTotal += (Double) arrayResult[2];
		}
		
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("","vacio"));
		filaPie.add(new CeldaVO("Total:","total"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);
		
		contenedor.add(tabla);
				
		// Cargamos los datos en el Print Model
		printModel.setData(contenedor);
		printModel.setTopeProfundidad(4);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"ReporteTotalesCuentaBanco7011-"+corrida.getPasoActual().toString()+".pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		
		return fileName;
	}

	/**
	 * Genera los Reportes pdf para los Clasificadores resultados del paso 6 del proceso de Balance.
	 * 
	 * @param fileDir
	 * @return fileName
	 */
	public List<String[]> generarPdfClasificadores(String fileDir) throws Exception{
		String fechaBalance = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();

		List<String[]> listFileInfo = new ArrayList<String[]>();
		
		// Generamos el reporte pare el clasificador Rubro (clasificador principal)
		Clasificador clasificador = Clasificador.getById(Clasificador.ID_CLA_RUBRO);

		// Aqui obtiene lista de BOs
		List<Nodo> listNodo = Nodo.getListActivosByClasificador(clasificador);  

		List<Nodo> listNodoArbol = Nodo.obtenerListaArbolConTotales(listNodo, null, null, null, this);
		
		List<NodoVO> listNodoVOOrdenados = (ArrayList<NodoVO>) ListUtilBean.toVO(listNodoArbol, 1,false);

		Double totalGeneral = 0D;
		Double subTotal = 0D;
		for(NodoVO nodoVO: listNodoVOOrdenados){
			nodoVO.setTotal(NumberUtil.truncate(nodoVO.getTotal(), 2));
			nodoVO.setTotalEjeAct(NumberUtil.truncate(nodoVO.getTotalEjeAct(), 2));
			nodoVO.setTotalEjeVen(NumberUtil.truncate(nodoVO.getTotalEjeVen(), 2));
			if(nodoVO.getEsNodoRaiz()){
				totalGeneral += nodoVO.getTotal();
				// Calculamos Subtotal Rubros 11 al 42 (pedido por el area de Balance)
				Long codNodoRaiz = null;
				try{ codNodoRaiz = Long.valueOf(nodoVO.getCodigo());}catch(Exception e){}
				if(codNodoRaiz != null && codNodoRaiz >=11 && codNodoRaiz <= 42){
					subTotal += nodoVO.getTotal();
				}
			}
		}
		
		InformeClasificador informe = new InformeClasificador();
		
		informe.setReporteProcesoBalance("true");
		informe.setFechaReporte(DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
		informe.setDesClasificador(clasificador.getDescripcion().toUpperCase());
		informe.setDesEjercicio(ejercicio);
		informe.setTotalGeneral(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalGeneral, 2)));
		informe.setSubTotal(StringUtil.formatDoubleWithComa(NumberUtil.truncate(subTotal, 2)));
		informe.setMostrarSubTotal("true");
		
		informe.setListNodo(listNodoVOOrdenados);
	
		// Generamos el printModel
		PrintModel print =null;
		
		print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_CLASIFICADOR);
		
		print.setNoAplicarTrim(true);
		print.putCabecera("TituloReporte", "Clasificador de "+clasificador.getDescripcion());
		print.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
		print.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
		print.putCabecera("FechaBalance", fechaBalance);
		print.putCabecera("Ejercicio", ejercicio);
		
		print.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		print.setData(informe);
		print.setTopeProfundidad(2);
					
		// Archivo pdf a generar
		String fileName = this.getId().toString()+"ReporteClasificador"+clasificador.getDescripcion()+".pdf"; 
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(print.getByteArray());
		pdfFile.close();
		
		String nombre = "Reporte de Clasificador de "+clasificador.getDescripcion();
		String descripcion = "Consulta de Totales para Clasificador. Sólo se incluyen los importes que corresponden al proceso de balance.";
		
		String[] fileInfo = new String[3];
		fileInfo[0] = fileName;
		fileInfo[1] = nombre;
		fileInfo[2] = descripcion;
		listFileInfo.add(fileInfo);
		
		// Generamos el reporte pare los clasificadores restantes (usando el principal)
		List<Clasificador> listClasificadorRestante = Clasificador.getListActivosExcluyendoId(clasificador.getId());
		List<Nodo> listNodoRubro = listNodo;
		for(Clasificador cla: listClasificadorRestante){
			// Aqui obtiene lista de BOs
			listNodo = Nodo.getListActivosByClasificador(cla);  

			listNodoArbol = Nodo.obtenerListaArbolConTotales(listNodo, null, null, listNodoRubro, this);
			
			listNodoVOOrdenados = (ArrayList<NodoVO>) ListUtilBean.toVO(listNodoArbol, 1,false);

			totalGeneral = 0D;
			Double aRestar = 0D;
			for(NodoVO nodoVO: listNodoVOOrdenados){
				nodoVO.setTotal(NumberUtil.truncate(nodoVO.getTotal(), 1));
				nodoVO.setTotalEjeAct(NumberUtil.truncate(nodoVO.getTotalEjeAct(), 2));
				nodoVO.setTotalEjeVen(NumberUtil.truncate(nodoVO.getTotalEjeVen(), 2));
				if(nodoVO.getEsNodoRaiz()){
					totalGeneral += nodoVO.getTotal();					
				}
				// Para el Clasificador de Procedencia: Calculamos total de 04.02.90 y 04.02.91 para restarlos al totalRecaudado y obtener el subTotal requerido (pedido por el area de Balance)
				if(cla.getId().longValue() == Clasificador.ID_CLA_PROC.longValue() 
						&& ("04.02.90".equals(nodoVO.getClave()) || "04.02.91".equals(nodoVO.getClave()))){
						aRestar += nodoVO.getTotal();							
				}
			}
			// Para el Clasificador de Procedencia: Calculamos el subTotal (Sin Ctas. 04.02.90 y 04.02.91) (pedido por el area de Balance)
			if(cla.getId().longValue() == Clasificador.ID_CLA_PROC.longValue()){
				subTotal = totalGeneral - aRestar;					
			}
			
			
			informe = new InformeClasificador();
			
			informe.setReporteProcesoBalance("true");
			informe.setFechaReporte(DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
			informe.setDesClasificador(cla.getDescripcion().toUpperCase());
			informe.setDesEjercicio(ejercicio);
			informe.setClasificadorRubro("false");
			informe.setTotalGeneral(StringUtil.formatDoubleWithComa(NumberUtil.truncate(totalGeneral, 2)));
			if(cla.getId().longValue() == Clasificador.ID_CLA_PROC.longValue()){
				informe.setSubTotal(StringUtil.formatDoubleWithComa(NumberUtil.truncate(subTotal, 2)));
				informe.setMostrarSubTotal("true");					
			}
			
			informe.setListNodo(listNodoVOOrdenados);
		
			// Generamos el printModel
			print =null;
			
			print = Formulario.getPrintModelForPDF(Formulario.COD_FRM_CLASIFICADOR);
			
			print.setNoAplicarTrim(true);
			print.putCabecera("TituloReporte", "Clasificador de "+cla.getDescripcion());
			print.putCabecera("Fecha", DateUtil.formatDate(new Date(), DateUtil.dd_MM_YYYY_MASK));
			print.putCabecera("Hora", DateUtil.formatDate(new Date(), DateUtil.HOUR_MINUTE_MASK));
			print.putCabecera("FechaBalance", fechaBalance);
			print.putCabecera("Ejercicio", ejercicio);
			
			print.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
			print.setData(informe);
			print.setTopeProfundidad(2);
				
			// Archivo pdf a generar
			fileName = this.getId().toString()+"ReporteClasificador"+cla.getDescripcion()+".pdf"; 
			pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
			pdfFile.write(print.getByteArray());
			pdfFile.close();
			
			nombre = "Reporte de Clasificador de "+cla.getDescripcion();
			descripcion = "Consulta de Totales para Clasificador. Sólo se incluyen los importes que corresponden al proceso de balance.";
			
			fileInfo = new String[3];
			fileInfo[0] = fileName;
			fileInfo[1] = nombre;
			fileInfo[2] = descripcion;
			listFileInfo.add(fileInfo);
		}
		return listFileInfo;
	}
	
	/**
	 * Consolidar registros procesados. Pasar a estado procesado: Folios, OtrIngTes, Compensaciones.
	 * 
	 * <i>(paso 7.3)</i>
	 */
	public void tareasFinales() throws Exception{
		// Recorremos los Folios incluidos 
		for(Folio folio: this.getListFolio()){
			// Por cada 'Otro Ingreso de Tesoreria'
			for(OtrIngTes otrIngTes: folio.getListOtrIngTes()){
				otrIngTes.cambiarEstOtrIngTes(EstOtrIngTes.ID_PROCESADO);
				BalDAOFactory.getOtrIngTesDAO().update(otrIngTes);
			}
			// Por cada Compensacion (FolCom)
			for(FolCom folCom: folio.getListFolCom()){
				if(folCom.getCompensacion() != null){
					folCom.getCompensacion().cambiarEstado(EstadoCom.ID_ASENTADO, "Balance Nro: "+this.getId());
					BalDAOFactory.getCompensacionDAO().update(folCom.getCompensacion());
					if(!ListUtil.isNullOrEmpty(folCom.getCompensacion().getListSaldoAFavor())){
						for(SaldoAFavor saldoAFavor: folCom.getCompensacion().getListSaldoAFavor()){
							saldoAFavor.desactivar();
						}
					}
				}
			}
			EstadoFol estadoFol = EstadoFol.getById(EstadoFol.ID_PROCESADO);
			folio.setEstadoFol(estadoFol);
			BalDAOFactory.getFolioDAO().update(folio);
		}
		
		// Por cada Compensacion incluida directamente al Balance (Mantis 5413)
		for(Compensacion compensacion: this.getListCompensacion()){
			compensacion.cambiarEstado(EstadoCom.ID_ASENTADO, "Balance Nro: "+this.getId());
			BalDAOFactory.getCompensacionDAO().update(compensacion);
			if(!ListUtil.isNullOrEmpty(compensacion.getListSaldoAFavor())){
				for(SaldoAFavor saldoAFavor: compensacion.getListSaldoAFavor()){
					saldoAFavor.desactivar();
				}
			}
		}
		
		// Por cada Reingreso (de Duplice o Indeterminado) incluido en el Balance.
		for(Reingreso reingreso: this.getListReingreso()) {
			// Buscamos el registro de reingreso en la base de Indeterminados
			IndetVO indet = BalDAOFactory.getIndeterminadoJDBCDAO().getReingresoById(reingreso.getNroReingreso());
			// Si se encuentra se le modifica la fecha_rein_r en la base de indeterminados
			if(indet != null && indet.getNroReing() != null){
				indet.setFechaReing(new Date());
				
				// Correccion por campos null
				if(StringUtil.isNullOrEmpty(indet.getResto()))
						indet.setResto("0");
				if(indet.getFechaBalance() == null)
					indet.setFechaBalance(this.getFechaBalance());
				
				BalDAOFactory.getIndeterminadoJDBCDAO().updateReingreso(indet);
			}
		}
		
	}
	
	public List<FilaVO> ordenarListaPartida(List<FilaVO> listFila){
    	Comparator<FilaVO> comparator = new Comparator<FilaVO>(){
			public int compare(FilaVO fila1, FilaVO fila2) {
				String codPartidaFila1 = fila1.getListCelda().get(0).getValor();
				String codPartidaFila2 = fila2.getListCelda().get(0).getValor();
				return codPartidaFila1.compareTo(codPartidaFila2);
			}    		
    	};    	
    	Collections.sort(listFila, comparator);
    	return listFila;
    }
	
	public List<String> ordenarListClaveArchivo(List<String> listClave){
		if(ListUtil.isNullOrEmpty(listClave)){
			return listClave;
		}
    	Comparator<String> comparator = new Comparator<String>(){
			public int compare(String clave1, String clave2) {
				Datum datum1 = Datum.parse(clave1);
				Datum datum2 = Datum.parse(clave2);
				String fechaStr1 = datum1.getCols(0);
				String fechaStr2 = datum2.getCols(0);
				Date fecha1 = DateUtil.getDate(fechaStr1,  DateUtil.ddSMMSYYYY_MASK);
				Date fecha2 = DateUtil.getDate(fechaStr2,  DateUtil.ddSMMSYYYY_MASK);
				
				return DateUtil.dateCompare(fecha1, fecha2);	
				//return codPartida1.compareTo(codPartida2);
			}    		
    	};    	
    	Collections.sort(listClave, comparator);
    	return listClave;
    }
	
	/**
	 *  Genera el Archivo de Planilla (*.cvs) para los Indeterminados generados en los Asentamientos. 
	 * 
	 * @param balance
	 * @param fileDir
	 * @return
	 * @throws Exception
	 */
	public List<String> exportReportesTranBalSellado(String fileDir) throws Exception{

		List<String> listPlanillaName = new ArrayList<String>();
	
		int indiceArchivo = 0;
		//Genero el archivo de texto
		String idBalance = this.getId().toString();
		String fileName = idBalance+"Sellados_"+indiceArchivo+".csv";
		
		listPlanillaName.add(fileName);

		BufferedWriter buffer = this.createEncForPlanillaSellado(new FileWriter(fileDir+"/"+fileName, false));

		// --> Resultado
		boolean resultadoVacio = true;
		long c = 0;                     // contador de registros

		
		// Recorrer los Archivos incluidos en el Balance
		for(Archivo archivo: this.getListArchivo()){
			// Si el archivo es de Tipo Banco (BMR)
			if(archivo.getTipoArc().getId().longValue() == TipoArc.ID_BMR && archivo.isPrefixSE()){ 
				resultadoVacio = false;
				for(TranArc tranArc: archivo.getListTranArc()){
					//Distrito, Cajero, Codigo, Importe, Fecha, Verificador, Caja, Transaccion, Verificador 2
					buffer.write(tranArc.getFilial());				
					buffer.write(", " +  tranArc.getCajero());
					buffer.write(", " +  tranArc.getCodigoSellado());
					buffer.write(", " +  NumberUtil.truncate(tranArc.getImportePago(), SiatParam.DEC_IMPORTE_DB));
					buffer.write(", " +  DateUtil.formatDate(tranArc.getFechaPago(), DateUtil.ddSMMSYYYY_MASK));
					buffer.write(", " +  tranArc.getPrimerCodigoVerificador());
					buffer.write(", " +  tranArc.getCaja().toString());
					buffer.write(", " +  tranArc.getTransaccion());
					buffer.write(", " +  tranArc.getSegundoCodigoVerificador());
					
					c++;
					if(c == 65534 ){
						buffer.close();				
						indiceArchivo++;
						fileName = idBalance+"Sellados_"+indiceArchivo+".csv";
						listPlanillaName.add(fileName);
						buffer = this.createEncForPlanillaSellado(new FileWriter(fileDir+"/"+fileName, false));
						c = 0; 
					}else{
						buffer.newLine();
					}
				}
			}
		}

		// --> Resultado vacio
		if(resultadoVacio ){
			return null;
		}		 
		// <-- Fin Resultado vacio
		
		buffer.close();

		return listPlanillaName;
	}
	
	/**
	 *  Crea un BufferWriter, y carga el encabezado que corresponde para la planilla de transacciones de sellado.
	 * 
	 * @param fileWriter
	 * @return
	 * @throws Exception
	 */
	private BufferedWriter createEncForPlanillaSellado(FileWriter fileWriter) throws Exception{

		BufferedWriter buffer = new BufferedWriter(fileWriter);
		// --> Creacion del Encabezado del Resultado
		//Distrito, Cajero, Codigo, Importe, Fecha, Verificador, Caja, Transaccion, Verificador 2
		buffer.write("Distrito");
		buffer.write(", Cajero"); // Solo para el caso de Sistema con Tipo Deuda "Deuda Pura", anioComprobante<>"99" y periodo<>"99"
		buffer.write(", Código");
		buffer.write(", Importe");
		buffer.write(", Fecha");
		buffer.write(", Verificador");
		buffer.write(", Caja");
		buffer.write(", Transacción");
		buffer.write(", Verificador 2");
		// <-- Fin Creacion del Encabezado del Resultado
		buffer.newLine();
		
		return buffer;
	}
	
	/**
	 * Genera el Reporte pdf "Detalle de Caja 7" resultado del paso 5 del proceso de Balance.
	 * 
	 * @param fileDir
	 * @return fileName
	 */
	public String generarPdfCaja7(String fileDir) throws Exception{
		//	Encabezado:
		String fechaBalance = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		
		List<Caja7> listCaja7 = this.getListCaja7();
	
		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Balance.COD_FRM_CAJA7);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Reporte de Balance - Detalle de Caja 7");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Reporte de Balance - Detalle de Caja7");
		printModel.putCabecera("NroBalance", this.getId().toString());
		printModel.putCabecera("FechaBalance", fechaBalance);
		printModel.putCabecera("Ejercicio", ejercicio);
		
		Double importeTotal = 0D;

		// Armamos un Contenedor para las Tablas
		ContenedorVO contenedor = new ContenedorVO("ContenedorTabla");

		// Se arman una tabla para el Reporte Detalle de Caja 7
		TablaVO tabla = new TablaVO("DetalleCaja7");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Código","codigo"));
		filaCabecera.add(new CeldaVO("Descripción","descripcion"));
		filaCabecera.add(new CeldaVO("Vencido","importeEjeVen"));
		filaCabecera.add(new CeldaVO("Actual","importeEjeAct"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		for(Caja7 caja7: listCaja7){
			Double suma = caja7.getImporteEjeAct()+caja7.getImporteEjeVen();

			FilaVO fila = new FilaVO();
			String codPartida = StringUtil.completarCerosIzq(caja7.getPartida().getCodPartida().trim(),5);
			fila.add(new CeldaVO(codPartida,"codigo"));
			fila.add(new CeldaVO(caja7.getPartida().getDesPartida(),"descripcion"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(caja7.getImporteEjeVen(), SiatParam.DEC_IMPORTE_DB)),"importe"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(caja7.getImporteEjeAct(), SiatParam.DEC_IMPORTE_DB)),"importe"));
			fila.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(suma, SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			importeTotal += caja7.getImporteEjeAct()+caja7.getImporteEjeVen();
		}
		tabla.setListFila(this.ordenarListaPartida(tabla.getListFila()));
		
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("","vacio"));
		filaPie.add(new CeldaVO("","vacio"));
		filaPie.add(new CeldaVO("","vacio"));
		filaPie.add(new CeldaVO("Total:","desTotal"));
		filaPie.add(new CeldaVO(StringUtil.formatDoubleWithComa(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"total"));
		tabla.addFilaPie(filaPie);
		
		contenedor.add(tabla);
				
		// Cargamos los datos en el Print Model
		printModel.setData(contenedor);
		printModel.setTopeProfundidad(4);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"ReporteDetalleCaja7-"+corrida.getPasoActual().toString()+".pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		
		return fileName;
	}
}
