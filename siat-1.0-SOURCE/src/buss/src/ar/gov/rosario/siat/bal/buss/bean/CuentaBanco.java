//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Banco;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a CuentaBanco
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_cuentaBanco")
public class CuentaBanco extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "nroCuenta")
	private String nroCuenta;

	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="idBanco") 
    private Banco banco;

	@ManyToOne(fetch=FetchType.LAZY) 
    @JoinColumn(name="idArea") 
    private Area area;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idTipCueBan") 
	private TipCueBan tipCueBan;

	@Column(name = "observaciones")
	private String observaciones;

	//<#Propiedades#>
	
	// Constructores
	public CuentaBanco(){
		super();
		// Seteo de valores default			
	}
	
	public CuentaBanco(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static CuentaBanco getById(Long id) {
		return (CuentaBanco) BalDAOFactory.getCuentaBancoDAO().getById(id);
	}
	
	public static CuentaBanco getByIdNull(Long id) {
		return (CuentaBanco) BalDAOFactory.getCuentaBancoDAO().getByIdNull(id);
	}
	
	public static CuentaBanco getByNroCuenta(String nroCuenta) throws Exception {
		return (CuentaBanco) BalDAOFactory.getCuentaBancoDAO().getByNroCuenta(nroCuenta);
	}
	
	public static List<CuentaBanco> getList() {
		return (ArrayList<CuentaBanco>) BalDAOFactory.getCuentaBancoDAO().getList();
	}
	
	public static List<CuentaBanco> getListActivos() {			
		return (ArrayList<CuentaBanco>) BalDAOFactory.getCuentaBancoDAO().getListActiva();
	}
	
	public static List<CuentaBanco> getListActivasOrderByNro() throws Exception{			
		return (ArrayList<CuentaBanco>) BalDAOFactory.getCuentaBancoDAO().getListActivasOrderByNro();
	}
	
	/**
	 * Obtiene lista de CuentaBanco Activos para el Area especificada
	 * @author tecso
	 * @param Area area	
	 * @return List<CuentaBanco> 
	 */
	public static List<CuentaBanco> getListActivosByArea(Area area){			
		return (ArrayList<CuentaBanco>) BalDAOFactory.getCuentaBancoDAO().getListActivosByArea(area);
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
		
		//	Validaciones        
				
		if (getBanco()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.CUENTABANCO_BANCO);
		}
		if (getArea()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.CUENTABANCO_AREA);
		}
		
		if (getTipCueBan()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.CUENTABANCO_TIPCUEBAN);
		}
		
		if (hasError()) {
			return false;
		}
		
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el CuentaBanco. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getCuentaBancoDAO().update(this);
	}

	/**
	 * Desactiva el CuentaBanco. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getCuentaBancoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del CuentaBanco
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del CuentaBanco
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	
	// Getters y setters
	public String getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public TipCueBan getTipCueBan() {
		return tipCueBan;
	}

	public void setTipCueBan(TipCueBan tipCueBan) {
		this.tipCueBan = tipCueBan;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	//<#MetodosBeanDetalle#>
}
