//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.bean;

import java.util.ArrayList;
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
 * Bean correspondiente a PagoCuenta - Datos de pago por Cuenta 
 * para el Formulario de Declaración Jurada proveniente de AFIP.
 * 
 * @author tecso
 */
@Entity
@Table(name = "afi_datospagocta")
public class DatosPagoCta extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name="idLocal")
	private Local local;	
	
	@Column(name = "numerocuenta")
	private String numeroCuenta;
	
	@Column(name = "idcuenta")
	private Long  idCuenta;
	 
	@Column(name = "codimpuesto")
	private Integer  codImpuesto;
	
	@Column(name = "totalmontoingresado")
	private Double totalMontoIngresado;
	
	// Constructores
	public DatosPagoCta(){
		super();
		// Seteo de valores default			
	}
	
	public DatosPagoCta(Long id){
		super();
		setId(id);
	}
		
	//Getters y Setters
	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public Local getLocal() {
		return local;
	}

	public Long getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(Long idCuenta) {
		this.idCuenta = idCuenta;
	}

	public Integer getCodImpuesto() {
		return codImpuesto;
	}

	public void setCodImpuesto(Integer codImpuesto) {
		this.codImpuesto = codImpuesto;
	}

	public Double getTotalMontoIngresado() {
		return totalMontoIngresado;
	}

	public void setTotalMontoIngresado(Double totalMontoIngresado) {
		this.totalMontoIngresado = totalMontoIngresado;
	}

	// Metodos de Clase
	public static DatosPagoCta getById(Long id) {
		return (DatosPagoCta) AfiDAOFactory.getDatosPagoCtaDAO().getById(id);
	}
	
	public static DatosPagoCta getByIdNull(Long id) {
		return (DatosPagoCta) AfiDAOFactory.getDatosPagoCtaDAO().getByIdNull(id);
	}
	
	public static List<DatosPagoCta> getList() {
		return (ArrayList<DatosPagoCta>) AfiDAOFactory.getDatosPagoCtaDAO().getList();
	}
	
	public static List<DatosPagoCta> getListActivos() {			
		return (ArrayList<DatosPagoCta>) AfiDAOFactory.getDatosPagoCtaDAO().getListActiva();
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
//		if (StringUtil.isNullOrEmpty(getCodPagoCuenta())) {
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_COD${BEAN} );
//		}
//		
//		if (StringUtil.isNullOrEmpty(getDesPagoCuenta())){
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_DES${BEAN});
//		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
//		UniqueMap uniqueMap = new UniqueMap();
//		uniqueMap.addString("codPagoCuenta");
//		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
//			addRecoverableError(BaseError.MSG_CAMPO_UNICO, AfiError.${BEAN}_COD${BEAN});			
//		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el PagoCuenta. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		AfiDAOFactory.getDatosPagoCtaDAO().update(this);
	}

	/**
	 * Desactiva el PagoCuenta. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		AfiDAOFactory.getDatosPagoCtaDAO().update(this);
	}
	
	/**
	 * Valida la activacion del PagoCuenta
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del PagoCuenta
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
		
}

	