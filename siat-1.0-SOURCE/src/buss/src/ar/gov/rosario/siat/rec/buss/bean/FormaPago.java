//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a Forma de Pago
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_formapago")
public class FormaPago extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idRecurso") 
	private Recurso recurso;

	@Column(name = "desFormaPago")
	private String desFormaPago;
	
	@Column(name = "esCantCuotasFijas")
	private Integer esCantCuotasFijas;  // indica si la cantidad de cuotas es fija o es un maximo solamente

	@Column(name = "cantCuotas")
	private Integer cantCuotas;

	@Column(name = "descuento")
	private Double descuento;

	@Column(name = "interesFinanciero")
	private Double interesFinanciero;

	@Column(name = "esEspecial")
	private Integer esEspecial;  // Planes a utilizar en casos especiales, como Caso Social, Exencion, etc

	@ManyToOne() 
    @JoinColumn(name="idexencion") 
	private Exencion exencion;

	// Constructores
	public FormaPago(){
		super();
		// Seteo de valores default	
	}
	
	public FormaPago(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static FormaPago getById(Long id) {
		return (FormaPago) RecDAOFactory.getFormaPagoDAO().getById(id);
	}
	
	public static List<FormaPago> getList() {
		return (ArrayList<FormaPago>) RecDAOFactory.getFormaPagoDAO().getList();
	}

	public static List<FormaPago> getListActivos() {			
		return (ArrayList<FormaPago>) RecDAOFactory.getFormaPagoDAO().getListActiva();
	}
	
	
	// Getters y setters
	public Integer getCantCuotas() {
		return cantCuotas;
	}

	public void setCantCuotas(Integer cantCuotas) {
		this.cantCuotas = cantCuotas;
	}

	public Double getDescuento() {
		return descuento;
	}

	public void setDescuento(Double descuento) {
		this.descuento = descuento;
	}

	public Integer getEsEspecial() {
		return esEspecial;
	}

	public void setEsEspecial(Integer esEspecial) {
		this.esEspecial = esEspecial;
	}

	public Double getInteresFinanciero() {
		return interesFinanciero;
	}

	public void setInteresFinanciero(Double interesFinanciero) {
		this.interesFinanciero = interesFinanciero;
	}
	
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Integer getEsCantCuotasFijas() {
		return esCantCuotasFijas;
	}

	public void setEsCantCuotasFijas(Integer esCantCuotasFijas) {
		this.esCantCuotasFijas = esCantCuotasFijas;
	}

	public Exencion getExencion() {
		return exencion;
	}

	public void setExencion(Exencion exencion) {
		this.exencion = exencion;
	}

	public String getDesFormaPago() {
		return desFormaPago;
	}
	
	public void setDesFormaPago(String desFormaPago) {
		
		if (desFormaPago == null && getEsCantCuotasFijas() != null
				&& getDescuento() != null && getInteresFinanciero() != null) {
		
			//Generamos la descripcion de la Forma de Pago
			this.desFormaPago = generarDescripcionFormaPago(this.getCantCuotas(), this.getEsCantCuotasFijas(), 
					this.getDescuento(), this.getInteresFinanciero(), this.getEsEspecial());
		}
		else 
			this.desFormaPago = desFormaPago;
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
	
	// Metodos de negocio
	
	/**
	 * Activa el ObraFormaPago. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		RecDAOFactory.getObraFormaPagoDAO().update(this);
	}

	/**
	 * Desactiva el ObraFormaPago. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		RecDAOFactory.getObraFormaPagoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del ObraFormaPago
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del ObraFormaPago
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones    
		if (getRecurso() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
		}
		
		if (getEsCantCuotasFijas() == null ) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.FORMAPAGO_ESCANTCUOTASFIJAS);
		}
		
		if (getCantCuotas() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.FORMAPAGO_CANTCUOTAS);
		}
		
		if (getCantCuotas() != null && getCantCuotas() < 0D) {
			addRecoverableError(BaseError.MSG_VALORMENORQUECERO, RecError.FORMAPAGO_CANTCUOTAS);
		}
		
		if (getDescuento() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.FORMAPAGO_DESCUENTO);
		}
		
		if (getDescuento() != null && ((getDescuento() < 0D) || (getDescuento() > 1D))) {
			addRecoverableError(BaseError.MSG_FORMATO_CAMPO_PORCENTAJE_INVALIDO, RecError.FORMAPAGO_DESCUENTO);
		}
		
		
		if (getInteresFinanciero() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.FORMAPAGO_INTERESFINANCIERO);			
		}
		
		if (getInteresFinanciero() != null && ((getInteresFinanciero() < 0D) || (getInteresFinanciero() > 1D))) {
			addRecoverableError(BaseError.MSG_FORMATO_CAMPO_PORCENTAJE_INVALIDO, RecError.FORMAPAGO_INTERESFINANCIERO);			
		}
		
		if (getEsEspecial() == null ) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.FORMAPAGO_ESESPECIAL);
		}

		// si la forma de pago es especial debe ingresar una exencion
		if (getEsEspecial() != null && getEsEspecial().equals(SiNo.SI.getId()) && getExencion() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.EXENCION_LABEL);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unicidad de la Forma de Pago
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addEntity("recurso");
		uniqueMap.addInteger("esCantCuotasFijas");
		uniqueMap.addInteger("cantCuotas");
		uniqueMap.addInteger("esEspecial");

		if (getExencion() != null)
			uniqueMap.addEntity("exencion");

		if (!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(RecError.FORMAPAGO_EXISTENTE);
		}

		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	public String generarDescripcionFormaPago(Integer cantCuotas, Integer esCantCuotasFijas,
			Double descuento, Double descInteres, Integer esEspecial) {
		String descripcion = "Plan";
		Boolean flagAnd = false;
		
		if (cantCuotas == 1) 
			descripcion +=" contado"; 
		else {
			descripcion += (esCantCuotasFijas == 1) ? " de " : " hasta ";
			descripcion += cantCuotas.toString() + " cuotas";
		}
		
		if (esEspecial != null)
			descripcion += esEspecial == 1 ? " especial" : ""; 
		
		if (descuento != null && ! descuento.equals(0D)) {
			descripcion += ", con el " + StringUtil.formatDouble(descuento * 100) + "% de descuento";
			flagAnd = true;
		}
		
		if (descInteres != null && ! descInteres.equals(0D)) {
			descripcion += flagAnd == false ? ", con el " :  " y "; 
			descripcion	+= StringUtil.formatDouble((descInteres * 100)) 
							+ "% de descuento sobre el inter\u00E9s de la obra";
		}
		
		if (descInteres == null || descInteres.equals(0D)) {
			descripcion += flagAnd == false ? "" :  ",";
			descripcion += " sin descuento sobre el inter\u00E9s de la obra"; 
		}
		
		return descripcion;
	}	    
	
    public String getEsCantCuotasFijasForReport(){
		return SiNo.getById(this.getEsCantCuotasFijas()).getValue();
	}
    
    public String getEsEspecialForReport(){
		return SiNo.getById(this.getEsEspecial()).getValue();
	}
	
}
