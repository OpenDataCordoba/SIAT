//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.bal.buss.cache.AsentamientoCache;
import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.buss.bean.CasSolicitudManager;
import ar.gov.rosario.siat.cas.buss.bean.TipoSolicitud;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Banco;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.buss.bean.TipoDeuda;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import ar.gov.rosario.siat.gde.buss.bean.ConDeuCuo;
import ar.gov.rosario.siat.gde.buss.bean.ConDeuDet;
import ar.gov.rosario.siat.gde.buss.bean.ConEstCon;
import ar.gov.rosario.siat.gde.buss.bean.ConstanciaDeu;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConCuo;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConvenio;
import ar.gov.rosario.siat.gde.buss.bean.EstadoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.PagoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import ar.gov.rosario.siat.gde.buss.bean.RecConCuo;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboConvenio;
import ar.gov.rosario.siat.gde.buss.bean.ReciboDeuda;
import ar.gov.rosario.siat.gde.buss.bean.TipoPago;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pro.buss.bean.Corrida;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.adpcore.AdpRunDirEnum;
import coop.tecso.demoda.buss.bean.BaseBO;
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

/**
 * Bean correspondiente a Asentamiento
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_asentamiento")
public class Asentamiento extends BaseBO {
	@Transient
	private static Logger log = Logger.getLogger(Asentamiento.class);
	
	public static final String COD_FRM_PASO1_1="ASE_TOT_SIS";
	public static final String COD_FRM_PASO1_SE="ASE_TOT_SEL";
	public static final String COD_FRM_PASO2_1="ASE_DIS_PAR";
	public static final String COD_FRM_PASO2_2="ASE_DIS_PAR_SB";
	public static final String COD_FRM_PASO2_3="ASE_RES_PRO";
	public static final String COD_FRM_PASO2_4="ASE_TOT_REC";
	
	private static final long serialVersionUID = 1L;

	@Column(name = "fechaBalance")
	private Date fechaBalance;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idBalance") 
	private Balance balance;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idEjercicio") 
	private Ejercicio ejercicio;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idServicioBanco") 
	private ServicioBanco servicioBanco;
	
    @Column(name="idCaso") 
	private String idCaso;
	
	@Column(name = "observacion")
	private String observacion;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name = "idCorrida")
	private Corrida corrida;
	
	@Column(name = "usuarioAlta")
	private String usuarioAlta;
	
	@OneToMany(mappedBy="asentamiento")
	@JoinColumn(name="idAsentamiento")
	@OrderBy(clause="fechaPago, id")
	private List<Transaccion> listTransaccion;
	
	@OneToMany()
	@JoinColumn(name="idAsentamiento")
	private List<SinPartida> listSinPartida;

	@OneToMany()
	@JoinColumn(name="idAsentamiento")
	private List<SinIndet> listSinIndet;

	@OneToMany()
	@JoinColumn(name="idAsentamiento")
	private List<SinSalAFav> listSinSalAFav;

	@OneToMany()
	@JoinColumn(name="idAsentamiento")
	private List<AuxPagDeu> listAuxPagDeu;

	@OneToMany()
	@JoinColumn(name="idAsentamiento")
	private List<AuxPagCuo> listAuxPagCuo;

	@OneToMany()
	@JoinColumn(name="idAsentamiento")
	private List<AuxConDeu> listAuxConDeu;

	@OneToMany()
	@JoinColumn(name="idAsentamiento")
	private List<AuxConDeuCuo> listAuxConDeuCuo;

	@OneToMany()
	@JoinColumn(name="idAsentamiento")
	private List<AuxConvenio> listAuxConvenio;

	@OneToMany()
	@JoinColumn(name="idAsentamiento")
	private List<AuxDeuMdf> listAuxDeuMdf;
	
	@Transient
	private TipoImporte tipoImporteIndet;
	
	//Constructores 
	public Asentamiento(){
		super();
	}

	// Getters Y Setters
	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public Ejercicio getEjercicio() {
		return ejercicio;
	}
	public void setEjercicio(Ejercicio ejercicio) {
		this.ejercicio = ejercicio;
	}
	public Date getFechaBalance() {
		return fechaBalance;
	}
	public void setFechaBalance(Date fechaBalance) {
		this.fechaBalance = fechaBalance;
	}
	public Corrida getCorrida() {
		return corrida;
	}
	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public ServicioBanco getServicioBanco() {
		return servicioBanco;
	}
	public void setServicioBanco(ServicioBanco servicioBanco) {
		this.servicioBanco = servicioBanco;
	}
	public String getUsuarioAlta() {
		return usuarioAlta;
	}
	public void setUsuarioAlta(String usuarioAlta) {
		this.usuarioAlta = usuarioAlta;
	}
	public List<Transaccion> getListTransaccion() {
		return listTransaccion;
	}
	public void setListTransaccion(List<Transaccion> listTransaccion) {
		this.listTransaccion = listTransaccion;
	}
	public List<SinPartida> getListSinPartida() {
		return listSinPartida;
	}
	public void setListSinPartida(List<SinPartida> listSinPartida) {
		this.listSinPartida = listSinPartida;
	}
	public List<AuxPagDeu> getListAuxPagDeu() {
		return listAuxPagDeu;
	}
	public void setListAuxPagDeu(List<AuxPagDeu> listAuxPagDeu) {
		this.listAuxPagDeu = listAuxPagDeu;
	}
	public List<AuxPagCuo> getListAuxPagCuo() {
		return listAuxPagCuo;
	}
	public void setListAuxPagCuo(List<AuxPagCuo> listAuxPagCuo) {
		this.listAuxPagCuo = listAuxPagCuo;
	}
	public List<AuxConDeu> getListAuxConDeu() {
		return listAuxConDeu;
	}
	public void setListAuxConDeu(List<AuxConDeu> listAuxConDeu) {
		this.listAuxConDeu = listAuxConDeu;
	}
	public List<AuxConDeuCuo> getListAuxConDeuCuo() {
		return listAuxConDeuCuo;
	}
	public void setListAuxConDeuCuo(List<AuxConDeuCuo> listAuxConDeuCuo) {
		this.listAuxConDeuCuo = listAuxConDeuCuo;
	}
	public List<AuxConvenio> getListAuxConvenio() {
		return listAuxConvenio;
	}
	public void setListAuxConvenio(List<AuxConvenio> listAuxConvenio) {
		this.listAuxConvenio = listAuxConvenio;
	}	
	public TipoImporte getTipoImporteIndet() {
		return tipoImporteIndet;
	}
	public void setTipoImporteIndet(TipoImporte tipoImporteIndet) {
		this.tipoImporteIndet = tipoImporteIndet;
	}
	public Balance getBalance() {
		return balance;
	}
	public void setBalance(Balance balance) {
		this.balance = balance;
	}
	public List<SinIndet> getListSinIndet() {
		return listSinIndet;
	}
	public void setListSinIndet(List<SinIndet> listSinIndet) {
		this.listSinIndet = listSinIndet;
	}
	public List<SinSalAFav> getListSinSalAFav() {
		return listSinSalAFav;
	}
	public void setListSinSalAFav(List<SinSalAFav> listSinSalAFav) {
		this.listSinSalAFav = listSinSalAFav;
	}
	public List<AuxDeuMdf> getListAuxDeuMdf() {
		return listAuxDeuMdf;
	}
	public void setListAuxDeuMdf(List<AuxDeuMdf> listAuxDeuMdf) {
		this.listAuxDeuMdf = listAuxDeuMdf;
	}

	// Metodos de clase	
	public static Asentamiento getById(Long id) {
		return (Asentamiento) BalDAOFactory.getAsentamientoDAO().getById(id);
	}
	
	public static Asentamiento getByIdNull(Long id) {
		return (Asentamiento) BalDAOFactory.getAsentamientoDAO().getByIdNull(id);
	}
	
	public static List<Asentamiento> getList() {
		return (ArrayList<Asentamiento>) BalDAOFactory.getAsentamientoDAO().getList();
	}
	
	public static List<Asentamiento> getListActivos() {			
		return (ArrayList<Asentamiento>) BalDAOFactory.getAsentamientoDAO().getListActiva();
	}
	public List<String> exportReportesTransacciones(String fileDir) throws Exception{
		return BalDAOFactory.getAsentamientoDAO().exportReportesTransaccionesByAsentamiento(this, fileDir);
	}
	public List<String> exportReportesPagosAsentados(String fileDir) throws Exception{
		return BalDAOFactory.getAsentamientoDAO().exportReportesPagosAsentadosByAsentamiento(this, fileDir);
	}
	public List<String> exportReportesIndeterminados(String fileDir) throws Exception{
		return BalDAOFactory.getAsentamientoDAO().exportReportesIndeterminadosByAsentamiento(this, fileDir);
	}
	public List<String> exportReportesSaldosAFavor(String fileDir) throws Exception{
		return BalDAOFactory.getAsentamientoDAO().exportReportesSaldosAFavorByAsentamiento(this, fileDir);
	}
	public List<String> exportReportesConveniosCancelados(String fileDir) throws Exception{ 
		return BalDAOFactory.getAsentamientoDAO().exportReportesConveniosCanceladosByAsentamiento(this, fileDir);
	}
	public List<String> exportReportesConveniosARevision(String fileDir) throws Exception{ 
		return BalDAOFactory.getAsentamientoDAO().exportReportesConveniosARevisionByAsentamiento(this, fileDir);
	}
	
	/**
	 *  Devuelve el Asentamiento asociado al Balance y para el Servicio Banco de ids pasados como parametro.
	 *  
	 * @param idBalance
	 * @param idServicioBanco
	 * @return asentamiento
	 */
	public static Asentamiento getByIdBalanceYIdSerBan(Long idBalance, Long idServicioBanco) {
		return (Asentamiento) BalDAOFactory.getAsentamientoDAO().getByIdBalanceYIdSerBan(idBalance, idServicioBanco);
	}
	
	@Deprecated
	public static Date getFechaUltimoAsentamientoExitoso(){
		return BalDAOFactory.getAsentamientoDAO().getFechaUltimoAsentamientoExitoso();
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
		if(getServicioBanco()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.ASENTAMIENTO_SERVICIO_BANCO);
		}
		if(getFechaBalance()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.ASENTAMIENTO_FECHABALANCE);
		}
		if(getEjercicio()==null){
			addRecoverableError(BalError.ASENTAMIENTO_EJERCICIO_NO_ENCONTRADO);
		}
		if(getCorrida()==null){
			addRecoverableError(BalError.ASENTAMIENTO_CORRIDA_NO_GENERADA);
		}
		if(StringUtil.isNullOrEmpty(getUsuarioAlta())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.ASENTAMIENTO_USUARIOALTA);
		}
		
		if (hasError()) {
			return false;
		}

		if(this.existAsentamientoBySerBanForCreate(this.getServicioBanco())){
			addRecoverableError(BalError.ASENTAMIENTO_EXISTENTE);
		}
		if(this.getEjercicio().getEstEjercicio().getId().longValue() == EstEjercicio.ID_FUTURO){
			addRecoverableError(BalError.ASENTAMIENTO_EJERCICIO_FUTURO);
		}
		
		// Valida que la Fecha Balance no sea mayor que la fecha Actual
		if(!DateUtil.isDateBeforeOrEqual(this.fechaBalance, new Date())){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, BalError.ASENTAMIENTO_FECHABALANCE, BaseError.MSG_FECHA_ACTUAL);
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
		
		if (GenericDAO.hasReference(this, Transaccion.class, "asentamiento")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.ASENTAMIENTO_LABEL , BalError.TRANSACCION_LABEL);
		}
		if (GenericDAO.hasReference(this, AuxRecaudado.class, "asentamiento")			
				|| GenericDAO.hasReference(this, AuxPagDeu.class, "asentamiento")
				|| GenericDAO.hasReference(this, AuxSellado.class, "asentamiento")
				|| GenericDAO.hasReference(this, SinIndet.class, "asentamiento")
				|| GenericDAO.hasReference(this, SinPartida.class, "asentamiento")
				|| GenericDAO.hasReference(this, SinSalAFav.class, "asentamiento")) {
			
			addRecoverableError(BalError.ASENTAMIENTO_AUXILIAR_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	// Administracion de Transaccion
	public Transaccion createTransaccion(Transaccion transaccion) throws Exception {
		
		// Validaciones de negocio
		if (!transaccion.validateCreate()) {
			return transaccion;
		}

		BalDAOFactory.getTransaccionDAO().update(transaccion);
		
		return transaccion;
	}	

	public Transaccion updateTransaccion(Transaccion transaccion) throws Exception {
		
		// Validaciones de negocio
		if (!transaccion.validateUpdate()) {
			return transaccion;
		}

		BalDAOFactory.getTransaccionDAO().update(transaccion);
		
		return transaccion;
	}	

	public Transaccion deleteTransaccion(Transaccion transaccion) throws Exception {
		
		// Validaciones de negocio
		if (!transaccion.validateDelete()) {
			return transaccion;
		}
				
		BalDAOFactory.getTransaccionDAO().delete(transaccion);
		
		return transaccion;
	}	
	
	// Administracion de AuxRecaudado	
	/**
	 * Obtiene el AuxRecaudado correspondiente en el mapa. Si no lo encuentra retorna null;
	 * 
	 */
	public AuxRecaudado getAuxRecaudado(Transaccion transaccion,Partida partida, Double porcentaje,ViaDeuda viaDeuda , Plan plan,Long tipoBoleta) throws Exception {
		AuxRecaudado auxRecaudado = null;
		StringBuffer clave = new StringBuffer();
		clave.append(transaccion.getSistema());
		clave.append("/");
		clave.append(tipoBoleta);
		clave.append("/");
		//clave.append(disParDet.getId()); // Para quitar el disParDet del AuxRecaudado se deberia reemplazar este string por uno que incluya el idPartida y el porcentaje de distribucion
		clave.append(partida.getId());
		clave.append("/");
		clave.append(porcentaje);
		clave.append("/");
		clave.append(DateUtil.formatDate(transaccion.getFechaPago(), DateUtil.ddSMMSYYYY_MASK));
		if(viaDeuda != null){
			clave.append("/");
			clave.append(viaDeuda.getId());			
		}
		if(plan != null){
			clave.append("/");
			clave.append(plan.getId());			
		}        		
		auxRecaudado = AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxRecaudado().get(clave.toString());
		//auxRecaudado = this.mapAuxRecaudado.get(clave.toString());
		
		return auxRecaudado;
	}
	
	/**
	 *  Guarda un resistro de AuxRecaudado en un mapa en memoria.
	 *  
	 * @param auxRecaudado
	 * @return
	 * @throws Exception
	 */
	public AuxRecaudado createAuxRecaudado(AuxRecaudado auxRecaudado) throws Exception {

		// Validaciones de negocio
		if (!auxRecaudado.validateCreate()) {
			return auxRecaudado;
		}

		//BalDAOFactory.getAuxRecaudadoDAO().update(auxRecaudado);
		StringBuffer clave = new StringBuffer();
		clave.append(auxRecaudado.getSistema().getNroSistema());
		clave.append("/");
		clave.append(auxRecaudado.getTipoBoleta());
		clave.append("/");
		//clave.append(auxRecaudado.getDisParDet().getId());  // Para quitar el disParDet del AuxRecaudado se deberia reemplazar este string por uno que incluya el idPartida y el porcentaje de distribucion
		clave.append(auxRecaudado.getPartida().getId());
		clave.append("/");
		clave.append(auxRecaudado.getPorcentaje());
		clave.append("/");
		clave.append(DateUtil.formatDate(auxRecaudado.getFechaPago(), DateUtil.ddSMMSYYYY_MASK));
		if(auxRecaudado.getViaDeuda() != null){
			clave.append("/");
			clave.append(auxRecaudado.getViaDeuda().getId());			
		}
		if(auxRecaudado.getPlan() != null){
			clave.append("/");
			clave.append(auxRecaudado.getPlan().getId());			
		}        	
		AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxRecaudado().put(clave.toString(), auxRecaudado);
		//this.mapAuxRecaudado.put(clave.toString(), auxRecaudado);
		
		return auxRecaudado;
	}
	
	/**
	 * 	Guarda en la DB todos los registros de AuxRecaudado del mapa.
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean saveMapAuxRecaudado() throws Exception {
		List<AuxRecaudado> listAuxRecaudado = new ArrayList<AuxRecaudado>(AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxRecaudado().values());
		for(AuxRecaudado auxRecaudado: listAuxRecaudado){
			// Validaciones de negocio
			if (!auxRecaudado.validateUpdate()) {
				return false;
			}
			BalDAOFactory.getAuxRecaudadoDAO().update(auxRecaudado);
		}

		AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxRecaudado().clear();
		//this.mapAuxRecaudado.clear();
		
	    return true;
	}
		
	// Administracion de AuxSellado
	
	/**
	 * Obtiene el AuxSellado correspondiente en el mapa. Si no lo encuentra retorna null;
	 * 
	 */
	public AuxSellado getAuxSellado(Transaccion transaccion, ParSel parSel) throws Exception {
		AuxSellado auxSellado = null;
		
		StringBuffer clave = new StringBuffer();
		clave.append(transaccion.getSistema());
		clave.append("/");
		clave.append(DateUtil.formatDate(transaccion.getFechaPago(), DateUtil.ddSMMSYYYY_MASK));
		clave.append("/");
		clave.append(parSel.getSellado().getId());
		clave.append("/");
		clave.append(parSel.getPartida().getId());
		
		if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_PORCENTAJE) {
			clave.append("/");
			clave.append("esPorcentaje");
		}
		
		if(parSel.getTipoDistrib().getId().longValue() == TipoDistrib.ID_MONTO_FIJO) {
			clave.append("/");
			clave.append("esImporteFijo");
		}
		
		auxSellado = AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxSellado().get(clave.toString());
		
		return auxSellado;
	}
	
	/**
	 *  Guarda un resistro de AuxSellado en un mapa en memoria.
	 *  
	 * @param auxSellado
	 * @return
	 * @throws Exception
	 */
	public AuxSellado createAuxSellado(AuxSellado auxSellado) throws Exception {

		// Validaciones de negocio
		if (!auxSellado.validateCreate()) {
			return auxSellado;
		}

		//BalDAOFactory.getAuxSelladoDAO().update(auxSellado);
		StringBuffer clave = new StringBuffer();
		clave.append(auxSellado.getSistema().getNroSistema());
		clave.append("/");
		clave.append(DateUtil.formatDate(auxSellado.getFechaPago(), DateUtil.ddSMMSYYYY_MASK));
		clave.append("/");
		clave.append(auxSellado.getSellado().getId());
		clave.append("/");
		clave.append(auxSellado.getPartida().getId());
		
		if(auxSellado.getEsImporteFijo().intValue() == SiNo.NO.getId().intValue()) {
			clave.append("/");
			clave.append("esPorcentaje");
		}
		
		if(auxSellado.getEsImporteFijo().intValue() == SiNo.SI.getId().intValue()) {
			clave.append("/");
			clave.append("esImporteFijo");
		}
	
		AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxSellado().put(clave.toString(), auxSellado);
		
		return auxSellado;
	}
	
	/**
	 * 	Guarda en la DB todos los registros de AuxSellado del mapa.
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean saveMapAuxSellado() throws Exception {
		List<AuxSellado> listAuxSellado = new ArrayList<AuxSellado>(AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxSellado().values());
		for(AuxSellado auxSellado: listAuxSellado){
			// Validaciones de negocio
			if (!auxSellado.validateUpdate()) {
				return false;
			}
			BalDAOFactory.getAuxSelladoDAO().update(auxSellado);
		}
		AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxSellado().clear();
		
	    return true;
	}
		
	// Administracion de AuxPagDeu
	public AuxPagDeu createAuxPagDeu(AuxPagDeu auxPagDeu) throws Exception {

		// Validaciones de negocio
		if (!auxPagDeu.validateCreate()) {
			return auxPagDeu;
		}

		BalDAOFactory.getAuxPagDeuDAO().update(auxPagDeu);

		return auxPagDeu;
	}
	
	public AuxPagDeu updateAuxPagDeu(AuxPagDeu auxPagDeu) throws Exception {
		
		// Validaciones de negocio
		if (!auxPagDeu.validateUpdate()) {
			return auxPagDeu;
		}
		
		BalDAOFactory.getAuxPagDeuDAO().update(auxPagDeu);
		
	    return auxPagDeu;
	}
	
	public AuxPagDeu deleteAuxPagDeu(AuxPagDeu auxPagDeu) throws Exception {

		
		// Validaciones de negocio
		if (!auxPagDeu.validateDelete()) {
			return auxPagDeu;
		}
		
		BalDAOFactory.getAuxPagDeuDAO().delete(auxPagDeu);
		
		return auxPagDeu;
	}	
	// Administracion de AuxPagCuo
	public AuxPagCuo createAuxPagCuo(AuxPagCuo auxPagCuo) throws Exception {

		// Validaciones de negocio
		if (!auxPagCuo.validateCreate()) {
			return auxPagCuo;
		}

		BalDAOFactory.getAuxPagCuoDAO().update(auxPagCuo);

		return auxPagCuo;
	}
	
	public AuxPagCuo updateAuxPagCuo(AuxPagCuo auxPagCuo) throws Exception {
		
		// Validaciones de negocio
		if (!auxPagCuo.validateUpdate()) {
			return auxPagCuo;
		}
		
		BalDAOFactory.getAuxPagCuoDAO().update(auxPagCuo);
		
	    return auxPagCuo;
	}
	
	public AuxPagCuo deleteAuxPagCuo(AuxPagCuo auxPagCuo) throws Exception {

		
		// Validaciones de negocio
		if (!auxPagCuo.validateDelete()) {
			return auxPagCuo;
		}
		
		BalDAOFactory.getAuxPagCuoDAO().delete(auxPagCuo);
		
		return auxPagCuo;
	}	

	// Administracion de AuxConvenio	
	/**
	 * Obtiene el AuxConvenio correspondiente en el mapa. Si no lo encuentra retorna null;
	 * 
	 */
	public AuxConvenio getAuxConvenio( Convenio convenio, TipoSolicitud tipoSolicitud) throws Exception {
		AuxConvenio auxConvenio = null;
		StringBuffer clave = new StringBuffer();
		clave.append(convenio.getId());
		clave.append("/");
		clave.append(tipoSolicitud.getId());

		auxConvenio = AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxConvenio().get(clave.toString());
		
		return auxConvenio;
	}
	
	/**
	 *  Guarda un registro de AuxConvenio en un mapa en memoria.
	 *  
	 * @param auxConvenio
	 * @return
	 * @throws Exception
	 */
	public AuxConvenio createAuxConvenio(AuxConvenio auxConvenio) throws Exception {

		// Validaciones de negocio
		if (!auxConvenio.validateCreate()) {
			return auxConvenio;
		}
		//BalDAOFactory.getAuxConvenioDAO().update(auxConvenio);

		StringBuffer clave = new StringBuffer();
		clave.append(auxConvenio.getConvenio().getId());
		clave.append("/");
		clave.append(auxConvenio.getTipoSolicitud().getId());

		AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxConvenio().put(clave.toString(), auxConvenio);
		
		return auxConvenio;
	}
	
	/**
	 * 	Guarda en la DB todos los registros de AuxConvenio del mapa.
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean saveMapAuxConvenio() throws Exception {
		List<AuxConvenio> listAuxConvenio = new ArrayList<AuxConvenio>(AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxConvenio().values());
		for(AuxConvenio auxConvenio: listAuxConvenio){
			// Validaciones de negocio
			if (!auxConvenio.validateUpdate()) {
				return false;
			}
			BalDAOFactory.getAuxConvenioDAO().update(auxConvenio);
		}
		AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxConvenio().clear();
		
	    return true;
	}
	
	// Administracion de AuxConDeu
	/**
	 * Obtiene el AuxConDeu con saldo mayor cero para el idConvenio pasado en el mapa.
	 * Si no lo encuentra retorna null;
	 * 
	 * @param idConvenio
	 * @return auxConDeu
	 */
	public AuxConDeu getAuxConDeuConSaldoMayorCero(Long idConvenio) throws Exception {
		
		AuxConDeu auxConDeuSel = null;
		List<AuxConDeu> listAuxConDeu = new ArrayList<AuxConDeu>(AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxConDeu().values());
		for(AuxConDeu auxConDeu: listAuxConDeu){
			if(auxConDeu.getConvenioDeuda().getConvenio().getId().longValue() == idConvenio.longValue()  
					&& auxConDeu.getSaldoEnPlan() > 0){
				auxConDeuSel = auxConDeu; 
				return auxConDeuSel;
			}
		}
		
		return auxConDeuSel;
	}
	
	/**
	 *  Guarda un registro de AuxConDeu en un mapa en memoria.
	 *  
	 * @param auxConDeu
	 * @return
	 * @throws Exception
	 */
	public AuxConDeu createAuxConDeu(AuxConDeu auxConDeu) throws Exception {

		// Validaciones de negocio
		if (!auxConDeu.validateCreate()) {
			return auxConDeu;
		}
		//BalDAOFactory.getAuxConDeuDAO().update(auxConDeu);
		AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxConDeu().put(auxConDeu.getConvenioDeuda().getId().toString(), auxConDeu);
		
		return auxConDeu;
	}
	
	/**
	 * 	Guarda en la DB todos los registros de AuxConDeu del mapa.
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean saveMapAuxConDeu() throws Exception {
		List<AuxConDeu> listAuxConDeu = new ArrayList<AuxConDeu>(AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxConDeu().values());
		for(AuxConDeu auxConDeu: listAuxConDeu){
			// Validaciones de negocio
			if (!auxConDeu.validateUpdate()) {
				return false;
			}
			BalDAOFactory.getAuxConDeuDAO().update(auxConDeu);
		}
		AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxConDeu().clear();
		
	    return true;
	}
		
	public AuxConDeu updateAuxConDeu(AuxConDeu auxConDeu) throws Exception {
		
		// Validaciones de negocio
		if (!auxConDeu.validateUpdate()) {
			return auxConDeu;
		}
		
		BalDAOFactory.getAuxConDeuDAO().update(auxConDeu);
		
	    return auxConDeu;
	}
	
	public AuxConDeu deleteAuxConDeu(AuxConDeu auxConDeu) throws Exception {

		
		// Validaciones de negocio
		if (!auxConDeu.validateDelete()) {
			return auxConDeu;
		}
		
		BalDAOFactory.getAuxConDeuDAO().delete(auxConDeu);
		
		return auxConDeu;
	}	
	// Administracion de AuxConDeuCuo
	public AuxConDeuCuo createAuxConDeuCuo(AuxConDeuCuo auxConDeuCuo) throws Exception {

		// Validaciones de negocio
		if (!auxConDeuCuo.validateCreate()) {
			return auxConDeuCuo;
		}

		BalDAOFactory.getAuxConDeuCuoDAO().update(auxConDeuCuo);

		return auxConDeuCuo;
	}
	
	public AuxConDeuCuo updateAuxConDeuCuo(AuxConDeuCuo auxConDeuCuo) throws Exception {
		
		// Validaciones de negocio
		if (!auxConDeuCuo.validateUpdate()) {
			return auxConDeuCuo;
		}
		
		BalDAOFactory.getAuxConDeuCuoDAO().update(auxConDeuCuo);
		
	    return auxConDeuCuo;
	}
	
	public AuxConDeuCuo deleteAuxConDeuCuo(AuxConDeuCuo auxConDeuCuo) throws Exception {

		
		// Validaciones de negocio
		if (!auxConDeuCuo.validateDelete()) {
			return auxConDeuCuo;
		}
		
		BalDAOFactory.getAuxConDeuCuoDAO().delete(auxConDeuCuo);
		
		return auxConDeuCuo;
	}	
	// Administracion de SinSalAFav
	public SinSalAFav createSinSalAFav(SinSalAFav sinSalAFav) throws Exception {

		// Validaciones de negocio
		if (!sinSalAFav.validateCreate()) {
			return sinSalAFav;
		}

		BalDAOFactory.getSinSalAFavDAO().update(sinSalAFav);

		return sinSalAFav;
	}
	
	public SinSalAFav updateSinSalAFav(SinSalAFav sinSalAFav) throws Exception {
		
		// Validaciones de negocio
		if (!sinSalAFav.validateUpdate()) {
			return sinSalAFav;
		}
		
		BalDAOFactory.getSinSalAFavDAO().update(sinSalAFav);
		
	    return sinSalAFav;
	}
	
	public SinSalAFav deleteSinSalAFav(SinSalAFav sinSalAFav) throws Exception {

		
		// Validaciones de negocio
		if (!sinSalAFav.validateDelete()) {
			return sinSalAFav;
		}
		
		BalDAOFactory.getSinSalAFavDAO().delete(sinSalAFav);
		
		return sinSalAFav;
	}	

	// Administracion de SinIndet
	public SinIndet createSinIndet(SinIndet sinIndet) throws Exception {

		// Validaciones de negocio
		if (!sinIndet.validateCreate()) {
			return sinIndet;
		}

		BalDAOFactory.getSinIndetDAO().update(sinIndet);

		return sinIndet;
	}
	
	public SinIndet updateSinIndet(SinIndet sinIndet) throws Exception {
		
		// Validaciones de negocio
		if (!sinIndet.validateUpdate()) {
			return sinIndet;
		}
		
		BalDAOFactory.getSinIndetDAO().update(sinIndet);
		
	    return sinIndet;
	}
	
	public SinIndet deleteSinIndet(SinIndet sinIndet) throws Exception {

		
		// Validaciones de negocio
		if (!sinIndet.validateDelete()) {
			return sinIndet;
		}
		
		BalDAOFactory.getSinIndetDAO().delete(sinIndet);
		
		return sinIndet;
	}	

	// Administracion de SinPartida
	public SinPartida createSinPartida(SinPartida sinPartida) throws Exception {

		// Validaciones de negocio
		if (!sinPartida.validateCreate()) {
			return sinPartida;
		}

		BalDAOFactory.getSinPartidaDAO().update(sinPartida);

		return sinPartida;
	}
	
	public SinPartida updateSinPartida(SinPartida sinPartida) throws Exception {
		
		// Validaciones de negocio
		if (!sinPartida.validateUpdate()) {
			return sinPartida;
		}
		
		BalDAOFactory.getSinPartidaDAO().update(sinPartida);
		
	    return sinPartida;
	}
	
	public SinPartida deleteSinPartida(SinPartida sinPartida) throws Exception {

		
		// Validaciones de negocio
		if (!sinPartida.validateDelete()) {
			return sinPartida;
		}
		
		BalDAOFactory.getSinPartidaDAO().delete(sinPartida);
		
		return sinPartida;
	}	

	// Administracion de AuxDeuMdf
	public AuxDeuMdf createAuxDeuMdf(AuxDeuMdf auxDeuMdf) throws Exception {
		
		// Validaciones de negocio
		if (!auxDeuMdf.validateCreate()) {
			return auxDeuMdf;
		}

		BalDAOFactory.getAuxDeuMdfDAO().update(auxDeuMdf);
		
		return auxDeuMdf;
	}	

	public AuxDeuMdf updateAuxDeuMdf(AuxDeuMdf auxDeuMdf) throws Exception {
		
		// Validaciones de negocio
		if (!auxDeuMdf.validateUpdate()) {
			return auxDeuMdf;
		}

		BalDAOFactory.getAuxDeuMdfDAO().update(auxDeuMdf);
		
		return auxDeuMdf;
	}	

	public AuxDeuMdf deleteAuxDeuMdf(AuxDeuMdf auxDeuMdf) throws Exception {
		
		// Validaciones de negocio
		if (!auxDeuMdf.validateDelete()) {
			return auxDeuMdf;
		}
				
		BalDAOFactory.getAuxDeuMdfDAO().delete(auxDeuMdf);
		
		return auxDeuMdf;
	}	
	
	// Administracion de AuxImpRec
	public AuxImpRec createAuxImpRec(AuxImpRec auxImpRec) throws Exception {

		// Validaciones de negocio
		if (!auxImpRec.validateCreate()) {
			return auxImpRec;
		}

		BalDAOFactory.getAuxImpRecDAO().update(auxImpRec);

		return auxImpRec;
	}
	
	public AuxImpRec updateAuxImpRec(AuxImpRec auxImpRec) throws Exception {
		
		// Validaciones de negocio
		if (!auxImpRec.validateUpdate()) {
			return auxImpRec;
		}
		
		BalDAOFactory.getAuxImpRecDAO().update(auxImpRec);
		
	    return auxImpRec;
	}
	
	public AuxImpRec deleteAuxImpRec(AuxImpRec auxImpRec) throws Exception {

		
		// Validaciones de negocio
		if (!auxImpRec.validateDelete()) {
			return auxImpRec;
		}
		
		BalDAOFactory.getAuxImpRecDAO().delete(auxImpRec);
		
		return auxImpRec;
	}	
	
	
	// Metodos Relacionados con el Proceso de Asentamiento
	
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
		// Validar que al menos un Sistema asociado al Servicio Banco a procesar.
		if(ListUtil.isNullOrEmpty(Sistema.getListByServicioBanco(this.getServicioBanco()))){
			this.addRecoverableValueError("No existen Sistemas asociados al Servicio Banco: "+ this.getServicioBanco().getDesServicioBanco());
		}			
	}
	
	/**
	 * Obtiene las Transacciones del archivo de input. Las valida y las carga en bal_transaccion
	 * <i>(paso 1.2)</i>
	 */
	public void obtenerTransacciones(BufferedReader input) throws Exception{
		// Leer Transacciones del archivo de Input	
		Datum datum;
		String line; 
		Transaccion transaccion = null;
		long c = 0;
	    while (( line = input.readLine()) != null) {
	     		c++;
	     		if(c%250==0){
	      			SiatHibernateUtil.currentSession().getTransaction().commit();
	     			SiatHibernateUtil.currentSession().beginTransaction();
	     			AdpRun.changeRunMessage(c+" Transacciones procesadas.", 0);
	     		}
	    		datum = Datum.parse(line);	//Parseamos la linea de Transaccion
	     		if((datum.getColNumMax()==1 || datum.getColNumMax()==0) 
	     				&& (datum.getCols(0)==null || "".equals(datum.getCols(0).trim()))){
	     			continue;
	     		}
	    		if(datum.getColNumMax()<15){
	     			this.addRecoverableValueError("El registro de transaccion correspondiente a la linea "+c+" no tiene la cantidad de columnas necesarias");
	     			continue;
	     		}
	     		
	    		if(this.getServicioBanco().getCodServicioBanco().equals("81")){
	    			transaccion = new TransaccionTGI();
	    		}else if(this.getServicioBanco().getCodServicioBanco().equals("82")){
	    			transaccion = new TransaccionCDM();
	    		}else if(this.getServicioBanco().getCodServicioBanco().equals("83")){
	    			transaccion = new TransaccionDREI();
	    		}else if(this.getServicioBanco().getCodServicioBanco().equals("84")){
	    			transaccion = new TransaccionETUR();
	    		}else if(this.getServicioBanco().getCodServicioBanco().equals("85")){
	    			transaccion = new TransaccionOTRTRI();
	    		}else if(this.getServicioBanco().getCodServicioBanco().equals("89")){
	    			transaccion = new TransaccionDER();
	    		}else{
	    			transaccion = new Transaccion();	    			
	    		}
	    		
	     		transaccion.setAsentamiento(this);
	     		Long nroSistema = datum.getLong(0);
	     		if(nroSistema == 1)
	     			nroSistema = 2L;
	     		transaccion.setSistema(nroSistema);
	     		transaccion.setNroComprobante(datum.getLong(1));
	     		Date fechaPago = DateUtil.getDate(datum.getCols(7),DateUtil.ddMMYYYY_MASK);
	     		transaccion.setFechaPago(fechaPago);
	     		if(datum.getCols(2).length()<6){
	     			this.addRecoverableValueError("El registro de transaccion correspondiente a la linea "+c+" no tiene la cantidad de caracteres esperados en la 3er columna.");
	     			continue;
	     		}
	     		if(!this.getServicioBanco().getCodServicioBanco().equals("85")){
	     			if(Long.valueOf(datum.getCols(2).substring(0, 4))>1000 && Long.valueOf(datum.getCols(2).substring(0, 4))<7700){
	     				transaccion.setAnioComprobante(new Long(datum.getCols(2).substring(0, 4)));//new Long(DateUtil.getAnio(anioPeriodo)));
	     				transaccion.setPeriodo(new Long(datum.getCols(2).substring(4, 6)));//new Long(DateUtil.getMes(anioPeriodo)));
	     			}else{
	     				if(Long.valueOf(datum.getCols(2).substring(0, 4))<1000 && Long.valueOf(datum.getCols(2).substring(4, 6))==0){
	     					transaccion.setAnioComprobante(new Long(datum.getCols(2).substring(0, 4)));
	     					transaccion.setPeriodo(0L);	     				     				
	     				}else{
	     					transaccion.setAnioComprobante(new Long(datum.getCols(2).substring(0, 2)));
	     					transaccion.setPeriodo(new Long(datum.getCols(2).substring(4, 6)));
	     				}
	     			}	     			
	     		}
	     		transaccion.setResto(datum.getLong(3));
	     		transaccion.setCodPago(datum.getLong(4)); 
	     		transaccion.setCaja(datum.getLong(5)); 
	     		transaccion.setCodTr(datum.getLong(6)); 
	     		transaccion.setImporte(datum.getDouble(8)); 
	     		transaccion.setRecargo(datum.getDouble(9));
	     		transaccion.setPaquete(datum.getLong(11));
	     		transaccion.setMarcaTr(datum.getLong(12));
	     		transaccion.setReciboTr(datum.getLong(13));
	    		Date fechaBalance = DateUtil.getDate(datum.getCols(14),DateUtil.ddMMYYYY_MASK);
	    		transaccion.setFechaBalance(fechaBalance);
	     		transaccion.setEsIndet(0);
	     		transaccion.setNroLinea(c);
	     		
	     		// busca el sistema y servicioBanco en el cache
	     		Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getServicioBanco().getId(), 
	     				                                                                       transaccion.getSistema());
	     		if (sistema==null){
	     			this.addRecoverableValueError("El registro de transaccion correspondiente a la linea "+c+" tiene un sistema que no se corresponde a los asociados al asentamiento.");
	     			continue;
	     		}
	     		// Calculamos tipo de boleta para Gravamenes Especiales
	     		if(this.getServicioBanco().getCodServicioBanco().equals("85")){
	     			// Si es una transaccion SIAT
	     			if(sistema.getEsServicioBanco().intValue() == SiNo.SI.getId().intValue()){
     					transaccion.setAnioComprobante(new Long(datum.getCols(2).substring(0, 4)));
     					transaccion.setPeriodo(0L);	     				     					     				
	     			// Si es una transaccion NO SIAT
	     			}else{	     			
	     				// Si es Deuda (Recibo de Deuda)
	     				if(sistema.getTipoDeuda().getId().longValue() == TipoDeuda.ID_DEUDA_PURA.longValue()){
	     					transaccion.setAnioComprobante(new Long(datum.getCols(2).substring(2, 6)));
		     				transaccion.setPeriodo(0L);		
	     				// Si es Plan de Pagos (Cuota o Recibo de Cuota)
	     				}else if(sistema.getTipoDeuda().getId().longValue() == TipoDeuda.ID_PLAN_DE_PAGOS.longValue()){
	     					// Si en el campo clave el nro es mayor a 1000 consideramos que es un anio y la transaccion es reciboConvenio 
	     					if(Long.valueOf(datum.getCols(2)) > 1000){
	     						transaccion.setAnioComprobante(new Long(datum.getCols(2)));
	     						transaccion.setPeriodo(0L);				     				
	     					// Si en el campo clave el nro es menor a 1000 consideramos que es una cuota y la transaccion es de cuota	     						
	     					}else{
	     						transaccion.setAnioComprobante(0L);
	     						transaccion.setPeriodo(new Long(datum.getCols(2)));
	     					}
	     				}
	     			}
	     		}
     			// Caso Especial para Recibo de Convenio de Servicio Banco DREI de transacciones NO SIAT
     			if(this.getServicioBanco().getCodServicioBanco().equals("83") && sistema.getEsServicioBanco().intValue() == SiNo.NO.getId().intValue()){
     				if(sistema.getTipoDeuda().getId().longValue() == TipoDeuda.ID_PLAN_DE_PAGOS.longValue() && Long.valueOf(datum.getCols(2)) < 7700){
     					//  Si en el campo clave el nro es mayor a 1000 consideramos que es un anio y la transaccion es reciboConvenio 
     					if(Long.valueOf(datum.getCols(2)) > 1000){
     						transaccion.setAnioComprobante(new Long(datum.getCols(2)));
     						transaccion.setPeriodo(0L);				     				
     					// Si en el campo clave el nro es menor a 1000 consideramos que es una cuota y la transaccion es de cuota	     						
     					}else{
     						transaccion.setAnioComprobante(0L);
     						transaccion.setPeriodo(new Long(datum.getCols(2)));
     					}
     				}
     			}

	     		// Para el Caso de Transacciones CdM con Sistema que no es Servicio Banco
     			// se considera que pueden venir en el campo 'anioPeriodo' un numero de cuota mayor que 100, 
     			// correspondiente a deuda CdM, y se debe cargar en el periodo de la transaccion.
	     		// Por lo tanto se corrigen los datos tomados anteriormente.
	     		// Ej: 000100, 000800 (pago contado), 000104, etc
     			if(this.getServicioBanco().getCodServicioBanco().equals("82") && sistema.getEsServicioBanco().intValue()==SiNo.NO.getId().intValue()){
     				if(Long.valueOf(datum.getCols(2).substring(0, 4))<1000 && Long.valueOf(datum.getCols(2).substring(0, 4))>0){
     					transaccion.setAnioComprobante(0L);
     					transaccion.setPeriodo(new Long(datum.getCols(2).substring(3, 6)));
     				}    					
     			}
	     		
	     		if(!DateUtil.isDateEqual(transaccion.getFechaBalance(), this.getFechaBalance())){
	     			this.addRecoverableValueError("El registro de transaccion correspondiente a la linea "+c+" tiene fecha de balance distinta a la del asentamiento.");
	     		}
	     		// Averiguamos el Tipo Boleta y se lo seteamos a la Transaccion.
	     		Long tipoBoleta = -1L;
	     		if(sistema.getEsServicioBanco().intValue() == SiNo.SI.getId().intValue()){
	    			tipoBoleta = transaccion.getAnioComprobante();
	    		}else {
		     			if(sistema.getTipoDeuda().getId().longValue() == TipoDeuda.ID_DEUDA_PURA){
		     				if(!this.getServicioBanco().getCodServicioBanco().equals("85")){
		     					if(transaccion.getAnioComprobante()!= 99L || transaccion.getPeriodo() != 99L)
		     						tipoBoleta = Transaccion.TIPO_BOLETA_DEUDA;
		     					if(transaccion.getAnioComprobante()== 99L && transaccion.getPeriodo() == 99L)
		     						tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_DEUDA;
		     					if(transaccion.getAnioComprobante()== 77L && transaccion.getPeriodo() == 77L)
		     						tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_DEUDA;
		     					if(transaccion.getAnioComprobante()== 88L && transaccion.getPeriodo() == 88L)
		     						tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_DEUDA;
		     				}else{
		     					tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_DEUDA;
				     		}			
		     			}
		     			if(sistema.getTipoDeuda().getId().longValue() == TipoDeuda.ID_PLAN_DE_PAGOS){
		     				if(!this.getServicioBanco().getCodServicioBanco().equals("85")){
		     					if(transaccion.getAnioComprobante()!= 99L || transaccion.getPeriodo() != 99L)
		     						tipoBoleta = Transaccion.TIPO_BOLETA_CUOTA;
		     					if(transaccion.getAnioComprobante()== 99L && transaccion.getPeriodo() == 99L)
		     						tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_CUOTA;
		     					if(transaccion.getAnioComprobante()== 77L && transaccion.getPeriodo() == 77L)
		     						tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_CUOTA;
		     					if(transaccion.getAnioComprobante()== 88L && transaccion.getPeriodo() == 88L)
		     						tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_CUOTA;		
		     					// Caso Especial para Recibo de Convenio de Servicio Banco DREI 
		     	     			if(this.getServicioBanco().getCodServicioBanco().equals("83")){
	     	     					//  Si el anio es mayor a 1000 es tipo boleta Recibo Cuota 
	     	     					if(transaccion.getAnioComprobante() > 1000 && transaccion.getPeriodo() == 0L ){
			     						tipoBoleta = Transaccion.TIPO_BOLETA_RECIBO_CUOTA;
	     	     					}
		     	     			}
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
	     		if(SiNo.SI.getId().equals(this.getServicioBanco().getEsAutoliquidable()) 
	     								&& Transaccion.TIPO_BOLETA_DEUDA.equals(tipoBoleta)){
	     			if(transaccion.getResto().longValue() > 0L && transaccion.getResto().longValue() < 10L){
	     				transaccion.setEsRectificativa(SiNo.SI.getId());
	     			}else{
	     				transaccion.setEsRectificativa(SiNo.NO.getId());
	     			}	     			
	     		}
	     		
	     		// Creamos el registro de transaccion
	     		this.createTransaccion(transaccion); 
	     		if(transaccion.hasError()){
	     			for(DemodaStringMsg error:transaccion.getListError()){
						this.addRecoverableError(error);
					}     			
	     		}
	    }
		
	}
	
	/**
	 * <b>Genera Formularios para control del paso 1:</b> 
	 * <p>- Totales por Sistema(5018): archivo pdf</p>
	 * <p>- Detalle de Transacciones: planilla de calculo</p>
	 * <p>- Archivo de Transacciones: txt (solo cuando no esta asociado a un Balance)</p>
	 * <i>(paso 1.3)</i>
	 */
	public void generarFormulariosPaso1(String outputDir) throws Exception{
		
		//-> Totales por Sistema/ServicioBanco (5018) (PDF)
		String fileNamePdf = this.generarPdfTotalesPorSistema(outputDir);
		String nombre = "Totales por Sistema (5018)";
		String descripcion = "Permite consultar los totales de transacciones e importes por cada Sistema asociado al Servicio Banco del Asentamiento.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileNamePdf);
		
		//-> Detalle de Transacciones (Planilla de calculo)
		List<String> listFileName = this.exportReportesTransacciones(outputDir);
		int c = 0;
		nombre = "Detalle de Transacciones";
		for(String fileName: listFileName){
			descripcion = "Planilla con Transacciones asociadas al Asentamiento. Hoja "+c;
			this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
			c++;
		}
		// Si el asentamiento no se encuentra asociado a un Proceso de Balance, se copia el archivo de transacciones.
		if(this.getBalance() == null){
			if(AdpRun.currentRun().copyInputFileSufix(AdpRunDirEnum.ENTRADA, AdpRunDirEnum.SALIDA,"."+this.getId().toString())){
				String fileName = AdpRun.currentRun().getInputFilename()+"."+this.getId().toString();
				nombre = "Archivo de entrada";
				descripcion = "Lista de transacciones que se procesa en el asentamiento. Conserva el formato con el que ingresa al proceso.";
				this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);			
			}
		}
		// Si el asentamiento es para el Servicio Banco correspondiente a Sellado generar Reporte Adicional.
		if("88".equals(this.getServicioBanco().getCodServicioBanco())){
			//-> Totales por CodSellado y por Caja (PDF)
			fileNamePdf = this.generarPdfTotalesSellado(outputDir);
			nombre = "Totales por CodSellado y por Caja";
			descripcion = "Permite consultar los totales de transacciones e importes agrupados por Código de Sellado, o agrupados por Caja.";
			this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileNamePdf);			
		}
		
	}
	
	/**
	 * Genera el Reporte pdf "Totales por Sistema (5018)" resultado del paso 1 del proceso de Asentamiento.
	 * 
	 * @param outputDir
	 * @return fileName
	 */
	public String generarPdfTotalesPorSistema(String fileDir) throws Exception{
		
		//	Encabezado:
		String fechaAsentamiento = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO)
			ejercicio += " - Asentamiento Común";
		else if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_CERRADO){
			ejercicio += " - Asentamiento Especial";
		}
		String servicioBanco = this.getServicioBanco().getCodServicioBanco() + " - " + this.getServicioBanco().getDesServicioBanco();
		String estado = this.getCorrida().getEstadoCorrida().getDesEstadoCorrida();
		List<Object[]> listResult = BalDAOFactory.getTransaccionDAO().getListForReportByAsentamiento(this);
		Double importeTotal = 0D;
		Long cantTotal = 0L;

		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Asentamiento.COD_FRM_PASO1_1);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Reporte de Asentamiento de Pagos - Totales por Sistema");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Reporte de Asentamiento de Pagos - Totales por Sistema");
		printModel.putCabecera("FechaAsentamiento", fechaAsentamiento);
		printModel.putCabecera("Ejercicio", ejercicio);
		printModel.putCabecera("ServicioBanco", servicioBanco);
		printModel.putCabecera("Estado", estado);
				
		// Se arma la tabla en una estructura de objectos. (TablaVO, FilaVO, CeldaVO)
		TablaVO tabla = new TablaVO("TotalesPorSistema");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Sistema","sistema"));
		filaCabecera.add(new CeldaVO("Cantidad Transacciones","cantTransaccion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		FilaVO fila = new FilaVO();
		for(Object[] arrayResult: listResult){
			fila.add(new CeldaVO((String) arrayResult[0],"sistema"));
			fila.add(new CeldaVO(((Long) arrayResult[1]).toString(),"cantTransaccion"));
			fila.add(new CeldaVO(StringUtil.formatDouble((NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB))),"importe"));
			tabla.add(fila);
			fila = new FilaVO();
			importeTotal += (Double) arrayResult[2];
			cantTotal += (Long) arrayResult[1];
		}
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total","total"));
		filaPie.add(new CeldaVO(cantTotal.toString(),"totalTransaccion"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);
		
		printModel.setData(tabla);
		printModel.setTopeProfundidad(3);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"ReporteTotales5018.pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		return fileName;
	}

	/**
	 * Genera el Reporte pdf "Totales por Codigo de Sellado y por Caja" resultado del paso 1 del proceso de Asentamiento
	 * cuando el Servicio Banco corresponde a Sellado.
	 * 
	 * @param outputDir
	 * @return fileName
	 */
	public String generarPdfTotalesSellado(String fileDir) throws Exception{
		
		//	Encabezado:
		String fechaAsentamiento = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO)
			ejercicio += " - Asentamiento Común";
		else if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_CERRADO){
			ejercicio += " - Asentamiento Especial";
		}
		String servicioBanco = this.getServicioBanco().getCodServicioBanco() + " - " + this.getServicioBanco().getDesServicioBanco();
		String estado = this.getCorrida().getEstadoCorrida().getDesEstadoCorrida();
		
		List<Object[]> listResultSellado = BalDAOFactory.getTransaccionDAO().getListPorSelladoForReportByAsentamiento(this);
		List<Object[]> listResultCaja = BalDAOFactory.getTransaccionDAO().getListPorCajaForReportByAsentamiento(this);
		
		Double importeTotal = 0D;
		Long cantTotal = 0L;

		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Asentamiento.COD_FRM_PASO1_SE);

		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Reporte de Asentamiento de Pagos - Totales de Sellados");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Reporte de Asentamiento de Pagos - Totales de Sellados");
		printModel.putCabecera("FechaAsentamiento", fechaAsentamiento);
		printModel.putCabecera("Ejercicio", ejercicio);
		printModel.putCabecera("ServicioBanco", servicioBanco);
		printModel.putCabecera("Estado", estado);

		// Se arma un contenedor de tablas para guardas los datos.
		ContenedorVO contenedor = new ContenedorVO("Sellados");

		// Se arma la tabla en una estructura de objectos. (TablaVO, FilaVO, CeldaVO)
		TablaVO tabla = new TablaVO("TotalesPorSellado");
		tabla.setTitulo("Detalle por Sellado");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Sellado","sellado"));
		filaCabecera.add(new CeldaVO("Cantidad Transacciones","cantTransaccion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		FilaVO fila = new FilaVO();
		for(Object[] arrayResult: listResultSellado){
			fila.add(new CeldaVO(((Long) arrayResult[0]).toString(),"sellado"));
			fila.add(new CeldaVO(((Long) arrayResult[1]).toString(),"cantTransaccion"));
			fila.add(new CeldaVO((StringUtil.formatDouble(NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB))),"importe"));
			tabla.add(fila);
			fila = new FilaVO();
			importeTotal += (Double) arrayResult[2];
			cantTotal += (Long) arrayResult[1];
		}
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total","total"));
		filaPie.add(new CeldaVO(cantTotal.toString(),"totalTransaccion"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);
		
		// Se arma la tabla en una estructura de objectos. (TablaVO, FilaVO, CeldaVO)
		tabla = new TablaVO("TotalesPorCaja");
		tabla.setTitulo("Detalle por Caja");
		filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Caja","caja"));
		filaCabecera.add(new CeldaVO("Cantidad Transacciones","cantTransaccion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		fila = new FilaVO();
		importeTotal = 0D;
		cantTotal = 0L;
		for(Object[] arrayResult: listResultCaja){
			fila.add(new CeldaVO(((Long) arrayResult[0]).toString(),"caja"));
			fila.add(new CeldaVO(((Long) arrayResult[1]).toString(),"cantTransaccion"));
			fila.add(new CeldaVO((StringUtil.formatDouble(NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB))),"importe"));
			tabla.add(fila);
			fila = new FilaVO();
			importeTotal += (Double) arrayResult[2];
			cantTotal += (Long) arrayResult[1];
		}
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total","total"));
		filaPie.add(new CeldaVO(cantTotal.toString(),"totalTransaccion"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);
		contenedor.add(tabla);
		
		printModel.setData(contenedor);
		printModel.setTopeProfundidad(5);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"ReporteTotalesSellado.pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		return fileName;
	}

	
	/**
	 * Obtiene la lista de Transacciones para el Asentamiento de manera paginada
	 * @param  firstResult
	 * @param  maxResults
	 * @return List<Transaccion>
	 */
	public List<Transaccion> getListTransaccion(Integer firstResult, Integer maxResults){
		return BalDAOFactory.getTransaccionDAO().getListByAsentamiento(this, firstResult, maxResults); 
	}
	
	/**
	 * Obtiene la lista de Transacciones no procesadas por el paso 3 para el Asentamiento de manera paginada
	 * @param  firstResult
	 * @param  maxResults
	 * @return List<Transaccion>
	 */
	public List<Transaccion> getListTransaccionNoAsentadas(Integer firstResult, Integer maxResults){
		return BalDAOFactory.getTransaccionDAO().getListNoAsentadasByAsentamiento(this, firstResult, maxResults); 
	}
	
	/**
	 * Obtiene la lista de AuxPagDeu para el Asentamiento de manera paginada
	 * @param  firstResult
	 * @param  maxResults
	 * @return List<AuxPagDeu>
	 */
	public List<AuxPagDeu> getListAuxPagDeu(Integer firstResult, Integer maxResults){
		return BalDAOFactory.getAuxPagDeuDAO().getListByAsentamiento(this, firstResult, maxResults); 
	}
	
	/**
	 * Obtiene la lista de AuxPagCuo para el Asentamiento de manera paginada
	 * @param  firstResult
	 * @param  maxResults
	 * @return List<AuxPagCuo>
	 */
	public List<AuxPagCuo> getListAuxPagCuo(Integer firstResult, Integer maxResults){
		return BalDAOFactory.getAuxPagCuoDAO().getListByAsentamiento(this, firstResult, maxResults); 
	}
	
	/**
	 * Obtiene la lista de AuxConDeu para el Asentamiento de manera paginada
	 * @param  firstResult
	 * @param  maxResults
	 * @return List<AuxConDeu>
	 */
	public List<AuxConDeu> getListAuxConDeu(Integer firstResult, Integer maxResults){
		return BalDAOFactory.getAuxConDeuDAO().getListByAsentamiento(this, firstResult, maxResults); 
	}
	
	/**
	 * Obtiene la lista de AuxConDeuCuo para el Asentamiento de manera paginada
	 * @param  firstResult
	 * @param  maxResults
	 * @return List<AuxConDeuCuo>
	 */
	public List<AuxConDeuCuo> getListAuxConDeuCuo(Integer firstResult, Integer maxResults){
		return BalDAOFactory.getAuxConDeuCuoDAO().getListByAsentamiento(this, firstResult, maxResults); 
	}
	
	/**
	 * Obtener y Procesar Transacciones SIAT. Con Paginacion de las Transacciones. 
	 * Por cada transaccion se obtiene la transaccion SIAT, y procesa la deuda, recibo deuda, cuota o recibo cuota
	 * segun corresponda.
	 * 	
	 * <i>(paso 2.1)</i>
	 */
	public void procesarTransaccionSiatPaginado() throws Exception{
		Integer firstResult = 0;
		Integer maxResults = 100;
		// Iterar la lista de Transacciones del Asentamiento de manera paginada
		boolean contieneTransacciones = true;
		long cantTxDec = 0; 
		while (contieneTransacciones){// && !this.hasError()){
			
			// Obtiene la lista de Transacciones  
			long queryTime = System.currentTimeMillis();
			List<Transaccion> listTransaccionPag = this.getListTransaccion(firstResult, maxResults);		
			queryTime = System.currentTimeMillis() - queryTime;
			AsentamientoCache.getInstance().getSession(this.getId()).addStats("getListTransaccion paginada de a "+maxResults+" transacciones.", queryTime);
			
			
			contieneTransacciones = (listTransaccionPag.size() > 0);

			if(contieneTransacciones){
				this.procesarTransaccionSiat(listTransaccionPag);
				firstResult += maxResults; // Incremento el indice del 1er registro
			}
			cantTxDec++;
			listTransaccionPag = null;
			// Por razones de rendimiento, especificamente para mejorar los tiempos de procesamiento, se encontró
			// que además de la paginación y de realizar un Commit de la transacción cada un cierto numero de transacciones,
			// fue necesario cerrar la session de hibernate y volver a abrirla. Con esto se evita que el procesamiento
			// incremente sus tiempos a medida que aumentan las transacciones procesadas. Se probó realizando un clean 
			// de la session y reiniciando solo la transacción, pero de esta manera no se lograba solucionar este problema.
			if((cantTxDec)%1==0){
				SiatHibernateUtil.currentSession().getTransaction().commit();
				SiatHibernateUtil.closeSession();
				SiatHibernateUtil.currentSession();
				SiatHibernateUtil.currentSession().beginTransaction();
				AdpRun.changeRunMessage(firstResult+" Transacciones procesadas.", 0);
			}
		}
		
		// Grabar los auxRecaudados:
		this.saveMapAuxRecaudado();
		// Grabar los auxSellados:
		this.saveMapAuxSellado();
		// Grabar los auxConvenios:
		this.saveMapAuxConvenio();
		// Grabar los auxConDeu
		this.saveMapAuxConDeu();

		
		// Finalizamos la transaccion y cerramos la session
		Session session = SiatHibernateUtil.currentSession();
		Transaction tx = session.getTransaction();
		if(tx != null){
			if (this.hasError()) {
				tx.rollback();
				return;
			}
			tx.commit();		
		}
		SiatHibernateUtil.closeSession();
	}

	
	/**
	 * Obtener y Procesar Transacciones SIAT.
	 * Por cada transaccion se obtiene la transaccion SIAT, y procesa la deuda, recibo deuda, cuota o recibo cuota
	 * segun corresponda.
	 * 	
	 * <i>(paso 2.1)</i>
	 */
	public void procesarTransaccionSiat(List<Transaccion> listTransaccionPag) throws Exception{
				
		if(ListUtil.isNullOrEmpty(listTransaccionPag)){
			this.addRecoverableValueError("No existen Transacciones para este proceso de asentamiento: ");
			return;
		}
		
		for(Transaccion transaccion: listTransaccionPag){
			long txTime = System.currentTimeMillis();
			
			String desTx = "";
			try{
				// Obtener el sistema asociado
				Sistema sistema = AsentamientoCache.getInstance().getSistemaBySerBanSistema (this.getServicioBanco().getId(), 
		                 transaccion.getSistema());
				if(sistema == null){
					this.addRecoverableValueError("Error!. Transaccion de Id: "+transaccion.getId()+", Linea Nro: "+transaccion.getNroLinea()+". No se encontro el sistema de la transaccion. Sistema: "+transaccion.getSistema());
					continue;
				}
				
				if(AsentamientoCache.getInstance().getSession(this.getId()).isForzado() 
						&& transaccion.getConError() != null
						&& transaccion.getConError().intValue() == SiNo.SI.getId().intValue()){
					
					if(transaccion.esDeuda())
						transaccion.registrarIndeterminado("Transaccion con Error.",(Deuda) null, null, "52");
					else if(transaccion.esReciboDeuda())
						transaccion.registrarIndeterminado("Transaccion con Error.",(Recibo) null, "52");
					else if(transaccion.esCuota())
						transaccion.registrarIndeterminado("Transaccion con Error.",(ConvenioCuota) null, null, "52");
					else if(transaccion.esReciboCuota())
						transaccion.registrarIndeterminado("Transaccion con Error.", (ReciboConvenio) null, "52");
					else if(transaccion.esSellado())
						transaccion.registrarIndeterminado("Transaccion con Error.", "52");
					if (transaccion.hasError()) {
						String descripcion = transaccion.getListError().get(0).key().substring(1);
						this.addRecoverableValueError(descripcion);
					}
					continue;
				}
				
				if(transaccion.esDeuda()){
					Deuda deuda = null;
					if(transaccion.esNueva()){
						desTx = "Deuda Codigo de Referencia de Pago: "+transaccion.getNroComprobante().toString(); 
						if(this.getServicioBanco().getEsAutoliquidable() == null || SiNo.NO.getId().intValue() == this.getServicioBanco().getEsAutoliquidable().intValue()){
							deuda = Deuda.getByCodRefPag(transaccion.getNroComprobante());
							if(null == deuda){
								transaccion.registrarIndeterminado("No existe la transaccion en SIAT: Deuda Codigo de Referencia de Pago: "+transaccion.getNroComprobante().toString()+".",(Deuda) null, null, "1");
								if (transaccion.hasError()) {
									String descripcion = transaccion.getListError().get(0).key().substring(1);
									this.addRecoverableValueError(descripcion);
									continue;
								}								
								continue;
							}
						}else{
							deuda = this.obtenerDeudaForAutoLiquidable((TransaccionAutoLiq)transaccion);

							//Los pagos se indeterminan dentro del obtener (por eso el continue)
							if(null == deuda) continue;
						}
					}else{
						desTx = "Deuda: "+transaccion.getNroComprobante()+" - "+transaccion.getPeriodo()+"/"+transaccion.getAnioComprobante();
						long getTime = System.currentTimeMillis();
						if(this.getServicioBanco().getEsAutoliquidable() == null || SiNo.NO.getId().intValue() == this.getServicioBanco().getEsAutoliquidable().intValue()){
							deuda = Deuda.getByCtaPerAnioSisRes(transaccion.getNroComprobante(),transaccion.getPeriodo(),transaccion.getAnioComprobante(),transaccion.getSistema(),transaccion.getResto());							
						}else{
							deuda = this.obtenerDeudaForAutoLiquidable((TransaccionAutoLiq)transaccion);

							//Los pagos se indeterminan dentro del obtener, por eso el continue
							if(null == deuda) continue;
						}
						getTime = System.currentTimeMillis() - getTime;
						AsentamientoCache.getInstance().getSession(this.getId()).addStats("Deuda.getByCtaPerAnioSisRes", getTime);
				
						if(deuda == null){
							if(this.getServicioBanco().getTipoAsentamiento().longValue() == ServicioBanco.TIPO_ASENTAMIENTO_MIXTO.longValue()){
								transaccion.delegar();
								if (transaccion.hasError()) {
									String descripcion = transaccion.getListError().get(0).key().substring(1);
									this.addRecoverableValueError(descripcion);
									continue;
								}	
								continue;
							}else{
								// Unicamente para Servicio Banco TGI, si no se encontró la deuda, detectada de tipo 'No Nueva', se busca de nuevo cambiando el sistema de la transaccion 
								// por el sistema 81. Esto lo hacemos porque por el momento se está enviando deuda 'Nueva' con la clave de deuda 'No Nueva' y sistema 02 para las transacciones
								// de pago electrónico (débito, etc)
								if(ServicioBanco.COD_TGI.equals(this.getServicioBanco().getCodServicioBanco())){
									Sistema sisSerBan = sistema.getSistemaEsServicioBanco(); // Busca el sistema que es Servicio Banco. (En este caso debe ser el sistema 81)
									deuda = Deuda.getByCtaPerAnioSisRes(transaccion.getNroComprobante(),transaccion.getPeriodo(),transaccion.getAnioComprobante(),sisSerBan.getNroSistema(),transaccion.getResto());									
								}
								if(deuda == null){
									transaccion.registrarIndeterminado("No existe la transaccion en SIAT: Deuda: "+transaccion.getNroComprobante()+" - "+transaccion.getPeriodo()+"/"+transaccion.getAnioComprobante(),(Deuda) null, null, "2");
									if (transaccion.hasError()) {
										String descripcion = transaccion.getListError().get(0).key().substring(1);
										this.addRecoverableValueError(descripcion);
										continue;
									}	
									continue;									
								}
							}							
						}
					}
				
					this.logDetallado("Transaccion Nro "+transaccion.getNroLinea()+", "+desTx+" encontrada.");
					transaccion.procesarDeuda(deuda);
					if(transaccion.hasError()){
						transaccion.addErrorMessages(this);
						transaccion.setConError(SiNo.SI.getId());
						this.updateTransaccion(transaccion);
						continue;
					}
					txTime = System.currentTimeMillis() - txTime;
					AsentamientoCache.getInstance().getSession(this.getId()).addStats("Transaccion", txTime);
					AsentamientoCache.getInstance().getSession(this.getId()).addStats("Transaccion Deuda", txTime);
					//this.logStats("<*> Tiempo consumido al procesar transaccion de Deuda, nro "+transaccion.getNroLinea()+": "+txTime+" ms <*>");
					continue;
				}
				
				if(transaccion.esReciboDeuda()){
					Recibo recibo = null;
					if(transaccion.esNueva()){
						desTx = "Recibo de Deuda Codigo de Referencia de Pago: "+transaccion.getNroComprobante().toString();
						recibo = Recibo.getByCodRefPag(transaccion.getNroComprobante()); 
						if(recibo == null){
							transaccion.registrarIndeterminado("No existe la transaccion en SIAT: Recibo de Deuda Codigo de Referencia de Pago: "+transaccion.getNroComprobante().toString()+".",(Recibo) null, "3");
							if (transaccion.hasError()) {
								String descripcion = transaccion.getListError().get(0).key().substring(1);
								this.addRecoverableValueError(descripcion);
								continue;
							}
							continue;
						}
					}else{
						desTx = "Recibo de Deuda: "+transaccion.getNroComprobante()+" - "+transaccion.getAnioComprobante();
						recibo = Recibo.getByNumero(transaccion.getNroComprobante());
						if(recibo == null){
							if(this.getServicioBanco().getTipoAsentamiento().longValue() == ServicioBanco.TIPO_ASENTAMIENTO_MIXTO.longValue()){
								transaccion.delegar();
								if (transaccion.hasError()) {
									String descripcion = transaccion.getListError().get(0).key().substring(1);
									this.addRecoverableValueError(descripcion);
									continue;
								}	
							}else{
								transaccion.registrarIndeterminado("No existe la transaccion en SIAT: Recibo de Deuda: "+transaccion.getNroComprobante()+" - "+transaccion.getAnioComprobante(),(Recibo) null, "4");
								if (transaccion.hasError()) {
									String descripcion = transaccion.getListError().get(0).key().substring(1);
									this.addRecoverableValueError(descripcion);
									continue;
								}								
							}
							continue;
						}
					}
					this.logDetallado("Transaccion Nro "+transaccion.getNroLinea()+", "+desTx+" encontrado.");
					transaccion.procesarReciboDeuda(recibo);
					if(transaccion.hasError()){
						for(DemodaStringMsg error:transaccion.getListError()){
							this.addRecoverableError(error);
						}
						transaccion.setConError(SiNo.SI.getId());
						this.updateTransaccion(transaccion);
						continue;
					}
					txTime = System.currentTimeMillis() - txTime;
					AsentamientoCache.getInstance().getSession(this.getId()).addStats("Transaccion", txTime);
					AsentamientoCache.getInstance().getSession(this.getId()).addStats("Transaccion Recibo de Deuda", txTime);
					//this.logStats("<*> Tiempo consumido al procesar transaccion de Recibo de Deuda, nro "+transaccion.getNroLinea()+": "+txTime+" ms <*>");
					continue;
				}

				if(transaccion.esCuota()){
					ConvenioCuota  convenioCuota = null;
					if(transaccion.esNueva()){
						desTx = "Cuota Codigo de Referencia de Pago: "+transaccion.getNroComprobante().toString();
						convenioCuota = ConvenioCuota.getByCodRefPag(transaccion.getNroComprobante());
						if(convenioCuota == null){
							transaccion.registrarIndeterminado("No existe la transaccion en SIAT: Cuota Codigo de Referencia de Pago: "+transaccion.getNroComprobante().toString()+".",(ConvenioCuota) null, null, "5");
							if (transaccion.hasError()) {
								String descripcion = transaccion.getListError().get(0).key().substring(1);
								this.addRecoverableValueError(descripcion);
								continue;
							}
							continue;
						}
					}else{
						desTx = "Cuota: "+transaccion.getNroComprobante()+" - "+transaccion.getPeriodo();
						long getTime = System.currentTimeMillis();
						convenioCuota = ConvenioCuota.getByNroCuoNroConSis(transaccion.getPeriodo(), transaccion.getNroComprobante(), transaccion.getSistema());				
						getTime = System.currentTimeMillis() - getTime;
						AsentamientoCache.getInstance().getSession(this.getId()).addStats("ConvenioCuota.getByNroCuoNroConSis", getTime);
				
						if(convenioCuota == null){
							if(this.getServicioBanco().getTipoAsentamiento().longValue() == ServicioBanco.TIPO_ASENTAMIENTO_MIXTO.longValue()){
								transaccion.delegar();
								if (transaccion.hasError()) {
									String descripcion = transaccion.getListError().get(0).key().substring(1);
									this.addRecoverableValueError(descripcion);
									continue;
								}	
							}else{
								transaccion.registrarIndeterminado("No existe la transaccion en SIAT: Cuota: "+transaccion.getNroComprobante()+" - "+transaccion.getPeriodo(), convenioCuota, null, "6");
								if (transaccion.hasError()) {
									String descripcion = transaccion.getListError().get(0).key().substring(1);
									this.addRecoverableValueError(descripcion);
									continue;
								}								
							}
							continue;
						}
					}
					this.logDetallado("Transaccion Nro "+transaccion.getNroLinea()+", "+desTx+" encontrada.");
					transaccion.procesarCuota(convenioCuota);
					if(transaccion.hasError()){
						transaccion.addErrorMessages(this);
						transaccion.setConError(SiNo.SI.getId());
						this.updateTransaccion(transaccion);
						continue;
					}
					// Se guarda el id del Plan en la transaccion para generar detalle por plan en los reportes
					transaccion.setIdPlan(convenioCuota.getConvenio().getPlan().getId());
					this.updateTransaccion(transaccion);
					txTime = System.currentTimeMillis() - txTime;
					AsentamientoCache.getInstance().getSession(this.getId()).addStats("Transaccion", txTime);
					AsentamientoCache.getInstance().getSession(this.getId()).addStats("Transaccion Cuota", txTime);
					//this.logStats("<*> Tiempo consumido al procesar transaccion de Cuota, nro "+transaccion.getNroLinea()+": "+txTime+" ms <*>");
					continue;
				}
				
				if(transaccion.esReciboCuota()){
					ReciboConvenio reciboConvenio = null;
					if(transaccion.esNueva()){
						desTx = "Recibo Cuota Codigo de Referencia de Pago: "+transaccion.getNroComprobante().toString();
						reciboConvenio = ReciboConvenio.getByCodRefPag(transaccion.getNroComprobante());
						if(reciboConvenio == null){
							transaccion.registrarIndeterminado("No existe la transaccion en SIAT: Recibo Cuota Codigo de Referencia de Pago: "+transaccion.getNroComprobante().toString()+".", reciboConvenio, "7");
							if (transaccion.hasError()) {
								String descripcion = transaccion.getListError().get(0).key().substring(1);
								this.addRecoverableValueError(descripcion);
								continue;
							}
							continue;
						}
						 	
					}else{
						desTx = "Recibo de Cuota: "+transaccion.getNroComprobante()+"/"+transaccion.getAnioComprobante();
						// Si el anio es mayor a 1000 y el periodo es 0, se trata de una transaccion de la forma del sistema de gravamenes (serBan 85 o 83)
						if(("83".equals(this.getServicioBanco().getCodServicioBanco()) || "85".equals(this.getServicioBanco().getCodServicioBanco())) 
																		&& (transaccion.getAnioComprobante() > 1000 && transaccion.getPeriodo() == 0)){
							reciboConvenio = ReciboConvenio.getByNroYAnioYSerBan(transaccion.getNroComprobante(), transaccion.getAnioComprobante(), this.getServicioBanco().getId());	
						}else{
							reciboConvenio = ReciboConvenio.getByNumeroYSerBan(transaccion.getNroComprobante(), this.getServicioBanco().getId());							
						}
						if(reciboConvenio == null){
							if(this.getServicioBanco().getTipoAsentamiento().longValue() == ServicioBanco.TIPO_ASENTAMIENTO_MIXTO.longValue()){
								transaccion.delegar();
								if (transaccion.hasError()) {
									String descripcion = transaccion.getListError().get(0).key().substring(1);
									this.addRecoverableValueError(descripcion);
									continue;
								}	
							}else{
								transaccion.registrarIndeterminado("No existe la transaccion en SIAT: Recibo de Cuota: "+transaccion.getNroComprobante()+"/"+transaccion.getAnioComprobante(), reciboConvenio, "8");
								if (transaccion.hasError()) {
									String descripcion = transaccion.getListError().get(0).key().substring(1);
									this.addRecoverableValueError(descripcion);
									continue;
								}								
							}
							continue;
						}
					}
					this.logDetallado("Transaccion Nro "+transaccion.getNroLinea()+", "+desTx+" encontrado.");
					transaccion.procesarReciboCuota(reciboConvenio);
					if(transaccion.hasError()){
						transaccion.addErrorMessages(this);
						transaccion.setConError(SiNo.SI.getId());
						this.updateTransaccion(transaccion);
					}
					// Se guarda el id del Plan en la transaccion para generar detalle por plan en los reportes
					transaccion.setIdPlan(reciboConvenio.getConvenio().getPlan().getId());
					this.updateTransaccion(transaccion);
					txTime = System.currentTimeMillis() - txTime;
					AsentamientoCache.getInstance().getSession(this.getId()).addStats("Transaccion", txTime);
					AsentamientoCache.getInstance().getSession(this.getId()).addStats("Transaccion Recibo de Cuota", txTime);
					//this.logStats("<*> Tiempo consumido al procesar transaccion recibo de cuota, nro "+transaccion.getNroLinea()+": "+txTime+" ms <*>");
					continue;
				}
				
				if(transaccion.esSellado()){
					Sellado sellado = null;
					desTx = "Sellado de Código : "+transaccion.getNroComprobante().toString(); 
					sellado = Sellado.getByCodigo(transaccion.getNroComprobante().toString());
					if(sellado == null){
						transaccion.registrarIndeterminado("No existe el sellado en SIAT: Código de Sellado: "+transaccion.getNroComprobante().toString()+".", "53"); 
						if (transaccion.hasError()) {
							String descripcion = transaccion.getListError().get(0).key().substring(1);
							this.addRecoverableValueError(descripcion);
							continue;
						}
						continue;
					}		
					this.logDetallado("Transaccion Nro "+transaccion.getNroLinea()+", "+desTx+" encontrada.");
					transaccion.procesarSellado(sellado);
					if(transaccion.hasError()){
						transaccion.addErrorMessages(this);
						transaccion.setConError(SiNo.SI.getId());
						this.updateTransaccion(transaccion);
						continue;
					}
					txTime = System.currentTimeMillis() - txTime;
					AsentamientoCache.getInstance().getSession(this.getId()).addStats("Transaccion", txTime);
					AsentamientoCache.getInstance().getSession(this.getId()).addStats("Transaccion Sellado", txTime);
					continue;
				}
				
				// Si no reconocio a la transaccion como Deuda, Recibo de Deuda, Cuota o Recibo de Cuota, significa
				// que el Tipo Boleta es de algun valor inexistente. (valido para transacciones Nuevas)
				transaccion.registrarIndeterminado("No existe la transaccion en SIAT: tipo de boleta inexistente: "+transaccion.getAnioComprobante().toString()+".", (Deuda) null, null, "9");
				if (transaccion.hasError()) {
					String descripcion = transaccion.getListError().get(0).key().substring(1);
					this.addRecoverableValueError(descripcion);
					continue;
				}		
			}catch(Exception e){
				AdpRun.currentRun().logError("Service Error en Transaccion de Id "+transaccion.getId()+" y Nro de Linea "+transaccion.getNroLinea()+": ",  e);
				this.addRecoverableValueError("Service Error en Transaccion de Id "+transaccion.getId()+" y Nro de Linea "+transaccion.getNroLinea()+": "+e.toString());
				continue;
			}
		}
		
	}

	/**
	 * Consolidar Recaudado y Sellado. Y Validar Tolerancia de la Distribucion.
	 * Consolida las tablas bal_auxRecaudado y bal_auxSellado en la tabla bal_sinPartida. Valida que la diferencia
	 * entre la suma del importe de la tabla bal_transaccion y la suma del importe de la tabla bal_sinPartida para el
	 * asentamiento en cuestion este dentro de los valores de tolerancia.
	 * 	
	 * <i>(paso 2.2)</i>
	 */
	public void consolidarValidar() throws Exception{
		if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO){			
			// -> Consolidar Recaudado
			List<AuxRecaudado> listAuxRecaudado = AuxRecaudado.getListByAsentamiento(this);
			Double importeTotalPartidas = 0D;
			for(AuxRecaudado auxRecaudado: listAuxRecaudado){	
				importeTotalPartidas += auxRecaudado.getImporteEjeAct();
				importeTotalPartidas += auxRecaudado.getImporteEjeVen();
				
				Double importeAImputarAct = auxRecaudado.getImporteEjeAct()*auxRecaudado.getPorcentaje();
				Double importeAImputarVen = auxRecaudado.getImporteEjeVen()*auxRecaudado.getPorcentaje();
				
				Long anioPago = new Long(DateUtil.getAnio(auxRecaudado.getFechaPago()));
				Long mesPago = new Long(DateUtil.getMes(auxRecaudado.getFechaPago()));
				
				// Prueba para solucionar Lazy Initialization al relizar partida.getId() dentro del getByAseParMesAnio
				//SiatHibernateUtil.currentSession().refresh(auxRecaudado);
				
				SinPartida sinPartida = SinPartida.getByAseParMesAnio(this,auxRecaudado.getPartida(),mesPago, anioPago);
				if(sinPartida != null){
					sinPartida.setImporteEjeAct(sinPartida.getImporteEjeAct()+importeAImputarAct);
					sinPartida.setImporteEjeVen(sinPartida.getImporteEjeVen()+importeAImputarVen);
					
					this.updateSinPartida(sinPartida);
				}else{
					sinPartida = new SinPartida();
					sinPartida.setAsentamiento(this);
					sinPartida.setMesPago(mesPago);
					sinPartida.setAnioPago(anioPago);
					sinPartida.setPartida(auxRecaudado.getPartida());
					sinPartida.setImporteEjeAct(importeAImputarAct);
					sinPartida.setImporteEjeVen(importeAImputarVen);
					sinPartida.setFechaBalance(this.getFechaBalance());
					sinPartida.setMarca("");
					this.createSinPartida(sinPartida);
				}
			}
			// -> Consolidar Sellado
			List<AuxSellado> listAuxSellado = AuxSellado.getListByAsentamiento(this);
			Double importeTotalSellado = 0D;
			for(AuxSellado auxSellado: listAuxSellado){	
				if(auxSellado.getEsImporteFijo().intValue() == SiNo.SI.getId().intValue()){
					importeTotalSellado += auxSellado.getImporteFijo();
				}else{
					importeTotalSellado += auxSellado.getImporteEjeAct();
					importeTotalSellado += auxSellado.getImporteEjeVen();				
				}
				Double importeAImputarAct = auxSellado.getImporteEjeAct()*auxSellado.getPorcentaje();
				Double importeAImputarVen = auxSellado.getImporteEjeVen()*auxSellado.getPorcentaje();
				
				Long anioPago = new Long(DateUtil.getAnio(auxSellado.getFechaPago()));
				Long mesPago = new Long(DateUtil.getMes(auxSellado.getFechaPago()));
				
				SinPartida sinPartida = SinPartida.getByAseParMesAnio(this,auxSellado.getPartida(),mesPago, anioPago);
				if(sinPartida != null){
					if(auxSellado.getEsImporteFijo().intValue() == SiNo.SI.getId().intValue()){
						sinPartida.setImporteEjeAct(sinPartida.getImporteEjeAct()+auxSellado.getImporteFijo()); 
					}else{
						sinPartida.setImporteEjeAct(sinPartida.getImporteEjeAct()+importeAImputarAct);
						sinPartida.setImporteEjeVen(sinPartida.getImporteEjeVen()+importeAImputarVen);
					}
					
					this.updateSinPartida(sinPartida);
				}else{
					sinPartida = new SinPartida();
					sinPartida.setAsentamiento(this);
					sinPartida.setMesPago(mesPago);
					sinPartida.setAnioPago(anioPago);
					sinPartida.setPartida(auxSellado.getPartida());
					if(auxSellado.getEsImporteFijo().intValue() == SiNo.SI.getId().intValue()){
						sinPartida.setImporteEjeAct(auxSellado.getImporteFijo());
						sinPartida.setImporteEjeVen(0D);
					}else{
						sinPartida.setImporteEjeAct(importeAImputarAct);
						sinPartida.setImporteEjeVen(importeAImputarVen);					
					}
					sinPartida.setFechaBalance(this.getFechaBalance());
					sinPartida.setMarca("");
					
					this.createSinPartida(sinPartida);
				}
			}
			// -> Validar tolerancia de la distribucion
			Double importeTotalTransaccion = 0D;
			Double importeTotalSinPartida = 0D;
			for(Transaccion transaccion: this.getListTransaccion(null, null)){
				if(transaccion.getEsDesgloce()!=null && transaccion.getEsDesgloce().intValue() == SiNo.NO.getId().intValue())
					importeTotalTransaccion += transaccion.getImporte();
			}
			for(SinPartida sinPartida: this.getListSinPartida()){
				importeTotalSinPartida += sinPartida.getImporteEjeAct()+sinPartida.getImporteEjeVen();
			}
			
			// Diferencia entre totales en valor absoluto.
			Double diferencia = importeTotalTransaccion-importeTotalSinPartida;
			if(diferencia<0)
				diferencia = diferencia*(-1);	// Valor Absoluto
			Tolerancia tolerancia = Tolerancia.getBySerBanYFecha(this.getServicioBanco(), new Date());
			if(tolerancia == null){
				tolerancia = new Tolerancia();
				tolerancia.setToleranciaDifer(0.1D);
				tolerancia.setToleranciaPartida(0.1D);
				tolerancia.setToleranciaSaldo(0.1D);
			}
			// Si la direfencia en valor absoluto es mayor a toleranciaPartida%, Finalizar con Error.
			if(diferencia>(tolerancia.getToleranciaPartida()*importeTotalTransaccion)){ 
				this.addRecoverableValueError("La diferencia entre el Importe total de las transacciones procesadas y el Importe total distribuido en Partidas es superior a la tolerancia Permitida. Importe Total de Transacciones: "+importeTotalTransaccion+", Importe Total Distribuido: "+importeTotalSinPartida+", Tolerancia: "+tolerancia.getToleranciaPartida()*100+" %");
				return;
			}
		}
	}

	/**
	 * <b>Genera Formularios para control del paso 2:</b> 
	 * <p>- Totales por Partida(4007): archivo pdf</p>
	 * <p>- Totales por Partida para Servicio Banco: archivo pdf</p>
	 * <p>- Resultado del Proceso: archivo pdf</p>
	 * <p>- Detalle de Pagos Asentados: planilla de calculo</p>
	 * <p>- Detalle de Indeterminados: planilla de calculo</p>
	 * <p>- Detalle de Saldos a Favor: planilla de calculo</p>
	 * <i>(paso 2.3)</i>
	 */
	public void generarFormulariosPaso2(String outputDir) throws Exception{
		// -> Totales por Partida(4007) (PDF)
		String fileNamePdf = this.generarPdfTotalesPorPartida(outputDir);
		String nombre = "Totales por Partida (ex4007, sólo válido para evaluar datos migrados)";
		String descripcion = "Permite consultar los totales imputados a cada partida.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileNamePdf);
		
		// -> Totales por Partida para Servicio Banco (PDF)
		fileNamePdf = this.generarPdfTotalesPorPartidaServicioBanco(outputDir);
		nombre = "Totales por Partida para Servicio Banco (4007 Siat)";
		descripcion = "Permite consultar los totales imputados a cada partida para los sistemas correspondientes a Servicio Banco.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileNamePdf);
		
		// -> Resultado del Proceso (PDF)
		fileNamePdf = this.generarPdfResultadoDelProceso(outputDir);
		nombre = "Resultado del proceso";
		descripcion = "Permite consultar el resultado del proceso.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileNamePdf);
		
		//	-> Detalle de Pagos Asentados (Planilla de calculo)
		List<String> listFileName = this.exportReportesPagosAsentados(outputDir);
		int c = 0;
		nombre = "Detalle de Pagos Asentados";
		for(String fileName: listFileName){
			descripcion = "Planilla con el Detalle de los Pagos Asentados. Hoja "+c;
			this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
			c++;
		}
		
		// -> Detalle de Indeterminados (Planilla de calculo)
		listFileName = this.exportReportesIndeterminados(outputDir);
		c = 0;
		nombre = "Detalle de Indeterminados";
		for(String fileName: listFileName){
			descripcion = "Planilla con el Detalle de los Pagos Indeterminados generados durante en proceso de Asentamiento. Hoja "+c;
			this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
			c++;
		}
		
		// -> Detalle de Saldos a Favor (Planilla de calculo)
		listFileName = this.exportReportesSaldosAFavor(outputDir);
		c = 0;
		nombre = "Detalle de Saldos a Favor";
		for(String fileName: listFileName){
			descripcion = "Planilla con el Detalle de los Saldos a Favor generados durante en proceso de Asentamiento. Hoja "+c;
			this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
			c++;
		}
		
		// Si corresponde a Servicio Banco: 85 - Otros Tributos (ex Gravamenes Especiales), guardamos datos para reporte por Recurso.
		if(ServicioBanco.COD_OTROS_TRIBUTOS.equals(this.getServicioBanco().getCodServicioBanco())){
			// -> Totales Distribuido por Recurso y Tipo Importe (PDF)
			fileNamePdf = this.generarPdfTotalesPorRecurso(outputDir);
			nombre = "Totales Distribuido por Recurso y Tipo Importe";
			descripcion = "Permite consultar los totales imputados por Recurso para cada tipo de importe.";
			this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileNamePdf);			
		}

	
	}
	
	/**
	 * Genera el Reporte pdf "Totales por Partida(4007)" resultado del paso 2 del proceso de Asentamiento.
	 * 
	 * @param fileDir
	 * @return fileName
	 */
	public String generarPdfTotalesPorPartida(String fileDir) throws Exception{
		//DecimalFormat decimalFormat = new DecimalFormat("0.000000");
		//		Encabezado:
		String fechaAsentamiento = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO)
			ejercicio += " - Asentamiento Común";
		else if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_CERRADO){
			ejercicio += " - Asentamiento Especial";
		}
		String servicioBanco = this.getServicioBanco().getCodServicioBanco() + " - " + this.getServicioBanco().getDesServicioBanco();
		String estado = this.getCorrida().getEstadoCorrida().getDesEstadoCorrida();
		
		Object[] valoresTotales = BalDAOFactory.getTransaccionDAO().getListTotalesForReportByAsentamiento(this, null,null);
		List<Object[]> listDetalleTotal = BalDAOFactory.getSinPartidaDAO().getListDetalleForReportByAsentamiento(this);
		List<Object[]> listDetalleSistema = BalDAOFactory.getAuxRecaudadoDAO().getListDetalleSistemaForReportByAsentamiento(this);	
		
		List<Object[]> listTotalesSellado = BalDAOFactory.getAuxSelladoDAO().getListDetalleForReportByAsentamiento(this);
		
		if(valoresTotales[0] == null){
			valoresTotales[0] = 0L;
		}
		if(valoresTotales[1] == null){
			valoresTotales[1] = 0D;
		}
		Double importeTotal = (Double) valoresTotales[1];
		Double importeTotalDetalle = 0D;
		Double diferencia = 0D;
		
		
		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Asentamiento.COD_FRM_PASO2_1);
		
		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Asentamiento de Pagos - Reporte Distribucion de Partidas");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Asentamiento de Pagos - Reporte Distribucion de Partidas");
		printModel.putCabecera("FechaAsentamiento", fechaAsentamiento);
		printModel.putCabecera("Ejercicio", ejercicio);
		printModel.putCabecera("ServicioBanco", servicioBanco);
		printModel.putCabecera("Estado", estado);
		
		// Armamos un Contenedor para las Tablas
		ContenedorVO contenedor = new ContenedorVO("ContenedorTabla");

		// Se arma una tabla para los Totales.
		TablaVO tabla = new TablaVO("Totales");
		FilaVO fila = new FilaVO();
		fila.add(new CeldaVO("Total de transacciones procesadas:","f1c1"));
		fila.add(new CeldaVO(((Long) valoresTotales[0]).toString(),"f1c2"));
		tabla.add(fila);
		fila = new FilaVO();
		fila.add(new CeldaVO("Importe total procesado:","f2c1"));
		fila.add(new CeldaVO((StringUtil.formatDouble(NumberUtil.truncate((Double) valoresTotales[1], SiatParam.DEC_IMPORTE_DB))),"f2c2"));
		tabla.add(fila);
		
		contenedor.setTablaCabecera(tabla);
		
		// Se arman una tabla para el Reporte Total Por Partidas
		tabla = new TablaVO("TotalesPartida");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Código","codigo"));
		filaCabecera.add(new CeldaVO("Descripción","descripcion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		for(Object[] arrayResult: listDetalleTotal){
			Double suma = (Double) arrayResult[2]+(Double) arrayResult[3];
			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate(suma, SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}

			fila = new FilaVO();
			fila.add(new CeldaVO((String) arrayResult[0],"codigo"));
			fila.add(new CeldaVO(((String) arrayResult[1]),"descripcion"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(suma, SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			importeTotalDetalle += (Double) arrayResult[2]+(Double) arrayResult[3];
		}
		if(importeTotal!=null && importeTotalDetalle!=null)
			diferencia = importeTotal-importeTotalDetalle;
		
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total Distribuido:","totalDistribuido"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotalDetalle, SiatParam.DEC_IMPORTE_DB)),"totalImporteDetalle"));
		tabla.addFilaPie(filaPie);
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total a Distribuir:","totalADistribuir"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Diferencia:","diferencia"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(diferencia, SiatParam.DEC_IMPORTE_DB)),"diferencia"));
		tabla.addFilaPie(filaPie);
		
		contenedor.add(tabla);
		
		// Se arman tablas para Los Detalles Por Sistemas
		FilaVO filaTitulo = new FilaVO();
		boolean inicioSistema = true;
		String desSistema = "";
		Double importeTotalDistribuidoSistema = 0D;
		for(Object[] arrayResult: listDetalleSistema){
			if(!"".equals(desSistema) && !desSistema.equals((String) arrayResult[0])){
				// Si se encuentra un nuevo Sistema:
				filaPie = new FilaVO();
				filaPie.add(new CeldaVO("Total Distribuido:","totalDistribuidoSis"));
				filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotalDistribuidoSistema, SiatParam.DEC_IMPORTE_DB)),"totalImporteDetalleSis"));
				tabla.addFilaPie(filaPie);
				
				contenedor.add(tabla);

				inicioSistema = true;
			}
			if(inicioSistema){
				desSistema = (String) arrayResult[0];
				tabla = new TablaVO("desSistema");
				filaTitulo = new FilaVO();
				filaTitulo.add(new CeldaVO("Detalle Sistema:","detalleSistema"));
				filaTitulo.add(new CeldaVO(desSistema,"sistema"));
				tabla.setFilaTitulo(filaTitulo);
				filaCabecera = new FilaVO();
				filaCabecera.add(new CeldaVO("Código","codigo"));
				filaCabecera.add(new CeldaVO("Descripción","descripcion"));
				filaCabecera.add(new CeldaVO("Importe","importe"));
				tabla.setFilaCabecera(filaCabecera);			
				inicioSistema = false;		
				importeTotalDistribuidoSistema = 0D;
			}
			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate((Double) arrayResult[3], SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}

			fila = new FilaVO();
			fila.add(new CeldaVO((String) arrayResult[1],"codigo"));
			fila.add(new CeldaVO(((String) arrayResult[2]),"descripcion"));
			fila.add(new CeldaVO((StringUtil.formatDouble(NumberUtil.truncate((Double) arrayResult[3], SiatParam.DEC_IMPORTE_DB))),"importe"));
			tabla.add(fila);
			importeTotalDistribuidoSistema += (Double) arrayResult[3];			
		}
		// Fin de la ultima tabla de Detalle de Sistema:
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total Distribuido:","totalDistribuidoSis"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotalDistribuidoSistema, SiatParam.DEC_IMPORTE_DB)),"totalImporteDetalleSis"));
		tabla.addFilaPie(filaPie);
		
		contenedor.add(tabla);

		// Se arman una tabla para el Reporte de Sellados Por Partidas
		tabla = new TablaVO("SelladoPartida");
		filaTitulo = new FilaVO();
		filaTitulo.add(new CeldaVO("Sellados","sellado"));
		tabla.setFilaTitulo(filaTitulo);
		filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Código","codigo"));
		filaCabecera.add(new CeldaVO("Descripción","descripcion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		importeTotalDetalle = 0D;
		for(Object[] arrayResult: listTotalesSellado){
			Double suma = (Double) arrayResult[2];
			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate(suma, SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}

			fila = new FilaVO();
			fila.add(new CeldaVO((String) arrayResult[0],"codigo"));
			fila.add(new CeldaVO(((String) arrayResult[1]),"descripcion"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(suma, SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			importeTotalDetalle += (Double) arrayResult[2];
		}
		
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total Distribuido:","totalDistribuido"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotalDetalle, SiatParam.DEC_IMPORTE_DB)),"totalImporteDetalle"));
		tabla.addFilaPie(filaPie);
		
		contenedor.add(tabla);
	
		
		// Cargamos los datos en el Print Model
		printModel.setData(contenedor);
		printModel.setTopeProfundidad(4);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"Reporte4007.pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		
		return fileName;
	}

	/**
	 * Genera el Reporte pdf "Totales por Partida para Servicio Banco" resultado del paso 2 del proceso de Asentamiento.
	 * 
	 * @param fileDir
	 * @return fileName
	 */
	public String generarPdfTotalesPorPartidaServicioBanco(String fileDir) throws Exception{
		//DecimalFormat decimalFormat = new DecimalFormat("0.000000");
		//		Encabezado:
		String fechaAsentamiento = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO)
			ejercicio += " - Asentamiento Común";
		else if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_CERRADO){
			ejercicio += " - Asentamiento Especial";
		}
		String servicioBanco = this.getServicioBanco().getCodServicioBanco() + " - " + this.getServicioBanco().getDesServicioBanco();
		String estado = this.getCorrida().getEstadoCorrida().getDesEstadoCorrida();
		
		Object[] valoresTotales = BalDAOFactory.getTransaccionDAO().getListTotalesForReportByAsentamiento(this, null,null);
		List<Object[]> listDetalleTotal = BalDAOFactory.getSinPartidaDAO().getListDetalleForReportByAsentamiento(this);
		List<Object[]> listDetalleTipoBoletaDeuda = BalDAOFactory.getAuxRecaudadoDAO().getListDetalleForReportByAsentamiento(this, Transaccion.TIPO_BOLETA_DEUDA, null);
		List<Object[]> listDetalleTipoBoletaCuota = BalDAOFactory.getAuxRecaudadoDAO().getListDetalleForReportByAsentamiento(this, Transaccion.TIPO_BOLETA_CUOTA, null);
		List<Object[]> listDetalleViaDeudaAdmin = BalDAOFactory.getAuxRecaudadoDAO().getListDetalleForReportByAsentamiento(this, null, ViaDeuda.ID_VIA_ADMIN);
		List<Object[]> listDetalleViaDeudaJudicial = BalDAOFactory.getAuxRecaudadoDAO().getListDetalleForReportByAsentamiento(this, null, ViaDeuda.ID_VIA_JUDICIAL);
		List<Object[]> listDetallePlan = BalDAOFactory.getAuxRecaudadoDAO().getListDetallePlanForReportByAsentamiento(this);	
		
		List<Object[]> listTotalesSellado = BalDAOFactory.getAuxSelladoDAO().getListDetalleForReportByAsentamiento(this);

		if(valoresTotales[0] == null){
			valoresTotales[0] = 0L;
		}
		if(valoresTotales[1] == null){
			valoresTotales[1] = 0D;
		}
		Double importeTotal = (Double) valoresTotales[1];
		Double importeTotalDetalle = 0D;
		Double diferencia = 0D;

		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Asentamiento.COD_FRM_PASO2_2);
		
		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Asentamiento de Pagos - Reporte Distribucion de Partidas");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
	
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Asentamiento de Pagos - Reporte Distribucion de Partidas");
		printModel.putCabecera("FechaAsentamiento", fechaAsentamiento);
		printModel.putCabecera("Ejercicio", ejercicio);
		printModel.putCabecera("ServicioBanco", servicioBanco);
		printModel.putCabecera("Estado", estado);
		
		// Armamos un Contenedor para las Tablas
		ContenedorVO contenedor = new ContenedorVO("ContenedorTabla");

		// Se arma una tabla para los Totales.
		TablaVO tabla = new TablaVO("Totales");
		FilaVO fila = new FilaVO();
		fila.add(new CeldaVO("Total de transacciones procesadas:","f1c1"));
		fila.add(new CeldaVO(((Long) valoresTotales[0]).toString(),"f1c2"));
		tabla.add(fila);
		fila = new FilaVO();
		fila.add(new CeldaVO("Importe total procesado:","f2c1"));
		fila.add(new CeldaVO((StringUtil.formatDouble(NumberUtil.truncate((Double) valoresTotales[1], SiatParam.DEC_IMPORTE_DB))),"f2c2"));
		tabla.add(fila);
		
		contenedor.setTablaCabecera(tabla);
		
		// Se arman una tabla para el Reporte Total Por Partidas
		tabla = new TablaVO("TotalesPartida");
		FilaVO filaTitulo = new FilaVO();
		filaTitulo.add(new CeldaVO("Total Sistema:","totalSistema"));
		filaTitulo.add(new CeldaVO(servicioBanco,"servicioBanco"));
		tabla.setFilaTitulo(filaTitulo);
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Código","codigo"));
		filaCabecera.add(new CeldaVO("Descripción","descripcion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		for(Object[] arrayResult: listDetalleTotal){
			Double suma = (Double) arrayResult[2]+(Double) arrayResult[3];
			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate(suma, SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}

			fila = new FilaVO();
			fila.add(new CeldaVO((String) arrayResult[0],"codigo"));
			fila.add(new CeldaVO(((String) arrayResult[1]),"descripcion"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(suma, SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			importeTotalDetalle += (Double) arrayResult[2]+(Double) arrayResult[3];
		}
		if(importeTotal!=null && importeTotalDetalle!=null)
			diferencia = importeTotal-importeTotalDetalle;
		
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total Distribuido:","totalDistribuido"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotalDetalle, SiatParam.DEC_IMPORTE_DB)),"totalImporteDetalle"));
		tabla.addFilaPie(filaPie);
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total a Distribuir:","totalADistribuir"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Diferencia:","diferencia"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(diferencia, SiatParam.DEC_IMPORTE_DB)),"diferencia"));
		tabla.addFilaPie(filaPie);
		
		contenedor.add(tabla);
		
		// Se arma una tabla para Detalle Tipo Boleta: Deuda (y Recibos)
		tabla = new TablaVO("Deuda");
		filaTitulo = new FilaVO();
		filaTitulo.add(new CeldaVO("Deuda (incluyendo Deuda proveniente de Recibos)","tipoBoleta"));
		tabla.setFilaTitulo(filaTitulo);
		filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Código","codigo"));
		filaCabecera.add(new CeldaVO("Descripción","descripcion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		Double importeTotalDistribuidoDeuda = 0D;
		for(Object[] arrayResult: listDetalleTipoBoletaDeuda){
			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}
			fila = new FilaVO();
			fila.add(new CeldaVO((String) arrayResult[0],"codigo"));
			fila.add(new CeldaVO(((String) arrayResult[1]),"descripcion"));
			fila.add(new CeldaVO((StringUtil.formatDouble(NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB))),"importe"));
			tabla.add(fila);
			importeTotalDistribuidoDeuda += (Double) arrayResult[2];
		}
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total Distribuido:","totalDistribuido"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotalDistribuidoDeuda, SiatParam.DEC_IMPORTE_DB)),"totalImporteDeuda"));
		tabla.addFilaPie(filaPie);
		
		contenedor.add(tabla);
	
		// Se arma una tabla para Detalle Tipo Boleta: Cuota (y Recibos)
		tabla = new TablaVO("Cuota");
		filaTitulo = new FilaVO();
		filaTitulo.add(new CeldaVO("Cuota (incluyendo Cuota proveniente de Recibos)","tipoBoleta"));
		tabla.setFilaTitulo(filaTitulo);
		filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Código","codigo"));
		filaCabecera.add(new CeldaVO("Descripción","descripcion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		Double importeTotalDistribuidoCuota = 0D;
		for(Object[] arrayResult: listDetalleTipoBoletaCuota){
			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}
			fila = new FilaVO();
			fila.add(new CeldaVO((String) arrayResult[0],"codigo"));
			fila.add(new CeldaVO(((String) arrayResult[1]),"descripcion"));
			fila.add(new CeldaVO((StringUtil.formatDouble(NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB))),"importe"));
			tabla.add(fila);
			importeTotalDistribuidoCuota += (Double) arrayResult[2];
		}	
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total Distribuido:","totalDistribuido"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotalDistribuidoCuota, SiatParam.DEC_IMPORTE_DB)),"totalImporteCuota"));
		tabla.addFilaPie(filaPie);
		
		contenedor.add(tabla);
	
		// Se arma una tabla para Detalle Abierto por Via Deuda: Via Administrativa
		tabla = new TablaVO("ViaAdmin");
		filaTitulo = new FilaVO();
		filaTitulo.add(new CeldaVO("Via Administrativa","viaDeuda"));
		tabla.setFilaTitulo(filaTitulo);
		filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Código","codigo"));
		filaCabecera.add(new CeldaVO("Descripción","descripcion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		Double importeTotalDistribuidoAdmin = 0D;
		for(Object[] arrayResult: listDetalleViaDeudaAdmin){
			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}
			fila = new FilaVO();
			fila.add(new CeldaVO((String) arrayResult[0],"codigo"));
			fila.add(new CeldaVO(((String) arrayResult[1]),"descripcion"));
			fila.add(new CeldaVO((StringUtil.formatDouble(NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB))),"importe"));
			tabla.add(fila);
			importeTotalDistribuidoAdmin += (Double) arrayResult[2];
		}	
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total Distribuido:","totalDistribuido"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotalDistribuidoAdmin, SiatParam.DEC_IMPORTE_DB)),"totalImporteAdmin"));
		tabla.addFilaPie(filaPie);
		
		contenedor.add(tabla);
		
		// Se arma una tabla para Detalle Abierto por Via Deuda: Via Judicial
		tabla = new TablaVO("ViaJudicial");
		filaTitulo = new FilaVO();
		filaTitulo.add(new CeldaVO("Via Judicial","viaDeuda"));
		tabla.setFilaTitulo(filaTitulo);
		filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Código","codigo"));
		filaCabecera.add(new CeldaVO("Descripción","descripcion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		Double importeTotalDistribuidoJudicial = 0D;
		for(Object[] arrayResult: listDetalleViaDeudaJudicial){
			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}
			fila = new FilaVO();
			fila.add(new CeldaVO((String) arrayResult[0],"codigo"));
			fila.add(new CeldaVO(((String) arrayResult[1]),"descripcion"));
			fila.add(new CeldaVO((StringUtil.formatDouble(NumberUtil.truncate((Double) arrayResult[2], SiatParam.DEC_IMPORTE_DB))),"importe"));
			tabla.add(fila);
			importeTotalDistribuidoJudicial += (Double) arrayResult[2];
		}	
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total Distribuido:","totalDistribuido"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotalDistribuidoJudicial, SiatParam.DEC_IMPORTE_DB)),"totalImporteJudicial"));
		tabla.addFilaPie(filaPie);
		
		contenedor.add(tabla);
		
		// Se arman tablas para Los Detalles Por Planes
		filaTitulo = new FilaVO();
		boolean inicioPlan = true;
		boolean primerPlan = true;
		String desPlan = "";
		Double importeTotalDistribuidoPlan = 0D;
		for(Object[] arrayResult: listDetallePlan){
			if(!"".equals(desPlan) && !desPlan.equals((String) arrayResult[0])){
				// Si se encuentra un nuevo Plan:
				filaPie = new FilaVO();
				filaPie.add(new CeldaVO("Total Distribuido:","totalDistribuidoPlan"));
				filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotalDistribuidoPlan, SiatParam.DEC_IMPORTE_DB)),"totalImporteDetallePlan"));
				tabla.addFilaPie(filaPie);
				
				contenedor.add(tabla);

				inicioPlan = true;
			}
			if(inicioPlan){
				desPlan = (String) arrayResult[0];
				if(primerPlan){
					tabla = new TablaVO("primerPlan");
					primerPlan = false;
				}else{
					tabla = new TablaVO("porPlanes");
				}
				filaTitulo = new FilaVO();
				filaTitulo.add(new CeldaVO(desPlan,"plan"));
				tabla.setFilaTitulo(filaTitulo);
				filaCabecera = new FilaVO();
				filaCabecera.add(new CeldaVO("Código","codigo"));
				filaCabecera.add(new CeldaVO("Descripción","descripcion"));
				filaCabecera.add(new CeldaVO("Importe","importe"));
				tabla.setFilaCabecera(filaCabecera);			
				inicioPlan = false;		
				importeTotalDistribuidoPlan = 0D;
			}
			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate((Double) arrayResult[3], SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}
			fila = new FilaVO();
			fila.add(new CeldaVO((String) arrayResult[1],"codigo"));
			fila.add(new CeldaVO(((String) arrayResult[2]),"descripcion"));
			fila.add(new CeldaVO((StringUtil.formatDouble(NumberUtil.truncate((Double) arrayResult[3], SiatParam.DEC_IMPORTE_DB))),"importe"));
			tabla.add(fila);
			importeTotalDistribuidoPlan += (Double) arrayResult[3];			
		}
		if(!ListUtil.isNullOrEmpty(listDetallePlan)){
			// Fin de la ultima tabla de Detalle Por Planes:
			filaPie = new FilaVO();
			filaPie.add(new CeldaVO("Total Distribuido:","totalDistribuidoPlan"));
			filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotalDistribuidoPlan, SiatParam.DEC_IMPORTE_DB)),"totalImporteDetallePlan"));
			tabla.addFilaPie(filaPie);
			
			contenedor.add(tabla);			
		}
		
		// Se arman una tabla para el Reporte de Sellados Por Partidas
		tabla = new TablaVO("SelladoPartida");
		filaTitulo = new FilaVO();
		filaTitulo.add(new CeldaVO("Sellados","sellado"));
		tabla.setFilaTitulo(filaTitulo);
		filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Código","codigo"));
		filaCabecera.add(new CeldaVO("Descripción","descripcion"));
		filaCabecera.add(new CeldaVO("Importe","importe"));
		tabla.setFilaCabecera(filaCabecera);
		importeTotalDetalle = 0D;
		for(Object[] arrayResult: listTotalesSellado){
			Double suma = (Double) arrayResult[2];
			// Saltear Registros con Importe Cero
			if(NumberUtil.truncate(suma, SiatParam.DEC_IMPORTE_DB).doubleValue() == 0D){
				continue;
			}

			fila = new FilaVO();
			fila.add(new CeldaVO((String) arrayResult[0],"codigo"));
			fila.add(new CeldaVO(((String) arrayResult[1]),"descripcion"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(suma, SiatParam.DEC_IMPORTE_DB)),"importe"));
			tabla.add(fila);
			importeTotalDetalle += (Double) arrayResult[2];
		}
		
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total Distribuido:","totalDistribuido"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotalDetalle, SiatParam.DEC_IMPORTE_DB)),"totalImporteDetalle"));
		tabla.addFilaPie(filaPie);
		
		contenedor.add(tabla);
		
		// Cargamos los datos en el Print Model
		printModel.setData(contenedor);
		printModel.setTopeProfundidad(4);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"Reporte4007SB.pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		
		return fileName;
	}

	/**
	 * Genera el Reporte pdf "Resultado del Proceso" resultado del paso 2 del proceso de Asentamiento.
	 * 
	 * @param fileDir
	 * @return fileName
	 */
	public String generarPdfResultadoDelProceso(String fileDir) throws Exception{
		//DecimalFormat decimalFormat = new DecimalFormat("0.000000");
		//	Encabezado:
		String fechaAsentamiento = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO)
			ejercicio += " - Asentamiento Común";
		else if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_CERRADO){
			ejercicio += " - Asentamiento Especial";
		}
		String servicioBanco = this.getServicioBanco().getCodServicioBanco() + " - " + this.getServicioBanco().getDesServicioBanco();
		String estado = this.getCorrida().getEstadoCorrida().getDesEstadoCorrida();
		
		Object[] valoresTotales = BalDAOFactory.getTransaccionDAO().getListTotalesForReportByAsentamiento(this, null, null);
		if(valoresTotales[0]==null)
			valoresTotales[0] = 0L;
		if(valoresTotales[1]==null)
			valoresTotales[1] = 0D;
		
		Object[] resultadoTotal = BalDAOFactory.getTransaccionDAO().getResultadoTotalForReportByAsentamiento(this, null, null);		
		for(int i=0;i<resultadoTotal.length;i++)
			if(resultadoTotal[i]==null)
				resultadoTotal[i]=0L;
		// Total Parcial para Tipo Boleta = Deuda
		Object[] detalleResultadoTotalDeuda = BalDAOFactory.getTransaccionDAO().getDetalleResultadoForReportByAsentamiento(this,1L, null, null);		
		for(int i=0;i<detalleResultadoTotalDeuda.length;i++)
			if(detalleResultadoTotalDeuda[i]==null)
				detalleResultadoTotalDeuda[i] = new Integer(0);
		// Total Parcial para Tipo Boleta = Cuota
		Object[] detalleResultadoTotalCuota = BalDAOFactory.getTransaccionDAO().getDetalleResultadoForReportByAsentamiento(this,3L, null, null);		
		for(int i=0;i<detalleResultadoTotalCuota.length;i++)
			if(detalleResultadoTotalCuota[i]==null)
				detalleResultadoTotalCuota[i]= new Integer(0);
		// Total Parcial para Tipo Boleta = Recibo Deuda
		Object[] detalleResultadoTotalReciboDeuda = BalDAOFactory.getTransaccionDAO().getDetalleResultadoForReportByAsentamiento(this,2L, null, null);		
		for(int i=0;i<detalleResultadoTotalReciboDeuda.length;i++)
			if(detalleResultadoTotalReciboDeuda[i]==null)
				detalleResultadoTotalReciboDeuda[i]= new Integer(0);
		// Total Parcial para Tipo Boleta = Recibo Cuota
		Object[] detalleResultadoTotalReciboCuota = BalDAOFactory.getTransaccionDAO().getDetalleResultadoForReportByAsentamiento(this,4L, null, null);		
		for(int i=0;i<detalleResultadoTotalReciboCuota.length;i++)
			if(detalleResultadoTotalReciboCuota[i]==null)
				detalleResultadoTotalReciboCuota[i]= new Integer(0);
		// Lista de Resultados totales agrupados por Plan
		List<Object[]> listResTotPorPlan = BalDAOFactory.getTransaccionDAO().getListResTotPorPlanForReportByAsentamiento(this);
		for(Object[] detalleResTotPorPlan: listResTotPorPlan){
			for(int i=0;i<detalleResTotPorPlan.length;i++)
				if(detalleResTotPorPlan[i]==null)
					detalleResTotPorPlan[i]= new Integer(0);
			
		}
		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Asentamiento.COD_FRM_PASO2_3);
		
		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Asentamiento de Pagos - Resultado del Proceso");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
	
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Asentamiento de Pagos - Resultado del Proceso");
		printModel.putCabecera("FechaAsentamiento", fechaAsentamiento);
		printModel.putCabecera("Ejercicio", ejercicio);
		printModel.putCabecera("ServicioBanco", servicioBanco);
		printModel.putCabecera("Estado", estado);
		
		// Armamos un Contenedor para las Tablas
		ContenedorVO contenedor = new ContenedorVO("ContenedorTabla");
	
		// Se arman una tabla para el Resultado Total 
		TablaVO tabla = new TablaVO("TotalTransaccion");
		FilaVO filaTitulo = new FilaVO();
		filaTitulo.add(new CeldaVO("none","detalleSistema"));
		filaTitulo.add(new CeldaVO("none","sistema"));
		filaTitulo.add(new CeldaVO("Total de transacciones procesadas:","totTran"));
		filaTitulo.add(new CeldaVO(((Long) valoresTotales[0]).toString(),"totTranVal"));
		filaTitulo.add(new CeldaVO("Importe total procesado:","totImp"));
		filaTitulo.add(new CeldaVO((StringUtil.formatDouble(NumberUtil.truncate((Double) valoresTotales[1], SiatParam.DEC_IMPORTE_DB))),"totImpVal"));

		tabla.setFilaTitulo(filaTitulo);
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Tipo Transacción","tipoTransaccion"));
		filaCabecera.add(new CeldaVO("Cantidad","cantidad"));
		filaCabecera.add(new CeldaVO("Importe Total","importeTotal"));
		filaCabecera.add(new CeldaVO("Cantidad Indeterminadas","cantIndet"));
		filaCabecera.add(new CeldaVO("Importe Indeterminados","importeIndet"));
		filaCabecera.add(new CeldaVO("Cantidad Asentamientos","cantAse"));
		filaCabecera.add(new CeldaVO("Importe Asentado","importeAse"));
		tabla.setFilaCabecera(filaCabecera);
		FilaVO fila = new FilaVO();
		if(detalleResultadoTotalReciboDeuda != null && detalleResultadoTotalReciboDeuda.length==6){
			fila.add(new CeldaVO("Recibo","tipoTransaccion"));
			fila.add(new CeldaVO(detalleResultadoTotalReciboDeuda[0].toString(),"cantidad"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboDeuda[1].toString()), SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
			fila.add(new CeldaVO(detalleResultadoTotalReciboDeuda[2].toString(),"cantIndet"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboDeuda[3].toString()), SiatParam.DEC_IMPORTE_DB)),"importeIndet"));
			fila.add(new CeldaVO(detalleResultadoTotalReciboDeuda[4].toString(),"cantAse"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboDeuda[5].toString()), SiatParam.DEC_IMPORTE_DB)),"importeAse"));
			tabla.add(fila);
		}
		fila = new FilaVO();
		if(detalleResultadoTotalDeuda != null && detalleResultadoTotalDeuda.length==6){
			fila.add(new CeldaVO("Deuda","tipoTransaccion"));
			fila.add(new CeldaVO(detalleResultadoTotalDeuda[0].toString(),"cantidad"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalDeuda[1].toString()), SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
			fila.add(new CeldaVO(detalleResultadoTotalDeuda[2].toString(),"cantIndet"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalDeuda[3].toString()), SiatParam.DEC_IMPORTE_DB)),"importeIndet"));
			fila.add(new CeldaVO(detalleResultadoTotalDeuda[4].toString(),"cantAse"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalDeuda[5].toString()), SiatParam.DEC_IMPORTE_DB)),"importeAse"));
			tabla.add(fila);
		}
		fila = new FilaVO();
		if(detalleResultadoTotalReciboCuota != null && detalleResultadoTotalReciboCuota.length==6){
			fila.add(new CeldaVO("Recibo Cuota","tipoTransaccion"));
			fila.add(new CeldaVO(detalleResultadoTotalReciboCuota[0].toString(),"cantidad"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboCuota[1].toString()), SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
			fila.add(new CeldaVO(detalleResultadoTotalReciboCuota[2].toString(),"cantIndet"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboCuota[3].toString()), SiatParam.DEC_IMPORTE_DB)),"importeIndet"));
			fila.add(new CeldaVO(detalleResultadoTotalReciboCuota[4].toString(),"cantAse"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboCuota[5].toString()), SiatParam.DEC_IMPORTE_DB)),"importeAse"));
			tabla.add(fila);
		}
		fila = new FilaVO();
		if(detalleResultadoTotalCuota != null && detalleResultadoTotalCuota.length==6){
			fila.add(new CeldaVO("Cuota","tipoTransaccion"));
			fila.add(new CeldaVO(detalleResultadoTotalCuota[0].toString(),"cantidad"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalCuota[1].toString()), SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
			fila.add(new CeldaVO(detalleResultadoTotalCuota[2].toString(),"cantIndet"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalCuota[3].toString()), SiatParam.DEC_IMPORTE_DB)),"importeIndet"));
			fila.add(new CeldaVO(detalleResultadoTotalCuota[4].toString(),"cantAse"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalCuota[5].toString()), SiatParam.DEC_IMPORTE_DB)),"importeAse"));
			tabla.add(fila);
		}
		FilaVO filaPie = new FilaVO();
		if(resultadoTotal != null && resultadoTotal.length==6){
			filaPie.add(new CeldaVO("Total","tipoTransaccion"));
			filaPie.add(new CeldaVO(resultadoTotal[0].toString(),"cantidad"));
			filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(resultadoTotal[1].toString()), SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
			filaPie.add(new CeldaVO(resultadoTotal[2].toString(),"cantIndet"));
			filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(resultadoTotal[3].toString()), SiatParam.DEC_IMPORTE_DB)),"importeIndet"));
			filaPie.add(new CeldaVO(resultadoTotal[4].toString(),"cantAse"));
			filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(resultadoTotal[5].toString()), SiatParam.DEC_IMPORTE_DB)),"importeAse"));
			tabla.addFilaPie(filaPie);
		}
	
		contenedor.add(tabla);
		
		List<Sistema> listSistema = BalDAOFactory.getTransaccionDAO().getListSistemaForReportByAsentamiento(this);

		for(Sistema sistema: listSistema){		
			Object[] valoresTotalesSis = BalDAOFactory.getTransaccionDAO().getListTotalesForReportByAsentamiento(this, sistema, null);
			Object[] resultadoTotalSis = BalDAOFactory.getTransaccionDAO().getResultadoTotalForReportByAsentamiento(this, sistema, null);		
		
			for(int i=0;i<valoresTotalesSis.length;i++)
				if(valoresTotalesSis[i]==null)
					valoresTotalesSis[i] = new Integer(0);
			for(int i=0;i<resultadoTotalSis.length;i++)
				if(resultadoTotalSis[i]==null)
					resultadoTotalSis[i]= new Integer(0);
			// Total Parcial para Tipo Boleta = Deuda
			Object[] detalleResultadoTotalDeudaSis = BalDAOFactory.getTransaccionDAO().getDetalleResultadoForReportByAsentamiento(this,1L, sistema, null);		
			for(int i=0;i<detalleResultadoTotalDeudaSis.length;i++)
				if(detalleResultadoTotalDeudaSis[i]==null)
					detalleResultadoTotalDeudaSis[i] = new Integer(0);
			// Total Parcial para Tipo Boleta = Cuota
			Object[] detalleResultadoTotalCuotaSis = BalDAOFactory.getTransaccionDAO().getDetalleResultadoForReportByAsentamiento(this,3L, sistema, null);		
			for(int i=0;i<detalleResultadoTotalCuotaSis.length;i++)
				if(detalleResultadoTotalCuotaSis[i]==null)
					detalleResultadoTotalCuotaSis[i]= new Integer(0);
			// Total Parcial para Tipo Boleta = Recibo Deuda
			Object[] detalleResultadoTotalReciboDeudaSis = BalDAOFactory.getTransaccionDAO().getDetalleResultadoForReportByAsentamiento(this,2L, sistema, null);		
			for(int i=0;i<detalleResultadoTotalReciboDeudaSis.length;i++)
				if(detalleResultadoTotalReciboDeudaSis[i]==null)
					detalleResultadoTotalReciboDeudaSis[i]= new Integer(0);
			// Total Parcial para Tipo Boleta = Recibo Cuota
			Object[] detalleResultadoTotalReciboCuotaSis = BalDAOFactory.getTransaccionDAO().getDetalleResultadoForReportByAsentamiento(this,4L, sistema, null);		
			for(int i=0;i<detalleResultadoTotalReciboCuotaSis.length;i++)
				if(detalleResultadoTotalReciboCuotaSis[i]==null)
					detalleResultadoTotalReciboCuotaSis[i]= new Integer(0);

			// Se arman tablas para el Resultado Por Sistemas 
			tabla = new TablaVO("TotalSistema");
			filaTitulo = new FilaVO();
			filaTitulo.add(new CeldaVO("Detalle Sistema:","detalleSistema"));
			filaTitulo.add(new CeldaVO(sistema.getDesSistema(),"sistema"));
			filaTitulo.add(new CeldaVO("Total de transacciones procesadas:","totTran"));
			filaTitulo.add(new CeldaVO(((Long) valoresTotalesSis[0]).toString(),"totTranVal"));
			filaTitulo.add(new CeldaVO("Importe total procesado:","totImp"));
			filaTitulo.add(new CeldaVO((StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(valoresTotalesSis[1].toString()), SiatParam.DEC_IMPORTE_DB))),"totImpVal"));

			tabla.setFilaTitulo(filaTitulo);
			filaCabecera = new FilaVO();
			filaCabecera.add(new CeldaVO("Tipo Transacción","tipoTransaccion"));
			filaCabecera.add(new CeldaVO("Cantidad","cantidad"));
			filaCabecera.add(new CeldaVO("Importe Total","importeTotal"));
			filaCabecera.add(new CeldaVO("Cantidad Indeterminadas","cantIndet"));
			filaCabecera.add(new CeldaVO("Importe Indeterminados","importeIndet"));
			filaCabecera.add(new CeldaVO("Cantidad Asentamientos","cantAse"));
			filaCabecera.add(new CeldaVO("Importe Asentado","importeAse"));
			tabla.setFilaCabecera(filaCabecera);
			fila = new FilaVO();
			if(detalleResultadoTotalReciboDeudaSis != null && detalleResultadoTotalReciboDeudaSis.length==6){
				fila.add(new CeldaVO("Recibo","tipoTransaccion"));
				fila.add(new CeldaVO(detalleResultadoTotalReciboDeudaSis[0].toString(),"cantidad"));
				fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboDeudaSis[1].toString()), SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
				fila.add(new CeldaVO(detalleResultadoTotalReciboDeudaSis[2].toString(),"cantIndet"));
				fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboDeudaSis[3].toString()), SiatParam.DEC_IMPORTE_DB)),"importeIndet"));
				fila.add(new CeldaVO(detalleResultadoTotalReciboDeudaSis[4].toString(),"cantAse"));
				fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboDeudaSis[5].toString()), SiatParam.DEC_IMPORTE_DB)),"importeAse"));
				tabla.add(fila);
			}
			fila = new FilaVO();
			if(detalleResultadoTotalDeudaSis != null && detalleResultadoTotalDeudaSis.length==6){
				fila.add(new CeldaVO("Deuda","tipoTransaccion"));
				fila.add(new CeldaVO(detalleResultadoTotalDeudaSis[0].toString(),"cantidad"));
				fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalDeudaSis[1].toString()), SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
				fila.add(new CeldaVO(detalleResultadoTotalDeudaSis[2].toString(),"cantIndet"));
				fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalDeudaSis[3].toString()), SiatParam.DEC_IMPORTE_DB)),"importeIndet"));
				fila.add(new CeldaVO(detalleResultadoTotalDeudaSis[4].toString(),"cantAse"));
				fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalDeudaSis[5].toString()), SiatParam.DEC_IMPORTE_DB)),"importeAse"));
				tabla.add(fila);
			}
			fila = new FilaVO();
			if(detalleResultadoTotalReciboCuotaSis != null && detalleResultadoTotalReciboCuotaSis.length==6){
				fila.add(new CeldaVO("Recibo Cuota","tipoTransaccion"));
				fila.add(new CeldaVO(detalleResultadoTotalReciboCuotaSis[0].toString(),"cantidad"));
				fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboCuotaSis[1].toString()), SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
				fila.add(new CeldaVO(detalleResultadoTotalReciboCuotaSis[2].toString(),"cantIndet"));
				fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboCuotaSis[3].toString()), SiatParam.DEC_IMPORTE_DB)),"importeIndet"));
				fila.add(new CeldaVO(detalleResultadoTotalReciboCuotaSis[4].toString(),"cantAse"));
				fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboCuotaSis[5].toString()), SiatParam.DEC_IMPORTE_DB)),"importeAse"));
				tabla.add(fila);
			}
			fila = new FilaVO();
			if(detalleResultadoTotalCuotaSis != null && detalleResultadoTotalCuotaSis.length==6){
				fila.add(new CeldaVO("Cuota","tipoTransaccion"));
				fila.add(new CeldaVO(detalleResultadoTotalCuotaSis[0].toString(),"cantidad"));
				fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalCuotaSis[1].toString()), SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
				fila.add(new CeldaVO(detalleResultadoTotalCuotaSis[2].toString(),"cantIndet"));
				fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalCuotaSis[3].toString()), SiatParam.DEC_IMPORTE_DB)),"importeIndet"));
				fila.add(new CeldaVO(detalleResultadoTotalCuotaSis[4].toString(),"cantAse"));
				fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalCuotaSis[5].toString()), SiatParam.DEC_IMPORTE_DB)),"importeAse"));
				tabla.add(fila);
			}
			filaPie = new FilaVO();
			if(resultadoTotalSis != null && resultadoTotalSis.length==6){
				filaPie.add(new CeldaVO("Total","tipoTransaccion"));
				filaPie.add(new CeldaVO(resultadoTotalSis[0].toString(),"cantidad"));
				filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(resultadoTotalSis[1].toString()), SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
				filaPie.add(new CeldaVO(resultadoTotalSis[2].toString(),"cantIndet"));
				filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(resultadoTotalSis[3].toString()), SiatParam.DEC_IMPORTE_DB)),"importeIndet"));
				filaPie.add(new CeldaVO(resultadoTotalSis[4].toString(),"cantAse"));
				filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(resultadoTotalSis[5].toString()), SiatParam.DEC_IMPORTE_DB)),"importeAse"));
				tabla.addFilaPie(filaPie);
			}
		
			contenedor.add(tabla);		
		}
		
		
	 	Object[] valoresTotalesReingresos = BalDAOFactory.getTransaccionDAO().getListTotalesForReportByAsentamiento(this, null, 80L);
		Object[] resultadoTotalReingresos = BalDAOFactory.getTransaccionDAO().getResultadoTotalForReportByAsentamiento(this, null, 80L);		
	
		for(int i=0;i<valoresTotalesReingresos.length;i++)
			if(valoresTotalesReingresos[i]==null)
				valoresTotalesReingresos[i] = new Integer(0);
		for(int i=0;i<resultadoTotalReingresos.length;i++)
			if(resultadoTotalReingresos[i]==null)
				resultadoTotalReingresos[i]= new Integer(0);
		// Total Parcial para Tipo Boleta = Deuda
		Object[] detalleResultadoTotalDeudaReingreso = BalDAOFactory.getTransaccionDAO().getDetalleResultadoForReportByAsentamiento(this,1L, null, 80L);		
		for(int i=0;i<detalleResultadoTotalDeudaReingreso.length;i++)
			if(detalleResultadoTotalDeudaReingreso[i]==null)
				detalleResultadoTotalDeudaReingreso[i] = new Integer(0);
		// Total Parcial para Tipo Boleta = Cuota
		Object[] detalleResultadoTotalCuotaReingreso = BalDAOFactory.getTransaccionDAO().getDetalleResultadoForReportByAsentamiento(this,3L, null, 80L);		
		for(int i=0;i<detalleResultadoTotalCuotaReingreso.length;i++)
			if(detalleResultadoTotalCuotaReingreso[i]==null)
				detalleResultadoTotalCuotaReingreso[i]= new Integer(0);
		// Total Parcial para Tipo Boleta = Recibo Deuda
		Object[] detalleResultadoTotalReciboDeudaReingreso = BalDAOFactory.getTransaccionDAO().getDetalleResultadoForReportByAsentamiento(this,2L, null, 80L);		
		for(int i=0;i<detalleResultadoTotalReciboDeudaReingreso.length;i++)
			if(detalleResultadoTotalReciboDeudaReingreso[i]==null)
				detalleResultadoTotalReciboDeudaReingreso[i]= new Integer(0);
		// Total Parcial para Tipo Boleta = Recibo Cuota
		Object[] detalleResultadoTotalReciboCuotaReingreso = BalDAOFactory.getTransaccionDAO().getDetalleResultadoForReportByAsentamiento(this,4L, null, 80L);		
		for(int i=0;i<detalleResultadoTotalReciboCuotaReingreso.length;i++)
			if(detalleResultadoTotalReciboCuotaReingreso[i]==null)
				detalleResultadoTotalReciboCuotaReingreso[i]= new Integer(0);

		// Se arman tablas para el Resultado Por Sistemas 
		tabla = new TablaVO("TotalReingresos");
		filaTitulo = new FilaVO();
		filaTitulo.add(new CeldaVO("Detalle de Reingresos de Indeterminados:","detalleReingresos"));
		filaTitulo.add(new CeldaVO("","sistema"));
		filaTitulo.add(new CeldaVO("Total de transacciones procesadas:","totTran"));
		filaTitulo.add(new CeldaVO(((Long) valoresTotalesReingresos[0]).toString(),"totTranVal"));
		filaTitulo.add(new CeldaVO("Importe total procesado:","totImp"));
		filaTitulo.add(new CeldaVO((NumberUtil.truncate(Double.valueOf(valoresTotalesReingresos[1].toString()), SiatParam.DEC_IMPORTE_DB)).toString(),"totImpVal"));

		tabla.setFilaTitulo(filaTitulo);
		filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Tipo Transacción","tipoTransaccion"));
		filaCabecera.add(new CeldaVO("Cantidad","cantidad"));
		filaCabecera.add(new CeldaVO("Importe Total","importeTotal"));
		filaCabecera.add(new CeldaVO("Cantidad Indeterminadas","cantIndet"));
		filaCabecera.add(new CeldaVO("Importe Indeterminados","importeIndet"));
		filaCabecera.add(new CeldaVO("Cantidad Asentamientos","cantAse"));
		filaCabecera.add(new CeldaVO("Importe Asentado","importeAse"));
		tabla.setFilaCabecera(filaCabecera);
		fila = new FilaVO();
		if(detalleResultadoTotalReciboDeudaReingreso != null && detalleResultadoTotalReciboDeudaReingreso.length==6){
			fila.add(new CeldaVO("Recibo","tipoTransaccion"));
			fila.add(new CeldaVO(detalleResultadoTotalReciboDeudaReingreso[0].toString(),"cantidad"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboDeudaReingreso[1].toString()), SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
			fila.add(new CeldaVO(detalleResultadoTotalReciboDeudaReingreso[2].toString(),"cantIndet"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboDeudaReingreso[3].toString()), SiatParam.DEC_IMPORTE_DB)),"importeIndet"));
			fila.add(new CeldaVO(detalleResultadoTotalReciboDeudaReingreso[4].toString(),"cantAse"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboDeudaReingreso[5].toString()), SiatParam.DEC_IMPORTE_DB)),"importeAse"));
			tabla.add(fila);
		}
		fila = new FilaVO();
		if(detalleResultadoTotalDeudaReingreso != null && detalleResultadoTotalDeudaReingreso.length==6){
			fila.add(new CeldaVO("Deuda","tipoTransaccion"));
			fila.add(new CeldaVO(detalleResultadoTotalDeudaReingreso[0].toString(),"cantidad"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalDeudaReingreso[1].toString()), SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
			fila.add(new CeldaVO(detalleResultadoTotalDeudaReingreso[2].toString(),"cantIndet"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalDeudaReingreso[3].toString()), SiatParam.DEC_IMPORTE_DB)),"importeIndet"));
			fila.add(new CeldaVO(detalleResultadoTotalDeudaReingreso[4].toString(),"cantAse"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalDeudaReingreso[5].toString()), SiatParam.DEC_IMPORTE_DB)),"importeAse"));
			tabla.add(fila);
		}
		fila = new FilaVO();
		if(detalleResultadoTotalReciboCuotaReingreso != null && detalleResultadoTotalReciboCuotaReingreso.length==6){
			fila.add(new CeldaVO("Recibo Cuota","tipoTransaccion"));
			fila.add(new CeldaVO(detalleResultadoTotalReciboCuotaReingreso[0].toString(),"cantidad"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboCuotaReingreso[1].toString()), SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
			fila.add(new CeldaVO(detalleResultadoTotalReciboCuotaReingreso[2].toString(),"cantIndet"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboCuotaReingreso[3].toString()), SiatParam.DEC_IMPORTE_DB)),"importeIndet"));
			fila.add(new CeldaVO(detalleResultadoTotalReciboCuotaReingreso[4].toString(),"cantAse"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalReciboCuotaReingreso[5].toString()), SiatParam.DEC_IMPORTE_DB)),"importeAse"));
			tabla.add(fila);
		}
		fila = new FilaVO();
		if(detalleResultadoTotalCuotaReingreso != null && detalleResultadoTotalCuotaReingreso.length==6){
			fila.add(new CeldaVO("Cuota","tipoTransaccion"));
			fila.add(new CeldaVO(detalleResultadoTotalCuotaReingreso[0].toString(),"cantidad"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalCuotaReingreso[1].toString()), SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
			fila.add(new CeldaVO(detalleResultadoTotalCuotaReingreso[2].toString(),"cantIndet"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalCuotaReingreso[3].toString()), SiatParam.DEC_IMPORTE_DB)),"importeIndet"));
			fila.add(new CeldaVO(detalleResultadoTotalCuotaReingreso[4].toString(),"cantAse"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(detalleResultadoTotalCuotaReingreso[5].toString()), SiatParam.DEC_IMPORTE_DB)),"importeAse"));
			tabla.add(fila);
		}
		filaPie = new FilaVO();
		if(resultadoTotalReingresos != null && resultadoTotalReingresos.length==6){
			filaPie.add(new CeldaVO("Total","tipoTransaccion"));
			filaPie.add(new CeldaVO(resultadoTotalReingresos[0].toString(),"cantidad"));
			filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(resultadoTotalReingresos[1].toString()), SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
			filaPie.add(new CeldaVO(resultadoTotalReingresos[2].toString(),"cantIndet"));
			filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(resultadoTotalReingresos[3].toString()), SiatParam.DEC_IMPORTE_DB)),"importeIndet"));
			filaPie.add(new CeldaVO(resultadoTotalReingresos[4].toString(),"cantAse"));
			filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(resultadoTotalReingresos[5].toString()), SiatParam.DEC_IMPORTE_DB)),"importeAse"));
			tabla.addFilaPie(filaPie);
		}
	
		contenedor.add(tabla);		
	 
		// Se arma tabla para totales por Plan  
		tabla = new TablaVO("TotalesPorPlan");
		filaTitulo = new FilaVO();
		filaTitulo.add(new CeldaVO("Detalle de Totales por Plan:","detallePorPlan"));
		filaTitulo.add(new CeldaVO("","plan"));

		tabla.setFilaTitulo(filaTitulo);
	
		filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Plan","plan"));
		filaCabecera.add(new CeldaVO("Cantidad","cantidad"));
		filaCabecera.add(new CeldaVO("Importe Total","importeTotal"));
		filaCabecera.add(new CeldaVO("Cantidad Indeterminadas","cantIndet"));
		filaCabecera.add(new CeldaVO("Importe Indeterminados","importeIndet"));
		filaCabecera.add(new CeldaVO("Cantidad Asentamientos","cantAse"));
		filaCabecera.add(new CeldaVO("Importe Asentado","importeAse"));
	
		tabla.setFilaCabecera(filaCabecera);
		fila = new FilaVO();
		Double importeTotal = 0D;
		Double cantTotal = 0D;
		for(Object[] arrayResult: listResTotPorPlan){
			fila.add(new CeldaVO((String) arrayResult[6],"plan"));
			fila.add(new CeldaVO(arrayResult[0].toString(),"cantidad"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(arrayResult[1].toString()), SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
			fila.add(new CeldaVO(arrayResult[2].toString(),"cantIndet"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(arrayResult[3].toString()), SiatParam.DEC_IMPORTE_DB)),"importeIndet"));
			fila.add(new CeldaVO(arrayResult[4].toString(),"cantAse"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(Double.valueOf(arrayResult[5].toString()), SiatParam.DEC_IMPORTE_DB)),"importeAse"));
			tabla.add(fila);
			fila = new FilaVO();
			importeTotal += (Double) arrayResult[1];
			cantTotal += (Long) arrayResult[0];
		}
		filaPie = new FilaVO();
		filaPie.add(new CeldaVO("Total","total"));
		filaPie.add(new CeldaVO(cantTotal.toString(),"totalTransaccion"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"totalImporte"));
		tabla.addFilaPie(filaPie);

		contenedor.add(tabla);		
		
		// Cargamos los datos en el Print Model
		printModel.setData(contenedor);
		printModel.setTopeProfundidad(4);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"ResultadoProceso.pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		
		return fileName;
	}

	/**
	 *  Enviar Solicitudes de Accion para el Convenio.
	 * 	
	 * <i>(paso 3.5)</i>
	 */
	public void enviarSolicitudAccionConvenio() throws Exception {
		for(AuxConvenio auxConvenio: this.getListAuxConvenio()){
			Convenio convenio = auxConvenio.getConvenio();
			if(TipoSolicitud.COD_RESCATE_CONVENIO_VIA_ADM.equals(auxConvenio.getTipoSolicitud().getCodigo())
					|| TipoSolicitud.COD_RESCATE_CONVENIO_VIA_JUD.equals(auxConvenio.getTipoSolicitud().getCodigo())){
				String descripcion = "El Convenio "+convenio.getNroConvenio()+" de la Cuenta "+convenio.getCuenta().getNumeroCuenta()
						+" se encuentra en estado CADUCO y el asentamiento detectó que podría no estarlo. Asentamiento "
						+this.getEjercicio().getDesEjercicio()+"/"+this.getServicioBanco().getCodServicioBanco()+"/"
						+this.getFechaBalance(); 
				CasSolicitudManager.getInstance().createSolicitud(auxConvenio.getTipoSolicitud(), 
						"Verificar Convenio", descripcion,convenio.getCuenta());				
			}
			if(TipoSolicitud.COD_VERIFICAR_CONVENIO_RECOMPUESTO.equals(auxConvenio.getTipoSolicitud().getCodigo())){
				String descripcion = "El Convenio "+convenio.getNroConvenio()+" de la Cuenta "+convenio.getCuenta().getNumeroCuenta()
						+" se encuentra en estado RECOMPUESTO y en el asentamiento se procesó un pago de Cuota o Recibo de Cuota, registrando la transacción como indeterminada. Asentamiento "
						+this.getEjercicio().getDesEjercicio()+"/"+this.getServicioBanco().getCodServicioBanco()+"/"
						+this.getFechaBalance(); 
				CasSolicitudManager.getInstance().createSolicitud(auxConvenio.getTipoSolicitud(), 
							"Verificar Convenio", descripcion,convenio.getCuenta());				
			}
		}
	}
	
	/**
	 * <b>Genera Formularios para control del paso 3:</b> 
	 * <p>- Totales por Partida (4007): archivo pdf</p>
	 * <p>- Totales por Partida para Servicio Banco: archivo pdf</p>
	 * <p>- Resultado del Proceso: archivo pdf</p>
	 * <p>- Detalle de Pagos Asentados: planilla de calculo</p>
	 * <p>- Detalle de Indeterminados: planilla de calculo</p>
	 * <p>- Detalle de Saldos a Favor: planilla de calculo</p>
	 * <p>- Detalle de Convenios Cancelados: planilla de calculo</p>
	 * <p>- Detalle de Convenios a Revision: planilla de calculo</p>
	 *
	 * <i>(paso 3.6)</i>
	 */
	public void generarFormulariosPaso3(String outputDir) throws Exception{
			
		// -> Detalle de Convenios Cancelados (Planilla de calculo)
		List<String> listFileName = this.exportReportesConveniosCancelados(outputDir);
		int c = 0;
		String nombre = "Detalle de Convenios Cancelados";
		String descripcion = "";
		for(String fileName: listFileName){
			descripcion = "Planilla con los Convenios que fueron Cancelados al asentarse el pago de todas sus cuotas. Hoja "+c;
			this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
			c++;
		}
	
		// -> Detalle de Convenios a Revision (Planilla de calculo)
		listFileName = this.exportReportesConveniosARevision(outputDir);
		c = 0;
		nombre = "Detalle de Convenios a Revisión";
		for(String fileName: listFileName){
			descripcion = "Planilla con los Convenios en los que se detectaron Pagos a Cuenta que pueden ser transformados en Pagos Buenos. Hoja "+c;
			this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
			c++;
		}
	
	}
	
	/**
	 * <b>Genera Archivos de Sincronismo</b> 
	 * <p>- Archivo de Partidas para Sincronismo</p>
	 * <p>- Archivo de Indeterminados para Sincronismo</p>
	 * <p>- Archivo de Saldos a Favor para Sincronismo</p>
	 *
	 * <i>(paso 3.7)</i>
	 */
	public void generarArchivosSincronismo(String outputDir) throws Exception{
		//	-> Archivo de Partidas para Sincronismo
		String fileName = this.generarArchSincPartidas(outputDir);
		String nombre = "Archivo de Partidas para Sincronismo";
		String descripcion = "Archivo Transferido correspondiente a las partidas.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);

		//	-> Archivo de Indeterminados para Sincronismo
		fileName = this.generarArchSincIndeterminados(outputDir);
		nombre = "Archivo de Indeterminados para Sincronismo";
		descripcion = "Archivo Transferido correspondiente a los Indeterminados generados por el proceso de Asentamiento.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
		
		//	-> Archivo de Saldos a Favor para Sincronismo
		fileName = this.generarArchSincSaldosAFavor(outputDir);
		nombre = "Archivo de Saldos a Favor para Sincronismo";
		descripcion = "Archivo Transferido correspondiente a los Saldos a Favor generados por el proceso de Asentamiento.";
		this.getCorrida().addOutputFile(nombre, descripcion, outputDir+fileName);
		
	}
	
	/**
	 * Genera el Archivo de Partidas para Sincronismo resultado del paso 3 del proceso de Asentamiento.
	 * 
	 * @param outputDir
	 * @return fileName
	 */
	public String generarArchSincPartidas(String outputDir) throws Exception{
		return BalDAOFactory.getAsentamientoDAO().exportSinPartidaByAsentamiento(this, outputDir);
	}

	/**
	 * Genera el Archivo de Indeterminados para Sincronismo resultado del paso 3 del proceso de Asentamiento.
	 * 
	 * @param outputDir
	 * @return fileName
	 */
	public String generarArchSincIndeterminados(String outputDir)throws Exception{
		return BalDAOFactory.getAsentamientoDAO().exportSinIndetByAsentamiento(this, outputDir);
	}
	
	/**
	 * Genera el Archivo de Saldos a Favor para Sincronismo resultado del paso 3 del proceso de Asentamiento.
	 * 
	 * @param outputDir
	 * @return fileName
	 */
	public String generarArchSincSaldosAFavor(String outputDir)throws Exception{
		return BalDAOFactory.getAsentamientoDAO().exportSinSalAFavByAsentamiento(this, outputDir);		
	}	
	
	/**
	 * Actualiza el Detalle de Deuda de la Constancia pasandolo a inactivo. Devuelve la Constancia.
	 * 	@return constanciaDeu
	 * <i>(paso 3.1.a)</i>
	 */
	public ConstanciaDeu actualizarConstancia(Deuda deuda) throws Exception{
		
		ConDeuDet conDeuDet = deuda.obtenerConDeuDetVigente();
		if(conDeuDet != null){
			conDeuDet.setEstado(Estado.INACTIVO.getId());
			conDeuDet.getConstanciaDeu().updateConDeuDet(conDeuDet);
			return conDeuDet.getConstanciaDeu();
		}else
			return null;
	}
	
	/**
	 *  Cambia el estado del convenio a Cancelado, registra el cambio de estado, cancela la deuda incluida y registra
	 *  los pagos de deuda. Además cuando corresponde actualiza Constancias (Via Judicial)
	 * 
	 * @param convenio
	 * @param transaccion 
	 */
	public void cancelarConvenio(Convenio convenio, Transaccion transaccion) throws Exception{
		// Cambiar el estado del convenio a cancelado
		EstadoConvenio estadoConvenio = EstadoConvenio.getById(EstadoConvenio.ID_CANCELADO);
		convenio.setEstadoConvenio(estadoConvenio);
		GdeDAOFactory.getConvenioDAO().update(convenio);
		
		// Registrar el cambio de estado en el log de cambio
		ConEstCon conEstCon = new ConEstCon();
		conEstCon.setConvenio(convenio);
		conEstCon.setEstadoConvenio(estadoConvenio);
		conEstCon.setFechaConEstCon(convenio.ultimaFechaPago());
		conEstCon.setIdCaso(null);
		conEstCon.setObservacion("Cancelado en Proceso de Asentamiento "+this.getEjercicio().getDesEjercicio()+"/"+this.getServicioBanco().getCodServicioBanco()+"/"+this.getFechaBalance());
		GdeDAOFactory.getConEstConDAO().update(conEstCon);
		
		// Obtener la deuda incluida en el convenio. Y por cada Deuda:
		List<ConstanciaDeu> listConstanciaDeu = new ArrayList<ConstanciaDeu>();
		for(ConvenioDeuda convenioDeuda: convenio.getListConvenioDeuda()){
			Deuda deuda = convenioDeuda.getDeuda();
			Double importe = deuda.getSaldo();
			// Mover la Deuda a "Cancelada" (Por ahora solo actualizamos la deuda en deuAdmin o deudaJudicial y le cambiamos el estado a cancelada)
			EstadoDeuda estadoDeuda = EstadoDeuda.getById(EstadoDeuda.ID_CANCELADA);
			if(deuda.getRecurso().getEsAutoliquidable().intValue() == SiNo.NO.getId().intValue()){				
				deuda.setEstadoDeuda(estadoDeuda);
			}
			if(deuda.getActualizacion()!=null)
				deuda.setActualizacion(deuda.getActualizacion()+convenioDeuda.getActualizacion());
			else
				deuda.setActualizacion(convenioDeuda.getActualizacion());
			
			Date fechaPago = convenioDeuda.getFechaPago();
			try {
				if (fechaPago == null && transaccion != null) {
					// issue 5994: Se cortó el paso 3 del asentamiento de DReI 
					// lo que hacemos es si fecha queda null usamos la fecha de la transaccion.
					// y avisar en el log
					String s = String.format("Se evito generar un PagoDeuda con fechaPago null, " +
							" se usara la fecha de pago de la transaccion." +
							" Es probable que el convenio tenga ConvenioDeuda demas. " +
							" idConvenio: %s, nro %s, idTransaccion: %s, nroLinea: %s",
							convenio.getId(), convenio.getNroConvenio(), transaccion.getId(), transaccion.getNroLinea());
					AdpRun.currentRun().logError(s);

					fechaPago = transaccion.getFechaPago();
				}
			} catch (Exception e) {
				log.error("Fallo fix para manejar pagodeuda con fechapago null.", e);
			}
			
			
			deuda.setFechaPago(fechaPago);
			deuda.setSaldo(0D);
			deuda.setConvenio(null);
			if(deuda.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_ADMIN)
				GdeDAOFactory.getDeudaAdminDAO().update(deuda);
			else if(deuda.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL)
				GdeDAOFactory.getDeudaJudicialDAO().update(deuda);
			
			// Registrar el PagoDeuda
			PagoDeuda pagoDeuda = new PagoDeuda();
			pagoDeuda.setIdDeuda(deuda.getId());
			TipoPago tipoPago = TipoPago.getById(TipoPago.ID_PAGO_BUENO);
			pagoDeuda.setTipoPago(tipoPago);
			pagoDeuda.setIdPago(convenio.getId());
			pagoDeuda.setFechaPago(fechaPago);
			pagoDeuda.setImporte(importe);
			pagoDeuda.setActualizacion(convenioDeuda.getActEnPlan());
			pagoDeuda.setBancoPago(null);
			pagoDeuda.setIdCaso(null);
			pagoDeuda.setAsentamiento(this);
			pagoDeuda.setEsPagoTotal(SiNo.NO.getId());
			GdeDAOFactory.getPagoDeudaDAO().update(pagoDeuda);

			/* se decide no modificar mas las constancias durante las cancelaciones de deuda y convenio judiciales.
			 * esto es a raiz de la generacion masiva de constancias para TGI.
			// Si la Deuda se encuentra en Via Judicial, se debe actualizar la constancia.
			if(deuda.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL){
				ConstanciaDeu constanciaDeu = this.actualizarConstancia(deuda);
				if(constanciaDeu != null && !listConstanciaDeu.contains(constanciaDeu)){
					listConstanciaDeu.add(constanciaDeu);					
				}
			}
			*/
			
		}
		
		// Al finalizar recorro la lista de Constancias Deu actualizadas.
		/*
		for (ConstanciaDeu constanciaDeu: listConstanciaDeu) {
			if (!constanciaDeu.tieneConDeuDetActivos()) {
				constanciaDeu.cambiarEstConDeu(EstConDeu.ID_CANCELADA, "Detalle de Deuda totalmente cancelado durante el Asentamiento");
			} else {
				constanciaDeu.cambiarEstConDeu(EstConDeu.ID_MODIFICADA, "Detalle de Deuda parcialmente cancelado durante el Asentamiento");
			}
		}
		*/
	}
	
	
	/**
	 *  Arma y devuelve un String con todos los errores cargados en el Asentamiento.
	 * @return
	 */
	public String getStringError(){
		StringBuffer stringError = new StringBuffer();
		for(DemodaStringMsg error:this.getListError()){
			stringError.append(error.key().substring(1));
			stringError.append("\n");
		}
		return stringError.toString();
	}
	
	/**
	 *  Verifica si existe un Asentamiento para el Servicio Banco pasado con estado "En Preparacion", 
	 *  "En espera comenzar", "Procesando" o "En espera continuar". Retorna true o false.
	 *  (Excluyendo al Asentamiento que realiza la consulta)
	 * @param servicioBanco
	 * @return boolean
	 * @throws Exception
	 */
	public boolean existAsentamientoBySerBanForCreate(ServicioBanco servicioBanco) throws Exception{
		return BalDAOFactory.getAsentamientoDAO().existAsentamientoBySerBanForCreate(servicioBanco, this);
	}

	// Metodos sobre los mapas de Indices
	
	/**
	 *  Agrega un Indice al mapa correspondiente.
	 *  
	 *  @param idDeuda
	 */
	public void addAuxPagDeuToIndex(Long idDeuda) throws Exception{
		AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxPagDeuIndex().put(idDeuda.toString(), true);
	}
	
	/**
	 *  Verifica si existe el indice en el mapa correspondiente.
	 * @param idDeuda
	 * @return
	 */
	public boolean existAuxPagDeu(Long idDeuda) throws Exception{
		Boolean existe = AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxPagDeuIndex().get(idDeuda.toString());	
		if(existe != null){
			return existe.booleanValue();
		}
		return false;
	}

	/**
	 *  Agrega un Indice al mapa correspondiente.
	 *  
	 *  @param idConvenioCuota
	 */
	public void addAuxPagCuoToIndex(Long idConvenioCuota) throws Exception{
		AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxPagCuoIndex().put(idConvenioCuota.toString(), true);
	}
	
	/**
	 *  Verifica si existe el indice en el mapa correspondiente.
	 * @param idConvenioCuota
	 * @return
	 */
	public boolean existAuxPagCuo(Long idConvenioCuota) throws Exception{
		Boolean existe = AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxPagCuoIndex().get(idConvenioCuota.toString());
		if(existe != null){
			return existe.booleanValue();
		}
		return false;
	}

	/**
	 *  Agrega un Indice al mapa correspondiente.
	 *  
	 *  @param idRecibo
	 */
	public void addAuxPagRecToIndex(Long idRecibo) throws Exception{
		AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxPagRecIndex().put(idRecibo.toString(), true);
	}
	
	/**
	 *  Verifica si existe el indice en el mapa correspondiente.
	 * @param idRecibo
	 * @return
	 */
	public boolean existAuxPagRec(Long idRecibo) throws Exception{
		Boolean existe = AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxPagRecIndex().get(idRecibo.toString());
		if(existe != null){
			return existe.booleanValue();
		}
		return false;
	}

	/**
	 *  Agrega un Indice al mapa correspondiente.
	 *  
	 *  @param idReciboConvenio
	 */
	public void addAuxPagRecConToIndex(Long idReciboConvenio) throws Exception{
		AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxPagRecConIndex().put(idReciboConvenio.toString(), true);
	}
	
	/**
	 *  Verifica si existe el indice en el mapa correspondiente.
	 * @param idReciboConvenio
	 * @return
	 */
	public boolean existAuxPagRecCon(Long idReciboConvenio) throws Exception{
		Boolean existe = AsentamientoCache.getInstance().getSession(this.getId()).getMapAuxPagRecConIndex().get(idReciboConvenio.toString());
		if(existe != null){
			return existe.booleanValue();
		}
		return false;
	}
	
	/**
	 *  Verifica si existe la pila de ConvenioDeuda con saldo mayor que cero para el idConvenio pasado
	 *  en el mapStackConDeu.
	 * @param idConvenio
	 * @return
	 */
	public boolean existStackConDeu(Long idConvenio) throws Exception{
		List<ConvenioDeuda> stackConDeu = AsentamientoCache.getInstance().getSession(this.getId()).getMapStackConDeu().get(idConvenio.toString());
		return (stackConDeu!=null); 	
	}
	
	/**
	 *  Guarda la lista de ConvenioDeuda en el mapa de la session del Asentamiento.
	 *  
	 * @param idConvenio
	 * @param listConvenioDeuda
	 * @throws Exception
	 */
	public void putStackConDeu(Long idConvenio, List<ConvenioDeuda> listConvenioDeuda) throws Exception{
		AsentamientoCache.getInstance().getSession(this.getId()).getMapStackConDeu().put(idConvenio.toString(),listConvenioDeuda);		
	}
	
	/**
	 *  Insterta el ConvenioDeuda al final de la lista de ConveniosDeuda guardadas en el mapStackConDeu
	 * 	(Si no existe la lista en el mapa, la crea)
	 * 
	 * @param idConvenio
	 * @param convenioDeuda
	 */
	public ConvenioDeuda pushIntoStackConDeu(Long idConvenio, ConvenioDeuda convenioDeuda) throws Exception{
		return AsentamientoCache.getInstance().getSession(this.getId()).pushIntoStackConDeu(idConvenio, convenioDeuda);
	}
	
	/**
	 *  Obtiene y remueve el ConvenioDeuda del final de la lista de ConveniosDeuda guardadas en el mapStackConDeu
	 * 	(Si no existe la lista en el mapa o esta vacia retorna null);
	 * 
	 * @param idConvenio
	 */
	public ConvenioDeuda popFromStackConDeu(Long idConvenio) throws Exception{
		return AsentamientoCache.getInstance().getSession(this.getId()).popFromStackConDeu(idConvenio);
	}	
	
	/**
	 * Elimina todas las tablas auxiliares utilizadas en el Asentamiento.
	 * (Solo se llama al finalizar correctamente el ultimo paso)
	 *
	 */
	public void eliminarTablasTemporales(){
		// Eliminar las tablas temporales asociadas al Asentamiento:
		// auxDeuMdf, auxConDeu, auxConDeuCuo, auxPagCuo, auxPagDeu, auxRecaudado, auxSellado, auxConvenio, sinPartida,
		// sinIndet, sinSalAFav, transaccion y auxImpRec.
		BalDAOFactory.getAuxDeuMdfDAO().deleteAllByAsentamiento(this);
		BalDAOFactory.getAuxRecaudadoDAO().deleteAllByAsentamiento(this);
		BalDAOFactory.getAuxSelladoDAO().deleteAllByAsentamiento(this);
		BalDAOFactory.getAuxPagDeuDAO().deleteAllByAsentamiento(this);
		BalDAOFactory.getAuxPagCuoDAO().deleteAllByAsentamiento(this);
		BalDAOFactory.getAuxConvenioDAO().deleteAllByAsentamiento(this);
		BalDAOFactory.getAuxConDeuDAO().deleteAllByAsentamiento(this);
		BalDAOFactory.getAuxConDeuCuoDAO().deleteAllByAsentamiento(this);
		BalDAOFactory.getSinPartidaDAO().deleteAllByAsentamiento(this);
		BalDAOFactory.getSinIndetDAO().deleteAllByAsentamiento(this);
		BalDAOFactory.getSinSalAFavDAO().deleteAllByAsentamiento(this);
		BalDAOFactory.getTransaccionDAO().deleteAllByAsentamiento(this);
		BalDAOFactory.getAuxImpRecDAO().deleteAllByAsentamiento(this);
	}
	
	@Override
	public String infoString() {
		String ret= "Asentamiento ";
		
		if(usuarioAlta!=null){
			ret +=" - generado por: "+usuarioAlta;
		}
		if(fechaBalance!=null){
			ret += " - con fecha de Balance: " + DateUtil.formatDate(getFechaBalance(), DateUtil.ddSMMSYYYY_MASK);
		}
		if(ejercicio!=null){
			ret += " - para el Ejercicio: "+ejercicio.getDesEjercicio();
		}
		if(servicioBanco!=null){
			ret += " - con Servicio Banco: "+servicioBanco.getDesServicioBanco();
		}
		if(idCaso!=null){
			ret +=" - para el caso: "+idCaso;
		}
		if(corrida!=null){
			ret += " - para la corrida: "+corrida.getDesCorrida();
		}
		if(observacion!=null){
			ret +=" - con la siguiente observacion: "+observacion;
		}
		
		return ret;
	}

	/**
	 * 	Log Detallado del Proceso. Solo logea si se habilit'o el modo "Log Detallado" desde el Administrador del Proceso.
	 * 
	 * @param log
	 * @throws Exception
	 */
	public void logDetallado(String log) throws Exception{
		if(AsentamientoCache.getInstance().getSession(this.getId()).isLogDetalladoEnabled())
			AdpRun.currentRun().logDebug(new Date()+" - "+log);
	}
	
	
	/**
	 * 	Log de Estadisticas y Tiempos del Proceso. Solo logea si se habilit'o el modo "Log Stats" desde el codigo (por ahora).
	 * 
	 * @param log
	 * @throws Exception
	 */
	public void logStats(String log) throws Exception{
		if(AsentamientoCache.getInstance().getSession(this.getId()).isLogStatsEnabled())
			AdpRun.currentRun().logDebug(log);
	}
	
	/**
	 * Obtener y Procesar Transacciones SIAT. Con Paginacion de las Transacciones. 
	 * Por cada transaccion se obtiene la transaccion SIAT, y procesa la deuda, recibo deuda, cuota o recibo cuota
	 * segun corresponda.
	 * 	
	 * <i>(paso 3.1)</i>
	 */
	public void asentarTransaccionesPaginado() throws Exception{
		Integer firstResult = 0;
		Integer maxResults = 100;
		// Iterar la lista de Transacciones del Asentamiento de manera paginada
		boolean contieneTransacciones = true;
		long cantTxDec = 0; 
		while (contieneTransacciones){
			
			// Obtiene la lista de Transacciones.
			long queryTime = System.currentTimeMillis();
			List<Transaccion> listTransaccionPag = this.getListTransaccion(firstResult, maxResults);				
			queryTime = System.currentTimeMillis() - queryTime;
			AsentamientoCache.getInstance().getSession(this.getId()).addStats("getListTransaccion paginada de a "+maxResults+" transacciones.", queryTime);
		
			contieneTransacciones = (listTransaccionPag.size() > 0);

			if(contieneTransacciones){
				this.asentarTransacciones(listTransaccionPag);
				firstResult += maxResults; // Incremento el indice del 1er registro
			}
			if(this.hasError()){
				SiatHibernateUtil.closeSession();
				return;
			}
			cantTxDec++;
			listTransaccionPag = null;
			if(cantTxDec%1==0){
				SiatHibernateUtil.closeSession();
				SiatHibernateUtil.currentSession();
				SiatHibernateUtil.currentSession().refresh(this);
				AdpRun.changeRunMessage(firstResult+" Transacciones asentadas.", 0);
			}
		}
		
		SiatHibernateUtil.closeSession();
		
	}

	
	/**
	 * Asentar Transacciones SIAT.
	 * Por cada transaccion se asienta la deuda, recibo deuda, cuota o recibo cuota
	 * segun corresponda.
	 * 	
	 * <i>(paso 3.1)</i>
	 */
	public void asentarTransacciones(List<Transaccion> listTransaccionPag) throws Exception{

		Session session = null;
		
		if(ListUtil.isNullOrEmpty(listTransaccionPag)){
			this.addRecoverableValueError("No existen Transacciones para este proceso de asentamiento: ");
			return;
		}
		for(Transaccion transaccion: listTransaccionPag){
			long txTime = System.currentTimeMillis();
			if(transaccion.getEstaAsentada() != null && transaccion.getEstaAsentada().intValue() == SiNo.SI.getId().intValue()){
				AdpRun.currentRun().logDebug("Transaccion Nro: "+transaccion.getNroLinea()+" ya fue asentada por el proceso de asentamiento. (Se ignora)");
				continue;
			}
			if(transaccion.getEsIndet() != null && transaccion.getEsIndet().intValue() == SiNo.SI.getId().intValue()){
				AdpRun.currentRun().logDebug("Transaccion Nro: "+transaccion.getNroLinea()+" fue procesada como Indeterminado. (Se ignora)");
				continue;
			}
			session = SiatHibernateUtil.currentSession();
			session.beginTransaction();			
			try{				 
				if(transaccion.esDeuda()){
					AuxPagDeu auxPagDeu = AuxPagDeu.getByAsentamientoYTransaccion(this, transaccion);
					this.asentarDeuda(auxPagDeu);
					if(transaccion.hasError()){
						transaccion.addErrorMessages(this);
					}else{
						transaccion.setEstaAsentada(SiNo.SI.getId());
						this.updateTransaccion(transaccion);
					}
				}else if(transaccion.esReciboDeuda()){
					List<AuxPagDeu> listAuxPagDeu = AuxPagDeu.getListByAsentamientoYTransaccion(this, transaccion);
					this.asentarReciboDeuda(listAuxPagDeu);
					if(transaccion.hasError()){
						transaccion.addErrorMessages(this);
					}else{
						transaccion.setEstaAsentada(SiNo.SI.getId());
						this.updateTransaccion(transaccion);
					}
				}else if(transaccion.esCuota()){
					AuxPagCuo auxPagCuo = AuxPagCuo.getByAsentamientoYTransaccion(this, transaccion);
					List<AuxConDeu> listAuxConDeu = AuxConDeu.getListByAsentamientoYTransaccion(this, transaccion);
					List<AuxConDeuCuo> listAuxConDeuCuo = AuxConDeuCuo.getListByAsentamientoYTransaccion(this, transaccion);
					this.asentarConvenioDeuda(listAuxConDeu);
					this.asentarConDeuCuo(listAuxConDeuCuo);
					this.asentarCuota(auxPagCuo);
					if(transaccion.hasError()){
						transaccion.addErrorMessages(this);
					}else{
						transaccion.setEstaAsentada(SiNo.SI.getId());
						this.updateTransaccion(transaccion);
					}
				}else if(transaccion.esReciboCuota()){
					List<AuxPagCuo> listAuxPagCuo = AuxPagCuo.getListByAsentamientoYTransaccion(this, transaccion);
					List<AuxConDeu> listAuxConDeu = AuxConDeu.getListByAsentamientoYTransaccion(this, transaccion);
					List<AuxConDeuCuo> listAuxConDeuCuo = AuxConDeuCuo.getListByAsentamientoYTransaccion(this, transaccion);
					this.asentarConvenioDeuda(listAuxConDeu);
					this.asentarConDeuCuo(listAuxConDeuCuo);
					this.asentarReciboCuota(listAuxPagCuo);
			
					if(transaccion.hasError()){
						transaccion.addErrorMessages(this);
					}else{
						transaccion.setEstaAsentada(SiNo.SI.getId());
						this.updateTransaccion(transaccion);
					}
				}
			}catch(Exception e){
				log.error("Service Error en Transaccion de Id "+transaccion.getId()+" y Nro de Linea "+transaccion.getNroLinea()+": "+e.toString(), e);
				AdpRun.currentRun().logDebug("Service Error en Transaccion de Id "+transaccion.getId()+" y Nro de Linea "+transaccion.getNroLinea()+": "+e.toString(), e);
				this.addRecoverableValueError("Service Error en Transaccion de Id "+transaccion.getId()+" y Nro de Linea "+transaccion.getNroLinea()+": "+e.toString());
			}finally{
				if(!this.hasError()){
					session.getTransaction().commit();				
				}else{
					session.getTransaction().rollback();
					this.addRecoverableValueError("Error al Asentar Transaccion de Id "+transaccion.getId()+" y Nro de Linea "+transaccion.getNroLinea());
					return;
				}				
			}
			txTime = System.currentTimeMillis() - txTime;
			AsentamientoCache.getInstance().getSession(this.getId()).addStats("Asentar Transaccion", txTime);
			//this.logStats("<*> Tiempo consumido al asentar transaccion nro "+transaccion.getNroLinea()+": "+txTime+" ms <*>");
		}
		
	}
	
	
	/**
	 * Asentar Transaccion de Deuda.
	 * 	
	 * <i>(paso 3.1.1)</i>
	 */
	public void asentarDeuda(AuxPagDeu auxPagDeu) throws Exception {

			// Asentamos la transaccion de deuda procesada como pago bueno en el paso anterior.
			Deuda deuda = Deuda.getById(auxPagDeu.getIdDeuda());
			if(deuda == null){
				this.addRecoverableValueError("Transacción de Id: "+auxPagDeu.getTransaccion().getId()+". No se encontro la deuda con id "+auxPagDeu.getIdDeuda()+" registrada en el paso anterior como pago bueno.");
				return;							
			}
			// Validar el Estado Deuda
			if((deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_CANCELADA) 
					|| (deuda.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_ANULADA)){
				this.addRecoverableValueError("Transacción de Id: "+auxPagDeu.getTransaccion().getId()+". La Deuda a asentar se encuentra en estado "
						+deuda.getEstadoDeuda().getDesEstadoDeuda()+" y no puede ser asentada.");
				return;			
			}
			// Validar si la Deuda se encuentra en un Convenio.
			Convenio convenio = deuda.getConvenio(); 
			if(convenio != null){
				if(convenio.getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_VIGENTE){
					this.addRecoverableValueError("Transacción de Id: "+auxPagDeu.getTransaccion().getId()+". La Deuda a asentar se encuentra en el Convenio "+convenio.getNroConvenio()+" y no puede ser asentada.");
					return;																												
				}
			}
			// Se verifica el tipo de cancelacion. Si corresponde a un pago parcial no se cambia el estado.
			if(auxPagDeu.getTransaccion().getTipoCancelacion() == null || auxPagDeu.getTransaccion().getTipoCancelacion().intValue() != Transaccion.TIPO_CANCELACION_PARCIAL.intValue()){
				// Caso normal de pago de deuda y caso de pago por menos:
			
				// Mover la Deuda a "Cancelada" (Por ahora solo actualizamos la deuda en deuAdmin o deudaJudicial y le cambiamos el estado a cancelada)
				EstadoDeuda estadoDeuda = AsentamientoCache.getInstance().getEstadoDeudaById(EstadoDeuda.ID_CANCELADA);
				if(deuda.getRecurso().getEsAutoliquidable().intValue() == SiNo.NO.getId().intValue()){
					deuda.setEstadoDeuda(estadoDeuda);				
				}
			}
			
			// Se verifica el tipo de cancelacion. Si corresponde a un pago parcial, por menos o normal para setear importe, actualizacion y fecha pago.
			if(auxPagDeu.getTransaccion().getTipoCancelacion() != null && auxPagDeu.getTransaccion().getTipoCancelacion().intValue() == Transaccion.TIPO_CANCELACION_PARCIAL.intValue()){
				// Caso de pago parcial de Deuda
				Double capitalPago = auxPagDeu.getTransaccion().getImporte() - auxPagDeu.getActualizacion();
				deuda.setSaldo(NumberUtil.truncate(deuda.getSaldo()-capitalPago,SiatParam.DEC_IMPORTE_DB));
			}else if(auxPagDeu.getTransaccion().getTipoCancelacion() != null && auxPagDeu.getTransaccion().getTipoCancelacion().intValue() == Transaccion.TIPO_CANCELACION_POR_MENOS.intValue()){
				// Caso de cancelacion de deuda por menor importe
				deuda.setActualizacion(NumberUtil.truncate(auxPagDeu.getActualizacion(),SiatParam.DEC_IMPORTE_DB)); 
				deuda.setFechaPago(auxPagDeu.getTransaccion().getFechaPago());
				deuda.setSaldo(0D);								
			}else{
				// Caso normal de pago de deuda:
				deuda.setActualizacion(NumberUtil.truncate(auxPagDeu.getActualizacion(),SiatParam.DEC_IMPORTE_DB));
				deuda.setFechaPago(auxPagDeu.getTransaccion().getFechaPago());
				deuda.setSaldo(0D);
			}
			
			// Si el campo reclamada tiene valor correspondiente a "En Asentamiento" lo cambiamos a cero.
			if(deuda.getEstaEnAsentamiento()){
				deuda.setReclamada(0);
			}
			
			if(deuda.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_ADMIN)
				GdeDAOFactory.getDeudaAdminDAO().update(deuda);
			else if(deuda.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL)
				GdeDAOFactory.getDeudaJudicialDAO().update(deuda);
			
			// Registrar el PagoDeuda
			PagoDeuda pagoDeuda = new PagoDeuda();
			pagoDeuda.setIdDeuda(deuda.getId());
			pagoDeuda.setFechaPago(auxPagDeu.getTransaccion().getFechaPago());
			Banco bancoPago = AsentamientoCache.getInstance().getBancoByCod(auxPagDeu.getTransaccion().getCaja().toString()) ;
			if(bancoPago == null){
				AdpRun.currentRun().logDebug("No se encontró el Banco con código "+auxPagDeu.getTransaccion().getCaja().toString()+" en Siat. Se carga un Banco Generico.");
				bancoPago = AsentamientoCache.getInstance().getBancoByCod(Banco.COD_BANCO_GENERICO); 
			}
			pagoDeuda.setBancoPago(bancoPago);
			pagoDeuda.setIdCaso(null);
			pagoDeuda.setAsentamiento(this);

			TipoPago tipoPago = null;
			Double importe = 0D;
			Double actualizacion = 0D;
			// Si la deuda no proviene de un recibo
			if(auxPagDeu.getRecibo()==null){
				/*
				 Se cambia la forma de detectar si la deuda se paga vencida o no. Se hacia comparando la actualización
				 , pero esto es incorrecto cuando la actualización es cero por diferencias en el pago y dentro de la 
				 tolerancia de los 0.5 $. Se cambia para averiguarlo con las fechas de pago y vencimiento.
				 */
				if(DateUtil.isDateAfter(auxPagDeu.getTransaccion().getFechaPago(), deuda.getFechaVencimiento())){
					Recurso recurso = AsentamientoCache.getInstance().getRecursoById(deuda.getRecurso().getId());
					Date fechaVencimiento = recurso.obtenerFechaVencimientoDeudaORecibo(deuda.getFechaVencimiento(),AsentamientoCache.getInstance().getMapFeriado());
					if(DateUtil.isDateAfter(auxPagDeu.getTransaccion().getFechaPago(), fechaVencimiento)){
						tipoPago = AsentamientoCache.getInstance().getTipoPagoById(TipoPago.ID_BOLETA_VENCIDA);						
					}else{
						tipoPago = AsentamientoCache.getInstance().getTipoPagoById(TipoPago.ID_BOLETA_NO_VENCIDA);
					}
				}else{
					tipoPago = AsentamientoCache.getInstance().getTipoPagoById(TipoPago.ID_BOLETA_NO_VENCIDA);
				}
				// Para el caso de cancelacion por menos se usa el tipo de pago 'Compensacion'
				if(auxPagDeu.getTransaccion().getTipoCancelacion() != null && auxPagDeu.getTransaccion().getTipoCancelacion().intValue() == Transaccion.TIPO_CANCELACION_POR_MENOS.intValue()){
					tipoPago = AsentamientoCache.getInstance().getTipoPagoById(TipoPago.ID_EXPEDIENTE_COMPENSACION);
				}
				//Para el caso de transaccion osiris se usa el tipo de pago 'AFIP'
				if(auxPagDeu.getTransaccion().getOrigen() != null && auxPagDeu.getTransaccion().getOrigen().intValue() == Transaccion.ORIGEN_OSIRIS.intValue()){
					tipoPago = AsentamientoCache.getInstance().getTipoPagoById(TipoPago.ID_PAGO_AFIP);
				}
				
				pagoDeuda.setIdPago(deuda.getId());
				importe  = auxPagDeu.getTransaccion().getImporte()-auxPagDeu.getActualizacion();
				actualizacion = auxPagDeu.getActualizacion();
				// Se verifica el tipo de cancelacion. Si corresponde a un pago parcial.
				if(auxPagDeu.getTransaccion().getTipoCancelacion() != null && auxPagDeu.getTransaccion().getTipoCancelacion().intValue() == Transaccion.TIPO_CANCELACION_PARCIAL.intValue()){
					pagoDeuda.setEsPagoTotal(SiNo.NO.getId());
				}else{
					// Caso normal de pago de deuda y caso de pago por menos:
					pagoDeuda.setEsPagoTotal(SiNo.SI.getId());					
				}

			// Si la deuda proviene de un recibo
			}else{
				tipoPago = AsentamientoCache.getInstance().getTipoPagoById(TipoPago.ID_RECIBO_DE_RECONFECCION);
				pagoDeuda.setIdPago(auxPagDeu.getRecibo().getId());
				// Si estamos en un caso de Recurso Autoliquidable y Recibo de boleta en blanco: Tomamos la actualizacion de la deuda.
				if(auxPagDeu.getRecibo().getRecurso().getEsAutoliquidable().intValue() == SiNo.SI.getId().intValue()
								&& NumberUtil.isDoubleEqualToDouble(auxPagDeu.getRecibo().getTotImporteRecibo(),0.0,0.001)){
				
					importe  = auxPagDeu.getTransaccion().getImporte()-auxPagDeu.getActualizacion();
					actualizacion = auxPagDeu.getActualizacion();
					pagoDeuda.setEsPagoTotal(SiNo.SI.getId());
				}else{
					ReciboDeuda reciboDeuda = ReciboDeuda.getByReciboYDeuda(auxPagDeu.getRecibo(), deuda);
					//XXX: código temporal para zafar
					if (null == reciboDeuda && auxPagDeu.getRecibo().getEsVolPagIntRS().intValue() == SiNo.SI.getId().intValue()) {
						reciboDeuda = auxPagDeu.getRecibo().getListReciboDeuda().get(0);
					}
					
					if(reciboDeuda == null){
						this.addRecoverableValueError("Transacción de Id: "+auxPagDeu.getTransaccion().getId()+". No se encontró la relacion entre el Recibo nro "+auxPagDeu.getRecibo().getNroRecibo()+" con la deuda.");
						return;	
					}
					importe  = reciboDeuda.getCapitalOriginal()-reciboDeuda.getDesCapitalOriginal();
					actualizacion = reciboDeuda.getTotActualizacion();
					if(auxPagDeu.getTransaccion().getImporte().equals(deuda.getImporte()))
						pagoDeuda.setEsPagoTotal(SiNo.SI.getId());
					else
						pagoDeuda.setEsPagoTotal(SiNo.NO.getId());
				}
				
			}
			pagoDeuda.setTipoPago(tipoPago);
			pagoDeuda.setImporte(NumberUtil.truncate(importe,SiatParam.DEC_IMPORTE_DB));
			pagoDeuda.setActualizacion(NumberUtil.truncate(actualizacion,SiatParam.DEC_IMPORTE_DB));
			
			GdeDAOFactory.getPagoDeudaDAO().update(pagoDeuda);

			// Grabar la fecha de Pago en el Recurso de la Deuda (si es mayor al que esta grabado)
			Recurso recurso = deuda.getRecurso();
			Date fecUltPag = recurso.getFecUltPag(); 
			if((fecUltPag == null	|| DateUtil.isDateAfter(pagoDeuda.getFechaPago(), fecUltPag)) && auxPagDeu.getTransaccion().getCaja().longValue() != 69){
				recurso.setFecUltPag(pagoDeuda.getFechaPago());
				DefDAOFactory.getRecursoDAO().update(recurso);
			}
			
			// Si la Deuda se encuentra en Via Judicial, se debe actualizar la constancia.
			/*
			if(deuda.getViaDeuda().getId().longValue() == ViaDeuda.ID_VIA_JUDICIAL){
				ConstanciaDeu constanciaDeu = this.actualizarConstancia(deuda);
				if(constanciaDeu != null && constanciaDeu.getEstConDeu().getId().longValue() != EstConDeu.ID_CANCELADA){
					if (!constanciaDeu.tieneConDeuDetActivos()) {
						constanciaDeu.cambiarEstConDeu(EstConDeu.ID_CANCELADA, "Detalle de Deuda totalmente cancelado durante el Asentamiento");
					} else {
						constanciaDeu.cambiarEstConDeu(EstConDeu.ID_MODIFICADA, "Detalle de Deuda parcialmente cancelado durante el Asentamiento");
					}
				}
			}
			*/
			
			// Llamo al metodo realizarTareasComplementarias() de la transaccion que se utiliza para acciones 
			// adicionales que depende del servicio banco.
			auxPagDeu.getTransaccion().realizarTareasComplementarias(deuda);
		
	}
	
	/**
	 * Asentar Transaccion de Recibos de Deuda.
	 * 	
	 * <i>(paso 3.1.2)</i>
	 */
	public void asentarReciboDeuda(List<AuxPagDeu> listAuxPagDeu) throws Exception {
		Transaccion transaccion = null; 
		Recibo recibo = null;
		
		if(ListUtil.isNullOrEmpty(listAuxPagDeu)){
			return;
		}
		for(AuxPagDeu auxPagDeu: listAuxPagDeu){
			this.asentarDeuda(auxPagDeu);
		}
		
		recibo = listAuxPagDeu.get(0).getRecibo();
		transaccion = listAuxPagDeu.get(0).getTransaccion();
		recibo.setFechaPago(transaccion.getFechaPago());
		Banco bancoPago = AsentamientoCache.getInstance().getBancoByCod(transaccion.getCaja().toString());
		if(bancoPago == null){
			bancoPago = AsentamientoCache.getInstance().getBancoByCod(Banco.COD_BANCO_GENERICO);
		}
		recibo.setBancoPago(bancoPago);
		recibo.setAsentamiento(this);
		
		GdeDAOFactory.getReciboDAO().update(recibo);

	}

	/**
	 * Asentar Transaccion de Cuotas.
	 * 	
	 * <i>(paso 3.1.3)</i>
	 */
	public void asentarCuota(AuxPagCuo auxPagCuo) throws Exception {
		// Asentar la transaccion de cuota procesada como pago bueno en el paso anterior.
		ConvenioCuota convenioCuota = auxPagCuo.getConvenioCuota();
		
		// Validar el Estado Cuota
		if(convenioCuota.getEstadoConCuo().getId().longValue() == EstadoConCuo.ID_PAGO_A_CUENTA 
				|| convenioCuota.getEstadoConCuo().getId().longValue() == EstadoConCuo.ID_PAGO_BUENO){
			this.addRecoverableValueError("Transacción de Id: "+auxPagCuo.getTransaccion().getId()+". La Cuota a asentar se encuentra en estado "
					+convenioCuota.getEstadoConCuo().getDesEstadoConCuo()+" y no puede ser asentada.");
			return;			
		}
		// Validar el Estado del Convenio.
		Convenio convenio = convenioCuota.getConvenio(); 
		if((convenio.getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_CANCELADO) 
				|| (convenio.getEstadoConvenio().getId().longValue() == EstadoConvenio.ID_RECOMPUESTO)){
			this.addRecoverableValueError("Transacción de Id: "+auxPagCuo.getTransaccion().getId()+". La Cuota a asentar se encuentra en el Convenio "+convenio.getNroConvenio()+" con estado "+convenio.getEstadoConvenio().getDesEstadoConvenio()+" y no puede ser asentada.");
			return;																												
		}
		
		Double importePagoCuota = convenioCuota.getImporteCuota();
		//Valido si la cuota proviene de un recibo de Cuota Saldo
		if(auxPagCuo.getReciboConvenio()!=null && auxPagCuo.getReciboConvenio().getEsCuotaSaldo()== SiNo.SI.getId()){
			//Obtengo el RecConCuo para la cuota
			RecConCuo recConCuo = GdeDAOFactory.getRecConCuoDAO().getByReciboConvenioYConvenioCuota(auxPagCuo.getReciboConvenio(), convenioCuota);
			importePagoCuota = recConCuo.getTotCapitalOriginal()+recConCuo.getTotIntFin();
		}
		
		// Actualizar la Cuota
		convenioCuota.setEstadoConCuo(auxPagCuo.getEstadoConCuo());
		convenioCuota.setActualizacion(NumberUtil.truncate(auxPagCuo.getActualizacion(),SiatParam.DEC_IMPORTE_DB));
		convenioCuota.setFechaPago(auxPagCuo.getTransaccion().getFechaPago());
		convenioCuota.setNroCuotaImputada(auxPagCuo.getNroCuotaImputada());
		convenioCuota.setImportePago(NumberUtil.truncate(convenioCuota.getActualizacion()+importePagoCuota,SiatParam.DEC_IMPORTE_DB));
		
		Banco bancoPago = AsentamientoCache.getInstance().getBancoByCod(auxPagCuo.getTransaccion().getCaja().toString());
		if(bancoPago == null){
			bancoPago = AsentamientoCache.getInstance().getBancoByCod(Banco.COD_BANCO_GENERICO);
			AdpRun.currentRun().logDebug("No se encontró el Banco con código "+auxPagCuo.getTransaccion().getCaja().toString()+" en Siat. Se carga un Banco Generico.");
		}
		convenioCuota.setBancoPago(bancoPago);
		convenioCuota.setAsentamiento(this);
		
		TipoPago tipoPago = null;
		// Si la cuota no proviene de un recibo
		if(auxPagCuo.getReciboConvenio()==null){
			tipoPago = AsentamientoCache.getInstance().getTipoPagoById(TipoPago.ID_RECIBO_CUOTA);
			convenioCuota.setIdPago(convenioCuota.getId());
			// Si la cuota proviene de un recibo
		}else{
			tipoPago = AsentamientoCache.getInstance().getTipoPagoById(TipoPago.ID_RECIBO_RECONFECCION_CUOTA);
			convenioCuota.setIdPago(auxPagCuo.getReciboConvenio().getId());
		}
		convenioCuota.setTipoPago(tipoPago);
		
		GdeDAOFactory.getConvenioCuotaDAO().update(convenioCuota);
		
		// Si el NroCuotaImputada es Mayor que el valor del Campo UltCuoImp del Convenio, actualizar dicho campo.			
		if(auxPagCuo.getNroCuotaImputada()!= null && auxPagCuo.getNroCuotaImputada().intValue()>convenio.getUltCuoImp().intValue()){
			convenio.setUltCuoImp(auxPagCuo.getNroCuotaImputada());
			GdeDAOFactory.getConvenioDAO().update(convenio);
		}
		
		int validacionEstadoCuotas = convenio.validarEstadoCuotas();
		// Si el Estado de la Cuota es "Pago Bueno", validar si todas las Cuotas del Convenio tienen estado "Pago Bueno".
		// si es asi Cancelar Convenio.
		if(validacionEstadoCuotas == 0){
			this.cancelarConvenio(convenio, auxPagCuo.getTransaccion());
		// Si se pagaron todas las cuotas, validar si al menos una tiene estado "Pago A Cuenta".
		// si es asi Enviar Solicitud para revision de caso.
		}else if(validacionEstadoCuotas == 1){
			String descripcion = "El Convenio "+convenio.getNroConvenio()+" de la Cuenta "+convenio.getCuenta().getNumeroCuenta()
			+" se terminó de Pagar pero no se canceló porque tiene Pagos a Cuenta. Asentamiento "
			+this.getEjercicio().getDesEjercicio()+"/"+this.getServicioBanco().getCodServicioBanco()+"/"
			+this.getFechaBalance(); 
			CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_VERIFICAR_CONVENIO_PAGO_PAGOS_A_CUENTA, 
					"Verificar Convenio Pago", descripcion,convenio.getCuenta());		
		}
		
		// Si el Estado de la Cuota es "Pago Bueno", validar si todas las Cuotas del Convenio tienen estado "Pago Bueno".
		// si es asi Cancelar Convenio.
		/*if(convenioCuota.getEstadoConCuo().getId().longValue()==EstadoConCuo.ID_PAGO_BUENO && convenio.tieneTodasCuotasPagosBuenos()){
			this.cancelarConvenio(convenio);
		// Si se pagaron todas las cuotas, validar si al menos una tiene estado "Pago A Cuenta".
		// si es asi Enviar Solicitud para revision de caso.
		}else if(convenio.pagoConCuotasPagosACuenta()){
				String descripcion = "El Convenio "+convenio.getNroConvenio()+" de la Cuenta "+convenio.getCuenta().getNumeroCuenta()
				+" se terminó de Pagar pero no se canceló porque tiene Pagos a Cuenta. Asentamiento "
				+this.getEjercicio().getDesEjercicio()+"/"+this.getServicioBanco().getCodServicioBanco()+"/"
				+this.getFechaBalance(); 
				CasSolicitudManager.getInstance().createSolicitud(TipoSolicitud.COD_VERIFICAR_CONVENIO_PAGO_PAGOS_A_CUENTA, 
						"Verificar Convenio Pago", descripcion,convenio.getCuenta());		
		}*/
			
	}

	/**
	 * Asenta ConvenioDeuda.
	 * 	
	 * <i>(paso 3.1.3.2)</i>
	 */
	public void asentarConvenioDeuda(List<AuxConDeu> listAuxConDeu) throws Exception {	
		// -> Registrar Deuda:
		for(AuxConDeu auxConDeu: listAuxConDeu){
			ConvenioDeuda convenioDeuda = auxConDeu.getConvenioDeuda();
			convenioDeuda.setSaldoEnPlan(NumberUtil.truncate(auxConDeu.getSaldoEnPlan(),SiatParam.DEC_IMPORTE_DB));
			convenioDeuda.setFechaPago(auxConDeu.getFechaPago());
			GdeDAOFactory.getConvenioDeudaDAO().update(convenioDeuda);
		}
	}
	
	/**
	 * Asentar ConvenioDeuda.
	 * 	
	 * <i>(paso 3.1.3.3)</i>
	 */
	public void asentarConDeuCuo(List<AuxConDeuCuo> listAuxConDeuCuo) throws Exception {	
		// -> Registrar Relacion Cuota/Deuda
		for(AuxConDeuCuo auxConDeuCuo: listAuxConDeuCuo){
			ConDeuCuo conDeuCuo = new ConDeuCuo();
			conDeuCuo.setConvenioCuota(auxConDeuCuo.getConvenioCuota());
			conDeuCuo.setConvenioDeuda(auxConDeuCuo.getConvenioDeuda());
			conDeuCuo.setSaldoEnPlanCub(NumberUtil.truncate(auxConDeuCuo.getSaldoEnPlanCub(),SiatParam.DEC_IMPORTE_DB));
			conDeuCuo.setEsPagoTotal(auxConDeuCuo.getEsPagoTotal());
			GdeDAOFactory.getConDeuCuoDAO().update(conDeuCuo);
		}
	}
	
	/**
	 * Asentar Transaccion de Recibo de Cuota.
	 * 	
	 * <i>(paso 3.1.4)</i>
	 */
	public void asentarReciboCuota(List<AuxPagCuo> listAuxPagCuo) throws Exception {		
	
		ReciboConvenio reciboConvenio = null;
		Transaccion transaccion = null;
		if(ListUtil.isNullOrEmpty(listAuxPagCuo))
			return;
		// Asentar las Cuotas incluidas en el Recibo de Cuota
		for(AuxPagCuo auxPagCuo: listAuxPagCuo){
			this.asentarCuota(auxPagCuo);
		}
		
		reciboConvenio = listAuxPagCuo.get(0).getReciboConvenio();
		transaccion = listAuxPagCuo.get(0).getTransaccion();
		
		reciboConvenio.setFechaPago(transaccion.getFechaPago());
		Banco bancoPago = AsentamientoCache.getInstance().getBancoByCod(transaccion.getCaja().toString());
		if(bancoPago == null){
			bancoPago = AsentamientoCache.getInstance().getBancoByCod(Banco.COD_BANCO_GENERICO);
			AdpRun.currentRun().logDebug("No se encontró el Banco con código "+transaccion.getCaja().toString()+" en Siat. Se carga un Banco Generico.");
		}
		reciboConvenio.setBancoPago(bancoPago);
		reciboConvenio.setAsentamiento(this);

		GdeDAOFactory.getReciboConvenioDAO().update(reciboConvenio);
		
	}

	/**
	 * Grabar Saldos, Indeterminados y Movimientos a Partida en SIAT (bal_diarioPartida, bal_indet, bal_saldoAFavor).
	 * (Ademas se crea un DiarioPartida para descargar el importe recaudado de la partida de Cuenta Puente) 
	 * <i>(paso 3.7)</i>
	 *
	 */
	public void consolidarAsentamiento() throws Exception{
		// Pasar datos de SinPartida a DiarioPartida.
		TipOriMov tipOriMov = TipOriMov.getById(TipOriMov.ID_ASENTAMIENTO);
		EstDiaPar estDiaPar = EstDiaPar.getById(EstDiaPar.ID_CREADA);

		List<SinPartida> listSinPartida = this.getListSinPartida();
		Double importeTotalRecaudadoAct = 0D;
		Double importeTotalRecaudadoVen = 0D;
		for(SinPartida sinPartida: listSinPartida){
			DiarioPartida diarioPartida = new DiarioPartida();
			diarioPartida.setFecha(sinPartida.getFechaBalance());
			diarioPartida.setEjercicio(this.getEjercicio());
			diarioPartida.setPartida(sinPartida.getPartida());
			diarioPartida.setImporteEjeAct(NumberUtil.truncate(sinPartida.getImporteEjeAct(),SiatParam.DEC_IMPORTE_DB));
			diarioPartida.setImporteEjeVen(NumberUtil.truncate(sinPartida.getImporteEjeVen(),SiatParam.DEC_IMPORTE_DB));
			diarioPartida.setTipOriMov(tipOriMov);
			diarioPartida.setIdOrigen(this.getId());
			diarioPartida.setEstDiaPar(estDiaPar);
			diarioPartida.setBalance(this.getBalance());				
			
			BalDAOFactory.getDiarioPartidaDAO().update(diarioPartida);
			
			importeTotalRecaudadoAct += diarioPartida.getImporteEjeAct();
			importeTotalRecaudadoVen += diarioPartida.getImporteEjeVen();
		}
		
		// Descargar el total recaudado de la Cuenta Puente que corresponde por Servicio Banco (ParCuePue)
		Partida parCuePue = this.getServicioBanco().getParCuePue();
		if(parCuePue != null){			
			// Creacion de Caja7 para Descarga de Cuentas Puentes
			Caja7 caja7 = new Caja7();
			
			caja7.setBalance(this.getBalance());
			caja7.setFecha(this.getFechaBalance());
			caja7.setDescripcion("Ajuste de descarga a Cuenta Puente. Asentamiento Nro: "+this.getId());
			//caja7.setImporteEjeAct(NumberUtil.truncate(importeTotalRecaudadoAct*(-1),SiatParam.DEC_IMPORTE_DB));
			//caja7.setImporteEjeVen(NumberUtil.truncate(importeTotalRecaudadoVen*(-1),SiatParam.DEC_IMPORTE_DB));
			// Lo modificamos e imputamos todo el importe a ejercicio actual
			caja7.setImporteEjeAct(NumberUtil.truncate((importeTotalRecaudadoAct+importeTotalRecaudadoVen)*(-1),SiatParam.DEC_IMPORTE_DB));
			caja7.setImporteEjeVen(0D);
			
			caja7.setPartida(parCuePue);
			
			BalDAOFactory.getCaja7DAO().update(caja7);
		}
		
		// Pasar datos de SinIndet a IndetBal
		List<SinIndet> listSinIndet = this.getListSinIndet();
		for(SinIndet sinIndet: listSinIndet){
			IndetBal indetBal = new IndetBal();
			
			indetBal.setSistema(sinIndet.getSistema().toString());
			indetBal.setNroComprobante(sinIndet.getNroComprobante().toString());
			// Armar Clave
			Long anio = sinIndet.getAnioComprobante();
			Long periodo = sinIndet.getPeriodo();
			String anioPeriodo = "";
			
			//Verifica si la transaccion posee cod ref pag y anio y periodo indica el tipo boleta
			if(anio > 0L && anio <=5L){
				anioPeriodo = StringUtil.completarCerosIzq(anio.toString(), 4);
				anioPeriodo += "00";
			}
			//Contempla los casos de recibo donde anio=99 y periodo=99
			else if(periodo == 99 && anio == 99){
				anioPeriodo = "999999"; 
			}
			// Contempla los casos de recibo donde anio=88 y periodo=88
			else if(periodo == 88 && anio == 88){
				anioPeriodo = "888888"; 
			}
			// Contempla los casos de recibo donde anio=77 y periodo=77
			else if(periodo == 77 && anio == 77){
				anioPeriodo = "777777"; 
			}
			//Contempla los casos donde las cuotas son mayores a 99
			else if(periodo >=100 && anio < 1000){	// Se corrige esta expresion, anteriormente estaba asi:  'if(periodo >=100 || anio < 1000)', puede ser el origen del cero en la clave al pasar al balance.
				anioPeriodo = StringUtil.completarCerosIzq(periodo.toString(), 6);
			}
			//Contempla deuda con anio y periodo o (cuotas menores a 100 y anio 0000)
			else{
				anioPeriodo  += StringUtil.completarCerosIzq(anio.toString(), 4);
				anioPeriodo  += StringUtil.completarCerosIzq(periodo.toString(), 2); 
			}
			
			indetBal.setClave(anioPeriodo);
		
    		indetBal.setResto(sinIndet.getResto().toString());
    		indetBal.setImporteCobrado(sinIndet.getImporteCobrado());
    		indetBal.setImporteBasico(sinIndet.getBasico());
    		if(indetBal.getImporteBasico() == null)
    			indetBal.setImporteBasico(0D);
    		indetBal.setImporteCalculado(sinIndet.getCalculado());
    		if(indetBal.getImporteCalculado() == null)
    			indetBal.setImporteCalculado(0D);
    		indetBal.setIndice(sinIndet.getIndice());
    		if(indetBal.getIndice() == null)
    			indetBal.setIndice(0D);
    		indetBal.setRecargo(sinIndet.getRecargo());
    		if(indetBal.getRecargo() == null)
    			indetBal.setRecargo(0D);
    		indetBal.setPartida("9".concat(sinIndet.getPartida().getCodPartida()));
    		indetBal.setCodIndet(new Integer(sinIndet.getCodIndeterminado().trim()));
    		indetBal.setFechaPago(sinIndet.getFechaPago());
    		if(sinIndet.getCaja() != null)
    			indetBal.setCaja(sinIndet.getCaja().intValue());
    		else
    			indetBal.setCaja(0);
    		if(sinIndet.getPaquete() != null)
    			indetBal.setPaquete(sinIndet.getPaquete().intValue());
    		else
    			indetBal.setPaquete(0);
    		if(sinIndet.getCodPago() != null)
    			indetBal.setCodPago(sinIndet.getCodPago().intValue());
    		else
    			indetBal.setCodPago(0);
    		indetBal.setFechaBalance(sinIndet.getFechaBalance()); 
    		indetBal.setFiller(sinIndet.getFiller());
    		if(StringUtil.isNullOrEmpty(indetBal.getFiller()))
				indetBal.setFiller("0");
    		if(sinIndet.getReciboTr() != null)
    			indetBal.setReciboTr(sinIndet.getReciboTr());
    		else
    			indetBal.setReciboTr(0L);
    		indetBal.setBalance(this.getBalance());
    		
    		BalDAOFactory.getIndetBalDAO().update(indetBal);
		}
		
		// Pasar datos de SinSalAFav a SaldoBal
		List<SinSalAFav> listSinSalAFav = this.getListSinSalAFav();
		for(SinSalAFav sinSalAFav: listSinSalAFav){
			SaldoBal saldoBal = new SaldoBal();
			
			saldoBal.setCuenta(sinSalAFav.getCuenta()); 
			TipoOrigen tipoOrigen = TipoOrigen.getById(TipoOrigen.ID_ASENTAMIENTO);
			saldoBal.setTipoOrigen(tipoOrigen);
			saldoBal.setFechaGeneracion(new Date());
       		Area area = Area.getByCodigo(Area.COD_DGGR_BAL);
       		saldoBal.setArea(area);
			saldoBal.setPartida(sinSalAFav.getPartida()); 
			saldoBal.setImporte(sinSalAFav.getImportePago()-sinSalAFav.getImporteDebPag());
			EstSalAFav estSalAFav = EstSalAFav.getById(EstSalAFav.ID_CREADO);
			saldoBal.setEstSalAFav(estSalAFav);
			saldoBal.setDescripcion("Generado en Asentamiento de Pago Nro: "+this.getId());			
			
			BalDAOFactory.getSaldoBalDAO().update(saldoBal);
		}
	}
	
	
	/**
	 * Incrementa el contador de cuotas del mapa correspondiente al idConvenio indicado.
	 * @param idConvenio
	 * 
	 */
	public void incCuotaCounter(Long idConvenio) throws Exception {
		Integer counter = null; 
		counter = AsentamientoCache.getInstance().getSession(this.getId()).getMapCuotaCounter().get(idConvenio.toString());
		if(counter == null){
			counter = 1;
		}else{
			counter++;			
		}
		AsentamientoCache.getInstance().getSession(this.getId()).getMapCuotaCounter().put(idConvenio.toString(), counter);
	}
	
	/**
	 * Obtiene el contador de cuotas del mapa correspondiente al idConvenio indicado.
	 * @param idConvenio
	 * @return counter
	 */
	public Integer getCuotaCounter(Long idConvenio) throws Exception {
		Integer counter = null; 
		counter = AsentamientoCache.getInstance().getSession(this.getId()).getMapCuotaCounter().get(idConvenio.toString());
		if(counter == null){
			counter = 0;
		}
		return counter;
	}
	
	/**
	 * Obtiene registro de deuda para recursos autoliquidables (DReI y ETuR).
	 * NOTA: La indetermición de pagos se realiza dentro del mismo método.
	 * @param transaccion
	 * @return
	 * @throws Exception
	 */
	public Deuda obtenerDeudaForAutoLiquidable(TransaccionAutoLiq transaccion) throws Exception{		
		Deuda deuda = null;
		
		if(transaccion.getOrigen() == null || transaccion.getOrigen().intValue() != Transaccion.ORIGEN_OSIRIS.intValue()){			
			// . Consultamos la variable de la transaccion que indican si se trata de una deuda rectifictiva o no.
			// 	. En caso de no serlo se busca el registro de deuda con la clave completa de la transaccion.
			if(transaccion.getEsRectificativa() == null || transaccion.getEsRectificativa().intValue() == SiNo.NO.getId().intValue()){
				if(transaccion.esNueva()){
					deuda = Deuda.getByCodRefPag(transaccion.getNroComprobante());
				}else{
					deuda = Deuda.getByCtaPerAnioSisRes(transaccion.getNroComprobante(),
														transaccion.getPeriodo(),
														transaccion.getAnioComprobante(),
														transaccion.getSistema(), 
														transaccion.getResto());		 	
				}
				
				// si no se encuentra se indetermina
				if(deuda == null){
					transaccion.registrarIndeterminado("No existe la transaccion en SIAT: Deuda Codigo de Referencia de Pago: "+transaccion.getNroComprobante().toString()+".",(Deuda) null, null, "1");
					if (transaccion.hasError()) {
						String err = transaccion.getListError().get(0).key().substring(1);
						this.addRecoverableValueError(err);
					}	
					return null;
				}

				// Valida que sea DeudaAdmin sino la indetermina
				if(deuda.getEstadoDeuda().getId().longValue() != EstadoDeuda.ID_ADMINISTRATIVA){
					transaccion.registrarIndeterminado("Indeterminado por Deuda en Vía distinta de Administrativa.", deuda, null, "16");
					if (transaccion.hasError()) {
						String err = transaccion.getListError().get(0).key().substring(1);
						this.addRecoverableValueError(err);
					}	
					return null;
				}
				
				// Si la Deuda Original no está determinada (importe es cero) considero Pago Original
				if(deuda.getImporte().doubleValue() == 0D)
				   deuda = transaccion.determinarPagoOriginal(deuda);
				
			}else{
				// Si es rectificativa: 
				// 	. buscamos la deuda original. 
				Deuda deudaOriginal = Deuda.getByCtaPerAnioSisRes(transaccion.getNroComprobante(),
																  transaccion.getPeriodo(),
																  transaccion.getAnioComprobante(),
																  transaccion.getSistema(),0L);
				
				// si no se encuentra se indetermina
				if(deudaOriginal == null){
					transaccion.registrarIndeterminado("No existe la transaccion en SIAT: Deuda Codigo de Referencia de Pago: "+transaccion.getNroComprobante().toString()+".",(Deuda) null, null, "1");
					if (transaccion.hasError()) {
						String descripcion = transaccion.getListError().get(0).key().substring(1);
						this.addRecoverableValueError(descripcion);
					}	
					return null;
				}
				
				// . Determino Deuda Rectificativa.
				deuda = transaccion.determinarDeudaRectificativa(deudaOriginal);				
			}
			
		}else{
			this.logDetallado("Entrando a Obtener Deuda AutoLiq Osiris");
			// La deuda es de origen Osiris:
			// . Obtengo el tipo de formulario
			Integer formulario = transaccion.getFormulario();
			// . Deuda no posee tipoFormulario:
			if (null == transaccion.getFormulario()) {
				// . Indetermino el Pago
				transaccion.registrarIndeterminado("No existe la transaccion en SIAT: La transaccion no posee formulario AFIP asociado: "+transaccion.getNroComprobante().toString()+".",(Deuda) null, null, "59");
				if (transaccion.hasError()) {
					String err = this.getListError().get(0).key().substring(1);
					this.addRecoverableValueError(err);
				}
				return null;
			}
			this.logDetallado("La transaccion corresponde a un formulario: " + formulario);
			
			// . Buscar deuda segun tipoFormulario:
			// Si es Regimen Simplificado (6057)
			if(FormularioAfip.getEsRegimenSimplificado(formulario)){
				deuda = obtenerDeudaForRS(transaccion);
			}
			// Si es Multa (6053,6061)
			if(FormularioAfip.getEsMulta(formulario)){
				deuda = obtenerDeudaForMulta(transaccion);
			}
			// Si es Solo Pagos(6052,6056)
			// o DJ y Pago (6050,6054)
			// o DJ y Pago Web (6058,6059)
			if(FormularioAfip.getEsSoloPago(formulario)||
			   FormularioAfip.getEsDJyPago(formulario) ||
			   FormularioAfip.getEsDJyPagoWeb(formulario)) {
				
				deuda = obtenerDeudaForSoloPagoDJPago((TransaccionAutoLiq)transaccion); 
			}
		}
		// ver si hacemos un flush para mandar estos cambios antes de entrar al procesar deuda
		SiatHibernateUtil.currentSession().flush();
		return deuda;
	}
	
	/**
	 * Genera el Reporte pdf  Totales Distribuido por Recurso y Tipo Importe" resultado del paso 2 del proceso de Asentamiento. Este reporte solo se genera
	 * para el Servicio Banco 85 (Otros Tributos, ex Gravamenes Especiales).
	 * 
	 * @param fileDir
	 * @return fileName
	 */
	public String generarPdfTotalesPorRecurso(String fileDir) throws Exception{
		//		Encabezado:
		String fechaAsentamiento = DateUtil.formatDate(this.getFechaBalance(),DateUtil.ddSMMSYYYY_MASK);
		String ejercicio = this.getEjercicio().getDesEjercicio()+ " - " + this.getEjercicio().getEstEjercicio().getDesEjeBal();
		if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_ABIERTO)
			ejercicio += " - Asentamiento Común";
		else if(this.getEjercicio().getEstEjercicio().getId().longValue()==EstEjercicio.ID_CERRADO){
			ejercicio += " - Asentamiento Especial";
		}
		String servicioBanco = this.getServicioBanco().getCodServicioBanco() + " - " + this.getServicioBanco().getDesServicioBanco();
		String estado = this.getCorrida().getEstadoCorrida().getDesEstadoCorrida();
		
		List<Object[]> listTotalRecurso = BalDAOFactory.getAuxImpRecDAO().getListDetalleForReportByAsentamiento(this);
		
		Double importeTotal = 0D;
		Map<String, Double[]> mapTotalRecurso = new HashMap<String, Double[]>();
		for(Object[] arrayResult: listTotalRecurso){
			String recurso = (String) arrayResult[0];
			Long idTipoImporte = (Long) arrayResult[1];
			Double importe = (Double) arrayResult[2];
			
			Double[] totales = mapTotalRecurso.get(recurso);
			if(totales == null){
				totales = new Double[6];
				if(idTipoImporte.longValue() == TipoImporte.ID_CAPITAL.longValue())
					totales[0] = importe;
				else
					totales[0] = 0D;
				if(idTipoImporte.longValue() == TipoImporte.ID_ACTUALIZACION.longValue())
					totales[1] = importe;
				else
					totales[1] = 0D;
				if(idTipoImporte.longValue() == TipoImporte.ID_INTERES_FINANCIERO.longValue())
					totales[2] = importe;
				else
					totales[2] = 0D;
				if(idTipoImporte.longValue() == TipoImporte.ID_INDETERMINADO.longValue())
					totales[3] = importe;
				else
					totales[3] = 0D;
				if(idTipoImporte.longValue() == TipoImporte.ID_DUPLICES.longValue())
					totales[4] = importe;
				else
					totales[4] = 0D;
				totales[5] = importe;
			}else{
				if(idTipoImporte.longValue() == TipoImporte.ID_CAPITAL.longValue())
					totales[0] += importe;
				if(idTipoImporte.longValue() == TipoImporte.ID_ACTUALIZACION.longValue())
					totales[1] += importe;
				if(idTipoImporte.longValue() == TipoImporte.ID_INTERES_FINANCIERO.longValue())
					totales[2] += importe;
				if(idTipoImporte.longValue() == TipoImporte.ID_INDETERMINADO.longValue())
					totales[3] += importe;
				if(idTipoImporte.longValue() == TipoImporte.ID_DUPLICES.longValue())
					totales[4] += importe;
			
				totales[5] += importe;
			}
			mapTotalRecurso.put(recurso, totales);
			importeTotal += importe;
		}
		
		// Armado del PDF.
		PrintModel printModel = Formulario.getPrintModelForPDF(Asentamiento.COD_FRM_PASO2_4); 
		
		// Datos del Encabezado Generico
		printModel.putCabecera("TituloReporte", "Asentamiento de Pagos - Reporte Totales por Recurso");
		printModel.putCabecera("Fecha",  DateUtil.formatDate(new Date(),DateUtil.ddSMMSYYYY_MASK));
		printModel.putCabecera("Hora", DateUtil.formatDate(new Date(), "HH:mm:ss"));
		printModel.putCabecera("Usuario", this.getCorrida().getUsuarioUltMdf());
		
		// Datos del Encabezado del PDF
		printModel.setTituloReporte("Asentamiento de Pagos - Reporte Totales por Recurso");
		printModel.putCabecera("FechaAsentamiento", fechaAsentamiento);
		printModel.putCabecera("Ejercicio", ejercicio);
		printModel.putCabecera("ServicioBanco", servicioBanco);
		printModel.putCabecera("Estado", estado);
				
		// Se arman una tabla para el Reporte Total Por Recurso
		TablaVO tabla = new TablaVO("TotalesRecurso");
		FilaVO filaCabecera = new FilaVO();
		filaCabecera.add(new CeldaVO("Recurso","recurso"));
		filaCabecera.add(new CeldaVO("Capital","capital"));
		filaCabecera.add(new CeldaVO("Act.","actualizacion"));
		filaCabecera.add(new CeldaVO("Int. Financiero","intFin"));
		filaCabecera.add(new CeldaVO("Indet.","indet"));
		filaCabecera.add(new CeldaVO("Duplice","duplice"));
		filaCabecera.add(new CeldaVO("Total","total"));
		tabla.setFilaCabecera(filaCabecera);
		for(String recurso: mapTotalRecurso.keySet()){
			Double[] arrayResult = mapTotalRecurso.get(recurso);
			FilaVO fila = new FilaVO();
			fila.add(new CeldaVO(recurso,"recurso"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate( arrayResult[0], SiatParam.DEC_IMPORTE_DB)),"capital"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate( arrayResult[1], SiatParam.DEC_IMPORTE_DB)),"actualizacion"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate( arrayResult[2], SiatParam.DEC_IMPORTE_DB)),"intFin"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate( arrayResult[3], SiatParam.DEC_IMPORTE_DB)),"indet"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate( arrayResult[4], SiatParam.DEC_IMPORTE_DB)),"duplice"));
			fila.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate( arrayResult[5], SiatParam.DEC_IMPORTE_DB)),"total"));
			tabla.add(fila);
		}
		
		FilaVO filaPie = new FilaVO();
		filaPie.add(new CeldaVO("","vacio"));
		filaPie.add(new CeldaVO("","vacio"));
		filaPie.add(new CeldaVO("","vacio"));
		filaPie.add(new CeldaVO("","vacio"));
		filaPie.add(new CeldaVO("","vacio"));
		filaPie.add(new CeldaVO("Total:","totalDistribuido"));
		filaPie.add(new CeldaVO(StringUtil.formatDouble(NumberUtil.truncate(importeTotal, SiatParam.DEC_IMPORTE_DB)),"importeTotal"));
		tabla.addFilaPie(filaPie);
	
		// Cargamos los datos en el Print Model
		printModel.setData(tabla);
		printModel.setTopeProfundidad(3);
		
		// Guardo el Archivo PDF en el directorio pasado como parametro.
		byte[] bytesPDF = printModel.getByteArray();
		String fileName = this.getId().toString()+"ReportePorRecurso.pdf";
		FileOutputStream pdfFile = new FileOutputStream(new File(fileDir+"/"+fileName));
		pdfFile.write(bytesPDF);
		pdfFile.close();
		
		
		return fileName;
	}
	
	/**
	 * Obtiene deuda para el Formulario afip Regimen Simplificado (6057). 
	 * Si no la encuentra, registra indeterminado.
	 * @param transaccion
	 * @return
	 * @throws Exception 
	 */
	private Deuda obtenerDeudaForRS(Transaccion transaccion) throws Exception{

		this.logDetallado("Entrando al Obtener deuda de Regimen Simplificado");

		Deuda deuda = Deuda.getByCtaPerAnioSisResForRS(transaccion.getNroComprobante(), //nro cuenta
													   transaccion.getPeriodo(),
													   transaccion.getAnioComprobante(),
													   transaccion.getSistema(),
													   transaccion.getResto());
		
		if (null == deuda) {
			transaccion.registrarIndeterminado("No se encuentra periodo de RS: Numero Cuenta :"+transaccion.getNroComprobante().toString()+".Periodo: "+transaccion.getPeriodo()+".Anio: "+transaccion.getAnioComprobante()+".Sistema: "+transaccion.getSistema()+".Resto: "+transaccion.getResto()+".",(Deuda) null, null, "62");
			if (transaccion.hasError()) {
				String descripcion = this.getListError().get(0).key().substring(1);
				this.addRecoverableValueError(descripcion);
			}
		}
		
		return deuda;
	}
	
	
	/**
	 * Obtiene deuda para el Formulario afip Multas (6053,6061). 
	 * Si no la encuentra, registra indeterminado.
	 * @param transaccion
	 * @param formulario
	 * @return
	 * @throws Exception 
	 */
	private Deuda obtenerDeudaForMulta(Transaccion transaccion) throws Exception {
		
		this.logDetallado("Entrando al Obtener deuda de Multas");
		
		//Busco la deuda por codigo de referencia de pago
		Deuda deuda = Deuda.getByCodRefPag(transaccion.getNroComprobante()); 
		
		if (null == deuda) {
			transaccion.registrarIndeterminado("No existe la transaccion en SIAT: Deuda Codigo de Referencia de Pago: "+transaccion.getNroComprobante().toString()+".",(Deuda) null, null, "1");
			if (transaccion.hasError()) {
				String descripcion = this.getListError().get(0).key().substring(1);
				this.addRecoverableValueError(descripcion);
			}
			return null;
		}
		
		Recurso recurso;
		//Determino el Recurso asociado a la multa
		if (FormularioAfip.DREI_MULTAS_6053.getId().equals(transaccion.getFormulario()))
			//Multa corresponde a DReI
			recurso = Recurso.getDReI();
		else
			//Multa corresponde a ETur
			recurso = Recurso.getETur();

		//Verifico que el recurso asociado a la deuda corresponde con el de la multa
		if (!deuda.getRecurso().getId().equals(recurso.getId())){
			// si no coincide, se indetermina
			transaccion.registrarIndeterminado("No existe la transaccion en SIAT: El recurso asociado a la deuda difiere del especificado en la multa. Deuda id: " + deuda.getId() +". Recurso deuda: " +deuda.getRecurso().getDesRecurso()+". Recurso multa: "+recurso.getDesRecurso()+".",(Deuda) null, null, "60");
			if (transaccion.hasError()) {
				String err = this.getListError().get(0).key().substring(1);
				this.addRecoverableValueError(err);
			}
			return null;
		} 

		//Recupero la TranAfip asociada a la transaccion
		TranAfip tranAfip = TranAfip.getById(transaccion.getIdTranAfip());

		//Comparo el cuit asociado a la deuda con el de la transaccion
		String cuit = deuda.getCuenta().getCuitTitPri();
		if (!StringUtil.iguales(cuit, tranAfip.getCuit())){
			// si no coincide, se indetermina
			transaccion.registrarIndeterminado("No existe la transaccion en SIAT: El CUIT asociado a la deuda difiere del CUIT especificado en la tranAfip. Deuda id: " + deuda.getId() +". CUIT deuda: " +cuit+". Nro. tranAfip: "+tranAfip.getNroTran()+". CUIT tranAfip:"+tranAfip.getCuit()+".",(Deuda) null, null, "61");
			if (transaccion.hasError()) {
				String err = this.getListError().get(0).key().substring(1);
				this.addRecoverableValueError(err);
			}
			return null; 
		} 
		
		return deuda;
	}
	
	/**
	 * Obtiene deuda para el Formulario Solo Pagos(6052,6056), DJ y Pago (6050,6054) y DJ y Pago Web (6058,6059).
	 * Si no la encuentra o es duplice, registra indeterminado.
	 * 
	 * @param transaccion
	 * @return
	 * @throws Exception 
	 */
	private Deuda obtenerDeudaForSoloPagoDJPago(TransaccionAutoLiq transaccion) throws Exception {
		
		this.logDetallado("Entrando al Obtener deuda de Solo Pago y DJ Pago");
		
		Deuda deudaOriginal = Deuda.getOriginalByCtaPerAnioSisRes(transaccion.getNroComprobante(),
																  transaccion.getPeriodo(),
																  transaccion.getAnioComprobante(),
																  transaccion.getSistema(),0L);
		
		//Si la deudaOriginal es nula, puede ser debido a que la misma posee servicio banco antiguo
		if (null == deudaOriginal) {
			/* Sistemas:
			 * --------
			 * · DReI = 3	· DReI SIAT = 83
			 * · ETur = 33	· ETur SIAT = 84
			 */
			// Si el perdiodo/anio de la transaccion coincide con el inicio del RS
			if (transaccion.getPeriodo().longValue() ==  12L && transaccion.getAnioComprobante().longValue() == 2009L) {
				Long sistema;
				//Determino el sistema antiguo
				if (transaccion.getSistema().equals(83L))
					sistema = 3L; //DReI
				else
					sistema = 33L;//ETuR
				
				this.logDetallado("Intentamos obtener deuda con sistema antiguo. Sistema transaccion: "+ transaccion.getSistema() + ", Sistema corregido: "+sistema);
				
				//Busco la cuenta con el sismeta corregido
				deudaOriginal = Deuda.getOriginalByCtaPerAnioSisRes(transaccion.getNroComprobante(),
						  											transaccion.getPeriodo(),
						  											transaccion.getAnioComprobante(),
						  											sistema,0L);
			}
		}
		
		//No encuentro Deuda Original
		if(null == deudaOriginal){
			// . Indetermino el Pago
			transaccion.registrarIndeterminado("No existe periodo original: Numero Cuenta: "+transaccion.getNroComprobante().toString()+". Periodo: "+transaccion.getPeriodo()+". Anio: "+transaccion.getAnioComprobante()+". Sistema:"+transaccion.getSistema()+".",(Deuda) null, null, "63");
			if (transaccion.hasError()) {
				String err = this.getListError().get(0).key().substring(1);
				this.addRecoverableValueError(err);
			}
			return null;
		}
		
		if(deudaOriginal.getEstadoDeuda().getId().longValue() == EstadoDeuda.ID_ANULADA){
			transaccion.registrarIndeterminado("Indeterminado por Deuda Anulada.", deudaOriginal, null, "13");
			if (transaccion.hasError()) {
				String err = this.getListError().get(0).key().substring(1);
				this.addRecoverableValueError(err);
			}
			return null;
		}

		//Si no se determina la Deuda Original (importe es cero), se considera Pago Original
		if(deudaOriginal.getImporte().doubleValue() == 0D){
			this.logDetallado("Determino Deuda Original");
			// . Determino el Pago Original
			return transaccion.determinarPagoOriginalAFIP(deudaOriginal);
		}

		//Verificamos si la Deuda Original esta paga (tiene fecha de pago o existe un pago en este asentamiento)
		if (null != deudaOriginal.getFechaPago() || this.existAuxPagDeu(deudaOriginal.getId())) {

			//Determino si corresponde a un Pago duplice
			if (NumberUtil.isDoubleEqualToDouble(transaccion.getImporte(), deudaOriginal.getImporte(), 0.01)) {
				// . Pago duplice
				transaccion.registrarIndeterminado("Indeterminado por Deuda Cancelada: Pago Dúplice.", deudaOriginal, null, "12");
				if (transaccion.hasError()) {
					String descripcion = this.getListError().get(0).key().substring(1);
					this.addRecoverableValueError(descripcion);
				}
				return null;

			} else {
				this.logDetallado("Determino Deuda Rectificativa");
				//Si está paga y no es un pago dúplice: Este pago se identifica como rectificativa
				// . Determino la deuda rectificativa
				return transaccion.determinarDeudaRectificativaAFIP(deudaOriginal);
			}

		} else {
			//Si está impaga
			// . Se considera Pago Original y se utiliza esta deuda 
			return deudaOriginal;
		}
	}
	
}

