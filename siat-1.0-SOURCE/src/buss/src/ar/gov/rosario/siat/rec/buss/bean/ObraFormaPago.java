//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Bean correspondiente a Obra Forma de Pago
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_obraformapago")
public class ObraFormaPago extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false) 
    @JoinColumn(name="idObra") 
	private Obra obra;
	
	@Column(name = "desFormaPago")
	private String desFormaPago;
	
	@Column(name = "cantCuotas")
	private Integer cantCuotas;
	 
	@Column(name = "montoMinimoCuota")
	private Double montoMinimoCuota;
	
	@Column(name = "descuento")
	private Double descuento;
	
	@Column(name = "interesFinanciero")
	private Double interesFinanciero;
	
	@Column(name = "esEspecial")
	private Integer esEspecial;  // Planes a utilizar en casos especiales, como Caso Social, Exencion, etc
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	@Column(name = "esCantCuotasFijas")
	private Integer esCantCuotasFijas; 
	
	@ManyToOne() 
    @JoinColumn(name="idExencion") 
	private Exencion exencion;
	
	// usado para contener la cantidad de cuotas a usar
	// en una forma de pago que se por ej hasta 100 cuotas
	@Transient
	private Integer cantCuotasVariables; 

	// Constructores
	public ObraFormaPago(){
		super();
		// Seteo de valores default	
	}
	
	public ObraFormaPago(Long id){
		super();
		setId(id);
	}
	
	/** crea una obraFormaPago a partir de
	 *  una obra y forma de pago
	 * 
	 * @param formaPago
	 */
	public ObraFormaPago(FormaPago formaPago, Double montoMinimoCuota, Double interesFinacioero) {
		super();
		this.setDesFormaPago(formaPago.getDesFormaPago());
		this.setCantCuotas(formaPago.getCantCuotas());
		this.setDescuento(formaPago.getDescuento());		
		this.setEsEspecial(formaPago.getEsEspecial());
		this.setEsCantCuotasFijas(formaPago.getEsCantCuotasFijas());
		this.setExencion(formaPago.getExencion());
		this.setInteresFinanciero(interesFinacioero*(1 - formaPago.getInteresFinanciero()));		
		this.setMontoMinimoCuota(montoMinimoCuota);
		this.setFechaDesde(new Date());
	}

	// Metodos de Clase
	public static ObraFormaPago getById(Long id) {
		return (ObraFormaPago) RecDAOFactory.getObraFormaPagoDAO().getById(id);
	}
	
	public static List<ObraFormaPago> getList() {
		return (ArrayList<ObraFormaPago>) RecDAOFactory.getObraFormaPagoDAO().getList();
	}

	public static List<ObraFormaPago> getListActivos() {			
		return (ArrayList<ObraFormaPago>) RecDAOFactory.getObraFormaPagoDAO().getListActiva();
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

	public Double getInteresFinanciero() {
		return interesFinanciero;
	}

	public void setInteresFinanciero(Double interesFinanciero) {
		this.interesFinanciero = interesFinanciero;
	}

	public Double getMontoMinimoCuota() {
		return montoMinimoCuota;
	}

	public void setMontoMinimoCuota(Double montoMinimoCuota) {
		this.montoMinimoCuota = montoMinimoCuota;
	}

	public Obra getObra() {
		return obra;
	}

	public void setObra(Obra obra) {
		this.obra = obra;
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

	public Integer getCantCuotasVariables() {
		return cantCuotasVariables;
	}

	public void setCantCuotasVariables(Integer cantCuotasVariables) {
		this.cantCuotasVariables = cantCuotasVariables;
	}
	
	public String getDesFormaPago() {
		return desFormaPago;
	}
	public void setDesFormaPago(String desFormaPago) {
		if (desFormaPago == null && getEsCantCuotasFijas() != null
				&& getDescuento() != null && getInteresFinanciero() != null) {
		
			//Generamos la descripcion de la Forma de Pago de la Obra
			this.desFormaPago = generarDescripcionObraFormaPago(this.getCantCuotas(), this.getEsCantCuotasFijas(), 
					this.getDescuento(), this.getInteresFinanciero(), this.getEsEspecial());
		}
		else 
			this.desFormaPago = desFormaPago;
	}

	/** Devuelve la cantidad de cuotas para las
	 *  cuales se deberan generar la emision, si es plan 
	 *  de cuotas fijas sera el valor de la cantidad
	 *  de cuotas, sino sera el valor guardado en CantCuotasVariables
	 *  que debera haber sido previamente guardado
	 * 
	 * @return
	 */
	public Integer getCantidadCuotasAGenerar(){
		Integer cantidadCuotasAGenerar = this.getCantCuotas();
		
		if (this.getCantCuotasVariables() != null) {
			cantidadCuotasAGenerar = this.getCantCuotasVariables();
		}
		
		return cantidadCuotasAGenerar;
	}
	
	// Validaciones 
	public boolean validateCreate() {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() {
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
	
		if (GenericDAO.hasReference(this, PlaCuaDet.class, "obraFormaPago")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				RecError.PLACUADET_LABEL , RecError.OBRAFORMAPAGO_LABEL);
		}
		if (GenericDAO.hasReference(this, HisCamPla.class, "obraFormaPago")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				RecError.HISCAMPLA_LABEL , RecError.OBRAFORMAPAGO_LABEL);
		}
		
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
	
	private boolean validate() {

		//	Validaciones
		if (getEsCantCuotasFijas() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRAFORMAPAGO_ESCANTCUOTASFIJAS);
		}

		if (getCantCuotas() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRAFORMAPAGO_CANTCUOTAS);
		}else if(getCantCuotas().intValue()<0)
			addRecoverableError(BaseError.MSG_VALORMENORQUECERO, RecError.OBRAFORMAPAGO_CANTCUOTAS);
		
		if(getMontoMinimoCuota() == null)
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRAFORMAPAGO_MONTOMINIMOCUOTA);
		
		if(getMontoMinimoCuota()!=null && getMontoMinimoCuota().intValue()<0)
			addRecoverableError(BaseError.MSG_VALORMENORQUECERO, RecError.OBRAFORMAPAGO_MONTOMINIMOCUOTA);
			
		if (getDescuento() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRAFORMAPAGO_DESCUENTO);
		}
		
		if (getInteresFinanciero() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRAFORMAPAGO_INTERESFINANCIERO );			
		}
		
		if (getEsEspecial() == null ) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRAFORMAPAGO_ESESPECIAL);
		}
		
		if (getFechaDesde() == null ) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRAFORMAPAGO_FECHADESDE);			
		}
		
		// si la forma de pago es especial debe ingresar una exencion
		if (getEsEspecial() != null && getEsEspecial().equals(SiNo.SI.getId()) && getExencion() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.EXENCION_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	public boolean hasInteres(){
		boolean hasInteres = false;
		if (this.getInteresFinanciero() != null &&
			this.getInteresFinanciero() != 0D) {
			hasInteres = true;
		}
		
		return hasInteres;
	}

	/** Determina si esta forma de pago es valida
	 *  para el monto total a pagar pasado como parametro,
	 *  en caso de ser cuotas variable setea en
	 * 
	 * @return
	 */
	public boolean isValidForMonto(Double montoTotal) {
	
		boolean isValid = false;
		Integer cantidadCuotas = this.getCantCuotas();
		Integer esCuotasFijas = this.getEsCantCuotasFijas();

		// calculo un total con el descuento correspondiente
		Double totalConDesc = montoTotal - (montoTotal * this.getDescuento());

		// si es cantidad de cuotas fijas verifico
		// si es valida para la cantidad de cuotas de esta forma de pago
		if (SiNo.SI.getId().equals(esCuotasFijas)) {
			isValid = this.isValid(totalConDesc, cantidadCuotas);
			this.setCantCuotasVariables(cantidadCuotas);
		}
	
		// si es cantidad de cuotas variables
		if (SiNo.NO.getId().equals(esCuotasFijas)) {
			// Obtengo la cuota desde la cual voy a iterar
			Integer cuotaDesde = this.obtenerUltimaCuota();

			// itero en orden descendente desde el numero de cuotas
			// de esta forma de pago, hasta el numero de cuotas de la inmediatamente inferior
			for (int i = cantidadCuotas ; cuotaDesde < i ; i--) {
				isValid = this.isValid(totalConDesc, i);
				
				// si es valido seteo la cantidad de cuotas y salgo
				if (isValid) {
					this.setCantCuotasVariables(i);
					break;
				}
			}
		}

		return isValid;

	}
	
	/** Determina si esta forma de pago es valida
	 *  para el monto y la cantidad de cuotas
	 *  pasadas como parametro
	 * 
	 * @param montoTotal
	 * @param numeroCuotas
	 * @return
	 */
	private boolean isValid(Double montoTotal, Integer numeroCuotas) {

		boolean isValid = false;
		Double importeCuota = null;
		// sino tiene interes calculo el importe directamente			
		if (!this.hasInteres()) {
			importeCuota = montoTotal / numeroCuotas;
		}
		
		// si tiene interes, llamo a una funcion que calcula el importe 
		// de la cuota
		if (this.hasInteres()) {
			importeCuota = Obra.calcularImporteCuota
				(montoTotal, numeroCuotas, this.getInteresFinanciero());
		}

		// si el importe de la cuota es mayor al monto
		// minimo es valida la forma de pago
		if (importeCuota > this.montoMinimoCuota) {
			isValid = true;
		}
		return isValid;
	}
	
	/** Obtiene el numero de la
	 *  ultima cuota de la forma de pago
	 *  anterior a esta. Ordenandolas en orden
	 *  descendente por cantidad de cuotas
	 * 
	 * @return
	 */
	public Integer obtenerUltimaCuota() {
		Integer ultimaCuota = null;
		List<ObraFormaPago> listObraFormaPago =
			RecDAOFactory.getObraFormaPagoDAO().getListByObraDes(this.getObra());
		
		for (ObraFormaPago obraFormaPago:listObraFormaPago) {
			if (obraFormaPago.getCantCuotas() < this.getCantCuotas()) {
				ultimaCuota = obraFormaPago.getCantCuotas();
			}
		}
		
		return ultimaCuota;
	}

    public String getEsEspecialView(){
		return SiNo.getById(this.getEsEspecial()).getValue();
	}

	public String generarDescripcionObraFormaPago(Integer cantCuotas, Integer esCantCuotasFijas,
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
							+ "% de interes";
		}
		
		if (descInteres == null || descInteres.equals(0D)) {
			descripcion += flagAnd == false ? "" :  ",";
			descripcion += " sin interes"; 
		}
		
		return descripcion;
	}	
    
}
