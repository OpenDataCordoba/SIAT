//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.ef.buss.bean.OrdenControl;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.ef.iface.model.TipoOrdenVO;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.CobranzaDetVO;
import ar.gov.rosario.siat.gde.iface.model.CobranzaVO;
import ar.gov.rosario.siat.gde.iface.model.EstadoCobranzaVO;
import ar.gov.rosario.siat.gde.iface.model.PerCobVO;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import ar.gov.rosario.siat.pad.buss.bean.Persona;
import ar.gov.rosario.siat.pad.iface.model.ContribuyenteVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Cobranza
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_cobranza")
public class Cobranza extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idContribuyente")
	private Contribuyente contribuyente;
	
	@Column(name="fechaInicio")
	private Date fechaInicio;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idOrdenControl")
	private OrdenControl ordenControl;
	
	@Column(name="idCaso")
	private String idCaso;
	
	@Column(name="fechaFin")
	private Date fechaFin;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idArea")
	private Area area;
	
	@Column
	private Double impIniPag;
	
	@Column(name="importeACobrar")
	private Double importeACobrar;
	
	@Column(name="impPagGes")
	private Double impPagGes;
	
	@Column(name="fechaResolucion")
	private Date fechaResolucion;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idEstadoCobranza")
	private EstadoCobranza estadoCobranza;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idPerCob")
	private PerCob perCob;
	
	@Column(name="proFecCon")
	private Date proFecCon;
	
	@Column(name="observacion")
	private String observacion;
	
	@OneToMany(mappedBy="cobranza", fetch=FetchType.LAZY)
	@JoinColumn(name="idCobranza")
	private List<CobranzaDet> listCobranzaDet;
	
	@OneToMany(mappedBy="cobranza", fetch=FetchType.LAZY)
	@JoinColumn(name="idCobranza")
	private List<GesCob> listGesCob;


	//<#Propiedades#>
	
	// Constructores
	public Cobranza(){
		super();
	}
	
	
	// Metodos de Clase
	public static Cobranza getById(Long id) {
		return (Cobranza) GdeDAOFactory.getCobranzaDAO().getById(id);
	}
	
	public static Cobranza getByIdNull(Long id) {
		return (Cobranza) GdeDAOFactory.getCobranzaDAO().getByIdNull(id);
	}
	
	public static List<Cobranza> getList() {
		return (List<Cobranza>) GdeDAOFactory.getCobranzaDAO().getList();
	}
	
	public static List<Cobranza> getListActivos() {			
		return (List<Cobranza>) GdeDAOFactory.getCobranzaDAO().getListActiva();
	}
	
	public static Cobranza getByOrdenControl(OrdenControl ordenControl){
		return GdeDAOFactory.getCobranzaDAO().getByOrdenControl(ordenControl);
	}
	
	
	// Getters y setters

	public Contribuyente getContribuyente() {
		return contribuyente;
	}


	public void setContribuyente(Contribuyente contribuyente) {
		this.contribuyente = contribuyente;
	}


	public Date getFechaInicio() {
		return fechaInicio;
	}


	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}


	public OrdenControl getOrdenControl() {
		return ordenControl;
	}


	public void setOrdenControl(OrdenControl ordenControl) {
		this.ordenControl = ordenControl;
	}


	public String getIdCaso() {
		return idCaso;
	}


	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}


	public Date getFechaFin() {
		return fechaFin;
	}


	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}


	public Area getArea() {
		return area;
	}


	public void setArea(Area area) {
		this.area = area;
	}


	public Double getImpIniPag() {
		return impIniPag;
	}


	public void setImpIniPag(Double impIniPag) {
		this.impIniPag = impIniPag;
	}


	public Double getImporteACobrar() {
		return importeACobrar;
	}


	public void setImporteACobrar(Double importeACobrar) {
		this.importeACobrar = importeACobrar;
	}


	public Double getImpPagGes() {
		return impPagGes;
	}


	public void setImpPagGes(Double impPagGes) {
		this.impPagGes = impPagGes;
	}


	public EstadoCobranza getEstadoCobranza() {
		return estadoCobranza;
	}


	public void setEstadoCobranza(EstadoCobranza estadoCobranza) {
		this.estadoCobranza = estadoCobranza;
	}


	public PerCob getPerCob() {
		return perCob;
	}


	public void setPerCob(PerCob perCob) {
		this.perCob = perCob;
	}


	public Date getProFecCon() {
		return proFecCon;
	}


	public void setProFecCon(Date proFecCon) {
		this.proFecCon = proFecCon;
	}


	public String getObservacion() {
		return observacion;
	}


	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}


	public List<CobranzaDet> getListCobranzaDet() {
		return listCobranzaDet;
	}


	public void setListCobranzaDet(List<CobranzaDet> listCobranzaDet) {
		this.listCobranzaDet = listCobranzaDet;
	}


	public List<GesCob> getListGesCob() {
		return listGesCob;
	}


	public void setListGesCob(List<GesCob> listGesCob) {
		this.listGesCob = listGesCob;
	}


	public Date getFechaResolucion() {
		return fechaResolucion;
	}


	public void setFechaResolucion(Date fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
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
		if (StringUtil.isNullOrEmpty(getDesEstadoConCuo())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.ESTADOCONCUO_DESESTADOCONCUO);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codEstadoConCuo");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, GdeError.ESTADOCONCUO_CODESTADOCONCUO);			
		}*/
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el EstadoConCuo. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getCobranzaDAO().update(this);
	}

	/**
	 * Desactiva el EstadoConCuo. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getCobranzaDAO().update(this);
	}
	
	/**
	 * Valida la activacion del EstadoConCuo
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones toVOForView
		return true;
	}
	
	/**
	 * Valida la desactivacion del EstadoConCuo
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//<#MetodosBeanDetalle#>
	public CobranzaVO toVOForView() throws Exception{
		CobranzaVO cobranzaVO = (CobranzaVO)this.toVO(false);
		for (CobranzaDet cobranzaDet : this.listCobranzaDet){
			CobranzaDetVO cobranzaDetVO = (CobranzaDetVO)cobranzaDet.toVO(1,false);
			cobranzaDetVO.getCuenta().setRecurso((RecursoVO)cobranzaDet.getCuenta().getRecurso().toVO(1,false));
			cobranzaVO.getListCobranzaDet().add(cobranzaDetVO);
		}
		
		cobranzaVO.setListGesCob(ListUtilBean.toVO(this.listGesCob, 1,false));
		Contribuyente contrib = Contribuyente.getByIdPersona(getContribuyente().getId());
		Persona persona = Persona.getByIdLight(contrib.getPersona().getId());
		cobranzaVO.setContribuyente((ContribuyenteVO)contrib.toVO(1,false));
		cobranzaVO.setEstadoCobranza((EstadoCobranzaVO)estadoCobranza.toVO());
		cobranzaVO.getContribuyente().setPersona((PersonaVO)persona.toVO());
		if(perCob!=null)
			cobranzaVO.setPerCob((PerCobVO)perCob.toVO());
		
		if (ordenControl!=null){
			cobranzaVO.setOrdenControl((OrdenControlVO)ordenControl.toVO(false));
			
			
			cobranzaVO.getOrdenControl().setTipoOrden((TipoOrdenVO)ordenControl.getTipoOrden().toVO(false));
		}
		
		return cobranzaVO;
		
	}
	
	public CobranzaVO toVOForViewLight() throws Exception{
		CobranzaVO cobranzaVO = (CobranzaVO)this.toVO(false);
		for (CobranzaDet cobranzaDet : this.listCobranzaDet){
			CobranzaDetVO cobranzaDetVO = (CobranzaDetVO)cobranzaDet.toVO(0,false);
			cobranzaDetVO.getCuenta().setRecurso((RecursoVO)cobranzaDet.getCuenta().getRecurso().toVO(1,false));
			cobranzaVO.getListCobranzaDet().add(cobranzaDetVO);
		}
		
		
		Contribuyente contrib = Contribuyente.getByIdPersona(getContribuyente().getId());
		Persona persona = Persona.getByIdLight(contrib.getPersona().getId());
		cobranzaVO.setContribuyente((ContribuyenteVO)contrib.toVO(1,false));
		cobranzaVO.setEstadoCobranza((EstadoCobranzaVO)estadoCobranza.toVO(0,false));
		//cobranzaVO.getContribuyente().setPersona((PersonaVO)persona.toVO());
		if(perCob!=null)
			cobranzaVO.setPerCob((PerCobVO)perCob.toVO(0,false));
		
		if (ordenControl!=null){
			cobranzaVO.setOrdenControl((OrdenControlVO)ordenControl.toVO(0,false));
			
			
			cobranzaVO.getOrdenControl().setTipoOrden((TipoOrdenVO)ordenControl.getTipoOrden().toVO(0,false));
		}
		
		return cobranzaVO;
		
	}
}
