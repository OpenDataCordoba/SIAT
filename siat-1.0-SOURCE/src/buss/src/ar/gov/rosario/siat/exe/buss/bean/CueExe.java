//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.bean;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.exe.buss.dao.ExeDAOFactory;
import ar.gov.rosario.siat.exe.iface.model.ConvivienteVO;
import ar.gov.rosario.siat.exe.iface.model.CueExeVO;
import ar.gov.rosario.siat.exe.iface.model.EstadoCueExeVO;
import ar.gov.rosario.siat.exe.iface.model.HisEstCueExeVO;
import ar.gov.rosario.siat.exe.iface.model.TipoSujetoVO;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Cuenta Exencion
 * 
 * @author tecso
 */
@Entity
@Table(name = "exe_cueexe")
public class CueExe extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "fechasolicitud")
	private Date fechaSolicitud;
	
	@Column(name = "fechadesde")
	private Date fechaDesde;
	
	@Column(name = "fechahasta")
	private Date fechaHasta;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idCuenta") 
	private Cuenta cuenta;

	@Column(name="idCuenta",insertable=false,updatable=false) 
	private Long idCuenta;
	
	@ManyToOne(optional=false) 
    @JoinColumn(name="idExencion") 
	private Exencion exencion;
	
	@ManyToOne() 
    @JoinColumn(name="idEstadoCueExe")
	private EstadoCueExe estadoCueExe;
			
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="idpersona")
	private Persona solicitante;
	
	@Column(name = "solicDescripcion")
	private String solicDescripcion;
	
	@ManyToOne() 
    @JoinColumn(name="idTipoSujeto")
	private TipoSujeto tipoSujeto;
	
	@Column(name = "solicFechaDesde")
	private Date solicFechaDesde;
	
	@Column(name = "solicFechaHasta")
	private Date solicFechaHasta;
	
	@Column(name = "fechaResolucion")
	private Date fechaResolucion;
	
	@Column(name = "ordenanza")
	private String ordenanza;
	
	@Column(name = "articulo")
	private String articulo;
	
	@Column(name = "inciso")
	private String inciso;
	
	@Column(name = "fechaUltIns")
	private Date fechaUltIns;
	
	@Column(name = "nroBeneficiario")
	private String nroBeneficiario;
	
	@Column(name = "caja")
	private String caja;
	
	@Column(name = "observaciones")
	private String observaciones;
	
	@Column(name = "documentacion")
	private String documentacion;
	
	@Column(name = "fechaPresent")
	private Date fechaPresent;
	
	@Column(name = "clase")
	private Integer clase;
	
	@Column(name = "fechaVencContInq")
	private Date fechaVencContInq;
	
    @Column(name="idcaso") 
	private String idCaso;

    @Column(name = "tipoDocumento")
	private String tipoDocumento;
	
    @Column(name = "nroDocumento")
	private String nroDocumento;
    
    @Column(name = "fechacadhab")
    private Date fechaCadHab; // Fecha caducidad habilitacion
    
	@OneToMany()
	@JoinColumn(name="idCueExe")
	@OrderBy(clause="id asc")
	private List<HisEstCueExe> listHisEstCueExe;
	
	@OneToMany()
	@JoinColumn(name="idCueExe")
	private List<Conviviente> listConviviente;
	
	
	// Constructores
	public CueExe(){
		super();
		// Seteo de valores default			
	}
	
	public CueExe(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static CueExe getById(Long id) {
		return (CueExe) ExeDAOFactory.getCueExeDAO().getById(id);
	}
	
	public static CueExe getByIdNull(Long id) {
		return (CueExe) ExeDAOFactory.getCueExeDAO().getByIdNull(id);
	}
	
	public static List<CueExe> getList() {
		return (ArrayList<CueExe>) ExeDAOFactory.getCueExeDAO().getList();
	}
	
	public static List<CueExe> getListActivos() {			
		return (ArrayList<CueExe>) ExeDAOFactory.getCueExeDAO().getListActiva();
	}
	
	public static List<CueExe> getListByIdCuentaIdExencion(Long idCuenta, Long idExencion) throws Exception{
		return (List<CueExe>) ExeDAOFactory.getCueExeDAO().getListByIdCuentaIdExencion( idCuenta, idExencion);
	}
	
	public static List<CueExe> getListVigentesByIdCuentaIdExencionIdEstado(Long idCuenta, Long idExencion, Date fecha, Long idEstado) throws Exception{
		return (List<CueExe>) ExeDAOFactory.getCueExeDAO().getListVigentesByIdCuentaIdExencionIdEstado(idCuenta, idExencion, fecha, idEstado);
	}
	
	public static List<CueExe> getListVigentesByIdCuentaIdExencionIdEstadoWithoutTipoSujeto(Long idCuenta, Long idExencion, Date fecha, Long idEstado) throws Exception{
		return (List<CueExe>) ExeDAOFactory.getCueExeDAO().getListVigentesByIdCuentaIdExencionIdEstadoWithoutTipoSujeto(idCuenta, idExencion, fecha, idEstado);
	}
	
	public static CueExe getByIdSolCueExeVig(Long idARevocar){
		return (CueExe) ExeDAOFactory.getCueExeDAO().getByIdSolCueExeVig(idARevocar);
	}
	
	public static List<CueExe> getListByIdCuentaIdExencionRangoFechas(Long idCuenta, Long idExencion, Date fechaDesde, Date fechaHasta)throws Exception{
		return (List<CueExe>) ExeDAOFactory.getCueExeDAO().getListByIdCuentaIdExencionRangoFechas(idCuenta, idExencion, fechaDesde, fechaHasta);
	}
	
	public static List<CueExe> getListByIdCuentaIdExencionRangoFechasWithoutTipoSujeto(Long idCuenta, Long idExencion, Date fechaDesde, Date fechaHasta)throws Exception{
		return (List<CueExe>) ExeDAOFactory.getCueExeDAO().getListByIdCuentaIdExencionRangoFechasWithoutTipoSujeto(idCuenta, idExencion, fechaDesde, fechaHasta);
	}
	
	public static List<EstadoCueExe> getListResoluciones() throws Exception {			
		return (ArrayList<EstadoCueExe>) ExeDAOFactory.getEstadoCueExeDAO().getListResoluciones();
	}
	
	// Getters y setters
	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Long getIdCuenta() {
		return idCuenta;
	}
	
	public Exencion getExencion() {
		return exencion;
	}

	public void setExencion(Exencion exencion) {
		this.exencion = exencion;
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
	
	public EstadoCueExe getEstadoCueExe() {
		return estadoCueExe;
	}

	public void setEstadoCueExe(EstadoCueExe estadoCueExe) {
		this.estadoCueExe = estadoCueExe;
	}

	public Persona getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Persona solicitante) {
		this.solicitante = solicitante;
	}

	public String getSolicDescripcion() {
		return solicDescripcion;
	}

	public void setSolicDescripcion(String solicDescripcion) {
		this.solicDescripcion = solicDescripcion;
	}

	public TipoSujeto getTipoSujeto() {
		return tipoSujeto;
	}

	public void setTipoSujeto(TipoSujeto tipoSujeto) {
		this.tipoSujeto = tipoSujeto;
	}

	public Date getSolicFechaDesde() {
		return solicFechaDesde;
	}

	public void setSolicFechaDesde(Date solicFechaDesde) {
		this.solicFechaDesde = solicFechaDesde;
	}

	public Date getSolicFechaHasta() {
		return solicFechaHasta;
	}

	public void setSolicFechaHasta(Date solicFechaHasta) {
		this.solicFechaHasta = solicFechaHasta;
	}

	public Date getFechaResolucion() {
		return fechaResolucion;
	}

	public void setFechaResolucion(Date fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}

	public String getOrdArtInc(){
		return this.getOrdenanza() +"-"+this.getArticulo()+"-"+this.getInciso();
	}
	public String getOrdenanza() {
		return ordenanza;
	}

	public void setOrdenanza(String ordenanza) {
		this.ordenanza = ordenanza;
	}

	public String getArticulo() {
		return articulo;
	}
    
	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}

	public String getInciso() {
		return inciso;
	}

	public void setInciso(String inciso) {
		this.inciso = inciso;
	}

	public Date getFechaUltIns() {
		return fechaUltIns;
	}

	public void setFechaUltIns(Date fechaUltIns) {
		this.fechaUltIns = fechaUltIns;
	}

	public String getNroBeneficiario() {
		return nroBeneficiario;
	}

	public void setNroBeneficiario(String nroBeneficiario) {
		this.nroBeneficiario = nroBeneficiario;
	}

	public String getCaja() {
		return caja;
	}

	public void setCaja(String caja) {
		this.caja = caja;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getDocumentacion() {
		return documentacion;
	}

	public void setDocumentacion(String documentacion) {
		this.documentacion = documentacion;
	}

	public Date getFechaPresent() {
		return fechaPresent;
	}

	public void setFechaPresent(Date fechaPresent) {
		this.fechaPresent = fechaPresent;
	}

	public Integer getClase() {
		return clase;
	}

	public void setClase(Integer clase) {
		this.clase = clase;
	}

	public Date getFechaVencContInq() {
		return fechaVencContInq;
	}

	public void setFechaVencContInq(Date fechaVencContInq) {
		this.fechaVencContInq = fechaVencContInq;
	}

	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public List<HisEstCueExe> getListHisEstCueExe() {
		return listHisEstCueExe;
	}

	public void setListHisEstCueExe(List<HisEstCueExe> listHisEstCueExe) {
		this.listHisEstCueExe = listHisEstCueExe;
	}

	public String getIdCaso() {
		return idCaso;
	}

	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public List<Conviviente> getListConviviente() {
		return listConviviente;
	}

	public void setListConviviente(List<Conviviente> listConviviente) {
		this.listConviviente = listConviviente;
	}
	
	public String getlistHisEstCueExe() {
		return idCaso;
	}
	
   public String getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	
	public Date getFechaCadHab() {
		return fechaCadHab;
	}
	public void setFechaCadHab(Date fechaCadHab) {
		this.fechaCadHab = fechaCadHab;
	}

	public String getLogHistoricos() throws Exception { 
	  List <HisEstCueExe> listHisEstCueExeResol = new ArrayList<HisEstCueExe>();

	  for(HisEstCueExe hisEstCueExe:listHisEstCueExe){

		  List <EstadoCueExe> listResoluciones = getListResoluciones();

		  for(EstadoCueExe estadoCueExeRes:listResoluciones){

			  if(hisEstCueExe.getEstadoCueExe().getDesEstadoCueExe().equals(estadoCueExeRes.getDesEstadoCueExe())){
				  listHisEstCueExeResol.add(hisEstCueExe);
				
				  break;
				
			  }
		  }
	  }
	   String rta="";
	   for(HisEstCueExe hisEstCueExeResol:listHisEstCueExeResol){
		   
		   rta +="["+DateUtil.formatDate(hisEstCueExeResol.getFecha(), DateUtil.ddSMMSYYYY_MASK)+"-"+hisEstCueExeResol.getEstadoCueExe().getDesEstadoCueExe()
		       +"-"+hisEstCueExeResol.getObservaciones()+"]";
	   }
	   return rta;
   }
	
	// Validaciones Para quita de Sobre Tasa 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();
		
	 	if (getCuenta() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL );
		}else if(getCuenta().getId()<0){
			//no se encontr la cuenta, ya sea porque no existe o porque se ingreso un nro de cuenta alfanumerico
			addRecoverableError(BaseError.MSG_NO_ENCONTRADO, PadError.CUENTA_LABEL);
		}
	
	 	if (getFechaSolicitud() == null){
	 		addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.CUEEXE_FECHASOLICITUD);
	 	}

	 	if (getFechaDesde() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.CUEEXE_FECHADESDE);
		}
		
		if (getExencion() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.EXENCION_LABEL);
		}
	 	
		if (getTipoSujeto() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.TIPOSUJETO_LABEL);
		}
	 	
		if (getSolicitante() == null && StringUtil.isNullOrEmpty(getSolicDescripcion())){
			addRecoverableValueError("Debe seleccionar un solicitante o ingresar una descripcion");
		}
		
		if (getFechaDesde() != null && getFechaHasta() != null){
			if(DateUtil.isDateAfter(getFechaDesde(), getFechaHasta())){
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, ExeError.CUEEXE_FECHADESDE, ExeError.CUEEXE_FECHAHASTA);
			}
		}
		
		if (hasError()) {
			return false;
		}
		// Valida, al agregar una "Quita de Sobre Tasa", que el objeto imponible de la cuenta sea de Tipo Parcela: Baldio
		if(Exencion.COD_EXENCION_QUITA_SOBRETASA.equals(this.getExencion().getCodExencion()) 
				&& 	"1".equals(this.getCuenta().getValorAtributo(36L))){
			addRecoverableError(ExeError.CUEEXE_QUITA_SOBRE_TASA_TIPOPARCELA);
		}
			
		if (hasError()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}
	
	//  Para quita de Sobre Tasa
	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (getFechaDesde() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.CUEEXE_FECHADESDE);
		}
		
		if (getSolicitante() == null && StringUtil.isNullOrEmpty(getSolicDescripcion())){
			addRecoverableValueError("Debe seleccionar un solicitante o ingresar una descripcion");
		}
		
		if (getFechaDesde() != null && getFechaHasta() != null){
			if(DateUtil.isDateAfter(getFechaDesde(), getFechaHasta())){
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, ExeError.CUEEXE_FECHADESDE, ExeError.CUEEXE_FECHAHASTA);
			}
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		if (GenericDAO.hasReference(this, HisEstCueExe.class, "cueExe")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, ExeError.CUEEXE_LABEL, ExeError.HISESTCUEEXE_LABEL);
		}
		
		if (GenericDAO.hasReference(this, Conviviente.class, "cueExe")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, ExeError.CUEEXE_LABEL, ExeError.CUEEXECONVIVIENTE_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	
	// Metodos de negocio
	
	/**
	 * Activa el CueExe. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		ExeDAOFactory.getCueExeDAO().update(this);
	}

	/**
	 * Desactiva el CueExe. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		ExeDAOFactory.getCueExeDAO().update(this);
	}
	
	/**
	 * Valida la activacion del CueExe
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del CueExe
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	
	public HisEstCueExe createHisEstCueExe(HisEstCueExe hisEstCueExe) throws Exception {

		// Validaciones de negocio
		if (!hisEstCueExe.validateCreate()) {
			return hisEstCueExe;
		}

		ExeDAOFactory.getHisEstCueExeDAO().update(hisEstCueExe);

		return hisEstCueExe;
	}
	
	public HisEstCueExe updateHisEstCueExe(HisEstCueExe hisEstCueExe) throws Exception {

		// Validaciones de negocio

		ExeDAOFactory.getHisEstCueExeDAO().update(hisEstCueExe);

		return hisEstCueExe;
	}
	
	public HisEstCueExe deleteHisEstCueExe(HisEstCueExe hisEstCueExe) throws Exception {
		
		// Validaciones de negocio
		if (!hisEstCueExe.validateDelete()) {
			return hisEstCueExe;
		}
		
		ExeDAOFactory.getHisEstCueExeDAO().delete(hisEstCueExe);
		
		return hisEstCueExe;
	}
	
	
	/**
	 * Chequea si existe deuda emitida dentro del periodo, que pudo haber sido modificado, por eso recibe cueExeVO
	 * 
	 * 
	 * @param cueExeVO
	 * @return List<Deuda>
	 */
	public List<Deuda> verificarDeudaEmitida(CueExeVO cueExeVO, Date fechaDesde, Date fechaHasta) {
		List<Deuda> listDeuda = new ArrayList<Deuda>();

		// si la fecha desde es menor a la actual
		if (DateUtil.isDateBefore(cueExeVO.getFechaDesde(), new Date())) {
			// recupero toda la deuda para el rango de fechas cargado 
			listDeuda = this.getCuenta().getListDeudaVto(fechaDesde, fechaHasta);//cueExeVO.getFechaDesde(), cueExeVO.getFechaHasta());
		}

		return listDeuda;
	}
	
	

	public Conviviente createCueExeConviv(Conviviente conviviente) throws Exception {
		// Validaciones de negocio
		if (!conviviente.validateCreate()) {
			return conviviente;
		}

		ExeDAOFactory.getConvivienteDAO().update(conviviente);

		return conviviente;
	}

	public Conviviente updateCueExeConviv(Conviviente conviviente) throws Exception {
		// Validaciones de negocio
		if (!conviviente.validateUpdate()) {
			return conviviente;
		}

		ExeDAOFactory.getConvivienteDAO().update(conviviente);

		return conviviente;
	}

	public Conviviente deleteCueExeConviv(Conviviente conviviente) {
		// Validaciones de negocio
		if (!conviviente.validateDelete()) {
			return conviviente;
		}

		ExeDAOFactory.getConvivienteDAO().delete(conviviente);

		return conviviente;
	}
	
	@Override
	public String infoString() {
		String solicitante = getSolicitante()!=null?getSolicitante().getRepresent():getSolicDescripcion();
		String tipoSujeto = getTipoSujeto()!=null?getTipoSujeto().getDesTipoSujeto():"<no definido>";
		String cuenta = getCuenta()!=null?getCuenta().getNumeroCuenta():"<no definida>";
		String exencion = getExencion()!=null?getExencion().getDesExencion():"<no definida>";
		
		String ret = "El Dia "+DateUtil.formatDate(getFechaSolicitud(), DateUtil.ddSMMSYYYY_MASK)+
					" el Usuario: "+ getUsuario()+
					" Gener� una exencion del tipo: "+exencion+
					" para la cuenta nro.: "+cuenta+
					" solicitada por: "+solicitante+
					" asignando el tipo de sujeto: "+tipoSujeto+
					" ingresando fecha Desde:"+DateUtil.formatDate(getFechaDesde(), DateUtil.ddSMMSYYYY_MASK);
			
		return ret;
	}

	public String getCasoView(){		
		CasoVO caso = CasServiceLocator.getCasCasoService().construirCasoVO(getIdCaso());		
		return caso.getCasoView();
	}
	
	/**
	 * 
	 * ToVO para la impresion del reporte de Exenciones por Cuenta
	 * 
	 * @return
	 * @throws Exception
	 */
	public CueExeVO toVO4Print() throws Exception{
		
		// se carga la persona si existe
		if(this.getSolicitante().getId()!=null)
			this.setSolicitante(Persona.getById(this.getSolicitante().getId()));
		
		// ToVo principal
		CueExeVO cueExeVO = (CueExeVO) this.toVO(0, false);
		
		cueExeVO.setExencion(this.getExencion().toVOLightForPDF());
		
		cueExeVO.setEstadoCueExe((EstadoCueExeVO)this.getEstadoCueExe().toVO(0,false));
		
		// Tipo Sujeto
		if (this.getTipoSujeto() != null)
			cueExeVO.setTipoSujeto((TipoSujetoVO)this.getTipoSujeto().toVO(0,false));
		
		// Solicitante
		if (this.getSolicitante() != null)
			cueExeVO.setSolicitante((PersonaVO)this.getSolicitante().toVO(0,false));
		
		// Cuenta
		cueExeVO.setCuenta((CuentaVO)this.getCuenta().toVO(0,false));
		
		// Caso
		CasoVO caso = CasServiceLocator.getCasCasoService().construirCasoVO(getIdCaso());
		cueExeVO.setCaso(caso);
		
		cueExeVO.setListHisEstCueExe(new ArrayList<HisEstCueExeVO>());
		cueExeVO.setListConviviente(new ArrayList<ConvivienteVO>());  
		
		return cueExeVO;
	}

	/**
	 * Retorna true si y solo si el cueExe esta 
	 * vigente para la fecha pasada como parametro.
 	 */
	public boolean getEsVigente(Date fechaAnalisis) {

		// Validamos que la fecha Desde sea anterior
		// o igual a la fecha de analisis
		if (this.getFechaDesde().before(fechaAnalisis) || 
			this.getFechaDesde().equals(fechaAnalisis)) { 

			// Validamos que la fecha Desde sea posterior
			// o igual a la fecha de analisis
			if (this.getFechaHasta() == null ||
				this.getFechaHasta().after(fechaAnalisis) || 
				this.getFechaHasta().equals(fechaAnalisis)) {
			
				return true;
			}
		}

		return false;
		
	}

}
