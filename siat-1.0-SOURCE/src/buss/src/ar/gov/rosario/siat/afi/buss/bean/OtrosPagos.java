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
import javax.persistence.Table;

import ar.gov.rosario.siat.afi.buss.dao.AfiDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a OtrosPagos - Otros Pagos informados en el 
 * Formulario de Declaración Jurada proveniente de AFIP.
 * 
 * @author tecso
 */
@Entity
@Table(name = "afi_otrosPagos")
public class OtrosPagos extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name="idLocal")
	private Local local;
	
	@Column(name = "numerocuenta")
	private String  numeroCuenta;
	
	@Column(name = "tipopago")
	private Integer tipoPago;
	
	@Column(name = "fechapago")
	private Date  fechaPago;
	
	@Column(name = "periodopago")
	private Integer periodoPago;
	
	@Column(name = "nroresolucion")
	private String nroResolucion;
	
	@Column(name = "anio")
	private Integer  anio;
	
	@Column(name = "importepago")
	private Double  importePago;
		
	// Constructores
	public OtrosPagos(){
		super();
		// Seteo de valores default			
	}
	
	public OtrosPagos(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static OtrosPagos getById(Long id) {
		return (OtrosPagos) AfiDAOFactory.getOtrosPagosDAO().getById(id);
	}
	
	public static OtrosPagos getByIdNull(Long id) {
		return (OtrosPagos) AfiDAOFactory.getOtrosPagosDAO().getByIdNull(id);
	}
	
	public static List<OtrosPagos> getList() {
		return (ArrayList<OtrosPagos>) AfiDAOFactory.getOtrosPagosDAO().getList();
	}
	
	public static List<OtrosPagos> getListActivos() {			
		return (ArrayList<OtrosPagos>) AfiDAOFactory.getOtrosPagosDAO().getListActiva();
	}
	
	
	// Getters y setters

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public Integer getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(Integer tipoPago) {
		this.tipoPago = tipoPago;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public Local getLocal() {
		return local;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Integer getPeriodoPago() {
		return periodoPago;
	}

	public void setPeriodoPago(Integer periodoPago) {
		this.periodoPago = periodoPago;
	}

	public String getNroResolucion() {
		return nroResolucion;
	}

	public void setNroResolucion(String nroResolucion) {
		this.nroResolucion = nroResolucion;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Double getImportePago() {
		return importePago;
	}

	public void setImportePago(Double importePago) {
		this.importePago = importePago;
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
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
//		if (StringUtil.isNullOrEmpty(getCodOtrosPagos())) {
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_COD${BEAN} );
//		}
//		
//		if (StringUtil.isNullOrEmpty(getDesOtrosPagos())){
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_DES${BEAN});
//		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
//		UniqueMap uniqueMap = new UniqueMap();
//		uniqueMap.addString("codOtrosPagos");
//		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
//			addRecoverableError(BaseError.MSG_CAMPO_UNICO, AfiError.${BEAN}_COD${BEAN});			
//		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el OtrosPagos. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		AfiDAOFactory.getOtrosPagosDAO().update(this);
	}

	/**
	 * Desactiva el OtrosPagos. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		AfiDAOFactory.getOtrosPagosDAO().update(this);
	}
	
	/**
	 * Valida la activacion del OtrosPagos
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del OtrosPagos
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
}

	