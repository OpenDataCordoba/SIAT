//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import ar.gov.rosario.siat.afi.buss.dao.AfiDAOFactory;
import ar.gov.rosario.siat.afi.iface.util.AfiError;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a Local - Totales por local de las Actividades 
 * declaradas para el Formulario de Declaración Jurada proveniente de AFIP.
 * 
 * @author tecso
 */
@Entity
@Table(name = "afi_local")
public class Local extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idForDecJur")
	private ForDecJur forDecJur;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name="idCuenta")
	private Cuenta cuenta;
	
	@Column(name = "numerocuenta")
	private String   numeroCuenta;
		
	@Column(name = "cantpersonal")
	private Integer cantPersonal;
	
	@Column(name = "feciniact")
	private Date fecIniAct;
	
	@Column(name = "feccesact")
	private Date  fecCesAct;
	
	@Column(name = "nombrefantasia")
	private String nombreFantasia;
	
	@Column(name = "centralizaingresos")
	private Integer centralizaIngresos;
	
	@Column(name = "paga") 
	private Integer paga;
		
	@Column(name = "contribetur")
	private Integer  contribEtur;
	
	@Column(name = "radio")
	private Integer radio;
		 
	@Column(name = "pagaminimo") 
	private Integer  pagaMinimo;
	
	@Column(name = "derdettot") 
	private Double  derDetTot;
	
	@Column(name = "minimogeneral") 
	private Double minimoGeneral;
	
	@Column(name = "derecho") 
	private Double  derecho;
	
	@Column(name = "alicuotapub") 
	private Double  alicuotaPub;
	
	@Column(name = "publicidad") 
	private Double  publicidad;
	
	@Column(name = "alicuotamesysil") 
	private Double  alicuotaMesYSil;
	
	@Column(name = "mesasysillas") 
	private Double   mesasYSillas;
	
	@Column(name = "subtotal1") 
	private Double   subTotal1;
	
	@Column(name = "otrospagos") 
	private Double   otrosPagos;
	
	@Column(name = "computado") 
	private Double   computado;
	
	@Column(name = "resto") 
	private Double	resto;
	
	@Column(name = "derechototal") 
	private Double   derechoTotal;
	
	@OneToMany(mappedBy="local")
	@JoinColumn(name="idlocal")
	private List<OtrosPagos> listOtrosPagos;
	
	@OneToMany(mappedBy="local")
	@JoinColumn(name="idlocal")
	@OrderBy("derechoDet desc")
	private List<DecActLoc> listDecActLoc;	

	@OneToMany(mappedBy="local")
	@JoinColumn(name="idlocal")
	private List<HabLoc> listHabLoc;	
	
	@OneToMany(mappedBy="local")
	@JoinColumn(name="idlocal")
	private List<ActLoc> listActLoc;
	
	@OneToMany(mappedBy="local")
	@JoinColumn(name="idlocal")
	private List<DatosPagoCta> listDatosPagoCta;
	
	@ManyToOne(fetch = FetchType.LAZY, optional= true)
	@JoinColumn(name="idDatosDomicilio")
	private DatosDomicilio datosDomicilio;
	
	// Constructores
	public Local(){
		super();
		// Seteo de valores default			
	}
	
	public Local(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static Local getById(Long id) {
		return (Local) AfiDAOFactory.getLocalDAO().getById(id);
	}
	
	public static Local getByIdNull(Long id) {
		return (Local) AfiDAOFactory.getLocalDAO().getByIdNull(id);
	}
	
	public static List<Local> getList() {
		return (ArrayList<Local>) AfiDAOFactory.getLocalDAO().getList();
	}
	
	public static List<Local> getListActivos() {			
		return (ArrayList<Local>) AfiDAOFactory.getLocalDAO().getListActiva();
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
	
	// Getters y setters
	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	
	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public Integer getCantPersonal() {
		return cantPersonal;
	}

	public void setCantPersonal(Integer cantPersonal) {
		this.cantPersonal = cantPersonal;
	}

	public Date getFecIniAct() {
		return fecIniAct;
	}

	public void setFecIniAct(Date fecIniAct) {
		this.fecIniAct = fecIniAct;
	}

	public Date getFecCesAct() {
		return fecCesAct;
	}

	public void setFecCesAct(Date fecCesAct) {
		this.fecCesAct = fecCesAct;
	}

	public String getNombreFantasia() {
		return nombreFantasia;
	}

	public void setNombreFantasia(String nombreFantasia) {
		this.nombreFantasia = nombreFantasia;
	}

	public Integer getCentralizaIngresos() {
		return centralizaIngresos;
	}

	public void setCentralizaIngresos(Integer centralizaIngresos) {
		this.centralizaIngresos = centralizaIngresos;
	}

	public Integer getContribEtur() {
		return contribEtur;
	}

	public void setContribEtur(Integer contribEtur) {
		this.contribEtur = contribEtur;
	}

	public Integer getRadio() {
		return radio;
	}

	public void setRadio(Integer radio) {
		this.radio = radio;
	}

	public List<OtrosPagos> getListOtrosPagos() {
		return listOtrosPagos;
	}

	public void setListOtrosPagos(List<OtrosPagos> listOtrosPagos) {
		this.listOtrosPagos = listOtrosPagos;
	}

	public List<DecActLoc> getListDecActLoc() {
		return listDecActLoc;
	}

	public void setListDecActLoc(List<DecActLoc> listDecActLoc) {
		this.listDecActLoc = listDecActLoc;
	}

	public DatosDomicilio getDatosDomicilio() {
		return datosDomicilio;
	}

	public void setDatosDomicilio(DatosDomicilio datosDomicilio) {
		this.datosDomicilio = datosDomicilio;
	}

	public Integer getPaga() {
		return paga;
	}

	public void setPaga(Integer paga) {
		this.paga = paga;
	}

	public Integer getPagaMinimo() {
		return pagaMinimo;
	}

	public void setPagaMinimo(Integer pagaMinimo) {
		this.pagaMinimo = pagaMinimo;
	}

	public Double getDerDetTot() {
		return derDetTot;
	}

	public void setDerDetTot(Double derDetTot) {
		this.derDetTot = derDetTot;
	}

	public Double getMinimoGeneral() {
		return minimoGeneral;
	}

	public void setMinimoGeneral(Double minimoGeneral) {
		this.minimoGeneral = minimoGeneral;
	}

	public Double getDerecho() {
		return derecho;
	}

	public void setDerecho(Double derecho) {
		this.derecho = derecho;
	}

	public ForDecJur getForDecJur() {
		return forDecJur;
	}

	public void setForDecJur(ForDecJur forDecJur) {
		this.forDecJur = forDecJur;
	}

	public Double getAlicuotaPub() {
		return alicuotaPub;
	}

	public void setAlicuotaPub(Double alicuotaPub) {
		this.alicuotaPub = alicuotaPub;
	}

	public Double getPublicidad() {
		return publicidad;
	}

	public void setPublicidad(Double publicidad) {
		this.publicidad = publicidad;
	}

	public Double getAlicuotaMesYSil() {
		return alicuotaMesYSil;
	}

	public void setAlicuotaMesYSil(Double alicuotaMesYSil) {
		this.alicuotaMesYSil = alicuotaMesYSil;
	}

	public Double getMesasYSillas() {
		return mesasYSillas;
	}

	public void setMesasYSillas(Double mesasYSillas) {
		this.mesasYSillas = mesasYSillas;
	}

	public Double getSubTotal1() {
		return subTotal1;
	}

	public void setSubTotal1(Double subTotal1) {
		this.subTotal1 = subTotal1;
	}

	public Double getOtrosPagos() {
		return otrosPagos;
	}

	public void setOtrosPagos(Double otrosPagos) {
		this.otrosPagos = otrosPagos;
	}

	public Double getComputado() {
		return computado;
	}

	public void setComputado(Double computado) {
		this.computado = computado;
	}

	public Double getResto() {
		return resto;
	}

	public void setResto(Double resto) {
		this.resto = resto;
	}

	public Double getDerechoTotal() {
		return derechoTotal;
	}

	public void setDerechoTotal(Double derechoTotal) {
		this.derechoTotal = derechoTotal;
	}

	public void setListActLoc(List<ActLoc> listActLoc) {
		this.listActLoc = listActLoc;
	}

	public List<HabLoc> getListHabLoc() {
		return listHabLoc;
	}

	public void setListHabLoc(List<HabLoc> listHabLoc) {
		this.listHabLoc = listHabLoc;
	}

	public List<ActLoc> getListActLoc() {
		return listActLoc;
	}

	public void setListActividadLocal(List<ActLoc> listActLoc) {
		this.listActLoc = listActLoc;
	}
	
	public List<DatosPagoCta> getListDatosPagoCta() {
		return listDatosPagoCta;
	}

	public void setListDatosPagoCta(List<DatosPagoCta> listDatosPagoCta) {
		this.listDatosPagoCta = listDatosPagoCta;
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
		if (StringUtil.isNullOrEmpty(getNumeroCuenta())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.LOCAL_NUMEROCUENTA);
		}
		
		if (SiNo.getEsValido(getCentralizaIngresos())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.LOCAL_CENTRALIZAINGRESOS);
		}
		
		if (StringUtil.isNullOrEmpty(getNombreFantasia())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.LOCAL_NOMBREFANTASIA);
		}

		if (SiNo.getEsValido(getContribEtur())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.LOCAL_CONTRIBETUR);
		}
		
		if (StringUtil.isNullOrEmpty(getNumeroCuenta())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.LOCAL_NUMEROCUENTA);
		}
		
		if (hasError()) {
			return false;
		}
		
