//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

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

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Bean correspondiente a Compensacion
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_compensacion")
public class Compensacion extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "fechaAlta")
	private Date fechaAlta;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idEstadoCom") 
	private EstadoCom estadoCom;
	
	@Column(name = "idCaso")
	private String idCaso;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoCom") 
	private TipoCom tipoCom;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idArea") 
	private Area area;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idCuenta") 
	private Cuenta cuenta;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idCuentaBanco") 
	private CuentaBanco cuentaBanco;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idBalance") 
	private Balance balance;
	
	// Lista de Entidades Relacionadas con Archivo
	@OneToMany(mappedBy="compensacion")
	@JoinColumn(name="idCompensacion")
	private List<ComDeu> listComDeu;

	// Lista de Entidades Relacionadas con Archivo
	@OneToMany(mappedBy="compensacion")
	@JoinColumn(name="idCompensacion")
	private List<SaldoAFavor> listSaldoAFavor;

	//Constructores 
	public Compensacion(){
		super();
	}
	
	// Getters & Setters
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public Cuenta getCuenta() {
		return cuenta;
	}
	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	public CuentaBanco getCuentaBanco() {
		return cuentaBanco;
	}
	public void setCuentaBanco(CuentaBanco cuentaBanco) {
		this.cuentaBanco = cuentaBanco;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public EstadoCom getEstadoCom() {
		return estadoCom;
	}
	public void setEstadoCom(EstadoCom estadoCom) {
		this.estadoCom = estadoCom;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}
	public Balance getBalance() {
		return balance;
	}
	public void setBalance(Balance balance) {
		this.balance = balance;
	}
	public List<ComDeu> getListComDeu() {
		return listComDeu;
	}
	public void setListComDeu(List<ComDeu> listComDeu) {
		this.listComDeu = listComDeu;
	}
	public TipoCom getTipoCom() {
		return tipoCom;
	}
	public void setTipoCom(TipoCom tipoCom) {
		this.tipoCom = tipoCom;
	}
	public List<SaldoAFavor> getListSaldoAFavor() {
		return listSaldoAFavor;
	}
	public void setListSaldoAFavor(List<SaldoAFavor> listSaldoAFavor) {
		this.listSaldoAFavor = listSaldoAFavor;
	}

	// Metodos de clase	
	public static Compensacion getById(Long id) {
		return (Compensacion) BalDAOFactory.getCompensacionDAO().getById(id);
	}
	
	public static Compensacion getByIdNull(Long id) {
		return (Compensacion) BalDAOFactory.getCompensacionDAO().getByIdNull(id);
	}
		
	public static List<Compensacion> getList() {
		return (ArrayList<Compensacion>) BalDAOFactory.getCompensacionDAO().getList();
	}
	
	public static List<Compensacion> getListActivos() {			
		return (ArrayList<Compensacion>) BalDAOFactory.getCompensacionDAO().getListActiva();
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
		if(StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.COMPENSACION_DESCRIPCION);
		}
		if(getCuenta()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.CUENTA_LABEL);
		}
		if(getTipoCom()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.TIPOCOM_LABEL);
		}
		if(getFechaAlta()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.COMPENSACION_FECHAALTA);
		}
		if(getEstadoCom()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.ESTADOCOM_LABEL);
		}
		
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
		
		if (GenericDAO.hasReference(this, ComDeu.class, "compensacion")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.COMPENSACION_LABEL , BalError.COMDEU_LABEL);
		}

		if (GenericDAO.hasReference(this, FolCom.class, "compensacion")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.COMPENSACION_LABEL , BalError.FOLCOM_LABEL);
		}
		
		if (GenericDAO.hasReference(this, SaldoAFavor.class, "compensacion")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					BalError.COMPENSACION_LABEL , BalError.SALDOAFAVOR_LABEL);
		}
		
		if (GenericDAO.hasReference(this, HisEstCom.class, "compensacion")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					BalError.COMPENSACION_LABEL , BalError.HISESTCOM_LABEL);
		}
				
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
	// Administracion de ComDeu
	public ComDeu createComDeu(ComDeu comDeu) throws Exception {
		
		// Validaciones de negocio
		if (!comDeu.validateCreate()) {
			return comDeu;
		}

		BalDAOFactory.getComDeuDAO().update(comDeu);
		
		return comDeu;
	}	

	public ComDeu updateComDeu(ComDeu comDeu) throws Exception {
		
		// Validaciones de negocio
		if (!comDeu.validateUpdate()) {
			return comDeu;
		}

		BalDAOFactory.getComDeuDAO().update(comDeu);
		
		return comDeu;
	}	

	public ComDeu deleteComDeu(ComDeu comDeu) throws Exception {
		
		// Validaciones de negocio
		if (!comDeu.validateDelete()) {
			return comDeu;
		}
				
		BalDAOFactory.getComDeuDAO().delete(comDeu);
		
		return comDeu;
	}	
	
	// Administrar HisEstCom
	
	public HisEstCom createHisEstCom(HisEstCom hisEstCom) throws Exception {

		// Validaciones de negocio
		if (!hisEstCom.validateCreate()) {
			return hisEstCom;
		}

		BalDAOFactory.getHisEstComDAO().update(hisEstCom);

		return hisEstCom;
	}
	
	public HisEstCom updateHisEstCom(HisEstCom hisEstCom) throws Exception {

		// Validaciones de negocio

		BalDAOFactory.getHisEstComDAO().update(hisEstCom);

		return hisEstCom;
	}
	
	public HisEstCom deleteHisEstCom(HisEstCom hisEstCom) throws Exception {
		
		// Validaciones de negocio
		if (!hisEstCom.validateDelete()) {
			return hisEstCom;
		}
		
		BalDAOFactory.getHisEstComDAO().delete(hisEstCom);
		
		return hisEstCom;
	}
	
	/**
	 *  Cambia el Estado de la Compensacion al pasado, y crea un registro historico de cambio con la observacion indicada.
	 *
	 * @param idNuevoEstado
	 * @param observacion
	 * @throws Exception
	 */
	public void cambiarEstado(Long idNuevoEstado, String observacion) throws Exception{
		EstadoCom estadoCom = EstadoCom.getById(idNuevoEstado);
		
		HisEstCom hisEstCom = new HisEstCom();
		hisEstCom.setCompensacion(this);
		hisEstCom.setEstadoCom(estadoCom);
		hisEstCom.setObservaciones(observacion);
		hisEstCom.setFecha(new Date());
		String logCambios = "Estado Anterior: "+this.getEstadoCom().getDescripcion()
							+" , Estado Nuevo: "+estadoCom.getDescripcion()
							+" , Fecha de Cambio de Estado: "+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK);
		hisEstCom.setLogCambios(logCambios);
		this.createHisEstCom(hisEstCom);
		
		this.setEstadoCom(estadoCom);
		BalDAOFactory.getCompensacionDAO().update(this);
	}
	
}