//		// Validaciones de unique
//		UniqueMap uniqueMap = new UniqueMap();
//		uniqueMap.addString("codLocal");
//		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
//			addRecoverableError(BaseError.MSG_CAMPO_UNICO, AfiError.${BEAN}_COD${BEAN});			
//		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el Local. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		AfiDAOFactory.getLocalDAO().update(this);
	}

	/**
	 * Desactiva el Local. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		AfiDAOFactory.getLocalDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Local
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Local
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//	---> ABM HabLoc
	public HabLoc createHabLoc(HabLoc habLoc) throws Exception {

		// Validaciones de negocio
		if (!habLoc.validateCreate()) {
			return habLoc;
		}

		AfiDAOFactory.getHabLocDAO().update(habLoc);

		return habLoc;
	}
	
	public HabLoc updateHabLoc(HabLoc habLoc) throws Exception {
		
		// Validaciones de negocio
		if (!habLoc.validateUpdate()) {
			return habLoc;
		}

		AfiDAOFactory.getHabLocDAO().update(habLoc);
		
		return habLoc;
	}
	
	public HabLoc deleteHabLoc(HabLoc habLoc) throws Exception {
	
		// Validaciones de negocio
		if (!habLoc.validateDelete()) {
			return habLoc;
		}
		
		AfiDAOFactory.getHabLocDAO().delete(habLoc);
		
		return habLoc;
	}
	//	<--- ABM HabLoc
	
	//	---> ABM ActLoc
	public ActLoc createActLoc(ActLoc actLoc) throws Exception {

		// Validaciones de negocio
		if (!actLoc.validateCreate()) {
			return actLoc;
		}

		AfiDAOFactory.getActLocDAO().update(actLoc);

		return actLoc;
	}
	
	public ActLoc updateActLoc(ActLoc actLoc) throws Exception {
		
		// Validaciones de negocio
		if (!actLoc.validateUpdate()) {
			return actLoc;
		}

		AfiDAOFactory.getActLocDAO().update(actLoc);
		
		return actLoc;
	}
	
	public ActLoc deleteActLoc(ActLoc actLoc) throws Exception {
	
		// Validaciones de negocio
		if (!actLoc.validateDelete()) {
			return actLoc;
		}
		
		AfiDAOFactory.getActLocDAO().delete(actLoc);
		
		return actLoc;
	}
	//	<--- ABM ActLoc
	
	
	//	---> ABM DecActLoc
	public DecActLoc createDecActLoc(DecActLoc decActLoc) throws Exception {

		// Validaciones de negocio
		if (!decActLoc.validateCreate()) {
			return decActLoc;
		}

		AfiDAOFactory.getDecActLocDAO().update(decActLoc);

		return decActLoc;
	}
	
	public DecActLoc updateDecActLoc(DecActLoc decActLoc) throws Exception {
		
		// Validaciones de negocio
		if (!decActLoc.validateUpdate()) {
			return decActLoc;
		}

		AfiDAOFactory.getDecActLocDAO().update(decActLoc);
		
		return decActLoc;
	}
	
	public DecActLoc deleteDecActLoc(DecActLoc decActLoc) throws Exception {
	
		// Validaciones de negocio
		if (!decActLoc.validateDelete()) {
			return decActLoc;
		}
		
		AfiDAOFactory.getDecActLocDAO().delete(decActLoc);
		
		return decActLoc;
	}
	//	<--- ABM DecActLoc
	
	//	---> ABM DatosPagoCta
	public DatosPagoCta createDatosPagoCta(DatosPagoCta datosPagoCta) throws Exception {

		// Validaciones de negocio
		if (!datosPagoCta.validateCreate()) {
			return datosPagoCta;
		}

		AfiDAOFactory.getDatosPagoCtaDAO().update(datosPagoCta);

		return datosPagoCta;
	}
	
	public DatosPagoCta updateDatosPagoCta(DatosPagoCta datosPagoCta) throws Exception {
		
		// Validaciones de negocio
		if (!datosPagoCta.validateUpdate()) {
			return datosPagoCta;
		}

		AfiDAOFactory.getDatosPagoCtaDAO().update(datosPagoCta);
		
		return datosPagoCta;
	}
	
	public DatosPagoCta deleteDatosPagoCta(DatosPagoCta datosPagoCta) throws Exception {
	
		// Validaciones de negocio
		if (!datosPagoCta.validateDelete()) {
			return datosPagoCta;
		}
		
		AfiDAOFactory.getDatosPagoCtaDAO().delete(datosPagoCta);
		
		return datosPagoCta;
	}
	//	<--- ABM DatosPagoCta
	
	//	---> ABM OtrosPagos
	public OtrosPagos createOtrosPagos(OtrosPagos otrosPagos) throws Exception {

		// Validaciones de negocio
		if (!otrosPagos.validateCreate()) {
			return otrosPagos;
		}

		AfiDAOFactory.getOtrosPagosDAO().update(otrosPagos);

		return otrosPagos;
	}
	
	public OtrosPagos updateOtrosPagos(OtrosPagos otrosPagos) throws Exception {
		
		// Validaciones de negocio
		if (!otrosPagos.validateUpdate()) {
			return otrosPagos;
		}

		AfiDAOFactory.getOtrosPagosDAO().update(otrosPagos);
		
		return otrosPagos;
	}
	
	public OtrosPagos deleteOtrosPagos(OtrosPagos otrosPagos) throws Exception {
	
		// Validaciones de negocio
		if (!otrosPagos.validateDelete()) {
			return otrosPagos;
		}
		
		AfiDAOFactory.getOtrosPagosDAO().delete(otrosPagos);
		
		return otrosPagos;
	}
	//	<--- ABM OtrosPagos

	
}

	